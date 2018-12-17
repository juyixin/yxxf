package com.eazytec.bpm.admin.systemLog.service;

import java.util.List;

import com.eazytec.bpm.admin.systemLog.dao.SystemLogDao;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LogAudit;
import com.eazytec.model.ProcessLogs;
import com.eazytec.model.SystemLog;
import com.eazytec.model.SystemLogs;
import com.eazytec.service.GenericManager;

public interface SystemLogManager extends GenericManager<SystemLog, Long> {

	SystemLog saveSystemLog(SystemLog systemLog);

	void setSystemLogDao(SystemLogDao systemLogdao);

	SystemLog getId(String systemLogId);

	public List<SystemLog> getAllSystemLog();

	SystemLog saveUserDetails(String logType, String user, String ipAddr,
			String timeStamp, String departmentName, String result);

	public void deleteSelectedSystemLog(List<String> ids) throws Exception;
	
	public List<Object[]> getSystemLogInGivenTime(String startDate,String endDate,String logType) throws Exception;
	
	public String deleteLogsInGivenTime(String startDate,String endDate,String logType) throws Exception;
	
	public String saveLogAudit(LogAudit logAudit) throws Exception;
	
	public void deleteLog(List<String> ids,String logType) throws Exception;
	
	/**
	 * Checks for a SystemLog name whether or not it is already existed.
	 * @param SystemLog name
	 *        SystemLog name to be checked
	 * @return
	 * 		boolean status about SystemLog name existed or not
	 * @throws EazyBpmException
	 */
	public boolean checkSystemLogNameExists(String systemLogName) throws EazyBpmException;	

	public boolean isDuplicateLogSetting(String logType,String id) throws Exception;

	String deleteAllLogs() throws Exception;
}
