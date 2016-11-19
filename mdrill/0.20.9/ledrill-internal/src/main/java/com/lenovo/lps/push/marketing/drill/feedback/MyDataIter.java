package com.lenovo.lps.push.marketing.drill.feedback;

import com.alimama.mdrillImport.DataParser.DataIter;
import com.lenovo.lps.push.marketing.drill.common.vo.FeedbackDataEntry;

public abstract class MyDataIter implements DataIter {

	protected FeedbackDataEntry de;

	public MyDataIter(FeedbackDataEntry de) {
		super();
		this.de = de;
	}

	@Override
	public boolean next() {
		return false;
	}

	@Override
	public long getTs() {
		long ts = de.getDate().getTime();
		return (ts / (10*1000)) * 10000;
	}

	@Override
	public abstract Object[] getGroup();

	@Override
	public Number[] getSum() {
		int n = FeedbackRedDataParser.SUM_LENGTH;
		Number[] rtn = new Number[n];
		long[] a = de.getCountsArray();
		for (int i = 0; i < n - 1; i++) {
			rtn[i] = a[i];
		}
		rtn[n - 1] = 1;
		return rtn;
	}

}
