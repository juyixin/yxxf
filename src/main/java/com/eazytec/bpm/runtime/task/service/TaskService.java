package com.eazytec.bpm.runtime.task.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.form.FormFieldPermission;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Event;
import org.activiti.engine.task.OperatingFunction;
import org.activiti.engine.task.Task;

import org.json.JSONArray;
import org.springframework.ui.ModelMap;

import com.eazytec.bpm.engine.TaskRole;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Opinion;
import com.eazytec.model.User;

/**
 * <p> The representation of task during runtime and all the functional blocks
 * related to process as an entity during runtime like completing suspending etc
 * Runtime here refers to the execution of workflow that is already defined. </p>
 * 
 * @author madan
 */

public interface TaskService {	
	
	/**
	 * Calls the bpm engine to mark the task as completed
	 * @param taskId
	 */
	void completeTask(String taskId);
	
	/**
	 * Makes the task claimed by the user so that it is in his bucket, assigned to him
	 * @param taskId
	 * @param user
	 */
	void claimTask(String taskId, User user);
	
	/**
	 * <p>Gets the details about the owner of task if any, and the 
	 * related info regarding the task-owner relationship</p>
	 * 
	 * @param task entity for which info needed
	 * @return the owner details as key-value pair
	 */
	Map<String, Object> getOwnerDetails(Task task)throws BpmException;
	
	/**
	 * <p>Gets the details about the assignee of task if any,
	 *  and the related info regarding the task-assignee relationship</p>
	 *  
	 * @param task entity for which info needed
	 * @return the assignee details as key-value pair
	 */
	Map<String, Object> getAssigneeDetails(Task task)throws BpmException;
	
	/**
	 * <p>Gets the details about all people involved to the task if any,
	 *  and the related info regarding the task-people relationship</p>
	 * @param task entity for which info needed
	 * @return the involved people details as list of key-value pair
	 */
	List<Map<String, Object>> getInvlovedPeopleDetails(Task task)throws BpmException;
	
	/** 
	 * Get list of task for the user currently logged in 
	 * @param loggedInUser of current logged in user, cannot be null
	 * @throws Exception 
	 */
	List<Task> getMyTaskByType(User user , String type) throws BpmException;
	
	
	void taskPriority(String taskId, Integer priority);
	
	/** 
	 * Get list of task for the user currently logged in 
	 * @param loggedInUser of current logged in user, cannot be null
	 * @throws Exception 
	 */
	List<Task> getToDoListByUser(User user) throws BpmException;
	
	
	/**
	 * 
	 * @param tasks
	 * @param user
	 * @return
	 * @throws BpmException
	 */
	List<Map<String, Object>> resolveTaskDetails(List<Task> tasks,User user,String type) throws BpmException;
	
	/**
	 * Gets the task instances history under a process instance id
	 * @param processInstanceId for teh process instance
	 * @return the list of task instances
	 */
	List<HistoricTaskInstance> getHistoricTaskInstances(String processInstanceId);
	
	/**
	 * Resolves the list of task instances as key-value pair lists
	 * @param processInstanceId the list of instances
	 * @return the resolved list of key-value pairs
	 */
	List<Map<String, Object>>  resolveHistoricTaskInstance(String  processInstanceId);
	
	/**
	   * Changes the assignee of the given task to the given userId 
	   * or  
	   * Transfers ownership of this task to another user.
	   * or 
	   * Involve the user as any of the Task Role 
	   * @param taskId id of the task, cannot be null.
	   * @param userId of the person that is receiving ownership.
	   * @param type either to reassign or Transfer
	   * @throws BpmException when the task or user doesn't exist.
	   */
	
	void addMember(String taskId, String userId, String type, String identityLinkType) throws BpmException;
	
	/**
	   * Remove the assignee of the given task to the given userId 
	   * or  
	   * Remove the  ownership of this task to another user.
	   * or 
	   * Remove the involved user of the Task. 
	   * @param taskId id of the task, cannot be null.
	   * @param userId of the person that is receiving ownership.
	   * @param type either to reassign or Transfer
	   * @throws BpmException when the task or user doesn't exist.
	   */
	
	void removeMember(String taskId, String userId, String identityLinkType) throws BpmException;
	
	/** The all events related to the given task. */
	  List<Event> getTaskEvents(String taskId);
	  
	/**
	 * Get All Task Form Values by Task Id
	 * @param taskId
	 * @return
	 * @throws BpmException
	 */
	  Map<HistoricTaskInstance, List<FormProperty>> getTaskFormValuesByTaskId(String taskId)throws BpmException;
	  
	  TaskFormData getHistoricTaskFormData(HistoricTaskInstance task);
	  
	  TaskFormData getHistoricTaskFormData(String taskId);
	  
	  List<HistoricTaskInstance> getHistoricTaskInstancesByTaskId(String taskId);
	  
	  HistoricTaskInstance getHistoricTaskInstanceByTaskId(String taskId);
	  
	  OperatingFunction getOperatingFunctionForUser(User user, Task task);
	  
	  
	  /**
	   * 根据"任务角色"获取用户在任务节点的操作对象
	   * @param user
	   * @param task
	   * @param taskRole
	   * @return
	   */
	  OperatingFunction getOperatingFunctionForUser(User user, Task task, TaskRole taskRole);
	  
