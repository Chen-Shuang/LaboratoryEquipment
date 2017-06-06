package model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
/**
 * 报废设备
 * @author 陈爽 & 
 *
 */
@SuppressWarnings("serial")
public class scrapItemsModel extends Model<scrapItemsModel> {

	public static final scrapItemsModel dao= new scrapItemsModel();
	
	/**
	 * 按分页获取报废的设备信息
	 * @param curr 当前页
	 * @param size 页面显示数量
	 * @param search 查询内容
	 * @param sTime 起始时间
	 * @param eTime 结束时间
	 * @return 返回该页设备信息
	 */
	public Page<scrapItemsModel> getScrapItemsInfo(int curr, int size, String search, String sTime, String eTime, int status){
		if(status==-1){ // 驳回
			return paginate(curr, size, "select a.*,b.*,a.id items_id,b.status scrap_status",
					" from items a,scrap_items b where a.status!=-1 and b.status=-1 and a.id=b.items_id and concat(a.code,a.name,a.type) like '%"+search+"%' "
							+ "and b.scrapDate >= '"+sTime+"' and b.scrapDate <= '"+eTime+"' order by a.createTime desc");
		}else{
			return paginate(curr, size, "select a.*,b.*,a.id items_id,b.status scrap_status",
					" from items a,scrap_items b where a.status=3 and b.status='"+status+"' and a.id=b.items_id and concat(a.code,a.name,a.type) like '%"+search+"%' "
							+ "and b.scrapDate >= '"+sTime+"' and b.scrapDate <= '"+eTime+"' order by a.createTime desc");
		}
	}
	
	/**
	 * 取报废的设备信息
	 * @param search 查询内容
	 * @param sTime 起始时间
	 * @param eTime 结束时间
	 * @return 返回该页设备信息
	 */
	public List<scrapItemsModel> getScrapItemsInfo(String search, String sTime, String eTime, int status){
		if(status==-1){ // 驳回
			return scrapItemsModel.dao.find("select * from items a,scrap_items b "
					+ "where  b.status=-1 and a.id=b.items_id and concat(a.code,a.name,a.type) like '%"+search+"%' "
							+ "and b.scrapDate >= '"+sTime+"' and b.scrapDate <= '"+eTime+"' order by a.createTime desc");
		}else{
			return scrapItemsModel.dao.find("select * from items a,scrap_items b "
					+ "where a.status=3 and b.status='"+status+"' and a.id=b.items_id and concat(a.code,a.name,a.type) like '%"+search+"%' "
							+ "and b.scrapDate >= '"+sTime+"' and b.scrapDate <= '"+eTime+"' order by a.createTime desc");
		}
	}
	
	/**
	 * 获取单个设备信息
	 * @param itemsId 设备id
	 * @return 返回查询的设备对象
	 */
	public scrapItemsModel getScrapItemInfo(int itemsId) {
		return scrapItemsModel.dao.findFirst("select a.*,b.*,a.id items_id,b.id scrap_id"
				+ "  from items a,scrap_items b where a.id=b.items_id and a.id=?",itemsId);
	}
}
