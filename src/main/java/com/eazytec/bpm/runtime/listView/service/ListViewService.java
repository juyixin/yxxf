package com.eazytec.bpm.runtime.listView.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.json.JSONArray;

import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LabelValue;
import com.eazytec.model.ListView;
import com.eazytec.model.ListViewButtons;
import com.eazytec.model.ListViewColumns;
import com.eazytec.model.ListViewGroupSetting;
import com.eazytec.model.Module;

import dtw.webmail.model.JwmaFolderImpl;

public interface ListViewService {
	
	/**
	 * To create a grid view by the give list View Name
	 * @param listViewName
	 * @param title
	 * @param viewObj
	 * @return
	 * @throws EazyBpmException
	 */
	String showListViewGrid(String listViewName,String title,String container,Map<String,Object> namedParameter,boolean isGrouping,List<Map<String, Object>> mapViewColumnProperty, boolean needHeader)throws Exception, EazyBpmException;
	

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
	 * @throws EazyBpmException
	 */
	ListView saveListView(ListView listView,String modeType,String moduleId)throws EazyBpmException;
	
	/**
	 * To save or update the list view columns details
	 * @throws EazyBpmException
	 */
	ListView saveListViewColumnDetails(ListView listView,List<Map<String,Object>> listViewMetaColumns,boolean isListViewColumnUpdate,String moduleId)throws EazyBpmException;
	
	Set<Module> getListViewModules(String listViewId,Module module)throws EazyBpmException;
	
	/**
	 * To save or update the list view group setting details
	 * @throws EazyBpmException
	 */
	ListView saveListViewGroupSettingDetails(ListView listViewId,List<Map<String,Object>> listViewMetaColumns,boolean isListViewGroupSettingUpdate,String moduleId)throws EazyBpmException;
	
	
	/**
	 * To delete the list view columns details
	 * @throws EazyBpmException
	 */
	void deleteListViewByIds(List<String> deleteListViewDetails,String tableName)throws EazyBpmException;
	
	/**
	 * To get the list view object by id
	 * @param listViewId
	 * @return
	 * @throws EazyBpmException
	 */
	ListView getListViewById(String listViewId)throws EazyBpmException;
	
	/**
	 * To get the list view object by id
	 * @param listViewId
	 * @return
	 * @throws EazyBpmException
	 */ 
	Module getListViewModulesById(String listViewId)throws EazyBpmException;
	
	/**
	 * Get child node for grid group setting
	 * @param listViewId
	 * @param root
	 * @param parentGroup
	 * @return
	 * @throws Exception
	 * @throws EazyBpmException
	 */
	JSONArray getChildNodesForTree(String listViewId, String root, String parentGroup, List<String> groupByFields, List<Map<String,Object>> whereParams) throws Exception, EazyBpmException;
	
	/**
	 * To get the list view Query
	 * @param listViewId
	 * @return
	 * @throws EazyBpmException
	 */
	String constructListViewQuery(ListView listView )throws EazyBpmException;
	
	/**
	 * To get the list view Query with search parameter
	 * @param listViewId
	 * @return
	 * @throws EazyBpmException
	 */
	List<Map<String, Object>> getAllListViewMapDataWithSearchParams(ListView listView, Map<String, Object> searchParamMap, String sortType, String sortTypeColumn, String searchType,Map<String,Object> listViewParamsList)throws EazyBpmException;
	
	/**
	 * To get the list view advanced search
	 * @param listViewId
	 * @return
	 * @throws EazyBpmException
	 */
	List<Map<String, Object>> getDataWithSearchParamsAndConstraints(ListView listView, List<Map<String, Object>> searchParamListOfMap, String sortType, String sortTypeColumn, String searchType,Map<String,Object> namedParameters)throws EazyBpmException;
	
	/**
	 * 
	 * @param listView
	 * @param searchValue
	 * @return
	 * @throws EazyBpmException
	 */
	List<Map<String, Object>> getAllListViewMapDataWithSearchValue(ListView listView, String searchValue,Map<String,Object> namedParameters)throws EazyBpmException;
	
	/**
	 * To check the query is executable or not 
	 * @param listView
	 * @throws EazyBpmException
	 */
	boolean checkListViewQuery(ListView listView)throws EazyBpmException;
	
	/**
	 * To check the query is executable or not 
	 * @param listView
	 * @throws EazyBpmException
	 */
	boolean checkListViewQuery(String listViewQuery)throws EazyBpmException;
	
	/**
	 * To check the List name is exits or not 
	 * @param listView
	 * @throws EazyBpmException
	 */
	boolean checkListViewName(String listViewName)throws EazyBpmException;
	
	/**
	 * To save or update the list view button details
	 * @throws EazyBpmException
	 */
	ListView saveListViewButtonDetails(ListView listViewObj,List<Map<String,Object>> listViewMetaButtons,boolean isListViewButtonUpdate,String moduleId)throws EazyBpmException;
	
