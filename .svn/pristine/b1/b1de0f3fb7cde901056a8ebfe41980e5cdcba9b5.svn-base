package com.eazytec.sendDocuments.dao;

import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.petitionLetter.model.PetitionLetterPerson;
import com.eazytec.sendDocuments.model.SendDocuments;
import com.eazytec.util.PageBean;

public interface SendDocumentsDao extends GenericDao<SendDocuments, String>{
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-4-14 下午1:38:35
     */
	 public List<SendDocuments> getSendDocuments();
	 
	 
	/**
	 * Description:查询数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-7  上午9:36:35
	 */
     public List<SendDocuments> searchSendDocuments(String name);
		 
	 
	 /**
	  * Description:保存数据
	  * 作者 : 蒋晨 
	  * 时间 : 2017-4-14 下午3:45:23
	  */
	  public SendDocuments  saveSendDocumentsInfo(SendDocuments SendDocuments);
       
       
      /**
 	   * Description:获取详情
 	   * 作者 : 蒋晨 
 	   * 时间 : 2017-2-6  下午6:28:54
 	   */
       public SendDocuments getDetail(String id);
       
       
     /**
  	  * Description:修改数据
  	  * 作者 : 蒋晨 
  	  * 时间 : 2017-2-7 上午9:16:09
  	  */
  	  public SendDocuments  updateSendDocumentsInfo(SendDocuments SendDocuments);
  	  
  	  
  	 /**
   	  * Description:删除数据
   	  * 作者 : 蒋晨 
   	  * 时间 : 2017-2-7 上午10:09:09
   	  */
   	  public String deleteSendDocumentsInfo(List<String> isFalse);
   	  
   	  
   	 /**
       * Description:初始化数据(APP)
       * 作者 : 蒋晨 
       * 时间 : 2017-4-14 下午1:38:35
       */
  	 public List<SendDocuments> getSendDocuments1();
  	 
  	 
     int getAllRecordCount(PageBean<SendDocuments> pageBean,String name,String userId,String type);
	
	 List<SendDocuments> getAllRecord(PageBean<SendDocuments> pageBean,String name,String userId,String type);
	
}
