package com.highgo.hgdbadmin.reportable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.highgo.hgdbadmin.myutil.C3P0Util;

public class EnvironmentReportTable {

	private static EnvironmentReportTable environment;

	public String[] database = new String[2];
	public String[] username = new String[2];
	// public String[] schema = new String[2];
	public String[] dbtype = new String[2];
	public String[] release = new String[2];
	public String[] host = new String[2];

	private EnvironmentReportTable() throws SQLException {
		Connection conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
		database[0] = conn.getCatalog();
		username[0] = conn.getMetaData().getUserName();
		// schema[1] = ;
		dbtype[0] = conn.getMetaData().getDatabaseProductName();
		release[0] = conn.getMetaData().getDatabaseProductVersion();
		host[0] = getHost(conn.getMetaData().getURL());

		Connection conn2 = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
		database[1] = conn2.getCatalog();
		username[1] = conn2.getMetaData().getUserName();
		// schema[1] = ;
		dbtype[1] = conn2.getMetaData().getDatabaseProductName();
		release[1] = conn2.getMetaData().getDatabaseProductVersion();
		host[1] = getHost(conn2.getMetaData().getURL());
	}

	public static synchronized  EnvironmentReportTable getInstance() {
		if (environment == null) {
			try {
				environment = new EnvironmentReportTable();
			} catch (SQLException e) {
			}
		}
		return environment;
	}

	public String toHTML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<div>");
		sb.append("<h2>Environment</h2>");
		sb.append("<table border=1 WIDTH=540>");

//		sb.append("<caption><h2>" + "Environment" + "<h2></caption>");

		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th  class='awrbg'>Database</th>");
		sb.append("<th  class='awrbg'>Username</th>");
		// sb.append("<th  class='awrbg'>Schema</th>");
		sb.append("<th  class='awrbg'>DBType</th>");
		sb.append("<th  class='awrbg'>Release</th>");
		sb.append("<th  class='awrbg'>Host</th>");
		sb.append("</tr>");
		sb.append("</thead>");

		sb.append("<tbody>");
		for (int i = 0; i < 2; i++) {
			sb.append("<tr>");
			sb.append("<td align='center' class='awrc'>" + database[i] + "</td>");
			sb.append("<td align='center' class='awrc'>" + username[i] + "</td>");
			// sb.append("<td align='center' class='awrnc'>" + schema[i] +
			// "</td>");
			sb.append("<td align='center' class='awrc'>" + dbtype[i] + "</td>");
			sb.append("<td align='center' class='awrc'>" + release[i] + "</td>");
			sb.append("<td align='center' class='awrc'>" + host[i] + "</td>");
			sb.append("</tr>");
		}
		sb.append("</tbody>");

		sb.append("</table>");
		sb.append("</div>");

		return sb.toString();
	}

	public static String getHost(String url) {
		String host = "";
		Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
		Matcher matcher = p.matcher(url);
		if (matcher.find()) {
			host = matcher.group();
		}
		return host;
	}
}
