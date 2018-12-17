package com.eazytec.bpm.runtime.table.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.runtime.table.dao.TableDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LabelValue;
import com.eazytec.model.MetaForm;
import com.eazytec.model.MetaTable;
import com.eazytec.model.MetaTableColumns;
import com.eazytec.model.MetaTableRelation;

@Repository("tableDao")
@Table(name = "TABLE")
@NamedQueries({
        @NamedQuery(
                name = "findTableByName",
                query = "select r from MetaTable r where r.name = :name "
        )
})
public class TableDaoImpl extends GenericDaoHibernate<MetaTable, String> implements TableDao {
	 private static final long serialVersionUID = 3690197650654049848L;
	 public String dataBaseSchemaName;
	 public String dataBaseSchema;
	 
	 
	 public String getDataBaseSchema() {
		return dataBaseSchema;
	 }

	 public void setDataBaseSchema(String dataBaseSchema) {
		this.dataBaseSchema = dataBaseSchema;
	 }
	 
	 
	 public String getDataBaseSchemaName() {
		return dataBaseSchemaName;
	 }

	 public void setDataBaseSchemaName(String dataBaseSchemaName) {
		this.dataBaseSchemaName = dataBaseSchemaName;
	 }

		/**
		 * Constructor to create a Generics-based version using Group as the entity
		 */
		public TableDaoImpl() {
			super(MetaTable.class);
		}
		
		/**
		 * {@inheritDoc saveTable}
		 */
		public MetaTable saveTable(MetaTable tableOperation) throws EazyBpmException{
			if(tableOperation.getId()==null || tableOperation.getId()=="" ){
				getSession().save(tableOperation);
			}else{
				getSession().update(tableOperation);
			}
			
			// necessary to throw a DataIntegrityViolation and catch it in
			// GroupManager
			getSession().flush();
			return tableOperation;
		}
		
		/**
		 * {@inheritDoc saveFields}
		 */
		public MetaTableColumns saveFields(MetaTableColumns fieldProperty)throws EazyBpmException{
			if(fieldProperty.getId()==null || fieldProperty.getId()==""){
				getSession().save(fieldProperty);
			}else{
				getSession().update(fieldProperty);
			}
			
			// necessary to throw a DataIntegrityViolation and catch it in
			// GroupManager
			return fieldProperty;
		}
		
		
		/**
		 * {@inheritDoc createTable}
		 */
		@Override
		public boolean createTable(String tableQuery) throws EazyBpmException{
			 if(!dataBaseSchema.equalsIgnoreCase("mysql")) {
				 tableQuery = tableQuery.replaceAll("`", "").replaceAll(" as ", " ").replaceAll(" AS ", " ");
			 }
			
		   boolean status=false;
           int exe = 0;
           Query query =null;
           String[] tableQueryString=tableQuery.split(";");
           if(tableQueryString.length>1){
		       for(String exeTableQuery:tableQueryString){
		    	   if(exeTableQuery!= "" && exeTableQuery!=null && exeTableQuery.length() != 0 ){
		    		   query=getSession().createSQLQuery(exeTableQuery);
						exe = query.executeUpdate();
						getSession().flush();
		    	   }
			   }
		   }else{
			   tableQuery = tableQuery.replace(";", "");
			   query=getSession().createSQLQuery(tableQuery);
		       exe = query.executeUpdate();
		       getSession().flush();
		   }
		   
		   if(exe>=0){
			   status=true;
		   }
		   return status;
		}
		
		/**
		 * {@inheritDoc checkTableName}
		 */
		@Override 
		public boolean checkTableName(String tableName)throws EazyBpmException{
			List<MetaTable> tableOperation = getSession().createCriteria(MetaTable.class).add(Restrictions.eq("tableName", tableName)).list();
			
			if (tableOperation.isEmpty()) {
				return false;
			}else{
				return true;
			}
			
		}
		
		/**
		 * {@inheritDoc getTables}
		 */
		@Override 
		public List<MetaTable> getTables()throws EazyBpmException{
			
			Query query = getSession().createQuery("SELECT metaTable FROM MetaTable AS metaTable");
			getSession().flush();
			return query.list();
		}
		
