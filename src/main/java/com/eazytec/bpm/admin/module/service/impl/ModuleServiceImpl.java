package com.eazytec.bpm.admin.module.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.admin.module.dao.ModuleDao;
import com.eazytec.bpm.admin.module.service.ModuleService;
import com.eazytec.bpm.runtime.listView.service.ListViewService;
import com.eazytec.bpm.runtime.table.service.TableService;
import com.eazytec.common.util.CommonUtil;
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
import com.eazytec.service.impl.GenericManagerImpl;
import com.eazytec.util.DateUtil;

/**
 * Implementation of ModuleService interface
 * @author revathi
 *
 */
@Service("moduleService")
public class ModuleServiceImpl extends GenericManagerImpl<Module, String> implements ModuleService{
	
	private ModuleDao moduleDao;
	private TableService tableService;
	private ListViewService listViewService;
	
	/**
	  * {@inheritDoc}
	  */
	public Module saveModule(Module module) throws EazyBpmException{
			if(module.getId()==null){
				module.setCreatedTime(new Date());
			}
			
			module.setUpdatedTime(new Date());
			return moduleDao.saveModule(module);
		
	}
	
	/**
	  * {@inheritDoc}
	  */
	public List<LabelValue> getAllModules(boolean isPrivilegeNeeded) throws EazyBpmException{
		boolean isAdmin=false;
		
		List<Module> modules = moduleDao.getAllModules();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Set<Role> roles=user.getRoles();
		for(Role role:roles){
			if(role.getId().equals("ROLE_ADMIN")){
				isAdmin=true;
				break;
			}
		}
		
		List<LabelValue> list = new ArrayList<LabelValue>();
		
		if(!isAdmin && isPrivilegeNeeded){
			List<Module> validModules=getUserModules(user);
			 for (Module module : validModules) {
				 if(module.isEdit()){
					 list.add(new LabelValue(module.getName(), module.getId())); 
				 }
		    }
		}else{
			 for (Module module : modules) {
		     		list.add(new LabelValue(module.getName(), module.getId()));
		    }
		}
	
		return list;
	}
	
	/**
	  * {@inheritDoc}
	  */
	/*public List<LabelValue> getAllParentModule() throws EazyBpmException{
		List<Module> modules = moduleDao.getAllParentModule();
		List<LabelValue> list = new ArrayList<LabelValue>();
		for (Module module : modules) {
			list.add(new LabelValue(module.getName(), module.getId()));
		}
		return list;

	}*/
	
	/**
	  * {@inheritDoc}
	  */
	public List<Module> getModulesByIds(List<String> ids)throws EazyBpmException{
		return moduleDao.getModulesByIds(ids);
	}
	
	/**
	  * {@inheritDoc}
	  *//*
	public List<Module> getAllParentModuleList()throws EazyBpmException{
		return moduleDao.getAllParentModule();
	}*/
	
	/**
	  * {@inheritDoc}
	  */
	/*public List<Module> getAllChildModuleList()throws EazyBpmException{
		return moduleDao.getAllChildModule();
	}*/
	
	/**
	  * {@inheritDoc}
	  */
	public List<Classification> getAllClassificationList() throws EazyBpmException{
		return moduleDao.getAllClassification();
	}

	/**
	  * {@inheritDoc}
	  */
	public List<Module> getAllModuleList() throws EazyBpmException{
		List<Module> allModules=moduleDao.getAllModules();
		boolean isAdmin=false;
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Set<Role> roles=user.getRoles();

		for(Role role:roles){
			if(role.getId().equals("ROLE_ADMIN")){
				isAdmin=true;
				break;
			}
		}
		
		if(!isAdmin){
			return	getUserModules(user);
		}else{
			for(Module module:allModules){
				module.setEdit(true);
			}
			return allModules;
		}
	}
	
