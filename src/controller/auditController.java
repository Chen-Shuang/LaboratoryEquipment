package controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.ItemsModel;
import model.needItemsModel;
import model.newItemsModel;
import model.scrapItemsModel;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
/**
 * 领导审核界面
 * @author 陈爽
 *
 */
public class auditController extends Controller {

	public void index() {
		render("audit.html");
	}
	
	/**
	 * 需购设备审核通过
	 */
	public void auditPassNeedItem() {
		final ItemsModel items = getModel(ItemsModel.class, "items"); // 获取序列化表单数据
		final needItemsModel needItems = getModel(needItemsModel.class, "need_items"); // 获取序列化表单数据
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		final String nowTime = df.format(new Date()).toString(); // 获取当前系统时间
		
		boolean success = Db.tx(new IAtom() {  // 添加事务处理
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("updateTime", nowTime).update();  // 修改需购设备的基本信息
	            	needItems.set("status", 1).update(); // 修改需购设备状态为通过
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
	 * 需购设备审核驳回
	 */
	public void auditRejectNeedItem() {
		final ItemsModel items = getModel(ItemsModel.class, "items"); // 获取序列化表单数据
		final needItemsModel needItems = getModel(needItemsModel.class, "need_items"); // 获取序列化表单数据
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		final String nowTime = df.format(new Date()).toString(); // 获取当前系统时间
		
		boolean success = Db.tx(new IAtom() {  // 添加事务处理
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("updateTime", nowTime).update();  // 修改需购设备的基本信息
	        		needItems.set("status", -1).update(); // 修改需购设备状态为驳回
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
	 * 报废设备审核通过
	 */
	public void auditPassScrapItem() {
		final ItemsModel items = getModel(ItemsModel.class, "items"); // 获取序列化表单数据
		final scrapItemsModel scrapItems = getModel(scrapItemsModel.class, "scrap_items"); // 获取序列化表单数据
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		final String nowTime = df.format(new Date()).toString(); // 获取当前系统时间
		
		boolean success = Db.tx(new IAtom() {  // 添加事务处理
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("updateTime", nowTime).update();  // 修改设备的基本信息
	            	scrapItems.set("status", 1).update(); // 修改报废设备审核状态为通过
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
	 * 报废设备审核驳回（如果驳回，则删除该条记录，将该设备状态改为新添设备）
	 */
	public void auditRejectScrapItem() {
		final ItemsModel items = getModel(ItemsModel.class, "items"); // 获取序列化表单数据
		final scrapItemsModel scrapItems = getModel(scrapItemsModel.class, "scrap_items"); // 获取序列化表单数据
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		final String nowTime = df.format(new Date()).toString(); // 获取当前系统时间
		
		boolean success = Db.tx(new IAtom() {  // 添加事务处理
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("status", 1).set("updateTime", nowTime).update();  // 修改设备的基本信息状态为新添设备
	            	newItemsModel.dao.auditRejectScrap(items.getLong("id"));  // 更新新添设备状态
	            	scrapItems.set("status", -1).update(); // 修改报废设备审核状态为驳回
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
}
