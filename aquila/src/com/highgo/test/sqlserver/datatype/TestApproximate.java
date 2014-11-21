package com.highgo.test.sqlserver.datatype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TestApproximate {
	
	public static void main(String[] args) {
//		writeTest();
		readTest();
	}
	
	public static void writeTest() {
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("insert into ApproximateTest(field1,field2) values(?,?)");
			for (int i = 0; i < 100; i++) {
//				ps.setDouble(1, i);// float = double   okay
//				ps.setFloat(2, i);// real = float	 	okay
				ps.setObject(1, i, Types.DOUBLE);
				ps.setObject(2, i, Types.FLOAT);
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
			PreparedStatement ps = conn.prepareStatement("select * from  ApproximateTest");
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
//				double field1 = rs.getDouble(1);   //okay
//				float field2 = rs.getFloat(2); 		//okay
				
//				Object field1 = rs.getObject(1);	//okay
//				Object field2 = rs.getObject(2);	//okay
				
				Double field1 = rs.getObject(1, Double.class);
				Float field2 = rs.getObject(1, Float.class);
				
//				System.out.println(field1.getClass().getName());
//				System.out.println(field2.getClass().getName());
				
				System.out.println("record" + i++ + ":field1=" + field1 + ":field2=" + field2);
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
