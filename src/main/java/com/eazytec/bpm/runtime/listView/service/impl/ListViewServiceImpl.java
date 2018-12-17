package com.eazytec.bpm.runtime.listView.service.impl;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

import org.activiti.engine.impl.util.ClockUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.module.service.ModuleService;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.bpm.runtime.listView.dao.ListViewDao;
import com.eazytec.bpm.runtime.listView.service.ListViewService;
import com.eazytec.bpm.runtime.table.dao.TableDao;
import com.eazytec.bpm.runtime.table.service.TableService;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.common.util.HashMapComparator;
import com.eazytec.common.util.ListViewColumnComparator;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;
import com.eazytec.model.ListView;
import com.eazytec.model.ListViewButtons;
import com.eazytec.model.ListViewColumns;
import com.eazytec.model.ListViewGroupSetting;
import com.eazytec.model.Module;
import com.eazytec.model.Role;
import com.eazytec.model.User;
import com.eazytec.util.DateUtil;

import dtw.webmail.model.JwmaFolderImpl;
import dtw.webmail.model.JwmaMessageInfo;

@Service("listViewService")
public class ListViewServiceImpl implements  ListViewService{
	
	public ListViewDao listViewDao;
	public TableDao tableDao;
	public TableService tableService;
    public VelocityEngine velocityEngine;
    public ModuleService moduleService;
	public String dataBaseSchema;
    
	public String getDataBaseSchema() {
		return dataBaseSchema;
	}

	public void setDataBaseSchema(String dataBaseSchema) {
		this.dataBaseSchema = dataBaseSchema;
	}

	/**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    
    @Autowired
    public void setTableService(TableService tableService) {
	    this.tableService = tableService;
    }
	
	@Autowired
    public void setListViewDao(ListViewDao listViewDao) {
	    this.listViewDao = listViewDao;
    }
	
	@Autowired
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

	@Autowired
    public void setTableDao(TableDao tableDao) {
	    this.tableDao = tableDao;
    }
	
	@Autowired
	public void setModuleService(ModuleService moduleService) {
		this.moduleService = moduleService;
	}
	
	/**
	 * To create a grid view by the give list View Name
	 * @param listViewName
	 * @param title
	 * @param viewObj
	 * @return
	 * @throws EazyBpmException
	 */
	public String showListViewGrid(String listViewName,String title,String container,Map<String,Object> listViewParamsList,boolean isGrouping,List<Map<String, Object>> mapViewColumnProperty, boolean needHeader)throws Exception, EazyBpmException {
		ListView listView=listViewDao.getListViewDetails(listViewName);
		Map<String, Object> context = new HashMap<String, Object>();
		if(listView!=null){
			
			if(StringUtils.isBlank(title)){
				title = listView.getViewTitle();
			}
			context.put("title", title);			
			context.put("needHeader", needHeader);
			context.put("gridId", listViewName);
			context.put("listViewId", listView.getId());
			context.put("isShowSearchBox", listView.getIsShowSearchBox());
			context.put("isNeedCheckbox", listView.getIsNeedCheckbox());
			context.put("onRenderScriptName", listView.getOnRenderScriptName());
			context.put("onRenderScript", listView.getOnRenderScript());
			context.put("isExportNeed", true);		
			context.put("isFormGrid", false);
			if(listViewParamsList!=null && !listViewParamsList.isEmpty()){
				String listViewParams="[";
				Set<String> paramsKeys=listViewParamsList.keySet();
				for(String paramsKey:paramsKeys){
					if(!paramsKey.equals("fromDivHeight") && !paramsKey.equals("fromDivWidth")){
						listViewParams=listViewParams+"{'"+paramsKey+"':'"+listViewParamsList.get(paramsKey)+"'}";
					}
				}
				listViewParams=listViewParams+"]";
				context.put("listViewParamsList", listViewParams);
			}else{
				context.put("listViewParamsList", "[{}]");
			}
			
			Set<ListViewColumns> columnNameSet=listView.getListViewColumns();
			if(!columnNameSet.isEmpty()){
				//list of map obj
				List<Map<String, Object>> viewColumnsList = getShortedViewColumnsList(columnNameSet);
				
				List<String> colunmnList=new ArrayList<String>();
				List<String> colunmnTitle=new ArrayList<String>();
				for(Map<String, Object> viewColumns:viewColumnsList){
					colunmnTitle.add((String)viewColumns.get("columnTitle"));
					if(!StringUtil.isEmptyString(viewColumns.get("dataFields"))){
						colunmnList.add(String.valueOf(viewColumns.get("columnTitle")).replaceAll(" ", "_"));
					}
				}
				List<Map<String,Object>>  listViewDataList=null;
				if(isGrouping){
					listViewDataList=mapViewColumnProperty;
				}else{
					String whereParams="";
					if(!listView.getListViewButtons().isEmpty()){
						Set<ListViewButtons>  listViewButtonSet=listView.getListViewButtons();
						for(ListViewButtons listViewButton:listViewButtonSet){
							if(listViewButton.getDisplayName().equals("Delete") && listViewButton.isActive() && listViewButton.getTableName() != null && listViewButton.getColumnName() != null && !((String)listViewButton.getTableName()).equalsIgnoreCase("null") &&
									!((String)listViewButton.getColumnName()).equalsIgnoreCase("null") && !listViewButton.getTableName().isEmpty() &&  !listViewButton.getColumnName().isEmpty()) {
								whereParams=listViewButton.getTableName()+".is_delete=0";
							}
						}
					}
					if(listViewParamsList!=null && !listViewParamsList.isEmpty()){
						listViewParamsList.put("needHeader", needHeader);
					}else{
						listViewParamsList=new HashMap<String, Object>();
						listViewParamsList.put("needHeader", needHeader);
					}
					
					listViewDataList=getAllListViewMapData(listView,colunmnList,viewColumnsList, whereParams, "asc", listView.getOrderBy(),listViewParamsList);
				}
				
				String jsonFieldValues = "";
				if(listViewDataList != null && !(listViewDataList.isEmpty())){
					jsonFieldValues = CommonUtil.getJsonString(listViewDataList);	
				}
				
				//addValueForGroupByFilter(listViewDataList, viewColumnsList, context);
				
				context.put("dynamicGridWidth", "getGridWidthWithSearchBox");
				context.put("dynamicGridHeight", "getGridHeightWithSearchBox");
				if(!container.equalsIgnoreCase("rightPanel")){
					constructJsonForGroupSettingTree(listView, context,listViewParamsList);
				}
				

				if(container.contains("_SubContainer")){
					if(context.get("isGroupSetting") != null && context.get("isGroupSetting").toString() == "true"){
						context.put("dynamicGridHeight", "getGridHeightForHomePageWithTree");
					}else {
						context.put("dynamicGridHeight", "getGridHeightForHomePage");
					}
					context.put("widgetContainer", container);
					context.put("dynamicGridWidth", "getGridWidthForHomePage");
				}
				if((listViewName.equals("PROCESS_LIST") || listViewName.equals("PROCESS_LIST_VERSION") || listViewName.equals("PROCESS_LIST_MY_INSTANCES") || listViewName.equals("AGENCY_LIST")) && !container.contains("_SubContainer")){
					context.put("dynamicGridHeight", "getGridHeightWithTreeAndSearchBox");
					context.put("needTreeStructure", true);
				}
				
				if(container.contains("_formgrid")){
					context.put("widgetContainer", container);
					context.put("isDynamicFromGridHeight", true);
					/*String divHeightString=String.valueOf(listViewParamsList.get("fromDivHeight"));
					int divHeight=Integer.parseInt(divHeightString)-100;*/
					context.put("dynamicFromGridHeight", listViewParamsList.get("fromDivHeight"));
					context.put("dynamicFromGridWidth", listViewParamsList.get("fromDivWidth"));
					context.put("isFormGrid",true);
				}
				
				
				context.put("columnNames", CommonUtil.getJsonString(colunmnTitle));	
				context.put("jsonFieldValues", jsonFieldValues);
				
				
				List<Map<String, Object>> columnsPropertyList = new ArrayList<Map<String, Object>>();
				
				int isAdvanceSearchCount=0;
				for(Map<String, Object> listViewColumns:viewColumnsList){
					CommonUtil.createFieldNameList(columnsPropertyList, String.valueOf(listViewColumns.get("columnTitle")).replaceAll(" ", "_"), (String)listViewColumns.get("width"), (String)listViewColumns.get("textAlign"), (String)listViewColumns.get("onRenderEvent"), listViewColumns.get("isHidden").toString(),listViewColumns.get("isSort").toString(),(String)listViewColumns.get("onRenderEventName"),(String)listViewColumns.get("columnType"));
					if(listViewColumns.get("isAdvancedSearch")!=null){
						if((Boolean)listViewColumns.get("isAdvancedSearch")){
							isAdvanceSearchCount++;
						}
					}
				}
				
				boolean isAdvanceSearchNeeded=false;
				if(isAdvanceSearchCount>0){
					isAdvanceSearchNeeded=true;
				}
				
				context.put("noOfRecords", listView.getPageSize());
				context.put("isAdvanceSearchNeeded", isAdvanceSearchNeeded);
			
				//For grid header Link
				Set<ListViewButtons> buttonSet=listView.getListViewButtons();
				//Check privilege and Shorting the button by order  
				List<Map<String, Object>> viewButtonList = getShortedViewButtonsList(buttonSet);
				List<Map<String, Object>> headerLinkList= new ArrayList<Map<String, Object>>();
				
				for(Map<String, Object> listViewButton:viewButtonList){
					String columnTitle = "";
					for(Map<String, Object> viewColumns:viewColumnsList){
						if(String.valueOf(viewColumns.get("dataFields")).equals(String.valueOf(listViewButton.get("columnName")))){
							columnTitle = String.valueOf(viewColumns.get("columnTitle")).replaceAll(" ", "_");
							break;
						}
					}
					String columnName = "";
					if(!String.valueOf(listViewButton.get("columnName")).equals("")){
						columnName = String.valueOf(listViewButton.get("columnName"))+","+columnTitle;
					}
					if(String.valueOf(listViewButton.get("active")).equals("true")){
						CommonUtil.createHeaderList(headerLinkList, (String)listViewButton.get("id"), (String)listViewButton.get("displayName"), (String)listViewButton.get("buttonMethod"), (String)listViewButton.get("iconPath"), (String)listViewButton.get("tableName"), columnName, (String)listViewButton.get("redirectValue"));
					}
				}
	
				context.put("headerLink", headerLinkList);
				context.put("fieldNameList", columnsPropertyList);
				context.put("container", container);
				return GridUtil.generateDynamicScript(velocityEngine, context);
			}else{
				return "";
			}
		}else{
			throw new EazyBpmException("There no List view in the name :: "+listViewName);
		}
	}
	
	/**
	 * Construct the json for group setting tree
	 * @param listView
	 * @param context
	 * @throws Exception
	 * @throws EazyBpmException
	 */
	private void constructJsonForGroupSettingTree(ListView listView, Map<String, Object> context,Map<String,Object> listViewParamsList) throws Exception, EazyBpmException{
		List<List<Map<String, Object>>> listOfGroupSettingsList = new ArrayList<List<Map<String, Object>>>();
		List<Map<String, Object>> listOfGroupSettings = null;
		Set<ListViewGroupSetting> listViewGroupSettings = listView.getListViewGroupSetting();
		List<Map<String, Object>> listViewGroupSettingList = getShortedViewGroupSettingList(listViewGroupSettings);
		JSONArray nodes = new JSONArray();
		if(listViewGroupSettingList != null && !(listViewGroupSettingList.isEmpty())){
			for(Map<String, Object> listViewGroupSetting : listViewGroupSettingList){
				if(String.valueOf(listViewGroupSetting.get("groupType")).equals("Single")  || 
						(listViewGroupSetting.get("parentGroup") == null || String.valueOf(listViewGroupSetting.get("parentGroup")).isEmpty())){
					listOfGroupSettings = new ArrayList<Map<String, Object>>();
					
					String queryString = constructQueryGroupSetting(listView, listViewGroupSetting, null, null,listViewParamsList);
					//log.info("\n\n\n constructed query ========= :"+queryString);
					List listViewObjArray=listViewDao.getListViewDataMap(queryString, null);
					listOfGroupSettings = formGroupSettingListOfMap(listViewObjArray, listViewGroupSetting);
					listOfGroupSettingsList.add(listOfGroupSettings);
				}
			}
			nodes = CommonUtil.getNodesFromListOfMap(listOfGroupSettingsList);
			context.put("nodes", nodes);
			context.put("isGroupSetting", true);
			context.put("needTreeStructure", true);
			context.put("dynamicGridHeight", "getGridHeightWithTreeAndSearchBox");
		}
	}
	
