package com.highgo.hgdbadmin.transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.highgo.hgdbadmin.model.ConstraintUK;
import com.highgo.hgdbadmin.myutil.C3P0Util;
import com.highgo.hgdbadmin.myutil.PostgreSQLObjectInfo;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;
import com.highgo.hgdbadmin.reportable.Constant;
import com.highgo.hgdbadmin.reportable.ObjectInfo;
import com.highgo.hgdbadmin.reportable.ObjectTable;
import com.highgo.hgdbadmin.reportable.ReportInstance;
import com.highgo.hgdbadmin.reportable.UniqueKeyDetail;

public class ConstraintUKTransfer {

	private static Logger logger = Logger.getLogger(ConstraintUKTransfer.class);

	/**
	 * 
	 * @param constrName
	 * @return
	 * 
	 *         public static boolean createConstraintUK(String constrName) {
	 *         logger.info("createConstraintUK");
	 * 
	 *         ObjectInfo objectInfo = new ObjectInfo(); objectInfo.objectName =
	 *         constrName; objectInfo.isSuccessed = true;
	 * 
	 *         Connection conn = null; Statement st = null; try { ConstraintUK
	 *         cu = fetchConstraintUKFromSqlServer(constrName); conn =
	 *         C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES); st =
	 *         conn.createStatement(); String sql = cu.toSql();
	 * 
	 *         logger.info(sql); objectInfo.sqlGenerated = sql;
	 * 
	 *         st.execute(sql); logger.info("create unique constraint " +
	 *         constrName.toUpperCase() + " successfully"); } catch
	 *         (SQLException e) { logger.error(e.getMessage());
	 *         ShellEnvironment.println(e.getMessage());
	 * 
	 *         objectInfo.isSuccessed = false;
	 *         objectInfo.causes.add(e.getMessage());
	 * 
	 *         return false; } finally { C3P0Util.getInstance().close(st);
	 *         C3P0Util.getInstance().close(conn);
	 * 
	 *         objectInfo.sourceDifinition = "";
	 *         ReportInstance.getObjectTable(Constant
	 *         .CHECK_CONSTRAINT).rows.add(objectInfo); } return true; }
	 */

	public static boolean createConstraintUK(String constrName) {
		ConstraintUK cu = fetchConstraintUKFromSqlServer(constrName);
		return createConstraintUK(cu);
	}

	public static boolean createConstraintUK(ConstraintUK cu) {
		logger.info("createConstraintUK");

		ObjectTable ukTable = ReportInstance.getObjectTable(Constant.UNIQUE_KEY);

		ObjectInfo objectInfo = new ObjectInfo();
		objectInfo.objectName = cu.cName;
		objectInfo.isSuccessed = true;

		UniqueKeyDetail ukd = new UniqueKeyDetail();

		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn.createStatement();
			String sql = cu.toSql();

			ukTable.sum++;
			logger.info(sql);
			objectInfo.sqlGenerated = sql;

			st.execute(sql);
			ukTable.numSuccessed++;
			logger.info("create unique constraint " + cu.cName.toUpperCase() + " successfully");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());

			ukTable.numFailed++;
			objectInfo.isSuccessed = false;
			objectInfo.causes.add(e.getMessage());

			return false;
		} finally {
			C3P0Util.getInstance().close(st);
			C3P0Util.getInstance().close(conn);

			objectInfo.sourceDifinition = "";
			ukTable.rows.add(objectInfo);
			ukd.baseInfo = objectInfo;
			ukd.source = cu;
			ukd.dest = PostgreSQLObjectInfo.fetchConstraintUKFromSqlServer(cu.cName);
			ReportInstance.report.uniqueKey.add(ukd);
		}
		return true;
	}

	public static List<ConstraintUK> fetchConstraintUKFromSqlServer() {
		logger.info("fetchConstraintUKFromSqlServer");
		List<ConstraintUK> list = new LinkedList<>();
		List<String> constrNames = new LinkedList<>();
		// String sql =
		// "select a.TABLE_SCHEMA as TSCHEMA,a.TABLE_NAME as TNAME,a.CONSTRAINT_NAME as CNAME,b.COLUMN_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS a,INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE b where a.CONSTRAINT_TYPE = 'UNIQUE' and a.CONSTRAINT_NAME = b.CONSTRAINT_NAME";
		String sql = "select CONSTRAINT_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS  where CONSTRAINT_TYPE = 'UNIQUE'";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		logger.info(sql);
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				constrNames.add(rs.getString("CONSTRAINT_NAME"));
			}
			for (String constrName : constrNames) {
				list.add(fetchConstraintUKFromSqlServer(constrName));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		logger.info("UNIQUE KEYS:" + list);
		return list;
	}

	public static ConstraintUK fetchConstraintUKFromSqlServer(String constrName) {
		logger.info("fetchConstraintUKFromSqlServer");
		ConstraintUK cu = null;
		String sql = "select a.TABLE_SCHEMA as TSCHEMA,a.TABLE_NAME as TNAME,a.CONSTRAINT_NAME as CNAME,b.COLUMN_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS a,INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE b where a.CONSTRAINT_TYPE = 'UNIQUE' and a.CONSTRAINT_NAME = b.CONSTRAINT_NAME and a.CONSTRAINT_NAME=?";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			ps.setString(1, constrName);
			rs = ps.executeQuery();
			List<String> columns = new LinkedList<>();
			while (rs.next()) {
				cu = new ConstraintUK(rs.getString("TSCHEMA"), rs.getString("TNAME"), rs.getString("CNAME"));
				columns.add(rs.getString("COLUMN_NAME"));
			}
			logger.info("create unique key " + constrName.toUpperCase() + " successfully");
			cu.columns = columns;
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		return cu;
	}
}
