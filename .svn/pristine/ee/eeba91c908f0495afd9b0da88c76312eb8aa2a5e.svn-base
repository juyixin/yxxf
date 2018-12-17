package com.eazytec.bpm.admin.role.service;

import java.util.List;
import java.util.Map;

import com.eazytec.dto.RoleDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.service.GenericManager;
import com.eazytec.service.GroupExistsException;
import com.eazytec.service.RoleExistsException;

public interface RoleService extends GenericManager<Role, String>{
	
	/**
     * Retrieves a role by roleId.  An exception is thrown if group not found
     *
     * @param roleId the identifier for the group
     * @return Group
     */
   /* @GET
    @Path("{id}")
    Role getRole(@PathParam("id") String roleId);*/


    /**
     * Retrieves a list of all roles.
     *
     * @return List
     */
    List<Role> getRoles();
    
    /**
     * Finds a role by their rolename.
     *
     * @param rolename the role's rolename used to login
     * @return Role a populated role object
     */
    Role getRoleByName(String rolename);

    /**
     * Saves a Roles's information
     *
     * @param role the role's information
     * @return updated role
     * @throws GroupExistsException thrown when group already exists
     */
    Role saveRole(Role role) throws RoleExistsException;

    /**
     * Removes a group from the database by their groupId
     *
     * @param groupId the group's id
     */
    void removeRole(String roleId);
    
    public Role getRole(String rolename);
    
    /**
     * Finds a role by their id.
     *
     * @param id the role's id used to login
     * @return Role a populated role object
     */
    Role getRoleById(String id);
    
    /**
     * Get the users lists based on the role id
     * @param id
     * @return
     */
   // Set<User> getUserByRoleId(String id);
    public List<LabelValue> getUsersLabelValueByRoleId(String id) throws EazyBpmException;

    /**
     * Finds a roles by their names.
     *
     * @param id the role's name used to login
     * @return Role a populated role object
     */
    List<Role> getRolesByNames(List<String> names);
    
    /**
     * method for delete the selected roles from role grid.
     * 
     * @param ids.
     * @throws Exception
     */
    public String deleteSelectedRoles(List<String> ids,User user) throws Exception;
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllRoles();
    
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
     * method for remove role department
     * 
     * @param department
     * @param newDepartment
     * @throws
     */
    public void updateRoleDepartment(Department department,Department newDepartment);
    
    /**
     * 
     * @return
     * @throws BpmException
     */
    public List<RoleDTO> getRolesDTO() throws BpmException;
    
    
    /**
     * Gets all the role names as labelvalue pair
     * @return
     * @throws EazyBpmException
     */
    public List<LabelValue> getAllRoleNames() throws EazyBpmException;
    
    /**
     * Get the Department admin's roles
     * @param userAdminDep
     * @throws EazyBpmException
     * @return
     */
    List<String> getUserAdminDepartmentRoles(List<String> userAdminDep)throws EazyBpmException;
    
	 /**
     * Gets all the role names as labelvalue pair
     * @return
     * @throws BpmException
     */
    public List<LabelValue> getAllRolesAsLabelValue() throws BpmException;
    
    List<User> getUsersByRoleIds(List<String> ids) throws EazyBpmException;
    
    /**
	 * Generates order no for a role based on the order no of the parent id related to its child role
	 * @return
	 * 		 Order no of the role id
	 * @throws EazyBpmException
	 */
	public int generateOrderNo() throws EazyBpmException;
	
   /**
	 * 
	 * @return
	 * @throws EazyBpmException
	 */
	public boolean updateRoleOrderNos(List<Map<String,Object>> roleList) throws EazyBpmException;
	
	List<Role> getAllUserRoles(User user)throws EazyBpmException;
	
	public List<String> getUserNamesByRoleId(String id) throws EazyBpmException;
	
	/**
	 * get usename list for given role id list
	 * @param ids
	 * @return
	 * @throws EazyBpmException
	 */
	List<String> getUserNamesByRoleIds(List<String> ids) throws EazyBpmException;
}
