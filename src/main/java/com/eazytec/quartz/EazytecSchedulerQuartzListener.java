package com.eazytec.quartz;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;

/**
 * This class is used to Notify the job executed,
 * 
 * @author mathi
 * 
 */
public class EazytecSchedulerQuartzListener implements SchedulerListener{

	protected static final Log log = LogFactory.getLog(EazytecSchedulerQuartzListener.class);
	private static final Object GROUP = null;
	private static final String SCHEDULAR_LISTENER_NAME = "EazytecSchedulerQuartzListener";
	private String name;

	public EazytecSchedulerQuartzListener() {
		this.name =SCHEDULAR_LISTENER_NAME;
	}

	public void jobScheduled(Trigger trigger) {
		log.info("Quartz job " + trigger.getFullJobName() + " scheduled by trigger " + trigger.getFullName());
		if (log.isDebugEnabled()) {
			log.debug("Quartz job " + trigger.getFullJobName() + " scheduled by trigger " + trigger.getFullName());
		}
	}

	public void jobUnscheduled(String name, String group) {
		log.info("Quartz job unscheduled " + group + "." + name);
		if (log.isDebugEnabled()) {
			log.debug("Quartz job unscheduled " + group + "." + name);
		}
	}

	public void triggerFinalized(Trigger trigger) {
		log.info("Quartz trigger finalized " + trigger.getFullName());
		if (log.isDebugEnabled()) {
			log.debug("Quartz trigger finalized " + trigger.getFullName());
		}

		if (trigger.getGroup().equals(GROUP)) {
			//reportTriggerFinalized(trigger);
		}
	}

	public void triggersPaused(String name, String group) {
		log.info("Trigger Paused"+ name);
	}

	public void triggersResumed(String name, String group) {
		log.info("Trigger Resumed"+name);
	}

	public void jobsPaused(String name, String group) {
		log.info("JobPaused"+name);
	}

	public void jobsResumed(String name, String group) {
		log.info("jobResumed"+name);
	}

	public void schedulerError(String msg, SchedulerException cause) {
		if (log.isInfoEnabled()) {
			log.info("Quartz scheduler error: " + msg, cause);
		}
	}

	public void schedulerShutdown() {
		if (log.isInfoEnabled()) {
			log.info("Quartz scheduler shutdown");
		}
	}

	public void schedulerStarted() {
		if (log.isInfoEnabled()) {
			log.info("Quartz scheduler started");
		}
	}

	public void jobAdded(JobDetail arg0) {
		// TODO Auto-generated method stub
		
	}

	public void jobDeleted(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	public void schedulerInStandbyMode() {
		// TODO Auto-generated method stub
		
	}

}


