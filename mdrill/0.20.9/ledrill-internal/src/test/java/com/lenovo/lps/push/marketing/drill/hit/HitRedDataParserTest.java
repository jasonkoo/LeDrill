package com.lenovo.lps.push.marketing.drill.hit;

import junit.framework.TestCase;

import com.alimama.mdrillImport.DataParser.DataIter;
import com.alimama.mdrillImport.InvalidEntryException;
import com.lenovo.lps.push.marketing.drill.test.datagenerator.DataGenerator;

public class HitRedDataParserTest extends TestCase {
	public void testParseLine() {
		// fail("Not yet implemented");
		// String deStr = HitDataEntry.getDataEntryAsStringForParsing();
		String deStr = DataGenerator.getHitDataEntry().toStringForParsing();
		//String deStr = "null|-1|1|2014-06-06 20:00:54|0|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null";
//		String deStr = "{\"result\":\"2\","
//				+ "\"hitTime\":\"Jun 11, 2014 10:50:58 PM\"," 
//		+ "\"firstTime\":false,"
//		+ "\"pid\":\"146608397\","
//		+ "\"accessNum\":262,\"apn\":"
//		+ "\"MERCURY_0C7614\",\"cellId\":\"38993\"," +
//"\"chargeStatus\":\"02\",\"cityName\":\"Nanning\",\"countryCode\":\"CN_16\",\"createDate\":\"May 26, 2014 3:05:56 AM\","
//		+ "\"deviceIMSI\":\"460028206274496\",\"deviceId \":\"\",\"ip\":\"120.85.78.160\",\"locIdv\":\"10332\"," 
//+ "\"modifyDate\":\"Jun 11, 2014 10:49:11 PM\",\"netaccessType\":\"2\",\"operationType\":\"MOB\","
//+ "\"operatorCode\":\"46 002\",\"pePkgName\":\"com.lenovo.lsf.device\","
//+ "\"peVersion\":\"v4.0.1.0_over\",\"pePollVersion\":\"03\",\"sysId\":\"46000\"}";
		
//		String deStr = "{\"result\":\"3\","
//				+ "\"hitTime\":\"Jul 12, 2014 9:38:23 AM\","
//				+ "\"firstTime\":false,\"pid\":\"85984443\","
//				+ "\"accessNum\":7802,"
//				+ "\"apn\":\"JSZX\","
//				+ "\"cellId\":\"49182\","
//				+ "\"chargeStatus\":\"01\","
//				+ "\"cityName\":\"Beijing\","
//				+ "\"countryCode\":\"CN_22\","
//				+ "\"createDate\":\"Jun 3, 2014 11:20:42 AM\","
//				+ "\"custVersion\":\"Lenovo_A820_S135_130322\","
//				+ "\"deviceIMSI\":\"460017357411196\","
//				+ "\"deviceModel\":\"Lenovo A820\",\"deviceId\":\"869186014996265\","
//				+ "\"deviceIdType\":\"imei\",\"ip\":\"116.1.83.45\","
//				+ "\"locId\":\"26930\",\"modifyDate\":\"Jun 12, 2014 8:44:56 AM\","
//						+ "\"netaccessType\":\"2\",\"operationType\":\"TEL\",\"operatorCode\":\"46001\",\"osVersion\":\"4.1.2\","
//						+ "\"pePkgName\":\"com.lenovo.lsf.device\",\"peVerCode\":\"401020693\",\"peVersion\":\"V4.1.2.0693pi\","
//						+ "\"pePollVersion\":\"03\",\"sysId\":\"46001\"}";
		System.out.println(deStr);
		HitRedDataParser parser = new HitRedDataParser();
		try {
			DataIter di = parser.parseLine(deStr);
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
