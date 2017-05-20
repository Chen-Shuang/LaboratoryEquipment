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
 * �蹺�豸
 * @author ��ˬ 
 *
 */
public class needController extends Controller {

	public void index() {
		render("need.html");
	}
	
	/**
	 * ����蹺�豸
	 */
	public void addNeedItem() {
		final String userLoginId = getSession().getAttribute("userLoginId").toString(); // ��ȡ��¼��id
		
		final ItemsModel items = getModel(ItemsModel.class, "items"); // ��ȡ���л�������
		final needItemsModel needItems = getModel(needItemsModel.class, "need_items"); // ��ȡ���л�������
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		final String nowTime = df.format(new Date()).toString(); // ��ȡ��ǰϵͳʱ��
		
		boolean success = Db.tx(new IAtom() {  // ���������
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("createTime", nowTime).set("updateTime", nowTime)
	            		.set("status", 0).set("user_login_id", userLoginId).save();  // �����蹺�豸�Ļ�����Ϣ���蹺�豸���豸û��������������⣬����û�б���
	        		needItems.set("items_id", items.getLong("id")).set("status", 0).save(); // �����蹺�豸��Ϣ{items.getLong("id") ��ȡ���������id}
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
	 * ��ȡ�蹺�豸��Ϣ
	 */
	public void getNeedItemsInfo() {
		int curr = getParaToInt("curr"); // ��ȡҳ��
		int size = getParaToInt("size"); // ��ȡҳ����ʾ����
		String search = getPara("search"); // ��ȡ��������
		String sTime = getPara("sTime"); // ��ȡ��ʼʱ��  ���������ǲ�ѯ��
		String eTime = getPara("eTime"); // ��ȡ��ֹʱ��
		int needStatus = getParaToInt("needStatus"); // ��ȡ���״̬
		
		// �ж��Ƿ�ѡ����ʱ��,ʱ���ʽת��
		if(sTime==""){
			sTime = "1900-01-01";
		}
		if(eTime==""){
			eTime += "2099-06-16"; // ��ǰϵͳʱ��
		}
		Page<needItemsModel> needItems = needItemsModel.dao.getNeedItemsInfo(curr, size, search,sTime,eTime,needStatus); // ��ѯ�蹺�豸��Ϣ
		renderJson(needItems);
	}
	
	/**
	 * ��ȡ�����豸��Ϣ
	 */
	public void getNeedItemInfo() {
		int itemsId = getParaToInt("itemsId"); // ��ȡ�豸id
		needItemsModel needItem = needItemsModel.dao.getNeedItemInfo(itemsId); // ��ȡ�豸��Ϣ
		renderJson(needItem);
	}
	
	/**
	 * �޸��豸��Ϣ
	 */
	public void updateItemInfo() {
		final ItemsModel items = getModel(ItemsModel.class, "items"); // ��ȡ���л�������
		final needItemsModel needItems = getModel(needItemsModel.class, "need_items"); // ��ȡ���л�������
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		final String nowTime = df.format(new Date()).toString(); // ��ȡ��ǰϵͳʱ��
		
		boolean success = Db.tx(new IAtom() {  // ���������
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("updateTime", nowTime).update();  // �޸��蹺�豸�Ļ�����Ϣ
	        		needItems.update(); // �޸��蹺�豸��Ϣ
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
	 * �޸��豸״̬Ϊ�ѹ���
	 */
	public void buyItemInfo() {
		int itemsId = getParaToInt("itemsId"); // ��ȡ�豸id
		int count = ItemsModel.dao.buyItemInfo(itemsId); // ����״̬
		if(count>0){
			renderText("success");
		}else{
			renderText("error");
		}
		
	}
	
	/**
	 * �蹺�豸��Excel�ļ��������ϴ�
	 */
	public void uploadFileWithNeedItemsExcel(){
		String userLoginId = getSession().getAttribute("userLoginId").toString(); // ��ȡ��¼��id

		UploadFile file = getFile("file");
		if(file==null){
			renderText("�ļ�Ϊ��");
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //�������ڸ�ʽ
		String nowTime = df.format(new Date()).toString(); // ��ȡ��ǰϵͳʱ��
		try {
			ReadExcelFile.readFile(ReadExcelFile.TABLE_NEEDITEMS , file.getFile(),userLoginId,nowTime); // ������д�����ݿ�
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
			realFile = PathKit.getWebRootPath() + "/template/" + "�蹺�豸��Ϣ¼��ģ��.xls"; // ����·����ȡ�ļ�-2003��
		}else if(type==7){
			realFile = PathKit.getWebRootPath() + "/template/" + "�蹺�豸��Ϣ¼��ģ��.xlsx"; // ����·����ȡ�ļ�-2007��
		}else{
			realFile = PathKit.getWebRootPath() + "/template/" + "�����ļ��ϴ�ʹ��˵���ĵ�.txt"; // ����·����ȡ�ļ�
		}
		renderFile(new File(realFile));
	}
	
	/**
	 * �蹺�豸���������
	 * @throws TemplateException
	 * @throws IOException
	 */
	public void downLoadWord() throws TemplateException, IOException {
		String search = getPara("search"); // ��ȡ��������
		int needStatus = getParaToInt("needStatus"); // ��ȡ״̬
		String sTime = getPara("sTime");   // ��ȡ��ʼʱ�� 
		String eTime = getPara("eTime");   // ��ȡ��ֹʱ��
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//�������ڸ�ʽ
		String nowTime = df.format(new Date()).toString(); // ��ȡ��ǰϵͳʱ��
		String time[] = nowTime.split("-");
		// �ж��Ƿ�ѡ����ʱ��,ʱ���ʽת��
		if(sTime==""){
			sTime = "1900-01-01";
		}
		if(eTime==""){
			eTime += "2099-06-16"; // ��ǰϵͳʱ��
		}
		List<needItemsModel> needItems = needItemsModel.dao.getNeedItemsInfo(search,sTime,eTime,needStatus); // ��ѯ�蹺�豸��Ϣ
		
		String fileName =  "�蹺�豸�����.doc"; 
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("needItems", needItems);  // �蹺��Ϣ
		dataMap.put("year", time[0]);  // �洢��ǰ���
		dataMap.put("mouth", time[1]); // �洢��ǰ�·�
		dataMap.put("date", time[2]);  // �洢��ǰ����
		PrintWord.taskBook("needItems.ftl", fileName, dataMap, getResponse());
	}
}
