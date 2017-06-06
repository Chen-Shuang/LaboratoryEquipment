package excel;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import model.newItemsModel;
import model.repairItemsModel;
import model.scrapItemsModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.jfinal.core.Controller;

/**
 * 导出Excel
 * @author 陈爽
 *
 */
public class WriteExcelFile extends Controller {
		
	
	
	/**
	 * 新添报表下载
	 * @param newItems 新添信息列表
	 * @param outputFileName 输出文件名 
	 * @param response 请求
	 * @throws IOException 输出流异常
	 */
		@SuppressWarnings("resource")
		public static void writeNewExcel(List<newItemsModel> newItems,String outputFileName, HttpServletResponse response) throws IOException {
			//创建HSSFWorkbook对象(excel的文档对象)  
		    HSSFWorkbook wb = new HSSFWorkbook();  
			//建立新的sheet对象（excel的表单）  
			HSSFSheet sheet=wb.createSheet("维修统计表");
			// 设置Excel样式
			HSSFCellStyle cellStyle = wb.createCellStyle(); 
			// 设置字体
			HSSFFont font = wb.createFont();    
			font.setFontName("宋体");    
			font.setFontHeightInPoints((short) 14);//设置字体大小
			cellStyle.setFont(font);//选择需要用到的字体格式 
			
			//在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个  
			HSSFRow row0=sheet.createRow(0);
			//创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个  
			HSSFCell cell=row0.createCell(0);  
			//设置单元格内容  
			cell.setCellValue("维修资金统计表");  
			//合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列  
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,8));  
			
			//在sheet里创建第二行
			HSSFRow row1 = sheet.createRow(1);
			// 创建单元格并设置单元格内容  
			row1.createCell(0).setCellValue("设备编号");  
			row1.createCell(1).setCellValue("设备名称");      
			row1.createCell(2).setCellValue("设备类别");  
			row1.createCell(3).setCellValue("设备型号");   
			row1.createCell(4).setCellValue("设备规格");  
			row1.createCell(5).setCellValue("购置日期");  
			row1.createCell(6).setCellValue("生产厂家");  
			row1.createCell(7).setCellValue("保质期（年）");
			row1.createCell(8).setCellValue("经办人");
			row1.createCell(9).setCellValue("单价（元）");
			
			// 循环输出数据
			for(int i=0;i<newItems.size();i++){ 
				//在sheet里创建第i行
				HSSFRow row_i = sheet.createRow(i+2); // 从第3行开始
				// 创建单元格并设置单元格内容  
				row_i.createCell(0).setCellValue(newItems.get(i).getStr("code"));  
				row_i.createCell(1).setCellValue(newItems.get(i).getStr("name"));      
				row_i.createCell(2).setCellValue(newItems.get(i).getStr("type"));  
				row_i.createCell(3).setCellValue(newItems.get(i).getStr("modelNum"));   
				row_i.createCell(4).setCellValue(newItems.get(i).getStr("norms"));  
				row_i.createCell(5).setCellValue(newItems.get(i).getDate("purchaseDate").toString());  
				row_i.createCell(6).setCellValue(newItems.get(i).getStr("vender"));  
				row_i.createCell(7).setCellValue(newItems.get(i).getInt("expirationDate"));
				row_i.createCell(8).setCellValue(newItems.get(i).getStr("userName"));
				row_i.createCell(9).setCellValue(newItems.get(i).getInt("price"));
			}
			  
			//输出Excel文件  
			OutputStream output=response.getOutputStream();  
			response.reset();  
			response.setHeader("Content-disposition", "attachment; filename=\""
		                        +URLEncoder.encode(outputFileName, "UTF-8")+ "\"");  
			response.setContentType("application/msexcel");          
			wb.write(output);  
			output.close();  // 关闭输出流
			    
		}
		
		/**
		 * 维修报表下载
		 * @param repairItems 维修信息列表
		 * @param outputFileName 输出文件名 
		 * @param response 请求
		 * @throws IOException 输出流异常
		 */
		@SuppressWarnings("resource")
		public static void writeRepairExcel(List<repairItemsModel> repairItems,String outputFileName, HttpServletResponse response) throws IOException {
			//创建HSSFWorkbook对象(excel的文档对象)  
		    HSSFWorkbook wb = new HSSFWorkbook();  
			//建立新的sheet对象（excel的表单）  
			HSSFSheet sheet=wb.createSheet("维修统计表");
			// 设置Excel样式
			HSSFCellStyle cellStyle = wb.createCellStyle(); 
			// 设置字体
			HSSFFont font = wb.createFont();    
			font.setFontName("宋体");    
			font.setFontHeightInPoints((short) 14);//设置字体大小
			cellStyle.setFont(font);//选择需要用到的字体格式 
			
			//在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个  
			HSSFRow row0=sheet.createRow(0);
			//创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个  
			HSSFCell cell=row0.createCell(0);  
			//设置单元格内容  
			cell.setCellValue("维修资金统计表");  
			//合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列  
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,8));  
			
			//在sheet里创建第二行
			HSSFRow row1 = sheet.createRow(1);
			// 创建单元格并设置单元格内容  
			row1.createCell(0).setCellValue("设备编号");  
			row1.createCell(1).setCellValue("设备名称");      
			row1.createCell(2).setCellValue("设备类别");  
			row1.createCell(3).setCellValue("设备型号");   
			row1.createCell(4).setCellValue("设备规格");  
			row1.createCell(5).setCellValue("修理厂家");  
			row1.createCell(6).setCellValue("修理日期");  
			row1.createCell(7).setCellValue("责任人");
			row1.createCell(8).setCellValue("修理费用");
			
			// 循环输出数据
			for(int i=0;i<repairItems.size();i++){ 
				//在sheet里创建第i行
				HSSFRow row_i = sheet.createRow(i+2); // 从第3行开始
				// 创建单元格并设置单元格内容  
				row_i.createCell(0).setCellValue(repairItems.get(i).getStr("code"));  
				row_i.createCell(1).setCellValue(repairItems.get(i).getStr("name"));      
				row_i.createCell(2).setCellValue(repairItems.get(i).getStr("type"));  
				row_i.createCell(3).setCellValue(repairItems.get(i).getStr("modelNum"));   
				row_i.createCell(4).setCellValue(repairItems.get(i).getStr("norms"));  
				row_i.createCell(5).setCellValue(repairItems.get(i).getStr("vender"));  
				row_i.createCell(6).setCellValue(repairItems.get(i).getDate("repairDate").toString());  
				row_i.createCell(7).setCellValue(repairItems.get(i).getStr("userName"));
				row_i.createCell(8).setCellValue(repairItems.get(i).getInt("price"));
			}
			  
			//输出Excel文件  
			OutputStream output=response.getOutputStream();  
			response.reset();  
			response.setHeader("Content-disposition", "attachment; filename=\""
		                        +URLEncoder.encode(outputFileName, "UTF-8")+ "\"");  
			response.setContentType("application/msexcel");          
			wb.write(output);  
			output.close();  // 关闭输出流
			    
		}
		
		/**
		 * 报废报表下载
		 * @param repairItems 报废信息列表
		 * @param outputFileName 输出文件名 
		 * @param response HttpServletResponse
		 * @throws IOException 输出流异常
		 */
		@SuppressWarnings("resource")
		public static void writeScrapExcel(List<scrapItemsModel> scrapItems,String outputFileName, HttpServletResponse response) throws IOException {
			//创建HSSFWorkbook对象(excel的文档对象)  
		    HSSFWorkbook wb = new HSSFWorkbook();  
			//建立新的sheet对象（excel的表单）  
			HSSFSheet sheet=wb.createSheet("报废统计表");
			// 设置Excel样式
			HSSFCellStyle cellStyle = wb.createCellStyle(); 
			// 设置字体
			HSSFFont font = wb.createFont();    
			font.setFontName("宋体");    
			font.setFontHeightInPoints((short) 14);//设置字体大小
			cellStyle.setFont(font);//选择需要用到的字体格式 
			
			//在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个  
			HSSFRow row0=sheet.createRow(0);
			//创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个  
			HSSFCell cell=row0.createCell(0);  
			//设置单元格内容  
			cell.setCellValue("报废设备统计表");  
			//合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列  
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,8));  
			
			//在sheet里创建第二行
			HSSFRow row1 = sheet.createRow(1);
			// 创建单元格并设置单元格内容  
			row1.createCell(0).setCellValue("设备编号");  
			row1.createCell(1).setCellValue("设备名称");      
			row1.createCell(2).setCellValue("设备类别");  
			row1.createCell(3).setCellValue("设备型号");   
			row1.createCell(4).setCellValue("设备规格");  
			row1.createCell(5).setCellValue("报废日期");  
			row1.createCell(6).setCellValue("经办人");
			
			// 循环输出数据
			for(int i=0;i<scrapItems.size();i++){ 
				//在sheet里创建第i行
				HSSFRow row_i = sheet.createRow(i+2); // 从第3行开始
				// 创建单元格并设置单元格内容  
				row_i.createCell(0).setCellValue(scrapItems.get(i).getStr("code"));  
				row_i.createCell(1).setCellValue(scrapItems.get(i).getStr("name"));      
				row_i.createCell(2).setCellValue(scrapItems.get(i).getStr("type"));  
				row_i.createCell(3).setCellValue(scrapItems.get(i).getStr("modelNum"));   
				row_i.createCell(4).setCellValue(scrapItems.get(i).getStr("norms"));  
				row_i.createCell(5).setCellValue(scrapItems.get(i).getDate("scrapDate").toString()); 
				row_i.createCell(6).setCellValue(scrapItems.get(i).getStr("userName")); 
			}
			  
			//输出Excel文件  
			OutputStream output=response.getOutputStream();  
			response.reset();  
			response.setHeader("Content-disposition", "attachment; filename=\""
		                        +URLEncoder.encode(outputFileName, "UTF-8")+ "\"");  
			response.setContentType("application/msexcel");          
			wb.write(output);  
			output.close();  // 关闭输出流
			    
		}
}
