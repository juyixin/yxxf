package com.eazytec.bpm.admin.role.dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.admin.role.dao.RoleDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.dto.RoleDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Role;
import com.eazytec.model.User;



/**
 * This class interacts with Hibernate session to save/delete,
 * retrieve Role objects.
 *
 * @author madan
*/
@Repository("roleDao")
public class RoleDaoImpl extends GenericDaoHibernate<Role, String> implements RoleDao {
	
	 /**
     * Constructor to create a Generics-based version using Role as the entity
     */
    public RoleDaoImpl() {
        super(Role.class);
    }

    /**
     * {@inheritDoc}
     */
    public Role getRoleByName(String rolename) {
    	org.hibernate.Criteria criteria = getSession().createCriteria(Role.class);
		criteria.add(Restrictions.eq("name", rolename));
		List<Role> roles = criteria.list();
        getSession().flush();
        if (roles.isEmpty()) {
            return null;
        } else {
            return (Role) roles.get(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeRole(String rolename) {
        Role role = getRoleByName(rolename);
        Session session = getSessionFactory().getCurrentSession();
        Set<User> userSet = role.getUsers();
        //a foreign key constraint fails when delete the role so that we temporary delete role-user relationship like this.
        for(User user : userSet){
        	user.getRoles().remove(role);
        	session.saveOrUpdate(user);
        }
        session.delete(role);
    }

   
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Role> getRoles() {
        Query qry = getSession().createQuery("from Role r where r.roleType!='Internal' order by r.orderNo");
        return qry.list();
    }
    
    /**
	 * {@inheritDoc}
	 */
	public boolean updateRoleOrderNos(String roleId, int orderNo) throws EazyBpmException {
		Query qry = getSession().createQuery("update Role role set role.orderNo = "+orderNo+" where role.id = '"+ roleId + "' ");
		int result = qry.executeUpdate();
		return result != 0  ? true : false;
	}

    /**
     * {@inheritDoc}
     */
    public Role saveRole(Role role) {
        if (log.isDebugEnabled()) {
            log.debug("role's id: " + role.getId());
        }
        getSession().saveOrUpdate(role);
        // necessary to throw a DataIntegrityViolation and catch it in RoleManager
        getSession().flush();
        return role;
    }

    /**
     * Overridden to call the saveRole method since saveRole flushes 
     * the session and saveObject of BaseDaoHibernate does not.
     *
     * @param role the role to save
     * @return the modified role (with a primary key set if they're new)
     */
    @Override
    public Role save(Role role) {
        return this.saveRole(role);
    }

    /**
     * {@inheritDoc}
     */
    public Role getRoleById(String id) {
        List roles = getSession().createCriteria(Role.class).add(Restrictions.eq("id", id)).list();
        if (roles.isEmpty()) {
            return null;
        } else {
            return (Role) roles.get(0);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	public List<LabelValue> getUsersLabelValueByRoleId(String id) throws EazyBpmException{
    	List<LabelValue> userList = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(concat(user.firstName ,' ', user.lastName) as userName,user.id as id) from Role as role join role.users as user where role.id = '"+id+"'").list();
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
	public List<Role> getRolesByNames(List<String> names) {
    	List<Role> roles = (List<Role>) getSession().createQuery("from Role as role where role.name in (:list)").setParameterList("list", names).list();
        if (roles.isEmpty()) {
            return null;
        } else {
            return roles;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	public List<Role> getRolesByIds(List<String> roleIds) {
    	List<Role> roles = getSession().createQuery("from Role as role where role.id in (:list)").setParameterList("list", roleIds).list();
        if (roles.isEmpty()) {
            return null;
        } else {
            return roles;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	public List<User> getUsersByRoleId(String id) throws EazyBpmException{
    	List<User> userList = (List<User>) getSession().createQuery("select role.users from Role as role where role.id = '"+id+"'").list();
    	if(userList.isEmpty()){
    		return null;
    	}else{
    		return userList;
    	}
    }
    
    @SuppressWarnings("unchecked")
	public List<User> getUsersByRoleIds(List<String> ids) throws EazyBpmException{
    	List<User> userList = (List<User>) getSession().createQuery("select role.users from Role as role where role.id in (:list)").setParameterList("list", ids).list();
    	if(userList.isEmpty()){
    		return null;
    	}else{
    		return userList;
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Role> getRolesByDepartment(Department department) {
    	List<Role> roles = getSession().createCriteria(Role.class)
    	.add(Restrictions.eq( "roleDepartment", department )).list();
		if (roles.isEmpty()) {
			return null;
		} else {
			return roles;
		}
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	public List<RoleDTO> getRolesDTO() throws BpmException{
    	List<RoleDTO> roles = (List<RoleDTO>) getSession().createQuery("select new com.eazytec.dto.RoleDTO(role.id as id,role.name as name,role.viewName as viewName) from Role as role where role.roleType !='Internal'").list();
    	return roles;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllRoleNames() throws EazyBpmException {
    	List<LabelValue> userList = (List<LabelValue>) getSession()
				.createQuery(
						"select new com.eazytec.model.LabelValue(role.name as name,role.id as id) from Role as role where role.roleType !='Internal'")
				.list();

		return userList;
    }

	/**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllRolesAsLabelValue() throws BpmException {
    	List<LabelValue> roleList = (List<LabelValue>) getSession()
				.createQuery(
						"select new com.eazytec.model.LabelValue(role.viewName as name,role.id as id) from Role as role where role.id != 'ROLE_DEFAULT' and role.roleType !='Internal'")
				.list();

		return roleList;
    }
    
    public List<Object> getUserNamesByRoleId(String id) throws EazyBpmException{
    	return getSession().createQuery("select user.id as id from Role as role join role.users as user where role.id = '"+id+"'").list();
    	
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getUserAdminDepartmentRoles(List<String>  userAdminDep) throws EazyBpmException{
    	// removed public roles in or condition
    	List<String> roles = getSession().createQuery("select role.id from Role as role where role.roleDepartment.id in (:list)").setParameterList("list", userAdminDep).list();
        if (roles.isEmpty()) {
            return null;
        } else {
        	return roles;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Role> getUserAdminRoles(List<String> adminDepartments)throws EazyBpmException{
    	List<Role> roles = getSession().createQuery(" from Role as role where role.roleDepartment.id in (:list) OR role.roleType='Public' ORDER BY role.orderNo").setParameterList("list", adminDepartments).list();
        if (roles.isEmpty()) {
            return null;
        } else {
        	return roles;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public List<String> getUserNamesByRoleIds(List<String> ids) throws EazyBpmException{
    	List<String> userNames =getSession().createQuery("select user.id as id from Role as role join role.users as user where role.id in (:list) ").setParameterList("list", ids).list();
    	if (userNames.isEmpty()) {
            return null;
        } else {
        	return userNames;
        }
    	
    }
}
