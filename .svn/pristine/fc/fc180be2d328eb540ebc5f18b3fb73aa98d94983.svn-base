package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.repository.ProcessDefinition;

public class GetProcessDefinitionByKeyCmd implements Command<List<ProcessDefinition>>, Serializable {
	private static final long serialVersionUID = 1L;
	  protected String key;
	  protected List<String> processDefinitionKeys = null;

	  public GetProcessDefinitionByKeyCmd(String key) {
	    this.key = key;
	  }
	  public GetProcessDefinitionByKeyCmd(String key,List<String> processDefinitionKeys) {
		    this.key = key;
		    this.processDefinitionKeys = processDefinitionKeys;
		  }
	   
	  public List<ProcessDefinition> execute(CommandContext commandContext) {
		  List<ProcessDefinition> processDefinition = null;
		  if(processDefinitionKeys !=null){
			  processDefinition = Context
		        .getCommandContext()
		        .getProcessDefinitionEntityManager().findProcessDefinitionByKeys(processDefinitionKeys);
		  }else{
			   processDefinition = Context
				        .getCommandContext()
				        .getProcessDefinitionEntityManager().findProcessDefinitionByKey(key);
		  }
	      
	    if (processDefinition == null && processDefinitionKeys == null) {
	      throw new ActivitiException("Cannot find process definition with process key " + key);
	    }else if(processDefinition == null && processDefinitionKeys != null){
	    	throw new ActivitiException("Cannot find process definition fory " + processDefinitionKeys);
	    }
	    
	    return processDefinition;
	  }

}
