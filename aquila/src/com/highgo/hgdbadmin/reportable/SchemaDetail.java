package com.highgo.hgdbadmin.reportable;

import com.highgo.hgdbadmin.model.Schema;

public class SchemaDetail {
	public ObjectInfo baseInfo;

	public Schema source;
	public Schema dest;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(baseInfo);
		sb.append("source:" + source);
		sb.append("dest:" + dest);
		return sb.toString();
	}
	
	public String toHTML() {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}
	
	
}