	private List<Module> getUserModules(User user){
		Set<Group>  groups=user.getGroups();
		Department departments=user.getDepartment();
		Set<Role> roles=user.getRoles();
		
		StringBuffer role_types = new StringBuffer();
		role_types.append("(");
		
		role_types.append("'"+user.getUsername()+"',");
		
		if(departments.getDepartmentRole()!=null){
			role_types.append("'"+departments.getDepartmentRole()+"',");
		}
		
		for(Group group:groups){
			if(group.getGroupRole()!=null){
				role_types.append("'"+group.getGroupRole()+"',");
			}
		}
		
		for(Role role:roles){
			if(role.getName()!=null){
				role_types.append("'"+role.getName()+"',");
			}
		}
		
		if(role_types.lastIndexOf(",")>0){
			role_types.deleteCharAt(role_types.lastIndexOf(","));
		}
		role_types.append(")");
			
		List<Module> allEditUserModules=moduleDao.getAllUserModules(role_types.toString(),"edit");
		
		List<Module> allViewUserModules=moduleDao.getAllUserModules(role_types.toString(),"view");
		for(Module module:allViewUserModules){
			module.setEdit(false);
		}
		for(Module module:allEditUserModules){
			/*if(module.getIsPublic()){
				module.setEdit(false);		
			}else{*/
				module.setEdit(true);
			/*}*/
		}
		Set<Module> temp=new HashSet<Module>();
		temp.addAll(allViewUserModules);
		temp.addAll(allEditUserModules);
		
		List<Module> allModules=new ArrayList<Module>();
		allModules.addAll(temp);
	
		//Shoring the modules by order
		Collections.sort(allModules, new Comparator<Module>() {
		        @Override
		        public int compare(final Module object1, final Module object2) {
		            return object1.getModuleOrder() > object2.getModuleOrder() ? 1 : (object1.getModuleOrder() < object2.getModuleOrder() ? -1 : 0);
		    	}
		       } );
		
		return allModules;
	}
	
	public List<Module> getAllModulesForUser(User user) throws EazyBpmException{
		Set<Role>roles = user.getRoles();
		return moduleDao.getAllModules();
	}
	
	/*public List<Module> getAllModulesForRoles(Set<Role>roles) throws EazyBpmException{		
		return moduleDao.getAllModulesForRoles(roles);
	}*/

	/**
	  * {@inheritDoc}
	  */
	
	public Module getModule(String moduleId) throws EazyBpmException{
		return moduleDao.get(moduleId);
	}
	
	
	/**
	  * {@inheritDoc}
	  */
	
	public Module getModuleByName(String moduleName) throws EazyBpmException{
		List<Module> moduleList= moduleDao.getModulesByName(moduleName);
		
		if(moduleList != null && !moduleList.isEmpty() ){
			return moduleList.get(0);
		}else{
			return null;
		}
	}
	
