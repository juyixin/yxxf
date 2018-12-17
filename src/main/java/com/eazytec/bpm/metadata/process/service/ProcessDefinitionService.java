package com.eazytec.bpm.metadata.process.service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.OperatingFunctionAudit;
import com.eazytec.model.User;

/**
 * <p>Process can be serviced differently at metadata or definition time, this class goes for static metadata
 * related service calls, getting just info and CRUDs</p>
 * 
 * @author Karthick
 * @author madan 
 *
 */

public interface ProcessDefinitionService {

	/** 
	 * Show list of process for who is logged in and type
	 * @param loggedInUsername of current logged in user, cannot be null
	 * @throws Exception 
	 */
	List<ProcessDefinition> getProcessesDefinitionsByUser(User user) throws BpmException;
	
	
	/** 
	 * Show list of process for who is logged in and type
	 * @param loggedInUsername of current logged in user, cannot be null
	 * @throws Exception 
	 */
	List<ProcessDefinition> getProcessesDefinitions() throws BpmException;
	
	
	List<ProcessDefinition> getProcessesDefinitionsForView() throws BpmException;
	
	/** 
	 * Starts creating a new deployment 
	 * @param filePath of BPMN source file, cannot be null
	 * @throws Exception when file is not found in filepath
	 */
	boolean deployBPMN(String deploymentName, String xmlfilePath, String imgFilePath) throws	BpmException, FileNotFoundException;
	
	/** 
	 * Starts creating a new deployment 
	 * @param filePath of BPMN source file, cannot be null
	 * @param isSystemDefined TODO
	 * @throws Exception when file is not found in filepath
	 */
	boolean deployBPMNSourceFile(String fileName,String filePath,String description,String classificationId, boolean isSystemDefined) throws	BpmException, FileNotFoundException;
	
	/** 
	 * Starts creating a new deployment 
	 * @param deployName the name for a deployment
	 * @param fileName the file being deployed as bpmn
	 * @param filePath of BPMN source file, cannot be null
	 * @param isSystemDefined TODO
	 * @throws Exception when file is not found in filepath
	 */
	boolean deployBPMNSourceFile(String deployName, String fileName,String filePath,String description,String classificationId, boolean isSystemDefined) throws	BpmException, FileNotFoundException;
		
	/**
	 * Gets the process definition that was deployed, by id
	 * 
	 * @param processDefinitionId
	 * 
	 */
	ProcessDefinition getProcessDefinitionById(String processDefinitionId);	
	
	/**
	 * Gets the process definition that was deployed, by id
	 * 
	 * @param processDefinitionId
	 * 
	 */
	List<Map<String,String>> getListProcessDefinitionById(String processDefinitionId);
	
	/**
	 * Gets the process definition that was deployed, by id
	 * 
	 * @param processDefinitionId
	 * 
	 */
	List<Map<String,String>> getListProcessDefinitionByIdJump(String processDefinitionId,String activityId,String jumpType,String processInstanceId,String executionId);
	
	 /**
	   * Suspends the process definition with the given id. 
	   * 
	   * If a process definition is in state suspended, it will not be possible to start new process instances
	   * based on the process definition.
	   * 
	   * <strong>Note: all the process instances of the process definition will still be active 
	   * (ie. not suspended)!</strong>
	   * 
	   *  @throws BpmException if no such processDefinition can be found or if the process definition is already in state suspended.
	   */
	 void suspendProcessDefinitionById(String processDefinitionId) throws BpmException;
	  
	 /**
	   * Suspends the <strong>all<strong> process definitions with the given key (= id in the bpmn20.xml file). 
	   * 
	   * If a process definition is in state suspended, it will not be possible to start new process instances
	   * based on the process definition.
	   * 
	   * <strong>Note: all the process instances of the process definition will still be active 
	   * (ie. not suspended)!</strong>
	   * 
	   *  @throws BpmException if no such processDefinition can be found or if the process definition is already in state suspended.
	   */
	  void suspendProcessDefinitionByKey(String processDefinitionKey) throws BpmException;
	  
	  /**
	   * <p>
	   * Retrieves <strong> all versions of the process definitions </strong> related to the particular process key
	   * </p>
	   * @param processKey	
	   * 		process key to search for
	   * @return list of process defintions 
	   * @throws EazyBpmException
	   */
	  List<ProcessDefinition> getAllProcessDefinitionVersions(String processKey) throws EazyBpmException;
	  
	  /**
	   * <p>
	   * Retrieves <strong> all versions of the process definitions </strong> 
	   * </p>
	   * @param processDefinitionKeys	
	   * 		processDefinitionKeys to search for
	   * @return list of process definitions 
	   * @throws EazyBpmException
	   */
	  
	  List<ProcessDefinition> getAllProcessDefinitionByKeys(List<String> processDefinitionIds) throws EazyBpmException;
	  
