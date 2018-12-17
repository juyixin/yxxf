package com.eazytec.bpm.metadata.process.dao;


import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.eazytec.dao.GenericDao;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Classification;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Process;



public interface ProcessDao  extends GenericDao<ProcessDao, String>{
	
	
	List<Classification> getClassifications();
	
	Classification getClassificationById(String id);
	
	@Transactional
    Classification saveClassification(Classification classification) throws BpmException;
	
	List<Classification> getClassificationsByIds(List<String> cids);

	void deleteClassification(Classification c);
	
	boolean updateClassificationOrderNos(String id, int orderNo);
	
	 /**
     * To validate whether the give Classification is exist or new
     * @param classification
     * @return
     * @throws BpmException
     */
    boolean isClassificationExist(String classification) throws BpmException;
    
    List<Object[]> getNewsItemForHomePage() throws EazyBpmException;
    
    /*
     * Get All Process List
     */
    List<Process> getAllProcess(String whereParams) throws EazyBpmException;
    
    /**
     * Get all Process for given id list
     * @param ids
     * @return
     * @throws EazyBpmException
     */
    List<Process> getProcessByIds(List<String> ids)throws EazyBpmException;
    /**
     * Get All Process list as LabelValue bean
     * @return
     * @throws BpmException
     */
    List<LabelValue> getAllProcessAsLabelValue() throws BpmException;
    
    /**
     * To search process 
     * @param processName
     * @return
     */
    List<LabelValue> searchProcessNames(String processName)throws BpmException;

    /**
     * Get Classification Id by deployment Id
     * @param deploymentId
     * @return
     */
	String getClassificationId(String deploymentId);
	/**
	 * Get all active process List from DB
	 * @return
	 */
	List<Process> getAllProcess()throws EazyBpmException;
	
	/**
	 * get all process based on system defined attributes
	 * @param isSystemDefined
	 * @return
	 * @throws Exception
	 */
	List<LabelValue> getAllProcessBySystemDefined(boolean isSystemDefined) throws Exception;
}
