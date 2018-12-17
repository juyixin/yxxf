package org.activiti.engine.impl.persistence.entity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.impl.RtQueryImpl;
import org.activiti.engine.impl.persistence.AbstractManager;
import org.activiti.engine.task.Task;
import org.apache.ibatis.exceptions.PersistenceException;

import com.eazytec.exceptions.DataSourceException;
import com.eazytec.model.MetaTable;
import com.eazytec.model.MetaTableColumns;


/**
 * @author madan
 */
public class TableEntityManager extends AbstractManager {

  @SuppressWarnings("unchecked")
  public MetaTable selectTableById(String tableId) {
    List<MetaTable> tables = getDbSqlSession().selectList("selectTableById", tableId);
    if(tables.size()>0){
    	return tables.get(0);
    }else{
    	return null;
    }
  }
  
  public MetaTable selectTableByFormId(String formId) {
	    List<MetaTable> tables = getDbSqlSession().selectList("selectTableByFormId", formId);
	    if(tables.size()>0){
	    	return tables.get(0);
	    }else{
	    	return null;
	    }
	  }
  
  public MetaTableColumns selectTableColumnById(String columnId) {
	    List<MetaTableColumns> columns = getDbSqlSession().selectList("selectColumnById", columnId);
	    if(columns.size()>0){
	    	return columns.get(0);
	    }else{
	    	return null;
	    }
	  }
  
  public MetaTableColumns selectTableColumnByName(String columnName) {
	    List<MetaTableColumns> columns = getDbSqlSession().selectList("selectColumnByName", columnName);
	    if(columns.size()>0){
	    	return columns.get(0);
	    }else{
	    	return null;
	    }
	  }
  
  public MetaTableColumns updateTableColumnIdNumberById(String id) {
	    List<MetaTableColumns> metaTableColumns = getDbSqlSession().selectList("updateColumnIdNumber", id);
	    if(metaTableColumns.size()>0){
	    	return metaTableColumns.get(0);
	    }else{
	    	return null;
	    }
	  }

  
  @SuppressWarnings("unchecked")
  public List<Task> storeRtValues(RtQueryImpl rtQuery) {
	try{
		final String query = "insertValues";
	    return getDbSqlSession().selectList(query, rtQuery);
	}catch(PersistenceException e){
		if (e.getCause() instanceof SQLException) {
	        SQLException cause = (SQLException) e.getCause();
	        if ("23000".equals(cause.getSQLState())) {
	            throw new DataSourceException(cause.getMessage(),cause);
	        } else {
	            throw new DataSourceException(cause.getMessage(),cause); // Oops, not our exception.
	        }
	    } else if(e.getCause() instanceof SQLException){
	        SQLException cause = (SQLException) e.getCause();
				throw new DataSourceException(cause.getMessage(), cause);
	    } else {
	        throw new DataSourceException(e.getMessage(),e); // Oops, not our exception.
	    }
	}
  }
  
  @SuppressWarnings("unchecked")
  public List<Task> storeRtValues(Map<String, Object> rtQuery)  {
	try{
		String query = "insertValuesFromMap";		
	    return getDbSqlSession().selectList(query, rtQuery);
	}catch(PersistenceException e){
		if (e.getCause() instanceof SQLException) {
	        SQLException cause = (SQLException) e.getCause();
	        if ("23000".equals(cause.getSQLState())) {
	            throw new DataSourceException(cause.getMessage(),cause);
	        }else if ("72000".equals(cause.getSQLState())) {
	            throw new DataSourceException(cause.getMessage().trim().replaceAll("\"",""),cause);
	        }else {
	            throw new DataSourceException(cause.getMessage(),cause); // Oops, not our exception.
	        }
	    } else if(e.getCause() instanceof SQLException){
	        SQLException cause = (SQLException) e.getCause();
				throw new DataSourceException(cause.getMessage(), cause);
	    } else {
	        throw new DataSourceException(e.getMessage(),e); // Oops, not our exception.
	    }
	}
  }
  
  @SuppressWarnings("unchecked")
  public void storeProcessTableValues(Map<String, Object> rtQuery) {
	try{
		String query = "insertProcessTableValues";		
	    getDbSqlSession().selectList(query, rtQuery);
	}catch(PersistenceException e){
		throw new DataSourceException(e.getMessage(),e);
	}
  }
  
