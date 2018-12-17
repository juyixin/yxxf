package com.eazytec.webapp.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.OperatingFunction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.eazytec.Constants;
import com.eazytec.bpm.common.util.FormDefinitionUtil;
import com.eazytec.bpm.metadata.process.service.ProcessDefinitionService;
import com.eazytec.bpm.metadata.task.service.TaskDefinitionService;
import com.eazytec.bpm.runtime.form.service.FormService;
import com.eazytec.bpm.runtime.process.service.ProcessService;
import com.eazytec.bpm.runtime.task.service.OperatingFunctionService;
import com.eazytec.bpm.runtime.task.service.TaskService;
import com.eazytec.common.util.FileUtil;
import com.eazytec.common.util.StringUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceException;
import com.eazytec.exceptions.DataSourceValidationException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.AgencySetting;
import com.eazytec.model.LabelValue;
import com.eazytec.model.OperatingFunctionAudit;
import com.eazytec.model.User;

/**
 * <p>Serves as the controller class for form entities during runtime like submitting form, linking forms,
 * instantiating etc</p>
 * 
 * @author madan
 * @author nkumar
 * 
 */

@Controller
public class FormController extends BaseFormController {

	/**
	 * Log variable for all child classes. Uses LogFactory.getLog(getClass())
	 * from Commons Logging
	 */
	protected final Log log = LogFactory.getLog(getClass());
	private FormService rtFormService;
	private TaskDefinitionService taskDefinitionService;
	private HistoryService historyService;
	private OperatingFunctionService operatingFunctionService;
	private ProcessService rtProcessService;
	private ProcessDefinitionService processDefinitionService;
	private RuntimeService runtimeService;;

	


