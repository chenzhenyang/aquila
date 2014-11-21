package com.highgo.hgdbadmin.vthread;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.highgo.hgdbadmin.log.DerbyUtil;
import com.highgo.hgdbadmin.log.Log;
import com.highgo.hgdbadmin.log.ReInsert;

/**
 * @author u
 */
public class MainThread {
	private static Logger logger = Logger.getLogger(MainThread.class);

	public static void migrateTable(String schema, String table, int readThreads, int bufferSize, int batchSize) throws InterruptedException
			 {
		beforeMigrate(schema,table);
		Date start = new Date();
		CountDownLatch startGate = new CountDownLatch(1);
		CountDownLatch endGate = new CountDownLatch(readThreads + 2);
		Buffer<Record> buffer = new Buffer<>(bufferSize);
		doAction(startGate, endGate, buffer, schema, table, readThreads, batchSize);
		startGate.countDown();// 所有线程一起启动
		endGate.await();// 等待 ，直到所有线程都完成
		Date end = new Date();
		afterMigrate();
		logger.info("TABLE[" + (schema + "." + table).toUpperCase() + "] is migrated successfully,spend time:"
				+ (end.getTime() - start.getTime()) + "ms");
	}

	private static void beforeMigrate(String schema,String table){
		String path = schema + "." + table;
		File redoFileForTable = new File(path);
		if (redoFileForTable.exists()) {
			redoFileForTable.delete();
		}
		Log.PATH = path;
		try {
			if(DerbyUtil.getSize2()!=0){
				DerbyUtil.delete2();
			}
		} catch (ClassNotFoundException | SQLException | IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	private static void afterMigrate(){
		Lock.isComeOn.set(true);// 只要所有线程完事了，就可以运行这句代码了
		Lock.cdl = new CountDownLatch(1);
	}
	
	public static void doAction(CountDownLatch startGate, CountDownLatch endGate, Buffer<Record> buffer, String schema,
			String table, int readThreads, int batchSize) throws InterruptedException {
		new Thread(new ReadThread(startGate, endGate, buffer, schema, table), "ReadThread").start();
		for (int i = 0; i < readThreads; i++) {
			new Thread(new WriteThread(startGate, endGate, buffer, schema, table, batchSize), "WriteThread" + i)
					.start();
		}
		new Thread(new ReInsert(endGate, startGate), "ReInsertThread").start();
		System.out.println("表" + table + " start");
		logger.info("TABLE[" + (schema + "." + table).toUpperCase() + "] start......");
	}
}
