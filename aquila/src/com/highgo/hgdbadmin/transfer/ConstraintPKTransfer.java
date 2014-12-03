package com.highgo.hgdbadmin.transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.highgo.hgdbadmin.model.ConstraintPK;
import com.highgo.hgdbadmin.myutil.C3P0Util;
import com.highgo.hgdbadmin.myutil.PostgreSQLObjectInfo;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;
import com.highgo.hgdbadmin.reportable.Constant;
import com.highgo.hgdbadmin.reportable.ObjectInfo;
import com.highgo.hgdbadmin.reportable.ObjectTable;
import com.highgo.hgdbadmin.reportable.PrimaryKeyDetail;
import com.highgo.hgdbadmin.reportable.ReportInstance;

public class ConstraintPKTransfer {

	private static Logger logger = Logger.getLogger(ConstraintPKTransfer.class);

	/**
	 * 
	 * @param constrName
	 * @return
	 * 
	 *         public static boolean createConstraintPK(String constrName) {
	 *         logger.info("createConstraintPK");
	 * 
	 *         ObjectInfo objectInfo = new ObjectInfo(); objectInfo.objectName =
	 *         constrName; objectInfo.isSuccessed = true;
	 * 
	 *         Connection conn = null; Statement st = null; try { ConstraintPK
	 *         cp = fetchConstraintPKFromSqlServer(constrName); conn =
	 *         C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES); st =
	 *         conn.createStatement(); String sql = cp.toSql();
	 * 
	 *         logger.info(sql); objectInfo.sqlGenerated = sql;
	 * 
	 *         st.execute(sql); logger.info("create primary key" +
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

	public static boolean createConstraintPK(String constrName) {
		ConstraintPK cp = fetchConstraintPKFromSqlServer(constrName);
		return createConstraintPK(cp);
	}

	public static boolean createConstraintPK(ConstraintPK constraint) {
		logger.info("createConstraintPK");

		ObjectTable pkTable = ReportInstance.getObjectTable(Constant.PRIMARY_KEY);

		ObjectInfo objectInfo = new ObjectInfo();
		objectInfo.objectName = constraint.cName;
		objectInfo.isSuccessed = true;

		PrimaryKeyDetail pkd = new PrimaryKeyDetail();

		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn.createStatement();
			String sql = constraint.toSql();

			logger.info(sql);
			objectInfo.sqlGenerated = sql;
			pkTable.sum++;

			st.execute(sql);
			pkTable.numSuccessed++;
			logger.info("create primary key" + constraint.cName.toUpperCase() + " successfully");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());

			pkTable.numFailed++;
			objectInfo.isSuccessed = false;
			objectInfo.causes.add(e.getMessage());

			return false;
		} finally {
			C3P0Util.getInstance().close(st);
			C3P0Util.getInstance().close(conn);

			objectInfo.sourceDifinition = "";
			pkTable.rows.add(objectInfo);
			pkd.baseInfo = objectInfo;
			pkd.source = constraint;
			pkd.dest = PostgreSQLObjectInfo.fetchConstraintPKFromSqlServer(constraint.cName);
			ReportInstance.report.primaryKeys.add(pkd);
			
		}
		return true;
	}

	public static List<ConstraintPK> fetchConstraintPKFromSqlServer() {
		logger.info("fetchConstraintPKFromSqlServer");
		List<ConstraintPK> list = new LinkedList<>();
		List<String> constrNames = new LinkedList<>();
		String sql = "select CONSTRAINT_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS  where CONSTRAINT_TYPE = 'PRIMARY KEY'";
		logger.info(sql);

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				constrNames.add(rs.getString("CONSTRAINT_NAME"));
			}
			for (String constrName : constrNames) {
				list.add(fetchConstraintPKFromSqlServer(constrName));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(st);
			C3P0Util.getInstance().close(conn);
		}
		logger.info("PRIMARY KEY:" + list);
		return list;
	}

	public static ConstraintPK fetchConstraintPKFromSqlServer(String constrName) {
		logger.info("fetchConstraintPKFromSqlServer");
		ConstraintPK cp = null;
		String sql = "select a.TABLE_SCHEMA as TSCHEMA,a.TABLE_NAME as TNAME,a.COLUMN_NAME from INFORMATION_SCHEMA.KEY_COLUMN_USAGE a where a.CONSTRAINT_NAME = ?";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			ps.setString(1, constrName);
			rs = ps.executeQuery();
			String tschema = null;
			String ttable = null;
			List<String> columns = new LinkedList<>();
			while (rs.next()) {
				tschema = rs.getString("TSCHEMA");
				ttable = rs.getString("TNAME");
				columns.add(rs.getString("COLUMN_NAME"));
			}
			cp = new ConstraintPK(tschema, ttable, constrName, columns);
			logger.info("PRIMARY KEY:" + cp);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		return cp;
	}
}
