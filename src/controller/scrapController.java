/**
 * 
 */
package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.ItemsModel;
import model.scrapItemsModel;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;

import excel.WriteExcelFile;

/**
 * 报废设备
 * @author 陈爽 & 
 *
 */
public class scrapController extends Controller {

	public void index() {
		render("scrap.html");
	}
	
	/**
	 * 获取维修的设备信息
	 */
	public void getScrapItemsInfo() {
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
		Page<scrapItemsModel> scrapItems = scrapItemsModel.dao.getScrapItemsInfo(curr, size, search,sTime,eTime,status); // 查询维修设备信息
		renderJson(scrapItems);
	}
	
	/**
	 * 获取单个设备信息
	 */
	public void getScrapItemInfo() {
		int itemsId = getParaToInt("itemsId"); // 获取设备id
		scrapItemsModel scrapItem = scrapItemsModel.dao.getScrapItemInfo(itemsId); // 获取设备信息
		renderJson(scrapItem);
	}
	
	/**
	 * 修改报废设备信息
	 */
	public void updateItemInfo() {
		final ItemsModel items = getModel(ItemsModel.class, "items"); // 获取序列化表单数据
		final scrapItemsModel scrapItems = getModel(scrapItemsModel.class, "scrap_items"); // 获取序列化表单数据
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		final String nowTime = df.format(new Date()).toString(); // 获取当前系统时间
		
		boolean success = Db.tx(new IAtom() {  // 添加事务处理
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("updateTime", nowTime).update();  // 修改设备的基本信息
	            	scrapItems.update(); // 修改报废设备信息
	            } catch (Exception e) {
	                e.printStackTrace();
	                return false;
	            }
	            return true;
	        }
	    });
	    
	    if(success){
	    	renderText("success");
	    }else{
	    	renderText("error");
	    }
	}
	
	/**
	 * 删除单个设备信息
	 */
	public void delItemInfo() {
		int itemsId = getParaToInt("itemsId"); // 获取设备id
		int count = ItemsModel.dao.delItemInfo(itemsId);
		if(count>0){
			renderText("success");
		}else{
			renderText("error");
		}
		
	}
	
	/**
	 * 下载（Excel文件）
	 * @throws IOException 
	 */
	public void downloadExcel() throws IOException{
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
		
		List<scrapItemsModel> scrapItems = scrapItemsModel.dao.getScrapItemsInfo(search,sTime,eTime,status); // 查询维修设备信息
		
		String fileName = "报废设备统计表.xls"; // 文件下载名称
		
		WriteExcelFile.writeScrapExcel(scrapItems, fileName, getResponse()); // 下载文档
	}
}
