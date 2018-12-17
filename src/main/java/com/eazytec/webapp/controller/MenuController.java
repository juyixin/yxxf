/**
 * 
 */
package com.eazytec.webapp.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.department.service.DepartmentService;
import com.eazytec.bpm.admin.group.service.GroupService;
import com.eazytec.bpm.admin.menu.service.MenuService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.dao.SearchException;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.Department;
import com.eazytec.model.Group;
import com.eazytec.model.Menu;
import com.eazytec.model.Role;
import com.eazytec.service.MenuExistsException;
import com.eazytec.service.UserService;

/**
 * @author mathi
 *
 */

@Controller
public class MenuController extends BaseFormController{

	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    
    public VelocityEngine velocityEngine;
    
    private MenuService menuService;
    
    private GroupService groupService;
    
    private DepartmentService departmentService;
    
    private UserService userService;
    
    public UserService getUserService() {
		return userService;
	}
    @Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
    public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@Autowired
    public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}
    
	@Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }
	
    @Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
	
    /**
     * method for create/edit menu
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "bpm/admin/manageMenus",method = RequestMethod.GET)
	public ModelAndView showMenuTree(HttpServletRequest request,ModelMap model) {
    	model.addAttribute("menu",new Menu());
    	return new ModelAndView("admin/manageMenus",model);
	}
    
    
	/**
     * method for create/edit menu
     * 
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "bpm/admin/menuForm",method = RequestMethod.GET)
	public ModelAndView showMenuForm(HttpServletRequest request,ModelMap model) {
    	String roles = "";
    	String groups ="";
    	String departments = "";
    	String users = "";
    	Set<Department> adminDeptartment = CommonUtil.getLoggedInUser().getDepartmentByAdministrators();    
    	String departmentAdmin = adminDeptartment.toString().replaceAll("\\[", "").replaceAll("\\]","");    	
		

    	if(request.getParameter("id") != null){
    		Menu menu = menuService.getMenu(request.getParameter("id"));   
    		
    		if(menu.getRoles() != null){
        		for(Role role : menu.getRoles()){
        		    if(role.getName().endsWith("GROUP")){
    		    		Group group = groupService.getGroupIdByGroupRole(role.getName());
        		    	if(groups.length() == 0) {
        		    		groups += group.getName();
        		    	}else {
        		    		groups += "," + group.getName();
        		    	}
        		    } else if(role.getName().endsWith("DEPARTMENT")) {
        		    	Department department = departmentService.getDepartmentIdByDepartmentRole(role.getName());
        		    	if(departments.length() == 0) {
        		    		departments += department.getName();
        		    	}else {
        		    		departments += "," + department.getName();
        		    	}
        		    } else if(role.getName().endsWith("USER") && role.getRoleType().equalsIgnoreCase("Internal")) {
        		    	String userId = userService.getUserIdByUserRole(role.getName());
        		    	if(!userId.isEmpty() || userId != null) {
            		    	if(users.length() == 0) {
            		    		users += userId;
            		    	}else {
            		    		users += "," + userId;
            		    	}
        		    	}
        		    } else {
        		    	if(roles.length() == 0 ) {
            				roles += role.getName();
            		    } else {
            		    	roles += "," + role.getName();
            		    }
        		    }
        		}
        	}
    		model.addAttribute("roles",roles);
    		model.addAttribute("groups",groups);
    		model.addAttribute("departments",departments);
    		model.addAttribute("users",users);
    		model.addAttribute("menu", menu);
    		if(menu.getParentMenu() != null){
        		model.addAttribute("parentMenu", menu.getParentMenu().getId());
        	}
        	model.addAttribute("isActive", menu.isActive());
    	}else{
    		model.addAttribute("departments",departmentAdmin);
    		model.addAttribute("menu",new Menu());
    	}
      return new ModelAndView("admin/menuForm",model);
	   }
    
    /**
     * method for save/update menu
     * 
     * @param menu
     * @param model
     * @return
     */
    @RequestMapping(value = "bpm/admin/saveMenu", method = RequestMethod.POST)
   	public ModelAndView saveMenu(HttpServletRequest request, @ModelAttribute("menu") Menu menu,
   			BindingResult errors, ModelMap model) {
    	Locale locale = request.getLocale();
    	String roles = request.getParameter("menuRoles");
    	String groups = request.getParameter("menuGroups");
    	String departments = request.getParameter("menuDepartments");
    	String users = request.getParameter("menuUsers");
    	String parentMenuValue = request.getParameter("parentMenu");
    	Set<Role> roleSet = new HashSet<Role>();
   		try {
   			if(menu.getId() == null || menu.getId() == ""){
   				menu.setId(null);
   			}
	        if (validator != null) {
	        	validator.validate(menu, errors);
	            if (errors.hasErrors()) {
	            	if(menu.getParentMenu() != null){
	            		model.addAttribute("parentMenu", menu.getParentMenu().getName());
	            	}
					model.addAttribute("roles",roles);
	            	model.addAttribute("isActive", menu.isActive());
	            	model.addAttribute("initial","false");
	            	return new ModelAndView("admin/menuForm",model);
	            }
	        }
   			if(menu.getIsParent()==true && (menu.getMenuType().equals("top") || menu.getMenuType().equals("header"))){
				menu.setParentMenu(null);
   			}else{
   				Menu parentMenu = menuService.getMenu(String.valueOf(menu.getParentMenu().getName()));
   				menu.setParentMenu(parentMenu);
   			}
   			
   			if(roles != null && !roles.isEmpty()){
	   			if (roles.contains(",")) {
	   				String[] ids = roles.split(",");
	   				for(String userid :ids){
	   					roleSet.add(new Role(userid, userid));
	   				}
	   			} else {
	   				roleSet.add(new Role(roles, roles));
	   			}
   			}
   			
   			//Get Group role
   			if(groups != null && !groups.isEmpty()) {
   					String[] groupIds = groups.split(",");
   					for(String groupId : groupIds) {
   						Group group = groupService.getGroupById(groupId);
   						if(group != null) {
   	   						roleSet.add(new Role(group.getGroupRole(),group.getGroupRole()));
   						}
   					}
   			}
   			
   			//Get department role
   			if(departments != null && !departments.isEmpty()) {
   					String[] departmentIds = departments.split(",");
   						for(String departmentId : departmentIds) {
   							Department department = departmentService.getDepartmentById(departmentId);
   							if(department != null) {
   	   							roleSet.add(new Role(department.getDepartmentRole(),department.getDepartmentRole()));
   							}
   						}
   			}
   			
   			if(users != null && !users.isEmpty()) {
					String[] userIds = users.split(",");
						for(String userId : userIds) {
							String userRole = userService.getUserRoleByUserId(userId);
							//User user = userService.getUserById(userId);
							if(userRole != null || !userRole.isEmpty()) {
	   							roleSet.add(new Role(userRole,userRole));
							}
						}
			}   			
   			
			if(!roleSet.isEmpty()) {
   				menu.setRoles(roleSet);
   			}else {
   				menu.setRoles(null);
   			}
   			
			if(menu.getId() == null || menu.getId() == ""){
				saveMessage(request, getText("menu.added", menu.getName(), locale));
				log.info(getText(menu.getName()+" "+"menu.add", locale));
			}else{
				saveMessage(request, getText("menu.saved", menu.getName(), locale));
				log.info(getText(menu.getName()+" "+"menu.save", locale));			
			}
			
			menuService.saveMenu(menu);
			model.addAttribute("parentMenu", parentMenuValue);
			model.addAttribute("groups",groups);
			model.addAttribute("departments",departments);
			model.addAttribute("users",users);
			model.addAttribute("roles",roles);
			reloadContext(request.getSession().getServletContext());
		} catch (MenuExistsException e) {
			// TODO Auto-generated catch block
			errors.rejectValue("name", "errors.save.menu");
			if(menu.getParentMenu() != null){
        		model.addAttribute("parentMenu", menu.getParentMenu().getName());
        	}
			model.addAttribute("roles",roles);
			model.addAttribute("isActive", menu.isActive());
			model.addAttribute("initial","false");
			return new ModelAndView("admin/menuForm",model);
		} catch (Exception e) {
			errors.rejectValue("name", "errors.save.menu");
			if(menu.getParentMenu() != null){
        		model.addAttribute("parentMenu", menu.getParentMenu().getName());
        	}
            model.addAttribute("roles",roles);
            model.addAttribute("groups",groups);
            model.addAttribute("departments",departments);
			model.addAttribute("isActive", menu.isActive());
			model.addAttribute("initial","false");
			return new ModelAndView("admin/menuForm",model);
		}
   		//return new ModelAndView("redirect:/logout");
		return new ModelAndView("admin/menuForm",model);
      }
    
    /**
     * method to show list of menus are available
     * 
     * @param query
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "bpm/admin/menus", method = RequestMethod.GET)
    public ModelAndView showMenuList(@RequestParam(required = false, value = "q") String query) throws Exception {
        Model model = new ExtendedModelMap();
        try {
        	List<Menu> menuList=menuService.getMenus();
            model.addAttribute(Constants.MENU_LIST, menuService.getMenus());
            String script=GridUtil.generateScriptForMenuGrid(CommonUtil.getMapListFromObjectList(menuList), velocityEngine);
            model.addAttribute("script", script);
        } catch (SearchException se) {
            model.addAttribute("searchError", se.getMessage());
            model.addAttribute(menuService.getMenus());
        }
        return new ModelAndView("admin/menuList", model.asMap());
    }
    
    /**
	 * Get tree node for Menu
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
	 @RequestMapping(value="bpm/admin/getTreeNode", method = RequestMethod.GET)
	 public @ResponseBody List<Map<String, Object>> getTreeNode(ModelMap model,@RequestParam("currentNode") String currentNode, @RequestParam("rootNode") String rootNode, @RequestParam("nodeLevel") int nodeLevel){
		List<Map<String, Object>> nodeListOfMap = new ArrayList<Map<String, Object>>();
    	try {
    		if(nodeLevel == 0){
    			CommonUtil.createMenuRootTreeNode(nodeListOfMap, "Header", "header", "顶部菜单","Header");
    			CommonUtil.createMenuRootTreeNode(nodeListOfMap, "Top", "top", "主菜单","Top");
    		}else if(nodeLevel == 1){
    			
    			List<Menu> menuList = menuService.getMenus();
    			for(Menu menu : menuList){
    				String menuType = rootNode.toLowerCase();
        			if(menu.getIsParent() == true && menu.getMenuType().equals(menuType)){	
        				CommonUtil.createMenuRootTreeNode(nodeListOfMap, menu.getId(), menu.getId().replace(".",""), menu.getName(),menu.getMenuType());
        			}
    			}
    		}else{
    			List<Menu> menuList = menuService.getMenus();
    			for(Menu menu : menuList){
    				if(menu.getParentMenu() != null){
    					if(String.valueOf(menu.getParentMenu().getId()).equals(currentNode) && String.valueOf(menu.getMenuType()).equals("side")){		
        					CommonUtil.createMenuRootTreeNode(nodeListOfMap, menu.getId(), menu.getId().replace(".",""), menu.getName(),menu.getMenuType());
            			}
    				}
    			}
    			
    		}
    	} catch (BpmException e) {
			log.warn(e.getMessage(),e);
		} catch (Exception e) {
			log.warn(e.getMessage(),e);
    	}
		
    	return nodeListOfMap;
	}
	 
    
    /**
   	 * To get menu icons through ajax call
   	 * @return icons url
   	 */
   	 @RequestMapping(value="bpm/admin/getMenuIcons", method = RequestMethod.GET)
   	   public @ResponseBody List<String> getMenuIcons(HttpServletRequest request) {
   		  List<String> menuIcons = new ArrayList<String>();
   		  try{
   			  String menuIconDir = getServletContext().getRealPath("/images/sidemenu");
   			  File iconDir = new File(menuIconDir);
   			  for(File icon : iconDir.listFiles()){  
                menuIcons.add("/images/sidemenu/"+icon.getName());
              }
        	 log.info("getting menu icon list Retrived Successfully");
   			  return menuIcons;
   		  }catch(Exception e){
   			  log.error("Error while getting menu icon list ",e);
   		  }		 
   	      return null;
   	   }
   	/**
 	 * Show the menu organization tree
 	 * @param title
 	 * @param requestorId
 	 * @param selectType
 	 * @param model
 	 * @param request
 	 * @return
 	 * @throws Exception
 	 */
 	@RequestMapping(value="bpm/admin/menuTree", method = RequestMethod.GET)
     public ModelAndView showMenuTree(@RequestParam("title") String title,@RequestParam("id") String requestorId,@RequestParam("selectType") String selectType,@RequestParam("selectedMenu") String selectedMenu,ModelMap model,HttpServletRequest request) throws Exception {
     	List<Menu> menuList = menuService.getActiveMenus();
     	model.addAttribute("menu", menuList);
     	model.addAttribute("title",title);
     	model.addAttribute("requestorId",requestorId);
     	model.addAttribute("selectType",selectType);
    	model.addAttribute("selectedMenu",selectedMenu);
    	model.addAttribute("menu",new Menu());
     	model.addAttribute("appendTo","menuId");
     	model.addAttribute("selection","widgetImportFiles");
     	return new ModelAndView("/admin/showMenuTree", model);
     }
 	
 	 /**
	 * method for delete the selected departments from department grid
	 * 
	 * @param departmentIds
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/deleteMenu", method = RequestMethod.GET)
	public ModelAndView deleteMenu(@RequestParam("menuId") String menuId, HttpServletRequest request) {
		log.info("Going to delete menu : "+menuId);
		Locale locale = request.getLocale();
		try{
			saveMessage(request, getText("success.menu.delete",locale));
			menuService.deleteMenu(menuId);
			reloadContext(request.getSession().getServletContext());
			log.info(getText("success.menu.delete",locale));
		} catch (BpmException e) {
			log.error(e.getMessage(), e);
			saveError(request, getText("error.menu.delete",e.getMessage(),locale));
		} 
//		return new ModelAndView("redirect:/logout");
		return new ModelAndView("redirect:/bpm/admin/manageMenus");
	}
	
	
   	/**
   	 * reload the context
   	 * 
   	 * @param context
   	 */
   	private void reloadContext(ServletContext context){
   		context.setAttribute(Constants.AVAILABLE_MENUS, new HashSet<Menu>(menuService.getMenus()));
   		context.setAttribute(Constants.AVAILABLE_TOP_MENUS, new HashSet<Menu>(menuService.getTopMenus()));
   		context.setAttribute(Constants.AVAILABLE_PARENT_SIDE_MENUS, new HashSet<Menu>(menuService.getParentSideMenus()));
	}
}
