package com.highgo.hgdbadmin.transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.highgo.hgdbadmin.model.Index;
import com.highgo.hgdbadmin.model.Table;
import com.highgo.hgdbadmin.myutil.C3P0Util;
import com.highgo.hgdbadmin.myutil.PostgreSQLObjectInfo;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;
import com.highgo.hgdbadmin.reportable.Constant;
import com.highgo.hgdbadmin.reportable.IndexDetail;
import com.highgo.hgdbadmin.reportable.ObjectInfo;
import com.highgo.hgdbadmin.reportable.ObjectTable;
import com.highgo.hgdbadmin.reportable.ReportInstance;

public class IndexTransfer {

	private static Logger logger = Logger.getLogger(IndexTransfer.class);

	/**
	 * index 有些特别，在SqlServer中会出现不同表有重名的index的情况，所以要对index的名字进行一次混乱，混乱之后的名字，
	 * 改变重名的两个或多个index的名字是随机的。 这样对于创建过程来说，indexName就成了一个不确定的参数。这个统一用下边的API
	 * 
	 * @param schema
	 * @param tableName
	 * @param indexName
	 * @return
	 * 
	 *         public static boolean createIndex(String schema, String
	 *         tableName, String indexName) { logger.info("createIndex");
	 * 
	 *         ObjectInfo objectInfo = new ObjectInfo(); objectInfo.objectName =
	 *         schema+"."+indexName+" ON "+schema+"."+tableName;
	 *         objectInfo.isSuccessed = true;
	 * 
	 *         Connection conn = null; Statement st = null; try { Index idx =
	 *         fetchIndexesFromSqlServer(schema, tableName, indexName); conn =
	 *         C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES); st =
	 *         conn.createStatement(); String sql = idx.toSql();
	 * 
	 *         logger.info(sql); objectInfo.sqlGenerated = sql;
	 * 
	 *         st.execute(sql); logger.info("create index " +
	 *         schema.toUpperCase() + "." + indexName.toLowerCase() +
	 *         " successfully"); } catch (SQLException e) {
	 *         logger.error(e.getMessage());
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
	 *         .INDEX).rows.add(objectInfo); } return true; }
	 */

	public static boolean createIndex(String schema, String tableName, String indexName) {
		Index idx = fetchIndexesFromSqlServer(schema, tableName, indexName);
		return createIndex(idx);
	}

