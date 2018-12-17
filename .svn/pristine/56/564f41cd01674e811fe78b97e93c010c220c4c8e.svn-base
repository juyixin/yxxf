package com.eazytec.dao.hibernate;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.eazytec.dao.UserDao;
import com.eazytec.dto.UserDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Role;
import com.eazytec.model.Schedule;
import com.eazytec.model.User;

/**
 * This class interacts with Hibernate session to save/delete,
 * retrieve User objects.
 *
 * @author madan
 * author mathi
*/
@Repository("userDao")
public class UserDaoHibernate extends GenericDaoHibernate<User, String> implements UserDao, UserDetailsService {

    /**
     * Constructor that sets the entity to User.class.
     */
    public UserDaoHibernate() {
        super(User.class);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        Query qry = getSession().createQuery("from User u order by upper(u.username)");
        return qry.list();
    }

    /**
     * {@inheritDoc}
     */
    public User saveUser(User user) {
        if (log.isDebugEnabled()) {
            log.debug("user's id: " + user.getId());
        }
        getSession().saveOrUpdate(user);
        // necessary to throw a DataIntegrityViolation and catch it in UserManager
        getSession().flush();
        return user;
    }

    /**
     * Overridden to call the saveUser method since saveUser flushes 
     * the session and saveObject of BaseDaoHibernate does not.
     *
     * @param user the user to save
     * @return the modified user (with a primary key set if they're new)
     */
    @Override
    public User save(User user) {
        return this.saveUser(user);
    }

    /**
     * {@inheritDoc}
    */
    public void createInternalMessageId(String username, String passwordHashAlgorithm, String password, int version){
    	Query query = getSession().createSQLQuery("INSERT INTO JAMES_USER (USER_NAME, PASSWORD_HASH_ALGORITHM, PASSWORD, version) VALUES ('"+username+"', '"+passwordHashAlgorithm+"','"+password+"', "+version+")");
       	query.executeUpdate();
    }
    
    /**
     * {@inheritDoc}
     */
    public String getInternalMessageId(String username){
    	/*List<String> users = getSession().createSQLQuery("select USR.USER_NAME FROM JAMES_USER USR WHERE USR.USER_NAME = '"+username+"'").list();
    	if(users.isEmpty()){
    		return null;
    	} else {
    		return users.get(0);
    	}*/
		SQLQuery stringQuery = getSession().createSQLQuery("select USR.USER_NAME FROM JAMES_USER USR WHERE USR.USER_NAME = '"+username+"'");
			stringQuery.addScalar("USER_NAME",StringType.INSTANCE);
		List<String> users = stringQuery.list();
		if(users.isEmpty()){
			return null;
		} else {
			return users.get(0);
		}
    }
    