	  /**
	   * 获取用户在任务节点的"任务角色"。
	   * 用户在某个任务节点可能会有多个任务角色，按任务角色优先级获取优先级最高的任务角色。
	   * 优先级从高到低 ORGANIZER、COORDINATOR、WORKFLOW_ADMINISTRATOR、CREATOR、PROCESSED_USER、READER。
	   * @param user
	   * @param taskId
	   * @return
	   */
	  TaskRole getTaskRoleApplicableForUser(User user, String taskId);
	  
	  /**
	   * <p>Returns the permissions for each field based on the {@link TaskRole} for the user.</p>
	   * @param user logged in user
	 * @param taskId
	 * @param isStartNodeTask TODO
	   * @return the permission as key - field name and value - {@link FormFieldPermission}
	   */
	  Map<String, FormFieldPermission> getNodeLevelFieldPermissions(User user, String taskId, boolean isStartNodeTask);
	  
	  Map<String,FormFieldPermission> getStartNodeFieldPermission(String proDefId);
	  
	  /**
	   * Checks if the current user is the final transactor or any other organizer is there
	   * after the user to sign off the node
	   * @param taskId
	   * @return
	   */
	  boolean isFinalTransactor(String taskId);
	  
	  TaskEntity getNextTaskForProcessInstance(String processInstanceId)throws BpmException;
	  
	  
	  void assignSubstitute(List<String> taskIds, String userId) throws BpmException;
	  
	  /**
	   * Get TaskPermission Roles
	   * @param user
	   * @param taskId
	   * @return
	   */
	  Set<TaskRole> getTaskPermission(User user, String taskId);
	  
	  /**
	   * 获取用户在任务节点的操作对象
	   * 用户在某个任务节点可能会有多个任务角色，每个角色都对应了一个操作对象，所以用户在某个任务节点可能会有多个操作对象。
	   * 最终需要合并所有操作对象的值
	   * @param user
	   * @param task
	   * @return
	   */
	  OperatingFunction getOperatingFunction(User user, Task task);
	  
	  /**
	   * Show task in my bucket for creator
	   * @param user
	   * @return
	   * @throws BpmException
	   */
	  List<Task> getMyBucketForCreator(User user) throws BpmException;
	  
	  /**
	   * show task in my bucket for processed user
	   * @param user
	   * @return
	   * @throws BpmException
	   */
	  List<Task> getMyBucketForProcessedUser(User user) throws BpmException;
	  
	  /**
	   * Check and Get past values for form if it is mapped in the previous task
	   * @param processInstanceId
	   * @return
	   */
	  Map<String,Object> checkAndGetPastValuesForForm(String processInstanceId,String taskId,String formId);
	  
	  Map<String,Object> checkAndGetPastValuesForInstance(String processInstanceId,String taskId);
	  
	  void addIdentityLinkDetail(String taskId);
	  void addIdentityLinkDetail(String proInsId,String userId,boolean isRead);
	  
	  void removeIdentityLinkDetail(String taskId,String userId);
	  
	  List<Task> getDoneTask(User user,boolean isDocument) throws BpmException;
	  
	  List<Task> getDoneCreatorTasks(User user,boolean isDocument) throws BpmException;
	  
	  List<Task> getDoneProcessedUserTasks(User user) throws BpmException;
	  
	  Task setTaskDefinition(Task task);
	  
	  void updateIdentityLinkDetailReadStatus(String proInsId,String userId,boolean isRead);
	  
	  List<Task> getWorkFlowTraceData(String processInstanceId, String processDefinitionId) throws BpmException;
	  
	  /**
	   * Get operating function for given user
	   * @param user
	   * @param task
	   * @return
	   */
	  OperatingFunction getRecallFunctionForHistoricTask(User user, HistoricTaskInstance historicTask);
	  
	  List<Task> getCancelDocumentData(User user) throws BpmException;
	  
	  Task getTask(String taskId) throws BpmException;
	  
	  JSONArray getFormFieldTraceData(String processInstanceId,String formId) throws BpmException;
	  
	  Map<String, Map<String, Object>> setFormFieldAutomatic(List<Map<String, String>> automaticFillFieldInfo,List<Opinion> opinionList,Task currentTask) throws EazyBpmException;
	  
  	/**
  	 * Handling active tasks for process time setting 
  	 * Sending notification & handling task(like return, jump, notice suspend etc as per the requirement) if time out 
  	 */
	String handleUnSubmittedTaskByTimeSetting();

	Map<String, Object> getScriptsInProcess(TaskEntity taskEntity,Map<String, Object> context);
	
	OperatingFunction getOperatingFunctionForStartNode(User user, Task task);

	void setInstanceTraceData(String processDefinitionId,String processInstanceId, String id, ModelMap model,String processDefinitionName, String parentProcessDefinitionId, Task currentTask);
	
	/**
	 * funtion to get the count for logged in user tasks
	 * 
	 * @param user
	 * @param type
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> getTaskCountByUserId(User user) throws Exception;

}
