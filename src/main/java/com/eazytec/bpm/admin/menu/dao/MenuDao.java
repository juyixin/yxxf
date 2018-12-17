package com.eazytec.bpm.admin.menu.dao;

import java.util.List;
import java.util.Set;

import com.eazytec.dao.GenericDao;
import com.eazytec.model.Menu;
import com.eazytec.model.Module;

/**
 * This interface interacts with Hibernate session to save/delete, retrieve Menu
 * objects.
 * 
 * @author mathi
 */

public interface MenuDao extends GenericDao<Menu, String>{
	
	  /**
   * Gets menu information based on menuName
   * @param menuName the menuName
   * @return populated menu object
   */
	Menu getMenuByName(String menuName);

  /**
   * Removes a menu from the database by name
   * @param menuName the menu's menuName
   */
  void removeMenu(String menuName);

 
  /**
   * Gets a list of menu's ordered by the uppercase version of their menuName.
   *
   * @return List populated list of menu's
   */
  List<Menu> getMenus();

  /**
   * Saves a menu's information.
   * @param menu the object to be saved
   * @return the persisted Menu object
   */
  Menu saveMenu(Menu menu);
	
  /**
   * Finds a menu by their id.
   *
   * @param id the menu's id used to login
   * @return Menu a populated menu object
   */
  Menu getMenuById(String id);

  /**
   * Finds a menus
   *
   * @return Menus a populated menus List
   */
  List<Menu> getTopMenus();
  
  /**
   * Finds a menus
   *
   * @return Menus a populated menus List
   */
  List<Menu> getParentSideMenus();
  
  /**
   * Finds a menus for top Menu
   *
   * @return Menus a populated menus List
   */
  List<Menu> getSideMenus(Menu topMenu);
  
  /**
   * get a menus for id list.
   *
   * @return Menu a populated menu List
   */
  List<Menu> getMenus(List<String> ids);
  
  /**
   * get all active menus for id list.
   *
   * @return Menu a populated menu List
   */
  List<Menu> getActiveMenus();
  
  /**
   * get menus that not assigned in role.
   *
   * @return Menu a populated menu List
   */
  List<Menu> getMenuNotInRoles(Set<Menu> menus);
  
  List<Menu> getAllChildMenuForParent(String id);

  /**
   * get child menus by id
   * 
   * @param id
   */
  List<Menu> getChildMenus(String id);
	
}
