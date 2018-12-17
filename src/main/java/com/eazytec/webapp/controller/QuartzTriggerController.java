package com.eazytec.webapp.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.validation.bean.conf.loader.annotation.Validatable;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Validators;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.quartzJob.service.QuartzJobManager;
import com.eazytec.bpm.common.LabelValueBean;
import com.eazytec.bpm.model.TimingTask;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.model.QuartzJobAudit;
import com.eazytec.service.TriggerExistsException;
import com.eazytec.service.UserExistsException;
import com.eazytec.util.DateUtil;

/**
 * This class is used to schedule,reschedule and
 * unschedule quartz jobs
 * 
 * @author srisudha
 * 
 */
@Controller
public class QuartzTriggerController extends BaseFormController {
	private static final long serialVersionUID = 1L;

	public VelocityEngine velocityEngine;
	
	public QuartzJobBean quartzJobBean; 

	private String triggerName;

	private final String triggerGroup = "DEFAULT";

	private final String jobGroup = "DEFAULT";

	private String triggerType = "SIMPLE";

	private String isUpdate = "false";

	private Scheduler quartzScheduler;

	private TriggerListener triggerListener;

	long dayMilli = Constants.SIXTY * Constants.THOUSAND * Constants.SIXTY
			* Constants.TWENTY_FOUR;

	long hourMilli = Constants.SECONDSVALUE * Constants.SECONDSVALUE
			* Constants.THOUSANDVALUE;

	long minuteMilli = Constants.SECONDSVALUE * Constants.THOUSANDVALUE;

	long secondsMilli = Constants.THOUSANDVALUE;

	public boolean concurrency = false;

	private QuartzJobManager quartzJobManager;

	@Autowired
	public void setquartzJobManager(QuartzJobManager quartzJobManager) {
		this.quartzJobManager = quartzJobManager;
	}

	public QuartzJobManager getquartzJobManager() {
		return quartzJobManager;
	}

	public boolean getConcurrency() {
		return concurrency;
	}

