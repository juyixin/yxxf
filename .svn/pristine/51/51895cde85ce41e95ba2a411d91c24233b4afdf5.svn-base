package com.eazytec.quartz;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.eazytec.bpm.admin.quartzJob.service.QuartzJobManager;
import com.eazytec.bpm.runtime.agency.service.AgencyService;
import com.eazytec.model.QuartzJobAudit;

/**
 * Remove the agency user
 * 
 * @author rajmohan
 *
 */
public class TestJob extends QuartzJobBean {
	private Log logger = LogFactory.getLog(TestJob.class);
	
	private ApplicationContext applicationContext;
	
	public TestJob(){
		
	}
	
	public void setApplicationContext(ApplicationContext appContext) {
		applicationContext = appContext;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("TestJob execute");
		
	}

}
