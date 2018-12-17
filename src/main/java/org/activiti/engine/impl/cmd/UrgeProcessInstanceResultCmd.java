package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

public class UrgeProcessInstanceResultCmd implements Command<List<Map<String, Object>>>, Serializable {

	 private static final long serialVersionUID = 1L;
	  
	  protected String taskId;
	  
	  public UrgeProcessInstanceResultCmd(String taskId) {
	    this.taskId = taskId;
	  } 

	@Override
	public List<Map<String, Object>> execute(CommandContext commandContext) {
		List<Map<String,Object>> urgeDetailsMap = null;
		urgeDetailsMap=Context.getCommandContext().getHistoricProcessInstanceEntityManager().selectUrgeProcessInstanceResultMap(taskId);
		return urgeDetailsMap;
	}
}
