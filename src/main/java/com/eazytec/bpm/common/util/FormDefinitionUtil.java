package com.eazytec.bpm.common.util;

import java.util.ArrayList;
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

/**
 * 
 * @author Karthick
 *
 */

public class FormDefinitionUtil {
	
	
	/**
	 * Set into context of grid column names and field names and it attributes
	 * @param isJspForm TODO
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForTask(List<Map<String,Object>> formList, VelocityEngine velocityEngine, Locale locale, boolean isJspForm) throws BpmException{
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		//String columnNames = "['Id','Form Name','Description','Created At','Created By','Version No','Form Version','Delete','moduleId']";		
		String isHide = "false";
		String formColumnName = "Form Name";
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY ,locale);
		context.put("title", appResourceBundle.getString("form.list"));
		if(isJspForm){
			isHide = "true";
			formColumnName = "Jsp Form Name";
			context.put("title", appResourceBundle.getString("jsp.form.list"));
		}
		String columnNames =  "['Id','"+formColumnName+"','Created At','Created By','Version No','Form Version','Delete','moduleId','Is Delete','isEdit','isSystemModule','isTemplate','View']"; 
		
		context.put("gridId", "FORMS_LIST");
		context.put("needTreeStructure", true);
		context.put("needCheckbox",true);
		context.put("dynamicGridWidth", "organizationGridWidth");
		context.put("dynamicGridHeight", "organizationGridHeight");
		String jsonFieldValues = "";
		
		
		if(formList != null && !(formList.isEmpty())){
			jsonFieldValues = CommonUtil.getJsonString(formList);	
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);	
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		if(isJspForm){
			CommonUtil.createFieldNameList(fieldNameList, "formName", "100", "left", "_showJspPage", "false");
		}else{
			CommonUtil.createFieldNameList(fieldNameList, "formName", "100", "left", "_showFormDesigner", "false");
		}
		//CommonUtil.createFieldNameList(fieldNameList, "description", "150", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdOn", "80", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdByFullName", "80", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "version", "50", "center", "", isHide, "int");
		CommonUtil.createFieldNameList(fieldNameList, "showversion", "80", "center", "_showVersions", isHide);
		if(isJspForm){
			CommonUtil.createFieldNameList(fieldNameList, "delete", "80", "center", "_deleteJspForm", "false"); 
		}else{
			CommonUtil.createFieldNameList(fieldNameList, "delete", "80", "center", "_deleteFormDesigns", "false"); 
		}
		CommonUtil.createFieldNameList(fieldNameList, "module", "80", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "isDelete", "80", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "isEdit", "80", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "isSystemModule", "80", "center", "", "true");
		
		CommonUtil.createFieldNameList(fieldNameList, "templateForm", "80", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "view", "80", "center", "_showFormTypeIcon", "true");
		context.put("noOfRecords", "16");
		context.put("fieldNameList", fieldNameList);		
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	
	/**
	 * Set into context of grid column names and field names and it attributes
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateFormVersionsScript(List<Map<String,Object>> formList,VelocityEngine velocityEngine,Locale locale) throws BpmException{
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		//String columnNames = "['Id','Form Name','Description','Created At','Created By','Version','Restore Version', 'Delete','moduleId']";	
		String columnNames = "['Id','Form Name','Created At','Created By','Version','Restore Version', 'Delete','moduleId','Is Delete','isEdit','isSystemModule','templateForm']";	
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY ,locale);
		context.put("title", appResourceBundle.getString("form.version"));
		context.put("gridId", "FORMS_LIST");
		context.put("needTreeStructure", true);
		context.put("needCheckbox",true);
		context.put("dynamicGridWidth", "organizationGridWidth");
		context.put("dynamicGridHeight", "organizationGridHeight");
		String jsonFieldValues = "";
		if(formList != null && !(formList.isEmpty())){
			jsonFieldValues = CommonUtil.getJsonString(formList);	
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);	
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "formName", "100", "left", "_showFormDesigner", "false");
		//CommonUtil.createFieldNameList(fieldNameList, "description", "150", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdOn", "80", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdByFullName", "80", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "version", "50", "center", "", "false", "int");
		CommonUtil.createFieldNameList(fieldNameList, "active", "60", "center", "_restoreForm", "false");
		CommonUtil.createFieldNameList(fieldNameList, "delete", "50", "center", "_deleteForm", "false");
		CommonUtil.createFieldNameList(fieldNameList, "module", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "isDelete", "80", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "isEdit", "80", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "isSystemModule", "80", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "templateForm", "80", "center", "", "true");
		context.put("noOfRecords", "16");
		context.put("fieldNameList", fieldNameList);		
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	/**
	 * 获取子表单参数map
	 * @param requestMap
	 * @return
	 */
	public static Map<String, String[]> getSubFormParams (Map<String,Object> requestMap){
		Map<String, String[]> resultMap = new HashMap<String, String[]>();
		Iterator<?> param = requestMap.keySet().iterator();
		while(param.hasNext()){
			String key = (String)param.next();
			if(key.startsWith("subForm_")){
				String[] value = ((String[])requestMap.get(key));
				resultMap.put(key, value);	
			}								
		}
		return resultMap;
	}
	
