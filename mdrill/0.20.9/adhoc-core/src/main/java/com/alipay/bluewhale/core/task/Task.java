package com.alipay.bluewhale.core.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ConcurrentHashMap;


import org.apache.log4j.Logger;

import backtype.storm.task.TopologyContext;

import com.alipay.bluewhale.core.callback.RunnableCallback;
import com.alipay.bluewhale.core.cluster.ClusterState;
import com.alipay.bluewhale.core.cluster.Common;
import com.alipay.bluewhale.core.cluster.StormClusterState;
import com.alipay.bluewhale.core.cluster.StormConfig;
import com.alipay.bluewhale.core.cluster.StormZkClusterState;
import com.alipay.bluewhale.core.messaging.IConnection;
import com.alipay.bluewhale.core.messaging.IContext;
import com.alipay.bluewhale.core.stats.BaseTaskStatsRolling;
import com.alipay.bluewhale.core.task.common.TaskShutdownDameon;
import com.alipay.bluewhale.core.task.common.TasksCommon;
import com.alipay.bluewhale.core.task.error.ITaskReportErr;
import com.alipay.bluewhale.core.task.error.TaskReportError;
import com.alipay.bluewhale.core.task.error.TaskReportErrorAndDie;
import com.alipay.bluewhale.core.task.group.MkGrouper;
import com.alipay.bluewhale.core.task.heartbeat.TaskHeartbeatRunable;
import com.alipay.bluewhale.core.task.transfer.TaskSendTargets;
import com.alipay.bluewhale.core.task.transfer.UnanchoredSend;
import com.alipay.bluewhale.core.utils.AsyncLoopThread;
import com.alipay.bluewhale.core.utils.EvenSampler;
import com.alipay.bluewhale.core.utils.StormUtils;
import com.alipay.bluewhale.core.utils.UptimeComputer;
import com.alipay.bluewhale.core.work.WorkerHaltRunable;
import com.alipay.bluewhale.core.work.transfer.WorkerTransfer;

/**
 * task������
 * 
 * @author yannian
 * 
 */
public class Task {

	private final static Logger LOG = Logger.getLogger(Task.class);

	@SuppressWarnings("rawtypes")
	private Map stormConf;
	private TopologyContext topologyContext;
	private TopologyContext userContext;
	private String topologyid;
	private IContext mqContext;

	private AtomicBoolean stormActive;
	private WorkerTransfer workerTransfer;
	private WorkerHaltRunable workHalt;

	private Integer taskid;
	private String componentid;
	private AtomicBoolean active = new AtomicBoolean(true);
	// ��������ʱ�������
	private UptimeComputer uptime = new UptimeComputer();

	private StormClusterState zkCluster;
	private Object taskObj;
	private BaseTaskStatsRolling taskStats;
	
	
	@SuppressWarnings("rawtypes")
	public Task(Map conf, Map stormConf, TopologyContext topologyContext,
			TopologyContext userContext, String topologyid, IContext mqContext,
			ClusterState clusterState, AtomicBoolean stormActive,
			WorkerTransfer workerTransfer, WorkerHaltRunable workHalt)
			throws Exception {
		this.topologyContext = topologyContext;
		this.userContext = userContext;
		this.topologyid = topologyid;
		this.mqContext = mqContext;
		this.stormActive = stormActive;
		this.workerTransfer = workerTransfer;
		this.workHalt = workHalt;

		this.taskid = topologyContext.getThisTaskId();
		
		this.componentid = topologyContext.getThisComponentId();
		this.stormConf = TasksCommon.component_conf(stormConf, topologyContext,
				componentid);
		LOG.info("Loading task " + componentid + ":" + taskid);
		this.zkCluster = new StormZkClusterState(clusterState);

		// ��ȡtask_obj����,��ʵ���Ǹ���Component_id��ȡ���ε�SpoutSpec��bolt��StateSpoutSpec
		this.taskObj = TasksCommon.get_task_object(
				topologyContext.getRawTopology(), componentid);
		// ����taskͳ�ƵĶ���-�μ�stats��
		int samplerate = StormConfig.sampling_rate(stormConf);
		this.taskStats = TasksCommon.mk_task_stats(taskObj, samplerate);
	}

