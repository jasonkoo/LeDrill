package com.lenovo.lps.push.marketing.drill.hit;

import junit.framework.TestCase;

import com.alimama.mdrillImport.DataParser.DataIter;
import com.alimama.mdrillImport.InvalidEntryException;
import com.lenovo.lps.push.marketing.drill.test.datagenerator.DataGenerator;

public class HitDataParserTest extends TestCase {

	public void testParseLine() {
		// fail("Not yet implemented");
		// String deStr = HitDataEntry.getDataEntryAsStringForParsing();
		String deStr = DataGenerator.getHitDataEntry().toStringForParsing();
		//String deStr = "null|-1|1|2014-06-06 20:00:54|0|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null|null";
		//String deStr = "null|1005812007|4|2014-06-10 15:26:46|262|null|20351|com.lenovo.leos.appstore|null|Changsha|CN_11|2014-05-09 10:41:12|MocorDroid4.0_13A_W14.02|01-06-2014_10:48:56|sp8825c1plus-user|460021647250684|OBEE_K999|862232020766610|imei|117.136.3.47|null|18245|null|2014-06-06 00:01:04|2|TEL|null|4.0.3|com.lenovo.lsf.device|401020636|V4.1.2.0636si|03|46000";
		System.out.println(deStr);
		HitDataParser parser = new HitDataParser();
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
		// LOG.debug(MyDataEntry.getMyDataEntryAsString());
	}

}
