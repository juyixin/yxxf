
package com.eazytec.bpm.runtime.task.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.OperatingFunction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.department.service.DepartmentService;
import com.eazytec.bpm.admin.group.service.GroupService;
import com.eazytec.bpm.admin.notification.service.NotificationService;
import com.eazytec.bpm.admin.role.service.RoleService;
import com.eazytec.bpm.common.util.FormDefinitionUtil;
import com.eazytec.bpm.common.util.I18nUtil;
import com.eazytec.bpm.engine.TaskRole;
import com.eazytec.bpm.engine.TransactorType;
import com.eazytec.bpm.engine.task.ReferenceRelation;
import com.eazytec.bpm.exceptions.MembersAlreadyAddedException;
import com.eazytec.bpm.metadata.process.service.ProcessDefinitionService;
import com.eazytec.bpm.metadata.task.service.TaskDefinitionService;
import com.eazytec.bpm.runtime.agency.service.AgencyService;
import com.eazytec.bpm.runtime.form.service.FormService;
import com.eazytec.bpm.runtime.process.service.ProcessService;
import com.eazytec.bpm.runtime.task.service.OperatingFunctionService;
import com.eazytec.bpm.runtime.task.service.TaskService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.FileUtil;
import com.eazytec.common.util.StringUtil;
import com.eazytec.dto.UserDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceException;
import com.eazytec.model.AgencySetting;
import com.eazytec.model.Department;
import com.eazytec.model.Notification;
import com.eazytec.model.OperatingFunctionAudit;
import com.eazytec.model.User;
import com.eazytec.service.UserManager;
import com.eazytec.service.UserService;
import com.eazytec.util.PropertyReader;

/**
 * <p></p>
 * @author madan
 * 
 */
@Service("operatingFunctionService")
public class OperatingFunctionServiceImpl implements OperatingFunctionService{	
	
	private org.activiti.engine.TaskService taskService;
	private ProcessService rtProcessService;
	private TaskService rtTaskService;
	private RuntimeService runtimeService;
	private UserService userService;
	private RoleService roleService;
	private GroupService groupService;
	private DepartmentService departmentService;
	private UserManager userManager;
	private TaskDefinitionService taskDefinitionService;
	private NotificationService notificationService;
	private ProcessDefinitionService processDefinitionService;
	private AgencyService agencyService;
    private FormService rtFormService;
    
