package com.eazytec.bpm.admin.module.service;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Classification;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Module;
import com.eazytec.model.ModuleRolePrivilege;
import com.eazytec.service.ModuleExistsException;
/**
 * It interacts with ModuleDao to save delete retrieve Module details
 * @author revathi
 *
 */
public interface ModuleService {
	
	/**
	 * Used to Save module object into DB.If ti already exist throw ModuleExistsException
	 * @param module
	 * @return
	 * @throws ModuleExistsException
	 */	
	Module saveModule(Module module) throws EazyBpmException;

	/**
	 * Used to get ALl module list in LabelValue bean
	 * @return
	 * @throws EazyBpmException
	 */
	List<LabelValue> getAllModules(boolean isPrivilegeNeeded) throws EazyBpmException;
	
	/**
	 * Used to get the Module for given module id
	 * @param moduleId
	 * @return
	 * @throws EazyBpmException
	 */
	Module getModule( String moduleId) throws EazyBpmException;
	
	Map<String,Object> isValidFormTable(String moduleId)throws EazyBpmException;
	
	/**
	 * Used to get the Module for given module id
	 * @param moduleId
	 * @return
	 * @throws EazyBpmException
	 */
	Module getModuleByName( String moduleName) throws EazyBpmException;
	
	Map<String,Object> getModuleDetails(String moduleId) throws EazyBpmException;
	
	/**
	 * Get all Classification List
	 * @return
	 * @throws EazyBpmException
	 */
	List<Classification> getAllClassificationList() throws EazyBpmException;
	
	/**
	 * Get List of classification for given list of classification ids
	 * @param ids
	 * @return
	 * @throws EazyBpmException
	 */
	List<Classification> getClassificationByIds(List<String> ids) throws EazyBpmException;
	
	/**
	 * Get All module list
	 * @return
	 * @throws EazyBpmException
	 */
	List<Module> getAllModuleList() throws EazyBpmException;
	
	/**
	 * Get all parent module list
	 * @return
	 * @throws EazyBpmException
	 */
	//List<LabelValue> getAllParentModule() throws EazyBpmException;
	
	/**
	 * Get All parent module list
	 * @return
	 * @throws EazyBpmException
	 */
	// List<Module> getAllParentModuleList() throws EazyBpmException;
	 
	 /**
	  * Get all child module list
	  * @return
	  * @throws EazyBpmException
	  */
	// List<Module> getAllChildModuleList() throws EazyBpmException;
	 
	 /**
	  * Get All modules for given list of ids
	  * @param idsrecycleGridData
	  * @return
	  * @throws EazyBpmException
	  */
	 List<Module> getModulesByIds(List<String> ids)throws EazyBpmException;

	 String removeModule(String moduleId)throws EazyBpmException, ModuleExistsException;
	 
	 Module isModuleExist(String name)throws EazyBpmException;
	 
	 /**
	  * Gets all the modules that were assigned to the roles
	  * @param roles
	  * @return modules
	  * @throws EazyBpmException
	  */
	 //List<Module> getAllModulesForRoles(Set<Role>roles) throws EazyBpmException;
	 /**
	  * Return All parent modules label value List
	  * @return
	  * @throws BpmException
	  */
	 //List<LabelValue> getParentModuleAsLabelValue() throws BpmException;
	 
	 /**
	  * Return Module object label value bean for given id
	  * @param moduleId
	  * @return
	  * @throws BpmException
	  */
	 LabelValue getModuleAsLabelValueForId(String moduleId) throws BpmException;
	 
	 /**
	  * To change the module order 
	  * @param moduleId
	  * @param orderingType
	  * @return
	  * @throws BpmException
	  */
	 //boolean changeModuleOrder(String moduleId,String orderingType)throws BpmException;
	 boolean changeModuleOrder(int currentNodeOrder,int replaceNodeOrder)throws BpmException;
	 
	 /**
	  * To set module Privileges
	  * @param moduleRolePrivileges
	  * @param moduleId
	  * @return
	  * @throws EazyBpmException
	  */
	 Set<ModuleRolePrivilege> saveModuleRolePrivilege(Set<ModuleRolePrivilege> moduleRolePrivileges,String moduleId)throws EazyBpmException;
	 
	// void getMetaDataInfo(String moduleId,HSSFWorkbook wb)throws EazyBpmException;
	 
	 /**
	  * To get the module dump for migration
	  */
	 void getModuleDump(OutputStream dumpOutPut ,String moduleId,List<String> dataBaseInfo )throws EazyBpmException;
	 
	 
	 /**
	  * To get the module for the list view
	  * @param listViewId
	  * @return
	  * @throws EazyBpmException
	  */
	 List<Module> getListViewModules(String listViewId)throws EazyBpmException;
	 
	 /**
	  *  get modules by current logged in user edit privilege
	  * @return
	  * @throws Exception
	  */
	 List<Module> getModulesByUserPrivilege() throws BpmException;

}
