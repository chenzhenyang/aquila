package com.highgo.test.sqlserver.datatype;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 测试nchar,nvarchar,ntext的JDBC接口，
 * 专用方法setNString方法报错，到是setString方法可以
 * getNString也不行，getString可以
 * setCharacterStream && getCharacterStream okay
 * @author u
 *
 */
public class TestUnicodeString {

	public static void main(String[] args) throws UnsupportedEncodingException {
//		writeTest();
		readTest();
//		for(String str : Charset.availableCharsets().keySet()){
//			System.out.println(str +": "+Charset.availableCharsets().get(str));
//		}
//		System.out.println(Charset.defaultCharset());
		
		
		
	}
	
	public static void writeTest() throws UnsupportedEncodingException {
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("insert into UnicodeStringTest(field1,field2,field3) values(?,?,?)");
			for (int i = 0; i < 100; i++) {
//				ps.setNString(1, new String((i+"").getBytes("GBK"),"UTF-8"));    no
//				ps.setNString(2, new String((i+"").getBytes("GBK"),"UTF-8"));    no
//				ps.setNString(3, new String((i+"").getBytes("GBK"),"UTF-8"));    no
				
//				ps.setString(1, ""+i);   okay
//				ps.setString(2, ""+i);   okay
//				ps.setString(3, ""+i);   okay
				
				ps.setCharacterStream(1, new StringReader("sdfs"+i));
				ps.setCharacterStream(2, new StringReader("sdfs"+i));
				ps.setCharacterStream(3, new StringReader("sdfs"+i));
//				
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
			PreparedStatement ps = conn.prepareStatement("select * from  UnicodeStringTest");
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
//				String field3 = rs.getString(3);
//				System.out.println("record" + i++ + ":field1=" + field1 + " field2=" + field2+ " field3=" + field3);
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
