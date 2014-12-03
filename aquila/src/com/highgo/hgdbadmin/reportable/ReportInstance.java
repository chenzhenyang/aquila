package com.highgo.hgdbadmin.reportable;

public class ReportInstance {
	public static Report report = new Report();

	public static ObjectTable getObjectTable(String type) {
		return report.objectTables.get(type);
	}
}
