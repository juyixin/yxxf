package com.eazytec.bpm.admin.datadictionary.dao;

import java.util.List;

import com.eazytec.dao.GenericDao;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.DataDictionary;
import com.eazytec.model.LabelValue;

/**
 * 
 * @author Karthick
 *
 */
public interface DataDictionaryDao extends GenericDao<DataDictionary, String> {
	
	/**
	 * Helps to create new data dictionary.
	 * Dictionary might have an interior dictionary related to it so that
	 * Through the Parent dictionary the interior dictionary are retrieved.
	 *  
	 * @param dictionary
	 * 		 dictionary params to save.
	 * @throws EazyBpmException
	 */
	public DataDictionary addNewDictionary(DataDictionary dictionary) throws EazyBpmException;
	
	public DataDictionary importDictionary(DataDictionary dictionary) throws EazyBpmException;
	
	/**
	 * Deletes an existing dictionary.
	 * 
	 * @throws EazyBpmException
	 */
	public void deleteDictionary() throws EazyBpmException;
	
	public List<DataDictionary> getAllDataDictionaries() throws EazyBpmException;
	
	
	/**
	 * Gets DataDictionary by its Id.
	 * @param id
	 *      id of the Dictionary to be passed.
	 * @return
	 * 		Datadictionary related to the given id.
	 * @throws EazyBpmException
	 */
	public DataDictionary getDataDictionaryById(String id) throws EazyBpmException;
	
	
	/**
	 * 
	 * @param parentId
	 * @return
	 * @throws EazyBpmException
	 */
	public List<LabelValue> getDictionaryLabelValueByParentId(String parentId) throws EazyBpmException;
	
	
	/**
	 * 
	 * @param parentId
	 * @return
	 * @throws EazyBpmException
	 */
	public List<DataDictionary> getDataDictionaryByParentId(String parentId) throws EazyBpmException;

	
	public List<DataDictionary> getDataDictionaryByDictionaryIds(String dictionaryIds) throws EazyBpmException;
	
	/**
	 * 
	 * @return
	 * @throws EazyBpmException
	 */
	public List<LabelValue> getAllDictionaryAsLabelValue()
			throws EazyBpmException;
	
	/**
	 * 
	 * @param parentId
	 * @return
	 * @throws EazyBpmException
	 */
	public List<LabelValue> getDictionaryValueByParentId(String parentId)
			throws EazyBpmException ;
	/**
	 * Checks for a dictionary name whether or not it is already existed.
	 * @param dictName
	 *        dict name to be checked
	 * @return
	 * 		boolean status about dictName existed or not
	 * @throws EazyBpmException
	 */
	public boolean checkDictionaryNameExists(String dictName) throws EazyBpmException;
	
	/**
	 * Checks for a dictionary value whether or not it is already existed.
	 * @param dictValue
	 *        dict value to be checked
	 * @return
	 * 		boolean status about dictValue existed or not
	 * @throws EazyBpmException
	 */
	public boolean checkDictionaryValueExists(String Value) throws EazyBpmException;
	
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
	 * Deletes DataDictionary based on the ids
	 * @param idList
	 * 		list of ids of the data dictionary to be deleted
	 * @return
	 *     success status of the operation
	 * @throws EazyBpmException
	 */
	public boolean deleteDataDictionary(String idList) throws EazyBpmException;
	
	
	/**
	 * Disables DataDictionary based on the ids
	 * @param idList
	 * 		list of ids of the data dictionary to be deleted
	 * @return
	 *     success status of the operation
	 * @throws EazyBpmException
	 */
	public boolean disableDataDictionary(String idList) throws EazyBpmException;
	
	/**
	 * Updates datadictionary order no 
	 * @param dictionaryId
	 * @param orderNo
	 * @return
	 * @throws EazyBpmException
	 */
	public boolean updateDictionaryOrderNos(String dictionaryId,int orderNo) throws EazyBpmException;
	
	
	/**
	 * Checks whether the given query is a valid query or not
	 * @param query
	 * 		Query to be checked
	 * @return
	 * 		Status of whether or not it is true.
	 * @throws EazyBpmException
	 */
	public List<?> getDynamicDictionaryValues(String query) throws EazyBpmException;

	public DataDictionary getDataDictionaryByCode(String code);
	
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
