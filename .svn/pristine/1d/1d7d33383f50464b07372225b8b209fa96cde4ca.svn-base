package com.eazytec.bpm.metadata.task.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.metadata.form.service.FormDefinitionService;
import com.eazytec.bpm.metadata.task.service.TaskDefinitionService;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.User;


/**
 * <p>For metadata related task service calls. Implements {@link TaskDefinitionService}.</p>
 * 
 * @author madan 
 *
 */
@Service("taskDefintionService")
public class TaskDefinitionServiceImpl implements TaskDefinitionService{
	
	private TaskService taskService;
	private FormDefinitionService formDefintionService;
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * {@inheritDoc} getTasksAssignedForLoggedInUser
	 */
	public List<Task> getTasksAssignedForLoggedInUser(ProcessInstance processInstance){
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
		String loggedInUserId = user.getId();
		try{
			List<Task> tasksAssignedForLoggedInUser = taskService.createTaskQuery() // creates a taskquery impl
			        .taskAssignee(loggedInUserId) // sets the assignee as the user id we pass
			        .processInstanceId(processInstance.getId()) // sets the process instance id to the one we pass
			        .list(); // gets the list of all resulting tasks by executing the query formed above
			return tasksAssignedForLoggedInUser;
		}catch(ActivitiException ae){
			throw new BpmException("Cannot get assigned tasks for user: "+loggedInUserId, ae);
		}
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public Task getTaskById(String id){
		try{
			Task task = taskService.createTaskQuery().taskId(id).singleResult();			
			return task;
		}catch(ActivitiException e){
			throw new BpmException("Problem getting task for id "+id, e);
		}		
	}
	
	/**
	 * Deleted the Task and other related details for that Task based on the Task Id.
	 * @param taskId
	 * @return
	 * @throws BpmException
	 */
	public void deleteTaskById(String taskId) throws BpmException{
		taskService.deleteTask(taskId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	 public void claim(String taskId, String userId) throws BpmException{
		 taskService.claim(taskId, userId);		 
	 }			
	
	 /**
	  * {@inheritDoc}
	  */
	public Task setOwnerIfEmpty(String taskId)throws BpmException{		
		try{
			Task task = getTaskById(taskId);
			if (task.getOwner() == null) {
	            task.setOwner(task.getAssignee());
	            taskService.setOwner(task.getId(), task.getAssignee());
	        }
			return task;
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isTaskFormApplicableForUser(Task task, User user){
        try{
            String currentUser = user.getId();
            return isCurrentUserAssignee(currentUser, task) || isCurrentUserOwner(currentUser, task) || isCurrentUserInvolved(currentUser, task);
        }catch(ActivitiException e){
            throw new BpmException(e.getMessage(), e);
        }
    }
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isTaskClaimableByUser(Task task, User user){
		try{
			String currentUser = user.getId();
			return !isCurrentUserAssignee(currentUser, task) && canUserClaimTask(user, task);
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Task getTaskByProcessInstanceId(String processInstanceId){		

		List<Task> taskList = new ArrayList();
		try{
			taskList =  taskService.createTaskQuery().processInstanceId(processInstanceId).list();
			if(taskList != null && taskList.size() > 0){
				return taskList.get(0);
			}else{
				return null;
			}
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}	
	}
	
	/**
	 * {@inheritDoc getTaskFormData}
	 */
	public TaskFormData getTaskFormData(String taskId){
		try{
			return formDefintionService.getTaskFormData(taskId);
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}				
	}
	
	/**
	 * {@inheritDoc getFormKey}
	 */
	public String getFormKey(String taskId){
		try{
			TaskFormData taskFormData = getTaskFormData(taskId);
			return taskFormData.getFormKey();
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}				
	}
	
	/**
	 * See if current user is the assignee of the task
	 * @param currentUser logged in user
	 * @param task
	 * @return assignee or not
	 */
	private boolean isCurrentUserAssignee(String currentUser, Task task) {	   
	   return currentUser.equals(task.getAssignee());
	}
	
	/**
	 * See if current user is the owner of the task
	 * @param currentUser logged in user
	 * @param task
	 * @return owner or not
	 */
	private boolean isCurrentUserOwner(String currentUser, Task task) {	   
	   return currentUser.equals(task.getOwner());
	}
	
	 /**
     * See if current user is involved to the task involved members can operate the task based on that role that given while involving
     * this can be enhance in future
     * @param currentUser logged in user
     * @param task
     * @return owner or not
     */
    private boolean isCurrentUserInvolved(String currentUser, Task task) {

         List<Task>  currentUserInvolvedTask = taskService.createTaskQuery().taskInvolvedUser(currentUser).taskId(task.getId()).active().list();
        if(currentUserInvolvedTask != null && currentUserInvolvedTask.size() >0){
//        	log.info(currentUser+" involved in  operating : "+task.getName());
            return true;
        }
//        log.info(currentUser+" not involved in operating : "+task.getName());
       return false;
    }
	
	/**
	 * See if the task is claimable by user
	 * @param user for whom check done
	 * @param task which is to be claimed
	 * @return can claim or not
	 */
	private boolean canUserClaimTask(User user, Task task) {
	   return taskService.createTaskQuery()
	     .taskCandidateUser(user.getId())
	     .taskId(task.getId())
	     .count() == 1; 
	}

	@Autowired
	@Qualifier("taskService")
	public void setTaskService(org.activiti.engine.TaskService taskService) {
		this.taskService = taskService;
	}
	
	@Autowired
	public void setFormDefinitionService(FormDefinitionService formDefintionService) {
		this.formDefintionService = formDefintionService;
	}
}