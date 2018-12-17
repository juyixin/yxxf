package org.activiti.engine.impl.cmd;

import java.io.Serializable;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.task.CustomOperatingFunction;

public class DeleteCustomOperatingFunction implements Command<Void>, Serializable{
	CustomOperatingFunction customOperatingFunction=null;
	public DeleteCustomOperatingFunction(CustomOperatingFunction customOperatingFunction){
		this.customOperatingFunction=customOperatingFunction;
	}
	
	@Override
	public Void execute(CommandContext commandContext) {
		// TODO Auto-generated method stub
		deleteCustomFunction();
		return null;
	}
	
	protected void deleteCustomFunction(){
		 Context.getCommandContext().getCustomOperatingFunctionManager().deleteCustomFunctionById(customOperatingFunction);
	}
}
