package com.highgo.test.sqlserver.datatype;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * binary/varbinary/image  都是二进制类型，setter和getter用的一样
 * 从测试来看
 * ps.setBinaryStream(parameterIndex, x);ps.setBytes(parameterIndex, x);
 * 这两个是可以的 
 * @author u
 *
 */
public class TestBinaryString {
	
	public static void main(String[] args) throws UnsupportedEncodingException {
//		writeTest();
		readTest();
	}
	
	public static void writeTest() throws UnsupportedEncodingException {
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("insert into BinaryStringTest(field1,field2,field3) values(?,?,?)");
			for (int i = 0; i < 100; i++) {
//				ps.setBinaryStream(parameterIndex, x);   okay
//				ps.setBytes(parameterIndex, x);        okay 
//				ps.setCharacterStream(parameterIndex, reader);    no  这是用来写nvarchar的，或许字符串都可以
//				ps.setNCharacterStream(parameterIndex, value);	  no  这是用来写nvarchar的，或许字符串都可以
				
				
//				ps.setBytes(1, (i+"").getBytes());  okay
//				ps.setBytes(2, (i+"").getBytes());  okay
//				ps.setBytes(3, (i+"").getBytes());  okay
				
				ps.setBinaryStream(1, new ByteArrayInputStream((i+"").getBytes()) );
				ps.setBinaryStream(2, new ByteArrayInputStream((i+"").getBytes()) );
				ps.setBinaryStream(3, new ByteArrayInputStream((i+"").getBytes()) );
				
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
			PreparedStatement ps = conn.prepareStatement("select * from  BinaryStringTest");
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
//				String field1 = new String(rs.getBytes(1));
//				String field2 = new String(rs.getBytes(2));
//				String field3 = new String(rs.getBytes(3));
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
