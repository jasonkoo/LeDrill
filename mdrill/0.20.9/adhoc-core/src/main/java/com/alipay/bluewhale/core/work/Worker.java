package com.alipay.bluewhale.core.work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import backtype.storm.Config;
import backtype.storm.daemon.Shutdownable;
import backtype.storm.generated.StormTopology;
import backtype.storm.serialization.KryoTupleSerializer;
import backtype.storm.task.TopologyContext;
import backtype.storm.utils.Utils;

import com.alipay.bluewhale.core.callback.RunnableCallback;
import com.alipay.bluewhale.core.cluster.Cluster;
import com.alipay.bluewhale.core.cluster.ClusterState;
import com.alipay.bluewhale.core.cluster.Common;
import com.alipay.bluewhale.core.cluster.StormClusterState;
import com.alipay.bluewhale.core.cluster.StormConfig;
import com.alipay.bluewhale.core.daemon.NodePort;
import com.alipay.bluewhale.core.messaging.IConnection;
import com.alipay.bluewhale.core.messaging.IContext;
import com.alipay.bluewhale.core.messaging.MsgLoader;
import com.alipay.bluewhale.core.task.Task;
import com.alipay.bluewhale.core.task.common.TaskShutdownDameon;
import com.alipay.bluewhale.core.utils.AsyncLoopThread;
import com.alipay.bluewhale.core.utils.PathUtils;
import com.alipay.bluewhale.core.utils.StormUtils;
import com.alipay.bluewhale.core.work.context.SystemContextMake;
import com.alipay.bluewhale.core.work.context.UserContextMake;
import com.alipay.bluewhale.core.work.refresh.RefreshActive;
import com.alipay.bluewhale.core.work.refresh.RefreshConnections;
import com.alipay.bluewhale.core.work.refresh.WorkerHeartbeatRunable;
import com.alipay.bluewhale.core.work.transfer.DrainerRunable;
import com.alipay.bluewhale.core.work.transfer.TransferData;
import com.alipay.bluewhale.core.work.transfer.WorkerTransfer;
import com.alipay.bluewhale.core.work.transfer.WorkerVirtualPort;

/**
 * worker��������
 * 
 * @author yannian
 * 
 */
public class Worker {

	private static Logger LOG = Logger.getLogger(Worker.class);
	// ϵͳ����
	@SuppressWarnings("rawtypes")
	private Map conf;
	// �û�����
	@SuppressWarnings("rawtypes")
	private Map stormConf;

	// ��Ϣ���ж���
	private IContext mqContext;
	private String topologyId;
	private String supervisorId;
	private Integer port;
	private String workerId;

	// ��ǰworker�Ƿ���active״̬
	private AtomicBoolean active;
	// ��zk�ж�ȡ�ĵ�ǰstorm�Ƿ���active״̬
	private AtomicBoolean zkActive;

	// zk���
	private ClusterState zkClusterstate;
	private StormClusterState zkCluster;
	
	
	private static StormClusterState shareCluster=null;
	
	public static StormClusterState getCluster()
	{
	   return  shareCluster;
	}
	
	public static Integer workerport=0;
	
	public static Integer getWorkPort()
	{
	    return workerport;
	}

	// �����˵�ǰworker������worker������
	private ConcurrentHashMap<NodePort, IConnection> nodeportSocket;
	// ������ĳ��taskid�����Ǹ�worker��ִ��
	private ConcurrentHashMap<Integer, NodePort> taskNodeport;
	// ����task��������Ϣ����������
	// ��ǰworkerִ�е�task�б�
	private Set<Integer> taskids;
	// ϵͳTopologyContext��maker����acker��
	private SystemContextMake systemContext;
	// �û�TopologyContext��maker,����acker
	private UserContextMake userContext;
	// ����ÿ��task��Ӧ��componentId
	private HashMap<Integer, String> tasksToComponent;
	// ���ϵͳ���֣����ͨ���˷������˳���ǰ����
	private final WorkerHaltRunable workHalt = new WorkerHaltRunable();

	//private ReentrantReadWriteLock endpoint_socket_lock=new ReentrantReadWriteLock();
	@SuppressWarnings("rawtypes")
	public Worker(Map conf, IContext mq_context, String topology_id,
			String supervisor_id, int port, String worker_id)
			throws Exception {
		
		LOG.info("Launching worker for " + topology_id + " on " + supervisor_id
				+ ":" + port + " with id " + worker_id + " and conf " + conf);

		this.conf = conf;
		this.mqContext = mq_context;
		this.topologyId = topology_id;
		this.supervisorId = supervisor_id;
		this.port = port;
		workerport=port;
		this.workerId = worker_id;

		this.active = new AtomicBoolean(true);
		this.zkActive = new AtomicBoolean();
		
		if (StormConfig.cluster_mode(conf).equals("distributed")) {
			PathUtils.touch(StormConfig.worker_pid_path(conf, worker_id,
					StormUtils.process_pid()));
		}
		
		// ������zk������
		this.zkClusterstate =Cluster.mk_distributed_cluster_state(conf);
		this.zkCluster = Cluster.mk_storm_cluster_state(zkClusterstate);
		shareCluster=this.zkCluster;

		this.stormConf = StormConfig.read_supervisor_storm_conf(conf, topology_id);

		// ����zeroMq����ZMQContext
		if (this.mqContext == null) {
			int zmqThreads = StormUtils.parseInt(stormConf.get(Config.ZMQ_THREADS));
			int linger = StormUtils.parseInt(stormConf.get(Config.ZMQ_LINGER_MILLIS));
			boolean isLocal = stormConf.get(Config.STORM_CLUSTER_MODE).equals("local");
			this.mqContext = MsgLoader.mk_zmq_context(zmqThreads, linger,isLocal);
		}

		this.nodeportSocket = new ConcurrentHashMap<NodePort, IConnection>();
		this.taskNodeport = new ConcurrentHashMap<Integer, NodePort>();
		this.tasksToComponent = Common.topology_task_info(zkCluster, topologyId);
		//��ǰworker��taskid�б�
		this.taskids = WorkCommon.readWorkerTaskids(zkCluster, topologyId,supervisorId, port);
		//�ӱ��ض�ȡsupervisorĿ¼�����л���topology�ļ�
		StormTopology topology = StormConfig.read_supervisor_topology(conf,	topology_id);
		// ����ϵͳtopology,�����acker
		this.systemContext = new SystemContextMake(topology, stormConf,
				topologyId, worker_id, tasksToComponent);
		// ������acker��StormTopology
		this.userContext = new UserContextMake(topology, stormConf, topologyId,
				worker_id, tasksToComponent);

	}

