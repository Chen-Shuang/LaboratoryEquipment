package config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.userLoginModel;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

public class ActController extends Controller {

	public void getMenuList() {	
		getSession().setAttribute("userLoginId", 1); // ���ԣ���¼���ƺ�ɾ��
		String userLoginId = getSession().getAttribute("userLoginId").toString(); // ��ȡ����Ա��¼�û���id
		userLoginModel user = userLoginModel.dao.findFirst("select b.rank from user_login a,role b where a.role_id=b.id and a.id='"+userLoginId+"'");  // ��ѯ���û���Ȩ��
		
		// ���ݹ���Ա������ʾ��Ӧ�˵�
		List dataList; 
		if(user.get("rank").equals(1)){
			dataList = Db.find("select * from memu where rank=1 and status=1 order by id"); // ��ͨ�û�Ȩ��
		}else if(user.get("rank").equals(2)){
			dataList = Db.find("select * from memu where rank!=0 and status=1 order by id"); // �쵼�û�Ȩ��
		}else{
			dataList = Db.find("select * from memu where status=1 order by id"); // ��������ԱȨ��
		}
		
		Map<String, Object> resp=new HashMap<String, Object>();
		resp.put("code", 0);
		resp.put("content", dataList);
		renderJson(resp);
	}
}
