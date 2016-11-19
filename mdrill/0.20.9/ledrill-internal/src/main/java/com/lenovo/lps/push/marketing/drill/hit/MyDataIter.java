package com.lenovo.lps.push.marketing.drill.hit;

import com.alimama.mdrillImport.DataParser.DataIter;
import com.lenovo.lps.push.marketing.drill.common.vo.HitDataEntry;

public abstract class MyDataIter implements DataIter {

	protected HitDataEntry de;

	public MyDataIter(HitDataEntry de) {
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
		return (ts / (10 * 1000)) * 10000;
	}

	@Override
	public abstract Object[] getGroup();

	@Override
	public Number[] getSum() {
		return new Number[] { 1L };

	}

}
