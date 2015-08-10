package com.rest.yun.listener;

public class AuthorityHelper {

	/**
	 * @Title: 				hasAuthority 
	 * @author 				杨贵松
	 * @date 				2014年2月21日 上午12:01:58
	 * @Description: 		判断是否有权限
	 * @param akey			aString中位置的索引值,也就是权限位
	 * @param aString		权限字段,比如 11010101011101
	 * @return 
	 * boolean 				返回
	 */
	public static boolean hasAuthority(int akey, String aString) {
		if (aString == null || "".equals(aString)) {
			return false;
		}

		char value = aString.charAt(akey);
		if (value == '1') {
			return true;
		}
		return false;
	}

	/**
	 * @Title: 				makeAuthority 
	 * @author 				杨贵松
	 * @date 				2014年2月21日 上午12:47:25
	 * @Description: 		创建权限字符串
	 * @param akeys			有权限的项,比如 1,3,6,11,20
	 * @return 				权限字符串, 比如0101001001000000000
	 * String 				返回
	 */
	
	public static String makeAuthority(String akeys){
		StringBuilder sb = new StringBuilder(_RAW);
		String []akeys_s = akeys.split(",");
		for(String akey: akeys_s){
			if(null == akey || "".equals(akey)){
				continue;
			}
			int ak = Integer.parseInt(akey);
			sb.setCharAt(ak, '1');
		}

		return sb.toString();
	}
	
	/**
	 * 250个0
	 */
	final public static String _RAW = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

}