package com.eazytec.link.service;

import java.util.List;

import javax.jws.WebService;

import com.eazytec.link.model.Link;

@WebService
public interface LinkService {
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-5 下午1:38:35
     */
	 public List<Link> getLink();
	 
	 
	/**
	 * Description:查询数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-7  上午9:36:35
	 */
     public List<Link> searchLink(String name);
		 
	 
	 /**
	  * Description:保存数据
	  * 作者 : 蒋晨 
	  * 时间 : 2017-2-5 下午3:45:23
	  */
	  public Link  saveLinkInfo(Link link);
	  
	  
	  /**
	   * Description:切换状态
	   * 作者 : 蒋晨 
	   * 时间 : 2017-2-6 下午1:45:23
	   */
       public void updateStatus(String id, String status);
       
       
      /**
 	   * Description:获取详情
 	   * 作者 : 蒋晨 
 	   * 时间 : 2017-2-6  下午6:28:54
 	   */
       public Link getDetail(String id);
       
       
     /**
  	  * Description:修改数据
  	  * 作者 : 蒋晨 
  	  * 时间 : 2017-2-7 上午9:16:09
  	  */
  	  public Link  updateLinkInfo(Link link);
  	  
  	  
  	 /**
   	  * Description:删除数据
   	  * 作者 : 蒋晨 
   	  * 时间 : 2017-2-7 上午10:09:09
   	  */
   	  public String deleteLinkInfo(List<String> isFalse);
   	  
   	  
   	 /**
       * Description:初始化数据(APP)
       * 作者 : 蒋晨 
       * 时间 : 2017-2-5 下午1:38:35
       */
  	 public List<Link> getLink1(String isActive);
 	 
}
