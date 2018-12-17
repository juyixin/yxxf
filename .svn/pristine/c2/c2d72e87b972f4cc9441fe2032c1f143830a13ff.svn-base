package com.eazytec.crm.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.eazytec.crm.dao.ChanceDao;
import com.eazytec.crm.model.Chance;
import com.eazytec.dao.hibernate.GenericDaoHibernate;

@Repository("chanceDao")
public class ChanceDaoImpl extends GenericDaoHibernate<Chance, String> implements ChanceDao {

	public ChanceDaoImpl() {
		super(Chance.class);

	}

	public List<Chance> getAllChanceList(String customerName) {
		StringBuilder sb = new StringBuilder("from Chance u where 1=1");
		
		if(StringUtils.isNotBlank(customerName)){
			sb.append(" and u.customerName like :customerName");
		}
		
		sb.append(" order by u.createdTime desc");
		
		Query qry = getSession().createQuery(sb.toString());
		
		if(StringUtils.isNotBlank(customerName)){
			qry.setParameter("customerName", "%"+customerName+"%");
		}
		return qry.list();
	}
	
	public Chance saveChance(Chance chance){
		return save(chance);
	}
	
	public List<Chance> getChancesByIds(List<String> ids) {
			List<Chance> chances = getSession().createQuery("from Chance as chance where chance.id in (:list)").setParameterList("list", ids).list();
	        if (chances.isEmpty()) {
	            return null;
	        } else {
	            return chances;
	        }
		}

		public void deleteChance(Chance chance) {
			getSession().delete(chance);
		}
	}


