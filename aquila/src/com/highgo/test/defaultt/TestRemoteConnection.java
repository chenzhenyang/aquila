package com.highgo.test.defaultt;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TestRemoteConnection {
	public static void main(String[] args) {
		ComboPooledDataSource cpds = new ComboPooledDataSource("postgres");
		Connection conn = null;
		try {
			conn = cpds.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
