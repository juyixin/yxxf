package com.eazytec.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eazytec.common.util.CommonUtil;
import com.eazytec.model.DataDictionary;
import com.eazytec.model.ListView;
import com.eazytec.model.MetaForm;
import com.eazytec.model.MetaTable;
import com.eazytec.model.Module;
import com.eazytec.model.Role;
import com.eazytec.model.Process;

/**
 * @author revathi
 *
 */

public final class JSTreeUtil {
	
	/**
	 * Get JSONArray for Given module List
	 * @param request
	 * @param moduleList
	 * @param isNeedAll
	 * @return
	 * @throws JSONException
	 */
public static JSONArray getJsonTreeForModuleList(HttpServletRequest request,List<Module> moduleList,boolean isNeedAll ) throws JSONException{
	JSONArray jsonDataArray = new JSONArray();
	if(isNeedAll){
		jsonDataArray.put(CommonUtil.defaultJSONForOrganizationTree("All Module"));
	}
	for(Module module : moduleList){
		JSONObject parentdata = new JSONObject();
		JSONObject  parentAttr= new JSONObject();
		JSONObject  parentmetaData= new JSONObject();
		parentAttr.put("id", module.getId());
		parentAttr.put("name", module.getName());
		parentAttr.put("isParent", "true");
		parentdata.put("data", module.getName());
		parentdata.put("attr",parentAttr);	
		parentmetaData.put("id", module.getId());
		parentmetaData.put("isParent", "true");
		parentmetaData.put("name", module.getName());
		parentdata.put("metadata",parentmetaData);		
		jsonDataArray.put(parentdata);
	}
	return jsonDataArray;
	
}

public static JSONArray getJsonTreeForModuleWithTableList(List<Module> moduleList) throws JSONException{
	JSONArray jsonDataArray = new JSONArray();
	for(Module module : moduleList){
		JSONObject parentdata = new JSONObject();
		JSONObject parentAttr= new JSONObject();
		JSONObject parentmetaData= new JSONObject();
		parentAttr.put("id", module.getId());
		parentAttr.put("name", module.getName());
		parentmetaData.put("id", module.getId());
		parentmetaData.put("name", module.getName());
		parentdata.put("data", module.getName());
		parentdata.put("attr",parentAttr);	
		parentdata.put("metadata",parentmetaData);		
		parentdata.put("children",getJsonTreeForTableList(module.getTables(),module));
		jsonDataArray.put(parentdata);
	}
	return jsonDataArray;
	
}


public static JSONArray getJsonTreeForModuleWithRelation(List<Module> moduleList ) throws JSONException{
	JSONArray jsonDataArray = new JSONArray();
	for(Module module : moduleList){
		JSONObject parentdata = new JSONObject();
		JSONObject parentAttr= new JSONObject();
		JSONObject parentmetaData= new JSONObject();
		parentAttr.put("id", module.getId());
		parentAttr.put("nodeid", module.getId());
		parentAttr.put("name", module.getName());
		parentAttr.put("nodeType", "module");
		parentAttr.put("isEdit",module.isEdit());
		parentAttr.put("isSystemModule",module.getIsSystemModule());
		parentAttr.put("nodeOrder", module.getModuleOrder());
		parentmetaData.put("id", module.getId());
		parentmetaData.put("name", module.getName());
		parentdata.put("data", module.getName());
		parentdata.put("attr",parentAttr);	
		parentdata.put("metadata",parentmetaData);		
		parentdata.put("children",getJsonTreeModuleRelation(module));
		jsonDataArray.put(parentdata);
	}
	return jsonDataArray;
	
}

public static JSONArray getJsonTreeModuleRelation(Module module) throws JSONException{
	JSONArray jsonDataArray = new JSONArray();
	JSONObject table = defaultTable("Tables",module);
	JSONObject form = defaultForm("Forms",module);
	JSONObject listView = defaultListView("List Views", module, false, "ListViews", "listview");
	JSONObject listViewRecycle = defaultListView("Recycle", module, true, "Recycle", "recycle");
	JSONObject jspForms = defaultListView("Jsp Forms", module, true, "JspForms", "jspforms");
	jsonDataArray.put(table);
	jsonDataArray.put(form);
	jsonDataArray.put(listView);
	jsonDataArray.put(listViewRecycle);
	jsonDataArray.put(jspForms);
	return jsonDataArray;
}

public static JSONObject defaultTable(String displayName,Module module) throws JSONException{
	
	JSONObject parentdata = new JSONObject();
	JSONObject parentAttr= new JSONObject();
	JSONObject parentmetaData= new JSONObject();
	parentAttr.put("id", module.getId()+"Tables");
	parentAttr.put("nodeid", module.getId()+"Tables");
	parentAttr.put("name", displayName);
	parentAttr.put("moduleName", module.getName());
	parentAttr.put("isEdit", module.isEdit());
	parentmetaData.put("id", module.getId()+"Tables");
	parentmetaData.put("nodeid", module.getId()+"Tables");
	parentmetaData.put("name",displayName);
	parentAttr.put("nodeType", "tables");
	parentdata.put("data", displayName);
	parentdata.put("attr",parentAttr);	
	parentdata.put("metadata",parentmetaData);		
	parentdata.put("children",getJsonTreeForTableList(module.getTables(),module));
	return parentdata;
}

public static JSONObject defaultForm(String displayName,Module module) throws JSONException{
	
	JSONObject parentdata = new JSONObject();
	JSONObject parentAttr= new JSONObject();
	JSONObject parentmetaData= new JSONObject();
	parentAttr.put("id", module.getId()+"AllForm");
	parentAttr.put("nodeid", module.getId()+"AllForm");
	parentAttr.put("moduleId", module.getId());
	parentAttr.put("name", displayName);
	parentAttr.put("isEdit", module.isEdit());
	parentAttr.put("moduleName", module.getName());
	parentAttr.put("isSystemModule", module.getIsSystemModule());
	parentAttr.put("nodeType", "allform");
	parentmetaData.put("id", module.getId()+"AllForm");
	parentmetaData.put("nodeid", module.getId()+"AllForm");
	parentmetaData.put("name",displayName);
	parentmetaData.put("moduleId", module.getId());
	parentdata.put("data", displayName);
	parentdata.put("attr",parentAttr);	
	parentdata.put("metadata",parentmetaData);		
	parentdata.put("children","");
	return parentdata;
}

public static JSONObject defaultListView(String displayName,Module module, boolean isDeleted, String listViewNodeType, String nodeType) throws JSONException{
	
	JSONObject parentdata = new JSONObject();
	JSONObject parentAttr= new JSONObject();
	JSONObject parentmetaData= new JSONObject();
	parentAttr.put("id", module.getId()+""+listViewNodeType);
	parentAttr.put("nodeid", module.getId()+""+listViewNodeType);
	parentAttr.put("moduleid", module.getId());
	parentAttr.put("name", displayName);
	parentAttr.put("nodeType", nodeType);
	parentAttr.put("moduleName", module.getName());
	parentAttr.put("isEdit", module.isEdit());
	parentmetaData.put("id", module.getId()+""+listViewNodeType);
	parentmetaData.put("nodeid", module.getId()+""+listViewNodeType);
	parentmetaData.put("name",displayName);
	parentdata.put("data", displayName);
	parentdata.put("attr",parentAttr);	
	parentdata.put("metadata",parentmetaData);
	if(!isDeleted){
		parentdata.put("children",getJsonTreeForListView(module.getListViews(), module, isDeleted, nodeType));
	}else{
		parentdata.put("children","");
	}
	return parentdata;
}

public static JSONArray getJsonTreeForTableList(Set<MetaTable> tableList,Module module) throws JSONException{
	//tableList
	List<MetaTable> allTables=new ArrayList<MetaTable>();
	allTables.addAll(tableList);
	
	//Shoring the tables by order
	Collections.sort(allTables, new Comparator<MetaTable>() {
	        @Override
	        public int compare(final MetaTable object1, final MetaTable object2) {
	          	return object1.getTableName().compareTo(object2.getTableName());
	    	}
	       });
	
	JSONArray jsonDataArray = new JSONArray();
	for(MetaTable table : allTables){
		JSONObject parentdata = new JSONObject();
		JSONObject parentAttr= new JSONObject();
		JSONObject parentmetaData= new JSONObject();
		parentAttr.put("id", table.getId()+"_"+module.getId());
		parentAttr.put("nodeid", table.getId());
		parentAttr.put("name", table.getTableName());
		parentAttr.put("isEdit", module.isEdit());
		parentAttr.put("moduleId", module.getId());
		parentAttr.put("moduleName", module.getName());
		parentAttr.put("isSystemModule", module.getIsSystemModule());
		parentAttr.put("nodetype", "table");
		parentmetaData.put("id", table.getId()+"_"+module.getId());
		parentmetaData.put("nodeid", table.getId());
		parentmetaData.put("name", table.getTableName());
		
		String cname = "";
		if(StringUtils.isNotBlank(table.getChineseName())){
			cname = "("+table.getChineseName()+")";
		}
		
		parentdata.put("data", table.getTableName()+cname);
		parentdata.put("attr",parentAttr);	
		parentdata.put("metadata",parentmetaData);		
		jsonDataArray.put(parentdata);
	}
	return jsonDataArray;
	
}

public static JSONArray getJsonTreeForFormList(Set<MetaForm> formList ) throws JSONException{
	JSONArray jsonDataArray = new JSONArray();
	for(MetaForm form : formList){
		JSONObject parentdata = new JSONObject();
		JSONObject parentAttr= new JSONObject();
		JSONObject parentmetaData= new JSONObject();
		parentAttr.put("id", form.getId());
		parentAttr.put("name", form.getFormName());
		parentmetaData.put("id", form.getId());
		parentmetaData.put("name", form.getFormName());
		parentdata.put("data", form.getFormName());
		parentdata.put("attr",parentAttr);	
		parentdata.put("metadata",parentmetaData);		
		jsonDataArray.put(parentdata);
	}
	return jsonDataArray;
	
}

public static JSONArray getJsonTreeForListView(Set<ListView> listViews,Module module, boolean isDeleted, String nodeType) throws JSONException{
	JSONArray jsonDataArray = new JSONArray();
	for(ListView listView : listViews){
		if(listView.isActive() && listView.getIsShow()){
			if(isDeleted){
				if(listView.getIsDelete() == true){
					jsonDataArray.put(jsonTreeForListViewMapping(listView, module, nodeType));	
				}
			}else{
				if(listView.getIsDelete() == false){
					jsonDataArray.put(jsonTreeForListViewMapping(listView, module, nodeType));
				}
			}
		}
	}
	return jsonDataArray;
}

public static JSONObject jsonTreeForListViewMapping(ListView listView,Module module, String nodeType) throws JSONException{	
	JSONObject parentdata = new JSONObject();
	JSONObject parentAttr= new JSONObject();
	JSONObject parentmetaData= new JSONObject();
	parentAttr.put("id", listView.getId()+"_"+module.getId());
	parentAttr.put("name", listView.getViewName());
	parentAttr.put("nodeid", listView.getId());
	parentAttr.put("moduleId", module.getId());
	parentAttr.put("moduleName", module.getName());
	parentAttr.put("isSystemModule", module.getIsSystemModule());
	parentAttr.put("isEdit", module.isEdit());
	parentAttr.put("nodetype", nodeType);
	parentmetaData.put("nodeid",listView.getId());
	parentmetaData.put("id", listView.getId());
	parentmetaData.put("name", listView.getViewName());
	parentdata.put("data", listView.getViewName());
	parentdata.put("attr",parentAttr);	
	parentdata.put("metadata",parentmetaData);
	return parentdata;
}
/**
 * Get JSONArray for given role list
 * @param request
 * @param roleList
 * @return
 * @throws JSONException
 */
public  static JSONArray getJsonTreeForRoleList(HttpServletRequest request,List<Role> roleList) throws JSONException{
	JSONArray jsonDataArray = new JSONArray();
	jsonDataArray.put(CommonUtil.defaultJSONForOrganizationTree("All Role"));
	for(Role role : roleList){
		JSONObject parentdata = new JSONObject();
		JSONObject  parentAttr= new JSONObject();
		JSONObject  parentmetaData= new JSONObject();
		parentAttr.put("id", role.getName());
		parentAttr.put("name", role.getName());
		parentdata.put("data", role.getName());
		parentdata.put("attr",parentAttr);	
		parentmetaData.put("id", role.getId());
		parentmetaData.put("name", role.getName());
		parentdata.put("metadata",parentmetaData);		
		jsonDataArray.put(parentdata);
	}
	return jsonDataArray;
	
}

/**
 * Get JSONArray for given form list
 * @param request
 * @param formList
 * @return
 * @throws JSONException
 */
public static JSONArray getJsonTreeForformList(HttpServletRequest request,List<MetaForm> formList,boolean isNeedAll) throws JSONException{
	JSONArray jsonDataArray = new JSONArray();
	if(isNeedAll){
		jsonDataArray.put(CommonUtil.defaultJSONForOrganizationTree("All Form"));
	}
	for(MetaForm form : formList){
		JSONObject parentdata = new JSONObject();
		JSONObject  parentAttr= new JSONObject();
		JSONObject  parentmetaData= new JSONObject();
		parentAttr.put("id", form.getFormName());
		parentAttr.put("name", form.getFormName());
		parentdata.put("data", form.getFormName());
		parentdata.put("attr",parentAttr);	
		parentmetaData.put("id", form.getId());
		parentmetaData.put("name", form.getFormName());
		parentdata.put("metadata",parentmetaData);		
		jsonDataArray.put(parentdata);
	}
	return jsonDataArray;
	
}

/**
 * Get JSONArray for given process list
 * @param request
 * @param formList
 * @return
 * @throws JSONException
 */
public static JSONArray getJsonTreeForProcessList(HttpServletRequest request,List<Process> processList,boolean isNeedAll) throws JSONException{
	JSONArray jsonDataArray = new JSONArray();
	if(isNeedAll){
		jsonDataArray.put(CommonUtil.defaultJSONForOrganizationTree("All Process"));
	}
	for(Process process : processList){
		JSONObject data = new JSONObject();
		JSONObject  attributes= new JSONObject();
		JSONObject  metaData= new JSONObject();
		attributes.put("id", process.getName());
		attributes.put("name", process.getName());
		data.put("data", process.getName());
		data.put("attr",attributes);	
		metaData.put("id", process.getId());
		metaData.put("name", process.getName());
		data.put("metadata",metaData);		
		jsonDataArray.put(data);
	}
	return jsonDataArray;
	
}

