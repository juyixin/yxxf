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

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.AttachmentEntity;


/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class DeleteAttachmentCmd implements Command<Object>, Serializable {

  private static final long serialVersionUID = 1L;
  protected String attachmentId;
  protected List<String> attachmentIds;
  
  public DeleteAttachmentCmd(String attachmentId) {
    this.attachmentId = attachmentId;
  }
  
  public DeleteAttachmentCmd(List<String> attachmentIds) {
	    this.attachmentIds = attachmentIds;
	  }

  public Object execute(CommandContext commandContext) {
	  if(attachmentId!= null){
	    AttachmentEntity attachment = commandContext
	      .getDbSqlSession()
	      .selectById(AttachmentEntity.class, attachmentId);
	
	    commandContext
	      .getDbSqlSession()
	      .delete(attachment);
	  }else if(attachmentIds != null){
		  List<AttachmentEntity> attachmentEntitys = commandContext.getAttachmentEntityManager().findAttachmentEntitiesByIds(attachmentIds);
		  for(AttachmentEntity attachEntity : attachmentEntitys){
			  commandContext
		      .getDbSqlSession()
		      .delete(attachEntity);
		  }
	  }
    
    /*if (attachment.getContentId() != null) {
      commandContext
        .getByteArrayEntityManager()
        .deleteByteArrayById(attachment.getContentId());
    }
    
    if (attachment.getTaskId()!=null) {
      commandContext.getHistoryManager()
        .createAttachmentComment(attachment.getTaskId(), attachment.getProcessInstanceId(), attachment.getName(), false);
    }*/

    return null;
  }

}
