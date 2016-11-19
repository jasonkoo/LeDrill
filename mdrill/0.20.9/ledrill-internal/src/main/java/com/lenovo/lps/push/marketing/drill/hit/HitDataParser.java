package com.lenovo.lps.push.marketing.drill.hit;

import java.util.Date;

import org.apache.log4j.Logger;

import com.alimama.mdrillImport.DataParser;
import com.alimama.mdrillImport.InvalidEntryException;
import com.lenovo.lps.push.marketing.drill.common.util.DateUtil;
import com.lenovo.lps.push.marketing.drill.common.util.StringUtil;
import com.lenovo.lps.push.marketing.drill.common.vo.HitDataEntry;

public class HitDataParser extends DataParser {

	private static Logger LOG = Logger.getLogger(HitDataParser.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -8073765362801007405L;
	public final static String TABLE_NAME = "hit";

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

	@Override
	public String[] getGroupName() {
		String[] rtn = new String[7];
		rtn[0] = "thedate";

		rtn[1] = "ad_id";
		rtn[2] = "pid";
		rtn[3] = "result";
		rtn[4] = "hit_time";

		rtn[5] = "miniute_5";
		rtn[6] = "hour";

		return rtn;

	}

	// private static String[] sumName = {};

	@Override
	public String[] getSumName() {
		// return sumName;
		String[] rtn = new String[1];
		rtn[0] = "records";
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
			HitDataEntry de = HitDataEntry.parseLine(line);
			
			if (de == null) {
				return null;
			}

			if (de.getPid() == null || de.getHitTime() == null) {
				return null;
			}
			
			setMdrillFields(de);
			checkPid(de);
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

			di = new HitDataIter(de);
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

	protected void setMdrillFields(HitDataEntry de) {
		de.setDate((de.getHitTime()));
	}

	protected void checkPid(HitDataEntry de) {
		// pid -1
		// user = null
		if (!"0".equals(de.getResult())  && !de.isFirstTime()) {
			de.setPid("-1");
			
			de.setApn(null);
			de.setCellId(null);
			de.setChannelName(null);
			de.setChargeStatus(null);
			de.setCityName(null);
			de.setCountryCode(null);
			de.setCustVersion(null);
			de.setDeviceId(null);
			de.setDeviceIdType(null);
			de.setDeviceIMSI(null);
			de.setDeviceModel(null);
			de.setIp(null);
			de.setLatitude(null);
			de.setLocId(null);
			de.setLongitude(null);
			de.setNetaccessType(null);
			de.setOperationType(null);
			de.setOperatorCode(null);
			de.setOsVersion(null);
			de.setPePkgName(null);
			de.setPePollVersion(null);
			de.setPeVerCode(null);
			de.setPeVersion(null);
			de.setSysId(null);
		}
	}

	protected void stringFieldNullCheck(HitDataEntry de) {
		String nullValue = UNKNOWN;
		if ("-1".equals(de.getPid())) {
			nullValue = NIL;
		}
		
		if (StringUtil.isEmpty(de.getAdId())) {
			de.setAdId(nullValue);
		}
		if (StringUtil.isEmpty(de.getResult())) {
			de.setResult(nullValue);
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

	public class HitDataIter extends MyDataIter {

		public HitDataIter(HitDataEntry de) {
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

					de.getAdId(), de.getPid(), de.getResult(),
					// hitTime不存储，用其他字段代替
					// DateUtil.dateToString(de.getHitTime(), DATE_TIME_PATTERN)
					"0", min5, hour };

		}
	}

}
