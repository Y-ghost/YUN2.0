package com.rest.yun.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

public class JSONConver {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	private final static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static {
		objectMapper.setDateFormat(df);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T conver(String value, Class<?> clazz) {
		T result = null;
		try {
			result = (T) objectMapper.readValue(value, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T conver(Map<String, Object> map, Class<?> clazz) {
		T result = null;
		try {
			String value = objectMapper.writeValueAsString(map);
			result = (T) objectMapper.readValue(value, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public static String converToJson(Object value) {
		String result = null;
		try {
			result = objectMapper.writeValueAsString(value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> loadJson(String filePath) {
		InputStream in = JSONConver.class.getResourceAsStream(filePath);
		try {
			return objectMapper.readValue(in, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// Do noting
				e.printStackTrace();
			}
		}
	}

}
