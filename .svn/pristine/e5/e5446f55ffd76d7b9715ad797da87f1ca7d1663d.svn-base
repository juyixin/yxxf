package com.eazytec.bpm.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;

import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.exceptions.BpmException;

public class TodoUtil {

	public static String generateScriptForTodoList(List<Map<String,Object>> todoList, VelocityEngine velocityEngine) throws BpmException{
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','Title','Description','Created At','Duedate','Created By','Status']";		
		context.put("title", "To-do List");
		context.put("gridId", "todo_lIST");
		
		String jsonFieldValues = "[{'id':'123','title':'','description':'','createdat':'','duedate':'','createdby':'','status':''}]";
		
		/*if(formList != null && !(formList.isEmpty())){
			jsonFieldValues = CommonUtil.getJsonString(formList);	
		}*/
		
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);	
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "title", "150", "left", "","false");
		CommonUtil.createFieldNameList(fieldNameList, "description", "150", "left", "","false");
		CommonUtil.createFieldNameList(fieldNameList, "createdat", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "duedate", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdby", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "status", "100", "left", "", "false");
		
		context.put("fieldNameList", fieldNameList);		
		return GridUtil.generateScript(velocityEngine, context);
	}
}
