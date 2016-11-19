package com.lenovo.lps.push.marketing.drill.common.util;

public class NumberUtil {
	public static String longToString(long l) {
		return Long.toString(l);
	}

	public static long stringToLong(String lStr) {
		return Long.parseLong(lStr);
	}

	public static int getRandomInt(int n) {
		return (int) (Math.random() * n);
	}

	public static void main(String[] args) {
		test001();
	}

	private static void test001() {
		for (int i = 0; i < 1000; i++) {
			System.out.println("i=" + getRandomInt(10));
		}
	}
}
