package com.eazytec.bpm.runtime.listView.dao;

import java.util.List;
import java.util.Map;

import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LabelValue;
import com.eazytec.model.ListView;
import com.eazytec.model.ListViewButtons;
import com.eazytec.model.ListViewColumns;
import com.eazytec.model.ListViewGroupSetting;
import com.eazytec.model.MetaForm;
import com.eazytec.model.Module;
import com.eazytec.model.Role;

public interface ListViewDao {
	
	/**
	 * To get the details of List view by its name
	 * @param listViewName
	 * @return
	 * @throws EazyBpmException
	 */
	ListView getListViewDetails(String listViewName)throws EazyBpmException;
	
	/**
	 * To get the List of data's by the constructed query 
	 * @param listViewQuery
	 * @param columnList TODO
	 * @return
	 * @throws EazyBpmException
	 */
	List getListViewDataMap(String listViewQuery, List<String> columnList)throws EazyBpmException;
	
	/**
	 * Get Columns Property for given list view id
	 * @param listViewId
	 * @return
	 * @throws EazyBpmException
	 */
	List<ListViewColumns> getColumnsPropertyByListViewId(String listViewId)throws EazyBpmException;
	
	/**
	 * Get Group Setting Property for given list view id
	 * @param listViewId
	 * @return
	 * @throws EazyBpmException
	 */
	List<ListViewGroupSetting> getGroupSettingPropertyByListViewId(String listViewId)throws EazyBpmException;
	
	/**
	 * To save or update the list view 
	 * @param listView
	 * @param boolean
	 * @throws EazyBpmException
	 */
	ListView saveListView(ListView listView)throws EazyBpmException;
	
	/**
	 * To get the list view object
	 * @param listViewId
	 * @return
	 * @throws EazyBpmException
	 */
	ListView getListViewById(String listViewId)throws EazyBpmException;
	
	/**
	 * To save or update ListViewColumns
	 * @param ListViewColumns
	 * @throws EazyBpmException
	 */
	ListViewColumns saveListViewColumns(ListViewColumns listViewDetails)throws EazyBpmException;
	
	/**
	 * To save or update ListViewGroupSetting
	 * @param ListViewColumns
	 * @throws EazyBpmException
	 */
	ListViewGroupSetting saveListViewGroupSetting(ListViewGroupSetting listViewGroupSettingDetails)throws EazyBpmException;
	
	/**
	 * To delete ListViewColumns by list of ids
	 * @param deleteListViewColumnQuery
	 * @throws EazyBpmException
	 */
	void deleteListViewByIds(String deleteListViewColumnQuery)throws EazyBpmException;
	
	/**
	 * To check the List name is exits or not 
	 * @param listView
	 * @throws EazyBpmException
	 */
	boolean checkListViewName(String listViewName)throws EazyBpmException;
	
	/**
	 * To save or update ListViewButtons
	 * @param ListViewButtons
	 * @throws EazyBpmException
	 */
	ListViewButtons saveListViewButtons(ListViewButtons listViewDetails)throws EazyBpmException;
	
	/**
	 * Get Button Property for given list view id
	 * @param listViewId
	 * @return
	 * @throws EazyBpmException
	 */
	List<ListViewButtons> getButtonPropertyByListViewId(String listViewId)throws EazyBpmException;
	
	/**
	 * Soft Delete the list view by ID
	 * @param deletedUser TODO
	 * @param deletedTime TODO
	 * @param listViewId
	 * @return
	 * @throws EazyBpmException
	 */
	int softDeleteTableDataAndRestore(String tableIds, String tableName, String columnName, int status, String deletedUser, String deletedTime)throws EazyBpmException;
	
	/**
	 * Delete the list view by ID
	 * @param listViewId
	 * @return
	 * @throws EazyBpmException
	 */
	int deleteTableData(String tableIds, String tableName, String columnName)throws EazyBpmException;
	
	
	/**
	 * Delete the list view by ID
	 * @param listViewId
	 * @return
	 * @throws EazyBpmException
	 */
	void deleteListViewDetailsById(ListView listViewObj)throws EazyBpmException;
	
	/**
	 * Get All the records of ListView for given ids
	 * @param ids
	 * @return
	 * @throws EazyBpmException
	 */
	List<ListView> getListViewByIds(List<String> ids)throws EazyBpmException;
	
	
	/**
	 * Get all list view as label value pair
	 * @return
	 * @throws EazyBpmException
	 */
	List<LabelValue> getAllListView()throws EazyBpmException;
	
	/**
	 * Get all list view as label value pair witout version
	 * @return
	 * @throws EazyBpmException
	 */
	List<LabelValue> getAllListViewWithoutVersion()throws EazyBpmException;
	
	/**
	 * Get all list view template as label value pair
	 * @return
	 * @throws EazyBpmException
	 */
	List<LabelValue> getAllListViewTemplate()throws EazyBpmException;
	
	/**
	 * Returns latest version of List view.
	 * @return version number.
	 */
	Integer getLatestVersionByForm(String listViewName) throws EazyBpmException;
	
	/**
	 * <P>Update list view status by List view name.</P>
	 * @param View Name
	 * @throws EazyBpmException
	 */
	void updateListViewStatusByName(String listViewName)throws EazyBpmException;
	
	/**
	 * <P>Update List view columns status by List view id.</P>
	 * @param View Name
	 * @throws EazyBpmException
	 */
	void updateListActiveStatusByListViewId(String listViewId,String tableName)throws EazyBpmException;
	
	/**
	 * To get the list of view id's
	 * @param viewName
	 * @return
	 * @throws EazyBpmException
	 */
	List<String> getListViewIdsByName(String viewName)throws EazyBpmException;
	
	/**
	 * To in Activate All ListView And Its Child
	 * @param viewName
	 * @return
	 * @throws EazyBpmException
	 */
	void inActivateAllListViewAndItsChild(String allListIds)throws EazyBpmException;
	
	/**
	 * To activate the list view by id
	 * @param viewId
	 */
	void activateListViewStatusById(String viewId)throws EazyBpmException;
	
	/**
	 * To delete All ListView And Its Child
	 * @param viewName
	 * @return
	 * @throws EazyBpmException
	 */
	void deleteAllListViewAndItsChild(String allListIds)throws EazyBpmException;
	
	List<ListView> getListViewDetailsByNames(String formNames)throws EazyBpmException;
	
	Module getModuleByListViewId(String listViewId)throws EazyBpmException;
	
	List<LabelValue> getListViewNames(String listViewName)throws EazyBpmException;
	
  	public boolean updateListViewColumnOrderNos(String columnId, int orderNo) throws EazyBpmException;
  	
    public List<ListViewColumns> getListViewColumns();

}
