package org.activiti.engine.impl;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

public class updateHistoricTaskEntityCmd implements Command<Object> {

	  protected TaskEntity taskEntity;
	  
	  public updateHistoricTaskEntityCmd(TaskEntity taskEntity) {
	    this.taskEntity = taskEntity;
	  }
	    
	  public Void execute(CommandContext commandContext) {

		  commandContext.getTaskEntityManager().updateHistoricTaskEntityCmd(taskEntity);
		  return null;
	  }

}
