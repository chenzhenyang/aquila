package com.highgo.test.postgresql.datatype;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * JDBC时间相关的setter方法有3个setDate,setTime,setTimestamp,数据库中的类型有多余3个，
 * 肯定有不同的数据类型用同一个JDBC接口的情况发生
 * 
 * @author u
 *
 */
public class TestDate {
	public static void main(String[] args) {
//		 writeTest();
		readTest();
	}

	public static void writeTest() {
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource("postgres");
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("insert into testdatatime2(field1,field2,field3) values(?,?,?)");
			for (int i = 0; i < 100; i++) {
				/**
				 * 这种可以
				 */
//				ps.setTimestamp(1, new Timestamp(i)); // date = date
//				ps.setDate(2, new Date(i)); // datetimeoffset = timestamp
//				ps.setTime(3, new Time(i)); // time = time
				
				/**
				 * 这种可以
				 */
//				ps.setObject(1, new Timestamp(i));
//				ps.setObject(2, new Date(i));
//				ps.setObject(3, new Time(i));
				
				 
				ps.setObject(1, new Timestamp(i), Types.TIMESTAMP);
				ps.setObject(2, new Date(i), Types.DATE);
				ps.setObject(3, new Time(i), Types.TIME);
				
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
			PreparedStatement ps = conn.prepareStatement("select * from  testdatatime2");
			ResultSet rs = ps.executeQuery();
			int i = 0;

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			for (int m = 1; m <= columnCount; m++) {
				String columnName = rsmd.getColumnName(m);
				String columntype = rsmd.getColumnTypeName(m);
				int columntypen = rsmd.getColumnType(m);
				System.out.println(columnName + ":" + columntype + ":" + columntypen);
			}

			while (rs.next()) {
				/**
				 * 這種可以
				 */
//				Timestamp field1 = rs.getTimestamp(1);
//				Date field2 = rs.getDate(2);
//				Time field3 = rs.getTime(3);
				
				/**
				 * 这种可以
				 */
				Object field1 = rs.getObject(1);
				Object field2 = rs.getObject(2);
				Object field3 = rs.getObject(3);
				
				System.out.println(field1.getClass().getName());
				System.out.println(field2.getClass().getName());
				System.out.println(field3.getClass().getName());
				
				System.out.println("record" + i++ + ":field1=" + field1 + ":field2=" + field2 + ":field3=" + field3);
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
