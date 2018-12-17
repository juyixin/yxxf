package com.eazytec.bpm.metadata.process.service;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.repository.ProcessDefinition;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p></p>
 * 
 * @author Rajasekar
 *
 */
public interface ProcessModelerService {
	
	boolean createProcess(HttpServletRequest request,String processName,String classificationId,Integer orderNo,ServletContext getServletContext)throws Exception;
	
	List<ProcessDefinition> getAllProcessDefinitionVersions(String processName);
	
	JSONObject editProcess(HttpServletRequest request)throws JSONException;
	
	List<String> userNameCheck(String strUserList)throws Exception;
	
	List<Map<String,String>> userNameCheckFullName(String strUserList)throws Exception;
	
	String showXMLSource(HttpServletRequest request,boolean asXML,String processName,String jsonData,String svgDOM,boolean isProcessCreate)throws Exception;
}
