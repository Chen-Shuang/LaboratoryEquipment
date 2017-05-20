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
	 * 路由添加
	 */
	public void config() {
		add("/", LoginController.class);
		add("/act", ActController.class);	 // 菜单管理
		add("/home",homeController.class,"WEB-INF/page/"); // 首页
		add("/audit", auditController.class,"WEB-INF/page/");  // 领导审核
		add("/need", needController.class,"WEB-INF/page/"); // 需购设备
		add("/new", newController.class,"WEB-INF/page/"); // 新添设备
		add("/repair", repairController.class,"WEB-INF/page/"); // 待修设备
		add("/scrap", scrapController.class,"WEB-INF/page/"); // 报废设备
		add("/all", allController.class,"WEB-INF/page/"); // 全部设备
		add("/user", userController.class,"WEB-INF/page/"); // 用户管理
	}
}
