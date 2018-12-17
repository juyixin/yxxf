package org.activiti.engine.impl.cmd;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.Condition;
import org.activiti.engine.impl.bpmn.behavior.ExclusiveGatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.NoneEndEventActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.task.OperatingFunction;

import com.eazytec.Constants;
import com.eazytec.exceptions.DataSourceException;

/**
 * This cmd class is to achieve return functionality that mean, 
 * if one process has 3 nodes, current active node is 3, 
 * user needs to go 2nd node ie returning to previous node
 * 
 * @author rajasekar
 *
 */
public class TaskJumpCmd extends NeedsActiveTaskCmd<Object>{

	private static final long serialVersionUID = 1L;
	  protected Map<String, Object> variables;
	  protected String activityId;
	  protected boolean needOrganizer = false;
	  protected String nodeType;
	  public TaskJumpCmd(String taskId,String activityId,boolean needOrganizer,String nodeType) {
	    super(taskId);
	    this.activityId = activityId;
	    this.needOrganizer = needOrganizer;
	    this.nodeType = nodeType;
	  }
	  
	protected Object execute(CommandContext commandContext, TaskEntity task)
			throws DataSourceException {
		if(!needOrganizer){
			Object object = null;
			String processInstanceId = task.getProcessInstanceId();
			if(nodeType.equalsIgnoreCase(Constants.BACKWARDNODE)){
				task.getExecution().setReturn(true);
			}else {
				task.getExecution().setJump(true);
			}
			if(activityId != null){
				task.getExecution().setJumpActivityId(activityId);
				object = processInstanceId;
			}else {
				//for jump automatically like in process time setting time out operation there is activity id will be passed
				TaskDefinition taskDefinition = null;
		    	PvmActivity jumpActivity = task.getExecution().getActivity();
/*		    	List<PvmTransition> incomingTransitions = jumpActivity.getIncomingTransitions();
		    	if(incomingTransitions != null && incomingTransitions.size() > 0){
		    		PvmTransition incomingTransition = incomingTransitions.get(incomingTransitions.size()-1);
		    		if(incomingTransition !=null){
		   	        	ActivityImpl activity= (ActivityImpl)incomingTransition.getDestination();
		   	        	//setting next node activity id for jump
						task.getExecution().setJumpActivityId(activity.getId());
		   	        	if(activity.getActivityBehavior()  instanceof UserTaskActivityBehavior){
		   	        		UserTaskActivityBehavior activityBehaviour  = (UserTaskActivityBehavior)activity.getActivityBehavior();
		   	        		taskDefinition = activityBehaviour.getTaskDefinition();
		   	        	}
		        	}
		    	}
		    	//returning task definition for set next node organizer
		    	object = taskDefinition;*/
		    	List<PvmTransition> incomingTransitions = jumpActivity.getOutgoingTransitions();
		    	if(incomingTransitions != null && incomingTransitions.size() > 0){
		    		PvmTransition incomingTransition = incomingTransitions.get(incomingTransitions.size()-1);
		    		if(incomingTransition !=null){
		   	        	ActivityImpl activity= (ActivityImpl)incomingTransition.getDestination();
		   	        	//setting next node activity id for jump
						task.getExecution().setJumpActivityId(activity.getId());
		   	        	if(activity.getActivityBehavior()  instanceof UserTaskActivityBehavior){
		   	        		UserTaskActivityBehavior activityBehaviour  = (UserTaskActivityBehavior)activity.getActivityBehavior();
		   	        		taskDefinition = activityBehaviour.getTaskDefinition();
		   	        	}else if(activity.getActivityBehavior()  instanceof NoneEndEventActivityBehavior){
		   	        		task.getExecution().end();
		   	        		return null;
		   	        	}
		        	}
		    	}
		    	//returning task definition for set next node organizer
		    	object = taskDefinition;
			}
			IdentityLinkEntity creatorIdentityLinkEntity = task.returnTask(TaskEntity.DELETE_REASON_JUMP);
			if(creatorIdentityLinkEntity != null){
				commandContext.getDbSqlSession()
			      .insert(new IdentityLinkEntity(creatorIdentityLinkEntity.getType(),creatorIdentityLinkEntity.getUserId(),
			    		  creatorIdentityLinkEntity.getGroupId(),processInstanceId,creatorIdentityLinkEntity.getOrder()));
			}
			return object;
		}else {
			TaskDefinition taskDefinition = null;
	    	PvmActivity jumpActivity = task.getExecution().getActivity(activityId);
	    	List<PvmTransition> incomingTransitions = jumpActivity.getIncomingTransitions();
	    	if(incomingTransitions != null && incomingTransitions.size() > 0){
	    		PvmTransition incomingTransition = incomingTransitions.get(incomingTransitions.size()-1);
	    		if(incomingTransition !=null){
	   	        	ActivityImpl activity= (ActivityImpl)incomingTransition.getDestination();
	   	        	if(activity.getActivityBehavior()  instanceof UserTaskActivityBehavior){
	   	        		UserTaskActivityBehavior activityBehaviour  = (UserTaskActivityBehavior)activity.getActivityBehavior();
	   	        		taskDefinition = activityBehaviour.getTaskDefinition();
	   	        	}
	        	}
	    	}
	    	return taskDefinition;
		}
	}
}
