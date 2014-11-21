package com.highgo.hgdbadmin.model;

public class Function {
	public String name;
	public String definition;
	
	public Function(String name, String definition) {
		super();
		this.name = name;
		this.definition = definition;
	}
	
	public String toSql() {
		return definition;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
