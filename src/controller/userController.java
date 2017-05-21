package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import mail.SendMail;
import model.userLoginModel;

import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.PathKit;

/**
 * �û�����
 * @author ��ˬ
 */
public class userController extends Controller{

	public void index() {
		render("user.html");
	}
	
	/**
	 * ����û�
	 */
	public void addUserInfo() {
		userLoginModel userLogin = getModel(userLoginModel.class,"user_login");  // ��ȡ����Ϣ
		
		String pwd = generateString(6); // ������λ�������
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		String nowTime = df.format(new Date()).toString(); // ��ȡ��ǰϵͳʱ��
		
		userLogin.set("status", 1).set("pwd", HashKit.md5(HashKit.sha1(pwd))).set("createTime", nowTime).set("updateTime", nowTime); // ���Ҫ�������Ϣ
		boolean isTrue = userLogin.save(); // �����û���Ϣ��������Ӱ������
	    
	    if(isTrue){ // ����ɹ�
		    String name = getPara("user_login.name"); // ��ȡ��ӵ��û�����
		    String email = getPara("user_login.email"); // ��ȡ��ӵ��û�����
	    	String subject = "���������Ѽ���ʵ�����豸����ϵͳ";  // �����ʼ�����
	    	String content = "�װ���" + name + "��\r\n\r\n";  // �����ʼ�����;
	    		   content += "��ӭʹ��ʵ�����豸����ϵͳ�����������ѱ�����Ա����ʵ�����豸����ϵͳ����������Ϊ"+pwd+"�����������ܸ��߱��ˣ�\r\n";  
	    		   content += "�������������޸����ĳ�ʼ����\r\n\r\n";
	    		   content +=  "������ַ\r\n\r\n";
	    		   content +=  "֣�ݴ�ѧ";
	    	SendMail send = new SendMail(email,subject,content); // �����̡߳��ʼ�����
	    	new Thread(send).start(); // �����̷߳����ʼ� �������ʼ��������йأ�������������̣߳���Ӳ����������ӳ٣�
	    	
	    	renderText("success"); // ���̷߳��ط��ͳɹ�
	    	
	    }else{ // ����ʧ��
	    	renderText("error");
	    }
	}
	
	 /**
	  *  �������������
	  */
	 private  final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyz";  
	 /**
	  * ���ɳ���Ϊlength�����
	  */
	 private  String generateString(int length) {  
	     StringBuffer sb = new StringBuffer();  // ����һ���ɱ䳤���ַ�������
	     Random random = new Random();  // ��������Ķ���
	     for (int i = 0; i < length; i++) {  
	         sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length()))); 
	     }  
	     return sb.toString();  
	 }

}

