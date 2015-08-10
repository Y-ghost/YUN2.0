package com.rest.yun.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SQLUtils {

	public static final String ORDER_BY_REGEX = "order\\s*by[\\w|\\W|\\s|\\S]*";

	public static String removeOrders(String sql) {
		Pattern p = Pattern.compile(ORDER_BY_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

}
