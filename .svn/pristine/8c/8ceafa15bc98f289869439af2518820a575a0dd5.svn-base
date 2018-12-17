package com.eazytec.bpm.runtime.task.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.form.FormFieldPermission;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.persistence.entity.HistoricIdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.task.OperatingFunctionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Event;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.OperatingFunction;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.eazytec.Constants;
import com.eazytec.bpm.common.Messages;
import com.eazytec.bpm.common.util.I18nUtil;
import com.eazytec.bpm.common.util.TaskUtil;
import com.eazytec.bpm.engine.TaskRole;
import com.eazytec.bpm.engine.TransactorType;
import com.eazytec.bpm.metadata.form.service.FormDefinitionService;
import com.eazytec.bpm.metadata.process.service.ProcessDefinitionService;
import com.eazytec.bpm.metadata.task.service.TaskDefinitionService;
import com.eazytec.bpm.runtime.holiday.service.HolidayService;
import com.eazytec.bpm.runtime.task.service.OperatingFunctionAuditService;
import com.eazytec.bpm.runtime.task.service.OperatingFunctionService;
import com.eazytec.bpm.runtime.task.service.TaskService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.StringUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.OperatingFunctionAudit;
import com.eazytec.model.Opinion;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.util.DateUtil;

/**
 * <p>Task service at runtime, implements {@link TaskService}</p>
 * @author madan
 *
 */
@Service("rtTaskService")
public class TaskServiceImpl implements TaskService{	
	
	private FormDefinitionService formDefintionService;	
	private org.activiti.engine.TaskService taskService;
	private TaskDefinitionService taskDefinitionService;
	private HistoryService historyService;
	private com.eazytec.bpm.runtime.process.service.ProcessService rtProcessService;
	protected final Log log = LogFactory.getLog(getClass());
	private ProcessDefinitionService processDefinitionService;
	private RuntimeService runtimeService;
	private OperatingFunctionAuditService opAuditService;
	private OperatingFunctionService operatingFunctionService;
	private com.eazytec.bpm.runtime.form.service.FormService formService; 
	private HolidayService holidayService;
	private RepositoryService repositoryService;
	
