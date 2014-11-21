package com.highgo.test.transfer.bq;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class BlockQueueTest {
	public static void main(String[] args) throws InterruptedException {
		BlockingDeque<String> bq = new LinkedBlockingDeque<>();
		String str = "test";
		bq.put(str);
		str = "yesorno";
		bq.put(str);
		System.out.println(bq.size());
		System.out.println(bq.take());
		System.out.println(bq.take());
	}
}
