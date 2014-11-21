package com.highgo.test.sqlserver.datatype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * @author u
 *
 */
public class TestOther2 {

	public static void main(String[] args) {
		readTest();
//		writeTest();
	}

	public static void writeTest(){
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn.prepareStatement("insert into OtherTest2 values(?,NEWID())");
			for (int i = 0; i < 100; i++) {
				ps.setString(1, "/"); //hierarchyid = string
										//uniqueidentifier =String  插入时貌似只能用NEWID函数生成16位GUID
				
				
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
			PreparedStatement ps = conn.prepareStatement("select * from  OtherTest2");
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
//				System.out.println("record" + i++ + ":field1=" + field1+ ":field2=" + field2);
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
