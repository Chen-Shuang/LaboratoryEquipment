package model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
/**
 * 设备基本信息
 * @author 陈爽  
 *
 */
@SuppressWarnings("serial")
public class ItemsModel extends Model<ItemsModel> {

	public static final ItemsModel dao= new ItemsModel();
	
	/**
	 * 删除设备信息
	 * @param itemsId 设备id
	 * @return 深受影响行数
	 */
	public int delItemInfo(int itemsId) {
		return Db.update("update items set status=-1 where id=?",itemsId);
	}
	
	/**
	 * 已修改设备状态为新添设备
	 * @param itemsId 设备id
	 */
	public void updateToNewItem(int itemsId){
		Db.update("update items set status=1 where id=?",itemsId);
	}
}
