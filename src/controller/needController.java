package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import word.PrintWord;
import model.ItemsModel;
import model.needItemsModel;

import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;

import excel.ReadExcelFile;
import freemarker.template.TemplateException;

/**
 * 需购设备
 * @author 陈爽 
 *
 */
public class needController extends Controller {

	public void index() {
		render("need.html");
	}
	
	/**
	 * 添加需购设备
	 */
	public void addNeedItem() {
		final String userLoginId = getSession().getAttribute("userLoginId").toString(); // 获取登录人id
		
		final ItemsModel items = getModel(ItemsModel.class, "items"); // 获取序列化表单数据
		final needItemsModel needItems = getModel(needItemsModel.class, "need_items"); // 获取序列化表单数据
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		final String nowTime = df.format(new Date()).toString(); // 获取当前系统时间
		
		boolean success = Db.tx(new IAtom() {  // 添加事务处理
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("createTime", nowTime).set("updateTime", nowTime)
	            		.set("status", 0).set("user_login_id", userLoginId).save();  // 保存需购设备的基本信息，需购设备中设备没有真正意义上入库，所以没有编码
	        		needItems.set("items_id", items.getLong("id")).set("status", 0).save(); // 保存需购设备信息{items.getLong("id") 获取保存后对象的id}
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
	 * 获取需购设备信息
	 */
	public void getNeedItemsInfo() {
		int curr = getParaToInt("curr"); // 获取页码
		int size = getParaToInt("size"); // 获取页面显示数据
		String search = getPara("search"); // 获取搜索条件
		String sTime = getPara("sTime"); // 获取起始时间  日期搜索是查询的
		String eTime = getPara("eTime"); // 获取终止时间
		int needStatus = getParaToInt("needStatus"); // 获取审核状态
		
		// 判断是否选择了时间,时间格式转换
		if(sTime==""){
			sTime = "1900-01-01";
		}
		if(eTime==""){
			eTime += "2099-06-16"; // 当前系统时间
		}
		Page<needItemsModel> needItems = needItemsModel.dao.getNeedItemsInfo(curr, size, search,sTime,eTime,needStatus); // 查询需购设备信息
		renderJson(needItems);
	}
	
	/**
	 * 获取单个设备信息
	 */
	public void getNeedItemInfo() {
		int itemsId = getParaToInt("itemsId"); // 获取设备id
		needItemsModel needItem = needItemsModel.dao.getNeedItemInfo(itemsId); // 获取设备信息
		renderJson(needItem);
	}
	
	/**
	 * 修改设备信息
	 */
	public void updateItemInfo() {
		final ItemsModel items = getModel(ItemsModel.class, "items"); // 获取序列化表单数据
		final needItemsModel needItems = getModel(needItemsModel.class, "need_items"); // 获取序列化表单数据
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		final String nowTime = df.format(new Date()).toString(); // 获取当前系统时间
		
		boolean success = Db.tx(new IAtom() {  // 添加事务处理
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("updateTime", nowTime).update();  // 修改需购设备的基本信息
	        		needItems.update(); // 修改需购设备信息
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
	 * 修改设备状态为已购买
	 */
	public void buyItemInfo() {
		int itemsId = getParaToInt("itemsId"); // 获取设备id
		int count = ItemsModel.dao.buyItemInfo(itemsId); // 更新状态
		if(count>0){
			renderText("success");
		}else{
			renderText("error");
		}
		
	}
	
	/**
	 * 需购设备（Excel文件）批量上传
	 */
	public void uploadFileWithNeedItemsExcel(){
		String userLoginId = getSession().getAttribute("userLoginId").toString(); // 获取登录人id

		UploadFile file = getFile("file");
		if(file==null){
			renderText("文件为空");
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置日期格式
		String nowTime = df.format(new Date()).toString(); // 获取当前系统时间
		try {
			ReadExcelFile.readFile(ReadExcelFile.TABLE_NEEDITEMS , file.getFile(),userLoginId,nowTime); // 将数据写入数据库
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
			realFile = PathKit.getWebRootPath() + "/template/" + "需购设备信息录入模板.xls"; // 根据路径获取文件-2003版
		}else if(type==7){
			realFile = PathKit.getWebRootPath() + "/template/" + "需购设备信息录入模板.xlsx"; // 根据路径获取文件-2007版
		}else{
			realFile = PathKit.getWebRootPath() + "/template/" + "批量文件上传使用说明文档.txt"; // 根据路径获取文件
		}
		renderFile(new File(realFile));
	}
	
	/**
	 * 需购设备申请表下载
	 * @throws TemplateException
	 * @throws IOException
	 */
	public void downLoadWord() throws TemplateException, IOException {
		String search = getPara("search"); // 获取搜索条件
		int needStatus = getParaToInt("needStatus"); // 获取状态
		String sTime = getPara("sTime");   // 获取起始时间 
		String eTime = getPara("eTime");   // 获取终止时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String nowTime = df.format(new Date()).toString(); // 获取当前系统时间
		String time[] = nowTime.split("-");
		// 判断是否选择了时间,时间格式转换
		if(sTime==""){
			sTime = "1900-01-01";
		}
		if(eTime==""){
			eTime += "2099-06-16"; // 当前系统时间
		}
		List<needItemsModel> needItems = needItemsModel.dao.getNeedItemsInfo(search,sTime,eTime,needStatus); // 查询需购设备信息
		
		String fileName =  "需购设备申请表.doc"; 
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("needItems", needItems);  // 需购信息
		dataMap.put("year", time[0]);  // 存储当前年份
		dataMap.put("mouth", time[1]); // 存储当前月份
		dataMap.put("date", time[2]);  // 存储当前日期
		PrintWord.taskBook("needItems.ftl", fileName, dataMap, getResponse());
	}
}
