package com.eazytec.bpm.admin.module.dao;

import java.util.List;


import com.eazytec.dao.GenericDao;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Classification;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Module;
import com.eazytec.model.ModuleRolePrivilege;

/**
 * Module Data Access Object.
 *  
 * @author revathi
 *
 */
public interface ModuleDao extends GenericDao<Module, String>{
	
	/**
	 * Save the module object into DB
	 * @param menu
	 * @return
	 */
	  Module saveModule(Module module) throws EazyBpmException;
	  
	  /**
	   * Get all the module list 
	   * @return
	   */	  
	  List<Module> getAllModules() throws EazyBpmException;
	  
	  List<Module> getAllUserModules(String role_types,String privilegeType);
	  /**
	   * Get all the parent module
	   * @return
	   */	  
	  //List<Module> getAllParentModule() throws EazyBpmException;
	  
	  /**
	   * Get all the child module list
	   * @return
	   * @throws EazyBpmException
	   */	  
	  //List<Module> getAllChildModule() throws EazyBpmException;
	  
	  /**
	   * Get all the Classification 
	   * @return
	   */	  
	  List<Classification> getAllClassification() throws EazyBpmException;
	  
	  /**
	   * Get the classification list for given ids
	   * @param ids
	   * @return
	   */
	  List<Classification> getClassificationByIds(List<String> ids) throws EazyBpmException;
	  
	  /**
	   * Get Module List by given list of ids
	   * @param ids
	   * @return
	   * @throws EazyBpmException
	   */	  
	  List<Module> getModulesByIds(List<String> ids)throws EazyBpmException;
	  
	  List<Module> getModulesByName(String name)throws EazyBpmException;
	  
	  //List<Module> getAllModulesForRoles(Set<Role>roles) throws EazyBpmException;
	  /**
	   * Get All parent modules LabelValue
	   * @return
	   * @throws BpmException
	   */
	  //List<LabelValue> getParentModuleAsLabelValue() throws BpmException;
	  /**
	   * Get Module LabelValue for given module id
	   * @return
	   * @throws BpmException
	   */
	  LabelValue getModuleAsLabelValueForId(String moduleId) throws BpmException;
	  
	  List<Module> getBetweenModuleList(int fromOrder,int toOrder)throws BpmException;
	  
	  void removeModuleRolePrivilege(String moduleId)throws BpmException;
	  ModuleRolePrivilege saveModuleRolePrivilege(ModuleRolePrivilege modulePrivileges)throws EazyBpmException;
	  
	  /**
	   * To get the modules for list view
	   * @param listViewId
	   * @return
	   * @throws EazyBpmException
	   */
	  List<String> getListViewModules(String listViewId)throws EazyBpmException;
	  
}
