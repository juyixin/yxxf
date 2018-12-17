package com.eazytec.bpm.metadata.process.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.NoneEndEventActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.SubProcessActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.module.service.ModuleService;
import com.eazytec.bpm.metadata.process.dao.ProcessDefinitionDao;
import com.eazytec.bpm.metadata.process.service.ProcessDefinitionService;
import com.eazytec.common.util.CommonUtil;
//import com.eazytec.bpm.runtime.task.service.TaskService;
import com.eazytec.dao.UserDao;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Module;
import com.eazytec.model.OperatingFunctionAudit;
import com.eazytec.model.User;
import com.eazytec.service.UserManager;
import com.eazytec.util.DateUtil;
/**
 * Meta Data level service logic related to process, implements {@link ProcessDefinitionService}
 * @author madan
 *
 */
@Service("processDefinitionService")
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService{

	
	private RepositoryService repositoryService;
	private HistoryService historyService;
	private RuntimeService runtimeService;
	private UserManager userManager;
	private ModuleService moduleService;
	private ProcessDefinitionDao processDefinitionDao;
	private UserDao userDao;
	private TaskService taskService;

	protected final Log log = LogFactory.getLog(getClass());

	
	/**
	 * {@inheritDoc} getProcessesDefinitionsByUser
	*/	
	public List<ProcessDefinition> getProcessesDefinitionsByUser(User user) throws BpmException{
		if(userManager.isUserAdmin(user)){
			log.debug("User is admin, getting all process defs by default----");
			return repositoryService.createProcessDefinitionQuery().activeVersion().orderByProcessDefinitionName().asc().list();
		}else{
			log.debug("User getting process defs by modules------");
			List<Module> modules = moduleService.getAllModuleList();			
			if(modules!=null&&modules.size()>0){
				log.debug("User getting modules------"+modules.size());
				return repositoryService.createProcessDefinitionQuery().activeVersion().orderByProcessDefinitionName().modules(modules).asc().list();
			}else{
				return null;
			}
		}
		
	}
	
	/**
	 * {@inheritDoc} getProcessesDefinitions
	*/	
	public List<ProcessDefinition> getProcessesDefinitions() throws BpmException{
		return repositoryService.createProcessDefinitionQuery().isSystemDefined("0").orderByProcessDefinitionName().asc().list();
	}
	
	public List<ProcessDefinition> getProcessesDefinitionsForView() throws BpmException{
		return repositoryService.createProcessDefinitionQuery().isSystemDefined("0").activeVersion().orderByOrderNo().asc().list();
	}
	
	public ProcessDefinition getActiveProcessesDefinitionByName(String name) throws BpmException{
		return repositoryService.createProcessDefinitionQuery().active().activeVersion().processDefinitionName(name).singleResult();
	}
	
	/**
	 * {@inheritDoc deployBPMN}
	 */
	public boolean deployBPMN(String deploymentName, String xmlfilePath, String imgFilePath) throws BpmException, FileNotFoundException{		
		DeploymentBuilder builder = repositoryService.createDeployment();
		builder.name(deploymentName);
		builder.addInputStream(xmlfilePath, new FileInputStream(xmlfilePath));
		builder.addInputStream(imgFilePath, new FileInputStream(imgFilePath));
		builder.deploy();
		return true;		
	}	
	
	/**
	 *{@inheritDoc}  getAllProcessDefinitionVersions
	 */
	public List<ProcessDefinition> getAllProcessDefinitionVersions(String processKey) throws EazyBpmException {
		return repositoryService.getProcessDefinitionByKey(processKey);
	}	
	
	/**
	 *{@inheritDoc}  getAllProcessDefinitionByKeys
	 */
	public List<ProcessDefinition> getAllProcessDefinitionByKeys(List<String> keys) throws EazyBpmException {
		return repositoryService.getProcessDefinitionByKeys(keys);
	}	

	/**
	 * {@inheritDoc} deployBPMNSourceFile
	 * @throws FileNotFoundException 
	 */
	public boolean deployBPMNSourceFile(String fileName,String filePath,String description,String classificationId, boolean isSystemDefined) throws	BpmException, FileNotFoundException {
		DeploymentBuilder builder = repositoryService.createDeployment();
		builder.setDescription(description);
		builder.setClassificationId(classificationId);
		builder.setIsSystemDefined(isSystemDefined);
		builder.addInputStream(fileName, new FileInputStream(filePath));
		builder.setModifiedUser(CommonUtil.getLoggedInUserId());
		builder.deploy();
		return true;
	}
	
	/**
	 * {@inheritDoc} deployBPMNSourceFileWithJson
	 * @throws FileNotFoundException 
	 */
	public boolean deployBPMNSourceFileWithJson(String fileName,String filePath,String description,String classification, String jsonString) throws	BpmException, FileNotFoundException {
		DeploymentBuilder builder = repositoryService.createDeployment();
		builder.setDescription(description);
		builder.setClassificationId(classification);
		builder.addInputStream(fileName, new FileInputStream(filePath), jsonString);
		builder.deploy();
		return true;
	}
	
	/**
	 * {@inheritDoc} deployBPMNSourceFileWithJson
	 * @throws FileNotFoundException 
	 */
	public boolean deployBPMNSourceFileWithJson(String fileName,String filePath,String description,String classification, Integer orderNo, String jsonString,String svgString) throws	BpmException, FileNotFoundException {
		DeploymentBuilder builder = repositoryService.createDeployment();
		builder.setDescription(description);
		builder.setClassificationId(classification);
		builder.setOrderNo(orderNo);
		builder.addInputStream(fileName, new FileInputStream(filePath), jsonString,svgString);
		builder.setModifiedUser(CommonUtil.getLoggedInUserId());
		builder.deploy();
		return true;
	}
	
	/**
	 * {@inheritDoc} deployBPMNSourceFile
	 * @throws FileNotFoundException 
	 */
	public boolean deployBPMNSourceFile(String deployName, String fileName,String filePath,String description,String classificationId, boolean isSystemDefined) throws	BpmException, FileNotFoundException {
		if(deployName==null){
			log.info("Deployment name is null and hence saved as null------");
			return deployBPMNSourceFile(fileName,filePath,description,classificationId, isSystemDefined);
		}
		DeploymentBuilder builder = repositoryService.createDeployment();
		builder.setDescription(description);
		builder.addInputStream(deployName, fileName, new FileInputStream(filePath));
		builder.setClassificationId(classificationId);
		builder.setModifiedUser(CommonUtil.getLoggedInUserId());
		//builder.setIsSystemDefined(isSystemDefined);
		builder.deploy();
		return true;
	}
	
	/**
	 * {@inheritDoc} getProcessDefinitionById
	 */
	public ProcessDefinition getProcessDefinitionById(String processDefinitionId){		
		ProcessDefinition procDef = repositoryService.getProcessDefinition(processDefinitionId);
		/*ReadOnlyProcessDefinition processDefinition = 
				((RepositoryServiceImpl)repositoryService)
				.getDeployedProcessDefinition(processDefinitionId);
	 
		if (processDefinition != null) {
				    for (PvmActivity activity : processDefinition.getActivities()) {
				      String type = (String) activity.getProperty("type");
				    }
		}*/
		return procDef;
	}
	
	/**
	 * {@inheritDoc} getListProcessDefinitionById
	 */
	public List<Map<String,String>> getListProcessDefinitionById(String processDefinitionId){		
		//ProcessDefinition procDef = repositoryService.getProcessDefinition(processDefinitionId);
		ReadOnlyProcessDefinition processDefinition = ((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(processDefinitionId);
		
		List<Map<String,String>> listProcessDefinition = new ArrayList<Map<String,String>>();
		if (processDefinition != null) {
		    for (PvmActivity activity : processDefinition.getActivities()) {
		    	String type = (String) activity.getProperty("type");
		    	if(type == "userTask"){
		    		Map<String,String> taskMap = new HashMap<String, String>();
		    		taskMap.put("taskId", activity.getId());
		    		taskMap.put("taskName",(String)activity.getProperty("name"));
		    		listProcessDefinition.add(taskMap);
		    	}
		    	
		    }	
		}
		return listProcessDefinition;
	}
	
	/**
	 * 	getting all tasks that were previously operated while submitting the returned task.
	 *	First row returned by the query result contains the user id who returned the task. 
	 */
	public List<Map<String,String>> getProcessDefinitionForBackOffJump(String processDefinitionId,String activityId,String jumpType,String processInstanceId){	

		List<Task> historicTaskInstances = taskService.createTaskQuery().customQuery("selectBackOffTask").processInstanceId(processInstanceId).taskDefinitionKey(activityId).list();
		historicTaskInstances = new ArrayList<Task>(new LinkedHashSet<Task>(historicTaskInstances));
		List<Map<String,String>> listProcessDefinition = new ArrayList<Map<String,String>>();
	//	List<Map<String,String>> organizers = new ArrayList<Map<String,String>>(); 
	//	String organizer = "";
		 List<Map<String, Object>> taskListAsMap = taskService.createTaskDescribeQuery(historicTaskInstances);
		 for (Map<String, Object> taskMap : taskListAsMap) {
			 Map<String,String> taskMapForBackOff = new HashMap<String, String>();
				taskMapForBackOff.put("taskName", (String) taskMap.get("name"));
				taskMapForBackOff.put("taskId",(String) taskMap.get("taskDefinitionKey"));	
				taskMapForBackOff.put("nodeType", Constants.FORWARDNODE);
				taskMapForBackOff.put("organizer",(String) taskMap.get("organizer") ); // adding organizer who completed task
				// initially adding the returned user as potential organizer
				/*if(organizers.isEmpty()) {
					taskMapForBackOff.put("organizer",(String) taskMap.get("organizer") );
					organizers.add(taskMapForBackOff);
					organizer= (String) taskMap.get("organizer");
				} else {
					taskMapForBackOff.put("organizer",(String) taskMap.get("assignee") );
				}*/
	    		listProcessDefinition.add(taskMapForBackOff);
		 }
		return listProcessDefinition;
	}	
	
	public boolean checkIsNextTaskCompleted(HistoricTaskInstance task){	
		List<Task> checkIsNextTaskCompleted = null;
		checkIsNextTaskCompleted = taskService.createTaskQuery().customQuery("checkIsNextTaskCompleted").taskCreatedOn(task.getStartTime()).taskId(task.getId()).processInstanceId(task.getProcessInstanceId()).list();
		if(checkIsNextTaskCompleted.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public List<Map<String,String>> getListProcessDefinitionByIdJump(String processDefinitionId,String activityId,String jumpType,String processInstanceId,String executionId){	
		ReadOnlyProcessDefinition processDefinition = ((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(processDefinitionId);
//		List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByHistoricTaskInstanceEndTime().desc()
//																	      .orderByHistoricActivityInstanceStartTime().desc().list();

		// get all previously completed task for corresponding execution id and process instance id 
		List<Task> completedhistoricTaskInstances = taskService.createTaskQuery().customQuery("selectPreviousCompletedTasks").processInstanceId(processInstanceId).executionId(executionId).taskDefinitionKey(activityId).list();
		
		 List<Map<String, Object>> completedhistoricTask = taskService.createTaskDescribeQuery(completedhistoricTaskInstances);

		boolean isForwardNode = false; // added to get all forward nodes (For JUMP)
		boolean stopCollect = false; // added to prevent forward nodes to be added for return jump type
		boolean subProcessJump = false;
		boolean subProcessJumpEnd = false;
		
		List<Map<String,String>> listProcessDefinition = new ArrayList<Map<String,String>>();
		if (processDefinition != null) {
			//iterate all activities in process
		    for(PvmActivity activity : processDefinition.getActivities()) {
		    	if(!activity.getId().equals(activityId)){
	    			boolean isTaskOperated = false;
			    	String type = (String) activity.getProperty("type");
			    	if(type.equalsIgnoreCase("userTask") || type.equalsIgnoreCase("subProcess") ){
			    		if(!type.equalsIgnoreCase("startEvent") && !type.equalsIgnoreCase("subProcess") ){
					   		 if(isForwardNode && !subProcessJump) {
						   			ActivityImpl act = (ActivityImpl) activity;
						   			addForWardNodes(activity,act,listProcessDefinition,jumpType,type,isForwardNode,stopCollect);
						   		 } else {
							   		 for (Map<String, Object> taskMap : completedhistoricTask) {
								   			String taskName = (String) taskMap.get("name");
								   			 	if( ((String)taskMap.get("taskDefinitionKey")).equals(activity.getId())) {
								    				isTaskOperated = true;
								    				addTask(listProcessDefinition,((String)taskMap.get("taskDefinitionKey")),taskName,jumpType,type,isForwardNode,stopCollect);
								    				break;
								   			 	}
								   		 }
						   		 }
			    		} else {
			    			ActivityImpl act = (ActivityImpl) activity;
									  if(act.getActivityBehavior() instanceof SubProcessActivityBehavior && !subProcessJumpEnd) {
										  for(PvmActivity subProcessActivity :act.getActivities()) {
								    			if(!subProcessActivity.getId().equals(activityId)) {
								    				if(isForwardNode) {
											   			ActivityImpl subProcess = (ActivityImpl) subProcessActivity;
											   			if(subProcess.getActivityBehavior() instanceof NoneEndEventActivityBehavior) {
											   				subProcessJumpEnd = true;
											   				break;
											   			}
											   			addForWardNodes(subProcessActivity,subProcess,listProcessDefinition,jumpType,type,isForwardNode,stopCollect);
											   			subProcessJump = true;
								    				} else {
												   		 for (Map<String, Object> taskMap : completedhistoricTask) {
													   			String taskName = (String) taskMap.get("name");
													   			 	if( ((String)taskMap.get("taskDefinitionKey")).equals(subProcessActivity.getId())) {
													    				isTaskOperated = true;
													    				addTask(listProcessDefinition,((String)taskMap.get("taskDefinitionKey")),taskName,jumpType,type,isForwardNode,stopCollect);
													    				break;
													   			 	}
													   		 }
								    				}

								    			} else {
													isForwardNode = true;
													stopCollect = true;
								    			}
										  }
									  }
			    		}
			    	}
		    	}else{
					isForwardNode = true;
					stopCollect = true;
				}
		    }	
		}
		return listProcessDefinition;
	}
	
	private void addForWardNodes(PvmActivity activity,ActivityImpl act,List<Map<String, String>> listProcessDefinition,String jumpType,String type,boolean isForwardNode,boolean stopCollect) {
			if(act.getActivityBehavior() instanceof UserTaskActivityBehavior) {
	        		UserTaskActivityBehavior activityBehaviour  = (UserTaskActivityBehavior)act.getActivityBehavior();
	        		Expression name = activityBehaviour.getTaskDefinition().getNameExpression();
	        		System.out.println("==========="+name.getExpressionText()+"===========");
	   			addTask(listProcessDefinition,activity.getId(),name.getExpressionText(),jumpType,type,isForwardNode,stopCollect);
   			}
	}

	private List<Map<String,String>> addTask(List<Map<String,String>> listProcessDefinition,String taskKey,String taskName,String jumpType,String type,boolean isForwardNode,boolean stopCollect) {
			if(jumpType.equals(Constants.RETURN)){
				if(!stopCollect) {
	    			Map<String,String> taskMap = new HashMap<String, String>();
		    		taskMap.put("taskId", taskKey);
		    		if(type.equalsIgnoreCase("startEvent")){
			    		taskMap.put("taskName",type);
		    		}else {
			    		taskMap.put("taskName",taskName);	
		    		}
	    			taskMap.put("nodeType", "backwardNode");
		    		listProcessDefinition.add(taskMap);
				}
    		}else if(jumpType.equals(Constants.JUMP)){
    			Map<String,String> taskMap = new HashMap<String, String>();
	    		taskMap.put("taskId",  taskKey);
	    		if(type.equalsIgnoreCase("startEvent")){
		    		taskMap.put("taskName",type);
	    		}else {
		    		taskMap.put("taskName", taskName);	
	    		}
	    		if(isForwardNode){
	    			taskMap.put("nodeType", Constants.FORWARDNODE);
	    		}else {
	    			taskMap.put("nodeType", Constants.BACKWARDNODE);
	    		}
	    		listProcessDefinition.add(taskMap);
    		}
			return listProcessDefinition;
		
	}

	/**
	 * {@inheritDoc} getListProcessDefinitionById
	 */
	/*public List<Map<String,String>> getListProcessDefinitionByIdJump(String processDefinitionId,String activityId,String jumpType,String processInstanceId){	
		ReadOnlyProcessDefinition processDefinition = ((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(processDefinitionId);
		List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByHistoricTaskInstanceEndTime().desc()
																		      .orderByHistoricActivityInstanceStartTime().desc().list();
		boolean isForwardNode = false;
		boolean stopCollect = false;
		List<Map<String,String>> listProcessDefinition = new ArrayList<Map<String,String>>();
		if (processDefinition != null) {
		    for(PvmActivity activity : processDefinition.getActivities()) {
		    	if(!activity.getId().equals(activityId)){
			    	boolean isTaskOperated = false;
			    	String type = (String) activity.getProperty("type");
			    	if(type.equalsIgnoreCase("userTask") || (type.equalsIgnoreCase("startEvent") && ((ProcessDefinitionEntity) processDefinition).getHasStartFormKey())){
			    		if(!type.equalsIgnoreCase("startEvent")){
				    		for(HistoricTaskInstance historicTaskInstance : historicTaskInstances){
				    			if(historicTaskInstance.getTaskDefinitionKey().equals(activity.getId()) && 
				    					historicTaskInstance.getDeleteReason().equals(TaskEntity.DELETE_REASON_COMPLETED)){
				    				isTaskOperated = true;
				    				break;
				    			}
				    		}
			    		}else {
			    			isTaskOperated = true;
			    		}
			    		if(isTaskOperated || isForwardNode){
			    			if(jumpType.equals(Constants.RETURN)){
				    			if(!stopCollect){
					    			Map<String,String> taskMap = new HashMap<String, String>();
						    		taskMap.put("taskId", activity.getId());
						    		if(type.equalsIgnoreCase("startEvent")){
							    		taskMap.put("taskName",type);
						    		}else {
							    		taskMap.put("taskName",(String)activity.getProperty("name"));	
						    		}
					    			taskMap.put("nodeType", "backwardNode");
						    		listProcessDefinition.add(taskMap);
				    			}
				    		}else if(jumpType.equals(Constants.JUMP)){
				    			Map<String,String> taskMap = new HashMap<String, String>();
					    		taskMap.put("taskId", activity.getId());
					    		if(type.equalsIgnoreCase("startEvent")){
						    		taskMap.put("taskName",type);
					    		}else {
						    		taskMap.put("taskName",(String)activity.getProperty("name"));	
					    		}
					    		if(isForwardNode){
					    			taskMap.put("nodeType", Constants.FORWARDNODE);
					    		}else {
					    			taskMap.put("nodeType", Constants.BACKWARDNODE);
					    		}
					    		listProcessDefinition.add(taskMap);
				    		}
			    		}
			    	}
		    	}else{
					isForwardNode = true;
					stopCollect = true;
				}
		    }	
		}
		return listProcessDefinition;
	}*/
	
	/**
	 * {@inheritDoc}
	 */
	public void suspendProcessDefinitionById(String processDefinitionId) throws BpmException{
		repositoryService.suspendProcessDefinitionById(processDefinitionId);
	}
	 
	/**
	 * {@inheritDoc}
	 */
	 public void suspendProcessDefinitionByKey(String processDefinitionKey) throws BpmException{
		 repositoryService.suspendProcessDefinitionByKey(processDefinitionKey);
	 }
	
	/**
	 * {@inheritDoc resolveProcessDefinitions}
	 * 
	 */
	public List<Map<String, Object>> resolveProcessDefinitions(List<ProcessDefinition> processDefinitions) throws BpmException {
		List<Map<String, Object>> resolvedProcessDefinitionsListAsMap = new ArrayList<Map<String, Object>>();
		try {
			String activeProcessId = null;
			List<Map<String, Object>> processDefinitionsMapAsList = repositoryService.createProcessDescribeQuery(processDefinitions);
			int instanceCount = 0;
			int activeCount = 0;
			int successCount = 0;
			int suspendCount = 0;
			int terminatedCount = 0;
			
			for (Map<String, Object> processDefinitionsMap : processDefinitionsMapAsList) {
				if (activeProcessId == null) {
					activeProcessId = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionsMap.get("key").toString()).activeVersion().list().get(0).getId();
				}
				if (activeProcessId != null) {
					processDefinitionsMap.put("activeProcessId", activeProcessId);
				}
				
				String processDefinitionId = processDefinitionsMap.get("id").toString();
				
				instanceCount = (int) historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).count();
				if (instanceCount > 0) {
					activeCount = (int) runtimeService.createProcessInstanceQuery().processDefinitionId(processDefinitionId).active().count();
					successCount = (int) historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).finished().count();
					suspendCount = (int) historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).suspended().count();
					terminatedCount = (int) historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).deleted().count();
					
					processDefinitionsMap.put("activeCount", activeCount);
					processDefinitionsMap.put("successCount", successCount);
					processDefinitionsMap.put("suspendCount", suspendCount);
					processDefinitionsMap.put("terminatedCount", terminatedCount);
					processDefinitionsMap.put("noOfInstacnes", instanceCount);

					processDefinitionsMap.put("processGridType", "monitorProcesses");
					
					if (processDefinitionsMap.get("activeVersion").toString().equalsIgnoreCase("true")) {
						processDefinitionsMap.put("versionStatus", "激活");
						processDefinitionsMap.put("restore", "Active");

					} else {
						processDefinitionsMap.put("versionStatus", "未激活");
						processDefinitionsMap.put("restore", "Restore");
					}
				}
				processDefinitionsMap.put("createdAt", getProcessCreatedDateForDeplymentId(processDefinitionsMap.get("deploymentId").toString()));

				resolvedProcessDefinitionsListAsMap.add(processDefinitionsMap);
			}
			return resolvedProcessDefinitionsListAsMap;

		} catch (Exception e) {
			return resolvedProcessDefinitionsListAsMap;
		}
	}

	public List<Map<String, Object>> resolveMyMonitorProcesses(List<ProcessDefinition> processDefinitions, String loggedInUser) throws BpmException {
		List<Map<String, Object>> resolvedProcessDefinitionsListAsMap = new ArrayList<Map<String, Object>>();
		try {
			String activeProcessId = null;

			List<Map<String, Object>> processDefinitionsMapAsList = repositoryService.createProcessDescribeQuery(processDefinitions);
			int instanceCount = 0;
			int activeCount = 0;
			int successCount = 0;
			int suspendCount = 0;
			int terminatedCount = 0;
			
			for (Map<String, Object> processDefinitionsMap : processDefinitionsMapAsList) {
				if (activeProcessId == null) {
					activeProcessId = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionsMap.get("key").toString()).activeVersion().list().get(0).getId();
				}
				if (activeProcessId != null) {
					processDefinitionsMap.put("activeProcessId", activeProcessId);
				}
				
				String processDefinitionId = processDefinitionsMap.get("id").toString();
				
				instanceCount = (int) historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).startedBy(loggedInUser).count();
				if (instanceCount > 0) {
					
					activeCount = (int) historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).startedBy(loggedInUser).activated().count();
					successCount = (int) historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).startedBy(loggedInUser).finished().count();
					suspendCount = (int) historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).startedBy(loggedInUser).suspended().count();
					terminatedCount = (int) historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).startedBy(loggedInUser).deleted().count();
					
					processDefinitionsMap.put("activeCount", activeCount);
					processDefinitionsMap.put("successCount", successCount);
					processDefinitionsMap.put("suspendCount", suspendCount);
					processDefinitionsMap.put("terminatedCount", terminatedCount);
					processDefinitionsMap.put("noOfInstacnes", instanceCount);
					
					processDefinitionsMap.put("createdAt", getProcessCreatedDateForDeplymentId(processDefinitionsMap.get("deploymentId").toString()));
					
					if (processDefinitionsMap.get("activeVersion").toString().equalsIgnoreCase("true")) {
						processDefinitionsMap.put("versionStatus", "激活");
						processDefinitionsMap.put("restore", "Active");

					} else {
						processDefinitionsMap.put("versionStatus", "未激活");
						processDefinitionsMap.put("restore", "Restore");
					}
					processDefinitionsMap.put("processGridType", "myMonitorProcesses");
				}
				resolvedProcessDefinitionsListAsMap.add(processDefinitionsMap);

			}
			return resolvedProcessDefinitionsListAsMap;
		} catch (Exception e) {
			return resolvedProcessDefinitionsListAsMap;
		}
	}
	
	/**
	 * {@inheritDoc getProcessCreatedDateForDeplymentId}
	 */
	public String getProcessCreatedDateForDeplymentId(String deploymentId)throws BpmException{
		Date date = repositoryService.createDeploymentQuery().deploymentId(deploymentId).list().get(0).getDeploymentTime();
		return DateUtil.convertDateToString(date);
	}
	
	
	 /**
	  * To Activate(Make as Currently Active version) the process Definition by processDefinitionId
	  * Which make other all other active version as Inactive.
	  * @param processDefinitionId
	  * @throws BpmException
	  * {@inheritDoc activateProcessDefinitionVersionById}
	  */
	public void activateProcessDefinitionVersionById(String processDefinitionId) throws BpmException{
		repositoryService.activateProcessDefinitionVersionById(processDefinitionId);
	}
	
	/**
	  * To In-activate the process Definition by processDefinitionId
	  * Only One version of the process should be active at a time
	  * @param processDefinitionId
	  * @throws BpmException
	  * {@inheritDoc inActivateProcessDefinitionVersionById}
	  */
	public void inActivateProcessDefinitionVersionById(String processDefinitionId) throws BpmException{
		repositoryService.inActivateProcessDefinitionVersionById(processDefinitionId);
	}
	
	
	/**
	 * {@inheritDoc deleteProcessDefinitionById}
	 */
	public boolean deleteProcessDefinitionById(String id, boolean cascade)throws BpmException{	
		//TO-DO: Configure delete reason in properties key
		repositoryService.deleteProcessDefinitionByDefinitionId(id, "Deleted Definition", cascade);
		return true;
	}
	
	/**
	 * {@inheritDoc deleteProcessDefinitions}
	 */
	public boolean deleteProcessDefinitions(List<ProcessDefinition>processDefinitions, boolean cascade)throws BpmException{	
		//TO-DO: Configure delete reason in properties key
		repositoryService.deleteProcessDefinitions(processDefinitions, "Deleted Definition", cascade);
		return true;
	}	
	
	/**
	 * {@inheritDoc} getAllProcessesDefinitionsByUser
	*/	
	public List<ProcessDefinition> getAllProcessesDefinitionsByUser(User user) throws BpmException{
		if(userManager.isUserAdmin(user)){
			log.debug("User is admin, getting all processlist by default----");
			return repositoryService.createProcessDefinitionQuery().activeVersion().list();
		}else{
			log.debug("User getting processlist by modules------");
			List<Module> modules = moduleService.getAllModuleList();			
			if(modules!=null&&modules.size()>0){
				log.debug("User getting modules------"+modules.size());
				return repositoryService.createProcessDefinitionQuery().activeVersion().modules(modules).list();
			}else{
				return null;
			}
		}
		
	}
	
	/**
	 * {@inheritDoc} isUserExist
	 */
	public List<String> userNameCheck(String strUserList) throws BpmException{
		
		List<String> userList = new ArrayList<String>();
        List<String> resUserList = new ArrayList<String>();
        
        String[] userListArray = strUserList.split(",");
    	
    	for (String user : userListArray) {
    		userList.add(user);
		}
        
    	for (User user : userDao.getUsersByIds(userList)) {
    		resUserList.add(user.getUsername());
		}
    	
    	return resUserList;
	}
	
	/**
	 * {@inheritDoc} isUserExist
	 */
	public List<Map<String,String>> userNameCheckFullName(String strUserList) throws BpmException{
		List<Map<String,String>> userFullNameList = new ArrayList<Map<String,String>>();
		List<String> userList = new ArrayList<String>();
		 String[] userListArray = strUserList.split(",");
		 for (String user : userListArray) {
	    		userList.add(user);
			}
		 for (User user : userDao.getUsersByIds(userList)) {
				Map<String,String> userNameMap = new HashMap<String,String>();
	    		userNameMap.put("id",user.getId());
	    		userNameMap.put("fullName",user.getFullName());
	    		userFullNameList.add(userNameMap);
			}
		 
		
		return userFullNameList;
		
	}
	
	@Override
	public OperatingFunctionAudit saveOperatingFunctionAudit(OperatingFunctionAudit operFunAudit) throws BpmException {
		return processDefinitionDao.saveOperatingFunctionAudit(operFunAudit);
	}
	
	
	@Autowired
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	@Autowired
	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}
	
	@Autowired
	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	@Autowired
	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	@Autowired
	public void setProcessDefinitionDao(ProcessDefinitionDao processDefinitionDao) {
		this.processDefinitionDao = processDefinitionDao;
	}
	
	public UserDao getUserDao() {
		return userDao;
	}

	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	@Autowired
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

}
