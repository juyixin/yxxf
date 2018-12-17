package com.eazytec.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.eazytec.bpm.admin.group.service.GroupService;
import com.eazytec.bpm.admin.role.service.RoleService;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.dao.SearchException;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.Department;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.service.GroupExistsException;
import com.eazytec.service.UserManager;
import com.eazytec.service.UserService;


/**
 * Simple class to retrieve a list of groups from the database.
 *
 * @author madan
 * @author vinoth
 */
@Controller
public class GroupController extends BaseFormController {
	protected final transient Log log = LogFactory.getLog(getClass());
    private GroupService groupService;
    private UserService userService;
    public VelocityEngine velocityEngine;
    private UserManager userManager;
    private RoleService roleService = null;
    
    @Autowired
    public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
    
    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }
    
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
    
    @Autowired     
    public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	@RequestMapping(value = "bpm/admin/saveGroup", method = RequestMethod.POST)
   	public ModelAndView saveGroup(HttpServletRequest request,@ModelAttribute("group") Group group,
   			ModelMap model,BindingResult errors) {
    	Integer originalVersion = group.getVersion();
    	String administrators = request.getParameter("administrators");
    	String interfacePeople = request.getParameter("interfacePeople");
    	Set<User> administratorsSet = new HashSet<User>();
    	Set<User> interfacePeopleSet = new HashSet<User>();
    	String groupName = group.getName();
   		try {
   			Locale locale = request.getLocale();
   			if (validator != null) { // validator is null during testing
				validator.validate(group, errors);

				if (errors.hasErrors()) { // don't validate when deleting
					group.setVersion(originalVersion);
					group.setName(groupName);
					model.addAttribute("administrators", administrators);
					model.addAttribute("interfacePeople", interfacePeople);
					return new ModelAndView("admin/groupForm", model);
				}
			}
   			if(originalVersion == 0){
   		        if(groupService.isDublicateGroup(StringUtil.removeFirstAndLastSpaceInString(groupName))) {
   		        	group.setVersion(originalVersion);
   		        	group.setName(groupName);
   		        	model.addAttribute("administrators", administrators);
   					model.addAttribute("interfacePeople", interfacePeople);
   		        	errors.rejectValue("name", "errors.existing.group",
   		                    new Object[]{group.getName()}, "errors.existing.group");
   					return new ModelAndView("admin/groupForm", model);
   		        } else {
   		        	group.setId(StringUtil.removeFirstAndLastSpaceInString(groupName));
	            	group.setName(StringUtil.removeFirstAndLastSpaceInString(groupName));
   	   			}
   	        }
   			if (group.getId() != null && group.getSuperGroup() != null && group.getName().equals(group.getSuperGroup())) {
   				errors.rejectValue("superGroup","errors.supergroup.group");
   				group.setVersion(originalVersion);
   				group.setName(groupName);
   				model.addAttribute("administrators", administrators);
   				model.addAttribute("interfacePeople", interfacePeople);
				return new ModelAndView("admin/groupForm", model);
			}
   			
   			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
   			Group tempGroup=groupService.getGroupById(group.getId());
   			
   			boolean isGroupEdit=false;
   			if(tempGroup==null){
   				isGroupEdit=groupService.getIsGroupAdmin(user,group,true);	
   			}else{
   				isGroupEdit=groupService.getIsGroupAdmin(user,group,false);
   			}
   			
   		    boolean isGroupAdmin=groupService.getIsGroupAdmin(user);
   		    
   		    model.addAttribute("isGroupEdit", true);
			model.addAttribute("isGroupAdmin", isGroupAdmin);
   			if(!CommonUtil.isUserAdmin(user)){
   				if(group.getIsParent() && tempGroup==null){
   					saveError(request, getText("group.parent.error.privilege", user.getFirstName()+" "+user.getLastName(), locale));
   	   				return new ModelAndView("admin/groupForm", model);
   				}
   				if(!isGroupEdit){
   					saveError(request, getText("group.error.admin.privilege", user.getFirstName()+" "+user.getLastName(), locale));
   	   				return new ModelAndView("admin/groupForm", model);
   				}
   			}
   			
   			//setGroupFields(group);
   			
   			if(administrators != null && !administrators.isEmpty()){
	   			if (administrators.contains(",")) {
	   				String[] ids = administrators.split(",");
	   				for(String userid :ids){
	   					administratorsSet.add(new User(userid, userid));
	   				}
	   			} else {
	   				administratorsSet.add(new User(administrators, administrators));
	   			}
	   			group.setAdministrators(administratorsSet);
   			}else {
   				group.setAdministrators(null);
   			}
   			
   			if(interfacePeople != null && !interfacePeople.isEmpty()){
	   			if (interfacePeople.contains(",")) {
	   				String[] ids = interfacePeople.split(",");
	   				for(String userid :ids){
	   					interfacePeopleSet.add(new User(userid, userid));
	   				}
	   			} else {
	   				interfacePeopleSet.add(new User(interfacePeople, interfacePeople));
	   			}
	   			group.setInterfacePeople(interfacePeopleSet);
   			}else {
   				group.setInterfacePeople(null);
   			}
   			
   			if (group.getIsParent() != true && (group.getSuperGroup() == null || group.getSuperGroup().isEmpty())){
   				errors.rejectValue("superGroup","errors.parent.supergroup");
   				group.setVersion(originalVersion);
   				group.setName(groupName);
   				model.addAttribute("administrators", administrators);
   				model.addAttribute("interfacePeople", interfacePeople);
				return new ModelAndView("admin/groupForm", model);
   			}
   			
   			if(group.getIsParent() == true && (group.getSuperGroup() == null || group.getSuperGroup().isEmpty())) {
   				group.setSuperGroup("");
   			}
   			
   			if(group.getGroupRole().isEmpty()){
   				Role role=new Role("ROLE_"+groupName+"_GROUP","ROLE_"+groupName+"_GROUP","Internal");
   				role=roleService.save(role);
   				group.setGroupRole(role.getName());
   				log.info("Creating Role : RoleName : "+role.getName());
   			}
   			if(originalVersion == 0){
   				group.setOrderNo(groupService.generateOrderNo(group.getSuperGroup()));
   			}
			groupService.saveGroup(group);
			if(originalVersion == 0){
				saveMessage(request, getText("group.added", group.getName(), locale));
				log.info("Group Name : "+group.getName()+" "+getText("add.success",locale));
			}else{
				saveMessage(request, getText("group.saved", group.getName(), locale));
				log.info("Group Name : "+group.getName()+" "+getText("save.success",locale));
			}
			model.addAttribute("administrators", administrators);
			model.addAttribute("interfacePeople", interfacePeople);
			reloadContext(request.getSession().getServletContext());
		} catch (GroupExistsException e) {
			log.error(e.getMessage(),e);
			group.setVersion(originalVersion);
			group.setName(groupName);
			model.addAttribute("administrators", administrators);
			model.addAttribute("interfacePeople", interfacePeople);
			errors.rejectValue("name", "errors.existing.group",
                    new Object[]{group.getName()}, "errors.existing.group");
			return new ModelAndView("admin/groupForm");
		}catch (Exception e) {
			log.error(e.getMessage(),e);
			group.setVersion(originalVersion);
			group.setName(groupName);group.setName(groupName);
			model.addAttribute("administrators", administrators);
			model.addAttribute("interfacePeople", interfacePeople);
			errors.rejectValue("error.occured", e.getMessage());
			return new ModelAndView("admin/groupForm");
		}
   		return new ModelAndView("redirect:groups");
      }
    
    
    @RequestMapping(value = "bpm/admin/groupForm",method = RequestMethod.GET)
	   public ModelAndView showNewGroupForm(ModelMap model) {
    	  User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	   boolean isGroupAdmin=groupService.getIsGroupAdmin(user);
    	   Group group=new Group();
		    if(isGroupAdmin){
		    	group.setSuperGroup(groupService.getUserGroupId(user.getId()));
		    }
		  model.addAttribute("isGroupEdit", true);
		  model.addAttribute("isGroupAdmin", isGroupAdmin);
		  model.addAttribute("isRoleEdit",true);
	      
	      model.addAttribute("group",group);
	      return new ModelAndView("/admin/groupForm",model);
	   }
	 
    /**
   	 * Updates Order No of Group when the user click on move up and
   	 * move down images in group  grid.
   	 * 
   	 * @param groupJsonString
   	 * @return response after updating the ordernos
   	 */
       @RequestMapping(value = "bpm/admin/updateGroupOrderNos", method = RequestMethod.POST)
       public @ResponseBody String updateGroupOrderNos(@RequestParam("groupJson") String groupJsonString) {
       	JSONObject respObj = new JSONObject();
       	JSONArray responseMsg = new JSONArray();
       	try {
   			List<Map<String,Object>> groupList = CommonUtil.convertJsonToList(groupJsonString);
   			boolean hasUpdated = groupService.updateGroupOrderNos(groupList);
   			if(hasUpdated){
   				respObj.put("isSuccess", "true");
   				responseMsg.put(respObj);
   				return responseMsg.toString();
   			}
 	        log.info("GroupOrder Updated Successfully ");
   		} catch (Exception e) {
   			log.error("Error while updating order nos for Group",e);
   		}
       	return Constants.EMPTY_STRING;
       			
       }
    
    @RequestMapping(value = "bpm/admin/groups", method = RequestMethod.GET)
    public ModelAndView showGroups(@RequestParam(required = false, value = "q") String query) throws Exception {
        Model model = new ExtendedModelMap();
        try {
        	List<Group> groupList=groupService.getGroups();
            model.addAttribute(Constants.GROUP_LIST, groupList);

            String[] fieldNames={"groupId","viewName","description","personIncharge", "superGroup", "createdTimeByString","orderNo"};
            String script=GridUtil.generateScriptForGroupGrid(CommonUtil.getMapListFromObjectListByFieldNames(groupList,fieldNames,""), velocityEngine);
            model.addAttribute("script", script);
        } catch (SearchException se) {
            model.addAttribute("searchError", se.getMessage());
          //  model.addAttribute(groupService.getGroups());
        }
        return new ModelAndView("admin/groupList", model.asMap());
    }
    
    @RequestMapping(value = "bpm/admin/editGroup",method = RequestMethod.GET)
    public ModelAndView editGroup(@RequestParam("id") String id,
   			ModelMap model)
            throws Exception {
    	String administrators = "";
    	String interfacePeople = "";
		Group group = groupService.getGroupById(id);
		
		if(group.getAdministrators() != null){
    		for(User administrator : group.getAdministrators()){
    			if(administrators.length() == 0 ) {
    				administrators+=administrator.getUsername();
    		    } else {
    		    	administrators+=","+administrator.getUsername();
    		    }
    		}
    	}
		if(group.getInterfacePeople() != null){
    		for(User interfacer : group.getInterfacePeople()){
    			if(interfacePeople.length() == 0 ) {
    				interfacePeople+=interfacer.getUsername();
    		    } else {
    		    	interfacePeople+=","+interfacer.getUsername();
    		    }
    		}
    	}
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		boolean isGroupEdit=groupService.getIsGroupAdmin(user,group,false);
		boolean isGroupAdmin=groupService.getIsGroupAdmin(user);
		    
		model.addAttribute("isGroupEdit", isGroupEdit);
		model.addAttribute("isGroupAdmin", isGroupAdmin);
		model.addAttribute("administrators", administrators);
		model.addAttribute("interfacePeople", interfacePeople);
		model.put("group", group);
        //roleMap.put("id", role.getId().toString());
	    log.info("Selected Group Edited Successfully ");
        return new ModelAndView("admin/groupForm", model);   
    }
    
    /**
	 * method for delete the selected groups from group grid
	 * 
	 * @param userId
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/deleteSelectedGroup", method = RequestMethod.GET)
	public ModelAndView deleteSelectedGroups(@RequestParam("groupIds") String groupIds, HttpServletRequest request) {
		Locale locale = request.getLocale();
		 List<String> groupIdList = new ArrayList<String>();
		 if (groupIds.contains(",")) {
			  String[] ids = groupIds.split(",");
			  for(String id :ids){
				  groupIdList.add(id);
			  }
			} else {
				groupIdList.add(groupIds);
			}
		try{
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			String notDeletedGroups=groupService.removeGroups(groupIdList,user);
			if(notDeletedGroups==null){
				log.info("Group Names : "+groupIds+" "+getText("delete.success",locale));					
				saveMessage(request, getText("success.group.delete",locale));
			}else{
				saveError(request, getText("group.error.delete.privilege", new Object[]{notDeletedGroups, user.getFirstName()+" "+user.getLastName()}  , locale));
				log.info(getText("group.error.delete.privilege", new Object[]{notDeletedGroups, user.getFirstName()+" "+user.getLastName()}  , locale));
			}
			reloadContext(request.getSession().getServletContext());
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			saveError(request, getText("error.group.delete",e.getMessage(),locale));
		}	
		return new ModelAndView("redirect:groups");
	}
    
    
    /**
	 * method to set group details
	 *
	 * @param request
	 * @param user
	 */
	private Group setGroupFields(Group group) {
		List<String> userNames = new ArrayList<String>();
		if (!group.getAdministrators().isEmpty()) {
			for (User user : group.getAdministrators()) {
				userNames.add(user.getUsername());
			}
			List<User> users = (List<User>) userService
					.getUserByUsernames(userNames);
			group.setAdministrators(new HashSet<User>(users));
		}
		userNames.clear();
		if (!group.getInterfacePeople().isEmpty()) {
			for (User user : group.getInterfacePeople()) {
				userNames.add(user.getUsername());
			}
			List<User> users = (List<User>) userService
					.getUserByUsernames(userNames);
			group.setInterfacePeople(new HashSet<User>(users));
		}
		return group;
	}
	
	private void reloadContext(ServletContext context){
		 context.setAttribute(Constants.AVAILABLE_GROUPS, new HashSet<LabelValue>(groupService.getAllGroups()));
	}
    
	 
	/**
	 * Fetches all groups
	 */
	@RequestMapping(value = "/admin/getAllGroups", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, List<Map<String, String>>> getAllGroups() {
		List<Map<String, String>> groupList = new ArrayList<Map<String, String>>();
		Map<String, List<Map<String, String>>> resultValue = new HashMap<String, List<Map<String, String>>>();
		Map<String, String> groupDetails = null;
		try {
			List<Group> groups = groupService.getGroups();
			for (Group group : groups) {
				groupDetails = new HashMap<String, String>();
				groupDetails.put("id", group.getId());
				groupDetails.put("name", group.getName());
				groupList.add(groupDetails);
			}
			resultValue.put("result", groupList);
			return resultValue;
		} catch (Exception e) {
			log.info("Error while getting all groups ", e);
		}
		return null;
	}
	
	/**
	 * Fetches all parent groups
	 */
	@RequestMapping(value = "/admin/getAllParentGroups", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, List<Map<String, String>>> getAllParentGroups() {
		List<Map<String, String>> groupList = new ArrayList<Map<String, String>>();
		Map<String, List<Map<String, String>>> resultValue = new HashMap<String, List<Map<String, String>>>();
		Map<String, String> groupDetails = null;
		try {
			List<Group> groups = groupService.getAllParentGroups();
			for (Group group : groups) {
				groupDetails = new HashMap<String, String>();
				groupDetails.put("id", group.getId());
				groupDetails.put("name", group.getName());
				groupDetails.put("viewName", group.getViewName());
				groupDetails.put("superDep", group.getSuperGroup());
				groupList.add(groupDetails);
			}
			resultValue.put("result", groupList);
			return resultValue;
		} catch (Exception e) {
			log.info("Error while getting all groups ", e);
		}
		return null;
	}
	
	@RequestMapping(value="/admin/getChildGroups", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, String>> getChildGroups(@RequestParam("groupId") String groupId) {
		  List<Map<String, String>> groupDetails = new ArrayList<Map<String, String>>();
		  List<Group> groupList = new ArrayList<Group>();
		try {
			  groupList = groupService.getGroupBySuperGroup(groupId);
				  if (groupList != null){
					  for(Group group : groupList){
						  Map<String,String> groupDetail = new HashMap<String, String>();
						  groupDetail.put("id", group.getId());
						  groupDetail.put("name", group.getName());
						  groupDetail.put("viewName", group.getViewName());
						  groupDetail.put("superDep", group.getSuperGroup());
						  groupDetails.add(groupDetail);
					  }
				  }
		} catch(Exception e ) {
			  log.error("Error while getting departments ",e);
		}
		return groupDetails;
    	
    }
    
	/**
     * method for show Group Tree
     * 
     * @param title
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value="bpm/admin/showGroupTree", method = RequestMethod.GET)
    public ModelAndView showDepartmentTree(@RequestParam("title") String title,@RequestParam("id") String groupId,ModelMap model,@RequestParam("selectType") String selectType) {
    	List<Group> groupList = groupService.getGroups();
    	model.addAttribute(Constants.GROUP_LIST, new HashSet<Group>(groupList));
    	model.addAttribute("title",title);
    	model.addAttribute("selectType",selectType);
    	model.addAttribute("requestorId",groupId);
    	return new ModelAndView("/admin/showGroupTree", model);
    }
    
	/**
	 * To get the department list based on departmentId through ajax call
	 * @param id
	 *
	 * @return
	 */
	 @RequestMapping(value="bpm/admin/getGroupsByLabelValue", method = RequestMethod.GET)
	 public @ResponseBody List<Map<String, String>> getGroupsByLabelValue(@RequestParam("id") String id) {
		  List<Map<String, String>> groupDetailsList = new ArrayList<Map<String, String>>();
		  try{
			  List<LabelValue> groups = null;
			  groups =  groupService.getGroupsByLabelValue(id);
			  if (groups != null){
				  for(LabelValue department : groups){
					  Map<String,String> groupDetail = new HashMap<String, String>();
					  groupDetail.put("groupName", department.getLabel());
					  groupDetail.put("groupId", department.getValue());
					  groupDetailsList.add(groupDetail);
				  }
				  return groupDetailsList;
			  }
		  }catch(Exception e){
			  log.error("Error while getting departments ",e);
		  }		 
	      return null;
	 }
	 
	 
	 @RequestMapping(value = "/admin/getAllUsers", method = RequestMethod.GET)
		public @ResponseBody
		Map<String, List<Map<String, String>>> getAllUsers() {
			List<Map<String, String>> groupList = new ArrayList<Map<String, String>>();
			Map<String, List<Map<String, String>>> resultValue = new HashMap<String, List<Map<String, String>>>();
			Map<String, String> groupDetails = null;
			try {
				List<LabelValue> users = userManager.getAllUsers();
				for (LabelValue user : users) {
					groupDetails = new HashMap<String, String>();
					groupDetails.put("id", user.getValue());
					groupDetails.put("name", user.getLabel());
					groupList.add(groupDetails);
				}
				resultValue.put("result", groupList);
				return resultValue;
			} catch (Exception e) {
				log.info("Error while getting all groups ", e);
			}
			return null;
		}
	 
	 
	/**
     * method for show Group Tree
     * 
     * @param title
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value="bpm/admin/getJsonGroupTree", method = RequestMethod.GET)
    public @ResponseBody List<Group> getAllGroupEntities() {
    	List<Group> groupList = groupService.getGroups();
    	return groupList;
    }
    
	 
    /**
	 * get child nodes of group
	 * 
	 * @param model
	 * @param currentNode
	 * @param errors
	 * @param request
	 * @param response
	 * 
	 * @return
	 */
	@RequestMapping(value="bpm/admin/getGroupNodes", method = RequestMethod.GET)
    public @ResponseBody String getGroupNodes(ModelMap model,@RequestParam("currentNode") String currentNode, @RequestParam("rootNode") String rootNode, @RequestParam("nodeLevel") int nodeLevel, 
    		User user, BindingResult errors, HttpServletRequest request, HttpServletResponse response){
		Locale locale = request.getLocale();
		JSONArray nodes = new JSONArray();
    	try {
    		List<LabelValue> childNodeList = groupService.getGroupsAsLabelValueByParent(currentNode);
    		nodes = CommonUtil.getNodesFromLabelValue(childNodeList);
    	} catch (BpmException e) {
			log.warn(e.getMessage(),e);
			saveError(request, getText("errors.userSelection.getNodes",locale));
		} catch (Exception e) {
			saveError(request, getText("errors.userSelection.getNodes",locale));
    		log.warn(e.getMessage(),e);
    	}
		return nodes.toString();
    }
	  
}
