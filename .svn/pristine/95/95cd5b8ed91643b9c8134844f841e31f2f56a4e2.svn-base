
package com.eazytec.quartz;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.eazytec.bpm.admin.quartzJob.service.QuartzJobManager;
import com.eazytec.bpm.runtime.task.service.TaskService;
import com.eazytec.model.QuartzJobAudit;


/**
 * handle process time setting tasks
 * 
 * @author sangeetha
 *
 */
public class ProcessTimeSettingJob extends QuartzJobBean {

	//private Log logger = LogFactory.getLog(SchedulerNotificationJob.class);
	
	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext appContext) {
		applicationContext = appContext;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.quartz.JobExecutionContext)
	 */
	protected final void executeInternal(final JobExecutionContext ctx)
			throws JobExecutionException {
//		logger.info("======Process Time Setting job started with the key============="+ctx.getTrigger().getFireInstanceId());
		System.out.println("======Process Time Setting job started with the key"+ctx.getTrigger().getPreviousFireTime());
		Date startDate = new Date();
		QuartzJobManager quartzJobManager = (QuartzJobManager) applicationContext.getBean("quartzJobManager");
		TaskService rtTaskService = (TaskService) applicationContext.getBean("rtTaskService");
		try {
			String jobErrorMessage = rtTaskService.handleUnSubmittedTaskByTimeSetting();	
			if(jobErrorMessage.isEmpty()){
				jobErrorMessage = "Job Run Successfully";
			}
			quartzJobManager.saveQuartzJob(new QuartzJobAudit(ctx.getTrigger().getName(),startDate,new Date(),true,jobErrorMessage));
//			logger.info("======Process Time Setting job ended with the key============="+ctx.getTrigger().getFireInstanceId());
		} catch (Exception e) {
			quartzJobManager.saveQuartzJob(new QuartzJobAudit(ctx.getTrigger().getName(),startDate,new Date(),true,"Job Failure because of "+e.getMessage()));
			System.out.println("======Process Time setting Exception : "+e.getMessage());
			//e.printStackTrace();
		}
	}
}
