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

import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.form.StartFormHandler;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;

import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.exceptions.DataSourceException;


/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class UpdateStartFormCmd extends NeedsActiveProcessDefinitionCmd<ProcessInstance> {

  private static final long serialVersionUID = 1L;
  
  protected Map<String, String> properties;
  protected Map<String, String[]> subProperties;
  protected Map<String, byte[]>files;
  protected Map<String, String>filePaths;
  protected ExecutionEntity processInstance;
  protected String processInstanceId;
  protected String actId;
  
  public UpdateStartFormCmd(String processDefinitionId, String processInstanceId, Map<String, String> properties, Map<String, String[]> subProperties, Map<String, byte[]>files, Map<String, String>filePaths) {
    super(processDefinitionId);
    this.processInstanceId = processInstanceId;
    this.properties = properties;
    this.subProperties = subProperties;
    this.files = files;
    this.filePaths = filePaths;
    //this.actId = actId;
  }
  
  protected ProcessInstance execute(CommandContext commandContext, ProcessDefinitionEntity processDefinition)throws DataSourceException {
//	 HistoricProcessInstance hisProcessInstance = commandContext.getHistoricProcessInstanceEntityManager().
//			 										findHistoricProcessInstanceByInsId(processInstanceId);
	 actId = processDefinition.getInitial().getId();
	  processInstance =  commandContext.getExecutionEntityManager().findExecutionById(processInstanceId);
	 if(processInstance==null){
		 processInstance = new ExecutionEntity();
		 processInstance.setProcessInstanceId(processInstanceId);
		 processInstance.setId(processInstanceId);
		 processInstance.setTemp(true);
	 }
	  
    StartFormHandler startFormHandler = processDefinition.getStartFormHandler();
    startFormHandler.submitFormProperties(properties, processInstance, subProperties, files, filePaths, true,actId);

    
    return processInstance;
  }
}
