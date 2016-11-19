package com.alipay.bluewhale.core.task.acker;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import backtype.storm.Config;
import backtype.storm.task.IBolt;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;
import backtype.storm.utils.TimeCacheMap;

import com.alipay.bluewhale.core.utils.StormUtils;


/**
 * akcer ԭ��
 * 1.spout����һ��tuple�󣬳��˻������bolt�����⣬���ᷢ����acker,��Ϣ����Ϊinit,ͬʱ���ػ�����TimeCacheMap����洢��tuple
 * 2.bolt�ڴ��������tuple����tuple��,��ͨ��ack��fail��acker������Ϣ
 * 3.acker���յ��󣬻��ж�����tuple���Ƿ�ȫ������ɹ�������������ʧ�ܣ�Ȼ������Ϣ��tuple��Ӧ��Դspout
 * 4.spout���TimeCacheMap�Ļ��壬spout��ack��fail�����ᱻ���ã������ﲢû���Զ������ط�-��Ҫҵ����fail��ack���Լ���ɡ�
 * @author yannian
 *
 */
public class Acker implements IBolt{
    public static String ACKER_COMPONENT_ID = "__acker";
    public static String ACKER_INIT_STREAM_ID = "__ack_init";
    public static String ACKER_ACK_STREAM_ID = "__ack_ack";
    public static String ACKER_FAIL_STREAM_ID = "__ack_fail";
    private static Logger LOG = Logger.getLogger(Acker.class);

    private OutputCollector collector = null;
    private TimeCacheMap pending = null;

    // update_ack ��current_set��:val��ֵ��value����xor����
    private static synchronized void update_ack(AckObject current_set,
	    Object value) {
	Long old = current_set.val;
	if (old == null) {
	    old = 0l;
	}
	Long newvalue = StormUtils.bit_xor(old, value);
	current_set.val = newvalue;
    }

    // acker_emit_direct ��values��Ϣ���͵�Ŀ��task
//    public static void acker_emit_direct(OutputCollector collector,   Integer task, String stream, List values) {
//    }

 

    // prepare ����TimeCacheMap������������msg_id
    @Override
    public void prepare(Map stormConf, TopologyContext context,
	    OutputCollector collector) {
	this.collector = collector;
	String key=Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS;
	pending = new TimeCacheMap(StormUtils.parseInt(stormConf.get(key)));
    }

    // execute ackerһ����������Ϣ��ACKER_INIT,ACKER-ACK,ACKER_FAIL,��������ʼ�����ɹ���ʧ��
    // TimeCacheMap�е�value�洢����������Ϊ{":val":"xor message_id",":spout-task":"soput��task_id",":failed":"����ʧ��"}
    // ��val=0��failed=true��ʱ��ֱ�����Ӧ��spout����ack��fail��Ϣ��spout���ж�ȷ���Ƿ����·��ʹ�tuple
    @Override
    public void execute(Tuple input) {

	Object id = input.getValue(0);

	AckObject curr = (AckObject) pending.get(id);
	if (curr == null) {
	    curr = new AckObject();
	}
	String stream_id = input.getSourceStreamId();

	if (stream_id.equals(Acker.ACKER_INIT_STREAM_ID)) {
	    Acker.update_ack(curr, input.getValue(1));
	    curr.spout_task = input.getValue(2);

	}

	if (stream_id.equals(Acker.ACKER_ACK_STREAM_ID)) {

	    Acker.update_ack(curr, input.getValue(1));
	}

	if (stream_id.equals(Acker.ACKER_FAIL_STREAM_ID)) {

	    curr.failed = true;
	}

	pending.put(id, curr);

	Integer task = (Integer) curr.spout_task;
	
	if (task != null) {

	    Long val = (Long) curr.val;

	    if (new Long(0).equals(val)) {
		pending.remove(id);
		List values =StormUtils.mk_list(id);

		collector.emitDirect(task, Acker.ACKER_ACK_STREAM_ID, values);

	    }

	    boolean isFail = (Boolean) curr.failed;
	    if (isFail) {
		pending.remove(id);
		List values = StormUtils.mk_list(id);
		collector.emitDirect(task, Acker.ACKER_FAIL_STREAM_ID, values);
	    }
	}

	collector.ack(input);


    }

    @Override
    public void cleanup() {

    }

}
