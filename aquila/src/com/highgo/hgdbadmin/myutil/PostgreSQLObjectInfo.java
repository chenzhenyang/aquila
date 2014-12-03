package com.highgo.hgdbadmin.myutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.groovy.tools.shell.Groovysh;

import com.highgo.hgdbadmin.model.Column;
import com.highgo.hgdbadmin.model.ConstraintCK;
import com.highgo.hgdbadmin.model.ConstraintFK;
import com.highgo.hgdbadmin.model.ConstraintPK;
import com.highgo.hgdbadmin.model.ConstraintUK;
import com.highgo.hgdbadmin.model.Index;
import com.highgo.hgdbadmin.model.Sequence;
import com.highgo.hgdbadmin.model.Table;
import com.highgo.hgdbadmin.model.View;
import com.highgo.hgdbadmin.transfer.ConstraintPKTransfer;

public class PostgreSQLObjectInfo {

	private static Logger logger = Logger.getLogger(PostgreSQLObjectInfo.class);

	// Shcema比较简单，先略过
	public static Table fetchATableFromSqlServer(String schema, String table) {
		schema = schema.toLowerCase();
		table = table.toLowerCase();
		logger.info("PostgreSQLObjectInfo");
		Table tab = null;
		String sql = "SELECT TABLE_SCHEMA, TABLE_NAME,TABLE_TYPE FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='BASE TABLE' AND TABLE_SCHEMA=? AND TABLE_NAME=?";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
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
			// ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		return tab;
	}

	private static List<Column> fetchColumnsForATable(String schema, String table) {
		logger.info("fetchColumnsForATable");
		List<Column> list = new LinkedList<>();
		String sql = "SELECT COLUMN_NAME,COLUMN_DEFAULT,DATA_TYPE,IS_NULLABLE,CHARACTER_MAXIMUM_LENGTH,NUMERIC_PRECISION,NUMERIC_SCALE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? and TABLE_NAME = ? ORDER BY ORDINAL_POSITION";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
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
			// ShellEnvironment.println(e.getMessage());
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
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
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
			// ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		return list;
	}

	public static Sequence fetchSequencesFromSqlServer(String schema, String name) {
		schema = schema.toLowerCase();
		name = name.toLowerCase();
		logger.info("fetchSequencesFromSqlServer");
		Sequence sequence = null;
		String sql = "select SEQUENCE_SCHEMA,SEQUENCE_NAME,1 as START,MAXIMUM_VALUE as MAXIMUM,MINIMUM_VALUE as MINIMUM,INCREMENT,CYCLE_OPTION from information_schema.sequences where SEQUENCE_SCHEMA = ? and SEQUENCE_NAME=?";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			ps = conn.prepareStatement(sql);
			// ps.setString(1, name);
			ps.setString(1, schema);
			ps.setString(2, name);
			rs = ps.executeQuery();
			while (rs.next()) {
				String minmum = rs.getString("MINIMUM");
				String maxmum = rs.getString("MAXIMUM");
				String increment = rs.getString("INCREMENT");
				int cycle = rs.getInt("CYCLE_OPTION");
				sequence = new Sequence(rs.getString("SEQUENCE_SCHEMA"), rs.getString("SEQUENCE_NAME"),
						Long.parseLong(rs.getString("START")), Long.parseLong(minmum == null ? "0" : "" + minmum),
						Long.parseLong(maxmum == null ? "0" : "" + maxmum), Integer.parseInt(increment == null ? "0"
								: "" + increment), cycle);
			}
			logger.info("SEQUENCE:" + sequence);
			rs.close();
			ps.close();
			conn.close();
		} catch (NumberFormatException | SQLException e) {
			logger.error(e.getMessage());
			// ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		return sequence;

	}

