package com.rest.yun.util.mail;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

/**
 * @project:					yun 
 * @Title: 						ReceiveMail.java 		
 * @Package 					com.rest.yun.util.mail		
 * @Description: 				使用Javamail实现邮件接收功能
 * @author 						杨贵松   
 * @date 						2014年4月16日 下午8:19:21 
 * @version 					V1.0
 */
public class ReceiveMail {
	public ReceiveMail() {
	}

	public static void main(String[] args) throws IOException {
		// 初始化主机
		String host = "rainet.com.cn";
		String username = "service";
		String password = "rainet2014";
		// 配置服务器属性
		Properties props = new Properties();
		props.put("mail.smtp.host", host); // smtp服务器
		props.put("mail.smtp.auth", "true"); // 是否smtp认证
		props.put("mail.smtp.port", "25"); // 设置smtp端口
		props.put("mail.transport.protocol", "smtp"); // 发邮件协议
		props.put("mail.store.protocol", "pop3"); // 收邮件协议
		// 获取会话
		Session session = Session.getDefaultInstance(props, null);
		// 获取Store对象，使用POP3协议，也可能使用IMAP协议
		try {
			Store store = session.getStore("pop3");
			// 连接到邮件服务器
			store.connect(host, username, password);
			// 获取该用户Folder对象，并以只读方式打开
			Folder folder = store.getFolder("inbox");
			folder.open(Folder.READ_ONLY);
			// 检索所有邮件，按需填充
			Message message[] = folder.getMessages();
			StringBuffer bodytext = new StringBuffer();//存放邮件内容 
			for (int i = 0; i < message.length; i++) {
				System.out.println(i + ":" + message[i].getFrom()[0] + "\t" + message[i].getSubject() + "\n");
				Part part = message[i];
				if (part.isMimeType("text/plain")) {   
		            bodytext.append((String) part.getContent());   
		        } else if (part.isMimeType("text/html")) {   
		            bodytext.append((String) part.getContent());   
		        } else if (part.isMimeType("multipart/*")) {   
		            Multipart multipart = (Multipart) part.getContent();   
		            int counts = multipart.getCount();   
		            for (int j = 0; j < counts; j++) {   
		                getMailContent(multipart.getBodyPart(j));   
		            }   
		        } else if (part.isMimeType("message/rfc822")) {   
		            getMailContent((Part) part.getContent());   
		        } else {}
				// 打印出每个邮件的发件人和主题
			}
			folder.close(false);
			store.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getMailContent(Part part) throws Exception{
		StringBuffer bodytext = new StringBuffer();//存放邮件内容 
		if (part.isMimeType("text/plain")) {   
            bodytext.append((String) part.getContent());   
        } else if (part.isMimeType("text/html")) {   
            bodytext.append((String) part.getContent());   
        } else if (part.isMimeType("multipart/*")) {   
            Multipart multipart = (Multipart) part.getContent();   
            int counts = multipart.getCount();   
            for (int j = 0; j < counts; j++) {   
                getMailContent(multipart.getBodyPart(j));   
            }   
        } else if (part.isMimeType("message/rfc822")) {   
            getMailContent((Part) part.getContent());   
        } else {}
		System.out.println(bodytext.toString());
	}
}