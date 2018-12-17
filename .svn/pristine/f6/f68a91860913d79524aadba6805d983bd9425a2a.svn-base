package com.eazytec.vacate.dao;

import java.util.List;

import com.eazytec.model.DataDictionary;
import com.eazytec.model.Department;
import com.eazytec.model.User;
import com.eazytec.util.PageBean;
import com.eazytec.vacate.model.Vacate;

public interface VacateDao {
	
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-4-14 下午1:38:35
     */
	 public List<Vacate> getVacate(String startTime,String endTime);
	 
	 
	/**
	 * Description:查询数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-7  上午9:36:35
	 */
     public List<Vacate> searchVacate(String type,String state);
		 
	 
	 /**
	  * Description:保存数据
	  * 作者 : 蒋晨 
	  * 时间 : 2017-4-14 下午3:45:23
	  */
	  public Vacate  saveVacateInfo(Vacate Vacate);
       
       
      /**
 	   * Description:获取详情
 	   * 作者 : 蒋晨 
 	   * 时间 : 2017-2-6  下午6:28:54
 	   */
       public Vacate getDetail(String id);
       
       
     /**
  	  * Description:修改数据
  	  * 作者 : 蒋晨 
  	  * 时间 : 2017-2-7 上午9:16:09
  	  */
  	  public Vacate  updateVacateInfo(Vacate Vacate);
  	  
  	  
  	 /**
   	  * Description:删除数据
   	  * 作者 : 蒋晨 
   	  * 时间 : 2017-2-7 上午10:09:09
   	  */
   	  public String deleteVacateInfo(List<String> isFalse);
   	  
   	  
   	 /**
       * Description:初始化数据(APP)
       * 作者 : 蒋晨 
       * 时间 : 2017-4-14 下午1:38:35
       */
  	 public List<Vacate> getVacate1();
  	 
  	 
     int getAllRecordCounts(PageBean<Vacate> pageBean,String name,String userId,String type);
	
	 List<Vacate> getAllRecords(PageBean<Vacate> pageBean,String name,String userId,String type);
	 
	 int getAllRecordCount(PageBean<Vacate> pageBean,String startTime,String endTime);
		
	 List<Vacate> getAllRecord(PageBean<Vacate> pageBean,String startTime,String endTime);
	 
	 public List<Department> getDepartment(String id);
	 
	 public List<User> getUser(String id);
	 
	 
	 /**
		 * Description:查询字典code对应的字典名称
		 * 作者 : 蒋晨   
		 * 时间 : 2017-11-11 下午1:40:10
		 */
	public DataDictionary getNameList(String wgtl);
	
	
	public void approverVacate(String id ,String opinion,String state,String type);
	
	public List<Vacate> getVacateList(String id,String startTime,String endTime);
	
	public int getCount(String id,String startTime,String endTime);
	
	public List<Vacate> getVacateList1(String id,String startTime,String endTime);

}
