package com.highgo.hgdbadmin.vthread;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Lock {
	public static AtomicBoolean isComeOn = new AtomicBoolean(true);
	
	
	public static String CURRENT_TABLE = null;
	public static AtomicInteger fieldNum4SomeTable = new AtomicInteger();
	public static List<String> fieldsType = new LinkedList<>();
	public static List<String> fieldsName = new LinkedList<>();
	
	
	
	public static CountDownLatch cdl = new CountDownLatch(1);//这个锁保证fieldNum4SomeTable，fieldsType和fieldsName被初始化之后再运行makeSql

	public static String SCHEMACURRENT = null;
	public static String TABLECURRENT = null;
	

}
