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
 * �����豸
 * @author ��ˬ 
 *
 */
public class repairController extends Controller {

	public void index() {
		render("repair.html");
	}
	
	/**
	 * ��ȡά�޵��豸��Ϣ
	 */
	public void getRepairItemsInfo() {
		int curr = getParaToInt("curr"); // ��ȡҳ��
		int size = getParaToInt("size"); // ��ȡҳ����ʾ����
		String search = getPara("search"); // ��ȡ��������
		String sTime = getPara("sTime"); // ��ȡ��ʼʱ��  ���������ǲ�ѯ��
		String eTime = getPara("eTime"); // ��ȡ��ֹʱ��
		int status = getParaToInt("status"); // ��ȡά��״̬
		
		// �ж��Ƿ�ѡ����ʱ��,ʱ���ʽת��
		if(sTime==""){
			sTime = "1900-01-01";
		}
		if(eTime==""){
			eTime += "2099-06-16"; // ��ǰϵͳʱ��
		}
		Page<repairItemsModel> repairItems = repairItemsModel.dao.getRepairItemsInfo(curr, size, search,sTime,eTime,status); // ��ѯά���豸��Ϣ
		renderJson(repairItems);
	}
	
	/**
	 * ��ȡ�����豸��Ϣ
	 */
	public void getRepairItemInfo() {
		int itemsId = getParaToInt("itemsId"); // ��ȡ�豸id
		repairItemsModel repairItem = repairItemsModel.dao.getRepairItemInfo(itemsId); // ��ȡ�豸��Ϣ
		renderJson(repairItem);
	}
	
	/**
	 * �޸��豸��Ϣ
	 */
	public void updateItemInfo() {
		final ItemsModel items = getModel(ItemsModel.class, "items"); // ��ȡ���л�������
		final repairItemsModel repairItems = getModel(repairItemsModel.class, "repair_items"); // ��ȡ���л�������
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		final String nowTime = df.format(new Date()).toString(); // ��ȡ��ǰϵͳʱ��
		
		boolean success = Db.tx(new IAtom() {  // ���������
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("updateTime", nowTime).update();  // �޸��豸�Ļ�����Ϣ
	            	repairItems.update(); // �޸ı����豸��Ϣ
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
	 * �豸���ά��
	 */
	public void toNewItem(){
		final int itemsId = getParaToInt("itemsId"); // ��ȡ�豸id
		
		boolean success = Db.tx(new IAtom() {  // ���������
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	ItemsModel.dao.updateToNewItem(itemsId); // �޸Ļ�����Ϣ״̬Ϊ�����豸
	            	newItemsModel.dao.updateRepairFinish(itemsId); // ���������豸���е�״̬Ϊ���ά��
	            	repairItemsModel.dao.RepairFinish(itemsId);   // ����ά�ޱ���״̬Ϊ�����ά��
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
		final repairItemsModel repairItems = getModel(repairItemsModel.class, "repair_items"); // ��ȡ�����豸��Ϣ��id
		final scrapItemsModel scrapItems = getModel(scrapItemsModel.class, "scrap_items"); // ��ȡ�޸��豸��Ϣ�����л�������
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		final String nowTime = df.format(new Date()).toString(); // ��ȡ��ǰϵͳʱ��
		
		boolean success = Db.tx(new IAtom() {  // ���������
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("status", 3).set("updateTime", nowTime).update();  // ���豸״̬����Ϊ������״̬
	            	repairItems.set("status", 2).update(); // ��ά��״̬����Ϊά��ʧ�ܱ���                                                                                                  
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
	 * ���أ�Excel�ļ���
	 * @throws IOException 
	 */
	public void downloadExcel() throws IOException{
		String search = getPara("search"); // ��ȡ��������
		String sTime = getPara("sTime"); // ��ȡ��ʼʱ��  ���������ǲ�ѯ��
		String eTime = getPara("eTime"); // ��ȡ��ֹʱ��
		int status = getParaToInt("status"); // ��ȡά��״̬
		
		// �ж��Ƿ�ѡ����ʱ��,ʱ���ʽת��
		if(sTime==""){
			sTime = "1900-01-01";
		}
		if(eTime==""){
			eTime += "2099-06-16"; // ��ǰϵͳʱ��
		}
		
		List<repairItemsModel> repairItems = repairItemsModel.dao.getRepairItemsInfo(search,sTime,eTime,status); // ��ȡ��ѯ����
		
		String fileName = "ά���ʽ�ͳ�Ʊ�.xls"; // �ļ���������
		
		WriteExcelFile.writeRepairExcel(repairItems, fileName, getResponse()); // �����ĵ�
		
	}
}
