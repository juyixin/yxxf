/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.engine.impl.persistence.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.TaskQueryImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.AbstractManager;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.springframework.security.core.context.SecurityContextHolder;

import com.eazytec.bpm.engine.TransactorType;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.User;


/**
 * @author Tom Baeyens
 * @author madan
 */
public class TaskEntityManager extends AbstractManager {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void deleteTasksByProcessInstanceId(String processInstanceId, String deleteReason, boolean cascade) {
    List<TaskEntity> tasks = (List) getDbSqlSession()
      .createTaskQuery()
      .processInstanceId(processInstanceId)
      .list();
  
    String reason = (deleteReason == null || deleteReason.length() == 0) ? TaskEntity.DELETE_REASON_DELETED : deleteReason;
    
    for (TaskEntity task: tasks) {
      deleteTask(task, reason, cascade);
    }
  }
  
  public void deleteCurrentAssigneeIdentityLink(TaskEntity task,String loggedInUser) {
	  CommandContext commandContext = Context.getCommandContext();
	  commandContext
      .getIdentityLinkEntityManager().deleteCandidateLinksByTaskAndUser(task.getId(), task.getAssignee(),loggedInUser);
  }
  
  public void deleteIdentityLinksByTaskId(TaskEntity task) {
	  CommandContext commandContext = Context.getCommandContext();
	  commandContext
      .getIdentityLinkEntityManager().deleteIdentityLinksByTaskId(task.getId());
  }
  public void deleteCurrentCoordinatorIdentityLink(TaskEntity task,String loggedInUserId) {
	  CommandContext commandContext = Context.getCommandContext();
	  commandContext
      .getIdentityLinkEntityManager().deleteCoordinatorLinksByTaskAndUser(task.getId(), task.getAssignee(),loggedInUserId);
  }
  
  public IdentityLinkEntity getNextCandidateIdentityLinkByOrder(TaskEntity task){
	  CommandContext commandContext = Context.getCommandContext();
	  return commandContext
      .getIdentityLinkEntityManager().findNextIdentityLinkByOrder(task, IdentityLinkType.CANDIDATE);
  }
  
  public IdentityLinkEntity getCreatorIDentityLink(TaskEntity task){
	  CommandContext commandContext = Context.getCommandContext();
	  return commandContext
      .getIdentityLinkEntityManager().findNextIdentityLinkByOrder(task, IdentityLinkType.CREATOR);
  }
  
  public IdentityLinkEntity getCreatorIDentityLinkForTaskId(String taskId){
	  CommandContext commandContext = Context.getCommandContext();
	  List<IdentityLinkEntity> identityLinkList = commandContext
		      .getIdentityLinkEntityManager().findCreatorIdentityLink(taskId);
	  if(identityLinkList != null){
		  return identityLinkList.get(0);
	  }else{
		  return null;
	  }
	 
	  
  }
  
public IdentityLinkEntity getNextCandidateIdentityLinkByOrder(IdentityLinkEntity identityLinkEntity){
	  
	  CommandContext commandContext = Context.getCommandContext();
	    
	  return commandContext
      .getIdentityLinkEntityManager().findNextIdentityLinkByOrder(identityLinkEntity);
  }
  
  public List<Task> findTasksBySignOffTypesAndUser(String userId, List<TransactorType> signOffTypes){
	  Map<String, Object> parameters = new HashMap<String, Object>();	    
	    parameters.put("userId", userId);
	    try{
	    	 int i = 0;
	 	    for (TransactorType transactorType : signOffTypes) {
	 	    	parameters.put("signOffType_"+i, transactorType.getStateCode());
	 	    	i++;
	 		}	    
	 	    return getDbSqlSession().selectList("selectTasksByUserAndSignOffTypes", parameters);
	    }catch(Exception e){
	    	throw new BpmException("cannot get tasks to be signed off for user "+userId, e);
	    }
	   
  }
  
  public List<IdentityLinkEntity> findCurrentAssigneeIdentityLink(TaskEntity task) {
	  CommandContext commandContext = Context.getCommandContext();
	 return commandContext
      .getIdentityLinkEntityManager().findIdentityLinkByTaskUserGroupAndType(task.getId(), task.getAssignee(), null, IdentityLinkType.CANDIDATE);
  }

