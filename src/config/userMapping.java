package config;

import model.ItemsModel;
import model.needItemsModel;
import model.newItemsModel;
import model.repairItemsModel;
import model.scrapItemsModel;
import model.userLoginModel;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * 表映射
 */
public class userMapping {
	/**
	 * 添加表与model的映射
	 * @param arp
	 */
	public static void mapping(ActiveRecordPlugin arp){
		arp.addMapping("user_login", userLoginModel.class); // 管理员表
		arp.addMapping("items", ItemsModel.class); // 设备基本信息表
		arp.addMapping("repair_items", repairItemsModel.class); // 待修设备表
		arp.addMapping("new_items", newItemsModel.class); // 新添设备表
		arp.addMapping("need_items", needItemsModel.class);  // 需购设备表
		arp.addMapping("scrap_items", scrapItemsModel.class); // 报废设备表
	}
}
