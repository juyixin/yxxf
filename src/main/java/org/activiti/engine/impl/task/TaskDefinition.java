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
package org.activiti.engine.impl.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.persistence.entity.HistoricIdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.OperatingFunction;

import com.eazytec.bpm.engine.TaskRole;

/**
 * Container for task definition information gathered at parsing time.
 * 
 * @author Joram Barrez
 */
public class TaskDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String key;

	// assignment fields
	protected Expression nameExpression;
	protected Expression descriptionExpression;
	protected Expression assigneeExpression;
	protected Set<Expression> candidateUserIdExpressions = new HashSet<Expression>();
	protected Set<Expression> candidateGroupIdExpressions = new HashSet<Expression>();
	protected Set<Expression> readerUserIdExpressions = new HashSet<Expression>();
	protected Set<Expression> readerGroupIdExpressions = new HashSet<Expression>();
	protected Set<Expression> adminGroupIdExpression = new HashSet<Expression>();

	public Set<Expression> getAdminGroupIdExpression() {
		return adminGroupIdExpression;
	}

	public void setAdminGroupIdExpression(Set<Expression> adminGroupIdExpression) {
		this.adminGroupIdExpression = adminGroupIdExpression;
	}

	protected Set<Expression> adminUserIdExpressions = new HashSet<Expression>();

	protected Expression dueDateExpression;
	protected Expression priorityExpression;
	protected Expression signOffTypeExpression;
	protected Expression startScriptExpression;
	protected Expression endScriptExpression;

	protected Expression startScriptNameExpression;
	protected Expression endScriptNameExpression;

	protected Expression skipRepeatedExpression;

	protected Expression skipEmptyExpression;

	protected Expression nodeType;

	protected Expression dynamicOrganizer;
	protected Expression dynamicReader;
	protected Expression formName;
	protected Expression dynamicOrganizerType;
	protected Expression dynamicReaderType;
	protected Expression isForStartNode;

	protected String endEvenStartScriptExpression;
	protected String endEventStartScriptNameExpression;

	protected String StartNodeDynamicOrganizer;
	protected String startFormName;
	protected String StartNodedynamicOrganizerType;

	protected Map<String, String> timeSettingDetails = new HashMap<String, String>();
	protected List<Map<String, Object>> referenceRelation = new ArrayList<Map<String, Object>>();
	protected List<Map<String, String>> formFieldAutomatic = new ArrayList<Map<String, String>>();
	protected String isOpinion;


	public String getIsOpinion() {
		return isOpinion;
	}

	public void setIsOpinion(String isOpinion) {
		this.isOpinion = isOpinion;
	}

	public List<Map<String, String>> getFormFieldAutomatic() {
		return formFieldAutomatic;
	}

	public void setFormFieldAutomatic(
			List<Map<String, String>> formFieldAutomatic) {
		this.formFieldAutomatic = formFieldAutomatic;
	}

// form fields
  protected TaskFormHandler taskFormHandler;
  
  protected OperatingFunctionHandler operatingFunctionHandler;
  
  protected Map<TaskRole,OperatingFunction> operatingFunctions;
  
  // task listeners
  protected Map<String, List<TaskListener>> taskListeners = new HashMap<String, List<TaskListener>>();
  
  public static String CANDIDATE_ORDER_SEPERATOR = "-";
  
  public TaskDefinition(TaskFormHandler taskFormHandler) {
    this.taskFormHandler = taskFormHandler;
  }
  public TaskDefinition( ) {
  }
  // getters and setters //////////////////////////////////////////////////////

  public Expression getNameExpression() {
    return nameExpression;
  }

  public void setNameExpression(Expression nameExpression) {
    this.nameExpression = nameExpression;
  }

  public Expression getDescriptionExpression() {
    return descriptionExpression;
  }

  public void setDescriptionExpression(Expression descriptionExpression) {
    this.descriptionExpression = descriptionExpression;
  }

  public Expression getAssigneeExpression() {
    return assigneeExpression;
  }

  public void setAssigneeExpression(Expression assigneeExpression) {
    this.assigneeExpression = assigneeExpression;
  }

  public Set<Expression> getCandidateUserIdExpressions() {
    return candidateUserIdExpressions;
  }

  public void addCandidateUserIdExpression(Expression userId) {
    candidateUserIdExpressions.add(userId);
  }

  public Set<Expression> getCandidateGroupIdExpressions() {
    return candidateGroupIdExpressions;
  }

  public void addCandidateGroupIdExpression(Expression groupId) {
    candidateGroupIdExpressions.add(groupId);
  }

  public Expression getPriorityExpression() {
    return priorityExpression;
  }

  public void setPriorityExpression(Expression priorityExpression) {
    this.priorityExpression = priorityExpression;
  }

  public TaskFormHandler getTaskFormHandler() {
    return taskFormHandler;
  }

  public void setTaskFormHandler(TaskFormHandler taskFormHandler) {
    this.taskFormHandler = taskFormHandler;
  }
  
	public void addAdminUserIdExpression(Expression userId) {
		    adminUserIdExpressions.add(userId);
	}
	
	
