package com.eazytec.webapp.controller;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eazytec.bpm.admin.notification.service.NotificationService;
import com.eazytec.bpm.common.util.FormDefinitionUtil;
import com.eazytec.bpm.runtime.process.service.ProcessService;
import com.eazytec.bpm.runtime.task.service.OperatingFunctionService;
import com.eazytec.bpm.runtime.task.service.TaskService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.FileUtil;
import com.eazytec.common.util.TemplateUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceException;
import com.eazytec.exceptions.DataSourceValidationException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.MetaForm;
import com.eazytec.model.Notification;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.service.NoticeService;

@Controller
public class NoticeController extends BaseFormController {

	private NoticeService noticeService;
	private ProcessService rtProcessService;
	private VelocityEngine velocityEngine;

	private static String ROLE_LIST = "role";
	private static String GROUP_LIST = "group";
	private static String USER_LIST = "user";
	private static String DEPARTMENT_LIST = "department";
	private static String IS_NOTIFICATION = "isNotice";
	private static String STATUS = "status";
	private TaskService rtTaskService;
	private OperatingFunctionService operatingFunctionService;

	private NotificationService notificationService;
	private RuntimeService runtimeService;

	/**
	 * To create Notice
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/notice/createNotice", method = RequestMethod.GET)
	public ModelAndView createNotice(ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		try {
			Map<String, Object> context = new HashMap<String, Object>();
			context.put("isEdit", false);
			context.put("isNotice", "true");
			ProcessDefinition noticeDefinition = noticeService.getNoticeDefinition();
			String noticeScript = "";
			String noticeId = noticeDefinition.getId();
			StartFormData startForm = rtProcessService.getStartForm(noticeId);
			if (startForm != null && ((startForm.getFormProperties() != null && startForm.getFormProperties().size() > 0) || startForm.getFormKey() != null)) {
				noticeScript = designStartProcessScript(noticeDefinition, context);
				model.addAttribute("hasForm", "true");
			} else {
				rtProcessService.startProcessInstanceById(noticeId);
				saveMessage(request, getText("notice.created", locale));
				noticeScript = "";
				model.addAttribute("hasForm", "false");
				model.addAttribute("isNotice", "true");
			}
			model.addAttribute("isEdit", "false");
			model.addAttribute("script", noticeScript);
		} catch (BpmException e) {
			saveError(request, getText("notice.create.failed", e.getMessage(), locale));
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			saveError(request, getText("notice.create.exception", locale));
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("startProcess", model);
	}

	/**
	 * To show Notice
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/notice/showDetailNotice", method = RequestMethod.GET)
	public ModelAndView showDetailNotice(@RequestParam("noticeInstanceId") String noticeInstanceId, ModelMap model, HttpServletRequest request) {
		Map<String, Object> context = new HashMap<String, Object>();
		String script = null;
		try {
			Map<String, Object> properties = CommonUtil.getStringRepresentations(noticeService.getNoticeItemValues(noticeInstanceId));
			String createdUser = (String) properties.get("CREATEDBY");
			// show news in read-only
			Set<Role> userRoles = CommonUtil.getLoggedInUser().getRoles();
			if (!userRoles.isEmpty() && userRoles != null) {
				if (!userRoles.toString().contains("ROLE_ADMIN") && !createdUser.equalsIgnoreCase(CommonUtil.getLoggedInUserId())) {
					context.put("isFormReadOnly", true);
				}
			}

			ProcessDefinition noticeDefinition = noticeService.getNoticeDefinition();
			MetaForm form = noticeService.getNoticeForm(noticeInstanceId);
			context.put("html", form.getHtmlSource());
			context.put("formId", form.getFormName());
			context.put("formAction", "bpm/notice/updateNoticeItem");
			context.put("properties", properties);
			context.put("pastValuesJson", CommonUtil.convertMapToJson(properties));
			context.put("instanceId", noticeInstanceId);
			context.put("processDefinition", noticeDefinition);
			context.put("isEdit", true);

			script = designEditProcessScript(noticeDefinition, noticeInstanceId, context);
			model.addAttribute("script", script);
			// log.info("Notice Detail is retrived");
			return new ModelAndView("notice/showDetailNotice", model);
		} catch (BpmException e) {
			saveError(request, e.getMessage());
			log.error("Cannot get notice details for instance " + noticeInstanceId + e.getMessage(), e);
			return new ModelAndView("redirect:noticeList");
		} catch (Exception e) {
			e.printStackTrace();
			saveError(request, "Problem getting notice details, check log for errors!");
			log.error("Cannot get notice details for instance " + noticeInstanceId + e.getMessage(), e);
			return new ModelAndView("redirect:noticeList");
		}

	}

	@RequestMapping(value = "bpm/news/showNoticeTemplate", method = RequestMethod.GET)
	public ModelAndView showNoticeTemplate(@RequestParam("noticeInstanceId") String noticeInstanceId, ModelMap model, HttpServletRequest request) {
		StringWriter htmlContent = new StringWriter();
		try {
			MetaForm form = noticeService.getNoticeForm(noticeInstanceId);
			Map<String, Object> properties = noticeService.getNoticeItemValues(noticeInstanceId);
			VelocityContext contextDetails = new VelocityContext(properties);
			if (form != null && form.getPrintTemplate() != null && !form.getPrintTemplate().isEmpty()) {
				velocityEngine.evaluate(contextDetails, htmlContent, "Print Template", String.valueOf(form.getPrintTemplate()));
			}
			model.addAttribute("script", htmlContent);

			return new ModelAndView("news/showDetailNews", model);
		} catch (BpmException e) {
			saveError(request, e.getMessage());
			log.error("Cannot get news details for instance " + noticeInstanceId + e.getMessage(), e);
			return new ModelAndView("redirect:newsList");
		} catch (Exception e) {
			saveError(request, "Problem getting news details, check log for errors!");
			log.error("Cannot get news details for instance " + noticeInstanceId + e.getMessage(), e);
			return new ModelAndView("redirect:newsList");
		}

	}

	@RequestMapping(value = "bpm/notice/submitNoticeForm", method = RequestMethod.POST)
	public ModelAndView submitNoticeForm(HttpServletRequest request, ModelMap model, final RedirectAttributes redirectAttributes) throws JSONException {
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("isEdit", false);
		Locale locale = request.getLocale();
		try {

			Map<String, String> filePathsMap = null;
			Map<String, String> rtFormValues = FormDefinitionUtil.getFormParams(request.getParameterMap());
			Map<String, String[]> rtSubFormValues = FormDefinitionUtil.getSubFormParams(request.getParameterMap());
			Set<User> publishUser = new HashSet<User>();
			String processDefinitionId = rtFormValues.get("processDefinitionId");
			rtFormValues.remove("processDefinitionId");
			rtFormValues.remove("nextTaskOrganizers");
			rtFormValues.remove("nextTaskCoordinators");
			rtFormValues.remove("organizerOrders");
			// Map<String, byte[]>files = null;
			String title = rtFormValues.get("title");
			String content = rtFormValues.get("mainContent");
			if (content == null) {
				content = "Notice Form EazyTec";
			}
			String uploadDir = getServletContext().getRealPath("/resources") + "/" + request.getRemoteUser() + "/";
			Map<String, byte[]> files = FileUtil.getFileUploadMap(request);
			ProcessInstance noticeInstance = noticeService.submitStartForm(processDefinitionId, rtFormValues, rtSubFormValues, files, filePathsMap);
			if (noticeInstance != null) {

				if (files != null) {
					filePathsMap = FileUtil.uploadFileForTask(request, uploadDir, null, noticeInstance.getId(), uploadDir, runtimeService, FileUtil.PROCESS_TYPE_START);
				}
				if (rtFormValues.containsKey(STATUS) && rtFormValues.get(STATUS) != null && rtFormValues.get(STATUS).equalsIgnoreCase("formal")) {
					if (rtFormValues.containsKey(USER_LIST) && rtFormValues.get(USER_LIST) != null && !rtFormValues.get(USER_LIST).isEmpty()) {
						String users = rtFormValues.get(USER_LIST);
						List<String> userIds = Arrays.asList(users.split("\\s*,\\s*"));
						publishUser.addAll(getUserManager().getUserByUserIds(userIds));
					}
					if (rtFormValues.containsKey(GROUP_LIST) && rtFormValues.get(GROUP_LIST) != null && !rtFormValues.get(GROUP_LIST).isEmpty()) {
						String groups = rtFormValues.get(GROUP_LIST);
						List<String> groupIds = Arrays.asList(groups.split("\\s*,\\s*"));
						publishUser.addAll(getUserManager().geUserByGroupIds(groupIds));
					}
					if (rtFormValues.containsKey(ROLE_LIST) && rtFormValues.get(ROLE_LIST) != null && !rtFormValues.get(ROLE_LIST).isEmpty()) {
						String roles = rtFormValues.get(ROLE_LIST);
						List<String> roleIds = Arrays.asList(roles.split("\\s*,\\s*"));
						publishUser.addAll(getUserManager().getUserByRoleIds(roleIds));
					}
					if (rtFormValues.containsKey(DEPARTMENT_LIST) && rtFormValues.get(DEPARTMENT_LIST) != null && !rtFormValues.get(DEPARTMENT_LIST).isEmpty()) {
						String depList = rtFormValues.get(DEPARTMENT_LIST);
						List<String> depIds = Arrays.asList(depList.split("\\s*,\\s*"));
						publishUser.addAll(getUserManager().getUsersByDepartmentIds(depIds));
					}
					/*
					 * if(rtFormValues.containsKey("userName") &&
					 * rtFormValues.get("userName") != null){ String users =
					 * rtFormValues.get("userName"); List<String> userIds =
					 * Arrays.asList(users.split("\\s*,\\s*"));
					 * publishUser.addAll(getUserManager().getUserByUserIds(
					 * userIds)); }
					 */
					if (rtFormValues.containsKey(IS_NOTIFICATION) && rtFormValues.get(IS_NOTIFICATION) != null) {
						sendNotification(publishUser, title, content);
					}
					insertNoticeToUser(publishUser, noticeInstance.getId());
					model.addAttribute("noticeInstanceId", noticeInstance.getId());
					/*
					 * if(noticeInstance.getId() !=null){
					 * endEventScript.put("processInstanceId",
					 * noticeInstance.getId());
					 * redirectAttributes.addFlashAttribute(
					 * "eventScriptDetails", endEventScript); }
					 */
					log.info("Instantiated notice for defintion: " + processDefinitionId);

				}
				log.info("Notice : " + title + " created successfully");
				saveMessage(request, getText("notice.created", locale));
			} else if (noticeInstance == null) {
				log.info("Could not instantiate notice: " + processDefinitionId);
				saveError(request, getText("notices.create.failed", locale));
			}
		} catch (EazyBpmException e) {
			saveError(request, e.getMessage());
			log.error(e.getMessage(), e);
			model.addAttribute("hasForm", "true");
			String script = getStratProcessScript(request.getParameterMap(), context);
			model.addAttribute("script", script);
			return new ModelAndView("startProcess", model);
		} catch (BpmException e) {
			saveError(request, e.getMessage());
			log.error(e.getMessage(), e);
			model.addAttribute("hasForm", "true");
			String script = getStratProcessScript(request.getParameterMap(), context);
			model.addAttribute("script", script);
			return new ModelAndView("startProcess", model);
		} catch (DataSourceValidationException e) {
			saveError(request, e.getMessage());
			log.error(e.getMessage(), e);
			String script = getStratProcessScript(request.getParameterMap(), context);
			model.addAttribute("hasForm", "true");
			model.addAttribute("script", script);
			return new ModelAndView("startProcess", model);
		} catch (DataSourceException e) {
			saveError(request, "Failed to create notices, your input values do not integrate with Database!");
			log.error(e.getMessage(), e);
			String script = getStratProcessScript(request.getParameterMap(), context);
			model.addAttribute("hasForm", "true");
			model.addAttribute("script", script);
			return new ModelAndView("startProcess", model);
		} catch (Exception e) {
			e.printStackTrace();
			saveError(request, "Failed to create notice!");
			model.addAttribute("message", "Failed to create notice" + e.getMessage());
			log.error(e.getMessage(), e);
			String script = getStratProcessScript(request.getParameterMap(), context);
			model.addAttribute("hasForm", "true");
			model.addAttribute("script", script);
			return new ModelAndView("startProcess", model);
		}

		return new ModelAndView("redirect:noticeList");
	}

	private Map<String, String> insertNoticeToUser(Set<User> userList, String noticeInstanceId) throws BpmException {
		Map<String, String> userinfo = new HashMap<String, String>();
		for (User user : userList) {
			rtTaskService.addIdentityLinkDetail(noticeInstanceId, user.getId(), false);
		}
		return userinfo;
	}

	private void sendNotification(Set<User> userList, String title, String content) throws BpmException {
		if (userList != null) {
			Calendar cal = Calendar.getInstance();
			for (User user : userList) {

				if (user.getEmail() != null && !user.getEmail().isEmpty()) {
					Notification notification = new Notification();
					notification.setNotificationType("EmailNotification");
					notification.setStatus("active");
					notification.setType("user");
					notification.setTypeId(user.getEmail());
					notification.setMessageSendOn(cal.getTime());
					notification.setSubject(title);
					notification.setMessage(content);
					notificationService.saveOrUpdateNotification(notification);
				}
			}
		}

	}

	@RequestMapping(value = "bpm/notice/noticeList", method = RequestMethod.GET)
	public ModelAndView noticeList(ModelMap model, HttpServletRequest request) {
		return new ModelAndView("notice/noticeList", model);
	}

	@RequestMapping(value = "bpm/notice/updateNoticeItem", method = RequestMethod.POST)
	public ModelAndView updateNoticeItem(HttpServletRequest request, ModelMap model, final RedirectAttributes redirectAttributes) {
		Locale locale = request.getLocale();
		try {
			Map<String, String> rtFormValues = FormDefinitionUtil.getFormParams(request.getParameterMap());
			String insId = rtFormValues.get("processInstanceId");
			Map<String, String[]> rtSubFormValues = FormDefinitionUtil.getSubFormParams(request.getParameterMap());
			rtFormValues.remove("processDefinitionId");
			String uploadDir = getServletContext().getRealPath("/resources") + "/" + request.getRemoteUser() + "/";
			Map<String, byte[]> files = FileUtil.getFileUploadMap(request);
			Map<String, String> filePathsMap = null;
			String title = rtFormValues.get("title");
			String content = rtFormValues.get("mainContent");
			if (content == null) {
				content = "Notice Form EazyTec";
			}

			Set<User> publishUser = new HashSet<User>();
			if (rtFormValues.containsKey(STATUS) && rtFormValues.get(STATUS) != null && rtFormValues.get(STATUS).equalsIgnoreCase("formal")) {
				if (rtFormValues.containsKey(USER_LIST) && rtFormValues.get(USER_LIST) != null && !rtFormValues.get(USER_LIST).isEmpty()) {
					String users = rtFormValues.get(USER_LIST);
					List<String> userIds = Arrays.asList(users.split("\\s*,\\s*"));
					publishUser.addAll(getUserManager().getUserByUserIds(userIds));
				}
				if (rtFormValues.containsKey(GROUP_LIST) && rtFormValues.get(GROUP_LIST) != null && !rtFormValues.get(GROUP_LIST).isEmpty()) {
					String groups = rtFormValues.get(GROUP_LIST);
					List<String> groupIds = Arrays.asList(groups.split("\\s*,\\s*"));
					publishUser.addAll(getUserManager().geUserByGroupIds(groupIds));
				}
				if (rtFormValues.containsKey(ROLE_LIST) && rtFormValues.get(ROLE_LIST) != null && !rtFormValues.get(ROLE_LIST).isEmpty()) {
					String roles = rtFormValues.get(ROLE_LIST);
					List<String> roleIds = Arrays.asList(roles.split("\\s*,\\s*"));
					publishUser.addAll(getUserManager().getUserByRoleIds(roleIds));
				}
				if (rtFormValues.containsKey(DEPARTMENT_LIST) && rtFormValues.get(DEPARTMENT_LIST) != null && !rtFormValues.get(DEPARTMENT_LIST).isEmpty()) {
					String depList = rtFormValues.get(DEPARTMENT_LIST);
					List<String> depIds = Arrays.asList(depList.split("\\s*,\\s*"));
					publishUser.addAll(getUserManager().getUsersByDepartmentIds(depIds));
				}
			
				if (rtFormValues.containsKey(IS_NOTIFICATION) && rtFormValues.get(IS_NOTIFICATION) != null) {
					sendNotification(publishUser, title, content);
				}
				insertNoticeToUser(publishUser, insId);
				model.addAttribute("noticeInstanceId", insId);
			}
			operatingFunctionService.attachmentOperation(rtFormValues, null);
			noticeService.updateNotice(rtFormValues, rtSubFormValues, files, filePathsMap);
			if (files != null) {
				filePathsMap = FileUtil.uploadFileForTask(request, uploadDir, null, insId, uploadDir, runtimeService, FileUtil.PROCESS_TYPE_START);
			}
			log.info("Updated Notice : " + rtFormValues.get("title"));
			saveMessage(request, getText("notice.updated", locale));
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
			saveError(request, "Failed to update, your input values do not integrate with Database!");
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			saveError(request, "Failed to update Notice!");
			model.addAttribute("message", "Failed to update Notice" + e.getMessage());
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("redirect:noticeList");
	}

	private String designStartProcessScript(ProcessDefinition noticeDefinition, Map<String, Object> permissionVal) throws BpmException {
		return TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showStartHtmlForm.vm", noticeService.designStartProcessScript(noticeDefinition, velocityEngine, permissionVal));
	}

	private String designEditProcessScript(ProcessDefinition noticeDefinition, String insId, Map<String, Object> permissionVal) throws BpmException {
		return TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showStartHtmlForm.vm", noticeService.designEditNoticeScript(noticeDefinition, velocityEngine, insId, permissionVal, "bpm/notice/updateNoticeItem"));
	}

	private Map<String, Object> getContextDetail(ProcessDefinition noticeDefinition, Map<String, Object> permissionVal) {
		return noticeService.designStartProcessScript(noticeDefinition, velocityEngine, permissionVal);
	}

	private String getStratProcessScript(Map<String, Object> properties, Map<String, Object> permissionVal) throws JSONException {
		Map<String, Object> context = new HashMap<String, Object>();
		ProcessDefinition noticeDefinition = noticeService.getNoticeDefinition();
		context.putAll(getContextDetail(noticeDefinition, permissionVal));
		context.put("properties", CommonUtil.getKeyValuePairs(properties));
		context.put("pastValuesJson", CommonUtil.convertMapToJson(properties));
		// context.put("processDefinition", noticeDefinition);
		return TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showStartHtmlForm.vm", context);
	}

	@RequestMapping(value = "bpm/notice/updateNoticeReadStatus", method = RequestMethod.GET)
	public ModelAndView updateReadNotice(@RequestParam("noticeInstanceId") String noticeInstanceId, HttpServletRequest request) {
		rtTaskService.updateIdentityLinkDetailReadStatus(noticeInstanceId, CommonUtil.getLoggedInUserId(), true);
		log.info("NoticeReadStatus Updated Successfully");
		return new ModelAndView("redirect:noticeList");
	}

	@RequestMapping(value = "bpm/notice/deleteNoticeInstance", method = RequestMethod.GET)
	public String deleteNoticeInstance(HttpServletRequest request, @RequestParam("noticeInsId") String noticeInsId) {
		Locale locale = request.getLocale();
		try {
			rtProcessService.deleteProcessInstance(noticeInsId, null, "Notice deleted");
			saveMessage(request, getText("instance.delete.success", locale));
			log.info("Notice Instance is deleted Successfully");
		} catch (BpmException e) {
			saveError(request, getText("instance.delete.error", e.getMessage(), locale));
			log.error("Error while delete notice Instances  : " + e.getMessage(), e);
		}
		return "redirect:myInstances";
	}

	@Autowired
	public void setRtProcessService(ProcessService rtProcessService) {
		this.rtProcessService = rtProcessService;
	}

	@Autowired
	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}

	@Autowired
	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	@Autowired
	public void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
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
	public void setOperatingFunctionService(OperatingFunctionService operatingFunctionService) {
		this.operatingFunctionService = operatingFunctionService;
	}

}
