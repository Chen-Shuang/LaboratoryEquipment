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




public class SendMail implements Runnable {
	//收件人邮箱地址
	private String to;
	//发件人邮箱地址
	private String from;
	//SMTP服务器地址
	private String smtpServer;
	//登录SMTP服务器的用户名
	private String username;
	//登录SMTP服务器的密码
	private String password;
	//邮件主题
	private String subject;
	//邮件正文
	private String content;
	//记录所有附件文件的集合
	List<String> attachments=new ArrayList<String>();
	/**
	 * 多参构造器
	 * @param to 
	 * @param from
	 * @param smtpServer
	 * @param username
	 * @param password
	 * @param subject
	 * @param content
	 */
	public SendMail(String to, String from, String smtpServer, String username,String password, String subject, String content) {
		this.to = to;
		this.from = from;
		this.smtpServer = smtpServer;
		this.username = username;
		this.password = password;
		this.subject = subject;
		this.content = content;
	}
	
	/**
	 * 无参构造器
	 */
	public SendMail() {}
	
	/**
	 * 有参构造器
	 * @param to 收件人邮箱地址
	 * @param subject 邮箱主题
	 * @param content 邮箱内容
	 */
	public SendMail(String to,String subject, String content) {
		this.to = to;
		this.subject = subject;
		this.content = content;
	}
	
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
	
	/**
	 * 把邮件主题转换成中文
	 * @param strText 需要转换的内容
	 * @return 返回转换过的内容
	 */
	public String transferChinese(String strText){
		try {
			strText=MimeUtility.encodeText(new String(strText.getBytes()),"GB2312","B");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return strText;
	}
	
	/**
	 * 增加附件， 可以添加多个，将附件文件名添加到List集合中，没有可以不写
	 * @param fname 附件路径
	 */
	public void attachfile(String fname){
		attachments.add(fname);
	}
	
	/**
	 * 发送邮件
	 * @return true/false
	 */
	public boolean send(){
		//创建邮件session所需的properties对象
		Properties props=new Properties();
		//开启ssl加密
		//MailSSLSocketFactory sf;
	
			//sf = new MailSSLSocketFactory();
			//sf.setTrustAllHosts(true);
			props.put("mail.smtp.ssl.enable", "true");
			//props.put("mail.smtp.ssl.socketFactory", sf);
			props.put("mail.smtp.host", smtpServer);
			props.put("mail.smtp.auth", "true");
	

	
		//创建session对象 
		Session session=Session.getDefaultInstance(props, new Authenticator() {
			//以匿名内部类的形式创建登录服务器的认证对象
			public PasswordAuthentication getPasswordAuthentication(){
				return new PasswordAuthentication(username, password);
			}
		});
		
		try {
			//构造MimeMessage并设置相关属性值
			MimeMessage msg=new MimeMessage(session);
			//设置发件人
			msg.setFrom(new InternetAddress(from));
			//设置收件人
			InternetAddress addresses[]={new InternetAddress(to)};
			msg.setRecipients(Message.RecipientType.TO, addresses);
			//设置邮件主题
			subject=transferChinese(subject);
			msg.setSubject(subject);
			//构造Multipart
			Multipart mp=new MimeMultipart();
			//向Mulipart添加正文
			MimeBodyPart mbpContent=new MimeBodyPart();
			mbpContent.setText(content);
			//将BodyPart添加到Multipart容器中
			mp.addBodyPart(mbpContent);
			//向Multipart添加附件
			//遍历附件列表，将所有文件添加到邮件消息里
			for (String efile : attachments) {
				MimeBodyPart mbpFile=new MimeBodyPart();
				//以文件名创建FileDataSource对象
				FileDataSource fds=new FileDataSource(efile);
				//处理附件
				mbpFile.setDataHandler(new DataHandler(fds));
				mbpFile.setFileName(fds.getName());
				//将BodyPart添加到Multipart容器中
				mp.addBodyPart(mbpFile);
			}
			//清空附件列表
			attachments.clear();
			//向Multipart添加MimeMessage
			msg.setContent(mp);
			//设置发送日期
			msg.setSentDate(new Date());
			//发送邮件
			Transport.send(msg);
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
/**
 * 调用邮箱发送邮件例子
 */
public void sendMailContent(){
	SendMail sendMail=new SendMail();
	//设置SMTP服务器地址[QQ邮箱服务器地址：smtp.qq.com；网易邮箱服务器地址：smtp.163.com]
	setSmtpServer("SMTP服务器地址");
	//此处设置登录的名称
	setUsername("发送邮件名称");
	//此处设置登录密码
	setPassword("发送邮件的授权码");
	//设置收件人地址
	setTo("收件人地址");
	//设置发件人地址
	setFrom("发件人地址");
	//设置标题
	setSubject("邮件标题");
	//设置内容
	setContent("邮件内容");
	//添加附件，可以添加多个,填写附件路径，没有可以不写,
	sendMail.attachfile("src/lee/SendMail.java");
	sendMail.attachfile("build.xml");
	
	if(send()){
		System.out.println("----------邮件发送成功！---------");
	}else{
		System.out.println("----------邮件发送失败！---------");
	}
	
}

/**
 * 实现run方法
 */
@Override
public void run() { // 由于整个系统用一个固定的网易邮箱发送邮件，所以这里将发送邮件的邮箱信息初始化
	//设置SMTP服务器地址
	this.setSmtpServer("smtp.163.com");
	//此处设置登录的名称
	this.setUsername("zzu_lem@163.com");
	//此处设置登录密码
	this.setPassword("zzu123");
	//设置收件人地址
	this.setTo(to);
	//设置发件人地址
	this.setFrom("zzu_lem@163.com");
	//设置标题
	this.setSubject(subject);
	//设置内容
	this.setContent(content);
	//添加附件，可以添加多个,填写附件路径，没有可以不写,
	//sendMail.attachfile("src/lee/SendMail.java");
	//sendMail.attachfile("build.xml");
		
	if(this.send()){
		System.out.println("----------邮件发送成功！---------");
	}else{
		System.out.println("----------邮件发送失败！---------");
	}
	
}


 }




