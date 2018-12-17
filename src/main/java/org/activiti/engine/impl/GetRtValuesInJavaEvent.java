package org.activiti.engine.impl;

import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

public class GetRtValuesInJavaEvent implements Command<List<String>> {

	protected Map<String, Object> rtValuesInJavaEvent;
	protected boolean isUpdate = false;
	
	public GetRtValuesInJavaEvent(Map<String, Object> paramsMap) {
		this.rtValuesInJavaEvent = paramsMap;
	}

	public List<String> execute(CommandContext commandContext) {
		String query = "getValuesForPrimaryKey";
		return Context.getCommandContext().getDbSqlSession().selectList(query, rtValuesInJavaEvent);
	}

}
