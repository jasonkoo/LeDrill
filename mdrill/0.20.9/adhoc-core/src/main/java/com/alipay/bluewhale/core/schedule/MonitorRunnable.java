package com.alipay.bluewhale.core.schedule;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import backtype.storm.utils.TimeCacheMap;

import com.alipay.bluewhale.core.cluster.StormClusterState;
import com.alipay.bluewhale.core.cluster.StormConfig;
import com.alipay.bluewhale.core.daemon.NimbusData;
import com.alipay.bluewhale.core.daemon.StatusTransition;
import com.alipay.bluewhale.core.daemon.StatusType;
import com.alipay.bluewhale.core.utils.PathUtils;

public class MonitorRunnable implements Runnable {
	private static Logger LOG = Logger.getLogger(MonitorRunnable.class);

	private NimbusData data;
	private AtomicLong index=new AtomicLong(0l);

	public MonitorRunnable(NimbusData data) {
		this.data = data;
	}

	TimeCacheMap<String, Long> lastneedClean=new TimeCacheMap<String, Long>(3600);
	@Override
	public void run() {
		StormClusterState clusterState = data.getStormClusterState();

		List<String> active_topologys = clusterState.active_storms();
		if (active_topologys != null){
		    for (String topologyid : active_topologys) {
			StatusTransition.transition(data, topologyid, false, StatusType.monitor);
		    }
		}
		
		long times=index.incrementAndGet();
		if(times%10!=0)
		{
			return ;
		}
		if(times>10000000)
		{
			index.set(0l);
		}


		// �̰߳�ȫ��������submitTopology��transition״̬ʱ
		synchronized (data.getSubmitLock()) {
			Set<String> to_cleanup_ids = do_cleanup(clusterState,active_topologys);
			if (to_cleanup_ids != null && to_cleanup_ids.size() > 0) {
				for (String storm_id : to_cleanup_ids) {
					if(!lastneedClean.containsKey(storm_id))
					{
					    LOG.info("nexttime Cleaning up " + storm_id);
					    lastneedClean.put(storm_id, System.currentTimeMillis() );
						continue;
					}
				    LOG.info("Cleaning up " + storm_id);
					clusterState.teardown_heartbeats(storm_id);
					clusterState.teardown_task_errors(storm_id);
					// ��ȡĿ¼ /nimbus/stormdist/topologyid
					String master_stormdist_root = StormConfig
							.masterStormdistRoot(data.getConf(), storm_id);
					try {
						// ǿ��ɾ��topologyid��Ӧ��Ŀ¼
						PathUtils.rmr(master_stormdist_root);
					} catch (IOException e) {
					    LOG.error("ǿ��ɾ��Ŀ¼" + master_stormdist_root + "����!", e);
					}
					// (swap! (:task-heartbeats-cache nimbus) dissoc id))
					data.getTaskHeartbeatsCache().remove(storm_id);

				}
			}
		}
	    
	}

	/**
	 * ��ȡ��Ҫ����� storm id�б�
	 * @param clusterState
	 * @return
	 */
	private Set<String> do_cleanup(StormClusterState clusterState,List<String> active_topologys) {
		
		List<String> heartbeat_ids =clusterState.heartbeat_storms();
		List<String> error_ids = clusterState.task_error_storms();

		String master_stormdist_root = StormConfig.masterStormdistRoot(data.getConf());
		// ��ȡ/nimbus/stormdist·�������ļ����Ƽ���(topology id����)
		List<String> code_ids = PathUtils.read_dir_contents(master_stormdist_root);

//		Set<String> assigned_ids = StormUtils.listToSet(clusterState.active_storms());//��β���
		Set<String> to_cleanup_ids = new HashSet<String>();
		if (heartbeat_ids != null){
		    to_cleanup_ids.addAll(heartbeat_ids);
		}
		if (error_ids != null){
		    to_cleanup_ids.addAll(error_ids);
		}
		if (code_ids != null){
		    to_cleanup_ids.addAll(code_ids);
		}
                if (active_topologys != null){
		    to_cleanup_ids.removeAll(active_topologys);
                }
		return to_cleanup_ids;
	}

}
