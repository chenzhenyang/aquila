package com.highgo.hgdbadmin.transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.highgo.hgdbadmin.model.Column;
import com.highgo.hgdbadmin.model.Table;
import com.highgo.hgdbadmin.myutil.C3P0Util;
import com.highgo.hgdbadmin.myutil.PostgreSQLObjectInfo;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;
import com.highgo.hgdbadmin.reportable.Constant;
import com.highgo.hgdbadmin.reportable.ObjectInfo;
import com.highgo.hgdbadmin.reportable.ObjectTable;
import com.highgo.hgdbadmin.reportable.ReportInstance;
import com.highgo.hgdbadmin.reportable.TableDetail;

public class TableTransfer {
	private static Logger logger = Logger.getLogger(TableTransfer.class);

	/**
	 * 普通表和带主键的表都可以了
	 * 
	 * @param source
	 * @param dest
	 * @param schema
	 * @param table
	 * @return
	 * @throws SQLException
	 * 
	 *             public static boolean createTable(String schema, String
	 *             table) { logger.info("createTable"); ObjectTable tableTable =
	 *             ReportInstance.getObjectTable(Constant.SCHEMA);
	 * 
	 *             ObjectInfo objectInfo = new ObjectInfo();
	 *             objectInfo.objectName = schema+"."+table;
	 *             objectInfo.isSuccessed = true;
	 * 
	 *             TableDetail td = new TableDetail();
	 * 
	 *             Table t = new Table(schema, table, "BASE TABLE"); Connection
	 *             conn = null; Statement st = null; try { t.columns =
	 *             fetchColumnsForATable(schema, table); t.keys =
	 *             fetchPrimaryKeysForATable(schema, table); String sql =
	 *             t.toSql();
	 * 
	 *             td.source = t; objectInfo.sqlGenerated = sql;
	 * 
	 *             logger.info(sql); conn =
	 *             C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES); st =
	 *             conn.createStatement(); st.execute(sql);
	 * 
	 *             tableTable.numSuccessed++; tableTable.sum++;
	 *             logger.info("create table["
	 *             +schema+"."+table+"] create successfully."); } catch
	 *             (SQLException e) { logger.error(e.getMessage());
	 *             ShellEnvironment.println(e.getMessage());
	 * 
	 * 
	 *             tableTable.numFailed++; tableTable.sum++;
	 *             objectInfo.isSuccessed = false;
	 *             objectInfo.causes.add(e.getMessage());
	 * 
	 * 
	 *             return false; } finally { C3P0Util.getInstance().close(st);
	 *             C3P0Util.getInstance().close(conn);
	 * 
	 *             objectInfo.sourceDifinition = ""; td.baseInfo = objectInfo;
	 *             tableTable.rows.add(objectInfo); } return true; }
	 */

	public static boolean createTable(String schema, String table) {
		Table t = new Table(schema, table, "BASE TABLE");
		t.columns = fetchColumnsForATable(schema, table);
		t.keys = fetchPrimaryKeysForATable(schema, table);
		return createTable(t);
	}

	public static boolean createTable(Table table) {
		String sql = table.toSql();

		ObjectTable tableTable = ReportInstance.getObjectTable(Constant.TABLE);
		

		ObjectInfo objectInfo = new ObjectInfo();
		objectInfo.objectName = table.schema + "." + table.name;
		objectInfo.isSuccessed = true;

		TableDetail td = new TableDetail();
		td.source = table;

		logger.info(sql);
		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn.createStatement();

			objectInfo.sqlGenerated = sql;
			logger.info(sql);
			tableTable.sum++;

			st.execute(sql);

			tableTable.numSuccessed++;
			logger.info("create table[" + table.schema + "." + table.name + "] create successfully.");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());

			tableTable.numFailed++;
			objectInfo.causes.add(e.getMessage());
			objectInfo.isSuccessed = false;

