package com.lenovo.lps.push.marketing.drill.feedback;

import java.util.Date;

import junit.framework.TestCase;

import com.alimama.mdrillImport.DataParser.DataIter;
import com.alimama.mdrillImport.InvalidEntryException;
import com.lenovo.lps.push.marketing.drill.common.util.DateUtil;
import com.lenovo.lps.push.marketing.drill.test.datagenerator.DataGenerator;

public class FeedbackDataParserTest extends TestCase {

	public void testParseLine() {
		//fail("Not yet implemented");
		//String deStr = "adId_1395887623676|pid_1395887623676|0|acId_1395887623676|adType_1395887623676|2014-03-27 10:33:43|channelName_1395887623676|cityName_1395887623676|deviceModel_1395887623676|deviceId_1395887623676|deviceIdType_1395887623676|operationType_1395887623676|osVersion_1395887623676|pePkgName_1395887623676|peVerCode_1395887623676|peVersion_1395887623676|pePollVersion_1395887623676";
		String deStr = DataGenerator.getFeedbackDataEntry().toStringForParsing();
		// String deStr =
		// "adId_1395734612108|pid_1395734612108|7|acId_1395734612108|adType_1395734612108|2014-03-25 16:03:32|1395734612108|apn_1395734612108|cellId_1395734612108|channelName_1395734612108|chargeStatus_1395734612108|cityName_1395734612108|countryCode_1395734612108|2014-03-25 16:03:32|custVersion_1395734612108|deviceIMSI_1395734612108|deviceModel_1395734612108|deviceId_1395734612108|deviceIdType_1395734612108|ip_1395734612108|latitude_1395734612108|locId_1395734612108|longitude_1395734612108|2014-03-25 16:03:32|netaccessType_1395734612108|operationType_1395734612108|operatorCode_1395734612108|osVersion_1395734612108|pePkgName_1395734612108|peVerCode_1395734612108|peVersion_1395734612108|pePollVersion_1395734612108|sysId_1395734612108";
		System.out.println(deStr);
		FeedbackDataParser parser = new FeedbackDataParser();
		try {
			DataIter di = parser.parseLine(deStr);
//			assertEquals(di, null);
			System.out.println(di);
			Object[] aaa = di.getGroup();
			System.out.println(aaa);
			System.out.println(di.getTs());
			aaa = di.getSum();
			System.out.println(aaa);
		} catch (InvalidEntryException e) {
			e.printStackTrace();
		}
	}
	
}
