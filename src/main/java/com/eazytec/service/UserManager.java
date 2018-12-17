package com.eazytec.service;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eazytec.dao.UserDao;
import com.eazytec.dto.UserDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Role;
import com.eazytec.model.User;


/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * @author madan
 */
public interface UserManager extends GenericManager<User, String> {
    /**
     * Convenience method for testing - allows you to mock the DAO and set it on an interface.
     * @param userDao the UserDao implementation to use
     */
    void setUserDao(UserDao userDao);

    /**
     * Retrieves a user by userId.  An exception is thrown if user not found
     *
     * @param userId the identifier for the user
     * @return User
     */
    User getUser(String userId);

    /**
     * Finds a user by their username.
     * @param username the user's username used to login
     * @return User a populated user object
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException
     *         exception thrown when user not found
     */
    User getUserByUsername(String username) throws UsernameNotFoundException;
    
    /**
     * Get a password by their user Id
     * @param userId
     * @retrun string
     */
    
    String isValidUserPassword(String userId, String oldPassword,String newPassword);
    
    /**
     * Retrieves a list of all users.
     * @return List
     */
    List<User> getUsers();

    /**
     * Saves a user's information.
     *
     * @param user the user's information
     * @throws UserExistsException thrown when user already exists
     * @return user the updated user object
     */
    User saveUser(User user) throws UserExistsException;

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
     * Retrieves the email password in DB for a user
     * @param userId the user's id
     * @return the email password in DB, if the user is already persisted
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    String getUserEmailPassword(String userId);
    
    /**
     * Saves a user's information.
     *
     * @param user the user's information
     * @throws UserExistsException thrown when user already exists
     * @return user the updated user object
     */
    public User saveEmailSetting(User user, HttpServletRequest request, HttpServletResponse response);
    	
    /**
     * Removes a user from the database
     *
     * @param user the user to remove
     */
    void removeUser(User user);

    /**
     * Removes a user from the database by their userId
     *
     * @param userId the user's id
     */
    void removeUser(String userId);
    
    /**
     * method for delete the selected users from user grid.
     * 
     * @param ids.
     * @throws Exception
     */
    public String deleteSelectedUsers(List<String> ids,User user) throws Exception;
    
    /**
     * method to delete the list of users.
     * 
     * @param users
     * @throws Exception 
     */
    public void deleteUser(User user)throws BpmException;
    
    /**
     * method for update the selected users status
     * @param ids
     * @param status
     * @throws Exception 
     */
    
    public String updateUserStatus(List<String> ids,boolean status,User logInuser) throws Exception;
    
    /**
     * update user department
     * @param users
     * @param department
     * 
     */
    public void updateUsersDepartment(Set<User> users, Department department);
    
    /**
     * update user part time department
     * @param users
     * @param partTimeDepartment
     * 
     */
    public void updateUsersPartTimeDepartment(Set<User> users, Department partTimeDepartment);
    
    /**
     * get all users by department
     * @param department
     * @return
     */
    public List<User> getUserByDepartments(Department department);
    
    /**
     * get all users
     * @return List<LabelValue>
     */
    public List<LabelValue> getAllUsers();
    
    /**
     * check the username already exist or not
     * @param username
     * @return
     */
    public boolean isDublicateUser(String username);
    
    /**
     * check the emailid already exits or not
     * @param email,userId
     * @return
     */
    
    public boolean isDuplicateEmail(String email,String userId);
    
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
     * Remove a list of user from the database
     *
     * @param users
     */
    void removeUsers(List<User> users);
    
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
   
    /**
	 * Get role users as by LabelValue
	 * 
	 * @param roleId
	 * @return
	 * @throws
	 */
    public List<LabelValue> getRoleUsersAsLabelValue(String roleId) throws BpmException;
   
    /**
  	 * Get role users as by LabelValue and current User
  	 * 
  	 * @param roleId
  	 * @param currentUser
  	 * @return
  	 * @throws
  	 */
    public List<LabelValue> getManagerRoleUsersAsLabelValue(String roleId, String currentEditUser) throws BpmException;
   
    /**
	 * Get group users as by LabelValue
	 * 
	 * @param groupId
	 * @return
	 * @throws
	 */
    public List<LabelValue> getGroupUsersAsLabelValue(String groupId) throws BpmException;
   
    /**
	 * Get group users as by LabelValue and current edit user
	 * 
	 * @param groupId
	 * @param current edit user
	 * @return
	 * @throws
	 */
    public List<LabelValue> getManagerGroupUsersAsLabelValue(String groupId, String currentEditUser) throws BpmException;
   
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
    public List<LabelValue> getManagerDepartmentUsersAsLabelValue(String departmentId, String currentUser) throws BpmException;
   
