/**
 * ========================
 * File Name  : ReportController.java
 * @author   : L.Parameswaran
 * ========================
 */

package com.eazytec.webapp.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.ModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.bpm.admin.report.service.ReportService;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Report;
import com.eazytec.model.Role;
import com.eazytec.model.Department;
import com.eazytec.model.Group;
import com.eazytec.model.User;
import com.eazytec.model.UserOpinion;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.service.ReportExistsException;
import com.eazytec.util.PropertyReader;


@Controller
public class ReportController extends BaseFormController {
	
	private ReportService reportService;
	
	public VelocityEngine velocityEngine;
	
	public ReportService getReportService() {
		return reportService;
	}
	
	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	
	@Autowired
	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	@RequestMapping(value="bpm/report/reportForm", method = RequestMethod.GET)
    public ModelAndView showReportPage(@ModelAttribute ModelMap model) {
		try{
         model.addAttribute("report", new Report());
		} catch (Exception e){
			log.error(e.getMessage(),e);
		}
         return new ModelAndView("report/reportForm",model);
	}
	
	@RequestMapping(value = "bpm/report/saveReport",method = RequestMethod.POST)
	public ModelAndView saveReport(ModelMap model,@ModelAttribute("report") Report report, BindingResult errors, HttpServletRequest request,
	      HttpServletResponse response) { 
		  
	      String roles = request.getParameter("roles");
	      String groups = request.getParameter("groups");
	      String departments = request.getParameter("departments");
	     // String classification = request.getParameter("classification");
	      String id = report.getId();
	      Set<Role> rolesSet = new HashSet<Role>();
	      Set<Group> groupsSet = new HashSet<Group>();
	      Set<Department> departmentsSet = new HashSet<Department>();
		  String reportId = request.getParameter("id");
		 
		  try {
			  
			//For name and classification field validation
			  	if (validator != null) {
		        	validator.validate(report, errors); 
		            if (errors.hasFieldErrors("name")) {
		            	model.addAttribute("reportId", reportId);
		            	return new ModelAndView("report/reportForm", model);
		            } else if (errors.hasFieldErrors("classification")) {
		            	model.addAttribute("reportId", reportId);	            		      		
	            	    return new ModelAndView("report/reportForm", model);		            	
		            } else if (errors.hasFieldErrors("reportUrl")) {
			    		model.addAttribute("reportId", reportId);
			    		
			            return new ModelAndView("report/reportForm", model);
		            }
		        }
			  	
			  	//For Name already Exists Exception
			  	boolean reportNameExists = reportService.checkReportNameExists(report.getName().toLowerCase());
			  		if(report.getId() == null && reportNameExists) {
			  			log.info("Report Name already Exists Please try again with other name");
			  			throw new EazyBpmException("errors.existing.report");
			  		}
			  	
			  
			Locale locale = request.getLocale();
		  	if(roles != null && !roles.isEmpty()) {
	  			if (roles.contains(",")) {
	  				String[] ids = roles.split(",");
	  				for(String roleid :ids){
	  				rolesSet.add(new Role(roleid, roleid));
	  				}
	  			} else {
	  			rolesSet.add(new Role(roles, roles));
	  			}
	  			report.setRoles(rolesSet);
			} else {
				report.setRoles(null);
			}
			
			if(groups != null && !groups.isEmpty()) {
				if(groups.contains(",")) {
					String[] ids = groups.split(",");
					for (String groupId :ids) {
						groupsSet.add(new Group(groupId, groupId));
					}
				} else {
					groupsSet.add(new Group(groups, groups));
				} 
				report.setGroups(groupsSet);
			} else {
				report.setGroups(null);
			} 
			
			if(departments != null && !departments.isEmpty()) {
				if(departments.contains(",")) {
					String[] ids = departments.split(",");
					for(String departmentId :ids) {
						departmentsSet.add(new Department(departmentId, departmentId));
					}
				} else {
					departmentsSet.add(new Department(departments, departments));
				} 
				report.setDepartments(departmentsSet);
			} else {
				report.setDepartments(null);
			}
		  	
	    		
	 		reportService.save(report);
	   		if(StringUtil.isEmptyString(id)) {
	   			saveMessage(request,getText("success.report.save", locale));
	   			log.info("Report Saved Successfully.");
		   	} else {
		   		saveMessage(request,getText("success.report.update", locale));
		   		log.info("Report update Successfully.");
		   	}
	   		
		} catch (EazyBpmException e) {
			saveError(request, e.getMessage());
			log.error(e.getMessage(),e);
			errors.rejectValue("name", "errors.existing.report",
            new Object[]{report.getName()}, "errors.existing.report");	
			return new ModelAndView("report/reportForm", model);   
		}
		  
			
		  if(rolesSet.isEmpty()) {
			  String listView="REPORT_NO_ROLE";
			  String container = "target";
			  String listViewParams = "[{}]";
			  String title = "Manage Reports";
			  model.addAttribute("listViewName",listView);
			  model.addAttribute("container",container);
			  model.addAttribute("listViewParams",listViewParams);
			  model.addAttribute("title",title);
			  return new ModelAndView("redirect:/bpm/listView/showListViewGrid");
		  } else {
			  String listView="REPORT_NO_ROLE";
			  String container = "target";
			  String listViewParams = "[{}]";
			  String title = "Manage Reports";
			  model.addAttribute("listViewName",listView);
			  model.addAttribute("container",container);
			  model.addAttribute("listViewParams",listViewParams);
			  model.addAttribute("title",title);
			  model.addAttribute("roles",roles);
			  return new ModelAndView("redirect:/bpm/listView/showListViewGrid");
		  }
	}  
		  
