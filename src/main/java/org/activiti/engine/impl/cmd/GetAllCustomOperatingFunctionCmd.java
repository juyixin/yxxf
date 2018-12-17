package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.task.CustomOperatingFunction;
import org.activiti.engine.repository.ProcessDefinition;

public class GetAllCustomOperatingFunctionCmd implements Command<List<CustomOperatingFunction>>, Serializable{

	public GetAllCustomOperatingFunctionCmd() {
		// TODO Auto-generated constructor stub
	}
	
	public List<CustomOperatingFunction> execute(CommandContext commandContext) {
		return clearProcessDefinitionsFromCache(commandContext);
	  }

	protected List<CustomOperatingFunction> clearProcessDefinitionsFromCache(CommandContext commandContext) {
		return Context.getCommandContext().getCustomOperatingFunctionManager().selectAllCustomOperations();
	}  
}
