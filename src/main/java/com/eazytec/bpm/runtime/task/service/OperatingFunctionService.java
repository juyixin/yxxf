package com.eazytec.bpm.runtime.task.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.task.TaskDefinition;
import org.json.JSONException;

import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceException;
import com.eazytec.model.AgencySetting;
import com.eazytec.model.User;


/**
 * <p>  </p>
 * 
 * @author madan 
 */

public interface OperatingFunctionService {
	
	//public void add(String taskId, List<String> userIds, String identityLinkType);
	
	void add(String taskId, String userIds, String identityLinkType,String addedOrganizerOrders,String processDefinitionId);
	
	void transfer(String taskId, String userId, String processDefinitionId);
	
	void confluentSignature(String taskId, List<String> userIds,String processDefinitionId) throws JSONException;
	
	void circulatePerusal(String taskId, List<String> userIds,String processDefinitionId);
	
	void replaceTransactor(String taskId, String userId,String processDefinitionId);
	
	void suspend(String processInstanceId,String userId,String taskId,String resourceName, String processDefinitionId);
	
	void terminate(String executionId);
	
	/**
     * Withdraw the instance if not needed to continue again
     * @param processInstanceId
     * @return
     */    
	void withdraw(String processInstanceId);  
	
	//void setJointConduction(String processInstanceId, String nextTaskOrganizers, String nextTaskCoordinators, boolean isStartProcess);
	
	//void addProcessCreator(String processInstanceId,String userId);
	
	TaskEntity setJointConductionByOrder(String processInstanceId, String nextTaskOrganizers, String nextTaskCoordinators,String nextOrganizerOrders,boolean isStartProcess, List<Map<String, Object>> referenceRelation, boolean sendMail, boolean sendInternalMessage) throws JSONException;
	/**
	 * Get dynamic users from form values and set the organizer to the list 
	 * @param organizerList
	 * @param rtFormValues
	 * @param isStartForm TODO
	 * @param taskDefinition
	 * @return
	 */
	List<Map<String,String>> getDynamicOrganizerValue(List<Map<String,String>> organizerList,Map<String, Object> rtFormValues,TaskDefinition taskDefinition, boolean isStartForm,List<String> transactorIds);
	
	/**
	 * get the user id's for given department id's
	 * @param depIds
	 * @return
	 */
	String getUserIdForDepartment(String depIds);

	void terminateTasks(List<Map<String,Object>> taskDetailsMap);
	
	/**
	 * Set dynamic reader to next task
	 * @param taskEntity
	 * @param rtFormValues
	 * @param proInsId
	 * @throws BpmException
	 */
	void setDynamicReaderValue(TaskEntity taskEntity,Map<String, String> rtFormValues,String proInsId,Map<String,String> nodeProperties) throws BpmException;
	
	//void setAutomaticRemainderValue(TaskEntity taskEntity,List<Map<String,String>> organizerDetails,HttpServletRequest request) throws BpmException;
	
	/**
	 * 
	 * @param rtFormValues
	 * @return
	 * @throws DataSourceException
	 */
	List<Map<String,String>> getOrganizersForCurrentTask(Map<String, Object> rtFormValues);
	
	/**
	 * 
	 * @param taskId
	 * @param activityId
	 * @return
	 */
	List<Map<String,String>> getOrganizersForJumpTask(String taskId,String activityId,TaskDefinition taskDefinition);
	
	/**
	 * 
	 * @param processDefinitionId
	 * @param formValues
	 * @return
	 * @throws DataSourceException
	 */
	List<Map<String,String>> getOrganizersForProcessStart(String processDefinitionId,Map<String, Object> formValues);
	
	/**
	 * Delete ProcessInstance Only
	 * @param processInstanceId
	 */
	void delete(String processInstanceId,String taskStatus)throws ActivitiException;
	
	void attachmentOperation(Map<String, String> rtFormValues,String taskId) throws BpmException;

	String getOrganizersByTaskId(String taskId);
	
	/**
	 * getting organizer by taskid and sign off type
	 * @param taskId
	 * @return
	 */
	String getOrganizersByTaskIdAndSignOffType(String taskId,int signOffType);

	void sendNotification(String commaSeparatedUser,String message,String commaSeparatedNotificationType,String subject);
	
	/**
	 * Getting workflow admin for task by task id
	 * @param taskId
	 * @return admins as comma seperated string
	 */
	String getWorkFlowAdminByTaskId(String taskId);
	
	void taskReturn(String userId,String taskId,String resourceName,String returnType,String processInstanceId, String processDefinitionId);
	
	void taskJump(String taskId,String activityId,String nodeType,String jumpTypeStr,String resourceName,String userId,String nextTaskOrganizers,String nextTaskCoordinators,String nextTaskOrganizerOrders, String processDefinitionId);
	
	void setJointConductionForAutomaticJump(String processInstanceId,String taskId, TaskDefinition taskDefinition, Map<String,AgencySetting> agencyMap);
	
	/**
	 * To replace the process details 
	 * @param urgeMessage
	 * @param processDefinitionId
	 * @param taskId
	 * @param workFlowAdmins 
	 * @return
	 * @throws BpmException
	 */
	String replaceTextInUrgeMessage(String urgeMessage,String url,String taskId, String workFlowAdmins,boolean urgeMessageFromJob)throws BpmException;
	
	/**
	 * 
	 * @param formValuesMap
	 * @param uploadDir
	 * @return
	 * @throws BpmException
	 */
	Map<String,String> saveTaskForm(HttpServletRequest formValuesMap,String uploadDir,String userId)throws Exception;
	
	/**
	 * 
	 * @param userId
	 * @param returnType
	 * @param formValuesMap
	 * @param uploadDir
	 * @throws Exception
	 */
	void taskAutoSaveAndBack(String userId,String returnType,HttpServletRequest formValuesMap,String uploadDir)throws Exception;
	
	/**
	 * 
	 * @param userId
	 * @param returnType
	 * @param formValuesMap
	 * @param uploadDir
	 * @throws Exception
	 */
	String autoSaveAndReturn(String userId,HttpServletRequest formValuesMap,String uploadDir)throws Exception;

	void createOrUpdateJaveEventValues(Map<String, Object> paramsMap, String uniqueColumns,Map<String, Object> paramsMapforCheck, Object result) throws Exception;

	Map<String,String> getOranizersByInstanceId(String processInsId);
} 

