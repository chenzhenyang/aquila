package com.highgo.hgdbadmin.model;

import java.util.LinkedList;
import java.util.List;

public class ConstraintUK {
	public String tSchema;
	public String tName;
	public String cSchema;// 这个先写上，不过貌似都是跟随table的schema
	public String cName;
	public List<String> columns;

	
	
	public ConstraintUK(String tSchema, String tName,  String cName) {
		super();
		this.tSchema = tSchema;
		this.tName = tName;
		this.cSchema = null;
		this.cName = cName;
		this.columns = null;
	}
	
	public ConstraintUK(String tSchema, String tName, String cSchema, String cName, List<String> columns) {
		super();
		this.tSchema = tSchema;
		this.tName = tName;
		this.cSchema = cSchema;
		this.cName = cName;
		this.columns = columns;
	}

	@Override
	public String toString() {
		return this.tSchema + "." + this.cSchema;
	}

	// alter table tschema.ttable add Constraint constrname Unique(col1,col2);
	public String toSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("ALTER TABLE ");
		sb.append(this.tSchema + "." + this.tName);
		sb.append(" ADD CONSTRAINT " + this.cName);
		sb.append(" Unique(");
		for (int i = 0; i < columns.size() - 1; i++) {
			sb.append(columns.get(i) + ",");
		}
		sb.append(columns.get(columns.size() - 1) + ")");
		return sb.toString();
	}
}