		/**
		 * {@inheritDoc revertBackTableDate}
		 */
		@Override 		
		public void revertBackTableDate(Set<MetaTableColumns> fieldPro,MetaTable tableOperation)throws EazyBpmException{
			for(MetaTableColumns fieldProperty:fieldPro){
				getSession().delete(fieldProperty);
				getSession().flush();
			}
			getSession().delete(tableOperation);
		}
		
		/**
		 * {@inheritDoc deleteTableDetails}
		 */
		@Override 	
		public boolean deleteTableDetails(MetaTable tableOperation)throws EazyBpmException{
			Query query = getSession().createQuery("DELETE FROM MetaTableColumns WHERE table='"+tableOperation.getId()+"'");
			int result = query.executeUpdate();
			query=getSession().createQuery("DELETE FROM MetaTable WHERE id='"+tableOperation.getId()+"'");
			int result1 = query.executeUpdate();
			getSession().flush();
			if(result==0 || result1==0){
				throw new EazyBpmException("Failed in deleting Table "+tableOperation.getTableName());
			}else{
				return true;
			}
		}
		
		/**
		 * {@inheritDoc dropTableFromDataBase}
		 */
		@Override 	
		public boolean dropTableFromDataBase(String tableName)throws EazyBpmException{
			//String dropTable="drop table if exists ETEC_RU_"+tableName;
			String dropTable= "";
			if(!dataBaseSchema.equals("mysql")){
				dropTable = "drop table "+tableName;
			}else{
				dropTable = "drop table if exists "+tableName;
			}
			
			Query query = getSession().createSQLQuery(dropTable);
			int result=query.executeUpdate();
			if(result==0){
				return true;
			}else{
				return false;
			}
		}
		
		/**
		 * {@inheritDoc getTableDetails}
		 */
		public MetaTable getTableDetails(String tableId)throws EazyBpmException{
			List<MetaTable> metaTable = getSession().createCriteria(MetaTable.class).add(Restrictions.eq("id", tableId)).list();
			if (!metaTable.isEmpty()) {
				return metaTable.get(0);
			}else{
				return null;
			}
		}
		
		/**
		 * {@inheritDoc getMetaTableColumnsById}
		 */
		public MetaTableColumns getMetaTableColumnsById(String id)throws EazyBpmException{
			List<MetaTableColumns> metaTableColumns = getSession().createCriteria(MetaTableColumns.class).add(Restrictions.eq("id", id)).list();
			getSession().flush();
			if (!metaTableColumns.isEmpty()) {
				return metaTableColumns.get(0);
			}else{
				return null;
			}
			
		}
		
		/**
		 * {@inheritDoc getAllTable}
		 */
		public List<LabelValue> getAllTable()throws EazyBpmException{
			List<LabelValue> tableList = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(metaTable.tableName as tableName, metaTable.id as id) from MetaTable as metaTable").list();
	    	if(tableList.isEmpty()){ 
	    		return null;
	    	}else{
	    		return tableList;
	    	}
		}
		
		/**
		 * {@inheritDoc getTableWithParentTable}
		 */
		@SuppressWarnings("unchecked")
		public List<LabelValue> getTableWithParentTable(String tableId)throws EazyBpmException{
			List<LabelValue> tableList = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(metaTable.tableName as tableName, metaTable.id as id) from MetaTable as metaTable, MetaTableRelation as metaTableRelation where metaTable.id = metaTableRelation.parentTable and metaTableRelation.table = '"+tableId+"' group by metaTable.id,metaTable.tableName").list();
			getSession().flush();
	    	if(tableList.isEmpty()){ 
	    		return null;
	    	}else{
	    		return tableList;
	    	}
		}
		
		/**
		 * {@inheritDoc getTablesByParentTableId}
		 */
		public List<LabelValue> getTablesByParentTableId(String parentTableId)throws EazyBpmException{
			List<LabelValue> tableList = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(metaTable.tableName as tableName, metaTable.id as id) from MetaTable as metaTable").list();
			getSession().flush();
	    	if(tableList.isEmpty()){ 
	    		return null;
	    	}else{
	    		return tableList;
	    	}
		}
		
