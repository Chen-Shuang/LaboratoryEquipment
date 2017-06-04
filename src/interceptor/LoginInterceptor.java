package interceptor;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import config.LoginController;

/**
 * ��¼session������
 * @author ��ˬ
 *
 */
public class LoginInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation arg0) {
		
	   	Controller controller = arg0.getController(); // ��ȡ�û����������Controller
	   	Object loginUser = controller.getSession().getAttribute("userLoginId");  // ��ȡ�û���¼��session
	   	
	    if (loginUser != null) 
	    	arg0.invoke(); // ���session��Ϊ�գ������
	    else
	      controller.redirect("/login.html"); // ������ת����¼ҳ��
	}
}
