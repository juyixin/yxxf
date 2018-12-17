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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.db.HasRevision;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.delegate.TaskListenerInvocation;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.OperatingFunction;
import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.eazytec.Constants;
import com.eazytec.bpm.engine.TaskRole;
import com.eazytec.bpm.engine.TransactorType;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.AgencySetting;
import com.eazytec.model.User;

/**
 * @author Tom Baeyens
 * @author Joram Barrez
 * @author Falko Menge
 * @author madan
 */ 
public class TaskEntity extends VariableScopeImpl implements Task, DelegateTask, Serializable, PersistentObject, HasRevision {

  protected final Log log = LogFactory.getLog(getClass());	

  public static final String DELETE_REASON_COMPLETED = "completed";
  public static final String DELETE_REASON_DELETED = "deleted";
  public static final String DELETE_REASON_RETURNED = "Task Returned";
  public static final String DELETE_REASON_JUMP = "Task Jump";
  public static final String DELETE_REASON_RECALL = "Task Recall";
  
  private static final long serialVersionUID = 1L;

  protected int revision;

  protected String owner;
  protected String assignee;
  protected DelegationState delegationState;
  
  protected String parentTaskId;
  
  protected String id;
  
  protected String name;
  protected String description;
  protected int priority = Task.PRIORITY_NORMAL;
  protected Date createTime; // The time when the task has been created
  protected Date dueDate;
  protected int suspensionState = SuspensionState.ACTIVE.getStateCode();
  
  protected boolean isIdentityLinksInitialized = false;
  protected List<IdentityLinkEntity> taskIdentityLinkEntities = new ArrayList<IdentityLinkEntity>();
  
  
  protected String executionId;
  protected ExecutionEntity execution;
  
  protected String processInstanceId;
  protected ExecutionEntity processInstance;
  
  protected String processDefinitionId;
  
  protected TaskDefinition taskDefinition;
  protected String taskDefinitionKey;
  
  protected boolean isDeleted;
  
  protected String eventName;
  
  protected int signOffType = TransactorType.SINGLE_PLAYER_SINGLE_SIGNOFF.getStateCode();
 
  protected String startScript;
  
  protected String endScript;
  
  protected String startScriptName;
  
  protected String endScriptName;
  
  protected String dynamicNextOrganizer;
  
  protected String dynamicNextReader;
  
  protected String dynamicOrganizerType;
  
  protected String dydnamicReaderType;
  
  protected String processStartUserId;
  
  protected List<Map<String,String>> referenceRelation = new ArrayList<Map<String,String>>();
  
  protected List<Map<String,String>> formFieldAutomatic = new ArrayList<Map<String,String>>();
  
  protected int maxDays;
  
  protected int warningDays;

  protected String dateType;
  
  protected int urgeTimes;
  
  protected int frequenceInterval;
  
  protected String undealOperation;
  
  protected Date lastUrgedTime; 
  
  protected String notificationType;
  
  protected boolean isForStartNodeTask;
  
  protected boolean isDratf;
  
  protected Date endTime;
  
  protected Date startTime; 
  
  protected Date creatTime;
  
  protected String htmlSourceForSubForm = null;
  
  protected String formkey = null;
  
  protected String assigneeFullName;
  
  protected String isOpinion;
  
/**
	* task type can be normal or extended task. Extended tasks are all Tasks
	* that are not completed with in warning days. Other task with no time
	* setting is normal task
	*/
  protected String taskType; 
  
	public String getAssigneeFullName() {
		return assigneeFullName;
	}

