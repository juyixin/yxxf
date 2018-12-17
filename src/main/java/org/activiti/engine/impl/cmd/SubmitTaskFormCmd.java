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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.Condition;
import org.activiti.engine.impl.bpmn.behavior.ExclusiveGatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.NoneEndEventActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.SubProcessActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricIdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.OperatingFunction;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;

import com.eazytec.bpm.common.util.I18nUtil;
import com.eazytec.bpm.engine.TransactorType;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceException;


/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class SubmitTaskFormCmd extends NeedsActiveTaskCmd<Object> {

  private static final long serialVersionUID = 1L;
  
  protected String taskId;
  protected Map<String, String> properties;
  protected Map<String, String[]> subProperties;
  protected Map<String, byte[]>files;
  protected Map<String, String>filePaths;
  protected boolean needExecutionReturn = false;
  protected Map<String, Object> variables;
  protected boolean isStartNodeTask = false;
  protected String proInsId;
  
  public SubmitTaskFormCmd(String taskId, Map<String, String> properties) {
    super(taskId);
    this.taskId = taskId;
  }
  
  public SubmitTaskFormCmd(String taskId, Map<String, String> properties, Map<String, String[]> subProperties, Map<String, byte[]>files, Map<String, String>filePaths, boolean isStartNodeTask, String proInsId) {
	  super(taskId);
	    this.taskId = taskId;
	    this.properties = properties;
	    this.subProperties = subProperties;
	    this.files = files;
	    this.filePaths = filePaths;
	    this.isStartNodeTask = isStartNodeTask;
	    this.proInsId = proInsId;
  }
  
  public SubmitTaskFormCmd(Map<String, Object> rtFormValues, boolean needExecutionReturn) {
	    super(rtFormValues.get("taskId").toString());
	    this.taskId = rtFormValues.get("taskId").toString();
	    this.needExecutionReturn = needExecutionReturn;
	    this.variables = rtFormValues;
  }
  
  protected Object execute(CommandContext commandContext, TaskEntity task)throws DataSourceException {
	  try{
		  boolean collabrativeOperationExists = false;//是否存在协办操作
		  if(task.getSignOffType() != TransactorType.MULTI_PROCESS_SIGNOFF.getStateCode()){
		    for(IdentityLinkEntity identityLinkEntity : task.getIdentityLinks()){
		    	if(identityLinkEntity.getOperationType() != null){
			    	if(identityLinkEntity.getOperationType().equalsIgnoreCase(OperatingFunction.CONFLUENT_SIGNATURE)){
			    		collabrativeOperationExists =true;
			    		break;
			    	}
		    	}
		    }
		  }
		  if(needExecutionReturn){
			  
			  if(!collabrativeOperationExists){
				  ExecutionEntity execution = task.getExecution();
				  //execution.setVariables(variables);
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
				  } //end checking null conditions
				  boolean isProcessEndNode = false;
				  for (PvmTransition outgoingTransition : execution.getActivity().getOutgoingTransitions()) {
			    	  if (defaultSequenceFlow == null || !outgoingTransition.getSource().getId().equals(defaultSequenceFlow)) {
				   	        Condition condition = (Condition) outgoingTransition.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
				   	        if(condition != null){
					   	        conditionStatisfied = condition.evaluate(execution);
				   	        }
				   	       // List<Map<String,String>> referenceRelation = (List<Map<String, String>>) outgoingTransition.getProperty(BpmnParse.PROPERTYNAME_REFERENCE_RELATION);
				   	        if (condition == null || conditionStatisfied) {
				   	        	ActivityImpl activity= (ActivityImpl)outgoingTransition.getDestination();
				   	        	if(activity.getActivityBehavior()  instanceof UserTaskActivityBehavior){
				   	        		UserTaskActivityBehavior activityBehaviour  = (UserTaskActivityBehavior)activity.getActivityBehavior();
									String relationInfo = (String) outgoingTransition.getProperty("relationInfo");
									try {
										if (relationInfo!= null && !relationInfo.isEmpty()) {
											List<Map<String, Object>> referenceRelation = CommonUtil.convertJsonToList((String) outgoingTransition.getProperty("relationInfo"));
											activityBehaviour.getTaskDefinition().setReferenceRelation(referenceRelation);
										}
									} catch (JSONException e) {
										  throw new ActivitiException(I18nUtil.getMessageProperty("error.jsonerror.onSubmit")+" Process Definition Name "+outgoingTransition.getProcessDefinition().getName());
									}
				   	        		taskDefinition = activityBehaviour.getTaskDefinition();
				   	        		//taskDefinition.setReferenceRelation(referenceRelation);
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
						   	        		return activityBehaviour.getTaskDefinition();
						   	        	}
				   	        	    }else {
				   	        	    	if (defaultSequenceFlow != null) {
					   	        	        PvmTransition defaultTransition = execution.getActivity().findOutgoingTransition(defaultSequenceFlow);
					   	        	        if (defaultTransition != null) {
								   	        	ActivityImpl xorActivity= (ActivityImpl)defaultTransition.getDestination();
								   	        	if(xorActivity.getActivityBehavior()  instanceof UserTaskActivityBehavior){
								   	        		UserTaskActivityBehavior activityBehaviour  = (UserTaskActivityBehavior)xorActivity.getActivityBehavior();
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
							  		taskDefinition = new TaskDefinition();
							  	} else if(activity.getActivityBehavior()  instanceof NoneEndEventActivityBehavior) {
							  		isProcessEndNode = true;
							  	}//end
				   	        	
				   	        	// Check and set subprocess end continue process 
				   	        	if(outgoingTransition.getParentSource() != null && taskDefinition == null){
					   	         for (PvmTransition subOutgoingTransition : execution.getActivity(outgoingTransition.getParentSource().getId()).getOutgoingTransitions()) {
					   	        	if (defaultSequenceFlow == null || !subOutgoingTransition.getSource().getId().equals(defaultSequenceFlow)) {
					   	        		Condition subCondition = (Condition) subOutgoingTransition.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
							   	        if(subCondition != null){
								   	        conditionStatisfied = subCondition.evaluate(execution);
							   	        }
								   	     if (subCondition == null || conditionStatisfied) {
							   	        	ActivityImpl newActivity= (ActivityImpl)subOutgoingTransition.getDestination();
							   	        	if(newActivity.getActivityBehavior()  instanceof UserTaskActivityBehavior){
							   	        		UserTaskActivityBehavior activityBehaviour  = (UserTaskActivityBehavior)newActivity.getActivityBehavior();
												String relationInfo = (String) subOutgoingTransition.getProperty("relationInfo");
												try {
													if (relationInfo!= null && !relationInfo.isEmpty()) {
														List<Map<String, Object>> referenceRelation = CommonUtil.convertJsonToList((String) subOutgoingTransition.getProperty("relationInfo"));
														activityBehaviour.getTaskDefinition().setReferenceRelation(referenceRelation);
													}
												} catch (JSONException e) {
													throw new ActivitiException("exception occured while generating json for transactor relationship "+
															"in process "+subOutgoingTransition.getProcessDefinition().getName());
												}
							   	        		taskDefinition = activityBehaviour.getTaskDefinition();
							   	        		//taskDefinition.setReferenceRelation(referenceRelation);
							   	        		//Checking if there is any condition exist
							   	        	}else if(newActivity.getActivityBehavior()  instanceof SubProcessActivityBehavior){
										  		taskDefinition = new TaskDefinition();
										  	} else if(newActivity.getActivityBehavior()  instanceof ExclusiveGatewayActivityBehavior){

							   	        	    PvmTransition outgoingSeqFlow = null;
							   	        	    PvmTransition remainingSeqFlow = null;
							   	        	    Iterator<PvmTransition> transitionIterator = newActivity.getOutgoingTransitions().iterator();
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
									   	        		return activityBehaviour.getTaskDefinition();
									   	        	}
							   	        	    }else {
							   	        	    	if (defaultSequenceFlow != null) {
								   	        	        PvmTransition defaultTransition = execution.getActivity().findOutgoingTransition(defaultSequenceFlow);
								   	        	        if (defaultTransition != null) {
											   	        	ActivityImpl xorActivity= (ActivityImpl)defaultTransition.getDestination();
											   	        	if(xorActivity.getActivityBehavior()  instanceof UserTaskActivityBehavior){
											   	        		UserTaskActivityBehavior activityBehaviour  = (UserTaskActivityBehavior)xorActivity.getActivityBehavior();
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
							   	        	
										  	}
						   	        	}else{
						   	        		throw new ActivitiException(I18nUtil.getMessageProperty("errors.sequenceflow.notexist"));
						   	        	}
					   	        	}
					   	        	
					   	          }
					   	        }
							}
			   	        }
			    	    if(taskDefinition != null){
					  		break;
			    	    }
				   	}
					if(taskDefinition != null){
						//variables.remove("htmlSourceForSubForm");
				  		return taskDefinition;
				  	}else if(!conditionStatisfied && !isProcessEndNode){
  	        	        //No sequence flow could be found, not even a default one
  	        	    	throw new ActivitiException(I18nUtil.getMessageProperty("errors.sequenceflow.notexist"));
				  	}
			  }
			  return null;
		  }else {
		    TaskDefinition taskDefinition = task.getTaskDefinition();
		    /**
		     * Get count of newly cloned rows and save in execution
		     */
		    if(properties.containsKey("rowNumbers")) {
			   	 String subFormRowCount = properties.get("rowNumbers").toString();
			   	 if(!subFormRowCount.isEmpty()) {
						task.getExecution().setHtmlSourceForSubForm(properties.get("htmlSourceForSubForm"));
						task.getExecution().setFormkey(properties.get("formId"));
			   	 }
			    properties.remove("htmlSourceForSubForm");
		    }
			
		    //setReferenceRelation(task,taskDefinition);
		    TaskFormHandler taskFormHandler = taskDefinition.getTaskFormHandler();
		    if(properties != null){
		    	boolean isUpdate = false;
		    	if(StringUtils.isNotBlank(properties.get("id"))){
		    		isUpdate = true;
		    	}
		    	taskFormHandler.submitFormProperties(properties, task.getExecution(), subProperties, files, filePaths, isUpdate, task.getTaskDefinitionKey());
		    }
		    //task.complete();
			//remove start node form from process
		    if(isStartNodeTask){
		    	// properties will be null if default task does not have form
		    	if(properties != null){
		    		proInsId = properties.get("processInstanceId");
		    	}  
		    	//set true to the process instance draft status for start node task complete
		    	HistoricProcessInstanceEntity hisProEntity = commandContext.getHistoricProcessInstanceEntityManager().findHistoricProcessInstanceByInsId(proInsId);
				if(hisProEntity.getIsDraft()){
					hisProEntity.setIsDraft(false);
					hisProEntity.setStartTime(ClockUtil.getCurrentTime());
					hisProEntity.setStartNodeAssignee(CommonUtil.getLoggedInUserId());
					//update the end date of HistoricProcessInstanceEntity
					commandContext.getHistoricProcessInstanceEntityManager().updateHistoricProcessInstanceEntity(hisProEntity);
				}
				
		    	task.complete();
		    	//Insert processed user for first task(i.e start node task) 
		    	IdentityLinkEntity currentIdentity = task.getCreatorIDentityLink(task.getId());
				HistoricIdentityLinkEntity historicIdentityLinkEntity = new HistoricIdentityLinkEntity(currentIdentity);
				historicIdentityLinkEntity.setType(IdentityLinkType.PROCESSED_USER);
			//	historicIdentityLinkEntity.setIdentityLinkEntityId(null);
				HistoricIdentityLinkEntity.createAndInsert(historicIdentityLinkEntity); //end
				HistoricIdentityLinkEntity historicIdentityLinkEntity1 = new HistoricIdentityLinkEntity(currentIdentity);
				historicIdentityLinkEntity1.setType(IdentityLinkType.ORGANIZER);
				HistoricIdentityLinkEntity.createAndInsert(historicIdentityLinkEntity1);
				
		    }else if(!collabrativeOperationExists){
			    task.signOff();
		    }else {
		    	task.deleteIdentityLink(CommonUtil.getLoggedInUserId(), null, IdentityLinkType.ORGANIZER, false);
		    }
		    if(task.getExecution().getActivity() != null){
			    if(task.getExecution().getActivity().getActivityBehavior() instanceof NoneEndEventActivityBehavior){
			    	// Get End event start script
		            if(task.getExecution().getActivity().getProperty("endEventStartScript") != null) {
	   					Map<String,String> startScriptContent =   (Map<String, String>) task.getExecution().getActivity().getProperty("endEventStartScript");
						if(startScriptContent.containsKey("functionName")) {
							taskDefinition.setEndEventStartScriptNameExpression(startScriptContent.get("functionName"));
						}
						if(startScriptContent.containsKey("script")) {
							taskDefinition.setEndEvenStartScriptExpression(startScriptContent.get("script"));
						}
		            }
		            if(task.getExecution().getActivity().getProperty("isArchive") != null){
		            	if(Boolean.parseBoolean(task.getExecution().getActivity().getProperty("isArchive").toString()))
		            		return task;
		            }
			    }
		    }
		    return taskDefinition;
		  }
	  }catch(Exception e){
		  throw new BpmException(e.getMessage(), e);
	  }
  }
  /**
   * Get and set the reference relation from the sequence and set it to the task
   * @param task
   * @param taskDefinition
   *//*
  private void setReferenceRelation(TaskEntity task,TaskDefinition taskDefinition){
	  ExecutionEntity execution = task.getExecution();
	  execution.setVariables(properties);
	  String defaultSequenceFlow = (String) execution.getActivity().getProperty("default");
	  for (PvmTransition outgoingTransition : execution.getActivity().getOutgoingTransitions()) {
		  if (defaultSequenceFlow == null || !outgoingTransition.getId().equals(defaultSequenceFlow)) {
			  Condition condition = (Condition) outgoingTransition.getProperty(BpmnParse.PROPERTYNAME_CONDITION);
	   	       // List<Map<String,String>> referenceRelation = (List<Map<String, String>>) outgoingTransition.getProperty(BpmnParse.PROPERTYNAME_REFERENCE_RELATION);
	   	        if (condition == null || condition.evaluate(execution)) {
	   	        	ActivityImpl activity= (ActivityImpl)outgoingTransition.getDestination();
	   	        	if(activity.getActivityBehavior()  instanceof UserTaskActivityBehavior){
	   	        		String relationInfo = (String)outgoingTransition.getProperty("relationInfo");
					  try {
						  if(relationInfo!= null &&!relationInfo.isEmpty()){
							  CommonUtil.convertJsonToList((String)outgoingTransition.getProperty("relationInfo"));
							  taskDefinition.setReferenceRelation( CommonUtil.convertJsonToList((String)outgoingTransition.getProperty("relationInfo")));
						  }
					  } catch (JSONException e) {
							e.printStackTrace();
						}
	   	        	}
	   	        }
		  }
	  }
  }*/
  
  @Override
  protected String getSuspendedTaskException() {
    return "Cannot submit a form to a suspended task";
  }
  
}
