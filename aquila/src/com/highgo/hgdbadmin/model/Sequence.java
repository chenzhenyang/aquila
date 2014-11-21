package com.highgo.hgdbadmin.model;

/**
 * Pg的Sequence貌似只支持整数，SqlServer支持所有的数字类型，还支持自定义类型，所以迁移过程中，只考虑整数的情况。
 * 
 * @author u
 *
 */
public class Sequence {
	public String schema;
	public String name;

	public long startValue;
	public long minimumValue;
	public long maximunValue;
	public int increment;
	public int cycle_option;

	public Sequence(String schema, String name, long startValue, long minimumValue, long maximunValue, int increment,
			int cycle_option) {
		super();
		this.schema = schema;
		this.name = name;
		this.startValue = startValue;
		this.minimumValue = minimumValue;
		this.maximunValue = maximunValue;
		this.increment = increment;
		this.cycle_option = cycle_option;
	}

	public String toSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE SEQUENCE " + this.schema + "." + this.name);
		sb.append(" INCREMENT BY " + this.increment);
		sb.append(" MINVALUE " + this.minimumValue);
		sb.append(" MAXVALUE " + this.maximunValue);
		sb.append(" START WITH " + this.startValue);
		if (this.cycle_option == 1) {
			sb.append(" CYCLE");
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return this.schema + "." + this.name;
	}

}