	/**
	 * 这个地方一定要注意，PG中的对象名都是小写的，在元数据表中也是小写的
	 * 
	 * @param schema
	 * @param name
	 * @return
	 */
	public static View fetchViewFromSqlServer(String schema, String name) {
		schema = schema.toLowerCase();
		name = name.toLowerCase();
		logger.info("fetchViewFromSqlServer");
		View view = null;
		String sql = "select table_schema,table_name,view_definition from information_schema.views where table_schema =? and table_name = ?";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			ps = conn.prepareStatement(sql);
			ps.setString(1, schema);
			ps.setString(2, name);
			rs = ps.executeQuery();
			while (rs.next()) {
				view = new View(rs.getString("table_schema"), rs.getString("table_name"),
						rs.getString("VIEW_DEFINITION"));
			}
			logger.info("VIEW:" + view);
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			// ShellEnvironment.println(e.getMessage());
			return null;
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		return view;
	}

	/**
	 * 查询指定Name的Index
	 * 
	 * @param source
	 * @param tableSchema
	 * @param indexNamePara
	 * @return
	 * @throws SQLException
	 */
	public static Index fetchIndexesFromSqlServer(String tableSchema, String tableNamePara, String indexNamePara) {
		tableSchema = tableSchema.toLowerCase();
		tableNamePara = tableNamePara.toLowerCase();
		indexNamePara = indexNamePara.toLowerCase();

		logger.info("fetchIndexesFromSqlServer");
		Index index = null;
		String sql = "select indisunique,indkey,schemaname,relname as tablename,indexrelname as indexname from  pg_index,pg_statio_user_indexes where pg_index.indexrelid = pg_statio_user_indexes.indexrelid and pg_statio_user_indexes.schemaname = ? and pg_statio_user_indexes.relname=? and pg_statio_user_indexes.indexrelname = ? ";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			ps = conn.prepareStatement(sql);
			ps.setString(1, tableSchema);
			ps.setString(2, tableNamePara);
			ps.setString(3, indexNamePara);
			rs = ps.executeQuery();
			boolean isUnique = false;
			String indexName = null;
			String tableSchemat = null;
			String tableName = null;
			List<String> columns = new ArrayList<>();
			List<Boolean> isDescendingKeys = new ArrayList<>();
			while (rs.next()) {
				isUnique = rs.getBoolean("indisunique");
				indexName = rs.getString("indexname");
				tableSchemat = rs.getString("schemaname");
				tableName = rs.getString("tablename");
				String[] strs = rs.getString("indkey").split(" ");
				// columns.add(rs.getString("indkey"));
				for (String str : strs) {
//					columns.add(str);
					isDescendingKeys.add(false);
				}
				columns.addAll(fetchColumnNamesForAIndexFromSqlServer(tableSchema,tableNamePara,indexNamePara,strs));
			}
			index = new Index(indexName, tableSchemat, tableName, isUnique, columns, isDescendingKeys);
			logger.info("INDEX [" + tableSchema.toUpperCase() + "." + indexNamePara.toUpperCase() + "]:" + index);
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
		return index;
	}

