package com.eazytec.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.bpm.common.Messages;
import com.eazytec.bpm.common.util.I18nUtil;
import com.eazytec.bpm.metadata.process.service.ProcessModelerService;

/**
 * 
 * @author Rajasekar
 * @author Karthick
 * Performs creation of process diagram through the modeler,save the diagram 
 * 
 * 
 */

@Controller
public class ProcessModelerController extends BaseFormController{

	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    
//	public static final String oryx_path = "/oryx/";
	
	private ProcessModelerService processModelerService;
    
	@Autowired
	public void setProcessModelerService(ProcessModelerService processModelerService) {
		this.processModelerService = processModelerService;
	}
	
	/**
	 * To show the form bulider, to design
	 * the form by drag and drop the fields
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/process/processEditor",method = RequestMethod.GET)
    public ModelAndView showFormBuilder(ModelMap model) {
        return new ModelAndView("process/processIndex",model);
    }
	
	
	/**
	 * To show the form bulider, to design
	 * the form by drag and drop the fields
	 * @param model
	 * @return
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "bpm/process/processCreate",method = RequestMethod.POST)
    public @ResponseBody String createProcess(HttpServletRequest request) {
		
		String processName = request.getParameter("title");
		String classificationId = request.getParameter("classification_type");
		classificationId = classificationId.trim().replaceAll("\\s+", " ");
		Integer orderNo = Integer.valueOf(request.getParameter("orderNo"));
		String designMode = request.getParameter("designMode");
		StringBuffer responseMsg = new StringBuffer();
		List<ProcessDefinition> existingProcessDefn = processModelerService.getAllProcessDefinitionVersions(processName.toLowerCase()); 
		if(existingProcessDefn != null && !existingProcessDefn.isEmpty() && !designMode.equalsIgnoreCase("edit")){
			responseMsg.append(I18nUtil.getMessageProperty(Messages.PROCESS_NAME_EXISTS));
			return responseMsg.toString();
		}
		try {
			boolean isDeployed = processModelerService.createProcess(request,processName,classificationId,orderNo,getServletContext()); 
			if(isDeployed){
				responseMsg.append("success");
			}
		} catch (Exception e) {
			log.error("Error while creating process  : "+e.getMessage(),e);
			responseMsg.append(e.getMessage());
		} finally {
			MDC.remove("processName");
			return responseMsg.toString();
		}
    }
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/process/editProcess",method = RequestMethod.POST)
    public @ResponseBody Map editProcess(HttpServletRequest request) {
		
		JSONObject result = new JSONObject();
		Map<String,String> resultMap = new HashMap<String, String>();
		
		try {
			result = processModelerService.editProcess(request);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		resultMap.put("jsonRes", result.toString());
		
		
		return resultMap;
	}
	
	/**
     * 
     * @param request
     * @param strUserList
     * @return
     */
	@RequestMapping(value = "/process/userNameCheck", method = RequestMethod.GET)
    public @ResponseBody List<String> userNameCheck(HttpServletRequest request,@RequestParam("strUserList") String strUserList) {
    	
		List<String> resUserList = new ArrayList<String>();
    	
        try {
        	
        	resUserList =  processModelerService.userNameCheck(strUserList);
        	
        } catch (Exception e) {
        	log.error("Error while getting all Classifications  : "+e.getMessage(),e);
        }
        
        
        return resUserList;
    }
	
	@RequestMapping(value = "/process/userNameCheckFullName", method = RequestMethod.GET)
    public @ResponseBody Map<String,List<Map<String,String>>> userNameCheckFullName(HttpServletRequest request,@RequestParam("strUserList") String strUserList) {
    	
		Map<String,List<Map<String,String>>> resUserList = new HashMap<String,List<Map<String,String>>>();
    	
        try {
        	
        	resUserList.put("userFullName", processModelerService.userNameCheckFullName(strUserList));
        	
        } catch (Exception e) {
        	log.error("Error while getting all Classifications  : "+e.getMessage(),e);
        }
        
        
        return resUserList;
    }

	/**
	 * To show the form builder, to design
	 * the form by drag and drop the fields
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/process/showProcessXML",method = RequestMethod.POST)
    public @ResponseBody Map showXML(HttpServletRequest request) {
		
		log.info("------------------------------------");
		log.info("show Process xml...");
		log.info("------------------------------------");
		String processName = request.getParameter("title");
		String bpmnXML = "";
		Map<String,String> responseMap = new HashMap<String, String>();
		
		boolean asXML = request.getParameter("xml") != null;
		
		try {
			
			String jsonData = request.getParameter("data");
			String svgDOM = new String(request.getParameter("svg").getBytes("UTF-8"));
			
			bpmnXML = processModelerService.showXMLSource(request,asXML,processName,jsonData,svgDOM,false);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error while create process e : "+e.getMessage(),e);
		}
		
		responseMap.put("xml", bpmnXML);
		
		return responseMap;
	}
	
}
