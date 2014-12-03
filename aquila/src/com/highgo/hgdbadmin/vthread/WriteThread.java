package com.highgo.hgdbadmin.vthread;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.highgo.hgdbadmin.log.DerbyUtil;
import com.highgo.hgdbadmin.myutil.C3P0Util;
import com.highgo.hgdbadmin.myutil.DBUtil;
import com.highgo.hgdbadmin.reportable.TableDetail;

public class WriteThread implements Runnable {

	private static Logger logger = Logger.getLogger(WriteThread.class);
	private CountDownLatch startGate;
	private CountDownLatch endGate;
	private Buffer<Record> buffer;

	private String toSchema;
	private String toTable;

	private int batchSize;
	private AtomicLong al;
	private TableDetail tableDetail;
	
	private List<Record> redo;

	private Connection conn;
	
	
	

	public WriteThread(CountDownLatch startGate, CountDownLatch endGate, Buffer<Record> buffer, String toSchema,
			String toTable, int batchSize,AtomicLong al,TableDetail tableDetail) {
		try {
			this.conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
		} catch (SQLException e) {
			try {
				this.conn = C3P0Util.getInstance().getConnection(C3P0Util.POSTGRES);
			} catch (SQLException e1) {
				logger.error(e1.getMessage());
			}
		}
		this.startGate = startGate;
		this.endGate = endGate;
		this.buffer = buffer;
		this.toSchema = toSchema;
		this.toTable = toTable;
		this.batchSize = batchSize;
		this.al = al;
		this.tableDetail = tableDetail;
		this.redo = new LinkedList<>();
	}

	@Override
	public void run() {
		try {
			startGate.await();
			doAction();
		} catch (InterruptedException e) {
		} finally {
			endGate.countDown();
		}

	}

	private void doAction() throws InterruptedException {
		List<String> errors = new LinkedList<>();
		Date start = new Date();
		Lock.cdl.await();
		PreparedStatement ps = null;
		try {
			conn.setAutoCommit(false);
			String sql = makeSql(toSchema, toTable);
			ps = conn.prepareStatement(sql);
		} catch (SQLException e1) {
			errors.add(e1.getMessage());
			logger.error(e1.getMessage());
		}
		redo.clear();
		int m = 1;
		Record rec = null;
		/**
		 * Lock.isComeOn.get()==true 说明 读线程活着，这时候可能buffer短时间片内没有数据，it‘s
		 * okay，buffer中有数据的话也Okay。 buffer.size!=0
		 * 说明buffer中海油数据，这时候读线程可能结束，但是也要把buffer中的剩余数据处理完。
		 * 
		 * 这个地方有一个问题就是，如果代码运行到Lock.isComeOn.get()时判断正确，也就是读线程还在，
		 * 当判断到buffer.size() != 0时也是true，但是这时候，buffer中唯一的一个数据接着被其他的线程拿走了，
		 * 这个时候，本线程就会阻塞在rec = buffer.take();上
		 * 这个地方可以使用BlockingQueue#poll(timeout,
		 * unit)方法，但是同样会产生问题，比如当Buffer中的数据不够用时，某些调度机会比较少的WriteThread线程可能就会超时
		 */
		while (Lock.isComeOn.get() || buffer.size() != 0) {
			// rec = buffer.take();
			rec = buffer.poll(500L, TimeUnit.MILLISECONDS);
			if (rec != null) {
				try {
					rec.write(ps);
					redo.add(rec);
					m++;
					ps.addBatch();
					// 在突然退出catch (SQLException e) 代码块的时候，可能会使conn变成close
					if (conn == null || conn.isClosed()) {
						conn = DBUtil.getNewConnection(C3P0Util.POSTGRES);
						if (conn != null) {
							conn.setAutoCommit(false);
							String sql = makeSql(toSchema, toTable);
							ps = conn.prepareStatement(sql);
						}
					}
					// 到达一次Batch提交的数量或者buffer中没有数据了就提交。
					if (m % batchSize == 0 || buffer.size() == 0) {
						ps.executeBatch();
						conn.commit();
						//这一行代码特别神奇，如果conn.commit成功，就加上这个redo.size(),如果conn.commit失败，则这句代码不会被执行，直接跳到catch里边去了，然后就是ReInsert线程的事情了
						al.addAndGet(redo.size());
						redo.clear();// 如果能走到这一行代码，说明提交成功了，然后把redo list清空
					}
				} catch (SQLException e) {
					// 没有插入成功的记录插入到derby中
					// 在这个地方需要清空连接池中没用的连接，不然一个失败的建立一个连接，很快就超过pgserver的max-connection了
					logger.info(e.getMessage());
					errors.add(e.getMessage());
					try {
						DerbyUtil.insertBatch2(redo);
						redo.clear();
						conn = DBUtil.getNewConnection(C3P0Util.POSTGRES);
						if (conn != null) {
							conn.setAutoCommit(false);
							String sql = makeSql(toSchema, toTable);
							ps = conn.prepareStatement(sql);
						}
					} catch (SQLException | IOException e1) {
						logger.error(e.getMessage());
						errors.add(e.getMessage());
					}
				}
			}
		}
		try {
			conn.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		Date end = new Date();
		long result = end.getTime() - start.getTime();
		logger.info("线程" + Thread.currentThread().getName() + "结束！ 耗时：" + result + "ms");
		errors.clear();
		tableDetail.baseInfo.causes.addAll(errors);
	}

	public String makeSql(String schema, String table) {
		String sql = "insert into " + schema + "." + table + "(";
		for (int i = 0; i < Lock.fieldNum4SomeTable.get() - 1; i++) {
			sql += Lock.fieldsName.get(i) + ",";
		}
		sql += Lock.fieldsName.get(Lock.fieldsName.size() - 1);

		sql += ")" + " values(";
		for (int i = 0; i < Lock.fieldNum4SomeTable.get() - 1; i++) {
			if (Lock.fieldsType.get(i).equals("xml")) {
				sql += "XML(?),";
			} else {
				sql += "?,";
			}
		}
		if (Lock.fieldsType.get(Lock.fieldsType.size() - 1).equals("xml")) {
			sql += "XML(?))";
		} else {
			sql += "?)";
		}
		return sql;
	}

}
