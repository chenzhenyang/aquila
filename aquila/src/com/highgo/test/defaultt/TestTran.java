package com.highgo.test.defaultt;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TestTran {
	public static void main(String[] args) {
		writeTest();
	}

	public static void writeTest() {
		int[] state = new int[5];
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			Connection conn = cpds.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("insert into TestTran(field1) values(?)");
			for (int i = 0; i < 11; i++) {
				ps.setInt(1, i);
				if (i == 4) {
					ps.setString(1, "str");
//					ps.setInt(1, i);
				}
				ps.addBatch();
				if (i == 5) {
					state = ps.executeBatch();
					conn.commit();
					for (int m : state) {
						System.out.println("state:" + m);
					}
				}
			}
			ps.close();
			conn.close();
			cpds.close();
		} catch (SQLException e) {
//			for (int m : state) {
//				System.out.println("state:" + m);
//			}
			if (e instanceof BatchUpdateException){
				int[] us = ((BatchUpdateException)e).getUpdateCounts();
				for(int t : us){
					System.out.println("t:"+t);
				}
			}
			e.printStackTrace();
		}
	}
}
