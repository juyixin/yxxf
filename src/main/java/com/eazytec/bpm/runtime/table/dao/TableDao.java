package com.eazytec.bpm.runtime.table.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LabelValue;
import com.eazytec.model.MetaForm;
import com.eazytec.model.MetaTable;
import com.eazytec.model.MetaTableColumns;
import com.eazytec.model.MetaTableRelation;

public interface TableDao {
	
	/**
	 * To save the table details in the db
	 * @param tableOperation
	 * @return TableOperation
	 */
	MetaTable saveTable(MetaTable tableOperation);
	
	/**
	 * To save the table details in the db
	 * @param FieldProperty
	 * @return FieldProperty
	 */
	@Transactional
	MetaTableColumns saveFields(MetaTableColumns fieldProperty);
	
	/**
	 * To create at run time object 
	 * @param tableOperation
	 * @return TableOperation
	 */	
	boolean createTable(String tableQuery)throws EazyBpmException;
	
	/**
	 * To check the table name is already exits .
	 * @param tableName
	 * @return TableOperation
	 */
	boolean checkTableName(String tableName);
	
	/**
	 * To revert back the process if any error occurs 
	 * @param fieldPro
	 * @param tableOperation
	 */
	void revertBackTableDate(Set<MetaTableColumns> fieldPro,MetaTable tableOperation);
	
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
    * To delete the table field properties
    * @param tableRelation
    * @throws EazyBpmException
    */
	boolean deleteTableDetails(MetaTable tableOperation)throws EazyBpmException;
	
	/**
	 * to delete table form database
	 * @param tableName
	 * @return
	 * @throws EazyBpmException
	 */
	boolean dropTableFromDataBase(String tableName)throws EazyBpmException;
	
	/**
	 * To delete the table forgien keys. 
	 * @param tableName
	 * @return
	 * @throws EazyBpmException
	 */
	boolean dropRelationColumn(String chidTableBuffer)throws EazyBpmException;
	
	/**
	 * To delete the table relation. 
	 * @param tableName
	 * @return
	 * @throws EazyBpmException
	 */
	boolean dropRelationship(MetaTable metaTable,String type)throws EazyBpmException;
	
	/**
	 * To delete the table relation. 
	 * @param tableName
	 * @return
	 * @throws EazyBpmException
	 */
	boolean dropParentRelationship(MetaTable metaTable)throws EazyBpmException;
	
	/**
	 * To check weather table is mapped in form 
	 * @param tableName
	 * @return
	 * @throws EazyBpmException
	 */
	List<MetaForm> isTableInForm(MetaTable tableOperation)throws EazyBpmException;
	
	/**
	 * To table details by its id
	 * @param tableId
	 * @return
	 * @throws EazyBpmException
	 */
	MetaTable getTableDetails(String tableId)throws EazyBpmException;
	
	/**
	 * The meta columns details by id
	 * @param tableId
	 * @return
	 * @throws EazyBpmException
	 */
	MetaTableColumns getMetaTableColumnsById(String tableId)throws EazyBpmException;
	
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
	 * To delete the table relationship datas
     * @param tableRelation
     * @throws EazyBpmException
     */
     void deleteTableRelation(MetaTable tableRelation)throws EazyBpmException;
	
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
	List<LabelValue> getAllColumnByTableId(String tableId,String removeDefaultValue)throws EazyBpmException;
	
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
	List<String> getTableDump(String selectTableDatasQuery)throws EazyBpmException;
	
	/**
     * To save the table relation in the db
     * @param MetaTableRelation
     * @return MetaTableRelation
     */
	@Transactional
    MetaTableRelation saveTableRelation(MetaTableRelation metaTableRelation);
    
    /**
     * Get all table child relation
     * @return
     * @throws EazyBpmException
     */
     List<MetaTableRelation> getChildMetaTableRelation(MetaTable tableOperation)throws EazyBpmException;    
    
     /**
 	 * To get the no relational Table list 
 	 * @return
 	 * @throws EazyBpmException
 	 */
 	List<MetaTable> getNonRelationTables(String tableId)throws EazyBpmException;
 	
    /**
     * Get all table parent relation
     * @return
     * @throws EazyBpmException
     */
     List<MetaTableRelation> getParentMetaTableRelation(MetaTable tableOperation)throws EazyBpmException;
     
     List<MetaTable> getTablesByIds(List<String> ids)throws EazyBpmException;
     
     /**
      * get all Meta table
      * @return
      * @throws EazyBpmException
      */
     List<String> getAllMetaTable()throws EazyBpmException;
     
     /**
      * Get all columns Name by table name
      * @param tableName
      * @return
      * @throws EazyBpmException
      */
     List<String> getAllColumnsByTableName(String tableName)throws EazyBpmException;
     
     /**To delete the columns by ids
      * 
      * @param tableColumnQuery
      * @throws EazyBpmException
      */
     void deleteTableColumnsByIds(String tableColumnQuery)throws EazyBpmException;
     
     List<String> getTableDataByTableName(String tableName,String columns)throws EazyBpmException;
     
     List<MetaTableRelation> getAllTalbeRelationById(String tableId)throws EazyBpmException;
     
     void executeModuleRelationQuery(String moduleQuery)throws EazyBpmException;
     
 	/**
 	 * To delete data
 	 * @param idList
 	 * @throws EazyBpmException
 	 */
 	List<String> deleteDataByIds(String idList, String tableName)throws EazyBpmException;
 	
 	/**
 	 * To get the tables mapped in the process
 	 * @param executionId
 	 * @return
 	 * @throws EazyBpmException
 	 */
 	List<String> getTableNamesByProcessIds(String executionId)throws EazyBpmException;
 	
 	/**
 	 * To update the run time table status
 	 * @param updateQuery
 	 * @throws EazyBpmException
 	 */
 	void updateRuntimeTableStatus(String updateQuery)throws EazyBpmException;
 	
 	/**
 	 * To delete the process table details
 	 * @param executionIds
 	 * @throws EazyBpmException
 	 */
 	void deleteProcessTableDetails(String executionIds)throws EazyBpmException;
 	
 	 MetaTable getTableById(String tableId) throws EazyBpmException;
 	 
 	/**
	 * get all meta table columns based on table id
	 * 
	 * @param tableId
	 * @param removeDefaultValue
	 * @return
	 * @throws EazyBpmException
	 */
	 List<MetaTableColumns> getAllMetaTableColumnsByTableId(String tableId,String removeDefaultValue)throws EazyBpmException;
	 
	  /**
	   * 
	   * @return
	   */
	 List<MetaTableRelation> checkExistingTableColumnRelation(List<String> deleteExistingTableColumns) throws EazyBpmException;
	 
	public List<String> getTableNames(String tableName)throws EazyBpmException;

	 Map getDataForUniqueKey(String uniquecolumn, String uniqueKey, String tableName);
	 
	 /**
	  * To get the Column Default Constraint
	  * @param tableName
	  * @param columnName
	  * @return
	  * @throws EazyBpmException
	  */
	 String getColumnDefaultConstraint(String tableName , String columnName)throws EazyBpmException;
	 
	 /**
	  * To export table data
	  * @param tableName
	  * @param selectColumn
	  * @return
	  * @throws EazyBpmException
	  */
	 List getTableDataForExport(String tableName,String selectColumn)throws EazyBpmException;
}
