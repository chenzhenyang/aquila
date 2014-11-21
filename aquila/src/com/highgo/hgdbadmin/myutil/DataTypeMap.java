package com.highgo.hgdbadmin.myutil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataTypeMap {
	public static Map<String, String> map = new HashMap<>();

	static {
		map.put("char", "char");
		map.put("nchar", "char");
		map.put("varchar", "varchar");
		map.put("nvarchar", "varchar");
		map.put("bit", "boolean");
		map.put("uniqueidentifier", "varchar");
		map.put("text", "text");
		map.put("ntext", "text");
		map.put("int", "int");
		map.put("bigint", "bigint");
		map.put("smallint", "smallint");
		map.put("tinyint", "smallint");
		map.put("real", "real");
		map.put("float", "double precision");
		map.put("money", "numeric");
		map.put("smallmoney", "numeric");
		map.put("numeric", "numeric");
		map.put("decimal", "decimal");
		map.put("binary", "bytea");
		map.put("varbinary", "bytea");
		map.put("image", "bytea");
		map.put("xml", "xml");
		map.put("timestamp", "bytea");
		map.put("date", "date");
		map.put("time", "time");
		map.put("datetime", "timestamp");
		map.put("datetime2", "timestamp");
		map.put("datetimeoffset", "timestamp");
		map.put("smalldatetime", "timestamp");
	}

	private static Set<String> numberic = new HashSet<>();
	static {
		numberic.add("int");
		numberic.add("bigint");
		numberic.add("smallint");
		numberic.add("tinyint");
		numberic.add("real");
		numberic.add("float");
		numberic.add("money");
		numberic.add("smallmoney");
		numberic.add("numeric");
		numberic.add("decimal");
	}

	public static boolean isNumberic(String datatype) {
		return numberic.contains(datatype) ? true : false;
	}

	public static boolean change(String dataTypeInSource, String dataTypeinDest) {
		map.put(dataTypeInSource, dataTypeinDest);
		return true;
	}

	public static String add(String dataTypeInSource, String dataTypeinDest) {
		return map.put(dataTypeInSource, dataTypeinDest);
	}

	public static String delete(String dataTypeInSource) {
		return map.remove(dataTypeInSource);
	}

	public static String get(String dataTypeInSource) {
		return map.get(dataTypeInSource);
	}

	public static boolean requireLength(String dataType) {
		if (dataType.equals("char") || dataType.equals("nchar") || dataType.equals("varchar")
				|| dataType.equals("nvarchar")||dataType.equals("uniqueidentifier")) {
			return true;
		}
		return false;
	}

	public static String getAllDataTypeMap() {
		int max = maxLength();
		int colon = max + 5;
		StringBuilder sb = new StringBuilder();
		int j = 0;
		for (String str : map.keySet()) {
			j++;
			StringBuilder line = new StringBuilder();
			line.append(str);
			for (int i = 0; i < colon - str.length(); i++) {
				line.append(" ");
			}
			line.append(":");
			for (int i = 0; i < 8; i++) {
				line.append(" ");
			}
			line.append(map.get(str) + "\n");
			if (j % 4 == 0) {
				sb.append("\n");
				sb.append("\n");
			}
			sb.append(line);
		}
		return sb.toString();
	}

	private static int maxLength() {
		int max = 0;
		for (String str : map.keySet()) {
			max = maxInThree(max, str.length(), map.get(str).length());
		}
		return max;
	}

	private static int maxInThree(int c1, int c2, int c3) {
		return Math.max(c1, Math.max(c2, c3));
	}
}
