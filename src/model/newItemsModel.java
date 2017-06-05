package model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
/**
 * 新添设备
 * @author 陈爽 
 *
 */
public class newItemsModel extends Model<newItemsModel> {

	public static final newItemsModel dao= new newItemsModel();
	
	/**
	 * 按分页获取设备信息
	 * @param curr 当前页
	 * @param size 页面显示数量
	 * @param search 查询内容
	 * @param sTime 起始时间
	 * @param eTime 结束时间
	 * @return 返回该页设备信息
	 */
	public Page<newItemsModel> getNewItemsInfo(int curr, int size, String search, String sTime, String eTime){
		return paginate(curr, size, "select a.*,b.*,a.id items_id,b.status new_status",
			" from items a,new_items b where a.status=1 and b.status=0 and a.id=b.items_id and concat(a.code,a.name,a.type) like '%"+search+"%' "
					+ "and b.purchaseDate >= '"+sTime+"' and b.purchaseDate <= '"+eTime+"' order by a.createTime desc");
	}
	
	/**
	 * 获取设备信息
	 * @param search 查询内容
	 * @param sTime 起始时间
	 * @param eTime 结束时间
	 * @return 返回该页设备信息
	 */
	public List<newItemsModel> getNewItemsInfo(String search, String sTime, String eTime){
		return newItemsModel.dao.find("select * from items a,new_items b "
				+ "where a.status=1 and b.status=0 and a.id=b.items_id and concat(a.code,a.name,a.type) like '%"+search+"%' "
					+ "and b.purchaseDate >= '"+sTime+"' and b.purchaseDate <= '"+eTime+"' order by a.createTime desc");
	}
	
	/**
	 * 获取单个设备信息
	 * @param itemsId 设备id
	 * @return 返回查询的设备对象
	 */
	public newItemsModel getNewItemInfo(int itemsId) {
		return newItemsModel.dao.findFirst("select a.*,b.*,a.id items_id,b.id new_id"
				+ "  from items a,new_items b where a.status=1 and b.status=0 and a.id=b.items_id and a.id=?",itemsId);
	}
	
	/**
	 * 维修成功后更新新添设备状态
	 * @param itemsId 设备id
	 */
	public void updateRepairFinish(int itemsId){
		Db.update("update new_items set status=0 where items_id=?",itemsId);
	}
	
	/**
	 * 报废审核驳回更新新添设备状态
	 * @param itemsId 设备id
	 */
	public void auditRejectScrap(Long itemsId){
		Db.update("update new_items set status=0 where items_id=?",itemsId);
	}
}

