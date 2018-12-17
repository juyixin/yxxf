package com.eazytec.service.impl;

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
import com.eazytec.service.NoticeService;

@Service("noticeService")
public class NoticeServiceImpl implements NoticeService {
	
	private ProcessService rtProcessService;
	private FormDefinitionService formDefinitionService;
	private HistoryService historyService;
	private ProcessDefinitionService processDefinitionService;
	private FormService rtFormService;
	
	private final static String PROCESS_NAME="Notice";
	
	/**
	 * {@inheritDoc}
	 */
	public List<Map<String, Object>> getNoticeItems(){
		Map<String, List<HistoricVariableInstance>> newsVariables = rtProcessService.getAllVariableValuesByProcess(PROCESS_NAME);
		return ProcessUtil.resolveHistoricVariablesAsMapList(newsVariables);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ProcessDefinition getNoticeDefinition()throws BpmException{
		ProcessDefinition noticeDef = processDefinitionService.getActiveProcessesDefinitionByName(PROCESS_NAME);
		if (noticeDef!=null){
			return noticeDef;
		}else{
			throw new BpmException("No notice definitions found active!");
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Map<String, Object> designStartProcessScript(ProcessDefinition noticeDefinition, VelocityEngine velocityEngine,Map<String,Object> permissionVal)throws BpmException{
		return rtProcessService.designStartProcessScript(noticeDefinition, velocityEngine, "bpm/notice/submitNoticeForm",true,"null",false, permissionVal);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Map<String, Object> designEditNoticeScript(ProcessDefinition newsDefinition, VelocityEngine velocityEngine,String insId,Map<String,Object> permissionVal,String formAction)throws BpmException{
		return rtProcessService.designStartProcessScript(newsDefinition, velocityEngine, formAction,true,insId,true, permissionVal);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ProcessInstance submitStartForm(String processDefinitionId, Map<String, String>formValues, Map<String, String[]>subFormValues, Map<String, byte[]>files, Map<String, String>filePaths)throws DataSourceException{
		return rtFormService.submitStartForm(processDefinitionId, formValues, subFormValues, files, filePaths,false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Map<String, Object> getNoticeItemValues(String newsInstanceId){
		try{
			Map<String, Object> result = rtProcessService.getHistoricVariablesForInstance(newsInstanceId);
			result.put("instanceId", newsInstanceId);
			return result;
		}catch(BpmException e){
			throw new BpmException("Problem getting values for news instance: "+newsInstanceId, e);			
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public MetaForm getNoticeForm(String noticeInstanceId){
		HistoricProcessInstance noticeInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(noticeInstanceId).singleResult();
		if(noticeInstance!=null){
			String noticeDefId = noticeInstance.getProcessDefinitionId();
			StartFormData noticeForm = rtProcessService.getStartFormData(noticeDefId);
			String formKey = noticeForm.getFormKey();
			if(formKey!=null){
				return formDefinitionService.getDynamicFormById(formKey);
			}else{
				throw new BpmException("No Form defined for news definition: "+noticeDefId);
			}			
		}else{
			throw new BpmException("No News Instance found for id "+noticeInstanceId);
		}
			
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateNotice(Map<String, String>newsValues, Map<String, String[]>subFormValues, Map<String, byte[]>files, Map<String, String>filePaths){
		try{
			String noticeInstanceId = newsValues.get("processInstanceId");
			if(StringUtil.isEmptyString(noticeInstanceId)){
				throw new EazyBpmException("No instance id found");
			}
			newsValues.remove("instanceId");
			rtFormService.updateStartForm(getNoticeDefinition().getId(), noticeInstanceId, newsValues, subFormValues, files, filePaths);		
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(),e);
		}		
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
