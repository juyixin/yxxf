package com.eazytec.bpm.runtime.table.service;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LabelValue;
import com.eazytec.model.MetaForm;
import com.eazytec.model.MetaTable;
import com.eazytec.model.MetaTableColumns;
import com.eazytec.model.MetaTableRelation;
import com.eazytec.model.Module;

public interface TableService {
	
	/**
	 * To create query 
	 * @param tableName
	 * @param fieldProperties
	 * @param parentTableList
	 * @param subTableList
	 * @param isUpdate
	 * @return
	 * @throws EazyBpmException
	 */
	 String createQuery(String tableName,List<Map<String,Object>> fieldProperties,List<Map<String,Object>> parentTableList,List<Map<String,Object>>  subTableList,boolean isUpdate,List<String> deleteExistingTableColumns)throws EazyBpmException;
	
	/**
	 * Save the meta Table and to create Runtime table.
	 * @param tableOperation
	 */
	 String createTable(MetaTable metaTable,List<Map<String,Object>> fieldProperties,List<Map<String,Object>> parentTableList,List<Map<String,Object>> subTableList,boolean isUpdate,List<String> deleteExistingTableColumns,Module module);
	
	/**
	 * To check the table name is already exits 
	 * @param tableName
	 * @return
	 */
	boolean checkTableName(String tableName)throws EazyBpmException;
	
	/**
	 * To get the TableOperation list 
	 * @return
	 * @throws EazyBpmException
	 */
	List<MetaTable> getTables()throws EazyBpmException;
	
	/**
	 * To get the TableOperation list by names 
	 * @return
	 * @throws EazyBpmException
	 */
	List<MetaTable> getTableDetailsByNames(String tableNames)throws EazyBpmException;
	
	/**
	 * To delete table by its id
	 * @param tableId
	 * @return
	 * @throws EazyBpmException
	 */
	String deleteTableById(String tableId)throws EazyBpmException;
	
	/**
	 * To table details by its id
	 * @param tableId
	 * @return
	 * @throws EazyBpmException
	 */
	MetaTable getTableDetails(String tableId)throws EazyBpmException;
	
	/**
	 * To construct the meta table dump
	 * @param metaTableObj
	 * @throws EazyBpmException
	 */
	String constructMetaTableDump(MetaTable metaTableObj)throws EazyBpmException;
	
	String constructMetaTableDumpForModules(MetaTable metaTableObj)throws EazyBpmException;
	
	String  getMetaTableRelationDump(List<MetaTableRelation> metaTableRelationList,Set<MetaTableRelation> metaTableRelationSet,boolean isTableAlter)throws EazyBpmException;
	
	/**
	 * Get all active table as label value pair
	 * @return
	 * @throws EazyBpmException
	 */
	List<LabelValue> getAllTable()throws EazyBpmException;
	
	/**
	 * Get the parent tables as label value pair
	 * @return
	 * @throws EazyBpmException
	 */
	List<LabelValue> getTableWithParentTable(String tableId)throws EazyBpmException;
	
	/**
	 * To get the table column name and id as label value pair by table id
	 * @return
	 * @throws EazyBpmException
	 */
	List<LabelValue> getTablesByParentTableId(String parentTableId)throws EazyBpmException;
	
	/**
	 * Get all table column as label value pair(exclude default columns)
	 * @return
	 * @throws EazyBpmException
	 */
	List<LabelValue> getAllColumnByTableId(String tableId,String defaultColumns,String selectDataType)throws EazyBpmException;
	
	/**
	 * Get all table column as label value pair(include default columns)
	 * @return
	 * @throws EazyBpmException
	 */
	List<LabelValue> getAllColumnIncludeDefaultByTableId(String tableId) throws EazyBpmException;
	
	/**
	 * To get all table datas by its name
	 * @return
	 * @throws EazyBpmException
	 */
	List<Map<String,Object>> getTableDump(String tableName,List<String> columnName)throws EazyBpmException;
	
	/**
	 * To create run time relation table for the sub form
	 * @param List<Map<String,Object>> of parent and child table
	 * @return void
	 * @throws EazyBpmException
	 */
	void createSubFormRelationshipTables(List<Map<String,Object>> subFormRelationList)throws EazyBpmException;
	