  public boolean getRtValues(Map<String, Object> rtQuery) throws DataSourceException{
	try{
		String query = "getValuesForPrimaryKey";
		if(getDbSqlSession().selectList(query, rtQuery).size() != 0){
			//Throw a exception if primary key does not unique
			if(getDbSqlSession().selectList(query, rtQuery).size() >1){
				throw new DataSourceException("Primary key "+rtQuery.get("primaryKey")+" does not have unique key value");
			}
			return true;
		}else{
			return false;
		}
	    
	}catch(PersistenceException e){
		throw new DataSourceException(e.getMessage(),e);
	}    
  }
  
  public List<String> getRtValuesForJavaEvent(Map<String, Object> rtQuery) throws DataSourceException{
	try{
		String query = "getValuesForPrimaryKey";
		return getDbSqlSession().selectList(query, rtQuery);
	}catch(PersistenceException e){
		throw new DataSourceException(e.getMessage(),e);
	}
  }
  
  public boolean updateRtValues(Map<String, Object> rtQuery)  {
		try{
			String query = "updateValues";
			List<String> columns = (List<String>) rtQuery.get("columns");
	        List<String>fieldValues = (List<String>) rtQuery.get("fieldValues");
	        Map<String,Object> columnWithValue = new HashMap<String,Object>();
	        List<String> update = new ArrayList<String>();
	        for(int i=0;i<columns.size();i++){
	        	if(!fieldValues.get(i).equalsIgnoreCase("NULL") && !columns.get(i).equalsIgnoreCase("id") && !columns.get(i).equalsIgnoreCase("CREATEDTIME") && 
	        			!columns.get(i).equalsIgnoreCase("CREATEDBY") && !columns.get(i).equalsIgnoreCase("PROC_INST_ID") && !columns.get(i).equalsIgnoreCase("EXECUTION_ID")){
		        	columnWithValue.put(columns.get(i), fieldValues.get(i).replaceAll("'", ""));
		        	update.add(columns.get(i)+"="+fieldValues.get(i));
	        	}else {
		        	columnWithValue.put(columns.get(i), null);
	        	}
	        }
	        columnWithValue.remove("ID");
	        columnWithValue.remove("`CREATEDTIME`");
	        columnWithValue.remove("`CREATEDBY`");
	        columnWithValue.remove("PROC_INST_ID");
	        columnWithValue.remove("EXECUTION_ID");
	        rtQuery.put("columns", columnWithValue.keySet());
	        rtQuery.putAll(columnWithValue);
	        rtQuery.put("bulkInsert",update);
			if(getDbSqlSession().selectList(query, rtQuery).size() != 0){
				return true;
			}else{
				return false;
			}
		    
		}catch(PersistenceException e){
			
			if (e.getCause() instanceof SQLException) {
		        SQLException cause = (SQLException) e.getCause();
		        if ("23000".equals(cause.getSQLState())) {
		            throw new DataSourceException(cause.getMessage(),cause);
		        } else {
		            throw new DataSourceException(cause.getMessage(),cause); // Oops, not our exception.
		        }
		    }  else if(e.getCause() instanceof SQLException){
		        SQLException cause = (SQLException) e.getCause();
					throw new DataSourceException(cause.getMessage(), cause);
		    } else {
		        throw new DataSourceException(e.getMessage(),e); // Oops, not our exception.
		    }
			//throw new DataSourceException(e.getMessage(),e);
		}    
	  }
  @SuppressWarnings("unchecked")
  public List<Map<String,Object>> selectForeignKeyByTableId(Map<String,Object> tableInfo) {
	  String query = "selectForeignKeyByTableId";
	  List<Map<String,Object>> foreignKeys = (List<Map<String,Object>>)getDbSqlSession().selectList(query, tableInfo);
	  return foreignKeys;
  }
  
