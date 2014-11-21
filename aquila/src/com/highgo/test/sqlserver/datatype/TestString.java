package com.highgo.test.sqlserver.datatype;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * char varchar text 用getString和getCharacterStream都可以
 * getNString和getNCharacterStream不行
 * 
 * @author u
 */
public class TestString {

	public static void main(String[] args) {
		// writeTest();
		readTest();
	}

	public static void writeTest() {
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn.prepareStatement("insert into StringTest(field1,field2,field3) values(?,?,?)");
			for (int i = 0; i < 100; i++) {
				// ps.setString(1, ""+i); //char = string okay
				// ps.setString(2, ""+i); //varchar =string okay
				// ps.setString(3, ""+i); //text = string okay

				ps.setCharacterStream(1, new StringReader("" + i)); // okay
				ps.setCharacterStream(2, new StringReader("" + i)); // okay
				ps.setCharacterStream(3, new StringReader("" + i)); // okay
				ps.execute();
			}
			ps.close();
			conn.close();
			cpds.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void readTest() {
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from  StringTest");
			ResultSet rs = ps.executeQuery();
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			for(int m = 1 ;m <= columnCount; m++){
				String columnName = rsmd.getColumnName(m);
				String columntype = rsmd.getColumnTypeName(m);
				int columntypen = rsmd.getColumnType(m);
				System.out.println(columnName + ":" +columntype+":"+columntypen);
			}
			
			
//			int i = 0;
//			while (rs.next()) {
//				String field1 = rs.getString(1);
//				String field2 = rs.getString(2);
//				String field3 = rs.getString(3);
//				System.out.println("record" + i++ + ":field1=" + field1 + " field2=" + field2 + " field3=" + field3);
//			}
			rs.close();
			ps.close();
			conn.close();
			cpds.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