			return false;
		} finally {
			C3P0Util.getInstance().close(st);
			C3P0Util.getInstance().close(conn);

			objectInfo.sourceDifinition = "";
			td.baseInfo = objectInfo;
			tableTable.rows.add(objectInfo);
			td.dest = PostgreSQLObjectInfo.fetchATableFromSqlServer(table.schema, table.name);
			ReportInstance.report.tables.add(td);
		}
		return true;
	}

	public static boolean deleteAllData(String schema, String table) {
		String sql = "delete from " + schema + "." + table;
		logger.info(sql);
		Connection conn = null;
		Statement st = null;

		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn.createStatement();
			st.execute(sql);
			st.close();
			conn.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
		} finally {
			C3P0Util.getInstance().close(st);
			C3P0Util.getInstance().close(conn);
		}

		return true;
	}

	/**
	 * fetch table information from sqlserver except view information
	 * 获取此Connection代表的数据库下的表 这个方法的主要作用是看看服务器中有没有这个table
	 * 
	 * @param source
	 * @return
	 * @throws SQLException
	 */
	public static Table fetchATableFromSqlServer(String schema, String table) {
		logger.info("fetchATableFromSqlServer");
		Table tab = null;
		String sql = "SELECT TABLE_SCHEMA, TABLE_NAME,TABLE_TYPE FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='BASE TABLE' AND TABLE_SCHEMA=? AND TABLE_NAME=?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			ps.setString(1, schema);
			ps.setString(2, table);
			logger.info(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				tab = new Table(rs.getString("TABLE_SCHEMA"), rs.getString("TABLE_NAME"), rs.getString("TABLE_TYPE"));
			}
			tab.columns = fetchColumnsForATable(schema, table);
			tab.keys = fetchPrimaryKeysForATable(schema, table);
			logger.info("TABLE:" + tab);
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		return tab;
	}

	/**
	 * fetch table information from sqlserver except view information
	 * 获取此Connection代表的数据库下的表
	 * 
	 * @param source
	 * @return
	 * @throws SQLException
	 */
	public static List<Table> fetchTableFromSqlServer() {
		logger.info("fetchTableFromSqlServer");
		List<Table> list = new LinkedList<>();
		String sql = "SELECT TABLE_SCHEMA, TABLE_NAME,TABLE_TYPE FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='BASE TABLE'";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Table tmp = new Table(rs.getString("TABLE_SCHEMA"), rs.getString("TABLE_NAME"),
						rs.getString("TABLE_TYPE"));
				tmp.columns = fetchColumnsForATable(tmp.schema, tmp.name);
				list.add(tmp);
			}
			logger.info("TABLES:" + list);
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		return list;
	}

	/**
	 * fetch some table's columns information,for create table at dest database;
	 * 
	 * @param source
	 * @param schema
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	private static List<Column> fetchColumnsForATable(String schema, String table) {
		logger.info("fetchColumnsForATable");
		List<Column> list = new LinkedList<>();
		String sql = "SELECT COLUMN_NAME,COLUMN_DEFAULT,DATA_TYPE,IS_NULLABLE,CHARACTER_MAXIMUM_LENGTH,NUMERIC_PRECISION,NUMERIC_SCALE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? and TABLE_NAME = ? ORDER BY ORDINAL_POSITION";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			ps.setString(1, schema);
			ps.setString(2, table);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Column(rs.getString("COLUMN_NAME"), rs.getString("COLUMN_DEFAULT"), rs
						.getBoolean("IS_NULLABLE"), rs.getString("DATA_TYPE"), rs.getInt("CHARACTER_MAXIMUM_LENGTH"),
						rs.getInt("NUMERIC_PRECISION"), rs.getInt("NUMERIC_SCALE")));
			}
			logger.info("COLUMNS FOR [" + schema.toUpperCase() + "." + table.toUpperCase() + "]:" + list);
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}

		return list;
	}

	private static List<String> fetchPrimaryKeysForATable(String schema, String table) {
		logger.info("fetchPrimaryKeysForATable");
		List<String> list = new LinkedList<>();
		String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_SCHEMA=? and TABLE_NAME = ?";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			ps.setString(1, schema);
			ps.setString(2, table);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("COLUMN_NAME"));
			}
			logger.info("PRIMARY KEY FOR [" + schema.toUpperCase() + "." + table.toUpperCase() + "]:" + list);
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		return list;
	}
}
