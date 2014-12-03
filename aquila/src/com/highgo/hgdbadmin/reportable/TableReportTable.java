package com.highgo.hgdbadmin.reportable;

import java.util.List;

import com.highgo.hgdbadmin.model.Column;
import com.highgo.hgdbadmin.myutil.DataTypeMap;

public class TableReportTable {
	public String toHTML() {
		List<TableDetail> tds = ReportInstance.report.tables;

		StringBuilder sb = new StringBuilder();
		sb.append("<A class='awr' NAME='tables'></A>");
		sb.append("<h2>Tables</h2>");
		for (TableDetail td : tds) {
			sb.append("<A class='awr' NAME='"+(td.source.schema + "." + td.source.name)+"'></A>");
			sb.append("<A class='awr' NAME='" + (td.source.schema + "." + td.source.name) + "'></A>");
			sb.append("<h2>" + (td.source.schema + "." + td.source.name) + "</h2></p>");
			sb.append("<h3>total tuples: " + td.totalRead + "  success:" + td.totalWrite + " failed:" + td.totalDerby
					+ "</h3>");

			sb.append("<TABLE BORDER=1>");
			sb.append("<caption>" + "" + "</caption>");
			sb.append("<TR>");
			sb.append("<TH width='169' class='awrbg'>Source Cloumn Name</TH>");
			sb.append("<TH width='76' class='awrbg'>Type</TH>");
			sb.append("<TH width='161' class='awrbg'>Target Cloumn Name</TH>");
			sb.append("<TH width='161' class='awrbg'>Type</TH>");
			sb.append("</TR>");

			List<Column> sourceColumns = td.source.columns;
			List<Column> destColumns = td.dest.columns;

			for (int i = 0; i < sourceColumns.size(); i++) {
				Column sc = sourceColumns.get(i);
				Column dc = destColumns.get(i);
				sb.append("<TR>");
				sb.append("<TD class='awrc'>" + sc.name + "</TD> ");

				String datatype1 = null;
				if (DataTypeMap.requireLength(sc.dataType)) {
					if (sc.character_maximun_length == -1) {
						datatype1 = sc.dataType + "(" + "max" + ")";
					} else {
						datatype1 = sc.dataType + "(" + sc.character_maximun_length + ")";
					}

				} else {
					datatype1 = sc.dataType;
				}

				sb.append("<TD ALIGN='right' class='awrc'>" + datatype1 + "</TD>");
				sb.append("<TD ALIGN='right' class='awrc'>" + dc.name + "</TD>");

				String datatype2 = null;
				if (DataTypeMap.requireLength(sc.dataType)) {
					if (dc.dataType.equals("text")) {
						datatype2 = dc.dataType;
					} else {
						datatype2 = dc.dataType + "(" + sc.character_maximun_length + ")";
					}
				} else {
					datatype2 = dc.dataType;
				}

				sb.append("<TD ALIGN='right' class='awrc'>" + datatype2 + "</TD>");
				sb.append("</TR>");
			}
			sb.append("</TABLE>");
		}
		sb.append("<A class='awr' HREF='#top'>Back to Top</A><P>");
		return sb.toString();
	}
}
