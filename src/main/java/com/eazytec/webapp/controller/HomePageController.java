package com.eazytec.webapp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
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

import com.eazytec.Constants;
import com.eazytec.bpm.admin.NoticePlat.NoticePlatService;
import com.eazytec.bpm.admin.layout.service.LayoutService;
import com.eazytec.bpm.admin.menu.service.MenuService;
import com.eazytec.bpm.admin.role.service.RoleService;
import com.eazytec.bpm.admin.widget.service.WidgetService;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.bpm.oa.dms.service.DocumentFormService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.dao.SearchException;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.Layout;
import com.eazytec.model.Menu;
import com.eazytec.model.NoticePlat;
import com.eazytec.model.QuickNavigation;
import com.eazytec.model.User;
import com.eazytec.model.UserWidget;
import com.eazytec.model.Widget;
import com.eazytec.service.UserService;

/**
 * Simple class to retrieve user defined home page.
 * 
 * @author Sri Sudha
 */
@Controller
public class HomePageController extends BaseFormController {
	protected final transient Log log = LogFactory.getLog(getClass());
	public VelocityEngine velocityEngine;
	private LayoutService layoutService;
	private WidgetService widgetService;
	private UserService userService;
	private RoleService roleService;
	private MenuService menuService;
	private DocumentFormService documentFormService;
	private String isUpdate = "false";
	@Autowired
	private NoticePlatService noticeplatService;

	/**
	 * Show the home page
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/user/homePage", method = RequestMethod.GET)
	public ModelAndView showHomePage(ModelMap model, HttpServletRequest request) {
	/*	Locale locale = request.getLocale();
		List<Widget> widgetList = new ArrayList<Widget>();
		try {
			// Get layout detail from DB and set the widget according to that
			String widgetNames = "";
			String widgetNameLists = "";
			Layout layout = new Layout();
			UserWidget widget = widgetService.getUserWidgetForId(request.getRemoteUser());
			if (widget != null && widget.getWidgetId() != null && widget.getWidgetId().length() > 0 && !widget.getWidgetId().isEmpty()) {
				Layout userLayout = layoutService.getLoggedInUserLayout();
				List<String> widgetIds = new ArrayList<String>(Arrays.asList(widget.getWidgetId().split(",")));
				List<String> widgetNameList = new ArrayList<String>(Arrays.asList(userLayout.getColumnWidget().split(",")));
				List<String> widgetIdList = new ArrayList<String>(Arrays.asList(widget.getWidgetId().split(",")));
				boolean isDelete = widgetIdList.removeAll(widgetNameList);
				if(isDelete){
					if(widgetIdList.contains("QuickNavigation")){
						widgetIdList.remove("QuickNavigation");
					}
					widgetIds.removeAll(widgetIdList);
				}
				widgetNames = widgetIds.toString();
				widgetNames = widgetNames.replace("[", "");
				widgetNames = widgetNames.replace("]", "");
				widgetNames = widgetNames.replace(" ", "");
				layout.setNoOfWidget(widget.getNoOfWidget());
			} else {
				layout = layoutService.getLoggedInUserLayout();
				if (layout != null) {
					widgetNames = layout.getColumnWidget();
				}
			}
//			log.info(" widget List "+widgetNames);
			int quickNavigationPosition;
			if(widgetNames.contains("QuickNavigation")){
				List<String> widgetIdList = new ArrayList<String>(Arrays.asList(widgetNames.split(",")));
				quickNavigationPosition = widgetIdList.indexOf("QuickNavigation");
				widgetIdList.remove("QuickNavigation"); 
				widgetNameLists = widgetIdList.toString();
				widgetNameLists = widgetNameLists.replace("[", "");
				widgetNameLists = widgetNameLists.replace("]", "");
				widgetNameLists = widgetNameLists.replace(" ", "");
			}else{
				widgetNameLists = widgetNames;
				String widgetNameList[] = widgetNames.split(","); 
				quickNavigationPosition = widgetNameList.length;
			}
			List<Widget> assignedWidgetList = getWidgetListValue(widgetNameLists, locale,request);
			if(assignedWidgetList != null){
				widgetList.addAll(assignedWidgetList);
			}
			Widget widgetNavigation = new Widget();
			widgetNavigation.setId("QuickNavigation");
			widgetNavigation.setName("QuickNavigation");
			if(assignedWidgetList !=null && quickNavigationPosition == assignedWidgetList.size()){
				widgetList.add(widgetNavigation);
			}else{
				widgetList.add(widgetNavigation);
			}
			List<QuickNavigation> quickNavigationList = layoutService.getAllActiveQuickNavigation();
			int noOfColumns = layout.getNoOfWidget();
			if(widget != null && widget.getNoOfWidget() != 0){
				noOfColumns = widget.getNoOfWidget();
			}
			Map<String, Object> context = new HashMap<String, Object>();

			if(widgetList != null){
				getWidgetDetails(context, widgetList);
			}
			context.put("widgetList", widgetList);
			context.put("noOfColumns", noOfColumns);
			context.put("quickNavigationList", quickNavigationList);
			context.put("readMore", getText("button.viewAll", locale));

			String result = CommonUtil.generateHomePageScript(velocityEngine,context);
			model.addAttribute("widgetNames", widgetNames);
			model.addAttribute("noOfColumns", noOfColumns);
			model.addAttribute("widgetId", context.get("widgetId"));
			model.addAttribute("result", result);
		} catch (BpmException be) {
			be.printStackTrace();
			saveError(request,
					getText("home.page.error", be.getMessage(), locale));
			log.error(be.getMessage(), be);
		} catch (Exception e) {
			e.printStackTrace();
			saveError(request,
					getText("home.page.error", e.getMessage(), locale));
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("user/homePage", model);
*/
		