	/**
	 * 获取表单参数map，不包括子表单
	 * @param requestMap
	 * @return
	 */
	public static Map<String, String[]> getFormParamsInStringArray (Map<String,Object> requestMap){
		Map<String, String[]> resultMap = new HashMap<String, String[]>();
		Iterator<?> param = requestMap.keySet().iterator();
		while(param.hasNext()){
			String key = (String)param.next();
			if(!key.startsWith("subForm_")){
				String[] value = ((String[])requestMap.get(key));//强转为数组格式
				resultMap.put(key, value);	
			}								
		}
		return resultMap;
	}
	
	public static Map<String, String> getFormParams (Map<String,Object> requestMap){
		Map<String, String> resultMap = new HashMap<String, String>();
		Iterator<?> param = requestMap.keySet().iterator();
		while(param.hasNext()){
			String key = (String)param.next();
			if(!key.startsWith("subForm_")){
				if(requestMap.get(key) instanceof String[]){
					String[] value = ((String[])requestMap.get(key));
					resultMap.put(key, value[0]);
				}							
			}					
		}
		return resultMap;
	}
	
	public static Map<String, Object> getFormParamsAsObjectMap (Map<String,Object> requestMap){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Iterator<?> param = requestMap.keySet().iterator();
		while(param.hasNext()){
			String key = (String)param.next();
			if(!key.startsWith("subForm_")){
				if(requestMap.get(key) instanceof String[]){
					String[] value = ((String[])requestMap.get(key));
					resultMap.put(key, value[0]);
				}							
			}					
		}
		return resultMap;
	}
	
	public static Map<String, Object> getFormObjParams (Map<String,Object> requestMap){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Iterator<?> param = requestMap.keySet().iterator();
		while(param.hasNext()){
			String key = (String)param.next();
			if(!key.startsWith("subForm_")){
				//String[] value = ((String[])requestMap.get(key));	
				resultMap.put(key, requestMap.get(key));			
			}					
		}
		return resultMap;
	}
	
	public static String getStringValue (Map<String,Object> requestMap, String key){		
				String[] value = ((String[])requestMap.get(key));
				return value[0];						
			

	}
	
	public static Map<String, byte[]> getFormFileParams (Map<String,Object> requestMap){
		Map<String, byte[]> resultMap = new HashMap<String, byte[]>();
		Iterator<?> param = requestMap.keySet().iterator();
		while(param.hasNext()){
			String key = (String)param.next();
			if(!key.startsWith("subForm_")){
				if(requestMap.get(key) instanceof byte[]){
					byte[] value = ((byte[])requestMap.get(key));		
					resultMap.put(key, value);
				}else{
				}
							
			}					
		}
		return resultMap;
	}
	
	/**
	 * 遍历formValues，如果其中的值（数组）长度大于1，转换成用逗号分割的字符串
	 * @param formValues
	 * @return
	 */
	public static Map<String, String> handleFromValues(Map<String, String[]> formValues) {
		Map<String, String> rtFormValues = new HashMap<String, String>();
		for(Map.Entry<String, String[]> entry : formValues.entrySet()){
			String[] rtFormValue = entry.getValue();
			String rtValue = "";
			for(int idx = 0; idx < rtFormValue.length; idx++){
				rtValue = rtValue + rtFormValue[idx];
				int rtLen = rtFormValue.length - 1;
				if(rtLen > idx){
					rtValue = rtValue + ",";
				}
			}
			if(!rtValue.isEmpty()){
				rtFormValues.put(entry.getKey(), rtValue);
			}else{
				rtFormValues.put(entry.getKey(), "");
			}
		}
		return rtFormValues;
	}	 

}
