package com.eazytec.bpm.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.activiti.engine.form.StartFormData;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.velocity.app.VelocityEngine;

import com.eazytec.Constants;
import com.eazytec.bpm.metadata.form.service.FormDefinitionService;
import com.eazytec.bpm.metadata.process.service.ProcessDefinitionService;
import com.eazytec.bpm.runtime.process.service.ProcessService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.common.util.TemplateUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.MetaForm;

/**
 * 
 * @author madan
 *
 */

public class ProcessUtil {
	
	public static List<Map<String, Object>>resolveHistoricVariablesAsMapList(Map<String, List<HistoricVariableInstance>>variables){
		List<Map<String, Object>> historicVariables = new ArrayList<Map<String,Object>>();
		Iterator<String> instanceids = variables.keySet().iterator();
		while (instanceids.hasNext()) {
		    String instanceId = instanceids.next();
		    List<HistoricVariableInstance>variableList = variables.get(instanceId);
		    Map<String, Object> variablesMap = getVariablesMap(variableList);
		    variablesMap.put("processInstanceId", instanceId);
		    historicVariables.add(variablesMap);		    
		}
		return historicVariables;
	}
	
	private static Map<String, Object>getVariablesMap(List<HistoricVariableInstance>variables){
		Map<String, Object>variablesMap=new HashMap<String, Object>();
		for (HistoricVariableInstance variable : variables) {
			variablesMap.put(variable.getVariableName(), variable.getValue());
		}
		return variablesMap;
	}
	
}
