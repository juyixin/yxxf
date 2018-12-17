package org.activiti.engine.impl;

import java.util.List;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import com.eazytec.bpm.common.util.StringUtil;
/**
 * This class returns the organizers of all completed tasks in a process 
 *
 */
public class GetHistoricOrganizers implements Command<List<String>> {

	
	protected String taskDefinitionKey;
	
	  public GetHistoricOrganizers(String taskDefinitionKey){
		  this.taskDefinitionKey = taskDefinitionKey ;
	  }
	@Override
	public List<String> execute(CommandContext commandContext) {
		if(!StringUtil.isEmptyString(taskDefinitionKey)){
			return commandContext.getHistoricTaskInstanceEntityManager().getHistoricOrganizers(taskDefinitionKey);
		}
		return null;
	}

}
