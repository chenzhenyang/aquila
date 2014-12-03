package com.highgo.hgdbadmin.reportable;

import java.util.LinkedList;
import java.util.List;

public class ObjectInfo {
	public String objectName;
	public String sourceDifinition;// 有些类型的对象没有这个，看看再说吧
	public String sqlGenerated;

	public boolean isSuccessed = false;
	public List<String> causes = new LinkedList<>();

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ObjectInfo:\n");
		sb.append("objectName:" + objectName + "\n");
		sb.append("sourceDifinition:" + sourceDifinition + "\n");
		sb.append("sqlGenerated:" + sqlGenerated + "\n");
		sb.append("isSuccessed:" + isSuccessed + "\n");
		sb.append("causes:" + causes + "\n");
		return sb.toString();
	}
	
	public String toHTML() {
		StringBuilder sb  = new StringBuilder();
		sb.append("<tr>");
		sb.append("<td>"+this.objectName+"</td>");
//		sb.append("<td>"+this.sourceDifinition+"</td>");
//		sb.append("<td>"+this.sqlGenerated+"</td>");
		sb.append("<td>"+this.isSuccessed+"</td>");
		sb.append("<td>");
		for(String str:causes){
			sb.append(str+"<br/>");	
		}
		sb.append("</td>");
		sb.append("</tr>");
		return  sb.toString();
	}
}