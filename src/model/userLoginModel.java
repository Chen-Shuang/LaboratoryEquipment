package model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
/**
 * 用户信息
 * @author 陈爽
 *
 */
@SuppressWarnings("serial")
public class userLoginModel extends Model<userLoginModel> {

	public static final userLoginModel dao= new userLoginModel();
	
	/**
	 * 获取注册用户信息
	 * @param curr 当前页面
	 * @param size 页面显示数量
	 * @param search 查询内容
	 * @param status 用户状态
	 * @return 返回当前页用户信息列表（查询的信息不包含系统管理员）
	 */
	public Page<userLoginModel> getUserInfo(int curr,int size,String search,int status,int role){
		if(status==-1&&role==-1){
			return paginate(curr, size, "select a.*,b.type ", " from user_login a,role b where concat(a.name,a.phone,a.email) like '%"+search+"%' and a.role_id=b.id and b.type!=0 and a.status!=-1 order by a.createTime desc");
		}else if(status!=-1&&role==-1){
			return paginate(curr, size, "select a.*,b.type ", " from user_login a,role b where concat(a.name,a.phone,a.email) like '%"+search+"%' and a.role_id=b.id and b.type!=0 and a.status="+status+" order by a.createTime desc");
		}else if(status==-1&&role!=-1){
			return paginate(curr, size, "select a.*,b.type ", " from user_login a,role b where concat(a.name,a.phone,a.email) like '%"+search+"%' and a.role_id=b.id and b.type="+role+" and a.status!=-1 order by a.createTime desc");
		}else{
			return paginate(curr, size, "select a.*,b.type ", " from user_login a,role b where concat(a.name,a.phone,a.email) like '%"+search+"%' and a.role_id=b.id and b.type="+role+" and a.status="+status+" order by a.createTime desc");
		}
	}
	
	/**
	 * 查询是否重复
	 * @param email 邮箱
	 * @param phone 手机号
	 * @param userId 用户id
	 * @return 代号（0表示都不存在；1表示邮箱存在；2表示手机号存在，3说明邮箱、手机号都已存在）
	 */
	public int isExist(String email,String phone,int userId){
		int count = 0;
		int	isEmail = userLoginModel.dao.find("select id  from user_login where email='"+email+"' and id!="+userId).size(); // 查询邮箱重复数量
		int	isPhone = userLoginModel.dao.find("select id  from user_login where phone='"+phone+"' and id!="+userId).size(); // 查询手机号重复数量
		
		if(isEmail>0){
			count += 1;  // 1表示邮箱存在
		}else if(isPhone>0){
			count += 2;  // 2表示手机号存在
		}
		return count;  
	}
	
	/**
	 * 修改用户账号状态
	 * @param id 用户id
	 * @param status 用户状态
	 * @return true/false
	 */
	public boolean changeStatusInfo(int id,int status){
		boolean isSuccess = false; // 定义返回值
		int count = Db.update("update user_login set status="+status+" where id="+id); // 更新状态,返回受影响行数
		if(count>0){ // 如果修改成功
			isSuccess = true;
		}
		return isSuccess;
	}
	
	/**
	 * 删除用户
	 * @param id 用户id
	 * @return true/false
	 */
	public boolean delUserInfo(int id) {
		boolean isSuccess = false; // 定义返回值
		int count = Db.update("update user_login set status=-1 where id="+id); // 更新为删除删除状态,返回受影响行数
		if(count>0){ // 如果更新成功
			isSuccess = true;
		}
		return isSuccess;
	}
	
	/**
	 * 获取单个用户的信息
	 * @param id 用户id
	 * @return 单用户信息
	 */
	public userLoginModel getOneUserInfo(int id){
		return userLoginModel.dao.findFirst("select a.*,b.type from user_login a,role b where a.role_id=b.id and a.id="+id);
	}
}
