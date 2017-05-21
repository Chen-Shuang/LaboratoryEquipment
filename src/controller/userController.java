package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import mail.SendMail;
import model.userLoginModel;

import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.PathKit;

/**
 * 用户管理
 * @author 陈爽
 */
public class userController extends Controller{

	public void index() {
		render("user.html");
	}
	
	/**
	 * 添加用户
	 */
	public void addUserInfo() {
		userLoginModel userLogin = getModel(userLoginModel.class,"user_login");  // 获取表单信息
		
		String pwd = generateString(6); // 生成六位随机密码
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String nowTime = df.format(new Date()).toString(); // 获取当前系统时间
		
		userLogin.set("status", 1).set("pwd", HashKit.md5(HashKit.sha1(pwd))).set("createTime", nowTime).set("updateTime", nowTime); // 添加要保存的信息
		boolean isTrue = userLogin.save(); // 保存用户信息，返回受影响行数
	    
	    if(isTrue){ // 保存成功
		    String name = getPara("user_login.name"); // 获取添加的用户姓名
		    String email = getPara("user_login.email"); // 获取添加的用户邮箱
	    	String subject = "您的邮箱已加入实验室设备管理系统";  // 定义邮件主题
	    	String content = "亲爱的" + name + "！\r\n\r\n";  // 定义邮件内容;
	    		   content += "欢迎使用实验室设备管理系统，您的邮箱已被管理员加入实验室设备管理系统，您的密码为"+pwd+"（打死都不能告诉别人）\r\n";  
	    		   content += "请点击下列链接修改您的初始密码\r\n\r\n";
	    		   content +=  "域名地址\r\n\r\n";
	    		   content +=  "郑州大学";
	    	SendMail send = new SendMail(email,subject,content); // 定义线程、邮件对象
	    	new Thread(send).start(); // 启动线程发送邮件 （发送邮件跟网速有关，如果不开启多线程，添加操作将会有延迟）
	    	
	    	renderText("success"); // 主线程返回发送成功
	    	
	    }else{ // 保存失败
	    	renderText("error");
	    }
	}
	
	 /**
	  *  定义随机的内容
	  */
	 private  final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyz";  
	 /**
	  * 生成长度为length随机数
	  */
	 private  String generateString(int length) {  
	     StringBuffer sb = new StringBuffer();  // 定义一个可变长的字符串变量
	     Random random = new Random();  // 定义随机的对象
	     for (int i = 0; i < length; i++) {  
	         sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length()))); 
	     }  
	     return sb.toString();  
	 }

}

