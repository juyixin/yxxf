package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import com.eazytec.bpm.common.util.I18nUtil;
import com.eazytec.exceptions.BpmException;

/**
 * @author madan
 */
public class GetHistoricvariableInstanceCmd implements Command<List<HistoricVariableInstance>>, Serializable {

  private static final long serialVersionUID = 1L;
  protected String processInstanceId;
  protected List<String> processInstanceIds;
  protected String currentTaskId;
  boolean selectCurrent;

  public GetHistoricvariableInstanceCmd(String processInstanceId) {
    this.processInstanceId = processInstanceId;
  }
  
  public GetHistoricvariableInstanceCmd(List<String> processInstanceIds) {
	    this.processInstanceIds = processInstanceIds;
	  }
  
  public GetHistoricvariableInstanceCmd(String processInstanceId, String currentTaskId, boolean selectCurrent) {
	    this.processInstanceId = processInstanceId;
	    this.currentTaskId = currentTaskId;
	    this.selectCurrent = selectCurrent;
	  }

  public List<HistoricVariableInstance> execute(CommandContext commandContext) {    
    if(processInstanceId!=null){
    	if(currentTaskId!=null){
        	try{
        		Map<String, Object>parameters = new HashMap<String, Object>();
        		
            	parameters.put("processInstanceId", processInstanceId);
            	parameters.put("currentTaskId", currentTaskId);
            	if(!selectCurrent){
            		return commandContext
              		      .getHistoricVariableInstanceEntityManager().findPrevHistoricVariableInstancesByInstanceId(parameters);
            	}else{
            		return commandContext
              		      .getHistoricVariableInstanceEntityManager().findHistoricVariableInstancesByTaskId(currentTaskId);
            	}
            	
        	}catch(BpmException e){
        		throw new BpmException(e.getMessage(), e);
        	}
    	}else{
    		return commandContext
  			      .getHistoricVariableInstanceEntityManager().findHistoricVariableInstanceByProcessInstanceId(processInstanceId);
    	}    
    	
    }else if(processInstanceIds!=null && processInstanceIds.size()>0){
    		return commandContext
  			      .getHistoricVariableInstanceEntityManager().findHistoricVariableInstanceByProcessInstanceIds(processInstanceIds);
    }else{
			throw new ActivitiException(I18nUtil.getMessageProperty("error.processInstance.notFound ")+" Task Id : "+currentTaskId);      			
    }    
  }

}
