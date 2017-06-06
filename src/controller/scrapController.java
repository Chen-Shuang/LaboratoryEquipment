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
 * �����豸
 * @author ��ˬ & 
 *
 */
public class scrapController extends Controller {

	public void index() {
		render("scrap.html");
	}
	
	/**
	 * ��ȡά�޵��豸��Ϣ
	 */
	public void getScrapItemsInfo() {
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
		Page<scrapItemsModel> scrapItems = scrapItemsModel.dao.getScrapItemsInfo(curr, size, search,sTime,eTime,status); // ��ѯά���豸��Ϣ
		renderJson(scrapItems);
	}
	
	/**
	 * ��ȡ�����豸��Ϣ
	 */
	public void getScrapItemInfo() {
		int itemsId = getParaToInt("itemsId"); // ��ȡ�豸id
		scrapItemsModel scrapItem = scrapItemsModel.dao.getScrapItemInfo(itemsId); // ��ȡ�豸��Ϣ
		renderJson(scrapItem);
	}
	
	/**
	 * �޸ı����豸��Ϣ
	 */
	public void updateItemInfo() {
		final ItemsModel items = getModel(ItemsModel.class, "items"); // ��ȡ���л�������
		final scrapItemsModel scrapItems = getModel(scrapItemsModel.class, "scrap_items"); // ��ȡ���л�������
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		final String nowTime = df.format(new Date()).toString(); // ��ȡ��ǰϵͳʱ��
		
		boolean success = Db.tx(new IAtom() {  // ���������
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("updateTime", nowTime).update();  // �޸��豸�Ļ�����Ϣ
	            	scrapItems.update(); // �޸ı����豸��Ϣ
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
		
		List<scrapItemsModel> scrapItems = scrapItemsModel.dao.getScrapItemsInfo(search,sTime,eTime,status); // ��ѯά���豸��Ϣ
		
		String fileName = "�����豸ͳ�Ʊ�.xls"; // �ļ���������
		
		WriteExcelFile.writeScrapExcel(scrapItems, fileName, getResponse()); // �����ĵ�
	}
}
