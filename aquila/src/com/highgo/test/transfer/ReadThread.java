package com.highgo.test.transfer;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ReadThread implements Runnable{

	private Buffer buffer;
	
	public ReadThread(Buffer buffer) {
		this.buffer=buffer;
	}
	
	@Override
	public void run() {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			Date start =new  Date();
			Connection conn = cpds.getConnection();
			String sql="select * from measurement";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				buffer.put(new Record(rs));
			}  
//			Lock.isComeOn.set(false);
			Date end =new  Date();
			String result = "Thread:"+Thread.currentThread().getName()+" time:"+(end.getTime()-start.getTime())/1000;
			System.out.println(result);//†ÎÎ»£ºÃë
			try {
				writeToFile(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SQLException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeToFile(String str) throws IOException {
		FileWriter fw = new FileWriter("hello.txt");  
		fw.write(str);  
		fw.flush(); 
		fw.close();
	}
	
	public static void main(String[] args) {
		Buffer buffer =new Buffer();
//		GetSizeThread gst = new GetSizeThread(buffer);
//		new Thread(gst).start();
		ReadThread rt = new ReadThread(buffer);
		rt.run();
//		
//		WriteThread wt = new WriteThread(buffer);
//		wt.run();
//		try {
//			writeToFile("t");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
