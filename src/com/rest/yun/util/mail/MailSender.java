package com.rest.yun.util.mail;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

/**
 * @project: yun
 * @Title: MailSender.java
 * @Package com.rest.yun.util.mail
 * @Description: 用于用户找回密码，发送验证邮件
 * @author 杨贵松
 * @date 2014年4月16日 下午12:08:45
 * @version V1.0
 */
public class MailSender {
	private static final Logger log = Logger.getLogger(MailSender.class.getName());

	private MimeMessage mimeMsg; // MIME邮件对象

	private Session session; // 邮件会话对象

	private Properties props; // 系统属性

	private String username = ""; // smtp认证用户名和密码

	private String password = "";

	private Multipart mp; // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象

	public MailSender() throws Exception{
		createMimeMessage();
	}

	public MailSender(String smtp) throws Exception{
		setSmtpHost(smtp);
		createMimeMessage();
	}

	public void setSmtpHost(String hostName) throws Exception{
		if (props == null)
			props = System.getProperties(); // 获得系统属性对象
		props.put("mail.smtp.host", hostName); // 设置SMTP主机
	}

	/**
	 * @Title: 				createMimeMessage 
	 * @author 				杨贵松
	 * @date 				2014年4月18日 下午3:49:39
	 * @Description: 		获得邮件会话对象、创建MIME邮件对象
	 * @return 
	 * boolean 				返回
	 */
	public boolean createMimeMessage() {
		try {
			session = Session.getDefaultInstance(props, null); // 获得邮件会话对象
		} catch (Exception e) {
			log.error("获取邮件会话对象时发生错误！" + e);
			return false;
		}

		try {
			mimeMsg = new MimeMessage(session); // 创建MIME邮件对象
			mp = new MimeMultipart();
			return true;
		} catch (Exception e) {
			log.error("创建MIME邮件对象失败！" + e);
			return false;
		}
	}

	public void setNeedAuth(boolean need) throws Exception{
		if (props == null)
			props = System.getProperties();
		if (need) {
			props.put("mail.smtp.auth", "true");
			props.put("mail.transport.protocol", "smtp");
		} else {
			props.put("mail.smtp.auth", "false");
		}
	}

	public void setNamePass(String name, String pass) throws Exception{
		username = name;
		password = pass;
	}

	/**
	 * @Title: 				setSubject 
	 * @author 				杨贵松
	 * @date 				2014年4月18日 下午3:49:20
	 * @Description: 		设置邮件主题
	 * @param mailSubject
	 * @return 
	 * boolean 				返回
	 */
	public boolean setSubject(String mailSubject) { 
		try {
			mimeMsg.setSubject(mailSubject);
			return true;
		} catch (Exception e) {
			log.error("设置邮件主题发生错误！");
			return false;
		}
	}

	/**
	 * @Title: 				setBody 
	 * @author 				杨贵松
	 * @date 				2014年4月18日 下午3:49:09
	 * @Description: 		增加邮件正文
	 * @param mailBody
	 * @return 
	 * boolean 				返回
	 */
	public boolean setBody(String mailBody) { 
		try {
			BodyPart bp = new MimeBodyPart();
			bp.setContent("" + mailBody, "text/html;charset=GB2312");
			mp.addBodyPart(bp);
			return true;
		}catch (Exception e) {
			log.error("设置邮件正文时发生错误！" + e);
			return false;
		}
	}

	//
	// public boolean addFileAffix(String filename){//增加附件
	//
	// System.out.println("增加邮件附件："+filename);
	//
	// try{
	// BodyPart bp = new MimeBodyPart();
	// FileDataSource fileds = new FileDataSource(filename);
	// bp.setDataHandler(new DataHandler(fileds));
	// bp.setFileName(fileds.getName());
	// mp.addBodyPart(bp);
	// return true;
	// }
	// catch(Exception e){
	// System.err.println("增加邮件附件："+filename+"发生错误！"+e);
	// return false;
	// }
	// }

