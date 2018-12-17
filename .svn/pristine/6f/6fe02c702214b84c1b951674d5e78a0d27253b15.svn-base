package com.eazytec.bpm.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;

import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;

public class NewsUtil {
	
	public static String generateScriptForNewsList(List<Map<String,Object>> newsList, VelocityEngine velocityEngine) throws BpmException{
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['id', 'processInstanceId','Title','Created By', 'Created At']";		
		context.put("title", "News List");
		context.put("gridId", "News_LIST");
		//String jsonFieldValues = "[{'image':'/images/easybpm/common/mail_box.jpg','news':'Some text goes hereSome text goes hereSome text goes hereSome text goes here','id':'123'}]";
		/*if(formList != null && !(formList.isEmpty())){
			jsonFieldValues = CommonUtil.getJsonString(formList);	
		}*/
		try {
			if(newsList != null && !(newsList.isEmpty())){					
				String jsonFieldValues = CommonUtil.getJsonString(newsList);
				context.put("jsonFieldValues", jsonFieldValues);
			} 
		} catch (Exception e) {
			throw new EazyBpmException("Problem parsing task instances as json", e);
		}
		
		context.put("columnNames", columnNames);	
		CommonUtil.createFieldNameList(fieldNameList, "ID", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "processInstanceId", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "title", "150", "left", "_showNewsDetail", "false");
		CommonUtil.createFieldNameList(fieldNameList, "CREATEDBY", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "CREATEDTIME", "100", "left", "", "false");
		context.put("fieldNameList", fieldNameList);		
		return GridUtil.generateScript(velocityEngine, context);
	}

}
