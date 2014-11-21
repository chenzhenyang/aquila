package com.highgo.test.defaultt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 插入1亿条数据，所用时间为17481秒
 * @author u
 *
 */
public class TestRowNumber {
	public static void main(String[] args) {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			Date start =new  Date();
			Connection conn = cpds.getConnection();
			String sql = "select ROW_NUMBER() over(order by id) from measurement";
//			String sql = "select * from measurement_row_number";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
			ResultSet rs = ps.getResultSet();
			int i = 0;
			while(rs.next()){
				System.out.println(i++);
			}  
			Date end =new  Date();
			System.out.println((end.getTime()-start.getTime())/1000);//單位：秒
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}