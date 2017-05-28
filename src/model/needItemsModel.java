package model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
/**
 * �蹺�豸
 * @author ��ˬ 
 *
 */
public class needItemsModel extends Model<needItemsModel> {

	public static final needItemsModel dao= new needItemsModel();
	
	/**
	 * ��ȡ�蹺�豸��Ϣ
	 * @param curr ҳ��
	 * @param size չʾ����
	 * @param search ��ѯ����
	 * @return ��ҳ��ѯ����
	 */
	public Page<needItemsModel> getNeedItemsInfo(int curr,int size,String search,String sTime,String eTime,int needStatus) {
		if(needStatus==2) // ��ѯ���е����״̬
			return paginate(curr, size, "select a.*,b.*,b.status need_status,a.id items_id",
				" from items a,need_items b where a.status=0 and a.id=b.items_id and concat(name,type) like '%"+search+"%' "
						+ "and b.purchaseDate >= '"+sTime+"' and b.purchaseDate <= '"+eTime+"' order by a.createTime desc");
		else
			return paginate(curr, size, "select a.*,b.*,b.status need_status,a.id items_id",
					" from items a,need_items b where a.status=0 and a.id=b.items_id and concat(name,type) like '%"+search+"%' "
							+ "and b.purchaseDate >= '"+sTime+"' and b.purchaseDate <= '"+eTime+"' and b.status='"+needStatus+"' order by a.createTime desc");
	}
	/**
	 * ��ȡ�蹺�豸��Ϣ
	 * @param search ��������
	 * @param sTime ��ʼʱ��
	 * @param eTime ����ʱ��
	 * @param needStatus ״̬
	 * @return
	 */
	public List<needItemsModel> getNeedItemsInfo(String search,String sTime,String eTime,int needStatus) {
		if(needStatus==2) // ��ѯ���е����״̬
			return needItemsModel.dao.find("select a.*,b.*,b.status need_status,a.id items_id"
				+" from items a,need_items b where a.status=0 and a.id=b.items_id and concat(name,type) like '%"+search+"%' "
						+ "and b.purchaseDate >= '"+sTime+"' and b.purchaseDate <= '"+eTime+"' order by a.createTime desc");
		else
			return needItemsModel.dao.find("select a.*,b.*,b.status need_status,a.id items_id"
					+" from items a,need_items b where a.status=0 and a.id=b.items_id and concat(name,type) like '%"+search+"%' "
							+ "and b.purchaseDate >= '"+sTime+"' and b.purchaseDate <= '"+eTime+"' and b.status='"+needStatus+"' order by a.createTime desc");
	}
	
	/**
	 * ��ȡ�����豸��Ϣ
	 * @param itemsId �豸id
	 * @return ���ز�ѯ���豸����
	 */
	public needItemsModel getNeedItemInfo(int itemsId) {
		return needItemsModel.dao.findFirst("select a.*,b.*,a.id items_id,b.id need_id"
				+ "  from items a,need_items b where a.status=0 and a.id=b.items_id and a.id=?",itemsId);
	}
	
	/**
	 * �޸��豸״̬Ϊ�ѹ��豸
	 * @param itemsId �豸id
	 * @return ��Ӱ������
	 */
	public int buyItemInfo(int itemsId) {
		return Db.update("update need_items set status=-2 where items_id=?",itemsId);
	}
}
