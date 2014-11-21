package com.highgo.hgdbadmin.model;

public class View {
	public String schema;
	public String name;
	public String definition;

	public View(String schema, String name, String definition) {
		super();
		this.schema = schema;
		this.name = name;
		this.definition = definition;
	}
	
	
	
	public String toSql() {
//		this.definition=this.definition.replace(arg0, arg1)
		return this.definition;
	}
	
	@Override
	public String toString() {
		return this.schema+"."+this.name;
	}
}
