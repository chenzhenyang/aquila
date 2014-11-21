package com.highgo.hgdbadmin.log;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.highgo.hgdbadmin.vthread.Record;

public class Log {
	private static ReadWriteLock lock = new ReentrantReadWriteLock(false);
	private static Lock r = lock.readLock();
	private static Lock w = lock.writeLock();

	public static String PATH = "redo1.log";

	// public static MyObjectOutputStream out;
	// public static MyObjectInputStream in;

	public static void writeObject(Record object) throws IOException {
		w.lock();
		MyObjectOutputStream out = new MyObjectOutputStream(new FileOutputStream(PATH, true));
		out.writeObject(object);
		out.close();
		w.unlock();
	}

	public static Record readObject() throws ClassNotFoundException, IOException {
		r.lock();
		MyObjectInputStream in = new MyObjectInputStream(new FileInputStream(PATH));
		Record record = (Record) in.readObject();
		r.unlock();
		return record;
	}

	/**
	 * 将redo的Record写入文件
	 * 
	 * @param redo
	 * @throws IOException
	 */
	public static void doWrite(List<Record> redo) {
		for (Record record : redo) {
			try {
				writeObject(record);
			} catch (IOException e) {
				// TODO
				e.printStackTrace();
			}
		}
	}

	public static List<Record> doRead() {
		List<Record> list = new LinkedList<>();
		try {
			MyObjectInputStream is = new MyObjectInputStream(new FileInputStream(PATH));
			Record r = null;
			while ((r = (Record) is.readObject()) != null) {
				list.add(r);
			}
			return list;
		} catch (ClassNotFoundException | IOException e) {
			// e.printStackTrace();
			if (e instanceof EOFException) {
				return list;
			}
		}
		return list;
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Integer integ = new Integer(1);
		Record record = new Record();
		record.fields.add(integ);
		// logger.info(record);
		//
		// FileOutputStream fo = new FileOutputStream("redo1.log");
		// ObjectOutputStream so = new ObjectOutputStream(fo);
		// so.writeObject(record);

		// FileInputStream fi = new FileInputStream("redo1.log");
		// ObjectInputStream ois = new ObjectInputStream(fi);
		// Record record2 = (Record) ois.readObject();
		// System.out.println(record2.fields);

		// String str = "str";
		// record.fields.add(str);
		// so.writeObject(record);
		//
		// Record record3 = (Record) ois.readObject();
		// System.out.println(record3.fields);
		// Record record4 = (Record) ois.readObject();
		// System.out.println(record4.fields);

		// 创建一个Record对象
		// Integer integ2 = new Integer(1);
		// Record record2 = new Record();
		// record2.fields.add(integ2);
		//
		// // 创建一个Record对象
		// Integer integ3 = new Integer(2);
		// Record record3 = new Record();
		// record3.fields.add(integ3);
		//
		// // 将对象record2写入到文件
		// FileOutputStream fo = new FileOutputStream("redo2.log", true);
		// MyObjectOutputStream so = new MyObjectOutputStream(fo);
		// so.writeObject(record2);
		// so.close();
		// fo.close();
		//
		// // 将对象record3追加到文件
		// FileOutputStream fo1 = new FileOutputStream("redo2.log", true);
		// MyObjectOutputStream so1 = new MyObjectOutputStream(fo1);
		// so1.writeObject(record3);
		// so1.close();
		// fo1.close();
		//
		// // 创建一个输入流
		// FileInputStream fi = new FileInputStream("redo2.log");
		// MyObjectInputStream ois = new MyObjectInputStream(fi);
		//
		// // 读取一个对象
		// Record record21 = (Record) ois.readObject();
		// System.out.println(record21.fields);
		//
		// // 读取一个对象
		// Record record31 = (Record) ois.readObject();
		// System.out.println(record31.fields);
		// ClassLoader cl = ClassLoader.getSystemClassLoader();
		// cl.loadClass("com.highgo.hgdbadmin.log.Log");
		
		List<Record> list = doRead();
		System.out.println(list);
		
//		Record r = readObject();
//		System.out.println(r);
	}

}
