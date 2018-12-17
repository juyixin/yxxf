package com.eazytec.bpm.admin.role.dao;

import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.eazytec.dao.GenericDao;
import com.eazytec.dto.RoleDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Role;
import com.eazytec.model.User;

/**
 * Role Data Access Object (GenericDao) interface.
 *
 * @author madan
 */
public interface RoleDao extends GenericDao<Role, String> {
	
	  /**
     * Gets role information based on rolename
     * @param rolename the rolename
     * @return populated role object
     */
    Role getRoleByName(String rolename);

    /**
     * Removes a role from the database by name
     * @param rolename the role's rolename
     */
    void removeRole(String rolename);

   
    /**
     * Gets a list of roles ordered by the uppercase version of their rolename.
     *
     * @return List populated list of roles
     */
    List<Role> getRoles();
    
    /**
     * Saves a role's information.
     * @param role the object to be saved
     * @return the persisted Role object
     */
    @Transactional
    Role saveRole(Role role);

    /**
     * Gets role information based on id
     * @param id the id
     * @return populated role object
     */
    Role getRoleById(String id);
    
    /**
     * Gets the user list based on role id
     * @param id
     * @return
     
    Set<User> getUserByRoleId(String id);*/
    
    /**
     * Gets roles information based on name
     * @param names the names
     * @return populated roles object
     */
    List<Role> getRolesByNames(List<String> names);
    
    /**
     * 
     * @param id
     * @return
     * @throws EazyBpmException
     */
    public List<LabelValue> getUsersLabelValueByRoleId(String id) throws EazyBpmException;
    
    /**
     * Finds a roles by their ids.
     *
     * @param id the role's name used to login
     * @return Role a populated role object
     */
    List<Role> getRolesByIds(List<String> roleIds);
    
    /**
     * 
     * @param id
     * @return
     * @throws EazyBpmException
     * @author vinoth
     */
    public List<User> getUsersByRoleId(String id) throws EazyBpmException;

    /**
     * method for get roles by department
     * 
     * @param department
     */
    public List<Role> getRolesByDepartment(Department department); 
    
    /**
     * Get all roles dto
     * @return
     * @throws BpmException
     */
    public List<RoleDTO> getRolesDTO() throws BpmException;
    
    
    /**
     * Gets all rolenames
     * @return all rolenames in a labelvalue pair.
     * @throws EazyBpmException
     */
    public List<LabelValue> getAllRoleNames() throws EazyBpmException;

	/**
     * Gets all rolenames
     * @return all rolenames in a labelvalue pair.
     * @throws BpmException
     */
    public List<LabelValue> getAllRolesAsLabelValue() throws BpmException;
    
    List<User> getUsersByRoleIds(List<String> ids) throws EazyBpmException;
    
    /**
  	 * Updates role order no 
  	 * @param roleId
  	 * @param orderNo
  	 * @return
  	 * @throws EazyBpmException
  	 */
  	public boolean updateRoleOrderNos(String roleId, int orderNo) throws EazyBpmException;

  	public List<Object> getUserNamesByRoleId(String id) throws EazyBpmException;
  	
  	/**
  	 * Get the Department admin's roles
  	 * @param userAdminDep
  	 * @return
  	 * @throws EazyBpmException
  	 */
  	List<String>  getUserAdminDepartmentRoles(List<String>  userAdminDep) throws EazyBpmException;
  	
  	/**
  	 * Get user admin roles
  	 * @param adminDepartments
  	 * @return
  	 * @throws EazyBpmException
  	 */
    List<Role> getUserAdminRoles(List<String> adminDepartments)throws EazyBpmException;
    
    /**
     * Get user name for given role id list
     * @param ids
     * @return
     * @throws EazyBpmException
     */
    List<String> getUserNamesByRoleIds(List<String> ids) throws EazyBpmException;
}
