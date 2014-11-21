package com.highgo.test.java;

public class TestStatic {

	public static void main(String[] args) {

		System.out.println(Constant.in);
		Constant.in = 11;
		System.out.println(Constant.in);

	}
}

class Constant {
	public static Integer in = new Integer(10);
}