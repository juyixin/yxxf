package com.eazytec.bpm.admin.quartzJob.dao;

import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.QuartzJobAudit;

public interface QuartzJobDao extends GenericDao<QuartzJobAudit, Long> {

	QuartzJobAudit saveQuartzJob(QuartzJobAudit quartzJob);

	QuartzJobAudit getId(String id);
	boolean checkQuartzJobName(String checkName)throws EazyBpmException;
	
	public List<QuartzJobAudit> getAllQuartzJob();

}