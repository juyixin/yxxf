package com.eazytec.common.util;

import java.util.ArrayList;
import java.util.List;

import com.eazytec.bpm.common.util.I18nUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.MetaTableColumns;


/**
 * Hold functions commonly used for string specific operations.
 * @author madan
 *
 */
public class StringUtil {
	
	public static boolean isEmptyString(String string){
		return (string==null || string.trim()=="" || string.equalsIgnoreCase("'NULL'"));
	}
	
	public static Boolean parseBooleanAttribute(String booleanText) {
	    if ("true".equals(booleanText) || "enabled".equals(booleanText) || "on".equals(booleanText) || "active".equals(booleanText) || "yes".equals(booleanText) || "1".equals(booleanText)) {
	      return Boolean.TRUE;
	    }
	    if ("false".equals(booleanText) || "disabled".equals(booleanText) || "off".equals(booleanText) || "inactive".equals(booleanText)
	            || "no".equals(booleanText) || "0".equals(booleanText) || "null".equals(booleanText) || booleanText==null) {
	      return Boolean.FALSE;
	    }
	    return null;
	}
	
	public static Boolean checkAndParseBooleanAttribute(String booleanText) {
	    Boolean attr = parseBooleanAttribute(booleanText);
	    if(attr==null){
	    	throw new BpmException("Invalid boolean value, "+booleanText+" Must be one of {on|yes|true|enabled|active|off|no|false|disabled|inactive|1|0}");
	    }
	    return attr;
	  }
	
	public static String checkAndParseBooleanAttribute(String booleanText, MetaTableColumns column) {
	    Boolean attr = parseBooleanAttribute(booleanText);
	    if(attr==null){
	    	throw new BpmException("Invalid boolean value, "+booleanText+" Must be one of {on|yes|true|enabled|active|off|no|false|disabled|inactive|1|0}");
	    }else{
	    	if(column.getType().contains("tinyint")||column.getType().contains("TINYINT")){
	    		if(attr.equals(Boolean.TRUE)){
	    			return String.valueOf(1);
	    		}else{
	    			return String.valueOf(0);
	    		}
	    	}else{
	    		return String.valueOf(attr);
	    	}
	    }
	    
	  }
	
	public static Boolean checkAndParseBooleanAttribute(Object booleanText) {
	    Boolean attr = parseBooleanAttribute((String)booleanText);
	    if(attr==null){
	    	throw new BpmException("Invalid boolean value, "+booleanText+" Must be one of {on|yes|true|enabled|active|off|no|false|disabled|inactive|1|0}");
	    }
	    return attr;
	  }
	
	
	public static String getDBStringValue(String string){
		if(!StringUtil.isEmptyString(string)){
			string = string.replaceAll("'", "\\\\'");
			return "'"+string+"'";
		}else {
			return "NULL";
		}
	}
	
	public static String getDBDateValue(String string){
		if(StringUtil.isEmptyString(string)){
			return "NULL";
		}else{
    	    if(I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("oracle")) {
    	    	return CommonUtil.convertDateTypeForDB("oracle", string);
    	    } else {
    			string = string.replaceAll("'", "\\\\'");
    			return "'"+string+"'";
    	    }

		}
	}
	
	public static String getDBStringValue(Object string){
		if(string instanceof String){
			return "'"+(String)string+"'";
		}else{
			return null;
		}
		
	}
	
	public static List<String> convertStringArrayToList(String[]string){
		List<String>listValue = new ArrayList<String>();
		for (String stringValue : string) {
			listValue.add(stringValue);
		}
		return listValue;
	}
	
	public static String[] getCommaSeparatedStrings(String string){
		if(string==null){
			throw new EazyBpmException("Cannot split separate an empty string");
		}else{
			return string.split(",");
		}
	}

	public static boolean removeQuotesForNull(String fieldValue) {
		String nullString = "'NULL'";
		if(nullString.equalsIgnoreCase(fieldValue)) {
			return true;
		}
		return false;
	}
	
}
