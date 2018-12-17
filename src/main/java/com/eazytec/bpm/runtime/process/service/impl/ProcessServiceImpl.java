package com.eazytec.bpm.runtime.process.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.form.FormFieldPermission;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.persistence.entity.AttachmentEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.ResourceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.task.OperatingFunctionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.OperatingFunction;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.jdom.JDOMException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.datadictionary.service.DataDictionaryService;
import com.eazytec.bpm.admin.department.service.DepartmentService;
import com.eazytec.bpm.common.util.FileUtils;
import com.eazytec.bpm.common.util.I18nUtil;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.bpm.engine.TaskRole;
import com.eazytec.bpm.metadata.form.service.FormDefinitionService;
import com.eazytec.bpm.metadata.process.dao.ProcessDao;
import com.eazytec.bpm.metadata.process.service.ProcessDefinitionService;
import com.eazytec.bpm.metadata.task.service.TaskDefinitionService;
import com.eazytec.bpm.opinion.service.UserOpinionService;
import com.eazytec.bpm.runtime.form.service.FormService;
import com.eazytec.bpm.runtime.process.service.ProcessService;
import com.eazytec.bpm.runtime.task.service.TaskService;
import com.eazytec.common.util.ClassificationComparator;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.TemplateUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Classification;
import com.eazytec.model.Department;
import com.eazytec.model.LabelValue;
import com.eazytec.model.MetaForm;
import com.eazytec.model.Opinion;
import com.eazytec.model.Process;
import com.eazytec.model.User;
import com.eazytec.service.LookupManager;
import com.eazytec.service.UserManager;
import com.eazytec.util.DateUtil;

/**
 * <p>Process service at runtime, implements {@link ProcessService}</p>
 * @author madan
 * @author Revathi
 *
 */
@Service("rtProcessService")
public class ProcessServiceImpl implements ProcessService{
	
	private RuntimeService runtimeService;	
	private FormDefinitionService formDefintionService;
	private HistoryService historyService;
	private RepositoryService repositoryService;
	private ProcessDefinitionService processDefinitionService;
	private org.activiti.engine.TaskService taskService;
	private TaskService rtTaskService;
	private ProcessDao processDao;	
    private UserManager userManager;
    private DepartmentService departmentService;
	public VelocityEngine velocityEngine;
	private UserOpinionService userOpinionService; 
	private DataDictionaryService dataDictionaryService;
    private TaskDefinitionService taskDefinitionService;
    private FormService rtFormService;
    @Autowired
    private LookupManager lookupManager;

	@Autowired
	public void setRtFormService(FormService rtFormService) {
		this.rtFormService = rtFormService;
	}

    @Autowired
	public void setTaskDefinitionService(TaskDefinitionService taskDefinitionService) {
		this.taskDefinitionService = taskDefinitionService;
	}

