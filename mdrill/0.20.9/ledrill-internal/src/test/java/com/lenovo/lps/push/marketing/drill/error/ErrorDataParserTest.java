package com.lenovo.lps.push.marketing.drill.error;

import junit.framework.TestCase;

import com.alimama.mdrillImport.DataParser.DataIter;
import com.alimama.mdrillImport.InvalidEntryException;
import com.lenovo.lps.push.marketing.drill.test.datagenerator.DataGenerator;

public class ErrorDataParserTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testParseLine() {
		// fail("Not yet implemented");
		//String deStr = "adId_1395893671207|pid_1395893671207|2|acId_1395893671207|adType_1395893671207|2014-03-27 12:14:31|channelName_1395893671207|cityName_1395893671207|deviceModel_1395893671207|deviceId_1395893671207|deviceIdType_1395893671207|operationType_1395893671207|osVersion_1395893671207|pePkgName_1395893671207|peVerCode_1395893671207|peVersion_1395893671207|pePollVersion_1395893671207";
		String deStr = DataGenerator.getErrorDataEntry().toStringForParsing();
		// String deStr =
		// "adId_1395734612108|pid_1395734612108|7|acId_1395734612108|adType_1395734612108|2014-03-25 16:03:32|1395734612108|apn_1395734612108|cellId_1395734612108|channelName_1395734612108|chargeStatus_1395734612108|cityName_1395734612108|countryCode_1395734612108|2014-03-25 16:03:32|custVersion_1395734612108|deviceIMSI_1395734612108|deviceModel_1395734612108|deviceId_1395734612108|deviceIdType_1395734612108|ip_1395734612108|latitude_1395734612108|locId_1395734612108|longitude_1395734612108|2014-03-25 16:03:32|netaccessType_1395734612108|operationType_1395734612108|operatorCode_1395734612108|osVersion_1395734612108|pePkgName_1395734612108|peVerCode_1395734612108|peVersion_1395734612108|pePollVersion_1395734612108|sysId_1395734612108";
		System.out.println(deStr);
		ErrorDataParser parser = new ErrorDataParser();
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
}
