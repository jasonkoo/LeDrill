package com.lenovo.lps.push.marketing.drill.common.vo;

import java.util.Date;

import com.google.gson.Gson;
import com.lenovo.lps.push.marketing.drill.common.util.DateUtil;
import com.lenovo.lps.push.marketing.drill.common.util.NumberUtil;

public class ErrorDataEntry extends UserDataEntry {

	// private static Logger LOG = Logger.getLogger(MyDataEntry.class);

	private final static String SEPARATOR = "|";

	private String adId;
	private String result;
	
	
	private String type;
	private String packageName;
	private String targetVersion;

	private Date logTime;
	
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
		
		sb.append(type);
		sb.append(SEPARATOR);
		sb.append(packageName);
		sb.append(SEPARATOR);
		sb.append(targetVersion);
		sb.append(SEPARATOR);
		
		
		String logTimeStr = DateUtil.dateToString(logTime, DATE_TIME_PATTERN);
		sb.append(logTimeStr);
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
//		
//		sb.append(adId);
//		sb.append(SEPARATOR);
//		sb.append(pid);
//		sb.append(SEPARATOR);
//		sb.append(result);
//		sb.append(SEPARATOR);
//		
//		sb.append(type);
//		sb.append(SEPARATOR);
//		sb.append(packageName);
//		sb.append(SEPARATOR);
//		sb.append(targetVersion);
//		sb.append(SEPARATOR);
//		
//		
//		String logTimeStr = DateUtil.dateToString(logTime, DATE_TIME_PATTERN);
//		sb.append(logTimeStr);
//		sb.append(SEPARATOR);
//		
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
	
	public static ErrorDataEntry parseLine(String line){
		Gson g = new Gson();
		return g.fromJson(line, ErrorDataEntry.class);
	}

//	public static ErrorDataEntry parseLine(String line) throws ParseException {
//		ErrorDataEntry de = null;
//		if (line != null) {
//			String[] cols = line.trim().split("\\" + SEPARATOR);
//			if (cols.length == 34) {
//				de = new ErrorDataEntry();
//
//				de.adId = cols[0];
//				de.pid = cols[1];
//				de.result = cols[2];
//				de.type = cols[3];
//				de.packageName = cols[4];
//				de.targetVersion = cols[5];
//				de.logTime = DateUtil.stringToDate(cols[6], DATE_TIME_PATTERN);
//
//				// set date according to hitTime
//				de.date = de.logTime;
//
//				if (!StringUtil.isEmpty(cols[7])) {
//					de.accessNum = NumberUtil.stringToLong(cols[7]);
//				}
//				de.apn = cols[8];
//				de.cellId = cols[9];
//				de.channelName = cols[10];
//				de.chargeStatus = cols[11];
//				de.cityName = cols[12];
//				de.countryCode = cols[13];
//				if (!StringUtil.isEmpty(cols[14])) {
//					de.createDate = DateUtil.stringToDate(cols[14],
//							DATE_TIME_PATTERN);
//				}
//				de.custVersion = cols[15];
//				de.deviceIMSI = cols[16];
//				de.deviceModel = cols[17];
//				de.deviceId = cols[18];
//				de.deviceIdType = cols[19];
//				de.ip = cols[20];
//				de.latitude = cols[21];
//				de.locId = cols[22];
//				de.longitude = cols[23];
//				if (!StringUtil.isEmpty(cols[24])) {
//					de.modifyDate = DateUtil.stringToDate(cols[24],
//							DATE_TIME_PATTERN);
//				}
//				de.netaccessType = cols[25];
//				de.operationType = cols[26];
//				de.operatorCode = cols[27];
//				de.osVersion = cols[28];
//				de.pePkgName = cols[29];
//				de.peVerCode = cols[30];
//				de.peVersion = cols[31];
//				de.pePollVersion = cols[32];
//				de.sysId = cols[33];
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

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getTargetVersion() {
		return targetVersion;
	}

	public void setTargetVersion(String targetVersion) {
		this.targetVersion = targetVersion;
	}

	
}
