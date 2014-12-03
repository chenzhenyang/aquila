package com.highgo.hgdbadmin.vthread;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.highgo.hgdbadmin.log.DerbyUtil;
import com.highgo.hgdbadmin.log.Log;
import com.highgo.hgdbadmin.log.ReInsert;
import com.highgo.hgdbadmin.myutil.C3P0Util;
import com.highgo.hgdbadmin.myutil.MigrateCenter;
import com.highgo.hgdbadmin.reportable.ObjectInfo;
import com.highgo.hgdbadmin.reportable.ReportInstance;
import com.highgo.hgdbadmin.reportable.TableDetail;

/**
 * @author u
 */
public class MainThread {
	private static Logger logger = Logger.getLogger(MainThread.class);

	public static void migrateTable(String schema, String table, int readThreads, int bufferSize, int batchSize)
			throws InterruptedException {
		beforeMigrate(schema, table);
		Date start = new Date();
		CountDownLatch startGate = new CountDownLatch(1);
		CountDownLatch endGate = new CountDownLatch(readThreads + 2);
		Buffer<Record> buffer = new Buffer<>(bufferSize);
		
		ObjectInfo objectInfo = new ObjectInfo();
		
		TableDetail tableDetail = new TableDetail();
		tableDetail.baseInfo = objectInfo;
		tableDetail.source = MigrateCenter.fetchATableFromSqlServer(schema, table);
		tableDetail.dest = MigrateCenter.fetchATableFromSqlServer(schema, table);
		
		tableDetail.baseInfo.objectName=schema+"."+table;
		tableDetail.baseInfo.sourceDifinition=null;
		tableDetail.baseInfo.sqlGenerated=tableDetail.source.toSql();
		
		List<AtomicLong> sum = new LinkedList<>();
		
 		doAction(startGate, endGate, buffer, schema, table, readThreads, batchSize,sum,tableDetail);
		
		startGate.countDown();// 所有线程一起启动
		endGate.await();// 等待 ，直到所有线程都完成
		Date end = new Date();
		afterMigrate(sum, tableDetail);
		logger.info("TABLE[" + (schema + "." + table).toUpperCase() + "] is migrated successfully,spend time:"
				+ (end.getTime() - start.getTime()) + "ms");
	}

	private static void beforeMigrate(String schema, String table) {
		// 一个表一个文件
		String path = schema + "." + table;
		File redoFileForTable = new File(path);
		if (redoFileForTable.exists()) {
			redoFileForTable.delete();
		}
		Log.PATH = path;
		// 清空Derby#whore2表
		try {
			if (DerbyUtil.getSize2() != 0) {
				DerbyUtil.delete2();
			}
		} catch (ClassNotFoundException | SQLException | IOException e) {
			logger.error(e.getMessage());
		}
	}

	private static void afterMigrate(List<AtomicLong> sum,TableDetail tableDetail) {
		// 只要所有线程完事了，就可以运行这句代码了
		Lock.isComeOn.set(true);
		// 这个锁保证fieldNum4SomeTable，fieldsType和fieldsName被初始化之后再运行makeSql.每个表再迁移完成之后，要将这个值重新设置
		Lock.cdl = new CountDownLatch(1);
		
		for(AtomicLong al : sum ){
			tableDetail.totalWrite += al.get();
		}
		
		if(tableDetail.totalRead==tableDetail.totalWrite+tableDetail.totalDerby){
			tableDetail.baseInfo.isSuccessed = true;
		}
		
		ReportInstance.report.tables.add(tableDetail);
		
		System.out.println(ReportInstance.report.tables.get(0));
	}

	private static void getTableMetaData(String schema, String table) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			String sql = "select * from " + schema + "." + table + " where 1 = 0";
			logger.info(sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnNum = rsmd.getColumnCount();
			Lock.fieldsName.clear();
			Lock.fieldsType.clear();
			for (int i = 1; i <= columnNum; i++) {
				Lock.fieldsName.add(rsmd.getColumnName(i));
				Lock.fieldsType.add(rsmd.getColumnTypeName(i));
			}
			Lock.CURRENT_TABLE = (schema + "." + table).toUpperCase();
			Lock.fieldNum4SomeTable.set(columnNum);
		} catch (SQLException e) {
			logger.info(e.getMessage());
		} finally {
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
		logger.info("Lock.fieldNum4SomeTable:" + Lock.fieldNum4SomeTable);
		logger.info("Lock.fieldsName:" + Lock.fieldsName);
		logger.info("Lock.fieldsType:" + Lock.fieldsType);
	}

	public static void doAction(CountDownLatch startGate, CountDownLatch endGate, Buffer<Record> buffer, String schema,
			String table, int readThreads, int batchSize,List<AtomicLong> sum,TableDetail tableDetail) throws InterruptedException {
		getTableMetaData(schema, table);
		
		new Thread(new ReadThread(startGate, endGate, buffer, schema, table,tableDetail), "ReadThread").start();
		
		for (int i = 0; i < readThreads; i++) {
			AtomicLong al = new AtomicLong(0);
			sum.add(al);
			new Thread(new WriteThread(startGate, endGate, buffer, schema, table, batchSize,al,tableDetail), "WriteThread" + i)
					.start();
		}
		new Thread(new ReInsert(endGate, startGate,tableDetail), "ReInsertThread").start();
		System.out.println("table[" + table + "] start");
		logger.info("TABLE[" + (schema + "." + table).toUpperCase() + "] start......");
	}
	
	public static void main(String[] args) throws InterruptedException {
		MainThread.migrateTable("dbo","attachment", 4, 1000, 100);
	}
}
