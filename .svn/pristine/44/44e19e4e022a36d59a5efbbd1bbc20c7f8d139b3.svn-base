/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.engine.impl.cmd;

import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.SuspensionState.SuspensionStateUtil;

/**
 * @author Daniel Meyer
 * @author Joram Barrez
 */
public abstract class AbstractSetProcessInstanceStateCmd implements Command<Void> {
    
  protected final String processInstanceId;
  

  public AbstractSetProcessInstanceStateCmd(String processInstanceId) {
    this.processInstanceId = processInstanceId;
  }

  public Void execute(CommandContext commandContext) {
    
    if(processInstanceId == null) {
      throw new ActivitiException("ProcessInstanceId cannot be null.");
    }
    //finding current task execution with process instance id
    TaskEntity execution = commandContext.getTaskEntityManager().findExecutionByProcessInstanceId(processInstanceId);
    if(execution == null) {
        throw new ActivitiException("Cannot find Task for Process Instance '"+processInstanceId+"'.");
      }    
    ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findExecutionById(execution.getExecutionId());

    if(executionEntity == null ) {
      throw new ActivitiException("Cannot find Execution for id '"+processInstanceId+"'.");
    }
   /* if(!executionEntity.isProcessInstance()) {
      throw new ActivitiException("Cannot set suspension state for execution '"+executionId+"': not a process instance.");
    }*/
    
    SuspensionStateUtil.setSuspensionState(executionEntity, getNewState(commandContext,executionEntity));
    
    // All child executions are suspended
/*    List<ExecutionEntity> childExecutions = commandContext.getExecutionEntityManager().findChildExecutionsByProcessInstanceId(executionId);
    for (ExecutionEntity childExecution : childExecutions) {
      if (!childExecution.getId().equals(executionId)) {
        SuspensionStateUtil.setSuspensionState(childExecution, getNewState(commandContext,executionEntity));
      }
    }*/
    
    // All tasks are suspended
    List<TaskEntity> tasks = commandContext.getTaskEntityManager().findTasksByProcessInstanceId(processInstanceId);
    for (TaskEntity taskEntity : tasks) {
      SuspensionStateUtil.setSuspensionState(taskEntity, getNewState(commandContext,executionEntity));
    }
    
    return null;
  }

  protected abstract SuspensionState getNewState(CommandContext commandContext,ExecutionEntity executionEntity);

}
