package com.eazytec.service;

import java.util.List;
import java.util.Set;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.eazytec.dto.UserDTO;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.User;

/**
 * Web Service interface so hierarchy of Generic Manager isn't carried through.
 * @author madan
 */
public interface UserService {
    /**
     * Retrieves a user by userId.  An exception is thrown if user not found
     *
     * @param userId the identifier for the user
     * @return User
     */
    User getUser(String userId);
    
    
    /**
     * Get user by user id
     * 
     * @param id
     * @return
     */
    User getUserById(String id);

    /**
     * Finds a user by their username.
     *
     * @param username the user's username used to login
     * @return User a populated user object
     */
    User getUserByUsername(String username);

    /**
     * Retrieves a list of all users.
     *
     * @return List
     */
    List<User> getUsers();

    /**
     * Saves a user's information
     *
     * @param user the user's information
     * @return updated user
     * @throws UserExistsException thrown when user already exists
     */
    User saveUser(User user) throws UserExistsException;
    
    /**
     * save user via API
     * @param user
     * @throws UserExistsException
     */
    User saveUserForAPI(User user) throws UserExistsException;

    /**
     * Removes a user from the database by their userId
     *
     * @param userId the user's id
     */
    void removeUser(String userId);
    
    /**
     * 
     * @param usernames
     * @return
     * @throws UsernameNotFoundException
     */
    public List<User> getUserByUsernames(List<String> usernames) throws UsernameNotFoundException;
    
    /**
     * 
     * @param usernames
     * @return
     * @throws UsernameNotFoundException
     */
    public List<User> getDistinctUserByUsernames(List<String> usernames) throws UsernameNotFoundException;
    /**
     * get all users with some of fields by dto object
     * @return
     */
    public List<UserDTO> getAllUsersDTO(String name);
    
    /**
     * Get all user dto by deparment
     * @param departmentId
     * @return
     */ 
    public List<UserDTO> getAllUsersByDepartment(String departmentId);
    
    /**
     * Get all user dto by deparment
     * @param departmentId
     * @return
     */ 
    public List<UserDTO> getAllUsersByRole(String roleId);
    
   
    
    /**
     * Get all user dto by deparment
     * @param departmentId
     * @return
     */ 
    public List<UserDTO> getAllUsersByGroup(String groupId);
    
	public List<Object[]> getUserName(String userName);


	String getUserIdByUserRole(String name);


	String getUserRoleByUserId(String userId);    
	
	/**
	 * Get the department admin's
	 * @param user
	 * @return
	 * @throws EazyBpmException
	 */
	List<String> getUserAdminDepartments(User user)throws EazyBpmException;
	
	/**
	 * Get the group admin's
	 * @param user
	 * @return
	 * @throws EazyBpmException
	 */
	List<String> getUserAdminGroups(User user)throws EazyBpmException;
	
	
	List<String> getUserAdminRoles(User user)throws EazyBpmException;
	
	 
	 /**
	  * Get All user names for given role id
	  * @param roleId
	  * @return
	  */
	 List<String> getAllUsersByRolePermission(String roleId);
	 
	 /**
	  * Get All user list who is having given module permission
	  * @param moduleName
	  * @return
	  */
	 Set<String> getUserForModulePermission(String moduleName);
	 
	 /**
	  * Get users for given menu permission
	  * @param menuName
	  * @return
	  */
	 List<String> getUserForMenuPermission(String menuName);
    
}
