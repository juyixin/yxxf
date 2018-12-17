package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.repository.ProcessDefinition;

import com.eazytec.exceptions.BpmException;

/**
 * <p>Commands the deletion of a process definition(S) from the cache
 * 
 * @author madan
 */
public class DeleteProcessDefinitionCacheCmd implements Command<Void>, Serializable {

  private static final long serialVersionUID = 1L;
  protected String processDefinitionId;
  protected List<ProcessDefinition>processDefinitions;

  public DeleteProcessDefinitionCacheCmd(String processDefinitionId) {
    this.processDefinitionId = processDefinitionId;
    this.processDefinitions=null;
  }
  
  public DeleteProcessDefinitionCacheCmd(List<ProcessDefinition>processDefinitions) {
	    this.processDefinitionId = null;
	    this.processDefinitions=processDefinitions;
  }

  public Void execute(CommandContext commandContext) {
	  try{
		  if(processDefinitionId == null) {
			  if(processDefinitions!=null){
				  if(processDefinitions.size()>0){
					  Context
				      .getProcessEngineConfiguration()
				      .getDeploymentManager().removeDefinitionsFromCache(processDefinitions);
					  return null;
				  }else{
					  throw new BpmException("Empty process definitions");
				  }
			  }else{
				  throw new BpmException("processDefinitionId is null and no process definitions also found");
			  }	      
		  }else{
			  Context
		      .getProcessEngineConfiguration()
		      .getDeploymentManager().removeDefinitionFromCache(processDefinitionId);
			  return null;
		  }	
	  }catch(ActivitiException e){
		  throw new BpmException(e.getMessage(), e);
	  }     
  }
}
