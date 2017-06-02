package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import model.ItemsModel;
import model.newItemsModel;
import model.repairItemsModel;
import model.scrapItemsModel;

import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

import excel.ReadExcelFile;
/**
 * 新添设备
 * @author 陈爽 
 *
 */
public class newController extends Controller {

	public void index() {
		render("new.html");
	}
	
	/**
	 * 获取新添设备信息
	 */
	public void getNewItemsInfo() {
		int curr = getParaToInt("curr"); // 获取页码
		int size = getParaToInt("size"); // 获取页面显示数据
		String search = getPara("search"); // 获取搜索条件
		String sTime = getPara("sTime"); // 获取起始时间  日期搜索是查询的
		String eTime = getPara("eTime"); // 获取终止时间
		
		// 判断是否选择了时间,时间格式转换
		if(sTime==""){
			sTime = "1900-01-01";
		}
		if(eTime==""){
			eTime += "2099-06-16"; // 当前系统时间
		}
		Page<newItemsModel> newItems = newItemsModel.dao.getNewItemsInfo(curr, size, search,sTime,eTime); // 查询新添设备信息
		renderJson(newItems);
	}
	
	/**
	 * 添加需购设备
	 */
	public void addNewItem() {
		final String userLoginId = getSession().getAttribute("userLoginId").toString(); // 获取登录人id
		
		final ItemsModel items = getModel(ItemsModel.class, "items"); // 获取序列化表单数据
		final newItemsModel newItems = getModel(newItemsModel.class, "new_items"); // 获取序列化表单数据
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		final String nowTime = df.format(new Date()).toString(); // 获取当前系统时间
		
		boolean success = Db.tx(new IAtom() {  // 添加事务处理
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("code", getCode()).set("createTime", nowTime).set("updateTime", nowTime)
	            		.set("status", 1).set("user_login_id", userLoginId).save();  // 保存新添设备的基本信息
	        		newItems.set("items_id", items.getLong("id")).set("status", 0).save(); // 保存新添设备信息{items.getLong("id") 获取保存后对象的id}
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
	 * 获取单个设备信息
	 */
	public void getNewItemInfo() {
		int itemsId = getParaToInt("itemsId"); // 获取设备id
		newItemsModel newItem = newItemsModel.dao.getNewItemInfo(itemsId); // 获取设备信息
		renderJson(newItem);
	}
	
	/**
	 * 修改设备信息
	 */
	public void updateItemInfo() {
		final ItemsModel items = getModel(ItemsModel.class, "items"); // 获取序列化表单数据
		final newItemsModel newItems = getModel(newItemsModel.class, "new_items"); // 获取序列化表单数据
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		final String nowTime = df.format(new Date()).toString(); // 获取当前系统时间
		
		boolean success = Db.tx(new IAtom() {  // 添加事务处理
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("updateTime", nowTime).update();  // 修改新添设备的基本信息
	        		newItems.update(); // 修改新添设备信息
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
	 * 设备状态设置为维修
	 */
	public void toRepairItem(){
		final ItemsModel items = getModel(ItemsModel.class, "items"); // 获取设备信息的id
		final newItemsModel newItems = getModel(newItemsModel.class, "new_items"); // 获取新添设备信息的id
		final repairItemsModel repairItems = getModel(repairItemsModel.class, "repair_items"); // 获取修改设备信息的序列化表单数据
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		final String nowTime = df.format(new Date()).toString(); // 获取当前系统时间
		
		boolean success = Db.tx(new IAtom() {  // 添加事务处理
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("status", 2).set("updateTime", nowTime).update();  // 将设备状态设置为待维修状态
	        		newItems.set("status", -1).update(); // 将新添状态设置为状态更改
	        		repairItems.set("items_id", items.getLong("id")).set("status", 0).save(); // 插入该条设备修改的信息，状态为待修改
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
		final newItemsModel newItems = getModel(newItemsModel.class, "new_items"); // 获取新添设备信息的id
		final scrapItemsModel scrapItems = getModel(scrapItemsModel.class, "scrap_items"); // 获取修改设备信息的序列化表单数据
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		final String nowTime = df.format(new Date()).toString(); // 获取当前系统时间
		
		boolean success = Db.tx(new IAtom() {  // 添加事务处理
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("status", 3).set("updateTime", nowTime).update();  // 将设备状态设置为待报废状态
	        		newItems.set("status", -1).update(); // 将新添状态设置为状态已更改
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
	 * 新添设备（Excel文件）批量上传
	 */
	public void uploadFileWithNewItemsExcel(){
		String userLoginId = getSession().getAttribute("userLoginId").toString(); // 获取登录人id

		UploadFile file = getFile("file");
		if(file==null){
			renderText("文件为空");
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置日期格式
		String nowTime = df.format(new Date()).toString(); // 获取当前系统时间
		try {
			ReadExcelFile.readFile(ReadExcelFile.TABLE_NEWITEMS , file.getFile(),userLoginId,nowTime); // 将数据写入数据库
			file.getFile().delete(); // 数据插入成功后删除Excel文件
		} catch (FileNotFoundException e) {
			System.out.println("找不到该文件");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("加载错误");
			e.printStackTrace();
		}
		renderText("123");
	}
	
	/**
	 * Excel模板下载
	 */
	public void downloadTemplate() {
		int type = getParaToInt("type"); // 获取要下载的文件类型 （0：说明文档；3：2003版本；7:2007版本）
		String realFile = null;
		if(type==3){
			realFile = PathKit.getWebRootPath() + "/template/" + "新添设备信息录入模板.xls"; // 根据路径获取文件-2003版
		}else if(type==7){
			realFile = PathKit.getWebRootPath() + "/template/" + "新添设备信息录入模板.xlsx"; // 根据路径获取文件-2007版
		}else{
			realFile = PathKit.getWebRootPath() + "/template/" + "批量文件上传使用说明文档.txt"; // 根据路径获取文件
		}
		renderFile(new File(realFile));
	}
	
	/**
	 * 为设备自动编码（编码规则：取6位基数，如果数量大于100,000 ，则将之前的数换算成A~Z，这样可存储26*100,000个设备。例如第一位数为A000000）
	 * @return 设备的唯一编码
	 */
	public String getCode(){
		String ALLCHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // 定义字符串
		String code = null; // 定义编码
		int count = ItemsModel.dao.getItemsCount()+1; // 查询设备除需购设备无编号之外的设备数量（包涵被删除的，因为被删除的设备有编号）
		
		 DecimalFormat df = new DecimalFormat("000000"); // 格式化为6位
		if(count<100000){ // 判断是否超过100,000
			code = ALLCHAR.charAt(0) +  df.format(count);  
		}else{
			
			code = ALLCHAR.charAt(count/100000) + df.format(count);  
		}
		return code;
	}
}
