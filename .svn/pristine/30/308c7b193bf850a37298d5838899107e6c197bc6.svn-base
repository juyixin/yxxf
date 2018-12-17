package org.activiti.engine.impl;

import java.util.List;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.task.IdentityLink;

public class GetIdentityLinksForCurrentTaskByInstanceId implements
		Command<List<IdentityLink>> {
	
	protected String processInstanceId;

	  public GetIdentityLinksForCurrentTaskByInstanceId(String processInstanceId) {
		    this.processInstanceId = processInstanceId;
		  }
	  
	@Override
	public List<IdentityLink> execute(CommandContext commandContext) {
		
		if(!processInstanceId.isEmpty()) {
	    	return Context
	    	        .getCommandContext()
	    	        .getTaskEntityManager().findIdentityLinksForTaskByInstanceId(processInstanceId);
		}
		
		
		// TODO Auto-generated method stub
		return null;
	}

}