	public Map<String,Object> getModuleDetails(String moduleId) throws EazyBpmException{
		//String administrators = "";
		StringBuffer users = new StringBuffer();
		StringBuffer groups = new StringBuffer();
		StringBuffer departments = new StringBuffer();
		StringBuffer roles =new StringBuffer();
		StringBuffer viewUsers = new StringBuffer();
		StringBuffer viewGroups = new StringBuffer();
		StringBuffer viewDepartments = new StringBuffer();
		StringBuffer viewRoles =new StringBuffer();
		Map<String,Object> moduleDetails=new HashMap<String, Object>();
		Module module=moduleDao.get(moduleId);
		/*if(module.getAdministrators() != null){
    		for(User administrator : module.getAdministrators()){
    			if(administrators.length() == 0 ) {
    				administrators+=administrator.getUsername();
    		    } else {
    		    	administrators+=","+administrator.getUsername();
    		    }
    		}
    	}*/
		
		/*if(module.getGroups() != null){
    		for(Group group : module.getGroups()){
    			if(groups.length() == 0 ) {
    				groups+=group.getName();
    		    } else {
    		    	groups+=","+group.getName();
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
    	}*/
		Set<ModuleRolePrivilege> modulePrivileges=module.getModleRoles();
		for(ModuleRolePrivilege modulePrivilege:modulePrivileges){
			if(modulePrivilege.getPrivilegeType().equals("edit")){
				if(modulePrivilege.getRoleType().equals("Department")){
					departments.append(modulePrivilege.getPrivilegeName()+",");
				}else if(modulePrivilege.getRoleType().equals("Role")){
					roles.append(modulePrivilege.getPrivilegeName()+",");
				}else if(modulePrivilege.getRoleType().equals("Group")){
					groups.append(modulePrivilege.getPrivilegeName()+",");
				}else if(modulePrivilege.getRoleType().equals("User")){
					users.append(modulePrivilege.getPrivilegeName()+",");
				}
			}else{
				if(modulePrivilege.getRoleType().equals("Department")){
					viewDepartments.append(modulePrivilege.getPrivilegeName()+",");
				}else if(modulePrivilege.getRoleType().equals("Role")){
					viewRoles.append(modulePrivilege.getPrivilegeName()+",");
				}else if(modulePrivilege.getRoleType().equals("Group")){
					viewGroups.append(modulePrivilege.getPrivilegeName()+",");
				}else if(modulePrivilege.getRoleType().equals("User")){
					viewUsers.append(modulePrivilege.getPrivilegeName()+",");
				}
			}
		}
		
		departments=removeLastComma(departments);
		roles=removeLastComma(roles);
		groups=removeLastComma(groups);
		users=removeLastComma(users);
		
		viewDepartments=removeLastComma(viewDepartments);
		viewRoles=removeLastComma(viewRoles);
		viewGroups=removeLastComma(viewGroups);
		viewUsers=removeLastComma(viewUsers);
	
		//moduleDetails.put("administrators", administrators);
		moduleDetails.put("users", users);
		moduleDetails.put("roles", roles);
		moduleDetails.put("groups", groups);
		moduleDetails.put("departments", departments);
		
		moduleDetails.put("viewUsers", viewUsers);
		moduleDetails.put("viewRoles", viewRoles);
		moduleDetails.put("viewGroups", viewGroups);
		moduleDetails.put("viewDepartments", viewDepartments);
		moduleDetails.put("module", module);
		
		return moduleDetails;
	}
	
	private StringBuffer removeLastComma(StringBuffer stringBuffer){
		if(stringBuffer.lastIndexOf(",")>0){
			stringBuffer.deleteCharAt(stringBuffer.lastIndexOf(","));
		}
		return stringBuffer;
	}

	/**
	  * {@inheritDoc}
	  */
	public List<Classification> getClassificationByIds(List<String> ids) throws EazyBpmException{
		return moduleDao.getClassificationByIds(ids);
	}
	
	public String removeModule(String moduleId)throws EazyBpmException{
		Module moduel = getModule(moduleId);
		//Module defaultModule = isModuleExist("Default Module");
		Set<MetaForm> forms = moduel.getForms();
		Set<MetaTable> tables = moduel.getTables();
		Set<ListView> listViews = moduel.getListViews();
		
		if(forms.isEmpty() && tables.isEmpty() && listViews.isEmpty() ){
			moduleDao.removeModuleRolePrivilege(moduleId);
			moduleDao.remove(moduel);
			return "true";
		}else{
			return "Module "+moduel.getName()+" contains data, Delete it first";
		}
		/*Set<MetaForm> emptyForms = new HashSet<MetaForm>();
		Set<MetaTable> emptyTables = new HashSet<MetaTable>();
		Set<ListView> emptyListView = new HashSet<ListView>();
		moduel.setForms(emptyForms);
		moduel.setTables(emptyTables);
		moduel.setListViews(emptyListView);
		
		//related forms are moved to default module
		Set<MetaForm> defaultForms = defaultModule.getForms();
		defaultForms.addAll(forms);
		defaultModule.setForms(defaultForms);
		//related tables are moved to default module
		Set<MetaTable> defaultTables = defaultModule.getTables();
		defaultTables.addAll(tables);
		defaultModule.setTables(defaultTables);
		//related listViews are moved to default module
		Set<ListView> defaultListViews = defaultModule.getListViews();
		defaultListViews.addAll(listViews);
		defaultModule.setListViews(defaultListViews);
		saveModule(defaultModule);*/
		
	}
	public Module isModuleExist(String name)throws EazyBpmException{
		List<Module> moduleList = moduleDao.getModulesByName(name);
		if(moduleList != null && !moduleList.isEmpty() ){
			return moduleList.get(0);
		}else{
			return null;
		}
	}
	/**
	  * {@inheritDoc}
	  */
	/*public List<LabelValue> getParentModuleAsLabelValue() throws BpmException{
		return moduleDao.getParentModuleAsLabelValue();
	}*/
	/**
	  * {@inheritDoc}
	  */
	public LabelValue getModuleAsLabelValueForId(String moduleId) throws BpmException{
		return moduleDao.getModuleAsLabelValueForId(moduleId);
	}
	
