package com.alimama.mdrill.jdbc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.alimama.mdrill.json.JSONArray;
import com.alimama.mdrill.json.JSONException;
import com.alimama.mdrill.json.JSONObject;


public class MdrillRequest {
	SqlParser parser;

	String strurl;
	
  
	
	private static HttpClient httpClient;

	public MdrillRequest(SqlParser parser, String strurl) {
		super();
		this.parser = parser;
		this.strurl = strurl;
	}

	static{
		int maxConnectionsPerAddress = 1000;
		httpClient = new HttpClient();
		httpClient.setMaxConnectionsPerAddress(maxConnectionsPerAddress);
		httpClient.setThreadPool(new QueuedThreadPool(250));
		try {
			httpClient.start();
		} catch (Exception e) {
			//logger.fatal("PollingHandler init faild!",e);
			e.printStackTrace();
		}
	}
	
	String content="";
	
	public Long request(List<List<Object>> results) throws ClientProtocolException, IOException, JSONException, InterruptedException {

		if (this.strurl.indexOf("higoself") >= 0) {
//			try {
//				text=DownLoad.result(parser.tablename, "", parser.start, parser.rows, parser.queryStr, "",  parser.fl, parser.groupby, parser.sort, parser.order).trim();
//			} catch (Exception e) {
//				throw new IOException(e);
//			}
		
		} else {
			/*
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000*30);
			 httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF8");
			 */
			
			
			 ContentExchange exchange = new ContentExchange(true)
			 {
				 @Override
			     protected void onResponseComplete() throws IOException
			     {
			         int status = getResponseStatus();
			         if (status == 200)
			        	 content =  (new String(getResponseContentBytes(), "utf-8")).trim();
	
			     }
			     
			     
			     
			 };

			 
			 exchange.setURL("http://" + this.strurl
						+ "/higo/result.jsp");


			 
			 /*
			HttpPost httppost = new HttpPost("http://" + this.strurl
					+ "/higo/result.jsp");
					*/
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("project",parser.tablename));
			nameValuePairs.add(new BasicNameValuePair("start", parser.start));
			nameValuePairs.add(new BasicNameValuePair("rows", parser.rows));
			nameValuePairs.add(new BasicNameValuePair("fl", parser.fl));
			if (parser.queryStr != null) {
				nameValuePairs
						.add(new BasicNameValuePair("q", parser.queryStr));
			}
			if (parser.groupby != null) {
				nameValuePairs.add(new BasicNameValuePair("groupby",
						parser.groupby));
			}
			if (parser.sort != null) {
				nameValuePairs.add(new BasicNameValuePair("sort", parser.sort));
			}
			if (parser.order != null) {
				nameValuePairs
						.add(new BasicNameValuePair("order", parser.order));
			}

			/*
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			*/

			
			 exchange.setRequestContentSource(new UrlEncodedFormEntity(nameValuePairs,"UTF-8").getContent());
			 //exchange.setRequestContent(requestContent);
			 exchange.setRequestContentType("application/x-www-form-urlencoded;charset=utf-8");
			 exchange.setMethod("POST");
			 exchange.setRequestHeader("Connection", "keep-alive");
			 exchange.setTimeout(30*1000);
			 
			
			 httpClient.send(exchange);
			 int exchangeState = exchange.waitForDone();
	
			
			 /*
			HttpResponse response = httpclient.execute(httppost);

			InputStream is = response.getEntity().getContent();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(1024);

			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			

			text = (new String(baf.toByteArray(), "utf-8")).trim();
			*/
		}
		
		

		JSONObject jsonObj = new JSONObject(content);
		if(!"1".equals(jsonObj.get("code")))
		{
			return -1l;
		}
		Long total=jsonObj.getLong("total");
		JSONObject data=jsonObj.getJSONObject("data");
		JSONArray list=data.getJSONArray("docs");
		for(int i=0;i<list.length();i++)
		{
			JSONObject rowMap=list.getJSONObject(i);
			ArrayList<Object> row=new ArrayList<Object>();
			for(int j=0;j<this.parser.colsNames.length;j++)
			{
				String colname=this.parser.colsNames[j];
				row.add(j, String.valueOf(rowMap.opt(colname)));
			}
			results.add(row);
		}
		return total;

	}
}