		/**
		 * {@inheritDoc}
		 */
		public List<MetaTableColumns> getAllMetaTableColumnsByTableId(String tableId,String removeDefaultValue)throws EazyBpmException {
			List<MetaTableColumns> metaTableColumnsList = (List<MetaTableColumns>) getSession().createQuery("select tableColumns from MetaTableColumns as tableColumns join tableColumns.table as table where table.id = '"+tableId+"' "+removeDefaultValue).list();
			if(metaTableColumnsList != null){
				return metaTableColumnsList;
			} else {
				return null;
			}
		}
		
		/**
		 * {@inheritDoc getAllColumnByTableId}
		 */
		public List<LabelValue> getAllColumnByTableId(String tableId,String removeDefaultValue)throws EazyBpmException{
			List<LabelValue> tableColumnList = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(tableColumns.name as name,tableColumns.id as id) from MetaTableColumns as tableColumns join tableColumns.table as table where table.id = '"+tableId+"' "+removeDefaultValue).list();
			getSession().flush();
	    	if(tableColumnList.isEmpty()){ 
	    		return null;
	    	}else{
	    		return tableColumnList;
	    	}
		}
		
		/**
		 * {@inheritDoc getAllColumnIncludeDefaultByTableId}
		 */
		public List<LabelValue> getAllColumnIncludeDefaultByTableId(String tableId) throws EazyBpmException{
			List<LabelValue> tableColumnList = (List<LabelValue>) getSession().createQuery("select new com.eazytec.model.LabelValue(tableColumns.name as name,tableColumns.id as id) from MetaTableColumns as tableColumns join tableColumns.table as table where table.id = '"+tableId+"' ").list();
			getSession().flush();
	    	if(tableColumnList.isEmpty()){ 
	    		return null;
	    	}else{
	    		return tableColumnList;
	    	}
		}
		
		/**
		 * {@inheritDoc getTableDump}
		 */
		public List<String> getTableDump(String selectAllData)throws EazyBpmException{
			 List<String> results = null;
			 if(!dataBaseSchema.equalsIgnoreCase("mysql")) {
				 selectAllData = selectAllData.replaceAll("`", "").replaceAll(" as ", " ").replaceAll(" AS ", " ");
			 }
	         //Query using HQL and print results
	         Query query =getSession().createSQLQuery(selectAllData);
	         results = query.list();
	         return results;
		}
		
		/**
		 * {@inheritDoc saveTableRelation}
		 */
		public MetaTableRelation saveTableRelation(MetaTableRelation metaTableRelation)throws EazyBpmException{
			
				if(metaTableRelation.getId()==null || metaTableRelation.getId()==""){
					getSession().save(metaTableRelation);
				}else{
					getSession().update(metaTableRelation);
				}
				
				// necessary to throw a DataIntegrityViolation and catch it in
				// GroupManager
				//getSession().flush();
			
			return metaTableRelation;
		}
		
		/**
		 * {@inheritDoc deleteTableRelation}
		 */
		@Override 	
		public void deleteTableRelation(MetaTable tableRelation)throws EazyBpmException{
			Query query = getSession().createQuery("DELETE FROM MetaTableRelation WHERE table='"+tableRelation.getId()+"' OR childTable='"+tableRelation.getId()+"'");
			int result = query.executeUpdate();
			
			if(result==0){
				throw new EazyBpmException("Failed in deleting Table "+tableRelation.getTableName());
			}
		}
		
		/**
		 * {@inheritDoc getChildMetaTableRelation}
		 */
		@Override 	
		public  List<MetaTableRelation> getChildMetaTableRelation(MetaTable tableOperation)throws EazyBpmException{
			Query query = getSession().createQuery("SELECT metaTableRelation FROM MetaTableRelation AS metaTableRelation where metaTableRelation.parentTable='"+tableOperation.getId()+"'");
			getSession().flush();
			return query.list();
		}
		