  public void deleteTask(TaskEntity task, String deleteReason, boolean cascade) {
    if (!task.isDeleted()) {
      task.setDeleted(true);
      
      CommandContext commandContext = Context.getCommandContext();
      String taskId = task.getId();
      
      List<Task> subTasks = findTasksByParentTaskId(taskId);
      for (Task subTask: subTasks) {
        deleteTask((TaskEntity) subTask, deleteReason, cascade);
      }
      
      commandContext
        .getIdentityLinkEntityManager()
        .deleteIdentityLinksByTaskId(taskId);

      commandContext
        .getVariableInstanceEntityManager()
        .deleteVariableInstanceByTask(task);

      if (cascade) {
        commandContext
          .getHistoricTaskInstanceEntityManager()
          .deleteHistoricTaskInstanceById(taskId);
      } else {
        commandContext
          .getHistoryManager()
          .recordTaskEnd(taskId, deleteReason,task.getSignOffType());
      }
        
      getDbSqlSession().delete(task);
    }
  }


  public TaskEntity findTaskById(String id) {
    if (id == null) {
      throw new ActivitiException("Invalid task id : null");
    }
    return (TaskEntity) getDbSqlSession().selectById(TaskEntity.class, id);
  }

  @SuppressWarnings("unchecked")
  public List<TaskEntity> findTasksByExecutionId(String executionId) {
    return getDbSqlSession().selectList("selectTasksByExecutionId", executionId);
  }
  
  @SuppressWarnings("unchecked")
  public List<TaskEntity> findTasksByProcessInstanceId(String processInstanceId) {
    return getDbSqlSession().selectList("selectTasksByProcessInstanceId", processInstanceId);
  }
  
  public TaskEntity findNextTaskForProcessInstance(String processInstanceId) {
    return (TaskEntity)getDbSqlSession().selectOne("selectNextTaskForProcessInstance", processInstanceId);
  }
  
  @Deprecated
  public List<Task> findTasksByQueryCriteria(TaskQueryImpl taskQuery, Page page) {
    taskQuery.setFirstResult(page.getFirstResult());
    taskQuery.setMaxResults(page.getMaxResults());
    return findTasksByQueryCriteria(taskQuery);
  }
  
  @SuppressWarnings("unchecked")
  public List<Task> findTasksByQueryCriteria(TaskQueryImpl taskQuery) {
	// If task query has custom query, then override that, else use default
	if(taskQuery.getCustomQuery()!=null){
	    return getDbSqlSession().selectList(taskQuery.getCustomQuery(), taskQuery);
	}else{
		final String query = "selectTaskByQueryCriteria";
	    return getDbSqlSession().selectList(query, taskQuery);
	}
    
  }

  public long findTaskCountByQueryCriteria(TaskQueryImpl taskQuery) {
    return (Long) getDbSqlSession().selectOne("selectTaskCountByQueryCriteria", taskQuery);
  }
  
  @SuppressWarnings("unchecked")
  public List<Task> findTasksByNativeQuery(Map<String, Object> parameterMap, int firstResult, int maxResults) {
    return getDbSqlSession().selectListWithRawParameter("selectTaskByNativeQuery", parameterMap, firstResult, maxResults);
  }

  public long findTaskCountByNativeQuery(Map<String, Object> parameterMap) {
    return (Long) getDbSqlSession().selectOne("selectTaskCountByNativeQuery", parameterMap);
  }
  
  public List<IdentityLink> findIdentityLinksForUser(String taskId, User user){
	  CommandContext commandContext = Context.getCommandContext();
	  return commandContext
		      .getIdentityLinkEntityManager().findIdentityLinksForUser(taskId, user.getId(), user.getGroupIds(), null);
  }
  
  public List<IdentityLink> findIdentityLinksApplicableForUser(String taskId, User user){
	  CommandContext commandContext = Context.getCommandContext();
	  return commandContext
		      .getIdentityLinkEntityManager().findIdentityLinksApplicableForUser(taskId, user, null);
  }
  
  public List<IdentityLink> findIdentityLinksApplicableForUser(String taskId, User user, List<String> excludeTypes){
	  CommandContext commandContext = Context.getCommandContext();
	  return commandContext
		      .getIdentityLinkEntityManager().findIdentityLinksApplicableForUserExcludingTypes(taskId, user, excludeTypes);
  }
  
  public List<IdentityLink> findIdentityLinksForTask(String taskId, String type){
	  CommandContext commandContext = Context.getCommandContext();
	  return commandContext
		      .getIdentityLinkEntityManager().findIdentityLinkByTaskAndType(taskId, type);
  }
  
  public List<IdentityLink> findIdentityLinksForUsers(String taskId, List<String>userIds, String type){
	  CommandContext commandContext = Context.getCommandContext();
	  return commandContext
		      .getIdentityLinkEntityManager().findIdentityLinksByUsersAndType(taskId, userIds, type);
  }

