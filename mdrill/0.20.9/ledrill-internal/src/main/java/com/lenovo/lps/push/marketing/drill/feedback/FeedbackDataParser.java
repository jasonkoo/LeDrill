package com.lenovo.lps.push.marketing.drill.feedback;

import java.util.Date;

import org.apache.log4j.Logger;

import com.alimama.mdrillImport.DataParser;
import com.alimama.mdrillImport.InvalidEntryException;
import com.lenovo.lps.push.marketing.drill.common.util.DateUtil;
import com.lenovo.lps.push.marketing.drill.common.util.StringUtil;
import com.lenovo.lps.push.marketing.drill.common.vo.FeedbackDataEntry;

public class FeedbackDataParser extends DataParser {

	private static Logger LOG = Logger.getLogger(FeedbackDataParser.class);

	protected final static String THE_DATE_PATTERN = DateUtil.DATE_PATTERN;
	//protected final static String DATE_TIME_PATTERN = DateUtil.DATE_TIME_PATTERN;
	protected final static String MIN_PATTERN = DateUtil.MIN_PATTERN;
	protected final static String HOUR_PATTERN = DateUtil.HOUR_PATTERN;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1254279073242843742L;
	private final static String TABLE_NAME = "feedback";
	public final static int SUM_LENGTH = FeedbackDataEntry.COUNTS_ARRAY_LENGTH + 1;

	protected volatile long lines = 0;
	protected static long TS_MAX = 3600l * 24 * 31;

	protected volatile long laststartts = (System.currentTimeMillis() / 1000)
			- TS_MAX;
	protected volatile long lastendts = (System.currentTimeMillis() / 1000)
			+ TS_MAX;
	protected volatile long groupCreateerror = 0;
	
	protected final static String NIL = "nil";
	protected final static String UNKNOWN = "unknown";

	@Override
	public String[] getGroupName() {
		String[] rtn = new String[8];

		rtn[0] = "thedate";

		rtn[1] = "ad_id";
		rtn[2] = "pid";
		rtn[3] = "ac_id";
		rtn[4] = "ad_type";
		rtn[5] = "log_time";

		// liuhk2: ctrl + cv typo
		rtn[6] = "miniute_5";
		rtn[7] = "hour";

		return rtn;

	}

	// private static String[] sumName = {};

	@Override
	public String[] getSumName() {
		// return sumName;
		String[] rtn = new String[SUM_LENGTH];

		for (int i = 0; i < SUM_LENGTH - 1; i++) {
			rtn[i] = "col_" + i;
		}
		rtn[SUM_LENGTH - 1] = "records";
		return rtn;
	}

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	@Override
	public DataIter parseLine(String line) throws InvalidEntryException {
		// LOG.info("parseLine");
		DataIter di = null;

		try {
			FeedbackDataEntry de = FeedbackDataEntry.parseLine(line);
			
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

			di = new FeedBackDataIter(de);
			//return null;
			return di;
		} catch (Throwable nfe) {
			if (groupCreateerror < 10) {
				LOG.error("InvalidEntryException:" + line, nfe);
				groupCreateerror++;
			}

			throw new InvalidEntryException("Invalid log: " + line + "\n", nfe);
		}
	}
	
	protected void setMdrillFields(FeedbackDataEntry de) {
		de.setDate(de.getLogTime());
		
		int et = Integer.parseInt(de.getEventType());
		long[] countsArray = new long[FeedbackDataEntry.COUNTS_ARRAY_LENGTH];
		for (int i = 0; i < FeedbackDataEntry.COUNTS_ARRAY_LENGTH; i++) {
			if (i == et) {
				countsArray[i] = 1L;
			} else {
				countsArray[i] = 0L;
			}
		}
		de.setCountsArray(countsArray);
	}

	protected void stringFieldNullCheck(FeedbackDataEntry de) {
		
		String nullValue = UNKNOWN;
		
		if (StringUtil.isEmpty(de.getAcId())) {
			de.setAcId(nullValue);
		}
		if (StringUtil.isEmpty(de.getAdId())) {
			de.setAdId(nullValue);
		}
		if (StringUtil.isEmpty(de.getAdType())) {
			de.setAdType(nullValue);
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
		if (StringUtil.isEmpty(de.getEventType())) {
			de.setEventType(nullValue);
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

	public class FeedBackDataIter extends MyDataIter {

		public FeedBackDataIter(FeedbackDataEntry de) {
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
					de.getAdId(), de.getPid(), de.getAcId(), de.getAdType(),
					// logTime不存储，用其他字段代替
					// DateUtil.dateToString(de.getLogTime(),
					// DATE_TIME_PATTERN),
					"0", min5, hour };

		}
	}

}
