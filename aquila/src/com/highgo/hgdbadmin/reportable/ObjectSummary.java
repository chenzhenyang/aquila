package com.highgo.hgdbadmin.reportable;

import com.highgo.hgdbadmin.myutil.Constants;

public class ObjectSummary {
	public String toHTML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<h2>Object Numbers</h2>");
		sb.append("<TABLE BORDER=1 WIDTH=562>");
//		sb.append("<caption><h3>Object Numbers</h3></caption>");
		sb.append("<TR>");
		sb.append("<TH width='114' class='awrbg'>Object Type</TH>");
		sb.append("<TH width='103' class='awrbg'>Total numbers</TH>");
		sb.append("<TH width='113' class='awrbg'>Success numbers</TH>");
		sb.append("<TH width='101' class='awrbg'>Failed numbers</TH>");
		sb.append("<TH width='97' class='awrbg'>No Migrate</TH></TR>");
		sb.append("<TR>");

		ObjectTable tableTable = ReportInstance.report.objectTables.get(Constant.TABLE);
		sb.append("<TR>");
		sb.append("<TD class='awrc'>" + Constant.TABLE.toUpperCase() + "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>" + Constants.TABLES.size() + "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>" + tableTable.numSuccessed + "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>" + tableTable.numFailed + "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>" + (Constants.TABLES.size() - tableTable.sum) + "</TD>");
		sb.append("</TR>");

		ObjectTable viewTable = ReportInstance.report.objectTables.get(Constant.VIEW);
		sb.append("<TR>");
		sb.append("<TD class='awrc'>" + Constant.VIEW.toUpperCase() + "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>" + Constants.VIEWS.size() + "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>" + viewTable.numSuccessed + "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>" + viewTable.numFailed + "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>" + (Constants.VIEWS.size() - viewTable.sum) + "</TD>");
		sb.append("</TR>");

		ObjectTable indexTable = ReportInstance.report.objectTables.get(Constant.INDEX);
		sb.append("<TR>");
		sb.append("<TD class='awrc'>" + Constant.INDEX.toUpperCase() + "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>" + Constants.INDEXES.size() + "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>" + indexTable.numSuccessed + "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>" + indexTable.numFailed + "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>" + (Constants.INDEXES.size() - indexTable.sum) + "</TD>");
		sb.append("</TR>");

		ObjectTable sequenceTable = ReportInstance.report.objectTables.get(Constant.SEQUENCE);
		sb.append("<TR>");
		sb.append("<TD class='awrc'>" + Constant.SEQUENCE.toUpperCase() + "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>" + Constants.SEQUENCES.size() + "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>" + sequenceTable.numSuccessed + "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>" + sequenceTable.numFailed + "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>" + (Constants.SEQUENCES.size() - sequenceTable.sum) + "</TD>");
		sb.append("</TR>");

		ObjectTable checkconTable = ReportInstance.report.objectTables.get(Constant.CHECK_CONSTRAINT);
		ObjectTable uniqueKeyTable = ReportInstance.report.objectTables.get(Constant.UNIQUE_KEY);
		ObjectTable primaryKeyTable = ReportInstance.report.objectTables.get(Constant.PRIMARY_KEY);
		ObjectTable foreignKeyTable = ReportInstance.report.objectTables.get(Constant.FOREIGN_KEY);
		int sumsum = Constants.CKS.size() + Constants.UKS.size() + Constants.PKS.size() + Constants.FKS.size();

		sb.append("<TR>");
		sb.append("<TD class='awrc'>" + "CONSTRAINT" + "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>" + (sumsum) + "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>"
				+ (checkconTable.numSuccessed + uniqueKeyTable.numSuccessed + primaryKeyTable.numSuccessed + foreignKeyTable.numSuccessed)
				+ "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>"
				+ (checkconTable.numFailed + uniqueKeyTable.numFailed + primaryKeyTable.numFailed + foreignKeyTable.numFailed)
				+ "</TD>");
		sb.append("<TD ALIGN='right' class='awrc'>"
				+ (sumsum - checkconTable.sum - uniqueKeyTable.sum - primaryKeyTable.sum - foreignKeyTable.sum)
				+ "</TD>");
		sb.append("</TR>");

		sb.append("</TABLE>");
		return sb.toString();
	}
}
