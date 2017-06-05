package model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
/**
 * �����豸
 * @author ��ˬ 
 *
 */
public class newItemsModel extends Model<newItemsModel> {

	public static final newItemsModel dao= new newItemsModel();
	
	/**
	 * ����ҳ��ȡ�豸��Ϣ
	 * @param curr ��ǰҳ
	 * @param size ҳ����ʾ����
	 * @param search ��ѯ����
	 * @param sTime ��ʼʱ��
	 * @param eTime ����ʱ��
	 * @return ���ظ�ҳ�豸��Ϣ
	 */
	public Page<newItemsModel> getNewItemsInfo(int curr, int size, String search, String sTime, String eTime){
		return paginate(curr, size, "select a.*,b.*,a.id items_id,b.status new_status",
			" from items a,new_items b where a.status=1 and b.status=0 and a.id=b.items_id and concat(a.code,a.name,a.type) like '%"+search+"%' "
					+ "and b.purchaseDate >= '"+sTime+"' and b.purchaseDate <= '"+eTime+"' order by a.createTime desc");
	}
	
	/**
	 * ��ȡ�豸��Ϣ
	 * @param search ��ѯ����
	 * @param sTime ��ʼʱ��
	 * @param eTime ����ʱ��
	 * @return ���ظ�ҳ�豸��Ϣ
	 */
	public List<newItemsModel> getNewItemsInfo(String search, String sTime, String eTime){
		return newItemsModel.dao.find("select * from items a,new_items b "
				+ "where a.status=1 and b.status=0 and a.id=b.items_id and concat(a.code,a.name,a.type) like '%"+search+"%' "
					+ "and b.purchaseDate >= '"+sTime+"' and b.purchaseDate <= '"+eTime+"' order by a.createTime desc");
	}
	
	/**
	 * ��ȡ�����豸��Ϣ
	 * @param itemsId �豸id
	 * @return ���ز�ѯ���豸����
	 */
	public newItemsModel getNewItemInfo(int itemsId) {
		return newItemsModel.dao.findFirst("select a.*,b.*,a.id items_id,b.id new_id"
				+ "  from items a,new_items b where a.status=1 and b.status=0 and a.id=b.items_id and a.id=?",itemsId);
	}
	
	/**
	 * ά�޳ɹ�����������豸״̬
	 * @param itemsId �豸id
	 */
	public void updateRepairFinish(int itemsId){
		Db.update("update new_items set status=0 where items_id=?",itemsId);
	}
	
	/**
	 * ������˲��ظ��������豸״̬
	 * @param itemsId �豸id
	 */
	public void auditRejectScrap(Long itemsId){
		Db.update("update new_items set status=0 where items_id=?",itemsId);
	}
}

