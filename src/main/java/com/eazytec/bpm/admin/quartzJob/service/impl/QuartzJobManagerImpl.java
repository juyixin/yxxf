package com.eazytec.bpm.admin.quartzJob.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.admin.quartzJob.dao.QuartzJobDao;
import com.eazytec.bpm.admin.quartzJob.service.QuartzJobManager;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.QuartzJobAudit;
import com.eazytec.service.impl.GenericManagerImpl;
/**
 * 
 * @author Sangeetha.G
 * 
 */
@Service("quartzJobManager")
public class QuartzJobManagerImpl extends
		GenericManagerImpl<QuartzJobAudit, Long> implements QuartzJobManager {

	private QuartzJobDao quartzJobDao;

	public QuartzJobDao getQuartzJobDao() {
		return quartzJobDao;
	}

	@Override
	@Autowired
	public void setQuartzJobDao(QuartzJobDao quartzJobDao) {
		// this.dao = quartzJobDao;
		this.quartzJobDao = quartzJobDao;
	}

	@Override
	public QuartzJobAudit saveQuartzJob(QuartzJobAudit quartzJob) {

		return quartzJobDao.saveQuartzJob(quartzJob);
	}

	/**
	 * {@inheritDoc}
	 */
	public QuartzJobAudit getQuartzId(String id) {
		return quartzJobDao.getId(id);
	}

	
	/**
	 * {@inheritDoc checkListViewName}
	 */
	public boolean  checkQuartzJobName(String checkname)throws EazyBpmException{
		return quartzJobDao. checkQuartzJobName(checkname);
		
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	public List<QuartzJobAudit> getAllQuartzJob() {

		try {
			return quartzJobDao.getAllQuartzJob();
		} catch (Exception e) {
			log.warn(e.getMessage());
			throw new BpmException(
					"problem in getting events" + e.getMessage(), e);
		}
	}

}