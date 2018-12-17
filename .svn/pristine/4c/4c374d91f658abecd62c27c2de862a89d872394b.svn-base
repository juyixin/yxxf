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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.form.FormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.cmd.ActivateProcessInstanceCmd;
import org.activiti.engine.impl.cmd.AddIdentityLinkCmd;
import org.activiti.engine.impl.cmd.CreateAttachmentCmd;
import org.activiti.engine.impl.cmd.DeleteAttachmentCmd;
import org.activiti.engine.impl.cmd.DeleteHistoricIdentityLinkDetail;
import org.activiti.engine.impl.cmd.DeleteProcessInstanceCmd;
import org.activiti.engine.impl.cmd.DescribeEntityCmd;
import org.activiti.engine.impl.cmd.FindActiveActivityIdsCmd;
import org.activiti.engine.impl.cmd.GetAndInsertAttachmentListCmd;
import org.activiti.engine.impl.cmd.GetAttachmentCmd;
import org.activiti.engine.impl.cmd.GetExecutionVariableCmd;
import org.activiti.engine.impl.cmd.GetExecutionVariablesCmd;
import org.activiti.engine.impl.cmd.GetHistoricTaskFormCmd;
import org.activiti.engine.impl.cmd.GetHistoricvariableInstanceCmd;
import org.activiti.engine.impl.cmd.GetIdentityLinkCmd;
import org.activiti.engine.impl.cmd.GetStartFormCmd;
import org.activiti.engine.impl.cmd.MessageEventReceivedCmd;
import org.activiti.engine.impl.cmd.RemoveExecutionVariablesCmd;
import org.activiti.engine.impl.cmd.SetExecutionVariablesCmd;
import org.activiti.engine.impl.cmd.SetProcessInstanceBusinessKeyCmd;
import org.activiti.engine.impl.cmd.SignalCmd;
import org.activiti.engine.impl.cmd.SignalEventReceivedCmd;
import org.activiti.engine.impl.cmd.StartProcessInstanceByMessageCmd;
import org.activiti.engine.impl.cmd.StartProcessInstanceCmd;
import org.activiti.engine.impl.cmd.SubmitTaskFormCmd;
import org.activiti.engine.impl.cmd.SuspendProcessInstanceCmd;
import org.activiti.engine.impl.cmd.TaskJumpCmd;
import org.activiti.engine.impl.cmd.TaskRecallCmd;
import org.activiti.engine.impl.cmd.TaskReturnCmd;
import org.activiti.engine.impl.cmd.TerminateExecutionCmd;
import org.activiti.engine.impl.cmd.UpdateHistoricIdentityLinkDetail;
import org.activiti.engine.impl.persistence.entity.AttachmentEntity;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.NativeExecutionQuery;
import org.activiti.engine.runtime.NativeProcessInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.impl.cmd.AddHistoricIdentityDetails;
import org.activiti.engine.impl.cmd.UpdateTaskEntityCmd;

import com.eazytec.exceptions.BpmException;

/**
 * @author Tom Baeyens
 * @author Daniel Meyer
 */
public class RuntimeServiceImpl extends ServiceImpl implements RuntimeService {

  public ProcessInstance startProcessInstanceByKey(String processDefinitionKey) {
    return (ProcessInstance)commandExecutor.execute(new StartProcessInstanceCmd(processDefinitionKey, null, null, null));
  }

  public ProcessInstance startProcessInstanceByKey(String processDefinitionKey, String businessKey) {
    return (ProcessInstance)commandExecutor.execute(new StartProcessInstanceCmd(processDefinitionKey, null, businessKey, null));
  }
  
  public ProcessInstance startProcessInstanceByKey(String processDefinitionKey, Map<String, Object> variables) {
    return (ProcessInstance)commandExecutor.execute(new StartProcessInstanceCmd(processDefinitionKey, null, null, variables));
  }
  
