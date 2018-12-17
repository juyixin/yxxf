/* Licensed under the Apache License, Version 2.0 (the "License");
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.Condition;
import org.activiti.engine.impl.bpmn.behavior.ExclusiveGatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.NoneEndEventActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.NoneStartEventActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.SubProcessActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.deploy.DeploymentCache;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.json.JSONException;

import com.eazytec.bpm.common.util.I18nUtil;
import com.eazytec.common.util.CommonUtil;


/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class StartProcessInstanceCmd implements Command<Object> {

  private static final long serialVersionUID = 1L;
  protected String processDefinitionKey;
  protected String processDefinitionId;
  protected Map<String, Object> variables;
  protected String businessKey;
  protected boolean needToStart = true;
  protected boolean needPreviousAssignee = false;
  protected String processInstanceId;
  protected String taskDefinitionKey;
  
  
  public StartProcessInstanceCmd(String processDefinitionKey, String processDefinitionId, String businessKey, Map<String, Object> variables) {
    this.processDefinitionKey = processDefinitionKey;
    this.processDefinitionId = processDefinitionId;
    this.businessKey = businessKey;
    this.variables = variables;
  }
  
  public StartProcessInstanceCmd(String processDefinitionId,boolean needToStart, Map<String, Object> variables) {
	    this.processDefinitionId = processDefinitionId;
	    this.variables = variables;
	    this.needToStart = needToStart;
  }
  
  public StartProcessInstanceCmd(String processInstanceId,String taskDefinitionKey,boolean needPreviousAssignee) {
	    this.processInstanceId = processInstanceId;
	    this.taskDefinitionKey = taskDefinitionKey;
	    this.needPreviousAssignee = needPreviousAssignee;
  }
  
  public Object execute(CommandContext commandContext) {
	  if(needPreviousAssignee){
		  return Context.getCommandContext().getHistoricTaskInstanceEntityManager().getLastTaskAssignee(processInstanceId, taskDefinitionKey);
	  }else {
		    DeploymentManager deploymentCache = Context
		      .getProcessEngineConfiguration()
		      .getDeploymentManager();
		    
		    // Find the process definition
		    ProcessDefinitionEntity processDefinition = null;
		    if (processDefinitionId!=null) {
		      processDefinition = deploymentCache.findDeployedProcessDefinitionById(processDefinitionId);
		      if (processDefinition == null) {
				  throw new ActivitiException(I18nUtil.getMessageProperty("error.noProcessDefinition")+" ProcessDefinition Id : "+processDefinitionId);    				
		      }
		    } else if(processDefinitionKey != null){
		      processDefinition = deploymentCache.findDeployedLatestProcessDefinitionByKey(processDefinitionKey);
		      if (processDefinition == null) {
				  throw new ActivitiException(I18nUtil.getMessageProperty("error.noProcessDefinitionKey")+" Process Definition key : "+processDefinitionKey);    				
		      }
		    } else {
				  throw new ActivitiException(I18nUtil.getMessageProperty("error.noProcessDefinition"));    				
		    }
		    
		    // Do not start process a process instance if the process definition is suspended
		    if (processDefinition.isSuspended()) {
				  throw new ActivitiException(I18nUtil.getMessageProperty("error.instanceSuspended"));    				
		    }
		    ExecutionEntity processInstance = null;
		    if(needToStart){
		        // Start the process instance
		        processInstance = processDefinition.createProcessInstance(businessKey);
		        if (variables!=null) {
		            processInstance.setVariables(variables);
		        }
			    //Set properties in process instance
		        if(processInstance.getActivity() != null){
		        	processInstance.setProperties(processInstance.getActivity().getProperties());
		        }
		        processInstance.start();
		        //setRelationInfo(commandContext,processDefinition,processInstance);
		        return processInstance;
		    }else {
		    	 ExecutionEntity execution = commandContext
		 		        .getExecutionEntityManager()
		 		        .findExecutionById(processDefinition.getProcessInstanceFromCache(businessKey, null));
		 				  execution.setVariables(variables);
		 				  TaskDefinition taskDefinition = null;
		 				  String defaultSequenceFlow = (String) execution.getActivity().getProperty("default");
		 				  boolean conditionStatisfied = true;
		 				 // check for null conditions
		 				  if(execution.getActivity().getOutgoingTransitions().size() > 1){
							  Condition condition = null;
							  for (PvmTransition outgoingTransition : execution.getActivity().getOutgoingTransitions()) {
								  condition = (Condition) outgoingTransition.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
					   	          if(condition != null){
					   	        	break;
					   	          }
							  }
							  if(condition == null){
								  throw new ActivitiException(I18nUtil.getMessageProperty("errors.conditions.notexist"));
							  }
						  } else if(execution.getActivity().getOutgoingTransitions().size() == 1) {
							  for (PvmTransition outgoingTransition : execution.getActivity().getOutgoingTransitions()) {
								  ActivityImpl activity= (ActivityImpl)outgoingTransition.getDestination();
								  Condition xorCondition = null;
								  if(activity.getActivityBehavior()  instanceof ExclusiveGatewayActivityBehavior){
									  for(PvmTransition outgoingTransition1 : activity.getOutgoingTransitions()) {
										  xorCondition = (Condition) outgoingTransition1.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
										  if(xorCondition != null){
				   	        	        		break;
				   	        	          }
									  }
									  if(xorCondition == null){
										  throw new ActivitiException(I18nUtil.getMessageProperty("errors.conditions.notexist"));
									  }
								  }
							  }
						  } 
		 				 List<Map<String, Object>> referenceRelation = null;
		 				  for (PvmTransition outgoingTransition : execution.getActivity().getOutgoingTransitions()) {
			   	        		//get reference relation
								String relationInfo = (String) outgoingTransition.getProperty("relationInfo");
								try {
									if (relationInfo!= null && !relationInfo.isEmpty()) {
										referenceRelation = CommonUtil.convertJsonToList((String) outgoingTransition.getProperty("relationInfo"));
									}
								} catch (JSONException e) {
									throw new ActivitiException("exception occured while generating json for transactor relationship "+
											"in process "+outgoingTransition.getProcessDefinition().getName());
								}
		 					  
		 			    	  if (defaultSequenceFlow == null || !outgoingTransition.getId().equals(defaultSequenceFlow)) {
		 				   	        Condition condition = (Condition) outgoingTransition.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
		 				   	        if(condition != null){
		 					   	        conditionStatisfied = condition.evaluate(execution);
		 				   	        }
		 				   	        if (condition == null || conditionStatisfied) {
		 				   	        	ActivityImpl activity= (ActivityImpl)outgoingTransition.getDestination();
		 				   	        	if(activity.getActivityBehavior()  instanceof UserTaskActivityBehavior){
		 				   	        		
		 				   	        		UserTaskActivityBehavior activityBehaviour  = (UserTaskActivityBehavior)activity.getActivityBehavior();
		 				   	        		//set reference relation
		 				   	        		activityBehaviour.getTaskDefinition().setReferenceRelation(referenceRelation);
		 									//Set start event dynamic property
		 									setDynamicOrganizerValue(activityBehaviour.getTaskDefinition(), execution.getActivity());
		 				   	        		return activityBehaviour.getTaskDefinition();
		 				   	        		//Checking if there is any condition exist
		 				   	        	}else if(activity.getActivityBehavior()  instanceof ExclusiveGatewayActivityBehavior){
		 				   	        	    PvmTransition outgoingSeqFlow = null;
						   	        	    PvmTransition remainingSeqFlow = null;
						   	        	    Iterator<PvmTransition> transitionIterator = activity.getOutgoingTransitions().iterator();
						   	        	    while(outgoingSeqFlow == null && transitionIterator.hasNext()) {
						   	        	        PvmTransition seqFlow = transitionIterator.next();
						   	        	        Condition xorCondition = (Condition) seqFlow.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
						   	        	        if(defaultSequenceFlow == null || !defaultSequenceFlow.equals(seqFlow.getId())) {
						   	        	        	//if there is no node satisfying the condition means we will use it in future
						   	        	        	if(xorCondition == null){
						   	        	        		remainingSeqFlow = seqFlow; //end
						   	        	        	}else if(xorCondition.evaluate(execution)){
						   	        	        		outgoingSeqFlow = seqFlow;
						   	        	        	}
						   	        	        }
						   	        	    }
						   	        	    //no node satisfying the condition so we are setting remaining node which has no condition expression
						   	        	    if(outgoingSeqFlow == null && remainingSeqFlow != null){
						   	        	    	outgoingSeqFlow = remainingSeqFlow;
						   	        	    }//end
		 				   	        	    if (outgoingSeqFlow != null) {
		 						   	        	ActivityImpl xorActivity= (ActivityImpl)outgoingSeqFlow.getDestination();
		 						   	        	if(xorActivity.getActivityBehavior()  instanceof UserTaskActivityBehavior){
		 						   	        		UserTaskActivityBehavior activityBehaviour  = (UserTaskActivityBehavior)xorActivity.getActivityBehavior();
		 						   	        		activityBehaviour.getTaskDefinition().setReferenceRelation(referenceRelation);
		 						   	        		return activityBehaviour.getTaskDefinition();
		 						   	        	}
		 				   	        	    }else {
		 				   	        	    	if (defaultSequenceFlow != null) {
		 					   	        	        PvmTransition defaultTransition = execution.getActivity().findOutgoingTransition(defaultSequenceFlow);
		 					   	        	        if (defaultTransition != null) {
		 								   	        	ActivityImpl xorActivity= (ActivityImpl)defaultTransition.getDestination();
		 								   	        	if(xorActivity.getActivityBehavior()  instanceof UserTaskActivityBehavior){
		 								   	        		UserTaskActivityBehavior activityBehaviour  = (UserTaskActivityBehavior)xorActivity.getActivityBehavior();
		 								   	        		//set reference relation
		 								   	        		activityBehaviour.getTaskDefinition().setReferenceRelation(referenceRelation);
		 								   	        		return activityBehaviour.getTaskDefinition();
		 								   	        	}
		 					   	        	        } else {
		 					   	        	        	throw new ActivitiException(I18nUtil.getMessageProperty("errors.sequenceflow.notexist"));
		 					   	        	        }
		 					   	        	    } else {
		 				   	        	        //No sequence flow could be found, not even a default one
		 					   	        	    	throw new ActivitiException(I18nUtil.getMessageProperty("errors.sequenceflow.notexist"));
		 				   	        	      }
		 				   	        	    }		   	        	  	
		 							  	}else if(activity.getActivityBehavior()  instanceof SubProcessActivityBehavior){
		 							  		return new TaskDefinition();
		 							  	}//end
		 				   	        	if(taskDefinition == null){
		 				   	        		if(activity.getActivityBehavior() instanceof NoneEndEventActivityBehavior){
		 				   	        			if(execution.getActivity().getActivityBehavior() instanceof NoneStartEventActivityBehavior){
		 				   	        				return ((ProcessDefinition)execution.getActivity().getProcessDefinition()).hasStartFormKey();
		 				   	        			}
		 				   	        		}
		 				   	        	}
		 							}
		 			   	        }
		 				   	}
		 				  	//No sequence flow could be found, not even a default one
		         	    	throw new ActivitiException(I18nUtil.getMessageProperty("errors.sequenceflow.notexist"));
		    }
		    
	  }
   }
  
  private void setDynamicOrganizerValue(TaskDefinition taskDef,ActivityImpl activity){
	  String dynamicOrganizer = null;
 		String dynamicOrganizerType = null;
 		if(activity.getProperty(BpmnParse.DYNAMIC_NEXT_ORGANIZER) !=null){
 			dynamicOrganizer = (String)activity.getProperty(BpmnParse.DYNAMIC_NEXT_ORGANIZER);
 			dynamicOrganizerType = (String)activity.getProperty(BpmnParse.DYNAMIC_ORGNIZER_TYPE);
 		}
 		if(dynamicOrganizer != null){
 			taskDef.setStartNodeDynamicOrganizer(dynamicOrganizer);
 			taskDef.setStartNodedynamicOrganizerType(dynamicOrganizerType);
 			taskDef.setStartFormName((String)activity.getProperty("formName"));
   		} else {
   			taskDef.setStartNodeDynamicOrganizer(null);
   		}
  }
  
  /**
   * Get and set the reference relation from the sequence and set it to the task
   * @param commandContext
   * @param processDefinition
   * @param processInstance
   */
