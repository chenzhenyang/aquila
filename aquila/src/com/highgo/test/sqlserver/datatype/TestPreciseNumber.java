package com.highgo.test.sqlserver.datatype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * SqlServer精确数字数据类型测试，需找与JDBC接口支持的类型对应的数据类型
 * 
 * @author u
 *
 */
public class TestPreciseNumber {
	public static void main(String[] args) {
		// writeTest();
		readTest();
	}

	public static void writeTest() {
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("insert into PreciseNumberTest(field0,field1,field2,field3,field4,field5,field6,field7,field8) values(?,?,?,?,?,?,?,?,?)");
			for (int i = 0; i < 100; i++) {
				ps.setLong(1, i);// bigint=long
				ps.setDouble(2, i);// numberic=double
				ps.setByte(3, (byte) i);// bit = byte
				ps.setShort(4, (short) i);// smallint = short
				ps.setDouble(5, i);// decimal = double
				ps.setFloat(6, i);// smallmoney = float
				ps.setInt(7, i);// int = int
				ps.setShort(8, (short) i);// tinyint = short
				ps.setDouble(9, i);// money = double
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
			PreparedStatement ps = conn.prepareStatement("select * from  PreciseNumberTest");
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
//				long field0 = rs.getLong(1);
//				double field1 = rs.getDouble(2);
//				byte field2 = rs.getByte(3);
//				short field3 = rs.getShort(4);
//				double field4 = rs.getDouble(5);
//				float field5 = rs.getFloat(6);
//				int field6 = rs.getInt(7);
//				short field7 = rs.getShort(8);
//				double field8 = rs.getDouble(9);
//				System.out.println("record" + i++ + ":field0=" + field0 + ":field1=" + field1 + ":field2=" + field2
//						+ ":field3=" + field3 + ":field4=" + field4 + ":field5=" + field5 + ":field6=" + field6
//						+ ":field7=" + field7 + ":field8=" + field8);
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
