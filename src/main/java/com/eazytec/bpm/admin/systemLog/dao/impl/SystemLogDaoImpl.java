package com.eazytec.bpm.admin.systemLog.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.admin.systemLog.dao.SystemLogDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.AdminLogs;
import com.eazytec.model.DMSRolePermission;
import com.eazytec.model.LogAudit;
import com.eazytec.model.ProcessLogs;
import com.eazytec.model.SystemLog;
import com.eazytec.model.SystemLogs;
import com.eazytec.model.User;
import com.eazytec.model.UserOpinion;

/**
 * 
 * @author Sangeetha Guhan
 * 
 */
@Repository("systemLogDao")
public class SystemLogDaoImpl extends GenericDaoHibernate<SystemLog, Long> implements SystemLogDao {

	public SystemLogDaoImpl() {
		super(SystemLog.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemLog addNewsystemLog(SystemLog systemLog)
			throws EazyBpmException {
		if (log.isDebugEnabled()) {
			// log.debug("DataDictionary id: " + dictionary.getId());
		}
		getSession().saveOrUpdate(systemLog);
		// necessary to throw a DataIntegrityViolation and catch it in
		// GroupManager
		getSession().flush();
		return systemLog;
	}

	@Override
	public SystemLog saveSystemLog(SystemLog systemLog) {

		// TODO Auto-generated method stub
		if (log.isDebugEnabled()) {
			// log.debug("DataDictionary id: " + dictionary.getId());
		}
		getSession().saveOrUpdate(systemLog);
		// necessary to throw a DataIntegrityViolation and catch it in
		// GroupManager
		getSession().flush();
		return systemLog;
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemLog getId(String id) {
		List systemlog = getSession().createCriteria(SystemLog.class)
				.add(Restrictions.eq("id", id)).list();
		if (systemlog.isEmpty()) {
			return null;
		} else {
			return (SystemLog) systemlog.get(0);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeSystemLog(String name) {
		SystemLog systemLog = getId(name);
		Session session = getSessionFactory().getCurrentSession();
		session.delete(systemLog);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<SystemLog> getSystemLogById(List<String> id) {
		List<SystemLog> systemLogs = getSession()
				.createQuery(
						"from SystemLog as systemLog where systemLog.id in (:list)")
				.setParameterList("list", id).list();

		if (systemLogs.isEmpty()) {
			return null;
		} else {
			return systemLogs;
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<SystemLog> getAllSystemLog() {

		Query qry = getSession().createQuery("from SystemLog systemLog");
		@SuppressWarnings("unchecked")
		List<SystemLog> systemlogs = (List<SystemLog>) qry.list();

		if (systemlogs.isEmpty()) {
			return null;
		} else {
			return systemlogs;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getSystemLogInGivenTime(String startDate,String endDate,String logType) throws Exception{
		
		List<Object[]> logList  = (List<Object[]>) getSession().createQuery("SELECT logs.userId,logs.logDate,logs.logger,logs.level,logs.message,logs.ip,logs.logType from "+logType+" AS logs WHERE logs.logDate BETWEEN TIMESTAMP('"+startDate+"') AND TIMESTAMP('"+endDate+"')" ).list();
			return logList;
	}

	@SuppressWarnings("unchecked")
	public String deleteLogsInGivenTime(String startDate,String endDate,String logType) throws Exception{
		Query query = getSession().createQuery("delete from "+logType+" AS logs WHERE logs.logDate BETWEEN TIMESTAMP('"+startDate+"') AND TIMESTAMP('"+endDate+"')" );
		query.executeUpdate();
		getSession().flush();
		return "success";
	}
	
	public String saveLogAudit(LogAudit logAudit) throws Exception{
		getSession().saveOrUpdate(logAudit);
		getSession().flush();
		return "success";
	}	
	
	public String deleteLog(String logDate,String logType){
		Query query = getSession().createQuery("delete from "+logType+" AS logs WHERE logs.logDate='"+logDate+"'");
		query.executeUpdate();
		getSession().flush();
		return "success";	
	}
	
	public boolean isDuplicateLogSetting(String logType,String id) throws Exception{
		List<SystemLog> systemLogs = getSession().createCriteria(SystemLog.class)
		    	.add( Restrictions.or(
		        Restrictions.eq( "logType", logType ))).list();
		if(systemLogs.isEmpty()){
			return false;
		}else{
		for(SystemLog systemLog:systemLogs){
			if (systemLog.getStatus() && !systemLog.getId().equalsIgnoreCase(id)) {
				return true;
			}
		}
		return false;
		}
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
		Query qry = getSession().createQuery("select systemLog from SystemLog systemLog where lower(systemLog.name) = '"+systemLogName+"' ");
		List<SystemLog> names = qry.list();
		return (names != null && !names.isEmpty()) ? true:false;
	}
	
	public String deleteAllLogs(List<String> logs) {
		for(String log : logs ) {
			Query query = getSession().createQuery("delete from "+log );
			query.executeUpdate();
		}
		getSession().flush();
		return "success";
	}
}