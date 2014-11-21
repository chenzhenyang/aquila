package com.highgo.hgdbadmin.model;

public class Trigger {
	public String name;
	public String definition;

	public Trigger(String name, String defi) {
		super();
		this.name = name;
		this.definition = defi;
	}

	public String toSql() {
		return this.definition;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
