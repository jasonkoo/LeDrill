package com.lenovo.lps.push.marketing.drill.test.reader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.alimama.mdrillImport.ImportReader;

public class TTDataReader extends ImportReader.RawDataReader {

	// private static Logger LOG = Logger.getLogger(TTDataReader.class);
	//
	// private SubscribeFuture subscriber = null;

	@Override
	public void init(Map config, String confPrefix, int readerIndex,
			int readerCount) throws IOException {
		// LOG.error("lei: init");
		// // -DROUTER=localhost:9999 -DRPCTIMEOUT=30000
		// System.setProperty("ROUTER", "192.168.12.13:9090");
		// System.setProperty("RPCTIMEOUT", "30000");
		// // use authenticate
		// use(passport("userHit", "88888"));
		// // subscribe a topic
		//
		// try {
		// subscriber = subscribe(tunnel("hit", false, false, 1800, 200));
		// } catch (ClosedException e) {
		// // tunnel has been closed or TimeTunnel.release() has been called
		// IOException ie = new IOException(e.getMessage());
		// throw ie;
		// }

	}

	@Override
	public List<Object> read() throws IOException {
		// LOG.error("lei: read");
		// if (subscriber != null) {
		// LOG.error("lei: 111");
		// // retrieve message
		// List<Message> ms = subscriber.get();
		// List<Object> list = new ArrayList<Object>();
		// String str = null;
		// for (Iterator<Message> it = ms.iterator(); it.hasNext();) {
		// // LOG.error(asString(it.next()));
		// // list.add(asString(it.next()));
		// str = asString(it.next());
		// LOG.error("lei: str=" + str);
		// if (StringUtils.isEmpty(str)) {
		// list.add(str);
		// }
		// }
		//
		// return list;
		// } else {
		// return null;
		// }
		return null;
	}

	@Override
	public void close() throws IOException {
		// close sub
		// subscriber.cancel();
	}

}