	@Autowired
	public void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}
	@Autowired
	public void setTaskDefinitionService(TaskDefinitionService taskDefinitionService) {
		this.taskDefinitionService = taskDefinitionService;
	}
	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	@Autowired
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	@Autowired
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}
	@Autowired
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	@Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
	@Autowired
	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}
	
	@Autowired
	public void setProcessDefinitionService(
			ProcessDefinitionService processDefinitionService) {
		this.processDefinitionService = processDefinitionService;
	}

	@Autowired
	public void setAgencyService(AgencyService agencyService) {
		this.agencyService = agencyService;
	}

	@Autowired
	public void setRtFormService(FormService rtFormService) {
		this.rtFormService = rtFormService;
	}
	
	protected final Log log = LogFactory.getLog(getClass());
	
	//Adding coordinator
	public void add(String taskId, List<String> userIds, String identityLinkType,List<AgencySetting> agencySettings){
		if(userIds==null || userIds.isEmpty()){
			  throw new BpmException(I18nUtil.getMessageProperty("error.userMandatory"));    				
		}
		List<IdentityLink>identities = taskService.getIdentityLinksByUsers(taskId, userIds, identityLinkType);
		if(identities!=null && identities.size()>0){
			List<String> alreadyAddedUserIds = new ArrayList<String>();
			for (IdentityLink identityLink : identities) {
				alreadyAddedUserIds.add(identityLink.getUserId());
				
				if(TaskRole.ORGANIZER.getName().equalsIgnoreCase(identityLinkType)){
					//To avoid duplicate in identity link table
					if(userIds.contains(identityLink.getUserId())){
						userIds.remove(identityLink.getUserId());
					}
					
				}
			}
			
			log.debug("already added userIds ::::::::: " + alreadyAddedUserIds);
		}
		
		for (String userId : userIds) {
			taskService.operateOnIdentityLinks(taskId, userId, null, OperatingFunction.ADD, identityLinkType,null,agencySettings);
		}	
	}
	
	public void add(String taskId, String userIds, String identityLinkType,String addedOrganizerOrders,String processDefinitionId){
		List<String> users = StringUtil.convertStringArrayToList(StringUtil.getCommaSeparatedStrings(userIds));
		if(users==null || users.isEmpty()){
			  throw new BpmException(I18nUtil.getMessageProperty("error.userMandatory"));    				
		}
		List<IdentityLink>identities=taskService.getIdentityLinksForTask(taskId, identityLinkType);
		//List<IdentityLink>identities = taskService.getIdentityLinksByUsers(taskId, users, identityLinkType);
		List<String> alreadyAddedUserIds = new ArrayList<String>();
		if(identities!=null && identities.size()>0){
			Map<String,Integer> oldOrganizerDetail = new HashMap<String,Integer>();
			for (IdentityLink identityLink : identities) {
				//if(TaskRole.ORGANIZER.getName().equalsIgnoreCase(identityLinkType)){
					oldOrganizerDetail.put(identityLink.getUserId(),identityLink.getOrder());
					//To avoid duplicate in identity link table
					for(String user: users) {
					    if(user.trim().equals(identityLink.getUserId()))
					    	alreadyAddedUserIds.add(identityLink.getUserId());
					}
				//}
			}
			if(alreadyAddedUserIds.size()>0){
				log.debug("already added userIds ::::::::: " + alreadyAddedUserIds);
				throw new MembersAlreadyAddedException(taskId, identityLinkType, alreadyAddedUserIds);
			}
			List<AgencySetting> agencySettings = getAgencySettingByProcessDefinitionId(processDefinitionId);
			String[] organizers  = StringUtil.getCommaSeparatedStrings(userIds);
			List<Map<String, Object>> organizerProperty;
			try {
				organizerProperty = CommonUtil.convertJsonToList(addedOrganizerOrders);
				TaskEntity historicTaskInstance=(TaskEntity) rtTaskService.getTask(taskId);
				int signofftype=historicTaskInstance.getSignOffType();
				int order=1;
				if(signofftype == 3){
					 User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					 order =oldOrganizerDetail.get(user.getUsername());
					 //adding the organizer
					 for(int i=0;i<organizers.length;i++){
						  Map<String, Object> organizerPropertyMap =  organizerProperty.get(i);
						  order= order + Integer.parseInt((String)organizerPropertyMap.get("order"));
						  taskService.operateOnIdentityLinks(taskId, organizers[i], null, OperatingFunction.ADD, identityLinkType, null,order, agencySettings);
					}
					 
					 ValueComparator bvc =  new ValueComparator(oldOrganizerDetail);
				     TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
				     sorted_map.putAll(oldOrganizerDetail);
					Iterator it = sorted_map.entrySet().iterator();
					//Ordering existing user and inserting organizer by same ordering with updated order
				    while (it.hasNext()) {
				        Map.Entry pairs = (Map.Entry)it.next();
				        if(!pairs.getKey().equals(user.getUsername())){
					        order=order+ 1;//(Integer.parseInt(pairs.getValue().toString()) - currentOrganizerOrder);
					        taskService.deleteUserIdentityLink(taskId, pairs.getKey().toString(), identityLinkType);
					        taskService.operateOnIdentityLinks(taskId, pairs.getKey().toString(), null, OperatingFunction.ADD, identityLinkType, null,order, agencySettings); 
				        }
				    }
					
				}else{// other than multi sequence. just adding.
					for(int i=0;i<organizers.length;i++){
						  taskService.operateOnIdentityLinks(taskId, organizers[i], null, OperatingFunction.ADD, identityLinkType, null,order, agencySettings);
					}
				}
			} catch (JSONException e) {
				  throw new BpmException(I18nUtil.getMessageProperty("errors.jsonException")+" Task Id : "+taskId);				
			}
		}else{
			String[] organizers  = StringUtil.getCommaSeparatedStrings(userIds);
			for(int i=0;i<organizers.length;i++){
				  taskService.operateOnIdentityLinks(taskId, organizers[i], null, OperatingFunction.ADD, identityLinkType, null,0, getAgencySettingByProcessDefinitionId(processDefinitionId));
			}
		}
	}

	public void transfer(String taskId, String userId, String processDefinitionId){
		if(StringUtil.isEmptyString(userId)){
			  throw new BpmException(I18nUtil.getMessageProperty("error.userMandatory"));    				
		}
		taskService.operateOnIdentityLinks(taskId, userId, null, OperatingFunction.TRANSFER, IdentityLinkType.ORGANIZER, null, getAgencySettingByProcessDefinitionId(processDefinitionId));
	}
	
	public void confluentSignature(String taskId, List<String> userIds,String processDefinitionId) throws JSONException{
		if(userIds == null || userIds.isEmpty()){
			  throw new BpmException(I18nUtil.getMessageProperty("error.userMandatory"));    				
		}
		taskService.operateOnIdentityLinks(taskId, userIds, null, OperatingFunction.CONFLUENT_SIGNATURE, IdentityLinkType.ORGANIZER, null, getAgencySettingByProcessDefinitionId(processDefinitionId));
	}
	
	public void circulatePerusal(String taskId,  List<String> userIds,String processDefinitionId){
		if(userIds==null || userIds.isEmpty()){
			  throw new BpmException(I18nUtil.getMessageProperty("error.userMandatory"));    				
		}
//		if(StringUtil.isEmptyString(userId)){
//			throw new BpmException("Circulate Perusal not possible for empty user");
//		}
//		operateIdentityLinks(taskId, userId, OperatingFunction.CIRCULATE_PERUSAL, IdentityLinkType.READER);
//	}
//	
//	
		/*
		 *  Allow MultiUser Access to circulatePerusal
		 */
		String identityLinkType = TaskRole.ORGANIZER.getName();
		List<IdentityLink>identities = taskService.getIdentityLinksByUsers(taskId, userIds, identityLinkType);
		if(identities!=null && identities.size()>0){
			List<String> alreadyAddedUserIds = new ArrayList<String>();
			for (IdentityLink identityLink : identities) {
				alreadyAddedUserIds.add(identityLink.getUserId());
			}
			log.debug("already added userIds ::::::::: " + alreadyAddedUserIds);
		}
		taskService.operateOnIdentityLinks(taskId, userIds, null, OperatingFunction.CIRCULATE_PERUSAL, IdentityLinkType.READER, null, getAgencySettingByProcessDefinitionId(processDefinitionId));
	}
		
	public void replaceTransactor(String taskId, String userId,String processDefinitionId){
		if(StringUtil.isEmptyString(userId)){
			  throw new BpmException(I18nUtil.getMessageProperty("error.userMandatory"));    				
		}
		taskService.operateOnIdentityLinks(taskId, userId, null, OperatingFunction.TRANSACTOR_REPLACEMENT, IdentityLinkType.ORGANIZER, null, getAgencySettingByProcessDefinitionId(processDefinitionId));
	}
	
	public List<AgencySetting> getAgencySettingByProcessDefinitionId(String processDefinitionId){
		ProcessDefinition processDefinition= processDefinitionService.getProcessDefinitionById(processDefinitionId); 
		return agencyService.getAgencySettingForUserByProcessId(processDefinition.getName());
	}
	
	public void suspend(String processInstanceId,String userId,String taskId,String resourceName, String processDefinitionId){
		rtProcessService.suspendProcessInstanceById(processInstanceId);
		OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(userId,taskId,resourceName,OperatingFunction.SUSPEND,processInstanceId, processDefinitionId);
		processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
	} 
	
    public void terminate(String executionId){
        Date date = new Date();
        rtProcessService.terminateExecutionByInstanceId(executionId,date);
    }   
   
    public void terminateTasks(List<Map<String,Object>> taskDetailsMaps){
        Date date = new Date();
        List<String> processInstanceIds = new ArrayList<String>();
        for(Map<String,Object> taskDetailsMap : taskDetailsMaps){
        	processInstanceIds.add(taskDetailsMap.get("executionId").toString());
        }
        rtProcessService.terminateExecutionsByInstanceIds(processInstanceIds,date);
    }   
    
    public void withdraw(String processInstanceId){
    	runtimeService.withdrawProcessInstanceById(processInstanceId); 
    }
    
    public void delete(String processInstanceId,String taskStatus)throws ActivitiException{
    	if(taskStatus.equals(OperatingFunction.TERMINATE)){
        	runtimeService.deleteProcessInstanceOnly(processInstanceId); 
    	}else {
    		runtimeService.deleteProcessInstance(processInstanceId, OperatingFunction.DELETE);
    	}
   }

   public TaskEntity setJointConductionByOrder(String processInstanceId, String nextTaskOrganizers, String nextTaskCoordinators,String nextOrganizerOrders,
		   boolean isStartProcess, List<Map<String, Object>> referenceRelation, boolean sendMail, boolean sendInternalMessage) throws JSONException{		 
		 log.debug("Setting joint conduction-------------------");
		TaskEntity nextTask = rtTaskService.getNextTaskForProcessInstance(processInstanceId);
		  
		  String urgeMessage = replaceTextInUrgeMessage(Constants.DEFAULT_URGE_MESSAGE, "", nextTask.getId(),"",true);
		  if(sendMail) {
			  sendNotification(nextTaskOrganizers,urgeMessage, Constants.NOTIFICATION_EMAIL_TYPE , Constants.NOTIFICATION_MESSAGE);
		  }
		  if(sendInternalMessage) {
			  sendNotification(nextTaskOrganizers,urgeMessage, Constants.NOTIFICATION_INTERNALMESSAGE_TYPE , Constants.NOTIFICATION_MESSAGE);
		  }		
		if(nextTask!=null){
			rtProcessService.setTaskDefinition(nextTask);
			String nextTaskId = nextTask.getId();
			log.debug("joint conduction set for -------------------"+nextTaskId);
			List<AgencySetting> agencySettings =  getAgencySettingByProcessDefinitionId(nextTask.getProcessDefinitionId());
			if(!StringUtil.isEmptyString(nextTaskOrganizers)){
				 String[] organizers  = StringUtil.getCommaSeparatedStrings(nextTaskOrganizers);
				// String[] organizerOrders  = StringUtil.getCommaSeparatedStrings(nextOrganizerOrders);
				// Added superior subordinate functionality
				// String[] superiors  = StringUtil.getCommaSeparatedStrings(organizerSuperior);
				 List<Map<String,Object>> organizerProperty = CommonUtil.convertJsonToList(nextOrganizerOrders);
				// String[] subordinates  = StringUtil.getCommaSeparatedStrings(organizerSubordinate);
				// List<String> organizerList = StringUtil.convertStringArrayToList(organizers);
/*				 List<User> users = (List<User>) userService.getUserByUsernames(organizerList);
				 Map<String,User> userMap = new HashMap<String,User>();
				 for(User user : users){
					 userMap.put(user.getUsername(), user);
				 }*/
				 String operateFunction = OperatingFunction.ADDORDER;
				 String identityLinkType = IdentityLinkType.ORGANIZER;	
				 List<Map<String,String>> organizerDetails = new ArrayList<Map<String,String>>();
				 Map<String,String> organizerDetailMap = null;
				
				// Added superior subordinate functionality
				 for(int i=0;i<organizers.length;i++){
//					 User user = userMap.get(organizers[i]);
					  organizerDetailMap = new HashMap<String,String>();
					  organizerDetailMap.put("userId", organizers[i]);
/*					  if(user.getEmail()!=null && !user.getEmail().isEmpty()){
						  organizerDetailMap.put("mailId", user.getEmail());
					  }else{
						  organizerDetailMap.put("mailId", null);
					  }
					  String sendMailNotification;
					  if(user.isSendEmailNotification()){
						  sendMailNotification = "true";
					  }else{
						  sendMailNotification = "false";
					  }
					  organizerDetailMap.put("isSendEmailNotification",sendMailNotification);*/
					  
					  
					  Map<String, Object> organizerPropertyMap =  organizerProperty.get(i);
					  organizerDetailMap.put("order", (String) organizerPropertyMap.get("order"));
					  
/*					  if(organizerPropertyMap.containsKey("superior") && organizerPropertyMap.get("superior")!=null && !organizerPropertyMap.get("superior").toString().isEmpty() && organizerPropertyMap.get("superior").toString().equalsIgnoreCase("true")){
						  organizerDetailMap.put("superior", user.getManager());
					  }else{
						  organizerDetailMap.put("superior", null);
					  }
					  if(organizerPropertyMap.containsKey("subordinate") && organizerPropertyMap.get("subordinate")!=null  && !organizerPropertyMap.get("subordinate").toString().isEmpty()&& organizerPropertyMap.get("subordinate").toString().equalsIgnoreCase("true")){
						  organizerDetailMap.put("subordinate", user.getSecretary());
					  }else{
						  organizerDetailMap.put("subordinate", null);
					  }*/
					  organizerDetails.add(organizerDetailMap);
				 }
				 if(!isStartProcess){
					 boolean isSkipRepeat = false;
				     Expression skipRepeatedExpr =   nextTask.getTaskDefinition().getSkipRepeatedExpression();
					  // if it is skip repeat processed user removed form organizer if they present
					  if(skipRepeatedExpr != null && skipRepeatedExpr.toString().equalsIgnoreCase("true")){
						  isSkipRepeat = true;
					  }
					  if(isSkipRepeat){
						  String previousAssignee = runtimeService.getLastTaskAssignee(processInstanceId,nextTask.getTaskDefinitionKey());
						  for(Map<String,String> organizerDetail : organizerDetails) {
							  if(organizerDetail.get("userId").equalsIgnoreCase(previousAssignee)){
								  if(organizerDetails.size() > 1){
									  organizerDetails.remove(organizerDetail);  
								  }
								  break;
							  }
						  }
					  }
				 }
				
					//AutomatiRemainder Notification
					// setAutomaticRemainderValue(nextTask,organizerDetails,request);
				 Set<String> userInfo = taskService.operateIdentityLinksByOrder(nextTaskId,organizerDetails,null,operateFunction,identityLinkType,null,agencySettings,processInstanceId);
				// setReferenceRelation(referenceRelation,organizerDetails,nextTask,userInfo);
			 }
			  if(!StringUtil.isEmptyString(nextTaskCoordinators)){
				  List<String>coordinatorIds = StringUtil.convertStringArrayToList(StringUtil.getCommaSeparatedStrings(nextTaskCoordinators));
				  add(nextTaskId, coordinatorIds, IdentityLinkType.COORDINATOR,agencySettings);
			  }
			  //Set Processed-User for Start Process
			  /*if(isStartProcess){
				  runtimeService.addProcessedUser(nextTask.getId(), CommonUtil.getLoggedInUserId());
			  }*/
		}else{
			log.info("No next task found for Instance : "+processInstanceId);
		}
		return nextTask;
	}
   
	public void setDynamicReaderValue(TaskEntity taskEntity, Map<String, String> rtFormValues, String proInsId, Map<String, String> nodeProperties) throws BpmException {
		rtProcessService.setTaskDefinition(taskEntity);
		String dynamicNextRedField = null;
		String dynamicNextRedType = null;
		String nextDynamicReader = null;
		TaskEntity nextTaskEntity = null;
		List<String> nextDynamicReadFieldValues = new ArrayList<String>();
		String formName = taskEntity.getTaskDefinition().getFormName().toString().toLowerCase().replaceAll(" ", "");

		if (taskEntity.getTaskDefinition().getDynamicReader() != null || nodeProperties != null) {
			if (nodeProperties == null) {
				dynamicNextRedField = taskEntity.getTaskDefinition().getDynamicReader().toString();
				if (taskEntity.getTaskDefinition().getDynamicReaderType() != null) {
					dynamicNextRedType = taskEntity.getTaskDefinition().getDynamicReaderType().toString();
				} else {
					dynamicNextRedType = "userlist";
				}
				// get next task entity

				nextTaskEntity = rtTaskService.getNextTaskForProcessInstance(proInsId);

			} else {
				dynamicNextRedField = nodeProperties.get("dynamicReader");
				dynamicNextRedType = nodeProperties.get("dynamicReaderType");
				formName = nodeProperties.get("formName").toLowerCase().replaceAll(" ", "");
				nextTaskEntity = taskEntity;
			}
			if (dynamicNextRedField != null) {
				String fieldName = dynamicNextRedField.replaceAll(formName + "_", "");
				nextDynamicReader = rtFormValues.get(fieldName);
			}

			if (nextDynamicReader != null) {
				String[] userListArray = nextDynamicReader.split(",");
				for (String user : userListArray) {
					nextDynamicReadFieldValues.add(user);
				}
			}
			if (!nextDynamicReadFieldValues.isEmpty() && nextTaskEntity != null) {

				if (dynamicNextRedType.equalsIgnoreCase("rolelist")) {
					List<User> users = userManager.getUserByRoleIds(nextDynamicReadFieldValues);
					if (users != null) {
						for (User user : users) {
							runtimeService.addCandidateReader(nextTaskEntity.getId(), user.getId());
						}
					}
				} else if (dynamicNextRedType.equalsIgnoreCase("grouplist")) {
					List<User> users = userManager.geUserByGroupIds(nextDynamicReadFieldValues);
					if (users != null) {
						for (User user : users) {
							runtimeService.addCandidateReader(nextTaskEntity.getId(), user.getId());
						}
					}
				} else if (dynamicNextRedType.equalsIgnoreCase("departmentlist")) {
					List<User> users = userManager.getUsersByDepartmentIds(nextDynamicReadFieldValues);
					if (users != null) {
						for (User user : users) {
							runtimeService.addCandidateReader(nextTaskEntity.getId(), user.getId());
						}
					}
				} else {
					List<String> exitUserIds = userManager.getExistUserIds(nextDynamicReadFieldValues);
					if (exitUserIds != null && !exitUserIds.isEmpty()) {
						for (String userId : exitUserIds) {
							if (nextDynamicReadFieldValues.contains(userId)) {
								runtimeService.addCandidateReader(nextTaskEntity.getId(), userId);
							}
						}
					}
				}

			}
		}
	}

	/**
	 * 从表单map数据中，获取任务动态办理人
	 */
	public List<Map<String, String>> getDynamicOrganizerValue(List<Map<String, String>> organizerList, Map<String, Object> rtFormValues, TaskDefinition taskDefinition, boolean isStartForm, List<String> transactorIds) throws BpmException {
		String dynamicNextOrgFiled = null;
		String dynamicNextOrgType = null;
		String nextDynamicUser = null;
		int organizerListSize = organizerList.size();
		List<String> nextDynamicOrgFiledValues = new ArrayList<String>();
		String formName = null;
		// isStartForm will be true for start node form
		// Get dynamic organizer field name
		if (!isStartForm) {
			String taskId = (String) rtFormValues.get("taskId");
			TaskEntity taskEntity = (TaskEntity) taskDefinitionService.getTaskById(taskId);
			taskDefinition = rtTaskService.setTaskDefinition(taskEntity).getTaskDefinition();
		}
		if (taskDefinition.getDynamicOrganizer() != null || taskDefinition.getStartNodeDynamicOrganizer() != null) {

			if (taskDefinition.getStartNodeDynamicOrganizer() != null && isStartForm) {
				formName = taskDefinition.getStartFormName().toString().toLowerCase().replaceAll(" ", "");
				dynamicNextOrgFiled = taskDefinition.getStartNodeDynamicOrganizer().toString();
				if (taskDefinition.getDynamicOrganizerType() != null) {
					dynamicNextOrgType = taskDefinition.getStartNodedynamicOrganizerType().toString();
				} else {
					dynamicNextOrgType = "userlist";
				}
			} else {

				formName = taskDefinition.getFormName().toString().toLowerCase().replaceAll(" ", "");
				dynamicNextOrgFiled = taskDefinition.getDynamicOrganizer().toString();
				if (taskDefinition.getDynamicOrganizerType() != null) {
					dynamicNextOrgType = taskDefinition.getDynamicOrganizerType().toString();
				} else {
					dynamicNextOrgType = "userlist";
				}
			}

			// Separate filed name form form name and get values from from
			if (dynamicNextOrgFiled != null) {
				String fieldName = dynamicNextOrgFiled.replaceAll(formName + "_", "");
				if (rtFormValues.get(fieldName) != null) {
					nextDynamicUser = rtFormValues.get(fieldName).toString();
				}
			}
			// get the filed values from form
			if (nextDynamicUser != null) {
				String[] userListArray = nextDynamicUser.split(",");
				for (String user : userListArray) {
					nextDynamicOrgFiledValues.add(user);
				}
			}
			if (!nextDynamicOrgFiledValues.isEmpty()) {

				if (dynamicNextOrgType.equalsIgnoreCase("rolelist")) {
					List<User> users = roleService.getUsersByRoleIds(nextDynamicOrgFiledValues);
					if (users != null) {
						for (User user : users) {
							Map<String, String> organizerMap = new HashMap<String, String>();
							++organizerListSize;
							organizerMap.put("organizer", user.getId());
							transactorIds.add(user.getId());
							// Added superior subordinate functionality
							organizerMap.put("organizerProperty", Integer.toString(organizerListSize) + "-false-false");
							organizerList.add(organizerMap);
						}
					}
				} else if (dynamicNextOrgType.equalsIgnoreCase("grouplist")) {
					List<User> users = groupService.getUsersByGroupIds(nextDynamicOrgFiledValues);
					if (users != null) {
						for (User user : users) {
							Map<String, String> organizerMap = new HashMap<String, String>();
							++organizerListSize;
							organizerMap.put("organizer", user.getId());
							transactorIds.add(user.getId());
							// Added superior subordinate functionality
							organizerMap.put("organizerProperty", Integer.toString(organizerListSize) + "-false-false");
							organizerList.add(organizerMap);
						}
					}

				} else if (dynamicNextOrgType.equalsIgnoreCase("departmentlist")) {
					List<User> users = userManager.getUsersByDepartmentIds(nextDynamicOrgFiledValues);
					if (users != null) {
						for (User user : users) {
							Map<String, String> organizerMap = new HashMap<String, String>();
							++organizerListSize;
							organizerMap.put("organizer", user.getId());
							transactorIds.add(user.getId());
							// Added superior subordinate functionality
							organizerMap.put("organizerProperty", Integer.toString(organizerListSize) + "-false-false");
							organizerList.add(organizerMap);
						}
					}
				} else {
					List<String> exitUserIds = userManager.getExistUserIds(nextDynamicOrgFiledValues);
					transactorIds.addAll(exitUserIds);
					for (String userId : exitUserIds) {
						Map<String, String> organizerMap = new HashMap<String, String>();
						++organizerListSize;
						organizerMap.put("organizer", userId);
						// Added superior subordinate functionality
						organizerMap.put("organizerProperty", Integer.toString(organizerListSize) + "-false-false");
						organizerList.add(organizerMap);
					}
				}

			}
		}
		return organizerList;
	}
   
   /**
    * Get the user ids for given department ids
    */
	public String getUserIdForDepartment(String depIds) {
		String userIds = "";
		List<String> depIdList = new ArrayList<String>();
		String[] depListArray = depIds.split(",");
		for (String dep : depListArray) {
			depIdList.add(dep);
		}
		List<String> depUsers = departmentService.getUsersByDepartmentIds(depIdList);
		if (depUsers != null) {
			for (String user : depUsers) {
				if (!userIds.isEmpty()) {
					userIds = userIds + "," + user;
				} else {
					userIds = user;
				}
			}
		}

		return userIds;
	}
   
	/**
	 * This method gets the next task node and all those reference relation
	 *  or potential organizers which is set in that task node
	 */
	public List<Map<String,String>> getOrganizersForCurrentTask(Map<String, Object> rtFormValues){
		try{
			//获取当前任务节点的下一个任务节点信息
			TaskDefinition  taskDefinition = (TaskDefinition)runtimeService.submitTaskFormData(rtFormValues);
			List<Map<String,String>> newOrganizerList = new ArrayList<Map<String,String>>();
			if(taskDefinition != null){
				List<Map<String,String>> organizerList = new ArrayList<Map<String,String>>();
				Map<String,String> organizerMap = null;
				List<String> orgIds = new ArrayList<String>();
				List<String> transactorIds = new ArrayList<String>();
				//获得任务候选人
				Set<Expression> organizersSet = taskDefinition.getCandidateUserIdExpressions();
				//获取任务候选组，此处包括用户组、部门、角色
				organizersSet.addAll(taskDefinition.getCandidateGroupIdExpressions());

				String taskId =  rtFormValues.get("taskId").toString();
				
				if(taskDefinition.getReferenceRelation() != null && !taskDefinition.getReferenceRelation().isEmpty()) {
					getOrganizerFromReferenceRelation(organizerList,taskId,transactorIds,taskDefinition,CommonUtil.getLoggedInUserId());
				}
				
				if(organizersSet.size() > 0){
					//把organizersSet候选人、组表达式转换成organizerList候选人列表
					getOrganizerList(organizersSet,organizerList,taskDefinition,orgIds);
					
					
					organizerList.addAll(getDynamicOrganizerValue(organizerList,rtFormValues, taskDefinition, Boolean.valueOf((String)rtFormValues.get("isStartForm")),transactorIds));
					//Get existing user
					List<String> userList = userManager.getExistUserIds(orgIds);
					userList.addAll(transactorIds);
					if(userList.size() == 0){
						organizerMap = new HashMap<String,String>();
						organizerMap.put("isNextTaskExist","true");
						newOrganizerList.add(organizerMap);
					}else{
						for(Map<String,String> orgMap : organizerList){
							if(userList.contains(orgMap.get("organizer"))){
								newOrganizerList.add(orgMap);
							}
						}
					}
				}else {
					organizerMap = new HashMap<String,String>();
					organizerMap.put("isNextTaskExist","true");
					organizerMap.put("nodeType", String.valueOf(taskDefinition.getNodeTypeExpression()));
					newOrganizerList.addAll(getDynamicOrganizerValue(organizerList,rtFormValues, taskDefinition, Boolean.valueOf((String)rtFormValues.get("isStartForm")) ,transactorIds));
					newOrganizerList.add(organizerMap);
				}
			}
			return newOrganizerList;
		}catch(DataSourceException e){
			  throw new BpmException(I18nUtil.getMessageProperty("error.gettingPotentialOrganizer"));    				
		}catch(ActivitiException e){
			  throw new BpmException(I18nUtil.getMessageProperty("error.gettingPotentialOrganizer"));    				
		}
				
	}
	
	/**
	 * 从任务的引用关系中，获取任务办理人
	 * 引用关系在流程定义的箭头上配置
	 * @param organizerList
	 * @param taskId
	 * @param transactorIds
	 * @param taskDefinition
	 * @param organizerId
	 */
	public void getOrganizerFromReferenceRelation(List<Map<String, String>> organizerList,String taskId,List<String> transactorIds,TaskDefinition taskDefinition,String organizerId) {
		Set<String> referenceRelationSet = new HashSet<String>();
		String creatorId = null;
		IdentityLinkEntity creatorIdentiLink = null;
		if(taskId != null) {
		   // getting creator of task 
		   List<IdentityLinkEntity> creatorLink = runtimeService.getCreatorIdentityLinkForTaskId(taskId);
		   creatorIdentiLink = creatorLink.get(0);
		   creatorId = creatorIdentiLink.getUserId();
	   } else {
		   // set logged in user as creator (while starting process)
		   creatorId = organizerId;
	   }
		referenceRelationSet = setReferenceRelationInProcess(taskDefinition,taskId,transactorIds,creatorId,organizerId);
		if(!referenceRelationSet.isEmpty()) {
			organizerList = getTransactorsOrganizerList(taskDefinition,referenceRelationSet,organizerList);
		}
	}
	
