package com.highgo.hgdbadmin.transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.codehaus.groovy.tools.shell.Groovysh;

import com.highgo.hgdbadmin.model.Schema;
import com.highgo.hgdbadmin.myutil.C3P0Util;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;
import com.highgo.hgdbadmin.reportable.Constant;
import com.highgo.hgdbadmin.reportable.ObjectInfo;
import com.highgo.hgdbadmin.reportable.ObjectTable;
import com.highgo.hgdbadmin.reportable.ReportInstance;
import com.highgo.hgdbadmin.reportable.SchemaDetail;

public class SchemaTransfer {
	private static Logger logger = Logger.getLogger(SchemaTransfer.class);

	/**
	 * 
	 * @param schemastr
	 * @return
	 
	public static boolean createSchema(String schemastr) {
		logger.info("createSchema");

		ObjectTable schemaTable = ReportInstance.getObjectTable(Constant.SCHEMA);
				
		ObjectInfo objectInfo = new ObjectInfo();
		objectInfo.objectName = schemastr;
		objectInfo.isSuccessed = true;
		
		SchemaDetail sd = new SchemaDetail();

		Schema schema = new Schema(schemastr, null);
		sd.source = schema;
		
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			stmt = conn.createStatement();
			String sql = schema.toSql();
			
			logger.info(sql);
			objectInfo.sqlGenerated = sql;
			schemaTable.sum++;
			
			stmt.executeUpdate(sql);
			
			schemaTable.numSuccessed++;
			logger.info("create schema " + schema.name + "successfully");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());

			schemaTable.numFailed++;
			objectInfo.causes.add(e.getMessage());
			objectInfo.isSuccessed = false;
			
			return false;
		} finally {
			C3P0Util.getInstance().close(stmt);
			C3P0Util.getInstance().close(conn);
			
			objectInfo.sourceDifinition = "";
			sd.baseInfo = objectInfo;
			schemaTable.rows.add(objectInfo);
			ReportInstance.report.schemas.add(sd);
		}
		return true;
	}
*/
	
	
	public static boolean createSchema(String schema){
		Schema sch = new Schema(schema, null);
		return createSchema(sch);
	}
	
	/**
	 * 这个创建schema的语句直接用create schema schema.name就可以了，在特定用户下创建的schema自动授权给当前用户
	 * PostgreSQL： 超级用户（postgres）用 select * from information_schema.schemata
	 * 查询，可以查询到当前数据库系统中中所有数据库的所有的schema 普通用户查询只能查询到当前用户的schema。 SqlServer：
	 * 不管什么用户 select * from information_schema.schemata 都能查看当前系统中所有的schema
	 * 
	 * Schema的属性较少，创建时只需要Schema的Name就可以。
	 * 
	 * @param dest
	 * @param schema
	 * @return
	 * @throws SQLException
	 */
	public static boolean createSchema(Schema schema) {
		logger.info("createSchema");

		ObjectTable schemaTable = ReportInstance.getObjectTable(Constant.SCHEMA);
		
		ObjectInfo objectInfo = new ObjectInfo();
		objectInfo.objectName = schema.name;
		objectInfo.isSuccessed =true;
		
		SchemaDetail sd = new SchemaDetail();
		sd.source = schema;
		
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			stmt = conn.createStatement();
			String sql = schema.toSql();

			objectInfo.sqlGenerated = sql;
			logger.info(sql);
			schemaTable.sum++;
			
			stmt.executeUpdate(sql);
			schemaTable.numSuccessed++;
			logger.info("create schema " + schema + " successfully");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());

			schemaTable.numFailed++;
			objectInfo.causes.add(e.getMessage());
			objectInfo.isSuccessed = false;

			return false;
		} finally {
			C3P0Util.getInstance().close(stmt);
			C3P0Util.getInstance().close(conn);
			
			objectInfo.sourceDifinition = "";
			
			sd.baseInfo = objectInfo;
			ReportInstance.getObjectTable(Constant.SCHEMA).rows.add(objectInfo);
		}
		return true;
	}

	/**
	 * SqlServer： 不管什么用户 select * from information_schema.schemata
	 * 都能查看当前系统中所有的schema 所以不管给迁移工具配置什么样的登录用户， select * from
	 * information_schema.schemata 都是系统中所有的schema都查出来。 虽然有SCHEMA_OWNER可以控制，比如
	 * select * from information_schema.schemata where SCHEMA_OWNER=？
	 * 但是不能保证一个数据库中使用了多个Schema
	 * 
	 * @param source
	 * @return
	 * @throws SQLException
	 */
	public static List<Schema> fetchSchemasFromSqlServer() {
		logger.info("fetchSchemasFromSqlServer");
		Set<Schema> set = new HashSet<Schema>();
		List<Schema> list = new LinkedList<>();
		String sql = "SELECT TABLE_SCHEMA FROM INFORMATION_SCHEMA.TABLES";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				set.add(new Schema(rs.getString("TABLE_SCHEMA"), null));
			}
			list.addAll(set);
			logger.info("SCHEMAS:" + list);
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
	
	public static void main(String[] args) {
		
		System.setProperty("groovysh.prompt", "aquila");
		Groovysh shell = new Groovysh();
		ShellEnvironment.setIo(shell.getIo());
		
		SchemaTransfer.createSchema("dbo");
		ObjectTable  ot = ReportInstance.getObjectTable(Constant.SCHEMA);
//		for(ObjectInfo oi : ot.rows){
//			System.out.println(oi);
//		}
		
		System.out.println(ReportInstance.report);
	}
}
