package com.lenovo.lps.push.marketing.drill.error;

import java.util.Date;

import org.apache.log4j.Logger;

import com.alimama.mdrillImport.DataParser;
import com.alimama.mdrillImport.InvalidEntryException;
import com.lenovo.lps.push.marketing.drill.common.util.DateUtil;
import com.lenovo.lps.push.marketing.drill.common.util.StringUtil;
import com.lenovo.lps.push.marketing.drill.common.vo.ErrorDataEntry;

public class ErrorDataParser extends DataParser {

	private static Logger LOG = Logger.getLogger(ErrorDataParser.class);
	
	protected final static String THE_DATE_PATTERN = DateUtil.DATE_PATTERN;
	//private final static String DATE_TIME_PATTERN = DateUtil.DATE_TIME_PATTERN;
	protected final static String MIN_PATTERN = DateUtil.MIN_PATTERN;
	protected final static String HOUR_PATTERN = DateUtil.HOUR_PATTERN;

	protected volatile long lines = 0;
	protected static long TS_MAX = 3600l * 24 * 31;

	protected volatile long laststartts = (System.currentTimeMillis() / 1000)
			- TS_MAX;
	protected volatile long lastendts = (System.currentTimeMillis() / 1000)
			+ TS_MAX;
	protected volatile long groupCreateerror = 0;
	
	// null on purpose
	protected final static String NIL = "nil";
	// really unknown
	protected final static String UNKNOWN = "unknown";

	/**
	 * 
	 */
	private static final long serialVersionUID = 3014783528983618914L;

	public final static String TABLE_NAME = "error";

	@Override
	public String[] getGroupName() {
		String[] rtn = new String[22];
		rtn[0] = "thedate";

		rtn[1] = "ad_id";
		rtn[2] = "pid";
		rtn[3] = "result";
		rtn[4] = "type";
		rtn[5] = "package_name";
		rtn[6] = "target_version";
		rtn[7] = "log_time";

		rtn[8] = "channelname";
		rtn[9] = "city_name";
		rtn[10] = "country_code";
		rtn[11] = "device_model";
		rtn[12] = "deviceid";
		rtn[13] = "deviceid_type";
		rtn[14] = "operation_type";
		rtn[15] = "os_version";
		rtn[16] = "pe_pkgname";
		rtn[17] = "pe_vercode";
		rtn[18] = "pe_version";
		rtn[19] = "pepollversion";

		rtn[20] = "miniute_5";
		rtn[21] = "hour";

		return rtn;

	}

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	@Override
	public DataIter parseLine(String line) throws InvalidEntryException {
		DataIter di = null;

		try {
			ErrorDataEntry de = ErrorDataEntry.parseLine(line);
			
			if (de == null) {
				return null;
			}
			
			if (de.getPid() == null || de.getLogTime() == null) {
				return null;
			}
			
			setMdrillFields(de);
			stringFieldNullCheck(de);

			this.lines++;
			if (this.lines > 100000) {
				this.laststartts = (System.currentTimeMillis() / 1000) - TS_MAX;
				this.lastendts = (System.currentTimeMillis() / 1000) + TS_MAX;
				this.lines = 0;
			}

			long ts = de.getDate().getTime() / 1000;
			if (ts < laststartts || ts > lastendts) {
				return null;
			}

			di = new ErrorDataIter(de);
			return di;
			//return null;
		} catch (Throwable nfe) {
			if (groupCreateerror < 10) {
				LOG.error("InvalidEntryException:" + line, nfe);
				groupCreateerror++;
			}

			throw new InvalidEntryException("Invalid log: " + line + "\n", nfe);
		}
	}
	
	private void setMdrillFields(ErrorDataEntry de) {
		de.setDate(de.getLogTime());
	}

