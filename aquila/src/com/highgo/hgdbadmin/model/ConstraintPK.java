package com.highgo.hgdbadmin.model;

import java.util.List;

public class ConstraintPK {
	public String tSchema; // table的Schema和name
	public String tName;
	public String cSchema;// 约束的schema和name，这个先写上，不过貌似都是跟随table的schema
	public String cName;
	public List<String> columns;

	
	public ConstraintPK() {
	}
	
	public ConstraintPK(String tSchema, String tName, String cName, List<String> columns) {
		super();
		this.tSchema = tSchema;
		this.tName = tName;
		this.cSchema = tSchema;
		this.cName = cName;
		this.columns = columns;
	}

	public ConstraintPK(String tSchema, String tName, String cSchema, String cName, List<String> columns) {
		super();
		this.tSchema = tSchema;
		this.tName = tName;
		this.cSchema = tSchema;
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
		sb.append(" PRIMARY KEY (");
		for (int i = 0; i < columns.size() - 1; i++) {
			sb.append(columns.get(i) + ",");
		}
		sb.append(columns.get(columns.size() - 1) + ")");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return (cSchema + cName).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		ConstraintPK pk = (ConstraintPK) obj;
		if (cSchema.equals(pk.cSchema) && cName.equals(pk.cName)) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		ConstraintPK pk1 = new ConstraintPK();
		pk1.cSchema="db1o";
		pk1.cName="c1";
		
		ConstraintPK pk2 = new ConstraintPK();
		pk2.cSchema="dbo";
		pk2.cName="c1";
		
		System.out.println(pk1.equals(pk2));
		
	}
	
}
