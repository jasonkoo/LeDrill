package com.lenovo.lps.push.marketing.drill.common.vo;

import java.util.Date;

import com.google.gson.Gson;
import com.lenovo.lps.push.marketing.drill.common.util.DateUtil;
import com.lenovo.lps.push.marketing.drill.common.util.NumberUtil;

public class HitDataEntry extends UserDataEntry {

	// private static Logger LOG = Logger.getLogger(MyDataEntry.class);

	private final static String SEPARATOR = "|";

	private String adId;
	private String result;
	private Date hitTime;
	
	private boolean firstTime;
	
	public boolean isFirstTime() {
		return firstTime;
	}

	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}

	public String getAdId() {
		return adId;
	}

	public void setAdId(String adId) {
		this.adId = adId;
	}

	// private static HitDataEntry getDataEntry() {
	// return DataGenerator.getAdHitDataEntry();
	// }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		String dateStr = DateUtil.dateToString(date, THE_DATE_PATTERN);
		sb.append(dateStr);
		sb.append(SEPARATOR);
		sb.append(adId);
		sb.append(SEPARATOR);
		sb.append(pid);
		sb.append(SEPARATOR);
		sb.append(result);
		sb.append(SEPARATOR);
		String hitTimeStr = DateUtil.dateToString(hitTime, DATE_TIME_PATTERN);
		sb.append(hitTimeStr);
		sb.append(SEPARATOR);

		String accessNumStr = NumberUtil.longToString(accessNum);
		sb.append(accessNumStr);
		sb.append(SEPARATOR);

		sb.append(apn);
		sb.append(SEPARATOR);

		sb.append(cellId);
		sb.append(SEPARATOR);

		sb.append(channelName);
		sb.append(SEPARATOR);

		sb.append(chargeStatus);
		sb.append(SEPARATOR);

		sb.append(cityName);
		sb.append(SEPARATOR);

		sb.append(countryCode);
		sb.append(SEPARATOR);

		String createDateStr = DateUtil.dateToString(createDate,
				DATE_TIME_PATTERN);
		sb.append(createDateStr);
		sb.append(SEPARATOR);

		sb.append(custVersion);
		sb.append(SEPARATOR);

		sb.append(deviceIMSI);
		sb.append(SEPARATOR);

		sb.append(deviceModel);
		sb.append(SEPARATOR);

		sb.append(deviceId);
		sb.append(SEPARATOR);

		sb.append(deviceIdType);
		sb.append(SEPARATOR);

		sb.append(ip);
		sb.append(SEPARATOR);

		sb.append(latitude);
		sb.append(SEPARATOR);

		sb.append(locId);
		sb.append(SEPARATOR);

		sb.append(longitude);
		sb.append(SEPARATOR);

		String modifyDateStr = DateUtil.dateToString(modifyDate,
				DATE_TIME_PATTERN);
		sb.append(modifyDateStr);
		sb.append(SEPARATOR);

		sb.append(netaccessType);
		sb.append(SEPARATOR);

		sb.append(operationType);
		sb.append(SEPARATOR);

		sb.append(operatorCode);
		sb.append(SEPARATOR);

		sb.append(osVersion);
		sb.append(SEPARATOR);

		sb.append(pePkgName);
		sb.append(SEPARATOR);

		sb.append(peVerCode);
		sb.append(SEPARATOR);

		sb.append(peVersion);
		sb.append(SEPARATOR);

		sb.append(pePollVersion);
		sb.append(SEPARATOR);

		sb.append(sysId);
		sb.append(SEPARATOR);

		String arrivalTimeStr = DateUtil.dateToString(arrivalTime,
				DATE_TIME_PATTERN);
		sb.append(arrivalTimeStr);
		return sb.toString();
	}

