package com.eazytec.bpm.metadata.task.service;

import java.util.List;

import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.eazytec.exceptions.BpmException;
import com.eazytec.model.User;


/**
 * <p>The definition calls for task related functionalities that may be a part
 * of the processes deployed, each individual task collectively form a process, hence needed
 * for process definition calls and also for runtime execution of processes too. </p>
 * 
 * @author madan
 *
 */

public interface TaskDefinitionService {
	
	/**
	 * Retrieves the list of all tasks that are assigned for the user currently logged in.
	 * @param processInstance that is created during process execution at runtime
	 * @return List of {@link Task}
	 */
	List<Task> getTasksAssignedForLoggedInUser(ProcessInstance processInstance);
	

	/**
	 * Deletes the given task, not deleting historic information that is related to this task.
	 * @param taskId The id of the task that will be deleted, cannot be null. If no task
	 * exists with the given taskId, the operation is ignored.
	 * @throws BpmException when an error occurs while deleting the task or in case the task is part
     * of a running process.
	 */
	void deleteTaskById(String taskId) throws BpmException;
	
	
	 /**
	   * <p>Claim responsibility for a task: the given user is made assignee for the task.
	   * The difference with {@link #setAssignee(String, String)} is that here 
	   * a check is done if the task already has a user assigned to it.
	   * No check is done whether the user is known by the identity component.</p>
	   * 
	   * @param taskId task to claim, cannot be null.
	   * @param userId user that claims the task. When userId is null the task is unclaimed,
	   * assigned to no one.
	   * @throws BpmException when the task doesn't exist or when the task
	   * is already claimed by another user.
	   */
	 void claim(String taskId, String userId) throws BpmException;	
	 
	 /**
	  * Gets the task by id
	  * 
	  * @param id
	  * @return the task
	  */
	 Task getTaskById(String id);
	 
	 /**
	  * Makes assignee as owner id task doesnot have owner
	  * @param taskId
	  * @return task
	  */
	 Task setOwnerIfEmpty(String taskId)throws BpmException;
	 
	 /**
	  * Checks if the user can access the form associated with task if any
	  * @param task to which form is associated
	  * @param user to whom check is needed
	  * @return applicable or not, boolean
	  */
	 boolean isTaskFormApplicableForUser(Task task, User user);
	 
	 /**
	  * Checks if the user can claim the task
	  * @param task which the user needs to claim
	  * @param user for whom claim check done
	  * @return claiming allowed or not, boolean
	  */
	 boolean isTaskClaimableByUser(Task task, User user);
	 
	 
	 Task getTaskByProcessInstanceId(String processInstanceId);
	 
 	/**
	 * <p>Traverse through the form that is associated with the task</p>
	 * @param taskId
	 * @return the task form entity
	 */
	TaskFormData getTaskFormData(String taskId);
	
	String getFormKey(String taskId);

}
