package com.eazytec.webapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.AttachmentEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.OperatingFunction;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import bsh.Interpreter;

import com.eazytec.Constants;
import com.eazytec.bpm.common.util.FormDefinitionUtil;
import com.eazytec.bpm.common.util.I18nUtil;
import com.eazytec.bpm.common.util.OperatingFunctionUtil;
import com.eazytec.bpm.engine.TaskRole;
import com.eazytec.bpm.exceptions.MembersAlreadyAddedException;
import com.eazytec.bpm.metadata.process.service.ProcessDefinitionService;
import com.eazytec.bpm.metadata.task.service.TaskDefinitionService;
import com.eazytec.bpm.runtime.form.service.FormService;
import com.eazytec.bpm.runtime.process.service.ProcessService;
import com.eazytec.bpm.runtime.table.service.TableService;
import com.eazytec.bpm.runtime.task.service.OperatingFunctionService;
import com.eazytec.bpm.runtime.task.service.TaskService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.FileUtil;
import com.eazytec.common.util.StringUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceException;
import com.eazytec.exceptions.DataSourceValidationException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.MetaTable;
import com.eazytec.model.MetaTableColumns;
import com.eazytec.model.OperatingFunctionAudit;
import com.eazytec.model.User;
import com.eazytec.service.UserService;

/**
 * 
 *
 * @author madan
 */
@Controller
public class OperatingFunctionController extends BaseFormController{
	
	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    
    private OperatingFunctionService operatingFunctionService;
    private FormService rtFormService;
	private HistoryService historyService;
	private TaskService rtTaskService;
	private TaskDefinitionService taskDefinitionService;
	public VelocityEngine velocityEngine;
	private ProcessService rtProcessService;
	private RuntimeService runtimeService;
	private ProcessDefinitionService processDefinitionService;
	private TableService tableService;
	@Autowired
	private UserService UserService;
	  
	@RequestMapping(value="bpm/of/executeJavaCode", method = RequestMethod.GET)
    public @ResponseBody Map<String, String> executeJava(@RequestParam("javaEvent") String javaEvent) {
    	Map<String, String> responseMap = new HashMap<String, String>();    
    	try{
        	Interpreter interpreter = new Interpreter();  
        	Object result = interpreter.eval(javaEvent);  //return map values
        	boolean noUniqueKey = false;
        	String uniqueColumn = null;
        if (result instanceof Map) {	
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				boolean isTableExistsMap = false;
				String tableName = ((Map) result).get("table").toString();
				//throw exception if table does not exist
				isTableExistsMap = tableService.checkTableName(tableName);
				if (isTableExistsMap) {
					/**
					 * save,update and delete operation in run time table .atleast one unique key is required to create a record.
					 * once record is inserted , values can be updated or deleted with primary key 
					 */
					List<MetaTable> tableDetails = tableService.getTableDetailsByNames("('"+tableName.toUpperCase()+"')");
					for (MetaTable tableDetail : tableDetails) {
						Set<MetaTableColumns> fieldProperty=tableDetail.getMetaTableColumns();
						for(MetaTableColumns fieldPropertyObj:fieldProperty){
							if(!fieldPropertyObj.getName().equalsIgnoreCase("ID") && fieldPropertyObj.getIsUniquekey()) {
								Map<String, Object> paramsMapforCheck = new HashMap<String, Object>();
								noUniqueKey = true;
								uniqueColumn = fieldPropertyObj.getName();
								operatingFunctionService.createOrUpdateJaveEventValues(paramsMap,fieldPropertyObj.getName(),paramsMapforCheck,result);
								break;
							}
						}
					}
				} else {
					  throw new BpmException(I18nUtil.getMessageProperty("error.tableNotFound"));    				
				}
				if(!noUniqueKey) {
					  throw new BpmException(I18nUtil.getMessageProperty("error.noUniqueId")+" Table Name : "+tableName);    				
				} else {
					responseMap.put("result", "success");
					responseMap.put("uniqueColumn", uniqueColumn);
				}
			} else {
				responseMap.put("result", "success");
			}
        }
        catch(BpmException e){
            log.error("Error executing java code "+e.getMessage());
            responseMap.put("result", e.getMessage().toString());
        } catch(Exception e) {
            log.error("Error executing java code "+e.getMessage());
            responseMap.put("result", e.getMessage().toString());
        }
		return responseMap;
    }
	
    @RequestMapping(value="bpm/of/showAddMembers", method = RequestMethod.GET)
    public ModelAndView showAddMembers(@RequestParam("taskId") String taskId, @RequestParam("type") String type, @RequestParam("formId") String formId,
    		@RequestParam("resourceName") String resourceName,@RequestParam("nodeType") String nodeType
    		,@RequestParam("processDefinitionId") String processDefinitionId, 
    		@RequestParam("processInsId") String processInsId,
    		ModelMap model,HttpServletRequest request) throws Exception {
    	if(type.equalsIgnoreCase(OperatingFunction.ADD)){  
    		model.addAttribute("userSelectionType", Constants.ORG_TREE_MULTI_SELECTION);
    		model.addAttribute("formAction", "bpm/of/add");
    		
    	}else if(type.equalsIgnoreCase(OperatingFunction.TRANSFER)){    		
    		model.addAttribute("userSelectionType", Constants.ORG_TREE_SINGLE_SELECTION);
    		model.addAttribute("formAction", "bpm/of/transfer");
    		
    	}else if(type.equalsIgnoreCase(OperatingFunction.TRANSACTOR_REPLACEMENT)){    	
    		if(nodeType.equalsIgnoreCase("1") || nodeType.equalsIgnoreCase("4")) {
    			model.addAttribute("userSelectionType", Constants.ORG_TREE_SINGLE_SELECTION);
    		} else if(nodeType.equalsIgnoreCase("2") || nodeType.equalsIgnoreCase("3")) {
    			model.addAttribute("userSelectionType", Constants.ORG_TREE_MULTI_SELECTION);
    		}
    		model.addAttribute("formAction", "bpm/of/replaceTransactor");
    		
    	}else if(type.equalsIgnoreCase(OperatingFunction.CONFLUENT_SIGNATURE)){    		
    		model.addAttribute("userSelectionType", Constants.ORG_TREE_MULTI_SELECTION);
    		model.addAttribute("formAction", "bpm/of/confluentSignature");
    		
    	}else if(type.equalsIgnoreCase(OperatingFunction.CIRCULATE_PERUSAL)){    		
    		model.addAttribute("userSelectionType", Constants.ORG_TREE_MULTI_SELECTION);
    		model.addAttribute("formAction", "bpm/of/circulatePerusal");
    	}else if(type.equalsIgnoreCase(OperatingFunction.BULKREPLACE)){    		
    		if(nodeType.equalsIgnoreCase("1") || nodeType.equalsIgnoreCase("4")) {
    			model.addAttribute("userSelectionType", Constants.ORG_TREE_SINGLE_SELECTION);
    		} else if(nodeType.equalsIgnoreCase("2") || nodeType.equalsIgnoreCase("3")) {
    			model.addAttribute("userSelectionType", Constants.ORG_TREE_MULTI_SELECTION);
    		}
    		model.addAttribute("formAction", "bpm/of/bulkReplace");
    	}
    	model=OperatingFunctionUtil.setOperatingFunctionTypes(model);
    	model.addAttribute("processInsId", processInsId);
    	model.addAttribute("taskId", taskId);
    	model.addAttribute("formId", formId);
    	model.addAttribute("resourceName", resourceName);
    	model.addAttribute("type",type);
    	model.addAttribute("processDefinitionId",processDefinitionId);
    	model.addAttribute("involveMembers", getText("task.involve.members", request.getLocale()));    	
    	return new ModelAndView("task/operatingfunction/addMembers", model);
    }
    
    @RequestMapping(value="bpm/of/showNextTaskAddMembers", method = RequestMethod.GET)
    public ModelAndView showNextTaskAddMembers(@RequestParam("taskId") String taskId, @RequestParam("type") String type, 
    		@RequestParam("formId") String formId, @RequestParam("organizersList") String organizersList,
    		@RequestParam("activityId") String activityId, @RequestParam("resourceName") String resourceName, @RequestParam("nodeType") String nodeType,
    		@RequestParam("endScriptName") String endScriptName,@RequestParam("canSave") boolean canSave,
    		ModelMap model,HttpServletRequest request) throws Exception {
    	try{
    		model=OperatingFunctionUtil.setOperatingFunctionTypes(model);
    	if(type.equalsIgnoreCase(OperatingFunction.JOINT_CONDUCTION)){
        	model.addAttribute("taskId", taskId);
    	}else if(type.equalsIgnoreCase(OperatingFunction.START_PROCESS)){
    		if(formId != null && !formId.isEmpty()){
    			model.addAttribute("formAction", "bpm/form/submitStartForm");
    		}else{
    			model.addAttribute("formAction", "bpm/manageProcess/startProcessInstance");
    		}
        	model.addAttribute("processKey", taskId);
    	}else if(type.equalsIgnoreCase(OperatingFunction.JUMP)){
        	model.addAttribute("taskId", taskId);
        	model.addAttribute("activityId", activityId);
        	model.addAttribute("resourceName", resourceName);
    	}else if(type.equalsIgnoreCase(OperatingFunction.DEFAULT_TASK)){
    		model.addAttribute("formAction", "bpm/manageProcess/assignOrganizerToTask");
    		model.addAttribute("taskId", taskId);
    	}
    	model.addAttribute("formId", formId);
    	model.addAttribute("type",type);
    	model.addAttribute("organizersList",organizersList);
    	model.addAttribute("nodeType",nodeType);
    	model.addAttribute("canSave",canSave);
    	model.addAttribute("involveMembers", getText("task.involve.members", request.getLocale()));  
    	if(!endScriptName.isEmpty()) {
    		model.addAttribute("endScriptName",endScriptName);
    	} else {
    		model.addAttribute("endScriptName","''");
    	}
    	}catch(Exception e){
    		//e.printStackTrace();
    		log.error("Error while adding organizers  "+e.getMessage());
    	}
    	return new ModelAndView("task/operatingfunction/addMembers", model);
    }
    
