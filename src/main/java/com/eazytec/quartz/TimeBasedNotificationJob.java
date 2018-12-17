
package com.eazytec.quartz;

import java.util.Date;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.eazytec.bpm.admin.notification.service.NotificationService;
import com.eazytec.util.DateUtil;

/**
 * send notifications to corresponding user
 * 
 * @author Ramachandran
 * @author Mathi
 *
 */
public class TimeBasedNotificationJob extends QuartzJobBean {

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
//		logger.info("======Scheduler Notification job started with the key============="+ctx.getTrigger().getFireInstanceId());
		Date dateToSendNotification = new Date();
		String notificationDate = DateUtil.convertDateToString(dateToSendNotification);
		NotificationService notificationService = (NotificationService) applicationContext.getBean("notificationService");
		notificationService.sendNotifications(notificationDate);
	} 
	
}
