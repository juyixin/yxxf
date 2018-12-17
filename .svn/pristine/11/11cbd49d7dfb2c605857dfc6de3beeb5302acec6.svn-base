package com.eazytec.bpm.admin.quartzJob.service;

import java.util.List;

import com.eazytec.bpm.admin.quartzJob.dao.QuartzJobDao;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.QuartzJobAudit;
import com.eazytec.service.GenericManager;

public interface QuartzJobManager extends GenericManager<QuartzJobAudit, Long> {

	QuartzJobAudit saveQuartzJob(QuartzJobAudit quartzJob);

	void setQuartzJobDao(QuartzJobDao quartzJobdao);

	QuartzJobAudit getQuartzId(String quartzJobId);

	boolean checkQuartzJobName(String checkname)throws EazyBpmException;
	
	public List<QuartzJobAudit> getAllQuartzJob();

}