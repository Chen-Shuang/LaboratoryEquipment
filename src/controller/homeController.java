package controller;

import java.util.HashMap;
import java.util.List;

import model.ItemsModel;
import model.needItemsModel;

import com.jfinal.core.Controller;
/**
 * 首页统计图展示
 * @author 陈爽
 *
 */
public class homeController extends Controller {

	/**
	 * 获得统计图信息
	 */
	public void getStatistical() {
		
		HashMap<String, Object> map = new HashMap<String, Object>(); // 创建map
		List<ItemsModel> itemsCategory = ItemsModel.dao.getItemsCategory();  // 查询库存中报废、新添、待修的数量
		List<needItemsModel> needItemsCategory = needItemsModel.dao.getNeedItemsCategory(); // 查询需购设备中各个状态的数量
		
		map.put("itemsCategory", itemsCategory); // 将信息存储到map中
		map.put("needItemsCategory", needItemsCategory);
		
		renderJson(map); 
	}
}
