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
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.impl.persistence.entity.TaskEntity;


/**
 * 
 * @author Daniel Meyer
 */
public class ActivateProcessInstanceCmd extends AbstractSetProcessInstanceStateCmd {

  public ActivateProcessInstanceCmd(String processInstanceId) {
    super(processInstanceId);
  }

@Override
protected SuspensionState getNewState(CommandContext commandContext,ExecutionEntity executionEntity) {
    if(processInstanceId == null) {
	      throw new ActivitiException("processInstanceId is null");
	    }else {
	        /*ExecutionEntity execution = commandContext
	        .getExecutionEntityManager()
	        .findExecutionById(executionId);*/
	        for(TaskEntity task : executionEntity.getTasks()){
	        	// updating historic task instance values to active state
	        	HistoricTaskInstanceEntity historicTaskInstanceEntity =
	        			commandContext.getHistoricTaskInstanceEntityManager().findHistoricTaskInstanceById(task.getId());
	        	historicTaskInstanceEntity.setEndTime(null);
	        	historicTaskInstanceEntity.setDeleteReason(null);
	        	historicTaskInstanceEntity.setDurationInMillis(null);
	        	commandContext.getHistoricTaskInstanceEntityManager().updateHistoricTaskInstance(historicTaskInstanceEntity);
	        }
	    }
	return SuspensionState.ACTIVE;
}

}
