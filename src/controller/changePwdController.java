package controller;

import model.userLoginModel;

import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
/**
 * �����޸��������
 * @author ��ˬ
 *
 */
public class changePwdController extends Controller {

	public String changePwdUrl = "http://106.14.170.241/change?p=";  // �����������޸������ҳ���ַ
	
	public void index(){
		
		String oldPwd = getPara("p"); // ��ȡ������
		setAttr("oldPwd",oldPwd);   // �������뷵�ص��޸������ҳ��
		
		render("changePassword.html");
	}
	
	/**
	 * �޸�����
	 */
	public void changePwd(){
		String email = getPara("email"); // ��ȡ�û�����
		String oldPwd = getPara("oldPwd"); // ��ȡ�����룬�Ѿ��Ǽ��ܵ�����
		String newPwd = HashKit.md5(HashKit.sha1(getPara("newPwd")));  // ��ȡ�����룬���ܴ���
		
		boolean isTrue = userLoginModel.dao.updatePwd(email, oldPwd, newPwd); // ��������
		if(isTrue){
			renderText("success");
		}else{
			renderText("error");
		}
	}
}
