package com.highgo.test.sqlserver.datatype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLXML;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * SQLXML这个类型怎么回事，竟然读不出XML类型的字段，
 * SqlServer中的XML类型用getString去取
 * @author u
 *
 */
public class TestOtherXML {

	public static void main(String[] args) {
		readTest();
//		writeTest();
	}

	public static void writeTest(){
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn.prepareStatement("insert into OtherTest1(field1) values(?)");
			for (int i = 0; i < 100; i++) {
				SQLXML sqlxml = conn.createSQLXML();
				sqlxml.setString("<t>m</t>");
				ps.setSQLXML(1, sqlxml );
//				ps.setString(1, "<test>"+i+"</test>");  //xml = String
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
			PreparedStatement ps = conn.prepareStatement("select * from  OtherTest1");
			ResultSet rs = ps.executeQuery();
			
			
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			for(int m = 1 ;m <= columnCount; m++){
				String columnName = rsmd.getColumnName(m);
				String columntype = rsmd.getColumnTypeName(m);
				int columntypen = rsmd.getColumnType(m);
				System.out.println(columnName + ":" +columntype+":"+columntypen);
			}
			
			int i = 0;
			while (rs.next()) {
//				String field1 = rs.getString(1);  //okay
//				SQLXML field1 = rs.getSQLXML(1);  //xml类型专门的接口，竟然不行
				
				
				Object field1 = rs.getObject(1);
				System.out.println(field1.getClass().getName());
				
				
				System.out.println("record" + i++ + ":field1=" + field1);
			}
			rs.close();
			ps.close();
			conn.close();
			cpds.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
