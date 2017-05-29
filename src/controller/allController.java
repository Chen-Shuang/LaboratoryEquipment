package controller;


import model.ItemsModel;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
/**
 * �豸��Ϣ�б�
 * @author ��ˬ
 *
 */
public class allController extends Controller {

	public void index() {
		render("all.html");
	}
	
	/**
	 * ��ѯ�����豸
	 */
	public void allItemsInfo() {
		int curr = getParaToInt("curr"); // ��ȡҳ��
		int size = getParaToInt("size"); // ��ȡҳ����ʾ����
		String search = getPara("search"); // ��ȡ��������
		String sTime = getPara("sTime"); // ��ȡ��ʼʱ��  ���������ǲ�ѯ��
		String eTime = getPara("eTime"); // ��ȡ��ֹʱ��
		int status = getParaToInt("status"); // ��ȡ�豸״̬
		
		// �ж��Ƿ�ѡ����ʱ��,ʱ���ʽת��
		if(sTime==""){
			sTime = "1900-01-01";
		}
		if(eTime==""){
			eTime += "2099-06-16"; // ��ǰϵͳʱ��
		}
		Page<ItemsModel> scrapItems = ItemsModel.dao.allItemsInfo(curr, size, search,sTime,eTime,status); // ��ѯά���豸��Ϣ
		renderJson(scrapItems);
	}
}
