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
public class AgencyCleanUpJob extends QuartzJobBean {
	private Log logger = LogFactory.getLog(AgencyCleanUpJob.class);
	
	private ApplicationContext applicationContext;
	
	public AgencyCleanUpJob(){
		
	}
	
	public void setApplicationContext(ApplicationContext appContext) {
		applicationContext = appContext;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub 
		logger.info("======Agency CleanUp job started with the key============="+context.getTrigger().getFireInstanceId());
		AgencyService agencyService = (AgencyService) applicationContext.getBean("agencyService");
		QuartzJobManager quartzJobManager = (QuartzJobManager) applicationContext.getBean("quartzJobManager");
		try{
			List<String> deletedLinkIds=agencyService.removeUnassignedAgency();
			if(deletedLinkIds!=null && !deletedLinkIds.isEmpty()){
				quartzJobManager.saveQuartzJob(new QuartzJobAudit(context.getTrigger().getName(),new Date(),new Date(),true,"Removing Unassigned Agencys Identity Link Ids "+deletedLinkIds+" successfully completed"));
			}
		}catch(Exception e){
			quartzJobManager.saveQuartzJob(new QuartzJobAudit(context.getTrigger().getName(),new Date(),new Date(),true,e.getMessage()));
			logger.warn("schedule error",e);
		}
		
	}

}
