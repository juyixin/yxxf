package com.eazytec.bpm.admin.layout.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.admin.department.service.DepartmentService;
import com.eazytec.bpm.admin.layout.dao.LayoutDao;
import com.eazytec.bpm.admin.layout.service.LayoutService;
import com.eazytec.bpm.admin.menu.service.MenuService;
import com.eazytec.bpm.admin.role.service.RoleService;
import com.eazytec.bpm.runtime.listView.service.ListViewService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Layout;
import com.eazytec.model.Menu;
import com.eazytec.model.QuickNavigation;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.service.impl.GenericManagerImpl;

/**
 * 
 * @author revathi
 * 
 */

@Service("layoutService")
public class LayoutServiceImpl extends GenericManagerImpl<Layout, String>
		implements LayoutService {
	private LayoutDao layoutDao;
	private MenuService menuService;
	private RoleService roleService;
	private DepartmentService departmentService;
	private ListViewService listViewService;

	@Autowired
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@Autowired
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * {@inheritDoc}
	 */
	public Layout saveLayout(Layout layout) throws BpmException {
		// layout.setId(layout.getName().toString().trim().toLowerCase().replaceAll("\\s+",
		// " ").replaceAll(" ", "_"));
		// layout.setName(layout.getName().toString().trim().toLowerCase().replaceAll("\\s+",
		// " ").replaceAll(" ", "_").concat("123"));
		return layoutDao.saveLayout(layout);

	}

	/**
	 * {@inheritDoc}
	 */
	public Layout saveLayout(Layout layout, String assignedTo,
			String departments, String roles) throws BpmException {
		log.info("saving layout");
		Set<String> assignedTos = new HashSet<String>();
		if (assignedTo != null && assignedTo.length() > 0) {
			assignedTos.add(assignedTo);
		}
		if (departments != null && departments.length() > 0
				&& departments.contains(",")) {
			List<String> departmentIds = new ArrayList<String>(
					Arrays.asList(departments.split(",")));
			assignedTos.addAll(departmentIds);
		} else if (departments != null && departments.length() > 0) {
			assignedTos.add(departments);
		}
		if (roles != null && roles.length() > 0 && roles.contains(",")) {
			List<String> roleIds = new ArrayList<String>(Arrays.asList(roles
					.split(",")));
			assignedTos.addAll(roleIds);
		} else if (roles != null && roles.length() > 0) {
			assignedTos.add(roles);
		}
		log.info("assigned to list size" + assignedTos.size());
		if (assignedTos.size() > 0) {
			layoutDao.deleteLayoutsByAssignedTos(new ArrayList<String>(
					assignedTos));
			for (String assign : assignedTos) {
				Layout newLayout = new Layout(layout.getName(),
						layout.getNoOfWidget(), layout.getStatus(),
						layout.getColumnWidget(), layout.getMenuType(),
						layout.getMenuType(), assign);

				layoutDao.saveLayout(newLayout);
			}
		}
		return layout;

	}

	/**
	 * {@inheritDoc}
	 */
	public List<Layout> getAllLayout() throws EazyBpmException {
		return layoutDao.getAllLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	public Layout getLayoutForId(String id) throws EazyBpmException {
		return layoutDao.getLayoutForId(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public Layout getActiveLayout() throws EazyBpmException {
		return layoutDao.getActiveLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	public Layout getLayoutByAssignedTo(String assignedTo)
			throws EazyBpmException {
		return layoutDao.getLayoutByAssignedTo(assignedTo);
	}

	/**
	 * {@inheritDoc}
	 */
	public QuickNavigation getQuickNavForId(String id) throws EazyBpmException {
		return layoutDao.getQuickNavForId(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<QuickNavigation> getAllQuickNavigation() throws EazyBpmException {
		List<QuickNavigation> quickNavigations = layoutDao.getAllQuickNavigation();
		List<QuickNavigation> newQuickNavigations = new ArrayList<QuickNavigation>();
		Menu menu = new Menu();
		for (QuickNavigation quickNavigation : quickNavigations) {
			String menuId = quickNavigation.getMenuId();
			menu = menuService.getMenuById(menuId);
			String menuUrl = "";
			if (menu.getUrlType().equals("script")) {
				menuUrl = menu.getMenuUrl();
				menuUrl = menuUrl.replace('"', '\'');
				quickNavigation.setMenuLink(menuUrl);
			} else if (menu.getUrlType().equals("url")) {
				menuUrl = menu.getMenuUrl();
				quickNavigation.setMenuLink(menuUrl);
			} else if (menu.getUrlType().equals("newTab")) {
				menuUrl = menu.getMenuUrl();
				quickNavigation.setMenuLink(menuUrl);
			}
			quickNavigation.setMenuType(menu.getUrlType());
			newQuickNavigations.add(quickNavigation);
		}
		return newQuickNavigations;

	}

	/**
	 * {@inheritDoc}
	 */
	public QuickNavigation saveQuickNavigation(QuickNavigation quickNav)
			throws BpmException {
		/*
		 * if(quickNav.getId()== null || quickNav.getId().isEmpty()){
		 * quickNav.setId(quickNav.getName().toString().trim().toLowerCase()); }
		 */
		return layoutDao.saveQuickNavigation(quickNav);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<QuickNavigation> getAllActiveQuickNavigation() throws EazyBpmException {

		List<QuickNavigation> quickNavigations = layoutDao.getAllActiveQuickNavigation();
		List<QuickNavigation> newQuickNavigations = new ArrayList<QuickNavigation>();
		String menuUrl = "";
		Menu menu = new Menu();
		//ListView listViewObj = null;
		for (QuickNavigation quickNavigation : quickNavigations) {
			 String urlType = quickNavigation.getUrlType();
			/* String viewName = quickNavigation.getListViewName();
			 listViewObj = listViewService.getListViewByName(viewName);*/
			String menuId = quickNavigation.getMenuId();		
			menu = menuService.getMenuById(menuId);
			if (urlType.equals("menu") && menu != null) {
				if (menu.getUrlType().equals("script")) {
					menuUrl = menu.getMenuUrl();
					menuUrl = menuUrl.replace('"', '\'');
					quickNavigation.setMenuLink(menuUrl);
				} else if (menu.getUrlType().equals("url")) {
					menuUrl = menu.getMenuUrl();
					quickNavigation.setMenuLink(menuUrl);
				} else if (menu.getUrlType().equals("newTab")) {
					menuUrl = menu.getMenuUrl();
					quickNavigation.setMenuLink(menuUrl);
				}
				quickNavigation.setMenuType(menu.getUrlType());
			} else if (urlType.equals("process") && !quickNavigation.getProcessName().isEmpty()) {
					quickNavigation.setUrlType("process");
					quickNavigation.setMenuLink(quickNavigation.getProcessName());
			} else if((urlType.equals("listViewQuick") || urlType.equals("listView")) && !quickNavigation.getListViewName().isEmpty()) {
				quickNavigation.setUrlType("listViewQuick");
				quickNavigation.setMenuLink(quickNavigation.getListViewName());
			}

			newQuickNavigations.add(quickNavigation);
		}
		return newQuickNavigations;

	}

	public List<QuickNavigation> getQuickNavigationsByNames(List<String> names) {
		return layoutDao.getQuickNavigationsByNames(names);
	}

	public void removeQuickNavigation(String Id) {
		layoutDao.removeQuickNavigation(Id);
	}

	public void deleteSelectedQuickNav(List<String> ids) throws Exception {
		try {
			List<QuickNavigation> quickNavigations = layoutDao
					.getQuickNavigationsByNames(ids);
			for (QuickNavigation quickNavigation : quickNavigations) {
				removeQuickNavigation(quickNavigation.getId());
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public LayoutDao getLayoutDao() {
		return layoutDao;
	}

	@Autowired
	public void setLayoutDao(LayoutDao layoutDao) {
		this.layoutDao = layoutDao;
	}

	@Autowired
	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public int getnoOfWidget() throws EazyBpmException {
		int noOfWidget = 0;
		// return noOfWidget;
		return layoutDao.getnoOfWidget();

	}

	/**
	 * get Role child nodes
	 * 
	 * @param currentNode
	 * @param nodeLevel
	 * 
	 * @return
	 */
	public String getRolesChildNode(String currentNode, int nodeLevel)
			throws BpmException, Exception {
		List<LabelValue> childNodeList = new ArrayList<LabelValue>();
		String nodes = "";
		if (nodeLevel == 1) {
			List<Role> roleList = roleService.getAllUserRoles(CommonUtil.getLoggedInUser());
			for(Role role : roleList){
				LabelValue labelValue = new LabelValue();
				labelValue.setLabel(role.getViewName());
				labelValue.setValue(role.getId());
				childNodeList.add(labelValue);
			}
			nodes = formUserSelectionNodes(childNodeList, "Role");
		}
		return nodes;
	}
	
	public boolean graduationForWidget(String currentNode) throws Exception{
		List<String> roleIds = new ArrayList<String>();
		List<String> departmentChildList = new ArrayList<String>();
		try{
			User loggedInUser = CommonUtil.getLoggedInUser();
			roleIds = loggedInUser.getRoleIds();
			if(roleIds.contains(currentNode) || roleIds.contains("ROLE_ADMIN") || currentNode.equalsIgnoreCase("public")){
				return true;
			}
			departmentChildList = layoutDao.getAllRelativeDepartments(loggedInUser.getDepartment().getId());
			if(departmentChildList != null){
				String deapartmentChilds = departmentChildList.get(0);
				List<String> departmentList = new ArrayList<String>(Arrays.asList(deapartmentChilds.split(","))); 
				if(departmentList.contains(currentNode)){
					return true;
				}
			}
		} catch(Exception e){
			log.info("Exception at the palce of graduation "+e.getMessage());
		}
		return false;
	}
	
	public List<String> getAllRelativeDepartments(String departmentId) throws Exception {
		return layoutDao.getAllRelativeDepartments(departmentId);
	}

	/**
	 * get Department child nodes
	 * 
	 * @param currentNode
	 * @param nodeLevel
	 * 
	 * @return
	 */
	public String getDepartmentsChildNode(String currentNode, int nodeLevel)
			throws BpmException, Exception {
		List<LabelValue> childNodeList = new ArrayList<LabelValue>();
		String nodes = "";
		if (nodeLevel == 1) {
			childNodeList = departmentService.getOrganizationAsLabelValue();
			nodes = formUserSelectionNodes(childNodeList, "Department");
		} else if (nodeLevel == 2 || nodeLevel == 3) {
			childNodeList = departmentService
					.getDepartmentsAsLabelValueByParent(currentNode);
			nodes = formUserSelectionNodes(childNodeList, "Department");
		}
		return nodes;
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
	public String formUserSelectionNodes(List<LabelValue> childNodeList,
			String root) throws BpmException, Exception {
		JSONArray nodes = new JSONArray();
		JSONObject jsonNodes = new JSONObject();
		Map<String, Object> attr = new HashMap<String, Object>();
		Map<String, Object> nodeDetails = new HashMap<String, Object>();
		List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
		if (childNodeList != null && childNodeList.size() > 0) {
			for (LabelValue node : childNodeList) {
				attr.put("id", node.getValue().replace(" ", ""));
				attr.put("name", node.getValue());
				attr.put("isUser", false);
				attr.put("root", root);
				nodeDetails.put("data", node.getLabel());
				nodeDetails.put("attr", attr);
				nodeList.add(nodeDetails);
				jsonNodes = CommonUtil.getOrganizationTreeNodes(nodeList);
				nodes.put(jsonNodes);
			}
		}
		return nodes.toString();
	}

	/**
	 * get loggedInUserLayout
	 * 
	 * @return
	 */
	public Layout getLoggedInUserLayout() throws EazyBpmException {
		Layout layout = getLayoutByAssignedTo("Public");
		Layout resultLayout = new Layout();
		resultLayout.setNoOfWidget(layout.getNoOfWidget());
		List<String> layoutWidgets = new ArrayList<String>();
		String widgetIds = "";
		User loggedInUser = CommonUtil.getLoggedInUser();
		List<String> assignedTo = new ArrayList<String>();
		assignedTo.add("Public");
		assignedTo.addAll(loggedInUser.getRoleIds());
		if (loggedInUser.getDepartment() != null) {
			assignedTo.add(loggedInUser.getDepartment().getId());
		}
		List<Layout> layouts = layoutDao.getLayoutsByAssignedTos(assignedTo);
		if (layouts != null) {
			for (Layout newLayout : layouts) {
				List<String> widgets = new ArrayList<String>();
				
				if(StringUtils.isNotBlank(newLayout.getColumnWidget())){
					widgets = Arrays.asList(newLayout.getColumnWidget().split(","));
				}
				
				layoutWidgets.addAll(widgets);
			}
			if (!layoutWidgets.isEmpty()) {
				Set<String> widgetSet = new HashSet<String>(layoutWidgets);
				if (!widgetSet.isEmpty()) {
					for (String widgetId : widgetSet) {
						if (widgetIds == "" && widgetId != "") {
							widgetIds = widgetId;
						} else if (widgetId != "") {
							widgetIds = widgetIds + ',' + widgetId;
						}
					}
				}
				resultLayout.setColumnWidget(widgetIds);
			}
		}
		return resultLayout;
	}

}