    /**
     * {@inheritDoc}
    */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	User user = null;
        List users = getSession().createCriteria(User.class).add(Restrictions.eq("username", username)).list();
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("user '" + username + "' not found...");
        } else {
        	
        	 List roles = getSession().createCriteria(Role.class).add(Restrictions.eq("name", "ROLE_DEFAULT")).list();
             if (!roles.isEmpty()) {
            	 Role role=(Role) roles.get(0);
                 user = (User) users.get(0);
                 user.addRole(role);
             }
            return (UserDetails) user;
        }
    }

    /**
     * {@inheritDoc}
    */
    public String getUserPassword(String userId) {
    /*	 List<User> users = getSession().createCriteria(User.class).add(Restrictions.eq("id", userId)).list();
         if (users == null || users.isEmpty()) {
             throw new UsernameNotFoundException("user '" + userId + "' not found...");
         } else {
             return (String) users.get(0).getPassword();
         }*/
       JdbcTemplate jdbcTemplate =
                new JdbcTemplate(SessionFactoryUtils.getDataSource(getSessionFactory()));
      /// jdbcTemplate.execute("select password from ETEC_USER where id='"+userId+"'");
        //Table table = AnnotationUtils.findAnnotation(User.class, Table.class);
        return jdbcTemplate.queryForObject(
                "select password from ETEC_USER where id='"+userId+"'", String.class);
    }
    
   /**
     * {@inheritDoc}
    */
    public String getUserEmailPassword(String userId) {
       JdbcTemplate jdbcTemplate =
                new JdbcTemplate(SessionFactoryUtils.getDataSource(getSessionFactory()));

       //Table table = AnnotationUtils.findAnnotation(User.class, Table.class);
        return jdbcTemplate.queryForObject(
                "select email_password from ETEC_USER where id='"+userId+"'", String.class);
    }

    /**
     * {@inheritDoc}
     */
    public List<User> getUsersByIds(List<String> ids) {
    	List<User> user = getSession().createQuery("from User as user where user.id in (:list)").setParameterList("list", ids).list();
        if (user.isEmpty()) {
            return null;
        } else {
            return user;
        }
    }
   
    /**
     * {@inheritDoc}
     */
    public void removeUser(User user) {
    	Session session = getSession();
    	Set<Group> groupList= user.getGroupByAdministrators();
    	for (Group group : groupList) {
			group.getAdministrators().remove(user);
			session.saveOrUpdate(group);
		}
    	groupList.clear();
    	groupList= user.getGroupByInterfacePeople();
    	for (Group group : groupList) {
			group.getInterfacePeople().remove(user);
			session.saveOrUpdate(group);
		}
    	Set<Department> departmentList= user.getDepartmentByAdministrators();
    	for (Department department : departmentList) {
			department.getAdministrators().remove(user);
			session.saveOrUpdate(department);
		}
    	departmentList.clear();
    	departmentList= user.getDepartmentByInterfacePeople();
    	for (Department department : departmentList) {
			department.getInterfacePeople().remove(user);
			session.saveOrUpdate(department);
		}
    	
    	Set<Schedule> events = user.getEventUsers();
    	for (Schedule event : events) {
    		event.getUsers().remove(user);
			session.saveOrUpdate(event);
		}
    	session.delete(user);
    }
    
    /**
     * {@inheritDoc}
     */
    public void updateUserStatus(List<String> ids,boolean status) {
    	Query query = getSession().createQuery("update User as user set user.enabled=" +status+ " where user.id in (:list)").setParameterList("list", ids);
        query.executeUpdate();
    }
    
    /**
     * {@inheritDoc}
    */
    public List<User> loadUserByUsernames(List<String> usernames) throws UsernameNotFoundException {
    	//List<Role> roles = getSession().createQuery("from Role as role where role.name in (:list)").setParameterList("list", names).list();
        List users = getSession().createCriteria(User.class).add(Restrictions.in("username", usernames)).list();
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("user '" + usernames + "' not found...");
        } else {
            return users;
        }
    }
    
    /**
     * {@inheritDoc}
    */
    public List<User> getDistinctUserByUsernames(List<String> usernames) throws UsernameNotFoundException {
        List<User> users = getSession().createCriteria(User.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).add(Restrictions.in("username", usernames)).list();
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("user '" + usernames + "' not found...");
        } else {
            return users;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public List<User> getUserByDepartments(Department department) {
    	List<User> users = getSession().createCriteria(User.class)
    	.add( Restrictions.or(
        Restrictions.eq( "department", department ),
        Restrictions.eq( "partTimeDepartment", department ))).list();
		if (users.isEmpty()) {
			return null;
		} else {
			return users;
		}
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isDublicateUser(String username){
    	List<User> users = getSession().createCriteria(User.class)
    	.add( Restrictions.or(
        Restrictions.eq( "username", username ))).list();
		if (users.isEmpty()) {
			return false;
		} else {
			return true;
		}
    }
    
    public boolean isDuplicateEmail(String email,String userId){
    	List<User> users = getSession().createCriteria(User.class).add(Restrictions.eq( "email", email )).list();
    	boolean isDuplicate = false ;
	    if(users.isEmpty()){
			isDuplicate = false;
	    }else{
	    	User user= users.get(0);
			if (userId != null && !(userId.isEmpty()) && userId.equals(user.getId())) {
					isDuplicate = false;
				} else {
					isDuplicate = true;
				}
			}
	     return isDuplicate;
	    }
    
    public boolean isDuplicateEmployeeNumber(String employeeNumber,String userId){
    	
    	List<User> users = getSession().createCriteria(User.class)
    	.add( Restrictions.and( Restrictions.eq( "employeeNumber", employeeNumber ),Restrictions.ne("id", userId))).list();
		if (users.isEmpty()) {
			return false;
		} else {
			return true;
		}
    }
    
    /**
     * {@inheritDoc}
     */
    public List<User> getUsersByDepartment(Department department) {
    	List<User> users = getSession().createCriteria(User.class)
    	.add(Restrictions.eq( "department", department )).list();
		if (users.isEmpty()) {
			return null;
		} else {
			return users;
		}
    }
    
    /**
     * {@inheritDoc}
     */
    public List<User> getUsersByPartTimeDepartment(Department partTimeDepartment) {
    	List<User> users = getSession().createCriteria(User.class)
    	.add(Restrictions.eq( "partTimeDepartment", partTimeDepartment )).list();
		if (users.isEmpty()) {
			return null;
		} else {
			return users;
		}
    }
    
    /**
     * {@inheritDoc}
     */
    public void updateManager(String userId,String manager){
    	Query query = getSession().createQuery("UPDATE User set manager = '"+manager+"'WHERE manager='"+userId+"'");
    	query.executeUpdate();
    }
    
   /**
    * {@inheritDoc}
    */
   public void updateSecretary(String userId,String secretary){
   	Query query = getSession().createQuery("UPDATE User set secretary = '"+secretary+"'WHERE secretary='"+userId+"'");
   	query.executeUpdate();
   }
   
   /**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<UserDTO> getAllUsers(String name) {
		
		StringBuilder sb = new StringBuilder("select new com.eazytec.dto.UserDTO(user.id as id,user.username as username,user.firstName as firstName,user.lastName as lastName,department.viewName as department,user.email as email,user.employeeNumber as employeeNumber,user.enabled as enabled) from User as user left join user.department as department where 1=1");
		
		if(StringUtils.isNotBlank(name)){
			sb.append(" and user.username like '%" + name + "%' or user.fullName like '%" + name + "%'");
		}
		
		List<UserDTO> user = (List<UserDTO>) getSession().createQuery(sb.toString()).list();
		return user;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<UserDTO> getAllUsersByDepartment(String departmentId) {
		List<UserDTO> user = (List<UserDTO>) getSession()
				.createQuery(
						"select new com.eazytec.dto.UserDTO(user.id as id,user.username as username,user.firstName as firstName,user.lastName as lastName,department.viewName as department,user.email as email,user.employeeNumber as employeeNumber,user.enabled as enabled) from User as user left join user.department as department where department.name='"
								+ departmentId + "'").list();
		return user;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<UserDTO> getAllUsersByRole(String roleId) {
		List<UserDTO> user = (List<UserDTO>) getSession()
				.createQuery(
						"select new com.eazytec.dto.UserDTO(user.id as id,user.username as username,user.firstName as firstName,user.lastName as lastName,department.viewName as department,user.email as email,user.employeeNumber as employeeNumber,user.enabled as enabled) from User as user left join user.roles as role left join user.department as department where role.id = '"+roleId+"'").list();
		return user;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<UserDTO> getAllUsersByGroup(String groupId) {
		List<UserDTO> user = (List<UserDTO>) getSession()
				.createQuery(
						"select new com.eazytec.dto.UserDTO(user.id as id,user.username as username,user.firstName as firstName,user.lastName as lastName,department.viewName as department,user.email as email,user.employeeNumber as employeeNumber,user.enabled as enabled) from User as user left join user.groups as group left join user.department as department where group.id = '"+groupId+"'").list();
		return user;
	}
	
	/**
     * {@inheritDoc}
     */
    public List<LabelValue> getRoleUsersAsLabelValue(String roleId) throws BpmException{
    	List<LabelValue> userList = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(concat(user.firstName ,' ', user.lastName ) as name,user.id as id) from User as user join user.roles as role where role.id = '"+roleId+"'").list();
		return userList;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getManagerRoleUsersAsLabelValue(String roleId, List<String> subordinateIds) throws BpmException{
    	List<LabelValue> userList = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(concat(user.firstName ,' ', user.lastName ) as name,user.id " +
    			"as id) from User as user join user.roles as role where role.id = '"+roleId+"' AND user.id not in(:list))").setParameterList("list", subordinateIds).list();
		return userList;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getGroupUsersAsLabelValue(String groupId) throws BpmException{
    	List<LabelValue> userList = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(concat(user.firstName ,' ', user.lastName ) as name,user.id as id) from User as user join user.groups as group where group.id = '"+groupId+"'").list();
		return userList;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getManagerGroupUsersAsLabelValue(String groupId, List<String> subordinateIds) throws BpmException{
    	List<LabelValue> userList = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(concat(user.firstName ,' ', user.lastName ) as name,user.id " +
    			"as id) from User as user join user.groups as group where group.id = '"+groupId+"' AND user.id not in(:list))").setParameterList("list", subordinateIds).list();
		return userList;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getDepartmentUsersAsLabelValue(String departmentId) throws BpmException{
    	List<LabelValue> userList = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(concat(user.firstName ,' ', user.lastName ) as name,user.id as id) from User as user where user.department.id = '"+departmentId+"'").list();
		return userList;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getManagerDepartmentUsersAsLabelValue(String departmentId, List<String> subordinateIds) throws BpmException{
    	List<LabelValue> userList = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(concat(user.firstName ,' ', user.lastName ) as name,user.id " +
    			"as id) from User as user where user.department.id = '"+departmentId+"' AND user.id not in(:list))").setParameterList("list", subordinateIds).list();
		return userList;
    }
    
	/**
     * {@inheritDoc}
     */
    public User getUserByEmail(String email) {
    	List<User> users = getSession().createCriteria(User.class)
    	.add(Restrictions.eq( "email", email)).list();
		if (users.isEmpty()) {
			return null;
		} else {
			return users.get(0);
		}
    }
    
    /**
     * {@inheritDoc}
     */
    public User getUserById(String id) {
    	List<User> users = getSession().createCriteria(User.class)
    	.add(Restrictions.eq( "id", id)).list();
		if (users.isEmpty()) {
			return null;
		} else {
			User user=users.get(0);
			List roles = getSession().createCriteria(Role.class).add(Restrictions.eq("name", "ROLE_DEFAULT")).list();
            if (!roles.isEmpty()) {
           	 Role role=(Role) roles.get(0);
                user = (User) users.get(0);
                user.addRole(role);
            }
			return user;
		}
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Object[]> getUserName(String userName) {
		List<Object[]> user = (List<Object[]>) getSession().createQuery("select user.id as id,concat(user.firstName,' ',user.lastName) as name from User as user where user.username='"+userName+"'").list();
		return user;
    }    
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
   	public List<User> getUsersByRoleIds(List<String> ids) throws EazyBpmException{
       	List<User> userList = (List<User>) getSession().createQuery("select role.users from Role as role where role.id in (:list)").setParameterList("list", ids).list();
       	if(userList.isEmpty()){
       		return null;
       	}else{
       		return userList;
       	}
       }
    
    @SuppressWarnings("unchecked")
   	public List<User> getUsersByDepartmentIds(List<String> ids) throws EazyBpmException{
       	List<User> userList = (List<User>) getSession().createQuery("select user from User as user where user.department.id in (:list)").setParameterList("list", ids).list();
       	if(userList.isEmpty()){
       		return null;
       	}else{
       		return userList;
       	}
       }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
  	public List<User> getUsersByGroupIds(List<String> ids) throws EazyBpmException{
      	List<User> userList = (List<User>) getSession().createQuery("select group.users from Group as group where group.id in (:list)").setParameterList("list", ids).list();
      	if(userList.isEmpty()){
      		return null;
      	}else{
      		return userList;
      	}
      }
    
    /**
     * {@inheritDoc}
     */
   /* @SuppressWarnings("unchecked")
  	public List<LabelValue> getUserNameAndPosition(String userName) throws EazyBpmException{
    	List<LabelValue> userList = null;
    	if(userName.contains(" ")){
    		String userNames[] = userName.split(" ");
    		userList = (List<LabelValue>) getSession().createQuery("SELECT new com.eazytec.model.LabelValue((CASE WHEN users.position IS NULL OR users.position=''  THEN  CONCAT(users.firstName , '' , users.lastName) ELSE CONCAT(users.firstName , ' ' , users.lastName , '(',users.position,')') END)  AS userName,users.id AS id) "+ 
					" FROM "+ 
					" User AS users WHERE users.firstName LIKE '"+userNames[0]+"%' OR users.lastName LIKE '"+userNames[1]+"%'").list();
    	} else {
    		userList = (List<LabelValue>) getSession().createQuery("SELECT new com.eazytec.model.LabelValue((CASE WHEN users.position IS NULL OR users.position=''  THEN  CONCAT(users.firstName , '' , users.lastName) ELSE CONCAT(users.firstName , ' ' , users.lastName , '(',users.position,')') END)  AS userName,users.id AS id) "+ 
					" FROM "+ 
					" User AS users WHERE users.firstName LIKE '"+userName+"%' OR users.lastName LIKE '"+userName+"%' ").list();
    	}
    	
		return userList;
    }
    */
    
    @SuppressWarnings("unchecked")
  	public List<LabelValue> getUserNameAndPosition(String userName) throws EazyBpmException{
    	List<LabelValue> userList = null;
    	if(userName.contains(" ")){
    		String userNames[] = userName.split(" ");
    		userList = (List<LabelValue>) getSession().createQuery("SELECT new com.eazytec.model.LabelValue((CASE WHEN users.position IS NULL OR users.position=''  THEN  CONCAT(users.firstName , ' ' , users.lastName) ELSE CONCAT(users.firstName , ' ' , users.lastName , '(',users.position,')') END)  AS userName,users.id AS id) "+ 
					" FROM "+ 
					" User AS users WHERE users.firstName LIKE '%"+userNames[0]+"%'").list();
    	} else {
    		userList = (List<LabelValue>) getSession().createQuery("SELECT new com.eazytec.model.LabelValue((CASE WHEN users.position IS NULL OR users.position=''  THEN  CONCAT(users.firstName , ' ' , users.lastName) ELSE CONCAT(users.firstName , ' ' , users.lastName , '(',users.position,')') END)  AS userName,users.id AS id) "+ 
					" FROM "+ 
					" User AS users WHERE users.firstName LIKE '%"+userName+"%'").list();
    	}
    	
		return userList;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
  	public  List<Object[]> getManagerNameAndPosition(String userName,String currentUser) throws EazyBpmException{
//    	List<LabelValue> userList = (List<LabelValue>) getSession().createQuery("SELECT new com.eazytec.model.LabelValue((CASE WHEN users.position IS NULL OR users.position=''  THEN  CONCAT(users.firstName , ' ' , users.lastName) ELSE CONCAT(users.firstName , ' ' , users.lastName , '(',users.position,')') END)  AS userName,users.id AS id) "+ 
//    																	" FROM "+ 
//    																	" User AS users WHERE users.id not in (:list) AND (users.firstName LIKE '"+userName+"%' OR users.lastName LIKE '"+userName+"%')").setParameterList("list", subordinateIds).list();
//		return userList;
		 List<Object[]> userIds = getSession().createSQLQuery("CALL AvoidRelations(:currentUser,:userName,TRUE)")
		 .setParameter("currentUser", currentUser)
		 .setParameter("userName", userName).list();
		 return userIds;
    }
    
    /**
     * {@inheritDoc}
     */
    
	public String getUserIdByUserRole(String userRole) throws EazyBpmException{
		List userId = getSession().createQuery("select user.id from User as user where user.userRole='"+userRole+"'").list();
		if(!userId.isEmpty()) {
			return (String) userId.get(0);
		} else {
			return null;
		}		
	}

	public String getUserRoleByUserId(String userId) {
		List userRole = getSession().createQuery("select user.userRole from User as user where user.id='"+userId+"'").list();
		if(!userRole.isEmpty()) {
			return (String) userRole.get(0);
		} else {
			return null;
		}

	}

	 /**
     * {@inheritDoc}
     */
    public List<String> getUsersSuperiorByIds(List<String> ids) throws EazyBpmException {
    	List<String> user = getSession().createQuery("select user.manager from User as user where user.id in (:list)").setParameterList("list", ids).list();
        if (user.isEmpty()) {
            return null;
        } else {
            return user;
        }
    } 
    
    /**
     * {@inheritDoc}
     */
    public List<String> getUsersSubordinateByIds(List<String> ids) throws EazyBpmException {
    	List<String> user = getSession().createQuery("select user.id from User as user where user.manager in (:list)").setParameterList("list", ids).list();
        if (user.isEmpty()) {
            return null;
        } else {
            return user;
        }
    } 
    
    public List<String> getLeaderToSecretaryIds(List<String> ids) throws EazyBpmException {
    	List<String> user = getSession().createQuery("select user.secretary from User as user where user.id in (:list)").setParameterList("list", ids).list();
        if (user.isEmpty()) {
            return null;
        } else {
            return user;
        }
    } 
    /**
     * {@inheritDoc}
     */
    public List<String> getPeopleInSameDepartment(List<String> ids) throws EazyBpmException{                            
    	
    	List<String> user = getSession().createQuery("select user.id from User as user where user.department in (select user1.department from User as user1 where user1.id in(:list))").setParameterList("list", ids).list();
    	 if (user.isEmpty()) {
             return null;
         } else {
             return user;
         }
    }
    
    /**
     * {@inheritDoc}
     */
	 public List<String> getOwnerOfTheDepartment(List<String> ids) throws EazyBpmException{                            
	    	
	    	List<String> user = getSession().createQuery("select department.departmentOwner from Department as department where department.id in (select user1.department from User as user1 where user1.id in(:list))").setParameterList("list", ids).list();
	    	 if (user.isEmpty()) {
	             return null;
	         } else {
	             return user;
	         }
	    }
	 
	 public List<User> getDepartmentInterfaceUser(List<String> ids) throws EazyBpmException{
		 List<User> user = getSession().createQuery("select department.interfacePeople from  Department as department where department.id in (select user1.department from User as user1 where user1.id in(:list))").setParameterList("list", ids).list();
		 if (user.isEmpty()) {
	            return null;
	        } else {
	            return user;
	        }
	 }
	 
	 public List<String> getParentDepartmentUser(List<String> ids) throws EazyBpmException{
		 List<String> user = getSession().createQuery("select user.id from User as user where user.department in (select user1.department.superDepartment from User as user1 where user1.id in(:list))").setParameterList("list", ids).list();
		 if (user.isEmpty()) {
	            return null;
	        } else {
	            return user;
	        }
	 }
	 
	 public List<String> getParentDepartmentWithSubDepartmentUser(List<String> ids) throws EazyBpmException{
		 List<String> user = getSession().createQuery("select user.id from User as user where user.department in (select user1.department.superDepartment from User as user1 where user1.id in(:list))").setParameterList("list", ids).list();
		 if (user.isEmpty()) {
	            return null;
	        } else {
	            return user;
	        }
	 }
	 
	 
	 public Department getParentDepartmentForUser(List<String> ids) throws EazyBpmException{
		 List<Department> department = getSession().createQuery("select department from Department as department where department.id in (select user1.department.superDepartment from User as user1 where user1.id in(:list))").setParameterList("list", ids).list();
		 if (department.isEmpty()) {
	            return null;
	        } else {
	            return department.get(0);
	        }
	 }
	 
	 public String getAllRelativeIds(String id, boolean isChild) throws EazyBpmException{
		 List<String> userIds = getSession().createSQLQuery("SELECT GetFamilyTree(:id,:isChild)")
								 .setParameter("isChild", isChild)
								 .setParameter("id", id).list();
		 if (userIds.isEmpty()) {
	            return null;
	        } else {
	            return userIds.get(0);
	        }
	 }
	 
	 public boolean getEmailNotificationEnabled(String userId) throws EazyBpmException{
		 List<Boolean> emailNotifications = getSession().createQuery("select user.sendEmailNotification from User as user where user.id='"+userId+"'").list();
		 if(emailNotifications.size() > 0){
			 return new Boolean(emailNotifications.get(0));
		 } else {
			 return false;
		 }
	 }
	 
	 /**
	     * {@inheritDoc}
	     */
	    public List<String> getSecretaryToLeader(List<String> ids) throws EazyBpmException{                            
	    	
	    	List<String> user = getSession().createQuery("select user.id from User as user where user.secretary in (:list)").setParameterList("list", ids).list();
	    	 if (user.isEmpty()) {
	             return null;
	         } else {
	             return user;
	         }
	    }
	    
	    public List<String> getUserIdsByDepartmentIds(List<String> ids) throws EazyBpmException{
	       	List<String> userList = (List<String>) getSession().createQuery("select user.id from User as user where user.department.id in (:list)").setParameterList("list", ids).list();
	       	if(userList.isEmpty()){
	       		return null;
	       	}else{
	       		return userList;
	       	}
	       }
	    
		/**
	     * {@inheritDoc}
	     */
	    public List<LabelValue> getAdminDepartmentRoleUsersAsLabelValue(String roleId,Set<String> adminisDepartmentIds) throws BpmException{
	    	List<LabelValue> userList = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(concat(user.firstName ,' ', user.lastName ) as name," +
	    			"user.id as id) from User as user join user.roles as role where role.id = '"+roleId+"' and user.department.id in (:list)").setParameterList("list", adminisDepartmentIds).list();
			return userList;
	    }
	    
	    /**
	     * {@inheritDoc}
	     */
	    public List<LabelValue> getDepartmentAdminGroupUsersAsLabelValue(String groupId,Set<String> adminisDepartmentIds) throws BpmException{
	    	List<LabelValue> userList = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(concat(user.firstName ,' ', user.lastName ) as name," +
	    			"user.id as id) from User as user join user.groups as group where group.id = '"+groupId+"' and user.department.id in (:list)").setParameterList("list", adminisDepartmentIds).list();
			return userList;
	    }
		@SuppressWarnings("unchecked")
	public List<UserDTO> getAllUsersById(List<String> userIds) {
		List<UserDTO> user = (List<UserDTO>) getSession()
				.createQuery(
						"select new com.eazytec.dto.UserDTO(user.id as id,user.username as username,user.firstName as firstName,user.lastName as lastName,department.name as department,user.email as email,user.employeeNumber as employeeNumber,user.enabled as enabled,user.sendEmailNotification as sendEmailNotification) from User as user  where user.enabled=true and user.id  in (:list)")
				.setParameterList("list", userIds).list();
		return user;
	}
	
	/**
     * {@inheritDoc}
     */
    public List<String> getUserFullNamesByUsernames(List<String> usernames){
    	List<String> userList = (List<String>) getSession().createQuery("select concat(concat(user.firstName, ' '), user.lastName) as fullName from User as user where user.username in (:list)").setParameterList("list", usernames).list();
       	if(userList.isEmpty()){
       		return null;
       	}else{
       		return userList;
       	}
    }
	    
    /**
     * {@inheritDoc}
     */
    public String getUserFullNamesByUsername(String username){
    	List<String> userList = (List<String>) getSession().createQuery("select concat(concat(user.firstName, ' '), user.lastName) as fullName from User as user where user.username = '"+username+"'").list();
       	if(userList.isEmpty()){
       		return null;
       	}else{
       		return userList.get(0);
       	}
    }
    
    /**
     * {@inheritDoc}
     */
    public void deleteInternalUserId(String username){
    	Query query = getSession().createSQLQuery("DELETE FROM JAMES_USER USR WHERE USER.USR_NAME = '"+username+"')");
       	query.executeUpdate();
    }
    
    public String getUserMailIdByUser(String userId) throws EazyBpmException{
    	List<String> emailIds = getSession().createQuery("select user.email from User as user where user.id='"+userId+"' and user.email is not null").list();
		 if(emailIds.size() > 0){
			 return emailIds.get(0);
		 } else {
			 return null;
		 }
    }
   
    /**
     * {@inheritDoc}
     */
    public List<String> getUsersEmailIdByRole(String roleId) throws EazyBpmException{
    	List<String> emailIds = getSession().createQuery("select user.email from User as user join user.roles as role where role.id='"+roleId+"' and user.email is not null and user.sendEmailNotification=true").list();
		return emailIds;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getUsersEmailIdByGroup(String groupId) throws EazyBpmException{
    	List<String> emailIds = getSession().createQuery("select user.email from User as user join user.groups as group where group.id='"+groupId+"' and user.email is not null and user.sendEmailNotification=true").list();
		return emailIds;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getUsersEmailIdByDepartment(String departmentId) throws EazyBpmException{
    	List<String> emailIds = getSession().createQuery("select user.email from User as user join user.department as department where department.id='"+departmentId+"' and user.email is not null and user.sendEmailNotification=true").list();
		return emailIds;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getUsersMobileNoByRole(String roleId) throws EazyBpmException{
    	List<String> emailIds = getSession().createQuery("select user.mobile from User as user join user.roles as role where role.id='"+roleId+"' and user.sendEmailNotification=true").list();
		return emailIds;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getUsersMobileNoByGroup(String groupId) throws EazyBpmException{
    	List<String> emailIds = getSession().createQuery("select user.mobile from User as user join user.groups as group where group.id='"+groupId+"' and user.sendEmailNotification=true").list();
		return emailIds;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getUsersMobileNoByDepartment(String departmentId) throws EazyBpmException{
    	List<String> emailIds = getSession().createQuery("select user.mobile from User as user join user.department as department where department.id='"+departmentId+"' and user.sendEmailNotification=true").list();
		return emailIds;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getUsersMobileNoByUser(String userId) throws EazyBpmException{
    	List<String> emailIds = getSession().createQuery("select user.mobile from User as user where user.id='"+userId+"' and user.mobile is not null and user.sendEmailNotification=true").list();
		return emailIds;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getUsersInternalMessageIdByRole(String roleId, String domainName) throws EazyBpmException{
    	List<String> emailIds = getSession().createQuery("select concat(user.id, concat('@', '"+domainName+"')) from User as user join user.roles as role where role.id='"+roleId+"' and user.sendEmailNotification=true").list();
		return emailIds;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getUsersInternalMessageIdByGroup(String groupId, String domainName) throws EazyBpmException{
    	List<String> emailIds = getSession().createQuery("select concat(user.id, concat('@', '"+domainName+"')) from User as user join user.groups as group where group.id='"+groupId+"' and user.sendEmailNotification=true").list();
		return emailIds;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getUsersInternalMessageIdByDepartment(String departmentId, String domainName) throws EazyBpmException{
    	List<String> emailIds = getSession().createQuery("select concat(user.id, concat('@', '"+domainName+"')) from User as user join user.department as department where department.id='"+departmentId+"' and user.sendEmailNotification=true").list();
		return emailIds;
    }
    
    public User getUserByToken(String token){
    	Finder f = Finder.create("from User u where u.token = :token").setParam("token", token);
    	f.setCacheable(true);
    	return findUnique(f);
    }
    
}
