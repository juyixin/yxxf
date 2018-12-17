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
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import com.eazytec.bpm.common.util.I18nUtil;
import com.eazytec.bpm.engine.TaskRole;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.Department;
import com.eazytec.model.Role;

/**
 * @author Joram Barrez
 * @author Tom Baeyens
 * @author Falko Menge
 * @author madan
 */
public class TaskQueryImpl extends AbstractQuery<TaskQuery, Task> implements TaskQuery {
  
  private static final long serialVersionUID = 1L;
  protected String taskId;
  protected String name;
  protected String nameLike;
  protected String description;
  protected String descriptionLike;
  protected Integer priority;
  protected Integer minPriority;
  protected Integer maxPriority;
  protected String assignee;
  protected String involvedUser;
  protected String owner;
  protected boolean unassigned = false;
  protected boolean noDelegationState = false;
  protected DelegationState delegationState;
  protected String candidateUser;  
  protected String classificationId;
  protected int dataLimit;
  public String getClassificationId() {
	return classificationId;
}

public void setClassificationId(String classificationId) {
	this.classificationId = classificationId;
}

protected String candidateGroup; 
  protected String candidateRole;
  protected String candidateDepartment;
  protected String coordinatorUser; 
  protected String creator;
  protected String processedUser;
  protected String coordinatorGroup; 
  protected String coordinatorRole;
  protected String coordinatorDepartment;
  protected String signOffType;
  protected List<String> signOffTypes;
  private List<String> candidateGroups;
  private List<String> candidateRoles;
  private List<String> candidateDepartments;
  private List<String> coordinatorGroups;
  private List<String> coordinatorRoles;
  private List<String> coordinatorDepartments;
  protected String readerUser;
  protected String readerGroup;
  protected String readerRole;
  protected String readerDepartment;
  private List<String> readerGroups;
  private List<String> readerRoles;
  private List<String> readerDepartments;
  protected String processInstanceId;
  protected String executionId;
  protected Date createTime;
  protected Date createTimeBefore;
  protected Date createTimeAfter;
  protected String key;
  protected String keyLike;
  protected String processDefinitionKey;
  protected String processDefinitionId;
  protected String processDefinitionName;
  protected String processInstanceBusinessKey;
  protected List<TaskQueryVariableValue> variables = new ArrayList<TaskQueryVariableValue>();
  protected Date dueDate;
  protected Date dueBefore;
  protected Date dueAfter;
  protected SuspensionState suspensionState;
  protected boolean excludeSubtasks = false;
  protected String customQuery;
  protected String identityType;

  

public TaskQueryImpl() {
  }
  
  public TaskQueryImpl(CommandContext commandContext) {
    super(commandContext);
  }
  
  public TaskQueryImpl(CommandExecutor commandExecutor) {
    super(commandExecutor);
  }
  
  public TaskQueryImpl taskId(String taskId) {
    if (taskId == null) {
      throw new ActivitiException("Task id is null");
    }
    this.taskId = taskId;
    return this;
  }
  
  public TaskQueryImpl taskName(String name) {
    this.name = name;
    return this;
  }
  
  public TaskQueryImpl taskNameLike(String nameLike) {
    if (nameLike == null) {
      throw new ActivitiException("Task namelike is null");
    }
    this.nameLike = nameLike;
    return this;
  }
  
  public TaskQueryImpl taskDescription(String description) {
    if (description == null) {
      throw new ActivitiException("Description is null");
    }
    this.description = description;
    return this;
  }
  
  public TaskQuery taskDescriptionLike(String descriptionLike) {
    if (descriptionLike == null) {
      throw new ActivitiException("Task descriptionlike is null");
    }
    this.descriptionLike = descriptionLike;
    return this;
  }
  
  public TaskQuery taskPriority(Integer priority) {
    if (priority == null) {
      throw new ActivitiException("Priority is null");
    }
    this.priority = priority;
    return this;
  }

  public TaskQuery taskMinPriority(Integer minPriority) {
    if (minPriority == null) {
      throw new ActivitiException("Min Priority is null");
    }
    this.minPriority = minPriority;
    return this;
  }

  public TaskQuery taskMaxPriority(Integer maxPriority) {
    if (maxPriority == null) {
      throw new ActivitiException("Max Priority is null");
    }
    this.maxPriority = maxPriority;
    return this;
  }

  public TaskQueryImpl taskAssignee(String assignee) {
    if (assignee == null) {
      throw new ActivitiException("Assignee is null");
    }
    this.assignee = assignee;
    return this;
  }
  
