package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

public class UpdateHistoricIdentityLinkDetail implements Command<Void>, Serializable {
	  
	  private static final long serialVersionUID = 1L;
	protected String taskId = null;
	protected String proInsId = null;
	protected String userId = null;
	protected boolean isRead = false;
	
	 public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProInsId() {
		return proInsId;
	}

	public void setProInsId(String proInsId) {
		this.proInsId = proInsId;
	}

	public UpdateHistoricIdentityLinkDetail(String proInsId,String userId,boolean isRead) {
		    this.proInsId = proInsId;
		    this.userId = userId;
		    this.isRead = isRead;
	  }
	
	
	 
	 public Void execute(CommandContext commandContext) {
		 Map<String, Object> parameters = new HashMap<String, Object>();
		 parameters.put("proInsId", proInsId);
		 parameters.put("userId", "'"+userId+"'");
		 parameters.put("read", isRead);
		 commandContext.getDbSqlSession().selectList("updateIdentityDetailValues", parameters);
		return null;
		    
		  }
	 


}
