package com.eazytec.webapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.form.FormFieldPermission;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.task.OperatingFunctionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.OperatingFunction;
import org.activiti.engine.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.Constants;
import com.eazytec.bpm.common.util.OperatingFunctionUtil;
import com.eazytec.bpm.common.util.ProcessDefinitionUtil;
import com.eazytec.bpm.common.util.TaskUtil;
import com.eazytec.bpm.engine.TaskRole;
import com.eazytec.bpm.metadata.form.service.FormDefinitionService;
import com.eazytec.bpm.metadata.process.service.ProcessDefinitionService;
import com.eazytec.bpm.metadata.task.service.TaskDefinitionService;
import com.eazytec.bpm.opinion.service.UserOpinionService;
import com.eazytec.bpm.runtime.task.service.TaskService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.TemplateUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.MetaForm;
import com.eazytec.model.Opinion;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.util.DateUtil;


/**
 * <p>Controller for task related operations like task list, CRUDs, grids etc</p>
 * @author nkumar
 * @author madan
 */

@Controller
public class TaskController extends BaseFormController{
	
	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
	
	private TaskDefinitionService taskDefinitionService;
	private TaskService rtTaskService;
	private ProcessDefinitionService processDefinitionService;
	public VelocityEngine velocityEngine;
	private com.eazytec.bpm.runtime.process.service.ProcessService rtProcessService;
	private HistoryService historyService;
	private FormDefinitionService formDefinitionService;
	private UserOpinionService userOpinionService; 
	
	
	/**
	 * <p>Shows the detailed view of a task like its attributes, associated forms, actionable links etc wherever
	 * applicable like claiming, completing etc</p>
	 * @param id of the task
	 * @param model
	 * @return
	 */
	@RequestMapping(value="bpm/manageTasks/showTaskDetail", method = RequestMethod.GET)
	public ModelAndView showTaskDetail(@RequestParam("taskId") String id, ModelMap model, HttpServletRequest request){
		try{
			Task task = taskDefinitionService.getTaskById(id);
			User user = CommonUtil.getLoggedInUser();
			OperatingFunction operatingFunction = rtTaskService.getOperatingFunction(user, task);
			String taskCreateTime = DateUtil.convertDateToSTDString(task.getCreateTime().toString());
			
			ProcessDefinition processDefinition = processDefinitionService.getProcessDefinitionById(task.getProcessDefinitionId());
			
			boolean isTaskClaimable = taskDefinitionService.isTaskClaimableByUser(task, user);
			boolean isTaskFormApplicable = taskDefinitionService.isTaskFormApplicableForUser(task, user);
			
			String peopleScript = designMembersGrid(task,request);
			model.addAttribute("task", task);
			model.addAttribute("taskCreateTime", taskCreateTime);
			model.addAttribute("process", processDefinition);
			model.addAttribute("peopleScript", peopleScript); 
			model.addAttribute("isTaskClaimable", isTaskClaimable);
			model.addAttribute("isTaskFormApplicable", isTaskFormApplicable);
			model.addAttribute("operatingFunction", operatingFunction);
			model.addAttribute("involveMembers", getText("task.involve.members", request.getLocale()));
		}catch(BpmException e){
			e.printStackTrace();
			log.error("Cannot get the tasks for "+id, e);
			saveError(request, e.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			log.error("Problem getting the tasks for "+id, e);
			saveError(request, e.getMessage());
		}			
		log.info("TaskDetail where task Id:"+id);

		return new ModelAndView("showTaskDetail",model);
	}
	//remove start node form from process
	@RequestMapping(value="bpm/manageTasks/defaultStartProcessInstance", method = RequestMethod.GET)
    public @ResponseBody Map<String, String>  defaultStartProcessInstance(
 		 @RequestParam("processKey") String processKey,
 		   HttpServletRequest request){
		 Map<String, String> result = new HashMap<String, String>();
		 Locale locale = request.getLocale();
	       try{
	    	   ProcessInstance proIns = rtProcessService.startProcessInstanceById(processKey);
	    	   Expression formName = ((UserTaskActivityBehavior)((ExecutionEntity)proIns).getActivity().getActivityBehavior()).getTaskDefinition().getFormName();
	    	   TaskEntity task = rtTaskService.getNextTaskForProcessInstance(proIns.getId());
	    	   if(formName == null){
	    		   task.complete();
	    	   }	   
	    	   result.put("taskId", task.getId());
	    	   log.info("Process : "+processKey+" started successfully");
	       }catch(BpmException be){
	    	   result.put("errorMsg", "Cannot start process, "+be.getMessage());
	    	   log.error(getText("process.start.failed",processKey,locale));
	       }catch(Exception e){
	    	   result.put("errorMsg", "Cannot start process, check log for errors!");
	    	   log.error(getText("process.start.failed",processKey,locale));
	       }
	       return result;
	    }
	
	
	/**
	 * <p> show the form details of the task in separate page </p>
	 * @param taskId
	 * @param model
	 * @param request
	 * @return
	 */
	//remove start node form from process
	@RequestMapping(value="showTaskFormDetail", method = RequestMethod.GET)
	public ModelAndView showTaskFormDetail(@RequestParam("taskId") String taskId,@RequestParam("suspendState") String suspendState,ModelMap model, HttpServletRequest request, @RequestParam("isStartNodeTask") boolean isStartNodeTask,
			@RequestParam("creator") String creator,@RequestParam("lastOperationPerformed") String lastOperationPerformed,@RequestParam("gridType") String gridType){
		Locale locale = request.getLocale();
		Map<String, Object>historicVariables = new HashMap<String, Object>();		
		boolean isFormReadOnly=false;
		try{
			Task currentTask = taskDefinitionService.getTaskById(taskId);
			
			// 方法中用到HistoricTaskInstance task 里的属性，currentTask中都存在，为什么用HistoricTaskInstance？
			// 已办任务列表中点击"办理"后，也调用这个方法，此时运行时任务currentTask已经不存在，只能获取历史任务
			HistoricTaskInstance task = rtTaskService.getHistoricTaskInstanceByTaskId(taskId);
			
			//TODO currentTask 和 activeTask 从debug的情况看，taskId 都是相同的，为什么要区分 currentTask 和 activeTask，两者有什么差异？
			Task activeTask = taskDefinitionService.getTaskByProcessInstanceId(task.getProcessInstanceId());
			
			User user = CommonUtil.getLoggedInUser();
			
			//根据实例ID、任务ID获取任务历史变量
			historicVariables = rtProcessService.getHistoricVariablesForInstance(task.getProcessInstanceId(), taskId);
			//根据实例ID获取意见列表，显示在第二个选项卡上
			List<Opinion> opinionList = userOpinionService.getOpinion(task.getProcessInstanceId());		
			ProcessDefinition processDefinition = processDefinitionService.getProcessDefinitionById(task.getProcessDefinitionId());
			ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) processDefinition;
			model.addAttribute("lastOperationPerformed", lastOperationPerformed);//上一步操作执行名称
			model.addAttribute("isOpinion",processDefinitionEntity.getTaskDefinitions().get(task.getTaskDefinitionKey()).getIsOpinion());
			model.addAttribute("processName",processDefinitionEntity.getName());
			HistoricProcessInstance historicProcessInstance= historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).list().get(0);
			String formReadOnly=request.getParameter("formReadOnly");
	   		if(formReadOnly!=null && formReadOnly.equals("true")){
	   			isFormReadOnly=true;
	   		}
	   		
	   		//在模板中放入业务id
	   		model.addAttribute("businessKey", historicProcessInstance.getBusinessKey()==null?"":historicProcessInstance.getBusinessKey());
	   		
	   		//判断当前任务是否存在历史变量，若存在选择showTaskHtmlFormWithValues.vm模板进行渲染，若不存在选择showTaskHtmlForm.vm模板进行渲染
			if(historicVariables!=null&&historicVariables.size()>0){
				//保存后重新办理
				
				model.addAttribute("isUpdateOnly", "true");
				model.addAttribute("isUpdate", "true");
				Map<String, Object> properties = CommonUtil.getStringRepresentations(historicVariables);
				model.addAttribute("task", task);				
				if(currentTask != null){
					TaskRole taskRole = rtTaskService.getTaskRoleApplicableForUser(user, taskId);
					if(taskRole!=null && taskRole.getName().equalsIgnoreCase(TaskRole.ORGANIZER.getName())){
						model.addAttribute("isUpdateOnly", "false");
						model.addAttribute("isOrganizer", "true");
					}else {
						model.addAttribute("isOrganizer", "false");
					}
					OperatingFunction operatingFunction = rtTaskService.getOperatingFunctionForUser(user, currentTask, taskRole);
					model.addAttribute("nodeType", String.valueOf(currentTask.getTaskDefinition().getNodeTypeExpression()));
					OperatingFunction operatingFunctionForTask = rtTaskService.getOperatingFunction(user, currentTask);
					model.addAttribute("operatingFunction", operatingFunction);
					model.addAttribute("operatingFunctionForTask", operatingFunctionForTask);
					model.addAttribute("customOfScript",designCustomOperatingFunctions(operatingFunction));
				}else{
					model.addAttribute("customOfScript","");
				}
				model.addAttribute("currentTaskName", task.getName());
				model.addAttribute("processDefinitionId", task.getProcessDefinitionId());
				model.addAttribute("processInstanceId", task.getProcessInstanceId());
				model.addAttribute("executionId", task.getExecutionId());
				model.addAttribute("suspendState", suspendState);
				model.addAttribute("activityId", task.getTaskDefinitionKey());
	   			properties.put("isFormReadOnly", isFormReadOnly);
   				//remove start node form from process
				model.addAttribute("taskFormScript", TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showTaskHtmlFormWithValues.vm", rtProcessService.designUpdateTaskProcessScriptContext(currentTask, task, properties,processDefinition, velocityEngine,"bpm/form/updateTaskForm",model, isStartNodeTask, creator)));
				model.addAttribute("isFormReadOnly", isFormReadOnly);
			}else{
				model.addAttribute("taskId", taskId);
				model.addAttribute("suspendState", suspendState);
				model = OperatingFunctionUtil.setOperatingFunctionTypes(model);
	   			model.put("isFormReadOnly", isFormReadOnly);
	   			Map<String, Map<String,Object>> fieldInfo = new HashMap<String, Map<String,Object>>();
	   			if(currentTask != null){
		   			rtTaskService.setTaskDefinition(currentTask);
		   			model.addAttribute("nodeType", String.valueOf(currentTask.getTaskDefinition().getNodeTypeExpression()));
		   			//根据流程配置，自动填充表单域
		   			fieldInfo = rtTaskService.setFormFieldAutomatic(currentTask.getTaskDefinition().getFormFieldAutomatic(), opinionList, currentTask);
	   			}
				/* Form Design to accept new Inputs*/
	   			//remove start node form from process
				model.addAttribute("taskFormScript", designTaskForm(user,currentTask, task, model,"com/eazytec/bpm/form/templates/showTaskHtmlForm.vm", fieldInfo, isStartNodeTask, creator,locale));
			}
			
			
			//如果当前任务为空，说明任务已经办理过
			if(currentTask == null) {
				
				//判断下一步的任务是否完成，没完成返回false，已完成返回true
				boolean isNextTaskEnd = processDefinitionService.checkIsNextTaskCompleted(task);
				
				//如果下一步任务没完成，实例也没结束，可以拿回
				if(!isNextTaskEnd && historicProcessInstance.getEndTime() == null) {
					model.addAttribute("recallOperatingFunctionForTask",rtTaskService.getRecallFunctionForHistoricTask(user, task));
				}

				//如果是流程发起人，可以撤办
				Task doneTask =  taskDefinitionService.getTaskByProcessInstanceId(task.getProcessInstanceId());
	   			if(doneTask != null){
			   		model.addAttribute("operatingFunctionForTask", rtTaskService.getOperatingFunctionForStartNode(user, doneTask));
	   			}
			}
			
			rtTaskService.setInstanceTraceData(task.getProcessDefinitionId(),task.getProcessInstanceId(),task.getId(), model,processDefinition.getName(), historicProcessInstance.getProcessDefinitionId(), currentTask);
			model.addAttribute("deploymentId", processDefinition.getDeploymentId());
			model.addAttribute("resourceName", processDefinition.getResourceName());
			model.addAttribute("opinion",new Opinion());
			model.addAttribute("opinionList",opinionList);
			model.addAttribute("loggedInUser",CommonUtil.getLoggedInUserId());
			model.addAttribute("taskId", taskId);
			model.addAttribute("processInsId",task.getProcessInstanceId());
			model.addAttribute("suspendState",suspendState);
			model = OperatingFunctionUtil.setOperatingFunctionTypes(model);
			model.addAttribute("gridType",gridType); // for grid auto refresh after close new tab
			model.addAttribute("activeTask",activeTask.getName()); // for grid auto refresh after close new tab
			
		}catch(EazyBpmException e){
			log.error(e.getMessage(), e);
			saveError(request, e.getMessage());
			return new ModelAndView("redirect:mybucket");
		}catch(BpmException bpme){
			log.error(bpme.getMessage(), bpme);
			saveError(request, bpme.getMessage());
			return new ModelAndView("redirect:mybucket");
		}catch(ActivitiTaskAlreadyClaimedException e){
			log.error(e.getMessage());
			saveError(request, e.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			log.error(getText("load.form.fail",e.getMessage(),locale));
			saveError(request, e.getMessage());
		}

		return new ModelAndView("showTaskFormDetail",model);
	}
	
	/**
	 * 
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="printPreviewDetail", method = RequestMethod.GET)
	public ModelAndView showTaskPrintPreviewDetail(@RequestParam("taskId")String id, ModelMap model, HttpServletRequest request){
		Locale locale = request.getLocale();
		Task currentTask = taskDefinitionService.getTaskById(id);
		
		HistoricTaskInstance task = rtTaskService.getHistoricTaskInstanceByTaskId(id);
		User user = CommonUtil.getLoggedInUser();
		
		Map<String, Object>historicVariables = new HashMap<String, Object>();			
		historicVariables = rtProcessService.getHistoricVariablesForInstance(task.getProcessInstanceId(), id);
				
		if(historicVariables!=null&&historicVariables.size()>0){
			Map<String, Object>properties = CommonUtil.getStringRepresentations(historicVariables);
			ProcessDefinition processDefinition = processDefinitionService.getProcessDefinitionById(task.getProcessDefinitionId());
			
			String taskFormDataScript = TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showTaskHtmlFormPrintValues.vm", rtProcessService.designUpdateTaskProcessScriptContext(currentTask, task, properties,processDefinition, velocityEngine,"bpm/form/updateTaskForm",model, false, null));
			
			model.addAttribute("taskFormScript", taskFormDataScript);
		}else{
			model.addAttribute("taskId", id);
			
			String taskFormDataScript = designTaskForm(user,currentTask, task, model, "com/eazytec/bpm/form/templates/showTaskHtmlFormPrintValues.vm", null, false, null,locale);	
			if(user!=null){
				log.debug("task to be claimed by user..");
				log.debug("user id...."+user.getId());
				log.debug("task id...."+id);
			}
			model.addAttribute("taskFormScript", taskFormDataScript);
		}
		
		model.addAttribute("printPreivew", true);
		
		return new ModelAndView("showTaskPrintPreviewDetail",model);
	}
	
	/**
	 * Set task unread task to redaedlist
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="bpm/manageTasks/saveRederDetails", method = RequestMethod.GET)
	public ModelAndView saveRederDetails(@RequestParam("taskId") String id, ModelMap model, HttpServletRequest request){
		Locale locale = request.getLocale();
		try {
			rtTaskService.addIdentityLinkDetail(id);
		}catch(Exception e) {
			model.put("statusMsg", "failure");
			log.error(getText("read.error",e.getMessage(),locale));
			saveError(request, getText("read.failure",locale));
		}
			model.put("statusMsg", "success");
			saveMessage(request, getText("read.success", locale));
			return new ModelAndView("submitProcess",model);
	}
	
	@RequestMapping(value="bpm/manageTasks/bulkReaderDetails", method = RequestMethod.GET)
	public ModelAndView bulkReaderDetails(@RequestParam("taskId") String id, ModelMap model, HttpServletRequest request){
		Locale locale = request.getLocale();
		try {
			rtTaskService.addIdentityLinkDetail(id);
		}catch(Exception e) {
			model.put("statusMsg", "failure");
			log.error(getText("read.error",e.getMessage(),locale));
			saveError(request, getText("read.failure",locale));
		}
			saveMessage(request, getText("read.bulk.success", locale));
			return new ModelAndView("redirect:/bpm/manageTasks/toRead", model);
	}
	/**
	 * Set readed task to be unread
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="bpm/manageTasks/removeRederDetails", method = RequestMethod.GET)
	public ModelAndView removeRederDetails(@RequestParam("taskId") String id, ModelMap model, HttpServletRequest request){
		Locale locale = request.getLocale();
		try {
			rtTaskService.removeIdentityLinkDetail(id,CommonUtil.getLoggedInUserId());
			model.put("statusMsg", "success");
			saveMessage(request, getText("unread.success", locale));
		}catch(Exception e) {
			log.error(getText("unread.error",e.getMessage(),locale));
			model.put("statusMsg", "failure");
			saveError(request, getText("unread.failure",locale));
		}
		 return new ModelAndView("submitProcess",model);   
//		return new ModelAndView("redirect:/bpm/manageTasks/readedList", model);
	}
	
	/**
	 * <p> show the form details of the task in separate page for Reader</p>
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="showTaskFormDetailForReader", method = RequestMethod.GET)
	public ModelAndView showTaskFormDetailForReader(@RequestParam("taskId") String id,@RequestParam("isRead") boolean isRead,
			@RequestParam("suspensionState") String suspensionState,@RequestParam("gridType") String gridType,ModelMap model, HttpServletRequest request){
		Locale locale = request.getLocale();
		try{
			HistoricTaskInstance task = rtTaskService.getHistoricTaskInstanceByTaskId(id);
			HistoricProcessInstance historicProcessInstance= historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).list().get(0);
			List<Opinion> opinionList = userOpinionService.getOpinion(task.getProcessInstanceId());
			ProcessDefinition processDefinition = processDefinitionService.getProcessDefinitionById(task.getProcessDefinitionId());
			MDC.put("processName",processDefinition.getName()); // for showing process name in process logs
			model.addAttribute("isOpinion",true);
			Task newTask = taskDefinitionService.getTaskById(id);
			User user = CommonUtil.getLoggedInUser();
			model.addAttribute("isFormReadOnly",true);
			if(newTask != null){
				OperatingFunction operatingFunctionForTask = rtTaskService.getOperatingFunction(user, newTask);
				model.addAttribute("operatingFunctionForTask", operatingFunctionForTask);
			}
			Map<String, Object>historicVariables = new HashMap<String, Object>();			
				historicVariables = rtProcessService.getHistoricVariablesForInstance(task.getProcessInstanceId(), id);
				model.addAttribute("userId",user.getId());		
			if(historicVariables!=null&&historicVariables.size()>0){
				model.addAttribute("isUpdateOnly", "true");
				model.addAttribute("isUpdate", "true");
				Map<String, Object>properties = CommonUtil.getStringRepresentations(historicVariables);
				model.addAttribute("task", task);				
				OperatingFunction operatingFunction = new OperatingFunctionImpl();
				model.addAttribute("deploymentId", processDefinition.getDeploymentId());
				model.addAttribute("resourceName", processDefinition.getResourceName());
				model.addAttribute("currentTaskName", task.getName());
				model.addAttribute("processDefinitionId", task.getProcessDefinitionId());
				model.addAttribute("activityId", task.getTaskDefinitionKey());
				model.addAttribute("operatingFunction", operatingFunction);  
				model.addAttribute("showReadButton", isRead);
				model.addAttribute("processInstanceId", task.getProcessInstanceId());
				model.addAttribute("executionId", task.getExecutionId());
				model.addAttribute("customOfScript",designCustomOperatingFunctions(operatingFunction));
				
				String taskFormDataScript = TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showTaskHtmlFormWithValues.vm", rtProcessService.designTaskProcessScriptContextForReader(task, properties,processDefinition, velocityEngine,"bpm/form/updateTaskForm",isRead,model));
				model.addAttribute("taskFormScript", taskFormDataScript);
			}else{		
				model.addAttribute("taskId", id);
				model = OperatingFunctionUtil.setOperatingFunctionTypes(model);
				Map<String, Map<String,Object>> fieldInfo = rtTaskService.setFormFieldAutomatic(newTask.getTaskDefinition().getFormFieldAutomatic(), opinionList, newTask);
				String taskFormDataScript = designTaskFormForReader(user, task, model,isRead, fieldInfo,locale);	
				if(user!=null){
					log.debug("task to be claimed by user..");
					log.debug("user id...."+user.getId());
					log.debug("task id...."+id);
				}
				//TaskRole taskRole = rtTaskService.getTaskRoleApplicableForUser(user,id);
				model.addAttribute("taskFormScript", taskFormDataScript);
			}
			model.addAttribute("processInsId",task.getProcessInstanceId());
			model.addAttribute("suspendState",suspensionState);
			rtTaskService.setInstanceTraceData(task.getProcessDefinitionId(),task.getProcessInstanceId(),task.getId(), model, processDefinition.getName(), historicProcessInstance.getProcessDefinitionId(), null);
			model.addAttribute("opinion",new Opinion());
			model.addAttribute("loggedInUser",CommonUtil.getLoggedInUserId());
			model.addAttribute("opinionList",opinionList);
			model.addAttribute("taskId", id);
			model.addAttribute("gridType",gridType); // for grid auto refresh after close new tab
			model = OperatingFunctionUtil.setOperatingFunctionTypes(model);
			
		}/*catch(EazyBpmException e){
			log.error(e.getMessage(), e);
			saveError(request, e.getMessage());
			return new ModelAndView("redirect:mybucket");
		}catch(BpmException bpme){
			log.error(bpme.getMessage(), bpme);
			saveError(request, bpme.getMessage());
			return new ModelAndView("redirect:mybucket");
		}*//*catch(ActivitiTaskAlreadyClaimedException e){
			log.error(e.getMessage());
			saveError(request, e.getMessage());
		}*/catch(Exception e){
			//e.printStackTrace();
			log.error(getText("load.readform.fail",e.getMessage(),locale));
			saveError(request, "Problem claiming and operating task, check log for errors!");
		}

		return new ModelAndView("showTaskFormDetail",model);
	}
	
