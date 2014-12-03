package com.highgo.hgdbadmin.reportable;

import com.highgo.hgdbadmin.model.Table;

public class TableDetail {
	public ObjectInfo baseInfo;

	public long totalRead; // 读线程读到的记录数
	public long totalWrite; // 这是写线程成功提交的个数，也就是最终能够确定的写入到PG中的记录数
	public long totalDerby; // 这是往derby中写入也失败的记录数，也就是最终失败的记录数

	public Table source;
	public Table dest;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(baseInfo);

		sb.append("totalRead:" + totalRead + "\n");
		sb.append("totalWrite:" + totalWrite + "\n");
		sb.append("totalDerby:" + totalDerby + "\n");

		sb.append("source:" + source);
		sb.append("dest:" + dest);

		return sb.toString();
	}
}
