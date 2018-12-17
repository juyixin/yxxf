package com.eazytec.bpm.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eazytec.Constants;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.LabelValue;

/**
 * Hold functions commonly used for bpm related task specific util methods.
 * 
 * @author madan
 *
 */
public class ProcessDefinitionUtil {

	/**
	 * Set into context of grid column names and field names and it attributes
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForProcess(VelocityEngine velocityEngine, List<Map<String, Object>> processDefinitions, String type, Locale locale) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		String jsonFieldValues = "";
		if (type.equalsIgnoreCase("myInstances") || type.equalsIgnoreCase("myDocumentInstance")) {
			createMyInstancesGridColumnMapping(context, locale, type);
		} else if (type.equalsIgnoreCase("processDefinitions")) {
			createProcessDefinitionsGridColumnMapping(context, locale);
		} else if (type.equalsIgnoreCase("listProcess")) {
			createProcessListGridColumnMapping(context, locale);
		} else if (type.equalsIgnoreCase("monitorProcess") || type.equalsIgnoreCase("myMonitorProcesses")) {
			createMonitoredProcessGridColumnMapping(context, locale, type);
		} else if (type.equalsIgnoreCase("journal")) {
			createJournalGridColumnMapping(context, locale);
		}
		try {
			if (processDefinitions != null && !(processDefinitions.isEmpty())) {
				jsonFieldValues = CommonUtil.getJsonString(processDefinitions);
			}
		} catch (Exception e) {
			throw new EazyBpmException("Problem parsing processDefinitions as json", e);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		return GridUtil.generateScript(velocityEngine, context);
	}

	/**
	 * Set into context of grid column names and field names and it attributes
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForFlowStatistics(VelocityEngine velocityEngine, List<Map<String, Object>> processDefinitions, String type, Locale locale) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		String jsonFieldValues = "";
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> groupHeaderList = new ArrayList<Map<String, Object>>();
		int departmentCount = 1;
		int processCount = 0;
		String columnNames = "['流程名称'";
		if (processDefinitions != null) {
			for (Map<String, Object> processDefinition : processDefinitions) {
				departmentCount = Integer.parseInt(processDefinition.get("departmentCount").toString());
				processCount = Integer.parseInt(processDefinition.get("processCount").toString());
				Map<String, Object> groupHeaderMap = null;
				for (int i = 0; i < departmentCount; i++) {
					groupHeaderMap = new HashMap<String, Object>();
					groupHeaderMap.put("startColumnName", "succeededCount" + i);
					groupHeaderMap.put("numberOfColumns", 4);
					groupHeaderMap.put("titleText", toCamelCase(processDefinition.get("departmentName" + i).toString()));
					groupHeaderList.add(groupHeaderMap);
				}
				break;
			}
		}
		String columnWidth = String.valueOf(Math.round(Math.floor(80 / (departmentCount * 4))));
		String dynamicColumns = ",'已成功','进行中','已暂停','已终止'";
		CommonUtil.createFieldNameList(fieldNameList, "processName", "20", "left", "", "false");
		for (int i = 0; i < departmentCount; i++) {
			columnNames = columnNames + dynamicColumns;
			CommonUtil.createFieldNameList(fieldNameList, "succeededCount" + i, columnWidth, "center", "", "false");
			CommonUtil.createFieldNameList(fieldNameList, "inProgressCount" + i, columnWidth, "center", "", "false");
			CommonUtil.createFieldNameList(fieldNameList, "suspendedCount" + i, columnWidth, "center", "", "false");
			CommonUtil.createFieldNameList(fieldNameList, "terminatedCount" + i, columnWidth, "center", "", "false");
		}
		columnNames = columnNames + "]";
		context.put("title", "流程办件统计");
		context.put("gridId", "Flow_Statistics");
		context.put("needTreeStructure", true);
		context.put("needHeader", true);
		context.put("dynamicGridWidth", "organizationGridWidth");
		context.put("dynamicGridHeight", "organizationGridHeight");

		context.put("columnNames", columnNames);
		context.put("noOfRecords", "16");
		context.put("fieldNameList", fieldNameList);
		context.put("groupHeaderList", groupHeaderList);
		if (processCount > 0) {
			try {
				if (processDefinitions != null && !(processDefinitions.isEmpty())) {
					jsonFieldValues = CommonUtil.getJsonString(processDefinitions);
				}
			} catch (Exception e) {
				throw new EazyBpmException("Problem parsing processDefinitions as json", e);
			}
		}
		context.put("jsonFieldValues", jsonFieldValues);
		return GridUtil.generateScript(velocityEngine, context);
	}

	private static String toCamelCase(String s) {
		String[] parts = s.split(" ");
		String camelCaseString = "";
		for (String part : parts) {
			camelCaseString = camelCaseString + toProperCase(part) + " ";
		}
		return camelCaseString.trim();
	}

	private static String toProperCase(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

	/**
	 * Add needed column and it mapping for my instances
	 * 
	 * @param context
	 */
	private static void createMyInstancesGridColumnMapping(Map<String, Object> context, Locale locale, String type) {
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale);
		if (type.equalsIgnoreCase("myDocumentInstance")) {
			context.put("title", "My Document");
		} else {
			context.put("title", appResourceBundle.getString("process.myInstances"));
			context.put("needCheckbox", true);
		}

