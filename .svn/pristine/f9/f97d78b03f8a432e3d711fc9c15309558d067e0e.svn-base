package com.eazytec.bpm.admin.quartzJob.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.admin.quartzJob.dao.QuartzJobDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.QuartzJobAudit;
import com.eazytec.model.SysConfig;

/**
 * 
 * @author Sangeetha.G
 * 
 */
@Repository("quartzJobDao")
public class QuartzJobDaoImpl extends GenericDaoHibernate<QuartzJobAudit, Long>
		implements QuartzJobDao {

	public QuartzJobDaoImpl() {
		super(QuartzJobAudit.class);
	}

	

	/**
	 * {@inheritDoc}
	 */
	public QuartzJobAudit addNewquartzJob(QuartzJobAudit quartzJob)
			throws EazyBpmException {
		if (log.isDebugEnabled()) {
			// log.debug("DataDictionary id: " + dictionary.getId());
		}
		getSession().saveOrUpdate(quartzJob);
		// necessary to throw a DataIntegrityViolation and catch it in
		// GroupManager
		getSession().flush();
		return quartzJob;
	}
	
	

	@Override
	public QuartzJobAudit saveQuartzJob(QuartzJobAudit quartzJob) {
		// TODO Auto-generated method stub
		if (log.isDebugEnabled()) {
			// log.debug("DataDictionary id: " + dictionary.getId());
		}
		getSession().saveOrUpdate(quartzJob);
		// necessary to throw a DataIntegrityViolation and catch it in
		// GroupManager
		getSession().flush();
		return quartzJob;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public QuartzJobAudit getId(String id) {
		List quartzjob = getSession().createCriteria(QuartzJobAudit.class)
				.add(Restrictions.eq("id", id)).list();
		if (quartzjob.isEmpty()) {
			return null;
		} else {
			return (QuartzJobAudit) quartzjob.get(0);
		}
	}
	
	

	/**
	 * {@inheritDoc checkListViewName}
	 */
	public boolean checkQuartzJobName(String checkName)throws EazyBpmException{
		@SuppressWarnings("unchecked")
		List<QuartzJobAudit> quartzJob = getSession().createCriteria(QuartzJobAudit.class).add(Restrictions.eq("triggerName", checkName)).list();
		if (quartzJob.isEmpty()) {
			return false;
		}else{
			return true;
		}
	}


    

	
	
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<QuartzJobAudit> getAllQuartzJob() {
	

		Query qry = getSession().createQuery("from QuartzJobAudit quartzjob");
		@SuppressWarnings("unchecked")
		List<QuartzJobAudit> quartzjobs = (List<QuartzJobAudit>) qry.list();

		if (quartzjobs.isEmpty()) {
			return null;
		} else {
			return quartzjobs;
		}
	}

}
