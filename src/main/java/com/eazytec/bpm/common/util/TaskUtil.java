package com.eazytec.bpm.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.task.IdentityLink;
import org.apache.velocity.app.VelocityEngine;

import com.eazytec.Constants;
import com.eazytec.bpm.engine.TaskRole;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.exceptions.EazyBpmException;


/**
 * Hold functions commonly used for bpm related task specific util methods.
 * @author madan
 *
 */
public class TaskUtil {
	
		
	/**
	 * Set into context of grid column names and field names and it attributes
	 * @param context
	 * @return
	 * @throws EazyBpmException
	 */
	public static String generateScriptForTask(VelocityEngine velocityEngine, List<Map<String, Object>> tasks, String type , Locale locale)throws EazyBpmException{
		Map<String, Object> context = new HashMap<String, Object>();
		if(type.equalsIgnoreCase("mybucket") || type.equalsIgnoreCase("documentToBeDone")){        
			createMyBuckets(context,locale, type);
		 }else if(type.equalsIgnoreCase("myinvolvements")){
			 createMyInvolvements(context,locale);
		 }else if(type.equalsIgnoreCase("waitinglist")){
			 createWaitingList(context,locale);
		 }else if(type.equalsIgnoreCase("toRead") || type.equalsIgnoreCase("readedList") || type.equalsIgnoreCase("toReadDocument") || type.equalsIgnoreCase("readDocumentList") ){
			 createToRead(context,locale,type);
		 }else if(type.equalsIgnoreCase("myOwnedTasks")){
			 createMyOwnedTasks(context,locale, type);
		 }else if(type.equalsIgnoreCase("cancelDocumentList")) {
			 createCancelDocumentList(context,locale, type);
		 }else if(type.equalsIgnoreCase("doneList") || type.equalsIgnoreCase("doneDocumentList")){
			 createDoneList(context,locale, type);
		 }
		String jsonFieldValues = "";		
		try {
			if(tasks != null && !(tasks.isEmpty())){					
				jsonFieldValues = CommonUtil.getJsonString(tasks);
			} 
		} catch (Exception e) {
			throw new EazyBpmException("Problem parsing tasks as json", e);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	private static void createCancelDocumentList(Map<String, Object> context,Locale locale, String type) {
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
//		String columnNames = "['Id','Name','Process Name','Current Task','Created User','Created At','Operate', 'Older Forms', 'Classification','suspensionState','lastOpPerformed','canActivate','isFormReadOnly']";
		String columnNames = "['Id','名称','流程名','当前任务','创建者','创建时间','Operate', '历史表单', 'Classification','suspensionState','lastOpPerformed','canActivate','isFormReadOnly']";
//		context.put("title", "Cancel Document List");
		context.put("title", "撤销公文");
		context.put("needHeader", true);
		context.put("gridId", "MY_BUCKET");
		context.put("needCheckbox", true);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "left", "_showTaskDetail", "false");
		CommonUtil.createFieldNameList(fieldNameList, "processName", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "currentTask", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "owner", "100", "left", "", "false");
		// CommonUtil.createFieldNameList(fieldNameList, "description", "150", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createTime", "100", "center", "", "false");
        CommonUtil.createFieldNameList(fieldNameList, "canActivate", "55", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "operate", "100", "center", "_operateOnTaskForDoneList", "false");
		CommonUtil.createFieldNameList(fieldNameList, "olderforms", "100", "center", "_showOlderForm", "true");
		CommonUtil.createFieldNameList(fieldNameList, "classificationId", "50", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "suspensionState", "50", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "lastOperationPerformed", "50", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "isFormReadOnly", "50", "center","", "true");
		if(!type.equalsIgnoreCase("doneDocumentList")){
			context.put("dynamicGridWidth", "organizationGridWidth");
			context.put("dynamicGridHeight", "organizationGridHeight");
			context.put("needTreeStructure", true);
		}
		context.put("columnNames", columnNames);
		context.put("noOfRecords", "16");
		context.put("sortname", "id");
		context.put("fieldNameList", fieldNameList);		
	}

	/**
	 * Gets the script for grid that lists the task instances
	 * @param velocityEngine
	 * @param taskInstances list of instances as detailed map
	 * @return the exact script
	 * @throws EazyBpmException
	 */
	public static String generateScriptForTaskInstancesGrid(VelocityEngine velocityEngine, List<Map<String, Object>> taskInstances ,Locale locale)throws EazyBpmException{
		Map<String, Object> context = new HashMap<String, Object>();
		String jsonFieldValues = "";	
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY,locale); 
		context.put("title", appResourceBundle.getString("task.instances"));
		context.put("gridId", "MY_INSTANCES_DETAIL");
		String columnNames = "['Id','任务名称','任务办理人','任务发起时间','任务完成时间','executionId','processInstanceId']";
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "center", "", "false");
		//CommonUtil.createFieldNameList(fieldNameList, "name", "100", "center", "_showTaskDetailForMyInstance", "false");
		CommonUtil.createFieldNameList(fieldNameList, "assignee", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "startTime", "80", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "endTime", "80", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "executionId", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "processInstanceId", "100", "left", "", "true");
		context.put("columnNames", columnNames);
		context.put("noOfRecords", "8");
		context.put("fieldNameList", fieldNameList);
		context.put("dynamicGridWidth", "organizationGridWidth");
		context.put("dynamicGridHeight", "organizationGridHeight");
		