		/**
		 * {@inheritDoc getParentMetaTableRelation}
		 */
		public  List<MetaTableRelation> getParentMetaTableRelation(MetaTable tableOperation)throws EazyBpmException{
			Query query = getSession().createQuery("SELECT metaTableRelation FROM MetaTableRelation AS metaTableRelation where metaTableRelation.childTable='"+tableOperation.getId()+"'");
			getSession().flush();
			return query.list();
		}
		
		/**
		 * {@inheritDoc}
		 */
		public List<MetaTableRelation> getAllTalbeRelationById(String tableId)throws EazyBpmException{
			Query query = getSession().createQuery("SELECT metaTableRelation FROM MetaTableRelation AS metaTableRelation where metaTableRelation.childTable='"+tableId+"' or metaTableRelation.parentTable='"+tableId+"' or metaTableRelation.table='"+tableId+"'");
			getSession().flush();
			return query.list();
		}
		
		/**
		 * {@inheritDoc dropRelationColumn}
		 */
		@Override 	
		public boolean dropRelationColumn(String chidTableBuffer)throws EazyBpmException{
			boolean status=false;
			if(!dataBaseSchema.equals("mysql")){
				chidTableBuffer = chidTableBuffer.replace("`", " ");
			}
			
           int exe = 0;
           Query query =null;
           String[] chidTableString=chidTableBuffer.split(";");
           if(chidTableString.length!=0){
        	       for(String exeTableQuery:chidTableString){
        	    	   log.info("Droping the Relation Columns :: "+exeTableQuery);
                           query=getSession().createSQLQuery(exeTableQuery);
                           exe = query.executeUpdate();
                   }
           }else{
        	   		log.info("Droping the Relation Columns :: "+chidTableBuffer);
                   query=getSession().createSQLQuery(chidTableBuffer);
                   exe = query.executeUpdate();
           }
           
           	if(exe>=0){
				status=true;
			}
	    	getSession().flush();
	    	return status;
		}
		
		/**
		 * {@inheritDoc dropRelationship}
		 */
		public boolean dropRelationship(MetaTable metaTable,String type){
			String exeQuery = "";
			if(type.equals("parent")){
				log.info("DELETE FROM MetaTableRelation WHERE (table='"+metaTable.getId()+"' AND childTable IS NULL) OR (childTable='"+metaTable.getId()+"' AND parentTable IS NULL)");
				 exeQuery="DELETE FROM MetaTableRelation WHERE (table='"+metaTable.getId()+"' AND childTable IS NULL) OR (childTable='"+metaTable.getId()+"' AND parentTable IS NULL)";
			}else if(type.equals("child")){
				log.info("DELETE FROM MetaTableRelation WHERE (table='"+metaTable.getId()+"' AND parentTable IS NULL) OR (parentTable='"+metaTable.getId()+"' AND childTable IS NULL)");
				 exeQuery="DELETE FROM MetaTableRelation WHERE (table='"+metaTable.getId()+"' AND parentTable IS NULL) OR (parentTable='"+metaTable.getId()+"' AND childTable IS NULL)";
			}
			//drop the parent relation in meta table
			Query query = getSession().createQuery(exeQuery);
			int result = query.executeUpdate();
			
			if(result==0){
				throw new EazyBpmException("Failed in deleting Table ");
			}
			return true;
		}
		
		/**
		 * {@inheritDoc dropParentRelationship}
		 */
		public boolean dropParentRelationship(MetaTable tableObject){
			//drop the parent relation in meta table
			log.info("DELETE FROM MetaTableRelation WHERE parentTable='"+tableObject.getId()+"'");
			Query query = getSession().createQuery("DELETE FROM MetaTableRelation WHERE parentTable='"+tableObject.getId()+"'");
			int result = query.executeUpdate();
			
			if(result==0){
				throw new EazyBpmException("Failed in deleting Table "+tableObject.getTableName());
			}
			return true;
		}
		
		/**
		 * {@inheritDoc isTableInForm}
		 */
		public List<MetaForm> isTableInForm(MetaTable tableOperation)throws EazyBpmException{
			Query query = getSession().createQuery("SELECT metaForm FROM MetaForm AS metaForm where metaForm.table='"+tableOperation.getId()+"'");
			getSession().flush();
			return query.list();
		}
		
