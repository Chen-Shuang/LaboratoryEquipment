package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.ItemsModel;
import model.newItemsModel;
import model.repairItemsModel;
import model.scrapItemsModel;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

import excel.ReadExcelFile;
import excel.WriteExcelFile;
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
		Page<repairItemsModel> repairItems = repairItemsModel.dao.getRepairItemsInfo(curr, size, search,sTime,eTime,status); // 查询维修设备信息
		renderJson(repairItems);
	}
	
	/**
	 * 获取单个设备信息
	 */
	public void getRepairItemInfo() {
		int itemsId = getParaToInt("itemsId"); // 获取设备id
		repairItemsModel repairItem = repairItemsModel.dao.getRepairItemInfo(itemsId); // 获取设备信息
		renderJson(repairItem);
	}
	
	/**
	 * 修改设备信息
	 */
	public void updateItemInfo() {
		final ItemsModel items = getModel(ItemsModel.class, "items"); // 获取序列化表单数据
		final repairItemsModel repairItems = getModel(repairItemsModel.class, "repair_items"); // 获取序列化表单数据
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		final String nowTime = df.format(new Date()).toString(); // 获取当前系统时间
		
		boolean success = Db.tx(new IAtom() {  // 添加事务处理
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("updateTime", nowTime).update();  // 修改设备的基本信息
	            	repairItems.update(); // 修改报废设备信息
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
	 * 设备完成维修
	 */
	public void toNewItem(){
		final int itemsId = getParaToInt("itemsId"); // 获取设备id
		
		boolean success = Db.tx(new IAtom() {  // 添加事务处理
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	ItemsModel.dao.updateToNewItem(itemsId); // 修改基本信息状态为新添设备
	            	newItemsModel.dao.updateRepairFinish(itemsId); // 更新新添设备表中的状态为完成维修
	            	repairItemsModel.dao.RepairFinish(itemsId);   // 更新维修表中状态为已完成维修
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
	 * 设备状态设置为报废
	 */
	public void toScrapItem(){
		final ItemsModel items = getModel(ItemsModel.class, "items"); // 获取设备信息的id
		final repairItemsModel repairItems = getModel(repairItemsModel.class, "repair_items"); // 获取新添设备信息的id
		final scrapItemsModel scrapItems = getModel(scrapItemsModel.class, "scrap_items"); // 获取修改设备信息的序列化表单数据
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		final String nowTime = df.format(new Date()).toString(); // 获取当前系统时间
		
		boolean success = Db.tx(new IAtom() {  // 添加事务处理
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("status", 3).set("updateTime", nowTime).update();  // 将设备状态设置为待报废状态
	            	repairItems.set("status", 2).update(); // 将维修状态设置为维修失败报废                                                                                                  
	        		scrapItems.set("items_id", items.getLong("id")).set("status", 0).save(); // 插入该条设备修改的信息，状态为待审核
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
		
		List<repairItemsModel> repairItems = repairItemsModel.dao.getRepairItemsInfo(search,sTime,eTime,status); // 获取查询内容
		
		String fileName = "维修资金统计表.xls"; // 文件下载名称
		
		WriteExcelFile.writeRepairExcel(repairItems, fileName, getResponse()); // 下载文档
		
	}
}
