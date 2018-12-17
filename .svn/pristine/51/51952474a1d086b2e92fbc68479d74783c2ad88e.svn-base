package com.eazytec.dao;

import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.AgencySetting;
import com.eazytec.model.Classification;
import com.eazytec.model.Department;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Menu;
import com.eazytec.model.Module;
import com.eazytec.model.Role;
import com.eazytec.model.SysConfig;
import com.eazytec.model.TaskRole;
import com.eazytec.model.User;

import java.util.List;

/**
 * Lookup Data Access Object (GenericDao) interface.  This is used to lookup values in
 * the database (i.e. for drop-downs).
 *
 * @author madan
 * @author nkumar
 */
public interface LookupDao {
    //~ Methods ================================================================

    /**
     * Returns all Roles ordered by name
     * @return populated list of roles
     */
    List<Role> getRoles();
    
    /**
     * Returns all Groups ordered by name
     * @return populated list of groups
     */
    List<Group> getGroups();
    
    /**
     * Returns all Department ordered by name
     * @return populated list of Departments
     */
    List<Department> getDepartments();
    
    /**
     * Returns all User ordered by name
     * @return populated list of Users
     */
    List<User> getUsers();
    
    /**
     * Returns all Task Roles ordered by name
     * @return populated list of Users
     */
    List<TaskRole> getTaskRoles();
    
    
    /**
     * Returns all Process Classification Roles ordered by name
     * @return populated list of Users
     */
    List<Classification> getProcessClassifications();
    
    /**
     * Returns agencySetting detail Map
     * @return
     */
    List<AgencySetting> getAgencySetting();
    
    /**
     * Returns all Menu ordered by name
     * @return populated list of Menus
     */
    List<Menu> getMenus();
    
    /**
     * Returns all active Menu ordered by name
     * @return populated list of Menus
     */
    List<Menu> getAllActiveMenus();
    
    /**
     * Returns all Top Menu ordered by name
     * @return populated list of Top Menus
     */
    List<Menu> getTopMenus();
    
    /**
     * get a menus for id list.
     *
     * @return Menu a populated menu List
     */
    List<Menu> getMenus(List<Role> roles);
    
    /**
     * get a all global menus
     *
     * @return Menu a populated menu List
     */
    List<Menu> getGlobalMenus();
    
    /**
     * get Modules by roles
     * 
     * @param roles
     * @return
     */
    public List<Module> getModulesByRoles(List<Role> roles);
    
    /**
     * Get all the Sys config paramerters
     * @return
     */
    public List<String> getSysConfigs();
    
    /**
     * 
     * @param type
     * @return
     */
    public List<SysConfig> getSysConfigsByType(String type);

	public Role getRoleByName(String groupRole);
	
	public User getUser(String username);
	
	/**
	 * get system config by key
	 * 
	 * @param key
	 * @return
	 * @throws EazyBpmException
	 */
	List<SysConfig> getSysConfigByKey(String key)throws EazyBpmException;
}
