package com.rest.yun.mapping.dialect;

public interface Dialect {

	boolean supportsLimit();

	String getLimitSql(String sql, int offset, int limit);

	String getCountSql(String sql);

}
