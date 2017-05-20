package controller;

import com.jfinal.core.Controller;
/**
 * 用户管理
 * @author 陈爽
 *
 */
public class userController extends Controller {

	public void index() {
		render("user.html");
	}
}
