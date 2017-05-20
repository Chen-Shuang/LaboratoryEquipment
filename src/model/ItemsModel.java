package model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
/**
 * �豸������Ϣ
 * @author ��ˬ  
 *
 */
@SuppressWarnings("serial")
public class ItemsModel extends Model<ItemsModel> {

	public static final ItemsModel dao= new ItemsModel();
	
	/**
	 * ɾ���豸��Ϣ
	 * @param itemsId �豸id
	 * @return ����Ӱ������
	 */
	public int delItemInfo(int itemsId) {
		return Db.update("update items set status=-1 where id=?",itemsId);
	}
	
	/**
	 * �޸��豸״̬Ϊ
	 * @param itemsId �豸id
	 * @return
	 */
	public int buyItemInfo(int itemsId) {
		return Db.update("update need_items set status=-2 where items_id=?",itemsId);
	}
}