    boolean isUserAdmin(User user);

    boolean isDuplicateEmployeeNumber(String employeeNumber,String userId);
   
    /**
	 * Get user by email
	 * 
	 * @param email
	 * @return
	 */
    User getUserByEmail(String email);
  
    List<String> getExistUserIds(List<String> ids) throws BpmException;
  
    /**
     * Get user by user id
     * 
     * @param id
     * @return
     */
    User getUserById(String id);
  
    List<LabelValue> getUserLabelValueByIds(List<String> ids) throws Exception;
    
    /**
     * Get All users by given role ids
     * @param roleIds
     * @return List Of User
     * @throws BpmException
     */
    List<User> getUserByRoleIds(List<String> roleIds) throws BpmException;
    
    /**
     * Get All User by given group ids
     * @param groupIds
     * @return List Of User
     * @throws BpmException
     */
    List<User> geUserByGroupIds(List<String> groupIds) throws BpmException;
    
    /**
     * Get All User by given user ids
     * @param userIds
     * @return List Of User
     * @throws BpmException
     */
    List<User> getUserByUserIds(List<String> userIds) throws BpmException;
  
    List<User> getUsersByDepartmentIds(List<String> ids) throws EazyBpmException;
  
    List<LabelValue> getUserNameAndPosition(String userName,String currentUser,String appendTo) throws EazyBpmException;
  
    /**
     * Get All Users superior(manager) by given userid's
     * @param ids
     * @return
     * @throws EazyBpmException
     */
    List<String> getUsersSuperiorByIds(List<String> ids) throws EazyBpmException;
  
    /**
     * Get All Users Subordinate(secretary) by given userid's
     * @param ids
     * @return
     * @throws EazyBpmException
     */
    List<String> getUsersSubordinateByIds(List<String> ids) throws EazyBpmException;
  
    /**
     * Return All user for given department id's
     * @param ids
     * @return
     * @throws EazyBpmException
     */
    List<String> getPeopleInSameDepartment(List<String> ids) throws EazyBpmException;
  
    /**
     * Get all Owner of user for given department id's
     * @param ids
     * @return
     * @throws EazyBpmException
     */
    List<String> getOwnerOfTheDepartment(List<String> ids) throws EazyBpmException;
  
    /**
     * return given users of departments interface users
     * @param ids
     * @return
     * @throws EazyBpmException
     */
    List<User> getDepartmentInterfaceUser(List<String> ids) throws EazyBpmException;
  
    /**
     * return given users of parent departments users for given user id's
     * @param ids
     * @return
     * @throws EazyBpmException
     */
    List<String> getParentDepartmentUser(List<String> ids) throws EazyBpmException;
  
  List<String> getUserIdsByDepartmentIds(List<String> ids) throws EazyBpmException;

  /**
     * 	
     * @param ids
     * @return
     * @throws EazyBpmException
     */
    List<String> getParentDepartmentWithSubDepartmentUser(List<String> ids) throws EazyBpmException;
  
    /**
     * return superiors superior for given id's
     * @param ids
     * @return
     * @throws EazyBpmException
     */
    String getAllRelativeIds(String id, boolean isChild) throws EazyBpmException;
  
  List<LabelValue> getDepartmentAdminRoleUsersAsLabelValue(String roleId,String userId) throws BpmException;
  
  List<LabelValue> getDepartmentAdminGroupUsersAsLabelValue(String groupId,String userId) throws BpmException;

  /**
	 * get is user email notification enabled or not
	 * @param userId
	 * @return
	 * @throws EazyBpmException
	 */
    boolean getEmailNotificationEnabled(String userId) throws EazyBpmException;
	  
    List<String> getSecretaryToLeader(List<String> ids) throws EazyBpmException;
	  
	Department getParentDepartmentForUser(List<String> ids) throws EazyBpmException;
	  
	List<String> getLeaderToSecretaryIds(List<String> ids) throws EazyBpmException;
	  
	List<UserDTO> getAllUsersById(List<String> userIds);
	
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
	 /**
	  * Set the Role for newly created user
	  * @param user
	  * @param isSetRole
	  * @param roles
	  * @throws BpmException
	  */
	 void setDefaultRoleForUser(User user,boolean isSetRole,Set<Role> roles) throws BpmException;

	 /**
	  * 此方法会被接口频繁调用，需做缓存处理
	  * @param token
	  * @return
	  */
	 User getUserByToken(String token);
	 
	 User updateUser(User user);
	 
}
