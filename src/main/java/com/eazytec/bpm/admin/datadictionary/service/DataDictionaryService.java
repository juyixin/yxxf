package com.eazytec.bpm.admin.datadictionary.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.ResponseBody;

import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.DataDictionary;
import com.eazytec.model.LabelValue;

/**
 * 
 * @author Karthick
 *
 */
public interface DataDictionaryService {
	
	/**
	 * Helps to create new data dictionary.
	 * Dictionary might have interior dictionary related to it so that
	 * Through the Parent dictionary the interior dictionary are retrieved.
	 *  
	 * @param dictionary
	 * 		 dictionary params to save.
	 * @throws EazyBpmException
	 */
	public DataDictionary addNewDataDictionary(DataDictionary dictionary) throws EazyBpmException;
	
	public DataDictionary importDataDictionary(DataDictionary dictionary) throws EazyBpmException;
	
	/**
	 * Deletes an existing dictionary.
	 * 
	 * @throws EazyBpmException
	 */
	public boolean deleteDataDictionary(String idList) throws EazyBpmException;
	
	/**
	 * Disables data dictionary by changing the status to disabled.
	 * @return
	 * @throws EazyBpmException
	 */
	public boolean disableDataDictionary(String idList) throws EazyBpmException;
	
	/**
	 * Returns all Data Dictionaries.
	 * @return
	 * 		List of Data dictionaries.
	 * @throws EazyBpmException
	 */
	public List<DataDictionary> getAllDataDictionaries() throws EazyBpmException;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws EazyBpmException
	 */
	public DataDictionary getDataDictionaryById(String id) throws EazyBpmException;
	
	/**
	 * 
	 * @return
	 * @throws EazyBpmException
	 */
	public List<LabelValue> getDictionaryCodeByParentId(String parentId) throws EazyBpmException;
	
	/**
	 * 
	 * @param parentId
	 * @return
	 * @throws EazyBpmException
	 */
	public List<DataDictionary> getDataDictionaryByParentId(String parentId) throws EazyBpmException;
	
	public List<DataDictionary> getDataDictionaryByDictionaryIds(String dictionaryIds) throws EazyBpmException;

	/**
	 * Get all data dictionary as label value.
	 * @return list of data dictioanaries as label value.
	 * @throws EazyBpmException
	 */
	public List<LabelValue> getAllDictionaryAsLabelValue() throws EazyBpmException;
	
	/**
	 * Checks for a dictionary Name whether or not it is already existed.
	 * @param dictName
	 *        dict Name to be checked
	 * @return
	 * 		boolean status about dictName existed or not
	 * @throws EazyBpmException
	 */
	public boolean checkDictionaryNameExists(String dictName) throws EazyBpmException;
	
	/**
	 * Checks for a dictionary Value whether or not it is already existed.
	 * @param dictValue
	 *        dict Value to be checked
	 * @return
	 * 		boolean status about dictValue existed or not
	 * @throws EazyBpmException
	 */
	public boolean checkDictionaryValueExists(String dictValue) throws EazyBpmException;
	
	/**
	 * Checks for a dictionary code whether or not it is already existed.
	 * @param dictCode
	 *        dict code to be checked
	 * @return
	 * 		boolean status about dictCode existed or not
	 * @throws EazyBpmException
	 */
	public boolean checkDictionaryCodeExists(String dictCode) throws EazyBpmException;
	
	/**
	 * Generates order no for a dictionary based on the order no of the parent id related to its child dictionaries
	 * @param parentDictId
	 * 		 Parent dict id which the data dictionary belongs to 
	 * @return
	 * 		 Order no of the dictionary id
	 * @throws EazyBpmException
	 */
	public int generateOrderNo(String parentDictId) throws EazyBpmException;
	
	/**
	 * 
	 * @return
	 * @throws EazyBpmException
	 */
	public boolean updateDictionaryOrderNos(List<Map<String,Object>> dictionaryList) throws EazyBpmException;
	
	/**
	 * 
	 * @param query
	 * @return
	 * @throws EazyBpmException
	 * @throws Exception 
	 */
	public boolean checkQueryValidity(String query) throws EazyBpmException, Exception;
	
	/**
	 * 
	 * @param query
	 * @return
	 * @throws EazyBpmException
	 */
	public List<LabelValue> getSqlDictionaryData(String query) throws EazyBpmException;
	
	/**
	 * Get All Child Dictionary Values By Parent Id
	 * @param parentId
	 * @return
	 * @throws EazyBpmException
	 */
	List<LabelValue> getChildDictionaryValuesByParentId(String parentId) throws EazyBpmException;

	/**
     * 
     * Gets all data dictionaries as label value
     * @return
     * 		
     * @throws Exception
     */
    List<Map<String,Object>> getDictionaryValueByParentId(String parentId);

	public List<DataDictionary> getDataDictionaryByParentCode(String string);
	
	/**
	 * Description:违规条例字典集
	 * 作者 : 蒋晨   
	 * 时间 : 2017-11-11 下午1:40:10
	 */
	public List<DataDictionary> getWgtlList(String wgtl);
	
	/**
	 * Description:违规条例字典集
	 * 作者 : 蒋晨   
	 * 时间 : 2017-11-11 下午1:40:10
	 */
	public List<DataDictionary> getWgtlAll();
}
