package org.activiti.engine.impl.cmd;

import java.io.Serializable;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.task.CustomOperatingFunction;

public class GetCustomOperatingByIdCmd implements Command<CustomOperatingFunction>, Serializable{
	String customOperatingFunctionId;
	public GetCustomOperatingByIdCmd(String customOperatingFunctionId){
		this.customOperatingFunctionId=customOperatingFunctionId;
	}
	
	@Override
	public CustomOperatingFunction execute(CommandContext commandContext) {
		// TODO Auto-generated method stub
		return getCustomOperatingById();
		
	}
	
	protected CustomOperatingFunction getCustomOperatingById(){
		 return Context.getCommandContext().getCustomOperatingFunctionManager().findOperatingFunctionById(customOperatingFunctionId);
	}
}
