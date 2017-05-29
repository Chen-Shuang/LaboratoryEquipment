package config;
import model.userLoginModel;

import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
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


}
