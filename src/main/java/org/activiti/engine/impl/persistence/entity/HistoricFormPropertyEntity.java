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

package org.activiti.engine.impl.persistence.entity;

import org.activiti.engine.history.HistoricFormProperty;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.util.ClockUtil;

/**
 * @author Tom Baeyens
 */
public class HistoricFormPropertyEntity extends HistoricDetailEntity implements HistoricFormProperty {

  private static final long serialVersionUID = 1L;
  
  protected String propertyId;
  protected String propertyValue;
  protected String columnId;
  protected String formId;
  
  public HistoricFormPropertyEntity() {
  }

  public HistoricFormPropertyEntity(ExecutionEntity execution, String propertyId, String propertyValue,String columnId,String formId) {
    this(execution, propertyId, propertyValue, null,columnId,formId);
  }
  
  public HistoricFormPropertyEntity(ExecutionEntity execution, String propertyId, String propertyValue, String taskId,String columnId,String formId) {
	    this.processInstanceId = execution.getProcessInstanceId();
	    this.executionId = execution.getId();
	    this.taskId = taskId;
	    this.propertyId = propertyId;
	    this.propertyValue = propertyValue;
	    this.columnId = columnId;
	    this.time = ClockUtil.getCurrentTime();
	    this.formId = formId;
	    HistoricActivityInstanceEntity historicActivityInstance = Context.getCommandContext().getHistoryManager().findActivityInstance(execution);
	    if (historicActivityInstance!=null) {
	      this.activityInstanceId = historicActivityInstance.getId();
	    }
	  } 

  public String getPropertyId() {
    return propertyId;
  }
  
  public void setPropertyId(String propertyId) {
    this.propertyId = propertyId;
  }
  
  public String getPropertyValue() {
    return propertyValue;
  }
  
  public void setPropertyValue(String propertyValue) {
    this.propertyValue = propertyValue;
  }

	public String getColumnId() {
		return columnId;
	}
	
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}
  
}
