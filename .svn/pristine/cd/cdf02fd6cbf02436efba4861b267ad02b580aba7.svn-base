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

import org.activiti.engine.ActivitiException;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricIdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.IdentityLinkType;

import com.eazytec.bpm.common.util.I18nUtil;


/**
 * @author Tom Baeyens
 */
public class GetHistoricTaskFormCmd implements Command<Object>, Serializable {

  private static final long serialVersionUID = 1L;
  protected HistoricTaskInstance historicTask;
  protected boolean needHistoricIdentityLinkEntity = false;
  protected boolean needTaskDefinion = false;
  
  public GetHistoricTaskFormCmd(HistoricTaskInstance historicTask) {
    this.historicTask = historicTask;
  }

  public GetHistoricTaskFormCmd(HistoricTaskInstance historicTask,boolean needHistoricIdentityLinkEntity) {
	    this.historicTask = historicTask;
	    this.needHistoricIdentityLinkEntity = needHistoricIdentityLinkEntity;
  }
  
  public GetHistoricTaskFormCmd(HistoricTaskInstance historicTask,boolean needHistoricIdentityLinkEntity,boolean needTaskDefinion) {
	    this.historicTask = historicTask;
	    this.needHistoricIdentityLinkEntity = needHistoricIdentityLinkEntity;
	    this.needTaskDefinion = needTaskDefinion;
  }
  
  public Object execute(CommandContext commandContext) {    
	
	  if(needHistoricIdentityLinkEntity){
		  List<HistoricIdentityLinkEntity> historicIdentityLinks = commandContext.getHistoricTaskInstanceEntityManager().getHistoricIdentityLinksByTaskId(historicTask);
		  for(HistoricIdentityLinkEntity historicIdentityLink : historicIdentityLinks){
			  if(historicIdentityLink.getType().equals(IdentityLinkType.PROCESSED_USER)){
				  return historicIdentityLink;
			  }
		  }
	  }else if(needTaskDefinion){
		  return historicTask.getTaskDefinition();
	  }else {
	    if(historicTask.getTaskDefinition() != null) {
	        TaskFormHandler taskFormHandler = historicTask.getTaskDefinition().getTaskFormHandler();
	        if (taskFormHandler == null) {
	  		  throw new ActivitiException(I18nUtil.getMessageProperty("error.noTaskHandler.found")+" Task Id : "+historicTask.getId());    	
	        }
	        
	        return taskFormHandler.createTaskForm(historicTask);
	      } else {
	        // Standalone task, no TaskFormData available
	        return null;
	      }  
	  }
	  return null;
   }
}
