package com.eazytec.bpm.runtime.form.service.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.runtime.ProcessInstance;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.common.util.I18nUtil;
import com.eazytec.bpm.runtime.form.service.FormService;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceException;
import com.eazytec.util.DateUtil;

/**
 * <p>Form service at runtime, implements {@link FormService}</p>
 * @author madan
 *
 */
@Service("rtFormService")
public class FormServiceImpl implements FormService{
	
	private org.activiti.engine.FormService formService;

	/**
	 * {@inheritDoc submitStartForm}
	 */
	public ProcessInstance submitStartForm(String processDefinitionId, Map<String, String>formValues)throws DataSourceException{
		return formService.submitStartFormData(processDefinitionId, formValues);		
	}
	
	public ProcessInstance submitStartForm(String processDefinitionId, Map<String, String>formValues, Map<String, String[]>subFormValues, Map<String, byte[]>files, Map<String, String>filePaths,boolean isDraft)throws DataSourceException{
		try{
			return formService.submitStartFormData(processDefinitionId, formValues, subFormValues, files, filePaths,isDraft);		
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(),e);
		}		
	}
	
	public ProcessInstance updateStartForm(String processDefinitionId, String processInstanceId, Map<String, String>formValues, Map<String, String[]>subFormValues, Map<String, byte[]>files, Map<String, String>filePaths)throws DataSourceException{
		try{
			
			return formService.updateStartFormData(processDefinitionId, processInstanceId, formValues, subFormValues, files, filePaths);		
		}catch(ActivitiException e){
			throw new BpmException(e.getMessage(),e);
		}		
	}
	
	/**
	 * {@inheritDoc submitTaskForm}
	 */
	public void submitTaskForm(String taskId, Map<String, String>formValues)throws DataSourceException{
		try{
			formService.submitTaskFormData(taskId, formValues);
		}catch(ActivitiException e){
			  throw new BpmException(I18nUtil.getMessageProperty("error.formSubmitError")+" "+e.getMessage());			
		}
				
	}
	
	public Object submitTaskForm(String taskId, Map<String, String>formValues, Map<String, String[]>subFormValues, Map<String, byte[]>files, Map<String, String>filePaths, boolean isStartNodeTask, String proInsId)throws DataSourceException{
		try{
			return formService.submitTaskFormData(taskId, formValues, subFormValues, files, filePaths, isStartNodeTask, proInsId);
		}catch(ActivitiException e){
			  throw new BpmException(I18nUtil.getMessageProperty("error.formSubmitError")+" "+e.getMessage());			
		}
				
	}
	
	public void saveTaskForm(String taskId, Map<String, String>formValues, Map<String, String[]>subFormValues, Map<String, byte[]>files, Map<String, String>filePaths,String loggedInUserId, String proInsId)throws DataSourceException{
		try{
			formService.saveTaskFormData(taskId, formValues, subFormValues, files, filePaths,loggedInUserId, proInsId);
		}catch(ActivitiException e){
			  throw new BpmException(I18nUtil.getMessageProperty("error.formSubmitError")+" "+e.getMessage());			
		}
				
	}
	
	public Object updateTaskForm(String taskId, Map<String, String>formValues, Map<String, String[]>subFormValues, Map<String, byte[]>files, Map<String, String>filePaths, boolean isSignOff)throws DataSourceException{
		try{
			return formService.updateTaskFormData(taskId, formValues, subFormValues, files, filePaths, isSignOff);
		}catch(ActivitiException e){
			  throw new BpmException(I18nUtil.getMessageProperty("error.formSubmitError")+" "+e.getMessage());			
		}
				
	}
	
	public JSONArray getFormFieldTraceData(String processInstanceId,String formId) throws BpmException{
		try{
			List<Map<String,Object>>  fieldTraceDataList = formService.getFormFieldTraceData(processInstanceId, formId);
	        Map<String, Object> fieldTraceDataTreeMap = null;

			JSONArray traceJsonArray = new JSONArray();
			JSONObject traceJson = null;
			for(Map<String,Object> fieldTraceData:fieldTraceDataList){
				fieldTraceDataTreeMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
				fieldTraceDataTreeMap.putAll(fieldTraceData);
				traceJson = new JSONObject();
				String fieldName = (String) fieldTraceDataTreeMap.get("fieldName");
				System.out.println("==============time======"+fieldTraceDataTreeMap.get("modifiedTime"));
				System.out.println("==============time======"+DateUtil.convertDateToSTDString(fieldTraceDataTreeMap.get("modifiedTime").toString()));
				if(fieldName != null && !fieldName.equalsIgnoreCase("ID")) {
					traceJson.put("modifiedTime",  fieldTraceDataTreeMap.get("modifiedTime"));
					traceJson.put("modifiedBy", fieldTraceDataTreeMap.get("modifiedBy"));
					traceJson.put("fieldName", fieldTraceDataTreeMap.get("fieldName"));
					traceJson.put("chineseName", fieldTraceDataTreeMap.get("chineseName"));
					traceJson.put("oldValue", fieldTraceDataTreeMap.get("oldValue"));
					traceJson.put("newValue", fieldTraceDataTreeMap.get("newValue"));
					traceJsonArray.put(traceJson);
				}
			}
			return traceJsonArray;
		}catch(ActivitiException e){
			  throw new BpmException(I18nUtil.getMessageProperty("error.gettingAuditDetails")+" "+e.getMessage());			
		}catch(DataSourceException e){
			  throw new BpmException(I18nUtil.getMessageProperty("error.gettingAuditDetails")+" "+e.getMessage());			
		}catch (JSONException e) {
			  throw new BpmException(I18nUtil.getMessageProperty("error.gettingJsonAuditDetails")+" "+formId+" "+e.getMessage());			
		}
	}
	
	@Autowired
	public void setFormService(org.activiti.engine.FormService formService) {
		this.formService = formService;
	}
	

}
