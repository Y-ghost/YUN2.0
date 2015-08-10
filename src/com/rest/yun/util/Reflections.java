package com.rest.yun.util;

import java.lang.reflect.Field;

public class Reflections {

	public static Field findField(Class<?> clazz, String name, Class<?> type) {
		Class<?> searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			for (Field field : fields) {
				if ((name == null || name.equals(field.getName()))
						&& (type == null || type.equals(field.getType()))) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	public static Object getField(Object obj, String fieldName) {
		Field field = findField(obj.getClass(), fieldName, null);
		if (field == null) {
			throw new RuntimeException("No such field [" + fieldName + "] on " + obj.getClass());
		}
		if (!field.isAccessible()) {
			field.setAccessible(true);
		}
		try {
			return field.get(obj);
		} catch (Exception e) {
			throw new RuntimeException("Get field [" + fieldName + "] failed", e);
		}
	}

	public static void setField(Object obj, String fieldName, Object value) {
		Field field = findField(obj.getClass(), fieldName, null);
		if (field == null) {
			throw new RuntimeException("No such field [" + fieldName + "] on " + obj.getClass());
		}
		if (!field.isAccessible()) {
			field.setAccessible(true);
		}
		try {
			field.set(obj, value);
		} catch (Exception e) {
			throw new RuntimeException("Set field [" + fieldName + "] failed", e);
		}
	}

}
