package model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
/**
 * ���ش����豸
 * @author ��ˬ & 
 *
 */
public class repairItemsModel extends Model<repairItemsModel> {

	public static final repairItemsModel dao= new repairItemsModel();
	
	/**
	 * ����ҳ��ȡά�޵��豸��Ϣ
	 * @param curr ��ǰҳ
	 * @param size ҳ����ʾ����
	 * @param search ��ѯ����
	 * @param sTime ��ʼʱ��
	 * @param eTime ����ʱ��
	 * @param status ״̬
	 * @return ���ظ�ҳ�豸��Ϣ
	 */
	public Page<repairItemsModel> getRepairItemsInfo(int curr, int size, String search, String sTime, String eTime, int status){
		if(status==0){ // ��ô�ά����Ϣ
			return paginate(curr, size, "select a.*,b.*,a.id items_id,b.status repair_status",
					" from items a,repair_items b where a.status=2 and b.status=0 and a.id=b.items_id and concat(a.code,a.name,a.type) like '%"+search+"%' "
							+ "and b.repairDate >= '"+sTime+"' and b.repairDate <= '"+eTime+"' order by a.createTime desc");
		}else{ // ������豸��Ϣ(����չʾ)
			return paginate(curr, size, "select a.*,b.*,a.id items_id,b.status repair_status",
					" from items a,repair_items b where a.status!=-1 and  b.status=1 and a.id=b.items_id and concat(a.code,a.name,a.type) like '%"+search+"%' "
							+ "and b.repairDate >= '"+sTime+"' and b.repairDate <= '"+eTime+"' order by a.createTime desc");
		}
		
	}
	
	/**
	 * ����ҳ��ȡά�޵��豸��Ϣ
	 * @param search ��ѯ����
	 * @param sTime ��ʼʱ��
	 * @param eTime ����ʱ��
	 * @param status ״̬
	 * @return ���ظ�ҳ�豸��Ϣ
	 */
	public List<repairItemsModel> getRepairItemsInfo(String search, String sTime, String eTime, int status){
		if(status==0){ // ��ô�ά����Ϣ
			return repairItemsModel.dao.find("select * from items a,repair_items b "
					+ "where a.status=2 and b.status=0 and a.id=b.items_id and concat(a.code,a.name,a.type) like '%"+search+"%' "
							+ "and b.repairDate >= '"+sTime+"' and b.repairDate <= '"+eTime+"' order by a.createTime desc");
		}else{ // ������豸��Ϣ(����չʾ)
			return repairItemsModel.dao.find("select * from items a,repair_items b "
					+ "where a.status!=-1 and  b.status=1 and a.id=b.items_id and concat(a.code,a.name,a.type) like '%"+search+"%' "
							+ "and b.repairDate >= '"+sTime+"' and b.repairDate <= '"+eTime+"' order by a.createTime desc");
		}
		
	}
	
	/**
	 * ��ȡ�����豸��Ϣ
	 * @param itemsId �豸id
	 * @return ���ز�ѯ���豸����
	 */
	public repairItemsModel getRepairItemInfo(int itemsId) {
		return repairItemsModel.dao.findFirst("select a.*,b.*,a.id items_id,b.id repair_id"
				+ "  from items a,repair_items b where a.id=b.items_id and a.id=?",itemsId);
	}
	
	/**
	 * �����豸����״̬Ϊ����״̬
	 * @param itemsId �豸id
	 */
	public void RepairFinish(int itemsId){
		Db.update("update repair_items set status=1 where items_id=?",itemsId);
	}
}
