package com.eazytec.bpm.admin.datadictionary.util;

import java.util.List;
import java.util.StringTokenizer;

import com.eazytec.Constants;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.DataDictionary;

/**
 * Contains all Utility methods related to Data Dicationary.
 * @author Karthick
 *
 */
public class DataDictionaryUtil {
	
	/**
	 * To check whether the dictionary code is already exists or not.
	 * @param dictionaryList
	 * 		list of Data dictionaries.
	 * @param dictCode
	 * 		Dictionary code given by the user.
	 * 		
	 * @return
	 * 		boolean whether code already exists or not.
	 * @throws EazyBpmException
	 */
	public static boolean isDictCodeExists(List<DataDictionary> dictionaryList ,String dictCode) throws EazyBpmException {
		
		for(DataDictionary dictionary:dictionaryList) {
			if(dictionary.getCode().equalsIgnoreCase(dictCode)) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param dictionaryList
	 * 
	 * @return
	 * 
	 * @throws EazyBpmException
	 */
	public static boolean isFormEntryValid(List<DataDictionary> dictionaryList) throws EazyBpmException {
		
		
		return false;
		
	}
	
	/**
	 * formats Sql String and returns a formatted String to be executed as a sql query.
	 * @param sql
	 *      Sql String given by the user.
	 * @return
	 * 		formatted sql string
	 * @throws EazyBpmException
	 */
	public static String formatSqlString(String sqlString) throws EazyBpmException {
		
		StringTokenizer tokenizer = new StringTokenizer(sqlString.trim());
		String[] validQryTokens = {"select","fieldName1,","fieldName2,","from","tableName"};
		
		while(tokenizer.hasMoreTokens()) {
			tokenizer.nextToken();
			
		}
		return Constants.EMPTY_STRING;
	}
}