	/**
	  * {@inheritDoc}
	  */
	public boolean changeModuleOrder(int currentNodeOrder,int replaceNodeOrder)throws BpmException{
		//List<Module> allModules=moduleDao.getAllModules();
		//int index=0;
		//currentNodeObj.setModuleOrder(replaceNodeObj.getModuleOrder());
		boolean isUp=false;
		List<Module> betweenModules=null;
		if(currentNodeOrder<=replaceNodeOrder){
			betweenModules=moduleDao.getBetweenModuleList(currentNodeOrder,replaceNodeOrder);
			isUp=true;
		}else{
			betweenModules=moduleDao.getBetweenModuleList(replaceNodeOrder,currentNodeOrder);
		}
		
		int previousModuleOrder=-1;
		if(isUp){
			for(int index=betweenModules.size()-1;index>=0;index--){
				if(previousModuleOrder==-1){
					previousModuleOrder=betweenModules.get(index).getModuleOrder();
				}else{
					int currentModuleOrder=betweenModules.get(index).getModuleOrder();
					betweenModules.get(index).setModuleOrder(previousModuleOrder);
					int i=index+1;
					betweenModules.get(i).setModuleOrder(currentModuleOrder);
				}
			}
		}else{
			for(int index=0;index<=betweenModules.size()-1;index++){
				if(previousModuleOrder==-1){
					previousModuleOrder=betweenModules.get(index).getModuleOrder();
				}else{
					int currentModuleOrder=betweenModules.get(index).getModuleOrder();
					betweenModules.get(index).setModuleOrder(previousModuleOrder);
					int i=index-1;
					betweenModules.get(i).setModuleOrder(currentModuleOrder);
				}
			}
		}
			
	/*int currentModuleOrder=0;
		int swapModuleOrder=0;
		int index=0;
		boolean isOrderChanged=false;
		List<Module> allModules=moduleDao.getAllModules();
		
		Module currentModule=null;
		Module swapModule=null;
		
		for(Module moduleObj:allModules){
			if(moduleObj.getId().equals(moduleId)){
				currentModule=moduleObj;
				break;
			}
			index++;
		}
		
		swapModule=getPreviousOrNextModule(index,allModules,orderingType);
		
		if(swapModule!=null){
			currentModuleOrder=currentModule.getModuleOrder();
			swapModuleOrder=swapModule.getModuleOrder();
			
			currentModule.setModuleOrder(swapModuleOrder);
			swapModule.setModuleOrder(currentModuleOrder);
			
			moduleDao.saveModule(currentModule);
			moduleDao.saveModule(swapModule);
			isOrderChanged=true;
		}
		return isOrderChanged;*/
		return true;
	}
	
	/**
	 * To get the previous or next module by its current index
	 * @param index
	 * @param allModules
	 * @param orderingType
	 * @return
	 */
	private  Module getPreviousOrNextModule(int index,List<Module> allModules,String orderingType){
		Module swapModule=null;
		if(orderingType.equals("up")){
			if((index-1)>=0){
				swapModule=allModules.get(index-1);
			}else{
				swapModule=null;
			}
		}else{
			if((index+1)<=allModules.size()){
				swapModule=allModules.get(index+1);
			}else{
				swapModule=null;
			}
		}
		return swapModule;
	}
	
