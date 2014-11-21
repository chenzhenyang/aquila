package com.highgo.test.defaultt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 插入1亿条数据，所用时间为17481秒
 * @author u
 *
 */
public class TestBatch {
	public static void main(String[] args) {
		ComboPooledDataSource cpds = new ComboPooledDataSource("postgres");
		try {
			Date start =new  Date();
			Connection conn = cpds.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("insert into measurement(field1,field2,field3,field4,field5,field6,field7) values(?,?,?,?,?,?,?)");
			int m=1;
			for (int i = 0; i < 100; i++) {
				ps.setString(1, "field1-"+i);
				ps.setString(2, "field2-"+i);
				ps.setString(3, "field3-"+i);
				ps.setString(4, "field4-"+i);
				ps.setString(5, "field5-"+i);
				ps.setString(6, "field6-"+i);
				ps.setString(7, "field7-"+i);
				ps.addBatch();
				m++;
				if(m%10==0){
					m=1;
					ps.executeBatch();
				}
			}
			conn.commit();
			Date end =new  Date();
			System.out.println((end.getTime()-start.getTime())/1000);//單位：秒
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}