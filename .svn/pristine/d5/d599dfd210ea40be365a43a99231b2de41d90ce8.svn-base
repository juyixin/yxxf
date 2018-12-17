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

package org.activiti.engine.impl.history;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.HistoricActivityInstanceQueryImpl;
import org.activiti.engine.impl.HistoricDetailQueryImpl;
import org.activiti.engine.impl.HistoricVariableInstanceQueryImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.cfg.IdGenerator;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.AbstractManager;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.impl.persistence.entity.CommentEntityManager;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricDetailVariableInstanceUpdateEntity;
import org.activiti.engine.impl.persistence.entity.HistoricFormPropertyEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.runtime.InterpretableExecution;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.task.Event;
import org.activiti.engine.task.IdentityLink;

import com.eazytec.bpm.common.util.I18nUtil;
import com.eazytec.common.util.StringUtil;
import com.eazytec.util.DateUtil;

/**
 * Manager class that centralises recording of all history-related operations
 * that are originated from inside the engine.
 * 
 * @author Frederik Heremans
 */
public class HistoryManager extends AbstractManager {
  
  private static Logger log = Logger.getLogger(HistoryManager.class.getName());
  
  private HistoryLevel historyLevel;
  
  public HistoryManager() {
    this.historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
  }
  
  /**
   * @return true, if the configured history-level is equal to OR set to
   * a higher value than the given level.
   */
  public boolean isHistoryLevelAtLeast(HistoryLevel level) {
    if(log.isLoggable(Level.FINE)) {
      log.fine("Current history level: " + historyLevel + ", level required: " + level);
    }
    // Comparing enums actually compares the location of values declared in the enum
    return historyLevel.isAtLeast(level);
  }
  
  /**
   * @return true, if history-level is configured to level other than "none".
   */
  public boolean isHistoryEnabled() {
    if(log.isLoggable(Level.FINE)) {
      log.fine("Current history level: " + historyLevel);
    }
    return !historyLevel.equals(HistoryLevel.NONE);
  }
  
  // Process related history
  
  /**
   * Record a process-instance ended. Updates the historic process instance if activity history is enabled.
   */
  public void recordProcessInstanceEnd(ExecutionEntity execution) {
    
    if(isHistoryLevelAtLeast(HistoryLevel.ACTIVITY)) {
      HistoricProcessInstanceEntity historicProcessInstance = getHistoricProcessInstanceManager()
              .findHistoricProcessInstance(execution.getProcessInstanceId());
      
      if (historicProcessInstance!=null) {
        historicProcessInstance.markEnded(execution.getDeleteReason());
        historicProcessInstance.setEndActivityId(execution.getActivityId());
        historicProcessInstance.setEndTime(ClockUtil.getCurrentTime());
        if(execution.getActivity() != null){
            if(execution.getActivity().getProperty("isArchive") != null){
            	boolean isArchive = Boolean.parseBoolean(execution.getActivity().getProperty("isArchive").toString());
                historicProcessInstance.setIsArchive(isArchive);
                if(isArchive){
             	  	 archiveProcessInstance(historicProcessInstance,execution.getProcessDefinition().getName(),
               	  			((ProcessDefinitionEntity)execution.getProcessDefinition()).getClassificationId());
                     List<PvmTransition> innerIncomingTransitions = execution.getActivity().getIncomingTransitions();
	       	       	 for (PvmTransition innerIncomingTransition : innerIncomingTransitions) {
	       	       		 if(innerIncomingTransition.getSource()!=null){
	       	       			 List<PvmTransition> prevIncomingTransitions = innerIncomingTransition.getSource().getIncomingTransitions();
	       	       			 if(prevIncomingTransitions!=null && prevIncomingTransitions.size()>0){
	       	       				innerIncomingTransition = prevIncomingTransitions.get(prevIncomingTransitions.size()-1);
	       	       				ActivityImpl activity= (ActivityImpl)innerIncomingTransition.getDestination();
				   	        	if(activity.getActivityBehavior()  instanceof UserTaskActivityBehavior){
				   	        		UserTaskActivityBehavior activityBehaviour  = (UserTaskActivityBehavior)activity.getActivityBehavior();
				   	        		for(Expression organizer : activityBehaviour.getTaskDefinition().getAdminUserIdExpressions()){
				   	        			storeArchivedAdmin(historicProcessInstance.getId(),organizer.getExpressionText().split("-")[0]);
									}
				   	        	}
	       	       			 }
	       	       		 }
	       	   		 }
                }
            }
        }
      }
    }
  }
  
