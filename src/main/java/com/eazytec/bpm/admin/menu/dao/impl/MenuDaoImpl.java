package com.eazytec.bpm.admin.menu.dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.admin.menu.dao.MenuDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.model.Menu;

/**
 * This class interacts with Hibernate session to save/delete, retrieve Menu
 * objects.
 * 
 * @author mathi
 */

@Repository("menuDao")
public class MenuDaoImpl extends GenericDaoHibernate<Menu, String> implements
MenuDao {
	/**
	 * Constructor to create a Generics-based version using Menu as the entity
	 */
	public MenuDaoImpl() {
		super(Menu.class);
	}

	/**
	 * {@inheritDoc}
	 */
	public Menu getMenuByName(String menuName) {
		List menus = getSession().createCriteria(Menu.class)
				.add(Restrictions.eq("name", menuName)).list();
		if (menus.isEmpty()) {
			return null;
		} else {
			return (Menu) menus.get(0);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeMenu(String menuName) {
		Object menu = getMenuByName(menuName);
		Session session = getSessionFactory().getCurrentSession();
		session.delete(menu);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> getMenus() {
		Query qry = getSession().createQuery("from Menu u order by u.menuOrder asc");
		return qry.list();
	}

	/**
	 * {@inheritDoc}
	 */
	public Menu saveMenu(Menu menu) {
		if (log.isDebugEnabled()) {
			log.debug("menu's id: " + menu.getId());
		}
		getSession().saveOrUpdate(menu);
		// necessary to throw a DataIntegrityViolation and catch it in
		// MenuManager
		getSession().flush();
		return menu;
	}

	/**
	 * Overridden to call the saveMenu method since saveMenu flushes the
	 * session and saveObject of BaseDaoHibernate does not.
	 * 
	 * @param menu
	 *            the menu to save
	 * @return the modified menu (with a primary key set if they're new)
	 */
	@Override
	public Menu save(Menu menu) {
		return this.saveMenu(menu);
	}

	/**
	 * {@inheritDoc}
	 */
	public Menu getMenuById(String id) {
		List menus = getSession().createCriteria(Menu.class)
				.add(Restrictions.eq("id", id)).list();
		if (menus.isEmpty()) {
			return null;
		} else {
			return (Menu) menus.get(0);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Menu> getTopMenus() {
		org.hibernate.Criteria criteria = getSession().createCriteria(Menu.class);
		criteria.add(Restrictions.eq("menuType", "top"));
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
	public List<Menu> getParentSideMenus() {
		org.hibernate.Criteria criteria = getSession().createCriteria(Menu.class);
		criteria.add(Restrictions.eq("menuType", "side"));
		criteria.add(Restrictions.eq("isParent", true));
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
	public List<Menu> getSideMenus(Menu topMenu) {
		org.hibernate.Criteria criteria = getSession().createCriteria(Menu.class);
		criteria.add(Restrictions.eq("menuType", "side"));
		criteria.add(Restrictions.eq("parentMenu", topMenu));
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
	public List<Menu> getActiveMenus() {
		org.hibernate.Criteria criteria = getSession().createCriteria(Menu.class);
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
    public List<Menu> getMenus(List<String> ids) {
    	List<Menu> menus = getSession().createQuery("from Menu as menu where menu.id in (:list)").setParameterList("list", ids).list();
        if (menus.isEmpty()) {
            return null;
        } else {
            return menus;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Menu> getMenuNotInRoles(Set<Menu> menus) {
    	List<Menu> menuIds = getSession().createQuery("select menu from Menu as menu where menu.isGlobal=false and menu not in (:list)").setParameterList("list", menus).list();
        if (menuIds.isEmpty()) {
            return null;
        } else {
            return menuIds;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Menu> getAllChildMenuForParent(String id){
		 Query qry = getSession().createQuery("from Menu menu where menu.isParent=1 and menu.parentMenu.id='"+id+"' order by upper(menu.name) asc");
	     return qry.list();
			
	 }
    
    /**
     * {@inheritDoc}
     */
	public List<Menu> getChildMenus(String id){
		 Query qry = getSession().createQuery("from Menu menu where menu.parentMenu.id='"+id+"' and menu.id != menu.parentMenu.id");
	     return qry.list();
			
	}
}