	public void setConcurrency(boolean concurrency) {
		this.concurrency = concurrency;
	}

	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	/**
	 *  To create and update timing tasks which has Task Name, description,Job Name, Job Run On
	 * 
	 * @param model
	 * @param timingTask
	 * @param errors
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "bpm/admin/timingTask", method = RequestMethod.GET)
	public ModelAndView showQuartzTrigger(ModelMap model,
			TimingTask timingTask, BindingResult errors,
			HttpServletRequest request) {
		String runOn = null;
		String jobNameHidden = null;
		try {
			Locale locale = request.getLocale();
//			log.info("Saving Timing task Name : "+timingTask.getName()+" "+getText("save.success",locale));
			String triggerName = request.getParameter("triggerName");
			if (triggerName != null) {
				timingTask = new TimingTask();
				setIsUpdate("true");
				SimpleTrigger trigger = (SimpleTrigger) quartzScheduler.getTrigger(triggerName, "DEFAULT");
				setTriggerName(trigger.getName());
				timingTask.setName(trigger.getName());
				timingTask.setDescription(trigger.getDescription());
				JobDetail jobDetail=(JobDetail)quartzScheduler.getJobDetail(trigger.getJobName(),getJobGroup());
			    jobNameHidden=jobDetail.getName();
				timingTask.setJobClassName(jobDetail.getJobClass().getName().trim());
				JobDataMap jobDataMap = jobDetail.getJobDataMap();
				String key,value,param = "",keyval,parameter = "";
				Iterator iterator = jobDataMap.keySet().iterator();
				int size=jobDataMap.keySet().size();
				for(int var=1;var<=size;var++){
					if(var>1){
						param=param+",";
					}
						keyval=(String)iterator.next();
						key=keyval;
						value=(String)jobDataMap.get(keyval);
						value=value.replaceAll("^\"|\"$", "");
						param=param+key+"="+value;
						
				}
				timingTask.setParameter(param);
				String days = getOffset(trigger.getRepeatInterval());
				String retval[] = days.split("-");
				timingTask.setjRunAt(retval[0]);
				timingTask.setEvery(retval[1]);
				timingTask.setStartDate(trigger.getNextFireTime());
				String dateStr = trigger.getNextFireTime().toString();
				DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.UK);
				Date date = (Date) formatter.parse(dateStr);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				String formatedDate=DateUtil.convertDateToStringFormat(date,"yyyy-MM-dd HH:mm");
				
				Date beginupd = new Date();
				long millisecond = beginupd.getTime();
				long triggerDatems = date.getTime();
				 runOn=timingTask.getJobRunOn();
				if(trigger.getStartTime().getTime() > millisecond   ){
					timingTask.setJobRunOn("On");
					timingTask.setJobRunAt(formatedDate);
					model.addAttribute("runOn","On");
				}
				else{
					timingTask.setJobRunOn("Immediately");
					model.addAttribute("runOn","Immediately");
				}
				model.addAttribute("jobNameHidden",jobNameHidden);
				model.addAttribute("timingTask", timingTask);
				model.addAttribute("mode", Constants.EDIT_MODE);
			} else {
				setIsUpdate("false");
				model.addAttribute("jobNameHidden",jobNameHidden);
				model.addAttribute("mode", Constants.CREATE_MODE);
				model.addAttribute("timingTask", new TimingTask());
				
			}
//			log.info("TimingTask is Created.");
		} catch (Exception e) {
			model.addAttribute("jobNameHidden",jobNameHidden);
			if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
				model.addAttribute("mode", Constants.EDIT_MODE);
				model.addAttribute("runOn",timingTask.getJobRunOn());
				
			}else{
				model.addAttribute("mode", Constants.CREATE_MODE);
				
			}
			saveError(request,	e.getMessage());
			log.error(e.getMessage(),e);
		}
		return new ModelAndView("admin/timingTask", model);
	}

	/**
	 *  To save timing tasks
	 * @param timingTask
	 * @param model
	 * @param errors
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "bpm/admin/saveTimingTask", method = RequestMethod.POST)
	public ModelAndView createQuartzTrigger(
			@ModelAttribute("timingTask") TimingTask timingTask,
			ModelMap model, BindingResult errors, HttpServletRequest request){
		 Locale locale = request.getLocale();
		 String jobNameHidden = null;
		try {
			
			
			/*System.out.println("/////////////////////////////////////////// line 233");*/
			
//			log.info("To check the TimingTask"+ timingTask);
			
			if (validator != null) {
				validator.validate(timingTask, errors);
				if (errors.hasErrors()) {
					setIsUpdate(getIsUpdate());
					if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
					model.addAttribute("mode", Constants.EDIT_MODE);
					model.addAttribute("runOn",timingTask.getJobRunOn());
					}else{
						model.addAttribute("mode", Constants.CREATE_MODE);
						model.addAttribute("runOn",timingTask.getJobRunOn());
						}
					model.addAttribute("timingTask", timingTask);
					return new ModelAndView("admin/timingTask", model);

				}
			}
