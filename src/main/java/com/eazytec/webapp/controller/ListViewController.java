package com.eazytec.webapp.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.module.service.ModuleService;
import com.eazytec.bpm.common.LabelValueBean;
import com.eazytec.bpm.common.QueryParser;
import com.eazytec.bpm.common.util.ProcessDefinitionUtil;
import com.eazytec.bpm.runtime.listView.service.ListViewService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LabelValue;
import com.eazytec.model.ListView;
import com.eazytec.model.ListViewButtons;
import com.eazytec.model.ListViewColumns;
import com.eazytec.model.ListViewGroupSetting;
import com.eazytec.model.Module;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.util.DateUtil;

/**
 * 
 * @author Mahesh
 * @author Rajmohan List View creation of data to show in grid view
 */

@Controller
public class ListViewController extends BaseFormController {

	/**
	 * Log variable for all child classes. Uses LogFactory.getLog(getClass())
	 * from Commons Logging
	 */
	protected final Log log = LogFactory.getLog(getClass());
	public VelocityEngine velocityEngine;
	private ListViewService listViewService;
	private ModuleService moduleService;

	@Autowired
	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	@Autowired
	public void setListViewService(ListViewService listViewService) {
		this.listViewService = listViewService;
	}

	@RequestMapping(value = "/bpm/listView/showListViewGrid", method = RequestMethod.GET)
	public @ResponseBody ModelAndView showListViewGrid(@RequestParam("listViewName") String listViewName, @RequestParam("title") String title, @RequestParam("container") String container, @RequestParam("listViewParams") String listViewParams, @RequestParam(value = "needHeader", required = false) String needPageHeader, HttpServletResponse response, ModelMap model, HttpServletRequest request) throws Exception {
		String script = null;
		Locale locale = request.getLocale();
		boolean needHeader = false;
		title = new String(title.getBytes("UTF-8"), "UTF-8");

		try {
			List<Map<String, Object>> listViewParamsList = CommonUtil.convertJsonToList(listViewParams);
			Map<String, Object> namedParameter = new HashMap<String, Object>();
			if (!listViewParamsList.isEmpty()) {
				for (Map<String, Object> listViewParamsMap : listViewParamsList) {
					namedParameter.putAll(listViewParamsMap);
				}
			}

			String height = request.getParameter("height");
			String width = request.getParameter("width");
			if (height != null && width != null) {
				namedParameter.put("fromDivHeight", height);
				namedParameter.put("fromDivWidth", width);
			}
			if (null == needPageHeader || needPageHeader == "true") {
				needHeader = true;
			}
			
			ListView listView = listViewService.getListViewByName(listViewName);

			if (listView.getViewTitle() != null && !listView.getViewTitle().isEmpty()) {
				title = listView.getViewTitle();
			}

			model.addAttribute("gridTitle", title);
			
			script = listViewService.showListViewGrid(listViewName.toUpperCase(), title, container, namedParameter, false, null, needHeader);

			if (script == null || script.isEmpty()) {
				script = getText("listview.column.not.exist", locale);
			}
			model.addAttribute("script", script);
			model.addAttribute("success", true);
			
			// log.info("List View Grid generated Successfully");
		} catch (BpmException e) {
			System.out.println("=====BpmException======");
			e.printStackTrace();
			log.error(e.getMessage(), e);
			model.addAttribute("success", false);
			saveError(request, getText("list.view.error", e.getMessage(), locale));
			model.addAttribute("script", "Problem loading grid. " + e.getMessage());
		} catch (Exception e) {
			System.out.println("=====Exception======");
			e.printStackTrace();
			model.addAttribute("success", false);
			model.addAttribute("script", "Problem loading grid. " + e.getMessage());
			log.error(e.getMessage(), e);

		}
		if ((listViewName.equals("PROCESS_LIST") || listViewName.equals("PROCESS_LIST_VERSION") || listViewName.equals("PROCESS_LIST_MY_INSTANCES") || listViewName.equals("AGENCY_LIST")) && needHeader) {
			model.addAttribute("jsonTree", ProcessDefinitionUtil.getJsonTreeForProcessList(request));
			return new ModelAndView("listview/showListViewProcessGrid", model);
		} else {
			return new ModelAndView("listview/showListViewGrid", model);
		}
		// return script;
	}

	/**
	 * Show list view page for create view with mapping grid column title
	 * 
	 * @param listView
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/bpm/listView/showListView", method = RequestMethod.GET)
	public ModelAndView showListView(@ModelAttribute("listView") ListView listView, ModelMap model, @RequestParam(value = "isFromModule", required = false) String isFromModule, HttpServletRequest request) {
		model.addAttribute("listView", listView);
		model.addAttribute("isListViewTemplate", true);
		boolean isAdmin = false;
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Set<Role> roles = user.getRoles();

		for (Role role : roles) {
			if (role.getId().equals("ROLE_ADMIN")) {
				isAdmin = true;
				break;
			}
		}
		model.addAttribute("isEdit", isAdmin);
		model.addAttribute("isFromModule", isFromModule);
		return new ModelAndView("listview/showListView", model);
	}

	@RequestMapping(value = "/bpm/listView/showDefaultListView", method = RequestMethod.GET)
	public ModelAndView showDefaultListView(@ModelAttribute("listView") ListView listView, ModelMap model, HttpServletRequest request) {
		model.addAttribute("listView", listView);
		return new ModelAndView("listview/defaultListView", model);
	}

	/**
	 * Get list view columns property like column title, data fields, width,
	 * etc...
	 * 
	 * @param listViewId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/bpm/listView/columnsProperty", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> columnsProperty(@ModelAttribute("listViewId") String listViewId, ModelMap model, HttpServletRequest request) {
		List<Map<String, Object>> mapViewColumnProperty = null;
		try {
			List<ListViewColumns> listViewColumnProperty = listViewService.getColumnsPropertyByListViewId(listViewId);
			String[] fieldNames = { "id", "columnTitle", "dataFields", "width", "otherName", "orderBy", "orderNo", "replaceWords", "isSort", "columnType", "isAdvancedSearch", "isSimpleSearch", "textAlign", "isHidden", "onRenderEvent", "onRenderEventName", "comment", "dataFieldType", "dataDictionary", "isRange" };
			mapViewColumnProperty = CommonUtil.getMapListFromObjectListByFieldNames(listViewColumnProperty, fieldNames, "");
		} catch (BpmException e) {
			log.error(e.getMessage(), e);
		} catch (EazyBpmException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return mapViewColumnProperty;
	}

	/**
	 * Save the List View in meta table.
	 * 
	 * @param model
	 * @return ListView
	 * @throws Exception
	 */
	@RequestMapping(value = "/bpm/listView/saveListView", method = RequestMethod.POST)
	public ModelAndView saveListView(@ModelAttribute("listView") ListView listView, @RequestParam("isListViewTemplate") boolean isListViewTemplate, ModelMap model, HttpServletRequest request, BindingResult errors) throws Exception {
		Locale locale = request.getLocale();
		try {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (user != null) {
				listView.setCreatedBy(user.getUsername());
			} else {
				listView.setCreatedBy(Constants.EMPTY_STRING);
			}
			String modeType;
			String listViewId = listView.getId();
			if (isListViewTemplate == true) {
				listView.setId("");
			}

			String moduleId = request.getParameter("module_Id");
			String moduleName = request.getParameter("_moduleName");
			model.addAttribute("moduleId", moduleId);
			model.addAttribute("moduleName", moduleName);
			model.addAttribute("isEdit", true);

			if (listView.getId() != null && listView.getId() != "" && !String.valueOf(listView.getId()).isEmpty()) {
				modeType = "update";
				model.addAttribute("removeListViewId", listView.getId());
			} else {
				modeType = "create";
				listView.setCreatedTime(new Date());
			}
			if (validator != null) {
				validator.validate(listView, errors);
				if (errors.hasErrors()) {
					if (isListViewTemplate == true) {
						listView.setId(listViewId);
					}
					model.addAttribute("isListViewTemplate", isListViewTemplate);
					return new ModelAndView("listview/showListView", model);
				}
			}
			// listView.getModule().setName(moduleName);
			if (listView.getId().equals("")) {
				if (listViewService.checkListViewName(listView.getViewName())) {
					listView.setId(listViewId);
					model.addAttribute("isListViewTemplate", isListViewTemplate);
					saveError(request, getText("listview.name.duplicate", (String) listView.getViewName(), locale));
					return new ModelAndView("listview/showListView", model);
				}
			}

			model.addAttribute("isListViewTemplate", isListViewTemplate);
			if (isListViewTemplate == true) {
				model.addAttribute("listViewTemplateId", listViewId);
			}

			listView = listViewService.saveListView(listView, modeType, moduleId);
			Module moduleObj = moduleService.getModule(moduleId);
			model.addAttribute("listView", listView);
			model.addAttribute("isSystemModule", moduleObj.getIsSystemModule());

			if (listViewId != null && !listViewId.isEmpty() && modeType.equalsIgnoreCase("update")) {
				model.addAttribute("isUpdate", true);
				model.addAttribute("newListViewModuleId", moduleId);
				model.addAttribute("showNewListView", "true");
				model.addAttribute("isListViewTemplate", isListViewTemplate);
				model.addAttribute("listView", listView);
				saveMessage(request, getText("listview.altered", locale));
				log.info("List View '" + listView.getViewName() + "' altered successfully");
			} else {

				saveMessage(request, getText("listview.created", locale));
				log.info("List View '" + listView.getViewName() + "' created successfully");
				return showListViewDesignEditScreen(listView, isListViewTemplate, moduleId, model);
			}
		} catch (Exception e) {
			log.error("Error while saving a List View ", e);
			saveError(request, getText("listview.error", e.getMessage(), locale));
		}
		return new ModelAndView("listview/showListView", model);
	}

