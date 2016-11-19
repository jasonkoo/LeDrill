package com.alipay.bluewhale.core.daemon.supervisor;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import backtype.storm.utils.LocalState;
import backtype.storm.utils.Utils;

import com.alipay.bluewhale.core.cluster.Cluster;
import com.alipay.bluewhale.core.cluster.Common;
import com.alipay.bluewhale.core.cluster.StormClusterState;
import com.alipay.bluewhale.core.cluster.StormConfig;
import com.alipay.bluewhale.core.event.EventManager;
import com.alipay.bluewhale.core.event.EventManagerImp;
import com.alipay.bluewhale.core.messaging.ZMQContext;
import com.alipay.bluewhale.core.utils.AsyncLoopThread;
import com.alipay.bluewhale.core.utils.NetWorkUtils;
import com.alipay.bluewhale.core.utils.SmartThread;
import com.alipay.bluewhale.core.utils.TimeUtils;

/**
 * supervisor ��Ҫ��������¼���������
 * 1. ��zk��д���Լ�������supervisorInfo. Ŀǰû���ڼ��
 * 2. ����һ���̣߳�ÿ��10sִ��SynchronizeSupervisor��
 *    (1) ����Ҫ���صĴ��롢jar���������ļ���
 *    (2) ɾ����Щû�б�nimbus���������topology��Դ��
 *    (3) ��nimbus���䵽��supervisor������д�뵽/storm-local-dir/supervisor/localstate�У�
 *        LocalState���Կ�����һ��KV database;
 *    (4) ��syncProcesses��ӵ���Ӧ���¼������С�
 * 3. ����һ���̣߳�ÿ��supervisor.monitor.frequency.secsִ��SyncProcesses��
 *    (1) �ر������쳣(�����ڣ���ʱ)��worker��
 *    (2) �ҳ���Ҫ������worker��������
 */

public class Supervisor {

	private static Logger LOG = Logger.getLogger(Supervisor.class);

//	public String generateSupervisorId() {
//		return StormUtils.uuid().toString();// util.clj
//	}

