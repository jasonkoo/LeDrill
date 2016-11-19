package com.lenovo.lps.push.marketing.drill.test.datacontroller;

public class FunctionTestDataControllerImpl implements IDataController {

	private final static int BASE = 10;

	private final static int MIN_COUNT = 0;
	private final static int MAX_COUNT = 100;

	private static int count = MIN_COUNT;

	@Override
	public int getDataCount() {
		nextCount();
		//System.out.println("getDataCount(): count: " + count);
		if (count % BASE == 1) {
			return 1;
		}
		return 0;
	}

	private void nextCount() {
		count++;
		if (count > MAX_COUNT) {
			count = MIN_COUNT;
		}
	}

	public static void main(String[] args) {
		IDataController lc = new FunctionTestDataControllerImpl();
		int n = 3033;
		for (int i = 0; i < n; i++) {
			System.out.println("count: " + lc.getDataCount());
		}
	}

}
