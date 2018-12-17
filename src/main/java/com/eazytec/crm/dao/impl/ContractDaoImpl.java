package com.eazytec.crm.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.eazytec.crm.dao.ContractDao;
import com.eazytec.crm.model.Contract;

import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.util.PageBean;

@Repository("contractDao")
public class ContractDaoImpl extends GenericDaoHibernate<Contract, String> implements ContractDao {

	public ContractDaoImpl() {
		super(Contract.class);
	}

	public List<Contract> getAllContractList(String title) {
         StringBuilder sb = new StringBuilder("from Contract u where 1=1");
		
		if(StringUtils.isNotBlank(title)){
			sb.append(" and u.title like :title");
		}
		
		sb.append(" order by u.createdTime desc");
         Query qry = getSession().createQuery(sb.toString());
		
		if(StringUtils.isNotBlank(title)){
			qry.setParameter("title", "%"+title+"%");
		}
		return qry.list();
	}
	
	public Contract saveContract(Contract contract){
		return save(contract);
	}

	public List<Contract> getContractsByIds(List<String> ids) {
		List<Contract> contracts = getSession().createQuery("from Contract as contract where contract.id in (:list)").setParameterList("list", ids).list();
        if (contracts.isEmpty()) {
            return null;
        } else {
            return contracts;
        }
	}

	public void deleteContract(Contract contract) {
		getSession().delete(contract);
	}

	public int getAllContractCount(PageBean<Contract> pageBean) {
		Query query = getSession().createQuery("select count(*) from Contract contract");
		return ((Number) query.iterate().next()).intValue();
	}

	public List<Contract> getAllContract(PageBean<Contract> pageBean){
		List<Contract> contracts = getSession().createQuery("from Contract contract")
				.setFirstResult(pageBean.getStartRow()).setMaxResults(pageBean.getPageSize()).list();
		return contracts;
	}

}

