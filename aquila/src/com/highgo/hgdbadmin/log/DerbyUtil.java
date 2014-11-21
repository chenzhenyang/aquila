package com.highgo.hgdbadmin.log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.highgo.hgdbadmin.myutil.MigrateCenter;
import com.highgo.hgdbadmin.vthread.Record;

public class DerbyUtil {
	private static Logger logger = Logger.getLogger(MigrateCenter.class);
	private static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private static String protocol = "jdbc:derby:";
	private static String dbName = "db";
	private static String url = protocol + dbName + ";create=true";

	static {
		try {
			Class.forName(driver).newInstance();
			logger.info("Loaded the appropriate driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createTable() throws SQLException {
		Connection conn = DriverManager.getConnection(protocol + dbName + ";create=true");
		Statement st = conn.createStatement();
		String sql = "create table whore(id bigint GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1) primary key,obj blob)";
		st.execute(sql);
		st.close();
		conn.close();
	}

	public static void insertBatch(List<Record> list) throws SQLException, IOException {
		for (Record rec : list) {
			insert(rec);
		}
	}

	public static void insert(Record obj) throws SQLException, IOException {
		Connection conn = DriverManager.getConnection(protocol + dbName + ";create=true");
		String sql = "insert into whore(obj) values(?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ByteArrayOutputStream saos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(saos);
		oos.writeObject(obj);
		oos.flush();
		ps.setBytes(1, saos.toByteArray());
		ps.execute();
		ps.close();
		conn.close();
		oos.close();
		saos.close();
		Constant.ROWSNUM.getAndIncrement();
	}

	public static Pair get(Long id) throws SQLException, IOException, ClassNotFoundException {
		Connection conn = DriverManager.getConnection(protocol + dbName + ";create=true");
		String sql = "select * from whore where id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		Record record = null;
		if (rs.next()) {
			byte[] bytes = rs.getBytes(2);
			ByteArrayInputStream sais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(sais);
			record = (Record) ois.readObject();
			// Blob blob =rs.getBlob(2);
			// InputStream is = blob.getBinaryStream();
			// ObjectInputStream ois = new ObjectInputStream(is);
			// // Record record = (Record) ois.readObject();
			// // System.out.println(record.fields);
			// byte[] bytes = new byte[100];
			// while(ois.read(bytes)!=-1){
			// System.out.println(bytes);
			// }
			ois.close();
			sais.close();
		}
		Constant.ROWSNUM.getAndDecrement();
		Constant.READNEXTID.getAndIncrement();
		return new Pair(id, record);
	}

	public static List<Pair> getAll() throws SQLException, IOException, ClassNotFoundException {
		List<Pair> list = new LinkedList<>();
		Connection conn = DriverManager.getConnection(protocol + dbName + ";create=true");
		String sql = "select * from whore";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		long id = 0;
		Record record = null;
		while (rs.next()) {
			id = rs.getLong(1);
			byte[] bytes = rs.getBytes(2);
			ByteArrayInputStream sais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(sais);
			record = (Record) ois.readObject();
			ois.close();
			sais.close();
			list.add(new Pair(id, record));
		}
		return list;
	}

	public static int getSize() throws SQLException, IOException, ClassNotFoundException {
		Connection conn = DriverManager.getConnection(protocol + dbName + ";create=true");
		String sql = "select count(*) from whore";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		rs.next();
		int id = rs.getInt(1);
		rs.close();
		ps.close();
		return id;
	}

	public static void delete(Long id) throws SQLException {
		Connection conn = DriverManager.getConnection(protocol + dbName + ";create=true");
		String sql = "delete from whore where id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setLong(1, id);
		ps.execute();
		ps.close();
		conn.close();
	}

	public static void delete() throws SQLException {
		Connection conn = DriverManager.getConnection(protocol + dbName + ";create=true");
		String sql = "delete from whore";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.execute();
		ps.close();
		conn.close();
	}

	public static void createTable2() throws SQLException {
		Connection conn = DriverManager.getConnection(protocol + dbName + ";create=true");
		Statement st = conn.createStatement();
		String sql = "create table whore2(id bigint  primary key,obj blob)";
		st.execute(sql);
		st.close();
		conn.close();
	}

	public static void insertBatch2(List<Record> list) throws SQLException, IOException {
		for (Record rec : list) {
			insert2(rec);
		}
	}

	/**
	 * 插入需要更新WRITENEXTID和ROWSNUM两个变量
	 * 
	 * @param obj
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void insert2(Record obj) throws SQLException, IOException {
		Connection conn = DriverManager.getConnection(protocol + dbName + ";create=true");
		String sql = "insert into whore2(id,obj) values(?,?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ByteArrayOutputStream saos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(saos);
		oos.writeObject(obj);
		oos.flush();
		ps.setLong(1, Constant.WRITENEXTID.getAndIncrement());
		ps.setBytes(2, saos.toByteArray());
		ps.execute();
		oos.close();
		saos.close();
		ps.close();
		conn.close();
		Constant.ROWSNUM.getAndIncrement();
	}

	/**
	 * get方法需要更新ROWSNUM和READNEXTID两个变量
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Pair get2() throws SQLException, IOException, ClassNotFoundException {
		Connection conn = DriverManager.getConnection(protocol + dbName + ";create=true");
		String sql = "select * from whore2 where id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setLong(1, Constant.READNEXTID.getAndIncrement());
		ResultSet rs = ps.executeQuery();
		Record record = null;
		long id = 0;
		if (rs.next()) {
			id = rs.getLong(1);
			byte[] bytes = rs.getBytes(2);
			ByteArrayInputStream sais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(sais);
			record = (Record) ois.readObject();
			ois.close();
			sais.close();
		}
		rs.close();
		ps.close();
		conn.close();
		deleteCurrent2();
		Constant.ROWSNUM.getAndDecrement();
		return new Pair(id, record);
	}

	private static void deleteCurrent2() throws SQLException {
		Connection conn = DriverManager.getConnection(protocol + dbName + ";create=true");
		String sql = "delete from whore2 where id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setLong(1, Constant.READNEXTID.get() - 1);
		ps.execute();
		ps.close();
		conn.close();
	}

	public static int getSize2() throws SQLException, IOException, ClassNotFoundException {
		Connection conn = DriverManager.getConnection(protocol + dbName + ";create=true");
		String sql = "select count(*) from whore2";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		rs.next();
		int id = rs.getInt(1);
		rs.close();
		ps.close();
		conn.close();
		return id;
	}

	public static void delete2(Long id) throws SQLException {
		Connection conn = DriverManager.getConnection(protocol + dbName + ";create=true");
		String sql = "delete from whore2 where id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setLong(1, id);
		ps.execute();
		ps.close();
		conn.close();
	}

	public static void delete2() throws SQLException {
		Connection conn = DriverManager.getConnection(protocol + dbName + ";create=true");
		String sql = "delete from whore2";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.execute();
		ps.close();
		conn.close();
	}

	public static Pair getLast2() throws SQLException, IOException, ClassNotFoundException {
		Connection conn = DriverManager.getConnection(protocol + dbName + ";create=true");
		String sql = "select * from whore2 order by id desc";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		Record record = null;
		long id = 0;
		if (rs.next()) {
			id = rs.getLong(1);
			byte[] bytes = rs.getBytes(2);
			ByteArrayInputStream sais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(sais);
			record = (Record) ois.readObject();
			ois.close();
			sais.close();
		}
		rs.close();
		ps.close();
		conn.close();
		return new Pair(id, record);
	}

	public static Pair getFirst2() throws SQLException, IOException, ClassNotFoundException {
		Connection conn = DriverManager.getConnection(protocol + dbName + ";create=true");
		String sql = "select * from whore2";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		Record record = null;
		long id = 0;
		if (rs.next()) {
			id = rs.getLong(1);
			byte[] bytes = rs.getBytes(2);
			ByteArrayInputStream sais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(sais);
			record = (Record) ois.readObject();
			ois.close();
			sais.close();
		}
		rs.close();
		ps.close();
		conn.close();
		return new Pair(id, record);
	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
		 DerbyUtil t = new DerbyUtil();
//		 loadDriver();
		 DerbyUtil.createTable2();
//		 t.doIt();

		// Integer in = new Integer(1);
		// Record rec = new Record();
		// rec.fields.add(in);

		// t.insert(rec);
		// System.out.println(t.get(new Long(1)));

		// List<Pair> list = t.getAll();
		// System.out.println(list);
//		System.out.println(DerbyUtil.getSize2());
//		DerbyUtil.delete2();

		// t.createTable2();

		// DerbyUtil.delete2();
		//
		// for (int i = 0; i < 10; i++) {
		// List<Object> list = new ArrayList<>();
		// list.add(new Integer(i));
		// DerbyUtil.insert2(new Record(list));
		// if (DerbyUtil.getLast2().id == Constant.WRITENEXTID.get() - 1) {
		// System.out.println("writenextid is okay!");
		// }
		// if (DerbyUtil.getSize2() == Constant.ROWSNUM.get()) {
		// System.out.println("rownum in write is okay!");
		// }
		// }
		// System.out.println(DerbyUtil.getSize2());
		// for (int i = 0; i < 10; i++) {
		// Pair r = DerbyUtil.get2();
		// if (Constant.READNEXTID.get() == DerbyUtil.getFirst2().id) {
		// System.out.println("you are equal!");
		// }
		// if (DerbyUtil.getSize2() == Constant.ROWSNUM.get()) {
		// System.out.println("rownum in read is okay!");
		// }
		// }

	}
}