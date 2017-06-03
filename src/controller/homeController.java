package controller;

import java.util.HashMap;
import java.util.List;

import model.ItemsModel;
import model.needItemsModel;

import com.jfinal.core.Controller;
/**
 * ��ҳͳ��ͼչʾ
 * @author ��ˬ
 *
 */
public class homeController extends Controller {

	/**
	 * ���ͳ��ͼ��Ϣ
	 */
	public void getStatistical() {
		
		HashMap<String, Object> map = new HashMap<String, Object>(); // ����map
		List<ItemsModel> itemsCategory = ItemsModel.dao.getItemsCategory();  // ��ѯ����б��ϡ��������޵�����
		List<needItemsModel> needItemsCategory = needItemsModel.dao.getNeedItemsCategory(); // ��ѯ�蹺�豸�и���״̬������
		
		map.put("itemsCategory", itemsCategory); // ����Ϣ�洢��map��
		map.put("needItemsCategory", needItemsCategory);
		
		renderJson(map); 
	}
}
