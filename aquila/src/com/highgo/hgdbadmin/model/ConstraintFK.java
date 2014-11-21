package com.highgo.hgdbadmin.model;

import java.util.List;

public class ConstraintFK {
	public String tSchema;
	public String tName;
	public String cSchema;// 这个先写上，不过貌似都是跟随table的schema
	public String cName;
	public List<String> columns;
	public String matchOption;
	public String updateRule;
	public String deleteRule;
	public ConstraintPK primaryKey;

	
	public ConstraintFK(String tSchema, String tName, String cName, List<String> columns,
			String matchOption, String updateRule, String deleteRule, ConstraintPK primaryKey) {
		super();
		this.tSchema = tSchema;
		this.tName = tName;
		this.cSchema = null;
		this.cName = cName;
		this.columns = columns;
		this.matchOption = matchOption;
		this.updateRule = updateRule;
		this.deleteRule = deleteRule;
		this.primaryKey = primaryKey;
	}
	
	public ConstraintFK(String tSchema, String tName, String cSchema, String cName, List<String> columns,
			String matchOption, String updateRule, String deleteRule, ConstraintPK primaryKey) {
		super();
		this.tSchema = tSchema;
		this.tName = tName;
		this.cSchema = cSchema;
		this.cName = cName;
		this.columns = columns;
		this.matchOption = matchOption;
		this.updateRule = updateRule;
		this.deleteRule = deleteRule;
		this.primaryKey = primaryKey;
	}

	@Override
	public String toString() {
		return this.tSchema + "." + this.cSchema;
	}

	// alter table tschema.ttable add constraint constrname foreign key
	// (col1,col2) references ttable(col1,col2) match match_option
	// on delete cascade
	// on update cascade
	public String toSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("ALTER TABLE ");
		sb.append(this.tSchema + "." + this.tName);
		sb.append(" ADD CONSTRAINT " + this.cName);
		sb.append(" FOREIGN  KEY(");
		for (int i = 0; i < columns.size() - 1; i++) {
			sb.append(columns.get(i) + ",");
		}
		sb.append(columns.get(columns.size() - 1) + ")");
		sb.append(" REFERENCES " + this.primaryKey.tSchema + "." + this.primaryKey.tName);
		sb.append(" (");
		for (int i = 0; i < this.primaryKey.columns.size() - 1; i++) {
			sb.append(this.primaryKey.columns.get(i) + ",");
		}
		sb.append(this.primaryKey.columns.get(this.primaryKey.columns.size() - 1) + ")");
		sb.append(" match " + this.matchOption);
		if ("CASCADE".equals(this.updateRule)) {
			sb.append(" ON UPDATE CASCADE");
		}
		if ("CASCADE".equals(this.deleteRule)) {
			sb.append(" ON DELETE CASCADE");
		}
		return sb.toString();
	}
}
