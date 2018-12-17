package com.eazytec.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.velocity.app.VelocityEngine;

import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.MetaForm;

/**
 * <p> The representation of process during runtime and all the functional blocks
 * related to process as an entity during runtime like execution of actual flow, instantiating 
 * at runtime etc. Runtime here refers to the execution of workflow that is already defined. </p>
 * 
 * @author madan
 * @author Revathi
 */

public interface NewsService {	
	
	/**
	 * Gets the input values for all the news items created so far
	 * @return
	 */
	List<Map<String, Object>> getNewsItems();
	
	/**
	 * Gets the all the news items created so far
	 * @return
	 */
	List<Map<String, Object>> getNewsItemForHomePage() throws Exception;
	
	/**
	 * Gets all the input values for the news instance created
	 * @param newsInstanceId
	 * @return
	 */
	Map<String, Object> getNewsItemValues(String newsInstanceId);
	
	/**
	 * Gets the form associated with the news definition
	 * @param newsDefinitionId
	 * @return
	 */
	MetaForm getNewsForm(String newsDefinitionId);
	
	/**
	 * Gets the latest news process definition that is active and deployed
	 * @return the news, process definition
	 * @throws BpmException
	 */
	ProcessDefinition getNewsDefinition()throws BpmException;
	
	void updateNewsItem(Map<String, String>newsValues, Map<String, String[]>subFormValues, Map<String, byte[]>files, Map<String, String>filePaths)throws DataSourceException;
	
	Map<String, Object> designStartProcessScript(ProcessDefinition newsDefinition, VelocityEngine velocityEngine, Map<String, Object> permissionVal)throws BpmException;

	ProcessInstance submitStartForm(String processDefinitionId, Map<String, String>formValues, Map<String, String[]>subFormValues, Map<String, byte[]>files, Map<String, String>filePaths)throws DataSourceException;
}