		/**
		 * {@inheritDoc getNonRelationTables}
		 */
		public List<MetaTable> getNonRelationTables(String tableId)throws EazyBpmException{
			String tableQuery="SELECT metaTable FROM MetaTable AS metaTable WHERE (metaTable.id  NOT IN " +
					"(SELECT metaRelation.childTable FROM MetaTableRelation as metaRelation WHERE metaRelation.table='"+tableId+"' AND metaRelation.childTable IS NOT NULL )) AND (metaTable.id  NOT IN " +
							"(SELECT metaRelation1.parentTable FROM MetaTableRelation as metaRelation1 WHERE metaRelation1.table='"+tableId+"' AND metaRelation1.parentTable IS NOT NULL ))";
			
			Query query = getSession().createQuery(tableQuery);
			getSession().flush();
			return query.list();
		}
		
		/**
		 * {@inheritDoc getTablesByIds}
		 */
		public List<MetaTable> getTablesByIds(List<String> ids)throws EazyBpmException { 
	    	log.info("Getting MetaTable object of : "+ids);
	    	List<MetaTable> tables = getSession().createQuery("from MetaTable as table where table.id in (:list)").setParameterList("list", ids).list();
	    	getSession().flush();
	        if (tables.isEmpty()) {
	            return null;
	        } else {
	            return tables;
	        }
	    }
		
		
		
		/**
		 * {@inheritDoc getAllColumnsByTableName}
		 */
		public List<String> getAllColumnsByTableName(String tableName)throws EazyBpmException{
			String selectAllData;
			tableName = tableName.toUpperCase();
			if(dataBaseSchema.equalsIgnoreCase("mysql")) {
				selectAllData="SELECT tab.column_name AS columnName FROM information_schema.columns AS tab WHERE tab.table_schema = '"+dataBaseSchemaName+"' AND tab.TABLE_NAME='"+tableName+"'";
			} else if(dataBaseSchema.equalsIgnoreCase("mssql")) {
				selectAllData="SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_CATALOG = '"+dataBaseSchemaName+"' AND TABLE_NAME = '"+tableName+"'";
			} else {
				selectAllData="SELECT COLUMN_NAME FROM ALL_TAB_COLUMNS WHERE OWNER = '"+dataBaseSchemaName+"' AND TABLE_NAME='"+tableName+"'";
			}
			SQLQuery query = getSession().createSQLQuery(selectAllData);
			if(!dataBaseSchema.equalsIgnoreCase("mysql")) {
				query.addScalar("COLUMN_NAME", StringType.INSTANCE);
			}
			getSession().flush();
			return (List<String>) query.list();
		}
		
		/**
		 * {@inheritDoc deleteTableColumnsByIds}
		 */
		@Override 	
		public void deleteTableColumnsByIds(String tableColumnQuery)throws EazyBpmException{
			Query query = getSession().createQuery(tableColumnQuery);
			query.executeUpdate();
		}
		
		 public List<String> getTableDataByTableName(String tableName,String columns)throws EazyBpmException{
			 List<String> results = null;
			 String constructedQuery="";
			 if(columns!=null){
				 constructedQuery="select "+columns+" from `"+tableName+"`";
			 }else{
				 constructedQuery=tableName;
			 }
			 
			 if(!dataBaseSchema.equalsIgnoreCase("mysql")) {
				 constructedQuery = constructedQuery.replaceAll("`", "");
			 }
			 
			 //Query using HQL and print results
			 SQLQuery query =getSession().createSQLQuery(constructedQuery);
			 if(!dataBaseSchema.equalsIgnoreCase("mysql") && columns!=null){
				 String[] columnNames = columns.split(",");
				 for(String columnName :columnNames){
					 query.addScalar(columnName, StringType.INSTANCE);
				 }
			 }
	         results =(List<String>) query.list();
	         return results;
		 }
		 
		 
		 /**
		 * {@inheritDoc getTableDataForExport}
		 */
		@Override 
		 public List getTableDataForExport(String tableName,String columns)throws EazyBpmException{
			 List results = null;
			 String constructedQuery="select "+columns+" from "+tableName+" ";
			 if(!dataBaseSchema.equalsIgnoreCase("mysql")) {
				 constructedQuery = constructedQuery.replaceAll("`", "");
			 }
			 Query query = getSession().createSQLQuery(constructedQuery);
			 results = query.list();
			 return results;
		 }
		 
