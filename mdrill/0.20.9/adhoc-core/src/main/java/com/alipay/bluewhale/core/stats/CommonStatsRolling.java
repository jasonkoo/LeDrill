package com.alipay.bluewhale.core.stats;

import java.io.Serializable;

import com.alipay.bluewhale.core.stats.RollingWindow.RollingWindowSet;
/**
 * spout��bolt���õ�ͳ�ƶ��� rollingwindowset�����
 * @author yannian
 *
 */
public class CommonStatsRolling  implements Serializable{

    private static final long serialVersionUID = 1L;
	public RollingWindowSet emitted;
	public RollingWindowSet transferred;
	public Integer rate;
}
