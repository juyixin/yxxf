package com.eazytec.bpm.admin.layout.service;

import java.util.List;

import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Layout;
import com.eazytec.model.QuickNavigation;

/**
 * 
 * @author revathi
 *
 */
public interface LayoutService {
	
	/**
	 * Used to save the layout details
	 * @param layout
	 * @return
	 * @throws BpmException
	 */
	Layout saveLayout(Layout layout) throws BpmException;
	
	/**
	 * to save layout
	 *  
	 * @param layout
	 * @param assignedTo
	 * @param departments
	 * @param roles
	 * @return
	 * @throws BpmException
	 */
	Layout saveLayout(Layout layout, String assignedTo, String departments, String roles) throws BpmException;
	
	/**
	 * return List of all layouts
	 * @return
	 * @throws EazyBpmException
	 */
	List<Layout> getAllLayout() throws EazyBpmException;
	
	/**
	 * Return Layout for given layout id
	 * @param id
	 * @return
	 * @throws EazyBpmException
	 */
	Layout getLayoutForId(String id) throws EazyBpmException;
	
	/**
	 * Return active Layout 
	 * @return
	 * @throws EazyBpmException
	 */
	Layout getActiveLayout() throws EazyBpmException;
	/**
	 * Return particular User layout
	 * 
	 * @param assignedTo
	 * @return
	 * @throws EazyBpmException
	 */
		
	public Layout getLayoutByAssignedTo(String assignedTo) throws EazyBpmException ;
	/**
	 * return QuickNavigation for given id
	 * @param id
	 * @return
	 * @throws EazyBpmException
	 */
	QuickNavigation getQuickNavForId(String id) throws EazyBpmException;
	
	/**
	 * Return all list of QuickNavigation
	 * @return
	 * @throws EazyBpmException
	 */
	List<QuickNavigation> getAllQuickNavigation() throws EazyBpmException;
	
	/**
	 * Used to save QuickNavigation 
	 * @param quickNav
	 * @return
	 * @throws BpmException
	 */
	QuickNavigation saveQuickNavigation(QuickNavigation quickNav) throws BpmException;
	
	/**
	 * Return all active QuickNavigation
	 * @return
	 * @throws EazyBpmException
	 */
	List<QuickNavigation> getAllActiveQuickNavigation() throws EazyBpmException;
	
	
	int getnoOfWidget() throws EazyBpmException;
	
	List<QuickNavigation> getQuickNavigationsByNames(List<String> names);
	
	void deleteSelectedQuickNav(List<String> ids) throws Exception;
	
	/**
	 * get Role child nodes
	 * 
	 * @param currentNode
	 * @param nodeLevel
	 * 
	 * @return
	 */
	public String getRolesChildNode(String currentNode, int nodeLevel) throws BpmException, Exception ;
	
	/**
	 * get Department child nodes
	 * 
	 * @param currentNode
	 * @param nodeLevel
	 * 
	 * @return
	 */
	public String getDepartmentsChildNode(String currentNode, int nodeLevel) throws BpmException, Exception;
	
	/**
	 * get loggedInUserLayout
	 * 
	 * @return
	 */
	Layout getLoggedInUserLayout() throws EazyBpmException;
	
	/**
	 * get all department id related childs upto nth level 
	 * 
	 * @param departmentId
	 * @return
	 * @throws Exception
	 */
	public List<String> getAllRelativeDepartments(String departmentId) throws Exception;
	
	/**
	 * check for the logged in userto have permission for accessing widget
	 * 
	 * @param currentNode
	 * @return
	 * @throws Exception
	 */
	public boolean graduationForWidget(String currentNode) throws Exception;
}
