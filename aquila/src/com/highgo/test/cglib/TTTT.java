package com.highgo.test.cglib;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TTTT {
	public static void main(String[] args) throws SQLException {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		Connection conn = cpds.getConnection();
		ObjectDaoImpl odi = new ObjectDaoImpl();
		List<Object> list = odi.getObjectList(conn, "measurement2");
		System.out.println(list.size());
	}
}