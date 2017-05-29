package controller;


import model.ItemsModel;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
/**
 * 设备信息列表
 * @author 陈爽
 *
 */
public class allController extends Controller {

	public void index() {
		render("all.html");
	}
	
	/**
	 * 查询所有设备
	 */
	public void allItemsInfo() {
		int curr = getParaToInt("curr"); // 获取页码
		int size = getParaToInt("size"); // 获取页面显示数据
		String search = getPara("search"); // 获取搜索条件
		String sTime = getPara("sTime"); // 获取起始时间  日期搜索是查询的
		String eTime = getPara("eTime"); // 获取终止时间
		int status = getParaToInt("status"); // 获取设备状态
		
		// 判断是否选择了时间,时间格式转换
		if(sTime==""){
			sTime = "1900-01-01";
		}
		if(eTime==""){
			eTime += "2099-06-16"; // 当前系统时间
		}
		Page<ItemsModel> scrapItems = ItemsModel.dao.allItemsInfo(curr, size, search,sTime,eTime,status); // 查询维修设备信息
		renderJson(scrapItems);
	}
}