		 /**
		  *{@inheritDoc}
		 */
		public void executeModuleRelationQuery(String moduleQuery)throws EazyBpmException{
			Query query = getSession().createSQLQuery(moduleQuery);
			query.executeUpdate();
		}
		
		
		public List<MetaTable> getTableDetailsByNames(String tableNames)throws EazyBpmException{
			String tableQuery="SELECT table from MetaTable as table where table.tableName in "+tableNames.replace(";", "");
	    	Query query = getSession().createQuery(tableQuery);
			getSession().flush();
			return query.list();
		}
		
		/**
		  *{@inheritDoc}
		 */
		public List<String> deleteDataByIds(String idList, String tableName)throws EazyBpmException{
			List<String> instIdList = new ArrayList<String>();
			SQLQuery q= getSession().createSQLQuery("select PROC_INST_ID from "+ tableName+" where id in "+idList);
			q.addScalar("PROC_INST_ID",StringType.INSTANCE);
			instIdList = q.list();
			
			String noticeIds="delete from "+tableName+" where id in "+idList;
			Query query= getSession().createSQLQuery(noticeIds);
			query.executeUpdate();
			getSession().flush();
			
			return instIdList;
	    }
		
		/**
		  *{@inheritDoc}
		 */
		public List<String> getTableNamesByProcessIds(String executionId)throws EazyBpmException{
			String tableQuery="SELECT TABLE_NAME_ from ETEC_RE_PROCESS_TABLE_DETAILS where PROC_INST_ID_ = '"+executionId+"'";
			//Query query = getSession().createSQLQuery(tableQuery);
			SQLQuery stringQuery = getSession().createSQLQuery(tableQuery);
			stringQuery.addScalar("TABLE_NAME_",StringType.INSTANCE);
			return stringQuery.list();
		}
		
		/**
		  *{@inheritDoc}
		 */
		public void updateRuntimeTableStatus(String updateQuery)throws EazyBpmException{
			Query query= getSession().createSQLQuery(updateQuery);
			query.executeUpdate();
			getSession().flush();
		}
		
		/**
		  *{@inheritDoc}
		 */
		public void deleteProcessTableDetails(String executionIds)throws EazyBpmException{
			String noticeIds="delete from ETEC_RE_PROCESS_TABLE_DETAILS where PROC_INST_ID_ in "+executionIds;
			Query query= getSession().createSQLQuery(noticeIds);
			query.executeUpdate();
			getSession().flush();
		}
		
		public MetaTable getTableById(String tableId) throws EazyBpmException{
			String tableQuery="SELECT table from MetaTable as table where table.id = '"+tableId+"' ";
	    	Query query = getSession().createQuery(tableQuery);
			if(query.list() != null && query.list().size() > 0){
				return (MetaTable) query.list().get(0);
			}else{
				return null;
			}
			
		}
		
		public List<MetaTableRelation> checkExistingTableColumnRelation(List<String> deleteExistingTableColumns) throws EazyBpmException{
			Query query = getSession().createQuery("SELECT metaTableRelation FROM MetaTableRelation AS metaTableRelation where metaTableRelation.foreignKeyColumnId in (:deleteExistingTableColumns) or " +
					"metaTableRelation.childKeyColumnId in (:deleteExistingTableColumns)").setParameterList("deleteExistingTableColumns", deleteExistingTableColumns) ;
			getSession().flush();
			List<MetaTableRelation> aaa =query.list();
			return aaa;
		}
		
