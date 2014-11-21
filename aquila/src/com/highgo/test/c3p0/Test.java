package com.highgo.test.c3p0;

import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Test {
	public static void main(String[] args) throws SQLException, InterruptedException {
		final ComboPooledDataSource cpds = new ComboPooledDataSource("postgres");
		for (int i = 0; i < 3; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 1; i++) {
						try {
							cpds.getConnection();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep(100000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}, "Thread" + i).start();
		}
	}
}
