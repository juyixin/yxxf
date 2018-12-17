package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

public class GetHistoricDetailCmd implements Command<List<Map<String,Object>>>, Serializable{
	 
	private static final long serialVersionUID = 1L;
	protected String processInstanceId;
	protected String formId;
	 public GetHistoricDetailCmd(String processInstanceId,String formId) {
	    this.processInstanceId = processInstanceId;
	    this.formId = formId;
	 }
	 public List<Map<String,Object>> execute(CommandContext commandContext) {
		 List<Map<String,Object>> historicDetailsMap = null; 
		  if(processInstanceId !=null){
			  if(formId != null){
				  historicDetailsMap = Context
					        .getCommandContext()
					        .getHistoricDetailEntityManager()
					        .selectHistoricDetailsAsMap(processInstanceId,formId);
			  }else{
				  historicDetailsMap = Context
					        .getCommandContext()
					        .getHistoricDetailEntityManager()
					        .selectHistoricDetailsForInstance(processInstanceId);
			  }
		  }else{
			  throw new ActivitiException("Cannot get historic detail for the process instance " + processInstanceId);
		  }
	    return historicDetailsMap;
	  }


}
