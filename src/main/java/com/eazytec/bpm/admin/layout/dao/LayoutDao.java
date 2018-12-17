package com.eazytec.bpm.admin.layout.dao;

import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Layout;
import com.eazytec.model.QuickNavigation;
/**
 * 
 * @author revathi
 *
 */
public interface LayoutDao extends GenericDao<Layout, String>  {
	/**
	 * Save the Layout Object into DB
	 * @param layout
	 * @return
	 * @throws EazyBpmException
	 */
	Layout saveLayout(Layout layout) throws EazyBpmException;
	
	/**
	 * Get All Layout List
	 * @return
	 * @throws EazyBpmException
	 */
	List<Layout> getAllLayout() throws EazyBpmException;
	
	/**
	 * Get Layout for given layout id
	 */
	Layout getLayoutForId(String id) throws EazyBpmException;
	
	/**
	 * Get the Active Layout form DB
	 * @return
	 * @throws EazyBpmException
	 */
	Layout getActiveLayout() throws EazyBpmException;
	
	/**
	 * method to get layout by assignee
	 * 
	 * @param assignedTo
	 * @return
	 * @throws EazyBpmException
	 */
	public Layout getLayoutByAssignedTo(String assignedTo) throws EazyBpmException;
	
	/**
	 * Get QuickNavigation for given id
	 * @param id
	 * @return
	 * @throws EazyBpmException
	 */
	QuickNavigation getQuickNavForId(String id) throws EazyBpmException;
	
	/**
	 * Get All QuickNavigation List
	 * @return
	 * @throws EazyBpmException
	 */
	List<QuickNavigation> getAllQuickNavigation() throws EazyBpmException;
	
	/**
	 * Save QuickNavigation details into DB
	 * @param quickNav
	 * @return
	 * @throws EazyBpmException
	 */
	QuickNavigation saveQuickNavigation(QuickNavigation quickNav) throws EazyBpmException;
	
	/**
	 * Get All active QuickNavigation for home page QuickNavigation
	 * @return
	 * @throws EazyBpmException
	 */
	List<QuickNavigation> getAllActiveQuickNavigation() throws EazyBpmException;
	
	int getnoOfWidget() throws EazyBpmException;
	
	
	List<QuickNavigation> getQuickNavigationsByNames(List<String> names);
	
	void removeQuickNavigation(String Id) ;

	/**
	 * delete old layout for assignees
	 * 
	 * @param asssignees
	 */
	void deleteLayoutsByAssignedTos(List<String> asssignedTos);
	
	/**
	 * get layout by assigned to
	 * 
	 * @param assignedTos
	 * @return
	 * @throws EazyBpmException
	 */
	public List<Layout> getLayoutsByAssignedTos(List<String> assignedTos) throws EazyBpmException;
	
	/**
	 * get all department id related childs upto nth level 
	 * 
	 * @param departmentId
	 * @return
	 * @throws Exception
	 */
	public List<String> getAllRelativeDepartments(String departmentId) throws Exception;
} 
