package config;
import mail.SendMail;
import model.userLoginModel;

import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;

import controller.changePwdController;
public class LoginController extends Controller{

/**
 * 登录页面
 */
public void index(){
	render("/login.html");
}

/**
 * 用户登录
 */
public void login(){
	String user = getPara("user");
	String pwd = HashKit.md5(HashKit.sha1(getPara("pwd"))); // 将用户密码进行加密 

	
}

/**
 * 登录入口页面
 */
public void loginIndex(){
	render("/index.html");
}

/**
 * 界面首页
 */
public void first(){
	render("/WEB-INF/page/first.html");
}

/**
 * 获取登录人信息
 */
public void getUserInfo(){
	String userId =  getSession().getAttribute("userLoginId").toString(); // 获取登录人的session值
	userLoginModel userInfo = userLoginModel.dao.findById(userId);  // 获取登录人信息
	renderJson(userInfo);
}

/**
 * 修改密码
 */
public void updatePwd(){
	String oldPwd = HashKit.md5(HashKit.sha1(getPara("oldPwd"))); // 获取加密后的原密码
	String newPwd = HashKit.md5(HashKit.sha1(getPara("newPwd"))); // 获取加密后的新密码
	String userId =  getSession().getAttribute("userLoginId").toString(); // 获取登录人的session值
	
	boolean isTrue = userLoginModel.dao.isTrueOldpwd(oldPwd,userId); // 验证旧密码是否正确
	if(isTrue){
		boolean isUpdateTrue = userLoginModel.dao.updatePwd(newPwd, userId); // 更新用户密码
		if(isUpdateTrue){
			renderText("success");
		}else{
			renderText("updateError");
		}
	}else{
		renderText("oldPwdError");
	}
}

/**
 * 修改个人信息
 */
public void updateUserInfo(){
	String userId =  getSession().getAttribute("userLoginId").toString(); // 获取登录人的session值
	String name = getPara("name");
	String phone = getPara("phone");
	String email = getPara("email");
	
	boolean isTrue = userLoginModel.dao.updateUserInfo(name, phone, email, userId); // 修改用户信息
	if(isTrue){
		renderText("success");
	}else{
		renderText("error");
	}
}

/**
 * 退出登录
 */
public void logout(){
	getSession().invalidate(); // 将session失效
	render("login.html");
}

/**
 * 忘记密码
 */
public void forgotPwd(){
	String email = getPara("email"); // 获取用户邮箱

	userLoginModel user = userLoginModel.dao.getUserInfo(email); // 获取用户密码
	String pwd = user.getStr("pwd");  // 获取加密过的密码
	String name = user.getStr("name"); // 获取用户名
	String Url = new changePwdController().changePwdUrl +pwd;  // 修改密码地址（地址中添加了加密的密码）
if(pwd==null){ // 判断用户所输入的邮箱账号是否存在
		renderText("error");
	}else{
		// 开启另一个线程发送邮件，修改密码
	    String subject = "实验室设备管理系统密码修改";  // 定义邮件主题
	    String content = "亲爱的" + name + "！\r\n\r\n";  // 定义邮件内容;
	    	   content += "您已申请实验室设备管理系统密码修改\r\n";  
	    	   content += "请点击下列链接修改您的密码，如果不是本人，请忽略！\r\n\r\n";
	    	   content +=  Url+"\r\n\r\n";
	    	   content +=  "郑州大学";
	    SendMail send = new SendMail(email,subject,content); // 定义线程、邮件对象
	    new Thread(send).start(); // 启动线程发送邮件 （发送邮件跟网速有关，如果不开启多线程，添加操作将会有延迟）
	    
		renderText("success");
	}
}


}
