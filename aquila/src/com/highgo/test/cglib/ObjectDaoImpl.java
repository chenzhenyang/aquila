package com.highgo.test.cglib;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectDaoImpl implements ObjectDao {
	@Override
	public List<Object> getObjectList(Connection conn, String tableName) {
		Map<String, Class<?>> columnMap = new HashMap<>();
//		CglibBeanUtil beanUtil = null;
		String sql = "select * from " + tableName;
		Object ob = null;
		try {
			ResultSetMetaData rs = conn.prepareStatement(sql).executeQuery().getMetaData();
			for (int i = 1; i <= rs.getColumnCount(); i++) {
				columnMap.put(rs.getColumnName(i), Class.forName(rs.getColumnClassName(i)));
			}
			ob = new CglibBeanUtil(columnMap).getObject();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return this.getObjectList(conn, ob, tableName);
	}

	private List<Object> getObjectList(Connection conn, Object ob, String tableName) {
		String sql = "select * from " + tableName;
		ResultSet rs = null;
		List<Object> list = null;
		try {
			rs = conn.prepareStatement(sql).executeQuery();
			list = GenerateUtil.generateObjectListFromDB(ob.getClass(), rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// DBUtil.closeConnection(conn);
		}
		return list;
	}
}
