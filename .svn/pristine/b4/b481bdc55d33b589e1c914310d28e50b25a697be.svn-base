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
import org.springframework.ui.velocity.VelocityEngineUtils;
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
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.dao.SearchException;
import com.eazytec.dto.DepartmentDTO;
import com.eazytec.dto.GroupDTO;
import com.eazytec.dto.RoleDTO;
import com.eazytec.dto.UserDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.Department;
import com.eazytec.model.LabelValue;
import com.eazytec.model.User;
import com.eazytec.service.DepartmentExistsException;
import com.eazytec.service.UserService;

/**
 * <p>
 * The controller for department related operations like its CRUD, list, grid
 * view etc on screen
 * </p>
 *
 * @author mathi
 */
@Controller
public class DepartmentController extends BaseFormController {

	/**
	 * Log variable for all child classes. Uses LogFactory.getLog(getClass())
	 * from Commons Logging
	 */
	protected final Log log = LogFactory.getLog(getClass());

	public VelocityEngine velocityEngine;

	private DepartmentService departmentService;

	private RoleService roleService;

	private UserService userService;

	private GroupService groupService;

	@Autowired
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
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
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	/**
	 * method for show department form for create or update.
	 * 
	 * @param request
	 * @param model
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/departmentForm", method = RequestMethod.GET)
	public ModelAndView showDepartmentForm(HttpServletRequest request, ModelMap model) {
		try {
			String administrators = "";
			String interfacePeople = "";
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (request.getParameter("id") != null) {
				Department department = departmentService.getDepartment(request.getParameter("id"));
				if (department.getAdministrators() != null) {
					for (User administrator : department.getAdministrators()) {
						if (administrators.length() == 0) {
							administrators += administrator.getUsername();
						} else {
							administrators += "," + administrator.getUsername();
						}
					}
				}
				if (department.getInterfacePeople() != null) {
					for (User interfacer : department.getInterfacePeople()) {
						if (interfacePeople.length() == 0) {
							interfacePeople += interfacer.getUsername();
						} else {
							interfacePeople += "," + interfacer.getUsername();
						}
					}
				}
				model.addAttribute("administrators", administrators);
				model.addAttribute("interfacePeople", interfacePeople);
				setDepartmentPermissionForEdit(model, user, department.getId());
				model.addAttribute(department);
			} else {
				boolean isPermitToCreate = setDepartmentPermissionValues(model, user);
				if (!isPermitToCreate) {
					saveError(request, "You dont have permission to create Department");
					return new ModelAndView("redirect:departments");
				} else {
					model.addAttribute("department", new Department());
				}
			}
		} catch (Exception e) {
			saveError(request, "Problem while showing department" + e.getMessage());
		}
		return new ModelAndView("admin/departmentForm", model);
	}

	/**
	 * method for save/update department
	 * 
	 * @param department
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/saveDepartment", method = RequestMethod.POST)
	public ModelAndView saveDepartment(@ModelAttribute("department") Department department, BindingResult errors, ModelMap model, HttpServletRequest request) {
		Integer originalVersion = department.getVersion();
		String administrators = request.getParameter("administrators");
		String interfacePeople = request.getParameter("interfacePeople");
		Set<User> administratorsSet = new HashSet<User>();
		Set<User> interfacePeopleSet = new HashSet<User>();
		try {
			Locale locale = request.getLocale();
			if (validator != null) {
				validator.validate(department, errors);
				if (errors.hasErrors()) {
					department.setVersion(originalVersion);
					model.addAttribute("administrators", administrators);
					model.addAttribute("interfacePeople", interfacePeople);
					User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					if (department.getId().isEmpty()) {
						setDepartmentPermissionValues(model, user);
					} else {
						setDepartmentPermissionForEdit(model, user, department.getId());
					}
					return new ModelAndView("admin/departmentForm", model);
				}
			}
			// Existing department check
			if (originalVersion == 0) {
				if (departmentService.isDuplicateDepartment(department.getName())) {
					errors.rejectValue("name", "errors.existing.department", new Object[] { department.getName() }, "duplicate department");
					department.setVersion(originalVersion);
					model.addAttribute("administrators", administrators);
					model.addAttribute("interfacePeople", interfacePeople);
					User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					if (department.getId().isEmpty()) {
						setDepartmentPermissionValues(model, user);
					} else {
						setDepartmentPermissionForEdit(model, user, department.getId());
					}
					return new ModelAndView("admin/departmentForm", model);
				}
			}
			if (department.getSuperDepartment() != null) {
				if (department.getName().equalsIgnoreCase(department.getSuperDepartment())) {
					errors.rejectValue("superDepartment", "errors.superdepartment.department");
					department.setVersion(originalVersion);
					model.addAttribute("administrators", administrators);
					model.addAttribute("interfacePeople", interfacePeople);
					User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					if (department.getId().isEmpty()) {
						setDepartmentPermissionValues(model, user);
					} else {
						setDepartmentPermissionForEdit(model, user, department.getId());
					}
					return new ModelAndView("admin/departmentForm", model);
				}
			}
			// department = setDepartmentFields(department);

			if (administrators != null && !administrators.isEmpty()) {
				if (administrators.contains(",")) {
					String[] ids = administrators.split(",");
					for (String userid : ids) {
						administratorsSet.add(new User(userid, userid));
					}
				} else {
					administratorsSet.add(new User(administrators, administrators));
				}
				department.setAdministrators(administratorsSet);
			} else {
				department.setAdministrators(null);
			}

			if (interfacePeople != null && !interfacePeople.isEmpty()) {
				if (interfacePeople.contains(",")) {
					String[] ids = interfacePeople.split(",");
					for (String userid : ids) {
						interfacePeopleSet.add(new User(userid, userid));
					}
				} else {
					interfacePeopleSet.add(new User(interfacePeople, interfacePeople));
				}
				department.setInterfacePeople(interfacePeopleSet);
			} else {
				department.setInterfacePeople(null);
			}

			if (department.getName().equalsIgnoreCase("Organization")) {
				department.setSuperDepartment(null);
			} else if (department.getIsParent()) {
				department.setSuperDepartment("Organization");
			} else {
				if (department.getSuperDepartment() == null || department.getSuperDepartment().isEmpty()) {
					errors.rejectValue("superDepartment", "errors.empty.superdepartment");
					department.setVersion(originalVersion);
					model.addAttribute("administrators", administrators);
					model.addAttribute("interfacePeople", interfacePeople);
					User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					if (department.getId().isEmpty()) {
						setDepartmentPermissionValues(model, user);
					} else {
						setDepartmentPermissionForEdit(model, user, department.getId());
					}
					return new ModelAndView("admin/departmentForm", model);
				}
			}
			if (originalVersion == 0) {	
				department.setOrderNo(departmentService.generateOrderNo(department.getSuperDepartment()));
			}
			departmentService.saveDepartment(department);
			if (originalVersion == 0) {
				saveMessage(request, getText("department.added", department.getName(), locale));
				log.info("Department Name : " + department.getName() + " " + getText("add.success", locale));
			} else {
				saveMessage(request, getText("department.saved", department.getName(), locale));
				log.info("Department Name : " + department.getName() + " " + getText("save.success", locale));
			}
			model.addAttribute("administrators", administrators);
			model.addAttribute("interfacePeople", interfacePeople);
			reloadContext(request.getSession().getServletContext());
		} catch (DepartmentExistsException e) {
			log.error(e.getMessage(), e);
			errors.rejectValue("name", "errors.existing.department", new Object[] { department.getName() }, "duplicate department");
			department.setVersion(originalVersion);
			model.addAttribute("administrators", administrators);
			model.addAttribute("interfacePeople", interfacePeople);
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (department.getId().isEmpty()) {
				setDepartmentPermissionValues(model, user);
			} else {
				setDepartmentPermissionForEdit(model, user, department.getId());
			}
			return new ModelAndView("admin/departmentForm", model);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			errors.rejectValue("error.department.save", e.getMessage());
			model.addAttribute("administrators", administrators);
			model.addAttribute("interfacePeople", interfacePeople);
			department.setVersion(originalVersion);
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (department.getId().isEmpty()) {
				setDepartmentPermissionValues(model, user);
			} else {
				setDepartmentPermissionForEdit(model, user, department.getId());
			}
			return new ModelAndView("admin/departmentForm", model);
		}
		return new ModelAndView("redirect:/bpm/admin/manageDepartment");
	}

	/**
	 * Updates Order No of Department when the user click on move up and move
	 * down images in departments grid.
	 * 
	 * @param dataDictionaryString
	 * @return response after updating the ordernos
	 */
	@RequestMapping(value = "bpm/admin/updateDepartmentOrderNos", method = RequestMethod.POST)
	public @ResponseBody String updateDepartmentOrderNos(@RequestParam("departmentJson") String departmentJsonString) {
		JSONObject respObj = new JSONObject();
		JSONArray responseMsg = new JSONArray();
		try {
			List<Map<String, Object>> departmentList = CommonUtil.convertJsonToList(departmentJsonString);
			boolean hasUpdated = departmentService.updateDepartmentOrderNos(departmentList);
			if (hasUpdated) {
				respObj.put("isSuccess", "true");
				responseMsg.put(respObj);
				return responseMsg.toString();
			}
		} catch (Exception e) {
			log.error("Error while updating order nos for Department", e);
		}
		return Constants.EMPTY_STRING;

	}

