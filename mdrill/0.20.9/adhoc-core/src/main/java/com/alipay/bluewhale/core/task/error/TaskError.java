package com.alipay.bluewhale.core.task.error;

import java.io.Serializable;
/**
 * ���taskִ��ʧ�ܣ��Ὣ������Ϣ�ϱ���Zk(/storm-zk-root/taskerrors/{topologyid}/{taskid})��
 * error: ������Ϣ
 * timSecs: ��������ʱ��
 * @author yannian
 *
 */
public class TaskError implements Serializable{
    private static final long serialVersionUID = 1L;

	private String error;

	private int timSecs;

	public TaskError(String error, int timSecs) {
		this.error = error;
		this.timSecs = timSecs;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getTimSecs() {
		return timSecs;
	}

	public void setTimSecs(int timSecs) {
		this.timSecs = timSecs;
	}
	
	@Override
        public String toString() {
	    return "TaskError [error=" + error + ", timSecs=" + timSecs + "]";
        }
 
}
