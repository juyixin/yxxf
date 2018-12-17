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
import java.util.List;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;


import com.eazytec.model.User;


/**
 * @author Joram Barrez
 * @author Falko Menge
 */
public class GetIdentityLinksForTaskCmd implements Command<List<IdentityLink>>, Serializable {
  
  private static final long serialVersionUID = 1L;
  protected String taskId;
  protected User user;
  protected List<String> userIds;
  protected String type;
  protected List<String> excludeTypes;


  public GetIdentityLinksForTaskCmd(String taskId) {
    this.taskId = taskId;
  }
  
  public GetIdentityLinksForTaskCmd(String taskId, String type) {
	    this.taskId = taskId;
	    this.type=type;
  }
  
  public GetIdentityLinksForTaskCmd(String taskId, User user) {
    this.taskId = taskId;
    this.user = user;
  }
  
  public GetIdentityLinksForTaskCmd(String taskId, User user, List<String> excludeTypes) {
	    this.taskId = taskId;
	    this.user=user;
	    this.excludeTypes = excludeTypes;
	  }
  
  public GetIdentityLinksForTaskCmd(String taskId, List<String>userIds, String type) {
	    this.taskId = taskId;
	    this.userIds = userIds;
	    this.type = type;
  }
  
  @SuppressWarnings({"unchecked", "rawtypes" })
  public List<IdentityLink> execute(CommandContext commandContext) {
    TaskEntity task = Context
      .getCommandContext()
      .getTaskEntityManager()
      .findTaskById(taskId);
    
    
    if(taskId!=null && type!=null && task != null){
    	return Context
    	        .getCommandContext()
    	        .getTaskEntityManager().findIdentityLinksForTask(taskId, type);
    } 
    
    if(taskId!=null && type!=null && task == null) {
    	return Context
    	        .getCommandContext()
    	        .getTaskEntityManager().findHistoricIdentityLinksForTask(taskId, type);
    }
    
    
    if(task==null){
    	return null;
    }

    if(user!=null){
    	if(excludeTypes!=null && excludeTypes.size()>0){
    		return task.getIdentityLinksApplicableForUser(user, excludeTypes);
    	}
    	return task.getIdentityLinksApplicableForUser(user);
    }
    if(userIds!=null && userIds.size()>0){
    	return Context
        .getCommandContext()
        .getTaskEntityManager().findIdentityLinksForUsers(taskId, userIds, type);

    }

    

    List<IdentityLink> identityLinks = (List) task.getIdentityLinks();
    
    // assignee is not part of identity links in the db. 
    // so if there is one, we add it here.
    // @Tom: we discussed this long on skype and you agreed ;-)
    // an assignee *is* an identityLink, and so must it be reflected in the API
    //
    // Note: we cant move this code to the TaskEntity (which would be cleaner),
    // since the task.delete cascased to all associated identityLinks 
    // and of course this leads to exception while trying to delete a non-existing identityLink
    if (task.getAssignee() != null) {
      IdentityLinkEntity identityLink = new IdentityLinkEntity();
      identityLink.setUserId(task.getAssignee());
      identityLink.setType(IdentityLinkType.ASSIGNEE);
      identityLinks.add(identityLink);
    }
    if (task.getOwner() != null) {
      IdentityLinkEntity identityLink = new IdentityLinkEntity();
      identityLink.setUserId(task.getOwner());
      identityLink.setType(IdentityLinkType.OWNER);
      identityLinks.add(identityLink);
    }
    
    return (List) task.getIdentityLinks();
  }
  
}
