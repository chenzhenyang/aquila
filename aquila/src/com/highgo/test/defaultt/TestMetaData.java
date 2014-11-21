package com.highgo.test.defaultt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.mchange.v2.c3p0.ComboPooledDataSource;


public class TestMetaData {
	public static void main(String[] args) {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			Date start = new Date();
			Connection conn = cpds.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("select * from measurement where 1=0");
//			ParameterMetaData pmd = ps.getParameterMetaData();
//			int pc = pmd.getParameterCount();
//			System.out.println(pc);
//			ps.getFetchDirection();
			ps.executeQuery();
//			ResultSet rs = ps.getGeneratedKeys();
//			ps.getMaxFieldSize();
//			ResultSetMetaData rsm = ps.getMetaData();
			System.out.println(ps.getMaxRows());
			
			Date end = new Date();
			System.out.println((end.getTime() - start.getTime()) / 1000);// †ÎÎ»£ºÃë
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