  public TaskQueryImpl taskOwner(String owner) {
    if (owner == null) {
      throw new ActivitiException("Owner is null");
    }
    this.owner = owner;
    return this;
  }
  
  /** @see {@link #taskUnassigned} */
  @Deprecated
  public TaskQuery taskUnnassigned() {
    return taskUnassigned();
  }

  public TaskQuery taskUnassigned() {
    this.unassigned = true;
    return this;
  }

  public TaskQuery taskDelegationState(DelegationState delegationState) {
    if (delegationState == null) {
      this.noDelegationState = true;
    } else {
      this.delegationState = delegationState;
    }
    return this;
  }
  
  /** Only select tasks with the logger in user as admin */
  
  public TaskQuery processAdminUser(String candidateUser){
	  setIdentityType(TaskRole.WORKFLOW_ADMINISTRATOR.getName());
	    if (candidateUser == null) {
	      throw new ActivitiException("Candidate user is null");
	    }
	    if (customQuery == null && candidateGroup != null) {
	      throw new ActivitiException("Invalid query usage: cannot set both candidateUser and candidateGroup");
	    }
	    if (customQuery == null && candidateGroups != null) {
	      throw new ActivitiException("Invalid query usage: cannot set both candidateUser and candidateGroupIn");
	    }
	    this.candidateUser = candidateUser;
	    return this;
  }
  
  public TaskQuery processClassification(String classificationId){
	  this.classificationId = classificationId;
	    return this;
  }
  
  public TaskQuery setDataLimit(int dataLimit){
	  this.dataLimit = dataLimit;
	  return this;
  }
    
  public TaskQueryImpl taskCandidateUser(String candidateUser) {
	  setIdentityType(TaskRole.ORGANIZER.getName());
    if (candidateUser == null) {
      throw new ActivitiException("Candidate user is null");
    }
    if (customQuery == null && candidateGroup != null) {
      throw new ActivitiException("Invalid query usage: cannot set both candidateUser and candidateGroup");
    }
    if (customQuery == null && candidateGroups != null) {
      throw new ActivitiException("Invalid query usage: cannot set both candidateUser and candidateGroupIn");
    }
    this.candidateUser = candidateUser;
    return this;
  }
  
  public TaskQueryImpl taskCoordinatorUser(String coordinatorUser) {
	  setIdentityType(TaskRole.COORDINATOR.getName());
    if (coordinatorUser == null) {
      throw new ActivitiException("Coordinator user is null");
    }
    if (customQuery == null && coordinatorGroup != null) {
      throw new ActivitiException("Invalid query usage: cannot set both coordinatorUser and coordinatorGroup");
    }
    if (customQuery == null && coordinatorGroups != null) {
      throw new ActivitiException("Invalid query usage: cannot set both coordinatorUser and coordinatorGroupIn");
    }
    this.coordinatorUser = coordinatorUser;
    return this;
  }
  
  public TaskQueryImpl taskCreatorUser(String creator) {
	  setIdentityType(TaskRole.CREATOR.getName());
    if (creator == null) {
      throw new ActivitiException("creator user is null");
    }
    
    this.creator = creator;
    return this;
  }
  
  public TaskQueryImpl taskProcessedUser(String processedUser) {
	  setIdentityType(TaskRole.PROCESSED_USER.getName());
    if (processedUser == null) {
      throw new ActivitiException("processedUser user is null");
    }
    
    this.processedUser = processedUser;
    return this;
  }
  
  public TaskQueryImpl taskReaderUser(String readerUser) {
	  setIdentityType(TaskRole.READER.getName());
	    if (readerUser == null) {
	      throw new ActivitiException("Reader user is null");
	    }
	    if (customQuery == null && readerGroup != null) {
	      throw new ActivitiException("Invalid query usage: cannot set both readerUser and readerGroup");
	    }
	    if (customQuery == null && readerGroups != null) {
	      throw new ActivitiException("Invalid query usage: cannot set both readerUser and readerGroupIn");
	    }
	    this.readerUser = readerUser;
	    return this;
	  }
  
  public TaskQueryImpl taskInvolvedUser(String involvedUser) {
    if (involvedUser == null) {
      throw new ActivitiException("Involved user is null");
    }
    this.involvedUser = involvedUser;
    return this;
  }
  