  /**
   * Record a process-instance started and record start-event if activity history is enabled.
   */
  public void recordProcessInstanceStart(ExecutionEntity processInstance,boolean isDraft) {
    if(isHistoryLevelAtLeast(HistoryLevel.ACTIVITY)) {
      HistoricProcessInstanceEntity historicProcessInstance = new HistoricProcessInstanceEntity(processInstance,isDraft);
      String startNodeName = (String) processInstance.getActivity().getProperty("name");
      if(startNodeName.isEmpty()) {
    	  historicProcessInstance.setStartNodeName("Default Start Node");
      } else {
          historicProcessInstance.setStartNodeName(startNodeName);
      }
      // Insert historic process-instance
      getDbSqlSession().insert(historicProcessInstance);
  
      // Also record the start-event manually, as there is no "start" activity history listener for this
      IdGenerator idGenerator = Context.getProcessEngineConfiguration().getIdGenerator();
      
      String processDefinitionId = processInstance.getProcessDefinitionId();
      String processInstanceId = processInstance.getProcessInstanceId();
      String executionId = processInstance.getId();
  
      HistoricActivityInstanceEntity historicActivityInstance = new HistoricActivityInstanceEntity();
      historicActivityInstance.setId(idGenerator.getNextId());
      historicActivityInstance.setProcessDefinitionId(processDefinitionId);
      historicActivityInstance.setProcessInstanceId(processInstanceId);
      historicActivityInstance.setExecutionId(executionId);
      historicActivityInstance.setActivityId(processInstance.getActivityId());
      historicActivityInstance.setActivityName((String) processInstance.getActivity().getProperty("name"));
      historicActivityInstance.setActivityType((String) processInstance.getActivity().getProperty("type"));
      Date now = ClockUtil.getCurrentTime();
      historicActivityInstance.setStartTime(now);
      
      getDbSqlSession()
        .insert(historicActivityInstance);
    }
  }
  
  /**
   * Record a sub-process-instance started and alters the calledProcessinstanceId
   * on the current active activity's historic counterpart. Only effective when activity history is enabled.
   */
  public void recordSubProcessInstanceStart(ExecutionEntity parentExecution, ExecutionEntity subProcessInstance) {
    if(isHistoryLevelAtLeast(HistoryLevel.ACTIVITY)) {
      
      HistoricProcessInstanceEntity historicProcessInstance = new HistoricProcessInstanceEntity((ExecutionEntity) subProcessInstance,false);
      getDbSqlSession().insert(historicProcessInstance);
      
      HistoricActivityInstanceEntity activitiyInstance = findActivityInstance(parentExecution);
      if (activitiyInstance != null) {
        activitiyInstance.setCalledProcessInstanceId(subProcessInstance.getProcessInstanceId());
      }
      
    }
  }
  
  // Activity related history

  /**
   * Record the start of an activitiy, if activity history is enabled.
   */
  public void recordActivityStart(ExecutionEntity executionEntity) {
    if(isHistoryLevelAtLeast(HistoryLevel.ACTIVITY)) {
      IdGenerator idGenerator = Context.getProcessEngineConfiguration().getIdGenerator();
      
      String processDefinitionId = executionEntity.getProcessDefinitionId();
      String processInstanceId = executionEntity.getProcessInstanceId();
      String executionId = executionEntity.getId();

      HistoricActivityInstanceEntity historicActivityInstance = new HistoricActivityInstanceEntity();
      historicActivityInstance.setId(idGenerator.getNextId());
      historicActivityInstance.setProcessDefinitionId(processDefinitionId);
      historicActivityInstance.setProcessInstanceId(processInstanceId);
      historicActivityInstance.setExecutionId(executionId);
      historicActivityInstance.setActivityId(executionEntity.getActivityId());
      historicActivityInstance.setActivityName((String) executionEntity.getActivity().getProperty("name"));
      historicActivityInstance.setActivityType((String) executionEntity.getActivity().getProperty("type"));
      historicActivityInstance.setStartTime(ClockUtil.getCurrentTime());
      
      getDbSqlSession().insert(historicActivityInstance);
    }
  }
  
  /**
   * Record the end of an activitiy, if activity history is enabled.
   */
  public void recordActivityEnd(ExecutionEntity executionEntity) {
    if(isHistoryLevelAtLeast(HistoryLevel.ACTIVITY)) {
      HistoricActivityInstanceEntity historicActivityInstance = findActivityInstance(executionEntity);
      if (historicActivityInstance!=null) {
        historicActivityInstance.markEnded(null);
      }
    }
  }
  
  /**
   * Record the end of a start-task, if activity history is enabled.
   */
  public void recordStartEventEnded(String executionId, String activityId) {
    if(isHistoryLevelAtLeast(HistoryLevel.ACTIVITY)) {
      
      // Interrupted executions might not have an activityId set, skip recording history.
      if(activityId == null) {
        return;
      }
      
      // Search for the historic activity instance in the dbsqlsession cache, since process hasn't been persisted to db yet
      List<HistoricActivityInstanceEntity> cachedHistoricActivityInstances = getDbSqlSession().findInCache(HistoricActivityInstanceEntity.class);
      for (HistoricActivityInstanceEntity cachedHistoricActivityInstance: cachedHistoricActivityInstances) {
        if ( executionId.equals(cachedHistoricActivityInstance.getExecutionId())
                && (activityId.equals(cachedHistoricActivityInstance.getActivityId()))
                && (cachedHistoricActivityInstance.getEndTime()==null)
                ) {
          cachedHistoricActivityInstance.markEnded(null);
          return;
        }
      }
    }
  }
  