	@RequestMapping(value = "bpm/report/editReport",method = RequestMethod.GET)
    public ModelAndView editReport(@RequestParam("id") String id, ModelMap model, HttpServletRequest request,
	           HttpServletResponse response) {
		
		/*String listView="REPORT";
		 String container = "target";
		 String listViewParams = "[{}]";
		 String title = "Report";
		 model.addAttribute("listViewName",listView);
		 model.addAttribute("container",container);
		 model.addAttribute("listViewParams",listViewParams);
		 model.addAttribute("title",title);*/
    	
    	try{
    		Report report = reportService.getReportById(id);	
    		model.addAttribute("report", report);
    		log.info("Report Edited SuccessFully");
    	}catch(Exception ex){
			log.error(ex.getMessage(),ex);
    	}	
            return new ModelAndView("report/reportForm", model);   
    }
/**
 * 	<p> To show report through Iframe from menu </p>
 * @param reportName
 * @param model
 * @param request
 * @param response
 * @return ModelAndView
 */
@RequestMapping(value = "bpm/report/iframeReportForMenu",method = RequestMethod.GET)
    public ModelAndView iframeReportForMenu(@RequestParam("reportName") String reportName, ModelMap model, HttpServletRequest request,
	           HttpServletResponse response) {
		
			
			String url = null;
    	try{
    		String reportUserName = PropertyReader.getInstance().getPropertyFromFile("String", "report.username");
    		String reportPassword = PropertyReader.getInstance().getPropertyFromFile("Password", "report.jasperPassword");
    		String loginUserName = CommonUtil.getLoggedInUserName();
    		String reportUrl = reportService.getUrlByName(reportName);
    		Report report = reportService.getReportByName(reportName);
    		String serverName = request.getServerName();
    		int serverPort = request.getServerPort();
    		String protocol = request.getScheme();
    		
    		url = protocol+"://"+serverName+":"+serverPort+reportUrl+"&j_username="+reportUserName+"&j_password="+reportPassword+"&bpmusername="+loginUserName+"&decorate=no";
    		model.addAttribute("report", report);
    		model.addAttribute("url", url);
     	}catch(Exception ex){
			log.error(ex.getMessage(),ex);
    	}	
    	 return new ModelAndView("report/reportIframe", model); 
    }
	
	@RequestMapping(value="/bpm/report/showHomepageReport",method = RequestMethod.GET)
	public @ResponseBody String showHomepageReport(@RequestParam("reportName") String reportName,@RequestParam("container") String container, HttpServletRequest request,HttpServletResponse response){
		String script = "";
		try{
			String reportUrl = reportService.getUrlByName(reportName);
			String serverName = "54.251.99.43";//request.getServerName();
    		int serverPort = request.getServerPort();
    		String protocol = request.getScheme();
    		String reportUserName = PropertyReader.getInstance().getPropertyFromFile("String", "report.username");
    		String reportPassword = PropertyReader.getInstance().getPropertyFromFile("Password", "report.jasperPassword");
    		String loginUserName = CommonUtil.getLoggedInUserName();
    		String url = protocol+"://"+serverName+":"+serverPort+reportUrl+"&j_username="+reportUserName+"&j_password="+reportPassword+"&bpmusername="+loginUserName+"&decorate=no";
    		script = "<iframe id='homePageReport' height='"+container.split(",")[0]+"' width='"+container.split(",")[1]+"' src='"+url+"' marginwidth=0 marginheight=0 hspace=0 vspace=0 frameBorder=0 scrolling=no></iframe>";
		}catch(Exception e){
			script = "";
		}
		return script;
	}

	@RequestMapping(value = "bpm/report/iframeReport",method = RequestMethod.GET)
    public ModelAndView iframeReport(@RequestParam("id") String id, ModelMap model, HttpServletRequest request,
	           HttpServletResponse response) {
		
		String url = null;

    	
    	try{
    		String reportUserName = PropertyReader.getInstance().getPropertyFromFile("String", "report.username");
    		String reportPassword = PropertyReader.getInstance().getPropertyFromFile("Password", "report.jasperPassword");
    		
    		String loginUserName = CommonUtil.getLoggedInUserName();
    		String serverName = request.getServerName();
    		int serverPort = request.getServerPort();
    		String protocol = request.getScheme();
    		Report report = reportService.getReportById(id);
    		String reportUrl = report.getReportUrl();
    		url = protocol+"://"+serverName+":"+serverPort+reportUrl+"&j_username="+reportUserName+"&j_password="+reportPassword+"&bpmusername="+loginUserName+"&decorate=no";

    		model.addAttribute("id", id);
    		model.addAttribute("url", url);
     	}catch(Exception ex){
			log.error(ex.getMessage(),ex);
    	}	
            return new ModelAndView("report/reportIframe", model);   
    }
	
