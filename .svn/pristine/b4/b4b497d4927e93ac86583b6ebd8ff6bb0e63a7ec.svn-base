package com.eazytec.bpm.runtime.customOperating.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.task.CustomOperatingFunction;

import com.eazytec.exceptions.EazyBpmException;

import com.eazytec.model.LabelValue;

public interface CustomOperatingService {
	
	/**
	 * Get all Custom Operational functions as label value pair 
	 * @return
	 * @throws EazyBpmException
	 */
	List<LabelValue> getAllCustomOperations()throws EazyBpmException;
	
	/**
	 * Get save or update Custom Operating Function
	 * @return
	 * @throws EazyBpmException
	 */
	CustomOperatingFunction saveCustomOperatingFunction(CustomOperatingFunction customFunction,boolean isCustomOperatingUpdate)throws EazyBpmException;
	
	/**
	 * Get save or update Custom Operating Function
	 * @return
	 * @throws EazyBpmException
	 */
	CustomOperatingFunction getCustomOperatingById(String customOperatingId)throws EazyBpmException;
	
	/**
	 * To delete Custom Operating Function
	 * @return
	 * @throws EazyBpmException
	 */
	void deleteCustomFunctionById(CustomOperatingFunction CustomOperatingObj)throws EazyBpmException;
	
	/**
	 * To check the function name duplicate  
	 * @return
	 */
	boolean checkCustomFunctionName(String functionName)throws EazyBpmException;
	
}
