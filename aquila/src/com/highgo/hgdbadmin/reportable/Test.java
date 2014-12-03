package com.highgo.hgdbadmin.reportable;

import java.util.Calendar;

public class Test {
	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(100);
		System.out.println(calendar.get(Calendar.HOUR));
	}
}
