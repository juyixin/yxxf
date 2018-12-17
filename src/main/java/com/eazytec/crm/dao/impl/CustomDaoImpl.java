package com.eazytec.crm.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.eazytec.crm.dao.CustomDao;
import com.eazytec.crm.model.Custom;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.util.PageBean;

@Repository("customDao")
public class CustomDaoImpl extends GenericDaoHibernate<Custom, String> implements CustomDao{
	public CustomDaoImpl() {
		super(Custom.class);
		}
	
	public List<Custom> getAllCustomList(String num,String name) {
     StringBuilder sb = new StringBuilder("from Custom u where 1=1");
		if(StringUtils.isNotBlank(num)){
			sb.append(" and u.num like :num");
		}
		if(StringUtils.isNotBlank(name)){
			sb.append(" and u.name like :name");
		}
		
		sb.append(" order by u.createdTime desc");
		
		Query qry = getSession().createQuery(sb.toString());
		
		if(StringUtils.isNotBlank(num)){
			qry.setParameter("num", "%"+num+"%");
		}
		if(StringUtils.isNotBlank(name)){
			qry.setParameter("name", "%"+name+"%");
		}
		return qry.list();
		
	}
	public Custom saveCustom(Custom custom){
		return save(custom);
	}
	public List<Custom> getCustomsByIds(List<String> ids) {
		List<Custom> customs = getSession().createQuery("from Custom as custom where custom.id in (:list)").setParameterList("list", ids).list();
        if (customs.isEmpty()) {
            return null;
        } else {
            return customs;
        }
	}
	

	public void deleteCustom(Custom custom) {
		getSession().delete(custom);
	}
	
	public int getAllCustomCount(PageBean<Custom> pageBean) {
		Query query = getSession().createQuery("select count(*) from Custom custom");
		return ((Number) query.iterate().next()).intValue();
	}

	public List<Custom> getAllCustom(PageBean<Custom> pageBean){
		List<Custom> customs = getSession().createQuery("from Custom custom")
				.setFirstResult(pageBean.getStartRow()).setMaxResults(pageBean.getPageSize()).list();
		return customs;
	}



}
	
