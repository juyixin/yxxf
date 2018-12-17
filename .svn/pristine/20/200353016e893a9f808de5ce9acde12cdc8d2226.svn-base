package org.activiti.engine.impl.cmd;

import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.bpmn.behavior.NoneStartEventActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.SubProcessActivityBehavior;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceException;

/**
 * This cmd class is to achieve return functionality that mean, 
 * if one process has 3 nodes, current active node is 3, 
 * user needs to go 2nd node ie returning to previous node
 * 
 * @author vinoth
 *
 */
public class TaskReturnCmd extends NeedsActiveTaskCmd<Object> {
    
	  private static final long serialVersionUID = 1L;
	  protected Map<String, Object> variables;
	  protected boolean needToDoReturn = true;
	  
	  public TaskReturnCmd(String taskId) {
	    super(taskId);
	  }
	  
	  public TaskReturnCmd(String taskId,boolean needToDoReturn) {
		    super(taskId);
		    this.needToDoReturn = needToDoReturn;
	 }
	protected Object execute(CommandContext commandContext, TaskEntity task)
			throws DataSourceException {
		task.getExecution().setReturn(true);
		String processInstanceId = task.getExecution().getProcessInstanceId();
		task.getExecution().setJumpActivityId(getLastOperatedActivity(task.getExecution(), task.getName()));
		if(needToDoReturn){
			IdentityLinkEntity creatorIdentityLinkEntity = task.returnTask(TaskEntity.DELETE_REASON_RETURNED);
			if(creatorIdentityLinkEntity != null){
			    Context
			      .getCommandContext()
			      .getDbSqlSession()
			      .insert(new IdentityLinkEntity(creatorIdentityLinkEntity.getType(),creatorIdentityLinkEntity.getUserId(),
			    		  creatorIdentityLinkEntity.getGroupId(),processInstanceId,creatorIdentityLinkEntity.getOrder()));
			}
		}else {
	    	/**
	    	 * checking the jumped activity is start node
	    	 * also check whether the start node has form or not
	    	 * if it has no from throwing error to user
	    	 */
			/*boolean isReturnedToStartNode = false;
			boolean noStartForm = true;
	    	PvmActivity jumpActivity = task.getExecution().getActivity(task.getExecution().getJumpActivityId());

			if(((String) jumpActivity.getProperty("type")).equalsIgnoreCase("startEvent")) {
				 isReturnedToStartNode = true;
				 if(((ProcessDefinitionEntity)jumpActivity.getProcessDefinition()).getHasStartFormKey()){
					noStartForm	= false;
				 }
			}
			if(isReturnedToStartNode && noStartForm) {
				return false;
			}*/
			return true;
		}
		return null;
	}
	
	protected String getLastOperatedActivity(ExecutionEntity execution, String taskName) {

		//获取历史任务中最后完成的任务
		List<HistoricTaskInstance> historicTaskInstances = Context.getCommandContext().getHistoricTaskInstanceEntityManager().
				  findLastCompletedTaskByProcessInstanceId(execution.getId(), taskName); // for normal process

		String lastOperatedTaskDefinionKey = null;
		if (historicTaskInstances.size() > 0) {
			
			lastOperatedTaskDefinionKey = historicTaskInstances.get(0).getTaskDefinitionKey();//获取任务ID
			
			List<ActivityImpl> activityImplList = execution.getActivity().getProcessDefinition().getActivities();
			
			for (PvmActivity activity : activityImplList) {
				
				if (activity.getId().equals(lastOperatedTaskDefinionKey)) {
					return activity.getId();
				}
				
				ActivityImpl act = (ActivityImpl) activity;
				
				if (act.getActivityBehavior() instanceof SubProcessActivityBehavior) {
					for (PvmActivity subProcessActivity : act.getActivities()) {
						if (subProcessActivity.getId().equals(lastOperatedTaskDefinionKey)) {
							return subProcessActivity.getId();
						}
					}
				}
			}
		}
		return null;
	}
}
