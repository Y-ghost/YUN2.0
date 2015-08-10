package com.rest.yun.mapping.dialect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;

public enum TypeNames {

	MYSQL;

	private static final Map<String, Dialect> DIALECT_MAPS = new ConcurrentHashMap<String, Dialect>();

	public static Dialect getDialectByName(String typeName) {
		TypeNames type = null;
		if (StringUtils.isEmpty(typeName)) {
			type = MYSQL;
		} else {
			try {
				type = TypeNames.valueOf(typeName.toUpperCase());
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Dialect type support: " + getAllInstanceNames());
			}
		}
		if (type == null) {
			throw new UnsupportedOperationException("Not support the [" + typeName + "] dialect");
		}
		if (DIALECT_MAPS.containsKey(type)) {
			return DIALECT_MAPS.get(type);
		}
		Dialect dialect = createDialect(type);
		DIALECT_MAPS.put(type.name(), dialect);
		return dialect;
	}

	public static void putCustomeDialect(String typeName, Dialect dialect) {
		if (StringUtils.isEmpty(typeName)) {
			throw new IllegalArgumentException("The dialect cannot be empty!");
		}
		if (DIALECT_MAPS.containsKey(typeName)) {
			throw new UnsupportedOperationException("The [" + typeName + "] dialect has existed");
		}
		DIALECT_MAPS.put(typeName, dialect);
	}

	private static Dialect createDialect(TypeNames type) {
		switch (type) {
		case MYSQL:
			return new MySQL5Dialect();
		default:
			throw new UnsupportedOperationException("Not create the [" + type + "] dialect");
		}

	}

	private static String getAllInstanceNames() {
		TypeNames[] names = TypeNames.values();
		StringBuilder sb = new StringBuilder();
		for (TypeNames type : names) {
			sb.append(type.name() + ",");
		}
		return sb.substring(0, sb.length() - 1);
	}

}
