package com.eazytec.bpm.admin.systemLog.dao;

import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LogAudit;
import com.eazytec.model.SystemLog;

public interface SystemLogDao extends GenericDao<SystemLog, Long> {

	SystemLog saveSystemLog(SystemLog systemLog);

	SystemLog getId(String id);

	List<SystemLog> getSystemLogById(List<String> id);

	void removeSystemLog(String name);

	public List<SystemLog> getAllSystemLog();

	public List<Object[]> getSystemLogInGivenTime(String startDate,String endDate,String logType) throws Exception;
	
	public String deleteLogsInGivenTime(String startDate,String endDate,String logType) throws Exception;

	public String saveLogAudit(LogAudit logAudit) throws Exception;
	
	public String deleteLog(String logDate,String logType) throws Exception;
	
	
	/**
	 * Checks for a SystemLog name whether or not it is already existed.
	 * @param SystemLog name
	 *        SystemLog name to be checked
	 * @return
	 * 		boolean status about SystemLog name existed or not
	 * @throws EazyBpmException
	 */
	public boolean checkSystemLogNameExists(String SystemLogName) throws EazyBpmException;

	public boolean isDuplicateLogSetting(String logType,String id) throws Exception;

	String deleteAllLogs(List<String> logs);


}