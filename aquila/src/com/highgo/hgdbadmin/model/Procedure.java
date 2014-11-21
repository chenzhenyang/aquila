package com.highgo.hgdbadmin.model;

public class Procedure {
	public String name;
	public String definition;
	
	public Procedure(String name,String definition) {
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