	/**
	 * Get JSONARRAY for group by setting of list view
	 */
	public JSONArray constructJsonListViewGroup(ListView listView) throws Exception, EazyBpmException{
		List<List<Map<String, Object>>> listOfGroupSettingsList = new ArrayList<List<Map<String, Object>>>();
		List<Map<String, Object>> listOfGroupSettings = null;
		Set<ListViewGroupSetting> listViewGroupSettings = listView.getListViewGroupSetting();
		List<Map<String, Object>> listViewGroupSettingList = getShortedViewGroupSettingList(listViewGroupSettings);
		JSONArray nodes = new JSONArray();
		if(listViewGroupSettingList != null && !(listViewGroupSettingList.isEmpty())){
			for(Map<String, Object> listViewGroupSetting : listViewGroupSettingList){
				if(String.valueOf(listViewGroupSetting.get("groupType")).equals("Single")  || 
						(listViewGroupSetting.get("parentGroup") == null || String.valueOf(listViewGroupSetting.get("parentGroup")).isEmpty())){
					listOfGroupSettings = new ArrayList<Map<String, Object>>();
					String queryString = constructQueryGroupSetting(listView, listViewGroupSetting, null, null,null);
					//log.info("\n\n\n constructed query ========= :"+queryString);
					List<String> listViewObjArray=listViewDao.getListViewDataMap(queryString, null);
					listOfGroupSettings = formGroupSettingListOfMap(listViewObjArray, listViewGroupSetting);
					listOfGroupSettingsList.add(listOfGroupSettings);
				}
			}
			nodes = CommonUtil.getNodesFromListOfMap(listOfGroupSettingsList);
		}
		return nodes;
	}
	/**
	 * {@inheritDoc getChildNodesForTree}
	 */
	public JSONArray getChildNodesForTree(String listViewId, String root, String parentGroup, List<String> groupByFields, List<Map<String,Object>> whereParams) throws Exception, EazyBpmException{
		List<List<Map<String, Object>>> listOfGroupSettingsList = new ArrayList<List<Map<String, Object>>>();
		List<Map<String, Object>> listOfGroupSettings = null;
		JSONArray nodes = new JSONArray();
		ListView listView = listViewDao.getListViewById(listViewId);
		Set<ListViewGroupSetting> listViewGroupSettings = listView.getListViewGroupSetting();
		List<Map<String, Object>> listViewGroupSettingList = getShortedViewGroupSettingList(listViewGroupSettings);
		if(listViewGroupSettingList != null && !(listViewGroupSettingList.isEmpty())){
			for(Map<String, Object> listViewGroupSetting : listViewGroupSettingList){
				if((listViewGroupSetting.get("parentGroup") != null && String.valueOf(listViewGroupSetting.get("parentGroup")).equals(parentGroup))){
					listOfGroupSettings = new ArrayList<Map<String, Object>>();
					String queryString = constructQueryGroupSetting(listView, listViewGroupSetting, groupByFields, whereParams,null);
					//log.info("\n\n\n constructed query for group setting ========= :"+queryString);
					List<String> listViewObjArray=listViewDao.getListViewDataMap(queryString, null);
					listOfGroupSettings = formGroupSettingListOfMap(listViewObjArray, listViewGroupSetting);
					listOfGroupSettingsList.add(listOfGroupSettings);
				}
			}
			nodes = CommonUtil.formGroupSettingSelectionNodes(listOfGroupSettingsList, root,listViewId);
		}
		return nodes;
	}
	
	/**
	 * Make the Group setting as list of map 
	 * @param listViewObjArray
	 * @param listViewGroupSetting
	 * @return
	 */
	private List<Map<String, Object>> formGroupSettingListOfMap(List<String> listViewObjArray, Map<String, Object> listViewGroupSetting){
		List<Map<String, Object>> listOfGroupSettings = new ArrayList<Map<String, Object>>();
		Map<String, Object> listViewObjMap =  null;
		for(String listViewObj  : listViewObjArray){
			listViewObjMap = new HashMap<String, Object>();
			listViewObjMap.put("label", listViewObj);
			String lblId = String.valueOf(listViewObj).replace(" ", "").replace("(", "").replace(")", "");
			String uniqueStr = String.valueOf(UUID.randomUUID());
			String lblIdWithUniqueUUID = lblId+"_"+uniqueStr; 
			listViewObjMap.put("labelId", lblIdWithUniqueUUID);
			listViewObjMap.put("groupName", listViewGroupSetting.get("groupFieldsName"));
			String listViewString = listViewObj.replace(" ", "");
			int endIndex  = listViewString.indexOf("(");
			listViewObjMap.put("groupValue", listViewString.substring(0, endIndex));
			listViewObjMap.put("groupType", listViewGroupSetting.get("groupType"));
			listViewObjMap.put("sortType", listViewGroupSetting.get("sortType"));
			listViewObjMap.put("orderBy", listViewGroupSetting.get("orderBy"));
			listOfGroupSettings.add(listViewObjMap);
		}
		Collections.sort(listOfGroupSettings, new HashMapComparator());
		return listOfGroupSettings;
	}
	public String replaceUserDetails(String queryString) {
		User user = CommonUtil.getLoggedInUser();
		if(queryString.indexOf(":loggedInUser") > -1){
			String userName = "'"+user.getUsername()+"'";
			queryString=queryString.replaceAll(":loggedInUser", userName);
		}
		
		if(queryString.indexOf(":userRole") > -1){
     		   String userRoles = "'"+user.getRoles()+"'";
     		   String userRoleArray[] = {};
     		  if(userRoles.contains("[")){
      			   userRoles = userRoles.replace("'[", "");
      			   userRoles = userRoles.replace("]'","");
         		   }
         		   if(userRoles.contains(" ")){
         			  userRoles = userRoles.replace(" ","");
         		   }
     		   if(userRoles.contains(",")){
     			   userRoleArray = userRoles.split(",");
     		   }
     		   userRoles = "(";
     		   for (int i=0;i<userRoleArray.length;i++){
     			   if(i == userRoleArray.length-1){
     				   userRoles = userRoles+"'"+userRoleArray[i]+"'"+")";
     			   }else{
     				   userRoles = userRoles+"'"+userRoleArray[i]+"'"+",";
     			   }
     		   }
     		  queryString = queryString.replaceAll(":userRole", userRoles);
 		   } 
		   if(queryString.indexOf(":userGroup") > -1){
       		   String userGroups = "'"+user.getGroups()+"'";
       		   String userGroupArray[] = {};
       		   if(userGroups.contains("[")) {
       			   userGroups = userGroups.replace("'[", "");
       			   userGroups = userGroups.replace("]'","");
	         		  }
	         		   if(userGroups.contains(" ")){
	         			userGroups = userGroups.replace(" ","");
	         		   }
       		   if(userGroups.contains(",")){
       			 userGroupArray = userGroups.split(",");
       			 userGroups = "(";
         		   for (int i=0;i<userGroupArray.length;i++){
         			   if(i == userGroupArray.length-1){
         				   userGroups = userGroups+"'"+userGroupArray[i]+"'"+")";
         			   }else{
         				   userGroups =userGroups+"'"+userGroupArray[i]+"'"+",";
         			   }
         		   }
       		   } else{
       			userGroups = "('"+userGroups+"')";
       		   }
       		  
       		  queryString = queryString.replaceAll(":userGroup", userGroups);
   		   } 
		   if(queryString.indexOf(":userDepartment") >-1) {
        		   String userDepartment = "('"+user.getDepartment()+"')";
        		   queryString = queryString.replaceAll(":userDepartment", userDepartment);
   			   }
		return queryString;
	}
	/**
	 * Construct the query to get the list of data's
	 * @param listView
	 * @param viewColumnsList
	 * @return String
	 * @throws EazyBpmException
	 */
	private String constructQueryGroupSetting(ListView listView,Map<String, Object> viewColumnsList, List<String> groupByFields, List<Map<String,Object>> whereParamsListOfMap,Map<String,Object> listViewParams)throws EazyBpmException{
		StringBuffer strbuf = new StringBuffer();
		String whereParams = "";
		String fromQueryString="";
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int count = 1;
		if(whereParamsListOfMap != null && !whereParamsListOfMap.isEmpty()){
			Map<String,Object> searchParamMap = whereParamsListOfMap.get(0);
			for(String key : searchParamMap.keySet()){
				String whereParamsValue = String.valueOf(searchParamMap.get(key));
				if(whereParamsValue != null && !(whereParamsValue.isEmpty())){
					whereParams = whereParams + key+" = '"+searchParamMap.get(key)+"'";
					if(searchParamMap.size() > count){
						whereParams = whereParams + " and ";
					}
				}
				count++;
			}	
		}
		strbuf.append("SELECT ");
		
		if(dataBaseSchema.equals("oracle")){
			strbuf.append("CONCAT(GROUPCOLUMN , CONCAT( CONCAT(' (',count(GROUPCOLUMN)) , ')') ) as groupCol ");
		}else{
			strbuf.append("concat(CAST(GROUPCOLUMN AS CHAR),' (', CAST(count(GROUPCOLUMN) AS CHAR) ,') ') as groupCol ");
		}
		
		
		strbuf.append("FROM (SELECT ");
		strbuf.append(viewColumnsList.get("groupFieldsName")+" AS GROUPCOLUMN");
		//User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(listView.getFromQuery()!=null){
			 fromQueryString=String.valueOf(listView.getFromQuery());
			/*String loggedInUserRole = "" + CommonUtil.getLoggedInUser().getRoles() +"";
			List<String> rolesList = new ArrayList<String>(Arrays.asList(loggedInUserRole.replace("[", "").replace("]", "").replace(" ", "").split(",")));
			if(rolesList.contains("ROLE_ADMIN") && fromQueryString.indexOf(":loggedInUserReport") > -1) {
				
			} else {
								
			}*/
			 fromQueryString = replaceUserDetails(fromQueryString);
		}
		
		strbuf.append(" FROM "+fromQueryString);
		
		if(listView.getWhereQuery()!=null && !listView.getWhereQuery().isEmpty()){
			String whereString = String.valueOf(listView.getWhereQuery());
			
			
			String loggedInUserRole = "" + CommonUtil.getLoggedInUser().getRoles() +"";
			List<String> rolesList = new ArrayList<String>(Arrays.asList(loggedInUserRole.replace("[", "").replace("]", "").replace(" ", "").split(",")));
			if(rolesList.contains("ROLE_ADMIN") && whereString.indexOf(":loggedInUserReport") > -1) {
				whereString = null;
			} else {
				
				if( whereString.indexOf(":loggedInUserReport") > -1){
					whereString = whereString.replaceAll(":loggedInUserReport", "");
				}
				
				whereString = replaceUserDetails(whereString);
				 if(listViewParams!=null){
						if(whereString.indexOf(":") > -1){
							String[] whereSplitString=whereString.split("AND");
							for(int index=0;index<whereSplitString.length;index++){
								String[] namedString=whereSplitString[index].split(":");
								if(!String.valueOf(namedString).isEmpty() && namedString.length>=2){
									String temp=namedString[1].trim();
									if(listViewParams.containsKey(temp)){
										String tempName=(String)listViewParams.get(temp);
										whereString = whereString.replaceAll(":"+temp, "'"+tempName+"'");
									}
								}
							}
						}
				}
				
			 	strbuf.append(" WHERE "+whereString+" and ("+viewColumnsList.get("groupFieldsName")+" != '' or "+viewColumnsList.get("groupFieldsName")+" is not null )");
				 	
				if(whereParams != null && !(whereParams.isEmpty())){
					strbuf.append(" and "+whereParams);	
				}
				if(listView.getViewName().equalsIgnoreCase("NOTICE")) {
					strbuf.append("AND NOTICE.IS_DELETE =0");	
				}
			}
		}else if(whereParams != null && !(whereParams.isEmpty())){
			strbuf.append(" WHERE "+whereParams+" and ("+viewColumnsList.get("groupFieldsName")+" != '' or "+viewColumnsList.get("groupFieldsName")+" is not null )");
		}else{
			strbuf.append(" WHERE ("+viewColumnsList.get("groupFieldsName")+" != '' or "+viewColumnsList.get("groupFieldsName")+" is not null )");
		}
		
