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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.HasRevision;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.form.StartFormHandler;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.runtime.InterpretableExecution;
import org.activiti.engine.impl.task.OperatingFunctionHandler;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.OperatingFunction;

import com.eazytec.bpm.engine.TaskRole;


/**
 * @author Tom Baeyens
 * @author Daniel Meyer
 */
public class ProcessDefinitionEntity extends ProcessDefinitionImpl implements ProcessDefinition, PersistentObject, HasRevision {

  private static final long serialVersionUID = 1L;

  protected String key;
  protected int revision = 1;
  protected int version;
  protected String category;
  protected String deploymentId;
  protected String description;
  protected String resourceName;
  protected Integer historyLevel;
  protected StartFormHandler startFormHandler;
  protected String diagramResourceName;
  protected boolean isGraphicalNotationDefined;
  protected Map<String, TaskDefinition> taskDefinitions;
  protected boolean hasStartFormKey;
  protected int suspensionState = SuspensionState.ACTIVE.getStateCode();
  protected boolean isIdentityLinksInitialized = false;
  protected boolean isActiveVersion;
  protected List<IdentityLinkEntity> definitionIdentityLinkEntities = new ArrayList<IdentityLinkEntity>();
  protected Set<Expression> candidateStarterUserIdExpressions = new HashSet<Expression>();
  protected Set<Expression> candidateStarterGroupIdExpressions = new HashSet<Expression>();
  protected String classificationId;
  protected Set<Expression> adminUserIdExpressions = new HashSet<Expression>();
  protected Set<Expression> readerUserIdExpressions = new HashSet<Expression>();
  protected Set<Expression> readerGroupIdExpressions = new HashSet<Expression>();
  protected Set<Expression> endReaderUserIdExpressions = new HashSet<Expression>();
  protected Set<Expression> endReaderGroupIdExpressions = new HashSet<Expression>();
  protected OperatingFunctionHandler operatingFunctionHandler;  
  protected Map<TaskRole,OperatingFunction> operatingFunctions;
  protected boolean isSystemDefined;
  protected Integer orderNo;
  protected Expression endScriptExpression;
  protected Expression endScriptNameExpression;
  protected String modifiedUser;



public String getModifiedUser() {
	return modifiedUser;
}

public void setModifiedUser(String modifiedUser) {
	this.modifiedUser = modifiedUser;
}

public boolean getIsSystemDefined() {
	return isSystemDefined;
}

public void setIsSystemDefined(boolean isSystemDefined) {
	this.isSystemDefined = isSystemDefined;
}

public Integer getOrderNo() {
	return orderNo;
}

public void setOrderNo(Integer orderNo) {
	this.orderNo = orderNo;
}

public ProcessDefinitionEntity() {
    super(null);
  }
  
  public ExecutionEntity createProcessInstance(String businessKey, ActivityImpl initial) {
	  
    ExecutionEntity processInstance = null;
  
    if(initial == null) {
      processInstance = (ExecutionEntity) super.createProcessInstance();
    }else {
      processInstance = (ExecutionEntity) super.createProcessInstanceForInitial(initial);
    }

    processInstance.setExecutions(new ArrayList<ExecutionEntity>());
    processInstance.setProcessDefinition(processDefinition);
    // Do not initialize variable map (let it happen lazily)

    if (businessKey != null) {
    	processInstance.setBusinessKey(businessKey);
    }
    
    // Reset the process instance in order to have the db-generated process instance id available
    processInstance.setProcessInstance(processInstance);
    
    String initiatorVariableName = (String) getProperty(BpmnParse.PROPERTYNAME_INITIATOR_VARIABLE_NAME);
    if (initiatorVariableName!=null) {
      String authenticatedUserId = Authentication.getAuthenticatedUserId();
      processInstance.setVariable(initiatorVariableName, authenticatedUserId);
    }

    
    Context.getCommandContext().getHistoryManager()
      .recordProcessInstanceStart(processInstance,true);
 
   
    return processInstance;
  }
  
  public String getProcessInstanceFromCache(String businessKey, ActivityImpl initial) {
	  
	    ExecutionEntity processInstance = null;
	  
	    if(initial == null) {
	      processInstance = (ExecutionEntity) super.createProcessInstance();
	    }else {
	      processInstance = (ExecutionEntity) super.createProcessInstanceForInitial(initial);
	    }

	    processInstance.setProcessDefinition(processDefinition);
	    // Do not initialize variable map (let it happen lazily)

	    if (businessKey != null) {
	    	processInstance.setBusinessKey(businessKey);
	    }
	    
	    // Reset the process instance in order to have the db-generated process instance id available
	    processInstance.setProcessInstance(processInstance);
	    return processInstance.getId();
	  }
  
  public ExecutionEntity createProcessInstance(String businessKey) {
    return createProcessInstance(businessKey, null);
  }

