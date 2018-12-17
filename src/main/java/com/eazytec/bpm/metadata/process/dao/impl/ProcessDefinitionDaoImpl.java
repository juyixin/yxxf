package com.eazytec.bpm.metadata.process.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.eazytec.bpm.metadata.process.dao.ProcessDefinitionDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Module;
import com.eazytec.model.OperatingFunctionAudit;

/**
 *
 * @author madan
 *
 */
@Repository("processDefinitionDao")
public class ProcessDefinitionDaoImpl extends GenericDaoHibernate<Process, String> implements ProcessDefinitionDao{

    /**
     * Constructor to create a Generics-based version using Group as the entity
     */
    public ProcessDefinitionDaoImpl() {
        super(Process.class);
    } 
    
    public List<Process> getAllLatestVersionProcessInModules(List<Module>moduleList) throws EazyBpmException {
    	try{		
			Set<Process>processes = new HashSet<Process>();
			processes.addAll(getSession().createQuery("SELECT form FROM Process AS process join process.modules as module WHERE process.isActiveVersion = 1 and module in (:list)").setParameterList("list", moduleList).list());
			return new ArrayList<Process>(processes);
    	}catch(Exception e){
    		throw new EazyBpmException("Problem retrieving processes from Database", e);
    	}
    	
    }

	@Override
	public OperatingFunctionAudit saveOperatingFunctionAudit(OperatingFunctionAudit operFunAudit)throws BpmException {
		if(operFunAudit.getId()==null || operFunAudit.getId()=="" ){
			getSession().save(operFunAudit);
		}else{
			getSession().update(operFunAudit);
		}
		
		// necessary to throw a DataIntegrityViolation and catch it in
		// GroupManager
		//getSession().flush();

		return operFunAudit;
		
	}
}