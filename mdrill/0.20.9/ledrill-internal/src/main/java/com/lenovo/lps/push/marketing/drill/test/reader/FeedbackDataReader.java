package com.lenovo.lps.push.marketing.drill.test.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alimama.mdrillImport.ImportReader;
import com.lenovo.lps.push.marketing.drill.test.datacontroller.FunctionTestDataControllerImpl;
import com.lenovo.lps.push.marketing.drill.test.datacontroller.IDataController;
import com.lenovo.lps.push.marketing.drill.test.datagenerator.DataGenerator;

public class FeedbackDataReader extends ImportReader.RawDataReader {

	private static Logger LOG = Logger.getLogger(FeedbackDataReader.class);

	public IDataController dataController;

	@Override
	public void init(Map config, String confPrefix, int readerIndex,
			int readerCount) throws IOException {
		LOG.info("readerIndex" + readerIndex);
		LOG.info("readerCount" + readerCount);
		this.dataController = new FunctionTestDataControllerImpl();
		//this.dataController = new PressureTestDataControllerImpl();
	}

	@Override
	public List<Object> read() throws IOException {

		if (dataController != null) {
			// 产生n行数据
			List<Object> list = new ArrayList<Object>();
			// int n = 10;
			int n = dataController.getDataCount();
			for (int i = 0; i < n; i++) {
				// LOG.debug("one line added");
				list.add(DataGenerator.getFeedbackDataEntry()
						.toStringForParsing());
			}
			return list;
		} else {
			// LOG.error("dataController is null");
			return null;
		}
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

}
