package appAPI;

import model.userLoginModel;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;

public class phoneLoginController extends Controller {

	/**
	 * 用户登录
	 */
	@Clear
	public void login(){
		String email = getPara("email"); // 获得用户邮箱
		String pwd = HashKit.md5(HashKit.sha1(getPara("pwd"))); // 将用户密码进行加密 
		
		boolean isTrue = userLoginModel.dao.validUser(email, pwd); // 验证账号密码是否正确
		if(isTrue){ // 账号密码正确，登录系统
			
			Long id = userLoginModel.dao.getUserInfo(email).getLong("id"); // 获取用户id
			getSession().setAttribute("userLoginId", id); // 存储用户ID
			
			String pwd_md5 = userLoginModel.dao.getUserInfo(email).getStr("pwd");
			renderText(pwd_md5); // 如果登录成功将登录的加密密码返回
		}else{ // 账号密码错误
			renderText("error");
		}
		
	}
}
