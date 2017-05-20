package config;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
public class LoginController extends Controller{

/**
 * ��¼ҳ��
 */
public void index(){
	render("/login.html");
}

/**
 * �û���¼
 */
public void login(){
	String user = getPara("user");
	String pwd = HashKit.md5(HashKit.sha1(getPara(getPara("pwd")))); // ��ȡ�������md5���ܴ���; 

	
}

/**
 * ��¼���ҳ��
 */
public void loginIndex(){
	render("/index.html");
}

/**
 * ������ҳ
 */
public void first(){
	render("/WEB-INF/page/first.html");
}

/**
 * �޸�����
 */
public void updatePwd(){
	String oldPwd = getPara("oldPwd");
	String newPwd = getPara("newPwd");
	// MD5����
	String user = getSession().getAttribute("user").toString();
	
}

/**
 * �˳���¼
 */
public void logout(){
	getSession().invalidate(); // ��sessionʧЧ
	render("login.html");
}


}
