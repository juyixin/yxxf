package com.eazytec.bpm.runtime.table.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.admin.module.service.ModuleService;
import com.eazytec.bpm.runtime.table.dao.TableDao;
import com.eazytec.bpm.runtime.table.service.TableService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.common.util.MetaTableColumnComparator;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LabelValue;
import com.eazytec.model.MetaForm;
import com.eazytec.model.MetaTable;
import com.eazytec.model.MetaTableColumns;
import com.eazytec.model.MetaTableRelation;
import com.eazytec.model.Module;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.util.DateUtil;

import au.com.bytecode.opencsv.CSVReader;

@Service("tableService")
public class TableServiceImpl implements TableService {
	
	private TableDao tableDao;
	private ModuleService moduleService;
    private RepositoryService repositoryService;
	public VelocityEngine velocityEngine;
	
	@Autowired
	private HistoryService historyService;
	public String mysqlExportCommand;
	public String mysqlImportCommand;
	public String dataBaseSchemaName;
	public String dataBaseSchema;
	public String getDataBaseSchemaName() {
		return dataBaseSchemaName;
	}

	public void setDataBaseSchemaName(String dataBaseSchemaName) {
		this.dataBaseSchemaName = dataBaseSchemaName;
	}
	
	public String getDataBaseSchema() {
		return dataBaseSchema;
	}

	public void setDataBaseSchema(String dataBaseSchema) {
		this.dataBaseSchema = dataBaseSchema;
	}

	public String getMysqlExportCommand() {
		return mysqlExportCommand;
	}

	public String getMysqlImportCommand() {
		return mysqlImportCommand;
	}

	public void setMysqlImportCommand(String mysqlImportCommand) {
		this.mysqlImportCommand = mysqlImportCommand;
	}

	public void setMysqlExportCommand(String mysqlExportCommand) {
		this.mysqlExportCommand = mysqlExportCommand;
	}

