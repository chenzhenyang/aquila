package com.highgo.test.transfer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Record {

	private int id;
	private String field1;
	private String field2;
	private String field3;
	private String field4;
	private String field5;
	private String field6;
	private String field7;

	public Record() {
		// TODO Auto-generated constructor stub
	}
	
	public Record(String field1,String field2,String field3,String field4,String field5,String field6,String field7){
		this.field1= field1;
		this.field2= field2;
		this.field3= field3;
		this.field4= field4;
		this.field5= field5;
		this.field6= field6;
		this.field7= field7;
	}

	public Record(ResultSet rs) throws SQLException {
		this.id = rs.getInt(1);
		this.field1 = rs.getString(2);
		this.field2 = rs.getString(3);
		this.field3 = rs.getString(4);
		this.field4 = rs.getString(5);
		this.field5 = rs.getString(6);
		this.field6 = rs.getString(7);
		this.field7 = rs.getString(8);
	}

	public void write(PreparedStatement ps) throws SQLException {
		ps.setString(1, this.field1);
		ps.setString(2, this.field2);
		ps.setString(3, this.field3);
		ps.setString(4, this.field4);
		ps.setString(5, this.field5);
		ps.setString(6, this.field6);
		ps.setString(7, this.field7);
		ps.addBatch();
	}
}
