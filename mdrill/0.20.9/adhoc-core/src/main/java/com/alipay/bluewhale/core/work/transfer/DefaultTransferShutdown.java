package com.alipay.bluewhale.core.work.transfer;

import org.apache.log4j.Logger;

import backtype.storm.daemon.Shutdownable;

/**
 * Ĭ�ϵ�shutdownʵ��
 * 
 * @author yannian
 * 
 */
public class DefaultTransferShutdown implements Shutdownable {
	private final static Logger LOG = Logger
			.getLogger(DefaultTransferShutdown.class);

	@Override
	public void shutdown() {
		LOG.info("DefaultTransferShutdown");
	}

}
