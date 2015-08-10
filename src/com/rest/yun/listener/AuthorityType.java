package com.rest.yun.listener;
/**
 * @project:					yun 
 * @Title: 						AuthorityType.java 		
 * @Package 					com.rest.yun.listener		
 * @Description: 				权限枚举列表
 * @author 						杨贵松   
 * @date 						2014年2月20日 下午11:54:39 
 * @version 					V1.0
 */

public enum AuthorityType {
	// 包含了枚举的中文名称, 枚举的索引值
	ADMINISTRATOR("超级管理员", 0),
	NORMAL_USER("普通用户", 1),;
	private String name;
	private int index;

	private AuthorityType(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}