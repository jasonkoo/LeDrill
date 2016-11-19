package com.lenovo.lps.push.marketing.drill.common.vo;

import junit.framework.TestCase;

public class UserDataEntryTest extends TestCase {

	private UserDataEntry de;
	
	protected void setUp() throws Exception {
		super.setUp();
		de = new UserDataEntry();
		de.setCustVersion("MocorDroid4.0_13A_W14.02|01-06-2014_10:48:56|sp8825c1plus-user");
		de.setChannelName("MocorDro\rid4.");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testToStringForParsing() {
		//fail("Not yet implemented");
		System.out.println(de.toStringForParsing());
	}

	public void testParseLine() {
		//fail("Not yet implemented");
		String str = de.toStringForParsing();
		UserDataEntry aaa = UserDataEntry.parseLine(str);
		System.out.println(aaa.getCustVersion());
		System.out.println(aaa.getChannelName());

	}

}
