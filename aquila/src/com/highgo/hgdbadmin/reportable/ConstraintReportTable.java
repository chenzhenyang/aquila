package com.highgo.hgdbadmin.reportable;

import java.util.List;

public class ConstraintReportTable {

	public String toHTML() {
		List<CheckKeyDetail> ckds = ReportInstance.report.checkKeys;
		List<UniqueKeyDetail> ukds = ReportInstance.report.uniqueKey;
		List<PrimaryKeyDetail> pkds = ReportInstance.report.primaryKeys;
		List<ForeignKeyDetail> fkds = ReportInstance.report.foreignKeys;

		StringBuilder sb = new StringBuilder();
		sb.append("<A class='awr' NAME='constraints'></A>");
		sb.append("<h2>Constraint</h2>");
		sb.append(toHTML1(ckds));
		sb.append(toHTML2(ukds));
		sb.append(toHTML3(pkds));
		sb.append(toHTML4(fkds));
		sb.append("<A class='awr' HREF='#top'>Back to Top</A><P>");
		return sb.toString();
	}

	private String toHTML1(List<CheckKeyDetail> ckds) {
		StringBuilder sb = new StringBuilder();
		for (CheckKeyDetail ckd : ckds) {
			sb.append("<A class='awr' NAME='"+ckd.baseInfo.objectName+"'></A>");
			sb.append("<h3>" + ckd.baseInfo.objectName + "</h3>");
			sb.append("<TABLE BORDER=1>");
			// sb.append("<caption>" + ckd.baseInfo.objectName + "</caption>");
			sb.append("<TR>");
			sb.append("<TH ALIGN='center' class='awrbg'>Constraint Schema</TH>");
			sb.append("<TH ALIGN='center' class='awrbg'>Constraint Name</TH>");
			sb.append("<TH ALIGN='center' class='awrbg'>On Table</TH>");
			sb.append("<TH ALIGN='center' class='awrbg'>Clause</TH>");
			sb.append("</TR>");

			sb.append("<TR>");
			sb.append("<TD ALIGN='center' class='awrc'>" + ckd.source.tSchema + "</TD>");
			sb.append("<TD ALIGN='center' class='awrc'>" + ckd.source.cName + "</TD>");
			sb.append("<TD ALIGN='center' class='awrc'>" + ckd.source.tName + "</TD>");
			sb.append("<TD ALIGN='center' class='awrc'>" + ckd.source.clause + "</TD>");
			sb.append("</TR>");

			sb.append("<TR>");
			sb.append("<TD ALIGN='center' class='awrc'>" + ckd.dest.tSchema + "</TD>");
			sb.append("<TD ALIGN='center' class='awrc'>" + ckd.dest.cName + "</TD>");
			sb.append("<TD ALIGN='center' class='awrc'>" + ckd.dest.tName + "</TD>");
			sb.append("<TD ALIGN='center' class='awrc'>" + ckd.dest.clause + "</TD>");
			sb.append("</TR>");

			sb.append("</TABLE>");
		}
		return sb.toString();
	}

	private String toHTML2(List<UniqueKeyDetail> ukds) {
		StringBuilder sb = new StringBuilder();
		for (UniqueKeyDetail ukd : ukds) {
			sb.append("<A class='awr' NAME='"+ukd.baseInfo.objectName+"'></A>");
			sb.append("<h3>" + ukd.baseInfo.objectName + "</h3>");
			sb.append("<TABLE BORDER=1>");
			// sb.append("<caption>" + ukd.baseInfo.objectName + "</caption>");
			sb.append("<TR>");
			sb.append("<TH  ALIGN='center' class='awrbg'>Constraint Schema</TH>");
			sb.append("<TH  ALIGN='center' class='awrbg'>Constraint Name</TH>");
			sb.append("<TH  ALIGN='center' class='awrbg'>On Table</TH>");
			sb.append("<TH  ALIGN='center' class='awrbg'>Columns</TH>");
			sb.append("</TR>");

			sb.append("<TR>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + ukd.source.tSchema + "</TD>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + ukd.source.cName + "</TD>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + ukd.source.tName + "</TD>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + ukd.source.columns + "</TD>");
			sb.append("</TR>");

			sb.append("<TR>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + ukd.dest.tSchema + "</TD>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + ukd.dest.cName + "</TD>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + ukd.dest.tName + "</TD>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + ukd.dest.columns + "</TD>");
			sb.append("</TR>");

			sb.append("</TABLE>");
		}
		return sb.toString();
	}

	private String toHTML3(List<PrimaryKeyDetail> pkds) {
		StringBuilder sb = new StringBuilder();
		for (PrimaryKeyDetail pkd : pkds) {
			sb.append("<A class='awr' NAME='"+pkd.baseInfo.objectName+"'></A>");
			sb.append("<h3>" + pkd.baseInfo.objectName + "</h3>");
			sb.append("<TABLE BORDER=1>");
			// sb.append("<caption>" + pkd.baseInfo.objectName + "</caption>");
			sb.append("<TR>");
			sb.append("<TH  ALIGN='center' class='awrbg'>Constraint Schema</TH>");
			sb.append("<TH  ALIGN='center' class='awrbg'>Constraint Name</TH>");
			sb.append("<TH  ALIGN='center' class='awrbg'>On Table</TH>");
			sb.append("<TH  ALIGN='center' class='awrbg'>Columns</TH>");
			sb.append("</TR>");

			sb.append("<TR>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + pkd.source.tSchema + "</TD>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + pkd.source.cName + "</TD>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + pkd.source.tName + "</TD>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + pkd.source.columns + "</TD>");
			sb.append("</TR>");

			sb.append("<TR>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + pkd.dest.tSchema + "</TD>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + pkd.dest.cName + "</TD>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + pkd.dest.tName + "</TD>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + pkd.dest.columns + "</TD>");
			sb.append("</TR>");

			sb.append("</TABLE>");
		}
		return sb.toString();
	}

	private String toHTML4(List<ForeignKeyDetail> fkds) {
		StringBuilder sb = new StringBuilder();
		for (ForeignKeyDetail fkd : fkds) {
			sb.append("<A class='awr' NAME='"+fkd.baseInfo.objectName+"'></A>");
			sb.append("<h3>" + fkd.baseInfo.objectName + "</h3>");
			sb.append("<TABLE BORDER=1>");
			// sb.append("<caption>" + fkd.baseInfo.objectName + "</caption>");
			sb.append("<TR>");
			sb.append("<TH  ALIGN='center' class='awrbg'>Constraint Schema</TH>");
			sb.append("<TH  ALIGN='center' class='awrbg'>Constraint Name</TH>");
			sb.append("<TH  ALIGN='center' class='awrbg'>On Table</TH>");
			sb.append("<TH  ALIGN='center' class='awrbg'>Columns</TH>");
			sb.append("</TR>");

			sb.append("<TR>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + fkd.source.tSchema + "</TD>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + fkd.source.cName + "</TD>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + fkd.source.tName + "</TD>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + fkd.source.columns + "</TD>");
			sb.append("</TR>");

			sb.append("<TR>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + fkd.dest.tSchema + "</TD>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + fkd.dest.cName + "</TD>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + fkd.dest.tName + "</TD>");
			sb.append("<TD  ALIGN='center' class='awrc'>" + fkd.dest.columns + "</TD>");
			sb.append("</TR>");

			sb.append("</TABLE>");
		}
		return sb.toString();
	}
}
