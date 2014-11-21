package com.highgo.test.transfer;

public class GetSizeThread implements Runnable {

	Buffer bt;

	public GetSizeThread(Buffer bt) {
		this.bt = bt;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
				System.out.println(bt.size());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}