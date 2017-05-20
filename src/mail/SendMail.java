package mail;


import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;




public class SendMail {
	//收件人邮箱地�??
	private String to;
	//发件人邮箱地�?
	private String from;
	//SMTP服务器地�?
	private String smtpServer;
	//登录SMTP服务器的用户�?
	private String username;
	//登录SMTP服务器的密码
	private String password;
	//邮件主题
	private String subject;
	//邮件正文
	private String content;
	//记录�?��附件文件的集�?
	List<String> attachments=new ArrayList<String>();
	//有参构�?�?
	public SendMail(String to, String from, String smtpServer, String username,String password, String subject, String content) {
		this.to = to;
		this.from = from;
		this.smtpServer = smtpServer;
		this.username = username;
		this.password = password;
		this.subject = subject;
		this.content = content;
	}
	//无参构�?�?
	public SendMail() {}
	
	
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSmtpServer() {
		return smtpServer;
	}
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	//把邮件主题转换成中文
	public String transferChinese(String strText){
		try {
			strText=MimeUtility.encodeText(new String(strText.getBytes()),"GB2312","B");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return strText;
	}
	
	//增加附件，将附件文件名添加到List集合�?
	public void attachfile(String fname){
		attachments.add(fname);
	}
	
	//发�?邮件
	public boolean send(){
		//创建邮件session�?��的properties对象
		Properties props=new Properties();
		//�?��ssl加密
		//MailSSLSocketFactory sf;
	
			//sf = new MailSSLSocketFactory();
			//sf.setTrustAllHosts(true);
			props.put("mail.smtp.ssl.enable", "true");
			//props.put("mail.smtp.ssl.socketFactory", sf);
			props.put("mail.smtp.host", smtpServer);
			props.put("mail.smtp.auth", "true");
	

	
		//创建session对象
		Session session=Session.getDefaultInstance(props
				//以匿名内部类的形式创建登录服务器的认证对�?
				,new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication(){
				return new PasswordAuthentication(username, password);
			}
		});
		
		try {
			//构�?MimeMessage并设置相关属性�?
			MimeMessage msg=new MimeMessage(session);
			//设置发件�?
			msg.setFrom(new InternetAddress(from));
			//设置收件�?
			InternetAddress addresses[]={new InternetAddress(to)};
			msg.setRecipients(Message.RecipientType.TO, addresses);
			//设置邮件主题
			subject=transferChinese(subject);
			msg.setSubject(subject);
			//构�?Multipart
			Multipart mp=new MimeMultipart();
			//向Mulipart添加正文
			MimeBodyPart mbpContent=new MimeBodyPart();
			mbpContent.setText(content);
			//将BodyPart添加到Multipart容器�?
			mp.addBodyPart(mbpContent);
			//向Multipart添加附件
			//遍历附件列表，将�?��文件添加到邮件消息里
			for (String efile : attachments) {
				MimeBodyPart mbpFile=new MimeBodyPart();
				//以文件名创建FileDataSource对象
				FileDataSource fds=new FileDataSource(efile);
				//处理附件
				mbpFile.setDataHandler(new DataHandler(fds));
				mbpFile.setFileName(fds.getName());
				//将BodyPart添加到Multipart容器�?
				mp.addBodyPart(mbpFile);
			}
			//清空附件列表
			attachments.clear();
			//向Multipart添加MimeMessage
			msg.setContent(mp);
			//设置发�?日期
			msg.setSentDate(new Date());
			//发�?邮件
			Transport.send(msg);
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
/**
 * QQ账号发�?邮箱
 */
public void QQMail(){
	SendMail sendMail=new SendMail();
	//设置SMTP服务器地�?
	sendMail.setSmtpServer("smtp.qq.com");
	//此处设置登录的名�?
	sendMail.setUsername("875079028");
	//此处设置登录密码
	sendMail.setPassword("gffcqcqobsukbdjh");
	//设置收件人地�?
	sendMail.setTo("1745822665@qq.com");
	//设置发件人地�?
	sendMail.setFrom("875079028@qq.com");
	//设置标题
	sendMail.setSubject("这里填写邮件标题");
	//设置内容
	sendMail.setContent("这里填写邮件内容");
	//添加附件，可以添加多�?填写附件路径，没有可以不�?
	//sendMail.attachfile("src/lee/SendMail.java");
	//sendMail.attachfile("build.xml");
	
	if(sendMail.send()){
		System.out.println("----------邮件发�?成功�?--------");
	}else{
		System.out.println("----------邮件发�?失败�?--------");
	}
}
	
/**
 * 网易邮箱发�?邮件
 * @param setTo 收件人邮�?
 * @param title 邮箱标题
 * @param content 邮箱内容
 */
public void wangyiMail(String setTo,String title,String content){
	//设置SMTP服务器地�?
	this.setSmtpServer("smtp.163.com");
	//此处设置登录的名�?
	this.setUsername("tracingSystem@163.com");
	//此处设置登录密码
	this.setPassword("ts1234");
	//设置收件人地�?
	this.setTo(setTo);
	//设置发件人地�?
	this.setFrom("tracingSystem@163.com");
	//设置标题
	this.setSubject(title);
	//设置内容
	this.setContent(content);
	//添加附件，可以添加多�?填写附件路径，没有可以不�?
	//sendMail.attachfile("src/lee/SendMail.java");
	//sendMail.attachfile("build.xml");
	
	if(this.send()){
		System.out.println("----------邮件发�?成功�?--------");
	}else{
		System.out.println("----------邮件发�?失败�?--------");
	}
	
}


 }




