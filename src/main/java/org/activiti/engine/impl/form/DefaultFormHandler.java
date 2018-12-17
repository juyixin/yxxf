/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.engine.impl.form;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.form.AbstractFormType;
import org.activiti.engine.form.FormFieldPermission;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.BpmnParser;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.util.xml.Element;
import org.activiti.engine.task.IdentityLink;
import org.apache.commons.lang.StringUtils;

import com.eazytec.bpm.common.util.DataSourceUtil;
import com.eazytec.bpm.common.util.I18nUtil;
import com.eazytec.bpm.common.util.TaskUtil;
import com.eazytec.bpm.engine.TaskRole;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.StringUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.DataSourceException;
import com.eazytec.exceptions.DataSourceValidationException;
import com.eazytec.model.MetaTable;
import com.eazytec.model.MetaTableColumns;
import com.eazytec.model.User;
import com.eazytec.util.DateUtil;


/**
 * @author Tom Baeyens
 */
public class DefaultFormHandler implements FormHandler {
	
 
	protected Expression formKey;
	protected String deploymentId;
	protected List<FormPropertyHandler> formPropertyHandlers = new ArrayList<FormPropertyHandler>();
	protected List<FormPropertyHandler> subFormPropertyHandlers = new ArrayList<FormPropertyHandler>();
	private TaskRole taskRoleForUser;
	private org.activiti.engine.TaskService taskService = Context.getProcessEngineConfiguration().getTaskService();
	ExpressionManager expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
	private String defaultFieldPermissions = "1-0-0-0-0"; // Editable-Required-Readonly-Hidden-No output
  
	public void parseConfiguration(Element activityElement, DeploymentEntity deployment, ProcessDefinitionEntity processDefinition, BpmnParse bpmnParse) {
		formPropertyHandlers.clear();
		this.deploymentId = deployment.getId();
		// Changing formkey to formid - Madan
		String formId = activityElement.attribute("form");
		if (formId != null && formId != "") {
			this.formKey = expressionManager.createExpression(formId);
		}
		createFormHandler(activityElement, activityElement, bpmnParse, false);
	}
  
	private void createsubFormHandler(Element activityElement, Element subFormEl, BpmnParse bpmnParse) {
		createFormHandler(activityElement, subFormEl, bpmnParse, true);
	}

