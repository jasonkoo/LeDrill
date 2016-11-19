package com.lenovo.lps.push.marketing.drill.pncofeedback;

import java.util.Date;

import org.apache.log4j.Logger;

import com.alimama.mdrillImport.InvalidEntryException;
import com.lenovo.lps.push.marketing.drill.common.util.DateUtil;
import com.lenovo.lps.push.marketing.drill.common.vo.FeedbackDataEntry;

public class FeedbackRedDataParser extends FeedbackDataParser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5471033221815609961L;

	private static Logger LOG = Logger.getLogger(FeedbackRedDataParser.class);

	private final static String TABLE_NAME = "pncofeedbackred";
	
	@Override
	public String[] getGroupName() {
		String[] rtn = new String[20];
		rtn[0] = "thedate";

		rtn[1] = "ad_id";
		rtn[2] = "pid";
		rtn[3] = "ac_id";
		rtn[4] = "ad_type";
		rtn[5] = "log_time";

		rtn[6] = "channelname";
		rtn[7] = "city_name";
		rtn[8] = "country_code";
		rtn[9] = "device_model";
		rtn[10] = "deviceid";
		rtn[11] = "deviceid_type";
		rtn[12] = "operation_type";
		rtn[13] = "os_version";
		rtn[14] = "pe_pkgname";
		rtn[15] = "pe_vercode";
		rtn[16] = "pe_version";
		rtn[17] = "pepollversion";

		rtn[18] = "miniute_5";
		rtn[19] = "hour";

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

			di = new FeedBackRedDataIter(de);
			LOG.info("xxxxxx!" + de.getAdId()  + "!" +  de.getDeviceId() +  "!" + de.getPid() + "!xxxxxx");
			return di;
		
		} catch (Throwable nfe) {
			if (groupCreateerror < 10) {
				LOG.error("InvalidEntryException:" + line, nfe);
				groupCreateerror++;
			}

			throw new InvalidEntryException("Invalid log: " + line + "\n", nfe);
		}
	}

	public class FeedBackRedDataIter extends MyDataIter {

		public FeedBackRedDataIter(FeedbackDataEntry de) {
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
					de.getAcId(),
					de.getAdType(),
					// logTime不存储，用其他字段代替
					// DateUtil.dateToString(de.getLogTime(),
					// DATE_TIME_PATTERN),
					"0",

					de.getChannelName(), de.getCityName(), de.getCountryCode(), de.getDeviceModel(),
					de.getDeviceId(), de.getDeviceIdType(),
					de.getOperationType(), de.getOsVersion(),
					de.getPePkgName(), de.getPeVerCode(), de.getPeVersion(),
					de.getPePollVersion(), min5, hour };

		}

	}

}
