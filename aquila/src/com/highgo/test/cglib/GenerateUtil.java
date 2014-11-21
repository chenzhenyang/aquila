package com.highgo.test.cglib;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class GenerateUtil {
	public static List<Object> generateObjectListFromDB(Class<?> cls, ResultSet rs) {
		System.out.println(cls.getName());
		List<Object> list = new LinkedList<Object>();
		Field[] fields = cls.getDeclaredFields();
		try {
			while (rs.next()) {
				Object ob = cls.newInstance();
				for (int i = 0; i < fields.length; i++) {
					String fieldName = fields[i].getName().replace("$cglib_prop_", "");
					String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					Method setMethod = cls.getDeclaredMethod(setMethodName, new Class[] { fields[i].getType() });
					setMethod.invoke(ob, rs.getObject(fieldName));
				}
				list.add(ob);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}