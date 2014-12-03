package com.highgo.hgdbadmin.reportable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Report {
	// sample
	public EnvironmentReportTable environment;
	public TimeTable timeTable;
	public ObjectSummary objSummary;
	public FailedObjectSummary foSummary;

	public TableReportTable trt;
	public ViewReportTable vrt;
	public SequenceReportTable srt;
	public IndexReportTable irt;
	public ConstraintReportTable crt;

	public Map<String, ObjectTable> objectTables;
	// detail
	public List<SchemaDetail> schemas;
	public List<TableDetail> tables;
	public List<ViewDetail> views;
	public List<IndexDetail> indexes;
	public List<SequenceDetail> sequences;
	public List<CheckKeyDetail> checkKeys;
	public List<PrimaryKeyDetail> primaryKeys;
	public List<UniqueKeyDetail> uniqueKey;
	public List<ForeignKeyDetail> foreignKeys;

	public Report() {
		this.environment = EnvironmentReportTable.getInstance();
		this.timeTable = new TimeTable();
		this.objSummary = new ObjectSummary();
		this.foSummary = new FailedObjectSummary();
		this.trt = new TableReportTable();
		this.vrt = new ViewReportTable();
		this.srt = new SequenceReportTable();
		this.irt = new IndexReportTable();
		this.crt = new ConstraintReportTable();

		this.objectTables = new HashMap<>();
		this.objectTables.put(Constant.SCHEMA, new ObjectTable(Constant.SCHEMA));
		this.objectTables.put(Constant.TABLE, new ObjectTable(Constant.TABLE));
		this.objectTables.put(Constant.VIEW, new ObjectTable(Constant.VIEW));
		this.objectTables.put(Constant.INDEX, new ObjectTable(Constant.INDEX));
		this.objectTables.put(Constant.SEQUENCE, new ObjectTable(Constant.SEQUENCE));
		this.objectTables.put(Constant.CHECK_CONSTRAINT, new ObjectTable(Constant.CHECK_CONSTRAINT));
		this.objectTables.put(Constant.PRIMARY_KEY, new ObjectTable(Constant.PRIMARY_KEY));
		this.objectTables.put(Constant.UNIQUE_KEY, new ObjectTable(Constant.UNIQUE_KEY));
		this.objectTables.put(Constant.FOREIGN_KEY, new ObjectTable(Constant.FOREIGN_KEY));

		this.schemas = new LinkedList<>();
		this.tables = new LinkedList<>();
		this.views = new LinkedList<>();
		this.indexes = new LinkedList<>();
		this.sequences = new LinkedList<>();
		this.checkKeys = new LinkedList<>();
		this.primaryKeys = new LinkedList<>();
		this.uniqueKey = new LinkedList<>();
		this.foreignKeys = new LinkedList<>();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String key : objectTables.keySet()) {
			sb.append(key + ":");
			sb.append(objectTables.get(key));
		}
		sb.append("schemas:");
		for (SchemaDetail sd : schemas) {
			sb.append(sd);
		}
		return sb.toString();
	}

	public String toHTML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");

		sb.append("<head>");
		sb.append("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
		sb.append("<title>");
		sb.append("Report For Database " + "Highgo");
		sb.append("</title>");
		sb.append("<style type='text/css'>body.awr {font:bold 10pt Arial,Helvetica,Geneva,sans-serif;color:black; background:White;}pre.awr  {font:8pt Courier;color:black; background:White;}h1.awr   {font:bold 20pt Arial,Helvetica,Geneva,sans-serif;color:#336699;background-color:White;border-bottom:1px solid #cccc99;margin-top:0pt; margin-bottom:0pt;padding:0px 0px 0px 0px;}h2.awr   {font:bold 18pt Arial,Helvetica,Geneva,sans-serif;color:#336699;background-color:White;margin-top:4pt; margin-bottom:0pt;}h3.awr {font:bold 16pt Arial,Helvetica,Geneva,sans-serif;color:#336699;background-color:White;margin-top:4pt; margin-bottom:0pt;}li.awr {font: 8pt Arial,Helvetica,Geneva,sans-serif; color:black; background:White;}th.awrnobg {font:bold 8pt Arial,Helvetica,Geneva,sans-serif; color:black; background:White;padding-left:4px; padding-right:4px;padding-bottom:2px}th.awrbg {font:bold 8pt Arial,Helvetica,Geneva,sans-serif; color:White; background:#0066CC;padding-left:4px; padding-right:4px;padding-bottom:2px}td.awrnc {font:8pt Arial,Helvetica,Geneva,sans-serif;color:black;background:White;vertical-align:top;}td.awrc    {font:8pt Arial,Helvetica,Geneva,sans-serif;color:black;background:#FFFFCC; vertical-align:top;}a.awr {font:bold 8pt Arial,Helvetica,sans-serif;color:#663300; vertical-align:top;margin-top:0pt; margin-bottom:0pt;}</style>");
		sb.append("</head>");
		sb.append("<body class='awr'>");

		sb.append("<h1 class='awr'>");
		sb.append("数据库迁移报告");
		sb.append("</h1>");
		sb.append("<p>");

		sb.append(this.environment.toHTML());

		sb.append("<p>");
		sb.append("<h1>Report Summary</h1>");
		sb.append("<p>");
		sb.append(timeTable.toHTML());

		sb.append("<p>");
		sb.append(this.objSummary.toHTML());

		sb.append("<p>");
		sb.append(this.foSummary.toHTML());

		sb.append("<h1>Report Detail</h1>");

		sb.append("<UL>");
		sb.append("<LI class='awr'><a href='#tables'>Tables</a>");
		sb.append("<LI class='awr'><a href='#views'>Views</a>");
		sb.append("<LI class='awr'><A class='awr' HREF='#sequences'>Sequences</A>");
		sb.append("<LI class='awr'><a href='#indexes'>Indexes</a>");
		sb.append("<LI class='awr'><a href='#constraints'>Constraints</a>");
		sb.append("<LI class='awr'><A class='awr' HREF='#triggers'>Triggers</A>");
		sb.append("<LI class='awr'><a href='#functions'>Functions</a>");
		sb.append("<LI class='awr'><a href='#prodecures'>Procedures</a>");
		sb.append("</UL>");
		sb.append("<A class='awr' HREF='#top'>Back to Top</A><P>");

		sb.append("<p>");
		sb.append(this.trt.toHTML());
		sb.append("<p>");
		sb.append(this.vrt.toHTML());
		sb.append("<p>");
		sb.append(this.srt.toHTML());
		sb.append("<p>");
		sb.append(this.irt.toHTML());
		sb.append("<p>");
		sb.append(this.crt.toHTML());

		/**
		 * 
		 // navigator sb.append("
		 * <ul>
		 * "); sb.append("
		 * <li><a HREF='#" + Constant.SCHEMA + "'>" +
		 * Constant.SCHEMA.toUpperCase() + "</a></li>"); sb.append("
		 * <li><a HREF='#" + Constant.TABLE + "'>" +
		 * Constant.TABLE.toUpperCase() + "</a></li>"); sb.append("
		 * <li><a HREF='#" + Constant.VIEW + "'>" + Constant.VIEW.toUpperCase()
		 * + "</a></li>"); sb.append("
		 * <li><a HREF='#" + Constant.INDEX + "'>" +
		 * Constant.INDEX.toUpperCase() + "</a></li>"); sb.append("
		 * <li><a HREF='#" + Constant.SEQUENCE + "'>" +
		 * Constant.SEQUENCE.toUpperCase() + "</a></li>"); sb.append("
		 * <li><a HREF='#" + Constant.CHECK_CONSTRAINT + "'>" +
		 * Constant.CHECK_CONSTRAINT.toUpperCase() + "</a></li>"); sb.append("
		 * <li><a HREF='#" + Constant.PRIMARY_KEY + "'>" +
		 * Constant.PRIMARY_KEY.toUpperCase() + "</a></li>"); sb.append("
		 * <li><a HREF='#" + Constant.UNIQUE_KEY + "'>" +
		 * Constant.UNIQUE_KEY.toUpperCase() + "</a></li>"); sb.append("
		 * <li><a HREF='#" + Constant.FOREIGN_KEY + "'>" +
		 * Constant.FOREIGN_KEY.toUpperCase() + "</a></li>"); sb.append("
		 * </ul>
		 * ");
		 * 
		 * 
		 * 
		 * sb.append(this.environment.toHTML());
		 * sb.append(this.objectTables.get(Constant.SCHEMA).toHTML());
		 * sb.append(this.objectTables.get(Constant.TABLE).toHTML());
		 * sb.append(this.objectTables.get(Constant.VIEW).toHTML());
		 * sb.append(this.objectTables.get(Constant.INDEX).toHTML());
		 * sb.append(this.objectTables.get(Constant.SEQUENCE).toHTML());
		 * sb.append(this.objectTables.get(Constant.CHECK_CONSTRAINT).toHTML());
		 * sb.append(this.objectTables.get(Constant.PRIMARY_KEY).toHTML());
		 * sb.append(this.objectTables.get(Constant.UNIQUE_KEY).toHTML());
		 * sb.append(this.objectTables.get(Constant.FOREIGN_KEY).toHTML());
		 */
		sb.append("<h2>End of Report</h2>");
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}

}