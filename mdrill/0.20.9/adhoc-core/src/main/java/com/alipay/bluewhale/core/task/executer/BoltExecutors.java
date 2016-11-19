package com.alipay.bluewhale.core.task.executer;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import backtype.storm.Config;
import backtype.storm.serialization.KryoTupleDeserializer;
import backtype.storm.task.IBolt;
import backtype.storm.task.IOutputCollector;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;
import backtype.storm.utils.TimeCacheMap;

import com.alipay.bluewhale.core.callback.RunnableCallback;
import com.alipay.bluewhale.core.cluster.StormConfig;
import com.alipay.bluewhale.core.messaging.IConnection;
import com.alipay.bluewhale.core.stats.BaseTaskStatsRolling;
import com.alipay.bluewhale.core.task.error.ITaskReportErr;
import com.alipay.bluewhale.core.task.transfer.TaskSendTargets;
import com.alipay.bluewhale.core.utils.EvenSampler;
import com.alipay.bluewhale.core.utils.StormUtils;
import com.alipay.bluewhale.core.work.transfer.WorkerTransfer;

/**
 * 
 * bolt ������
 * 
 * chages
 * 
 * �Ұ�mk-executors IBolt �е� tuple-start-times (ConcurrentHashMap.) pending-acks
 * (ConcurrentHashMap.) ���͸������� TimeCacheMap����
 * 
 * ��Щʱ��û׼��bolt�����һЩbug,�������ǵ��ã�ack����fail.���ʱ��spout��pending�ͻᳬʱ�����ͻ��ط����tuple.
 * ������pending-acks��tuple-start-time��Ͳ��ᱻ������������ͻᵼ��ConcurrentHashMap
 * ���Խ��Խ����������޸ĵ�ԭ�� Ŀǰ��ʹ��storm_conf.get(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS)
 * �������,�����ó�ʱ��ʱ��
 * 
 * 
 * @author yannian
 * 
 */
public class BoltExecutors extends RunnableCallback {
	private static Logger LOG = Logger.getLogger(BoltExecutors.class);
	private IConnection puller;
	private AtomicBoolean zkActive;
	private Integer task_id;
	private IBolt bolt;
	private KryoTupleDeserializer deserializer;
	private TimeCacheMap<Tuple, Long> tuple_start_times;
	private EvenSampler sampler;
	private Exception errorReport = null;

	public BoltExecutors(IBolt _bolt, WorkerTransfer _transfer_fn,
			Map storm_conf, IConnection _puller, TaskSendTargets _send_fn,
			AtomicBoolean _storm_active_atom,
			TopologyContext _topology_context, TopologyContext _user_context,
			BaseTaskStatsRolling _task_stats, ITaskReportErr _report_error) {
		this.bolt = _bolt;
		this.puller = _puller;
		this.zkActive = _storm_active_atom;
		this.task_id = _topology_context.getThisTaskId();
		this.sampler = StormConfig.mk_stats_sampler(storm_conf);

		String component_id = _topology_context.getThisComponentId();

		String timeoutkey = Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS;
		int message_timeout_secs = StormUtils.parseInt(storm_conf
				.get(timeoutkey));

		// ԭ���� tuple-start-times (ConcurrentHashMap.)
		// ��Ҫ�Ƿ�ֹ ��bolt�У����ҵ���߼���bug����ĳһ���������ǵ���ack��fail���ͻᵼ������mapԽ��Խ��ֱ���ڴ治����

		this.tuple_start_times = new TimeCacheMap<Tuple, Long>(
				message_timeout_secs);

		IOutputCollector output_collector = new BoltCollector(
				message_timeout_secs, _report_error, _send_fn, _transfer_fn,
				_topology_context, task_id, tuple_start_times, _task_stats);
		LOG.info("Preparing bolt " + component_id + ":" + task_id);
		bolt.prepare(storm_conf, _user_context, new OutputCollector(
				output_collector));
		LOG.info("Prepared bolt " + component_id + ":" + task_id);
		deserializer = new KryoTupleDeserializer(storm_conf, _topology_context);

	}

	@Override
	public void run() {

		byte[] ser_msg = puller.recv();

		if (ser_msg != null && ser_msg.length > 0) {
			LOG.debug("Processing message");
			Tuple tuple = deserializer.deserialize(ser_msg);

			if (sampler.getResult()) {
				tuple_start_times.put(tuple, System.currentTimeMillis());
			}

			try {
				bolt.execute(tuple);
			} catch (RuntimeException e) {
				errorReport = e;
				LOG.error("bolt execute error ", e);
			} catch (Exception e) {
				errorReport = e;
				LOG.error("bolt execute error ", e);
			}

		}

	}

	@Override
	public Object getResult() {
		if (this.IsActive()) {
			return 0;
		} else {
			return -1;
		}
	}

	public boolean IsActive() {
		return zkActive.get();
	}

	@Override
	public Exception error() {
		return errorReport;
	}
}
