package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;


/**
 * 
 * @author vinoth
 *
 */
public class TaskRecallCmd implements Command<Object>, Serializable {
	
	private static final long serialVersionUID = 1L;
	  
	protected String processInstanceId;
	protected String taskId;	  
	  public TaskRecallCmd(String processInstanceId,String taskId) {
		    this.processInstanceId = processInstanceId;
		    this.taskId = taskId;
	}

	public Object execute(CommandContext commandContext) {
	    TaskEntity execution = commandContext.getTaskEntityManager().findExecutionByProcessInstanceId(processInstanceId);
	    if(execution == null) {
	        throw new ActivitiException("Cannot find Task for Process Instance '"+processInstanceId+"'.");
	      }    
	    ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findExecutionById(execution.getExecutionId());

		TaskEntity activeTask = null;
		for(TaskEntity task : executionEntity.getTasks()){
			if(task.getSuspensionState() == SuspensionState.ACTIVE.getStateCode()){
				activeTask = task;
			}
		}
		activeTask.getExecution().setJump(true);
		activeTask.getExecution().setReturn(true);
	   	List<PvmTransition> incomingTransitions = executionEntity.getActivity().getIncomingTransitions();
		for (PvmTransition incomingTransition : incomingTransitions) {
			ActivityImpl activity= (ActivityImpl)incomingTransition.getSource();
			activeTask.getExecution().setJumpActivityId(activity.getId());
		}
		IdentityLinkEntity creatorIdentityLinkEntity = activeTask.returnTask(TaskEntity.DELETE_REASON_RECALL);
		if(creatorIdentityLinkEntity != null){
			commandContext.getDbSqlSession()
		      .insert(new IdentityLinkEntity(creatorIdentityLinkEntity.getType(),creatorIdentityLinkEntity.getUserId(),
		    		  creatorIdentityLinkEntity.getGroupId(),executionEntity.getId(),creatorIdentityLinkEntity.getOrder()));
		}
		return executionEntity.getId();
	}
}