  /**
   * Finds the {@link HistoricActivityInstanceEntity} that is active in the given
   * execution. Uses the {@link DbSqlSession} cache to make sure the right instance
   * is returned, regardless of whether or not entities have already been flushed to DB.
   */
  public HistoricActivityInstanceEntity findActivityInstance(ExecutionEntity execution) {
    String executionId = execution.getId();
    String activityId = execution.getActivityId();

    // search for the historic activity instance in the dbsqlsession cache
    List<HistoricActivityInstanceEntity> cachedHistoricActivityInstances = getDbSqlSession().findInCache(HistoricActivityInstanceEntity.class);
    for (HistoricActivityInstanceEntity cachedHistoricActivityInstance: cachedHistoricActivityInstances) {
      if (executionId.equals(cachedHistoricActivityInstance.getExecutionId())
           && activityId != null
           && (activityId.equals(cachedHistoricActivityInstance.getActivityId()))
           && (cachedHistoricActivityInstance.getEndTime()==null)
         ) {
        return cachedHistoricActivityInstance;
      }
    }
    
    List<HistoricActivityInstance> historicActivityInstances = new HistoricActivityInstanceQueryImpl(Context.getCommandContext())
      .executionId(executionId)
      .activityId(activityId)
      .unfinished()
      .listPage(0, 1);
    
    if (!historicActivityInstances.isEmpty()) {
      return (HistoricActivityInstanceEntity) historicActivityInstances.get(0);
    }
    
    if (execution.getParentId()!=null) {
      return findActivityInstance((ExecutionEntity) execution.getParent());
    }
    
    return null;
  }
  
  /**
   * Replaces any open historic activityInstances' execution-id's to the id of the replaced
   * execution, if activity history is enabled. 
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void recordExecutionReplacedBy(ExecutionEntity execution, InterpretableExecution replacedBy) {
    if (isHistoryLevelAtLeast(HistoryLevel.ACTIVITY)) {
      
      // Update the cached historic activity instances that are open
      List<HistoricActivityInstanceEntity> cachedHistoricActivityInstances = getDbSqlSession().findInCache(HistoricActivityInstanceEntity.class);
      for (HistoricActivityInstanceEntity cachedHistoricActivityInstance: cachedHistoricActivityInstances) {
        if ( (cachedHistoricActivityInstance.getEndTime()==null)
             && (execution.getId().equals(cachedHistoricActivityInstance.getExecutionId())) 
           ) {
          cachedHistoricActivityInstance.setExecutionId(replacedBy.getId());
        }
      }
    
      // Update the persisted historic activity instances that are open
      List<HistoricActivityInstanceEntity> historicActivityInstances = (List) new HistoricActivityInstanceQueryImpl(Context.getCommandContext())
        .executionId(execution.getId())
        .unfinished()
        .list();
      for (HistoricActivityInstanceEntity historicActivityInstance: historicActivityInstances) {
        historicActivityInstance.setExecutionId(replacedBy.getId());
      }
    }
  }
  /**
   * Record a change of the process-definition id of a process instance, if activity history is enabled.
   */
  public void recordProcessDefinitionChange(String processInstanceId, String processDefinitionId) {
    if(isHistoryLevelAtLeast(HistoryLevel.ACTIVITY)) {
      HistoricProcessInstanceEntity historicProcessInstance = getHistoricProcessInstanceManager().findHistoricProcessInstance(processInstanceId);
      if(historicProcessInstance != null) {
        historicProcessInstance.setProcessDefinitionId(processDefinitionId);
      }
    }
  }
  
  
  // Task related history 
  
  /**
   * Record the creation of a task, if audit history is enabled.
   */
  public void recordTaskCreated(TaskEntity task, ExecutionEntity execution) {
    if (isHistoryLevelAtLeast(HistoryLevel.AUDIT)) {
		task.setTaskType(TaskEntity.NORMAL_TASK);
	    HistoricTaskInstanceEntity historicTaskInstance = new HistoricTaskInstanceEntity(task, execution);
	    getDbSqlSession().insert(historicTaskInstance);
    }
  }
  
