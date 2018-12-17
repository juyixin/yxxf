package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;


/**
 * Terminate Process instance ,delete all related {Task,variable instances,execution}
 * @author Revathi
 */
public class TerminateExecutionCmd  implements Command<Void>, Serializable  {

	private static final long serialVersionUID = 1L;
	private String processInstanceId;
	private Date endDate = null;
	private boolean bulkUpdate = false;
	private List<String> processInstanceIds;

	public TerminateExecutionCmd(String processInstanceId,Date currentDate) {
	    this.processInstanceId = processInstanceId;
	    this.endDate = currentDate;
	}

	public TerminateExecutionCmd(List<String> processInstanceIds,Date currentDate,boolean bulkUpdate) {
	    this.processInstanceIds = processInstanceIds;
	    this.endDate = currentDate;
	    this.bulkUpdate = bulkUpdate;
	}
	
	public Void execute(CommandContext commandContext) {
		if(!bulkUpdate){
			if(processInstanceId == null) {
			      throw new ActivitiException("processInstanceId is null");
			}
			//get HistoricProcessInstanceEntity to update end date for give process instance
			HistoricProcessInstanceEntity hisProEntity = commandContext.getHistoricProcessInstanceEntityManager().findHistoricProcessInstanceByInsId(processInstanceId);
			hisProEntity.setEndTime(endDate);
			//update the end date of HistoricProcessInstanceEntity
			commandContext.getHistoricProcessInstanceEntityManager().updateHistoricProcessInstanceEntity(hisProEntity);
			ExecutionEntity execution =  commandContext.getExecutionEntityManager().findChildExecutionsByProcessInstanceId(processInstanceId).get(0);
			//this will remove all execution related records
			execution.remove();
			return null;
		}else {
			//get HistoricProcessInstanceEntity to update end date for give process instance
			List<HistoricProcessInstanceEntity> hisProEntitys = commandContext.getHistoricProcessInstanceEntityManager().getAllProcessInstanceEntitiesByIds(processInstanceIds);
			for(HistoricProcessInstanceEntity hisProEntity :hisProEntitys){
				hisProEntity.setEndTime(endDate);
				hisProEntity.setDeleteReason("terminated");
				//update the end date of HistoricProcessInstanceEntity
				commandContext.getHistoricProcessInstanceEntityManager().updateHistoricProcessInstanceEntity(hisProEntity);
			}
			List<ExecutionEntity> executions =  commandContext.getExecutionEntityManager().getAllChildExecutionsByProcessInstanceIds(processInstanceIds);
			for(ExecutionEntity execution :executions){
				//this will remove all execution related records
				execution.remove();
			}
			return null;
		}

	}

}
