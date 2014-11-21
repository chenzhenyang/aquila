package com.highgo.hgdbadmin.vthread;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Record implements Serializable {
	public List<Object> fields;

	public Record() {
		this.fields = new ArrayList<>();
	}

	public Record(List<Object> fields) {
		this.fields = fields;
	}

	public Record(ResultSet rs, int size) throws SQLException {
		fields = new ArrayList<>(size);
		for (int i = 1; i <= size; i++) {
			fields.add(rs.getObject(i));
		}
	}

	public Record(ResultSet rs) throws SQLException {
		fields = new ArrayList<>(Lock.fieldNum4SomeTable.get());
		for (int i = 1; i <= Lock.fieldNum4SomeTable.get(); i++) {
			fields.add(rs.getObject(i));
		}
	}

	public boolean write(PreparedStatement ps) throws SQLException {
		for (int i = 1; i <= Lock.fieldNum4SomeTable.get(); i++) {
			ps.setObject(i, fields.get(i - 1));
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Object o : fields) {
			sb.append(o + " ");
		}
		return sb.toString();
	}
}