  public ExecutionEntity createProcessInstance() {
    return createProcessInstance(null);
  }
  
  
  @Override
  protected InterpretableExecution newProcessInstance(ActivityImpl activityImpl) {
    ExecutionEntity processInstance = new ExecutionEntity(activityImpl);
    processInstance.insert();
    return processInstance;
  }
  
  public IdentityLinkEntity addIdentityLink(String userId, String groupId) {
    IdentityLinkEntity identityLinkEntity = IdentityLinkEntity.createAndInsert();
    getIdentityLinks().add(identityLinkEntity);
    identityLinkEntity.setProcessDef(this);
    identityLinkEntity.setUserId(userId);
    identityLinkEntity.setGroupId(groupId);
    identityLinkEntity.setType(IdentityLinkType.CANDIDATE);
    return identityLinkEntity;
  }
  
     
  public void deleteIdentityLink(String userId, String groupId, boolean isAddHisIdentity) {
    List<IdentityLinkEntity> identityLinks = Context
      .getCommandContext()
      .getIdentityLinkEntityManager()
      .findIdentityLinkByProcessDefinitionUserAndGroup(id, userId, groupId);
    if(isAddHisIdentity){
    
	    for (IdentityLinkEntity identityLink: identityLinks) {
	    	HistoricIdentityLinkEntity.createAndInsert(identityLink);
	      Context
	        .getCommandContext()
	        .getDbSqlSession()
	        .delete(identityLink);
	    }
    }else{
    	for (IdentityLinkEntity identityLink: identityLinks) {
	      Context
	        .getCommandContext()
	        .getDbSqlSession()
	        .delete(identityLink);
	    }
    }
  }
  
  public List<IdentityLinkEntity> getIdentityLinks() {
    if (!isIdentityLinksInitialized) {
		definitionIdentityLinkEntities = Context
		    .getCommandContext()
		    .getIdentityLinkEntityManager()
		    .findIdentityLinksByProcessDefinitionId(id);
	
      isIdentityLinksInitialized = true;
    }
    return definitionIdentityLinkEntities;
  }

  public String toString() {
    return "ProcessDefinitionEntity["+id+"]";
  }


  // getters and setters //////////////////////////////////////////////////////
  
  public Object getPersistentState() {
    Map<String, Object> persistentState = new HashMap<String, Object>();  
    persistentState.put("suspensionState", this.suspensionState);
    return persistentState;
  }
  
