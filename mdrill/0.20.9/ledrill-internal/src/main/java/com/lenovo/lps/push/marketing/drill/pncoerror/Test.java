package com.lenovo.lps.push.marketing.drill.pncoerror;

import org.apache.commons.lang.StringUtils;
import com.alimama.mdrillImport.DataParser.DataIter;
import com.alimama.mdrillImport.InvalidEntryException;
import com.lenovo.lps.push.marketing.drill.common.vo.ErrorDataEntry;

public class Test {

	public static void main(String[] args) {
		String line="{\"adId\":\"rinter2_2c91bc5542377df3014237c2e8f00003\",\"result\":\"ERROR_INVALID_TIME\",\"type\":\"\",\"logTime\":\"Feb 26, 2015 5:48:31 PM\",\"pid\":\"44575383\",\"accessNum\":0,\"apn\":\"MERCURY_7FC15A\",\"channelName\":\"com.lenovo.lsf.device\",\"chargeStatus\":\"02\",\"cityName\":\"Shenyang\",\"countryCode\":\"CN_19\",\"custVersion\":\"Lenovo_P770_S128_130807\",\"deviceIMSI\":\"460023421214498\",\"deviceModel\":\"Lenovo P770\",\"deviceId\":\"866959013063524\",\"deviceIdType\":\"imei\",\"ip\":\"175.151.165.21\",\"modifyDate\":\"Feb 11, 2015 9:55:24 AM\",\"netaccessType\":\"2\",\"operationType\":\"UNI\",\"operatorCode\":\"46002\",\"osVersion\":\"4.1.1\",\"pePkgName\":\"com.lenovo.lsf.device\",\"peVerCode\":\"403071184\",\"peVersion\":\"V4.3.7.1184pi\",\"pePollVersion\":\"03\"}";
		ErrorDataParser parser = new ErrorDataParser();
		try {
			DataIter di = parser.parseLine(line);
			System.out.println(di.getGroup());
		} catch (InvalidEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}

}
