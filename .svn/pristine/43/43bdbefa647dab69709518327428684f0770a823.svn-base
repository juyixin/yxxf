package com.eazytec.quartz;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

/**
 * This class is used to Notify the current job executed,
 * 
 * @author mathi
 * 
 */

public class EazytecSchedulerJobListener implements JobListener{
	
	protected static final Log log = LogFactory.getLog(EazytecSchedulerJobListener.class);
	
	private static final String JOB_LISTENER_NAME = "EazytecSchedulerJobListener";
	private String name;
	
	public EazytecSchedulerJobListener() {
		this.name =JOB_LISTENER_NAME;
	}
	
	
	public String getName(){
		
		return JOB_LISTENER_NAME;
		
	}

    public void jobToBeExecuted(JobExecutionContext context){
    	   	
    	if (log.isDebugEnabled()) {
			log.debug("Job To Be Executed : " +context.getJobDetail().getName());
		}
    	    
    	
    }

    public void jobExecutionVetoed(JobExecutionContext context){
    	    	
    	if (log.isDebugEnabled()) {
			log.debug("Job Execution Vetoed : " +context.getJobDetail().getName());
		}
    }

    public void jobWasExecuted(JobExecutionContext context,
            JobExecutionException jobException){
      	if (log.isDebugEnabled()) {
			log.debug("Executed Job Name  : " +context.getJobDetail().getName());
		}
    }
  

}
