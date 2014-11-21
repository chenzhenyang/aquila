package com.highgo.test.c3p0deadlock;

import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;


//加锁source个postgre的ComboPooledDataSource的getConnection用一个锁
public class Test {

	public static void main(String[] args) throws InterruptedException {
		ComboPooledDataSource source = new ComboPooledDataSource("source");
//		ComboPooledDataSource source2 = new ComboPooledDataSource("source");
		ComboPooledDataSource postgres = new ComboPooledDataSource("postgres");
//		ComboPooledDataSource postgres2 = new ComboPooledDataSource("postgres");
		new Thread(new SourceGetConn(source), "source").start();
		new Thread(new SourceGetConn(source), "source2").start();
//		Thread.sleep(1000);
//		new Thread(new DestGetConn(postgres), "postgres").start();
//		new Thread(new DestGetConn(postgres2), "postgres2").start();
	}

}

class SourceGetConn implements Runnable {

	private ComboPooledDataSource source = null;

	public SourceGetConn(ComboPooledDataSource source) {
		this.source = source;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				source.getConnection();
				System.out.println("I get a Connection! I am in " + Thread.currentThread().getName());
			} catch (InterruptedException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

}

class DestGetConn implements Runnable {

	private ComboPooledDataSource postgres = null;

	public DestGetConn(ComboPooledDataSource source) {
		this.postgres = source;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				postgres.getConnection();
				System.out.println("I get a Connection! I am in " + Thread.currentThread().getName());
			} catch (InterruptedException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

}