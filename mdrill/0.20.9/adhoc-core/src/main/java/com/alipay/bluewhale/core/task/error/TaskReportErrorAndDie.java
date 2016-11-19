package com.alipay.bluewhale.core.task.error;

import com.alipay.bluewhale.core.work.WorkerHaltRunable;

/**
 * ��task�����쳣�����ͨ���˽ӿڽ���Ϣ�ϱ��������ж�work��ִ��
 * @author yannian
 *
 */
public class TaskReportErrorAndDie implements ITaskReportErr {
    private ITaskReportErr reporterror;
    private WorkerHaltRunable haltfn;

    public TaskReportErrorAndDie(ITaskReportErr _reporterror,
	    WorkerHaltRunable _haltfn) {
	this.reporterror = _reporterror;
	this.haltfn = _haltfn;
    }

    @Override
    public void report(Throwable error) {
	this.reporterror.report(error);
	this.haltfn.run();
    }
}

