package com.highgo.test.sql2pg.datetype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.highgo.hgdbadmin.vthread.Lock;
import com.highgo.hgdbadmin.vthread.Record;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 测试从SqlServer中的xml,image,binary,varbinary到pg中xml和bytea的转换
 * 
 * @author u
 *
 */
public class TestClob {

	public static List<Record> list = new LinkedList<>();

	public static void main(String[] args) throws InterruptedException {
		Lock.fieldNum4SomeTable.set(4);
		readTest();
		System.out.println(list.size());
		writeTest();
	}

	public static void writeTest() {
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource("postgres");
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("insert into transfertestclob(field1,field2,field3,field4) values(XML(?),?,?,?)");
			for (int i = 0; i < list.size(); i++) {
				list.get(i).write(ps);
				ps.execute();
			}

			ps.close();
			conn.close();
			cpds.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void readTest() {
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			Connection conn = cpds.getConnection();
			PreparedStatement ps = conn.prepareStatement("select * from  TransferTestClob");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				list.add(new Record(rs, 4));
			}

			rs.close();
			ps.close();
			conn.close();
			cpds.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