    public ExecutionEntity saveProcessInstance() {
	    ExecutionEntity processInstance = null;
	    processInstance = (ExecutionEntity) super.createProcessInstanceForInitial(initial);
	    processInstance.setExecutions(new ArrayList<ExecutionEntity>());
	    processInstance.setProcessDefinition(processDefinition);
	    
	    // Reset the process instance in order to have the db-generated process instance id available
	    processInstance.setProcessInstance(processInstance);
	    
	    String initiatorVariableName = (String) getProperty(BpmnParse.PROPERTYNAME_INITIATOR_VARIABLE_NAME);
	    if (initiatorVariableName!=null) {
	      String authenticatedUserId = Authentication.getAuthenticatedUserId();
	      processInstance.setVariable(initiatorVariableName, authenticatedUserId);
	    }
	    Context.getCommandContext().getHistoryManager()
	      .recordProcessInstanceStart(processInstance,true);
	    return processInstance;
	 }
  
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }
  
  public void setDescription(String description) {
	this.description = description;
  }
  
  public String getDescription() {
    return description;
  }

  public String getDeploymentId() {
    return deploymentId;
  }

  public void setDeploymentId(String deploymentId) {
    this.deploymentId = deploymentId;
  }
  
  public int getVersion() {
    return version;
  }
  
  public void setVersion(int version) {
    this.version = version;
  }

  public void setId(String id) {
    this.id = id;
  }
  
  public String getResourceName() {
    return resourceName;
  }

  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }

  public Integer getHistoryLevel() {
    return historyLevel;
  }

  public void setHistoryLevel(Integer historyLevel) {
    this.historyLevel = historyLevel;
  }

  public StartFormHandler getStartFormHandler() {
    return startFormHandler;
  }

  public void setStartFormHandler(StartFormHandler startFormHandler) {
    this.startFormHandler = startFormHandler;
  }

  public Map<String, TaskDefinition> getTaskDefinitions() {
    return taskDefinitions;
  }

  public void setTaskDefinitions(Map<String, TaskDefinition> taskDefinitions) {
    this.taskDefinitions = taskDefinitions;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }
  
  public String getDiagramResourceName() {
    return diagramResourceName;
  }

  public void setDiagramResourceName(String diagramResourceName) {
    this.diagramResourceName = diagramResourceName;
  }

  public boolean hasStartFormKey() {
    return hasStartFormKey;
  }
  
  public boolean getHasStartFormKey() {
    return hasStartFormKey;
  }
  
  public void setStartFormKey(boolean hasStartFormKey) {
    this.hasStartFormKey = hasStartFormKey;
  }

  public void setHasStartFormKey(boolean hasStartFormKey) {
    this.hasStartFormKey = hasStartFormKey;
  }
  
  public boolean isGraphicalNotationDefined() {
    return isGraphicalNotationDefined;
  }
  
  public void setGraphicalNotationDefined(boolean isGraphicalNotationDefined) {
    this.isGraphicalNotationDefined = isGraphicalNotationDefined;
  }
  
  
  
  public int getRevision() {
    return revision;
  }
  public void setRevision(int revision) {
    this.revision = revision;
  }
  
  public int getRevisionNext() {
    return revision+1;
  }
  
  public int getSuspensionState() {
    return suspensionState;
  }
  
  public void setSuspensionState(int suspensionState) {
    this.suspensionState = suspensionState;
  }

  public boolean isSuspended() {
    return suspensionState == SuspensionState.SUSPENDED.getStateCode();
  }
  
  public Set<Expression> getCandidateStarterUserIdExpressions() {
    return candidateStarterUserIdExpressions;
  }

  public void addCandidateStarterUserIdExpression(Expression userId) {
    candidateStarterUserIdExpressions.add(userId);
  }
  
  public void addAdminUserIdExpression(Expression userId) {
	    adminUserIdExpressions.add(userId);
  }
  
  public void addReaderUserIdExpression(Expression userId) {
	    readerUserIdExpressions.add(userId);
  }
  
  public void addReaderGroupIdExpression(Expression groupId) {
	    readerGroupIdExpressions.add(groupId);
  }
  
  public void addEndReaderUserIdExpression(Expression userId) {
	    endReaderUserIdExpressions.add(userId);
  }

  public void addEndReaderGroupIdExpression(Expression groupId) {
	    endReaderGroupIdExpressions.add(groupId);
  }

  public Set<Expression> getCandidateStarterGroupIdExpressions() {
    return candidateStarterGroupIdExpressions;
  }

  public void addCandidateStarterGroupIdExpression(Expression groupId) {
    candidateStarterGroupIdExpressions.add(groupId);
  }
  
  public String getDisplayName(){
	  if(this.getName()!=null){
		  return this.getName();
	  }else{
		  return this.getKey();
	  }
  }

  public boolean isActiveVersion() {
	return isActiveVersion;
  }

  public void setActiveVersion(boolean isActiveVersion) {
	this.isActiveVersion = isActiveVersion;
  }
  public String getClassificationId() {
		return classificationId;
	}

	public void setClassificationId(String classificationId) {
		this.classificationId = classificationId;
	}

	public Set<Expression> getAdminUserIdExpressions() {
		return adminUserIdExpressions;
	}

	public void setAdminUserIdExpressions(Set<Expression> adminUserIdExpressions) {
		this.adminUserIdExpressions = adminUserIdExpressions;
	}

	public Set<Expression> getReaderUserIdExpressions() {
		return readerUserIdExpressions;
	}

	public void setReaderUserIdExpressions(Set<Expression> readerUserIdExpressions) {
		this.readerUserIdExpressions = readerUserIdExpressions;
	}

	public Set<Expression> getReaderGroupIdExpressions() {
		return readerGroupIdExpressions;
	}

	public void setReaderGroupIdExpressions(Set<Expression> readerGroupIdExpressions) {
		this.readerGroupIdExpressions = readerGroupIdExpressions;
	}

	public Set<Expression> getEndReaderUserIdExpressions() {
		return endReaderUserIdExpressions;
	}

	public void setEndReaderUserIdExpressions(
			Set<Expression> endReaderUserIdExpressions) {
		this.endReaderUserIdExpressions = endReaderUserIdExpressions;
	}

	public Set<Expression> getEndReaderGroupIdExpressions() {
		return endReaderGroupIdExpressions;
	}

	public void setEndReaderGroupIdExpressions(
			Set<Expression> endReaderGroupIdExpressions) {
		this.endReaderGroupIdExpressions = endReaderGroupIdExpressions;
	}

	public OperatingFunctionHandler getOperatingFunctionHandler() {
		return operatingFunctionHandler;
	}

	public void setOperatingFunctionHandler(
			OperatingFunctionHandler operatingFunctionHandler) {
		this.operatingFunctionHandler = operatingFunctionHandler;
	}

	public Map<TaskRole, OperatingFunction> getOperatingFunctions() {
		return operatingFunctions;
	}

	public void setOperatingFunctions(
			Map<TaskRole, OperatingFunction> operatingFunctions) {
		this.operatingFunctions = operatingFunctions;
	}

	public Expression getEndScriptExpression() {
		return endScriptExpression;
	}

	public void setEndScriptExpression(Expression endScriptExpression) {
		this.endScriptExpression = endScriptExpression;
	}
	public Expression getEndScriptNameExpression() {
		return endScriptNameExpression;
	}

	public void setEndScriptNameExpression(Expression endScriptNameExpression) {
		this.endScriptNameExpression = endScriptNameExpression;
	}
}
