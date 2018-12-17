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
package org.activiti.engine.impl.bpmn.behavior;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.calendar.DueDateBusinessCalendar;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.task.TaskDefinition;

/**
 * activity implementation for the user task.
 * 
 * @author Joram Barrez
 */
public class UserTaskActivityBehavior extends TaskActivityBehavior {

  protected TaskDefinition taskDefinition;
  public UserTaskActivityBehavior(TaskDefinition taskDefinition) {
    this.taskDefinition = taskDefinition;
  }
  

  public void execute(ActivityExecution execution) throws Exception {
	//  ExecutionEntity executionEnt = (ExecutionEntity) execution; // html source for cloned rows is added in hidden field

    TaskEntity task = TaskEntity.createAndInsert(execution);
	if( ((ExecutionEntity) execution).getHtmlSourceForSubForm() != null) {
	    task.setHtmlSourceForSubForm(((ExecutionEntity) execution).getHtmlSourceForSubForm());
	    task.setFormkey(((ExecutionEntity) execution).getFormkey());
	    ((ExecutionEntity) execution).setHtmlSourceForSubForm(null);
	    ((ExecutionEntity) execution).setFormkey(null);
	}
	
    task.setExecution(execution);
    task.setTaskDefinition(taskDefinition);

    if (taskDefinition.getNameExpression() != null) {
      String name = (String) taskDefinition.getNameExpression().getValue(execution);
      task.setName(name);
    }

    if (taskDefinition.getDescriptionExpression() != null) {
      String description = (String) taskDefinition.getDescriptionExpression().getValue(execution);
      task.setDescription(description);
    }
    
    if(taskDefinition.getIsOpinion() != null) {
    	String isOpinion =  taskDefinition.getIsOpinion();
    	task.setIsOpinion(isOpinion);
    }    
    // Step for setting sign off type for a task, the 4 types of node signoff
    if(taskDefinition.getSignOffTypeExpression() != null) {
    	String signOffType = (String) taskDefinition.getSignOffTypeExpression().getValue(execution);
    	task.setSignOffType(signOffType);
    }
    //check and set the start node task attributes
    if(taskDefinition.getIsForStartNode() != null && taskDefinition.getIsForStartNode().toString().equalsIgnoreCase("true")){
    	task.setIsDratf(true);
    	task.setIsForStartNodeTask(true);
    	task.setIsOpinion("true");
    }
    
    if(taskDefinition.getStartScriptNameExpression() != null) {
    	String startScriptName = (String) taskDefinition.getStartScriptNameExpression().getValue(execution);
    	task.setStartScriptName(startScriptName);
    }
    
    if(taskDefinition.getEndScriptNameExpression() != null) {
    	String endScriptName = (String) taskDefinition.getEndScriptNameExpression().getValue(execution);
    	task.setEndScriptName(endScriptName);
    } 
    
    if(taskDefinition.getStartScriptExpression() != null) {
    	String startScript = (String) taskDefinition.getStartScriptExpression().getValue(execution);
    	task.setStartScript(startScript);
    }
    
    if(taskDefinition.getEndScriptExpression() != null) {
    	String endScript = (String) taskDefinition.getEndScriptExpression().getValue(execution);
    	task.setEndScript(endScript);
    }    
        
    if(taskDefinition.getDueDateExpression() != null) {
      Object dueDate = taskDefinition.getDueDateExpression().getValue(execution);
      if(dueDate != null) {
        if (dueDate instanceof Date) {
          task.setDueDate((Date) dueDate);
        } else if (dueDate instanceof String) {
          task.setDueDate(new DueDateBusinessCalendar().resolveDuedate((String) dueDate)); 
        } else {
          throw new ActivitiException("Due date expression does not resolve to a Date or Date string: " + 
              taskDefinition.getDueDateExpression().getExpressionText());
        }
      }
    }

    if (taskDefinition.getPriorityExpression() != null) {
      final Object priority = taskDefinition.getPriorityExpression().getValue(execution);
      if (priority != null) {
        if (priority instanceof String) {
          try {
            task.setPriority(Integer.valueOf((String) priority));
          } catch (NumberFormatException e) {
            throw new ActivitiException("Priority does not resolve to a number: " + priority, e);
          }
        } else if (priority instanceof Number) {
          task.setPriority(((Number) priority).intValue());
        } else {
          throw new ActivitiException("Priority expression does not resolve to a number: " + 
                  taskDefinition.getPriorityExpression().getExpressionText());
        }
      }
    }    
   	task.addAdminCreator(task.getProcessStartUserId());
    	
   
    handleAssignments(task, execution);
    //Setting time setting elements
    task.setMaxDays(Integer.parseInt((taskDefinition.getTimeSettingDetails().get("maxDays") != null) ? taskDefinition.getTimeSettingDetails().get("maxDays") : "0"));
    task.setWarningDays(Integer.parseInt((taskDefinition.getTimeSettingDetails().get("warningDays") != null) ? taskDefinition.getTimeSettingDetails().get("warningDays") : "0"));
    task.setDateType(taskDefinition.getTimeSettingDetails().get("dateType"));
    task.setUrgeTimes(Integer.parseInt((taskDefinition.getTimeSettingDetails().get("urgeTimes") != null) ? taskDefinition.getTimeSettingDetails().get("urgeTimes") : "0"));
    task.setFrequenceInterval(Integer.parseInt((taskDefinition.getTimeSettingDetails().get("frequence") != null) ? taskDefinition.getTimeSettingDetails().get("frequence") : "0"));
    task.setUndealOperation(taskDefinition.getTimeSettingDetails().get("dealIfTimeout"));
    task.setNotificationType(taskDefinition.getTimeSettingDetails().get("notificationType"));
    
    // All properties set, now firing 'create' event
    task.fireEvent(TaskListener.EVENTNAME_CREATE);
  }

