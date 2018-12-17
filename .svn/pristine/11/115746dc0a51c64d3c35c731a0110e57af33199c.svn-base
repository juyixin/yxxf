package com.eazytec.bpm.runtime.task.dao.impl;



import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.runtime.task.dao.OperatingFunctionAuditDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.OperatingFunctionAudit;

/**
 *  This class interacts with Hibernate session for OperatingfunctionAuditLog performence
 * 
 * @author revathi
 *
 */
@Repository("opAuditDao")
public class OperatingFunctionAuditDaoImpl extends GenericDaoHibernate<OperatingFunctionAudit, String> implements OperatingFunctionAuditDao{

	public OperatingFunctionAuditDaoImpl() {
		super(OperatingFunctionAudit.class);
	}
	

	public List<OperatingFunctionAudit> getLastOperatinPerformedToTask(String processInstanceId) throws BpmException{
		Query query ;
		query = getSession().createQuery("from OperatingFunctionAudit as opAudit  where opAudit.processId='"+processInstanceId+"' order by opAudit.createdOn desc" );
		query.setMaxResults(1);
		query.setCacheable(true);
	    return query.list();
	}
	
	
}
