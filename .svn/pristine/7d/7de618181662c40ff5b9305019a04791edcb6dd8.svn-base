package com.eazytec.quartz;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerListener;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.eazytec.exceptions.BpmException;

/**
 * class to intialize the quartz job
 *
 * @author mathi
 * 
 */
public class QuartzSchedulerControl implements InitializingBean {

	private static final Log log = LogFactory.getLog(QuartzSchedulerControl.class);

	private Scheduler scheduler;
	
	private final Set listeners;
	private final SchedulerListener schedulerListener;
	private final TriggerListener triggerListener;
	private final JobListener jobListener;

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	public QuartzSchedulerControl(){
	listeners = new HashSet();
	triggerListener = new EazytecSchedulerTriggerListener();
	jobListener    = new EazytecSchedulerJobListener();
	schedulerListener = new EazytecSchedulerQuartzListener();
	}

	public void start() {
		try {
			if (!getScheduler().isStarted()) {
				getScheduler().start();

			} else {
				if (log.isDebugEnabled()) {
					log.debug("Scheduler already running.");
				}
				log.info("Scheduler Already Running");
			}
		} catch (SchedulerException e) {
			log.error("Error starting the scheduler", e);
			throw new BpmException("Error (de)registering Quartz listener",e);
		}
	}
	
	public void afterPropertiesSet() {
		try {
			//scheduleJob();
			start();
			getScheduler().addSchedulerListener(schedulerListener);
			getScheduler().addTriggerListener(triggerListener);
			getScheduler().addJobListener(jobListener);
		} catch (SchedulerException e) {
			log.error("Error (de)registering Quartz listener", e);
			throw new BpmException("Error (de)registering Quartz listener", e);
		}
	}
	
	/* Reschedule All triggers in database */
	 public void reScheduleAllTriggers() throws SchedulerException
	 {
	 // Initiate a Schedule Factory
	 SchedulerFactory schedulerFactory = new StdSchedulerFactory();
	 // Retrieve a scheduler from schedule factory
	 Scheduler scheduler = schedulerFactory.getScheduler();

	 String[] triggerGroups;
	 String[] triggers;

	 triggerGroups = scheduler.getTriggerGroupNames();
	 for (int i = 0; i < triggerGroups.length; i++) {
	 triggers = scheduler.getTriggerNames(triggerGroups[i]);
	 for (int j = 0; j < triggers.length; j++) {
	 Trigger trigger = scheduler.getTrigger(triggers[j], triggerGroups[i]);
     if (trigger instanceof SimpleTrigger ) {	 
	 // reschedule the job
	 scheduler.rescheduleJob(triggers[j], triggerGroups[i], trigger);
	 // unschedule the job
	 //scheduler.unscheduleJob(triggersInGroup[j], triggerGroups[i]);
	 }
	 }
	 }
	 // start the scheduler
	 scheduler.start();
	 } 

}
