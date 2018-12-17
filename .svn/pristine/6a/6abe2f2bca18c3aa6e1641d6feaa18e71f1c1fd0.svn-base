package com.eazytec.bpm.admin.widget.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.eazytec.dao.GenericDao;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.Widget;
import com.eazytec.model.UserWidget;
import com.eazytec.model.Classification;
import com.eazytec.model.WidgetDepartment;
/**
 * 
 * @author sri sudha
 *
 */
public interface WidgetDao extends GenericDao<Widget, String>  {
	/**
	 * Save the Widget Object into DB
	 * @param widget
	 * @return
	 * @throws EazyBpmException
	 */
	Widget saveWidget(Widget widget) throws EazyBpmException;
	
	UserWidget saveUserWidget(UserWidget widget) throws EazyBpmException;
	
	List<Widget> getAllWidget() throws EazyBpmException;
	
	public List<UserWidget> getUserSelectedWidget(String userName) throws EazyBpmException ;
	
	List<Widget> getAllUserSelectedWidget(List<String> widgetIdList) throws EazyBpmException;
	
	public UserWidget getUserWidgetForId(String userName) throws EazyBpmException;
	
	/**
	 * Get Widget for given layout id
	 */
	Widget getWidgetForId(String id) throws EazyBpmException;
	
	
	/**
	 * Get the Active Widget form DB
	 * @return
	 * @throws EazyBpmException
	 */
	
	Widget getActiveWidget() throws EazyBpmException;
	
	/**
	 * Get all the Classification name
	 * @return classificationList
	 * 
	 * @throws EazyBpmException
	 */
	 List<Classification> getAllClassification() throws EazyBpmException;
	
	 ArrayList<Widget> getWidgetGridData() ;
	 
	 List<Widget> getWidgetForNames(String name) throws EazyBpmException;
	 
	 public void removeWidget(String Id) ;
	 
	 public List<Widget> getWidgetForIds(List<String> names) throws EazyBpmException;
	 
	 WidgetDepartment saveWidgetDepartment(WidgetDepartment widgetDepartment) throws EazyBpmException;
	 
	 List<WidgetDepartment> getWidgetDepartmentList(String widgetId,List<String> departments) throws EazyBpmException;
	 
	 List<Widget> getPublicWidgetByDepartment(List<String> userDeps)throws EazyBpmException;
}