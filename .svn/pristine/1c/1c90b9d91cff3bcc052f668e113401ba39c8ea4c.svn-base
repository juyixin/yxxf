package com.eazytec.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.admin.department.service.DepartmentService;
import com.eazytec.bpm.admin.group.service.GroupService;
import com.eazytec.bpm.admin.menu.service.MenuService;
import com.eazytec.bpm.admin.module.service.ModuleService;
import com.eazytec.bpm.admin.role.service.RoleService;
import com.eazytec.bpm.oa.dms.service.FolderService;
import com.eazytec.bpm.oa.mail.service.MailService;
import com.eazytec.common.util.CipherUtils;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.StringUtil;
import com.eazytec.dao.UserDao;
import com.eazytec.dao.hibernate.Updater;
import com.eazytec.dto.UserDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Menu;
import com.eazytec.model.Module;
import com.eazytec.model.ModuleRolePrivilege;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.service.UserExistsException;
import com.eazytec.service.UserManager;
import com.eazytec.service.UserService;

import dtw.webmail.JwmaSession;


/**
 * Implementation of UserManager interface.
 *
 * @author madan
 * @author mathi
 */
@Service("userManager")
public class UserManagerImpl extends GenericManagerImpl<User, String> implements UserManager, UserService {
    private PasswordEncoder passwordEncoder;
    private UserDao userDao;
    private DepartmentService departmentService;
    private GroupService groupService;
    private FolderService folderService;
    private MailService mailService;
    private RoleService roleService = null;
    private ModuleService moduleService;
    private MenuService  menuService;
     
