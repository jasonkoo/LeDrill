package com.alipay.bluewhale.core.command;

import java.util.Map;

import com.alipay.bluewhale.core.cluster.StormConfig;
/**
 * ��ӡ�������ֵ�������� python�ű�������
 * @author yannian
 *
 */
public class ConfigValue {
    public static void main(String[] args) {
	String name=args[0];
	Map conf=StormConfig.read_storm_config();
	Object value=conf.get(name);
	if(value==null)
	{
	    value=" ";
	}
	System.out.println("VALUE: "+value);
    }
}
