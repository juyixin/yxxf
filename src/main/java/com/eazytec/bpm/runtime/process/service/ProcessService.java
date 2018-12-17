package com.eazytec.bpm.runtime.process.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.persistence.entity.ResourceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.velocity.app.VelocityEngine;
import org.jdom.JDOMException;
import org.springframework.ui.ModelMap;

import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Classification;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Process;

/**
 * <p> The representation of process during runtime and all the functional blocks
 * related to process as an entity during runtime like execution of actual flow, instantiating 
 * at runtime etc. Runtime here refers to the execution of workflow that is already defined. </p>
 * 
 * @author madan
 * @author Revathi
 */
public interface ProcessService {	
	
	/** 
	 * <p>Starts a new process instance in the latest version of the process definition with the given key.</p>
	 * @param processDefinitionKey key of process definition, cannot be null.
	 * @throws Exception when no process definition is deployed with the given key.
	 */
	boolean startProcessInstanceByKey(String processDefinitionKey) throws BpmException;	
	
	/**
	 * <p>Gets the start form if any, associated with the process as the first step</p>
	 * @param processDefinitionId for the process deployed
	 * @return the start form data
	 * @throws BpmException
	 */
	StartFormData getStartForm(String processDefinitionId) throws BpmException;
	
	/**
	 * <p>Starts the process, instantiates the process as a sign of kick-off by creating all the runtime 
	 * execution variable entities.</p>
	 * @param processDefinitionId for the process deployed
	 * @return the process instance created
	 */
	ProcessInstance startProcessInstanceById(String processDefinitionId);
	
	/** 
	 * Gets list of running process instances for who is logged in and type for manage
	 * @param loggedInUsername of current logged in user, cannot be null
	 * @throws Exception 
	 */
	List<HistoricProcessInstance> getProcessIntanceByUser(String userId) throws BpmException;
	
	/** 
	 * Show list of running process for admin to manage 
	 * @param 
	 * @throws Exception 
	 */
	List<HistoricProcessInstance> getAllProcessIntance() throws BpmException;
	
	
	
	
	/**
	 * Suspend the process for given processDefinitionId
	 * @param processDefinitionId
	 * @param suspendProcessInstances
	 * @param suspensionDate
	 * @throws BpmException
	 */
	void suspendProcessDefinitionById ( String processDefinitionId,boolean suspendProcessInstances,Date suspensionDate);
	/**
	 * Suspend the process for given List of processDefinition Id
	 * @param suspendProcessInstances
	 * @param suspensionDate
	 * @param ids
	 */
	
	void suspendProcessDefinitionByIds (boolean suspendProcessInstances,Date suspensionDate,List<String> ids);
	/**
	 * Activate the process for given processDefinitionId
	 * @param processDefinitionId
	 * @param suspendProcessInstances
	 * @param suspensionDate
	 * @return
	 * @throws BpmException
	 */
	void activateProcessDefinitionById ( String processDefinitionId,boolean suspendProcessInstances,Date suspensionDate);
	/**
	 * Activate the process for given List of processDefinitionIds
	 * @param processDefinitionId
	 * @param suspendProcessInstances
	 * @param suspensionDate
	 * @return
	 * @throws BpmException
	 */
	
	void activateProcessDefinitionByIds (boolean suspendProcessInstances,Date suspensionDate,List<String> ids);
	
	/**
	 * Resolves the process instancess as key-value details
	 * @param historicProcessInstanceList list of instances
	 * @return the key-value pair list
	 */
	List<Map<String, Object>>  resolveHistoricProcessInstance(List<HistoricProcessInstance>  historicProcessInstanceList, String processGridType);
	
	/**
	 * Gets the variable details for a process instance
	 * @param processInstanceId for the process instance
	 * @return the variable details
	 */
	Map<String, Object>getVariablesForInstance(String processInstanceId);
	
	/**
	 * Gets the process instance from the id
	 * @param processInstanceId
	 * @return the process instance
	 */
	List<HistoricProcessInstance> getProcessInstance(String processInstanceId);
	
	/**
	 * Delete process instance for given id
	 * @param processDefinitionId
	 * @param deleteReason
	 * @return
	 */
	void deleteProcessInstance(String processInstanceId,List<String> processInstanceIds, String deleteReason);
	
	/**
	 * Get all process instance status for given process definition id
	 * @param processDefinitionId
	 * @return
	 */
	Map<String,Object> getAllProcessInstance(String processDefinitionId) throws BpmException;
	
	
	/**
	 * Get all logged in user process instance status for given process definition id
	 * @param processDefinitionId
	 * @return
	 */
	
	Map<String,Object> getAllMyProcessInstance(String processDefinitionId,String loggedInUser) throws BpmException;
	
