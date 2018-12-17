package com.eazytec.webapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.InvalidNameException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.bpm.admin.department.service.DepartmentService;
import com.eazytec.bpm.admin.group.service.GroupService;
import com.eazytec.bpm.admin.menu.service.MenuService;
import com.eazytec.bpm.admin.role.service.RoleService;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.dto.DepartmentDTO;
import com.eazytec.dto.GroupDTO;
import com.eazytec.dto.RoleDTO;
import com.eazytec.dto.UserDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.Department;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Menu;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.service.UserManager;
import com.eazytec.service.UserService;
import com.eazytec.webapp.util.ValidationUtil;


/**
 * Simple class to retrieve a list of users from the database.
 *
 * @author madan
 */
@Controller
public class UserController extends BaseFormController{
	
	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    
    private RoleService roleService;
    
    private GroupService groupService;
    
    private DepartmentService departmentService;
    
    private UserManager userManager;
    
    public VelocityEngine velocityEngine;
    
    private UserService userService;
    
    private MenuService menuService;
    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
  		this.departmentService = departmentService;
  	}
    
    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }
    
    @Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
    
    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    
    @Autowired
    public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value="bpm/admin/usersList", method = RequestMethod.GET)
    public ModelAndView usersList(@RequestParam("taskId") String taskId, @RequestParam("type") String type,ModelMap model,HttpServletRequest request) throws Exception {
    	
    	model.addAttribute("taskId", taskId);
    	model.addAttribute("type",type);
    	model.addAttribute("involveMembers", getText("task.involve.members", request.getLocale()));    
    	log.info("User List Displayed Successfully");
    	return new ModelAndView("addMembers", model);
    }
    
    @RequestMapping(value="bpm/admin/usersTreeList", method = RequestMethod.GET)
    public ModelAndView usersTreeList(@RequestParam("title") String title,@RequestParam("id") String requestorId,@RequestParam("selectType") String selectType,ModelMap model) throws Exception {
    	List<RoleDTO> roleList=roleService.getRolesDTO();
    	List<GroupDTO> groupList = groupService.getGroupsDTO();
    	List<DepartmentDTO> departmentList = departmentService.getAllDepartmentDTO();
    	model.addAttribute("roles", roleList);
    	model.addAttribute("groups", groupList);
    	model.addAttribute("departments", departmentList);
    	model.addAttribute("title",title);
    	model.addAttribute("requestorId",requestorId);
    	model.addAttribute("selectType",selectType);
    	log.info("usersTreeList Retrived Successfully");
    	return new ModelAndView("showUserTreeList", model);
    }
    
    /**
	 * show user selection popup with user tree
	 * 
	 * @param model
	 * @param selectionType
	 * @param appendTo
	 * @param callAfter
	 * @param errors
	 * @param request
	 * @param response
	 * 
	 * @return
	 */
    @RequestMapping(value="bpm/admin/showOrganizationTree", method = RequestMethod.GET)
    public ModelAndView showOrganizationTree(ModelMap model, @RequestParam("selectionType") String selectionType, 
    		@RequestParam("appendTo") String appendTo, @RequestParam("selectedValues") String selectedValues,
    		@RequestParam("callAfter") String callAfter,
    		@RequestParam("from") String from,
    		@RequestParam("parentId") String parentId,
    		@RequestParam("selection") String selection,
    		@RequestParam("rootNodeURL") String rootNodeURL,
    		@RequestParam("childNodeURL") String childNodeURL,@RequestParam(value="currentUser", required=false) String currentUser,
    		User user,	BindingResult errors, 
    		HttpServletRequest request, HttpServletResponse response){
    	Map<String, Object> context = new HashMap<String, Object>();
    	try {
    		context.put("selection", selection);
    		if(currentUser == null){
    			context.put("currentUser", "");
    		}else{
    			context.put("currentUser", currentUser);
    		}
    		context.put("selectionType", selectionType);
    		
    		String isGraduation=request.getParameter("isGraduationTree");
    		if(isGraduation==null){
    			isGraduation="false";
    		}
    		
    		if(selection.equalsIgnoreCase("department") && isGraduation.equals("true")){
    			User logInuser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    			List<String> userAdminDep=userService.getUserAdminDepartments(logInuser);
    			if(userAdminDep!=null && !userAdminDep.isEmpty()){
    				context.put("userAdminPrivileges", CommonUtil.convertListToJson(userAdminDep));
    			}else{
    				context.put("userAdminPrivileges", "[]");
    			}
    		}else if(selection.equalsIgnoreCase("group") && isGraduation.equals("true") ){
    			User logInuser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    			List<String> userAdminGroups=userService.getUserAdminGroups(logInuser);
    			if(userAdminGroups!=null && !userAdminGroups.isEmpty()){
    				context.put("userAdminPrivileges", CommonUtil.convertListToJson(userAdminGroups));
    			}else{
    				context.put("userAdminPrivileges", "[]");
    			}
    		}else if(selection.equalsIgnoreCase("Role")){
    			if(isGraduation.equals("true")){
    				context.put("isGraduationTree", isGraduation);
    				User logInuser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        			List<String> userAdminRoles=userService.getUserAdminRoles(logInuser);
        			if(userAdminRoles!=null && !userAdminRoles.isEmpty()){
        				context.put("userAdminPrivileges", CommonUtil.convertListToJson(userAdminRoles));
        			}else{
        				context.put("userAdminPrivileges", "[]");
        			}
        		}else{
    				context.put("isGraduationTree", "false");
    				context.put("userAdminPrivileges", "[]");
    			}
    		}
    		
    		if(selection.equals("user") && !(selectedValues.isEmpty())){
    			context.put("selectedValues", convertJsonArray(selectedValues));
    		}else if(selection.equals("department")){
    			context.put("selectedValues", convertDepartmentJsonArray(selectedValues));
    		}else if(selection.equals("role")){
    			context.put("selectedValues", convertRoleJsonArray(selectedValues));
    		}else if(selection.equals("group")){
    			context.put("selectedValues", convertGroupJsonArray(selectedValues));
    		}else if(selection.equals("menuQuick")){
    		//	context.put("selectedValues", convertMenuJsonArray(selectedValues));
    			context.put("selectedValues", selectedValues);
    		}
    		else{
    			context.put("selectedValues", selectedValues);
    		}
    		context.put("needTreeCheckbox", true);
    		context.put("needContextMenu", true);
    		context.put("rootNodeURL", rootNodeURL);
    		context.put("childNodeURL", childNodeURL);
    		context.put("appendTo", appendTo);
    		String script=VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "organizationTree.vm", context);
    		model.addAttribute("selection", selection);
    		model.addAttribute("script", script);
    		model.addAttribute("appendTo",appendTo);
    		model.addAttribute("callAfter",callAfter);
    		model.addAttribute("selectionType",selectionType);
    		model.addAttribute("selectedValues",selectedValues);
    		model.addAttribute("from", from);
    		model.addAttribute("parentId", parentId);
    		model.addAttribute("currentUser", currentUser);
    	} catch (BpmException e) {
			log.warn(e.getMessage(),e);
			e.printStackTrace();
		} catch (Exception e) {
			log.warn(e.getMessage(),e);
			e.printStackTrace();
    	}
    	return new ModelAndView("admin/organizationTree", model);
    }
    
    /**
     * Convert User object to json array
     * @param users
     * @return
     * @throws JSONException
     */
	private JSONArray convertJsonArray(String selectedValues) throws JSONException {
		JSONArray userList = new JSONArray();
		List<String> userSelections = Arrays.asList(selectedValues.split("\\s*,\\s*"));
		List<String> userNames = new ArrayList<String>();
		for (String selection : userSelections) {
			if (selection.contains("@")) {
				JSONObject userDetail = new JSONObject();
				userDetail.put("name", selection);
				userDetail.put("id", selection);
				userList.put(userDetail);
			} else {
				userNames.add(selection);
			}
		}
		// List<String> userNames = Arrays.asList(selectedValues.split("\\s*,\\s*"));
		if (userNames != null && userNames.size() > 0) {
			for (String user : userNames) {
				if (!user.isEmpty()) {
					List<Object[]> userDetails = userService.getUserName(user);
					if (userDetails != null && userDetails.size() > 0) {
						for (Object[] userObj : userDetails) {
							JSONObject userDetail = new JSONObject();
							userDetail.put("id", userObj[0].toString());
							userDetail.put("name", userObj[1].toString());
							userList.put(userDetail);
						}
					} else {
						JSONObject userDetail = new JSONObject();
						userDetail.put("id", user);
						userDetail.put("name", user);
						userList.put(userDetail);
					}
				}
			}
		}
		return userList;
	}
    
	private JSONArray convertDepartmentJsonArray(String selectedValues) throws JSONException{
      	 JSONArray depList = new JSONArray();
      	 List<String> depNames = Arrays.asList(selectedValues.split("\\s*,\\s*"));
      	 if(depNames != null && depNames.size()>0){
      		 for(String depName : depNames) {
      			 if(!depName.isEmpty()) {
      				 Department dep = departmentService.getDepartmentByName(depName);
      				 if(dep != null){
	    				 JSONObject depDetail = new JSONObject();
	    				 depDetail.put("id",dep.getId());
	    				 depDetail.put("name",dep.getViewName());
	    				 depList.put(depDetail);
      				 }
      			 }
      		 }
      	 }
      	 return depList;
	}
    
	private JSONArray convertRoleJsonArray(String selectedValues) throws JSONException {
		JSONArray roleList = new JSONArray();
		List<String> roleNames = Arrays.asList(selectedValues.split("\\s*,\\s*"));
		if (roleNames != null && roleNames.size() > 0) {
			for (String roleName : roleNames) {
				if (!roleName.isEmpty()) {
					Role role = roleService.getRoleByName(roleName);
					if(role != null){
						JSONObject roleDetail = new JSONObject();
						roleDetail.put("id", role.getId());
						roleDetail.put("name", role.getViewName());
						roleList.put(roleDetail);
					}
				}
			}
		}
		return roleList;
	}
	
	private JSONArray convertGroupJsonArray(String selectedValues) throws JSONException {
		JSONArray groupList = new JSONArray();
		List<String> groupNames = Arrays.asList(selectedValues.split("\\s*,\\s*"));
		if (groupNames != null && groupNames.size() > 0) {
			for (String groupName : groupNames) {
				if (!groupName.isEmpty()) {
					Group group = groupService.getGroupByName(groupName);
					if(group != null){
						JSONObject groupDetail = new JSONObject();
						groupDetail.put("id", group.getId());
						groupDetail.put("name", group.getViewName());
						groupList.put(groupDetail);
					}
				}
			}
		}
		return groupList;
	}
    
    private JSONArray convertMenuJsonArray(String selectedValues) throws JSONException{
    
    JSONArray userList = new JSONArray();
    
			 Menu userDetails = menuService.getMenuByName(selectedValues);
			 	if(userDetails != null ){
				 	
	    				 JSONObject userDetail = new JSONObject();
	    				 userDetail.put("id",userDetails.toString());
	    				 userDetail.put("name",userDetails.toString());
	    				 userList.put(userDetail); 	   				 		
				 	
			 	}else {
				 JSONObject userDetail = new JSONObject();
				 userDetail.put("id",selectedValues);
				 userDetail.put("name",selectedValues);
				 userList.put(userDetail); 	   				 		
			 	}
  	 return userList;
  }
    
    /**
     * Get User selection tree data for root
     * @param model
     * @param currentNode
     * @param rootNode
     * @param nodeLevel
     * @param errors
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value="bpm/admin/getUserRootNodes", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> getUserRootNodes(ModelMap model,@RequestParam("currentNode") String currentNode, @RequestParam("rootNode") String rootNode, @RequestParam("nodeLevel") int nodeLevel, @RequestParam(value = "currentUser", required=false) String currentUser){
       	List<Map<String, Object>> nodeListOfMap = new ArrayList<Map<String, Object>>();
       	try {
       		CommonUtil.createRootTreeNode(nodeListOfMap, "Roles", "roles", "按角色");
       		CommonUtil.createRootTreeNode(nodeListOfMap, "Groups", "groups", "按用户组");
       		CommonUtil.createRootTreeNode(nodeListOfMap, "Departments", "departments", "按部门");
       	} catch (BpmException e) {
   			log.warn(e.getMessage(),e);
   		} catch (Exception e) {
   			log.warn(e.getMessage(),e);
       	}
       	return nodeListOfMap;
	}

    @RequestMapping(value="bpm/admin/updateUsersDetails", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
     public @ResponseBody
     String updateUsersDetails(ModelMap model,@RequestParam("from") String from,@RequestParam("parentId") String parentId,@RequestParam("selectedValues") String userId, 
    		 HttpServletRequest request) throws BpmException
    {
    //	String response=null;
    	String script = null;
    	Locale locale = request.getLocale();
    	try{
		 List<String> userIdList = new ArrayList<String>();
		User user=new User();
		 if (userId.contains(",")) {
			  String[] ids = userId.split(",");
			  for(String id :ids){
				  userIdList.add(id);
				  
			  }
			} else {
				userIdList.add(userId);
		}
		
		 
		 Group group=new Group();
		 Role role = new Role();
		 Department department=new Department();
		 
		 if(from.equals("group")){
			 group=groupService.getGroupById(parentId);
		 }else if(from.equals("role")){
				  role=roleService.getRoleById(parentId);
		  }else if(from.equals("department")){
				  department=departmentService.getDepartmentById(parentId);
		 }
	
		 boolean alreadyUserExists =false;
		 String[] ids = userId.split(",");
		  for(String id :ids){
			  user= userManager.getUser(id);
			  if(from.equals("group")){
				  for(Group existGroup:user.getGroups()){
					  if(existGroup.getGroupId().equals(group.getGroupId())){
						  //saveError(request, getText("error.user.delete",locale));  
						  alreadyUserExists =true;
						//  throw new BpmException(" User already exists in group ");
					  }
				  }
				  if(!alreadyUserExists){
					  user.getGroups().add(group);
					  userManager.save(user);
				  }
			  }/*
			  public void saveRefund() throws IOException {
				  String response = Constants.EMPTY_STRING;
				  try{
				  String refundSuccess = billingService.saveRefundByService(billId,convertJsonToMap(serviceBillId),convertJsonToMap(jsonValues),convertJsonToList(paymentJson),refundAmount, reason, ScreenCode.valueOf(screenCode), (String)getSessionParam("shiftId"));
				     response="{\"message\":\""+refundSuccess+" \",\"error\":\"\"}";
				  }catch(RttException e){
				  response="{\"error\":\""+e.getMessage()+" \"}";
				  logger.error(MessageResourceUtil.getMessage("error.saveRefund.failed", new Object[] {patientId}) , e);
				  }finally{
				  RadaptiveUtil.generateResponseThroughJSON(getResponse(), response);
				  }
				  }*/
			  else if(from.equals("role")){
				  for(Role existRole:user.getRoles()){
					  if(existRole.getRoleId().equals(role.getRoleId())){
						  //saveError(request, getText("error.user.delete",locale));  
						  alreadyUserExists =true;
						 // throw new BpmException(" Selected user has been assigned in this role ");
					  }
				  }
				  if(!alreadyUserExists){
					  user.getRoles().add(role);
					  userManager.save(user);
				  }
			  }
			  else if(from.equals("department")){
				 user.setDepartment(department);
				 userManager.save(user);
			  }

		  }
		  
		  List<UserDTO> users = new ArrayList<UserDTO>();
			if (from.equalsIgnoreCase("department")) {
				users = userService.getAllUsersByDepartment(parentId);
			} else if (from.equalsIgnoreCase("group")) {
				users = userService.getAllUsersByGroup(parentId);
			} else if (from.equalsIgnoreCase("role")) {
				users = userService.getAllUsersByRole(parentId);
			}else if (from.equalsIgnoreCase("all")) {
				users = userService.getAllUsersDTO(null);
			}

		/*if(userStatus.equalsIgnoreCase("disable")){
			status=false;
		}*/
		//model.addAttribute("id", parentId);
		//model.addAttribute("parentNode", from);
		//	getUserManager().updateUserStatus(userIdList, status);
			
			String[] fieldNames = { "id", "username", "fullName", "department",
					"email", "employeeNumber", "enabled" };
			script = GridUtil.generateScriptForUserGrid(CommonUtil
					.getMapListFromObjectListByFieldNames(users, fieldNames,""), velocityEngine);
			
		//	response="{\"gridContent\":\""+script+" \",\"error\":\"\"}";
			log.info("User Detail Updated SuccessFully");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			//response="{\"error\":\""+e.getMessage()+" \"}";
			//return e.getMessage();
			
			//saveError(request, getText("error.user.updated",e.getMessage(),locale));
		}	
		
		return script;
	
		//return new RedirectView("/bpm/admin/getDepartmentUserGrid?id="+parentId+"&parentNode="+from);
    	//return new ModelAndView("redirect:/bpm/admin/getDepartmentUserGrid?id="+parentId+"?parentNode="+from);
		//return new ModelAndView("redirect:/bpm/admin/getDepartmentUserGrid?id=tomcat&parentNode=group");
    }

    /**
	 * get root elements of user selection tree
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
	@RequestMapping(value="bpm/admin/getUserSelectionChildNodes", method = RequestMethod.GET)
    public @ResponseBody String getUserSelectionChildNodes(ModelMap model,@RequestParam("currentNode") String currentNode,
    		@RequestParam(value = "currentUser", required = false) String currentUser,
    		@RequestParam(value = "appendTo", required = false) String appendTo,
    		@RequestParam("rootNode") String rootNode, @RequestParam("nodeLevel") String nodeLevel,User user,
    		BindingResult errors, HttpServletRequest request, HttpServletResponse response){
		Locale locale = request.getLocale();
    	String nodes = "";
    	try {
    		if(StringUtil.isEmptyString(currentUser)){
    			if (rootNode.equalsIgnoreCase("Roles")) {
        			nodes = getRolesChildNode(currentNode, Integer.parseInt(nodeLevel), locale);
        		} else if (rootNode.equalsIgnoreCase("Groups")) {
        			nodes = getGroupChildNodes(currentNode, Integer.parseInt(nodeLevel), locale);
        		} else if (rootNode.equalsIgnoreCase("Departments")) {
        			nodes = getDepartmentsChildNode(currentNode, Integer.parseInt(nodeLevel), locale);
        		}
    		}else if(appendTo.equalsIgnoreCase("manager") || appendTo.equalsIgnoreCase("secretary")){
    			if (rootNode.equalsIgnoreCase("Roles")) {
        			nodes = getManagerRolesChildNode(currentNode, Integer.parseInt(nodeLevel), locale,currentUser);
        		} else if (rootNode.equalsIgnoreCase("Groups")) {
        			nodes = getManagerGroupChildNodes(currentNode, Integer.parseInt(nodeLevel), locale,currentUser);
        		} else if (rootNode.equalsIgnoreCase("Departments")) {
        			nodes = getManagerDepartmentsChildNode(currentNode, Integer.parseInt(nodeLevel), locale,currentUser);
        		}
    		}
    	}catch (BpmException e) {
			log.warn(e.getMessage(),e);
			saveError(request, getText("errors.userSelection.getNodes",locale));
		} catch (Exception e) {
			saveError(request, getText("errors.userSelection.getNodes",locale));
    		log.warn(e.getMessage(),e);
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
	public String formUserSelectionNodes(List<LabelValue> childNodeList, List<LabelValue> userNodeList, String root, String index) throws BpmException, Exception {
		JSONArray nodes = new JSONArray();
		JSONObject jsonNodes = new JSONObject();
		Map<String, Object> attr = new HashMap<String, Object>();
		Map<String, Object> nodeDetails = new HashMap<String, Object>();
		List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
		if (childNodeList != null && childNodeList.size() > 0) {
			for(LabelValue node : childNodeList){
				attr.put("id", root+"-"+node.getValue().replace(" ", ""));
	    		attr.put("name", node.getValue());
	    		attr.put("isUser", false);
	    		attr.put("root", root);
	    		nodeDetails.put("data", node.getLabel()+" ("+root+")");
	    		nodeDetails.put("attr", attr);
	    		nodeList.add(nodeDetails);
	    		jsonNodes = CommonUtil.getOrganizationTreeNodes(nodeList);
	    		nodes.put(jsonNodes);
			}
		}
		if (userNodeList != null && userNodeList.size() > 0) {
			for(LabelValue node : userNodeList){
				attr.put("id", node.getValue().replace(" ", ""));
	    		attr.put("name", node.getValue());
	    		attr.put("root", root);
	    		attr.put("isUser", true);
	    		attr.put("nodeName", node.getLabel());
	    		nodeDetails.put("data", node.getLabel()+" ("+index+")");
	    		nodeDetails.put("attr", attr);
	    		nodeList.add(nodeDetails);
	    		jsonNodes = CommonUtil.getOrganizationTreeNodes(nodeList);
	    		nodes.put(jsonNodes);
			}
		}
		return nodes.toString();
	}
	
	/**
	 * get Role child nodes
	 * 
	 * @param currentNode
	 * @param nodeLevel
	 * 
	 * @return
	 */
	public String getRolesChildNode(String currentNode, int nodeLevel, Locale locale) throws BpmException, Exception {
		List<LabelValue> childNodeList = new ArrayList<LabelValue>();
		List<LabelValue> userNodeList = new ArrayList<LabelValue>();
		String nodes = "";
		if(nodeLevel == 1) {
			childNodeList = roleService.getAllRolesAsLabelValue();
			nodes = formUserSelectionNodes(childNodeList,null,getText("userSelection.role",locale),getText("userSelection.user",locale));
		}else if(nodeLevel > 1) {
			userNodeList = userManager.getRoleUsersAsLabelValue(currentNode);
			nodes = formUserSelectionNodes(null,userNodeList,getText("userSelection.role",locale),getText("userSelection.user",locale));
		}
		return nodes;
	}
	
	/**
	 * get Manager Role child nodes
	 * 
	 * @param currentNode
	 * @param nodeLevel
	 * @param currentUser
	 * 
	 * @return
	 */
	
	public String getManagerRolesChildNode(String currentNode, int nodeLevel, Locale locale,String currentUser) throws BpmException, Exception {
		List<LabelValue> childNodeList = new ArrayList<LabelValue>();
		List<LabelValue> userNodeList = new ArrayList<LabelValue>();
		String nodes = "";
		if(nodeLevel == 1) {
			childNodeList = roleService.getAllRolesAsLabelValue();
			nodes = formUserSelectionNodes(childNodeList,null,getText("userSelection.role",locale),getText("userSelection.user",locale));
		}else if(nodeLevel > 1) {
			userNodeList = userManager.getManagerRoleUsersAsLabelValue(currentNode,currentUser);
			nodes = formUserSelectionNodes(null,userNodeList,getText("userSelection.role",locale),getText("userSelection.user",locale));
		}
		return nodes;
	}
	
	/**
	 * get Group child nodes
	 * 
	 * @param currentNode
	 * @param nodeLevel
	 * 
	 * @return
	 */
	public String getGroupChildNodes(String currentNode, int nodeLevel, Locale locale) throws BpmException, Exception {
		List<LabelValue> childNodeList = new ArrayList<LabelValue>();
		List<LabelValue> userNodeList = new ArrayList<LabelValue>();
		String nodes = "";
		if(nodeLevel == 1) {
			childNodeList = groupService.getAllParentGroupsAsLabelValue();
			nodes = formUserSelectionNodes(childNodeList,null,getText("userSelection.group",locale),getText("userSelection.user",locale));
		}else if(nodeLevel == 2) {
			childNodeList = groupService.getGroupsAsLabelValueByParent(currentNode);
			userNodeList = userManager.getGroupUsersAsLabelValue(currentNode);
			nodes = formUserSelectionNodes(childNodeList,userNodeList,getText("userSelection.group",locale),getText("userSelection.user",locale));
		}else if(nodeLevel > 2) {
			userNodeList = userManager.getGroupUsersAsLabelValue(currentNode);
			nodes = formUserSelectionNodes(null,userNodeList,getText("userSelection.group",locale),getText("userSelection.user",locale));
		}
		return nodes;
	}
	
	/**
	 * get Group child nodes
	 * 
	 * @param currentNode
	 * @param nodeLevel
	 * 
	 * @return
	 */
	public String getManagerGroupChildNodes(String currentNode, int nodeLevel, Locale locale, String currentEditUser) throws BpmException, Exception {
		List<LabelValue> childNodeList = new ArrayList<LabelValue>();
		List<LabelValue> userNodeList = new ArrayList<LabelValue>();
		String nodes = "";
		if(nodeLevel == 1) {
			childNodeList = groupService.getAllParentGroupsAsLabelValue();
			nodes = formUserSelectionNodes(childNodeList,null,getText("userSelection.group",locale),getText("userSelection.user",locale));
		}else if(nodeLevel == 2) {
			childNodeList = groupService.getGroupsAsLabelValueByParent(currentNode);
			userNodeList = userManager.getManagerGroupUsersAsLabelValue(currentNode,currentEditUser);
			nodes = formUserSelectionNodes(childNodeList,userNodeList,getText("userSelection.group",locale),getText("userSelection.user",locale));
		}else if(nodeLevel > 2) {
			userNodeList = userManager.getManagerGroupUsersAsLabelValue(currentNode,currentEditUser);
			nodes = formUserSelectionNodes(null,userNodeList,getText("userSelection.group",locale),getText("userSelection.user",locale));
		}
		return nodes;
	}
	
	/**
	 * get Department child nodes
	 * 
	 * @param currentNode
	 * @param nodeLevel
	 * 
	 * @return
	 */
	public String getDepartmentsChildNode(String currentNode, int nodeLevel, Locale locale) throws BpmException, Exception {
		List<LabelValue> childNodeList = new ArrayList<LabelValue>();
		List<LabelValue> userNodeList = new ArrayList<LabelValue>();
		String nodes = "";
		if(nodeLevel == 1) {
			childNodeList = departmentService.getOrganizationAsLabelValue();
			nodes = formUserSelectionNodes(childNodeList,null, getText("userSelection.department",locale),getText("userSelection.user",locale));
		}else if(nodeLevel == 2 || nodeLevel == 3) {
			childNodeList = departmentService.getDepartmentsAsLabelValueByParent(currentNode);
			userNodeList = userManager.getDepartmentUsersAsLabelValue(currentNode);
			nodes = formUserSelectionNodes(childNodeList,userNodeList, getText("userSelection.department",locale),getText("userSelection.user",locale));
		}else if(nodeLevel > 3) {
			childNodeList = departmentService.getDepartmentsAsLabelValueByParent(currentNode);
			userNodeList = userManager.getDepartmentUsersAsLabelValue(currentNode);
			nodes = formUserSelectionNodes(childNodeList,userNodeList, getText("userSelection.department",locale),getText("userSelection.user",locale));
		}
		return nodes;
	}
	
	/**
	 * get Department child nodes
	 * 
	 * @param currentNode
	 * @param nodeLevel
	 * 
	 * @return
	 */
	public String getManagerDepartmentsChildNode(String currentNode, int nodeLevel, Locale locale, String currentUser) throws BpmException, Exception {
		List<LabelValue> childNodeList = new ArrayList<LabelValue>();
		List<LabelValue> userNodeList = new ArrayList<LabelValue>();
		String nodes = "";
		if(nodeLevel == 1) {
			childNodeList = departmentService.getOrganizationAsLabelValue();
			nodes = formUserSelectionNodes(childNodeList,null, getText("userSelection.department",locale),getText("userSelection.user",locale));
		}else if(nodeLevel == 2 || nodeLevel == 3) {
			childNodeList = departmentService.getDepartmentsAsLabelValueByParent(currentNode);
			userNodeList = userManager.getManagerDepartmentUsersAsLabelValue(currentNode,currentUser);
			nodes = formUserSelectionNodes(childNodeList,userNodeList, getText("userSelection.department",locale),getText("userSelection.user",locale));
		}else if(nodeLevel > 3) {
			userNodeList = userManager.getManagerDepartmentUsersAsLabelValue(currentNode,currentUser);
			nodes = formUserSelectionNodes(null,userNodeList, getText("userSelection.department",locale),getText("userSelection.user",locale));
		}
		return nodes;
	}
	
	/**
	 * show user selection popup with user tree
	 * 
	 * @param model
	 * @param selectionType
	 * @param appendTo
	 * @param callAfter
	 * @param errors
	 * @param request
	 * @param response
	 * 
	 * @return
	 */
	@RequestMapping(value="bpm/admin/showOrganizerTree", method = RequestMethod.GET)
	public ModelAndView showOrganizerTree(ModelMap model, @RequestParam("selectionType") String selectionType, 
			@RequestParam("appendTo") String appendTo, @RequestParam("selectedValues") String selectedValues,
			@RequestParam("callAfter") String callAfter,
			@RequestParam("from") String from,
			@RequestParam("parentId") String parentId,
			@RequestParam("organizerOrders") String organizerOrders,
			@RequestParam("rootNodeURL") String rootNodeURL,
			@RequestParam("childNodeURL") String childNodeURL,
			@RequestParam("organizersList") String organizersListStr,
			@RequestParam("isPotentialOrganizer") String isPotentialOrganizer,
			User user,	BindingResult errors, 
			HttpServletRequest request, HttpServletResponse response) throws JSONException{
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String,Object>> organizersList = new ArrayList<Map<String,Object>>();
		if(!StringUtil.isEmptyString(organizersListStr)){
			organizersList = CommonUtil.convertJsonToList(organizersListStr);
		}
		try {
			JSONArray organizerOrderArray = new JSONArray();
			if(StringUtil.isEmptyString(selectedValues)){
				//setting Potential organizer initially
				int count= 0;
				for(Map<String,Object> organizer :organizersList){
					JSONObject organizerPropertyJson = new JSONObject();
					if(count == 0){
						selectedValues = selectedValues + organizer.get("organizer");
					}else {
						selectedValues = selectedValues + "," +organizer.get("organizer");
					}
					// Added superior subordinate functionality
					String organizerProperty = (String) organizer.get("organizerProperty");
					organizerPropertyJson.put("organizerOrder",organizerProperty.split("-")[0]);
					organizerPropertyJson.put("organizerSuperior",organizerProperty.split("-")[1]);
					organizerPropertyJson.put("organizerSubordinate",organizerProperty.split("-")[2]);
					organizerOrderArray.put(organizerPropertyJson);
					count++;
				}
			}else {
				List<Map<String,Object>> organizerProperty = new ArrayList<Map<String,Object>>();
				if(!StringUtil.isEmptyString(organizerOrders) && !organizerOrders.equalsIgnoreCase("undefined")){
					organizerProperty = CommonUtil.convertJsonToList(organizerOrders);
				}
				// Added superior subordinate functionality
				for(Map<String,Object> organizerVal: organizerProperty){
					JSONObject organizerPropertyJson = new JSONObject();
					organizerPropertyJson.put("organizerOrder",organizerVal.get("order"));
					organizerPropertyJson.put("organizerSuperior",organizerVal.get("superior"));
					organizerPropertyJson.put("organizerSubordinate",organizerVal.get("subordiante"));
					organizerOrderArray.put(organizerPropertyJson);
				}
		         /*if(organizerOrders.contains(",")) {
		              String[] keys = organizerOrders.split(",");
		              for(String key :keys){
		            	  organizerOrderArray.put(key);
		              }
		            } else {
		            	  organizerOrderArray.put(organizerOrders);
		            }*/
			}
			context.put("selectionType", selectionType);
			context.put("selectedValues", convertJsonArray(selectedValues));
			context.put("organizerOrders",organizerOrderArray);
			context.put("needTreeCheckbox", true);
			context.put("needContextMenu", true);
			context.put("rootNodeURL", rootNodeURL);
			context.put("childNodeURL", childNodeURL);
			context.put("organizersList", organizersListStr);
			context.put("isPotentialOrganizer", isPotentialOrganizer);
			String script=VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "organizerTree.vm", context);
			model.addAttribute("script", script);
			model.addAttribute("appendTo",appendTo);
			model.addAttribute("callAfter",callAfter);
			model.addAttribute("selectionType",selectionType);
    		model.addAttribute("selection", "");
			model.addAttribute("selectedValues",selectedValues);
			model.addAttribute("organizerOrders",organizerOrderArray);
			model.addAttribute("from", from);
			model.addAttribute("parentId", parentId);
			model.addAttribute("isPotentialOrganizer", isPotentialOrganizer);
//			log.info("Oraganization Tree Displayed Successfully");
		} catch (BpmException e) {
			log.warn(e.getMessage(),e);
			e.printStackTrace();
		} catch (Exception e) {
			log.warn(e.getMessage(),e);
			e.printStackTrace();
		}
		return new ModelAndView("admin/organizerTree", model);
	}
	
	
	
    /**
     * Get User selection tree data for root
     * @param model
     * @param currentNode
     * @param rootNode
     * @param nodeLevel
     * @param errors
     * @param request
     * @param response
     * @return
     */
	   @RequestMapping(value="bpm/admin/getOrganizerRootNodes", method = RequestMethod.GET)
	   public @ResponseBody List<Map<String, Object>> getOrganizerRootNodes(ModelMap model,@RequestParam("currentNode") String currentNode, 
			   @RequestParam("rootNode") String rootNode, @RequestParam("nodeLevel") int nodeLevel,@RequestParam("isPotentialOrganizer") String isPotentialOrganizer){
//	       	log.info("getting Organizer Root Nodes");
	       	List<Map<String, Object>> nodeListOfMap = new ArrayList<Map<String, Object>>();
	       	try {
	       		CommonUtil.createRootTreeNode(nodeListOfMap, "Roles", "roles", "按角色");
	       		CommonUtil.createRootTreeNode(nodeListOfMap, "Groups", "groups", "按用户组");
	       		CommonUtil.createRootTreeNode(nodeListOfMap, "Departments", "departments", "按部门");
	       		if(isPotentialOrganizer.equalsIgnoreCase("true")){
	       			CommonUtil.createRootTreeNode(nodeListOfMap, "PotentialOrganizers", "potentialOrganizers", "已选办理人");
	       		}
//				log.info("Oraganization Root Nodes Displayed Successfully");
	       	} catch (BpmException e) {
	   			log.warn(e.getMessage(),e);
	   		} catch (Exception e) {
	   			log.warn(e.getMessage(),e);
	       	}
	       	return nodeListOfMap;
	   }
	   
	    /**
		 * get root elements of user selection tree
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
		@RequestMapping(value="bpm/admin/getOrganizerSelectionChildNodes", method = RequestMethod.GET)
	    public @ResponseBody String getOrganizerSelectionChildNodes(ModelMap model,@RequestParam("currentNode") String currentNode, 
	    		@RequestParam("rootNode") String rootNode, @RequestParam("nodeLevel") String nodeLevel,User user, @RequestParam("organizersList") String organizersListStr,
	    		BindingResult errors, HttpServletRequest request, HttpServletResponse response){
//			log.info("getting Organizer Selection child nodes");
			Locale locale = request.getLocale();
	    	String nodes = "";
	    	try {
	    		if (rootNode.equalsIgnoreCase("Roles")) {
	    			nodes = getRolesChildNode(currentNode, Integer.parseInt(nodeLevel), locale);
	    		} else if (rootNode.equalsIgnoreCase("Groups")) {
	    			nodes = getGroupChildNodes(currentNode, Integer.parseInt(nodeLevel), locale);
	    		} else if (rootNode.equalsIgnoreCase("Departments")) {
	    			nodes = getDepartmentsChildNode(currentNode, Integer.parseInt(nodeLevel), locale);
	    		} else if (rootNode.equalsIgnoreCase("PotentialOrganizers")) {
	    			List<Map<String,Object>> organizersList = CommonUtil.convertJsonToList(organizersListStr);
	    			if(organizersList.size() > 0){
		    			nodes = getPotentialOrganizersChildNode(organizersList, locale);
	    			}
	    		}
//				log.info("Oraganization Selection Child Nodes Displayed Successfully");
	    	}catch (BpmException e) {
				log.warn(e.getMessage(),e);
				saveError(request, getText("errors.userSelection.getNodes",locale));
			} catch (Exception e) {
				saveError(request, getText("errors.userSelection.getNodes",locale));
	    		log.warn(e.getMessage(),e);
	    	}
			return nodes;
	    }
		
		/**
		 * get Department child nodes
		 * 
		 * @param currentNode
		 * @param nodeLevel
		 * 
		 * @return
		 */
		public String getPotentialOrganizersChildNode(List<Map<String,Object>> organizersList,Locale locale) throws BpmException, Exception {
			String nodes = "";
			List<String> organizers =  new ArrayList<String>();
			for(Map<String,Object> organizer :organizersList){
				if(!StringUtil.isEmptyString(organizer.get("organizer"))){
					organizers.add(organizer.get("organizer").toString());
				}
			}
			if(organizers.size() > 0){
				List<LabelValue> childNodeList = userManager.getUserLabelValueByIds(organizers);
				nodes = formUserSelectionNodes(null,childNodeList,"PotentialOrganizer",getText("userSelection.user",locale));	
			}
			return nodes;
		}

		@Autowired
		public void setMenuService(MenuService menuService) {
			this.menuService = menuService;
		}

		public MenuService getMenuService() {
			return menuService;
		}

		
		@RequestMapping(value="bpm/admin/getUserNameAndPosition", method = RequestMethod.GET)
	    public @ResponseBody List<Map<String, String>> getUserNameAndPosition(@RequestParam("userName") String userName,
	    		@RequestParam("currentUser") String currentUser,@RequestParam("appendTo") String appendTo,ModelMap model,HttpServletRequest request) throws Exception {
			List<Map<String, String>> userDetailsList = new ArrayList<Map<String, String>>();
	         try{
	             List<LabelValue> allUsers = userManager.getUserNameAndPosition(userName,currentUser,appendTo);
	        	 if (allUsers != null){
	                 for(LabelValue user : allUsers){
	                     Map<String,String> userDetail = new HashMap<String, String>();
	                     userDetail.put("userName", user.getLabel());
	                     userDetail.put("id", user.getValue());
	                     userDetailsList.add(userDetail);
	                 }
	                 return userDetailsList;
	             }
//	        	 log.info("All User Retrived Successfully");
	         }catch(Exception e){
	             log.error("Error while getting all users "+e.getMessage());
	         }
	         return new ArrayList<Map<String, String>>();
	    }
		
		@RequestMapping(value="bpm/admin/getDepartmentAdminUserSelectionChildNodes", method = RequestMethod.GET)
	    public @ResponseBody String getDepartmentAdminUserSelectionChildNodes(ModelMap model,@RequestParam("currentNode") String currentNode,
	    		@RequestParam(value = "appendTo", required = false) String appendTo,
	    		@RequestParam("rootNode") String rootNode, @RequestParam("nodeLevel") String nodeLevel,User user,
	    		BindingResult errors, HttpServletRequest request, HttpServletResponse response){
//			log.info("getting Department Admin User Selection child nodes");
			Locale locale = request.getLocale();
	    	String nodes = "";
	    	try {
	    		User userObject = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
    			if (rootNode.equalsIgnoreCase("Roles")) {
        			nodes = getDepartmentAdminRolesChildNode(currentNode, Integer.parseInt(nodeLevel), locale,userObject.getId());
        		} else if (rootNode.equalsIgnoreCase("Groups")) {
        			nodes = getDepartmentAdminGroupChildNodes(currentNode, Integer.parseInt(nodeLevel), locale,userObject.getId());
        		} else if (rootNode.equalsIgnoreCase("Departments")) {
        			nodes = getDepartmentAdminDepartmentsChildNode(currentNode, Integer.parseInt(nodeLevel), locale,userObject.getId());
        		}
//    			log.info("Department Admin User Selection child nodes retrived Successfully");
	    	}catch (BpmException e) {
				log.warn(e.getMessage(),e);
				saveError(request, getText("errors.userSelection.getNodes",locale));
			} catch (Exception e) {
				saveError(request, getText("errors.userSelection.getNodes",locale));
	    		log.warn(e.getMessage(),e);
	    	}
			return nodes;
	    }
		
	/**
	 * get Role child nodes
	 * 
	 * @param currentNode
	 * @param nodeLevel
	 * 
	 * @return
	 */
	public String getDepartmentAdminRolesChildNode(String currentNode, int nodeLevel, Locale locale,String userId) throws BpmException, Exception {
		List<LabelValue> childNodeList = new ArrayList<LabelValue>();
		List<LabelValue> userNodeList = new ArrayList<LabelValue>();
		String nodes = "";
		if(nodeLevel == 1) {
			childNodeList = roleService.getAllRolesAsLabelValue();
			nodes = formUserSelectionNodes(childNodeList,null,getText("userSelection.role",locale),getText("userSelection.user",locale));
		}else if(nodeLevel > 1) {
			userNodeList = userManager.getDepartmentAdminRoleUsersAsLabelValue(currentNode,userId);
			nodes = formUserSelectionNodes(null,userNodeList,getText("userSelection.role",locale),getText("userSelection.user",locale));
		}
//		log.info("Department Admin Roles child nodes retrived Successfully");
		return nodes;
	}
	
	
		/**
	 * get Group child nodes
	 * 
	 * @param currentNode
	 * @param nodeLevel
	 * 
	 * @return
	 */
	public String getDepartmentAdminGroupChildNodes(String currentNode, int nodeLevel, Locale locale,String userId) throws BpmException, Exception {
		List<LabelValue> childNodeList = new ArrayList<LabelValue>();
		List<LabelValue> userNodeList = new ArrayList<LabelValue>();
		String nodes = "";
		if(nodeLevel == 1) {
			childNodeList = groupService.getAllParentGroupsAsLabelValue();
			nodes = formUserSelectionNodes(childNodeList,null,getText("userSelection.group",locale),getText("userSelection.user",locale));
		}else if(nodeLevel == 2) {
			childNodeList = groupService.getGroupsAsLabelValueByParent(currentNode);
			userNodeList = userManager.getDepartmentAdminGroupUsersAsLabelValue(currentNode,userId);
			nodes = formUserSelectionNodes(childNodeList,userNodeList,getText("userSelection.group",locale),getText("userSelection.user",locale));
		}else if(nodeLevel > 2) {
			userNodeList = userManager.getDepartmentAdminGroupUsersAsLabelValue(currentNode,userId);
			nodes = formUserSelectionNodes(null,userNodeList,getText("userSelection.group",locale),getText("userSelection.user",locale));
		}
		
		return nodes;
	}
		
		
	/**
	 * get Department child nodes
	 * 
	 * @param currentNode
	 * @param nodeLevel
	 * 
	 * @return
	 */
	public String getDepartmentAdminDepartmentsChildNode(String currentNode, int nodeLevel, Locale locale,String userId) throws BpmException, Exception {
		List<LabelValue> childNodeList = new ArrayList<LabelValue>();
		List<LabelValue> userNodeList = new ArrayList<LabelValue>();
		String nodes = "";
		if(nodeLevel == 1) {
			childNodeList = departmentService.getUserAdministrationDepartments(userId);
			nodes = formUserSelectionNodes(childNodeList,null, getText("userSelection.department",locale),getText("userSelection.user",locale));
		}else if(nodeLevel == 2 || nodeLevel == 3) {
			childNodeList = departmentService.getDepartmentsAsLabelValueByParent(currentNode);
			userNodeList = userManager.getDepartmentUsersAsLabelValue(currentNode);
			nodes = formUserSelectionNodes(childNodeList,userNodeList, getText("userSelection.department",locale),getText("userSelection.user",locale));
		}else if(nodeLevel > 3) {
			userNodeList = userManager.getDepartmentUsersAsLabelValue(currentNode);
			nodes = formUserSelectionNodes(null,userNodeList, getText("userSelection.department",locale),getText("userSelection.user",locale));
		}
		return nodes;
	}
		
	@RequestMapping(value="bpm/admin/getChangePassword",method=RequestMethod.GET)
	public ModelAndView getChangePassword(ModelMap model,HttpServletRequest request, HttpServletResponse response){
		
		try{
			User userObject = userManager.getUserById(CommonUtil.getLoggedInUserId());
			String language = userObject.getLanguage();
			String preferredSkin = userObject.getPreferredSkin();
			
			/*userObject.setPicture(CommonUtil.getLoggedInUser().getPicture());
			userObject.setPictureByteArrayId(CommonUtil.getLoggedInUser().getPictureByteArrayId());
			userObject.setUsername(CommonUtil.getLoggedInUser().getUsername());
			userObject.setLanguage(CommonUtil.getLoggedInUser().getLanguage());
			userObject.setPreferredSkin(CommonUtil.getLoggedInUser().getPreferredSkin());*/
			model.addAttribute("language",language);
			model.addAttribute("preferredSkin",preferredSkin);
			model.addAttribute("userObject",userObject);
			log.info("Changed Password Retrived Successfully");
		}catch(Exception e){
			log.info("Exception during get user"+e.getMessage());
		}
		return new ModelAndView("passwordChange",model);
	}
		
	@RequestMapping(value = "bpm/admin/saveChangedPassword", method = RequestMethod.POST)
	public ModelAndView saveChangedPassword(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("userObject") User userObject) throws Exception {
		String pictureValue = request.getParameter("pictureByteArrayId");
		String currentPassword = request.getParameter("oldPassword");
		String remoteUser = request.getRemoteUser();
		String contextPath = request.getContextPath();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");

		try {
			if (userObject != null) {
				User user = userManager.getUserById(CommonUtil.getLoggedInUserId());

				if (file != null && !file.isEmpty() && !file.getOriginalFilename().isEmpty()) {
					user.setPictureByteArrayId(getUserProfileImagePath(user, file, remoteUser, contextPath));
				} else if (pictureValue.equals("/images/profile/default.png")) {
					user.setPictureByteArrayId("/images/profile/default.png");
				}
				user.setPreferredSkin(userObject.getPreferredSkin());
				user.setLanguage(userObject.getLanguage());

				if (!currentPassword.isEmpty()) {
					String isValidPassword = userManager.isValidUserPassword(CommonUtil.getLoggedInUserId(), currentPassword, userObject.getPassword());
					if (isValidPassword.equals("true")) {

					} else {
						saveError(request, "当前密码不正确");
						return new ModelAndView("passwordChange", model);
					}
				}

				if (userObject.getPassword() != null && !userObject.getPassword().isEmpty() && userObject.getPassword().length() > 0) {
					user.setPassword(userObject.getPassword());
					user.setConfirmPassword(userObject.getPassword());
				}

				User userDetail = userManager.saveUser(user);

				model.addAttribute("userObject", userDetail);
				saveMessage(request, "更新成功，重新登录后生效");
				return new ModelAndView("passwordChange", model);
			} else {
				userObject.setPictureByteArrayId(CommonUtil.getLoggedInUser().getPictureByteArrayId());
				userObject.setUsername(CommonUtil.getLoggedInUser().getUsername());
				model.addAttribute("userObject", userObject);
				saveError(request, "操作失败");
				log.error("User Details failed to change. The entered information incorrect");
				return new ModelAndView("passwordChange", model);
			}
		} catch (Exception e) {
			log.info("Exception at the place of saving User detail" + e.getMessage());
			userObject.setPictureByteArrayId(CommonUtil.getLoggedInUser().getPictureByteArrayId());
			userObject.setUsername(CommonUtil.getLoggedInUser().getUsername());
			model.addAttribute("userObject", userObject);
			saveError(request, "No Changes in Preferences");
			return new ModelAndView("passwordChange", model);
		}
	}
		
	/**
	 * method to get user profile image path
	 *
	 * @param user
	 * @param file
	 * @param remoteUser
	 * @param contextPath
	 * @return pictureByteArrayId
	 * @throws IOException
	 * @throws InvalidNameException 
	 */
	private String getUserProfileImagePath(User user, CommonsMultipartFile file,String remoteUser, String contextPath)throws IOException, InvalidNameException{
		 if (file != null){
			 String fileName = file.getOriginalFilename();
			 int pos = fileName.lastIndexOf("."); 
			 String extension = fileName.substring(pos+1);
			 fileName = fileName.substring(0, pos-1);
			 fileName = fileName.replace(" ", "_");
		     byte[] encodedFileName = fileName.getBytes("UTF-8");
		     fileName= encodedFileName.toString();
		     if(fileName.startsWith("[")){
		     fileName = fileName.substring(1);
		     }
			 fileName = fileName+"."+extension;
		     if(!ValidationUtil.validateImage(fileName)){
				 throw new InvalidNameException("Invalid FileName");
			 }
			 
	         String uploadDir = getServletContext().getRealPath("/images") + "/" + remoteUser + "/";
	         File dirPath = new File(uploadDir);
	         if (!dirPath.exists()) {
	             dirPath.mkdirs();
	         }
		         
	         InputStream stream = file.getInputStream();
	         OutputStream bos = new FileOutputStream(uploadDir + fileName);
	         int bytesRead;
	         byte[] buffer = new byte[8192];
	
	         while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
	             bos.write(buffer, 0, bytesRead);
	         }
	
	         bos.close();
	         stream.close();
	         String link = contextPath + "/images" + "/" + remoteUser + "/";
	         return link + fileName;
	     }else if (user.getPictureByteArrayId().isEmpty() || user.getPictureByteArrayId() == null){
	     	 return "/images/profile/default.png";
	     }else {
	    	 return user.getPictureByteArrayId();
	    	// return "/images/profile/default.png";
	     }
	}
}
