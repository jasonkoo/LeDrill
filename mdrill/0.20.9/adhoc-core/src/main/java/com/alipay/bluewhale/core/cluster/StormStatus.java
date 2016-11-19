package com.alipay.bluewhale.core.cluster;

import java.io.Serializable;

import com.alipay.bluewhale.core.daemon.StatusType;


 /**
  * topology��״̬
  * type: topology�����������ͣ�active��inactive��killed or rebalancing
  * killTimeSecs�� ������killed״̬��ʱ��Ϊ-1�������ã�killed״̬��ʱ�򣬱�ʾ�ӳٶ೤ʱ��ִ��kill����
  * delaySecs����ʾrebalancing״̬��ʱ���ӳٶ೤ʱ��ִ��rebalance����
  * oldStatus: �洢��һ��topology��״̬������˵��rebalance��ʱ�򣬻�ִ��һЩ��������������ǻ�����topology��״̬Ϊ��һ��״̬
  */
public class StormStatus  implements Serializable{

   
    private static final long serialVersionUID = 1L;
    private StatusType type;
    private int killTimeSecs;
    private int delaySecs;
    private StormStatus oldStatus = null;

    public StormStatus(int killTimeSecs, StatusType type, StormStatus oldStatus) {
	this.type = type;
	this.killTimeSecs = killTimeSecs;
	this.oldStatus = oldStatus;
    }

    public StormStatus(int killTimeSecs, StatusType type) {
	this.type = type;
	this.killTimeSecs = killTimeSecs;
    }

    public StormStatus(StatusType type, int delaySecs, StormStatus oldStatus) {
	this.type = type;
	this.delaySecs = delaySecs;
	this.oldStatus = oldStatus;
    }

    public StormStatus(StatusType type) {
	this.type = type;
	this.killTimeSecs = -1;
	this.delaySecs = -1;
    }

    public StatusType getStatusType() {
	return type;
    }

    public void setStatusType(StatusType type) {
	this.type = type;
    }

    public Integer getKillTimeSecs() {
	return killTimeSecs;
    }

    public void setKillTimeSecs(int killTimeSecs) {
	this.killTimeSecs = killTimeSecs;
    }

    public Integer getDelaySecs() {
	return delaySecs;
    }

    public void setDelaySecs(int delaySecs) {
	this.delaySecs = delaySecs;
    }

    public StormStatus getOldStatus() {
	return oldStatus;
    }

    public void setOldStatus(StormStatus oldStatus) {
	this.oldStatus = oldStatus;
    }

    @Override
    public boolean equals(Object base) {
	if (base instanceof StormStatus
		&& ((StormStatus) base).getStatusType().equals(getStatusType())
		&& ((StormStatus) base).getKillTimeSecs() == getKillTimeSecs()
		&& ((StormStatus) base).getDelaySecs().equals(getDelaySecs())) {
	    return true;
	}
	return false;
    }

    @Override
    public int hashCode() {
	return this.getStatusType().hashCode() + this.getKillTimeSecs().hashCode()
		+ this.getDelaySecs().hashCode();
    }

}