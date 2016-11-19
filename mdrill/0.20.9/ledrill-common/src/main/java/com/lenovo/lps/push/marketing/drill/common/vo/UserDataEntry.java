package com.lenovo.lps.push.marketing.drill.common.vo;

import java.util.Date;

import com.google.gson.Gson;
import com.lenovo.lps.push.marketing.drill.common.util.DateUtil;
import com.lenovo.lps.push.marketing.drill.common.util.NumberUtil;

public class UserDataEntry {

	// private static Logger LOG = Logger.getLogger(UserDataEntry.class);

	private final static String SEPARATOR = "|";
	protected final static String THE_DATE_PATTERN = DateUtil.DATE_PATTERN;
	protected final static String DATE_TIME_PATTERN = DateUtil.DATE_TIME_PATTERN;
	
	

	protected Date date;

	// push id
	protected String pid;
	// access No: the number of visits of the client's contacting the push
	// server
	// delete
	protected long accessNum;
	// client's APN (name) on device; ssid when wifi
	// delete
	protected String apn;
	// cell id of BS on client
	// delete
	protected String cellId;
	// the package name of SDK' hosting App (null if Apk is the host)
	protected String channelName;
	// battery status of the device
	// delete
	protected String chargeStatus;
	// the located city name of the device (from ip)
	protected String cityName;
	// the country code + city code (level one) (from ip)
	// delete
	// use: 20140526
	protected String countryCode;
	// the created date of the entry
	// delete
	protected Date createDate;
	// OS customized version (e.g., LeOS 1.0)
	// delete
	protected String custVersion;
	// IMSI of the device (android api)
	// delete
	protected String deviceIMSI;
	// the model of the device (android api)
	protected String deviceModel;
	// the IMEI of the device / S/N / Mac address
	protected String deviceId;
	// the type of the deviceId
	protected String deviceIdType;
	// delete
	// the ip of the last visiting of the client
	protected String ip;
	// delete
	// the latitude of the device (Android API)
	protected String latitude;
	// delete
	// the locId of the device (Android API)
	protected String locId;
	// delete
	// the longitude of the device (Android API)
	protected String longitude;
	// delete
	// the modified date of the entry
	protected Date modifyDate;
	// delete
	// the network type of the device (e.g., 2g/3g/4g/wifi)
	protected String netaccessType;
	// the type of the operator (e.g., TEL/MOB/UNI) (from Ip)
	protected String operationType;
	// the type of the operator (from Ip)
	// delete
	protected String operatorCode;
	// the OS version of the device (Android Api)
	protected String osVersion;
	// the package name of the push engine (sdk/apk has it)
	protected String pePkgName;
	// the version code of the push engine
	protected String peVerCode;
	// the version name of the push engine
	protected String peVersion;
	// the version of the polling service
	protected String pePollVersion;
	// the system id of the base station (Android Api)
	// delete
	protected String sysId;

	protected Date arrivalTime;

	public long getAccessNum() {
		return accessNum;
	}

	public void setAccessNum(long accessNum) {
		this.accessNum = accessNum;
	}

	public String getApn() {
		return apn;
	}

	public void setApn(String apn) {
		this.apn = apn;
	}

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChargeStatus() {
		return chargeStatus;
	}

	public void setChargeStatus(String chargeStatus) {
		this.chargeStatus = chargeStatus;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCustVersion() {
		return custVersion;
	}

	public void setCustVersion(String custVersion) {
		this.custVersion = custVersion;
	}

	public String getDeviceIMSI() {
		return deviceIMSI;
	}

	public void setDeviceIMSI(String deviceIMSI) {
		this.deviceIMSI = deviceIMSI;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceIdType() {
		return deviceIdType;
	}

	public void setDeviceIdType(String deviceIdType) {
		this.deviceIdType = deviceIdType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLocId() {
		return locId;
	}

	public void setLocId(String locId) {
		this.locId = locId;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getNetaccessType() {
		return netaccessType;
	}

	public void setNetaccessType(String netaccessType) {
		this.netaccessType = netaccessType;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getPePkgName() {
		return pePkgName;
	}

	public void setPePkgName(String pePkgName) {
		this.pePkgName = pePkgName;
	}

	public String getPeVerCode() {
		return peVerCode;
	}

	public void setPeVerCode(String peVerCode) {
		this.peVerCode = peVerCode;
	}

	public String getPeVersion() {
		return peVersion;
	}

	public void setPeVersion(String peVersion) {
		this.peVersion = peVersion;
	}

	public String getPePollVersion() {
		return pePollVersion;
	}

	public void setPePollVersion(String pePollVersion) {
		this.pePollVersion = pePollVersion;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public Date getDate() {
		return date;
	}

	public String getPid() {
		return pid;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String dateStr = DateUtil.dateToString(date, THE_DATE_PATTERN);
		sb.append(dateStr);
		sb.append(SEPARATOR);

		sb.append(pid);
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
	
	public String toStringForParsing() {
		Gson g = new Gson();
		return g.toJson(this);
	}

//	public String toStringForParsing() {
//		StringBuilder sb = new StringBuilder();
////		String dateStr = DateUtil.dateToString(date, THE_DATE_PATTERN);
////		sb.append(dateStr);
////		sb.append(SEPARATOR);
//
//		sb.append(pid);
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

	public static void main(String[] args) {
		// System.out.println( DataGenerator.getUserDataEntry());
		// LOG.debug(MyDataEntry.getMyDataEntryAsString());
	}
	
	public static UserDataEntry parseLine(String line) {
		Gson g = new Gson();
		return g.fromJson(line, UserDataEntry.class);
	}

//	public static UserDataEntry parseLine(String line) throws ParseException {
//		UserDataEntry de = null;
//
//		if (!StringUtils.isEmpty(line)) {
//			de = new UserDataEntry();
//			String[] cols = line.trim().split("\\" + SEPARATOR);
//			if (cols.length == 28) {
//
//				de.pid = cols[0];
//				de.accessNum = NumberUtil.stringToLong(cols[1]);
//				de.apn = cols[2];
//				de.cellId = cols[3];
//				de.channelName = cols[4];
//				de.chargeStatus = cols[5];
//				de.cityName = cols[6];
//				de.countryCode = cols[7];
//				de.createDate = DateUtil.stringToDate(cols[8],
//						DATE_TIME_PATTERN);
//				de.custVersion = cols[9];
//				de.deviceIMSI = cols[10];
//				de.deviceModel = cols[11];
//				de.deviceId = cols[12];
//				de.deviceIdType = cols[13];
//				de.ip = cols[14];
//				de.latitude = cols[15];
//				de.locId = cols[16];
//				de.longitude = cols[17];
//				de.modifyDate = DateUtil.stringToDate(cols[18],
//						DATE_TIME_PATTERN);
//				de.netaccessType = cols[19];
//				de.operationType = cols[20];
//				de.operatorCode = cols[21];
//				de.osVersion = cols[22];
//				de.pePkgName = cols[23];
//				de.peVerCode = cols[24];
//				de.peVersion = cols[25];
//				de.pePollVersion = cols[26];
//				de.sysId = cols[27];
//			}
//		}
//		return de;
//	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

}
