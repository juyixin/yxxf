package com.eazytec.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;

/**
 * 
 * @author mahesh
 *
 */
public class GridUtil {

	public static String generateScript(VelocityEngine velocityEngine, Map<String, Object> context) {
		String result = null;
		try {
			if (null == context.get("noOfRecords")) {
				context.put("noOfRecords", "10");
			}
			if (null == context.get("link")) {
				context.put("link", "");
			}
			result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "GridTemplate.vm", context);
		} catch (Exception e) {
			throw new EazyBpmException("Problem loading Grid :" + context.get("title"), e);
		}
		return result;
	}

	public static String generateScriptDy(VelocityEngine velocityEngine, Map<String, Object> context) {
		String result = null;
		try {
			if (null == context.get("noOfRecords")) {
				context.put("noOfRecords", "10");
			}
			if (null == context.get("link")) {
				context.put("link", "");
			}
			result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "GridTemplatePager.vm", "utf-8", context);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EazyBpmException("Problem loading Grid :" + context.get("title"), e);
		}
		return result;
	}

	/**
	 * To generate Dynamic script
	 * 
	 * @param velocityEngine
	 * @param context
	 * @return
	 */
	public static String generateDynamicScript(VelocityEngine velocityEngine, Map<String, Object> context) {
		String result = null;
		try {
			if (null == context.get("noOfRecords")) {
				context.put("noOfRecords", "10");
			}
			if (null == context.get("link")) {
				context.put("link", "");
			}
			result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "DynamicGridTemplate.vm", "utf-8", context);

		} catch (Exception e) {
			throw new EazyBpmException("Problem loading Grid :" + context.get("title"), e);
		}
		return result;
	}

	/**
	 * Set into context of grid column names and field names and it attributes
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForRoleGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','角色名称','显示名称','描述','类型', '排序', '上移', '下移', '创建时间']";
		context.put("title", "Roles");
		context.put("needCheckbox", true);
		/* context.put("link", "/bpm/admin/newRole"); */
		context.put("gridId", "ROLE_LIST");
		context.put("noOfRecords", "20");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "roleId", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "left", "_showEditRole", "false");
		CommonUtil.createFieldNameList(fieldNameList, "viewName", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "description", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "roleType", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "orderNo", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "orderUpImg", "100", "center", "_moveRoleGridRowUpImage", "false");
		CommonUtil.createFieldNameList(fieldNameList, "orderDownImg", "100", "center", "_moveRoleGridRowDownImage", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdTimeByString", "100", "center", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

	public static String generateScriptForUnitUnionGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','部门名称', '上级部门代码','部门代码', '备注']";
		context.put("title", "Units");
		context.put("gridId", "UNITUNION_LIST");
		/* context.put("link", "bpm/admin/departmentForm"); */
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
//		context.put("dynamicGridWidth", "unitGridWidth");
		context.put("dynamicGridHeight", "organizationGridHeight");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "bm", "30", "center", "_showEditUnitUnionMessage", "false");
		CommonUtil.createFieldNameList(fieldNameList, "sjbmdm", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "bmdm", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "bz", "30", "center", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	
	public static String generateScriptForSjCode(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','姓名','部门', '手机号码','办公电话','备注']";
		context.put("title", "Units");
		context.put("gridId", "UNITUNION_LIST");
		 context.put("link", "bpm/admin/departmentForm"); 
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
	//	context.put("dynamicGridWidth", "unitGridWidth");
		context.put("dynamicGridHeight", "organizationGridHeight");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "xm", "30", "center", "_showEditUnitUnionMessage", "false");
		CommonUtil.createFieldNameList(fieldNameList, "bm", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "dh", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "tele", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "bz", "30", "center", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	
	
	
	
	public static String generateScriptForLayoutGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','布局名称','描述','部件No','部件名称','状态']";
		context.put("title", "Layouts");
		context.put("needCheckbox", true);
		context.put("gridId", "LAYOUT_LIST");
		context.put("noOfRecords", "20");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "left", "_editLayout", "false");
		CommonUtil.createFieldNameList(fieldNameList, "description", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "noOfWidget", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "widgetNames", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "status", "100", "left", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

	public static String generateScriptForWidgetGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','部件名称','URL','是否激活']";
		context.put("title", "Widgets");
		context.put("needCheckbox", true);
		context.put("gridId", "WIDGET_LIST");
		context.put("noOfRecords", "20");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "left", "_editWidget", "false");
		CommonUtil.createFieldNameList(fieldNameList, "widgetUrl", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "isActive", "100", "left", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	public static String generateScriptForReadNoticePlatGrid(List<Map<String, Object>> list,
			VelocityEngine velocityEngine) {
		// TODO Auto-generated method stub
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','formId','标题','创建人','类型','创建年份','创建时间','接收类型','isRead']";
		context.put("title", "noticeList");
		context.put("needCheckbox", true);
		context.put("gridId", "NOTICE_LIST");
		context.put("noOfRecords", "20");
		String jsonFieldValues = "";
		if(list != null && !(list.isEmpty())){
			jsonFieldValues = CommonUtil.getJsonString(list);	
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "formId", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "title", "100", "left", "_showReadNoticeInfo", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createperson", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "dataType", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "dataYear", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "createtime", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "toperson", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "isRead", "100", "left", "", "true");
		context.put("fieldNameList", fieldNameList);		
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	
	
	
	public static String generateScriptForNoticePlatGrid(List<Map<String, Object>> list,
			VelocityEngine velocityEngine) {
		// TODO Auto-generated method stub
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','标题','创建人','创建时间','是否发布']";	
		context.put("title", "noticeList");
		context.put("needCheckbox", true);
		context.put("gridId", "NOTICE_LIST");
		context.put("noOfRecords", "20");
		String jsonFieldValues = "";
		if(list != null && !(list.isEmpty())){
			jsonFieldValues = CommonUtil.getJsonString(list);	
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "title", "100", "center", "_showNoticeInfo", "false"); 
		CommonUtil.createFieldNameList(fieldNameList, "createperson", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createtime", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "sffb", "30", "center", "", "false");
		context.put("fieldNameList", fieldNameList);		
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	
	public static String generateScriptForDjsdGrid(List<Map<String, Object>> list,
			VelocityEngine velocityEngine) {
		// TODO Auto-generated method stub
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','标题','创建人','创建时间','是否发布']";	
		context.put("title", "djsdList");
		context.put("needCheckbox", true);
		context.put("gridId", "DJSD_LIST");
		context.put("noOfRecords", "20");
		String jsonFieldValues = "";
		if(list != null && !(list.isEmpty())){
			jsonFieldValues = CommonUtil.getJsonString(list);	
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "title", "100", "center", "_showDjsdInfo", "false"); 
		CommonUtil.createFieldNameList(fieldNameList, "createperson", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createtime", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "sffb", "30", "center", "", "false");
		context.put("fieldNameList", fieldNameList);		
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	public static String generateScriptForNoticePlatGrid1(List<Map<String, Object>> list,
			VelocityEngine velocityEngine) {
		// TODO Auto-generated method stub
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','标题','创建人','创建时间','是否发布']";	
		context.put("title", "noticeList");
		context.put("needCheckbox", true);
		context.put("gridId", "NOTICE_LIST");
		context.put("noOfRecords", "20");
		String jsonFieldValues = "";
		if(list != null && !(list.isEmpty())){
			jsonFieldValues = CommonUtil.getJsonString(list);	
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "title", "100", "center", "_showNoticeInfo1", "false"); 
		CommonUtil.createFieldNameList(fieldNameList, "createperson", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createtime", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "sffb", "30", "center", "", "false");
		context.put("fieldNameList", fieldNameList);		
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	
	
	public static String generateScriptZcfgGrid(List<Map<String, Object>> list,VelocityEngine velocityEngine) {
		// TODO Auto-generated method stub
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','标题','创建人','创建时间','发布内容']";	
		context.put("title", "zcfgList");
		context.put("needCheckbox", true);
		context.put("gridId", "ZCFG_LIST");
		context.put("noOfRecords", "20");
		String jsonFieldValues = "";
		if(list != null && !(list.isEmpty())){
			jsonFieldValues = CommonUtil.getJsonString(list);	
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "title", "100", "center", "_showzcfgInfo", "false"); 
		CommonUtil.createFieldNameList(fieldNameList, "createperson", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createtime", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "content", "100", "center", "", "true");
		context.put("fieldNameList", fieldNameList);		
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	
	
	public static String generateScriptZcfgckGrid(List<Map<String, Object>> list,VelocityEngine velocityEngine) {
		// TODO Auto-generated method stub
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','标题','创建人','创建时间','发布内容']";	
		context.put("title", "zcfgList");
		context.put("needCheckbox", true);
		context.put("gridId", "ZCFG_LIST");
		context.put("noOfRecords", "20");
		String jsonFieldValues = "";
		if(list != null && !(list.isEmpty())){
			jsonFieldValues = CommonUtil.getJsonString(list);	
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "title", "100", "center", "_showzcfgckInfo", "false"); 
		CommonUtil.createFieldNameList(fieldNameList, "createperson", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createtime", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "content", "100", "center", "", "true");
		context.put("fieldNameList", fieldNameList);		
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	
	
	
	//tzgg
	public static String tzggScriptForStudyGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','标题','内容','附件','是否发布','发布时间']";
		context.put("title", "Tzggs");
		context.put("needCheckbox", true);
		context.put("gridId", "TZGG_LIST");
		context.put("noOfRecords", "20");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "BT", "100", "center", "_showGai", "false");
		CommonUtil.createFieldNameList(fieldNameList, "nr", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "fj", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "sffb", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "cjsj", "30", "center", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}
	

	public static String generateScriptForQuickNavGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','导航名称','排序','图标路径','链接','加入快捷栏']";
		context.put("title", "Quick Navigations");
		context.put("needCheckbox", true);
		context.put("gridId", "QUICKNAV_LIST");
		context.put("noOfRecords", "20");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "left", "_editQuickNav", "false");
		CommonUtil.createFieldNameList(fieldNameList, "sortOrder", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "iconPath", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "url", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "status", "100", "left", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

	/**
	 * Set into context of grid column names and field names and it attributes
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForGroupGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','分组名称','描述','负责人', '父节点', '排序', '上移', '下移', '创建时间']";
		context.put("title", "Groups");
		context.put("gridId", "GROUP_LIST");
		// context.put("link", "bpm/admin/groupForm");
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "groupId", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "viewName", "100", "center", "_showEditGroup", "false");
		CommonUtil.createFieldNameList(fieldNameList, "description", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "personIncharge", "100", "center", "_showPersonIncharge", "false");
		CommonUtil.createFieldNameList(fieldNameList, "superGroup", "100", "center", "_showSuperGroup", "false");
		CommonUtil.createFieldNameList(fieldNameList, "orderNo", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "orderUpImg", "100", "center", "_moveGroupGridRowUpImage", "false");
		CommonUtil.createFieldNameList(fieldNameList, "orderDownImg", "100", "center", "_moveGroupGridDownImage", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdTimeByString", "100", "center", "", "false");

		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

	/**
	 * Set into context of grid column names and field names and it attributes
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForDepartmentGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','部门名称','描述','部门所以者', '排序', '上移', '下移', '创建时间','部门类型']";
		context.put("title", "Departments");
		context.put("gridId", "DEPARTMENT_LIST");
		/* context.put("link", "bpm/admin/departmentForm"); */
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
		context.put("dynamicGridWidth", "organizationUserGridWidth");
		context.put("dynamicGridHeight", "organizationGridHeight");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "departmentId", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "viewName", "60", "center", "_showEditDepartment", "false");
		CommonUtil.createFieldNameList(fieldNameList, "description", "60", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "departmentOwner", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "orderNo", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "orderUpImg", "60", "center", "_moveDepartmentGridRowUpImage", "false");
		CommonUtil.createFieldNameList(fieldNameList, "orderDownImg", "60", "center", "_moveDepartmentGridDownImage", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdTimeByString", "60", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "departmentType", "100", "center", "", "true");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

	/**
	 * Set into context of grid column names and field names and it attributes
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForMenuGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','菜单名称','描述']";
		context.put("title", "Menus");
		context.put("gridId", "MENU_LIST");
		context.put("link", "bpm/admin/menuForm");
		context.put("noOfRecords", "20");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "left", "_showEditMenu", "false");
		CommonUtil.createFieldNameList(fieldNameList, "description", "100", "left", "", "false");

		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

	/**
	 * Set into context of grid column names and field names and it attributes
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForUserGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','用户名','姓名','部门','邮箱','员工号','状态']";
		context.put("title", "Users");
		context.put("needCheckbox", true);
		context.put("gridId", "USER_LIST");
		/*
		 * context.put("link", "/bpm/admin/userform"); context.put("params",
		 * "method=add");
		 */
		context.put("noOfRecords", "20");
		context.put("dynamicGridWidth", "organizationUserGridWidth");
		context.put("dynamicGridHeight", "organizationGridHeight");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "50", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "username", "100", "center", "_showEditUser", "false");
		CommonUtil.createFieldNameList(fieldNameList, "fullName", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "department", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "email", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "employeeNumber", "80", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "enabled", "50", "center", "_showEnabled", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

	/**
	 * Set into context of grid column names and field names and it attributes
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForDataDictionaryGrid(List<Map<String, Object>> dictionaryList, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','字典名称','字典编码','状态','排序','上移','下移','下级字典']";
		context.put("title", "Data Dictionary List");
		context.put("gridId", "DICTIONARY_LIST");
		context.put("needCheckbox", true);
		context.put("needTreeStructure", true);
		context.put("dynamicGridWidth", "organizationUserGridWidth");
		context.put("dynamicGridHeight", "organizationGridHeight");

		String jsonFieldValues = "";
		if (dictionaryList != null && !(dictionaryList.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(dictionaryList);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);

		CommonUtil.createFieldNameList(fieldNameList, "dictionaryId", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "name", "150", "left", "_editDataDictionary", "false");
		CommonUtil.createFieldNameList(fieldNameList, "code", "150", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "isEnabled", "100", "center", "_showIsEnabled", "false");
		CommonUtil.createFieldNameList(fieldNameList, "orderNo", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "orderUpImg", "100", "center", "_showMoveUpImage", "false");
		CommonUtil.createFieldNameList(fieldNameList, "orderDownImg", "100", "center", "_showMoveDownImage", "false");
		CommonUtil.createFieldNameList(fieldNameList, "interiorDictionary", "100", "center", "_showInteriorDictionaries", "false");

		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

	/**
	 * Set into context of grid column names and field names and it attributes
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForScheduleGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','事件名称','描述','类型 ','位置','开始日期','结束日期','开始时间','结束时间','指定的','创建时间']";
		context.put("title", "Schedule");
		context.put("gridId", "SCHEDULE_LIST");
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "eventName", "100", "left", "_showEditSchedule", "false");
		CommonUtil.createFieldNameList(fieldNameList, "description", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "eventType", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "location", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "startDate", "100", "center", "_changeStartDateFormat", "false");
		CommonUtil.createFieldNameList(fieldNameList, "endDate", "100", "center", "_changeEndDateFormat", "false");
		CommonUtil.createFieldNameList(fieldNameList, "startTime", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "endTime", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "assignedUser", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdBy", "100", "center", "", "true");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

	/**
	 * Set into context of grid column names and field names and it attributes
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForDocumentFormGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','文档表单名称','创建人','创建时间']";
		context.put("title", "Documents");
		context.put("needCheckbox", false);
		/* context.put("link", "/bpm/dms/documentForm"); */
		context.put("gridId", "DOCUMENTFORM_LIST");
		context.put("noOfRecords", "20");
		context.put("noOfRecords", "20");
		context.put("dynamicGridWidth", "documentGridWidth");
		context.put("dynamicGridHeight", "documentGridHeight");
		context.put("needTreeStructure", true);
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "25", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "name", "25", "left", "_showViewDocumentForm", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdBy", "25", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createdTimeByString", "25", "left", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

	/**
	 * Set into context of grid column names and field names and it attributes
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForDMSUserPermissionGrid(String gridType, List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "";
		if (gridType.equalsIgnoreCase("document")) {
			columnNames = "['Id','名称','编辑','删除','打印', '下载']";
		} else {
			columnNames = "['Id','名称','写入','删除','打印','下载','阅读']";
		}

		context.put("title", "User Permissions");
		context.put("gridId", "USER_PERMISSION_LIST");
		/* context.put("link", "bpm/admin/departmentForm"); */
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
		context.put("dynamicGridWidth", "userPermissionGridWidth");
		context.put("dynamicGridHeight", "userPermissionGridHeight");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "userName", "100", "left", "_editDmsUserPermission", "false");
		CommonUtil.createFieldNameList(fieldNameList, "canEdit", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "canDelete", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "canPrint", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "canDownload", "100", "center", "", "false");
		if (gridType.equalsIgnoreCase("folder")) {
			CommonUtil.createFieldNameList(fieldNameList, "canRead", "100", "center", "", "false");
		}
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

	/**
	 * Set into context of grid column names and field names and it attributes
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForDMSRolePermissionGrid(String gridType, List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "";
		if (gridType.equalsIgnoreCase("document")) {
			columnNames = "['Id','Name','User Name','Edit','Delete','Print', 'Download','Read']";
		} else {
			columnNames = "['Id','Name','User Name','Write','Delete','Print','Download','Read']";
		}
		context.put("title", "Permissions");
		context.put("gridId", "ROLE_PERMISSION_LIST");
		/* context.put("link", "bpm/admin/departmentForm"); */
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
		context.put("dynamicGridWidth", "userPermissionGridWidth");
		context.put("dynamicGridHeight", "userPermissionGridHeight");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "left", "_editDmsRolePermission", "true");
		CommonUtil.createFieldNameList(fieldNameList, "userFullName", "100", "left", "_editDmsRolePermission", "false");
		CommonUtil.createFieldNameList(fieldNameList, "canEdit", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "canDelete", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "canPrint", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "canDownload", "100", "center", "", "false");
		// if(gridType.equalsIgnoreCase("folder")) {
		CommonUtil.createFieldNameList(fieldNameList, "canRead", "100", "center", "", "false");
		// }
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

	/**
	 * Set into context of grid column names and field names and it attributes
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForDMSGroupPermissionGrid(String gridType, List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "";
		if (gridType.equalsIgnoreCase("document")) {
			columnNames = "['Id','名称','编辑','删除','打印', '下载']";
		} else {
			columnNames = "['Id','名称','写入','删除','打印','下载','阅读']";
		}
		context.put("title", "Group Permissions");
		context.put("gridId", "GROUP_PERMISSION_LIST");
		/* context.put("link", "bpm/admin/departmentForm"); */
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
		context.put("dynamicGridWidth", "userPermissionGridWidth");
		context.put("dynamicGridHeight", "userPermissionGridHeight");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "roleName", "100", "left", "_editDmsGroupPermission", "false");
		CommonUtil.createFieldNameList(fieldNameList, "canEdit", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "canDelete", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "canPrint", "100", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "canDownload", "100", "center", "", "false");
		if (gridType.equalsIgnoreCase("folder")) {
			CommonUtil.createFieldNameList(fieldNameList, "canRead", "100", "center", "", "false");
		}
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

	public static String generateScriptForSysConfigGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','键','描述 ','类型','值','系统界定']";
		context.put("title", "系统配置");
		context.put("needHeader", true);
		context.put("needCheckbox", true);
		context.put("gridId", "SYSCONFIG_LIST");
		/*
		 * context.put("link", "/bpm/admin/userform"); context.put("params",
		 * "method=add");
		 */
		context.put("noOfRecords", "20");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "50", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "selectKey", "100", "left", "_showEditSysConfig", "false");
		CommonUtil.createFieldNameList(fieldNameList, "description", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "selectType", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "selectValue", "100", "left", "_showSysconfigValue", "false");
		CommonUtil.createFieldNameList(fieldNameList, "systemDefined", "100", "left", "", "true");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

	public static String generateScriptForTimingTaskGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['任务名称','工作名称','描述','下一个解除时间','上一个解除时间','重复间隔','触发器名称']";
		context.put("title", "TimingTask");
		context.put("needCheckbox", true);
		/* context.put("link", "/bpm/admin/newRole"); */
		context.put("gridId", "TIMINGTASK_LIST");
		context.put("noOfRecords", "20");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);

		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "left", "_showEditTrigger", "false");
		CommonUtil.createFieldNameList(fieldNameList, "jobName", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "description", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "nextFireTime", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "previousFireTime", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "intervel", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "triggerName", "100", "left", "", "true");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

	public static String generateScriptForQuartzJobGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id ','触发器名称',' 开始日期 ','结束日期']";
		context.put("title", "QuartzJob");
		context.put("needCheckbox", true);
		/* context.put("link", "/bpm/admin/newRole"); */
		context.put("gridId", "QUARTZJOB_LIST");
		context.put("noOfRecords", "20");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);

		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "triggerName", "100", "left", "_showEditQuartzJob", "false");
		CommonUtil.createFieldNameList(fieldNameList, "startDate", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "endDate", "100", "left", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

	public static String generateScriptForSystemLogGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['id','名称','类型','保存周期','状态']";
		context.put("title", "SystemLog");
		context.put("needCheckbox", true);
		context.put("gridId", "SYSTEMLOG_LIST");
		/*
		 * context.put("link", "/bpm/admin/userform"); context.put("params",
		 * "method=add");
		 */
		context.put("noOfRecords", "20");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "50", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "name", "50", "left", "_showEditSystemLog", "false");
		CommonUtil.createFieldNameList(fieldNameList, "logType", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "saveCycle", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "status", "100", "left", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

	/* For Report */
	public static String generateScriptForReportGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['id','Report Name','Report Url','Classification','Roles','Description','Status','groups','users','departments']";
		context.put("title", "Report");
		context.put("needCheckbox", true);
		context.put("gridId", "REPORT_LIST");
		/*
		 * context.put("link", "/bpm/report/"); context.put("params",
		 * "method=add");
		 */
		context.put("noOfRecords", "20");
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "100", "left", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "name", "100", "left", "_showEditReportForm", "false");
		CommonUtil.createFieldNameList(fieldNameList, "reportUrl", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "classification", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "roles", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "users", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "groups", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "departments", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "status", "100", "left", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "description", "100", "left", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	
	/**
	 * Set into context of grid column names and field names and it attributes
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForLinkGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','标题','地址', '创建时间', '状态']";
		context.put("title", "Links");
		context.put("gridId", "LINK_LIST");
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "title", "30", "center", "showDetail", "false");
		CommonUtil.createFieldNameList(fieldNameList, "url", "60", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createDate", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "isActive", "20", "center", "_showColor", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	
	/**
	 * Set into context of grid column names and field names and it attributes
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForPetitionLetterPersonGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','姓名','性别','电话','身份证','地址','创建时间']";
		context.put("title", "PetitionLetterPersons");
		context.put("gridId", "PERSON_LIST");
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "xm", "30", "center", "showPersonDetail", "false");
		CommonUtil.createFieldNameList(fieldNameList, "xb", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "dh", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "sfzhm", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "address", "60", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createTime", "30", "center", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	
	
	/**
	 * Set into context of grid column names and field names and it attributes
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForPetitionLetterEventGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','事件名称','事件内容','备注','创建时间']";
		context.put("title", "PetitionLetterEvents");
		context.put("gridId", "EVENT_LIST");
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "eventName", "30", "center", "showEventDetail", "false");
		CommonUtil.createFieldNameList(fieldNameList, "eventDetail", "30", "center", "showLongDetail", "false");
		CommonUtil.createFieldNameList(fieldNameList, "bz", "30", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createTime", "30", "center", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	
	
	/**
	 * Set into context of grid column names and field names and it attributes
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String generateScriptForEvidenceGrid(List<Map<String, Object>> list, VelocityEngine velocityEngine) throws BpmException {
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> fieldNameList = new ArrayList<Map<String, Object>>();
		String columnNames = "['Id','事件名称','当事人姓名', '处理人姓名', '处理人联系方式', '创建时间']";
		context.put("title", "Evidences");
		context.put("gridId", "EVIDENCE_LIST");
		context.put("noOfRecords", "20");
		context.put("needCheckbox", true);
		String jsonFieldValues = "";
		if (list != null && !(list.isEmpty())) {
			jsonFieldValues = CommonUtil.getJsonString(list);
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		CommonUtil.createFieldNameList(fieldNameList, "id", "50", "center", "", "true");
		CommonUtil.createFieldNameList(fieldNameList, "eventName", "30", "center", "showEvidenceDetail", "false");
		CommonUtil.createFieldNameList(fieldNameList, "concernName", "20", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "handlerName", "20", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "handlerPhone", "20", "center", "", "false");
		CommonUtil.createFieldNameList(fieldNameList, "createDate", "20", "center", "", "false");
		context.put("fieldNameList", fieldNameList);
		return GridUtil.generateScript(velocityEngine, context);
	}

}
