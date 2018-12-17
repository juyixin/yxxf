package com.eazytec.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import bsh.Interpreter;

import com.eazytec.bpm.runtime.table.service.TableService;
import com.eazytec.bpm.runtime.task.service.OperatingFunctionService;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.MetaTable;
import com.eazytec.model.MetaTableColumns;

public class JavaEvent extends BaseFormController{
	
	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    
	private TableService tableService;
    private OperatingFunctionService operatingFunctionService;

	@Autowired
	public void setOperatingFunctionService(
			OperatingFunctionService operatingFunctionService) {
		this.operatingFunctionService = operatingFunctionService;
	}

    @Autowired
    public void setTableService(TableService tableService) {
             this.tableService = tableService;
    }
  
    	@RequestMapping(value="bpm/of/executeJavaCode", method = RequestMethod.GET)
	    public @ResponseBody Map<String, String> executeJava(@RequestParam("javaEvent") String javaEvent) {
	    	Map<String, String> responseMap = new HashMap<String, String>();    
	    	try{
	        	Interpreter interpreter = new Interpreter();  
	        	Object result = interpreter.eval(javaEvent);  //return map values
	        if (result instanceof Map) {
					Map<String, Object> paramsMap = new HashMap<String, Object>();
					boolean isTableExistsMap = false;
					String tableName = ((Map) result).get("table").toString();
					//throw exception if table does not exist
					isTableExistsMap = tableService.checkTableName(tableName);
					if (isTableExistsMap) {
						/**
						 * save,update and delete operation in run time table .atleast one unique key is required to create a record.
						 * once record is inserted , values can be updated or deleted with primary key 
						 */
						List<MetaTable> tableDetails = tableService.getTableDetailsByNames("('"+tableName.toUpperCase()+"')");
						for (MetaTable tableDetail : tableDetails) {
							Set<MetaTableColumns> fieldProperty=tableDetail.getMetaTableColumns();
							for(MetaTableColumns fieldPropertyObj:fieldProperty){
								if(!fieldPropertyObj.getName().equalsIgnoreCase("ID") && fieldPropertyObj.getIsUniquekey()) {
									Map<String, Object> paramsMapforCheck = new HashMap<String, Object>();
									operatingFunctionService.createOrUpdateJaveEventValues(paramsMap,fieldPropertyObj.getName(),paramsMapforCheck,result);
									break;
								}
							}
						}
					} else {
						throw new BpmException("Table Name does not exist in Hash Map of Java code");
					}
					responseMap.put("result", "success");
				} else {
					responseMap.put("result", "success");
				}
	        }
	        catch(BpmException e){
	            log.error("Error executing java code "+e.getMessage());
	            responseMap.put("result", e.getMessage().toString());
	        } catch(Exception e) {
	            log.error("Error executing java code "+e.getMessage());
	            responseMap.put("result", e.getMessage().toString());
	        }
			return responseMap;
	    }
    	
	    public void executeJavaCode() {
	    	System.out.println("Executed Java Code");
	    }
 
}
