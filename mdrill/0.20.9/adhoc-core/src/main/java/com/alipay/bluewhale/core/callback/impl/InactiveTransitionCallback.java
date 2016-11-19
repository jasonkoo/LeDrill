package com.alipay.bluewhale.core.callback.impl;

import com.alipay.bluewhale.core.callback.BaseCallback;
import com.alipay.bluewhale.core.cluster.StormStatus;
import com.alipay.bluewhale.core.daemon.StatusType;

/**
 * 
 * * ��topology��״̬����Ϊinactive״̬
 */
public class InactiveTransitionCallback extends BaseCallback {

	@Override
	public <T> Object execute(T... args) {
		
		return new StormStatus(StatusType.inactive);
	}

}
