package com.highgo.test.defaultt;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class SuperTest {
	public static void main(String[] args) throws IOException {
		long start = System.nanoTime();
		writeTest();
		long end = System.nanoTime();
		System.out.println(Thread.currentThread().getName() + ":" + (end - start));
	}

	public static void writeTest() throws IOException {
		StringBuilder sb = new StringBuilder();
		while (sb.length() < 5000) {
			sb.append("hello world!");
		}
		String xml = fileReader();
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("insert into supertest(field1,field2,field3,field4,field5,field7,field8,filed9,field10,field11,field12,field13,field15,field16) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			for (int i = 0; i < 1000; i++) {
				ps.setLong(1, i);
				InputStream is1 = new FileInputStream(new File("c3p0-config.xml"));
				ps.setBinaryStream(2, is1);
				ps.setDate(3, new Date(i));
				ps.setTimestamp(4, new Timestamp(i));
				ps.setBigDecimal(5, new BigDecimal(i));
				ps.setFloat(6, i);
				InputStream is2 = new FileInputStream(new File("field2"));
				ps.setBinaryStream(7, is2);
				ps.setInt(8, i);
				ps.setBigDecimal(9, new BigDecimal(i));
				ps.setFloat(10, i);
				ps.setString(11, sb.toString());
				ps.setTime(12, new Time(i));
				// ps.setTimestamp(13, new Timestamp(i));
				InputStream is3 = new FileInputStream(new File("c3p0-config.xml"));
				ps.setBinaryStream(13, is3);
				ps.setString(14, xml);

				if (i % 9999 == 0) {
					System.out.println(i);
				}

				ps.execute();
			}
			ps.close();
			conn.close();
			cpds.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String fileReader() throws IOException {
		FileReader fr = new FileReader("c3p0-config.xml");
		char[] bytes = new char[1];
		int m = 0;
		StringBuilder sb = new StringBuilder();
		while ((m = fr.read(bytes)) != -1) {
			sb.append(new String(bytes));
		}
		return sb.toString();
	}
}