		 try { 
	        	String userid=CommonUtil.getLoggedInUserId();
	        	String userName=CommonUtil.getLoggedInUserName();
	        	List<NoticePlat> newList = new ArrayList<NoticePlat>();
	        	User user=CommonUtil.getLoggedInUser();
	        	List<NoticePlat> noticePlatList = new ArrayList<NoticePlat>();
	        	String isActive = "是";
	        	noticePlatList = noticeplatService.getNoticeListByUserid1(isActive);
	        	 String[] fieldNames={"id","title","createperson","createtime","sffb"};
	            String script=GridUtil.generateScriptForNoticePlatGrid1(CommonUtil.getMapListFromObjectListByFieldNames(noticePlatList,fieldNames,""), velocityEngine);
	            model.addAttribute("script", script);  
	            model.addAttribute("readOnly", "readOnly"); 
	        } catch (SearchException se) {
	            model.addAttribute("searchError", se.getMessage());
	            
	           
	        }
	        return new ModelAndView("admin/showNoticePLatList", model); 
	}

	@RequestMapping(value = "bpm/user/homePagePreview", method = RequestMethod.GET)
	public ModelAndView showHomePagePreview(ModelMap model,
			HttpServletRequest request,
			@RequestParam("noOfColumn") String noOfColumn,
			@RequestParam("selectedWidgets") String selectedWidgets) {
		Locale locale = request.getLocale();
		List<Widget> widgetList = new ArrayList<Widget>();
		try {

			if(!StringUtil.isEmptyString(selectedWidgets)){
				widgetList = getWidgetListValue(selectedWidgets,locale,request);
			}
			Map<String, Object> context = new HashMap<String, Object>();
			List<QuickNavigation> quickNavigationList = layoutService
					.getAllActiveQuickNavigation();

			context.put("widgetList", widgetList);
			context.put("noOfColumns", Integer.parseInt(noOfColumn));
			context.put("quickNavigationList", quickNavigationList);
			context.put("readMore", getText("button.viewAll", locale));
			String result = CommonUtil.generateHomePageScript(velocityEngine,
					context);
			model.addAttribute("result", result);
			log.info("Home page displayed Successfully");
		} catch (Exception e) {
			saveError(request,
					getText("home.page.error", e.getMessage(), locale));
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("user/homePage", model);
	}

	@RequestMapping(value = "bpm/user/homePagePreviewandSave", method = RequestMethod.GET)
	public ModelAndView showHomePagePreviewandSave(ModelMap model,
			HttpServletRequest request,
			@RequestParam("noOfColumn") String noOfColumn,
			@RequestParam("selectedWidgets") String selectedWidgets,
			@RequestParam("isRender") boolean isRender,
			@RequestParam("isSave") boolean isSave) {
		Locale locale = request.getLocale();
		List<Widget> widgetList = new ArrayList<Widget>();
		try {
			Layout layout = layoutService.getActiveLayout();
			String widgetNames = layout.getColumnWidget();

			if(!StringUtil.isEmptyString(selectedWidgets)){
				widgetList = getWidgetListValue(selectedWidgets,locale,request);
			}
			Map<String, Object> context = new HashMap<String, Object>();

			List<QuickNavigation> quickNavigationList = layoutService
					.getAllActiveQuickNavigation();

			if(widgetList != null){
				getWidgetDetails(context, widgetList);
			}

			context.put("widgetList", widgetList);
			context.put("noOfColumns", Integer.parseInt(noOfColumn));
			context.put("quickNavigationList", quickNavigationList);
			context.put("readMore", getText("button.viewAll", locale));

			String result = "";
			if (isRender == true) {
				result = CommonUtil.generateHomePageScript(velocityEngine,
						context);
				model.addAttribute("result", result);
			}
			Layout activeLayout = layoutService.getActiveLayout();
			if (isSave == true) {
				activeLayout.setNoOfWidget(Integer.parseInt(noOfColumn));
				activeLayout.setColumnWidget(selectedWidgets);
				layoutService.saveLayout(activeLayout);
			}
			model.addAttribute("widgetNames", widgetNames);
			model.addAttribute("widgetId", context.get("widgetId"));
			log.info("Home page Previewand Saveed Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			saveError(request,
					getText("home.page.error", e.getMessage(), locale));
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("user/homePage", model);
	}

	@RequestMapping(value = "bpm/user/userHomePagePreviewandSave", method = RequestMethod.GET)
	public ModelAndView showUserHomePagePreviewandSave(ModelMap model,
			HttpServletRequest request,
			@RequestParam("noOfColumn") String noOfColumn,
			@RequestParam("selectedWidgets") String selectedWidgets,
			@RequestParam("isRender") boolean isRender,
			@RequestParam("isSave") boolean isSave) {
		Locale locale = request.getLocale();
		try {
			Layout layout = layoutService.getActiveLayout();
			String widgetNames = layout.getColumnWidget();

			List<Widget> widgetList = getWidgetListValue(selectedWidgets,
					locale, request);
			int noOfColumns = 0;

			Map<String, Object> context = new HashMap<String, Object>();
			noOfColumns = convertNoOfColumnToInt(noOfColumn);

			List<QuickNavigation> quickNavigationList = layoutService
					.getAllActiveQuickNavigation();

			if(widgetList != null){
				getWidgetDetails(context, widgetList);
			}

			context.put("widgetList", widgetList);
			context.put("noOfColumns", noOfColumns);
			context.put("quickNavigationList", quickNavigationList);
			context.put("readMore", getText("button.viewAll", locale));

			String result = "";
			if (isRender == true) {
				result = CommonUtil.generateHomePageScript(velocityEngine,
						context);
				model.addAttribute("result", result);
			}
			Layout activeLayout = layoutService.getActiveLayout();
			if (isSave == true) {
				activeLayout.setNoOfWidget(noOfColumns);
				activeLayout.setColumnWidget(selectedWidgets);
				layoutService.saveLayout(activeLayout);
			}
			model.addAttribute("widgetNames", widgetNames);
			model.addAttribute("widgetId", context.get("widgetId"));
			log.info("User home page Previewand Saveed Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			saveError(request,
					getText("home.page.error", e.getMessage(), locale));
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("user/homePage", model);
	}

	private List<Widget> getWidgetListValue(String selectedWidgets,
			Locale locale, HttpServletRequest request) {
//		log.info("getting widgets for id's");
		List<Widget> widgetList = new ArrayList<Widget>();
		if (selectedWidgets != null && selectedWidgets.length() > 0) {
			List<String> widgetIds = new ArrayList<String>(
					Arrays.asList(selectedWidgets.split(",")));
			widgetList = widgetService.getWidgetForIds(widgetIds);
		}
		return widgetList;
	}

	private void getWidgetDetails(Map<String, Object> context,
			List<Widget> widgetList) throws Exception {
		Map<String, Object> fileDetails = new HashMap<String, Object>();
		String widgetId = "";
		String resourcePath = "";
		for (Widget widget : widgetList) {
			if(widget.getId().equals("QuickNavigation")){}
			else{
				widgetId = widgetId + "" + widget.getId().concat(",");
				if (widget.getLinkType().equals("JSP")) {
					/*
					 * String documentId = widget.getDocumentId(); FileImportForm
					 * fileImportForm =
					 * documentFormService.getImportFileById(documentId);
					 * if(fileImportForm != null){ resourcePath =
					 * fileImportForm.getResourcePath();
					 * fileDetails.put(documentId,widget.getWidgetUrl()); }
					 */
					fileDetails.put(widget.getId(), widget.getWidgetUrl());
				}
			}
		}

		StringBuffer strbuf = new StringBuffer();
		strbuf.append(widgetId);
		if (strbuf.lastIndexOf(",") > 0) {
			strbuf.deleteCharAt(strbuf.lastIndexOf(","));
		}
		context.put("fileDetails", fileDetails);
		context.put("widgetId", widgetId);
	}

	@RequestMapping(value = "bpm/user/defaultPage", method = RequestMethod.GET)
	public String showDefaultPage(ModelMap model) {
		return "user/defaultPage";
	}

	/**
	 * Save the widget order into layout when admin change the order of widget
	 * 
	 * @param model
	 * @param request
	 * @param column1
	 * @param column2
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/saveWidgetOrder", method = RequestMethod.GET)
	public @ResponseBody String saveWidgetOrder(ModelMap model, HttpServletRequest request,	@RequestParam("widgetIdList") String widgetIdList, @RequestParam("noOfColumns") String noOfColumns){
		Locale locale = request.getLocale();
		try {
			widgetService.saveUserWidget(widgetIdList, CommonUtil.getLoggedInUserId(),Integer.parseInt(noOfColumns));
		} catch (Exception e) {
			//saveError(request,getText("widget.order.save.error", e.getMessage(), locale));
		}

		return "";
	}

	/**
	 * Show the layout create screen to user
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/showCreateLayout", method = RequestMethod.GET)
	public ModelAndView showCreateLayout(ModelMap model,
			HttpServletRequest request) {
		Locale locale = request.getLocale();
		try {
			if (request.getParameter("id") != null) {
				Layout layout = layoutService.getLayoutForId(request
						.getParameter("id").toString());
				// model.addAttribute("widgetNames", layout.getWidgetNames());
				model.addAttribute("layout", layout);
			} else {
				model.addAttribute("layout", new Layout());

			}
			log.info("Successfully displayed create layout");
		} catch (BpmException be) {
			saveError( request, getText("show.create.layout.error", be.getMessage(), locale));
			log.error(be.getMessage(), be);
		} catch (Exception e) {
			saveError(request, getText("show.create.layout.error", e.getMessage(), locale));
		}
		return new ModelAndView("homePage/createLayout", model);
	}

	/**
	 * Show widget tree to select
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/showWidgetSelection", method = RequestMethod.GET)
	public ModelAndView showWidgetSelection(ModelMap model,
			@RequestParam("selectionType") String selectionType,
			@RequestParam("appendTo") String appendTo,
			@RequestParam("selectedValues") String selectedValues,
			@RequestParam("rootNodeURL") String rootNodeURL,
			@RequestParam("callAfter") String callAfter, User user,
			BindingResult errors, HttpServletRequest request,
			HttpServletResponse response) {
		log.info("getting widget selection details");
		JSONArray nodes = new JSONArray();
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			context.put("nodes", nodes);
			context.put("rootNodeURL", rootNodeURL);
			context.put("selectionType", selectionType);
			context.put("selection", "widgetSelection");
			context.put("selectedValues", selectedValues);
			context.put("needContextMenu", true);
			context.put("needTreeCheckbox", true);
			String script = VelocityEngineUtils.mergeTemplateIntoString( velocityEngine, "organizationTree.vm", context);
			model.addAttribute("script", script);
			model.addAttribute("appendTo", appendTo);
			model.addAttribute("callAfter", callAfter);
			model.addAttribute("selectionType", selectionType);
			model.addAttribute("selectedValues", selectedValues);
			model.addAttribute("selection", "widget");
			log.info("Successfully displayed widget selection details");
		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
		return new ModelAndView("admin/organizationTree", model);
	}

	@RequestMapping(value = "bpm/user/showLayoutSelection", method = RequestMethod.GET)
	public ModelAndView showLayoutSelection(ModelMap model,	@RequestParam("selectedValues") String selectedValues,	@RequestParam("appendTo") String appendTo,
			@RequestParam("callAfter") String callAfter,		@RequestParam("assignedTo") String assignedTo,		@RequestParam("layoutDepartments") String layoutDepartments,
			@RequestParam("layoutRoles") String layoutRoles, User user,		BindingResult errors, HttpServletRequest request,	HttpServletResponse response, @RequestParam("layoutType") String layoutType) {
		log.info("getting Layout selection details");
		JSONArray nodes = new JSONArray();
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			model.addAttribute("appendTo", appendTo);
			model.addAttribute("callAfter", callAfter);
			// model.addAttribute("selectionType", selectionType);
			model.addAttribute("selectedValues", selectedValues);

			Layout layout = layoutService.getLayoutByAssignedTo(assignedTo);
			if (layout == null) {
				layout = new Layout();
				layout.setNoOfWidget(1);
			}
			model.addAttribute("layout", layout);
			model.addAttribute("layoutDepartments", layoutDepartments);
			model.addAttribute("layoutRoles", layoutRoles);
			model.addAttribute("layoutType",layoutType);
			log.info("Successfully displayed Layout selection details");
		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
		return new ModelAndView("user/layouts", model);
	}

	@RequestMapping(value = "bpm/admin/getWidgetRootNodes", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> getWidgetRootNodes(ModelMap model,
			@RequestParam("currentNode") String currentNode,
			@RequestParam("rootNode") String rootNode,
			@RequestParam("nodeLevel") int nodeLevel) {
		// log.info("getting user selection details");
		List<Map<String, Object>> nodeListOfMap = new ArrayList<Map<String, Object>>();
		List<Widget> widgetsList = widgetService.getAllWidget();

		try {

			for (Widget widget : widgetsList) {
				CommonUtil.createWidgetRootTreeNode(nodeListOfMap,
						widget.getName(), widget.getId(), widget.getName());
			}
			log.info("Getting Widget Root Nodes Successfully");
		} catch (BpmException e) {
			log.warn(e.getMessage(), e);

		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
		return nodeListOfMap;
	}

	@RequestMapping(value = "bpm/admin/getUserWidgetRootNodes", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> getUserWidgetRootNodes(ModelMap model,
			@RequestParam("currentNode") String currentNode,
			@RequestParam("rootNode") String rootNode,
			@RequestParam("nodeLevel") int nodeLevel) {
		// log.info("getting user selection details");
		List<Map<String, Object>> nodeListOfMap = new ArrayList<Map<String, Object>>();
		List<Widget> widgetsList = widgetService.getAllUserSelectedWidget();
		try {
			if(widgetsList != null){
				for (Widget widget : widgetsList) {
					CommonUtil.createWidgetRootTreeNode(nodeListOfMap,
							widget.getName(), widget.getId(), widget.getName());
				}
				log.info("Getting User Widget Root Nodes Successfully");
			}
		} catch (BpmException e) {
			log.warn(e.getMessage(), e);

		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
		return nodeListOfMap;
	}

	@RequestMapping(value = "bpm/admin/getLayoutFromUser", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, Object>> getLayoutFromUser(ModelMap model,
			@RequestParam("currentNode") String currentNode,
			@RequestParam("rootNode") String rootNode,
			@RequestParam("nodeLevel") int nodeLevel) {
		// log.info("getting user selection details");
		List<Map<String, Object>> nodeListOfMap = new ArrayList<Map<String, Object>>();

		try {
			CommonUtil.createRootTreeNode(nodeListOfMap, "1", "one", "<img src=\"/images/sidemenu/administrator.png\">");
			CommonUtil.createRootTreeNode(nodeListOfMap, "2", "two", "2 columns");
			CommonUtil.createRootTreeNode(nodeListOfMap, "3", "three", "3 columns");
			log.info("Retrived Layout From User Successfully");
		} catch (BpmException e) {
			log.warn(e.getMessage(), e);

		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
		return nodeListOfMap;
	}

	/**
	 * Used to save the layout details
	 * 
	 * @param request
	 * @param layout
	 * @param model
	 * @param errors
	 * @return
	 */

	@RequestMapping(value = "bpm/admin/saveLayout", method = RequestMethod.POST)
	public ModelAndView saveLayout(HttpServletRequest request, Layout layout,
			ModelMap model, BindingResult errors) {
		Locale locale = request.getLocale();
		try {
			layout = layoutService.getLayoutForId("123");
			layoutService.saveLayout(layout);
			saveMessage(request, getText("layout.save.success", layout.getName(), locale));
			log.info("Saved Layout Successfully");
		} catch (BpmException be) {
			saveError(request, getText("layout.save.error", be.getMessage(), locale));
			log.error(be.getMessage(), be);
		} catch (Exception e) {
			saveError(request, getText("layout.save.error", e.getMessage(), locale));
		}

		return new ModelAndView("redirect:manageLayout");
	}

	private int convertNoOfColumnToInt(String noOfColumn) {
		int noOfColumns = 0;
		if (noOfColumn.equals("1")) {
			noOfColumns = 1;
		} else if (noOfColumn.equals("2")) {
			noOfColumns = 2;
		} else if (noOfColumn.equals("3")) {
			noOfColumns = 3;
		}
		return noOfColumns;
	}

	/**
	 * method to save layout
	 * 
	 * @param request
	 * @param noOfColumn
	 * @param selectedWidgets
	 * @param departments
	 * @param roles
	 * @param id
	 * @param assignedTo
	 * @param model
	 * @param errors
	 * @return
	 */
	@RequestMapping(value = "bpm/user/saveHomePage", method = RequestMethod.GET)
	public ModelAndView saveHomePage(HttpServletRequest request,
			@ModelAttribute("noOfColumn") String noOfColumn,
			@ModelAttribute("selectedWidgets") String selectedWidgets,
			@ModelAttribute("departments") String departments,
			@ModelAttribute("roles") String roles,
			@ModelAttribute("id") String id,
			@ModelAttribute("assignedTo") String assignedTo, ModelMap model,
			BindingResult errors) {
		log.info("saving layout");
		Locale locale = request.getLocale();
		try {
			List<Widget> widgetList = getWidgetListValue(selectedWidgets,
					locale, request);
			Map<String, Object> context = new HashMap<String, Object>();
			Layout activeLayout = layoutService.getActiveLayout();
			List<QuickNavigation> quickNavigationList = layoutService
					.getAllActiveQuickNavigation();
			if(widgetList != null){
				getWidgetDetails(context, widgetList);
			}
			context.put("widgetList", widgetList);
			context.put("noOfColumns", Integer.parseInt(noOfColumn));
			context.put("readMore", getText("button.viewAll", locale));
			context.put("quickNavigationList", quickNavigationList);
			activeLayout.setNoOfWidget(Integer.parseInt(noOfColumn));
			activeLayout.setColumnWidget(selectedWidgets);
			Layout layout = layoutService.saveLayout(activeLayout, assignedTo,
					departments, roles);
			String result = CommonUtil.generateHomePageScript(velocityEngine,
					context);
			model.addAttribute("result", result);
			log.info("Home Page Saved Successfully");
		} catch (Exception e) {
			saveError(request,
					getText("home.page.error", e.getMessage(), locale));
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("/user/homePage", model);
	}

	@RequestMapping(value = "bpm/user/saveUserHomePage", method = RequestMethod.GET)
	public ModelAndView saveUserHomePage(HttpServletRequest request,
			@ModelAttribute("noOfColumn") String noOfColumn,
			@ModelAttribute("selectedWidgets") String selectedWidgets,
			@ModelAttribute("departments") String departments,
			@ModelAttribute("roles") String roles,
			@ModelAttribute("id") String id,
			@ModelAttribute("assignedTo") String assignedTo, ModelMap model,
			BindingResult errors) {
		log.info("saving layout");
		Locale locale = request.getLocale();
		try {
			List<Widget> widgetList = getWidgetListValue(selectedWidgets,
					locale, request);
			widgetService.saveUserWidget(selectedWidgets,
					CommonUtil.getLoggedInUserName(),
					Integer.parseInt(noOfColumn));
			Map<String, Object> context = new HashMap<String, Object>();
			List<QuickNavigation> quickNavigationList = layoutService
					.getAllActiveQuickNavigation();
			if(widgetList != null){
				getWidgetDetails(context, widgetList);
			}
			context.put("widgetList", widgetList);
			context.put("noOfColumns", Integer.parseInt(noOfColumn));
			context.put("readMore", getText("button.viewAll", locale));
			context.put("quickNavigationList", quickNavigationList);
			String result = CommonUtil.generateHomePageScript(velocityEngine,
					context);
			model.addAttribute("result", result);
			log.info("User home page Saved Successfully");
		} catch (Exception e) {
			saveError(request,
					getText("home.page.error", e.getMessage(), locale));
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("/user/homePage", model);
	}

	/**
	 * Show All Layout details into grid
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "bpm/admin/manageLayout", method = RequestMethod.GET)
	public ModelAndView manageLayout(ModelMap model, HttpServletRequest request) {
		Locale locale = request.getLocale();
		Map<String, Object> orgTreeMap = new HashMap<String, Object>();
		try {
			// List<Layout> layoutList = layoutService.getAllLayout();
			// Layout layout = layoutService.getActiveLayout();
			Layout layout = layoutService.getLayoutByAssignedTo("Public");
			String widgetNames = layout.getColumnWidget();
			List<Widget> widgetList = getWidgetListValue(widgetNames, locale,
					request);
			int noOfColumns = layout.getNoOfWidget();
			List<QuickNavigation> quickNavigationList = layoutService
					.getAllActiveQuickNavigation();
			Map<String, Object> context = new HashMap<String, Object>();
			if(widgetList != null){
				getWidgetDetails(context, widgetList);
			}
			context.put("widgetList", widgetList);
			context.put("noOfColumns", noOfColumns);
			context.put("quickNavigationList", quickNavigationList);
			context.put("readMore", getText("button.viewAll", locale));
			String result = "";
			//String result = CommonUtil.generateHomePageScript(velocityEngine,context);
			// String result =
			// CommonUtil.generateHomePageScript(velocityEngine,context);
			
			User logInuser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			//Graduation authorization for department Admin
			List<String> userAdminDep=userService.getUserAdminDepartments(logInuser);
			List<String> userAdminDepRoles=null;
			if(userAdminDep!=null && !userAdminDep.isEmpty()){
				orgTreeMap.put("userAdminDept", CommonUtil.convertListToJson(userAdminDep));
				//Department Admin's roles
				userAdminDepRoles=roleService.getUserAdminDepartmentRoles(userAdminDep);
			}else{
				userAdminDep=new ArrayList<String>();
				userAdminDep.add("empty");
				orgTreeMap.put("userAdminDept", CommonUtil.convertListToJson(userAdminDep));
			}
			
			if(userAdminDepRoles!=null && !userAdminDepRoles.isEmpty()){
				orgTreeMap.put("userAdminDepRoles", CommonUtil.convertListToJson(userAdminDepRoles));
			}else{
				userAdminDepRoles=new ArrayList<String>();
				userAdminDepRoles.add("empty");
				orgTreeMap.put("userAdminDepRoles", CommonUtil.convertListToJson(userAdminDepRoles));
			}
			
			orgTreeMap.put("needTreeCheckbox", false);
			orgTreeMap.put("needContextMenu", false);
			orgTreeMap.put("orgTreeId", "layoutTree");
			orgTreeMap.put("rootNodeURL", "bpm/home/getHomePageRootNodes");
			orgTreeMap.put("childNodeURL", "bpm/admin/getHomePageChildNodes");
			String orgTreeScript = VelocityEngineUtils.mergeTemplateIntoString(
					velocityEngine, "organizationTree.vm", orgTreeMap);
			
			model.addAttribute("orgTreeScript", orgTreeScript);

			model.addAttribute("id", layout.getId());
			model.addAttribute("assignedTo", layout.getAssignedTo());
			model.addAttribute("noOfColumns", noOfColumns);
			model.addAttribute("departments", layout.getAssignedTo());
			model.addAttribute("roles", layout.getId());
			model.addAttribute("result",result);
			model.addAttribute("widgetId", context.get("widgetId"));
			model.addAttribute("widgetNames", widgetNames);
			log.info("Manage Layout Displayed Successfully");
		} catch (BpmException be) {
			saveError(request,
					getText("manage.layout.error", be.getMessage(), locale));
			log.error(be.getMessage(), be);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			saveError(request,
					getText("manage.layout.error", e.getMessage(), locale));
		}
		return new ModelAndView("homePage/manageLayout", model);
	}

	@RequestMapping(value = "bpm/admin/manageUserLayout", method = RequestMethod.GET)
	public ModelAndView manageUserLayout(ModelMap model,
			HttpServletRequest request) {
		Locale locale = request.getLocale();
		Map<String, Object> orgTreeMap = new HashMap<String, Object>();
		try {
			// List<Layout> layoutList = layoutService.getAllLayout();
			// Layout layout = layoutService.getActiveLayout();
			String widgetNames = "";
			Layout layout = new Layout();
			UserWidget widget = widgetService.getUserWidgetForId(request
					.getRemoteUser());
			if (widget != null) {
				widgetNames = widget.getWidgetId();
				layout.setNoOfWidget(widget.getNoOfWidget());
			} else {
				layout = layoutService.getLoggedInUserLayout();
				if (layout != null) {
					widgetNames = layout.getColumnWidget();
				}
			}
			List<Widget> widgetList = getWidgetListValue(widgetNames, locale,
					request);
			int noOfColumns = layout.getNoOfWidget();
			List<QuickNavigation> quickNavigationList = layoutService
					.getAllActiveQuickNavigation();
			Map<String, Object> context = new HashMap<String, Object>();
			if(widgetList != null){
				getWidgetDetails(context, widgetList);
			}
			context.put("widgetList", widgetList);
			context.put("noOfColumns", noOfColumns);
			context.put("quickNavigationList", quickNavigationList);
			context.put("readMore", getText("button.viewAll", locale));
			String result = CommonUtil.generateHomePageScript(velocityEngine,
					context);
			model.addAttribute("id", layout.getId());
			model.addAttribute("assignedTo", layout.getAssignedTo());
			model.addAttribute("noOfColumns", noOfColumns);
			model.addAttribute("departments", layout.getAssignedTo());
			model.addAttribute("roles", layout.getId());
			model.addAttribute("result", result);
			model.addAttribute("widgetId", context.get("widgetId"));
			model.addAttribute("widgetNames", widgetNames);
			log.info("User Layout Displayed Successfully");
		} catch (BpmException be) {
			saveError(request,
					getText("manage.layout.error", be.getMessage(), locale));
			log.error(be.getMessage(), be);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			saveError(request,
					getText("manage.layout.error", e.getMessage(), locale));
		}
		return new ModelAndView("homePage/manageUserLayout", model);
	}

	/**
	 * Show All Layout details into grid
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "bpm/admin/getLayoutScript", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, String>> getLayoutScript(HttpServletRequest request,
			@RequestParam("assignedTo") String assignedTo) {
		log.info("get layout script of current node");
		Locale locale = request.getLocale();
		String script = Constants.EMPTY_STRING;
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			boolean isValidAssignedTo = layoutService.graduationForWidget(assignedTo);
			Layout layout = layoutService.getLayoutByAssignedTo(assignedTo);
			if (layout != null && isValidAssignedTo == true ) {
				String widgetNames = layout.getColumnWidget();
				List<Widget> widgetList = getWidgetListValue(widgetNames,
						locale, request);
				int noOfColumns = layout.getNoOfWidget();
				List<QuickNavigation> quickNavigationList = layoutService
						.getAllActiveQuickNavigation();
				Map<String, Object> context = new HashMap<String, Object>();
				if(widgetList != null){
					getWidgetDetails(context, widgetList);
				}
				context.put("widgetList", widgetList);
				context.put("noOfColumns", noOfColumns);
				context.put("quickNavigationList", quickNavigationList);
				context.put("readMore", getText("button.viewAll", locale));
				script = CommonUtil.generateHomePageScript(velocityEngine,
						context);
				resultMap.put("layoutId", layout.getId());
				resultMap.put("assignedTo", layout.getAssignedTo());
				resultMap.put("noOfColumns", "" + layout.getNoOfWidget());
				resultMap.put("widgetNames", layout.getColumnWidget());
				resultMap.put("script", script);
				result.add(resultMap);
			}
		} catch (BpmException be) {
			saveError(request,
					getText("manage.layout.error", be.getMessage(), locale));
			log.error(be.getMessage(), be);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			saveError(request,
					getText("manage.layout.error", e.getMessage(), locale));
		}
		return result;
	}

	/**
	 * Show create QuickNavigation to user
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/showCreateQuickNav", method = RequestMethod.GET)
	public ModelAndView showCreateQuickNav(ModelMap model,
			HttpServletRequest request) {
		Locale locale = request.getLocale();
		QuickNavigation quickNavigation = null;
		Menu menu = new Menu();
		try {
			if (request.getParameter("id") != null) {
				setIsUpdate("true");
				quickNavigation = layoutService.getQuickNavForId(request
						.getParameter("id").toString());
				String menuId = quickNavigation.getMenuId();
				model.addAttribute("quickNavigation", quickNavigation);
				model.addAttribute("mode", Constants.EDIT_MODE);
			} else {
				setIsUpdate("false");
				model.addAttribute("quickNavigation", new QuickNavigation());
				model.addAttribute("mode", Constants.CREATE_MODE);
			}
			log.info("Success to displayed CreateQuickNAv");
		} catch (BpmException be) {
			setIsUpdate(getIsUpdate());
			saveError(request,
					getText("create.quickNav.error", be.getMessage(), locale));
			log.error(be.getMessage(), be);
			model.addAttribute("quickNavigation", quickNavigation);
		} catch (Exception e) {
			setIsUpdate(getIsUpdate());
			e.printStackTrace();
			saveError(request,
					getText("create.quickNav.error", e.getMessage(), locale));
			model.addAttribute("quickNavigation", quickNavigation);
		}
		return new ModelAndView("homePage/createQuickNavigation", model);
	}

	/**
	 * Save or update quickNavigation details
	 * 
	 * @param request
	 * @param quickNavigation
	 * @param model
	 * @param errors
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/saveQuickNavigation", method = RequestMethod.POST)
	public ModelAndView saveQuickNavigation(HttpServletRequest request,
			@ModelAttribute("quickNavigation") QuickNavigation quickNavigation,
			ModelMap model, BindingResult errors) {
		Locale locale = request.getLocale();
		log.info("saving quickNavigation");
		// Menu menu = null;
		try {

			if (validator != null) {
				validator.validate(quickNavigation, errors);
				if (errors.hasErrors()) {
					setIsUpdate(getIsUpdate());
					if (getIsUpdate().equalsIgnoreCase("true")
							&& getIsUpdate() != null) {
						model.addAttribute("mode", Constants.EDIT_MODE);
					} else {
						model.addAttribute("mode", Constants.CREATE_MODE);
					}
					model.addAttribute("quickNavigation", quickNavigation);
					return new ModelAndView("homePage/createQuickNavigation",
							model);

				}
			}
			String menuId = quickNavigation.getRootNodeId();
			Menu menu = menuService.getMenuById(menuId);
			if(menu != null){
				String menuName = menu.getName();
				quickNavigation.setRootNodeName(menuName.toLowerCase());
			}
			quickNavigation=layoutService.saveQuickNavigation(quickNavigation);
			if (getIsUpdate().equalsIgnoreCase("true") && getIsUpdate() != null) {
				saveMessage(
						request,
						getText("quickNav.update.success", quickNavigation.getName(), locale));
				log.info(getText("quickNav.update.success", quickNavigation.getName(), locale));

			} else {
				saveMessage(
						request,
						getText("quickNav.save.success", quickNavigation.getName(), locale));
				log.info(getText("quickNav.save.success", quickNavigation.getName(), locale));

			}
		} catch (BpmException be) {
			setIsUpdate(getIsUpdate());
			saveError(request,
					getText("quickNav.save.error", be.getMessage(), locale));
			log.error(be.getMessage(), be);
		} catch (Exception e) {
			setIsUpdate(getIsUpdate());
			saveError(request,
					getText("quickNav.save.error", e.getMessage(), locale));
			log.error(e.getMessage(), e);
		}

		return new ModelAndView("homePage/createQuickNavigation");
	}

	/**
	 * Show quickNavigation details into grid
	 * 
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "bpm/admin/manageQuickNavigation", method = RequestMethod.GET)
	public ModelAndView manageQuickNavigation(ModelMap model,
			HttpServletRequest request) {
		Locale locale = request.getLocale();
		try {
			List<QuickNavigation> quicNavList = layoutService
					.getAllQuickNavigation();
			String[] fieldNames = { "id", "name", "sortOrder", "iconPath",
					"url", "status" };
			String script = GridUtil.generateScriptForQuickNavGrid(CommonUtil
					.getMapListFromObjectListByFieldNames(quicNavList,
							fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
			log.info("Success to displayed manageQuickNavigation");
		} catch (Exception e) {
		}
		return new ModelAndView("homePage/manageQuickNavigation");
	}

	/**
	 * update quickNavigation status into active or deactive,when
	 * quickNavigation set into active it will show in the home page
	 * quickNavigation widget
	 * 
	 * @param model
	 * @param request
	 * @param status
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/updateQuickNavStatus", method = RequestMethod.GET)
	public @ResponseBody
	String updateQuickNavStatus(ModelMap model, HttpServletRequest request,
			@RequestParam("status") String status, @RequestParam("id") String id) {
		Locale locale = request.getLocale();
		try {
			QuickNavigation quickNavigation = layoutService
					.getQuickNavForId(id);
			if (status.equalsIgnoreCase("true")) {
				quickNavigation.setStatus(true);
			} else {
				quickNavigation.setStatus(false);
			}
			layoutService.saveQuickNavigation(quickNavigation);
			log.info("QuickNavigation Status updated Successfuly");
		} catch (Exception e) {
			saveError(
					request,
					getText("quickNav.status.update.error", e.getMessage(),
							locale));
		}
		return "";
	}

	@RequestMapping(value = "bpm/admin/quickNavigationDelete", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, Object> deleteSelectedTriggers(
			@RequestParam("quickNavigation") String quickNavigation,
			HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		Map<String, Object> message = new HashMap<String, Object>();
		try {

			List<String> quickIdList = new ArrayList<String>();

			if (quickNavigation.contains(",")) {
				String[] ids = quickNavigation.split(",");
				for (String name : ids) {
					quickIdList.add(name);
				}
			} else {
				quickIdList.add(quickNavigation);
			}
			layoutService.deleteSelectedQuickNav(quickIdList);
			message.put("successMessage", getText("success.quickNav.delete", locale));
			// saveMessage(request, getText("success.quickNav.delete",locale));
			log.info("QuickNavigation Deleted Successfuly");
		} catch (Exception e1) {
			message.put("errorMessage",
					getText("error.quicknav.delete", locale));
			// saveError(request, getText("error.trigger.delete",
			// e1.getMessage(), locale));
			e1.printStackTrace();
		}
		return message;
	}

	@RequestMapping(value = "/bpm/homePage/showJspContent", method = RequestMethod.GET)
	public @ResponseBody
	ModelAndView showListViewGrid(
			@RequestParam("contentLink") String contentLink,
			@RequestParam("container") String container,
			HttpServletResponse response, ModelMap model,
			HttpServletRequest request) throws Exception {
		String script = null;
		Locale locale = request.getLocale();
		try {

			if (script == null || script.isEmpty()) {
				script = getText("listview.column.not.exist", locale);
			}
			model.addAttribute("script", script);
			model.addAttribute("success", true);
			log.info("Success generating List View");
		} catch (BpmException e) {
			log.error(e.getMessage(), e);
			model.addAttribute("success", false);
			saveError(request, getText("list.view.error", e.getMessage(), locale));
			model.addAttribute("script", "Problem loading grid. " + e.getMessage());
		} catch (Exception e) {
			model.addAttribute("success", false);
			model.addAttribute("script", "Problem loading grid. " + e.getMessage());
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("listview/showListViewGrid", model);
		// return script;
	}

	@RequestMapping(value = "/bpm/admin/getSelectedWidgets", method = RequestMethod.GET)
	public @ResponseBody
	String getSelectedWidgets() {

		String selectedWidgets = "";
		try {
			Layout layout = layoutService.getActiveLayout();
			selectedWidgets = layout.getColumnWidget();
			return selectedWidgets;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	@RequestMapping(value = "/bpm/admin/deleteSelectedWidgets", method = RequestMethod.GET)
	public @ResponseBody	String deleteSelectedWidgets(@RequestParam("widgetIdDelete") String widgetIdDelete,	@RequestParam("widgetDeleteType") String widgetDeleteType,
			@RequestParam("assignedTo") String assignedTo,		HttpServletRequest request) {
		ArrayList<String> widgetsAfterDel = new ArrayList<String>();
		Locale locale = request.getLocale();
		String afterDeleteWidgetIds = "";
		String widgetString = "";
		try {
			if (widgetDeleteType.equals("Home")) {
				String widgetNames = "";
				int noOfColumn = 0;
				String[] widgetIds;
				Layout layout = new Layout();
				UserWidget widget = widgetService.getUserWidgetForId(CommonUtil.getLoggedInUserId());
				if (widget != null) {
					widgetNames = widget.getWidgetId();
					noOfColumn = widget.getNoOfWidget();
				} else {
					layout = layoutService.getLoggedInUserLayout();
					if (layout != null) {
						widgetNames = layout.getColumnWidget();
						noOfColumn = layout.getNoOfWidget();
					}
				}
				log.info("SelectedWidgets deleted Successfuly with Ids" + widgetNames);
				
				if(widgetNames.contains(",")){
						widgetIds = widgetNames.split(",");
					for (int i = 0; i < widgetIds.length; i++) {
						if (!widgetIds[i].equals(widgetIdDelete)) {
							widgetsAfterDel.add(widgetIds[i]);
						}
					}
					afterDeleteWidgetIds = widgetsAfterDel.toString();
					afterDeleteWidgetIds = afterDeleteWidgetIds.replace("[", "");
					afterDeleteWidgetIds = afterDeleteWidgetIds.replace("]", "");
					afterDeleteWidgetIds = afterDeleteWidgetIds.replace(" ", "");
					log.info("After Deleted the widget Ids " + afterDeleteWidgetIds);
					widgetService.saveUserWidget(afterDeleteWidgetIds,
							CommonUtil.getLoggedInUserId(), noOfColumn);
					return afterDeleteWidgetIds;
				}else{
					afterDeleteWidgetIds = "";
					log.info("After delete the widgetIds "+afterDeleteWidgetIds);
					widgetService.saveUserWidget(afterDeleteWidgetIds,
							CommonUtil.getLoggedInUserId(), noOfColumn);
					return "";
				}
			} else {
				String[] eachWidgetId;
				String selectedWidgets = "";
				Layout layout = layoutService.getLayoutByAssignedTo(assignedTo);
				selectedWidgets = layout.getColumnWidget();
				if(selectedWidgets.contains(",")){
					eachWidgetId = selectedWidgets.split(",");
					for (int widgetId = 0; widgetId < eachWidgetId.length; widgetId++) {
						if (!eachWidgetId[widgetId].equals(widgetIdDelete)) {
							widgetsAfterDel.add(eachWidgetId[widgetId]);
	
						}
					}
					widgetString = widgetsAfterDel.toString();
					widgetString = widgetString.replace("[", "");
					widgetString = widgetString.replace("]", "");
					widgetString = widgetString.replace(" ", "");
					layout.setColumnWidget(widgetString);
					layoutService.saveLayout(layout);
					return widgetString;
				}else{
					layout.setColumnWidget(widgetString);
					layoutService.saveLayout(layout);
					return "";
				}
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveError(
					request,
					getText("widget.delete.error",
							e.getMessage(), locale));
		}
		return "";
	}

	/**
	 * show manage layout org tree root nodes.
	 * 
	 * @param model
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/home/getHomePageRootNodes", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> getHomePageRootNodes(ModelMap model, HttpServletRequest request) {
		log.info("Getting homepage org tree root nodes");
		List<Map<String, Object>> nodeListOfMap = new ArrayList<Map<String, Object>>();
		try {
			CommonUtil.createRootTreeNode(nodeListOfMap, "Public", "public", "公共");
			CommonUtil.createRootTreeNode(nodeListOfMap, "Roles", "roles", "角色");
			CommonUtil.createRootTreeNode(nodeListOfMap, "Departments", "departments", "部门");
		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
		return nodeListOfMap;
	}

	/**
	 * get child node of selected tree
	 * 
	 * @param model
	 * @param currentNode
	 * @param rootNode
	 * @param nodeLevel
	 * @param errors
	 * @param request
	 * @param response
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/getHomePageChildNodes", method = RequestMethod.GET)
	public @ResponseBody String getUserSelectionChildNodes(ModelMap model, @RequestParam("currentNode") String currentNode, @RequestParam("rootNode") String rootNode, @RequestParam("nodeLevel") String nodeLevel, User user, BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
		log.info("Getting homepage org tree child nodes of " + currentNode);
		Locale locale = request.getLocale();
		String nodes = "";
		try {
			if (rootNode.equalsIgnoreCase("Roles")) {
				nodes = layoutService.getRolesChildNode(currentNode, Integer.parseInt(nodeLevel));
			} else if (rootNode.equalsIgnoreCase("Departments")) {
				nodes = layoutService.getDepartmentsChildNode(currentNode, Integer.parseInt(nodeLevel));
			}
			log.info("Homepage child nodes is Selected Successfully ");
		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
			saveError(request, getText("errors.userSelection.getNodes", locale));
		} catch (Exception e) {
			saveError(request, getText("errors.userSelection.getNodes", locale));
			log.warn(e.getMessage(), e);
		}
		return nodes;
	}

	@Autowired
	public void setLayoutService(LayoutService layoutService) {
		this.layoutService = layoutService;
	}

	@Autowired
	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}
	
	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	@Autowired
	public void setWidgetService(WidgetService widgetService) {
		this.widgetService = widgetService;
	}

	@Autowired
	public void setDocumentFormService(DocumentFormService documentFormService) {
		this.documentFormService = documentFormService;
	}
	
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    @Autowired
    public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getIsUpdate() {
		return isUpdate;
	}

}
