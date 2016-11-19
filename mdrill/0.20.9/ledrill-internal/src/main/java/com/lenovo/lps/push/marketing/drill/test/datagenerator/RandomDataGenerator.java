package com.lenovo.lps.push.marketing.drill.test.datagenerator;

import java.util.Date;

import com.lenovo.lps.push.marketing.drill.common.util.NumberUtil;
import com.lenovo.lps.push.marketing.drill.common.vo.FeedbackDataEntry;
import com.lenovo.lps.push.marketing.drill.common.vo.HitDataEntry;
import com.lenovo.lps.push.marketing.drill.common.vo.UserDataEntry;

public class RandomDataGenerator {

	public static FeedbackDataEntry getFeedbackDataEntry() {
		FeedbackDataEntry de = new FeedbackDataEntry();
		Date now = new Date();
		//long nowl = now.getTime();
		//String timeStr = Long.toString(nowl);

		de.setAdId("adId_" + getRandomNumber());
		de.setPid("pid_" + getRandomNumber());
		de.setEventType(Integer.toString(getEventType()));
		de.setAcId("acId_" + getRandomNumber());
		de.setAdType("adType_" +  getRandomNumber());
		de.setLogTime(now);

		de.setChannelName("channelName_" + getRandomNumber());
		de.setCityName("cityName_" +  getRandomNumber());
		de.setDeviceModel("deviceModel_" + getRandomNumber());
		de.setDeviceId("deviceId_" + getRandomNumber());
		de.setDeviceIdType("deviceIdType_" + getRandomNumber());
		de.setOperationType("operationType_" + getRandomNumber());
		de.setOsVersion("osVersion_" + getRandomNumber());
		de.setPePkgName("pePkgName_" + getRandomNumber());
		de.setPeVerCode("peVerCode_" + getRandomNumber());
		de.setPeVersion("peVersion_" + getRandomNumber());
		de.setPePollVersion("pePollVersion_" + getRandomNumber());

		return de;
	}

	private static int getEventType() {
		return NumberUtil.getRandomInt(FeedbackDataEntry.COUNTS_ARRAY_LENGTH);
	}

	public static HitDataEntry getHitDataEntry() {
		HitDataEntry de = new HitDataEntry();
		Date now = new Date();
		//long nowl = now.getTime();
		//String timeStr = Long.toString(nowl);

		de.setDate(now);
		de.setAdId("adId_" + getRandomNumber());
		de.setPid("pid_" + getRandomNumber());
		de.setResult("result_" + getRandomNumber());
		de.setHitTime(now);

		de.setChannelName("channelName_" + getRandomNumber());
		de.setCityName("cityName_" + getRandomNumber());
		de.setDeviceModel("deviceModel_" + getRandomNumber());
		de.setDeviceId("deviceId_" + getRandomNumber());
		de.setDeviceIdType("deviceIdType_" + getRandomNumber());
		de.setOperationType("operationType_" + getRandomNumber());
		de.setOsVersion("osVersion_" + getRandomNumber());
		de.setPePkgName("pePkgName_" + getRandomNumber());
		de.setPeVerCode("peVerCode_" + getRandomNumber());
		de.setPeVersion("peVersion_" + getRandomNumber());
		de.setPePollVersion("pePollVersion_" + getRandomNumber());

		return de;
	}

	public static UserDataEntry getUserDataEntry() {
		UserDataEntry de = new UserDataEntry();
		Date now = new Date();
		long nowl = now.getTime();
		//String timeStr = Long.toString(nowl);

		de.setPid("pid_" + getRandomNumber());
		de.setAccessNum(nowl);
		de.setApn("apn_" + getRandomNumber());
		de.setCellId("cellId_" + getRandomNumber());
		de.setChannelName("channelName_" + getRandomNumber());
		de.setChargeStatus("chargeStatus_" + getRandomNumber());
		de.setCityName("cityName_" + getRandomNumber());
		de.setCountryCode("countryCode_" + getRandomNumber());
		de.setCreateDate(now);
		de.setCustVersion("custVersion_" + getRandomNumber());
		de.setDeviceIMSI("deviceIMSI_" + getRandomNumber());
		de.setDeviceModel("deviceModel_" + getRandomNumber());
		de.setDeviceId("deviceId_" + getRandomNumber());
		de.setDeviceIdType("deviceIdType_" + getRandomNumber());
		de.setIp("ip_" + getRandomNumber());
		de.setLatitude("latitude_" + getRandomNumber());
		de.setLocId("locId_" + getRandomNumber());
		de.setLongitude("longitude_" + getRandomNumber());
		de.setModifyDate(now);
		de.setNetaccessType("netaccessType_" + getRandomNumber());
		de.setOperationType("operationType_" + getRandomNumber());
		de.setOperatorCode("operatorCode_" + getRandomNumber());
		de.setOsVersion("osVersion_" + getRandomNumber());
		de.setPePkgName("pePkgName_" + getRandomNumber());
		de.setPeVerCode("peVerCode_" + getRandomNumber());
		de.setPeVersion("peVersion_" + getRandomNumber());
		de.setPePollVersion("pePollVersion_" + getRandomNumber());
		de.setSysId("sysId_" + getRandomNumber());

		return de;
	}
	
	private static int getRandomNumber(){
		return NumberUtil.getRandomInt(Integer.MAX_VALUE);
	}

}
