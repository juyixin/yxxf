package com.eazytec.quartz;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.WebApplicationContext;

/**
 * This class is used to Notify the current trigger executed,
 * trigger misfired and trigger state quartz jobs
 * 
 * @author mathi
 * 
 */


public class EazytecSchedulerTriggerListener implements TriggerListener{

	protected static final Log log = LogFactory.getLog(EazytecSchedulerTriggerListener.class);

	private String name;
	private static final String TRIGGER_LISTENER_NAME = "EazytecSchedulerTriggerListener";
	public EazytecSchedulerTriggerListener() {
		this.name =TRIGGER_LISTENER_NAME;
	}

	public String getName() {
	 return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	// Notify the fired trigger
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		if (log.isDebugEnabled()) {
			log.debug("Quartz trigger fired " + trigger.getFullName());
		}
	}
	// Notify current executing trigger
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		if (log.isDebugEnabled()) {
			log.debug("Quartz trigger fired " + trigger.getFullName());
		}
		return false;
	}
	
	// Notify misfired trigger
	public void triggerMisfired(Trigger trigger)  {
		if (log.isDebugEnabled()) {
			log.debug("Quartz trigger misfired " + trigger.getFullName());
		}
			
		}
	// Notify completed trigger with instruction code 
	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			int triggerInstructionCode) {
		if (log.isDebugEnabled()) {
			log.debug("Quartz trigger complete " + trigger.getFullName() + " "
					+ triggerInstructionCode);
		}
	}
		
}
