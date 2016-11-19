package com.alipay.bluewhale.core.work.transfer;

import java.util.concurrent.LinkedBlockingQueue;

import backtype.storm.serialization.KryoTupleSerializer;
import backtype.storm.tuple.Tuple;

/**
 * taskͨ���˽ӿڣ���Ҫ���͵�tuple���ŵ�worker�ķ��ͻ�����transfer_queue
 * @author yannian
 *
 */
public class WorkerTransfer {
	private LinkedBlockingQueue<TransferData> transferQueue;
	private KryoTupleSerializer serializer;
	
	public WorkerTransfer(KryoTupleSerializer serializer,LinkedBlockingQueue<TransferData> _transfer_queue) {
		this.transferQueue = _transfer_queue;
		this.serializer = serializer;
	}

    public void transfer(Integer taskid, Tuple tuple) {
         TransferData tData = new TransferData(taskid,serializer.serialize(tuple));
         transferQueue.add(tData);
    }

}
