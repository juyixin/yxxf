package com.eazytec.bpm.runtime.form.service;

import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.json.JSONArray;

import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceException;


/**
 * <p>Form specific service calls related to runtime. A form can be used differently at runtime apart
 * from definition time, like form submission, form entity creation, form linkage to a task etc</p> 
 * 
 * @author madan
 */

public interface FormService {	
	
	/**
	 * Gets the form runtime values and submits the form data to the BPM engine, thereby all the runtime
	 * instances are created
	 * @param processDefinitionId for the process that is being started
	 * @param formValues the key-value runtime values of form
	 */
	ProcessInstance submitStartForm(String processDefinitionId, Map<String, String>formValues)throws DataSourceException;
	
	ProcessInstance submitStartForm(String processDefinitionId, Map<String, String>formValues, Map<String, String[]>subFormValues, Map<String, byte[]>files, Map<String, String>filePaths,boolean isDraft)throws DataSourceException;
	
	/**
	 * Gets the form runtime values and submits the form data to the BPM engine, thereby all the runtime
	 * instances are created
	 * @param taskId for the task that is associated with the form
	 * @param formValues the key-value runtime values of form
	 */
	void submitTaskForm(String taskId, Map<String, String>formValues)throws DataSourceException;
	
	Object submitTaskForm(String taskId, Map<String, String>formValues, Map<String, String[]>subFormValues, Map<String, byte[]>files, Map<String, String>filePaths, boolean isStartNodeTask, String proInsId)throws DataSourceException;
	
	void saveTaskForm(String taskId, Map<String, String>formValues, Map<String, String[]>subFormValues, Map<String, byte[]>files, Map<String, String>filePaths, String loggedInUserId, String proInsId)throws DataSourceException;
	
	ProcessInstance updateStartForm(String processDefinitionId, String processInstanceId, Map<String, String>formValues, Map<String, String[]>subFormValues, Map<String, byte[]>files, Map<String, String>filePaths)throws DataSourceException;
	
	Object updateTaskForm(String taskId, Map<String, String>formValues, Map<String, String[]>subFormValues, Map<String, byte[]>files, Map<String, String>filePaths, boolean isSignOff)throws DataSourceException;
	
	JSONArray getFormFieldTraceData(String processInstanceId,String formId) throws BpmException;
}