//	@Override
//	public String toStringForParsing() {
//		StringBuilder sb = new StringBuilder();
//
////		String dateStr = DateUtil.dateToString(date, THE_DATE_PATTERN);
////		sb.append(dateStr);
////		sb.append(SEPARATOR);
//		sb.append(adId);
//		sb.append(SEPARATOR);
//		sb.append(pid);
//		sb.append(SEPARATOR);
//		sb.append(result);
//		sb.append(SEPARATOR);
//		String hitTimeStr = DateUtil.dateToString(hitTime, DATE_TIME_PATTERN);
//		sb.append(hitTimeStr);
//		sb.append(SEPARATOR);
//
//		String accessNumStr = NumberUtil.longToString(accessNum);
//		sb.append(accessNumStr);
//		sb.append(SEPARATOR);
//
//		sb.append(apn);
//		sb.append(SEPARATOR);
//
//		sb.append(cellId);
//		sb.append(SEPARATOR);
//
//		sb.append(channelName);
//		sb.append(SEPARATOR);
//
//		sb.append(chargeStatus);
//		sb.append(SEPARATOR);
//
//		sb.append(cityName);
//		sb.append(SEPARATOR);
//
//		sb.append(countryCode);
//		sb.append(SEPARATOR);
//
//		String createDateStr = DateUtil.dateToString(createDate,
//				DATE_TIME_PATTERN);
//		sb.append(createDateStr);
//		sb.append(SEPARATOR);
//
//		sb.append(custVersion);
//		sb.append(SEPARATOR);
//
//		sb.append(deviceIMSI);
//		sb.append(SEPARATOR);
//
//		sb.append(deviceModel);
//		sb.append(SEPARATOR);
//
//		sb.append(deviceId);
//		sb.append(SEPARATOR);
//
//		sb.append(deviceIdType);
//		sb.append(SEPARATOR);
//
//		sb.append(ip);
//		sb.append(SEPARATOR);
//
//		sb.append(latitude);
//		sb.append(SEPARATOR);
//
//		sb.append(locId);
//		sb.append(SEPARATOR);
//
//		sb.append(longitude);
//		sb.append(SEPARATOR);
//
//		String modifyDateStr = DateUtil.dateToString(modifyDate,
//				DATE_TIME_PATTERN);
//		sb.append(modifyDateStr);
//		sb.append(SEPARATOR);
//
//		sb.append(netaccessType);
//		sb.append(SEPARATOR);
//
//		sb.append(operationType);
//		sb.append(SEPARATOR);
//
//		sb.append(operatorCode);
//		sb.append(SEPARATOR);
//
//		sb.append(osVersion);
//		sb.append(SEPARATOR);
//
//		sb.append(pePkgName);
//		sb.append(SEPARATOR);
//
//		sb.append(peVerCode);
//		sb.append(SEPARATOR);
//
//		sb.append(peVersion);
//		sb.append(SEPARATOR);
//
//		sb.append(pePollVersion);
//		sb.append(SEPARATOR);
//
//		sb.append(sysId);
////		sb.append(SEPARATOR);
////
////		String arrivalTimeStr = DateUtil.dateToString(arrivalTime,
////				DATE_TIME_PATTERN);
////		sb.append(arrivalTimeStr);
//		return sb.toString();
//	}

	// public static String getDataEntryAsStringForParsing() {
	// return getDataEntry().toStringForParsing();
	// }

	// public static void main(String[] args) {
	// System.out.println(HitDataEntry.getDataEntry());
	// // LOG.debug(MyDataEntry.getMyDataEntryAsString());
	// }

	public static HitDataEntry parseLine(String line) {
		Gson g = new Gson();
		return g.fromJson(line, HitDataEntry.class);
	}
	
//	public static HitDataEntry parseLine(String line) throws ParseException {
//		HitDataEntry de = null;
//		if (line != null) {
//			String[] cols = line.trim().split("\\" + SEPARATOR);
//			if (cols.length == 31) {
//				de = new HitDataEntry();
//
//				de.adId = cols[0];
//				de.pid = cols[1];
//				de.result = cols[2];
//				de.hitTime = DateUtil.stringToDate(cols[3], DATE_TIME_PATTERN);
//
//				// set date according to hitTime
//				de.date = de.hitTime;
//
//				if (!StringUtil.isEmpty(cols[4])) { 
//					de.accessNum = NumberUtil.stringToLong(cols[4]);
//				}
//				de.apn = cols[5];
//				de.cellId = cols[6];
//				de.channelName = cols[7];
//				de.chargeStatus = cols[8];
//				de.cityName = cols[9];
//				de.countryCode = cols[10];
//				
//				if (!StringUtil.isEmpty(cols[11])) { 
//					de.createDate = DateUtil.stringToDate(cols[11],
//							DATE_TIME_PATTERN);
//				}
//				de.custVersion = cols[12];
//				de.deviceIMSI = cols[13];
//				de.deviceModel = cols[14];
//				de.deviceId = cols[15];
//				de.deviceIdType = cols[16];
//				de.ip = cols[17];
//				de.latitude = cols[18];
//				de.locId = cols[19];
//				de.longitude = cols[20];
//				if (!StringUtil.isEmpty(cols[21])) { 
//					de.modifyDate = DateUtil.stringToDate(cols[21],
//							DATE_TIME_PATTERN);
//				}
//				de.netaccessType = cols[22];
//				de.operationType = cols[23];
//				de.operatorCode = cols[24];
//				de.osVersion = cols[25];
//				de.pePkgName = cols[26];
//				de.peVerCode = cols[27];
//				de.peVersion = cols[28];
//				de.pePollVersion = cols[29];
//				de.sysId = cols[30];
//			}
//		}
//		return de;
//	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Date getHitTime() {
		return hitTime;
	}

	public void setHitTime(Date hitTime) {
		this.hitTime = hitTime;
	}
}
