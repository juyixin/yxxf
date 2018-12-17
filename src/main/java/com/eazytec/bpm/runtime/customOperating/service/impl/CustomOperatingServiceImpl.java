package com.eazytec.bpm.runtime.customOperating.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.task.CustomOperatingFunction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.runtime.customOperating.service.CustomOperatingService;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LabelValue;

@Service("CustomOperatingService")
public class CustomOperatingServiceImpl implements CustomOperatingService{
	
	public VelocityEngine velocityEngine;
	private RepositoryService repositoryService;
    
	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
	
	@Autowired
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<LabelValue> getAllCustomOperations() throws EazyBpmException{
		List<CustomOperatingFunction> listOfCustomOperating= repositoryService.getAllCustomOperatingFunction();
		List<LabelValue> customOperatingList=new ArrayList<LabelValue>();
		for(CustomOperatingFunction customOperatingObj:listOfCustomOperating){
			LabelValue customOperatingLableValue=new LabelValue();
			customOperatingLableValue.setLabel(customOperatingObj.getName());
			customOperatingLableValue.setValue(customOperatingObj.getId());
			customOperatingList.add(customOperatingLableValue);
		}
		return customOperatingList;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CustomOperatingFunction saveCustomOperatingFunction(CustomOperatingFunction customFunction,boolean isCustomOperatingUpdate)throws EazyBpmException{
		if(!isCustomOperatingUpdate){
			customFunction.setId(UUID.randomUUID().toString());
		}
		
		CustomOperatingFunction CustomOperatingObj= repositoryService.saveCustomOperatingFunction(customFunction,isCustomOperatingUpdate);
		
		return CustomOperatingObj;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public CustomOperatingFunction getCustomOperatingById(String customOperatingId)throws EazyBpmException{
		CustomOperatingFunction CustomOperatingObj= repositoryService.getCustomOperatingById(customOperatingId);
		return CustomOperatingObj;
	}
	/**
	 * {@inheritDoc}
	 */
	public void deleteCustomFunctionById(CustomOperatingFunction CustomOperatingObj)throws EazyBpmException{
		repositoryService.deleteCustomFunctionById(CustomOperatingObj);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean checkCustomFunctionName(String functionName)throws EazyBpmException{
		boolean isCustomFunctionNameDuplicate=false;
		List<CustomOperatingFunction> listOfCustomOperating= repositoryService.getAllCustomOperatingFunction();
		for(CustomOperatingFunction custionObj:listOfCustomOperating){
			if(custionObj.getName().equals(functionName)){
				isCustomFunctionNameDuplicate=true;
				break;
			}
		}
		return isCustomFunctionNameDuplicate;
	}

}
