package com.highgo.hgdbadmin.model;

import com.highgo.hgdbadmin.myutil.DataTypeMap;

public class Column {
	public String name;
	public String difault;
	public boolean isNullable;
	public String dataType;
	public Integer character_maximun_length;
	public int numeric_precision;
	public int numeric_scale;

	public Column(String name, String difault, boolean isNullable, String dataType, int character_maximun_length,
			int numeric_precision, int numeric_scale) {
		super();
		this.name = name;
		this.difault = difault;
		this.isNullable = isNullable;
		this.dataType = dataType;
		this.character_maximun_length = character_maximun_length;
		if (this.dataType.equals("uniqueidentifier")) {
			this.character_maximun_length = 16;
		}
		this.numeric_precision = numeric_precision;
		this.numeric_scale = numeric_scale;
	}

	public String toSql() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);

		if (DataTypeMap.requireLength(this.dataType.toLowerCase()) && this.character_maximun_length == -1) {
			sb.append(" " + "text");
		} else {
			sb.append(" " + DataTypeMap.map.get(dataType));
			if (DataTypeMap.requireLength(this.dataType.toLowerCase())) {
				if (character_maximun_length != null) {
					sb.append("(" + character_maximun_length + ")");
				}
			}
		}

		/**
		 * 这个地方先不加，貌似pg里边很少指定字段的长度
		 */

		// 默认值先不处理
		/**
		 * if (difault != null && (!"".equals(difault))) { if
		 * (DataTypeMap.isNumberic(dataType)) { sb.append(" DEFAULT " +
		 * difault); } else { // [TestSequence] // Sequence 最后加上，不要在这加，这样加名字是随机的
		 * // if (difault.startsWith("(NEXT VALUE FOR")) { // int start =
		 * difault.indexOf('['); // int end = difault.indexOf(']'); // String
		 * sequenceName = difault.substring(start, end); //
		 * sb.append(" DEFAULT nextval('" + sequenceName + "'"); // }
		 * sb.append(" DEFAULT " + difault); } }
		 **/
		if (isNullable) {
			sb.append(" NOT NULL");
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return this.name + ":" + this.dataType;
	}

	public String toString2() {
		return "Column:" + this.name + " default:" + this.difault + " isNull:" + this.isNullable + " datatype:"
				+ this.dataType + " maxlength:" + this.character_maximun_length + " numeric_precision"
				+ this.numeric_precision + " numeric_scale:" + this.numeric_scale+"\n";
	}

	public static void main(String[] args) {
		Column co = new Column("field5", "0", false, "int", 214, 10, 10);
		System.out.println(co.toSql());
	}
}