	public void setAssigneeFullName(String assigneeFullName) {
		this.assigneeFullName = assigneeFullName;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getFormkey() {
		return formkey;
	}

	public void setFormkey(String formkey) {
		this.formkey = formkey;
	}
  
	public String getHtmlSourceForSubForm() {
		return htmlSourceForSubForm;
	}

	public void setHtmlSourceForSubForm(String htmlSourceForSubForm) {
		this.htmlSourceForSubForm = htmlSourceForSubForm;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	protected String workflowAdmin;

	public String getWorkflowAdmin() {
		return workflowAdmin;
	}

	public void setWorkflowAdmin(String workflowAdmin) {
		this.workflowAdmin = workflowAdmin;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public boolean getIsForStartNodeTask() {
		return isForStartNodeTask;
	}
	public void setIsForStartNodeTask(boolean isForStartNodeTask) {
		
		this.isForStartNodeTask = isForStartNodeTask;
		CommandContext commandContext = Context.getCommandContext();
	    if (commandContext!=null) {
	      commandContext
	        .getHistoryManager()
	        .recordTaskInfoChange(id, isForStartNodeTask);
	    }
	}
	public boolean getIsDratf() {
		return isDratf;
	}
	public void setIsDratf(boolean isDratf) {
		this.isDratf = isDratf;
	}
  
	public List<Map<String, String>> getFormFieldAutomatic() {
		return formFieldAutomatic;
	}
	public void setFormFieldAutomatic(List<Map<String, String>> formFieldAutomatic) {
		this.formFieldAutomatic = formFieldAutomatic;
	}
	public List<Map<String, String>> getReferenceRelation() {
		return referenceRelation;
	}
	public void setReferenceRelation(List<Map<String, String>> referenceRelation) {
		this.referenceRelation = referenceRelation;
	}
  

	protected Map<TaskRole, OperatingFunction> operatingFunctions;
  
	protected static final Logger LOGGER = Logger.getLogger(TaskEntity.class.getName());

	public static final String TIMESETTING_TASK = "extended";

	public static final String NORMAL_TASK = "normal";

  
  public TaskEntity() {
  }

  public TaskEntity(String taskId) {
    this.id = taskId;
  }
  
  

  
  /** creates and initializes a new persistent task. */
  public static TaskEntity createAndInsert(ActivityExecution execution) {
    TaskEntity task = create();
    ExecutionEntity executionEnt = (ExecutionEntity) execution;
    task.setHtmlSourceForSubForm(executionEnt.getHtmlSourceForSubForm());
    task.setFormkey(executionEnt.getFormkey());
    task.insert((ExecutionEntity) execution);
    task.setProcessInstance(executionEnt.getProcessInstance());
    //Get historic process instance and set it into Task entity
    HistoricProcessInstance instance = Context.getCommandContext()
    	      .getHistoricProcessInstanceEntityManager()
    	      .findHistoricProcessInstance(executionEnt.getProcessInstanceId());
    task.setProcessStartUserId(instance.getStartUserId());
    return task;
  }

  public void insert(ExecutionEntity execution) {
	
    CommandContext commandContext = Context.getCommandContext();
    DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
    dbSqlSession.insert(this);
    
    if(execution != null) {
      execution.addTask(this);
    }
    
    commandContext.getHistoryManager().recordTaskCreated(this, execution);
  }
  
  public void update() {
    // Needed to make history work: the setter will also update the historic task
    setOwner(this.getOwner());
    setAssignee(this.getAssignee());
    setDelegationState(this.getDelegationState());
    setName(this.getName());
    setDescription(this.getDescription());
    setPriority(this.getPriority());
    setCreateTime(this.getCreateTime());
    setDueDate(this.getDueDate());
    setParentTaskId(this.getParentTaskId());
    setStartScript(this.getStartScript());
    setEndScript(this.getEndScript());
    setHtmlSourceForSubForm(this.getHtmlSourceForSubForm());
    setFormkey(this.getFormkey());
    
    CommandContext commandContext = Context.getCommandContext();
    DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
    dbSqlSession.update(this);
  }
  
  /** new task.  Embedded state and create time will be initialized.
   * But this task still will have to be persisted with 
   * TransactionContext
   *     .getCurrent()
   *     .getPersistenceSession()
   *     .insert(task);
   */
  public static TaskEntity create() {
    TaskEntity task = new TaskEntity();
    task.isIdentityLinksInitialized = true;
    task.createTime = ClockUtil.getCurrentTime();
    return task;
  }
  
  
  
  /**
   * <p>Signing off a task is done by the task's assignee. This means the assignee user
   * has declared that he has completed the task. Based on the signoff nature of task,
   * the task may move to next assignee or it may complete and process will move to next task.</p>
   * 
   * @author madan
   */
	public void signOff() throws BpmException {
		
		int signOffType = this.getSignOffType();
		LOGGER.log(Level.INFO, "To sign off task----" + this.getId());
		LOGGER.log(Level.INFO, "Sign off type----" + signOffType);
		
		//setting processed user for all submitted tasks
		IdentityLinkEntity currentIdentity = this.getNextCandidateByOrder();
		HistoricIdentityLinkEntity historicIdentityLinkEntity = new HistoricIdentityLinkEntity(currentIdentity);
		historicIdentityLinkEntity.setType(IdentityLinkType.PROCESSED_USER);
	//	historicIdentityLinkEntity.setIdentityLinkEntityId(null);
		HistoricIdentityLinkEntity.createAndInsert(historicIdentityLinkEntity); //end
		
		if (signOffType == TransactorType.SINGLE_PLAYER_SINGLE_SIGNOFF
				.getStateCode()) {
			// The task will have only one assignee, so here signing off will
			// complete that task
			LOGGER.log(Level.INFO,
					"task is a single player sign off so completing....");
			this.complete();
		} else if (signOffType == TransactorType.MULTI_SEQUENCE_SIGNOFF
				.getStateCode()) {

			// Task should be passed to assignees in defined order
			LOGGER.log(Level.INFO,
					"task is multi sequence sign off, checking if next assignee in order...");
			
			IdentityLinkEntity nextcandidateIdentity = new IdentityLinkEntity();
			if (currentIdentity != null) {
				//HistoricIdentityLinkEntity.createAndInsert(currentIdentity);
				//the below code are commented because of current identity link not inserted in history identity link
				//And also there is no parent id inserted as of now
				/*if(currentIdentity.getParentId() == null){
					//Delete substitute Identity Link
					Context.getCommandContext().getIdentityLinkEntityManager().deleteIdentityLinksByParentId(currentIdentity.getId());
				}else{
					//Delete substitute Identity Link 
					Context.getCommandContext().getIdentityLinkEntityManager().deleteSubstituteIdentityLinksById(currentIdentity.getParentId());
				}
				Context.getCommandContext().getDbSqlSession()
						.delete(currentIdentity);*/
				
				
				nextcandidateIdentity = this
						.getNextCandidateByOrder(currentIdentity);
//				
			} else {
				nextcandidateIdentity = this.getNextCandidateByOrder();
			}
			if (nextcandidateIdentity == null) {
				
				LOGGER.log(Level.INFO,
						"There are no next waiting assignees, so completing....");
				//this.deleteIdentityLinksByTaskId();
				this.complete();
			} else {
				Context.getCommandContext().getIdentityLinkEntityManager().deleteIdentityLinkWithAgency(currentIdentity, true);
				LOGGER.log(Level.INFO, "next assignee in order----"
						+ nextcandidateIdentity.userId);
				LOGGER.log(Level.INFO,
						"finishing the current assignee link----");
				this.finishCurrentTransactorSignOff();
				
				/** if user id --- */
				if (nextcandidateIdentity.getUserId() != null) {
					LOGGER.log(Level.INFO,
							"setting assignee for next identity link---");
					this.setNextSignOffTransactor(nextcandidateIdentity);
				}

			}
		} else if (signOffType == TransactorType.MULTI_PROCESS_SIGNOFF
				.getStateCode()) {

			LOGGER.log(Level.INFO,
					"task is multi process sign off, checking if next assignee in order...");
			IdentityLinkEntity nextcandidateIdentity = new IdentityLinkEntity();
			if (currentIdentity != null) {
				//HistoricIdentityLinkEntity.createAndInsert(currentIdentity);
				//HistoricIdentityLinkEntity.createAndInsert(currentIdentity);
				//the below code are commented because of current identity link not inserted in history identity link
				//And also there is no parent id inserted as of now
				/*if(currentIdentity.getParentId() == null){
					//Delete substitute Identity Link
					Context.getCommandContext().getIdentityLinkEntityManager().deleteIdentityLinksByParentId(currentIdentity.getId());
				}else{
					//Delete substitute Identity Link 
					Context.getCommandContext().getIdentityLinkEntityManager().deleteSubstituteIdentityLinksById(currentIdentity.getParentId());
				}
				Context.getCommandContext().getDbSqlSession()
						.delete(currentIdentity);*/
				
				nextcandidateIdentity = this
						.getNextCandidateByOrder(currentIdentity);
			} else {
				nextcandidateIdentity = this.getNextCandidateByOrder();
			}

			if (nextcandidateIdentity == null) {
				LOGGER.log(Level.INFO,
						"There are no next waiting assignees, so completing....");
				this.complete();
			} else {
				Context.getCommandContext().getIdentityLinkEntityManager().deleteIdentityLinkWithAgency(currentIdentity, true);
				LOGGER.log(Level.INFO, "next assignee----"
						+ nextcandidateIdentity.userId);
				LOGGER.log(Level.INFO,
						"finishing the current assignee link----");
				this.finishCurrentTransactorSignOff();
				LOGGER.log(Level.INFO, "next assignee----"
						+ nextcandidateIdentity.userId);
			}
			// this.finishCurrentTransactorSignOff();
		} else if (signOffType == TransactorType.MULTI_PLAYER_SINGLE_SIGNOFF
				.getStateCode()) {

			LOGGER.log(Level.INFO,
					"task is a multi player single sign off so completing....");
			this.complete();
		}
	}
  
  /**
   * <p>When one transactor signs off the task, the task must be handed over to next one in list</p>
   * @param nextcandidateIdentity the {@link IdentityLinkEntity} to which the task to be linked for next sign off
   * @author madan
   */
  private void setNextSignOffTransactor(IdentityLinkEntity nextcandidateIdentity){
	if(nextcandidateIdentity!=null){
		this.transfer(nextcandidateIdentity.getUserId());
	}else{
		throw new BpmException("No Identity link provided, cannot transfer the task "+this.getId());
	}		  	  
  }
  
  /**
   * <p>Completing a task will delete all the {@link IdentityLinkEntity} associated to the task and move
   * the task to history. Here we just move the current {@link IdentityLinkEntity} who has signed off the task,
   * saying this user has signed off and no more in the list</p>
   * @author madan
   */
  private void finishCurrentTransactorSignOff(){
	  try{
//		  Context
//	      .getCommandContext().getTaskEntityManager().deleteCurrentAssigneeIdentityLink(this);
		  this.setAssignee(null);
		  this.update();
	  }catch(Exception e){
		  throw new BpmException("Cannot finish current trasactor sign off for task "+this.getId());
	  }
	  
  }
  
  private void deleteIdentityLinksByTaskId(){
	  try{
		  Context
	      .getCommandContext().getTaskEntityManager().deleteIdentityLinksByTaskId(this);
	  }catch(Exception e){
		  throw new BpmException("Cannot finish current trasactor sign off for task "+this.getId());
	  }
	  
  }
  
  /**
   * <p>Final sign off by the co-ordinator level transactor indicating he has saved the task,
   * not signed off just saved the form values, he is done with his part</p>
   */
  public void saveOff(String loggedInUserId){
	  this.finishCurrentTransactorSaveOff(loggedInUserId);
  }
  
  /**
   * <p>Saving a task by user will delete all the {@link IdentityLinkEntity} associated to the task
   * by the user as {@link TaskRole} 's coordinator saying this user has saved and no more in the list</p>
   * @author madan
   */
  private void finishCurrentTransactorSaveOff(String loggedInUserId){
	  try{
		  Context
	      .getCommandContext().getTaskEntityManager().deleteCurrentCoordinatorIdentityLink(this,loggedInUserId);
		  this.setAssignee(null);
		  this.update();
	  }catch(Exception e){
		  throw new BpmException("Cannot finish current trasactor save off for task "+this.getId());
	  }
	  
  }
  
  /**
   * <p>Gets the next person, {@link IdentityLinkEntity} who is in the list to sign off
   * the task, by the order defined during process creation</p>
   * @return the next user as {@link IdentityLinkEntity}
   */
  private IdentityLinkEntity getNextCandidateByOrder(){
	  return Context
      .getCommandContext().getTaskEntityManager().getNextCandidateIdentityLinkByOrder(this);
  }
  public IdentityLinkEntity getCreatorIDentityLink(){
	  return Context
      .getCommandContext().getTaskEntityManager().getCreatorIDentityLink(this);
  }
  
  public IdentityLinkEntity getCreatorIDentityLink(String taskId){
	  return Context
      .getCommandContext().getTaskEntityManager().getCreatorIDentityLinkForTaskId(taskId);
  }
  
  
  private IdentityLinkEntity getNextCandidateByOrder(IdentityLinkEntity nextcandidateIdentity){
	  return Context
      .getCommandContext().getTaskEntityManager().getNextCandidateIdentityLinkByOrder(nextcandidateIdentity);

  }
  
  public String getPreviousTaskAssignee(){
	  return Context
		      .getCommandContext().getHistoricTaskInstanceEntityManager().getPreviousTaskAssignee(this.processInstanceId);
  }

  public void complete() {
    fireEvent(TaskListener.EVENTNAME_COMPLETE);
    //Get task complete user name
    String processedUser = CommonUtil.getLoggedInUserId();
    this.setAssignee(processedUser);
    this.setIsDratf(false);
   // this.setHtmlSourceForSubForm(htmlSourceForSubForm);
   // this.setFormkey(formkey);
    Context
      .getCommandContext()
      .getTaskEntityManager()
      .deleteTask(this, TaskEntity.DELETE_REASON_COMPLETED, false);
    
    if (executionId!=null) {
      ExecutionEntity execution = getExecution();
      execution.removeTask(this);
      execution.signal(null, null);
    }
    //Set the processed user to the next task
    /*for(TaskEntity ent : execution.getTasks()){
    	ent.addAdminProcessedUser(processedUser);
    }*/
  }
  
  public void delegate(String userId) {
    setDelegationState(DelegationState.PENDING);
    if (getOwner() == null) {
      setOwner(getAssignee());
    }
    setAssignee(userId);
  }
  
  public void transfer(String userId) {
	  setAssignee(userId);
  }

  public void resolve() {
    setDelegationState(DelegationState.RESOLVED);
    setAssignee(this.owner);
  }

  public Object getPersistentState() {
    Map<String, Object> persistentState = new  HashMap<String, Object>();
    persistentState.put("assignee", this.assignee);
    persistentState.put("owner", this.owner);
    persistentState.put("name", this.name);
    persistentState.put("priority", this.priority);
    if (executionId != null) {
      persistentState.put("executionId", this.executionId);
    }
    if (processDefinitionId != null) {
      persistentState.put("processDefinitionId", this.processDefinitionId);
    }
    if (createTime != null) {
      persistentState.put("createTime", this.createTime);
    }
    if(description != null) {
      persistentState.put("description", this.description);
    }
    if(dueDate != null) {
      persistentState.put("dueDate", this.dueDate);
    }
    if (parentTaskId != null) {
      persistentState.put("parentTaskId", this.parentTaskId);
    }
    if (delegationState != null) {
      persistentState.put("delegationState", this.delegationState);
    }
    
    persistentState.put("suspensionState", this.suspensionState);
    
    return persistentState;
  }
  
  public int getRevisionNext() {
    return revision+1;
  }

  // variables ////////////////////////////////////////////////////////////////
  
  @Override
  protected VariableScopeImpl getParentVariableScope() {
    if (getExecution()!=null) {
      return execution;
    }
    return null;
  }

  @Override
  protected void initializeVariableInstanceBackPointer(VariableInstanceEntity variableInstance) {
    variableInstance.setTaskId(id);
    variableInstance.setExecutionId(executionId);
    variableInstance.setProcessInstanceId(processInstanceId);
  }

  @Override
  protected List<VariableInstanceEntity> loadVariableInstances() {
    return Context
      .getCommandContext()
      .getVariableInstanceEntityManager()
      .findVariableInstancesByTaskId(id);
  }

  // execution ////////////////////////////////////////////////////////////////

  public ExecutionEntity getExecution() {
    if ( (execution==null) && (executionId!=null) ) {
      this.execution = Context
        .getCommandContext()
        .getExecutionEntityManager()
        .findExecutionById(executionId);
    }
    return execution;
  }
  
  public void setExecution(DelegateExecution execution) {
    if (execution!=null) {
      this.execution = (ExecutionEntity) execution;
      this.executionId = this.execution.getId();
      this.processInstanceId = this.execution.getProcessInstanceId();
      this.processDefinitionId = this.execution.getProcessDefinitionId();
      
      Context.getCommandContext().getHistoryManager().recordTaskExecutionIdChange(this.id, executionId);
      
    } else {
      this.execution = null;
      this.executionId = null;
      this.processInstanceId = null;
      this.processDefinitionId = null;
      
      throw new ActivitiException("huh?");
    }
  }
    
  // task assignment //////////////////////////////////////////////////////////
  
  public IdentityLinkEntity addIdentityLink(String userId, String groupId, String type,String groupType) {
	  
    IdentityLinkEntity identityLinkEntity = IdentityLinkEntity.createAndInsert();
    getIdentityLinks().add(identityLinkEntity);
    identityLinkEntity.setTask(this);
    identityLinkEntity.setUserId(userId);
    identityLinkEntity.setGroupId(groupId);
    identityLinkEntity.setType(type);
    identityLinkEntity.setGroupType(groupType);
    return identityLinkEntity;
  }
  
	public IdentityLinkEntity addIdentityLink(String userId, String groupId, String type, int order , String groupType, String processInstanceId) {

	  	IdentityLinkEntity identityLinkEntity = IdentityLinkEntity.createAndInsert();
	    getIdentityLinks().add(identityLinkEntity);
	    identityLinkEntity.setTask(this);
	    identityLinkEntity.setUserId(userId);
	    identityLinkEntity.setGroupId(groupId);
	    identityLinkEntity.setType(type);
	    identityLinkEntity.setOrder(order);
	    identityLinkEntity.setGroupType(groupType);
	    identityLinkEntity.setProcessInstanceId(processInstanceId);
	    return identityLinkEntity;
  }
  
  public IdentityLinkEntity addIdentityLinkWithParentId(String userId, String groupId, String type, int order , String groupType,String parentId, String operationType) {
	  	IdentityLinkEntity identityLinkEntity = IdentityLinkEntity.createAndInsert();
	    getIdentityLinks().add(identityLinkEntity);
	    identityLinkEntity.setTask(this);
	    identityLinkEntity.setUserId(userId);
	    identityLinkEntity.setGroupId(groupId);
	    identityLinkEntity.setType(type);
	    identityLinkEntity.setOrder(order);
	    identityLinkEntity.setGroupType(groupType);
	    identityLinkEntity.setParentId(parentId);
	    identityLinkEntity.setOperationType(operationType);
	    return identityLinkEntity;
  }
  
  public void setNotificationForAgency(AgencySetting agencySetting,String[] agentIds){
	  if(agencySetting.getIsMail()){
		  setNotificationByType(agencySetting,agentIds,Constants.NOTIFICATION_EMAIL_TYPE);
	  }
	  if(agencySetting.getIsSms()){
		  setNotificationByType(agencySetting,agentIds,Constants.NOTIFICATION_SMS_TYPE);  
	  }
	  if(agencySetting.getIsInternalMessage()){
		  setNotificationByType(agencySetting,agentIds,Constants.NOTIFICATION_INTERNALMESSAGE_TYPE);  
	  }
  }
  
  private void setNotificationByType(AgencySetting agencySetting,String[] agentIds,String type){
	  NotificationDetailEntity notification = null;
	  for(String agentId:agentIds){
		  notification = new NotificationDetailEntity();
		  notification.setNotificationType(type);
		  notification.setMessage(agencySetting.getNotificationMessage());
		  notification.setStatus(Constants.ACTIVE);
		  notification.setMessageSendOn(new Date());
		  notification.setSubject(agencySetting.getNotificationMessage());
		  notification.setType(Constants.USER);
		  notification.setUserId(agentId);
		  Context.getCommandContext().getDbSqlSession().insert(notification);
	  }
  }
  
  public IdentityLinkEntity addIdentityLinkWithOperationType(String userId, String groupId, String type, int order , String groupType,String operationType,String parentId) {

	  	IdentityLinkEntity identityLinkEntity = IdentityLinkEntity.createAndInsert();
	    getIdentityLinks().add(identityLinkEntity);
	    identityLinkEntity.setTask(this);
	    identityLinkEntity.setUserId(userId);
	    identityLinkEntity.setGroupId(groupId);
	    identityLinkEntity.setType(type);
	    identityLinkEntity.setOrder(order);
	    identityLinkEntity.setGroupType(groupType);
	    identityLinkEntity.setOperationType(operationType);
	    identityLinkEntity.setParentId(parentId);
	    return identityLinkEntity;
  }
  
  public void deleteIdentityLink(String userId, String groupId, String type, boolean isInsertHisIdentity) {
    List<IdentityLinkEntity> identityLinks = Context
      .getCommandContext()
      .getIdentityLinkEntityManager()
	      .findIdentityLinkByTaskUserGroupAndType(id, userId, groupId, type);
	    if(isInsertHisIdentity){
		    for (IdentityLinkEntity identityLink: identityLinks) {
		    	HistoricIdentityLinkEntity.createAndInsert(identityLink);
		      Context
		        .getCommandContext()
		        .getDbSqlSession()
		        .delete(identityLink);
		    }
	  }else{
		  for (IdentityLinkEntity identityLink: identityLinks) {
			  if(identityLink.getParentId() != null){
				  Context
			      .getCommandContext().getIdentityLinkEntityManager().deleteIdentityLinksWithRelationById(identityLink.getParentId(), isInsertHisIdentity);
				  
			  }else{
				  Context
			      .getCommandContext().getIdentityLinkEntityManager().deleteIdentityLinksWithRelationById(identityLink.getId(), isInsertHisIdentity);
			  }
			  
		    }
	  }
  }
  
  
  
  public Set<IdentityLink> getCandidates() {   
    return getIdentityLinksByType(IdentityLinkType.CANDIDATE);
  }
  
  public Set<IdentityLink> getOrganizers() {   
	    return getIdentityLinksByType(IdentityLinkType.CANDIDATE);
  }
  
  public Set<IdentityLink> getReaders() {	    
	    return getIdentityLinksByType(IdentityLinkType.READER);
  }
  
  public Set<IdentityLink> getCoordinators() {	    
	    return getIdentityLinksByType(IdentityLinkType.COORDINATOR);
  }
  
  public Set<IdentityLink> getCreators() {	    
	    return getIdentityLinksByType(IdentityLinkType.CREATOR);
  }
  
  public Set<IdentityLink> getProcessedUsers() {	    
	    return getIdentityLinksByType(IdentityLinkType.PROCESSED_USER);
  }
  
  public Set<IdentityLink> getWorkflowAdmins() {	    
	    return getIdentityLinksByType(IdentityLinkType.WORKFLOW_ADMINISTRATOR);
  }
  
  private Set<IdentityLink> getIdentityLinksByType(String type) {
	    Set<IdentityLink> potentialOwners = new HashSet<IdentityLink>();
	    for (IdentityLinkEntity identityLinkEntity : getIdentityLinks()) {
	      if (type.equals(identityLinkEntity.getType())) {
	        potentialOwners.add(identityLinkEntity);
	      }
	    }
	    return potentialOwners;
  }
  
	public void addCandidateUser(String userId) {
	int order = getIdentityOrderFromNameExpression(userId);
    addIdentityLink(getIdentityNameFromNameExpression(userId), null, IdentityLinkType.CANDIDATE, order,null, null);
  }
  
 /** The purpose of validation user is to skip the insertion of the Identity of the user if he as Assignee of the previous task*/ 
  
  public void addCandidateUsers(Collection<String> candidateUsers, int expressionSize ,String previousTaskAssignee,String skipRepeat) {

    for (String candidateUser : candidateUsers) {
    	if(expressionSize > 1 && skipRepeat.equalsIgnoreCase("true") ){
    		String[] nameOrder = candidateUser.split("-");
        	if(!nameOrder[0].equals(previousTaskAssignee) ){
        		addCandidateUser(candidateUser);
        	}
    	}else{
    		addCandidateUser(candidateUser);
    	}
    }
  }
  
  public void addCandidateGroup(String groupId) {
	int order = getIdentityOrderFromNameExpression(groupId);
	String groupType = getIdentityGroupTypeFromNameExpression(groupId);
    addIdentityLink(null, getIdentityNameFromNameExpression(groupId), IdentityLinkType.CANDIDATE, order,groupType, null);
  }
  
  public void addCandidateGroups(Collection<String> candidateGroups) {
    for (String candidateGroup : candidateGroups) {
      addCandidateGroup(candidateGroup);
    }
  }
  
  public void addAdminCreator(String userId) {
	    addIdentityLink(getIdentityNameFromNameExpression(userId), null, IdentityLinkType.CREATOR, 0,null, null);
	  }
  
  public void addAdminProcessedUser(String userId) {
	    addIdentityLink(getIdentityNameFromNameExpression(userId), null, IdentityLinkType.PROCESSED_USER, 0,null, null);
	  }
  
  public void addAdminUser(String userId) {
	    addIdentityLink(getIdentityNameFromNameExpression(userId), null, IdentityLinkType.WORKFLOW_ADMINISTRATOR, 0,null, null);
	  }
  
  public void addAdminUsers(Collection<String> adminUsers) {
	    for (String adminUser : adminUsers) {
	    	addAdminUser(adminUser);
	    }
	  }
  
  public void addReaderUser(String userId) {
	int order = getIdentityOrderFromNameExpression(userId);
    addIdentityLink(getIdentityNameFromNameExpression(userId), null, IdentityLinkType.READER, order,null, null);
  }
	  
  public void addReaderUsers(Collection<String> readerUsers) {
    for (String readerUser : readerUsers) {
    	addReaderUser(readerUser);
    }
  }
  
  public void addReaderGroup(String groupId) {
	int order = getIdentityOrderFromNameExpression(groupId);
	String groupType = getIdentityGroupTypeFromNameExpression(groupId);
    addIdentityLink(null, getIdentityNameFromNameExpression(groupId), IdentityLinkType.READER, order,groupType, null);
  }
  
  public void addReaderGroups(Collection<String> readerGroups) {
    for (String readerGroup : readerGroups) {
    	addReaderGroup(readerGroup);
    }
  }
  
	public void addAdminUsersGroups(Collection<String> admins) {
	    for (String admin : admins) {
	    	addAdminUsersGroups(admin);
	    }
	}
	public void addAdminUsersGroups(String admin) {
		int order = getIdentityOrderFromNameExpression(admin);
		String groupType = getIdentityGroupTypeFromNameExpression(admin);
	    addIdentityLink(null, getIdentityNameFromNameExpression(admin), IdentityLinkType.WORKFLOW_ADMINISTRATOR, order,groupType, null);
	}
  
  public void addGroupIdentityLink(String groupId, String identityLinkType) {
    addIdentityLink(null, groupId, identityLinkType,null);
  }

  public void addUserIdentityLink(String userId, String identityLinkType) {
    addIdentityLink(userId, null, identityLinkType,null);
  }

  public void deleteCandidateGroup(String groupId) {
    deleteGroupIdentityLink(groupId, IdentityLinkType.CANDIDATE);
  }

  public void deleteCandidateUser(String userId) {
    deleteUserIdentityLink(userId, IdentityLinkType.CANDIDATE);
  }
  
  public void deleteReaderGroup(String groupId) {
    deleteGroupIdentityLink(groupId, IdentityLinkType.READER);
  }

  public void deleteReaderUser(String userId) {
    deleteUserIdentityLink(userId, IdentityLinkType.READER);
  }

  public void deleteGroupIdentityLink(String groupId, String identityLinkType) {
    if (groupId!=null) {
      deleteIdentityLink(null, groupId, identityLinkType, true);
    }
  }

  public void deleteUserIdentityLink(String userId, String identityLinkType) {
    if (userId!=null) {
      deleteIdentityLink(userId, null, identityLinkType, true);
    }
  }

  public List<IdentityLinkEntity> getIdentityLinks() {
    if (!isIdentityLinksInitialized) {
      taskIdentityLinkEntities = Context
        .getCommandContext()
        .getIdentityLinkEntityManager()
        .findIdentityLinksByTaskId(id);
      isIdentityLinksInitialized = true;
    }
    
    return taskIdentityLinkEntities;
  }
  
//  public List<TransactorIdentityLinkEntity> getTransactorIdentityLinks() {
//	    if (!isTransactorIdentityLinksInitialized) {
//	      transactorIdentityLinkEntities = Context
//	        .getCommandContext()
//	        .getTransactorIdentityLinkEntityManager()
//	        .findIdentityLinksByTaskId(id);
//	      isTransactorIdentityLinksInitialized = true;
//	    }
//	    
//	    return transactorIdentityLinkEntities;
//   }

  @SuppressWarnings("unchecked")
  public Map<String, Object> getActivityInstanceVariables() {
    if (execution!=null) {
      return execution.getVariables();
    }
    return Collections.EMPTY_MAP;
  }
  
  public void setExecutionVariables(Map<String, Object> parameters) {
    if (getExecution()!=null) {
      execution.setVariables(parameters);
    }
  }
  
  public String toString() {
    return "Task["+id+"]";
  }
  
  // special setters //////////////////////////////////////////////////////////
  
  public void setName(String taskName) {
    this.name = taskName;

    CommandContext commandContext = Context.getCommandContext();
    if (commandContext!=null) {
      commandContext
        .getHistoryManager()
        .recordTaskNameChange(id, taskName);
    }
  }

  /* plain setter for persistence */
  public void setNameWithoutCascade(String taskName) {
    this.name = taskName;
  }

  public void setDescription(String description) {
    this.description = description;

    CommandContext commandContext = Context.getCommandContext();
    if (commandContext!=null) {
      commandContext
        .getHistoryManager()
        .recordTaskDescriptionChange(id, description);
    }
  }

  /* plain setter for persistence */
  public void setDescriptionWithoutCascade(String description) {
    this.description = description;
  }

  public void setAssignee(String assignee) {
    if (assignee==null && this.assignee==null) {
      return;
    }
//    if (assignee!=null && assignee.equals(this.assignee)) {
//      return;
//    }
    this.assignee = assignee;

    CommandContext commandContext = Context.getCommandContext();
    if (commandContext!=null) {
      commandContext
        .getHistoryManager()
        .recordTaskAssigneeChange(id, assignee);
      
      // if there is no command context, then it means that the user is calling the 
      // setAssignee outside a service method.  E.g. while creating a new task.
      if (commandContext!=null) {
        fireEvent(TaskListener.EVENTNAME_ASSIGNMENT);
      }
    }
  }

  /* plain setter for persistence */
  public void setAssigneeWithoutCascade(String assignee) {
    this.assignee = assignee;
  }
  
  public void setOwner(String owner) {
    if (owner==null && this.owner==null) {
      return;
    }
//    if (owner!=null && owner.equals(this.owner)) {
//      return;
//    }
    this.owner = owner;

    CommandContext commandContext = Context.getCommandContext();
    if (commandContext!=null) {
      commandContext
        .getHistoryManager()
        .recordTaskOwnerChange(id, owner);
    }
  }

  /* plain setter for persistence */
  public void setOwnerWithoutCascade(String owner) {
    this.owner = owner;
  }
  
  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
    
    CommandContext commandContext = Context.getCommandContext();
    if (commandContext!=null) {
      commandContext
        .getHistoryManager()
        .recordTaskDueDateChange(id, dueDate);
    }
  }

  public void setDueDateWithoutCascade(Date dueDate) {
    this.dueDate = dueDate;
  }
  
  public void setPriority(int priority) {
    this.priority = priority;
    
    CommandContext commandContext = Context.getCommandContext();
    if (commandContext!=null) {
      commandContext
        .getHistoryManager()
        .recordTaskPriorityChange(id, priority);
    }
  }

  public void setPriorityWithoutCascade(int priority) {
    this.priority = priority;
  }
  
  public void setParentTaskId(String parentTaskId) {
    this.parentTaskId = parentTaskId;
    
    CommandContext commandContext = Context.getCommandContext();
    if (commandContext!=null) {
      commandContext
        .getHistoryManager()
        .recordTaskParentTaskIdChange(id, parentTaskId);
    }
  }

  public void setParentTaskIdWithoutCascade(String parentTaskId) {
    this.parentTaskId = parentTaskId;
  }
  
  public void setTaskDefinitionKeyWithoutCascade(String taskDefinitionKey) {
       this.taskDefinitionKey = taskDefinitionKey;
  }       

  public void fireEvent(String taskEventName) {
    TaskDefinition taskDefinition = getTaskDefinition();
    if (taskDefinition != null) {
      List<TaskListener> taskEventListeners = getTaskDefinition().getTaskListener(taskEventName);
      if (taskEventListeners != null) {
        for (TaskListener taskListener : taskEventListeners) {
          ExecutionEntity execution = getExecution();
          if (execution != null) {
            setEventName(taskEventName);
          }
          try {
            Context.getProcessEngineConfiguration()
              .getDelegateInterceptor()
              .handleInvocation(new TaskListenerInvocation(taskListener, (DelegateTask)this));
          }catch (Exception e) {
            throw new ActivitiException("Exception while invoking TaskListener: "+e.getMessage(), e);
          }
        }
      }
    }
  }
  
  @Override
  protected boolean isActivityIdUsedForDetails() {
    return false;
  }

  // modified getters and setters /////////////////////////////////////////////
  
  public void setTaskDefinition(TaskDefinition taskDefinition) {
    this.taskDefinition = taskDefinition;
    this.taskDefinitionKey = taskDefinition.getKey();
    
    CommandContext commandContext = Context.getCommandContext();
    if(commandContext != null) {
      commandContext.getHistoryManager().recordTaskDefinitionKeyChange(id, taskDefinitionKey);
    }
  }

  public TaskDefinition getTaskDefinition() {
    if (taskDefinition==null && taskDefinitionKey!=null) {
      ProcessDefinitionEntity processDefinition = Context
        .getProcessEngineConfiguration()
        .getDeploymentManager()
        .findDeployedProcessDefinitionById(processDefinitionId);
      taskDefinition = processDefinition.getTaskDefinitions().get(taskDefinitionKey);
    }
    return taskDefinition;
  }
  
  // getters and setters //////////////////////////////////////////////////////

  public int getRevision() {
    return revision;
  }

  public void setRevision(int revision) {
    this.revision = revision;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
  
  public Date getDueDate() {
    return dueDate;
  }
  
  public int getPriority() {
    return priority;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getExecutionId() {
    return executionId;
  }
  
  public String getProcessInstanceId() {
    return processInstanceId;
  }

  public String getProcessDefinitionId() {
    return processDefinitionId;
  }

  public void setProcessDefinitionId(String processDefinitionId) {
    this.processDefinitionId = processDefinitionId;
  }  
  
  public String getAssignee() {
    return assignee;
  }
  
  public String getTaskDefinitionKey() {
    return taskDefinitionKey;
  }
  
  public void setTaskDefinitionKey(String taskDefinitionKey) {
    this.taskDefinitionKey = taskDefinitionKey;
    
    CommandContext commandContext = Context.getCommandContext();
    if(commandContext != null) {
      commandContext.getHistoryManager().recordTaskDefinitionKeyChange(id, taskDefinitionKey);
    }
  }
  
  
  public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}


/**
 * {@inheritDoc}
 */
public int hashCode() {
    return (id != null ? id.hashCode() : 0);
}

/**
 * {@inheritDoc}
 */
public boolean equals(Object o) {

    if (this == o) {
        return true;
    }
    if (!(o instanceof TaskEntity)) {
        return false;
    }

    final TaskEntity task = (TaskEntity) o;

    return !(id != null ? !id.equals(task.id) : task.id != null);

}

public String getEventName() {
    return eventName;
  }
  public void setEventName(String eventName) {
    this.eventName = eventName;
  }
  public void setExecutionId(String executionId) {
    this.executionId = executionId;
  }
  public ExecutionEntity getProcessInstance() {
    return processInstance;
  }
  public void setProcessInstance(ExecutionEntity processInstance) {
    this.processInstance = processInstance;
  }
  public void setExecution(ExecutionEntity execution) {
    this.execution = execution;
  }
  public void setProcessInstanceId(String processInstanceId) {
    this.processInstanceId = processInstanceId;
  }
  public String getOwner() {
    return owner;
  }
  public DelegationState getDelegationState() {
    return delegationState;
  }
  public void setDelegationState(DelegationState delegationState) {
    this.delegationState = delegationState;
  }
  public String getDelegationStateString() {
    return (delegationState!=null ? delegationState.toString() : null);
  }
  public void setDelegationStateString(String delegationStateString) {
    this.delegationState = (delegationStateString!=null ? DelegationState.valueOf(DelegationState.class, delegationStateString) : null);
  }
  public boolean isDeleted() {
    return isDeleted;
  }
  public void setDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
  public String getParentTaskId() {
    return parentTaskId;
  }
  public Map<String, VariableInstanceEntity> getVariableInstances() {
    ensureVariableInstancesInitialized();
    return variableInstances;
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

	public int getSignOffType() {
		return signOffType;
	}
	
	public void setSignOffType(int signOffType) {
		this.signOffType = signOffType;
	}
	
	public Map<TaskRole, OperatingFunction> getOperatingFunctions() {
		return operatingFunctions;
	}
	
	public void setOperatingFunctions(
			Map<TaskRole, OperatingFunction> operatingFunctions) {
		this.operatingFunctions = operatingFunctions;
	}
	
	public void setSignOffType(String signOffType) {
		try{
			int signOffCode = Integer.parseInt(signOffType);
			this.signOffType = signOffCode;
		}catch(NumberFormatException e){
			this.signOffType = TransactorType.SINGLE_PLAYER_SINGLE_SIGNOFF.getStateCode();
		}	
	} 
	
	public List<IdentityLink>getIdentityLinksApplicableForUser(User user){
		CommandContext commandContext = Context.getCommandContext();
		  return commandContext.getTaskEntityManager().findIdentityLinksApplicableForUser(this.getId(), user);
	}
	
	public List<IdentityLink>getIdentityLinksApplicableForUser(User user, List<String> excludeTypes){
		CommandContext commandContext = Context.getCommandContext();
		  return commandContext.getTaskEntityManager().findIdentityLinksApplicableForUser(this.getId(), user, excludeTypes);
	}
	
	public String getStartScript() {
		return startScript;
	}
	
	public void setStartScript(String startScript) {
		this.startScript = startScript;
	}
	
	public String getEndScript() {
		return endScript;
	}
	
	public void setEndScript(String endScript) {
		this.endScript = endScript;
	}
	
	
	public String getStartScriptName() {
		return startScriptName;
	}
	
	public void setStartScriptName(String startScriptName) {
		this.startScriptName = startScriptName;
	}
	
	public String getEndScriptName() {
		return endScriptName;
	}
	
	public void setEndScriptName(String endScriptName) {
		this.endScriptName = endScriptName;
	}
	
	private String getIdentityNameFromNameExpression(String identityName){
		String[] nameOrder = identityName.split(TaskDefinition.CANDIDATE_ORDER_SEPERATOR);
		if(nameOrder.length>1){
			return nameOrder[0];
		}else{
			return identityName;
		}
	}
	
	private int getIdentityOrderFromNameExpression(String identityName){
		String[] nameOrder = identityName.split(TaskDefinition.CANDIDATE_ORDER_SEPERATOR);
		if(nameOrder.length>1){
			try{
				return Integer.parseInt(nameOrder[1]);
			}catch(NumberFormatException e){
				return 0;
			}
			
		}else{
			return 0;
		}
	}
	
	private String getIdentityGroupTypeFromNameExpression(String identityName){
	
		String[] nameOrder = identityName.split(TaskDefinition.CANDIDATE_ORDER_SEPERATOR);
		if(nameOrder.length>2){
			return nameOrder[nameOrder.length-1];
		}else{
			return identityName;
		}
	}
	

	/**
	 * @author Vinoth 
	 * modified by sangeetha
	 */
	public IdentityLinkEntity returnTask(String reason) {
		List<IdentityLinkEntity> identityLinks= this.getIdentityLinks();
		if (executionId != null) {
	    	/**
	    	 * checking the jumped activity is start node
	    	 * also check whether the start node has form or not
	    	 * if it has no from throwing error to user
	    	 */
			/*boolean isReturnedToStartNode = false;
			boolean noStartForm = true;
	    	PvmActivity jumpActivity = execution.getActivity();
	    	System.out.println("=========="+execution.getJumpActivityId());
	    	System.out.println("=========="+jumpActivity.getIncomingTransitions());*/

			/*if(((String) jumpActivity.getProperty("type")).equalsIgnoreCase("startEvent")) {
				 isReturnedToStartNode = true;
				 if(((ProcessDefinitionEntity)jumpActivity.getProcessDefinition()).getHasStartFormKey()){
					noStartForm	= false;
				 }
			}
			if(isReturnedToStartNode && noStartForm) {
				throw new BpmException("Back Operation is not possible if no Form in Start Node");
			}*/
			//end
			//deleting the current task records and removing the task from execution
			Context.getCommandContext().getTaskEntityManager().deleteTask(this,reason, false);
			ExecutionEntity execution = getExecution();
			execution.removeTask(this);//end
			execution.setHtmlSourceForSubForm(this.getHtmlSourceForSubForm());
			execution.setFormkey(this.getFormkey());
			execution.signal(null, null);
			//if it is start node returning the creator identity link for start node identity link
			/*if(isReturnedToStartNode) {
				IdentityLinkEntity creatorIdentityLinkEntity = null;
				for(IdentityLinkEntity identityLinkEntity : identityLinks){
					if(identityLinkEntity.getType().equalsIgnoreCase(IdentityLinkType.CREATOR)){
						creatorIdentityLinkEntity = identityLinkEntity; 
						break;
					}
				}
				return creatorIdentityLinkEntity;//end
			}else {
				//if it is not a start node doing the operation for task
				execution.signal(null, null);
			}*/
		}
		return null;
	}

	public String getDynamicNextOrganizer() {
		return dynamicNextOrganizer;
	}
	
	public void setDynamicNextOrganizer(String dynamicNextOrganizer) {
		this.dynamicNextOrganizer = dynamicNextOrganizer;
	}
	
	public String getDynamicNextReader() {
		return dynamicNextReader;
	}
	
	public void setDynamicNextReader(String dynamicNextReader) {
		this.dynamicNextReader = dynamicNextReader;
	}
	
	public String getProcessStartUserId() {
		return processStartUserId;
	}
	
	public void setProcessStartUserId(String processStartUserId) {
		this.processStartUserId = processStartUserId;
	}
	
	public String getDynamicOrganizerType() {
		return dynamicOrganizerType;
	}
	
	public void setDynamicOrganizerType(String dynamicOrganizerType) {
		this.dynamicOrganizerType = dynamicOrganizerType;
	}
	
	public String getDydnamicReaderType() {
		return dydnamicReaderType;
	}
	
	public void setDydnamicReaderType(String dydnamicReaderType) {
		this.dydnamicReaderType = dydnamicReaderType;
	}
	
	public int getMaxDays() {
		return maxDays;
	}
	public void setMaxDays(int maxDays) {
		this.maxDays = maxDays;
	}
	public int getWarningDays() {
		return warningDays;
	}
	public void setWarningDays(int warningDays) {
		this.warningDays = warningDays;
	}
	public String getDateType() {
		return dateType;
	}
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	public int getUrgeTimes() {
		return urgeTimes;
	}
	public void setUrgeTimes(int urgeTimes) {
		this.urgeTimes = urgeTimes;
	}

	public int getFrequenceInterval() {
		return frequenceInterval;
	}
	public void setFrequenceInterval(int frequenceInterval) {
		this.frequenceInterval = frequenceInterval;
	}
	
	public String getUndealOperation() {
		return undealOperation;
	}
	public void setUndealOperation(String undealOperation) {
		this.undealOperation = undealOperation;
	}
	
	public Date getLastUrgedTime() {
		return lastUrgedTime;
	}
	public void setLastUrgedTime(Date lastUrgedTime) {
		this.lastUrgedTime = lastUrgedTime;
	}
	
	public String getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	public String getIsOpinion() {
		return isOpinion;
	}
	public void setIsOpinion(String isOpinion) {
		this.isOpinion = isOpinion;
	}
}