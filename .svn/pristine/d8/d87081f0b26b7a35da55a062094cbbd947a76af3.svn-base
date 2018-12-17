package com.eazytec.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

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
import com.eazytec.bpm.admin.department.service.DepartmentService;
import com.eazytec.bpm.admin.group.service.GroupService;
import com.eazytec.bpm.admin.role.service.RoleService;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.dao.SearchException;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.Department;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.service.RoleExistsException;
import com.eazytec.util.JSTreeUtil;


/**
 * Simple class to retrieve a list of roles from the database.
 *
 * @author madan
 */
@Controller
public class RoleController extends BaseFormController{
	
    protected final transient Log log = LogFactory.getLog(getClass());

    private RoleService roleService = null;
    public VelocityEngine velocityEngine;
    private GroupService groupService;
    private DepartmentService departmentService;
     
    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }
    
    @Autowired
    public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

    @Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    @Autowired
    public void setDepartmentService(DepartmentService departmentService) {
  		this.departmentService = departmentService;
  	}
    
	@RequestMapping(value = "bpm/admin/roles*", method = RequestMethod.GET)
    public ModelAndView showRoles(@RequestParam(required = false, value = "q") String query) throws Exception {
        Model model = new ExtendedModelMap();
        try {
        	/* List<Role> allRoleList =roleService.search(query);
        	 List<Role> roleList =new ArrayList<Role>();
        	 for(Role role:allRoleList){
        		 if(!role.getRoleType().equals("Internal")){
        			 roleList.add(role);
        		 }
        	 }
        	model.addAttribute(Constants.ROLE_LIST, new HashSet<Role>(roleList));
            Set<Role> roleSet = new HashSet<Role>(roleList);
            roleList=new ArrayList<Role>(roleSet);*/
        	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        	List<Role> roleList =roleService.getAllUserRoles(user);
        	if(roleList!=null && !roleList.isEmpty()){
        		model.addAttribute(Constants.ROLE_LIST, new HashSet<Role>(roleList));
               // Set<Role> roleSet = new HashSet<Role>(roleList);
                //roleList=new ArrayList<Role>(roleSet);
        	}else{
        		model.addAttribute(Constants.ROLE_LIST, null);
        	}
        	
            String[] fieldNames={"roleId","name","viewName","description","roleType", "orderNo", "createdTimeByString"};
            String script=GridUtil.generateScriptForRoleGrid(CommonUtil.getMapListFromObjectListByFieldNames(roleList,fieldNames,""), velocityEngine);
            model.addAttribute("script", script);
        	
        	
        } catch (SearchException se) {
            model.addAttribute("searchError", se.getMessage());
            List<Role> roleList=roleService.getRoles();
            String script=GridUtil.generateScriptForRoleGrid(CommonUtil.getMapListFromObjectList(roleList), velocityEngine);
            model.addAttribute("script", script);
        }
        return new ModelAndView("admin/roleList", model.asMap());
    }
    
    
    @RequestMapping(value = "bpm/admin/newRole",method = RequestMethod.GET)
	   public ModelAndView showNewGroupForm(ModelMap model) {
    		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	      boolean isDepartmentAdmin=departmentService.getIsDepartmentAdmin(user);
	      Role role=new Role();
	      if(isDepartmentAdmin){
	    	  role.setRoleDepartment(user.getDepartment());
	      }
	      model.addAttribute("isDepartmentAdmin",isDepartmentAdmin);
	      model.addAttribute("isRoleEdit",true);
	      model.addAttribute("role",role);
	      return new ModelAndView("admin/newRole",model);
	   }
    
    
    @RequestMapping(value = "bpm/admin/saveRole", method = RequestMethod.POST)
   	public ModelAndView saveRole(HttpServletRequest request, @ModelAttribute("role") Role role,
   			ModelMap model,BindingResult errors) {
   			int originalVersion = role.getVersion();
   			Department originalDepartment = role.getRoleDepartment();
   			String roleName = role.getName();
	   		try {
	            Locale locale = request.getLocale();
	            
	            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	            boolean isRoleEdit=departmentService.getIsDepartmentAdmin(user,role.getRoleDepartment());
			    boolean isDepartmentAdmin=departmentService.getIsDepartmentAdmin(user); 
			    
			    model.addAttribute("isRoleEdit",isRoleEdit);
	      	    model.addAttribute("isDepartmentAdmin",isDepartmentAdmin);
	            if(!isRoleEdit){
	            	saveError(request, getText("role.error.admin.privilege", user.getFirstName()+" "+user.getLastName(), locale));
                    return new ModelAndView("admin/newRole",model);
	            }
	            
	            if(originalVersion == 0){
	            	Role duplicateRole =  roleService.getRole(StringUtil.removeFirstAndLastSpaceInString(roleName));
	            	if(duplicateRole == null){
	            		role.setId(StringUtil.removeFirstAndLastSpaceInString(roleName));
		            	role.setName(StringUtil.removeFirstAndLastSpaceInString(roleName));
	            	} else {
	            		role.setVersion(originalVersion);
	                	role.setName(roleName);
                		if (originalDepartment != null) {
        	   				if(role.getRoleType().equalsIgnoreCase("Department") && !originalDepartment.getName().isEmpty()){
        	   					role.setRoleDepartment(departmentService.getDepartment(originalDepartment.getName()));
        	   				}else{
        	   					role.setRoleDepartment(null);
        	   				}
        	            }
                		errors.rejectValue("name", "errors.existing.role",
        	                    new Object[]{roleName}, "duplicate role");
                        return new ModelAndView("admin/newRole",model);
	            	}
	            	
	            }
	            if (originalDepartment != null) {
	   				if(role.getRoleType().equalsIgnoreCase("Department") && !originalDepartment.getName().isEmpty()){
	   					role.setRoleDepartment(departmentService.getDepartment(originalDepartment.getName()));
	   				}
	            }
	   			if (validator != null) {
	                validator.validate(role, errors);
	                if (errors.hasErrors()){
	                	role.setVersion(originalVersion);
	                	role.setName(roleName);
                		if (originalDepartment != null) {
        	   				if(role.getRoleType().equalsIgnoreCase("Department") && !originalDepartment.getName().isEmpty()){
        	   					role.setRoleDepartment(departmentService.getDepartment(originalDepartment.getName()));
        	   				}else{
        	   					role.setRoleDepartment(null);
        	   				}
        	            }
                        return new ModelAndView("admin/newRole",model);
	                }
	            }
	   			if (originalDepartment != null) {
	   				if(role.getRoleType().equalsIgnoreCase("Department") && !originalDepartment.getName().isEmpty()){
	   					role.setRoleDepartment(departmentService.getDepartment(originalDepartment.getName()));
	   				}else{
	   					role.setVersion(originalVersion);
	   					role.setRoleDepartment(null);
	   					role.setName(roleName);
	   					errors.rejectValue("roleDepartment", "errors.role.departmentType");
	   					return new ModelAndView("admin/newRole");
	   				}
	            }
	   			
	   			if(originalVersion == 0){
	   				role.setOrderNo(roleService.generateOrderNo());
	   			}
	   			
	   			roleService.saveRole(role);
	   			
	   			if(originalVersion == 0){
	   				saveMessage(request, getText("role.added", role.getName(), locale));
	   				log.info("Role Name : "+role.getName()+" "+getText("add.success",locale));	   				
	   			}else{
	   				saveMessage(request, getText("role.saved", role.getName(), locale));
	   				log.info("Role Name : "+role.getName()+" "+getText("save.success",locale));	
	   			}
	            reloadContext(request.getSession().getServletContext());
			} catch (RoleExistsException e) {
				log.error(e.getMessage(),e);
				errors.rejectValue("name", "errors.existing.role",
	                    new Object[]{role.getName()}, "duplicate role");
				role.setVersion(originalVersion);
				role.setName(roleName);
				return new ModelAndView("admin/newRole");
			}catch (Exception e) {
				log.error(e.getMessage(),e);
				errors.rejectValue("error.role.save", e.getMessage());
				role.setVersion(originalVersion);
				role.setName(roleName);
				return new ModelAndView("admin/newRole");
			}

   		return new ModelAndView("redirect:roles");
      }
    
    
    @RequestMapping(value = "bpm/admin/editRole",method = RequestMethod.GET)
    public ModelAndView editRole(@RequestParam("id") String id,
   			ModelMap model)
            throws Exception {
    		Role role = roleService.getRoleById(id);
    		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		boolean isRoleEdit=departmentService.getIsDepartmentAdmin(user,role.getRoleDepartment());
		    boolean isDepartmentAdmin=departmentService.getIsDepartmentAdmin(user); 
      	    model.put("isRoleEdit",isRoleEdit);
      	    model.put("isDepartmentAdmin",isDepartmentAdmin);
   	        model.put("role", role);
   	        log.info(role.getName()+" edited Successfully.");
            return new ModelAndView("admin/newRole", model);   
    }
    
    /**
   	 * Updates Order No of Role when the user click on move up and
   	 * move down images in role  grid.
   	 * 
   	 * @param roleJsonString
   	 * @return response after updating the ordernos
   	 */
       @RequestMapping(value = "bpm/admin/updateRoleOrderNos", method = RequestMethod.POST)
       public @ResponseBody String updateRoleOrderNos(@RequestParam("roleJson") String roleJsonString) {
       	JSONObject respObj = new JSONObject();
       	JSONArray responseMsg = new JSONArray();
       	try {
   			List<Map<String,Object>> roleList = CommonUtil.convertJsonToList(roleJsonString);
   			boolean hasUpdated = roleService.updateRoleOrderNos(roleList);
   			if(hasUpdated){
   				respObj.put("isSuccess", "true");
   				responseMsg.put(respObj);
   				return responseMsg.toString();
   			}
   		} catch (Exception e) {
   			log.error("Error while updating order nos for Role",e);
   		}
       	return Constants.EMPTY_STRING;
       			
       }
       
    /**
	 * method for delete the selected roles from users grid
	 * 
	 * @param roleId
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/deleteSelectedRoles", method = RequestMethod.GET)
	public ModelAndView deleteSelectedUsers(@RequestParam("roleIds") String roleId, HttpServletRequest request,ModelMap model) {
		Locale locale = request.getLocale();
		 List<String> roleIdList = new ArrayList<String>();
		 if (roleId.contains(",")) {
			  String[] ids = roleId.split(",");
			  for(String id :ids){
				  roleIdList.add(id);
			  }
			} else {
				roleIdList.add(roleId);
			}
		try{
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String notDeletedRoles=roleService.deleteSelectedRoles(roleIdList,user);
			if(notDeletedRoles==null){
				log.info("Role Names : "+roleId+" "+getText("delete.success",locale));	
				saveMessage(request, getText("success.role.delete",locale));
			}else{
				saveError(request, getText("role.error.delete.privilege", new Object[]{notDeletedRoles, user.getFirstName()+" "+user.getLastName()}  , locale));
				log.error("Error While Deleting Selected Role : "+roleId);
			}
			
			reloadContext(request.getSession().getServletContext());
		}catch(BpmException e){
			log.error(e.getMessage(), e);
			saveError(request, getText("error.role.delete",e.getMessage(),locale));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			saveError(request, getText("error.role.delete",e.getMessage(),locale));
		}	
		return new ModelAndView("redirect:roles");
	}
	
	private void reloadContext(ServletContext context){
		 context.setAttribute(Constants.AVAILABLE_ROLES, new HashSet<LabelValue>(roleService.getAllRoles()));
	}
	
	 /**
	 * To get the user list based on Id(role, group, department) through ajax call
	 * @param id
	 * @param parentNode
	 * @return
	 */
	@RequestMapping(value = "admin/getRoleUsers", method = RequestMethod.GET)
	public @ResponseBody
	List<Map<String, String>> getUsers(@RequestParam("id") String id,
			@RequestParam("parentNode") String parentNode) {
		List<Map<String, String>> userDetailsList = new ArrayList<Map<String, String>>();
		try {
			List<LabelValue> users = null;
			String userType="";
			if (parentNode.equals("roles")) {
				userType=" (Roles)";
				users = roleService.getUsersLabelValueByRoleId(id);
			} else if (parentNode.equals("groups")) {
				userType=" (Groups)";
				users = groupService.getUsersAsLabelValueByGroupId(id);
			} else if (parentNode.equals("departments")) {
				userType=" (Departments)";
				users = departmentService.getUsersByDepartmentId(id);
			}
			if (users != null) {
				for (LabelValue user : users) {
					Map<String, String> userDetail = new HashMap<String, String>();
					userDetail.put("userName", user.getLabel()+userType);
					userDetail.put("userId", user.getValue());
					userDetailsList.add(userDetail);
				}
				return userDetailsList;
			}
          	log.info("Roles Retrived Successfully");
		} catch (Exception e) {
			log.error("Error while getting role users ", e);
		}
		return new ArrayList<Map<String, String>>();
	}
	 
 	/**
     * Gets all departments as a Label value pair
     * 
	 * @return list of department names.
	 */
	 @RequestMapping(value="bpm/admin/getAllRoleNames", method = RequestMethod.GET)
	   public @ResponseBody List<Map<String, String>> getAllRoleNames() {
		  List<Map<String, String>> roleList = new ArrayList<Map<String, String>>();
		  try{
			  List<LabelValue> roles = roleService.getAllRoleNames();
			  if (roles != null){
				  for(LabelValue role : roles){
					  Map<String,String> roleDetail = new HashMap<String, String>();
					  roleDetail.put("name", role.getLabel());
					  roleDetail.put("id", role.getValue());
					  roleList.add(roleDetail);
				  }
				  return roleList;
			  }
	         log.info("Role List Retrived Successfully");
		  }catch(Exception e){
			  log.error("Error while getting roles ",e);
		  }		 
	      return new ArrayList<Map<String,String>>();
	   }
	 
	
    /**
     * Get Role selection tree data for root
     * @param model
     * @param currentNode
     * @param rootNode
     * @param nodeLevel
     * @return
     */
    @RequestMapping(value="bpm/admin/getRoleNodes", method = RequestMethod.GET)
    public @ResponseBody String getRoleRootNodes(ModelMap model,@RequestParam("currentNode") String currentNode, @RequestParam("rootNode") String rootNode, @RequestParam("nodeLevel") int nodeLevel,@RequestParam("isGraduationTree") String isGraduationTree,@RequestParam("userRoles") String userRoles){
	       	JSONArray nodes = new JSONArray();
	       	try {
	       		List<LabelValue> roles =new ArrayList<LabelValue>();
	       		if(isGraduationTree.equals("true")){
	       			List<String> userRoleList=CommonUtil.convertJsonToListStrings(userRoles);
	       			for(String userRole:userRoleList){
	       				LabelValue labelValueObj=new LabelValue();
	       				labelValueObj.setLabel(userRole);
	       				labelValueObj.setValue(userRole);
	       				roles.add(labelValueObj);
	       			}
	       		}else{
	       			roles=roleService.getAllRolesAsLabelValue();
	       		}
	       		
	       		nodes = CommonUtil.getNodesFromLabelValue(roles);
	       	} catch (BpmException e) {
	   			log.warn(e.getMessage(),e);
	   		} catch (Exception e) {
	   			log.warn(e.getMessage(),e);
	       	}
	       	return nodes.toString();
    }
    
    /**
	 * show the Role organization tree
	 * @param title
	 * @param requestorId
	 * @param selectType
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="bpm/admin/roleTree", method = RequestMethod.GET)
    public ModelAndView showRoleTree(@RequestParam("title") String title,@RequestParam("id") String requestorId,@RequestParam("selectType") String selectType,ModelMap model,HttpServletRequest request) throws Exception {
    	List<Role> roleList = roleService.getRoles();
    	model.addAttribute("availableRole", roleList);
    	model.addAttribute("title",title);
    	model.addAttribute("requestorId",requestorId);
    	model.addAttribute("selectType",selectType);
    	model.addAttribute("roleTree",JSTreeUtil.getJsonTreeForRoleList(request,roleList));
    	return new ModelAndView("/admin/showRoleTree", model);
    }
}
