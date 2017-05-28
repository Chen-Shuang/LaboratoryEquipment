package model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
/**
 * �����豸
 * @author ��ˬ & 
 *
 */
public class scrapItemsModel extends Model<scrapItemsModel> {

	public static final scrapItemsModel dao= new scrapItemsModel();
	
	/**
	 * ����ҳ��ȡ���ϵ��豸��Ϣ
	 * @param curr ��ǰҳ
	 * @param size ҳ����ʾ����
	 * @param search ��ѯ����
	 * @param sTime ��ʼʱ��
	 * @param eTime ����ʱ��
	 * @return ���ظ�ҳ�豸��Ϣ
	 */
	public Page<scrapItemsModel> getScrapItemsInfo(int curr, int size, String search, String sTime, String eTime, int status){
		return paginate(curr, size, "select a.*,b.*,a.id items_id,b.status scrap_status",
				" from items a,scrap_items b where a.status=3 and b.status='"+status+"' and a.id=b.items_id and concat(a.code,a.name,a.type) like '%"+search+"%' "
						+ "and b.scrapDate >= '"+sTime+"' and b.scrapDate <= '"+eTime+"' order by a.createTime desc");
		
	}
	
	/**
	 * ��ȡ�����豸��Ϣ
	 * @param itemsId �豸id
	 * @return ���ز�ѯ���豸����
	 */
	public scrapItemsModel getScrapItemInfo(int itemsId) {
		return scrapItemsModel.dao.findFirst("select a.*,b.*,a.id items_id,b.id scrap_id"
				+ "  from items a,scrap_items b where a.id=b.items_id and a.id=?",itemsId);
	}
}