  public ProcessInstance startProcessInstanceByKey(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
    return (ProcessInstance)commandExecutor.execute(new StartProcessInstanceCmd(processDefinitionKey, null, businessKey, null));
  }
  
  public ProcessInstance startProcessInstanceById(String processDefinitionId) {
    return (ProcessInstance)commandExecutor.execute(new StartProcessInstanceCmd(null, processDefinitionId, null, null));
  }
  
  public Object getOrganizersForProcessStart(String processDefinitionId,Map<String, Object> formValues) {
	    return commandExecutor.execute(new StartProcessInstanceCmd(processDefinitionId,false,formValues));
  }
  
  public ProcessInstance startProcessInstanceById(String processDefinitionId, String businessKey) {
    return (ProcessInstance)commandExecutor.execute(new StartProcessInstanceCmd(null, processDefinitionId, businessKey, null));
  }

  public ProcessInstance startProcessInstanceById(String processDefinitionId, Map<String, Object> variables) {
    return (ProcessInstance)commandExecutor.execute(new StartProcessInstanceCmd(null, processDefinitionId, null, variables));
  }
  
  public ProcessInstance startProcessInstanceById(String processDefinitionId, String businessKey, Map<String, Object> variables) {
    return (ProcessInstance)commandExecutor.execute(new StartProcessInstanceCmd(null, processDefinitionId, businessKey, variables));
  }
  
  public void deleteProcessInstance(String processInstanceId, String deleteReason) {
   		commandExecutor.execute(new DeleteProcessInstanceCmd(processInstanceId, deleteReason));
  }
  
  public void deleteMultipleProcessInstance(List<String> processInstanceId,String deleteReason) {
	    commandExecutor.execute(new DeleteProcessInstanceCmd(processInstanceId,deleteReason,true));
  }
  
  public void deleteProcessInstanceOnly(String processInstanceId) throws ActivitiException{
	    commandExecutor.execute(new DeleteProcessInstanceCmd(processInstanceId,false));
  }
  
  public List<HistoricVariableInstance> getHistoricVariableInstances(String processInstanceId) {
	    return commandExecutor.execute(new GetHistoricvariableInstanceCmd(processInstanceId));
  }
  
  public List<HistoricVariableInstance> getHistoricVariableInstances(List<String> processInstanceIds) {
	    return commandExecutor.execute(new GetHistoricvariableInstanceCmd(processInstanceIds));
  }
  
  public List<HistoricVariableInstance> getPrevHistoricVariableInstances(String processInstanceId, String currentTaskId) throws BpmException {
	    return commandExecutor.execute(new GetHistoricvariableInstanceCmd(processInstanceId, currentTaskId, false));
  }
  
  public List<HistoricVariableInstance> getHistoricVariableInstancesByTaskId(String processInstanceId, String taskId) {
	  return commandExecutor.execute(new GetHistoricvariableInstanceCmd(processInstanceId, taskId, true));
}

  public ExecutionQuery createExecutionQuery() {
    return new ExecutionQueryImpl(commandExecutor);
  }
  
  public NativeExecutionQuery createNativeExecutionQuery() {
    return new NativeExecutionQueryImpl(commandExecutor);
  }

  public NativeProcessInstanceQuery createNativeProcessInstanceQuery() {
    return new NativeProcessInstanceQueryImpl(commandExecutor);
  }  
  
  public Map<String, Object> getVariables(String executionId) {
    return commandExecutor.execute(new GetExecutionVariablesCmd(executionId, null, false));
  }
  
	public List<Map<String, Object>> getVariablesByTaskId(String taskId,String processInsId) {
	  List<HistoricVariableInstance> historicVariableInstance = commandExecutor.execute(new GetHistoricvariableInstanceCmd(processInsId,taskId,true));
	  return commandExecutor.execute(new DescribeEntityCmd(historicVariableInstance));
  }

