package com.eazytec.webapp.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.util.StopWatch;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.department.service.DepartmentService;
import com.eazytec.bpm.admin.group.service.GroupService;
import com.eazytec.bpm.admin.role.service.RoleService;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.bpm.oa.schedule.service.ScheduleService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.Department;
import com.eazytec.model.Group;
import com.eazytec.model.Role;
import com.eazytec.model.Schedule;
import com.eazytec.model.User;
import com.eazytec.service.UserManager;

/**
 * Implementation of Scheduling the events and 
 * retrieve/persist values in database.
 *
 * @author mathi
 */

@Controller
public class ScheduleController extends BaseFormController{
    
	private ScheduleService scheduleService; 
	
	public VelocityEngine velocityEngine;
	
	
	private RoleService roleService;
    
	private GroupService groupService;
    
    private DepartmentService departmentService;
    
    @Autowired
    public void setRoleService(RoleService roleService) {
  		this.roleService = roleService;
  	}
    
    @Autowired
    public void setGroupService(GroupService groupService) {
  		this.groupService = groupService;
  	}

    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
  		this.departmentService = departmentService;
  	}
    
    @Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Autowired
	public void setScheduleService(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}

    /**
	 * To bind date and string fields from controller to jsp
	 * 
	 * @param request
	 * @param binder
	 * 
	 * @return
	 */
    @Override
    public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
        binder.registerCustomEditor(Integer.class, null,
                new CustomNumberEditor(Integer.class, null, true));
    }

	/**
	 * show the calendar to schedule events
	 * 
	 * @param model
	 * @param schedule
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/oa/scheduleForm",method = RequestMethod.GET)
    public ModelAndView showSchedule(Schedule schedule, HttpServletRequest request) {
		log.info("show schedule form");
		Model model = new ExtendedModelMap();
		setIsAdminModel(model, request.isUserInRole(Constants.ADMIN_ROLE));
        return new ModelAndView("oa/scheduleForm",model.asMap());
    }
	
	/**
	 * show popup window for create event
	 * 
	 * @param model
	 * @param schedule
	 * @param errors
	 * 
	 * @return
	 */
	@RequestMapping(value="bpm/oa/eventForm", method = RequestMethod.GET)
    public ModelAndView showEventForm(Schedule schedule,BindingResult errors, Model model, 
    		@RequestParam("eventOwner") String eventOwner, @RequestParam("startDate") String startDate, 
    		@RequestParam("endDate") String endDate, @RequestParam("from") String from, HttpServletRequest request){
		log.info("show event form");
		Locale locale = request.getLocale();
		Date date = new Date();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		if (startDate == null || startDate.isEmpty()) {
			startDate = date.toString();
		}
		if (endDate == null || endDate.isEmpty()) {
			endDate = date.toString();
		}
		try {
			schedule.setStartDate((Date)formatter.parse(startDate));
			schedule.setEndDate((Date)formatter.parse(startDate));
			schedule.setCreatedTime(date);
			schedule.setUpdatedTime(date);
			schedule.setRemindBefore(Integer.parseInt("0"));
			schedule.setStartTime("00:00");
			schedule.setEndTime("23:59");
			schedule.setFrom(from);
			schedule.setCreatedBy(CommonUtil.getLoggedInUserId());
			model.addAttribute("users",request.getRemoteUser());
			setIsAdminModel(model, request.isUserInRole(Constants.ADMIN_ROLE));
			log.info("show event form Displayed");
		} catch (ParseException e) {
			log.warn(e.getMessage());
    		saveError(request, getText("error in parse date",locale));
		}
		model.addAttribute("schedule", schedule);
    	return new ModelAndView("/oa/eventForm", model.asMap());
    }
	
	/**
	 * show popup window for edit event
	 * 
	 * @param schedule
	 * @param errors
	 * @param eventId
	 * @param from
	 * 
	 * @return
	 */
	@RequestMapping(value="bpm/oa/editEvent", method = RequestMethod.GET)
    public ModelAndView editEvent(@RequestParam("eventId") String eventId, @RequestParam("from") String from, 
    			Schedule schedule,BindingResult errors, HttpServletRequest request){
		Model model = new ExtendedModelMap();
		Locale locale = request.getLocale();
		String users = "";
    	try {
    		schedule = scheduleService.getEventForEdit(eventId);
	    	users = schedule.getUserNames();
	    	schedule.setFrom(from);
	    	model.addAttribute("schedule",schedule);
	    	model.addAttribute("users",users);
	    	setIsAdminModel(model, request.isUserInRole(Constants.ADMIN_ROLE));
	    	log.info("Edit event Window popup Displayed Successfully");
    	} catch (BpmException e) {
    		log.warn(e.getMessage());
    		saveError(request, getText("errors.schedule.getEvent",locale));
    		return new ModelAndView("/oa/eventForm", model.asMap());
    	} catch (Exception e) {
			log.warn(e.getMessage());
			saveError(request, getText("errors.schedule.getEvent",locale));
			model.addAttribute("jsonString","error");
		}
	    return new ModelAndView("/oa/eventForm", model.asMap());
    }
	
	/**
	 * get events are already created
	 * 
	 * @param model
	 * 
	 * @return
	 */
	@RequestMapping(value="bpm/oa/getEvents", method = RequestMethod.GET)
    public @ResponseBody String getEvents(@RequestParam("eventOwner") String eventOwner,
    		@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, 
    		ModelMap model,Schedule schedule, BindingResult errors, HttpServletRequest request,
            HttpServletResponse response){
//		log.info("getting events");
		String jsonString = "";
		Locale locale = request.getLocale();
		List<Schedule> events = new ArrayList<Schedule>();
		try{
			if (request.isUserInRole(Constants.ADMIN_ROLE)) {
				if ( eventOwner != null && eventOwner != "") {
					events=scheduleService.getEventsByOwnerId(eventOwner, startDate, endDate);
				} else {
					events=scheduleService.getAllEvents();
				}
	       	}else{
	       		events=scheduleService.getEventsByOwnerId(CommonUtil.getLoggedInUserId(),startDate, endDate);
	       	}
			if(events != null && events.size() > 0){
				jsonString = scheduleService.getEventsJsonArray(events).toString();
			}
	    	model.addAttribute("jsonString",jsonString);
	    	log.info("Event Retrived Successfully");
		}catch (BpmException e) {
			log.warn(e.getMessage());
			saveError(request, getText("errors.schedule.getEvents",locale));
			model.addAttribute("jsonString","error");
		}catch (Exception e) {
			log.warn(e.getMessage());
			saveError(request, getText("errors.schedule.getEvents",locale));
			model.addAttribute("jsonString","error");
		}
		return jsonString;
    }
	
	
	/**
	 * save the event
	 * 
	 * @param schedule
	 * @param errors
	 * @param request
	 * @param response
	 * 
	 * @return
	 */
	@RequestMapping(value="bpm/oa/saveEvent", method = RequestMethod.POST)
    public ModelAndView saveEvent(Schedule schedule, BindingResult errors, HttpServletRequest request,
            HttpServletResponse response){
		log.info("saving event");
		Model model = new ExtendedModelMap();
		int originalVersion = schedule.getVersion();
		Locale locale = request.getLocale();
		String from = schedule.getFrom();
		String userIds = request.getParameter("users");
		String eventId = schedule.getId();
		try{
			Set<User> users = CommonUtil.getUsersFromuserIds(userIds);
			if (validator != null) {
	        	validator.validate(schedule, errors);
	            if (errors.hasErrors()) { 
	            	setscheduleErrorFields(model, schedule, from, userIds, originalVersion, request);
	            	return new ModelAndView("/oa/eventForm", model.asMap());
	            }
	        }
			
			/*if (schedule.getStartTime().equals(schedule.getEndTime())) {
   				errors.rejectValue("endTime","errors.starttime.endtime");
   				setscheduleErrorFields(model, schedule, from, userIds, originalVersion, request);
   				return new ModelAndView("/oa/eventForm", model.asMap());
			}*/
			schedule.setUserNames(userIds);
			schedule.setUsers(users);
			schedule = scheduleService.saveEvent(schedule);
			if(!StringUtil.isEmptyString(eventId)) {
				saveMessage(request, getText("success.event.update",locale));
			} else {
				saveMessage(request, getText("success.event.save",locale));
			}
			log.info(schedule.getEventName()+" saved Succesfully");
			setIsAdminModel(model, request.isUserInRole(Constants.ADMIN_ROLE));
			if (from.equalsIgnoreCase("grid")) {
				List<Schedule> schedules = scheduleService.getSchedulesGridData(request.isUserInRole(Constants.ADMIN_ROLE));
				String script=GridUtil.generateScriptForScheduleGrid(CommonUtil.getMapListFromObjectList(schedules), velocityEngine);
			    model.addAttribute("script", script);
			    return new ModelAndView("oa/scheduleList", model.asMap());
			}else {
				model.addAttribute("schedule",schedule);
				model.addAttribute("eventId",schedule.getId());
				return new ModelAndView("/oa/eventForm", model.asMap());
			}
		} catch (BpmException e) {
			log.warn(e.getMessage());
			saveError(request, getText("errors.schedule.save",locale));
			setscheduleErrorFields(model, schedule, from, userIds, originalVersion, request);
            return new ModelAndView("/oa/eventForm", model.asMap());
		} catch(Exception e) {
			log.warn(e.getMessage());
			saveError(request, getText("errors.schedule.save",locale));
			setscheduleErrorFields(model, schedule, from, userIds, originalVersion, request);
            return new ModelAndView("/oa/eventForm", model.asMap());
		}
    }
	
	/**
	* method to show list of schedules are available
	* 
	* @return
	* @throws Exception
	*/
	@RequestMapping(value = "bpm/oa/schedules", method = RequestMethod.GET)
	public ModelAndView showScheduleGrid(Schedule schedule, BindingResult errors, HttpServletRequest request,
	            HttpServletResponse response) {
		log.info("getting grid datas");
		Model model = new ExtendedModelMap();
		Locale locale = request.getLocale();
		try {
			List<Schedule> schedules = scheduleService.getSchedulesGridData(request.isUserInRole(Constants.ADMIN_ROLE));
			String script=GridUtil.generateScriptForScheduleGrid( CommonUtil.getMapListFromObjectList(schedules), velocityEngine);
		    model.addAttribute("script", script);
		    log.info("List of schedule is retrived");
		} catch (Exception e) {
			log.warn(e.getMessage());
			saveError(request, getText("errors.schedule.getEvents",locale));
		}
		return new ModelAndView("oa/scheduleList", model.asMap());
	}
	
	/**
	* method to show event owner selection
	* 
	* @param title
	* @param requestorId
	* @param selectType
	* @param model
	* 
	* @return
	* @throws Exception
	*/
	@RequestMapping(value="bpm/oa/eventOwnerTree", method = RequestMethod.GET)
    public ModelAndView showEventOwnerTree(@RequestParam("title") String title,@RequestParam("id") String requestorId,@RequestParam("selectType") String selectType,ModelMap model) {
		log.info("show event owner popup");
    	List<Role> roleList=roleService.getRoles();
    	List<Group> groupList = groupService.getGroups();
    	List<Department> departmentList = departmentService.getDepartments();
    	model.addAttribute("roles", roleList);
    	model.addAttribute("groups", groupList);
    	model.addAttribute("departments", departmentList);
    	model.addAttribute("title",title);
    	model.addAttribute("requestorId",requestorId);
    	model.addAttribute("selectType",selectType);
    	return new ModelAndView("oa/showEventOwnerTree", model);
    }
	
	/**
	 * method for delete event by eventId
	 * 
	 * @param eventId
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/oa/deleteEvent", method = RequestMethod.GET)
	public @ResponseBody String deleteEvent(@RequestParam("eventId") String eventId, @RequestParam("eventOwner") String eventOwner, HttpServletRequest request) {
		log.info("deleting event"+eventId);
		Locale locale = request.getLocale();
		String jsonString = "";
		try {
			scheduleService.deleteEvent(eventId);
			List<Schedule> events=scheduleService.getEventsByOwnerId(eventOwner);
			if(events != null && events.size() > 0){
				jsonString = scheduleService.getEventsJsonArray(events).toString();
			}
			log.info("Event Deleted Successfully.");
		} catch (BpmException e) {
			log.error(e.getMessage(), e);
			saveError(request, getText("error.events.delete",e.getMessage(),locale));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			saveError(request, getText("error.events.delete",e.getMessage(),locale));
		}	
		return jsonString;
	}
	
	/**
	 * method for delete the selected schedules
	 * 
	 * @param eventIds
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/oa/deleteSelectedSchedules", method = RequestMethod.GET)
	public ModelAndView deleteSelectedSchedules(@RequestParam("eventIds") String eventIds, HttpServletRequest request) {
		log.info("deleting SelectedSchedules");
		Locale locale = request.getLocale();
		try {
			scheduleService.deleteEvents(eventIds);
			saveMessage(request, getText("success.events.delete",locale));
			log.info("Select Event Deleted Successfully.");
		} catch (BpmException e) {
			log.error(e.getMessage(), e);
			saveError(request, getText("error.events.delete",e.getMessage(),locale));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			saveError(request, getText("error.events.delete",e.getMessage(),locale));
		}	
		return new ModelAndView("redirect:schedules");
	}
	
	
	
	/**
	 * set the user is admin or not in model
	 * 
	 * @param isAdmin
	 */
	
	public Model setIsAdminModel(Model model, boolean isAdmin) {
		if (isAdmin) {
			model.addAttribute("isAdmin",true);
       	}else{
       		model.addAttribute("isAdmin",false);
       	}
		return model;
	}
	
	/**
	 * set schedule fields
	 * 
	 * @param isAdmin
	 */
	
	public Schedule setscheduleErrorFields(Model model, Schedule schedule, String from, String userIds, int originalVersion, HttpServletRequest request) {
		schedule.setFrom(from);
		schedule.setVersion(originalVersion);
		model.addAttribute("users",userIds);
		setIsAdminModel(model, request.isUserInRole(Constants.ADMIN_ROLE));
		return schedule;
	}
	
	@RequestMapping(value = "bpm/oa/getScheduleList", method = RequestMethod.GET)
	public @ResponseBody List<Map<String,Object>> getScheduleList(HttpServletRequest request){
		List<Map<String, Object>> scheduleDetail = new ArrayList<Map<String, Object>>();
		int count = 0;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	    Date date = new Date();
	    try{
			List<Schedule> events=scheduleService.getEventsByOwner(CommonUtil.getLoggedInUserId());
			if(events!=null && events.size()>0){
				for(Schedule scheduleEvent : events){
					if(dateFormat.format(scheduleEvent.getStartDate()).compareTo(dateFormat.format(date))>=0 && (count < 4)){
						Map<String,Object> showScheduleEvent= new HashMap<String,Object>();
						showScheduleEvent.put("name",scheduleEvent.getEventName());
						showScheduleEvent.put("description",scheduleEvent.getDescription());
						showScheduleEvent.put("location", scheduleEvent.getLocation());
						showScheduleEvent.put("endDate", dateFormat.format(scheduleEvent.getEndDate()));
						showScheduleEvent.put("startdate", dateFormat.format(scheduleEvent.getStartDate()));
						scheduleDetail.add(showScheduleEvent);
						count++;
					}
				}
			}
	    }catch(Exception e) {
			log.error(e.getMessage(), e);
	    }
		
		return scheduleDetail;
	}
}
