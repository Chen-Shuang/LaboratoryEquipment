package controller;

import model.userLoginModel;

import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
/**
 * 忘记修改密码界面
 * @author 陈爽
 *
 */
public class changePwdController extends Controller {

	public String changePwdUrl = "http://106.14.170.241/change?p=";  // 忘记密码中修改密码的页面地址
	
	public void index(){
		
		String oldPwd = getPara("p"); // 获取旧密码
		setAttr("oldPwd",oldPwd);   // 将旧密码返回到修改密码的页面
		
		render("changePassword.html");
	}
	
	/**
	 * 修改密码
	 */
	public void changePwd(){
		String email = getPara("email"); // 获取用户邮箱
		String oldPwd = getPara("oldPwd"); // 获取旧密码，已经是加密的密码
		String newPwd = HashKit.md5(HashKit.sha1(getPara("newPwd")));  // 获取新密码，加密处理
		
		boolean isTrue = userLoginModel.dao.updatePwd(email, oldPwd, newPwd); // 更新密码
		if(isTrue){
			renderText("success");
		}else{
			renderText("error");
		}
	}
}