	/**
	 * <p>Deletes the process definition entity and clears the local refrence to it, like
	 * its entries in cache etc</p>
	 * 
	 * @param processDefinitionId
	 * @param cascade should referenced instances should also be deleted
	 * @return
	 * @throws BpmException
	 */
	boolean deleteProcessDefinition(String processDefinitionId, boolean cascade)throws BpmException;
	
	/**
	 * Deletes all the versions of the process definitions
	 * 
	 * @param key referring commonly for all definition versions
	 * @param cascade should all their corresponding instances should be deleted
	 * @return
	 * @throws BpmException
	 */
	boolean deleteAllProcessDefinitionVersions(String key, boolean cascade)throws BpmException;	
	
	/**
	 * Deletes all the process definitions for given processDefinitionIds
	 * 
	 * @param processDefinitionIds referring commonly for all definition and their versions
	 * @param cascade should all their corresponding instances should be deleted
	 * @return
	 * @throws BpmException
	 */
	
	boolean deleteAllProcessDefinition(List<String> processDefinitionKeys, boolean cascade)throws BpmException;
	
	 /**
     * Get all Process instance for given processDefinitionId and type
     * @param processDefinitionId
     * @param type
     * @return
     */
    List<HistoricProcessInstance> getAllProcessInstnaceByStauts(String processDefinitionId,String status)throws BpmException;
    
    /**
     * Get all logged in user Process instance for given processDefinitionId and type
     * @param processDefinitionId
     * @param type
     * @return
     */
    List<HistoricProcessInstance> getAllMyProcessInstnaceByStauts(String processDefinitionId,String status,String loggedInUser)throws BpmException;
    
    /**
     * Get process definition xml InputStream
     * @param deploymentId
     * @param resourceName
     * @return
     */
    
    InputStream getResourceAsStream(String deploymentId, String resourceName);
    
    
    /**
     * Get process definition svgString 
     * @param deploymentId
     * @param resourceName
     * @return
     */
    
    String getSvgString(String deploymentId, String resourceName);
    
    
    /**
     * Get Map of process definition xml InputStream
     * @param deploymentIds
     * @return
     */
    
    Map<String,Map<String,InputStream>> getResourceListAsStream(List<String> deploymentIds);
    
    /**
     * Terminate the process instance
     * @param processInstanceId
     */
    void terminateExecutionByInstanceId(String processInstanceId,Date currentDate);
    
    /**
     * suspend the process instance by id
     * @param id
     */
    void suspendProcessInstanceById(String id);
    
    /**
     * activate the process instance by id
     * @param id
     */
    void activateProcessInatanceById(String id);
    
    /**
     * retrieves the resource as entity
     * @param deploymentId
     * @param resourceName
     * @return
     */
    ResourceEntity getResourceAsEntity(String deploymentId, String resourceName);
    
    List<FormProperty> getTaskFormValuesByTaskId(String processInstanceId)throws BpmException;
    
    
    List<Classification> getClassifications();
    
    Classification getClassificationById(String id);
    
    void removeClassifications(List<String> cidList, HttpServletRequest request);
    
    boolean updateClassificationOrderNos(List<Map<String, Object>> classificationList, HttpServletRequest request);
    
    /**
     * Save the classification entity details
     * @param classification
     * @return
     */
    Classification saveClassification(Classification classification, HttpServletRequest request) throws BpmException;
    
    /**
     * To validate whether the give Classification is exist or new
     * @param classification
     * @return
     * @throws BpmException
     */
    boolean isClassificationExist(String classification) throws BpmException;
    
    /**
     * Get All process list
     * @return
     * @throws BpmException
     */
    //List<Process> getAllProcessList() throws BpmException;
    
    /**
     * Get process list for given list of ids
     * @param ids
     * @return
     * @throws EazyBpmException
     */
    List<Process> getProcessByIds(List<String> ids)throws EazyBpmException;
    
    Map<String, Object>getHistoricVariablesForInstance(String processInstanceId);
    
    List<Object[]> getNewsItemForHomePage();
    
    Map<String, Object>getPrevHistoricVariablesForInstance(String processInstanceId, String currentTaskId)throws BpmException;
    
    /**
     * Gets all the instances from the history for the process
     * @param processDefinitionIds for the processes
     * @return list of instances
     */
    List<HistoricProcessInstance> getHistoricProcessInstancesByDefIds(List<String> processDefinitionId);
    
    /**
     * Gets all process definitions under the def name
     * @param name process definition name
     * @return all the definitions which are active, but all versions
     */
    List<ProcessDefinition> getProcessDefinitionsByName(String name);
    
    /**
     * Gets all process definition ids under the def name
     * @param name process definition name
     * @return all the definition ids which are active, but all versions
     */
    List<String> getProcessDefinitionIdsByName(String name) throws BpmException;
    
