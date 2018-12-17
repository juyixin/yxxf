package com.eazytec.bpm.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;

import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.exceptions.BpmException;

public class SmsUtil {
	/**
	 * Set into context of grid column names and field names and it attributes
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForSms(List<Map<String,Object>> smsList, VelocityEngine velocityEngine) throws BpmException{
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','Sender','Recipient','Created On','Sent On']";		
		context.put("title", "SMS List");
		context.put("gridId", "SMS_LIST");
		String jsonFieldValues = "[{'id':'123','sender':'','recipient':'','createdOn':'','sentOn':''}]";
		if(smsList != null && !(smsList.isEmpty())){
			jsonFieldValues = CommonUtil.getJsonString(smsList);	
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "sender", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "recipient", "150", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdOn", "80", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "sentOn", "80", "left", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}
}
