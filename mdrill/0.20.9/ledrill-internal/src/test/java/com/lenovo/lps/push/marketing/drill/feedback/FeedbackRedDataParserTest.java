package com.lenovo.lps.push.marketing.drill.feedback;

import java.util.Date;

import junit.framework.TestCase;

import com.alimama.mdrillImport.DataParser.DataIter;
import com.alimama.mdrillImport.InvalidEntryException;
import com.lenovo.lps.push.marketing.drill.common.util.DateUtil;
import com.lenovo.lps.push.marketing.drill.test.datagenerator.DataGenerator;

public class FeedbackRedDataParserTest extends TestCase {

	public void testParseLine() {
		// fail("Not yet implemented");
		//String deStr = "adId_1395893671207|pid_1395893671207|2|acId_1395893671207|adType_1395893671207|2014-03-27 12:14:31|channelName_1395893671207|cityName_1395893671207|deviceModel_1395893671207|deviceId_1395893671207|deviceIdType_1395893671207|operationType_1395893671207|osVersion_1395893671207|pePkgName_1395893671207|peVerCode_1395893671207|peVersion_1395893671207|pePollVersion_1395893671207";
		String deStr = DataGenerator.getFeedbackDataEntry().toStringForParsing();
		// String deStr =
		// "adId_1395734612108|pid_1395734612108|7|acId_1395734612108|adType_1395734612108|2014-03-25 16:03:32|1395734612108|apn_1395734612108|cellId_1395734612108|channelName_1395734612108|chargeStatus_1395734612108|cityName_1395734612108|countryCode_1395734612108|2014-03-25 16:03:32|custVersion_1395734612108|deviceIMSI_1395734612108|deviceModel_1395734612108|deviceId_1395734612108|deviceIdType_1395734612108|ip_1395734612108|latitude_1395734612108|locId_1395734612108|longitude_1395734612108|2014-03-25 16:03:32|netaccessType_1395734612108|operationType_1395734612108|operatorCode_1395734612108|osVersion_1395734612108|pePkgName_1395734612108|peVerCode_1395734612108|peVersion_1395734612108|pePollVersion_1395734612108|sysId_1395734612108";
		System.out.println(deStr);
		FeedbackRedDataParser parser = new FeedbackRedDataParser();
		try {
			DataIter di = parser.parseLine(deStr);
			System.out.println(di);
			//assertEquals(di, null);
			 Object[] aaa = di.getGroup();
			 System.out.println(aaa);
			 System.out.println(di.getTs());
			 aaa = di.getSum();
			 System.out.println(aaa);
		} catch (InvalidEntryException e) {
			e.printStackTrace();
		}
	}

//	public void testParseLineNull() {
//		Date now = new Date();
//		String nowStr = DateUtil.dateToString(now, DateUtil.DATE_TIME_PATTERN);
//
//		String deStr = "adId_1401945597398|pid_1401945597398|2|acId_1401945597398|adType_1401945597398|"
//				+ nowStr
//				+ "|1401945597398|apn_1401945597398|cellId_1401945597398|channelName_1401945597398|chargeStatus_1401945597398|null|countryCode_1401945597398|2014-06-05 13:19:57|custVersion_1401945597398|deviceIMSI_1401945597398|deviceModel_1401945597398|deviceId_1401945597398|deviceIdType_1401945597398|ip_1401945597398|latitude_1401945597398|locId_1401945597398|longitude_1401945597398|2014-06-05 13:19:57|netaccessType_1401945597398|operationType_1401945597398|operatorCode_1401945597398|osVersion_1401945597398|pePkgName_1401945597398|peVerCode_1401945597398|peVersion_1401945597398|pePollVersion_1401945597398|sysId_1401945597398";
//		System.out.println(deStr);
//		FeedbackRedDataParser parser = new FeedbackRedDataParser();
//		try {
//			DataIter di = parser.parseLine(deStr);
//			System.out.println(di);
//			Object[] aaa = di.getGroup();
//			System.out.println(aaa);
//			System.out.println(di.getTs());
//			aaa = di.getSum();
//			System.out.println(aaa);
//			assertEquals("unknown",di.getGroup()[7]);
//		} catch (InvalidEntryException e) {
//			e.printStackTrace();
//		}
//	}

}
