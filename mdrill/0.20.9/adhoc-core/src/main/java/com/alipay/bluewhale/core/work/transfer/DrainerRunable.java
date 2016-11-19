package com.alipay.bluewhale.core.work.transfer;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.alipay.bluewhale.core.callback.RunnableCallback;
import com.alipay.bluewhale.core.daemon.NodePort;
import com.alipay.bluewhale.core.messaging.IConnection;

/**
 * ���ڽ�transfer_queue�������Ϣ�����͸�Ŀ��worker
 * 
 * ֮ǰһֱ�Ƚϵ���->worker���͵���Ϣ����һ�����߳��з��ͣ����ĳһ���̷߳��й����г������ⱻ����������ͻᱻ����ס
 * ��zeromq�Ĳ��Է��֣�����ʹ�õ����첽�ķ�ʽ ��һ��ֱ��connectһ��tcp�Ķ˿ڣ�ʵ��û���κν��ն˰󶨣�Ȼ������Ϣ������û������
 * Ȼ��sleep һ�룬���ն˿�ʼbind ��Ȼ���������յ���Ϣ
 * 
 * @author yannian
 * 
 */
public class DrainerRunable extends RunnableCallback {
	private final static Logger LOG = Logger.getLogger(DrainerRunable.class);

	private LinkedBlockingQueue<TransferData> transferQueue;
	private ConcurrentHashMap<NodePort, IConnection> nodeportSocket;
	private ConcurrentHashMap<Integer, NodePort> taskNodeport;
	
	public DrainerRunable(LinkedBlockingQueue<TransferData> transfer_queue,
			ConcurrentHashMap<NodePort, IConnection> node_port__socket,
			ConcurrentHashMap<Integer, NodePort> task__node_port) {
		this.transferQueue = transfer_queue;
		this.nodeportSocket = node_port__socket;
		this.taskNodeport = task__node_port;
	}

	@Override
	public void run() {
			try {
				TransferData felem = transferQueue.take();
				if (felem != null) {
					ArrayList<TransferData> drainer = new ArrayList<TransferData>();
					drainer.add(felem);
					transferQueue.drainTo(drainer);
					for (TransferData o : drainer) {
						int taskId = o.getTaskid();
						byte[] tuple = o.getData();

						NodePort nodePort = taskNodeport.get(taskId);
						if (nodePort == null) {
							String errormsg = "can`t not found IConnection";
							LOG.warn("DrainerRunable warn", new Exception(
									errormsg));
							continue;
						}
						IConnection conn = nodeportSocket.get(nodePort);
						if (conn == null) {
							String errormsg = "can`t not found nodePort";
							LOG.warn("DrainerRunable warn", new Exception(
									errormsg));
							continue;
						}

						conn.send(taskId, tuple);
					}
					drainer.clear();
				}
			} catch (Exception e) {
				LOG.error("DrainerRunable send error", e);
			}
	}

	@Override
	public Object getResult() {
		return 0;
	}

}