/**
	 * 
	 * @param taskDefinition
	 * @param referenceRelation
	 * @param organizerList
	 * @return
	 */
	public List<Map<String, String>> getTransactorsOrganizerList(TaskDefinition taskDefinition,Set<String> referenceRelation,List<Map<String,String>> organizerList) {
		Map<String,String> referenceRelationMap = null;
		if(!referenceRelation.isEmpty()) {
			int organizerOrder = 1;
			for(String organizer : referenceRelation) {
				referenceRelationMap = new HashMap<String,String>();
				referenceRelationMap.put("nodeType", String.valueOf(taskDefinition.getNodeTypeExpression()));
				referenceRelationMap.put("organizer", organizer);
				//orgIds.add(organizer);
				referenceRelationMap.put("organizerProperty", organizerOrder+"-false-false");
				organizerList.add(referenceRelationMap);
				organizerOrder++;
			}
		}
			return organizerList;
	}
	/**
	 * 
	 * @param taskId
	 * @param activityId
	 * @return
	 * @throws DataSourceException
	 */
	public List<Map<String,String>> getOrganizersForJumpTask(String taskId,String activityId,TaskDefinition taskDefinition)throws DataSourceException{
		try{
			if(taskDefinition == null){
				taskDefinition = (TaskDefinition)runtimeService.taskJump(taskId,activityId,true,Constants.EMPTY_STRING);
			}			
			List<Map<String,String>> organizerList = new ArrayList<Map<String,String>>();
			Map<String,String> organizerMap = null;
			if(taskDefinition != null){
				Set<Expression> organizersSet = taskDefinition.getCandidateUserIdExpressions();
				organizersSet.addAll(taskDefinition.getCandidateGroupIdExpressions());
				List<String> orgIds = new ArrayList<String>();
				List<String> transactorIds = new ArrayList<String>();
				// getting transactors for process
				if(taskDefinition.getReferenceRelation() != null) {
/*					referenceRelation = setReferenceRelationInProcess(taskDefinition.getReferenceRelation(),taskId,transactorIds);
					if(!referenceRelation.isEmpty()) {
						organizerList = getTransactorsOrganizerList(taskDefinition,referenceRelation,organizerList);
					}*/
					getOrganizerFromReferenceRelation(organizerList,taskId,transactorIds,taskDefinition,CommonUtil.getLoggedInUserId());
				}
				if(organizersSet.size() > 0){
					getOrganizerList(organizersSet,organizerList,taskDefinition,orgIds);
					/*for(Expression organizer : organizersSet){
						organizerMap = new HashMap<String,String>();
						organizerMap.put("nodeType", String.valueOf(taskDefinition.getNodeTypeExpression()));
						organizerMap.put("organizer",organizer.getExpressionText().split("-")[0]);
						// Added superior subordinate functionality 
						organizerMap.put("organizerProperty",organizer.getExpressionText().split("-")[1]+"-"+organizer.getExpressionText().split("-")[2]+"-"+organizer.getExpressionText().split("-")[3]);
						organizerList.add(organizerMap);
					}*/
				}else {
					organizerMap = new HashMap<String,String>();
					organizerMap.put("isNextTaskExist","true");
					organizerMap.put("nodeType", String.valueOf(taskDefinition.getNodeTypeExpression()));
					organizerList.add(organizerMap);
				}
			}else {
				  throw new BpmException(I18nUtil.getMessageProperty("error.gettingPotentialOrganizer"));    				
			}
			return organizerList;
		}catch(ActivitiException e){
			  throw new BpmException(I18nUtil.getMessageProperty("error.gettingPotentialOrganizer"));    				
		}
	}
   	
	/**
	 * 
	 * @param processDefinitionId
	 * @param formValues
	 * @return
	 * @throws DataSourceException
	 */
	public List<Map<String,String>> getOrganizersForProcessStart(String processDefinitionId,Map<String, Object> formValues){
		try{
			Object object = runtimeService.getOrganizersForProcessStart(processDefinitionId,formValues);
			List<Map<String,String>> newOrganizerList = new ArrayList<Map<String,String>>();
			Map<String,String> organizerMap = new HashMap<String,String>();
			Set<String> referenceRelation = new HashSet<String>();
			if(object instanceof TaskDefinition){
				TaskDefinition  taskDefinition = (TaskDefinition)object;
				if(taskDefinition != null){
					List<Map<String,String>> organizerList = new ArrayList<Map<String,String>>();
					List<String> transactorIds = new ArrayList<String>();
					List<String> orgIds = new ArrayList<String>();
					Set<Expression> organizersSet = taskDefinition.getCandidateUserIdExpressions();
					organizersSet.addAll(taskDefinition.getCandidateGroupIdExpressions());
					// getting transactors for process
					if(taskDefinition.getReferenceRelation() != null) {
/*						referenceRelation = setReferenceRelationInProcess(taskDefinition.getReferenceRelation(),null,transactorIds);
						if(!referenceRelation.isEmpty()) {
							organizerList = getTransactorsOrganizerList(taskDefinition,referenceRelation,organizerList);
						}*/
						getOrganizerFromReferenceRelation(organizerList,null,transactorIds,taskDefinition,CommonUtil.getLoggedInUserId());
					}
					// add dynamic organizer with potential organizer
					if(formValues!=null){
						organizerList.addAll(getDynamicOrganizerValue(organizerList,formValues, taskDefinition, true,transactorIds));
					}
					if(organizersSet.size() > 0){
						getOrganizerList(organizersSet,organizerList,taskDefinition,orgIds);
						//Get existing user
						List<String> userList = userManager.getExistUserIds(orgIds);
						userList.addAll(transactorIds);
						if(userList.size() == 0){
							organizerMap = new HashMap<String,String>();
							organizerMap.put("isNextTaskExist","true");
							newOrganizerList.add(organizerMap);
						}else{
							for(Map<String,String> orgMap : organizerList){
								if(userList.contains(orgMap.get("organizer"))){
									newOrganizerList.add(orgMap);
								}
							}
						}
					}else {
						organizerMap.put("isNextTaskExist","true");
						organizerMap.put("nodeType", String.valueOf(taskDefinition.getNodeTypeExpression()));
						if(!organizerList.isEmpty() && organizerList != null) {
								for(Map<String,String> orgMap : organizerList){
										newOrganizerList.add(orgMap);
								}
						}
						newOrganizerList.add(organizerMap);
					}
				}
			}else if(object instanceof Boolean){
				organizerMap.put("nodeWithForm",object.toString());
				newOrganizerList.add(organizerMap);
			}
			return newOrganizerList;
		}catch(ActivitiException e){
			  throw new BpmException(I18nUtil.getMessageProperty("error.gettingPotentialOrganizer"));    				
		}
				
	}
	
	/**
	 * 把organizersSet候选人、组表达式转换成organizerList候选人列表
	 * @param organizersSet
	 * @param organizerList
	 * @param taskDefinition
	 * @param orgIds
	 * @return
	 */
	private List<Map<String,String>> getOrganizerList(Set<Expression> organizersSet,List<Map<String,String>> organizerList,TaskDefinition taskDefinition,List<String> orgIds){
		Map<String,String> organizerMap = new HashMap<String,String>();

		List<String> userIds = null;
		int order = 1;
		if(organizersSet.size() > 0){
			for(Expression organizer : organizersSet){
				String[] organizerDetail=organizer.getExpressionText().split("-");
				if(organizerDetail.length > 4){
					String type = organizerDetail[4];
					userIds = new ArrayList<String>();
					if(type.equalsIgnoreCase("roles")){
						userIds = roleService.getUserNamesByRoleId(organizer.getExpressionText().split("-")[0]);	
					}else if(type.equalsIgnoreCase("groups")){
					    userIds = groupService.getUserNamesByGroupId(organizer.getExpressionText().split("-")[0]);	
					}else if(type.equalsIgnoreCase("departments")){
					    userIds = departmentService.getUserNamesByDepartmentId(organizer.getExpressionText().split("-")[0]);	
					}
				    for (String userId : userIds) {
				    	organizerMap = new HashMap<String,String>();
				    	organizerMap.put("nodeType", String.valueOf(taskDefinition.getNodeTypeExpression()));
						organizerMap.put("organizer",userId);
						orgIds.add(userId);
						organizerMap.put("organizerProperty",order+"-"+organizer.getExpressionText().split("-")[2]+"-"+organizer.getExpressionText().split("-")[3]);
						organizerList.add(organizerMap);
						order = order + 1;
					}
				}else{
					organizerMap = new HashMap<String,String>();
					organizerMap.put("nodeType", String.valueOf(taskDefinition.getNodeTypeExpression()));
					organizerMap.put("organizer",organizer.getExpressionText().split("-")[0]);
					orgIds.add(organizer.getExpressionText().split("-")[0]);
					organizerMap.put("organizerProperty",order+"-"+organizer.getExpressionText().split("-")[2]+"-"+organizer.getExpressionText().split("-")[3]);
					organizerList.add(organizerMap);
					order = order + 1;
				}
			}
		}
		return organizerList;
	}

	/**
	 * Set relation reference to the task which is set in the process flow
	 * @param taskDefinition
	 * @param taskId
	 * @return
	 */
	public Set<String> setReferenceRelationInProcess(TaskDefinition taskDefinition,String taskId,List<String> transactorIds,String creatorId,String orgId) {
		  log.info("Getting potential organizers for task : "+taskDefinition.getNameExpression());
		   Set<String> endUserList = new HashSet<String>();
		   try{
			   IdentityLinkEntity creatorIdentiLink = null;
			   List<Map<String, Object>> referenceRelation = taskDefinition.getReferenceRelation();
		   if(referenceRelation != null){
			   if(taskId != null) {
				   // getting creator of task 
				   List<IdentityLinkEntity> creatorLink = runtimeService.getCreatorIdentityLinkForTaskId(taskId);
				   creatorIdentiLink = creatorLink.get(0);
				   creatorId = creatorIdentiLink.getUserId();
			   } else {
				   // set logged in user as creator (while starting process)
				   creatorId = orgId;
			   }
			   int referenceType = 0;
			   boolean isCreator = false;
			   boolean isOrganizer = false;
			   String opertaion = BpmnParse.REFERENCE_RELATION_OPERATION_OR;
			   Set<String> endUserListSet = new HashSet<String>();
			   boolean isNullUserType = false;
			   
			   for(Map<String,Object> refRelation : referenceRelation){
				   String userType = (String) refRelation.get(BpmnParse.REFERENCE_RELATION_USER);
				   String reference = (String) refRelation.get(BpmnParse.REFERENCE_RELATION_TYPE);
				   referenceType = Integer.parseInt(reference);			  
				   List<String> userList = new ArrayList<String>();		
				   if(userType.equalsIgnoreCase(BpmnParse.CREATOR)){
					   isCreator = true;
					   //creatorId = creatorIdentiLink.getUserId();;
					   userList.add(creatorId);;
				   }else if(userType.equalsIgnoreCase(BpmnParse.NULL)){
					   if(ReferenceRelation.CREATOR.ReferenceRelationCode() == referenceType){
						   endUserListSet.add(creatorId);
						   isNullUserType = true;
					   }else if(ReferenceRelation.HISTORY_ORGANIZERS_OF_TARGET_NODE.ReferenceRelationCode() == referenceType){
						   isNullUserType = true;
						   List<String> userIds = taskService.getHistoricOrganizers(taskDefinition.getKey());
						   if(!userIds.isEmpty() && userIds != null) {
							   for(String userId : userIds) {
								   if(userId != null) {
									   endUserListSet.add(userId);
								   }
							   } 
						   }
//						   endUserListSet.add(orgId);
					   }
				   } else if(userType.equalsIgnoreCase(BpmnParse.ORGANIZER)){
					   isOrganizer = true;
					   if(orgId != null){
						   userList.add(orgId);   
					   }
				   }else{
					   
				   }
				   /*if(ReferenceRelation.HISTORY_ORGANIZERS_OF_TARGET_NODE.ReferenceRelationCode() == referenceType){
					   
				   }else if(ReferenceRelation.CREATOR.ReferenceRelationCode() == referenceType){
					   
				   }else*/ 
				   if(userList.size() !=0){
				   if(ReferenceRelation.PEOPLE_OF_THE_SAME_DEPARTMENT.ReferenceRelationCode() == referenceType){
					   List<String> users = userManager.getPeopleInSameDepartment(userList);
					   if(users!= null){
						   if(BpmnParse.REFERENCE_RELATION_OPERATION_AND.equalsIgnoreCase(opertaion)){
							   endUserList.retainAll(users);
						   }else{
							   endUserList.addAll(users);
						   }
					   }
				   }else if(ReferenceRelation.DIRECT_SUPERIOR.ReferenceRelationCode() == referenceType){
					  
					   		List<String> users = userManager.getUsersSuperiorByIds(userList);
					   	 if(users!= null){
						   if(BpmnParse.REFERENCE_RELATION_OPERATION_AND.equalsIgnoreCase(opertaion)){
							   endUserList.retainAll(users);
						   }else{
							   endUserList.addAll(users);
						   }
					   }
				   }else if(ReferenceRelation.ALL_SUPERIOR.ReferenceRelationCode() == referenceType){
					   String superiorIdStr = userManager.getAllRelativeIds(userList.get(0),false);
					   if(superiorIdStr!= null){
						   userList = new ArrayList<String>();	
						   for(String superiorId:superiorIdStr.split(",")){
							   userList.add(superiorId);
						   }
						   if(BpmnParse.REFERENCE_RELATION_OPERATION_AND.equalsIgnoreCase(opertaion)){
							   endUserList.retainAll(userList);
						   }else{
							   endUserList.addAll(userList);
						   }
					   }
				   }else if(ReferenceRelation.DIRECT_SUBORDINATE.ReferenceRelationCode() == referenceType){
					   List<String> users = userManager.getUsersSubordinateByIds(userList);
					   if(users!= null){
						   if(BpmnParse.REFERENCE_RELATION_OPERATION_AND.equalsIgnoreCase(opertaion)){
							   endUserList.retainAll(users);
						   }else{
							   endUserList.addAll(users);
						   }
					   }
				   }else if(ReferenceRelation.ALL_SUBORDIANTE.ReferenceRelationCode() == referenceType){
					   String subordinateIdStr = userManager.getAllRelativeIds(userList.get(0), true);
					   if(subordinateIdStr!= null){
						   userList = new ArrayList<String>();	
						   for(String subordinateId:subordinateIdStr.split(",")){
							   userList.add(subordinateId);
						   }
						   if(BpmnParse.REFERENCE_RELATION_OPERATION_AND.equalsIgnoreCase(opertaion)){
							   endUserList.retainAll(userList);
						   }else{
							   endUserList.addAll(userList);
						   }
					   }
				   }else if(ReferenceRelation.LEADERS_IN_CHARGE.ReferenceRelationCode() == referenceType){
					   List<String> users = userManager.getOwnerOfTheDepartment(userList);
					   if(users!= null){
						   if(BpmnParse.REFERENCE_RELATION_OPERATION_AND.equalsIgnoreCase(opertaion)){
							   endUserList.retainAll(users);
						   }else{
							   endUserList.addAll(users);
						   }
					   }
				   }else if(ReferenceRelation.LEADER_TO_SECRETARY.ReferenceRelationCode() == referenceType){
					   List<String> users = userManager.getLeaderToSecretaryIds(userList);
					   if(users!= null){
						   if(BpmnParse.REFERENCE_RELATION_OPERATION_AND.equalsIgnoreCase(opertaion)){
							   endUserList.retainAll(users);
						   }else{
							   endUserList.addAll(users);
						   }
					   }
				   }else if(ReferenceRelation.SECRETARY_TO_LEADER.ReferenceRelationCode() == referenceType){
					   List<String> users = userManager.getSecretaryToLeader(userList);
					   if(users!= null){
						   if(BpmnParse.REFERENCE_RELATION_OPERATION_AND.equalsIgnoreCase(opertaion)){
							   endUserList.retainAll(users);
						   }else{
							   endUserList.addAll(users);
						   }
					   }
				   }else if(ReferenceRelation.PARENT_DEPARTMENT_PERSON_INCLUDING_SUB_DEPARTMENT.ReferenceRelationCode() == referenceType){
					  Department dep = userManager.getParentDepartmentForUser(userList);
					  List<Department>  depList = departmentService.getDepartmentBySuperDepartmentId(dep.getId());
					  List<String> depIds = new ArrayList<String>();
					  depIds.add(dep.getId());
					  for(Department depa : depList){
						  depIds.add(depa.getId());
					  }
					  List<String> users = userManager.getUserIdsByDepartmentIds(depIds);
					  
					  
					   if(users!= null){
						   if(BpmnParse.REFERENCE_RELATION_OPERATION_AND.equalsIgnoreCase(opertaion)){
							   endUserList.retainAll(users);
						   }else{
							   endUserList.addAll(users);
						   }
					   }
					   
					   
				   }else if(ReferenceRelation.PARENT_DEPARTMENT_PERSON_EXCLUDING_SUB_DEPARTMENT.ReferenceRelationCode() == referenceType){
					   List<String> users = userManager.getParentDepartmentUser(userList);
					   if(users!= null){
						   if(BpmnParse.REFERENCE_RELATION_OPERATION_AND.equalsIgnoreCase(opertaion)){
							   endUserList.retainAll(users);
						   }else{
							   endUserList.addAll(users);
						   }
					   }
					   
				   }else{
					   List<User> interfaceUser = userManager.getDepartmentInterfaceUser(userList);
					   
					   List<String> users = new ArrayList<String>();
					   for(User user :interfaceUser){
						   users.add(user.getId());
					   }
					   if(users!= null){
						   if(BpmnParse.REFERENCE_RELATION_OPERATION_AND.equalsIgnoreCase(opertaion)){
							   endUserList.retainAll(users);
						   }else{
							   endUserList.addAll(users);
						   }
					   }
				   }
				   
				   opertaion = (String) refRelation.get(BpmnParse.REFERENCE_RELATION_OPERATION);
				   
			   }
				   if(isCreator && creatorId != null && endUserList.contains(creatorId)){
					   endUserList.remove(creatorId);
				   }
				   if(isOrganizer && orgId != null && endUserList.contains(orgId)){
					   endUserList.remove(orgId);
				   }

			   }

			   if(isNullUserType) {
				   endUserList.addAll(endUserListSet); 
			   }
			   /*for(String user : endUserList){
				   taskService.operateIdentityLinksForReference(nextTask.getId(), user, null, OperatingFunction.REFERENCE, IdentityLinkType.ORGANIZER, null, creatorIdentiLink.getId());
			   }*/
			   
		   }
		   }catch(Exception e){
				  throw new BpmException(I18nUtil.getMessageProperty("error.gettingReferenceRelation"));    				
		   }
		   
		   transactorIds.addAll(endUserList);
		   log.info("Potential Organizer for task : "+taskDefinition.getNameExpression()+ " : "+endUserList);
		   return endUserList;
		
	}

	public void attachmentOperation(Map<String, String> rtFormValues,String taskId) throws BpmException{

		 List<String> attachmentIds = new ArrayList<String>();
		 List<String> removeAttachmentIdList = new ArrayList<String>();
		 String addAttachmentIds = rtFormValues.get("addAttachmentIds");
		 String removeAttachmentIds = rtFormValues.get("removeAttachmentIds");
		 if(taskId != null) {
			 if (addAttachmentIds.contains(",")) {
	             String[] keys = addAttachmentIds.split(",");
	             for(String key :keys){
	                 attachmentIds.add(key);
	             }
	           } else {
	               attachmentIds.add(addAttachmentIds);
	           }
			 runtimeService.getAttachmentAndInsertForTask(attachmentIds,taskId,true);
		 }
		 if (removeAttachmentIds.contains(",")) {
             String[] keys = removeAttachmentIds.split(",");
             for(String key :keys){
           	  removeAttachmentIdList.add(key);
             }
           } else {
           	removeAttachmentIdList.add(removeAttachmentIds);
           }
		 runtimeService.deleteAttachmentByIds(removeAttachmentIdList);
	  
	}
	
	/**
	 * Getting organizer for task by task id
	 * @param taskId
	 * @return organizers as comma seperated string
	 */
	public String getOrganizersByTaskId(String taskId) {
		List<IdentityLink> identities = taskService.getIdentityLinksForTask(taskId, TaskRole.ORGANIZER.getName());
		List<String> organizerList = new ArrayList<String>();
		for (IdentityLink identityLink : identities) {
			organizerList.add(identityLink.getUserId());
		}
		return com.eazytec.bpm.common.util.StringUtil.toCSL(organizerList);
	}
	
	/**
	 * Getting organizer for task by task id
	 * @param taskId
	 * @return organizers as comma seperated string
	 */
	public String getOrganizersByTaskIdAndSignOffType(String taskId,int signOffType) {
		List<IdentityLink> identities = taskService.getIdentityLinksForTask(
				taskId, TaskRole.ORGANIZER.getName());
		List<String> organizerList = new ArrayList<String>();
		if (signOffType == TransactorType.MULTI_SEQUENCE_SIGNOFF
				.getStateCode()) {
			if(identities != null){
				String currentOrganizer = identities.get(0).getUserId();
				int minOrder = identities.get(0).getOrder();
				identities.remove(0);
				for(IdentityLink identityLink : identities){
					if(identityLink.getOrder() < minOrder){
						currentOrganizer = identityLink.getUserId();
						minOrder = identityLink.getOrder();
					}
				}
				return currentOrganizer;
			}
		}else {
			for (IdentityLink identityLink : identities) {
				organizerList.add(identityLink.getUserId());
			}
			return com.eazytec.bpm.common.util.StringUtil.toCSL(organizerList);
		}
		return null;
	}

	/**
	 * Getting workflow admin for task by task id
	 * @param taskId
	 * @return admins as comma seperated string
	 */
	public String getWorkFlowAdminByTaskId(String taskId) {
		List<IdentityLink> identities = taskService.getIdentityLinksForTask(
				taskId, TaskRole.WORKFLOW_ADMINISTRATOR.getName());
		List<String> organizerList = new ArrayList<String>();
		for (IdentityLink identityLink : identities) {
			organizerList.add(identityLink.getUserId());
		}
		return com.eazytec.bpm.common.util.StringUtil.toCSL(organizerList);
	}
	
	@SuppressWarnings("unchecked")
	public void sendNotification(String commaSeparatedUser, String message,String commaSeparatedNotificationType,String subject) {
		if(!commaSeparatedUser.isEmpty() && commaSeparatedUser != null){
			List<String> userIds = com.eazytec.bpm.common.util.StringUtil.tokenize(
					commaSeparatedUser, ",");
			List<UserDTO> dtos = userManager.getAllUsersById(userIds);
			List<String> types = com.eazytec.bpm.common.util.StringUtil.tokenize(
					commaSeparatedNotificationType, ",");
			Calendar cal = Calendar.getInstance();
			Date date = cal.getTime();

			for (String type : types) {
				for (UserDTO user : dtos) {
					Notification notification = new Notification();
					notification.setMessage(message);
					notification.setNotificationType(type);
					notification.setSubject(subject);
					notification.setUserId(user.getId());
					if (user.isSendEmailNotification()) {
						notification.setStatus("active");
					} else {
						notification.setStatus(user.getFullName()
								+ "'s send notification flag is not enabled");
					}
					notification.setType("user");
					if(type.equalsIgnoreCase(Constants.NOTIFICATION_EMAIL_TYPE)){
						notification.setTypeId(user.getEmail());
					}
					if(type.equalsIgnoreCase(Constants.NOTIFICATION_INTERNALMESSAGE_TYPE)){
						notification.setTypeId(user.getUsername());
					}
					if(type.equalsIgnoreCase(Constants.NOTIFICATION_SMS_TYPE)){
						notification.setTypeId(user.getUsername());
					}
					notification.setMessageSendOn(date);
					notificationService.saveOrUpdateNotification(notification);
				}
			}
		}
		
	}
	
	public void taskAutoSaveAndBack(String userId,String returnType,HttpServletRequest formValuesMap,String uploadDir)throws Exception{
		Map<String, String> rtFormValues = new HashMap<String, String>();
		Map<String, String[]> formValues = FormDefinitionUtil
				.getFormParamsInStringArray(formValuesMap.getParameterMap());
		Set<String> keyList = formValues.keySet();
		for(String key : keyList){
			String[] rtFormValue = formValues.get(key);
			String rtValue = "";
			for(int idx = 0; idx < rtFormValue.length; idx++){
				rtValue = rtValue + rtFormValue[idx];
				int rtLen = rtFormValue.length - 1;
				if(rtLen > idx){
					rtValue = rtValue + ",";
				}
			}
			if(!rtValue.isEmpty()){
				rtFormValues.put(key, rtValue);
			}else{
				rtFormValues.put(key, "");
			}
		}
		formValuesMap.setAttribute("isAutoSave", "true");
		// Throwing exception if task is returning to start node which has no form.
		//Object canRetun = runtimeService.taskReturn(rtFormValues.get("taskId"),false);
		//if(canRetun instanceof Boolean && Boolean.valueOf(canRetun.toString())){
			Map<String,String> taskDetailsMap = saveTaskForm(formValuesMap,uploadDir,userId);
			taskReturn(userId,taskDetailsMap.get("taskId"),taskDetailsMap.get("resourceName"),returnType,taskDetailsMap.get("processInstanceId"), taskDetailsMap.get("processDefinitionId") );	
		//}else {
		//	throw new BpmException("Back Operation is not possible if no Form in Start Node");
		//}
	} 
	
	public String autoSaveAndReturn(String userId,HttpServletRequest formValuesMap,String uploadDir)throws Exception{
		formValuesMap.setAttribute("isAutoSave", "true");
		Map<String,String> taskDetailsMap = saveTaskForm(formValuesMap,uploadDir,userId);
		taskJump(taskDetailsMap.get("taskId"),taskDetailsMap.get("activityId"),taskDetailsMap.get("nodeType"),taskDetailsMap.get("jumpType"),
				taskDetailsMap.get("resourceName"),userId,taskDetailsMap.get("nextTaskOrganizers"),taskDetailsMap.get("nextTaskCoordinators"),
				taskDetailsMap.get("organizerOrders"), taskDetailsMap.get("processDefinitionId"));
		return taskDetailsMap.get("jumpType");
	} 
	
	public void taskReturn(String userId,String taskId,String resourceName,String returnType,String processInstanceId, String processDefinitionId){
		runtimeService.taskReturn(taskId);
		OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(userId,taskId,resourceName,returnType,processInstanceId, processDefinitionId);
		processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
	} 

	public Map<String,String> saveTaskForm(HttpServletRequest formValuesMap,String uploadDir,String userId)throws Exception{
		Map<String,String> taskDetailsMap = new HashMap<String, String>();
		Map<String, String[]> formValues = FormDefinitionUtil.getFormParamsInStringArray(formValuesMap.getParameterMap());
		Map<String, String> rtFormValues = FormDefinitionUtil.handleFromValues(formValues);
		
		Map<String, String[]> rtSubFormValues = FormDefinitionUtil.getSubFormParams(formValuesMap.getParameterMap());
		Map<String, String> filePathsMap = null;
		boolean isStartNodeTask = Boolean.valueOf(rtFormValues.get("isStartNodeTask"));
		
		String taskId = rtFormValues.get("taskId");
		
		taskDetailsMap.put("taskId", taskId);
		taskDetailsMap.put("resourceName", rtFormValues.get("resourceName"));
		taskDetailsMap.put("activityId", rtFormValues.get("activityId"));
		taskDetailsMap.put("jumpType", rtFormValues.get("jumpType"));
		taskDetailsMap.put("nodeType", rtFormValues.get("jumpNodeType"));
		taskDetailsMap.put("nextTaskOrganizers", rtFormValues.get("nextTaskOrganizers"));
		taskDetailsMap.put("nextTaskCoordinators", rtFormValues.get("nextTaskCoordinators"));
		taskDetailsMap.put("organizerOrders", rtFormValues.get("organizerOrders"));
		taskDetailsMap.put("processDefinitionId", rtFormValues.get("processDefinitionId"));
		
		rtFormValues.remove("activityId");
		rtFormValues.remove("jumpType");
		rtFormValues.remove("nodeType");
		rtFormValues.remove("nextTaskOrganizers");
		rtFormValues.remove("nextTaskCoordinators");
		rtFormValues.remove("isJointConduction");
		rtFormValues.remove("organizerOrders");
		
		if( (String) formValuesMap.getAttribute("isAutoSave") != null ){
			rtFormValues.put("isAutoSave",  (String) formValuesMap.getAttribute("isAutoSave"));  // added to show mandatory alert while back or jump
		} else {
			rtFormValues.put("isAutoSave", "false");
		}
		TaskEntity taskEntity = (TaskEntity) taskDefinitionService.getTaskById(taskId);
		String processInstanceId = taskEntity.getProcessInstanceId();
		taskDetailsMap.put("processInstanceId", processInstanceId);	
		
		Map<String, byte[]>files = null;
		try{
			files = FileUtil.getFileUploadMap(formValuesMap);
			if(files!=null){
			    filePathsMap = FileUtil.uploadFileForTask(formValuesMap, uploadDir,taskId,taskEntity.getProcessInstanceId(),uploadDir,runtimeService,FileUtil.PROCESS_TYPE_TASK);
	        }		
		}catch(IOException e){
			throw new BpmException(I18nUtil.getMessageProperty("error.userMandatory")+" "+uploadDir);    							
		}
		
		rtFormService.saveTaskForm(taskId, rtFormValues, rtSubFormValues, files, filePathsMap, CommonUtil.getLoggedInUserId(), taskEntity.getProcessInstanceId());	
		
		//如果是开始节点，保存表单后给流程实例设置业务主键
		if(isStartNodeTask){
			runtimeService.updateBusinessKey(processInstanceId, rtFormValues.get("id"));
		}
		
		// Add or remove attachment file operation perform
		attachmentOperation(rtFormValues,taskId);  
		
		// avoid save entry in audit table while return,jump or back
		if(rtFormValues.get("isAutoSave") == "false") {
			OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(userId,taskId,rtFormValues.get("resourceName"),OperatingFunction.SAVE,taskEntity.getProcessInstanceId(),rtFormValues.get("processDefinitionId"));
			processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
		}
		return taskDetailsMap;
	}
	
	public void taskJump(String taskId,String activityId,String nodeType,String jumpTypeStr,String resourceName,String userId,String nextTaskOrganizers,String nextTaskCoordinators,
			String nextTaskOrganizerOrders, String processDefinitionId) throws BpmException{
		try{
			String processInstanceId = runtimeService.taskJump(taskId,activityId,false,nodeType).toString();
			if(jumpTypeStr.equals(OperatingFunction.JUMP) || jumpTypeStr.equals(Constants.FORWARD)){
				if(!StringUtil.isEmptyString(nextTaskOrganizers)){
					 setJointConductionByOrder(processInstanceId, nextTaskOrganizers, nextTaskCoordinators,nextTaskOrganizerOrders, false, null, false, false);
				}
			}
			OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(userId,taskId,resourceName,jumpTypeStr,processInstanceId, processDefinitionId);
			processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
		}catch(JSONException e){
			  throw new BpmException(I18nUtil.getMessageProperty("error.jump.jsonError")+" "+taskId+" Jump Type: "+jumpTypeStr);    				
		}catch(Exception e){
			  e.printStackTrace();
			  throw new BpmException(I18nUtil.getMessageProperty("error.jump")+" "+taskId+" Jump Type: "+jumpTypeStr);    				
		}
	}
	 
   public void setJointConductionForAutomaticJump(String processInstanceId,String taskId, TaskDefinition taskDefinition, Map<String,AgencySetting> agencyMap){		 
		 log.debug("Getting Reference Relation for TaskID : "+taskId);
		TaskEntity nextTask = rtTaskService.getNextTaskForProcessInstance(processInstanceId);
		if(nextTask!=null){
			String nextTaskId = nextTask.getId();
			List<Map<String,String>> organizerDetails = new ArrayList<Map<String,String>>();
			Set<String> referenceRelation = new HashSet<String>();
			List<String> transactorIds = new ArrayList<String>();
			Set<Expression> organizersSet = new HashSet<Expression>();
			organizersSet = taskDefinition.getCandidateUserIdExpressions();
			organizersSet.addAll(taskDefinition.getCandidateGroupIdExpressions());
			// getting transactors for process
			if(!taskDefinition.getReferenceRelation().isEmpty() && taskDefinition.getReferenceRelation() != null) {
				referenceRelation = setReferenceRelationInProcess(taskDefinition,taskId,transactorIds,null,null);
				if(!referenceRelation.isEmpty()) {
					organizerDetails = getReferenceRelationAsOrganizerDetails(referenceRelation);
				}
			}
			if(organizersSet.isEmpty()) {
				organizerDetails = getWorkFlowAdminAsOrganizerDetails(taskDefinition.getAdminUserIdExpressions());
			} else {
				organizerDetails.addAll(getOrganizerDetailsByOrganizerSet(organizersSet));
			}
			log.debug("joint conduction set for -------------------"+nextTaskId);
			 String operateFunction = OperatingFunction.ADDORDER;
			 String identityLinkType = IdentityLinkType.ORGANIZER;	
			 List<AgencySetting> agencySettings =  getAgencySettingByProcessDefinitionId(nextTask.getProcessDefinitionId());
			 taskService.operateIdentityLinksByOrder(nextTaskId,organizerDetails,null,operateFunction,identityLinkType,null,agencySettings,processInstanceId);
		}else{
			log.info("No next task found for Process Instance : "+processInstanceId);
		}
	 }
	   
   	public List<Map<String,String>> getWorkFlowAdminAsOrganizerDetails(Set<Expression> adminSet) {
   		List<Map<String,String>> organizerDetails = new ArrayList<Map<String,String>>();
		Map<String,String> organizerMap = null;
		/*Set<Expression> organizersSet = taskDefinition.getCandidateUserIdExpressions();
		organizersSet.addAll(taskDefinition.getCandidateGroupIdExpressions());*/
		List<String> userIds = null;
		int organizerOrder = 1;
		if(adminSet.size() > 0){
			for(Expression organizer : adminSet){
				String[] organizerDetail=organizer.getExpressionText().split("-");
				if(organizerDetail.length > 4){
					String type = organizerDetail[4];
					userIds = new ArrayList<String>();
					if(type.equalsIgnoreCase("roles")){
						userIds = roleService.getUserNamesByRoleId(organizer.getExpressionText().split("-")[0]);	
					}else if(type.equalsIgnoreCase("groups")){
					    userIds = groupService.getUserNamesByGroupId(organizer.getExpressionText().split("-")[0]);	
					}else if(type.equalsIgnoreCase("departments")){
					    userIds = departmentService.getUserNamesByDepartmentId(organizer.getExpressionText().split("-")[0]);	
					}
				    for (String userId : userIds) {
				    	organizerMap = new HashMap<String,String>();
						organizerMap.put("userId",userId);
						organizerMap.put("order",String.valueOf(organizerOrder));	
						organizerDetails.add(organizerMap);
					}
				}else{
					organizerMap = new HashMap<String,String>();
					organizerMap.put("userId",organizer.getExpressionText().split("-")[0]);
					organizerMap.put("order",String.valueOf(organizerOrder));
					organizerDetails.add(organizerMap);
				}
				organizerOrder++;
			}
		}
		return organizerDetails;
	}
   
   	public List<Map<String,String>> getOrganizerDetailsByOrganizerSet(Set<Expression> organizerSet) {
   		List<Map<String,String>> organizerDetails = new ArrayList<Map<String,String>>();
		Map<String,String> organizerMap = null;
		/*Set<Expression> organizersSet = taskDefinition.getCandidateUserIdExpressions();
		organizersSet.addAll(taskDefinition.getCandidateGroupIdExpressions());*/
		List<String> userIds = null;
		int organizerOrder = 1;
		if(organizerSet.size() > 0){
			for(Expression organizer : organizerSet){
				String[] organizerDetail=organizer.getExpressionText().split("-");
				if(organizerDetail.length > 4){
					String type = organizerDetail[4];
					userIds = new ArrayList<String>();
					if(type.equalsIgnoreCase("roles")){
						userIds = roleService.getUserNamesByRoleId(organizer.getExpressionText().split("-")[0]);	
					}else if(type.equalsIgnoreCase("groups")){
					    userIds = groupService.getUserNamesByGroupId(organizer.getExpressionText().split("-")[0]);	
					}else if(type.equalsIgnoreCase("departments")){
					    userIds = departmentService.getUserNamesByDepartmentId(organizer.getExpressionText().split("-")[0]);	
					}
				    for (String userId : userIds) {
				    	organizerMap = new HashMap<String,String>();
						organizerMap.put("userId",userId);
						organizerMap.put("order",organizer.getExpressionText().split("-")[1]);	
						organizerDetails.add(organizerMap);
					}
				}else{
					organizerMap = new HashMap<String,String>();
					organizerMap.put("userId",organizer.getExpressionText().split("-")[0]);
					organizerMap.put("order",organizer.getExpressionText().split("-")[1]);
					organizerDetails.add(organizerMap);
				}
				organizerOrder++;
			}
		}
		return organizerDetails;
	}
   	
   	/**
   	 * 
   	 * @param urgeMessage
   	 * @return
   	 * @throws BpmException
   	 * @throws UnsupportedEncodingException 
   	 */
   	public String replaceTextInUrgeMessage(String urgeMessage,String url,String taskId,String workFlowAdmins,boolean urgeMessageFromJob){
   		try {
   			List<Map<String, Object>> urgeDetailslist=taskService.getUrgeProcessInstanceResultMap(taskId);
   			Map<String, Object> urgeDetailMap = null;
   			Map<String, Object> urgeDetails = new HashMap<String, Object>();
   	   		String currentUrl = "";
   	   		if(urgeMessageFromJob) {
   	   			String propertyUrl = String.valueOf(PropertyReader.getInstance().getPropertyFromFile("String", "system.url"));
   				currentUrl = propertyUrl+"showTaskFormDetail?taskId="+taskId+"&suspendState=&isStartNodeTask=false&formReadOnly=false&creator=&lastOperationPerformed=submit&gridType=mybucket";
   	   		} else {
   				currentUrl = url ;
   	   		}
   	   		if(!urgeDetailslist.isEmpty()){
   	   			//url = url+"/showTaskFormDetail?taskId="+taskId+"&suspendState=&isStartNodeTask=&formReadOnly=&creator=&lastOperationPerformed=";
   	   			urgeDetails = urgeDetailslist.get(0);
	   	   		urgeDetailMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
	   	   		urgeDetailMap.putAll(urgeDetails);
   	   			String processName = String.valueOf(urgeDetailMap.get("processName"));
   		   		User userObj=userService.getUserById(String.valueOf(urgeDetailMap.get("createdBy")));
   		   		String userName = userObj.getFullName();
   	   			urgeMessage=urgeMessage.replace("<workFolwName>", processName);
   	   			urgeMessage=urgeMessage.replace("<createdBy>", userName);
   	   	   		urgeMessage=urgeMessage.replace("<createdAt>", String.valueOf(urgeDetailMap.get("createdAt")).split(" ")[0]);
   	   	   		urgeMessage=urgeMessage.replace("<url>", "<a href="+currentUrl+" target=_blank><u>this link</u></a>");
   	   	   		urgeMessage=urgeMessage.replace("<organizerName>", workFlowAdmins);  	   		
   	   	}
		} catch (Exception e) {
			  throw new BpmException(I18nUtil.getMessageProperty("error.generateUrgeMessage"));    							
		}
   		
   		return urgeMessage;
   	}
   	
   	/**
	 * 
	 * @param taskDefinition
	 * @param referenceRelation
	 * @param organizerList
	 * @return
	 */
	public List<Map<String, String>> getReferenceRelationAsOrganizerDetails(Set<String> referenceRelation) {
		List<Map<String,String>> organizerList = new ArrayList<Map<String,String>>();
		Map<String,String> referenceRelationMap = null;
		if(!referenceRelation.isEmpty()) {
			int organizerOrder = 1;
			for(String organizer : referenceRelation) {
				referenceRelationMap = new HashMap<String,String>();
				referenceRelationMap.put("userId", organizer);
				referenceRelationMap.put("order", String.valueOf(organizerOrder));
				organizerList.add(referenceRelationMap);
				organizerOrder++;
			}
		}
		return organizerList;
	}
	public void createOrUpdateJaveEventValues(Map<String, Object> paramsMap, String uniqueColumns,Map<String, Object> paramsMapforCheck,Object result) throws Exception{
			String uniqueColumnValue;
			List<String> columnId = new ArrayList<String>();
			List<String> columns = new ArrayList<String>();
			List<String> fieldValues = new ArrayList<String>();
			paramsMap.put("tableName", ((Map) result).get("table"));
			paramsMapforCheck.put("tableName", ((Map) result).get("table"));
			if ( ( (Map) result ).containsKey("table")  ) {
				((Map) result).remove("table");
				Iterator it = ((Map) result).entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pairs = (Map.Entry) it.next();
					if (columns.isEmpty()) {
						// initially setting null values for instance id and execution id
						columns.add("`PROC_INST_ID`");
						fieldValues.add("'NULL'");
						columns.add("`EXECUTION_ID`");
						fieldValues.add("'NULL'");
					}
					if(!pairs.getKey().toString().equalsIgnoreCase("delete")) {
						columns.add("`" + pairs.getKey().toString()+ "`");
						fieldValues.add("'"+ pairs.getValue().toString() + "'");
					}
				}
			}
			if (!uniqueColumns.isEmpty()) {
				// should throw exception if no unique key is found.
					if(!uniqueColumns.equalsIgnoreCase("ID")) {
						uniqueColumnValue = ( ((Map)result).get(uniqueColumns)).toString();
						paramsMapforCheck.put("primaryKey",uniqueColumns.toUpperCase());
						paramsMapforCheck.put("primaryKeyVal","'"+uniqueColumnValue+"'");
					}
			} else  {
				  throw new BpmException(I18nUtil.getMessageProperty("error.noUniqueId"));    								
			}
			columnId = getRtValuesInJavaEvent(paramsMapforCheck);
			if( ((Map)result).containsKey("delete") && columnId.size() >0 && ((Map)result).get("delete").toString().equalsIgnoreCase("true") ) {
				// if map contains delete key, then deleting the record in run time table
				paramsMap.put("primaryKey", "ID");
				paramsMap.put("primaryKeyVal", "'"+columnId.get(0)+"'");
				paramsMap.put("columns", columns);
				paramsMap.put("fieldValues", fieldValues);	
				// deletes record in run time table
				operateRtValuesInJavaEvent(paramsMap,false,true);
			} else {
				// if columnId is empty, then insert record or update record with primary key
				if(columnId.size() > 0) {
					//update record
					paramsMap.put("primaryKey", "ID");
					paramsMap.put("primaryKeyVal", "'"+columnId.get(0)+"'");
					paramsMap.put("columns", columns);
					paramsMap.put("fieldValues", fieldValues);					
					operateRtValuesInJavaEvent(paramsMap,true,false);
				} else {
					//insert record
					String id = UUID.randomUUID().toString();
					columns.add("`ID`");
					fieldValues.add("'" + id + "'");
					paramsMap.put("columns", columns);
					paramsMap.put("fieldValues", fieldValues);
					operateRtValuesInJavaEvent(paramsMap,false,false);
				}
			}
	}
	
	public Map<String,String> getOranizersByInstanceId(String processInsId) {
		List<IdentityLink> identityLinks = taskService.getIdentityLinksByInstanceId(processInsId);
		
		Map<String,String> currentTaskDetails = new HashMap<String,String>();
		for (IdentityLink identityLink : identityLinks) {
			currentTaskDetails.put("organizer",identityLink.getUserId());
			currentTaskDetails.put("currentTaskId",identityLink.getTaskId());
		}
		//return com.eazytec.bpm.common.util.StringUtil.toCSL(organizerList);
		return currentTaskDetails;
	}
	
	public void operateRtValuesInJavaEvent(Map<String, Object> paramsMap, boolean isUpdate, boolean isDelete){
		taskService.operateRtValuesInJavaEvent(paramsMap,isUpdate, isDelete);
	}

	public List<String> getRtValuesInJavaEvent(Map<String, Object> paramsMap){
		return taskService.getRtValuesInJavaEvent(paramsMap);
	}	
	
	
	
	@Autowired
	public void setTaskService(org.activiti.engine.TaskService taskService) {
		this.taskService = taskService;
	}

	@Autowired
	public void setRtProcessService(ProcessService rtProcessService) {
		this.rtProcessService = rtProcessService;
	}
	
	@Autowired
	public void setRtTaskService(TaskService rtTaskService) {
		this.rtTaskService = rtTaskService;
	}


}

class ValueComparator implements Comparator<String> {

    Map<String, Integer> base;
    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(b) >= base.get(a)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