	private String designCustomOperatingFunctions(OperatingFunction operatingFunction){
		try{
			Map<String, Object> context = new HashMap<String, Object>();
			context.put("operatingFunction", operatingFunction);
			return TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/task/operatingfunction/templates/showCustomOperatingFunctions.vm", context);
		}catch(BpmException e){
			log.error(e.getMessage(), e);
			return e.getMessage();
		}
		
	}
	
	private String designTaskFormForReader(User user, HistoricTaskInstance task, ModelMap model,boolean isRead, Map<String, Map<String, Object>> fieldInfo,Locale locale){
		Map<String, Object> context = new HashMap<String, Object>();
		String taskFormScript = null;
		String taskId=task.getId();
		context.put("taskId", taskId);
		JSONObject pastValuesJson = new JSONObject();
		try{
			TaskRole taskRole = rtTaskService.getTaskRoleApplicableForUser(user, taskId);
			OperatingFunction operatingFunction = new OperatingFunctionImpl();
			model.addAttribute("operatingFunction", operatingFunction);
			model.addAttribute("processInstanceId", task.getProcessInstanceId());
			model.addAttribute("executionId", task.getExecutionId());
			model.addAttribute("customOfScript","");
			String formKey = taskDefinitionService.getFormKey(taskId);
			ProcessDefinition processDefinition = processDefinitionService.getProcessDefinitionById(task.getProcessDefinitionId());
			
			model.addAttribute("deploymentId", processDefinition.getDeploymentId());
			model.addAttribute("resourceName", processDefinition.getResourceName());
			model.addAttribute("currentTaskName", task.getName());
			model.addAttribute("processDefinitionId", task.getProcessDefinitionId());
			model.addAttribute("activityId", task.getTaskDefinitionKey());
			Map<String, FormFieldPermission> nodeLevelFieldPermissions = rtTaskService.getNodeLevelFieldPermissions(user, taskId, false);
			context.put("nodeLevelFieldPermissions", nodeLevelFieldPermissions);
			context.put("taskId", task.getId());	
			context.put("automatiFielInfo",fieldInfo);
			context.put("isFormReadOnly", "true");
			if(formKey!=null){
				MetaForm form = formDefinitionService.getDynamicFormById(formKey);
				if(form!=null){
					String html = form.getHtmlSource();
					context.put("taskId", taskId);		
					context.put("html", html);		
					context.put("formId", form.getFormName());	
					model.addAttribute("formId", form.getFormName());
					context.put("formUniqueId", form.getId());
					context.put("userId", user.getId());
					context.put("username", user.getUsername());
					context.put("operatingFunction", operatingFunction);
					context.put("showReadButton", isRead);
					context.put("taskName", task.getName());
					Map<String,Object> pastValuesMap = rtTaskService.checkAndGetPastValuesForInstance(task.getProcessInstanceId(),task.getId());
					try {
						pastValuesJson = CommonUtil.convertMapToJson(pastValuesMap);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					context.put("pastValuesMap", pastValuesMap);
					context.put("pastValuesJson",pastValuesJson);
					taskFormScript = TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showTaskHtmlForm.vm", context);
					JSONArray formFieldAuditValues= rtTaskService.getFormFieldTraceData(task.getProcessInstanceId(), form.getId());
					model.addAttribute("formFieldAuditValues",formFieldAuditValues);
				}else{
					throw new BpmException("No form found with the bounded id "+formKey);
				}				
			}else{
				context.put("html", "No form specified");
				taskFormScript = TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showTaskForm.vm", context);
			}						
			
		}catch(BpmException e){
			log.error(getText("load.readform.fail",e.getMessage(),locale));
			taskFormScript = e.getMessage();			
		}		    	
		return taskFormScript;
	}
	
	/**
	 * <p>To design the task form html script that can be rendered on script</p>   
	 * @param fieldInfo TODO
	 * @param creator TODO
	 * @param taskId for which form associated
	 * @return the task for script that can be rendered
	 */
	private String designTaskForm(User user, Task currentTask,HistoricTaskInstance task, ModelMap model, String templateURL, Map<String, Map<String, Object>> fieldInfo, boolean isStartNodeTask, String creator,Locale locale){
		Map<String, Object> context = new HashMap<String, Object>();
		String taskFormScript = null;
		boolean isSubForm = false ;
    	String subFormCount = null;
		String taskId=task.getId();
		try{
			context.put("businessKey", model.get("businessKey"));
			context.put("taskId", taskId);
			context.put("isFormReadOnly", model.get("isFormReadOnly"));
			context.put("lastOperationPerformed", model.get("lastOperationPerformed"));
			List<Map<String, Object>> peopleDetails = new ArrayList<Map<String,Object>>();
			
			TaskRole taskRole = rtTaskService.getTaskRoleApplicableForUser(user, taskId);
			
			// get scripts in process
			TaskEntity taskEntity = (TaskEntity) taskDefinitionService.getTaskById(taskId);
			if(taskEntity != null) {
				context = rtTaskService.getScriptsInProcess(taskEntity,context);
			} 
			context.put("automatiFielInfo",fieldInfo);
			String formKey = null;
			if(currentTask != null){
				context.put("taskName", currentTask.getName()); // to show task name in opinion list	
				TaskEntity entity = (TaskEntity) currentTask;
				OperatingFunction operatingFunction = rtTaskService.getOperatingFunctionForUser(user, currentTask, taskRole);
				OperatingFunction operatingFunctionForTask = rtTaskService.getOperatingFunction(user, currentTask);
				model.addAttribute("customOfScript",designCustomOperatingFunctions(operatingFunction));
				model.addAttribute("operatingFunction", operatingFunction);
				model.addAttribute("isStartNodeTask", entity.getIsForStartNodeTask());
				if(isStartNodeTask){
					if(creator != null && !creator.isEmpty()){
						if(!creator.equalsIgnoreCase(CommonUtil.getLoggedInUserId())){
							context.put("isCreator", false);
						}
					}else{
						context.put("isCreator", true);
					}
				}
				model.addAttribute("operatingFunctionForTask", operatingFunctionForTask);
				Map<String, FormFieldPermission> nodeLevelFieldPermissions = rtTaskService.getNodeLevelFieldPermissions(user, taskId, isStartNodeTask); 
				context.put("nodeLevelFieldPermissions", nodeLevelFieldPermissions);
				context.put("operatingFunction", operatingFunction);
				context.put("operatingFunctionForTask", operatingFunctionForTask);
				context.put("isStartNodeTask",entity.getIsForStartNodeTask());
				context.put("executionId",task.getExecutionId());
				peopleDetails.add(rtTaskService.getAssigneeDetails(currentTask));
				peopleDetails.addAll(rtTaskService.getInvlovedPeopleDetails(currentTask));
				context.put("isFinalTransactor", rtTaskService.isFinalTransactor(taskId));
				formKey = taskDefinitionService.getFormKey(taskId);
			}else{
				TaskFormData taskFormData = null;
				taskFormData = rtTaskService.getHistoricTaskFormData(task.getId());
				formKey = taskFormData.getFormKey();
				model.addAttribute("customOfScript","");
			}
			
			model.addAttribute("processInstanceId", task.getProcessInstanceId());
			model.addAttribute("executionId", task.getExecutionId());
			ProcessDefinition processDefinition = processDefinitionService.getProcessDefinitionById(task.getProcessDefinitionId());
			
			context.put("processInstanceId", task.getProcessInstanceId());
			context.put("deploymentId", processDefinition.getDeploymentId());
			context.put("processName",  processDefinition.getName());
			context.put("processDefinitionId",  task.getProcessDefinitionId());
			context.put("activityId", task.getTaskDefinitionKey());
			context.put("resourceName", processDefinition.getResourceName());
			context.put("processDefinitionId", processDefinition.getId());

			
			model.addAttribute("deploymentId", processDefinition.getDeploymentId());
			model.addAttribute("processName", processDefinition.getName());
			model.addAttribute("resourceName", processDefinition.getResourceName());
			model.addAttribute("currentTaskName", task.getName());
			model.addAttribute("processDefinitionId", task.getProcessDefinitionId());
			model.addAttribute("activityId", task.getTaskDefinitionKey());
			model.addAttribute("isOrganizer", "false");
			if(taskRole != null){
				if(taskRole.getName().equals(taskRole.ORGANIZER.getName())){
					model.addAttribute("isOrganizer", "true");
				}
			}else {
				boolean isUserAdmin = false;
		        for (Role role : user.getRoles()) {
					if(role.getId().equals(Constants.ADMIN_ROLE)){
						isUserAdmin = true;
						break;
					}
				}
		        if(isUserAdmin){
					model.addAttribute("isOrganizer", "true");
		        }
			}
			context.put("taskId", task.getId());		
			context.put("peopleDetails", peopleDetails);
			JSONObject pastValuesJson = new JSONObject();
			if(formKey!=null){
				MetaForm form = formDefinitionService.getDynamicFormById(formKey);
				if(form!=null){
					if(taskEntity != null) {
						if(taskEntity.getHtmlSourceForSubForm() != null && !taskEntity.getHtmlSourceForSubForm().isEmpty()) {
							if(taskEntity.getFormkey().equalsIgnoreCase(form.getFormName())) {
								context.put("htmlSubForm", taskEntity.getHtmlSourceForSubForm());
								context.put("subForm", true);
								isSubForm = true;
							}
						}
					}
					if(!isSubForm){
						context.put("html", form.getHtmlSource());
					}
					context.put("taskId", taskId);		
					context.put("formId", form.getFormName());
					model.addAttribute("formId",form.getFormName());
					context.put("formUniqueId", form.getId());
					context.put("userId", user.getId());
					context.put("username", user.getUsername());
					
					//get and check any task with the same form
					Map<String,Object> pastValuesMap = rtTaskService.checkAndGetPastValuesForInstance(task.getProcessInstanceId(),task.getId());
					pastValuesJson = CommonUtil.convertMapToJson(pastValuesMap);
					if(taskRole!=null && taskRole.getName().equalsIgnoreCase(TaskRole.COORDINATOR.getName())){
						context.put("formAction", "/bpm/of/save");
					}else{
						context.put("formAction", "/bpm/of/submit");
					}
					context.put("pastValuesJson", pastValuesJson);
			    	subFormCount = (String) pastValuesMap.get("subFormCount"); // setting initial subFormCount to 1.
			    	if(subFormCount != null) {
			    		context.put("subFormCount", Integer.valueOf(subFormCount));
			    	} else {
			    		context.put("subFormCount", "1");
			    	}
					context.put("pastValuesMap", pastValuesMap);
					taskFormScript = TemplateUtil.generateScriptForTemplate(velocityEngine, templateURL, context);
					JSONArray formFieldAuditValues= rtTaskService.getFormFieldTraceData(task.getProcessInstanceId(), form.getId());
					model.addAttribute("formFieldAuditValues",formFieldAuditValues);
				}else{
					throw new BpmException("No form found with the bounded id "+formKey);
				}				
			}else{
				context.put("html", "No form specified");
				taskFormScript = TemplateUtil.generateScriptForTemplate(velocityEngine, templateURL, context);
			}						
			
		}catch(BpmException e){
			log.error(getText("load.readform.fail",e.getMessage(),locale));
			taskFormScript = e.getMessage();			
		}catch(JSONException e){
			log.error("Problem generating json values from map in script forming for task form for task::"+taskId, e);
			taskFormScript = e.getMessage();			
		}		    	
		return taskFormScript;
	}
	
	/**
     * View the proper svgxml and show the task lists process definition
     * @param resourceName
     * @param deploymentId
     * @throws IOException
     */
    @RequestMapping(value="bpm/manageTasks/jumpPreview" , method = RequestMethod.GET )
	public ModelAndView jumpTaskPreview(@RequestParam("processDefinitionId") String processDefinitionId,@RequestParam("resourceName") String resourceName,
			@RequestParam("deploymentId") String deploymentId,@RequestParam("isTitleShow") String isTitleShow,
			@RequestParam("processName") String processName,@RequestParam("taskId") String taskId,
			@RequestParam("activityId") String activityId,@RequestParam("jumpType") String jumpType,@RequestParam("processInstanceId") String processInstanceId,
			@RequestParam("canSave") boolean canSave,@RequestParam("formId") String formId,@RequestParam("executionId") String executionId,
			HttpServletResponse response,ModelMap model, HttpServletRequest request) throws IOException{
    	Locale locale = request.getLocale();
    	try{
			log.debug("process image preview");
			log.debug("for deployment"+deploymentId);
			log.debug("for resourcename"+resourceName);
			String svgString = rtProcessService.getSvgString(deploymentId, resourceName);
			//Generate xml into a single line
			//String cleanedString = svgString.replaceAll("&lt;", "<").trim();
			//replace single quotes into a double quotes
			//String cleanedXmlNew = cleanedString.replaceAll("&gt;", ">");
			List<Map<String,String>> processDefinitionList =  processDefinitionService.getListProcessDefinitionByIdJump(processDefinitionId,activityId,jumpType,processInstanceId,executionId);
			
			model.addAttribute("processDefinitionList",processDefinitionList);
			model.addAttribute("processDefinitionListSize", processDefinitionList.size());
			model.addAttribute("svgString",svgString);
			model.addAttribute("resourceName",resourceName);
			model.addAttribute("deploymentId",deploymentId);
			model.addAttribute("isTitleShow",isTitleShow);
			model.addAttribute("processName",processName);
			model.addAttribute("taskId",taskId);
			model.addAttribute("jumpType",jumpType);
			model.addAttribute("canSave",canSave);
			model.addAttribute("formId",formId);
			model.addAttribute("processDefinitionId",processDefinitionId);
		      
		}catch(Exception e){
			saveMessage(request, getText("jumpPreview.error",locale));
			log.error(getText("jumpPreview.error",jumpType,locale));
			//return new ModelAndView(new RedirectView("/bpm/manageProcess/listProcess"));
		}
		return new ModelAndView("process/jumpPreview",model);
	}
	
	@RequestMapping(value = "bpm/manageTasks/showOlderTaskForm", method = RequestMethod.GET)
    public ModelAndView showOlderTaskForm(@RequestParam("taskId") String taskId,
        ModelMap model,HttpServletRequest request){
		
		String script=null;			
		try{
			Task task = taskDefinitionService.getTaskById(taskId);
			Map<String, Object>properties = CommonUtil.getStringRepresentations(rtProcessService.getPrevHistoricVariablesForInstance(task.getProcessInstanceId(), taskId));//task.getProcessInstanceId()));
			HistoricTaskInstance prevTask = rtTaskService.getHistoricTaskInstanceByTaskId((String)properties.get("taskId"));	
			ProcessDefinition processDefinition = processDefinitionService.getProcessDefinitionById(task.getProcessDefinitionId());
			if(prevTask!=null){
				model.addAttribute("task", prevTask);
				model.addAttribute("script", script);
				
				model.addAttribute("deploymentId", processDefinition.getDeploymentId());
				model.addAttribute("resourceName", processDefinition.getResourceName());
				model.addAttribute("currentTaskName", task.getName());
				model.addAttribute("processDefinitionId", task.getProcessDefinitionId());
				model.addAttribute("activityId", task.getTaskDefinitionKey());
				
				User user = CommonUtil.getLoggedInUser();
				
				TaskRole taskRole = rtTaskService.getTaskRoleApplicableForUser(user, taskId);
				
				if(taskRole.getName().equals(taskRole.ORGANIZER.getName())){
					model.addAttribute("isOrganizer", "true");
				}else {
					model.addAttribute("isOrganizer", "false");
				}
				
				script = TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showEditTaskHtmlForm.vm", rtProcessService.designUpdateTaskProcessScriptContext(task, prevTask, properties,processDefinition, velocityEngine,"bpm/form/updateTaskForm",model, false, null));
			}else{
			saveError(request, "No previous task found");
			return new ModelAndView("redirect:mybucket");
			}
	    	
		}catch (Exception e) {
			// TODO: handle exception
			saveError(request, e.getMessage());
			log.error(e.getMessage(), e);
			return new ModelAndView("redirect:mybucket");
		}
		return new ModelAndView("showTasksEditForm",model);
    	
    }
	
	/**
	 *  Displays all the task 
	 * 
	 * @param model
	 * @return view containing my buckets, involvements, waiting list  
	 * @throws Exception
	 */
	@RequestMapping(value="bpm/manageTasks/{type}", method = RequestMethod.GET)
	public ModelAndView showTask(@PathVariable String type, ModelMap model, HttpServletRequest request) {
//		log.info(" Get Task type:"+type);
		Locale locale =  request.getLocale();
		model.addAttribute("type", type);
		String status = "";
		try{
			status = request.getParameter("statusMsg");
			if(status != null && !status.isEmpty()) {
				model.addAttribute("newTab","true");
				model.addAttribute("statusMsg",status);
			} else {
				model.addAttribute("newTab","false");
			}
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Task> tasks = rtTaskService.getMyTaskByType(user, type);
			
			if(type.equalsIgnoreCase("mybucket")){
				
				//判断当前登录用户是否有秘书
				if(user.getSecretary() != null && !user.getSecretary().isEmpty()){
					model.addAttribute("isSecretaryExist", "true");
					model.addAttribute("substitute", user.getSecretary());
				}else{
					model.addAttribute("isSecretaryExist", "false");
				}
			}
			
			model.addAttribute("gridType", type);			
			String script = generateTasksGrid(tasks, user, type, locale);
			String gridTitle = "";
			ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY,locale); 
			if(type.equalsIgnoreCase("mybucket") || type.equalsIgnoreCase("documentToBeDone")){        
				gridTitle = appResourceBundle.getString("task.myBuckets");
			} else if(type.equalsIgnoreCase("myinvolvements")){
				gridTitle = appResourceBundle.getString("task.myInvolvements");
			} else if(type.equalsIgnoreCase("waitinglist")){
				gridTitle = appResourceBundle.getString("task.waitingList");
			} else if(type.equalsIgnoreCase("toRead")){
				gridTitle = appResourceBundle.getString("task.toRead");
			} else if(type.equalsIgnoreCase("readedList")){
				gridTitle = appResourceBundle.getString("task.readList");
			} else if(type.equalsIgnoreCase("toReadDocument")){
				gridTitle = appResourceBundle.getString("task.toRead");
			} else if(type.equalsIgnoreCase("readDocumentList")){
				gridTitle = appResourceBundle.getString("task.readList");
			} else if(type.equalsIgnoreCase("myOwnedTasks")){
				gridTitle = appResourceBundle.getString("task.myOwnedTasks");
			} else if(type.equalsIgnoreCase("doneDocumentList")){
				gridTitle =appResourceBundle.getString("task.doneList");
			} else if(type.equalsIgnoreCase("doneList")){
				gridTitle = appResourceBundle.getString("task.doneList");
			} else if(type.equalsIgnoreCase("cancelDocumentList")) {
				gridTitle = appResourceBundle.getString("task.cancelDocumentList");
			}
			model.addAttribute("jsonTree", ProcessDefinitionUtil.getJsonTreeForProcessList(request));
			model.addAttribute("gridTitle", gridTitle);
			model.addAttribute("script", script);
		}catch(BpmException e){
            log.error(getText("getTask.error",e.getMessage()+" Type: "+type,locale));
            model.addAttribute("statusMsg",status);
			model.addAttribute("script", "Problem getting entries for "+type);
		}catch(EazyBpmException e){
            model.addAttribute("statusMsg",status);
            log.error(getText("getTask.error",e.getMessage()+" Type: "+type,locale));
			model.addAttribute("script", e.getMessage());
		}catch(Exception e){
			e.printStackTrace();
            model.addAttribute("statusMsg",status);
            log.error(getText("getTask.error",e.getMessage()+" Type: "+type,locale));
		}
		return new ModelAndView("showTask",model);
	}		
	
