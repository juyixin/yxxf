package com.eazytec.link.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.link.dao.LinkDao;
import com.eazytec.link.model.Link;
import com.eazytec.model.User;

@Repository("linkDao")
public class LinkDaoImpl extends GenericDaoHibernate<Link,String> implements LinkDao{
	
	public LinkDaoImpl(){
		super(Link.class);
	}
    
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-5 下午1:38:35
     */
	@Override
	public List<Link> getLink() {
		String sql = "select * from ETEC_Link order by create_Date desc";
		List<Link> linkList = getSession().createSQLQuery(sql).addEntity(Link.class).list();
		return linkList;
	}
	
	
	/**
     * Description:初始化数据
     * 作者 : 蒋晨 
     * 时间 : 2017-2-5 下午1:38:35
     */
	@Override
	public List<Link> getLink1(String isActive) {
		String sql = "select * from ETEC_Link where is_active='"+isActive+"' order by create_Date desc";
		List<Link> linkList = getSession().createSQLQuery(sql).addEntity(Link.class).list();
		return linkList;
	}
	
	
	/**
	 * Description:查询数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-7  上午9:36:35
	 */
	@Override
	public List<Link> searchLink(String name) {
		String sql = "select * from ETEC_Link  where title like '%"+ name +"%' order by create_Date desc";
		List<Link> linkList = getSession().createSQLQuery(sql).addEntity(Link.class).list();
		return linkList;
	}
	

	/**
	 * Description:保存数据
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-5 下午3:45:23
	 */
	@Override
	public Link saveLinkInfo(Link link) {
		getSession().save(link);
		getSession().flush();
		return link;
	}

	
	/**
	 * Description:切换状态
	 * 作者 : 蒋晨 
	 * 时间 : 2017-2-6 下午1:45:23
	 */
	@Override
	public void updateStatus(String id, String status) {
	   // String sql = "update etec_link set is_active = '"+status+"'"+"where id ='"+id+"'";
	    String hql="update Link link set link.isActive = '"+status+"'"+"where link.id ='"+id+"'";
	  //System.out.println(hql); 
	    getSession().createQuery(hql).executeUpdate();
	    getSession().flush();
	}
	
	
	/**
 	 * Description:修改数据
 	 * 作者 : 蒋晨 
 	 * 时间 : 2017-2-6上午9:16:09
 	 */
	@Override
	public Link updateLinkInfo(Link link) {
		getSession().update(link);
		getSession().flush();
		return link;
	}
	
	
	/**
 	 * Description:删除数据
 	 * 作者 : 蒋晨 
 	 * 时间 : 2017-2-7 上午 10:17:09
 	 */
	@Override
	public void deleteLinkInfo(String id) {
		String sql = "delete from etec_link where id= ?";   
	    Query query = getSession().createSQLQuery(sql);   
	    query.setParameter(0, id); 
	    query.executeUpdate();
	}
	
	/**
 	 * Description:查找数据
 	 * 作者 : 蒋晨 
 	 * 时间 : 2017-2-7 上午 10:17:09
 	 */
	@Override
	public User getUser(String loginAccount) {
		String sql = "select * from etec_user where id ='"+loginAccount+"'";
		User userInfo = (User) getSession().createSQLQuery(sql).addEntity(User.class).setMaxResults(1).uniqueResult();
		return userInfo;
	}

}
