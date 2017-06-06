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
 * ����Excel
 * @author ��ˬ
 *
 */
public class WriteExcelFile extends Controller {
		
	
	
	/**
	 * ����������
	 * @param newItems ������Ϣ�б�
	 * @param outputFileName ����ļ��� 
	 * @param response ����
	 * @throws IOException ������쳣
	 */
		@SuppressWarnings("resource")
		public static void writeNewExcel(List<newItemsModel> newItems,String outputFileName, HttpServletResponse response) throws IOException {
			//����HSSFWorkbook����(excel���ĵ�����)  
		    HSSFWorkbook wb = new HSSFWorkbook();  
			//�����µ�sheet����excel�ı���  
			HSSFSheet sheet=wb.createSheet("ά��ͳ�Ʊ�");
			// ����Excel��ʽ
			HSSFCellStyle cellStyle = wb.createCellStyle(); 
			// ��������
			HSSFFont font = wb.createFont();    
			font.setFontName("����");    
			font.setFontHeightInPoints((short) 14);//���������С
			cellStyle.setFont(font);//ѡ����Ҫ�õ��������ʽ 
			
			//��sheet�ﴴ����һ�У�����Ϊ������(excel����)��������0��65535֮����κ�һ��  
			HSSFRow row0=sheet.createRow(0);
			//������Ԫ��excel�ĵ�Ԫ�񣬲���Ϊ��������������0��255֮����κ�һ��  
			HSSFCell cell=row0.createCell(0);  
			//���õ�Ԫ������  
			cell.setCellValue("ά���ʽ�ͳ�Ʊ�");  
			//�ϲ���Ԫ��CellRangeAddress����������α�ʾ��ʼ�У������У���ʼ�У� ������  
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,8));  
			
			//��sheet�ﴴ���ڶ���
			HSSFRow row1 = sheet.createRow(1);
			// ������Ԫ�����õ�Ԫ������  
			row1.createCell(0).setCellValue("�豸���");  
			row1.createCell(1).setCellValue("�豸����");      
			row1.createCell(2).setCellValue("�豸���");  
			row1.createCell(3).setCellValue("�豸�ͺ�");   
			row1.createCell(4).setCellValue("�豸���");  
			row1.createCell(5).setCellValue("��������");  
			row1.createCell(6).setCellValue("��������");  
			row1.createCell(7).setCellValue("�����ڣ��꣩");
			row1.createCell(8).setCellValue("������");
			row1.createCell(9).setCellValue("���ۣ�Ԫ��");
			
			// ѭ���������
			for(int i=0;i<newItems.size();i++){ 
				//��sheet�ﴴ����i��
				HSSFRow row_i = sheet.createRow(i+2); // �ӵ�3�п�ʼ
				// ������Ԫ�����õ�Ԫ������  
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
			  
			//���Excel�ļ�  
			OutputStream output=response.getOutputStream();  
			response.reset();  
			response.setHeader("Content-disposition", "attachment; filename=\""
		                        +URLEncoder.encode(outputFileName, "UTF-8")+ "\"");  
			response.setContentType("application/msexcel");          
			wb.write(output);  
			output.close();  // �ر������
			    
		}
		
		/**
		 * ά�ޱ�������
		 * @param repairItems ά����Ϣ�б�
		 * @param outputFileName ����ļ��� 
		 * @param response ����
		 * @throws IOException ������쳣
		 */
		@SuppressWarnings("resource")
		public static void writeRepairExcel(List<repairItemsModel> repairItems,String outputFileName, HttpServletResponse response) throws IOException {
			//����HSSFWorkbook����(excel���ĵ�����)  
		    HSSFWorkbook wb = new HSSFWorkbook();  
			//�����µ�sheet����excel�ı���  
			HSSFSheet sheet=wb.createSheet("ά��ͳ�Ʊ�");
			// ����Excel��ʽ
			HSSFCellStyle cellStyle = wb.createCellStyle(); 
			// ��������
			HSSFFont font = wb.createFont();    
			font.setFontName("����");    
			font.setFontHeightInPoints((short) 14);//���������С
			cellStyle.setFont(font);//ѡ����Ҫ�õ��������ʽ 
			
			//��sheet�ﴴ����һ�У�����Ϊ������(excel����)��������0��65535֮����κ�һ��  
			HSSFRow row0=sheet.createRow(0);
			//������Ԫ��excel�ĵ�Ԫ�񣬲���Ϊ��������������0��255֮����κ�һ��  
			HSSFCell cell=row0.createCell(0);  
			//���õ�Ԫ������  
			cell.setCellValue("ά���ʽ�ͳ�Ʊ�");  
			//�ϲ���Ԫ��CellRangeAddress����������α�ʾ��ʼ�У������У���ʼ�У� ������  
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,8));  
			
			//��sheet�ﴴ���ڶ���
			HSSFRow row1 = sheet.createRow(1);
			// ������Ԫ�����õ�Ԫ������  
			row1.createCell(0).setCellValue("�豸���");  
			row1.createCell(1).setCellValue("�豸����");      
			row1.createCell(2).setCellValue("�豸���");  
			row1.createCell(3).setCellValue("�豸�ͺ�");   
			row1.createCell(4).setCellValue("�豸���");  
			row1.createCell(5).setCellValue("������");  
			row1.createCell(6).setCellValue("��������");  
			row1.createCell(7).setCellValue("������");
			row1.createCell(8).setCellValue("�������");
			
			// ѭ���������
			for(int i=0;i<repairItems.size();i++){ 
				//��sheet�ﴴ����i��
				HSSFRow row_i = sheet.createRow(i+2); // �ӵ�3�п�ʼ
				// ������Ԫ�����õ�Ԫ������  
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
			  
			//���Excel�ļ�  
			OutputStream output=response.getOutputStream();  
			response.reset();  
			response.setHeader("Content-disposition", "attachment; filename=\""
		                        +URLEncoder.encode(outputFileName, "UTF-8")+ "\"");  
			response.setContentType("application/msexcel");          
			wb.write(output);  
			output.close();  // �ر������
			    
		}
		
		/**
		 * ���ϱ�������
		 * @param repairItems ������Ϣ�б�
		 * @param outputFileName ����ļ��� 
		 * @param response HttpServletResponse
		 * @throws IOException ������쳣
		 */
		@SuppressWarnings("resource")
		public static void writeScrapExcel(List<scrapItemsModel> scrapItems,String outputFileName, HttpServletResponse response) throws IOException {
			//����HSSFWorkbook����(excel���ĵ�����)  
		    HSSFWorkbook wb = new HSSFWorkbook();  
			//�����µ�sheet����excel�ı���  
			HSSFSheet sheet=wb.createSheet("����ͳ�Ʊ�");
			// ����Excel��ʽ
			HSSFCellStyle cellStyle = wb.createCellStyle(); 
			// ��������
			HSSFFont font = wb.createFont();    
			font.setFontName("����");    
			font.setFontHeightInPoints((short) 14);//���������С
			cellStyle.setFont(font);//ѡ����Ҫ�õ��������ʽ 
			
			//��sheet�ﴴ����һ�У�����Ϊ������(excel����)��������0��65535֮����κ�һ��  
			HSSFRow row0=sheet.createRow(0);
			//������Ԫ��excel�ĵ�Ԫ�񣬲���Ϊ��������������0��255֮����κ�һ��  
			HSSFCell cell=row0.createCell(0);  
			//���õ�Ԫ������  
			cell.setCellValue("�����豸ͳ�Ʊ�");  
			//�ϲ���Ԫ��CellRangeAddress����������α�ʾ��ʼ�У������У���ʼ�У� ������  
			sheet.addMergedRegion(new CellRangeAddress(0,0,0,8));  
			
			//��sheet�ﴴ���ڶ���
			HSSFRow row1 = sheet.createRow(1);
			// ������Ԫ�����õ�Ԫ������  
			row1.createCell(0).setCellValue("�豸���");  
			row1.createCell(1).setCellValue("�豸����");      
			row1.createCell(2).setCellValue("�豸���");  
			row1.createCell(3).setCellValue("�豸�ͺ�");   
			row1.createCell(4).setCellValue("�豸���");  
			row1.createCell(5).setCellValue("��������");  
			row1.createCell(6).setCellValue("������");
			
			// ѭ���������
			for(int i=0;i<scrapItems.size();i++){ 
				//��sheet�ﴴ����i��
				HSSFRow row_i = sheet.createRow(i+2); // �ӵ�3�п�ʼ
				// ������Ԫ�����õ�Ԫ������  
				row_i.createCell(0).setCellValue(scrapItems.get(i).getStr("code"));  
				row_i.createCell(1).setCellValue(scrapItems.get(i).getStr("name"));      
				row_i.createCell(2).setCellValue(scrapItems.get(i).getStr("type"));  
				row_i.createCell(3).setCellValue(scrapItems.get(i).getStr("modelNum"));   
				row_i.createCell(4).setCellValue(scrapItems.get(i).getStr("norms"));  
				row_i.createCell(5).setCellValue(scrapItems.get(i).getDate("scrapDate").toString()); 
				row_i.createCell(6).setCellValue(scrapItems.get(i).getStr("userName")); 
			}
			  
			//���Excel�ļ�  
			OutputStream output=response.getOutputStream();  
			response.reset();  
			response.setHeader("Content-disposition", "attachment; filename=\""
		                        +URLEncoder.encode(outputFileName, "UTF-8")+ "\"");  
			response.setContentType("application/msexcel");          
			wb.write(output);  
			output.close();  // �ر������
			    
		}
}