  @SuppressWarnings("unchecked")
  public List<Task> findTasksByParentTaskId(String parentTaskId) {
    return getDbSqlSession().selectList("selectTasksByParentTaskId", parentTaskId);
  }

  public void deleteTask(String taskId, String deleteReason, boolean cascade) {
    TaskEntity task = Context
      .getCommandContext()
      .getTaskEntityManager()
      .findTaskById(taskId);
    
    if (task!=null) {
      if(task.getExecutionId() != null) {
        throw new ActivitiException("The task cannot be deleted because is part of a running process");
      }
      
      String reason = (deleteReason == null || deleteReason.length() == 0) ? TaskEntity.DELETE_REASON_DELETED : deleteReason;
      deleteTask(task, reason, cascade);
    } else if (cascade) {
      Context
        .getCommandContext()
        .getHistoricTaskInstanceEntityManager()
        .deleteHistoricTaskInstanceById(taskId);
    }
  }
  
  public void updateAssigneeForTasks(List<String> taskIds, String userId){
  
	  Map<String, Object> parameters = new HashMap<String, Object>();	    
	  parameters.put("taskIds", taskIds);
	  try{
		  String identityType = IdentityLinkType.ORGANIZER;
		  String groupId = null;
		  String groupType = null;
		  List<TaskEntity> taskEntities =  getDbSqlSession().selectList("selectTasks", parameters);
		  if(taskEntities !=null && taskEntities.size() > 0){
			  for(TaskEntity taskEntity : taskEntities){
				  	//Changing assignee
				  	taskEntity.setAssignee(userId); // end
			        //Deleting the Identity link for the user who is transfer his task
				    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			        for(IdentityLinkEntity indentityLink : taskEntity.getIdentityLinks()){
			    	    if(indentityLink.getUserId().equals(user.getId()) && indentityLink.getType().equals(identityType)){
			    	    	groupId = indentityLink.getGroupId();
			    	    	groupType = indentityLink.getGroupType();
			    	    	taskEntity.deleteIdentityLink(user.getId(),groupId, identityType, false);
			    		    break;
			    	    }
			        }//end
			        taskEntity.addIdentityLink(userId, groupId, identityType, groupType);
				  getDbSqlSession().update(taskEntity);
			  }
		  }
//		  List<IdentityLinkEntity> identityLinkEntities =  getDbSqlSession().selectList("selectNextIdentityLinks", parameters);
//		  if(identityLinkEntities !=null && identityLinkEntities.size() > 0){
//			  for(IdentityLinkEntity identityLinkEntity : identityLinkEntities){
//
//				  identityLinkEntity.setGroupId(null);
//				  identityLinkEntity.setGroupType(null);
//				  identityLinkEntity.setUserId(userId);
//				  getDbSqlSession().update(identityLinkEntity);
//			  }
//		  }
 
	  }catch(Exception e){
	  	throw new BpmException("Error while  update Assignee For Tasks "+taskIds, e);
	  }
	  
  }
  
  	public void updateTaskEntity(TaskEntity taskEntity){
  		try{
  			getDbSqlSession().update(taskEntity);
  		}catch(Exception e){
  			throw new BpmException("Error while  update urge details for the tasks "+taskEntity.getId(), e);
  		}
  	}

	public List<IdentityLink> findHistoricIdentityLinksForTask(String taskId,
			String type) {
		  CommandContext commandContext = Context.getCommandContext();
		  return commandContext
			      .getIdentityLinkEntityManager().findHistoricIdentityLinksForTask(taskId, type);
	}

	public List<IdentityLink> findIdentityLinksForTaskByInstanceId(String processInstanceId) {
		  CommandContext commandContext = Context.getCommandContext();
		  return commandContext
			      .getIdentityLinkEntityManager().findIdentityLinksForTaskByInstanceId(processInstanceId);
	}

	public TaskEntity findExecutionByProcessInstanceId(String processInstanceId) {
	      return (TaskEntity) getDbSqlSession().selectOne("findExecutionByProcessInstanceId", processInstanceId);
	  //  return getDbSqlSession().selectList("findExecutionByProcessInstanceId", processInstanceId);
	}

	public void updateHistoricTaskEntityCmd(TaskEntity taskEntity) {
	      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, taskEntity.getId());
	      if(historicTaskInstance.getTaskType().equalsIgnoreCase(TaskEntity.NORMAL_TASK)) {
	    	  historicTaskInstance.setTaskType(TaskEntity.TIMESETTING_TASK);
	  			getDbSqlSession().update(historicTaskInstance);
	      }
	}
}
