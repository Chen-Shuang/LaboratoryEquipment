package controller;

import model.newItemsModel;
import model.repairItemsModel;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
/**
 * 待修设备
 * @author 陈爽 
 *
 */
public class repairController extends Controller {

	public void index() {
		render("repair.html");
	}
	
	/**
	 * 获取维修的设备信息
	 */
	public void getRepairItemsInfo() {
		int curr = getParaToInt("curr"); // 获取页码
		int size = getParaToInt("size"); // 获取页面显示数据
		String search = getPara("search"); // 获取搜索条件
		String sTime = getPara("sTime"); // 获取起始时间  日期搜索是查询的
		String eTime = getPara("eTime"); // 获取终止时间
		int status = getParaToInt("status"); // 获取维修状态
		
		// 判断是否选择了时间,时间格式转换
		if(sTime==""){
			sTime = "1900-01-01";
		}
		if(eTime==""){
			eTime += "2099-06-16"; // 当前系统时间
		}
		Page<repairItemsModel> newItems = repairItemsModel.dao.getRepairItemsInfo(curr, size, search,sTime,eTime,status); // 查询需购设备信息
		renderJson(newItems);
	}
	
	/**
	 * 获取单个设备信息
	 */
	public void getRepairItemInfo() {
		int itemsId = getParaToInt("itemsId"); // 获取设备id
		repairItemsModel newItem = repairItemsModel.dao.getRepairItemInfo(itemsId); // 获取设备信息
		renderJson(newItem);
	}
}