	/**
	 * @Title: 				setFrom 
	 * @author 				杨贵松
	 * @date 				2014年4月18日 下午3:48:56
	 * @Description: 		设置发信人地址
	 * @param from
	 * @return 
	 * boolean 				返回
	 */
	public boolean setFrom(String from) {  
		try {
			String nick= javax.mail.internet.MimeUtility.encodeText("Rainet云灌溉");
			mimeMsg.setFrom(new InternetAddress(from,nick));
			return true;
		}catch (Exception e) {
			log.error("设置发信人地址错误！"+e);
			return false;
		}

	}

	/**
	 * @Title: 				setTo 
	 * @author 				杨贵松
	 * @date 				2014年4月18日 下午3:48:24
	 * @Description: 		设置接收地址
	 * @param to
	 * @return 
	 * boolean 				返回
	 */
	public boolean setTo(String to)
	{
		if (to == null)
			return false;
		try {
			mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			return true;
		}catch (Exception e) {
			log.error("设置发送地址错误!"+e);
			return false;
		}
	}

	/**
	 * @Title: 				setCopyTo 
	 * @author 				杨贵松
	 * @date 				2014年4月18日 下午3:48:10
	 * @Description: 		设置抄送地址
	 * @param copyto
	 * @return 
	 * boolean 				返回
	 */
	public boolean setCopyTo(String copyto)
	{
		if (copyto == null)
			return false;
		try {
			mimeMsg.setRecipients(Message.RecipientType.CC, (Address[]) InternetAddress.parse(copyto));
			return true;
		} catch (Exception e) {
			log.error("设置抄送地址错误!"+e);
			return false;
		}
	}

	/**
	 * @Title: 				sendout 
	 * @author 				杨贵松
	 * @date 				2014年4月18日 下午3:43:14
	 * @Description: 		发送验证邮件
	 * @return 
	 * boolean 				返回
	 */
	public boolean sendout() {
		try {
			log.info("正在发送邮件...");
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();

			MyAuthenticator authenticator = new MyAuthenticator(username, password);
			Session mailSession = Session.getInstance(props, authenticator);
			Transport transport = mailSession.getTransport("smtp");
			transport.connect((String) props.get("mail.smtp.host"), username, password);
			transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));

