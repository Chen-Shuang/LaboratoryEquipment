package model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
/**
 * �û���Ϣ
 * @author ��ˬ
 *
 */
@SuppressWarnings("serial")
public class userLoginModel extends Model<userLoginModel> {

	public static final userLoginModel dao= new userLoginModel();
	
	/**
	 * ��ȡע���û���Ϣ
	 * @param curr ��ǰҳ��
	 * @param size ҳ����ʾ����
	 * @param search ��ѯ����
	 * @param status �û�״̬
	 * @return ���ص�ǰҳ�û���Ϣ�б���ѯ����Ϣ������ϵͳ����Ա��
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
	 * ��ѯ�Ƿ��ظ�
	 * @param email ����
	 * @param phone �ֻ���
	 * @param userId �û�id
	 * @return ���ţ�0��ʾ�������ڣ�1��ʾ������ڣ�2��ʾ�ֻ��Ŵ��ڣ�3˵�����䡢�ֻ��Ŷ��Ѵ��ڣ�
	 */
	public int isExist(String email,String phone,int userId){
		int count = 0;
		int	isEmail = userLoginModel.dao.find("select id  from user_login where email='"+email+"' and id!="+userId).size(); // ��ѯ�����ظ�����
		int	isPhone = userLoginModel.dao.find("select id  from user_login where phone='"+phone+"' and id!="+userId).size(); // ��ѯ�ֻ����ظ�����
		
		if(isEmail>0){
			count += 1;  // 1��ʾ�������
		}else if(isPhone>0){
			count += 2;  // 2��ʾ�ֻ��Ŵ���
		}
		return count;  
	}
	
	/**
	 * �޸��û��˺�״̬
	 * @param id �û�id
	 * @param status �û�״̬
	 * @return true/false
	 */
	public boolean changeStatusInfo(int id,int status){
		boolean isSuccess = false; // ���巵��ֵ
		int count = Db.update("update user_login set status="+status+" where id="+id); // ����״̬,������Ӱ������
		if(count>0){ // ����޸ĳɹ�
			isSuccess = true;
		}
		return isSuccess;
	}
	
	/**
	 * ɾ���û�
	 * @param id �û�id
	 * @return true/false
	 */
	public boolean delUserInfo(int id) {
		boolean isSuccess = false; // ���巵��ֵ
		int count = Db.update("update user_login set status=-1 where id="+id); // ����Ϊɾ��ɾ��״̬,������Ӱ������
		if(count>0){ // ������³ɹ�
			isSuccess = true;
		}
		return isSuccess;
	}
	
	/**
	 * ��ȡ�����û�����Ϣ
	 * @param id �û�id
	 * @return ���û���Ϣ
	 */
	public userLoginModel getOneUserInfo(int id){
		return userLoginModel.dao.findFirst("select a.*,b.type from user_login a,role b where a.role_id=b.id and a.id="+id);
	}
}