    /**
     * Start the default task of start node
     * @param processKey
     * @param request
     * @return
     */
  //remove start node form from process
  	@RequestMapping(value="bpm/of/defaultStartProcessInstance", method = RequestMethod.GET)
      public @ResponseBody Map<String, String>  defaultStartProcessInstance(
   		 @RequestParam("processKey") String processKey,
   		   HttpServletRequest request){
  		 Map<String, String> result = new HashMap<String, String>();
  	       try{
  	    	   ProcessInstance proIns = rtProcessService.startProcessInstanceById(processKey);
  	    	   Expression formName = ((UserTaskActivityBehavior)((ExecutionEntity)proIns).getActivity().getActivityBehavior()).getTaskDefinition().getFormName();
  	    	   TaskEntity task = rtTaskService.getNextTaskForProcessInstance(proIns.getId());
  	    	   if(formName == null){
  	    	 		 rtFormService.submitTaskForm(task.getId(), null, null, null, null, true, proIns.getId());
  	    	 		 User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  	    	 		 OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(),task.getId(),"",OperatingFunction.SUBMIT,proIns.getId(), proIns.getProcessDefinitionId());
  	 			  processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
  	    	 		TaskEntity  newTask = rtTaskService.getNextTaskForProcessInstance(proIns.getId());
  	    	 		result.put("taskId", newTask.getId());
  	    	 		result.put("needOrganizerPopup", "true");
  	    	 		result.put("nodeType", String.valueOf(newTask.getSignOffType()));
  	    	   }else{
  	    		 result.put("taskId", task.getId());
  	    		 result.put("needOrganizerPopup", "false");
  	    		 log.info("For process starting Organization is needed");
  	    	   }
  	       }catch(BpmException be){
  	    	   result.put("errorMsg", "Cannot start process, "+be.getMessage());
  	    	   log.error("Error while starting process  "+be.getMessage());
  	       }catch(Exception e){
  	    	   result.put("errorMsg", "Cannot start process, check log for errors!");
  	    	   log.error("Error while starting process  "+e.getMessage());
  	       }
  	       return result;
  	    }
    
    /**
	 * <p>Submits the form associated to a task that the user may need to fill in at processing a task. 
	 * Various runtime entity values are created based on the values submitted.</p>
	 * @param request
	 * @param model
	 * @return
	 */
	 @RequestMapping(value="bpm/of/submit", method = RequestMethod.POST)
	   public ModelAndView submitTaskForm(HttpServletRequest request,  ModelMap model,final RedirectAttributes redirectAttributes) {
		 Locale locale = request.getLocale();
		 HistoricTaskInstance historicTaskInstance = null;
		 HistoricProcessInstance historicProcessInstance = null;
		 Map <String,String> endEventScript = new HashMap<String, String>();
		  try{
			  Map<String, String[]> formValues = FormDefinitionUtil.getFormParamsInStringArray(request.getParameterMap());
			  Map<String, String> rtFormValues = FormDefinitionUtil.handleFromValues(formValues);
			  
			  String isJointConduction = rtFormValues.get("isJointConduction");
			  String nextTaskOrganizers =  rtFormValues.get("nextTaskOrganizers");
			  String nextTaskCoordinators =  rtFormValues.get("nextTaskCoordinators");
			  String processName = rtFormValues.get("processDefinitionId").split(":")[0];
			  // Added superior subordinate functionality
			  String organizerOrders =  rtFormValues.get("organizerOrders");			
			  rtFormValues.remove("nextTaskOrganizers");
			  rtFormValues.remove("nextTaskCoordinators");
			  rtFormValues.remove("isJointConduction");
			  rtFormValues.remove("organizerOrders");
			  String taskId = rtFormValues.get("taskId");
			  boolean isStartNodeTask = Boolean.valueOf(rtFormValues.get("isStartNodeTask"));

			  TaskEntity taskEntity = (TaskEntity) taskDefinitionService.getTaskById(taskId);
			  String processInstanceId = taskEntity.getProcessInstanceId();
			  rtFormValues.put("processInstanceId", processInstanceId);

			  String uploadDir = getServletContext().getRealPath("/resources") + "/"+ request.getRemoteUser() + "/";
			  Map<String, byte[]>files = FileUtil.getFileUploadMap(request);	
			  Map<String, String> filePathsMap = null;
			  if(files!=null){
				  filePathsMap = FileUtil.uploadFileForTask(request, uploadDir,taskId,taskEntity.getProcessInstanceId(),uploadDir,runtimeService,FileUtil.PROCESS_TYPE_TASK);
			  }
			  
			  Map<String, String[]> rtSubFormValues = FormDefinitionUtil.getSubFormParams(request.getParameterMap());
			  
			  //开始提交表单
			  Object object = rtFormService.submitTaskForm(taskId, rtFormValues, rtSubFormValues, files, filePathsMap, isStartNodeTask, null);
			  
			  //如果是开始节点，保存表单后给流程实例设置业务主键
			  if(isStartNodeTask){
				  runtimeService.updateBusinessKey(processInstanceId, rtFormValues.get("id"));
			  }
			  
			  log.info("Successfully submitted Task: "+taskEntity.getName()+" Process Name: "+processName);
			  MDC.put("processName",processName);
			  TaskEntity task = null;
			  TaskDefinition taskDef = null;
			  if(object instanceof TaskEntity){
				  task = (TaskEntity)object;
				  taskDef = task.getTaskDefinition();
			  }else if(object instanceof TaskDefinition){
				  taskDef = (TaskDefinition)object;
			  }
			  // Get start scripts in end node
			  if (taskDef.getEndEventStartScriptNameExpression() != null && taskDef.getEndEvenStartScriptExpression() !=null ) { 
				  endEventScript.put("endEventStartScriptName", taskDef.getEndEventStartScriptNameExpression());
				  endEventScript.put("endEventStartScript",  taskDef.getEndEvenStartScriptExpression());
			  }
			  // Add or remove attachment file operation perform
			  operatingFunctionService.attachmentOperation(rtFormValues,taskId);
			  if(StringUtil.parseBooleanAttribute(isJointConduction) && !StringUtil.isEmptyString(nextTaskOrganizers)){
				  operatingFunctionService.setJointConductionByOrder(taskEntity.getProcessInstanceId(), nextTaskOrganizers, nextTaskCoordinators,organizerOrders, 
					false, taskDef.getReferenceRelation(), rtFormValues.containsValue("isMail"), rtFormValues.containsValue("isInternalMessage"));
			  }
			  //Set the dynamic reader 
			  operatingFunctionService.setDynamicReaderValue(taskEntity,rtFormValues,taskEntity.getProcessInstanceId(),null);
			  User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			  //Archiving process
			  if(task != null){
				  rtProcessService.designArchivedProcessForm(task, getServletContext().getRealPath("/resources"), getServletContext().getRealPath("/styles"),getServletContext().getRealPath("/scripts")); 
			  }//end
			  historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).list().get(0);
			  // get end scripts in task
			  if(historicTaskInstance != null){
				 /* if(historicTaskInstance.getEndTime() != null){
					  if(taskEntity.getEndScriptName() != null && taskEntity.getEndScript() != null ) {
						  String endScriptName = taskEntity.getEndScriptName();
						  String endScript = taskEntity.getEndScript();
						  endEventScript.put("endScriptName", endScriptName);
						  endEventScript.put("endScript", endScript);
					  }
					  endEventScript.put("processInstanceId", historicTaskInstance.getProcessInstanceId());
					  redirectAttributes.addFlashAttribute("eventScriptDetails", endEventScript);
				  }*/
				  
				  //再次获取一下流程实例，用来判断流程是否结束
				  historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).list().get(0);
				  
				  if(historicProcessInstance.getEndTime() != null ){
					  saveMessage(request, getText("process.completed",historicTaskInstance.getName(), locale));
					  redirectAttributes.addFlashAttribute("status", "success");
					  OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(),taskId,rtFormValues.get("resourceName"),OperatingFunction.COMPELETE,taskEntity.getProcessInstanceId(),rtFormValues.get("processDefinitionId"));
					  processDefinitionService.saveOperatingFunctionAudit(operFunAudit);  
				  }else{
					  saveMessage(request, getText("task.completed",historicTaskInstance.getName(), locale));
					  redirectAttributes.addFlashAttribute("status", "success");	
					  //For Back off functionality to work for multi process and mutli sequence process , saving all operation type as return submit if previous operation is return.
					  if(rtFormValues.get("lastOperationPerformed").equalsIgnoreCase("return") || rtFormValues.get("lastOperationPerformed").equalsIgnoreCase("return submit")) {
						  OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(),taskId,rtFormValues.get("resourceName"),OperatingFunction.RETURN_SUBMIT,taskEntity.getProcessInstanceId(), rtFormValues.get("processDefinitionId"));
						  processDefinitionService.saveOperatingFunctionAudit(operFunAudit);  
					  } else {
						  OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(),taskId,rtFormValues.get("resourceName"),OperatingFunction.SUBMIT,taskEntity.getProcessInstanceId(),rtFormValues.get("processDefinitionId"));
						  processDefinitionService.saveOperatingFunctionAudit(operFunAudit); 
					  }					  
				  }
			  }
	      		model.addAttribute("statusMsg","success");      				
	      		model.addAttribute("newTab","true");
	      		model.addAttribute("gridType","mybucket");
		  }catch(EazyBpmException e){
			  saveError(request, e.getMessage());
			  redirectAttributes.addFlashAttribute("status", "failure");	
			  log.error("Error while submitting task : "+e.getMessage());
		  }catch(BpmException e){
			  saveError(request, e.getMessage());
			  redirectAttributes.addFlashAttribute("status", "failure");					  
			  log.error("Error while submitting task : "+e.getMessage());
		  }catch(DataSourceValidationException e){
			  saveError(request, e.getMessage());
			  redirectAttributes.addFlashAttribute("status", "failure");					  
			  log.error("Error while submitting task : "+e.getMessage());
		  }catch(DataSourceException e){
			  saveError(request, e.getMessage());
			  redirectAttributes.addFlashAttribute("status", "failure");					  
			  log.error("Failed to complete, your input values do not integrate with Database! "+e.getMessage());
		  }catch(Exception e){
			  e.printStackTrace();
			  saveError(request, "Failed to complete task!");
			  redirectAttributes.addFlashAttribute("status", "failure");					  
			  log.error("Error while submitting task : "+e.getMessage());
		  }finally {
				MDC.remove("processName");
			}
		  return new ModelAndView("submitProcess",model);
	   }
	
	 
	/**
	 * 在选择下个任务节点的办理人之前，获取流程设计时 任务节点配置的默认办理人
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "bpm/of/checkAndGetNextOrganizer", method = RequestMethod.POST)
	public @ResponseBody Set<Map<String, String>> checkAndGetNextOrganizer(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> responseMap = new HashMap<String, String>();
		List<Map<String, String>> organizerList = new ArrayList<Map<String, String>>();
		Set<Map<String, String>> organizerSet = new HashSet<Map<String, String>>();
		try {
			Map<String, Object> rtFormValues = FormDefinitionUtil.getFormParamsAsObjectMap(request.getParameterMap());
			// 获取流程设计时 任务节点配置的默认办理人
			organizerList = operatingFunctionService.getOrganizersForCurrentTask(rtFormValues);
			if (!organizerList.isEmpty() && organizerList != null) {
				organizerSet.addAll(organizerList);
			}
		} catch (BpmException e) {
			log.error("Error while getting Organizer : " + e.getMessage());
			responseMap.put("error", e.getMessage());
			organizerSet.add(responseMap);
		} catch (DataSourceException e) {
			responseMap.put("error", "Failed to get organizer, task does not integrate with Database!");
			organizerSet.add(responseMap);
			log.error("Failed to get organizer, task does not integrate with Database! : " + e.getMessage());
		} catch (Exception e) {
			log.error("Error while getting Organizer : " + e.getMessage());
			e.printStackTrace();
			organizerSet.add(responseMap);
			log.error(e.getMessage(), e);
		}
		return organizerSet;
	}	 
	 
	 @RequestMapping(value="bpm/of/getOrganizersForJumpTask", method = RequestMethod.GET)
	   public @ResponseBody Set<Map<String,String>> getOrganizersForJumpTask(@RequestParam("activityId") String activityId,@RequestParam("taskIdFromJump") String taskId,
			   ModelMap model,HttpServletRequest request, HttpServletResponse response) {
		  Map<String,String> responseMap = new HashMap<String,String>();
		  List<Map<String,String>> organizerList = new ArrayList<Map<String,String>>();
		  Set<Map<String,String>> organizerSet = new HashSet<Map<String,String>>();
		  try{
			  //setting Potential organiser initially
			  organizerList = operatingFunctionService.getOrganizersForJumpTask(taskId,activityId,null);
			  if(!organizerList.isEmpty() && organizerList != null) {
	  				organizerSet.addAll(organizerList);
	  			}
		  }catch(BpmException e){
			  responseMap.put("error",e.getMessage());
			  organizerSet.add(responseMap);
			  log.error("Error while getting Organizer : "+e.getMessage());
		  }catch(DataSourceException e){
			  responseMap.put("error",e.getMessage());
			  organizerSet.add(responseMap);
			  log.error("Failed to get organizer, task does not integrate with Database! : "+e.getMessage());
		  }catch(Exception e){
			  e.printStackTrace();
			  responseMap.put("error",e.getMessage());
			  organizerSet.add(responseMap);
			  log.error("Error while getting Organizer : "+e.getMessage());
		  }
		return organizerSet;
	   }

	/**
	 * <p>
	 * Saves the form associated to a task that the user may need to fill in at
	 * processing a task. Various runtime entity values are created based on the
	 * values submitted.
	 * </p>
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/of/save", method = RequestMethod.POST)
	public ModelAndView saveTaskForm(HttpServletRequest request, ModelMap model, final RedirectAttributes redirectAttributes) {
		Locale locale = request.getLocale();

		try {
			String uploadDir = getServletContext().getRealPath("/resources") + "/" + request.getRemoteUser() + "/";
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			Map<String, String> taskDetailsMap = operatingFunctionService.saveTaskForm(request, uploadDir, user.getId());

			String processName = taskDetailsMap.get("processDefinitionId").split(":")[0];
			log.info("Form saved successfully for task : " + taskDetailsMap.get("taskId") + " Process Name: " + processName);
			MDC.put("processName", processName);
			saveMessage(request, getText("form.saved", locale));
			model.addAttribute("statusMsg", "success");
			model.addAttribute("newTab", "true");
			model.addAttribute("gridType", "mybucket");
		} catch (EazyBpmException e) {
			saveError(request, e.getMessage());
			redirectAttributes.addFlashAttribute("status", "failure");
			log.error("Error while saving task : " + e.getMessage());
		} catch (BpmException e) {
			saveError(request, e.getMessage());
			redirectAttributes.addFlashAttribute("status", "failure");
			log.error("Error while saving task : " + e.getMessage());
		} catch (DataSourceValidationException e) {
			saveError(request, e.getMessage());
			redirectAttributes.addFlashAttribute("status", "failure");
			log.error("Error while saving taskr : " + e.getMessage());
		} catch (DataSourceException e) {
			saveError(request, e.getMessage());
			redirectAttributes.addFlashAttribute("status", "failure");
			log.error("Failed to save, your input values do not integrate with Database! : " + e.getMessage());
		} catch (Exception e) {
			saveError(request, e.getMessage());
			redirectAttributes.addFlashAttribute("status", "failure");
			log.error("Failed to save form : " + e.getMessage());
		} finally {
			MDC.remove("processName");
		}

		return new ModelAndView("submitProcess", model);
	}
		 
 	@RequestMapping(value="bpm/of/withdraw", method = RequestMethod.GET)
	public ModelAndView withdraw(@RequestParam("processInstanceId") String processInstanceId,@RequestParam("taskId") String taskId,@RequestParam("resourceName") String resourceName,
			@RequestParam("processDefinitionId") String processDefinitionId,HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		try{			
			operatingFunctionService.withdraw(processInstanceId);
			
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(),taskId,resourceName,OperatingFunction.WITHDRAW,processInstanceId, processDefinitionId);
			processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
			saveMessage(request, getText("instance.withdraw.success",locale));
			log.info("Withdraw taskId : "+taskId+" is success");
			model.put("statusMsg", "success");
		}catch(BpmException e){
			log.error(getText("instance.withdraw.error",e.getMessage(),locale));
			model.put("statusMsg", "failure");
			saveError(request, getText("instance.withdraw.error",e.getMessage(),locale));
		}catch(EazyBpmException e){
			log.error(getText("instance.withdraw.error",e.getMessage(),locale));
			model.put("statusMsg", "failure");
			saveError(request, getText("instance.withdraw.error",e.getMessage(),locale));
		}
		return new ModelAndView("submitProcess",model);
//		return new ModelAndView("redirect:/bpm/manageTasks/mybucket", model);
	}
 	
    
    /**
	 * <p>Add the user to a particular task by id, use can be a Owner or Assignee or Ivvolved based 
	 * on the Task Role(Identity Link Type)</p> 
	 * @param request
	 * @param model
	 * @return
	 */
 	@RequestMapping(value="bpm/of/add", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> add(HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		Map<String,Object> message = new HashMap<String, Object>();
		try{
			Map<String, String>rtFormValues = CommonUtil.getKeyValuePairs(request.getParameterMap());
			String taskId = rtFormValues.get("taskId");
			log.info("Add Member for task : "+taskId);
			String identityLinkType = rtFormValues.get("identityLinkType");
			String processDefinitionId = rtFormValues.get("processDefinitionId");
			String userId="";
			/*if(!StringUtil.isEmptyString("userId")){
				userId = rtFormValues.get("userId");
			}*/
			if(!StringUtil.isEmptyString("nextTaskOrganizersSelect")){
				userId = rtFormValues.get("nextTaskOrganizersSelect");
			}
			String taskOrganizerOrder = rtFormValues.get("taskOrganizerOrder"); 
			if(StringUtil.isEmptyString(userId)){
				message.put("failure", getText("of.add.empty.users", locale));
				log.info(getText("of.add.empty.users", locale));
			}else{
				operatingFunctionService.add(taskId, userId, identityLinkType,taskOrganizerOrder, processDefinitionId);	
				User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(),taskId,rtFormValues.get("resourceName"),OperatingFunction.ADD,rtFormValues.get("processInsId"), rtFormValues.get("processDefinitionId"));
				processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
				log.info("Added "+userId+" for task : "+taskId);
				model.addAttribute("status", "success");
//				model.addAttribute("type", "mybucket");
				message.put("success", getText("of.add.success", locale));
			}			
		}catch(MembersAlreadyAddedException e){
			message.put("failure", getText("of.add.already.added.users", new Object[] { e.getCommaSeparatedUserIds(), e.getIdentityLinkType() }, locale));
			log.error(getText("of.add.already.added.users",e.getMessage(),locale));
		}catch(BpmException e){
			message.put("failure", e.getMessage());
			log.error(getText("of.add.already.added.users",e.getMessage(),locale));
		}catch(EazyBpmException e){
			message.put("failure", e.getMessage());
			log.error(getText("of.add.already.added.users",e.getMessage(),locale));
		}
		
		return message;
	}
	
	@RequestMapping(value="bpm/of/transfer", method = RequestMethod.POST)
	public  @ResponseBody Map<String,Object> transfer(HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		Map<String,Object> message = new HashMap<String, Object>();
		try{
			Map<String, String>rtFormValues = CommonUtil.getKeyValuePairs(request.getParameterMap());
			String taskId = rtFormValues.get("taskId");
			String userId =rtFormValues.get("userId");
			String processDefinitionId =rtFormValues.get("processDefinitionId");
			operatingFunctionService.transfer(taskId, userId, processDefinitionId);	
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(),taskId,rtFormValues.get("resourceName"),OperatingFunction.TRANSFER,rtFormValues.get("processInsId"), rtFormValues.get("processDefinitionId"));
			processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
			message.put("success", getText("of.transfer.success", userId, locale));
			log.info("Transfer taskId : "+taskId+" to userIds :"+userId);
			model.addAttribute("status", "success");
//			model.addAttribute("type", "mybucket");
		}catch(BpmException e){
			log.error(getText("of.transfer.failure",e.getMessage(),locale));
			message.put("failure", e.getMessage());
		}catch(EazyBpmException e){
			log.error(getText("of.transfer.failure",e.getMessage(),locale));
			message.put("failure", e.getMessage());
		}
		return message;
	}
	
	@RequestMapping(value="bpm/of/replaceTransactor", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> replaceTransactor(HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		Map<String,Object> message = new HashMap<String, Object>();
		try{
			Map<String, String>rtFormValues = CommonUtil.getKeyValuePairs(request.getParameterMap());
			String taskId = rtFormValues.get("taskId");
			String userId =rtFormValues.get("userId");
			String processDefinitionId =rtFormValues.get("processDefinitionId");
			operatingFunctionService.replaceTransactor(taskId, userId,processDefinitionId);	
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(),taskId,rtFormValues.get("resourceName"),OperatingFunction.TRANSACTOR_REPLACEMENT,rtFormValues.get("processInsId"), rtFormValues.get("processDefinitionId").toString());
			processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
			message.put("success", getText("of.transactor-replacement.success", userId, locale));
			log.info("Replace Transactor for taskId : "+taskId+" to users :"+userId);
			//model.addAttribute("type", "mybucket");
		}catch(BpmException e){
			log.error(getText("of.transactor-replacement.failure",e.getMessage(),locale));
			message.put("failure", e.getMessage());
		}catch(EazyBpmException e){
			log.error(getText("of.transactor-replacement.failure",e.getMessage(),locale));
			message.put("failure", e.getMessage());
		}
		return message;
	}
	
	@RequestMapping(value="bpm/of/confluentSignature", method = RequestMethod.POST)
	public  @ResponseBody Map<String,Object> confluentSignature(HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		Map<String,Object> message = new HashMap<String, Object>();
		try{
			Map<String, String>rtFormValues = CommonUtil.getKeyValuePairs(request.getParameterMap());
			String taskId = rtFormValues.get("taskId");
			String userId =rtFormValues.get("userId");
			String processDefinitionId =rtFormValues.get("processDefinitionId");
			if(StringUtil.isEmptyString(userId)){
				message.put("failure", getText("of.confluentSignature.empty.users", locale));
				log.info(getText("of.add.empty.users", locale));
			}else{
				List<String>userIds = StringUtil.convertStringArrayToList(StringUtil.getCommaSeparatedStrings(userId));
				operatingFunctionService.confluentSignature(taskId, userIds, processDefinitionId);	
				User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(),taskId,rtFormValues.get("resourceName"),OperatingFunction.CONFLUENT_SIGNATURE,rtFormValues.get("processInsId"), rtFormValues.get("processDefinitionId"));
				processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
				message.put("success", getText("of.confluent-signature.success",userId, locale));
				log.info("Confluent Signature to users : "+userId);
				//model.addAttribute("type", "mybucket");
			}	
				
		}catch(BpmException e){
			log.error(getText("of.confluent-signature.failure",e.getMessage(),locale));
			message.put("failure", e.getMessage()); 
		}catch(EazyBpmException e){
			log.error(getText("of.confluent-signature.failure",e.getMessage(),locale));
			message.put("failure", e.getMessage());
		}catch(JSONException e){
			log.error(getText("of.confluent-signature.failure",e.getMessage(),locale));
			message.put("failure", e.getMessage());
		}
		return message;
	}
    
	@RequestMapping(value="bpm/of/circulatePerusal", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> circulatePerusal(HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		Map<String,Object> message = new HashMap<String, Object>();
		try{
			Map<String, String>rtFormValues = CommonUtil.getKeyValuePairs(request.getParameterMap());
			String taskId = rtFormValues.get("taskId");
			String userId =rtFormValues.get("userId");
			String processDefinitionId =rtFormValues.get("processDefinitionId");
//			operatingFunctionService.circulatePerusal(taskId, userId);
//			message.put("success", getText("of.circulate-perusal.success", userId, locale));
			
			if(StringUtil.isEmptyString(userId)){
				message.put("failure", getText("of.circulatePerusal.empty.users", locale));
				log.info(getText("of.add.empty.users", locale));
			}else{
				List<String>userIds = StringUtil.convertStringArrayToList(StringUtil.getCommaSeparatedStrings(userId));
				operatingFunctionService.circulatePerusal(taskId, userIds,processDefinitionId);	
				User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(),taskId,rtFormValues.get("resourceName"),OperatingFunction.CIRCULATE_PERUSAL,rtFormValues.get("processInsId"), rtFormValues.get("processDefinitionId"));
				processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
				message.put("success", getText("of.circulate-perusal.success",userId, locale));
				log.info(getText("of.circulate-perusal.success",userId, locale));
				log.info("Circulate Perusal of taskID : "+taskId+" to users : "+userId);
				//model.addAttribute("type", "mybucket");
			}	
				
		}catch(BpmException e){
			log.error(getText("of.circulate-signature.failure",e.getMessage(),locale));
			message.put("failure", e.getMessage());
		}catch(EazyBpmException e){
			log.error(getText("of.circulate-signature.failure",e.getMessage(),locale));
			message.put("failure", e.getMessage());
		}
		return message;
	}
		
		
	
	@RequestMapping(value="bpm/of/suspend", method = RequestMethod.GET)
	public ModelAndView suspend(@RequestParam("processInstanceId") String processInstanceId,@RequestParam("taskId") String taskId,@RequestParam("resourceName") String resourceName,
			@RequestParam("processDefinitionId") String processDefinitionId,HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		try{			
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			operatingFunctionService.suspend(processInstanceId,user.getId(),taskId,resourceName, processDefinitionId);
			saveMessage(request, getText("instance.suspend.success",locale));
			model.put("statusMsg", "success");
			model.put("newTab", true);
			log.info("Task id : "+taskId+" is suspended successfully");
		}catch(BpmException e){
			log.error(getText("instance.suspend.error",processInstanceId,locale));
			saveError(request, getText("instance.suspend.error",e.getMessage(),locale));
			model.put("statusMsg", "failure");
		}catch(EazyBpmException e){
			log.error(getText("instance.suspend.error",processInstanceId,locale));
			model.put("statusMsg", "failure");
			saveError(request, getText("instance.suspend.error",e.getMessage(),locale));
		}
		return new ModelAndView("submitProcess",model);

	}
	
	@RequestMapping(value="bpm/of/terminate", method = RequestMethod.GET)
	public ModelAndView  terminate(@RequestParam("executionId") String executionId,@RequestParam("taskId") String taskId,@RequestParam("resourceName") String resourceName,
			@RequestParam("processDefinitionId") String processDefinitionId,HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		try{			
			operatingFunctionService.terminate(executionId);
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(),taskId,resourceName,OperatingFunction.TERMINATE,executionId, processDefinitionId);
			processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
			saveMessage(request, getText("terminated.process.instance.success",locale));
			log.info("Execution ID : "+executionId+" terminated successfully");
			model.addAttribute("statusMsg","success");			
		}catch(BpmException e){
			log.error(getText("terminated.process.error",executionId,locale));
			saveError(request, getText("terminated.process.error",e.getMessage(),locale));
			model.addAttribute("statusMsg","error");			
		}
		return new ModelAndView("submitProcess",model);

	}
	
	/**
	 * Set the next task organizer and next task reader
	 * @param taskEntity
	 * @param rtFormValues
	 * @param proInsId
	 * @throws Exception
	 */
	/*private void storeNextDynamicReaderAndOrganizerForTask(TaskEntity taskEntity,Map<String, String> rtFormValues,String proInsId) throws Exception{
		rtProcessService.setTaskDefinition(taskEntity);
		  String dynamicNextOrgFiled = null;
		  String dynamicNextRedField = null;
		  String dynamicNextOrgType = null;
		  String dynamicNextRedType = null;
		  String nextDynamicUser = null;
		  String nextDynamicReader = null;
		  List<String> nextDynamicOrgFiledValues = new ArrayList<String>();		  
		  List<String> nextDynamicReadFieldValues = new ArrayList<String>();
		  String formName = taskEntity.getTaskDefinition().getFormName().toString();
		  //Get dynamic organizer,Reader field name
		  if(taskEntity.getTaskDefinition().getDynamicOrganizer() != null){
			  dynamicNextOrgFiled = taskEntity.getTaskDefinition().getDynamicOrganizer().toString();
			  if(taskEntity.getTaskDefinition().getDynamicOrganizerType()!=null){
				  dynamicNextOrgType = taskEntity.getTaskDefinition().getDynamicOrganizerType().toString();
			  }else{
				  dynamicNextOrgType = "user";
			  }
		  }
		 
		  if( taskEntity.getTaskDefinition().getDynamicReader() != null){
			  dynamicNextRedField = taskEntity.getTaskDefinition().getDynamicReader().toString();
			  if(taskEntity.getTaskDefinition().getDynamicReaderType()!=null){
				  dynamicNextRedType = taskEntity.getTaskDefinition().getDynamicReaderType().toString();
			  }else{
				  dynamicNextRedType = "user";
			  }
		  }
		 
		  //Separate filed name form form name and get values from from
		  if(dynamicNextOrgFiled!= null){
			  String fieldName = dynamicNextOrgFiled.replaceAll(formName+"_", "");
			  nextDynamicUser = rtFormValues.get(fieldName);
		  }
		  if(dynamicNextRedField != null){
			  String fieldName = dynamicNextRedField.replaceAll(formName+"_", ""); 
			  nextDynamicReader =  rtFormValues.get(fieldName);
		  }
		  
		  // get the filed values from form
		  if(nextDynamicUser!=null){
			  String[] userListArray = nextDynamicUser.split(",");		    	
		    	for (String user : userListArray) {
		    		nextDynamicOrgFiledValues.add(user);
				}
		  }
		  
		  if(nextDynamicReader!=null){
			  String[] userListArray = nextDynamicReader.split(",");		    	
		    	for (String user : userListArray) {
		    		nextDynamicReadFieldValues.add(user);
				}
		  }
		  
		  //get next task entity
		  TaskEntity nextTaskEntity = rtTaskService.getNextTaskForProcessInstance(proInsId);
		  
		  if(!nextDynamicOrgFiledValues.isEmpty()){
			  
			  if(dynamicNextOrgType.equalsIgnoreCase("user")){
				  
				  List<String> exitUSerIds = userManager.getExistUserIds(nextDynamicOrgFiledValues);
				  if(exitUSerIds != null && !exitUSerIds.isEmpty()){
				    	 for(String userId : exitUSerIds){
				    		 if(nextDynamicOrgFiledValues.contains(userId)){
				    			 runtimeService.addCandidateUser(nextTaskEntity.getId(), userId);
				    		 }
				    	 }			    	
				    }
			  }else if(dynamicNextOrgType.equalsIgnoreCase("role")){
				  
			  }else if(dynamicNextOrgType.equalsIgnoreCase("group")){
				  
			  }else{
				  
			  }
			  
		  }
		  
		  if(!nextDynamicReadFieldValues.isEmpty()){
			  
			  if(dynamicNextRedType.equalsIgnoreCase("user")){
				  List<String> exitUserIds = userManager.getExistUserIds(nextDynamicReadFieldValues);
				  if(exitUserIds != null && !exitUserIds.isEmpty()){
				    	 for(String userId : exitUserIds){
				    		 if(nextDynamicReadFieldValues.contains(userId)){
				    			 runtimeService.addCandidateReader(nextTaskEntity.getId(), userId);
				    		 }
				    	 }			    	
				    }
			  }else if(dynamicNextRedType.equalsIgnoreCase("role")){
				  
			  }else if(dynamicNextRedType.equalsIgnoreCase("group")){
				  
			  }else{
				  
			  }
			  
		  }
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  //Remove duplicates
		  Set<String> allUsers = new HashSet<String>();
		    allUsers.addAll(nextDynamicOrgFiledValues);
		    allUsers.addAll(nextDynamicReadFieldValues);
		    List<String> usetList = new ArrayList<String>(allUsers);
		    //Check whether the user exist or not
		    if(usetList != null && !usetList.isEmpty()){
			    List<String> exitUSerIds = userManager.getExistUserIds(usetList);
			    //add the next task organizer as user and add next task reader
			    if(exitUSerIds != null && !exitUSerIds.isEmpty()){
			    	 for(String userId : exitUSerIds){
			    		 if(nextDynamicOrgFiledValues.contains(userId)){
			    			 runtimeService.addCandidateUser(nextTaskEntity.getId(), userId);
			    		 }
			    		 if(nextDynamicReadFieldValues.contains(userId)){
			    			 runtimeService.addCandidateReader(nextTaskEntity.getId(), userId);
			    		 }
			    	 }			    	
			    }
		    }
		 
	 }*/
	
	@RequestMapping(value = "bpm/of/taskReturn", method = RequestMethod.GET)
   public ModelAndView taskReturn(@RequestParam("taskId") String taskId,@RequestParam("resourceName") String resourceName,
                   @RequestParam("returnType") String returnType,@RequestParam("processInsId") String processInstanceId, 
                   @RequestParam("processDefinitionId") String processDefinitionId, ModelMap model,HttpServletRequest request){                
       Locale locale = request.getLocale();
       try{
	       User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	       operatingFunctionService.taskReturn(user.getId(),taskId,resourceName,returnType,processInstanceId, processDefinitionId);
	       
	       /*Task task = taskDefinitionService.getTaskById(taskId);
	       Map<String, Object>properties = CommonUtil.getStringRepresentations(rtProcessService.getPrevHistoricVariablesForInstance(task.getProcessInstanceId(), taskId));//task.getProcessInstanceId()));
	       String prevTaskId = (String)properties.get("taskId");
	       HistoricTaskInstance prevTask = rtTaskService.getHistoricTaskInstanceByTaskId(prevTaskId);                
	       if(prevTask!=null){
	               model.addAttribute("task", prevTask);
	               script = TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showEditTaskHtmlForm.vm", rtProcessService.designUpdateTaskProcessScriptContext(task, prevTask, properties, velocityEngine,"bpm/form/updateTaskForm"));
	               model.addAttribute("script", script);
	       }else{
	               saveError(request, "No previous task found");
	               return new ModelAndView("redirect:/bpm/manageTasks/mybucket");
	       } */          
	       saveMessage(request, getText("jumped.process.instance.success",returnType,locale));
			log.info("Task ID : "+taskId+" returned successfully");
	       model.put("statusMsg", "success");
       }catch (Exception e) {
    	   log.error(getText("jumped.process.instance.error",returnType,locale));
    	   saveError(request, getText("jumped.process.instance.error",locale));
           model.put("statusMsg", "failure");
		  return new ModelAndView("submitProcess",model);
//           return new ModelAndView("redirect:/bpm/manageTasks/mybucket");
       }
	  return new ModelAndView("submitProcess",model);
//       return new ModelAndView("redirect:/bpm/manageTasks/mybucket",model);            
    }
	
	@RequestMapping(value = "bpm/of/taskAutoSaveAndBack", method = RequestMethod.POST)
    public ModelAndView taskAutoSaveAndBack(HttpServletRequest request,  ModelMap model,final RedirectAttributes redirectAttributes){		
		Locale locale = request.getLocale();
		try{
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String uploadDir = getServletContext().getRealPath("/resources") + "/"+ request.getRemoteUser() + "/";
			operatingFunctionService.taskAutoSaveAndBack(user.getId(),OperatingFunction.BACK,request,uploadDir);
			
			/*Task task = taskDefinitionService.getTaskById(taskId);
			Map<String, Object>properties = CommonUtil.getStringRepresentations(rtProcessService.getPrevHistoricVariablesForInstance(task.getProcessInstanceId(), taskId));//task.getProcessInstanceId()));
			String prevTaskId = (String)properties.get("taskId");
			HistoricTaskInstance prevTask = rtTaskService.getHistoricTaskInstanceByTaskId(prevTaskId);		
			if(prevTask!=null){
				model.addAttribute("task", prevTask);
				script = TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showEditTaskHtmlForm.vm", rtProcessService.designUpdateTaskProcessScriptContext(task, prevTask, properties, velocityEngine,"bpm/form/updateTaskForm"));
				model.addAttribute("script", script);
			}else{
				saveError(request, "No previous task found");
				return new ModelAndView("redirect:/bpm/manageTasks/mybucket");
			} */  	
			saveMessage(request, getText("jumped.process.instance.success","Back operation",locale));
			model.put("statusMsg", "success");
		}catch (Exception e) {
//			e.printStackTrace();
			saveError(request, getText("jumped.process.instance.error",locale));
			log.error(getText("jumped.process.instance.error",locale));
			model.put("statusMsg", "failure");
			return new ModelAndView("submitProcess",model);
//			return new ModelAndView("redirect:/bpm/manageTasks/mybucket");
		}
//		return new ModelAndView("redirect:/bpm/manageTasks/mybucket",model);
		return new ModelAndView("submitProcess",model);
 	}
	
	@RequestMapping(value="bpm/manageTasks/jumpToTask" , method = RequestMethod.GET )
	public ModelAndView jumpToTask(@RequestParam("taskId") String taskId,@RequestParam("resourceName") String resourceName,
									@RequestParam("activityId") String activityId,@RequestParam("jumpType") String jumpType,
									@RequestParam("nodeType") String nodeType,
									@RequestParam("nextTaskOrganizers") String nextTaskOrganizers,@RequestParam("nextTaskCoordinators") String nextTaskCoordinators,
						    		@RequestParam("nextTaskOrganizerOrders") String nextTaskOrganizerOrders,
						    		@RequestParam("processDefinitionId") String processDefinitionId,
						    		HttpServletResponse response,ModelMap model, HttpServletRequest request) throws IOException{
		Locale locale = request.getLocale();
		String jumpTypeStr = Constants.EMPTY_STRING;
		String jumpClassification = "Jump";
		if(jumpType.equals("forward")){
			jumpTypeStr = OperatingFunction.JUMP;
		}else{
			jumpTypeStr = OperatingFunction.RETURN;
			jumpClassification = "Return";
		}
    	try{
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		operatingFunctionService.taskJump(taskId,activityId,nodeType,jumpTypeStr,resourceName,user.getId(),nextTaskOrganizers,nextTaskCoordinators,nextTaskOrganizerOrders, processDefinitionId);
			saveMessage(request, getText("jumped.process.instance.success",jumpClassification,locale));
			model.put("statusMsg", "success");
			log.info(getText("jumped.process.instance.success",jumpClassification,locale));
		}catch(Exception e){
			saveError(request, getText("jumped.process.instance.failure",jumpClassification,locale));
			model.put("statusMsg", "failure");
			log.error(getText("jumped.process.instance.error",locale));
		}
    	return new ModelAndView("submitProcess",model);
//    	return new ModelAndView("redirect:/bpm/manageTasks/mybucket",model); 
	}
	
	@RequestMapping(value="bpm/of/autoSaveAndReturn" , method = RequestMethod.POST )
	public ModelAndView autoSaveAndReturn(HttpServletRequest request,  ModelMap model,final RedirectAttributes redirectAttributes,@RequestParam("taskIdFromJump") String taskId ,@RequestParam("processName") String processName) throws IOException{
		Locale locale = request.getLocale();
    	try{
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String uploadDir = getServletContext().getRealPath("/resources") + "/"+ request.getRemoteUser() + "/";
			String jumpClassification = operatingFunctionService.autoSaveAndReturn(user.getId(),request,uploadDir);
			if(jumpClassification.equalsIgnoreCase(Constants.FORWARD)){
				jumpClassification = Constants.JUMPFORMESSAGE;
			}else {
				jumpClassification = Constants.RETURNFORMESSAGE;
			}
			saveMessage(request, getText("jumped.process.instance.success",jumpClassification,locale));
			model.put("statusMsg", "success");
			log.info("Task ID : "+taskId +jumpClassification+ "success");
			MDC.put("processName",processName);
		}catch(BpmException bpe) {
			bpe.printStackTrace();
			saveError(request, bpe.getLocalizedMessage());
			model.put("statusMsg", "failure");
			log.error(getText("jumped.process.instance.error","TaskId : "+taskId,locale));
			//bpe.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
			saveError(request, getText("jumped.process.instance.error","TaskId : "+taskId,locale));
			model.put("statusMsg", "failure");
			log.error(getText("jumped.process.instance.error","TaskId : "+taskId,locale));
			//e.printStackTrace();
		} finally {
			MDC.remove("processName");
		}
    	return new ModelAndView("submitProcess",model);
//    	return new ModelAndView("redirect:/bpm/manageTasks/mybucket",model); 
	}
	
	public Map<String,String> canUserOperate(User user,List<Map<String,Object>> taskDetailsMap,String operation) {
		boolean canUserOperate = true;
		Map<String,String> canOperate = new HashMap<String, String>();
		canOperate.put("canOperate", "true");
		//	TaskRole taskRole = null;
			Task task = null;
			OperatingFunction operatingFunction = null;
			String taskId = null;
			for(Map<String,Object>taskDetailMap : taskDetailsMap){
				taskId = taskDetailMap.get("taskId").toString();
				/** No need to get task Role. Because all task displayed in 
				instance manager grid will be for work flow admin only.*/				
				//taskRole = rtTaskService.getTaskRoleApplicableForUser(user, taskId); 
				task = taskDefinitionService.getTaskById(taskId);
				operatingFunction = rtTaskService.getOperatingFunctionForUser(user, task, TaskRole.WORKFLOW_ADMINISTRATOR);
				if(operation.equalsIgnoreCase(OperatingFunction.TERMINATE)) {
					if(!operatingFunction.isTerminate()){
						canUserOperate = false;
					}
				}
				if(operation.equalsIgnoreCase(OperatingFunction.SUSPEND)) {
					if(!operatingFunction.isSuspend()){
						canUserOperate = false;
					}
				} 
				if(operation.equalsIgnoreCase(OperatingFunction.BULKREPLACE)) {
					if(!operatingFunction.isTransactorReplacement()){
						canUserOperate = false;
					}
				} 
				if(operation.equalsIgnoreCase(OperatingFunction.RETURN)) {
					if(!operatingFunction.isReturnOperation()){
						canUserOperate = false;
					}
				} 
				if(!canUserOperate) {
					canOperate.put("canOperate", "false");
					canOperate.put("taskName", task.getName());	
					return canOperate;
				} 
			}
			return canOperate;
		
	}
	
	@RequestMapping(value="bpm/of/terminateTasks", method = RequestMethod.GET)
	public RedirectView  terminateTasks(@RequestParam("taskDetails") String taskDetails,HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		try{			
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Map<String,Object>> taskDetailsMap = CommonUtil.convertJsonToList(taskDetails);
			Map<String,String> canUserOperate = canUserOperate(user,taskDetailsMap,OperatingFunction.TERMINATE);
			if(canUserOperate.get("canOperate").equalsIgnoreCase("true")){
				operatingFunctionService.terminateTasks(taskDetailsMap);
				for(Map<String,Object> taskDetailMap : taskDetailsMap){
					String executionId = (String) taskDetailMap.get("executionId");
					OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(),(String)taskDetailMap.get("taskId"),(String)taskDetailMap.get("resourceName"),OperatingFunction.TERMINATE,executionId, null);
					processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
				}
				saveMessage(request, getText("terminated.tasks.success",locale));
			}else {
				saveError(request, getText("terminated.permission.error",locale)+" : "+(String)canUserOperate.get("taskName"));
			}
		}catch(BpmException e){
			log.error(e.getMessage(), e);
			saveError(request, getText("terminated.tasks.error",e.getMessage(),locale));
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			saveError(request, e.getMessage());
		}
		return new RedirectView("/bpm/listView/showListViewGrid?listViewName=WORKFLOW_INSTANCE_MANAGER&container=target&title=Workflow Instance Manage&listViewParams=[{}]");
	}

	@RequestMapping(value="bpm/of/suspendTasks", method = RequestMethod.GET)
	public RedirectView  suspendTasks(@RequestParam("taskDetails") String taskDetails,HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		try{			
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Map<String,Object>> taskDetailsMap = CommonUtil.convertJsonToList(taskDetails);
			Map<String,String> canUserOperate = canUserOperate(user,taskDetailsMap,OperatingFunction.SUSPEND);
			if(canUserOperate.get("canOperate").equalsIgnoreCase("true")){
				for(Map<String,Object> taskDetailMap : taskDetailsMap){
					operatingFunctionService.suspend((String)taskDetailMap.get("executionId"),user.getId(),(String)taskDetailMap.get("taskId"),(String)taskDetailMap.get("resourceName"),(String) taskDetailMap.get("processDefinitionId"));
				}
				saveMessage(request, getText("instance.suspend.success",locale));
			}else {
				saveError(request, getText("suspend.permission.error",canUserOperate.get("taskName"),locale));
			}
		}catch(BpmException e){
			log.error(getText("instance.suspend.error",locale));
			saveError(request, getText("instance.suspend.error",e.getMessage(),locale));
		} catch(Exception e) {
			log.error(getText("instance.suspend.error",locale));
			saveError(request, e.getMessage());
		}
//		return new ModelAndView("redirect:/bpm/manageTasks/myOwnedTasks", model);
		return new RedirectView("/bpm/listView/showListViewGrid?listViewName=WORKFLOW_INSTANCE_MANAGER&container=target&title=Workflow Instance Manage&listViewParams=[{}]");
	}
	
	@RequestMapping(value="bpm/of/deleteTasks", method = RequestMethod.GET)
	public RedirectView  deleteTasks(@RequestParam("taskDetails") String taskDetails,HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		try{			
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Map<String,Object>> taskDetailsMap = CommonUtil.convertJsonToList(taskDetails);
			Map<String,String> canUserOperate = canUserOperate(user,taskDetailsMap,OperatingFunction.TERMINATE);
			if(canUserOperate.get("canOperate").equalsIgnoreCase("true")){
				OperatingFunctionAudit operFunAudit = null;
				for(Map<String,Object> taskDetailMap : taskDetailsMap){
					operatingFunctionService.delete(taskDetailMap.get("executionId").toString(),taskDetailMap.get("status").toString());
					operFunAudit = new OperatingFunctionAudit(user.getId(),(String)taskDetailMap.get("taskId"),(String)taskDetailMap.get("resourceName"),OperatingFunction.DELETE,(String)taskDetailMap.get("executionId"),(String)taskDetailMap.get("processDefinitionId"));
					processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
					tableService.runtimeTableStatusChange(taskDetailsMap);
				}
				saveMessage(request, getText("instance.delete.success",locale));
			}else {
				saveError(request, getText("delete.permission.error",canUserOperate.get("taskName"),locale));
			}
		}catch(BpmException e){
			log.error(getText("instance.delete.error",locale));
			saveError(request, getText("instance.delete.error",e.getMessage(),locale));
		} catch(Exception e) {
			log.error(getText("instance.delete.error",locale));
			saveError(request, getText("instance.delete.error",e.getMessage(),locale));
		}
		return new RedirectView("/bpm/listView/showListViewGrid?listViewName=WORKFLOW_INSTANCE_MANAGER&container=target&title=Workflow Instance Manage&listViewParams=[{}]");

	}
	
	@RequestMapping(value="bpm/of/replaceTasks", method = RequestMethod.GET)
	public RedirectView  replaceTasks(@RequestParam("taskDetails") String taskDetails,@RequestParam("userId") String userId,HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		try{			
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Map<String,Object>> taskDetailsMap = CommonUtil.convertJsonToList(taskDetails);
			Map<String,String> canUserOperate = canUserOperate(user,taskDetailsMap,OperatingFunction.TERMINATE);
			if(canUserOperate.get("canOperate").equalsIgnoreCase("true")){
				OperatingFunctionAudit operFunAudit = null;
				//Get Agency Setting map
				for(Map<String,Object> taskDetailMap : taskDetailsMap){
					operatingFunctionService.replaceTransactor(taskDetailMap.get("taskId").toString(), userId,taskDetailMap.get("processDefinitionId").toString());	
					operFunAudit = new OperatingFunctionAudit(user.getId(),(String) taskDetailMap.get("taskId"),(String)taskDetailMap.get("resourceName"),OperatingFunction.TRANSACTOR_REPLACEMENT,(String)taskDetailMap.get("executionId"), (String)taskDetailMap.get("processDefinitionId"));
					processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
				}
				saveMessage(request, getText("of.transactor-replacement.success", userId, locale));
				//model.addAttribute("type", "mybucket");
			}else {
				saveError(request, getText("replace.permission.error",canUserOperate.get("taskName"),locale));
			}
		}catch(BpmException e){
			log.error(getText("of.transactor-replacement.failure",userId,locale));
			saveError(request, getText("of.transactor-replacement.failure", userId, locale));
		}catch(EazyBpmException e) {
			log.error(getText("of.transactor-replacement.failure",userId,locale));
			saveError(request, e.getMessage());
		} catch(Exception e) {
			log.error(getText("of.transactor-replacement.failure",userId,locale));
			saveError(request, e.getMessage());
		} 
		return new RedirectView("/bpm/listView/showListViewGrid?listViewName=WORKFLOW_INSTANCE_MANAGER&container=target&title=Workflow Instance Manage&listViewParams=[{}]");

	}
	
	@RequestMapping(value="bpm/of/returnTasks", method = RequestMethod.GET)
	public RedirectView returnTasks(@RequestParam("taskDetails") String taskDetails,HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		try{			
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Map<String,Object>> taskDetailsMap = CommonUtil.convertJsonToList(taskDetails);
//			if(canUserOperate(user,taskDetailsMap,OperatingFunction.RETURN)){
				OperatingFunctionAudit operFunAudit = null;
				for(Map<String,Object> taskDetailMap : taskDetailsMap){
					runtimeService.taskReturn(taskDetailMap.get("taskId").toString());	
					operFunAudit = new OperatingFunctionAudit(user.getId(),(String)taskDetailMap.get("taskId"),(String)taskDetailMap.get("resourceName"),OperatingFunction.RETURN,(String)taskDetailMap.get("executionId"),(String) taskDetailMap.get("processDefinitionId"));
					processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
				}
				saveMessage(request, getText("jumped.process.instance.success","Return",locale));
		/*	}else {
				saveError(request, getText("returned.process.instance.permssion.error",locale));
			}*/
		}catch(BpmException e){
			log.error(getText("of.transactor-replacement.failure","Return",locale));
			saveError(request, getText("jumped.process.instance.failure","Return",locale));
		} catch(Exception e) {
			e.printStackTrace();
			log.error(getText("of.transactor-replacement.failure","Return",locale));
			saveError(request, getText("jumped.process.instance.failure","Return",locale));
		}
	return new RedirectView("/bpm/listView/showListViewGrid?listViewName=WORKFLOW_INSTANCE_MANAGER&container=target&title=Workflow Instance Manage&listViewParams=[{}]");

	}
	
	@RequestMapping(value="bpm/of/showAttachments", method = RequestMethod.GET)
	   public @ResponseBody List<AttachmentEntity> showAttachments(HttpServletRequest request,@RequestParam("taskId") String taskId,@RequestParam("isSameTable") boolean isSameTable,@RequestParam("isStartForm") boolean isStartForm,@RequestParam("instanceId")String instanceId) {
		  List<AttachmentEntity> attachments = runtimeService.getAttachmentsOfTask(isSameTable,taskId, false,isStartForm,instanceId);
	      return attachments;
	   }   	
   	 
   	@RequestMapping(value = "bpm/of/downloadAttachment",method = RequestMethod.GET)
	public void downloadAttachment(HttpServletResponse response,
			HttpServletRequest request) throws IOException {
		log.info("Preparing to download Document");
		String fileName = null;
		AttachmentEntity attachment = new AttachmentEntity();
		Locale locale = request.getLocale();
		OutputStream o = null;
		try {
			if (!StringUtil.isEmptyString(request.getParameter("id"))) {
				attachment = runtimeService.getAttachmentEntity(request
						.getParameter("id"));
			}
			fileName = attachment.getOriginalName();
            fileName = URLEncoder.encode(fileName, "UTF-8");
            fileName = URLDecoder.decode(fileName, "ISO8859_1");
			response.setContentType(attachment.getType());
			response.setHeader("Content-disposition", "attachment; filename=\""+ fileName + "\"");
			
			o = response.getOutputStream();
			InputStream is = new FileInputStream(attachment.getUrl() +"/"
							+ attachment.getName());
			IOUtils.copy(is, o);
			log.info("File Name : "+fileName+"downloaded successfully");
			
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			saveError(request, getText("error.document.download",locale));
		}finally{
			o.flush();
			o.close();
		}
	}
   	 
  	@RequestMapping(value = "bpm/of/viewAttachment",method = RequestMethod.GET)
	public ModelAndView viewAttachment(ModelMap model,
			HttpServletRequest request) {
		AttachmentEntity attachment = new AttachmentEntity();
		Locale locale = request.getLocale();
		UUID uuid= UUID.randomUUID();
		String REPOSITORY_CACHE_PDF=getServletContext().getRealPath("/resources") + "/root";
		String tmpFilePath="/resources/root";
		File pdfCache = new File(REPOSITORY_CACHE_PDF + File.separator + "pdf" +  File.separator + uuid + ".pdf");
		try {
			model.addAttribute("canPrint",true);
			if (!StringUtil.isEmptyString(request.getParameter("id"))) {
				attachment = runtimeService.getAttachmentEntity(request
						.getParameter("id")); 
			}
			model.addAttribute("title",attachment.getOriginalName());
			// Save content to temporary file
			model.addAttribute("filePath", FileUtil.writeTempFileToViewFile(attachment.getUrl() + "/"+ attachment.getName(), 
					attachment.getOriginalName(), attachment.getType(), REPOSITORY_CACHE_PDF, tmpFilePath));
		}catch(FileNotFoundException e){
			log.error(e.getMessage(),e);
			model.addAttribute("filePath", "/resources/file_not_found.pdf");
		}catch(NotImplementedException ne){
			pdfCache.delete();
			log.error(ne.getMessage(),ne); 
			model.addAttribute("filePath", "/resources/conversion_not_avail.pdf");
			saveError(request, getText("error.document.download",locale));
		} catch (Exception e) {
			pdfCache.delete();
			log.error(e.getMessage(),e);
			model.addAttribute("filePath","/resources/conversion_problem.pdf");
			saveError(request, getText("error.document.download",locale));
		}
		return new ModelAndView("dms/viewDocument",model);
	}
	
	@RequestMapping(value="bpm/of/recallTask" , method = RequestMethod.GET )
	public ModelAndView recallTask(@RequestParam("processInstanceId") String processInstanceId,
								@RequestParam("taskId") String taskId,
								@RequestParam("resourceName") String resourceName,@RequestParam("processDefinitionId") String processDefinitionId,
									HttpServletResponse response,ModelMap model, HttpServletRequest request) throws IOException{
		Locale locale = request.getLocale();
    	try{
    		runtimeService.recallTask(processInstanceId, taskId);
    		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(),taskId,resourceName,"recall",processInstanceId, processDefinitionId);
			processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
			saveMessage(request, getText("jumped.process.instance.success","Return",locale));
			log.info(getText("jumped.process.instance.success","Return",locale));
			model.put("statusMsg", "success");	
			model.put("newTab", true);
		}catch(Exception e){
			saveError(request, getText("jumped.process.instance.failure","Return",locale));
			model.put("statusMsg", "failure");
			log.info(getText("jumped.process.instance.failure","Return",locale));
		}
