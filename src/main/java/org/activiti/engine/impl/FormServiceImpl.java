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

package org.activiti.engine.impl;

import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.cmd.GetFormFieldAuditCmd;
import org.activiti.engine.impl.cmd.GetFormKeyCmd;
import org.activiti.engine.impl.cmd.GetHistoricTaskFormCmd;
import org.activiti.engine.impl.cmd.GetRenderedStartFormCmd;
import org.activiti.engine.impl.cmd.GetRenderedTaskFormCmd;
import org.activiti.engine.impl.cmd.GetStartFormCmd;
import org.activiti.engine.impl.cmd.GetTaskFormCmd;
import org.activiti.engine.impl.cmd.SaveTaskFormCmd;
import org.activiti.engine.impl.cmd.SubmitStartFormCmd;
import org.activiti.engine.impl.cmd.SubmitTaskFormCmd;
import org.activiti.engine.impl.cmd.UpdateStartFormCmd;
import org.activiti.engine.impl.cmd.UpdateTaskFormCmd;
import org.activiti.engine.runtime.ProcessInstance;

import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceException;


/**
 * @author Tom Baeyens
 * @author Falko Menge (camunda)
 */
public class FormServiceImpl extends ServiceImpl implements FormService {

  public Object getRenderedStartForm(String processDefinitionId) {
    return commandExecutor.execute(new GetRenderedStartFormCmd(processDefinitionId, null));
  }

  public Object getRenderedStartForm(String processDefinitionId, String engineName) {
    return commandExecutor.execute(new GetRenderedStartFormCmd(processDefinitionId, engineName));
  }

  public Object getRenderedTaskForm(String taskId) {
    return commandExecutor.execute(new GetRenderedTaskFormCmd(taskId, null));
  }

  public Object getRenderedTaskForm(String taskId, String engineName) {
    return commandExecutor.execute(new GetRenderedTaskFormCmd(taskId, engineName));
  }

  public StartFormData getStartFormData(String processDefinitionId) {
	  try{
		  return commandExecutor.execute(new GetStartFormCmd(processDefinitionId));
	  }catch(Exception e){
		  throw new BpmException(e.getMessage(), e);
	  }
    
  }

  public TaskFormData getTaskFormData(String taskId) {
    return commandExecutor.execute(new GetTaskFormCmd(taskId));
  }
  
  public TaskFormData getHistoricTaskFormData(HistoricTaskInstance task) {
	    return (TaskFormData)commandExecutor.execute(new GetHistoricTaskFormCmd(task));
	  }

  public ProcessInstance submitStartFormData(String processDefinitionId, Map<String, String> properties)throws DataSourceException {
    return commandExecutor.execute(new SubmitStartFormCmd(processDefinitionId, null, null, null, null, null,false));
  }
  
  public ProcessInstance submitStartFormData(String processDefinitionId, Map<String, String> properties,  Map<String, String[]> subProperties, Map<String, byte[]>files, Map<String, String>filePaths,boolean isDraft)throws DataSourceException {
	    return commandExecutor.execute(new SubmitStartFormCmd(processDefinitionId, null, properties, subProperties, files, filePaths,isDraft));
	  }
  
  public ProcessInstance submitStartFormData(String processDefinitionId, String businessKey, Map<String, String> properties)throws DataSourceException {
	  return commandExecutor.execute(new SubmitStartFormCmd(processDefinitionId, businessKey, null, null, null, null,false));
  }
  
  public ProcessInstance submitStartFormData(String processDefinitionId, String businessKey, Map<String, String> properties, Map<String, String[]> subProperties, Map<String, byte[]>files, Map<String, String>filePaths)throws DataSourceException {
	  return commandExecutor.execute(new SubmitStartFormCmd(processDefinitionId, businessKey, properties, subProperties, files, filePaths,false));
  }
  
  public ProcessInstance updateStartFormData(String processDefinitionId, String processInstanceId, Map<String, String> properties,  Map<String, String[]> subProperties, Map<String, byte[]>files, Map<String, String>filePaths)throws DataSourceException {
	    return commandExecutor.execute(new UpdateStartFormCmd(processDefinitionId, processInstanceId, properties, subProperties, files, filePaths));
	  }

  public void submitTaskFormData(String taskId, Map<String, String> properties)throws DataSourceException {
    commandExecutor.execute(new SubmitTaskFormCmd(taskId, properties));
  }
  
  public Object submitTaskFormData(String taskId, Map<String, String> properties, Map<String, String[]> subProperties, Map<String, byte[]>files, Map<String, String>filePaths, boolean isStartNodeTask, String proInsId)throws DataSourceException {
	  return commandExecutor.execute(new SubmitTaskFormCmd(taskId, properties, subProperties, files, filePaths, isStartNodeTask, proInsId));
	  }
  
  public void saveTaskFormData(String taskId, Map<String, String> properties, Map<String, String[]> subProperties, Map<String, byte[]>files, Map<String, String>filePaths, String loggedInUserId, String proInsId)throws DataSourceException {
	    commandExecutor.execute(new SaveTaskFormCmd(taskId, properties, subProperties, files, filePaths ,loggedInUserId, proInsId));
	  }
  
  public Object updateTaskFormData(String taskId, Map<String, String> properties, Map<String, String[]> subProperties, Map<String, byte[]>files, Map<String, String>filePaths, boolean isSignOff)throws DataSourceException {
	    return commandExecutor.execute(new UpdateTaskFormCmd(taskId, properties, subProperties, files, filePaths, isSignOff));
	  }
  
  public String getStartFormKey(String processDefinitionId) {
    return commandExecutor.execute(new GetFormKeyCmd(processDefinitionId));
  }

  public String getTaskFormKey(String processDefinitionId, String taskDefinitionKey) {
    return commandExecutor.execute(new GetFormKeyCmd(processDefinitionId, taskDefinitionKey));
  }

	public List<Map<String,Object>> getFormFieldTraceData(String processInstanceId,String formId) throws DataSourceException{
	    return commandExecutor.execute(new GetFormFieldAuditCmd(processInstanceId, formId));
  }
}
