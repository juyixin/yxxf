package com.eazytec.bpm.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.velocity.app.VelocityEngine;

import com.eazytec.Constants;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceValidationException;
import com.eazytec.model.MetaTableColumns;

/**
 * 
 * @author madan
 *
 */
 
public class DataSourceUtil {
	
	public static boolean isColumnTextType(String columnType){
		if(columnType.equalsIgnoreCase("varchar") || columnType.equalsIgnoreCase("varchar2") || columnType.equalsIgnoreCase("date") || columnType.equalsIgnoreCase("time")
				|| columnType.equalsIgnoreCase("char") || columnType.equalsIgnoreCase("tinyblob") || columnType.equalsIgnoreCase("NVARCHAR") || columnType.equalsIgnoreCase("NCHAR") ) {
			return true;
		}
		return false;
	  }
	
	public static boolean isColumnDateType(String columnType){
		  if(columnType.equalsIgnoreCase("date")|| columnType.equalsIgnoreCase("TIMESTAMP") || columnType.equalsIgnoreCase("DATETIME") ) {
			  return true;
		  }
		  return false;
	  }
	
	public static boolean isColumnVarcharType(String columnType){
		  return columnType.contains("varchar")||columnType.contains("VARCHAR");
	  }
	  
	public static boolean isColumnBooleanType(String columnType){
		  return columnType.equalsIgnoreCase("tinyint")||columnType.equalsIgnoreCase("boolean");
	  }
	  
	public static boolean isColumnByteType(String columnType){
		  return (columnType.equalsIgnoreCase("longtext")||columnType.equalsIgnoreCase("longblob")||columnType.equalsIgnoreCase("blob")||columnType.equalsIgnoreCase("text")
				  ||columnType.equalsIgnoreCase("mediumblob")||columnType.equalsIgnoreCase("mediumText")||columnType.equalsIgnoreCase("tinytext")||columnType.equalsIgnoreCase("clob")||columnType.equalsIgnoreCase("ntext"));
	  }
	
	public static boolean isColumnIntType(String columnType){
		  return (columnType.equalsIgnoreCase("int")||columnType.equalsIgnoreCase("year"));
	  }
	
	public static boolean isColumnFloatType(String columnType){
		  return columnType.equalsIgnoreCase("float")||columnType.equalsIgnoreCase("decimal");
	  }
	
	public static boolean isColumnDoubleType(String columnType){
		  return columnType.equalsIgnoreCase("double");
	  }
	
	
	/**
	 * Checks if field value matches the constraint of table column and throws exception if not
	 * @param fieldValue
	 * @param column
	 * @return
	 * @throws DataSourceValidationException
	 */
	public static boolean isFieldValidForColumn(String fieldName, String fieldValue, MetaTableColumns column)throws DataSourceValidationException{
		try{
			  checkForNull(fieldValue, column);
			  // Already null check is performed and will throw error if column is notnull type. So after
			  // that if field value is still null, then no need to do other field type validations since
			  // null value is accepted. Remaining will be performed under node level permissions.
			  if(StringUtil.isEmptyString(fieldValue)){
				  return true;
			  }
			  if (isColumnVarcharType(column.getType())){
				  return isValueValidVarchar(fieldValue, column);
			  }else if(isColumnBooleanType(column.getType())){
				  return isValueValidBoolean(fieldValue, column);
			  }else if(isColumnIntType(column.getType())){
				  return isValueValidInteger(fieldValue, column);
			  }else if(isColumnFloatType(column.getType())){
				  return isValueValidFloat(fieldValue, column);
			  }else if(isColumnDoubleType(column.getType())){
				  return isValueValidDouble(fieldValue, column);
			  }else if(isColumnDateType(column.getType())){
				  return isValueValidDate(fieldValue, column);
			  }else if(isColumnIntType(column.getType())){
				  return isValueYear(fieldValue, column);
			  }else{
				  return true;
			  }
		}catch(DataSourceValidationException e){
			throw new DataSourceValidationException("Error in field "+fieldName+"-"+e.getMessage(), e);
		}
		
	  }
	
	private static boolean isValueYear(String fieldValue,
			MetaTableColumns column) {
		try{			
			if(column.getLength() == "4" || column.getLength() == "2")  {
				if(column.getLength() == "4" ) {
					if( !(Integer.parseInt(fieldValue) > 1900) && !(Integer.parseInt(fieldValue) < 2155)){
						throw new DataSourceValidationException(fieldValue+" year range should be between 1900 - 2155");
					} 
				} else {
					if( !(Integer.parseInt(fieldValue) > 0) && !(Integer.parseInt(fieldValue) < 99)){
						throw new DataSourceValidationException(fieldValue+" year range should be between 0 - 99");
					} 
				}
			} else {
				throw new DataSourceValidationException(fieldValue+" should be a valid year");
			}
		}catch(NumberFormatException e){
			throw new DataSourceValidationException(fieldValue+" should be a valid integer");
		}
		return true;
	}

	private static boolean checkForNull(String fieldValue, MetaTableColumns column){
		if(!StringUtil.isEmptyString(column.getDefaultValue())&&column.getDefaultValue().equals("NOTNULL")){
			if(StringUtil.isEmptyString(fieldValue)){
				throw new DataSourceValidationException(fieldValue+" cannot be null since the table doesnot accept");
			}else{
				return true;
			}
			
		}else{
			return true;
		}
	}
	
	private static boolean isValueValidVarchar(String fieldValue, MetaTableColumns column){
		if(!StringUtil.isEmptyString(fieldValue) && fieldValue.length()>Integer.parseInt(column.getLength())){
			throw new DataSourceValidationException(fieldValue+" should not be greater than "+column.getLength()+" characters");
		}else{
			return true;
		}
	}
	
	private static boolean isValueValidBoolean(String fieldValue, MetaTableColumns column){
		if(StringUtil.isEmptyString(com.eazytec.common.util.StringUtil.parseBooleanAttribute(fieldValue))){
			throw new DataSourceValidationException(fieldValue+" should be a valid boolean");
		}else{
			return true;
		}
	}
	
	private static boolean isValueValidInteger(String fieldValue, MetaTableColumns column){		
		try{			
			Integer.parseInt(fieldValue);
			return true;
		}catch(NumberFormatException e){
			throw new DataSourceValidationException(fieldValue+" should be a valid integer");
		}
		
	}
	
	private static boolean isValueValidFloat(String fieldValue, MetaTableColumns column){
		try{			
			Float.parseFloat(fieldValue);
			return true;
		}catch(NumberFormatException e){
			throw new DataSourceValidationException(fieldValue+" should be a valid decimal");
		}
		
	}
	
	private static boolean isValueValidDouble(String fieldValue, MetaTableColumns column){
		try{			
			Double.parseDouble(fieldValue);
			return true;
		}catch(NumberFormatException e){
			throw new DataSourceValidationException(fieldValue+" should be a valid double");
		}
		
	}
	private static boolean isValueValidDate(String fieldValue, MetaTableColumns column){
		if(!StringUtil.isEmptyString(fieldValue)){
			try{	
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sdf.setLenient(false);
				// if not valid, it will throw ParseException
				Date date = sdf.parse(fieldValue);
				return true;
			}catch(ParseException e){
				throw new DataSourceValidationException(fieldValue+" should be a valid Date");
			}
		}else{
			return true;
		}
		
	}

	public static boolean isColoumnNumberType(String columnType) {
		  return (columnType.equalsIgnoreCase("number"));
	}

}
