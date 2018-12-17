package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.task.CustomOperatingFunction;

public class SaveCustomOperatingFunctionCmd implements Command<CustomOperatingFunction>, Serializable{
	CustomOperatingFunction customOperatingFunction;
	boolean isCustomOperatingUpdate=false;
	public SaveCustomOperatingFunctionCmd(CustomOperatingFunction customOperatingFunction,boolean isCustomOperatingUpdate){
		this.customOperatingFunction=customOperatingFunction;
		this.isCustomOperatingUpdate=isCustomOperatingUpdate;
	}
	@Override
	public CustomOperatingFunction execute(CommandContext commandContext) {
		// TODO Auto-generated method stub
		return saveCustomOperatingFunction();
		
	}
	
	protected CustomOperatingFunction saveCustomOperatingFunction(){
		 return Context.getCommandContext().getCustomOperatingFunctionManager().saveCustomOperatingFunction(customOperatingFunction,isCustomOperatingUpdate);
	}
}