	/**
	  * {@inheritDoc}
	  */
	 public List<Module> getModulesByUserPrivilege() throws BpmException{
		 User user = CommonUtil.getLoggedInUser();
			Set<Group>  groups=user.getGroups();
			Department departments=user.getDepartment();
			Set<Role> roles=user.getRoles();
			
			StringBuffer role_types = new StringBuffer();
			role_types.append("(");
			
			role_types.append("'"+user.getUsername()+"',");
			
			if(departments.getDepartmentRole()!=null){
				role_types.append("'"+departments.getDepartmentRole()+"',");
			}
			
			for(Group group:groups){
				if(group.getGroupRole()!=null){
					role_types.append("'"+group.getGroupRole()+"',");
				}
			}
			
			for(Role role:roles){
				if(role.getName()!=null){
					role_types.append("'"+role.getName()+"',");
				}
			}
			
			if(role_types.lastIndexOf(",")>0){
				role_types.deleteCharAt(role_types.lastIndexOf(","));
			}
			role_types.append(")");
				
			return moduleDao.getAllUserModules(role_types.toString(),"edit");
	 }
	
	/**
	  * {@inheritDoc}
	  */
	 public Set<ModuleRolePrivilege> saveModuleRolePrivilege(Set<ModuleRolePrivilege> moduleRolePrivileges,String moduleId)throws EazyBpmException{
		 moduleDao.removeModuleRolePrivilege(moduleId);
		 Set<ModuleRolePrivilege> savedModulePrivileges=new HashSet<ModuleRolePrivilege>();
		 for(ModuleRolePrivilege modulePrivileges:moduleRolePrivileges){
			 ModuleRolePrivilege moduleRolePrivilege =moduleDao.saveModuleRolePrivilege(modulePrivileges);
			 savedModulePrivileges.add(moduleRolePrivilege);
		 }
		 return savedModulePrivileges;
	 }
	 
	 /**
	  * {@inheritDoc}
	  */
	 public void getModuleDump(OutputStream dumpOutPut ,String moduleId,List<String> dataBaseInfo )throws EazyBpmException{
		 
		 Module module = moduleDao.get(moduleId);
		 Set<MetaTable> metaTables=module.getTables();
		 Set<MetaForm> metaForms =module.getForms();
		 Set<ListView> listViews =module.getListViews();
		 
		 //set the table , list view and form names to set on the dump 
		 setNamesToCheck(dumpOutPut,metaTables,metaForms,listViews);
		 
		 //construct module dump
		 constructSqlDump(constructModuleDumps(module),dumpOutPut);
		 
		 //construct table dump and module relation
		 if(!metaTables.isEmpty()){
			 tableService.constructTableDumps(metaTables,dumpOutPut,dataBaseInfo,true);
			 constructSqlDump(constructModuleTableRelation(module.getId(),metaTables),dumpOutPut);
		 }
		 
		 //construct meta form and module relation dump
		 if(!metaForms.isEmpty()){
			 constructSqlDump(constructFormDump(metaForms),dumpOutPut);
			 constructSqlDump(constructModuleFromRelation(module.getId(),metaForms),dumpOutPut); 
		 }
		 
		//construct meta list view and module relation dump
		 if(!listViews.isEmpty()){
			 constructSqlDump(listViewService.constructListViewDump(listViews,module.getId()),dumpOutPut);
			// constructSqlDump(constructModuleFromRelation(module.getId(),metaForms),dumpOutPut);
		 }
	
	 }
	 
