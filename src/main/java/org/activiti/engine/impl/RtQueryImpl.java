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
package org.activiti.engine.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
 
import org.activiti.engine.ActivitiException;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.impl.variable.VariableTypes;
import org.activiti.engine.query.RtQuery;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import com.eazytec.exceptions.BpmException;
import com.eazytec.model.MetaTable;

/**

 * @author madan
 */
public class RtQueryImpl extends AbstractQuery<RtQuery, MetaTable> implements RtQuery {
  
  private static final long serialVersionUID = 1L;

  private String tableName;
  
  private List<String> columns;
  
  private List<String> values;
  
  private String primaryKeyVal;

  public String getPrimaryKeyVal() {
	return primaryKeyVal;
}

public void setPrimaryKeyVal(String primaryKeyVal) {
	this.primaryKeyVal = primaryKeyVal;
}

public static long getSerialversionuid() {
	return serialVersionUID;
}

public String getTableName() {
	return tableName;
}

public List<String> getColumns() {
	return columns;
}

public List<String> getValues() {
	return values;
}

public RtQueryImpl() {
  }
  
  public RtQueryImpl(CommandContext commandContext) {
    super(commandContext);
  }
  
  public RtQueryImpl(CommandExecutor commandExecutor) {
    super(commandExecutor);
  }
  
  public RtQuery tableName(String tableName){
	  this.tableName=tableName;
	  return this;
  }
	
  public RtQuery columns(List<String> columns){
	  this.columns=columns;
	  return this;
  }
	
  public RtQuery values(List<String> values){
	  this.values=values;
	  return this;
  }
  
  public List executeList(CommandContext commandContext, Page page) {
	    //ensureVariablesInitialized();
	    checkQueryOk();
	    return commandContext
	      .getTableEntityManager()
	      .storeRtValues(this);
	  }
	  
	  public long executeCount(CommandContext commandContext) {
	    //ensureVariablesInitialized();
	    checkQueryOk();
	    return commandContext
	      .getTableEntityManager()
	      .findTaskCountByQueryCriteria(this);
	  }

	  
		  
  
	  protected void checkQueryOk() {
		    if (orderProperty != null) {
		      throw new ActivitiException("Invalid query: call asc() or desc() after using orderByXX()");
		    }
		  }
  
 
  
  
}
