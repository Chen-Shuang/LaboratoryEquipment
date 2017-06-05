package model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
/**
 * 严重待修设备
 * @author 陈爽 & 
 *
 */
public class repairItemsModel extends Model<repairItemsModel> {

	public static final repairItemsModel dao= new repairItemsModel();
	
	/**
	 * 按分页获取维修的设备信息
	 * @param curr 当前页
	 * @param size 页面显示数量
	 * @param search 查询内容
	 * @param sTime 起始时间
	 * @param eTime 结束时间
	 * @param status 状态
	 * @return 返回该页设备信息
	 */
	public Page<repairItemsModel> getRepairItemsInfo(int curr, int size, String search, String sTime, String eTime, int status){
		if(status==0){ // 获得待维修信息
			return paginate(curr, size, "select a.*,b.*,a.id items_id,b.status repair_status",
					" from items a,repair_items b where a.status=2 and b.status=0 and a.id=b.items_id and concat(a.code,a.name,a.type) like '%"+search+"%' "
							+ "and b.repairDate >= '"+sTime+"' and b.repairDate <= '"+eTime+"' order by a.createTime desc");
		}else{ // 已完成设备信息(分组展示)
			return paginate(curr, size, "select a.*,b.*,a.id items_id,b.status repair_status",
					" from items a,repair_items b where a.status!=-1 and  b.status=1 and a.id=b.items_id and concat(a.code,a.name,a.type) like '%"+search+"%' "
							+ "and b.repairDate >= '"+sTime+"' and b.repairDate <= '"+eTime+"' order by a.createTime desc");
		}
		
	}
	
	/**
	 * 按分页获取维修的设备信息
	 * @param search 查询内容
	 * @param sTime 起始时间
	 * @param eTime 结束时间
	 * @param status 状态
	 * @return 返回该页设备信息
	 */
	public List<repairItemsModel> getRepairItemsInfo(String search, String sTime, String eTime, int status){
		if(status==0){ // 获得待维修信息
			return repairItemsModel.dao.find("select * from items a,repair_items b "
					+ "where a.status=2 and b.status=0 and a.id=b.items_id and concat(a.code,a.name,a.type) like '%"+search+"%' "
							+ "and b.repairDate >= '"+sTime+"' and b.repairDate <= '"+eTime+"' order by a.createTime desc");
		}else{ // 已完成设备信息(分组展示)
			return repairItemsModel.dao.find("select * from items a,repair_items b "
					+ "where a.status!=-1 and  b.status=1 and a.id=b.items_id and concat(a.code,a.name,a.type) like '%"+search+"%' "
							+ "and b.repairDate >= '"+sTime+"' and b.repairDate <= '"+eTime+"' order by a.createTime desc");
		}
		
	}
	
	/**
	 * 获取单个设备信息
	 * @param itemsId 设备id
	 * @return 返回查询的设备对象
	 */
	public repairItemsModel getRepairItemInfo(int itemsId) {
		return repairItemsModel.dao.findFirst("select a.*,b.*,a.id items_id,b.id repair_id"
				+ "  from items a,repair_items b where a.id=b.items_id and a.id=?",itemsId);
	}
	
	/**
	 * 更新设备待修状态为已修状态
	 * @param itemsId 设备id
	 */
	public void RepairFinish(int itemsId){
		Db.update("update repair_items set status=1 where items_id=?",itemsId);
	}
}
