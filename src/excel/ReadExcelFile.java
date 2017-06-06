package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.ItemsModel;
import model.needItemsModel;
import model.newItemsModel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

import controller.newController;


public class ReadExcelFile {
	/**
	 * 定义常量表示需购设备
	 */
	public static final Integer TABLE_NEEDITEMS = 0;  
	/**
	 * 定义常量表示新添设备
	 */
	public static final Integer TABLE_NEWITEMS = 1;  
	
	/**
	 * 读取文件，判断文件是否存在
	 * @param table 宏定义变量表明要导入的表
	 * @param file  获取上传的文件
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void readFile(Integer table ,File file,String loginId,String nowTime) throws FileNotFoundException, IOException{
		if(!file.exists()) return ; // 如果文件不存在 , 则无法访问
		readFile(table,file.getAbsolutePath(),loginId,nowTime);
	}
	/**
	 * 读取文件判断是否为Excel文件和区分版本类别，获取第一个选项卡的值
	 * @param table  宏定义变量表明要导入的表
	 * @param fileName 获取文件名
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	private static void readFile(Integer table , String fileName,String loginId,String nowTime) throws FileNotFoundException, IOException {
		Workbook workbook = null;
		if(fileName.endsWith(".xls")){// 用后缀去区别不同版本的Excel文件
			workbook = new HSSFWorkbook(new FileInputStream(fileName));
		}else if(fileName.endsWith(".xlsx")){
			workbook = new XSSFWorkbook(new FileInputStream(fileName));
		}else{
			return ;
		}
		Sheet sheet = null ;
		sheet = workbook.getSheetAt(0);// 获取第一个选项卡
		switch(table){
		case 0 : readFileWithNeedItems(sheet,loginId,nowTime);break;
		case 1 : readFileWithNewItems(sheet,loginId,nowTime);break;
		}
	}
	/**
	 * 将需购设备上传的文件信息写入数据库
	 * @param sheet 第几个标签
	 * @param loginId 登录人id
	 * @param nowTime 当前系统时间
	 */
	@SuppressWarnings("unused")
	private static void readFileWithNeedItems(final Sheet sheet,final String loginId,final String nowTime) {
		final int rowNum = sheet.getLastRowNum(); // 获取该标签的行数，（从0开始，比实际行数少一行）
		if(rowNum<1){
			return ;
		}
		final Row firstRow = sheet.getRow(0);  // 获取标签的第一行
		if(firstRow==null){
			return ;
		}
		final Map<String,String> mapItems = new HashMap<String,String>(); // 定义map对象，存入要存储items表的字段
		mapItems.put("设备名称", "name");
		mapItems.put("设备类别", "type");
		mapItems.put("设备型号", "modelNum");
		mapItems.put("设备规格", "norms");
		
		final Map<String,String> mapNeedItems = new HashMap<String,String>(); // 定义map对象，存入要存储need_items表的字段
		mapNeedItems.put("单价（元）", "price");
		mapNeedItems.put("需购数量", "number");
		mapNeedItems.put("购置日期", "purchaseDate");
		mapNeedItems.put("生产厂家", "vender");
		mapNeedItems.put("保质期（年）", "expirationDate");
		mapNeedItems.put("经办人", "userName");
		mapNeedItems.put("备注", "remark");
		
		final List<String> listItems = new ArrayList<String>();  // 定义数组存放Excel对应的items表字段
		final List<String> listNeedItems = new ArrayList<String>();
		final int colNum = firstRow.getPhysicalNumberOfCells();  // 获取第一行的列数
		
		boolean success = Db.tx(new IAtom() {    // 添加事务处理，如果中间有错误则回滚，数据不保存到数据库
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	// 循环将Excel表头对应的表字段存入list数组中
	        		for (int i = 0; i < colNum; i++) { 
	        			String cellTitle = firstRow.getCell(i).getStringCellValue(); // 获取Excel中标题
	        			if(mapItems.get(cellTitle) != null) // 判断表头字段对应是否为空
	        				listItems.add(mapItems.get(cellTitle)); // 获取第一行的标题，获取对应map中的字段名，添加到list列表中
	        			else if(mapNeedItems.get(cellTitle) != null)
	        				listNeedItems.add(mapNeedItems.get(cellTitle));
	        		}
	        		// 按行循环保存数据结构
	        		for (int i = 1; i <= rowNum; i++) {
	        			Row row = sheet.getRow(i);
	        			if(row==null){
	        				continue;
	        			}
	        			// 因为Excel中的数据要存放到不同
	        			ItemsModel items = new ItemsModel(); 
	        			for (int j = 0; j < listItems.size(); j++) {
	        				row.getCell(j).setCellType(Cell.CELL_TYPE_STRING); // 将cell中数字类型转化为字符串类型读入
	        				items.set(listItems.get(j), row.getCell(j).getStringCellValue()); // 每行中将数据库将字段名和cell中的值对应set存储
	        			}
	        			needItemsModel needItems = new needItemsModel(); 
	        			for (int j = 0; j < listNeedItems.size(); j++) {
	        				row.getCell(j+4).setCellType(Cell.CELL_TYPE_STRING); // 将cell中数字类型转化为字符串类型读入   加4是因为模板中前4个字段存入了items表中
	        				needItems.set(listNeedItems.get(j), row.getCell(j+4).getStringCellValue()); // 每行中将数据库将字段名和cell中的值对应set存储
	        			}
	        			items.set("user_login_id", loginId).set("status", 0).set("createTime", nowTime).set("updateTime", nowTime); // 需购设备不需要
	        			items.save(); // 先将基本数据插入数据库
	        			needItems.set("items_id", items.getLong("id")).set("status", 0); 
	        			needItems.save(); // 将数据插入数据库
	        		}
	            	
	            } catch (Exception e) {
	                e.printStackTrace();
	                return false;
	            }
	            return true;
	        }
	    });
		
	}

	/**
	 * 将新添设备上传的文件信息写入数据库
	 * @param sheet 第几个标签
	 * @param loginId 登录人id
	 * @param nowTime 当前系统时间
	 */
	@SuppressWarnings("unused")
	private static void readFileWithNewItems(final Sheet sheet,final String loginId,final String nowTime) {
		final int rowNum = sheet.getLastRowNum(); // 获取该标签的行数，（从0开始，比实际行数少一行）
		if(rowNum<1){
			return ;
		}
		final Row firstRow = sheet.getRow(0);  // 获取标签的第一行
		if(firstRow==null){
			return ;
		}
		final Map<String,String> mapItems = new HashMap<String,String>(); // 定义map对象，存入要存储items表的字段
		mapItems.put("设备名称", "name");
		mapItems.put("设备类别", "type");
		mapItems.put("设备型号", "modelNum");
		mapItems.put("设备规格", "norms");
		
		final Map<String,String> mapNewItems = new HashMap<String,String>(); // 定义map对象，存入要存储need_items表的字段
		mapNewItems.put("单价（元）", "price");
		mapNewItems.put("购置日期", "purchaseDate");
		mapNewItems.put("生产厂家", "vender");
		mapNewItems.put("保质期（年）", "expirationDate");
		mapNewItems.put("经办人", "userName");
		mapNewItems.put("备注", "remark");
		
		final List<String> listItems = new ArrayList<String>();  // 定义数组存放Excel对应的items表字段
		final List<String> listNewItems = new ArrayList<String>();
		final int colNum = firstRow.getPhysicalNumberOfCells();  // 获取第一行的列数
		
		boolean success = Db.tx(new IAtom() {    // 添加事务处理，如果中间有错误则回滚，数据不保存到数据库
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	// 循环将Excel表头对应的表字段存入list数组中
	        		for (int i = 0; i < colNum; i++) { 
	        			String cellTitle = firstRow.getCell(i).getStringCellValue(); // 获取Excel中标题
	        			if(mapItems.get(cellTitle) != null) // 判断表头字段对应是否为空
	        				listItems.add(mapItems.get(cellTitle)); // 获取第一行的标题，获取对应map中的字段名，添加到list列表中
	        			else if(mapNewItems.get(cellTitle) != null)
	        				listNewItems.add(mapNewItems.get(cellTitle));
	        		}
	        		// 按行循环保存数据结构
	        		for (int i = 1; i <= rowNum; i++) {
	        			Row row = sheet.getRow(i);
	        			if(row==null){
	        				continue;
	        			}
	        			// 因为Excel中的数据要存放到不同
	        			ItemsModel items = new ItemsModel(); 
	        			for (int j = 0; j < listItems.size(); j++) {
	        				row.getCell(j).setCellType(Cell.CELL_TYPE_STRING); // 将cell中数字类型转化为字符串类型读入
	        				items.set(listItems.get(j), row.getCell(j).getStringCellValue()); // 每行中将数据库将字段名和cell中的值对应set存储
	        			}
	        			newItemsModel newItems = new newItemsModel(); // 创建新添设备model对象
	        			for (int j = 0; j < listNewItems.size(); j++) {
	        				row.getCell(j+4).setCellType(Cell.CELL_TYPE_STRING); // 将cell中数字类型转化为字符串类型读入   加4是因为模板中前4个字段存入了items表中
	        				newItems.set(listNewItems.get(j), row.getCell(j+4).getStringCellValue()); // 每行中将数据库将字段名和cell中的值对应set存储
	        			}
	        			
	        			newController newControl = new newController(); // 创建对象，为获取设备编码
	        			items.set("code", newControl.getCode()).set("user_login_id", loginId).set("status", 1).set("createTime", nowTime).set("updateTime", nowTime); // 状态置为新添设备
	        			items.save(); // 先将基本数据插入数据库
	        			newItems.set("items_id", items.getLong("id")).set("status", 0); 
	        			newItems.save(); // 将数据插入数据库
	        		}
	            	
	            } catch (Exception e) {
	                e.printStackTrace();
	                return false;
	            }
	            return true;
	        }
	    });
	}
	
}