	@Autowired
    public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	@Autowired
	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}

	@Autowired
    public void setDepartmentService(DepartmentService departmentService) {
  		this.departmentService = departmentService;
  	}
    
    @Autowired
    public void setGroupService(GroupService groupService) {
  		this.groupService = groupService;
  	}
    
    @Autowired
    public void setFolderService(FolderService folderService) {
  		this.folderService = folderService;
  	}
    
    
    @Autowired
    public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
    
    @Autowired(required = false)
    private SaltSource saltSource;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.dao = userDao;
        this.userDao = userDao;
    }

    @Autowired
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
    
    /**
     * {@inheritDoc}
     */
    public User getUser(String userId) {
      // return userDao.get(new Long(userId));
    	return userDao.get(userId);
    }

    /**
     * {@inheritDoc}
     */
    public List<User> getUsers() {
        return userDao.getAllDistinct();
    }

    /**
     * {@inheritDoc}
     * @throws UserExistsException 
     */
    
    public User saveUserForAPI(User user) throws UserExistsException{
    	//setDefaultRoleForUser(user,false,null);
    	if(user.getPictureByteArrayId() == null){
    		user.setPictureByteArrayId("/images/profile/default.png");
    	}
    	if(user.getEmailPassword() == null){
    		user.setEmailPassword("");
    	}
    	user.setConfirmPassword(user.getPassword());
    	user.setCredentialsExpired(false);    		
    	User newUSer = saveUser(user);
    	return newUSer;
    }
    /**
     * {@inheritDoc}
     * @throws UserExistsException 
     */
    
    public User saveUser(User user) throws UserExistsException {
    	

       /* if (user.getVersion() == null) {
            // if new user, lowercase userId
            user.setUsername(user.getUsername().toLowerCase());
        }*/

        // Get and prepare password management-related artifacts
        boolean passwordChanged = false;
        if (passwordEncoder != null) {
           // Check whether we have to encrypt (or re-encrypt) the password
            if (user.getVersion() == 0) {
                // New user, always encrypt
                passwordChanged = true;
            } else {
                // Existing user, check password in DB
                String currentPassword = userDao.getUserPassword(user.getId());
                if (currentPassword == null) {
                    passwordChanged = true;
                } else {
                    if (!currentPassword.equals(user.getPassword())) {
                        passwordChanged = true;
                    }
                }
                
            }

            // If password was changed (or new user), encrypt it
            if (passwordChanged) {
                if (saltSource == null) {
                	//Encrypt Email password by base64
                	//user.setEmailPassword(CipherUtils.encrypt(user.getPassword()));
                	
                	// backwards compatibility
                    user.setPassword(passwordEncoder.encodePassword(user.getPassword(), null));
                    log.warn("SaltSource not set, encrypting password w/o salt");
                	
                } else {
                	//Encrypt Email password by base64
                	//user.setEmailPassword(CipherUtils.encrypt(user.getPassword()));
                    user.setPassword(passwordEncoder.encodePassword(user.getPassword(),saltSource.getSalt(user)));
                }
            }
        } else {
            log.warn("PasswordEncoder not set, skipping password encryption...");
        }
        
        try {
        	
        	// Temporary saving of picture and its properties.
        	
        	//user.setPicture(new Picture());
        	//user.setPictureByteArrayId("123");
        	//setting user department as Organisation if department is empty
        	if(user.getDepartment() == null){
        		user.setDepartment(departmentService.getRootDepartment());
        	}
        	user.setVersion(user.getVersion() + 1);
        	//return userDao.saveUser(user);
        	user = userDao.saveUser(user);
        	createInternalMessageId(CommonUtil.getInternalMessageId(user.getUsername()), "MD5", com.eazytec.bpm.common.util.StringUtil.getMD5String(user.getUsername()), 1);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        }
    }
    
    /**
     * {@inheritDoc}
     * @throws UserExistsException 
     */
    
    public void setDefaultRoleForUser(User user,boolean isSetRole,Set<Role> roles) throws BpmException{
    	if(user.getRoles() == null || user.getUserRole().isEmpty()){
    		Role userRole = roleService.getRoleByName("ROLE_"+user.getId()+"_USER");
    		if(userRole == null){
				Role role=new Role("ROLE_"+user.getId()+"_USER","ROLE_"+user.getId()+"_USER","Internal");
				role=roleService.save(role);
				user.setUserRole(role.getName());
				
				if(isSetRole && roles != null){
					roles.add(role);
					user.setRoles(roles);
				}else{
					user.addRole(role);
				}
    		} else {
    			if(isSetRole && roles != null){
    				user.setUserRole(userRole.getName());
	    			roles.add(userRole);
	    			user.setRoles(roles);
				}else{
					user.addRole(userRole);
				}
    		}
		}else{
			Role role=roleService.getRoleByName("ROLE_"+user.getId()+"_USER");
			user.addRole(role);
		}
    }

    /**
     * {@inheritDoc}
     */
    public void createInternalMessageId(String username, String passwordHashAlgorithm, String password, int version){
    	String user = getInternalMessageId(username);
    	if(user == null){
    		userDao.createInternalMessageId(username, passwordHashAlgorithm, password, version);
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    public String getInternalMessageId(String username){
    	return userDao.getInternalMessageId(username);
    }
    
    /**
     * {@inheritDoc}
     */
    public User saveEmailSetting(User user, HttpServletRequest request, HttpServletResponse response){
    	try {
	    	JwmaSession jwmaSession = mailService.getEmailAuthentication(request, response, user , true);
	      
	    	if(jwmaSession == null){
	    		throw new BpmException("session.login.authentication");
	    	}
	        // Get and prepare password management-related artifacts
	        boolean emailPasswordChanged = false;
	        
	        // Existing user, check password in DB
	        String currentEmailPassword = userDao.getUserEmailPassword(user.getId());
	        if (currentEmailPassword == null) {
	        	emailPasswordChanged = true;
	        } else {
	            if (!currentEmailPassword.equals(user.getEmailPassword())) {
	            	emailPasswordChanged = true;
	            }
	        }
	            
	        // If password was changed (or new user), encrypt it
	        if (emailPasswordChanged) {
	        	//Encrypt Email password by base64
	        	user.setEmailPassword(CipherUtils.encrypt(user.getEmailPassword()));
	        }
       
            return userDao.saveUser(user);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new BpmException("Problem in updating user mail setting.");
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public String getUserEmailPassword(String userId){
    	return userDao.getUserEmailPassword(userId);
    }
    
    /**
     * {@inheritDoc}
     */
    public void removeUser(User user) {
        log.debug("removing user: " + user);
        userDao.remove(user);
    }

    /**
     * {@inheritDoc}
     */
    public void removeUser(String userId) {
        log.debug("removing user: " + userId);
        userDao.removeUser(userDao.get(userId));
    }

    /**
     * {@inheritDoc}
     *
     * @param username the login name of the human
     * @return User the populated user object
     * @throws UsernameNotFoundException thrown when username not found
     */
    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return (User) userDao.loadUserByUsername(username);
    }
    
    /**
     * {@inheritDoc}
     * 
     * @param username and currentPassword
     * @return string true or false
     */
    public String isValidUserPassword(String userName, String oldPassword, String newPassword) throws UsernameNotFoundException {
    	User user = userDao.getUserById(userName);
    	log.info("Test at the service"+oldPassword);
    	boolean valueTest = passwordEncoder.isPasswordValid(user.getPassword(), oldPassword, saltSource.getSalt(user));
    	if(valueTest){
    		try{
    			user.setPassword(newPassword);
        		user.setConfirmPassword(newPassword);
        		User updatedUser = this.saveUser(user);
     		}catch(Exception e){
    			log.warn(e.getMessage());
    		}
    		return "true";
    	}else{
    		return "false";
    	}
    }
    
    /**
     *  To check whether the list of users can be deleted
     * @param users
     * @return boolean 
     */
    private boolean checkCanDelete(List<User> users)
    {
    	boolean isAllowed=true;
		for(User user:users){
		
		if(!user.getCanDelete())
		{
			isAllowed=false;
		}
		}
	return isAllowed;
    }
 
	/**
	 * {@inheritDoc}
	 */
	public Set<String> getUserForModulePermission(String moduleName) {

		Set<String> users = new HashSet<String>();
		List<String> departments = new ArrayList<String>();
		List<String> roles = new ArrayList<String>();
		List<String> groups = new ArrayList<String>();
		try {
			Module module = moduleService.getModuleByName(moduleName);
			Set<ModuleRolePrivilege> modulePrivileges = module.getModleRoles();
			for (ModuleRolePrivilege modulePrivilege : modulePrivileges) {
				if (modulePrivilege.getPrivilegeType().equals("edit")) {
					if (modulePrivilege.getRoleType().equals("Department")) {
						departments.add(modulePrivilege.getPrivilegeName());
					} else if (modulePrivilege.getRoleType().equals("Role")) {
						roles.add(modulePrivilege.getPrivilegeName());
					} else if (modulePrivilege.getRoleType().equals("Group")) {
						groups.add(modulePrivilege.getPrivilegeName());
					} else if (modulePrivilege.getRoleType().equals("User")) {
						users.add(modulePrivilege.getPrivilegeName());
					}
				} else {
					if (modulePrivilege.getRoleType().equals("Department")) {
						departments.add(modulePrivilege.getPrivilegeName());
					} else if (modulePrivilege.getRoleType().equals("Role")) {
						roles.add(modulePrivilege.getPrivilegeName());
					} else if (modulePrivilege.getRoleType().equals("Group")) {
						groups.add(modulePrivilege.getPrivilegeName());
					} else if (modulePrivilege.getRoleType().equals("User")) {
						users.add(modulePrivilege.getPrivilegeName());
					}
				}
			}
			if (!departments.isEmpty()) {
				List<String> departmentUser = departmentService
						.getUsersByDepartmentIds(departments);
				users.addAll(departmentUser);
			}

			if (!groups.isEmpty()) {
				List<String> groupUsers = groupService
						.getUserNamesByGroupIds(groups);
				users.addAll(groupUsers);
			}
			if (!roles.isEmpty()) {
				List<String> roleUsers = roleService
						.getUserNamesByRoleIds(roles);
				users.addAll(roleUsers);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;

	}
	
	public List<String> getUserForMenuPermission(String menuName){
		List<String> users = new ArrayList<String>();
		List<String> departments = new ArrayList<String>();
		List<String> roles = new ArrayList<String>();
		List<String> groups = new ArrayList<String>();
		try{
		Menu menu = menuService.getMenuByName(menuName);
		if(menu.getRoles() != null){
    		for(Role role : menu.getRoles()){
    		    if(role.getName().endsWith("GROUP")){
		    		Group group = groupService.getGroupIdByGroupRole(role.getName());
		    		groups.add(group.getName());
    		    } else if(role.getName().endsWith("DEPARTMENT")) {
    		    	Department department = departmentService.getDepartmentIdByDepartmentRole(role.getName());
    		    	departments.add(department.getName());
    		    } else if(role.getName().endsWith("USER") && role.getRoleType().equalsIgnoreCase("Internal")) {
    		    	String userId = getUserIdByUserRole(role.getName());
    		    	users.add(userId);
    		    } else {
    		    	roles.add(role.getName());
    		    }
    		}
    	}
		
		if (!departments.isEmpty()) {
			List<String> departmentUser = departmentService
					.getUsersByDepartmentIds(departments);
			users.addAll(departmentUser);
		}

		if (!groups.isEmpty()) {
			List<String> groupUsers = groupService
					.getUserNamesByGroupIds(groups);
			users.addAll(groupUsers);
		}
		if (!roles.isEmpty()) {
			List<String> roleUsers = roleService
					.getUserNamesByRoleIds(roles);
			users.addAll(roleUsers);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return users;
		
	}

    /**
     * {@inheritDoc}
     */
    public String deleteSelectedUsers(List<String> ids,User logInuser) throws BpmException{
	    	List<User> users = userDao.getUsersByIds(ids);
    		boolean isAllowed;
    		boolean isAdmin=false;
    		Set<Role> roles=logInuser.getRoles();
    		for(Role role:roles){
    			if(role.getId().equals("ROLE_ADMIN")){
    				isAdmin=true;
    				break;
    			}
    		}
    		
    		if(users!=null){
    			StringBuffer notDeletedUsers=new StringBuffer();
    			notDeletedUsers.append("(");
    	    	int usersCount=0;
    	    	if(!isAdmin){
    	    		for(User user:users){
        	    		if(user.getDepartment().getIsParent() || !departmentService.getIsDepartmentAdmin(logInuser,user.getDepartment())){
        	    			notDeletedUsers.append(user.getId()+",");
        	    			usersCount++;
        	    		}
    	    		}
    	    	}
    	    		
    	    	if(usersCount==0){
    	    		isAllowed=checkCanDelete(users);
    	    		for(User user:users){
    	    			if(isAllowed==true)
    	    			{
	    	    			groupService.updateGroupIncharge(user.getId(),"");
	    	    			groupService.updateGroupLeader(user.getId(),"");
	    	    			folderService.updateFolderOwner(user.getId(), "");
	    	    			departmentService.updateDepartmentOwner(user.getId(),"");
	    	    		    updateManager(user.getId(),"");
	        	    		updateSecretary(user.getId(),"");
	    	    			deleteUser(user);
    	    			}
    	    		}
    	    		return null;
    	    	}else{
    	    		if(notDeletedUsers.lastIndexOf(",")>0){
    	    			notDeletedUsers.deleteCharAt(notDeletedUsers.lastIndexOf(","));
        			}
    	    		notDeletedUsers.append(")");
    	    		return notDeletedUsers.toString();
    	    	}
    		}else{
    			throw new BpmException("error.user.delete");
    		}
    }
    
    /**
     * {@inheritDoc}
     */
    public void deleteUser(User user)throws BpmException{
    	userDao.removeUser(user);
    }
    
    /**
     * {@inheritDoc}
     */
    public void deleteInternalUserId(String username){
    	
    }
    
    /**
     * {@inheritDoc}
     */
    public void updateManager(String userId,String manager){
    	userDao.updateManager(userId, manager);
    }
    
    /**
     * {@inheritDoc}
     */
    public void updateSecretary(String userId,String secretary){
    	userDao.updateSecretary(userId, secretary);
    }
    
    /**
     * {@inheritDoc}
     */
    public String updateUserStatus(List<String> ids,boolean status,User logInuser) throws Exception{
    	List<User> users = userDao.getUsersByIds(ids);
    	try{
    			StringBuffer notDeletedUsers=new StringBuffer();
    			notDeletedUsers.append("(");
    	    	int usersCount=0;
    	    	
	    		for(User user:users){
		    		if(!departmentService.getIsDepartmentAdmin(logInuser,user.getDepartment())){
		    			notDeletedUsers.append(user.getId()+",");
		    			usersCount++;
		    		}
	    		}
	    		
	    		if(usersCount==0){
	    			userDao.updateUserStatus(ids, status);
	    			return null;
	    		}else{
	    			if(notDeletedUsers.lastIndexOf(",")>0){
    	    			notDeletedUsers.deleteCharAt(notDeletedUsers.lastIndexOf(","));
        			}
    	    		notDeletedUsers.append(")");
    	    		return notDeletedUsers.toString();
	    		}
    		
	    }catch (Exception e) {
    		throw new BpmException("error.user.status"+e.getMessage(),e);
    	}
    }
    
    /**
     * {@inheritDoc}
     *
     * @param username the login name of the human
     * @return User the populated user object
     * @throws UsernameNotFoundException thrown when username not found
     */
    public List<User> getUserByUsernames(List<String> usernames) throws UsernameNotFoundException {
        return userDao.loadUserByUsernames(usernames);
    }
    
    /**
     * {@inheritDoc}
     *
     * @param username the login name of the human
     * @return User the populated user object
     * @throws UsernameNotFoundException thrown when username not found
     */
    public List<User> getDistinctUserByUsernames(List<String> usernames) throws UsernameNotFoundException {
        return userDao.getDistinctUserByUsernames(usernames);
    }
    
    /**
     * {@inheritDoc}
     * 
     */
    public void updateUsersDepartment(Set<User> users, Department department){
    	log.info("Updating the users department");
    	try{
	    	for (User user : users) {
				user.setDepartment(department);
	    		userDao.saveUser(user);
	    	}
    	}catch (Exception e) {
    		throw new BpmException("error.userDepartment.update"+e.getMessage(),e);
    	}
    }
    
    /**
     * {@inheritDoc}
     * 
     */
    public void updateUsersPartTimeDepartment(Set<User> users, Department partTimeDepartment) {
    	log.info("Updating the users part time department");
    	for (User user : users) {
			user.setPartTimeDepartment(partTimeDepartment);
    		userDao.saveUser(user);
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    public List<User> getUserByDepartments(Department department) throws UsernameNotFoundException {
        return userDao.getUserByDepartments(department);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllUsers() {
        List<User> users = userDao.getUsers();
        List<LabelValue> list = new ArrayList<LabelValue>();

        for (User user : users) {
            list.add(new LabelValue(user.getUsername(), user.getId()));
        }

        return list;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isDublicateUser(String username){
    	return userDao.isDublicateUser(username);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isDuplicateEmail(String email,String userId){
    	return userDao.isDuplicateEmail(email,userId);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isDuplicateEmployeeNumber(String employeeNumber,String userId){
    	return userDao.isDuplicateEmployeeNumber(employeeNumber,userId);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public List<User> getUsersByDepartment(Department department){
    	log.info("Getting the department user of : "+department.getName());
        return userDao.getUsersByDepartment(department);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<User> getUsersByPartTimeDepartment(Department partTimeDepartment) {
    	log.info("Getting the part time department users of : "+partTimeDepartment.getName());
        return userDao.getUsersByPartTimeDepartment(partTimeDepartment);
    }
    
    public boolean isUserAdmin(User user) {
        Set<Role>roles = user.getRoles();
        List<Role> roleList = new ArrayList<Role>(roles);
        for (Role role : roleList) {
			if(role.getId().equals("ROLE_ADMIN")){
				return true;
			}
		}
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    public void removeUsers(List<User> users){
    	log.debug("removing users");
    	try{
	        for(User user:users){
	        	userDao.removeUser(user);
	        }
    	}catch (Exception e) {
			throw new BpmException("error.user.delete"+e.getMessage(),e);
		}
    }

    public List<UserDTO> getAllUsersDTO(String name){
    	return userDao.getAllUsers(name);
    }
    
    public List<UserDTO> getAllUsersByDepartment(String departmentId){
    	return userDao.getAllUsersByDepartment(departmentId);
    }
    
    public List<UserDTO> getAllUsersByRole(String roleId){
    	return userDao.getAllUsersByRole(roleId);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getAllUsersByRolePermission(String roleId){
    	return roleService.getUserNamesByRoleId(roleId);
    }
    public List<UserDTO> getAllUsersByGroup(String groupId){
    	return userDao.getAllUsersByGroup(groupId);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getRoleUsersAsLabelValue(String roleId) throws BpmException{
		return userDao.getRoleUsersAsLabelValue(roleId);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getManagerRoleUsersAsLabelValue(String roleId,String currentUser) throws BpmException{
    	String subordinateIdStr = getAllRelativeIds(currentUser,true);
    	List<String> subordinateIds = new ArrayList<String>();
    	if(subordinateIdStr!= null){
		   for(String subordinateId:subordinateIdStr.split(",")){
			   subordinateIds.add(subordinateId);
		   }
    	}
    	subordinateIds.add(currentUser);
	    return userDao.getManagerRoleUsersAsLabelValue(roleId, subordinateIds);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getGroupUsersAsLabelValue(String groupId) throws BpmException{
		return userDao.getGroupUsersAsLabelValue(groupId);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getManagerGroupUsersAsLabelValue(String groupId, String currentUser) throws BpmException{
    	String subordinateIdStr = getAllRelativeIds(currentUser,true);
    	List<String> subordinateIds = new ArrayList<String>();
    	if(subordinateIdStr!= null){
		   for(String subordinateId:subordinateIdStr.split(",")){
			   subordinateIds.add(subordinateId);
		   }
    	}
    	subordinateIds.add(currentUser);
		return userDao.getManagerGroupUsersAsLabelValue(groupId,subordinateIds);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getDepartmentUsersAsLabelValue(String departmentId) throws BpmException{
		return userDao.getDepartmentUsersAsLabelValue(departmentId);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getManagerDepartmentUsersAsLabelValue(String departmentId, String currentUser) throws BpmException{
    	String subordinateIdStr = getAllRelativeIds(currentUser,true);
    	List<String> subordinateIds = new ArrayList<String>();
    	if(subordinateIdStr!= null){
		   for(String subordinateId:subordinateIdStr.split(",")){
			   subordinateIds.add(subordinateId);
		   }
    	}
    	subordinateIds.add(currentUser);
    	return userDao.getManagerDepartmentUsersAsLabelValue(departmentId,subordinateIds);
    }

	/**
     * {@inheritDoc}
     */
    public User getUserByEmail(String email){
    	return userDao.getUserByEmail(email);
    }
    
    public List<String> getExistUserIds(List<String> ids) throws BpmException{
    	List<User> users = userDao.getUsersByIds(ids);
    	List<String> userIds = new ArrayList<String>();
    	if(users != null && !users.isEmpty()){
    		for(User user : users){
    			userIds.add(user.getId());
    		}
    	}
    	
    		return userIds; 
    }
    
    /**
     * {@inheritDoc}
     */
    public User getUserById(String id){
    	return userDao.getUserById(id);
    }

    /**
     * @return 
     * 
     */
    public List<Object[]> getUserName(String userName) {
    	return userDao.getUserName(userName);
    }
    
    public List<LabelValue> getUserLabelValueByIds(List<String> ids) throws Exception{
    	List<User> users = userDao.getUsersByIds(ids);
    	List<LabelValue> userLabelValues = new ArrayList<LabelValue>();
    	if(users.size() > 0){
    		for(User user : users){
    			LabelValue userLabelValue = new LabelValue();
    			userLabelValue.setLabel(user.getFullName());
    			userLabelValue.setValue(user.getId());
    			userLabelValues.add(userLabelValue);
    		}
    	}
    	return userLabelValues; 
    }
    
	public String getUserIdByUserRole(String userRole) {
		return userDao.getUserIdByUserRole(userRole);  
	}

	public String getUserRoleByUserId(String userId) {
		return userDao.getUserRoleByUserId(userId);  
	}

    
    /**
     * {@inheritDoc}
     */
    public List<User> getUserByRoleIds(List<String> roleIds) throws BpmException{
    	return userDao.getUsersByRoleIds(roleIds);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<User> geUserByGroupIds(List<String> groupIds) throws BpmException{
    	return userDao.getUsersByGroupIds(groupIds);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<User> getUserByUserIds(List<String> userIds) throws BpmException{
    	return userDao.getUsersByIds(userIds);
    }
    /**
     * {@inheritDoc}
     */
    public List<User> getUsersByDepartmentIds(List<String> ids) throws EazyBpmException{
    	return userDao.getUsersByDepartmentIds(ids);
    }
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getUserNameAndPosition(String userName,String currentUser,String appendTo) throws EazyBpmException{
    	if(StringUtil.isEmptyString(currentUser)){
    		return userDao.getUserNameAndPosition(userName);
    	}else if(appendTo.equalsIgnoreCase("manager") || appendTo.equalsIgnoreCase("secretary") ){
    		 List<LabelValue> userDetails = new ArrayList<LabelValue>();
    		 LabelValue userLabelAndValue = null;
    		 List<Object[]> objects = userDao.getManagerNameAndPosition(userName, currentUser);
    		 for(Object[] object:objects){
    			 userLabelAndValue = new LabelValue();
    			 if(!String.valueOf(object[0]).equalsIgnoreCase(currentUser)){
    					 userLabelAndValue.setValue(String.valueOf(object[0]));
            			 userLabelAndValue.setLabel(String.valueOf(object[1]));
            			 userDetails.add(userLabelAndValue);
    			 }
    		 }
    		 return userDetails;
    	}
    	return null;
    }
    /**
     * {@inheritDoc}
     */
    public List<String> getUsersSuperiorByIds(List<String> ids)throws EazyBpmException{
    	return userDao.getUsersSuperiorByIds(ids);
    }
    /**
     * {@inheritDoc}
     */
    public List<String> getUsersSubordinateByIds(List<String> ids) throws EazyBpmException{
    	return userDao.getUsersSubordinateByIds(ids);
    }
    /**
     * {@inheritDoc}
     */
    public List<String> getPeopleInSameDepartment(List<String> ids) throws EazyBpmException{
    	return userDao.getPeopleInSameDepartment(ids);
    }
    /**
     * {@inheritDoc}
     */
    public List<String> getOwnerOfTheDepartment(List<String> ids) throws EazyBpmException{
    	return userDao.getOwnerOfTheDepartment(ids);
    }
    /**
     * {@inheritDoc}
     */
    public List<User> getDepartmentInterfaceUser(List<String> ids) throws EazyBpmException{
    	return userDao.getDepartmentInterfaceUser(ids);
    }
    /**
     * {@inheritDoc}
     */
    public List<String> getParentDepartmentUser(List<String> ids) throws EazyBpmException{
    	return userDao.getParentDepartmentUser(ids);
    }
    /**
     * {@inheritDoc}
     */
    public List<String> getParentDepartmentWithSubDepartmentUser(List<String> ids) throws EazyBpmException{
    	return userDao.getParentDepartmentUser(ids);
    }
    
    /**
     * {@inheritDoc}
     */
    public String getAllRelativeIds(String id, boolean isChild) throws EazyBpmException{
    	return userDao.getAllRelativeIds(id, isChild);
    }
    
	/**
     * {@inheritDoc}
     */
	public boolean getEmailNotificationEnabled(String userId) throws EazyBpmException{
		return userDao.getEmailNotificationEnabled(userId);
	}
	
	public List<String> getSecretaryToLeader(List<String> ids) throws EazyBpmException{
		return userDao.getSecretaryToLeader(ids);
	}
	
	public Department getParentDepartmentForUser(List<String> ids) throws EazyBpmException{
		return userDao.getParentDepartmentForUser(ids);
	}
	public List<String> getUserIdsByDepartmentIds(List<String> ids) throws EazyBpmException{
		return userDao.getUserIdsByDepartmentIds(ids);
	}
	
	public List<String> getLeaderToSecretaryIds(List<String> ids) throws EazyBpmException{
		return userDao.getLeaderToSecretaryIds(ids);
	}

	/**
     * {@inheritDoc}
     */
	public List<String> getUserAdminDepartments(User user)throws EazyBpmException{
		user=userDao.get(user.getId());
		List<String> userAdminDep=new ArrayList<String>();
		
		//Check is System admin
		if(CommonUtil.isUserAdmin(user)){
			userAdminDep=null;
		}else{
			Set<Role> roles=user.getRoles();
			for(Role s : roles){
				if(s.getId().equals("ROLE_DEPT_ADMIN")){
					userAdminDep=null;
					break;
				}
			}
			if(userAdminDep!=null){
				Set<Department> departmentObjs=user.getDepartmentByAdministrators();
				//Check is Department admin
				if(!departmentObjs.isEmpty()){
					List<String> depIds=new ArrayList<String>();
					for(Department depObj:departmentObjs){
						depIds.add(depObj.getId());
					}
					
					//Get is sub department's of Department admin
					List<String> subDepartMentIds=departmentService.getAdminDepartments(depIds);
					if(subDepartMentIds!=null && !subDepartMentIds.isEmpty()){
						depIds.addAll(subDepartMentIds);
					}
					userAdminDep.addAll(depIds);
				}else{
					userAdminDep.add("empty");
				}
			}
			
		}
		return userAdminDep;
	}
	
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getDepartmentAdminRoleUsersAsLabelValue(String roleId,String userId) throws BpmException{
    	Set<String> departmentIdsSet = new HashSet<String>();
    	departmentIdsSet.addAll(departmentService.getDepartmentAdminDepartmentIds(userId));
		return userDao.getAdminDepartmentRoleUsersAsLabelValue(roleId,departmentIdsSet);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getDepartmentAdminGroupUsersAsLabelValue(String groupId,String userId) throws BpmException{
    	Set<String> departmentIdsSet = new HashSet<String>();
    	departmentIdsSet.addAll(departmentService.getDepartmentAdminDepartmentIds(userId));
		return userDao.getDepartmentAdminGroupUsersAsLabelValue(groupId,departmentIdsSet);
    }
    
    /**
     * {@inheritDoc}
     */
	public List<String> getUserAdminGroups(User user)throws EazyBpmException{
		user=userDao.get(user.getId());
		
		List<String> userAdminGroups=new ArrayList<String>();
		
		//Check is System admin
		if(CommonUtil.isUserAdmin(user)){
			userAdminGroups=null;
		}else{
			Set<Group> groupObjs=user.getGroupByAdministrators();
			//Check is Group admin
			if(!groupObjs.isEmpty()){
				List<String> groupIds=new ArrayList<String>();
				for(Group groupObj:groupObjs){
					groupIds.add(groupObj.getId());
				}
				
				//Get is sub group's of group admin
				List<String> subDepartMentIds=groupService.getAdminGroups(groupIds);
				if(subDepartMentIds!=null && !subDepartMentIds.isEmpty()){
					groupIds.addAll(subDepartMentIds);
				}
				userAdminGroups.addAll(groupIds);
			}else{
				userAdminGroups.add("empty");
			}
		}
		return userAdminGroups;
	}
	
	
	public List<String> getUserAdminRoles(User user)throws EazyBpmException{
	user=userDao.get(user.getId());
			
			List<String> userAdminRoles=new ArrayList<String>();
			
			//Check is System admin
			if(CommonUtil.isUserAdmin(user)){
				userAdminRoles=null;
			}else{
				List<String> userAdminDep=new ArrayList<String>();
				Set<Department> departmentObjs=user.getDepartmentByAdministrators();
				//Check is Department admin
				if(!departmentObjs.isEmpty()){
					List<String> depIds=new ArrayList<String>();
					for(Department depObj:departmentObjs){
						depIds.add(depObj.getId());
					}
					
					//Get is sub department's of Department admin
					List<String> subDepartMentIds=departmentService.getAdminDepartments(depIds);
					if(subDepartMentIds!=null && !subDepartMentIds.isEmpty()){
						depIds.addAll(subDepartMentIds);
					}
					userAdminDep.addAll(depIds);
					userAdminRoles=roleService.getUserAdminDepartmentRoles(userAdminDep);
				}else{
					userAdminRoles.add("empty");
				}
			}
			return userAdminRoles;
	}
    
	public List<UserDTO> getAllUsersById(List<String> userIds) {
		return userDao.getAllUsersById(userIds);
	}
	public List<String> getUserFullNamesByUsernames(List<String> usernames){
		return userDao.getUserFullNamesByUsernames(usernames);
	}
	
	public String getUserFullNamesByUsername(String username){
		return userDao.getUserFullNamesByUsername(username);
	}
	
	public  String getUserMailIdByUser(String userId) throws EazyBpmException{
		return userDao.getUserMailIdByUser(userId);
	}
	
	/**
     * {@inheritDoc}
     */
	public List<String> getUsersEmailIdByRole(String roleId) throws EazyBpmException{
		return userDao.getUsersEmailIdByRole(roleId);
	}
	
	/**
     * {@inheritDoc}
     */
	public List<String> getUsersEmailIdByGroup(String groupId) throws EazyBpmException{
		return userDao.getUsersEmailIdByGroup(groupId);
	}
	
	/**
     * {@inheritDoc}
     */
	public List<String> getUsersEmailIdByDepartment(String departmentId) throws EazyBpmException{
		return userDao.getUsersEmailIdByDepartment(departmentId);
	}
	
	/**
     * {@inheritDoc}
     */
	public List<String> getUsersMobileNoByRole(String roleId) throws EazyBpmException{
		return userDao.getUsersMobileNoByRole(roleId);
	}

	/**
     * {@inheritDoc}
     */
	public List<String> getUsersMobileNoByGroup(String groupId) throws EazyBpmException{
		return userDao.getUsersMobileNoByGroup(groupId);
	}

	/**
     * {@inheritDoc}
     */
	public List<String> getUsersMobileNoByDepartment(String departmentId) throws EazyBpmException{
		return userDao.getUsersMobileNoByDepartment(departmentId);
	}
   
	/**
     * {@inheritDoc}
     */
	public List<String> getUsersMobileNoByUser(String userId) throws EazyBpmException{
		return userDao.getUsersMobileNoByUser(userId);
	}
	 
	/**
     * {@inheritDoc}
     */
	public List<String> getUsersInternalMessageIdByRole(String roleId, String domainName) throws EazyBpmException{
		return userDao.getUsersInternalMessageIdByRole(roleId, domainName);
	}
   
	/**
     * {@inheritDoc}
     */
	public List<String> getUsersInternalMessageIdByGroup(String groupId, String domainName) throws EazyBpmException{
		return userDao.getUsersInternalMessageIdByGroup(groupId, domainName);
	}
   
	/**
     * {@inheritDoc}
     */
	public List<String> getUsersInternalMessageIdByDepartment(String departmentId, String domainName) throws EazyBpmException{
		return userDao.getUsersInternalMessageIdByDepartment(departmentId, domainName);
	}
	
	public User getUserByToken(String token){
		return userDao.getUserByToken(token);
	}
	
	public User updateUser(User user){
		Updater<User> updater = new Updater<User>(user);
		return dao.updateByUpdater(updater);
	}

}
