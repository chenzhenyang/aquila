package com.highgo.test.sqlserver.datatype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * geography和geometry 类型都可以用getString来读取
 * 
 * @author u
 *
 */
public class TestOther4 {

	public static void main(String[] args) {
		readTest();
//		writeTest();
	}
	
	public static void readTest() {
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from  OtherTest4");
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
//				System.out.println("record" + i++ + ":field1=" + field1 + ":field1=" + field2);
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