	@Autowired
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	@Autowired
	public void setOpAuditService(OperatingFunctionAuditService opAuditService) {
		this.opAuditService = opAuditService;
	}
	@Autowired
	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}
	
	public ProcessDefinitionService getProcessDefinitionService() {
		return processDefinitionService;
	}
	@Autowired
	public void setProcessDefinitionService(
			ProcessDefinitionService processDefinitionService) {
		this.processDefinitionService = processDefinitionService;
	}

	@Autowired
	public void setFormService(
			com.eazytec.bpm.runtime.form.service.FormService formService) {
		this.formService = formService;
	}
	
	@Autowired
	public void setOperatingFunctionService(
			OperatingFunctionService operatingFunctionService) {
		this.operatingFunctionService = operatingFunctionService;
	}
	
	@Autowired
	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}
	/**
	 * {@inheritDoc getTaskFormData}
	 */
	public TaskFormData getTaskFormData(String taskId){
		try{
			return formDefintionService.getTaskFormData(taskId);
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}				
	}
	
	public TaskFormData getHistoricTaskFormData(HistoricTaskInstance task){
		try{
			return formDefintionService.getHistoricTaskFormData(task);
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}				
	}
	
	/**
	 * {@inheritDoc completeTask}
	 */
	public void completeTask(String taskId){
		if(StringUtil.isEmptyString(taskId)){
			  throw new EazyBpmException(I18nUtil.getMessageProperty("error.task.notCompleted"));    				
		}
		try{
			taskService.complete(taskId);
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}		
	}
	
	/**
	 * {@inheritDoc claimTask}
	 */
	 public void claimTask(String taskId, User user){
		 try{
			 taskService.claim(taskId, user.getId());
		 }catch(ActivitiException e){
			 throw new BpmException(e.getMessage(), e);
		 }		 		 
	 }	 
	 
	 /**
	  * {@inheritDoc getOwnerDetails}
	  */
	 public Map<String, Object> getOwnerDetails(Task task)throws BpmException{		 
		 Map<String, Object>ownerDetails = new HashMap<String, Object>();
		 String owner = task.getOwner();		 
		 String roleMessage = (owner != null && !owner.isEmpty())  ? Messages.TASK_OWNER : Messages.TASK_NO_OWNER;
		 ownerDetails.put("name", owner);
		 ownerDetails.put("taskId", task.getId());
		 ownerDetails.put("roleMessage", I18nUtil.getMessageProperty(roleMessage));
		 ownerDetails.put("transfer",I18nUtil.getMessageProperty(Messages.TASK_OWNER_TRANSFER));
		 return ownerDetails;
	 }
	 
	 /**
	  * {@inheritDoc getAssigneeDetails}
	  */
	 public Map<String, Object> getAssigneeDetails(Task task)throws BpmException{		 
		 Map<String, Object>assigneeDetails = new HashMap<String, Object>();
		 String assignee = task.getAssignee();	
		 String roleMessage = (assignee != null && !assignee.isEmpty()) ? Messages.TASK_ASSIGNEE : Messages.TASK_NO_ASSIGNEE;
		 assigneeDetails.put("name", assignee);
		 assigneeDetails.put("taskId", task.getId());
		 assigneeDetails.put("roleMessage", I18nUtil.getMessageProperty(roleMessage));
		 assigneeDetails.put("reassign",I18nUtil.getMessageProperty(Messages.TASK_ASSIGNEE_REASSIGN));
		 assigneeDetails.put("assign",I18nUtil.getMessageProperty(Messages.TASK_ASSIGNEE_ASSIGN));
		 return assigneeDetails;
	 }
	 
	 /**
	  * {@inheritDoc getInvlovedPeopleDetails}
	  */
	 public List<Map<String, Object>> getInvlovedPeopleDetails(Task task)throws BpmException{	
		 List<Map<String, Object>>involvedPeopleDetails = new ArrayList<Map<String,Object>>();		 
		 List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(task.getId());
		 if((identityLinks != null) && (identityLinks.size() > 0)){
			 for (IdentityLink identityLink : identityLinks) {
				 if (identityLink.getUserId() != null) { // only user identity links, ignoring the group ids
			        if (!IdentityLinkType.ASSIGNEE.equals(identityLink.getType())
			                && !IdentityLinkType.OWNER.equals(identityLink.getType())) {
			        	Map<String, Object>involvedPersonDetails = new HashMap<String, Object>();
			        	involvedPersonDetails.put("name", identityLink.getUserId());
			        	involvedPersonDetails.put("taskId", task.getId());
			        	involvedPersonDetails.put("roleMessage", identityLink.getType());
			        	involvedPersonDetails.put("involvedMembers",I18nUtil.getMessageProperty(Messages.PEOPLE_INVOLVE_POPUP_CAPTION));
			        	involvedPersonDetails.put("removeMembers",I18nUtil.getMessageProperty(Messages.TASK_INVOLVED_REMOVE));
			        	involvedPeopleDetails.add(involvedPersonDetails);
			        }
				 }
			 }
		 }else{
			 
		 }	 
		 		 
		 return involvedPeopleDetails;
	 }
	 
	 /**
	 * Get task by who logged in user and type
	 * @param type
	 * @param loggedInUser
	 * @return
	 * @throws Exception
	 */
	public List<Task> getMyTaskByType(User user, String type) throws BpmException{
		if(StringUtil.isEmptyString(type)){
			  throw new EazyBpmException(I18nUtil.getMessageProperty("error.type.notNull"));    				
		}
		String loggedInUser = user.getId();
		List<Task> loggedInUserTaskEntiyList = new ArrayList<Task>();
		 if(type.equalsIgnoreCase("mybucket")){
			 List<Task> taskList = new ArrayList<Task>();
			 taskList=getMyBucketGridData(user, 0);
//			 taskList.addAll(getMyBucketForCoordinator(user));
			// taskList.addAll(getMyBucketForAdmin(user));
			// taskList.addAll(getMyBucketForCreator(user));
			// taskList.addAll(getMyBucketForProcessedUser(user));
			 Set<Task> taskSet = new HashSet<Task>(taskList);
			 loggedInUserTaskEntiyList = new ArrayList<Task>(taskSet);
		 }else if(type.equalsIgnoreCase("myinvolvements")){
		     loggedInUserTaskEntiyList = getMyInvolvements(loggedInUser);
		 }else if(type.equalsIgnoreCase("waitinglist")){
		     loggedInUserTaskEntiyList = getMyWaitingTasks(user);		     
		 }else if(type.equalsIgnoreCase("toRead")){
		     loggedInUserTaskEntiyList = getToReadTask(user,false,false);		     
		 }else if(type.equalsIgnoreCase("readedList")){
		     loggedInUserTaskEntiyList = getToReadTask(user,true,false);		     
		 }else if(type.equalsIgnoreCase("myOwnedTasks")){
		     loggedInUserTaskEntiyList = getMyOwnedTasks(user);		     
		 }else if(type.equalsIgnoreCase("doneList")){
			 List<Task> taskList = new ArrayList<Task>();
			 taskList= getDoneTask(user,false);	
			/* taskList.addAll(getDoneCreatorTasks(user,false));
			 taskList.addAll(getDoneProcessedUserTasks(user));*/
			 Set<Task> taskSet = new HashSet<Task>(taskList);
			 loggedInUserTaskEntiyList = new ArrayList<Task>(taskSet);
		 }else if(type.equalsIgnoreCase("documentToBeDone")){
			 List<Task> taskList = new ArrayList<Task>();
			 taskList=getMyDocumentGridData(user);
			 Set<Task> taskSet = new HashSet<Task>(taskList);
			 loggedInUserTaskEntiyList = new ArrayList<Task>(taskSet);
		 }else if(type.equalsIgnoreCase("toReadDocument")){
		     loggedInUserTaskEntiyList = getToReadTask(user,false,true);		     
		 }else if(type.equalsIgnoreCase("readDocumentList")){
		     loggedInUserTaskEntiyList = getToReadTask(user,true,true);		     
		 }
		 else if(type.equalsIgnoreCase("doneDocumentList")){
			 List<Task> taskList = new ArrayList<Task>();
			 taskList= getDoneTask(user,true);	
			 taskList.addAll(getDoneCreatorTasks(user,true));
			// taskList.addAll(getDoneProcessedUserTasks(user));
			 Set<Task> taskSet = new HashSet<Task>(taskList);
			 loggedInUserTaskEntiyList = new ArrayList<Task>(taskSet);
		 } else if(type.equalsIgnoreCase("cancelDocumentList")) {
		     loggedInUserTaskEntiyList = getCancelDocumentData(user);		     
		 }  else{
			  throw new EazyBpmException(I18nUtil.getMessageProperty("error.invalid.gridType"));    				
		 }
		 		           
		return loggedInUserTaskEntiyList;
	}
	
	@Override
	public void taskPriority(String taskId, Integer priority){
		taskService.setPriority(taskId, priority);
	}
	
	
	@Override
	public Map<String,Object> getTaskCountByUserId(User user) throws Exception{
		Map<String,Object> countMap = new HashMap<String, Object>();
		int toDoListCount = 0;
		int toReadCount = 0;
		try{
				toDoListCount = getMyBucketGridData(user, 0).size();
				toReadCount = getToReadTask(user,false,false).size();	
		} catch(Exception e){
			log.info("Error while Getting todolist and toread count");
		}
		countMap.put("toDoListCount", toDoListCount);
		countMap.put("toReadCount", toReadCount);
		return countMap;
	}
	
	 /**
		 * Get task by who logged in user
		 * @param type
		 * @param loggedInUser
		 * @return
		 * @throws Exception
		 */
		public List<Task> getToDoListByUser(User user) throws BpmException{
			List<Task> loggedInUserTaskEntiyList = new ArrayList<Task>();
			List<Task> taskList = new ArrayList<Task>();
			taskList = getMyBucketGridData(user, 5);
			Set<Task> taskSet = new HashSet<Task>(taskList);
			loggedInUserTaskEntiyList = new ArrayList<Task>(taskSet);
			return loggedInUserTaskEntiyList;
		}
    
	/**
	 * Get Task that the assignee or claimed by the logged in user.
	 * @param user logged in user
	 * @return
	 * @throws BpmException
	 */
	// For Process Admin
	
		public List<Task> getMyBucketForAdmin(User user) throws BpmException{
			List<Task> userTaskList = new ArrayList<Task>(); 		
				userTaskList = taskService.createTaskQuery().customQuery("selectProcessAdminTasks").processAdminUser(user.getId()).active().orderByTaskId().asc().list();
				
			return userTaskList;		
		}
		
		
		public List<Task> getMyBucketForCreator(User user) throws BpmException{
			List<Task> userTaskList = new ArrayList<Task>(); 		
			//if (user.getGroupIds() != null && user.getGroupIds().size() > 0) {
				userTaskList = taskService.createTaskQuery().customQuery("selectProcessCreatorTasks").taskCreatorUser(user.getId()).active().orderByTaskId().asc().list();
				
				
			//}
			return userTaskList;		
		}
		
		
		
		public List<Task> getMyBucketForProcessedUser(User user) throws BpmException{
			List<Task> userTaskList = new ArrayList<Task>(); 		
			//if (user.getGroupIds() != null && user.getGroupIds().size() > 0) {
				userTaskList = taskService.createTaskQuery().customQuery("selectProcessedUserTasks").taskProcessedUser(user.getId()).active().orderByTaskId().asc().list();
				
			//}
			return userTaskList;		
		}
	
		
	// For Task Organiser

	public List<Task> getMyBucketForCanditate(User user) throws BpmException{
		List<Task> userTaskList = new ArrayList<Task>(); 		
		if (user.getGroupIds() != null && user.getGroupIds().size() > 0) {
			userTaskList = taskService.createTaskQuery().customQuery("selectIntegratedTasksForUser").taskCandidateUser(user.getId()).taskCandidateGroupIn(user.getGroupIds())
					.taskCandidateRoleIn(user.getRoleIds())
					.taskCandidateDepartmentIn(user.getDepartment().toString())
					//.taskAssignee(user.getId()).active().orderByTaskId().asc().list();
					.active().orderByTaskId().asc().list();
			
		}else{
			//userTaskList = taskService.createTaskQuery().customQuery("selectIntegratedTasksForUser").taskAssignee(user.getId()).taskCandidateUser(user.getId()).active().orderByTaskId().asc().list();
			userTaskList = taskService.createTaskQuery().customQuery("selectIntegratedTasksForUser").taskCandidateUser(user.getId()).active().orderByTaskId().asc().list();
		}
		return userTaskList;		
	}
	
	public List<Task> getMyBucketGridData(User user, int dataLimit) throws BpmException{
		List<Task> userTaskList = new ArrayList<Task>(); 
		if(user.getGroupIds().size() > 0){
			userTaskList = taskService.createTaskQuery().customQuery("getMyBucketGridData").taskCreatorUser(user.getId()).taskCandidateUser(user.getId())
					//.taskCandidateGroupIn(user.getGroupIds())
					//.taskCandidateRoleIn(user.getRoleIds())
					//.taskCandidateDepartmentIn(user.getDepartment().toString())
					//.taskAssignee(user.getId()).active().orderByTaskId().asc().list();
					.active().setDataLimit(dataLimit).list();
		}else {
			userTaskList = taskService.createTaskQuery().customQuery("getMyBucketGridData").taskCreatorUser(user.getId()).taskCandidateUser(user.getId()).active().setDataLimit(dataLimit).list();
		}
		//userTaskList.addAll(taskService.createTaskQuery().customQuery("selectReturnedStartNodeForMyBucket").taskCandidateUser(user.getId()).active().orderByTaskId().setDataLimit(dataLimit).asc().list());
		userTaskList.addAll(taskService.createTaskQuery().customQuery("selectProcessCreatorTasks").taskCreatorUser(user.getId()).orderByTaskCreateTime().asc().list());
		return userTaskList;		
	} 
	
	public List<Task> getMyDocumentGridData(User user) throws BpmException{
		List<Task> userTaskList = new ArrayList<Task>(); 
		if(user.getGroupIds().size() > 0){
			userTaskList = taskService.createTaskQuery().customQuery("getMyBucketGridData").processClassification("document_management").taskCandidateUser(user.getId()).taskCandidateGroupIn(user.getGroupIds())
					.taskCandidateRoleIn(user.getRoleIds())
					.taskCandidateDepartmentIn(user.getDepartment().toString())
					//.taskAssignee(user.getId()).active().orderByTaskId().asc().list();
					.active().orderByTaskId().asc().list();
		}else {
			userTaskList = taskService.createTaskQuery().customQuery("getMyBucketGridData").processClassification("document_management").taskCandidateUser(user.getId()).active().orderByTaskId().asc().list();
		}
		return userTaskList;
	}
	public List<Task> getCancelDocumentData(User user) throws BpmException{
		List<Task> userTaskList = new ArrayList<Task>(); 
		if(user.getGroupIds().size() > 0){
			userTaskList = taskService.createTaskQuery().customQuery("getCancelDocumentData").processClassification("document_management").taskCandidateUser(user.getId()).taskCandidateGroupIn(user.getGroupIds())
					.taskCandidateRoleIn(user.getRoleIds())
					.taskCandidateDepartmentIn(user.getDepartment().toString())
					//.taskAssignee(user.getId()).active().orderByTaskId().asc().list();
					.active().list();
		}else {
			userTaskList = taskService.createTaskQuery().customQuery("getCancelDocumentData").processClassification("document_management").taskCandidateUser(user.getId()).active().list();
		}
		return userTaskList;		
	}
	// For Task Coordinator
	public List<Task> getMyBucketForCoordinator(User user) throws BpmException{
		List<Task> userTaskList = new ArrayList<Task>(); 		
		if (user.getGroupIds() != null && user.getGroupIds().size() > 0) {
			userTaskList = taskService.createTaskQuery().customQuery("selectIntegratedTasksForUser").taskCandidateUser(user.getId())
					.taskCoordinatorUser(user.getId())
					.taskCoordinatorGroupIn(user.getGroupIds())
					.taskCoordinatorRoleIn(user.getRoleIds())
					.taskCoordinatorDepartmentIn(user.getDepartment().toString()).active().orderByTaskId().asc().list();	       
		}else{
			userTaskList = taskService.createTaskQuery().customQuery("selectIntegratedTasksForUser").taskAssignee(user.getId()).taskCandidateUser(user.getId()).taskCoordinatorUser(user.getId()).active().orderByTaskId().asc().list();
		}
		return userTaskList;		
	}
	
	// For My Owned task
	public List<Task> getMyOwnedTasks(User user) throws BpmException{
		List<Task> userTaskList = null;
		boolean isUserAdmin = false;
        for (Role role : user.getRoles()) {
			if(role.getId().equals(Constants.ADMIN_ROLE)){
				isUserAdmin = true;
				break;
			}
		}
        if(isUserAdmin){
        	 userTaskList =  taskService.createTaskQuery().customQuery("selectIntegratedMyOwnedTasks").active().orderByTaskId().asc().list();
        	 userTaskList.addAll(taskService.createTaskQuery().customQuery("selectTerminatedTasks").active().orderByTaskId().asc().list());
             //userTaskList.addAll(taskService.createTaskQuery().customQuery("selectReturnedStartNode").active().orderByTaskId().asc().list());
        }else {
        	 userTaskList =  taskService.createTaskQuery().customQuery("selectIntegratedMyOwnedTasks").processAdminUser(user.getId()).active().orderByTaskId().asc().list();
        	 userTaskList.addAll(taskService.createTaskQuery().customQuery("selectTerminatedTasks").taskCandidateUser(user.getId()).active().orderByTaskId().asc().list());
        	// userTaskList.addAll(taskService.createTaskQuery().customQuery("selectReturnedStartNode").taskCandidateUser(user.getId()).active().orderByTaskId().asc().list());
        }

		return userTaskList;	
	}
	
	public List<Task> getDoneCreatorTasks(User user,boolean isDocument) throws BpmException{
		List<Task> userTaskList = new ArrayList<Task>();
		if(isDocument){
			//userTaskList =  taskService.createTaskQuery().customQuery("selectIntegratedDoneCreator").processClassification("document_management").taskCreatorUser(user.getId()).active().orderByTaskId().asc().list();
			userTaskList.addAll(taskService.createTaskQuery().customQuery("selectDoneStartNode").processClassification("document_management").taskCreatorUser(user.getId()).active().orderByTaskId().asc().list());
		}else{
			//userTaskList =  taskService.createTaskQuery().customQuery("selectIntegratedDoneCreator").taskCreatorUser(user.getId()).active().orderByTaskId().asc().list();
			userTaskList.addAll(taskService.createTaskQuery().customQuery("selectDoneStartNode").taskCreatorUser(user.getId()).active().orderByTaskId().asc().list());
		}
		return userTaskList;		
	}
	
	public List<Task> getDoneProcessedUserTasks(User user) throws BpmException{
		List<Task> userTaskList =  taskService.createTaskQuery().customQuery("selectIntegratedDoneProcessedUser").taskProcessedUser(user.getId()).active().orderByTaskId().asc().list();
		return userTaskList;		
	}
	
	public List<Task> getToReadTask(User user,boolean isReaded,boolean isDocument) throws BpmException{
		List<Task> userTaskList = new ArrayList<Task>(); 		
		if (user.getGroupIds() != null && user.getGroupIds().size() > 0) {
				     //To show Read List  
					if(isReaded){
						if(isDocument){
							userTaskList = taskService.createTaskQuery().customQuery("selectReadedTask").processClassification("document_management").taskReaderUser(user.getId())
									.taskReaderGroupIn(user.getGroupIds())
									.taskReaderRoleIn(user.getRoleIds())
									.taskReaderDepartmentIn(user.getDepartment().getId()).active().orderByTaskId().asc().list();
							userTaskList.addAll(taskService.createTaskQuery().customQuery("selectReadedEndTask").processClassification("document_management").taskReaderUser(user.getId())
									.taskReaderGroupIn(user.getGroupIds())
									.taskReaderRoleIn(user.getRoleIds())
									.taskReaderDepartmentIn(user.getDepartment().getId()).active().orderByTaskId().asc().list());
						
						}else{
							userTaskList = taskService.createTaskQuery().customQuery("selectReadedTask").taskReaderUser(user.getId())
									.taskReaderGroupIn(user.getGroupIds())
									.taskReaderRoleIn(user.getRoleIds())
									.taskReaderDepartmentIn(user.getDepartment().getId()).active().orderByTaskId().asc().list();
							userTaskList.addAll(taskService.createTaskQuery().customQuery("selectReadedEndTask").taskReaderUser(user.getId())
									.taskReaderGroupIn(user.getGroupIds())
									.taskReaderRoleIn(user.getRoleIds())
									.taskReaderDepartmentIn(user.getDepartment().getId()).active().orderByTaskId().asc().list());
						}
					}else{
						//To show TO read List
						if(isDocument){
							userTaskList = taskService.createTaskQuery().customQuery("selectToReadTask").processClassification("document_management").taskReaderUser(user.getId())
									.taskReaderGroupIn(user.getGroupIds())
									.taskReaderRoleIn(user.getRoleIds())
									.taskReaderDepartmentIn(user.getDepartment().getId()).active().orderByTaskId().asc().list();
							userTaskList.addAll(taskService.createTaskQuery().customQuery("selectToReadEndTask").processClassification("document_management").taskReaderUser(user.getId())
									.taskReaderGroupIn(user.getGroupIds())
									.taskReaderRoleIn(user.getRoleIds())
									.taskReaderDepartmentIn(user.getDepartment().getId()).active().orderByTaskId().asc().list());
						}else{
						userTaskList = taskService.createTaskQuery().customQuery("selectToReadTask").taskReaderUser(user.getId())
								.taskReaderGroupIn(user.getGroupIds())
								.taskReaderRoleIn(user.getRoleIds())
								.taskReaderDepartmentIn(user.getDepartment().getId()).active().orderByTaskId().asc().list();
						userTaskList.addAll(taskService.createTaskQuery().customQuery("selectToReadEndTask").taskReaderUser(user.getId())
								.taskReaderGroupIn(user.getGroupIds())
								.taskReaderRoleIn(user.getRoleIds())
								.taskReaderDepartmentIn(user.getDepartment().getId()).active().orderByTaskId().asc().list());
						}
					}
		}else{
			 //To show Read List 
			if(isReaded){
				//To show TO read List
				if(isDocument){
					userTaskList = taskService.createTaskQuery().customQuery("selectReadedTask").processClassification("document_management").taskAssignee(user.getId()).taskReaderUser(user.getId()).active().orderByTaskId().asc().list();
					
					userTaskList.addAll(taskService.createTaskQuery().customQuery("selectReadedEndTask").processClassification("document_management").taskAssignee(user.getId()).taskReaderUser(user.getId()).active().orderByTaskId().asc().list());
				
				}else{
					userTaskList = taskService.createTaskQuery().customQuery("selectReadedTask").taskAssignee(user.getId()).taskReaderUser(user.getId()).active().orderByTaskId().asc().list();
				
					userTaskList.addAll(taskService.createTaskQuery().customQuery("selectReadedEndTask").taskAssignee(user.getId()).taskReaderUser(user.getId()).active().orderByTaskId().asc().list());
				}
			}else{
					if(isDocument){
						userTaskList = taskService.createTaskQuery().customQuery("selectToReadTask").processClassification("document_management").taskAssignee(user.getId()).taskReaderUser(user.getId()).active().orderByTaskId().asc().list();
					
						userTaskList.addAll(taskService.createTaskQuery().customQuery("selectToReadEndTask").processClassification("document_management").taskAssignee(user.getId()).taskReaderUser(user.getId()).active().orderByTaskId().asc().list());
					}else{
						userTaskList = taskService.createTaskQuery().customQuery("selectToReadTask").taskAssignee(user.getId()).taskReaderUser(user.getId()).active().orderByTaskId().asc().list();
						
						userTaskList.addAll(taskService.createTaskQuery().customQuery("selectToReadEndTask").taskAssignee(user.getId()).taskReaderUser(user.getId()).active().orderByTaskId().asc().list());
					}
				
				}
		}
		return userTaskList;		
	}
	
	public List<Task> getDoneTask(User user,boolean isDocument) throws BpmException{
		List<Task> userTaskList = new ArrayList<Task>(); 		
/*		if (user.getGroupIds() != null && user.getGroupIds().size() > 0) {
			if(isDocument){
				userTaskList = taskService.createTaskQuery().customQuery("selectDoneTask").processClassification("document_management").taskReaderUser(user.getId())
						.taskReaderGroupIn(user.getGroupIds())
						.taskReaderRoleIn(user.getRoleIds())
						.taskReaderDepartmentIn(user.getDepartment().getId()).orderByTaskId().asc().list();
			}else{
						userTaskList = taskService.createTaskQuery().customQuery("selectDoneTask").taskReaderUser(user.getId())
								.taskReaderGroupIn(user.getGroupIds())
								.taskReaderRoleIn(user.getRoleIds())
								.taskReaderDepartmentIn(user.getDepartment().getId()).orderByTaskId().asc().list();
			}
		}else{*/
			if(isDocument){
				userTaskList = taskService.createTaskQuery().customQuery("selectDoneTask").processClassification("document_management").taskCreatorUser(user.getId()).orderByTaskId().asc().list();
			}else{
				userTaskList = taskService.createTaskQuery().customQuery("selectDoneTask").taskCreatorUser(user.getId()).orderByTaskId().asc().list();
			}
		//}
		return userTaskList;		
	}
	/**
	 * Get Task that logged in user involved (Identity link - user id).
	 * @param loggedInUser
	 * @return
	 * @throws BpmException
	 */
	public List<Task> getMyInvolvements(String loggedInUser) throws BpmException{
	    List<Task> userTaskList = taskService.createTaskQuery().taskInvolvedUser(loggedInUser).active().orderByTaskId().asc().list();
        return userTaskList;
	}
	
	/**
	 * Get Task that assigned to the logged in user group which are unclaimed.
	 * @param loggedInUser
	 * @return
	 * @throws BpmException
	 */
	public List<Task> getMyWaitingTasks(User user) throws BpmException{	
		List<Task> userTaskList = new ArrayList<Task>(); 		
		if (user.getGroupIds() != null && user.getGroupIds().size() > 0) {
			userTaskList = taskService.createTaskQuery().customQuery("selectTasksForWaitingList").taskCandidateUser(user.getId()).taskCandidateGroupIn(user.getGroupIds()).active().orderByTaskId().asc().list();	       
		}
		return userTaskList;	
    }
	
	public Task getTask(String taskId) throws BpmException{	
		List<Task> userTaskList = new ArrayList<Task>(); 		
			userTaskList = taskService.createTaskQuery().customQuery("selectTask").taskId(taskId).list();	       
		return userTaskList.get(0);	
    }
	
	/**
	 * {@inheritDoc getHistoricTaskInstances}
	 */
	public List<HistoricTaskInstance> getHistoricTaskInstances(String processInstanceId){		
		try{
			return historyService.createHistoricTaskInstanceQuery()
				      .processInstanceId(processInstanceId)
				      .orderByHistoricTaskInstanceEndTime().desc()
				      .orderByHistoricActivityInstanceStartTime().desc()
				      .list();
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}
		
	}
	
	public List<HistoricTaskInstance> getHistoricTaskInstancesByTaskId(String taskId){		
		try{
			return historyService.createHistoricTaskInstanceQuery()
				      .taskId(taskId)
				      .orderByHistoricTaskInstanceEndTime().desc()
				      .orderByHistoricActivityInstanceStartTime().desc()
				      .list();
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}
		
	}
	
	public HistoricTaskInstance getHistoricTaskInstanceByTaskId(String taskId){			
			List<HistoricTaskInstance> taskInstances = getHistoricTaskInstancesByTaskId(taskId);
			if(taskInstances!=null && taskInstances.size()>0){
				return taskInstances.get(0);
			}else{
				  throw new BpmException(I18nUtil.getMessageProperty("error.noHistoricTask.found")+" Task Id : "+taskId);    					
			}				
	}
	
	public TaskFormData getHistoricTaskFormData(String taskId){
		try{
			List<HistoricTaskInstance> taskInstances = getHistoricTaskInstancesByTaskId(taskId);
			if(taskInstances!=null && taskInstances.size()>0){
				return getHistoricTaskFormData(taskInstances.get(0));
			}else{
				  throw new BpmException(I18nUtil.getMessageProperty("error.noHistoricTask.found")+" Task Id : "+taskId);    					
			}
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}
		
		
	}
	
	public Map<HistoricTaskInstance, List<FormProperty>> getTaskFormValuesByTaskId(String taskId)throws BpmException{
		HistoricTaskInstance newHistoricTaskInstance = null;
		try{
			newHistoricTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).list().get(0);
			if(newHistoricTaskInstance.getProcessInstanceId() != null){
				
				Map<String, Object> variables = CommonUtil.getStringRepresentations(rtProcessService.getVariablesForInstance(newHistoricTaskInstance.getProcessInstanceId()));
				List<HistoricTaskInstance>taskHistoryInstances = getHistoricTaskInstances(newHistoricTaskInstance.getProcessInstanceId());
				Map<HistoricTaskInstance, List<FormProperty>>historyDetails = new HashMap<HistoricTaskInstance, List<FormProperty>>();
				
				for (HistoricTaskInstance historicTaskInstance : taskHistoryInstances) {
					List<FormProperty> formPropertiesFinal = new ArrayList<FormProperty>();
					if(taskHistoryInstances.size() == 1){
						List<FormProperty> startFormProperties = formDefintionService.getStartFormData(historicTaskInstance.getProcessDefinitionId()).getFormProperties();
						for (FormProperty formProperty : startFormProperties) {
							formProperty.setValue((String)variables.get(formProperty.getName()));
							formPropertiesFinal.add(formProperty);
						}
					}else{
						List<FormProperty> taskFormProperties = getHistoricTaskFormData(historicTaskInstance).getFormProperties();
						for (FormProperty formProperty : taskFormProperties) {
							formProperty.setValue((String)variables.get(formProperty.getName()));
							formPropertiesFinal.add(formProperty);
						}
					}				

					historyDetails.put(historicTaskInstance, formPropertiesFinal);
				}
				return historyDetails;
				
			}else{
				return null;
			}			
						
		}catch(ActivitiException ae){
			  throw new BpmException(I18nUtil.getMessageProperty("error.formValues.notFound")+" Task Id : "+taskId);			
		}	
	}
	
	
	/**
	 * Resolve Task Details is to get set the needed columns for task details to grid
	 * 觉得该方法有些隐晦，参数中的List<Task> tasks 中的对象 可以是运行时任务，也可以是历史任务，只要不强制转换就不会报错,写法上很容易让人误解。
	 * 可使用instanceof来确定List中真正的对象类型
	 * for (Object o : tasks) { System.out.println(o instanceof HistoricTaskInstanceEntity); }
	 * 
	 * @param taskListAsMap
	 * @param user
	 * @param type
	 * @return
	 * @throws BpmException
	 * {@inheritDoc resolveTaskDetails}
	 */
	public List<Map<String, Object>> resolveTaskDetails(List<Task> tasks,User user,String type)throws BpmException{
				
		  List<Map<String, Object>> resolvedTaskListAsMap = new ArrayList<Map<String,Object>>();
		  Map<String, ProcessDefinition> processDefinitionCache = new HashMap<String, ProcessDefinition>();
	        try{
	       
	            List<Map<String, Object>> taskListAsMap = taskService.createTaskDescribeQuery(tasks);
	            for (Map<String, Object> taskMap : taskListAsMap) {
	            	taskMap.remove("variables");
	            	taskMap.remove("htmlSourceForSubForm");
	            	
	            	/**
            		List<Task> entities = (List<Task>)taskService.createTaskQuery().taskId((String)taskMap.get("id")).list();
            		TaskEntity taskEntity = null;
            		if(entities != null && entities.size() > 0){
            			taskEntity = (TaskEntity) entities.get(0);
            			//get node type for select user type
	            		Task currentTask = (Task) taskEntity;
	                    setTaskDefinition(currentTask);
	                    taskMap.put("nodeType", String.valueOf(taskEntity.getTaskDefinition().getNodeTypeExpression()));
	                    taskMap.put("taskClaimable", taskDefinitionService.isTaskClaimableByUser((Task)taskEntity, user));
	                    taskMap.put("taskFormApplicable", taskDefinitionService.isTaskFormApplicableForUser((Task)taskEntity, user));
            		}else{
	                    taskMap.put("taskClaimable", false);
	                    taskMap.put("taskFormApplicable", false);
            		}
            		**/
            		
	            	
	            	String processDefinitionId = taskMap.get("processDefinitionId").toString();
                    
                    ProcessDefinition processDefinition = processDefinitionCache.get(processDefinitionId);
                    if(processDefinition == null){
                    	processDefinition= processDefinitionService.getProcessDefinitionById(processDefinitionId);
                        processDefinitionCache.put(processDefinitionId, processDefinition);
                    }
	            	
        			if(taskMap.containsKey("createTime")&& taskMap.get("createTime")!=null){
        				taskMap.put("createTime",DateUtil.convertDateToSTDString(taskMap.get("createTime").toString()));
        			}else {
	                    taskMap.put("createTime",null);	
        			}
        			if(taskMap.containsKey("assigneeFullName")&& taskMap.get("assigneeFullName")!=null){
        				taskMap.put("submitUser",taskMap.get("assigneeFullName"));
        			}
        			if(taskMap.containsKey("endTime")&& taskMap.get("endTime")!=null){
        				taskMap.put("endTime",DateUtil.convertDateToSTDString(taskMap.get("endTime").toString()));
        			}else {
	                    taskMap.put("endTime",null);	
        			}
        			
            		// to check whether user can activate the suspended process
        			if(taskMap.containsKey("owner")&& taskMap.get("owner")!=null){
						if (user.getRoles().toString().contains("ROLE_ADMIN")
								|| taskMap.get("owner").toString().equals(CommonUtil.getLoggedInUser().toString())) {
							taskMap.put("canActivate", "true");
						} else {
							taskMap.put("canActivate", "false");
						}
        			}
        			taskMap.put("processInstanceId", taskMap.get("processInstanceId"));            			
        			taskMap.put("classificationId", processDefinition.getClassificationId());
        			taskMap.put("resourceName", processDefinition.getResourceName());
        			taskMap.put("processName",processDefinition.getName());
        			taskMap.put("processDefinitionId",processDefinition.getId());
        			OperatingFunctionAudit  opAudit = opAuditService.getLastOperatinPerformedToTask((String)taskMap.get("processInstanceId"));
                    if(opAudit!=null){
                    	taskMap.put("lastOperationPerformed",opAudit.getOperationType());
                    	taskMap.put("operate", opAudit.getOperationType());
                    }else{
                    	taskMap.put("lastOperationPerformed", "active");
                    }
                    
                    /**
                    if(type.equalsIgnoreCase("myinvolvements") || type.equalsIgnoreCase("waitinglist")){
                    	
                        if(taskEntity.getAssignee() == null){
                            List<IdentityLink> newIdentityLinkList = taskService.getIdentityLinksForTask(taskEntity.getId());
                            if( newIdentityLinkList != null && newIdentityLinkList.size() > 0){
                                String groupId = "";
                                for (IdentityLink identityLink : newIdentityLinkList) {
                                    if(identityLink.getGroupId() != null && identityLink.getType().equalsIgnoreCase("candidate")){
                                        if(groupId.isEmpty())
                                            groupId = identityLink.getGroupId();
                                        else
                                            groupId = groupId + ","+identityLink.getGroupId();
                                    }
                                }
                                taskMap.put("assignedGroup", groupId);
                            }
                        }
                    }
                    
                    
                    if(taskMap.get("lastOperationPerformed").equals(OperatingFunction.TERMINATE)){
                		taskMap.put("state", "Terminated");
                	}else if(taskMap.get("lastOperationPerformed").equals(OperatingFunction.SUSPEND)){
                		taskMap.put("state", "Suspended");
                	}else if(taskMap.get("lastOperationPerformed").equals(OperatingFunction.WITHDRAW)){
                		taskMap.put("state", "Withdrawn");
                	}else {
                		taskMap.put("state", "Active");
                	}
                    
                     **/
                    
                   
                    
                	if(taskMap.get("createTime") == null){
                		if(taskMap.get("startTime") != null){
                			taskMap.put("createTime",DateUtil.convertDateToSTDString(taskMap.get("startTime").toString()));	
                		}
                	}
                	
                	if(type.equalsIgnoreCase("doneList") || type.equalsIgnoreCase("doneDocumentList")){
                		taskMap.put("isFormReadOnly", true);
                		//we just want both done task name and current task name thats why we have put current
                		//task name in description and set
                		taskMap.put("currentTask",taskMap.get("description"));
                	}else{
                		taskMap.put("isFormReadOnly", false);
                		taskMap.put("currentTask",taskMap.get("name"));
                	}
           	
                    resolvedTaskListAsMap.add(taskMap);
                }
                return resolvedTaskListAsMap;
		} catch(ActivitiException ae){
			  throw new BpmException(I18nUtil.getMessageProperty("error.taskDetails"));    	
		}
	}
	
	/**
	 * {@inheritDoc addMember}
	 */
	
	public void addMember(String taskId, String userId, String type , String identityLinkType) throws BpmException{
		if(type.equalsIgnoreCase(I18nUtil.getMessageProperty(Messages.TASK_OWNER_TRANSFER))){
			 taskService.setOwner(taskId,userId);
		}else if((type.equalsIgnoreCase(I18nUtil.getMessageProperty(Messages.TASK_ASSIGNEE_REASSIGN))) || (type.equalsIgnoreCase(I18nUtil.getMessageProperty(Messages.TASK_ASSIGNEE_ASSIGN)))){
			 taskService.setAssignee(taskId,userId);
		}else if(type.equalsIgnoreCase(I18nUtil.getMessageProperty(Messages.PEOPLE_INVOLVE_POPUP_CAPTION))){
			 taskService.addUserIdentityLink(taskId, userId, identityLinkType);
		}
	}
	
	
	public void assignSubstitute(List<String> taskIds, String userId) throws BpmException{
		taskService.assignSubstitute(taskIds,userId);
	}
	
	/**
	 * {@inheritDoc removeMember}
	 */
	
	public void removeMember(String taskId, String userId, String identityLinkType) throws BpmException{
		taskService.deleteUserIdentityLink(taskId, userId, identityLinkType);
	}
	
	/**
	 * {@inheritDoc getTaskEvents}
	 */
	
	public List<Event> getTaskEvents(String taskId) throws BpmException{
		return taskService.getTaskEvents(taskId);
	}
	
	public OperatingFunction getOperatingFunctionForUser(User user, Task task){
		TaskRole taskRole = getTaskRoleApplicableForUser(user, task.getId());
		OperatingFunction operatingFunction = getOperatingFunctionForUser(user, task, taskRole);
		return operatingFunction;
	}
	
	public OperatingFunction getOperatingFunctionForUser(User user, Task task, TaskRole taskRole){
		task = taskService.setTaskDefinition(task);
		OperatingFunction operatingFunction = task.getTaskDefinition().getOperatingFunctions(taskRole);
		return operatingFunction;
	}
	
	/**
	 * {@inheritDoc getNodeLevelFieldPermissions}
	 */
	public Map<String, FormFieldPermission> getNodeLevelFieldPermissions(User user, String taskId, boolean isStartNodeTask){
		Map<String, FormFieldPermission> permissions = new HashMap<String, FormFieldPermission>();
		TaskRole taskRole = null; 
		if(isStartNodeTask) {
			taskRole = TaskRole.ORGANIZER;
		} else {
			taskRole = getTaskRoleApplicableForUser(user, taskId);
		}
		TaskFormData taskFormData = taskDefinitionService.getTaskFormData(taskId);
		List<FormProperty>formProperties = taskFormData.getFormProperties();
		for (FormProperty formProperty : formProperties) {
			Map<TaskRole, FormFieldPermission>formFieldPermissions = formProperty.getFormFieldPermissions();
			permissions.put(formProperty.getName(), formFieldPermissions.get(taskRole));
		}		
		return permissions;
	}
	
	public Map<String,FormFieldPermission> getStartNodeFieldPermission(String proDefId){
		Map<String, FormFieldPermission> permissions = new HashMap<String, FormFieldPermission>();
		List<FormProperty> startFormProperties = formDefintionService.getStartFormData(proDefId).getFormProperties();
		for (FormProperty formProperty : startFormProperties) {
			Map<TaskRole, FormFieldPermission>formFieldPermissions = formProperty.getFormFieldPermissions();
			permissions.put(formProperty.getName(), formFieldPermissions.get(TaskRole.ORGANIZER));
		}		
		return permissions;
	}
	
	public TaskRole getTaskRoleApplicableForUser(User user, String taskId){
		List<IdentityLink> identityLinks = taskService.getIdentityLinksForTaskAndUser(taskId, user);
		return TaskUtil.getTransactorTypeOfPriority(identityLinks);
	}
	
	/**
	 * {@inheritDoc isFinalTransactor}
	 */
	public boolean isFinalTransactor(String taskId){		
		TaskEntity taskEntity = (TaskEntity)taskService.createTaskQuery().taskId(taskId).singleResult();
		if(taskEntity!=null){
			int signOffType = taskEntity.getSignOffType();
			// If single sign off ormulti player single, any how the task will be completed ater this user even there are other users
			if(signOffType==TransactorType.SINGLE_PLAYER_SINGLE_SIGNOFF.getStateCode()||signOffType==TransactorType.MULTI_PLAYER_SINGLE_SIGNOFF.getStateCode()){
				return true;
			}
			List<IdentityLink>transactors = taskService.getIdentityLinksForTask(taskId, TaskRole.ORGANIZER.getName());
			
			// May have current user identity link also
			if(transactors.size()>1){
				return false;
			}else{
				return true;
			}
		}else{
			throw new BpmException("Not able to fetch task for "+taskId);
		}	
	}
	
	public TaskEntity getNextTaskForProcessInstance(String processInstanceId)throws BpmException{
		return taskService.getNextTaskForProcessInstance(processInstanceId);
	}
	
	public OperatingFunction getOperatingFunction(User user, Task task){
		Set<TaskRole> taskRoles = getTaskPermission(user, task.getId());
		Set<OperatingFunction> OperatingFunctions = new HashSet<OperatingFunction>();
		for(TaskRole taskRole : taskRoles){
			OperatingFunctions.add( getOperatingFunctionForUser(user, task, taskRole));
		}
		OperatingFunction newOperatingFunction = getPrimaryOperatingFunction(OperatingFunctions);
		return newOperatingFunction;
	}

	public OperatingFunction getOperatingFunctionForStartNode(User user, Task task){
		Set<TaskRole> taskRoles = getTaskPermission(user, task.getId());
		Set<OperatingFunction> OperatingFunctions = new HashSet<OperatingFunction>();
		if(taskRoles.contains(TaskRole.CREATOR)){
			OperatingFunctions.add( getOperatingFunctionForUser(user, task, TaskRole.CREATOR));
		}
		OperatingFunction newOperatingFunction = getPrimaryOperatingFunction(OperatingFunctions);
		return newOperatingFunction;
	}

	public Set<TaskRole> getTaskPermission(User user, String taskId){
		List<String> excludeTypes = new ArrayList<String>();
		excludeTypes.add(TaskRole.ORGANIZER.getName());
		List<IdentityLink> identityLinks = taskService.getIdentityLinksForTaskAndUser(taskId, user, excludeTypes);
		
		Set<TaskRole> taskRoles = new HashSet<TaskRole>();
		for(IdentityLink idLink :identityLinks){
			String taskRoleType = idLink.getType();
			if(taskRoleType!=null){
				taskRoles.add(TaskRole.GET_TASK_ROLE.getTaskRoleByName(taskRoleType));
			}
		}
		List<Task>organizerTasks = getMyBucketForCanditate(user);
		for (Task task : organizerTasks) {
			
			if(task.getId().equals(taskId)){
				taskRoles.add(TaskRole.ORGANIZER);
				break;
			}
		}
		return taskRoles;
	}

	public OperatingFunction getRecallFunctionForHistoricTask(User user, HistoricTaskInstance historicTask){
		Set<OperatingFunction> OperatingFunctions = new HashSet<OperatingFunction>();
		HistoricIdentityLinkEntity historicIdentityLink = (HistoricIdentityLinkEntity)runtimeService.getHistoricProcessedUserLink(historicTask);
			if(historicIdentityLink != null) {
				TaskRole taskRole = TaskRole.GET_TASK_ROLE.getTaskRoleByName(historicIdentityLink.getType());
				TaskDefinition taskDefinition = (TaskDefinition)runtimeService.getHistoricTaskDefinition(historicTask);
				OperatingFunctions.add(taskDefinition.getOperatingFunctions(taskRole));
				OperatingFunction newOperatingFunction = getPrimaryOperatingFunction(OperatingFunctions);
				return newOperatingFunction;
			}
			return null;
	}

	private OperatingFunction getPrimaryOperatingFunction(Set<OperatingFunction> OperatingFunctions){
		OperatingFunction newOpFunction = new OperatingFunctionImpl();
		for(OperatingFunction opFunction : OperatingFunctions){
			if(opFunction != null){
				newOpFunction.setAdd(newOpFunction.isAdd() || opFunction.isAdd());
				newOpFunction.setCirculatePerusal(newOpFunction.isCirculatePerusal() || opFunction.isCirculatePerusal());
				newOpFunction.setConfluentSignature(newOpFunction.isConfluentSignature() || opFunction.isConfluentSignature());
				newOpFunction.setJump(newOpFunction.isJump() || opFunction.isJump());
				newOpFunction.setRecall(newOpFunction.isRecall() || opFunction.isRecall());
				newOpFunction.setReturnOperation(newOpFunction.isReturnOperation() || opFunction.isReturnOperation());
				newOpFunction.setSave(newOpFunction.isSave() || opFunction.isSave());
				newOpFunction.setSubmit(newOpFunction.isSubmit() || opFunction.isSubmit());
				newOpFunction.setSuspend(newOpFunction.isSuspend() || opFunction.isSuspend());
				newOpFunction.setTerminate(newOpFunction.isTerminate() || opFunction.isTerminate());
				newOpFunction.setTransactorReplacement(newOpFunction.isTransactorReplacement() || opFunction.isTransactorReplacement());
				newOpFunction.setTransfer(newOpFunction.isTransfer() || opFunction.isTransfer());
				newOpFunction.setUrge(newOpFunction.isUrge() || opFunction.isUrge());
				newOpFunction.setWithdraw(newOpFunction.isWithdraw() || opFunction.isWithdraw());
				newOpFunction.setPrint(newOpFunction.isPrint() || opFunction.isPrint());
			}
			
		}
		return newOpFunction;
	}

	/**
	 * Check and Get past values for form if it is mapped in the previous task
	 * @param processInstanceId
	 * @return
	 */
	public Map<String,Object> checkAndGetPastValuesForForm(String processInstanceId,String taskId,String formId){
		Map<String,Object> elementValuesMap = new HashMap<String,Object>();
		List<Map<String,Object>> historicDetailsMap = taskService.getHistoricDetailForProcessInstance(processInstanceId,formId);
		boolean isInsert = false;
		for(Map<String,Object> historicDetails : historicDetailsMap){
			if(!elementValuesMap.containsKey(historicDetails.get("columnId"))){
				elementValuesMap.put(historicDetails.get("columnId").toString(), historicDetails.get("columnValue"));
			}
			
			if(!isInsert && historicDetails.get("taskId") != null){
				elementValuesMap.put("taskId", historicDetails.get("taskId"));
				elementValuesMap.put("startNode", false);
				isInsert =true;
			}
		}
		if(!isInsert){
			elementValuesMap.put("taskId", null);
			elementValuesMap.put("startNode", true);
		}
		return elementValuesMap;
	}
	
    /**
     * Check and Get past values for form if it is mapped in the previous task for different form with same table
     * @param processInstanceId
     * @return
     */
    public Map<String,Object> checkAndGetPastValuesForInstance (String processInstanceId,String taskId){
        Map<String,Object> elementValuesMap = new HashMap<String,Object>();
        List<Map<String,Object>> historicDetailsMap = taskService.getHistoricDetailForInstance(processInstanceId);
        Map<String, Object> historicDetailTreeMap = null;

        boolean isInsert = false;
        for(Map<String,Object> historicDetails : historicDetailsMap){
        	historicDetailTreeMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
        	historicDetailTreeMap.putAll(historicDetails);
          if(!elementValuesMap.containsKey(historicDetailTreeMap.get("columnId"))){
        	  // all subform values will have same column id. so appending the name of the form element with column id 
        	  if(historicDetailTreeMap.get("name").toString().startsWith("subForm_") && !elementValuesMap.containsKey(historicDetailTreeMap.get("columnId")+"_"+historicDetailTreeMap.get("name"))) {
              	elementValuesMap.put(historicDetailTreeMap.get("columnId")+"_"+historicDetailTreeMap.get("name"), historicDetailTreeMap.get("columnValue") );
              } else {
              	// replacing line breaks with space (for ckeditor bug fix)
              	String columnValue = (String) historicDetailTreeMap.get("columnValue");
              	//System.out.println("======"+columnValue+"=======");
              	if(columnValue != null && !columnValue.isEmpty()) {
              		columnValue = columnValue.replaceAll("(\r\n|\n)", " ");
              	} else {
              		columnValue = "";
              	}
                  elementValuesMap.put(historicDetailTreeMap.get("columnId").toString(), columnValue );
              }
            }
            if(!isInsert && historicDetailTreeMap.get("taskId") != null){
                elementValuesMap.put("taskId", historicDetailTreeMap.get("taskId"));
                elementValuesMap.put("startNode", false);
                isInsert =true;
            }

        }
        if(!isInsert){
            elementValuesMap.put("taskId", null);
            elementValuesMap.put("startNode", true);
        }
        return elementValuesMap;
    }
    
	/**
	 * Set the automatic form fill value to the target form
	 */
	public Map<String, Map<String, Object>> setFormFieldAutomatic(List<Map<String, String>> automaticFillFieldInfo, List<Opinion> opinionList, Task currentTask) throws EazyBpmException {
		Map<String, Map<String, Object>> fieldWithValue = new HashMap<String, Map<String, Object>>();
		String OrgSignOffTime = "";
		String organizer = "";
		String orgOpinion = "";
		String hisTaskId = null;
		HistoricTaskInstance hisTask = taskService.getPreviousHisTaskIndByInsId(currentTask.getProcessInstanceId());
		Map<String, Object> historicVariables = new HashMap<String, Object>();
		// Set Task organizer detail
		if (hisTask != null) {
			hisTaskId = hisTask.getId();
			OrgSignOffTime = DateUtil.convertDateToStringFormat(hisTask.getEndTime(), "yyyy-MM-dd HH:mm:ss");
			organizer = hisTask.getAssignee();

		} else {
			// Set Start node organizer detail
			OrgSignOffTime = DateUtil.convertDateToStringFormat(currentTask.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
			IdentityLinkEntity creatorIdentiLink = runtimeService.getCreatorIdentityLinkForTaskId(currentTask.getId()).get(0);
			organizer = creatorIdentiLink.getUserId();
		}

		for (Map<String, String> fieldInfo : automaticFillFieldInfo) {
			Map<String, Object> fieldValueMap = new HashMap<String, Object>();
			String fillType = fieldInfo.get(BpmnParse.AUTOMATIC_FIELD_VALUE);
			boolean isSkip = Boolean.valueOf(fieldInfo.get(BpmnParse.AUTOMATIC_IS_SKIP));
			if (fillType.equalsIgnoreCase("Organizer") && !isSkip) {
				fieldValueMap.put("value", organizer);
				fieldValueMap.put("type", fieldInfo.get(BpmnParse.AUTOMATIC_FIELD_TYPE));
				fieldWithValue.put(fieldInfo.get(BpmnParse.AUTOMATIC_FIELD_NAME), fieldValueMap);

			} else if (fillType.equalsIgnoreCase("OrganizerSignOffTime") && !isSkip) {
				fieldValueMap.put("value", OrgSignOffTime);
				fieldValueMap.put("type", fieldInfo.get(BpmnParse.AUTOMATIC_FIELD_TYPE));
				fieldWithValue.put(fieldInfo.get(BpmnParse.AUTOMATIC_FIELD_NAME), fieldValueMap);
			} else if (fillType.equalsIgnoreCase("Opinion") && !isSkip) {
				// To check to set the opinion once and can reuse it
				if (orgOpinion.isEmpty() && hisTask != null) {
					for (Opinion op : opinionList) {
						if (op.getUserId().equalsIgnoreCase(organizer) && op.getTaskId().equalsIgnoreCase(hisTask.getId())) {
							if (orgOpinion != null) {
								if (orgOpinion != "" && orgOpinion.length() > 0) {
									orgOpinion = orgOpinion + "," + op.getMessage();
								} else {
									orgOpinion = op.getMessage();
								}

							} else {
								orgOpinion = op.getMessage();
							}
						}
					}
				}
				fieldValueMap.put("value", orgOpinion);
				fieldValueMap.put("type", fieldInfo.get(BpmnParse.AUTOMATIC_FIELD_TYPE));
				fieldWithValue.put(fieldInfo.get(BpmnParse.AUTOMATIC_FIELD_NAME), fieldValueMap);
			} else if (!isSkip) {
				// To check to set historicVariables once and reuse it
				if (historicVariables.isEmpty()) {
					historicVariables = rtProcessService.getHistoricVariablesForInstance(currentTask.getProcessInstanceId(), hisTaskId);
				}
				// Check null to avoid previous value show in UI
				if (historicVariables != null && historicVariables.containsKey(fieldInfo.get(BpmnParse.AUTOMATIC_FROM_FORM_FILED))) {
					fieldValueMap.put("value", historicVariables.get(fieldInfo.get(BpmnParse.AUTOMATIC_FROM_FORM_FILED)));

				} else {
					fieldValueMap.put("value", "");
				}
				fieldValueMap.put("type", fieldInfo.get(BpmnParse.AUTOMATIC_FIELD_TYPE));
				fieldWithValue.put(fieldInfo.get(BpmnParse.AUTOMATIC_FIELD_NAME), fieldValueMap);

			}
		}
		// log.info("Automatic Form Field Info for Task :"+fieldWithValue);
		return fieldWithValue;
	}
	
	/**
	 * Get Task that logged in user involved (Identity link - user id).
	 * @param loggedInUser
	 * @return
	 * @throws BpmException
	 */
	public List<Task> getWorkFlowTraceData(String processInstanceId, String processDefinitionId) throws BpmException{
		return taskService.createTaskQuery().customQuery("getWorkFlowTraceData").processInstanceId(processInstanceId).processDefinitionId(processDefinitionId).list();
         
	}
	
	/**
	 * Get Task that logged in user involved (Identity link - user id).
	 * @param loggedInUser
	 * @return
	 * @throws BpmException
	 */
	public List<Task> getAllTimeSettingTasks() throws BpmException{
		return taskService.createTaskQuery().customQuery("getAllTimeSettingTasks").list();
	}
	
	public Task setTaskDefinition(Task task) {
		return taskService.setTaskDefinition(task);
	}
	
	public void addIdentityLinkDetail(String taskId){
		runtimeService.AddHistoricIdentityDetails(taskId);
	}
	
	public void addIdentityLinkDetail(String proInsId,String userId,boolean isRead){
		runtimeService.AddHistoricIdentityDetails(proInsId,userId,isRead);
	}
	
	public void removeIdentityLinkDetail(String taskId,String userId){
		runtimeService.removeHistoricIdentityDetails(taskId,userId);
	}
	
	public void updateIdentityLinkDetailReadStatus(String proInsId,String userId,boolean isRead){
		runtimeService.updateIdentityLinkDetailReadStatus(proInsId,userId,isRead);
	}
	
  	public JSONArray getFormFieldTraceData(String processInstanceId,String formId) throws BpmException{
		return formService.getFormFieldTraceData(processInstanceId, formId);
	}
  	
  	/**
  	 * Handling active tasks for process time setting 
  	 * Sending notification & handling task(like return, jump, notice suspend etc as per the requirement) if time out 
  	 */
	public String handleUnSubmittedTaskByTimeSetting(){
		String jobErrorMessage = "";
		//getting All time setting tasks ie) task with suspension state 1 and task with urge time != -1.
		// Urge time is set to -1 if any time operation such as (undeal,notice,return,jump) is performed
		List<Task> activeTasks = getAllTimeSettingTasks();
		Calendar calendar = null;
		calendar=Calendar.getInstance();
		//omitting the seconds in calender object for data checking
		Calendar currentTimeObject = Calendar.getInstance();
		currentTimeObject.set(Calendar.MILLISECOND, 0);		
		currentTimeObject.set(Calendar.SECOND, 0);
		for(Task activeTask : activeTasks){
			try{
				//handling the task
			if(activeTask.getUrgeTimes() != 0 ){
				if(activeTask.getLastUrgedTime() != null){
					//for the second time just sending notification within the frequence
					calendar.setTime(activeTask.getLastUrgedTime());
					calendar.add(Calendar.HOUR,activeTask.getFrequenceInterval());
				}else {
					//for the first time handling just checking the warning days
					calendar.setTime(activeTask.getCreateTime());
					calendar.add(Calendar.DATE,activeTask.getWarningDays());
				}
				calendar.set(Calendar.MILLISECOND, 0);
				calendar.set(Calendar.SECOND, 0);//end
				//checking the time for sending notification with current time
				if(calendar.getTime().compareTo(currentTimeObject.getTime()) <= 0){
					//sending notification after exceeding warning days
					if(activeTask.getWarningDays() > 0 && activeTask.getLastUrgedTime() == null && activeTask.getUrgeTimes() > 0){
						if(activeTask.getDateType().equals(Constants.WORK)){
							// adding warning day in calender 
							Calendar wordCalender = null;
							wordCalender=Calendar.getInstance();
							wordCalender.setTime(activeTask.getCreateTime());
							wordCalender.add(Calendar.DATE,activeTask.getWarningDays());
							
							// checking if warning day is a holiday. 
							Date warningDate = null;
							warningDate = DateUtils.truncate(wordCalender.getTime(), Calendar.DATE);
							List<Date> warningDates = new ArrayList<Date>();
							warningDates.add(warningDate);
							List<Date> holidays =new ArrayList<Date>();
							holidays = holidayService.getHolidayByDates(warningDates);
							// getting next working day for sending notification 
							int count = 1;
							if(holidays != null && !holidays.isEmpty()){
								while(holidays != null) {
									wordCalender.add(Calendar.DATE, 1);
									Date warningHoliday = null;
									warningHoliday = DateUtils.truncate(wordCalender.getTime(), Calendar.DATE);
									List<Date> warningHolidayList = new ArrayList<Date>();
									warningHolidayList.add(warningHoliday);
									holidays = holidayService.getHolidayByDates(warningHolidayList);
										if(holidays != null && !holidays.isEmpty()) {
											count++;

										} else {
											calendar.add(Calendar.DATE,count);
											calendar.add(Calendar.HOUR, -activeTask.getFrequenceInterval());
											break;
										}
								}
							} else {
								calendar.add(Calendar.HOUR, -activeTask.getFrequenceInterval());
							}
							//end
						} else {
							calendar.add(Calendar.HOUR, -activeTask.getFrequenceInterval());
						}
						TaskEntity taskEntity = (TaskEntity)activeTask;
						taskEntity.setLastUrgedTime(calendar.getTime());
						runtimeService.updateTaskEntityCmd(taskEntity);
					}else if(activeTask.getUrgeTimes() > 0){
						//sending notification to organizer 
						ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) processDefinitionService.getProcessDefinitionById(activeTask.getProcessDefinitionId());
						String urgeMessage = (String) processDefinition.getProperties().get(Constants.URGE_MESSAGE);
						urgeMessage = operatingFunctionService.replaceTextInUrgeMessage(urgeMessage, "", activeTask.getId(),"",true);
						TaskEntity taskEntity = (TaskEntity)activeTask;
						operatingFunctionService.sendNotification(operatingFunctionService.getOrganizersByTaskIdAndSignOffType(activeTask.getId(),taskEntity.getSignOffType()), urgeMessage+" "+activeTask.getName(),activeTask.getNotificationType(),"Urge Message");
						//reducing urge time after sending notification & setting last urge time for sending notification for the next time
						taskEntity.setUrgeTimes(activeTask.getUrgeTimes()-1);
						taskEntity.setLastUrgedTime(Calendar.getInstance().getTime());
						runtimeService.updateTaskEntityCmd(taskEntity);
					} 
				}
			} else {
					//Since urge time here is 0 , we ignore warning day and set max days in calender.
					calendar.setTime(activeTask.getCreateTime());
					calendar.add(Calendar.DATE,activeTask.getMaxDays());
					if(calendar.getTime().compareTo(currentTimeObject.getTime()) <= 0) {
						//handling time out functions
						if(activeTask.getUndealOperation().equals(Constants.UN_DEAL)){
							//we have no need deal anything but updating urgetime(0-1) as negative for omitting the task in query itself
							TaskEntity taskEntity = (TaskEntity)activeTask;
							//setting urge time as -1 for restricting the task for processing while job runs next time
							taskEntity.setUrgeTimes(activeTask.getUrgeTimes()-1);
							runtimeService.updateTaskEntityCmd(taskEntity);	
						}else if(activeTask.getUndealOperation().equals(Constants.NOTICE)){
							ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) processDefinitionService.getProcessDefinitionById(activeTask.getProcessDefinitionId());
							String noticeMessage = (String) processDefinition.getProperties().get(Constants.NOTIFICATION_MESSAGE);
							String workFlowAdmins = operatingFunctionService.getWorkFlowAdminByTaskId(activeTask.getId());
							operatingFunctionService.replaceTextInUrgeMessage(noticeMessage, "", activeTask.getId(),workFlowAdmins,true);							
							operatingFunctionService.sendNotification(workFlowAdmins, noticeMessage,activeTask.getNotificationType(),"Notification Message");
							//after sending notice to admin, updating urgetime(0-1) as negative for omitting the task in query itself
							TaskEntity taskEntity = (TaskEntity)activeTask;
							//setting urge time as -1 for restricting the task for processing while job runs next time
							taskEntity.setUrgeTimes(activeTask.getUrgeTimes()-1);
							runtimeService.updateTaskEntityCmd(taskEntity);	
						}else if(activeTask.getUndealOperation().equals(Constants.SUSPEND)){
							//here we dont set the urge time as -1 because the suspended task will be filtered in query itself
							operatingFunctionService.suspend(activeTask.getProcessInstanceId(), Constants.AUTOUPDATE, activeTask.getId(), activeTask.getProcessDefinitionId(), activeTask.getProcessDefinitionId());
						}else if(activeTask.getUndealOperation().equals(Constants.RETURN)){
							operatingFunctionService.taskReturn(Constants.AUTOUPDATE, activeTask.getId(), activeTask.getProcessDefinitionId(), Constants.RETURN,activeTask.getProcessInstanceId(), activeTask.getProcessDefinitionId());
							//setting urge time as -1 for restricting the task for processing while job runs next time
							TaskEntity taskEntity = (TaskEntity)activeTask;
							taskEntity.setUrgeTimes(-1);
							runtimeService.updateTaskEntityCmd(taskEntity);
						}else if(activeTask.getUndealOperation().equals(Constants.JUMP)){
							TaskDefinition taskDefinition = (TaskDefinition)runtimeService.taskJump(activeTask.getId(),null,false,Constants.FORWARDNODE);
							operatingFunctionService.setJointConductionForAutomaticJump(activeTask.getProcessInstanceId(),activeTask.getId(),taskDefinition,null);
							//setting organizer to the auto jump
							OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(Constants.AUTOUPDATE,activeTask.getId(),activeTask.getProcessDefinitionId(),OperatingFunction.JUMP,activeTask.getProcessInstanceId(), activeTask.getProcessDefinitionId());
							processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
						}
						
					} else { 
						/**
						 * save current task in historic task instance table as extended task (tasks which are not completed with in warning days). 
						 */
						TaskEntity taskEntity = (TaskEntity)activeTask;
						runtimeService.updateHistoricTaskEntityCmd(taskEntity);	
					}
			}
			}catch(Exception e){
				jobErrorMessage = jobErrorMessage+" "+activeTask.getId()+" "+e.getMessage();
			}
		}
		return jobErrorMessage;
	}
	
	public Map<String,Object> getScriptsInProcess(TaskEntity taskEntity,Map<String,Object> context) {
		Map <String,String> endEventScript = new HashMap<String, String>();
		String endScript = null;
		String endScriptName = null ;
		endScript = taskEntity.getEndScript();
		endScriptName = taskEntity.getEndScriptName();
		ModelMap model = new ModelMap();
		if(endScript != null && endScriptName != null) {
				endEventScript.put("endScriptName", endScriptName);
				endEventScript.put("endScript", endScript);
				model = rtProcessService.setEndEventScript(model,endEventScript);
				context.put("isEndScriptExist", model.get("isEndScriptExist"));
				context.put("endScript", model.get("endScript"));
				context.put("endScriptName", model.get("endScriptName"));
		} else {
			context.put("endScriptName", "");
		}
		// get start scripts for task
		if(taskEntity.getStartScript() != null && taskEntity.getStartScriptName() != null) {
			model = rtProcessService.setStartEventScriptForProcessInstance(taskEntity, model);
				context.put("isStartScriptExist",model.get("isStartScriptExist"));
				context.put("startScript", model.get("startScript"));
				context.put("startScriptName", model.get("startScriptName"));
		} else {
			context.put("startScriptName", "");
		}
		return context;
	}
	
	public void setInstanceTraceData(String defId,String insId,String taskId, ModelMap model,String processDefinitionName, String parentProcessDefinitionId, Task currentTask){
		ProcessDefinition processDefinition = null;
		try {
			String workFlowTrace = "";
			String parentWorkFlowTrace = "";
			processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(defId).active().list().get(0);
			if(currentTask != null ) {
				String taskName = currentTask.getName();
			}
			workFlowTrace = rtProcessService.getSvgString(processDefinition.getDeploymentId(), processDefinition.getResourceName());
			List<Task> tasks ;
			if(defId.equalsIgnoreCase(parentProcessDefinitionId)) {
				tasks = getWorkFlowTraceData(insId, null);
			} else {
				tasks = getWorkFlowTraceData(insId, defId);
			}
			// to show parent process svg string when current task belongs to any subprocess
			if(!defId.equalsIgnoreCase(parentProcessDefinitionId)) {
				ProcessDefinition parentProcessDef = repositoryService.createProcessDefinitionQuery().processDefinitionId(parentProcessDefinitionId).active().list().get(0);
				parentWorkFlowTrace = rtProcessService.getSvgString(parentProcessDef.getDeploymentId(), parentProcessDef.getResourceName());
				List<Task> parentTask = getWorkFlowTraceData(insId, null);
				JSONArray parentTraceJsonArray = new JSONArray();
				JSONObject parentTraceJson = null;
					for(Task traceTask:parentTask){
						parentTraceJson = new JSONObject();
						parentTraceJson.put("name", traceTask.getName());
						parentTraceJson.put("createdby", traceTask.getAssignee());
						parentTraceJson.put("createdtime", DateUtil.convertDateToSTDString(traceTask.getCreateTime().toString()));
						parentTraceJson.put("operationType", traceTask.getDescription());
						parentTraceJson.put("processDefinitionName", parentProcessDefinitionId.split(":")[0]);
						parentTraceJsonArray.put(parentTraceJson);
					}
					model.addAttribute("parentTraceJsonArray", parentTraceJsonArray);
 			}
			JSONArray traceJsonArray = new JSONArray();
			JSONObject traceJson = null;
				for(Task traceTask:tasks){
					traceJson = new JSONObject();
					traceJson.put("name", traceTask.getName());
					traceJson.put("createdby", traceTask.getAssignee());
					traceJson.put("createdtime", DateUtil.convertDateToSTDString(traceTask.getCreateTime().toString()));
					traceJson.put("operationType", traceTask.getDescription());
					traceJson.put("processDefinitionName", processDefinitionName);
					traceJsonArray.put(traceJson);
				}
			
			model.addAttribute("workFlowTrace", workFlowTrace);
			model.addAttribute("traceJsonArray", traceJsonArray);
			model.addAttribute("parentWorkFlowTrace", parentWorkFlowTrace);

		} catch (JSONException e) {
			  throw new BpmException(I18nUtil.getMessageProperty("error.getInstanceTrace")+" ProcessDefinition Name : "+processDefinition.getName());    						 
		}
	}
	
	@Override
	public List<Map<String, Object>> resolveHistoricTaskInstance(String processInstanceId) {
		List<Map<String, Object>>  resolvedHistoricTaskInstanceListAsMap = new ArrayList<Map<String,Object>>();
		try{
			List<Task> historicTaskInstances = taskService.createTaskQuery().customQuery("selectHistoricTaskColumns").processInstanceId(processInstanceId).list();
	  		 List<Map<String, Object>> taskListAsMap = taskService.createTaskDescribeQuery(historicTaskInstances);
			//List<Map<String, Object>>  historicTaskInstanceListAsMap = historyService.createHistoricTaskDescribeQuery(historicTaskInstanceList);
			for(Map<String, Object> historicTaskInstanceMap : taskListAsMap){
				historicTaskInstanceMap.put("startTime",DateUtil.convertDateToSTDString(historicTaskInstanceMap.get("startTime").toString()));
				if(historicTaskInstanceMap.get("endTime") != null){
					historicTaskInstanceMap.put("endTime",DateUtil.convertDateToSTDString(historicTaskInstanceMap.get("endTime").toString()));
				}
				/*if(historicTaskInstanceMap.get("assignee") == null){
					List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(historicTaskInstanceMap.get("id").toString());
					if(identityLinks!=null&&identityLinks.size()>0){
						IdentityLink identityLink =  identityLinks.get(0);
						if((identityLink != null) && (identityLink.getGroupId() != null) && (identityLink.getType().equalsIgnoreCase("candidate"))){
							//historicTaskInstanceMap.put("assignee", identityLink.getGroupId());
						}
					}					
				}*/
				if(historicTaskInstanceMap.containsKey("assigneeFullName")) {
					historicTaskInstanceMap.put("assignee",historicTaskInstanceMap.get("assigneeFullName"));
				}
				resolvedHistoricTaskInstanceListAsMap.add(historicTaskInstanceMap);
			}
		} 
		catch(ActivitiException ae){
			  throw new BpmException(I18nUtil.getMessageProperty("error.HistoricTask"));    					
		}
		return resolvedHistoricTaskInstanceListAsMap;
	}
	
	@Autowired
	public void setFormDefinitionService(FormDefinitionService formDefintionService) {
		this.formDefintionService = formDefintionService;
	}	

	@Autowired
	public void setTaskService(org.activiti.engine.TaskService taskService) {
		this.taskService = taskService;
	}

	@Autowired
	public void setTaskDefinitionService(TaskDefinitionService taskDefinitionService) {
		this.taskDefinitionService = taskDefinitionService;
	}

	@Autowired
	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	@Autowired
	public void setRtProcessService(
			com.eazytec.bpm.runtime.process.service.ProcessService rtProcessService) {
		this.rtProcessService = rtProcessService;
	}
}
