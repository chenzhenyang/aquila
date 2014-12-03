package com.highgo.hgdbadmin.transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.highgo.hgdbadmin.model.Sequence;
import com.highgo.hgdbadmin.myutil.C3P0Util;
import com.highgo.hgdbadmin.myutil.PostgreSQLObjectInfo;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;
import com.highgo.hgdbadmin.reportable.Constant;
import com.highgo.hgdbadmin.reportable.ObjectInfo;
import com.highgo.hgdbadmin.reportable.ObjectTable;
import com.highgo.hgdbadmin.reportable.ReportInstance;
import com.highgo.hgdbadmin.reportable.SequenceDetail;

public class SequenceTransfer {

	private static Logger logger = Logger.getLogger(SequenceTransfer.class);

	/**
	 * Pg
	 * 
	 * @param source
	 * @param dest
	 * @param schema
	 * @param seqname
	 * @return
	 * @throws SQLException
	 * 
	 *             public static boolean createSequence(String schema, String
	 *             seqname) { logger.info("createSequence");
	 * 
	 *             ObjectInfo objectInfo = new ObjectInfo();
	 *             objectInfo.objectName=schema+"."+seqname;
	 *             objectInfo.isSuccessed =true;
	 * 
	 *             Connection conn = null; Statement st = null; try { Sequence
	 *             seq = fetchSequencesFromSqlServer(schema, seqname); if (seq
	 *             == null) { return false; } conn =
	 *             C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES); st =
	 *             conn.createStatement(); String sql = seq.toSql();
	 * 
	 *             logger.info(sql); objectInfo.sqlGenerated = sql;
	 * 
	 *             st.execute(sql); logger.info("create sequence " + schema +
	 *             "." + seqname + " successfully"); } catch (SQLException e) {
	 *             logger.error(e.getMessage());
	 *             ShellEnvironment.println(e.getMessage());
	 * 
	 *             objectInfo.isSuccessed = false;
	 *             objectInfo.causes.add(e.getMessage());
	 * 
	 *             return false; } finally { C3P0Util.getInstance().close(st);
	 *             C3P0Util.getInstance().close(conn);
	 * 
	 *             objectInfo.sourceDifinition = "";
	 *             ReportInstance.getObjectTable
	 *             (Constant.SEQUENCE).rows.add(objectInfo); } return true; }
	 */

	public static boolean createSequence(String schema, String seqname) {
		Sequence seq = fetchSequencesFromSqlServer(schema, seqname);
		if (seq == null) {
			return false;
		}
		return createSequence(seq);
	}

	/**
	 * Pg
	 * 
	 * @param source
	 * @param dest
	 * @param schema
	 * @param seqname
	 * @return
	 * @throws SQLException
	 */
	public static boolean createSequence(Sequence seq) {
		logger.info("createSequence");

		ObjectTable seqTable = ReportInstance.getObjectTable(Constant.SEQUENCE);

		ObjectInfo objectInfo = new ObjectInfo();
		objectInfo.objectName = seq.schema + "." + seq.name;
		objectInfo.isSuccessed = true;

		SequenceDetail sd = new SequenceDetail();

		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn.createStatement();

			String sql = seq.toSql();
			logger.info(sql);
			objectInfo.sqlGenerated = sql;
			seqTable.sum++;

			st.execute(sql);
			seqTable.numSuccessed++;
			logger.info("create sequence " + seq.schema + "." + seq.name + " successfully");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());

			seqTable.numFailed++;
			objectInfo.isSuccessed = false;
			objectInfo.causes.add(e.getMessage());

