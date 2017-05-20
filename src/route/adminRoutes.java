package route;

import com.jfinal.config.Routes;

import config.ActController;
import config.LoginController;
import controller.allController;
import controller.auditController;
import controller.homeController;
import controller.needController;
import controller.newController;
import controller.repairController;
import controller.scrapController;
import controller.userController;

public class adminRoutes extends Routes {

	/**
	 * ·�����
	 */
	public void config() {
		add("/", LoginController.class);
		add("/act", ActController.class);	 // �˵�����
		add("/home",homeController.class,"WEB-INF/page/"); // ��ҳ
		add("/audit", auditController.class,"WEB-INF/page/");  // �쵼���
		add("/need", needController.class,"WEB-INF/page/"); // �蹺�豸
		add("/new", newController.class,"WEB-INF/page/"); // �����豸
		add("/repair", repairController.class,"WEB-INF/page/"); // �����豸
		add("/scrap", scrapController.class,"WEB-INF/page/"); // �����豸
		add("/all", allController.class,"WEB-INF/page/"); // ȫ���豸
		add("/user", userController.class,"WEB-INF/page/"); // �û�����
	}
}
