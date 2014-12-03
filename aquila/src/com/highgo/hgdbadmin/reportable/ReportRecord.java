package com.highgo.hgdbadmin.reportable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ReportRecord {
	public static AtomicLong  NUMBER_READER  = new AtomicLong(0);    //读线程总共读到的数据，这个值在每个表迁移完之后要还原
	public static List<AtomicLong> NUMBER_WRITE;
	static{
		NUMBER_WRITE=Collections.synchronizedList(new LinkedList<AtomicLong>());
	}
	public static AtomicLong NUMBER_DERBY = new AtomicLong(0);
}