	 private void setNamesToCheck(OutputStream dumpOutPut,Set<MetaTable> metaTables, Set<MetaForm> metaForms,Set<ListView> listViews){
		 StringBuffer checkTableName = new StringBuffer();
		 StringBuffer checkFormName = new StringBuffer();
		 StringBuffer checkListViewName = new StringBuffer();
         checkTableName.append("-- (");
         if(!metaTables.isEmpty()){
        	 for(MetaTable metaTableObj:metaTables){
            	 checkTableName.append("'"+metaTableObj.getTableName()+"',");
             }
         }else{
        	 checkTableName.append("''");
         }
         
         if(checkTableName.lastIndexOf(",")>0){
        	 checkTableName.deleteCharAt(checkTableName.lastIndexOf(","));
		 }
         checkTableName.append(");\n");
        
         checkFormName.append("-- (");
         if(!metaForms.isEmpty()){
        	 for(MetaForm metaObj:metaForms){
        		 if(metaObj.isActive()){
        			 checkFormName.append("'"+metaObj.getFormName()+"',");
        		 }
        	 }
         }else{
        	 checkFormName.append("''");
         }
         
         if(checkFormName.lastIndexOf(",")>0){
        	 checkFormName.deleteCharAt(checkFormName.lastIndexOf(","));
		 }
         checkFormName.append(");\n");
        
         checkListViewName.append("-- (");
         if(!listViews.isEmpty()){
        	 for(ListView listViewObj:listViews){
        		 if(listViewObj.isActive()){
        			 checkListViewName.append("'"+listViewObj.getViewName()+"',");
        		 }
        	 }
         }else{
        	 checkListViewName.append("''");
         }
         
         if(checkListViewName.lastIndexOf(",")>0){
        	 checkListViewName.deleteCharAt(checkListViewName.lastIndexOf(","));
		 }
         checkListViewName.append(");\n");
       
         constructSqlDump(checkTableName.toString(),dumpOutPut);
         constructSqlDump(checkFormName.toString(),dumpOutPut);
         constructSqlDump(checkListViewName.toString(),dumpOutPut);
         User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         constructSqlDump("-- "+user.getId()+";\n",dumpOutPut);
	 }
	 
	 /**
	  * {@inheritDoc}
	  */
	 public Map<String,Object> isValidFormTable(String moduleId){
		 Module module = moduleDao.get(moduleId);
		 Set<MetaTable> metaTables=module.getTables();
		 Set<MetaForm> metaForms =module.getForms();
		 Map<String,Object> validForm=new HashMap<String, Object>();
		 boolean status=false;
		 String message="";
		 if(metaForms.isEmpty()){
			 validForm.put("isValid", true);
			 return validForm;
		 }
		 if(metaTables.isEmpty()){
			 validForm.put("isValid", false);
			 validForm.put("moduleMessage", "No table is mapped in the module");
			 return validForm;
		 }
		 
		 for(MetaForm formObj:metaForms){
			 for(MetaTable tableObj:metaTables){
				 if(formObj.getTable().getId().equals(tableObj.getId())){
					 status=true;
					 break;
				 }else{
					 status=false;
				 }
			 }
			 
			 if(!status){
				 status=false;
				 message="Form '"+formObj.getFormName()+"' mapped table '"+formObj.getTable().getTableName()+"' is not in the module '"+module.getName()+"'.";
				 break;
			 }
		 }
		 validForm.put("isValid", status);
		 validForm.put("moduleMessage", message);
		 return validForm;
	 }
	 
