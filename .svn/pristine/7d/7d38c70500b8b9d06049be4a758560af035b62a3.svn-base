package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.repository.ProcessDefinition;

import com.eazytec.exceptions.BpmException;


/**
 * <p>Commands the deletion of process definition(s) entity</p>
 * 
 * @author madan
 */
public class DeleteProcessDefinitionCmd implements Command<Void>, Serializable {
  
  private static final long serialVersionUID = 1L;
  protected String processDefinitionId;
  protected List<ProcessDefinition>processDefinitions;
  protected String deleteReason;
  protected boolean cascade;

  public DeleteProcessDefinitionCmd(String processDefinitionId, String deleteReason, boolean cascade) {
    this.processDefinitionId = processDefinitionId;
    this.deleteReason = deleteReason;
    this.cascade = cascade;
    this.processDefinitions=null;
  }
  
  public DeleteProcessDefinitionCmd(List<ProcessDefinition>processDefinitions, String deleteReason, boolean cascade) {
	    this.processDefinitions = processDefinitions;
	    this.deleteReason = deleteReason;
	    this.cascade = cascade;
	    this.processDefinitionId = null;
  }

  public Void execute(CommandContext commandContext) { 
	  try{
		  if(processDefinitionId == null) {
			  if(processDefinitions!=null){
				  if(processDefinitions.size()>0){
					  commandContext.getProcessDefinitionEntityManager().deleteProcessDefinition(processDefinitions, deleteReason, cascade);
					  return null;
				  }else{
					  throw new BpmException("Empty process definitions");
				  }
			  }else{
				  throw new BpmException("processDefinitionId is null and no process definitions also found");
			  }	      
		  }else{
			  commandContext.getProcessDefinitionEntityManager().deleteProcessDefinition(processDefinitionId, deleteReason, cascade);
			  return null;
		  }	
	  }catch(ActivitiException e){
		  throw new BpmException(e.getMessage(), e);
	  }	      
  }
}
