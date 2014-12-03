package com.highgo.hgdbadmin.reportable;

import java.util.List;

public class ViewReportTable {
	public String toHTML() {
		List<ViewDetail> vds = ReportInstance.report.views;
		StringBuilder sb = new StringBuilder();
		sb.append("<A class='awr' NAME='views'></A>");
		sb.append("<h2>Views<h2>");
		for (ViewDetail vd : vds) {
			sb.append("<A class='awr' NAME='"+vd.baseInfo.objectName+"'></A>");
			sb.append("<h3>"+vd.baseInfo.objectName+"</h3>");
			sb.append("<TABLE BORDER=1>");
//			sb.append("<caption><h3>"+""+"</h3></caption>");
			sb.append("<TR>");
			sb.append("<TD width='72' class='awrc'>Before</TD>");
			sb.append("<TD width='500' ALIGN='left' class='awrc'>" + vd.source.definition + "</TD>");
			sb.append("</TR>");

			sb.append("<TR>");
			sb.append("<TD class='awrc'>After</TD>");
			sb.append("<TD width='500' ALIGN='left' class='awrc'>" + vd.dest.definition + "</TD>");
			sb.append("</TR>");
			sb.append("</TABLE>");
		}
		sb.append("<A class='awr' HREF='#top'>Back to Top</A><P>");
		return sb.toString();
	}
}