	 /**
	  * Write the constructed Sql Dump in OutputStream
	  * @param sqlString
	  * @param dumpOutPut
	  */
	 private void constructSqlDump(String sqlString,OutputStream dumpOutPut){
		 try {
			 
			 if(!tableService.getDataBaseSchema().equals("mysql")){
				 sqlString = sqlString.replaceAll("`", "");
			 }
				//Process child= rt.exec(aaaa);
				InputStream in =new ByteArrayInputStream(sqlString.getBytes());
		         int ch;
		         while ((ch = in.read()) != -1) {
		             dumpOutPut.write(ch);
		         }
		        
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 }
	 
	 /**
	  * To construct sql dump for meta forms
	  * @param metaForms
	  * @return
	  * @throws EazyBpmException
	  */
	 private String constructFormDump(Set<MetaForm>  metaForms)throws EazyBpmException{
		 User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 Date createdOn = new Date();
		 String createdDataString = DateUtil.convertDateToString(createdOn);
		 StringBuffer formDump=new StringBuffer();
		 formDump.append("\n\n\n\n");
		 formDump.append("insert into `ETEC_RE_FORM`(`id`,`is_active`,`created_by`,`created_time`,`english_name`,`form_name`,`html_source`,`is_delete`,`is_jsp_form`,`print_template`,`is_public`,`revision`," +
		 		"`table_name`,`is_template`,`version`,`xml_source`,`table_id`) values ");
		 
		 if(tableService.getDataBaseSchema().equals("mysql")){
			 createdDataString = CommonUtil.convertDateTypeForDB(tableService.getDataBaseSchema(),createdDataString);
			 createdDataString = "'"+createdDataString+"'";
		 }else{
			 createdDataString = CommonUtil.convertDateTypeForDB(tableService.getDataBaseSchema(),createdDataString);
		 }
		 
		 for(MetaForm metaForm:metaForms){
			 String printTemp="";
			 if(metaForm.getPrintTemplate()!=null){
				 printTemp=metaForm.getPrintTemplate().replace("'", "\\'");
			 }
			 if(metaForm.isActive()){
				 formDump.append("('"+metaForm.getId()+"',"+metaForm.isActive()+",'"+user+"',"+createdDataString+",'"+metaForm.getEnglishName()+"','"+metaForm.getFormName()+"','"+metaForm.getHtmlSource().replace("'", "\\'"));
				 formDump.append("',"+metaForm.getIsDelete()+","+metaForm.getIsJspForm()+",'"+printTemp+"',"+metaForm.isPublicForm()+","+metaForm.getRevisionNext()+",'"+metaForm.getTableName()+"',"+metaForm.isTemplateForm()+","+metaForm.getVersion());
				 formDump.append(",'"+metaForm.getXmlSource().replace("'", "\\'")+"','"+metaForm.getTable().getId()+"'),");
		 }
		 }
		 
		 if(formDump.lastIndexOf(",")>0){
			 formDump.deleteCharAt(formDump.lastIndexOf(","));
		 }
		 formDump.append(";");
		 return formDump.toString();
	 }
	 
	 /**
	  * To construct sql dump for Module Table Relation
	  * @param moduleId
	  * @param metaTables
	  * @return
	  * @throws EazyBpmException
	  */
	 private String constructModuleTableRelation(String moduleId,Set<MetaTable> metaTables)throws EazyBpmException{
		 StringBuffer moduleTableRelationDump=new StringBuffer();
		 moduleTableRelationDump.append("\n\n\n\n");
		 
		 if(tableService.getDataBaseSchema().equals("mysql")){
			 moduleTableRelationDump.append("insert into `ETEC_MODULE_TABLES`(`table_id`,`module_id`) values ");
		 }
		 
		 for(MetaTable metaTable:metaTables){
			 if(!tableService.getDataBaseSchema().equals("mysql")){
				 moduleTableRelationDump.append("insert into `ETEC_MODULE_TABLES`(`table_id`,`module_id`) values ");
			 }
			 moduleTableRelationDump.append("('"+metaTable.getId()+"','"+moduleId+"')");
			 if(tableService.getDataBaseSchema().equals("mysql")){
				 moduleTableRelationDump.append(",");
			 }else{
				 moduleTableRelationDump.append(";\n");
			 }
		 }
		 if(tableService.getDataBaseSchema().equals("mysql")){
			 if(moduleTableRelationDump.lastIndexOf(",")>0){
			 	 moduleTableRelationDump.deleteCharAt(moduleTableRelationDump.lastIndexOf(","));
			 }
			 moduleTableRelationDump.append(";");
		 }
		 
		 return moduleTableRelationDump.toString();
	 }
	 
	 /**
	  * To construct sql dump for Module From Relation
	  * @param moduleId
	  * @param metaFroms
	  * @return
	  * @throws EazyBpmException
	  */
	 private String constructModuleFromRelation(String moduleId,Set<MetaForm> metaFroms)throws EazyBpmException{
		 StringBuffer moduleTableRelationDump=new StringBuffer();
		 moduleTableRelationDump.append("\n\n\n\n");
		 if(tableService.getDataBaseSchema().equals("mysql")){
			 moduleTableRelationDump.append("insert into `ETEC_MODULE_FORMS`(`from_id`,`module_id`) values ");
		 }
		 
		 for(MetaForm metaForm:metaFroms){
			 if(metaForm.isActive()){
				 if(!tableService.getDataBaseSchema().equals("mysql")){
					 moduleTableRelationDump.append("insert into `ETEC_MODULE_FORMS`(`from_id`,`module_id`) values ");
				 }
				 moduleTableRelationDump.append("('"+metaForm.getId()+"','"+moduleId+"')");
				 if(tableService.getDataBaseSchema().equals("mysql")){
					 moduleTableRelationDump.append(",");
				 }else{
					 moduleTableRelationDump.append(";\n");
				 }
			 }
		 }
		 
		 if(tableService.getDataBaseSchema().equals("mysql")){
			 if(moduleTableRelationDump.lastIndexOf(",")>0){
			 	 moduleTableRelationDump.deleteCharAt(moduleTableRelationDump.lastIndexOf(","));
			 }
			 moduleTableRelationDump.append(";");
		 }
		 
		 return moduleTableRelationDump.toString();
	 }
	 
	 /**
	  * To construct sql dump for Module
	  * @param module
	  * @return
	  * @throws EazyBpmException
	  */
	 private String constructModuleDumps(Module module)throws EazyBpmException{
		 User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 Date createdOn = new Date();
		 StringBuffer moduleDump=new StringBuffer();
		 
		 if(tableService.getDataBaseSchema().equals("mysql")){
			 moduleDump.append("insert  into `ETEC_MODULE`(`module_id`,`created_by`,`created_time`,`description`,`is_public`,`is_system_module`,`module_order`,`name`,`updated_by`,`updated_time`) values (");
			 moduleDump.append("'"+module.getId()+"','"+user.getUsername()+"','"+DateUtil.convertDateToString(createdOn)+"','"+(module.getDescription() != null ? module.getDescription().replace("'","") : "")+"'," +
			 		""+module.getIsPublic()+","+module.getIsSystemModule()+","+module.getModuleOrder()+",'"+module.getName()+"','"+module.getUpdatedBy()+"','"+CommonUtil.convertDateTypeForDB(tableService.getDataBaseSchema(), DateUtil.convertDateToString(createdOn))+"');");
		 }else{
			 moduleDump.append("insert  into `ETEC_MODULE`(`module_id`,`created_by`,`created_time`,`description`,`is_public`,`is_system_module`,`module_order`,`name`,`updated_by`,`updated_time`) values (");
			 moduleDump.append("'"+module.getId()+"','"+user.getUsername()+"',"+CommonUtil.convertDateTypeForDB(tableService.getDataBaseSchema(), DateUtil.convertDateToString(createdOn))+",'"+module.getDescription()+"'," +
			 		""+CommonUtil.checkTrueOfFalse(module.getIsPublic())+","+CommonUtil.checkTrueOfFalse(module.getIsSystemModule())+","+module.getModuleOrder()+",'"+module.getName()+"','"+module.getUpdatedBy()+"',"+CommonUtil.convertDateTypeForDB(tableService.getDataBaseSchema(), DateUtil.convertDateToString(createdOn))+");");
			 moduleDump.append("\n\n\n\n");
			 //System.out.println("==============="+moduleDump);
			 String moduleDumpStr = moduleDump.toString();
			 moduleDumpStr = moduleDumpStr.replaceAll("`", "").replaceAll(",true,", ",1,").replaceAll(",false,",",0,");
			 return moduleDumpStr;
		 }
		 moduleDump.append("\n\n\n\n");
		 
		 return moduleDump.toString();
	 }
	 
	 /**
	  * {@inheritDoc}
	  */
	 public List<Module> getListViewModules(String listViewId)throws EazyBpmException{
		 List<String> moduleIds=moduleDao.getListViewModules(listViewId);
		 return moduleDao.getModulesByIds(moduleIds);
	 }
	 
	@Autowired
	public void setModuleDao(ModuleDao moduleDao) throws EazyBpmException{
		this.moduleDao = moduleDao;
	}
	
	@Autowired
    public void setTableService(TableService tableService) {
             this.tableService = tableService;
    }
	
	@Autowired
    public void setListViewService(ListViewService listViewService) {
        this.listViewService = listViewService;
    }
}
