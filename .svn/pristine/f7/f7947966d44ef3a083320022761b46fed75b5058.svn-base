package com.eazytec.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.common.util.ProcessUtil;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.bpm.metadata.form.service.FormDefinitionService;
import com.eazytec.bpm.metadata.process.service.ProcessDefinitionService;
import com.eazytec.bpm.runtime.form.service.FormService;
import com.eazytec.bpm.runtime.process.service.ProcessService;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.MetaForm;
import com.eazytec.service.NewsService;
import com.eazytec.util.DateUtil;

/**
 * <p>Process service at runtime, implements {@link ProcessService}</p>
 * @author madan
 * @author Revathi
 *
 */
@Service("newsService")
public class NewsServiceImpl implements NewsService{
	
	private ProcessService rtProcessService;
	private FormDefinitionService formDefinitionService;
	private HistoryService historyService;
	private ProcessDefinitionService processDefinitionService;
	private FormService rtFormService;
	private final static String PROCESS_NAME="News";
	
	public List<Map<String, Object>> getNewsItems(){
		Map<String, List<HistoricVariableInstance>> newsVariables = rtProcessService.getAllVariableValuesByProcess(PROCESS_NAME);
		return ProcessUtil.resolveHistoricVariablesAsMapList(newsVariables);
	}
	
	public List<Map<String, Object>> getNewsItemForHomePage() throws Exception{
		List<Object[]> newsItems = rtProcessService.getNewsItemForHomePage();
		List<Map<String, Object>> newsList = new ArrayList<Map<String, Object>>();
		Map<String, Object> newsMap = null;
		if(newsItems != null && !newsItems.isEmpty()){
			for(Object[] newsItem : newsItems){
				newsMap = new HashMap<String, Object>();
				newsMap.put("id", newsItem[0]);
				newsMap.put("Title", newsItem[1]);
				Date dateTime = DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss", String.valueOf(newsItem[2]));
	  			String dateStr = DateUtil.convertDateToString(dateTime);
				newsMap.put("CREATEDTIME", dateStr);
				newsList.add(newsMap);
			}
		}
		return newsList;
	}
	
	public ProcessDefinition getNewsDefinition()throws BpmException{
		ProcessDefinition newsDef = processDefinitionService.getActiveProcessesDefinitionByName(PROCESS_NAME);
		if (newsDef!=null){
			return newsDef;
		}else{
			throw new BpmException("No news definitions found active!");
		}
		
	}
	
	public Map<String, Object> getNewsItemValues(String newsInstanceId){
		try{
			Map<String, Object> result = rtProcessService.getHistoricVariablesForInstance(newsInstanceId);
			result.put("instanceId", newsInstanceId);
			return result;
		}catch(BpmException e){
			throw new BpmException("Problem getting values for news instance: "+newsInstanceId, e);			
		}
		
	}
	
	public MetaForm getNewsForm(String newsInstanceId){
		HistoricProcessInstance newsInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(newsInstanceId).singleResult();
		if(newsInstance!=null){
			String newsDefId = newsInstance.getProcessDefinitionId();
			StartFormData newsForm = rtProcessService.getStartFormData(newsDefId);
			String formKey = newsForm.getFormKey();
			if(formKey!=null){
				return formDefinitionService.getDynamicFormById(formKey);
			}else{
				throw new BpmException("No Form defined for news definition: "+newsDefId);
			}			
		}else{
			throw new BpmException("No News Instance found for id "+newsInstanceId);
		}
			
	}
	
	public void updateNewsItem(Map<String, String>newsValues, Map<String, String[]>subFormValues, Map<String, byte[]>files, Map<String, String>filePaths)throws DataSourceException{
		try{
			String newsInstanceId = newsValues.get("processInstanceId");
			if(StringUtil.isEmptyString(newsInstanceId)){
				throw new EazyBpmException("No instance id found");
			}
			newsValues.remove("instanceId");
			rtFormService.updateStartForm(getNewsDefinition().getId(), newsInstanceId, newsValues, subFormValues, files, filePaths);		
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(),e);
		}		
	}
	
	public ProcessInstance submitStartForm(String processDefinitionId, Map<String, String>formValues, Map<String, String[]>subFormValues, Map<String, byte[]>files, Map<String, String>filePaths)throws DataSourceException{
		return rtFormService.submitStartForm(processDefinitionId, formValues, subFormValues, files, filePaths,false);
	}
	
	public Map<String, Object> designStartProcessScript(ProcessDefinition newsDefinition, VelocityEngine velocityEngine, Map<String, Object> permissionVal)throws BpmException{
		return rtProcessService.designStartProcessScript(newsDefinition, velocityEngine, "bpm/news/submitNewsForm",true,"null",false, permissionVal);
	}

	
	@Autowired
	public void setRtProcessService(ProcessService rtProcessService) {
		this.rtProcessService = rtProcessService;
	}

	@Autowired
	public void setFormDefinitionService(FormDefinitionService formDefinitionService) {
		this.formDefinitionService = formDefinitionService;
	}

	@Autowired
	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	@Autowired
	public void setProcessDefinitionService(
			ProcessDefinitionService processDefinitionService) {
		this.processDefinitionService = processDefinitionService;
	}

	@Autowired
	public void setRtFormService(FormService rtFormService) {
		this.rtFormService = rtFormService;
	}
	
}
