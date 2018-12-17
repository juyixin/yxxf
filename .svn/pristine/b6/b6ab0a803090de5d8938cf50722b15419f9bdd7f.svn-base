package org.activiti.engine.impl.cmd;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

public class ClearProcessDefinitionsFromCacheCmd implements Command<Void> {

	public ClearProcessDefinitionsFromCacheCmd() {
		// TODO Auto-generated constructor stub
	}
	
	public Void execute(CommandContext commandContext) {
		clearProcessDefinitionsFromCache(commandContext);
		return null;
	  }

	protected void clearProcessDefinitionsFromCache(CommandContext commandContext) {
		 Context
	        .getProcessEngineConfiguration()
	        .getDeploymentManager()
	        .getProcessDefinitionCache().clear();
	}  

}