	private RefreshConnections makeRefreshConnections(Set<Integer> task_ids) {
		//���� ÿ��taskid��������tupple�п���������Щtasks��
		Set<Integer> outboundTasks = WorkCommon.worker_outbound_tasks(
				tasksToComponent, systemContext, task_ids);
		RefreshConnections refresh_connections = new RefreshConnections(active,
				conf, zkCluster, topologyId, outboundTasks, nodeportSocket,
				mqContext, taskNodeport);
		return refresh_connections;
	}

	public WorkerShutdown execute() throws Exception {

		
		

		// ִ�д�������˿ڶ���worker���շ��͹�����tuple,Ȼ�����task_id���ַ������ص����task(ͨ��zeromo�ı���ģʽ)
		WorkerVirtualPort virtual_port = new WorkerVirtualPort(conf,
				supervisorId, topologyId, port, mqContext, taskids);
		Shutdownable virtual_port_shutdown = virtual_port.launch();

		TopologyContext systemTopology = systemContext.make(null);

		// ˢ������
		RefreshConnections refreshConn = makeRefreshConnections(taskids);
		refreshConn.run();

		// ˢ��zk�е�active״̬
		RefreshActive refreshZkActive = new RefreshActive(active, conf,
				zkCluster, topologyId, zkActive);
		refreshZkActive.run();

		// ���������߳�
		RunnableCallback heartbeat_fn = new WorkerHeartbeatRunable(conf,
				workerId, port, topologyId, new CopyOnWriteArraySet<Integer>(taskids),
				active);
		heartbeat_fn.run();

		// ����worker����tuple�Ļ�����
		LinkedBlockingQueue<TransferData> transferQueue = new LinkedBlockingQueue<TransferData>();

		
		// ������Ϣtuple�����̣߳���task���뵽transfer_queue�е�Tuple���͵�Ŀ��work
		KryoTupleSerializer serializer = new KryoTupleSerializer(stormConf,systemTopology);
		
		// ������������task������Ϣ�Ķ��У�task���͵���Ϣ����ʱ�洢����s��
		WorkerTransfer workerTransfer = new WorkerTransfer(serializer, transferQueue);

		// ����ֹͣ���̻ص�����
		List<TaskShutdownDameon> shutdowntasks = new ArrayList<TaskShutdownDameon>();
		// ����task
		if (taskids != null) {
			for (int taskid : taskids) {

				TaskShutdownDameon t = Task.mk_task(conf, stormConf,
						systemContext.make(taskid), userContext.make(taskid),
						topologyId, mqContext, zkClusterstate, zkActive,
						workerTransfer, workHalt);

				shutdowntasks.add(t);
			}
		}

		//FIXME �����̳߳ز���
		// worker���������ĸ��߳�
		AsyncLoopThread refreshconn = new AsyncLoopThread(refreshConn);

		AsyncLoopThread refreshzk = new AsyncLoopThread(refreshZkActive);

		AsyncLoopThread hb = new AsyncLoopThread(heartbeat_fn, false, null,
				Thread.MAX_PRIORITY, true);

		AsyncLoopThread dr = new AsyncLoopThread(new DrainerRunable(transferQueue, nodeportSocket, taskNodeport));

		AsyncLoopThread[] threads = { refreshconn, refreshzk, hb, dr };


		return new WorkerShutdown(shutdowntasks, active, nodeportSocket,
				virtual_port_shutdown, mqContext, threads, zkCluster,
				zkClusterstate);

	}

	/**
	 * ���� work���̲���
	 * 
	 * @param conf
	 * @param mq_context
	 * @param topology_id
	 * @param supervisor_id
	 * @param port
	 * @param worker_id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static WorkerShutdown mk_worker(Map conf, IContext mq_context,
			String topology_id, String supervisor_id, int port,
			String worker_id) throws Exception {
		Worker w = new Worker(conf, mq_context, topology_id, supervisor_id, port,
				worker_id);
		return w.execute();
	}

	/**
	 * work����������
	 * 
	 * @param args
	 */
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		if (args.length != 4) {
			LOG.error("the length of args is less than 4");
			return;
		}
		String topology_id = args[0];
		String supervisor_id = args[1];
		String port_str = args[2];
		String worker_id = args[3];

		Map conf = Utils.readStormConfig();
		// ����Ƿֲ�ʽģʽ ���˳�
		Common.validate_distribute_mode(conf);
		try {
			WorkerShutdown sd = mk_worker(conf, null, topology_id,
					supervisor_id, Integer.parseInt(port_str), worker_id);
			sd.join();
			LOG.info("WorkerShutdown topology_id=" + topology_id + ",port_str="
					+ port_str);
		}  catch (Throwable e) {
			LOG.error("make worker error" ,e);
			LOG.info("WorkerShutdown topology_id=" + topology_id + ",port_str="
					+ port_str);
			System.exit(0);

		}
	}

}
