package com.highgo.test.transfer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Buffer {
	// 这就是一个缓冲池了
	BlockingQueue<Record> bbb = new LinkedBlockingQueue<>();

	public void put(Record record) throws InterruptedException {
		bbb.put(record);
		// if(bbb.size()==10){
		// Thread.sleep(10000);
		// }
	}

	public Record take() throws InterruptedException {
		return bbb.take();
	}

	public int size() {
		return bbb.size();
	}

	public static void main(String[] args) throws InterruptedException {
		Buffer bt = new Buffer();
		
		new Thread(new GetSizeThread(bt)).start();
		
		new Thread(new PutThread(bt)).start();
		new Thread(new TakeThread(bt)).start();
		
		new Thread(new PutThread(bt)).start();
		new Thread(new TakeThread(bt)).start();
		
//		new Thread(new PutThread(bt)).start();
		new Thread(new TakeThread(bt)).start();
//		
//		new Thread(new PutThread(bt)).start();
		new Thread(new TakeThread(bt)).start();
	}

	public static class PutThread implements Runnable {
		Buffer bt;

		public PutThread(Buffer bt) {
			this.bt = bt;
		}

		@Override
		public void run() {
			while (true) {
				try {
					bt.put(new Record()); 
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static class TakeThread implements Runnable {
		Buffer bt;

		public TakeThread(Buffer bt) {
			this.bt = bt;
		}

		@Override
		public void run() {
			while (true) {
				try {
					bt.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
