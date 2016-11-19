package com.lenovo.lps.push.marketing.drill.common.vo;

import java.util.Date;

import com.google.gson.Gson;
import com.lenovo.lps.push.marketing.drill.common.util.DateUtil;
import com.lenovo.lps.push.marketing.drill.common.util.NumberUtil;

public class FeedbackDataEntry extends UserDataEntry {

	//private static Logger LOG = Logger.getLogger(FeedbackDataEntry.class);

	private final static String SEPARATOR = "|";

	private String adId;
	private String eventType;
	private String acId;
	private String adType;
	private Date logTime;

	public final static int COUNTS_ARRAY_LENGTH = 10;
	//private long[] countsArray = new long[COUNTS_ARRAY_LENGTH];
	private long[] countsArray;
	
	public void setCountsArray(long[] countsArray) {
		this.countsArray = countsArray;
	}

	public long[] getCountsArray() {
		return countsArray;
	}

	public String getAdId() {
		return adId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setAdId(String adId) {
		this.adId = adId;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	// private static FeedbackDataEntry getDataEntry() {
	// return DataGenerator.getFeedbackDataEntry();
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
		sb.append(eventType);
		sb.append(SEPARATOR);
		sb.append(acId);
		sb.append(SEPARATOR);
		sb.append(adType);
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

		for (int i = 0; i < COUNTS_ARRAY_LENGTH; i++) {
			sb.append(SEPARATOR);
			sb.append(countsArray[i]);
		}

		return sb.toString();
	}
	


//	@Override
//	public String toStringForParsing() {
//		StringBuilder sb = new StringBuilder();
////		String dateStr = DateUtil.dateToString(date, THE_DATE_PATTERN);
////		sb.append(dateStr);
////		sb.append(SEPARATOR);
//
//		sb.append(adId);
//		sb.append(SEPARATOR);
//		sb.append(pid);
//		sb.append(SEPARATOR);
//		sb.append(eventType);
//		sb.append(SEPARATOR);
//		sb.append(acId);
//		sb.append(SEPARATOR);
//		sb.append(adType);
//		sb.append(SEPARATOR);
//
//		String logTimeStr = DateUtil.dateToString(logTime, DATE_TIME_PATTERN);
//		sb.append(logTimeStr);
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
////
////		for (int i = 0; i < COUNTS_ARRAY_LENGTH; i++) {
////			sb.append(SEPARATOR);
////			sb.append(countsArray[i]);
////		}
//
//		return sb.toString();
//	}

	// public static String getDataEntryAsStringForParsing() {
	// return getDataEntry().toStringForParsing();
	// }

	// public static void main(String[] args) {
	// System.out.println(FeedbackDataEntry.getDataEntry());
	// // LOG.debug(MyDataEntry.getMyDataEntryAsString());
	// }

	public static FeedbackDataEntry parseLine(String line){
		Gson g = new Gson();
		return g.fromJson(line, FeedbackDataEntry.class);
	}
	
//	public static FeedbackDataEntry parseLine(String line)
//			throws ParseException {
//		// LOG.info("parseLine: line=" + line);
//		FeedbackDataEntry de = null;
//		if (!StringUtils.isEmpty(line)) {
//			// regex("|") = "\\|"
//			String[] cols = line.trim().split("\\" + SEPARATOR);
//			if (cols.length == 33) {
//				de = new FeedbackDataEntry();
//
//				de.adId = cols[0];
//				de.pid = cols[1];
//				// de.eventType = cols[2];
//				long offset = Long.parseLong(cols[2]);
//				for (int i = 0; i < COUNTS_ARRAY_LENGTH; i++) {
//					if (i == offset) {
//						de.countsArray[i] = 1L;
//					} else {
//						de.countsArray[i] = 0L;
//					}
//				}
//
//				de.acId = cols[3];
//				de.adType = cols[4];
//				de.logTime = DateUtil.stringToDate(cols[5], DATE_TIME_PATTERN);
//
//				// set the date according to logTime
//				de.date = de.logTime;
//				if (!StringUtil.isEmpty(cols[6])) {
//					de.accessNum = NumberUtil.stringToLong(cols[6]);
//				}
//				de.apn = cols[7];
//				de.cellId = cols[8];
//				de.channelName = cols[9];
//				de.chargeStatus = cols[10];
//				de.cityName = cols[11];
//				de.countryCode = cols[12];
//				if (!StringUtil.isEmpty(cols[13])) {
//					de.createDate = DateUtil.stringToDate(cols[13],
//							DATE_TIME_PATTERN);
//				}
//				de.custVersion = cols[14];
//				de.deviceIMSI = cols[15];
//				de.deviceModel = cols[16];
//				de.deviceId = cols[17];
//				de.deviceIdType = cols[18];
//				de.ip = cols[19];
//				de.latitude = cols[20];
//				de.locId = cols[21];
//				de.longitude = cols[22];
//				if (!StringUtil.isEmpty(cols[23])) {
//					de.modifyDate = DateUtil.stringToDate(cols[23],
//							DATE_TIME_PATTERN);
//				}
//				de.netaccessType = cols[24];
//				de.operationType = cols[25];
//				de.operatorCode = cols[26];
//				de.osVersion = cols[27];
//				de.pePkgName = cols[28];
//				de.peVerCode = cols[29];
//				de.peVersion = cols[30];
//				de.pePollVersion = cols[31];
//				de.sysId = cols[32];
//			}
//		}
//		//LOG.info("parseLine: de=" + de);
//		return de;
//	}

	public String getAdType() {
		return adType;
	}

	public void setAdType(String adType) {
		this.adType = adType;
	}

	public String getAcId() {
		return acId;
	}

	public void setAcId(String acId) {
		this.acId = acId;
	}

	public Date getLogTime() {
		return logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}
}
