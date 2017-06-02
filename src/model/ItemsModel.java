package model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
/**
 * 设备基本信息
 * @author 陈爽  
 *
 */
@SuppressWarnings("serial")
public class ItemsModel extends Model<ItemsModel> {

	public static final ItemsModel dao= new ItemsModel();
	
	/**
	 * 按分页所有的设备信息
	 * @param curr 当前页
	 * @param size 页面显示数量
	 * @param search 查询内容
	 * @param sTime 起始时间
	 * @param eTime 结束时间
	 * @param status 设备状态
	 * @return 返回该页设备信息
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
	
	/**
	 * 查询设备除需购设备无编号之外的设备数量（包涵被删除的，因为被删除的设备有编号）
	 * @return 数量
	 */
	public int getItemsCount(){
		List<ItemsModel> items = ItemsModel.dao.find("select * from items where status!=0");
		return items.size();
	}
}