  public TaskQueryImpl taskCandidateGroup(String candidateGroup) {
	  setIdentityType(TaskRole.ORGANIZER.getName());
	   
    if (candidateGroup == null) {
      throw new ActivitiException("Candidate group is null");
    }
    if (customQuery == null && candidateUser != null) {
      throw new ActivitiException("Invalid query usage: cannot set both candidateGroup and candidateUser");
    }
    if (customQuery == null && candidateGroups != null) {
      throw new ActivitiException("Invalid query usage: cannot set both candidateGroup and candidateGroupIn");
    }
    this.candidateGroup = candidateGroup;
    return this;
  }
  
  public TaskQueryImpl taskCoordinatorGroup(String coordinatorGroup) {
	  setIdentityType(TaskRole.COORDINATOR.getName());
	   
    if (coordinatorGroup == null) {
      throw new ActivitiException("coordinator group is null");
    }
    if (customQuery == null && coordinatorUser != null) {
      throw new ActivitiException("Invalid query usage: cannot set both coordinatorGroup and coordinatorUser");
    }
    if (customQuery == null && coordinatorGroups != null) {
      throw new ActivitiException("Invalid query usage: cannot set both coordinatorGroup and coordinatorGroupIn");
    }
    this.coordinatorGroup = coordinatorGroup;
    return this;
  }
  
  public TaskQueryImpl taskReaderGroup(String readerGroup) {
	  setIdentityType(TaskRole.READER.getName());
	   
	    if (readerGroup == null) {
	      throw new ActivitiException("Reader group is null");
	    }
	    if (customQuery == null && readerUser != null) {
	      throw new ActivitiException("Invalid query usage: cannot set both readerGroup and readerUser");
	    }
	    if (customQuery == null && readerGroups != null) {
	      throw new ActivitiException("Invalid query usage: cannot set both readerGroup and readerGroupIn");
	    }
	    this.readerGroup = readerGroup;
	    return this;
	  }
  public TaskQuery taskCandidateGroupIn(List<String> candidateGroups) {
	  setIdentityType(TaskRole.ORGANIZER.getName());
    if(candidateGroups == null) {
      throw new ActivitiException("Candidate group list is null");
    }
    if(candidateGroups.size()== 0) {
      throw new ActivitiException("Candidate group list is empty");
    }
    
    if (customQuery == null && candidateUser != null) {
      throw new ActivitiException("Invalid query usage: cannot set both candidateGroupIn and candidateUser");
    }
    if (customQuery == null && candidateGroup != null) {
      throw new ActivitiException("Invalid query usage: cannot set both candidateGroupIn and candidateGroup");
    }
    
    this.candidateGroups = candidateGroups;
    return this;
  }
  
  public TaskQuery taskCoordinatorGroupIn(List<String> coordinatorGroups) {
	  setIdentityType(TaskRole.COORDINATOR.getName());
    if(coordinatorGroups == null) {
      throw new ActivitiException("coordinator group list is null");
    }
    if(coordinatorGroups.size()== 0) {
      throw new ActivitiException("coordinator group list is empty");
    }
    
    if (customQuery == null && coordinatorUser != null) {
      throw new ActivitiException("Invalid query usage: cannot set both coordinatorGroupIn and coordinatorUser");
    }
    if (customQuery == null && coordinatorGroup != null) {
      throw new ActivitiException("Invalid query usage: cannot set both coordinatorGroupIn and coordinatorGroup");
    }
    
    this.coordinatorGroups = coordinatorGroups;
    return this;
  }
  
  public TaskQuery taskReaderGroupIn(List<String> readerGroups) {
	  setIdentityType(TaskRole.READER.getName());
    if(readerGroups == null) {
      throw new ActivitiException("Reader group list is null");
    }
    if(readerGroups.size()== 0) {
      throw new ActivitiException("Reader group list is empty");
    }
    
    if (customQuery == null && readerUser != null) {
      throw new ActivitiException("Invalid query usage: cannot set both readerGroupIn and readerUser");
    }
    if (customQuery == null && readerGroup != null) {
      throw new ActivitiException("Invalid query usage: cannot set both readerGroupIn and readerGroup");
    }
    
    this.readerGroups = readerGroups;
    return this;
  }
  
  public TaskQuery taskCandidateRoleIn(List<String> candidateRoles) {
	  setIdentityType(TaskRole.ORGANIZER.getName());
	  
	    if(candidateRoles == null) {
	      throw new ActivitiException("Candidate role list is null");
	    }
	    if(candidateRoles.size()== 0) {
	      throw new ActivitiException("Candidate role list is empty");
	    }
	    
	    if (customQuery == null && candidateUser != null) {
	      throw new ActivitiException("Invalid query usage: cannot set both candidateRoleIn and candidateUser");
	    }
	    if (customQuery == null && candidateRole != null) {
	      throw new ActivitiException("Invalid query usage: cannot set both candidateRoleIn and candidateRole");
	    }
	    this.candidateRoles = candidateRoles;
	    return this;
	  }
  
