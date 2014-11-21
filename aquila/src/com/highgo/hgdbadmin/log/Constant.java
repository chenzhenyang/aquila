package com.highgo.hgdbadmin.log;

import java.util.concurrent.atomic.AtomicLong;

public class Constant {
	// 读数据库时的id的初始值
	public static AtomicLong READNEXTID = new AtomicLong(1);
	// 写数据库时的Id的初始值，跟读的初始值一样
	public static AtomicLong WRITENEXTID = new AtomicLong(1);
	// 维护Derby数据库中的记录的条数
	public static AtomicLong ROWSNUM = new AtomicLong(0);
	// 当前活着的线程个数
	@Deprecated
	public static AtomicLong THREADNUM = new AtomicLong(0);

}
