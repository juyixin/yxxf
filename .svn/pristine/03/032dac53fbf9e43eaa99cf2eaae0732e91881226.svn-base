package com.eazytec.generalProcess.dao;

import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.generalProcess.model.GeneralProcess;
import com.eazytec.util.PageBean;

public interface GeneralProcessDao extends GenericDao<GeneralProcess, String>{
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-4-14 下午1:38:35
     */
	 public List<GeneralProcess> getGeneralProcess();
	 
	 
	/**
	 * Description:查询数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-7  上午9:36:35
	 */
     public List<GeneralProcess> searchGeneralProcess(String name,String state);
		 
	 
	 /**
	  * Description:保存数据
	  * 作者 : 蒋晨 
	  * 时间 : 2017-4-14 下午3:45:23
	  */
	  public GeneralProcess saveGeneralProcessInfo(GeneralProcess GeneralProcess);
       
       
      /**
 	   * Description:获取详情
 	   * 作者 : 蒋晨 
 	   * 时间 : 2017-2-6  下午6:28:54
 	   */
       public GeneralProcess getDetail(String id);
       
       
     /**
  	  * Description:修改数据
  	  * 作者 : 蒋晨 
  	  * 时间 : 2017-2-7 上午9:16:09
  	  */
  	  public GeneralProcess updateGeneralProcessInfo(GeneralProcess GeneralProcess);
  	  
  	  
  	 /**
   	  * Description:删除数据
   	  * 作者 : 蒋晨 
   	  * 时间 : 2017-2-7 上午10:09:09
   	  */
   	  public String deleteGeneralProcessInfo(List<String> isFalse);
   	  
   	  
   	 /**
       * Description:初始化数据(APP)
       * 作者 : 蒋晨 
       * 时间 : 2017-4-14 下午1:38:35
       */
  	 public List<GeneralProcess> getGeneralProcess1();
  	 
  	 
     int getAllRecordCount(PageBean<GeneralProcess> pageBean,String name,String userId,String type,String state);
	
	 List<GeneralProcess> getAllRecord(PageBean<GeneralProcess> pageBean,String name,String userId,String type,String state);
	

}
