package com.eazytec.dao;

import com.eazytec.dto.UserDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.LabelValue;
import com.eazytec.model.User;

import org.hibernate.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * User Data Access Object (GenericDao) interface.
 *
 * @author madan
 * @author mathi
 */
public interface UserDao extends GenericDao<User, String> {

    /**
     * Gets users information based on login name.
     * @param username the user's username
     * @return userDetails populated userDetails object
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException thrown when user not
     * found in database
     */
    @Transactional
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * Gets a list of users ordered by the uppercase version of their username.
     *
     * @return List populated list of users
     */
    List<User> getUsers();

    /**
     * Saves a user's information.
     * @param user the object to be saved
     * @return the persisted User object
     */
    @Transactional
    User saveUser(User user);

    /**
     * Create internal message user id when save user
     * 
     * @param username
     * @param passwordHashAlgorithm
     * @param password
     * @param version
     */
    void createInternalMessageId(String username, String passwordHashAlgorithm, String password, int version);
    
    /**
     * get internal user by username
     *  
     * @param username
     * @return
     */
    String getInternalMessageId(String username);
    
    /**
     * Retrieves the password in DB for a user
     * @param userId the user's id
     * @return the password in DB, if the user is already persisted
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    String getUserPassword(String userId);
    
    /**
     * Retrieves the email password in DB for a user
     * @param userId the user's id
     * @return the email password in DB, if the user is already persisted
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    String getUserEmailPassword(String userId);

    /**
     * method for Retrieve list of user object
     * 
     * @param ids
     * @return List<User>
     */
    public List<User> getUsersByIds(List<String> ids);
    
    /**
     * method for delete the users
     * 
     * @param users
     */
    public void removeUser(User user);
    
    /**
     * method for update the users status
     * 
     * @param ids
     * @param status
     */
    public void updateUserStatus(List<String> ids,boolean status);
    
    /**
     * Load all users by usernames
     * @param usernames
     * @return
     * @throws UsernameNotFoundException
     */
    public List<User> loadUserByUsernames(List<String> usernames) throws UsernameNotFoundException;
    
    /**
     * 
     * @param usernames
     * @return
     * @throws UsernameNotFoundException
     */
    public List<User> getDistinctUserByUsernames(List<String> usernames) throws UsernameNotFoundException;
    
    /**
     * get all users by department
     * @param department
     * @return
     */
    public List<User> getUserByDepartments(Department department);
    
    /**
     * check the username already exist or not
     * @param username
     * @return
     */
    public boolean isDublicateUser(String username);
    
    public boolean isDuplicateEmail(String email,String userId);
   
    public boolean isDuplicateEmployeeNumber(String username,String userId);
    /**
     * get all users by department
     * @param department
     * @return
     */
    public List<User> getUsersByDepartment(Department department);
    
    /**
     * get all users by part time department
     * @param partTimeDepartment
     * @return
     */
    public List<User> getUsersByPartTimeDepartment(Department partTimeDepartment);
    
    /**
     * update user manager
     * 
     * @param userId
     * @param manager
     */
    public void updateManager(String userId,String manager);
    
    /**
     * update user secretary
     * 
     * @param userId
     * @param secretary
     */
   public void updateSecretary(String userId,String secretary);
   
   /** Get all users by specific fields with dto object
    * @author vinoth
    * @return
    */
   public List<UserDTO> getAllUsers(String name);
   
	/**
	 * Get all users dto by departmentid
	 * @author vinoth
	 * @param departmentId
	 * @return
	 */
	public List<UserDTO> getAllUsersByDepartment(String departmentId);
	
	/**
	 * Get all users dto by departmentid
	 * @author vinoth
	 * @param departmentId
	 * @return
	 */
	public List<UserDTO> getAllUsersByRole(String roleId);
	
	/**
	 * Get all users dto by departmentid
	 * @author vinoth
	 * @param departmentId
	 * @return
	 */
	public List<UserDTO> getAllUsersByGroup(String groupId);
	
	/**
	 * Get role users as by LabelValue
	 * 
	 * @param roleId
	 * @return
	 * @throws
	 */
    public List<LabelValue> getRoleUsersAsLabelValue(String roleId) throws BpmException;
    