	protected void stringFieldNullCheck(ErrorDataEntry de) {
		String nullValue = UNKNOWN;
		
		if (StringUtil.isEmpty(de.getAdId())) {
			de.setAdId(nullValue);
		}
		if (StringUtil.isEmpty(de.getResult())) {
			de.setResult(nullValue);
		}
		if (StringUtil.isEmpty(de.getType())) {
			de.setType(nullValue);
		}
		if (StringUtil.isEmpty(de.getPackageName())) {
			de.setPackageName(nullValue);
		}
		if (StringUtil.isEmpty(de.getTargetVersion())) {
			de.setTargetVersion(nullValue);
		}
		
		if (StringUtil.isEmpty(de.getApn())) {
			de.setApn(nullValue);
		}
		if (StringUtil.isEmpty(de.getCellId())) {
			de.setCellId(nullValue);
		}
		if (StringUtil.isEmpty(de.getChannelName())) {
			de.setChannelName(nullValue);
		}
		if (StringUtil.isEmpty(de.getChargeStatus())) {
			de.setChargeStatus(nullValue);
		}
		if (StringUtil.isEmpty(de.getCityName())) {
			de.setCityName(nullValue);
		}
		if (StringUtil.isEmpty(de.getCountryCode())) {
			de.setCountryCode(nullValue);
		}
		if (StringUtil.isEmpty(de.getCustVersion())) {
			de.setCustVersion(nullValue);
		}
		if (StringUtil.isEmpty(de.getDeviceId())) {
			de.setDeviceId(nullValue);
		}
		if (StringUtil.isEmpty(de.getDeviceIdType())) {
			de.setDeviceIdType(nullValue);
		}
		if (StringUtil.isEmpty(de.getDeviceIMSI())) {
			de.setDeviceIMSI(nullValue);
		}
		if (StringUtil.isEmpty(de.getDeviceModel())) {
			de.setDeviceModel(nullValue);
		}
		
		if (StringUtil.isEmpty(de.getIp())) {
			de.setIp(nullValue);
		}
		if (StringUtil.isEmpty(de.getLatitude())) {
			de.setLatitude(nullValue);
		}
		if (StringUtil.isEmpty(de.getLocId())) {
			de.setLocId(nullValue);
		}
		if (StringUtil.isEmpty(de.getLongitude())) {
			de.setLongitude(nullValue);
		}
		if (StringUtil.isEmpty(de.getNetaccessType())) {
			de.setNetaccessType(nullValue);
		}
		if (StringUtil.isEmpty(de.getOperationType())) {
			de.setOperationType(nullValue);
		}
		if (StringUtil.isEmpty(de.getOperatorCode())) {
			de.setOperatorCode(nullValue);
		}
		if (StringUtil.isEmpty(de.getOsVersion())) {
			de.setOsVersion(nullValue);
		}
		if (StringUtil.isEmpty(de.getPePkgName())) {
			de.setPePkgName(nullValue);
		}
		if (StringUtil.isEmpty(de.getPePollVersion())) {
			de.setPePollVersion(nullValue);
		}
		if (StringUtil.isEmpty(de.getPeVerCode())) {
			de.setPeVerCode(nullValue);
		}
		if (StringUtil.isEmpty(de.getPeVersion())) {
			de.setPeVersion(nullValue);
		}
		if (StringUtil.isEmpty(de.getPid())) {
			de.setPid(nullValue);
		}
		if (StringUtil.isEmpty(de.getSysId())) {
			de.setSysId(nullValue);
		}
		
	}

	public class ErrorDataIter extends MyDataIter {

		public ErrorDataIter(ErrorDataEntry de) {
			super(de);
		}

		@Override
		public Object[] getGroup() {

			long ts = de.getDate().getTime();
			long ts300 = (ts / (300 * 1000)) * 300000;
			Date d = new Date(ts300);

			String hour = String
					.valueOf(DateUtil.dateToString(d, HOUR_PATTERN));
			String min5 = String.valueOf(DateUtil.dateToString(d, MIN_PATTERN));

			return new String[] {
					DateUtil.dateToString(de.getDate(), THE_DATE_PATTERN),

					de.getAdId(),
					de.getPid(),
					de.getResult(),
					de.getType(),
					de.getPackageName(),
					de.getTargetVersion(),
					// hitTime不存储，用其他字段代替
					// DateUtil.dateToString(de.getHitTime(),
					// DATE_TIME_PATTERN),
					"0",

					de.getChannelName(), de.getCityName(), de.getCountryCode(), de.getDeviceModel(),
					de.getDeviceId(), de.getDeviceIdType(),
					de.getOperationType(), de.getOsVersion(),
					de.getPePkgName(), de.getPeVerCode(), de.getPeVersion(),
					de.getPePollVersion(), min5, hour };

		}

	}

	@Override
	public String[] getSumName() {
		String[] rtn = new String[1];
		rtn[0] = "records";
		return rtn;
	}
}
