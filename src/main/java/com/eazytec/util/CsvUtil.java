package com.eazytec.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * This class provides helper methods for CSV export,import.
 * @author revathi
 *
 */
public final class CsvUtil {
	/**
	 * Insert New CSV Sheet with given header names
	 * @param wb
	 * @param sheetName
	 * @param values
	 * @return
	 */
	public static HSSFSheet createNewSheetWithHeader(HSSFWorkbook wb,String sheetName,List<String> values){
		HSSFSheet newSheet = wb.createSheet(sheetName);
    	HSSFRow header = newSheet.createRow(1);
    	int cellCount=0;
    	for(String headerName : values){
    		header.createCell(cellCount).setCellValue(headerName);
    		cellCount++;
    	}    		
		return newSheet;
		
	}
	
	/**
	 * Insert the one row Cell values for newly created headers 
	 * @param newSheet
	 * @param values
	 * @return
	 */
	public static HSSFSheet insertSingleCellValues(HSSFSheet newSheet,List<String> values){
			int cellCount=0;
			HSSFRow cellRow = newSheet.createRow(2);
			for(String cellValue : values){    		
	    		cellRow.createCell(cellCount).setCellValue(cellValue);
	    		cellCount++;
	    	}
			return newSheet;
	}
	/**
	 * Insert the multiple row Cell values for newly created headers 
	 * @param newSheet
	 * @param values
	 * @return
	 */
	public static HSSFSheet insertMultipleCellValues(HSSFSheet newSheet,List<List<String>> values){
		int rowcount=2;		
		for(List<String> cellValue : values){ 
			HSSFRow cellRow = newSheet.createRow(rowcount);
			int cellCount=0;
			for(String val :cellValue){
    		cellRow.createCell(cellCount).setCellValue(val);
    		cellCount++;
			}
			rowcount++;
    	}
		return newSheet;
	}
	
	/**
	 * Insert the relationship sheet values
	 * @param newSheet
	 * @param values
	 * @return
	 */
	public static HSSFSheet insertRelationCellValues(HSSFSheet newSheet,List<Map<String,String>> values){
		
		int rowcount=2;
		for(Map<String,String> cellValue : values){
    		HSSFRow cellRow = newSheet.createRow(rowcount);
    		cellRow.createCell(0).setCellValue(cellValue.get("moduleId"));
    		cellRow.createCell(1).setCellValue(cellValue.get("relationId"));
    		rowcount++;
    	}
		return newSheet;
	}
	
	public static HSSFWorkbook readFile(String filename) throws FileNotFoundException, IOException  {
		return new HSSFWorkbook(new FileInputStream(filename));
	}
	
	public static List<Map<Integer,String>> getSheetInformation(HSSFSheet mySheet){
		Vector cellVectorHolder = new Vector();
		Iterator rowIter = mySheet.rowIterator();
        while(rowIter.hasNext())
        {
            HSSFRow myRow = (HSSFRow) rowIter.next();
            Iterator cellIter = myRow.cellIterator();
            Vector cellStoreVector=new Vector();
            while(cellIter.hasNext())
            {
                HSSFCell myCell = (HSSFCell) cellIter.next();
                cellStoreVector.addElement(myCell);
            }
            cellVectorHolder.addElement(cellStoreVector);
        }
        List<Map<Integer,String>> moduleInfo = new ArrayList<Map<Integer,String>>();
        for (int i=0;i<cellVectorHolder.size(); i++)
        {
        	Map<Integer,String> moduleMap = new HashMap<Integer,String>();
            Vector cellStoreVector=(Vector)cellVectorHolder.elementAt(i);
            for (int j=0; j < cellStoreVector.size();j++)
            {            	
                HSSFCell myCell = (HSSFCell)cellStoreVector.elementAt(j);
                String st = myCell.toString();
                moduleMap.put(j, st);
            }
            moduleInfo.add(moduleMap);
        }
        return moduleInfo;
	}
	
	public static Map<String,String> SheetInformationAsMap(List<Map<Integer,String>> sheetValueInfo){
		Map<String,String> information = new HashMap<String,String>();
		if(sheetValueInfo.get(0) != null && sheetValueInfo.get(1)!=null){
			for(int val : sheetValueInfo.get(0).keySet()){
				information.put(sheetValueInfo.get(0).get(val), sheetValueInfo.get(1).get(val));
			}
		}
		return information;
	}
	public static List<Map<String,String>> SheetInformationAsMapOfList(List<Map<Integer,String>> sheetValueInfo){
		List<Map<String,String>> information = new ArrayList<Map<String,String>>();
		int i =1;
		if(sheetValueInfo.size() >=2){
			for(Map<Integer,String> infoMap : sheetValueInfo){
				Map<String,String> informationMap = new HashMap<String,String>();
				if(i == sheetValueInfo.size())
					break;
				for(int val : sheetValueInfo.get(0).keySet()){
					informationMap.put(sheetValueInfo.get(0).get(val), sheetValueInfo.get(i).get(val));
					
				}
				information.add(informationMap);
				i++;
			}
			
		}
		return information;
	}
}
