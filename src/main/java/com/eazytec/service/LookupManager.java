package com.eazytec.service;

import java.util.List;
import java.util.Map;

import com.eazytec.bpm.engine.TaskRole;
import com.eazytec.model.AgencySetting;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Menu;
import com.eazytec.model.Module;
import com.eazytec.model.Role;
import com.eazytec.model.SysConfig;
import com.eazytec.model.User;

/**
 * Business Service Interface to talk to persistence layer and
 * retrieve values for drop-down choice lists.
 *
 * @author madan
 * @author nkumar
 */
public interface LookupManager {
    /**
     * Retrieves all possible roles from persistence layer
     * @return List of LabelValue objects
     */
    List<LabelValue> getAllRoles();
    
    /**
     * Retrieves all possible groups from persistence layer
     * @return List of LabelValue objects
     */
    List<LabelValue> getAllGroups();
    
    /**
     * Retrieves all possible departments from persistence layer
     * @return List of LabelValue objects
     */
    List<LabelValue> getAllDepartments();
    
    /**
     * Retrieves all possible users from persistence layer
     * @return List of LabelValue objects
     */
    List<LabelValue> getAllUsers();
    
    /**
     * Retrieves all possible task roles from persistence layer
     * @return List of LabelValue objects
     */
    List<LabelValue> getAllTaskRoles();
    
    
    /**
     * Retrieves all possible process classification from persistence layer
     * @return List of LabelValue objects
     */
    List<LabelValue> getAllProcessClassifications();
    
    /**
     * Return Agency Setting information map
     * @return
     */
    Map<String,AgencySetting> getAllAgencySetting();
    
    /**
     * Retrieves all possible menus from persistence layer
     * @return List of Menu objects
     */
    List<Menu> getAllMenus();
    
    /**
     * Retrieves all possible top menus from persistence layer
     * @return List of Top Menu objects
     */
    List<Menu> getAllTopMenus();
    
    /**
     * get a menus for id list.
     *
     * @return Menu a populated menu List
     */
    List<Menu> getMenus(List<Role> roles);
    
    /**
     * get all global menus.
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
     * Returns all active Menu ordered by name
     * @return populated list of Menus
     */
    List<Menu> getAllActiveMenus();
    
    /**
     * <p>{@link TaskRole} that are allowed for the user to add at runtime apart from metadata.
     * Not all can be added at runtime, few are desided automatically or designed, like admin or creator</p>
     * @return
     * 
     * @author madan
     */
    List<LabelValue> getTaskRolesApplicableForRuntime();

	List<String> getAllSysConfig();

	List<SysConfig> getSysConfigsByType(String configType);

	Role getRoleByName(String roleName);
	
	public User getUser(String username);

	/**
	 * update time zone to sys config
	 */
	void updateTimeZonetoSysconfig();
	
}
