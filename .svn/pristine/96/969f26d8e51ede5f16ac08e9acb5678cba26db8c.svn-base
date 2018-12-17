package com.eazytec.bpm.metadata.process.dao;

import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Module;
import com.eazytec.model.OperatingFunctionAudit;


/**
 * <p>
 * Performs all the CRUD functionalities related to the form entity.
 * </p>
 * @author madan
 */

public interface ProcessDefinitionDao extends GenericDao<Process, String> {
	
	List<Process> getAllLatestVersionProcessInModules(List<Module>moduleList) throws EazyBpmException;
	
	/**
	 * 
	 * @param operFunAudit
	 * @throws BpmException
	 */
	OperatingFunctionAudit saveOperatingFunctionAudit(OperatingFunctionAudit operFunAudit)throws BpmException;
}
	