	private TaskSendTargets makeSendTargets() {
		String taskName = TasksCommon.get_readable_name(topologyContext);
		EvenSampler statSample = StormConfig.mk_stats_sampler(stormConf);
		//// ��ȡ��ǰtask��ÿ��streamӦ��������ЩcommponID,�Լ���������η����
		// <Stream_id,<component,Grouping>>
		Map<String, Map<String, MkGrouper>> streamComponentGrouper = TasksCommon
				.outbound_components(topologyContext);
		Map<Integer, String> task2Component = topologyContext
				.getTaskToComponent();
		Map<String, List<Integer>> component2Tasks = StormUtils
				.reverse_map(task2Component);
		return new TaskSendTargets(stormConf, taskName, streamComponentGrouper,
				topologyContext, statSample, component2Tasks, taskStats);
	}

	private RunnableCallback mkExecutor(IConnection puller,
			TaskSendTargets sendTargets) {
		// ���������ϱ��ص�����ʵ�����ǵ���storm_cluster.report-task-error
		ITaskReportErr reportError = new TaskReportError(zkCluster, topologyid,
				taskid);
		// �����������˳�����-ʵ�����ǵ����ϲ���report_error��������halt_process
		TaskReportErrorAndDie reportErrorDie = new TaskReportErrorAndDie(
				reportError, workHalt);
		return TasksCommon.mk_executors(taskObj, workerTransfer, stormConf,
				puller, sendTargets, stormActive, topologyContext, userContext,
				taskStats, reportErrorDie);
	}

	public TaskShutdownDameon execute() throws Exception {

		IConnection puller = mqContext.bind(topologyid, taskid);

		// ���������߳�
		TaskHeartbeatRunable hb = new TaskHeartbeatRunable(zkCluster, topologyid,
				taskid, uptime, taskStats, active, stormConf);
		AsyncLoopThread heartbeat_thread = new AsyncLoopThread(hb, true,
				Thread.MAX_PRIORITY, true);

		// ����tuple���ͺ�������ϵͳstream����startup��Ϣ
		List<Object> msg = new ArrayList<Object>();
		msg.add("startup");

		// ����task���ն���
		TaskSendTargets sendTargets = makeSendTargets();
		UnanchoredSend.send(topologyContext, sendTargets, workerTransfer,
				Common.SYSTEM_STREAM_ID, msg);

		// �����̣߳���zeroMq�ж�ȡtuple,����spout��bolt���д���Ȼ���͸�worker
		RunnableCallback componsementExecutor = mkExecutor(puller, sendTargets);
		AsyncLoopThread executor_threads = new AsyncLoopThread(
				componsementExecutor);
		AsyncLoopThread[] all_threads = { executor_threads, heartbeat_thread };

		LOG.info("Finished loading task " + componentid + ":" + taskid);

		return getShutdown(all_threads, heartbeat_thread, puller);
	}

	public TaskShutdownDameon getShutdown(AsyncLoopThread[] all_threads,
			AsyncLoopThread heartbeat_thread, IConnection puller) {
		TaskShutdownDameon shutdown = new TaskShutdownDameon(active, topologyid,
				taskid, mqContext, all_threads, zkCluster, puller, taskObj,
				heartbeat_thread);
		return shutdown;
	}

	@SuppressWarnings("rawtypes")
	public static TaskShutdownDameon mk_task(Map conf, Map stormConf,
			TopologyContext topologyContext, TopologyContext userContext,
			String stormId, IContext mqContext, ClusterState clusterState,
			AtomicBoolean stormActive, WorkerTransfer workerTransfer,
			WorkerHaltRunable workHalt) throws Exception {

		Task t = new Task(conf, stormConf, topologyContext, userContext,
				stormId, mqContext, clusterState, stormActive, workerTransfer,
				workHalt);

		return t.execute();
	}

}
