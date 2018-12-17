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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.runtime.Execution;

import com.eazytec.bpm.engine.TransactorType;


/**
 * @author Tom Baeyens
 */
public class HistoricTaskInstanceEntity extends HistoricScopeInstanceEntity implements HistoricTaskInstance, PersistentObject {

  private static final long serialVersionUID = 1L;
  
  protected String executionId;
  protected String name;
  protected String parentTaskId;
  protected String description;
  protected String owner;
  protected String assignee;
  protected String taskDefinitionKey;
  protected int priority;
  protected Date dueDate;
  protected TaskDefinition taskDefinition; //Added by madan temporarily
  protected ExecutionEntity execution;
  protected ExecutionEntity processInstance;
  protected int signOffType = TransactorType.SINGLE_PLAYER_SINGLE_SIGNOFF.getStateCode();
  protected boolean isForStartNodeTask;
  protected boolean isDratf;
  protected Date createTime; // task started time.
  protected String taskId; // task id
  protected String taskType;
  protected String assigneeFullName;
  protected String lastOperationPerformed;

  
  
	public String getLastOperationPerformed() {
		return lastOperationPerformed;
	}

	public void setLastOperationPerformed(String lastOperationPerformed) {
		this.lastOperationPerformed = lastOperationPerformed;
	}

	public String getAssigneeFullName() {
		return assigneeFullName;
	}

	public void setAssigneeFullName(String assigneeFullName) {
		this.assigneeFullName = assigneeFullName;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	protected String organizer; // added to get the organizer who returned the
								// task.

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}
public boolean getIsDratf() {
		return isDratf;
	}
	public void setIsDratf(boolean isDratf) {
		this.isDratf = isDratf;
	}
	public boolean getIsForStartNodeTask() {
		return isForStartNodeTask;
	}
	public void setIsForStartNodeTask(boolean isForStartNodeTask) {
		this.isForStartNodeTask = isForStartNodeTask;
	}

  public ExecutionEntity getProcessInstance() {
	return processInstance;
}

public void setProcessInstance(ExecutionEntity processInstance) {
	this.processInstance = processInstance;
}

public HistoricTaskInstanceEntity() {
  }

  public HistoricTaskInstanceEntity(TaskEntity task, ExecutionEntity execution) {
    this.id = task.getId();
    if (execution!=null) {
    	if(execution.getActivity().getProperty("processDefinition") != null) {
    	      this.processDefinitionId = (String) execution.getActivity().getProperty("processDefinition");
    	} else {
    	      this.processDefinitionId = execution.getProcessDefinitionId();
    	}
      this.processInstanceId = execution.getProcessInstanceId();
      this.executionId = execution.getId();
      this.processInstance = execution.getProcessInstance();
    }
    this.name = task.getName();
    this.parentTaskId = task.getParentTaskId();
    this.description = task.getDescription();
    this.owner = task.getOwner();
    this.assignee = task.getAssignee();
    this.startTime = ClockUtil.getCurrentTime();
    this.taskDefinitionKey = task.getTaskDefinitionKey();
    this.setPriority(task.getPriority());
    this.isForStartNodeTask = task.getIsForStartNodeTask();
    this.taskType = task.getTaskType();
  }

  // persistence //////////////////////////////////////////////////////////////
  
  public Object getPersistentState() {
    Map<String, Object> persistentState = new HashMap<String, Object>();
    persistentState.put("name", name);
    persistentState.put("owner", owner);
    persistentState.put("assignee", assignee);
    persistentState.put("endTime", endTime);
    persistentState.put("durationInMillis", durationInMillis);
    persistentState.put("description", description);
    persistentState.put("deleteReason", deleteReason);
    persistentState.put("taskDefinitionKey", taskDefinitionKey);
    persistentState.put("priority", priority);
    if(parentTaskId != null) {
      persistentState.put("parentTaskId", parentTaskId);
    }
    if(dueDate != null) {
      persistentState.put("dueDate", dueDate);
    }
    persistentState.put("taskType", taskType);
    return persistentState;
  }

  // getters and setters //////////////////////////////////////////////////////
  public String getExecutionId() {
    return executionId;
  }
  public void setExecutionId(String executionId) {
    this.executionId = executionId;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getAssignee() {
    return assignee;
  }
  public void setAssignee(String assignee) {
    this.assignee = assignee;
  }
  public String getTaskDefinitionKey() {
    return taskDefinitionKey;
  }
  public void setTaskDefinitionKey(String taskDefinitionKey) {
    this.taskDefinitionKey = taskDefinitionKey;
  }
  public int getPriority() {
    return priority;
  }
  public void setPriority(int priority) {
    this.priority = priority;
  }
  public Date getDueDate() {
    return dueDate;
  }
  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }
  public String getOwner() {
    return owner;
  }
  public void setOwner(String owner) {
    this.owner = owner;
  }
  public String getParentTaskId() {
    return parentTaskId;
  }
  public void setParentTaskId(String parentTaskId) {
    this.parentTaskId = parentTaskId;
  }
  public TaskDefinition getTaskDefinition() {
	    if (taskDefinition==null && taskDefinitionKey!=null) {
	      ProcessDefinitionEntity processDefinition = Context
	        .getProcessEngineConfiguration()
	        .getDeploymentManager()
	        .findDeployedProcessDefinitionById(processDefinitionId);
	      taskDefinition = processDefinition.getTaskDefinitions().get(taskDefinitionKey);
	    }
	    return taskDefinition;
	  }
  
  public ExecutionEntity getExecution() {
	    if ( (execution==null) && (executionId!=null) ) {
	      this.execution = Context
	        .getCommandContext()
	        .getExecutionEntityManager()
	        .findExecutionById(executionId);
	    }
	    return execution;
	  }
  
  public int getSignOffType() {
		return signOffType;
	}

	public void setSignOffType(int signOffType) {
		this.signOffType = signOffType;
	}
}