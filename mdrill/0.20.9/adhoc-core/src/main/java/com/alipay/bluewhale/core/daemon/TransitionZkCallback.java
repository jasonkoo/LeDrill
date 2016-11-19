package com.alipay.bluewhale.core.daemon;

import com.alipay.bluewhale.core.callback.RunnableCallback;


/**
 * zk��Ŀ¼�����ı��ʱ���ص�������Լ��������·���tasks
 *
 */
public class TransitionZkCallback extends RunnableCallback {

	//FIXME ��ʱʵ�ֽӿ�ArgsRunnable����ʱ����com.alipay.bluewhale.core.daemon����
	
	private NimbusData data;
	private String topologyid;
	
	public TransitionZkCallback(NimbusData data, String topologyid) {
		this.data=data;
		this.topologyid=topologyid;
	}

	@Override
	public void run() {
		StatusTransition.transition(data, topologyid, false, StatusType.monitor);
	}
}
