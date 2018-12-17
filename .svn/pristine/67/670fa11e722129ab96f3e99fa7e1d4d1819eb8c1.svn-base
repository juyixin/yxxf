package com.eazytec.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.department.service.DepartmentService;
import com.eazytec.bpm.admin.layout.service.LayoutService;
import com.eazytec.bpm.admin.sysConfig.service.SysConfigManager;
import com.eazytec.bpm.admin.widget.service.WidgetService;
import com.eazytec.bpm.common.util.FileUtils;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.Layout;
import com.eazytec.model.Classification;
import com.eazytec.model.SysConfig;
import com.eazytec.model.User;
import com.eazytec.model.Widget;

@Controller
public class WidgetController extends BaseFormController{
	
    protected final transient Log log = LogFactory.getLog(getClass());

    private WidgetService widgetService; 

	public VelocityEngine velocityEngine;
	
    private DepartmentService departmentService;
	
	private String isUpdate = "false";

	
	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
    
	@Autowired
	public void setWidgetService(WidgetService widgetService) {
		this.widgetService = widgetService;
	}
	
    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
  		this.departmentService = departmentService;
  	}
	
	@RequestMapping(value = "bpm/admin/saveWidget", method = RequestMethod.POST)
	public ModelAndView saveWidget(HttpServletRequest request,@ModelAttribute("widget") Widget widget, ModelMap model,BindingResult errors) {
		Locale locale = request.getLocale();
		String widgetId = widget.getId();
		String isDepartmentAdmin = request.getParameter("isDepartmentAdmin");
		try {
			if (validator != null) {
				validator.validate(widget, errors);
				if (errors.hasErrors()) {
					setIsUpdate(getIsUpdate());
					if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
						model.addAttribute("mode", Constants.EDIT_MODE);
						model.addAttribute("linkType",widget.getLinkType());
					}else{
						model.addAttribute("mode", Constants.CREATE_MODE);
						model.addAttribute("linkType",widget.getLinkType());
						}
					model.addAttribute("widget", widget);
					return new ModelAndView("homePage/createWidget", model);
				}
			}
			
			if(widget.getLinkType().equalsIgnoreCase("JSP")){
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
		        String uploadDir=getServletContext().getRealPath("/WEB-INF") + "/pages/jsp/";
		        
				if((StringUtil.isEmptyString(widget.getId()) && StringUtil.isEmptyString(widget.getWidgetUrl()) && file == null) || (!StringUtil.isEmptyString(widget.getId()) && !StringUtil.isEmptyString(widget.getWidgetUrl()))){
					saveError(request,getText("widget.file.required", locale));
				} else {
					setIsUpdate(getIsUpdate());
					if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
						model.addAttribute("mode", Constants.EDIT_MODE);
						model.addAttribute("linkType",widget.getLinkType());
					}else{
						model.addAttribute("mode", Constants.CREATE_MODE);
						model.addAttribute("linkType",widget.getLinkType());
						}
					model.addAttribute("widget", widget);
				//	return new ModelAndView("homePage/createWidget", model);
				}
				
		        String filePath = FileUtils.uploadFile(file, uploadDir);
		        if(!StringUtil.isEmptyString(filePath)){
		        	widget.setWidgetUrl("../pages/jsp/"+file.getOriginalFilename());
		        }
			}
			if(StringUtil.isEmptyString(widget.getId())){
				widget.setId(null);
			}
			
			 User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			 if(!CommonUtil.isUserAdmin(user)){
				 widget.setIsPublic(false);
			 }else{
				 widget.setIsPublic(true);
			 }
			
			widgetService.saveWidget(widget);
			if(!widget.getIsPublic()){
				widgetService.setDepartmentType(widget);
			}
			
			
			if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
				//saveMessage(request,getText("success.report.update", locale));
				saveMessage(request, getText("widget.update.success",locale));
				log.info("Widget updated Successfully");
			} else {
				saveMessage(request, getText("widget.save.success", locale));
				log.info("Widget saved Successfully");
			}
			 model.addAttribute("isDepartmentAdmin",isDepartmentAdmin);
		} catch(BpmException be){
			log.error(be.getMessage(),be);
			 setIsUpdate(getIsUpdate());
			 if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
				model.addAttribute("mode", Constants.EDIT_MODE);
				model.addAttribute("linkType",widget.getLinkType());
			}else{
				model.addAttribute("mode", Constants.CREATE_MODE);
				model.addAttribute("linkType",widget.getLinkType());
				}
			widget.setId(widgetId);
			model.addAttribute("widget", widget);
			saveError(request,getText("widget.save.error", be.getMessage(), locale));
			 model.addAttribute("isDepartmentAdmin",isDepartmentAdmin);
         }catch (Exception e) {
        	log.error(e.getMessage(),e);
        	widget.setId(widgetId);
        	setIsUpdate(getIsUpdate());
        	if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
 				model.addAttribute("mode", Constants.EDIT_MODE);
 				model.addAttribute("linkType",widget.getLinkType());
 			}else{
 				model.addAttribute("mode", Constants.CREATE_MODE);
 				model.addAttribute("linkType",widget.getLinkType());
 				}
 			model.addAttribute("widget", widget);
 			if(e.getMessage().contains("Duplicate entry '"+widget.getName()+"' for key 'name'")){
 				saveError(request,getText("widget.exists.error", e.getMessage(), locale));
 				log.info(getText("widget.exists.error", e.getMessage(), locale));
 			} else {
 				saveError(request,getText("widget.save.error", e.getMessage(), locale));
 				log.error(getText("widget.save.error", e.getMessage(), locale));
 			}
 			model.addAttribute("isDepartmentAdmin",isDepartmentAdmin);
		}

		return new ModelAndView("homePage/createWidget",model);
	}
	
	
	 @RequestMapping(value = "bpm/admin/showCreateWidget",method = RequestMethod.GET)
	 	public ModelAndView showCreateWidget(ModelMap model,HttpServletRequest request) {
		 Locale locale = request.getLocale();
		 try{
			 User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			 if(request.getParameter("id") != null){
				 setIsUpdate("true");
				 Widget widget = widgetService.getWidgetForId(request.getParameter("id").toString());
				// widget.setName(widget.getName());
				// widget.setClassify(widget.getClassify());
				 model.addAttribute("widget",widget);
				 model.addAttribute("mode", Constants.EDIT_MODE);
				 model.addAttribute("linkType",widget.getLinkType());
				 if(!CommonUtil.isUserAdmin(user)){
					 boolean isDepartmentAdmin=widgetService.getWidgetIsDepartmentAdmin(user,widget);
					 model.addAttribute("isDepartmentAdmin",isDepartmentAdmin);
				 }else{
					 model.addAttribute("isDepartmentAdmin",true);
				 }
			 }else{
				 	setIsUpdate("false");
					model.addAttribute("mode", Constants.CREATE_MODE);
					model.addAttribute("linkType","JSP");
				    model.addAttribute("widget",new Widget());
				    if(!CommonUtil.isUserAdmin(user)){
						 boolean isDepartmentAdmin=departmentService.getIsDepartmentAdmin(user);
						 model.addAttribute("isDepartmentAdmin",isDepartmentAdmin);
					 }else{
						 model.addAttribute("isDepartmentAdmin",true);
					 }
			 }
		 }catch(BpmException be){
			 setIsUpdate(getIsUpdate());
			 saveError(request,getText("show.create.widget.error", be.getMessage(), locale));
          log.error(be.getMessage(),be);
      }catch(Exception e){
    	  setIsUpdate(getIsUpdate());
     	 saveError(request,getText("show.create.widget.error", e.getMessage(), locale));
     	 log.error(getText("show.create.widget.error", e.getMessage(), locale));
      }
		 return new ModelAndView("homePage/createWidget",model);
	   }
	 
	 
	 @RequestMapping(value = "bpm/admin/manageWidget", method = RequestMethod.GET)
		public ModelAndView manageWidget(ModelMap model, HttpServletRequest request) {
			Locale locale = request.getLocale();
			try {
				List<Widget> widgetList = widgetService.getAllWidget();
				String[] fieldNames = { "id", "name", "widgetUrl","isActive" };
				String script = GridUtil.generateScriptForWidgetGrid(CommonUtil
						.getMapListFromObjectListByFieldNames(widgetList,
								fieldNames,""), velocityEngine);
				
				model.addAttribute("script", script);
				
			}catch(BpmException be){
				setIsUpdate(getIsUpdate());
				 saveError(request,getText("manage.widget.error", be.getMessage(), locale));
	             log.error(be.getMessage(),be);
	        }catch (Exception e) {
	        	setIsUpdate(getIsUpdate());
	        	log.error(e.getMessage(),e);
				saveError(request,getText("manage.widget.error", e.getMessage(), locale));
			}
			return new ModelAndView("homePage/manageWidget");
		}

	 @RequestMapping(value = "bpm/admin/widgetdelete", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object> deleteSelectedTriggers(
				@RequestParam("widgetNames") String widgetNames,
				HttpServletRequest request, ModelMap model) {
			Locale locale = request.getLocale();
			 Map<String,Object> message = new HashMap<String, Object>();
			try {
				//widgetService.deleteWidgets(widgetNames);
				
				 List<String> widgetIdList = new ArrayList<String>();
				 
					if (widgetNames.contains(",")) {
						String[] ids = widgetNames.split(",");
						for (String name : ids) {
							widgetIdList.add(name);
						}
					} else {
						widgetIdList.add(widgetNames);
					}
					widgetService.deleteWidgets(widgetIdList);
				//	layoutService.deleteSelectedQuickNav(roleIdList);
				
				//	saveMessage(request, getText("",triggerNames , locale));
				 message.put("successMessage", getText("widget.delete.success",locale));
				 log.info(getText("widget.delete.success",locale));
			}
			catch(Exception e){
				 message.put("errorMessage", getText("widget.delete.error",locale));
				setIsUpdate(getIsUpdate());
	        	log.error(e.getMessage(),e);
				saveError(request,getText("Error in deleting widget(s)", e.getMessage(), locale));
			}
				//saveMessage(request, getText("trigger.deleted",triggerNames , locale));
			
			 return message;
		//	return new ModelAndView("redirect:timingTask");
		}
	 
	 /**
	  * get all classification values as label
	  * @return 
	  * 
	  * @throws Exception
	  * 
	  */
	@RequestMapping(value = "bpm/admin/getAllClassification", method = RequestMethod.GET)
	public @ResponseBody List<Map<String,Object>> getAllClassification(ModelMap model, HttpServletRequest request){
		List<Map<String,Object>> classificationListMap = new ArrayList<Map<String,Object>>();
		List<Classification> classificationList;
		try{
			classificationList = widgetService.getAllClassification();
			for(Classification classification : classificationList){
				Map<String,Object> classificationMap = new HashMap<String,Object>();
				classificationMap.put("classificationName", classification.getName());
				classificationListMap.add(classificationMap);
			}
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
		return classificationListMap;
	}
	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getIsUpdate() {
		return isUpdate;
	}
		

}
