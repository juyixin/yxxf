package com.eazytec.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.velocity.app.VelocityEngine;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.eazytec.Constants;
import com.eazytec.bpm.model.LoggedUserDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.util.DateUtil;
import com.eazytec.util.PageBean;
import com.eazytec.util.PageResultBean;
import com.eazytec.util.PageResultRow;
import com.eazytec.util.PropertyReader;
import com.google.common.collect.HashMultimap;

/**
 * Hold functions commonly used for conversions, manipulations and other utility
 * functions.
 * 
 * @author Karthick
 * @author madan
 * @author revathi
 *
 */
public class CommonUtil {

	private static HashMultimap<String, Object> sessionMap;

	private static Map<String, Object> globalUserAttributes = new HashMap<String, Object>();
	private static Set<LoggedUserDTO> loggedUserSet = new HashSet<LoggedUserDTO>();

	/**
	 * Returns Json String from a list.
	 * 
	 * @param list
	 * @return
	 */
	public static String getJsonString(List<?> list) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = Constants.EMPTY_STRING;
		try {
			jsonString = mapper.writeValueAsString(list);
		} catch (JsonGenerationException e) {
			throw new EazyBpmException("Error while generating JSON for the list " + list, e);
		} catch (JsonMappingException e) {
			throw new EazyBpmException("Error while mapping JSON for the list " + list, e);
		} catch (Exception e) {
			throw new EazyBpmException("Error while getting JSON for the list " + list, e);
		}
		return jsonString;
	}

	/**
	 * Create grid column attributes
	 * 
	 * @param fieldNameList
	 * @param index
	 * @param width
	 * @param renderFun
	 * @param textAlign
	 * @param isHidden
	 */
	public static void createFieldNameList(List<Map<String, Object>> fieldNameList, String index, String width, String textAlign, String onRenderEvent, String isHidden) {
		Map<String, Object> fieldNameMap = new HashMap<String, Object>();
		fieldNameMap.put("index", index);
		fieldNameMap.put("width", width);
		fieldNameMap.put("textAlign", textAlign);
		fieldNameMap.put("isHidden", isHidden);
		fieldNameMap.put("onRenderEvent", onRenderEvent);
		fieldNameList.add(fieldNameMap);
	}

	/**
	 * Create grid column attributes with sort type
	 * 
	 * @param fieldNameList
	 * @param index
	 * @param width
	 * @param renderFun
	 * @param textAlign
	 * @param isHidden
	 */
	public static void createFieldNameList(List<Map<String, Object>> fieldNameList, String index, String width, String textAlign, String onRenderEvent, String isHidden, String sortType) {
		Map<String, Object> fieldNameMap = new HashMap<String, Object>();
		fieldNameMap.put("index", index);
		fieldNameMap.put("width", width);
		fieldNameMap.put("textAlign", textAlign);
		fieldNameMap.put("isHidden", isHidden);
		fieldNameMap.put("onRenderEvent", onRenderEvent);
		fieldNameMap.put("sorttype", sortType);
		fieldNameList.add(fieldNameMap);
	}

	/**
	 * Create grid column attributes
	 * 
	 * @param fieldNameList
	 * @param index
	 * @param width
	 * @param renderFun
	 * @param textAlign
	 * @param isHidden
	 * @param isSort
	 * @param onRenderEventName
	 */
	public static void createFieldNameList(List<Map<String, Object>> fieldNameList, String index, String width, String textAlign, String onRenderEvent, String isHidden, String isSort, String onRenderEventName, String columnType) {
		Map<String, Object> fieldNameMap = new HashMap<String, Object>();
		fieldNameMap.put("index", index);
		fieldNameMap.put("width", width);
		fieldNameMap.put("textAlign", textAlign);
		fieldNameMap.put("isHidden", isHidden);
		fieldNameMap.put("onRenderEvent", onRenderEvent);
		fieldNameMap.put("onRenderEventName", onRenderEventName);
		fieldNameMap.put("isSort", isSort);
		fieldNameMap.put("columnType", columnType);
		fieldNameList.add(fieldNameMap);
	}

	/**
	 * Create grid Header attributes
	 * 
	 * @param headerNameList
	 * @param headerId
	 * @param headerName
	 * @param headerEvent
	 */
	public static void createHeaderList(List<Map<String, Object>> headerNameList, String headerId, String headerName, String headerEvent, String iconPath, String tableName, String columnName, String redirectValue) {
		Map<String, Object> headerMap = new HashMap<String, Object>();
		headerMap.put("linkId", headerId);
		headerMap.put("linkName", headerName);
		headerMap.put("linkEvent", headerEvent);
		headerMap.put("iconPath", iconPath);
		headerMap.put("tableName", tableName);
		headerMap.put("columnName", columnName);
		headerMap.put("redirectValue", redirectValue);
		headerNameList.add(headerMap);
	}

	/**
	 * Gets the key-value pairs as string-string from a map
	 * 
	 * @param requestMap
	 *            the map which needs to be parsed
	 * @return the parsed key-value map
	 */
	public static Map<String, String> getKeyValuePairs(Map<String, Object> requestMap) {
		Map<String, String> resultMap = new HashMap<String, String>();
		Iterator<?> param = requestMap.keySet().iterator();
		while (param.hasNext()) {
			String key = (String) param.next();
			String[] value = ((String[]) requestMap.get(key));
			resultMap.put(key, value[0]);
		}
		return resultMap;
	}

	/**
	 * convert json data into list of map values
	 * 
	 * @param jsonValues
	 * @return
	 * @throws JSONException
	 */
	public static List<Map<String, Object>> convertJsonToList(String jsonValues) throws JSONException {
		JSONArray jsonArray = new JSONArray(jsonValues);
		List<Map<String, Object>> paramValues = new ArrayList<Map<String, Object>>();
		for (int j = 0; j < jsonArray.length(); j++) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			JSONObject jsonObject = jsonArray.getJSONObject(j);
			Iterator i = jsonObject.keys();
			while (i.hasNext()) {
				String key = i.next().toString();
				paramMap.put(key, jsonObject.getString(key));
			}
			paramValues.add(paramMap);
		}
		return paramValues;
	}

	/**
	 * convert json data into map values
	 * 
	 * @param jsonValues
	 * @return
	 * @throws JSONException
	 */
	public static Map<String, Object> convertJsonToMap(String jsonValues) throws JSONException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		JSONObject jsonObject = new JSONObject(jsonValues);

		Iterator i = jsonObject.keys();
		while (i.hasNext()) {
			String key = i.next().toString();
			paramMap.put(key, jsonObject.getString(key));
		}
		return paramMap;
	}

	/**
	 * convert Map To JsonObj
	 * 
	 * @param mapValues
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject convertMapToJsonObj(Map<String, Object> mapValues) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		Set<String> keySet = mapValues.keySet();
		for (String key : keySet) {
			jsonObject.put(key, mapValues.get(key));
		}
		return jsonObject;
	}

	/**
	 * convert json data into list of String values
	 * 
	 * @param jsonValues
	 * @return
	 * @throws JSONException
	 */
	public static List<String> convertJsonToListStrings(String jsonValues) throws JSONException {
		JSONArray jsonArray = new JSONArray(jsonValues);
		List<String> paramValues = new ArrayList<String>();
		for (int j = 0; j < jsonArray.length(); j++) {
			String param = "";
			param = (String) jsonArray.get(j);
			paramValues.add(param);
		}
		return paramValues;
	}

	public static String convertListStringToString(List<String> listValues) throws JSONException {
		StringBuffer dateString = new StringBuffer();
		for (String listValue : listValues) {
			dateString.append(listValue + ",");
		}

		if (dateString.lastIndexOf(",") > 0) {
			dateString.deleteCharAt(dateString.lastIndexOf(","));
		}

		return dateString.toString();
	}

	/**
	 * <p>
	 * Gets the string representation of each object value in a map represented
	 * as generic object, but the value will be string only
	 * </p>
	 * 
	 * @param requestMap
	 *            the raw map
	 * @return the string represented map
	 */
	public static Map<String, Object> getStringRepresentations(Map<String, Object> requestMap) {
		Locale locale = LocaleContextHolder.getLocale();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Iterator<?> param = requestMap.keySet().iterator();
		while (param.hasNext()) {
			String key = (String) param.next();
			Object value = requestMap.get(key);
			if (value != null) {
				if (ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale).getString("date.class").equals(value.getClass().getName())) {
					resultMap.put(key, DateUtil.convertDateToString((Date) value));
				} else {
					resultMap.put(key, value.toString());
				}
				// TO-DO: Add for other types that may need similar special
				// conversions.
			}
		}
		return resultMap;
	}

	/**
	 * Converts a list of object to a list of map after describing it.
	 * 
	 * @param objectList
	 *            list of object
	 * @return list of map
	 */
	public static List<Map<String, Object>> getMapListFromObjectList(List<?> objectList) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

		if (objectList != null) {
			for (Object object : objectList) {
				try {
					Map<String, Object> describedData = BeanUtils.describe(object);
					mapList.add(describedData);
				} catch (Exception e) {
					throw new EazyBpmException("Error while describing object for entitiy " + object.getClass().getName(), e);
				}
			}
		}
		return mapList;
	}

	/**
	 * Converts a list of object to a list of map after describing it.
	 * 
	 * @param objectList
	 *            list of object
	 * @return list of map
	 */
	public static List<Map<String, Object>> getMapListFromObjectListByFieldNames(List<?> objectList, String[] fieldNames, String moduleId) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

		if (objectList != null) {
			for (Object object : objectList) {
				try {
					Map<String, Object> describedData = new HashMap<String, Object>();
					for (String fieldName : fieldNames) {
						if (fieldName.equals("module")) {
							describedData.put(fieldName, moduleId);
						} else {
							describedData.put(fieldName, BeanUtils.getProperty(object, fieldName));
						}

					}
					mapList.add(describedData);
				} catch (Exception e) {
					throw new EazyBpmException("Error while describing object for entitiy " + object.getClass(), e);
				}
			}
		}
		return mapList;
	}

	/**
	 * Form the default JSON data for organization tree in Grid
	 * 
	 * @param displayName
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject defaultJSONForOrganizationTree(String displayName) throws JSONException {
		JSONObject allTreeJson = new JSONObject();
		JSONObject allAttributes = new JSONObject();
		JSONObject allMetaData = new JSONObject();
		allTreeJson.put("data", displayName);
		allAttributes.put("id", "all");
		allAttributes.put("name", "all");
		allAttributes.put("parent", "all");
		allMetaData.put("id", "all");
		allMetaData.put("name", "all");
		allTreeJson.put("attr", allAttributes);
		allTreeJson.put("metadata", allMetaData);
		return allTreeJson;
	}

	/**
	 * get user selection tree root elements
	 * 
	 * @param displayName
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject getOrganizationTreeNodes(List<Map<String, Object>> nodeDetails) throws JSONException {
		JSONObject root = new JSONObject();
		JSONObject attributes = new JSONObject();
		JSONObject metaDatas = new JSONObject();
		for (Map<String, Object> nodeDetail : nodeDetails) {
			Map<String, Object> attr = (Map<String, Object>) nodeDetail.get("attr");
			attributes.put("id", attr.get("id"));
			attributes.put("name", attr.get("name"));
			attributes.put("root", attr.get("root"));
			attributes.put("isRoot", attr.get("isRoot"));
			attributes.put("isUser", attr.get("isUser"));
			attributes.put("canCreate", attr.get("canCreate"));
			attributes.put("canEdit", attr.get("canEdit"));
			attributes.put("canDelete", attr.get("canDelete"));
			attributes.put("canPrint", attr.get("canPrint"));
			attributes.put("canRead", attr.get("canRead"));
			attributes.put("canDownload", attr.get("canDownload"));
			attributes.put("nodeName", attr.get("nodeName"));
			metaDatas.put("id", attr.get("id"));
			metaDatas.put("name", attr.get("name"));
			root.put("data", nodeDetail.get("data"));
			root.put("attr", attributes);
			root.put("metadata", metaDatas);
		}
		return root;
	}

	/**
	 * get JSONArray from List of LabelValue
	 * 
	 * @param nodeList
	 * 
	 * @return
	 * @throws Exception,BpmException
	 */
	public static JSONArray getNodesFromLabelValue(List<LabelValue> nodeList) throws Exception, BpmException {
		JSONArray nodes = new JSONArray();
		if (nodeList != null && nodeList.size() > 0) {
			for (LabelValue node : nodeList) {
				JSONObject treeNode = new JSONObject();
				JSONObject attributes = new JSONObject();
				JSONObject metaDatas = new JSONObject();
				attributes.put("id", node.getValue().replace(" ", ""));
				attributes.put("name", node.getValue());
				attributes.put("label", node.getLabel());
				attributes.put("nodeName", node.getLabel());
				metaDatas.put("id", node.getValue().replace(" ", ""));
				metaDatas.put("name", node.getLabel());
				treeNode.put("data", node.getLabel());
				treeNode.put("attr", attributes);
				treeNode.put("metadata", metaDatas);
				nodes.put(treeNode);
			}
		}
		return nodes;
	}

	public static List<Map<String, Object>> createListViewRootTreeNode(List<Map<String, Object>> nodeListOfMap, String name, String id, String data) throws BpmException, Exception {
		Map<String, Object> attr = new HashMap<String, Object>();
		Map<String, Object> metaData = new HashMap<String, Object>();
		Map<String, Object> nodeDetails = new HashMap<String, Object>();
		attr.put("id", id);
		attr.put("name", name);
		attr.put("parent", name);
		attr.put("nodeName", name);
		metaData.put("id", id);
		metaData.put("name", name);
		nodeDetails.put("data", data);
		nodeDetails.put("attr", attr);
		nodeDetails.put("metaData", metaData);
		nodeListOfMap.add(nodeDetails);

		return nodeListOfMap;
	}

	public static List<Map<String, Object>> createReportRootTreeNode(List<Map<String, Object>> nodeListOfMap, String name, String id, String data) throws BpmException, Exception {
		Map<String, Object> attr = new HashMap<String, Object>();
		Map<String, Object> metaData = new HashMap<String, Object>();
		Map<String, Object> nodeDetails = new HashMap<String, Object>();
		attr.put("id", id);
		attr.put("name", name);
		attr.put("parent", name);
		attr.put("nodeName", name);
		metaData.put("id", id);
		metaData.put("name", name);
		nodeDetails.put("data", data);
		nodeDetails.put("attr", attr);
		nodeDetails.put("metaData", metaData);
		nodeListOfMap.add(nodeDetails);

		return nodeListOfMap;
	}

	/**
	 * get JSONArray from List of LabelValue
	 * 
	 * @param nodeList
	 * 
	 * @return
	 * @throws Exception,BpmException
	 */
	public static JSONArray getNodesFromListOfMap(List<List<Map<String, Object>>> nodeListofMap) throws Exception, BpmException {
		JSONArray nodes = new JSONArray();
		if (nodeListofMap != null && nodeListofMap.size() > 0) {
			for (List<Map<String, Object>> nodeMapList : nodeListofMap) {
				for (Map<String, Object> nodeMap : nodeMapList) {
					JSONObject treeNode = new JSONObject();
					JSONObject attributes = new JSONObject();
					JSONObject metaDatas = new JSONObject();
					attributes.put("id", nodeMap.get("labelId"));
					attributes.put("name", nodeMap.get("label"));
					attributes.put("groupName", nodeMap.get("groupName"));
					attributes.put("groupValue", nodeMap.get("groupValue"));
					attributes.put("groupType", nodeMap.get("groupType"));
					attributes.put("sortType", nodeMap.get("sortType"));
					metaDatas.put("id", nodeMap.get("labelId"));
					metaDatas.put("name", nodeMap.get("label"));
					treeNode.put("data", nodeMap.get("label"));
					treeNode.put("attr", attributes);
					treeNode.put("metadata", metaDatas);
					nodes.put(treeNode);
				}
			}
		}
		return nodes;
	}

	/**
	 * 
	 * @param childNodeList
	 * @param root
	 * @param index
	 * @return
	 * @throws BpmException
	 * @throws Exception
	 */
	public static JSONArray formGroupSettingSelectionNodes(List<List<Map<String, Object>>> childNodeListOfMap, String root, String listViewId) throws BpmException, Exception {
		JSONArray nodes = new JSONArray();
		JSONObject jsonNodes = new JSONObject();
		Map<String, Object> attr = new HashMap<String, Object>();
		Map<String, Object> nodeDetails = new HashMap<String, Object>();
		List<Map<String, Object>> nodeListOfMap = new ArrayList<Map<String, Object>>();
		if (childNodeListOfMap != null && childNodeListOfMap.size() > 0) {
			for (List<Map<String, Object>> childNodeMapList : childNodeListOfMap) {
				for (Map<String, Object> childNodeMap : childNodeMapList) {
					attr.put("id", childNodeMap.get("labelId"));
					attr.put("name", childNodeMap.get("label"));
					attr.put("groupName", childNodeMap.get("groupName"));
					attr.put("groupValue", childNodeMap.get("groupValue"));
					attr.put("groupType", childNodeMap.get("groupType"));
					attr.put("sortType", childNodeMap.get("sortType"));
					attr.put("listViewId", listViewId);
					attr.put("root", root);
					nodeDetails.put("data", childNodeMap.get("label"));
					nodeDetails.put("attr", attr);
					nodeListOfMap.add(nodeDetails);
					jsonNodes = CommonUtil.getGridGroupSettingTreeNodes(nodeListOfMap);
					nodes.put(jsonNodes);
				}
			}
		}
		return nodes;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject getGridGroupSettingTreeNodes(List<Map<String, Object>> nodeDetails) throws JSONException {
		JSONObject root = new JSONObject();
		JSONObject attributes = new JSONObject();
		JSONObject metaDatas = new JSONObject();
		for (Map<String, Object> nodeDetail : nodeDetails) {
			Map<String, Object> attr = (Map<String, Object>) nodeDetail.get("attr");
			attributes.put("id", attr.get("id"));
			attributes.put("name", attr.get("name"));
			attributes.put("root", attr.get("root"));
			attributes.put("groupName", attr.get("groupName"));
			attributes.put("groupValue", attr.get("groupValue"));
			attributes.put("groupType", attr.get("groupType"));
			attributes.put("sortType", attr.get("sortType"));
			if (attr.containsKey("listViewId")) {
				attributes.put("listViewId", attr.get("listViewId"));
			}
			metaDatas.put("id", attr.get("id"));
			metaDatas.put("name", attr.get("name"));
			root.put("data", nodeDetail.get("data"));
			root.put("attr", attributes);
			root.put("metadata", metaDatas);
		}
		return root;
	}

	public static String getLoggedInUserId() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user != null) {
			return user.getId();
		} else {
			throw new EazyBpmException("Cannot find logged in user");
		}
	}

	public static String getLoggedInUserName() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user != null) {
			return user.getUsername();
		} else {
			throw new EazyBpmException("Cannot find logged in user");
		}
	}

	/**
	 * Will return logged in user roles
	 * 
	 * @author vinoth
	 * @return Object[]
	 */
	public static Object[] getLoggedInUserRoles() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Object[] object = {};
		if (user != null) {
			if (!user.getRoles().isEmpty() && user.getRoles().size() > 0) {
				object = user.getRoles().toArray();
				return object;
			}
		} else {
			throw new EazyBpmException("Cannot find logged in user");
		}
		return object;
	}

	/**
	 * Will return logged in user object
	 * 
	 * @author vinoth
	 * @return User
	 */
	public static User getLoggedInUser() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user != null) {
			return user;
		} else {
			throw new EazyBpmException("Cannot find logged in user");
		}
	}

	/**
	 * Will return logged in user groups
	 * 
	 * @author vinoth
	 * @return Object[]
	 */
	public static Object[] getLoggedInUserGroups() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Object[] object = {};
		if (user != null) {
			if (!user.getGroups().isEmpty() && user.getGroups().size() > 0) {
				object = user.getGroups().toArray();
				return object;
			}
		} else {
			throw new EazyBpmException("Cannot find logged in user");
		}
		return object;
	}

	/**
	 * convert a list of user object into comma separated userId's
	 * 
	 * @param users
	 * @return
	 * 
	 */
	public static String getCommaSeparatedUser(Set<User> users) {
		String usersString = "";
		if (users != null) {
			for (User user : users) {
				if (usersString.length() == 0) {
					usersString += user.getFullName();
				} else {
					usersString += "," + user.getFullName();
				}
			}
		}
		return usersString;
	}

	/**
	 * get user objects from userIds
	 * 
	 * @param users
	 * @return
	 * 
	 */
	public static Set<User> getUsersFromuserIds(String userIds) {
		Set<User> users = new HashSet<User>();
		if (userIds.contains(",")) {
			String[] ids = userIds.split(",");
			for (String userid : ids) {
				users.add(new User(userid, userid));
			}
		} else {
			users.add(new User(userIds, userIds));
		}
		return users;
	}

	/**
	 * Convert List<String> into String separated by comma with Single Code i.e.
	 * 'abcd','defg' this may be used IN Query to fetch the records
	 * 
	 * @param listOfString
	 *            List<String> to be converted
	 * @return String Converted String
	 */
	public static String getCommaSeparatedString(List<String> listOfString) {
		StringBuffer stringBuffer = new StringBuffer();
		int size = 0;
		Set<String> stringSet = new HashSet<String>(listOfString);
		for (String element : stringSet) {
			if (stringBuffer.length() > 0) {
				stringBuffer.append(",");
			}
			stringBuffer.append("'");
			stringBuffer.append(element);
			stringBuffer.append("'");
			size = size + 1;
		}
		return stringBuffer.toString();
	}

	/**
	 * Generate script for tree
	 * 
	 * @param selectionType
	 * @param appendTo
	 * @param selectedValues
	 * @param callAfter
	 * @param results
	 * @param needTreeCheckbox
	 * @param needContextMenu
	 * @param velocityEngine
	 * @return
	 * @throws BpmException
	 * @throws Exception
	 */
	public static String showTreeSelection(VelocityEngine velocityEngine, String selectionType, String appendTo, String selectedValues, String callAfter, List<LabelValue> results, String childNodeUrl, boolean needTreeCheckbox, boolean needContextMenu) throws BpmException, Exception {
		Map<String, Object> context = new HashMap<String, Object>();
		JSONArray nodes = getNodesFromLabelValue(results);
		context.put("nodes", nodes);
		context.put("selectionType", selectionType);
		context.put("selectedValues", selectedValues);
		context.put("needTreeCheckbox", needTreeCheckbox);
		context.put("needContextMenu", needContextMenu);
		context.put("childNodeUrl", childNodeUrl);
		String script = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "DynamicTree.vm", context);
		return script;
	}

	/**
	 * Generate script for menu tree
	 * 
	 * @param nodeListOfMap
	 * @param name
	 * @param id
	 * @return
	 * @throws BpmException
	 * @throws Exception
	 */
	public static List<Map<String, Object>> createMenuRootTreeNode(List<Map<String, Object>> nodeListOfMap, String name, String id, String data, String menuType) throws BpmException, Exception {
		Map<String, Object> attr = new HashMap<String, Object>();
		Map<String, Object> metaData = new HashMap<String, Object>();
		Map<String, Object> nodeDetails = new HashMap<String, Object>();
		attr.put("id", id);
		attr.put("name", data);
		attr.put("parent", name);
		attr.put("nodeName", data);
		if (menuType.equals("side")) {
			attr.put("isMenu", true);
		} else {
			attr.put("isMenu", false);
		}
		metaData.put("id", id);
		metaData.put("name", data);
		nodeDetails.put("data", data);
		nodeDetails.put("attr", attr);
		nodeDetails.put("metaData", metaData);
		nodeListOfMap.add(nodeDetails);
		return nodeListOfMap;
	}

	public static List<Map<String, Object>> createRootTreeNode(List<Map<String, Object>> nodeListOfMap, String name, String id, String data) throws BpmException, Exception {
		Map<String, Object> attr = new HashMap<String, Object>();
		Map<String, Object> metaData = new HashMap<String, Object>();
		Map<String, Object> nodeDetails = new HashMap<String, Object>();
		attr.put("id", id);
		attr.put("name", name);
		attr.put("parent", name);

		metaData.put("id", id);
		metaData.put("name", name);
		nodeDetails.put("data", data);
		nodeDetails.put("attr", attr);
		nodeDetails.put("metaData", metaData);
		nodeListOfMap.add(nodeDetails);
		return nodeListOfMap;
	}

	public static List<Map<String, Object>> createWidgetRootTreeNode(List<Map<String, Object>> nodeListOfMap, String name, String id, String data) throws BpmException, Exception {
		Map<String, Object> attr = new HashMap<String, Object>();
		Map<String, Object> metaData = new HashMap<String, Object>();
		Map<String, Object> nodeDetails = new HashMap<String, Object>();
		attr.put("id", id);
		attr.put("name", name);
		attr.put("parent", name);
		attr.put("nodeName", name);
		metaData.put("id", id);
		metaData.put("name", name);
		nodeDetails.put("data", data);
		nodeDetails.put("attr", attr);
		nodeDetails.put("metaData", metaData);
		nodeListOfMap.add(nodeDetails);
		return nodeListOfMap;
	}

	/**
	 * get the user role as string
	 * 
	 * @param user
	 * @return
	 */
	public static String getRolesAsString(User user) {
		String userRoles = "";
		if (user.getRoles() != null) {
			for (Role role : user.getRoles()) {
				if (!role.getRoleType().equalsIgnoreCase("Internal")) {
					if (userRoles.length() == 0) {
						userRoles += role.getName();
					} else {
						userRoles += "," + role.getName();
					}
				}
			}
		}
		return userRoles;
	}

	/**
	 * get the user group as string
	 * 
	 * @param user
	 * @return
	 */
	public static String getGroupsAsString(User user) {
		String userGroups = "";
		if (user.getGroups() != null) {
			for (Group group : user.getGroups()) {
				if (userGroups.length() == 0) {
					userGroups += group.getName();
				} else {
					userGroups += "," + group.getName();
				}
			}
		}
		return userGroups;
	}

	public static String generateHomePageScript(VelocityEngine velocityEngine, Map<String, Object> context) {
		String result = null;
		try {
			if (null == context.get("noOfRecords")) {
				context.put("noOfRecords", "10");
			}
			if (null == context.get("link")) {
				context.put("link", "");
			}
			result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "com/eazytec/bpm/form/templates/homePage.vm", "utf-8", context);

		} catch (Exception e) {
			throw new EazyBpmException("Problem loading Home page", e);
		}
		return result;
	}

	/**
	 * To convert the InputStream to String
	 * 
	 * @param InputStream
	 * @return
	 */
	public static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	/**
	 * Checking the user us admin or not
	 * 
	 * @param user
	 * @return
	 */
	public static boolean isUserAdmin(User user) {
		Set<Role> roles = user.getRoles();
		List<Role> roleList = new ArrayList<Role>(roles);
		for (Role role : roleList) {
			if (role.getId().equals("ROLE_ADMIN")) {
				return true;
			}
		}
		return false;
	}

	public static void setUserSessionInfo(String userId, Object session) {
		if (sessionMap == null) {
			sessionMap = HashMultimap.create();
		}
		sessionMap.put(userId, session);
	}

	public static HashMultimap<String, Object> getUserSessionInfo() {
		return sessionMap;
	}

	public static List<Map<String, Object>> createImportFilesRootTreeNode(List<Map<String, Object>> nodeListOfMap, String name, String id, String data, String resourcePath) throws BpmException, Exception {
		Map<String, Object> attr = new HashMap<String, Object>();
		Map<String, Object> metaData = new HashMap<String, Object>();
		Map<String, Object> nodeDetails = new HashMap<String, Object>();
		attr.put("id", id);
		attr.put("name", data);
		attr.put("parent", name);
		attr.put("nodeName", data);
		attr.put("resourcePath", resourcePath);
		metaData.put("id", id);
		metaData.put("name", data);
		metaData.put("resourcePath", resourcePath);
		nodeDetails.put("data", data);
		nodeDetails.put("attr", attr);
		nodeDetails.put("metaData", metaData);
		nodeListOfMap.add(nodeDetails);
		return nodeListOfMap;
	}

	/**
	 * convert map values into json data
	 * 
	 * @param jsonValues
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject convertMapToJson(Map<String, Object> mapValues) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		for (String key : mapValues.keySet()) {
			jsonObject.put(key, mapValues.get(key));
		}
		return jsonObject;
	}

	/**
	 * convert map values into json data
	 * 
	 * @param jsonValues
	 * @return
	 * @throws JSONException
	 */
	public static JSONArray convertListToJson(List<String> listValues) throws JSONException {
		JSONArray jsonArray = new JSONArray();
		for (String listValue : listValues) {
			jsonArray.put(listValue);
		}
		return jsonArray;
	}

	public static void setGlobalAttributes(Map<String, Object> globalAttributes) {
		globalUserAttributes = globalAttributes;
	}

	public static Map<String, Object> getGlobalAttributes() {
		return globalUserAttributes;
	}

	public static String getInternalMessageDomain() {
		return (String) PropertyReader.getInstance().getPropertyFromFile("String", "system.app.internalMessage.domain");
	}

	public static String getInternalMessageId(String username) {
		return username + "@" + getInternalMessageDomain();
	}

	public static String getInternalMessageNotificationAdminName() {
		return (String) PropertyReader.getInstance().getPropertyFromFile("String", "system.app.internalMessage.notification.admin");
	}

	public static String getEmailNotificationId() {
		return (String) PropertyReader.getInstance().getPropertyFromFile("String", "system.notification.email");
	}

	public static String getEmailNotificationPassword() {
		return (String) PropertyReader.getInstance().getPropertyFromFile("String", "system.notification.password");
	}

	public static String getFullNameFromEmailAddress(String emailAddress) {
		String fullNames = "";
		if (emailAddress.contains(",")) {
			String[] userIds = emailAddress.split(",");
			for (String userId : userIds) {
				if (userId.contains("<")) {
					String[] userFullName = userId.split("<");
					if (fullNames.length() > 1) {
						fullNames = fullNames + "," + userFullName[0];
					} else {
						fullNames = userFullName[0];
					}
				} else if (userId.contains("&lt;")) {
					String[] userFullName = userId.split("&lt;");
					if (fullNames.length() > 1) {
						fullNames = fullNames + "," + userFullName[0];
					} else {
						fullNames = userFullName[0];
					}
				} else {
					if (fullNames.length() > 1) {
						fullNames = fullNames + "," + userId;
					} else {
						fullNames = userId;
					}
				}
			}
		} else {
			if (emailAddress.contains("<")) {
				String[] userFullName = emailAddress.split("<");
				if (fullNames.length() > 1) {
					fullNames = fullNames + "," + userFullName[0];
				} else {
					fullNames = userFullName[0];
				}
			} else if (emailAddress.contains("&lt;")) {
				String[] userFullName = emailAddress.split("&lt;");
				if (fullNames.length() > 1) {
					fullNames = fullNames + "," + userFullName[0];
				} else {
					fullNames = userFullName[0];
				}
			} else {
				if (fullNames.length() > 1) {
					fullNames = fullNames + "," + emailAddress;
				} else {
					fullNames = emailAddress;
				}
			}
		}
		return fullNames;
	}

	public static String getUsernameFromEmailAddress(String emailAddress) {
		String usernames = "";
		if (emailAddress.contains(",")) {
			String[] userIds = emailAddress.split(",");
			for (String userId : userIds) {
				if (userId.contains("<")) {
					String[] userMail = userId.split("<");
					if (userMail[1].contains("@")) {
						String[] username = userMail[1].split("@");
						if (usernames.length() > 1) {
							usernames = usernames + "," + username[0];
						} else {
							usernames = username[0];
						}
					}
				} else if (userId.contains("&lt;")) {
					String[] userMail = userId.split("&lt;");
					if (userMail[1].contains("@")) {
						String[] username = userMail[1].split("@");
						if (usernames.length() > 1) {
							usernames = usernames + "," + username[0];
						} else {
							usernames = username[0];
						}
					}
				} else {
					if (usernames.length() > 1) {
						usernames = usernames + "," + userId;
					} else {
						usernames = userId;
					}
				}
			}
		} else {
			if (emailAddress.contains("<")) {
				String[] userMail = emailAddress.split("<");
				if (userMail[1].contains("@")) {
					String[] username = userMail[1].split("@");
					if (usernames.length() > 1) {
						usernames = usernames + "," + username[0];
					} else {
						usernames = username[0];
					}
				}
			} else if (emailAddress.contains("&lt;")) {
				String[] userMail = emailAddress.split("&lt;");
				if (userMail[1].contains("@")) {
					String[] username = userMail[1].split("@");
					if (usernames.length() > 1) {
						usernames = usernames + "," + username[0];
					} else {
						usernames = username[0];
					}
				}
			} else {
				if (usernames.length() > 1) {
					usernames = usernames + "," + emailAddress;
				} else {
					usernames = emailAddress;
				}
			}
		}
		return usernames;
	}

	/**
	 * get eazytec time zone
	 * 
	 * @return
	 */
	public static String getEazytecTimeZone() {
		return (String) PropertyReader.getInstance().getPropertyFromFile("String", "system.app.timezone.id");
	}

	public static String convertDateMapToString(Map<String, Object> dateMap) {
		Set<String> keySet = dateMap.keySet();
		StringBuffer dateString = new StringBuffer();

		for (String key : keySet) {
			dateString.append(key + " " + dateMap.get(key) + ",");

		}
		if (dateString.lastIndexOf(",") > 0) {
			dateString.deleteCharAt(dateString.lastIndexOf(","));
		}

		return dateString.toString();
	}

	public static void addLoggedUserDetail(LoggedUserDTO loggedUserDTO) {
		if (loggedUserSet == null) {
			loggedUserSet = new HashSet<LoggedUserDTO>();
		}
		loggedUserSet.add(loggedUserDTO);
	}

	public static void removeLoggedUserDetail(LoggedUserDTO loggedUserDTO) {
		if (loggedUserDTO != null) {
			loggedUserSet.remove(loggedUserDTO);
		}
	}

	public static Set<LoggedUserDTO> getLoggedUserDetail() {
		return loggedUserSet;
	}

	public static void setLoggedUserDetail(Set<LoggedUserDTO> loggedUserSets) {
		loggedUserSet.clear();
		loggedUserSet.addAll(loggedUserSets);
	}

	/**
	 * return the byte array for given input stream
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static byte[] getByteArry(InputStream input) throws IOException {
		byte[] buffer = new byte[8192];
		int bytesRead;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
		return output.toByteArray();
	}

	public static boolean isNumber(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static boolean isDate(String str) {
		try {
			DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss", str);
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}

	public static String convertDateTypeForDB(String dataBaseSchema, String dateValue) {
		String dateTypeForDB = "";
		if (dataBaseSchema.equals("oracle")) {
			dateTypeForDB = "TO_DATE('" + dateValue + "','" + Constants.NON_MYSQL_DATE_FORMAT + "')";
		} else if (dataBaseSchema.equals("mssql")) {
			dateTypeForDB = "'" + dateValue + ".0'";
		} else {
			return dateValue;
		}
		return dateTypeForDB;
	}

	public static int checkTrueOfFalse(boolean isBoolean) {
		if (isBoolean) {
			return 1;
		} else {
			return 0;
		}
	}

	public static PageResultBean convertPageBeanToPageResultBean(PageBean<?> pageBean, String[] fieldNames, Map<Object, Object> userdata) {

		PageResultBean pageResultBean = new PageResultBean();

		pageResultBean.setPage(pageBean.getCurrpage());
		pageResultBean.setRecords(pageBean.getTotalrecords());
		pageResultBean.setTotal(pageBean.getTotalpages());
		pageResultBean.setUserdata(userdata);

		List<PageResultRow> rows = new ArrayList<PageResultRow>();

		PageResultRow row = null;
		if (pageBean.getResult() != null) {
			for (Object object : pageBean.getResult()) {
				row = new PageResultRow();
				List<Object> objects = new ArrayList<Object>();
				try {
					for (String fieldName : fieldNames) {
						Object fieldValue = BeanUtils.getProperty(object, fieldName);
						if (fieldName.equals("id")) {
							row.setId(fieldValue);
						}
						objects.add(fieldValue);
					}
				} catch (Exception e) {
					throw new EazyBpmException("Error while convert PageBean To PageResultBean  for entitiy " + object.getClass(), e);
				}
				row.setCell(objects);
				rows.add(row);
			}
		}
		pageResultBean.setRows(rows);
		return pageResultBean;
	}

	public static String convertBeanToJsonString(Object object) {
		String jsoinStr = "";
		ObjectMapper mapper = new ObjectMapper();  
		try {
			jsoinStr = mapper.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return jsoinStr;
	}
}
