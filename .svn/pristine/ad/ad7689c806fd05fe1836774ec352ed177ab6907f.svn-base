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
import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;


/**
 * @author Sangeetha Krishnan
 */
public class UpdateTaskEntityCmd implements Command<Void>, Serializable {
  
  private static final long serialVersionUID = 1L;
  
  protected TaskEntity taskEntity;
  
  public UpdateTaskEntityCmd(TaskEntity taskEntity) {
    this.taskEntity = taskEntity;
  }
    
  public Void execute(CommandContext commandContext) {
	  commandContext.getTaskEntityManager().updateTaskEntity(taskEntity);
	  return null;
  }
}
