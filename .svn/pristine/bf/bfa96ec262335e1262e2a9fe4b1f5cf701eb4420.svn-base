package com.eazytec.bpm.admin.systemLog.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.admin.systemLog.dao.SystemLogDao;
import com.eazytec.bpm.admin.systemLog.service.SystemLogManager;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LogAudit;
import com.eazytec.model.ProcessLogs;
import com.eazytec.model.SystemLog;
import com.eazytec.model.SystemLogs;
import com.eazytec.service.impl.GenericManagerImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 
 * @author Sangeetha Guhan
 * 
 */

@Service("systemLogManager")
public class SystemLogManagerImpl extends GenericManagerImpl<SystemLog, Long>
		implements SystemLogManager {

	private SystemLogDao systemLogDao;

	public SystemLogDao getSystemLogDao() {
		return systemLogDao;
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeSystemLog(String id) {
		log.debug("removing group: " + id);
		systemLogDao.removeSystemLog(id);
	}

	@Override
	public SystemLog saveSystemLog(SystemLog systemLog) {

		// TODO Auto-generated method stub
		return systemLogDao.saveSystemLog(systemLog);
	}

	@Override
	@Autowired
	public void setSystemLogDao(SystemLogDao systemLogdao) {
		this.dao = systemLogdao;
		this.systemLogDao = systemLogdao;
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemLog getId(String id) {
		return systemLogDao.getId(id);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<SystemLog> getAllSystemLog() {
//		log.info("getting events");
		try {
			return systemLogDao.getAllSystemLog();
		} catch (Exception e) {
			log.warn(e.getMessage());
			throw new BpmException(
					"problem in getting events" + e.getMessage(), e);
		}
	}

	@Override
	public SystemLog saveUserDetails(String logType, String user,
			String ipAddr, String timeStamp, String departmentName,
			String result) {

		SystemLog systemLog = new SystemLog(logType, user, ipAddr, timeStamp,
				departmentName, result);
		return systemLogDao.saveSystemLog(systemLog);

	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteSelectedSystemLog(List<String> ids) throws Exception {
		try {
			log.info("List of ids===" + ids);
			List<SystemLog> systemLogs = systemLogDao.getSystemLogById(ids);
			for (SystemLog systemLog : systemLogs) {
				removeSystemLog(systemLog.getId());
			}
		} catch (Exception e) {
			throw new BpmException("error.sysconfig.delete" + e.getMessage(), e);
		}
	}

	@Override
	public List<Object[]> getSystemLogInGivenTime(String startDate,String endDate,String logType) throws Exception{
		return systemLogDao.getSystemLogInGivenTime(startDate,endDate,logType);
	}

	@Override
	public String deleteLogsInGivenTime(String startDate,String endDate,String logType) throws Exception{
		return systemLogDao.deleteLogsInGivenTime(startDate,endDate,logType);
	}
	
	@Override
	public String saveLogAudit(LogAudit logAudit) throws Exception{
		return systemLogDao.saveLogAudit(logAudit);
	}

	public void deleteLog(List<String> ids,String logType) throws Exception {
		try {
			for (String logDate : ids){
				systemLogDao.deleteLog(logDate, logType);
			}
		} catch (Exception e) {
			throw new BpmException("error.sysconfig.delete" + e.getMessage(), e);
		}
	}
	
	public boolean isDuplicateLogSetting(String logType,String id) throws Exception{
		return systemLogDao.isDuplicateLogSetting(logType,id);
	}
	
	/**
	 * Checks for a SystemLog name whether or not it is already existed.
	 * @param SystemLog name
	 *        SystemLog name to be checked
	 * @return
	 * 		boolean status about SystemLog name existed or not
	 * @throws EazyBpmException
	 */
	public boolean checkSystemLogNameExists(String systemLogName) throws EazyBpmException {
		
		return systemLogDao.checkSystemLogNameExists(systemLogName);
	}

	@Override
	public String deleteAllLogs() throws Exception{
		List<String> logs = new ArrayList<String>();
		logs.add("AdminLogs");
		logs.add("ProcessLogs");
		logs.add("SystemLogs");
		logs.add("ModuleLogs");
		logs.add("LoginLogs");
		String status = systemLogDao.deleteAllLogs(logs);
		return status;
	}
}