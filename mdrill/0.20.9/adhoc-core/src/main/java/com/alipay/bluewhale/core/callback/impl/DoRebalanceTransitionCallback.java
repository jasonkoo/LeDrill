package com.alipay.bluewhale.core.callback.impl;

import org.apache.log4j.Logger;

import com.alipay.bluewhale.core.callback.BaseCallback;
import com.alipay.bluewhale.core.cluster.StormStatus;
import com.alipay.bluewhale.core.daemon.NimbusData;
import com.alipay.bluewhale.core.daemon.NimbusUtils;

/**
 * topology ״̬Ϊ rebalancing��ʱ��  ִ��do_rebalance�����·���topology�����񣬲����ü�Ⱥ״̬Ϊoldstatus
 *
 */
public class DoRebalanceTransitionCallback extends BaseCallback {

	private static Logger LOG = Logger.getLogger(DoRebalanceTransitionCallback.class);
	
	private NimbusData data;
	private String topologyid;
	private StormStatus oldStatus;
	
	public DoRebalanceTransitionCallback(NimbusData data, String topologyid,
			StormStatus status) {
		this.data=data;
		this.topologyid=topologyid;
		this.oldStatus=status;
	}

	@Override
	public <T> Object execute(T... args) {
		try {
			NimbusUtils.mkAssignments(data, topologyid, true);
		} catch (Exception e) {
			LOG.error("do-rebalance error!", e);
		}
		//FIXME Why oldStatus?
		return oldStatus.getOldStatus();
	}

}
