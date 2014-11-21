package com.highgo.hgdbadmin.model;

public class ConstraintCK {
	public String tSchema;
	public String tName;
	public String cSchema;// 这个先写上，不过貌似都是跟随table的schema
	public String cName;
	public String clause;

	public ConstraintCK(String tSchema, String tTableName, String cName, String clause) {
		super();
		this.tSchema = tSchema;
		this.tName = tTableName;
		this.cSchema = null;
		this.cName = cName;
		this.clause = clause;
	}

	public ConstraintCK(String tSchema, String tTableName, String cSchema, String cName, String clause) {
		super();
		this.tSchema = tSchema;
		this.tName = tTableName;
		this.cSchema = cSchema;
		this.cName = cName;
		this.clause = clause;
	}

	@Override
	public String toString() {
		return this.tSchema + "." + this.cSchema;
	}

	// alter table tschema.ttable add constraint consname check (check-clause)
	public String toSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("ALTER TABLE ");
		sb.append(this.tSchema + "." + this.tName);
		sb.append(" ADD CONSTRAINT " + this.cName);
		String clauset = this.clause.replace('[', ' ').replace(']', ' ');
		sb.append(" CHECK" + clauset);
		return sb.toString();
	}
}
