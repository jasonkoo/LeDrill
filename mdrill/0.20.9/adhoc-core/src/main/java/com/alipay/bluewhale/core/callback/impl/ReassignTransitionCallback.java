package com.alipay.bluewhale.core.callback.impl;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import backtype.storm.Config;

import com.alipay.bluewhale.core.callback.BaseCallback;
import com.alipay.bluewhale.core.daemon.NimbusData;
import com.alipay.bluewhale.core.daemon.NimbusUtils;

/**
 * monitorÿ�ε��ô˷��������쳣���������·���
 *
 */
public class ReassignTransitionCallback extends BaseCallback {

	private static Logger LOG = Logger.getLogger(ReassignTransitionCallback.class);
	
	private NimbusData data;
	private String topologyid;
	
	public ReassignTransitionCallback(NimbusData data, String topologyid) {
		this.data=data;
		this.topologyid=topologyid;
	}

	@Override
	public <T> Object execute(T... args) {
		
		Map<?, ?> conf = data.getConf();
		if ((Boolean) conf.get(Config.NIMBUS_REASSIGN)) {
			try {
				NimbusUtils.mkAssignments(data, topologyid);
			} catch (IOException e) {
				LOG.error("mkAssignments failure! ", e);
			}
		}
		//����Ҫ����״ֱ̬�ӷ���null
		return null;
	}

}
