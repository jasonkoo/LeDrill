package com.alipay.bluewhale.core.daemon;
/**
 * topology��״̬��
 * ��Ҫ�־û���״̬��д��zk�еģ�  active��inactive��killed��rebalancing
 * �ı�״̬�Ĳ���:startup��monitor��inactivate��activate��rebalance��do_rebalance��kill
 *             ÿ���ı�״̬����Ӧһ������
 * ���磺��Ҫ��active��״̬��Ϊinactive״̬����Ҫ�ҵ�inactivate��Ӧ�Ĳ�����
 * 
 */
public enum StatusType {

	kill(":kill"),
	killed(":killed"),
	monitor(":monitor"),
	inactive(":inactive"),
	inactivate(":inactivate"),
	active(":active"),
	activate(":activate"),
	startup(":startup"),
	remove(":remove"),
	rebalance(":rebalance"),
	rebalancing(":rebalancing"),
	do_rebalance(":do-rebalance");

	private String status;
	StatusType(String status) {
		this.status=status;
	}
	
	//��ȡ��Ӧ�ַ���״̬
	public String getStatus(){
		return status;		
	}
}