		context.put("gridId", "MY_INSTANCES");
		if (!type.equalsIgnoreCase("myDocumentInstance")) {
			context.put("needTreeStructure", true);
			context.put("dynamicGridWidth", "organizationGridWidth");
			context.put("dynamicGridHeight", "organizationGridHeight");
		}
		
		String columnNames = "['Id','流水号','流程名称','流程版本','当前任务名称','开始时间','结束时间','状态','操作','办理人','办理组','Terminate','Classification','Process Definition Id','Operation Performed','taskId','resourceName']";
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "processInstanceId", "50", "center","", "false");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "center", "_showInstanceDetail", "false");
		CommonUtil.createFieldNameList(fieldNameList, "version", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "taskName", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "startTime", "70", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "endTime", "70", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "endTime", "70", "center", "_showStatusForMyInstances", "false");
		CommonUtil.createFieldNameList(fieldNameList, "delete", "50", "center", "_deleteProcessInstance", "false");
		CommonUtil.createFieldNameList(fieldNameList, "assignee", "70", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "assignedGroup", "85", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "terminate", "55", "center", "_terminateProcessInstance", "true");
		CommonUtil.createFieldNameList(fieldNameList, "classificationId", "50", "center", "", "true");

		CommonUtil.createFieldNameList(fieldNameList, "taskId", "20", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "resourceName", "20", "center", "", "true");

		CommonUtil.createFieldNameList(fieldNameList, "processDefinitionId", "20", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "operationPerformed", "20", "center", "", "true");
		context.put("columnNames", columnNames);
		context.put("noOfRecords", "16");
		context.put("fieldNameList", fieldNameList);
	}

	/**
	 * Add needed column and it mapping for journal
	 * 
	 * @param context
	 */
	private static void createJournalGridColumnMapping(Map<String, Object> context, Locale locale) {
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale);
		context.put("title", appResourceBundle.getString("process.journal"));
		context.put("gridId", "JOURNAL");
		context.put("needTreeStructure", true);
		context.put("dynamicGridWidth", "organizationGridWidth");
		context.put("dynamicGridHeight", "organizationGridHeight");
		String columnNames = "['Id','Process 2222','Task Name','Operator','Operation Time','Operation','Classification']";
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "name", "125", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "taskName", "125", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "operator", "50", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "startTime", "80", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "actionTaken", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "classificationId", "50", "center", "", "true");
		context.put("columnNames", columnNames);
		context.put("noOfRecords", "16");
		context.put("fieldNameList", fieldNameList);
	}

	/**
	 * Add needed column and it mapping for Process definitions
	 * 
	 * @param context
	 */
	private static void createProcessDefinitionsGridColumnMapping(Map<String, Object> context, Locale locale) {
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale);
		context.put("title", appResourceBundle.getString("process.processDefinitions"));
		context.put("gridId", "PROCESS_DEFINITION");
		context.put("needTreeStructure", true);
		context.put("dynamicGridWidth", "organizationGridWidth");
		context.put("dynamicGridHeight", "organizationGridHeight");
		String columnNames = "['Id','Process Name3333','Description','Created At','Start','Classification','hasStartFormKey','isSystemDefined']";
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "description", "150", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdAt", "80", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "start", "50", "center", "_startProcessInstances", "false");
		CommonUtil.createFieldNameList(fieldNameList, "classificationId", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "hasStartFormKey", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "isSystemDefined", "100", "left", "", "true");
		context.put("columnNames", columnNames);
		context.put("noOfRecords", "16");
		context.put("fieldNameList", fieldNameList);
	}

	/**
	 * Add needed column and it mapping for process list
	 * 
	 * @param context
	 */
	private static void createProcessListGridColumnMapping(Map<String, Object> context, Locale locale) {
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale);
		context.put("title", appResourceBundle.getString("manage.listProcess"));
		context.put("gridId", "LIST_PROCESS");
		context.put("needCheckbox", true);
		context.put("needTreeStructure", true);
		context.put("dynamicGridWidth", "organizationGridWidth");
		context.put("dynamicGridHeight", "organizationGridHeight");
		String columnNames = "['Id','名称','描述','创建时间','版本号','历史版本','Deploy','Process Key','State','Action','Resource Name','DeploymentId','Browse','Classification']";
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "description", "125", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdAt", "75", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "version", "50", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "showVersion", "75", "center", "_showProcessVersions", "false");
		CommonUtil.createFieldNameList(fieldNameList, "deploy", "50", "center", "_deployProcess", "true");
		CommonUtil.createFieldNameList(fieldNameList, "key", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "suspensionState", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "suspend", "40", "center", "_suspensionProcess", "true");
		CommonUtil.createFieldNameList(fieldNameList, "resourceName", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "deploymentId", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "browse", "50", "center", "_xmlBrowseLink", "true");
		CommonUtil.createFieldNameList(fieldNameList, "classificationId", "50", "center", "", "true");
		context.put("noOfRecords", "16");
		context.put("columnNames", columnNames);
		context.put("fieldNameList", fieldNameList);
	}

	/**
	 * Add needed column and it mapping for monitor process
	 * 
	 * @param context
	 */
	private static void createMonitoredProcessGridColumnMapping(Map<String, Object> context, Locale locale, String type) {
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale);
		if (type.equalsIgnoreCase("monitorProcess")) {
			context.put("title", appResourceBundle.getString("manage.monitorProcesses"));
			context.put("gridId", "MONITOR_PROCESS");
		} else {
			context.put("title", appResourceBundle.getString("process.mymonitorProcesses"));
			context.put("gridId", "MY_MONITOR_PROCESS");
		}

		context.put("needTreeStructure", true);

		context.put("dynamicGridWidth", "organizationGridWidth");
		context.put("dynamicGridHeight", "organizationGridHeight");
		String columnNames = "['Id','流程名称','描述','创建日期','版本','状态','总实例','进行中实例','已完成实例','暂停实例','终止实例','Classification','Process Grid Type']";
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "description", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "createdAt", "70", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "version", "40", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "versionStatus", "50", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "noOfInstacnes", "70", "center", "_processInstances", "false");
		CommonUtil.createFieldNameList(fieldNameList, "activeCount", "75", "center", "", "false"); // _processInstanceGirdforSuspend
		CommonUtil.createFieldNameList(fieldNameList, "successCount", "70", "center", "", "false"); // _processInstanceGirdforActive
		CommonUtil.createFieldNameList(fieldNameList, "suspendCount", "70", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "terminatedCount", "70", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "classificationId", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "processGridType", "50", "center", "", "true");
		context.put("columnNames", columnNames);
		context.put("fieldNameList", fieldNameList);
		context.put("noOfRecords", "8");
	}

	/**
	 * 
	 * Add needed column and it mapping for process instance list
	 * 
	 * @param velocityEngine
	 * @param processInstancesMapAsList
	 * @param type
	 * @return
	 */
	public static String generateScriptForProcessInstance(VelocityEngine velocityEngine, List<Map<String, Object>> processInstancesMapAsList, String type, Locale locale) {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		ResourceBundle appResourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale);
		context.put("title", appResourceBundle.getString("process.instance"));
		context.put("gridId", "PROCESS_INSTANCE");
		context.put("needCheckbox", true);
		String columnNames = "['Id','Process Grid Type','流水号','流程名称','当前任务名称','任务办理人','草稿','描述','发起人','开始时间','结束时间','Task Id', 'Resource Name'";
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "processGridType", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "processInstanceId", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "taskName", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "operator", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "isDraft", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "description", "150", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "startUserId", "100", "center", "_showStartUser", "false");
		CommonUtil.createFieldNameList(fieldNameList, "startTime", "80", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "endTime", "80", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "taskId", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "resourceName", "50", "center", "", "true");
		
		if (type.equalsIgnoreCase("success")) {
			CommonUtil.createFieldNameList(fieldNameList, "endTime", "80", "center", "", "false");
			columnNames = columnNames + ",'End Time']";
		} else if (type.equalsIgnoreCase("deleted")) {
			CommonUtil.createFieldNameList(fieldNameList, "endTime", "80", "center", "", "false");
			CommonUtil.createFieldNameList(fieldNameList, "deleteReason", "80", "center", "", "false");
			columnNames = columnNames + ",'End Time','Deleted Reason']";
		} else if (type.equalsIgnoreCase("suspended") || type.equalsIgnoreCase("activated")) {
			if (type.equalsIgnoreCase("suspended")) {
				CommonUtil.createFieldNameList(fieldNameList, "status", "50", "center", "_activateProcessInstance", "false");
			}
			if (type.equalsIgnoreCase("activated")) {
				CommonUtil.createFieldNameList(fieldNameList, "status", "50", "center", "_suspendProcessInstance", "true");
			}
			columnNames = columnNames + ",'Action']";
		} else {
			columnNames = columnNames + "]";
		}
		context.put("noOfRecords", "16");
		context.put("columnNames", columnNames);
		context.put("fieldNameList", fieldNameList);
		String jsonFieldValues = Constants.EMPTY_STRING;
		try {
			if (processInstancesMapAsList != null && !(processInstancesMapAsList.isEmpty())) {
				jsonFieldValues = CommonUtil.getJsonString(processInstancesMapAsList);
			}
		} catch (Exception e) {
			throw new EazyBpmException("Problem parsing processInstances as json", e);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		return GridUtil.generateScript(velocityEngine, context);
	}

	/**
	 * Add needed column and it mapping for File list
	 * 
	 * @param velocityEngine
	 * @param fileListInfo
	 * @return
	 */
	public static String generateScriptForListOfFiles(VelocityEngine velocityEngine, List<Map<String, Object>> fileListInfo) {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		context.put("title", "File List");
		context.put("gridId", "FILE_LIST");
		String columnNames = "['File Name','File Size(KB)','fileObj','Export']";
		CommonUtil.createFieldNameList(fieldNameList, "fileName", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "filesize", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "fileObj", "10", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "", "30", "center", "_selectedXmlDownload", "false");
		context.put("columnNames", columnNames);
		context.put("fieldNameList", fieldNameList);
		String jsonFieldValues = Constants.EMPTY_STRING;
		try {
			if (fileListInfo != null && !(fileListInfo.isEmpty())) {
				jsonFieldValues = CommonUtil.getJsonString(fileListInfo);
			}
		} catch (Exception e) {
			throw new EazyBpmException("Problem parsing processInstances as json", e);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("gridHeight", 50);
		context.put("gridWidth", 500);
		return GridUtil.generateScript(velocityEngine, context);
	}

	/**
	 * Form the JSON Data To show organization tree structure in Process Related
	 * Grid
	 * 
	 * @param request
	 * @return
	 * @throws JSONException
	 */
	public static JSONArray getJsonTreeForProcessList(HttpServletRequest request) throws JSONException {
		List<LabelValue> classificationList = (List<LabelValue>) request.getSession().getServletContext().getAttribute(Constants.PROCESS_CLASSIFICATION);
		JSONArray jsonDataArray = new JSONArray();
		// Get the Default JSON data for organizationTree
		jsonDataArray.put(CommonUtil.defaultJSONForOrganizationTree("全部"));
		for (LabelValue classification : classificationList) {
			JSONObject treeJson = new JSONObject();
			JSONObject attributes = new JSONObject();
			JSONObject metaData = new JSONObject();
			attributes.put("id", classification.getValue());
			attributes.put("name", classification.getValue());
			attributes.put("parent", classification.getValue());
			metaData.put("id", classification.getValue());
			metaData.put("name", classification.getValue());
			treeJson.put("data", classification.getLabel());
			treeJson.put("attr", attributes);
			treeJson.put("metadata", metaData);
			jsonDataArray.put(treeJson);
		}
		return jsonDataArray;
	}

	public static JSONObject getJsonTreeForProcess(HttpServletRequest request) throws JSONException {
		List<LabelValue> classificationList = (List<LabelValue>) request.getSession().getServletContext().getAttribute(Constants.PROCESS_CLASSIFICATION);
		JSONArray jsonDataArray = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		for (LabelValue classification : classificationList) {
			JSONObject treeJson = new JSONObject();
			treeJson.put("value", classification.getValue());
			treeJson.put("name", classification.getValue());
			jsonDataArray.put(treeJson);
		}
		jsonObj.put("items", jsonDataArray);
		return jsonObj;
	}

}