			log.info("发送邮件成功！");
			transport.close();
			return true;

		} catch (Exception e) {
			log.error("邮件发送异常！" + e);
			return false;
		}
	}

	/**
	 * @Title: 				iForgetPassword 
	 * @author 				杨贵松
	 * @date 				2014年4月18日 下午3:47:05
	 * @Description: 		初始化找回密码验证邮件 
	 * @param toEmail
	 * @param urlStr
	 * @param userName
	 * @param outDate
	 * @return 
	 * boolean 				返回
	 */
	public static boolean iForgetPassword(String toEmail,String urlStr,String userName,String outDate) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("<div style=\"font-family: '微软雅黑,宋体';margin:50px;width:850px;font-size: 14px; border: #ccc solid 1px;\">"+
				"<div style=\"height:70px; color: #999999;line-height: 60px;margin-top: 0px;background-color: #fbfbfb;font-size: 12px;border-bottom: #eeeeee 1px solid;border-top: #1b926c 1px solid;\">"+
		        "<div style=\"float:left;margin-right:15px;margin-left:15px;padding:5px;font-family: 'Copperplate Gothic Bold'; font-size: 36px; color: #000;\">Rainet</div> "+
		        "<div style=\"float:left;margin-top: 12px;\">此信为系统邮件，请不要直接回复。</div>"+
		        "<div style=\"float:right;margin-right:60px; margin-top: 12px;\"><a href=\"http://www.rainet.com.cn/contact.jsp\" style=\"font-family: 'Copperplate Gothic Bold';\"><strong><font color=\"#666666\">服务中心</font></strong></a></div>"+
		        "<div style=\"float:right;margin-right:30px; margin-top: 12px;\"><a href=\"http://www.rainet.com.cn/service.jsp\" style=\"font-family: 'Copperplate Gothic Bold';\"><strong><font color=\"#666666\">产品中心</font></strong></a></div>"+
		        "<div style=\"float:right;margin-right:30px; margin-top: 12px;\"><a href=\"http://www.rainet.com.cn\" style=\"font-family: 'Copperplate Gothic Bold';\"><strong><font color=\"#666666\">首页</font></strong></a></div>"+
		        "</div><div style=\"padding:20px;\">"+
		        "<div style=\"height:30px;line-height: 30px;margin-top: 10px;\">亲爱的 <span style=\"font-size: 18px;font-weight:700;\">"+userName+"</span> 用户：</div>"+
		        "<div style=\"height:40px;padding-left:28px;line-height: 40px;\">您好！</div>"+
		        "<div style=\"height:30px;padding-left:28px;line-height: 30px;\">您收到这封这封电子邮件是因为您 (也可能是某人冒充您的名义) 申请了一个新的密码。假如这不是您本人所申请, 请不用理会</div>"+
		        "<div style=\"height:30px;line-height: 30px;\">这封电子邮件, 但是如果您持续收到这类的信件骚扰, 请您尽快联络管理员。</div>"+
		        "<div style=\"height:30px;padding-left:28px;line-height: 30px;\">要使用新的密码,请点击以下链接启用密码:</div>"+
		        "<div style=\"height:60px;text-align:center;line-height: 60px;\"><a href=\""+urlStr+"\" style=\"font-family: 'Copperplate Gothic Bold'; font-size: 20px; color: #1b926c;\">点击我重设密码</a></div>"+
		        "<div style=\"height:30px;padding-left:28px;line-height: 30px;\">(如果无法点击该URL链接地址，请将它复制并粘帖到浏览器的地址输入框，然后单击回车即可。该链接使用后将立即失效。)</div>"+
		        "<div style=\"height:30px;line-height: 30px;padding-left:28px;\">注意:请您在收到邮件1个小时内( <span style=\"font-size: 18px;font-weight:700;\">"+outDate+"</span> 前 )使用，否则该链接将会失效。想了解更多云灌溉信息，请访问</div>"+
		        "<div style=\"height:30px;line-height: 30px;\"> <a href=\"http://www.rainet.com.cn\" style=\"font-family: 'Copperplate Gothic Bold'; font-size: 16px; color: #1b926c;\">官网首页</a>或者登录 <a href=\"http://yun.rainet.com.cn\" style=\"font-family: 'Copperplate Gothic Bold'; font-size: 16px; color: #1b926c;\">云灌溉系统</a>。</div>"+
		        "<div style=\"height:1px;margin-top:20px;margin-bottom:20px;border-top: #e7e7e7 solid 1px;\"></div>"+
		        "<div style=\"height:30px;padding-left:28px;line-height: 30px;\">锐利特科技将一如既往、热忱的为您服务！</div>"+
		        "<div style=\"height:30px;padding-left:28px;line-height: 30px;\">与您携手共创智慧生活，开启未来的世界!</div>"+
		        "<div style=\"height:30px;padding-left:28px;line-height: 30px;margin-bottom: 30px;\">用户服务支持：<a href=\"mailto:service@rainet.com.cn\"><strong><font color=\"#666666\">service@rainet.com.cn</font></strong></a></div>"+
				"</div></div>");
		String mailbody = sb.toString();
		// 设置SMTP主机
		MailSender themail = new MailSender("115.28.143.115");
		themail.setNeedAuth(true);
		// 设置邮件标题
		if (themail.setSubject("Rainet云灌溉找回密码") == false)
			return false;
		// 设置邮件内容
		if (themail.setBody(mailbody) == false)
			return false;
		// 设置收件人的邮箱
		if (themail.setTo(toEmail) == false)
			return false;
		// 设置发件人的邮件
		if (themail.setFrom("service@rainet.com.cn") == false)
			return false;
		// 设置发件人的邮件的用户名密码
		themail.setNamePass("service", "rainet2014");
		// 设置发送的附件
		// themail.addFileAffix("E:\\ftp\\cca\\Test.java");
		if (themail.sendout() == false)
			return false;
		return true;
	}

}
