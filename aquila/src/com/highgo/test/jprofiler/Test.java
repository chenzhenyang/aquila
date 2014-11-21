package com.highgo.test.jprofiler;

public class Test {

	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						System.out.println(3 * 7);
					}
				}
			}).start();
		}
	}
}