//			Class superClass=Class.forName(timingTask.getJobClassName()).getSuperclass();
			SimpleTrigger trigger = new SimpleTrigger();
			JobDetail jobDetail = null;
			if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
				jobDetail=(JobDetail)quartzScheduler.getJobDetail(timingTask.getJobName(),getJobGroup());
				jobDetail.setName(timingTask.getJobName());
				jobNameHidden=jobDetail.getName();
				model.addAttribute("jobNameHidden",jobNameHidden);
				model.addAttribute("mode", Constants.EDIT_MODE);
				model.addAttribute("runOn",timingTask. getJobRunOn());
				}
			else{
				jobDetail = new JobDetail();
				jobDetail.setName(Class.forName(timingTask.getJobClassName().trim()).getSimpleName().toString()+"_"+UUID.randomUUID());
				jobNameHidden=jobDetail.getName();
				model.addAttribute("jobNameHidden",jobNameHidden);
				model.addAttribute("runOn",timingTask.getJobRunOn());
				model.addAttribute("mode", Constants.CREATE_MODE);
			}

		
			jobDetail.setDescription("Description");
			jobDetail.setGroup(getJobGroup());
			jobDetail.setJobClass(Class.forName(timingTask.getJobClassName().trim()));
			String parameter=timingTask.getParameter();
			String key,value,params[] = null;
			if(!parameter.isEmpty()){
			if (parameter.contains(",")) {
				String comma[] = parameter.split(",");

				for (int commaStr = 0; commaStr < comma.length; commaStr++) {
					if (comma[commaStr].contains("=")) {
						String[] ids = comma[commaStr].split("=");
						if (ids.length != 1 && ids.length != 0) {
							key = ids[0];
							value = ids[1];
							if (key.isEmpty()) {
								saveError(request,getText("task.key.null",timingTask.getParameter(),locale));
								setIsUpdate(getIsUpdate());
								model.addAttribute("runOn",timingTask.getJobRunOn());
								model.addAttribute("timingTask", timingTask);
								model.addAttribute("jobNameHidden",jobNameHidden);
								if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
									model.addAttribute("mode", Constants.EDIT_MODE);
								}else{
									model.addAttribute("mode", Constants.CREATE_MODE);
								}
							
								return new ModelAndView("admin/timingTask",model);
							} else {
								jobDetail.getJobDataMap().put(key, value);
							}
						} else {
							saveError(request,getText("task.parameter.keyValue",timingTask.getParameter(), locale));
							setIsUpdate(getIsUpdate());
							model.addAttribute("runOn",timingTask.getJobRunOn());
							model.addAttribute("jobNameHidden",jobNameHidden);
							model.addAttribute("timingTask", timingTask);
							if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
								model.addAttribute("mode", Constants.EDIT_MODE);
							}else{
								model.addAttribute("mode", Constants.CREATE_MODE);
							}
						
							return new ModelAndView("admin/timingTask",model);
						}
					} else {// param doesn't contain =
						saveError(request,getText("task.parameter.keyValue",timingTask.getParameter(), locale));
						setIsUpdate(getIsUpdate());
						model.addAttribute("runOn",timingTask.getJobRunOn());
						model.addAttribute("jobNameHidden",jobNameHidden);
						model.addAttribute("timingTask", timingTask);
						if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
							model.addAttribute("mode", Constants.EDIT_MODE);
						}else{
							model.addAttribute("mode", Constants.CREATE_MODE);
						}
					
						return new ModelAndView("admin/timingTask",model);
					}
				}
			}
			//parameter doesnt contain ,
			else{
				if(parameter.contains("=")) {
					
					int counter = 0;
					for( int i=0; i<parameter.length(); i++ ) {
					    if( parameter.charAt(i) == '=' ) {
					        counter++;
					    } 
					}
					if(counter==1){
					String[] ids = parameter.split("=");
					if (ids.length != 0 && ids.length != 1 ) {
						key = ids[0];
						value = ids[1];
						if (key.isEmpty()) {
							saveError(request,getText("task.key.null",timingTask.getParameter(), locale));
							setIsUpdate(getIsUpdate());
							model.addAttribute("runOn",timingTask.getJobRunOn());
							model.addAttribute("jobNameHidden",jobNameHidden);
							model.addAttribute("timingTask", timingTask);
							if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
								model.addAttribute("mode", Constants.EDIT_MODE);
							}else{
								model.addAttribute("mode", Constants.CREATE_MODE);
							}
						
							return new ModelAndView("admin/timingTask",model);
						} else {
							jobDetail.getJobDataMap().put(key, value);
						}
					} else {
						saveError(request,getText("task.parameter.keyValue",timingTask.getParameter(), locale));
						setIsUpdate(getIsUpdate());
						model.addAttribute("runOn",timingTask.getJobRunOn());
						model.addAttribute("jobNameHidden",jobNameHidden);
						model.addAttribute("timingTask", timingTask);
						if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
							model.addAttribute("mode", Constants.EDIT_MODE);
						}else{
							model.addAttribute("mode", Constants.CREATE_MODE);
						}
					
						return new ModelAndView("admin/timingTask",model);
					}
				}
					else{
						saveError(request, getText("task.parameter.keyValue", timingTask.getParameter(), locale));
						setIsUpdate(getIsUpdate());
						model.addAttribute("runOn",timingTask.getJobRunOn());
						model.addAttribute("jobNameHidden",jobNameHidden);
						model.addAttribute("timingTask", timingTask);
						if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
							model.addAttribute("mode", Constants.EDIT_MODE);
						}else{
							model.addAttribute("mode", Constants.CREATE_MODE);
						}
					
						return new ModelAndView("admin/timingTask",model);
					}
				}
				else{//param doesnt contain =
					saveError(request, getText("task.parameter.keyValue", timingTask.getParameter(), locale));
					setIsUpdate(getIsUpdate());
					model.addAttribute("runOn",timingTask.getJobRunOn());
					model.addAttribute("timingTask", timingTask);
					model.addAttribute("jobNameHidden",jobNameHidden);
					if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
						model.addAttribute("mode", Constants.EDIT_MODE);
					}else{
						model.addAttribute("mode", Constants.CREATE_MODE);
					}
				
					return new ModelAndView("admin/timingTask",model);
				}
			}
			}
			jobDetail.setDurability(true);
			jobDetail.setVolatility(false);
			jobDetail.setRequestsRecovery(false);
			quartzScheduler.addJob(jobDetail,true);
			
			SimpleTrigger simpleTrigger = new SimpleTrigger();
			simpleTrigger.setName(timingTask.getName());
			simpleTrigger.setGroup(getTriggerGroup());
			simpleTrigger.setJobName(jobDetail.getName());
			simpleTrigger.setDescription(timingTask.getDescription());
			simpleTrigger.setJobGroup("DEFAULT");
			if (timingTask.getJobRunOn().equals("Immediately")) {
				model.addAttribute("runOn","Immediately");
				simpleTrigger.setStartTime(new Date());
			} else {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date startDate = null;
				startDate = df.parse(timingTask.getJobRunAt());
				simpleTrigger.setStartTime(startDate);
				model.addAttribute("runOn","On");
			}
			simpleTrigger.setRepeatCount(-1);
			simpleTrigger.setRepeatInterval(getTimeInterval(timingTask.getEvery(), timingTask.getjRunAt()));
			simpleTrigger.addTriggerListener("EazytecSchedulerTriggerListener");

			if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
				quartzScheduler.rescheduleJob(getTriggerName(), getJobGroup(),simpleTrigger);

			} else {
				quartzScheduler.scheduleJob(simpleTrigger);
				
			}
			model.addAttribute(Constants.TIMINGTASK_LIST,generateNameValueBeanForQuartzJob(getAllJobs()));
			
		}
		catch (ObjectAlreadyExistsException e) {
			if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
				model.addAttribute("mode", Constants.EDIT_MODE);
			}else{
				model.addAttribute("mode", Constants.CREATE_MODE);
			}
			log.error(e.getMessage(),e);
			//setIsUpdate(getIsUpdate());
			model.addAttribute("runOn",timingTask.getJobRunOn());
			model.addAttribute("jobNameHidden",jobNameHidden);
			model.addAttribute("timingTask", timingTask);
            errors.rejectValue("name", "errors.existing.trigger", "duplicate timing task");
            saveError(request,	"Exception occurred");
            return new ModelAndView("admin/timingTask",model);
            }
		
		catch(IllegalArgumentException e){
			if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
				model.addAttribute("mode", Constants.EDIT_MODE);
			}else{
				model.addAttribute("mode", Constants.CREATE_MODE);
			}
			log.error(e.getMessage(),e);
			setIsUpdate(getIsUpdate());
			model.addAttribute("timingTask", timingTask);
			model.addAttribute("jobNameHidden",jobNameHidden);
			model.addAttribute("runOn",timingTask.getJobRunOn());
			saveError(request,	e.getMessage());
			errors.rejectValue("jobClassName", "errors.class.implement", "must implement Job interface");
			return new ModelAndView("admin/timingTask",model);
		}
		catch(ArrayIndexOutOfBoundsException e){
			if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
				model.addAttribute("mode", Constants.EDIT_MODE);
			}else{
				model.addAttribute("mode", Constants.CREATE_MODE);
			}
			log.error(e.getMessage(),e);
			saveError(request,	"Array Index Out of bounds exception occurred");
			errors.rejectValue("parameter", "errors.class.implement", "parameter");
			model.addAttribute("jobNameHidden",jobNameHidden);
			model.addAttribute(Constants.TIMINGTASK_LIST,generateNameValueBeanForQuartzJob(getAllJobs()));
			return new ModelAndView("admin/timingTask",model);
		}
		catch(ClassNotFoundException e){
			setIsUpdate(getIsUpdate());
			if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
				model.addAttribute("mode", Constants.EDIT_MODE);
			}else{
				model.addAttribute("mode", Constants.CREATE_MODE);
			}
			model.addAttribute("jobNameHidden",jobNameHidden);
			model.addAttribute("runOn",timingTask.getJobRunOn());
			log.error("Class you have entered is not found", e);
			model.addAttribute("timingTask", timingTask);
			errors.rejectValue("jobClassName", "errors.class.invalid", "invalid Class");
			saveError(request,	"Exception occurred");
			model.addAttribute(Constants.TIMINGTASK_LIST,generateNameValueBeanForQuartzJob(getAllJobs()));
			return new ModelAndView("admin/timingTask",model);
		}
		
		catch (Exception e) {
			log.error(e.getMessage(),e);
			model.addAttribute("timingTask", timingTask);
			model.addAttribute("jobNameHidden",jobNameHidden);
			if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
				model.addAttribute("mode", Constants.EDIT_MODE);
			}else{
				model.addAttribute("mode", Constants.CREATE_MODE);
			}
		
			setIsUpdate(getIsUpdate());
			model.addAttribute("runOn",timingTask.getJobRunOn());
			//errors.rejectValue("jobClassName", e.getMessage(), e.getMessage());
			model.addAttribute(Constants.TIMINGTASK_LIST,generateNameValueBeanForQuartzJob(getAllJobs()));
			saveError(request,	e.getMessage());
			return new ModelAndView("admin/timingTask",model);
		}
		
		if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
			saveMessage(request, getText("trigger.updated", timingTask.getName(), locale));

		} else {
			saveMessage(request, getText("trigger.saved", timingTask.getName(), locale));
			
		}
		return new ModelAndView("redirect:timingTask");
		
	}

	private long getTimeInterval(String startType, String timeValue) {

		Long durationMilli = 0L;
		if (startType.equalsIgnoreCase("Day")) {

			if ((timeValue != null) && (timeValue.trim().length() > 0)) {
				durationMilli += (Long.parseLong(timeValue) * dayMilli);
			}
		} else if (startType.equalsIgnoreCase("Hour")) {

			if ((timeValue != null) && (timeValue.trim().length() > 0)) {
				durationMilli += (Long.parseLong(timeValue) * hourMilli);
			}
		} else if (startType.equalsIgnoreCase("Minute")) {

			if ((timeValue != null) && (timeValue.trim().length() > 0)) {
				durationMilli += (Long.parseLong(timeValue) * minuteMilli);
			}
		} else if (startType.equalsIgnoreCase("Second")) {
			if ((timeValue != null) && (timeValue.trim().length() > 0)) {
				durationMilli += (Long.parseLong(timeValue) * secondsMilli);
			}
		}

		return durationMilli;

	}