	/**
	 * ����һ��supervisor
	 * 
	 * @param conf�� ������Ϣ��default.yaml storm.yaml
	 * @param sharedContext�� Ŀǰ����ʱ����Ϊ null
	 * @return SupervisorManger: �ɹر�supervisor�������е�worker     
	 */
	public SupervisorManger mkSupervisor(Map conf, ZMQContext sharedContext)
			throws Exception {

		LOG.info("Starting Supervisor with conf " + conf);
		/**
		 * Step 1: cleanup all files in supervisor_tmp_dir
		 */
		//���/storm-local-dir/supervisor/tmp �µ������ļ�
		String path = StormConfig.supervisorTmpDir(conf);
		FileUtils.cleanDirectory(new File(path));

		AtomicBoolean active = new AtomicBoolean(true);

		int startTimeStamp = TimeUtils.current_time_secs();
		
		//Step 2, get hostname 
		//���������
		String myHostName = NetWorkUtils.local_hostname();

		ConcurrentHashMap<String, String> workerThreadPids = new ConcurrentHashMap<String, String>();

		/*
		 * Step 3: mk_storm_cluster_state register supervisor stats in zookeeper
		 * 3.1 connection zk 
		 * 3.2 register watcher of zk 
		 * 3.3 register all kinds of callbacks 
		 * 3.4 create znode
		 */
		//�ɲ���zookeeper��ʵ��
		StormClusterState stormClusterState = Cluster
				.mk_storm_cluster_state(conf);

		/*
		 * Step 4, create LocalStat LocalStat is one KV database 
		 * 4.1 create LocalState instance 
		 * 4.2 get or put supervisorId
		 */
		//��supervisoridд�뱾��
		LocalState localState = StormConfig.supervisorState(conf);

		String supervisorId = (String) localState.get(Common.LS_ID);
		if (supervisorId == null) {
			supervisorId = UUID.randomUUID().toString();
			localState.put(Common.LS_ID, supervisorId);
		}

		/*
		 * Step 5, create event manager EventManger create one thread to handle
		 * event get one event from queue, then execute it
		 */
		/*
		 * EventManager ���������ʱ�򣬻�����һ���������̻߳�ѭ�����¼�������ȡ��һ��ʱ�䣬��ִ�У�
		 * eventManager����SynchronizeSupervisor��processesEventManager����SyncProcesses��
		*/
		
		EventManager eventManager = new EventManagerImp(false);
		EventManager processesEventManager = new EventManagerImp(false);

		//Step 6, create syncProcess
		 
		//SyncProcesses���ر������쳣(�����ڣ���ʱ)��worker���ҳ���Ҫ������worker��������
		SyncProcesses syncProcesses = new SyncProcesses(supervisorId, conf,
				localState, workerThreadPids, sharedContext);

		
		//Step 7, create synchronizeSupervisor

		/*    SynchronizeSupervisor
		 *    (1) ����Ҫ���صĴ��롢jar���������ļ���
                 *    (2) ɾ����Щû�б�nimbus���������topology��Դ��
                 *    (3) ��nimbus���䵽��supervisor������д�뵽/storm-local-dir/supervisor/localstate�У�
                 *        LocalState���Կ�����һ��KV database;
                 *    (4) ��syncProcesses��ӵ���Ӧ���¼������С�
		 */
		SynchronizeSupervisor synchronizeSupervisor = new SynchronizeSupervisor(
				supervisorId, conf, eventManager, processesEventManager,
				stormClusterState, localState, syncProcesses);

		
		//Step 8: create HeartBeat
		
		//��zk��д��һ��supervisor��������Ϣ
		Heartbeat hb = new Heartbeat(conf, stormClusterState, supervisorId,
				myHostName, startTimeStamp);
		hb.run();

		//Step 9: start the threads

		Vector<SmartThread> threads = new Vector<SmartThread>();

		
		//Step 9.1 start heartbeat thread
		
		//����һ���̣߳�ÿ��supervisor.heartbeat.frequency.secsд��һ��supervisor������Ϣ
		AsyncHeartbeat asyncHB = new AsyncHeartbeat(conf, hb, active);
		AsyncLoopThread heartbeat = new AsyncLoopThread(asyncHB, false, null,
				Thread.MAX_PRIORITY, true);
		threads.add(heartbeat);
		
		//Step 9.2 start sync Supervisor thread

		//����һ���̣߳�ÿ��10��ִ��SynchronizeSupervisor
		AsyncSynchronizeSupervisor syncSupervisor = new AsyncSynchronizeSupervisor(
				eventManager, synchronizeSupervisor, active);
		AsyncLoopThread syncsThread = new AsyncLoopThread(syncSupervisor);
		threads.add(syncsThread);


		// Step 9.3 start sync process thread

		//����һ���̣߳�ÿ��supervisor.monitor.frequency.secs��ִ��SyncProcesses
		AsyncSyncProcesses syncProcess = new AsyncSyncProcesses(conf,
				processesEventManager, syncProcesses, active);
		AsyncLoopThread syncpThread = new AsyncLoopThread(syncProcess, false,
				null, Thread.MAX_PRIORITY, true);
		threads.add(syncpThread);

		LOG.info("Starting supervisor with id " + supervisorId + " at host "
				+ myHostName);

		//SupervisorManger�� �ɹر�supervisor�������е�worker
		return new SupervisorManger(conf, supervisorId, active, threads,
				processesEventManager, eventManager, stormClusterState,
				workerThreadPids);
	}

	/**
	 * �ر�supervisor 
	 * @param supervisor
	 */
	public void killSupervisor(SupervisorManger supervisor) {
		supervisor.shutdown();
	}

	/**
	 * ����supervisor
	 */
	public void run() {

		SupervisorManger supervisorManager = null;
		try {
			Map conf = Utils.readStormConfig();

			Common.validate_distribute_mode(conf);

			supervisorManager = mkSupervisor(conf, null);

		} catch (Exception e) {
			LOG.error("Failed to start supervisor\n", e);
			System.exit(1);
		}

		try {
                        
			Thread.currentThread().join();

		} catch (InterruptedException e) {
			LOG.info("Begin to shutdown supervisor");

			supervisorManager.ShutdownAllWorkers();

			supervisorManager.shutdown();

			LOG.info("Successfully shutdown supervisor");

		}

	}

	/**
	 * supervisor daemon enter entrance
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Supervisor instance = new Supervisor();

		instance.run();

	}

}