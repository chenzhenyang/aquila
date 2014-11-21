package com.highgo.hgdbadmin.myutil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0Util {
	private static Logger logger = Logger.getLogger(C3P0Util.class);
	public static String SOURCE = "source";
	public static String POSTGRES = "postgres";

	private ComboPooledDataSource source = null;
	private ComboPooledDataSource postgres = null;

	private static C3P0Util instance = null;

	private C3P0Util() {
		source = new ComboPooledDataSource("source");
		postgres = new ComboPooledDataSource("postgres");
	}

	public static final synchronized C3P0Util getInstance() {
		if (instance == null) {
			instance = new C3P0Util();
		}
		return instance;
	}

	public synchronized Connection getConnection(String dataSource) throws SQLException {
		Connection conn = null;
		if ("source".equals(dataSource)) {
			conn = source.getConnection();
		} else if ("postgres".equals(dataSource)) {
			conn = postgres.getConnection();
		}
		logger.info("application get a new connection.");
		return conn;
	}

	public synchronized void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			conn= null;
		}
	}

	public synchronized void close(Statement stat) {
		try {
			if (stat != null) {
				stat.close();
				stat = null;
			}
		} catch (SQLException e) {
			stat = null;
		}
	}

	public synchronized void close(ResultSet rest) {
		try {
			if (rest != null) {
				rest.close();
				rest = null;
			}
		} catch (SQLException e) {
			rest = null;
		}
	}

	public static void main(String[] args) {
		new Thread(new TestThread(), "test").start();
	}

	private static class TestThread implements Runnable {

		private String dataSource = "source";

		@Override
		public void run() {
			while (true) {
				try {
					Connection conn = C3P0Util.getInstance().getConnection(dataSource);
					System.out.println("hello,this is " + conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if ("source".equals(dataSource)) {
					dataSource = "postgres";
				} else {
					dataSource = "source";
				}
			}

		}

	}
}
