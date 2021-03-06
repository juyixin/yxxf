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

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;


/**
 * @author Joram Barrez
 */
public class DeleteProcessInstanceCmd implements Command<Void>, Serializable {
  
  private static final long serialVersionUID = 1L;
  protected String processInstanceId;
  protected String deleteReason;
  protected boolean executionExists = true;
  protected List<String> multipleProcessInstanceId;
private boolean bulkDelete = false;

  public DeleteProcessInstanceCmd(String processInstanceId, String deleteReason) {
    this.processInstanceId = processInstanceId;
    this.deleteReason = deleteReason;
  }
  
  public DeleteProcessInstanceCmd(List<String> multipleProcessInstanceId,String deleteReason,boolean bulkDelete) {
	    this.multipleProcessInstanceId = multipleProcessInstanceId;
	    this.deleteReason = deleteReason;
	    this.bulkDelete  = bulkDelete;
  }
  
  public DeleteProcessInstanceCmd(String processInstanceId, boolean executionExists) {
	    this.processInstanceId = processInstanceId;
	    this.executionExists = executionExists;
	  }

	public Void execute(CommandContext commandContext) {
		if (executionExists && bulkDelete != true) {
			if (processInstanceId == null) {
				throw new ActivitiException("processInstanceId is null");
			}
			commandContext.getExecutionEntityManager().deleteProcessInstance(
					processInstanceId, deleteReason, true);
		} else if (bulkDelete) {
			if (multipleProcessInstanceId.isEmpty()) {
				throw new ActivitiException("processInstanceId is null");
			}
			for (String processInstanceId : multipleProcessInstanceId) {
				commandContext.getExecutionEntityManager()
						.deleteProcessInstance(processInstanceId, deleteReason,
								true);
			}
		} else {
			if (processInstanceId == null) {
				throw new ActivitiException("processInstanceId is null");
			}
			commandContext.getHistoricProcessInstanceEntityManager()
					.deleteHistoricProcessInstanceById(processInstanceId);
		}
		return null;
	}

}
