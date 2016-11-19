package com.lenovo.lps.push.marketing.drill.hit;

import java.util.Date;

import org.apache.log4j.Logger;

import com.alimama.mdrillImport.InvalidEntryException;
import com.lenovo.lps.push.marketing.drill.common.util.DateUtil;
import com.lenovo.lps.push.marketing.drill.common.vo.HitDataEntry;

public class HitRedDataParser extends HitDataParser {

	private static Logger LOG = Logger.getLogger(HitRedDataParser.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 3014783528983618914L;

	public final static String TABLE_NAME = "hitred";

	@Override
	public String[] getGroupName() {
		String[] rtn = new String[19];
		rtn[0] = "thedate";

		rtn[1] = "ad_id";
		rtn[2] = "pid";
		rtn[3] = "result";
		rtn[4] = "hit_time";

		rtn[5] = "channelname";
		rtn[6] = "city_name";
		rtn[7] = "country_code";
		rtn[8] = "device_model";
		rtn[9] = "deviceid";
		rtn[10] = "deviceid_type";
		rtn[11] = "operation_type";
		rtn[12] = "os_version";
		rtn[13] = "pe_pkgname";
		rtn[14] = "pe_vercode";
		rtn[15] = "pe_version";
		rtn[16] = "pepollversion";

		rtn[17] = "miniute_5";
		rtn[18] = "hour";

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

			di = new HitRedDataIter(de);
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

	public class HitRedDataIter extends MyDataIter {

		public HitRedDataIter(HitDataEntry de) {
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
}
