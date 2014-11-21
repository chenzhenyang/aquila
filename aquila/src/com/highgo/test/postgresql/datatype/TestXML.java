package com.highgo.test.postgresql.datatype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLXML;

import org.postgresql.jdbc4.Jdbc4SQLXML;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * xml文件的处理，用这个函数XMLPARSE
 * @author u
 *
 */
public class TestXML {

	public static void main(String[] args) {
//		readTest();
		writeTest();
	}

	public static void writeTest(){
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource("postgres");
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn.prepareStatement("insert into testxml(field1) values(XML(?))");
			for (int i = 0; i < 100; i++) {
//				SQLXML sqlxml = conn.createSQLXML();
//				sqlxml.setString("<t>m</t>");
//				ps.setSQLXML(1, sqlxml);  //xml ！= String  no okay
//				ps.setObject(1, sqlxml);
				ps.setString(1, "<t>m</t>");
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
			PreparedStatement ps = conn.prepareStatement("select * from  testxml");
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
//				String field1 = rs.getString(1);  //okay
//				SQLXML field1 = rs.getSQLXML(1);  //xml类型专门的接口，竟然不行
				
				/**
				 * 直接用getSQLXML不行，这样用可以，先getObject，然后转型
				 */
				Object field1 = rs.getObject(1);
				SQLXML sqlxml = (Jdbc4SQLXML)field1;
				String str = sqlxml.getString();
				System.out.println(field1.getClass().getName());
				
				System.out.println("record" + i++ + ":field1=" + str);
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
