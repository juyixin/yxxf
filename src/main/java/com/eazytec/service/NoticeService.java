package com.eazytec.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.velocity.app.VelocityEngine;

import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceException;
import com.eazytec.model.MetaForm;

public interface NoticeService {
	
	/**
	 * return all notice list items 
	 * @return
	 */
	List<Map<String, Object>> getNoticeItems();
	
	/**
	 * return the current active notice definition
	 * @return
	 * @throws BpmException
	 */
	ProcessDefinition getNoticeDefinition()throws BpmException;
	
	/**
	 * Design the start process script map valuse
	 * @param noticeDefinition
	 * @param velocityEngine
	 * @return
	 */
	Map<String, Object> designStartProcessScript(ProcessDefinition noticeDefinition, VelocityEngine velocityEngine,Map<String,Object> permissionVal);
	
	/**
	 * start the notice form from process
	 * @param processDefinitionId
	 * @param formValues
	 * @param subFormValues
	 * @param files
	 * @param filePaths
	 * @return
	 * @throws DataSourceException
	 */
	ProcessInstance submitStartForm(String processDefinitionId, Map<String, String>formValues, Map<String, String[]>subFormValues, Map<String, byte[]>files, Map<String, String>filePaths)throws DataSourceException;
	/**
	 * get the map values of given instance id
	 * @param noticeInstanceId
	 * @return
	 */
	Map<String, Object> getNoticeItemValues(String noticeInstanceId);
	
	/**
	 * Get form for given instance id
	 * @param noticeInstanceId
	 * @return
	 */
	MetaForm getNoticeForm(String noticeInstanceId);
	
	/**
	 * update the notice from process
	 * @param newsValues
	 * @param subFormValues
	 * @param files
	 * @param filePaths
	 * @throws DataSourceException
	 */
	void updateNotice(Map<String, String>newsValues, Map<String, String[]>subFormValues, Map<String, byte[]>files, Map<String, String>filePaths)throws DataSourceException;
	
	/**
	 * get the map values for edit notice
	 * @param newsDefinition
	 * @param velocityEngine
	 * @param insId
	 * @return
	 * @throws BpmException
	 */
	 Map<String, Object> designEditNoticeScript(ProcessDefinition newsDefinition, VelocityEngine velocityEngine,String insId,Map<String,Object> permissionVal,String formAction)throws BpmException;

}