	/**
	 * 设置待办任务的优先级
	 * @param taskId
	 * @param priority
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="bpm/manageTasks/priority", method = RequestMethod.GET)
	public String taskPriority(String taskId, Integer priority, ModelMap model, HttpServletRequest request) {
		rtTaskService.taskPriority(taskId, priority);
		saveMessage(request, "任务重要程度标记成功。");
		return "redirect:mybucket";
	}		
	
	/**
	 *  Get my bucket data
	 * 
	 * @param model
	 * @return view containing my bucket list  
	 * @throws Exception
	 */
	@RequestMapping(value="bpm/manageTasks/getMyBucket", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> getMyBucket(ModelMap model, HttpServletRequest request,@RequestParam("type") String type) {
		Locale locale = request.getLocale();
		List<Map<String, Object>> taskDetails = new ArrayList<Map<String,Object>>();
		try{
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Task> tasks = rtTaskService.getMyTaskByType(user, type);
			taskDetails = rtTaskService.resolveTaskDetails(tasks, user, type);
//			log.info("Task Processed for My Bucket");
		}catch(EazyBpmException e){
            log.error(getText("getTask.error",e.getMessage()+" Type: "+type,locale));
		}catch(Exception e){
			 log.error(getText("getTask.error",e.getMessage()+" Type: "+type,locale));
		}
		return taskDetails;
	}	
	
	/**
	 * <p>Marks the task as completed so that the action moves to the next step after
	 * making the necessary attributes of the task updated for completion</p>
	 * @param request the input params from screen
	 * @param model
	 * @return
	 */
	@RequestMapping(value="bpm/manageTasks/completeTask", method = RequestMethod.POST)
	public String completeTask(@RequestParam("taskId") String taskId, HttpServletRequest request, ModelMap model) {	
		 HistoricTaskInstance historicTaskInstance = null;
		 HistoricProcessInstance historicProcessInstance = null;
		 Locale locale = request.getLocale();
		try{
			Map<String, String>rtFormValues = CommonUtil.getKeyValuePairs(request.getParameterMap());
			//String taskId = rtFormValues.get("taskId");
			taskDefinitionService.setOwnerIfEmpty(taskId);
			rtTaskService.completeTask(taskId);
			historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).list().get(0);
			  if(historicTaskInstance != null){
				  historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).list().get(0);				  
				  if(historicProcessInstance.getEndTime() != null ){
					  saveMessage(request, getText("process.completed",historicTaskInstance.getName(), locale));
				  }else{
					  saveMessage(request, getText("task.completed",historicTaskInstance.getName(), locale));
				  }
			  }
			log.info("Task completed for id "+taskId);	
		}catch(EazyBpmException e){
			log.error(e.getMessage(), e);
			model.addAttribute("message", e.getMessage());
		}catch(BpmException bpme){
			log.error(bpme.getMessage(), bpme);
			model.addAttribute("message", bpme.getMessage());
		}
		return "redirect:mybucket";
	}
	
	/**
	 * <p>Add the user to a particular task by id, use can be a Owner or Assignee or Ivvolved based 
	 * on the Task Role(Identity Link Type)</p> 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="bpm/manageTasks/addMember", method = RequestMethod.POST)
	public ModelAndView addMember(HttpServletRequest request, ModelMap model) {
		String taskId = null;
		try{
			Map<String, String>rtFormValues = CommonUtil.getKeyValuePairs(request.getParameterMap());
			taskId = rtFormValues.get("taskId");
			String userId = rtFormValues.get("userId");
			String identityLinkType = rtFormValues.get("identityLinkType");
			String type = rtFormValues.get("type");
			if(userId != null && userId.isEmpty()){
				userId=null;
			}
			rtTaskService.addMember(taskId, userId, type, identityLinkType);
			model.addAttribute("gridType", "myInstances");
		}catch(BpmException e){
			log.error(e.getMessage(), e);
		}
		return showTaskDetail(taskId, model,request);
	}
	
	
	/**
	 * <p>Add the user to a particular task by id, use can be a Assignee  
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="bpm/manageTasks/assignSubstitute", method = RequestMethod.GET)
	public ModelAndView assignSubstitute(HttpServletRequest request, ModelMap model) {
		String taskId = null;
		List<String> taskIds = new ArrayList<String>();
		try{
			Map<String, String>rtFormValues = CommonUtil.getKeyValuePairs(request.getParameterMap());
			taskId = rtFormValues.get("taskId");
			if (taskId.contains(",")) {
                String[] ids = taskId.split(",");
                for(String id :ids){
                	taskIds.add(id);
                }
              } else {
            	  taskIds.add(taskId);
              }
			String userId = rtFormValues.get("assignee");
			log.info(" New Assignee  : "+  userId +" to task id  : "+taskIds);
			if(userId != null && userId.isEmpty()){
				userId=null;
			}
			rtTaskService.assignSubstitute(taskIds, userId);
			model.addAttribute("gridType", "myInstances");
		}catch(BpmException e){
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("redirect:mybucket");
	}
	
	/**
	 * <p>Remove the  the user to a particular task by id, use can be a Owner or Assignee or Ivvolved based 
	 * on the Task Role(Identity Link Type)</p> 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="bpm/TaskDetails/removeMember", method = RequestMethod.GET)
	public ModelAndView removeMember(HttpServletRequest request, ModelMap model) {
		String taskId = null;
		try{
			Map<String, String>rtFormValues = CommonUtil.getKeyValuePairs(request.getParameterMap());
			taskId = rtFormValues.get("taskId");
			String userId = rtFormValues.get("userId");
			String identityLinkType = rtFormValues.get("identityLinkType");
			rtTaskService.removeMember(taskId, userId, identityLinkType);
		}catch(BpmException e){
			log.error(e.getMessage(), e);
		}
		return showTaskDetail(taskId, model,request);
	}
	
	
	/**
	 * <p>The task is marked as claimed for the current user sending the claim request</p>
	 * @param id of the task to claim
	 * @param model
	 * @return
	 */
	@RequestMapping(value="bpm/manageTasks/claimTask", method = RequestMethod.GET)
	public String claimTask(@RequestParam("taskId") String id, ModelMap model, HttpServletRequest request) {
		try{
			User user = CommonUtil.getLoggedInUser();
			rtTaskService.claimTask(id, user);
			saveMessage(request, getText("task.claimed", request.getLocale()));
		}catch(BpmException e){
			log.error(e.getMessage(), e);
			model.addAttribute("message", e.getMessage());
		}
		return "redirect:mybucket";
	}
	
	/**
	 * <p>Generates the actual grid template using the details got from the task entities, depending on the type
	 * of task requested</p>
	 * @param tasks list of tasks got
	 * @param user logged in user
	 * @param type may be waiting tasks, claimed tasks, involved tasks etc
	 * @return the template script
	 */
	private String generateTasksGrid(List<Task>tasks, User user, String type , Locale locale){
		String script = null;
		try{
			List<Map<String, Object>> taskDetails = rtTaskService.resolveTaskDetails(tasks, user,type);
			script = TaskUtil.generateScriptForTask(velocityEngine, taskDetails, type,locale);
//			log.info("Task details Retrived");
		}catch(BpmException e){
			script="Problem getting task details, check log for errors";
			log.error(e.getMessage(), e);
		}catch(EazyBpmException ee){
			script = "Problem generating task grid, check log for errors";
			log.error(ee.getMessage(), ee);
		}
		return script;
	}
	
	/**
	 * <p>Gets the various type of people details related to task like owner, assignee etc 
	 * and generates the people info template to render on screen</p>
	 * 
	 * @param task for which people info needed
	 * @return the template as script
	 */
	private String designMembersGrid(Task task ,HttpServletRequest request){		
		List<Map<String, Object>> peopleDetails = new ArrayList<Map<String,Object>>();
		Map<String, Object> context = new HashMap<String, Object>();
		String script = null;
		try{
			peopleDetails.add(rtTaskService.getOwnerDetails(task));
			peopleDetails.add(rtTaskService.getAssigneeDetails(task));
			peopleDetails.addAll(rtTaskService.getInvlovedPeopleDetails(task));
			context.put("taskId", task.getId());
			
			context.put("taskNoOwner", getText("task.no.owner", request.getLocale()));
			context.put("taskAssignee", getText("task.assignee", request.getLocale()));
			context.put("taskNoAssignee", getText("task.no.assignee", request.getLocale()));
			context.put("taskInvolveMembers", getText("task.involve.members", request.getLocale()));
			context.put("taskOwner", getText("task.owner", request.getLocale()));
	
			context.put("peopleDetails", peopleDetails);
			
			script = TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showTaskPeopleDetail.vm", context);
			log.info("Member Detail retrived SuccessFully");
		}catch(BpmException e){
			script = "Problem getting member details for task:"+e.getMessage();
			log.error(e.getMessage(),e);
		}		    	
		return script;
	}
	
	/**
	 * Back off functionality. when submitting the returned process, user can select one of the previously operated tasks.
	 * User who returned the task will be shown as potential organizer.
	 */
	 @RequestMapping(value="bpm/manageTasks/backOffJumpPreview" , method = RequestMethod.GET )
		public ModelAndView backOffJumpPreview(@RequestParam("processDefinitionId") String processDefinitionId,@RequestParam("resourceName") String resourceName,
				@RequestParam("deploymentId") String deploymentId,@RequestParam("isTitleShow") String isTitleShow,
				@RequestParam("processName") String processName,@RequestParam("taskId") String taskId,
				@RequestParam("activityId") String activityId,@RequestParam("jumpType") String jumpType,@RequestParam("processInstanceId") String processInstanceId,
				@RequestParam("formId") String formId,
				HttpServletResponse response,ModelMap model, HttpServletRequest request) throws IOException{
		 Locale locale = request.getLocale();
	    	try{
				log.debug("process image preview");
				log.debug("for deployment"+deploymentId);
				log.debug("for resourcename"+resourceName);
				String svgString = rtProcessService.getSvgString(deploymentId, resourceName);
				List<Map<String,String>> processDefinitionList =  processDefinitionService.getProcessDefinitionForBackOffJump(processDefinitionId,activityId,jumpType,processInstanceId);
				model.addAttribute("processDefinitionList",processDefinitionList);
				model.addAttribute("processDefinitionListSize", processDefinitionList.size());
				model.addAttribute("svgString",svgString);
				model.addAttribute("resourceName",resourceName);
				model.addAttribute("deploymentId",deploymentId);
				model.addAttribute("isTitleShow",isTitleShow);
				model.addAttribute("processName",processName);
				model.addAttribute("taskId",taskId);
				model.addAttribute("jumpType","backOff");
				model.addAttribute("canSave",true);
				model.addAttribute("formId",formId);
			      
			}catch(Exception e){
				log.error(getText("jumpPreview.error",jumpType,locale));
				saveMessage(request, getText("jumpPreview.error",locale));
			}
			return new ModelAndView("process/jumpPreview",model);
		}
	@Autowired
	public void setTaskDefinitionService(TaskDefinitionService taskDefinitionService) {
		this.taskDefinitionService = taskDefinitionService;
	}	
	
	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }	

	@Autowired
	public void setRtTaskService(TaskService rtTaskService) {
		this.rtTaskService = rtTaskService;
	}

	@Autowired
	public void setProcessDefinitionService(
			ProcessDefinitionService processDefinitionService) {
		this.processDefinitionService = processDefinitionService;
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
	
	@Autowired
	public void setFormDefinitionService(FormDefinitionService formDefinitionService) {
		this.formDefinitionService = formDefinitionService;
	}
	
	@Autowired
	public void setUserOpinionService(UserOpinionService userOpinionService) {
		this.userOpinionService = userOpinionService;
	}
}
