package com.alipay.bluewhale.core.daemon;

import java.io.IOException;
import java.nio.channels.Channel;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.thrift7.protocol.TBinaryProtocol;
import org.apache.thrift7.server.THsHaServer;
import org.apache.thrift7.transport.TNonblockingServerSocket;

import backtype.storm.Config;
import backtype.storm.generated.Nimbus;
import backtype.storm.generated.Nimbus.Iface;
import backtype.storm.utils.BufferFileInputStream;
import backtype.storm.utils.TimeCacheMap;
import backtype.storm.utils.Utils;

import com.alipay.bluewhale.core.cluster.StormClusterState;
import com.alipay.bluewhale.core.cluster.StormConfig;
import com.alipay.bluewhale.core.schedule.CleanRunnable;
import com.alipay.bluewhale.core.schedule.MonitorRunnable;
import com.alipay.bluewhale.core.utils.PathUtils;

/**
 * NimbusServer��Ҫ�����¹�����
 * (1) �����ж��˵�topology
 *     ɾ����/storm-local-dir/stormdist�´��ڣ�zk��storm-zk-root/topologyid�����ڵ�topology�����Ϣ
 * (2) ��zk��storm-zk-root/storms�����е�topology��״̬����Ϊ����״̬��״̬�����StatusTransition
 * (3) ����һ���̣߳�ÿ���nimbus.monitor.reeq.secsʱ�佫zk��storm-zk-root/storms�����е�topology��״̬ת��Ϊmonitor״̬
 *     ת����monitor״̬��ʱ�򣬻���¼���ÿ��topology������������������Ƿ�����һ�εķ��������ͬ��������ڲ�ͬ�����滻
 * (4) ����һ���̣߳����nimbus.cleanup.inbox.freq.secsʱ������һ�ι��ڵ�jar��
 *
 */
public class NimbusServer {

	private static Logger LOG = Logger.getLogger(NimbusServer.class);

	/**
	 * Nimbus Server ����
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// ��ȡ�����ļ�
		@SuppressWarnings("rawtypes")
		Map config = Utils.readStormConfig();
		// launch-server
		launch_server(config);

	}

	@SuppressWarnings("rawtypes")
	private static void launch_server(Map conf) throws Exception {
		// 1����֤�Ƿ�ֲ�ʽģʽ
		NimbusUtils.validate_distributed_mode(conf);

		NimbusData data=service_handler(conf);

		
		// ���ö�ʱ�����߳�
		final ScheduledExecutorService scheduExec= data.getScheduExec();

		//Schedule Nimbus monitor
		MonitorRunnable r1 = new MonitorRunnable(data);
		
		int monitor_freq_secs = (Integer) conf.get(Config.NIMBUS_MONITOR_FREQ_SECS);
		scheduExec.scheduleAtFixedRate(r1, monitor_freq_secs, monitor_freq_secs,TimeUnit.SECONDS);
		
		//Schedule Nimbus inbox cleaner.����/nimbus/inbox�¹��ڵ�jar
		String dir_location=StormConfig.masterInbox(conf);
		int inbox_jar_expiration_secs=(Integer)conf.get(Config.NIMBUS_INBOX_JAR_EXPIRATION_SECS);
		CleanRunnable r2 = new CleanRunnable(dir_location,inbox_jar_expiration_secs);
		int cleanup_inbox_freq_secs = (Integer) conf.get(Config.NIMBUS_CLEANUP_INBOX_FREQ_SECS);
		scheduExec.scheduleAtFixedRate(r2, cleanup_inbox_freq_secs, cleanup_inbox_freq_secs,TimeUnit.SECONDS);

		//Thrift server���ü���������
		Integer thrift_port = (Integer) conf.get(Config.NIMBUS_THRIFT_PORT);
		TNonblockingServerSocket socket = new TNonblockingServerSocket(
				thrift_port);
		THsHaServer.Args args = new THsHaServer.Args(socket);
		args.workerThreads(64);
		args.protocolFactory(new TBinaryProtocol.Factory());
		final ServiceHandler service_handler = new ServiceHandler(data);
		args.processor(new Nimbus.Processor<Iface>(service_handler));
		final THsHaServer server = new THsHaServer(args);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				service_handler.shutdown();
				scheduExec.shutdown();
				server.stop();
			}

		});
		LOG.info("Starting BlueWhale server...");
		server.serve();
	}
	
	//for test
	@SuppressWarnings("rawtypes")
	public static NimbusData s_hander(Map conf) throws Exception{
	    return service_handler(conf);
	}
	
	@SuppressWarnings("rawtypes")
	private static NimbusData service_handler(Map conf) throws Exception {
		LOG.info("Starting BlueWhale with conf " + conf);

		TimeCacheMap.ExpiredCallback<Object, Object> expiredCallback = new TimeCacheMap.ExpiredCallback<Object, Object>() {
			@Override
			public void expire(Object key, Object val) {
				try {
					if (val!=null) {
						if(val instanceof Channel){
							Channel channel = (Channel) val;
							channel.close();		
						}else if(val instanceof BufferFileInputStream){
							BufferFileInputStream is=(BufferFileInputStream)val;
							is.close();
						}
					}
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				}	

			}
		};
		int file_copy_expiration_secs = (Integer) conf
				.get(Config.NIMBUS_FILE_COPY_EXPIRATION_SECS);
		TimeCacheMap<Object, Object> uploaders = new TimeCacheMap<Object, Object>(
				file_copy_expiration_secs, expiredCallback);
		TimeCacheMap<Object, Object> downloaders = new TimeCacheMap<Object, Object>(
				file_copy_expiration_secs, expiredCallback);

		// Callback callback=new TimerCallBack();
		// StormTimer timer=Timer.mkTimerTimer(callback);
		NimbusData data = new NimbusData(conf, downloaders, uploaders);

		// �����жϵ�topology
		cleanup_corrupt_topologies(data);

		// ��ȡzk�������topology id�б�
		List<String> active_ids = data.getStormClusterState().active_storms();

		if (active_ids != null){
		    
		    for (String topologyid : active_ids) {
			//�л�Ϊ :startup ״̬
			    StatusTransition.transition(data, topologyid, false, StatusType.startup);
		    }
		    
		}

		return data;

	}


	/**
	 * ������Ȼ��״̬��zookeeper���棬������nimbus����Ŀ¼�²����ڵ�topology
	 * 
	 * @param data
	 *            NimbusData
	 */
	private static void cleanup_corrupt_topologies(NimbusData data) {
		// ��ȡStormClusterState
		StormClusterState stormClusterState = data.getStormClusterState();
		// ��ȡnimbus�����ݴ洢Ŀ¼/nimbus/stormdist·��
		String master_stormdist_root = StormConfig.masterStormdistRoot(data
				.getConf());
		// ��ȡ/nimbus/stormdist·�������ļ����Ƽ���(topology id����)
		List<String> code_ids = PathUtils
				.read_dir_contents(master_stormdist_root);
		// ��ȡ��ǰZK������Ȼ����״̬��topology id����
		List<String> active_ids = data.getStormClusterState().active_storms();
		if (active_ids != null && active_ids.size() > 0) {
			if (code_ids != null) {
				// ��ȡ���ڱ���Ŀ¼�£�������Ȼ����zk�����topology id����
				active_ids.removeAll(code_ids);
			}
			for (String corrupt : active_ids) {
				LOG.info("Corrupt topology "
						+ corrupt
						+ " has state on zookeeper but doesn't have a local dir on Nimbus. Cleaning up...");
				// ִ������ZK����topology id
				stormClusterState.remove_storm(corrupt);
			}
		}

	}
	

}