	/**
	 * Redirect the create screen to edit screen of Listview which is created
	 * 
	 * @param listView
	 * @param isListViewTemplate
	 * @param model
	 * @return
	 */
	private ModelAndView showListViewDesignEditScreen(ListView listView, boolean isListViewTemplate, String moduleId, ModelMap model) {
		try {
			model.addAttribute("newListViewModuleId", moduleId);
			model.addAttribute("showNewListView", "true");
			model.addAttribute("listView", listView);
			model.addAttribute("isListViewTemplate", isListViewTemplate);
			// log.info("List View rendered Successfully for editing");
		} catch (Exception e) {
			log.error("Error while rendering list view for editing", e);
		}
		return new ModelAndView("listview/showListView", model);
	}

	/**
	 * Check whether ListView name already exist or not
	 * 
	 * @param listViewName
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/listView/checkListViewExist", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkListViewExist(@RequestParam("listViewName") String listViewName, ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			if (listViewService.checkListViewName(listViewName)) {
				message.put("error", getText("listview.name.duplicate", listViewName, locale));
				// log.info("This List View Name ({0}) already exists. Please
				// try a different List view name.");
			} else {
				message.put("success", "success");
			}
		} catch (Exception e) {
			message.put("error", getText("listview.error", locale));
		}
		return message;
	}

	/**
	 * Save the List view columns
	 * 
	 * @param model
	 * @return ListView
	 * @throws Exception
	 */
	@RequestMapping(value = "/bpm/listView/saveListViewColumns", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveListViewColumns(@ModelAttribute("listViewId") String listViewId, @ModelAttribute("listViewColumns") String listViewColumns, @ModelAttribute("moduleId") String moduleId, ModelMap model, HttpServletRequest request) throws Exception {
		Locale locale = request.getLocale();
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			boolean isListViewColumnUpdate = false;
			ListView listView = null;
			List<Map<String, Object>> listViewMetaColumns = CommonUtil.convertJsonToList(listViewColumns);
			// List<String>
			// deleteListViewDetails=CommonUtil.convertJsonToListStrings(deleteListViewColumns);@RequestParam("listViewId")
			// String listViewId,@RequestParam("listViewColumns") String
			// listViewColumns,

			if (listViewMetaColumns != null && !listViewMetaColumns.isEmpty()) {
				ListView listViewObj = listViewService.getListViewById(listViewId);
				Set<ListViewColumns> columsnSet = listViewObj.getListViewColumns();
				if (!columsnSet.isEmpty()) {
					isListViewColumnUpdate = true;
				}
				// listViewColumnsObject.setOrderNo(listViewService.generateOrderNo());

				listView = listViewService.saveListViewColumnDetails(listViewObj, listViewMetaColumns, isListViewColumnUpdate, moduleId);
			}

			/*
			 * if(deleteListViewDetails!=null &&
			 * !deleteListViewDetails.isEmpty()){
			 * listViewService.deleteListViewByIds
			 * (deleteListViewDetails,"ListViewColumns"); }
			 */
			if (isListViewColumnUpdate) {
				message.put("successMessage", getText("listview.columns.updated", (String) listView.getViewName(), locale));
				log.info(getText("listview.columns.updated", (String) listView.getViewName(), locale));
			} else {
				message.put("successMessage", getText("listview.columns.created", (String) listView.getViewName(), locale));
				log.info(getText("listview.columns.created", (String) listView.getViewName(), locale));
				// saveMessage(request,
				// getText("listview.columns.created",locale));
			}
			message.put("listViewId", listView.getId());
			model.addAttribute("listView", listView);
		} catch (Exception e) {
			e.printStackTrace();
			message.put("errorMessage", getText("listview.columns.error", locale));
		}

		return message;
	}

	/**
	 * Get list view group setting property like group name, group field name,
	 * etc...
	 * 
	 * @param listViewId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/bpm/listView/groupSettingProperty", method = RequestMethod.GET)

	public @ResponseBody List<Map<String, Object>> groupSettingProperty(@ModelAttribute("listViewId") String listViewId, ModelMap model, HttpServletRequest request) {
		List<Map<String, Object>> mapViewGroupSettingProperty = null;
		try {
			List<ListViewGroupSetting> listViewGroupSettingProperty = listViewService.getGroupSettingPropertyByListViewId(listViewId);
			String[] fieldNames = { "id", "groupName", "groupFieldsName", "parentGroup", "sortType", "comment", "groupType", "orderBy" };
			mapViewGroupSettingProperty = CommonUtil.getMapListFromObjectListByFieldNames(listViewGroupSettingProperty, fieldNames, "");
		} catch (BpmException e) {
			log.error(e.getMessage(), e);
		} catch (EazyBpmException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return mapViewGroupSettingProperty;
	}

	/**
	 * Save the List view group setting
	 * 
	 * @param model
	 * @return ListView
	 * @throws Exception
	 */
	@RequestMapping(value = "/bpm/listView/saveListViewGroupSetting", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveListViewGroupSetting(@RequestParam("listViewId") String listViewId, @RequestParam("createGroupSetting") String createGroupSetting, @ModelAttribute("moduleId") String moduleId, ModelMap model, HttpServletRequest request) throws Exception {
		Locale locale = request.getLocale();
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			boolean isListViewGroupSettingUpdate = false;
			ListView listView = null;
			List<Map<String, Object>> listViewMetaColumns = null;

			if (createGroupSetting != null && !createGroupSetting.equals("null")) {
				createGroupSetting = URLDecoder.decode(createGroupSetting, "UTF-8");
				listViewMetaColumns = CommonUtil.convertJsonToList(createGroupSetting);
			}

			// List<String>
			// deleteListViewDetails=CommonUtil.convertJsonToListStrings(deleteGroupSetting);

			// if(listViewMetaColumns!=null && !listViewMetaColumns.isEmpty()){
			ListView listViewObj = listViewService.getListViewById(listViewId);
			Set<ListViewGroupSetting> groupSet = listViewObj.getListViewGroupSetting();
			if (!groupSet.isEmpty()) {
				isListViewGroupSettingUpdate = true;
			}

			listView = listViewService.saveListViewGroupSettingDetails(listViewObj, listViewMetaColumns, isListViewGroupSettingUpdate, moduleId);
			// }

			/*
			 * if(deleteListViewDetails!=null &&
			 * !deleteListViewDetails.isEmpty()){
			 * listViewService.deleteListViewByIds
			 * (deleteListViewDetails,"ListViewGroupSetting"); }
			 */
			if (isListViewGroupSettingUpdate) {
				message.put("successMessage", getText("listview.groupsetting.updated", (String) listViewObj.getViewName(), locale));
				log.info(getText("listview.groupsetting.updated", (String) listViewObj.getViewName(), locale));
			} else {
				message.put("successMessage", getText("listview.groupsetting.created", (String) listViewObj.getViewName(), locale));
				log.info(getText("listview.groupsetting.created", (String) listViewObj.getViewName(), locale));
			}
			message.put("listViewId", listView.getId());
			model.addAttribute("listView", listView);
		} catch (Exception e) {
			log.error(getText("listview.groupsetting.error", locale), e);
			message.put("errorMessage", getText("listview.groupsetting.error", locale));
		}

		return message;
	}

