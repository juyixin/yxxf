package com.eazytec.webapp.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.bpm.admin.systemLog.service.SystemLogManager;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.SystemLog;


/**
 *  @author Sangeetha Guhan
 */
 

@Controller
public class SystemLogController extends BaseFormController {

	private SystemLogManager systemLogManager;

	public VelocityEngine velocityEngine;

	@Autowired
	public void setsystemLogManager(SystemLogManager systemLogManager) {
		this.systemLogManager = systemLogManager;
	}

	public SystemLogManager getsystemLogManager() {
		return systemLogManager;
	}

	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	@RequestMapping(value = "bpm/admin/systemLogForm", method = RequestMethod.GET)
	public ModelAndView showEventForm(SystemLog systemLog, ModelMap model,
			HttpServletRequest request) {
		try {
			log.info("show event form");
			model.addAttribute("systemLog", systemLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("admin/systemLogForm", model);
	}

	@RequestMapping(value = "bpm/admin/saveSystemLog", method = RequestMethod.POST)
	public ModelAndView saveSystemLog(SystemLog systemLog,
			BindingResult errors, ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {
		String id = systemLog.getId();
		String listView="LOG_SETTING";
		 String container = "target";
		 String listViewParams = "[{}]";
		 String title = "Log Setting";
		 String systemLogId = request.getParameter("id");
		String cycleValue = request.getParameter("cycleValue");
				
		try {
			Locale locale = request.getLocale();
			model.addAttribute("listViewName",listView);
			model.addAttribute("container",container);
			model.addAttribute("listViewParams",listViewParams);
			model.addAttribute("title",title);
			
			
			if (validator != null) {
				validator.validate(systemLog, errors);
				if (errors.hasFieldErrors("name")) {
					model.addAttribute("systemLogId", systemLogId);
					return new ModelAndView("admin/systemLogForm", model);
				} else if(errors.hasFieldErrors("moduleName")) {
					model.addAttribute("systemLogId", systemLogId);
					return new ModelAndView("admin/systemLogForm", model);
				}
				
			}
			if (systemLog.getId() == null  || systemLog.getId().isEmpty()) {
				systemLog.setId(null);
			}
			if(systemLog.getStatus()){
				if(getsystemLogManager().isDuplicateLogSetting(systemLog.getLogType(),systemLog.getId())){
					errors.rejectValue("logType","errors.existing.logType" ,new Object[]{systemLog.getLogType()} ,"duplicate logtype enabling");
					model.addAttribute(systemLog);
	            	return new ModelAndView("admin/systemLogForm",model);
				}
			}
			
			
			//For Name already Exists Exception
			boolean systemLogNameExists = systemLogManager.checkSystemLogNameExists(systemLog.getName().toLowerCase());
			if( id == null  && systemLogNameExists ) {
			  	log.info("SystemLog Name already Exists Please try again with other name");
				throw new EazyBpmException("errors.existing.sysLogName");
			}
		  							
			if( Integer.parseInt(cycleValue) < 1  ) {
		  		throw new NumberFormatException("systemLog.cycleValue.positive");
		  	}else{
		  		systemLog.setCycleValue(Integer.parseInt(cycleValue));		  		
		  	}
						
		  	systemLogManager.saveSystemLog(systemLog);
		  	if (StringUtil.isEmptyString(id)) {
				saveMessage(request, getText("success.systemlog.save", locale));
				log.info("SystemLog saved Successfully");
			} else {
				saveMessage(request, getText("success.systemlog.update", locale));
				log.info("SystemLog updated Successfully");
			}
		} catch (EazyBpmException e) {
			log.error(e);
			saveError(request, e.getMessage());
			return new ModelAndView("admin/systemLogForm", model);
		} catch (NumberFormatException ex) {
			saveError(request, ex.getMessage());
			log.error(ex);
			//errors.rejectValue("error.systemlog.save", ex.getMessage());
			return new ModelAndView("admin/systemLogForm", model);
		} catch (Exception e) {
			saveError(request, e.getMessage());
			log.error(e);
			//errors.rejectValue("error.systemlog.save", e.getMessage());
			return new ModelAndView("admin/systemLogForm", model);
		}
		return new ModelAndView("redirect:/bpm/listView/showListViewGrid");
	}
	
//	@RequestMapping(value = "bpm/admin/editSystemLog", method = RequestMethod.GET)
//	public ModelAndView editSystemLog(@RequestParam("id") String id,
//			ModelMap model) throws Exception {
//		SystemLog systemlog = systemLogManager.getId(id);
//		model.put("systemLog", systemlog);
//		return new ModelAndView("admin/systemLogForm", model);
//	}
	
	@RequestMapping(value = "bpm/admin/editSystemLog", method = RequestMethod.GET)
	public ModelAndView editSystemLog(@RequestParam("id") String id,
			ModelMap model) throws Exception {
		SystemLog systemlog = systemLogManager.getId(id);
		model.put("systemLog", systemlog);
	  	log.info("SystemLog Edited Successfully");
		return new ModelAndView("admin/systemLogForm", model);
	}
	
	@RequestMapping(value = "bpm/admin/deleteSystemLog", method = RequestMethod.GET)
	public ModelAndView deleteSystemLog(
			@RequestParam("systemLogIds") String systemLogIds,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {
		Locale locale = request.getLocale();
		String[] ids = systemLogIds.split(",");
		String listView="LOG_SETTING";
		 String container = "target";
		 String listViewParams = "[{}]";
		 String title = "Log Setting";
		try {
			systemLogManager.deleteSelectedSystemLog(Arrays.asList(ids));
			saveMessage(request, getText("success.systemlog.delete", locale));
			log.info("SystemLog Deleted Successfully");
		} catch (BpmException e) {
			log.error(e.getMessage(), e);
			saveError(request,
					getText("error.systemlog.delete", e.getMessage(), locale));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			saveError(request,
					getText("error.systemlog.delete", e.getMessage(), locale));

		}
		model.addAttribute("listViewName",listView);
		model.addAttribute("container",container);
		model.addAttribute("listViewParams",listViewParams);
		model.addAttribute("title",title);
		return new ModelAndView("redirect:/bpm/listView/showListViewGrid");
	}
	
	@RequestMapping(value = "bpm/admin/showSystemLogGrid", method = RequestMethod.GET)
	public ModelAndView showSystemLogGrid(HttpServletRequest request,
			HttpServletResponse response) {	
		Model model = new ExtendedModelMap();
		Locale locale = request.getLocale();
		try {
			List<SystemLog> systemlog = systemLogManager.getAllSystemLog();
			String script = GridUtil.generateScriptForSystemLogGrid(CommonUtil.getMapListFromObjectList(systemlog),velocityEngine);
			log.info("SystemLogGrid after script");
			model.addAttribute("script", script);
			log.info("SystemLogGrid after addAttribute");
		} catch (Exception e) {
			log.warn(e.getMessage());
			saveError(request, getText("errors.systemlog.getEvents", locale));
		}
		return new ModelAndView("admin/systemLogGrid", model.asMap());
	}
	
	@RequestMapping(value = "bpm/admin/deleteLog", method = RequestMethod.GET)
	public ModelAndView deleteLog(
			@RequestParam("logDate") String logDate,@RequestParam("logType") String logType,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {
			Locale locale = request.getLocale();
			String[] ids = logDate.split(",");
			 String listView="";
			 String container = "target";
			 String listViewParams = "[{}]";
			 String title = "";
			 
			if(logType.equalsIgnoreCase("admin")){
				logType = "AdminLogs";
				listView = "ADMIN_LOGS";
				title = "Admin Logs";
			}else if(logType.equalsIgnoreCase("process")){
				logType = "ProcessLogs";
				listView = "PROCESS_LOGS";
				title = "Process Logs";
			}else if(logType.equalsIgnoreCase("System Log")){
				logType = "SystemLogs";
				listView = "SYSTEM_LOGS";
				title = "System Logs";
			}else if(logType.equalsIgnoreCase("module")){
				logType = "ModuleLogs";
				listView = "MODULE_LOGS";
				title = "Module Logs";
			}else{
				logType = "LoginLogs";
				listView = "LOGIN_LOGS";
				title = "Login Logs";
			}
			try {
				systemLogManager.deleteLog(Arrays.asList(ids),logType);
				saveMessage(request, getText("success.log.delete", locale));
				log.info("log deleted Successfully");
			} catch (BpmException e) {
				log.error(e.getMessage(), e);
				saveError(request,
						getText("error.systemlog.delete", e.getMessage(), locale));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				saveError(request,
						getText("error.systemlog.delete", e.getMessage(), locale));

			}
			 model.addAttribute("listViewName",listView);
			 model.addAttribute("container",container);
			 model.addAttribute("listViewParams",listViewParams);
			 model.addAttribute("title",title);
			 return new ModelAndView("redirect:/bpm/listView/showListViewGrid");

	}

}
