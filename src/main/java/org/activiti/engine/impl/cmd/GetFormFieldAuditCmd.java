package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;

import com.eazytec.bpm.common.util.I18nUtil;

public class GetFormFieldAuditCmd implements Command<List<Map<String,Object>>>, Serializable{
	 
	private static final long serialVersionUID = 1L;
	protected String processInstanceId;
	protected String formId;
	 public GetFormFieldAuditCmd(String processInstanceId,String formId) {
		    this.processInstanceId = processInstanceId;
		    this.formId = formId;
	  }
	 public List<Map<String,Object>> execute(CommandContext commandContext) {
		  if(processInstanceId !=null && formId !=null){
			  Map<String,Object> parameter = new HashMap<String, Object>();
			  parameter.put("processInstanceId", processInstanceId);
			  parameter.put("formId", formId);
			  return commandContext.getTableEntityManager().getFormFieldTraceData(parameter);
		  }else{
			  throw new ActivitiException(I18nUtil.getMessageProperty("error.processInstance.notFound"));
		  }
	  }
}
