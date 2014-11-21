package com.highgo.test.c3p0deadlock;

import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

import com.mchange.v2.c3p0.ComboPooledDataSource;

//加锁source个postgre的ComboPooledDataSource的getConnection用一个锁
public class Test2 {

	public static void main(String[] args) throws InterruptedException {
		ComboPooledDataSource source = new ComboPooledDataSource("source");
		ComboPooledDataSource source2 = new ComboPooledDataSource("source");
		ComboPooledDataSource postgres = new ComboPooledDataSource("postgres");
		ComboPooledDataSource postgres2 = new ComboPooledDataSource("postgres");
		ReentrantLock lock = new ReentrantLock();
		new Thread(new SourceGetConn2(source, lock), "source").start();
		new Thread(new SourceGetConn2(source2, lock), "source2").start();
		Thread.sleep(1000);
		new Thread(new DestGetConn2(postgres, lock), "postgres").start();
		new Thread(new DestGetConn2(postgres2, lock), "postgres2").start();
	}

}

class SourceGetConn2 implements Runnable {

	private ComboPooledDataSource source = null;
	private ReentrantLock lock;

	public SourceGetConn2(ComboPooledDataSource source, ReentrantLock lock) {
		this.source = source;
		this.lock = lock;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				lock.lock();
				source.getConnection();
				lock.unlock();
				System.out.println("I get a Connection! I am in " + Thread.currentThread().getName());
			} catch (InterruptedException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

}

class DestGetConn2 implements Runnable {

	private ComboPooledDataSource postgres = null;
	private ReentrantLock lock;

	public DestGetConn2(ComboPooledDataSource source, ReentrantLock lock) {
		this.postgres = source;
		this.lock = lock;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				lock.lock();
				postgres.getConnection();
				lock.unlock();
				System.out.println("I get a Connection! I am in " + Thread.currentThread().getName());
			} catch (InterruptedException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

}