    /**
	 * Get role users as by LabelValue and current user
	 * 
	 * @param roleId
	 * @param currentUser
	 * @return
	 * @throws
	 */
    public List<LabelValue> getManagerRoleUsersAsLabelValue(String roleId, List<String> subordinateIds) throws BpmException;
    
    /**
	 * Get group users as by LabelValue
	 * 
	 * @param groupId
	 * @return
	 * @throws
	 */
    public List<LabelValue> getGroupUsersAsLabelValue(String groupId) throws BpmException;
    
    /**
	 * Get group users as by LabelValue and current user
	 * 
	 * @param groupId
	 * @param current user
	 * @return
	 * @throws
	 */
    public List<LabelValue> getManagerGroupUsersAsLabelValue(String groupId, List<String> subordinateIds) throws BpmException;
    
    /**
	 * Get department users as by LabelValue
	 * 
	 * @param departmentId
	 * @return
	 * @throws
	 */
    public List<LabelValue> getDepartmentUsersAsLabelValue(String departmentId) throws BpmException;
    
    /**
   	 * Get department users as by LabelValue and current user
   	 * 
   	 * @param departmentId
   	 * @param current user
   	 * @return
   	 * @throws
   	 */
     public List<LabelValue> getManagerDepartmentUsersAsLabelValue(String departmentId, List<String> subordinateIds) throws BpmException;
    
    /**
	 * Get user by email
	 * 
	 * @param email
	 * @return
	 */
    User getUserByEmail(String email);
    
    /**
	 * Get user by user id
	 * 
	 * @param id
	 * @return
	 */
    User getUserById(String id);
    
    /**
     * Get User Details by User name
     * @param userName
     * @return
     */
    
	public List<Object[]> getUserName(String userName);
	/**
	 * Get all user for given role ids
	 * @param ids
	 * @return
	 * @throws EazyBpmException
	 */
	List<User> getUsersByRoleIds(List<String> ids) throws EazyBpmException;
	/**
	 * Get All user for given group ids
	 * @param ids
	 * @return
	 * @throws EazyBpmException
	 */
	List<User> getUsersByGroupIds(List<String> ids) throws EazyBpmException;
	
	List<User> getUsersByDepartmentIds(List<String> ids) throws EazyBpmException;
	
	List<LabelValue> getUserNameAndPosition(String userName) throws EazyBpmException;

	String getUserIdByUserRole(String userRole) throws EazyBpmException;
	
	/**
	 * return user superior(manager) for given userids
	 * @param ids
	 * @return
	 * @throws EazyBpmException
	 */
	List<String> getUsersSuperiorByIds(List<String> ids) throws EazyBpmException;
	
	/**
	 * return users subordinate(secretary)
	 * @param ids
	 * @return
	 * @throws EazyBpmException
	 */
	List<String> getUsersSubordinateByIds(List<String> ids) throws EazyBpmException;
	
	List<String> getLeaderToSecretaryIds(List<String> ids) throws EazyBpmException;
	
	String getUserRoleByUserId(String userId);
	/**
	 * Get All user of given department ids
	 * @param userId
	 * @return
	 */
	List<String> getPeopleInSameDepartment(List<String> ids) throws EazyBpmException;
	
	/**
	 * Get the department owner of given departmentIds
	 * @param ids
	 * @return
	 * @throws EazyBpmException
	 */
	List<String> getOwnerOfTheDepartment(List<String> ids) throws EazyBpmException;
	
	/**
	 * get user of department interface user
	 * @param ids
	 * @return
	 * @throws EazyBpmException
	 */
	List<User> getDepartmentInterfaceUser(List<String> ids) throws EazyBpmException;
	
	/**
	 * get the user of parent department for given user ids
	 * @param ids
	 * @return
	 * @throws EazyBpmException
	 */
	List<String> getParentDepartmentUser(List<String> ids) throws EazyBpmException;
	
	/**
	 * 
	 * @param ids
	 * @return
	 * @throws EazyBpmException
	 */
	List<String> getParentDepartmentWithSubDepartmentUser(List<String> ids) throws EazyBpmException;
	
	Department getParentDepartmentForUser(List<String> ids) throws EazyBpmException;
	
