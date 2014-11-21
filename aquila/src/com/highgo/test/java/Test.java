package com.highgo.test.java;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Test {

	public static void main(String[] args) {
		try {
			DriverManager.getConnection("");
		} catch (SQLException e) {
		}
		System.out.println("1");
	}
}
