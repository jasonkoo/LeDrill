package com.alipay.bluewhale.core.utils;

/**
 * storm�̻߳���
 * @author yannian
 *
 */
public interface SmartThread {
	 public void start();
	 public void join() throws InterruptedException;;
	 public void interrupt();
	 public Boolean isSleeping();
}
