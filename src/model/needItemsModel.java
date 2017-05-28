package model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
/**
 * 需购设备
 * @author 陈爽 
 *
 */
public class needItemsModel extends Model<needItemsModel> {

	public static final needItemsModel dao= new needItemsModel();
	
	/**
	 * 获取需购设备信息
	 * @param curr 页码
	 * @param size 展示数量
	 * @param search 查询条件
	 * @return 分页查询数据
	 */
	public Page<needItemsModel> getNeedItemsInfo(int curr,int size,String search,String sTime,String eTime,int needStatus) {
		if(needStatus==2) // 查询所有的审核状态
			return paginate(curr, size, "select a.*,b.*,b.status need_status,a.id items_id",
				" from items a,need_items b where a.status=0 and a.id=b.items_id and concat(name,type) like '%"+search+"%' "
						+ "and b.purchaseDate >= '"+sTime+"' and b.purchaseDate <= '"+eTime+"' order by a.createTime desc");
		else
			return paginate(curr, size, "select a.*,b.*,b.status need_status,a.id items_id",
					" from items a,need_items b where a.status=0 and a.id=b.items_id and concat(name,type) like '%"+search+"%' "
							+ "and b.purchaseDate >= '"+sTime+"' and b.purchaseDate <= '"+eTime+"' and b.status='"+needStatus+"' order by a.createTime desc");
	}
	/**
	 * 获取需购设备信息
	 * @param search 搜索内容
	 * @param sTime 起始时间
	 * @param eTime 结束时间
	 * @param needStatus 状态
	 * @return
	 */
	public List<needItemsModel> getNeedItemsInfo(String search,String sTime,String eTime,int needStatus) {
		if(needStatus==2) // 查询所有的审核状态
			return needItemsModel.dao.find("select a.*,b.*,b.status need_status,a.id items_id"
				+" from items a,need_items b where a.status=0 and a.id=b.items_id and concat(name,type) like '%"+search+"%' "
						+ "and b.purchaseDate >= '"+sTime+"' and b.purchaseDate <= '"+eTime+"' order by a.createTime desc");
		else
			return needItemsModel.dao.find("select a.*,b.*,b.status need_status,a.id items_id"
					+" from items a,need_items b where a.status=0 and a.id=b.items_id and concat(name,type) like '%"+search+"%' "
							+ "and b.purchaseDate >= '"+sTime+"' and b.purchaseDate <= '"+eTime+"' and b.status='"+needStatus+"' order by a.createTime desc");
	}
	
	/**
	 * 获取单个设备信息
	 * @param itemsId 设备id
	 * @return 返回查询的设备对象
	 */
	public needItemsModel getNeedItemInfo(int itemsId) {
		return needItemsModel.dao.findFirst("select a.*,b.*,a.id items_id,b.id need_id"
				+ "  from items a,need_items b where a.status=0 and a.id=b.items_id and a.id=?",itemsId);
	}
	
	/**
	 * 修改设备状态为已购设备
	 * @param itemsId 设备id
	 * @return 受影响行数
	 */
	public int buyItemInfo(int itemsId) {
		return Db.update("update need_items set status=-2 where items_id=?",itemsId);
	}
}
