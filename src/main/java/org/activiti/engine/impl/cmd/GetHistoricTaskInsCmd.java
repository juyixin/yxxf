package org.activiti.engine.impl.cmd;
import java.io.Serializable;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.exceptions.BpmException;

public class GetHistoricTaskInsCmd implements Command<HistoricTaskInstance>,Serializable{
	
	 protected String processInstanceId;
	  
	  public GetHistoricTaskInsCmd(String processInstanceId){
		  this.processInstanceId = processInstanceId;
	  }
	@Override
	public HistoricTaskInstance execute(CommandContext commandContext) {
		if(StringUtil.isEmptyString(processInstanceId)){
			throw new BpmException("Process Instance Id required for getting next active task");
		}
		return commandContext.getHistoricTaskInstanceEntityManager().getPreviousTaskInstace(processInstanceId);
	}

}