  public void signal(ActivityExecution execution, String signalName, Object signalData) throws Exception {
    leave(execution);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected void handleAssignments(TaskEntity task, ActivityExecution execution) {
	
    if (taskDefinition.getAssigneeExpression() != null) {    	
      task.setAssignee((String) taskDefinition.getAssigneeExpression().getValue(execution));
    }
    // commenting to avoid group id entry in runtime idendity link table
    /*if (!taskDefinition.getCandidateGroupIdExpressions().isEmpty()) {
      for (Expression groupIdExpr : taskDefinition.getCandidateGroupIdExpressions()) {
    	 
        Object value = groupIdExpr.getValue(execution);
        if (value instanceof String) {
          List<String> candiates = extractCandidates((String) value);
          task.addCandidateGroups(candiates);
        } else if (value instanceof Collection) {
          task.addCandidateGroups((Collection) value);
        } else {
          throw new ActivitiException("Expression did not resolve to a string or collection of strings");
        }
      }
    }*/
    
    // Back - return
    // Return - backward jump
    // Jump - forward jump
    if(execution.isReturn()){
    	String previousAssignee = task.getPreviousTaskAssignee();
        	
         int expressionSize = taskDefinition.getCandidateUserIdExpressions().size();
         Expression skipRepeatedExpr =  taskDefinition.getSkipRepeatedExpression();
         String skipRepeat = (String) skipRepeatedExpr.getValue(execution);
    	List<String> candiateUserIdExpression = Context.getCommandContext().getHistoricTaskInstanceEntityManager().
    					getOrganizerIdentityLinksDetails(execution.getProcessInstanceId(), taskDefinition.getKey());
    	if(candiateUserIdExpression != null){
        	for (String value: candiateUserIdExpression) {
            	List<String> candiates = extractCandidates(value);
            	task.addCandidateUsers(candiates,expressionSize,previousAssignee,skipRepeat);
        	}
    	}else {
    		throw new ActivitiException("Problem in getting candidate users for returning task");
    	}
    }
    
    //Set the next dynamic organizer
    if (taskDefinition.getDynamicOrganizer() != null) {
    	Expression dynamicNextOrganizer =  taskDefinition.getDynamicOrganizer();
    	 String dynamicNextOrg = (String) dynamicNextOrganizer.getValue(execution);
    	 task.setDynamicNextOrganizer(dynamicNextOrg);
    	//Set the next dynamic organizer Type
    	 Expression dynamicNextOrganizerType =  taskDefinition.getDynamicOrganizerType();
    	 if(dynamicNextOrganizerType != null){
	    	 String dynamicNextOrgType = (String) dynamicNextOrganizerType.getValue(execution);
	    	 task.setDynamicOrganizerType(dynamicNextOrgType);
    	 }
    	
    }
    //Set the next dynamic reader
    if (taskDefinition.getDynamicReader()!= null) {
    	Expression dynamicNextReader =  taskDefinition.getDynamicReader();
    	 String dynamicNextRed = (String) dynamicNextReader.getValue(execution);
    	 task.setDynamicNextReader(dynamicNextRed);
    	//Set the next dynamic Reader Type
    	 Expression dynamicNextReaderType =  taskDefinition.getDynamicReaderType();
    	 if(dynamicNextReaderType!=null){
	    	 String dynamicNextRedType = (String) dynamicNextReaderType.getValue(execution);
	    	 task.setDynamicOrganizerType(dynamicNextRedType);
    	 }
    	
    }
    
    if (!taskDefinition.getReaderGroupIdExpressions().isEmpty()) {
        for (Expression groupIdExpr : taskDefinition.getReaderGroupIdExpressions()) {
          Object value = groupIdExpr.getValue(execution);
          if (value instanceof String) {
            List<String> readers = extractCandidates((String) value);
            task.addReaderGroups(readers);
          } else if (value instanceof Collection) {
            task.addReaderGroups((Collection) value);
          } else {
            throw new ActivitiException("Expression did not resolve to a string or collection of strings");
          }
        }
      }

      if (!taskDefinition.getReaderUserIdExpressions().isEmpty()) {
        for (Expression userIdExpr : taskDefinition.getReaderUserIdExpressions()) {
          Object value = userIdExpr.getValue(execution);
          if (value instanceof String) {
            List<String> readers = extractCandidates((String) value);
            task.addReaderUsers(readers);
          } else if (value instanceof Collection) {
            task.addReaderUsers((Collection) value);
          } else {
            throw new ActivitiException("Expression did not resolve to a string or collection of strings");
          }
        }
      }
      
      if (!taskDefinition.getAdminUserIdExpressions().isEmpty()) {
          for (Expression userIdExpr : taskDefinition.getAdminUserIdExpressions()) {
            Object value = userIdExpr.getValue(execution);
            if (value instanceof String) {
              List<String> workflowAdmins = extractCandidates((String) value);
              task.addAdminUsers(workflowAdmins);
            } else if (value instanceof Collection) {
              task.addAdminUsers((Collection) value);
            } else {
              throw new ActivitiException("Expression did not resolve to a string or collection of strings");
            }
          }
        }
      
      if (!taskDefinition.getAdminGroupIdExpression().isEmpty()) {
          for (Expression userIdExpr : taskDefinition.getAdminGroupIdExpression()) {
            Object value = userIdExpr.getValue(execution);
            if (value instanceof String) {
              List<String> workflowAdmins = extractCandidates((String) value);
              task.addAdminUsersGroups(workflowAdmins);
            } else if (value instanceof Collection) {
              task.addAdminUsersGroups((Collection) value);
            } else {
              throw new ActivitiException("Expression did not resolve to a string or collection of strings");
            }
          }
        }
      // check skip empty,if it set true then task will be completed
      if(taskDefinition.getSkipEmptyExpression() != null){
	      Expression skipEmptyExpr =  taskDefinition.getSkipEmptyExpression();
	      String skipEmpty = (String) skipEmptyExpr.getValue(execution);
	      if(skipEmpty.equalsIgnoreCase("true")){
	    	  completeTaskForSkipEmpty(task);
	      }
      }
      
  }
  
  //Complete the task for skip empty
  private void completeTaskForSkipEmpty(TaskEntity task){	  
	  if((task.getIdentityLinks() == null || task.getIdentityLinks().size() == 0)){
    	  task.complete();
      }
  }

  /**
   * Extract a candidate list from a string. 
   * 
   * @param str
   * @return 
   */
  protected List<String> extractCandidates(String str) {
    return Arrays.asList(str.split("[\\s]*,[\\s]*"));
  }
  
  // getters and setters //////////////////////////////////////////////////////
  
  public TaskDefinition getTaskDefinition() {
    return taskDefinition;
  }
  
}