  /**
   * Record the assignment of task, if activity history is enabled.
   */
  public void recordTaskAssignment(TaskEntity task) {
    ExecutionEntity executionEntity = task.getExecution();
    if(isHistoryLevelAtLeast(HistoryLevel.ACTIVITY)) {
      if (executionEntity != null) {
        HistoricActivityInstanceEntity historicActivityInstance = findActivityInstance(executionEntity);
        if(historicActivityInstance != null) {
          historicActivityInstance.setAssignee(task.getAssignee());
        }
      }
    }
  }
  
  /**
   * Record the id of a the task associated with a historic activity, if activity history is enabled.
   */
  public void recordTaskId(TaskEntity task) {
    if(isHistoryLevelAtLeast(HistoryLevel.ACTIVITY)) {
      ExecutionEntity execution = task.getExecution();
      if (execution != null) {
        HistoricActivityInstanceEntity historicActivityInstance = findActivityInstance(execution);
        if(historicActivityInstance != null) {
          historicActivityInstance.setTaskId(task.getId());
        }
      }
    }
  }
  
  /**
   * Record task as ended, if audit history is enabled.
   */
  public void recordTaskEnd(String taskId, String deleteReason,int signOffType) {
    if (isHistoryLevelAtLeast(HistoryLevel.AUDIT)) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, taskId);
      if (historicTaskInstance!=null) {
        historicTaskInstance.markEnded(deleteReason);
		/*Calendar cal = Calendar.getInstance();
        if(historicTaskInstance.getTaskDefinition().getTimeSettingDetails().get("warningDays") != null) {
			cal.setTime(historicTaskInstance.getCreateTime());
			cal.add(Calendar.DATE, Integer.parseInt(historicTaskInstance.getTaskDefinition().getTimeSettingDetails().get("warningDays")));
        }
        if(historicTaskInstance.getEndTime().after(cal.getTime())) {
        	historicTaskInstance.setTaskType(TaskEntity.TIMESETTING_TASK);
		}else {
        	historicTaskInstance.setTaskType(TaskEntity.NORMAL_TASK);
        }*/
        historicTaskInstance.setSignOffType(signOffType);
      }
    }
  }
  
  /**
   * Record task assignee change, if audit history is enabled.
   */
  public void recordTaskAssigneeChange(String taskId, String assignee) {
    if (isHistoryLevelAtLeast(HistoryLevel.AUDIT)) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, taskId);
      if (historicTaskInstance!=null) {
        historicTaskInstance.setAssignee(assignee);
      }
    }
  }
  
  /**
   * Record task owner change, if audit history is enabled.
   */
  public void recordTaskOwnerChange(String taskId, String owner) {
    if (isHistoryLevelAtLeast(HistoryLevel.AUDIT)) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, taskId);
      if (historicTaskInstance!=null) {
        historicTaskInstance.setOwner(owner);
      }
    }
  }

  /**
   * Record task name change, if audit history is enabled.
   */
  public void recordTaskNameChange(String taskId, String taskName) {
    if (isHistoryLevelAtLeast(HistoryLevel.AUDIT)) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, taskId);
      if (historicTaskInstance!=null) {
        historicTaskInstance.setName(taskName);
      }
    }
  }

  /**
   * Record task description change, if audit history is enabled.
   */
  public void recordTaskDescriptionChange(String taskId, String description) {
    if (isHistoryLevelAtLeast(HistoryLevel.AUDIT)) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, taskId);
      if (historicTaskInstance!=null) {
        historicTaskInstance.setDescription(description);
      }
    }
  }

  /**
   * Record task due date change, if audit history is enabled.
   */
  public void recordTaskDueDateChange(String taskId, Date dueDate) {
    if (isHistoryLevelAtLeast(HistoryLevel.AUDIT)) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, taskId);
      if (historicTaskInstance!=null) {
        historicTaskInstance.setDueDate(dueDate);
      }
    }
  }

  /**
   * Record task priority change, if audit history is enabled.
   */
  public void recordTaskPriorityChange(String taskId, int priority) {
    if (isHistoryLevelAtLeast(HistoryLevel.AUDIT)) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, taskId);
      if (historicTaskInstance!=null) {
        historicTaskInstance.setPriority(priority);
      }
    }
  }

  /**
   * Record task parent task id change, if audit history is enabled.
   */
  public void recordTaskParentTaskIdChange(String taskId, String parentTaskId) {
    if (isHistoryLevelAtLeast(HistoryLevel.AUDIT)) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, taskId);
      if (historicTaskInstance!=null) {
        historicTaskInstance.setParentTaskId(parentTaskId);
      }
    }
  }

  /**
   * Record task execution id change, if audit history is enabled.
   */
  public void recordTaskExecutionIdChange(String taskId, String executionId) {
    if (isHistoryLevelAtLeast(HistoryLevel.AUDIT)) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, taskId);
      if (historicTaskInstance!=null) {
        historicTaskInstance.setExecutionId(executionId);
      }
    }
  }
  
  /**
   * Record task definition key change, if audit history is enabled.
   */
  public void recordTaskDefinitionKeyChange(String taskId, String taskDefinitionKey) {
    if (isHistoryLevelAtLeast(HistoryLevel.AUDIT)) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, taskId);
      if (historicTaskInstance!=null) {
        historicTaskInstance.setTaskDefinitionKey(taskDefinitionKey);
      }
    }
  }
  
 
  // Variables related history
  
  /**
   * Record a variable has been created, if audit history is enabled.
   */
  public void recordVariableCreate(VariableInstanceEntity variable) {
    // Historic variables
    if(isHistoryLevelAtLeast(HistoryLevel.ACTIVITY)) {
      HistoricVariableInstanceEntity historicVariableInstance = new HistoricVariableInstanceEntity(variable);
      getDbSqlSession().insert(historicVariableInstance);
    }
  }
  
  /**
   * Record a variable has been created, if audit history is enabled.
   */
  public void recordHistoricDetailVariableCreate(VariableInstanceEntity variable, ExecutionEntity sourceActivityExecution, boolean useActivityId) {
    if(isHistoryLevelAtLeast(HistoryLevel.FULL)) {
      
      HistoricDetailVariableInstanceUpdateEntity historicVariableUpdate = new HistoricDetailVariableInstanceUpdateEntity(variable);
      
      if(useActivityId && sourceActivityExecution != null) {
        HistoricActivityInstanceEntity historicActivityInstance = findActivityInstance(sourceActivityExecution); 
        if (historicActivityInstance!=null) {
          historicVariableUpdate.setActivityInstanceId(historicActivityInstance.getId());
        }
      }
      
     getDbSqlSession().insert(historicVariableUpdate);
    }
  }
  
  /**
   * Record a variable has been updated, if audit history is enabled.
   */
  public void recordVariableUpdate(VariableInstanceEntity variable) {
    if(isHistoryLevelAtLeast(HistoryLevel.ACTIVITY)) {
      HistoricVariableInstanceEntity historicProcessVariable = 
      getDbSqlSession().findInCache(HistoricVariableInstanceEntity.class, variable.getId());
      if (historicProcessVariable==null) {
        historicProcessVariable = Context
                .getCommandContext()
                .getHistoricVariableInstanceEntityManager()
                .findHistoricVariableInstanceByVariableInstanceId(variable.getId());
      }
      if (historicProcessVariable!=null) {
        historicProcessVariable.copyValue(variable);
      } else {
        historicProcessVariable = new HistoricVariableInstanceEntity(variable);
        getDbSqlSession().insert(historicProcessVariable);
      }
    }
  }
  
  /**
   * Record start node task, if audit history is enabled.
   */
  public void recordTaskInfoChange(String taskId,boolean isStartNode) {
    if (isHistoryLevelAtLeast(HistoryLevel.AUDIT) && isStartNode) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, taskId);
      if (historicTaskInstance!=null ) {
        historicTaskInstance.setIsForStartNodeTask(isStartNode);
        historicTaskInstance.setIsDratf(true);
      }
    }
  }
  
  // Comment related history
  
  /**
   * Creates a new comment to indicate a new {@link IdentityLink} has been created or deleted, 
   * if history is enabled. 
   */
  public void createIdentityLinkComment(String taskId, String userId, String groupId, String type, boolean create) {
    if(isHistoryEnabled()) {
    	//commented by sangeetha
     // String authenticatedUserId = Authentication.getAuthenticatedUserId();
      CommentEntity comment = new CommentEntity();
    //  comment.setUserId(authenticatedUserId);
      comment.setUserId(userId);
      comment.setType(CommentEntity.TYPE_EVENT);
      comment.setTime(ClockUtil.getCurrentTime());
      comment.setTaskId(taskId);
      if (userId!=null) {
        if(create) {
          comment.setAction(Event.ACTION_ADD_USER_LINK);
        } else {
          comment.setAction(Event.ACTION_DELETE_USER_LINK);
        }
        comment.setMessage(new String[]{userId, type});
      } else {
        if(create) {
          comment.setAction(Event.ACTION_ADD_GROUP_LINK);
        } else {
          comment.setAction(Event.ACTION_DELETE_GROUP_LINK);
        }
        comment.setMessage(new String[]{groupId, type});
      }
      getSession(CommentEntityManager.class).insert(comment);
    }
  }
  
  /**
   * Creates a new comment to indicate a new attachment has been created or deleted, 
   * if history is enabled. 
   */
  public void createAttachmentComment(String taskId, String processInstanceId, String attachmentName, boolean create) {
    if (isHistoryEnabled()) {
      String userId = Authentication.getAuthenticatedUserId();
      CommentEntity comment = new CommentEntity();
      comment.setUserId(userId);
      comment.setType(CommentEntity.TYPE_EVENT);
      comment.setTime(ClockUtil.getCurrentTime());
      comment.setTaskId(taskId);
      comment.setProcessInstanceId(processInstanceId);
      if(create) {
        comment.setAction(Event.ACTION_ADD_ATTACHMENT);
      } else {
        comment.setAction(Event.ACTION_DELETE_ATTACHMENT);
      }
      comment.setMessage(attachmentName);
      getSession(CommentEntityManager.class).insert(comment);
    }
  }

  /**
   * Report form properties submitted, if audit history is enabled.
   * @param subProperties 
   */
  public void reportFormPropertiesSubmitted(ExecutionEntity processInstance,Map<String, String> properties, String taskId, List<FormPropertyHandler> formPropertyHandlers,String formId, Map<String, String[]> subProperties) {
    if (isHistoryLevelAtLeast(HistoryLevel.AUDIT)) {
    	HistoricFormPropertyEntity historicFormProperty = null;
    	String subFormRowCount = StringUtil.isEmptyString(properties.get("rowNumbers")) ? "" :   properties.get("rowNumbers").toString();
      		if(!subFormRowCount.isEmpty()) {
      			String[] subFormCountArray = properties.get("rowNumbers").toString().split(",");
      			int i = 0;
      				for(String subFormCount : subFormCountArray ) {
      						for(FormPropertyHandler formPropertyHandler: formPropertyHandlers) {
      							System.out.println("=====Starting========="+formPropertyHandler.getName()+"============="+properties.get(formPropertyHandler.getName()));
      								if(properties.containsKey(formPropertyHandler.getName()) && !formPropertyHandler.getName().startsWith("subForm_") && i==0){
      									System.out.println("======his details========="+formPropertyHandler.getName()+"======valueeeeeee======="+properties.get(formPropertyHandler.getName()));
      									if(formPropertyHandler.getName().endsWith("_FullName")) {
      										//System.out.println("==============="+formPropertyHandler.getName().substring(0, formPropertyHandler.getName().indexOf("_")));
      										historicFormProperty = new HistoricFormPropertyEntity(processInstance,formPropertyHandler.getName().substring(0, formPropertyHandler.getName().indexOf("_")),
      										properties.get(formPropertyHandler.getName().substring(0, formPropertyHandler.getName().indexOf("_"))), taskId,formPropertyHandler.getColumn().getId()+"_hiddenUserId",formId);
      										getDbSqlSession().insert(historicFormProperty);
      									} else {
          									historicFormProperty = new HistoricFormPropertyEntity(processInstance, formPropertyHandler.getName(),  properties.get(formPropertyHandler.getName()), taskId,formPropertyHandler.getColumn().getId(),formId);
          									getDbSqlSession().insert(historicFormProperty);
      									}
      								}else if(formPropertyHandler.getName().startsWith("subForm_")) {
  										String fieldPropertyName = formPropertyHandler.getName().substring(0,formPropertyHandler.getName().length()-1);
  										System.out.println("==============hisd----- "+fieldPropertyName+subFormCount);
  										historicFormProperty = new HistoricFormPropertyEntity(processInstance, fieldPropertyName+subFormCount,  properties.get(fieldPropertyName+subFormCount), taskId,formPropertyHandler.getColumn().getId(),formId);
  										getDbSqlSession().insert(historicFormProperty);
  										//System.out.println("subFormCountInt==========="+subFormCountInitial);
      								}else if(formPropertyHandler.getColumn().getName().equalsIgnoreCase("id") && i==0){
      									historicFormProperty = new HistoricFormPropertyEntity(processInstance, formPropertyHandler.getColumn().getName(),  null, taskId,formPropertyHandler.getColumn().getId(),formId);
      									getDbSqlSession().insert(historicFormProperty);
      								} 	
      						}
      						i++;
      				}
       	} else{
       		for(FormPropertyHandler formPropertyHandler: formPropertyHandlers) {
					System.out.println("=====Starting========="+formPropertyHandler.getName()+"============="+properties.get(formPropertyHandler.getName()));
						if(properties.containsKey(formPropertyHandler.getName()) || formPropertyHandler.getName().startsWith("subForm_")){
							historicFormProperty = new HistoricFormPropertyEntity(processInstance, formPropertyHandler.getName(),  properties.get(formPropertyHandler.getName()), taskId,formPropertyHandler.getColumn().getId(),formId);
							getDbSqlSession().insert(historicFormProperty);
							System.out.println("======his details========="+formPropertyHandler.getName()+"======valueeeeeee======="+properties.get(formPropertyHandler.getName()));
							if(formPropertyHandler.getName().startsWith("subForm_")) {
								historicFormProperty = new HistoricFormPropertyEntity(processInstance, formPropertyHandler.getName(),  properties.get(formPropertyHandler.getName()), taskId,formPropertyHandler.getColumn().getId(),formId);
								getDbSqlSession().insert(historicFormProperty);
								//System.out.println("subFormCountInt==========="+subFormCountInitial);
							}
							if(formPropertyHandler.getName().endsWith("_FullName")) {
								//System.out.println("==============="+formPropertyHandler.getName().substring(0, formPropertyHandler.getName().indexOf("_")));
								historicFormProperty = new HistoricFormPropertyEntity(processInstance,formPropertyHandler.getName().substring(0, formPropertyHandler.getName().indexOf("_")),
								properties.get(formPropertyHandler.getName().substring(0, formPropertyHandler.getName().indexOf("_"))), taskId,formPropertyHandler.getColumn().getId()+"_hiddenUserId",formId);
								getDbSqlSession().insert(historicFormProperty);
							}
						}else if(formPropertyHandler.getColumn().getName().equalsIgnoreCase("id")){
							historicFormProperty = new HistoricFormPropertyEntity(processInstance, formPropertyHandler.getColumn().getName(),  null, taskId,formPropertyHandler.getColumn().getId(),formId);
							getDbSqlSession().insert(historicFormProperty);
						} 	
				}
       	}
    } 
 }
  
  
  public void reportFormPropertiesUpdated(String processInstanceId, String actId, Map<String, String> properties, String taskId, List<FormPropertyHandler> formPropertyHandlers) {
	  HistoricActivityInstance histActInstance = Context.getCommandContext().getHistoricActivityInstanceEntityManager().
			  findHistoricActivityInstance(actId, processInstanceId);
	  
	  List<HistoricFormPropertyEntity>historicFormPropertyEntities = new ArrayList<HistoricFormPropertyEntity>();
	  
	  List<HistoricVariableInstanceEntity>historicVariableEntities = new ArrayList<HistoricVariableInstanceEntity>();
	  
	  if (isHistoryLevelAtLeast(HistoryLevel.AUDIT)) {
		  
		  List<HistoricDetail> historicDetails = new HistoricDetailQueryImpl(Context.getCommandContext())
			.activityInstanceId(histActInstance.getId()).
			executionId(processInstanceId).formProperties().list();
		  
		  List<HistoricVariableInstance>histVariableInstances = new HistoricVariableInstanceQueryImpl(Context.getCommandContext()).
			      activityInstanceId(histActInstance.getId()).processInstanceId(processInstanceId).list();
	      
		  
    for(FormPropertyHandler formPropertyHandler: formPropertyHandlers) {
	    if(properties.containsKey(formPropertyHandler.getName())){
	        String propertyValue = properties.get(formPropertyHandler.getName());	        
	        for (HistoricDetail historicDetail : historicDetails) {
	        	HistoricFormPropertyEntity historicFormProperty = (HistoricFormPropertyEntity)historicDetail;
	        	if(historicFormProperty.getPropertyId().equals(formPropertyHandler.getName())){
					historicFormProperty.setPropertyValue(propertyValue);
					historicFormProperty.setTime(ClockUtil.getCurrentTime());
					historicFormPropertyEntities.add(historicFormProperty);
				}				
			}
	        for (HistoricVariableInstance historicVariableInstance : histVariableInstances) {
	        	HistoricVariableInstanceEntity historicVariableInstanceEntity = (HistoricVariableInstanceEntity)historicVariableInstance;
		    	  if(historicVariableInstanceEntity.getVariableName().equals(formPropertyHandler.getName())){
		    		  if(historicVariableInstanceEntity.getByteArrayValueId()!=null){
		    			  historicVariableInstanceEntity.setByteArrayValueId(propertyValue);
		    		  }
		    		  if(historicVariableInstanceEntity.getDoubleValue()!=null){
		    			  historicVariableInstanceEntity.setByteArrayValueId(propertyValue);
		    		  }
		    		  if(historicVariableInstanceEntity.getLongValue()!=null){
		    			  try{
		    				  historicVariableInstanceEntity.setLongValue(Long.valueOf(propertyValue));
		    			  }catch(NumberFormatException e){
		    				  
		    			  }		    			  
		    		  }
		    		  if(historicVariableInstanceEntity.getTextValue()!=null){
		    			  historicVariableInstanceEntity.setTextValue(propertyValue);
		    		  }
		    		  if(historicVariableInstanceEntity.getTextValue2()!=null){
		    			  historicVariableInstanceEntity.setTextValue2(propertyValue);
		    		  }		    		  
		    		  historicVariableEntities.add(historicVariableInstanceEntity);
					}
		      }
	        //HistoricFormPropertyEntity historicFormProperty = new HistoricFormPropertyEntity(processInstance, propertyId, propertyValue, taskId);
	        
	      }
	      
	      for (HistoricFormPropertyEntity historicFormPropertyEntity : historicFormPropertyEntities) {
	    	  getDbSqlSession().update(historicFormPropertyEntity);
	      }
	      for (HistoricVariableInstanceEntity historicVariableEntity : historicVariableEntities) {
	    	  getDbSqlSession().update(historicVariableEntity);
	      }
    	}
	 }
   }
  
    public void archiveProcessInstance(HistoricProcessInstanceEntity historicProcessInstance,String processName,String classificationId){
    	Map<String, Object>paramsMap = new HashMap<String, Object>();
  	  	List<String>columns = new ArrayList<String>();
  	  	List<String> newColumns = new ArrayList<String>();
  	  	
  	  	List<String> fieldValues = new ArrayList<String>();
  	  	columns.add("`ID_`");  			
		columns.add("`PROC_INST_ID_`"); 
		columns.add("`PROC_DEF_ID_`");  			
		columns.add("`START_TIME_`");  			
		columns.add("`END_TIME_`");  			
		columns.add("`START_USER_ID_`");  	
		columns.add("`CLASSIFICATION_`");
		columns.add("`PROCESS_NAME_`");
		columns.add("`PATH_`");
		String id = UUID.randomUUID().toString();
		fieldValues.add(StringUtil.getDBStringValue(id));
		fieldValues.add(StringUtil.getDBStringValue(historicProcessInstance.getId()));
		fieldValues.add(StringUtil.getDBStringValue(historicProcessInstance.getProcessDefinitionId()));
		fieldValues.add(StringUtil.getDBStringValue(DateUtil.convertDateToString(historicProcessInstance.getStartTime())));
		fieldValues.add(StringUtil.getDBStringValue(DateUtil.convertDateToString(historicProcessInstance.getEndTime())));
		fieldValues.add(StringUtil.getDBStringValue(historicProcessInstance.getStartUserId()));
		fieldValues.add(StringUtil.getDBStringValue(classificationId));
		fieldValues.add(StringUtil.getDBStringValue(processName));
		//String truncatedName = processName;
		String path = "/root/processArchivalFiles/"+classificationId+"/"+processName.replace(" ", "_")+"/"+historicProcessInstance.getId();
		fieldValues.add(StringUtil.getDBStringValue(path));
        paramsMap.put("tableName", I18nUtil.getMessageProperty("databaseTablePrefix")+"ACT_HI_ARCHIVED_PROCESS");
        
	    if(I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("mssql")) {
	    	for(String column : columns) {
	    		newColumns.add(column.replace("`", ""));
	    	}
	    	 paramsMap.put("columns", newColumns);
	    } else if (I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("oracle")) {
	    	 paramsMap.put("columns",  columns.toString().replaceAll("`", ""));
	    } else {
	    	 paramsMap.put("columns", columns);
	    }
  	    paramsMap.put("fieldValues", fieldValues);
    	Context.getCommandContext().getTableEntityManager().storeRtValues(paramsMap);
    }
    
    public void storeArchivedAdmin(String historicProcessInstanceId,String adminId){
    	Map<String, Object>paramsMap = new HashMap<String, Object>();
  	  	List<String>columns = new ArrayList<String>();
  	  	List<String> newColumns = new ArrayList<String>();
  	  	List<String> fieldValues = new ArrayList<String>();
  	  	columns.add("`ID_`");  			
		columns.add("`ADMIN_ID_`"); 
		columns.add("`ARCHIVED_PROC_INS_ID_`");  			
		String id = UUID.randomUUID().toString();
		fieldValues.add(StringUtil.getDBStringValue(id));
		fieldValues.add(StringUtil.getDBStringValue(adminId));
		fieldValues.add(StringUtil.getDBStringValue(historicProcessInstanceId));
        paramsMap.put("tableName", I18nUtil.getMessageProperty("databaseTablePrefix")+"ACT_HI_ARCHIVED_DET");
        
	    if(I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("mssql")) {
	    	for(String column : columns) {
	    		newColumns.add(column.replace("`", ""));
	    	}
	    	 paramsMap.put("columns", newColumns);
	    } else if (I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("oracle")) {
	    	 paramsMap.put("columns",  columns.toString().replaceAll("`", ""));
	    } else {
	    	 paramsMap.put("columns", columns);
	    }
	    
  	    paramsMap.put("fieldValues", fieldValues);
    	Context.getCommandContext().getTableEntityManager().storeRtValues(paramsMap);
    }
    
    public void updateProcessBusinessKeyInHistory(ExecutionEntity processInstance) {
        if (isHistoryEnabled()) {
          if (processInstance != null) {
            HistoricProcessInstanceEntity historicProcessInstance = getDbSqlSession().selectById(HistoricProcessInstanceEntity.class, processInstance.getId());
            if (historicProcessInstance != null) {
              historicProcessInstance.setBusinessKey(processInstance.getProcessBusinessKey());
              getDbSqlSession().update(historicProcessInstance);
            }
          }
        }
      }
}
