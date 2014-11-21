package com.highgo.test.postgresql.datatype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author u
 *
 */
public class TestBoolean {
	
	public static void main(String[] args) {
//		writeTest();
		readTest();
	}
	
	public static void writeTest() {
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource("postgres");
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("insert into testboolean(field1) values(?)");
			for (int i = 0; i < 100; i++) {
				/**
				 *这个可以
				 */
//				ps.setBoolean(1, true);  //boolean = boolean
				
				ps.setObject(1, true);
				
//				ps.setObject(1, 0,	Types.BOOLEAN);
				
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
			ComboPooledDataSource cpds = new ComboPooledDataSource("postgres");
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from  testboolean");
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
				/**
				 * 这个可以
				 */
//				boolean field1 = rs.getBoolean(1);
				
//				Object field1 = rs.getObject(1);
				
				System.out.println(rs.getRow());
//				System.out.println("record" + i++ + ":field1=" + field1);
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