	@Autowired
	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}	
	
	@Autowired
    public void setTableDao(TableDao tableDao) {
	    this.tableDao = tableDao;
    }
	
	
	@Autowired
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }


	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    
    /**
     * To create a set of MetaTableColumns
     * @param fieldProperties
     * @param tableOperation
     * @return
     * @throws EazyBpmException
     */
    public Set<MetaTableColumns> getMetaFieldProperty(List<Map<String,Object>> fieldProperties,MetaTable tableOperation)throws EazyBpmException{
    	Set<MetaTableColumns> fieldPro = new TreeSet<MetaTableColumns>(new MetaTableColumnComparator());//使用TreeSet处理字段的排序
    	for(Map<String,Object> formFields : fieldProperties){
    		    Integer orderNum= Integer.valueOf(formFields.get("orderNum").toString());
				String fieldName=(String)formFields.get("columnName");
				String fieldChineseName=(String)formFields.get("columnChineseName");
				String fieldType=(String)formFields.get("columnType");
				String fieldLength="0";
				if(fieldLength!="" && !fieldLength.isEmpty()){
					fieldLength=(String)formFields.get("columnSize");
				}
				String defaultValue=(String)formFields.get("defaultValue");
				String autoGenerationId=(String)formFields.get("autoGenerationId");
				String id=(String)formFields.get("id");
				String uniqueKey=(String)formFields.get("isUniqueKey");
				String compositeKey=(String)formFields.get("isCompositeKey");
				boolean isUniqueKey=false;
				if(uniqueKey.equals("true")){
					isUniqueKey=true;
				}
				
				boolean isCompositeKey=false;
				if(compositeKey != null && compositeKey.equals("true")){
					isCompositeKey=true;
				}
				MetaTableColumns fieldProperty=null;
				
				if(fieldName!="" && !fieldName.isEmpty()){
					fieldProperty=new MetaTableColumns(orderNum,fieldName,fieldChineseName,fieldType,fieldLength,defaultValue,autoGenerationId,isUniqueKey,isCompositeKey);
					if(id!=null && !id.equalsIgnoreCase("null")){
						fieldProperty.setId(id);
					}
					fieldProperty.setTable(tableOperation);
					fieldPro.add(fieldProperty);
				}
		}
    	return fieldPro;
    }
    
    
	/**
	 * {@inheritDoc createTable}
	 * @throws Exception 
	 */
    public String createTable(MetaTable tableOperation,List<Map<String,Object>> fieldProperties,List<Map<String,Object>> parentTableList,List<Map<String,Object>> subTableList,boolean isUpdate,List<String> deleteExistingTableColumns,Module module) throws EazyBpmException{
		String table_Name=tableOperation.getTableName().replaceAll(" ", "_");
		Set<MetaTableColumns> field_Properties=new HashSet<MetaTableColumns>();
		Set<MetaTableRelation> parent_Table=new HashSet<MetaTableRelation>();
		Set<MetaTableRelation> child_Table=new HashSet<MetaTableRelation>();
		
		tableOperation.setTableName(table_Name);
		MetaTable tempMetaTable=getTableDetails(tableOperation.getId());
		
		if(isUpdate){
			Set<Module> oldModules=tempMetaTable.getModules();
			if(!oldModules.contains(module)){
				tempMetaTable.addModule(module);
			}
			tempMetaTable.setChineseName(tableOperation.getChineseName());
		    tempMetaTable.setDescription(tableOperation.getDescription());
		    tempMetaTable.setIsAutoFormCreate(tableOperation.getIsAutoFormCreate());
			tableDao.saveTable(tempMetaTable);
		}else{
			tableOperation.addModule(module);
			tableDao.saveTable(tableOperation);
		}
		
		field_Properties=getMetaFieldProperty(fieldProperties,tableOperation);
		parent_Table=getMetTableRelation(tableOperation,parentTableList,true);
		child_Table=getMetTableRelation(tableOperation,subTableList,false);
		parent_Table.addAll(child_Table);
		tableOperation.setMetaTableColumns(field_Properties);
		tableOperation.setMetaTableRelation(parent_Table);
		
		if(field_Properties!=null){
			String tableQuery="";
			if(!isUpdate){
				tableQuery=constructQuery(tableOperation);
				if(tableDao.createTable(tableQuery)){
					tableDao.saveTable(tableOperation);
					saveFieldPropertys(field_Properties);
					saveTableRelation(parent_Table);
					saveTableRelation(child_Table);
					//log.info("Table 203 "+tableOperation.getTableName()+" is created successfully ");
				}
			}else{
				tableQuery=constructAlterQuery(tableOperation,false,deleteExistingTableColumns);
				if(tableQuery!="" && !tableQuery.isEmpty() && !tableQuery.equals("No Changes has been done for Query Preview")){
					try {
						if(tableDao.createTable(tableQuery)){
							tableDao.saveTable(tempMetaTable);
							//log.info("Table "+tempMetaTable.getTableName()+" is altered successfully ");
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw new EazyBpmException("Foreign key cannot be generated, check the mapped field is a primary key."+e.getMessage(),e);
					}
				}
			}
		}
	
		return tableOperation.getId();
	}
    
    /**
     * To remove the columns for the meta table by it's id
     * @param List<String> deleteExistingTableColumns
     */
    public void deleteExistingTableColumnsByIds(List<String> deleteExistingTableColumns){
    	StringBuffer strbuf=new StringBuffer();
    	strbuf.append("DELETE FROM MetaTableColumns WHERE id in (");
    	for(String tableColumnsId :deleteExistingTableColumns){
    		strbuf.append("'"+tableColumnsId+"',");
    	}
    	
    	if(strbuf.lastIndexOf(",")>0){
			strbuf.deleteCharAt(strbuf.lastIndexOf(","));
		}
    	strbuf.append(")");
    	//log.info("Delete Existing Column Query :: "+strbuf.toString());
    	tableDao.deleteTableColumnsByIds(strbuf.toString());
    }
    
    
    /**
     * To create MetaTableRelation set
     * @param currentTable
     * @param relationTableList
     * @param isParentTable
     * @return
     */
	 private Set<MetaTableRelation> getMetTableRelation(MetaTable currentTable,List<Map<String,Object>> relationTableList,boolean isParentTable)throws EazyBpmException{
		 Set<MetaTableRelation> tableRelation=new HashSet<MetaTableRelation>();
			for(Map<String,Object> tableRelationFields : relationTableList){
				String tableId = tableRelationFields.get("tableId").toString();
				String tableName = tableRelationFields.get("tableName").toString();
				String foreignKeyColumnId = tableRelationFields.get("foreignKeyColumnId").toString();
				String foreignKeyColumnName = tableRelationFields.get("foreignKeyColumnName").toString();
				String childKeyColumnid = tableRelationFields.get("childKeyColumnid").toString();
				String childKeyColumnName = tableRelationFields.get("childKeyColumnName").toString();
				
				MetaTableRelation metaTableRelation1=null;
				MetaTableRelation metaTableRelation2=null;
				if(isParentTable){
					String parentTableId=tableId;
					MetaTable parentTable=tableDao.getTableDetails(parentTableId);
					metaTableRelation1=new MetaTableRelation(parentTable,null,currentTable,foreignKeyColumnId,foreignKeyColumnName,childKeyColumnid,childKeyColumnName);
					metaTableRelation2=new MetaTableRelation(currentTable,parentTable,null,foreignKeyColumnId,foreignKeyColumnName,childKeyColumnid,childKeyColumnName);
				}else{
					String childTableId=tableId;
					MetaTable childTable=tableDao.getTableDetails(childTableId);
					metaTableRelation1=new MetaTableRelation(currentTable,null,childTable,foreignKeyColumnId,foreignKeyColumnName,childKeyColumnid,childKeyColumnName);
					metaTableRelation2=new MetaTableRelation(childTable,currentTable,null,foreignKeyColumnId,foreignKeyColumnName,childKeyColumnid,childKeyColumnName);
					
				}
				tableRelation.add(metaTableRelation1);
				tableRelation.add(metaTableRelation2);
			}
			return tableRelation;
	}

	 /**
	  * save the Table Relation
	  * @param tableRelation
	  */
	private void saveTableRelation(Set<MetaTableRelation> tableRelation){
		for(MetaTableRelation metaTableRelation : tableRelation){
			//saving in Field Property table
			tableDao.saveTableRelation(metaTableRelation);
		}
	}
	
	/**
	 * To save the field property
	 * @param fieldPro
	 */
	public void saveFieldPropertys(Set<MetaTableColumns> fieldPro){
		for(MetaTableColumns fieldProperty : fieldPro){
			if(dataBaseSchema.equals("mssql") && (fieldProperty.getType().equals("TINYINT") || fieldProperty.getType().equals("SMALLINT") || fieldProperty.getType().equals("INT"))){
				fieldProperty.setLength("0");
				//fieldProperty.setDefaultValue("NULL");
			}
				
			//saving in Field Property table
			tableDao.saveFields(fieldProperty);
		}
	}
	
	/**
	 * To constructed the sql query from TableOperation
	 * @param tableOperation
	 * @return
	 */
	public String constructQuery(MetaTable tableOperation)throws EazyBpmException{
		StringBuffer strbuf = new StringBuffer();
		StringBuffer childTableQuery = new StringBuffer();
		StringBuffer uniqueKeyQuery = new StringBuffer();
		StringBuffer uniqueConstraint = new StringBuffer();

		String tableQuery="CREATE TABLE `"+tableOperation.getTableName()+"`(";
		strbuf.append(tableQuery);
		
		Set<MetaTableColumns> fieldProperty=tableOperation.getMetaTableColumns();
		for(MetaTableColumns fieldPropertyObj:fieldProperty){
			strbuf.append("`"+fieldPropertyObj.getName()+"` "+fieldPropertyObj.getType());
			
			if(fieldPropertyObj.getType().equals("DECIMAL") || fieldPropertyObj.getType().equals("NUMERIC") || fieldPropertyObj.getType().equals("DOUBLE")){
					String fieldLengthArray[]=fieldPropertyObj.getLength().split(",");
					int fieldLength=0;
					int decimalLength=0;
					
					fieldLength=Integer.parseInt(fieldLengthArray[0]);
					if(fieldLengthArray.length>=2){
						decimalLength=Integer.parseInt(fieldLengthArray[1]);
					}
					
				strbuf.append(" ("+fieldLength+","+decimalLength+")");
				if(fieldPropertyObj.getDefaultValue().equalsIgnoreCase("NULL")){
					strbuf.append(" DEFAULT "+fieldPropertyObj.getDefaultValue()+" ,");
				}else{
					strbuf.append(" DEFAULT '"+fieldPropertyObj.getDefaultValue()+"' ,");
				}
			}else if(dataBaseSchema.equals("mssql") && (fieldPropertyObj.getType().equals("TINYINT") || fieldPropertyObj.getType().equals("SMALLINT") || fieldPropertyObj.getType().equals("INT") || fieldPropertyObj.getType().equals("NVARCHAR"))){
				if(fieldPropertyObj.getType().equals("NVARCHAR")){
					if(fieldPropertyObj.getLength()!="0"){
						strbuf.append(" ("+fieldPropertyObj.getLength()+") ");
					}else{
						strbuf.append(" ("+25+") ");
					}
					
					strbuf.append(" DEFAULT ('"+fieldPropertyObj.getDefaultValue()+"') ,");
				}else{
					if(!fieldPropertyObj.getDefaultValue().equalsIgnoreCase("NULL")){
						if(fieldPropertyObj.getDefaultValue().equalsIgnoreCase("true") || fieldPropertyObj.getDefaultValue().equalsIgnoreCase("1")){
							fieldPropertyObj.setDefaultValue("1");
						}else{
							fieldPropertyObj.setDefaultValue("0");
						}
						strbuf.append(" DEFAULT '"+fieldPropertyObj.getDefaultValue()+"' ,");
					} else {
						strbuf.append(",");
					}
				}
			}else if (fieldPropertyObj.getType().equals("TIMESTAMP")){
				if(dataBaseSchema.equals("mysql")){
					strbuf.append(" NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,");
				}else if(dataBaseSchema.equals("oracle")){
					strbuf.append(" "+fieldPropertyObj.getDefaultValue()+" ,");
				}
			}else if(Integer.parseInt(fieldPropertyObj.getLength())==0){
				if(fieldPropertyObj.getType().equals("BOOLEAN")){
					if(!fieldPropertyObj.getDefaultValue().equalsIgnoreCase("NULL")){
						if(fieldPropertyObj.getDefaultValue().equalsIgnoreCase("true") || fieldPropertyObj.getDefaultValue().equalsIgnoreCase("1")){
							fieldPropertyObj.setDefaultValue("1");
						}else{
							fieldPropertyObj.setDefaultValue("0");
						}
						strbuf.append(" DEFAULT '"+fieldPropertyObj.getDefaultValue()+"' ,");
					} else {
						strbuf.append(",");
					}
				}else if(fieldPropertyObj.getType().equalsIgnoreCase("TIME")){
					if(fieldPropertyObj.getDefaultValue() != null && !fieldPropertyObj.getDefaultValue().equalsIgnoreCase("NULL") && fieldPropertyObj.getDefaultValue().length() > 6){
						strbuf.append(" DEFAULT '"+fieldPropertyObj.getDefaultValue()+"' ,");
					} else {
						strbuf.append(" DEFAULT '00:00:00' ,");
					}
				}else{
					if(fieldPropertyObj.getDefaultValue() != null && !fieldPropertyObj.getDefaultValue().equalsIgnoreCase("NULL") && !fieldPropertyObj.getDefaultValue().isEmpty() && fieldPropertyObj.getDefaultValue().length() > 0){
						strbuf.append(" DEFAULT '"+fieldPropertyObj.getDefaultValue()+"' ,");
					} else {
						strbuf.append(",");
					}
				}
			}else if(fieldPropertyObj.getDefaultValue().equalsIgnoreCase("NOTNULL")){
				strbuf.append(" ("+fieldPropertyObj.getLength()+") NOT NULL, ");
			}else{
				
				if(!fieldPropertyObj.getDefaultValue().equalsIgnoreCase("NULL")){
				//	strbuf.append(" DEFAULT "+fieldPropertyObj.getDefaultValue()+" ,");
				//}else{
					strbuf.append(" ("+fieldPropertyObj.getLength()+") ");
					strbuf.append(" DEFAULT '"+fieldPropertyObj.getDefaultValue()+"' ,");
				} else {
					strbuf.append(" ("+fieldPropertyObj.getLength()+") ,");
				}
			}
			
			//CONSTRAINT UK_"+tableOperation.getTableName()+"_"+fieldPropertyObj.getName()+
			if((fieldPropertyObj.getIsUniquekey() || fieldPropertyObj.getIsCompositeKey())&& !fieldPropertyObj.getName().equals("ID")){
				if(!dataBaseSchema.equals("mysql")){
					if(fieldPropertyObj.getIsUniquekey()){
						uniqueConstraint.append(";ALTER TABLE "+tableOperation.getTableName()+" ADD CONSTRAINT UK_"+tableOperation.getTableName()+"_"+fieldPropertyObj.getName()+"  UNIQUE ("+fieldPropertyObj.getName()+")");
					}else{
						uniqueConstraint.append(";ALTER TABLE "+tableOperation.getTableName()+" ADD CONSTRAINT UK_"+tableOperation.getTableName()+"_"+fieldPropertyObj.getName()+"  UNIQUE ("+fieldPropertyObj.getName()+",TASK_ID,PROC_INST_ID)");
					}
				//}else if(dataBaseSchema.equals("mysql")){
				}else{
					uniqueKeyQuery.append(",CONSTRAINT UK_"+tableOperation.getTableName()+"_"+fieldPropertyObj.getName()+" UNIQUE ("+fieldPropertyObj.getName()+",PROC_INST_ID,TASK_ID)");
				}
			}
		}
		
		/*if(!dataBaseSchema.equals("mysql")){
			uniqueKeyQuery.append(",CONSTRAINT UK_"+tableOperation.getTableName()+"_PROC_INST_ID UNIQUE (PROC_INST_ID)");
			uniqueKeyQuery.append(",CONSTRAINT UK_"+tableOperation.getTableName()+"_TASK_ID UNIQUE (TASK_ID)");
		}*/
	
		Set<MetaTableRelation> metaTableRelation=tableOperation.getMetaTableRelation();
		if(!metaTableRelation.isEmpty()){
			StringBuffer foreignKeyBuffer = new StringBuffer();
			for(MetaTableRelation tableRelation:metaTableRelation){
				if(tableRelation.getParentTable()!=null  && tableOperation.getId()!=tableRelation.getParentTable().getId()){
					String parentTableName=tableRelation.getParentTable().getTableName();
					String foreignKeyColumnName = tableRelation.getForeignKeyColumnName();
					String childKeyColumnName=tableRelation.getChildKeyColumnName();
					//strbuf.append(parentTableName+"_"+foreignKeyColumnName+" VARCHAR (100) DEFAULT NULL,");
					//foreignKeyBuffer.append(",CONSTRAINT fk_"+parentTableName+"_"+foreignKeyColumnName+"_"+tableOperation.getTableName()+" FOREIGN KEY ("+parentTableName+"_"+foreignKeyColumnName+") REFERENCES ETEC_RU_"+parentTableName+"("+foreignKeyColumnName+")");
					foreignKeyBuffer.append(",CONSTRAINT fk_"+parentTableName+"_"+foreignKeyColumnName+"_"+tableOperation.getTableName()+" FOREIGN KEY ("+childKeyColumnName+") REFERENCES "+parentTableName+"("+foreignKeyColumnName+")");
				}else if(tableRelation.getChildTable()!=null && tableOperation.getId()!=tableRelation.getChildTable().getId() ){
					String childTableName=tableRelation.getChildTable().getTableName();
					String foreignKeyColumnName = tableRelation.getForeignKeyColumnName();
					String parentTableName=tableOperation.getTableName();
					String childKeyColumnName=tableRelation.getChildKeyColumnName();
					//childTableQuery.append(";ALTER TABLE ETEC_RU_"+childTableName+" ADD "+parentTableName+"_"+foreignKeyColumnName+" VARCHAR (100) DEFAULT NULL ,ADD CONSTRAINT fk_"+parentTableName+"_"+foreignKeyColumnName+"_"+childTableName+" FOREIGN KEY ("+parentTableName+"_"+foreignKeyColumnName+") REFERENCES ETEC_RU_"+parentTableName+" ("+foreignKeyColumnName+")");
					childTableQuery.append(";ALTER TABLE `"+childTableName+"` ADD CONSTRAINT fk_"+parentTableName+"_"+foreignKeyColumnName+"_"+childTableName+" FOREIGN KEY ("+childKeyColumnName+") REFERENCES "+parentTableName+" ("+foreignKeyColumnName+")");
				}
			}
			strbuf.append(" PRIMARY KEY (ID)");
			if(uniqueKeyQuery.length()>0){
				strbuf.append(uniqueKeyQuery);
			}
			strbuf.append(foreignKeyBuffer+")"+childTableQuery);
		}else{
			strbuf.append(" PRIMARY KEY (ID)");
			if(uniqueKeyQuery.length()>0){
				strbuf.append(uniqueKeyQuery);
			}
			strbuf.append(")");
		}
		// unique constaint for multiple columns
		if(uniqueConstraint.length() > 0) {
			strbuf.append(uniqueConstraint);
		}
		if(!dataBaseSchema.equals("mysql")){
			return strbuf.toString().replaceAll("`", "");
		}
		
		/*log.info("=====================================================================================");
		log.info("Constructed Query "+strbuf);
		log.info("=====================================================================================");*/
		return strbuf.toString();
	}
	
	/**
	 * To constructed the sql query from TableOperation
	 * @param tableOperation
	 * @return
	 */
	public String constructAlterQuery(MetaTable tableOperation,boolean isPreview,List<String> deleteExistingTableColumns)throws EazyBpmException{
		if(dataBaseSchema.equals("mysql")){
			return mySqlAlterQuery(tableOperation,isPreview,deleteExistingTableColumns);
		}else if(dataBaseSchema.equals("mssql")){
			return msSqlAlterQuery(tableOperation,isPreview,deleteExistingTableColumns);
		}else if(dataBaseSchema.equals("oracle")) {
			return oracleAlterQuery(tableOperation,isPreview,deleteExistingTableColumns);
		}else{
			return "";
		}
	}
	
	public boolean checkExistingTableColumnRelation(List<String> deleteExistingTableColumns)throws EazyBpmException{
		if(!deleteExistingTableColumns.isEmpty()){
			List<MetaTableRelation> metaTableRelation = tableDao.checkExistingTableColumnRelation(deleteExistingTableColumns);
			 if(!metaTableRelation.isEmpty()){
				 throw new EazyBpmException("::  Error in removing the columns , Columns has mapped as froigen key ");
			 }
		}
		 
		 return true;
	}
	
	/**
	 * To drop the previous foreign key before altering the table 
	 */
	private void dropPreviousData(MetaTable metatable,String relationType)throws EazyBpmException{
		String tableName="";
		String isTableForm=isTableInForm(metatable);
		
		if(metatable!=null){
			tableName=metatable.getTableName();
			if(metatable.getMetaTableRelation()!=null && !metatable.getMetaTableRelation().isEmpty()){
				// To remove the child relation of the table
				if(relationType.equals("parent")){
					
					Set<MetaTableRelation> tableRelationSet=metatable.getMetaTableRelation();
					if(tableRelationSet!=null && !tableRelationSet.isEmpty()){
						StringBuffer parentTableBuffer = new StringBuffer();
						for(MetaTableRelation tableRelation :tableRelationSet){
							if(tableRelation.getId()!=null && tableRelation.getParentTable()!=null){
								String constrainKey = "fk_"+tableRelation.getParentTable().getTableName()+"_"+tableRelation.getForeignKeyColumnName()+"_"+tableRelation.getForeignKeyColumnName()+"_"+tableName;
								
								if(dataBaseSchema.equals("oracle") && constrainKey.length() > 30){
									constrainKey = "fk_"+tableRelation.getParentTable().getTableName()+"_"+tableRelation.getForeignKeyColumnName();
								}
								
								parentTableBuffer.append("ALTER TABLE `"+tableName+"` DROP FOREIGN KEY "+constrainKey);
								if(dataBaseSchema.equals("mysql")){
									parentTableBuffer.append(" DROP KEY "+constrainKey+";");
								}else{
									parentTableBuffer.append(";");
									String tableDropQuery = parentTableBuffer.toString();
									parentTableBuffer =  new StringBuffer(tableDropQuery.replace("`", "").replace("FOREIGN KEY", "CONSTRAINT")); 
								}
									
								/*parentTableBuffer.append("ALTER TABLE `"+tableName+"` DROP FOREIGN KEY fk_"+tableRelation.getParentTable().getTableName()+"_"+tableRelation.getForeignKeyColumnName()+"_"+tableName+",");
								parentTableBuffer.append("DROP KEY fk_"+tableRelation.getParentTable().getTableName()+"_"+tableRelation.getForeignKeyColumnName()+"_"+tableName+";");*/
							}
						}
						
						if(parentTableBuffer!=null && !parentTableBuffer.toString().isEmpty() ){
							//log.info("Parent Table field are droped before alter the relationship talbe :: "+parentTableBuffer.toString());
							//To remove the parent relationship of the table
							tableDao.dropRelationColumn(parentTableBuffer.toString());
							//log.info("Parent Table field are droped before alter the relationship talbe :: "+parentTableBuffer.toString());
							tableDao.dropRelationship(metatable,"parent");
							
						}
					}
					
				}else if(relationType.equals("child")){
					
					List<MetaTableRelation> chidTableRelationList=tableDao.getChildMetaTableRelation(metatable);
					//Set<MetaTableRelation> chidTableRelationList=metatable.getMetaTableRelation();
					
					if(chidTableRelationList!=null && !chidTableRelationList.isEmpty()){
						StringBuffer chidTableBuffer = new StringBuffer();
						for(MetaTableRelation chidTableRelation:chidTableRelationList){
							if(chidTableRelation.getId()!=null){
								String constrainKey = " fk_"+tableName+"_"+chidTableRelation.getForeignKeyColumnName()+"_"+chidTableRelation.getTable().getTableName(); 
								chidTableBuffer.append("ALTER TABLE `"+chidTableRelation.getTable().getTableName()+"` DROP FOREIGN KEY "+constrainKey);
								
								if(dataBaseSchema.equals("mysql")){
									chidTableBuffer.append(" DROP KEY "+constrainKey+";");
								}else{
									chidTableBuffer.append(";");
									String tableDropQuery = chidTableBuffer.toString();
									chidTableBuffer =  new StringBuffer(tableDropQuery.replace("`", "").replace("FOREIGN KEY", "CONSTRAINT")); 
								}
							}
							//chidTableBuffer.append("ALTER TABLE ETEC_RU_"+chidTableRelation.getTable().getTableName()+" DROP FOREIGN KEY fk_"+tableName+"_"+chidTableRelation.getForeignKeyColumnName()+"_"+chidTableRelation.getTable().getTableName()+",DROP  "+tableName+"_"+chidTableRelation.getForeignKeyColumnName()+";");
						}
						
						if(chidTableBuffer!=null  && !chidTableBuffer.toString().isEmpty()){
							//log.info("Child Table field are droped before alter the relationship talbe :: "+chidTableBuffer.toString());
							//To remove the child relationship of the table
							if(tableDao.dropRelationColumn(chidTableBuffer.toString())){
								tableDao.dropRelationship(metatable,"child");
							}
						}
					}
				}
			}
		}else{
			throw new EazyBpmException(isTableForm);
		}
	}
	
	/**
	 * {@inheritDoc checkTableName}
	 */
	public boolean checkTableName(String tableName)throws EazyBpmException{
		return tableDao.checkTableName(tableName);
	}
	
	/**
	 * {@inheritDoc getTables}
	 */
	public List<MetaTable> getTables()throws EazyBpmException{
		return tableDao.getTables();
	}
	
	/**
	 * {@inheritDoc getTables}
	 */
	public List<MetaTable> getTableDetailsByNames(String tableNames)throws EazyBpmException{
		return tableDao.getTableDetailsByNames(tableNames);
	}
	
	/**
	 * {@inheritDoc deleteTableById}
	 */
	public String deleteTableById(String tableId)throws EazyBpmException{
		MetaTable tableOperation= tableDao.getTableDetails(tableId);
		String tableName="";
		String isTableForm=isTableInForm(tableOperation);
		if(tableOperation!=null && isTableForm.equals("Table Droped")){
			Set<Module> modules = tableOperation.getModules();
			//Remove form relation from module before delete
			if(!modules.isEmpty()){
				tableOperation.getModules().removeAll(modules);
				tableOperation=tableDao.saveTable(tableOperation);
			}
			
			tableName=tableOperation.getTableName();
			if(tableOperation.getMetaTableRelation()!=null && !tableOperation.getMetaTableRelation().isEmpty()){
				// To remove the child relation of the table
				tableDao.deleteTableRelation(tableOperation);
				List<MetaTableRelation> chidTableRelationList=tableDao.getChildMetaTableRelation(tableOperation);
				
				if(chidTableRelationList!=null && !chidTableRelationList.isEmpty()){
					
					StringBuffer chidTableBuffer = new StringBuffer();
					for(MetaTableRelation chidTableRelation:chidTableRelationList){
						if(chidTableRelation.getId()!=null){
							String constrainKey = "fk_"+tableOperation.getTableName()+"_"+chidTableRelation.getForeignKeyColumnName()+"_"+chidTableRelation.getTable().getTableName();
							
							if(dataBaseSchema.equals("oracle") && constrainKey.length() > 30){
								constrainKey = "fk_"+tableOperation.getTableName()+"_"+chidTableRelation.getForeignKeyColumnName();
							}
							
							chidTableBuffer.append("ALTER TABLE `"+chidTableRelation.getTable().getTableName()+"` DROP FOREIGN KEY "+constrainKey);
							if(dataBaseSchema.equals("mysql")){
								chidTableBuffer.append(" DROP KEY "+constrainKey+";");
							}else{
								chidTableBuffer.append(";");
							}
						}
						//chidTableBuffer.append("ALTER TABLE ETEC_RU_"+chidTableRelation.getTable().getTableName()+" DROP FOREIGN KEY fk_"+tableOperation.getTableName()+"_"+chidTableRelation.getForeignKeyColumnName()+"_"+chidTableRelation.getTable().getTableName()+",DROP  "+tableOperation.getTableName()+"_"+chidTableRelation.getForeignKeyColumnName()+";");
					}
					if(chidTableBuffer!=null){
						
						String childTableDropQuery = chidTableBuffer.toString();
						if(!dataBaseSchema.equals("mysql")){
							childTableDropQuery = childTableDropQuery.replace("`", "").replace("FOREIGN KEY", "CONSTRAINT");
						}
						
						//To remove the parent relationship of the table
						if(tableDao.dropRelationColumn(childTableDropQuery)){
							tableDao.dropParentRelationship(tableOperation);
						}
					}
				}
			}
			
			if(tableDao.deleteTableDetails(tableOperation)){
				tableDao.dropTableFromDataBase(tableName);
				log.info("Table "+tableOperation.getTableName()+" is deleted successfully");
			}
			
			return null;
		}else{
			return isTableForm;
		}
	}
	
	/**
	 * To check whether the table is mapped with form .
	 * @param tableOperation
	 * @return
	 */
	private String isTableInForm(MetaTable tableOperation){
		List<MetaForm> metaForm= tableDao.isTableInForm(tableOperation);
		if(metaForm.isEmpty()){
			return "Table Droped";
		}else{
			return "Table "+tableOperation.getTableName()+" is mapped with Form "+metaForm.get(0).getFormName().toString();
		}
	}
	
	/**
	 * {@inheritDoc createQuery}
	 */
	public String createQuery(String tableName,List<Map<String,Object>> fieldProperties,List<Map<String,Object>> parentTableList,List<Map<String,Object>>  subTableList, boolean isUpdate,List<String> deleteExistingTableColumns)throws EazyBpmException{
		String tableQuery="";
		String test=tableName.toUpperCase().replaceAll(" ", "_");
		
		MetaTable tableOperation=new MetaTable();
		tableOperation.setTableName(test);
		
		Set<MetaTableColumns> fieldPro=new HashSet<MetaTableColumns>();
		Set<MetaTableRelation> parent_Table=new HashSet<MetaTableRelation>();
		Set<MetaTableRelation> child_Table=new HashSet<MetaTableRelation>();
		
		fieldPro=getMetaFieldProperty(fieldProperties,tableOperation);
		parent_Table=getMetTableRelation(tableOperation,parentTableList,true);
		child_Table=getMetTableRelation(tableOperation,subTableList,false);
		parent_Table.addAll(child_Table);
		
		tableOperation.setMetaTableColumns(fieldPro);
		tableOperation.setMetaTableRelation(parent_Table);
		tableOperation.setId(null);
		
		if(fieldPro!=null){
			if(isUpdate){
				tableQuery=constructAlterQuery(tableOperation,true,deleteExistingTableColumns);
			}else{
				tableQuery=constructQuery(tableOperation);
			}
		}
		
		return tableQuery;
	}
	
	
	/**
	 * {@inheritDoc getTableDetails}
	 */
	public MetaTable getTableDetails(String tableId)throws EazyBpmException{
		return tableDao.getTableDetails(tableId);
	}
	
	/**
	 * {@inheritDoc getAllTable}
	 */
	public List<LabelValue> getAllTable()throws EazyBpmException{
		boolean isAdmin=false;
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Set<Role> roles=user.getRoles();

		for(Role role:roles){
			if(role.getId().equals("ROLE_ADMIN")){
				isAdmin=true;
				break;
			}
		}
		
		if(!isAdmin){
			List<LabelValue> tableList = new ArrayList<LabelValue>();
			List<Module> userModule = moduleService.getAllModuleList();
			Set<MetaTable> tableSet=new HashSet<MetaTable>();
			// Get user modules
			for(Module moduleObj:userModule){
				tableSet.addAll(moduleObj.getTables());
			}
			
			//Get user tables
			for(MetaTable tableObj:tableSet){
				LabelValue labelValueObj=new LabelValue();
				labelValueObj.setLabel(tableObj.getTableName());
				labelValueObj.setValue(tableObj.getId());
				tableList.add(labelValueObj);
			}
			
			return tableList;
		}else{
			return tableDao.getAllTable();
		}
		
	}
	
	/**
	 * {@inheritDoc getTableWithParentTable}
	 */
	public List<LabelValue> getTableWithParentTable(String tableId)throws EazyBpmException{
		return tableDao.getTableWithParentTable(tableId);
	}
	
	
	/**
	 * {@inheritDoc getTablesByParentTableId}
	 */
	public List<LabelValue> getTablesByParentTableId(String parentTableId)throws EazyBpmException{
		return tableDao.getTablesByParentTableId(parentTableId);
	}
	
	/**
	 * {@inheritDoc getAllColumnByTableId}
	 */
	public List<LabelValue> getAllColumnByTableId(String tableId,String defaultColumns,String selectDataType)throws EazyBpmException{
		StringBuffer filterCondition=new StringBuffer();
		
		String[] defaultFields = defaultColumns.split("-");
		for(int index=0;index<defaultFields.length;index++){
			String[] columnField=defaultFields[index].split(",");
			filterCondition.append(" and tableColumns.name!='"+columnField[0]+"' ");
		}
		String[] selectDatatypes = selectDataType.split(",");
		filterCondition.append("and (");
		for(int i=0;i<selectDatatypes.length;i++){
			filterCondition.append(" tableColumns.type ='"+selectDatatypes[i]+"' ");
			if (i < (selectDatatypes.length - 1)) {
				filterCondition.append(" or ");
			}
			
		}
		filterCondition.append(")");
		return tableDao.getAllColumnByTableId(tableId,filterCondition.toString());
	}
	
	/**
	 * {@inheritDoc getAllColumnIncludeDefaultByTableId}
	 */
	public List<LabelValue> getAllColumnIncludeDefaultByTableId(String tableId) throws EazyBpmException{
		return tableDao.getAllColumnIncludeDefaultByTableId(tableId);
	}
	
	/**
	 * {@inheritDoc getTableDump}
	 */
	public List<Map<String,Object>> getTableDump(String tableName,List<String> columnNameList)throws EazyBpmException{
		StringBuffer strbuf=new StringBuffer();
		for(String columns:columnNameList){
			if(!columns.equalsIgnoreCase("id")) {
				if(columns.equalsIgnoreCase("CREATEDBY")) {
					strbuf.append("users.FULL_NAME as CREATEDBY,"); 
				}else if(columns.equalsIgnoreCase("MODIFIEDBY")) {
					strbuf.append("users.FULL_NAME as MODIFIEDBY,"); 
				} else {
					strbuf.append("table1."+columns+" as `"+columns+"`,");	
				}
			}else{
				strbuf.append("table1."+columns+" as id ,");
			}
		}
		if(strbuf.lastIndexOf(",")>0){
			strbuf.deleteCharAt(strbuf.lastIndexOf(","));
		}
		String selectTableDatas="select "+strbuf+" from " +tableName+" as table1, ETEC_USER as users WHERE users.id = table1.CREATEDBY";
		List tableDataObjArray= tableDao.getTableDump(selectTableDatas);
		
		List<Map<String,Object>> allTableDataList=new ArrayList<Map<String,Object>>();
		if(tableDataObjArray.isEmpty()){
			 Map<String,Object> columData=new HashMap<String, Object>();
			 for(int index=0;index<columnNameList.size();index++){
					  columData.put(columnNameList.get(index), "");
			  }
			 allTableDataList.add(columData);
		}else{
			 /*for(Object[] tableDataArray:tableDataObjArray){
				  Map<String,Object> columData=new HashMap<String, Object>();
				  for(int index=0;index<columnNameList.size();index++){
					  if(tableDataArray[index]!=null){
						  columData.put(columnNameList.get(index), tableDataArray[index]);
					  }else{
						  columData.put(columnNameList.get(index), "");
					  }
				  }
				  allTableDataList.add(columData);
	         }*/
			if ((tableDataObjArray != null) && (tableDataObjArray.size() > 0)) {
				for (int i = 0; i < tableDataObjArray.size(); i++) {
					if (tableDataObjArray.get(i) instanceof String) {
						String listViewResult = String.valueOf(tableDataObjArray.get(i));
						Map<String,Object> columData=new HashMap<String, Object>();
				 	  	for(int index=0;index<columnNameList.size();index++){
				 	  		if(listViewResult.contains("<script xmlns")){
				 	  			listViewResult=listViewResult.replace("<script xmlns", "<A_script xmlns");
				 	  			listViewResult=listViewResult.replace("</script>", "</A_script>");
				 	  		}
			 	  				columData.put(columnNameList.get(index), listViewResult);
			 	  		}
				 	  	allTableDataList.add(columData);
					} else if (tableDataObjArray.get(i) instanceof Object[]) {
						Object[] listViewObjInfo = (Object[]) tableDataObjArray.get(i);
						Map<String,Object> columData=new HashMap<String, Object>();
				 	  	for(int index=0;index<columnNameList.size();index++){
				 	  		//For date field
				 	  		if(listViewObjInfo[index] instanceof Date){
				 	  			Date dateTime = null;
				 	  			String dateStr = null;
				 	  			try {
				 	  				String dbData=String.valueOf(listViewObjInfo[index]);
				 	  				if(dbData.length()>11){
				 	  					dateTime = DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss", dbData);
				 	  					dateStr = DateUtil.convertDateToStringFormat(dateTime,"yyyy-MM-dd HH:mm:ss");
				 	  				}else{
				 	  					dateTime = DateUtil.convertStringToDate("yyyy-MM-dd", dbData);
				 	  					dateStr = DateUtil.convertDateToStringFormat(dateTime,"yyyy-MM-dd");
				 	  				}
				 	  				
				 	  				if(dateStr != null){
				 	  					columData.put(columnNameList.get(index), dateStr);   
					 	            }else{
					 	            	columData.put(columnNameList.get(index), dateTime.toString());      
					 	            }
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}	
				 	  		}else if (listViewObjInfo[index] instanceof BigDecimal || listViewObjInfo[index] instanceof Number) {
				 	  			String temp = String.valueOf(listViewObjInfo[index]);
				 	  			if(temp!= null){
				 	  				if(temp.equals("0")){
					 	  				columData.put(columnNameList.get(index), "false");
					 	  			}else if(temp.equals("1")){
					 	  				columData.put(columnNameList.get(index), "true");
					 	  			}else{
					 	  				columData.put(columnNameList.get(index), listViewObjInfo[index]);
					 	  			}
				 	  			}
				 	  		}else{
				 	  			String content_data=String.valueOf(listViewObjInfo[index]);
				 	  			if(content_data.contains("<script xmlns")){
				 	  				content_data=content_data.replace("<script xmlns", "<A_script xmlns");
				 	  				content_data=content_data.replace("</script>", "</A_script>");
				 	  				columData.put(columnNameList.get(index), content_data);
				 	  			}else{
				 	  				columData.put(columnNameList.get(index), listViewObjInfo[index]);
				 	  			}
				 	  		}
				 	  	}
				 	  	allTableDataList.add(columData);
					}
				}
			}
		}
		
		return allTableDataList;
	}
	
	/**
	 * {@inheritDoc createSubFormRelationshipTables}
	 */
	public void createSubFormRelationshipTables(List<Map<String,Object>> subFormRelationList)throws EazyBpmException{
		for(Map<String,Object> subFormRelation :subFormRelationList){
			String parentTableName=subFormRelation.get("parentTableName").toString();
			String childTableName=subFormRelation.get("childTableName").toString();
			
			StringBuffer strbuf = new StringBuffer();
			String tableQuery=" create table "+parentTableName+"_"+childTableName+"( ID VARCHAR(100)  NOT NULL," +
				parentTableName+"_ID VARCHAR(100)  NOT NULL," +
				childTableName+"_ID VARCHAR(100)  NOT NULL,PRIMARY KEY (ID)," +
						"FOREIGN KEY ("+parentTableName+"_ID) REFERENCES "+parentTableName+"(ID)," +
						"FOREIGN KEY ("+childTableName+"_ID) REFERENCES "+childTableName+"(ID))" ;
			
			strbuf.append(tableQuery);
			
			if(tableDao.createTable(strbuf.toString())){
				//log.info("Form Relation Ship table is created: "+strbuf.toString());
			}
		}
	}
	
	/**
	 * {@inheritDoc importExcelDatas}
	 */
	public void importExcelDatas(String fileNameWithPath,String tableId,String fileName)throws EazyBpmException{
		MetaTable metaTable=getTableDetails(tableId);
		Set<MetaTableColumns> metaTableColumns=metaTable.getMetaTableColumns();
		List<String> columnList=new ArrayList<String>();
		
		for(MetaTableColumns metaColumn:metaTableColumns){
			columnList.add(metaColumn.getName());
		}
		
		List sheetData = new ArrayList();
		List headerData = new ArrayList();
		List<String> headerDataList=new  ArrayList<String>();
		
		 FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(fileNameWithPath);
		
			//
            // Create an excel workbook from the file system.
            //
			HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            
            //
            // Get the first sheet on the workbook.
            //
            HSSFSheet sheet = workbook.getSheetAt(0);

            //
            // When we have a sheet object in hand we can iterator on 
            // each sheet's rows and on each row's cells. We store the 
            // data read on an ArrayList so that we can printed the 
            // content of the excel to the console.
            //
            
            Iterator rows = sheet.rowIterator();
            int rowCount=0;
            while (rows.hasNext()) {
                HSSFRow row = (HSSFRow) rows.next();
                Iterator cells = row.cellIterator();

                List data = new ArrayList();
                while (cells.hasNext()) {
                    HSSFCell cell = (HSSFCell) cells.next();
                    data.add(cell);
                }
                if(rowCount==0){
                	headerData.add(data);
                }else{
                	sheetData.add(data);
                }
                rowCount++;
            }
         
            if(!headerData.isEmpty()){
            	
            		StringBuffer strbuf = new StringBuffer();
					StringBuffer valueString = new StringBuffer();
					strbuf.append("Insert into "+metaTable.getTableName()+" (");
					
					for(int i = 0; i < headerData.size(); i++){
						List list = (List) headerData.get(i);
						for (int j = 0; j < list.size(); j++) {
                            HSSFCell cell = (HSSFCell) list.get(j);
                            strbuf.append("`"+cell.getRichStringCellValue().getString()+"`");
                            headerDataList.add(cell.getRichStringCellValue().getString());
                            if (j < list.size() - 1) {
                            	strbuf.append(", ");
                            }
                        }
					}
					strbuf.append(")");
					String insertQuery = strbuf.toString();
				if(checkHeadMatchTheTable(headerDataList,columnList)){	
					valueString.append(" values ");
					for (int i = 0; i < sheetData.size(); i++) {
						valueString.append(" (");
                        List list = (List) sheetData.get(i);
                        for (int j = 0; j < list.size(); j++) {
                        	HSSFCell cell = (HSSFCell) list.get(j);
                            String cellValue = String.valueOf(cell);
                            
                            if(cellValue.equals("") || cellValue.equals("null") || cellValue.equals(null)){
                            	valueString.append(" null ");
                            }else{
                            	if(cellValue.equalsIgnoreCase("true") || cellValue.equalsIgnoreCase("false")){
                            		if(cellValue.equalsIgnoreCase("true")){
                            			valueString.append(1);
                            		}else{
                            			valueString.append(0);
                            		}
                            		
                            	}else{
                            		if(cellValue.contains("'")){
                            			cellValue=cellValue.replace("'", "\''");
                            		}else if(cellValue.contains("\\")){
                            			cellValue=cellValue.replace("\\", "\\\\");
                            		}
                            		
                            		if(CommonUtil.isDate(cellValue)){
                            			valueString.append(CommonUtil.convertDateTypeForDB(dataBaseSchema,cellValue));
                            		}else{
                            			valueString.append("'"+cellValue+"' ");
                            		}
                            	}
                            }
                            if (j < list.size() - 1) {
                            	valueString.append(", ");
                            }
                        }
                        valueString.append(")");
                        if(!dataBaseSchema.equals("mysql")){
                        	strbuf.append(valueString+";");
    						valueString = new StringBuffer(insertQuery+" values ");
    					}else if (i < sheetData.size() - 1) {
                        	valueString.append(", ");
                        }
                    }
            		
					if(dataBaseSchema.equals("mysql")){
						strbuf.append(valueString);
					}
            		if(tableDao.createTable(strbuf.toString())){
						//log.info("Inserted Data Query :: "+strbuf.toString());
					}
			    }else{
			    	throw new EazyBpmException("Check the file '"+fileName+"' header is not matched with the Table '"+metaTable.getTableName()+"'");
        		}
            }
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * {@inheritDoc importCSVDatas}
	 */
	public void importCSVDatas(String fileNameWithPath,String tableId,String fileName)throws EazyBpmException{
		File uploadedFile = new File(fileNameWithPath);
		MetaTable metaTable=getTableDetails(tableId);
		Set<MetaTableColumns> metaTableColumns=metaTable.getMetaTableColumns();
		List<String> columnList=new ArrayList<String>();
		
		for(MetaTableColumns metaColumn:metaTableColumns){
			columnList.add(metaColumn.getName());
		}
		List<String> unavailableData = new ArrayList<String>();
		List<String> dataImportInfo = new ArrayList<String>();
		List<String> headers=getHeaderFromCSV(uploadedFile);
		List<String> errorInfo=new ArrayList<String>();
		
		if(checkHeadMatchTheTable(headers,columnList)){
		
			FileReader reader = null;
			CSVReader csvReader = null;
			int count = 0;
			
			try{
				reader = new FileReader(uploadedFile);
				csvReader=getCSVReader(reader);
				String[] nextLineArray = null;
	
				boolean first = true;	
				
				
					while ((nextLineArray = csvReader.readNext()) != null) {
						StringBuffer strbuf = new StringBuffer();
						StringBuffer valueString = new StringBuffer();
						strbuf.append("Insert into "+metaTable.getTableName()+" (");
						valueString.append(" values (");
						Map<String, String> csvValues = new HashMap<String, String>();
		
						count++;
						// first line is header and ArrayIndexof bound Exception occurs
						// when next read line is empty
						if (!first && nextLineArray.length >= 1) {
							csvValues.putAll(csvFileReader(headers,nextLineArray));
							Set<String> headerKey=csvValues.keySet();
							for(String head:headerKey){
								if(!csvValues.get(head).isEmpty() && csvValues.get(head)!=""){
									strbuf.append("`"+head+"`,");
									valueString.append("'"+csvValues.get(head)+"',");
								}
							}
							if(strbuf.lastIndexOf(",")>0){
								strbuf.deleteCharAt(strbuf.lastIndexOf(","));
							}
							if(valueString.lastIndexOf(",")>0){
								valueString.deleteCharAt(valueString.lastIndexOf(","));
							}
							strbuf.append(")");
							valueString.append(")");
							strbuf.append(valueString);
							
							if(tableDao.createTable(strbuf.toString())){
								//log.info("Inserted Data Query :: "+strbuf);
							}
						}
						first = false;
					}
					
				
					if (unavailableData.size() != 0) { 
						for (String logInfo : unavailableData) {
						//	log.info(logInfo);
						}
					}
					
					if (dataImportInfo.size() != 0) {
						for (String logInfo : dataImportInfo) {
							//log.info(logInfo);
						}
					}
					
					} catch (Exception e) {
						throw new EazyBpmException(e.getMessage(),e);
					} finally {
						try {
							if (reader != null) {
								reader.close();
							}
							if (csvReader != null) {
								csvReader.close();
							}
							if(!errorInfo.isEmpty()) {
			              	  throw new EazyBpmException("Import Failures at"+errorInfo.toString());
			                }
						} catch (IOException e) {
							e.printStackTrace();
							throw new EazyBpmException(e.getMessage(), e);
						}
					}
		}else{
			throw new EazyBpmException("Check the file '"+fileName+"' header is not matched with the Table '"+metaTable.getTableName()+"'");
		}
	}
	
	/**
	 * To check the CSV head is mapped with the meta column List list
	 */
	private boolean checkHeadMatchTheTable(List<String> CSVheader,List<String> columnList)throws EazyBpmException {
		int count=0;
		for(String header:CSVheader){
			for(String column:columnList){
				if(header.equals(column)){
					count++;
				}
			}
		}
		if(columnList.size()==count){
			return true;
		}else{
			return false;
		}
		
	}
	
	/**
	 * @inheritDoc getHeaderFromCSV
	 */
	public List<String> getHeaderFromCSV(final File csvFile) throws EazyBpmException {
	
		List<String> headerDataAsList = new ArrayList<String>();
		FileReader reader = null;
		CSVReader csvReader = null;
		
		try {
			reader = new FileReader(csvFile);
			csvReader=getCSVReader(reader);

			String[] nextLineArray;
			nextLineArray = csvReader.readNext();
			for (int i = 0; i < nextLineArray.length; i++) {
				headerDataAsList.add(nextLineArray[i]);
			}
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(),e);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (csvReader != null) {
					csvReader.close();
				}
			} catch (IOException e) {
				throw new EazyBpmException(e.getMessage(), e);
			}
		}
		return headerDataAsList;
	}
	
	/**
	 * To Read the data in the CSV sheet
	 * @param reader
	 * @param fileFormat
	 * @return
	 */
	private CSVReader getCSVReader(FileReader reader){
		CSVReader csvReader =  new CSVReader(reader);
		return csvReader;
	}
	
	/**
	 * To read the csv file
	 * @param header
	 * @param nextLineArray
	 * @return
	 * @throws EazyBpmException
	 */
	private Map<String, String> csvFileReader(List<String> header,String[] nextLineArray)throws EazyBpmException{
		Map<String, String> csvValues = new HashMap<String, String>();
		for (int i = 0; i < header.size(); i++) {
			try {
				if (nextLineArray[i].equalsIgnoreCase("NULL") || nextLineArray[i].equalsIgnoreCase("\\N")) {
					nextLineArray[i] = "";
				}
				csvValues.put(header.get(i), nextLineArray[i]);
			} catch (ArrayIndexOutOfBoundsException e) {
				// sometimes headers and field values list may not
				// be same
				// to handle that situation only catch the exception
				log.debug("Array Index Out of bounds Exception" + e.getMessage());
				throw new EazyBpmException(e.getMessage(), e);
			}
		}
		return csvValues;
	}
	
	/**
	 * {@inheritDoc getNonRelationTables}
	 */
	public List<MetaTable> getNonRelationTables(String tableId)throws EazyBpmException{
		List<LabelValue> privilageTableList= getAllTable();
		List<MetaTable> nonRelationTablesList = tableDao.getNonRelationTables(tableId);
		List<MetaTable> relationTables = new ArrayList<MetaTable>();
		for(LabelValue privilageTable:privilageTableList){
			for(MetaTable nonRelationTables : nonRelationTablesList){
				if(nonRelationTables.getId().equals(privilageTable.getValue())){
					relationTables.add(nonRelationTables);
				}
			}
		}
		return relationTables;
	}
	
	@Autowired
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	
	/**
	 * {@inheritDoc deleteParentTableList}
	 */
	public void deleteParentTableList(MetaTable metaTable,List<String> parentTableList)throws EazyBpmException{
		List<MetaTableRelation> parentTableRelationList=tableDao.getParentMetaTableRelation(metaTable);
		//log.info("Droping the Parent relation");
		if(parentTableRelationList!=null && !parentTableRelationList.isEmpty()){
			
			StringBuffer parentTableBuffer = new StringBuffer();
			for(MetaTableRelation parentTableRelation:parentTableRelationList){
				//parentTableBuffer.append("ALTER TABLE ETEC_RU_"+metaTable.getTableName()+" DROP FOREIGN KEY fk_"+parentTableRelation.getTable().getTableName()+"_"+parentTableRelation.getForeignKeyColumnName()+"_"+metaTable.getTableName()+",DROP  "+parentTableRelation.getTable().getTableName()+"_"+parentTableRelation.getForeignKeyColumnName()+";");
				String constrainKey = "fk_"+parentTableRelation.getTable().getTableName()+"_"+parentTableRelation.getForeignKeyColumnName()+"_"+metaTable.getTableName();
				if(dataBaseSchema.equals("oracle") && constrainKey.length() > 30){
						constrainKey = "fk_"+parentTableRelation.getTable().getTableName()+"_"+parentTableRelation.getForeignKeyColumnName();
				}
				parentTableBuffer.append("ALTER TABLE `"+metaTable.getTableName()+"` DROP FOREIGN KEY "+constrainKey);
				if(dataBaseSchema.equals("mysql")){
					parentTableBuffer.append(",DROP KEY "+constrainKey+";");
				}else{
					parentTableBuffer.append(";");
				}
				
			}
			if(parentTableBuffer!=null){
				//To remove the parent relationship of the table
				String parentTableDropQuery = parentTableBuffer.toString();
				if(!dataBaseSchema.equals("mysql")){
					parentTableDropQuery = parentTableDropQuery.replace("`", "").replace("FOREIGN KEY", "CONSTRAINT");
				}
				if(tableDao.dropRelationColumn(parentTableDropQuery)){
					tableDao.dropRelationship(metaTable,"parent");
				}
			}
		}
	}
	
	/**
	 *{@inheritDoc deleteSubTableList}
	 */
	public void deleteSubTableList(MetaTable metaTable,List<String> subTableList)throws EazyBpmException{
		List<MetaTableRelation> childTableRelationList=tableDao.getChildMetaTableRelation(metaTable);
		//log.info("Droping the Child relation");
		if(childTableRelationList!=null && !childTableRelationList.isEmpty()){
			
			StringBuffer childTableBuffer = new StringBuffer();
			for(MetaTableRelation childTableRelation:childTableRelationList){
				//childTableBuffer.append("ALTER TABLE ETEC_RU_"+childTableRelation.getTable().getTableName()+" DROP FOREIGN KEY fk_"+metaTable.getTableName()+"_"+childTableRelation.getForeignKeyColumnName()+"_"+childTableRelation.getTable().getTableName()+",DROP  "+metaTable.getTableName()+"_"+childTableRelation.getForeignKeyColumnName()+";");
				String constrainKey = "fk_"+metaTable.getTableName()+"_"+childTableRelation.getForeignKeyColumnName()+"_"+childTableRelation.getTable().getTableName();  
				
				if(dataBaseSchema.equals("oracle") && constrainKey.length() > 30){
					constrainKey = "fk_"+metaTable.getTableName()+"_"+childTableRelation.getForeignKeyColumnName();
				}
				
				childTableBuffer.append("ALTER TABLE `"+childTableRelation.getTable().getTableName()+"` DROP FOREIGN KEY "+constrainKey);
				if(dataBaseSchema.equals("mysql")){
					childTableBuffer.append(",DROP KEY "+constrainKey+";");
				}else{
					childTableBuffer.append(";");
				}
				
			}
			if(childTableBuffer!=null){
				
				String childTableDropQuery = childTableBuffer.toString();
				if(!dataBaseSchema.equals("mysql")){
					childTableDropQuery = childTableDropQuery.replace("`", "").replace("FOREIGN KEY", "CONSTRAINT");
				}
				
				//To remove the parent relationship of the table
				if(tableDao.dropRelationColumn(childTableDropQuery)){
					tableDao.dropRelationship(metaTable,"child");
				}
			}
		}
	}
	
	/**
	 *{@inheritDoc}
	 */
	public List<MetaTable> getTablesByIds(List<String> ids)throws EazyBpmException{
		return tableDao.getTablesByIds(ids);
	}
	
	/**
	 *{@inheritDoc}
	 */
	public String getListViewByTableName(String tableName,boolean isQuery)throws EazyBpmException{
		List<String> columnTableNameList=new ArrayList<String>();
		if(isQuery){
			columnTableNameList=getTableColumnNames(tableName.toUpperCase(),isQuery);
		}else{
			columnTableNameList=tableDao.getAllColumnsByTableName(tableName);
		}
		
		String container="rightPanel";
		Map<String, Object> context = new HashMap<String, Object>();
		if(!columnTableNameList.isEmpty()){
			if(isQuery){
				context.put("title", "Table Data");
				context.put("gridId", "constructedGrid");
				context.put("listViewId", "constructedGrid");
			}else{
				context.put("title", tableName+" Table Data");
				context.put("gridId", tableName);
				context.put("listViewId", tableName);
			}
			context.put("isNeedCheckbox", false);
			context.put("isExportNeed", false);
						
			List<String> colunmnTitle=new ArrayList<String>();
			for(String viewColumns:columnTableNameList){
				colunmnTitle.add(viewColumns);
			}
		
			List<Map<String,Object>>  listViewDataList=new ArrayList<Map<String,Object>>();
			if(isQuery){
				listViewDataList=getAllListViewMapData(tableName,colunmnTitle,isQuery);
			}else{
				listViewDataList=getAllListViewMapData(tableName,colunmnTitle,isQuery);
			}
			
			String jsonFieldValues = "";
			if(listViewDataList != null && !(listViewDataList.isEmpty())){
				jsonFieldValues = CommonUtil.getJsonString(listViewDataList);	
			}
			//addValueForGroupByFilter(listViewDataList, viewColumnsList, context);
			
			context.put("dynamicGridWidth", "getGridWidthWithSearchBox");
			context.put("dynamicGridHeight", "getGridHeightWithTextArea");
			
			context.put("columnNames", CommonUtil.getJsonString(colunmnTitle));	
			context.put("jsonFieldValues", jsonFieldValues);
			
			
			List<Map<String, Object>> columnsPropertyList = new ArrayList<Map<String, Object>>();
			
			for(String listViewColumns:colunmnTitle){
				CommonUtil.createFieldNameList(columnsPropertyList, listViewColumns, "100", "left", "", "false","true","","text");
			}
			context.put("noOfRecords", 20);
		
			//For grid header Link
			List<Map<String, Object>> headerLinkList= new ArrayList<Map<String, Object>>();
			
			context.put("headerLink", headerLinkList);
			context.put("fieldNameList", columnsPropertyList);
			context.put("container", container);
			return GridUtil.generateDynamicScript(velocityEngine, context);
		}else{
			throw new EazyBpmException("There no List view in the name :: "+tableName);
		}
	}
	
	public List<String> getTableColumnNames(String constructedQuery,boolean isQuery){
		List<String> columnTitle=new ArrayList<String>();
		String[] selectPart=constructedQuery.split("FROM");
		if(constructedQuery.contains("*")){
			String[] tablePart=selectPart[1].split("WHERE");
			String[] fromTablePart=tablePart[0].split(",");
			columnTitle=tableDao.getAllColumnsByTableName(fromTablePart[0].trim());
		}else{
			String[] columnPart=selectPart[0].split("SELECT");
			String[] columnsAlise=columnPart[1].split(",");
			for(int index=0;index<columnsAlise.length;index++){
				String[] splitColumAlise=columnsAlise[index].split(" AS ");
				if(splitColumAlise.length==2){
					columnTitle.add(splitColumAlise[1].trim());
				}else{
					columnTitle.add(splitColumAlise[0].trim());
				}
				
			}
		}
		
		return columnTitle;
	}
	
	public List<Map<String,Object>> getAllListViewMapData(String tableName,List<String> colunmnList,boolean isQuery)throws EazyBpmException{
		List listViewObjArray=null;
		
		if(isQuery){
			listViewObjArray=tableDao.getTableDataByTableName(tableName,null);
		}else{
			StringBuffer selectColumn=new StringBuffer();
			for(String columns:colunmnList){
				selectColumn.append("`"+columns+"`,");
			}
			if(selectColumn.lastIndexOf(",")>0){
				selectColumn.deleteCharAt(selectColumn.lastIndexOf(","));
			}
			
			listViewObjArray=tableDao.getTableDataByTableName(tableName,selectColumn.toString());
		}
		List<Map<String,Object>> listViewDataList = new ArrayList<Map<String,Object>>();
		if ((listViewObjArray != null) && (listViewObjArray.size() > 0)) {
			for (int i = 0; i < listViewObjArray.size(); i++) {
				if (listViewObjArray.get(i) instanceof String) {
					String listViewResult = String.valueOf(listViewObjArray.get(i));
					Map<String,Object> columData=new HashMap<String, Object>();
			 	  	for(int index=0;index<colunmnList.size();index++){
			 	  		if(listViewResult.contains("<script")){
			 	  			listViewResult=listViewResult.replace("<script", "<A_script");
			 	  			listViewResult=listViewResult.replace("</script>", "</A_script>");
			 	  		}
		 	  				columData.put(colunmnList.get(index), listViewResult);
		 	  		}
				 	listViewDataList.add(columData);
				} else if (listViewObjArray.get(i) instanceof Object[]) {
					Object[] listViewObjInfo = (Object[]) listViewObjArray.get(i);
					Map<String,Object> columData=new HashMap<String, Object>();
			 	  	for(int index=0;index<colunmnList.size();index++){
			 	  		//For date field
			 	  		if(listViewObjInfo[index] instanceof Date){
			 	  			Date dateTime = null;
			 	  			String dateStr = null;
			 	  			try {
			 	  				String dbData=String.valueOf(listViewObjInfo[index]);
			 	  				if(dbData.length()>11){
			 	  					dateTime = DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss", dbData);
			 	  					dateStr = DateUtil.convertDateToStringFormat(dateTime,"yyyy-MM-dd HH:mm:ss");
			 	  				}else{
			 	  					dateTime = DateUtil.convertStringToDate("yyyy-MM-dd", dbData);
			 	  					dateStr = DateUtil.convertDateToStringFormat(dateTime,"yyyy-MM-dd");
			 	  				}
								
			 	  				if(dateStr != null){
			 	  					if(!dateStr.equals("null")){
			 	  						columData.put(colunmnList.get(index), dateStr);  
			 	  					}else{
			 	  						columData.put(colunmnList.get(index), "");  
			 	  					}
			 	  					 
				 	            }else{
				 	            	if(!String.valueOf(dateTime).equals("null")){
				 	            		columData.put(colunmnList.get(index), dateTime.toString()); 
				 	            	}else{
				 	            		columData.put(colunmnList.get(index), ""); 
				 	            	}
				 	            }
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
			 	  		}else{
			 	  			String content_data= "";
			 	  			if(listViewObjInfo[index] != null){
			 	  				content_data=String.valueOf(listViewObjInfo[index]);
			 	  			}
			 	  			 
			 	  			if(content_data.contains("<script")){
			 	  				content_data=content_data.replace("<script", "<A_script");
			 	  				content_data=content_data.replace("</script>", "</A_script>");
			 	  			}
			 	  			columData.put(colunmnList.get(index), content_data);
			 	  		}
			 	  	}
				 	listViewDataList.add(columData);
				}else{
					String listViewResult = String.valueOf(listViewObjArray.get(i));
					Map<String,Object> columData=new HashMap<String, Object>();
			 	  	for(int index=0;index<colunmnList.size();index++){
		 	  			columData.put(colunmnList.get(index), listViewResult);
		 	  		}
				 	listViewDataList.add(columData);
				}
			}
		}
		
		return listViewDataList;
	}
	
	/**
	 *{@inheritDoc}
	 */
	public String constructMetaTableDump(MetaTable metaTableObj)throws EazyBpmException{
		StringBuffer metaTableDump = new StringBuffer();
		
		String metaTableColumnDump = "";
		//String metaTableRelationDump = "";
		
		Set<MetaTableColumns> metaTableColumnSet =metaTableObj.getMetaTableColumns();
		//Set<MetaTableRelation> metaTableRelationSet=metaTableObj.getMetaTableRelation();
		
		//Making the relationship empty to avoid in dump
		Set<MetaTableRelation> test=new HashSet<MetaTableRelation>();
		metaTableObj.setMetaTableRelation(test);
		metaTableDump.append(constructQuery(metaTableObj)+";");
		metaTableDump.append("\n\n\n\n");
		//List<MetaTableRelation> metaTableRelationList=tableDao.getAllTalbeRelationById(metaTableObj.getId());
			
		String metaTableQuery=getMetaTableQuery(metaTableObj);
		//Meta table query
		metaTableDump.append(metaTableQuery);
		
		//Meta table column query
		if(!metaTableColumnSet.isEmpty()){
			metaTableDump.append("\n\n\n\n");
			metaTableColumnDump=getMetaTableColumnDump(metaTableColumnSet);
			metaTableDump.append(metaTableColumnDump+"\n\n\n\n");
		}
		
		//Meta table relation query
		/*if(!metaTableRelationSet.isEmpty()){
			metaTableDump.append("\n\n\n\n");
			metaTableRelationDump=getMetaTableRelationDump(metaTableRelationList,metaTableRelationSet,true);
			metaTableDump.append(metaTableRelationDump);	
		}*/
		
		//log.info("Meta table dump :: "+metaTableDump.toString());
		return metaTableDump.toString();
	}
	
	/**
	 *{@inheritDoc}
	 */
	public String constructMetaTableDumpForModules(MetaTable metaTableObj)throws EazyBpmException{
		StringBuffer metaTableDump = new StringBuffer();
		
		String metaTableColumnDump = "";
		
		Set<MetaTableColumns> metaTableColumnSet =metaTableObj.getMetaTableColumns();
			
		String metaTableQuery=getMetaTableQuery(metaTableObj);
		//Meta table query
		metaTableDump.append(metaTableQuery);
		
		//Meta table column query
		if(!metaTableColumnSet.isEmpty()){
			metaTableDump.append("\n\n\n\n");
			metaTableColumnDump=getMetaTableColumnDump(metaTableColumnSet);
		   metaTableDump.append(metaTableColumnDump+"\n\n\n\n");
		}
		
		return metaTableDump.toString();
	}
	
	
	/**
	 * To construct query for meta table
	 * @param metaTableObj
	 * @return
	 * @throws EazyBpmException
	 */
	private String getMetaTableQuery(MetaTable metaTableObj)throws EazyBpmException {
		 User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 Date createdOn = new Date();
		 String createdOnDate = "";
		 createdOnDate =  DateUtil.convertDateToStringFormat(createdOn,"yyyy-MM-dd HH:mm:ss");
		 if(dataBaseSchema.equals("mysql")){
			 createdOnDate = "'"+createdOnDate+"'";
		 }else{
			 createdOnDate = CommonUtil.convertDateTypeForDB(dataBaseSchema,createdOnDate);
		 }
		 
		String metaTableQuery="insert into `ETEC_RE_TABLE`(`id`,`chinese_name`,`created_by`,`created_time`,`description`,`table_name`,`IS_AUTOFORMCREATE`) values ('" +
		metaTableObj.getId()+"','"+metaTableObj.getChineseName()+"','"+user.getUsername()+"',"+createdOnDate+",'"+metaTableObj.getDescription()+"','" +
		metaTableObj.getTableName()+"',";
		
		if(!dataBaseSchema.equals("mysql")){
			if(metaTableObj.getIsAutoFormCreate()){
				metaTableQuery += 1+");";
			}else{
				metaTableQuery += 0+");";
			}
		}else{
			metaTableQuery += metaTableObj.getIsAutoFormCreate()+");";
		}
		
		return metaTableQuery;
	}
	
	/**
	 * To construct query for meta table columns
	 * @param metaTableColumnSet
	 * @return
	 * @throws EazyBpmException
	 */
	private String getMetaTableColumnDump(Set<MetaTableColumns> metaTableColumnSet)throws EazyBpmException {
		StringBuffer metaTableColumnDump = new StringBuffer();
		if(!dataBaseSchema.equals("mysql")){
			for(MetaTableColumns meatTableColumn:metaTableColumnSet){
				int isUniqueKey= 0;
				if(meatTableColumn.getIsUniquekey()){
					isUniqueKey =1 ;
				}
				
				int isCompositeKey= 0;
				if(meatTableColumn.getIsCompositeKey()){
					isCompositeKey =1 ;
				}
				
				metaTableColumnDump.append("insert  into `ETEC_RE_TABLE_COLUMNS`(`id`,`auto_generation_id`,`chinese_name`,`default_value`,`id_number`,`is_unique_key`,`length`,`name`,`column_type`,`table_id`,`is_composite_key`) values ");
				metaTableColumnDump.append("('"+meatTableColumn.getId()+"','"+meatTableColumn.getAutoGenerationId()+"','"+meatTableColumn.getChineseName()+"','");
				metaTableColumnDump.append(meatTableColumn.getDefaultValue()+"',"+meatTableColumn.getIdNumber()+","+isUniqueKey+",'"+meatTableColumn.getLength()+"','");
				metaTableColumnDump.append(meatTableColumn.getName()+"','"+meatTableColumn.getType()+"','"+meatTableColumn.getTable().getId()+"',"+isCompositeKey+");\n");
			}
		}else{
			metaTableColumnDump.append("insert  into `ETEC_RE_TABLE_COLUMNS`(`id`,`auto_generation_id`,`chinese_name`,`default_value`,`id_number`,`is_unique_key`,`length`,`name`,`column_type`,`table_id`,`is_composite_key`) values ");
			for(MetaTableColumns meatTableColumn:metaTableColumnSet){
				metaTableColumnDump.append("('"+meatTableColumn.getId()+"','"+meatTableColumn.getAutoGenerationId()+"','"+meatTableColumn.getChineseName()+"','");
				metaTableColumnDump.append(meatTableColumn.getDefaultValue()+"',"+meatTableColumn.getIdNumber()+","+meatTableColumn.getIsUniquekey()+",'"+meatTableColumn.getLength()+"','");
				metaTableColumnDump.append(meatTableColumn.getName()+"','"+meatTableColumn.getType()+"','"+meatTableColumn.getTable().getId()+"',"+meatTableColumn.getIsCompositeKey()+"),");
			}
			if(metaTableColumnDump.lastIndexOf(",")>0){
				metaTableColumnDump.deleteCharAt(metaTableColumnDump.lastIndexOf(","));
			}
			metaTableColumnDump.append(metaTableColumnDump+";");
		}
		
		return metaTableColumnDump.toString();
	}
	
	/**
	 * To construct query for meta table relation
	 * @param metaTableRelationList
	 * @param metaTableRelationSet
	 * @return
	 * @throws EazyBpmException
	 */
	public String  getMetaTableRelationDump(List<MetaTableRelation> metaTableRelationList,Set<MetaTableRelation> metaTableRelationSet,boolean isTableAlter)throws EazyBpmException {
		StringBuffer relationTableDump = new StringBuffer();
		StringBuffer metaTableRelationDump = new StringBuffer();
		
		metaTableRelationDump.append("\n\n\n\n");
		metaTableRelationDump.append("insert  into `ETEC_RE_TABLE_RELATION`(`id`,`child_key_column_id`,`child_key_column_name`,`foreign_key_column_id`,`foreign_key_column_name`,`child_table_id`,`parent_table_id`,`table_id`) values");
	
		for(MetaTableRelation metaTableRelation:metaTableRelationList){
			String parentTableId=null;
			String childTableId=null;
			if(metaTableRelation.getParentTable()!=null){
				parentTableId="'"+metaTableRelation.getParentTable().getId()+"'";
			}
			
			if(metaTableRelation.getChildTable()!=null){
				childTableId="'"+metaTableRelation.getChildTable().getId()+"'";
			}
			metaTableRelationDump.append("('"+metaTableRelation.getId()+"','"+metaTableRelation.getChildKeyColumnId()+"','"+metaTableRelation.getChildKeyColumnName()+"','");
			metaTableRelationDump.append(metaTableRelation.getForeignKeyColumnId()+"','"+metaTableRelation.getForeignKeyColumnName()+"',"+childTableId+",");
			metaTableRelationDump.append(parentTableId+",'"+metaTableRelation.getTable().getId()+"'),");
		}
		if(metaTableRelationDump.lastIndexOf(",")>0){
			metaTableRelationDump.deleteCharAt(metaTableRelationDump.lastIndexOf(","));
		}
		metaTableRelationDump.append(";\n\n\n");
		
		if(isTableAlter){
			//StringBuffer metaRelationQuerys = new StringBuffer();
			
			for(MetaTableRelation metaTableRelation:metaTableRelationSet){
				/*String parentTableName="";
				String childTableName="";*/
				MetaTable relationDumpTable=null;
				if(metaTableRelation.getParentTable()==null){
					//ALTER TABLE ETEC_RU_BBBB ADD CONSTRAINT fk_TEST_ID_BBBB FOREIGN KEY (RAJU) REFERENCES ETEC_RU_TEST(ID)
				/*	parentTableName=metaTableRelation.getTable().getTableName();
					childTableName=metaTableRelation.getChildTable().getTableName();*/
					relationDumpTable=metaTableRelation.getChildTable();
				}
				
				if(metaTableRelation.getChildTable()==null){
					//ALTER TABLE ETEC_RU_CHILD ADD CONSTRAINT fk_PARENT_AAA_CHILD FOREIGN KEY (BBB) REFERENCES ETEC_RU_PARENT (AAA)
					/*parentTableName=metaTableRelation.getParentTable().getTableName();
					childTableName=metaTableRelation.getTable().getTableName();*/
					relationDumpTable=metaTableRelation.getParentTable();
				}
				relationTableDump.append("\n\n\n"+constructMetaTableDumpForModules(relationDumpTable));
				relationTableDump.append("insert into `ETEC_MODULE_TABLES`(`table_id`,`module_id`) values ('"+relationDumpTable.getId()+"','ff8081813ecaaf35013ecad4681b0001');");
				
				//metaRelationQuerys.append("ALTER TABLE "+childTableName+" add CONSTRAINT fk_"+parentTableName+"_"+metaTableRelation.getForeignKeyColumnName()+"_"+childTableName+" FOREIGN KEY ("+metaTableRelation.getChildKeyColumnName()+") REFERENCES "+parentTableName+"("+metaTableRelation.getForeignKeyColumnName()+");");
			}
			/*if(metaRelationQuerys.lastIndexOf(";")>0){
				metaRelationQuerys.deleteCharAt(metaRelationQuerys.lastIndexOf(";"));
			}*/
			//metaTableRelationDump.append(metaRelationQuerys);
		}
	
		relationTableDump.append(metaTableRelationDump);
		return relationTableDump.toString();
	}
	
	/**
	 * To get the table sql dump
	 * @param tableName
	 * @param schemaDetails
	 * @return
	 * @throws EazyBpmException
	 */
	private String getTableSqlDump(String tableName,String schemaDetails)throws EazyBpmException {
		schemaDetails=schemaDetails+" "+tableName;
		//log.info("Relation table dump query :: "+schemaDetails);
		Runtime rt = Runtime.getRuntime();
		Process child;
		String dump="";
		try {
			child = rt.exec(schemaDetails);
			   InputStream inputStream = child.getInputStream();
			   dump=CommonUtil.getStringFromInputStream(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
		return dump;
	}
	
	/**
	 *{@inheritDoc}
	 */
	public String copyTableToModule(String moduleId,String tableId)throws EazyBpmException{
		MetaTable metaTableObj=tableDao.getTableDetails(tableId);
		Set<Module> oldModules=metaTableObj.getModules();
		int tableModulesSize=oldModules.size();
		
		//Get module
	   	Module module=moduleService.getModule(moduleId);
	   	if(!oldModules.contains(module)){
	   		metaTableObj.addModule(module);
	   		metaTableObj=tableDao.saveTable(metaTableObj);
		}else{
	   		return "Table Name : "+metaTableObj.getTableName()+" is already exits in this Module : "+module.getName();
	   	}
   		
		if(metaTableObj.getModules().size()>tableModulesSize){
			return "true";
		}else{
			return "false";
		}
		
	}
	
	/**
	 *{@inheritDoc}
	 */
	public void executeModuleRelationQuery(String moduleQuery)throws EazyBpmException{
		tableDao.executeModuleRelationQuery(moduleQuery);
	}
	
	/**
	 *{@inheritDoc}
	 */
	public void constructMultiTableDump(OutputStream dumpOutPut,String tableNames,List<String> dataBaseInfo)throws EazyBpmException{
		String[] tableNamesArray=tableNames.split(",");
	
		StringBuffer tableNamesForQuery = new StringBuffer();
		tableNamesForQuery.append("(");
		for(String tableName:tableNamesArray){
			tableNamesForQuery.append("'"+tableName+"',");
		}
		
		if(tableNamesForQuery.lastIndexOf(",")>0){
		  tableNamesForQuery.deleteCharAt(tableNamesForQuery.lastIndexOf(","));
		}
		 tableNamesForQuery.append(")");
         
		 List<MetaTable>  metaTableObjList = getTableDetailsByNames(tableNamesForQuery.toString());
		 Set<MetaTable> metaTableObjs=new HashSet<MetaTable>();
		 metaTableObjs.addAll(metaTableObjList);
		 
		 setNamesToCheck(dumpOutPut,metaTableObjs);
		 
		 //construct table dump and module relation
		 if(!metaTableObjs.isEmpty()){
			 constructTableDumps(metaTableObjs,dumpOutPut,dataBaseInfo,false);
		 }
	}
	
	/**
	 * To set the table names and id's in the dump 
	 * @param dumpOutPut
	 * @param metaTables
	 */
	private void setNamesToCheck(OutputStream dumpOutPut,Set<MetaTable> metaTables){
		StringBuffer checkTableName = new StringBuffer();
		
        checkTableName.append("-- (");
        if(!metaTables.isEmpty()){
        	 for(MetaTable metaTableObj:metaTables){
            	 checkTableName.append("'"+metaTableObj.getTableName()+"',");
             }
        }else{
       	 checkTableName.append("''");
        }
        
        if(checkTableName.lastIndexOf(",")>0){
       	 checkTableName.deleteCharAt(checkTableName.lastIndexOf(","));
		 }
        checkTableName.append(");\n");
        
        checkTableName.append("-- ");
        if(!metaTables.isEmpty()){
        	 for(MetaTable metaTableObj:metaTables){
            	 checkTableName.append("('"+metaTableObj.getId()+"',moduleId),");
             }
        }else{
       	 checkTableName.append("''");
        }
        
        if(checkTableName.lastIndexOf(",")>0){
       	 checkTableName.deleteCharAt(checkTableName.lastIndexOf(","));
		 }
        checkTableName.append(";\n");
        
        constructSqlDump(checkTableName.toString(),dumpOutPut);
     }
	
	 /**
	  * Write the constructed Sql Dump in OutputStream
	  * @param sqlString
	  * @param dumpOutPut
	  */
	 private void constructSqlDump(String sqlString,OutputStream dumpOutPut){
		 try {
			 
			 if(!dataBaseSchema.equals("mysql")){
				 sqlString = sqlString.replaceAll("`", "");
			 }
			 
				//Process child= rt.exec(aaaa);
				InputStream in =new ByteArrayInputStream(sqlString.getBytes());
		         int ch;
		         while ((ch = in.read()) != -1) {
		             dumpOutPut.write(ch);
		         }
		        
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 }
	 
	 /**
	 *{@inheritDoc}
	 */
	 public void constructTableDumps(Set<MetaTable> metaTables,OutputStream dumpOutPut,List<String> dataBaseInfo,boolean isWholeDump)throws EazyBpmException{
		 String dataBaseName= dataBaseInfo.get(0);
         String bdUserName=dataBaseInfo.get(1);
         String bdPassword=dataBaseInfo.get(2);
         String ip="localhost";
         String port="3306";
         Runtime rt = Runtime.getRuntime();
         Set<MetaTableRelation> metaTableRelationSet=new HashSet<MetaTableRelation>();
         StringBuffer metaTableDumpQuery = new StringBuffer();
         
         metaTableDumpQuery.append("mysqldump  -h " + ip + " -u" + bdUserName +" -p" + bdPassword);
         if(!isWholeDump){
        	 metaTableDumpQuery.append(" --no-create-info --complete-insert ");
         }
         metaTableDumpQuery.append(" "+dataBaseName);
         
         for(MetaTable metaTableObj:metaTables){
        	 String tableName=metaTableObj.getTableName();
        	 //Getting the table names for dump
        	 metaTableDumpQuery.append(" "+tableName);
	 	     //Table and column Meta data
		 	    if(isWholeDump){
		 	    	metaTableRelationSet.addAll(metaTableObj.getMetaTableRelation());
		 	    	constructSqlDump(constructMetaTableDumpForModules(metaTableObj),dumpOutPut);
		 	    	 
		 	    	 if(!dataBaseSchema.equals("mysql")){
		 	    		constructSqlDump(constructQuery(metaTableObj)+";",dumpOutPut);
		 	    		 constructSqlDump(exportInsertQuery(tableName),dumpOutPut);
		 	    	 }
		 	    }else{
		 	    	constructSqlDump(constructMetaTableDump(metaTableObj),dumpOutPut);
		 	    	String insertQuery = exportInsertQuery(tableName);
		 	    	try {
		 	    		InputStream insertQueryStream = new ByteArrayInputStream(insertQuery.getBytes());
			         	int ch;
			         	while ((ch = insertQueryStream.read()) != -1) {
		                  dumpOutPut.write(ch);
			         	}
		 	    	}catch (Exception e) {
		 				// TODO Auto-generated catch block
		 				e.printStackTrace();
		 			}
		 	    }
    	     
		    }
         
         try{
        	 if(!metaTableRelationSet.isEmpty()){
	        	 List<MetaTableRelation> metaTableRelationList=new ArrayList<MetaTableRelation>();
	        	 metaTableRelationList.addAll(metaTableRelationSet);
	        	 //Dump of table relationship 
	        	 constructSqlDump(getMetaTableRelationDump(metaTableRelationList,metaTableRelationSet,false),dumpOutPut);
	         }
        	 
	         if(dataBaseSchema.equals("mysql")){
	        	 log.info("Dump  Query :: "+metaTableDumpQuery.toString());
	         	 Process child = rt.exec(metaTableDumpQuery.toString());
	             InputStream in = child.getInputStream();
	             int ch;
	             while ((ch = in.read()) != -1) {
	                 dumpOutPut.write(ch);
	             }
	             
	             InputStream err = child.getErrorStream();
	             while ((ch = err.read()) != -1) {
	                 log.error(ch);
	             }
	         }
	        
         }catch(Exception e) {
	           e.printStackTrace();
         }
         
     }
	 
	 /**
	 *{@inheritDoc}
	 */
	 public void deleteDataByIds(List<String> idList, String tableName)throws EazyBpmException{
		 StringBuffer ids = new StringBuffer();
		 ids.append("(");
		 for(String id:idList){
			 ids.append("'"+id+"',");
		 }
		 
		 if(ids.lastIndexOf(",")>0){
			 ids.deleteCharAt(ids.lastIndexOf(","));
		}
		 ids.append(")");
		 List<String> instIdList = tableDao.deleteDataByIds(ids.toString(), tableName);
		 for (String instId : instIdList) {
			 historyService.deleteHistoricProcessInstance(instId);
		}
	 }
	 
	 /**
	 *{@inheritDoc}
	 */
	 public void runtimeTableStatusChange(List<Map<String,Object>> taskDetailsMap)throws EazyBpmException{
		 StringBuffer executionIds = new StringBuffer();
		 executionIds.append("(");
		 for(Map<String,Object> taskDetails:taskDetailsMap){
			 String processId=String.valueOf(taskDetails.get("executionId"));
			// String taskId=String.valueOf(taskDetails.get("taskId"));
			 executionIds.append("'"+processId+"',");
			 List<String> tableNames=tableDao.getTableNamesByProcessIds(processId);
			 
			 for(String tableName:tableNames){
				 //String updateQuery="UPDATE "+ tableName +" SET IS_DELETE=TRUE WHERE TASK_ID='"+taskId+"' AND PROC_INST_ID='"+processId+"'";
				 String updateQuery="UPDATE "+ tableName +" SET IS_DELETE=1 WHERE PROC_INST_ID='"+processId+"'";
				 tableDao.updateRuntimeTableStatus(updateQuery);
			 }
		 }
		 
		 if(executionIds.lastIndexOf(",")>0){
			 executionIds.deleteCharAt(executionIds.lastIndexOf(","));
		}
		 executionIds.append(")");
		 		 
		 tableDao.deleteProcessTableDetails(executionIds.toString());
	 }
	 
	 public MetaTable getTableById(String tableId) throws EazyBpmException{
		 return tableDao.getTableById(tableId);
	 }
	 
	 /**
	  * {@inheritDoc}
	  */
	 public List<MetaTableColumns> getAllMetaTableColumnsByTableId(String tableId, String defaultColumns) throws EazyBpmException {
		StringBuffer filterCondition=new StringBuffer();
		String[] defaultFields = defaultColumns.split("-");
		for(int index=0;index<defaultFields.length;index++){
			String[] columnField=defaultFields[index].split(",");
			filterCondition.append(" and tableColumns.name!='"+columnField[0]+"' ");
		}
		return tableDao.getAllMetaTableColumnsByTableId(tableId, filterCondition.toString());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public MetaForm setFormFieldResources(MetaForm metaForm, List<MetaTableColumns> tableColumnList) throws EazyBpmException {
		StringBuffer htmlSource = new StringBuffer();
		StringBuffer xmlSource = new StringBuffer();
		String formName = metaForm.getFormName();
		htmlSource.append("<form action ='#' enctype = 'multipart/form-data' id = '"+formName+"' method='post' name = '"+formName+"' table='"+metaForm.getTable().getTableName()+"' style = 'min-height:181px;width:1096.4285714285716px'><div id='form_div'>");
		xmlSource.append("<?xml version='1.0' encoding='utf-8'?><form initiator = '"+formName+"' id='"+formName+"' name='"+formName+"' action='#' label='"+formName+"' tableName='"+metaForm.getTable().getTableName()+"' tableId='"+metaForm.getTable().getId()+"'  method='post'><extensionElements>");
		for(MetaTableColumns metaTableColumn : tableColumnList){
			if(metaTableColumn.getType().equalsIgnoreCase("TINYTEXT") || metaTableColumn.getType().equalsIgnoreCase("BLOB") || metaTableColumn.getType().equalsIgnoreCase("TEXT") || metaTableColumn.getType().equalsIgnoreCase("MEDIUMBLOB") || metaTableColumn.getType().equalsIgnoreCase("MEDIUMTEXT") || metaTableColumn.getType().equalsIgnoreCase("LONGBLOB") || metaTableColumn.getType().equalsIgnoreCase("LONGTEXT")){
				htmlSource.append("<p>&nbsp</p>");
				htmlSource.append("<p>"+metaTableColumn.getChineseName()+"&nbsp&nbsp&nbsp:&nbsp&nbsp&nbsp<textarea column='"+metaTableColumn.getId()+"' id='"+metaForm.getEnglishName()+"_"+metaTableColumn.getName()+"' name='"+metaTableColumn.getName()+"' table='"+metaTableColumn.getTable().getId()+"'></textarea></p>");
				xmlSource.append("<formProperty type= 'textarea' column='"+metaTableColumn.getId()+"' id='"+formName+"_"+metaTableColumn.getName()+"' name='"+metaTableColumn.getName()+"' table='"+metaTableColumn.getTable().getId()+"' isSubForm='false' rows='' cols=''></formProperty>");
			} else if(metaTableColumn.getType().equalsIgnoreCase("BOOLEAN")){
				htmlSource.append("<p>&nbsp</p>");
				htmlSource.append("<p>"+metaTableColumn.getChineseName()+"&nbsp&nbsp&nbsp:&nbsp&nbsp&nbsp<input type='checkbox' column='"+metaTableColumn.getId()+"' id='"+metaForm.getEnglishName()+"_"+metaTableColumn.getName()+"' name='"+metaTableColumn.getName()+"' table='"+metaTableColumn.getTable().getId()+"' optionType='static' datadictionary='' datadictionarylabel = '' value='' ></p>");
				xmlSource.append("<formProperty type= 'checkbox' isSubForm='false' column='"+metaTableColumn.getId()+"' id='"+formName+"_"+metaTableColumn.getName()+"' name='"+metaTableColumn.getName()+"' table='"+metaTableColumn.getTable().getId()+"' optionType='static' dataDictionary='' value=''> </formProperty>");
			} else if(metaTableColumn.getType().equalsIgnoreCase("TIMESTAMP")){
				htmlSource.append("<p>&nbsp</p>");
				htmlSource.append("<p>"+metaTableColumn.getChineseName()+"&nbsp&nbsp&nbsp:&nbsp&nbsp&nbsp<input class='datepicker' column='"+metaTableColumn.getId()+"' id='"+metaForm.getEnglishName()+"_"+metaTableColumn.getName()+"' name='"+metaTableColumn.getName()+"' table='"+metaTableColumn.getTable().getId()+"' type='date'></p>");
				xmlSource.append("<formProperty type= 'date' column='"+metaTableColumn.getId()+"' id='"+formName+"_"+metaTableColumn.getName()+"' name='"+metaTableColumn.getName()+"' table='"+metaTableColumn.getTable().getId()+"' isSubForm='false'  fieldClass='datepicker' optionType='null'></formProperty>");
			} else if(metaTableColumn.getType().equalsIgnoreCase("DATE") || metaTableColumn.getType().equalsIgnoreCase("TIME") || metaTableColumn.getType().equalsIgnoreCase("DATETIME")){
				htmlSource.append("<p>&nbsp</p>");
				htmlSource.append("<p>"+metaTableColumn.getChineseName()+"&nbsp&nbsp&nbsp:&nbsp&nbsp&nbsp<input class='datetimepicker' column='"+metaTableColumn.getId()+"' id='"+metaForm.getEnglishName()+"_"+metaTableColumn.getName()+"' name='"+metaTableColumn.getName()+"' table='"+metaTableColumn.getTable().getId()+"' type='date'></p>");
				xmlSource.append("<formProperty type= 'date' column='"+metaTableColumn.getId()+"' id='"+formName+"_"+metaTableColumn.getName()+"' name='"+metaTableColumn.getName()+"' table='"+metaTableColumn.getTable().getId()+"' isSubForm='false'  fieldClass='datetimepicker' optionType='null'></formProperty>");
			} else if(metaTableColumn.getType().equalsIgnoreCase("INT") || metaTableColumn.getType().equalsIgnoreCase("YEAR") || metaTableColumn.getType().equalsIgnoreCase("TINYINT")  || metaTableColumn.getType().equalsIgnoreCase("SMALLINT") || metaTableColumn.getType().equalsIgnoreCase("MEDIUMINT") || metaTableColumn.getType().equalsIgnoreCase("FLOAT") || metaTableColumn.getType().equalsIgnoreCase("DOUBLE") || metaTableColumn.getType().equalsIgnoreCase("DECIMAL") || metaTableColumn.getType().equalsIgnoreCase("NUMERIC")){
				htmlSource.append("<p>&nbsp</p>");
				htmlSource.append("<p>"+metaTableColumn.getChineseName()+"&nbsp&nbsp&nbsp:&nbsp&nbsp&nbsp<input class='number-validation' column='"+metaTableColumn.getId()+"' id='"+metaForm.getEnglishName()+"_"+metaTableColumn.getName()+"' name='"+metaTableColumn.getName()+"' table='"+metaTableColumn.getTable().getId()+"' type='number'/></p>");
				xmlSource.append("<formProperty type= 'number' fieldClass='number-validation' isSubForm='false'  column='"+metaTableColumn.getId()+"' id='"+formName+"_"+metaTableColumn.getName()+"' name='"+metaTableColumn.getName()+"' table='"+metaTableColumn.getTable().getId()+"' optionType='null'></formProperty>");
			} else {
				htmlSource.append("<p>&nbsp</p>");
				htmlSource.append("<p>"+metaTableColumn.getChineseName()+"&nbsp&nbsp&nbsp:&nbsp&nbsp&nbsp<input column='"+metaTableColumn.getId()+"' id='"+metaForm.getEnglishName()+"_"+metaTableColumn.getName()+"' name='"+metaTableColumn.getName()+"' table='"+metaTableColumn.getTable().getId()+"' type='text'></p>");
				xmlSource.append("<formProperty type= 'text' column='"+metaTableColumn.getId()+"' id='"+formName+"_"+metaTableColumn.getName()+"' name='"+metaTableColumn.getName()+"' table='"+metaTableColumn.getTable().getId()+"' isSubForm='false' optionType='null'></formProperty>");
			}
		}
		htmlSource.append("<script>setDefaultValue('"+formName+"');</script></div></form>");
		xmlSource.append("</extensionElements></form>");
		metaForm.setHtmlSource(htmlSource.toString());
		metaForm.setXmlSource(xmlSource.toString());
		return metaForm;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Map<String, String>> searchModuleTableNames(String tableName) throws EazyBpmException{
		List<Map<String, String>> tableNameList= new ArrayList<Map<String,String>>();
			List<Module> userModule = moduleService.getAllModuleList();
			// Get user modules
			for(Module moduleObj:userModule){
				Set<MetaTable> metaTables= moduleObj.getTables();
				for(MetaTable tableObj:metaTables){
					Map<String, String> tableNameMap = new HashMap<String, String>();
					String tempTableName = tableObj.getTableName();
					String tableSubString = tempTableName.substring(0, tableName.length());
					if(tableName.equalsIgnoreCase(tableSubString)){
						tableNameMap.put("dataName",tableObj.getTableName());
						tableNameMap.put("id",tableObj.getId()+"_"+moduleObj.getId());
						tableNameList.add(tableNameMap);
					}
				}
			}
		return tableNameList;
	}
	public List<String> getTableNames(String tableName)throws EazyBpmException {
		return tableDao.getTableNames(tableName);
	}
	@Override
	public void exportTableSql(String tableId, HttpServletResponse response) {
		 try{
			 MetaTable metaTableObj = getTableDetails(tableId);
			 String tableName=metaTableObj.getTableName();
	          response.setContentType("text/x-sql");
	          response.setHeader("Content-Disposition", "attachment; filename="+tableName+"_"+metaTableObj.getId()+"-.sql");
	          
	            Runtime rt = Runtime.getRuntime();
                OutputStream dumpOutPut = response.getOutputStream();
                int ch;
                
                String metaDataQuery=constructMetaTableDump(metaTableObj);
                if(!dataBaseSchema.equals("mysql")){
                	metaDataQuery =metaDataQuery.replaceAll("`", "");
        		}
                InputStream inMeta = new ByteArrayInputStream(metaDataQuery.getBytes());
	                while ((ch = inMeta.read()) != -1) {
	                    dumpOutPut.write(ch);
	                }
	                
	                String exportCommand = mysqlExportCommand;
	                if(dataBaseSchema.equals("mysql")){
	                	exportCommand += tableName;
	                	Process child = rt.exec(mysqlExportCommand);
	 	                InputStream in = child.getInputStream();  
	 	                while ((ch = in.read()) != -1) {
	 	                    dumpOutPut.write(ch);
	 	                }
	 	               
	 	                InputStream err = child.getErrorStream();
	 	                while ((ch = err.read()) != -1) {
	 	                    log.error(ch);
	 	                }
	                }else{
	                	String insertQuery = exportInsertQuery(tableName);
	                	InputStream insertQueryStream = new ByteArrayInputStream(insertQuery.getBytes());
	 	                while ((ch = insertQueryStream.read()) != -1) {
	 	                    dumpOutPut.write(ch);
	 	                }
	                }
	               
		 }catch (Exception e) {
					e.printStackTrace();
					throw new EazyBpmException(e.getMessage(),e);
	    }
	}
	
	/**
	 * To construct insert query for table export
	 * @param tableName
	 * @return
	 */
	private String exportInsertQuery(String tableName){
		List<String> columnTableNameList = tableDao.getAllColumnsByTableName(tableName);
    	
    	StringBuffer selectColumn=new StringBuffer();
		for(String columns:columnTableNameList){
			selectColumn.append("`"+columns+"`,");
		}
		if(selectColumn.lastIndexOf(",")>0){
			selectColumn.deleteCharAt(selectColumn.lastIndexOf(","));
		}
		
		List listViewObjArray=tableDao.getTableDataForExport(tableName,selectColumn.toString());
		
    	
    	StringBuffer inserQuery = new StringBuffer();
    	
    	if ((listViewObjArray != null) && (listViewObjArray.size() > 0)) {
			for (int i = 0; i < listViewObjArray.size(); i++) {
				inserQuery.append("insert into "+tableName+" (");
	        	
	        	for(String columnTableName :columnTableNameList){
	        		inserQuery.append(columnTableName+",");
	        	}
	        	if(inserQuery.lastIndexOf(",")>0){
	        		inserQuery.deleteCharAt(inserQuery.lastIndexOf(","));
	    		}
	        	inserQuery.append(") values (");
				if (listViewObjArray.get(i) instanceof Object[]) {
	 	  			Object[] listViewObjInfo = (Object[]) listViewObjArray.get(i);
				  	for(int index=0;index<columnTableNameList.size();index++){
				  		//For date field
			 	  		if(listViewObjInfo[index] instanceof Date){
			 	  			Date dateTime = null;
			 	  			String dateStr = null;
			 	  			try {
			 	  				String dbData=String.valueOf(listViewObjInfo[index]);
			 	  				if(dbData.length()>11){
			 	  					dateTime = DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss", dbData);
			 	  					dateStr = DateUtil.convertDateToStringFormat(dateTime,"yyyy-MM-dd HH:mm:ss");
			 	  				}else{
			 	  					dateTime = DateUtil.convertStringToDate("yyyy-MM-dd", dbData);
			 	  					dateStr = DateUtil.convertDateToStringFormat(dateTime,"yyyy-MM-dd");
			 	  				}
								
			 	  				if(dateStr != null){
			 	  					if(!dateStr.equals("null")){
			 	  						inserQuery.append(CommonUtil.convertDateTypeForDB(dataBaseSchema,dateStr)+",");
			 	  					}else{
			 	  						inserQuery.append(null+",");
			 	  					}
			 	  					 
				 	            }else{
				 	            	if(!String.valueOf(dateTime).equals("null")){
				 	            		inserQuery.append(CommonUtil.convertDateTypeForDB(dataBaseSchema,dateStr)+",");
				 	            	}else{
				 	            		inserQuery.append(null+","); 
				 	            	}
				 	            }
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
			 	  		}else if(listViewObjInfo[index] instanceof String ){
			 	  			String content_data=String.valueOf(listViewObjInfo[index]);
			 	  			if(content_data.contains("<script")){
			 	  				content_data=content_data.replace("<script", "<A_script");
			 	  				content_data=content_data.replace("</script>", "</A_script>");
			 	  			}
			 	  			inserQuery.append("'"+listViewObjInfo[index]+"',"); 
			 	  		}else{
			 	  			inserQuery.append(listViewObjInfo[index]+","); 
			 	  		}
			 	  	}
				}
				
				if(inserQuery.lastIndexOf(",")>0){
            		inserQuery.deleteCharAt(inserQuery.lastIndexOf(","));
        		}
            	inserQuery.append(");\n");
			}
		}
    	
        return inserQuery.toString();
	}
	
	public  Map getDataForUniqueKey(String uniquecolumn,String uniqueKey, String tableName)throws EazyBpmException {
		return tableDao.getDataForUniqueKey(uniquecolumn,uniqueKey,tableName);
	}
	
	/**
	 * To construct Query for mysql
	 * @param tableOperation
	 * @param isPreview
	 * @param deleteExistingTableColumns
	 * @return
	 * @throws EazyBpmException
	 */
	private String mySqlAlterQuery(MetaTable tableOperation,boolean isPreview,List<String> deleteExistingTableColumns)throws EazyBpmException {
		Set<MetaTableColumns> fieldPro=new HashSet<MetaTableColumns>();
		
		StringBuffer strbuf = new StringBuffer();
		StringBuffer childTableQuery = new StringBuffer();
		StringBuffer dropUniqueKey=new StringBuffer();
		StringBuffer uniqueKeyQuery = new StringBuffer();
		String tableQuery="ALTER TABLE `"+tableOperation.getTableName()+"` ";
		strbuf.append(tableQuery);
	
		Set<MetaTableColumns> fieldProperty=tableOperation.getMetaTableColumns();
		for(MetaTableColumns fieldPropertyObj:fieldProperty){
			
			if(fieldPropertyObj.getId()!=null){
				MetaTableColumns tempFieldPropertyObj=tableDao.getMetaTableColumnsById(fieldPropertyObj.getId());
				if(!tempFieldPropertyObj.getType().equalsIgnoreCase(fieldPropertyObj.getType())){
					strbuf.append(" DROP `"+tempFieldPropertyObj.getName()+"`, ADD COLUMN `"+fieldPropertyObj.getName()+"` "+fieldPropertyObj.getType());
				}else{
					strbuf.append(" CHANGE `"+tempFieldPropertyObj.getName()+"` `"+fieldPropertyObj.getName()+"` "+fieldPropertyObj.getType());
				}
				
				if((tempFieldPropertyObj.getIsUniquekey() || tempFieldPropertyObj.getIsCompositeKey()) && !tempFieldPropertyObj.getName().equals("ID")){
					dropUniqueKey.append(";"+tableQuery+" DROP KEY UK_"+tableOperation.getTableName()+"_"+fieldPropertyObj.getName());
				}
				
				if(!isPreview){
					tempFieldPropertyObj.setName(fieldPropertyObj.getName());
					tempFieldPropertyObj.setChineseName(fieldPropertyObj.getChineseName());
					tempFieldPropertyObj.setType(fieldPropertyObj.getType());
					tempFieldPropertyObj.setLength(fieldPropertyObj.getLength());
					tempFieldPropertyObj.setDefaultValue(fieldPropertyObj.getDefaultValue());
					tempFieldPropertyObj.setAutoGenerationId(fieldPropertyObj.getAutoGenerationId());
					tempFieldPropertyObj.setIsUniquekey(fieldPropertyObj.getIsUniquekey());
					tempFieldPropertyObj.setIsCompositeKey(fieldPropertyObj.getIsCompositeKey());
					fieldPro.add(tempFieldPropertyObj);
				}
			}else{
				strbuf.append(" ADD COLUMN `"+fieldPropertyObj.getName()+"` "+fieldPropertyObj.getType());
				if(!isPreview){
					fieldPro.add(fieldPropertyObj);
				}
			}
			
			setDefaultValueAndLength(fieldPropertyObj,strbuf);
			
			if((fieldPropertyObj.getIsUniquekey() || fieldPropertyObj.getIsCompositeKey()) && !fieldPropertyObj.getName().equals("ID")){
				uniqueKeyQuery.append(";"+tableQuery+" ADD CONSTRAINT UK_"+tableOperation.getTableName()+"_"+fieldPropertyObj.getName()+" UNIQUE ("+fieldPropertyObj.getName()+",PROC_INST_ID,TASK_ID)");
				//uniqueKeyQuery.append(";"+tableQuery+" ADD UNIQUE KEY "+fieldPropertyObj.getName()+" ("+fieldPropertyObj.getName()+")");
			}
		}
		
		Set<MetaTableRelation> metaTableRelation=tableOperation.getMetaTableRelation();
		if(!metaTableRelation.isEmpty()){
			boolean hasParent=false;
			boolean hasChild=false;
			MetaTable metatable=tableDao.getTableDetails(tableOperation.getId());
			StringBuffer foreignKeyBuffer = new StringBuffer();
			for(MetaTableRelation tableRelation:metaTableRelation){
				if(tableRelation.getParentTable()!=null  && tableOperation.getId()!=tableRelation.getParentTable().getId()){
					hasParent=true;
					String parentTableName=tableRelation.getParentTable().getTableName();
					String foreignKeyColumnName = tableRelation.getForeignKeyColumnName();
					String childKeyColumnName=tableRelation.getChildKeyColumnName();
					//strbuf.append(" ADD COLUMN `"+parentTableName+"_"+foreignKeyColumnName+"` VARCHAR (100) DEFAULT NULL,");
					//foreignKeyBuffer.append(",ADD CONSTRAINT fk_"+parentTableName+"_"+foreignKeyColumnName+"_"+tableOperation.getTableName()+" FOREIGN KEY ("+parentTableName+"_"+foreignKeyColumnName+") REFERENCES ETEC_RU_"+parentTableName+"("+foreignKeyColumnName+")");
					foreignKeyBuffer.append("ADD CONSTRAINT fk_"+parentTableName+"_"+foreignKeyColumnName+"_"+tableOperation.getTableName()+" FOREIGN KEY ("+childKeyColumnName+") REFERENCES `"+parentTableName+"` ("+foreignKeyColumnName+"),");
				}else if(tableRelation.getChildTable()!=null && tableOperation.getId()!=tableRelation.getChildTable().getId() ){
					hasChild=true;
					String childTableName=tableRelation.getChildTable().getTableName();
					String foreignKeyColumnName = tableRelation.getForeignKeyColumnName();
					String parentTableName=tableOperation.getTableName();
					String childKeyColumnName=tableRelation.getChildKeyColumnName();
					//childTableQuery.append(";ALTER TABLE ETEC_RU_"+childTableName+" ADD `"+parentTableName+"_"+foreignKeyColumnName+"` VARCHAR (100) DEFAULT NULL ,ADD CONSTRAINT fk_"+parentTableName+"_"+foreignKeyColumnName+"_"+childTableName+" FOREIGN KEY ("+parentTableName+"_"+foreignKeyColumnName+") REFERENCES ETEC_RU_"+parentTableName+" ("+foreignKeyColumnName+")");
					childTableQuery.append(";ALTER TABLE `"+childTableName+"` ADD CONSTRAINT fk_"+parentTableName+"_"+foreignKeyColumnName+"_"+childTableName+" FOREIGN KEY ("+childKeyColumnName+") REFERENCES `"+parentTableName+"` ("+foreignKeyColumnName+")");
				}
			}
			
			if(strbuf.lastIndexOf(",")>0){
				strbuf.deleteCharAt(strbuf.lastIndexOf(","));
			}
			
			if(foreignKeyBuffer.lastIndexOf(",")>0){
				foreignKeyBuffer.deleteCharAt(foreignKeyBuffer.lastIndexOf(","));
			}
			
			if(hasParent){
				strbuf.append(foreignKeyBuffer);
				if(!isPreview){
					dropPreviousData(metatable,"parent");
				}
				
			}
			
			if(hasChild){
				if(tableQuery.length()==strbuf.length()){
					if(!hasParent){
						childTableQuery.deleteCharAt(0);
					}
					strbuf=new StringBuffer();
					strbuf.append(childTableQuery);
				}else{
					strbuf.append(childTableQuery);
				}
				if(!isPreview){
					dropPreviousData(metatable,"child");
				}
			}
			
		}else{
			if(strbuf.lastIndexOf(",")>0){
				strbuf.deleteCharAt(strbuf.lastIndexOf(","));
			}
		}
		
		if(!deleteExistingTableColumns.isEmpty()){
			StringBuffer deleteExistingColumnQuery = new StringBuffer();
			deleteExistingColumnQuery.append("ALTER TABLE `"+tableOperation.getTableName()+"` ");
			for(String collumnId:deleteExistingTableColumns){
				MetaTableColumns tempFieldPropertyObj=tableDao.getMetaTableColumnsById(collumnId);
				deleteExistingColumnQuery.append(" DROP `"+tempFieldPropertyObj.getName()+"`,");
			}
			
			if(deleteExistingColumnQuery.lastIndexOf(",")>0){
				deleteExistingColumnQuery.deleteCharAt(deleteExistingColumnQuery.lastIndexOf(","));
			}
			
			if(tableQuery.length()==strbuf.length()){
				strbuf=deleteExistingColumnQuery;
			}else{
				strbuf.append(";"+deleteExistingColumnQuery);
			}
			
		}
		
		//Drop the existing unique key
		if(dropUniqueKey.length()>0){
			strbuf.append(dropUniqueKey);
		}
		
		//Alter and add new unique key
		if(uniqueKeyQuery.length()>0){
			strbuf.append(uniqueKeyQuery);
		}
		
		if(tableQuery.length()==strbuf.length()){
			/*log.info("===============================================================================================");
			log.info("Constructed Query: No Changes has been done in the Table Fields");
			log.info("===============================================================================================");*/
			return "No Changes has been done for Query Preview";
		}
		
		/*log.info("===================================================================================================");
		log.info("Constructed Query "+strbuf);
		log.info("===================================================================================================");
		*/
		if(!isPreview){
			saveFieldPropertys(fieldPro);
			saveTableRelation(metaTableRelation);
			if(!deleteExistingTableColumns.isEmpty()){
				deleteExistingTableColumnsByIds(deleteExistingTableColumns);
			}
			/* Added by Nithi - This functionality has been added to clear the process definitions 
			 * from the cache is any changes has been made in the table
			 * This will help to reload the table changes while starting corresponding process*/
			repositoryService.clearProcessDefinitionsFromCache();
			/* Added by Nithi*/
		}
	
		return strbuf.toString();
	}
	
	/**
	 * To construct Query for mysql
	 * @param tableOperation
	 * @param isPreview
	 * @param deleteExistingTableColumns
	 * @return
	 * @throws EazyBpmException
	 */
	private String oracleAlterQuery(MetaTable tableOperation,boolean isPreview,List<String> deleteExistingTableColumns)throws EazyBpmException {
		Set<MetaTableColumns> fieldPro=new HashSet<MetaTableColumns>();
		
		StringBuffer finalQuery = new StringBuffer();
		StringBuffer renameQuery = new StringBuffer();
		StringBuffer modifyQuery = new StringBuffer();
		StringBuffer dropQuery = new StringBuffer();
		
		boolean isDropQuery = false;
		boolean isRenameQuery = false;
		boolean isModifyQuery = false;
		boolean isStrbuf = false;
		boolean hasParent=false;
		boolean hasChild=false;
		boolean isDeleteExistingColumnQuery = false;
		
		StringBuffer strbuf = new StringBuffer();
		StringBuffer foreignKeyBuffer = new StringBuffer();
		StringBuffer childTableQuery = new StringBuffer();
		StringBuffer dropUniqueKey=new StringBuffer();
		StringBuffer uniqueKeyQuery = new StringBuffer();
		StringBuffer deleteExistingColumnQuery = new StringBuffer();
		
		String tableQuery="ALTER TABLE `"+tableOperation.getTableName()+"` ";
		strbuf.append(tableQuery+" ADD (");
		
		dropQuery.append(tableQuery+" DROP (");
	
		Set<MetaTableColumns> fieldProperty=tableOperation.getMetaTableColumns();
		for(MetaTableColumns fieldPropertyObj:fieldProperty){
			if(fieldPropertyObj.getId()!=null){
				MetaTableColumns tempFieldPropertyObj=tableDao.getMetaTableColumnsById(fieldPropertyObj.getId());
				if(!tempFieldPropertyObj.getType().equalsIgnoreCase(fieldPropertyObj.getType())){
					dropQuery.append(tempFieldPropertyObj.getName()+",");
					strbuf.append(" `"+fieldPropertyObj.getName()+"` "+fieldPropertyObj.getType());
					setDefaultValueAndLength(fieldPropertyObj,strbuf);
					isStrbuf = true;
					isDropQuery = true;
				}else{
					if(!tempFieldPropertyObj.getName().equalsIgnoreCase(fieldPropertyObj.getName())){
						renameQuery.append(tableQuery+" RENAME COLUMN "+tempFieldPropertyObj.getName()+" to "+fieldPropertyObj.getName()+";");
						isRenameQuery = true;
					}
					
					if(!tempFieldPropertyObj.getDefaultValue().equalsIgnoreCase(fieldPropertyObj.getDefaultValue()) || !tempFieldPropertyObj.getLength().equalsIgnoreCase(fieldPropertyObj.getLength())){
						modifyQuery.append(tableQuery+" modify "+fieldPropertyObj.getName()+" "+fieldPropertyObj.getType());
						setDefaultValueAndLength(fieldPropertyObj,modifyQuery);
						
						if(modifyQuery.lastIndexOf(",")>0){
							modifyQuery.deleteCharAt(modifyQuery.lastIndexOf(","));
						}
						modifyQuery.append(";");
						isModifyQuery = true;
					}
				}
				
				if((tempFieldPropertyObj.getIsUniquekey() || tempFieldPropertyObj.getIsCompositeKey()) && !tempFieldPropertyObj.getName().equals("ID")){
					dropUniqueKey.append(";"+tableQuery+" DROP CONSTRAINT UK_"+tableOperation.getTableName()+"_"+fieldPropertyObj.getName());
				}
				
				if(!isPreview){
					tempFieldPropertyObj.setName(fieldPropertyObj.getName());
					tempFieldPropertyObj.setChineseName(fieldPropertyObj.getChineseName());
					tempFieldPropertyObj.setType(fieldPropertyObj.getType());
					tempFieldPropertyObj.setLength(fieldPropertyObj.getLength());
					tempFieldPropertyObj.setDefaultValue(fieldPropertyObj.getDefaultValue());
					tempFieldPropertyObj.setAutoGenerationId(fieldPropertyObj.getAutoGenerationId());
					tempFieldPropertyObj.setIsUniquekey(fieldPropertyObj.getIsUniquekey());
					tempFieldPropertyObj.setIsCompositeKey(fieldPropertyObj.getIsCompositeKey());
					fieldPro.add(tempFieldPropertyObj);
				}
			}else{
				strbuf.append("`");
				strbuf.append(fieldPropertyObj.getName()+"` "+fieldPropertyObj.getType());
				setDefaultValueAndLength(fieldPropertyObj,strbuf);
				isStrbuf = true;
				if(!isPreview){
					fieldPro.add(fieldPropertyObj);
				}
			}

			if((fieldPropertyObj.getIsUniquekey() || fieldPropertyObj.getIsCompositeKey())  && !fieldPropertyObj.getName().equals("ID")){
				if(fieldPropertyObj.getIsUniquekey()){
					uniqueKeyQuery.append(";"+tableQuery+" ADD CONSTRAINT UK_"+tableOperation.getTableName()+"_"+fieldPropertyObj.getName()+" UNIQUE ("+fieldPropertyObj.getName()+");");
				}else{
					uniqueKeyQuery.append(";"+tableQuery+" ADD CONSTRAINT UK_"+tableOperation.getTableName()+"_"+fieldPropertyObj.getName()+" UNIQUE ("+fieldPropertyObj.getName()+",TASK_ID,PROC_INST_ID);");
				}
			}
		}
		
		if(dropQuery.lastIndexOf(",")>0){
			dropQuery.deleteCharAt(dropQuery.lastIndexOf(","));
		}
		dropQuery.append(");");
		strbuf.append(" )");
		
		Set<MetaTableRelation> metaTableRelation=tableOperation.getMetaTableRelation();
		if(!metaTableRelation.isEmpty()){
			MetaTable metatable=tableDao.getTableDetails(tableOperation.getId());
			for(MetaTableRelation tableRelation:metaTableRelation){
				if(tableRelation.getParentTable()!=null  && tableOperation.getId()!=tableRelation.getParentTable().getId()){
					hasParent=true;
					String parentTableName=tableRelation.getParentTable().getTableName();
					String foreignKeyColumnName = tableRelation.getForeignKeyColumnName();
					String childKeyColumnName=tableRelation.getChildKeyColumnName();
					//strbuf.append(" ADD COLUMN `"+parentTableName+"_"+foreignKeyColumnName+"` VARCHAR (100) DEFAULT NULL,");
					//foreignKeyBuffer.append(",ADD CONSTRAINT fk_"+parentTableName+"_"+foreignKeyColumnName+"_"+tableOperation.getTableName()+" FOREIGN KEY ("+parentTableName+"_"+foreignKeyColumnName+") REFERENCES ETEC_RU_"+parentTableName+"("+foreignKeyColumnName+")");
					String constrainKey = "fk_"+parentTableName+"_"+foreignKeyColumnName+"_"+tableOperation.getTableName();
					if(constrainKey.length() > 30 ){
						constrainKey = "fk_"+parentTableName+"_"+foreignKeyColumnName;
					}
					foreignKeyBuffer.append(tableQuery+" ADD CONSTRAINT "+constrainKey+" FOREIGN KEY ("+childKeyColumnName+") REFERENCES `"+parentTableName+"` ("+foreignKeyColumnName+");");
				}else if(tableRelation.getChildTable()!=null && tableOperation.getId()!=tableRelation.getChildTable().getId() ){
					hasChild=true;
					String childTableName=tableRelation.getChildTable().getTableName();
					String foreignKeyColumnName = tableRelation.getForeignKeyColumnName();
					String parentTableName=tableOperation.getTableName();
					String childKeyColumnName=tableRelation.getChildKeyColumnName();
					
					String constrainKey = "fk_"+parentTableName+"_"+foreignKeyColumnName+"_"+childTableName;
					if(constrainKey.length() > 30 ){
						constrainKey = "fk_"+parentTableName+"_"+foreignKeyColumnName;
					}
					
					//childTableQuery.append(";ALTER TABLE ETEC_RU_"+childTableName+" ADD `"+parentTableName+"_"+foreignKeyColumnName+"` VARCHAR (100) DEFAULT NULL ,ADD CONSTRAINT fk_"+parentTableName+"_"+foreignKeyColumnName+"_"+childTableName+" FOREIGN KEY ("+parentTableName+"_"+foreignKeyColumnName+") REFERENCES ETEC_RU_"+parentTableName+" ("+foreignKeyColumnName+")");
					childTableQuery.append(";ALTER TABLE `"+childTableName+"` ADD CONSTRAINT "+constrainKey+" FOREIGN KEY ("+childKeyColumnName+") REFERENCES `"+parentTableName+"` ("+foreignKeyColumnName+")");
				}
			}
			
			
			if(!isPreview){
				if(hasParent){
					dropPreviousData(metatable,"parent");
				}
				
				if(hasChild){
					dropPreviousData(metatable,"child");
				}
			}
			/*
			if(strbuf.lastIndexOf(",")>0){
				strbuf.deleteCharAt(strbuf.lastIndexOf(","));
			}
			
			if(foreignKeyBuffer.lastIndexOf(",")>0){
				foreignKeyBuffer.deleteCharAt(foreignKeyBuffer.lastIndexOf(","));
			}
			
			if(hasParent){
				strbuf.append(foreignKeyBuffer);
				isStrbuf = true;
				if(!isPreview){
					dropPreviousData(metatable,"parent");
				}
				
			}
			
			if(hasChild){
				if(tableQuery.length()==strbuf.length()){
					if(!hasParent){
						childTableQuery.deleteCharAt(0);
					}
					strbuf=new StringBuffer();
					strbuf.append(childTableQuery);
					isStrbuf = true;
				}else{
					strbuf.append(childTableQuery);
					isStrbuf = true;
				}
				if(!isPreview){
					dropPreviousData(metatable,"child");
				}
			}*/
			
		}
		
		if(strbuf.lastIndexOf(",")>0){
			strbuf.deleteCharAt(strbuf.lastIndexOf(","));
		}
		
		if(!deleteExistingTableColumns.isEmpty()){
			deleteExistingColumnQuery.append("ALTER TABLE `"+tableOperation.getTableName()+"` ");
			for(String collumnId:deleteExistingTableColumns){
				MetaTableColumns tempFieldPropertyObj=tableDao.getMetaTableColumnsById(collumnId);
				deleteExistingColumnQuery.append(" DROP COLUMN `"+tempFieldPropertyObj.getName()+"`,");
			}
			
			if(deleteExistingColumnQuery.lastIndexOf(",")>0){
				deleteExistingColumnQuery.deleteCharAt(deleteExistingColumnQuery.lastIndexOf(","));
			}
			deleteExistingColumnQuery.append(";");
			isDeleteExistingColumnQuery = true;
		}
		
		//Drop the existing unique key
		if(dropUniqueKey.length()>0){
			if(!isStrbuf){
				strbuf = new StringBuffer();
			}
			strbuf.append(dropUniqueKey);
			isStrbuf = true;
		}
		
		//Alter and add new unique key
		if(uniqueKeyQuery.length()>0){
			if(!isStrbuf){
				strbuf = new StringBuffer();
			}
			strbuf.append(uniqueKeyQuery);
			isStrbuf = true;
		}
		
		if(tableQuery.length()==strbuf.length()){
			/*log.info("===============================================================================================");
			log.info("Constructed Query: No Changes has been done in the Table Fields");
			log.info("===============================================================================================");*/
			return "No Changes has been done for Query Preview";
		}
		
		if(!isPreview){
			saveFieldPropertys(fieldPro);
			saveTableRelation(metaTableRelation);
			if(!deleteExistingTableColumns.isEmpty()){
				deleteExistingTableColumnsByIds(deleteExistingTableColumns);
			}
			/* Added by Nithi - This functionality has been added to clear the process definitions 
			 * from the cache is any changes has been made in the table
			 * This will help to reload the table changes while starting corresponding process*/
			repositoryService.clearProcessDefinitionsFromCache();
			/* Added by Nithi*/
		}
		
		if(isDeleteExistingColumnQuery){
			finalQuery.append(deleteExistingColumnQuery);
		}
		
		if(isDropQuery){
			finalQuery.append(dropQuery.toString());
		}
		
		if(isRenameQuery){
			finalQuery.append(renameQuery.toString());
		}
		
		if(isModifyQuery){
			finalQuery.append(modifyQuery.toString());
		}
		
		if(isStrbuf){
			finalQuery.append(strbuf.toString());
		}
		
		if(hasParent){
			finalQuery.append(foreignKeyBuffer);
		}
		
		if(hasChild){
			finalQuery.append(childTableQuery);
		}
				
		return finalQuery.toString().replaceAll("`", "");
	}
	
	
	
	
	
	/**
	 * To construct Query for mysql
	 * @param tableOperation
	 * @param isPreview
	 * @param deleteExistingTableColumns
	 * @return
	 * @throws EazyBpmException
	 */
	private String msSqlAlterQuery(MetaTable tableOperation,boolean isPreview,List<String> deleteExistingTableColumns)throws EazyBpmException {
		Set<MetaTableColumns> fieldPro=new HashSet<MetaTableColumns>();
		
		StringBuffer finalQuery = new StringBuffer();
		StringBuffer renameQuery = new StringBuffer();
		StringBuffer modifyQuery = new StringBuffer();
		StringBuffer dropQuery = new StringBuffer();
		StringBuffer dropConstraintQuery = new StringBuffer();
		StringBuffer foreignKeyBuffer = new StringBuffer();
		StringBuffer uniqueConstraint = new StringBuffer();
 
		
		boolean isDropQuery = false;
		boolean isRenameQuery = false;
		boolean isModifyQuery = false;
		boolean isStrbuf = false;
		boolean isDropConstraintQuery = false;
		boolean hasParent=false;
		boolean hasChild=false;
		boolean isDeleteExistingColumnQuery = false;
		
		StringBuffer strbuf = new StringBuffer();
		StringBuffer childTableQuery = new StringBuffer();
		StringBuffer dropUniqueKey=new StringBuffer();
		StringBuffer uniqueKeyQuery = new StringBuffer();
		StringBuffer deleteExistingColumnQuery = new StringBuffer();
		
		String tableQuery="ALTER TABLE `"+tableOperation.getTableName()+"` ";
		
		strbuf.append(tableQuery+" ADD  ");
		
		Set<MetaTableColumns> fieldProperty=tableOperation.getMetaTableColumns();
	
		for(MetaTableColumns fieldPropertyObj:fieldProperty){
			dropQuery.append(tableQuery+" DROP COLUMN ");
			dropConstraintQuery.append(tableQuery+" DROP CONSTRAINT ");
			
			//boolean isDefaultNeeded = true ;
			if(fieldPropertyObj.getId()!=null){
				MetaTableColumns tempFieldPropertyObj=tableDao.getMetaTableColumnsById(fieldPropertyObj.getId());
				if(!tempFieldPropertyObj.getType().equalsIgnoreCase(fieldPropertyObj.getType())){
					String columnDefaultConstraint = tableDao.getColumnDefaultConstraint(tableOperation.getTableName() , tempFieldPropertyObj.getName());
					if(columnDefaultConstraint!=null &&  columnDefaultConstraint!="" && columnDefaultConstraint.contains("_")){
						dropConstraintQuery.append(columnDefaultConstraint+";");
						isDropConstraintQuery = true;
					}
					dropQuery.append(tempFieldPropertyObj.getName()+";");
					strbuf.append(" `"+fieldPropertyObj.getName()+"` "+fieldPropertyObj.getType());
					setDefaultValueAndLength(fieldPropertyObj,strbuf);
					isStrbuf = true;
					isDropQuery = true;
				}else{
					if(!tempFieldPropertyObj.getName().equalsIgnoreCase(fieldPropertyObj.getName())){
						renameQuery.append("sp_RENAME '"+tableOperation.getTableName()+"."+tempFieldPropertyObj.getName()+"','"+fieldPropertyObj.getName()+"','COLUMN';");
						isRenameQuery = true;
					}
					
					if(!tempFieldPropertyObj.getDefaultValue().equalsIgnoreCase(fieldPropertyObj.getDefaultValue()) || !tempFieldPropertyObj.getLength().equalsIgnoreCase(fieldPropertyObj.getLength())){
						StringBuffer lenghtAndDefaultString = new StringBuffer();
						setDefaultValueAndLength(fieldPropertyObj,lenghtAndDefaultString);
						String lenghtString = lenghtAndDefaultString.toString();
						String[] lenghtStringArray = lenghtString.split("DEFAULT");
						
						if(!tempFieldPropertyObj.getLength().equalsIgnoreCase(fieldPropertyObj.getLength())){
							modifyQuery.append(tableQuery+" ALTER COLUMN "+fieldPropertyObj.getName()+" "+fieldPropertyObj.getType());
							modifyQuery.append(" "+lenghtStringArray[0]+";");
						}
						
						if(!tempFieldPropertyObj.getDefaultValue().equalsIgnoreCase(fieldPropertyObj.getDefaultValue()) && lenghtStringArray.length!=1){
							String columnDefaultConstraint = tableDao.getColumnDefaultConstraint(tableOperation.getTableName() , tempFieldPropertyObj.getName());
							if(columnDefaultConstraint!=null &&  columnDefaultConstraint!="" && columnDefaultConstraint.contains("_")){
								modifyQuery.append(tableQuery+" DROP CONSTRAINT "+columnDefaultConstraint+";");
							}
							
							modifyQuery.append(tableQuery+"ADD DEFAULT "+lenghtStringArray[1].replace(",", "")+" for "+fieldPropertyObj.getName()+";");
						}
						
						// stop sending modifyQuery get the default value and remove it and split the default value and make a new 
						
						isModifyQuery = true;
					}
				}
				
				if((tempFieldPropertyObj.getIsUniquekey() || tempFieldPropertyObj.getIsCompositeKey()) && !tempFieldPropertyObj.getName().equals("ID")){
					dropUniqueKey.append(";"+tableQuery+" DROP CONSTRAINT UK_"+tableOperation.getTableName()+"_"+fieldPropertyObj.getName()+";");
				}
				
				if(!isPreview){
					tempFieldPropertyObj.setName(fieldPropertyObj.getName());
					tempFieldPropertyObj.setChineseName(fieldPropertyObj.getChineseName());
					tempFieldPropertyObj.setType(fieldPropertyObj.getType());
					tempFieldPropertyObj.setLength(fieldPropertyObj.getLength());
					tempFieldPropertyObj.setDefaultValue(fieldPropertyObj.getDefaultValue());
					tempFieldPropertyObj.setAutoGenerationId(fieldPropertyObj.getAutoGenerationId());
					tempFieldPropertyObj.setIsUniquekey(fieldPropertyObj.getIsUniquekey());
					tempFieldPropertyObj.setIsCompositeKey(fieldPropertyObj.getIsCompositeKey());
					fieldPro.add(tempFieldPropertyObj);
				}
			}else{
				strbuf.append("`");
				strbuf.append(fieldPropertyObj.getName()+"` "+fieldPropertyObj.getType());
				setDefaultValueAndLength(fieldPropertyObj,strbuf);
				isStrbuf = true;
				if(!isPreview){
					fieldPro.add(fieldPropertyObj);
				}
			}
			//ALTER TABLE parent_a ADD CONSTRAINT UK_parent_a_parent_key_a UNIQUE (parent_key_a)		
			if((fieldPropertyObj.getIsUniquekey() || fieldPropertyObj.getIsCompositeKey()) && !fieldPropertyObj.getName().equals("ID")){
				if(fieldPropertyObj.getIsUniquekey()){
					uniqueKeyQuery.append(";"+tableQuery+" ADD CONSTRAINT UK_"+tableOperation.getTableName()+"_"+fieldPropertyObj.getName()+" UNIQUE ("+fieldPropertyObj.getName()+");");
				}else{
					uniqueKeyQuery.append(";"+tableQuery+" ADD CONSTRAINT UK_"+tableOperation.getTableName()+"_"+fieldPropertyObj.getName()+" UNIQUE ("+fieldPropertyObj.getName()+",TASK_ID,PROC_INST_ID);");
				}
			}
		}
		
		
		/*	if(dropQuery.lastIndexOf(",")>0){
				dropQuery.deleteCharAt(dropQuery.lastIndexOf(","));
			}
			dropQuery.append(";");*/
		
		Set<MetaTableRelation> metaTableRelation=tableOperation.getMetaTableRelation();
		if(!metaTableRelation.isEmpty()){
			
			MetaTable metatable=tableDao.getTableDetails(tableOperation.getId());
			
			for(MetaTableRelation tableRelation:metaTableRelation){
				if(tableRelation.getParentTable()!=null  && tableOperation.getId()!=tableRelation.getParentTable().getId()){
					hasParent=true;
					String parentTableName=tableRelation.getParentTable().getTableName();
					String foreignKeyColumnName = tableRelation.getForeignKeyColumnName();
					String childKeyColumnName=tableRelation.getChildKeyColumnName();
					//strbuf.append(" ADD COLUMN `"+parentTableName+"_"+foreignKeyColumnName+"` VARCHAR (100) DEFAULT NULL,");
					//foreignKeyBuffer.append(",ADD CONSTRAINT fk_"+parentTableName+"_"+foreignKeyColumnName+"_"+tableOperation.getTableName()+" FOREIGN KEY ("+parentTableName+"_"+foreignKeyColumnName+") REFERENCES ETEC_RU_"+parentTableName+"("+foreignKeyColumnName+")");
					foreignKeyBuffer.append(";"+tableQuery+" ADD CONSTRAINT fk_"+parentTableName+"_"+foreignKeyColumnName+"_"+tableOperation.getTableName()+" FOREIGN KEY ("+childKeyColumnName+") REFERENCES `"+parentTableName+"` ("+foreignKeyColumnName+");");
				}else if(tableRelation.getChildTable()!=null && tableOperation.getId()!=tableRelation.getChildTable().getId() ){
					hasChild=true;
					String childTableName=tableRelation.getChildTable().getTableName();
					String foreignKeyColumnName = tableRelation.getForeignKeyColumnName();
					String parentTableName=tableOperation.getTableName();
					String childKeyColumnName=tableRelation.getChildKeyColumnName();
					//childTableQuery.append(";ALTER TABLE ETEC_RU_"+childTableName+" ADD `"+parentTableName+"_"+foreignKeyColumnName+"` VARCHAR (100) DEFAULT NULL ,ADD CONSTRAINT fk_"+parentTableName+"_"+foreignKeyColumnName+"_"+childTableName+" FOREIGN KEY ("+parentTableName+"_"+foreignKeyColumnName+") REFERENCES ETEC_RU_"+parentTableName+" ("+foreignKeyColumnName+")");
					childTableQuery.append(";ALTER TABLE `"+childTableName+"` ADD CONSTRAINT fk_"+parentTableName+"_"+foreignKeyColumnName+"_"+childTableName+" FOREIGN KEY ("+childKeyColumnName+") REFERENCES `"+parentTableName+"` ("+foreignKeyColumnName+");");
				}
			}
			
			if(!isPreview){
				if(hasParent){
					dropPreviousData(metatable,"parent");
				}
				
				if(hasChild){
					dropPreviousData(metatable,"child");
				}
			}
			
		}
		
		if(strbuf.lastIndexOf(",")>0){
			strbuf.deleteCharAt(strbuf.lastIndexOf(","));
		}
		
		if(!deleteExistingTableColumns.isEmpty()){
			for(String collumnId:deleteExistingTableColumns){
				deleteExistingColumnQuery.append("ALTER TABLE `"+tableOperation.getTableName()+"` ");
				MetaTableColumns tempFieldPropertyObj=tableDao.getMetaTableColumnsById(collumnId);
				
				String columnDefaultConstraint = tableDao.getColumnDefaultConstraint(tableOperation.getTableName() , tempFieldPropertyObj.getName());
				if(columnDefaultConstraint!=null &&  columnDefaultConstraint!="" && columnDefaultConstraint.contains("_")){
					dropConstraintQuery.append(";"+tableQuery+" DROP CONSTRAINT ");
					dropConstraintQuery.append(columnDefaultConstraint+";");
					isDropConstraintQuery = true;
				}
				
				deleteExistingColumnQuery.append(" DROP COLUMN `"+tempFieldPropertyObj.getName()+"`;");
				
				if((tempFieldPropertyObj.getIsUniquekey() || tempFieldPropertyObj.getIsCompositeKey()) && !tempFieldPropertyObj.getName().equals("ID")){
					dropUniqueKey.append(";"+tableQuery+" DROP CONSTRAINT UK_"+tableOperation.getTableName()+"_"+tempFieldPropertyObj.getName());
				}
			}
			
			/*if(deleteExistingColumnQuery.lastIndexOf(",")>0){
				deleteExistingColumnQuery.deleteCharAt(deleteExistingColumnQuery.lastIndexOf(","));
			}
			deleteExistingColumnQuery.append(";");*/
			isDeleteExistingColumnQuery = true;
		}
		
		/*if(dropConstraintQuery.lastIndexOf(",")>0){
			dropConstraintQuery.deleteCharAt(dropConstraintQuery.lastIndexOf(","));
		}
		dropConstraintQuery.append(";");*/
		
		//Drop the existing unique key
		if(dropUniqueKey.length()>0){
			/*if(!isStrbuf){
				strbuf = new StringBuffer();
			}
			strbuf.append(dropUniqueKey);
			isStrbuf = true;*/
			finalQuery.append(dropUniqueKey);
		}
		
		//Alter and add new unique key
		if(uniqueKeyQuery.length()>0){
			if(!isStrbuf){
				strbuf = new StringBuffer();
			}
			
			strbuf.append(uniqueKeyQuery);
			isStrbuf = true;
		}
		
		if(tableQuery.length()==strbuf.length()){
			/*log.info("===============================================================================================");
			log.info("Constructed Query: No Changes has been done in the Table Fields");
			log.info("===============================================================================================");*/
			return "No Changes has been done for Query Preview";
		}
		
		if(!isPreview){
			saveFieldPropertys(fieldPro);
			saveTableRelation(metaTableRelation);
			if(!deleteExistingTableColumns.isEmpty()){
				deleteExistingTableColumnsByIds(deleteExistingTableColumns);
			}
			/* Added by Nithi - This functionality has been added to clear the process definitions 
			 * from the cache is any changes has been made in the table
			 * This will help to reload the table changes while starting corresponding process*/
			repositoryService.clearProcessDefinitionsFromCache();
			/* Added by Nithi*/
		}
		
		if(isDropConstraintQuery){
			finalQuery.append(dropConstraintQuery);
		}
		
		if(isDeleteExistingColumnQuery){
			finalQuery.append(deleteExistingColumnQuery);
		}
		
		if(isDropQuery){
			finalQuery.append(dropQuery);
		}
		
		if(isRenameQuery){
			finalQuery.append(renameQuery);
		}
		
		if(isModifyQuery){
			finalQuery.append(modifyQuery);
		}
		
		if(isStrbuf){
			finalQuery.append(strbuf);
		}
		
		if(hasParent){
			finalQuery.append(foreignKeyBuffer);
		}
		
		if(hasChild){
			finalQuery.append(childTableQuery);
		}
		
		return finalQuery.toString().replaceAll("`", "");
	}
	
	
	
	private void setDefaultValueAndLength(MetaTableColumns fieldPropertyObj, StringBuffer strbuf)throws EazyBpmException{
		if(fieldPropertyObj.getType().equals("DECIMAL") || fieldPropertyObj.getType().equals("NUMERIC") || fieldPropertyObj.getType().equals("DOUBLE") ){
			String fieldLengthArray[]=fieldPropertyObj.getLength().split(",");
			int fieldLength=0;
			int decimalLength=0;
			
			fieldLength=Integer.parseInt(fieldLengthArray[0]);
			if(fieldLengthArray.length>=2){
				decimalLength=Integer.parseInt(fieldLengthArray[1]);
			}
			
			strbuf.append(" ("+fieldLength+","+decimalLength+")");
			if(fieldPropertyObj.getDefaultValue().equalsIgnoreCase("NULL")){
				strbuf.append(" DEFAULT "+fieldPropertyObj.getDefaultValue()+" ,");
			}else{
				strbuf.append(" DEFAULT '"+fieldPropertyObj.getDefaultValue()+"' ,");
			}
		}else if (fieldPropertyObj.getType().equals("TIMESTAMP")){
			if(dataBaseSchema.equals("mysql")){
				strbuf.append(" NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,");
			}else if(dataBaseSchema.equals("oracle")){
				strbuf.append(" "+fieldPropertyObj.getDefaultValue()+" ,");
			}
		}else if ((fieldPropertyObj.getType().equals("NVARCHAR") || fieldPropertyObj.getType().equals("SMALLINT") || fieldPropertyObj.getType().equals("TINYINT") || fieldPropertyObj.getType().equals("INT")) && dataBaseSchema.equals("mssql")){
			if(fieldPropertyObj.getType().equals("NVARCHAR") ){
				if(fieldPropertyObj.getLength()!="0"){
					strbuf.append(" ("+fieldPropertyObj.getLength()+") ");
				}else{
					strbuf.append(" ("+25+") ");
				}
			}
				strbuf.append(" DEFAULT ('"+fieldPropertyObj.getDefaultValue()+"') ,");
		}else if(Integer.parseInt(fieldPropertyObj.getLength())==0){
			if(fieldPropertyObj.getType().equals("BOOLEAN") && !fieldPropertyObj.getDefaultValue().equalsIgnoreCase("NULL")){
				if(fieldPropertyObj.getDefaultValue().equalsIgnoreCase("true") || fieldPropertyObj.getDefaultValue().equalsIgnoreCase("1")){
					fieldPropertyObj.setDefaultValue("1");
				}else{
					fieldPropertyObj.setDefaultValue("0");
				}
				strbuf.append(" DEFAULT '"+fieldPropertyObj.getDefaultValue()+"' ,");
			}else if(fieldPropertyObj.getType().equalsIgnoreCase("TIME")){
				strbuf.append(" DEFAULT '"+fieldPropertyObj.getDefaultValue()+"' ,");
			}else{
				if(fieldPropertyObj.getDefaultValue() != null && !fieldPropertyObj.getDefaultValue().equalsIgnoreCase("NULL") && !fieldPropertyObj.getDefaultValue().isEmpty() && fieldPropertyObj.getDefaultValue().length() > 0){
					strbuf.append(" DEFAULT '"+fieldPropertyObj.getDefaultValue()+"' ,");
				} else {
					strbuf.append(",");
				}
			}
		}else if(fieldPropertyObj.getDefaultValue().equalsIgnoreCase("NOTNULL")){
			strbuf.append(" ("+fieldPropertyObj.getLength()+") NOT NULL, ");
		}else{
			strbuf.append(" ("+fieldPropertyObj.getLength()+") ");
			if(fieldPropertyObj.getDefaultValue().equalsIgnoreCase("NULL")){
				strbuf.append(" DEFAULT "+fieldPropertyObj.getDefaultValue()+" ,");
			}else{
				strbuf.append(" DEFAULT '"+fieldPropertyObj.getDefaultValue()+"' ,");
			}
		}
	}
	
}

