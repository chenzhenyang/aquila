package com.highgo.hgdbadmin.reportable;

import java.util.List;

public class IndexReportTable {
	public String toHTML() {
		List<IndexDetail> ids = ReportInstance.report.indexes;
		StringBuilder sb = new StringBuilder();
		sb.append("<A class='awr' NAME='indexes'></A>");
		sb.append("<h2>Index</h2>");
		for (IndexDetail id : ids) {
			sb.append("<A class='awr' NAME='"+id.baseInfo.objectName+"'></A>");
			sb.append("<h3>" + id.baseInfo.objectName + "</h3>");
			sb.append("<TABLE BORDER=1>");
			// sb.append("<caption>" + id.baseInfo.objectName + "</caption>");
			sb.append("<TR>");
			sb.append("<TH width='72' ALIGN='center' class='awrbg'>schema</TH>");
			sb.append("<TH width='72' ALIGN='center' class='awrbg'>name</TH>");
			sb.append("<TH width='72' ALIGN='center' class='awrbg'>on table</TH>");
			sb.append("<TH width='72' ALIGN='center' class='awrbg'>unique</TH>");
			sb.append("<TH ALIGN='center' class='awrbg'>columns</TH>");
			sb.append("<TH width='72' ALIGN='center' class='awrbg'>isDescendingKeys</TH>");
			sb.append("</TR>");

			sb.append("<TR>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + id.source.schema + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + id.source.name + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + id.source.tableName + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + id.source.unique + "</TD>");
			sb.append("<TD ALIGN='center' class='awrc'>" + id.source.columns + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + id.source.isDescendingKeys + "</TD>");
			sb.append("</TR>");

			sb.append("<TR>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + id.dest.schema + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + id.dest.name + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + id.dest.tableName + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + id.dest.unique + "</TD>");
			sb.append("<TD ALIGN='center' class='awrc'>" + id.dest.columns + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + id.dest.isDescendingKeys + "</TD>");
			sb.append("</TR>");
			sb.append("</TABLE>");
		}
		sb.append("<A class='awr' HREF='#top'>Back to Top</A><P>");
		return sb.toString();
	}

}
