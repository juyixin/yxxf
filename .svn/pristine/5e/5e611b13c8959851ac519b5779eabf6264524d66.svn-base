package com.eazytec.bpm.runtime.task.dao;

import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.OperatingFunctionAudit;

/**
 * OperatingFunctionAudit Access Object.
 * @author revathi
 *
 */
public interface OperatingFunctionAuditDao extends GenericDao<OperatingFunctionAudit, String>{
	
	/**
	 * Get last performed operation of given task
	 * @param taskId
	 * @param processInstanceId 
	 * @return
	 * @throws BpmException
	 */
	List<OperatingFunctionAudit> getLastOperatinPerformedToTask(String processInstanceId) throws BpmException;

}