  public Map<String, Object> getVariablesLocal(String executionId) {
    return commandExecutor.execute(new GetExecutionVariablesCmd(executionId, null, true));
  }

  public Map<String, Object> getVariables(String executionId, Collection<String> variableNames) {
    return commandExecutor.execute(new GetExecutionVariablesCmd(executionId, variableNames, false));
  }

  public Map<String, Object> getVariablesLocal(String executionId, Collection<String> variableNames) {
    return commandExecutor.execute(new GetExecutionVariablesCmd(executionId, variableNames, true));
  }

  public Object getVariable(String executionId, String variableName) {
    return commandExecutor.execute(new GetExecutionVariableCmd(executionId, variableName, false));
  }
  
  public Object getVariableLocal(String executionId, String variableName) {
    return commandExecutor.execute(new GetExecutionVariableCmd(executionId, variableName, true));
  }
  
  public void setVariable(String executionId, String variableName, Object value) {
    if(variableName == null) {
      throw new ActivitiException("variableName is null");
    }
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put(variableName, value);
    commandExecutor.execute(new SetExecutionVariablesCmd(executionId, variables, false));
  }
  
  public void setVariableLocal(String executionId, String variableName, Object value) {
    if(variableName == null) {
      throw new ActivitiException("variableName is null");
    }
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put(variableName, value);
    commandExecutor.execute(new SetExecutionVariablesCmd(executionId, variables, true));
  }

  public void setVariables(String executionId, Map<String, ? extends Object> variables) {
    commandExecutor.execute(new SetExecutionVariablesCmd(executionId, variables, false));
  }

  public void setVariablesLocal(String executionId, Map<String, ? extends Object> variables) {
    commandExecutor.execute(new SetExecutionVariablesCmd(executionId, variables, true));
  }
  
  public void removeVariable(String executionId, String variableName) {
    Collection<String> variableNames = new ArrayList<String>();
    variableNames.add(variableName);
    commandExecutor.execute(new RemoveExecutionVariablesCmd(executionId, variableNames, false));
  }
  
  public void removeVariableLocal(String executionId, String variableName) {
    Collection<String> variableNames = new ArrayList<String>();
    variableNames.add(variableName);
    commandExecutor.execute(new RemoveExecutionVariablesCmd(executionId, variableNames, true));
    
  }

  public void removeVariables(String executionId, Collection<String> variableNames) {
    commandExecutor.execute(new RemoveExecutionVariablesCmd(executionId, variableNames, false));    
  }

  public void removeVariablesLocal(String executionId, Collection<String> variableNames) {
    commandExecutor.execute(new RemoveExecutionVariablesCmd(executionId, variableNames, true));    
  }

  public void signal(String executionId) {
    commandExecutor.execute(new SignalCmd(executionId, null, null, null));
  }
  
  public void signal(String executionId, Map<String, Object> processVariables) {
    commandExecutor.execute(new SignalCmd(executionId, null, null, processVariables));
  }

  public ProcessInstanceQuery createProcessInstanceQuery() {
    return new ProcessInstanceQueryImpl(commandExecutor);
  }

  public List<String> getActiveActivityIds(String executionId) {
    return commandExecutor.execute(new FindActiveActivityIdsCmd(executionId));
  }

  public FormData getFormInstanceById(String processDefinitionId) {
    return commandExecutor.execute(new GetStartFormCmd(processDefinitionId));
  }

  public void suspendProcessInstanceById(String processInstanceId) {
    commandExecutor.execute(new SuspendProcessInstanceCmd(processInstanceId));
  }

  public void activateProcessInstanceById(String processInstanceId) {
    commandExecutor.execute(new ActivateProcessInstanceCmd(processInstanceId));
  }
  
  public ProcessInstance startProcessInstanceByMessage(String messageName) {
    return commandExecutor.execute(new StartProcessInstanceByMessageCmd(messageName,null, null));
  }
  
