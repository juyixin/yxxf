package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;
import org.apache.commons.beanutils.BeanUtils;

import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.bpm.engine.TransactorType;
import com.eazytec.exceptions.BpmException;


/**
 * <p>For getting next task entity for the process instance, or the active task entity</p>
 * @author madan
 */
public class GetNextTaskCmd implements Command<Task>, Serializable {

  private static final long serialVersionUID = 1L;  
  
  protected String processInstanceId;
  
  public GetNextTaskCmd(String processInstanceId){
	  this.processInstanceId = processInstanceId;
  }

@Override
public Task execute(CommandContext commandContext)throws BpmException {
	if(StringUtil.isEmptyString(processInstanceId)){
		throw new BpmException("Process Instance Id required for getting next active task");
	}
	return commandContext.getTaskEntityManager().findNextTaskForProcessInstance(processInstanceId);
}
  
}
