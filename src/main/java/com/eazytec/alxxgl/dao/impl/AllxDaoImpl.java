/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 * Since 2008 - 2015
 */

package com.eazytec.alxxgl.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eazytec.alxxgl.dao.AllxDao;
import com.eazytec.alxxgl.model.Allx;
import com.eazytec.dao.hibernate.Finder;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.dto.AllxDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.Department;
import com.eazytec.model.LabelValue;
import com.eazytec.util.PageBean;
/**
 * @author easybpm
 *
 */


@Repository("allxDao")
public class AllxDaoImpl extends GenericDaoHibernate<Allx,String> implements AllxDao{

	public AllxDaoImpl(){
		super(Allx.class);
	}

	@Override
	public List<Allx> getAllAllxList(String lxmc) {
		Finder f = Finder.create("from Allx u where 1=1");
		if (StringUtils.isNotBlank(lxmc)) {
			f.append(" and u.lxmc like :lxmc");
			f.setParam("lxmc", "%" + lxmc + "%");
		}
		return find(f);
	}

	@Override
	public Allx saveAllx(Allx allx) {
		 return save(allx);
	}

	@Override
	public List<Allx> getAllxsByIds(List<String>  ids) {
		List<Allx> allxs = getSession().createQuery("from Allx as allx where allx.id in (:list)").setParameterList("list", ids).list();
		if (allxs.isEmpty()) {
			return null;
		} else {
			return allxs;
		}
	}

	@Override
	public void deleteAllx(Allx allx) {
		getSession().delete(allx);
	}

	@Override
	public int getAllAllxCount(PageBean<Allx> pageBean, String num) {

		Finder f = Finder.create("select count(*) from Allx allx where 1=1");
		return findCount(f);
	}

	@Override
	public List<Allx> getAllAllx(PageBean<Allx> pageBean, String num, String orderName, String orderType) {

		Finder f = Finder.create("from Allx allx where 1=1");
		 
		if(StringUtils.isNotBlank(orderName)){
			f.append( "order by record."+ orderName + " " +orderType);
		}
		f.setFirstResult(pageBean.getStartRow());
		f.setMaxResults(pageBean.getPageSize());
		
		return find(f);
		
	}

	@Override
	public Allx updateAllx(Allx allx) {
		getSession().update(allx);
		getSession().flush();
		return allx;
	}
	
