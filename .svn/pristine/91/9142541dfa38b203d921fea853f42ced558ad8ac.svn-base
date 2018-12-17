package org.activiti.engine.impl.cmd;

import java.io.Serializable;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;


/**
 * {@link Command} that changes the business key of an existing
 * process instance.
 * 
 * @author Tijs Rademakers
 */
public class SetProcessInstanceBusinessKeyCmd implements Command<Void>, Serializable {

  private static final long serialVersionUID = 1L;

  private final String processInstanceId;
  private final String businessKey;

  public SetProcessInstanceBusinessKeyCmd(String processInstanceId, String businessKey) {
    this.processInstanceId = processInstanceId;
    this.businessKey = businessKey;
  }

  public Void execute(CommandContext commandContext) {
    ExecutionEntityManager executionManager = commandContext.getExecutionEntityManager();
    ExecutionEntity processInstance = executionManager.findExecutionById(processInstanceId);
    processInstance.updateProcessBusinessKey(businessKey);
    return null;
  }
}