	/**
	 * To get the no relational Table list 
	 * @return
	 * @throws EazyBpmException
	 */
	List<MetaTable> getNonRelationTables(String tableId)throws EazyBpmException;
	
	/**
	 * To import table date form csv
	 * @param fileNameWithPath
	 * @param tableId
	 * @throws EazyBpmException
	 */
	void importCSVDatas(String fileNameWithPath,String tableId, String fileName)throws EazyBpmException;
	
	/**
	 * To import table date form Excel sheet
	 * @param fileNameWithPath
	 * @param tableId
	 * @throws EazyBpmException
	 */
	void importExcelDatas(String fileNameWithPath,String tableId, String fileName)throws EazyBpmException;
	
	/**
	 * To delete the parent table relation list
	 * @param deletParentTableList
	 * @throws EazyBpmException
	 */
	void deleteParentTableList(MetaTable metaTable,List<String> parentTableList)throws EazyBpmException;
	
	/**
	 * To delete the parent table relation list
	 * @param deletParentTableList
	 * @throws EazyBpmException
	 */
	void deleteSubTableList(MetaTable metaTable,List<String> subTableList)throws EazyBpmException;
	
	List<MetaTable> getTablesByIds(List<String> ids)throws EazyBpmException;
	
	String getListViewByTableName(String tableName,boolean isQuery)throws EazyBpmException;
	
	String copyTableToModule(String moduleId,String tableId)throws EazyBpmException;
	
	void executeModuleRelationQuery(String moduleQuery)throws EazyBpmException;
	
	/**
	 * To construct the multi table dump
	 * @param dumpOutPut
	 * @param tableNames
	 * @param dataBaseInfo
	 * @throws EazyBpmException
	 */
	void constructMultiTableDump(OutputStream dumpOutPut,String tableNames,List<String> dataBaseInfo)throws EazyBpmException;
	
	 /**
	  * construct Table Dumps
	  * @param metaTables
	  * @param dumpOutPut
	  * @param dataBaseInfo
	  * @throws EazyBpmException
	  */
	void constructTableDumps(Set<MetaTable> metaTables,OutputStream dumpOutPut,List<String> dataBaseInfo,boolean isWholeDump)throws EazyBpmException;
	
	/**
	 * To delete data
	 * @param idList
	 * @throws EazyBpmException
	 */
	void deleteDataByIds(List<String> idList, String tableName)throws EazyBpmException;
	
	/**
	 * To change the runtime table status
	 * @param taskDetailsMap
	 * @throws EazyBpmException
	 */
	void runtimeTableStatusChange(List<Map<String,Object>> taskDetailsMap)throws EazyBpmException;
	
	MetaTable getTableById(String tableId) throws EazyBpmException;
	
	/**
	  * to get all meta table columns by table id
	  * 
	  * @param tableId
	  * @param defaultColumns
	  * @return
	  * @throws EazyBpmException
	  */
	  List<MetaTableColumns> getAllMetaTableColumnsByTableId(String tableId,String defaultColumns) throws EazyBpmException;
	  
	  /**
	   * set the xml and html source of field in metaform
	   * 
	   * @param metaForm
	   * @param tableColumns
	   * @return
	   * @throws EazyBpmException
	   */
	  MetaForm setFormFieldResources(MetaForm metaForm, List<MetaTableColumns> tableColumns) throws EazyBpmException;
	  
	  /**
	   * 
	   * @return
	   */
	  boolean checkExistingTableColumnRelation(List<String> deleteExistingTableColumns) throws EazyBpmException;
	  /**
	   * To get the list of tables by name
	   * @param tableName
	   * @return
	   * @throws EazyBpmException
	   */
	  List<Map<String, String>> searchModuleTableNames(String tableName) throws EazyBpmException;
	  
	  public List<String> getTableNames(String tableName)throws EazyBpmException;
	  void exportTableSql(String tableId, HttpServletResponse response);
	  String getMysqlImportCommand();

	  Map getDataForUniqueKey(String uniquecolumn, String uniqueKey, String tableName);
	  
	  String getDataBaseSchema()throws EazyBpmException;
}