	/**
	 * delete the Report.
	 * 
	 * @param reportIds
	 * @param request
	 * 
	 * @return ModelAndView
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "bpm/report/deleteReport", method = RequestMethod.GET)
	public ModelAndView deleteReport(@RequestParam("reportIds") String reportId, HttpServletRequest request,ModelMap model) {
	 Locale locale = request.getLocale();
	 List<Report> reportIdList = new ArrayList<Report>();
	 
	String listView="REPORT_NO_ROLE";
	 String container = "target";
	 String listViewParams = "[{}]";
	 String title = "Manage Reports";
	 model.addAttribute("listViewName",listView);
	 model.addAttribute("container",container);
	 model.addAttribute("listViewParams",listViewParams);
	 model.addAttribute("title",title);
	 
	 if (reportId.contains(",")) {
		  String[] ids = reportId.split(",");
		  for(String id :ids){
			  Report report = reportService.getReportById(id);
			  reportIdList.add(report);
		  }
		} else {
			Report report = reportService.getReportById(reportId);
			reportIdList.add(report);
		}
		try{
			
			boolean isDelete = reportService.deleteReport(reportIdList);
			if(isDelete = true){
				saveMessage(request,"Deleted Successfully");
				log.info("Report Deleted Successfully");
			}else {
				saveMessage(request,"Report not found");
			}
			
		}catch(Exception e){
			log.info("Exception Occured",e);
			saveMessage(request,"Requested Report not available");
		}
		
		
		return new ModelAndView("redirect:/bpm/listView/showListViewGrid");
	}
	

	@RequestMapping(value = "bpm/report/showReportGrid", method = RequestMethod.GET)
	public ModelAndView showReportGrid(HttpServletRequest request,
			HttpServletResponse response) {	
		Model model = new ExtendedModelMap();
		Locale locale = request.getLocale();
		try {
			List<Report> report = reportService.getReports();
			String script = GridUtil.generateScriptForReportGrid(CommonUtil.getMapListFromObjectList(report),velocityEngine);
			log.info(script);
			model.addAttribute("script", script);
			log.info("after addAttribute....");
		} catch (Exception e) {
			log.warn(e.getMessage());
			saveError(request, getText("errors.report.getEvents", locale));
		}
		return new ModelAndView("report/reportGrid", model.asMap());
	}
	/**
	 * <p>To show report list in menu create page</p>
	 * 
	 * @param modelMap
	 * @param selectionType
	 * @param appendTo
	 * @param selectedValues
	 * @param rootNodeURL
	 * @param callAfter
	 * @param user
	 * @param errors
	 * @param request
	 * @param response
	 * @return selected report
	 */
	@RequestMapping(value = "bpm/report/showReportSelection", method = RequestMethod.GET)
	public ModelAndView showReportSelection(ModelMap model,
			@RequestParam("selectionType") String selectionType,
			@RequestParam("appendTo") String appendTo,
			@RequestParam("selectedValues") String selectedValues,
			@RequestParam("rootNodeURL") String rootNodeURL,
			@RequestParam("callAfter") String callAfter, User user,
			BindingResult errors, HttpServletRequest request,
			HttpServletResponse response) {
		JSONArray nodes = new JSONArray();
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			context.put("nodes", nodes);
			context.put("rootNodeURL", rootNodeURL);
			context.put("selectionType", selectionType);
			context.put("selection", "selectedReport");
			context.put("selectedValues", selectedValues);
			context.put("needContextMenu", true);
			context.put("needTreeCheckbox", true);
			String script = VelocityEngineUtils.mergeTemplateIntoString(
					velocityEngine, "organizationTree.vm", context);
			model.addAttribute("script", script);
			model.addAttribute("appendTo", appendTo);
			model.addAttribute("callAfter", callAfter);
			model.addAttribute("selectionType", selectionType);
			model.addAttribute("selectedValues", selectedValues);
			model.addAttribute("selection", "selectedReport");

		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
		return new ModelAndView("admin/organizationTree", model);
	}

	@RequestMapping(value = "bpm/report/getReportRootNodes", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> getReportRootNodes(ModelMap model,
			@RequestParam("currentNode") String currentNode,
			@RequestParam("rootNode") String rootNode,
			@RequestParam("nodeLevel") int nodeLevel) {
		List<Map<String, Object>> nodeListOfMap = new ArrayList<Map<String, Object>>();
	
		List<LabelValue> reportList = reportService.getAllReports();

		try {
			for (LabelValue report : reportList) {
				CommonUtil.createReportRootTreeNode(nodeListOfMap,
						report.getLabel(), report.getValue(),
						report.getLabel());
			}
		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
		return nodeListOfMap;
	}
    

}
