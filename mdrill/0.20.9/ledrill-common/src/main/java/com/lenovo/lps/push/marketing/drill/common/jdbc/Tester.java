package com.lenovo.lps.push.marketing.drill.common.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.alimama.mdrill.jdbc.MdrillQueryResultSet;

public class Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			test();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void test() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub

		String connstr = "jdbc:mdrill://192.168.12.12:1107";

		Class.forName("com.alimama.mdrill.jdbc.MdrillDriver");

		Connection con = DriverManager.getConnection(connstr, "", "");

		Statement stmt = con.createStatement();

		long mil1 = System.currentTimeMillis();

		MdrillQueryResultSet res = null;

		res = (MdrillQueryResultSet) stmt

				.executeQuery("select thedate, content from lei_test where thedate ='20140318' limit 0,1000");

		System.out.println("totalRecords:" + res.getTotal());

		List<String> colsNames = res.getColumnNames();

		for (int i = 0; i < colsNames.size(); i++) {

			System.out.print(colsNames.get(i));

			System.out.print("\t");

		}

		System.out.println();

		while (res.next()) {

			for (int i = 0; i < colsNames.size(); i++) {

				System.out.print(res.getString(colsNames.get(i)));

				System.out.print("\t");

			}

		}

		con.close();

		long mil2 = System.currentTimeMillis();

		System.out.println(mil2 - mil1);

	}

}
