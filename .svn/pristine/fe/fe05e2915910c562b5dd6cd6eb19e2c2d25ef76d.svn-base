package com.eazytec.bpm.admin.widget.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InvalidNameException;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.User;
import com.eazytec.model.Widget;
import com.eazytec.model.UserWidget;
import com.eazytec.model.Classification;

public interface WidgetService {
	
	Widget saveWidget(Widget widget) throws BpmException;
	
	public void saveUserWidget(String selectedWidgets,String userName,Integer noOfWidget) throws BpmException;
	
	List<Widget> getAllWidget() throws EazyBpmException;
	
	List<Widget> getAllUserSelectedWidget() throws EazyBpmException;
	
	public UserWidget getUserWidgetForId(String userName) throws EazyBpmException;
	
	Widget getWidgetForId(String id) throws EazyBpmException;
	
	//Widget getWidgetForName(String name) throws EazyBpmException;
	
	Widget getActiveWidget() throws EazyBpmException;
	
	ArrayList<Widget> getWidgetGridData();
	 
	public void deleteWidgets(List<String> ids); 
	
	/**
	 * Get all the Classification name
	 * @return classificationList
	 * 
	 * @throws EazyBpmException
	 */
	List<Classification> getAllClassification() throws EazyBpmException;
	
	List<Widget> getWidgetForNames(String name) throws EazyBpmException;
	
	List<Widget> getUserWidgetForNames(String name) throws EazyBpmException;
	
	public void removeWidget(String Id) ;
	
	public List<Widget> getWidgetForIds(List<String> names) throws EazyBpmException;
	
	void setDepartmentType(Widget widget)throws EazyBpmException;
	
	boolean getWidgetIsDepartmentAdmin(User user,Widget widget)throws EazyBpmException;
	
}