	/**
	 * method to show list of departments are available
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "bpm/admin/departments", method = RequestMethod.GET)
	public ModelAndView showDepartments(@RequestParam(required = false, value = "q") String query) throws Exception {
		Model model = new ExtendedModelMap();
		try {
			/*
			 * List<Department>
			 * departmentList=departmentService.getDepartments();
			 * 
			 * String[]
			 * fieldNames={"departmentId","name","description","departmentOwner"
			 * ,"createdTimeByString","departmentType","orderNo"}; String
			 * script=GridUtil.generateScriptForDepartmentGrid(CommonUtil.
			 * getMapListFromObjectListByFieldNames(departmentList,fieldNames,""
			 * ), velocityEngine); model.addAttribute("script", script);
			 * model.addAttribute(Constants.DEPARTMENT_LIST, departmentList);
			 */
		} catch (SearchException se) {
			model.addAttribute("searchError", se.getMessage());
		}
		return new ModelAndView("admin/departmentList", model.asMap());
	}

	/**
	 * method for delete the selected departments from department grid
	 * 
	 * @param departmentIds
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/deleteSelectedDepartments", method = RequestMethod.GET)
	public ModelAndView deleteSelectedDepartments(@RequestParam("departmentIds") String departmentIds, HttpServletRequest request) throws Exception {
		Locale locale = request.getLocale();
		/*
		 * List<String> departmentIdList = new ArrayList<String>(); if
		 * (departmentIds.contains(",")) { String[] ids =
		 * departmentIds.split(","); for(String id :ids){
		 * departmentIdList.add(id); } } else {
		 * departmentIdList.add(departmentIds); }
		 */
		try {
			List<String> departmentIdList = CommonUtil.convertJsonToListStrings(departmentIds);
			boolean isPermitToDelete = false;
			User logInuser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// check whether the user have permission to delete the department
			// or not
			// if permit department will be delete
			isPermitToDelete = departmentService.removeDepartments(departmentIdList, logInuser, isPermitToDelete);
			if (isPermitToDelete) {
				reloadContext(request.getSession().getServletContext());
				log.info("Department Names : " + departmentIds + " " + getText("delete.success", locale));
				saveMessage(request, getText("success.department.delete", locale));
			} else {
				saveError(request, "You dont have permission to delete all these department");
			}
		} catch (BpmException e) {
			log.error(e.getMessage(), e);
			saveError(request, getText("error.department.delete", e.getMessage(), locale));
		}
		return new ModelAndView("redirect:/bpm/admin/manageDepartment");
	}

	/**
	 * method to set department details
	 *
	 * @param department
	 */
	private Department setDepartmentFields(Department department) {
		List<String> userNames = new ArrayList<String>();
		// List<String> roleNames = new ArrayList<String>();
		/*
		 * if(!department.getDepartmentalRoles().isEmpty()){ for(Role
		 * role:department.getDepartmentalRoles()){
		 * roleNames.add(role.getName().toString()); } List<Role> roles =
		 * roleService.getRolesByNames(roleNames);
		 * department.setDepartmentalRoles(new HashSet<Role>(roles)); }
		 */
		if (!department.getAdministrators().isEmpty()) {
			for (User user : department.getAdministrators()) {
				userNames.add(user.getUsername());
			}
			List<User> users = (List<User>) userService.getUserByUsernames(userNames);
			department.setAdministrators(new HashSet<User>(users));
		}
		userNames.clear();
		if (!department.getInterfacePeople().isEmpty()) {
			for (User user : department.getInterfacePeople()) {
				userNames.add(user.getUsername());
			}
			List<User> users = (List<User>) userService.getUserByUsernames(userNames);
			department.setInterfacePeople(new HashSet<User>(users));
		}
		return department;
	}

	private void reloadContext(ServletContext context) {
		context.setAttribute(Constants.AVAILABLE_DEPARTMENTS, new HashSet<LabelValue>(departmentService.getAllDepartments()));
	}

	/**
	 * method to select users based on the jstree selection ie
	 * Roles,Groups,Depts etc
	 * 
	 * @param departmentIds
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/getDepartmentUserGrid", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public @ResponseBody String getDepartmentUserGrid(@RequestParam("id") String id, @RequestParam("parentNode") String parentNode, String name, HttpServletRequest request) {
		Locale locale = request.getLocale();
		String script = null;
		try {
			log.info("Getting users for selected " + id + " : for : " + parentNode);
			List<UserDTO> users = new ArrayList<UserDTO>();
			if (parentNode.equalsIgnoreCase("organization")) {
				users = userService.getAllUsersByDepartment(id);
			} else if (parentNode.equalsIgnoreCase("group")) {
				users = userService.getAllUsersByGroup(id);
			} else if (parentNode.equalsIgnoreCase("role")) {
				users = userService.getAllUsersByRole(id);
			} else if (parentNode.equalsIgnoreCase("all")) {
				users = userService.getAllUsersDTO(name);
			}

			/*
			 * if (users != null) { userSet = new HashSet<User>(users); }
			 */
			String[] fieldNames = { "id", "username", "fullName", "department", "email", "employeeNumber", "enabled" };
			script = GridUtil.generateScriptForUserGrid(CommonUtil.getMapListFromObjectListByFieldNames(users, fieldNames, ""), velocityEngine);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			saveError(request, getText("error.department.delete", e.getMessage(), locale));
		}
		return script;
	}

	/**
	 * method for Manage Organization
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/manageOrganization", method = RequestMethod.GET)
	public ModelAndView manageOrganization(HttpServletRequest request, ModelMap model) {
		String script = null;
		List<DepartmentDTO> departmentList = departmentService.getAllDepartmentDTO();
		model.addAttribute(Constants.DEPARTMENT_LIST, departmentList);

		List<RoleDTO> roleList = roleService.getRolesDTO();
		model.addAttribute(Constants.ROLE_LIST, roleList);

		List<GroupDTO> groupList = groupService.getGroupsDTO();
		model.addAttribute(Constants.GROUP_LIST, groupList);

		List<UserDTO> users = userService.getAllUsersDTO(null);

		String[] fieldNames = { "id", "username", "fullName", "department", "email", "employeeNumber", "enabled" };
		script = GridUtil.generateScriptForUserGrid(CommonUtil.getMapListFromObjectListByFieldNames(users, fieldNames, ""), velocityEngine);
		model.addAttribute("script", script);
		return new ModelAndView("admin/departmentTree", model);
	}

	/**
	 * method for show Department Tree
	 * 
	 * @param title
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/showDepartmentTree", method = RequestMethod.GET)
	public ModelAndView showDepartmentTree(@RequestParam("title") String title, @RequestParam("id") String departmentId, ModelMap model, @RequestParam("selectType") String selectType) {
		List<Department> departmentList = departmentService.getDepartments();
		model.addAttribute(Constants.DEPARTMENT_LIST, new HashSet<Department>(departmentList));
		model.addAttribute("title", title);
		model.addAttribute("selectType", selectType);
		model.addAttribute("requestorId", departmentId);
		return new ModelAndView("/admin/showDepartmentTree", model);
	}

	/**
	 * To get the department list based on departmentId through ajax call
	 * 
	 * @param id
	 *
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/getDepartmentsByLabelValue", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, String>> getDepartmentsByLabelValue(@RequestParam("id") String id) {
		List<Map<String, String>> departmentDetailsList = new ArrayList<Map<String, String>>();
		try {
			List<LabelValue> departments = null;
			departments = departmentService.getDepartmentsByLabelValue(id);
			if (departments != null) {
				for (LabelValue department : departments) {
					Map<String, String> departmentDetail = new HashMap<String, String>();
					departmentDetail.put("departmentName", department.getLabel());
					departmentDetail.put("departmentId", department.getValue());
					departmentDetailsList.add(departmentDetail);
				}
				return departmentDetailsList;
			}
		} catch (Exception e) {
			log.error("Error while getting departments ", e);
		}
		return null;
	}

	/**
	 * method for show part time Department Tree
	 * 
	 * @param title
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/showPartTimeDepartmentTree", method = RequestMethod.GET)
	public ModelAndView showPartTimeDepartmentTree(@RequestParam("title") String title, @RequestParam("id") String departmentId, ModelMap model) {
		List<Department> departmentList = departmentService.getDepartments();
		model.addAttribute(Constants.DEPARTMENT_LIST, departmentList);
		model.addAttribute("title", title);
		model.addAttribute("requestorId", departmentId);
		return new ModelAndView("/admin/showPartTimeDepartmentTree", model);
	}

	/**
	 * Gets all departments as a Label value pair
	 * 
	 * @return list of department names.
	 */
	@RequestMapping(value = "bpm/admin/getAllDepartmentNames", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, String>> getAllDepartmentNames() {
		List<Map<String, String>> departmentDetailsList = new ArrayList<Map<String, String>>();
		try {
			List<LabelValue> departments = departmentService.getAllDepartmentNames();
			if (departments != null) {
				for (LabelValue department : departments) {
					Map<String, String> departmentDetail = new HashMap<String, String>();
					departmentDetail.put("name", department.getLabel());
					departmentDetail.put("id", department.getValue());
					departmentDetailsList.add(departmentDetail);
				}
				return departmentDetailsList;
			}
			log.info("All Department retrived successfully ");
		} catch (Exception e) {
			log.error("Error while getting departments ", e);
		}
		return new ArrayList<Map<String, String>>();
	}

	@RequestMapping(value = "/admin/getDepartmentsBySuperDep", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, String>> getDepartmentsBySuperDep(@RequestParam("superDep") String superDep) {
		List<Map<String, String>> departmentDetailsList = new ArrayList<Map<String, String>>();
		List<Department> departmentList = new ArrayList<Department>();

		try {
			/*
			 * if(superDep.equals("Departments")) { Map<String,String>
			 * departmentDetail = new HashMap<String, String>();
			 * departmentDetail.put("id", "Organization");
			 * departmentDetail.put("name", "Organization");
			 * departmentDetail.put("superDep","Organization");
			 * departmentDetailsList.add(departmentDetail); } else {
			 */
			departmentList = departmentService.getDepartmentBySuperDepartmentId(superDep);
			if (departmentList != null) {
				for (Department department : departmentList) {
					Map<String, String> departmentDetail = new HashMap<String, String>();
					departmentDetail.put("id", department.getId());
					departmentDetail.put("name", department.getName());
					departmentDetail.put("viewName", department.getViewName());
					departmentDetail.put("superDep", department.getSuperDepartment());
					departmentDetailsList.add(departmentDetail);
				}
			}
			// }
		} catch (Exception e) {
			log.error("Error while getting departments ", e);
		}
		return departmentDetailsList;
	}

	/**
	 * show department selection popup with department tree
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
	@RequestMapping(value = "bpm/admin/showDepartmentSelection", method = RequestMethod.GET)
	public ModelAndView showDepartmentSelection(ModelMap model, @RequestParam("selectionType") String selectionType, @RequestParam("appendTo") String appendTo, @RequestParam("selectedValues") String selectedValues, @RequestParam("callAfter") String callAfter, User user, BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
		JSONArray nodes = new JSONArray();
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			List<LabelValue> organization = departmentService.getOrganizationAsLabelValue();
			nodes = CommonUtil.getNodesFromLabelValue(organization);
			context.put("nodes", nodes);
			context.put("selectionType", selectionType);
			context.put("selection", "department");
			context.put("selectedValues", selectedValues);
			context.put("needContextMenu", true);
			context.put("needTreeCheckbox", true);
			String script = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "organizationTree.vm", context);
			model.addAttribute("script", script);
			model.addAttribute("appendTo", appendTo);
			model.addAttribute("callAfter", callAfter);
			model.addAttribute("selectionType", selectionType);
			model.addAttribute("selectedValues", selectedValues);
		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
		return new ModelAndView("admin/organizationTree", model);
	}

	/**
	 * get child nodes of department
	 * 
	 * @param model
	 * @param currentNode
	 * @param errors
	 * @param request
	 * @param response
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/getDepartmentNodes", method = RequestMethod.GET)
	public @ResponseBody String getDepartmentNodes(ModelMap model, @RequestParam("currentNode") String currentNode, @RequestParam("rootNode") String rootNode, @RequestParam("nodeLevel") int nodeLevel, User user, BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
		Locale locale = request.getLocale();
		JSONArray nodes = new JSONArray();
		try {
			List<LabelValue> organization = departmentService.getOrganizationAsLabelValue();
			nodes = CommonUtil.getNodesFromLabelValue(organization);
		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
			saveError(request, getText("errors.userSelection.getNodes", locale));
		} catch (Exception e) {
			saveError(request, getText("errors.userSelection.getNodes", locale));
			log.warn(e.getMessage(), e);
		}
		return nodes.toString();
	}

	/**
	 * get child nodes of department
	 * 
	 * @param model
	 * @param currentNode
	 * @param errors
	 * @param request
	 * @param response
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/getDepartmentChildNodes", method = RequestMethod.GET)
	public @ResponseBody String getDepartmentChildNodes(ModelMap model, @RequestParam("currentNode") String currentNode, User user, BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
		Locale locale = request.getLocale();
		JSONArray nodes = new JSONArray();
		try {
			List<LabelValue> childNodeList = departmentService.getDepartmentsAsLabelValueByParent(currentNode);
			nodes = CommonUtil.getNodesFromLabelValue(childNodeList);
		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
			saveError(request, getText("errors.userSelection.getNodes", locale));
		} catch (Exception e) {
			saveError(request, getText("errors.userSelection.getNodes", locale));
			log.warn(e.getMessage(), e);
		}
		return nodes.toString();
	}

	@RequestMapping(value = "bpm/admin/getSuperDepartmentChildNodes", method = RequestMethod.GET)
	public @ResponseBody String getSuperDepartmentChildNodes(ModelMap model, @RequestParam("currentNode") String currentNode, User user, BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
		Locale locale = request.getLocale();
		JSONArray nodes = new JSONArray();
		try {
			List<LabelValue> childNodeList = departmentService.getSuperDepartmentsAsLabelValueByParent(currentNode);
			nodes = CommonUtil.getNodesFromLabelValue(childNodeList);
		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
			saveError(request, getText("errors.userSelection.getNodes", locale));
		} catch (Exception e) {
			saveError(request, getText("errors.userSelection.getNodes", locale));
			log.warn(e.getMessage(), e);
		}
		return nodes.toString();
	}

	@RequestMapping(value = "bpm/admin/getUserAdministrationSuperDepartmentNodes", method = RequestMethod.GET)
	public @ResponseBody String getUserAdministrationSuperDepartmentNodes(ModelMap model, @RequestParam("currentNode") String currentNode, @RequestParam("rootNode") String rootNode, @RequestParam("nodeLevel") int nodeLevel, User user, BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
		Locale locale = request.getLocale();
		JSONArray nodes = new JSONArray();
		try {
			User userObject = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<LabelValue> departments = departmentService.getUserAdministrationSuperDepartments(userObject.getId());
			nodes = CommonUtil.getNodesFromLabelValue(departments);
		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
			saveError(request, getText("errors.userSelection.getNodes", locale));
		} catch (Exception e) {
			saveError(request, getText("errors.userSelection.getNodes", locale));
			log.warn(e.getMessage(), e);
		}
		return nodes.toString();
	}

	private boolean setDepartmentPermissionValues(ModelMap model, User user) throws BpmException {
		boolean isAdmin = CommonUtil.isUserAdmin(user);
		boolean isDepartmentAdmin = false;
		if (!isAdmin) {
			// List<String> departmentIdsList =
			// departmentService.getDepartmentAdminDepartmentIds(user.getId());
			// if(departmentIdsList != null && !departmentIdsList.isEmpty()){

			List<LabelValue> departments = departmentService.getUserAdministrationSuperDepartments(user.getId());
			if (departments != null && !departments.isEmpty()) {
				isDepartmentAdmin = true;
				if (departments.size() == 1) {
					for (LabelValue department : departments) {
						model.addAttribute("superDepartment", department.getValue());
						model.addAttribute("superDepartmentLabel", department.getLabel());
					}
				}
			}
			// }
		}
		if (isAdmin || isDepartmentAdmin) {
			model.addAttribute("isDepartmentAdmin", isDepartmentAdmin);
			model.addAttribute("isSystemAdmin", isAdmin);
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	private void setDepartmentPermissionForEdit(ModelMap model, User user, String departmentId) throws BpmException {
		boolean isAdmin = CommonUtil.isUserAdmin(user);
		boolean permitToEdit = false;
		if (!isAdmin) {
			Set<String> departmentIdsSet = new HashSet<String>();
			List<String> departmentIdsList = departmentService.getDepartmentAdminDepartmentIds(user.getId());
			if (departmentIdsList != null && !departmentIdsList.isEmpty()) {
				departmentIdsSet.addAll(departmentIdsList);
			}
			for (String adminDepartmentId : departmentIdsSet) {
				if (adminDepartmentId.equals(departmentId)) {
					model.addAttribute("isDepartmentAdmin", true);
					permitToEdit = true;
				}
			}
		} else {
			model.addAttribute("isSystemAdmin", true);
			permitToEdit = true;
		}
		model.addAttribute("permitToEdit", permitToEdit);
	}

	@RequestMapping(value = "bpm/admin/getUserAdministrationDepartmentNodes", method = RequestMethod.GET)
	public @ResponseBody String getUserAdministrationDepartmentNodes(ModelMap model, @RequestParam("currentNode") String currentNode, @RequestParam("rootNode") String rootNode, @RequestParam("nodeLevel") int nodeLevel, User user, BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
		Locale locale = request.getLocale();
		JSONArray nodes = new JSONArray();
		try {
			User userObject = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<LabelValue> departments = departmentService.getUserAdministrationDepartments(userObject.getId());
			nodes = CommonUtil.getNodesFromLabelValue(departments);
		} catch (BpmException e) {
			log.warn(e.getMessage(), e);
			saveError(request, getText("errors.userSelection.getNodes", locale));
		} catch (Exception e) {
			saveError(request, getText("errors.userSelection.getNodes", locale));
			log.warn(e.getMessage(), e);
		}
		return nodes.toString();
	}

	@RequestMapping(value = "bpm/admin/getCurrentUSerDepartment", method = RequestMethod.POST)
	public @ResponseBody String getCurrentUSerDepartment() {
		String departemntId = CommonUtil.getLoggedInUser().getDepartment().getName();
		return departemntId;
	}

	/**
	 * method to select users based on the jstree selection ie
	 * Roles,Groups,Depts etc
	 * 
	 * @param departmentIds
	 * @param request
	 * 
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/getDepartmentGrid", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public @ResponseBody String getDepartmentGrid(@RequestParam("id") String id, @RequestParam("parentNode") String parentNode, HttpServletRequest request) {
		Locale locale = request.getLocale();
		String script = null;

		List<Department> department = null;
		try {
			log.info("Getting users for selected " + id + " : for : " + parentNode);
			// if (parentNode.equalsIgnoreCase("organization")) {
			// departmentList = departmentService.getAllDepartmentDTO();
			// } else {
			department = departmentService.getDepartmentBySuperDepartmentId(id);
			// }
			/*
			 * if (parentNode.equalsIgnoreCase("organization")) { departmentList
			 * = userService.getAllUsersByDepartment(id); } else if
			 * (parentNode.equalsIgnoreCase("group")) { users =
			 * userService.getAllUsersByGroup(id); } else if
			 * (parentNode.equalsIgnoreCase("role")) { users =
			 * userService.getAllUsersByRole(id); }else if
			 * (parentNode.equalsIgnoreCase("all")) { users =
			 * userService.getAllUsersDTO(); }
			 */

			/*
			 * if (users != null) { userSet = new HashSet<User>(users); }
			 */
			String[] fieldNames = { "departmentId", "viewName", "description", "departmentOwner", "createdTimeByString", "orderNo" };
			script = GridUtil.generateScriptForDepartmentGrid(CommonUtil.getMapListFromObjectListByFieldNames(department, fieldNames, ""), velocityEngine);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			saveError(request, getText("error.department.delete", e.getMessage(), locale));
		}
		return script;
	}

	/**
	 * method for Manage Department
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "bpm/admin/manageDepartment", method = RequestMethod.GET)
	public ModelAndView manageDepartment(HttpServletRequest request, ModelMap model) {
		String script = null;
		List<Department> department = null;
		Locale locale = request.getLocale();
		try {
			List<DepartmentDTO> departmentList = departmentService.getAllDepartmentDTO();
			department = departmentService.getDepartmentBySuperDepartmentId("Organization");
			model.addAttribute(Constants.DEPARTMENT_LIST, departmentList);

			/*
			 * List<RoleDTO> roleList=roleService.getRolesDTO();
			 * model.addAttribute(Constants.ROLE_LIST, roleList);
			 * 
			 * List<GroupDTO> groupList=groupService.getGroupsDTO();
			 * model.addAttribute(Constants.GROUP_LIST, groupList);
			 * 
			 * List<UserDTO> users = userService.getAllUsersDTO();
			 */
			/*
			 * if (users != null) { userSet = new HashSet<User>(users);
			 * 
			 * }
			 */
			String[] fieldNames = { "departmentId", "viewName", "description", "departmentOwner", "createdTimeByString", "orderNo" };
			script = GridUtil.generateScriptForDepartmentGrid(CommonUtil.getMapListFromObjectListByFieldNames(department, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			saveError(request, getText("error.department.delete", e.getMessage(), locale));
		}

		return new ModelAndView("admin/departmentScriptTree", model);
	}

}
