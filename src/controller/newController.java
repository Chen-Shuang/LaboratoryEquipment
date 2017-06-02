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
 * �����豸
 * @author ��ˬ 
 *
 */
public class newController extends Controller {

	public void index() {
		render("new.html");
	}
	
	/**
	 * ��ȡ�����豸��Ϣ
	 */
	public void getNewItemsInfo() {
		int curr = getParaToInt("curr"); // ��ȡҳ��
		int size = getParaToInt("size"); // ��ȡҳ����ʾ����
		String search = getPara("search"); // ��ȡ��������
		String sTime = getPara("sTime"); // ��ȡ��ʼʱ��  ���������ǲ�ѯ��
		String eTime = getPara("eTime"); // ��ȡ��ֹʱ��
		
		// �ж��Ƿ�ѡ����ʱ��,ʱ���ʽת��
		if(sTime==""){
			sTime = "1900-01-01";
		}
		if(eTime==""){
			eTime += "2099-06-16"; // ��ǰϵͳʱ��
		}
		Page<newItemsModel> newItems = newItemsModel.dao.getNewItemsInfo(curr, size, search,sTime,eTime); // ��ѯ�����豸��Ϣ
		renderJson(newItems);
	}
	
	/**
	 * ����蹺�豸
	 */
	public void addNewItem() {
		final String userLoginId = getSession().getAttribute("userLoginId").toString(); // ��ȡ��¼��id
		
		final ItemsModel items = getModel(ItemsModel.class, "items"); // ��ȡ���л�������
		final newItemsModel newItems = getModel(newItemsModel.class, "new_items"); // ��ȡ���л�������
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		final String nowTime = df.format(new Date()).toString(); // ��ȡ��ǰϵͳʱ��
		
		boolean success = Db.tx(new IAtom() {  // ���������
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("code", getCode()).set("createTime", nowTime).set("updateTime", nowTime)
	            		.set("status", 1).set("user_login_id", userLoginId).save();  // ���������豸�Ļ�����Ϣ
	        		newItems.set("items_id", items.getLong("id")).set("status", 0).save(); // ���������豸��Ϣ{items.getLong("id") ��ȡ���������id}
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
	 * ��ȡ�����豸��Ϣ
	 */
	public void getNewItemInfo() {
		int itemsId = getParaToInt("itemsId"); // ��ȡ�豸id
		newItemsModel newItem = newItemsModel.dao.getNewItemInfo(itemsId); // ��ȡ�豸��Ϣ
		renderJson(newItem);
	}
	
	/**
	 * �޸��豸��Ϣ
	 */
	public void updateItemInfo() {
		final ItemsModel items = getModel(ItemsModel.class, "items"); // ��ȡ���л�������
		final newItemsModel newItems = getModel(newItemsModel.class, "new_items"); // ��ȡ���л�������
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		final String nowTime = df.format(new Date()).toString(); // ��ȡ��ǰϵͳʱ��
		
		boolean success = Db.tx(new IAtom() {  // ���������
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("updateTime", nowTime).update();  // �޸������豸�Ļ�����Ϣ
	        		newItems.update(); // �޸������豸��Ϣ
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
	 * ɾ�������豸��Ϣ
	 */
	public void delItemInfo() {
		int itemsId = getParaToInt("itemsId"); // ��ȡ�豸id
		int count = ItemsModel.dao.delItemInfo(itemsId);
		if(count>0){
			renderText("success");
		}else{
			renderText("error");
		}
		
	}
	
	/**
	 * �豸״̬����Ϊά��
	 */
	public void toRepairItem(){
		final ItemsModel items = getModel(ItemsModel.class, "items"); // ��ȡ�豸��Ϣ��id
		final newItemsModel newItems = getModel(newItemsModel.class, "new_items"); // ��ȡ�����豸��Ϣ��id
		final repairItemsModel repairItems = getModel(repairItemsModel.class, "repair_items"); // ��ȡ�޸��豸��Ϣ�����л�������
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		final String nowTime = df.format(new Date()).toString(); // ��ȡ��ǰϵͳʱ��
		
		boolean success = Db.tx(new IAtom() {  // ���������
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("status", 2).set("updateTime", nowTime).update();  // ���豸״̬����Ϊ��ά��״̬
	        		newItems.set("status", -1).update(); // ������״̬����Ϊ״̬����
	        		repairItems.set("items_id", items.getLong("id")).set("status", 0).save(); // ��������豸�޸ĵ���Ϣ��״̬Ϊ���޸�
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
	 * �豸״̬����Ϊ����
	 */
	public void toScrapItem(){
		final ItemsModel items = getModel(ItemsModel.class, "items"); // ��ȡ�豸��Ϣ��id
		final newItemsModel newItems = getModel(newItemsModel.class, "new_items"); // ��ȡ�����豸��Ϣ��id
		final scrapItemsModel scrapItems = getModel(scrapItemsModel.class, "scrap_items"); // ��ȡ�޸��豸��Ϣ�����л�������
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		final String nowTime = df.format(new Date()).toString(); // ��ȡ��ǰϵͳʱ��
		
		boolean success = Db.tx(new IAtom() {  // ���������
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("status", 3).set("updateTime", nowTime).update();  // ���豸״̬����Ϊ������״̬
	        		newItems.set("status", -1).update(); // ������״̬����Ϊ״̬�Ѹ���
	        		scrapItems.set("items_id", items.getLong("id")).set("status", 0).save(); // ��������豸�޸ĵ���Ϣ��״̬Ϊ�����
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
	 * �����豸��Excel�ļ��������ϴ�
	 */
	public void uploadFileWithNewItemsExcel(){
		String userLoginId = getSession().getAttribute("userLoginId").toString(); // ��ȡ��¼��id

		UploadFile file = getFile("file");
		if(file==null){
			renderText("�ļ�Ϊ��");
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //�������ڸ�ʽ
		String nowTime = df.format(new Date()).toString(); // ��ȡ��ǰϵͳʱ��
		try {
			ReadExcelFile.readFile(ReadExcelFile.TABLE_NEWITEMS , file.getFile(),userLoginId,nowTime); // ������д�����ݿ�
			file.getFile().delete(); // ���ݲ���ɹ���ɾ��Excel�ļ�
		} catch (FileNotFoundException e) {
			System.out.println("�Ҳ������ļ�");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("���ش���");
			e.printStackTrace();
		}
		renderText("123");
	}
	
	/**
	 * Excelģ������
	 */
	public void downloadTemplate() {
		int type = getParaToInt("type"); // ��ȡҪ���ص��ļ����� ��0��˵���ĵ���3��2003�汾��7:2007�汾��
		String realFile = null;
		if(type==3){
			realFile = PathKit.getWebRootPath() + "/template/" + "�����豸��Ϣ¼��ģ��.xls"; // ����·����ȡ�ļ�-2003��
		}else if(type==7){
			realFile = PathKit.getWebRootPath() + "/template/" + "�����豸��Ϣ¼��ģ��.xlsx"; // ����·����ȡ�ļ�-2007��
		}else{
			realFile = PathKit.getWebRootPath() + "/template/" + "�����ļ��ϴ�ʹ��˵���ĵ�.txt"; // ����·����ȡ�ļ�
		}
		renderFile(new File(realFile));
	}
	
	/**
	 * Ϊ�豸�Զ����루�������ȡ6λ�����������������100,000 ����֮ǰ���������A~Z�������ɴ洢26*100,000���豸�������һλ��ΪA000000��
	 * @return �豸��Ψһ����
	 */
	public String getCode(){
		String ALLCHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // �����ַ���
		String code = null; // �������
		int count = ItemsModel.dao.getItemsCount()+1; // ��ѯ�豸���蹺�豸�ޱ��֮����豸������������ɾ���ģ���Ϊ��ɾ�����豸�б�ţ�
		
		 DecimalFormat df = new DecimalFormat("000000"); // ��ʽ��Ϊ6λ
		if(count<100000){ // �ж��Ƿ񳬹�100,000
			code = ALLCHAR.charAt(0) +  df.format(count);  
		}else{
			
			code = ALLCHAR.charAt(count/100000) + df.format(count);  
		}
		return code;
	}
}
