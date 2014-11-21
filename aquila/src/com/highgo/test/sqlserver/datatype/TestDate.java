package com.highgo.test.sqlserver.datatype;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

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
		 writeTest();
//		readTest();
	}

	public static void writeTest() {
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			Connection conn = cpds.getConnection();
			// String sql =
			// "insert into DateTest(field1,field2,field3,field4,field5,field6) values(?,?,?,?,?,?)";
			String sql = "insert into TransferDatetimeTest(field1,field2,field3,field4,field5) values(?,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			for (int i = 0; i < 10; i++) {
				ps.setDate(1, new Date(i)); // date = date
				ps.setTimestamp(2, new Timestamp(i)); // datetimeoffset =
														// timestamp
				ps.setTimestamp(3, new Timestamp(i)); // datetime2 = timestamp
				ps.setTimestamp(4, new Timestamp(i)); // smalltime = timestamp
				ps.setTime(5, new Time(i)); // time = time
//				ps.setTimestamp(6, new Timestamp(i)); // datetime = timestamp
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
			PreparedStatement ps = conn.prepareStatement("select * from  DateTest");
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

			// while (rs.next()) {
			// Date field1 = rs.getDate(1);
			// Timestamp field2 = rs.getTimestamp(2);
			// Timestamp field3 = rs.getTimestamp(3);
			// Timestamp field4 = rs.getTimestamp(4);
			// Timestamp field5 = rs.getTimestamp(5);
			// Time field6 = rs.getTime(6);
			//
			// System.out.println("record" + i++ + ":field1=" + field1 +
			// ":field2=" + field2 + ":field3=" + field3
			// + ":field4=" + field4 + ":field5=" + field5 + ":field6=" +
			// field6);
			// }
			rs.close();
			ps.close();
			conn.close();
			cpds.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
