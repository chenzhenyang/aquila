package com.highgo.test.postgresql.datatype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.postgresql.util.PGmoney;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 鉴于此数据类型在pg中的特性，将SqlServer的money映射到pg的decimal和numeric是比较合理的（decimal和numeric是等价的）
 * 所以这个测试没有意义
 * @author u
 *
 */
public class TestMoney {
	
	public static void main(String[] args) {
		writeTest();
//		readTest();
	}
	
	public static void writeTest() {
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource("postgres");
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("insert into testmoney(field1) values(?)");
			for (int i = 0; i < 100; i++) {
				
				PGmoney pm = new PGmoney(10);
				System.out.println(pm.getType());
//				pm.setType(type);
				
//				ps.setString(1, "￥"+i);// no way
				ps.setObject(1, pm);
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
			PreparedStatement ps = conn.prepareStatement("select * from  testmoney");
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
				String field1 = rs.getString(1);
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
