package config;

import model.ItemsModel;
import model.needItemsModel;
import model.newItemsModel;
import model.repairItemsModel;
import model.scrapItemsModel;
import model.userLoginModel;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * ��ӳ��
 */
public class userMapping {
	/**
	 * ��ӱ���model��ӳ��
	 * @param arp
	 */
	public static void mapping(ActiveRecordPlugin arp){
		arp.addMapping("user_login", userLoginModel.class); // ����Ա��
		arp.addMapping("items", ItemsModel.class); // �豸������Ϣ��
		arp.addMapping("repair_items", repairItemsModel.class); // �����豸��
		arp.addMapping("new_items", newItemsModel.class); // �����豸��
		arp.addMapping("need_items", needItemsModel.class);  // �蹺�豸��
		arp.addMapping("scrap_items", scrapItemsModel.class); // �����豸��
	}
}
