package org.activiti.engine.impl.persistence.entity;

import java.io.Serializable;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.PersistentObject;


public class HistoricIdentityLinkDetailEntity implements Serializable,PersistentObject {
	  
	  private static final long serialVersionUID = 1L;
	  
	  private String id;
	  private String taskId;
	  private String userId;
	  private boolean isRead;
	  private String proInsId;
	  
	  public String getProInsId() {
		return proInsId;
	}
	public void setProInsId(String proInsId) {
		this.proInsId = proInsId;
	}
	public boolean getIsRead() {
		return isRead;
	}
	public void setIsRead(boolean isRead) {
		this.isRead = isRead;
	}
	
	  public static HistoricIdentityLinkDetailEntity createAndInsert(HistoricIdentityLinkDetailEntity historicIdentityLinkDetailEntity) {
		  
	    Context
	      .getCommandContext()
	      .getDbSqlSession()
	      .insert(historicIdentityLinkDetailEntity);
	    return historicIdentityLinkDetailEntity;
	  }
	  
	  
	  public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public Object getPersistentState() {
		// TODO Auto-generated method stub
		return null;
	}
	



}
