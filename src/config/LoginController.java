package config;
import mail.SendMail;
import model.userLoginModel;

import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;

import controller.changePwdController;
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
	String pwd = HashKit.md5(HashKit.sha1(getPara("pwd"))); // ���û�������м��� 

	
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
 * ��ȡ��¼����Ϣ
 */
public void getUserInfo(){
	String userId =  getSession().getAttribute("userLoginId").toString(); // ��ȡ��¼�˵�sessionֵ
	userLoginModel userInfo = userLoginModel.dao.findById(userId);  // ��ȡ��¼����Ϣ
	renderJson(userInfo);
}

/**
 * �޸�����
 */
public void updatePwd(){
	String oldPwd = HashKit.md5(HashKit.sha1(getPara("oldPwd"))); // ��ȡ���ܺ��ԭ����
	String newPwd = HashKit.md5(HashKit.sha1(getPara("newPwd"))); // ��ȡ���ܺ��������
	String userId =  getSession().getAttribute("userLoginId").toString(); // ��ȡ��¼�˵�sessionֵ
	
	boolean isTrue = userLoginModel.dao.isTrueOldpwd(oldPwd,userId); // ��֤�������Ƿ���ȷ
	if(isTrue){
		boolean isUpdateTrue = userLoginModel.dao.updatePwd(newPwd, userId); // �����û�����
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
 * �޸ĸ�����Ϣ
 */
public void updateUserInfo(){
	String userId =  getSession().getAttribute("userLoginId").toString(); // ��ȡ��¼�˵�sessionֵ
	String name = getPara("name");
	String phone = getPara("phone");
	String email = getPara("email");
	
	boolean isTrue = userLoginModel.dao.updateUserInfo(name, phone, email, userId); // �޸��û���Ϣ
	if(isTrue){
		renderText("success");
	}else{
		renderText("error");
	}
}

/**
 * �˳���¼
 */
public void logout(){
	getSession().invalidate(); // ��sessionʧЧ
	render("login.html");
}

/**
 * ��������
 */
public void forgotPwd(){
	String email = getPara("email"); // ��ȡ�û�����

	userLoginModel user = userLoginModel.dao.getUserInfo(email); // ��ȡ�û�����
	String pwd = user.getStr("pwd");  // ��ȡ���ܹ�������
	String name = user.getStr("name"); // ��ȡ�û���
	String Url = new changePwdController().changePwdUrl +pwd;  // �޸������ַ����ַ������˼��ܵ����룩
if(pwd==null){ // �ж��û�������������˺��Ƿ����
		renderText("error");
	}else{
		// ������һ���̷߳����ʼ����޸�����
	    String subject = "ʵ�����豸����ϵͳ�����޸�";  // �����ʼ�����
	    String content = "�װ���" + name + "��\r\n\r\n";  // �����ʼ�����;
	    	   content += "��������ʵ�����豸����ϵͳ�����޸�\r\n";  
	    	   content += "�������������޸��������룬������Ǳ��ˣ�����ԣ�\r\n\r\n";
	    	   content +=  Url+"\r\n\r\n";
	    	   content +=  "֣�ݴ�ѧ";
	    SendMail send = new SendMail(email,subject,content); // �����̡߳��ʼ�����
	    new Thread(send).start(); // �����̷߳����ʼ� �������ʼ��������йأ�������������̣߳���Ӳ����������ӳ٣�
	    
		renderText("success");
	}
}


}
