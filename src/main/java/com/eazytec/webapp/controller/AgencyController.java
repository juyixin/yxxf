package com.eazytec.webapp.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.notification.service.NotificationService;
import com.eazytec.bpm.runtime.agency.service.AgencyService;
import com.eazytec.bpm.runtime.task.service.OperatingFunctionService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.AgencySetting;
import com.eazytec.model.MetaTable;
import com.eazytec.model.Notification;
import com.eazytec.model.User;
import com.eazytec.service.LookupManager;

@Controller
public class AgencyController extends BaseFormController{
	
	/**
	 * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
	 */
	protected final Log log = LogFactory.getLog(getClass());
	
	private AgencyService agencyService;
	
	private NotificationService notificationService;
	
	private OperatingFunctionService opFunctionService;

	@Autowired
	public void setOpFunctionService(OperatingFunctionService opFunctionService) {
		this.opFunctionService = opFunctionService;
	}

	@Autowired
	public void setAgencyService(AgencyService agencyService) {
		this.agencyService = agencyService;
	}
	
	@Autowired
	public void setAgencyService(NotificationService notificationService) {
		this.notificationService = notificationService;
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
	
	 @RequestMapping(value = "bpm/agency/agencyForm",method = RequestMethod.GET)
	public ModelAndView showAgencySettingForm(HttpServletRequest request,ModelMap model){
		User user=CommonUtil.getLoggedInUser();
		AgencySetting agencySetting=agencyService.getAgencySettingByUser(user.getId());
		if(null!=agencySetting){
			model.addAttribute("agencySetting",agencySetting);
		}else{
			agencySetting=new AgencySetting();
			agencySetting.setUserId(user.getId());
			model.addAttribute("agencySetting",agencySetting);
		}
		return new ModelAndView("admin/agencySettingForm",model);
	}
	
	@RequestMapping(value = "bpm/agency/saveAgencySetting", method = RequestMethod.POST)
   	public ModelAndView saveAgencySetting(@ModelAttribute("agencySetting") AgencySetting agencySetting,
   			BindingResult errors, ModelMap model, HttpServletRequest request) throws Exception {
		Locale locale = request.getLocale();
		model.addAttribute("listViewName","AGENCY_LIST");
		model.addAttribute("listViewTitle","AGENCY LIST");
/*		if (validator != null) {
        	validator.validate(agencySetting, errors);
            if (errors.hasErrors()) { 
            	return new ModelAndView("listview/reloadListView", model);
            }
        }*/
		try {
				List<String> processList=CommonUtil.convertJsonToListStrings(request.getParameter("processIds"));
				agencyService.saveOrUpdate(agencySetting,processList);
	        		if(agencySetting.getUserId().equalsIgnoreCase(CommonUtil.getLoggedInUser().toString())){
	        			String[] agentIds = agencySetting.getAgent().split(","); 
	            			setNotificationForAgency(agencySetting, agentIds);
	            		
	        		}
				saveMessage(request, getText("success.agency.save",locale));
    	   		log.info("Agency Setting saved successfully");

		}catch (EazyBpmException e1) {
			// TODO Auto-generated catch block
			saveError(request, getText("error.agency.save",locale));
			 log.error(e1.getMessage(), e1);
			e1.printStackTrace();
		}
		
/*		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
        LookupManager mgr = (LookupManager) ctx.getBean("lookupManager");
		request.getSession().getServletContext().setAttribute(Constants.AGENCY_SETTING,mgr.getAllAgencySetting());*/
		
		return new ModelAndView("listview/reloadListView",model);
	}
	
	 @RequestMapping(value="bpm/agency/agencySetting", method = RequestMethod.GET)
		public ModelAndView agencySetting(ModelMap model,HttpServletRequest request) {
		 	User user=CommonUtil.getLoggedInUser();
		 	AgencySetting agencySetting=new AgencySetting();
		 	
			agencySetting.setUserId(user.getId());
		 	model.addAttribute("agencySetting",agencySetting);
			return new ModelAndView("admin/agencySettingForm", model);	
		}
	 
	 @RequestMapping(value="bpm/agency/editAgencySetting", method = RequestMethod.GET)
		public ModelAndView editAgencySetting(@ModelAttribute("agentId") String agencySettingId,ModelMap model,HttpServletRequest request) {
		 	AgencySetting agencySetting=agencyService.get(agencySettingId);
		 	model.addAttribute("processId",agencySetting.getProcessId()); 
		 	model.addAttribute("isEdit",true);
		 	model.addAttribute("agencySetting",agencySetting);
		 	log.info("Agency Setting updated successfully");
			return new ModelAndView("admin/agencySettingForm", model);	
		}
	 
	 @RequestMapping(value="bpm/agency/removeAgentSettings",method = RequestMethod.GET)
	 public @ResponseBody Map<String, Object> removeAgentSettings(@ModelAttribute("agentIds") String agentIds,ModelMap model,HttpServletRequest request)throws Exception{
		 Map<String, Object> responseMap = new HashMap<String, Object>();
		 Locale locale = request.getLocale();
		try {
			List<String> agentIdList = CommonUtil.convertJsonToListStrings(agentIds);
			agencyService.removeAgentByIds(agentIdList);
			responseMap.put("status", "success");
        	responseMap.put("message", getText("success.agency.removed",locale));
        	log.info("Agency Setting removed successfully");
			
		} catch (EazyBpmException e) {
			 log.error(e.getMessage(), e);
	         responseMap.put("status", "error");
	         responseMap.put("message",getText("error.agency.remove",e.getMessage(),locale));
	    }
		
		 return responseMap;
	 }
	 
	  public void setNotificationForAgency(AgencySetting agencySetting,String[] agentIds){
		  if(agencySetting.getIsMail()){
			  setNotificationByType(agencySetting,agentIds,Constants.NOTIFICATION_EMAIL_TYPE);
		  }
		  if(agencySetting.getIsSms()){
			  setNotificationByType(agencySetting,agentIds,Constants.NOTIFICATION_SMS_TYPE);  
		  }
		  if(agencySetting.getIsInternalMessage()){
			  setNotificationByType(agencySetting,agentIds,Constants.NOTIFICATION_INTERNALMESSAGE_TYPE);  
		  }
	  }	 
	  private void setNotificationByType(AgencySetting agencySetting,String[] agentIds,String type){
		  for(String agentId:agentIds){
			  opFunctionService.sendNotification(agentId, agencySetting.getNotificationMessage(), type , Constants.AGENCY_NOTIFICATION);
		  }
	  }	  
}