	public static boolean createIndex(Index index) {
		logger.info("createIndex");

		ObjectTable indexTable = ReportInstance.getObjectTable(Constant.INDEX);

		ObjectInfo objectInfo = new ObjectInfo();
		// objectInfo.objectName =
		// index.schema+"."+index.name+" ON "+index.schema+"."+index.tableName;
		objectInfo.objectName = index.schema + "." + index.name;
		objectInfo.isSuccessed = true;

		IndexDetail id = new IndexDetail();

		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			st = conn.createStatement();
			String sql = index.toSql();

			logger.info(sql);
			objectInfo.sqlGenerated = sql;
			indexTable.sum++;

			st.execute(sql);
			indexTable.numSuccessed++;
			logger.info("create index " + index.schema.toUpperCase() + "." + index.name.toLowerCase() + " successfully");
		} catch (SQLException e) {
			System.out.println(e);
			logger.error(e.getMessage());
			ShellEnvironment.println(e.getMessage());

			indexTable.numFailed++;
			objectInfo.isSuccessed = false;
			objectInfo.causes.add(e.getMessage());

			return false;
		} finally {
			C3P0Util.getInstance().close(st);
			C3P0Util.getInstance().close(conn);

			objectInfo.sourceDifinition = "";
			indexTable.rows.add(objectInfo);
			id.baseInfo = objectInfo;
			id.source = index;
			id.dest = PostgreSQLObjectInfo.fetchIndexesFromSqlServer(index.schema, index.tableName, index.name);
			ReportInstance.report.indexes.add(id);
		}
		return true;
	}

	/**
	 * 获取一个表的所有的索引信息
	 * 
	 * @param source
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	@Deprecated
	public static List<Index> fetchIndexesFromSqlServer(Connection source, String table) throws SQLException {
		List<Index> list = new LinkedList<>();
		// 查询一个表的所有索引
		String sql = "select sys.objects.object_id as TABLE_ID, sys.indexes.name as INDEX_NAME,sys.schemas.name as SCHEMA_NAMEE,sys.indexes.is_unique as UNIQUEE,sys.indexes.index_id as INDEX_ID from sys.objects,sys.indexes,sys.schemas where sys.objects.name=? and sys.objects.object_id = sys.indexes.object_id and sys.objects.schema_id = sys.schemas.schema_id";
		PreparedStatement ps = source.prepareStatement(sql);
		ps.setString(1, table);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String indexName = rs.getString("INDEX_NAME");
			if (!(indexName == null || "".equals(indexName))) {
				list.add(new Index(indexName, rs.getString("SCHEMA_NAMEE"), rs.getString("TABLE_ID"), table, rs
						.getBoolean("UNIQUEE"), rs.getInt("INDEX_ID")));
			}
		}

		// 根据tableId和indexId获取一个index的所需信息
		String sql2 = "select column_id from sys.index_columns where sys.index_columns.object_id = ? and sys.index_columns.index_id = ?";
		for (Index idx : list) {
			ps = source.prepareStatement(sql2);
			ps.setString(1, idx.table);
			ps.setInt(2, idx.indexId);
			rs = ps.executeQuery();
			while (rs.next()) {
				Long columnid = rs.getLong("column_id");
				String sql3 = " select name from sys.index_columns,sys.all_columns where sys.index_columns.object_id = ? and sys.all_columns.object_id = ? and  sys.index_columns.object_id = sys.all_columns.object_id and sys.index_columns.index_id = ? and sys.index_columns.column_id=? and sys.all_columns.column_id=?";
				PreparedStatement ps3 = source.prepareStatement(sql3);
				ps3.setString(1, idx.table);
				ps3.setString(2, idx.table);
				ps3.setLong(3, idx.indexId);
				ps3.setLong(4, columnid);
				ps3.setLong(5, columnid);
				ResultSet rs3 = ps3.executeQuery();
				rs3.next();
				idx.columns.add(rs3.getString("name"));
				rs3.close();
			}
		}
		rs.close();
		return list;
	}

	public static List<Index> fetchIndexesFromSqlServer() {
		logger.info("fetchIndexesFromSqlServer");
		List<Index> indexes = new LinkedList<>();
		List<Table> tables = TableTransfer.fetchTableFromSqlServer();
		for (Table table : tables) {
			indexes.addAll(fetchIndexesFromSqlServer4Table(table.schema, table.name));
		}
		logger.info("INDEXES:" + indexes);
		return chaosIndex(indexes);
	}

	private static List<Index> chaosIndex(List<Index> indexes) {
		Set<Index> indexSet = new HashSet<>();
		List<Index> returnList = new LinkedList<Index>();
		for (Index index : indexes) {
			if (indexSet.contains(index)) {
				index.name = index.tableName + "_" + index.name;
			}
			indexSet.add(index);
		}
		returnList.clear();
		returnList.addAll(indexSet);
		return returnList;
	}

	/**
	 * 查询一个表的所有Index
	 * 
	 * @param source
	 * @param tableSchema
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static List<Index> fetchIndexesFromSqlServer4Table(String tableSchema, String tableName) {
		logger.info("fetchIndexesFromSqlServer4Table");
		List<Index> list = new LinkedList<>();
		String sql = "select c.is_unique,c.name,a.TABLE_SCHEMA,e.COLUMN_NAME,c.type from INFORMATION_SCHEMA.TABLES a,sys.all_objects b,sys.indexes c,sys.index_columns d,INFORMATION_SCHEMA.COLUMNS e,sys.schemas F where a.TABLE_NAME=b.name and c.object_id=b.object_id and c.type<>0 and c.object_id=d.object_id and c.index_id=d.index_id and e.TABLE_NAME=a.TABLE_NAME and e.ORDINAL_POSITION=d.column_id AND B.schema_id=F.schema_id AND C.is_primary_key=0 AND C.is_unique_constraint=0 AND a.TABLE_SCHEMA=? AND b.name = ?";
		logger.info(sql);
		// 这张表的所有的Index Name和Index Schema
		Map<String, String> indexes;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			ps = conn.prepareStatement(sql);
			ps.setString(1, tableSchema);
			ps.setString(2, tableName);
			rs = ps.executeQuery();
			indexes = new HashMap<>();
			while (rs.next()) {
				indexes.put(rs.getString("name"), rs.getString("TABLE_SCHEMA"));
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
		for (String indexName : indexes.keySet()) {
			list.add(fetchIndexesFromSqlServer(indexes.get(indexName), tableName, indexName));
		}
		logger.info("INDEXES IN [" + tableSchema.toUpperCase() + "." + tableName.toUpperCase() + "]:" + list);
		return list;
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
		logger.info("fetchIndexesFromSqlServer");
		Index index = null;
		String sql = "select c.is_unique,c.name,a.TABLE_SCHEMA,a.TABLE_NAME,e.COLUMN_NAME,c.type,d.is_descending_key from INFORMATION_SCHEMA.TABLES a,sys.all_objects b,sys.indexes c,sys.index_columns d,INFORMATION_SCHEMA.COLUMNS e,sys.schemas F where a.TABLE_NAME=b.name and c.object_id=b.object_id and c.type<>0 and c.object_id=d.object_id and c.index_id=d.index_id and e.TABLE_NAME=a.TABLE_NAME and e.ORDINAL_POSITION=d.column_id AND B.schema_id=F.schema_id AND C.is_primary_key=0 AND C.is_unique_constraint=0 AND a.TABLE_SCHEMA=? AND a.TABLE_NAME=? AND c.NAME=?";
		logger.info(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
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
				isUnique = rs.getBoolean("is_unique");
				indexName = rs.getString("name");
				tableSchemat = rs.getString("TABLE_SCHEMA");
				tableName = rs.getString("TABLE_NAME");
				columns.add(rs.getString("COLUMN_NAME"));
				isDescendingKeys.add(rs.getBoolean("is_descending_key"));
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

}
