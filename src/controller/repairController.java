package controller;

import model.newItemsModel;
import model.repairItemsModel;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
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
		Page<repairItemsModel> newItems = repairItemsModel.dao.getRepairItemsInfo(curr, size, search,sTime,eTime,status); // ��ѯ�蹺�豸��Ϣ
		renderJson(newItems);
	}
	
	/**
	 * ��ȡ�����豸��Ϣ
	 */
	public void getRepairItemInfo() {
		int itemsId = getParaToInt("itemsId"); // ��ȡ�豸id
		repairItemsModel newItem = repairItemsModel.dao.getRepairItemInfo(itemsId); // ��ȡ�豸��Ϣ
		renderJson(newItem);
	}
}
