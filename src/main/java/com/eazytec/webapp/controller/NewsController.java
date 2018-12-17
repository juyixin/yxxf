package com.eazytec.webapp.controller;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eazytec.Constants;
import com.eazytec.bpm.common.util.FormDefinitionUtil;
import com.eazytec.bpm.runtime.process.service.ProcessService;
import com.eazytec.bpm.runtime.task.service.OperatingFunctionService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.FileUtil;
import com.eazytec.common.util.TemplateUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceException;
import com.eazytec.exceptions.DataSourceValidationException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.MetaForm;
import com.eazytec.model.Role;
import com.eazytec.service.NewsService;
import com.eazytec.service.NoticeService;
import com.eazytec.service.UserManager;

/**
 * @author Rajmohan To process with internal NEWS
 */
@Controller
public class NewsController extends BaseFormController {
	/**
	 * Log variable for all child classes. Uses LogFactory.getLog(getClass())
	 * from Commons Logging
	 */
	protected final Log log = LogFactory.getLog(getClass());
	public VelocityEngine velocityEngine;
	public NewsService newsService;
	public ProcessService rtProcessService;
	private RuntimeService runtimeService;
	private NoticeService noticeService;
	private OperatingFunctionService operatingFunctionService;
	private UserManager userManager;

	/**
	 * To create News
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/news/createNews", method = RequestMethod.GET)
	public ModelAndView createNews(ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		try {
			Map<String, Object> context = new HashMap<String, Object>();
			context.put("isEdit", false);
			context.put("isNotice", "false");
			ProcessDefinition newsDefinition = newsService.getNewsDefinition();
			String newsScript = "";
			String newsId = newsDefinition.getId();
			StartFormData startForm = rtProcessService.getStartForm(newsId);
			if (startForm != null && ((startForm.getFormProperties() != null && startForm.getFormProperties().size() > 0) || startForm.getFormKey() != null)) {
				newsScript = designStartProcessScript(newsDefinition, context);
				model.addAttribute("hasForm", "true");
			} else {
				rtProcessService.startProcessInstanceById(newsId);
				saveMessage(request, getText("news.created", locale));
				newsScript = "";
				model.addAttribute("hasForm", "false");
			}
			model.addAttribute("script", newsScript);
		} catch (BpmException e) {
			saveError(request, getText("news.create.failed", e.getMessage(), locale));
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			saveError(request, getText("news.create.exception", locale));
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("startProcess", model);
	}

	@RequestMapping(value = "bpm/news/submitNewsForm", method = RequestMethod.POST)
	public ModelAndView submitNewsForm(HttpServletRequest request, ModelMap model, final RedirectAttributes redirectAttributes) {
		Locale locale = request.getLocale();
		Map<String, String> endEventScript = new HashMap<String, String>();
		try {
			Map<String, String> filePathsMap = null;
			Map<String, String> rtFormValues = FormDefinitionUtil.getFormParams(request.getParameterMap());
			Map<String, String[]> rtSubFormValues = FormDefinitionUtil.getSubFormParams(request.getParameterMap());
			String processDefinitionId = rtFormValues.get("processDefinitionId");
			rtFormValues.remove("processDefinitionId");
			String uploadDir = getServletContext().getRealPath("/resources") + "/" + request.getRemoteUser() + "/";
			Map<String, byte[]> files = FileUtil.getFileUploadMap(request);

			ProcessInstance newsInstance = newsService.submitStartForm(processDefinitionId, rtFormValues, rtSubFormValues, files, filePathsMap);
			if (newsInstance != null) {
				if (newsInstance.getId() != null) {

					if (files != null) {
						filePathsMap = FileUtil.uploadFileForTask(request, uploadDir, null, newsInstance.getId(), uploadDir, runtimeService, FileUtil.PROCESS_TYPE_START);
					}
					endEventScript.put("processInstanceId", newsInstance.getId());
					redirectAttributes.addFlashAttribute("eventScriptDetails", endEventScript);
				}
				log.info("News : " + rtFormValues.get("title") + " created successfully");
				if (rtFormValues.containsKey("insideapp")) {
					operatingFunctionService.sendNotification(userManager.getUsers().toString(), "News Created", Constants.NOTIFICATION_INTERNALMESSAGE_TYPE, Constants.NOTIFICATION_MESSAGE);
				}
				saveMessage(request, getText("news.created", locale));
			} else {
				log.info("Could not instantiate news: " + rtFormValues.get("title"));
				saveError(request, getText("news.create.failed", locale));
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
			saveError(request, "Failed to create news, your input values do not integrate with Database!");
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			saveError(request, "Failed to create news!");
			model.addAttribute("message", "Failed to create news" + e.getMessage());
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("redirect:newsList");
	}

	/**
	 * To News List
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/news/newsList", method = RequestMethod.GET)
	public ModelAndView newsList(ModelMap model, HttpServletRequest request) {
		return new ModelAndView("news/newsList", model);
	}

	/**
	 * To News List
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/news/getNewsList", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> getNewsList(ModelMap model, HttpServletRequest request) {
		List<Map<String, Object>> newsItems = new ArrayList<Map<String, Object>>();
		try {
			newsItems = newsService.getNewsItemForHomePage();
			// log.info("News List Retrived Successfully");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return newsItems;
	}

	/**
	 * To show News
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/news/showDetailNews", method = RequestMethod.GET)
	public ModelAndView showDetailNews(@RequestParam("newsInstanceId") String newsInstanceId, ModelMap model, HttpServletRequest request) {

		Map<String, Object> context = new HashMap<String, Object>();
		String script = null;
		try {
			Map<String, Object> properties = CommonUtil.getStringRepresentations(newsService.getNewsItemValues(newsInstanceId));
			String createdUser = (String) properties.get("CREATEDBY");
			// show news in read-only
			Set<Role> userRoles = CommonUtil.getLoggedInUser().getRoles();
			if (!userRoles.isEmpty() && userRoles != null) {
				if (!userRoles.toString().contains("ROLE_ADMIN") && !createdUser.equalsIgnoreCase(CommonUtil.getLoggedInUserId())) {
					context.put("isFormReadOnly", true);
				}
			}
			ProcessDefinition newsDefinition = newsService.getNewsDefinition();
			MetaForm form = newsService.getNewsForm(newsInstanceId);
			context.put("html", form.getHtmlSource());
			context.put("formId", form.getFormName());
			context.put("formAction", "bpm/news/updateNewsItem");
			context.put("properties", properties);
			context.put("pastValuesJson", CommonUtil.convertMapToJson(properties));
			context.put("instanceId", newsInstanceId);
			context.put("processDefinition", newsDefinition);
			context.put("isEdit", true);

			script = designEditProcessScript(newsDefinition, newsInstanceId, context);
			model.addAttribute("script", script);
			// log.info("Editing News : "+properties.get("title"));
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

	@RequestMapping(value = "bpm/news/showNewsTemplate", method = RequestMethod.GET)
	public ModelAndView showNewsTemplate(@RequestParam("newsInstanceId") String newsInstanceId, ModelMap model, HttpServletRequest request) {
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

	@RequestMapping(value = "bpm/news/updateNewsItem", method = RequestMethod.POST)
	public ModelAndView updateNewsItem(HttpServletRequest request, ModelMap model, final RedirectAttributes redirectAttributes) {
		Locale locale = request.getLocale();
		try {

			Map<String, String> rtFormValues = FormDefinitionUtil.getFormParams(request.getParameterMap());
			Map<String, String[]> rtSubFormValues = FormDefinitionUtil.getSubFormParams(request.getParameterMap());
			rtFormValues.remove("processDefinitionId");
			String uploadDir = getServletContext().getRealPath("/resources") + "/" + request.getRemoteUser() + "/";
			Map<String, byte[]> files = FileUtil.getFileUploadMap(request);
			Map<String, String> filePathsMap = null;
			String insId = rtFormValues.get("processInstanceId");
			newsService.updateNewsItem(rtFormValues, rtSubFormValues, files, filePathsMap);
			if (files != null && !files.isEmpty()) {
				filePathsMap = FileUtil.uploadFileForTask(request, uploadDir, null, insId, uploadDir, runtimeService, FileUtil.PROCESS_TYPE_START);
			}
			operatingFunctionService.attachmentOperation(rtFormValues, null);
			log.info("Updated news item: " + rtFormValues.get("title"));
			saveMessage(request, getText("news.updated", locale));
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
			saveError(request, "Failed to update News!");
			model.addAttribute("message", "Failed to update News" + e.getMessage());
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("redirect:newsList");
	}

	/**
	 * <p>
	 * Decides and designs the initial screen template when process is started,
	 * if a start form is associated that form needs to be rendered, else
	 * process instantiated directly and corresponding screen shown
	 * </p>
	 * 
	 * @param permissionVal
	 *            TODO
	 * @param processId
	 *            for the process starting
	 * @return the template script as string, to be rendered
	 */
	private String designStartProcessScript(ProcessDefinition newsDefinition, Map<String, Object> permissionVal) throws BpmException {
		return TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showStartHtmlForm.vm", newsService.designStartProcessScript(newsDefinition, velocityEngine, permissionVal));
	}

	private String designEditProcessScript(ProcessDefinition noticeDefinition, String insId, Map<String, Object> permissionVal) throws BpmException {
		return TemplateUtil.generateScriptForTemplate(velocityEngine, "com/eazytec/bpm/form/templates/showStartHtmlForm.vm", noticeService.designEditNoticeScript(noticeDefinition, velocityEngine, insId, permissionVal, "bpm/news/updateNewsItem"));
	}

	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	@Autowired
	public void setNewsService(NewsService newsService) {
		this.newsService = newsService;
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
	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}

	@Autowired
	public void setOperatingFunctionService(OperatingFunctionService operatingFunctionService) {
		this.operatingFunctionService = operatingFunctionService;
	}

	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
}
