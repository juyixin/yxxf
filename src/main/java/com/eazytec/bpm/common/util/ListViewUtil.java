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

public class ListViewUtil {
	/**
	 * Set into context of grid column names and field names and it attributes
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForColumnsProperty(List<Map<String,Object>> listViewColumnProperty, VelocityEngine velocityEngine, Locale locale) throws BpmException{
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','Column Title', 'Data Fields','Move Up','Move Down', 'Column Width','Other Name','Replace Words','Is Sort','Is Advanced Search','Is Simple Search','Text Align','Is Hidden','Render Event','Comment']";		
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY ,locale);
		//context.put("title", appResourceBundle.getString("title.columns.property"));
		context.put("gridId", "LIST_VIEW_COLUMNS_PROPERTY");
		context.put("dynamicGridWidth", "listViewColumnsPropertyGridWidth");
		context.put("dynamicGridHeight", "listViewColumnsPropertyGridHeight");
		String jsonFieldValues = "";
		if(listViewColumnProperty != null && !(listViewColumnProperty.isEmpty())){
			jsonFieldValues = CommonUtil.getJsonString(listViewColumnProperty);	
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);	
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "columnTitle", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "dataFields", "150", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "orderUpImg", "100","center", "_showMoveUpImageForListViewColumn", "false");
		CommonUtil.createFieldNameList(fieldNameList, "orderDownImg", "100","center", "_showMoveDownImageForListViewColumn", "false");
		CommonUtil.createFieldNameList(fieldNameList, "width", "80", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "otherName", "80", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "replaceWords", "50", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "isSort", "80", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "isAdvancedSearch", "80", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "isSimpleSearch", "80", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "textAlign", "80", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "isHidden", "80", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "onRenderEvent", "80", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "comment", "80", "left", "", "true");
		context.put("noOfRecords", "16");
		context.put("fieldNameList", fieldNameList);		
		return GridUtil.generateScript(velocityEngine, context);
	}
}
