package com.rest.yun.util.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @project:					yun 
 * @Title: 						MyAuthenticator.java 		
 * @Package 					com.rest.yun.util.mail		
 * @Description: 				邮件的安全认证
 * @author 						杨贵松   
 * @date 						2014年4月16日 下午12:11:51 
 * @version 					V1.0
 */
public class MyAuthenticator extends Authenticator {
	String userName = null;
	String password = null;

	public MyAuthenticator() {
	}

	public MyAuthenticator(String username, String password) {
		this.userName = username;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}

}