	@Autowired
	public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
		this.dataDictionaryService = dataDictionaryService;
	}
	
	@Autowired
	public void setProcessDao(ProcessDao processDao) {
		this.processDao = processDao;
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
	public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }	
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * {@inheritDoc} startProcessInstanceByKey
	 */
	public boolean startProcessInstanceByKey(String processDefinitionKey) throws BpmException {
		try{
			runtimeService.startProcessInstanceByKey(processDefinitionKey);
			return true;
		}catch(ActivitiException ae){
			  throw new BpmException(I18nUtil.getMessageProperty("error.startProcessFailed")+"ProcessDefinition Key:"+processDefinitionKey);
		}				
	}
	
	/**
	 * {@inheritDoc startProcessInstanceById}
	 */
	public ProcessInstance startProcessInstanceById(String processDefinitionId){
		try{
			return runtimeService.startProcessInstanceById(processDefinitionId);
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}		
	}
	
    public String designDefaultForm(ProcessDefinition defaultProcessDefinition, Map<String, Object> permissionVal)throws BpmException{
        return TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showStartHtmlForm.vm", designDefaultForm(defaultProcessDefinition, velocityEngine, permissionVal));
    }

	private Map<String, Object> designDefaultForm(ProcessDefinition defaultProcessDefinition,VelocityEngine velocityEngine2, Map<String, Object> permissionVal) {
			return designStartProcessScript(defaultProcessDefinition, velocityEngine, "bpm/default/submitDefaultProcessForm",true,"null",false, permissionVal);
	}

    public String designEditDefaultProcessScript(ProcessDefinition noticeDefinition,String insId,Map<String,Object> permissionVal)throws BpmException{
        return TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showStartHtmlForm.vm", 
        		designStartProcessScript(noticeDefinition, velocityEngine, "bpm/default/updateDefaultProcessForm",true,insId,true, permissionVal));
    }
    
	@Override
	public ProcessDefinition getDefaultProcessDefinition(String formName) throws BpmException {
		ProcessDefinition processDefinition = processDefinitionService.getActiveProcessesDefinitionByName(formName);
		if (processDefinition!=null){
			return processDefinition;
		}else{
			throw new BpmException("No Definitions found active!");
		}
	} 
	
	public void updateDefaultProcess(Map<String, String> rtFormValues,Map<String, String[]> rtSubFormValues, Map<String, byte[]> files,	Map<String, String> filePathsMap) {
		try{
			String instanceId = rtFormValues.get("processInstanceId");
			if(StringUtil.isEmptyString(rtFormValues)){
				throw new EazyBpmException("No instance id found");
			}
			ProcessDefinition processDef = getDefaultProcessDefinition((String)rtFormValues.get("formId"));
			rtFormValues.remove("instanceId");
			rtFormService.updateStartForm(processDef.getId(), instanceId, rtFormValues, rtSubFormValues, files, filePathsMap);		
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(),e);
		}	
	}
	/**
	 * {@inheritDoc} get logged in users running process
	 */
	public List<HistoricProcessInstance> getProcessIntanceByUser(String loggedInUser) throws BpmException {		
		return historyService.createHistoricProcessInstanceQuery().startedBy(loggedInUser).orderByProcessInstanceStartTime().desc().list();
	}	
	
	public List<HistoricProcessInstance> getMyDocumentProcessIntanceByUser(String loggedInUser) throws BpmException {		
		return historyService.createHistoricProcessInstanceQuery().processClassification("document_management").startedBy(loggedInUser).unfinished().activated().list();
	}	
	 
	public List<Map<String, Object>> getAllProcessIntanceCountForFlowStatistics(Map<String, Object> searchParams) throws BpmException, ParseException {
		
		List<Map<String, Object>> processInstanceDetails = new ArrayList<Map<String, Object>>();
		List<Process> processList=getAllProcessList(searchParams);
		Department department = null;
		if(searchParams != null && !searchParams.isEmpty()){
			if(searchParams.get("departmentId") != null && !(String.valueOf(searchParams.get("departmentId")).isEmpty()) && String.valueOf(searchParams.get("isAdvancedSearch")).equals("true")){
				department = departmentService.getDepartmentById(String.valueOf(searchParams.get("departmentId")));
    		}/*else{
    			departmentList = departmentService.getAllDepartments();
    		}
		}else{
			departmentList = departmentService.getAllDepartments();
		}*/
		if(processList.size() > 0){
			for(Process process : processList){
				List<String> processDefinitionIds = getProcessDefinitionIdsByName(process.getName());
				 Map<String,Object> proInsStatus = new HashMap<String, Object>();
				int index=0;
				//for (LabelValue department : departmentList) {
				if(department != null) {
		    		 int succeededCount=0,inProgCount=0,suspendedCount=0,terminatedCount=0;
					 String departmentId = department.getId();
					 List<LabelValue> departmentUsers=userManager.getDepartmentUsersAsLabelValue(departmentId)	;
					 for(LabelValue departmentUser : departmentUsers)
					 {
						 String userId=	departmentUser.getValue();
						 if(searchParams != null && !searchParams.isEmpty() && String.valueOf(searchParams.get("isAdvancedSearch")).equals("true") && searchParams.get("startDate") != null && !(String.valueOf(searchParams.get("startDate")).isEmpty()) && searchParams.get("finishDate") != null && !(String.valueOf(searchParams.get("finishDate")).isEmpty())){
							 Date startDate = DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss", String.valueOf(searchParams.get("startDate"))+":00");
							 Date finishDate = DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss", String.valueOf(searchParams.get("finishDate"))+":00");
							 succeededCount += (int) historyService.createHistoricProcessInstanceQuery().processDefinitionIds(processDefinitionIds).startDateBy(startDate).finishDateBy(finishDate).startedBy(userId).finished().count();
				    		 inProgCount += (int) historyService.createHistoricProcessInstanceQuery().processDefinitionIds(processDefinitionIds).startDateBy(startDate).finishDateBy(finishDate).startedBy(userId).activated().count();
							 suspendedCount += (int) historyService.createHistoricProcessInstanceQuery().processDefinitionIds(processDefinitionIds).startDateBy(startDate).finishDateBy(finishDate).startedBy(userId).suspended().count();
							 terminatedCount += (int)historyService.createHistoricProcessInstanceQuery().processDefinitionIds(processDefinitionIds).startDateBy(startDate).finishDateBy(finishDate).startedBy(userId).terminated().count();
						 }else{
							 succeededCount += (int) historyService.createHistoricProcessInstanceQuery().processDefinitionIds(processDefinitionIds).startedBy(userId).finished().count();
				    		 inProgCount += (int) historyService.createHistoricProcessInstanceQuery().processDefinitionIds(processDefinitionIds).startedBy(userId).activated().count();
							 suspendedCount += (int) historyService.createHistoricProcessInstanceQuery().processDefinitionIds(processDefinitionIds).startedBy(userId).suspended().count();
							 terminatedCount += (int)historyService.createHistoricProcessInstanceQuery().processDefinitionIds(processDefinitionIds).startedBy(userId).terminated().count();
						 }
					 }
					 proInsStatus.put("succeededCount"+index, succeededCount);
					 proInsStatus.put("inProgressCount"+index, inProgCount);
					 proInsStatus.put("suspendedCount"+index, suspendedCount);
					 proInsStatus.put("terminatedCount"+index, terminatedCount);
					 proInsStatus.put("departmentName"+index, department.getName());
					 index++;
				}	 
				 proInsStatus.put("processName", process.getName());
				 proInsStatus.put("processCount",processList.size());
				 proInsStatus.put("departmentCount","1");
				 processInstanceDetails.add(proInsStatus);
			}
		}/*else {
			int index=0;
			Map<String,Object> proInsStatus = new HashMap<String, Object>();
			for (LabelValue department : departmentList) {
				 proInsStatus.put("departmentName"+index, department.getLabel());
				 index++;
			}	
			 proInsStatus.put("processCount",processList.size());
			 proInsStatus.put("departmentCount",departmentList.size());
			 processInstanceDetails.add(proInsStatus);
		}*/
	}
		return processInstanceDetails;
	}
	
	/**
	 * {@inheritDoc} get all users running process
	 */
	public List<HistoricProcessInstance> getAllProcessIntance() throws BpmException {
		return historyService.createHistoricProcessInstanceQuery().unfinished().list();
	}
	
	/**
	 * {@inheritDoc resolveHistoricProcessInstance} 
	 */
	public List<Map<String, Object>> resolveHistoricProcessInstance(List<HistoricProcessInstance>  historicProcessInstanceList,String processGridType){
		List<Map<String, Object>> resolvedHistoricProcessInstanceListAsMap = new ArrayList<Map<String,Object>>();
		List<HistoricTaskInstance> historicTaskInstances = new ArrayList<HistoricTaskInstance>();
	int historicTaskInstanceCount = 0;

	try{
		List<Map<String, Object>>  historicProcessInstanceListAsMap = historyService.createHistoicProcessDescribeQuery(historicProcessInstanceList);
		for(Map<String, Object> historicProcessInstanceMap : historicProcessInstanceListAsMap){
			ProcessDefinition processDefinition = repositoryService.getProcessDefinition((String)(historicProcessInstanceMap.get("processDefinitionId")));
			historicProcessInstanceMap.put("processDefinitionId",processDefinition.getId());
			historicProcessInstanceMap.put("name", processDefinition.getName());
			historicProcessInstanceMap.put("version", processDefinition.getVersion());
			historicProcessInstanceMap.put("description", processDefinition.getDescription());
			historicProcessInstanceMap.put("classificationId", processDefinition.getClassificationId());
			historicProcessInstanceMap.put("startTime",convertDateToSTDString(historicProcessInstanceMap.get("startTime").toString()));
			if(historicProcessInstanceMap.get("endTime") != null){
				historicProcessInstanceMap.put("endTime",convertDateToSTDString(historicProcessInstanceMap.get("endTime").toString()));
			}
			historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(historicProcessInstanceMap.get("id").toString()).list();
			//check if any operation performed after start process
			if(historicTaskInstances !=null && historicTaskInstances.size() > 1){
				HistoricTaskInstance firstTask =  historicTaskInstances.get(1);
				if(firstTask.getEndTime() == null) {
					historicProcessInstanceMap.put("operationPerformed", "false");
				} else {
					historicProcessInstanceMap.put("operationPerformed", "true");
				}
				
			} else { // for saved process
				historicProcessInstanceMap.put("operationPerformed", "false");
			}
			//end			
			if(historicTaskInstances !=null && historicTaskInstances.size() > 0){
				historicTaskInstanceCount = historicTaskInstances.size();
				historicProcessInstanceMap.put("noOfInstacnes",historicTaskInstanceCount); 
				HistoricTaskInstance historicActivityInstance =  historicTaskInstances.get(historicTaskInstanceCount-1);

				historicProcessInstanceMap.put("taskId",historicActivityInstance.getId());
				historicProcessInstanceMap.put("resourceName",processDefinition.getResourceName());
				historicProcessInstanceMap.put("taskName",historicActivityInstance.getName());
				if(historicActivityInstance.getAssignee() == null){
					List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(historicActivityInstance.getId());
					if(identityLinks!=null&&identityLinks.size()>0){
						//IdentityLink identityLink =  identityLinks.get(0);
						String groupId = "";
						for(IdentityLink identityLink :identityLinks){
							if((identityLink != null) && (identityLink.getGroupId() != null) && (identityLink.getType().equalsIgnoreCase("candidate"))){
								if(groupId.isEmpty())
									groupId = identityLink.getGroupId();
								else
									groupId = groupId + ","+identityLink.getGroupId();
								
							}
						}
						historicProcessInstanceMap.put("assignedGroup",groupId );
						historicProcessInstanceMap.put("operator", groupId);
						
					}
					
				}else{
					historicProcessInstanceMap.put("assignee", historicActivityInstance.getAssignee());
					historicProcessInstanceMap.put("operator", historicActivityInstance.getAssignee());
				}
			}
			
			historicProcessInstanceMap.put("actionTaken", "Submit");
			if(processGridType!=null){
				historicProcessInstanceMap.put("processGridType", processGridType);
			}
			resolvedHistoricProcessInstanceListAsMap.add(historicProcessInstanceMap);
		}		
	} catch(ActivitiException ae){
		  throw new BpmException(I18nUtil.getMessageProperty("error.HistoricTask"));
	} catch(Exception e){
		  throw new BpmException(I18nUtil.getMessageProperty("error.HistoricTask"));
	}
	return resolvedHistoricProcessInstanceListAsMap;}
	/**
     * To Convert the Date for Format : EEE MMM dd HH:mm:ss zzz yyyy to Standard Date time Format : MM/dd/yyyy HH:mm:ss string
	 * @param dateTime
	 * @return
	 */
	private String convertDateToSTDString(String dateStr) {
		String standardDate = null;
		try {
			standardDate = DateUtil.convertDefaultDateToSTDDateFormat(dateStr);
		} catch (ParseException e) {
			log.info("Date cannot be parsed for : "+dateStr);
		}	
		return standardDate;
	}
	
	/**
	 * {@inheritDoc} Suspend the process for given processDefinitionID
	 */
	public void suspendProcessDefinitionById ( String processDefinitionId,boolean suspendProcessInstances,Date suspensionDate){
		try{
			if(suspensionDate !=null){
				repositoryService.suspendProcessDefinitionById(processDefinitionId, suspendProcessInstances, suspensionDate);
			}else{
				repositoryService.suspendProcessDefinitionById(processDefinitionId);
			}
		}catch(ActivitiException ae){
			  throw new BpmException(I18nUtil.getMessageProperty("error.suspendFailed"));
		}
	}
	/**
	 * {@inheritDoc} Suspend the process for given processDefinitionIds
	 */
	public void suspendProcessDefinitionByIds (boolean suspendProcessInstances,Date suspensionDate,List<String> processDefinitionIds){
		try{
			if(suspensionDate !=null){
				repositoryService.suspendProcessDefinitionByIds(suspendProcessInstances, suspensionDate,processDefinitionIds);
			}else{
				repositoryService.suspendProcessDefinitionByIds(processDefinitionIds);
			}
		}catch(ActivitiException ae){
			throw new BpmException(I18nUtil.getMessageProperty("error.bulkSuspendFailed"));			
		}
	}
	
	/**
	 * {@inheritDoc} Activate the process for given processDefinitionID
	 */
	public void activateProcessDefinitionById ( String processDefinitionId,boolean suspendProcessInstances,Date suspensionDate){
		try{
			if(suspensionDate !=null){
				repositoryService.activateProcessDefinitionById(processDefinitionId, suspendProcessInstances, suspensionDate);
			}else{
				repositoryService.activateProcessDefinitionById(processDefinitionId);
			}
		}catch(ActivitiException ae){
			throw new BpmException(I18nUtil.getMessageProperty("error.bulkSuspendFailed"));			
		}
	}	
	
	/**
	 * {@inheritDoc} Activate the process for given processDefinitionIds
	 */
	public void activateProcessDefinitionByIds (boolean suspendProcessInstances,Date suspensionDate,List<String> processDefinitionIds){
		try{
			if(suspensionDate !=null){
				repositoryService.activateProcessDefinitionByIds(suspendProcessInstances, suspensionDate,processDefinitionIds);
			}else{
				repositoryService.activateProcessDefinitionByIds(processDefinitionIds);
			}
		}catch(ActivitiException ae){
			throw new BpmException(I18nUtil.getMessageProperty("error.activateFailed"));			
		}
	}	
	
	/**
	 * {@inheritDoc getStartForm}
	 */
	public StartFormData getStartForm(String processDefinitionId) throws BpmException {
		try{			
			return formDefintionService.getStartFormData(processDefinitionId);
		}catch(ActivitiException ae){
			throw new BpmException(I18nUtil.getMessageProperty("error.startProcessFailed"));			
		}				

	}
	
	/**
	 * {@inheritDoc getVariablesForInstance}
	 */
	public Map<String, Object>getVariablesForInstance(String processInstanceId){
		Map<String, Object>variables = new TreeMap<String, Object>();
		try{
			variables =  runtimeService.getVariables(processInstanceId);
			return variables;
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}		
	}
	
	private Map<String, Object>getVariablesMap(List<HistoricVariableInstance>variables){
		Map<String, Object>variablesMap=new HashMap<String, Object>();
		for (HistoricVariableInstance variable : variables) {
			variablesMap.put(variable.getVariableName(), variable.getValue());
		}
		return variablesMap;
	}
	
	private Map<String, List<HistoricVariableInstance>> getVariablesInstanceWise(List<HistoricVariableInstance>variables){
			  Map<String, List<HistoricVariableInstance>> propertiesMap = new HashMap<String, List<HistoricVariableInstance>>();
			  for (HistoricVariableInstance variable : variables) {		  
					  String procInstanceId = variable.getProcessInstanceId();
					  if(propertiesMap.containsKey(procInstanceId)){
						  List<HistoricVariableInstance>varProperties=propertiesMap.get(procInstanceId);
						  varProperties.add(variable);
						  propertiesMap.put(procInstanceId, varProperties);
					  }else{
						  List<HistoricVariableInstance>varProperties=new ArrayList<HistoricVariableInstance>();
						  varProperties.add(variable);
						  propertiesMap.put(procInstanceId, varProperties);
					  }		  
				  
			  }
			  return propertiesMap;
		  }
	
	public Map<String, Object>getHistoricVariablesForInstance(String processInstanceId){
		List<HistoricVariableInstance>variables = new ArrayList<HistoricVariableInstance>();
		try{
			variables =  runtimeService.getHistoricVariableInstances(processInstanceId);
			return getVariablesMap(variables);
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}		
	}
	
	public List<Object[]> getNewsItemForHomePage(){
		return processDao.getNewsItemForHomePage();
	}
	
	public Map<String, Object>getPrevHistoricVariablesForInstance(String processInstanceId, String currentTaskId)throws BpmException{
		List<HistoricVariableInstance>variables = new ArrayList<HistoricVariableInstance>();
		try{
			variables =  runtimeService.getPrevHistoricVariableInstances(processInstanceId, currentTaskId);
			if(variables!=null && variables.size()>0){
				return getVariablesMap(variables);
			}else{
				  throw new BpmException(I18nUtil.getMessageProperty("error.previousTaskFormValuesNotFound")+" Task Id:"+currentTaskId);				
			}
			
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}		
	}
	
	public Map<String, Object>getHistoricVariablesForInstance(String processInstanceId, String taskId)throws BpmException{
		List<HistoricVariableInstance>variables = new ArrayList<HistoricVariableInstance>();
		try{
			variables =  runtimeService.getHistoricVariableInstancesByTaskId(processInstanceId, taskId);
			if(variables!=null && variables.size()>0){
				return getVariablesMap(variables);
			}else{
				return null;
			}
			
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}		
	}
	
	public List<ProcessDefinition> getProcessDefinitionsByName(String name){
		if(StringUtil.isEmptyString(name)){
			  throw new BpmException(I18nUtil.getMessageProperty("error.nullProcessName"));				
		}
		return repositoryService.createProcessDefinitionQuery().processDefinitionName(name).activeVersion().list();
	}
	
	public List<String> getProcessDefinitionIdsByName(String name)throws BpmException{
		List<String>processDefIds = new ArrayList<String>();
		List<ProcessDefinition>processes = getProcessDefinitionsByName(name);
		for (ProcessDefinition processDefinition : processes) {
			processDefIds.add(processDefinition.getId());
		}
		return processDefIds;
	}
	
	
	
	public Map<String, List<HistoricVariableInstance>>getAllVariableValuesByProcess(String processName)throws BpmException{
		List<HistoricVariableInstance>variables = new ArrayList<HistoricVariableInstance>();
		try{
			List<String>processDefinitionIds = getProcessDefinitionIdsByName(processName);
			List<String>processInstanceIds = new ArrayList<String>();			
			List<HistoricProcessInstance>historicProcessInstances = getHistoricProcessInstancesByDefIds(processDefinitionIds);
			if(historicProcessInstances!=null&&historicProcessInstances.size()>0){
				for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
					processInstanceIds.add(historicProcessInstance.getId());
				}
				variables = runtimeService.getHistoricVariableInstances(processInstanceIds);
				
				if(variables!=null && variables.size()>0){
					return getVariablesInstanceWise(variables);
				}else{
					  throw new BpmException(I18nUtil.getMessageProperty("error.noInputValues")+" Process Name:"+processName);				
				}
			}else{
				  throw new BpmException(I18nUtil.getMessageProperty("error.noInstanceFound")+" Process Name:"+processName);				
			}
			
			
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}		
	}
	
	/**
	 * {@inheritDoc getProcessInstance}
	 */
	public List<HistoricProcessInstance> getProcessInstance(String processInstanceId) {
		try{
			//return runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			return historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).list();

		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}	   
	}
	
	public List<HistoricProcessInstance> getHistoricProcessInstancesByDefIds(List<String> processDefinitionIds) {
		try{
			return historyService.createHistoricProcessInstanceQuery().processDefinitionIds(processDefinitionIds).list();
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}	   
	}
	
	/**
	 * {@inheritDoc deleteProcessInstance}
	 */
	public void deleteProcessInstance(String processInstanceId ,List<String> processInstanceIds, String deleteReason){
		try{
			if(processInstanceIds != null) {
				runtimeService.deleteMultipleProcessInstance(processInstanceIds,deleteReason);
			} else {
				runtimeService.deleteProcessInstance(processInstanceId,deleteReason);
			}
			
		}catch(ActivitiException ae){
			throw new BpmException("Error while Delete process Instance"+ae.getMessage(), ae);
		}
	}
	
	/**
	 * {@inheritDoc deleteProcessDefinition}
	 */
	public boolean deleteProcessDefinition(String processDefinitionId, boolean cascade)throws BpmException{
		repositoryService.deleteProcessDefinitionFromCache(processDefinitionId);
		processDefinitionService.deleteProcessDefinitionById(processDefinitionId, cascade);
		return true;	
	}
	
	/**
	 * {@inheritDoc deleteAllSelectedProcessDefinition}
	 */
	public boolean deleteAllSelectedProcessDefinition(List<String> processDefinitionIds, boolean cascade)throws BpmException {
		for(String processDefinitionId : processDefinitionIds){
			repositoryService.deleteProcessDefinitionFromCache(processDefinitionId);
			processDefinitionService.deleteProcessDefinitionById(processDefinitionId,cascade);
        }		
		return true;
	}
	
	/**
	 * {@inheritDoc deleteAllProcessDefinitionVersions}
	 */
	public boolean deleteAllProcessDefinitionVersions(String key, boolean cascade)throws BpmException{
		List<ProcessDefinition>processDefinitions = processDefinitionService.getAllProcessDefinitionVersions(key);
		repositoryService.deleteProcessDefinitionsFromCache(processDefinitions);
		processDefinitionService.deleteProcessDefinitions(processDefinitions, cascade);
		return true;
	}
	
	public boolean deleteAllProcessDefinition(List<String> keys, boolean cascade)throws BpmException{
		List<ProcessDefinition>processDefinitions = processDefinitionService.getAllProcessDefinitionByKeys(keys);

		repositoryService.deleteProcessDefinitionsFromCache(processDefinitions);

		processDefinitionService.deleteProcessDefinitions(processDefinitions, cascade);
		 StringBuffer ids = new StringBuffer();
		 ids.append("(");
		 for(String key:keys){
			 ids.append("'"+key+"',");
		 }
		 
		 if(ids.lastIndexOf(",")>0){
			 ids.deleteCharAt(ids.lastIndexOf(","));
		}
		 ids.append(")");
		 
		 formDefintionService.deleteFormNameByProcessKey(ids.toString());
		 
		return true;
	}
	/**
	 * {@inheritDoc getAllProcessInstance}
	 */
	public Map<String,Object> getAllProcessInstance(String processDefinitionId)throws BpmException{
		Map<String,Object> proInsStatus = new HashMap<String, Object>();
		 int successCount = (int) historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).finished().count();
		 int inProgCount = (int) runtimeService.createProcessInstanceQuery().processDefinitionId(processDefinitionId).active().count();
		 int deletedCount = (int) historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).deleted().count();
		 int terminateCount = (int) historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).terminated().count();
		 int suspendedCount = (int) runtimeService.createProcessInstanceQuery().processDefinitionId(processDefinitionId).suspended().count();
		 proInsStatus.put("Success", successCount);
		 proInsStatus.put("Deleted", deletedCount);
		 proInsStatus.put("InProgress", inProgCount);
		 proInsStatus.put("Terminated", terminateCount);
		 proInsStatus.put("suspendedCount", suspendedCount);
		 proInsStatus.put("processDefinitionId", processDefinitionId);
		 return proInsStatus;
	}
	
	/**
	 * {@inheritDoc getAllMyProcessInstance}
	 */
	public Map<String,Object> getAllMyProcessInstance(String processDefinitionId,String loggedInUser)throws BpmException{
		Map<String,Object> proInsStatus = new HashMap<String, Object>();
		 int successCount = (int) historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).startedBy(loggedInUser).finished().count();
		 int inProgCount = (int) historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).startedBy(loggedInUser).activated().count();
		 int deletedCount = (int) historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).startedBy(loggedInUser).deleted().count();
		 int terminateCount = (int) historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).startedBy(loggedInUser).terminated().count();
		 int suspendedCount = (int) historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).startedBy(loggedInUser).suspended().count();
		 proInsStatus.put("Success", successCount);
		 proInsStatus.put("Deleted", deletedCount);
		 proInsStatus.put("InProgress", inProgCount);
		 proInsStatus.put("Terminated", terminateCount);
		 proInsStatus.put("suspendedCount", suspendedCount);
		 proInsStatus.put("processDefinitionId", processDefinitionId);
		 return proInsStatus;
	}
	
	
	/**
	 * {@inheritDoc getAllProcessInstnaceByStauts}
	 */
	public List<HistoricProcessInstance> getAllProcessInstnaceByStauts(String processDefinitionId, String status) throws BpmException {
		List<HistoricProcessInstance> processInsList = new ArrayList<HistoricProcessInstance>();
		if (status.equalsIgnoreCase("success")) {
			processInsList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).finished().list();
		} else if (status.equalsIgnoreCase("inProgress")) {
			processInsList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).unfinished().list();
		} else if (status.equalsIgnoreCase("terminated")) {
			processInsList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).terminated().list();
		} else if (status.equalsIgnoreCase("activated")) {
			processInsList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).activated().list();
		} else if (status.equalsIgnoreCase("suspended")) {
			processInsList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).suspended().list();
		} else if (status.equalsIgnoreCase("totalInstances")) {
			processInsList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).list();
		} else {
			processInsList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).deleted().list();
		}
		return processInsList;
	}
	
	/**
	 * {@inheritDoc getAllMyProcessInstnaceByStauts}
	 */
	public List<HistoricProcessInstance> getAllMyProcessInstnaceByStauts(String processDefinitionId, String status, String loggedInUser) throws BpmException {
		List<HistoricProcessInstance> processInsList = new ArrayList<HistoricProcessInstance>();
		if (status.equalsIgnoreCase("success")) {
			processInsList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).startedBy(loggedInUser).finished().list();

		} else if (status.equalsIgnoreCase("inProgress")) {
			processInsList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).startedBy(loggedInUser).unfinished().list();

		} else if (status.equalsIgnoreCase("terminated")) {
			processInsList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).startedBy(loggedInUser).terminated().list();
		} else if (status.equalsIgnoreCase("activated")) {
			processInsList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).startedBy(loggedInUser).activated().list();
		} else if (status.equalsIgnoreCase("suspended")) {
			processInsList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).startedBy(loggedInUser).suspended().list();
		} else if (status.equalsIgnoreCase("totalInstances")) {
			processInsList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).startedBy(loggedInUser).list();
		} else {
			processInsList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).startedBy(loggedInUser).deleted().list();
		}
		return processInsList;
	}
	
	/**
	 * {@inheritDoc getResourceAsStream}
	 */
	public InputStream getResourceAsStream(String deploymentId, String resourceName) {
		InputStream xmlStream = null;
		try {
			xmlStream = repositoryService.getResourceAsStream(deploymentId,resourceName);
		}catch(ActivitiException ae){
			throw new BpmException("Error while getting resource stream "+ae.getMessage(),ae);
		}
		return xmlStream;
	}
	
	/**
	 * {@inheritDoc getResourceAsEntity}
	 */
	public ResourceEntity getResourceAsEntity(String deploymentId, String resourceName) {		
		try {
			return repositoryService.getResourceAsEntity(deploymentId,resourceName);
		}catch(ActivitiException ae){
			throw new BpmException("Error while getting resource "+ae.getMessage(),ae);
		}
	}
	
	/**
	 * {@inheritDoc getResourceListAsStream}
	 */
	public Map<String,Map<String,InputStream>> getResourceListAsStream(List<String> deploymentIds){
		Map<String,Map<String,InputStream>> xmlStream = null;
		try {
			xmlStream = repositoryService.getResourceListAsStream(deploymentIds);
		}catch(ActivitiException ae){
			throw new BpmException("Error while download xml "+ae.getMessage(),ae);
		}
		return xmlStream;
	}
	
	/**
	 * {@inheritDoc getResourceAsStream}
	 */
	public String getSvgString(String deploymentId, String resourceName) {
		String svgString = null;
		try {
			svgString = repositoryService.getSvgString(deploymentId,resourceName);
		}catch(ActivitiException ae){
			throw new BpmException("Error while getting resource stream "+ae.getMessage(),ae);
		}
		return svgString;
	}

	/**
	 * {@inheritDoc getResourceListAsStream}
	 */
	public void terminateExecutionByInstanceId(String processInstanceId,Date currentDate){
		try {
			runtimeService.terminateExecutionByInstanceId(processInstanceId,currentDate);
		}catch(ActivitiException ae){
			throw new BpmException("Error while Terminate the Process Instnace"+ae.getMessage(),ae);
		}
	}
	
	/**
	 * {@inheritDoc getResourceListAsStream}
	 */
	public void terminateExecutionsByInstanceIds(List<String> processInstanceIds,Date currentDate){
		try {
			runtimeService.terminateExecutionsByInstanceIds(processInstanceIds,currentDate);
		}catch(ActivitiException ae){
			throw new BpmException("Error while Terminating the Process Instnaces"+ae.getMessage(),ae);
		}
	}
	
	/**
	 * {@inheritDoc suspendProcessInstanceById}
	 */
	public void suspendProcessInstanceById ( String processInstanceId){
		try{
				repositoryService.suspendProcessInstanceById(processInstanceId);
		}catch(ActivitiException ae){
			throw new BpmException("Error while suspend process instance", ae);
		}
	}
	
	
	public List<FormProperty> getTaskFormValuesByTaskId(String processInstanceId)throws BpmException{
		try{			
			List<FormProperty> formPropertiesFinal = new ArrayList<FormProperty>();
			Map<String, Object> variables = CommonUtil.getStringRepresentations(getVariablesForInstance(processInstanceId));
			List<HistoricTaskInstance>taskHistoryInstances = rtTaskService.getHistoricTaskInstances(processInstanceId);
			Map<HistoricTaskInstance, List<FormProperty>>historyDetails = new HashMap<HistoricTaskInstance, List<FormProperty>>();
			
			for (HistoricTaskInstance historicTaskInstance : taskHistoryInstances) {
				
				if(taskHistoryInstances.size() == 1){
					List<FormProperty> startFormProperties = formDefintionService.getStartFormData(historicTaskInstance.getProcessDefinitionId()).getFormProperties();
					for (FormProperty formProperty : startFormProperties) {
						formProperty.setValue((String)variables.get(formProperty.getName()));
						formPropertiesFinal.add(formProperty);
					}
				}else{
					List<FormProperty> taskFormProperties = rtTaskService.getHistoricTaskFormData(historicTaskInstance).getFormProperties();
					for (FormProperty formProperty : taskFormProperties) {
						formProperty.setValue((String)variables.get(formProperty.getName()));
						formPropertiesFinal.add(formProperty);
					}
				}				

				
			}
			return formPropertiesFinal;					
		}catch(ActivitiException ae){
			throw new BpmException("Error while getting form values for process instance : "+processInstanceId, ae);
		}	
	}
	
	public List<Classification> getClassifications(){
		return processDao.getClassifications();
	}
	
	public Classification getClassificationById(String id){
		return processDao.getClassificationById(id);
	}
	
	public void removeClassifications(List<String> cids, HttpServletRequest request){
		List<Classification> list=processDao.getClassificationsByIds(cids);
		for (Classification c : list) {
			processDao.deleteClassification(c);
		}
		refreshClassification(request);
	}
	
	public boolean updateClassificationOrderNos(List<Map<String, Object>> classificationList,HttpServletRequest request){
		boolean hasUpdated = false;
 		for (Map<String, Object> classification : classificationList) {
 			hasUpdated = processDao.updateClassificationOrderNos(
					(String) classification.get("classificationId"),
					Integer.parseInt((String)classification.get("orderNo")));
 		}
 		
 		refreshClassification(request);
		return hasUpdated;
	}
	
	public Classification saveClassification(Classification classification, HttpServletRequest request) throws BpmException {

		if(StringUtils.isBlank(classification.getId())){
			classification.setOrderNo(generateOrderNo());
		}
		processDao.saveClassification(classification);
		
		refreshClassification(request);

		log.info("Classification added successfully");
		return classification;
	}

	private void refreshClassification(HttpServletRequest request) {
		request.getSession().getServletContext().setAttribute(Constants.PROCESS_CLASSIFICATION, lookupManager.getAllProcessClassifications());
	}
	
	public int generateOrderNo(){
		List<Classification> list = processDao.getClassifications();
		if(list != null && !list.isEmpty()) {
			Collections.sort(list, new ClassificationComparator());
			int maxOrderNo = list.get(list.size() - 1).getOrderNo();
			return maxOrderNo + 1;
		}else {
			return Constants.DEFAULT_ORDER_NO;
		}
	}
	
    public boolean isClassificationExist(String classification) throws BpmException{
    	return processDao.isClassificationExist(classification);
	}
    /**
	 * {@inheritDoc getAllProcessList}
	 */
    public List<Process> getAllProcessList(Map<String, Object> searchParams) throws BpmException{
    	StringBuffer whereParams = new StringBuffer();
    	if(searchParams != null && !(searchParams.isEmpty())){
    		if(searchParams.get("processName") != null && !(String.valueOf(searchParams.get("processName")).isEmpty())){
    			if(String.valueOf(searchParams.get("isAdvancedSearch")).equals("true")){
    				whereParams.append("and process.name = '"+String.valueOf(searchParams.get("processName"))+"'");
    			}else{
    				whereParams.append("and process.name like '"+String.valueOf(searchParams.get("processName"))+"%'");
    			}
    		}
    	}
    	//System.out.println("whereparams =================== "+whereParams.toString());
    	return processDao.getAllProcess(whereParams.toString());
    }
    
    /**
	 * {@inheritDoc getAllProcessAsLabelValue}
	 */
    public List<LabelValue> getAllProcessAsLabelValue() throws BpmException{
    	return processDao.getAllProcessAsLabelValue();
    }
    
    /**
	 * {@inheritDoc searchProcessNames}
	 */
    
    public List<LabelValue> searchProcessNames(String processName)throws BpmException{
    	return processDao.searchProcessNames(processName);
    }
    /**
     * {@inheritDoc getAllProcessBySystemDefined}
     */
     public List<LabelValue> getAllProcessBySystemDefined(boolean isSystemDefined) throws Exception {
    	 return processDao.getAllProcessBySystemDefined(isSystemDefined);
     }
    
    /**
	 * {@inheritDoc getProcessByIds}
	 */
    public List<Process> getProcessByIds(List<String> ids)throws EazyBpmException{
    	return processDao.getProcessByIds(ids);
    }
	
	/**
	 * {@inheritDoc activateProcessInatanceById}
	 */
	public void activateProcessInatanceById ( String processInstanceId){
		try{
				repositoryService.activateProcessInstanceById(processInstanceId);
		}catch(ActivitiException ae){
			throw new BpmException("Error while suspend process instance", ae);
		}
	}
	
	public StartFormData getStartFormData(String processDefinitionId)throws BpmException {
		return formDefintionService.getStartFormData(processDefinitionId);
	}
	
	public Map<String, Object> designStartProcessScript(String processId, VelocityEngine velocityEngine, String formAction,boolean isSytemDefined,String processInstanceId,boolean isFromDoneList)throws BpmException{
            ProcessDefinition processDefinition = null;
            processDefinition = processDefinitionService.getProcessDefinitionById(processId);            
            return designStartProcessScript(processDefinition, velocityEngine, formAction,isSytemDefined,processInstanceId,isFromDoneList, null);
    }
	
	public Map<String, Object> designStartProcessScript(ProcessDefinition processDefinition, VelocityEngine velocityEngine, String formAction,boolean isSytemDefined,String processInstanceId,boolean isFromDoneList, Map<String, Object> permissionValues)throws BpmException{
        if(processDefinition==null){
        	throw new BpmException("No Process Definition provided");
        }
        Map<String, Object> context = new HashMap<String, Object>();
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) processDefinition;
        ModelMap model = new ModelMap();
		// Get end script values in start node
		/*if(processDefinitionEntity.getEndScriptNameExpression() != null && processDefinitionEntity.getEndScriptExpression() != null){
			Map<String,String> endScriptContent = new HashMap<String,String>();
			endScriptContent.put("endScriptName", processDefinitionEntity.getEndScriptNameExpression().toString());
			endScriptContent.put("endScript", processDefinitionEntity.getEndScriptExpression().toString());
			 model = setEndEventScript(model,endScriptContent);
			 context.put("isEndScriptExist", model.get("isEndScriptExist"));
			 context.put("endScript",model.get("endScript"));
			 context.put("endScriptName", model.get("endScriptName"));
		} else {
			context.put("endScriptName", "");
		}*/
		if(permissionValues != null){
			context.putAll(permissionValues);
		}
		
		JSONObject pastValuesJson = new JSONObject();
		String isForRecall = "notApplicable";
		String activeTaskId = null;
        context.put("processDefinition", processDefinition);
        context.put("processDefinitionId", processDefinition.getId());
        context.put("formAction", formAction);
        context.put("userId", CommonUtil.getLoggedInUserId());
		context.put("username", CommonUtil.getLoggedInUserName());
		context.put("isSystemDefined", isSytemDefined);
		context.put("processInstanceId", processInstanceId);
		context.put("resourceName", processDefinition.getResourceName());
        context.put("processCreatedDate",processDefinitionService.getProcessCreatedDateForDeplymentId(processDefinition.getDeploymentId()));
        StartFormData startForm = getStartForm(processDefinition.getId());
        String formKey = startForm.getFormKey();
        if(formKey!=null){
			MetaForm form = formDefintionService.getDynamicFormById(formKey);
			if(form!=null){
				String html = form.getHtmlSource();
				context.put("html", html);		
				context.put("formId", form.getFormName());	
				context.put("formUniqueId", form.getId());
				Map<String, FormFieldPermission> nodeLevelFieldPermissions = rtTaskService.getStartNodeFieldPermission(processDefinition.getId());
				context.put("nodeLevelFieldPermissions", nodeLevelFieldPermissions);
				if(!processInstanceId.equalsIgnoreCase("null")){
					//get and pastValues with the same form
					Map<String, Object> properties = CommonUtil.getStringRepresentations(getHistoricVariablesForInstance(processInstanceId, null));
					context.put("properties", properties);
					try {
						pastValuesJson = CommonUtil.convertMapToJson(properties);
					} catch (JSONException e) {
						throw new BpmException("exception occured while generating json with last entered values for the processInstanceId "+processInstanceId);
					}
	            	/*if(isFromDoneList){
						//for recall functionality until the next task end
						List<HistoricTaskInstance> historicTaskInstances = rtTaskService.getHistoricTaskInstances(processInstanceId);
						for(HistoricTaskInstance historicTaskInstance : historicTaskInstances){
							//For checking the next task has been ended
							if(historicTaskInstance.getEndTime() != null){
								isForRecall = "no";
								break;
							}else {
								isForRecall = "yes";
								activeTaskId = historicTaskInstance.getId();
								break;
							}
						}
						//end
	            	}*/
				}
			}else{
				throw new BpmException("Invalid form id binded "+formKey);
			}
			context.put("isForRecall", isForRecall);
			context.put("activeTaskId", activeTaskId);
			context.put("pastValuesJson", pastValuesJson);
			context.put("isCreator", true);
        }else{
        	context.put("html", "No form specified, click complete to start the process");
        	throw new BpmException("No form binded to task");
        }            
        return context;
	}
	
	public Map<String, Object> designUpdateStartProcessScript(String processId, Map<String, Object>formValues, VelocityEngine velocityEngine)throws BpmException{
        ProcessDefinition processDefinition = processDefinitionService.getProcessDefinitionById(processId);
		if(processDefinition==null){
        	throw new BpmException("No Process Definition provided");
        }
		Map<String, Object> context = new HashMap<String, Object>();
        context.put("processDefinition", processDefinition);
        context.put("processDefinitionId", processDefinition.getId());
        context.put("formAction", "bpm/form/submitStartForm");
        context.put("userId", CommonUtil.getLoggedInUserId());
		context.put("username", CommonUtil.getLoggedInUserName());
        context.put("processCreatedDate",processDefinitionService.getProcessCreatedDateForDeplymentId(processDefinition.getDeploymentId()));
        
        StartFormData startForm = getStartForm(processDefinition.getId());
        String formKey = startForm.getFormKey();
        if(formKey!=null){
			MetaForm form = formDefintionService.getDynamicFormById(formKey);
			if(form!=null){
				context.put("html", form.getHtmlSource());		
				context.put("formId", form.getFormName());
				context.put("formUniqueId", form.getId());
			}else{
				throw new BpmException("Invalid form id binded "+formKey);
			}
        }else{
        	context.put("html", "No form specified, click update to update the form");
        	throw new BpmException("No form binded to task");
        }            
        return context;
	}
	
	public Map<String, Object> designTaskProcessScriptContextForReader( HistoricTaskInstance task, Map<String, Object>formValues,ProcessDefinition processDefinition, 
			VelocityEngine velocityEngine, String formAction,boolean isRead,ModelMap model)throws BpmException{
        //ProcessDefinition processDefinition = processDefinitionService.getProcessDefinitionById(currentTask.getProcessDefinitionId());
		if(processDefinition==null){
        	throw new BpmException("No Process Definition provided");
        }
		Map<String, Object> context = new HashMap<String, Object>();
		JSONObject pastValuesJson = new JSONObject();
		TaskFormData taskFormData = rtTaskService.getHistoricTaskFormData(task);
		String formKey = taskFormData.getFormKey();
		context.put("isFormReadOnly", "true");
		if(formKey!=null){
			MetaForm form = formDefintionService.getDynamicFormById(formKey);	
			if(form!=null){
				context.put("html", form.getHtmlSource());		
				context.put("formId", form.getFormName());
				model.addAttribute("formId", form.getFormName());
				context.put("formUniqueId", form.getId());
			}else{
				throw new BpmException("Invalid form id binded "+formKey);
			}

            OperatingFunction operatingFunction = new OperatingFunctionImpl();
			context.put("taskId", task.getId());	
			context.put("properties", formValues);
			try {
				pastValuesJson = CommonUtil.convertMapToJson(formValues);
			} catch (JSONException e) {
				throw new BpmException("exception occured while generating json with last entered values for the task "+task.getId());
			}
			context.put("instanceId", task.getProcessInstanceId());
			context.put("processDefinitionId", task.getProcessDefinitionId());
			context.put("processDefinition", processDefinition);        
			context.put("userId", CommonUtil.getLoggedInUserId());
			context.put("username", CommonUtil.getLoggedInUserName());
			context.put("processCreatedDate","");
			context.put("taskId", task.getId());		
			context.put("operatingFunction", operatingFunction);
			context.put("showReadButton", isRead);
			context.put("pastValuesJson", pastValuesJson);
		}else{
        	context.put("html", "No form specified, click update to update the form");
        	throw new BpmException("No form binded to task");
        }            
        return context;
	}
	
	/**
	 * {@inheritDoc designUpdateTaskProcessScriptContext}
	 */
	public Map<String, Object> designUpdateTaskProcessScriptContext(Task currentTask, HistoricTaskInstance task, Map<String, Object>formValues,ProcessDefinition processDefinition, VelocityEngine velocityEngine, String formAction,ModelMap model, boolean isStartNodeTask, String creator)throws BpmException{
        //ProcessDefinition processDefinition = processDefinitionService.getProcessDefinitionById(currentTask.getProcessDefinitionId());
		if(processDefinition==null){
			  throw new BpmException(I18nUtil.getMessageProperty("error.noProcessDefinition"));
        }
		User user = CommonUtil.getLoggedInUser();
		Map<String, Object> context = new HashMap<String, Object>();
		JSONObject pastValuesJson = new JSONObject();
		String formKey = rtTaskService.getHistoricTaskFormData(task).getFormKey();
		
		context.put("businessKey", model.get("businessKey"));
		context.put("lastOperationPerformed", model.get("lastOperationPerformed"));
    	String subFormCount = null;
    	subFormCount = (String) formValues.get("subFormCount"); // setting initial subForm count to 1.
    	if(subFormCount != null) {
    		context.put("subFormCount", Integer.valueOf(subFormCount));
    	} else {
    		context.put("subFormCount", "1");
    	}
		boolean isSubForm = false;
	//	model.addAttribute("subProcessExecution", task.getExecution().getProcessDefinition());
		if(formKey!=null){
				MetaForm form = formDefintionService.getDynamicFormById(formKey);	
				if(form!=null){
					context.put("htmlViewSource", form.getHtmlSourceView());	
					context.put("formId", form.getFormName());
					model.addAttribute("formId",form.getFormName());
					context.put("formUniqueId", form.getId());
				}else{
					  throw new BpmException(I18nUtil.getMessageProperty("error.invalid.formId")+" Form Id : "+formKey);
				}
				context.put("properties", formValues);
				context.put("isFormReadOnly", formValues.get("isFormReadOnly"));
				
				try {
					pastValuesJson = CommonUtil.convertMapToJson(formValues);
	
	
				} catch (JSONException e) {
					  throw new BpmException(I18nUtil.getMessageProperty("error.jsonException")+" Task Id : "+formKey);
				}
				context.put("pastValuesJson", pastValuesJson);
				context.put("taskId", task.getId());	
				context.put("processInstanceId", task.getProcessInstanceId());
				if(currentTask != null){
					TaskEntity entity = (TaskEntity) currentTask;
					if(entity.getHtmlSourceForSubForm() != null && !entity.getHtmlSourceForSubForm().isEmpty()) {
						if(entity.getFormkey().equalsIgnoreCase(form.getFormName())) {
							context.put("htmlSubForm", entity.getHtmlSourceForSubForm());
							context.put("subForm", true);
							isSubForm = true;
						}
					} 
					if(!isSubForm){
						context.put("html", form.getHtmlSource());
					}
					TaskRole taskRole = rtTaskService.getTaskRoleApplicableForUser(user, currentTask.getId());
					Map<String, FormFieldPermission>nodeLevelFieldPermissions = rtTaskService.getNodeLevelFieldPermissions(CommonUtil.getLoggedInUser(), currentTask.getId(), false); 
					OperatingFunction operatingFunction = rtTaskService.getOperatingFunctionForUser(user, currentTask, taskRole);
					OperatingFunction operatingFunctionForTask = rtTaskService.getOperatingFunction(user, currentTask);
					context.put("operatingFunction", operatingFunction);
					context.put("operatingFunctionForTask", operatingFunctionForTask);
					context.put("isStartNodeTask", entity.getIsForStartNodeTask());
					model.put("isStartNodeTask", entity.getIsForStartNodeTask());
					//Show the submit button only for creator of default task
					if(isStartNodeTask){
						if(creator != null && !creator.isEmpty()){
							if(!creator.equalsIgnoreCase(CommonUtil.getLoggedInUserId())){
								context.put("isCreator", false);
							}
						}else{
							context.put("isCreator", true);
						}
					}
					context.put("isFinalTransactor", rtTaskService.isFinalTransactor(currentTask.getId()));
					context.put("processCreatedDate",processDefinitionService.getProcessCreatedDateForDeplymentId(processDefinition.getDeploymentId()));
					context.put("resourceName", processDefinition.getResourceName());
					
					//adding current process instance id in context
					context.put("processInstanceId", currentTask.getProcessInstanceId());
					context.put("deploymentId", processDefinition.getDeploymentId());
					context.put("processName",  processDefinition.getName());
					context.put("processDefinitionId",  processDefinition.getId());
					context.put("activityId", currentTask.getTaskDefinitionKey());
					context.put("executionId",task.getExecutionId());
					context.put("taskName", currentTask.getName()); // to show task name in opinion list		
					context.put("nodeLevelFieldPermissions", nodeLevelFieldPermissions);
					// get scripts in process
					TaskEntity taskEntity = (TaskEntity) taskDefinitionService.getTaskById(formValues.get("taskId").toString());
					if(taskEntity != null) {
						context = rtTaskService.getScriptsInProcess(taskEntity,context);
					}
	//			context.put("properties", formValues);
	//			context.put("isFormReadOnly", formValues.get("isFormReadOnly"));
	
				context.put("formAction", formAction);	
				context.put("instanceId", task.getProcessInstanceId());
				context.put("processDefinitionId", task.getProcessDefinitionId());
				context.put("processDefinition", processDefinition);        
				context.put("userId", CommonUtil.getLoggedInUserId());
				context.put("username", CommonUtil.getLoggedInUserName());
				JSONArray formFieldAuditValues= rtTaskService.getFormFieldTraceData(task.getProcessInstanceId(), form.getId());
				model.addAttribute("formFieldAuditValues",formFieldAuditValues);
			} else {
				/*htmlSubForm = task.getExecution().getHtmlSourceForSubForm();
				if(htmlSubForm != null) {
					if(task.getExecution().getFormkey().equalsIgnoreCase(formKey)) {
						context.put("htmlSubForm", htmlSubForm);
					}
				} else {*/
					context.put("html", form.getHtmlSource());
				//}
			}
		}else{
        	context.put("html", "No form specified, click update to update the form");
  			throw new BpmException(I18nUtil.getMessageProperty("error.form.notFound")+" ProcessDefinition Id : "+processDefinition);      			
        }            
        return context;
	}
	
	public Map<String, Object> designUpdateOlderTaskProcessScript(HistoricTaskInstance task, Map<String, Object>formValues, VelocityEngine velocityEngine, String formAction)throws BpmException{
        ProcessDefinition processDefinition = processDefinitionService.getProcessDefinitionById(task.getProcessDefinitionId());
		if(processDefinition==null){
			throw new BpmException(I18nUtil.getMessageProperty("error.noProcessDefinition")+" ProcessDefinition Id : "+processDefinition);
		}
		Map<String, Object> context = new HashMap<String, Object>();
		JSONObject pastValuesJson = new JSONObject();
		HistoricTaskInstance prevTask = rtTaskService.getHistoricTaskInstanceByTaskId(task.getId());			
		TaskFormData taskFormData = rtTaskService.getHistoricTaskFormData(prevTask);
		String formKey = taskFormData.getFormKey();
		if(formKey!=null){
			MetaForm form = formDefintionService.getDynamicFormById(formKey);	
			if(form!=null){
				context.put("html", form.getHtmlSource());		
				context.put("formId", form.getFormName());
				context.put("formUniqueId", form.getId());
			}else{
				  throw new BpmException(I18nUtil.getMessageProperty("error.invalid.formId")+" Form Id : "+formKey);
			}
			context.put("taskId", task.getId());	
			context.put("properties", formValues);
			try {
				pastValuesJson = CommonUtil.convertMapToJson(formValues);
			} catch (JSONException e) {
				  throw new BpmException(I18nUtil.getMessageProperty("error.jsonException")+" Task Id : "+formKey);				
			}
			context.put("formAction", "formAction");	
			context.put("instanceId", task.getProcessInstanceId());
			context.put("processDefinitionId", task.getProcessDefinitionId());
			context.put("processDefinition", processDefinition);        
			context.put("userId", CommonUtil.getLoggedInUserId());
			context.put("username", CommonUtil.getLoggedInUserName());
			context.put("pastValuesJson", pastValuesJson);
			context.put("processCreatedDate",processDefinitionService.getProcessCreatedDateForDeplymentId(processDefinition.getDeploymentId()));
        }else{
        	context.put("html", "No form specified, click update to update the form");
  			throw new BpmException(I18nUtil.getMessageProperty("error.form.notFound")+" ProcessDefinition Id : "+processDefinition);      			
        }            
        return context;
	}
	
	/**
	 * {@inheritDoc getVariablesForInstanceByTaskId}
	 */
	public List<Map<String, Object>> getVariablesForInstanceByTaskId(String taskId,String processsInsId){
		List<Map<String, Object>> variables = new ArrayList<Map<String, Object>>();
		try{
			variables =  runtimeService.getVariablesByTaskId(taskId,processsInsId);
			return variables;
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(), e);
		}	
	}


	/**
	 * 
	 * @param taskId
	 * @param processInstanceId
	 * @return
	 * @throws BpmException
	 */
	public List<FormProperty> getTaskFormValues(String taskId,String processInstanceId)throws BpmException{
		try{			
			List<FormProperty> formPropertiesFinal = new ArrayList<FormProperty>();
			//Get all the map of values of record for historical variable
			List<Map<String, Object>> variables = getVariablesForInstanceByTaskId(taskId,processInstanceId);
			Map<String,Object> allVariables = new HashMap<String,Object>();
			//Get the form values and create the map for that
			for(Map<String, Object> var : variables){				
				allVariables.put((String) var.get("variableName"), var.get("value"));
			}
			//Get the Historic task instance for given task id
			List<HistoricTaskInstance> taskHistoryInstances = rtTaskService.getHistoricTaskInstancesByTaskId(taskId);
			for (HistoricTaskInstance historicTaskInstance : taskHistoryInstances) {
					List<FormProperty> taskFormProperties = rtTaskService.getHistoricTaskFormData(historicTaskInstance).getFormProperties();
					for (FormProperty formProperty : taskFormProperties) {
						formProperty.setValue((String)allVariables.get(formProperty.getName()));
						formPropertiesFinal.add(formProperty);
					}

				
			}
			return formPropertiesFinal;					
		}catch(ActivitiException ae){
			  throw new BpmException(I18nUtil.getMessageProperty("error.variableInstance.notFound")+" Process InstanceID :"+processInstanceId);
		}	
	}
	
	public void designArchivedProcessForm(TaskEntity task,String resourcePath,String stylePath, String ScriptPath)throws BpmException, EazyBpmException, JDOMException, IOException{
		Map<String, List<Map<String,Object>>> listBoxData = new HashMap<String, List<Map<String,Object>>>();
		Map<String, List<Map<String,Object>>> autoCompleteData = new HashMap<String, List<Map<String,Object>>>();
		Map<String, List<Map<String,Object>>> radioButtonData = new HashMap<String, List<Map<String,Object>>>();
		Map<String, List<Map<String,Object>>> checkBoxData = new HashMap<String, List<Map<String,Object>>>();
		if(task.getProcessDefinitionId() == null){
			throw new BpmException(I18nUtil.getMessageProperty("error.noProcessDefinition")+" ProcessDefinition Id : "+task.getProcessDefinitionId());
        }
		String formKey = null;
		TaskFormData taskFormData = null;
		taskFormData = rtTaskService.getHistoricTaskFormData(task.getId());
		formKey = taskFormData.getFormKey();	
		if(formKey!=null){
			MetaForm form = formDefintionService.getDynamicFormById(formKey);	
			if(form!=null){
				ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).active().list().get(0);
				//for workflow trace image
				String workFlowTrace = getSvgString(processDefinition.getDeploymentId(), processDefinition.getResourceName());//end
				//for getting past values of form
				Map<String,Object> elementValuesMap = rtTaskService.checkAndGetPastValuesForForm(task.getProcessInstanceId(), task.getId(),form.getId());//end
				//for input opinion
				List<Opinion> opinionList = userOpinionService.getOpinion(task.getProcessInstanceId());
				
				//Setting Work flow trace data for grid
				List<Task> tasks = rtTaskService.getWorkFlowTraceData(task.getProcessInstanceId(), null);
				JSONObject valueJson = new JSONObject();
				JSONArray traceJsonArray = new JSONArray();
				JSONObject traceJson = null;
				try {
					for(String key:elementValuesMap.keySet()){
							valueJson.put(key, elementValuesMap.get(key));
					}
					for(Task traceTask:tasks){
						traceJson = new JSONObject();
						traceJson.put("name", traceTask.getName());
						traceJson.put("createdby", traceTask.getAssignee());
						traceJson.put("createdtime", convertDateToSTDString(traceTask.getCreateTime().toString()));
						traceJson.put("operationType", traceTask.getDescription());
						traceJsonArray.put(traceJson);
					}
				} catch (JSONException e) {
					  throw new BpmException(I18nUtil.getMessageProperty("error.jsonException")+" Form Name : "+form.getFormName());					
				}//end
				Map<String, Object> context = new HashMap<String, Object>();
				List<Map<String,String>> formFields = formDefintionService.getFields(form.getId()); 
				for(Map<String,String> fields : formFields){
					if(!StringUtil.isEmptyString(fields.get("datadictionary"))){
						List<Map<String,Object>> dataDictionaryValues = dataDictionaryService.getDictionaryValueByParentId(fields.get("datadictionary"));
						if(fields.get("fieldClass").equals("data_dictionary")){
							listBoxData.put(form.getEnglishName()+"_"+fields.get("name"), dataDictionaryValues);
						} else if(fields.get("fieldClass").equals("autocomplete")){
							autoCompleteData.put(form.getEnglishName()+"_"+fields.get("name"), dataDictionaryValues);
						} else if(fields.get("fieldClass").equals("data_dictionary_radio")){
							radioButtonData.put(form.getEnglishName()+"_"+fields.get("name"), dataDictionaryValues);
						} else if(fields.get("fieldClass").equals("data_dictionary_checkbox")){
							checkBoxData.put(form.getEnglishName()+"_"+fields.get("name"), dataDictionaryValues);
						} 
					}
				}
				context.put("html", form.getHtmlSource());
				context.put("valueJson", valueJson);
				context.put("formId", form.getFormName());
				context.put("formUniqueId", form.getId());
				context.put("workFlowTrace", workFlowTrace);
				context.put("traceJsonArray", traceJsonArray);
				context.put("opinionList", opinionList);
				context.put("listBoxData", new JSONObject(listBoxData));
				context.put("autoCompleteData", new JSONObject(autoCompleteData));
				context.put("radioButtonData", new JSONObject(radioButtonData));
				context.put("checkBoxData", new JSONObject(checkBoxData));
				JSONArray formFieldAuditValues= rtTaskService.getFormFieldTraceData(task.getProcessInstanceId(), form.getId());
				context.put("formFieldAuditValues",formFieldAuditValues);
				String htmlContent = TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showArchivedProcessDetails.vm", context);
				/*encoding for showing chinease archived process. after encode all space changed to "+" */
				String processName = URLEncoder.encode(processDefinition.getName(), "UTF-8"); 
				//processNmae = URLDecoder.decode(processNmae, "ISO8859_1");
				String path= resourcePath + "/root/processArchivalFiles/"+processDefinition.getClassificationId()+"/"+processName.replace("+","_")+"/"+task.getProcessInstanceId();
				File dirPath = new File(path);
		        if (!dirPath.exists()) {
		             dirPath.mkdirs();
		        }
		        //Generate Html file operate page
				FileUtils.writeFile(path+"/operatePage.html", htmlContent); //end
				//style sheets files
				FileUtils.copyAndPasteFile(stylePath+"/lib/bootstrap-2.2.1.min.css",path+"/bootstrap-2.2.1.min.css");
				FileUtils.copyAndPasteFile(stylePath+"/lib/bootstrap-responsive-2.2.1.css",path+"/bootstrap-responsive-2.2.1.css");
				FileUtils.copyAndPasteFile(stylePath+"/easybpm/basicLayout/layout.css",path+"/layout.css");
				FileUtils.copyAndPasteFile(stylePath+"/easybpm/common/common.css",path+"/common.css"); //end
				FileUtils.copyAndPasteFile(ScriptPath+"/lib/jquery-1.7.2.min.js",path+"/jquery-1.7.2.min.js"); //end
				FileUtils.copyAndPasteFile(ScriptPath+"/easybpm/processArchive.js",path+"/processArchive.js"); //end
				//Uploaded file generation in folder
				List<AttachmentEntity> attachments = runtimeService.getAttachmentForTask(task.getId());
				if(attachments.size() > 0){
					for(AttachmentEntity attachment : attachments){
						FileUtils.copyAndPasteFile(attachment.getUrl()+"/"+attachment.getName(), path+"/"+attachment.getName());
					}
				} //end
			}else{
				  throw new BpmException(I18nUtil.getMessageProperty("error.invalid.formId")+" Form Id : "+formKey);				
			}
		}else{
  			throw new BpmException(I18nUtil.getMessageProperty("error.form.notFound")+" ProcessDefinition Id : "+task.getProcessDefinitionId());      			
        }       
	}
	
		public ModelMap setEndEventScript(ModelMap model,Map <String,String> endEventScript){
			String endScript = null;
			String endScriptName = null;
			try{
				endScriptName = (String) java.net.URLDecoder.decode(endEventScript.get("endScriptName"), "UTF-8"); 
				endScript = "<script type='text/javascript'> function " + endScriptName + "{";
				endScript = endScript + (String) endEventScript.get("endScript") + "} </script>";         			 	
				//endScriptName = (String) endEventScript.get("endScriptName");
				 
			       String endScriptContent = endScript.replaceAll("\\+", "plus");
			       endScriptContent = URLDecoder.decode(endScriptContent, "utf-8");
			       String script = endScriptContent.replaceAll("plus","\\+");
			       
				if(endScriptName!=null && !endScriptName.isEmpty()){
					model.addAttribute("isEndScriptExist", "true");
					model.addAttribute("endScript", script);
					model.addAttribute("endScriptName",endScriptName);
	
				}else{
					log.info("NO, End Script Exist for previous Task");
					model.addAttribute("isEndScriptExist", "false");
				}
			}catch(Exception e){
				log.error(e.getMessage(), e);
				throw new BpmException(I18nUtil.getMessageProperty("error.scriptError"));				
			}
			return model;
		}
	
		// generate start scripts in end node
		public ModelMap setEndEventStartScript(ModelMap model,Map <String,String> endEventScript){
			String endScript = null;
			String endScriptName = null;
			try{
				endScriptName = (String) endEventScript.get("endEventStartScriptName");
				endScript = "<script type='text/javascript'> function " + endScriptName + "{";
				endScript = endScript + (String) endEventScript.get("endEventStartScript") + "} "+ endScriptName+";  </script>";           			 	
				endScriptName = (String) endEventScript.get("endEventStartScriptName");
				
			       String endScriptContent = endScript.replaceAll("\\+", "plus");
			       endScriptContent = URLDecoder.decode(endScriptContent, "utf-8");
			       String script = endScriptContent.replaceAll("plus","\\+");
				
				if(endScriptName!=null && !endScriptName.isEmpty()){
					model.addAttribute("isStartScriptExist", "true");
					model.addAttribute("startScriptName", endScriptName);
					model.addAttribute("startScript",script );
	
				}else{
					log.info("NO, End Script Exist for previous Task");
					model.addAttribute("isEndScriptExist", "false");
				}
			}catch(Exception e){
				log.error(e.getMessage(), e);
				throw new BpmException(I18nUtil.getMessageProperty("error.scriptError"));				
			}
			return model;
		}
   
		// get start scripts in task
		 public ModelMap setStartEventScriptForProcessInstance(TaskEntity taskEntity,ModelMap model){
        	 String startScript = null;
             String startScriptName = null;
        	try{
            	if(taskEntity != null){
            		startScriptName = (String) java.net.URLDecoder.decode(taskEntity.getStartScriptName(), "UTF-8"); 
            		startScript = "<script type='text/javascript'> function " + startScriptName +"{";
            		startScript = startScript + taskEntity.getStartScript() + "} "+ startScriptName+";  </script>";  
            		
 			       	String startScriptContent = startScript.replaceAll("\\+", "plus");
 			       	startScriptContent = URLDecoder.decode(startScriptContent, "utf-8");
 			       	String script = startScriptContent.replaceAll("plus","\\+");
            		
            		if(startScriptName!=null && !startScriptName.isEmpty()){
            			 model.addAttribute("isStartScriptExist", "true");
                         model.addAttribute("startScriptName", startScriptName);
                         model.addAttribute("startScript", script);
            		}else{
            			log.info("NO, Start Script Exist for Task Id : -  "+taskEntity.getId());
            			model.addAttribute("isStartScriptExist", "false");
            		}
            	}else{
       			log.info("NO, Task found for Process Instance ID : -  "+taskEntity);
            	}
            
        	}catch(Exception e){
                log.error(e.getMessage(), e);
				throw new BpmException(I18nUtil.getMessageProperty("error.scriptError"));				
            }
        	return model;
        }
		 
	public void designWorkflowInstanceExport(List<Map<String,Object>> processDetailsMap,boolean withTrace,HttpServletResponse response,String stylePath,String scriptPath)throws BpmException, EazyBpmException, JDOMException, IOException{
		 response.setContentType("Content-type: application/zip");
         response.setHeader("Content-disposition", "attachment; filename=\"" + "workFlow.zip"); 
		Map<String, List<Map<String,Object>>> listBoxData = new HashMap<String, List<Map<String,Object>>>();
		Map<String, List<Map<String,Object>>> autoCompleteData = new HashMap<String, List<Map<String,Object>>>();
		Map<String, List<Map<String,Object>>> radioButtonData = new HashMap<String, List<Map<String,Object>>>();
		Map<String, List<Map<String,Object>>> checkBoxData = new HashMap<String, List<Map<String,Object>>>();
		ServletOutputStream sout= response.getOutputStream();
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(sout));
        
        Map<String,String> scriptAndStyleFilePaths = new HashMap<String,String>();
        scriptAndStyleFilePaths.put("/bootstrap-2.2.1.min.css", stylePath+"/lib/bootstrap-2.2.1.min.css");
        scriptAndStyleFilePaths.put("/bootstrap-responsive-2.2.1.css", stylePath+"/lib/bootstrap-responsive-2.2.1.css");
        scriptAndStyleFilePaths.put("/layout.css", stylePath+"/easybpm/basicLayout/layout.css");
        scriptAndStyleFilePaths.put("/common.css", stylePath+"/easybpm/common/common.css");
        scriptAndStyleFilePaths.put("/jquery-1.7.2.min.js", scriptPath+"/lib/jquery-1.7.2.min.js");
        scriptAndStyleFilePaths.put("/processArchive.js", scriptPath+"/easybpm/processArchive.js");
        
		for(Map<String,Object> processDetail : processDetailsMap){
			if(processDetail.get("processDefinitionId") == null){
	        	throw new BpmException("No Process Definition provided");
	        }
			String formKey = null;
			if(processDetail.get("taskId").equals(processDetail.get("processInstanceId"))){
				StartFormData startForm = getStartForm(processDetail.get("processDefinitionId").toString());
				formKey = startForm.getFormKey();
			}else {
				TaskFormData taskFormData = rtTaskService.getHistoricTaskFormData(processDetail.get("taskId").toString());
				formKey = taskFormData.getFormKey();
			}
			if(formKey!=null){
				MetaForm form = formDefintionService.getDynamicFormById(formKey);	
				if(form!=null){
					ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDetail.get("processDefinitionId").toString()).active().list().get(0);
					//for getting past values of form
					Map<String,Object> elementValuesMap = rtTaskService.checkAndGetPastValuesForForm(processDetail.get("processInstanceId").toString(), processDetail.get("taskId").toString(),form.getId());//end
					Map<String, Object> context = new HashMap<String, Object>();
					JSONObject valueJson = new JSONObject();
					try{
						for(String key:elementValuesMap.keySet()){
								valueJson.put(key, elementValuesMap.get(key));
						}
						if(withTrace){
							//for workflow trace image
							String workFlowTrace = getSvgString(processDefinition.getDeploymentId(), processDefinition.getResourceName());//end
							//Setting Work flow trace data for grid
							List<Task> tasks = rtTaskService.getWorkFlowTraceData(processDetail.get("processInstanceId").toString(), null);
							JSONArray traceJsonArray = new JSONArray();
							JSONObject traceJson = null;
							for(Task traceTask:tasks){
								traceJson = new JSONObject();
								traceJson.put("name", traceTask.getName());
								traceJson.put("createdby", traceTask.getAssignee());
								traceJson.put("createdtime", convertDateToSTDString(traceTask.getCreateTime().toString()));
								traceJson.put("operationType", traceTask.getDescription());
								traceJsonArray.put(traceJson);
							}
							context.put("workFlowTrace", workFlowTrace);
							context.put("traceJsonArray", traceJsonArray);
						}
					} catch (JSONException e) {
						  throw new BpmException(I18nUtil.getMessageProperty("errors.jsonException")+" Form Name : "+form.getFormName());						
					}//end
					List<Map<String,String>> formFields = formDefintionService.getFields(form.getId()); 
					for(Map<String,String> fields : formFields){
						if(!StringUtil.isEmptyString(fields.get("datadictionary"))){
							List<Map<String,Object>> dataDictionaryValues = dataDictionaryService.getDictionaryValueByParentId(fields.get("datadictionary"));
							if(fields.get("fieldClass").equals("data_dictionary")){
								listBoxData.put(form.getEnglishName()+"_"+fields.get("name"), dataDictionaryValues);
							} else if(fields.get("fieldClass").equals("autocomplete")){
								autoCompleteData.put(form.getEnglishName()+"_"+fields.get("name"), dataDictionaryValues);
							} else if(fields.get("fieldClass").equals("data_dictionary_radio")){
								radioButtonData.put(form.getEnglishName()+"_"+fields.get("name"), dataDictionaryValues);
							} else if(fields.get("fieldClass").equals("data_dictionary_checkbox")){
								checkBoxData.put(form.getEnglishName()+"_"+fields.get("name"), dataDictionaryValues);
							} 
						}
					}
					context.put("html", form.getHtmlSource());
					context.put("valueJson", valueJson);
					context.put("formId", form.getFormName());
					context.put("formUniqueId", form.getId());
					context.put("listBoxData", new JSONObject(listBoxData));
					context.put("autoCompleteData", new JSONObject(autoCompleteData));
					context.put("radioButtonData", new JSONObject(radioButtonData));
					context.put("checkBoxData", new JSONObject(checkBoxData));
					JSONArray formFieldAuditValues= rtTaskService.getFormFieldTraceData(processDetail.get("processInstanceId").toString(), form.getId());
					context.put("formFieldAuditValues",formFieldAuditValues);
					String htmlContent = TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showArchivedProcessDetails.vm", context);
					String path= processDetail.get("classificationId").toString()+"/"+processDetail.get("processName").toString().replace(" ", "_")+"/"+processDetail.get("processInstanceId").toString();
			        // Set the content type based to zip
			        try{
			        	//added files to zip
	                	log.info("Adding file " + path);
				        //Generate Html file operate page
	                	zos.putNextEntry(new ZipEntry(path+"/operatePage.html"));
	                    zos.write(htmlContent.getBytes());
//							//style sheets and script files
	                    for (String fileName : scriptAndStyleFilePaths.keySet()) {
		            		File file = new File(scriptAndStyleFilePaths.get(fileName));
		            		FileInputStream fis = new FileInputStream(file);
		                	zos.putNextEntry(new ZipEntry(path+fileName));
		                	byte[] bytes = new byte[1024];
		            		int length;
		            		while ((length = fis.read(bytes)) >= 0) {
		            			zos.write(bytes, 0, length);
		            		}
	                    }
	                    log.debug("Finished adding file " + path);
			        } catch(Exception e){
			         	log.info("The Exception:"+e.getMessage());
			         	throw new BpmException("Error while preparing file to download "+e.getMessage());
			        }
				}else{
					  throw new BpmException(I18nUtil.getMessageProperty("error.invalid.formId")+" Form Id : "+formKey);					
				}
			}else{
//	        	throw new BpmException("All or selected tasks contains no Form");
	        }          
		}
        zos.closeEntry();
        zos.close();
        sout.close();
	}

	public List<Process> getAllProcess(){
		return processDao.getAllProcess();
	}
	public Task setTaskDefinition(Task task){
		return taskService.setTaskDefinition(task);
	}
	
	public String getClassificationId(String deploymentId) {
		return processDao.getClassificationId(deploymentId);
	}

	@Autowired
	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	@Autowired
	public void setFormDefinitionService(FormDefinitionService formDefintionService) {
		this.formDefintionService = formDefintionService;
	}

	@Autowired
	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}
	
	@Autowired
	public void setTaskService(org.activiti.engine.TaskService taskService) {
		this.taskService = taskService;
	}

	@Autowired
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	@Autowired
	public void setProcessDefinitionService(
			ProcessDefinitionService processDefinitionService) {
		this.processDefinitionService = processDefinitionService;
	}

	@Autowired
	public void setRtTaskService(TaskService rtTaskService) {
		this.rtTaskService = rtTaskService;
	}	
	
	@Autowired
	public void setUserOpinionService(UserOpinionService userOpinionService) {
		this.userOpinionService = userOpinionService;
	}

}