		if(listView.getGroupBy()!=null && !listView.getGroupBy().isEmpty()){
			strbuf.append(" GROUP BY "+listView.getGroupBy());	
		}/*else{
			if(groupByFields!=null && !groupByFields.isEmpty()){
				StringBuffer groupBy=new StringBuffer();
				for(String groupByField : groupByFields){
					groupBy.append(" "+groupByField+",");
				}
				
				if(groupBy.lastIndexOf(",")>0){
					groupBy.deleteCharAt(groupBy.lastIndexOf(","));
				}
				
				strbuf.append(" GROUP BY "+groupBy.toString()+" ");
			}
		}*/
		if(!dataBaseSchema.equalsIgnoreCase("mssql")) {
			if(viewColumnsList.get("sortType") != null && !String.valueOf(viewColumnsList.get("sortType")).isEmpty()){
				strbuf.append(" ORDER BY "+viewColumnsList.get("groupFieldsName")+" "+viewColumnsList.get("sortType"));	
			}else{
				strbuf.append(" ORDER BY "+viewColumnsList.get("groupFieldsName"));	
			}
		}
		strbuf.append(") AS RESULT GROUP BY GROUPCOLUMN");
		return strbuf.toString();
	}
	
	
	/**
	 * Add group filter in context
	 * @param listViewDataList
	 * @param viewColumnsList
	 * @param context
	 */
	private void addValueForGroupByFilter(List<Map<String, Object>> listViewDataList, List<Map<String, Object>> viewColumnsList, Map<String, Object> context){
		List<Map<String, Object>> groupByFilterDataList = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> viewColumn : viewColumnsList){
			if(String.valueOf(viewColumn.get("isGroupBy")).equals("true")){
				Map<String, Object> groupByFilterData = new HashMap<String, Object>();
				String dataFields = String.valueOf(viewColumn.get("dataFields"));
				String columnTitle = String.valueOf(viewColumn.get("columnTitle"));
				Set<String> groupFilterValue = new HashSet<String>();
				for(Map<String, Object> listViewData : listViewDataList){
					groupFilterValue.add(String.valueOf(listViewData.get(columnTitle)));
				}
				groupByFilterData.put(dataFields, groupFilterValue);
				groupByFilterData.put("searchLabel", viewColumn.get("columnTitle"));
				groupByFilterData.put("dataFields", dataFields);
				groupByFilterDataList.add(groupByFilterData);
				context.put("needTreeStructure", true);
				context.put("isGroupFilter", true);
			}
		}
		context.put("groupFilters", groupByFilterDataList);
	}
	
	
	/**
	 * To short the column linked with the List view
	 * @param columnNameSet
	 * @return List<Map<String, Object>>
	 * @throws EazyBpmException
	 */
	private List<Map<String, Object>> getShortedViewColumnsList(Set<ListViewColumns> columnNameSet)throws EazyBpmException{
		List<Map<String, Object>> viewColumnsList = new ArrayList<Map<String, Object>>();
			for(ListViewColumns listViewColumns:columnNameSet){
				Map<String, Object> viewColumnsMap= new HashMap<String, Object>();
				viewColumnsMap.put("id", listViewColumns.getId());
				viewColumnsMap.put("columnTitle", listViewColumns.getColumnTitle());
				viewColumnsMap.put("width", listViewColumns.getWidth());
				viewColumnsMap.put("onRenderEvent", listViewColumns.getOnRenderEvent());
				viewColumnsMap.put("isHidden", listViewColumns.getIsHidden());
				viewColumnsMap.put("textAlign", listViewColumns.getTextAlign());
				viewColumnsMap.put("orderBy", listViewColumns.getOrderBy());
				viewColumnsMap.put("dataFields", listViewColumns.getDataFields());
				viewColumnsMap.put("isSort", listViewColumns.getIsSort());
				viewColumnsMap.put("replaceWords", listViewColumns.getReplaceWords());
				viewColumnsMap.put("onRenderEventName", listViewColumns.getOnRenderEventName());
				viewColumnsMap.put("isSimpleSearch", listViewColumns.getIsSimpleSearch());
				viewColumnsMap.put("isAdvancedSearch", listViewColumns.getIsAdvancedSearch());
				viewColumnsMap.put("isGroupBy", listViewColumns.getIsGroupBy());
				viewColumnsMap.put("dataFieldType", listViewColumns.getDataFieldType());
				viewColumnsMap.put("dataDictionary", listViewColumns.getDataDictionary());
				viewColumnsMap.put("isRange", listViewColumns.getIsRange());
				viewColumnsMap.put("columnType", listViewColumns.getColumnType());
				viewColumnsList.add(viewColumnsMap);
			}
			Collections.sort(viewColumnsList,new HashMapComparator());
			return viewColumnsList;
	}
	
	/**
	 * To short the Group setting linked with the List view
	 * @param listViewGroupSettings
	 * @return List<Map<String, Object>>
	 * @throws EazyBpmException
	 */
	private List<Map<String, Object>> getShortedViewGroupSettingList(Set<ListViewGroupSetting> listViewGroupSettings)throws EazyBpmException{
		List<Map<String, Object>> groupSettingList = new ArrayList<Map<String, Object>>();
			for(ListViewGroupSetting listViewGroupSetting : listViewGroupSettings){
				Map<String, Object> groupSettingMap= new HashMap<String, Object>();
				groupSettingMap.put("id", listViewGroupSetting.getId());
				groupSettingMap.put("groupName", listViewGroupSetting.getGroupName());
				groupSettingMap.put("groupFieldsName", listViewGroupSetting.getGroupFieldsName());
				groupSettingMap.put("parentGroup", listViewGroupSetting.getParentGroup());
				groupSettingMap.put("sortType", listViewGroupSetting.getSortType());
				groupSettingMap.put("comment", listViewGroupSetting.getComment());
				groupSettingMap.put("groupType", listViewGroupSetting.getGroupType());
				groupSettingMap.put("orderBy", listViewGroupSetting.getOrderBy());
				groupSettingList.add(groupSettingMap);
			}
			Collections.sort(groupSettingList, new HashMapComparator());
			return groupSettingList;
	}
	
	/**
	 * To short the column linked with the List view
	 * @param columnNameSet
	 * @return List<Map<String, Object>>
	 * @throws EazyBpmException
	 */
	private List<Map<String, Object>> getShortedViewButtonsList(Set<ListViewButtons> buttonSet)throws EazyBpmException{
		boolean isAdmin=false;
		List<Map<String, Object>> viewButtonsList = new ArrayList<Map<String, Object>>();
		
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			Set<Role> roles=user.getRoles();
			for(Role role:roles){
				if(role.getId().equals("ROLE_ADMIN")){
					isAdmin=true;
					break;
				}
			}
			for(ListViewButtons listViewButton:buttonSet){
				//Checking button privilege
				if(isAdmin || checkButtonPrivilege(listViewButton,user)){
					Map<String, Object> viewButtonsMap= new HashMap<String, Object>();
					viewButtonsMap.put("id", listViewButton.getId());
					viewButtonsMap.put("displayName", listViewButton.getDisplayName());
					viewButtonsMap.put("buttonMethod", listViewButton.getButtonMethod());
					viewButtonsMap.put("orderBy", listViewButton.getOrderBy());
					viewButtonsMap.put("iconPath", listViewButton.getIconPath());
					viewButtonsMap.put("active", listViewButton.isActive());
					viewButtonsMap.put("columnName", listViewButton.getColumnName());
					viewButtonsMap.put("tableName", listViewButton.getTableName());
					viewButtonsMap.put("redirectValue", listViewButton.getRedirectValue());
					viewButtonsList.add(viewButtonsMap);
				}
			}
			Collections.sort(viewButtonsList,new HashMapComparator());
			return viewButtonsList;
	}
	
	/**
	 * To check the user has the button privilege
	 * @param buttonRoles
	 * @param roles
	 * @return
	 * @throws EazyBpmException
	 */
	private boolean checkButtonPrivilege(ListViewButtons listViewButton,User user)throws EazyBpmException{
		String buttonRoles=listViewButton.getListViewButtonRoles();
		String buttonUserIds=listViewButton.getListViewButtonUsers();
		String buttonDeps=listViewButton.getListViewButtonDeps();
		String buttonGroups=listViewButton.getListViewButtonGroups();
		
			if(buttonUserIds!="" && buttonUserIds!=null && !buttonUserIds.equals("null")){
				String[] buttonUserIdList=buttonUserIds.split(",");
				for(String buttonUserId:buttonUserIdList){
					if(buttonUserId.equals(user.getId())){
						return true;
					}
				}
			}
			
			if(buttonDeps!="" && buttonDeps!=null && !buttonDeps.equals("null")){
				String[] buttonDepList=buttonDeps.split(",");
				for(String buttonDepId:buttonDepList){
					if(buttonDepId.equals(user.getDepartment().getId())){
						return true;
					}
				}
			}
		
			if(buttonRoles!="" && buttonRoles!=null && !buttonRoles.equals("null")){
				String[] buttonRoleList=buttonRoles.split(",");
				Set<Role> roles=user.getRoles();
				for(Role role:roles){
					for(String buttonRoleId:buttonRoleList){
						if(role.getId().equals(buttonRoleId)){
							return true;
						}
					}
				}
			}
		
			if(buttonGroups!="" && buttonGroups!=null && !buttonGroups.equals("null")){
				String[] buttonGroupList=buttonGroups.split(",");
				Set<Group> groups=user.getGroups();
				for(Group group:groups){
					for(String buttonGroupId:buttonGroupList){
						if(group.getId().equals(buttonGroupId)){
							return true;
						}
					}
				}
			}
			
			return false;
	}
	
	/**
	 * To Get Data to form a List View
	 * @param listView
	 * @param colunmnList
	 * @param viewColumnsList
	 * @return
	 * @throws EazyBpmException
	 */
	private List<Map<String,Object>> getAllListViewMapData(ListView listView,List<String> colunmnList,List<Map<String, Object>> viewColumnsList, String whereParams, String sortType, String sortTypeColumn,Map<String, Object> listViewParamsList)throws EazyBpmException{
		String listViewQuery=constructQueryToSelectListData(listView,viewColumnsList, whereParams, sortType, sortTypeColumn,listViewParamsList);
		//log.info("Constructed Query :: "+listViewQuery);
		List listViewObjArray = listViewDao.getListViewDataMap(listViewQuery.replaceAll("[\r\n]+", " "),colunmnList); // replace listview query string to single line string 
		List<Map<String,Object>> listViewDataList = new ArrayList<Map<String,Object>>();
		
		if ((listViewObjArray != null) && (listViewObjArray.size() > 0)) {
			for (int i = 0; i < listViewObjArray.size(); i++) {
				if (listViewObjArray.get(i) instanceof String) {
					String listViewResult = String.valueOf(listViewObjArray.get(i));
					Map<String,Object> columData=new HashMap<String, Object>();
			 	  	for(int index=0;index<colunmnList.size();index++){
			 	  		if(!listViewResult.equals("null")){
			 	  			columData.put(colunmnList.get(index), listViewResult);
			 	  		}else{
			 	  			columData.put(colunmnList.get(index), "");
			 	  		}
			 	  	}
				 	listViewDataList.add(columData);
				} else if (listViewObjArray.get(i) instanceof Object[]) {
					Object[] listViewObjInfo = (Object[]) listViewObjArray.get(i);
					Map<String,Object> columData=new HashMap<String, Object>();
			 	  	for(int index=0;index<colunmnList.size();index++){
			 	  		//For date field
			 	  		if(listViewObjInfo[index] instanceof Date){
			 	  			Date dateTime = null;
			 	  			String dateStr = null;
			 	  			try {
			 	  				String dbData=String.valueOf(listViewObjInfo[index]);
			 	  				if(dbData.length()>11){
			 	  					dateTime = DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss", dbData);
			 	  					dateStr = DateUtil.convertDateToStringFormat(dateTime,"yyyy-MM-dd HH:mm:ss");
			 	  				}else{
			 	  					dateTime = DateUtil.convertStringToDate("yyyy-MM-dd", dbData);
			 	  					dateStr = DateUtil.convertDateToStringFormat(dateTime,"yyyy-MM-dd");
			 	  				}
								
			 	  				if(dateStr != null){
			 	  					if(!dateStr.equals("null")){
			 	  						columData.put(colunmnList.get(index), dateStr);  
			 	  					}else{
			 	  						columData.put(colunmnList.get(index), "");  
			 	  					}
			 	  					 
				 	            }else{
				 	            	if(!String.valueOf(dateTime).equals("null")){
				 	            		columData.put(colunmnList.get(index), dateTime.toString()); 
				 	            	}else{
				 	            		columData.put(colunmnList.get(index), ""); 
				 	            	}
				 	            }
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
			 	  		}else{
			 	  			if(!String.valueOf(listViewObjInfo[index]).equals("null")){
			 	  				columData.put(colunmnList.get(index), String.valueOf(listViewObjInfo[index]));
			 	  			}else{
			 	  				columData.put(colunmnList.get(index), "");
			 	  			}
			 	  		}
			 	  	}
				 	listViewDataList.add(columData);
				}
			}
		}
		
		return listViewDataList;
	}
	
	/**
	 * Construct the query to get the list of data's
	 * @param listView
	 * @param viewColumnsList
	 * @return String
	 * @throws EazyBpmException
	 */
	private String constructQueryToSelectListData(ListView listView,List<Map<String, Object>> viewColumnsList, String whereParams, String sortType, String sortTypeColumn,Map<String, Object> listViewParams)throws EazyBpmException{
		StringBuffer strbuf = new StringBuffer();
		String fromQueryString="";
		int queryLimit = listView.getPageSize();
		strbuf.append("SELECT ");
		
		if(listViewParams!=null && listViewParams.containsKey("needHeader")){
			if(!(Boolean)listViewParams.get("needHeader")){
				if(dataBaseSchema.equalsIgnoreCase("mssql")) {
					strbuf.append(" top "+queryLimit+" ");
				}
			}
		}	
		
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		for(Map<String, Object> viewColumn:viewColumnsList){
			if(!StringUtil.isEmptyString(viewColumn.get("dataFields"))){
				//Add replace word functionality
				String replaceWords = String.valueOf(viewColumn.get("replaceWords"));
				if(replaceWords != null && !(replaceWords.isEmpty()) && !replaceWords.equalsIgnoreCase("null")){
					String replaceStr = "case";
					if(replaceWords.matches("(.*;.*)")) {
						String[] replaceWord = replaceWords.split(";");
						int replaceWordLength = replaceWord.length;	
						for(int index = 0; index < replaceWordLength; index++){
							String[] rplWord = replaceWord[index].split(":");
							replaceStr = replaceStr + " when "+viewColumn.get("dataFields")+" = '"+rplWord[0]+"' then '"+rplWord[1]+"'";
						}
					}
					replaceStr = replaceStr + " else "+viewColumn.get("dataFields")+" end";
					replaceStr = replaceStr + " as '"+String.valueOf(viewColumn.get("columnTitle")+"',");
					strbuf.append(replaceStr);
				}else{
					strbuf.append((String)viewColumn.get("dataFields")+" as '"+(String)viewColumn.get("columnTitle")+"',");
				}
			}
		}

		if(strbuf.lastIndexOf(",")>0){
			strbuf.deleteCharAt(strbuf.lastIndexOf(","));
		}
		
		if(listView.getFromQuery()!=null){
			 fromQueryString=String.valueOf(listView.getFromQuery());
			 fromQueryString = replaceUserDetails(fromQueryString);
		}
		
		strbuf.append(" FROM "+fromQueryString);
		
		if(listView.getWhereQuery()!=null && !listView.getWhereQuery().isEmpty()){
			String whereString = String.valueOf(listView.getWhereQuery());
			
			String loggedInUserRole = "" + CommonUtil.getLoggedInUser().getRoles() +"";
			loggedInUserRole = loggedInUserRole.replace("[", "").replace("]", "").replace("  ", "");
			List<String> rolesList = new ArrayList<String>(Arrays.asList(loggedInUserRole.replace(" ", "").split(",")));
			if(rolesList.contains("ROLE_ADMIN") && whereString.indexOf(":loggedInUserReport") > -1) {
				whereString = null;
			} else {
				
				if( whereString.indexOf(":loggedInUserReport") > -1){
					whereString = whereString.replaceAll(":loggedInUserReport", "");
				}
				
				if(whereString.indexOf(":loggedInUser") > -1){
					String userName = "'"+user.getUsername()+"'";
					
					whereString = whereString.replaceAll(":loggedInUser", userName);
				}
				
				if(whereString.indexOf(":userRole") > -1){
		        	   String userRoles = "'"+user.getRoles()+"'";
		        	   String userRoleArray[] = {};
		        	   if(userRoles.contains("[")){
	           		   userRoles = userRoles.replace("'[", "");
	           		   userRoles = userRoles.replace("]'","");
		        	   }
		        	   if(userRoles.contains(" ")){
		        		  userRoles = userRoles.replace(" ","");
		        	   }
		        	   if(userRoles.contains(",")){
		        		   userRoleArray = userRoles.split(",");
		        	   }
		        	   userRoles = "(";
		        	   for (int i=0;i<userRoleArray.length;i++){
		        		   if(i == userRoleArray.length-1){
		        			   userRoles = userRoles+"'"+userRoleArray[i]+"'"+")";
		        		   }else{
		        			   userRoles = userRoles+"'"+userRoleArray[i]+"'"+",";
		        		   }
		        	   }
		        	  whereString = whereString.replaceAll(":userRole", userRoles);
		     	  } 
				   if (whereString.indexOf(":userGroup") > -1) {
						String userGroups = "'" + user.getGroups() + "'";
						String[] userGroupArray = {};
						if (userGroups.contains("[")) {
							userGroups = userGroups.replace("'[", "");
							userGroups = userGroups.replace("]'", "");
						}
						if (userGroups.contains(" ")) {
							userGroups = userGroups.replace(" ", "");
						}
						
						if (userGroups.contains(",")) {
							userGroups = "(";
							userGroupArray = userGroups.split(",");
							for (int i = 0; i < userGroupArray.length; i++) {
								if (i == userGroupArray.length - 1) {
									userGroups = userGroups + "'" + userGroupArray[i] + "'" + ")";
								} else {
									userGroups = userGroups + "'" + userGroupArray[i] + "'" + ",";
								}
							}
						} else {
							userGroups =   "('" +userGroups+ "')";
						}
										
						whereString = whereString.replaceAll(":userGroup",userGroups);
					} 
				   if(whereString.indexOf(":userDepartment") > -1) {
						String userDepartment = "('" + user.getDepartment() + "')";
						whereString = whereString.replaceAll(":userDepartment",	userDepartment);
					}
					

					if(listViewParams!=null){
							if(whereString.indexOf(":") > -1){
								String[] whereSplitString=whereString.split("AND");
								for(int index=0;index<whereSplitString.length;index++){
									String[] namedString=whereSplitString[index].split(":");
									
									if(!String.valueOf(namedString).isEmpty() && namedString.length>=2){
										String temp=namedString[1].trim();
										if(listViewParams.containsKey(temp)){
											String tempName=(String)listViewParams.get(temp);
											whereString = whereString.replaceAll(":"+temp, "'"+tempName+"'");
										}
									}
								}
							}
					}
					//String loggedInUser = CommonUtil.getLoggedInUserId();
					if(whereParams != null && !(whereParams.isEmpty())){
						strbuf.append(" WHERE ( "+whereString+" )");
						strbuf.append(" and "+whereParams);
					}else{
						strbuf.append(" WHERE "+whereString);
					}
			}
			
			
		}else{
			if(whereParams != null && !(whereParams.isEmpty())){
				if(listView.getViewName().equals("WORKFLOW_INSTANCE_MANAGER")){
					strbuf.append(" AND "+whereParams);
				}else{
					strbuf.append(" WHERE "+whereParams);
				}
					
			}
		}
		
		if(listView.getGroupBy()!=null && !listView.getGroupBy().isEmpty()){
			strbuf.append(" GROUP BY "+listView.getGroupBy());	
		}
		
		if(listView.getOrderBy()!=null && !listView.getOrderBy().isEmpty()){
			strbuf.append(" ORDER BY "+listView.getOrderBy());	
		}else if(sortTypeColumn!=null && !sortTypeColumn.isEmpty()){
			if(sortType != null && !sortType.isEmpty()){
				strbuf.append(" ORDER BY "+sortTypeColumn+" "+sortType);	
			}else{
				strbuf.append(" ORDER BY "+sortTypeColumn);	
			}
		}
		
		if(listViewParams!=null && listViewParams.containsKey("needHeader")){
			if(!(Boolean)listViewParams.get("needHeader")){
				if(dataBaseSchema.equalsIgnoreCase("oracle")) {
					if(strbuf.toString().contains(" WHERE ") || strbuf.toString().contains(" where ")){
						strbuf.append(" and (ROWNUM < "+queryLimit+" or ROWNUM = "+queryLimit+")");
					}else{
						strbuf.append(" where (ROWNUM < "+queryLimit+" or ROWNUM = "+queryLimit+")");
					}
				}else if(dataBaseSchema.equalsIgnoreCase("mysql")){
					strbuf.append(" limit "+queryLimit+"");
				}
			}
		}	
		
		return strbuf.toString();
	}

	/**
	 * {@inheritDoc getColumnsPropertyByListViewId}
	 */
	public List<ListViewColumns> getColumnsPropertyByListViewId(String listViewId)throws EazyBpmException{
		return listViewDao.getColumnsPropertyByListViewId(listViewId);
	}
	
	/**
	 * {@inheritDoc getColumnsPropertyByListViewId}
	 */
	public List<ListViewGroupSetting> getGroupSettingPropertyByListViewId(String listViewId)throws EazyBpmException{
		return listViewDao.getGroupSettingPropertyByListViewId(listViewId);
	}
	/**
	 * {@inheritDoc saveListView}
	 */
	public ListView saveListView(ListView listView,String modeType,String moduleId)throws EazyBpmException{
		String listViewId=null;
		ListView oldListView=null;
		Set<ListViewColumns> listViewColumnsSet=null;
		Set<ListViewButtons> listViewButtonSet=null;
		Set<ListViewGroupSetting> listViewGroupSet=null;
		
		Module module=moduleService.getModule(moduleId);
		
		if(modeType.equalsIgnoreCase("update")) {
			listViewId=listView.getId();
			oldListView=listViewDao.getListViewById(listViewId);
			
			listViewColumnsSet=oldListView.getListViewColumns();
			listViewButtonSet=oldListView.getListViewButtons();
			listViewGroupSet=oldListView.getListViewGroupSetting();
			
			Integer version = listViewDao.getLatestVersionByForm(listView.getViewName());
			listView.setVersion(version + 1);
			listView.setCreatedTime(oldListView.getCreatedTime());
			listViewDao.updateListViewStatusByName(listView.getViewName());
			
			//to get the old modules
			Set<Module> oldModules=oldListView.getModules();
			
			if(!oldModules.contains(module)){
				oldModules.add(module);
			}
			
			List<String> moduleIds=new ArrayList<String>();
			for(Module oldModulesObj: oldModules){
				moduleIds.add(oldModulesObj.getId());
			}
			
			Set<Module> newModules=new HashSet<Module>();
			newModules.addAll(moduleService.getModulesByIds(moduleIds));
			listView.setModules(newModules);
			
			listView.setListViewColumns(null);
			listView.setListViewButtons(null);
			listView.setListViewGroupSetting(null);
		} else {
			listView.addModule(module);
			listView.setVersion(1);
		}
		
		listView.setActive(true);
		listView=listViewDao.saveListView(listView);		
	
		if(modeType.equalsIgnoreCase("update")){
			//update ListViewColumns
			if(!listViewColumnsSet.isEmpty()){
				versionForListViewColumns(listViewId,listViewColumnsSet,listView);
			}
			
			//update ListViewButtons
			if(!listViewButtonSet.isEmpty()){
				versionForViewButtons(listViewId,listViewButtonSet,listView);
			}
			
			//update List View Groups
			if(!listViewGroupSet.isEmpty()){
				versionForViewGroups(listViewId,listViewGroupSet,listView);
			}
			
		}
		
		return listView;
	}
	
	/**
	 * To create List view columns versioning
	 * @param listViewId
	 * @param listViewColumnsSet
	 * @param listView
	 */
	private void versionForListViewColumns(String listViewId,Set<ListViewColumns> listViewColumnsSet,ListView listView){
		listViewDao.updateListActiveStatusByListViewId(listViewId,"ListViewColumns");
		for(ListViewColumns listViewObj:listViewColumnsSet){
			ListViewColumns listViewColumn = new ListViewColumns();				
			listViewColumn.setColumnTitle(String.valueOf(listViewObj.getColumnTitle()));
			listViewColumn.setDataFields(String.valueOf(listViewObj.getDataFields()));
			listViewColumn.setWidth(String.valueOf(listViewObj.getWidth()));
			listViewColumn.setTextAlign(String.valueOf(listViewObj.getTextAlign()));
			listViewColumn.setOnRenderEvent(String.valueOf(listViewObj.getOnRenderEvent()));
			listViewColumn.setOtherName(String.valueOf(listViewObj.getOtherName()));
			listViewColumn.setReplaceWords(String.valueOf(listViewObj.getReplaceWords()));
			listViewColumn.setComment(String.valueOf(listViewObj.getComment()));
			listViewColumn.setOrderBy(Integer.parseInt(String.valueOf(listViewObj.getOrderBy())));
			listViewColumn.setIsHidden(Boolean.parseBoolean(String.valueOf(listViewObj.getIsHidden())));
			listViewColumn.setIsSort(Boolean.parseBoolean(String.valueOf(listViewObj.getIsSort())));
			listViewColumn.setIsAdvancedSearch(Boolean.parseBoolean(String.valueOf(listViewObj.getIsAdvancedSearch())));
			listViewColumn.setIsSimpleSearch(Boolean.parseBoolean(String.valueOf(listViewObj.getIsSimpleSearch())));
			listViewColumn.setOnRenderEventName(String.valueOf(listViewObj.getOnRenderEventName()));
			listViewColumn.setIsGroupBy(Boolean.parseBoolean(String.valueOf(listViewObj.getIsGroupBy())));
			listViewColumn.setDataFieldType(String.valueOf(listViewObj.getDataFieldType()));
			listViewColumn.setIsRange(Boolean.parseBoolean(String.valueOf(listViewObj.getIsRange())));
			listViewColumn.setColumnType(String.valueOf(listViewObj.getColumnType()));
			listViewColumn.setActive(Boolean.parseBoolean(String.valueOf(listViewObj.isActive())));
			listViewColumn.setListView(listView);
			listViewColumn.setVersion(listView.getVersion());
			listViewDao.saveListViewColumns(listViewColumn);
		}
	}
	
	/**
	 * To create List view button versioning
	 * @param listViewId
	 * @param listViewColumnsSet
	 * @param listView
	 */
	private void versionForViewGroups(String listViewId,Set<ListViewGroupSetting> listViewGroupSet,ListView listView){
		listViewDao.updateListActiveStatusByListViewId(listViewId,"ListViewGroupSetting");
		for(ListViewGroupSetting listViewObj:listViewGroupSet){
			ListViewGroupSetting listViewGroupSetting = new ListViewGroupSetting();
			
			listViewGroupSetting.setGroupName(String.valueOf(listViewObj.getGroupName()));
			listViewGroupSetting.setGroupFieldsName(String.valueOf(listViewObj.getGroupFieldsName()));
			listViewGroupSetting.setParentGroup(String.valueOf(listViewObj.getParentGroup()));
			listViewGroupSetting.setSortType(String.valueOf(listViewObj.getSortType()));
			listViewGroupSetting.setComment(String.valueOf(listViewObj.getComment()));
			listViewGroupSetting.setGroupType(String.valueOf(listViewObj.getGroupType()));
			listViewGroupSetting.setOrderBy(Integer.parseInt(String.valueOf(listViewObj.getOrderBy())));
			
			listViewGroupSetting.setActive(Boolean.parseBoolean(String.valueOf(listViewObj.isActive())));
			listViewGroupSetting.setListView(listView);
			listViewGroupSetting.setVersion(listView.getVersion());
			listViewDao.saveListViewGroupSetting(listViewGroupSetting);
		}
	}
	
	/**
	 * To create List view Group versioning
	 * @param listViewId
	 * @param listViewColumnsSet
	 * @param listView
	 */
	private void versionForViewButtons(String listViewId,Set<ListViewButtons> listViewColumnsSet,ListView listView){
		//listViewDao.updateListActiveStatusByListViewId(listViewId,"ListViewButtons");
		for(ListViewButtons listViewObj:listViewColumnsSet){
			ListViewButtons listViewButton = new ListViewButtons();	
			listViewButton.setDisplayName(String.valueOf(listViewObj.getDisplayName()));
			listViewButton.setButtonMethod(String.valueOf(listViewObj.getButtonMethod()));
			listViewButton.setOrderBy(Integer.parseInt(String.valueOf(listViewObj.getOrderBy())));
			listViewButton.setIconPath(String.valueOf(listViewObj.getIconPath()));
			listViewButton.setTableName(String.valueOf(listViewObj.getTableName()));
			listViewButton.setColumnName(String.valueOf(listViewObj.getColumnName()));
			listViewButton.setRedirectValue(String.valueOf(listViewObj.getRedirectValue()));
			listViewButton.setListViewButtonRoles(String.valueOf(listViewObj.getListViewButtonRoles()));
			listViewButton.setListViewButtonUsers(String.valueOf(listViewObj.getListViewButtonUsers()));
			listViewButton.setListViewButtonUsersFullName(String.valueOf(listViewObj.getListViewButtonUsersFullName()));
			listViewButton.setListViewButtonDeps(String.valueOf(listViewObj.getListViewButtonDeps()));
			listViewButton.setListViewButtonGroups(String.valueOf(listViewObj.getListViewButtonGroups()));
			listViewButton.setActive(Boolean.parseBoolean(String.valueOf(listViewObj.isActive())));
			listViewButton.setListView(listView);
			listViewButton.setVersion(listView.getVersion());
			listViewDao.saveListViewButtons(listViewButton);
		}
	}
	
	/**
	 * Remove buttons property id when create colon the list view
	 * @param columnsPropertys
	 * @return
	 */
	private Set<ListViewButtons> removeIdInListViewButtonsProperties(Set<ListViewButtons> buttonsProperties){
		Set<ListViewButtons> listViewButtonsProperties = new HashSet<ListViewButtons>();
		for(ListViewButtons buttonsProperty : buttonsProperties){
			buttonsProperty.setId("");
			listViewButtonsProperties.add(buttonsProperty);
		}
		return listViewButtonsProperties;
	}
	
	public Set<Module> getListViewModules(String listViewId,Module moduleObj){
		List<Module> moduleList=moduleService.getListViewModules(listViewId);
		Set<Module> modules= new HashSet<Module>();
		modules.addAll(moduleList);
		if(!modules.contains(moduleObj)){
			modules.add(moduleObj);
		}
		return modules;
	}
	
	/**
	 * {@inheritDoc saveListViewColumnDetails}
	 */
	public ListView saveListViewColumnDetails(ListView listView,List<Map<String,Object>> listViewMetaColumns,boolean isListViewColumnUpdate,String moduleId)throws EazyBpmException{
		String listViewId="";
		Module module=moduleService.getModule(moduleId);
		listView.setModules(getListViewModules(listView.getId(),module));
		
		Set<ListViewColumns> listViewColumnsSet=new HashSet<ListViewColumns>();
		Set<ListViewButtons> listViewButtonsSet=new HashSet<ListViewButtons>();
		Set<ListViewGroupSetting> listViewGroupSettingSet=new HashSet<ListViewGroupSetting>();
		if(isListViewColumnUpdate){
			listViewId=listView.getId();
			Integer version = listViewDao.getLatestVersionByForm(listView.getViewName());
			listView.setVersion(version + 1);
			listViewDao.updateListViewStatusByName(listView.getViewName());
			listView.setActive(true);
			
			listViewColumnsSet=listView.getListViewColumns();
			listViewButtonsSet=listView.getListViewButtons();
			listViewGroupSettingSet=listView.getListViewGroupSetting();
			
			listView.setListViewColumns(null);
			listView.setListViewButtons(null);
			listView.setListViewGroupSetting(null);
			
			//save new list view
			listView=listViewDao.saveListView(listView);
			
			//update ListViewColumns
			if(!listViewColumnsSet.isEmpty()){
				listViewDao.updateListActiveStatusByListViewId(listViewId,"ListViewColumns");
			}
			
			//update ListViewButtons
			if(!listViewButtonsSet.isEmpty()){
				versionForViewButtons(listViewId,listViewButtonsSet,listView);
			}
			
			//update List View Groups
			if(!listViewGroupSettingSet.isEmpty()){
				versionForViewGroups(listViewId,listViewGroupSettingSet,listView);
			}
			
		}
		//int orderNo = 1;
		for(Map<String,Object> listViewMap: listViewMetaColumns){
			
			ListViewColumns listViewColumn = new ListViewColumns();				
			listViewColumn.setColumnTitle(String.valueOf(listViewMap.get("columnTitle")));
			listViewColumn.setDataFields(String.valueOf(listViewMap.get("dataFields")));
			listViewColumn.setWidth(String.valueOf(listViewMap.get("width")));
			listViewColumn.setTextAlign(String.valueOf(listViewMap.get("textAlign")));
			listViewColumn.setOnRenderEvent(String.valueOf(listViewMap.get("onRenderEvent")));
			listViewColumn.setOtherName(String.valueOf(listViewMap.get("otherName")));
			listViewColumn.setReplaceWords(String.valueOf(listViewMap.get("replaceWords")));
			listViewColumn.setComment(String.valueOf(listViewMap.get("comment")));
			listViewColumn.setOrderBy(Integer.parseInt(String.valueOf(listViewMap.get("orderBy"))));
			listViewColumn.setIsHidden(Boolean.parseBoolean(String.valueOf(listViewMap.get("isHidden"))));
			listViewColumn.setIsSort(Boolean.parseBoolean(String.valueOf(listViewMap.get("isSort"))));
			listViewColumn.setIsAdvancedSearch(Boolean.parseBoolean(String.valueOf(listViewMap.get("isAdvancedSearch"))));
			listViewColumn.setIsSimpleSearch(Boolean.parseBoolean(String.valueOf(listViewMap.get("isSimpleSearch"))));
			listViewColumn.setOnRenderEventName(String.valueOf(listViewMap.get("onRenderEventName")));
			listViewColumn.setIsGroupBy(Boolean.parseBoolean(String.valueOf(listViewMap.get("isGroupBy"))));
			listViewColumn.setDataDictionary(String.valueOf(listViewMap.get("dataDictionary")));
			listViewColumn.setDataFieldType(String.valueOf(listViewMap.get("dataFieldType")));
			listViewColumn.setIsRange(Boolean.parseBoolean(String.valueOf(listViewMap.get("isRange"))));
			listViewColumn.setColumnType(String.valueOf(listViewMap.get("columnType")));
			listViewColumn.setActive(true);
			listViewColumn.setVersion(listView.getVersion());//generateOrderNo
			//listViewColumn.setOrderNo(orderNo);
			//orderNo++;
			listViewColumn.setListView(listView);

			listViewDao.saveListViewColumns(listViewColumn);
		}
		
		return listView;
	}
	
	/**
	 * {@inheritDoc saveListViewGroupSettingDetails}
	 */
	public ListView saveListViewGroupSettingDetails(ListView listView,List<Map<String,Object>> listViewMetaColumns,boolean isListViewGroupSettingUpdate,String moduleId)throws EazyBpmException{
		String listViewId="";
		Module module=moduleService.getModule(moduleId);
		listView.setModules(getListViewModules(listView.getId(),module));
		
		Set<ListViewColumns> listViewColumnsSet=new HashSet<ListViewColumns>();
		Set<ListViewButtons> listViewButtonsSet=new HashSet<ListViewButtons>();
		Set<ListViewGroupSetting> listViewGroupSettingSet=new HashSet<ListViewGroupSetting>();
		if(isListViewGroupSettingUpdate){
			listViewId=listView.getId();
			Integer version = listViewDao.getLatestVersionByForm(listView.getViewName());
			listView.setVersion(version + 1);
			listViewDao.updateListViewStatusByName(listView.getViewName());
			listView.setActive(true);
			
			listViewColumnsSet=listView.getListViewColumns();
			listViewButtonsSet=listView.getListViewButtons();
			listViewGroupSettingSet=listView.getListViewGroupSetting();
			
			listView.setListViewColumns(null);
			listView.setListViewButtons(null);
			listView.setListViewGroupSetting(null);
			
			listView=listViewDao.saveListView(listView);
			
			//update ListViewButtons
			if(!listViewColumnsSet.isEmpty()){
				versionForListViewColumns(listViewId, listViewColumnsSet, listView);
			}
			
			//update ListViewButtons
			if(!listViewButtonsSet.isEmpty()){
				versionForViewButtons(listViewId,listViewButtonsSet,listView);
			}
			
			//update List View Groups
			if(!listViewGroupSettingSet.isEmpty()){
				listViewDao.updateListActiveStatusByListViewId(listViewId,"ListViewGroupSetting");
			}
			
		}
		
		if(listViewMetaColumns!=null){
			for(Map<String,Object> listViewMap: listViewMetaColumns){
				ListViewGroupSetting listViewGroupSetting = new ListViewGroupSetting();				
				listViewGroupSetting.setGroupName(String.valueOf(listViewMap.get("groupName")));
				listViewGroupSetting.setGroupFieldsName(String.valueOf(listViewMap.get("groupFieldsName")));
				listViewGroupSetting.setParentGroup(String.valueOf(listViewMap.get("parentGroup")));
				listViewGroupSetting.setSortType(String.valueOf(listViewMap.get("sortType")));
				listViewGroupSetting.setComment(String.valueOf(listViewMap.get("comment")));
				listViewGroupSetting.setGroupType(String.valueOf(listViewMap.get("groupType")));
				listViewGroupSetting.setOrderBy(Integer.parseInt(String.valueOf(listViewMap.get("orderBy"))));
				listViewGroupSetting.setActive(true);
				listViewGroupSetting.setVersion(listView.getVersion());
				listViewGroupSetting.setListView(listView);
				listViewDao.saveListViewGroupSetting(listViewGroupSetting);
			}
		}
		
		return listView;
	}
	
	/**
	 * {@inheritDoc deleteListViewColumnsByIds}
	 */
	public void deleteListViewByIds(List<String> deleteListViewDetails,String tableName)throws EazyBpmException{
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("DELETE FROM "+tableName+" WHERE ID IN (");
		for(String listViewIds:deleteListViewDetails){
			strbuf.append("'"+listViewIds+"',");
		}
		
		if(strbuf.lastIndexOf(",")>0){
			strbuf.deleteCharAt(strbuf.lastIndexOf(","));
		}
		strbuf.append(")");
		listViewDao.deleteListViewByIds(strbuf.toString());
	}
	
	/**
	 * {@inheritDoc getListViewById}
	 */
	public ListView getListViewById(String listViewId)throws EazyBpmException{
		return listViewDao.getListViewById(listViewId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Module getListViewModulesById(String listViewId)throws EazyBpmException{
		/*Module module=null;
		ListView listView=listViewDao.getListViewById(listViewId);
		for(Module moduleObj:listView.getModules()){
   		 module=moduleObj;
   		 break;
		}
		*/
		return listViewDao.getModuleByListViewId(listViewId);
	}
	
	/**
	 * {@inheritDoc constructListViewQuery}
	 */
	public String constructListViewQuery(ListView listView)throws EazyBpmException{
		Set<ListViewColumns> columnNameSet=listView.getListViewColumns();
		
		//list of map obj
		List<Map<String, Object>> viewColumnsList = getShortedViewColumnsList(columnNameSet);
		
		String listViewQuery=constructQueryToSelectListData(listView,viewColumnsList, null, "asc", listView.getOrderBy(),null);
		//log.info("Constructed Query :: "+listViewQuery);
		return listViewQuery;
	}
	
	/**
	 * {@inheritDoc getAllListViewMapDataWithSearchParams}
	 */
	public List<Map<String, Object>> getAllListViewMapDataWithSearchParams(ListView listView, Map<String, Object> searchParamMap, String sortType, String sortTypeColumn, String searchType,Map<String,Object> listViewParamsList)throws EazyBpmException{
		String whereParams = "";
		Set<ListViewColumns> columnNameSet = listView.getListViewColumns();
		//list of map obj
		List<Map<String, Object>> viewColumnsList = getShortedViewColumnsList(columnNameSet);
		List<String> colunmnList = new ArrayList<String>();
		for(Map<String, Object> viewColumns : viewColumnsList){
			//if((String)viewColumns.get("dataFields")!="" && (String)viewColumns.get("dataFields")!=null ){
			if(!StringUtil.isEmptyString(viewColumns.get("dataFields"))){
				colunmnList.add(String.valueOf(viewColumns.get("columnTitle")).replaceAll(" ", "_"));
			}
		}
		int index = 1;
		for(String key : searchParamMap.keySet()){
			String whereParamsValue = String.valueOf(searchParamMap.get(key));
			if(whereParamsValue != null && !(whereParamsValue.isEmpty())){
				whereParams = whereParams + key+" = '"+searchParamMap.get(key)+"'";
				if(searchParamMap.size() > index){
					whereParams = whereParams + " "+searchType+" ";
				}
			}
			index++;
		}			
		//log.info("search params ===== : "+whereParams);
		//list of map obj
		List<Map<String,Object>>  listViewDataList = getAllListViewMapData(listView, colunmnList, viewColumnsList, whereParams, sortType, sortTypeColumn,listViewParamsList);
		return listViewDataList;
	}
	
	/**
	 * {@inheritDoc listViewAdvancedSearch}
	 */
	public List<Map<String, Object>> getDataWithSearchParamsAndConstraints(ListView listView, List<Map<String, Object>> searchParamListOfMap, String sortType, String sortTypeColumn, String searchType,Map<String,Object> namedParameters)throws EazyBpmException{
		String whereParams = "";
		Set<ListViewColumns> columnNameSet = listView.getListViewColumns();
		//list of map obj
		List<Map<String, Object>> viewColumnsList = getShortedViewColumnsList(columnNameSet);
		List<String> colunmnList = new ArrayList<String>();
		for(Map<String, Object> viewColumns : viewColumnsList){
			//if((String)viewColumns.get("dataFields")!="" && (String)viewColumns.get("dataFields")!=null ){
			if(!StringUtil.isEmptyString(viewColumns.get("dataFields"))){
				colunmnList.add(String.valueOf(viewColumns.get("columnTitle")).replaceAll(" ", "_"));
			}
		}
		int index = 1;
		for(Map<String, Object> searchParamMap : searchParamListOfMap){
			boolean isDataFormate = false;
			String searchValue=String.valueOf(searchParamMap.get("searchValue"));
			
			if(dataBaseSchema.equals("mssql") || dataBaseSchema.equals("oracle")){
				try {
					DateUtil.convertStringToDate("yyyy-MM-dd", searchValue);
					isDataFormate=true;
				} catch (ParseException e) {
				}
			}	
			
			if(!searchValue.isEmpty() && searchParamMap.get("constraint").equals("like")){
				if(searchValue.equalsIgnoreCase("true") || searchValue.equalsIgnoreCase("false")){
					searchValue = searchValue;
				}else{
					if(isDataFormate){
						String tempSearchValue = searchValue;
						String tempSearchText = "";
						if(dataBaseSchema.equals("oracle")){
							searchValue = " TO_DATE('"+searchValue+"', 'YYYY-mm-dd')";
						}else{
							String[] date =tempSearchValue.split(" ");
							String[] sepratedDate =date[0].split("-");
							if(sepratedDate.length > 1){
								tempSearchText = " (DATEPART(yy, DEPLOY_TIME_) = "+sepratedDate[0]+" AND  DATEPART(mm, DEPLOY_TIME_) = "+sepratedDate[1]+" " +
										" AND DATEPART(dd, DEPLOY_TIME_) = "+sepratedDate[2]+")";
							}
							searchValue=tempSearchText;
						}
					}else{
						searchValue="'%"+searchValue+"%'";
					}	
				}
			}
			
			if(isDataFormate){
				if(dataBaseSchema.equals("oracle")){
					whereParams = whereParams + " TRUNC("+searchParamMap.get("searchField")+")" +" "+ searchParamMap.get("constraint") +" "+ searchValue ;
				}else{
					whereParams = whereParams + searchValue;
				}
			}else{
				whereParams = whereParams + searchParamMap.get("searchField") +" "+ searchParamMap.get("constraint") +" "+searchValue;
			}
			
			if(searchParamListOfMap.size() > index){
				whereParams = whereParams + " "+searchType+" ";
			}
			index++;
		}
		//log.info("search params ===== : "+whereParams);
		//list of map obj
		List<Map<String,Object>>  listViewDataList = getAllListViewMapData(listView, colunmnList, viewColumnsList, whereParams, sortType, sortTypeColumn,namedParameters);
		return listViewDataList;}
	
	/**
	 * {@inheritDoc getAllListViewMapDataWithSearchValue}
	 */
	public List<Map<String, Object>> getAllListViewMapDataWithSearchValue(ListView listView, String searchValue,Map<String,Object> namedParameters)throws EazyBpmException{
		String whereParams = "";
		String avoidAcutalWords = "";
		Set<ListViewColumns> columnNameSet = listView.getListViewColumns();
		//list of map obj
		List<Map<String, Object>> viewColumnsList = getShortedViewColumnsList(columnNameSet);
		List<String> colunmnList = new ArrayList<String>();
		int index = 0;
		for(Map<String, Object> viewColumns : viewColumnsList){
			//if((String)viewColumns.get("dataFields")!="" && (String)viewColumns.get("dataFields")!=null ){
	    	if(!StringUtil.isEmptyString(viewColumns.get("dataFields"))){
		        colunmnList.add(String.valueOf(viewColumns.get("columnTitle")).replaceAll(" ", "_"));
		    }
		    if(String.valueOf(viewColumns.get("isSimpleSearch")).equals("true")){
		        String columnName = String.valueOf(viewColumns.get("dataFields"));
		        String replaceWords = String.valueOf(viewColumns.get("replaceWords"));
		        // to show replace and actual words while search
		        if(!replaceWords.isEmpty() && replaceWords != null && !(replaceWords.equalsIgnoreCase("null"))) {
		            String replaceValue = (String) viewColumns.get("replaceWords");
		            
					String[] replaceWord = replaceValue.split(";");
					int replaceWordLength = replaceWord.length;	
					for(int index1 = 0; index1 < replaceWordLength; index1++){
						String[] rplWord = replaceWord[index1].split(":");
						//replaceStr = replaceStr + " when "+viewColumn.get("dataFields")+" = '"+rplWord[0]+"' then '"+rplWord[1]+"'";
		                if(!whereParams.isEmpty()){
		                    whereParams = whereParams + " or ";
		                }
		                
		                if(rplWord.length!= 1 && rplWord[1].startsWith(searchValue)) {
		                    whereParams = whereParams + columnName+" like '"+rplWord[0]+"%'";
		                    break;
		                } else {
		                    if(!avoidAcutalWords.isEmpty()){
		                        avoidAcutalWords = avoidAcutalWords + " and ";
		                    }
		                    whereParams = whereParams + columnName+" like '"+searchValue+"%' or "+columnName+" like '%"+searchValue+"%'";
		                    avoidAcutalWords = avoidAcutalWords + columnName+" NOT LIKE '"+rplWord[0]+"'";
		                }
						
					}
		        } else {
		            if(index != 0 && !whereParams.isEmpty()){
		                whereParams = whereParams + " or ";
		            }
		            whereParams = whereParams + columnName+" like '"+searchValue+"%' or "+columnName+" like '%"+searchValue+"%'";
		        }
		        index++;
		    }
		}

		    if(searchValue.isEmpty()){
		        whereParams="";
		    }
		    if(whereParams!=""){
		        whereParams="("+whereParams+")";
		    }
		    if(avoidAcutalWords != "") {
		    	if(whereParams!=""){
		    		whereParams = whereParams + "AND ("+avoidAcutalWords+")";
		    	}else{
		    		whereParams = whereParams + " ("+avoidAcutalWords+")";
		    	}
		    	
		    }
		//log.info("search params with search value ===== : "+whereParams);
		//list of map obj
		List<Map<String,Object>>  listViewDataList = getAllListViewMapData(listView, colunmnList, viewColumnsList, whereParams, "asc", listView.getOrderBy(),namedParameters);
		return listViewDataList;
	}
	
	/**
	 *  {@inheritDoc checkListViewQuery}
	 */
	public boolean checkListViewQuery(ListView listView)throws EazyBpmException{
		String listViewQuery=constructListViewQuery(listView);
		listViewDao.getListViewDataMap(listViewQuery, null);
		return true;
	}
	
	/**
	 *  {@inheritDoc checkListViewQuery}
	 */
	public boolean checkListViewQuery(String listViewQuery)throws EazyBpmException{
		if(listViewQuery.contains(":")){
			String[] whereSplitString = listViewQuery.split("AND");
			for(int index=0;index<whereSplitString.length;index++){
				String whereString=whereSplitString[index];
				if(whereString.contains(":")){
					String[] namedString=whereString.split(":");
					if(!String.valueOf(namedString).isEmpty()){
						listViewQuery=listViewQuery.replaceAll(":"+namedString[1], "''");
					}
				}
				
			}
		}
		listViewDao.getListViewDataMap(listViewQuery, null);
		return true;
	}
	
	/**
	 * {@inheritDoc checkListViewName}
	 */
	public boolean checkListViewName(String listViewName)throws EazyBpmException{
		return listViewDao.checkListViewName(listViewName);
		
	}
	
	/**
	 *  {@inheritDoc saveListViewButtonDetails}
	 */
	public ListView saveListViewButtonDetails(ListView listView,List<Map<String,Object>> listViewMetaButtons,boolean isListViewButttonUpdate,String moduleId)throws EazyBpmException{
		String listViewId="";
		Module module=moduleService.getModule(moduleId);
		listView.setModules(getListViewModules(listView.getId(),module));
		
		Set<ListViewColumns> listViewColumnsSet=new HashSet<ListViewColumns>();
		Set<ListViewButtons> listViewButtonsSet=new HashSet<ListViewButtons>();
		Set<ListViewGroupSetting> listViewGroupSettingSet=new HashSet<ListViewGroupSetting>();
		
		if(isListViewButttonUpdate){
			listViewId=listView.getId();
			Integer version = listViewDao.getLatestVersionByForm(listView.getViewName());
			listView.setVersion(version + 1);
			listViewDao.updateListViewStatusByName(listView.getViewName());
			listView.setActive(true);
			
			listViewColumnsSet=listView.getListViewColumns();
			listViewButtonsSet=listView.getListViewButtons();
			listViewGroupSettingSet=listView.getListViewGroupSetting();
			
			listView.setListViewColumns(null);
			listView.setListViewButtons(null);
			listView.setListViewGroupSetting(null);
			//save new list view
			listView=listViewDao.saveListView(listView);
			
			//update ListViewButtons
			if(!listViewColumnsSet.isEmpty()){
				versionForListViewColumns(listViewId, listViewColumnsSet, listView);
			}
			
			//update ListViewButtons
			if(!listViewButtonsSet.isEmpty()){
				//listViewDao.updateListActiveStatusByListViewId(listViewId,"ListViewButtons");
			}
			
			//update List View Groups
			if(!listViewGroupSettingSet.isEmpty()){
				versionForViewGroups(listViewId, listViewGroupSettingSet, listView);
			}
			
		}
		
		if(listViewMetaButtons!=null){
			for(Map<String,Object> listViewMap: listViewMetaButtons){
				ListViewButtons listViewButton = new ListViewButtons();
				listViewButton.setDisplayName(String.valueOf(listViewMap.get("displayName")));
				listViewButton.setButtonMethod(String.valueOf(listViewMap.get("buttonMethod")));
				listViewButton.setOrderBy(Integer.parseInt(String.valueOf(listViewMap.get("orderBy"))));
				listViewButton.setIconPath(String.valueOf(listViewMap.get("iconPath")));
				listViewButton.setActive(Boolean.parseBoolean(String.valueOf(listViewMap.get("active"))));
				listViewButton.setVersion(listView.getVersion());
				listViewButton.setListView(listView);
				listViewButton.setColumnName(String.valueOf(listViewMap.get("columnName")));
				listViewButton.setTableName(String.valueOf(listViewMap.get("tableName")));
				listViewButton.setRedirectValue(String.valueOf(listViewMap.get("redirectValue")));
				if(listViewMap.get("listViewButtonRoles")==null || listViewMap.get("listViewButtonRoles").equals("null")){
					listViewButton.setListViewButtonRoles("");
				}else{
					listViewButton.setListViewButtonRoles(String.valueOf(listViewMap.get("listViewButtonRoles")));
				}
				
				if(listViewMap.get("listViewButtonUsers")==null || listViewMap.get("listViewButtonUsers").equals("null")){
					listViewButton.setListViewButtonUsers("");
				}else{
					listViewButton.setListViewButtonUsers(String.valueOf(listViewMap.get("listViewButtonUsers")));
				}
				
				if(listViewMap.get("listViewButtonUsersFullName")==null || listViewMap.get("listViewButtonUsersFullName").equals("null")){
					listViewButton.setListViewButtonUsersFullName("");
				}else{
					listViewButton.setListViewButtonUsersFullName(String.valueOf(listViewMap.get("listViewButtonUsersFullName")));
				}
				
				if(listViewMap.get("listViewButtonGroups")==null || listViewMap.get("listViewButtonGroups").equals("null")){
					listViewButton.setListViewButtonGroups("");
				}else{
					listViewButton.setListViewButtonGroups(String.valueOf(listViewMap.get("listViewButtonGroups")));
				}
				
				if(listViewMap.get("listViewButtonDeps")==null || listViewMap.get("listViewButtonDeps").equals("null")){
					listViewButton.setListViewButtonDeps("");
				}else{
					listViewButton.setListViewButtonDeps(String.valueOf(listViewMap.get("listViewButtonDeps")));
				}
					
				listViewDao.saveListViewButtons(listViewButton);
			}
		}
		
		
		return listView;
	}
	
	/**
	 * {@inheritDoc getColumnsPropertyByListViewId}
	 */
	public List<ListViewButtons> getButtonPropertyByListViewId(String listViewId)throws EazyBpmException{
		return listViewDao.getButtonPropertyByListViewId(listViewId);
	}
	
	/**
	 * {@inheritDoc deleteListViewDetailsById}
	 */
	public void deleteListViewDetailsById(String listViewId)throws EazyBpmException{
		ListView viewObj = listViewDao.getListViewById(listViewId);
		Set<Module> modules = viewObj.getModules();
		//Remove form relation from module before delete
		if(!modules.isEmpty()){
			viewObj.getModules().removeAll(modules);
			viewObj=listViewDao.saveListView(viewObj);
		}
		listViewDao.deleteListViewDetailsById(viewObj);
	}
	
	/**
	 * {@inheritDoc softDeleteTableData}
	 */
	public int softDeleteTableDataAndRestore(List<String> deleteTableIds, String tableName, String columnName, int status)throws EazyBpmException{
		StringBuffer tableIds = new StringBuffer();
		String deletedUser = null;
		String deletedTime = null;
		
		tableIds.append("(");
		for(String tableId: deleteTableIds){
			tableIds.append("'"+tableId+"',");
		}		
		if(tableIds.lastIndexOf(",")>0){
			tableIds.deleteCharAt(tableIds.lastIndexOf(","));
		}
		tableIds.append(")");
		//log.info("tableIds "+String.valueOf(tableIds));
		if(tableName.equalsIgnoreCase("ETEC_RE_LIST_VIEW")) {
			deletedUser = CommonUtil.getLoggedInUserId();
			deletedTime = DateUtil.convertDateToString(ClockUtil.getCurrentTime());
			if(dataBaseSchema.equals("oracle")){
				deletedTime = CommonUtil.convertDateTypeForDB(dataBaseSchema,deletedTime);
			}
		}
		return listViewDao.softDeleteTableDataAndRestore(String.valueOf(tableIds), tableName, columnName, status, deletedUser, deletedTime);
	}
	
	/**
	 * {@inheritDoc deleteTableData}
	 */
	public int deleteTableData(List<String> deleteTableIds, String tableName, String columnName)throws EazyBpmException{
		StringBuffer tableIds = new StringBuffer();
		tableIds.append("(");
		for(String tableId: deleteTableIds){
			tableIds.append("'"+tableId+"',");
		}		
		if(tableIds.lastIndexOf(",")>0){
			tableIds.deleteCharAt(tableIds.lastIndexOf(","));
		}
		tableIds.append(")");
		return listViewDao.deleteTableData(tableName, String.valueOf(tableIds), columnName);
	}
	
	/**
	 * {@inheritDoc getListViewByIds}
	 */
	public  List<ListView> getListViewByIds(List<String> ids)throws EazyBpmException{
		return listViewDao.getListViewByIds(ids);
	}
	
	/**
	 * {@inheritDoc getAllListView}
	 */
	public List<LabelValue> getAllListView()throws EazyBpmException{
		return listViewDao.getAllListView();
	}
	
	/*public List<LabelValue> getAllListViewWithoutVersion()throws EazyBpmException{
		return listViewDao.getAllListViewWithoutVersion();
	}*/
	
	
	/**
	 * {@inheritDoc getAllListViewTemplate}
	 */
	public List<LabelValue> getAllListViewTemplate()throws EazyBpmException{
		return listViewDao.getAllListViewTemplate();
	}
	
	/**
	 * {@inheritDoc getAllTable}
	 */
	public List<LabelValue> getAllTable()throws EazyBpmException{
		return tableService.getAllTable();
	}
	
	/**
	 * {@inheritDoc getAllMetaTable}
	 */
	public List<LabelValue> getAllMetaTable()throws EazyBpmException{
		List<String> metaTableNameList=tableDao.getAllMetaTable();
		List<LabelValue> metaLabelValueList=new ArrayList<LabelValue>();
		
		for(String metaTableName:metaTableNameList){
			LabelValue labelValue=new LabelValue();
			labelValue.setLabel(metaTableName);
			labelValue.setValue(metaTableName);
			metaLabelValueList.add(labelValue);
		}
		return metaLabelValueList;
	}
	
	/**
	 * {@inheritDoc getAllColumnsByTableName}
	 */
	public List<LabelValue> getAllColumnsByTableName(String tableName)throws EazyBpmException{
		List<String> columnTableNameList=tableDao.getAllColumnsByTableName(tableName);
		List<LabelValue> columnLabelValueList=new ArrayList<LabelValue>();
	
		for(String metaTableName:columnTableNameList){
			LabelValue labelValue=new LabelValue();
			labelValue.setLabel(metaTableName);
			labelValue.setValue(metaTableName);
			columnLabelValueList.add(labelValue);
		}
		return columnLabelValueList;
	}
	
	/**
	 * {@inheritDoc restoreListViewVersion}
	 */
	public void restoreListViewVersion(String viewId, String viewName)throws EazyBpmException{
		List<String> listViewIds=listViewDao.getListViewIdsByName(viewName);
		
		StringBuffer allListIds = new StringBuffer();
		allListIds.append("(");
		for(String listViewId: listViewIds){
			allListIds.append("'"+listViewId+"',");
		}
		
		if(allListIds.lastIndexOf(",")>0){
			allListIds.deleteCharAt(allListIds.lastIndexOf(","));
		}
		allListIds.append(")");
		
		// in activate the all the list status as inactive by view name
		listViewDao.inActivateAllListViewAndItsChild(allListIds.toString());
		// Activate the list view by list view id 
		listViewDao.activateListViewStatusById(viewId);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deleteAllListViewDetails(String listViewName)throws EazyBpmException{
		List<String> listViewIds=listViewDao.getListViewIdsByName(listViewName);
		for(String listViewId:listViewIds){
			ListView listViewObj=listViewDao.getListViewById(listViewId);
			listViewObj.getModules().removeAll(listViewObj.getModules());
			listViewDao.saveListView(listViewObj);
		}
		StringBuffer allListIds = new StringBuffer();
		allListIds.append("(");
		for(String listViewId: listViewIds){
			allListIds.append("'"+listViewId+"',");
		}
		
		if(allListIds.lastIndexOf(",")>0){
			allListIds.deleteCharAt(allListIds.lastIndexOf(","));
		}
		allListIds.append(")");
		//delete all list view and its details
		listViewDao.deleteAllListViewAndItsChild(allListIds.toString());
		
	}
	
	/**
	 * {@inheritDoc}
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	public String getListViewForInboxMail(Message[] msgs)throws EazyBpmException, MessagingException, UnsupportedEncodingException{
		List<Map<String, Object>> mailList = new ArrayList<Map<String, Object>>();
		for(int i = msgs.length-1; i >= 0; i--){
			Map<String, Object> mailDetails=new HashMap<String, Object>();
			String from = "";
			from = MimeUtility.decodeText(msgs[i].getFrom()[0].toString());
			if(from.equalsIgnoreCase("notificationadmin@mydomain.com")) {
				from = "Notification Admin";
			}
			if(!msgs[i].isSet(Flags.Flag.SEEN)){
				mailDetails.put("flagType", "<img class='header-menu-name' width='14px' height='14px' alt='refresh' src='/images/mail/new.png' />");
				mailDetails.put("from", "<b>"+from+"</b>");
			} else {
				mailDetails.put("flagType", "<img class='header-menu-name' width='14px' height='14px' alt='refresh' src='/images/mail/read.png' />");
				mailDetails.put("from", from);
			}
			//Prepare datestring
			String date=DateUtil.convertDateToString(msgs[i].getReceivedDate());
			//Prepare subject
			String msgsubject=msgs[i].getSubject();
			if(msgsubject==null || msgsubject.equals("")) {
				msgsubject="<em>"+"(no subject)"+"</em>";
			} else {
				//clean up whitespace
				msgsubject.trim();
			}
			
			mailDetails.put("subject", msgsubject);
			mailDetails.put("date", date);
			mailDetails.put("isDraft", false);
			mailDetails.put("isSent", false);
			mailDetails.put("isSentLater", false);
			mailDetails.put("messageNumber", msgs[i].getMessageNumber());
			mailDetails.put("folderName", "inbox");
			mailDetails.put("type", "<img class='header-menu-name' width='14px' height='14px' alt='mail' src='/images/easybpm/form/user_icon.png' />");
			mailList.add(mailDetails);
        }
		return generateScriptForMail("inbox", mailList);
	}
	
	/**
	 * {@inheritDoc}
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	public String getListViewForDraftMail(Message[] msgs)throws EazyBpmException, MessagingException, UnsupportedEncodingException{
		List<Map<String, Object>> mailList = new ArrayList<Map<String, Object>>();
		for(int i = msgs.length-1; i >= 0; i--){
			Map<String, Object> mailDetails=new HashMap<String, Object>();
			if(!msgs[i].isSet(Flags.Flag.SEEN)){
				mailDetails.put("flagType", "<img class='header-menu-name' width='14px' height='14px' alt='refresh' src='/images/mail/new.png' />");
			} else {
				mailDetails.put("flagType", "<img class='header-menu-name' width='14px' height='14px' alt='refresh' src='/images/mail/read.png' />");
			}
			//Prepare datestring
			String date=DateUtil.convertDateToString(msgs[i].getReceivedDate());
			//Prepare subject
			String msgsubject=msgs[i].getSubject();
			if(msgsubject==null || msgsubject.equals("")) {
				msgsubject="<em>"+"(no subject)"+"</em>";
			} else {
				//clean up whitespace
				msgsubject.trim();
			}
			if(msgs[i].getRecipients(Message.RecipientType.TO) != null && msgs[i].getRecipients(Message.RecipientType.TO).length > 0){
				mailDetails.put("from", MimeUtility.decodeText(msgs[i].getRecipients(Message.RecipientType.TO)[0].toString()));
			} else {
				mailDetails.put("from", "");
			}
			mailDetails.put("subject", msgsubject);
			mailDetails.put("date", date);
			mailDetails.put("isDraft", true);
			mailDetails.put("isSent", false);
			mailDetails.put("isSentLater", false);
			mailDetails.put("messageNumber", msgs[i].getMessageNumber());
			mailDetails.put("folderName", "draft");
			mailList.add(mailDetails);
        }
		return generateScriptForMail("draft" ,mailList);
	}
	
	/**
	 * {@inheritDoc}
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	public String getListViewForSentMail(Message[] msgs)throws EazyBpmException, MessagingException, UnsupportedEncodingException{
		List<Map<String, Object>> mailList = new ArrayList<Map<String, Object>>();
		for(int i = msgs.length-1; i >= 0; i--){
			Map<String, Object> mailDetails=new HashMap<String, Object>();
			if(!msgs[i].isSet(Flags.Flag.SEEN)){
				mailDetails.put("flagType", "<img class='header-menu-name' width='14px' height='14px' alt='refresh' src='/images/mail/new.png' />");
			} else {
				mailDetails.put("flagType", "<img class='header-menu-name' width='14px' height='14px' alt='refresh' src='/images/mail/read.png' />");
			}
			//Prepare datestring
			String date=DateUtil.convertDateToString(msgs[i].getReceivedDate());
			//Prepare subject
			String msgsubject=msgs[i].getSubject();
			if(msgsubject==null || msgsubject.equals("")) {
				msgsubject="<em>"+"(no subject)"+"</em>";
			} else {
				//clean up whitespace
				msgsubject.trim();
			}
			mailDetails.put("from", MimeUtility.decodeText(msgs[i].getRecipients(Message.RecipientType.TO)[0].toString()));
			mailDetails.put("subject", msgsubject);
			mailDetails.put("date", date);
			mailDetails.put("isDraft", false);
			mailDetails.put("isSent", true);
			mailDetails.put("isSentLater", false);
			mailDetails.put("messageNumber", msgs[i].getMessageNumber());
			mailDetails.put("folderName", "sent-mail");
			mailList.add(mailDetails);
        }
		return generateScriptForMail("sent-mail", mailList);
	}
	
	/**
	 * {@inheritDoc}
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	public String getListViewForSentLaterMessage(Message[] msgs)throws EazyBpmException, MessagingException, UnsupportedEncodingException{
		List<Map<String, Object>> mailList = new ArrayList<Map<String, Object>>();
		for(int i = msgs.length-1; i >= 0; i--){
			Map<String, Object> mailDetails=new HashMap<String, Object>();
			if(!msgs[i].isSet(Flags.Flag.SEEN)){
				mailDetails.put("flagType", "<img class='header-menu-name' width='14px' height='14px' alt='refresh' src='/images/mail/new.png' />");
			} else {
				mailDetails.put("flagType", "<img class='header-menu-name' width='14px' height='14px' alt='refresh' src='/images/mail/read.png' />");
			}
			//Prepare datestring
			String date=DateUtil.convertDateToString(msgs[i].getReceivedDate());
			//Prepare subject
			String msgsubject=msgs[i].getSubject();
			if(msgsubject==null || msgsubject.equals("")) {
				msgsubject="<em>"+"(no subject)"+"</em>";
			} else {
				//clean up whitespace
				msgsubject.trim();
			}
			mailDetails.put("from", MimeUtility.decodeText(msgs[i].getRecipients(Message.RecipientType.TO)[0].toString()));
			mailDetails.put("subject", msgsubject);
			mailDetails.put("date", date);
			mailDetails.put("isDraft", false);
			mailDetails.put("isSent", false);
			mailDetails.put("isSentLater", true);
			mailDetails.put("messageNumber", msgs[i].getMessageNumber());
			mailDetails.put("folderName", "sent-later");
			mailList.add(mailDetails);
        }
		return generateScriptForMail("sent-later", mailList);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getListViewForMailInbox(JwmaFolderImpl folder,String from)throws EazyBpmException{
		JwmaMessageInfo[] infos=folder.listMessageInfos();	
		List<Map<String, Object>> mailList = new ArrayList<Map<String, Object>>();

       
		//List<Map<String,Object>>
		for (int index=0;index<infos.length;index++) {
			boolean isDraft=false;
			Map<String, Object> mailDetails=new HashMap<String, Object>();
			
			JwmaMessageInfo msg=infos[index];
			String flagType=getMailFlag(msg,folder.getName());
			//Prepare datestring
			String date=DateUtil.convertDateToString(msg.getDate());
			if (msg.isSent()) {
				date="<i>"+date+"</i>";
			}
			//Prepare subject
			String msgsubject=msg.getSubject();
			if(msgsubject==null || msgsubject.equals("")) {
				msgsubject="<em>"+"(no subject)"+"</em>";
			} else {
				//clean up whitespace
				msgsubject.trim();
			}
			
			if (msg.isDraft()) {
				isDraft=msg.isDraft();
			}
			
			mailDetails.put("flagType", flagType);
			mailDetails.put("from", ((msg.isReceived())? msg.getFrom():("<i>"+msg.getTo()+"</i>")));
			mailDetails.put("subject", msgsubject);
			mailDetails.put("date", date);
			mailDetails.put("isDraft", isDraft);
			mailDetails.put("isSent", msg.isSent());
			mailDetails.put("messageNumber", msg.getMessageNumber());
			mailDetails.put("folderName", folder.getName());
			mailList.add(mailDetails);
		}
		return generateScriptForMail(from,mailList);
	}
	
	/**
	 * To get the mail flag
	 * @param msg
	 * @return
	 */
	private String getMailFlag(JwmaMessageInfo msg,String folderName){
		String flagType="";
		if(msg.isNew()){
			//flagType=flagType+"N";
			flagType="<img class='header-menu-name' width='14px' height='14px' alt='refresh' src='/images/mail/new.png' />";
		}
		
		if(msg.isRead()){
			//flagType=flagType+"R";
			flagType="<img class='header-menu-name' width='14px' height='14px' alt='refresh' src='/images/mail/read.png' />";
		}
		
		if(msg.isAnswered()){
			//flagType=flagType+"A";
			flagType="<img class='header-menu-name' width='14px' height='14px' alt='refresh' src='/images/mail/answered.png' />";
		}
		
		if(msg.isSent() && folderName.equals("sent-mail")){
			//flagType=flagType+"S";
			flagType="<img class='header-menu-name' width='14px' height='14px' alt='refresh' src='/images/mail/sent_mail.png' />";
		}
		
		if(msg.isDraft() && folderName.equals("Draft")){
			//flagType=flagType+"C";
			flagType="<img class='header-menu-name' width='14px' height='14px' alt='refresh' src='/images/mail/draft.png' />";
		}
		
		if(!msg.isNew() && !msg.isRead() && !msg.isAnswered() && !msg.isDeleted() && !msg.isDraft() && !msg.isSent()){
			flagType="<img class='header-menu-name' width='14px' height='14px' alt='refresh' src='/images/mail/new.png' />";
		}
		return flagType;
	}
	
	/**
	 * To generte script for mail grid
	 * @param folderName
	 * @param mailList
	 * @return
	 */
	private String generateScriptForMail(String from, List<Map<String, Object>> mailList){
		Map<String, Object> context = new HashMap<String, Object>();
		List<Map<String, Object>> mailNameList = new ArrayList<Map<String, Object>>();
		String columnNames ="";
		if(from.equalsIgnoreCase("inbox")){
			columnNames = "['状态','来自','主题','时间','isDraft','isSent','messageNumber','folderName', '类型']";
		}else{
			columnNames = "['状态','发往','主题','时间','isDraft','isSent','messageNumber','folderName']";
		}
		if(from.equalsIgnoreCase("draft")){
			context.put("title", "草稿箱");
		} else if(from.equalsIgnoreCase("sent-mail")) {
			context.put("title", "发件箱");
		} else if(from.equalsIgnoreCase("sent-later")){
			context.put("title", "延时发送");
		} else {
			context.put("title", "收件箱");
		}
		context.put("needHeader",true);
		context.put("gridId", "INBOX_LIST");
		context.put("dynamicGridWidth", "getGridWidth");
		context.put("dynamicGridHeight", "getGridHeight");
		String jsonFieldValues = "";
		if(mailList != null && !(mailList.isEmpty())){
			jsonFieldValues = CommonUtil.getJsonString(mailList);	
		}
		context.put("jsonFieldValues", jsonFieldValues);
		context.put("columnNames", columnNames);
		
		CommonUtil.createFieldNameList(mailNameList, "flagType", "50", "center", "", "false");
		CommonUtil.createFieldNameList(mailNameList, "from", "70", "left", "_mailFrom", "false");
		CommonUtil.createFieldNameList(mailNameList, "subject", "150", "left", "_mailSubject", "false");
		CommonUtil.createFieldNameList(mailNameList, "date", "80", "center", "", "false");
		CommonUtil.createFieldNameList(mailNameList, "isDraft", "50", "center", "", "true");
		CommonUtil.createFieldNameList(mailNameList, "isSent", "50", "center", "", "true");
		CommonUtil.createFieldNameList(mailNameList, "messageNumber", "60", "center", "", "true");
		CommonUtil.createFieldNameList(mailNameList, "folderName", "60", "center", "", "true");
		if(from.equalsIgnoreCase("inbox")){
			CommonUtil.createFieldNameList(mailNameList, "type", "100", "center", "", "false");
		}
		context.put("noOfRecords", "10");
		context.put("fieldNameList", mailNameList);
		context.put("myHeaderLink", true);
		context.put("needCheckbox", true);
		
		return GridUtil.generateScript(velocityEngine, context);
	}
	
	public List<String> getListViewWhereParamNames(String viewName)throws EazyBpmException{
		ListView vistView=listViewDao.getListViewDetails(viewName);
		List<String> whereparamNames=new ArrayList<String>();
		String whereString=vistView.getWhereQuery();
		whereString=whereString.toLowerCase();
		if(whereString.contains(":")){
			String[] whereSplitString = whereString.split("and");
			for(int index=0;index<whereSplitString.length;index++){
				String[] namedString=whereSplitString[index].split(":");
				if(!String.valueOf(namedString).isEmpty() && namedString.length>1){
					if(!namedString[1].contains("loggedinuser")){
						String[] whereConditionLabel=namedString[1].split(",");
						String whereCondition=whereConditionLabel[0].replace(")", "");
						whereparamNames.add(whereCondition.trim());
					}
				}
			}
			return whereparamNames;
		}else{
			return null;
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String copyListViewToModule(String moduleId,String listViewId)throws EazyBpmException{
		ListView listViewObj=listViewDao.getListViewById(listViewId);
		Set<Module> oldModules=listViewObj.getModules();
		int viewModulesSize=oldModules.size();
		
		//Get module
	   	Module module=moduleService.getModule(moduleId);
	   	if(!oldModules.contains(module)){
	   		listViewObj.addModule(module);
	   		listViewObj=listViewDao.saveListView(listViewObj);
		}else{
	   		return "List View Name : "+listViewObj.getViewName()+" is already exits in this Module : "+module.getName();
	   	}
   		
		if(listViewObj.getModules().size()>viewModulesSize){
			return "true";
		}else{
			return "false";
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<LabelValue> getAllListViewWithoutVersion()throws EazyBpmException{
		return listViewDao.getAllListViewWithoutVersion();
	}
	
	public String constructListViewDump(Set<ListView> listViews,String moduleId)throws EazyBpmException{
		StringBuffer listViewDump = new StringBuffer();
		StringBuffer moduleListViewDump = new StringBuffer();
		moduleListViewDump.append("\n\n\n\n");
		if(tableService.getDataBaseSchema().equals("mysql")){
			moduleListViewDump.append("insert  into `ETEC_MODULE_LIST_VIEW`(`list_view_id`,`module_id`) values ");
		}
		
		for(ListView listView:listViews){
			if(listView.isActive()){
				if(!tableService.getDataBaseSchema().equals("mysql")){
					moduleListViewDump.append("insert  into `ETEC_MODULE_LIST_VIEW`(`list_view_id`,`module_id`) values ");
				}
				String metaListViewDump=constructMetaListViewDump(listView);
				listViewDump.append(metaListViewDump);
				Set<ListViewColumns> listViewColumns =listView.getListViewColumns();
				Set<ListViewButtons> listViewButtons =listView.getListViewButtons();
				Set<ListViewGroupSetting> listViewGroupSettings =listView.getListViewGroupSetting();
				
				if(!listViewColumns.isEmpty()){
					listViewDump.append(constructMetaListViewColumnDump(listViewColumns));
				}
				
				if(!listViewButtons.isEmpty()){
					listViewDump.append(constructMetaListViewButtonDump(listViewButtons));
				}
				
				if(!listViewGroupSettings.isEmpty()){
					listViewDump.append(constructMetaListViewGroupDump(listViewGroupSettings));
				}
				moduleListViewDump.append("('"+listView.getId()+"','"+moduleId+"')");
				if(tableService.getDataBaseSchema().equals("mysql")){
					moduleListViewDump.append(";\n");
				}else{
					moduleListViewDump.append(",");
				}
			}
		}
		
		if(tableService.getDataBaseSchema().equals("mysql")){
			if(moduleListViewDump.lastIndexOf(",")>0){
				moduleListViewDump.deleteCharAt(moduleListViewDump.lastIndexOf(","));
			}
			moduleListViewDump.append(";");
		}
		
		if(tableService.getDataBaseSchema().equals("mysql")){
			listViewDump.append(moduleListViewDump);
			return listViewDump.toString();
		}else{
			listViewDump.append(moduleListViewDump);
			return listViewDump.toString().replaceAll("`", "");
		}
	}
	
	private String constructMetaListViewGroupDump(Set<ListViewGroupSetting> listViewGroupSettings){
		StringBuffer listViewDump = new StringBuffer();
		listViewDump.append("\n\n\n\n");
		if(tableService.getDataBaseSchema().equals("mysql")){
			listViewDump.append("insert  into `ETEC_RE_LIST_VIEW_GROUP_SETTING`(`id`,`is_active`,`comment`,`group_fields_name`,`group_name`,`group_type`,`order_by`,`parent_group`,`sort_type`,`version`,`list_view_id`) values ");
		}
		
		for(ListViewGroupSetting listViewGroupSetting:listViewGroupSettings){
			if(!tableService.getDataBaseSchema().equals("mysql")){
				listViewDump.append("insert  into `ETEC_RE_LIST_VIEW_GROUP_SETTING`(`id`,`is_active`,`comment`,`group_fields_name`,`group_name`,`group_type`,`order_by`,`parent_group`,`sort_type`,`version`,`list_view_id`) values ");
			}
			listViewDump.append("('"+listViewGroupSetting.getId()+"',"+listViewGroupSetting.isActive()+",'"+listViewGroupSetting.getGroupFieldsName()+"','"+listViewGroupSetting.getGroupName());
			listViewDump.append("','"+listViewGroupSetting.getGroupType()+"',"+listViewGroupSetting.getOrderBy()+",'"+listViewGroupSetting.getParentGroup()+"','"+listViewGroupSetting.getSortType());
			listViewDump.append("',"+listViewGroupSetting.getVersion()+",'"+listViewGroupSetting.getListView().getId()+"')");
			
			if(!tableService.getDataBaseSchema().equals("mysql")){
				listViewDump.append(";\n");
			}else{
				listViewDump.append(",");
			}
		}
		
		if(tableService.getDataBaseSchema().equals("mysql")){
			if(listViewDump.lastIndexOf(",")>0){
				listViewDump.deleteCharAt(listViewDump.lastIndexOf(","));
			}
			listViewDump.append(";");
		}
			
		return listViewDump.toString();
	}
	
	private String constructMetaListViewButtonDump(Set<ListViewButtons> listViewButtons){
		StringBuffer listViewDump = new StringBuffer();
		listViewDump.append("\n\n\n\n");
		if(tableService.getDataBaseSchema().equals("mysql")){
			listViewDump.append("insert  into `ETEC_RE_LIST_VIEW_BUTTONS`(`id`,`is_active`,`button_method`,`column_name`,`display_name`,`icon_path`,`order_by`,`redirect_value`,`table_name`,`version`,`list_view_id`) values ");
		}
		
		for(ListViewButtons listViewButton:listViewButtons){
			if(listViewButton.isActive()){
				if(!tableService.getDataBaseSchema().equals("mysql")){
					listViewDump.append("insert  into `ETEC_RE_LIST_VIEW_BUTTONS`(`id`,`is_active`,`button_method`,`column_name`,`display_name`,`icon_path`,`order_by`,`redirect_value`,`table_name`,`version`,`list_view_id`) values ");
				}
				
				listViewDump.append("('"+listViewButton.getId()+"',"+listViewButton.isActive()+",'"+listViewButton.getButtonMethod().replace("'", "\\'")+"','"+listViewButton.getColumnName());
				listViewDump.append("','"+listViewButton.getDisplayName()+"','"+listViewButton.getIconPath()+"',"+listViewButton.getOrderBy()+",'"+listViewButton.getRedirectValue());
				listViewDump.append("','"+listViewButton.getTableName()+"',"+listViewButton.getVersion()+",'"+listViewButton.getListView().getId()+"')");
				if(!tableService.getDataBaseSchema().equals("mysql")){
					listViewDump.append(";\n");
				}else{
					listViewDump.append(",");
				}
			}
		}
		
		if(tableService.getDataBaseSchema().equals("mysql")){
			if(listViewDump.lastIndexOf(",")>0){
				listViewDump.deleteCharAt(listViewDump.lastIndexOf(","));
			}
			listViewDump.append(";");
		}
		return listViewDump.toString();
	}
	
	private String constructMetaListViewColumnDump(Set<ListViewColumns> listViewColumns){
		StringBuffer listViewDump = new StringBuffer();
		listViewDump.append("\n\n\n\n");
		
		if(tableService.getDataBaseSchema().equals("mysql")){
			listViewDump.append("insert  into `ETEC_RE_LIST_VIEW_COLUMNS`(`id`,`is_active`,`column_title`,`comment`,`data_field_type`,`data_fields`,`is_advanced_search`,`is_group_by`,`is_hidden`,");
			listViewDump.append("`is_simple_search`,`is_sort`,`on_render_event`,`on_render_event_name`,`order_by`,`other_name`,`replace_words`,`text_align`,`version`,`width`,`list_view_id`,`data_dictionary`,`is_range`) values ");
		}
		
		for(ListViewColumns listViewColumn:listViewColumns){
			if(!tableService.getDataBaseSchema().equals("mysql")){
				listViewDump.append("insert  into `ETEC_RE_LIST_VIEW_COLUMNS`(`id`,`is_active`,`column_title`,`comment`,`data_field_type`,`data_fields`,`is_advanced_search`,`is_group_by`,`is_hidden`,");
				listViewDump.append("`is_simple_search`,`is_sort`,`on_render_event`,`on_render_event_name`,`order_by`,`other_name`,`replace_words`,`text_align`,`version`,`width`,`list_view_id`,`data_dictionary`,`is_range`) values ");
			}
			listViewDump.append("('"+listViewColumn.getId()+"',"+listViewColumn.isActive()+",'"+listViewColumn.getColumnTitle()+"','"+listViewColumn.getComment()+"','"+listViewColumn.getDataFieldType());
			listViewDump.append("','"+listViewColumn.getDataFields().replace("'", "\\'")+"',"+listViewColumn.getIsAdvancedSearch()+","+listViewColumn.getIsGroupBy()+","+listViewColumn.getIsHidden());
			listViewDump.append(","+listViewColumn.getIsSimpleSearch()+","+listViewColumn.getIsSort()+",'"+listViewColumn.getOnRenderEvent().replace("'", "\\'")+"','"+listViewColumn.getOnRenderEventName());
			listViewDump.append("',"+listViewColumn.getOrderBy()+",'"+listViewColumn.getOtherName()+"','"+listViewColumn.getReplaceWords()+"','"+listViewColumn.getTextAlign()+"',"+listViewColumn.getVersion());
			listViewDump.append(",'"+listViewColumn.getWidth()+"','"+listViewColumn.getListView().getId()+"'");
			listViewDump.append(",'"+listViewColumn.getDataDictionary()+"'");
			listViewDump.append(",'"+listViewColumn.getIsRange()+"')");
			if(tableService.getDataBaseSchema().equals("mysql")){
				listViewDump.append(",");
			}else{
				listViewDump.append(";\n");
			}
		}
		if(tableService.getDataBaseSchema().equals("mysql")){
			if(listViewDump.lastIndexOf(",")>0){
				listViewDump.deleteCharAt(listViewDump.lastIndexOf(","));
			}
			listViewDump.append(";");
		}
			
		return listViewDump.toString();
	}
	
	private String constructMetaListViewDump(ListView listView){
		 User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	//	 Date createdOn = new Date();
		StringBuffer listViewDump = new StringBuffer();
		listViewDump.append("\n\n\n\n");
		listViewDump.append("insert  into `ETEC_RE_LIST_VIEW`(`id`,`is_active`,`created_by`,`from_query`,`group_by`,`is_delete`,`is_filter_duplicate_data`,`is_need_checkbox`,`is_show`," +
				"`is_show_search_box`,`is_template`,`on_render_script`,`on_render_script_name`,`order_by`,`page_size`,`select_columns`,`version`,`view_name`,`where_query`) values (");
		
		listViewDump.append("'"+listView.getId()+"',"+listView.isActive()+",'"+user.getUsername()+"','"+listView.getFromQuery().replace("'", "\\'")+"','"+listView.getGroupBy());
		listViewDump.append("',"+listView.getIsDelete()+","+listView.getIsFilterDuplicateData()+","+listView.getIsNeedCheckbox()+","+listView.getIsShow()+","+listView.getIsShowSearchBox());
		listViewDump.append(","+listView.getIsTemplate()+",'"+listView.getOnRenderScript().replace("'", "\\'")+"','"+listView.getOnRenderScriptName()+"','"+listView.getOrderBy().replace("'", "\\'"));
		listViewDump.append("',"+listView.getPageSize()+",'"+listView.getSelectColumns().replace("'", "\\'")+"',"+listView.getVersion()+",'"+listView.getViewName()+"','"+listView.getWhereQuery().replace("'", "\\'")+"');");
		return listViewDump.toString();
		
	}
	
	public List<ListView> getListViewDetailsByNames(String listViewName)throws EazyBpmException{
		return listViewDao.getListViewDetailsByNames(listViewName);
	}
	
	public ListView getListViewByName(String listViewName)throws EazyBpmException{
		return listViewDao.getListViewDetails(listViewName);
	}
	
	public List<LabelValue> getListViewNames(String listViewName)throws EazyBpmException{
		return listViewDao.getListViewNames(listViewName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int generateOrderNo(String listViewId) throws EazyBpmException {	
		try {
			List<ListViewColumns> columnList = listViewDao.getColumnsPropertyByListViewId(listViewId);
			if(columnList != null && !columnList.isEmpty()) {
				Collections.sort(columnList, new ListViewColumnComparator());
				int maxOrderNo = columnList.get(columnList.size() - 1).getOrderNo();
				return maxOrderNo + 1;
			}else {
				return Constants.DEFAULT_ORDER_NO;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public boolean updateListViewColumnOrderNos(
			List<Map<String, Object>> columnList) throws EazyBpmException {
		
		boolean hasUpdated = false;
 		for (Map<String, Object> column : columnList) {
 			hasUpdated = listViewDao.updateListViewColumnOrderNos(
					(String) column.get("id"),
					Integer.parseInt((String)column.get("orderBy")));
		}

		return hasUpdated;
	}

	
}