  public TaskQuery taskCoordinatorRoleIn(List<String> coordinatorRoles) {
	  setIdentityType(TaskRole.COORDINATOR.getName());
	  
	    if(coordinatorRoles == null) {
	      throw new ActivitiException("coordinator role list is null");
	    }
	    if(coordinatorRoles.size()== 0) {
	      throw new ActivitiException("coordinator role list is empty");
	    }
	    
	    if (customQuery == null && coordinatorUser != null) {
	      throw new ActivitiException("Invalid query usage: cannot set both coordinatorRoleIn and coordinatorUser");
	    }
	    if (customQuery == null && coordinatorRole != null) {
	      throw new ActivitiException("Invalid query usage: cannot set both coordinatorRoleIn and coordinatorRole");
	    }
	    this.coordinatorRoles = coordinatorRoles;
	    return this;
	  }
  
  public TaskQuery taskReaderRoleIn(List<String> readerRoles) {
	  setIdentityType(TaskRole.READER.getName());
	  
	    if(readerRoles == null) {
	      throw new ActivitiException("Reader role list is null");
	    }
	    if(readerRoles.size()== 0) {
	      throw new ActivitiException("Reader role list is empty");
	    }
	    
	    if (customQuery == null && readerUser != null) {
	      throw new ActivitiException("Invalid query usage: cannot set both readerRoleIn and readerUser");
	    }
	    if (customQuery == null && readerRole != null) {
	      throw new ActivitiException("Invalid query usage: cannot set both readerRoleIn and readerRole");
	    }
	    this.readerRoles = readerRoles;
	    return this;
	  }
  
  public TaskQuery taskCandidateDepartmentIn(String candidateDepartment) {
	  setIdentityType(TaskRole.ORGANIZER.getName());
	  
	    if (candidateDepartment == null) {
	      throw new ActivitiException("User Department is null");
	    }
	    this.candidateDepartment = candidateDepartment;
	    return this;
	  }
  
  public TaskQuery taskCoordinatorDepartmentIn(String coordinatorDepartment) {
	  setIdentityType(TaskRole.COORDINATOR.getName());
	  
	    if (coordinatorDepartment == null) {
	      throw new ActivitiException("User Department is null");
	    }
	    this.coordinatorDepartment = coordinatorDepartment;
	    return this;
	  }
  
  public TaskQuery taskReaderDepartmentIn(String readerDepartment) {
	  setIdentityType(TaskRole.READER.getName());
	  
	    if (readerDepartment == null) {
	      throw new ActivitiException("User Department is null");
	    }
	    this.readerDepartment = readerDepartment;
	    return this;
	  }
  
  public TaskQuery signOffTypeIn(List<String> signOffTypes) {
    if(signOffTypes == null) {
      throw new BpmException("Sign Off Type list is null");
    }
    if(signOffTypes.size()== 0) {
      throw new BpmException("Sign Off Type list is empty");
    }
    
    if (signOffType != null) {
      throw new BpmException("Invalid query usage: cannot set both signOffTypeIn and signOffType");
    }   
    
    this.signOffTypes = signOffTypes;
    return this;
  }
  
  public TaskQuery signOffType(String signOffType) {
    if(signOffType == null) {
      throw new BpmException("Sign Off Type is null");
    }	    
    this.signOffType = signOffType;
    return this;
  }
  
  public TaskQueryImpl processInstanceId(String processInstanceId) {
    this.processInstanceId = processInstanceId;
    return this;
  }
  
  public TaskQueryImpl processInstanceBusinessKey(String processInstanceBusinessKey) {
    this.processInstanceBusinessKey = processInstanceBusinessKey;
    return this;
  }
  
  public TaskQueryImpl executionId(String executionId) {
    this.executionId = executionId;
    return this;
  }
  
  public TaskQueryImpl taskCreatedOn(Date createTime) {
    this.createTime = createTime;
    return this;
  }
  
  public TaskQuery taskCreatedBefore(Date before) {
    this.createTimeBefore = before;
    return this;
  }
  
  public TaskQuery taskCreatedAfter(Date after) {
    this.createTimeAfter = after;
    return this;
  }
  