		try {
			if(taskInstances != null && !(taskInstances.isEmpty())){					
				jsonFieldValues = CommonUtil.getJsonString(taskInstances);
			} 
		} catch (Exception e) {
			throw new EazyBpmException("Problem parsing task instances as json", e);
		}
		
		context.put("jsonFieldValues", jsonFieldValues);
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	private static void createMyOwnedTasks(Map<String, Object> context , Locale locale, String type) {
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY,locale); 
		String columnNames = "['Id','Process Name22','Created User','Current Task','Creater','Created Time','State','Operate', 'Older Forms', 'Classification','ProcessDefinitionId'," +
				"'suspensionState','executionId','resourceName','LastOperationPerformed','nodeType','myOwnedTasks','isForStartNodeTask','workflowAdmin','processInstanceId','formkey','formId']";
		context.put("title", appResourceBundle.getString("task.myOwnedTasks"));
		context.put("gridId", "MY_BUCKET");
		context.put("needCheckbox", true);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "processName","100","left","","false");
		CommonUtil.createFieldNameList(fieldNameList, "owner", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "currentTask", "100", "center", "_showTaskDetail", "false");
		CommonUtil.createFieldNameList(fieldNameList, "creater", "150", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "createTime", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "state", "100", "center","", "false");
		CommonUtil.createFieldNameList(fieldNameList, "operate", "100", "center", "_operateOnTask", "false");
		CommonUtil.createFieldNameList(fieldNameList, "olderforms", "100", "center", "_showOlderForm", "true");
		CommonUtil.createFieldNameList(fieldNameList, "classificationId", "50", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "processDefinitionId", "50", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "suspensionState", "50", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "executionId", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "resourceName", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "lastOperationPerformed", "100", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "nodeType", "100", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "myOwnedTasks", "100", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "isForStartNodeTask", "100", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "workflowAdmin", "50", "center","", "true"); 
		CommonUtil.createFieldNameList(fieldNameList, "processInstanceId", "50", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "formkey", "50", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "formId", "50", "center","", "true");
		context.put("dynamicGridWidth", "organizationGridWidth");
		context.put("dynamicGridHeight", "organizationGridHeight");
		context.put("needTreeStructure", true);
		context.put("columnNames", columnNames);
		context.put("noOfRecords", "16");
		context.put("sortname", "id");
		context.put("fieldNameList", fieldNameList);
	}
	
	private static void createMyBuckets(Map<String, Object> context , Locale locale, String type) {
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY,locale); 
		String columnNames = "['Id','流水号','当前任务名称','流程名称','发起人','开始时间','状态','重要程度','操作', 'Older Forms', 'Classification','suspensionState','lastOpPerformed','hasFormKey']";
		
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "processInstanceId", "50", "center","", "false");
		
		if(type.equalsIgnoreCase("documentToBeDone")){
			context.put("title", "Document To Be Done");
			context.put("needHeader", true);
			CommonUtil.createFieldNameList(fieldNameList, "name", "100", "center", "_showTaskDetail", "false");
		}else if(type.equalsIgnoreCase("doneList")){
			
		}else{
			context.put("title", appResourceBundle.getString("task.myBuckets"));
			CommonUtil.createFieldNameList(fieldNameList, "name", "100", "center", "_showTaskDetail", "false");
		}
		context.put("gridId", "MY_BUCKET");
		//context.put("needCheckbox", true);
		
		CommonUtil.createFieldNameList(fieldNameList, "processName", "100", "center", "_showFormOnly", "false");
		CommonUtil.createFieldNameList(fieldNameList, "owner", "50", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createTime", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "operate", "60", "center", "_showStatusForMyBuckets", "false");
		CommonUtil.createFieldNameList(fieldNameList, "priority", "50", "center", "_showPriorityForMyBuckets", "false");
		CommonUtil.createFieldNameList(fieldNameList, "", "50", "center", "_operateOnTask", "false");
		CommonUtil.createFieldNameList(fieldNameList, "olderforms", "100", "center", "_showOlderForm", "true");
		CommonUtil.createFieldNameList(fieldNameList, "classificationId", "50", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "suspensionState", "50", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "lastOperationPerformed", "50", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "hasFormKey", "50", "center","", "true");
		
		if(!type.equalsIgnoreCase("documentToBeDone")){
			context.put("dynamicGridWidth", "organizationGridWidth");
			context.put("dynamicGridHeight", "organizationGridHeight");
			context.put("needTreeStructure", true);
		}
		context.put("columnNames", columnNames);
		context.put("noOfRecords", "16");
		context.put("sortname", "id");
		context.put("fieldNameList", fieldNameList);
	}
	
	private static void createDoneList(Map<String, Object> context , Locale locale, String type) {
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','流水号','已办任务','流程名称','当前任务','发起人','开始日期','办理人','办理时间','状态', '操作','canActivate', 'Classification','suspensionState','lastOpPerformed','canActivate','isFormReadOnly']";
		if(type.equalsIgnoreCase("doneDocumentList")){
			context.put("title", "Done Document List");
			context.put("needHeader", true);
		}else{
			context.put("title", "已办列表");
		}
		context.put("gridId", "MY_BUCKET");
		//context.put("needCheckbox", true);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "processInstanceId", "50", "center","", "false");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "processName", "100", "center", "_showFormOnly", "false");
		CommonUtil.createFieldNameList(fieldNameList, "currentTask", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "owner", "50", "center", "", "false");
		// CommonUtil.createFieldNameList(fieldNameList, "description", "150", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createTime", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "submitUser", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "endTime", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "operate", "60", "center", "_showStatusForDoneList", "false");
		CommonUtil.createFieldNameList(fieldNameList, "", "50", "center", "_operateOnTaskForDoneList", "false");
		CommonUtil.createFieldNameList(fieldNameList, "canActivate", "55", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "olderforms", "100", "center", "_showOlderForm", "true");
		CommonUtil.createFieldNameList(fieldNameList, "classificationId", "50", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "suspensionState", "50", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "lastOperationPerformed", "50", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "isFormReadOnly", "50", "center","", "true");
		if(!type.equalsIgnoreCase("doneDocumentList")){
			context.put("dynamicGridWidth", "organizationGridWidth");
			context.put("dynamicGridHeight", "organizationGridHeight");
			context.put("needTreeStructure", true);
		}
		context.put("columnNames", columnNames);
		context.put("noOfRecords", "16");
		context.put("sortname", "id");
		context.put("fieldNameList", fieldNameList);
	}
	
	private static void createToRead(Map<String, Object> context , Locale locale,String type) {
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		context.put("gridId", "MY_BUCKET");
		if(type.equalsIgnoreCase("toRead") ){
			context.put("title", "To Read");
			context.put("needCheckbox", true);
		}else if(type.equalsIgnoreCase("readedList")){
			context.put("title", "Read List");
		}else if(type.equalsIgnoreCase("toReadDocument")){
			context.put("title", "Document To Be Read");
			context.put("needHeader", true);
		}else if(type.equalsIgnoreCase("readDocumentList")){
			context.put("title", "Read Document List");
			context.put("needHeader", true);
		}
		String columnNames = "['Id','流水号','已阅任务','流程名称','发起人','开始日期','办理人','办理时间','操作', 'Older Forms', 'Classification','suspensionState']";
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "processInstanceId", "50", "center","", "false");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "center", "_showTaskDetail", "false");
		CommonUtil.createFieldNameList(fieldNameList, "processName", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "owner", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createTime", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "submitUser", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "endTime", "100", "center", "", "false");
		if(type.equalsIgnoreCase("toRead") || type.equalsIgnoreCase("toReadDocument")){
			CommonUtil.createFieldNameList(fieldNameList, "operate", "100", "center", "_operateOnTaskForReader", "false");
		}else{
			CommonUtil.createFieldNameList(fieldNameList, "operate", "100", "center", "_operateOnTaskForReadedUser", "false");
		}
		CommonUtil.createFieldNameList(fieldNameList, "olderforms", "100", "center", "_showOlderForm", "true");
		CommonUtil.createFieldNameList(fieldNameList, "classificationId", "50", "center","", "true");
		CommonUtil.createFieldNameList(fieldNameList, "suspensionState", "50", "center","", "true");
		if(!type.equalsIgnoreCase("toReadDocument") && !type.equalsIgnoreCase("readDocumentList")){
			context.put("dynamicGridWidth", "organizationGridWidth");
			context.put("dynamicGridHeight", "organizationGridHeight");
			context.put("needTreeStructure", true);
		}
		context.put("columnNames", columnNames);
		context.put("noOfRecords", "16");
		context.put("fieldNameList", fieldNameList);
	}
	
	private static void createMyInvolvements(Map<String, Object> context, Locale locale) {
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY,locale); 
		context.put("title", appResourceBundle.getString("task.myInvolvements"));
		context.put("gridId", "MY_BUCKET");
		String columnNames = "['Id','Name','Process Name','Created User','Created At','Assigned User','Assigned Group','Is Claimable','Action','Operate','Classification']";
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "center", "_showTaskDetail", "false");
		CommonUtil.createFieldNameList(fieldNameList, "processName", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "owner", "100", "left", "", "false");
		//CommonUtil.createFieldNameList(fieldNameList, "description", "150", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createTime", "80", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "assignee", "80", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "assignedGroup", "80", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "taskClaimable", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "status", "80", "center", "_claim", "false");
		CommonUtil.createFieldNameList(fieldNameList, "operate", "100", "center", "_operateOnTask", "false");
		CommonUtil.createFieldNameList(fieldNameList, "classificationId", "50", "center","", "true");
		context.put("dynamicGridWidth", "organizationGridWidth");
		context.put("dynamicGridHeight", "organizationGridHeight");
		context.put("needTreeStructure", true);
		context.put("columnNames", columnNames);
		context.put("noOfRecords", "16");
		context.put("fieldNameList", fieldNameList);
	}
	
	private static void createWaitingList(Map<String, Object> context, Locale locale) {
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY,locale); 
		context.put("title", appResourceBundle.getString("task.waitingList"));
		context.put("gridId", "MY_BUCKET");		
		String columnNames = "['Id','流水号','当前任务名称','流程名称','发起人','开始时间','指定用户组','Is Claimable','操作','Classification']";
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "processInstanceId", "50", "center","", "false");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "center", "_showTaskDetail", "false");
		CommonUtil.createFieldNameList(fieldNameList, "processName", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "owner", "100", "center", "_showOwnerFullName", "false");
		//CommonUtil.createFieldNameList(fieldNameList, "description", "150", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createTime", "80", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "assignedGroup", "80", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "taskClaimable", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "status", "50", "center", "_claim", "false");
		CommonUtil.createFieldNameList(fieldNameList, "classificationId", "50", "center","", "true");
		context.put("dynamicGridWidth", "organizationGridWidth");
		context.put("dynamicGridHeight", "organizationGridHeight");
		context.put("needTreeStructure", true);
		context.put("columnNames", columnNames);
		context.put("noOfRecords", "16");
		context.put("fieldNameList", fieldNameList);
	}
	
	public static TaskRole getTransactorTypeOfPriority(List<IdentityLink> identityLinks) {
		TaskRole taskRole = null;
		if (null != identityLinks) {
			for (IdentityLink identityLinkEntity : identityLinks) {
				String taskRoleType = identityLinkEntity.getType();
				if (taskRoleType != null && taskRoleType.equalsIgnoreCase(TaskRole.ORGANIZER.getName())) {
					taskRole = TaskRole.ORGANIZER;
				} else if (taskRoleType.equalsIgnoreCase(TaskRole.COORDINATOR.getName())) {
					if (taskRole == null) {
						taskRole = TaskRole.COORDINATOR;
					} else {
						if (!taskRole.equals(TaskRole.ORGANIZER)) {
							taskRole = TaskRole.COORDINATOR;
						}
					}

				} else if (taskRoleType.equalsIgnoreCase(TaskRole.WORKFLOW_ADMINISTRATOR.getName())) {
					if (taskRole == null) {
						taskRole = TaskRole.WORKFLOW_ADMINISTRATOR;
					} else {
						if (!taskRole.equals(TaskRole.ORGANIZER) && !taskRole.equals(TaskRole.COORDINATOR)) {
							taskRole = TaskRole.WORKFLOW_ADMINISTRATOR;
						}
					}

				} else if (taskRoleType.equalsIgnoreCase(TaskRole.CREATOR.getName())) {
					if (taskRole == null) {
						taskRole = TaskRole.CREATOR;
					} else {
						if (!taskRole.equals(TaskRole.ORGANIZER) && !taskRole.equals(TaskRole.COORDINATOR) && !taskRole.equals(TaskRole.WORKFLOW_ADMINISTRATOR)) {
							taskRole = TaskRole.CREATOR;
						}
					}

				} else if (taskRoleType.equalsIgnoreCase(TaskRole.PROCESSED_USER.getName())) {
					if (taskRole == null) {
						taskRole = TaskRole.PROCESSED_USER;
					} else {
						if (!taskRole.equals(TaskRole.ORGANIZER) && !taskRole.equals(TaskRole.COORDINATOR) && !taskRole.equals(TaskRole.WORKFLOW_ADMINISTRATOR) && !taskRole.equals(TaskRole.CREATOR)) {
							taskRole = TaskRole.PROCESSED_USER;
						}
					}

				} else if (taskRoleType.equalsIgnoreCase(TaskRole.READER.getName())) {
					if (taskRole == null) {
						taskRole = TaskRole.READER;
					}
				}
			}
		}
		return taskRole;
	}
	
	public static Map<String, Boolean>getNodeLevelPermissionForTaskRole(TaskRole taskRoleForUser, FormPropertyHandler formPropertyHandler){
		 String taskRoleName = taskRoleForUser.getName();
		 if(taskRoleName.equalsIgnoreCase(TaskRole.CREATOR.getName())){
			 return formPropertyHandler.getCreatorPermissions();
		 }else if(taskRoleName.equalsIgnoreCase(TaskRole.COORDINATOR.getName())){
			 return formPropertyHandler.getCoordinatorPermissions();
		 }else if(taskRoleName.equalsIgnoreCase(TaskRole.ORGANIZER.getName())){
			 return formPropertyHandler.getOrganizerPermissions();
		 }else if(taskRoleName.equalsIgnoreCase(TaskRole.READER.getName())){
			 return formPropertyHandler.getReaderPermissions();
		 }else if(taskRoleName.equalsIgnoreCase(TaskRole.PROCESSED_USER.getName())){
			 return formPropertyHandler.getProcessedUserPermissions();
		 }else{
			 return formPropertyHandler.getWfAdminPermissions();
		 }
	  }
	
	
}
