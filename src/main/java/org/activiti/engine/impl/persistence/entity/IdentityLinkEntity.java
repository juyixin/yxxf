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

import java.io.Serializable;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.task.IdentityLink;


/**
 * @author Joram Barrez
 */
public class IdentityLinkEntity implements Serializable, IdentityLink, PersistentObject {
  
  private static final long serialVersionUID = 1L;
  
  protected String id;
  
  protected String type;
  
  protected String userId;
  
  protected String groupId;
  
  protected String taskId;
  
  protected String processDefId;
  
  protected int order=1;
  
  protected String parentId;
  
  protected String operationType;
  
  protected String processInstanceId;
  
  public IdentityLinkEntity(String type, String userId, String groupId, String processInstanceId ,int order){
	  this.type = type;
	  this.userId = userId;
	  this.groupId = groupId;
	  this.processInstanceId = processInstanceId;
	  this.order = order;
  }
  
  public IdentityLinkEntity(){
//	  this.type = type;
//	  this.userId = userId;
//	  this.groupId = groupId;
//	  this.processInstanceId = processInstanceId;
//	  this.order = order;
  }
  
  public String getParentId() {
	return parentId;
}

public void setParentId(String parentId) {
	this.parentId = parentId;
}

protected String groupType;
  
  protected TaskEntity task;
  
  protected ProcessDefinitionEntity processDef;

  public Object getPersistentState() {
    return this.type;
  }
  
  public static IdentityLinkEntity createAndInsert() {
	  
    IdentityLinkEntity identityLinkEntity = new IdentityLinkEntity();
    Context
      .getCommandContext()
      .getDbSqlSession()
      .insert(identityLinkEntity);
    return identityLinkEntity;
  }
  
  public boolean isUser() {
    return userId != null;
  }
  
  public boolean isGroup() {
    return groupId != null;
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public int getOrder() {
	return order;
}

public void setOrder(int order) {
	this.order = order;
}

public String getType() {
    return type;
  }
  
  public void setType(String type) {
    this.type = type;
  }

  public String getUserId() {
    return userId;
  }
  
  public void setUserId(String userId) {
    if (this.groupId != null && userId != null) {
      throw new ActivitiException("Cannot assign a userId to a task assignment that already has a groupId");
    }
    this.userId = userId;
  }
  
  public String getGroupId() {
    return groupId;
  }
  
  public void setGroupId(String groupId) {
    if (this.userId != null && groupId != null) {
      throw new ActivitiException("Cannot assign a groupId to a task assignment that already has a userId");
    }
    this.groupId = groupId;
  }
  
  public String getTaskId() {
    return taskId;
  }

  void setTaskId(String taskId) {
    this.taskId = taskId;
  }
    
  public String getProcessDefId() {
    return processDefId;
  }
  
  public void setProcessDefId(String processDefId) {
    this.processDefId = processDefId;
  }

  public TaskEntity getTask() {
    if ( (task==null) && (taskId!=null) ) {
      this.task = Context
        .getCommandContext()
        .getTaskEntityManager()
        .findTaskById(taskId);
    }
    return task;
  }
  
  public void setTask(TaskEntity task) {
    this.task = task;
    this.taskId = task.getId();
  }

  public ProcessDefinitionEntity getProcessDef() {
    if ((processDef == null) && (processDefId != null)) {
      this.processDef = Context
              .getCommandContext()
              .getProcessDefinitionEntityManager()
              .findLatestProcessDefinitionById(processDefId);
    }
    return processDef;
  }
  
  public void setProcessDef(ProcessDefinitionEntity processDef) {
    this.processDef = processDef;
    this.processDefId = processDef.getId();
  }

	public String getGroupType() {
		return groupType;
	}
	
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
}
