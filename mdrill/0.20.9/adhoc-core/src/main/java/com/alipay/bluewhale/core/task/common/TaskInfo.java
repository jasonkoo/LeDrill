package com.alipay.bluewhale.core.task.common;

import java.io.Serializable;

/**
 * zk��/storm-zk-root/tasks/{topologyid}/{taskid}�´洢��������Ϣ
 * componentId�� ����������componentId
 */
public class TaskInfo implements Serializable{

    private static final long serialVersionUID = 1L;
    private String componentId;


    public TaskInfo(String componentId) {
	this.componentId = componentId;
    }

    public String getComponentId() {
	return componentId;
    }

    public void setComponentId(String componentId) {
	this.componentId = componentId;
    }

    @Override
    public boolean equals(Object assignment) {
	if (assignment instanceof TaskInfo
		&& ((TaskInfo) assignment).getComponentId().equals(
			getComponentId())) {
	    return true;
	}
	return false;
    }

    @Override
    public int hashCode() {
	return this.getComponentId().hashCode();
    }
    

    @Override
    public String toString() {
	return "TaskInfo [componentId=" + componentId + "]";
    }

}
