package config;
import interceptor.LoginInterceptor;
import route.adminRoutes;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

public class MainConfig extends JFinalConfig {

	public static String webUrl; // 定义静态变量保存项目地址
	/**
	 * 配置
	 */
	@Override
	public void configConstant(Constants arg0) {
		loadPropertyFile("dbConfig.txt");
		arg0.setDevMode(getPropertyToBoolean("devMode"));
		
		webUrl = getProperty("webUrl"); // 读取配置文件中的项目地址
	}

	@Override
	public void configHandler(Handlers arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 拦截器
	 */
	@Override
	public void configInterceptor(Interceptors arg0) {
		arg0.add(new SessionInViewInterceptor());
		arg0.add(new LoginInterceptor()); // 添加登录拦截器
		
	}
	
	/**
	 * 数据库连接池
	 */
	@Override
	public void configPlugin(Plugins arg0) {
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"),
				getProperty("user"), getProperty("password"));
		arg0.add(c3p0Plugin);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		arg0.add(arp);
		userMapping.mapping(arp);  // 添加映射类
	}
	
	/**
	 * 路由
	 */
	@Override
	public void configRoute(Routes arg0) {
		arg0.add(new adminRoutes());  // 系统管理路由
	}

}