  public TaskQuery taskDefinitionKey(String key) {
    this.key = key;
    return this;
  }
  
  public TaskQuery taskDefinitionKeyLike(String keyLike) {
    this.keyLike = keyLike;
    return this;
  }
  
  public TaskQuery taskVariableValueEquals(String variableName, Object variableValue) {
    variables.add(new TaskQueryVariableValue(variableName, variableValue, QueryOperator.EQUALS, true));
    return this;
  }
  
  public TaskQuery taskVariableValueEquals(Object variableValue) {
    variables.add(new TaskQueryVariableValue(null, variableValue, QueryOperator.EQUALS, true));
    return this;
  }
  
  public TaskQuery taskVariableValueEqualsIgnoreCase(String name, String value) {
    if(value == null) {
      throw new ActivitiException("value is null");
    }
    variables.add(new TaskQueryVariableValue(name, value.toLowerCase(), QueryOperator.EQUALS_IGNORE_CASE, true));
    return this;
  }
  
  public TaskQuery taskVariableValueNotEqualsIgnoreCase(String name, String value) {
    if(value == null) {
      throw new ActivitiException("value is null");
    }
    variables.add(new TaskQueryVariableValue(name, value.toLowerCase(), QueryOperator.NOT_EQUALS_IGNORE_CASE, true));
    return this;
  }

  public TaskQuery taskVariableValueNotEquals(String variableName, Object variableValue) {
    variables.add(new TaskQueryVariableValue(variableName, variableValue, QueryOperator.NOT_EQUALS, true));
    return this;
  }

  public TaskQuery processVariableValueEquals(String variableName, Object variableValue) {
    variables.add(new TaskQueryVariableValue(variableName, variableValue, QueryOperator.EQUALS, false));
    return this;
  }

  public TaskQuery processVariableValueNotEquals(String variableName, Object variableValue) {
    variables.add(new TaskQueryVariableValue(variableName, variableValue, QueryOperator.NOT_EQUALS, false));
    return this;
  }
  
  public TaskQuery processVariableValueEquals(Object variableValue) {
    variables.add(new TaskQueryVariableValue(null, variableValue, QueryOperator.EQUALS, false));
    return this;
  }
  
  public TaskQuery processVariableValueEqualsIgnoreCase(String name, String value) {
    if(value == null) {
      throw new ActivitiException("value is null");
    }
    variables.add(new TaskQueryVariableValue(name, value.toLowerCase(), QueryOperator.EQUALS_IGNORE_CASE, false));
    return this;
  }
  
  public TaskQuery processVariableValueNotEqualsIgnoreCase(String name, String value) {
    if(value == null) {
      throw new ActivitiException("value is null");
    }
    variables.add(new TaskQueryVariableValue(name, value.toLowerCase(), QueryOperator.NOT_EQUALS_IGNORE_CASE, false));
    return this;
  }

  public TaskQuery processDefinitionKey(String processDefinitionKey) {
    this.processDefinitionKey = processDefinitionKey;
    return this;
  }

  public TaskQuery processDefinitionId(String processDefinitionId) {
    this.processDefinitionId = processDefinitionId;
    return this;
  }
  
  public TaskQuery processDefinitionName(String processDefinitionName) {
    this.processDefinitionName = processDefinitionName;
    return this;
  }
  
  public TaskQuery dueDate(Date dueDate) {
    this.dueDate = dueDate;
    return this;
  }
  
  public TaskQuery dueBefore(Date dueBefore) {
    this.dueBefore = dueBefore;
    return this;
  }
  
  public TaskQuery dueAfter(Date dueAfter) {
    this.dueAfter = dueAfter;
    return this;
  }

  public TaskQuery excludeSubtasks() {
    this.excludeSubtasks = true;
    return this;
  }
  
  public TaskQuery suspended() {
    this.suspensionState = SuspensionState.SUSPENDED;
    return this;
  }

  public TaskQuery active() {
    this.suspensionState = SuspensionState.ACTIVE;
    return this;
  }
  
  /**
   * {@inheritDoc customQuery}
   */
  public TaskQuery customQuery(String customQuery) {
  	if(customQuery==null){
		  throw new BpmException(I18nUtil.getMessageProperty("error.query.notFound"));    	  		
  	}
    this.customQuery = customQuery;
    return this;
  }

  public List<String> getCandidateGroups() {
	  setIdentityType(TaskRole.ORGANIZER.getName());
    if (candidateGroup!=null) {
      return Collections.singletonList(candidateGroup);
    } else if (candidateUser != null) {
      return getGroupsForCandidateUser(candidateUser);
    } else if(candidateGroups != null) {
      return candidateGroups;
    }
    return null;
  }
  
