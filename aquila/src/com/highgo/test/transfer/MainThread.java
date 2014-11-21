package com.highgo.test.transfer;


public class MainThread {
//	private static final int NTHREADS = 4;
//	private static final ExecutorService es = Executors.newFixedThreadPool(NTHREADS);
	 
	public static void main(String[] args) {
		
		Buffer buffer = new Buffer();
		
//		es.execute(new GetSizeThread(buffer));
//		es.execute(new ReadThread(buffer));
//		es.execute(new WriteThread(buffer));
//		es.execute(new WriteThread(buffer));
//		es.execute(new WriteThread(buffer));
//		es.shutdown();
		
		new Thread(new GetSizeThread(buffer)).start();
		
		new Thread(new ReadThread(buffer)).start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new WriteThread(buffer)).start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new WriteThread(buffer)).start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new WriteThread(buffer)).start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new WriteThread(buffer)).start();
		
	}
}