	/**
	 * To Construct a list view grid query
	 * 
	 * @param List
	 *            View Id
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/bpm/listView/constructListViewQuery", method = RequestMethod.POST)
	public @ResponseBody String constructListViewQuery(@RequestParam("listViewId") String listViewId, HttpServletRequest request) throws Exception {
		Locale locale = request.getLocale();
		try {
			ListView listView = listViewService.getListViewById(listViewId);
			// log.info("List View construct query is done Successfully");
			return listViewService.constructListViewQuery(listView);
		} catch (Exception e) {
			log.error(getText("listview.construct.query.error", locale), e);
			saveError(request, getText("listview.construct.query.error", locale));
		}
		return null;
	}

	/**
	 * To Construct a list view grid query
	 * 
	 * @param List
	 *            View Id
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/bpm/listView/checkListViewQuery", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkListViewQuery(@RequestParam("listViewId") String listViewId, HttpServletRequest request) throws Exception {
		Locale locale = request.getLocale();
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			ListView listView = listViewService.getListViewById(listViewId);
			if (listViewService.checkListViewQuery(listView)) {
				message.put("success", getText("listview.query.checked", locale));
			}
			// log.info("check ListView Query is done Successfully");
		} catch (Exception e) {
			log.error(getText("listview.query.checked.error", locale), e);
			message.put("error", getText("listview.query.checked.error", locale));
		}
		return message;
	}

	/**
	 * Validate the list view grid query
	 * 
	 * @param List
	 *            View Id
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/bpm/listView/validateListViewQuery", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> validateListViewQuery(HttpServletRequest request) throws Exception {
		Locale locale = request.getLocale();
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			String constructedQuery = request.getParameter("constructedQuery");

			String loggedInUserRole = "" + CommonUtil.getLoggedInUser().getRoles() + "";
			List<String> rolesList = new ArrayList<String>(Arrays.asList(loggedInUserRole.replace("[", "").replace("]", "").replace(" ", "").split(",")));
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (rolesList.contains("ROLE_ADMIN") && constructedQuery.indexOf(":loggedInUserReport") > -1) {
				constructedQuery = constructedQuery.split("WHERE")[0];
			} else {

				if (constructedQuery.indexOf(":loggedInUser") > -1) {
					String userName = "('" + user.getUsername() + "')";
					constructedQuery = constructedQuery.replaceAll(":loggedInUser", userName);
				}

				if (constructedQuery.indexOf(":userRole") > -1) {
					String userRoles = "'" + user.getRoles() + "'";
					String userRoleArray[] = {};
					if (userRoles.contains("[")) {
						userRoles = userRoles.replace("'[", "");
						userRoles = userRoles.replace("]'", "");
					}
					if (userRoles.contains(" ")) {
						userRoles = userRoles.replace(" ", "");
					}
					if (userRoles.contains(",")) {
						userRoleArray = userRoles.split(",");
					}
					userRoles = "(";
					for (int i = 0; i < userRoleArray.length; i++) {
						if (i == userRoleArray.length - 1) {
							userRoles = userRoles + "'" + userRoleArray[i] + "'" + ")";
						} else {
							userRoles = userRoles + "'" + userRoleArray[i] + "'" + ",";
						}
					}
					constructedQuery = constructedQuery.replaceAll(":userRole", userRoles);

				}
				if (constructedQuery.indexOf(":userGroup") > -1) {
					String userGroups = "'" + user.getGroups() + "'";
					String[] userGroupArray = {};
					if (userGroups.contains("[")) {
						userGroups = userGroups.replace("'[", "");
						userGroups = userGroups.replace("]'", "");
					}
					if (userGroups.contains(" ")) {
						userGroups = userGroups.replace(" ", "");
					}

					if (userGroups.contains(",")) {
						userGroupArray = userGroups.split(",");
						userGroups = "(";
						for (int i = 0; i < userGroupArray.length; i++) {
							if (i == userGroupArray.length - 1) {
								userGroups = userGroups + "'" + userGroupArray[i] + "'" + ")";
							} else {
								userGroups = userGroups + "'" + userGroupArray[i] + "'" + ",";
							}
						}
					} else {
						userGroups = "('" + userGroups + "')";
					}

					constructedQuery = constructedQuery.replaceAll(":userGroup", userGroups);

				}
				if (constructedQuery.indexOf(":userDepartment") > -1) {
					String userDepartment = "('" + user.getDepartment() + "')";
					constructedQuery = constructedQuery.replaceAll(":userDepartment", userDepartment);
				}

			}

			if (listViewService.checkListViewQuery(constructedQuery)) {
				message.put("success", getText("listview.query.checked", locale));
			}
			// log.info("validate ListView Query is done Successfully");
		} catch (Exception e) {
			log.error("Error while checking the query ", e);
			message.put("error", getText("listview.query.checked.error", locale));
		}
		return message;
	}

	/**
	 * Validate the list view grid query
	 * 
	 * @param List
	 *            View Id
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/bpm/listView/getAllSelectColumnsWithoutAlice", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, String>> getAllSelectColumnsWithoutAlice(@RequestParam("constructedQuery") String constructedQuery, HttpServletRequest request) throws BpmException {
		List<Map<String, String>> columnDetailsList = new ArrayList<Map<String, String>>();
		try {

			QueryParser queryParser = new QueryParser(constructedQuery);
			List<LabelValueBean> columnNameBean = queryParser.getAllSelectColumnsWithoutAlice();

			for (LabelValueBean columnName : columnNameBean) {
				Map<String, String> columnDetail = new HashMap<String, String>();
				columnDetail.put("columnName", columnName.getLabel());
				columnDetail.put("columnId", columnName.getLabel());
				columnDetailsList.add(columnDetail);
			}
			return columnDetailsList;
		} catch (Exception e) {
			log.error("Error while getting all tables " + e);
		}
		return new ArrayList<Map<String, String>>();
	}

	/**
	 * Show advanced search page
	 * 
	 * @param listViewId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/bpm/listView/advanceSearch", method = RequestMethod.GET)
	public ModelAndView advanceSearch(@RequestParam("listViewId") String listViewId, ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			List<ListViewColumns> listViewColumnPropertyAdvancedSearch = new ArrayList<ListViewColumns>();
			List<ListViewColumns> listViewColumnProperty = listViewService.getColumnsPropertyByListViewId(listViewId);
			for (ListViewColumns listViewColumns : listViewColumnProperty) {
				if (listViewColumns.getIsAdvancedSearch() == true) {
					listViewColumnPropertyAdvancedSearch.add(listViewColumns);
				}
			}
			model.addAttribute("listViewColumns", listViewColumnPropertyAdvancedSearch);
		} catch (EazyBpmException e) {
			message.put("error", getText("listview.show.advance.search.error", locale));
			log.error(getText("listview.show.advance.search.error", locale));
		}
		return new ModelAndView("listview/showAdvanceSearch", model);
	}

	/**
	 * Get grid data for given parameters
	 * 
	 * @param listViewId
	 * @param searchParams
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/bpm/listView/reloadGridWithSearchParams", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> reloadGridWithSearchParams(@ModelAttribute("listViewId") String listViewId, @RequestParam("searchParams") String searchParams, @RequestParam("sortType") String sortType, @RequestParam("sortTypeColumn") String sortTypeColumn, @ModelAttribute("searchType") String searchType, @RequestParam("listViewParams") String listViewParams, ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		Map<String, Object> message = new HashMap<String, Object>();
		List<Map<String, Object>> mapViewColumnProperty = null;
		try {
			Map<String, Object> searchParamMap = CommonUtil.convertJsonToList(searchParams).get(0);
			List<Map<String, Object>> listViewParamsList = CommonUtil.convertJsonToList(listViewParams);
			Map<String, Object> namedParameter = new HashMap<String, Object>();
			if (!listViewParamsList.isEmpty()) {
				for (Map<String, Object> listViewParamsMap : listViewParamsList) {
					namedParameter.putAll(listViewParamsMap);
				}
			}

			ListView listView = listViewService.getListViewById(listViewId);
			mapViewColumnProperty = listViewService.getAllListViewMapDataWithSearchParams(listView, searchParamMap, sortType, sortTypeColumn, searchType, namedParameter);
			// log.info("Getting List View grid data for given parameters
			// Successfully");
		} catch (BpmException e) {
			message.put("error", getText("listview.advance.search.data.error", locale));
			log.error(e.getMessage(), e);
		} catch (EazyBpmException e) {
			message.put("error", getText("listview.advance.search.data.error", locale));
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			message.put("error", getText("listview.advance.search.data.error", locale));
			log.error(e.getMessage(), e);
		}
		return mapViewColumnProperty;
	}

	/**
	 * Get grid data for given parameters and constraints
	 * 
	 * @param listViewId
	 * @param searchParams
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/bpm/listView/reloadGridWithSearchParamsAndConstraints", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> reloadGridWithSearchParamsAndConstraints(ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		Map<String, Object> message = new HashMap<String, Object>();
		List<Map<String, Object>> mapViewColumnProperty = null;
		try {
			List<Map<String, Object>> listViewParamsList = CommonUtil.convertJsonToList(request.getParameter("listViewParams"));
			Map<String, Object> namedParameters = new HashMap<String, Object>();
			if (!listViewParamsList.isEmpty()) {
				for (Map<String, Object> listViewParamsMap : listViewParamsList) {
					namedParameters.putAll(listViewParamsMap);
				}
			}

			List<Map<String, Object>> searchParamListOfMap = CommonUtil.convertJsonToList(request.getParameter("searchParams"));
			ListView listView = listViewService.getListViewById(request.getParameter("listViewId"));
			mapViewColumnProperty = listViewService.getDataWithSearchParamsAndConstraints(listView, searchParamListOfMap, request.getParameter("sortType"), request.getParameter("sortTypeColumn"), request.getParameter("searchType"), namedParameters);

		} catch (BpmException e) {
			message.put("error", getText("listview.search.data.error", locale));
			log.error(e.getMessage(), e);
		} catch (EazyBpmException e) {
			message.put("error", getText("listview.search.data.error", locale));
			log.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (Exception e) {
			message.put("error", getText("listview.search.data.error", locale));
			log.error(e.getMessage(), e);
		}
		return mapViewColumnProperty;
	}

	/**
	 * Get grid data for given parameters
	 * 
	 * @param listViewId
	 * @param searchParams
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/bpm/listView/reloadListViewGridModuleGroups", method = RequestMethod.GET)
	public @ResponseBody ModelAndView reloadListViewGridModuleGroups(@ModelAttribute("listViewId") String listViewId, @RequestParam("searchParams") String searchParams, @RequestParam("sortType") String sortType, @RequestParam("sortTypeColumn") String sortTypeColumn, @ModelAttribute("searchType") String searchType, ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		Map<String, Object> message = new HashMap<String, Object>();
		List<Map<String, Object>> mapViewColumnProperty = null;
		String script = "";
		try {
			Map<String, Object> searchParamMap = CommonUtil.convertJsonToList(searchParams).get(0);
			ListView listView = listViewService.getListViewById(listViewId);
			List<Map<String, Object>> listViewParamsList = CommonUtil.convertJsonToList(request.getParameter("listViewParams"));
			Map<String, Object> namedParameters = new HashMap<String, Object>();
			if (!listViewParamsList.isEmpty()) {
				for (Map<String, Object> listViewParamsMap : listViewParamsList) {
					namedParameters.putAll(listViewParamsMap);
				}
			}
			mapViewColumnProperty = listViewService.getAllListViewMapDataWithSearchParams(listView, searchParamMap, sortType, sortTypeColumn, searchType, namedParameters);

			script = listViewService.showListViewGrid(listView.getViewName(), "List", "rightPanel", null, true, mapViewColumnProperty, true);
			model.addAttribute("script", script);
			model.addAttribute("success", true);
			// log.info("List View Grid reload module Groups Successfully");
		} catch (BpmException e) {
			model.addAttribute("success", false);
			message.put("error", getText("listview.search.data.error", locale));
			log.error(e.getMessage(), e);
		} catch (EazyBpmException e) {
			model.addAttribute("success", false);
			message.put("error", getText("listview.search.data.error", locale));
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			model.addAttribute("success", false);
			message.put("error", getText("listview.search.data.error", locale));
			log.error(e.getMessage(), e);
		}
		// return mapViewColumnProperty;
		return new ModelAndView("listview/showListViewGrid", model);
	}

	@RequestMapping(value = "/bpm/listView/reloadGridWithSimpleSearch", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> reloadGridWithSimpleSearch(@ModelAttribute("listViewId") String listViewId, @RequestParam("searchValue") String searchValue, @RequestParam("listViewParams") String listViewParams, ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		Map<String, Object> message = new HashMap<String, Object>();
		List<Map<String, Object>> mapViewColumnProperty = null;
		try {
			List<Map<String, Object>> listViewParamsList = CommonUtil.convertJsonToList(listViewParams);
			Map<String, Object> namedParameters = new HashMap<String, Object>();
			if (!listViewParamsList.isEmpty()) {
				for (Map<String, Object> listViewParamsMap : listViewParamsList) {
					namedParameters.putAll(listViewParamsMap);
				}
			}
			// System.out.println("========"+searchValue+"========="+URLDecoder.decode(searchValue,
			// "ISO8859_1"));
			ListView listView = listViewService.getListViewById(listViewId);
			mapViewColumnProperty = listViewService.getAllListViewMapDataWithSearchValue(listView, searchValue, namedParameters);
		} catch (BpmException e) {
			message.put("error", getText("listview.search.data.error", locale));
			log.error(e.getMessage(), e);
		} catch (EazyBpmException e) {
			message.put("error", getText("listview.search.data.error", locale));
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			message.put("error", getText("listview.search.data.error", locale));
			log.error(e.getMessage(), e);
		}
		return mapViewColumnProperty;
	}

	/**
	 * Check the list view name already exists or not
	 * 
	 * @param List
	 *            View Id
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/bpm/listView/checkListViewName", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> checkListViewName(@RequestParam("listViewName") String listViewName, HttpServletRequest request) throws Exception {
		Locale locale = request.getLocale();
		try {
			Map<String, String> isListViewName = new HashMap<String, String>();
			if (listViewService.checkListViewName(listViewName)) {
				isListViewName.put("isListViewName", "true");
			} else {
				isListViewName.put("isListViewName", "false");
			}
			// log.info("List View check List View Name Successfully");
			return isListViewName;
		} catch (Exception e) {
			log.error(getText("listView.error.checkQuery", locale), e);
		}
		return null;
	}

	/**
	 * Save the List view columns
	 * 
	 * @param model
	 * @return ListView
	 * @throws Exception
	 */
	@RequestMapping(value = "/bpm/listView/saveListViewButtons", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveListViewButtons(@RequestParam("listViewId") String listViewId, @RequestParam("listViewButtons") String listViewButtons, @ModelAttribute("moduleId") String moduleId, ModelMap model, HttpServletRequest request) throws Exception {
		Locale locale = request.getLocale();
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			boolean isListViewButtonUpdate = false;
			ListView listView = null;
			List<Map<String, Object>> listViewMetaButtons = null;
			if (listViewButtons != null && !listViewButtons.equals("null")) {
				listViewButtons = URLDecoder.decode(listViewButtons, "UTF-8");
				listViewMetaButtons = CommonUtil.convertJsonToList(listViewButtons);
			}

			// List<String>
			// deleteViewButtons=CommonUtil.convertJsonToListStrings(deleteListViewButtons);

			// if(listViewMetaButtons!=null && !listViewMetaButtons.isEmpty()){
			ListView listViewObj = listViewService.getListViewById(listViewId);
			Set<ListViewButtons> buttonsSet = listViewObj.getListViewButtons();

			if (!buttonsSet.isEmpty()) {
				isListViewButtonUpdate = true;
			}

			listView = listViewService.saveListViewButtonDetails(listViewObj, listViewMetaButtons, isListViewButtonUpdate, moduleId);

			// }

			/*
			 * if(deleteViewButtons!=null && !deleteViewButtons.isEmpty()){
			 * listViewService
			 * .deleteListViewByIds(deleteViewButtons,"ListViewButtons"); }
			 */

			if (isListViewButtonUpdate) {
				message.put("successMessage", getText("listview.buttons.updated", (String) listViewObj.getViewName(), locale));
				log.info(getText("listview.buttons.updated", (String) listViewObj.getViewName(), locale));

			} else {
				message.put("successMessage", getText("listview.buttons.created", (String) listViewObj.getViewName(), locale));
				log.info(getText("listview.buttons.created", (String) listViewObj.getViewName(), locale));
			}
			message.put("listViewId", listView.getId());
			model.addAttribute("listView", listView);
		} catch (Exception e) {
			log.error(getText("listview.buttons.error", locale), e);
			message.put("errorMessage", getText("listview.buttons.error", locale));
		}

		return message;
	}

