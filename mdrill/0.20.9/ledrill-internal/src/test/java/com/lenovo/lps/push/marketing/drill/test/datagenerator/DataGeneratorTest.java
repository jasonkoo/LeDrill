package com.lenovo.lps.push.marketing.drill.test.datagenerator;

import junit.framework.TestCase;

public class DataGeneratorTest extends TestCase {

	public void testGetFeedbackDataEntry() {
		for (int i = 10; i < 1; i++) {
			System.out.println("testGetFeedbackDataEntry");
			System.out.println(DataGenerator.getFeedbackDataEntry().toStringForParsing());
		}
	}

	public void testGetHitDataEntry() {
		//System.out.println("testGetAdHitDataEntry");
		//for (int i = 0; i < 0; i++) {
		for (int i = 0; i < 10000; i++) {
			System.out.println(DataGenerator.getHitDataEntry().toStringForParsing());
		}

	}

	public void testGetUserDataEntry() {
		for (int i = 10; i < 1; i++) {
			System.out.println("testGetUserDataEntry");
			System.out.println(DataGenerator.getUserDataEntry().toStringForParsing());
		}
	}
	
	public void testGetErrorDataEntry() {
		for (int i = 10; i < 1; i++) {
			System.out.println("testGetErrorDataEntry");
			System.out.println(DataGenerator.getErrorDataEntry().toStringForParsing());
		}
	}

}
