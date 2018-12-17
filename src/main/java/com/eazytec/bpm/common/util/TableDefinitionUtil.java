package com.eazytec.bpm.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.velocity.app.VelocityEngine;

import com.eazytec.Constants;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.exceptions.BpmException;

public class TableDefinitionUtil {
	
	/**
	 * Set into context of grid column names and field names and it attributes
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForTable(List<Map<String,Object>> tableList, VelocityEngine velocityEngine, Locale locale) throws BpmException{
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','Table Name','Chinese Name','Sql Query','Description','Created At','Created By','Delete','sqlQueryHidden']";		
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY ,locale);
		context.put("title", appResourceBundle.getString("table.list"));
		context.put("gridId", "TABLE_LIST");
		String jsonFieldValues = "";
		if(tableList != null && !(tableList.isEmpty())){
			jsonFieldValues = CommonUtil.getJsonString(tableList);	
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);	
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "tableName", "100", "left", "_showEditTable", "false");
		CommonUtil.createFieldNameList(fieldNameList, "chineseName", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "preview", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "description", "250", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdOn", "70", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdBy", "80", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "delete", "40", "center", "_deleteTableDesign", "false");
		CommonUtil.createFieldNameList(fieldNameList, "tableQuery", "100", "left", "", "true");
		context.put("noOfRecords", "16");
		context.put("fieldNameList", fieldNameList);		
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	/**
	 * Gets all the datatype related to the form field type
	 * @param fieldType
	 * 		field type of the forms
	 * @return
	 * 		list of data types mapping with the field types
	 * @throws BpmException
	 */
	public static String getDatatypeFromFieldtype(String fieldType) throws BpmException {
		
		if (fieldType.toLowerCase().equals("textarea")) {
			return Constants.TEXTAREA_DATATYPE;
		} else if (fieldType.toLowerCase().equals("text")
				|| fieldType.toLowerCase().equals("hidden")
				|| fieldType.toLowerCase().equals("userlist")
				|| fieldType.toLowerCase().equals("department")
				|| fieldType.toLowerCase().equals("role")
				|| fieldType.toLowerCase().equals("group")) {
			return Constants.TEXT_DATATYPE;
		} else if (fieldType.toLowerCase().equals("radio")
				|| fieldType.toLowerCase().equals("checkbox")) {
			return Constants.RADIO_CHECKBOX_DATATYPE;
		} else if (fieldType.toLowerCase().equals("number")) {
			return Constants.NUMBER_DATATYPE;
		} else if (fieldType.toLowerCase().equals("datepicker")) {
			return Constants.DATE_DATATYPE;
		} else if (fieldType.toLowerCase().equals("datetimepicker")) {
			return Constants.DATE_TIME_DATATYPE;
		} else if (fieldType.toLowerCase().equals("decimal")) {
			return Constants.DECIMAL_DATATYPE;
		} else if (fieldType.toLowerCase().equals("file")) {
			return Constants.FILE_DATATYPE;
		} else if (fieldType.toLowerCase().equals("richtextbox")) {
			return Constants.RICHTEXTBOX_DATATYPE;
		} else if (fieldType.toLowerCase().equals("email")
				|| fieldType.toLowerCase().equals("password")) {
			return Constants.PASSWORD_EMAIL_DATATYPE;
		} else if(fieldType.toLowerCase().equals("select")){
			return Constants.SELECT_DATATYPE;
		}
		
		return Constants.ALL_DATATYPE;
	}

}
