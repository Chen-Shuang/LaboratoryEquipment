package controller;

import com.jfinal.core.Controller;
/**
 * 领导审核界面
 * @author 陈爽
 *
 */
public class auditController extends Controller {

	public void index() {
		render("audit.html");
	}
}
