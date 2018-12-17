package com.eazytec.link.dao;

import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.link.model.Link;
import com.eazytec.model.User;

public interface LinkDao extends GenericDao<Link,String>{
	
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-5 下午1:38:35
     */
	public List<Link> getLink();
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-5 下午1:38:35
     */
	public List<Link> getLink1(String isActive);
	
	
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
    public Link saveLinkInfo(Link link);
    
    
    /**
	 * Description:切换状态
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-6 下午1:45:23
	 */
    public void updateStatus(String id, String status);
    
    
    /**
 	 * Description:修改数据
 	 * 作者 : 蒋晨 
 	 * 时间 : 2017-2-6上午9:16:09
 	 */
    public Link updateLinkInfo(Link link);
    
    
    /**
 	 * Description:删除数据
 	 * 作者 : 蒋晨 
 	 * 时间 : 2017-2-7 上午10:15:09
 	 */
    public void deleteLinkInfo(String id);
    
    
    public User getUser(String loginAccount);
}
