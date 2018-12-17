package com.eazytec.bpm.engine.task.impl;

import java.io.Serializable;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

import com.eazytec.bpm.engine.task.TransactorIdentityLink;
import com.eazytec.exceptions.BpmException;


/**
 * @author madan
 */
public class TransactorIdentityLinkEntity implements Serializable, TransactorIdentityLink, PersistentObject {
  
  private static final long serialVersionUID = 1L;
  
  protected String id;
  
  protected int type;
  
  protected String userId;
  
  protected String groupId;
  
  protected String taskId;
  
  protected String processDefId;
  
  protected TaskEntity task;
  
  protected int order;
  
  protected boolean isSignedOff = false;
  
  protected ProcessDefinitionEntity processDef;

  public Object getPersistentState() {
    return this.type;
  }
  
  public static TransactorIdentityLinkEntity createAndInsert() {
    TransactorIdentityLinkEntity identityLinkEntity = new TransactorIdentityLinkEntity();
    Context
      .getCommandContext()
      .getDbSqlSession()
      .insert(identityLinkEntity);
    return identityLinkEntity;
  }
  
  public void signedOff(){
	  this.isSignedOff=true;
	  Context
      .getCommandContext()
      .getDbSqlSession().update(this);
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
  
  public int getType() {
    return type;
  }
  
  public void setType(int type) {
    this.type = type;
  }

  public String getUserId() {
    return userId;
  }
  
  public void setUserId(String userId) {
    if (this.groupId != null && userId != null) {
      throw new BpmException("Cannot assign a userId to a task assignment that already has a groupId");
    }
    this.userId = userId;
  }
  
  public String getGroupId() {
    return groupId;
  }
  
  public void setGroupId(String groupId) {
    if (this.userId != null && groupId != null) {
      throw new BpmException("Cannot assign a groupId to a task assignment that already has a userId");
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

	public int getOrder() {
		return order;
	}
	
	public void setOrder(int order) {
		this.order = order;
	}

	public boolean isSignedOff() {
		return isSignedOff;
	}

	public void setSignedOff(boolean isSignedOff) {
		this.isSignedOff = isSignedOff;
	}
  
}
