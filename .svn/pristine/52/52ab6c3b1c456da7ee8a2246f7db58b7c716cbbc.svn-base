package org.activiti.engine.impl;

import java.util.Map;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

/**
 * This class handles create,update and delete operations with respect
 * to map values given in form
 * 
 */
public class OperateRtValuesInJavaEvent implements Command<Void> {

	protected Map<String, Object> rtValuesInJavaEvent;
	protected boolean isUpdate = false;
	protected boolean isDelete = false;
	
	
	public OperateRtValuesInJavaEvent(Map<String, Object> paramsMap,boolean isUpdate, boolean isDelete) {
		this.rtValuesInJavaEvent = paramsMap;
		this.isUpdate = isUpdate;
		this.isDelete = isDelete;
	}

	public Void execute(CommandContext commandContext) {
		String query = "";
		if(!isUpdate && !isDelete) {
			//insert record
			query = "insertValuesFromMap";		
			Context.getCommandContext().getDbSqlSession().selectList(query, rtValuesInJavaEvent);
		} else if(isUpdate && !isDelete){
			//update record
			Context.getCommandContext().getTableEntityManager().updateRtValues(rtValuesInJavaEvent);
		} else {
			//delete record
		    String primaryKey = rtValuesInJavaEvent.get("primaryKey").toString();
		    String primaryKeyVal = rtValuesInJavaEvent.get("primaryKeyVal").toString();
		    String tableName = rtValuesInJavaEvent.get("tableName").toString();
			Context.getCommandContext().getTableEntityManager().deleteRtValues(tableName,primaryKey,primaryKeyVal);
		}
		return null;
	}

}