	/**
	 * For Constructing Default dictionary
	 * @param id
	 * @param displayName
	 * @param dataDictionaryList
	 * @return
	 * @throws JSONException
	 */
	public static JSONArray constructDefaultDataDictionaryJson(String id,String displayName,List<DataDictionary> dataDictionaryList) throws JSONException{
		JSONArray jsonDataArray = new JSONArray();
		JSONObject parentdata = new JSONObject();
		JSONObject parentAttr= new JSONObject();
		JSONObject parentmetaData= new JSONObject();
		parentAttr.put("id",id);
		parentAttr.put("name",displayName);
		parentAttr.put("parent",displayName);
		parentmetaData.put("id",id);
		parentmetaData.put("name",displayName);
		parentdata.put("data", displayName);
		parentdata.put("attr",parentAttr);	
		parentdata.put("metadata",parentmetaData);		
		parentdata.put("children",constructChildDataDictionaryJson(dataDictionaryList));
		jsonDataArray.put(parentdata);
		return jsonDataArray;
	}

	/**
	 * 
	 * @param dataDictionaryList
	 * @return
	 * @throws JSONException
	 */
	public static JSONArray constructChildDataDictionaryJson(List<DataDictionary> dataDictionaryList) throws JSONException{
		JSONArray jsonDataArray = new JSONArray();
		for(DataDictionary dataDictionary : dataDictionaryList){
			JSONObject parentdata = new JSONObject();
			JSONObject parentAttr= new JSONObject();
			JSONObject parentmetaData= new JSONObject();
			parentAttr.put("id", dataDictionary.getId());
			parentAttr.put("name", dataDictionary.getName());
			parentAttr.put("parent", dataDictionary.getParentDictId());
			parentmetaData.put("id", dataDictionary.getId());
			parentmetaData.put("name", dataDictionary.getName());
			parentdata.put("data", dataDictionary.getName());
			parentdata.put("attr",parentAttr);	
			parentdata.put("metadata",parentmetaData);		
			jsonDataArray.put(parentdata);
		}
		return jsonDataArray;
	}

}
