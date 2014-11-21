package com.highgo.hgdbadmin.myutil;

import java.sql.SQLException;
import java.util.List;

import com.highgo.hgdbadmin.model.Index;

public class Test {
	public static void main(String[] args) throws SQLException {
//		List<Schema> schemas = MigrateCenter.fetchSchemasFromSqlServer();
//		for (Schema schema : schemas) {
//			System.out.println(schema.toSql());
//			MigrateCenter.createSchema(schema);
//		}
//
//		List<Table> tables = MigrateCenter.fetchTableFromSqlServer();
//		System.out.println("tables size:" + tables.size());
//		for (Table table : tables) {
//			MigrateCenter.createTable(table);
//		}
//
//		List<View> views = MigrateCenter.fetchViewsFromSqlServer();
//		System.out.println("views size:" + views.size());
//		for (View viewv : views) {
//			MigrateCenter.createView(viewv);
//		}
//
		List<Index> indexes = MigrateCenter.fetchIndexesFromSqlServer();
		System.out.println("indexes size:"+indexes.size());
		for (Index index : indexes) {
			System.out.println(index.toSql());
			MigrateCenter.createIndex(index.schema,index.tableName,index.name);
		}
//		
//		Index index = MigrateCenter.fetchIndexesFromSqlServer("dbo","SiteInfo","Index_1");
//		System.out.println(index.toSql());
		
//		Index index = MigrateCenter.fetchIndexesFromSqlServer("dbo","SITEMONITOREXTURLINFO","Index_1");
//		System.out.println(index);
//
//		List<ConstraintPK> pks = MigrateConstraint.fetchConstraintPKFromSqlServer();
//		System.out.println("pks size:"+pks.size());
//		for (ConstraintPK pk : pks) {
//			MigrateConstraint.createConstraintPK(pk);
//		}
		
		
//		System.out.println(testError());
//		System.out.println(testError());
		
//		List<ConstraintCK> cks = MigrateConstraint.fetchConstraintCKFromSqlServer();
//		System.out.println(cks.size());
		
//		
//		Connection conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
//		System.out.println(conn);
		
	}
	
	
	public static boolean testError() {
		String str = "t";
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
