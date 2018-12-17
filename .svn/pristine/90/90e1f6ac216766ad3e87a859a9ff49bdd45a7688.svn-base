package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricIdentityLinkDetailEntity;

import com.eazytec.common.util.CommonUtil;

public class AddHistoricIdentityDetails implements Command<Void>, Serializable {
	  
	private static final long serialVersionUID = 1L;
	protected String taskId = null;
	protected String proInsId = null;
	protected String userId = null;
	protected boolean isRead = false;
	
	 public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProInsId() {
		return proInsId;
	}

	public void setProInsId(String proInsId) {
		this.proInsId = proInsId;
	}

	public AddHistoricIdentityDetails(String proInsId,String userId,boolean isRead) {
		    this.proInsId = proInsId;
		    this.userId = userId;
		    this.isRead = isRead;
	  }
	public AddHistoricIdentityDetails(String taskId) {
	    this.taskId = taskId;
	}
	
	 
	public Void execute(CommandContext commandContext) {
		if (taskId != null || proInsId != null) {
			HistoricIdentityLinkDetailEntity historicIdentityLinkDetailEntity = new HistoricIdentityLinkDetailEntity();
			if (taskId != null) {
			       if (taskId.contains(",")) {
			       		String[] taskID = taskId.split(",");
						for(String task : taskID){
							historicIdentityLinkDetailEntity = new HistoricIdentityLinkDetailEntity();
							historicIdentityLinkDetailEntity.setTaskId(task);
							historicIdentityLinkDetailEntity.setUserId(CommonUtil
									.getLoggedInUserId());
							historicIdentityLinkDetailEntity.setIsRead(true);
							commandContext.getDbSqlSession().insert(historicIdentityLinkDetailEntity);
						}
					} else {
						historicIdentityLinkDetailEntity.setTaskId(taskId);
						historicIdentityLinkDetailEntity.setUserId(CommonUtil.getLoggedInUserId());
						historicIdentityLinkDetailEntity.setIsRead(true);
						commandContext.getDbSqlSession().insert(historicIdentityLinkDetailEntity);

					}
			} else {
				historicIdentityLinkDetailEntity.setProInsId(proInsId);
				historicIdentityLinkDetailEntity.setUserId(userId);
				historicIdentityLinkDetailEntity.setIsRead(isRead);
				commandContext.getDbSqlSession().insert(historicIdentityLinkDetailEntity);
			}


		} else {
			throw new ActivitiException("Cannot find taskId " + taskId);
		}
		return null;

	}
	 


}
