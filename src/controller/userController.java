package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import mail.SendMail;
import model.userLoginModel;

import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * 用户管理
 * @author 陈爽
 */
public class userController extends Controller{

	public void index() {
		render("user.html");
	}
	
	/**
	 * 获取用户注册信息
	 */
	public void getUserInfo(){
		int curr = getParaToInt("curr"); // 获取当前页
		int size = getParaToInt("size"); // 每页显示的数量
		String search = getPara("search"); // 获取搜索的内容
		int status = getParaToInt("userStatus"); // 获得用户状态
		int role = getParaToInt("role"); // 获得用户角色
		
		Page<userLoginModel> userInfo = userLoginModel.dao.getUserInfo(curr, size, search, status, role); // 获取用户信息
		renderJson(userInfo);
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
	    	String Url = new changePwdController().changePwdUrl +HashKit.md5(HashKit.sha1(pwd));  // 修改密码地址（地址中添加了加密的密码）
		    String name = getPara("user_login.name"); // 获取添加的用户姓名
		    String email = getPara("user_login.email"); // 获取添加的用户邮箱
	    	String subject = "您的邮箱已加入实验室设备管理系统";  // 定义邮件主题
	    	String content = "亲爱的" + name + "！\r\n\r\n";  // 定义邮件内容;
	    		   content += "欢迎使用实验室设备管理系统，您的邮箱已被管理员加入实验室设备管理系统，您的密码为"+pwd+"（打死都不能告诉别人）\r\n";  
	    		   content += "请点击下列链接修改您的初始密码\r\n\r\n";
	    		   content +=  Url+"\r\n\r\n";
	    		   content +=  "郑州大学";
	    	SendMail send = new SendMail(email,subject,content); // 定义线程、邮件对象
	    	new Thread(send).start(); // 启动线程发送邮件 （发送邮件跟网速有关，如果不开启多线程，添加操作将会有延迟）
	    	
	    	renderText("success"); // 主线程返回发送成功
	    	
	    }else{ // 保存失败
	    	renderText("error");
	    }
	}
	
	/**
	 * 获取单个用户信息
	 */
	public void getOneUserInfo(){
		int id = getParaToInt("id"); // 获得用户的ID
		userLoginModel userInfo = userLoginModel.dao.getOneUserInfo(id); // 获取用户信息
		renderJson(userInfo);
	}
	
	/**
	 * 查询用户信息是否已经绑定其他注册用户
	 */
	public void isExist(){
		String email = getPara("email"); // 获取用户邮箱
		String phone = getPara("phone"); // 获取用户手机号
		int userId = getParaToInt("userId"); // 获取要修改用户的id
		
		int isExist = userLoginModel.dao.isExist(email, phone, userId); // 查询手机号、邮箱是否已存在
		
		renderJson(isExist);
	}
	
	public void updateUserInfo(){
		userLoginModel userInfo = getModel(userLoginModel.class,"user_login"); // 获取表单信息
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String nowTime = df.format(new Date()).toString(); // 获取当前系统时间
		
		userInfo.set("updateTime", nowTime); // 添加修改时间
		boolean isSuccess = userInfo.update(); // 更新用户信息
		
		renderJson(isSuccess);
	}
	
	/**
	 * 修改用户的状态
	 */
	public void changeStatusInfo(){
		int id = getParaToInt("id"); // 获得用户id
		int status = getParaToInt("status"); // 获得要修改的状态
		
		boolean isSucces = userLoginModel.dao.changeStatusInfo(id,status);
		
		renderJson(isSucces);
	}
	
	/**
	 * 删除用户
	 */
	public void delUserInfo() {
		int id = getParaToInt("id");  // 获取用户的id
		
		boolean isSucces = userLoginModel.dao.delUserInfo(id);
		renderJson(isSucces);
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

