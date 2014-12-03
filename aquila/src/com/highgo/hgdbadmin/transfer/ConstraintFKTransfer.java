package com.highgo.hgdbadmin.transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.highgo.hgdbadmin.model.ConstraintFK;
import com.highgo.hgdbadmin.model.ConstraintPK;
import com.highgo.hgdbadmin.myutil.C3P0Util;
import com.highgo.hgdbadmin.myutil.PostgreSQLObjectInfo;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;
import com.highgo.hgdbadmin.reportable.Constant;
import com.highgo.hgdbadmin.reportable.ForeignKeyDetail;
import com.highgo.hgdbadmin.reportable.ObjectInfo;
import com.highgo.hgdbadmin.reportable.ObjectTable;
import com.highgo.hgdbadmin.reportable.ReportInstance;

public class ConstraintFKTransfer {

	private static Logger logger = Logger.getLogger(ConstraintFKTransfer.class);

	/**
	 * 
	 * @param constrName
	 * @return
	 * 
	 *         public static boolean createConstraintFK(String constrName) {
	 *         logger.info("createConstraintFK");
	 * 
	 *         ObjectInfo objectInfo = new ObjectInfo(); objectInfo.objectName =
	 *         constrName; objectInfo.isSuccessed = true;
	 * 
	 *         Connection conn = null; Statement st = null; try { ConstraintFK
	 *         cf = fetchConstraintFKFromSqlServer(constrName); conn =
	 *         C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES); st =
	 *         conn.createStatement(); String sql = cf.toSql();
	 * 
	 *         logger.info(sql); objectInfo.sqlGenerated = sql;
	 * 
	 *         st.execute(sql); logger.info("create foreign key " +
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

	public static boolean createConstraintFK(String constrName) {
		ConstraintFK cf = fetchConstraintFKFromSqlServer(constrName);
		return createConstraintFK(cf);
	}

	public static boolean createConstraintFK(ConstraintFK cf) {
		logger.info("createConstraintFK");

		ObjectTable cfTable = ReportInstance.getObjectTable(Constant.CHECK_CONSTRAINT);

		ObjectInfo objectInfo = new ObjectInfo();
		objectInfo.objectName = cf.cName;
		objectInfo.isSuccessed = true;

		ForeignKeyDetail fkd = new ForeignKeyDetail();

		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn.createStatement();
			String sql = cf.toSql();

			logger.info(sql);
			objectInfo.sqlGenerated = sql;
			cfTable.sum++;

			st.execute(sql);
			cfTable.numSuccessed++;
			logger.info("create foreign key " + cf.cName.toUpperCase() + " successfully");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());

			cfTable.numFailed++;
			objectInfo.isSuccessed = false;
			objectInfo.causes.add(e.getMessage());

			return false;
		} finally {
			C3P0Util.getInstance().close(st);
			C3P0Util.getInstance().close(conn);

			objectInfo.sourceDifinition = "";
			cfTable.rows.add(objectInfo);
			fkd.baseInfo = objectInfo;
			fkd.source = cf;
			fkd.dest = PostgreSQLObjectInfo.fetchConstraintFKFromSqlServer(cf.cName);
			ReportInstance.report.foreignKeys.add(fkd);
		}
		return true;
	}

	public static List<ConstraintFK> fetchConstraintFKFromSqlServer() {
		logger.info("fetchConstraintFKFromSqlServer");
		List<ConstraintFK> list = new LinkedList<>();
		List<String> constrNames = new LinkedList<>();
		String sql = "select CONSTRAINT_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS where CONSTRAINT_TYPE = 'FOREIGN KEY'";
		logger.info(sql);
		Connection conn = null;
		Statement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.createStatement();
			rs = ps.executeQuery(sql);
			while (rs.next()) {
				constrNames.add(rs.getString("CONSTRAINT_NAME"));
			}
			for (String constrName : constrNames) {
				list.add(fetchConstraintFKFromSqlServer(constrName));
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
		logger.info("FOREIGN KEY:" + list);
		return list;
	}

	public static ConstraintFK fetchConstraintFKFromSqlServer(String constrName) {
		logger.info("fetchConstraintFKFromSqlServer");
		ConstraintFK cf = null;
		String sql = "select b.TABLE_SCHEMA,b.TABLE_NAME,b.CONSTRAINT_NAME,c.COLUMN_NAME,a.UNIQUE_CONSTRAINT_NAME,a.MATCH_OPTION,a.UPDATE_RULE,a.DELETE_RULE from INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS a,INFORMATION_SCHEMA.TABLE_CONSTRAINTS b,INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE c where a.CONSTRAINT_NAME = b.CONSTRAINT_NAME  and a.CONSTRAINT_NAME = c.CONSTRAINT_NAME and a.CONSTRAINT_NAME = ?";
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
			String cname = null;
			String uniqueConstraintName = null;
			String matchOption = null;
			String updateRule = null;
			String deleteRule = null;
			List<String> columns = new LinkedList<>();
			ConstraintPK cp = null;
			while (rs.next()) {
				tschema = rs.getString("TABLE_SCHEMA");
				ttable = rs.getString("TABLE_NAME");
				cname = rs.getString("CONSTRAINT_NAME");
				uniqueConstraintName = rs.getString("UNIQUE_CONSTRAINT_NAME");
				cp = ConstraintPKTransfer.fetchConstraintPKFromSqlServer(uniqueConstraintName);
				matchOption = rs.getString("MATCH_OPTION");
				updateRule = rs.getString("UPDATE_RULE");
				deleteRule = rs.getString("DELETE_RULE");
				columns.add(rs.getString("COLUMN_NAME"));
			}
			cf = new ConstraintFK(tschema, ttable, cname, columns, matchOption, updateRule, deleteRule, cp);
			logger.info("FOREIGN KEY:" + cf);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		return cf;
	}
}
