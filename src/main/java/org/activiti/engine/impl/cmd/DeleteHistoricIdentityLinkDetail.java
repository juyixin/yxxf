package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;


public class DeleteHistoricIdentityLinkDetail implements Command<Void>, Serializable {
	  
	  private static final long serialVersionUID = 1L;
	protected String taskId;
	protected String userId;
	 public DeleteHistoricIdentityLinkDetail(String taskId,String userId) {
		    this.taskId = taskId;
		    this.userId = userId;
	  }
	 
	 public Void execute(CommandContext commandContext) {
		 if(taskId !=null){
				Map<String,String> parameter = new HashMap<String,String>();
				parameter.put("taskId", taskId);
				parameter.put("userId", userId);
				 commandContext.getDbSqlSession().delete("deleteHistoricIdentityLinkDetail", parameter);
				
			  
		  }else{
	      throw new ActivitiException("Cannot find taskId " + taskId);
	    }
		return null;
		    
		  }
}
	 