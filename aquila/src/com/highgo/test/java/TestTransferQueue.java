package com.highgo.test.java;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

public class TestTransferQueue {
	public static void main(String[] args) {
		TransferQueue<String> tq = new LinkedTransferQueue<>();
		BlockingQueue<String> bq =new  LinkedBlockingQueue<>(100);
	}
}
