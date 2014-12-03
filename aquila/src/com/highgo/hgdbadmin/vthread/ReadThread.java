package com.highgo.hgdbadmin.vthread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.highgo.hgdbadmin.myutil.C3P0Util;
import com.highgo.hgdbadmin.myutil.ShellEnvironment;
import com.highgo.hgdbadmin.reportable.ReportRecord;
import com.highgo.hgdbadmin.reportable.TableDetail;

public class ReadThread implements Runnable {

	private static Logger logger = Logger.getLogger(ReadThread.class);
	private CountDownLatch startGate;
	private CountDownLatch endGate;
	private Buffer<Record> buffer;

	private String schema;
	private String table;
	private TableDetail tableDetail;
	

	public ReadThread(CountDownLatch startGate, CountDownLatch endGate, Buffer<Record> buffer, String schema,
			String table,TableDetail tableDetail) {
		this.startGate = startGate;
		this.endGate = endGate;
		this.buffer = buffer;
		this.schema = schema;
		this.table = table;
		this.tableDetail = tableDetail;
	}

	@Override
	public void run() {
		try {
			startGate.await();
			doAction();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			endGate.countDown();
		}

	}

	public void doAction() throws InterruptedException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Date start = new Date();
			conn = C3P0Util.getInstance().getConnection(C3P0Util.SOURCE);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			String sql = "select * from " + schema + "." + table;
			logger.info(sql);

			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			//这一段放在MainThread中顺序执行
			/**
			 * ResultSetMetaData rsmd = rs.getMetaData(); int columnNum =
			 * rsmd.getColumnCount();
			 * 
			 * Lock.fieldsName.clear(); Lock.fieldsType.clear();
			 * 
			 * for (int i = 1; i <= columnNum; i++) {
			 * Lock.fieldsName.add(rsmd.getColumnName(i));
			 * Lock.fieldsType.add(rsmd.getColumnTypeName(i)); }
			 * Lock.CURRENT_TABLE = (schema + "." + table).toUpperCase();
			 * Lock.fieldNum4SomeTable.set(columnNum);
			 * 
			 * logger.info("Lock.fieldNum4SomeTable:" +
			 * Lock.fieldNum4SomeTable); logger.info("Lock.fieldsName:" +
			 * Lock.fieldsName); logger.info("Lock.fieldsType:" +
			 * Lock.fieldsType);
			 **/
			
			Lock.cdl.countDown();// 开启写线程

			while (rs.next()) {
				// ReportRecord对象
				ReportRecord.NUMBER_READER.incrementAndGet();
				buffer.put(new Record(rs));
			}
			tableDetail.totalRead = ReportRecord.NUMBER_READER.get();
			ReportRecord.NUMBER_READER.set(0L);//用完了接着重置
					
			/**
			 * WriteThread结束的条件是Lock.isComeOn ==
			 * false(ReadThread2结束)&&buffer.size==0(缓冲区中没有数据了)，
			 * 有了再finally代码块里的那行代码，这个就不要了，为了保持记忆，在这继续放一行，其实已经没有用了。
			 */
			Lock.isComeOn.set(false);

			rs.close();
			ps.close();
			conn.close();

			tableDetail.baseInfo.causes.clear();
			
			Date end = new Date();
			long result = end.getTime() - start.getTime();
			logger.info("线程" + Thread.currentThread().getName() + "结束！ 耗时：" + result + "ms");
		} catch (SQLException e) {
			String message = "Table " + schema + "." + table + " has something wrong in migrating! " + e.getMessage();
			tableDetail.baseInfo.causes.add(message);
			ShellEnvironment.println(message);
			logger.error(message);
		} finally {
			Lock.isComeOn.set(false);// 把这句话放在这就好了
			C3P0Util.getInstance().close(rs);
			C3P0Util.getInstance().close(ps);
			C3P0Util.getInstance().close(conn);
		}
	}
}