	@SuppressWarnings("unchecked")
	public List<AllxDTO> getAllAllxDTO() throws BpmException {
		List<AllxDTO> allxDTOList =null;
		try {
			allxDTOList = (List <AllxDTO>) getSession().createQuery("select new com.eazytec.dto.AllxDTO(allx.id as id,allx.name as name,allx.superDepartment as superDepartment,allx.departmentType as departmentType,allx.lxmc as lxmc,allx.lxdm as lxdm) from Allx as allx").list();
	    	System.out.println(allxDTOList+"@");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allxDTOList;
	}

	public List<Allx> getAllxBySuperDepartmentId(String superDepartment) {
		 			
	  	List<Allx> allxs = getSession().createQuery("from Allx as allx where allx.superDepartment ='"+superDepartment+"'").list();
			if (allxs.isEmpty()) {
				return null;
			} else {
				return allxs;
			}
	}
	
	@Override
	public List<Allx> getAllxBySuperDepartmentId1(String name) {
			
	  	List<Allx> allxs = getSession().createQuery("from Allx as allx where allx.lxmc like '%"+name+"%'").list();
			if (allxs.isEmpty()) {
				return null;
			} else {
				return allxs;
			}
	}
	
	@Override
	public List<Allx> getAllxBySuperDepartmentId2(String id, String name) {
	  	List<Allx> allxs = getSession().createQuery("from Allx as allx where allx.lxdm ='"+id+"'"+"and allx.lxmc like'%"+name+"%'").list();
			if (allxs.isEmpty()) {
				return null;
			} else {
				return allxs;
			}
	}

	@Override
	public List<Allx> getAllAllx() throws BpmException {
		System.out.println("************");
		String sql = "select * from ETEC_YXXF_ALLX  ";

		List<Allx> allxList = getSession().createSQLQuery(sql).addEntity(Allx.class).list();
		System.out.println(sql);
		System.out.println(allxList);
		return allxList;
	}

	@Override
	public List<Allx> getAllxTeam3(String superDepartment) {
		String sql = "select * from ETEC_YXXF_ALLX  where super_Department ='"+superDepartment+"' or lxdm = '"+superDepartment+"' and 1=1 ";
		List<Allx> allxs = getSession().createSQLQuery(sql).addEntity(Allx.class).list();
		if (allxs.isEmpty()) {
			return null;
		} else {
			return allxs;
		}
	}

	@Override
	public List<LabelValue> getOrganizationAsLabelValue() throws BpmException {
		List<LabelValue> allxList = (List<LabelValue>) getSession()
				.createQuery(
						"select new com.eazytec.model.LabelValue(allx.lxmc as name,allx.lxdm as id) from Allx as allx where allx.departmentType = 'root'")
				.list();
		return allxList;
	}

	@Override
	public List<LabelValue> getAllxsAsLabelValueByParent(String parentDepartment) throws BpmException {
		List<LabelValue> allxList = (List<LabelValue>) getSession()
				.createQuery(
						"select new com.eazytec.model.LabelValue(allx.lxmc as name,allx.lxdm as id) from Allx as allx where allx.superDepartment = '"+parentDepartment+"'")
				.list();

		return allxList;
    }

	@Override
	public List<Allx> getAllxByIds(List<String> ids) {
		List<Allx> allxs = getSession().createQuery("from Allx as allx where allx.lxdm in (:list)").setParameterList("list", ids).list();
        if (allxs.isEmpty()) {
            return null;
        } else {
            return allxs;
        }
	}

	@Override
	public void removeAllx(String lxdm) {
		log.info("removing Allx"+lxdm);
		Allx allx = getAllxByName(lxdm);
		Session session = getSessionFactory().getCurrentSession();
		session.delete(allx);
	}
	
	public Allx getAllxByName(String lxdm) {
		List<Allx> allxs = getSession().createCriteria(Allx.class)
				.add(Restrictions.eq("lxdm", lxdm)).list();
		if (allxs.isEmpty()) {
			return null;
		} else {
			return (Allx) allxs.get(0);
		}
	}

	@Override
	public List<Allx> getAllxs() {
		Query qry = getSession().createQuery(
				"from Allx u order by upper(u.lxdm)");
		return qry.list();
	}

	@Override
	public Allx getByLxdm(String lxdm) {
		List<Allx> allxs = getSession().createCriteria(Allx.class)
				.add(Restrictions.eq("lxdm", lxdm)).list();
		if (allxs.isEmpty()) {
			return null;
		} else {
			return (Allx) allxs.get(0);
		}
	}

	@Override
	public List<Allx> getAllxBydepartment(String id) {
		String sql=null;
		if(id==null || id==""){
			sql = "select * from ETEC_YXXF_ALLX  where super_Department ='Organization' and is_Active='Y'";
		}else{
			sql = "select * from ETEC_YXXF_ALLX  where super_Department ='"+id+"' and is_Active='Y'";
		}
			
		List<Allx> allxs = getSession().createSQLQuery(sql).addEntity(Allx.class).list();
		if (allxs.isEmpty()) {
			return null;
		} else {
			return allxs;
		}
	}

	@Override
	public Allx searchlxmc(String lxdm) {
		String sql=null;
		sql = "select * from ETEC_YXXF_ALLX  where lxdm ='"+lxdm+"' and is_Active='Y'";	
		List<Allx> usersList = getSession().createSQLQuery(sql).addEntity(Allx.class).list();
		if (usersList.isEmpty()) {
			return null;
		} else {
			return (Allx) usersList.get(0);
		}
	}

}
