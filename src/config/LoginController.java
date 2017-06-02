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
 * ��¼
 * @author ��ˬ
 *
 */
public class LoginController extends Controller{

/**
 * ��¼ҳ��
 */
public void index(){
	render("/login.html");
}

/**
 * ������֤��
 */
public void sendEmailCode(){
	String email = getPara("email"); // ����û�����
	String pwd = HashKit.md5(HashKit.sha1(getPara("pwd"))); // ���û�������м��� 
	
	boolean isTrue = userLoginModel.dao.validUser(email, pwd); // ��֤�˺������Ƿ���ȷ
	if(isTrue){ // �˺�������ȷ�������ʼ�
		String code = emailCode(4);
    	String subject = "ʵ�����豸����ϵͳ��¼��֤��";  // �����ʼ�����
    	String content = "���ĵ�¼��֤��Ϊ  "+ code +" ������Ǳ��˲���������ԣ����������ܸ��߱��ˣ���\r\n\r\n";  
    		   content +=  "֣�ݴ�ѧ";
    	SendMail send = new SendMail(email,subject,content); // �����̡߳��ʼ�����
    	new Thread(send).start(); // �����̷߳����ʼ� �������ʼ��������йأ�������������̣߳���Ӳ����������ӳ٣�
		
    	getSession().setAttribute("sendCode", code); // �洢���͵���֤��
    	getSession().setAttribute("sendEmailTime", new Date().getTime()); // �洢�����ʼ���ʱ��
		
		renderText("success");
	}else{ // ���򣬷���error
		renderText("error");
	}
}
/**
 * �û���¼
 */
public void login(){
	String email = getPara("email"); // ����û�����
	String pwd = HashKit.md5(HashKit.sha1(getPara("pwd"))); // ���û�������м��� 
	String code = getPara("code"); // ��ȡ��֤��
	
	try { // �ж��û��Ƿ��͹���֤��
		long sendTime = (Long) getSession().getAttribute("sendEmailTime"); // ��ȡ������֤��ʱ��
		String sendCode = getSession().getAttribute("sendCode").toString();
		
		// �Աȷ�����֤��ʱ��
		int timeDifference = (int)((new Date().getTime() - sendTime) / 1000); 
		if(timeDifference<300){ // ������֤�볬ʱʱ��Ϊ300��
			if(code.equals(sendCode)){ // ��֤����ȷ
				boolean isTrue = userLoginModel.dao.validUser(email, pwd); // ��֤�˺������Ƿ���ȷ
				if(isTrue){ // �˺�������ȷ����¼ϵͳ
					getSession().invalidate(); // ����֤��sessionʧЧ
					
					Long id = userLoginModel.dao.getUserInfo(email).getLong("id"); // ��ȡ�û�id
					getSession().setAttribute("userLoginId", id); // �洢�û�ID
					renderText("success");
				}else{ // �˺��������
					renderText("error");
				}
			}else{ // ��֤�����
				renderText("codeError");
			}
		}else{ // ��֤�볬ʱ
			getSession().invalidate(); // ����֤��sessionʧЧ
			renderText("codeOutTime");
		}
	} catch (Exception e) { // û�д洢��¼��������֤�����
		renderText("codeError");
	}
	
	
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
		if(isUpdateTrue){ // �Ƿ��޸ĳɹ�
			renderText("success");
		}else{
			renderText("updateError");
		}
	}else{ // ���������
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


/**
 *  �������������
 */
private  final String ALLCHAR = "0123456789";  
/**
 * ���ɳ���Ϊlength�����
 */
private  String emailCode(int length) {  
    StringBuffer sb = new StringBuffer();  // ����һ���ɱ䳤���ַ�������
    Random random = new Random();  // ��������Ķ���
    for (int i = 0; i < length; i++) {  
        sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length()))); 
    }  
    return sb.toString();  
}

}
