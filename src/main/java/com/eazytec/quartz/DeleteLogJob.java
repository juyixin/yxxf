package com.eazytec.quartz;


import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.eazytec.bpm.admin.quartzJob.service.QuartzJobManager;
import com.eazytec.bpm.admin.systemLog.service.SystemLogManager;
import com.eazytec.model.QuartzJobAudit;

public class DeleteLogJob extends QuartzJobBean {

//	private Log logger = LogFactory.getLog(TimeBasedNotificationJob.class);
	
	private ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext appContext) {
		applicationContext = appContext;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	protected final void executeInternal(final JobExecutionContext ctx)	throws JobExecutionException {
		QuartzJobManager quartzJobManager = (QuartzJobManager) applicationContext.getBean("quartzJobManager");
		try {
//			logger.info("======Delete All Logs started with the key============="+ctx.getTrigger().getFireInstanceId());
			SystemLogManager systemLogManager = (SystemLogManager) applicationContext.getBean("systemLogManager");
			String status = systemLogManager.deleteAllLogs();
		} catch (Exception e) {
			quartzJobManager.saveQuartzJob(new QuartzJobAudit(ctx.getTrigger().getName(),null,null,true,"Job Failure because of "+e.getMessage()));
		}
	}
	
}