			return false;
		} finally {
			C3P0Util.getInstance().close(st);
			C3P0Util.getInstance().close(conn);

			objectInfo.sourceDifinition = "";
			seqTable.rows.add(objectInfo);
			sd.baseInfo = objectInfo;
			sd.source = seq;
			sd.dest = PostgreSQLObjectInfo.fetchSequencesFromSqlServer(seq.schema, seq.name);
			ReportInstance.report.sequences.add(sd);
		}
		return true;
	}

	/**
	 * list all sequence in various schema
	 * 
	 * @param source
	 * @return
	 * @throws SQLException
	 */
	public static List<Sequence> fetchSequencesFromSqlServer() {
		logger.info("fetchSequencesFromSqlServer");
		List<Sequence> list = new LinkedList<>();
		String sql = "SELECT SYS.schemas.name as SEQUENCE_SCHEMA,SYS.SEQUENCES.NAME as SEQUENCE_NAME,CAST(CURRENT_VALUE AS VARCHAR) START,CAST(MINIMUM_VALUE AS VARCHAR) MINIMUM,CAST(MAXIMUM_VALUE AS VARCHAR) MAXIMUM,CAST(INCREMENT AS VARCHAR) INCREMENTT,is_cycling as CYCLE_OPTION FROM SYS.SEQUENCES,SYS.schemas WHERE SYS.SEQUENCES.SCHEMA_ID = SYS.schemas.SCHEMA_ID";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Sequence(rs.getString("SEQUENCE_SCHEMA"), rs.getString("SEQUENCE_NAME"), Long.parseLong(rs
						.getString("START")), Long.parseLong(rs.getString("MINIMUM")), Long.parseLong(rs
						.getString("MAXIMUM")), Integer.parseInt(rs.getString("INCREMENTT")), rs.getInt("CYCLE_OPTION")));
			}
			logger.info("SEQUENCES:" + list);
		} catch (NumberFormatException | SQLException e) {
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
	 * CAST(CURRENT_VALUE AS VARCHAR)
	 * START这句的意思是，把在元数据库中的当前值当做在目标数据库中的起始值，其实应该是当前值加1，后边再改
	 * 
	 * @param source
	 * @param schema
	 * @return
	 * @throws SQLException
	 */
	public static List<Sequence> fetchSequencesFromSqlServer(String schema) {
		logger.info("fetchSequencesFromSqlServer");
		List<Sequence> list = new LinkedList<>();
		String sql = "SELECT SYS.schemas.name as SEQUENCE_SCHEMA,SYS.SEQUENCES.NAME as SEQUENCE_NAME,CAST(CURRENT_VALUE AS VARCHAR) START,CAST(MINIMUM_VALUE AS VARCHAR) MINIMUM,CAST(MAXIMUM_VALUE AS VARCHAR) MAXIMUM,CAST(INCREMENT AS VARCHAR) INCREMENTT,is_cycling as CYCLE_OPTION FROM SYS.SEQUENCES,SYS.schemas WHERE SYS.SEQUENCES.SCHEMA_ID = SYS.schemas.SCHEMA_ID AND SYS.schemas.name =?";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			ps.setString(1, schema);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Sequence(rs.getString("SEQUENCE_SCHEMA"), rs.getString("SEQUENCE_NAME"), Long.parseLong(rs
						.getString("START")), Long.parseLong(rs.getString("MINIMUM")), Long.parseLong(rs
						.getString("MAXIMUM")), Integer.parseInt(rs.getString("INCREMENTT")), rs.getInt("CYCLE_OPTION")));
			}
			logger.info("SEQUENCES IN SCHEMA[" + schema.toUpperCase() + "]:" + list);
		} catch (NumberFormatException | SQLException e) {
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
	 * CAST(CURRENT_VALUE AS VARCHAR) START这句的意思是，把在元数据库中的当前值当做在目标数据库中的起始值
	 * 
	 * @param source
	 * @param schema
	 * @return
	 * @throws SQLException
	 */
	public static Sequence fetchSequencesFromSqlServer(String schema, String name) {
		logger.info("fetchSequencesFromSqlServer");
		Sequence sequence = null;
		String sql = "SELECT SYS.schemas.name as SEQUENCE_SCHEMA,SYS.SEQUENCES.NAME as SEQUENCE_NAME,CAST(CURRENT_VALUE AS VARCHAR) START,CAST(MINIMUM_VALUE AS VARCHAR) MINIMUM,CAST(MAXIMUM_VALUE AS VARCHAR) MAXIMUM,CAST(INCREMENT AS VARCHAR) INCREMENTT,is_cycling as CYCLE_OPTION FROM SYS.SEQUENCES,SYS.schemas WHERE SYS.SEQUENCES.SCHEMA_ID = SYS.schemas.SCHEMA_ID AND SYS.schemas.name = ? AND SYS.SEQUENCES.NAME = ?";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			ps.setString(1, schema);
			ps.setString(2, name);
			rs = ps.executeQuery();
			while (rs.next()) {
				sequence = new Sequence(rs.getString("SEQUENCE_SCHEMA"), rs.getString("SEQUENCE_NAME"),
						Long.parseLong(rs.getString("START")), Long.parseLong(rs.getString("MINIMUM")),
						Long.parseLong(rs.getString("MAXIMUM")), Integer.parseInt(rs.getString("INCREMENTT")),
						rs.getInt("CYCLE_OPTION"));
			}
			logger.info("SEQUENCE:" + sequence);
		} catch (NumberFormatException | SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		return sequence;

	}
}