	/**
	 * Get Button Property for given list view id
	 * @param listViewId
	 * @return
	 * @throws EazyBpmException
	 */
	List<ListViewButtons> getButtonPropertyByListViewId(String listViewId)throws EazyBpmException;
	
	/**
	 * Soft Delete the list view by ID
	 * @param listViewId
	 * @return
	 * @throws EazyBpmException
	 */
	int softDeleteTableDataAndRestore( List<String> deleteTableIds, String tableName, String columnName, int status)throws EazyBpmException;
	
	/**
	 * Delete the list view by ID
	 * @param listViewId
	 * @return
	 * @throws EazyBpmException
	 */
	int deleteTableData(List<String> deleteTableIds, String tableName, String columnName)throws EazyBpmException;
	
	
	/**
	 * Delete the list view by ID
	 * @param listView
	 * @return
	 * @throws EazyBpmException
	 */
	void deleteListViewDetailsById(String listViewObj)throws EazyBpmException;
	
	/**
	 * return ListView list for given ids
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
	 * Get all list view template as label value pair
	 * @return
	 * @throws EazyBpmException
	 */
	List<LabelValue> getAllListViewTemplate()throws EazyBpmException;
	
	
	/**
	 * Get all active table as label value pair
	 * @return
	 * @throws EazyBpmException
	 */
	List<LabelValue> getAllTable()throws EazyBpmException;
	
	 /**
     * get all Meta table
     * @return
     * @throws EazyBpmException
     */
	List<LabelValue> getAllMetaTable()throws EazyBpmException;
	
	 /**
     * Get all columns Name by table name
     * @param tableName
     * @return
     * @throws EazyBpmException
     */
	List<LabelValue> getAllColumnsByTableName(String tableName)throws EazyBpmException;

	/**
     * Get JSONARRAY for listview groupsetting
     * @param tableName
     * @return
     * @throws EazyBpmException
     */
	JSONArray constructJsonListViewGroup(ListView listView) throws Exception, EazyBpmException;
	
	 /**
     * Get restore the list view version
     * @param viewId,viewName
     * @return
     * @throws EazyBpmException
     */
	void restoreListViewVersion(String viewId, String viewName)throws EazyBpmException;
	
	/**
	 * To delete all versions by its name
	 * @param listViewName
	 * @throws EazyBpmException
	 */
	void deleteAllListViewDetails(String listViewName)throws EazyBpmException;
	
	/**
	 * To generate script for mail grid view
	 * @param folder
	 * @param folderName
	 * @return String
	 * @throws EazyBpmException
	 */
	String getListViewForMailInbox(JwmaFolderImpl folder,String folderName)throws EazyBpmException;
	
	/**
	 * get mail inbox messages script
	 * 
	 * @param folder
	 * @param from
	 * @return
	 * @throws EazyBpmException
	 * @throws MessagingException
	 */
	String getListViewForInboxMail(Message[] msgs)throws EazyBpmException, MessagingException, UnsupportedEncodingException;
	
	/**
	 * get mail draft messages script
	 * 
	 * @param folder
	 * @param from
	 * @return
	 * @throws EazyBpmException
	 * @throws MessagingException
	 */
	String getListViewForDraftMail(Message[] msgs)throws EazyBpmException, MessagingException, UnsupportedEncodingException;
	
	/**
	 * get mail sent messages script
	 * 
	 * @param folder
	 * @param from
	 * @return
	 * @throws EazyBpmException
	 * @throws MessagingException
	 */
	String getListViewForSentMail(Message[] msgs)throws EazyBpmException, MessagingException, UnsupportedEncodingException;
	
	/**
	 * get listview for sent-later
	 * 
	 * @param msgs
	 * @return
	 * @throws EazyBpmException
	 * @throws MessagingException
	 */
	String getListViewForSentLaterMessage(Message[] msgs)throws EazyBpmException, MessagingException, UnsupportedEncodingException;
	
	List<String> getListViewWhereParamNames(String viewName)throws EazyBpmException;
	
	/**
	 * To copy the list view to modules
	 * @param moduleId
	 * @param listViewId
	 * @return
	 * @throws EazyBpmException
	 */
	String copyListViewToModule(String moduleId,String listViewId)throws EazyBpmException;
	
	String constructListViewDump(Set<ListView> listViews,String moduleId)throws EazyBpmException;
	

	/**
	 * Get all list view as label value pair witout version
	 * @return
	 * @throws EazyBpmException
	 */
	List<LabelValue> getAllListViewWithoutVersion()throws EazyBpmException;
	
	List<ListView> getListViewDetailsByNames(String listViewName)throws EazyBpmException;
	
	ListView getListViewByName(String listViewName)throws EazyBpmException;
	
	List<LabelValue> getListViewNames(String listViewName)throws EazyBpmException;
	
	public boolean updateListViewColumnOrderNos(
			List<Map<String, Object>> columnList) throws EazyBpmException;
	
}
