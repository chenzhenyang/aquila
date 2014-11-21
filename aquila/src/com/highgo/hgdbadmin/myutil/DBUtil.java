package com.highgo.hgdbadmin.myutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.highgo.hgdbadmin.vthread.Lock;
import com.highgo.hgdbadmin.vthread.Record;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBUtil {

	private static Logger logger = Logger.getLogger(DBUtil.class);
	public static Object lock = new Object();

	public static boolean testConnection(String driver, String url, String username, String password) {

		try {
			Class.forName(driver);
			DriverManager.getConnection(url, username, password);
		} catch (SQLException | ClassNotFoundException e) {
			logger.error(e.getMessage());
		}
		return true;
	}

	public static boolean insert(ComboPooledDataSource cpds, Record record) throws SQLException {
		Connection conn = cpds.getConnection();
		String sql = makeSql(Lock.SCHEMACURRENT, Lock.TABLECURRENT);
		PreparedStatement ps = conn.prepareStatement(sql);
		for (int i = 1; i <= Lock.fieldNum4SomeTable.get(); i++) {
			ps.setObject(i, record.fields.get(i - 1));
		}
		ps.execute();
		conn.close();
		return true;
	}

	public static boolean insert(Record record) throws SQLException {
		Connection conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
		String sql = makeSql(Lock.SCHEMACURRENT, Lock.TABLECURRENT);
		PreparedStatement ps = conn.prepareStatement(sql);
		for (int i = 1; i <= Lock.fieldNum4SomeTable.get(); i++) {
			ps.setObject(i, record.fields.get(i - 1));
		}
		ps.execute();
		conn.close();
		return true;
	}

	public static String makeSql(String schema, String table) {
		String sql = "insert into " + schema + "." + table + "(";
		for (int i = 0; i < Lock.fieldNum4SomeTable.get() - 1; i++) {
			sql += Lock.fieldsName.get(i) + ",";
		}
		sql += Lock.fieldsName.get(Lock.fieldsName.size() - 1);

		sql += ")" + " values(";
		for (int i = 0; i < Lock.fieldNum4SomeTable.get() - 1; i++) {
			if (Lock.fieldsType.get(i).equals("xml")) {
				sql += "XML(?),";
			} else {
				sql += "?,";
			}
		}
		if (Lock.fieldsType.get(Lock.fieldsType.size() - 1).equals("xml")) {
			sql += "XML(?))";
		} else {
			sql += "?)";
		}
		return sql;
	}

	public static Connection getNewConnection(String poolName) {
		Connection conn = null;
		try {
			conn = C3P0Util.getInstance().getConnection("postgres");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return null;
		}
		return conn;
	}
}
