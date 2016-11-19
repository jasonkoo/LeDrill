package com.lenovo.lps.push.marketing.drill.user;

import java.util.Date;

import com.alimama.mdrillImport.DataParser;
import com.alimama.mdrillImport.InvalidEntryException;
import com.lenovo.lps.push.marketing.drill.common.util.DateUtil;
import com.lenovo.lps.push.marketing.drill.common.util.NumberUtil;
import com.lenovo.lps.push.marketing.drill.common.vo.UserDataEntry;

public class UserDataParser extends DataParser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3529504146775863798L;
	public final static String TABLE_NAME = "user";

	private final static String THE_DATE_PATTERN = DateUtil.DATE_PATTERN;
	private final static String DATE_TIME_PATTERN = DateUtil.DATE_TIME_PATTERN;

	@Override
	public String[] getGroupName() {
		String[] rtn = new String[30];
		rtn[0] = "thedate";

		rtn[1] = "pid";
		rtn[2] = "accessnum";
		rtn[3] = "apn";
		rtn[4] = "cellid";
		rtn[5] = "channelname";
		rtn[6] = "charge_status";

		rtn[7] = "city_name";
		rtn[8] = "country_code";
		rtn[9] = "createdate";
		rtn[10] = "cust_version";
		rtn[11] = "device_imsi";
		rtn[12] = "device_model";
		rtn[13] = "deviceid";

		rtn[14] = "deviceid_type";
		rtn[15] = "ip";
		rtn[16] = "latitude";
		rtn[17] = "locid";
		rtn[18] = "longitude";
		rtn[19] = "modifydate";
		rtn[20] = "netaccess_type";

		rtn[21] = "operation_type";
		rtn[22] = "operator_code";
		rtn[23] = "os_version";
		rtn[24] = "pe_pkgname";
		rtn[25] = "pe_vercode";
		rtn[26] = "pe_version";
		rtn[27] = "pepollversion";
		rtn[28] = "sysid";

		rtn[29] = "arrival_time";

		return rtn;

	}

	private static String[] sumName = {};

	@Override
	public String[] getSumName() {
		return sumName;
	}

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	@Override
	public DataIter parseLine(String line) throws InvalidEntryException {
		DataIter di = null;
		if (line != null) {
			try {
				UserDataEntry de = UserDataEntry.parseLine(line);
				Date now = new Date();
				de.setDate(now);
				de.setArrivalTime(now);
				di = new MyDataIter(de);
			} catch (Exception e) {
				throw new InvalidEntryException(e.getMessage());
			}
		} else {
			throw new InvalidEntryException("line is null");
		}
		return di;
	}

	public class MyDataIter implements DataIter {

		private UserDataEntry de;

		public MyDataIter(UserDataEntry de) {
			super();
			this.de = de;
		}

		@Override
		public boolean next() {
			return false;
		}

		@Override
		public long getTs() {
			long ts = de.getDate().getTime();
			return (ts / 10) * 10000;
		}

		@Override
		public Object[] getGroup() {

			return new String[] {
					DateUtil.dateToString(de.getDate(), THE_DATE_PATTERN),
					de.getPid(),
					NumberUtil.longToString(de.getAccessNum()),
					de.getApn(),
					de.getCellId(),
					de.getChannelName(),
					de.getChargeStatus(),
					de.getCityName(),
					de.getCountryCode(),
					DateUtil.dateToString(de.getCreateDate(), DATE_TIME_PATTERN),
					de.getCustVersion(),
					de.getDeviceIMSI(),
					de.getDeviceModel(),
					de.getDeviceId(),
					de.getDeviceIdType(),
					de.getIp(),
					de.getLatitude(),
					de.getLocId(),
					de.getLongitude(),
					DateUtil.dateToString(de.getModifyDate(), DATE_TIME_PATTERN),
					de.getNetaccessType(),
					de.getOperationType(),
					de.getOperatorCode(),
					de.getOsVersion(),
					de.getPePkgName(),
					de.getPeVerCode(),
					de.getPeVersion(),
					de.getPePollVersion(),
					de.getSysId(),
					DateUtil.dateToString(de.getArrivalTime(),
							DATE_TIME_PATTERN) };

		}

		@Override
		public Number[] getSum() {
			return new Number[] {};

		}

	}

}
