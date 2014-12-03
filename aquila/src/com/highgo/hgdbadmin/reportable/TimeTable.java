package com.highgo.hgdbadmin.reportable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTable {
	public Date start;
	public Date end;

	public TimeTable() {
	}

	public String toHTML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<h2>Time</h2>");
		sb.append("<TABLE BORDER=1 WIDTH=461>");
//		sb.append("<caption><h3>"+"Time"+"</h3></caption>");
		sb.append("<TR>");
		sb.append("<TH width='158' class='awrbg'>Begin</TH>");
		sb.append("<TH width='132' class='awrbg'>End</TH>");
		sb.append("<TH width='149' class='awrbg'>Elapsed</TH>");
		sb.append("</TR>");
		
		sb.append("<tr>");
		sb.append("<td ALIGN='center' class='awrc'>"+getTimeString(start)+"</td>");
		sb.append("<td ALIGN='center' class='awrc'>"+getTimeString(end)+"</td>");
		sb.append("<td ALIGN='center' class='awrc'>"+getTimeString(end.getTime()-start.getTime())+"</td>");
		sb.append("</tr>");
		
		sb.append("</Table>");
		return sb.toString();
	}

	public String getTimeString(long time) {
		time = time / 1000;
		long second = time % 60;
		long mininute = time / 60;
		long hour = mininute / 60;
		long day = hour / 24;
		long rs = second;
		long rm = mininute - hour * 60;
		long rh = hour - day * 24;
		long rd = day;
		return rd + "d " + rh + "h " + rm + "m " + rs + "s";
	}

	public String getTimeString(Date d) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String s = df.format(d);
		return s;
	}
}
