package com.highgo.hgdbadmin.vthread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Buffer<T> {
	BlockingQueue<T> bbb;

	// BlockingQueue<T> bbb = new SynchronousQueue<>();

	public Buffer(int size) {
		bbb = new LinkedBlockingQueue<>(size);
	}

	public void put(T record) throws InterruptedException {
		bbb.put(record);
	}

	public T take() throws InterruptedException {
		return bbb.take();
	}

	public int size() {
		return bbb.size();
	}
	
	
	public T poll(Long timeout, TimeUnit unit) throws InterruptedException{
		return bbb.poll(timeout, unit);
	}
}