  public ProcessInstance startProcessInstanceByMessage(String messageName, String businessKey) {
    return commandExecutor.execute(new StartProcessInstanceByMessageCmd(messageName, businessKey, null));
  }
  
  public ProcessInstance startProcessInstanceByMessage(String messageName, Map<String, Object> processVariables) {
    return commandExecutor.execute(new StartProcessInstanceByMessageCmd(messageName, null, processVariables));
  }
  
  public ProcessInstance startProcessInstanceByMessage(String messageName, String businessKey, Map<String, Object> processVariables) {
    return commandExecutor.execute(new StartProcessInstanceByMessageCmd(messageName, businessKey, processVariables));
  }

  public void signalEventReceived(String signalName) {
    commandExecutor.execute(new SignalEventReceivedCmd(signalName, null, null));
  }

  public void signalEventReceived(String signalName, Map<String, Object> processVariables) {
    commandExecutor.execute(new SignalEventReceivedCmd(signalName, null, processVariables));
  }

  public void signalEventReceived(String signalName, String executionId) {
    commandExecutor.execute(new SignalEventReceivedCmd(signalName, executionId, null));
  }

  public void signalEventReceived(String signalName, String executionId, Map<String, Object> processVariables) {
    commandExecutor.execute(new SignalEventReceivedCmd(signalName, executionId, processVariables));
  }

  public void messageEventReceived(String messageName, String executionId) {
    commandExecutor.execute(new MessageEventReceivedCmd(messageName, executionId, null));
  }

  public void messageEventReceived(String messageName, String executionId, Map<String, Object> processVariables) {
    commandExecutor.execute(new MessageEventReceivedCmd(messageName, executionId, processVariables));
  }
  
  public void terminateExecutionByInstanceId(String processInstanceId,Date currentDate){
	  commandExecutor.execute(new TerminateExecutionCmd(processInstanceId,currentDate));
  }
  
  public void terminateExecutionsByInstanceIds(List<String> processInstanceIds,Date currentDate){
	  commandExecutor.execute(new TerminateExecutionCmd(processInstanceIds,currentDate,true));
  }
  
  public void addCandidateUser(String taskId, String userId) {
	    commandExecutor.execute(new AddIdentityLinkCmd(taskId, userId, null, IdentityLinkType.CANDIDATE,null));
	  }
	  
	  public void addCandidateReader(String taskId, String userId) {
		    commandExecutor.execute(new AddIdentityLinkCmd(taskId, userId, null, IdentityLinkType.READER,null));
		  }
	  
	  public void addCreatorUser(String taskId, String userId) {
		    commandExecutor.execute(new AddIdentityLinkCmd(taskId, userId, null, IdentityLinkType.CREATOR,null));
		  }

	public Object taskReturn(String taskId) {
		return commandExecutor.execute(new TaskReturnCmd(taskId));
	}

	public Object taskReturn(String taskId,boolean needToDoReturn) {
		return commandExecutor.execute(new TaskReturnCmd(taskId,needToDoReturn));
	}
	
	public List<IdentityLinkEntity> getIdentityLinkForTaskId(String taskId) {
		return commandExecutor.execute(new GetIdentityLinkCmd(taskId,false));
	}
	
	public List<IdentityLinkEntity> getCreatorIdentityLinkForTaskId(String taskId) {
		return commandExecutor.execute(new GetIdentityLinkCmd(taskId,true));
	}
	
	public void AddHistoricIdentityDetails(String proInsId,String userId,boolean isRead) {
		commandExecutor.execute(new AddHistoricIdentityDetails(proInsId,userId,isRead));
	}
	
	public void AddHistoricIdentityDetails(String taskId) {
		commandExecutor.execute(new AddHistoricIdentityDetails(taskId));
	}
	
	public void removeHistoricIdentityDetails(String taskId,String userId) {
		commandExecutor.execute(new DeleteHistoricIdentityLinkDetail(taskId,userId));
	}
	
