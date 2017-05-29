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
 * �쵼��˽���
 * @author ��ˬ
 *
 */
public class auditController extends Controller {

	public void index() {
		render("audit.html");
	}
	
	/**
	 * �蹺�豸���ͨ��
	 */
	public void auditPassNeedItem() {
		final ItemsModel items = getModel(ItemsModel.class, "items"); // ��ȡ���л�������
		final needItemsModel needItems = getModel(needItemsModel.class, "need_items"); // ��ȡ���л�������
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		final String nowTime = df.format(new Date()).toString(); // ��ȡ��ǰϵͳʱ��
		
		boolean success = Db.tx(new IAtom() {  // ���������
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("updateTime", nowTime).update();  // �޸��蹺�豸�Ļ�����Ϣ
	            	needItems.set("status", 1).update(); // �޸��蹺�豸״̬Ϊͨ��
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
	 * �蹺�豸��˲���
	 */
	public void auditRejectNeedItem() {
		final ItemsModel items = getModel(ItemsModel.class, "items"); // ��ȡ���л�������
		final needItemsModel needItems = getModel(needItemsModel.class, "need_items"); // ��ȡ���л�������
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		final String nowTime = df.format(new Date()).toString(); // ��ȡ��ǰϵͳʱ��
		
		boolean success = Db.tx(new IAtom() {  // ���������
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("updateTime", nowTime).update();  // �޸��蹺�豸�Ļ�����Ϣ
	        		needItems.set("status", -1).update(); // �޸��蹺�豸״̬Ϊ����
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
	 * �����豸���ͨ��
	 */
	public void auditPassScrapItem() {
		final ItemsModel items = getModel(ItemsModel.class, "items"); // ��ȡ���л�������
		final scrapItemsModel scrapItems = getModel(scrapItemsModel.class, "scrap_items"); // ��ȡ���л�������
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		final String nowTime = df.format(new Date()).toString(); // ��ȡ��ǰϵͳʱ��
		
		boolean success = Db.tx(new IAtom() {  // ���������
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("updateTime", nowTime).update();  // �޸��豸�Ļ�����Ϣ
	            	scrapItems.set("status", 1).update(); // �޸ı����豸���״̬Ϊͨ��
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
	 * �����豸��˲��أ�������أ���ɾ��������¼�������豸״̬��Ϊ�����豸��
	 */
	public void auditRejectScrapItem() {
		final ItemsModel items = getModel(ItemsModel.class, "items"); // ��ȡ���л�������
		final scrapItemsModel scrapItems = getModel(scrapItemsModel.class, "scrap_items"); // ��ȡ���л�������
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		final String nowTime = df.format(new Date()).toString(); // ��ȡ��ǰϵͳʱ��
		
		boolean success = Db.tx(new IAtom() {  // ���������
	        @Override
	        public boolean run() throws SQLException {
	            try {
	            	items.set("status", 1).set("updateTime", nowTime).update();  // �޸��豸�Ļ�����Ϣ״̬Ϊ�����豸
	            	newItemsModel.dao.auditRejectScrap(items.getLong("id"));  // ���������豸״̬
	            	scrapItems.set("status", -1).update(); // �޸ı����豸���״̬Ϊ����
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