	  /** Starts creating a new deployment */
/*	  public Deployment deploy(DeploymentBuilderImpl deploymentBuilder) throws	BpmException, FileNotFoundException;  
*/	
	  
	 /**
	  * <p>Gets the process entities as entity details with all the entities resolve to get monitor grid described.</p> 
	  * @param processDefinitions
	  * @return
	  * @throws BpmException
	  */
	 List<Map<String, Object>> resolveProcessDefinitions(List<ProcessDefinition>  processDefinitions) throws BpmException;
	 
	 /**
	  * <p>Gets the process entities as entity details with all the entities resolve to get monitor grid described for logged in user.</p> 
	  * @param processDefinitions
	  * @return
	  * @throws BpmException
	  */
	 List<Map<String, Object>> resolveMyMonitorProcesses(List<ProcessDefinition>  processDefinitions, String loggedInUser) throws BpmException;
	 
	 /**
	 * <p>Deletes a process definition by id, so if we pass a process definition id, it includes
	 * versioning of that definition, so only that particular version of the process definition is
	 * deleted, since the id contains info about only one version.</p>
	 * 
	 * @param id the id of the definition, for one version
	 * @param cascade should all the referring instances and other entities should also be deleted
	 * @return
	 * @throws BpmException
	 */
	 boolean deleteProcessDefinitionById(String id, boolean cascade)throws BpmException;
	 
	 /**
	  * <p>Deletes all process definitions, irrespective of versioning, all the versions will
	  * be deleted.</p>
	  * 
	  * @param processDefinitions list of definitions
	  * @param cascade should all the related instances should be deleted or not
	  * @return
	  * @throws BpmException
	  */
	 boolean deleteProcessDefinitions(List<ProcessDefinition>processDefinitions, boolean cascade)throws BpmException;
	 
	 /**
	 * Gets all process definition that was deployed, by id
	 * 
	 * @param processDefinitionId
	 * 
	 */
	 List<ProcessDefinition> getAllProcessesDefinitionsByUser(User user) throws BpmException;
	 
	 /***
	  * To get Process Creates Date for Deployment Id 
	  * @param deploymentId
	  * @return
	  * @throws BpmException
	  */
	 String getProcessCreatedDateForDeplymentId(String deploymentId) throws BpmException;
	 
	 /**
	  * To Activate(Make as Currently Active version) the process Definition by processDefinitionId
	  * Which make other all other active version as Inactive.
	  * @param processDefinitionId
	  * @throws BpmException
	  */
	 void activateProcessDefinitionVersionById(String processDefinitionId) throws BpmException;
	 
	 /**
	  * To In-activate the process Definition by processDefinitionId
	  * Only One version of the process should be active at a time
	  * @param processDefinitionId
	  * @throws BpmException
	  */
	 void inActivateProcessDefinitionVersionById(String processDefinitionId) throws BpmException;
	 
	 /** 
		 * Starts creating a new deployment 
		 * @param deployName the name for a deployment
		 * @param fileName the file being deployed as bpmn
		 * @param filePath of BPMN source file, cannot be null
		 * @param description
		 * @param jsonString
		 * @throws Exception when file is not found in filepath
		 */
	 boolean deployBPMNSourceFileWithJson(String fileName,String filePath,String description,String classification, String jsonString) throws	BpmException, FileNotFoundException;
	 
	 /** 
		 * Starts creating a new deployment 
		 * @param deployName the name for a deployment
		 * @param fileName the file being deployed as bpmn
		 * @param filePath of BPMN source file, cannot be null
		 * @param description
		 * @param jsonString
		 * @param svgString
		 * @throws Exception when file is not found in filepath
		 */
	 boolean deployBPMNSourceFileWithJson(String fileName,String filePath,String description,String classification, Integer orderNo, String jsonString,String svgString) throws	BpmException, FileNotFoundException;
	 
	 /**
	  * Gets the active and active version for the given process name
	  * @param name process def name
	  * @return process definition
	  * @throws BpmException
	  */
	 ProcessDefinition getActiveProcessesDefinitionByName(String name) throws BpmException;
	 
	 /**
	  * 
	  * @param strUserList
	  * @throws BpmException
	  */
	 List<String> userNameCheck(String strUserList) throws BpmException;
	 
	 
	 /**
	  * Returns User FullName
	  * @param strUserList
	  * @throws BpmException
	  */
 	 List<Map<String,String>> userNameCheckFullName(String strUserList)throws Exception;

	 
	 /**
	  * 
	  * @param operFunAudit
	  * @throws BpmException
	  */
	 OperatingFunctionAudit saveOperatingFunctionAudit(OperatingFunctionAudit operFunAudit)throws BpmException;


	List<Map<String, String>> getProcessDefinitionForBackOffJump(
			String processDefinitionId, String activityId, String jumpType,
			String processInstanceId);


	boolean checkIsNextTaskCompleted(HistoricTaskInstance task);
}
