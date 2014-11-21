package com.highgo.hgdbadmin.model;

import java.util.HashSet;
import java.util.Set;

public class Schema {
	public String name;
	public String owner;

	public Schema(String name, String owner) {
		super();
		this.name = name;
		this.owner = owner;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public String toSql() {
		return "CREATE SCHEMA " + this.name.toUpperCase();
	}

	@Override
	public boolean equals(Object obj) {
		return this.name.equals(((Schema) obj).name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public static void main(String[] args) {
		Set<Schema> set = new HashSet<>();
		set.add(new Schema("1", null));
		set.add(new Schema("1", null));
		System.out.println(set.size());
	}

}
