package com.eazytec.webapp.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.task.OperatingFunctionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.OperatingFunction;
import org.activiti.engine.task.Task;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.department.service.DepartmentService;
import com.eazytec.bpm.common.util.FormDefinitionUtil;
import com.eazytec.bpm.common.util.ProcessDefinitionUtil;
import com.eazytec.bpm.common.util.TaskUtil;
import com.eazytec.bpm.metadata.process.service.ProcessDefinitionService;
import com.eazytec.bpm.metadata.task.service.TaskDefinitionService;
import com.eazytec.bpm.opinion.service.UserOpinionService;
import com.eazytec.bpm.runtime.form.service.FormService;
import com.eazytec.bpm.runtime.task.service.OperatingFunctionService;
import com.eazytec.bpm.runtime.task.service.TaskService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.FileUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.common.util.StringUtil;
import com.eazytec.common.util.TemplateUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceException;
import com.eazytec.exceptions.DataSourceValidationException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Classification;
import com.eazytec.model.LabelValue;
import com.eazytec.model.MetaForm;
import com.eazytec.model.OperatingFunctionAudit;
import com.eazytec.model.Opinion;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.service.NewsService;
import com.eazytec.service.UserManager;
import com.eazytec.util.DateUtil;

/**
 * <p>
 * The controller for process related operations like its CRUD, list, grid view
 * etc on screen
 * </p>
 *
 * @author Karthick
 * @author madan
 * @author Revathi
 * @author nkumar
 */

@Controller
public class ProcessController extends BaseFormController implements HandlerExceptionResolver {

	/**
	 * Log variable for all child classes. Uses LogFactory.getLog(getClass())
	 * from Commons Logging
	 */
	protected final Log log = LogFactory.getLog(getClass());

	private ProcessDefinitionService processDefinitionService;
	private com.eazytec.bpm.runtime.process.service.ProcessService rtProcessService;
	private TaskService rtTaskService;
	public VelocityEngine velocityEngine;
	private TaskDefinitionService taskDefinitionService;
	private OperatingFunctionService operatingFunctionService;;
	private UserOpinionService userOpinionService;
	private DepartmentService departmentService;
	private FormService rtFormService;
	private RuntimeService runtimeService;
	private NewsService newsService;
	private UserManager userManager;

	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	@Autowired
	public void setNewsService(NewsService newsService) {
		this.newsService = newsService;
	}

	@Autowired
	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	@Autowired
	public void setRtFormService(FormService rtFormService) {

		this.rtFormService = rtFormService;
	}

	/**
	 * Displays all the process related to an user on login
	 *
	 * @param model
	 * @return view containing list of processes for a logged in user.
	 * @throws Exception
	 */
	@RequestMapping(value = "bpm/showProcess/{type}", method = RequestMethod.GET)
	public ModelAndView displayProcessesDefinition(@PathVariable String type, ModelMap model, HttpServletRequest request) {
		String redirectPage = null;
		Locale locale = request.getLocale();
		try {
			User user = CommonUtil.getLoggedInUser();
			// String loggedInUser = user.getUsername();
			List<ProcessDefinition> processDefinitions = processDefinitionService.getProcessesDefinitionsByUser(user);

			String script = designProcessDefinitionsGridScript(processDefinitions, type, locale);
			model.addAttribute("script", script);
			if (type.contentEquals("listProcess") || type.contentEquals("processDefinitions")) {
				// Get the JSON data for organization tree structure
				model.addAttribute("jsonTree", ProcessDefinitionUtil.getJsonTreeForProcessList(request));
				if (type.contentEquals("listProcess"))
					model.addAttribute("gridType", "listprocess");
				redirectPage = "manageProcess";
			} else {
				redirectPage = "showProcess";
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			model.addAttribute("script", getText("process.log.error", locale));
		}
		model.addAttribute("type", type);
		model.addAttribute("fileUpload", new FileUpload());
		return new ModelAndView(redirectPage, model);
	}

	@RequestMapping(value = "bpm/showProcess/getProcessDefinitionsByName", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getProcessDefinitionsByName(@RequestParam("processDefName") String processDefName, ModelMap model, HttpServletRequest request) {
		List<ProcessDefinition> processes = rtProcessService.getProcessDefinitionsByName(processDefName);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		for (ProcessDefinition processDefinition : processes) {
			if (processDefinition.isActiveVersion() == true) {
				resultMap.put("id", processDefinition.getId());
				resultMap.put("hasStartFormKey", processDefinition.hasStartFormKey());
				resultMap.put("isSystemDefined", false);
				break;
			}
		}
		return resultMap;
	}

	/**
	 * <p>
	 * Imports file gives by the user, saves it and reads it in bytes and then
	 * Deploys the processs created by an user
	 * </p>
	 * 
	 * @param model
	 * @return deploys a process and return to the same page from where it is
	 *         called.
	 * @throws Exception
	 */

	@RequestMapping(value = "bpm/showProcess/deployBpmnFile", method = RequestMethod.POST)
	public ModelAndView deployBPMNSourceFile(FileUpload fileUpload, BindingResult errors, HttpServletRequest request, ModelMap model) {
		if (request.getParameter("cancel") != null) {
			// return getCancelView();
			return new ModelAndView("showProcess", model);
		}
		String message = "";
		Locale locale = request.getLocale();
		try {

			if (validator != null) { // validator is null during testing
				validator.validate(fileUpload, errors);

				if (errors.hasErrors()) {
					saveError(request, getText("process.upload.error", locale));
					// return "redirect:listProcess";
					return new ModelAndView("showProcess", model);
					// return "fileupload";
				}

			}

			// validate a file was entered
			if (fileUpload.getFile().length == 0) {
				Object[] args = new Object[] { getText("uploadForm.file", request.getLocale()) };
				errors.rejectValue("file", "errors.required", args, "File");

				// return "fileupload";
				saveError(request, getText("process.upload.validFile", locale));
				// return "redirect:listProcess";
				return new ModelAndView("showProcess", model);
			}

			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			// Getting file list which user upload
			Map<String, List<MultipartFile>> fileInList = multipartRequest.getMultiFileMap();
			List<MultipartFile> files = fileInList.get("file");
			// the directory to upload to
			String uploadDir = getServletContext().getRealPath("/resources") + "/" + request.getRemoteUser() + "/";
			// Create the directory if it doesn't exist
			File dirPath = new File(uploadDir);
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}
			// iterate the list of file and create the file in specified path
			// and deploy it
			if (null != files && files.size() > 0) {
				for (MultipartFile multipartFile : files) {
					if (multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()) {
						// retrieve the file data
						InputStream stream = multipartFile.getInputStream();
						// write the file to the file specified
						OutputStream bos = new FileOutputStream(uploadDir + multipartFile.getOriginalFilename());
						int bytesRead;
						byte[] buffer = new byte[8192];
						while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
							bos.write(buffer, 0, bytesRead);
						}
						bos.close();
						// close the stream
						stream.close();
						String fileNameWithPath = dirPath.getAbsolutePath() + Constants.FILE_SEP + multipartFile.getOriginalFilename();
						String name = fileUpload.getName();
						String description = fileUpload.getDescription();

						if (fileNameWithPath.endsWith(".xml")) {
							// bpmn deploy

							processDefinitionService.deployBPMNSourceFile(name, multipartFile.getOriginalFilename(), fileNameWithPath, description, fileUpload.getClassificationId(), false);
						} else if (fileNameWithPath.endsWith(".zip")) {
							// zip file import
							message = importProcessZipFile(request, fileNameWithPath, name, description, fileUpload);
						}
					}
				}
			}
			if (message != null && !message.isEmpty()) {
				saveError(request, "" + message);
			} else {
				saveMessage(request, getText("file.upload", request.getLocale()));
			}
		} catch (ActivitiException e) {
			saveError(request, getText("process.file.upload.error", e.getMessage(), locale));
			log.error(e.getMessage(), e);
		} catch (BpmException e) {
			saveError(request, getText("process.file.upload.error", e.getMessage(), locale));
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			saveError(request, getText("importBpmn.error", e.getMessage(), locale));
			log.error(e.getMessage(), e);
		}
		// return "redirect:listProcess";
		return new ModelAndView("showProcess", model);

	}

