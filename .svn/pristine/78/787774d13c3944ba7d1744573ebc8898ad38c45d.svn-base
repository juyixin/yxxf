package com.eazytec.bpm.runtime.task.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.runtime.task.dao.OperatingFunctionAuditDao;
import com.eazytec.bpm.runtime.task.service.OperatingFunctionAuditService;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.OperatingFunctionAudit;
import com.eazytec.service.impl.GenericManagerImpl;


/**
 * Implementation of OperatingFunctionAudit interface
 * @author revathi
 *
 */
@Service("opAuditService")
public class OperatingFunctionAuditServiceImpl extends GenericManagerImpl<OperatingFunctionAudit, String> implements OperatingFunctionAuditService{
	
	private OperatingFunctionAuditDao opAuditDao;

	@Autowired
	public void setOpAuditDao(OperatingFunctionAuditDao opAuditDao) {
		this.opAuditDao = opAuditDao;
	}
	public OperatingFunctionAudit getLastOperatinPerformedToTask(String processInstanceId) throws BpmException{
		List<OperatingFunctionAudit> opAuditList = opAuditDao.getLastOperatinPerformedToTask(processInstanceId);
		if(opAuditList != null && opAuditList.size() > 0){
			return opAuditList.get(0);
		}else{
			return null;
		}
	}

}