/**
 *  To delete timing tasks with its name
 * @param triggerNames
 * @param request
 * @param model
 * @return
 */
	@RequestMapping(value = "bpm/admin/timingtaskdelete", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> deleteSelectedTriggers(
			@RequestParam("triggerNames") String triggerNames,
			@RequestParam("jobNames") String jobNames,
			HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		 Map<String,Object> message = new HashMap<String, Object>();
		try {
			if (triggerNames.contains(",")) {
				String[] ids = triggerNames.split(",");
				for (String triggerName : ids) {
					quartzScheduler.unscheduleJob(triggerName, "DEFAULT");
				}
			} else {
				quartzScheduler.unscheduleJob(triggerNames, "DEFAULT");
			}
			
			if (jobNames.contains(",")) {
				String[] ids = jobNames.split(",");
				for (String jobName : ids) {
					quartzScheduler.deleteJob(jobName, "DEFAULT");
				}
			} else {
				quartzScheduler.deleteJob(jobNames, "DEFAULT");
			}
			 message.put("successMessage", getText("trigger.deleted",locale));
		     log.info("TimingTask deleted Successfully");

			 
			//saveMessage(request, getText("trigger.deleted",triggerNames , locale));
		} catch (SchedulerException e1) {
			// TODO Auto-generated catch block
			  message.put("errorMessage", getText("error.trigger.delete",locale));
			//saveError(request,	getText("error.trigger.delete", e1.getMessage(), locale));
			e1.printStackTrace();
		}
		 return message;
	//	return new ModelAndView("redirect:timingTask");
	}

	@SuppressWarnings("rawtypes")
	public List getAllJobs() {
		String[] jobNames = null;
		try {
			jobNames = quartzScheduler.getJobNames("DEFAULT");
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block

		}
		return Arrays.asList(jobNames);
	}

/**
 * To get all timing tasks
 * @return
 * @throws SchedulerException
 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getAllQuartzTriggers() throws SchedulerException {
		String[] triggers;
		List quartzList = new ArrayList();
		triggers = quartzScheduler.getTriggerNames("DEFAULT");
		for (int j = 0; j < triggers.length; j++) {
			TimingTask timingTask=new TimingTask();
			
			SimpleTrigger trigger = (SimpleTrigger) quartzScheduler.getTrigger(
					triggers[j], "DEFAULT");

			timingTask.setName(trigger.getName());
			timingTask.setTriggerName(trigger.getName());
			timingTask.setJobName(trigger.getJobName());
			timingTask.setDescription(trigger.getDescription());
			timingTask.setPreviousFireTime(DateUtil.convertDateToString(trigger.getPreviousFireTime()));
			timingTask.setNextFireTime(DateUtil.convertDateToString(trigger.getNextFireTime()));
			String days = getOffset(trigger.getRepeatInterval());
			String retval[] = days.split("-");
			timingTask.setIntervel(retval[0]+" "+retval[1]+"(s)");
			quartzList.add(timingTask);
		}
		return quartzList;
	}

	public String getTriggerState(String triggerName, Scheduler scheduler) {
		try {
			int stateValue = scheduler.getTriggerState(triggerName,
					getTriggerGroup());
			if (stateValue == SimpleTrigger.STATE_NORMAL) {
				return "RUNNING";
			} else if (stateValue == SimpleTrigger.STATE_PAUSED) {
				return "PAUSED";
			} else if (stateValue == SimpleTrigger.STATE_COMPLETE) {
				return "COMPLETE";
			} else if (stateValue == SimpleTrigger.STATE_ERROR) {
				return "ERROR";
			} else if (stateValue == SimpleTrigger.STATE_BLOCKED) {
				return "BLOCKED";
			} else {
				return "NONE";
			}
		} catch (Exception e) {

		}
		return " ";
	}

	public String getOffset(long time) {
		String dayStr = "";
		if (time >= 86400000) {
			dayStr = String.valueOf(time / dayMilli) + "-Day";
		} else if (time <= 82800000 && time > 3540000) {
			dayStr = String.valueOf(time / hourMilli) + "-Hour";
		} else if (time <= 3540000 && time >= 60000) {
			dayStr = String.valueOf(time / minuteMilli) + "-Minute";
		} else {
			dayStr = String.valueOf(time / secondsMilli) + "-Second";
		}
		return dayStr;
	}
	/**
	 *  To retrieve the details of timing task and display it in a grid
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/showTimingTaskGrid", method = RequestMethod.GET)
	public ModelAndView showTimingTaskGrid(HttpServletRequest request,
			HttpServletResponse response) {

		Model model = new ExtendedModelMap();
		Locale locale = request.getLocale();
		try {
			String script = GridUtil.generateScriptForTimingTaskGrid(CommonUtil.getMapListFromObjectList(getAllQuartzTriggers()),velocityEngine);
			model.addAttribute("script", script);
//			log.info("Timing Task Detail retrived Successfully.");
		} catch (Exception e) {

			saveError(request, getText("error.timingTask.update", locale));
		}
		return new ModelAndView("admin/timingtasksuccess", model.asMap());
	}

	// SAVE FOR QUARTZJOB

	@RequestMapping(value = "bpm/admin/quartzJob", method = RequestMethod.GET)
	public ModelAndView showEventForm(QuartzJobAudit quartzJob,
			BindingResult errors, Model model, HttpServletRequest request) {
		model.addAttribute("quartzJob", quartzJob);
		return new ModelAndView("admin/quartzJob", model.asMap());
	}

	
	@RequestMapping(value = "bpm/admin/saveQuartzJob", method = RequestMethod.POST)
	public ModelAndView saveQuartzTrigger(
			@ModelAttribute("quartzJob") QuartzJobAudit quartzJob,
			ModelMap model, BindingResult errors, HttpServletRequest request,
			HttpServletResponse response) {
		String id = quartzJob.getId();

		try {

			@SuppressWarnings("unused")
			Locale locale = request.getLocale();
					if (quartzJob.getId().equals("")) {
				if (quartzJobManager.checkQuartzJobName(quartzJob
						.getTaskName())) {
					model.addAttribute("quartzJob", quartzJob);

					return new ModelAndView("admin/quartzJob", model);
				}
			}

			if (null == quartzJob.getId() || quartzJob.getId().isEmpty()) {
				quartzJob.setId(null);
			}

			
			if (validator != null) {
				validator.validate(quartzJob, errors);
			
				
				if (errors.hasErrors()) {
					return new ModelAndView("admin/quartzJob", model);
				}
			}

			getquartzJobManager().saveQuartzJob(quartzJob);
//			log.info("QuartzJob saved successfully");

		} catch (Exception e) {
			e.printStackTrace();
			errors.rejectValue("error.quartzjob.save", e.getMessage());
			
			return new ModelAndView("admin/quartzJob", model);
		}
		return showquartzJobGrid(request, response);
	}

	@RequestMapping(value = "bpm/admin/editQuartzJob", method = RequestMethod.GET)
	public ModelAndView editQuartzJob(@RequestParam("id") String id,
			ModelMap model) throws Exception {
		QuartzJobAudit quartzjob = quartzJobManager.getQuartzId(id);
		model.put("quartzJob", quartzjob);
//		log.info("QuartzJob Edited successfully");
		return new ModelAndView("admin/quartzJob", model);
	}

	@RequestMapping(value = "bpm/admin/showquartzJobGrid", method = RequestMethod.GET)
	public ModelAndView showquartzJobGrid(HttpServletRequest request,
			HttpServletResponse response) {
		Model model = new ExtendedModelMap();
		Locale locale = request.getLocale();
		try {
			List<QuartzJobAudit> quartzjob = quartzJobManager.getAllQuartzJob();
			String script = GridUtil.generateScriptForQuartzJobGrid(
					CommonUtil.getMapListFromObjectList(quartzjob),
					velocityEngine);
			model.addAttribute("script", script);
//			log.info("Quartz job retrived Successfully");
		} catch (Exception e) {
			saveError(request, getText("errors.quartzjobs.getEvents", locale));
		}
		return new ModelAndView("admin/quartzJobGrid", model.asMap());
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getTriggerGroup() {
		return triggerGroup;
	}

	public String getJobGroup() {
		return jobGroup;
	}


	public String getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}



	public String getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}

	@SuppressWarnings("unchecked")
	private List generateNameValueBeanForQuartzJob(final List<String> values) {
		List valuesObjs = new ArrayList();

		if (values != null) {
			for (int i = 0; i < values.size(); i++) {
				valuesObjs
						.add(new LabelValueBean(values.get(i), values.get(i)));
			}
		}

		return valuesObjs;
	}

	

	public void setTriggerListener(TriggerListener triggerListener) {
		this.triggerListener = triggerListener;
	}

	public TriggerListener getTriggerListener() {
		return triggerListener;
	}

	@Autowired
	public void setQuartzScheduler(Scheduler quartzScheduler) {
		this.quartzScheduler = quartzScheduler;
	}

	
	public Scheduler getQuartzScheduler() {
		return quartzScheduler;
	}
}
