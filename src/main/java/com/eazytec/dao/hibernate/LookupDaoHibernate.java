package com.eazytec.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eazytec.dao.LookupDao;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.AgencySetting;
import com.eazytec.model.Classification;
import com.eazytec.model.Department;
import com.eazytec.model.Group;
import com.eazytec.model.Menu;
import com.eazytec.model.Module;
import com.eazytec.model.Role;
import com.eazytec.model.SysConfig;
import com.eazytec.model.TaskRole;
import com.eazytec.model.User;

/**
 * Hibernate implementation of LookupDao.
 *
 * @author madan
 * @author nkumar
 * @author Vinoth
 */
@Repository
public class LookupDaoHibernate implements LookupDao {
    private Log log = LogFactory.getLog(LookupDaoHibernate.class);
    private final SessionFactory sessionFactory;

    /**
     * Initialize LookupDaoHibernate with Hibernate SessionFactory.
     * @param sessionFactory
     */
    @Autowired
    public LookupDaoHibernate(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Role> getRoles() {
        log.debug("Retrieving all role names...");
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Role.class).list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Group> getGroups() {
        log.debug("Retrieving all group names...");
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Group.class).list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Department> getDepartments() {
        log.debug("Retrieving all department names...");
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Department.class).list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        log.debug("Retrieving all User names...");
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(User.class).list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<TaskRole> getTaskRoles() {
        log.debug("Retrieving all Task Roles...");
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(TaskRole.class).list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Classification> getProcessClassifications() {
        log.debug("Retrieving all process classifications...");
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Classification.class).addOrder(Order.asc("orderNo")).list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<AgencySetting> getAgencySetting() {
        log.debug("Retrieving all Agency Setting ...");
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(AgencySetting.class).add( Restrictions.ge("endDate", new Date())).list();
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Menu> getMenus() {
        log.debug("Retrieving all Menus...");
        Session session = sessionFactory.getCurrentSession();
        org.hibernate.Criteria criteria = session.createCriteria(Menu.class);
		//criteria.add(Restrictions.eq("active", true));
		List<Menu> menus = criteria.list();
		if (menus.isEmpty()) {
			return null;
		} else {
			return menus;
		}
    }
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Menu> getTopMenus() {
        log.debug("Retrieving all Top Menus...");
        Session session = sessionFactory.getCurrentSession();
        return (List<Menu>) session.createCriteria(Menu.class)
		.add((Criterion) Restrictions.eq("menuType", "top")).list();
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Menu> getMenus(List<Role> roles) {
    	 Session session = sessionFactory.getCurrentSession();
    	 List<Menu> menus = session.createQuery("from Role as role where role.id in (:list)").setParameterList("list", roles).list();
        if (menus.isEmpty()) {
            return null;
        } else {
            return menus;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Menu> getGlobalMenus() {
    	Session session = sessionFactory.getCurrentSession();
 		org.hibernate.Criteria criteria = session.createCriteria(Menu.class);
 		criteria.add(Restrictions.eq("isGlobal", true));
 		criteria.add(Restrictions.eq("active", true));
 		List<Menu> menus = criteria.list();
 		if (menus.isEmpty()) {
 			return null;
 		} else {
 			return menus;
 		}
 	
    }
    
   /**
    * {@inheritDoc}
    */
   public List<Module> getModulesByRoles(List<Role> roles) {
   	 Session session = sessionFactory.getCurrentSession();
   	 List<Module> modules = session.createQuery("select module from Module as module join module.roles as role where role in (:list)").setParameterList("list", roles).list();
       if (modules.isEmpty()) {
           return null;
       } else {
           return modules;
       }
   }
   
   /**
    * {@inheritDoc}
    */
   @SuppressWarnings("unchecked")
   public List<Menu> getAllActiveMenus() {
       log.debug("Retrieving all Menus...");
       Session session = sessionFactory.getCurrentSession();
       org.hibernate.Criteria criteria = session.createCriteria(Menu.class);
       criteria.add(Restrictions.eq("active", true));
       List<Menu> menus = criteria.list();
       if (menus.isEmpty()) {
			return null;
       } else {
			return menus;
       }
   }
   
   /**
    * {@inheritDoc}
    */
   @SuppressWarnings("unchecked")
   public List<String> getSysConfigs() {
       log.debug("Retrieving all SysConfigs...");
       Session session = sessionFactory.getCurrentSession();
       return session.createCriteria(SysConfig.class).setProjection(Projections.distinct(Projections.property("selectType"))).list();
   }
   
   @SuppressWarnings("unchecked")
   public List<SysConfig> getSysConfigsByType(String type) {
       log.debug("Retrieving all SysConfigs...");
       Session session = sessionFactory.getCurrentSession();
       return session.createCriteria(SysConfig.class).add((Criterion) Restrictions.eq("selectType",type)).list();
   }  
		
   /**
	* {@inheritDoc checkListViewName}
	*/
   public List<SysConfig> getSysConfigByKey(String key)throws EazyBpmException{
		Session session = sessionFactory.getCurrentSession();
		List<SysConfig> sysConfigs = session.createCriteria(SysConfig.class).add(Restrictions.eq("selectKey", key)).list();
		return sysConfigs;
   }
	
	public Role getRoleByName(String roleName) {
       Session session = sessionFactory.getCurrentSession();
    	org.hibernate.Criteria criteria = session.createCriteria(Role.class);
		criteria.add(Restrictions.eq("name", roleName));
		List<Role> roles = criteria.list();
        if (roles.isEmpty()) {
            return null;
        } else {
            return (Role) roles.get(0);
        }
	}

	public User getUser(String username){
		Session session = sessionFactory.getCurrentSession();
    	org.hibernate.Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("username", username));
		List<User> users = criteria.list();
        if (users.isEmpty()) {
            return new User(username);
        } else {
            return (User) users.get(0);
        }
	}
}
