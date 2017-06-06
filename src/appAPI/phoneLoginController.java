package appAPI;

import model.userLoginModel;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;

public class phoneLoginController extends Controller {

	/**
	 * �û���¼
	 */
	@Clear
	public void login(){
		String email = getPara("email"); // ����û�����
		String pwd = HashKit.md5(HashKit.sha1(getPara("pwd"))); // ���û�������м��� 
		
		boolean isTrue = userLoginModel.dao.validUser(email, pwd); // ��֤�˺������Ƿ���ȷ
		if(isTrue){ // �˺�������ȷ����¼ϵͳ
			
			Long id = userLoginModel.dao.getUserInfo(email).getLong("id"); // ��ȡ�û�id
			getSession().setAttribute("userLoginId", id); // �洢�û�ID
			
			String pwd_md5 = userLoginModel.dao.getUserInfo(email).getStr("pwd");
			renderText(pwd_md5); // �����¼�ɹ�����¼�ļ������뷵��
		}else{ // �˺��������
			renderText("error");
		}
		
	}
}
