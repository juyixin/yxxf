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

import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.util.ClockUtil;
import org.apache.commons.lang.StringUtils;

import com.eazytec.bpm.engine.TaskRole;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceException;

/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class SaveTaskFormCmd extends NeedsActiveTaskCmd<Object> {

	private static final long serialVersionUID = 1L;

	protected String taskId;
	protected Map<String, String> properties;
	protected Map<String, String[]> subProperties;
	protected Map<String, byte[]> files;
	protected Map<String, String> filePaths;
	protected String loggedInUserId;
	protected String proInsId;

	public SaveTaskFormCmd(String taskId, Map<String, String> properties) {
		super(taskId);
		this.taskId = taskId;
	}

	public SaveTaskFormCmd(String taskId, Map<String, String> properties, Map<String, String[]> subProperties, Map<String, byte[]> files, Map<String, String> filePaths, String loggedInUserId, String proInsId) {
		super(taskId);
		this.taskId = taskId;
		this.properties = properties;
		this.subProperties = subProperties;
		this.files = files;
		this.filePaths = filePaths;
		this.loggedInUserId = loggedInUserId;
		this.proInsId = proInsId;
	}

	protected Object execute(CommandContext commandContext, TaskEntity task) throws DataSourceException {
		try {
			TaskFormHandler taskFormHandler = task.getTaskDefinition().getTaskFormHandler();
			String subFormRowCount = properties.get("rowNumbers").toString();
			if (!subFormRowCount.isEmpty()) {
				task.getExecution().setHtmlSourceForSubForm(properties.get("htmlSourceForSubForm"));
				task.getExecution().setFormkey(properties.get("formId"));
			}
			task.setHtmlSourceForSubForm(properties.get("htmlSourceForSubForm"));
			properties.remove("htmlSourceForSubForm");
			task.setFormkey(properties.get("formId"));
			
			boolean isUpdate = false;
	    	if(StringUtils.isNotBlank(properties.get("id"))){
	    		isUpdate = true;
	    	}
			
			//提交表单内容
			taskFormHandler.submitFormProperties(properties, task.getExecution(), subProperties, files, filePaths, isUpdate, task.getTaskDefinitionKey());
			
			task.saveOff(loggedInUserId);
			if (task.getIsForStartNodeTask()) {
				List<IdentityLinkEntity> identityLinks = Context.getCommandContext().getIdentityLinkEntityManager().findIdentityLinksByTaskId(task.getId());
				boolean isOrgIDentityExist = false;
				String creatorUserId = "";
				for (IdentityLinkEntity identityLink : identityLinks) {
					if (identityLink.getType().equalsIgnoreCase(TaskRole.ORGANIZER.getName())) {
						isOrgIDentityExist = true;
					} else if (identityLink.getType().equalsIgnoreCase(TaskRole.CREATOR.getName())) {
						creatorUserId = identityLink.getUserId();
					}
				}
				if (proInsId != null) {
					HistoricProcessInstanceEntity hisProEntity = commandContext.getHistoricProcessInstanceEntityManager().findHistoricProcessInstanceByInsId(proInsId);
					if (hisProEntity.getIsDraft()) {
						hisProEntity.setIsDraft(false);
						hisProEntity.setStartTime(ClockUtil.getCurrentTime());
						// update the end date of HistoricProcessInstanceEntity
						commandContext.getHistoricProcessInstanceEntityManager().updateHistoricProcessInstanceEntity(hisProEntity);
					}
				}
				// create organizer identity link if it doesn't exist
				if (!isOrgIDentityExist) {
					IdentityLinkEntity identityLink = IdentityLinkEntity.createAndInsert();
					identityLink.setType(TaskRole.ORGANIZER.getName());
					identityLink.setTask(task);
					identityLink.setUserId(creatorUserId);
				}
			}
			// task.complete();
			// task.signOff();
		} catch (Exception e) {
			throw new BpmException(e.getMessage(), e);
		}
		return null;
	}

	@Override
	protected String getSuspendedTaskException() {
		return "Cannot save a form to a suspended task";
	}

}
