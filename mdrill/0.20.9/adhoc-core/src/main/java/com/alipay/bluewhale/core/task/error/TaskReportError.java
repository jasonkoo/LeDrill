package com.alipay.bluewhale.core.task.error;

import org.apache.log4j.Logger;

import com.alipay.bluewhale.core.cluster.StormClusterState;
/**
 * ��taskִ�г������ͨ���˷�����������Ϣ�ϱ���zk
 * @author yannian
 *
 */
public class TaskReportError implements ITaskReportErr {
    private static Logger LOG = Logger.getLogger(TaskReportError.class);
    private StormClusterState zkCluster;
    private String storm_id;
    private int task_id;

    public TaskReportError(StormClusterState _storm_cluster_state,
	    String _storm_id, int _task_id) {
	this.zkCluster = _storm_cluster_state;
	this.storm_id = _storm_id;
	this.task_id = _task_id;
    }

    @Override
    public void report(Throwable error) {
	LOG.error("ReportError", error);
	zkCluster.report_task_error(storm_id, task_id, error);
    }

}
