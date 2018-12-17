package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;
import org.apache.commons.beanutils.BeanUtils;

import com.eazytec.bpm.engine.TransactorType;
import com.eazytec.exceptions.BpmException;


/**
 * <p>For getting task entities by signoff type and user</p>
 * @author madan
 */
public class GetTaskDefinitionCmd implements Command<Task>, Serializable {

  private static final long serialVersionUID = 1L;
  
  protected Task task;
  
  public GetTaskDefinitionCmd(Task task){
	  this.task=task;
  }

@Override
public Task execute(CommandContext commandContext)throws BpmException {
	
	task.setTaskDefinition(task.getTaskDefinition());
	return task;
}
  
}
