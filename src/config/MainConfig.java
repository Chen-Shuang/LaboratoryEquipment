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

	public static String webUrl; // ���徲̬����������Ŀ��ַ
	/**
	 * ����
	 */
	@Override
	public void configConstant(Constants arg0) {
		loadPropertyFile("dbConfig.txt");
		arg0.setDevMode(getPropertyToBoolean("devMode"));
		
		webUrl = getProperty("webUrl"); // ��ȡ�����ļ��е���Ŀ��ַ
	}

	@Override
	public void configHandler(Handlers arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * ������
	 */
	@Override
	public void configInterceptor(Interceptors arg0) {
		arg0.add(new SessionInViewInterceptor());
		arg0.add(new LoginInterceptor()); // ��ӵ�¼������
		
	}
	
	/**
	 * ���ݿ����ӳ�
	 */
	@Override
	public void configPlugin(Plugins arg0) {
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"),
				getProperty("user"), getProperty("password"));
		arg0.add(c3p0Plugin);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		arg0.add(arp);
		userMapping.mapping(arp);  // ���ӳ����
	}
	
	/**
	 * ·��
	 */
	@Override
	public void configRoute(Routes arg0) {
		arg0.add(new adminRoutes());  // ϵͳ����·��
	}

}
