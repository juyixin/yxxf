package com.eazytec.quartz;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.core.QuartzScheduler;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

import com.eazytec.bpm.admin.quartzJob.service.QuartzJobManager;
import com.eazytec.bpm.admin.systemLog.service.SystemLogManager;
import com.eazytec.bpm.oa.schedule.service.ScheduleService;
import com.eazytec.model.LogAudit;
import com.eazytec.model.ProcessLogs;
import com.eazytec.model.QuartzJobAudit;
import com.eazytec.model.Schedule;
import com.eazytec.model.SystemLog;
import com.eazytec.util.DateUtil;
import com.eazytec.util.PropertyReader;
import com.eazytec.quartz.ServerConfig;


/**
 * send scheduled event reminders
 * 
 * @author Sangeetha Guhan
 * 
 */

public class TimeBasedLogManagementJob extends QuartzJobBean {

	private Log logger = LogFactory.getLog(TimeBasedLogManagementJob.class);

	private ApplicationContext applicationContext;
	private String directoryPath;


	public void setApplicationContext(ApplicationContext appContext) {
		applicationContext = appContext;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org
	 * .quartz.JobExecutionContext)
	 */
	protected final void executeInternal(final JobExecutionContext ctx)	throws JobExecutionException {
		
//		logger.info("======Scheduler Notification job started with the key============="+ ctx.getTrigger().getFireInstanceId());
		
		ScheduleService scheduleService = (ScheduleService) applicationContext
				.getBean("scheduleService");
		QuartzJobManager quartzJobManager = (QuartzJobManager) applicationContext
				.getBean("quartzJobManager");
		SystemLogManager systemLogManager = (SystemLogManager) applicationContext
				.getBean("systemLogManager");
		try {
			Calendar calendar = Calendar.getInstance();
			Date date =  new Date();
    		   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			List<SystemLog> events = systemLogManager.getAllSystemLog();
			String result="";
			if (events != null) {
				for (SystemLog event : events) {
					String logType = event.getLogType();
					if(logType != null && logType !=""){
					if(logType.equalsIgnoreCase(("Admin Logs"))){
						logType = "AdminLogs";
					}else if(logType.equalsIgnoreCase(("Process Logs"))){
						logType = "ProcessLogs";
					}else{
						logType = "SystemLogs";
					}
					}
					
					String deadLine=event.getDeadLine();
					if(deadLine==null){
						deadLine="";
					}
					
					if(event.getStatus() == true && !(deadLine.equalsIgnoreCase("Manually Process"))){
						String saveCycle = event.getSaveCycle();
						if(saveCycle.equalsIgnoreCase("DAY")){
							int cycleValue = event.getCycleValue();
							calendar.add(Calendar.DATE,-(cycleValue));
							String startDate = dateFormat.format(calendar.getTime());
							startDate = startDate+" "+"00:00:00";
							Calendar calender =Calendar.getInstance();
							String endDate = dateFormat.format(calender.getTime());
							endDate =  endDate+" "+"23:59:59";
							if(deadLine.equalsIgnoreCase("Automatically deleted")){
								result = systemLogManager.deleteLogsInGivenTime(startDate, endDate, logType);
							}
							else if(deadLine.equalsIgnoreCase("Automatically archived")){
								List<Object[]> logList = systemLogManager.getSystemLogInGivenTime(startDate,endDate,logType);							    
								  // String path = serverConfig.getServerRootUrl();
								result = writeIntoFile(logList,logType);
								if(result.equalsIgnoreCase("success")){
									result = systemLogManager.deleteLogsInGivenTime(startDate, endDate, logType);
								}
							}
							if(result != null && result != "")
								//saveLogAudit(logType,DateUtil.convertDateToString(date),deadLine,result,event.getName());
								saveLogAudit(logType,date,deadLine,result,event.getName());
							}else if(saveCycle.equalsIgnoreCase("MONTH")){
								int cycleValue = event.getCycleValue();
								calendar.add(Calendar.MONTH,-(cycleValue));
								String startDate = dateFormat.format(calendar.getTime());
								startDate = startDate+" "+"00:00:00";
								Calendar calender =Calendar.getInstance();
								String endDate = dateFormat.format(calender.getTime());
								endDate =  endDate+" "+"23:59:59";
								if(deadLine.equalsIgnoreCase("Automatically deleted")){
									result = systemLogManager.deleteLogsInGivenTime(startDate, endDate, logType);
									
								}
								else if(deadLine.equalsIgnoreCase("Automatically archived")){
									List<Object[]> logList = systemLogManager.getSystemLogInGivenTime(startDate,endDate,logType);
									  // String path = serverConfig.getServerRootUrl();
									result = writeIntoFile(logList,logType);
									if(result.equalsIgnoreCase("success")){
										result = systemLogManager.deleteLogsInGivenTime(startDate, endDate, logType);
									}
								}
								//saveLogAudit(logType,DateUtil.convertDateToString(date),deadLine,result,event.getName());
								saveLogAudit(logType,date,deadLine,result,event.getName());

							}else if(saveCycle.equalsIgnoreCase("QUARTER")){
								int cycleValue = event.getCycleValue();
								cycleValue = 3 * cycleValue;
								calendar.add(Calendar.MONTH,-(cycleValue));
								String startDate = dateFormat.format(calendar.getTime());
								startDate = startDate+" "+"00:00:00";
								Calendar calender =Calendar.getInstance();								
								String endDate = dateFormat.format(calender.getTime());
								endDate =  endDate+" "+"23:59:59";
								if(deadLine.equalsIgnoreCase("Automatically deleted")){
									result = systemLogManager.deleteLogsInGivenTime(startDate, endDate, logType);
								}
								else if(deadLine.equalsIgnoreCase("Automatically archived")){
									List<Object[]> logList = systemLogManager.getSystemLogInGivenTime(startDate,endDate,logType);
									  // String path = serverConfig.getServerRootUrl();
									result = writeIntoFile(logList,logType);
									if(result.equalsIgnoreCase("success")){
										result = systemLogManager.deleteLogsInGivenTime(startDate, endDate, logType);
									}
								}
								//saveLogAudit(logType,DateUtil.convertDateToString(date),deadLine,result,event.getName());
								saveLogAudit(logType,date,deadLine,result,event.getName());								
							
							}else if(saveCycle.equalsIgnoreCase("YEAR")){
								int cycleValue = event.getCycleValue();
								calendar.add(Calendar.YEAR,-(cycleValue));
								String startDate = dateFormat.format(calendar.getTime());
								startDate = startDate+" "+"00:00:00";
								Calendar calender =Calendar.getInstance();								
								String endDate = dateFormat.format(calender.getTime());
								endDate =  endDate+" "+"23:59:59";
								if(deadLine.equalsIgnoreCase("Automatically deleted")){
									result = systemLogManager.deleteLogsInGivenTime(startDate, endDate, logType);
								}
								else if(deadLine.equalsIgnoreCase("Automatically archived")){
									List<Object[]> logList = systemLogManager.getSystemLogInGivenTime(startDate,endDate,logType);
									  // String path = serverConfig.getServerRootUrl();
									result = writeIntoFile(logList,logType);
									if(result.equalsIgnoreCase("success")){
										result = systemLogManager.deleteLogsInGivenTime(startDate, endDate, logType);
									}
								}
								saveLogAudit(logType,date,deadLine,result,event.getName());
							}
						}
					}
			} 
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
	}
	
	public String writeIntoFile(List<Object[]> logList,String logType)throws Exception{
			Date date =  new Date();
			String fileName = DateUtil.convertDateToString(date);
			VelocityEngine velocityEngine = (VelocityEngine) applicationContext.getBean("velocityEngine");
			String filePath =(String) velocityEngine.getProperty("logManagement.file.path");
			directoryPath = filePath+"/"+fileName;
			try{
			File dirPath = new File(filePath);
			if (!dirPath.exists()) {
                dirPath.mkdirs();
            }
			FileWriter writer = new FileWriter(filePath+"/"+fileName+".txt");
			BufferedWriter out=new BufferedWriter(writer);
			if(logList != null && logList.size()>0){
				for (Object[] logObj : logList) { 
					out.write(logObj[0].toString()+"  ");
					out.write(logObj[1].toString()+"  ");
					out.write(logObj[2].toString()+"  ");
					out.write(logObj[3].toString()+"  ");
					out.write(logObj[4].toString()+" ");
					out.write(logObj[5].toString()+" ");
					out.write(logObj[6].toString()+"\n");
				}
			}
			out.close();
			return "success";
			}catch(Exception e){
				logger.error("Error in creation of file");
				return "Failure :: "+e.getMessage() ;
			}
			}
	
	
    public String saveLogAudit(String logType,Date date,String actionPerformed,String result,String name) throws Exception{
    	try{
    	LogAudit logAudit = new LogAudit();
    	logAudit.setLogType(logType);
    	logAudit.setActionPerformed(actionPerformed);
    	logAudit.setDate(date);
    	logAudit.setResult(result);
    	logAudit.setName(name);
    	logAudit.setFilePath(directoryPath);
    	VelocityEngine velocityEngine = (VelocityEngine) applicationContext.getBean("velocityEngine");
		String filePath =(String) velocityEngine.getProperty("logManagement.file.path");
    	SystemLogManager systemLogManager = (SystemLogManager) applicationContext
				.getBean("systemLogManager");
    	systemLogManager.saveLogAudit(logAudit);
    	}catch(Exception e){
			logger.warn(e.getMessage(),e);
    	}
    	return "success";
    }

}
