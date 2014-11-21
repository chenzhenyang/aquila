package com.highgo.test.transfer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class MuitleThreadWriter {
	private static final int NTHREADS = 8;
	private static final ExecutorService es = Executors.newFixedThreadPool(NTHREADS);

	public static void main(String[] args) {
		Runnable task1 = new InsertTask();
		Runnable task2 = new InsertTask();
		Runnable task3 = new InsertTask();
		Runnable task4 = new InsertTask();
		Runnable task5 = new InsertTask();
		Runnable task6 = new InsertTask();
		Runnable task7 = new InsertTask();
		Runnable task8 = new InsertTask();
//		
		es.execute(task1);
		es.execute(task2);
		es.execute(task3);
		es.execute(task4);
		es.execute(task5);
		es.execute(task6);
		es.execute(task7);
		es.execute(task8);
		
		es.shutdown();
		// // es.awaitTermination(timeout, unit)

		// 測試串行執行，用這個
//		 InsertTask it = new InsertTask();
//		 it.run();
	}
}

class InsertTask implements Runnable {

	private static int SUM = 1000000/8;

	@Override
	public void run() {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			Date start = new Date();
			Connection conn = cpds.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement("insert into measurement2(field1,field2,field3,field4,field5,field6,field7) values(?,?,?,?,?,?,?)");
			int m = 1;
			for (int i = 0; i < InsertTask.SUM; i++) {
				ps.setString(1, "field1-" + i);
				ps.setString(2, "field2-" + i);
				ps.setString(3, "field3-" + i);
				ps.setString(4, "field4-" + i);
				ps.setString(5, "field5-" + i);
				ps.setString(6, "field6-" + i);
				ps.setString(7, "field7-" + i);
				ps.addBatch();
				m++;
				if (m % 50 == 0) {
					m = 1;
					ps.executeBatch();
					conn.commit();
				}
			}
			Date end = new Date();
			System.out.println((end.getTime() - start.getTime()) / 1000);// 單位：秒
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
