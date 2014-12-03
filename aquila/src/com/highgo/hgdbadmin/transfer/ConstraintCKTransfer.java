package com.highgo.hgdbadmin.transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.highgo.hgdbadmin.model.ConstraintCK;
import com.highgo.hgdbadmin.myutil.C3P0Util;
import com.highgo.hgdbadmin.myutil.PostgreSQLObjectInfo;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;
import com.highgo.hgdbadmin.reportable.CheckKeyDetail;
import com.highgo.hgdbadmin.reportable.Constant;
import com.highgo.hgdbadmin.reportable.ObjectInfo;
import com.highgo.hgdbadmin.reportable.ObjectTable;
import com.highgo.hgdbadmin.reportable.ReportInstance;

public class ConstraintCKTransfer {

	private static Logger logger = Logger.getLogger(ConstraintCKTransfer.class);

	/**
	 * 
	 * @param constrName
	 * @return
	 * 
	 *         public static boolean createConstraintCK(String constrName) {
	 *         logger.info("createConstraintCK");
	 * 
	 *         ObjectInfo objectInfo = new ObjectInfo(); objectInfo.objectName =
	 *         constrName; objectInfo.isSuccessed = true;
	 * 
	 *         Connection conn = null; Statement st = null; try { ConstraintCK
	 *         cc = fetchConstraintCKFormSqlServer(constrName); conn =
	 *         C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES); st =
	 *         conn.createStatement(); String sql = cc.toSql();
	 * 
	 *         logger.info(sql); objectInfo.sqlGenerated = sql;
	 * 
	 *         st.execute(sql); logger.info("create check constraint " +
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
	 *         .CHECK_CONSTRAINT).rows.add(objectInfo);
	 * 
	 *         } return true; }
	 */

	public static boolean createConstraintCK(String constrName) {
		ConstraintCK cc = fetchConstraintCKFormSqlServer(constrName);
		return createConstraintCK(cc);
	}

	public static boolean createConstraintCK(ConstraintCK cc) {
		logger.info("createConstraintCK");

		ObjectTable ckTable = ReportInstance.getObjectTable(Constant.CHECK_CONSTRAINT);

		ObjectInfo objectInfo = new ObjectInfo();
		objectInfo.objectName = cc.cName;
		objectInfo.isSuccessed = true;

		CheckKeyDetail ckd = new CheckKeyDetail();

		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn.createStatement();
			String sql = cc.toSql();

			logger.info(sql);
			objectInfo.sqlGenerated = sql;
			ckTable.sum++;

			st.execute(sql);
			ckTable.numSuccessed++;
			logger.info("create check constraint " + cc.cName.toUpperCase() + " successfully");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());

			objectInfo.isSuccessed = false;
			objectInfo.causes.add(e.getMessage());
			ckTable.numFailed++;

			return false;
		} finally {
			C3P0Util.getInstance().close(st);
			C3P0Util.getInstance().close(conn);

			objectInfo.sourceDifinition = "";
			ckTable.rows.add(objectInfo);
			ckd.baseInfo = objectInfo;
			ckd.source = cc;
			ckd.dest = PostgreSQLObjectInfo.fetchConstraintCKFormSqlServer(cc.cName);
			ReportInstance.report.checkKeys.add(ckd);
		}
		return true;
	}

	public static List<ConstraintCK> fetchConstraintCKFromSqlServer() {
		logger.info("fetchConstraintCKFromSqlServer");
		List<ConstraintCK> list = new LinkedList<>();
		String sql = "select b.TABLE_SCHEMA as TSCHEMA,b.TABLE_NAME as TNAME,b.CONSTRAINT_NAME as CNAME,a.CHECK_CLAUSE as CLAUSE from INFORMATION_SCHEMA.CHECK_CONSTRAINTS a ,INFORMATION_SCHEMA.TABLE_CONSTRAINTS b where a.CONSTRAINT_NAME = b.CONSTRAINT_NAME";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new ConstraintCK(rs.getString("TSCHEMA"), rs.getString("TNAME"), rs.getString("CNAME"), rs
						.getString("CLAUSE")));

			}
			logger.info("CHECK CONSTRAINTS:" + list);
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

	public static ConstraintCK fetchConstraintCKFormSqlServer(String constrName) {
		logger.info("fetchConstraintCKFormSqlServer");
		String sql = "select b.TABLE_SCHEMA as TSCHEMA,b.TABLE_NAME as TNAME,b.CONSTRAINT_NAME as CNAME,a.CHECK_CLAUSE as CLAUSE from INFORMATION_SCHEMA.CHECK_CONSTRAINTS a ,INFORMATION_SCHEMA.TABLE_CONSTRAINTS b where a.CONSTRAINT_NAME = b.CONSTRAINT_NAME  AND b.CONSTRAINT_NAME = ?";
		logger.info(sql);
		ConstraintCK cc = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			ps.setString(1, constrName);
			cc = null;
			rs = ps.executeQuery();
			while (rs.next()) {
				cc = new ConstraintCK(rs.getString("TSCHEMA"), rs.getString("TNAME"), rs.getString("CNAME"),
						rs.getString("CLAUSE"));
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
		return cc;
	}
}
