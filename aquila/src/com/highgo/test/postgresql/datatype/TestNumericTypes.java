package com.highgo.test.postgresql.datatype;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * SqlServer精确数字数据类型测试，需找与JDBC接口支持的类型对应的数据类型
 * 
field1:int2:5
field2:int4:4
field3:int8:-5
field4:numeric:2
field5:numeric:2
field6:float4:7
field7:float8:8
field8:int4:4
field9:int8:-5
 * 
 * @author u
 *
 */
public class TestNumericTypes{
	public static void main(String[] args) {
		 writeTest();
//		readTest();
	}

	public static void writeTest() {
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource("postgres");
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("insert into testnumberictypes(field1,field2,field3,field4,field5,field6,field7,field8,field9) values(?,?,?,?,?,?,?,?,?)");
			conn.setAutoCommit(false);
			for (int i = 0; i < 1000; i++) {
				
				/**
				 * 这种方式肯定是okay的
				 */
//				ps.setShort(1, (short)i);// smallint =short
//				ps.setInt(2, i);// integer=int
//				ps.setLong(3, i);//  bigint = long
//				ps.setBigDecimal(4, new BigDecimal(i));// decimal = BigDecimal
//				ps.setBigDecimal(5, new BigDecimal(i));// numeric = BigDecimal
//				ps.setFloat(6, i);// real = float
//				ps.setDouble(7, i);// double precision = double
//				ps.setInt(8, i);// serial = int
//				ps.setLong(9, i);// bigserial = long
				
				
				/**
				 * 这种方式是可以的
				 */
//				ps.setObject(1, i);
//				ps.setObject(2, i);
//				ps.setObject(3, i);
//				ps.setObject(4, i);
//				ps.setObject(5, i);
//				ps.setObject(6, i);
//				ps.setObject(7, i);
//				ps.setObject(8, i);
//				ps.setObject(9, i);
				
				/**
				 * 这种方式也可以
				 */
				ps.setObject(1, i,Types.SMALLINT);
				ps.setObject(2, i,Types.INTEGER);
				ps.setObject(3, i,Types.BIGINT);
				ps.setObject(4, i,Types.DECIMAL);
				ps.setObject(5, i,Types.NUMERIC);
				ps.setObject(6, i,Types.REAL);
				ps.setObject(7, i,Types.DOUBLE);
				ps.setObject(8, i,Types.INTEGER);
				ps.setObject(9, i,Types.BIGINT);
				
				ps.addBatch();
				if(i%100==99){
					ps.executeBatch();
					conn.commit();
				}
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
			PreparedStatement ps = conn.prepareStatement("select * from  testnumberictypes");
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
				/**
				 * 这种方式肯定可以
				 */
//				short field1 = rs.getShort(1);
//				int field2 = rs.getInt(2);
//				long field3 = rs.getLong(3);
//				BigDecimal field4 = rs.getBigDecimal(4);
//				BigDecimal field5 = rs.getBigDecimal(5);
//				float field6 = rs.getFloat(6);
//				double field7 = rs.getDouble(7);
//				int field8 = rs.getInt(8);
//				long field9 = rs.getLong(9);
				
				/**
				 * 这种方式是可以的，反射出来的fieldx的类型也是可以接受的
				 */
				Object field1 = rs.getObject(1);
				Object field2 = rs.getObject(2);
				Object field3 = rs.getObject(3);
				Object field4 = rs.getObject(4);
				Object field5 = rs.getObject(5);
				Object field6 = rs.getObject(6);
				Object field7 = rs.getObject(7);
				Object field8 = rs.getObject(8);
				Object field9 = rs.getObject(9);
				
				
				/**
				 * 这种方式会报错
				 */
//				Integer field1 = rs.getObject(1,Integer.class);
				
				
				
				
				System.out.println(field1.getClass().getName());
				System.out.println(field2.getClass().getName());
				System.out.println(field3.getClass().getName());
				System.out.println(field4.getClass().getName());
				System.out.println(field5.getClass().getName());
				System.out.println(field6.getClass().getName());
				System.out.println(field7.getClass().getName());
				System.out.println(field8.getClass().getName());
				System.out.println(field9.getClass().getName());
				
				
				System.out.println("record" + i++ +":field1=" + field1 + ":field2=" + field2
						+ ":field3=" + field3 + ":field4=" + field4 + ":field5=" + field5 + ":field6=" + field6
						+ ":field7=" + field7 + ":field8=" + field8+ ":field9=" + field9);
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