    Map<String, List<HistoricVariableInstance>>getAllVariableValuesByProcess(String processName)throws BpmException;
    
    StartFormData getStartFormData(String processDefinitionId)throws BpmException;
    
    Map<String, Object> designStartProcessScript(String processId, VelocityEngine velocityEngine, String formAction,boolean isSystemDefined,String processInstanceId,boolean isFromDoneList)throws BpmException;
    
    Map<String, Object> designStartProcessScript(ProcessDefinition processDefinition, VelocityEngine velocityEngine, String formAction,boolean isSytemDefined,String processInstanceId,boolean isFromDoneList, Map<String, Object> permissionValues)throws BpmException;
    /**
     * Return All Process list LabelValue bean
     * @return
     * @throws BpmException
     */
    List<LabelValue> getAllProcessAsLabelValue() throws BpmException;
    
    /**
     * To search process 
     * @param processName
     * @return
     */
    List<LabelValue> searchProcessNames(String processName)throws BpmException;
    
    Map<String, Object> designUpdateStartProcessScript(String processId, Map<String, Object>formValues, VelocityEngine velocityEngine)throws BpmException;

    /**
     * <p>Generates the context required for showing a form in update mode for a previous 
     * task which has already been completed</p>
     * 
     * @param currentTask TaskEntity at which the process is currently running
     * @param task HistoricTask, the previous or older task for which form update needed
     * @param formValues the form values that were submitted at the time the older task was completed
     * @param velocityEngine
     * @param formAction where the form should be submitted if ewe update form on screen
     * @param isStartNodeTask TODO
     * @param creator TODO
     * @return the context required for generating script with template merge
     * @throws BpmException
     * 
     * @author madan
     */
    Map<String, Object> designUpdateTaskProcessScriptContext(Task currentTask, HistoricTaskInstance task, Map<String, Object>formValues,ProcessDefinition processDefinition, VelocityEngine velocityEngine, String formAction,ModelMap model, boolean isStartNodeTask, String creator)throws BpmException;

    Map<String, Object>getHistoricVariablesForInstance(String processInstanceId, String taskId)throws BpmException;
    
    /**
     * Get All Process Instance For All Users
     * @return
     * @throws BpmException
     */
	List<Map<String, Object>> getAllProcessIntanceCountForFlowStatistics(Map<String, Object> whereParams) throws BpmException, ParseException;
	
	Task setTaskDefinition(Task task);

	String getClassificationId(String deploymentId);
	
	Map<String, Object> designTaskProcessScriptContextForReader( HistoricTaskInstance task, Map<String, Object>formValues,ProcessDefinition processDefinition, 
			VelocityEngine velocityEngine, String formAction,boolean isRead,ModelMap model)throws BpmException;
	
	void terminateExecutionsByInstanceIds(List<String> processInstanceIds,Date currentDate);
	
	/**
	 * 
	 * @param processInstance
	 * @param processDefinition
	 * @return
	 * @throws BpmException
	 * @throws JDOMException 
	 * @throws IOException 
	 */
	void designArchivedProcessForm(TaskEntity task,String resourcePath,String stylePath, String scriptPath)throws BpmException, EazyBpmException, JDOMException, IOException, JDOMException, IOException;
	
	List<HistoricProcessInstance> getMyDocumentProcessIntanceByUser(String loggedInUser) throws BpmException;
	
	/**
	 * get all process based on system defined attributes
	 * @param isSystemDefined
	 * @return
	 * @throws Exception
	 */
	List<LabelValue> getAllProcessBySystemDefined(boolean isSystemDefined) throws Exception;

	ModelMap setEndEventStartScript(ModelMap model,Map<String, String> endEventScript);

	ModelMap setEndEventScript(ModelMap model,Map<String, String> endEventScript);
	
	void designWorkflowInstanceExport(List<Map<String,Object>> processDetailsMap,boolean withTrace,HttpServletResponse response,String stylePath,String scriptPath)throws BpmException, EazyBpmException, JDOMException, IOException;
	
	public boolean deleteAllSelectedProcessDefinition(List<String> processDefinitionIds, boolean cascade)throws BpmException;

	ModelMap setStartEventScriptForProcessInstance(TaskEntity task,ModelMap model);
	
	/**
	 * Get all active process List and return
	 * @return
	 */
	List<Process> getAllProcess();

	String designDefaultForm(ProcessDefinition defaultProcessDefinition,
			Map<String, Object> context);

	ProcessDefinition getDefaultProcessDefinition(String formName);

	public String designEditDefaultProcessScript(ProcessDefinition newsDefinition,
			String newsInstanceId, Map<String, Object> context);

	void updateDefaultProcess(Map<String, String> rtFormValues,
			Map<String, String[]> rtSubFormValues, Map<String, byte[]> files,
			Map<String, String> filePathsMap);
}
