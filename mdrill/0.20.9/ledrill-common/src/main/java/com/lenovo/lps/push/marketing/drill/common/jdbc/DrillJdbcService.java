package com.lenovo.lps.push.marketing.drill.common.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.alimama.mdrill.jdbc.MdrillQueryResultSet;
import com.lenovo.lps.push.marketing.drill.common.util.DateUtil;

public class DrillJdbcService {

	private String host;
	private String port;
	private String connStr;

	protected final static String THE_DATE_PATTERN = DateUtil.DATE_PATTERN;

	public DrillJdbcService(String host, String port) {
		super();
		this.host = host;
		this.port = port;
		this.connStr = "jdbc:mdrill://" + host + ":" + port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	private final static String MDRILL_DRIVE_CLASS_NAME = "com.alimama.mdrill.jdbc.MdrillDriver";

	protected MdrillQueryResultSet select(String sqlStr)
			throws ClassNotFoundException, SQLException {

		Class.forName(MDRILL_DRIVE_CLASS_NAME);
		Connection con = DriverManager.getConnection(connStr, "", "");
		Statement stmt = con.createStatement();
		// long mil1 = System.currentTimeMillis();
		MdrillQueryResultSet res = null;
		res = (MdrillQueryResultSet) stmt.executeQuery(sqlStr);
		// System.out.println("totalRecords:" + res.getTotal());
		// List<String> colsNames = res.getColumnNames();

		// for (int i = 0; i < colsNames.size(); i++) {
		// System.out.print(colsNames.get(i));
		// System.out.print("\t");
		//
		// }

		// System.out.println();

		// while (res.next()) {
		//
		// for (int i = 0; i < colsNames.size(); i++) {
		// System.out.print(res.getString(colsNames.get(i)));
		// System.out.print("\t");
		// }
		// }

		con.close();
		return res;

		// long mil2 = System.currentTimeMillis();
		// System.out.println(mil2 - mil1);

	}
}
