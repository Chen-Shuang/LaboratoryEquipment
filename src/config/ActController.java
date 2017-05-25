package config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.userLoginModel;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

public class ActController extends Controller {

	public void getMenuList() {	
		getSession().setAttribute("userLoginId", 1); // 测试，登录完善后删除
		String userLoginId = getSession().getAttribute("userLoginId").toString(); // 获取管理员登录用户的id
		userLoginModel user = userLoginModel.dao.findFirst("select b.rank from user_login a,role b where a.role_id=b.id and a.id='"+userLoginId+"'");  // 查询该用户的权限
		
		// 根据管理员级别显示对应菜单
		List dataList; 
		if(user.get("rank").equals(1)){
			dataList = Db.find("select * from memu where rank=1 and status=1 order by id"); // 普通用户权限
		}else if(user.get("rank").equals(2)){
			dataList = Db.find("select * from memu where rank!=0 and status=1 order by id"); // 领导用户权限
		}else{
			dataList = Db.find("select * from memu where status=1 order by id"); // 超级管理员权限
		}
		
		Map<String, Object> resp=new HashMap<String, Object>();
		resp.put("code", 0);
		resp.put("content", dataList);
		renderJson(resp);
	}
}