	/**
	 * Submits the initial form that the user may need to fill in at starting a process. Various runtime entity values are 
	 * created based on the values submitted
	 * @param request
	 * @param model
	 * @return
	 */
	 @RequestMapping(value="bpm/form/submitStartForm", method = RequestMethod.POST)
	public ModelAndView submitStartForm(HttpServletRequest request,
			ModelMap model, final RedirectAttributes redirectAttributes) {
		 TaskEntity nextTask = null;
		Locale locale = request.getLocale();
		Map<String, String> endEventScript = new HashMap<String, String>();
		boolean isDraft = false;
		try {
			Map<String, String> rtFormValues = new HashMap<String, String>();
			Map<String, String[]> formValues = FormDefinitionUtil
					.getFormParamsInStringArray(request.getParameterMap());
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
			Map<String, String[]> rtSubFormValues = FormDefinitionUtil
					.getSubFormParams(request.getParameterMap());
			String processDefinitionId = rtFormValues
					.get("processDefinitionId");
			String nextTaskOrganizers = rtFormValues
					.get("nextTaskOrganizers");
			String nextTaskCoordinators = rtFormValues
					.get("nextTaskCoordinators");
			isDraft = Boolean.valueOf(rtFormValues.get("isDraft"));
			// Added superior subordinate functionality
			String organizerOrders =  rtFormValues.get("organizerOrders");
			rtFormValues.remove("processDefinitionId");
			rtFormValues.remove("nextTaskOrganizers");
			rtFormValues.remove("nextTaskCoordinators");
			rtFormValues.remove("organizerOrders");
			rtFormValues.remove("isDraft");
			String uploadDir = getServletContext().getRealPath("/resources")
					+ "/" + request.getRemoteUser() + "/";
			Map<String, byte[]> files = FileUtil.getFileUploadMap(request);
			Map<String, String> filePathsMap = null;
			

			ProcessInstance processInstance = rtFormService.submitStartForm(
					processDefinitionId, rtFormValues, rtSubFormValues, files,
					filePathsMap,isDraft);
			if (files != null) {
				filePathsMap = FileUtil.uploadFileForTask(request, uploadDir,null,processInstance.getId(),uploadDir,runtimeService, FileUtil.PROCESS_TYPE_START);
			}
			// Add or remove attachment file operation perform
			operatingFunctionService.attachmentOperation(rtFormValues,null);
			String successMessage,faliureMessage,operatingFunction = null;
			if(!isDraft){
				if(!StringUtil.isEmptyString(nextTaskOrganizers) ){
					nextTask = operatingFunctionService.setJointConductionByOrder(processInstance.getId(), nextTaskOrganizers, nextTaskCoordinators,organizerOrders, true, processInstance.getReferenceRelation(), false, false);
	            }
				//Set dynamic reader values
				if(processInstance.getProperties().containsKey("dynamicReaderType") && processInstance.getProperties().get("dynamicReaderType")!=null){
					Map<String,String> nodePRoperties = new HashMap<String,String>();
					nodePRoperties.put("dynamicReaderType", (String) processInstance.getProperties().get("dynamicReaderType"));
					nodePRoperties.put("dynamicReader", (String) processInstance.getProperties().get("dynamicReader"));
					nodePRoperties.put("formName", (String) processInstance.getProperties().get("formName"));
					operatingFunctionService.setDynamicReaderValue(nextTask, rtFormValues, processInstance.getId(), nodePRoperties);
				}
				
				// Get end script values in start node
				if(processInstance.getProperties().containsKey("endScript")){
					Map<String,String> endScriptContent =   (Map<String, String>) processInstance.getProperties().get("endScript");
						if(endScriptContent.containsKey("functionName") && endScriptContent.containsKey("script")) {
							endEventScript.put("endScriptName", endScriptContent.get("functionName"));
							endEventScript.put("endScript", endScriptContent.get("script"));
						}
				}
				if (processInstance.getId() != null) {
					endEventScript.put("processInstanceId",
							processInstance.getId());
					redirectAttributes.addFlashAttribute("eventScriptDetails",
							endEventScript);
				}
				successMessage = getText("process.started", locale);
				faliureMessage = getText("process.notstarted", locale);
				operatingFunction = OperatingFunction.CREATE;
			}else {
				successMessage = getText("process.saved", locale);
				faliureMessage = getText("process.notsaved", locale);
				operatingFunction = OperatingFunction.SAVE;
			}
			if (processInstance != null) {
				//Operating function audit log
				 User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				  OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(),null,rtFormValues.get("resourceName"),operatingFunction,processInstance.getId(), null);
				  processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
				log.info("Instantiated process for defintion: "+ processDefinitionId);
				saveMessage(request, successMessage);
			} else {
				log.info("Could not instantiate process: "+ processDefinitionId);
				saveError(request,faliureMessage );
			}
		} catch (EazyBpmException e) {
			saveError(request, e.getMessage());
			log.error(e.getMessage(), e);
		} catch (BpmException e) {
			saveError(request, e.getMessage());
			log.error(e.getMessage(), e);
		} catch (DataSourceValidationException e) {
			saveError(request, e.getMessage());
			log.error(e.getMessage(), e);
		} catch (DataSourceException e) {
			saveError(request,getText("form.invalid",locale));
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			saveError(request, getText("form.process.start.error",locale));
			model.addAttribute("message",getText("form.process.error ", e.getMessage(),locale));
			log.error(e.getMessage(), e);
		}
//		if(isDraft){
//			return new RedirectView("/bpm/manageTasks/mybucket");
//		}else{
			return new ModelAndView("submitProcess",model);
//		}
	}	 	

		
	 @RequestMapping(value="bpm/form/updateTaskForm", method = RequestMethod.POST)
	public ModelAndView updateTaskForm(HttpServletRequest request,
			ModelMap model, final RedirectAttributes redirectAttributes) {
		Locale locale = request.getLocale();
		 HistoricTaskInstance historicTaskInstance = null;
		 HistoricProcessInstance historicProcessInstance = null;
		 Map <String,String> endEventScript = new HashMap<String, String>();
		try {
			Map<String, String> rtFormValues = new HashMap<String, String>();
			Map<String, String[]> formValues = FormDefinitionUtil
					.getFormParamsInStringArray(request.getParameterMap());
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
			Map<String, String[]> rtSubFormValues = FormDefinitionUtil
					.getSubFormParams(request.getParameterMap());
			String isJointConduction = rtFormValues.get("isJointConduction");
			String nextTaskOrganizers =  rtFormValues.get("nextTaskOrganizers");
			String nextTaskCoordinators =  rtFormValues.get("nextTaskCoordinators");
			 String processName = rtFormValues.get("processDefinitionId").split(":")[0];
			// Added superior subordinate functionality
			String organizerOrders =  rtFormValues.get("organizerOrders");	
			String taskId = rtFormValues.get("taskId");
			String isSignOff = null;
			isSignOff = rtFormValues.get("isSignOff");
			String isSave = rtFormValues.get("isSave");
			rtFormValues.remove("nextTaskOrganizers");
			rtFormValues.remove("nextTaskCoordinators");
			rtFormValues.remove("isJointConduction");
			rtFormValues.remove("processDefinitionId");
			rtFormValues.remove("organizerOrders");
			rtFormValues.remove("isSave");
			TaskEntity taskEntity = (TaskEntity) taskDefinitionService.getTaskById(taskId);
		//	  rtFormValues.remove("htmlSource");
			String uploadDir = getServletContext().getRealPath("/resources")
					+ "/" + request.getRemoteUser() + "/";
			Map<String, byte[]> files = FileUtil.getFileUploadMap(request);
			Map<String, String> filePathsMap = null;
			if (files != null) {
				//filePathsMap = FileUtil.uploadFile(request, uploadDir);
				  filePathsMap = FileUtil.uploadFileForTask(request, uploadDir,taskId,taskEntity.getProcessInstanceId(),uploadDir,runtimeService,FileUtil.PROCESS_TYPE_TASK);
			}
			
			Object object = rtFormService.updateTaskForm(taskId, rtFormValues, rtSubFormValues,
					files, filePathsMap,
					StringUtil.parseBooleanAttribute(isSignOff));
			log.info("Updated TaskForm: " + taskId+" Process Name: "+processName);
			MDC.put("processName",processName);
			// Add or remove attachment file operation perform
			  operatingFunctionService.attachmentOperation(rtFormValues,taskId);
			if(StringUtil.parseBooleanAttribute(isJointConduction) && !StringUtil.isEmptyString(nextTaskOrganizers)){
				operatingFunctionService.setJointConductionByOrder(taskEntity.getProcessInstanceId(), nextTaskOrganizers, nextTaskCoordinators,organizerOrders, false, null, false, false);
			}	
			//Operating function audit log
			  User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//Archiving process
			if(object instanceof TaskEntity){
				rtProcessService.designArchivedProcessForm((TaskEntity)object, getServletContext().getRealPath("/resources"), getServletContext().getRealPath("/styles"),getServletContext().getRealPath("/scripts")); 
			}//end
			historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).list().get(0);
			  if(historicTaskInstance != null){
				  if(historicTaskInstance.getEndTime() != null){
					  if(taskEntity.getEndScriptName() != null && taskEntity.getEndScript() != null ) {
						  endEventScript.put("endScriptName", taskEntity.getEndScriptName());
						  endEventScript.put("endScript", taskEntity.getEndScript());
					  }
					  endEventScript.put("processInstanceId", historicTaskInstance.getProcessInstanceId());
					  redirectAttributes.addFlashAttribute("eventScriptDetails", endEventScript);
				  }
				  historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).list().get(0);				  
				  if(historicProcessInstance.getEndTime() != null ){
					  saveMessage(request, getText("process.completed",historicTaskInstance.getName(), locale));
					  OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(),taskId,rtFormValues.get("resourceName"),OperatingFunction.COMPELETE,taskEntity.getProcessInstanceId(), rtFormValues.get("processDefinitionId"));
					  processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
//					  redirectAttributes.addFlashAttribute("status", "success");
				  }else if(isSave.equalsIgnoreCase("true")) {
					  saveMessage(request, getText("form.saved", locale));
					  OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(),taskId,rtFormValues.get("resourceName"),OperatingFunction.SAVE,taskEntity.getProcessInstanceId(), rtFormValues.get("processDefinitionId"));
					  processDefinitionService.saveOperatingFunctionAudit(operFunAudit);  
