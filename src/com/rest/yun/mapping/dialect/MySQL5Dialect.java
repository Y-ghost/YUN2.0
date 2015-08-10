package com.rest.yun.mapping.dialect;

import com.rest.yun.util.SQLUtils;

public class MySQL5Dialect implements Dialect {

	@Override
	public boolean supportsLimit() {
		return true;
	}

	@Override
	public String getLimitSql(String sql, int offset, int limit) {
		StringBuilder stringBuilder = new StringBuilder(sql);
		stringBuilder.append(" limit ");
		if (offset > 0) {
			stringBuilder.append(offset).append(",").append(limit);
		} else {
			stringBuilder.append(limit);
		}
		return stringBuilder.toString();
	}

	@Override
	public String getCountSql(String sql) {
		String removeOrderSql = SQLUtils.removeOrders(sql);
		return "select count(0) from (" + removeOrderSql + ") as tmp_count";
	}

}