  @SuppressWarnings("unchecked")
  public List<Task> storeRtSubValues(Map<String, Object> rtQuery) {
	try {
		String query = "insertSubFormValuesFromMap";	
		String queryColumns = rtQuery.get("columns").toString().replace("[", "(").replace("]", ")");
		rtQuery.put("bulkInsert","INTO "+rtQuery.get("tableName")+" "+queryColumns+" values");
	    return getDbSqlSession().selectList(query, rtQuery);
	}catch(PersistenceException e){
		if (e.getCause() instanceof SQLException) {
	        SQLException cause = (SQLException) e.getCause();
	        if ("23000".equals(cause.getSQLState())) {
	            throw new DataSourceException(cause.getMessage(),cause);
	        } else {
	            throw new DataSourceException(cause.getMessage(),cause); // Oops, not our exception.
	        }
	    } else if(e.getCause() instanceof SQLException){
	        SQLException cause = (SQLException) e.getCause();
				throw new DataSourceException(
						"Relationship exists in Table : "+ rtQuery.get("tableName")+ " So Field Cannot be Empty Or Invalid Data type set in Relationship "
							+ "Caused By : " + cause.getMessage(), cause);
	    } else {
	    	throw new DataSourceException(e.getMessage(),e); // Oops, not our exception.
	    }
	} 
  }
  
  public boolean updateRtSubFormValues(Map<String, Object> rtQuery) {
	try{
		String query = "updateSubFormValues";
		List<String> columns = (List<String>) rtQuery.get("columns");
		Set<List<String>>fieldValuesList = (Set<List<String>>)rtQuery.get("fieldValues");
        Map<String,String> columnWithValue=null;
        List<Map<String,String>> columnsWithValue = new ArrayList<Map<String,String>>();
        for (List<String> fieldValues : fieldValuesList) {
        	columnWithValue = new HashMap<String, String>();
        	for(int i=0;i<columns.size();i++){
	        	if(!fieldValues.get(i).equalsIgnoreCase("NULL")){
		        	columnWithValue.put(columns.get(i), fieldValues.get(i));
	        	}else {
		        	columnWithValue.put(columns.get(i), null);
	        	}
	        }
        	columnWithValue.remove("ID");
	        columnWithValue.remove("CREATEDTIME");
	        columnWithValue.remove("CREATEDBY");
	        columnWithValue.remove("PROC_INST_ID");
	        columnWithValue.remove("EXECUTION_ID");
        	columnsWithValue.add(columnWithValue);
		}
        // The recent map will still have values
        rtQuery.put("columns", columnWithValue.keySet());
        rtQuery.put("columnsWithValue", columnsWithValue);
        //rtQuery.putAll(columnWithValue);
		if(getDbSqlSession().selectList(query, rtQuery).size() != 0){
			return true;
		}else{
			return false;
		}	    
	}catch(PersistenceException e){
		if (e.getCause() instanceof SQLException) {
	        SQLException cause = (SQLException) e.getCause();
	        if ("23000".equals(cause.getSQLState())) {
	            throw new DataSourceException(cause.getMessage(),cause);
	        } else {
	            throw new DataSourceException(cause.getMessage(),cause); // Oops, not our exception.
	        }
	    }   else if(e.getCause() instanceof SQLException){
	        SQLException cause = (SQLException) e.getCause();
				throw new DataSourceException(
						"Relationship exists in Table : "
								+ rtQuery.get("tableName")
								+ " So Field Cannot be Empty Or Invalid Data type set in Relationship "
								+ "Caused By : " + cause.getMessage(), cause);
	    } else {
	        throw new DataSourceException(e.getMessage(),e); // Oops, not our exception.
	    }
	}    
  }

  public long findTaskCountByQueryCriteria(RtQueryImpl rtQuery) {
    return (Long) getDbSqlSession().selectOne("insertValues", rtQuery);
  }

  public List<Map<String, Object>> getFormFieldTraceData(Map<String,Object>  parameter) {
	try{
	    return (List<Map<String, Object>>)getDbSqlSession().selectList("getFormFieldTraceData", parameter);
	}catch(PersistenceException e){
		throw new DataSourceException(e.getMessage(),e);
	}
  }

public void deleteRtValues(String tableName, String primaryKey, String primaryKeyVal) {
	Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("tableName", tableName);
    parameters.put("primaryKey", primaryKey);
    parameters.put("primaryKeyVal",primaryKeyVal );
    getDbSqlSession().delete("deleteValues", parameters);
    
}


public void deleteRtSubFormValues(String tableName, String foreignKeyName, String primaryKeyVal) {
	Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("tableName", tableName);
    parameters.put("foreignKeyName", foreignKeyName);
    parameters.put("primaryKeyVal",primaryKeyVal );
    getDbSqlSession().selectList("deleteSubFormValues", parameters);
}

  
}