	/**
	 * To get button all Properties of the list view
	 */
	@RequestMapping(value = "/bpm/listView/buttonsProperty", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> buttonsProperty(@ModelAttribute("listViewId") String listViewId, ModelMap model, HttpServletRequest request) {
		List<Map<String, Object>> mapViewColumnProperty = null;
		try {
			List<ListViewButtons> listViewButtonProperty = listViewService.getButtonPropertyByListViewId(listViewId);

			String[] fieldNames = { "id", "displayName", "orderBy", "buttonMethod", "iconPath", "tableName", "columnName", "redirectValue", "active", "listViewButtonUsers", "listViewButtonUsersFullName", "listViewButtonDeps", "listViewButtonRoles", "listViewButtonGroups" };
			mapViewColumnProperty = CommonUtil.getMapListFromObjectListByFieldNames(listViewButtonProperty, fieldNames, "");
		} catch (BpmException e) {
			log.error(e.getMessage(), e);
		} catch (EazyBpmException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return mapViewColumnProperty;
	}

	/**
	 * Get the saved list view based on id, to show the edit view
	 * 
	 * @param listViewId
	 * @param model
	 * @return
	 * @throws BpmException
	 */
	@RequestMapping(value = "bpm/listView/showListViewDesignEdit", method = RequestMethod.GET)
	public @ResponseBody ModelAndView showListViewDesignEdit(@RequestParam(value = "listViewId", required = false) String listViewId, @RequestParam(value = "moduleId", required = false) String moduleId, @RequestParam(value = "isListViewTemplate", required = false) boolean isListViewTemplate, @RequestParam(value = "isFromModule", required = false) String isFromModule, @RequestParam(value = "viewName", required = false) String viewName, ModelMap model, HttpServletRequest request) {
		ListView listView = null;
		Locale locale = request.getLocale();
		try {
			listView = listViewService.getListViewById(listViewId);
			if (isListViewTemplate == true && listView.getIsTemplate() == true) {
				listView.setViewName("");
			}
			Module module = null;
			if (moduleId.isEmpty()) {
				module = listViewService.getListViewModulesById(listViewId);
			} else {
				module = moduleService.getModule(moduleId);
			}

			String isEdit = request.getParameter("isEdit");
			if (isEdit != null && isEdit.equals("true")) {
				model.addAttribute("isEdit", true);
			} else {
				model.addAttribute("isEdit", false);
			}

			model.addAttribute("moduleId", module.getId());
			model.addAttribute("moduleName", module.getName());
			model.addAttribute("listView", listView);
			model.addAttribute("isFromModule", isFromModule);
			model.addAttribute("isListViewTemplate", isListViewTemplate);
			model.addAttribute("viewName", viewName);

		} catch (Exception e) {
			log.error(getText("listView.error.rendering", locale), e);
		}
		return new ModelAndView("listview/showListView", model);
	}

	/**
	 * Show the List view group setting in Module tree
	 * 
	 * @param listViewId
	 * @param listViewName
	 * @param title
	 * @param container
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/listView/showListViewGroupGrid", method = RequestMethod.GET)
	public @ResponseBody ModelAndView showListViewGroupGrid(@RequestParam("listViewId") String listViewId, @RequestParam("moduleId") String moduleId, @RequestParam("listViewName") String listViewName, @RequestParam("title") String title, @RequestParam("container") String container, ModelMap model, HttpServletRequest request) {
		String script = null;
		ListView listView = null;
		Locale locale = request.getLocale();
		try {
			listView = listViewService.getListViewById(listViewId);
			if (listView.getListViewColumns().size() != 0) {
				script = listViewService.showListViewGrid(listViewName, title, container, null, false, null, true);

				model.addAttribute("listView", listView);
				model.addAttribute("listViewGroup", listViewService.constructJsonListViewGroup(listView));
				model.addAttribute("script", script);
				model.addAttribute("moduleId", moduleId);
				model.addAttribute("success", true);
			} else {
				return showListViewDesignEdit(listViewId, moduleId, false, "false", "", model, request);
			}
		} catch (BpmException e) {
			log.error(e.getMessage(), e);
			model.addAttribute("success", false);
			saveError(request, getText("list.view.error", e.getMessage(), locale));
			model.addAttribute("script", getText("listView.grid.error", e.getMessage(), locale));
		} catch (Exception e) {
			model.addAttribute("success", false);
			model.addAttribute("script", getText("listView.grid.error", e.getMessage(), locale));
			log.error(e.getMessage(), e);
		}

		return new ModelAndView("listview/listViewGroupGrid", model);
	}

	/**
	 * To delete the list view details
	 * 
	 * @param listViewId
	 * @param model
	 * @return
	 * @throws BpmException
	 */
	@RequestMapping(value = "bpm/listView/deleteListViewDetails", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deleteListViewDetails(@RequestParam("listViewId") String listViewId, ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		Map<String, Object> message = new HashMap<String, Object>();
		ListView listViewObj = listViewService.getListViewById(listViewId);
		try {
			listViewService.deleteListViewDetailsById(listViewId);
			log.info("List View  '" + listViewObj.getViewName() + "' is deleted successfully.");
			message.put("success", getText("listview.deleted", listViewObj.getViewName(), locale));
		} catch (Exception e) {
			message.put("error", getText("listview.delete.error", listViewObj.getViewName(), locale));
			log.error(getText("listView.delete.error", locale), e);
		}
		return message;
	}

	/**
	 * To delete bulk list view details
	 * 
	 * @param listViewId
	 * @param model
	 * @return
	 * @throws BpmException
	 */
	@RequestMapping(value = "bpm/listView/deleteBulkListViewDetails", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteBulkListViewDetails(@RequestParam("listViewIds") String listViewIds, ModelMap model, HttpServletRequest request) throws Exception {
		Locale locale = request.getLocale();
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			List<String> listOfIds = CommonUtil.convertJsonToListStrings(listViewIds);
			for (String listViewId : listOfIds) {
				ListView listViewObj = listViewService.getListViewById(listViewId);
				listViewService.deleteListViewDetailsById(listViewId);
				log.info("List View  '" + listViewObj.getViewName() + "' is deleted successfully.");
			}
			message.put("success", getText("listviews.deleted", locale));
		} catch (Exception e) {
			message.put("error", getText("listviews.delete.error", locale));
			log.error(getText("listView.delete.error", locale), e);
		}
		return message;
	}

	/**
	 * To delete the list view details
	 * 
	 * @param listViewId
	 * @param model
	 * @return
	 * @throws JSONException
	 * @throws BpmException
	 */
	@RequestMapping(value = "bpm/listView/softDeleteTableDataAndRestore", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> softDeleteTableDataAndRestore(@RequestParam("deleteTblIds") String deleteTblIds, @RequestParam("tableName") String tableName, @RequestParam("isDelete") String isDelete, @RequestParam("columnName") String columnName, ModelMap model, HttpServletRequest request) throws JSONException {
		Locale locale = request.getLocale();
		Map<String, Object> message = new HashMap<String, Object>();
		List<String> deleteTableIds = CommonUtil.convertJsonToListStrings(deleteTblIds);
		try {
			int status = isDelete.equalsIgnoreCase("true") ? 1 : 0;
			int result = listViewService.softDeleteTableDataAndRestore(deleteTableIds, tableName, columnName, status);
			if (isDelete.equalsIgnoreCase("true")) {
				log.info("Soft Deleted '" + tableName + "' datas successfully.");
			} else {
				log.info("Restored '" + tableName + "' datas successfully.");
			}

			message.put("success", isDelete.equalsIgnoreCase("true") ? getText("softdelete.success", tableName, locale) : getText("restore.success", tableName, locale));
		} catch (Exception e) {
			message.put("error", isDelete.equalsIgnoreCase("true") ? getText("softdelete.error", tableName, locale) : getText("restore.error", tableName, locale));
			log.error("Error while deleting the " + tableName + " data ", e);
		}
		return message;
	}

	/**
	 * To delete the list view details
	 * 
	 * @param listViewId
	 * @param model
	 * @return
	 * @throws JSONException
	 * @throws BpmException
	 */
	@RequestMapping(value = "bpm/listView/deleteTableData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deleteData(@RequestParam("deleteTblIds") String deleteTblIds, @RequestParam("tableName") String tableName, @RequestParam("columnName") String columnName, ModelMap model, HttpServletRequest request) throws JSONException {
		Locale locale = request.getLocale();
		Map<String, Object> message = new HashMap<String, Object>();
		List<String> deleteTableIds = CommonUtil.convertJsonToListStrings(deleteTblIds);
		try {
			int result = listViewService.deleteTableData(deleteTableIds, tableName, columnName);
			log.info("'" + tableName + "' data is deleted successfully.");
			message.put("success", getText("delete.tabledata.success", tableName, locale));
		} catch (Exception e) {
			message.put("error", getText("delete.tabledata.error", tableName, locale));
			log.error("Error while deleting the " + tableName + " data ", e);
		}
		return message;
	}

	/**
	 * To delete the list view details
	 * 
	 * @param listViewId
	 * @param model
	 * @return
	 * @throws BpmException
	 */
	@RequestMapping(value = "bpm/listView/deleteAllListViewDetails", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deleteAllListViewDetails(@RequestParam("listViewName") String listViewName, ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			listViewService.deleteAllListViewDetails(listViewName);
			log.info("List View  '" + listViewName + "' is deleted successfully.");
			message.put("success", getText("listview.deleted", listViewName, locale));
		} catch (Exception e) {
			message.put("error", getText("listview.delete.error", listViewName, locale));
			log.error("Error while deleting the list view for editing", e);
		}
		return message;
	}

	/**
	 * 
	 * @param model
	 * @param currentNode
	 * @param rootNode
	 * @param groupByFields
	 * @param errors
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "bpm/listView/getGroupSettingSelectionChildNodes", method = RequestMethod.GET)
	public @ResponseBody String getUserSelectionChildNodes(@RequestParam("currentNode") String currentNode, @RequestParam("rootNode") String rootNode, @RequestParam("groupByFields") String groupByFields, @RequestParam("listViewId") String listViewId, @RequestParam("parentGroup") String parentGroup, @RequestParam("whereParams") String whereParams, HttpServletRequest request) {
		Locale locale = request.getLocale();
		String nodes = "";
		try {
			List<Map<String, Object>> whereParamsListOfMap = CommonUtil.convertJsonToList(whereParams);
			List<String> groupByFieldList = CommonUtil.convertJsonToListStrings(groupByFields);
			JSONArray childNodeList = listViewService.getChildNodesForTree(listViewId, rootNode, parentGroup, groupByFieldList, whereParamsListOfMap);
			nodes = String.valueOf(childNodeList);
		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
			saveError(request, getText("errors.userSelection.getNodes", locale));
		} catch (Exception e) {
			e.printStackTrace();
			saveError(request, getText("errors.userSelection.getNodes", locale));
			log.warn(e.getMessage(), e);
		}
		return nodes;
	}

	/**
	 * Get all list view as label value pair
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/listView/getAllListView", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, String>> getAllListView(HttpServletRequest request) {
		List<Map<String, String>> listViewList = new ArrayList<Map<String, String>>();
		Locale locale = request.getLocale();
		try {
			List<LabelValue> listViews = listViewService.getAllListViewWithoutVersion();
			if (listViews != null) {
				for (LabelValue listView : listViews) {
					Map<String, String> listViewDetail = new HashMap<String, String>();
					listViewDetail.put("listViewName", listView.getLabel());
					listViewDetail.put("listViewId", listView.getValue());
					listViewList.add(listViewDetail);
				}
				return listViewList;
			}
			// log.info("List View Retrived all labelvalue Successfully");
		} catch (Exception e) {
			log.error(getText("listView.error.labelValue", locale), e);
		}
		return null;
	}

	/**
	 * Get all list view as label value pair
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/listView/getAllListViewTemplate", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, String>> getAllListViewTemplate(HttpServletRequest request) {
		List<Map<String, String>> listViewList = new ArrayList<Map<String, String>>();
		Locale locale = request.getLocale();
		try {
			List<LabelValue> listViews = listViewService.getAllListViewTemplate();
			if (listViews != null) {
				for (LabelValue listView : listViews) {
					Map<String, String> listViewDetail = new HashMap<String, String>();
					listViewDetail.put("listViewName", listView.getLabel());
					listViewDetail.put("listViewId", listView.getValue());
					listViewList.add(listViewDetail);
				}
				return listViewList;
			}
			// log.info("List View Retrived All ListView Template
			// Successfully");

		} catch (Exception e) {
			log.error(getText("listView.error.labelValue", locale), e);
		}
		return null;
	}

	/**
	 * show table selection dialog with table
	 * 
	 * @param model
	 * @param selectionType
	 * @param appendTo
	 * @param selectedValues
	 * @param callAfter
	 * @param user
	 * @param errors
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "bpm/listView/showTableSelection", method = RequestMethod.POST)
	public @ResponseBody String showTableSelection(ModelMap model, @RequestParam("currentNode") String currentNode, @RequestParam("rootNode") String rootNode, @RequestParam("nodeLevel") String nodeLevel, @RequestParam("selectedValues") String selectedValues, User user, @RequestParam("defaultNodes") String defaultNodes, BindingResult errors, HttpServletRequest request, HttpServletResponse response) {

		String nodes = "";
		try {
			if (rootNode.equalsIgnoreCase("Meta_Tables")) {
				List<LabelValue> allMetaTables = listViewService.getAllMetaTable();
				nodes = formTableSelectionNodes(allMetaTables, rootNode, false, selectedValues);
			} else if (rootNode.equalsIgnoreCase("Run_Time_Tables")) {
				List<LabelValue> allMetaTables = listViewService.getAllTable();
				nodes = formTableSelectionNodes(allMetaTables, rootNode, true, selectedValues);
			} else {
				JSONObject jsonNodes = new JSONObject();
				JSONArray node = new JSONArray();
				Map<String, Object> context = new HashMap<String, Object>();
				List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
				Map<String, Object> nodeDetails = new HashMap<String, Object>();
				Map<String, Object> attr = new HashMap<String, Object>();
				List<String> nodeNames = CommonUtil.convertJsonToListStrings(defaultNodes);
				for (String nodeName : nodeNames) {
					attr.put("id", nodeName);
					attr.put("name", nodeName);
					attr.put("isChild", false);
					nodeDetails.put("data", nodeName);
					nodeDetails.put("attr", attr);
					nodeList.add(nodeDetails);
					jsonNodes = CommonUtil.getOrganizationTreeNodes(nodeList);
					node.put(jsonNodes);
					context.put("nodes", nodes);
					context.put("selection", "fromTable");
				}
				context.put("selectionType", "multi");
				context.put("childNodeUrl", "bpm/listView/showTableSelection");
				selectedValues = selectedValues.replace(".", "-");
				context.put("selectedValues", selectedValues);
				context.put("needTreeCheckbox", true);
				context.put("needContextMenu", true);

				nodes = node.toString();
			}

		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}

		return nodes.toString();
	}

	/**
	 * Child node for tables selection
	 * 
	 * @param childNodeList
	 * @param userNodeList
	 * @param root
	 * @param index
	 * 
	 * @return
	 */
	public String formTableSelectionNodes(List<LabelValue> tableNodeList, String root, boolean isRuntimeTable, String selectedValues) throws BpmException, Exception {
		JSONArray nodes = new JSONArray();
		JSONObject jsonNodes = new JSONObject();
		Map<String, Object> attr = new HashMap<String, Object>();
		Map<String, Object> nodeDetails = new HashMap<String, Object>();
		List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
		if (tableNodeList != null && tableNodeList.size() > 0) {
			for (LabelValue node : tableNodeList) {
				String tableName = node.getLabel();
				String tableId = "";
				if (isRuntimeTable) {
					if (selectedValues.equals("allTables")) {
						tableName = tableName;
					} else {
						tableName = "`" + tableName + "`" + " AS " + "`" + tableName + "`";
					}
				} else {
					if (!selectedValues.equals("allTables")) {
						tableName = tableName + " AS " + tableName;
					} else {
						tableName = tableName;
					}
				}
				/*
				 * if(isRuntimeTable){ tableName="ETEC_RU_"+tableName; }
				 */
				tableId = tableName.replaceAll(" ", "");
				tableId = tableId.replaceAll("`", "");
				attr.put("id", tableId);
				attr.put("name", tableName);
				attr.put("root", root);
				attr.put("isChild", true);
				nodeDetails.put("data", tableName);
				nodeDetails.put("attr", attr);
				nodeList.add(nodeDetails);
				jsonNodes = CommonUtil.getOrganizationTreeNodes(nodeList);
				nodes.put(jsonNodes);
			}
		}
		return nodes.toString();
	}

	/**
	 * show table selection dialog with table
	 * 
	 * @param model
	 * @param selectionType
	 * @param appendTo
	 * @param selectedValues
	 * @param callAfter
	 * @param user
	 * @param errors
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "bpm/listView/showTableSelectFrom", method = RequestMethod.GET)
	public ModelAndView showTableSelectFrom(ModelMap model, @RequestParam("selectionType") String selectionType, @RequestParam("appendTo") String appendTo, @RequestParam("selectedValues") String selectedValues, @RequestParam("callAfter") String callAfter, @RequestParam("tableList") String tableList, @RequestParam("treeType") String treeType) {
		Map<String, Object> context = new HashMap<String, Object>();
		try {

			context.put("defaultNodes", tableList);
			context.put("selectionType", selectionType);
			selectedValues = selectedValues.replace(".", "-");
			context.put("selectedValues", selectedValues);

			if (treeType.equals("Table")) {
				context.put("childNodeUrl", "bpm/listView/showTableSelection");
				context.put("isSelectedValues", false);
			} else if (treeType.equals("Table Export")) {
				context.put("childNodeUrl", "bpm/listView/showTableSelection");
				context.put("isSelectedValues", true);
			} else {
				context.put("isSelectedValues", false);
				context.put("childNodeUrl", "bpm/listView/showColumnSelection");
			}

			context.put("needTreeCheckbox", true);
			context.put("needContextMenu", true);
			String script = "";
			if (appendTo.equals("allTables")) {
				context.put("childNodeUrl", "bpm/listView/showTableSelection");
				script = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "TableTree.vm", context);
			} else {
				script = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "DynamicTree.vm", context);
			}

			model.addAttribute("selectedValues", selectedValues);
			model.addAttribute("script", script);
			model.addAttribute("appendTo", appendTo);
			model.addAttribute("callAfter", callAfter);
			model.addAttribute("selectionType", selectionType);

		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}

		if (appendTo.equals("allTables")) {
			return new ModelAndView("table/showManageTables", model);
		} else {
			return new ModelAndView("jstree/showTree", model);
		}

	}

	/**
	 * show table selection dialog with table
	 * 
	 * @param model
	 * @param selectionType
	 * @param appendTo
	 * @param selectedValues
	 * @param callAfter
	 * @param user
	 * @param errors
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "bpm/listView/showColumnSelection", method = RequestMethod.POST)
	public @ResponseBody String showColumnSelection(ModelMap model, @RequestParam("currentNode") String currentNode, @RequestParam("rootNode") String rootNode, @RequestParam("nodeLevel") String nodeLevel, @RequestParam("selectedValues") String selectedValues, User user, BindingResult errors, HttpServletRequest request, HttpServletResponse response, @RequestParam("defaultNodes") String tableList) {

		String nodes = "";
		try {
			if (rootNode.equalsIgnoreCase("default")) {
				JSONObject jsonNodes = new JSONObject();
				JSONArray node = new JSONArray();
				Map<String, Object> context = new HashMap<String, Object>();
				List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
				Map<String, Object> nodeDetails = new HashMap<String, Object>();
				Map<String, Object> attr = new HashMap<String, Object>();
				List<String> nodeNames = CommonUtil.convertJsonToListStrings(tableList);

				for (String nodeName : nodeNames) {
					attr.put("id", nodeName);
					attr.put("name", nodeName);
					attr.put("isChild", false);
					nodeDetails.put("data", nodeName);
					nodeDetails.put("attr", attr);
					nodeList.add(nodeDetails);
					jsonNodes = CommonUtil.getOrganizationTreeNodes(nodeList);
					node.put(jsonNodes);
					context.put("nodes", nodes);
					context.put("selection", "fromTable");
				}
				context.put("selectionType", "multi");
				selectedValues = selectedValues.replace(".", "-");
				context.put("selectedValues", selectedValues);
				context.put("childNodeUrl", "bpm/listView/showTableSelection");
				context.put("needTreeCheckbox", true);
				context.put("needContextMenu", true);

				nodes = node.toString();
			} else {
				List<LabelValue> allMetaColumns = listViewService.getAllColumnsByTableName(rootNode);
				nodes = getColumnWhitTableName(allMetaColumns, rootNode);
			}

		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}

		return nodes.toString();
	}

	/**
	 * form user selection tree child nodes
	 * 
	 * @param childNodeList
	 * @param userNodeList
	 * @param root
	 * @param index
	 * 
	 * @return
	 */
	public String getColumnWhitTableName(List<LabelValue> tableNodeList, String root) throws BpmException, Exception {
		JSONArray nodes = new JSONArray();
		JSONObject jsonNodes = new JSONObject();
		Map<String, Object> attr = new HashMap<String, Object>();
		Map<String, Object> nodeDetails = new HashMap<String, Object>();
		List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
		if (tableNodeList != null && tableNodeList.size() > 0) {
			for (LabelValue node : tableNodeList) {
				String columnName = node.getLabel();
				String tableId = root + "-" + columnName;
				columnName = root + "." + columnName;
				attr.put("id", tableId);
				attr.put("name", columnName);
				attr.put("root", root);
				attr.put("isChild", true);
				nodeDetails.put("data", columnName);
				nodeDetails.put("attr", attr);
				nodeList.add(nodeDetails);
				jsonNodes = CommonUtil.getOrganizationTreeNodes(nodeList);
				nodes.put(jsonNodes);
			}
		}
		return nodes.toString();
	}

	/**
	 * To export CSV data as xsl file
	 * 
	 * @param gridHeader
	 * @param gridDatas
	 * @param gridName
	 * @param model
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "bpm/listView/gridDataCSVExport", method = RequestMethod.POST)
	public void gridDataCSVExport(@RequestParam("gridHeader") String gridHeader, @RequestParam("gridDatas") String gridDatas, @RequestParam("gridName") String gridName, @RequestParam("hiddenColumn") String hiddenColumn, ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Locale locale = request.getLocale();
		String gridHead = gridHeader;
		// String gridData =gridDatas;
		// convert the josn data into list of map data
		try {

			List<Map<String, Object>> gridDataMap = CommonUtil.convertJsonToList(gridDatas);

			List<String> hiddenColumnName = new ArrayList<String>();
			// Get hidden column
			if (hiddenColumn != "") {
				if (hiddenColumn.contains(",")) {
					String[] keys = hiddenColumn.split(",");
					for (String key : keys) {
						hiddenColumnName.add(key);
					}
				} else {
					hiddenColumnName.add(hiddenColumn);
				}
			}

			// removing the hidden columns
			for (String columnName : hiddenColumnName) {
				if (gridHeader.contains(columnName)) {
					gridHead = gridHead.replaceAll("\\b" + columnName + "\\b", "");
				}
			}

			List<String> columnName = new ArrayList<String>();
			if (gridHead.contains(",")) {
				String[] keys = gridHead.split(",");
				for (String key : keys) {
					if (key != " " && !key.isEmpty()) {
						columnName.add(key);
					}
				}
			} else {
				columnName.add(gridHead);
			}

			List<String> colmnName = new ArrayList<String>();
			List<List<String>> rows = new ArrayList<List<String>>();
			int count = 0;
			// Create the header name and grid data for CSV Export
			for (Map<String, Object> rowMap : gridDataMap) {
				List<String> rowData = new ArrayList<String>();
				for (String colName : columnName) {
					if (rowMap.containsKey(colName)) {
						// Getting Header names
						if (count == 0) {
							colmnName.add(colName);
						}
						// Getting row values
						rowData.add(String.valueOf(rowMap.get(colName)));
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
			for (String cellValue : colmnName) {
				header.createCell(cellCount).setCellValue(cellValue.replace("_", " "));
				cellCount++;
			}

			int rowcount = 1;
			for (List<String> row : rows) {
				cellCount = 0;
				HSSFRow cellRow = newSheet.createRow(rowcount);
				for (String cellValue : row) {
					cellRow.createCell(cellCount).setCellValue(cellValue);
					cellCount++;
				}
				rowcount++;
			}
			wb.write(response.getOutputStream());
			// log.info("List View dataCSV export done Successfully");

		} catch (Exception e) {
			saveError(request, getText("grid.dataCSV.export.error", e.getMessage(), locale));
		}
	}

	/**
	 * Restore the selected List view
	 * 
	 * @return view containing list of form
	 */
	@RequestMapping(value = "bpm/listView/restoreListView", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> restoreListView(@RequestParam("viewId") String viewId, @RequestParam("viewName") String viewName, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale locale = request.getLocale();
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			listViewService.restoreListViewVersion(viewId, viewName);
			message.put("successMessage", getText("listView.restored", locale));
		} catch (BpmException e) {
			log.error(e.getMessage(), e);
			message.put("errorMessage", getText("listView.restored.error", locale));
		}
		return message;
	}

	/**
	 * To get the list of where param names
	 * 
	 * @return List<String>
	 */
	@RequestMapping(value = "bpm/listView/getListViewByName", method = RequestMethod.GET)
	public @ResponseBody List<String> getListViewByName(@RequestParam("viewName") String viewName, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<String> listViewWhereParamList = new ArrayList<String>();
		try {
			listViewWhereParamList = listViewService.getListViewWhereParamNames(viewName);
			if (listViewWhereParamList != null) {
				return listViewWhereParamList;
			} else {
				listViewWhereParamList = null;
			}
		} catch (BpmException e) {
			log.error(e.getMessage(), e);
		}
		return listViewWhereParamList;
	}

	@RequestMapping(value = "bpm/admin/showListViewSelection", method = RequestMethod.GET)
	public ModelAndView showListViewSelection(ModelMap model, @RequestParam("selectionType") String selectionType, @RequestParam("appendTo") String appendTo, @RequestParam("selectedValues") String selectedValues, @RequestParam("rootNodeURL") String rootNodeURL, @RequestParam("callAfter") String callAfter, User user, BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
		JSONArray nodes = new JSONArray();
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			context.put("nodes", nodes);
			context.put("rootNodeURL", rootNodeURL);
			context.put("selectionType", selectionType);
			context.put("selection", "selectedListView");
			context.put("selectedValues", selectedValues);
			context.put("needContextMenu", true);
			context.put("needTreeCheckbox", true);
			String script = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "organizationTree.vm", context);
			model.addAttribute("script", script);
			model.addAttribute("appendTo", appendTo);
			model.addAttribute("callAfter", callAfter);
			model.addAttribute("selectionType", selectionType);
			model.addAttribute("selectedValues", selectedValues);
			model.addAttribute("selection", "selectedListView");

		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
		return new ModelAndView("admin/organizationTree", model);
	}

	@RequestMapping(value = "bpm/admin/getListViewRootNodes", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> getListViewRootNodes(ModelMap model, @RequestParam("currentNode") String currentNode, @RequestParam("rootNode") String rootNode, @RequestParam("nodeLevel") int nodeLevel) {
		List<Map<String, Object>> nodeListOfMap = new ArrayList<Map<String, Object>>();
		List<LabelValue> listViewList = listViewService.getAllListViewWithoutVersion();
		try {
			for (LabelValue listView : listViewList) {
				CommonUtil.createListViewRootTreeNode(nodeListOfMap, listView.getLabel(), listView.getValue(), listView.getLabel());
			}
		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
		return nodeListOfMap;
	}

	/**
	 * To copy the list view to modules
	 * 
	 * @param listViewId
	 * @param moduleId
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bpm/listView/copyListViewToModule", method = RequestMethod.GET)
	public RedirectView copyListViewToModule(@RequestParam("listViewId") String listViewId, @RequestParam("moduleId") String moduleId, @RequestParam("currentModuleName") String currentModuleName, @RequestParam("moduleName") String moduleName, @RequestParam("listViewName") String listViewName, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Locale locale = request.getLocale();
		try {
			String copyStatus = listViewService.copyListViewToModule(moduleId, listViewId);
			if (copyStatus.equals("true")) {
				saveMessage(request, getText("listView.copied.successfully", locale));
				log.info("List View '" + listViewName + "' Copied From Module:'" + currentModuleName + "' To Module:'" + moduleName + "' successfully");
			} else if (copyStatus.equals("false")) {
				saveError(request, getText("listView.copied.failed", locale));
				log.error(getText("listView.copied.failed", locale));
			} else {
				saveError(request, getText("listView.copied.already.exits", copyStatus, locale));
				log.error(getText("listView.copied.already.exits", copyStatus, locale));
			}

		} catch (BpmException e) {
			log.error(e.getMessage(), e);
			saveError(request, getText("listView.copied.failed", locale));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new RedirectView("/bpm/module/manageModuleRelation");
	}

	/**
	 * get add button event page
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/bpm/listView/buttonEvent", method = RequestMethod.GET)
	public ModelAndView buttonEvent(ModelMap model, HttpServletRequest request) {
		return new ModelAndView("listview/buttonEvent", model);
	}

	/**
	 * List view name search
	 * 
	 * @param listViewName
	 * @param appendTo
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bpm/listView/getListViewNames", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, String>> getListViewNames(@RequestParam("listViewName") String listViewName, @RequestParam("appendTo") String appendTo, ModelMap model, HttpServletRequest request) throws Exception {
		List<Map<String, String>> listViewDetails = new ArrayList<Map<String, String>>();
		try {
			List<LabelValue> listViews = listViewService.getListViewNames(listViewName);
			if (listViews != null) {
				for (LabelValue listView : listViews) {
					Map<String, String> userDetail = new HashMap<String, String>();
					userDetail.put("listViewName", listView.getLabel());
					userDetail.put("id", listView.getValue());
					listViewDetails.add(userDetail);
				}
				return listViewDetails;
			}
			// log.info("All User Retrived Successfully");
		} catch (Exception e) {
			log.error("Error while getting list view " + e.getMessage());
		}
		return new ArrayList<Map<String, String>>();
	}

	/**
	 * Updates Order No of Role when the user click on move up and move down
	 * images in role grid.
	 * 
	 * @param listViewColumnJson
	 * @return response after updating the ordernos
	 */
	@RequestMapping(value = "bpm/listView/updateListViewColumnOrderNos", method = RequestMethod.POST)
	public @ResponseBody String updateListViewColumnOrderNos(@RequestParam("listViewColumnJson") String listViewColumnJson) {
		JSONObject respObj = new JSONObject();
		JSONArray responseMsg = new JSONArray();
		try {
			List<Map<String, Object>> columnList = CommonUtil.convertJsonToList(listViewColumnJson);
			boolean hasUpdated = listViewService.updateListViewColumnOrderNos(columnList);
			if (hasUpdated) {
				respObj.put("isSuccess", "true");
				responseMsg.put(respObj);
				return responseMsg.toString();
			}
		} catch (Exception e) {
			log.error("Error while updating order nos for Role", e);
		}
		return Constants.EMPTY_STRING;
	}

}