	/*
	 * Import .zip file in processList
	 */
	private String importProcessZipFile(HttpServletRequest request, String fileNameWithPath, String name, String description, FileUpload fileUpload) throws IOException {
		StringBuffer message = new StringBuffer();
		// get the zip file content
		ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(fileNameWithPath));
		// get the zipped file list entry
		ZipEntry zipEntry = zipInputStream.getNextEntry();
		Locale locale = request.getLocale();
		int zipFileCount = 0;
		try {
			while (zipEntry != null) {
				String fileName = zipEntry.getName();
				// Output Directory where files are saved
				String outputFolder = getServletContext().getRealPath("/resources") + "/" + request.getRemoteUser() + "/";
				// Create the directory if it doesn't exist
				File dirPath = new File(outputFolder);
				if (!dirPath.exists()) {
					dirPath.mkdirs();
				}
				File newFile = new File(outputFolder + File.separator + fileName);
				// write the file
				FileOutputStream fos = new FileOutputStream(newFile);
				byte[] buffer = new byte[8192];
				int length;
				while ((length = zipInputStream.read(buffer)) > 0) {
					fos.write(buffer, 0, length);
				}
				fos.close();
				log.info("file unzip : " + newFile.getAbsoluteFile().getName());
				// get zip file name with path
				String fileNameWithPathForZipFile = dirPath.getAbsolutePath() + Constants.FILE_SEP + newFile.getAbsoluteFile().getName();
				// bpmn deploy
				if (fileName.endsWith(".bpmn20.xml")) {
					processDefinitionService.deployBPMNSourceFile(name, newFile.getAbsoluteFile().getName(), fileNameWithPathForZipFile, description, fileUpload.getClassificationId(), false);
					zipFileCount++;
				} else {
					message.append("Invalid File Format : " + newFile.getAbsoluteFile().getName() + " is not in .bpmn20.xml format");
				}
				zipEntry = zipInputStream.getNextEntry();
			}
		} catch (IOException ex) {
			saveError(request, getText("importBpmn.error", ex.getMessage(), locale));
			log.error(ex.getMessage(), ex);
		} finally {
			zipInputStream.closeEntry();
			zipInputStream.close();
		}
		return message.toString();
	}

	/**
	 * File upload maximum size exception caught and shows the user
	 */
	public ModelAndView resolveException(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception exception) {

		Map<Object, Object> model = new HashMap<Object, Object>();
		try {
			if (exception instanceof MaxUploadSizeExceededException) {
				model.put("errors", "File size should be less then " + ((MaxUploadSizeExceededException) exception).getMaxUploadSize() + " byte.");
			} else {
				model.put("errors", "Unexpected error: " + exception.getMessage());
			}
			model.put("fileUpload", new FileUpload());
			model.put("type", "import");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return new ModelAndView("showProcess", (Map) model);
	}

	/**
	 * Insert the Identity link for default task start node
	 * 
	 * @param taskId
	 * @param nextTaskOrganizers
	 * @param nextTaskCoordinators
	 * @param nextTaskOrganizerOrders
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/manageProcess/assignOrganizerToTask", method = RequestMethod.GET)
	public ModelAndView assignOrganizerToTask(@RequestParam("taskId") String taskId, @RequestParam("nextTaskOrganizers") String nextTaskOrganizers, @RequestParam("nextTaskCoordinators") String nextTaskCoordinators, @RequestParam("nextTaskOrganizerOrders") String nextTaskOrganizerOrders, ModelMap model, HttpServletRequest request) {
		TaskEntity taskEntity = (TaskEntity) taskDefinitionService.getTaskById(taskId);
		Locale locale = request.getLocale();
		if (!StringUtil.isEmptyString(nextTaskOrganizers)) {
			try {

				rtProcessService.setTaskDefinition(taskEntity);
				TaskDefinition taskDef = taskEntity.getTaskDefinition();
				operatingFunctionService.setJointConductionByOrder(taskEntity.getProcessInstanceId(), nextTaskOrganizers, nextTaskCoordinators, nextTaskOrganizerOrders, false, taskDef.getReferenceRelation(), false, false);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		model.put("isFromDoneList", false);
		model.put("hasForm", false);
		model.put("processInstanceId", taskEntity.getProcessInstanceId());
		saveMessage(request, getText("process.started", locale));
		return new ModelAndView("startProcess", model);
	}

	/**
	 * Starts the process instance.
	 * 
	 * @param model
	 * @return starts a process instance and return to the same page from where
	 *         it is called.
	 * @throws Exception
	 */
	@RequestMapping(value = "bpm/manageProcess/startProcessInstance", method = RequestMethod.GET)
	public ModelAndView startProcessInstanceByKey(@RequestParam("nextTaskOrganizers") String nextTaskOrganizers, @RequestParam("nextTaskCoordinators") String nextTaskCoordinators, @RequestParam("processKey") String processKey, @RequestParam("nextTaskOrganizerOrders") String nextTaskOrganizerOrders, @RequestParam("isSystemDefined") boolean isSystemDefined, @RequestParam("processInstanceId") String processInstanceId, @RequestParam("isFromDoneList") boolean isFromDoneList, @RequestParam("creator") String creator, ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		try {
			ProcessDefinition processDefinition = processDefinitionService.getProcessDefinitionById(processKey);
			String processStartScript = "";
			boolean hasForm = false;
			StartFormData startForm = rtProcessService.getStartForm(processKey);
			if (startForm != null && ((startForm.getFormProperties() != null && startForm.getFormProperties().size() > 0) || startForm.getFormKey() != null)) {
				String formReadOnly = request.getParameter("formReadOnly");
				boolean isFormReadOnly = false;
				if (formReadOnly != null && formReadOnly.equals("true")) {
					isFormReadOnly = true;
				}
				processStartScript = designStartProcessScript(processDefinition, isSystemDefined, processInstanceId, isFromDoneList, creator, isFormReadOnly);
				hasForm = true;

				// show input opinion and work flow trace for returned process
				if (processInstanceId != null && !processInstanceId.isEmpty()) {
					model.addAttribute("isFormReadOnly", isFormReadOnly);
					List<Opinion> opinionList = userOpinionService.getOpinion(processInstanceId);
					rtTaskService.setInstanceTraceData(processKey, processInstanceId, null, model, processDefinition.getName(), null, null);
					model.addAttribute("opinion", new Opinion());
					model.addAttribute("opinionList", opinionList);

				}

			} else if (!isFromDoneList) {
				ProcessInstance processInstance = rtProcessService.startProcessInstanceById(processKey);
				// Get End scripts present in start node
				if (processInstance.getProperties().containsKey("endScript")) {
					Map<String, String> endScriptContent = (Map<String, String>) processInstance.getProperties().get("endScript");
					if (endScriptContent.containsKey("functionName") && endScriptContent.containsKey("script")) {
						endScriptContent.put("endScript", endScriptContent.get("script"));
						endScriptContent.put("endScriptName", endScriptContent.get("functionName"));
						model = rtProcessService.setEndEventScript(model, endScriptContent);
					}
				}
				if (!StringUtil.isEmptyString(nextTaskOrganizers)) {
					operatingFunctionService.setJointConductionByOrder(processInstance.getId(), nextTaskOrganizers, nextTaskCoordinators, nextTaskOrganizerOrders, true, processInstance.getReferenceRelation(), false, false);
				}
				if (processInstance != null) {
					// Operating function audit log
					User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(), null, processInstance.getProcessDefinitionId(), OperatingFunction.CREATE, processInstance.getId(), null);
					processDefinitionService.saveOperatingFunctionAudit(operFunAudit);

					model.addAttribute("processInstanceId", processInstance.getId());
					if (processInstance.isEnded()) {
						saveMessage(request, processDefinition.getDisplayName() + " Completed Successfully");
					} else {
						saveMessage(request, processDefinition.getDisplayName() + " Started Successfully");
						model.addAttribute("hasForm", "false");
					}
				}
			}
			if (isFromDoneList) {
				User user = CommonUtil.getLoggedInUser();
				Task currentTask = taskDefinitionService.getTaskByProcessInstanceId(processInstanceId);
				if (currentTask != null) {
					if (((TaskEntity) currentTask).getSuspensionState() == SuspensionState.ACTIVE.getStateCode()) {
						OperatingFunction operatingFunction = new OperatingFunctionImpl();
						OperatingFunction operatingFunctionForTask = rtTaskService.getOperatingFunctionForStartNode(user, currentTask);
						model.addAttribute("operatingFunction", operatingFunction);
						model.addAttribute("operatingFunctionForTask", operatingFunctionForTask);
						model.addAttribute("resourceName", processDefinition.getResourceName());
						model.addAttribute("processInstanceId", processInstanceId);
						model.addAttribute("taskId", currentTask.getId());
						model.addAttribute("currentTaskName", currentTask.getName());
						model.addAttribute("processDefinitionId", currentTask.getProcessDefinitionId());
					}
				}
				model.addAttribute("customOfScript", Constants.EMPTY_STRING);
			}
			model.addAttribute("isFromDoneList", isFromDoneList);
			model.addAttribute("script", processStartScript);
			model.addAttribute("hasForm", hasForm);
		} catch (BpmException be) {
			saveError(request, getText("process.started.error", be.getMessage(), locale));
			log.error(be.getMessage(), be);
		} catch (Exception e) {
			saveError(request, getText("process.start.error", locale));
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("startProcess", model);
	}

	@RequestMapping(value = "bpm/manageProcess/getOrganizersForStartProcess", method = RequestMethod.GET)
	public @ResponseBody Set<Map<String, String>> getOrganizersForStartProcess(@RequestParam("processKey") String processKey, ModelMap model, HttpServletRequest request) {
		List<Map<String, String>> organizerList = new ArrayList<Map<String, String>>();
		Map<String, String> responseMap = new HashMap<String, String>();
		Set<Map<String, String>> organizerSet = new HashSet<Map<String, String>>();
		Locale locale = request.getLocale();
		try {
			organizerList = operatingFunctionService.getOrganizersForProcessStart(processKey, null);
			if (!organizerList.isEmpty() && organizerList != null) {
				organizerSet.addAll(organizerList);
			}
		} catch (BpmException e) {
			responseMap.put("error", getText("process.started.error", e.getMessage(), locale));
			organizerSet.add(responseMap);
			log.error(e.getMessage(), e);
		} catch (DataSourceValidationException e) {
			responseMap.put("error", e.getMessage());
			log.error(e.getMessage(), e);
			organizerSet.add(responseMap);
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("error", getText("process.start.error", locale));
			organizerSet.add(responseMap);
			log.error(e.getMessage(), e);
		}
		return organizerSet;
	}

	/**
	 * Get organizer for next task if there is any condition
	 * 
	 * @param processKey
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/manageProcess/getOrganizersForStartForm", method = RequestMethod.POST)
	public @ResponseBody Set<Map<String, String>> getOrganizersForStartForm(ModelMap model, HttpServletRequest request) {
		List<Map<String, String>> organizerList = new ArrayList<Map<String, String>>();
		Map<String, String> responseMap = new HashMap<String, String>();
		Set<Map<String, String>> organizerSet = new HashSet<Map<String, String>>();
		Locale locale = request.getLocale();
		try {
			Map<String, Object> rtFormValues = FormDefinitionUtil.getFormParamsAsObjectMap(request.getParameterMap());
			// setting Potential organiser initially
			organizerList = operatingFunctionService.getOrganizersForProcessStart(rtFormValues.get("processDefinitionId").toString(), rtFormValues);
			if (!organizerList.isEmpty() && organizerList != null) {
				organizerSet.addAll(organizerList);
			}
		} catch (BpmException e) {
			responseMap.put("error", getText("process.started.error", e.getMessage(), locale));
			organizerSet.add(responseMap);
			log.error(e.getMessage(), e);
		} catch (DataSourceException e) {
			responseMap.put("error", e.getMessage());
			log.error(e.getMessage(), e);
			organizerSet.add(responseMap);
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("error", getText("process.start.error", locale));
			organizerSet.add(responseMap);
			log.error(e.getMessage(), e);
		}
		return organizerSet;
	}

	/**
	 * Manage all the process
	 *
	 * @param model
	 * @return view containing list process
	 * @throws Exception
	 */
	@RequestMapping(value = "bpm/manageProcess/listProcess", method = RequestMethod.GET)
	public ModelAndView listProcess(ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale);
		try {

			List<ProcessDefinition> processDefinitions = processDefinitionService.getAllProcessesDefinitionsByUser(CommonUtil.getLoggedInUser());

			String script = designProcessDefinitionsGridScript(processDefinitions, "listProcess", locale);
			// Get the JSON data for organization tree structure
			model.addAttribute("jsonTree", ProcessDefinitionUtil.getJsonTreeForProcessList(request));
			model.addAttribute("script", script);
			model.addAttribute("gridTitle", appResourceBundle.getString("manage.listProcess"));
			model.addAttribute("gridType", "listprocess");

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			model.addAttribute("script", getText("process.log.error", locale));
		}
		return new ModelAndView("manageProcess", model);
	}
	
	
	@RequestMapping(value = "bpm/manageProcess/viewProcess", method = RequestMethod.GET)
	public ModelAndView viewProcess(ModelMap model, HttpServletRequest request) {
		
		List<Classification> clist = rtProcessService.getClassifications();
		List<ProcessDefinition> plist = processDefinitionService.getProcessesDefinitionsForView();
		
		model.addAttribute("clist", clist);
		model.addAttribute("plist", plist);
		return new ModelAndView("viewProcess", model);
	}

	/**
	 * Show process on user's the currently running process for the logged in
	 * user myInstances or journal
	 *
	 * @param model
	 * @return view containing list process
	 * @throws Exception
	 */

	@RequestMapping(value = "bpm/process/{type}", method = RequestMethod.GET)
	public ModelAndView instanceJournal(@PathVariable String type, ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale);
		String statusMsg = "";
		// String processInstanceId = null;
		Map<String, String> endEventScript = new HashMap<String, String>();
		try {
			if (model.containsKey("status")) {
				statusMsg = model.get("status").toString();
				model.addAttribute("newTab", "true");
			} else {
				model.addAttribute("newTab", "false");
			}
			if (model.containsKey("eventScriptDetails")) {
				endEventScript = (Map<String, String>) model.get("eventScriptDetails");
				// processInstanceId = (String)
				// endEventScript.get("processInstanceId");
				model.clear();
				// end scripts for task
				if (endEventScript.get("endScriptName") != null && !endEventScript.get("endScriptName").isEmpty()) {
					model = rtProcessService.setEndEventScript(model, endEventScript);
				}
				// Get end event start scripts
				if (endEventScript.get("endEventStartScriptName") != null && !endEventScript.get("endEventStartScript").isEmpty()) {
					model = rtProcessService.setEndEventStartScript(model, endEventScript);
				}
				// start scripts for task
				/*
				 * if(processInstanceId != null &&
				 * !processInstanceId.isEmpty()){ model =
				 * rtProcessService.setStartEventScriptForProcessInstance(
				 * processInstanceId, model); }
				 */
			}
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String loggedInUser = user.getUsername();
			List<HistoricProcessInstance> processInstances = new ArrayList<HistoricProcessInstance>();
			if (type.equalsIgnoreCase("myDocumentInstance")) {
				processInstances = rtProcessService.getMyDocumentProcessIntanceByUser(loggedInUser);
			} else {
				processInstances = rtProcessService.getProcessIntanceByUser(loggedInUser);
			}

			String script = designProcessInstancesGridScript(processInstances, type, locale);
			String gridTitle = "";
			if (type.equalsIgnoreCase("myDocumentInstance")) {
				// gridTitle = "My Document";
				gridTitle = "我的公文";
			} else if (type.equalsIgnoreCase("myInstances")) {
				gridTitle = appResourceBundle.getString("process.myInstances");
			} else if (type.equalsIgnoreCase("processDefinitions")) {
				gridTitle = appResourceBundle.getString("process.processDefinitions");
			} else if (type.equalsIgnoreCase("listProcess")) {
				gridTitle = appResourceBundle.getString("manage.listProcess");
			} else if (type.equalsIgnoreCase("monitorProcess")) {
				gridTitle = appResourceBundle.getString("manage.monitorProcesses");
			} else if (type.equalsIgnoreCase("myMonitorProcesses")) {
				gridTitle = appResourceBundle.getString("process.mymonitorProcesses");
			} else if (type.equalsIgnoreCase("journal")) {
				gridTitle = appResourceBundle.getString("process.journal");
			}

			// Get the JSON data for organization tree structure
			model.addAttribute("jsonTree", ProcessDefinitionUtil.getJsonTreeForProcessList(request));
			model.addAttribute("script", script);
			model.addAttribute("gridTitle", gridTitle);
			model.addAttribute("gridType", type);
			model.addAttribute("statusMsg", statusMsg);
		} catch (Exception e) {
			saveError(request, getText("process.instance.error", locale));
			log.error(e.getMessage(), e);
		}

		return new ModelAndView("manageProcess", model);
	}

	@RequestMapping(value = "bpm/defaultProcess/{formName}", method = RequestMethod.GET)
	public ModelAndView createDefaultForm(@PathVariable String formName, ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		try {
			Map<String, Object> context = new HashMap<String, Object>();
			context.put("isEdit", false);
			ProcessDefinition defaultProcessDefinition = rtProcessService.getDefaultProcessDefinition(formName);
			String defaultFormScript = "";
			model.addAttribute("isSystemDefined", true);
			defaultFormScript = rtProcessService.designDefaultForm(defaultProcessDefinition, context);
			model.addAttribute("hasForm", "true");
			model.addAttribute("script", defaultFormScript);
		} catch (BpmException e) {
			log.error(e.getMessage(), e);
			// e.printStackTrace();
			// saveError(request, "Failed to save default process");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			// e.printStackTrace();
			// saveError(request, "Failed to save default process");
		}
		return new ModelAndView("startProcess", model);
	}

	@RequestMapping(value = "bpm/default/submitDefaultProcessForm", method = RequestMethod.POST)
	public RedirectView submitNewsForm(HttpServletRequest request, ModelMap model, final RedirectAttributes redirectAttributes) {
		Locale locale = request.getLocale();
		Map<String, String> endEventScript = new HashMap<String, String>();
		Map<String, String> rtFormValues = new HashMap<String, String>();
		Map<String, Object> context = new HashMap<String, Object>();

		try {
			Map<String, String> filePathsMap = null;
			rtFormValues = FormDefinitionUtil.getFormParams(request.getParameterMap());
			Map<String, String[]> rtSubFormValues = FormDefinitionUtil.getSubFormParams(request.getParameterMap());
			String processDefinitionId = rtFormValues.get("processDefinitionId");
			rtFormValues.remove("processDefinitionId");
			String uploadDir = getServletContext().getRealPath("/resources") + "/" + request.getRemoteUser() + "/";
			Map<String, byte[]> files = FileUtil.getFileUploadMap(request);

			ProcessInstance processInstance = rtFormService.submitStartForm(processDefinitionId, rtFormValues, rtSubFormValues, files, filePathsMap, false);
			if (processInstance != null) {
				if (processInstance.getId() != null) {

					if (files != null) {
						filePathsMap = FileUtil.uploadFileForTask(request, uploadDir, null, processInstance.getId(), uploadDir, runtimeService, FileUtil.PROCESS_TYPE_START);
					}
					endEventScript.put("processInstanceId", processInstance.getId());
					redirectAttributes.addFlashAttribute("eventScriptDetails", endEventScript);
				}
				log.info("Default Process : " + rtFormValues.get("formId") + " created");
				saveMessage(request, rtFormValues.get("formId") + " saved successfully");
			} else {
				log.info("Could not instantiate news: " + rtFormValues.get("title"));
				saveError(request, "Failed to save default process");
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
			saveError(request, "Failed to save default process");
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			saveError(request, "Failed to save default process");
			model.addAttribute("message", "Failed to save default process " + e.getMessage());
			log.error(e.getMessage(), e);
		}
		return new RedirectView("/bpm/defaultProcess/" + rtFormValues.get("formId"));

	}

	@RequestMapping(value = "bpm/default/showDefaultFormDetail", method = RequestMethod.GET)
	public ModelAndView showDefaultFormDetail(@RequestParam("processInstanceId") String processInstanceId, ModelMap model, HttpServletRequest request) {

		Map<String, Object> context = new HashMap<String, Object>();
		context.put("isEdit", true);
		String script = null;
		try {
			Map<String, Object> properties = CommonUtil.getStringRepresentations(newsService.getNewsItemValues(processInstanceId));
			String createdUser = (String) properties.get("CREATEDBY");
			// show form in read-only
			Set<Role> userRoles = CommonUtil.getLoggedInUser().getRoles();
			if (!userRoles.isEmpty() && userRoles != null) {
				if (!userRoles.toString().contains("ROLE_ADMIN") && !createdUser.equalsIgnoreCase(CommonUtil.getLoggedInUserId())) {
					context.put("isFormReadOnly", true);
				}
			}
			ProcessDefinition defaultProcessDefinition = rtProcessService.getDefaultProcessDefinition((String) properties.get("formId"));
			MetaForm form = newsService.getNewsForm(processInstanceId);
			context.put("html", form.getHtmlSource());
			context.put("formId", form.getFormName());
			context.put("formAction", "bpm/news/updateNewsItem");
			context.put("properties", properties);
			context.put("pastValuesJson", CommonUtil.convertMapToJson(properties));
			context.put("instanceId", processInstanceId);
			context.put("processDefinition", defaultProcessDefinition);
			context.put("isEdit", true);
			script = rtProcessService.designEditDefaultProcessScript(defaultProcessDefinition, processInstanceId, context);
			model.addAttribute("script", script);
			return new ModelAndView("showDefaultProcessForm", model);
		} catch (BpmException e) {
			saveError(request, e.getMessage());
			log.error("Cannot get form details for instance " + processInstanceId + e.getMessage(), e);
			return new ModelAndView("showDefaultProcessForm", model);
		} catch (Exception e) {
			saveError(request, "Problem getting form details, check log for errors!");
			log.error("Cannot get form details for instance " + processInstanceId + e.getMessage(), e);
			return new ModelAndView("showDefaultProcessForm", model);
		}

	}

	@RequestMapping(value = "bpm/default/updateDefaultProcessForm", method = RequestMethod.POST)
	public RedirectView updateDefaultProcessForm(HttpServletRequest request, ModelMap model, final RedirectAttributes redirectAttributes) {
		Locale locale = request.getLocale();
		Map<String, String> rtFormValues = new HashMap<String, String>();
		try {
			rtFormValues = FormDefinitionUtil.getFormParams(request.getParameterMap());
			Map<String, String[]> rtSubFormValues = FormDefinitionUtil.getSubFormParams(request.getParameterMap());
			rtFormValues.remove("processDefinitionId");
			String uploadDir = getServletContext().getRealPath("/resources") + "/" + request.getRemoteUser() + "/";
			Map<String, byte[]> files = FileUtil.getFileUploadMap(request);
			Map<String, String> filePathsMap = null;
			String insId = rtFormValues.get("processInstanceId");
			rtProcessService.updateDefaultProcess(rtFormValues, rtSubFormValues, files, filePathsMap);
			if (files != null && !files.isEmpty()) {
				filePathsMap = FileUtil.uploadFileForTask(request, uploadDir, null, insId, uploadDir, runtimeService, FileUtil.PROCESS_TYPE_START);
			}
			operatingFunctionService.attachmentOperation(rtFormValues, null);
			saveMessage(request, "Form updated successfully");
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
			saveError(request, "Failed to update form!");
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			saveError(request, "Failed to update form!");
			model.addAttribute("message", "Failed to update form" + e.getMessage());
			log.error(e.getMessage(), e);
		}
		return new RedirectView("/bpm/defaultProcess/" + rtFormValues.get("formId"));
	}

	@RequestMapping(value = "/mail/sendMail", method = RequestMethod.GET)
	public void sendMail(@RequestParam("message") String message, @RequestParam("notificationType") String notificationType, @RequestParam("subject") String subject, ModelMap model, HttpServletRequest request) {
		try {
			operatingFunctionService.sendNotification(userManager.getUsers().toString(), message, notificationType, subject);

		} catch (Exception e) {
			saveError(request, "Failed to send mail");
			log.error(e.getMessage(), e);
		}
	}

	@RequestMapping(value = "bpm/default/showDefaultTemplateView", method = RequestMethod.GET)
	public ModelAndView showDefaultTemplateView(@RequestParam("newsInstanceId") String newsInstanceId, ModelMap model, HttpServletRequest request) {
		Map<String, Object> context = new HashMap<String, Object>();
		StringWriter htmlContent = new StringWriter();
		try {
			MetaForm form = newsService.getNewsForm(newsInstanceId);
			Map<String, Object> properties = newsService.getNewsItemValues(newsInstanceId);
			// context.put("html", form.getHtmlSource());
			// context.put("formId", form.getFormName());
			// context.put("properties", properties);
			VelocityContext contextDetails = new VelocityContext(properties);
			if (form != null && form.getPrintTemplate() != null && !form.getPrintTemplate().isEmpty()) {
				velocityEngine.evaluate(contextDetails, htmlContent, "Print Template", String.valueOf(form.getPrintTemplate()));
			}
			context.put("html", form.getHtmlSource());
			model.addAttribute("script", htmlContent);

			return new ModelAndView("news/showDetailNews", model);
		} catch (BpmException e) {
			saveError(request, e.getMessage());
			log.error("Cannot get news details for instance " + newsInstanceId + e.getMessage(), e);
			return new ModelAndView("redirect:newsList");
		} catch (Exception e) {
			saveError(request, "Problem getting news details, check log for errors!");
			log.error("Cannot get news details for instance " + newsInstanceId + e.getMessage(), e);
			return new ModelAndView("redirect:newsList");
		}

	}

	/**
	 * Getting all instance count for different criteria
	 * 
	 * @param statisticsType
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/processInstance/{statisticsType}", method = RequestMethod.GET)
	public ModelAndView getAllProcessInstancesCount(@PathVariable String statisticsType, ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		try {
			List<Map<String, Object>> processInstanceDetails = null;
			String script = null;
			// processInstanceDetails =
			// rtProcessService.getAllProcessIntanceCountForFlowStatistics(null);
			script = ProcessDefinitionUtil.generateScriptForFlowStatistics(velocityEngine, processInstanceDetails, statisticsType, locale);
			model.addAttribute("script", script);
			model.addAttribute("gridType", statisticsType);
		} catch (Exception e) {
			saveError(request, getText("process.instance.error", locale));
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("showProcessInstanceCount", model);
	}

	/**
	 * Show advanced search page
	 * 
	 * @param listViewId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/bpm/processInstance/showAdvanceSearch", method = RequestMethod.GET)
	public ModelAndView showAdvanceSearch(ModelMap model, HttpServletRequest request) {
		return new ModelAndView("showGridSearchPage", model);
	}

	/**
	 * Getting all instance count for different criteria
	 * 
	 * @param statisticsType
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/processInstance/reloadFlowStatisticsGridWithSearchParamsAndConstraints", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> reloadFlowStatisticsGridWithSearchParamsAndConstraints(@RequestParam("searchParams") String searchParams, ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		try {
			Map<String, Object> searchParamMap = CommonUtil.convertJsonToList(searchParams).get(0);
			List<Map<String, Object>> processInstanceDetails = rtProcessService.getAllProcessIntanceCountForFlowStatistics(searchParamMap);
			if (searchParamMap.get("departmentId") != null && !(String.valueOf(searchParamMap.get("departmentId")).isEmpty()) && String.valueOf(searchParamMap.get("isAdvancedSearch")).equals("true")) {
				String script = ProcessDefinitionUtil.generateScriptForFlowStatistics(velocityEngine, processInstanceDetails, "flowStatistics", locale);
				Map<String, Object> responseScript = new HashMap<String, Object>();
				List<Map<String, Object>> processInstanceScript = new ArrayList<Map<String, Object>>();
				responseScript.put("script", script);
				responseScript.put("isDepartment", true);
				processInstanceScript.add(responseScript);
				return processInstanceScript;
			} else {
				return processInstanceDetails;
			}
		} catch (Exception e) {
			saveError(request, getText("process.instance.error", locale));
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * <p>
	 * Shows the detailed instances, that is all the task iinstances running
	 * under a process instance
	 * </p>
	 * 
	 * @param id
	 *            process instance id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/manageProcess/showInstanceDetail", method = RequestMethod.GET)
	public ModelAndView showInstanceDetail(@RequestParam("instanceId") String id, ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		try {
			List<HistoricProcessInstance> processInstance = rtProcessService.getProcessInstance(id);
			if (processInstance == null) {
				saveError(request, getText("process.intance.notFound", locale));
				return new ModelAndView(new RedirectView("/bpm/process/myInstances"));
			}
			// List<FormProperty> formProperties =
			// rtProcessService.getTaskFormValuesByTaskId(id);
			// model.addAttribute("formProperties", formProperties);
			ProcessDefinition processDefinition = processDefinitionService.getProcessDefinitionById(processInstance.get(0).getProcessDefinitionId());
			String instancesScript = designMyInstancesDetailGridScript(processInstance.get(0).getId(), locale);
			// Map<String, Object>variables =
			// CommonUtil.getStringRepresentations(rtProcessService.getVariablesForInstance(id));
			model.addAttribute("processInstance", processInstance);
			model.addAttribute("processDefinition", processDefinition);
			model.addAttribute("instancesScript", instancesScript);
			model.addAttribute("gridType", "myInstances");
		} catch (BpmException e) {
			saveError(request, getText("process.intance.load.error", locale));
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			saveError(request, getText("process.instance.load.unKnownError", locale));
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("showMyInstancesDetail", model);
	}

	/**
	 * Show the formProperties of Form
	 * 
	 * @param id
	 * @param processInstanceId
	 * @param model
	 * @param request
	 * @return
	 */
	/*
	 * @RequestMapping(value = "bpm/manageProcess/showTaskDetail", method =
	 * RequestMethod.GET) public ModelAndView
	 * showTaskDetails(@RequestParam("taskId") String
	 * id,@RequestParam("processInstanceId") String processInstanceId, ModelMap
	 * model,HttpServletRequest request){ List<FormProperty> formProperties =
	 * rtProcessService.getTaskFormValues(id,processInstanceId);
	 * model.addAttribute("formProperties", formProperties);
	 * model.addAttribute("instancesScript", ""); return new
	 * ModelAndView("showMyInstancesDetail", model); }
	 */
	/**
	 * Show process that currently running process for the Admin
	 *
	 * @param model
	 * @return view containing list process
	 * @throws Exception
	 */

	@RequestMapping(value = "bpm/manageProcess/monitorProcesses", method = RequestMethod.GET)
	public ModelAndView monitorProcess(ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale);
		try {

			List<ProcessDefinition> processDefinitions = processDefinitionService.getProcessesDefinitions();

			String script = designProcessDefinitionsGridScript(processDefinitions, "monitorProcess", locale);
			// Get the JSON data for organization tree structure
			model.addAttribute("jsonTree", ProcessDefinitionUtil.getJsonTreeForProcessList(request));
			model.addAttribute("script", script);
			model.addAttribute("gridTitle", appResourceBundle.getString("manage.monitorProcesses"));
			model.addAttribute("gridType", "monitorProcesses");
		} catch (BpmException be) {
			log.error(be.getMessage(), be);
			model.addAttribute("script", getText("process.load.error", locale));
			model.addAttribute("gridType", "monitorProcesses");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			model.addAttribute("script", getText("process.load.error", locale));
			model.addAttribute("gridType", "monitorProcesses");
		}
		return new ModelAndView("manageProcess", model);
	}

	/**
	 * Show process that currently running process for the Admin
	 *
	 * @param model
	 * @return view containing list process
	 * @throws Exception
	 */

	@RequestMapping(value = "bpm/manageProcess/myMonitorProcesses", method = RequestMethod.GET)
	public ModelAndView myMonitorProcesses(ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale);
		try {
			List<ProcessDefinition> processDefinitions = processDefinitionService.getProcessesDefinitions();

			String script = designProcessDefinitionsGridScript(processDefinitions, "myMonitorProcesses", locale);
			// Get the JSON data for organization tree structure
			model.addAttribute("jsonTree", ProcessDefinitionUtil.getJsonTreeForProcessList(request));
			model.addAttribute("script", script);
			model.addAttribute("gridTitle", appResourceBundle.getString("process.mymonitorProcesses"));
			model.addAttribute("gridType", "myMonitorProcesses");
		} catch (BpmException be) {
			log.error(be.getMessage(), be);
			model.addAttribute("script", getText("process.load.error", locale));
			model.addAttribute("gridType", "myMonitorProcesses");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			model.addAttribute("script", getText("process.load.error", locale));
			model.addAttribute("gridType", "myMonitorProcesses");
		}
		return new ModelAndView("manageProcess", model);
	}

	/**
	 * Deletes the process definition corresponding to that version alone
	 * 
	 * @param id
	 *            representing one version of the definition
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/manageProcess/deleteProcessVersionAjax", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> deleteProcessVersionAjax(@RequestParam("processId") String processId, @RequestParam("processKey") String processKey, HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		List<String> processIdList = new ArrayList<String>();
		Map<String, String> result = new HashMap<String, String>();
		try {
			if (processId.contains(",")) {
				String[] ids = processId.split(",");
				for (String id : ids) {
					processIdList.add(id);
				}
				rtProcessService.deleteAllSelectedProcessDefinition(processIdList, true);
			} else {
				rtProcessService.deleteProcessDefinition(processId, true);
			}
			// saveMessage(request,
			// getText("process.delete.version.success",locale));
			result.put("successMsg", getText("process.delete.version.success", locale));
			result.put("processKey", processKey);
		} catch (BpmException e) {
			log.error(e.getMessage(), e);
			result.put("errorMsg", getText("process.delete.version.error", locale));
			// saveError(request,
			// getText("process.delete.version.error",e.getMessage(),locale));
		}
		return result;
	}

	/**
	 * Deletes the process definitions, all the versions of that process deleted
	 * 
	 * @param key,
	 *            a common one for all versions of that process definition
	 * @return
	 */
	@RequestMapping(value = "bpm/manageProcess/deleteAllProcessVersions", method = RequestMethod.GET)
	public String deleteAllProcessDefinitionVersions(@RequestParam("processKey") String key, HttpServletRequest request) {
		Locale locale = request.getLocale();
		try {
			rtProcessService.deleteAllProcessDefinitionVersions(key, true);
			saveMessage(request, getText("process.allVersion", locale));

		} catch (BpmException e) {
			log.error(e.getMessage(), e);
			saveError(request, getText("process.allVersion.error", e.getMessage(), locale));
		}
		return "redirect:listProcess";
	}

	/**
	 * Deletes the selected process definitions, all the versions of that
	 * process deleted
	 * 
	 * @param processIds
	 * @return
	 */
	@RequestMapping(value = "bpm/manageProcess/bulkDeleteAllProcess", method = RequestMethod.GET)
	public String deleteAllProcessDefinition(@RequestParam("processKeys") String processKeys, HttpServletRequest request) {
		Locale locale = request.getLocale();
		List<String> processDefinitionKeys = new ArrayList<String>();
		// Get List of Ids for bulk suspend or bulk activate process
		if (processKeys.contains(",")) {
			String[] keys = processKeys.split(",");
			for (String key : keys) {
				processDefinitionKeys.add(key);
			}
		} else {
			processDefinitionKeys.add(processKeys);
		}
		try {
			rtProcessService.deleteAllProcessDefinition(processDefinitionKeys, true);
			saveMessage(request, getText("selected.process", locale));

		} catch (BpmException e) {
			log.error(e.getMessage(), e);
			saveError(request, getText("selected.process.error", e.getMessage(), locale));
		}
		return "redirect:listProcess";
	}

	/**
	 * Deletes the selected process definitions, all the versions of that
	 * process deleted
	 * 
	 * @param processIds
	 * @return
	 */
	@RequestMapping(value = "bpm/manageProcess/deleteSelectedProcessDefinition", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> deleteSelectedProcessDefinition(@RequestParam("processKeys") String processKeys, HttpServletRequest request) {
		Locale locale = request.getLocale();
		Map<String, String> message = new HashMap<String, String>();
		List<String> processDefinitionKeys = new ArrayList<String>();
		// Get List of Ids for bulk suspend or bulk activate process
		if (processKeys.contains(",")) {
			String[] keys = processKeys.split(",");
			for (String key : keys) {
				processDefinitionKeys.add(key);
			}
		} else {
			processDefinitionKeys.add(processKeys);
		}
		try {
			rtProcessService.deleteAllProcessDefinition(processDefinitionKeys, true);
			message.put("successMsg", getText("selected.process", locale));
			message.put("success", "true");

		} catch (BpmException e) {
			log.error(e.getMessage(), e);
			message.put("errorMsg", getText("selected.process.error", locale));
			message.put("success", "false");
		}
		return message;
	}

	/**
	 * Terminate the selected process instance,{will delete all related task}
	 * 
	 * @param executionId
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "bpm/process/terminateProcessInstance", method = RequestMethod.GET)
	public String terminateProcessInstance(@RequestParam("executionId") String executionId, HttpServletRequest request) throws ParseException {
		Locale locale = request.getLocale();
		try {
			Date date = new Date();
			rtProcessService.terminateExecutionByInstanceId(executionId, date);
			saveMessage(request, getText("terminated.process.instance.success", locale));
		} catch (BpmException e) {
			saveError(request, getText("terminated.process.error", e.getMessage(), locale));
		}
		return "redirect:myInstances";

	}

	/**
	 * Terminate the selected process instance,{will delete all related task}
	 * 
	 * @param executionId
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "bpm/process/terminateProcessInstanceAjax", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> terminateProcessInstanceAjax(@RequestParam("executionId") String executionId, @RequestParam("taskId") String taskId, @RequestParam("resourceName") String resourceName, @RequestParam("operationType") String operationType, HttpServletRequest request) throws ParseException {
		Locale locale = request.getLocale();
		Map<String, String> result = new HashMap<String, String>();
		try {
			Date date = new Date();
			rtProcessService.terminateExecutionByInstanceId(executionId, date);
			OperatingFunctionAudit operFunAudit = null;
			operFunAudit = new OperatingFunctionAudit(CommonUtil.getLoggedInUserId(), taskId, resourceName, OperatingFunction.TERMINATE, executionId, null);
			processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
			result.put("successMsg", getText("terminated.process.instance.success", locale));
			result.put("executionId", executionId);
		} catch (BpmException e) {
			result.put("successMsg", getText("terminated.process.error", e.getMessage(), locale));
		}
		return result;
	}

	/**
	 * <p>
	 * Decides and designs the initial screen template when process is started,
	 * if a start form is associated that form needs to be rendered, else
	 * process instantiated directly and corresponding screen shown
	 * </p>
	 * 
	 * @param processId
	 *            for the process starting
	 * @return the template script as string, to be rendered
	 */
	private String designStartProcessScript(ProcessDefinition processDefinition, boolean isSystemDefined, String processInstanceId, boolean isFromDoneList, String creator, boolean isFormReadOnly) throws BpmException {
		Map<String, Object> vmContextMap = rtProcessService.designStartProcessScript(processDefinition, velocityEngine, "bpm/form/submitStartForm", isSystemDefined, processInstanceId, isFromDoneList, null);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!creator.equals(Constants.EMPTY_STRING)) {
			vmContextMap.put("isCreator", creator.equals(user.getUsername()));
		}
		vmContextMap.put("isFormReadOnly", isFormReadOnly);
		return TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showStartHtmlForm.vm", vmContextMap);
	}

	/**
	 * Gets the grid script for the process instances that can be rendered on
	 * screen
	 * 
	 * @param processInstances
	 *            the list of process instances that has to be viewed as grid
	 * @return the script for getting grid on screen
	 */
	private String designProcessInstancesGridScript(List<HistoricProcessInstance> processInstances, String type, Locale locale) {
		String processInstanceScript = null;
		try {
			List<Map<String, Object>> processInstancesMapAsLIst = rtProcessService.resolveHistoricProcessInstance(processInstances, null);
			processInstanceScript = ProcessDefinitionUtil.generateScriptForProcess(velocityEngine, processInstancesMapAsLIst, type, locale);
		} catch (BpmException e) {
			processInstanceScript = e.getMessage();
			log.error(e.getMessage(), e);
		}
		return processInstanceScript;
	}

	/**
	 * Gets the grid script for the task instances under a process instance that
	 * can be rendered on screen
	 * 
	 * @param taskInstances
	 *            the list of task instances that has to be viewed as grid
	 * @return the script for getting grid on screen
	 */
	private String designMyInstancesDetailGridScript(String processInstanceId, Locale locale) {
		String myInstancesScript = null;
		try {
			List<Map<String, Object>> myInstanceDetails = rtTaskService.resolveHistoricTaskInstance(processInstanceId);
			myInstancesScript = TaskUtil.generateScriptForTaskInstancesGrid(velocityEngine, myInstanceDetails, locale);
		} catch (BpmException e) {
			myInstancesScript = e.getMessage();
			log.error(e.getMessage(), e);
		}
		return myInstancesScript;
	}

	@RequestMapping(value = "startProcess", method = RequestMethod.GET)
	public ModelAndView createProcess(Model model) throws EazyBpmException {
		return new ModelAndView("startsProcess");
	}

	/**
	 * Show the Restore process definition page
	 * 
	 * @param processId
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "bpm/manageProcess/restoreProcessVersionAjax", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> restoreProcessVersionAjax(HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		Map<String, String> result = new HashMap<String, String>();
		String processKey = null;
		String processDefinitionId = null;
		String inactivateProcessDefinitionId = null;
		try {
			Map<String, String> restoreFormValues = CommonUtil.getKeyValuePairs(request.getParameterMap());
			inactivateProcessDefinitionId = restoreFormValues.get("activeProcessId");
			processDefinitionService.inActivateProcessDefinitionVersionById(inactivateProcessDefinitionId);
			processDefinitionId = restoreFormValues.get("processId");
			processDefinitionService.activateProcessDefinitionVersionById(processDefinitionId);
			processKey = restoreFormValues.get("processKey");
			result.put("successMsg", getText("process.restore.success", locale));
			result.put("processKey", processKey);
		} catch (Exception e) {
			result.put("errorMsg", getText("process.restore.error", locale));
			log.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * Suspend or Activate the process for given processId
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/manageProcess/suspensionProcess", method = RequestMethod.GET)
	public String suspensionProcess(HttpServletRequest request, @RequestParam("processId") String processDefinitionId, @RequestParam("suspensionState") String suspensionState, ModelMap model) {
		Locale locale = request.getLocale();
		try {
			List<String> processDefinitionIds = new ArrayList<String>();
			// Get List of Ids for bulk suspend or bulk activate process
			if (processDefinitionId.contains(",")) {
				String[] ids = processDefinitionId.split(",");
				for (String id : ids) {
					processDefinitionIds.add(id);
				}
			} else {
				processDefinitionIds = null;
			}
			/*
			 * boolean suspendProcessInstances = false;
			 * if(suspendFormValues.containsKey("isAllProcess")){
			 * suspendProcessInstances = true; } Date suspensionDate;
			 * if(suspendFormValues.get("onDatePic")!=null &&
			 * !suspendFormValues.get("onDatePic").isEmpty()){ suspensionDate =
			 * DateUtil.convertStringToDate("mm/dd/yyyy",suspendFormValues.get(
			 * "onDatePic")); }else{ suspensionDate =null; }
			 */

			if (Integer.valueOf(suspensionState) == 1) {
				// check bulk suspend or single suspend and proceed according to
				// that
				if (processDefinitionIds == null) {
					rtProcessService.suspendProcessDefinitionById(processDefinitionId, false, null);
				} else {
					rtProcessService.suspendProcessDefinitionByIds(false, null, processDefinitionIds);
				}
				saveMessage(request, getText("process.suspend", locale));
			} else {
				// check bulk activate or single activate and proceed according
				// to that
				if (processDefinitionIds == null) {
					rtProcessService.activateProcessDefinitionById(processDefinitionId, false, null);
				} else {
					rtProcessService.activateProcessDefinitionByIds(false, null, processDefinitionIds);
				}
				saveMessage(request, getText("process.activate", locale));
			}

		} catch (Exception e) {
			saveError(request, getText("process.suspension.error", e.getMessage(), locale));
			log.error(e.getMessage(), e);
		}
		return "redirect:listProcess";
	}

	/**
	 * Gets the grid script for the process Definitions that can be rendered on
	 * screen
	 * 
	 * @param processDefinitions
	 *            the list of process Definitions that has to be viewed as grid
	 * @return the script for getting grid on screen
	 */
	private String designProcessDefinitionsGridScript(List<ProcessDefinition> processDefinitions, String type, Locale locale) {
		String processDefinitionsScript = null;
		try {
			List<Map<String, Object>> processDefinitionsMapAsList = new ArrayList<Map<String, Object>>();
			if (type.contentEquals("myMonitorProcesses")) {
				User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				String loggedInUser = user.getUsername();
				processDefinitionsMapAsList = processDefinitionService.resolveMyMonitorProcesses(processDefinitions, loggedInUser);

			} else {
				processDefinitionsMapAsList = processDefinitionService.resolveProcessDefinitions(processDefinitions);

			}
			processDefinitionsScript = ProcessDefinitionUtil.generateScriptForProcess(velocityEngine, processDefinitionsMapAsList, type, locale);
		} catch (BpmException e) {
			processDefinitionsScript = e.getMessage();
			log.error(e.getMessage(), e);
		}
		return processDefinitionsScript;
	}

	/**
	 * Show the delete page for given process instance id
	 * 
	 * @param processInsId
	 * @param name
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/manageProcess/showDeleteProcessInstance", method = RequestMethod.GET)
	public ModelAndView showDeleteProcessInstance(@RequestParam("processInsId") String processInsId, @RequestParam("name") String name, ModelMap model) {
		model.addAttribute("processInsId", processInsId);
		model.addAttribute("name", name);
		return new ModelAndView("showDeleteProcessInstance", model);
	}

	
	@RequestMapping(value = "bpm/manageProcess/classifications", method = RequestMethod.GET)
	public ModelAndView showClassifications(ModelMap model) {
		List<Classification> list = rtProcessService.getClassifications();
		String[] fieldNames = { "classificationId", "name", "description", "orderNo"};
		String script = generateScriptForClassificationGrid(CommonUtil.getMapListFromObjectListByFieldNames(list, fieldNames, ""), velocityEngine);
		model.addAttribute("script", script);
		return new ModelAndView("process/classificationList", model);
	}
	
	public static String generateScriptForClassificationGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','分类名称', '描述', '排序', '上移', '下移']";
		context.put("gridId", "CLASSIFICATION_LIST");
		context.put("needCheckbox", true);
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "classificationId", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "center", "_showEditRecord", "false");
		CommonUtil.createFieldNameList(fieldNameList, "description", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "orderNo", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "orderUpImg", "100", "center", "_moveClassificationGridRowUpImage", "false");
		CommonUtil.createFieldNameList(fieldNameList, "orderDownImg", "100", "center", "_moveClassificationGridDownImage", "false");

		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	@RequestMapping(value = "bpm/manageProcess/classificationForm", method = RequestMethod.GET)
	public ModelAndView showClassificationForm(ModelMap model) {
		Classification classification = new Classification();
		model.addAttribute("classification", classification);
		return new ModelAndView("process/classificationForm", model);
	}
	
	@RequestMapping(value = "bpm/manageProcess/saveClassification", method = RequestMethod.POST)
	public ModelAndView saveGroup(HttpServletRequest request, @ModelAttribute("classification") Classification classification, ModelMap model, BindingResult errors) {

		validator.validate(classification, errors);

		if (errors.hasErrors()) {
			model.addAttribute("classification", classification);
			return new ModelAndView("process/classificationForm", model);
		}
		
		String classificationName = classification.getName().trim().replaceAll("\\s+", " ");
		
//		if(rtProcessService.isClassificationExist(classificationName)) {
//        	model.addAttribute("classification", classification);
//        	saveError(request, "分类名称已存在");
//			return new ModelAndView("process/classificationForm", model);
//      }
		classification.setName(classificationName);
		rtProcessService.saveClassification(classification, request);
		return new ModelAndView("redirect:classifications");
	}
	
	@RequestMapping(value = "bpm/manageProcess/editClassification", method = RequestMethod.GET)
	public ModelAndView editGroup(@RequestParam("id") String id, ModelMap model) {

		Classification classification = rtProcessService.getClassificationById(id);
		model.put("classification", classification);
		return new ModelAndView("process/classificationForm", model);
	}
	
	@RequestMapping(value = "bpm/manageProcess/deleteSelectedClassification", method = RequestMethod.GET)
	public ModelAndView deleteSelectedRecords(@RequestParam("cids") String cids, HttpServletRequest request) {

		List<String> cidList = new ArrayList<String>();
		if (cids.contains(",")) {
			String[] ids = cids.split(",");
			for (String id : ids) {
				cidList.add(id);
			}
		} else {
			cidList.add(cids);
		}

		rtProcessService.removeClassifications(cidList, request);
		return new ModelAndView("redirect:classifications");
	}
	
	
	@RequestMapping(value = "bpm/manageProcess/showAddClassification", method = RequestMethod.GET)
	public ModelAndView showAddClassification(ModelMap model) {
		model.addAttribute("isNeedImport", "true");
		return new ModelAndView("process/showAddClassification", model);
	}

	@RequestMapping(value = "bpm/manageProcess/showAddClassificationForModule", method = RequestMethod.GET)
	public ModelAndView showAddClassificationForModule(ModelMap model) {
		model.addAttribute("isNeedImport", "false");
		return new ModelAndView("process/showAddClassification", model);
	}

	@RequestMapping(value = "bpm/process/saveClassification", method = RequestMethod.POST)
	public @ResponseBody String saveClassification(HttpServletRequest request, @ModelAttribute("classification") Classification classification) {
		Locale locale = request.getLocale();
		try {
			rtProcessService.saveClassification(classification, request);

		} catch (BpmException e) {
			log.error("Error while saving new classification  : " + e.getMessage(), e);
			saveError(request, getText("classificaion.save.error", e.getMessage(), locale));
		}
		return "";
	}
	
	@RequestMapping(value = "bpm/manageProcess/updateClassificationOrderNos", method = RequestMethod.POST)
    public @ResponseBody String updateClassificationOrderNos(@RequestParam("classificationJson") String classificationJson, HttpServletRequest request) {
    	JSONObject respObj = new JSONObject();
    	JSONArray responseMsg = new JSONArray();
    	try {
			List<Map<String,Object>> classificationList = CommonUtil.convertJsonToList(classificationJson);
			boolean hasUpdated = rtProcessService.updateClassificationOrderNos(classificationList, request);
			if(hasUpdated){
				respObj.put("isSuccess", "true");
				responseMsg.put(respObj);
				return responseMsg.toString();
			}
	        log.info("ClassificationOrder Updated Successfully ");
		} catch (Exception e) {
			log.error("Error while updating order nos for Group",e);
		}
    	return Constants.EMPTY_STRING;
    			
    }
	
	/**
	 * Delete the process instance for given instance id
	 * 
	 * @param processInsId
	 * @param reason
	 * @param model
	 * @return
	 */
	
	@RequestMapping(value = "bpm/process/deleteProcessInstance", method = RequestMethod.GET)
	public String deleteProcessInstance(HttpServletRequest request, @RequestParam("processInsId") String processInsId, @RequestParam("reason") String reason, ModelMap model) {
		Locale locale = request.getLocale();
		try {
			rtProcessService.deleteProcessInstance(processInsId, null, reason);
			saveMessage(request, getText("instance.delete.success", locale));
		} catch (BpmException e) {
			saveError(request, getText("instance.delete.error", e.getMessage(), locale));
			log.error("Error while delete Process Instances  : " + e.getMessage(), e);
		}
		return "redirect:myInstances";
	}
	 

	/**
	 * Delete the process instance for given instance id
	 * 
	 * @param processInsId
	 * @param reason
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/process/deleteProcessInstanceAjax", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> deleteProcessInstanceAjax(HttpServletRequest request, @RequestParam("processInsId") String processInsId, ModelMap model) {
		Locale locale = request.getLocale();
		Map<String, String> result = new HashMap<String, String>();
		List<String> processInstanceIds = null;
		String processInstanceId = null;
		try {
			if (processInsId.contains(",")) {
				processInstanceIds = new ArrayList<String>();
				String[] ids = processInsId.split(",");
				for (String id : ids) {
					processInstanceIds.add(id);
				}
			} else {
				processInstanceId = processInsId;
			}
			rtProcessService.deleteProcessInstance(processInstanceId, processInstanceIds, null);
			// saveMessage(request, getText("instance.delete.success",locale));
			result.put("successMsg", getText("instance.delete.success", locale));
		} catch (BpmException e) {
			result.put("errorMsg", getText("instance.delete.error", locale));
			// saveError(request,
			// getText("instance.delete.error",e.getMessage(),locale));
			log.error("Error while delete Process Instances  : " + e.getMessage(), e);
		}
		return result;
	}

	/**
	 * Show status information for all process instance on given definition id
	 * 
	 * @param processDefId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/showProcessInstances", method = RequestMethod.GET)
	public ModelAndView showProcessInstances(@RequestParam("processDefinitionId") String processDefId, @RequestParam("processGridType") String processGridType, ModelMap model) {
		try {
			log.info(processDefId + " : - : " + processGridType);

			Map<String, Object> processInstance = new HashMap<String, Object>();
			if (processGridType.contentEquals("monitorProcesses")) {
				processInstance = rtProcessService.getAllProcessInstance(processDefId);
			} else {
				User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				String loggedInUser = user.getUsername();
				processInstance = rtProcessService.getAllMyProcessInstance(processDefId, loggedInUser);
			}
			model.addAttribute("Success", (Integer) processInstance.get("Success"));
			model.addAttribute("Deleted", (Integer) processInstance.get("Deleted"));
			model.addAttribute("InProgress", (Integer) processInstance.get("InProgress"));
			model.addAttribute("processDefinitionId", processInstance.get("processDefinitionId"));
			model.addAttribute("Terminated", (Integer) processInstance.get("Terminated"));
			model.addAttribute("Suspended", (Integer) processInstance.get("suspendedCount"));
			model.addAttribute("processGridType", processGridType);

		} catch (BpmException e) {
			model.addAttribute("message", "Failed in show Process Instances " + e.getMessage());
			log.error("Error while show Process Instances  : " + e.getMessage(), e);

		}
		return new ModelAndView("process/showProcessInstances", model);
	}

	/**
	 * show the process instance grid for given status
	 * 
	 * @param request
	 * @param processDefId
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/manageProcess/showProcessInstanceForStatus", method = RequestMethod.GET)
	public ModelAndView showProcessInstanceForStatus(@RequestParam("processDefinitionId") String processDefId, @RequestParam("status") String status, @RequestParam("processGridType") String processGridType, ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		try {
			log.info(processDefId + " : " + status + " : " + processGridType);
			List<HistoricProcessInstance> processInstances = new ArrayList<HistoricProcessInstance>();
			if (processGridType.contentEquals("monitorProcesses")) {
				processInstances = rtProcessService.getAllProcessInstnaceByStauts(processDefId, status);
				model.addAttribute("gridTitle", "监控的流程实例");
			} else {
				model.addAttribute("gridTitle", "我监控的流程实例");
				User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				String loggedInUser = user.getUsername();
				processInstances = rtProcessService.getAllMyProcessInstnaceByStauts(processDefId, status, loggedInUser);
			}

			String script = processInstancesGridScript(processInstances, status, locale, processGridType);
			model.addAttribute("script", script);
			model.addAttribute("gridType", "myInstances");
		} catch (BpmException e) {
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("process/processInstanceGrid", model);
	}

	/**
	 * Download the proper xml of process definition
	 * 
	 * @param resourceName
	 * @param deploymentId
	 * @throws IOException
	 */
	@RequestMapping(value = "bpm/manageProcess/processXmlDownload", method = RequestMethod.GET)
	public void processXmlDownload(@RequestParam("resourceName") String resourceName, @RequestParam("deploymentId") String deploymentId, HttpServletResponse response, HttpServletRequest request) throws IOException {
		InputStream xmlStream = null;
		Locale locale = request.getLocale();
		try {
			Date date = new Date();
			// Get current date string to append with file
			String currentDate = DateUtil.convertDateToDefalutDateTimeString(date);
			String[] fileName = resourceName.split("\\.");

			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/xml;charset=UTF-8");

			/*
			 * fileName[0] = URLEncoder.encode(fileName[0], "UTF-8");
			 * fileName[0] = URLDecoder.decode(fileName[0], "ISO8859_1");
			 */
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName[0].replaceAll(" ", "_") + "_" + currentDate + ".bpmn20.xml");
			xmlStream = rtProcessService.getResourceAsStream(deploymentId, resourceName);
			FileCopyUtils.copy(xmlStream, response.getOutputStream());
		} catch (Exception e) {
			saveError(request, getText("process.xmlDownload.error", e.getMessage(), locale));
		} finally {
			if (xmlStream != null) {
				xmlStream.close();
			}
		}
	}

	/**
	 * Get Grid column names and grid data, then export the grid data to CSV
	 * file
	 * 
	 * @param response
	 * @param request
	 * @param gridHeader
	 * @param gridDatas
	 * @throws IOException
	 * @throws JSONException
	 */
	@RequestMapping(value = "bpm/manageProcess/gridCSVExport", method = RequestMethod.GET)
	public void gridCSVExport(HttpServletResponse response, HttpServletRequest request, @RequestParam("gridName") String gridName, @RequestParam("gridHeader") String gridHeader, @RequestParam("gridType") String gridType, @RequestParam("instanceId") String instanceId) throws IOException {
		Locale locale = request.getLocale();
		String gridHead = gridHeader;
		// String gridData =gridDatas;
		// convert the json data into list of map data
		try {
			List<Map<String, Object>> gridDataMap = null;
			List<Map<String, Object>> selectedDataMap = new ArrayList<Map<String, Object>>();
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String loggedInUser = user.getUsername();
			List<HistoricProcessInstance> processInstances = rtProcessService.getProcessIntanceByUser(loggedInUser);
			gridDataMap = rtProcessService.resolveHistoricProcessInstance(processInstances, null);
			if (instanceId.contains(",")) {
				String[] instanceIds = instanceId.split(",");
				for (int index = 0; index < instanceIds.length; index++) {
					for (Map<String, Object> rowMap : gridDataMap) {
						if (rowMap.get("id").equals(instanceIds[index])) {
							selectedDataMap.add(rowMap);
						}
					}
				}
			} else {
				for (Map<String, Object> rowMap : gridDataMap) {
					if (rowMap.get("id").equals(instanceId)) {
						selectedDataMap.add(rowMap);
					}
				}
			}
			if (!instanceId.equals("") && instanceId != "" && instanceId.length() != 0) {
				gridDataMap = selectedDataMap;
			}

			// List<Map<String,Object>> gridDataMap =
			// CommonUtil.convertJsonToList(gridData);
			List<String> columnName = new ArrayList<String>();
			if (gridHead.contains(",")) {
				String[] keys = gridHead.split(",");
				for (String key : keys) {
					columnName.add(key);
				}
			} else {
				columnName.add(gridHead);
			}
			String colmnName = "";
			List<String> rows = new ArrayList<String>();
			int count = 0;
			// Create the header name and grid data for CSV Export
			for (Map<String, Object> rowMap : gridDataMap) {
				String rowData = "";
				for (String colName : columnName) {
					if (rowMap.containsKey(colName)) {
						// Getting Header names
						if (count == 0) {
							colmnName = colmnName + colName + ",";
						}
						// Getting row values
						if (rowMap.get(colName) != null) {
							rowData = rowData + rowMap.get(colName) + ",";
						} else {
							rowData = rowData + Constants.EMPTY_STRING + ",";
						}
					}
				}
				count++;
				rows.add(rowData);
			}

			Date date = new Date();
			// Get current date string and append with file name
			String currentDate = DateUtil.convertDateToDefalutDateTimeString(date);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + gridName + "_" + currentDate + ".xls");
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet newSheet = wb.createSheet(gridName + "_" + currentDate);

			HSSFRow header = newSheet.createRow(0);
			int cellCount = 0;
			for (String cellValue : colmnName.split(",")) {
				header.createCell(cellCount).setCellValue(cellValue);
				cellCount++;
			}

			int rowcount = 1;
			for (String row : rows) {
				cellCount = 0;
				HSSFRow cellRow = newSheet.createRow(rowcount);
				for (String cellValue : row.split(",")) {
					cellRow.createCell(cellCount).setCellValue(cellValue);
					cellCount++;
				}
				rowcount++;
			}
			wb.write(response.getOutputStream());

		} catch (Exception e) {
			saveError(request, getText("grid.dataCSV.export.error", e.getMessage(), locale));
		}

	}

	/**
	 * Get Grid column names and grid data, then export the grid data to CSV
	 * file
	 * 
	 * @param response
	 * @param request
	 * @param gridHeader
	 * @param gridDatas
	 * @throws IOException
	 * @throws JSONException
	 */
	@RequestMapping(value = "bpm/manageProcess/flowStatisticsGridExport", method = RequestMethod.POST)
	public void flowStatisticsGridExport(HttpServletResponse response, HttpServletRequest request, @RequestParam("gridData") String gridData, @RequestParam("gridHeader") String gridHeader, @RequestParam("gridDataKey") String gridDataKey, @RequestParam("groupHeader") String groupHeader, @RequestParam("gridType") String gridType) throws IOException {
		Locale locale = request.getLocale();

		try {
			// convert the json data into list of map data
			List<Map<String, Object>> gridDataMap = CommonUtil.convertJsonToList(gridData);
			List<String> mapKeys = new ArrayList<String>();
			if (gridDataKey.contains(",")) {
				for (String key : gridDataKey.split(",")) {
					mapKeys.add(key);
				}
			} else {
				mapKeys.add(gridDataKey);
			}

			List<String> rows = new ArrayList<String>();
			// Create the header name and grid data for CSV Export
			for (Map<String, Object> rowMap : gridDataMap) {
				String rowData = "";
				for (String mapKey : mapKeys) {
					if (rowMap.containsKey(mapKey)) {
						// Getting row values
						if (!rowMap.get(mapKey).equals(null) && !rowMap.get(mapKey).equals("null")) {
							rowData = rowData + rowMap.get(mapKey) + ",";
						} else {
							rowData = rowData + Constants.EMPTY_STRING + ",";
						}
						// rowData = rowMap.get(mapKey).equals(null) ? rowData
						// +Constants.EMPTY_STRING+"," : rowData
						// +rowMap.get(mapKey)+",";
					}
				}
				rows.add(rowData);
			}

			Date date = new Date();
			// Get current date string and append with file name
			String currentDate = DateUtil.convertDateToDefalutDateTimeString(date);

			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + gridType + "_" + currentDate + ".xls");
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet newSheet = wb.createSheet(gridType + "_" + currentDate);

			int cellCount = 1;
			int rowCount = 0;
			if (!groupHeader.equalsIgnoreCase("null")) {
				// creating group header column cells
				HSSFRow groupHeaderRow = newSheet.createRow(rowCount);
				// Cell value center alignment
				HSSFCellStyle cellStyle = wb.createCellStyle();
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				for (String groupHeaderName : groupHeader.split(",")) {
					HSSFCell cell = groupHeaderRow.createCell(cellCount);
					cell.setCellValue(groupHeaderName);
					cell.setCellStyle(cellStyle);
					newSheet.addMergedRegion(new CellRangeAddress(0, 0, cellCount, cellCount + 3));
					cellCount = cellCount + 4;
				}
				rowCount += 1;
			}

			// creating column cells
			HSSFRow header = newSheet.createRow(rowCount);
			cellCount = 0;
			for (String columnName : gridHeader.split(",")) {
				header.createCell(cellCount).setCellValue(columnName);
				cellCount++;
			}
			rowCount += 1;
			// creating data cells
			for (String row : rows) {
				cellCount = 0;
				HSSFRow cellRow = newSheet.createRow(rowCount);
				for (String cellValue : row.split(",")) {
					cellRow.createCell(cellCount).setCellValue(cellValue);
					cellCount++;
				}
				rowCount++;
			}
			wb.write(response.getOutputStream());

		} catch (Exception e) {
			saveError(request, getText("grid.dataCSV.export.error", e.getMessage(), locale));
		}

	}

	/**
	 * View the proper xml of process definition
	 * 
	 * @param resourceName
	 * @param deploymentId
	 * @throws IOException
	 */
	@RequestMapping(value = "bpm/manageProcess/processXmlView", method = RequestMethod.GET)
	public ModelAndView processXmlView(@RequestParam("resourceName") String resourceName, @RequestParam("deploymentId") String deploymentId, @RequestParam("isTitleShow") String isTitleShow, @RequestParam("processName") String processName, HttpServletResponse response, ModelMap model, HttpServletRequest request) throws IOException {
		try {
			String xmlData = new String(CommonUtil.getByteArry(rtProcessService.getResourceAsStream(deploymentId, resourceName)));
			// Generate xml into a single line
			String cleanedXml = xmlData.replaceAll("\\s*[\\r\\n]+\\s*", " ").trim();
			// replace single quotes
			String cleanedXmlNew = cleanedXml.replaceAll("'", "\\\\'");
			cleanedXmlNew = cleanedXmlNew.replaceAll("<script", "<scripts");
			cleanedXmlNew = cleanedXmlNew.replaceAll("</script>", "<\\/scripts>");
			model.addAttribute("xmlString", cleanedXmlNew);
			model.addAttribute("resourceName", resourceName);
			model.addAttribute("deploymentId", deploymentId);
			model.addAttribute("isTitleShow", isTitleShow);
			model.addAttribute("processName", processName);
		} catch (Exception e) {
			log.error("cannot get xml view for deployment.." + deploymentId, e);
			saveError(request, "Cannot get the xml view, check if process has valid xml source!");
			return new ModelAndView(new RedirectView("/bpm/manageProcess/listProcess"));
		}
		return new ModelAndView("process/processXmlView", model);
	}

	/**
	 * View the proper svgxml of process definition
	 * 
	 * @param resourceName
	 * @param deploymentId
	 * @throws IOException
	 */
	@RequestMapping(value = "bpm/manageProcess/processPreview", method = RequestMethod.GET)
	public ModelAndView processPreview(@RequestParam("resourceName") String resourceName, @RequestParam("deploymentId") String deploymentId, @RequestParam("isTitleShow") String isTitleShow, @RequestParam("processName") String processName, HttpServletResponse response, ModelMap model, HttpServletRequest request) throws IOException {
		try {
			String svgString = rtProcessService.getSvgString(deploymentId, resourceName);

			// Generate xml into a single line
			// String cleanedString = svgString.replaceAll("&lt;", "<").trim();
			// replace single quotes into a double quotes
			// String cleanedXmlNew = cleanedString.replaceAll("&gt;", ">");

			model.addAttribute("svgString", svgString);
			model.addAttribute("resourceName", resourceName);
			model.addAttribute("deploymentId", deploymentId);
			model.addAttribute("isTitleShow", isTitleShow);
			model.addAttribute("processName", processName);

		} catch (Exception e) {
			log.error("cannot get xml view for deployment.." + deploymentId, e);
			saveError(request, "Cannot get the process preview, check if process has valid process preview!");
			return new ModelAndView(new RedirectView("/bpm/manageProcess/listProcess"));
		}
		return new ModelAndView("process/processPreviewImage", model);
	}

	/**
	 * BulknXmlDownload :Download Zip of Xml files for given deployment id list
	 * 
	 * @param deploymentId
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "bpm/manageProcess/bulkProcessXmlDownload", method = RequestMethod.GET)
	public void bulkProcessXmlDownload(@RequestParam("deploymentId") String deploymentId, @RequestParam("classificationId") String classificationId, HttpServletResponse response, ModelMap model, HttpServletRequest request) throws IOException {
		List<String> deploymentIds = new ArrayList<String>();
		Locale locale = request.getLocale();
		// Get List of Ids for bulk Download
		if (deploymentId.contains(",")) {
			String[] ids = deploymentId.split(",");
			for (String id : ids) {
				deploymentIds.add(id);
			}
		} else {
			deploymentIds.add(deploymentId);
		}
		try {
			List<File> files = new ArrayList<File>();
			// String classificationId =
			// rtProcessService.getClassificationId(deploymentIds.get(0));
			Map<String, Map<String, InputStream>> xmlStreams = rtProcessService.getResourceListAsStream(deploymentIds);
			for (Entry<String, Map<String, InputStream>> entry : xmlStreams.entrySet()) {
				Map<String, InputStream> inputStreamMap = entry.getValue();
				InputStream inputStream = null;
				File file = null;
				for (Entry<String, InputStream> subEntry : inputStreamMap.entrySet()) {
					// new file created
					file = new File(entry.getKey() + "_" + subEntry.getKey());
					// getting inputstream for that new file
					inputStream = subEntry.getValue();
				}
				OutputStream out = new FileOutputStream(file);
				byte buf[] = new byte[1024];
				int len;
				while ((len = inputStream.read(buf)) > 0)
					out.write(buf, 0, len);
				out.close();
				inputStream.close();
				files.add(file);
			}
			zipDownloadProcess(response, files, classificationId);
		} catch (Exception e) {
			saveError(request, getText("process.xmlDownload.error", e.getMessage(), locale));
		}
	}

	/**
	 * BulknXmlDownload :Download Zip of Xml files for given Classification id
	 * list
	 * 
	 * @param ClassificationId
	 * @param response
	 * @param List<File>
	 * @param model
	 * @return
	 * @throws IOException
	 */

	public void zipDownloadProcess(HttpServletResponse response, List<File> files, String classificationId) throws IOException {
		Date date = new Date();
		// Get current date string to append with file
		String currentDate = DateUtil.convertDateToDefalutDateTimeString(date);
		// Set the content type based to zip
		String filename = classificationId + "_" + currentDate;
		ServletOutputStream sout;
		try {
			// added files to zip
			if (files != null && files.size() > 0) {
				sout = response.getOutputStream();
				// ZipOutputStream zos = new ZipOutputStream(new
				// BufferedOutputStream(sout));
				ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(new BufferedOutputStream(sout));
				zipOut.setEncoding("Cp437"); // This should handle your
												// "special" characters
				zipOut.setFallbackToUTF8(true); // For "unknown" characters!
				zipOut.setUseLanguageEncodingFlag(true);
				zipOut.setCreateUnicodeExtraFields(ZipArchiveOutputStream.UnicodeExtraFieldPolicy.NOT_ENCODEABLE);

				for (File file : files) {
					log.info("Adding file " + file.getName());
					if (file.getPath().contains("processArchivalFiles")) {
						zipOut.putArchiveEntry(new ZipArchiveEntry(file.getPath().split("processArchivalFiles")[1]));
					} else {
						zipOut.putArchiveEntry(new ZipArchiveEntry(file.getName()));
					}
					FileInputStream fis = null;
					try {
						fis = new FileInputStream(file);
					} catch (FileNotFoundException fnfe) {
						zipOut.write(("ERROR: Could not find file " + file.getName()).getBytes());
						zipOut.close();
						log.error("Could not find file " + file.getAbsolutePath());
						continue;
					}
					BufferedInputStream fif = new BufferedInputStream(fis);
					int data = 0;
					while ((data = fif.read()) != -1) {
						zipOut.write(data);
					}
					response.setContentType("Content-type: application/zip");
					response.setHeader("Content-disposition", "attachment; filename=\" " + filename + ".zip");
					fif.close();
					log.debug("Finished adding file " + file.getName());
				}
				zipOut.closeArchiveEntry();
				zipOut.flush();
				zipOut.close();
				sout.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("The Exception:" + e.getMessage());
		}
	}

	/**
	 * When we click to browse in particular process it will be download in
	 * particular directory and show the all the files from that directory into
	 * grid
	 * 
	 * @param resourceName
	 * @param deploymentId
	 * @param response
	 * @param request
	 * @param model
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "bpm/manageProcess/browseXmlList", method = RequestMethod.GET)
	public ModelAndView browseXmlList(@RequestParam("resourceName") String resourceName, @RequestParam("deploymentId") String deploymentId, HttpServletResponse response, HttpServletRequest request, ModelMap model) throws IOException {
		InputStream xmlStream = null;
		Locale locale = request.getLocale();
		try {
			response.setContentType("application/xml");
			response.setHeader("Content-Disposition", "attachment; filename=" + resourceName);
			xmlStream = rtProcessService.getResourceAsStream(deploymentId, resourceName);
			String downloadFile = getServletContext().getRealPath("/resources") + "/downloads/" + deploymentId + "_" + resourceName;
			String downloadDir = getServletContext().getRealPath("/resources") + "/downloads/";
			// Create the directory if it doesn't exist
			File dirPath = new File(downloadDir);
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}
			OutputStream bos = new FileOutputStream(downloadFile);
			FileCopyUtils.copy(xmlStream, bos);
			bos.close();
			// close the stream
			xmlStream.close();
			String fileListScript = fileListGirdScript(request);
			model.addAttribute("script", fileListScript);
		} catch (Exception e) {
			saveError(request, getText("process.xmlDownload.error", e.getMessage(), locale));
		} finally {
			if (xmlStream != null) {
				xmlStream.close();
			}
		}

		return new ModelAndView("process/fileListGrid", model);

	}

	/**
	 * Download the xml for given file object
	 * 
	 * @param fileObj
	 * @param fileName
	 * @param response
	 * @param request
	 * @param model
	 * @throws IOException
	 */
	@RequestMapping(value = "bpm/manageProcess/selectedXmlDownload", method = RequestMethod.GET)
	public void selectedXmlDownload(@RequestParam("fileObj") String fileObj, @RequestParam("fileName") String fileName, HttpServletResponse response, HttpServletRequest request, ModelMap model) throws IOException {
		File file = new File(fileObj);
		FileInputStream stream = new FileInputStream(file);
		response.setContentType("application/xml");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		FileCopyUtils.copy(stream, response.getOutputStream());

	}

	/**
	 * Get a fileObj information and add those files to zip and download the zip
	 * (BulkDownload of files)
	 * 
	 * @param fileObj
	 * @param response
	 * @param request
	 * @param model
	 * @throws IOException
	 */
	@RequestMapping(value = "bpm/manageProcess/bulkXmlDownload", method = RequestMethod.GET)
	public void bulkXmlDownload(@RequestParam("fileObj") String fileObj, HttpServletResponse response, HttpServletRequest request, ModelMap model) throws IOException {
		List<File> fileList = new ArrayList<File>();
		Locale locale = request.getLocale();
		try {
			if (fileObj.contains(",")) {
				String[] ids = fileObj.split(",");
				File file = null;
				for (String id : ids) {
					file = new File(id);
					fileList.add(file);
				}
			} else {
				File file = new File(fileObj);
				fileList.add(file);
			}
			ServletOutputStream sout = response.getOutputStream();
			ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(sout));
			for (File file : fileList) {
				log.info("Adding file " + file.getName());
				zos.putNextEntry(new ZipEntry(file.getName()));
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(file);

				} catch (FileNotFoundException fnfe) {

					zos.write(("ERROR: Could not find file " + file.getName()).getBytes());
					zos.closeEntry();
					log.error("Could not find file " + file.getAbsolutePath());
					continue;
				}
				BufferedInputStream fif = new BufferedInputStream(fis);
				int data = 0;
				while ((data = fif.read()) != -1) {
					zos.write(data);
				}
				fif.close();
				zos.closeEntry();
				log.debug("Finished adding file " + file.getName());
			}
			// Set the content type based to zip
			response.setContentType("Content-type: text/zip");
			// Get current date string to append with file
			Date date = new Date();
			String defaultTime = DateUtil.convertDateToDefalutDateTimeString(date);
			response.setHeader("Content-Disposition", "attachment; filename=ProcessXml_" + defaultTime + ".zip");
			zos.close();
		} catch (Exception e) {
			saveError(request, getText("process.xmlDownload.error", e.getMessage(), locale));
		}

	}

	/**
	 * Show the Process information in detail view
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/manageProcess/showProcessInformation", method = RequestMethod.GET)
	public ModelAndView showProcessInformation(@RequestParam("id") String id, ModelMap model) {
		try {

			ProcessDefinition processDefinition = processDefinitionService.getProcessDefinitionById(id);
			List<ProcessDefinition> processDefinitions = new ArrayList<ProcessDefinition>();
			processDefinitions.add(processDefinition);
			List<Map<String, Object>> processDefinitionsMapAsList = processDefinitionService.resolveProcessDefinitions(processDefinitions);
			model.putAll(processDefinitionsMapAsList.get(0));
			model.addAttribute("gridType", "myInstances");
		} catch (BpmException e) {
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("process/processInformation", model);

	}

	@RequestMapping(value = "bpm/manageProcess/suspendProcessInstance", method = RequestMethod.GET)
	public String suspendProcessInstanceById(@RequestParam("id") String id, @RequestParam("processGridType") String processGridType, @RequestParam("taskId") String taskId, @RequestParam("resourceName") String resourceName, HttpServletRequest request) {
		Locale locale = request.getLocale();
		try {
			rtProcessService.suspendProcessInstanceById(id);
			saveMessage(request, getText("instance.suspend.success", locale));

		} catch (Exception e) {
			saveError(request, getText("instance.suspend.error", e.getMessage(), locale));
		}

		return "redirect:" + processGridType;
	}

	@RequestMapping(value = "bpm/manageProcess/activateProcessInstance", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> activateProcessInstanceById(@RequestParam("id") String id, @RequestParam("processGridType") String processGridType, @RequestParam("taskId") String taskId, @RequestParam("resourceName") String resourceName, @RequestParam("processDefinitionId") String processDefinitionId, HttpServletRequest request) {
		Locale locale = request.getLocale();
		Map<String, String> responseMap = new HashMap<String, String>();
		try {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			rtProcessService.activateProcessInatanceById(id);
			OperatingFunctionAudit operFunAudit = new OperatingFunctionAudit(user.getId(), taskId, resourceName, OperatingFunction.ACTIVATE, id, processDefinitionId);
			processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
			saveMessage(request, getText("instance.activate.success", locale));
			responseMap.put("success", getText("instance.activate.success", locale));
		} catch (Exception e) {
			saveError(request, getText("instance.activate.error", e.getMessage(), locale));
			responseMap.put("success", getText("instance.activate.error", locale));

		}
		return responseMap;
	}

	@RequestMapping(value = "/process/getAllClassifications", method = RequestMethod.GET)
	public @ResponseBody Map getAllClassifications(HttpServletRequest request) {

		List<Map<String, String>> classificationListMap = new ArrayList<Map<String, String>>();
		Map<String, List<Map<String, String>>> classificationValue = new HashMap<String, List<Map<String, String>>>();
		Map<String, String> classificationDetails = null;
		try {
			List<LabelValue> classificationList = (List<LabelValue>) request.getSession().getServletContext().getAttribute(Constants.PROCESS_CLASSIFICATION);

			for (LabelValue classification : classificationList) {
				classificationDetails = new HashMap<String, String>();
				classificationDetails.put("id", classification.getValue());
				classificationDetails.put("name", classification.getLabel());
				classificationListMap.add(classificationDetails);
			}
			classificationValue.put("classifications", classificationListMap);
			return classificationValue;
		} catch (Exception e) {
			log.error("Error while getting all Classifications  : " + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Show the process List organization tree
	 * 
	 * @param title
	 * @param requestorId
	 * @param selectType
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "bpm/admin/processTree", method = RequestMethod.GET)
	public ModelAndView showProcessTree(ModelMap model, @RequestParam("selectionType") String selectionType, @RequestParam("appendTo") String appendTo, @RequestParam("selectedValues") String selectedValues, @RequestParam("callAfter") String callAfter, User user, BindingResult errors, HttpServletRequest request) throws Exception {
		List<LabelValue> processList = rtProcessService.getAllProcessAsLabelValue();
		List<LabelValue> newProcessList = new ArrayList<LabelValue>();
		for (LabelValue process : processList) {
			LabelValue lableVal = new LabelValue();
			lableVal.setLabel(process.getLabel());
			String id = process.getValue().replaceAll(":", "_");
			lableVal.setValue(id);
			newProcessList.add(lableVal);
		}
		JSONArray nodes = new JSONArray();
		nodes = CommonUtil.getNodesFromLabelValue(newProcessList);
		model.addAttribute("nodes", nodes);
		model.addAttribute("nodeDetail", newProcessList);
		model.addAttribute("selectionType", selectionType);
		model.addAttribute("selection", "form");
		model.addAttribute("selectedValues", selectedValues);
		model.addAttribute("appendTo", appendTo);
		model.addAttribute("callAfter", callAfter);
		model.addAttribute("selectionType", selectionType);
		model.addAttribute("selectedValues", selectedValues);
		return new ModelAndView("/admin/showModuleTree", model);
	}

	/**
	 * Show the process List
	 * 
	 * @param title
	 * @param requestorId
	 * @param selectType
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "bpm/admin/showProcessTreeList", method = RequestMethod.GET)
	public @ResponseBody String showProcessTreeList(ModelMap model, @RequestParam("currentNode") String currentNode, @RequestParam("rootNode") String rootNode, @RequestParam("nodeLevel") String nodeLevel) throws Exception {
		List<LabelValue> processList = rtProcessService.getAllProcessAsLabelValue();
		List<LabelValue> newProcessList = new ArrayList<LabelValue>();
		for (LabelValue process : processList) {
			LabelValue lableVal = new LabelValue();
			lableVal.setLabel(process.getLabel());
			String id = process.getValue().replaceAll(":", "_");
			lableVal.setValue(process.getLabel());
			newProcessList.add(lableVal);
		}
		JSONArray nodes = new JSONArray();
		nodes = CommonUtil.getNodesFromLabelValue(newProcessList);
		return nodes.toString();
	}

	/**
	 * To search the process by its name
	 * 
	 * @param processName
	 * @param appendTo
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "bpm/admin/searchProcessNameList", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, String>> searchProcessNameList(@RequestParam("processName") String processName, @RequestParam("appendTo") String appendTo, ModelMap model, HttpServletRequest request) throws Exception {
		List<Map<String, String>> processDetails = new ArrayList<Map<String, String>>();
		try {
			List<LabelValue> processList = rtProcessService.searchProcessNames(processName);
			if (processList != null) {
				for (LabelValue process : processList) {
					Map<String, String> userDetail = new HashMap<String, String>();
					String id = process.getValue().replaceAll(":", "_");
					userDetail.put("processName", process.getLabel());
					userDetail.put("id", process.getLabel());
					processDetails.add(userDetail);
				}
				return processDetails;
			}
		} catch (Exception e) {
			log.error("Error while getting process list " + e.getMessage());
		}
		return new ArrayList<Map<String, String>>();
	}

	/**
	 * Get a list of files from specified directory and form a list of map for
	 * that file detail and generate script for that file detail list
	 * 
	 * @param request
	 * @return
	 */
	private String fileListGirdScript(HttpServletRequest request) throws BpmException {
		// directory path to get list of files
		String downloadDir = getServletContext().getRealPath("/resources") + "/downloads/";
		File folder = new File(downloadDir);
		File[] listOfFiles = folder.listFiles();
		// construct file detail map list
		List<Map<String, Object>> fileListInfo = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				Map<String, Object> fileInfo = new HashMap<String, Object>();
				fileInfo.put("fileName", listOfFiles[i].getName());
				long filesize = listOfFiles[i].length();
				long filesizeInKB = filesize / 1024;
				fileInfo.put("filesize", filesizeInKB);
				fileInfo.put("fileObj", listOfFiles[i]);
				fileListInfo.add(fileInfo);
			}
		}
		// generate script for grid
		String fileListScript = ProcessDefinitionUtil.generateScriptForListOfFiles(velocityEngine, fileListInfo);
		return fileListScript;
	}

	/**
	 * Gets the grid script for the process Instance that can be rendered on
	 * screen
	 * 
	 * @param processInstances
	 *            the list of process Instance that has to be viewed as grid
	 * @return the script for getting grid on screen
	 */
	private String processInstancesGridScript(List<HistoricProcessInstance> processInstances, String type, Locale locale, String processGridType) {
		String processInstanceScript = null;
		try {
			List<Map<String, Object>> processInstancesMapAsList = rtProcessService.resolveHistoricProcessInstance(processInstances, processGridType);
			processInstanceScript = ProcessDefinitionUtil.generateScriptForProcessInstance(velocityEngine, processInstancesMapAsList, type, locale);
		} catch (Exception e) {
			processInstanceScript = e.getMessage();
		}
		return processInstanceScript;
	}

	/**
	 * Get all process as label value
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "bpm/admin/getAllProcess", method = RequestMethod.GET)
	public @ResponseBody List<LabelValue> getAllProcess() throws Exception {
		List<LabelValue> processList = new ArrayList<LabelValue>();
		try {
			processList = rtProcessService.getAllProcessAsLabelValue();
		} catch (Exception e) {
			log.error("Error while getting all process  : " + e.getMessage(), e);
		}
		return processList;
	}

	/**
	 * get all process as label by system defined as true
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "bpm/admin/aetAllProcessBySystemDefiend", method = RequestMethod.GET)
	public @ResponseBody List<LabelValue> getAllProcessBySystemDefined(ModelMap model, @RequestParam("isSystemDefined") boolean isSystemDefined) {
		List<LabelValue> processList = new ArrayList<LabelValue>();
		try {
			processList = rtProcessService.getAllProcessBySystemDefined(isSystemDefined);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return processList;
	}

	/**
	 * to get all process as map
	 * 
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping(value = "/process/getAllProcessAsMap", method = RequestMethod.GET)
	public @ResponseBody Map<String, List<Map<String, String>>> getAllProcessAsMap(HttpServletRequest request) {
		List<Map<String, String>> processList = new ArrayList<Map<String, String>>();
		Map<String, List<Map<String, String>>> resultValue = new HashMap<String, List<Map<String, String>>>();
		try {
			List<LabelValue> processListAsLabelValue = rtProcessService.getAllProcessAsLabelValue();
			if (processListAsLabelValue != null && !processListAsLabelValue.isEmpty() && processListAsLabelValue.size() > 0) {
				for (LabelValue labelValue : processListAsLabelValue) {
					Map<String, String> processMap = new HashMap<String, String>();
					processMap.put("processName", labelValue.getLabel());
					processMap.put("id", labelValue.getValue());
					processList.add(processMap);
				}
				resultValue.put("process", processList);
				return resultValue;
			}
		} catch (Exception e) {
			log.error("Error while gettting all process as map" + e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @param taskDetails
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/manageProcess/activateProcessInstances", method = RequestMethod.GET)
	public RedirectView activateProcessInstances(@RequestParam("taskDetails") String taskDetails, HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		try {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Map<String, Object>> taskDetailsMap = CommonUtil.convertJsonToList(taskDetails);
			OperatingFunctionAudit operFunAudit = null;
			for (Map<String, Object> taskDetailMap : taskDetailsMap) {
				rtProcessService.activateProcessInatanceById(taskDetailMap.get("executionId").toString());
				operFunAudit = new OperatingFunctionAudit(user.getId(), taskDetailMap.get("taskId").toString(), taskDetailMap.get("resourceName").toString(), OperatingFunction.ACTIVATE, taskDetailMap.get("executionId").toString(), null);
				processDefinitionService.saveOperatingFunctionAudit(operFunAudit);
			}
			saveMessage(request, getText("instance.activate.success", locale));
		} catch (Exception e) {
			saveError(request, getText("instance.activate.error", e.getMessage(), locale));
		}
		return new RedirectView("/bpm/listView/showListViewGrid?listViewName=WORKFLOW_INSTANCE_MANAGER&container=target&title=Workflow Instance Manage&listViewParams=[{}]");
	}

	/**
	 * BulknXmlDownload :Download Zip of Xml files for given deployment id list
	 * 
	 * @param deploymentId
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "bpm/manageProcess/bulkProcessArchivalExport", method = RequestMethod.POST)
	public void bulkProcessArchivalExport(@RequestParam("processDetails") String processDetails, HttpServletResponse response, ModelMap model, HttpServletRequest request) throws IOException {
		Locale locale = request.getLocale();
		try {
			List<Map<String, Object>> processDetailsMap = CommonUtil.convertJsonToList(processDetails);
			List<File> files = new ArrayList<File>();
			String uploadDir = getServletContext().getRealPath("/resources");
			for (Map<String, Object> processDetail : processDetailsMap) {
				final File folder = new File(uploadDir + processDetail.get("path").toString());
				for (final File fileEntry : folder.listFiles()) {
					files.add(fileEntry);
				}
			}
			zipDownloadProcess(response, files, "archive");
		} catch (Exception e) {
			saveError(request, getText("process.xmlDownload.error", e.getMessage(), locale));
		}
	}

	/**
	 * BulknXmlDownload :Download Zip of Xml files for given deployment id list
	 * 
	 * @param deploymentId
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "bpm/manageProcess/exportWorkFlowInstance", method = RequestMethod.POST)
	public void exportWorkFlowInstance(@RequestParam("processDetails") String processDetails, @RequestParam("withTrace") boolean withTrace, HttpServletResponse response, ModelMap model, HttpServletRequest request) throws IOException {
		Locale locale = request.getLocale();
		try {
			List<Map<String, Object>> processDetailsMap = CommonUtil.convertJsonToList(processDetails);
			// String classificationId =
			// rtProcessService.getClassificationId(deploymentIds.get(0));
			rtProcessService.designWorkflowInstanceExport(processDetailsMap, withTrace, response, getServletContext().getRealPath("/styles"), getServletContext().getRealPath("/scripts"));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			saveError(request, getText("process.xmlDownload.error", e.getMessage(), locale));
		}
	}

	@Autowired
	public void setProcessDefinitionService(ProcessDefinitionService processDefinitionService) {
		this.processDefinitionService = processDefinitionService;
	}

	@Autowired
	public void setRtProcessService(com.eazytec.bpm.runtime.process.service.ProcessService rtProcessService) {
		this.rtProcessService = rtProcessService;
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
	public void setTaskDefinitionService(TaskDefinitionService taskDefinitionService) {
		this.taskDefinitionService = taskDefinitionService;
	}

	@Autowired
	public void setOperatingFunctionService(OperatingFunctionService operatingFunctionService) {
		this.operatingFunctionService = operatingFunctionService;
	}

	@Autowired
	public void setUserOpinionService(UserOpinionService userOpinionService) {
		this.userOpinionService = userOpinionService;
	}

	public DepartmentService getDepartmentService() {
		return departmentService;
	}

	@Autowired
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

}
