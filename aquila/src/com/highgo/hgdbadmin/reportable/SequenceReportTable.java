package com.highgo.hgdbadmin.reportable;

import java.util.List;

public class SequenceReportTable {
	
	
	public String toHTML() {
		List<SequenceDetail> sds = ReportInstance.report.sequences;
		StringBuilder sb = new StringBuilder();
		sb.append("<A class='awr' NAME='sequences'></A>");
		sb.append("<h2>Sequence</h2>");
		for (SequenceDetail sd : sds) {
			sb.append("<A class='awr' NAME='"+sd.baseInfo.objectName+"'></A>");
			sb.append("<h3>"+sd.baseInfo.objectName+"</h3>");
			sb.append("<TABLE BORDER=1>");
//			sb.append("<caption>" + sd.baseInfo.objectName + "</caption>");
			sb.append("<TR>");
			sb.append("<TH width='72' ALIGN='center' class='awrbg'>schema</TH>");
			sb.append("<TH width='72' ALIGN='center' class='awrbg'>name</TH>");
			sb.append("<TH width='72' ALIGN='center' class='awrbg'>startValue</TH>");
			sb.append("<TH width='72' ALIGN='center' class='awrbg'>minimumValue</TH>");
			sb.append("<TH width='72' ALIGN='center' class='awrbg'>maximunValue</TH>");
			sb.append("<TH width='72' ALIGN='center' class='awrbg'>increment</TH>");
			sb.append("<TH width='72' ALIGN='center' class='awrbg'>cycle_option</TH>");
			sb.append("</TR>");

			sb.append("<TR>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + sd.source.schema + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + sd.source.name + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + sd.source.startValue + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + sd.source.minimumValue + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + sd.source.maximunValue + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + sd.source.increment + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + sd.source.cycle_option + "</TD>");
			sb.append("</TR>");

			sb.append("<TR>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + sd.dest.schema + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + sd.dest.name + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + sd.dest.startValue + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + sd.dest.minimumValue + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + sd.dest.maximunValue + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + sd.dest.increment + "</TD>");
			sb.append("<TD width='72' ALIGN='center' class='awrc'>" + sd.dest.cycle_option + "</TD>");
			sb.append("</TR>");
			sb.append("</TABLE>");
		}
		sb.append("<A class='awr' HREF='#top'>Back to Top</A><P>");
		return sb.toString();
	}
}