	/**
	 * get superiors superior for given id's
	 * @param ids
	 * @return
	 * @throws EazyBpmException
	 */
	String getAllRelativeIds(String id, boolean isChild) throws EazyBpmException;
	
	/**
	 * get is user email notification enabled or not
	 * @param userId
	 * @return
	 * @throws EazyBpmException
	 */
	public boolean getEmailNotificationEnabled(String userId) throws EazyBpmException;
	
	List<String> getSecretaryToLeader(List<String> ids) throws EazyBpmException;
	
	List<String> getUserIdsByDepartmentIds(List<String> ids) throws EazyBpmException;
	
	/**
	 * 
	 * @param userName
	 * @param subordinateIds
	 * @return
	 * @throws EazyBpmException
	 */
	 List<Object[]> getManagerNameAndPosition(String userName,String currentUser) throws EazyBpmException;
	 
	 List<LabelValue> getAdminDepartmentRoleUsersAsLabelValue(String roleId,Set<String> adminisDepartmentIds) throws BpmException;
	 
	 List<LabelValue> getDepartmentAdminGroupUsersAsLabelValue(String groupId,Set<String> adminisDepartmentIds) throws BpmException;
	 public List<UserDTO> getAllUsersById(List<String> userIds);

	/**
	  * get user fullnames by usernames
	  * 
	  * @param usernames
	  * @return
	  */
	 List<String> getUserFullNamesByUsernames(List<String> usernames);
	 
	 /**
	  * get user fullname by username
	  * 
	  * @param username
	  * @return
	  */
	 String getUserFullNamesByUsername(String username);
	 
	 /**
	  * delete internal message id when delete user
	  * 
	  * @param username
	  */
	 void deleteInternalUserId(String username);
	 
	 String getUserMailIdByUser(String userId) throws EazyBpmException;

	 /**
	  * get users email id by role
	  * 
	  * @param roleId
	  * @return
	  * @throws EazyBpmException
	  */
	 List<String> getUsersEmailIdByRole(String roleId) throws EazyBpmException;
	 
	 /**
	  * get users email id by group
	  * 
	  * @param groupId
	  * @return
	  * @throws EazyBpmException
	  */
	 List<String> getUsersEmailIdByGroup(String groupId) throws EazyBpmException;
	 
	 /**
	  * get users email id by department
	  * @param departmentId
	  * @return
	  * @throws EazyBpmException
	  */
	 List<String> getUsersEmailIdByDepartment(String departmentId) throws EazyBpmException;
	 
	 /**
	  * get user mobile no by role
	  * 
	  * @param roleId
	  * @return
	  * @throws EazyBpmException
	  */
	 List<String> getUsersMobileNoByRole(String roleId) throws EazyBpmException;

	 /**
	  * get user mobile no by group
	  * 
	  * @param groupId
	  * @return
	  * @throws EazyBpmException
	  */
	 List<String> getUsersMobileNoByGroup(String groupId) throws EazyBpmException;

	 /**
	  * get user mobile no by department
	  * 
	  * @param departmentId
	  * @return
	  * @throws EazyBpmException
	  */
	 List<String> getUsersMobileNoByDepartment(String departmentId) throws EazyBpmException;
    
	 /**
	  * get user mobile no by user id
	  * 
	  * @param userId
	  * @return
	  * @throws EazyBpmException
	  */
	 List<String> getUsersMobileNoByUser(String userId) throws EazyBpmException;
	 
     /**
	  * get users internal id by role
	  * 
	  * @param roleId
	  * @param domainName
	  * @return
	  * @throws EazyBpmException
	  */
	 List<String> getUsersInternalMessageIdByRole(String roleId, String domainName) throws EazyBpmException;
    
	 /**
	  * get users internal id by group
	  * 
	  * @param groupId
	  * @param domainName
	  * @return
	  * @throws EazyBpmException
	  */
	 List<String> getUsersInternalMessageIdByGroup(String groupId, String domainName) throws EazyBpmException;
    
	 /**
	  * get users internal id by department
	  * 
	  * @param departmentId
	  * @param domainName
	  * @return
	  * @throws EazyBpmException
	  */
	 List<String> getUsersInternalMessageIdByDepartment(String departmentId, String domainName) throws EazyBpmException;

	User getUserByToken(String token);
}