//					  redirectAttributes.addFlashAttribute("status", "success");
				  } else{
					  saveMessage(request, getText("task.completed",historicTaskInstance.getName(), locale));
					  OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(),taskId,rtFormValues.get("resourceName"),OperatingFunction.SUBMIT,taskEntity.getProcessInstanceId(), rtFormValues.get("processDefinitionId"));
					  processDefinitionService.saveOperatingFunctionAudit(operFunAudit); 
//					  redirectAttributes.addFlashAttribute("status", "success");
				  }
			  }
			//saveMessage(request, getText("form.updated", locale));
      		model.addAttribute("statusMsg","success");      				
      		model.addAttribute("newTab","true");			  
	 	     log.info("Form Task Updated Successfully ");
		} catch (EazyBpmException e) {
			saveError(request, e.getMessage());
			redirectAttributes.addFlashAttribute("status", "failure");					  
			log.error(e.getMessage(), e);
		} catch (BpmException e) {
			saveError(request, e.getMessage());
			redirectAttributes.addFlashAttribute("status", "failure");					  
			log.error(e.getMessage(), e);
		} catch (DataSourceValidationException e) {
			saveError(request, e.getMessage());
			redirectAttributes.addFlashAttribute("status", "failure");					  
			log.error(e.getMessage(), e);
		} catch (DataSourceException e) {
			saveError(request,	getText("form.db.integrate.error",locale));
			redirectAttributes.addFlashAttribute("status", "failure");					  
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("status", "failure");					  
			saveError(request, getText("form.update.error",locale));
			model.addAttribute("message",getText("form.update.error",e.getMessage(),locale));
			log.error(e.getMessage(), e);
		} finally {
			MDC.remove("processName");
		}
//		return new RedirectView("/bpm/process/myInstances");
		return new ModelAndView("submitProcess",model);
	}
	
	@Autowired
	public void setRtFormService(FormService rtFormService) {
		this.rtFormService = rtFormService;
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
	public void setOperatingFunctionService(
			OperatingFunctionService operatingFunctionService) {
		this.operatingFunctionService = operatingFunctionService;
	}
	
	@Autowired
	public void setRtProcessService(ProcessService rtProcessService) {
		this.rtProcessService = rtProcessService;
	}  
	
	@Autowired
	public void setProcessDefinitionService(ProcessDefinitionService processDefinitionService) {
		this.processDefinitionService = processDefinitionService;
	}
	
	@Autowired
	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}
}
