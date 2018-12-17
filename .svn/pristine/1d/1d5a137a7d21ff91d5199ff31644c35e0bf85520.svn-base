package com.eazytec.bpm.exceptions;

import java.util.List;

import org.activiti.engine.task.IdentityLink;

import com.eazytec.exceptions.BpmException;


/**
 * This exception is thrown when you try to claim a task that is already claimed
 * by someone else.
 * 
 * @author Jorg Heymans
 * @author Falko Menge 
 */
public class MembersAlreadyAddedException extends BpmException {
    
    private static final long serialVersionUID = 1L;

    /** the id of the task that is already claimed */
    private String taskId;
    
    private String identityLinkType;
    
    /** the assignee of the task that is already claimed */
    private List<String>userIds;
    
    public MembersAlreadyAddedException(String taskId, String identityLinkType, List<String>userIds) {
        super("Members already added as "+identityLinkType);
        this.taskId = taskId;
        this.identityLinkType = identityLinkType;
        this.userIds = userIds;
        
    }
    
    public String getTaskId() {
        return this.taskId;
    }

	public String getIdentityLinkType() {
		return identityLinkType;
	}

	public List<String> getUserIds() {
		return userIds;
	}
 
	public String getCommaSeparatedUserIds() {
		StringBuffer alreadyAddedUserIds = new StringBuffer();
		for (String userId : userIds) {
			alreadyAddedUserIds.append(userId);
			alreadyAddedUserIds.append(",");
		}
		return alreadyAddedUserIds.toString();
	}
    

}
