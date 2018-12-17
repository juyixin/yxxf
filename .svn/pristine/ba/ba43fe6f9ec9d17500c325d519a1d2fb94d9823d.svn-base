package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.AttachmentEntity;

public class GetAndInsertAttachmentListCmd implements Command<List<AttachmentEntity>>, Serializable{
	private static final long serialVersionUID = 1L;
	protected String taskId;
	protected boolean isPrevious = false;
	protected boolean isInsert = false;
	protected boolean isStartNode = false;
	protected String instanceId;
	
	protected List<String> attachmetIds = null;
	
	 public GetAndInsertAttachmentListCmd(boolean isPrevious,String taskId,boolean isInsert,boolean isStartNode,String instanceId) {
		    this.isPrevious = isPrevious;
		    this.taskId = taskId;
		    this.isInsert = isInsert;
		    this.isStartNode = isStartNode;
		    this.instanceId = instanceId;
	  }
	 
	 public GetAndInsertAttachmentListCmd(String taskId,boolean isInsert,List<String> attachmetIds) {
		    this.taskId = taskId;
		    this.attachmetIds = attachmetIds;
		    this.isInsert = isInsert;
	  }
	@Override
	public List<AttachmentEntity> execute(CommandContext commandContext) {
		List<AttachmentEntity> attachments = null; 
		//Get AttachmentFor previous task
		  if(taskId !=null && attachmetIds == null && isPrevious && !isStartNode){
			  List<String> taskIds = new ArrayList<String>();
			  String previousTaskId = Context.getCommandContext().getHistoricTaskInstanceEntityManager().getPreviousTaskId(instanceId);
			  taskIds.add(previousTaskId);
			  taskIds.add(taskId);
			  attachments = Context
				        .getCommandContext().getAttachmentEntityManager().selectAttachmentListByTaskIds(taskIds);
		  }else if(taskId !=null && attachmetIds == null && !isStartNode){
			  //Get attachment for current task
			  attachments = Context
		        .getCommandContext().getAttachmentEntityManager().findAttachmentEntitiesByTaskId(taskId);
		  }else if(isStartNode && instanceId != null && isPrevious){
			  // get attachment for start node
			  attachments = Context
				        .getCommandContext().getAttachmentEntityManager().selectAttachmentEntityByProcessInstanceId(instanceId);
		  }else if(attachmetIds!= null){
			  //Get Attachment By attachment IDs
			  attachments = Context
				        .getCommandContext().getAttachmentEntityManager().findAttachmentEntitiesByIds(attachmetIds);
		  }
		  if(isInsert){
			  Context
		        .getCommandContext().getAttachmentEntityManager().insertAttachments(attachments, taskId);
			 
		  }
	    return attachments;
	}
}