/*  private void setRelationInfo(CommandContext commandContext,ProcessDefinitionEntity processDefinition,ExecutionEntity processInstance){
	  ExecutionEntity execution = commandContext
		        .getExecutionEntityManager()
		        .findExecutionById(processDefinition.getProcessInstanceFromCache(businessKey, null));
	  execution.setVariables(variables);
	  String defaultSequenceFlow = (String) execution.getActivity().getProperty("default");
	  for (PvmTransition outgoingTransition : execution.getActivity().getOutgoingTransitions()) {
		  if (defaultSequenceFlow == null || !outgoingTransition.getId().equals(defaultSequenceFlow)) {
			  Condition condition = (Condition) outgoingTransition.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
			  if (condition == null || condition.evaluate(execution)) {
				   	ActivityImpl activity= (ActivityImpl)outgoingTransition.getDestination();
	   	        	if(activity.getActivityBehavior()  instanceof UserTaskActivityBehavior){
						String relationInfo = (String) outgoingTransition.getProperty("relationInfo");
						try {
							if (relationInfo!= null && !relationInfo.isEmpty()) {
								CommonUtil.convertJsonToList((String) outgoingTransition.getProperty("relationInfo"));
								processInstance.setReferenceRelation(CommonUtil.convertJsonToList((String) outgoingTransition.getProperty("relationInfo")));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	   	        	}
			  }
		  }
	}
  }*/
}
