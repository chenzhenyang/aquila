package com.highgo.test.cglib;

import java.sql.Connection;
import java.util.List;

public interface ObjectDao {
	List<Object> getObjectList(Connection conn, String tableName);
}
