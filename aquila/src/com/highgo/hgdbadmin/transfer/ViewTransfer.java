package com.highgo.hgdbadmin.transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.highgo.hgdbadmin.model.Schema;
import com.highgo.hgdbadmin.model.View;
import com.highgo.hgdbadmin.myutil.C3P0Util;
import com.highgo.hgdbadmin.myutil.PostgreSQLObjectInfo;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;
import com.highgo.hgdbadmin.reportable.Constant;
import com.highgo.hgdbadmin.reportable.ObjectInfo;
import com.highgo.hgdbadmin.reportable.ObjectTable;
import com.highgo.hgdbadmin.reportable.ReportInstance;
import com.highgo.hgdbadmin.reportable.ViewDetail;

public class ViewTransfer {

	private static Logger logger = Logger.getLogger(ViewTransfer.class);

	/**
	 * 目前是假设创建语句table都不带Schema，只能处理tableName都带Schema和都不带Schema的情况，表明没有重复的情况
	 * 
	 * @param source
	 * @param dest
	 * @param schema
	 * @param view
	 * @return
	 * @throws SQLException
	
	
	public static boolean createView(String schema, String view) {
		logger.info("createView");
		
		ObjectTable viewTable = ReportInstance.getObjectTable(Constant.VIEW);
		
		ObjectInfo objectInfo = new ObjectInfo();
		objectInfo.objectName = schema+"."+view;
		objectInfo.isSuccessed = true;
		
		ViewDetail vd = new ViewDetail();
		
		Connection conn = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		Connection conn2 = null;
		Statement st = null;
		try {
			View viewv = fetchViewFromSqlServer(schema, view);
			
			String definition = viewv.toSql();
			if (definition.indexOf(" " + schema + "." + viewv.name + " ") == -1) {
				definition = definition.replaceAll(" " + view + " ", " " + schema + "." + view + " ");
			}
			String sql2 = "SELECT TABLE_SCHEMA,TABLE_NAME FROM INFORMATION_SCHEMA.VIEW_TABLE_USAGE WHERE VIEW_SCHEMA = ? and VIEW_NAME = ?";
			logger.info(sql2);
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps2 = conn.prepareStatement(sql2);
			ps2.setString(1, schema);
			ps2.setString(2, view);
			rs2 = ps2.executeQuery();
			while (rs2.next()) {
				String tableSchema = rs2.getString("TABLE_SCHEMA");
				String tableName = rs2.getString("TABLE_NAME");
				// 很神奇，勿动,前三个处理带where语句的情况，最后一个处理不带where语句的情况
				// create view dbo.TestView4
				// as
				// select * from dbo.TSCHEMA7 where ...
				if (definition.indexOf(" " + tableSchema + "." + tableName + " ") == -1) {
					definition = definition
							.replaceAll(" " + tableName + " ", " " + tableSchema + "." + tableName + " ");
					definition = definition
							.replaceAll(" " + tableName + ",", " " + tableSchema + "." + tableName + ",");
					definition = definition
							.replaceAll("," + tableName + " ", "," + tableSchema + "." + tableName + " ");
					definition = definition.replaceAll(" " + tableName, " " + tableSchema + "." + tableName);
					definition = definition.replaceAll("," + tableName, "," + tableSchema + "." + tableName);
				}
			}
			viewv.definition = definition;
			vd.source = viewv;
			
			conn2 = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn2.createStatement();
			
			logger.info(definition);
			objectInfo.sqlGenerated  = definition;
			
			viewTable.sum++;
			st.execute(definition);
			viewTable.numSuccessed++;
			
			logger.info("create view " + viewv + " successfully");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			
			objectInfo.isSuccessed = false;
			objectInfo.causes.add(e.getMessage());
			viewTable.numFailed++;
			
			return false;
		} finally {
			
			C3P0Util.getInstance().close(rs2);
			C3P0Util.getInstance().close(ps2);
			C3P0Util.getInstance().close(conn);
			C3P0Util.getInstance().close(st);
			C3P0Util.getInstance().close(conn2);
			
			
			objectInfo.sourceDifinition = "";
			vd.baseInfo = objectInfo;
			viewTable.rows.add(objectInfo);
		}
		return true;
	}
	 */
	
	
	public static boolean createView(String schema,String name){
		View viewv = fetchViewFromSqlServer(schema, name);
		return createView(viewv);
	}
	