  public List<String> getCoordinatorGroups() {
	  setIdentityType(TaskRole.COORDINATOR.getName());
    if (coordinatorGroup!=null) {
      return Collections.singletonList(coordinatorGroup);
    } else if (coordinatorUser != null) {
      return getGroupsForCoordinatorUser(coordinatorUser);
    } else if(coordinatorGroups != null) {
      return coordinatorGroups;
    }
    return null;
  }
  
  public List<String> getReaderGroups() {
		 setIdentityType(TaskRole.READER.getName());
		if (readerGroup!=null) {
		      return Collections.singletonList(readerGroup);
		    } else if (readerUser != null) {
		      return getGroupsForReaderUser(readerUser);
		    } else if(readerGroups != null) {
		      return readerGroups;
		    }
		    return null;
	}
  
  protected List<String> getGroupsForCandidateUser(String candidateUser) {
	  setIdentityType(TaskRole.ORGANIZER.getName());
    // TODO: Discuss about removing this feature? Or document it properly and maybe recommend to not use it
    // and explain alternatives
    List<Group> groups = Context
      .getCommandContext()
      .getGroupEntityManager()
      .findGroupsByUser(candidateUser);
    List<String> groupIds = new ArrayList<String>();
    for (Group group : groups) {
      groupIds.add(group.getId());
    }
    return groupIds;
  } 
  
  protected List<String> getGroupsForCoordinatorUser(String coordinatorUser) {
	  setIdentityType(TaskRole.COORDINATOR.getName());
    // TODO: Discuss about removing this feature? Or document it properly and maybe recommend to not use it
    // and explain alternatives
    List<Group> groups = Context
      .getCommandContext()
      .getGroupEntityManager()
      .findGroupsByUser(coordinatorUser);
    List<String> groupIds = new ArrayList<String>();
    for (Group group : groups) {
      groupIds.add(group.getId());
    }
    return groupIds;
  }
  protected List<String> getGroupsForReaderUser(String readerUser) {
	  setIdentityType(TaskRole.READER.getName());
	    // TODO: Discuss about removing this feature? Or document it properly and maybe recommend to not use it
	    // and explain alternatives
	    List<Group> groups = Context
	      .getCommandContext()
	      .getGroupEntityManager()
	      .findGroupsByUser(readerUser);
	    List<String> groupIds = new ArrayList<String>();
	    for (Group group : groups) {
	      groupIds.add(group.getId());
	    }
	    return groupIds;
	  } 
  
  public List<String> getCandidateRoles() {
	  setIdentityType(TaskRole.ORGANIZER.getName());

	    if (candidateRole!=null) {
	      return Collections.singletonList(candidateRole);
	    } else if (candidateUser != null) {
	      return getRolesForCandidateUser(candidateUser);
	    } else if(candidateRoles != null) {
	      return candidateRoles;
	    }
	    return null;
	  }
  
  public List<String> getCoordinatorRoles() {
	  setIdentityType(TaskRole.COORDINATOR.getName());

	    if (coordinatorRole!=null) {
	      return Collections.singletonList(coordinatorRole);
	    } else if (coordinatorUser != null) {
	      return getRolesForCoordinatorUser(coordinatorUser);
	    } else if(coordinatorRoles != null) {
	      return coordinatorRoles;
	    }
	    return null;
	  }
	  
  public List<String> getReaderRoles() {
		 setIdentityType(TaskRole.READER.getName());
		if (readerRole!=null) {
		      return Collections.singletonList(readerRole);
		    } else if (readerUser != null) {
		      return getRolesForReaderUser(readerUser);
		    } else if(readerRoles != null) {
		      return readerRoles;
		    }
		    return null;
	}
  protected List<String> getRolesForCandidateUser(String candidateUser) {
	  setIdentityType(TaskRole.ORGANIZER.getName());
	    // TODO: Discuss about removing this feature? Or document it properly and maybe recommend to not use it
	    // and explain alternatives
	    List<Role> roles = Context
	      .getCommandContext().getUserEntityManager().findRolesByUser(candidateUser);
	    List<String> roleIds = new ArrayList<String>();
	    for (Role role : roles) {
	      roleIds.add(role.getId());
	    }
	    return roleIds;
	  } 
  
