package com.highgo.test.postgresql.datatype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Pg中的这个数据类型相当于SqlServer中的uniqueidentifier类型
 * @author u
 *
 */
public class TestUUID {
	
	public static void main(String[] args) {
//		writeTest();
		readTest();
	}
	
	public static void writeTest() {
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource("postgres");
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("insert into testuuid(field1) values(?)");
			for (int i = 0; i < 1; i++) {
				ps.setBytes(1, "A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11".getBytes());
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
			PreparedStatement ps = conn.prepareStatement("select * from  testuuid");
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
//				String field1 = rs.getString(1);  okay
				Object field1 = rs.getObject(1);
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
