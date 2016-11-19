package com.lenovo.lps.push.marketing.drill.user;

import junit.framework.TestCase;

import com.alimama.mdrillImport.DataParser.DataIter;
import com.alimama.mdrillImport.InvalidEntryException;
import com.lenovo.lps.push.marketing.drill.test.datagenerator.DataGenerator;
import com.lenovo.lps.push.marketing.drill.user.UserDataParser;

public class UserDataParserTest extends TestCase {

	public void testParseLine() {
		// fail("Not yet implemented");

		// String deStr = UserDataEntry.getDataEntryAsStringForParsing();
		String deStr = DataGenerator.getUserDataEntry().toStringForParsing();
		System.out.println(deStr);
		UserDataParser parser = new UserDataParser();
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
