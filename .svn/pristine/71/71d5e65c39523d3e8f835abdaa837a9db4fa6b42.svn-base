package org.activiti.engine.impl.persistence.entity;

import java.io.Serializable;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.task.IdentityLink;
/**
 * This class is to maintain historic identity link
 * @author vinoth
 *
 */
public class HistoricIdentityLinkEntity implements Serializable,  PersistentObject {
	  
	  private static final long serialVersionUID = 1L;
	  
	  protected String id;
	  
	  protected String type;
	  
	  protected String userId;
	  
	  protected String groupId;
	  
	  protected String taskId;
	  
	  protected String processDefId;
	  
	  protected int order=0;
	  
	  protected String groupType;
	  
	  protected TaskEntity task;
	  
	  protected ProcessDefinitionEntity processDef;
	  
	  protected String identityLinkEntityId;
	  
	  protected String executionId;
	  
	  protected String isEndEvent;

	  public Object getPersistentState() {
	    return this.type;
	  }
	  
	  public HistoricIdentityLinkEntity(IdentityLinkEntity identityLinkEntity){		  
		  this.type=identityLinkEntity.getType();
		  this.userId=identityLinkEntity.getUserId();
		  this.groupId=identityLinkEntity.getGroupId();
		  this.taskId=identityLinkEntity.getTaskId();
		  this.processDefId=identityLinkEntity.getProcessDefId();
		  this.order=identityLinkEntity.getOrder();
		  this.groupType=identityLinkEntity.getGroupType();
		  this.task=identityLinkEntity.getTask();
		  this.processDef=identityLinkEntity.getProcessDef();
		  this.identityLinkEntityId=identityLinkEntity.getId();
	  }
	  public HistoricIdentityLinkEntity(){
	  }
	  public static HistoricIdentityLinkEntity createAndInsert(IdentityLinkEntity identityLinkEntity) {
		  
		HistoricIdentityLinkEntity historicIdentityLinkEntity = new HistoricIdentityLinkEntity(identityLinkEntity);
	    Context
	      .getCommandContext()
	      .getDbSqlSession()
	      .insert(historicIdentityLinkEntity);
	    return historicIdentityLinkEntity;
	  }
	  
	  public static HistoricIdentityLinkEntity createAndInsert(HistoricIdentityLinkEntity historicIdentityLinkEntity) {
		    Context
		      .getCommandContext()
		      .getDbSqlSession()
		      .insert(historicIdentityLinkEntity);
		    return historicIdentityLinkEntity;
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

    public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
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

	public String getIdentityLinkEntityId() {
		return identityLinkEntityId;
	}

	public void setIdentityLinkEntityId(String identityLinkEntityId) {
		this.identityLinkEntityId = identityLinkEntityId;
	}

	public String getIsEndEvent() {
		return isEndEvent;
	}

	public void setIsEndEvent(String isEndEvent) {
		this.isEndEvent = isEndEvent;
	}
	  
	  

	  
}
