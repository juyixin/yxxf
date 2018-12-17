package org.activiti.engine.impl.cmd;

import java.util.Map;

import org.activiti.engine.impl.bpmn.behavior.NoneEndEventActivityBehavior;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.OperatingFunction;
import org.springframework.security.core.context.SecurityContextHolder;

import com.eazytec.bpm.engine.TransactorType;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceException;
import com.eazytec.model.User;


/**
 * @author madan
 */
public class UpdateTaskFormCmd extends NeedsActiveTaskCmd<Object> {

  private static final long serialVersionUID = 1L;
  
  protected String taskId;
  protected Map<String, String> properties;
  protected Map<String, String[]> subProperties;
  protected Map<String, byte[]>files;
  protected Map<String, String>filePaths;
  protected boolean isSignOff;
  
  public UpdateTaskFormCmd(String taskId, Map<String, String> properties) {
    super(taskId);
    this.taskId = taskId;
    this.isSignOff = false;
    //this.properties = properties;
  }
  
//  public SubmitTaskFormCmd(String taskId, Map<String, String> properties, Map<String, String[]> subProperties) {
//	    super(taskId);
//	    this.taskId = taskId;
//	    this.properties = properties;
//	    this.subProperties = subProperties;
//  }
  
  public UpdateTaskFormCmd(String taskId, Map<String, String> properties, Map<String, String[]> subProperties, Map<String, byte[]>files, Map<String, String>filePaths) {
	    super(taskId);
	    this.taskId = taskId;
	    this.properties = properties;
	    this.subProperties = subProperties;
	    this.files = files;
	    this.filePaths = filePaths;
	    this.isSignOff = true;
}
  
  public UpdateTaskFormCmd(String taskId, Map<String, String> properties, Map<String, String[]> subProperties, Map<String, byte[]>files, Map<String, String>filePaths, boolean isSignOff) {
	    super(taskId);
	    this.taskId = taskId;
	    this.properties = properties;
	    this.subProperties = subProperties;
	    this.files = files;
	    this.filePaths = filePaths;
	    this.isSignOff = isSignOff;
}
  
  protected Object execute(CommandContext commandContext, TaskEntity task)throws DataSourceException {
    try {
	   	 String subFormRowCount = properties.get("rowNumbers").toString();
	   	 if(!subFormRowCount.isEmpty()) {
				task.getExecution().setHtmlSourceForSubForm(properties.get("htmlSourceForSubForm"));
				task.getExecution().setFormkey(properties.get("formId"));
	   	 }
		String isStartNodeTask = properties.get("isStartNodeTask");
	    task.setHtmlSourceForSubForm(properties.get("htmlSourceForSubForm"));
	  	properties.remove("htmlSourceForSubForm");
	  	task.setFormkey(properties.get("formId"));
        TaskFormHandler taskFormHandler = task.getTaskDefinition().getTaskFormHandler();
        taskFormHandler.submitFormProperties(properties, task.getExecution(), subProperties, files, filePaths, true,task.getTaskDefinitionKey());
    	//set true to the process instance draft status for start node task complete
        if( isStartNodeTask.equalsIgnoreCase("true") ) {
 	    	HistoricProcessInstanceEntity hisProEntity = commandContext.getHistoricProcessInstanceEntityManager().findHistoricProcessInstanceByInsId(properties.get("instanceId"));
 				hisProEntity.setStartNodeAssignee(CommonUtil.getLoggedInUserId());
 				commandContext.getHistoricProcessInstanceEntityManager().updateHistoricProcessInstanceEntity(hisProEntity);
 		}
        //task.complete();
        boolean collabrativeOperationExists = false;
        if(task.getSignOffType() != TransactorType.MULTI_PROCESS_SIGNOFF
    				.getStateCode()){
        	for(IdentityLinkEntity identityLinkEntity : task.getIdentityLinks()){
        		if(identityLinkEntity.getOperationType() != null){
        	    	if(identityLinkEntity.getOperationType().equalsIgnoreCase(OperatingFunction.CONFLUENT_SIGNATURE)){
        	    		collabrativeOperationExists =true;
        	    		break;
        	    	}
            	}
            }
        }
        if(!collabrativeOperationExists){
        	if(isSignOff){
            	task.signOff();
            }else{
            	task.saveOff(null);
            }    
        }else {
        	task.deleteIdentityLink(CommonUtil.getLoggedInUserId(), null, IdentityLinkType.ORGANIZER, false);
        }
        if(task.getExecution().getActivity() != null){
    	    if(task.getExecution().getActivity().getActivityBehavior() instanceof NoneEndEventActivityBehavior){
                if(task.getExecution().getActivity().getProperty("isArchive") != null){
                	if(Boolean.parseBoolean(task.getExecution().getActivity().getProperty("isArchive").toString()))
                		return task;
                }
    	    }
        }
    } catch(Exception e) {
  	  throw new BpmException(e.getMessage(), e);
    }

    return null;
  }
  
  @Override
  protected String getSuspendedTaskException() {
    return "Cannot submit a form to a suspended task";
  }
  
}
