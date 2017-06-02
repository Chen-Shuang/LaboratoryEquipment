package config;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import mail.SendMail;
import model.userLoginModel;

import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;

import controller.changePwdController;
/**
 * 登录
 * @author 陈爽
 *
 */
public class LoginController extends Controller{

/**
 * 登录页面
 */
public void index(){
	render("/login.html");
}

/**
 * 发送验证码
 */
public void sendEmailCode(){
	String email = getPara("email"); // 获得用户邮箱
	String pwd = HashKit.md5(HashKit.sha1(getPara("pwd"))); // 将用户密码进行加密 
	
	boolean isTrue = userLoginModel.dao.validUser(email, pwd); // 验证账号密码是否正确
	if(isTrue){ // 账号密码正确，发送邮件
		String code = emailCode(4);
    	String subject = "实验室设备管理系统登录验证码";  // 定义邮件主题
    	String content = "您的登录验证码为  "+ code +" ，如果非本人操作，请忽略（打死都不能告诉别人）。\r\n\r\n";  
    		   content +=  "郑州大学";
    	SendMail send = new SendMail(email,subject,content); // 定义线程、邮件对象
    	new Thread(send).start(); // 启动线程发送邮件 （发送邮件跟网速有关，如果不开启多线程，添加操作将会有延迟）
		
    	getSession().setAttribute("sendCode", code); // 存储发送的验证码
    	getSession().setAttribute("sendEmailTime", new Date().getTime()); // 存储发送邮件的时间
		
		renderText("success");
	}else{ // 否则，发送error
		renderText("error");
	}
}
/**
 * 用户登录
 */
public void login(){
	String email = getPara("email"); // 获得用户邮箱
	String pwd = HashKit.md5(HashKit.sha1(getPara("pwd"))); // 将用户密码进行加密 
	String code = getPara("code"); // 获取验证码
	
	try { // 判断用户是否发送过验证码
		long sendTime = (Long) getSession().getAttribute("sendEmailTime"); // 获取发送验证码时间
		String sendCode = getSession().getAttribute("sendCode").toString();
		
		// 对比发送验证码时间
		int timeDifference = (int)((new Date().getTime() - sendTime) / 1000); 
		if(timeDifference<300){ // 设置验证码超时时间为300秒
			if(code.equals(sendCode)){ // 验证码正确
				boolean isTrue = userLoginModel.dao.validUser(email, pwd); // 验证账号密码是否正确
				if(isTrue){ // 账号密码正确，登录系统
					getSession().invalidate(); // 将验证码session失效
					
					Long id = userLoginModel.dao.getUserInfo(email).getLong("id"); // 获取用户id
					getSession().setAttribute("userLoginId", id); // 存储用户ID
					renderText("success");
				}else{ // 账号密码错误
					renderText("error");
				}
			}else{ // 验证码错误
				renderText("codeError");
			}
		}else{ // 验证码超时
			getSession().invalidate(); // 将验证码session失效
			renderText("codeOutTime");
		}
	} catch (Exception e) { // 没有存储记录，返回验证码错误
		renderText("codeError");
	}
	
	
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
		if(isUpdateTrue){ // 是否修改成功
			renderText("success");
		}else{
			renderText("updateError");
		}
	}else{ // 旧密码错误
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


/**
 *  定义随机的内容
 */
private  final String ALLCHAR = "0123456789";  
/**
 * 生成长度为length随机数
 */
private  String emailCode(int length) {  
    StringBuffer sb = new StringBuffer();  // 定义一个可变长的字符串变量
    Random random = new Random();  // 定义随机的对象
    for (int i = 0; i < length; i++) {  
        sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length()))); 
    }  
    return sb.toString();  
}

}
