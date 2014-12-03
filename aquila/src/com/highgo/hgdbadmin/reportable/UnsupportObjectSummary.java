package com.highgo.hgdbadmin.reportable;

import java.util.List;

import com.highgo.hgdbadmin.model.Function;
import com.highgo.hgdbadmin.model.Procedure;
import com.highgo.hgdbadmin.model.Trigger;
import com.highgo.hgdbadmin.myutil.Constants;

public class UnsupportObjectSummary {

	public String toHTML() {
		StringBuilder sb = new StringBuilder();
		List<Function> funcs = Constants.FUNCTIONS;
		List<Procedure> procs = Constants.PROCEDURES;
		List<Trigger> triggers = Constants.TRIGGERS;

		sb.append("<TABLE BORDER=1 WIDTH=461>");
		sb.append("<TR>");
		sb.append("<TH width='158' class='awrbg'>Object Name</TH>");
		sb.append("<TH width='132' class='awrbg'>Object Type</TH>");
		sb.append("<TH width='149' class='awrnobg'>Cause</TH>");
		sb.append("</TR>");

		for (Function func : funcs) {
			sb.append("<TR>");
			sb.append("<TD ALIGN='center' class='awrnc'><a href='#4700'>" + func.name + "</a></TD>");
			sb.append("<TD ALIGN='center' class='awrnc'>FUNCTION</TD>");
			sb.append("<TD align='center' class='awrnc'>Unsuportted</TD>");
			sb.append("</TR>");
		}

		for (Procedure proc : procs) {
			sb.append("<TR>");
			sb.append("<TD ALIGN='center' class='awrnc'><a href='#4700'>" + proc.name + "</a></TD>");
			sb.append("<TD ALIGN='center' class='awrnc'>Procedure</TD>");
			sb.append("<TD align='center' class='awrnc'>Unsuportted</TD>");
			sb.append("</TR>");
		}

		for (Trigger trigger : triggers) {
			sb.append("<TR>");
			sb.append("<TD ALIGN='center' class='awrnc'><a href='#4700'>" + trigger.name + "</a></TD>");
			sb.append("<TD ALIGN='center' class='awrnc'>Trigger</TD>");
			sb.append("<TD align='center' class='awrnc'>Unsuportted</TD>");
			sb.append("</TR>");
		}
		sb.append("</TABLE>");
		return sb.toString();
	}

}