//  public OperatingFunctionHandler getOperatingFunctionHandler() {
//	return operatingFunctionHandler;
//  }
//
//  public void setOperatingFunctionHandler(
//		OperatingFunctionHandler operatingFunctionHandler) {
//	this.operatingFunctionHandler = operatingFunctionHandler;
//  }
  
  

public Set<Expression> getAdminUserIdExpressions() {
	return adminUserIdExpressions;
}

public Expression getSkipRepeatedExpression() {
	return skipRepeatedExpression;
}

public void setSkipRepeatedExpression(Expression skipRepeatedExpression) {
	this.skipRepeatedExpression = skipRepeatedExpression;
}

public Expression getNodeTypeExpression() {
	return nodeType;
}

public void setNodeTypeExpression(Expression nodeType) {
	this.nodeType = nodeType;
}

public Expression getSkipEmptyExpression() {
	return skipEmptyExpression;
}

public void setSkipEmptyExpression(Expression skipEmptyExpression) {
	this.skipEmptyExpression = skipEmptyExpression;
}

public void setAdminUserIdExpressions(
		Set<Expression> adminUserIdExpressions) {
	this.adminUserIdExpressions = adminUserIdExpressions;
}



public Map<TaskRole, OperatingFunction> getOperatingFunctions() {
	return operatingFunctions;
}

public OperatingFunction getOperatingFunctions(TaskRole taskRole) {
	if(operatingFunctions!=null){
		return operatingFunctions.get(taskRole);
	}else{
		return null;
	}	
}

public void setOperatingFunctions(Map<TaskRole, OperatingFunction> operatingFunctions) {
	this.operatingFunctions = operatingFunctions;
}

public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }
  
  public Expression getDueDateExpression() {
    return dueDateExpression;
  }
  
  public void setDueDateExpression(Expression dueDateExpression) {
    this.dueDateExpression = dueDateExpression;
  }

  public Map<String, List<TaskListener>> getTaskListeners() {
    return taskListeners;
  }

  public void setTaskListeners(Map<String, List<TaskListener>> taskListeners) {
    this.taskListeners = taskListeners;
  }
  
  public List<TaskListener> getTaskListener(String eventName) {
    return taskListeners.get(eventName);
  }
  
  public void addTaskListener(String eventName, TaskListener taskListener) {
    List<TaskListener> taskEventListeners = taskListeners.get(eventName);
    if (taskEventListeners == null) {
      taskEventListeners = new ArrayList<TaskListener>();
      taskListeners.put(eventName, taskEventListeners);
    }
    taskEventListeners.add(taskListener);
  }

public Expression getSignOffTypeExpression() {
	return signOffTypeExpression;
}

public void setSignOffTypeExpression(Expression signOffTypeExpression) {
	this.signOffTypeExpression = signOffTypeExpression;
}

public Expression getStartScriptExpression() {
	return startScriptExpression;
}

public void setStartScriptExpression(Expression startScriptExpression) {
	this.startScriptExpression = startScriptExpression;
}

public Expression getEndScriptExpression() {
	return endScriptExpression;
}

public void setEndScriptExpression(Expression endScriptExpression) {
	this.endScriptExpression = endScriptExpression;
}

public Expression getStartScriptNameExpression() {
	return startScriptNameExpression;
}

public void setStartScriptNameExpression(Expression startScriptNameExpression) {
	this.startScriptNameExpression = startScriptNameExpression;
}

public Expression getEndScriptNameExpression() {
	return endScriptNameExpression;
}

public void setEndScriptNameExpression(Expression endScriptNameExpression) {
	this.endScriptNameExpression = endScriptNameExpression;
}

public Set<Expression> getReaderUserIdExpressions() {
	return readerUserIdExpressions;
}

public Set<Expression> getReaderGroupIdExpressions() {
	return readerGroupIdExpressions;
}

public void addReaderUserIdExpression(Expression userId) {
    readerUserIdExpressions.add(userId);
}

public void addReaderGroupIdExpression(Expression groupId) {
    readerGroupIdExpressions.add(groupId);
}
public Expression getDynamicOrganizer() {
	return dynamicOrganizer;
}

public void setDynamicOrganizer(Expression dynamicOrganizer) {
	this.dynamicOrganizer = dynamicOrganizer;
}

public Expression getDynamicReader() {
	return dynamicReader;
}

