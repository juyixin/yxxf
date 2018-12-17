package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;

public class GetIdentityLinkCmd implements Command<List<IdentityLinkEntity>>, Serializable{
	 
	private static final long serialVersionUID = 1L;
	protected String taskId;
	protected boolean isCreatorType = false;
	 public GetIdentityLinkCmd(String taskId,boolean isCreatorType) {
		    this.taskId = taskId;
		    this.isCreatorType = isCreatorType;
	  }
	 public List<IdentityLinkEntity> execute(CommandContext commandContext) {
		 List<IdentityLinkEntity> idenTityLinks = null; 
		 
		  if(taskId !=null){
			  if(!isCreatorType){
				   idenTityLinks = Context
					        .getCommandContext()
					        .getIdentityLinkEntityManager()
					        .findIdentityLinksByTaskId(taskId);
			  }else{
				  idenTityLinks = Context
					        .getCommandContext()
					        .getIdentityLinkEntityManager()
					        .findCreatorIdentityLink(taskId);
			  }
		  }else{
	      throw new ActivitiException("Cannot find taskId " + taskId);
	    }
	    return idenTityLinks;
	  }


}
