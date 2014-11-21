package com.highgo.test.pgcopy;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

/**
 * jdbc:148s
 * 
 * @author u
 *
 */
public class TestPgCopyManager {
	public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
		// ComboPooledDataSource cpds = new ComboPooledDataSource("postgres");
		// Connection conn = cpds.getConnection();

//		 testGenerateFile();
		// testCopy();
		testStringWriter();
	}

	public static void testCopy() throws ClassNotFoundException, SQLException, IOException {
		Long start = System.nanoTime();
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection("jdbc:postgresql://192.168.100.8:5866/sqoop", "hive", "highgo");
		CopyManager cm = new CopyManager((BaseConnection) conn);
		Reader reader = new FileReader(new File("result"));
		cm.copyIn("copy testnumberictypes from STDIN", reader);
		Long end = System.nanoTime();
		System.out.println((end - start) / Math.pow(10, 9));
	}

	public static void testGenerateFile() throws IOException {
		Long start = System.nanoTime();
		File file = new File("result2");
		Writer writer = new FileWriter(file);
		Long fileopen = System.nanoTime();
		System.out.println("file open:" + (fileopen - start) / Math.pow(10, 9));
		int i = 0;
		while (file.length() < 5312258) {
			i++;
			writer.write("hello");
		}
		System.out.println(i);
		Long fileclose1 = System.nanoTime();
		writer.close();
		Long fileclose2 = System.nanoTime();
		System.out.println("file close:" + (fileclose2 - fileclose1) / Math.pow(10, 9));
		Long end = System.nanoTime();
		System.out.println((end - start) / Math.pow(10, 9)+3.5);
	}

	public static void testStringWriter() {
		Long start = System.nanoTime();
		StringWriter sw = new StringWriter();
		int i = 0;
		while (i < 1063322) {
			i++;
			sw.write("hello");
		}
		StringBuffer sb = sw.getBuffer();
		System.out.println(sb.toString());
		Long end = System.nanoTime();
		System.out.println((end - start) / Math.pow(10, 9));
	}
}
