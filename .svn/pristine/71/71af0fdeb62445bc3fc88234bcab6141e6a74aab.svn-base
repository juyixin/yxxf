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

import com.eazytec.bpm.engine.TransactorType;
import com.eazytec.exceptions.BpmException;


/**
 * <p>For getting task entities by signoff type and user</p>
 * @author madan
 */
public class GetTaskEntitiesCmd implements Command<List<Task>>, Serializable {

  private static final long serialVersionUID = 1L;
  
  protected List<TransactorType>signOffTypes ;
  protected String userId;
  
  public GetTaskEntitiesCmd(String userId, List<TransactorType>signOffTypes){
	  this.signOffTypes = signOffTypes;
	  this.userId = userId;
  }

@Override
public List<Task> execute(CommandContext commandContext)throws BpmException {
	return commandContext.getTaskEntityManager().findTasksBySignOffTypesAndUser(userId, signOffTypes);
}
  
}