//    	return new ModelAndView("redirect:/bpm/manageTasks/mybucket",model);
		  return new ModelAndView("submitProcess",model);

	}
	/**
	 * return the user for given department methods
	 * @param deparmentIds
	 * @return
	 */
	 @RequestMapping(value="bpm/of/getUserIdForDepartment", method = RequestMethod.POST)
	 public @ResponseBody String getUserIdForDepartment(@RequestParam("deparmentIds") String deparmentIds) {
		 String userIds = null;
		 userIds = operatingFunctionService.getUserIdForDepartment(deparmentIds);
		return userIds;
	   }
	   
	@RequestMapping(value = "bpm/of/getCreatorByTask", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> getCreatorByTask(@RequestParam("taskId") String taskId) {
		IdentityLinkEntity creatorIdentiLink = runtimeService.getCreatorIdentityLinkForTaskId(taskId).get(0);
		String creatorId = creatorIdentiLink.getUserId();
		
		Map<String, String> response = new HashMap<String, String>();
		User user = UserService.getUser(creatorId);
		Department department = user.getDepartment();
		response.put("username", user.getUsername());
		response.put("fullName", user.getFullName());
		response.put("dep", department.getName());
		response.put("depViewName", department.getViewName());
		return response;
	}
	
	@RequestMapping(value = "bpm/of/getCreator", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> getCreator() {
		Map<String, String> response = new HashMap<String, String>();
		User user = CommonUtil.getLoggedInUser();
		Department department = user.getDepartment();
		response.put("username", user.getUsername());
		response.put("fullName", user.getFullName());
		response.put("dep", department.getName());
		response.put("depViewName", department.getViewName());
		return response;
	}

	@RequestMapping(value = "bpm/of/getRequestInsData", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> getRequestInsData(@RequestParam("taskId") String taskId) {
		Map<String, String> response = new HashMap<String, String>();
		IdentityLinkEntity creatorIdentiLink = runtimeService.getCreatorIdentityLinkForTaskId(taskId).get(0);
		String creatorId = creatorIdentiLink.getUserId();
		String departemntId = CommonUtil.getLoggedInUser().getDepartment().getName();
		response.put("creatorId", creatorId);
		response.put("departemntId", departemntId);
		return response;
	}
	/**
	 * View the urge form
	 * 
	 * @param resourceName
	 * @param deploymentId
	 * @throws IOException
	 */
	@RequestMapping(value = "bpm/of/showUrge", method = RequestMethod.GET)
	public ModelAndView showUrge(@RequestParam("processDefinitionId") String processDefinitionId,@RequestParam("taskId") String taskId,@RequestParam("url") String url,
			@RequestParam("processInsId") String processInsId,HttpServletResponse response, ModelMap model,HttpServletRequest request) throws IOException {
		try {
			log.debug("---for taskId---" + taskId);
			log.debug("----for processDefinitionId---" + processDefinitionId);
			ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) processDefinitionService
					.getProcessDefinitionById(processDefinitionId);
			Map<String,String> currentTaskOrganizer = operatingFunctionService.getOranizersByInstanceId(processInsId);
			String currentUrl = url + "?taskId="+currentTaskOrganizer.get("currentTaskId")+"&suspendState=&isStartNodeTask=false&formReadOnly=false&creator=&lastOperationPerformed=submit&gridType=mybucket";
//			model.addAttribute("organizers", operatingFunctionService.getOrganizersByTaskId(taskId));
			model.addAttribute("organizers", currentTaskOrganizer.get("organizer"));
			model.addAttribute("currentTaskId", currentTaskOrganizer.get("currentTaskId"));
			String object = (String) processDefinition.getProperties().get("urgeMessage");
			object=operatingFunctionService.replaceTextInUrgeMessage(object,currentUrl,taskId,"",false);
			model.addAttribute("notificationMessage", object);
		} catch (Exception e) {
			log.error("Not able to get urge Messge/organizer for taskId:"
					+ taskId + " with processDefinitionId:"
					+ processDefinitionId, e);
			saveError(request, e.getMessage());
			return new ModelAndView("task/operatingfunction/urge", model);
		}
		return new ModelAndView("task/operatingfunction/urge", model);
	}

	/**
	 * View the proper svgxml and show the task lists process definition
	 * 
	 * @param resourceName
	 * @param deploymentId
	 * @throws IOException
	 */
	@RequestMapping(value = "bpm/of/sendUrgeNotification", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> sendUrgeNotification(HttpServletResponse response,
			ModelMap model, HttpServletRequest request) {
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			String commaSeparatedNotificationType = "";
			if (!com.eazytec.bpm.common.util.StringUtil.isEmptyString(request
					.getParameter("Mail"))) {
				commaSeparatedNotificationType = Constants.NOTIFICATION_EMAIL_TYPE;
			}
			if (!com.eazytec.bpm.common.util.StringUtil.isEmptyString(request
					.getParameter("Sms"))) {
				commaSeparatedNotificationType = commaSeparatedNotificationType
						+ "," + Constants.NOTIFICATION_SMS_TYPE;
			}
 			if (!com.eazytec.bpm.common.util.StringUtil.isEmptyString(request
					.getParameter("internalMsg"))) {
				commaSeparatedNotificationType = commaSeparatedNotificationType
						+ "," + Constants.NOTIFICATION_INTERNALMESSAGE_TYPE;
			}
			if (com.eazytec.bpm.common.util.StringUtil
					.isEmptyString(commaSeparatedNotificationType)) {
				throw new Exception(
						"Please select atlease one notification type!");
			}
			operatingFunctionService.sendNotification(request.getParameter("organizers"), request.getParameter("notificationMessage"),commaSeparatedNotificationType,"Urge Message");
			log.debug("----Scheduled urge notifications----");
			message.put("success", "Urge Notification send successfully");
		} catch (Exception e) {
			message.put("failure", e.getMessage());
			log.error("Urge Notification sending failed " + e.getMessage(), e);
		}
		return message;
	}
	
	@Autowired
	public void setOperatingFunctionService(
			OperatingFunctionService operatingFunctionService) {
		this.operatingFunctionService = operatingFunctionService;
	}

	@Autowired
	public void setRtFormService(FormService rtFormService) {
		this.rtFormService = rtFormService;
	}

	@Autowired
	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	@Autowired
	public void setTaskDefinitionService(TaskDefinitionService taskDefinitionService) {
		this.taskDefinitionService = taskDefinitionService;
	}

	@Autowired
	public void setRtTaskService(TaskService rtTaskService) {
		this.rtTaskService = rtTaskService;
	}

	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	@Autowired
	public void setRtProcessService(ProcessService rtProcessService) {
		this.rtProcessService = rtProcessService;
	}   
	
	@Autowired
	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	@Autowired
	public void setProcessDefinitionService(ProcessDefinitionService processDefinitionService) {
		this.processDefinitionService = processDefinitionService;
	}
	
    @Autowired
    public void setTableService(TableService tableService) {
             this.tableService = tableService;
    }
	
}