  protected List<String> getRolesForCoordinatorUser(String coordinatorUser) {
	  setIdentityType(TaskRole.COORDINATOR.getName());
	    // TODO: Discuss about removing this feature? Or document it properly and maybe recommend to not use it
	    // and explain alternatives
	    List<Role> roles = Context
	      .getCommandContext().getUserEntityManager().findRolesByUser(coordinatorUser);
	    List<String> roleIds = new ArrayList<String>();
	    for (Role role : roles) {
	      roleIds.add(role.getId());
	    }
	    return roleIds;
	  } 
  
  protected List<String> getRolesForReaderUser(String readerUser) {
	  setIdentityType(TaskRole.READER.getName());
	    // TODO: Discuss about removing this feature? Or document it properly and maybe recommend to not use it
	    // and explain alternatives
	    List<Role> roles = Context
	      .getCommandContext().getUserEntityManager().findRolesByUser(readerUser);
	    List<String> roleIds = new ArrayList<String>();
	    for (Role role : roles) {
	      roleIds.add(role.getId());
	    }
	    return roleIds;
	  } 
  
  public List<String> getCandidatetDepartments() {
	  
	  setIdentityType(TaskRole.ORGANIZER.getName());
	    if (candidateDepartment !=null) {
	      return Collections.singletonList(candidateDepartment);
	    } else if (candidateDepartment != null) {
	      return getRolesForCandidateUser(candidateDepartment);
	    } else if(candidateDepartments != null) {
	      return candidateDepartments;
	    }
	    return null;
	  }
  
 public List<String> getCoordinatorDepartments() {
	  
	  setIdentityType(TaskRole.COORDINATOR.getName());
	    if (coordinatorDepartment !=null) {
	      return Collections.singletonList(coordinatorDepartment);
	    } else if (coordinatorUser != null) {
	      return getRolesForCoordinatorUser(coordinatorUser);
	    } else if(coordinatorDepartments != null) {
	      return coordinatorDepartments;
	    }
	    return null;
	  }
 	  
  public List<String> getReaderDepartments() {
		 setIdentityType(TaskRole.READER.getName());

	    if (readerDepartment !=null) {
	      return Collections.singletonList(readerDepartment);
	    } else if (readerDepartment != null) {
	      return getRolesForReaderUser(readerDepartment);
	    } else if(readerDepartments != null) {
	      return readerDepartments;
	    }
	    return null;
	}
  
  protected List<String> getDepartmentsForCandidateUser(String candidateUser) {
	  setIdentityType(TaskRole.ORGANIZER.getName());

	    // TODO: Discuss about removing this feature? Or document it properly and maybe recommend to not use it
	    // and explain alternatives
	    List<Department> departments = Context
	      .getCommandContext().getUserEntityManager().findDepartmentsByUser(candidateUser); 
	    List<String> departmentIds = new ArrayList<String>();
	    for (Department department : departments) {
	      departmentIds.add(department.getId());
	    }
	    return departmentIds;
	  } 
  
  protected List<String> getDepartmentsForCoordinatorUser(String coordinatorUser) {
	  setIdentityType(TaskRole.COORDINATOR.getName());

	    // TODO: Discuss about removing this feature? Or document it properly and maybe recommend to not use it
	    // and explain alternatives
	    List<Department> departments = Context
	      .getCommandContext().getUserEntityManager().findDepartmentsByUser(coordinatorUser); 
	    List<String> departmentIds = new ArrayList<String>();
	    for (Department department : departments) {
	      departmentIds.add(department.getId());
	    }
	    return departmentIds;
	  } 
  
  protected List<String> getDepartmentsForReaderUser(String readerUser) {
	  setIdentityType(TaskRole.READER.getName());

	    // TODO: Discuss about removing this feature? Or document it properly and maybe recommend to not use it
	    // and explain alternatives
	    List<Department> departments = Context
	      .getCommandContext().getUserEntityManager().findDepartmentsByUser(readerUser); 
	    List<String> departmentIds = new ArrayList<String>();
	    for (Department department : departments) {
	      departmentIds.add(department.getId());
	    }
	    return departmentIds;
	  } 

protected void ensureVariablesInitialized() {    
    VariableTypes types = Context.getProcessEngineConfiguration().getVariableTypes();
    for(QueryVariableValue var : variables) {
      var.initialize(types);
    }
  }

  //ordering ////////////////////////////////////////////////////////////////
  
  public TaskQuery orderByTaskId() {
    return orderBy(TaskQueryProperty.TASK_ID);
  }
  
  public TaskQuery orderByTaskName() {
    return orderBy(TaskQueryProperty.NAME);
  }
  