	public static boolean createView(View viewv) {
		logger.info("createView");
		
		ObjectTable viewTable = ReportInstance.getObjectTable(Constant.VIEW);
		
		ObjectInfo objectInfo = new ObjectInfo();
		objectInfo.objectName = viewv.schema+"."+viewv.name;
		objectInfo.isSuccessed = true;
		
		ViewDetail vd = new ViewDetail();
		
		String definition = viewv.toSql();
		// 将view的定义中的ViewName替换成SchemaName.ViewName
		if (definition.contains(" " + viewv.schema + "." + viewv.name + " ")) {
			definition = definition.replaceAll(" " + viewv.name + " ", " " + viewv.schema + "." + viewv.name + " ");
		}
		// 去掉[和]，SqlServer中经常出现这个，无语了
		if (definition.contains("[") || definition.contains("]")) {
			definition = definition.replace("[", "");
			definition = definition.replace("]", "");
		}
		Connection conn = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		Connection conn2 = null;
		Statement st = null;

		String sql2 = "SELECT TABLE_SCHEMA,TABLE_NAME FROM INFORMATION_SCHEMA.VIEW_TABLE_USAGE WHERE VIEW_SCHEMA = ? and VIEW_NAME = ?";
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps2 = conn.prepareStatement(sql2);
			ps2.setString(1, viewv.schema);
			ps2.setString(2, viewv.name);
			rs2 = ps2.executeQuery();
			while (rs2.next()) {
				String tableSchema = rs2.getString("TABLE_SCHEMA");
				String tableName = rs2.getString("TABLE_NAME");
				// 很神奇，勿动,前三个处理带where语句的情况，最后一个处理不带where语句的情况
				// create view dbo.TestView4
				// as
				// select * from dbo.TSCHEMA7 where ...
				if (definition.indexOf(" " + tableSchema + "." + tableName + " ") == -1) {
					definition = definition
							.replaceAll(" " + tableName + " ", " " + tableSchema + "." + tableName + " ");
					System.out.println(definition);
					definition = definition
							.replaceAll(" " + tableName + ",", " " + tableSchema + "." + tableName + ",");
					System.out.println(definition);
					definition = definition
							.replaceAll("," + tableName + " ", "," + tableSchema + "." + tableName + " ");
					System.out.println(definition);
					definition = definition.replaceAll(" " + tableName, " " + tableSchema + "." + tableName);
					System.out.println(definition);
					definition = definition.replaceAll("," + tableName, "," + tableSchema + "." + tableName);
				}
			}
			
			conn2 = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn2.createStatement();
			
			logger.info(definition);
			objectInfo.sqlGenerated  = definition;
			viewv.definition = definition;
			vd.source = viewv;
			viewTable.sum++;
			
			st.execute(definition);
			
			viewTable.numSuccessed++;
			logger.info("create view " + viewv + " successfully");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());
			
			objectInfo.isSuccessed = false;
			objectInfo.causes.add(e.getMessage());
			viewTable.numFailed++;
			
			return false;
		} finally {
			C3P0Util.getInstance().close(rs2);
			C3P0Util.getInstance().close(ps2);
			C3P0Util.getInstance().close(conn);
			C3P0Util.getInstance().close(st);
			C3P0Util.getInstance().close(conn2);
			
			objectInfo.sourceDifinition = "";
			viewTable.rows.add(objectInfo);
			vd.baseInfo = objectInfo;
			vd.dest = PostgreSQLObjectInfo.fetchViewFromSqlServer(viewv.schema, viewv.name);
			ReportInstance.report.views.add(vd);
		}
		return true;
	}

	public static List<View> fetchViewsFromSqlServer() {
		logger.info("fetchViewsFromSqlServer");
		List<View> list = new LinkedList<>();
		List<Schema> schemas = SchemaTransfer.fetchSchemasFromSqlServer();
		Iterator<Schema> iterator = schemas.iterator();
		while (iterator.hasNext()) {
			list.addAll(fetchViewsFromSqlServer(iterator.next().name));
		}
		logger.info("VIEWS:" + list);
		return list;
	}

	public static List<View> fetchViewsFromSqlServer(String schema) {
		logger.info("fetchViewsFromSqlServer");
		List<View> list = new LinkedList<>();
		String sql = "SELECT TABLE_NAME,VIEW_DEFINITION FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = ?";
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
				list.add(new View(schema, rs.getString("TABLE_NAME"), rs.getString("VIEW_DEFINITION")));
			}
			logger.info("VIEWS IN SCHEMA[" + schema.toUpperCase() + "]:" + list);
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
	 * TABLE_NAME就是view的name
	 * 
	 * @param source
	 * @param schema
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public static View fetchViewFromSqlServer(String schema, String name) {
		logger.info("fetchViewFromSqlServer");
		View view = null;
		String sql = "SELECT VIEW_DEFINITION FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";
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
				view = new View(schema, name, rs.getString("VIEW_DEFINITION"));
			}
			logger.info("VIEW:" + view);
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
		return view;
	}

}
