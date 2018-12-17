package com.eazytec.webapp.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.department.service.DepartmentService;
import com.eazytec.bpm.admin.group.service.GroupService;
import com.eazytec.bpm.admin.module.service.ModuleService;
import com.eazytec.bpm.admin.role.service.RoleService;
import com.eazytec.bpm.common.util.ProcessDefinitionUtil;
import com.eazytec.bpm.metadata.form.service.FormDefinitionService;
import com.eazytec.bpm.runtime.listView.service.ListViewService;
import com.eazytec.bpm.runtime.table.service.TableService;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Classification;
import com.eazytec.model.Department;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;
import com.eazytec.model.ListView;
import com.eazytec.model.MetaForm;
import com.eazytec.model.MetaTable;
import com.eazytec.model.Module;
import com.eazytec.model.ModuleRolePrivilege;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.service.ModuleExistsException;
import com.eazytec.service.UserService;
import com.eazytec.util.CsvUtil;
import com.eazytec.util.DateUtil;
import com.eazytec.util.JSTreeUtil;

/**
 * <p>The controller for Module related operations like its CRUD</p>
 * 
 * @author revathi
 *
 */

@Controller
public class ModuleController extends BaseFormController{
	
	private ModuleService moduleService;
	private UserService userService;
	private GroupService groupService;
	private DepartmentService departmentService;
	private FormDefinitionService formService;
	private RoleService roleService;
	//private MenuService menuService;
	private TableService tableService;
	private ListViewService listViewService;
	
	
	
	
	/**
	 * To show the create module screen
	 * @param request
	 * @param model
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping(value = "bpm/module/createModule",method = RequestMethod.GET)
	public ModelAndView showCreateModule(HttpServletRequest request,ModelMap model) throws JSONException{
		Locale locale = request.getLocale();
		/*String administrators = "";
		String departments = "";
		String roles = "";*/
		/*String classifications = "";*/
		try{
			boolean isAdmin=false;
	    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Set<Role> roles=user.getRoles();

			for(Role role:roles){
				if(role.getId().equals("ROLE_ADMIN")){
					isAdmin=true;
					break;
				} 
			}
   	
		 if(request.getParameter("id") != null){
		
	    		Module module = moduleService.getModule(request.getParameter("id")); 
	    		Map<String,Object> moduleDetails=moduleService.getModuleDetails(request.getParameter("id"));
	    		/*
	    		 if(module.getAdministrators() != null){
	        		for(User administrator : module.getAdministrators()){
	        			if(administrators.length() == 0 ) {
	        				administrators+=administrator.getUsername();
	        		    } else {
	        		    	administrators+=","+administrator.getUsername();
	        		    }
	        		}
	        	}
	    			if(module.getDepartments() != null){
	        		for(Department department : module.getDepartments()){
	        			if(departments.length() == 0 ) {
	        				departments+=department.getName();
	        		    } else {
	        		    	departments+=","+department.getName();
	        		    }
	        		}
	        	}
	    		if(module.getRoles() != null){
	        		for(Role role : module.getRoles()){
	        			if(roles.length() == 0 ) {
	        				roles+=role.getName();
	        		    } else {
	        		    	roles+=","+role.getName();
	        		    }
	        		}
	        	}
	    		if(module.getClassification() != null){
	        		for(Classification classification : module.getClassification()){
	        			if(classifications.length() == 0 ) {
	        				classifications+=classification.getId();
	        		    } else {
	        		    	classifications+=","+classification.getId();
	        		    }
	        		}
	        	}*/
	    		//replace ':' with '_' for organization tree
	    		/*if(module.getProcesses() != null){
	        		for(Process process : module.getProcesses()){
	        			String id = process.getId().replaceAll(":", "_");
	        			process.setId(id);
	        		}
	        	}*/
	    		
	    		model.addAttribute("users", moduleDetails.get("users"));
	    		model.addAttribute("viewUsers", moduleDetails.get("viewUsers"));
	    		model.addAttribute("roles", moduleDetails.get("roles"));
	    		
	    		//model.addAttribute("administrators", moduleDetails.get("administrators"));
	    		model.addAttribute("groups", moduleDetails.get("groups"));
	    		model.addAttribute("departments", moduleDetails.get("departments"));
	    		//model.addAttribute("classifications", classifications);
	    		model.addAttribute("module", moduleDetails.get("module"));
	    		
	    		/*if(moduleDetails.get("roles").toString().contains("ROLE_ADMIN")){
	    			System.out.println("2nd" +isAdmin);
	    					isAdmin=true;			
	        	}*/
	    		
	    		/*String users = moduleDetails.get("users").toString();
	    		String userRoles = moduleDetails.get("roles").toString(); 
	    		String group = moduleDetails.get("groups").toString();
	    		String department = moduleDetails.get("departments").toString(); 
	    		
	    		String currentUserRole = user.getRoles().toString(); 
	    		String currentUserGroup = user.getGroups().toString(); 
	    		
	    		List<String> usersList = new ArrayList<String>(Arrays.asList(users.split(",")));
	    		//List<String> viewUsersList = new ArrayList<String>(Arrays.asList(moduleDetails.get("viewUsers").toString().split(",")));
	    		List<String> roleList = new ArrayList<String>(Arrays.asList(userRoles.split(",")));
	    		List<String> groupList = new ArrayList<String>(Arrays.asList(group.split(",")));
	    		List<String> departmentList = new ArrayList<String>(Arrays.asList(department.split(",")));
	    		
	    		List<String> currentUserRoleList = new ArrayList<String>(Arrays.asList(currentUserRole.split(",")));
	    		List<String> currentUserGroupList = new ArrayList<String>(Arrays.asList(currentUserGroup.split(",")));
	    		
	    		if( usersList != null && !usersList.isEmpty() && usersList.contains(user.getUsername())){
	    			isAdmin=true;
	    		}else if( roleList != null && !roleList.isEmpty() && roleList.contains(currentUserRoleList)){
	    			isAdmin=true;
	    		}else if( departmentList != null && !departmentList.isEmpty() && departmentList.contains(user.getDepartment())){
	    			isAdmin=true;
	    		}else if( groupList != null && !groupList.isEmpty() && groupList.contains(currentUserGroupList)){
	    			isAdmin=true;
	    		}else{
	    			
	    		}*/
  		
	    		/*if( moduleDetails.get("users").equals(user.getUsername()) ||  list.contains(users)) ||
	    			moduleDetails.get("groups").equals(user.getGroups()) ||  moduleDetails.get("departments").equals(user.getDepartment())) {
						isAdmin=true;
				} else {
					    isAdmin=false;
				}*/
	    		
	    		model.addAttribute("isEdit", isAdmin);
	    		model.addAttribute("viewRoles", moduleDetails.get("viewRoles"));
	    		model.addAttribute("viewGroups", moduleDetails.get("viewGroups"));
	    		model.addAttribute("viewDepartments", moduleDetails.get("viewDepartments"));
	    		
	    	}else {
	    		model.addAttribute("isEdit", isAdmin);
	    		model.addAttribute("module",new Module());
	    	}
		 //Classification JSON for auto complete
 		//model.addAttribute("classificationList", ProcessDefinitionUtil.getJsonTreeForProcess(request));
	    }catch(Exception e){
			saveError(request, getText("moduel.error.create",e.getMessage(),locale));
			log.error(e.getMessage(), e);
		}
		return new ModelAndView("module/createModule",model);
	}
	
	/**
	 * show manage module screen  to user
	 * @param request
	 * @param model
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping(value = "bpm/module/manageModules",method = RequestMethod.GET)
	public ModelAndView manageModules(HttpServletRequest request,ModelMap model) throws JSONException {
		Locale locale = request.getLocale();
    	model.addAttribute("module",new Module());
    	List<Module> moduleList = moduleService.getAllModuleList();
    	model.addAttribute("availableModules", moduleList);
    	model.addAttribute("parentModules",JSTreeUtil.getJsonTreeForModuleList(request,moduleList,false));
    	//if(moduleList!= null && !moduleList.isEmpty()){
    		return new ModelAndView("module/manageModules",model);
//    	}else{
//    		return showCreateModule(request,model);
//    	}
	}
	
	
	@RequestMapping(value = "bpm/module/manageModuleRelation",method = RequestMethod.GET)
	public ModelAndView manageModuleRelation(HttpServletRequest request,ModelMap model){
		Locale locale = request.getLocale();
		 Map <String,String> formDetail = new HashMap<String, String>();
		try{		
			if(model.containsKey("formDetail")){
				formDetail = (Map <String,String>) model.get("formDetail");
				model.putAll(formDetail);
			}
			String isFormCancel=request.getParameter("isFormCancel");
			String isTableCancel = request.getParameter("isTableCancel");
			if(isFormCancel!=null && isFormCancel.equals("true")){
				  formDetail.put("isFormShow", "true");
		          formDetail.put("formModuleId", request.getParameter("moduleId"));
		          model.putAll(formDetail);
			}
			if(isTableCancel != null && isTableCancel.equals("true")){
				 formDetail.put("isTableCancel", "true");
		         formDetail.put("formModuleId", request.getParameter("moduleId"));
		         model.putAll(formDetail);
			}
	    	model.addAttribute("module",new Module());
	    	List<Module> moduleList = moduleService.getAllModuleList();
	    	model.addAttribute("availableModules", moduleList);
	    	model.addAttribute("moduleTree",JSTreeUtil.getJsonTreeForModuleWithRelation(moduleList));
	    }
	    catch(Exception e){
	    	saveError(request, getText("module.relation.show.error",e.getMessage(),locale));
	    	log.error(e.getMessage(), e);
	    }
    	return new ModelAndView("module/manageModuleRelation",model);
	}
	
	@RequestMapping(value = "bpm/module/getAllModule",method = RequestMethod.GET)
	public @ResponseBody Map<String,String> getAllModule(HttpServletRequest request)throws Exception {
        Locale locale = request.getLocale();
        Map<String, String> allModule=new HashMap<String, String>();
           try {
        	   List<Module> moduleList = moduleService.getAllModuleList();
        	   allModule.put("moduleTree", JSTreeUtil.getJsonTreeForModuleWithRelation(moduleList).toString());
           } catch (Exception e) {
        	saveError(request, getText("module.relation.show.error",e.getMessage(),locale));
        	log.error(e.getMessage(), e);
        }
        return allModule;
    }
	
	@RequestMapping(value = "bpm/listView/getAllListViewTemplateByModule", method = RequestMethod.GET)
	public @ResponseBody Map<String,List<Map<String,String>>> getAllTemplateListviewByModule(HttpServletRequest request) throws Exception{
	    Map<String,List<Map<String,String>>> templateListView = new HashMap<String,List<Map<String,String>>>();
		
	    Locale locale=request.getLocale();
	    try{
			 List<Module> moduleList = moduleService.getAllModuleList();
			 for(Module module: moduleList){
				  List<Map<String,String>> listViewModuleList = new ArrayList<Map<String,String>>();
				  Set<ListView> moduleListView =  module.getListViews();
				  	for(ListView listView: moduleListView){
				  		if(listView.getIsTemplate() == true && listView.getIsDelete() == false && listView.isActive() == true){
				  			Map<String,String> listViewMap = new HashMap<String,String>();
				  			listViewMap.put("listViewName", listView.getViewName());
				  			listViewMap.put("listViewId", listView.getId());
							listViewModuleList.add(listViewMap);
				  		}
				  	}
				  	templateListView.put(module.getName(),listViewModuleList);
			   }
		} catch(Exception e){
			saveError(request, getText("listView.error.labelValue",e.getMessage(),locale));
        	log.error(e.getMessage(), e);
		}
		return templateListView;
	}
	
	
	/**
	 * To change the module order
	 * @param moduleId
	 * @param orderingType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="bpm/module/changeModuleOrder",method = RequestMethod.GET)
	public String changeModuleOrder(@RequestParam("currentNodeOrder") int currentNodeOrder,@RequestParam("replaceNodeOrder") int replaceNodeOrder,HttpServletRequest request,ModelMap model){
		Locale locale = request.getLocale();
		try{
			if(moduleService.changeModuleOrder(currentNodeOrder,replaceNodeOrder)){
				saveMessage(request, getText("module.ordering.success", locale));
		    }else{
				saveError(request, getText("module.ordering.notAllowed",locale));
			}
		}catch(Exception e){
			saveError(request, getText("module.ordering.error",locale));
			log.error(e.getMessage(), e);
		}
		return "redirect:manageModuleRelation";
	}
	
	
	/*@RequestMapping(value = "bpm/module/endUserModule",method = RequestMethod.GET)
	public ModelAndView endUserModule(HttpServletRequest request,ModelMap model) throws JSONException {
		Locale locale = request.getLocale();
		try{
		User user = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
		Department department = user.getDepartment();
		List<Module> moduleList = moduleService.getAllParentModuleList();
		List<Module> endUserModule = new ArrayList<Module>(); 
		Set<Module> endModule = new HashSet<Module>();
		//form only current user module
		for(Module module :moduleList){
			Set<User> userList = module.getAdministrators();
			Set<Department> departmentList = module.getDepartments();
			boolean isAdded = false;
			if(userList.contains(user)){
				endModule.add(module);
				isAdded = true;
			}else if(departmentList.contains(department)){
				endModule.add(module);
				isAdded = true;
			}
			//Child to add if its parent doesnt have privilege to ahow end user
			//temporary Fix. Need to Fix after phase-3 release
			if(!isAdded){
				Set<Module> childModule = module.getChildModules();
				Module modToAdd = new Module();
				for(Module chilMod : childModule){
					modToAdd = chilMod;
					Set<User> childUserList = chilMod.getAdministrators();
					Set<Department> childDpartmentList = chilMod.getDepartments();
					Set<Module> parentMod = chilMod.getParentModules();
					boolean isAdd = true;
					for(Module parMod : parentMod){
					Set<Department> parDpartmentList = parMod.getDepartments();
					Set<User> parUserList = parMod.getAdministrators();
					if( (parUserList.contains(user))){
						isAdd = false;
					}else if( parDpartmentList.contains(department)){
						isAdd = false;
					}
					}
					if(isAdd &&(childUserList.contains(user) || childDpartmentList.contains(department))){
						endModule.add(modToAdd);
					}
					
				}
				
			}
		}
		//remove child module from list, which module parent already in List 
		for(Module module :endUserModule){
			if(!module.getIsParent()){
				Set<Module> parentModule = module.getParentModules();
				for(Module mod :parentModule){
					Set<User> userList = mod.getAdministrators();
					Set<Department> departmentList = mod.getDepartments();
					
					if(userList.contains(user)){
						endUserModule.remove(module);
					}else if(departmentList.contains(department)){
						endUserModule.remove(module);
					}						
				}
				if(parentModule.containsAll(endUserModule)){
					endUserModule.remove(module);
				}
			}
		}
        model.addAttribute("module",new Module());
        endUserModule.addAll(endModule);
    	model.addAttribute("availableModules", endUserModule);
    	model.addAttribute("currentUser", user);
    	model.addAttribute("currentDepartment", department);
    	model.addAttribute("endUserModules",JSTreeUtil.getJsonTreeForModuleList(request,endUserModule,false));
		}catch(Exception e){
			saveError(request, getText("moduel.end.error",e.getMessage(),locale));
		}
    		return new ModelAndView("module/endUserModule",model);

	}*/
	
	/*private String replaceLast(String string, String from, String to) {
	     int lastIndex = string.lastIndexOf(from);
	     if (lastIndex < 0) return string;
	     String tail = string.substring(lastIndex).replaceFirst(from, to);
	     return string.substring(0, lastIndex) + tail;
	}*/
	/**
	 * Used to save the newly created module or update the module
	 * @param request
	 * @param parentModule1
	 * @param module
	 * @param errors
	 * @param model
	 * @return
	 * @throws JSONException 
	 */
	@RequestMapping(value = "bpm/module/saveModule", method = RequestMethod.POST)
   	public ModelAndView saveModule(HttpServletRequest request, @ModelAttribute("module") Module module,
   			BindingResult errors, ModelMap model,@RequestParam("screenName") String screenName) throws JSONException {
		Locale locale = request.getLocale();
		try{
			boolean isCreate = false;
			String userName = request.getUserPrincipal().getName();
			Module existModule = null;
			if(module.getId()!=null && module.getId()!=""){
				existModule = moduleService.getModule(module.getId());
			}
			
			//Set the relationship while update the module
			if(existModule!=null){
				module.setForms(existModule.getForms());
				module.setTables(existModule.getTables());
				module.setCreatedBy(existModule.getCreatedBy());
			}
			if(module.getId() == null || module.getId().isEmpty() || (existModule!= null && !(existModule.getId().equalsIgnoreCase(module.getId())) && existModule.getName().equalsIgnoreCase(module.getName()))){
				isCreate = true;
				module.setCreatedBy(userName);
			}			
				
			if(existModule == null || !isCreate){
						if(module.getId() == null || module.getId() == ""){
							module.setId(null);
			   			}else{
			   				module.setCreatedTime(existModule.getCreatedTime());
		   					module.setUpdatedTime(new Date());
			   			}/*else{
			   				Module parentModule = moduleService.getModule(module.getId());
			   				module.setChildModules(parentModule.getChildModules());
			   			}*/
						/*if (validator != null) { // validator is null during testing
			                validator.validate(module, errors);
			                if (errors.hasErrors()) { // don't validate when deleting
			                	return new ModelAndView("module/createModule");
							}
			            }*/
					
					//Temp fix to save form values
					Map<String, String[]> formValues =  request.getParameterMap();
					/*if(formValues.containsKey("forms")){
						List<String> formIds = Arrays.asList(formValues.get("forms"));
						List<MetaForm> forms = formService.getDynamicFormsByIds(formIds);
						module.setForms(new HashSet<MetaForm>(forms));
					}*/
					
					/*if(module.getIsParent()==true){
							module.setParentModules(null);
						}else{
							if(formValues.containsKey("parentModule")){
								List<String> moduleIds = Arrays.asList(formValues.get("parentModule"));
								List<Module> modules = moduleService.getModulesByIds(moduleIds);
								module.setParentModules(new HashSet<Module>(modules));
							}
						}*/
					/*if(formValues.containsKey("processes")){
						List<String> processIds = Arrays.asList(formValues.get("processes"));
						//replace '_' with ':' for organization tree
						List<String> newProcess = new ArrayList<String>();
						for(String id :processIds){
							String str1 = replaceLast(id, "_", ":");
							String str2 = replaceLast(str1, "_", ":");
							newProcess.add(str2);
						}
						List<Process> process = rtProcessService.getProcessByIds(newProcess);
						module.setProcesses(new HashSet<Process>(process));
					}*/
					module.setUpdatedBy(userName);
					//try {
					module=moduleService.saveModule(module);
					module=setModuleValuse(module,request);
					/*} catch (ModuleExistsException e) {
						saveError(request, getText("errors.existing.module",module.getName(),locale));
						
						return new ModelAndView("module/manageModules");
					}*/
					model.addAttribute("availableModules",moduleService.getAllModules(false));
					model.addAttribute("module",new Module());
					saveMessage(request, getText("module.save.success", module.getName(), locale));
					if(isCreate){
						log.info("Module '"+module.getName()+"' created successfully");
					}else{
						log.info("Module '"+module.getName()+"' updated successfully");
					}
					
			}else{
				saveError(request, getText("errors.existing.module",module.getName(),locale));
			}
		}
		catch(Exception e){
			saveError(request, getText("module.save.error",e.getMessage(),locale));
			log.error(e.getMessage(), e);
			e.printStackTrace();
			
		}
    	//model.addAttribute("moduleTree",getJsonTreeForModuleList(request));
		//if(screenName.equalsIgnoreCase("managModules")){
			return manageModuleRelation(request,model);
		/*}else{
			return endUserModule(request, model);
		}*/
		
	}
	
	/**
	 * Copy the module in module organization tree from one parent to another
	 * @param request
	 * @param nodeId
	 * @param parentNodeId
	 * @param model
	 * @return
	 * @throws JSONException
	 * @throws ModuleExistsException
	 *//*
	@RequestMapping(value = "bpm/module/copyModule", method = RequestMethod.GET)
   	public ModelAndView copyModule(HttpServletRequest request, @RequestParam("nodeId") String nodeId,@RequestParam("parentNodeId") String parentNodeId, ModelMap model,@RequestParam("screenName") String screenName) throws JSONException{
		Locale locale = request.getLocale();
		try{
			List<String> moduleIds = new ArrayList<String>();
			moduleIds.add(nodeId);
			moduleIds.add(parentNodeId);
			List<Module> modules =  moduleService.getModulesByIds(moduleIds);
			Module currentModule = new Module();
			Module parentModule =new Module();	
			for(Module module : modules){
				if(nodeId.equalsIgnoreCase(module.getId())){
					currentModule = module;
				}else{
					parentModule = module;
				}
				
			}
			Set<Module> currentModuleSet = new HashSet<Module>();
			currentModuleSet = currentModule.getParentModules();
			currentModuleSet.add(parentModule);
			currentModule.setParentModules(currentModuleSet);
				moduleService.saveModule(currentModule);
			
			model.addAttribute("module",parentModule);
		}catch(Exception e){
			saveError(request, getText("module.copy.error",e.getMessage(),locale));
		}
		if(screenName.equalsIgnoreCase("managModules")){
			return manageModules(request,model);
		}else{
			return endUserModule(request, model);
		}
		
	}
	
	*//**
	 * Change the module order in organization tree form one place to another place
	 * @param request
	 * @param currentModuleId
	 * @param movedModuleId
	 * @param model
	 * @return
	 * @throws JSONException
	 * @throws ModuleExistsException
	 *//*
	@RequestMapping(value = "bpm/module/migrationModule", method = RequestMethod.GET)
   	public ModelAndView migrationModule(HttpServletRequest request, @RequestParam("currentModule") String currentModuleId,@RequestParam("movedModule") String movedModuleId,@RequestParam("screenName") String screenName, ModelMap model) throws JSONException, ModuleExistsException {
		Locale locale = request.getLocale();
		try{
			List<String> moduleIds = new ArrayList<String>();
			moduleIds.add(currentModuleId);
			moduleIds.add(movedModuleId);
			List<Module> modules =  moduleService.getModulesByIds(moduleIds);
			Module currentModule = new Module();
			Module movedModule =new Module();	
			for(Module module : modules){
				if(currentModuleId.equalsIgnoreCase(module.getId())){
					currentModule = module;
				}else{
					movedModule = module;
				}
				
			}
			int currentModuleOrder = currentModule.getModuleOrder();
			int movedModuleOrder = movedModule.getModuleOrder();
			currentModule.setModuleOrder(movedModuleOrder);
			movedModule.setModuleOrder(currentModuleOrder);
			moduleService.saveModule(currentModule);
			moduleService.saveModule(movedModule);
			model.addAttribute("module",movedModule);	
		}catch(Exception e){
			saveError(request, getText("module.migration.error",e.getMessage(),locale));
		}
		if(screenName.equalsIgnoreCase("managModules")){
			return manageModules(request,model);
		}else{
			return endUserModule(request, model);
		}
		
	}*/
	
	@RequestMapping(value = "bpm/module/deleteModule", method = RequestMethod.GET)
	public String deleteModule(HttpServletRequest request,@RequestParam("moduleId") String moduleId,@RequestParam("moduleName") String moduleName,@RequestParam("screenName") String screenName){
		Locale locale = request.getLocale();
		try{
			String removeStatus=moduleService.removeModule(moduleId);
			if(removeStatus.equals("true")){
				saveMessage(request, getText("module.delete.success", locale));
				log.info("Module '"+moduleName+"' deleted Sucessfully");
			}else{
				saveError(request, getText("module.contains.data",removeStatus,locale));
			}
			
		}catch(Exception e){
			saveError(request, getText("module.delete.error",locale));
			log.error(e.getMessage(), e);
		}
		/*if(screenName.equalsIgnoreCase("managModules")){
			return "redirect:manageModules";
		}else{
			return "redirect:endUserModule";
		}*/
		
		return "redirect:manageModuleRelation";
	}
	
	/**
	 * Show the classification tree structure
	 * @throws Exception
	 */
	@RequestMapping(value="bpm/admin/classificationTreeList", method = RequestMethod.GET)
    public ModelAndView classificationTreeList(@RequestParam("title") String title,@RequestParam("id") String requestorId,@RequestParam("selectType") String selectType,@RequestParam("selectedValue") String selectedValue,ModelMap model,HttpServletRequest request) throws Exception {
    	List<Classification> classificationList = moduleService.getAllClassificationList();
    	model.addAttribute("classifications", classificationList);
    	model.addAttribute("title",title);
    	model.addAttribute("requestorId",requestorId);
    	model.addAttribute("selectType",selectType);
    	model.addAttribute("selectedValue",selectedValue);
    	model.addAttribute("classificationTree", ProcessDefinitionUtil.getJsonTreeForProcess(request));
    	return new ModelAndView("showClassificationTree", model);
    }
	
	
	/**
	 * Get all module for auto complete
	 * @param request
	 * @return
	 * @throws BpmException
	 */
	  @RequestMapping(value="/bpm/table/allModules", method = RequestMethod.GET)
      public @ResponseBody List<Map<String, String>> allModules(HttpServletRequest request) throws BpmException{
        List<Map<String, String>> moduleDetailsList = new ArrayList<Map<String, String>>();
         try{
        	 String privilegeNeeded= request.getParameter("isPrivilegeNeeded");
        	 
        	 boolean isPrivilegeNeeded=false;
        	 if(privilegeNeeded!=null){
        		 isPrivilegeNeeded=true;
        	 }
        	 
             List<LabelValue> allModules = moduleService.getAllModules(isPrivilegeNeeded);
             if (allModules != null){
                 for(LabelValue module : allModules){
                     Map<String,String> moduleDetail = new HashMap<String, String>();
                     moduleDetail.put("moduleName", module.getLabel());
                     moduleDetail.put("id", module.getValue());
                     moduleDetailsList.add(moduleDetail);
                 }
                 return moduleDetailsList;
             }
         }catch(Exception e){
             log.error("Error while getting all tables "+e);
         }
         return new ArrayList<Map<String, String>>();
      }
	
	
	 
	/**
	 * Show the module organization tree
	 * @param title
	 * @param requestorId
	 * @param moduleId
	 * @param selectType
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value="bpm/admin/moduleTree", method = RequestMethod.GET)
    public ModelAndView showModuleTree(ModelMap model, @RequestParam("selectionType") String selectionType, 
    		@RequestParam("appendTo") String appendTo, @RequestParam("selectedValues") String selectedValues, 
    		@RequestParam("callAfter") String callAfter, User user,	BindingResult errors, 
    		HttpServletRequest request, HttpServletResponse response,@RequestParam("moduleId") String moduleId) throws Exception {
		List<LabelValue> modules = moduleService.getallmo;
		LabelValue currentModule = null;
		JSONArray nodes = new JSONArray();
		if(!moduleId.isEmpty()){
			currentModule = moduleService.getModuleAsLabelValueForId(moduleId);
			
			// remove current module
			modules.remove(currentModule);
			}
			 	
		nodes = CommonUtil.getNodesFromLabelValue(modules);
		model.addAttribute("nodes", nodes);
		model.addAttribute("nodeDetail",modules );
		model.addAttribute("selectionType", selectionType);
		model.addAttribute("selection", "module");
		model.addAttribute("selectedValues", selectedValues);
		model.addAttribute("appendTo",appendTo);
		model.addAttribute("callAfter",callAfter);
		model.addAttribute("selectionType",selectionType);
		model.addAttribute("selectedValues",selectedValues);
	  	return new ModelAndView("/admin/showModuleTree", model);
    }	*/
	/**
	 * Export the module information to CSV file for given module id
	 * @param response
	 * @param request
	 * @param moduleId
	 * @throws IOException
	 */	
	@RequestMapping(value="bpm/module/moduelCSVExport", method = RequestMethod.GET)
	public void moduelCSVExport(HttpServletResponse response,HttpServletRequest request,@RequestParam("moduleId") String moduleId) throws IOException{
		ServletOutputStream out = response.getOutputStream(); 
		Locale locale = request.getLocale();
		try{
			 HSSFWorkbook wb = new HSSFWorkbook();
			 Module module = moduleService.getModule(moduleId);
			 	//Module sheet
			 	getModuleInfo(module,wb);
			 	//User sheet
		        /*if(module.getAdministrators()!=null){		        	 
		        	getUserRelationInfo(module,wb);
		        }*/
		        //Role Sheet
		        /*if(module.getRoles()!=null){		        	 
		        	getRoleInfo(module,wb);
		        }*/
		        //Department sheet
		       /* if(module.getDepartments() != null){
		        	getDepartmentRelationInfo(module,wb);
		        }*/
		        //Menu sheet
		        /*if(module.getMenus()!=null){
		        	getMenuInfo(module,wb);
		        }*/
		        //Form Sheet
		        if(module.getForms() != null){
		        	getMetaFormInfo(module,wb);
		        }
		        if(module.getTables()!=null){
		        	getMetaTableInfo(module,wb);
		        }
		        if(module.getListViews()!=null){
		        	getListViewInfo(module,wb);
		        }
		        //Process Sheet
		       /* if(module.getProcesses() != null){
		        	getProcessInfo(module,wb);
		        }*/
		        //ParetModuleSheet
		        /*if(module.getParentModules() != null){
		        	getParentModuleInfo(module, wb);
		        }*/
		      //classificationSheet
		        /*if(module.getClassification() != null){
		        	getClassificationInfo(module, wb);
		        }*/
		        Date date = new Date();
		         //Get current date string and append with file name
		        String currentDate = DateUtil.convertDateToDefalutDateTimeString(date);
		        response.setHeader("Content-Disposition", "attachment; filename="+module.getName().replaceAll(" ", "_")+"_"+currentDate+".xls");
		        response.setContentType("application/vnd.ms-excel"); 		        
		        wb.write(out);
		 }catch(Exception e){
			 saveError(request, getText("module.csv.export.error",e.getMessage(),locale));
			 log.error(e.getMessage(), e);
		 }finally{
			 if(out != null){
				 out.flush(); 
			     out.close(); 
			 }
		 }

		    
	}
	
	/**
	 * For module export 
	 * @param response
	 * @param request
	 * @param moduleId
	 * @throws IOException
	 */
	@RequestMapping(value="bpm/module/moduleMigrationExport",method = RequestMethod.GET)
	public void moduleMigrationExport(HttpServletResponse response,HttpServletRequest request,@RequestParam("moduleId") String moduleId) throws IOException{
		ServletOutputStream out = response.getOutputStream(); 
		Locale locale = request.getLocale();
		 Date date = new Date();
		 //Get current date string and append with file name
        String currentDate = DateUtil.convertDateToDefalutDateTimeString(date);
    
		 Module module = moduleService.getModule(moduleId);
		 Map<String,Object> formvalidation=moduleService.isValidFormTable(moduleId);
		 if((Boolean)formvalidation.get("isValid")){
			    response.setContentType("text/x-sql");
		        response.setHeader("Content-Disposition", "attachment; filename="+module.getName().replaceAll(" ", "_")+"-"+module.getId()+"-"+currentDate+".sql");
		        OutputStream dumpOutPut = response.getOutputStream();
				 try{
				 	  String dataBaseName=getText("table.dataBase.name",null);
			          String bdUserName=getText("table.dataBase.userName",null);
			          String bdPassword=getText("table.dataBase.password",null);
			          List<String> dataBaseInfo=new ArrayList<String>();
			          dataBaseInfo.add(dataBaseName);
			          dataBaseInfo.add(bdUserName);
			          dataBaseInfo.add(bdPassword);
			          moduleService.getModuleDump(dumpOutPut,moduleId,dataBaseInfo);
			          log.info("Module '"+module.getName()+"' Export successfully");
			         // return "redirect:manageModuleRelation";
			     }catch(EazyBpmException e){
					 saveError(request, getText("module.csv.export.error",locale));
					 log.error(e.getMessage());
					// return "redirect:manageModuleRelation";
				 }finally{
					 if(out != null){
						 out.flush(); 
					     out.close(); 
					 }
				 }
		 }else{
			// saveError(request, getText("module.csv.export.error",locale));
			 saveError(request, getText("module.csv.export.error",new Object[]{formvalidation.get("moduleMessage")},locale));
			 log.error((String)formvalidation.get("moduleMessage"));
			 response.sendError(400);
			 
		 }
	}
	
	/**
	 * For module import
	 * @param response
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="bpm/module/moduleMigrationImport",method = RequestMethod.POST)
	public String moduleMigrationImport(HttpServletResponse response,HttpServletRequest request) throws IOException{
		 Locale locale = request.getLocale();
         String dataBaseName=getText("table.dataBase.name",null);
         String bdUserName=getText("table.dataBase.userName",null);
         String bdPassword=getText("table.dataBase.password",null);
         String fileNameWithPath="";
         String fileName="";
         
         MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	        Map<String, List<MultipartFile>> fileInList = multipartRequest.getMultiFileMap();
	        List<MultipartFile> files =fileInList.get("file");
	       // the directory to upload to
	         String uploadDir = getServletContext().getRealPath("/resources") + "/"+ request.getRemoteUser() + "/";
	         // Create the directory if it doesn't exist
	         File dirPath = new File(uploadDir);
	         if (!dirPath.exists()) {
	             dirPath.mkdirs();
	         }
	         //iterate the list of file and create the file in specified path and deploy it
	        if(null != files && files.size() > 0) {
	            for (MultipartFile multipartFile : files) {
	                // retrieve the file data
	                InputStream stream = multipartFile.getInputStream();
	               
	                // write the file to the file specified
	                OutputStream bos = new FileOutputStream(uploadDir + multipartFile.getOriginalFilename());
	                int bytesRead;
	                byte[] buffer = new byte[8192];
	                while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
	                	bos.write(buffer, 0, bytesRead);
	                }
	                bos.close();
	                // close the stream
	                stream.close();
	                fileName=multipartFile.getOriginalFilename();
	                fileNameWithPath = dirPath.getAbsolutePath() + Constants.FILE_SEP + multipartFile.getOriginalFilename();
	              	
	                if(fileName.contains("Multi_Table_Dump_")){
	                	saveError(request, getText("errors.invalid.module",fileName,locale));
   	        		 	return "redirect:manageModuleRelation";
	                }
	                
	                //Get the first line of the sql file 
	            	 BufferedReader bf = new BufferedReader(new FileReader(fileNameWithPath));
		                String komut = "";
		                String tableNames = "";
		                String formNames = "";
		                String listViewName = "";
		                String dumpCreatedUserId = "";
		                int i=0;
		                while ((komut = bf.readLine()) != null) {
		                   if (komut.length() != 0) {
		                        if (komut.charAt(komut.length() - 1) == ';') {
		                        	if(i==0){
		                        		tableNames=komut;
		                        	}
		                        	if(i==1){
		                        		formNames=komut;
		                        	}
		                        	if(i==2){
		                        		listViewName=komut;
		                        	}

		                        	if(i==3){
		                        		dumpCreatedUserId=komut;
		                        	}
		                        	i++;
		                        }
		                   }
		                }
		                
		                if(dumpCreatedUserId!="" && !dumpCreatedUserId.isEmpty()){
		                	dumpCreatedUserId=dumpCreatedUserId.replaceAll("--", " ");
		                	dumpCreatedUserId=dumpCreatedUserId.replace(";", "");
		                	User userObj=userService.getUserById(dumpCreatedUserId.trim());
		                	if(userObj==null){
		                		saveError(request, getText("errors.import.existing.module.userName",dumpCreatedUserId.trim(),locale));
		   	        		 	return "redirect:manageModuleRelation";
		                	}
		                }
		              
		                if(tableNames!="" && !tableNames.isEmpty()){
		                	tableNames =tableNames.replaceAll("--", " ");
		                	List<MetaTable> metaTable=tableService.getTableDetailsByNames(tableNames);
		                    String[] moduleName=fileName.split("-");
		                	if(!metaTable.isEmpty()){
		                		saveError(request, getText("errors.import.existing.module.table",moduleName[0],locale));
		   	        		 	return "redirect:manageModuleRelation";
		                	}
		                }
		                
		                if(formNames!="" && !formNames.isEmpty()){
		                	formNames =formNames.replaceAll("--", " ");
		                	formNames=formNames.replace(";", "");
		                	List<MetaForm> metaTable=formService.getFormDetailsByNames(formNames);
		                    String[] moduleName=fileName.split("-");
		                	if(!metaTable.isEmpty()){
		                		saveError(request, getText("errors.import.existing.module.form",moduleName[0],locale));
		   	        		 	return "redirect:manageModuleRelation";
		                	}
		                }
		                
		                if(listViewName!="" && !listViewName.isEmpty()){
		                	listViewName =listViewName.replaceAll("--", " ");
		                	listViewName=listViewName.replace(";", "");
		                	List<ListView> metaTable=listViewService.getListViewDetailsByNames(listViewName);
		                    String[] moduleName=fileName.split("-");
		                	if(!metaTable.isEmpty()){
		                		saveError(request, getText("errors.import.existing.module.listView",moduleName[0],locale));
		   	        		 	return "redirect:manageModuleRelation";
		                	}
		                }
		        }
	        } 
	       String[] moduleName=fileName.split("-");
	        if(moduleName[1]!=""){
	        	List<String> temp=new ArrayList<String>();
	        	temp.add(moduleName[1]);
	        	List<Module> tempModule=moduleService.getModulesByIds(temp);//moduleService.isModuleExist(moduleName[0]);
	        	if(tempModule!=null){
	        		 saveError(request, getText("errors.import.existing.module",moduleName[0],locale));
	        		 return "redirect:manageModuleRelation";
	        	}
	        	
	        	Module moduleObj=moduleService.getModuleByName(moduleName[0]);
	        	if(moduleObj!=null){
	        		saveError(request, getText("errors.import.existing.module",moduleName[0],locale));
	        		 return "redirect:manageModuleRelation";
	        	}
	        	
	        }
	        
	        if(!tableService.getDataBaseSchema().equals("mysql")){
	        	BufferedReader bf = new BufferedReader(new FileReader(fileNameWithPath));
	        	 String queryString = "";
	        	 try {
	        		 int i=0;
			        	 while ( (queryString = bf.readLine()) != null) {
			                   if (queryString.length() != 0 && !queryString.startsWith("--")) {
			                	   if (queryString.charAt(queryString.length() - 1) == ';') {
			                		 	StringBuffer tempQuery = new StringBuffer(queryString);
			                		 	if(tempQuery.lastIndexOf(";")>0){
			                		 		tempQuery.deleteCharAt(tempQuery.lastIndexOf(";"));
			                    		}

			                		   tableService.executeModuleRelationQuery(tempQuery.toString().replaceAll("`", "").replaceAll(",true,", ",1,").replaceAll(",false,", ",0,"));
			                	   }
			                   }
			                   i++;
			                }
		        	 saveMessage(request, getText("module.csv.import.success",locale));
	        	 }catch(Exception e) {
	        		 saveError(request, getText("module.migration.error",e.getMessage(),locale));
	        		
	        	 }
	        	 
	        }else{
	        	String[] executeCmd = new String[]{"mysql", "--user=" + bdUserName, "--password=" + bdPassword, dataBaseName,"-e", "source "+fileNameWithPath};
	            
	            Process runtimeProcess;
	               try {
	        
	                   runtimeProcess = Runtime.getRuntime().exec(executeCmd);
	                   int processComplete = runtimeProcess.waitFor();
	                   if (processComplete >= 0) {
	                       saveMessage(request, getText("module.csv.import.success",locale)); 
	                   }
	             }catch(Exception e) {
	                    saveError(request, getText("module.migration.error",e.getMessage(),locale));
	               }
	        }
	     
        //return "redirect:defaultTable";
         return "redirect:manageModuleRelation";
	}
	
	@RequestMapping(value="bpm/module/showModuleImport", method = RequestMethod.GET)
	public ModelAndView showModuleImport(HttpServletRequest request,ModelMap model){
		return new ModelAndView("module/showModuleImport",model);
		
	}
	
	
	@RequestMapping(value="bpm/module/moduelCSVImport", method = RequestMethod.POST)
	public String importModuleFile(HttpServletRequest request,@RequestParam("screenName") String screenName) {
		Locale locale = request.getLocale();
		Module module = new Module();
		try{
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		String fileNameWithPath="";
        //Getting file list which user upload
        Map<String, List<MultipartFile>> fileInList = multipartRequest.getMultiFileMap();
        List<MultipartFile> files =fileInList.get("file");
       // the directory to upload to
         String uploadDir = getServletContext().getRealPath("/resources") + "/"+ request.getRemoteUser() + "/";
         // Create the directory if it doesn't exist
         File dirPath = new File(uploadDir);
         if (!dirPath.exists()) {
             dirPath.mkdirs();
         }
         
         if(null != files && files.size() > 0) {
	            for (MultipartFile multipartFile : files) {
	                // retrieve the file data
	                InputStream stream = multipartFile.getInputStream();
	                // write the file to the file specified
	                OutputStream bos = new FileOutputStream(uploadDir + multipartFile.getOriginalFilename());
	                int bytesRead;
	                byte[] buffer = new byte[8192];
	                while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
	                    bos.write(buffer, 0, bytesRead);
	                }
	                bos.close();
	                // close the stream
	                stream.close();
	                fileNameWithPath = dirPath.getAbsolutePath() + Constants.FILE_SEP + multipartFile.getOriginalFilename();
	            }
	        } 
         Module defaultModule = moduleService.isModuleExist("Default Module");
		HSSFWorkbook myWorkBook = CsvUtil.readFile(fileNameWithPath);
		//ModuleInformation
		HSSFSheet moduleSheet = myWorkBook.getSheetAt(0);
		List<Map<Integer,String>> moduleInfo = CsvUtil.getSheetInformation(moduleSheet);
        Map<String,String> moduleInformation = CsvUtil.SheetInformationAsMap(moduleInfo);
        Module newModule = setModuleInfo(moduleInformation);
        
        //USer Information
       /* HSSFSheet userSheet = myWorkBook.getSheetAt(1);
        List<Map<Integer,String>> userInfo = CsvUtil.getSheetInformation(userSheet);
        List<Map<String,String>> userRelationInformation = CsvUtil.SheetInformationAsMapOfList(userInfo);
        if(!userRelationInformation.isEmpty()){
        	setUserRelationInfo(userRelationInformation,newModule);
        }*/
        
              
       //Role Relation information
       /*HSSFSheet roleSheet = myWorkBook.getSheetAt(3);
       List<Map<Integer,String>> roleInfo = CsvUtil.getSheetInformation(roleSheet);
       List<Map<String,String>> roleRelationInformation = CsvUtil.SheetInformationAsMapOfList(roleInfo);
       if(!roleRelationInformation.isEmpty()){
    	   setRoleRelationInfo(roleRelationInformation,newModule);
       }*/
       
       //Department Relation information
       /*HSSFSheet departMentSheet = myWorkBook.getSheetAt(5);
       List<Map<Integer,String>> departmentInfo = CsvUtil.getSheetInformation(departMentSheet);
       List<Map<String,String>> departmnetRelationInformation = CsvUtil.SheetInformationAsMapOfList(departmentInfo);
       if(!departmnetRelationInformation.isEmpty()){
    	   setDepartmentRelationInfo(departmnetRelationInformation,newModule);
       }*/
       
     //Menu Relation information
      /* HSSFSheet menuSheet = myWorkBook.getSheetAt(7);
       List<Map<Integer,String>> menuInfo = CsvUtil.getSheetInformation(menuSheet);
       List<Map<String,String>> menuRelationInfo = CsvUtil.SheetInformationAsMapOfList(menuInfo);
       if(!menuRelationInfo.isEmpty()){
    	   setMenuRelationInfo(menuRelationInfo,newModule);
       }*/
       
     //Form Relation information
       HSSFSheet formSheet = myWorkBook.getSheetAt(1);
       List<Map<Integer,String>> formInfo = CsvUtil.getSheetInformation(formSheet);
       List<Map<String,String>> formRelationInfo = CsvUtil.SheetInformationAsMapOfList(formInfo);
       if(!formRelationInfo.isEmpty()){
    	   setFormRelationInfo(formRelationInfo,newModule,defaultModule);
       }
       
       HSSFSheet tableSheet = myWorkBook.getSheetAt(2);
       List<Map<Integer,String>> tableInfo = CsvUtil.getSheetInformation(tableSheet);
       List<Map<String,String>> tableRelationInfo = CsvUtil.SheetInformationAsMapOfList(tableInfo);
       if(!tableRelationInfo.isEmpty()){
    	   setTableRelationInfo(tableRelationInfo,newModule,defaultModule);
       }
       HSSFSheet listViewSheet = myWorkBook.getSheetAt(3);
       List<Map<Integer,String>> lisViewInfo = CsvUtil.getSheetInformation(listViewSheet);
       List<Map<String,String>> listViewRelationInfo = CsvUtil.SheetInformationAsMapOfList(lisViewInfo);
       if(!listViewRelationInfo.isEmpty()){
    	   setListViewRelationInfo(listViewRelationInfo,newModule,defaultModule);
       }
       moduleService.saveModule(defaultModule);
     /*//Process Relation information
       HSSFSheet processSheet = myWorkBook.getSheetAt(11);
       List<Map<Integer,String>> processInfo = CsvUtil.getSheetInformation(processSheet);
       List<Map<String,String>> processRelationInfo = CsvUtil.SheetInformationAsMapOfList(processInfo);
       if(!processRelationInfo.isEmpty()){
    	   setProcessRelationInfo(processRelationInfo,newModule);
       }*/
       
     //Parent module information
       /*if(!newModule.getIsParent()){
	       HSSFSheet parentModuleSheet = myWorkBook.getSheetAt(13);
	       List<Map<Integer,String>> parentModuleInfo = CsvUtil.getSheetInformation(parentModuleSheet);
	       List<Map<String,String>> parentModuleRelationInformation = CsvUtil.SheetInformationAsMapOfList(parentModuleInfo);
	       if(!parentModuleRelationInformation.isEmpty()){
	    	   setparentRelationInfo(parentModuleRelationInformation,newModule);
	       }
       }*/
       
     //Classification module information
	      /* HSSFSheet classiModuleSheet = myWorkBook.getSheetAt(15);
	       List<Map<Integer,String>> classiModuleInfo = CsvUtil.getSheetInformation(classiModuleSheet);
	       List<Map<String,String>> classiRelationInformation = CsvUtil.SheetInformationAsMapOfList(classiModuleInfo);
	       if(!classiRelationInformation.isEmpty()){
	    	   setClassificationRelationInfo(classiRelationInformation,newModule);
	       }
       */
	   moduleService.saveModule(newModule);
		}catch(Exception e){
			 saveError(request, getText("module.csv.import.error",e.getMessage(),locale));
			 log.error(e.getMessage(), e);
		}
		saveMessage(request, getText("module.csv.import.success", module.getName(), locale));
		//if(screenName.equalsIgnoreCase("managModules")){
			return "redirect:manageModuleRelation";
		/*}else{
			return "redirect:endUserModule";
		}*/
        
	}
	/**
	 * Set classification information to the module
	 * @param parentModInformation
	 * @param module
	 * @return
	 */
	/*private Module setClassificationRelationInfo( List<Map<String,String>> classiInformation,Module module){
		List<String> classiIds = new ArrayList<String>();
		for(Map<String,String> userMap : classiInformation){
			classiIds.add(userMap.get("classification_id"));
		}
		List<Classification> classifications = (List<Classification>) moduleService.getClassificationByIds(classiIds);
		module.setClassification(new HashSet<Classification>(classifications));
		return module;
	}*/
	
	/**
	 * Set MetaForm information to the module if it present in default module
	 * @param formformation
	 * @param module
	 * @return
	 */
	private Module setFormRelationInfo( List<Map<String,String>> formformation,Module module,Module defaultModule){
		List<String> formIds = new ArrayList<String>();
		for(Map<String,String> userMap : formformation){
			formIds.add(userMap.get("form_id"));
		}
		Set<MetaForm> defaultForm = defaultModule.getForms();
		List<MetaForm> forms = formService.getDynamicFormsByIds(formIds);
		Set<MetaForm> addForms = new HashSet<MetaForm>();
		boolean isRemoved = false;
		if(forms != null){
			for(MetaForm form:forms){
				/*if(form.getModule().getClass().equalsIgnoreCase(defaultModule.getId())){
					isRemoved = true;
					addForms.add(form);
				}*/
				if(form.getModules().equals(defaultModule)){
					isRemoved = true;
					addForms.add(form);
				}
			}
		}
		if(isRemoved){
			defaultForm.removeAll(addForms);
			defaultModule.setForms(defaultForm);
			module.setForms(new HashSet<MetaForm>(addForms));
		}
		
		
		return module;
	}
	
	/**
	 * Set Table information to the module if it present in default module
	 * @param formformation
	 * @param module
	 * @return
	 */
	private Module setTableRelationInfo( List<Map<String,String>> tableIformation,Module module,Module defaultModule){
		List<String> tableIds = new ArrayList<String>();
		for(Map<String,String> userMap : tableIformation){
			tableIds.add(userMap.get("table_id"));
		}
		Set<MetaTable> defaultTable = defaultModule.getTables();
		List<MetaTable> tables =  tableService.getTablesByIds(tableIds);
		Set<MetaTable> addTables = new HashSet<MetaTable>();
		boolean isRemoved = false;
		if(tables!=null){
			for(MetaTable table:tables){
				/*if(defaultModule.getId().equalsIgnoreCase(table.getModule().getId())){
					addTables.add(table);
					isRemoved = true;
				}*/
				if(table.getModules().equals(defaultModule)){
					addTables.add(table);
					isRemoved = true;
				}
			}
		}
		if(isRemoved){
			defaultTable.removeAll(addTables);
			defaultModule.setTables(defaultTable);
			module.setTables(new HashSet<MetaTable>(addTables));
		}
		
		return module;
	}
	
	/**
	 * Set Table information to the module if it present in default module
	 * @param formformation
	 * @param module
	 * @return
	 */
	private Module setListViewRelationInfo( List<Map<String,String>> listViewIformation,Module module,Module defaultModule){
		List<String> listViewIds = new ArrayList<String>();
		for(Map<String,String> listViewMap : listViewIformation){
			listViewIds.add(listViewMap.get("listView_id"));
		}
		
		Set<ListView> defaultListView = defaultModule.getListViews();
		List<ListView> listViews = listViewService.getListViewByIds(listViewIds);
		Set<ListView> addListViews = new HashSet<ListView>();
		boolean isRemoved = false;
		if(listViews != null){
			for(ListView listView:listViews){
				/*if(defaultModule.getId().equalsIgnoreCase(listView.getModule().getId())){
					addListViews.add(listView);
					isRemoved = true;
				}*/
				if(defaultModule.getId().equals(listView.getModules())){
					addListViews.add(listView);
					isRemoved = true;
				}
			}
		}
		if(isRemoved){
			defaultListView.removeAll(addListViews);
			defaultModule.setListViews(defaultListView);
			module.setListViews(new HashSet<ListView>(addListViews));
		}
		
		return module;
	}
	/**
	 * Set Process information to the module
	 * @param processInformation
	 * @param module
	 * @return
	 */
	/*private Module setProcessRelationInfo( List<Map<String,String>> processInformation,Module module){
		List<String> processIds = new ArrayList<String>();
		for(Map<String,String> userMap : processInformation){
			processIds.add(userMap.get("process_id"));
		}
		List<Process> process = rtProcessService.getProcessByIds(processIds);
		module.setProcesses(new HashSet<Process>(process));
		return module;
	}*/
	
	/**
	 * Set Role information to the module
	 * @param roleInformation
	 * @param module
	 * @return
	 */
	/*private Module setRoleRelationInfo( List<Map<String,String>> roleInformation,Module module){
		List<String> roleIds = new ArrayList<String>();
		for(Map<String,String> userMap : roleInformation){
			roleIds.add(userMap.get("role_id"));
		}
		List<Role> roles = (List<Role>) roleService.getRolesByIds(roleIds);
		module.setRoles(new HashSet<Role>(roles));
		return module;
	}*/
	
	/**
	 * Set Menu information to the module
	 * @param menuInformation
	 * @param module
	 * @return
	 */
	/*private Module setMenuRelationInfo( List<Map<String,String>> menuInformation,Module module){
		List<String> menuIds = new ArrayList<String>();
		for(Map<String,String> userMap : menuInformation){
			menuIds.add(userMap.get("menu_id"));
		}
		List<Menu> menus = (List<Menu>) menuService.getMenus(menuIds);
		module.setMenus(new HashSet<Menu>(menus));
		return module;
	}*/
	
	/**
	 * Set Department information to the module
	 * @param departmentInformation
	 * @param module
	 * @return
	 */
	/*private Module setDepartmentRelationInfo( List<Map<String,String>> departmentInformation,Module module){
		List<String> departmentIds = new ArrayList<String>();
		for(Map<String,String> userMap : departmentInformation){
			departmentIds.add(userMap.get("department_id"));
		}
		
		List<Department> departments = (List<Department>) departmentService.getDepartmentsByNames(departmentIds);
		module.setDepartments(new HashSet<Department>(departments));
		return module;
	}*/
	
	/**
	 * Set Parent module information to the module
	 * @param parentModInformation
	 * @param module
	 * @return
	 */
	/*private Module setparentRelationInfo( List<Map<String,String>> parentModInformation,Module module){
		List<String> parentModIds = new ArrayList<String>();
		for(Map<String,String> userMap : parentModInformation){
			parentModIds.add(userMap.get("ParentModuduleId"));
		}
		
		List<Module> modules = moduleService.getModulesByIds(parentModIds);
		module.setParentModules(new HashSet<Module>(modules));
		return module;
	}*/
	
	/**
	 * Set User information to the module
	 * @param userInformation
	 * @param module
	 * @return
	 */
	/*private Module setUserRelationInfo( List<Map<String,String>> userInformation,Module module){
		List<String> userNames = new ArrayList<String>();
		for(Map<String,String> userMap : userInformation){
			userNames.add(userMap.get("user_id"));
		}
		List<User> users = userService.getUserByUsernames(userNames);
		module.setAdministrators(new HashSet<User>(users));
		return module;
	}*/
	
	/**
	 * Set Module information to the module
	 * @param menuInformation
	 * @param module
	 * @return
	 */
	private Module setModuleInfo(Map<String,String> information){
		Module module = new Module();
		if(information.containsKey("name")){
			module.setName(information.get("name"));
		}
		if(information.containsKey("description")){
			module.setDescription(information.get("description"));
		}
		if(information.containsKey("module_order")){
			module.setModuleOrder(Integer.parseInt((information.get("module_order"))));
		}
		/*if(information.containsKey("version")){
			module.setVersion(Integer.parseInt((information.get("version"))));
		}*/
		if(information.containsKey("created_by")){
			module.setCreatedBy((information.get("created_by")));
		}
		/*if(information.containsKey("is_parent")){
			module.setIsParent(Boolean.parseBoolean(information.get("is_parent")));
		}*/
		return module;
		
	}
	/**
	 * Get module information to export CSV
	 * @param module
	 * @param wb
	 */
	private void getModuleInfo(Module module,HSSFWorkbook wb){
		List<String> moduelHeader = new ArrayList<String>();
		 List<String> modulesValues = new ArrayList<String>();
		 moduelHeader.add("name");
		 moduelHeader.add("description");
		// moduelHeader.add("module_order");
		 //moduelHeader.add("version");
		 moduelHeader.add("created_by");
		 moduelHeader.add("order");
		// moduelHeader.add("is_parent");
		 modulesValues.add(module.getName());
		 modulesValues.add(module.getDescription());
		 //modulesValues.add(Integer.toString(module.getModuleOrder()));
		// modulesValues.add(Integer.toString(module.getVersion()));
		 modulesValues.add(module.getCreatedBy());
		 modulesValues.add(Integer.toString(module.getModuleOrder()));
		 //modulesValues.add(Boolean.valueOf(module.getIsParent()).toString());
		 HSSFSheet moduleIfo = CsvUtil.createNewSheetWithHeader(wb,"ModuleInfo",moduelHeader);
		 CsvUtil.insertSingleCellValues(moduleIfo, modulesValues);
	}
	
	/**
	 * Get User and user relation information to export CSV
	 * @param module
	 * @param wb
	 */
	/*private void getUserRelationInfo(Module module,HSSFWorkbook wb){
	 List<String> userRelationHeader = new ArrayList<String>();
   	 userRelationHeader.add("module_id");
   	 userRelationHeader.add("user_id");
   	 HSSFSheet Administrator = CsvUtil.createNewSheetWithHeader(wb,"AdministratorRelation",userRelationHeader);
   	 List<Map<String,String>> userMaplist = new ArrayList<Map<String,String>>();
   	 List<String> userHeader = new ArrayList<String>();
   	 List<List<String>> userValuesList = new ArrayList<List<String>>();
   	 userHeader.add("id");
   	 userHeader.add("Name");
   	 userHeader.add("position");
   	 userHeader.add("email");
   	 userHeader.add("username");
   	 HSSFSheet User = CsvUtil.createNewSheetWithHeader(wb,"Administrator",userHeader);
   	 for(User user : module.getAdministrators()){
   		 List<String> userValues = new ArrayList<String>();
	        	Map<String,String> userRelation = new HashMap<String,String>();
	        	userRelation.put("moduleId", module.getId());
	        	userRelation.put("relationId", user.getId());
	        	userMaplist.add(userRelation);
	        	userValues.add(user.getId());
	        	userValues.add(user.getFullName());
	        	userValues.add(user.getPosition());
	        	userValues.add(user.getEmail());
	        	userValues.add(user.getUsername());
	        	userValuesList.add(userValues);
	        	
   	 }
   	CsvUtil.insertMultipleCellValues(User, userValuesList);
   	CsvUtil.insertRelationCellValues(Administrator, userMaplist);
	}*/
	
	/**
	 * Get Department and department relation information to export CSV
	 * @param module
	 * @param wb
	 */
	/*private void getDepartmentRelationInfo(Module module,HSSFWorkbook wb){
	 List<String> departmentRelationHeader = new ArrayList<String>();
	 departmentRelationHeader.add("module_id");
	 departmentRelationHeader.add("department_id");
   	 HSSFSheet departmentSheet = CsvUtil.createNewSheetWithHeader(wb,"DepartmentRelation",departmentRelationHeader);
   	 List<Map<String,String>> departmentMaplist = new ArrayList<Map<String,String>>();
   	 List<String> departmentHeader = new ArrayList<String>();
   	 List<List<String>> departmentValuesList = new ArrayList<List<String>>();
   	 departmentHeader.add("id");
   	 departmentHeader.add("name");
   	 departmentHeader.add("description");
   	 departmentHeader.add("department_type");
   	 departmentHeader.add("super_department");
   	 departmentHeader.add("department_owner");
   	 departmentHeader.add("user_action");
   	 HSSFSheet Department = CsvUtil.createNewSheetWithHeader(wb,"Department",departmentHeader);
   	 for(Department department : module.getDepartments()){
   		 List<String> departmentValues = new ArrayList<String>();
	        	Map<String,String> departmentRelation = new HashMap<String,String>();
	        	departmentRelation.put("moduleId", module.getId());
	        	departmentRelation.put("relationId", department.getId());
	        	departmentMaplist.add(departmentRelation);
	        	departmentValues.add(department.getId());
	        	departmentValues.add(department.getName());
	        	departmentValues.add(department.getDescription());
	        	departmentValues.add(department.getDepartmentType());
	        	departmentValues.add(department.getSuperDepartment());
	        	departmentValues.add(department.getDepartmentOwner());
	        	departmentValuesList.add(departmentValues);
	        	
   	 }
   	CsvUtil.insertMultipleCellValues(Department, departmentValuesList);
   	CsvUtil.insertRelationCellValues(departmentSheet, departmentMaplist);
	}
	*/
	/**
	 * Get Role and role relation information to export CSV
	 * @param module
	 * @param wb
	 */
	/*private void getRoleInfo(Module module,HSSFWorkbook wb){
	 List<String> roleRelationHeader = new ArrayList<String>();
   	 roleRelationHeader.add("module_id");
   	 roleRelationHeader.add("role_id");
   	 HSSFSheet roleRelationSheet = CsvUtil.createNewSheetWithHeader(wb,"RoleRelation",roleRelationHeader);
   	 List<Map<String,String>> roleMaplist = new ArrayList<Map<String,String>>();
   	 List<String> roleHeader = new ArrayList<String>();
   	 List<List<String>> roleValuesList = new ArrayList<List<String>>();
   	 roleHeader.add("id");
   	 roleHeader.add("description");
   	 roleHeader.add("name");
   	 roleHeader.add("role_type");
   	 HSSFSheet roleSheet = CsvUtil.createNewSheetWithHeader(wb,"Role",roleHeader);
   	 for(Role role : module.getRoles()){
	        	Map<String,String> roleRelation = new HashMap<String,String>();
	        	List<String> roleValues = new ArrayList<String>();
	        	roleRelation.put("moduleId", module.getId());
	        	roleRelation.put("relationId", role.getId());
	        	roleMaplist.add(roleRelation);
	        	roleValues.add(role.getId());
	        	roleValues.add(role.getDescription());
	        	roleValues.add(role.getName());
	        	roleValues.add(role.getRoleType());
	        	roleValuesList.add(roleValues);
   	 }
   	CsvUtil.insertMultipleCellValues(roleSheet, roleValuesList);
   	CsvUtil.insertRelationCellValues(roleRelationSheet, roleMaplist);
	}*/
	
	/**
	 * Get Menu and menu relation information to export CSV
	 * @param module
	 * @param wb
	 */
	/*private void getMenuInfo(Module module,HSSFWorkbook wb){
	    List<String> moduleRelationHeader = new ArrayList<String>();
	    moduleRelationHeader.add("module_id");
	    moduleRelationHeader.add("menu_id");
	    HSSFSheet menuRelationSheet = CsvUtil.createNewSheetWithHeader(wb,"MenuRelation",moduleRelationHeader);
	    List<Map<String,String>> menuMaplist = new ArrayList<Map<String,String>>();
	    List<String> menuHeader = new ArrayList<String>();
	    List<List<String>> menuValuesList = new ArrayList<List<String>>();
	   	menuHeader.add("id");
	   	menuHeader.add("name");
	   	menuHeader.add("description");
	   	menuHeader.add("menu_type");
	   	menuHeader.add("menu_url");
	   	menuHeader.add("parent_menu");
	   	menuHeader.add("index_page");
	   	menuHeader.add("menu_order");
	    HSSFSheet menuSheet = CsvUtil.createNewSheetWithHeader(wb,"Menu",menuHeader);
	   	 for(Menu menu : module.getMenus()){
		        	Map<String,String> menuRelation = new HashMap<String,String>();
		        	List<String> menuValues = new ArrayList<String>();
		        	menuRelation.put("moduleId", module.getId());
		        	menuRelation.put("relationId", menu.getId());
		        	menuMaplist.add(menuRelation);
		        	menuValues.add(menu.getId());
		        	menuValues.add(menu.getName());
		        	menuValues.add(menu.getDescription());
		        	menuValues.add(menu.getUrlType());
		        	menuValues.add(menu.getMenuUrl());
		        	menuValues.add(menu.getParentMenu().getName());
		        	menuValues.add(menu.getIndexPage());
		        	menuValues.add(Integer.toString(menu.getMenuOrder()));
		        	menuValuesList.add(menuValues);
	   	 }
	   	CsvUtil.insertMultipleCellValues(menuSheet, menuValuesList);
	   	CsvUtil.insertRelationCellValues(menuRelationSheet, menuMaplist);
	}
	*/
	/**
	 * Get Dynamic Form and Form relation information to export CSV
	 * @param module
	 * @param wb
	 */
	private void getMetaFormInfo(Module module,HSSFWorkbook wb){
		List<String> formRelationHeader = new ArrayList<String>();
		formRelationHeader.add("module_id");
		formRelationHeader.add("form_id");
	   	HSSFSheet formRelationSheet = CsvUtil.createNewSheetWithHeader(wb,"FormRelation",formRelationHeader);
	   	List<Map<String,String>> formMaplist = new ArrayList<Map<String,String>>();
	   //	List<String> formHeader = new ArrayList<String>();
	   	//List<List<String>> formValuesList = new ArrayList<List<String>>();
	   /*	formHeader.add("id");
	   	formHeader.add("module_id");
	   	formHeader.add("table_name");
	   	formHeader.add("created_by");*/
	   //	HSSFSheet formSheet = CsvUtil.createNewSheetWithHeader(wb,"Form",formHeader);
	   	for(MetaForm form : module.getForms()){
		        	Map<String,String> formRelation = new HashMap<String,String>();
		        	//List<String> formValues = new ArrayList<String>();
		        	formRelation.put("moduleId", module.getId());
		        	formRelation.put("relationId", form.getId());
		        	formMaplist.add(formRelation);
		        	/*formValues.add(form.getId());
		        	formValues.add(form.getFormName());
		        	formValues.add(form.getTableName());
		        	formValues.add(form.getCreatedBy());*/
		        	//formValuesList.add(formValues);
	   	 }
	   	//CsvUtil.insertMultipleCellValues(formSheet, formValuesList);
	   	CsvUtil.insertRelationCellValues(formRelationSheet, formMaplist);
	}
	
	/**
	 * Get Table relation information to export CSV
	 * @param module
	 * @param wb
	 */
	private void getMetaTableInfo(Module module,HSSFWorkbook wb){
		List<String> tableRelationHeader = new ArrayList<String>();
		tableRelationHeader.add("module_id");
		tableRelationHeader.add("table_id");
	   	HSSFSheet tableRelationSheet = CsvUtil.createNewSheetWithHeader(wb,"TableRelation",tableRelationHeader);
	   	List<Map<String,String>> tableMaplist = new ArrayList<Map<String,String>>();
	  
	   	for(MetaTable table : module.getTables()){
		        	Map<String,String> tableRelation = new HashMap<String,String>();
		        	tableRelation.put("moduleId", module.getId());
		        	tableRelation.put("relationId", table.getId());
		        	tableMaplist.add(tableRelation);
	   	 }
	   	CsvUtil.insertRelationCellValues(tableRelationSheet, tableMaplist);
	}
	/**
	 * Get ListView and Form relation information to export CSV
	 * @param module
	 * @param wb
	 */
	private void getListViewInfo(Module module,HSSFWorkbook wb){
		List<String> listViewRelationHeader = new ArrayList<String>();
		listViewRelationHeader.add("module_id");
		listViewRelationHeader.add("listView_id");
	   	HSSFSheet listViewRelationSheet = CsvUtil.createNewSheetWithHeader(wb,"ListViewRelation",listViewRelationHeader);
	   	List<Map<String,String>> listViewMaplist = new ArrayList<Map<String,String>>();
	  
	   	for(ListView listView : module.getListViews()){
		        	Map<String,String> lisViewRelation = new HashMap<String,String>();
		        	lisViewRelation.put("moduleId", module.getId());
		        	lisViewRelation.put("relationId", listView.getId());
		        	listViewMaplist.add(lisViewRelation);
	   	 }
	   	CsvUtil.insertRelationCellValues(listViewRelationSheet, listViewMaplist);
	}
	/**
	 * Get Classification and classification relation information to export CSV
	 * @param module
	 * @param wb
	 */
	/*private void getClassificationInfo(Module module,HSSFWorkbook wb){
		List<String> classificationRelationHeader = new ArrayList<String>();
		classificationRelationHeader.add("module_id");
		classificationRelationHeader.add("classification_id");
	   	HSSFSheet classificationRelationSheet = CsvUtil.createNewSheetWithHeader(wb,"ClassificationRelation",classificationRelationHeader);
	   	List<Map<String,String>> classificationMaplist = new ArrayList<Map<String,String>>();
	   	List<String> classificationHeader = new ArrayList<String>();
	   	List<List<String>> classificationValuesList = new ArrayList<List<String>>();
	   	classificationHeader.add("id");
	   	classificationHeader.add("name");
	   	classificationHeader.add("description");
	   	HSSFSheet classificationSheet = CsvUtil.createNewSheetWithHeader(wb,"Classification",classificationHeader);
	   	for(Classification classi : module.getClassification()){
		        	Map<String,String> classificationRelation = new HashMap<String,String>();
		        	List<String> formValues = new ArrayList<String>();
		        	classificationRelation.put("moduleId", module.getId());
		        	classificationRelation.put("relationId", classi.getId());
		        	classificationMaplist.add(classificationRelation);
		        	formValues.add(classi.getId());
		        	formValues.add(classi.getName());
		        	formValues.add(classi.getDescription());
		        	classificationValuesList.add(formValues);
	   	 }
	   	CsvUtil.insertMultipleCellValues(classificationSheet, classificationValuesList);
	   	CsvUtil.insertRelationCellValues(classificationRelationSheet, classificationMaplist);
	}*/
	/**
	 * Get Process and process relation information to export CSV
	 * @param module
	 * @param wb
	 */
	/*private void getProcessInfo(Module module,HSSFWorkbook wb){
		List<String> processRelationHeader = new ArrayList<String>();
	 	processRelationHeader.add("module_id");
	 	processRelationHeader.add("process_id");
	 	HSSFSheet processRelationSheet = CsvUtil.createNewSheetWithHeader(wb,"ProcessRelation",processRelationHeader);
	 	List<Map<String,String>> processMaplist = new ArrayList<Map<String,String>>();
	  	List<String> processHeader = new ArrayList<String>();
	    List<List<String>> processValuesList = new ArrayList<List<String>>();
	   	processHeader.add("id");
		processHeader.add("name");
	   	processHeader.add("description");
	   	processHeader.add("category");	   
	   	processHeader.add("key");
	   	processHeader.add("deploymentId");
	   	processHeader.add("resourceName");
	   	processHeader.add("diagramResourceName");
	   	processHeader.add("classificationId");
	   	HSSFSheet processSheet = CsvUtil.createNewSheetWithHeader(wb,"Process",processHeader);
	   	 for(Process process : module.getProcesses()){
		        	Map<String,String> processRelation = new HashMap<String,String>();
		        	List<String> processValues = new ArrayList<String>();
		        	processRelation.put("moduleId", module.getId());
		        	processRelation.put("relationId", process.getId());
		        	processMaplist.add(processRelation);
		        	processValues.add(process.getId());
		        	processValues.add(process.getName());
		        	processValues.add(process.getDescription());
		        	processValues.add(process.getCategory());
		        	processValues.add(process.getKey());
		        	processValues.add(process.getDeploymentId());
		        	processValues.add(process.getResourceName());
		        	processValues.add(process.getDiagramResourceName());
		        	processValues.add(process.getClassificationId());
		        	processValuesList.add(processValues);
	   	 }
	   	CsvUtil.insertMultipleCellValues(processSheet, processValuesList);
	   	CsvUtil.insertRelationCellValues(processRelationSheet, processMaplist);
	}
	*/
	/**
	 * Get parent module and parentmodule relation information to export CSV
	 * @param module
	 * @param wb
	 */
	/*private void getParentModuleInfo(Module module,HSSFWorkbook wb){
		 List<String> parentModuleRelationHeader = new ArrayList<String>();
		 parentModuleRelationHeader.add("module_id");
		 parentModuleRelationHeader.add("parent_module_id");
	   	 HSSFSheet parentModuleRelationSheet = CsvUtil.createNewSheetWithHeader(wb,"ParentModuleRelation",parentModuleRelationHeader);
	   	 List<Map<String,String>> parentModuleMaplist = new ArrayList<Map<String,String>>();
	   	 List<String> parentModuleHeader = new ArrayList<String>();
	   	 List<List<String>> parentModuleValuesList = new ArrayList<List<String>>();
	   	parentModuleHeader.add("name");
	   	parentModuleHeader.add("description");
	   	parentModuleHeader.add("module_order");
	   	parentModuleHeader.add("version");
	   	parentModuleHeader.add("created_by");
	   	 HSSFSheet parentModuleSheet = CsvUtil.createNewSheetWithHeader(wb,"ParentModule",parentModuleHeader);
	   	 for(Module parentModule : module.getParentModules()){
		        	Map<String,String> parentModuleRelation = new HashMap<String,String>();
		        	List<String> parentModuleValues = new ArrayList<String>();
		        	parentModuleRelation.put("moduleId", module.getId());
		        	parentModuleRelation.put("relationId", parentModule.getId());
		        	parentModuleMaplist.add(parentModuleRelation);
		        	parentModuleValues.add(module.getName());
		        	parentModuleValues.add(module.getDescription());
		        	parentModuleValues.add(Integer.toString(module.getModuleOrder()));
		        	parentModuleValues.add(Integer.toString(module.getVersion()));
		        	parentModuleValues.add(module.getCreatedBy());
		        	parentModuleValuesList.add(parentModuleValues);
	   	 }
	   	CsvUtil.insertMultipleCellValues(parentModuleSheet, parentModuleValuesList);
	   	CsvUtil.insertRelationCellValues(parentModuleRelationSheet, parentModuleMaplist);
		}*/
	
	/**
	 * Used to set the Group,User,Classification,Role,Menu set object to the module 
	 * @param module
	 * @return
	 */
	private Module setModuleValuse(Module module,HttpServletRequest request) {
		//List<String> userNames = new ArrayList<String>();
		List<String> groupNames = new ArrayList<String>();
		List<String> departmentNames = new ArrayList<String>();
		//List<String> classificationNames = new ArrayList<String>();
		List<String> roleIds = new ArrayList<String>();
		Set<ModuleRolePrivilege> moduleRolePrivileges=new HashSet<ModuleRolePrivilege>();
		//List<String> menuIds = new ArrayList<String>();
		/*if (!module.getAdministrators().isEmpty()) {
			for (User user : module.getAdministrators()) {
				userNames.add(user.getUsername());
			}
			List<User> users = (List<User>) userService
					.getUserByUsernames(userNames);
			module.setAdministrators(new HashSet<User>(users));
		}*/
		
		if (!module.getGroups().isEmpty()) {
			for (Group group : module.getGroups()) {
				ModuleRolePrivilege modelPrivilege=new ModuleRolePrivilege(module,"ROLE_"+group.getName()+"_GROUP","Group",group.getName(),"edit");
				moduleRolePrivileges.add(modelPrivilege);
				/*groupNames.add(group.getName());
				group.getGroupRole();*/
			}
			/*List<Group> groups =(List<Group>) groupService.getGroupsByIds(groupNames);
			module.setGroups(new HashSet<Group>(groups));*/
		}
		if (!module.getDepartments().isEmpty()) {
			for (Department department : module.getDepartments()) {
				ModuleRolePrivilege modelPrivilege=new ModuleRolePrivilege(module,"ROLE_"+department.getName()+"_DEPARTMENT","Department",department.getName(),"edit");
				moduleRolePrivileges.add(modelPrivilege);
				//departmentNames.add(department.getName());
			}
			/*List<Department> departments = (List<Department>) departmentService.getDepartmentsByNames(departmentNames);
			module.setDepartments(new HashSet<Department>(departments));*/
		}
		/*if (!module.getClassification().isEmpty()) {
			List<String> existClassification = new ArrayList<String>();
			//Get Already Exist Classification
			List<LabelValue> classificationList = (List<LabelValue>) request.getSession().getServletContext().getAttribute(Constants.PROCESS_CLASSIFICATION);
			for(LabelValue classi : classificationList){
				existClassification.add(classi.getValue());
			}
			//Save new Classification
			for (Classification classification : module.getClassification()) {
				classificationNames.add(classification.getName());
				if(!classification.getName().isEmpty() && !existClassification.contains(classification.getName())){
					Classification classi = new Classification();
					classi.setName(classification.getName());
					rtProcessService.saveClassification(classi, request);
				}
			}
			List<Classification> classifications = (List<Classification>) moduleService.getClassificationByIds(classificationNames);
			module.setClassification(new HashSet<Classification>(classifications));
		}*/
		if (!module.getRoles().isEmpty()) {
			for (Role role : module.getRoles()) {
				ModuleRolePrivilege modelPrivilege=new ModuleRolePrivilege(module,role.getName(),"Role",role.getName(),"edit");
				moduleRolePrivileges.add(modelPrivilege);
				//roleIds.add(role.getName());
			}
			/*List<Role> roles = (List<Role>) roleService.getRolesByIds(roleIds);
			module.setRoles(new HashSet<Role>(roles));*/
		}
		/*if (!module.getMenus().isEmpty()) {
			for (Menu menu : module.getMenus()) {
				menuIds.add(menu.getName());
			}
			List<Menu> menus = (List<Menu>) menuService.getMenus(menuIds);
			module.setMenus(new HashSet<Menu>(menus));
		}*/
		
		if (!module.getViewGroups().isEmpty()) {
			for (Group group : module.getViewGroups()) {
				ModuleRolePrivilege modelPrivilege=new ModuleRolePrivilege(module,"ROLE_"+group.getName()+"_GROUP","Group",group.getName(),"view");
				moduleRolePrivileges.add(modelPrivilege);
			}
		}

		if (!module.getViewDepartments().isEmpty()) {
			for (Department department : module.getViewDepartments()) {
				ModuleRolePrivilege modelPrivilege=new ModuleRolePrivilege(module,"ROLE_"+department.getName()+"_DEPARTMENT","Department",department.getName(),"view");
				moduleRolePrivileges.add(modelPrivilege);
			}
		}

		if (!module.getViewRoles().isEmpty()) {
			for (Role role : module.getViewRoles()) {
				ModuleRolePrivilege modelPrivilege=new ModuleRolePrivilege(module,role.getName(),"Role",role.getName(),"view");
				moduleRolePrivileges.add(modelPrivilege);
			}
		}
		
		if (!module.getAdministrators().isEmpty()) {
			for (User user : module.getAdministrators()) {
				ModuleRolePrivilege modelPrivilege=new ModuleRolePrivilege(module,"ROLE_"+user.getUsername()+"_USER","User",user.getUsername(),"edit");
				moduleRolePrivileges.add(modelPrivilege);
			}
		}
		
		if (!module.getViewAdministrators().isEmpty()) {
			for (User user : module.getViewAdministrators()) {
				ModuleRolePrivilege modelPrivilege=new ModuleRolePrivilege(module,"ROLE_"+user.getUsername()+"_USER","User",user.getUsername(),"view");
				moduleRolePrivileges.add(modelPrivilege);
			}
		}
		
		module.setModleRoles(moduleService.saveModuleRolePrivilege(moduleRolePrivileges,module.getId()));
		return module;
	}
	
	@Autowired
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}
	/*@Autowired
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }*/
	
	@Autowired
    public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	 @Autowired
	public void setFormService(FormDefinitionService formService) {
	        this.formService = formService;
	 }
	 
	 @Autowired
	 public void setTableService(TableService tableService) {
	             this.tableService = tableService;
	    }
	 @Autowired
	    public void setListViewService(ListViewService listViewService) {
	        this.listViewService = listViewService;
	    }
	 
	 @Autowired
	    public void setGroupService(GroupService groupService) {
	        this.groupService = groupService;
	    }
}