  public TaskQuery orderByTaskDescription() {
    return orderBy(TaskQueryProperty.DESCRIPTION);
  }
  
  public TaskQuery orderByTaskPriority() {
    return orderBy(TaskQueryProperty.PRIORITY);
  }
  
  public TaskQuery orderByProcessInstanceId() {
    return orderBy(TaskQueryProperty.PROCESS_INSTANCE_ID);
  }
  
  public TaskQuery orderByExecutionId() {
    return orderBy(TaskQueryProperty.EXECUTION_ID);
  }
  
  public TaskQuery orderByTaskAssignee() {
    return orderBy(TaskQueryProperty.ASSIGNEE);
  }
  
  public TaskQuery orderByTaskCreateTime() {
    return orderBy(TaskQueryProperty.CREATE_TIME);
  }
  
  public TaskQuery orderByDueDate() {
    return orderBy(TaskQueryProperty.DUE_DATE);
  }
  
  //results ////////////////////////////////////////////////////////////////
  
  public List<Task> executeList(CommandContext commandContext, Page page) {
    ensureVariablesInitialized();
    checkQueryOk();
    return commandContext
      .getTaskEntityManager()
      .findTasksByQueryCriteria(this);
  }
  
  public long executeCount(CommandContext commandContext) {
    ensureVariablesInitialized();
    checkQueryOk();
    return commandContext
      .getTaskEntityManager()
      .findTaskCountByQueryCriteria(this);
  }
  
  //getters ////////////////////////////////////////////////////////////////

  public String getName() {
    return name;
  }
  public String getNameLike() {
    return nameLike;
  }
  public String getAssignee() {
    return assignee;
  }
  public boolean getUnassigned() {
    return unassigned;
  }
  public DelegationState getDelegationState() {
    return delegationState;
  }
  public boolean getNoDelegationState() {
    return noDelegationState;
  }
  public String getDelegationStateString() {
    return (delegationState!=null ? delegationState.toString() : null);
  }
  public String getCandidateUser() {
    return candidateUser;
  }
  public String getCandidateGroup() {
    return candidateGroup;
  }
  public String getProcessInstanceId() {
    return processInstanceId;
  }
  public String getExecutionId() {
    return executionId;
  }
  public String getTaskId() {
    return taskId;
  }
  public String getDescription() {
    return description;
  }
  public String getDescriptionLike() {
    return descriptionLike;
  }
  public Integer getPriority() {
    return priority;
  }
  public Date getCreateTime() {
    return createTime;
  }
  public Date getCreateTimeBefore() {
    return createTimeBefore;
  }
  public Date getCreateTimeAfter() {
    return createTimeAfter;
  }
  public String getKey() {
    return key;
  }
  public String getKeyLike() {
    return keyLike;
  }
  public List<TaskQueryVariableValue> getVariables() {
    return variables;
  }
  public String getProcessDefinitionKey() {
    return processDefinitionKey;
  }
  public String getProcessDefinitionId() {
    return processDefinitionId;
  }
  public String getProcessDefinitionName() {
    return processDefinitionName;
  }
  public String getProcessInstanceBusinessKey() {
    return processInstanceBusinessKey;
  }
  public boolean getExcludeSubtasks() {
    return excludeSubtasks;
  }

public static long getSerialversionuid() {
	return serialVersionUID;
}

public Integer getMinPriority() {
	return minPriority;
}

public Integer getMaxPriority() {
	return maxPriority;
}

public String getInvolvedUser() {
	return involvedUser;
}

public String getOwner() {
	return owner;
}

public String getSignOffType() {
	return signOffType;
}

public List<String> getSignOffTypes() {
	return signOffTypes;
}

public Date getDueDate() {
	return dueDate;
}

public Date getDueBefore() {
	return dueBefore;
}

public Date getDueAfter() {
	return dueAfter;
}

public String getCustomQuery() {
	return customQuery;
}

public String getCandidateRole() {
	return candidateRole;
}

public String getCandidateDepartment() {
	return candidateDepartment;
}

public List<String> getCandidateDepartments() {
	return candidateDepartments;
}

public String getReaderUser() {
	return readerUser;
}

public String getReaderGroup() {
	return readerGroup;
}

public String getReaderRole() {
	return readerRole;
}

public String getReaderDepartment() {
	return readerDepartment;
}

public SuspensionState getSuspensionState() {
	return suspensionState;
}

public String getIdentityType() {
	return identityType;
}

public void setIdentityType(String identityType) {
	this.identityType = identityType;
}

}
