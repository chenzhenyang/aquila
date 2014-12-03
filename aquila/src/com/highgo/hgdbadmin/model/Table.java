package com.highgo.hgdbadmin.model;

import java.util.List;

public class Table {
	public String schema;
	public String name;
	public String type; // basic table or view

	public List<Column> columns;

	public List<String> keys;

	public Table(String schema, String name, String type) {
		super();
		this.schema = schema;
		this.name = name;
		this.type = type;
	}

	public String toSql() {
		StringBuilder sb = new StringBuilder();
		if (columns != null) {
			sb.append("CREATE TABLE " + this.schema + "." + this.name + " (");
			for (int i = 0; i < columns.size() - 1; i++) {
				sb.append(columns.get(i).toSql() + ",");
			}
			sb.append(columns.get(columns.size() - 1).toSql());
			// primary key,现在不用了，放到统一的约束迁移里边了
			// if(keys!=null&&keys.size()>0){
			// sb.append(", PRIMARY KEY(");
			// for(int j=0;j< keys.size()-1;j++){
			// sb.append(keys.get(j)+",");
			// }
			// sb.append(keys.get(keys.size()-1)+")");
			// }
			sb.append(")");
			return sb.toString();
		}
		return null;
	}

	@Override
	public String toString() {
		return this.schema + "." + this.name;
	}

	public String toString2() { 
		StringBuilder sb = new StringBuilder();
		sb.append("[" + this.schema + "." + this.name + "]");
		sb.append(" type:" + type + "\n");
		sb.append("columns:" + columns+"\n");
		sb.append("keys:" + this.keys);
		return sb.toString();
	}
}
