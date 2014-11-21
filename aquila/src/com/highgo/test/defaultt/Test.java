package com.highgo.test.defaultt;


public class Test {
	public static void main(String[] args) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select c.is_unique,c.name,a.TABLE_SCHEMA,a.TABLE_NAME,e.COLUMN_NAME,c.type ")
				.append(" from INFORMATION_SCHEMA.TABLES a,sys.all_objects b,sys.indexes c,")
				.append(" sys.index_columns d,INFORMATION_SCHEMA.COLUMNS e,sys.schemas F ")
				.append(" where a.TABLE_NAME=b.name and c.object_id=b.object_id and c.type<>0 ")
				.append(" and c.object_id=d.object_id and c.index_id=d.index_id ")
				.append(" and e.TABLE_NAME=a.TABLE_NAME and e.ORDINAL_POSITION=d.column_id ")
				.append(" AND B.schema_id=F.schema_id  ")
				.append(" AND C.is_primary_key=0 AND C.is_unique_constraint=0")
				.append(" AND a.TABLE_SCHEMA=? AND c.NAME=?");
		System.out.println(sql);
	}
	
}