	 public Object taskJump(String taskId,String activityId,boolean needOrganizer,String nodeType) {
		  return commandExecutor.execute(new TaskJumpCmd(taskId,activityId,needOrganizer,nodeType));
	  }
	 public void addProcessedUser(String taskId, String userId) {
		    commandExecutor.execute(new AddIdentityLinkCmd(taskId, userId, null, IdentityLinkType.PROCESSED_USER,null));
     }
	  public String getLastTaskAssignee(String processInstanceId,String taskDefinitionKey) {
		    return (commandExecutor.execute(new StartProcessInstanceCmd(processInstanceId,taskDefinitionKey,true))).toString();
	  } 
	  
	  public Object submitTaskFormData(Map<String, Object> rtFormValues) {
		  return commandExecutor.execute(new SubmitTaskFormCmd(rtFormValues,true));
	  }
	  
	  public void updateIdentityLinkDetailReadStatus(String proInsId,String userId,boolean isRead){
		   commandExecutor.execute(new UpdateHistoricIdentityLinkDetail(proInsId,userId,true));
	  }
	  
  public void withdrawProcessInstanceById(String processInstanceId) {
	  commandExecutor.execute(new SuspendProcessInstanceCmd(processInstanceId,SuspensionState.WITHDRAWN));
  }
  
  public List<AttachmentEntity> getAttachmentsOfTask(boolean isPrevious,String taskId, boolean isInsert,boolean isStartForm,String insId){
	  return commandExecutor.execute(new GetAndInsertAttachmentListCmd(isPrevious,taskId,isInsert,isStartForm,insId));
  }
  
  public AttachmentEntity getAttachmentEntity(String id){
	  return (AttachmentEntity) commandExecutor.execute(new GetAttachmentCmd(id));
  }
  
  public List<AttachmentEntity> getAttachmentAndInsertForTask(List<String> ids,String taskId, boolean isInsert){
	  return commandExecutor.execute(new GetAndInsertAttachmentListCmd(taskId,isInsert,ids));
  }
  
  public void createAttachmentOfTask(String attachmentType, String taskId, String processInstanceId, String attachmentName, String attachmentDescription, InputStream content, String url,int attachmentCount,String dateTime){
	  commandExecutor.execute(new CreateAttachmentCmd(attachmentType, taskId, processInstanceId, attachmentName, attachmentDescription, content, url,attachmentCount,dateTime));
  }
  
  public void deleteAttachmentByIds(List<String> attachmentIds){
	  commandExecutor.execute(new DeleteAttachmentCmd(attachmentIds));
  }

  public Object getHistoricProcessedUserLink(HistoricTaskInstance task) {
	    return commandExecutor.execute(new GetHistoricTaskFormCmd(task,true));
  } 
  
  public Object getHistoricTaskDefinition(HistoricTaskInstance task) {
	    return commandExecutor.execute(new GetHistoricTaskFormCmd(task,false,true));
  } 
  
  public List<AttachmentEntity> getAttachmentForTask(String taskId){
	  return commandExecutor.execute(new GetAndInsertAttachmentListCmd(taskId,false,null));
  }
  
	public void recallTask(String processInstanceId,String taskId) {
		commandExecutor.execute(new TaskRecallCmd(processInstanceId,taskId));
	}
	
	public void updateTaskEntityCmd(TaskEntity taskEntity) {
		commandExecutor.execute(new UpdateTaskEntityCmd(taskEntity));
	}
	
	public void updateHistoricTaskEntityCmd(TaskEntity taskEntity) {
		commandExecutor.execute(new updateHistoricTaskEntityCmd(taskEntity));
	}
	
	public void updateBusinessKey(String processInstanceId, String businessKey) {
	    commandExecutor.execute(new SetProcessInstanceBusinessKeyCmd(processInstanceId, businessKey));
	}
	
}
