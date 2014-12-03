package com.highgo.hgdbadmin.reportable;

import java.util.List;

import com.highgo.hgdbadmin.myutil.Constants;

public class FailedObjectSummary {
	public String toHTML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<h2>Failed Ojbects Summary<h2>");
		sb.append("<TABLE BORDER=1 WIDTH=461>");
//		sb.append("<caption><h3>Failed Ojbects Summary<h3></caption>");
		sb.append("<TR>");
		sb.append("<TH width='158' class='awrbg'>Object Name</TH>");
		sb.append("<TH width='132' class='awrbg'>Object Type</TH>");
		sb.append("<TH width='149' class='awrbg'>Cause</TH>");
		sb.append("</TR>");

		for (String type : ReportInstance.report.objectTables.keySet()) {
			ObjectTable ot = ReportInstance.getObjectTable(type);
			List<ObjectInfo> ois = ot.rows;
			for (ObjectInfo oi : ois) {
				if (!oi.isSuccessed) {
					sb.append("<TR>");
//					String herf = null;
//					if(type.equals(Constant.TABLE)){
//						herf = oi.objectName;
//					}
					sb.append("<TD ALIGN='left' class='awrc'><a href='#"+oi.objectName+"'>" + oi.objectName + "</a></TD>");
					sb.append("<TD ALIGN='center' class='awrc'>" + type + "</TD>");
					sb.append("<TD align='left' class='awrc'>"+oi.causes+"</TD>");
					sb.append("</TR>");
				}
			}
		}
		sb.append("</TABLE>");
		return sb.toString();
	}
}
