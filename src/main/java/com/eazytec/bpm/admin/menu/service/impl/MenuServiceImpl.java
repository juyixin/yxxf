package com.eazytec.bpm.admin.menu.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.eazytec.bpm.admin.menu.dao.MenuDao;
import com.eazytec.bpm.admin.menu.service.MenuService;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Menu;
import com.eazytec.service.GroupExistsException;
import com.eazytec.service.MenuExistsException;
import com.eazytec.service.impl.GenericManagerImpl;

/**
 * This class represents the basic "menu" object operations
 * and extends GenericManagerImpl and implementing menu service interface.
 *
 * @author mathi
 */
@Service("menuService")
public class MenuServiceImpl extends GenericManagerImpl<Menu, String> implements MenuService {
	
	private MenuDao menuDao;
	public VelocityEngine velocityEngine;
	 
	@Autowired
	public MenuServiceImpl(MenuDao menuDao) {
		super(menuDao);
		this.menuDao = menuDao;
	}

	@Autowired
	public void setMenuDao(MenuDao menuDao) {
		this.dao = menuDao;
		this.menuDao = menuDao;
	}

	@Autowired
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

	/**
	 * {@inheritDoc}
	 */
	public Menu getMenuByName(String menuName) {
		return menuDao.getMenuByName(menuName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Menu getMenu(String menuId) {
		return menuDao.get(menuId);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Menu> getMenus() {
		return menuDao.getMenus();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws GroupExistsException
	 */
	public Menu getMenu(Menu menu) throws GroupExistsException {

		try {
			return menuDao.saveMenu(menu);
		} catch (Exception e) {
			e.printStackTrace();
			log.warn(e.getMessage());
			throw new GroupExistsException("Menu '" + menu.getName()
					+ "' already exists!");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeMenu(Menu menu) {
		log.debug("removing menu: " + menu);
		menuDao.remove(menu);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeMenu(String menuId) {
		log.debug("removing menu: " + menuId);
		menuDao.remove(menuId);
	}
	
	/**
     * {@inheritDoc}
     */
    public Menu getMenuById(String id) {
        return menuDao.getMenuById(id);
    }

    /**
     * {@inheritDoc}
     */
    public List<Menu> getTopMenus() {
        return menuDao.getTopMenus();
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Menu> getParentSideMenus() {
        return menuDao.getParentSideMenus();
    }

    /**
     * {@inheritDoc}
     */
    public List<Menu> getSideMenus(Menu topMenu) {
        return menuDao.getSideMenus(topMenu);
    }
    
    /**
	 * {@inheritDoc}
	 */
	public Menu saveMenu(Menu menu) throws MenuExistsException {
		try {
			return menuDao.saveMenu(menu);
		} catch (Exception e) {
			e.printStackTrace();
			log.warn(e.getMessage());
			throw new MenuExistsException("Menu '" + menu.getName()
					+ "' already exists!");
		}
	}
	
	/**
     * {@inheritDoc}
     */
    public List<Menu> getMenus(List<String> ids) {
        return menuDao.getMenus(ids);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Menu> getActiveMenus() {
        return menuDao.getActiveMenus();
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Menu> getMenuNotInRoles(Set<Menu> menus) {
        return menuDao.getMenuNotInRoles(menus);
    }
    
    public List<Menu> getAllChildMenuForParent(String id){
    	return menuDao.getAllChildMenuForParent(id);
    }
    
    /**
     * {@inheritDoc}
     */
    public void deleteMenu(String id){
    	log.info("Deleting menu : "+id);
    	List<Menu> childMenus = menuDao.getChildMenus(id);
    	if(childMenus != null && childMenus.size() > 0){
    		for(Menu child : childMenus){
    			List<Menu> subChildMenus = menuDao.getChildMenus(child.getId());
    			if(childMenus != null && childMenus.size() > 0){
    				for(Menu subChild : subChildMenus){
        				dao.remove(subChild);
        			}
    			}
    			dao.remove(child);
    		}
    	}
    	Menu menu = dao.get(id);
    	menu.setParentMenu(null);
    	menuDao.saveMenu(menu);
    	dao.remove(id);
    }
    
    /**
     * {@inheritDoc}
     */
    public String getUserMenuScript(String templateName, List<Menu> menus){
    	String script = "";
    	Map<String, Object> context = new HashMap<String, Object>();
    	try {
    		context.put("menus", menus);
    		script = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, context);
        } catch (Exception e) {
           throw new EazyBpmException("Problem loading Page : "+templateName,e);
        }
    	return script;
    }
}