	private void createFormHandler(Element activityElement, Element formEl, BpmnParse bpmnParse, boolean isSubFormElement) {
		Element extensionElement = formEl.element("extensionElements");
		if (extensionElement != null) {
			List<Element> formPropertyElements = extensionElement.elementsNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "formProperty");
			for (Element formPropertyElement : formPropertyElements) {
				String isSubForm = formPropertyElement.attribute("isSubForm", "false");
				isSubFormElement = StringUtil.parseBooleanAttribute(isSubForm);
				FormPropertyHandler formPropertyHandler = new FormPropertyHandler();
				formPropertyHandler.setSubFormElement(isSubFormElement);
				createFormPropertyHandler(formPropertyElement, formPropertyHandler, bpmnParse, isSubFormElement);
			}
			setDefaultFormProperties(activityElement, bpmnParse);
		}
	}
	
	/**
	 * 创建表单域处理器
	 * @param formPropertyElement
	 * @param formPropertyHandler
	 * @param bpmnParse
	 * @param isSubForm
	 */
	private void createFormPropertyHandler(Element formPropertyElement, FormPropertyHandler formPropertyHandler, BpmnParse bpmnParse, boolean isSubForm) {

		FormTypes formTypes = Context.getProcessEngineConfiguration().getFormTypes();

		String id = formPropertyElement.attribute("id");
		if (id == null) {
			bpmnParse.addError("attribute 'id' is required", formPropertyElement);
		}
		formPropertyHandler.setId(id);
		String name = formPropertyElement.attribute("name");
		formPropertyHandler.setName(name);

		AbstractFormType type = formTypes.parseFormPropertyType(formPropertyElement, bpmnParse);
		formPropertyHandler.setType(type);

		String subType = formPropertyElement.attribute("subtype");
		formPropertyHandler.setSubType(subType);

		String maxLength = formPropertyElement.attribute("maxlength");
		formPropertyHandler.setMaxLength(maxLength);

		String helpText = formPropertyElement.getText().trim();
		if (!helpText.isEmpty() && helpText != "") {
			formPropertyHandler.setHelpText(helpText);
		}

		String value = formPropertyElement.attribute("val_defaut");
		if (value != null && value != "") {
			formPropertyHandler.setDefValue(value);
		}

		String mask = formPropertyElement.attribute("mask");
		formPropertyHandler.setMask(mask);

		String label = formPropertyElement.attribute("label");
		formPropertyHandler.setLabel(label);

		String size = formPropertyElement.attribute("size");
		formPropertyHandler.setSize(size);

		String tableId = formPropertyElement.attribute("table");
		if (tableId == null) {
			bpmnParse.addError("No table specified for formProperty " + name, formPropertyElement);
		} else {

			MetaTable table = Context.getCommandContext().getTableEntityManager().selectTableById(tableId);
			if (table == null) {
				bpmnParse.addError("No table with id " + tableId, formPropertyElement);
			} else {
				formPropertyHandler.setTable(table);
			}
		}

		String columnId = formPropertyElement.attribute("column");
		if (columnId == null) {

			bpmnParse.addError("No column specified for formProperty " + name, formPropertyElement);
		} else {

			MetaTableColumns column = Context.getCommandContext().getTableEntityManager().selectTableColumnById(columnId);

			if (column == null) {
				bpmnParse.addError("No column with id " + columnId, formPropertyElement);
			} else {
				formPropertyHandler.setColumn(column);
			}
		}

		String creatorExpression = formPropertyElement.attribute(TaskRole.CREATOR.getRawName(), defaultFieldPermissions);
		String organizerExpression = formPropertyElement.attribute(TaskRole.ORGANIZER.getRawName(), defaultFieldPermissions);
		String coordinatorExpression = formPropertyElement.attribute(TaskRole.COORDINATOR.getRawName(), defaultFieldPermissions);
		String readerExpression = formPropertyElement.attribute(TaskRole.READER.getRawName(), defaultFieldPermissions);
		String processedUserExpression = formPropertyElement.attribute(TaskRole.PROCESSED_USER.getRawName(), defaultFieldPermissions);
		String wfAdminExpression = formPropertyElement.attribute(TaskRole.WORKFLOW_ADMINISTRATOR.getRawName(), defaultFieldPermissions);
		try {
			formPropertyHandler.setCreatorPermissions(getPermissions(bpmnParse, creatorExpression));
			formPropertyHandler.setOrganizerPermissions(getPermissions(bpmnParse, organizerExpression));
			formPropertyHandler.setCoordinatorPermissions(getPermissions(bpmnParse, coordinatorExpression));
			formPropertyHandler.setReaderPermissions(getPermissions(bpmnParse, readerExpression));
			formPropertyHandler.setProcessedUserPermissions(getPermissions(bpmnParse, processedUserExpression));
			formPropertyHandler.setWfAdminPermissions(getPermissions(bpmnParse, wfAdminExpression));
		} catch (BpmException e) {
			bpmnParse.addError(e.getMessage(), formPropertyElement);
		}

		String requiredText = formPropertyElement.attribute("required", "false");
		Boolean required = bpmnParse.parseBooleanAttribute(requiredText);
		if (required != null) {
			formPropertyHandler.setRequired(required);
		} else {
			bpmnParse.addError("attribute 'required' must be one of {on|yes|true|enabled|active|off|no|false|disabled|inactive}", formPropertyElement);
		}

		String readOnlyText = formPropertyElement.attribute("readonly", "false");
		Boolean readOnly = bpmnParse.parseBooleanAttribute(readOnlyText);
		if (readOnly != null) {
			formPropertyHandler.setReadOnly(readOnly);
		} else {
			bpmnParse.addError("attribute 'required' must be one of {on|yes|true|enabled|active|off|no|false|disabled|inactive}", formPropertyElement);
		}

		String readableText = formPropertyElement.attribute("readable", "true");
		Boolean readable = bpmnParse.parseBooleanAttribute(readableText);
		if (readable != null) {
			formPropertyHandler.setReadable(readable);
		} else {
			bpmnParse.addError("attribute 'readable' must be one of {on|yes|true|enabled|active|off|no|false|disabled|inactive}", formPropertyElement);
		}

		String writableText = formPropertyElement.attribute("writable", "true");
		Boolean writable = bpmnParse.parseBooleanAttribute(writableText);
		if (writable != null) {
			formPropertyHandler.setWritable(writable);
		} else {
			bpmnParse.addError("attribute 'writable' must be one of {on|yes|true|enabled|active|off|no|false|disabled|inactive}", formPropertyElement);
		}

		String onClick = formPropertyElement.attribute("onclick");
		formPropertyHandler.setOnClick(onClick);

		String onChange = formPropertyElement.attribute("onchange");
		formPropertyHandler.setOnChange(onChange);

		String onBlur = formPropertyElement.attribute("onblur");
		formPropertyHandler.setOnBlur(onBlur);

		String onFocus = formPropertyElement.attribute("onfocus");
		formPropertyHandler.setOnFocus(onFocus);

		String variableName = formPropertyElement.attribute("variable");
		formPropertyHandler.setVariableName(variableName);

		String expressionText = formPropertyElement.attribute("expression");
		if (expressionText != null) {
			Expression expression = expressionManager.createExpression(expressionText);
			formPropertyHandler.setVariableExpression(expression);
		}

		String defaultExpressionText = formPropertyElement.attribute("default");
		if (defaultExpressionText != null) {
			Expression defaultExpression = expressionManager.createExpression(defaultExpressionText);
			formPropertyHandler.setDefaultExpression(defaultExpression);
		}

		if (!isSubForm) {
			formPropertyHandlers.add(formPropertyHandler);
		} else {
			subFormPropertyHandlers.add(formPropertyHandler);
		}

	}

	private void setDefaultFormProperties(Element activityElement, BpmnParse bpmnParse) {
		String formId = activityElement.attribute("form");
		System.out.println("======QUERY BEGIN====ss=====");
		// MetaTable formTables = tableService.selectTableByFormId(formId);
		MetaTable formTable = Context.getCommandContext().getTableEntityManager().selectTableByFormId(formId);
		System.out.println("======QUERY BEGIN END======");

		String defaultFieldExpressions = "";
		if (I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("mssql")) {
			defaultFieldExpressions = I18nUtil.getMessageProperty("mssql.table.default.values");
		} else if (I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("oracle")) {
			defaultFieldExpressions = I18nUtil.getMessageProperty("oracle.table.default.values");
		} else {
			defaultFieldExpressions = I18nUtil.getMessageProperty("mysql.table.default.values");
		}

		String[] defaultFields = getSplittedString(defaultFieldExpressions, "-");
		for (String defaultField : defaultFields) {
			FormPropertyHandler formPropertyHandler = new FormPropertyHandler();
			String[] fieldAttributes = getSplittedString(defaultField, ",");
			formPropertyHandler.setWritable(true);
			formPropertyHandler.setRequired(false);
			formPropertyHandler.setReadOnly(false);
			formPropertyHandler.setId(fieldAttributes[0]);
			formPropertyHandler.setName(fieldAttributes[1]);
			formPropertyHandler.setMaxLength(fieldAttributes[3]);
			formPropertyHandler.setSize(fieldAttributes[3]);
			formPropertyHandler.setTable(formTable);
			MetaTableColumns column = Context.getCommandContext().getTableEntityManager().selectTableColumnByName(fieldAttributes[1]);
			formPropertyHandler.setColumn(column);
			formPropertyHandler.setCreatorPermissions(getPermissions(bpmnParse, defaultFieldPermissions));
			formPropertyHandler.setOrganizerPermissions(getPermissions(bpmnParse, defaultFieldPermissions));
			formPropertyHandler.setCoordinatorPermissions(getPermissions(bpmnParse, defaultFieldPermissions));
			formPropertyHandler.setReaderPermissions(getPermissions(bpmnParse, defaultFieldPermissions));
			formPropertyHandler.setProcessedUserPermissions(getPermissions(bpmnParse, defaultFieldPermissions));
			formPropertyHandler.setWfAdminPermissions(getPermissions(bpmnParse, defaultFieldPermissions));
			formPropertyHandler.setDefault(true);
			formPropertyHandlers.add(formPropertyHandler);
			if (subFormPropertyHandlers.size() > 0) {
				// subFormPropertyHandlers.add(formPropertyHandler);
			}

		}
	}
  
  private Map<String, Boolean>getPermissions(BpmnParse bpmnParse, String expression)throws BpmException{
	  Map<String, Boolean>permissions = new HashMap<String, Boolean>();
      String[]permissionExpressions = getSplittedString(expression, "-");
      if(permissionExpressions.length<5){
    	  throw new BpmException("Cannot find complete permission info in format "+FormFieldPermission.PERMISSION_FORMAT);
      }      
      permissions.put(FormFieldPermission.IS_EDITABLE, bpmnParse.checkAndParseBooleanAttribute(permissionExpressions[0]));
      permissions.put(FormFieldPermission.IS_REQUIRED, bpmnParse.checkAndParseBooleanAttribute(permissionExpressions[1]));
      permissions.put(FormFieldPermission.IS_READ_ONLY, bpmnParse.checkAndParseBooleanAttribute(permissionExpressions[2]));
      permissions.put(FormFieldPermission.IS_HIDDEN, bpmnParse.checkAndParseBooleanAttribute(permissionExpressions[3]));
      permissions.put(FormFieldPermission.IS_NO_OUTPUT, bpmnParse.checkAndParseBooleanAttribute(permissionExpressions[4]));
      return permissions;
  }
  
  private String[] getSplittedString(String expression, String splitter){
	  if(expression==null){
		 throw new BpmException("Cannot split a null expression");
	  }
	  return expression.split(splitter);
  }

  protected void initializeFormProperties(FormDataImpl formData, ExecutionEntity execution) {
	  
    List<FormProperty> formProperties = new ArrayList<FormProperty>();
    for (FormPropertyHandler formPropertyHandler: formPropertyHandlers) {
    	System.out.println("name======"+formPropertyHandler.getColumn());
      if (formPropertyHandler.isReadable()) {
        FormProperty formProperty = formPropertyHandler.createFormProperty(execution);
        formProperties.add(formProperty);
      }
    }
    for (FormPropertyHandler formPropertyHandler: subFormPropertyHandlers) {
        if (formPropertyHandler.isReadable()) {
          FormProperty formProperty = formPropertyHandler.createFormProperty(execution);
          formProperties.add(formProperty);
        }
      }
    formData.setFormProperties(formProperties);
  }

  
  /**
   * 把所有表单域处理器，转换成以表名为key的 Map<String, List<FormPropertyHandle>结构
   * @param formPropertiesHandler
   * @return
   */
  private Map<String, List<FormPropertyHandler>> getFormPropertiesTableWise(List<FormPropertyHandler>formPropertiesHandler){
	  Map<String, List<FormPropertyHandler>> propertiesMap = new HashMap<String, List<FormPropertyHandler>>();
	  for (FormPropertyHandler formPropertyHandler : formPropertiesHandler) {	
		  if(formPropertyHandler.getTable() != null){
			  String tableName = formPropertyHandler.getTable().getTableName();
			  if(propertiesMap.containsKey(tableName)){
				  List<FormPropertyHandler>formProperties=propertiesMap.get(tableName);
				  formProperties.add(formPropertyHandler);
				  propertiesMap.put(tableName, formProperties);
			  }else{
				  List<FormPropertyHandler>formProperties=new ArrayList<FormPropertyHandler>();
				  formProperties.add(formPropertyHandler);
				  propertiesMap.put(tableName, formProperties);
			  }		  
		  }
	  }
	  return propertiesMap;
  }  
  
  private void constructSubFormValuesAsMap( Map<String, String[]> properties, Map<String, String> subFormProperties) {
		Set<String> keyList = properties.keySet();
		for(String key : keyList){
			String[] rtFormValue = properties.get(key);
			String rtValue = "";
			for(int idx = 0; idx < rtFormValue.length; idx++){
				rtValue = rtValue + rtFormValue[idx];
				int rtLen = rtFormValue.length - 1;
				if(rtLen > idx){
					rtValue = rtValue + ",";
				}
			}
			if(!rtValue.isEmpty()){
				subFormProperties.put(key, rtValue);
			}else{
				subFormProperties.put(key, "");
			}
		}
	
}
  
  
  	
	private void submitSubFormProperties(String primaryTable, String primaryRecordId, Map<String, String[]> properties, Map<String, String> primaryProperties, String primaryTableId, Map<String, String> columnValues, Map<String, Object> primaryParamsMap, boolean isUpdate) throws DataSourceValidationException, DataSourceException {
		List<Map<String, Object>> subFormParamsMap = new ArrayList<Map<String, Object>>();
		Map<String, Object> tableDetails = new HashMap<String, Object>();
		List<Map<String, Object>> foreignKeyIdNames = new ArrayList<Map<String, Object>>();
		tableDetails.put("parentTableId", primaryTableId);
		String foreignKeyValue = null;
		String foreignKey = null;
		String subFormForeignKeyName = null;
		boolean isCheckForUpdate = false;
		List<String> tableNames = new ArrayList<String>();
		List<String> newColumns = new ArrayList<String>();

		Map<String, List<FormPropertyHandler>> fieldPropertiesMap = getFormPropertiesTableWise(subFormPropertyHandlers);
		for (String tableName : fieldPropertiesMap.keySet()) {
			tableNames.add(tableName);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			List<FormPropertyHandler> formPropertyHandlers = fieldPropertiesMap.get(tableName);
			List<String> columns = new ArrayList<String>();

			// adding subform forign columns
			Set<List<String>> fieldValuesList = new HashSet<List<String>>();
			Map<String, Object> paramsMapDetail = new HashMap<String, Object>();
			for (FormPropertyHandler formProHand : formPropertyHandlers) {
				tableDetails.put("tableId", formProHand.getTable().getId());
				// Get foreign key details from DB
				foreignKeyIdNames = Context.getCommandContext().getTableEntityManager().selectForeignKeyByTableId(tableDetails);
				for (Map<String, Object> keyMap : foreignKeyIdNames) {
					foreignKeyValue = columnValues.get(String.valueOf(keyMap.get("FOREIGN_KEY_COLUMN_NAME")).toUpperCase());
					foreignKey = String.valueOf(keyMap.get("FOREIGN_KEY_COLUMN_NAME")).toUpperCase();
					paramsMapDetail.put("primaryKey", foreignKey);
					paramsMapDetail.put("primaryKeyVal", "'" + foreignKeyValue + "'");
					subFormForeignKeyName = String.valueOf(keyMap.get("CHILD_KEY_COLUMN_NAME")).toUpperCase();
				}
			}
			columns.add("`ID`");
			columns.add("`CREATEDBY`");
			columns.add("`CREATEDTIME`");
			columns.add("`MODIFIEDTIME`");
			columns.add("`TASK_ID`");
			columns.add("`PROC_INST_ID`");
			columns.add("`EXECUTION_ID`");
			// Set the foreign key to the subform column
			columns.add("`" + subFormForeignKeyName + "`");
			// columns.add("`"+primaryTable.toUpperCase()+"_ID`");
			// generate field values for cloned rows present in sub forms
			// int subFormCountInt =
			// StringUtil.isEmptyString(primaryProperties.get("subFormCount")) ?
			// 0 :
			// Integer.valueOf((String)primaryProperties.get("subFormCount"));
			String subFormRowCount = primaryProperties.get("rowNumbers").toString();
			System.out.println(subFormRowCount);
			int index = 0;
			if (!subFormRowCount.isEmpty()) {
				String[] subFormCountArray = primaryProperties.get("rowNumbers").toString().split(",");
				for (String subFormCount : subFormCountArray) {
					// for(int subFormCountInitial = 2 ; subFormCountInitial <=
					// subFormCountInt ; subFormCountInitial++) {
					List<String> fieldValues = new ArrayList<String>();
					fieldValues.add(StringUtil.getDBStringValue(UUID.randomUUID().toString()));
					fieldValues.add(StringUtil.getDBStringValue(primaryProperties.get("CREATEDBY")));
					fieldValues.add(StringUtil.getDBDateValue(primaryProperties.get("CREATEDTIME")));
					fieldValues.add(StringUtil.getDBDateValue(primaryProperties.get("MODIFIEDTIME")));
					fieldValues.add(StringUtil.getDBStringValue(primaryProperties.get("TASK_ID")));
					fieldValues.add(StringUtil.getDBStringValue(primaryProperties.get("PROC_INST_ID")));
					fieldValues.add(StringUtil.getDBStringValue(primaryProperties.get("EXECUTION_ID")));
					// fieldValues.add(StringUtil.getDBStringValue(primaryRecordId));
					fieldValues.add("'" + foreignKeyValue + "'");
					for (FormPropertyHandler formPropertyHandler : formPropertyHandlers) {
						if (formPropertyHandler.getName().startsWith("subForm_")) {
							String fieldPropertyName = formPropertyHandler.getName().substring(0, formPropertyHandler.getName().length() - 1);
							System.out.println("==============hisd----- " + fieldPropertyName + subFormCount);
							System.out.println(fieldPropertyName + subFormCount + "===value===" + primaryProperties.get(fieldPropertyName + subFormCount));
							/* Adding all subform columns only once */
							if (index == 0) {
								columns.add("`" + formPropertyHandler.getColumn().getName() + "`");
							}

							System.out.println("===columns===" + columns);
							System.out.println("===fieldValues===" + fieldValues);
							MetaTableColumns column = formPropertyHandler.getColumn();
							DataSourceUtil.isFieldValidForColumn(fieldPropertyName + subFormCount, primaryProperties.get(fieldPropertyName), column);
							String fieldValue = primaryProperties.get(fieldPropertyName + subFormCount);
							System.out.println("=======" + fieldValue + "===" + primaryProperties.get(fieldPropertyName + subFormCount));
							fieldValues.add("'" + fieldValue + "'");
						}
					}
					index++;
					fieldValuesList.add(fieldValues);
				}
			}

			if (I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("mssql") || I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("oracle")) {
				for (String column : columns) {
					newColumns.add(column.replace("`", ""));
				}
				paramsMap.put("columns", newColumns);
			} else {
				paramsMap.put("columns", columns);
			}

			paramsMap.put("tableName", tableName);
			paramsMap.put("fieldValues", fieldValuesList);
			paramsMapDetail.put("tableName", primaryTable);
			// Check the primary table, whether that foreign key value already
			// exist or not.If not record will create else record will update
			// the primary table
			if (!isCheckForUpdate) {
				isCheckForUpdate = Context.getCommandContext().getTableEntityManager().getRtValues(paramsMapDetail);
			}
			if (isCheckForUpdate) {
				primaryParamsMap.put("tableName", primaryTable);
				primaryParamsMap.put("primaryKey", foreignKey);
				primaryParamsMap.put("primaryKeyVal", "'" + foreignKeyValue + "'");
			}
			subFormParamsMap.add(paramsMap);

		}

		//新增、更新主表单数据
		if (isCheckForUpdate || isUpdate) {
			Context.getCommandContext().getTableEntityManager().updateRtValues(primaryParamsMap);
		} else {
			for (String subTableName : tableNames) {
				Map<String, Object> tableDetailsMap = new HashMap<String, Object>();
				tableDetailsMap.put("processInstId", primaryProperties.get("PROC_INST_ID"));
				tableDetailsMap.put("tableName", subTableName);
				Context.getCommandContext().getTableEntityManager().storeProcessTableValues(tableDetailsMap);
			}

			Context.getCommandContext().getTableEntityManager().storeRtValues(primaryParamsMap);
		}
		
		//新增、更新子表单数据
		for (Map<String, Object> subParamMap : subFormParamsMap) {
			if (!isUpdate) {
				Context.getCommandContext().getTableEntityManager().storeRtSubValues(subParamMap);
			} else {

				//删除子表数据，没有物理删除，只是更新IS_DELETE字段为1
				Context.getCommandContext().getTableEntityManager().deleteRtSubFormValues(subParamMap.get("tableName").toString(),subFormForeignKeyName,primaryRecordId);
				
				//保存子表数据
				Context.getCommandContext().getTableEntityManager().storeRtSubValues(subParamMap);
				// Context.getCommandContext().getTableEntityManager().updateRtSubFormValues(subParamMap);
			}
		}

	}

	private TaskRole getTaskRoleApplicableForUser(User user, String taskId){
		List<IdentityLink> identityLinks = taskService.getIdentityLinksForTaskAndUser(taskId, user);
		return TaskUtil.getTransactorTypeOfPriority(identityLinks);
	}
  
  public void submitFormProperties(Map<String, String> properties, ExecutionEntity execution, Map<String, String[]> subProperties, Map<String, byte[]>files, Map<String, String>filePaths, boolean isUpdate,String taskDefinitionKey) throws DataSourceException{
	  User loggedInUser = CommonUtil.getLoggedInUser();
	  List<FormPropertyHandler> parentFormPropertyHandlers = null;
	  List<FormPropertyHandler> parentForms = new ArrayList<FormPropertyHandler>();

	  String userId = loggedInUser.getId();
	  Map<String, String> subFormProperties  = new HashMap<String, String>();
	  String taskId = properties.get("taskId");
	  if(taskId!=null){
		  setTaskRoleForUser(getTaskRoleApplicableForUser(loggedInUser, taskId));
	  }
	  
	String formId = getFormKey().getExpressionText();
	
	//根据表单ID获得表定义对象
	MetaTable table =  Context.getCommandContext().getTableEntityManager().selectTableByFormId(formId);
	
	String primaryTable = table.getTableName();
	
    Map<String, List<FormPropertyHandler>> fieldPropertiesMap = getFormPropertiesTableWise(formPropertyHandlers);
    
    //根据数据库表名，获得表单域处理器集合
    List<FormPropertyHandler> primaryFormPropertyHandlers = fieldPropertiesMap.get(primaryTable);

    Map<String, String> allProperties = new HashMap<String, String>();
    allProperties.putAll(properties);

    Map<String, Object>paramsMap = new HashMap<String, Object>();
    
    properties.put("CREATEDBY", userId);    
    String currentDate = DateUtil.getCurrentDateTime();
    properties.put("CREATEDTIME", currentDate);
    properties.put("MODIFIEDTIME", currentDate);
    properties.put("MODIFIEDBY", userId);
    properties.put("TASK_ID", taskId);
    properties.put("PROC_INST_ID", execution.getProcessInstanceId());
    properties.put("EXECUTION_ID", execution.getId());    
    properties.put("IS_LAST_TASK", properties.get("isLastTask"));  
    properties.put("IS_DELETE", "0");    
    
    Map<String, String> relationShipKeyValues = new HashMap<String, String>();
    
    //此处把移除primaryTable后，fieldPropertiesMap没有元素，以下for循环中的代码什么场景会执行到？
    fieldPropertiesMap.remove(primaryTable);
    
    Map<String,String> tableMap = new HashMap<String,String>();
    boolean isSameFormUpdate = false;
    // saving parent table information for the primary table
    for (String tableName : fieldPropertiesMap.keySet()) {
    	//Set parent table information in map to get foreign key information
    	for(FormPropertyHandler formHandler : fieldPropertiesMap.get(tableName)){
    		tableMap.put(formHandler.getTable().getTableName(),formHandler.getTable().getId());
    	}    	
		if(StringUtil.isEmptyString(properties.get("id"))){
			String id = UUID.randomUUID().toString();
        	properties.put("id", id);
        	properties.put("ID", id);
		}else{
	    	properties.put("ID", properties.get("id"));	
	    	isSameFormUpdate = true;
	    }
		
    	List<String>columns = new ArrayList<String>();
        List<String>fieldValues = new ArrayList<String>();
        parentFormPropertyHandlers = fieldPropertiesMap.get(tableName);
        parentForms.addAll(parentFormPropertyHandlers);
        Map<String,String> columnValues = new HashMap<String,String>();

	    if(I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("mssql") ) {
			generateColumnAndFieldValuesMssql(columns, fieldValues, parentFormPropertyHandlers, properties, files, filePaths,columnValues, isUpdate,isSameFormUpdate,allProperties);
	    } else if (I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("oracle")) {
			generateColumnAndFieldValuesOracle(columns, fieldValues, parentFormPropertyHandlers, properties, files, filePaths,columnValues, isUpdate,isSameFormUpdate,allProperties);
	    } else {
			generateColumnAndFieldValues(columns, fieldValues, parentFormPropertyHandlers, properties, files, filePaths,columnValues, isUpdate,isSameFormUpdate,allProperties);
	    }

		columns.add("`ID`");
		fieldValues.add(StringUtil.getDBStringValue(properties.get("id")));
		columns.add("`CREATEDBY`");
		fieldValues.add(StringUtil.getDBStringValue(properties.get("CREATEDBY")));
		
		columns.add("`MODIFIEDBY`");
		fieldValues.add(StringUtil.getDBStringValue(properties.get("MODIFIEDBY")));
		
		columns.add("`CREATEDTIME`");
		fieldValues.add(StringUtil.getDBDateValue(properties.get("CREATEDTIME")));
		
		columns.add("`MODIFIEDTIME`");
		fieldValues.add(StringUtil.getDBDateValue(properties.get("MODIFIEDTIME")));
		
		columns.add("`TASK_ID`");
		fieldValues.add(StringUtil.getDBStringValue(properties.get("TASK_ID")));
		
		columns.add("`PROC_INST_ID`");
		fieldValues.add(StringUtil.getDBStringValue(properties.get("PROC_INST_ID")));
		
		columns.add("`EXECUTION_ID`");
		fieldValues.add(StringUtil.getDBStringValue(properties.get("EXECUTION_ID")));	
		
		columns.add("`IS_LAST_TASK`");
		fieldValues.add(properties.get("isLastTask"));
		
		//Set table relation information in map to get foreign key information
		Map<String,Object> tableDetails = new HashMap<String,Object>();
		tableDetails.put("parentTableId", tableMap.get(tableName));
		tableDetails.put("tableId", table.getId()); // primary table will be child table
		
		//Get foreign key information
		List<Map<String,Object>> foreignKeyIdNames = Context.getCommandContext().getTableEntityManager().selectForeignKeyByTableId(tableDetails);
		List<String> newColumns=new ArrayList<String>();
    	String foreignKeyValue ;
    	Map<String, Object>paramsMapforCheck = new HashMap<String, Object>();
		for(Map<String,Object> keyMap :foreignKeyIdNames){
			foreignKeyValue =  columnValues.get(String.valueOf(keyMap.get("FOREIGN_KEY_COLUMN_NAME")).toUpperCase());
			//relationShipKeyValues.put(String.valueOf(keyMap.get("FOREIGN_KEY_COLUMN_NAME")).toUpperCase(), foreignKeyValue);
			paramsMapforCheck.put("primaryKey", String.valueOf(keyMap.get("FOREIGN_KEY_COLUMN_NAME")).toUpperCase());
			paramsMapforCheck.put("primaryKeyVal", "'"+foreignKeyValue+"'");
			relationShipKeyValues.put(String.valueOf(keyMap.get("CHILD_KEY_COLUMN_NAME")).toUpperCase(), foreignKeyValue);
		}
 	    if(I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("mssql") || I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("oracle") ) {
	    	for(String column : columns) {
	    		newColumns.add(column.replace("`", ""));
	    	}
	    	 paramsMap.put("columns", newColumns);
	    } else {
	    	 paramsMap.put("columns", columns);
	    }
		
		paramsMapforCheck.put("tableName", tableName);
		paramsMap.put("tableName", tableName);
	    paramsMap.put("fieldValues", fieldValues);
	    
	    //Check the record already exist or not if exist record will be update else new entry will be create
	    boolean checkForUpdate = Context.getCommandContext().getTableEntityManager().getRtValues(paramsMapforCheck);
	    if(checkForUpdate||isUpdate || isSameFormUpdate){
	    	paramsMap.putAll(paramsMapforCheck);
	    	Context.getCommandContext().getTableEntityManager().updateRtValues(paramsMap);
	    	
	    }else{
	    	Context.getCommandContext().getTableEntityManager().storeRtValues(paramsMap);
	    }
	}

    //保存表单数据到数据库表中
    saveRtValuesForPrimaryTable(primaryTable, primaryFormPropertyHandlers, relationShipKeyValues, properties, subProperties, files, filePaths, isUpdate, allProperties, subFormProperties);
    
    //adding all subproperties map to make a entry in historic detail
    allProperties.putAll(subFormProperties); 
    execution.setPresentTaskId((String)properties.get("taskId"));
    
    if(parentForms != null) {
        primaryFormPropertyHandlers.addAll(parentForms); // adding parent form property handlers 
    }
    
    
    //保存或更新历史详情
    //showTaskFormDetail中的pastValuesMap就是从历史详情表ETEC_ACT_HI_DETAIL中获取
    //每个任务节点的第一次提交或保存操作插入历史详情，以后的所有操作都是更新历史详情。
    //用isUpdate来做判断不太合适,暂时只进行插入操作，取的时候进行处理。
    
    Context.getCommandContext().getHistoryManager().reportFormPropertiesSubmitted(execution, allProperties, taskId, primaryFormPropertyHandlers, formId, subProperties);
	
   	Map<String, Object> tableDetailsMap = new HashMap<String, Object>();
   	tableDetailsMap.put("processInstId", execution.getProcessInstanceId());
   	tableDetailsMap.put("tableName", table.getTableName());
   	Context.getCommandContext().getTableEntityManager().storeProcessTableValues(tableDetailsMap);
   	
    /**
    if(isUpdate){
    	if(subFormPropertyHandlers!=null && subFormPropertyHandlers.size()>0){
	    	Context.getCommandContext().getHistoryManager().reportFormPropertiesSubmitted(execution, allProperties, taskId, primaryFormPropertyHandlers, formId, subProperties);
	    	for (String propertyId: allProperties.keySet()) {
	    		 execution.setVariable(propertyId, allProperties.get(propertyId));
	    	}
	    } else {
	    	Context.getCommandContext().getHistoryManager().reportFormPropertiesUpdated(execution.getId(), taskDefinitionKey, allProperties, taskId,primaryFormPropertyHandlers);
	    }
    }else {
    	Context.getCommandContext().getHistoryManager().reportFormPropertiesSubmitted(execution, allProperties, taskId, primaryFormPropertyHandlers, formId, subProperties);
    	
    	Map<String, Object> tableDetailsMap = new HashMap<String, Object>();
    	tableDetailsMap.put("processInstId", execution.getProcessInstanceId());
    	tableDetailsMap.put("tableName", table.getTableName());
    	Context.getCommandContext().getTableEntityManager().storeProcessTableValues(tableDetailsMap);
    }
	**/

    //保存流程变量或者更新值有变化的流程变量
    //进行更新操作时，所有任务节点的流程变量都会被更新
    Map<String, String> propertiesCopy = new HashMap<String, String>(properties);
    if(!execution.isTemp()){
    	for (String propertyId: propertiesCopy.keySet()) {
    		execution.setVariable(propertyId, propertiesCopy.get(propertyId));
    	}
	    if(filePaths!=null && filePaths.size()>0){
	    	for (String fileName  : filePaths.keySet()) {
				execution.setVariable(fileName, filePaths.get(fileName));
			}
	    } 
    }
    //for form fields Audit log
    if(taskId != null) {
        saveFormFieldAudit(properties,execution.getProcessInstanceId(),formId,userId,taskId);   
    }
  }
  
  
	/**
	 * 保存表单数据到数据库表中
	 * 此时properties中的元素要比 allProperties中的元素多9个
	 * 
	 * @param primaryTable
	 * @param primaryFormPropertyHandlers
	 * @param relationShipKeyValues
	 * @param properties
	 * @param subProperties
	 * @param files
	 * @param filePaths
	 * @param isUpdate
	 * @param allProperties
	 * @param subFormProperties
	 * @throws DataSourceException
	 */
	private void saveRtValuesForPrimaryTable(String primaryTable, List<FormPropertyHandler> primaryFormPropertyHandlers, Map<String, String> relationShipKeyValues, 
		  Map<String, String> properties, Map<String, String[]> subProperties, Map<String, byte[]> files, Map<String, String> filePaths, 
		  boolean isUpdate, Map<String, String> allProperties, Map<String, String> subFormProperties) throws DataSourceException{
	  
		List<String> columns = new ArrayList<String>();
		List<String> columnList = new ArrayList<String>();
		List<String> fieldValues = new ArrayList<String>();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		boolean isSameFormUpdate = false;
		
		//生成主键,properties中没有key为id的元素，所以每次都是insert，如何处理？
		if (StringUtil.isEmptyString(properties.get("id"))) {
			if (isUpdate) {
				throw new DataSourceException("No id provided for update");
			}
			String id = UUID.randomUUID().toString();
			properties.put("id", id);
			properties.put("ID", id);
		} else {
			properties.put("ID", properties.get("id"));
			isSameFormUpdate = true;
		}
		Map<String, String> columnValues = new HashMap<String, String>();

		if (I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("mssql")) {
			generateColumnAndFieldValuesMssql(columns, fieldValues, primaryFormPropertyHandlers, properties, files, filePaths, columnValues, isUpdate, isSameFormUpdate, allProperties);
		} else if (I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("oracle")) {
			generateColumnAndFieldValuesOracle(columns, fieldValues, primaryFormPropertyHandlers, properties, files, filePaths, columnValues, isUpdate, isSameFormUpdate, allProperties);
		} else {
			generateColumnAndFieldValues(columns, fieldValues, primaryFormPropertyHandlers, properties, files, filePaths, columnValues, isUpdate, isSameFormUpdate, allProperties);
		}

		// Now we are setting only foreign key relationship so no need this code
		for (String relationShipKey : relationShipKeyValues.keySet()) {
			columns.add("`" + relationShipKey + "`");
			fieldValues.add(StringUtil.getDBStringValue(relationShipKeyValues.get(relationShipKey)));
			columnValues.put(relationShipKey, relationShipKeyValues.get(relationShipKey));

		}
		if (isUpdate || isSameFormUpdate) {
			paramsMap.put("primaryKey", "ID");
			paramsMap.put("primaryKeyVal", StringUtil.getDBStringValue(properties.get("id")));
		}
		if (I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("mssql") || I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("oracle")) {
			for (String column : columns) {
				columnList.add(column.replace("`", ""));
			}
			paramsMap.put("columns", columnList);
		} else {
			paramsMap.put("columns", columns);
		}
		paramsMap.put("tableName", primaryTable);
		paramsMap.put("fieldValues", fieldValues);

		if (subFormPropertyHandlers != null && subFormPropertyHandlers.size() > 0) {
			String primaryTableId = null;
			for (FormPropertyHandler formProHand : primaryFormPropertyHandlers) {
				primaryTableId = formProHand.getTable().getId();
			}
			constructSubFormValuesAsMap(subProperties, subFormProperties);
			properties.putAll(subFormProperties);
			primaryFormPropertyHandlers.addAll(subFormPropertyHandlers);
			submitSubFormProperties(primaryTable, properties.get("id"), subProperties, properties, primaryTableId, columnValues, paramsMap, isUpdate);
		} else {

			if (isUpdate && isSameFormUpdate) {
				Context.getCommandContext().getTableEntityManager().updateRtValues(paramsMap);
			} else {
				Context.getCommandContext().getTableEntityManager().storeRtValues(paramsMap);
			}

		}
	}
  
  private void generateColumnAndFieldValues(List<String>columns, List<String>fieldValues, List<FormPropertyHandler>formPropertyHandlers, Map<String, String>properties, Map<String, byte[]>files, Map<String, String>filePaths,Map<String, String> columnValues, boolean isUpdate,boolean isSameFormUpdate, Map<String, String> allProperties)throws DataSourceValidationException, DataSourceException{
	  Map<String, String> columnValuesMap = new HashMap<String, String>();
	  if(formPropertyHandlers!=null){

		  for (FormPropertyHandler formPropertyHandler: formPropertyHandlers) {
			  System.out.println("=========="+formPropertyHandler.getName()+"=====colu====="+formPropertyHandler.getColumn().getName());
		    	MetaTableColumns column = formPropertyHandler.getColumn();
				System.out.println("======coumn===="+column.getType()+"==========="+column.getTable());

		    	String fieldValue = properties.get(formPropertyHandler.getName());
		    	boolean isPermitted = checkForNodeLevelFieldPermission(formPropertyHandler, fieldValue, isUpdate,properties);
    			if(!isPermitted){
    				continue;
    			}
		    	if(columns.contains("`"+formPropertyHandler.getColumn().getName()+"`")){
		    		continue;
		    	}
		    	DataSourceUtil.isFieldValidForColumn(formPropertyHandler.getName(), fieldValue, column);
		    	
		    	
		    	boolean isAutoGenerationId=false;
		    	if(!isUpdate && !isSameFormUpdate ){
		    		isAutoGenerationId=true;
		    	}else if(column.getAutoGenerationId().isEmpty()){
		    		isAutoGenerationId=true;
		    	}
		    	
		    	if(isAutoGenerationId){
		    		columns.add("`"+formPropertyHandler.getColumn().getName()+"`");
		    		
		    		 if(DataSourceUtil.isColumnDateType(column.getType())){
				        	fieldValue = StringUtil.getDBDateValue(fieldValue);
				        }
				        else if(DataSourceUtil.isColumnTextType(column.getType())){
				        	
				        	fieldValue = StringUtil.isEmptyString(fieldValue) ? "'"+column.getDefaultValue()+"'" : StringUtil.getDBStringValue(fieldValue);
				        }else if(DataSourceUtil.isColumnBooleanType(column.getType())){
				        	fieldValue = StringUtil.isEmptyString(fieldValue) ? "'"+column.getDefaultValue()+"'" : StringUtil.checkAndParseBooleanAttribute(fieldValue, column);				        	
				        }else{
				        	fieldValue = StringUtil.isEmptyString(fieldValue) ? "'"+column.getDefaultValue()+"'" : fieldValue;
				        }
				        AbstractFormType handlerType = formPropertyHandler.getType();
				        if(DataSourceUtil.isColumnByteType(column.getType())||(handlerType!=null && handlerType.getName().equals("file"))){
		        			if(filePaths!=null && !filePaths.isEmpty()){
			        			//String byteString = (new String(files.get(formPropertyHandler.getName()), "UTF-8"));
				        		//fieldValue = StringUtil.getDBStringValue(byteString.replaceAll("'", "\\\\'"));
			        			fieldValue=StringUtil.getDBStringValue(filePaths.get(formPropertyHandler.getName()));        			
			        		}else{
			        			fieldValue = StringUtil.getDBStringValue(fieldValue);
			        		}        		
						}
			        	columnValuesMap.put(formPropertyHandler.getColumn().getName(), fieldValue.replaceAll("'", ""));
				        fieldValues.add(fieldValue);
		    	}
		    }
	
		  if(columnValues!=null){
			  columnValues.putAll(columnValuesMap);
		  }
		  if(!isUpdate && !isSameFormUpdate){
			  for (FormPropertyHandler formPropertyHandler: formPropertyHandlers) {
				  MetaTableColumns column = formPropertyHandler.getColumn();
				  
				  if(column.getAutoGenerationId() != null && !column.getAutoGenerationId().isEmpty()){
					 
			        String autoGenerationExpression = column.getAutoGenerationId();

			   	  	 // Mapping column is the column that having Auto Generation Id
					 String	mappingColumn = column.getName(); 
		        	 
			   	  	 // Mapped column is the column that present inside the  Auto Generation Id expression 

			         String	mappedColumn = getAutoIdMappedColumn(autoGenerationExpression);
			         if(mappedColumn != null){
			        	 
			        	 String mappedColumnValue = columnValuesMap.get(mappedColumn.toUpperCase());
				        	if(mappedColumnValue != null && !mappedColumnValue.isEmpty()){

				        		mappedColumnValue = mappedColumnValue.replaceAll("\\'", "");
				        		        		
				        		column = getAutoGenerationIdForColumn(column, mappedColumnValue);
				        		
				        	}else{
				        		column = getAutoGenerationIdForColumn(column, null);
				        	}
				     }else{
				    	 column = getAutoGenerationIdForColumn(column, null); 
				     }
			         String autoGeneratedId = column.getAutoGenerationId();
		        		column.setAutoGenerationId(autoGenerationExpression);
		        		formPropertyHandler.setColumn(column);
		        		if(autoGeneratedId != null && !autoGeneratedId.isEmpty()){
		        			 int i=0;
		        			 for(String col: columns ){
		        				if(col.contains(mappingColumn)){
				        			fieldValues.set(i, "'"+autoGeneratedId+"'");
				        			//insert the auto generation id value to form
				        			allProperties.put(formPropertyHandler.getName(), autoGeneratedId);
		        				}			        				
		        				i = i+1; 
		        			}	        			   				        			
		        		}	         
				  	}
				}
		  }
		 
	  }	  
  }
  
 private void generateColumnAndFieldValuesMssql(List<String>columns, List<String>fieldValues, List<FormPropertyHandler>formPropertyHandlers, Map<String, String>properties, Map<String, byte[]>files, Map<String, String>filePaths,Map<String, String> columnValues, boolean isUpdate,boolean isSameFormUpdate, Map<String, String> allProperties)throws DataSourceValidationException, DataSourceException{
	  Map<String, String> columnValuesMap = new HashMap<String, String>();
	  if(formPropertyHandlers!=null){

		  for (FormPropertyHandler formPropertyHandler: formPropertyHandlers) {
		    	MetaTableColumns column = formPropertyHandler.getColumn();
		    	String fieldValue = properties.get(formPropertyHandler.getName());
		    	boolean isPermitted = checkForNodeLevelFieldPermission(formPropertyHandler, fieldValue, isUpdate,properties);
   			if(!isPermitted){
   				continue;
   			}
		    	if(columns.contains(formPropertyHandler.getColumn().getName())){
		    		continue;
		    	}
		    	DataSourceUtil.isFieldValidForColumn(formPropertyHandler.getName(), fieldValue, column);
		    	
		    	
		    	boolean isAutoGenerationId=false;
		    	if(!isUpdate && !isSameFormUpdate ){
		    		isAutoGenerationId=true;
		    	}else if(column.getAutoGenerationId().isEmpty()){
		    		isAutoGenerationId=true;
		    	}
		    	
		    	if(isAutoGenerationId){
		    		columns.add(formPropertyHandler.getColumn().getName());
					//System.out.println("=========column type===="+column.getType()+"=====table==="+column.getTable()+"==column name=="+column.getName());
		    		 if(DataSourceUtil.isColumnDateType(column.getType())){
				        	fieldValue = StringUtil.getDBDateValue(fieldValue);
				        }
				        else if(DataSourceUtil.isColumnTextType(column.getType())){
				        	fieldValue = StringUtil.isEmptyString(fieldValue) ? "'"+column.getDefaultValue()+"'" : StringUtil.getDBStringValue(fieldValue);
				        }else if(DataSourceUtil.isColumnBooleanType(column.getType())){
				        	fieldValue = StringUtil.isEmptyString(fieldValue) ? "'"+column.getDefaultValue()+"'" : StringUtil.checkAndParseBooleanAttribute(fieldValue, column);				        	
				        }else{
				        	fieldValue = StringUtil.isEmptyString(fieldValue) ? "'"+column.getDefaultValue()+"'" : fieldValue;
				        }
				        AbstractFormType handlerType = formPropertyHandler.getType();
				        if(DataSourceUtil.isColumnByteType(column.getType())||(handlerType!=null && handlerType.getName().equals("file"))){
		        			if(filePaths!=null && !filePaths.isEmpty()){
			        			//String byteString = (new String(files.get(formPropertyHandler.getName()), "UTF-8"));
				        		//fieldValue = StringUtil.getDBStringValue(byteString.replaceAll("'", "\\\\'"));
			        			fieldValue=StringUtil.getDBStringValue(filePaths.get(formPropertyHandler.getName()));        			
			        		}else{
			        			fieldValue = StringUtil.getDBStringValue(fieldValue);
			        		}        		
						}
			        	columnValuesMap.put(formPropertyHandler.getColumn().getName(), fieldValue.replaceAll("'", ""));
			    	   // System.out.println("=========="+fieldValue);
				        fieldValues.add(StringUtil.removeQuotesForNull(fieldValue) ? fieldValue.replace("'", "") : fieldValue);
			    	   // System.out.println("=========="+fieldValues);
		    	}
		    }
	
		  if(columnValues!=null){
			  columnValues.putAll(columnValuesMap);
		  }
		  if(!isUpdate && !isSameFormUpdate){
			  for (FormPropertyHandler formPropertyHandler: formPropertyHandlers) {
				  MetaTableColumns column = formPropertyHandler.getColumn();
				  
				  if(column.getAutoGenerationId() != null && !column.getAutoGenerationId().isEmpty()){
					 
			        String autoGenerationExpression = column.getAutoGenerationId();

			   	  	 // Mapping column is the column that having Auto Generation Id
					 String	mappingColumn = column.getName(); 
		        	 
			   	  	 // Mapped column is the column that present inside the  Auto Generation Id expression 

			         String	mappedColumn = getAutoIdMappedColumn(autoGenerationExpression);
			         if(mappedColumn != null){
			        	 
			        	 String mappedColumnValue = columnValuesMap.get(mappedColumn.toUpperCase());
				        	if(mappedColumnValue != null && !mappedColumnValue.isEmpty()){

				        		mappedColumnValue = mappedColumnValue.replaceAll("\\'", "");
				        		        		
				        		column = getAutoGenerationIdForColumn(column, mappedColumnValue);
				        		
				        	}else{
				        		column = getAutoGenerationIdForColumn(column, null);
				        	}
				     }else{
				    	 column = getAutoGenerationIdForColumn(column, null); 
				     }
			         String autoGeneratedId = column.getAutoGenerationId();
		        		column.setAutoGenerationId(autoGenerationExpression);
		        		formPropertyHandler.setColumn(column);
		        		if(autoGeneratedId != null && !autoGeneratedId.isEmpty()){
		        			 int i=0;
		        			 for(String col: columns ){
		        				if(col.contains(mappingColumn)){
				        			fieldValues.set(i, "'"+autoGeneratedId+"'");
				        			//insert the auto generation id value to form
				        			allProperties.put(formPropertyHandler.getName(), autoGeneratedId);
		        				}			        				
		        				i = i+1; 
		        			}	        			   				        			
		        		}	         
				  	}
				}
		  }
		 
	  }	  
 }
  
	private void generateColumnAndFieldValuesOracle(List<String> columns, List<String> fieldValues, List<FormPropertyHandler> formPropertyHandlers, Map<String, String> properties, Map<String, byte[]> files, Map<String, String> filePaths, Map<String, String> columnValues, boolean isUpdate, boolean isSameFormUpdate, Map<String, String> allProperties) throws DataSourceValidationException, DataSourceException {
		Map<String, String> columnValuesMap = new HashMap<String, String>();
		if (formPropertyHandlers != null) {

			for (FormPropertyHandler formPropertyHandler : formPropertyHandlers) {
				MetaTableColumns column = formPropertyHandler.getColumn();
				System.out.println("=====column type===="+column.getType()+"=====table==="+column.getTable()+"==column name=="+column.getName());
				String fieldValue = properties.get(formPropertyHandler.getName());
				boolean isPermitted = checkForNodeLevelFieldPermission(formPropertyHandler, fieldValue, isUpdate, properties);
				if (!isPermitted) {
					continue;
				}
				if (columns.contains(formPropertyHandler.getColumn().getName())) {
					continue;
				}
				DataSourceUtil.isFieldValidForColumn(formPropertyHandler.getName(), fieldValue, column);

				boolean isAutoGenerationId = false;
				if (!isUpdate && !isSameFormUpdate) {
					isAutoGenerationId = true;
				} else if (StringUtil.isEmptyString(column.getAutoGenerationId())) {
					isAutoGenerationId = true;
				}

				if (isAutoGenerationId) {
					AbstractFormType handlerType = formPropertyHandler.getType();
					columns.add(formPropertyHandler.getColumn().getName());
					if (DataSourceUtil.isColumnDateType(column.getType())) {
						fieldValue = StringUtil.getDBDateValue(fieldValue);
					} else if (DataSourceUtil.isColoumnNumberType(column.getType())) {
						fieldValue = StringUtil.isEmptyString(fieldValue) ? column.getDefaultValue() : fieldValue;
					} else if ((handlerType != null && handlerType.getName().equals("file"))) {
						fieldValue = StringUtil.isEmptyString(fieldValue) ? "'" + column.getDefaultValue() + "'" : fieldValue;
					} else if (DataSourceUtil.isColumnTextType(column.getType())) {
						fieldValue = StringUtil.isEmptyString(fieldValue) ? "'" + column.getDefaultValue() + "'" : StringUtil.getDBStringValue(fieldValue);
					} else if (DataSourceUtil.isColumnBooleanType(column.getType())) {
						fieldValue = StringUtil.isEmptyString(fieldValue) ? "'" + column.getDefaultValue() + "'" : StringUtil.checkAndParseBooleanAttribute(fieldValue, column);
					} else {
						fieldValue = StringUtil.isEmptyString(fieldValue) ? "'" + column.getDefaultValue() + "'" : fieldValue.replaceAll("(\r\n|\n)", " ");
					}

					if (DataSourceUtil.isColumnByteType(column.getType()) || (handlerType != null && handlerType.getName().equals("file"))) {
						if (filePaths != null && !filePaths.isEmpty()) {
							fieldValue = StringUtil.getDBStringValue(filePaths.get(formPropertyHandler.getName()));
						} else {
							fieldValue = StringUtil.getDBStringValue(fieldValue);
						}
					}
					columnValuesMap.put(formPropertyHandler.getColumn().getName(), fieldValue.replaceAll("'", ""));
					// System.out.println("============"+fieldValue);
					fieldValues.add(StringUtil.removeQuotesForNull(fieldValue) ? fieldValue.replace("'", "") : fieldValue);
					// System.out.println("============"+fieldValues);

				}
			}

			if (columnValues != null) {
				columnValues.putAll(columnValuesMap);
			}
			if (!isUpdate && !isSameFormUpdate) {
				for (FormPropertyHandler formPropertyHandler : formPropertyHandlers) {
					MetaTableColumns column = formPropertyHandler.getColumn();

					if (column.getAutoGenerationId() != null && !column.getAutoGenerationId().isEmpty()) {

						String autoGenerationExpression = column.getAutoGenerationId();

						// Mapping column is the column that having Auto
						// Generation Id
						String mappingColumn = column.getName();

						// Mapped column is the column that present inside the
						// Auto Generation Id expression

						String mappedColumn = getAutoIdMappedColumn(autoGenerationExpression);
						if (mappedColumn != null) {

							String mappedColumnValue = columnValuesMap.get(mappedColumn.toUpperCase());
							if (mappedColumnValue != null && !mappedColumnValue.isEmpty()) {

								mappedColumnValue = mappedColumnValue.replaceAll("\\'", "");

								column = getAutoGenerationIdForColumn(column, mappedColumnValue);

							} else {
								column = getAutoGenerationIdForColumn(column, null);
							}
						} else {
							column = getAutoGenerationIdForColumn(column, null);
						}
						String autoGeneratedId = column.getAutoGenerationId();
						column.setAutoGenerationId(autoGenerationExpression);
						formPropertyHandler.setColumn(column);
						if (autoGeneratedId != null && !autoGeneratedId.isEmpty()) {
							int i = 0;
							for (String col : columns) {
								if (col.contains(mappingColumn)) {
									fieldValues.set(i, "'" + autoGeneratedId + "'");
									// insert the auto generation id value to
									// form
									allProperties.put(formPropertyHandler.getName(), autoGeneratedId);
								}
								i = i + 1;
							}
						}
					}
				}
			}

		}
	}
  
  private boolean checkForNodeLevelFieldPermission(FormPropertyHandler formPropertyHandler, String fieldValue, boolean isUpdate, Map<String, String> properties){
	  if(taskRoleForUser==null){
		  return true;
	  }else{
		  Map<String, Boolean>nodeLevelPermission = TaskUtil.getNodeLevelPermissionForTaskRole(taskRoleForUser, formPropertyHandler);
		  if(nodeLevelPermission==null||nodeLevelPermission.isEmpty()){
			  return true;
		  }
		  boolean isEditable = true;
		  if(isUpdate){
			  isEditable = checkEditable(formPropertyHandler, nodeLevelPermission, fieldValue);
		  }
		  return isEditable&&checkRequired(formPropertyHandler, nodeLevelPermission, fieldValue,properties)&&
				  checkReadonly(formPropertyHandler, nodeLevelPermission, fieldValue)&&
				  checkHidden(formPropertyHandler, nodeLevelPermission, fieldValue);
				  
	  }
  }  
  
  private boolean checkEditable(FormPropertyHandler formPropertyHandler, Map<String, Boolean>nodeLevelPermission, String fieldValue){
	  if(nodeLevelPermission.get(FormFieldPermission.IS_EDITABLE)){
		  return true;
	  }else{
		  return false;
	  }
	  
  }
  
  private boolean checkRequired(FormPropertyHandler formPropertyHandler, Map<String, Boolean>nodeLevelPermission, String fieldValue,Map<String, String>properties){
	  String fieldName = formPropertyHandler.getName();
	  String isJump = null;
	  if(properties.containsKey("isAutoSave")) {
		  isJump = properties.get("isAutoSave");
	  }
	  if(nodeLevelPermission.get(FormFieldPermission.IS_REQUIRED) ){
		  if(StringUtil.isEmptyString(fieldValue) && !isJump.equalsIgnoreCase("true")  ){
			  
			  throw new DataSourceValidationException("Field is a required field: "+fieldName);
		  }
	  }
	  return true;
  }
  
  private boolean checkReadonly(FormPropertyHandler formPropertyHandler, Map<String, Boolean>nodeLevelPermission, String fieldValue){
	  if(nodeLevelPermission.get(FormFieldPermission.IS_READ_ONLY)){
		  return false;
	  }
	  return true;
  }
  
  private boolean checkHidden(FormPropertyHandler formPropertyHandler, Map<String, Boolean>nodeLevelPermission, String fieldValue){
	  if(nodeLevelPermission.get(FormFieldPermission.IS_HIDDEN)){
		  return false;
	  }
	  return true;
  }
  
  
  
  
  
  private String getAutoIdMappedColumn(String autoGenerationIdExpression){
	  String[] autoIdExpression =  autoGenerationIdExpression.split("\\|");
	  String columnName = null;
	  if(autoIdExpression.length > 1 && !autoIdExpression[1].isEmpty()){
		  columnName = autoIdExpression[1];
		  columnName = columnName.replaceAll("\\{", "");
		  columnName = columnName.replaceAll("\\}", "");
	  }
	  return columnName;	  
  }
  
	private MetaTableColumns getAutoGenerationIdForColumn(
			MetaTableColumns metaTableColumn, String mappedColumnValue) {
		String autoGenerationIdExpression = metaTableColumn
				.getAutoGenerationId();
		String autoGeneratedId = null;
		String splitSeperator = "";
		String joinSeperator = "";

		if (StringUtils.countMatches(autoGenerationIdExpression, "|") > 0) {
			splitSeperator = "\\|";
			joinSeperator = "";
		}

		autoGenerationIdExpression = autoGenerationIdExpression.replaceAll(
				"\\{", "");
		autoGenerationIdExpression = autoGenerationIdExpression.replaceAll(
				"\\}", "");

		if (autoGenerationIdExpression.contains("|")) {

			String[] autoIdExpression = autoGenerationIdExpression
					.split(splitSeperator);

			if (!autoIdExpression[0].isEmpty()) {

				autoGeneratedId = autoIdExpression[0];

				if (autoIdExpression.length > 1) {if (mappedColumnValue != null && !mappedColumnValue.equalsIgnoreCase("NULL")) {
					autoGeneratedId = autoGeneratedId + joinSeperator
							+ mappedColumnValue;
				}

				int index = 0;
				if ((autoIdExpression.length > 2)
						&& (!autoIdExpression[2].isEmpty()) && (mappedColumnValue != null)){
					index = 2;
				}else if ((autoIdExpression.length == 2) && (mappedColumnValue == null)){
					index = 1;
				}
				
				if (index != 0) {

					autoGeneratedId = autoGeneratedId ;
					Date date = new Date();

					if (autoIdExpression[index].contains("Year")) {
						SimpleDateFormat simpleDateformat = new SimpleDateFormat(
								"yyyy");
						autoGeneratedId = autoGeneratedId
								+ simpleDateformat.format(date).toString();

					}
					if (autoIdExpression[index].contains("year")) {
						SimpleDateFormat simpleDateformat = new SimpleDateFormat(
								"yy");
						autoGeneratedId = autoGeneratedId
								+ simpleDateformat.format(date).toString();

					}
					if (autoIdExpression[index].contains("month")) {
						SimpleDateFormat simpleDateformat = new SimpleDateFormat(
								"MM");
						autoGeneratedId = autoGeneratedId
								+ simpleDateformat.format(date).toString();

					}
					if (autoIdExpression[index].contains("day")) {
						SimpleDateFormat simpleDateformat = new SimpleDateFormat(
								"dd");
						autoGeneratedId = autoGeneratedId
								+ simpleDateformat.format(date).toString();
					}
					if (autoIdExpression[index].contains("*")) {
						int starIndex=autoIdExpression[index].lastIndexOf("*");
						int stringLenght=autoIdExpression[index].length()-1;
						int autoIdPrefix=metaTableColumn.getIdNumber()+1;
						String autoIdPrefixString="";
						if(starIndex!=stringLenght){
							autoIdPrefixString=String.format("%0"+autoIdExpression[index].charAt(stringLenght)+"d", autoIdPrefix);
						}else{
							autoIdPrefixString=String.valueOf(autoIdPrefix);
						}
						
						autoGeneratedId = autoGeneratedId
								+ (autoIdPrefixString);
						metaTableColumn.setIdNumber(autoIdPrefix);
						Context.getCommandContext()
								.getTableEntityManager()
								.updateTableColumnIdNumberById(
										metaTableColumn.getId());
					}
				}
			}
			}
			metaTableColumn.setAutoGenerationId(autoGeneratedId);
			return metaTableColumn;
		} else if (autoGenerationIdExpression != null
				&& !autoGenerationIdExpression.isEmpty()) {
			metaTableColumn.setAutoGenerationId(autoGenerationIdExpression);
			return metaTableColumn;
		} else {
			return null;
		}
	}
  
	private void saveFormFieldAudit(Map<String,String> properties,String processInstanceId,String formId,String userId,String taskId){
	    Map<String,FormPropertyHandler> formFieldNewValues = new HashMap<String, FormPropertyHandler>();
	    //Extracting only form fields except common fields like CREATED_TIME,CREATED_BY etc
	    for(FormPropertyHandler formPropertyHandler: formPropertyHandlers) {
	    	System.out.println("======audit=========="+formPropertyHandler.getName());
	  	  if(properties.containsKey(formPropertyHandler.getName())){
	  		formFieldNewValues.put(formPropertyHandler.getColumn().getId(),formPropertyHandler);
	  	  }
	    } //end  
	    Map<String,Object> modifiedFileds = new HashMap<String, Object>();
	    //getting old values for the formId
	    List<Map<String,Object>> historicDetailsMap = Context.getCommandContext().getHistoricDetailEntityManager().selectHistoricDetailsAsMap(processInstanceId, formId);
	    //extracting only last transaction elements
	    Map<String,Object> formFieldOldValuesMap = new HashMap<String,Object>();
        Map<String, Object> historicDetailTreeMap = null;

	    for(Map<String,Object> historicDetails : historicDetailsMap){
	    	historicDetailTreeMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
        	historicDetailTreeMap.putAll(historicDetails);
        	String columnValue = (String) historicDetailTreeMap.get("columnValue");
        	if(columnValue == null) {
        		columnValue = "";
        	}
			if(!formFieldOldValuesMap.containsKey(historicDetailTreeMap.get("columnId"))){
				formFieldOldValuesMap.put(historicDetailTreeMap.get("columnId").toString(), columnValue);
			}
		}//end getting old values
	    
	    if(formFieldOldValuesMap.size() > 0){
	    	Set<List<String>>fieldValuesList = new HashSet<List<String>>(); 
	    	List<String>columns = new ArrayList<String>();
	    	List<String> newColumnList = new ArrayList<String>();
	        columns.add("`ID_`");
	        columns.add("`PROC_INST_ID_`");
	        columns.add("`MODIFIED_TIME_`");
	        columns.add("`MODIFIED_BY_`");
	        columns.add("`FILED_NAME_`");
	        columns.add("`CHINESE_FILED_NAME_`");
	        columns.add("`OLD_VALUE_`");
	        columns.add("`NEW_VALUE_`");
	        columns.add("`FORM_ID_`");
	        columns.add("`TASK_ID_`");
	    	for(String columnId : formFieldOldValuesMap.keySet()){
			    List<String>fieldValues = new ArrayList<String>();
		        FormPropertyHandler formPropertyHandler = formFieldNewValues.get(columnId);
		        if(formPropertyHandler != null){
		        	System.out.println("=========val=========="+formFieldOldValuesMap.get(columnId));
		        	
		        	System.out.println("====old==val==="+formFieldOldValuesMap.get(columnId));
		        	System.out.println("====new==val==="+properties.get(formPropertyHandler.getName()));
		        	//checking whether the field value is differ from old value or not
		        	//insert if the filed value differ
		        	if(!properties.get(formPropertyHandler.getName()).equals(formFieldOldValuesMap.get(columnId))){
						fieldValues.add(StringUtil.getDBStringValue(UUID.randomUUID().toString()));
						fieldValues.add(StringUtil.getDBStringValue(processInstanceId));
						fieldValues.add(StringUtil.getDBDateValue(DateUtil.getCurrentDateTime()));
						fieldValues.add(StringUtil.getDBStringValue(userId));
						fieldValues.add(StringUtil.getDBStringValue(formPropertyHandler.getName()));
						fieldValues.add(StringUtil.getDBStringValue(formPropertyHandler.getColumn().getChineseName()));
						fieldValues.add(StringUtil.getDBStringValue(formFieldOldValuesMap.get(columnId)));
						fieldValues.add(StringUtil.getDBStringValue(properties.get(formPropertyHandler.getName())));
						fieldValues.add(StringUtil.getDBStringValue(formId));
						fieldValues.add(StringUtil.getDBStringValue(taskId));
						fieldValuesList.add(fieldValues);
		        	}	
		        }
			}
	    	//inserting into table
	    	if(fieldValuesList.size() > 0){
	    		modifiedFileds.put("tableName", I18nUtil.getMessageProperty("databaseTablePrefix")+"ACT_HI_FORM_FILED_AUDIT");
	    		modifiedFileds.put("fieldValues", fieldValuesList);
	    	    if(I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("mssql") || I18nUtil.getMessageProperty("db.source").equalsIgnoreCase("oracle")) {
					for(String column : columns) {
						newColumnList.add(column.replace("`", ""));
	    	    	}
					modifiedFileds.put("columns", newColumnList);
	    	    	//modifiedFileds.put("columns",  columns.toString().replaceAll("`", ""));
	    	    } else {
	    	    	modifiedFileds.put("columns", columns);
	    	    }	    		
	    		Context.getCommandContext().getTableEntityManager().storeRtSubValues(modifiedFileds);
	    	}
	    }
	}

  // getters and setters //////////////////////////////////////////////////////
  
  public Expression getFormKey() {
    return formKey;
  }
  
  public void setFormKey(Expression formKey) {
    this.formKey = formKey;
  }
  
  public String getDeploymentId() {
    return deploymentId;
  }
  
  public void setDeploymentId(String deploymentId) {
    this.deploymentId = deploymentId;
  }
  
  public List<FormPropertyHandler> getFormPropertyHandlers() {
    return formPropertyHandlers;
  }
  
  public void setFormPropertyHandlers(List<FormPropertyHandler> formPropertyHandlers) {
    this.formPropertyHandlers = formPropertyHandlers;
  }

public List<FormPropertyHandler> getSubFormPropertyHandlers() {
	return subFormPropertyHandlers;
}

public void setSubFormPropertyHandlers(
		List<FormPropertyHandler> subFormPropertyHandlers) {
	this.subFormPropertyHandlers = subFormPropertyHandlers;
}

public TaskRole getTaskRoleForUser() {
	return taskRoleForUser;
}

public void setTaskRoleForUser(TaskRole taskRoleForUser) {
	this.taskRoleForUser = taskRoleForUser;
}
}
