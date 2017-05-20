package config;
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
	String pwd = HashKit.md5(HashKit.sha1(getPara(getPara("pwd")))); // 获取密码进行md5加密处理; 

	
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
 * 修改密码
 */
public void updatePwd(){
	String oldPwd = getPara("oldPwd");
	String newPwd = getPara("newPwd");
	// MD5加密
	String user = getSession().getAttribute("user").toString();
	
}

/**
 * 退出登录
 */
public void logout(){
	getSession().invalidate(); // 将session失效
	render("login.html");
}


}
