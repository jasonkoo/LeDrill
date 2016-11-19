package com.lenovo.lps.push.marketing.drill.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE_PATTERN = "yyyyMMdd";
	
	public final static String MIN_PATTERN = "HHmm";
	public final static String HOUR_PATTERN = "HH";

	public static String dateToString(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		} else {
			return null;
		}
	}

	public static Date stringToDate(String dateStr, String pattern)
			throws ParseException {
		if (dateStr != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.parse(dateStr);
		} else {
			return null;
		}
	}
}
