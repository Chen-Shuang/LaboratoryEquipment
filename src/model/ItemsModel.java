package model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
/**
 * �豸������Ϣ
 * @author ��ˬ  
 *
 */
@SuppressWarnings("serial")
public class ItemsModel extends Model<ItemsModel> {

	public static final ItemsModel dao= new ItemsModel();
	
	/**
	 * ����ҳ���е��豸��Ϣ
	 * @param curr ��ǰҳ
	 * @param size ҳ����ʾ����
	 * @param search ��ѯ����
	 * @param sTime ��ʼʱ��
	 * @param eTime ����ʱ��
	 * @param status �豸״̬
	 * @return ���ظ�ҳ�豸��Ϣ
	 */
	public Page<ItemsModel> allItemsInfo(int curr, int size, String search, String sTime, String eTime, int status){
		if(status==-2){
			return paginate(curr, size, "select *",
					" from items a where  concat(a.code,a.name,a.type) like '%"+search+"%' "
							+ "and a.createTime >= '"+sTime+"' and a.createTime <= '"+eTime+"' order by a.createTime desc");
		}else{
			return paginate(curr, size, "select *",
					" from items a where  concat(a.code,a.name,a.type) like '%"+search+"%' and a.status="+status+" "
							+ "and a.createTime >= '"+sTime+"' and a.createTime <= '"+eTime+"' order by a.createTime desc");
		}
	}
	
	/**
	 * ɾ���豸��Ϣ
	 * @param itemsId �豸id
	 * @return ����Ӱ������
	 */
	public int delItemInfo(int itemsId) {
		return Db.update("update items set status=-1 where id=?",itemsId);
	}
	
	/**
	 * ���޸��豸״̬Ϊ�����豸
	 * @param itemsId �豸id
	 */
	public void updateToNewItem(int itemsId){
		Db.update("update items set status=1 where id=?",itemsId);
	}
	
	/**
	 * ��ѯ�豸���蹺�豸�ޱ��֮����豸������������ɾ���ģ���Ϊ��ɾ�����豸�б�ţ�
	 * @return ����
	 */
	public int getItemsCount(){
		List<ItemsModel> items = ItemsModel.dao.find("select * from items where status!=0");
		return items.size();
	}
}
