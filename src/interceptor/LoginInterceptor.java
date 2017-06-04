package interceptor;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import config.LoginController;

/**
 * 登录session拦截器
 * @author 陈爽
 *
 */
public class LoginInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation arg0) {
		
	   	Controller controller = arg0.getController(); // 获取用户正在请求的Controller
	   	Object loginUser = controller.getSession().getAttribute("userLoginId");  // 获取用户登录的session
	   	
	    if (loginUser != null) 
	    	arg0.invoke(); // 如果session不为空，则放行
	    else
	      controller.redirect("/login.html"); // 负责跳转到登录页面
	}
}
