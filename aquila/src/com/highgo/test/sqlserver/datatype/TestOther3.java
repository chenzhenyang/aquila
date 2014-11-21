package com.highgo.test.sqlserver.datatype;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * sql_variant类型 jdbc不支持
 * 
 * @author u
 *
 */
public class TestOther3 {

	public static void main(String[] args) throws IOException {
		readTest();
		// writeTest();
	}

	public static void readTest() throws IOException {
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from  OtherTest3");
			ResultSet rs = ps.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			for (int m = 1; m <= columnCount; m++) {
				String columnName = rsmd.getColumnName(m);
				String columntype = rsmd.getColumnTypeName(m);
				int columntypen = rsmd.getColumnType(m);
				System.out.println(columnName + ":" + columntype + ":" + columntypen);
			}

			int i = 0;
			while (rs.next()) {
				InputStream field1 = rs.getBinaryStream(1);
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