	private static List<String> fetchColumnNamesForAIndexFromSqlServer(String tableSchema, String tableNamePara,
			String indexNamePara,String[] columnNum) {
		List<String> columnNames = new LinkedList<String>();
		logger.info("fetchColumnNamesForAIndexFromSqlServer");
		String attnum = "";
		if(columnNum.length>1){
			for (int i = 0; i < columnNum.length - 1; i++) {
				attnum = "pg_attribute.attnum = " + columnNum[i] + " or ";
			}
		}
		attnum += "pg_attribute.attnum = " + columnNum[(columnNum.length - 1)];
		String sql = "select attname from pg_statio_user_indexes,pg_attribute where pg_statio_user_indexes.relid = pg_attribute.attrelid AND schemaname = ? AND relname = ? and indexrelname=? and ("
				+ attnum + ")";
		System.out.println(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			ps = conn.prepareStatement(sql);
			ps.setString(1, tableSchema);
			ps.setString(2, tableNamePara);
			ps.setString(3, indexNamePara);
			rs = ps.executeQuery();
			while (rs.next()) {
				columnNames.add(rs.getString("attname"));
			}
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
		return columnNames;
	}

	public static ConstraintCK fetchConstraintCKFormSqlServer(String constrName) {
		constrName = constrName.toLowerCase();
		logger.info("fetchConstraintCKFormSqlServer");
		String sql = "select b.TABLE_SCHEMA as TSCHEMA,b.TABLE_NAME as TNAME,b.CONSTRAINT_NAME as CNAME,a.CHECK_CLAUSE as CLAUSE from INFORMATION_SCHEMA.CHECK_CONSTRAINTS a ,INFORMATION_SCHEMA.TABLE_CONSTRAINTS b where a.CONSTRAINT_NAME = b.CONSTRAINT_NAME  AND b.CONSTRAINT_NAME = ?";
		logger.info(sql);
		ConstraintCK cc = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
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

	public static ConstraintPK fetchConstraintPKFromSqlServer(String constrName) {
		constrName = constrName.toLowerCase();
		logger.info("fetchConstraintPKFromSqlServer");
		ConstraintPK cp = null;
		String sql = "select a.TABLE_SCHEMA as TSCHEMA,a.TABLE_NAME as TNAME,a.COLUMN_NAME from INFORMATION_SCHEMA.KEY_COLUMN_USAGE a where a.CONSTRAINT_NAME = ?";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
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

	public static ConstraintUK fetchConstraintUKFromSqlServer(String constrName) {
		constrName = constrName.toLowerCase();
		logger.info("fetchConstraintUKFromSqlServer");
		ConstraintUK cu = null;
		String sql = "select a.TABLE_SCHEMA as TSCHEMA,a.TABLE_NAME as TNAME,a.CONSTRAINT_NAME as CNAME,b.COLUMN_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS a,INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE b where a.CONSTRAINT_TYPE = 'UNIQUE' and a.CONSTRAINT_NAME = b.CONSTRAINT_NAME and a.CONSTRAINT_NAME=?";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
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

	public static ConstraintFK fetchConstraintFKFromSqlServer(String constrName) {
		logger.info("fetchConstraintFKFromSqlServer");
		ConstraintFK cf = null;
		String sql = "select b.TABLE_SCHEMA,b.TABLE_NAME,b.CONSTRAINT_NAME,c.COLUMN_NAME,a.UNIQUE_CONSTRAINT_NAME,a.MATCH_OPTION,a.UPDATE_RULE,a.DELETE_RULE from INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS a,INFORMATION_SCHEMA.TABLE_CONSTRAINTS b,INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE c where a.CONSTRAINT_NAME = b.CONSTRAINT_NAME  and a.CONSTRAINT_NAME = c.CONSTRAINT_NAME and a.CONSTRAINT_NAME = ?";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
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

	public static void main(String[] args) {
		// Table table = PostgreSQLObjectInfo.fetchATableFromSqlServer("dbo",
		// "blob");
		// System.out.println(table.toString2());
		// Table table2 = MigrateCenter.fetchATableFromSqlServer("dbo", "blob");
		// System.out.println(table2.toString2());

		// Sequence sequence =
		// PostgreSQLObjectInfo.fetchSequencesFromSqlServer("public",
		// "tbl_xulie2_id_seq");
		// System.out.println(sequence.toString2());

		// View view = PostgreSQLObjectInfo.fetchViewFromSqlServer("dbo",
		// "view_siteforminfo");
		// System.out.println(view.toString2());

		
		System.setProperty("groovysh.prompt", "aquila");
		Groovysh shell = new Groovysh();
		ShellEnvironment.setIo(shell.getIo());

//		InitialSourceList.initialSourceObjectList();
		
		Index index = PostgreSQLObjectInfo.fetchIndexesFromSqlServer("dbo", "sitemonitorexturlinfo", "sitemonitorexturlinfo_index_1");
		System.out.println(index.toString2());

	}
}