public void setDynamicReader(Expression dynamicReader) {
	this.dynamicReader = dynamicReader;
}
public Expression getFormName() {
	return formName;
}
public void setFormName(Expression formName) {
	this.formName = formName;
}

public Expression getDynamicOrganizerType() {
	return dynamicOrganizerType;
}
public void setDynamicOrganizerType(Expression dynamicOrganizerType) {
	this.dynamicOrganizerType = dynamicOrganizerType;
}
public Expression getDynamicReaderType() {
	return dynamicReaderType;
}
public void setDynamicReaderType(Expression dynamicReaderType) {
	this.dynamicReaderType = dynamicReaderType;
}

public String getStartFormName() {
	return startFormName;
}

public void setStartFormName(String startFormName) {
	this.startFormName = startFormName;
}

public String getStartNodeDynamicOrganizer() {
	return StartNodeDynamicOrganizer;
}

public void setStartNodeDynamicOrganizer(String startNodeDynamicOrganizer) {
	StartNodeDynamicOrganizer = startNodeDynamicOrganizer;
}

public String getStartNodedynamicOrganizerType() {
	return StartNodedynamicOrganizerType;
}

public void setStartNodedynamicOrganizerType(
		String startNodedynamicOrganizerType) {
	StartNodedynamicOrganizerType = startNodedynamicOrganizerType;
}

public String getEndEvenStartScriptExpression() {
	return endEvenStartScriptExpression;
}

public void setEndEvenStartScriptExpression(
		String endEvenStartScriptExpression) {
	this.endEvenStartScriptExpression = endEvenStartScriptExpression;
}

public String getEndEventStartScriptNameExpression() {
	return endEventStartScriptNameExpression;
}

public void setEndEventStartScriptNameExpression(
		String endEventStartScriptNameExpression) {
	this.endEventStartScriptNameExpression = endEventStartScriptNameExpression;
}
public List<Map<String, Object>> getReferenceRelation() {
	return referenceRelation;
}
public void setReferenceRelation(List<Map<String, Object>> referenceRelation) {
	this.referenceRelation = referenceRelation;
}
public Map<String, String> getTimeSettingDetails() {
	return timeSettingDetails;
}
public void setTimeSettingDetails(
		Map<String, String> timeSettingDetails) {
	this.timeSettingDetails = timeSettingDetails;
}

public Expression getIsForStartNode() {
	return isForStartNode;
}
public void setIsForStartNode(Expression isForStartNode) {
	this.isForStartNode = isForStartNode;
}
public void addReaderGroup(String groupId,String executionId) {
	String groupType = getIdentityGroupTypeFromNameExpression(groupId);
	addHistoricIdentityLinkEntity(null, getIdentityNameFromNameExpression(groupId), IdentityLinkType.READER, 0,groupType,executionId);
  }
  
  public void addReaderGroups(Collection<String> readerGroups,String executionId) {
    for (String readerGroup : readerGroups) {
    	addReaderGroup(readerGroup,executionId);
    }
  }
  
  private String getIdentityNameFromNameExpression(String identityName){
		String[] nameOrder = identityName.split(TaskDefinition.CANDIDATE_ORDER_SEPERATOR);
		if(nameOrder.length>1){
			return nameOrder[0];
		}else{
			return identityName;
		}
	}
  
	private String getIdentityGroupTypeFromNameExpression(String identityName) {
		String[] nameOrder = identityName
				.split(TaskDefinition.CANDIDATE_ORDER_SEPERATOR);
		if (nameOrder.length > 2) {
			return nameOrder[nameOrder.length-1];
		} else {
			return identityName;
		}
	}
  
	public void addReaderUser(String userId,String executionId) {
		addHistoricIdentityLinkEntity(getIdentityNameFromNameExpression(userId), null,
				IdentityLinkType.READER, 0, null,executionId);
	}
		  
	public void addReaderUsers(Collection<String> readerUsers,String executionId) {
		for (String readerUser : readerUsers) {
			addReaderUser(readerUser,executionId);
		}
	}
	
	
  
  public void addHistoricIdentityLinkEntity(String userId, String groupId, String type, int order , String groupType,String executionId) {
	  	HistoricIdentityLinkEntity identityLinkEntity = new HistoricIdentityLinkEntity();
	    identityLinkEntity.setExecutionId(executionId);
	    identityLinkEntity.setUserId(userId);
	    identityLinkEntity.setGroupId(groupId);
	    identityLinkEntity.setType(type);
	    identityLinkEntity.setOrder(order);
	    identityLinkEntity.setGroupType(groupType);
	    identityLinkEntity.setIsEndEvent("true");
	    HistoricIdentityLinkEntity.createAndInsert(identityLinkEntity);
}

	public void addAdminGroupIdExpression(Expression groupId) {
		adminGroupIdExpression.add(groupId);
	}

}