		/**
		 * {@inheritDoc getAllMetaTable}
		 */
		@SuppressWarnings("unchecked")
		public List<String> getAllMetaTable()throws EazyBpmException{
			String selectAllData;
			if(dataBaseSchema.equalsIgnoreCase("mysql")) {
				selectAllData="SELECT tab.TABLE_NAME AS tableName FROM INFORMATION_SCHEMA.TABLES AS tab WHERE tab.table_schema = '"+dataBaseSchemaName+"' AND (tab.TABLE_NAME LIKE 'ETEC_%' OR tab.TABLE_NAME LIKE 'QRTZ_%')";
			} else if(dataBaseSchema.equalsIgnoreCase("mssql")) {
				selectAllData="SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_CATALOG = '"+dataBaseSchemaName+"' AND (TABLE_NAME LIKE 'ETEC_%' OR TABLE_NAME LIKE 'QRTZ_%')";
			} else {
				selectAllData="SELECT TABLE_NAME FROM ALL_TABLES WHERE OWNER = '"+dataBaseSchemaName+"' AND (TABLE_NAME LIKE 'ETEC_%' OR TABLE_NAME LIKE 'QRTZ_%')";
			}
			
			SQLQuery query = getSession().createSQLQuery(selectAllData);
			if(!dataBaseSchema.equalsIgnoreCase("mysql")) {
				query.addScalar("TABLE_NAME", StringType.INSTANCE);
			}
			getSession().flush();
			return (List<String>) query.list();
	    }
		
		
		 public List<String> getTableNames(String tableName)throws EazyBpmException{
			 String getTable  = "SELECT tab.TABLE_NAME FROM INFORMATION_SCHEMA.TABLES AS tab WHERE tab.table_schema = '"+dataBaseSchemaName+"' AND tab.TABLE_NAME like '"+tableName+"%'";//"select new com.eazytec.model.LabelValue(SELECT bb.table_name FROM(SELECT tab.TABLE_NAME FROM INFORMATION_SCHEMA.TABLES AS tab WHERE tab.table_schema = '"+dataBaseSchemaName+"' AND (tab.TABLE_NAME LIKE 'ETEC_%' OR tab.TABLE_NAME LIKE 'QRTZ_%') UNION SELECT aa.table_name FROM ETEC_RE_TABLE AS aa) AS bb WHERE bb.table_name LIKE '"+tableName+"%' );";
		    	/*List<LabelValue> tables = getSession().createQuery(
						"select new com.eazytec.model.LabelValue(metaTable.tableName as tableName,metaTable.id as id) from MetaTable as metaTable where metaTable.tableName like '"+tableName+"%'  order by metaTable.tableName ")
				.list();*/
			 	List<String> tables = getSession().createSQLQuery(getTable)				
				.list();
		    	
		        if (tables.isEmpty()) {
		            return null;
		        } else {
		            return tables;
		        }
		    }
		 
		 public  Map getDataForUniqueKey(String uniquecolumn, String uniqueKey, String tableName)throws EazyBpmException{
		       JdbcTemplate jdbcTemplate =
		                new JdbcTemplate(SessionFactoryUtils.getDataSource(getSessionFactory()));
				 String query = "SELECT * FROM "+tableName+" WHERE "+uniquecolumn+"= '"+uniqueKey+"'";

		       return jdbcTemplate.queryForMap(query);
		 }
		 
		 /**
		 * {@inheritDoc getColumnDefaultConstraint}
		 */
		 public String getColumnDefaultConstraint(String tableName , String columnName)throws EazyBpmException{
			 List<String> columnDefaultConstraints = null;
			 
			 String columnDefaultConstraintQuery = "select d.name columnConstrain from sys.tables t join" +
			 		" sys.default_constraints d on d.parent_object_id = t.object_id  join sys.columns c" +
			 		" on c.object_id = t.object_id and c.column_id = d.parent_column_id where t.name = '"+tableName+"' and c.name = '"+columnName+"'";
			 
			 SQLQuery query =getSession().createSQLQuery(columnDefaultConstraintQuery);
			 query.addScalar("columnConstrain", StringType.INSTANCE);
			 columnDefaultConstraints = query.list();
			
			if (columnDefaultConstraints.isEmpty()) {
				return null;
			} else {
				return String.valueOf(columnDefaultConstraints.get(0));
			}
		 }
		
		
}
