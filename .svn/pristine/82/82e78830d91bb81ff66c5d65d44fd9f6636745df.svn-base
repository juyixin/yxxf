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

import java.util.Calendar;
import java.util.Map;

import org.activiti.engine.impl.Condition;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.form.StartFormHandler;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.json.JSONException;

import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.DataSourceException;


/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class SubmitStartFormCmd extends NeedsActiveProcessDefinitionCmd<ProcessInstance> {

  private static final long serialVersionUID = 1L;
  
  protected final String businessKey;
  protected Map<String, String> properties;
  protected Map<String, String[]> subProperties;
  protected Map<String, byte[]>files;
  protected Map<String, String>filePaths;
  protected boolean isDraft=false;
  
  public SubmitStartFormCmd(String processDefinitionId, String businessKey, Map<String, String> properties, Map<String, String[]> subProperties, Map<String, byte[]>files, Map<String, String>filePaths,boolean isDraft) {
    super(processDefinitionId);
    this.businessKey = businessKey;
    this.properties = properties;
    this.subProperties = subProperties;
    this.files = files;
    this.filePaths = filePaths;
    this.isDraft = isDraft;
  }
  
  protected ProcessInstance execute(CommandContext commandContext, ProcessDefinitionEntity processDefinition)throws DataSourceException {
    ExecutionEntity processInstance = null;
    boolean isUpdate = false;
    boolean isStartSavedProcess = false;
    if(!properties.get("processInstanceId").equalsIgnoreCase("null")){
    	processInstance = Context.getCommandContext().getExecutionEntityManager().findExecutionById(properties.get("processInstanceId"));
    	isUpdate = true;
    }else {
    	//is draft is for saving the form in start node
    	if(isDraft){
    		 processInstance = processDefinition.saveProcessInstance();
    	}else {
            if (businessKey != null) {
                processInstance = processDefinition.createProcessInstance(businessKey);
            }else {
                processInstance = processDefinition.createProcessInstance();
            }
    	}
    }
   
    if(processInstance.getActivity() != null){
    	processInstance.setProperties(processInstance.getActivity().getProperties());
    }
    
    StartFormHandler startFormHandler = processDefinition.getStartFormHandler();
    startFormHandler.submitFormProperties(properties, processInstance, subProperties, files, filePaths, isUpdate,processDefinition.getInitial().getId());
    
    if(isUpdate && !isDraft){
		HistoricProcessInstanceEntity hisProEntity = commandContext.getHistoricProcessInstanceEntityManager().findHistoricProcessInstanceByInsId(processInstance.getId());
		if(hisProEntity.getIsDraft()){
			hisProEntity.setIsDraft(false);
			hisProEntity.setStartTime(ClockUtil.getCurrentTime());
			//update the end date of HistoricProcessInstanceEntity
			commandContext.getHistoricProcessInstanceEntityManager().updateHistoricProcessInstanceEntity(hisProEntity);
			isStartSavedProcess = true;
		}
		if(!isStartSavedProcess){
	    	processInstance.setProcessDefinition(processDefinition);
	    	Context.getCommandContext().getDbSqlSession().delete("deleteIdentityLinkByProcessInstanceId", processInstance.getId());
		}
    }
    /*if(!isUpdate || isStartSavedProcess){
    	setRelationInfo(commandContext,processDefinition,processInstance);
    }*/
	if(!isDraft){
		processInstance.start();
	}
    return processInstance;
  }
  /**
   * Get and set the reference relation from the sequence and set it to the task
   * @param commandContext
   * @param processDefinition
   * @param processInstance
   */
  private void setRelationInfo(CommandContext commandContext,ProcessDefinitionEntity processDefinition,ExecutionEntity processInstance){
	  String defaultSequenceFlow = (String) processInstance.getActivity().getProperty("default");
	  for (PvmTransition outgoingTransition : processInstance.getActivity().getOutgoingTransitions()) {
		  if (defaultSequenceFlow == null || !outgoingTransition.getId().equals(defaultSequenceFlow)) {
			  Condition condition = (Condition) outgoingTransition.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
			  if (condition == null || condition.evaluate(processInstance)) {
				  
	   	        	ActivityImpl activity= (ActivityImpl)outgoingTransition.getDestination();
	   	        	if(activity.getActivityBehavior()  instanceof UserTaskActivityBehavior){
	   	        		String relationInfo = (String)outgoingTransition.getProperty("relationInfo");
	   				  try {
	   					  if(relationInfo!= null &&!relationInfo.isEmpty()){
	   						  CommonUtil.convertJsonToList((String)outgoingTransition.getProperty("relationInfo"));
	   						processInstance.setReferenceRelation( CommonUtil.convertJsonToList((String)outgoingTransition.getProperty("relationInfo")));
	   					  }
	   				  } catch (JSONException e) {
	   						// TODO Auto-generated catch block
	   						e.printStackTrace();
	   					}
	   	        	}
		  }
	  }
	  
  }
}
}
