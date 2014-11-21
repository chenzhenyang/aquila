package com.highgo.test.transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class WriteThread implements Runnable {

	private Buffer buffer;

	public WriteThread(Buffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void run() {
		ComboPooledDataSource cpds = new ComboPooledDataSource("postgres");
		try {
			Date start = new Date();
			Connection conn = cpds.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement ps = conn
					.prepareStatement("insert into measurement(field1,field2,field3,field4,field5,field6,field7) values(?,?,?,?,?,?,?)");
			int m = 1;
			System.out.println(Lock.isComeOn.get());
			while (Lock.isComeOn.get()) {
				buffer.take().write(ps);
				m++;
				if (m % 1000 == 0) {
					ps.executeBatch();
					conn.commit();
				}
			}
			Date end = new Date();
			System.out.println("WriteThread:" + Thread.currentThread().getName()
					+ " Over with time:" + (end.getTime() - start.getTime()) / 1000);// †ÎÎ»£ºÃë
		} catch (SQLException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	// public static void main(String[] args) {
	// Record record1 = new Record("1","1","1","1","1","1","1");
	// Record record2 = new Record("1","1","1","1","1","1","1");
	// Record record3 = new Record("1","1","1","1","1","1","1");
	//
	// Buffer buffer = new Buffer();
	//
	// try {
	// buffer.put(record1);
	// buffer.put(record2);
	// buffer.put(record3);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// WriteThread wt = new WriteThread(buffer);
	// wt.run();
	// }
}
