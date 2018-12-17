
package com.eazytec.quartz;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.eazytec.bpm.admin.quartzJob.service.QuartzJobManager;
import com.eazytec.bpm.oa.schedule.service.ScheduleService;
import com.eazytec.model.QuartzJobAudit;
import com.eazytec.model.Schedule;
//import com.sun.star.lib.uno.environments.remote.ThreadId;


/**
 * send scheduled event reminders
 * 
 * @author mathi
 *
 */
public class SchedulerNotificationJob extends QuartzJobBean {

	
	private Log logger = LogFactory.getLog(SchedulerNotificationJob.class);
	
	private ApplicationContext applicationContext;
	
	private String parameter;
	private String parameter1;
	private String parameter2;
	
	public SchedulerNotificationJob(){
		setParameter("");
	}
	/*public SchedulerNotificationJob(final String parameter,final String parameter1,final String parameter2){
		this.setParameter(parameter);
	}
	*/
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
		logger.info("Scheduler Notification job started with the key : "+ctx.getTrigger().getFireInstanceId());
		Date startDate = new Date();
		SimpleMailMessage message = (SimpleMailMessage) applicationContext.getBean("mailMessage");
		MailSender mailSender = (MailSender) applicationContext.getBean("mailSender");
		ScheduleService scheduleService = (ScheduleService) applicationContext.getBean("scheduleService");
		QuartzJobManager quartzJobManager = (QuartzJobManager) applicationContext.getBean("quartzJobManager");
		
		try {
			Date date = new Date();
			List<Schedule> events = scheduleService.getEventsForReminder();
			if(events != null){
				for(Schedule event : events){
				}
			}
		/*message.setTo("Mathi <mathivanan.ideas2it@gmail.com>");
			message.setSubject("Reg: reminder");
			message.setText("reminder");
			mailSender.send(message);*/
		    
			quartzJobManager.saveQuartzJob(new QuartzJobAudit(ctx.getTrigger().getName(),startDate,new Date(),true,"Task successfully completed"));
			logger.info("Scheduler Notification job ended with the key : "+ctx.getTrigger().getFireInstanceId());
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("schedule error",e);
		}
	}
	
	
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter1(String parameter1) {
		this.parameter1 = parameter1;
	}
	public String getParameter1() {
		return parameter1;
	}
	public void setParameter2(String parameter2) {
		this.parameter2 = parameter2;
	}
	public String getParameter2() {
		return parameter2;
	}	
}
