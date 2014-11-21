package com.highgo.hgdbadmin.model;

import java.util.ArrayList;
import java.util.List;

public class Index {
	public String name;//
	public String schema;//
	@Deprecated
	public String table;
	public String tableName;//
	public boolean unique;//
	@Deprecated
	public int indexId;
	public List<String> columns;//
	public List<Boolean> isDescendingKeys;

	public Index(String name, String schema, String tableName, boolean unique, List<String> columns,
			List<Boolean> isDescendingKeys) {
		super();
		this.name = name;
		this.schema = schema;
		this.tableName = tableName;
		this.unique = unique;
		this.columns = columns;
		this.isDescendingKeys = isDescendingKeys;
	}

	public Index(String name, String schema, String table, String tableName, boolean unique, int indexId) {
		super();
		this.name = name;
		this.schema = schema;
		this.table = table;
		this.tableName = tableName;
		this.unique = unique;
		this.indexId = indexId;
		this.columns = new ArrayList<>();
	}

	public Index() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return this.schema + "." + this.name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return this.name.equals(((Index) obj).name);
	}

	/**
	 * create
	 * 
	 * @return
	 */
	public String toSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE ");
		if (unique) {
			sb.append("UNIQUE ");
		}
		sb.append("INDEX " + this.name + " ON " + this.schema + "." + this.tableName + "(");

		if (this.columns.size() > 1) {
			for (int i = 0; i < columns.size() - 1; i++) {
				sb.append(columns.get(i) + " " + (isDescendingKeys.get(i) == false ? "ASC" : "DESC") + ",");
			}
		}
		
		sb.append(columns.get(columns.size() - 1) + " "
				+ (isDescendingKeys.get(isDescendingKeys.size() - 1) == false ? "ASC" : "DESC"));

		sb.append(")");
		return sb.toString();
	}
}