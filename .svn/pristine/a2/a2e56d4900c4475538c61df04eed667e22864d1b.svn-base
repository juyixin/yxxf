/**
 * @author Rajasekar and Karthick 
 * 
 * This script file contains all the 
 * functions that are used in
 * rendering process modeler components.
 */
var reinitializedFlag = 0;
var resourcesAry = new Array();
var performerAry = new Array();
var singleIdCheck = 0;
var documentIdCheck = 0;
var multiDocumentIdCheck = 0;
var multiIdCheck = 0;
var previousStartPermissionFormId = "";
var previousStartDynamicFormId = "";
var previousFormFieldAutomatic = "";
var previousFormId = "";
var previousorganizerFormId = "";
var previouscoordinatorFormId = "";
var previousreaderFormId = "";
var previouscreatorFormId = "";
var previousprocesseduserFormId = "";
var previousworkflowadministratorFormId = "";
var previousadminFormId = "";
var previousDynamicTransactororganizerFormId = "";
var previousDynamicTransactorreaderFormId = "";


/**
 * 
 */
var orderStore = new Ext.data.JsonStore({
	fields: [{
        name: 'resource_type',
        mapping: 'resource_type'
    }, {
        name: 'resource',
        mapping: 'resource'
    }, {
        name: 'resourceassignmentexpr',
        mapping: 'resourceassignmentexpr'
    }, {
        name: 'evaluatestotype',
        mapping: 'evaluatestotype'
    }
],
    root: 'items',
});

/**
 * Loads the tree view for the process modeler resources attribute.
 * show List of Group
 * 
 * @param treeLoadType
 * @param resourceTypeName
 */
function loadOwnerTree(treeLoadType,resourceTypeName) {
	
	var groupTree = new jktreeview("tree2");
	var groups_root = groupTree.addItem("Groups", "");
	
	
	Ext.Ajax.request({
		waitMsg: 'Saving changes...',
		method: 'GET',
		headers: {'Content-Type': 'application/json'},
		url:'/admin/getAllGroups',
		failure:function(response){
			//alert("failure");

			Ext.Msg.show({
				title:'Message',
				msg: ORYX.I18N.Feedback.failure,
				buttons: Ext.Msg.OK,
				animEl: 'elId',
				icon: Ext.MessageBox.ERROR
			});
		},
		success:function(response){
			var responseObj = Ext.util.JSON.decode(response.responseText);
			if(responseObj != null) {
				for(var i=0;i<responseObj.result.length;i++) {
					groupTree.addItem(responseObj.result[i].name,groups_root,"#","showSelectedGroup('"+responseObj.result[i].id+"','groups','"+treeLoadType+"','"+resourceTypeName+"')");
				}
				groupTree.treetop.draw();
			}
		}
	}); 
		
}

/**
 * Loads the tree view for the process modeler resources attribute.
 * show List of Role
 * 
 * @param treeLoadType
 * @param resourceTypeName
 */
function loadOwnerRoleTree(treeLoadType,resourceTypeName) {
	
	var roleTree = new jktreeview("tree2");
	var roles_root = roleTree.addItem("Roles", "");
	
	
	Ext.Ajax.request({
		waitMsg: 'Saving changes...',
		method: 'GET',
		headers: {'Content-Type': 'application/json'},
		url:'/bpm/admin/getAllRoleNames',
		failure:function(response){
			//alert("failure");

			Ext.Msg.show({
				title:'Message',
				msg: ORYX.I18N.Feedback.failure,
				buttons: Ext.Msg.OK,
				animEl: 'elId',
				icon: Ext.MessageBox.ERROR
			});
		},
		success:function(response){
			var responseObj = Ext.util.JSON.decode(response.responseText);
			if(responseObj != null) {
				for(var i=0;i<responseObj.length;i++) {
				    roleTree.addItem(responseObj[i].name,roles_root,"#","showSelectedRole('"+responseObj[i].id+"','roles','"+treeLoadType+"','"+resourceTypeName+"')");
				}
				roleTree.treetop.draw();
			}
		}
	}); 
		
}

/**
 * Loads the tree view for the process modeler resources attribute.
 * show List of Department
 * @param treeLoadType
 * @param resourceTypeName
 */
function loadOwnerDepartmentTree(treeLoadType,resourceTypeName) {
	
	var departmentTree = new jktreeview("tree2");
	var department_root = departmentTree.addItem("Departments", "");
	
	
	Ext.Ajax.request({
		waitMsg: 'Saving changes...',
		method: 'GET',
		headers: {'Content-Type': 'application/json'},
		url:'/bpm/admin/getAllDepartmentNames',
		failure:function(response){
			//alert("failure");
			Ext.Msg.show({
				title:'Message',
				msg: ORYX.I18N.Feedback.failure,
				buttons: Ext.Msg.OK,
				animEl: 'elId',
				icon: Ext.MessageBox.ERROR
			});
		},
		success:function(response){
			var responseObj = Ext.util.JSON.decode(response.responseText);
			if(responseObj != null) {
				for(var i=0;i<responseObj.length;i++) {
					departmentTree.addItem(responseObj[i].name,department_root,"#","showSelectedDepartment('"+responseObj[i].id+"','departments','"+treeLoadType+"','"+resourceTypeName+"')");
				}
				departmentTree.treetop.draw();
			}
		}
	});
		
}

/**
 * Loads the performer tree
 * This tree contains all the root and children related to the performer type resources.
 * 
 * @param treeLoadType
 * @param resourceTypeName
 */
function loadPerformerTree(treeLoadType,resourceTypeName, nodeType) {
	var performerTree = new jktreeview("tree2");
	var roles_root = performerTree.addItem("Roles","");
	var department_root = performerTree.addItem("Departments","");
	var groups_root = performerTree.addItem("Groups", "");
	//alert("========="+treeLoadType+"========="+resourceTypeName+"========="+nodeType);
	Ext.Ajax.request({
		waitMsg: 'Saving changes...',
		method: 'GET',
		headers: {'Content-Type': 'application/json'},
		url:'/bpm/admin/getAllRoleNames',
		failure:function(response){
			//alert("failure");
			Ext.Msg.show({
				title:'Message',
				msg: ORYX.I18N.Feedback.failure,
				buttons: Ext.Msg.OK,
				animEl: 'elId',
				icon: Ext.MessageBox.ERROR
			});
		},
		success:function(response){
			var responseObj = Ext.util.JSON.decode(response.responseText);
			if(responseObj != null) {
				for(var i=0;i<responseObj.length;i++) {
					performerTree.addItem(responseObj[i].name,roles_root,"#","getAllUsersByCategory('"+responseObj[i].id+"','roles','"+treeLoadType+"','"+resourceTypeName+"', '"+nodeType+"')");
				}
				performerTree.treetop.draw();
			}
		}
	});
	
	Ext.Ajax.request({
		waitMsg: 'Saving changes...',
		method: 'GET',
		headers: {'Content-Type': 'application/json'},
		url:'/admin/getAllParentGroups',
		failure:function(response){
			//alert("failure");
			Ext.Msg.show({
				title:'Message',
				msg: ORYX.I18N.Feedback.failure,
				buttons: Ext.Msg.OK,
				animEl: 'elId',
				icon: Ext.MessageBox.ERROR
			});
		},
		success:function(response){
			var responseObj = Ext.util.JSON.decode(response.responseText);
			if(responseObj != null) {
				for(var i=0;i<responseObj.result.length;i++) {
					performerTree.addItem(responseObj.result[i].name,groups_root,"#","getAllUsersByCategory('"+responseObj.result[i].id+"','groups','"+treeLoadType+"','"+resourceTypeName+"', '"+nodeType+"')");
				}
				performerTree.treetop.draw();
			}
		}
	}); 
	
	performerTree.addItem("Organization",department_root,"#","getAllUsersByCategory('Organization','departments','"+treeLoadType+"','"+resourceTypeName+"', '"+nodeType+"')");
	//performerTree.addItem('exelanz',groups_root,"#","getAllUsersByCategory('exelanz','groups','"+treeLoadType+"','"+resourceTypeName+"', '"+nodeType+"')");
		/*Ext.Ajax.request({
			waitMsg: 'Saving changes...',
			method: 'GET',
			headers: {'Content-Type': 'application/json'},
			url:'/bpm/admin/getAllDepartmentNames',
			failure:function(response){
				//alert("failure");
				Ext.Msg.show({
					title:'Message',
					msg: "failure. ",
					buttons: Ext.Msg.OK,
					animEl: 'elId',
					icon: Ext.MessageBox.ERROR
				});
			},
			success:function(response){
				var responseObj = Ext.util.JSON.decode(response.responseText);
				if(responseObj != null) {
					for(var i=0;i<responseObj.length;i++) {
						performerTree.addItem(responseObj[i].name,department_root,"#","getAllUsersByCategory('"+responseObj[i].id+"','departments','"+treeLoadType+"','"+resourceTypeName+"', '"+nodeType+"')");
					}
					performerTree.treetop.draw();
				}
			}
		});*/	
}

/**
 * A function that load the specific tree based on the resource option selected
 * Default resource_options
 * @param resourceOption
 * @param treeLoadType
 * @param resourceTypeName
 */
function loadTree(resourceOption,treeLoadType,resourceTypeName, nodeType) {
	if(resourceOption != undefined && resourceOption != "" && resourceOption != null) {
		loadSpecificTree(resourceOption,treeLoadType,resourceTypeName, nodeType);
	} else if(document.getElementById("resources_options") != null) {
		loadSpecificTree(document.getElementById("resources_options").value,treeLoadType,resourceTypeName, nodeType);
	}
	
}

/**
 * A function that loads the specific tree based on the resource option selected
 * eg: Humanperformer or Potentialowner
 * @param resourceValue
 * @param treeLoadType
 * @param resourceTypeName
 */
function loadSpecificTree(resourceValue,treeLoadType,resourceTypeName, nodeType) {
	if(resourceValue.toLowerCase() == "potentialowner" ) {
		var e = document.getElementById("resources_options");
		var roleType = e.options[e.selectedIndex].text;
		if(roleType == "Role"){
			loadOwnerRoleTree(treeLoadType,resourceTypeName);
		}else if(roleType == "Department"){
			loadOwnerDepartmentTree(treeLoadType,resourceTypeName);
		}else{
			// if role type Group
			loadOwnerTree(treeLoadType,resourceTypeName);
		}
	} else {
		loadPerformerTree(treeLoadType,resourceTypeName, nodeType);
	}
}

/**
 * 
 * @param sourceId
 * @param category
 * @param treeLoadType
 * @param resourceTypeName
 */
function getAllUsersByCategory(sourceId,category,treeLoadType,resourceTypeName, nodeType) {
	showResourceTreeDiv();
	
	Ext.Ajax.request({
		waitMsg: 'Saving changes...',
		method: 'GET',
		headers: {'Content-Type': 'application/json'},
		url:'/admin/getRoleUsers',
		params: {id:sourceId,parentNode:category},
		failure:function(response){
			//alert("failure");
			Ext.Msg.show({
				title:'Message',
				msg: ORYX.I18N.Feedback.failure,
				buttons: Ext.Msg.OK,
				animEl: 'elId',
				icon: Ext.MessageBox.ERROR
			});
		},
		success:function(response){
			var menuData = "<div class='padding5' id='userList'>";
			var responseObj = Ext.util.JSON.decode(response.responseText);
			if(responseObj != null) {
				menuData = renderResourcesView(responseObj,menuData,sourceId,resourceTypeName, nodeType);
				if(responseObj.length > 0) {
					if(treeLoadType=="fieldPermission"){
					menuData += "<div class='floatRight'><input type='button' style='font-size: 11px;padding: 2px 7px;' name='add' id='add_btn' value='Add' onclick='addSelectedResources(\""+category+"\",\""+resourceTypeName+"\")' class='btn-primary btn'></input> &nbsp;<input type='button'  name='view' id='view_btn' value='View' onclick='viewSelectedResources(\""+resourceTypeName+"\")' class='btn-primary btn' style='font-size: 11px;padding: 2px 7px;'></input></div>";
					}else{
					menuData += "<div class='floatRight'><input type='button'  name='add' id='add_btn' value='Add/Remove' onclick='setSelectedResources(\""+category+"\",\""+resourceTypeName+"\")'class='btn-primary btn' style='font-size: 11px;padding: 2px 7px;'></input></div>";
					}
				}
				menuData += "</div><div class='padding5' id='userListView' style='display:none;'></div><div id ='hidden_div'><input type='hidden' name='userDetail' id='"+sourceId+"' value='"+sourceId+"' /></div>";
	    			document.getElementById('userInfo_').innerHTML = menuData;
			}
		}
	});  	
}

/**
 * 
 * @param groupId
 * @param category
 * @param treeLoadTypememberJsonString += "roletype" + ":'" + aRecord.get("roletype") + "'";
 * @param resourceTypeName
 */
function showSelectedGroup(groupId,category,treeLoadType,resourceTypeName) {
	var resourceStoreMember = getExtjsJsonObjByNameMembers(resourceTypeName);
	showResourceTreeDiv();
	var menuData = "<div class='padding5' id='userList'>";
	if(groupId != null) {
		var recordIndex = findARecordInStore(document.getElementById("resources_options").value,groupId,groupId,resourceStoreMember);
		menuData += "<div class='overflow height250'>";
		if(recordIndex == -1) {
			menuData += "<input type='checkbox' name='userDetail' id='"+groupId+"' value='"+groupId+"'><span class='pad-L5'>"+groupId+"</span></input><br>";
		}else {
			menuData += "<input type='checkbox' name='userDetail' id='"+groupId+"' value='"+groupId+"' checked='checked'><span class='pad-L5' >"+groupId+"</span></input><br>";
		}
		menuData += "</div>";
		if(treeLoadType=="fieldPermission"){
			menuData += "<div class='floatRight' ><input type='button'  name='add' id='add_btn' value='Add' class='btn-primary btn' style='font-size: 11px;padding: 2px 7px;' onclick='addSelectedResources(\""+category+"\",\""+resourceTypeName+"\")'></input> &nbsp; <input type='button'  name='view' id='view_btn' value='View' class='btn-primary btn' style='font-size:11px;padding:2px 7px;' onclick='viewSelectedResources(\""+resourceTypeName+"\")'></input></div></div><div id ='hidden_div'><input type='hidden' name='userDetail' id='"+groupId+"' value='"+groupId+"' /></div><div class='padding5' id='userListView' style='display:none;'></div>";
		}else{
			menuData += "<div  class='floatRight' '><input type='button'  name='add' id='add_btn' value='Add/Remove' class='btn-primary btn' style='font-size: 11px;padding: 2px 7px;' onclick='setSelectedResources(\""+category+"\",\""+resourceTypeName+"\")'></input></div></div><div id ='hidden_div'><input type='hidden' name='userDetail' id='"+groupId+"' value='"+groupId+"' /></div>";
		}
		 document.getElementById('userInfo_').innerHTML = menuData;
	}
}

/**
 * 
 * @param roleId
 * @param category
 * @param treeLoadType
 * @param resourceTypeName
 */
function showSelectedRole(roleId,category,treeLoadType,resourceTypeName) {
	var resourceStoreMember = getExtjsJsonObjByNameMembers(resourceTypeName);
	showResourceTreeDiv();
	var menuData = "<div class='padding5' id='userList'>";
	if(roleId != null) {
		var recordIndex = findARecordInStore(document.getElementById("resources_options").value,roleId,roleId,resourceStoreMember);
		menuData += "<div class='overflow height250'>";
		if(recordIndex == -1) {
			menuData += "<input type='checkbox' name='userDetail' id='"+roleId+"' value='"+roleId+"'><span class='pad-L5'>"+roleId+"</span></input><br>";
		}else {
			menuData += "<input type='checkbox' name='userDetail' id='"+roleId+"' value='"+roleId+"' checked='checked'><span class='pad-L5' >"+roleId+"</span></input><br>";
		}
		menuData += "</div>";
		if(treeLoadType=="fieldPermission"){
			menuData += "<div class='floatRight' ><input type='button'  name='add' id='add_btn' value='Add' class='btn-primary btn' style='font-size: 11px;padding: 2px 7px;' onclick='addSelectedResources(\""+category+"\",\""+resourceTypeName+"\")'></input> &nbsp; <input type='button'  name='view' id='view_btn' value='View' class='btn-primary btn' style='font-size: 11px;padding: 2px 7px;' onclick='viewSelectedResources(\""+resourceTypeName+"\")'></input></div></div><div id ='hidden_div'><input type='hidden' name='userDetail' id='"+roleId+"' value='"+roleId+"' /></div><div class='padding5' id='userListView' style='display:none;'></div>";
		}else{
			menuData += "<div  class='floatRight' '><input type='button'  name='add' id='add_btn' value='Add/Remove' class='btn-primary btn' style='font-size: 11px;padding: 2px 7px;' onclick='setSelectedResources(\""+category+"\",\""+resourceTypeName+"\")'></input></div></div><div id ='hidden_div'><input type='hidden' name='userDetail' id='"+roleId+"' value='"+roleId+"' /></div>";
		}
		 document.getElementById('userInfo_').innerHTML = menuData;
	}
}

/**
 * 
 * @param departmentId
 * @param category
 * @param treeLoadType
 * @param resourceTypeName
 */
function showSelectedDepartment(departmentId,category,treeLoadType,resourceTypeName) {
	var resourceStoreMember = getExtjsJsonObjByNameMembers(resourceTypeName);
	showResourceTreeDiv();
	var menuData = "<div class='padding5' id='userList'>";
	if(departmentId != null) {
		var recordIndex = findARecordInStore(document.getElementById("resources_options").value,departmentId,departmentId,resourceStoreMember);
		menuData += "<div class='overflow height250'>";
		if(recordIndex == -1) {
			menuData += "<input type='checkbox' name='userDetail' id='"+departmentId+"' value='"+departmentId+"'><span class='pad-L5'>"+departmentId+"</span></input><br>";
		}else {
			menuData += "<input type='checkbox' name='userDetail' id='"+departmentId+"' value='"+departmentId+"' checked='checked'><span class='pad-L5' >"+departmentId+"</span></input><br>";
		}
		menuData += "</div>";
		if(treeLoadType=="fieldPermission"){
			menuData += "<div class='floatRight' ><input type='button'  name='add' id='add_btn' value='Add' class='btn-primary btn' style='font-size: 11px;padding: 2px 7px;' onclick='addSelectedResources(\""+category+"\",\""+resourceTypeName+"\")'></input> &nbsp; <input type='button'  name='view' id='view_btn' value='View' class='btn-primary btn' style='font-size: 11px;padding: 2px 7px;' onclick='viewSelectedResources(\""+resourceTypeName+"\")'></input></div></div><div id ='hidden_div'><input type='hidden' name='userDetail' id='"+departmentId+"' value='"+departmentId+"' /></div><div class='padding5' id='userListView' style='display:none;'></div>";
		}else{
			menuData += "<div  class='floatRight' '><input type='button'  name='add' id='add_btn' value='Add/Remove' class='btn-primary btn' style='font-size: 11px;padding: 2px 7px;' onclick='setSelectedResources(\""+category+"\",\""+resourceTypeName+"\")'></input></div></div><div id ='hidden_div'><input type='hidden' name='userDetail' id='"+departmentId+"' value='"+departmentId+"' /></div>";
		}
		 document.getElementById('userInfo_').innerHTML = menuData;
	}
}

/**
 * 
 * @param resourceTypeName
 */
function viewSelectedResources(resourceTypeName){
	var resourceStoreMember = getExtjsJsonObjByNameMembers(resourceTypeName);
	if(resourceStoreMember.getCount() >= 0){
		var selectView = "<div id='viewSelectedResourceDiv'><div class='overflow height250'>";
		selectView += "<table border='0' cellpadding='1' cellspacing='1' class='line-height20 width-per-100'>";
		selectView += "<tbody>";	
		selectView += "<tr class='width-per-100'>";
		selectView += "<td><b>Parent</b></td>";
		selectView += "<td class='pad-L5'><b>Resource</b></td>";
		selectView += "</tr>";
		for(var i=0; i<resourceStoreMember.getCount(); i++) {
			var aRecord = resourceStoreMember.getAt(i);
			
			selectView += "<tr class='width-per-100'>";
			selectView += '<td><img class="pad-R5" src="/images/easybpm/form/close_btn.jpg" onclick="removeSelectedResources(\''+aRecord.get("resource_type")+'\',\''+aRecord.get("resource")+'\',\''+aRecord.get("resourceassignmentexpr")+'\',\''+resourceTypeName+'\');">'+aRecord.get("resource")+'</td>';
			if(aRecord.get("resource") == aRecord.get("resourceassignmentexpr")){
				selectView += "<td class='pad-L5'>"+aRecord.get("resource")+"</td>";
			}else{
				selectView += "<td class='pad-L5'>"+aRecord.get("fullName")+"</td>";
			}
			
			
			selectView += "</tr>";
		}
		selectView += "</tbody></table></div>";
		selectView += "<div class='floatRight'><input type='button'  name='showTree' id='show_tree_btn' value='Show Tree' class='btn-primary btn' style='font-size: 11px;padding: 2px 7px;' onclick='showTreeSelectedResources()'></input></div></div>";
		document.getElementById("userList").style.display="none";
		document.getElementById("userListView").style.display="block";
		document.getElementById("userListView").innerHTML = selectView;
	}/*else{
		document.getElementById("userList").style.display="block";
		document.getElementById("userListView").style.display="none";
		alert("Select at least one user");
	}*/
}

/**
 * 
 * @param resourceTypeName
 */
function showSelectedResources(resourceTypeName){
	var resourceStoreMember = getExtjsJsonObjByNameMembers(resourceTypeName);
	if(resourceStoreMember.getCount() >= 0){
		var selectView = "<div id='viewSelectedResourceDiv'><div class='overflow height250'>";
		selectView += "<table border='0' cellpadding='1' cellspacing='1' class='line-height20 width-per-100'>";
		selectView += "<tbody>";	
		selectView += "<tr class='width-per-100'>";
		selectView += "<td><b>Parent</b></td>";
		selectView += "<td class='pad-L5'><b>Resource</b></td>";
		selectView += "</tr>";
		for(var i=0; i<resourceStoreMember.getCount(); i++) {
			var aRecord = resourceStoreMember.getAt(i);

			selectView += "<tr class='width-per-100'>";
			selectView += '<td><img class="pad-R5" src="/images/easybpm/form/close_btn.jpg" onclick="removeSelectedResources(\''+aRecord.get("resource_type")+'\',\''+aRecord.get("resource")+'\',\''+aRecord.get("resourceassignmentexpr")+'\',\''+resourceTypeName+'\');">'+aRecord.get("resource")+'</td>';
			if(aRecord.get("resource") == aRecord.get("resourceassignmentexpr")){
				selectView += "<td class='pad-L5'>"+aRecord.get("resourceassignmentexpr")+"</td>";
			} else {
				selectView += "<td class='pad-L5'>"+aRecord.get("fullName")+"</td>";
			}
			
			
			selectView += "</tr>";
		}
		selectView += "</tbody></table></div>";
		
		//document.getElementById("userList").style.display="none";
		document.getElementById("userInfo_").style.display="block";
		document.getElementById("userInfo_").innerHTML = selectView;
	}
}

/**
 * 
 * @param cntRoleType
 * @param resourceTypeName
 */
function addSelectedResources(cntRoleType,resourceTypeName){
	
	var resourceStoreMember = getExtjsJsonObjByNameMembers(resourceTypeName);
    var resourceRecord = resourceStoreMember.recordType;
    var hiddenElem = document.getElementById("hidden_div").getElementsByTagName("input");
    var hiddenResource = "";
    if (hiddenElem != undefined) {
        hiddenResource = hiddenElem[0].value;
    }
    var selected = false;
    var elements = document.getElementById("userList").getElementsByTagName("input");
    var fullNames = document.getElementById("userList").getElementsByTagName("span");
    for (var i = 0; i < elements.length; i++) {
		if(elements[i].name != "userDetailSuperior" && elements[i].name != "userDetailSubordinate"){
		    if ((elements[i].type == 'checkbox' || elements[i].type == 'radio') && elements[i].checked == true) {
				
				//checks for a record in the store. returns the index of the record or -1 if no match found
				var recordIndex = findARecordInStore(document.getElementById("resources_options").value,hiddenResource,elements[i].value,resourceStoreMember);
			
		        if (recordIndex == -1) {
					var superior = elements[i].value+"_superior";
					var subordinate = elements[i].value+"_subordinate";
					var superiorValue = "false";
					var subordinateValue = "false";
					if( document.getElementById(superior)){
						superiorValue = document.getElementById(superior).checked;
					}
					if(document.getElementById(subordinate)){
						subordinateValue = document.getElementById(subordinate).checked;
					}
		            var aRecordVal = new resourceRecord({
		                resource_type: document.getElementById("resources_options").value,
		                resource: hiddenResource,
		                resourceassignmentexpr: elements[i].value,
		                language: '',
		                evaluatestotype: '',
						roletype: cntRoleType,
						superior: superiorValue,
						subordinate: subordinateValue,
						fullName:fullNames[i].innerHTML,
		            });
		            resourceStoreMember.add(aRecordVal);
				selected = true;
				

		        }
			Ext.Msg.show({
				   title:'Information',
				   msg: ORYX.I18N.PropertyWindow.memberAddedSucc,
				   buttons: Ext.Msg.OK,
				   animEl: 'elId',
				   icon: Ext.MessageBox.Info
				});
			//return true;
		    } else if((elements[i].type == 'checkbox' || elements[i].type == 'radio') && elements[i].checked == false && !selected) {
		    	var recordIndex = findARecordInStore(document.getElementById("resources_options").value,hiddenResource,elements[i].value,resourceStoreMember);
		    	
		    	if(recordIndex != -1) {
		    		resourceStoreMember.remove(resourceStoreMember.getAt(recordIndex));
		    		resourceStoreMember.commitChanges();
		    	}
			Ext.Msg.show({
				   title:'Information',
				   msg: ORYX.I18N.PropertyWindow.selectMember,
				   buttons: Ext.Msg.OK,
				   animEl: 'elId',
				   icon: Ext.MessageBox.Info
				});
			
			//return false;
		    }
		}
    }
		
   // viewSelectedResources(resourceTypeName);
}

/**
 * 
 * @param resources_options
 * @param hiddenResource
 * @param resourceassignmentexpr
 * @param resourceTypeName
 */
function removeSelectedResources(resources_options,hiddenResource,resourceassignmentexpr,resourceTypeName){
   var resourceStoreMember = getExtjsJsonObjByNameMembers(resourceTypeName);
    var recordIndex = findARecordInStore(resources_options,hiddenResource,resourceassignmentexpr,resourceStoreMember);
	if(recordIndex != -1) {
		resourceStoreMember.remove(resourceStoreMember.getAt(recordIndex));
		resourceStoreMember.commitChanges();
	}
	if(resourceTypeName == ORYX.I18N.PropertyWindow.organizer){
		renderResourceOrderView(resourceTypeName, '');
	}else{
		showSelectedResources(resourceTypeName);
	}
   	//renderResourceOrderView(resourceTypeName,'');
	//viewSelectedResources(resourceTypeName);
}

/**
 * 
 */
function showTreeSelectedResources(){
	document.getElementById("userList").style.display="block";
	document.getElementById("userListView").style.display="none";
}

/**
 * when the selected resources in the resources window are assigned in a store
 * where duplicate records are checked before insertion.
 * @param cntRoleType
 * @param resourceTypeName
 */
function setSelectedResources(cntRoleType,resourceTypeName, nodeType) {
	
	var resourceStoreMember = getExtjsJsonObjByNameMembers(resourceTypeName);
    var resourceRecord = resourceStoreMember.recordType;
    var hiddenElem = document.getElementById("hidden_div").getElementsByTagName("input");
    var hiddenResource = "";
    if (hiddenElem != undefined) {
        hiddenResource = hiddenElem[0].value;
    }
    var elements = document.getElementById("userList").getElementsByTagName("input");
    var fullNames = document.getElementById("userList").getElementsByTagName("span");

    for (var i = 0; i < elements.length; i++) {
		if(elements[i].name != "userDetailSuperior" && elements[i].name != "userDetailSubordinate"){
			if ((elements[i].type == 'checkbox' || elements[i].type == 'radio') && elements[i].checked == true) {
		    	//checks for a record in the store. returns the index of the record or -1 if no match found
		    	var recordIndex = findARecordInStore(document.getElementById("resources_options").value,hiddenResource,elements[i].value,resourceStoreMember);
		    	if (recordIndex == -1) {
					var superior = elements[i].value+"_superior";
					var subordinate = elements[i].value+"_subordinate";
					var superiorValue = "false";
					var subordinateValue = "false";
					if( document.getElementById(superior)){
						superiorValue = document.getElementById(superior).checked;
					}
					if(document.getElementById(subordinate)){
						subordinateValue = document.getElementById(subordinate).checked;
					}
		            var aRecordVal = new resourceRecord({
		                resource_type: document.getElementById("resources_options").value,
		                resource: hiddenResource,
		                resourceassignmentexpr: elements[i].value,
		                language: '',
		                evaluatestotype: '',
						roletype: cntRoleType,
						superior: superiorValue,
						subordinate: subordinateValue,
						fullName: fullNames[i].innerHTML,
		            });
		            resourceStoreMember.add(aRecordVal);
			    
		        }
		        //viewSelectedResources(resourceTypeName);
		        renderResourceOrderView(resourceTypeName, nodeType);
			Ext.Msg.show({
				   title:'Information',
				   msg: ORYX.I18N.PropertyWindow.memberAddedSucc,
				   buttons: Ext.Msg.OK,
				   animEl: 'elId',
				   icon: Ext.MessageBox.Info
				});
			//return true;
		    } else if((elements[i].type == 'radio' || elements[i].type == 'checkbox') && elements[i].checked == false) {
		    	
		    	var recordIndex = findARecordInStore(document.getElementById("resources_options").value,hiddenResource,elements[i].value,resourceStoreMember);
		    	
		    	if(recordIndex != -1) {
				
		    		resourceStoreMember.remove(resourceStore.getAt(recordIndex));
		    		resourceStoreMember.commitChanges();
		    	}
			
			
		    }
		}
    }
	
}

/**
 * 
 */
function showResourceOrderDiv() {
	document.getElementById("userInfo_").style.display = "none";
	document.getElementById("resource_order").style.display = "block";
}

/**
 * 
 */
function showResourceTreeDiv() {
	document.getElementById("userInfo_").style.display = "block";
	document.getElementById("resource_order").style.display = "none";
}

/**
 * 
 * @param resourceType
 */
function renderResourceOrderView(resourceType, nodeType) {
	var resourceStoreMember = getExtjsJsonObjByNameMembers(resourceType);
	var orderView = "<div id='selectedResList' class='overflow height250'>";
	orderView += "<table border='0' cellpadding='1' cellspacing='1' class='line-height20 width-per-100'>";
	orderView += "<tbody>";	
	orderView += "<tr class='width-per-100'><td class='pad-L5'></td>";
	orderView += "<td><b>Parent</b></td>";
	orderView += "<td class='pad-L5'><b>Resource</b></td>";
	orderView += "<td class='pad-L5'><b>Order</b></td>";
	orderView += "</tr>";
	if(nodeType == "Single Sign Off"){
		var idx = resourceStoreMember.getCount() - 1;
		var aRecord = resourceStoreMember.getAt(idx);
		orderView += "<tr class='width-per-100'>";
		orderView += '<td><img class="pad-R5" src="/images/easybpm/form/close_btn.jpg" onclick="removeSelectedResources(\''+aRecord.get("resource_type")+'\',\''+aRecord.get("resource")+'\',\''+aRecord.get("resourceassignmentexpr")+'\',\''+resourceType+'\');"></td>';
		orderView += "<td>"+aRecord.get("resource")+"</td>";
		if(aRecord.get("resource") == aRecord.get("resourceassignmentexpr")){
			orderView += "<td class='pad-L5'>"+aRecord.get("resource")+"</td>";
		}else{
			orderView += "<td class='pad-L5'>"+aRecord.get("fullName")+"</td>";
		}
		
		var str = aRecord.get('resource_type')+'$'+aRecord.get('resource')+'$'+aRecord.get('resourceassignmentexpr');
		
		if(aRecord.get('evaluatestotype') != "" && aRecord.get('evaluatestotype') != undefined) {
			orderView += "<td class='pad-L5'><input class='fontSize11 height12 width30' type='text' id='"+str+"' value='"+aRecord.get('evaluatestotype')+"' /></td>";
		} else {
			orderView += "<td class='pad-L5'><input class='fontSize11 height12 width30' type='text' id='"+str+"' value='1' /></td>";
		}
		orderView += "</tr>";
	}else{
		for(var i=0; i<resourceStoreMember.getCount(); i++) {
			var aRecord = resourceStoreMember.getAt(i);
			orderView += "<tr class='width-per-100'>";
			orderView += '<td><img class="pad-R5" src="/images/easybpm/form/close_btn.jpg" onclick="removeSelectedResources(\''+aRecord.get("resource_type")+'\',\''+aRecord.get("resource")+'\',\''+aRecord.get("resourceassignmentexpr")+'\',\''+resourceType+'\');"></td>';
			orderView += "<td>"+aRecord.get("resource")+"</td>";
			if(aRecord.get("resource") == aRecord.get("resourceassignmentexpr")){
				orderView += "<td class='pad-L5'>"+aRecord.get("resource")+"</td>";
			}else{
				orderView += "<td class='pad-L5'>"+aRecord.get("fullName")+"</td>";
			}
			var str = aRecord.get('resource_type')+'$'+aRecord.get('resource')+'$'+aRecord.get('resourceassignmentexpr');
			
			if(aRecord.get('evaluatestotype') != "" && aRecord.get('evaluatestotype') != undefined) {
				orderView += "<td class='pad-L5'><input class='fontSize11 height12 width30' type='text' id='"+str+"' value='"+aRecord.get('evaluatestotype')+"' /></td>";
			} else {
				orderView += "<td class='pad-L5'><input class='fontSize11 height12 width30' type='text' id='"+str+"' value='"+generateResourceOrderNo(i+1)+"' /></td>";
			}
			orderView += "</tr>";
		}
	}
	orderView += "</tbody></table>";
	orderView += "</div>";
	document.getElementById("resource_order").innerHTML = orderView;
}


/**
 * All Resource data are identified and converted to a string 
 * 
 * @returns {Resources data in a String format}
 */
function getResourceStoreDataAsString(resourceTypeName) {
	var resourceStoreMember = getExtjsJsonObjByNameMembers(resourceTypeName);
    var jsonString = "[";

    for (var i = 0; i < resourceStoreMember.getCount(); i++) {
        var aRecord = resourceStoreMember.getAt(i);
				
        jsonString += "{";
        jsonString += "resource_type" + ":'" +aRecord.get("resource_type") + "'," + "resource" + ":'" + aRecord.get("resource") + "',";
        jsonString += "resourceassignmentexpr" + ":'"  + aRecord.get("resourceassignmentexpr") + "',";
        jsonString += "language" + ":'" + "" + "'," + "evaluatestotype" + ":'" + aRecord.get("evaluatestotype") + "',";
		jsonString += "roletype" + ":'" + aRecord.get("roletype") + "'," + "superior" + ":'" + aRecord.get("superior") + "',";
		jsonString += "subordinate" + ":'" + aRecord.get("subordinate") + "'";
        jsonString += "}";
        if (i < (resourceStoreMember.getCount() - 1)) {
            jsonString += ", ";
        }
    }
    jsonString += "]";
    jsonString = "{'totalCount':" + resourceStoreMember.getCount().toJSON() +
        ", 'items':" + jsonString + "}";

    return jsonString;
}

function getResourceStoreDataAsStringForProcess(resourceTypeName) {
	
	var resourceStoreMember = getExtjsJsonObjByNameMembers(resourceTypeName);
	
	 var jsonString = "[";

    for (var i = 0; i < resourceStoreMember.getCount(); i++) {
        var aRecord = resourceStoreMember.getAt(i);
				
        jsonString += "{";
        jsonString += "resource_type" + ":'" +aRecord.get("resource_type") + "'," + "resource" + ":'" + aRecord.get("resource") + "',";
        jsonString += "resourceassignmentexpr" + ":'"  + aRecord.get("resourceassignmentexpr") + "',";
        jsonString += "language" + ":'" + "" + "'," + "evaluatestotype" + ":'" + aRecord.get("evaluatestotype") + "',";
		jsonString += "roletype" + ":'" + aRecord.get("roletype") + "'," + "superior" + ":'" + aRecord.get("superior") + "',";
		jsonString += "subordinate" + ":'" + aRecord.get("subordinate") + "'";
        jsonString += "}";
        if (i < (resourceStoreMember.getCount() - 1)) {
            jsonString += ", ";
        }
    }
    jsonString += "]";
    jsonString = "{'totalCount':" + resourceStoreMember.getCount().toJSON() +
        ", 'items':" + jsonString + "}";

    return jsonString;
}



/**
 * Assigns order to the resources based on the user input or sets the default order
 */
function addOrderToResources() {
	
	if(orderStore.getCount() != 0) {
		orderStore.removeAll();
		orderStore.commitChanges();
	}
	var orderRecord = orderStore.recordType;
	var elements = document.getElementById("resource_order").getElementsByTagName("input");
	
	//sets orders based on the user input
	for (var i = 0; i < elements.length; i++) {
		var elemDetails = elements[i].id.split('$');
		var orderNo = document.getElementById(elements[i].id).value;
		var orderIndex = orderStore.find("evaluatestotype",orderNo);
		//elemDetails[0],elemDetails[1],elemDetails[2]
		if(orderIndex == -1){
			 var aRec = new orderRecord({
				 resource_type: elemDetails[0],
				 resource: elemDetails[1],
				 resourceassignmentexpr:elemDetails[2],
				 evaluatestotype:orderNo,
				 roletype:'',
             });
			 orderStore.add(aRec);
		} else {
			orderStore.remove(orderStore.getAt(orderIndex));
			orderStore.commitChanges();
			Ext.Msg.show({
				   title:'Error',
				   msg: ORYX.I18N.PropertyWindow.assignedError,
				   buttons: Ext.Msg.OK,
				   animEl: 'elId',
				   icon: Ext.MessageBox.ERROR
				});
			return false;
			//break;
			
		}
	}
	//assignOrderAndGetResourceData(resourceRole);
	return true;
}

/**
 * Assigns the order to the resource data from the orderstore.
 * @param resourceRole
 * @returns {String} Json representation of the resource data
 */
function assignOrderAndGetResourceData(resourceRole,paramFormId) {
	var resourceStoreMember = getExtjsJsonObjByNameMembers(resourceRole);
	//previousFormId = paramFormId;
	var jsonString = "";
	var memberJsonString = "[";
	for (var i = 0; i < resourceStoreMember.getCount(); i++) {
		var aRecord = resourceStoreMember.getAt(i);
	        
	    var orderIndex = findARecordInStore(aRecord.get("resource_type"),aRecord.get("resource"),aRecord.get("resourceassignmentexpr"),resourceStoreMember);
	        
		if (orderIndex != -1) {
			var aRecord = resourceStoreMember.getAt(i);
			aRecord.set("evaluatestotype",(orderStore.getAt(orderIndex)).get("evaluatestotype"));
			memberJsonString += "{";
		    memberJsonString += "resource_type" + ":'" + aRecord.get("resource_type") + "'," + "resource" + ":'" + aRecord.get("resource") + "',";
		    memberJsonString += "resourceassignmentexpr" + ":'" + aRecord.get("resourceassignmentexpr") + "',";
		    memberJsonString += "language" + ":'" + ""+ "'," + "evaluatestotype" + ":'" + aRecord.get("evaluatestotype")+ "',";
			memberJsonString += "roletype" + ":'" + aRecord.get("roletype") + "'," + "superior" + ":'" + aRecord.get("superior") + "',";
			memberJsonString += "subordinate" + ":'" + aRecord.get("subordinate") + "'";
			
		    if(resourceRole != undefined || resourceRole != "") {
			   	memberJsonString += ",";
			   	memberJsonString += "role" + ":'" + resourceRole+ "'";
		    }
		        
		    memberJsonString += "}";
		    if (i < (resourceStoreMember.getCount() - 1)) {
		    	memberJsonString += ", ";
		    }
		}
	}
	memberJsonString += "]";
	memberJsonString = "{'totalCount':" + resourceStoreMember.getCount().toJSON() +", 'items':" + memberJsonString +"}";
	var permissionJsonString =  generateFieldPermissionJSON(resourceRole,paramFormId);
	var dynamicTransactorJsonString = generatedynamicTransactorJSON(resourceRole,paramFormId);
	jsonString ="${"+ permissionJsonString +","+ "'members':["+ memberJsonString+"]"+","+dynamicTransactorJsonString+",'previousFormId' : '"+paramFormId+"'}";
	
	return jsonString;
}


/**
 * 
 * @returns {Boolean}
 */
function isResourceOrderDivVisible() {
	if(document.getElementById("resource_order").style.display == "block") {
		return true;
	}
	return false;
}

/**
 * 
 * @param responseObj
 * @param menuData
 * @param sourceId
 * @returns
 */
function renderResourcesView(responseObj,menuData,sourceId,resourceTypeName, nodeType) {
	var resourceStoreMember = getExtjsJsonObjByNameMembers(resourceTypeName);
	menuData = menuData + "<div class='overflow height250'>";
	var inputType = nodeType == "Single Sign Off" ? 'radio' : 'checkbox';
	for(var i=0; i<responseObj.length; i++){
		var userName = responseObj[i].userName;
		var userFullName = userName.split("(")[0];
		var userId = responseObj[i].userId;
		var recordIndex = findARecordInStore(document.getElementById("resources_options").value,sourceId,userId,resourceStoreMember);
		if (recordIndex == -1) {
			menuData += "<input type='"+inputType+"' name='userDetail' id='"+userId+"' value='"+userId+"' ><span class='pad-L5'>"+userFullName+"</span></input><br><div style='padding-left:20px;display:none'><input type='checkbox'  name='userDetailSuperior' id='"+userId+"_superior' value='"+userId+"_superior' /><span class='pad-L5'>Superior</span><br/><input type='checkbox' name='userDetailSubordinate' id='"+userId+"_subordinate' value='"+userId+"_subordinate' /><span class='pad-L5'>Subordinate</span></div><br/>";
		}else {
			var aRecord = resourceStoreMember.getAt(recordIndex);
			menuData += "<input type='"+inputType+"'  name='userDetail' id='"+userId+"' value='"+userId+"' checked='checked'><span class='pad-L5'>"+userName+"</span></input><br><div style='padding-left:20px;display:none'><input type='checkbox'  name='userDetailSuperior' "+renderCheckOrganizer(aRecord.get("superior"))+" id='"+userId+"_superior' value='"+userId+"_superior'><span class='pad-L5'>Superior</span><br/><input type='checkbox'  name='userDetailSubordinate' "+renderCheckOrganizer(aRecord.get("subordinate"))+" id='"+userId+"_subordinate' value='"+userId+"_subordinate'><span class='pad-L5'>Subordinate</span></div><br/>";
		}
	}
	menuData = menuData + "</div>";
	return menuData;
}

/**
 * 
 * @param organizerChkValue
 * @returns {String}
 */
function renderCheckOrganizer(organizerChkValue){
	if(organizerChkValue == "true"){
		return "checked='checked'";
	}else{
		return "";
	}
}

/**
 * finds a record in a store based on the params passed
 * @param resource_type
 * 			resource type whether it is potential owner or human performer 
 * @param resource
 * 		 
 * @param resourceassignmentexpr 
 *           name of the user/group
 * @returns the index if matching record is found.
 *         -1 is returned when no matching record is found.
 */
function findARecordInStore(resource_type,resource,resourceassignmentexpr,specificStore) {
	
	if(specificStore != undefined && specificStore != "undefined" && specificStore != "") {
		
		return specificStore.findBy(function (record, id) {
	        if (record.get('resource_type') == resource_type &&
	            record.get('resource') == resource &&
	            record.get('resourceassignmentexpr') == resourceassignmentexpr){
	            return true; // a record with this data exists
	        }
	        return false; // there is no record in the store with this dataoperationStore
	    });	
	}
	return resourceStore.findBy(function (record, id) {
	        if (record.get('resource_type') == resource_type &&
	            record.get('resource') == resource &&
	            record.get('resourceassignmentexpr') == resourceassignmentexpr) {
	            return true; // a record with this data exists
	        }
	        return false; // there is no record in the store with this data
	    });	
} 


/**
 * Generates order no for the resources, If the resource no is not present already,
 * New resource order no is generated.
 */
function generateResourceOrderNo(defaultOrderNo) {
	
	var recordIndex = resourceStore.findBy(function (record, id) {
	    if (record.get('evaluatestotype') === defaultOrderNo) {
	        return true; // a record with this data exists
	    }
	    return false; // there is no record in the store with this data
	});
	if(recordIndex != -1) {
		defaultOrderNo = parseInt(defaultOrderNo);
		return generateResourceOrderNo(++defaultOrderNo);
	}
	return defaultOrderNo;
	
}


function getResourcesAry() {
	return (resourcesAry != undefined || "undefined") ? resourcesAry : new Array(); 
}

function getPerformerAry() {
	return (performerAry != undefined || "undefined") ? performerAry : new Array();
}

/**
 * Data Store for loading Operation Resource.
 */
var operationStore_organizer = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }
    ],
    root: 'items',
});


var operationStore_organizerPermission = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }
    ],
    root: 'items',
});


var operationStore_coordinator = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }
    ],
    root: 'items',
});

var operationStore_reader = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }
    ],
    root: 'items',
});

var operationStore_creator = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }
    ],
    root: 'items',
});

var operationStore_processeduser = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }
    ],
    root: 'items',
});

var operationStore_workflowadministrator = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }
    ],
    root: 'items',
});

/**
 * Data Store for loading field permission.
 */
var fieldPermission_organizer = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }
    ],
    root: 'items',
});

var fieldPermission_coordinator = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }
    ],
    root: 'items',
});

var fieldPermission_reader = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }
    ],
    root: 'items',
});

var fieldPermission_creator = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }
    ],
    root: 'items',
});

var fieldPermission_processeduser = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }
    ],
    root: 'items',
});

var fieldPermission_workflowadministrator = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }
    ],
    root: 'items',
});

/**
 * Data Store for loading Dynamic Transactor.
 */
var dynamicTransactor_organizer = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }
    ],
    root: 'items',
});
var dynamicTransactor_startPermission = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }
    ],
    root: 'items',
});

/**
 * Data Store for loading Start End Script.
 */
var operationStore_startScript = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }
    ],
    root: 'items',
});

var operationStore_endScript = new Ext.data.JsonStore({
    fields: [{
            name: 'resource_type',
            mapping: 'resource_type'
        }, {
            name: 'resource',
            mapping: 'resource'
        }, {
            name: 'resourceassignmentexpr',
            mapping: 'resourceassignmentexpr'
        }, {
            name: 'language',
            mapping: 'language'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }
    ],
    root: 'items',
});

/**
 * 
 * @param resourceType
 */
function loadDefinedOperationList(resourceType){
	var definOptArray = new Array ();
	//definOptArray=["submit","Save","Return Operation","Recall","Withdraw","Urge","Transfer","Add","Collaborative Operation","Circulate Perusal","Jump","TransactorReplacement","Terminate","Suspend"];
	
	definOptArray = defaultOptionPermissionArray(resourceType);
	
	var definedOptHTML = "<table width='100%'><tr><td valign='top'><span><b>"+ORYX.I18N.PropertyWindow.operationFunction+"</b></span></td></tr><tr><td>&nbsp;</td></tr><tr><td><div style='float: left;width:100%;'><table width='100%'><tr>";
	var TRFlag = 0;
	for(var i=0; i<definOptArray.length; i++){
		if(TRFlag==3){
			definedOptHTML += "</tr><tr>";
			TRFlag = 0;
		}
		definedOptHTML += '<td><input type="checkbox" '+getOperationPermissionchecked(definOptArray[i].replace(/\s/g, "").toLowerCase()+'_elm',definOptArray[i],'Default Operation',resourceType)+' name="userDetail" id="'+definOptArray[i].replace(/\s/g, "").toLowerCase()+'_elm" value="'+definOptArray[i]+'" onclick="addOperationResourceCheck(\''+definOptArray[i].replace(/\s/g, "").toLowerCase()+'_elm\',\''+definOptArray[i]+'\',this,\''+resourceType+'\')"><span class="pad-L5">'+definOptArray[i]+'</span></input></td>';
		TRFlag += 1;
	}

	definedOptHTML += "</tr></table></div></td></tr></table>";

	document.getElementById("definedOperationPermission").innerHTML = definedOptHTML;

	viewOperationResources(resourceType);
}

/**
 * 
 * @param resourceType
 * @returns {Array}
 */
function defaultOptionPermissionArray(resourceType){
	var permissionName = (resourceType.replace("-","")).toLowerCase().replace(/ /gi,"");
	var definOptArray = new Array ();
	if(permissionName == "oforganizer" || permissionName == ORYX.I18N.PropertyWindow.ofOrganizer ){
		definOptArray = ["submit","Save","Return Operation","Transfer","Add","Collaborative Operation","Circulate Perusal"];
	}else if(permissionName == "ofcoordinator" || permissionName == ORYX.I18N.PropertyWindow.ofCoordinator ){
		definOptArray = ["Save"];
	}else if(permissionName == "ofreader" || permissionName == ORYX.I18N.PropertyWindow.ofReader){
		definOptArray = ["Circulate Perusal"];
	}else if(permissionName == "ofcreator" || permissionName == ORYX.I18N.PropertyWindow.ofCreator){
		definOptArray = ["Withdraw","Urge"];
	}else if(permissionName == "ofprocesseduser" || permissionName == ORYX.I18N.PropertyWindow.ofProcessedUser){
		definOptArray = ["Recall"];
	}else if(permissionName == "ofworkflowadministrator" || permissionName == ORYX.I18N.PropertyWindow.ofWorkFlowAdmin){
		definOptArray = ["Jump","TransactorReplacement","Terminate","Suspend","Print","Save"];
	}
	return definOptArray;
}

/**
 * 
 * @param elementId
 * @param elementValue
 * @param operationType
 * @param resourceType
 * @returns {String}
 */
function getOperationPermissionchecked(elementId,elementValue,operationType,resourceType){
	var operationFieldResource = getExtjsJsonObjByNameOFPermission(resourceType);
	var recordIndex = findARecordInStore(elementId,elementValue,operationType,operationFieldResource);
	if (recordIndex != -1) {
		return 'checked="checked"';
	}else{
		return '';
	}
}


/**
 * 
 */
function loadCustomOperationList(){
	var definOptArray = new Array ();
	
	for(var i=0; i<definOptArray.length; i++){
		if(TRFlag==3){
			definedOptHTML += "</tr><tr>";
			TRFlag = 0;
		}
		definedOptHTML += "<td><input type='checkbox' name='userDetail' id='"+definOptArray[i]+"' value='opfn"+definOptArray[i]+"'><span class='pad-L5'>"+definOptArray[i]+"</span></input></td>";
		TRFlag += 1;
	}
}

/**
 * 
 * @param elementId
 * @param elementValue
 * @param checkboxObject
 */
function addOperationResourceCheck(elementId,elementValue,checkboxObject,resourceType){
	if(checkboxObject.checked == true){
		addSelectedOperationResource(elementId,elementValue,'Default Operation',resourceType);
	}else{
		removeSelectedOperationResources(elementId,elementValue,'Default Operation',resourceType);
	}
}

/**
 * 
 * @param resources_options
 * @param hiddenResource
 * @param resourceassignmentexpr
 * @param operationStore
 */
function addSelectedOperationResource(resources_options,hiddenResource,resourceassignmentexpr,resourceType){
	var operationFieldResource = getExtjsJsonObjByNameOFPermission(resourceType);
	var resourceRecord = operationFieldResource.recordType;	

	//checks for a record in the store. returns the index of the record or -1 if no match found
	var recordIndex = findARecordInStore(resources_options,hiddenResource,resourceassignmentexpr,operationFieldResource);
	
	if (recordIndex == -1) {
		var aRecordVal = new resourceRecord({
		    resource_type: resources_options,
		    resource: hiddenResource,
		    resourceassignmentexpr: resourceassignmentexpr,
		    language: '',
		    evaluatestotype: '',
		});
		operationFieldResource.add(aRecordVal);
	}
	viewOperationResources(resourceType);
}

/**
 * 
 * @param customId
 * @param customValue
 */
function addCustomOperationResources(customId,customValue,resourceType){
	var operationFieldResource = getExtjsJsonObjByNameOFPermission(resourceType);
	var resourceRecord = operationFieldResource.recordType;

	var recordIndex = findARecordInStore(customId,customValue,'Custom Operation',operationFieldResource);
	
	if (recordIndex == -1) {
		var	customIdConvLower = customId.replace(/\s/g, "").toLowerCase();
        var aRecordVal = new resourceRecord({
            resource_type: customIdConvLower,
            resource: customValue,
            resourceassignmentexpr: 'Custom Operation',
            language: '',
            evaluatestotype: '',
        });
        operationFieldResource.add(aRecordVal);
    }
	viewOperationResources(resourceType);
}

/**
 * 
 * @param resourceType
 */
function viewOperationResources(resourceType){
	var operationFieldResource = getExtjsJsonObjByNameOFPermission(resourceType);
	var operationView = "<table border='0' cellpadding='1' cellspacing='1' class='line-height20 width-per-100'>";
		operationView += "<tbody>";	

	for(var i=0; i<operationFieldResource.getCount(); i++) {
		var aRecord = operationFieldResource.getAt(i);
		if(aRecord.get("resourceassignmentexpr")== 'Custom Operation'){
			operationView += "<tr class='width-per-100'>";
			operationView += '<td><img class="pad-R5" src="/images/easybpm/form/close_btn.jpg" onclick="removeSelectedOperationResources(\''+aRecord.get("resource_type")+'\',\''+aRecord.get("resource")+'\',\''+aRecord.get("resourceassignmentexpr")+'\',\''+resourceType+'\');">'+aRecord.get("resource")+'</td>';
			operationView += "<td class='pad-L5'>"+aRecord.get("resourceassignmentexpr")+"</td>";
			operationView += "</tr>";
		}
	}
	operationView += "</tbody></table>";
	document.getElementById("customOperationPermission").innerHTML=operationView;
}

/**
 * 
 * @param resources_options
 * @param hiddenResource
 * @param resourceassignmentexpr
 * @param operationStore
 */
function removeSelectedOperationResources(resources_options,hiddenResource,resourceassignmentexpr,resourceType){
	var operationFieldResource = getExtjsJsonObjByNameOFPermission(resourceType);
    var recordIndex = findARecordInStore(resources_options,hiddenResource,resourceassignmentexpr,operationFieldResource);
	if(recordIndex != -1) {
		operationFieldResource.remove(operationFieldResource.getAt(recordIndex));
		operationFieldResource.commitChanges();
	}

	if(resourceassignmentexpr == 'Default Operation'){
		document.getElementById(resources_options).checked = false;
	}
   
	viewOperationResources(resourceType);
}

/**
 * All Resource data are identified and converted to a string 
 * 
 * @returns {Resources data in a String format}
 */
function getOperationResourceStoreDataAsString(resourceType) {
	var operationFieldResource = getExtjsJsonObjByNameOFPermission(resourceType);
    var jsonString = "[";
    for (var i = 0; i < operationFieldResource.getCount(); i++) {
        var aRecord = operationFieldResource.getAt(i);
        jsonString += "{";
        jsonString += "resource_type" + ":'" +aRecord.get("resource_type") + "'," + "resource" + ":'" + aRecord.get("resource") + "',";
        jsonString += "resourceassignmentexpr" + ":'" + aRecord.get("resourceassignmentexpr")+ "',";
        jsonString += "language" + ":'" + "" + "'," + "evaluatestotype" + ":'" +  aRecord.get("evaluatestotype") +"'";
        jsonString += "}";
        if (i < (operationFieldResource.getCount() - 1)) {
            jsonString += ", ";
        }
    }
    jsonString += "]";
    jsonString = "{'totalCount':" + operationFieldResource.getCount().toJSON() +
        ", 'items':" + jsonString + "}";
    
    return jsonString;
}

/**
 * 
 * @param resourceType
 */
function loadStartScriptHtml(resourceType){
	var operationStartScript = getExtjsObjStartEndScript(resourceType);
	var startScriptHTML = "";
	startScriptHTML += "<table><tr>";
	startScriptHTML += "<td><span>"+ORYX.I18N.PropertyWindow.sfn+" </span> </td>";
	if(operationStartScript.getCount()>0){
		var aRecord = operationStartScript.getAt(0);
		startScriptHTML += "<td><input type='text' name='startscriptname' id='startscriptname' class='width290' value='"+decodeURIComponent(aRecord.get("resource_type"))+"'/>";
		startScriptHTML += "</tr>";
		startScriptHTML += "<tr><td colspan='2'>Script Content:</td></tr>";
		startScriptHTML += "<tr>";
		startScriptHTML += "<td colspan='2'><textarea id='startscriptfunction' name='startscriptfunction' class='width405 height290' >"+decodeURIComponent(aRecord.get("resource"))+"</textarea></td>";
	}else{
		startScriptHTML += "<td><input type='text' name='startscriptname' id='startscriptname' class='width290' value=''/>";
		startScriptHTML += "</tr>";
		startScriptHTML += "<tr><td colspan='2'>Script Content:</td></tr>";
		startScriptHTML += "<tr>";
		startScriptHTML += "<td colspan='2'><textarea id='startscriptfunction' name='startscriptfunction' class='width405 height290' ></textarea></td>";
	}
	
	startScriptHTML += "</tr></table>";
	
	document.getElementById('startScriptTabPanel').innerHTML = startScriptHTML;
}

/**
 * 
 * @param resourceType
 */
function loadEndScriptHtml(resourceType){
	var operationEndScript = getExtjsObjStartEndScript(resourceType);
	var endScriptHTML = "";
	endScriptHTML += "<table><tr>";
	endScriptHTML += "<td><span>"+ORYX.I18N.PropertyWindow.sfn+" </span> </td>";
	if(operationEndScript.getCount()>0){
		var aRecord = operationEndScript.getAt(0);
		endScriptHTML += "<td><input type='text' name='endscriptname' id='endscriptname' class='width290' value='"+decodeURIComponent(aRecord.get("resource_type"))+"' />";
		endScriptHTML += "</tr>";
		endScriptHTML += "<tr><td colspan='2'>Script Content:</td></tr>";
		endScriptHTML += "<tr>";
		endScriptHTML += "<td colspan='2'><textarea id='endscriptfunction' name='endscriptfunction' class='width405 height290' >"+decodeURIComponent(aRecord.get("resource"))+"</textarea></td>";
		endScriptHTML += "</tr></table>";
	}else{
		endScriptHTML += "<td><input type='text' name='endscriptname' id='endscriptname' class='width290' value='' />";
		endScriptHTML += "</tr>";
		endScriptHTML += "<tr><td colspan='2'>Script Content:</td></tr>";
		endScriptHTML += "<tr>";
		endScriptHTML += "<td colspan='2'><textarea id='endscriptfunction' name='endscriptfunction' class='width405 height290' ></textarea></td>";
		
	}
	endScriptHTML += "</tr></table>";
	
	document.getElementById('endScriptTabPanel').innerHTML = endScriptHTML;
}

/**
 * 
 * @param resourceType
 * @returns {String}
 */
function createStartScriptJson(resourceType){
	var operationStartScript = getExtjsObjStartEndScript(resourceType);
	var resourceRecord = operationStartScript.recordType;
	var scriptName = document.getElementById('startscriptname').value;
	var scriptFunction = document.getElementById('startscriptfunction').value;
	operationStartScript.removeAll();
    	var aRecordVal = new resourceRecord({
		resource_type: escape(scriptName),
		resource: escape(scriptFunction),
		resourceassignmentexpr: 'Start Script Operation',
		language: '',
		evaluatestotype: '',
    });
    operationStartScript.add(aRecordVal);
    
	var jsonString = "[{";
	jsonString += "'scriptName'" + ":" +"'"+escape(scriptName)+"',";
	jsonString += "'scriptFunction'" + ":" +"'"+escape(scriptFunction)+"'";
	jsonString += "}]";
	
	return jsonString;
}

/**
 * 
 * @param resourceType
 * @returns {String}
 */
function createEndScriptJson(resourceType){
	var operationEndScript = getExtjsObjStartEndScript(resourceType);
	var resourceRecord = operationEndScript.recordType;
	var scriptName = document.getElementById('endscriptname').value;
	var scriptFunction = document.getElementById('endscriptfunction').value;
	operationEndScript.removeAll();
    var aRecordVal = new resourceRecord({
        resource_type: escape(scriptName),
        resource: escape(scriptFunction),
        resourceassignmentexpr: 'End Script Operation',
        language: '',
        evaluatestotype: '',
    });
    operationEndScript.add(aRecordVal);
    
	var jsonString = "[{";
	jsonString += "'scriptName'" + ":" +"'"+escape(scriptName)+"',";
	jsonString += "'scriptFunction'" + ":" +"'"+escape(scriptFunction)+"'";
	jsonString += "}]";
	
	return jsonString;
}

/**
 * 
 * @param fieldResponseData
 * @returns {String}
 */
function getFieldPermissionString(fieldResponseData){
	var fieldLength = fieldResponseData.length-1;
	var consJsonContent = "[{";
	for(var i= 0;i< fieldResponseData.length;i++){
			consJsonContent = consJsonContent +"'"+fieldResponseData[i].id+"':'"+fieldPermissionCheck("check_editable_"+fieldResponseData[i].id)+"-"+fieldPermissionCheck("check_required_"+fieldResponseData[i].id)+"-"+fieldPermissionCheck("check_readonly_"+fieldResponseData[i].id)+"-"+fieldPermissionCheck("check_hidden_"+fieldResponseData[i].id)+"-"+fieldPermissionCheck("check_nooutput_"+fieldResponseData[i].id);
			if(fieldLength > i){
				consJsonContent = consJsonContent  + "',";
			}else{
				consJsonContent = consJsonContent + "'";
			}
		}
		
	var lastTwoString = consJsonContent;

	if(lastTwoString.slice(-2) == "&&" || lastTwoString.slice(-2) == '||'){
		consJsonContent = consJsonContent.substring(0, consJsonContent.length - 2);
	}
	consJsonContent += "}]";
	return consJsonContent;
}

/**
 * 
 * @param fieldId
 * @returns {Number}
 */
function fieldPermissionCheck(fieldId){
	var fieldPermission  = document.getElementById(fieldId).checked;
	if(fieldPermission == true){
		return 1;
	}else{
		return 0;
	}	
}

/**
 * 
 * @param resourceType
 * @returns {String}
 */
function getExtjsJsonObjWithName(resourceType){
	var permissionName = (resourceType.replace("-","")).toLowerCase().replace(/ /gi,"");
	var fieldResource = "";
	if(permissionName == "organizer" || permissionName == ORYX.I18N.PropertyWindow.organizer ){
		fieldResource = fieldPermission_organizer;
	}else if(permissionName == "coordinator" || permissionName == ORYX.I18N.PropertyWindow.coordinator){
		fieldResource = fieldPermission_coordinator;
	}else if(permissionName == "reader" || permissionName == ORYX.I18N.PropertyWindow.reader){
		fieldResource = fieldPermission_reader;
	}else if(permissionName == "creator" || permissionName == ORYX.I18N.PropertyWindow.creator){
		fieldResource = fieldPermission_creator;
	}else if(permissionName == "processeduser" || permissionName == ORYX.I18N.PropertyWindow.processedUser ){
		fieldResource = fieldPermission_workflowadministrator;
	}else if(permissionName == "workflowadministrator" || permissionName == ORYX.I18N.PropertyWindow.workFlowAdmin){
		fieldResource = fieldPermission_workflowadministrator;
	}else if(permissionName == "startpermission" || permissionName == ORYX.I18N.PropertyWindow.startPermission){
		fieldResource = operationStore_organizerPermission;
	}

	return fieldResource;
}

/**
 * 
 * @param resourceTypeName
 * @returns {String}
 */
function getExtjsJsonObjByNameMembers(resourceTypeName){
	var permissionName = (resourceTypeName.replace("-","")).toLowerCase().replace(/ /gi,"");
	var resourceStoreMember = "";
	if(permissionName == "organizer" || permissionName == ORYX.I18N.PropertyWindow.organizer ){
		resourceStoreMember = resourceStore;
	}else if(permissionName == "coordinator" || permissionName == ORYX.I18N.PropertyWindow.coordinator ){
		resourceStoreMember = resourceStore_coordinator;
	}else if(permissionName == "reader" || permissionName == ORYX.I18N.PropertyWindow.reader){
		resourceStoreMember = resourceStore_reader;
	}else if(permissionName == "creator" || permissionName == ORYX.I18N.PropertyWindow.creator ){
		resourceStoreMember = resourceStore_creator;
	}else if(permissionName == "processeduser" || permissionName == ORYX.I18N.PropertyWindow.processedUser){
		resourceStoreMember = resourceStore_workflowadministrator;
	}else if(permissionName == "workflowadministrator" || permissionName == ORYX.I18N.PropertyWindow.workFlowAdmin){
		resourceStoreMember = resourceStore_workflowadministrator;
	}else if(permissionName == "admin" || permissionName == ORYX.I18N.PropertyWindow.admin){
		resourceStoreMember = resourceStore_admin;
	}else if(permissionName == "process" || permissionName == ORYX.I18N.PropertyWindow.process){	
		resourceStoreMember = resourceStore_process;
	}
	return resourceStoreMember;
}

/**
 * 
 * @param resourceType
 * @returns {String}
 */
function getExtjsJsonObjByNameOFPermission(resourceType){
	var permissionName = (resourceType.replace("-","")).toLowerCase().replace(/ /gi,"");
	var operationStoreOfPer = "";
	if(permissionName == "oforganizer" || permissionName == ORYX.I18N.PropertyWindow.ofOrganizer){
		operationStoreOfPer = operationStore_organizer;
	}else if(permissionName == "ofcoordinator" || permissionName == ORYX.I18N.PropertyWindow.ofCoordinator){
		operationStoreOfPer = operationStore_coordinator;
	}else if(permissionName == "ofreader" || permissionName == ORYX.I18N.PropertyWindow.ofReader){
		operationStoreOfPer = operationStore_reader;
	}else if(permissionName == "ofcreator" || permissionName == ORYX.I18N.PropertyWindow.ofCreator){
		operationStoreOfPer = operationStore_creator;
	}else if(permissionName == "ofprocesseduser" || permissionName == ORYX.I18N.PropertyWindow.ofProcessedUser){
		operationStoreOfPer = operationStore_processeduser;
	}else if(permissionName == "ofworkflowadministrator" || permissionName == ORYX.I18N.PropertyWindow.ofWorkFlowAdmin){
		operationStoreOfPer = operationStore_workflowadministrator;
	}

	return operationStoreOfPer;
}

/**
 * 
 * @returns {String}
 */
function getExtjsObjStartEndScript(resourceType){
	var startEndScriptName = (resourceType.replace("-","")).toLowerCase().replace(/ /gi,"");
	var startEndScriptObj = "";
	if(startEndScriptName == "endscript" || startEndScriptName == ORYX.I18N.PropertyWindow.endScript){
		startEndScriptObj = operationStore_startScript;
	}else if(startEndScriptName == "startscript" || startEndScriptName == ORYX.I18N.PropertyWindow.startScript){
		startEndScriptObj = operationStore_endScript;
	}
	return startEndScriptObj;
}

/**
 * 
 * @param resourceType
 * @returns {String}
 */
function getExtjsJsonObjByNameOFdynamicTransactor(resourceType){
	var dynamicTransactorName = (resourceType.replace("-","")).toLowerCase().replace(/ /gi,"");
	var dynamicTransactorOfPer = "";
	if(dynamicTransactorName == "organizer" || dynamicTransactorName == ORYX.I18N.PropertyWindow.organizer){
		dynamicTransactorOfPer = dynamicTransactor_organizer;
	}else if(dynamicTransactorName == "reader" || dynamicTransactorName == ORYX.I18N.PropertyWindow.reader){
		dynamicTransactorOfPer = dynamicTransactor_organizer;	
	}else if(dynamicTransactorName == "startpermission" || dynamicTransactorName == ORYX.I18N.PropertyWindow.startPermission){
		dynamicTransactorOfPer = dynamicTransactor_startPermission;
	}
	return dynamicTransactorOfPer;
}


/**
 * 
 * @param resourceType
 */
function selectAllPermissions(resourceType){
	var fieldResource = getExtjsJsonObjWithName(resourceType);
	var checkbox_editable = false;
	var checkbox_required = false;
	var checkbox_readonly = false;
	var checkbox_hidden = false;
	var checkbox_nooutput = false;
	var checkbox_selectAll = document.getElementById('check_selectall').checked;
	if(checkbox_selectAll){
		for(var i=0; i<fieldResource.getCount(); i++) {
			var aRecord = fieldResource.getAt(i);
			if(i==0){
				chk_editable = document.getElementById('check_editable_'+aRecord.get("resource_type")).checked;
				chk_required = document.getElementById('check_required_'+aRecord.get("resource_type")).checked;
				chk_readonly = document.getElementById('check_readonly_'+aRecord.get("resource_type")).checked;
				chk_hidden = document.getElementById('check_hidden_'+aRecord.get("resource_type")).checked;
				chk_nooutput = document.getElementById('check_nooutput_'+aRecord.get("resource_type")).checked;
				if(chk_editable){
					document.getElementById('check_readonly_'+aRecord.get("resource_type")).disabled = true;
				}
				if(chk_hidden || chk_nooutput){
					document.getElementById('check_editable_'+aRecord.get("resource_type")).disabled = true;
					document.getElementById('check_required_'+aRecord.get("resource_type")).disabled = true;
					document.getElementById('check_readonly_'+aRecord.get("resource_type")).disabled = true;
					
					if(chk_nooutput){
						document.getElementById('check_hidden_'+aRecord.get("resource_type")).disabled = true;
					}else{
						document.getElementById('check_nooutput_'+aRecord.get("resource_type")).disabled = true;
					}
				}
			}else{
				document.getElementById('check_editable_'+aRecord.get("resource_type")).checked = chk_editable;
				document.getElementById('check_required_'+aRecord.get("resource_type")).checked = chk_required;
				document.getElementById('check_readonly_'+aRecord.get("resource_type")).checked = chk_readonly;
				document.getElementById('check_hidden_'+aRecord.get("resource_type")).checked = chk_hidden;
				document.getElementById('check_nooutput_'+aRecord.get("resource_type")).checked = chk_nooutput;
				if(chk_editable){
					document.getElementById('check_readonly_'+aRecord.get("resource_type")).disabled = true;
				}
				if(chk_hidden || chk_nooutput){
					document.getElementById('check_editable_'+aRecord.get("resource_type")).disabled = true;
					document.getElementById('check_required_'+aRecord.get("resource_type")).disabled = true;
					document.getElementById('check_readonly_'+aRecord.get("resource_type")).disabled = true;
					
					if(chk_nooutput){
						document.getElementById('check_hidden_'+aRecord.get("resource_type")).disabled = true;
					}else{
						document.getElementById('check_nooutput_'+aRecord.get("resource_type")).disabled = true;
					}
				}
			}
		}
	}else{
		for(var i=0; i<fieldResource.getCount(); i++) {
			var aRecord = fieldResource.getAt(i);
			if(i > 0){
				document.getElementById('check_editable_'+aRecord.get("resource_type")).checked = false;
				document.getElementById('check_required_'+aRecord.get("resource_type")).checked = false;
				document.getElementById('check_readonly_'+aRecord.get("resource_type")).checked = false;
				document.getElementById('check_hidden_'+aRecord.get("resource_type")).checked = false;
				document.getElementById('check_nooutput_'+aRecord.get("resource_type")).checked = false;
				document.getElementById('check_readonly_'+aRecord.get("resource_type")).disabled = false;
				document.getElementById('check_editable_'+aRecord.get("resource_type")).disabled = false;
				document.getElementById('check_required_'+aRecord.get("resource_type")).disabled = false;
				document.getElementById('check_readonly_'+aRecord.get("resource_type")).disabled = false;
				document.getElementById('check_hidden_'+aRecord.get("resource_type")).disabled = false
				document.getElementById('check_nooutput_'+aRecord.get("resource_type")).disabled = false;
			}	
		}
	}
}

/**
 * 
 * @param resource_type
 * @param resource
 * @param resourceassignmentexpr
 * @param operationStore
 */
function removeFieldPermission(resource_type,resource,resourceassignmentexpr,operationStore){
    var recordIndex = findARecordInStore(resource_type,resource,resourceassignmentexpr,operationStore);
	if(recordIndex != -1) {
		operationStore.remove(operationStore.getAt(recordIndex));
		operationStore.commitChanges();
	}
}

/**
 * 
 * @param tresource_type
 * @param tresource
 * @param tresourceassignmentexpr
 * @param tevaluatestotype
 * @param operationStore
 */
function addFieldPermission(tresource_type,tresource,tresourceassignmentexpr,tevaluatestotype,operationStore,language){
	var recordIndex = findARecordInStore(tresource_type,tresource,tresourceassignmentexpr,operationStore);
	var resourceRecord = operationStore.recordType;	
	if(recordIndex == -1) {
		var aRecordVal = new resourceRecord({
			resource_type: tresource_type,
			resource: tresource,
			resourceassignmentexpr: tresourceassignmentexpr,
			language: language,
			evaluatestotype: tevaluatestotype,
		});
		operationStore.add(aRecordVal);
	}
}

/**
 * 
 * @param resource_type
 * @param resource
 * @param resourceassignmentexpr
 * @param evaluatestotype
 * @param resourceType
 */
function updateFieldPermissionMultiplexing(resource_type,resource,resourceassignmentexpr,evaluatestotype,resourceType,language){
	var fieldResource = getExtjsJsonObjWithName(resourceType);
	removeFieldPermission(resource_type,resource,resourceassignmentexpr,fieldResource);
	addFieldPermission(resource_type,resource,resourceassignmentexpr,evaluatestotype,fieldResource,language);
	
}

/**
 * 
 * @param fldPerList
 * @param resourceType
 */
function updateFieldPermissons(fldPerList,resourceType){
	for(var i=0;i<fldPerList.length;i++){
		updateFieldPermissionMultiplexing(fldPerList[i].resource_type,fldPerList[i].resource,fldPerList[i].resourceassignmentexpr,fldPerList[i].evaluatestotype,resourceType,fldPerList[i].language)
	}
}

/**
 * 
 * @param resourceType
 * @param paramFormId
 * @param resourceProperty
 */
function setFormPreviousId(resourceType,paramFormId,resourceProperty){
	var permissionName = (resourceType.replace("-","")).toLowerCase().replace(/ /gi,"");
	if((permissionName == "organizer" || permissionName == ORYX.I18N.PropertyWindow.organizer) && resourceProperty == "FieldPermission"){
		previousorganizerFormId = paramFormId;
	}else if((permissionName == "coordinator" || permissionName ==  ORYX.I18N.PropertyWindow.coordinator) && resourceProperty == "FieldPermission"){
		previouscoordinatorFormId = paramFormId;
	}else if((permissionName == "reader" || permissionName == ORYX.I18N.PropertyWindow.reader)  && resourceProperty == "FieldPermission"){
		previousreaderFormId = paramFormId;
	}else if((permissionName == "creator" || permissionName == ORYX.I18N.PropertyWindow.creator)  && resourceProperty == "FieldPermission"){
		previouscreatorFormId = paramFormId;
	}else if((permissionName == "processeduser" || permissionName == ORYX.I18N.PropertyWindow.processedUser)  && resourceProperty == "FieldPermission"){
		previousprocesseduserFormId = paramFormId;
	}else if((permissionName == "workflowadministrator" || permissionName == ORYX.I18N.PropertyWindow.workFlowAdmin) && resourceProperty == "FieldPermission"){
		previousworkflowadministratorFormId = paramFormId;
	}else if((permissionName == "admin" || permissionName == ORYX.I18N.PropertyWindow.admin) && resourceProperty == "FieldPermission"){
		previousadminFormId = paramFormId;
	}else if((permissionName == "organizer"|| permissionName == ORYX.I18N.PropertyWindow.organizer) && resourceProperty == "DynamicTransactor"){
		previousDynamicTransactororganizerFormId = paramFormId;
	}else if((permissionName == "reader"|| permissionName == ORYX.I18N.PropertyWindow.reader) && resourceProperty == "DynamicTransactor"){
		previousDynamicTransactorreaderFormId = paramFormId;
	}else if((permissionName == "startpermission" || permissionName == ORYX.I18N.PropertyWindow.startPermission) && resourceProperty == "StartPermission"){
		previousStartPermissionFormId = paramFormId;
	}else if((permissionName == "startpermission" || permissionName == ORYX.I18N.PropertyWindow.startPermission) && resourceProperty == "StartDynamicTransactor"){
		previousStartDynamicFormId = paramFormId;
	}
}

/**
 * 
 * @param resourceType
 * @param paramFormId
 * @param resourceProperty
 * @returns {Boolean}
 */
function checkFormPreviousId(resourceType,paramFormId,resourceProperty){
	var permissionName = (resourceType.replace("-","")).toLowerCase().replace(/ /gi,"");
	var permissionFlag = false;
	if((permissionName == "organizer"|| permissionName == ORYX.I18N.PropertyWindow.organizer) && resourceProperty == "FieldPermission"){
		if(previousorganizerFormId == null || previousorganizerFormId == "" || previousorganizerFormId == undefined){
			previousorganizerFormId=paramFormId;
		}
		if(previousorganizerFormId==paramFormId){
			permissionFlag = true;
		}
	}else if((permissionName == "coordinator" || permissionName ==  ORYX.I18N.PropertyWindow.coordinator) && resourceProperty == "FieldPermission"){
		if(previouscoordinatorFormId == null || previouscoordinatorFormId == "" || previouscoordinatorFormId == undefined){
			previouscoordinatorFormId=paramFormId;
		}
		if(previouscoordinatorFormId==paramFormId){
			permissionFlag = true;
		}
	}else if((permissionName == "reader"|| permissionName == ORYX.I18N.PropertyWindow.reader) && resourceProperty == "FieldPermission"){
		if(previousreaderFormId == null || previousreaderFormId == "" || previousreaderFormId == undefined){
			previousreaderFormId=paramFormId;
		}
		if(previousreaderFormId==paramFormId){
			permissionFlag = true;
		}
	}else if((permissionName == "creator" || permissionName == ORYX.I18N.PropertyWindow.creator)&& resourceProperty == "FieldPermission"){
		if(previouscreatorFormId == null || previouscreatorFormId == "" || previouscreatorFormId == undefined){
			previouscreatorFormId=paramFormId;
		}
		if(previouscreatorFormId==paramFormId){
			permissionFlag = true;
		}
	}else if((permissionName == "processeduser"|| permissionName == ORYX.I18N.PropertyWindow.processedUser) && resourceProperty == "FieldPermission"){
		if(previousprocesseduserFormId == null || previousprocesseduserFormId == "" || previousprocesseduserFormId == undefined){
			previousprocesseduserFormId=paramFormId;
		}
		if(previousprocesseduserFormId==paramFormId){
			permissionFlag = true;
		}
	}else if((permissionName == "workflowadministrator" || permissionName == ORYX.I18N.PropertyWindow.workFlowAdmin) && resourceProperty == "FieldPermission"){
		if(previousworkflowadministratorFormId == null || previousworkflowadministratorFormId == "" || previousworkflowadministratorFormId == undefined){
			previousworkflowadministratorFormId=paramFormId;
		}
		if(previousworkflowadministratorFormId==paramFormId){
			permissionFlag = true;
		}
	}else if((permissionName == "admin" || permissionName == ORYX.I18N.PropertyWindow.admin)&& resourceProperty == "FieldPermission"){
		if(previousadminFormId == null || previousadminFormId == "" || previousadminFormId == undefined){
			previousadminFormId=paramFormId;
		}
		if(previousadminFormId==paramFormId){
			permissionFlag = true;
		}
	}else if((permissionName == "organizer"|| permissionName == ORYX.I18N.PropertyWindow.organizer) && resourceProperty == "DynamicTransactor"){
		if(previousDynamicTransactororganizerFormId == null || previousDynamicTransactororganizerFormId == "" || previousDynamicTransactororganizerFormId == undefined){
			previousDynamicTransactororganizerFormId=paramFormId;
		}
		if(previousDynamicTransactororganizerFormId==paramFormId){
			permissionFlag = true;
		}
	}else if((permissionName == "reader"|| permissionName == ORYX.I18N.PropertyWindow.reader)  && resourceProperty == "DynamicTransactor"){
		if(previousDynamicTransactorreaderFormId == null || previousDynamicTransactorreaderFormId == "" || previousDynamicTransactorreaderFormId == undefined){
			previousDynamicTransactorreaderFormId=paramFormId;
		}
		if(previousDynamicTransactorreaderFormId==paramFormId){
			permissionFlag = true;
		}
		
	}else if((permissionName == "startpermission"|| permissionName == ORYX.I18N.PropertyWindow.startPermission) && resourceProperty == "StartPermission"){
		if(previousStartPermissionFormId == null || previousStartPermissionFormId == "" || previousStartPermissionFormId == undefined){
			previousStartPermissionFormId = paramFormId;
		}
		if(previousStartPermissionFormId == paramFormId){
			permissionFlag = true;
		}else{
			permissionFlag = false;
		}
	}else if((permissionName == "startpermission"|| permissionName == ORYX.I18N.PropertyWindow.startPermission) && resourceProperty == "StartDynamicTransactor"){
		if(previousStartDynamicFormId == null || previousStartDynamicFormId == "" || previousStartDynamicFormId == undefined){
			previousStartDynamicFormId = paramFormId;
		}
		if(previousStartDynamicFormId == paramFormId){
			permissionFlag = true;
		}
	}
	return permissionFlag;
}

/**
 * 
 * @param resourceType
 * @returns {String}
 */
function generateFieldPermissionJSON(resourceType,paramFormId){
	setFormPreviousId(resourceType,paramFormId,"FieldPermission");
	//previousFormId = paramFormId;
	var fieldResource = getExtjsJsonObjWithName(resourceType);
	var fieldLength = fieldResource.getCount() - 1;
	var consJsonContent = "'fieldsPersmission':[{";
	//var fieldPermissionValueList = new Array();  
	for(var i=0; i<fieldResource.getCount(); i++) {
		var aRecord = fieldResource.getAt(i);
		/*var fieldPermissionValueMap = {};
		fieldPermissionValueMap['resource_type'] = aRecord.get("resource_type");
		fieldPermissionValueMap['resource'] = aRecord.get("resource");
		fieldPermissionValueMap['resourceassignmentexpr'] = aRecord.get("resourceassignmentexpr");
		if(document.getElementById('check_selectall') == null || document.getElementById('check_selectall') == "" || document.getElementById('check_selectall') == undefined){
			fieldPermissionValueMap['evaluatestotype'] = "'"+aRecord.get("evaluatestotype");+"'";
		}else{
			fieldPermissionValueMap['evaluatestotype'] = "'"+fieldPermissionCheck("check_editable_"+aRecord.get("resource_type"))+"-"+fieldPermissionCheck("check_required_"+aRecord.get("resource_type"))+"-"+fieldPermissionCheck("check_readonly_"+aRecord.get("resource_type"))+"-"+fieldPermissionCheck("check_hidden_"+aRecord.get("resource_type"))+"-"+fieldPermissionCheck("check_nooutput_"+aRecord.get("resource_type"))+"'";
		}
		
		fieldPermissionValueList[i] = fieldPermissionValueMap;*/
		if(document.getElementById('check_selectall') == null || document.getElementById('check_selectall') == "" || document.getElementById('check_selectall') == undefined){
			consJsonContent = consJsonContent +"'"+aRecord.get("resource_type")+"':'"+aRecord.get("evaluatestotype");
		}else{
			consJsonContent = consJsonContent +"'"+aRecord.get("resource_type")+"':'"+fieldPermissionCheck("check_editable_"+aRecord.get("resource_type"))+"-"+fieldPermissionCheck("check_required_"+aRecord.get("resource_type"))+"-"+fieldPermissionCheck("check_readonly_"+aRecord.get("resource_type"))+"-"+fieldPermissionCheck("check_hidden_"+aRecord.get("resource_type"))+"-"+fieldPermissionCheck("check_nooutput_"+aRecord.get("resource_type"));
		}
		
		if(fieldLength > i){
			consJsonContent = consJsonContent  + "',";
		}else{
			consJsonContent = consJsonContent + "'";
		}
	}
	//updateFieldPermissons(fieldPermissionValueList,resourceType);
	consJsonContent += "}]";
	return consJsonContent;
}


/**
 * 
 * @param resourceType
 */
function setFieldPermission(resourceType){
	var fieldResource = getExtjsJsonObjWithName(resourceType);
	var fieldPermissionValueList = new Array();  
	for(var i=0; i<fieldResource.getCount(); i++) {
		var aRecord = fieldResource.getAt(i);
		var fieldPermissionValueMap = {};
		fieldPermissionValueMap['resource_type'] = aRecord.get("resource_type");
		fieldPermissionValueMap['resource'] = aRecord.get("resource");
		fieldPermissionValueMap['resourceassignmentexpr'] = aRecord.get("resourceassignmentexpr");
		fieldPermissionValueMap['language'] = aRecord.get("language");
		if(document.getElementById('check_selectall') == null || document.getElementById('check_selectall') == "" || document.getElementById('check_selectall') == undefined){
			fieldPermissionValueMap['evaluatestotype'] = "'"+aRecord.get("evaluatestotype");+"'";
		}else{
			fieldPermissionValueMap['evaluatestotype'] = "'"+fieldPermissionCheck("check_editable_"+aRecord.get("resource_type"))+"-"+fieldPermissionCheck("check_required_"+aRecord.get("resource_type"))+"-"+fieldPermissionCheck("check_readonly_"+aRecord.get("resource_type"))+"-"+fieldPermissionCheck("check_hidden_"+aRecord.get("resource_type"))+"-"+fieldPermissionCheck("check_nooutput_"+aRecord.get("resource_type"))+"'";
		}
		fieldPermissionValueList[i] = fieldPermissionValueMap;
		
	}
	updateFieldPermissons(fieldPermissionValueList,resourceType);
}

/**
 * 
 * @param resourceType
 * @returns {String}
 */
function generatedynamicTransactorJSON(resourceType,paramFormId){
	setFormPreviousId(resourceType,paramFormId,"DynamicTransactor");
	var dynamicTransactorObj = getExtjsJsonObjByNameOFdynamicTransactor(resourceType);
	var fieldLength = dynamicTransactorObj.getCount() - 1;
	var consJsonContent = "'dynamicTransactor':[";
	
	for(var i=0; i<dynamicTransactorObj.getCount(); i++) {
		var aRecord = dynamicTransactorObj.getAt(i);
		if(aRecord.get("evaluatestotype") == "1"){
			consJsonContent +=  "'"+aRecord.get("resource_type")+"','"+aRecord.get("language")+"'";
		}
	}
	consJsonContent += "]";
	reinitializedFlag = 0;
	return consJsonContent;
}

/**
 * 
 * @param checkedValue
 * @returns {String}
 */
function getcheckedInformation(checkedValue,checkedValueEditable,checkedValueReadOnly){
	if(checkedValueReadOnly){ 
		if(checkedValueReadOnly == '0'){
			return 'disabled="disabled"';
		}else{
			return 'checked="checked"';
		}
	}else{
		if(checkedValue == '1'){
			return 'checked="checked"';
		} if(checkedValueEditable == '1'){
			return 'checked="checked"';
		}else{
			return '';
		}
		
	}
}

/**
 *
 * @param fieldPermissionResource
 * @param paramFormId
 * @param resourceType
 * @param resourceValue
 */
function setStartPropertyFieldPermission ( fieldPermissionResource, paramFormId, resourceType , resourceValue ){
var permissionContainer  = "";
permissionContainer = "<div class='overflow height250'><table width='100%'><tr><td><b>"+ORYX.I18N.PropertyWindow.premission+"</b></td><td><b>"+ORYX.I18N.PropertyWindow.editable+"</b></td><td><b>"+ORYX.I18N.PropertyWindow.required+"<b></td><td><b>"+ORYX.I18N.PropertyWindow.readOnly+"</b></td><td><b>"+ORYX.I18N.PropertyWindow.hidden+"</b></td><td><b>"+ORYX.I18N.PropertyWindow.noOutput+"</b></td><td>"+ORYX.I18N.PropertyWindow.selectAll+"</td></tr>";
		var fieldResource = getExtjsJsonObjWithName(resourceType);
		var previousFormCheckFlag = checkPreviousFormId(resourceValue, paramFormId);
 		if(fieldResource.getCount() > 0 && previousFormCheckFlag == true ){
			for(var i=0; i<fieldResource.getCount(); i++) {
				var aRecord = fieldResource.getAt(i);
				var permissionEval = aRecord.get("evaluatestotype").replace(/'/g,"");
				var permissionCheckedValue = permissionEval.split('-');
				var checkbox_editable = '<input type="checkbox" '+getcheckedInformation(permissionCheckedValue[5],permissionCheckedValue[0])+' name="check_editable_'+aRecord.get("resource_type")+'" id="check_editable_'+aRecord.get("resource_type")+'" value="check_editable_'+aRecord.get("resource")+'"   onclick="checkReadOnlyPermission(this)">';
				var checkbox_required = '<input type="checkbox" '+getcheckedInformation(permissionCheckedValue[5],permissionCheckedValue[1])+' name="check_required_'+aRecord.get("resource_type")+'" id="check_required_'+aRecord.get("resource_type")+'" value="check_required_'+aRecord.get("resource")+'" onclick="checkReadOnlyPermission(this)">';
				var checkbox_readonly = '<input type="checkbox" '+getcheckedInformation(permissionCheckedValue[0],permissionCheckedValue[1],permissionCheckedValue[2])+' name="check_readonly_'+aRecord.get("resource_type")+'" id="check_readonly_'+aRecord.get("resource_type")+'" value="check_readonly_'+aRecord.get("resource")+'"  onclick="checkReadOnlyPermission(this)">';
				var checkbox_hidden = '<input type="checkbox" '+getcheckedInformation(permissionCheckedValue[3])+' name="check_hidden_'+aRecord.get("resource_type")+'" id="check_hidden_'+aRecord.get("resource_type")+'" value="check_hidden_'+aRecord.get("resource")+'" onclick="setHiddenPermission(this)">';
				var checkbox_nooutput = '<input type="checkbox" '+getcheckedInformation(permissionCheckedValue[4])+' name="check_nooutput_'+aRecord.get("resource_type")+'" id="check_nooutput_'+aRecord.get("resource_type")+'" value="check_nooutput_'+aRecord.get("resource")+'" onclick="setHiddenPermission(this)">';
				
				var checkbox_selectAll = "";

				if(i==0){
					checkbox_selectAll = '<input type="checkbox" name="check_selectall" id="check_selectall" value="check_selectall" onclick="selectAllPermissions(\''+resourceType+'\');checkReadOnlyPermission(this);">';
				}
				permissionContainer +=  '<tr><td>'+aRecord.get("resource")+'</td><td>'+checkbox_editable+'</td><td>'+checkbox_required+'</td><td>'+checkbox_readonly+'</td><td>'+checkbox_hidden+'</td><td>'+checkbox_nooutput+'</td><td>'+checkbox_selectAll+'</td></tr>';
			}

			permissionContainer  +=  '</table></div><div class="floatRight"><input type="button" class="btn-primary btn" style="font-size: 11px;padding: 2px 7px;" value="Add"  onClick="setFieldPermission(\''+resourceType+'\')" /></div>';
			htmlContent = permissionContainer;
			document.getElementById('startFieldPermissionDiv').innerHTML = htmlContent;
			
		}else{
		if(paramFormId != '' && paramFormId.length >0 ){
			Ext.Ajax.request({
				url: "/bpm/form/getAllFormFields",
				params: {formId :paramFormId},
				asynchronous: false,
				method: 'GET',
				failure:function(response,options){

				},
				success:function(response,options){	
					
					var fieldData = response.responseText;
					var jsonFieldData = {};
					jsonFieldData = JSON.parse(fieldData.toString());
					var fieldParse = jsonFieldData.forms;
					//fieldResponseDataGlob = fieldParse;
					fieldResource.removeAll();
					var resourceRecord = fieldResource.recordType;	
					
					var fieldLength = fieldParse.length-1;
					for(var i= 0;i< fieldParse.length;i++){
						if(!fieldParse[i].id.endsWith("_FullName")) {
							var aRecordVal = new resourceRecord({
								resource_type: fieldParse[i].id,
								resource: fieldParse[i].name,
								resourceassignmentexpr: "fieldPermissions",
								language: fieldParse[i].type,
								evaluatestotype: '1-0-0-0-0',
							});
							fieldResource.add(aRecordVal);
							var checkbox_editable = '<input type="checkbox" name="check_editable_'+fieldParse[i].id+'" id="check_editable_'+fieldParse[i].id+'" value="check_editable_'+fieldParse[i].name+'"  checked="checked" onclick="checkReadOnlyPermission(this)">';
							
							var checkbox_required = '<input type="checkbox" name="check_required_'+fieldParse[i].id+'" id="check_required_'+fieldParse[i].id+'" value="check_required_'+fieldParse[i].name+'" onclick="checkReadOnlyPermission(this)" >';
							var checkbox_readonly = '<input type="checkbox" name="check_readonly_'+fieldParse[i].id+'" id="check_readonly_'+fieldParse[i].id+'" value="check_readonly_'+fieldParse[i].name+'"  disabled="true" onclick="checkReadOnlyPermission(this)">';
							var checkbox_hidden = '<input type="checkbox" name="check_hidden_'+fieldParse[i].id+'" id="check_hidden_'+fieldParse[i].id+'" value="check_hidden_'+fieldParse[i].name+'" onclick="setHiddenPermission(this)">';
							var checkbox_nooutput = '<input type="checkbox" name="check_nooutput_'+fieldParse[i].id+'" id="check_nooutput_'+fieldParse[i].id+'" value="check_nooutput_'+fieldParse[i].name+'" onclick="setHiddenPermission(this)">';
				
							var checkbox_selectAll = "";
							if(i==0){
								checkbox_selectAll = '<input type="checkbox" name="check_selectall" id="check_selectall" value="check_selectall" onclick="selectAllPermissions(\''+resourceType+'\');">';
							}
							permissionContainer = permissionContainer + '<tr><td>'+fieldParse[i].name+'</td><td>'+checkbox_editable+'</td><td>'+checkbox_required+'</td><td>'+checkbox_readonly+'</td><td>'+checkbox_hidden+'</td><td>'+checkbox_nooutput+'</td><td>'+checkbox_selectAll+'</td></tr>';
						}
						
					}
					permissionContainer  +=  '</table></div><div class="floatRight"><input type="button" class="btn-primary btn" style="font-size: 11px;padding: 2px 7px;" value="Add" onClick="setFieldPermission(\''+resourceType+'\')" /></div>';
					htmlContent = permissionContainer;
					document.getElementById('startFieldPermissionDiv').innerHTML = htmlContent;
				}
			});
	}
}
}

/**
 *
 * @param resourceType
 * @param paramFormId
 * @return {String}
 */
function generateStartPermissionJSON(resourceType,paramFormId){
   setFormPreviousId(resourceType,paramFormId,"StartPermission");  
    var resourceStoreMember = getExtjsJsonObjWithName(resourceType);
    var jsonString = "[";

    for (var i = 0; i < resourceStoreMember.getCount(); i++) {
        var aRecord = resourceStoreMember.getAt(i);
        jsonString += "{";
        jsonString += "'resource_type'" + ":'" +aRecord.get("resource_type") + "'," + "'resource'" + ":'" + aRecord.get("resource") + "',";
        if(aRecord.get("evaluatestotype").contains("'")){
        jsonString += "'evaluatestotype'" + ":" + aRecord.get("evaluatestotype");
        }else{
                jsonString += "'evaluatestotype'" + ":'" + aRecord.get("evaluatestotype") +"'";
        }
        jsonString += ",'language'"+":'"+aRecord.get("language")+"'";
	jsonString += "}";
        if (i < (resourceStoreMember.getCount() - 1)) {
            jsonString += ", ";
        }
    }
    jsonString += "]";
    return jsonString;
	

}

/**
 *
 * @param startFieldPermission
 * @param resourceType
 */
function reInitializeStartPermission(startFieldPermission,resourceType){
	var fieldResource = getExtjsJsonObjWithName(resourceType);
	fieldResource.removeAll();
	for(var i=0;i<startFieldPermission.length;i++){
		if(!startFieldPermission[i].resource.endsWith("_FullName")) {
			var resource_type = startFieldPermission[i].resource_type;
			var resource = startFieldPermission[i].resource;
			var resourceassignmentexpr = '';
			var evaluatestotype = startFieldPermission[i].evaluatestotype;
			var language = startFieldPermission[i].language;
			addFieldPermission(resource_type,resource,resourceassignmentexpr,evaluatestotype,fieldResource,language);
		}
	}

}

/**
 *
 * @param tresource,tresourceassignmentexpr
 * @param tresource_type,tevaluatestotype
 * @param operationStore, language
 */
function addStartFieldPermission(tresource_type,tresource,tresourceassignmentexpr,tevaluatestotype,operationStore,language){
	var recordIndex = findARecordInStore(tresource_type,tresource,tresourceassignmentexpr,operationStore);
	var resourceRecord = operationStore.recordType;	
	if(recordIndex == -1) {
		var aRecordVal = new resourceRecord({
			resource_type: tresource_type,
			resource: tresource,
			resourceassignmentexpr: tresourceassignmentexpr,
			language: language,
			evaluatestotype: tevaluatestotype,
		});
		operationStore.add(aRecordVal);
	}
}

/**
 *
 * @param resourceType
 * @param paramFormId
 * @param resourceValue 
 */
function setStartDynamicTransactorContent(paramFormId, resourceType , resourceValue ){
	var dynamicTransactorObj = getExtjsJsonObjByNameOFdynamicTransactor(resourceType);
	var dynamicTransactorHtmlContainer = "<div class='overflow height250'><table width='100%'>";
	var previousFormCheckFlag = checkPreviousFormId(resourceValue , paramFormId);
	if(dynamicTransactorObj.getCount() > 0 && previousFormCheckFlag == true ){
			var TRFlag = 0;
			for(var i=0; i<dynamicTransactorObj.getCount(); i++) {
				var aRecord = dynamicTransactorObj.getAt(i);
				if(TRFlag==3){
					dynamicTransactorHtmlContainer += "</tr><tr>";
					TRFlag = 0;
				}
				dynamicTransactorHtmlContainer += '<td><input type="radio" '+getDynamicTransactorchecked(aRecord.get("evaluatestotype"))+' name="dynamicTransactorForm" id="'+aRecord.get("resource_type").replace(/\s/g, "").toLowerCase()+'" value="'+aRecord.get("resource")+'" onClick="addDynamicTransactors(\''+aRecord.get("resource_type")+'\',\''+aRecord.get("resource")+'\',\''+resourceType+'\')" ><span class="pad-L5">'+aRecord.get("resource")+'</span></input></td>';
			
				TRFlag += 1;
			}
			var type = "noneRadio";
			dynamicFieldId = "None";
			dynamicTransactorHtmlContainer  +=  '<td><input type="radio" id="noneRadio" name="dynamicTransactorForm" onClick="addDynamicTransactors(\''+type+'\',\''+dynamicFieldId+'\',\''+resourceType+'\')"/>None</td></tr></table></div>';
			document.getElementById('startDynamicTransactorDiv').innerHTML = dynamicTransactorHtmlContainer;
		}else {
		Ext.Ajax.request({
				url: "/bpm/form/getAllFormFields",
				params: {formId :paramFormId},
				asynchronous: false,
				method: 'GET',
				failure:function(response,options){
					//alert("failure");
					Ext.Msg.show({
						title:'Message',
						msg: ORYX.I18N.Feedback.failure,
						buttons: Ext.Msg.OK,
						animEl: 'elId',
						icon: Ext.MessageBox.ERROR
				});
				},
				success:function(response,options){	
					previousStartDynamicFormId = paramFormId;
					var fieldData = response.responseText;
					var jsonFieldData = {};
					jsonFieldData = JSON.parse(fieldData.toString());
					var fieldParse = jsonFieldData.forms;
					//fieldResponseDataGlob = fieldParse;
					var aRecord = dynamicTransactorObj.getAt(0);
					var dynamicTransactorId ;//= aRecord.get("resource_type");
				
					dynamicTransactorObj.removeAll();
					var resourceRecord = dynamicTransactorObj.recordType;	
				
					var dynamicFieldId;
					var fieldLength = fieldParse.length-1;
					var TRFlag = 0;
					for(var i= 0;i< fieldParse.length;i++){
						if(!fieldParse[i].id.endsWith("_FullName")) {
							var tpEvaluatesToType = "";
							if(dynamicTransactorId == fieldParse[i].id.replace(/\s/g, "").toLowerCase()){
								tpEvaluatesToType = '1';
							}else{
								tpEvaluatesToType = '0';
							}
							var aRecordVal = new resourceRecord({
								resource_type: fieldParse[i].id,//.replace(/\s/g, "").toLowerCase(),
								resource: fieldParse[i].name,
								resourceassignmentexpr: "dynamicTransactor",
								language: fieldParse[i].type,
								evaluatestotype: tpEvaluatesToType,
							});
							dynamicTransactorObj.add(aRecordVal);
							if(TRFlag==3){
								dynamicTransactorHtmlContainer += "</tr><tr>";
								TRFlag = 0;
							}
							dynamicTransactorHtmlContainer += '<td><input type="radio" name="dynamicTransactorForm" id="'+fieldParse[i].id.replace(/\s/g, "").toLowerCase()+'_elm" '+dynamicTransactorSelect(dynamicTransactorId,fieldParse[i].id.replace(/\s/g, "").toLowerCase())+' value="'+fieldParse[i].name+'" onClick="addDynamicTransactors(\''+fieldParse[i].id.replace(/\s/g, "").toLowerCase()+'\',\''+fieldParse[i].name+'\',\''+resourceType+'\')" ><span class="pad-L5">'+fieldParse[i].name+'</span></input></td>';
						
						TRFlag += 1;
						} 
					
				}
				var type = "noneRadio";
				dynamicFieldId = "None";
				
					dynamicTransactorHtmlContainer  +=  '<td><input type="radio" id="noneRadio" name="dynamicTransactorForm" onClick="addDynamicTransactors(\''+type+'\',\''+dynamicFieldId+'\',\''+resourceType+'\')"/>None</td></tr></table></div>';
					document.getElementById('startDynamicTransactorDiv').innerHTML = dynamicTransactorHtmlContainer;
				}
			});
	}
}

/**
 *
 * @param resourceType
 * @param paramFormId
 * @return {String} 
 */
function generateStartDynamicTransactorJSON(resourceType,paramFormId){
	setFormPreviousId(resourceType,paramFormId,"StartDynamicTransactor");
	var dynamicTransactorObj = getExtjsJsonObjByNameOFdynamicTransactor(resourceType);
	var fieldLength = dynamicTransactorObj.getCount() - 1;
	var consJsonContent = "'dynamicTransactor':[";
	
	for(var i=0; i<dynamicTransactorObj.getCount(); i++) {
		var aRecord = dynamicTransactorObj.getAt(i);
		if(aRecord.get("evaluatestotype") == "1"){
			consJsonContent +=  "'"+aRecord.get("resource")+"','"+aRecord.get("resource_type")+"','"+aRecord.get("language")+"'";
		}
	}
	consJsonContent += "]";
	reinitializedFlag = 0;
	return consJsonContent;

}

/**
 *
 * @param fieldParse
 * @param dynamicTransactor
 * @param resourceType
 */
function reInitializeStartDynamicTransactor(fieldParse,dynamicTransactor,resourceType){
	var dynamicTransactorObj = getExtjsJsonObjByNameOFdynamicTransactor(resourceType);
	var resourceRecord = dynamicTransactorObj.recordType;	
	dynamicTransactorObj.removeAll();
	for(var i= 0;i< fieldParse.length;i++){
		if(!fieldParse[i].resource.endsWith("_FullName")) {
			var evaluatestotypeRecord = 0;
			if(dynamicTransactor == fieldParse[i].resource_type){
				evaluatestotypeRecord = 1;
			}
			var aRecordVal = new resourceRecord({
			resource_type:fieldParse[i].resource_type,
			resource: fieldParse[i].resource,
			resourceassignmentexpr: "dynamicTransactor",
			language: fieldParse[i].language,
			evaluatestotype: evaluatestotypeRecord,
			});
			dynamicTransactorObj.add(aRecordVal);
		}
	}
}


/**
 * 
 * @param fieldResource
 * @param paramFormId
 * @param resourceType
 */
function setFieldPermissionContent(fieldResource,paramFormId,resourceType,resourceValue){
	var checkbox_selectAll = '<input type="checkbox" name="check_selectall" id="check_selectall" value="check_selectall" onclick="selectAllPermissions(\''+resourceType+'\');checkReadOnlyPermission(this);">';
	var selectAllFieldEle  = "<tr class='height-30px'><td><b>"+ORYX.I18N.PropertyWindow.selectAll+"</b></td><td>"+checkbox_selectAll+"</td></tr>";
	var permissionContainer = "<div class='overflow height250'><table width='100%'>"+selectAllFieldEle+"<tr><td><b>"+ORYX.I18N.PropertyWindow.premission+"</b></td><td><b>"+ORYX.I18N.PropertyWindow.editable+"</b></td><td><b>"+ORYX.I18N.PropertyWindow.required+"<b></td><td><b>"+ORYX.I18N.PropertyWindow.readOnly+"</b></td><td><b>"+ORYX.I18N.PropertyWindow.hidden+"</b></td><td><b>"+ORYX.I18N.PropertyWindow.noOutput+"</b></td></tr>";
	var previousFormCheckFlag = checkPreviousFormId(resourceValue , paramFormId);
	//if(resourceValue == "" || resourceValue == null){
		if(fieldResource.getCount() > 0 && previousFormCheckFlag == true){
			for(var i=0; i<fieldResource.getCount(); i++) {
				var aRecord = fieldResource.getAt(i);
				var permissionEval = aRecord.get("evaluatestotype").replace(/'/g,"");
				var permissionCheckedValue = permissionEval.split('-');
				var checkbox_editable = '<input type="checkbox" '+getcheckedInformation(permissionCheckedValue[5],permissionCheckedValue[0])+' name="check_editable_'+aRecord.get("resource_type")+'" id="check_editable_'+aRecord.get("resource_type")+'" value="check_editable_'+aRecord.get("resource")+'"   onclick="checkReadOnlyPermission(this)">';
				var checkbox_required = '<input type="checkbox" '+getcheckedInformation(permissionCheckedValue[5],permissionCheckedValue[1])+' name="check_required_'+aRecord.get("resource_type")+'" id="check_required_'+aRecord.get("resource_type")+'" value="check_required_'+aRecord.get("resource")+'" onclick="checkReadOnlyPermission(this)">';
				var checkbox_readonly = '<input type="checkbox" '+getcheckedInformation(permissionCheckedValue[0],permissionCheckedValue[1],permissionCheckedValue[2])+' name="check_readonly_'+aRecord.get("resource_type")+'" id="check_readonly_'+aRecord.get("resource_type")+'" value="check_readonly_'+aRecord.get("resource")+'"  onclick="checkReadOnlyPermission(this)">';
				var checkbox_hidden = '<input type="checkbox" '+getcheckedInformation(permissionCheckedValue[3])+' name="check_hidden_'+aRecord.get("resource_type")+'" id="check_hidden_'+aRecord.get("resource_type")+'" value="check_hidden_'+aRecord.get("resource")+'" onclick="setHiddenPermission(this)">';
				var checkbox_nooutput = '<input type="checkbox" '+getcheckedInformation(permissionCheckedValue[4])+' name="check_nooutput_'+aRecord.get("resource_type")+'" id="check_nooutput_'+aRecord.get("resource_type")+'" value="check_nooutput_'+aRecord.get("resource")+'" onclick="setHiddenPermission(this)">';
				permissionContainer +=  '<tr><td>'+aRecord.get("resource")+'</td><td>'+checkbox_editable+'</td><td>'+checkbox_required+'</td><td>'+checkbox_readonly+'</td><td>'+checkbox_hidden+'</td><td>'+checkbox_nooutput+'</td></tr>';
			}
			permissionContainer  +=  '</table></div><div class="floatRight"><input type="button" value="Add" class="btn-primary btn" style="font-size: 11px;padding: 2px 7px;" onClick="setFieldPermission(\''+resourceType+'\')" /></div>';
			htmlContent = permissionContainer;
			document.getElementById('fieldPermissionDiv').innerHTML = htmlContent;
			
		}else{
			//previousFormId = paramFormId;
			Ext.Ajax.request({
				url: "/bpm/form/getAllFormFields",
				params: {formId :paramFormId},
				asynchronous: false,
				method: 'GET',
				failure:function(response,options){

				},
				success:function(response,options){	
					var fieldData = response.responseText;
					var jsonFieldData = {};
					jsonFieldData = JSON.parse(fieldData.toString());
					var fieldParse = jsonFieldData.forms;
					//fieldResponseDataGlob = fieldParse;
					fieldResource.removeAll();
					var resourceRecord = fieldResource.recordType;	

					var fieldLength = fieldParse.length-1;
					var evaluateType = (resourceType == ORYX.I18N.PropertyWindow.reader) ? '0-0-1-0-0' : '1-0-0-0-0';
					for(var i= 0;i< fieldParse.length;i++){
						if(!fieldParse[i].id.endsWith("_FullName")) {
							var aRecordVal = new resourceRecord({
								resource_type: fieldParse[i].id,
								resource: fieldParse[i].name,
								resourceassignmentexpr: "fieldPermissions",
								language: '',
								evaluatestotype: evaluateType ,
							});
							fieldResource.add(aRecordVal);
							setFormPreviousId(resourceType,paramFormId,"FieldPermission");
							var checkbox_editable = "";
							var checkbox_readonly = "";
							if(resourceType == ORYX.I18N.PropertyWindow.reader){
								checkbox_editable = '<input type="checkbox" name="check_editable_'+fieldParse[i].id+'" id="check_editable_'+fieldParse[i].id+'" value="check_editable_'+fieldParse[i].name+'"  onclick="checkReadOnlyPermission(this)">';
								checkbox_readonly = '<input type="checkbox" name="check_readonly_'+fieldParse[i].id+'" id="check_readonly_'+fieldParse[i].id+'" value="check_readonly_'+fieldParse[i].name+'" checked="checked" onclick="checkReadOnlyPermission(this)">';
							}else{
								checkbox_editable = '<input type="checkbox" name="check_editable_'+fieldParse[i].id+'" id="check_editable_'+fieldParse[i].id+'" value="check_editable_'+fieldParse[i].name+'" checked="checked" onclick="checkReadOnlyPermission(this)">';
								checkbox_readonly = '<input type="checkbox" name="check_readonly_'+fieldParse[i].id+'" id="check_readonly_'+fieldParse[i].id+'" value="check_readonly_'+fieldParse[i].name+'" disabled="true" onclick="checkReadOnlyPermission(this)">';
							}
							var checkbox_required = '<input type="checkbox" name="check_required_'+fieldParse[i].id+'" id="check_required_'+fieldParse[i].id+'" value="check_required_'+fieldParse[i].name+'" onclick="checkReadOnlyPermission(this)">';
							//var checkbox_readonly = '<input type="checkbox" name="check_readonly_'+fieldParse[i].id+'" id="check_readonly_'+fieldParse[i].id+'" value="check_readonly_'+fieldParse[i].name+'" checked="checked" onclick="checkReadOnlyPermission(this)">';
							var checkbox_hidden = '<input type="checkbox" name="check_hidden_'+fieldParse[i].id+'" id="check_hidden_'+fieldParse[i].id+'" value="check_hidden_'+fieldParse[i].name+'" onclick="setHiddenPermission(this)">';
							var checkbox_nooutput = '<input type="checkbox" name="check_nooutput_'+fieldParse[i].id+'" id="check_nooutput_'+fieldParse[i].id+'" value="check_nooutput_'+fieldParse[i].name+'" onclick="setHiddenPermission(this)">';
							permissionContainer = permissionContainer + '<tr><td>'+fieldParse[i].name+'</td><td>'+checkbox_editable+'</td><td>'+checkbox_required+'</td><td>'+checkbox_readonly+'</td><td>'+checkbox_hidden+'</td><td>'+checkbox_nooutput+'</td></tr>';
						}
					}
					permissionContainer  +=  '</table></div><div class="floatRight"><input type="button" value="Add" class="btn-primary btn" style="font-size: 11px;padding: 2px 7px;" onClick="setFieldPermission(\''+resourceType+'\')" /></div>';
					htmlContent = permissionContainer;
					document.getElementById('fieldPermissionDiv').innerHTML = htmlContent;
				}
			});
		}

	/*}else{
		if(fieldResource.getCount() > 0){
			for(var i=0; i<fieldResource.getCount(); i++) {
				var aRecord = fieldResource.getAt(i);
				var permissionEval = aRecord.get("evaluatestotype").replace(/'/g,"");
				var permissionCheckedValue = permissionEval.split('-');
				var checkbox_editable = '<input type="checkbox" '+getcheckedInformation(permissionCheckedValue[0])+' name="check_editable_'+aRecord.get("resource_type")+'" id="check_editable_'+aRecord.get("resource_type")+'" value="check_editable_'+aRecord.get("resource")+'">';
				var checkbox_required = '<input type="checkbox" '+getcheckedInformation(permissionCheckedValue[1])+' name="check_required_'+aRecord.get("resource_type")+'" id="check_required_'+aRecord.get("resource_type")+'" value="check_required_'+aRecord.get("resource")+'">';
				var checkbox_readonly = '<input type="checkbox" '+getcheckedInformation(permissionCheckedValue[2])+' name="check_readonly_'+aRecord.get("resource_type")+'" id="check_readonly_'+aRecord.get("resource_type")+'" value="check_readonly_'+aRecord.get("resource")+'">';
				var checkbox_hidden = '<input type="checkbox" '+getcheckedInformation(permissionCheckedValue[3])+' name="check_hidden_'+aRecord.get("resource_type")+'" id="check_hidden_'+aRecord.get("resource_type")+'" value="check_hidden_'+aRecord.get("resource")+'">';
				var checkbox_nooutput = '<input type="checkbox" '+getcheckedInformation(permissionCheckedValue[4])+' name="check_nooutput_'+aRecord.get("resource_type")+'" id="check_nooutput_'+aRecord.get("resource_type")+'" value="check_nooutput_'+aRecord.get("resource")+'">';
				
				var checkbox_selectAll = "";

				if(i==0){
					checkbox_selectAll = '<input type="checkbox" name="check_selectall" id="check_selectall" value="check_selectall" onclick="selectAllPermissions(\''+resourceType+'\');">';
				}
				permissionContainer +=  '<tr><td>'+aRecord.get("resource")+'</td><td>'+checkbox_editable+'</td><td>'+checkbox_required+'</td><td>'+checkbox_readonly+'</td><td>'+checkbox_hidden+'</td><td>'+checkbox_nooutput+'</td><td>'+checkbox_selectAll+'</td></tr>';
			}
			permissionContainer  +=  '</table>';
			htmlContent = permissionContainer;
			document.getElementById('fieldPermissionDiv').innerHTML = htmlContent;

		}
	}*/
	
}

/** 
 * @param paramFormId
 * retrun fieldLength
 */
function getFormCountLength(paramFormId){
	var fieldLength = 0;
	Ext.Ajax.request({
			url: "/bpm/form/getAllFormFields",
			params: {formId :paramFormId},
			asynchronous: false,
			method: 'GET',
			failure:function(response,options){
				},
			success:function(response,options){	
				var fieldData = response.responseText;
				var jsonFieldData = {};
				jsonFieldData = JSON.parse(fieldData.toString());
				var fieldParse = jsonFieldData.forms;
				fieldLength = fieldParse.length;
			},
	});
		
	return 	fieldLength;		
}

/**
 *
 * @param hiddenField
 */
function setHiddenPermission(hiddenField){
	var id = hiddenField.id;
	var fieldId = id.split("_");
	var permissionFieldId = "";
	for(var i=2;i<fieldId.length;i++){
	if(permissionFieldId.length==0){
			permissionFieldId += fieldId[i];
		}else{
			permissionFieldId += "_"+fieldId[i];
		}
	}
	if(document.getElementById(id).checked == true){
		document.getElementById("check_required_"+permissionFieldId).checked = false;
		document.getElementById("check_readonly_"+permissionFieldId).checked = false;
		document.getElementById("check_editable_"+permissionFieldId).checked = false;
		if(document.getElementById("check_nooutput_"+permissionFieldId).checked == true){
			document.getElementById("check_hidden_"+permissionFieldId).checked = false;
			document.getElementById('check_hidden_'+permissionFieldId).disabled = true;
		}
		if(document.getElementById("check_hidden_"+permissionFieldId).checked == true){
			document.getElementById("check_nooutput_"+permissionFieldId).checked = false;
			document.getElementById('check_nooutput_'+permissionFieldId).disabled = true;
			
		}
		document.getElementById("check_required_"+permissionFieldId).disabled = true;
		document.getElementById("check_readonly_"+permissionFieldId).disabled = true;
		document.getElementById("check_editable_"+permissionFieldId).disabled = true;
		
	
	}else{
		document.getElementById('check_hidden_'+permissionFieldId).disabled = false;
		document.getElementById('check_nooutput_'+permissionFieldId).disabled = false;
		document.getElementById("check_required_"+permissionFieldId).disabled = false;
		document.getElementById("check_readonly_"+permissionFieldId).disabled = false;
		document.getElementById("check_editable_"+permissionFieldId).disabled = false;
		
	}
		document.getElementById("check_selectall").checked = false;
	
}



/**
 * 
 * @param checkFieldId
 */
function checkReadOnlyPermission(checkFieldId) {
	var id = checkFieldId.id;
	var checkboxId = id.split("_");
	var singleFieldId = "";
	for(var i=2;i<checkboxId.length;i++){
		if(singleFieldId.length==0){
			singleFieldId += checkboxId[i];
		}else{
			singleFieldId += "_"+checkboxId[i];
		}
	}
	if (singleFieldId!="" && singleFieldId.length>0) {
		if (document.getElementById("check_required_" + singleFieldId).checked == true) {
			if (document.getElementById("check_editable_" + singleFieldId).checked == true) {
				document.getElementById("check_readonly_" + singleFieldId).checked = false;
				document.getElementById("check_readonly_" + singleFieldId).disabled = true;
			} else {
				if (documentIdCheck == 0) {
					document.getElementById("check_editable_" + singleFieldId).checked = false;
					document.getElementById("check_required_" + singleFieldId).checked = false
					document.getElementById("check_readonly_" + singleFieldId).checked = false;
					document.getElementById("check_readonly_" + singleFieldId).disabled = false;
					documentIdCheck = 1;
				} else {
					document.getElementById("check_editable_" + singleFieldId).checked = true;
					document.getElementById("check_required_" + singleFieldId).checked = true
					document.getElementById("check_readonly_" + singleFieldId).checked = false;
					document.getElementById("check_readonly_" + singleFieldId).disabled = true;
					documentIdCheck = 0;
				}
			}
		} else {
			if (document.getElementById("check_editable_" + singleFieldId).checked == true) {
				/*if (singleIdCheck == 0) {
					document.getElementById("check_editable_" + singleFieldId).checked = false;
					document.getElementById("check_readonly_" + singleFieldId).checked = false;
					document.getElementById("check_readonly_" + singleFieldId).disabled = false;
					singleIdCheck = 1;
				} */
					document.getElementById("check_editable_" + singleFieldId).checked = true;
					document.getElementById("check_readonly_" + singleFieldId).checked = false;
					document.getElementById("check_readonly_" + singleFieldId).disabled = true;
					singleIdCheck = 0;
			} else {
				if (document.getElementById("check_readonly_" + singleFieldId).checked == false) {
					document.getElementById("check_required_" + singleFieldId).disabled = false;
					document.getElementById("check_editable_" + singleFieldId).disabled = false;
					document.getElementById("check_readonly_" + singleFieldId).disabled = false;
				} else {
					document.getElementById("check_readonly_" + singleFieldId).checked = true;
					document.getElementById("check_readonly_" + singleFieldId).disabled = false;
					document.getElementById("check_editable_" + singleFieldId).disabled = true;
					document.getElementById("check_required_" + singleFieldId).disabled = true;
				}
			}
		}
	}
}



/**
 * 
 * @param paramFormId
 * @param resourceType
 * @param resourceValue
 */
function setDynamicTransactorContent(paramFormId,resourceType,resourceValue){
	if(reinitializedFlag == 1){
		var dynamicTransactorObj = getExtjsJsonObjByNameOFdynamicTransactor(resourceType);
		var dynamicTransactorHtmlContainer  = "";
		var count = dynamicTransactorObj.getCount();
		dynamicTransactorHtmlContainer = "<div class='overflow height250'><table width='100%'>";
		var previousFormCheckFlag = checkPreviousFormId(resourceValue , paramFormId);
		var dynamicFieldId;
		if(count > 0 && previousFormCheckFlag == true){
			var TRFlag = 0;
			for(var i=0; i<dynamicTransactorObj.getCount(); i++) {
				var aRecord = dynamicTransactorObj.getAt(i);
				if(TRFlag==3){
					dynamicTransactorHtmlContainer += "</tr><tr>";
					TRFlag = 0;
				}
				dynamicTransactorHtmlContainer += '<td><input type="radio" '+getDynamicTransactorchecked(aRecord.get("evaluatestotype"))+' name="dynamicTransactorForm" id="'+aRecord.get("resource_type").replace(/\s/g, "").toLowerCase()+'" value="'+aRecord.get("resource")+'" onClick="addDynamicTransactors(\''+aRecord.get("resource_type")+'\',\''+aRecord.get("resource")+'\',\''+resourceType+'\')" ><span class="pad-L5">'+aRecord.get("resource")+'</span></input></td>';
			
				TRFlag += 1;
			}
			var type = "noneRadio";
			dynamicFieldId = "None";
			dynamicTransactorHtmlContainer  +=  '<td><input type="radio" id="noneRadio" name="dynamicTransactorForm" onClick="addDynamicTransactors(\''+type+'\',\''+dynamicFieldId+'\',\''+resourceType+'\')"/>None</td></tr></table></div>';
			document.getElementById('dynamicTransactorDiv').innerHTML = dynamicTransactorHtmlContainer;
		}else{
			Ext.Ajax.request({
				url: "/bpm/form/getAllFormFields",
				params: {formId :paramFormId},
				asynchronous: false,
				method: 'GET',
				failure:function(response,options){
					//alert("failure");
					Ext.Msg.show({
						title:'Message',
						msg: ORYX.I18N.Feedback.failure,
						buttons: Ext.Msg.OK,
						animEl: 'elId',
						icon: Ext.MessageBox.ERROR
					});
				},
				success:function(response,options){	
					var fieldData = response.responseText;
					var jsonFieldData = {};
					jsonFieldData = JSON.parse(fieldData.toString());
					var fieldParse = jsonFieldData.forms;
					//fieldResponseDataGlob = fieldParse;
					var aRecord = dynamicTransactorObj.getAt(0);
					var dynamicTransactorId ;//= aRecord.get("resource_type");
				
					dynamicTransactorObj.removeAll();
					var resourceRecord = dynamicTransactorObj.recordType;	
				
					var dynamicFieldId;
					var fieldLength = fieldParse.length-1;
					var TRFlag = 0;
					for(var i= 0;i< fieldParse.length;i++){
						if(!fieldParse[i].id.endsWith("_FullName")) {
							var tpEvaluatesToType = "";
							if(dynamicTransactorId != undefined && (dynamicTransactorId.replace(/\s/g, "").toLowerCase() == fieldParse[i].id.replace(/\s/g, "").toLowerCase())){
								tpEvaluatesToType = '1';
							}else{
								tpEvaluatesToType = '0';
							}
							var aRecordVal = new resourceRecord({
								resource_type: fieldParse[i].id,//replace(/\s/g, "").toLowerCase(),
								resource: fieldParse[i].name,
								resourceassignmentexpr: "dynamicTransactor",
								language: fieldParse[i].type,
								evaluatestotype: tpEvaluatesToType,
							});
							dynamicTransactorObj.add(aRecordVal);
							if(TRFlag==3){
								dynamicTransactorHtmlContainer += "</tr><tr>";
								TRFlag = 0;
							}
							dynamicTransactorHtmlContainer += '<td><input type="radio" name="dynamicTransactorForm" id="'+fieldParse[i].id.replace(/\s/g, "").toLowerCase()+'_elm" '+dynamicTransactorSelect(dynamicTransactorId,fieldParse[i].id.replace(/\s/g, "").toLowerCase())+' value="'+fieldParse[i].name+'" onClick="addDynamicTransactors(\''+fieldParse[i].id.replace(/\s/g, "").toLowerCase()+'\',\''+fieldParse[i].name+'\',\''+resourceType+'\')" ><span class="pad-L5">'+fieldParse[i].name+'</span></input></td>';
						TRFlag += 1;
						}
				}
				var type = "noneRadio";
				dynamicFieldId = "None";
				
					dynamicTransactorHtmlContainer  +=  '<td><input type="radio" id="noneRadio" name="dynamicTransactorForm" onClick="addDynamicTransactors(\''+type+'\',\''+dynamicFieldId+'\',\''+resourceType+'\')"/>None</td></tr></table></div>';
					document.getElementById('dynamicTransactorDiv').innerHTML = dynamicTransactorHtmlContainer;
				}
			});
		}
		reinitializedFlag = 0;
	}else{
		setTimeout(function(){
			setDynamicTransactorContent(paramFormId,resourceType,resourceValue);
		},100);
	}
}

/**
 * @param dynamicTransactorId,currentDynamicTransactorId
 * @retrun {String}
 */
function dynamicTransactorSelect(dynamicTransactorId,currentDynamicTransactorId){
	if(dynamicTransactorId == currentDynamicTransactorId){
		return 'checked="checked"';
	}else{
		return "";
	}
}

/**
 * 
 * @param id
 * @param name
 * @param resourceType
 */
function addDynamicTransactors(id,name,resourceType){
	var dynamicTransactorObj = getExtjsJsonObjByNameOFdynamicTransactor(resourceType);
	var dynamicTransactorValueList = new Array();  
	for(var i=0; i<dynamicTransactorObj.getCount(); i++) {
		var aRecord = dynamicTransactorObj.getAt(i);
		var dynamicTransactorValueMap = {};
		dynamicTransactorValueMap['resource_type'] = aRecord.get("resource_type");
		dynamicTransactorValueMap['resource'] = aRecord.get("resource");
		dynamicTransactorValueMap['resourceassignmentexpr'] = aRecord.get("resourceassignmentexpr");
		dynamicTransactorValueMap['language'] = aRecord.get("language");
		
		if(aRecord.get("resource_type").toLowerCase() == id.toLowerCase()){
			dynamicTransactorValueMap['evaluatestotype']  = "1";
		}else{
			dynamicTransactorValueMap['evaluatestotype']  = "0";
			
		}
		dynamicTransactorValueList[i] = dynamicTransactorValueMap;

	}
	updateDynamicTransactor(dynamicTransactorValueList,resourceType);
}

/**
 * 
 * @param resource_type
 * @param resource
 * @param resourceassignmentexpr
 * @param evaluatestotype
 * @param resourceType
 */
function updateDynamicTransactorMultiplexing(resource_type,resource,resourceassignmentexpr,evaluatestotype,language,resourceType){
	var dynamicTransactorObj = getExtjsJsonObjByNameOFdynamicTransactor(resourceType);
	removeDynamicTransactor(resource_type,resource,resourceassignmentexpr,dynamicTransactorObj);
	addDynamicTransactor(resource_type,resource,resourceassignmentexpr,evaluatestotype,language,dynamicTransactorObj);
}

/**
 * 
 * @param dmicTransList
 * @param resourceType
 */
function updateDynamicTransactor(dmicTransList,resourceType){
	for(var i=0;i<dmicTransList.length;i++){
		updateDynamicTransactorMultiplexing(dmicTransList[i].resource_type,dmicTransList[i].resource,dmicTransList[i].resourceassignmentexpr,dmicTransList[i].evaluatestotype,dmicTransList[i].language,resourceType);
	}
}

/**
 * 
 * @param resource_type
 * @param resource
 * @param resourceassignmentexpr
 * @param operationStore
 */
function removeDynamicTransactor(resource_type,resource,resourceassignmentexpr,operationStore){
    var recordIndex = findARecordInStore(resource_type,resource,resourceassignmentexpr,operationStore);
	if(recordIndex != -1) {
		operationStore.remove(operationStore.getAt(recordIndex));
		operationStore.commitChanges();
	}
}

/**
 * 
 * @param tresource_type
 * @param tresource
 * @param tresourceassignmentexpr
 * @param tevaluatestotype
 * @param operationStore
 */
function addDynamicTransactor(tresource_type,tresource,tresourceassignmentexpr,tevaluatestotype,tlanguage,operationStore){
	var recordIndex = findARecordInStore(tresource_type,tresource,tresourceassignmentexpr,operationStore);
	var resourceRecord = operationStore.recordType;	
	if(recordIndex == -1) {
		var aRecordVal = new resourceRecord({
			resource_type: tresource_type,
			resource: tresource,
			resourceassignmentexpr: tresourceassignmentexpr,
			language: tlanguage,
			evaluatestotype: tevaluatestotype,
		});
		operationStore.add(aRecordVal);
	}
}


/**
 * 
 * @param checkedValue
 * @returns {String}
 */
function getDynamicTransactorchecked(checkedValue){
	if(checkedValue == '1'){
		return 'checked="checked"';
	}else{
		return '';
	}
}

/**
 * 
 * @param formStore
 * @param value
 */
function getFormVersionName(formStore,value) {
	Ext.Ajax.request({
		waitMsg: 'Saving changes...',
		method: 'GET',
		headers: {'Content-Type': 'application/json'},
		url:'/form/getLatestFormVersion',
		params: {formId:value},
		failure:function(response){
			//alert("failure");

			Ext.Msg.show({
				title:'Message',
				msg: ORYX.I18N.Feedback.failure,
				buttons: Ext.Msg.OK,
				animEl: 'elId',
				icon: Ext.MessageBox.ERROR
			});
		},
		success:function(response){
			var responseObj = Ext.util.JSON.decode(response.responseText);
			var resourceRecord = formStore.recordType;
			
		 var aRecordVal = new resourceRecord({
		                    id: responseObj.forms.id,
		                    formName: responseObj.forms.formName,
		                });

		var isRecordPresent =  formStore.findBy(function (record, id) {
		        if (record.get('id') == value){
		            return true; // a record with this data exists
		        }
		        return false; // there is no record in the store with this dataoperationStore
		    });	
		 if(isRecordPresent == -1) {
			 formStore.add(aRecordVal);
		 }
		var newindex = formStore.find("id", value);
		var aNewRec = formStore.getAt(newindex);
        return aNewRec.get("formName");
		}
		
	});
}

/**
 * 
 * @param formStore
 * @param value
 */
function getAndSetFormVersionName(formStore,value,record,gridColumn) {
	Ext.Ajax.request({
		waitMsg: 'Saving changes...',
		method: 'GET',
		headers: {'Content-Type': 'application/json'},
		url:'/form/getLatestFormVersion',
		params: {formId:value},
		failure:function(response){
			//alert("failure");
			Ext.Msg.show({
				title:'Message',
				msg: ORYX.I18N.Feedback.failure,
				buttons: Ext.Msg.OK,
				animEl: 'elId',
				icon: Ext.MessageBox.ERROR
			});
		},
		success:function(response){
			var responseObj = Ext.util.JSON.decode(response.responseText);
			var resourceRecord = formStore.recordType;
			var aRecordVal = new resourceRecord({
		                    id: responseObj.forms.id,
		                    formName: responseObj.forms.formName,
		                });
			var isRecordPresent =  formStore.findBy(function (record, id) {
		        if (record.get('id') == value){
		            return true; // a record with this data exists
		        }
		        return false; // there is no record in the store with this dataoperationStore
		    });	
			if(isRecordPresent == -1) {
				formStore.add(aRecordVal);
				record.set(gridColumn,responseObj.forms.formName);
			}
		}
	});
}

/**
 * 
 * @param resourceValue
 * @param resourceType
 * @param memberFieldType
 */
function reInitializeObjectObj(resourceValue,resourceType,memberFieldType,paramFormId){
 	var strReplace = "";
	if(memberFieldType == "operationPermission"){
		strReplace = '"'+resourceValue.replace('$','').replace(/^\s+|\s+$/g,"")+'"';
	} else if(memberFieldType == "startScript" || memberFieldType == "endScript"){
		strReplace = '"'+resourceValue.replace('$','').replace(/^\s+|\s+$/g,"")+'"';
	}else if(memberFieldType == "TransactorRelationship"){
		strReplace = '"'+resourceValue.replace('$','').replace(/^\s+|\s+$/g,"")+'"';
	}else{
		strReplace = '"['+resourceValue.replace('$','').replace(/^\s+|\s+$/g,"")+']"';
	}
	var resourceJSON = eval(JSON.parse(strReplace));
	if(memberFieldType == "membersFields"){
		var fieldPermissions = resourceJSON[0].fieldsPersmission[0];
		var members = resourceJSON[0].members[0].items;	
		reInitializeMemberObj(members,resourceType);
		reInitializePermissionObj(fieldPermissions,resourceType);
		//previousFormCheckFlag = true;
		var dynamicTransactor = resourceJSON[0].dynamicTransactor[0];	
		reInitializeDynamicTransactor(dynamicTransactor,resourceType,paramFormId);
	}else if(memberFieldType == "operationPermission"){
		var operationPermissions = resourceJSON[0].items;
		reInitializeOperationPermissionObj(operationPermissions,resourceType);
	}else if(memberFieldType == "membersPermission"){
		var members = resourceJSON[0].members[0].items;	
		reInitializeMemberObj(members,resourceType);
		if(resourceType != ORYX.I18N.PropertyWindow.admin){
			var dynamicTransactor = resourceJSON[0].dynamicTransactor[0];	
			reInitializeDynamicTransactor(dynamicTransactor,resourceType,paramFormId);
		}
	}else if(memberFieldType == "membersPermissionEnd"){
		var members = resourceJSON[0].members[0].items;	
		reInitializeMemberObj(members,resourceType);
	} else if(memberFieldType == "membersPermissionprocess"){
		var members = resourceJSON[0].processMembers[0].items;	
		reInitializeMemberObj(members,resourceType);
		
	}else if(memberFieldType == "startScript"){
		var startScriptName = resourceJSON[0].scriptName;
		var startScriptFunction = resourceJSON[0].scriptFunction;
		reInitializeStartEndEvent(startScriptName,startScriptFunction,resourceType);
	} else if(memberFieldType == "endScript"){
		var endScriptName = resourceJSON[0].scriptName;
		var endScriptFunction = resourceJSON[0].scriptFunction;
		reInitializeStartEndEvent(endScriptName,endScriptFunction,resourceType);
	}else if(memberFieldType == "Notification"){
		var notificationObj = notificationStore;
		var notificationJson = resourceJSON[0];
		reInitializeNotificationObj(notificationJson,notificationStore);
	}else if(memberFieldType == "TransactorRelationship"){
		reInitializeTransactorRelationshipObj(resourceJSON);
	}else if(memberFieldType == "startPermission"){
		var startFieldPermission = resourceJSON[0].startPermission;
		reInitializeStartPermission(startFieldPermission,resourceType);
		var dynamicTransactor = resourceJSON[0].dynamicTransactor[1];	
		reInitializeStartDynamicTransactor(startFieldPermission,dynamicTransactor,resourceType);
		
	}else if(memberFieldType == "formFieldAutomatic"){
		var formFieldAutomatic = resourceJSON[0].formFieldAutomatic;
		reInitializeFromFieldAutomaticObj(formFieldAutomatic,paramFormId);
	}else{
		var fieldPermissions = resourceJSON[0].fieldsPersmission[0];
		reInitializePermissionObj(fieldPermissions,resourceType);
	}

}

/**
 * 
 * @param resourceValue
 * @param resourceType
 * @param memberFieldType
 */
function clearObjects(resourceValue,resourceType,memberFieldType){

	if(memberFieldType == "membersFields"){
		var resourceStoreMember = getExtjsJsonObjByNameMembers(resourceType);
		resourceStoreMember.removeAll();
		var fieldResource = getExtjsJsonObjWithName(resourceType);
		fieldResource.removeAll();
		var dynamicTransactorObj = getExtjsJsonObjByNameOFdynamicTransactor(resourceType);
		dynamicTransactorObj.removeAll();
		reinitializedFlag = 1;
	}else if(memberFieldType == "operationPermission"){
		var operationFieldResource = getExtjsJsonObjByNameOFPermission(resourceType);
		operationFieldResource.removeAll();
	}else if(memberFieldType == "membersPermission"){
		var resourceStoreMember = getExtjsJsonObjByNameMembers(resourceType);
		resourceStoreMember.removeAll();
		if(resourceType != ORYX.I18N.PropertyWindow.admin){
			var dynamicTransactorObj = getExtjsJsonObjByNameOFdynamicTransactor(resourceType);
			dynamicTransactorObj.removeAll();
		}
	}else if(memberFieldType == "membersPermissionEnd"){
		var resourceStoreMember = getExtjsJsonObjByNameMembers(resourceType);
		resourceStoreMember.removeAll();
	}else if(memberFieldType == "membersPermissionprocess"){
		var resourceStoreMember = getExtjsJsonObjByNameMembers(resourceType);
		resourceStoreMember.removeAll();
	}else if(memberFieldType == "startScript"){
		var startTransactorObj = getExtjsObjStartEndScript(resourceType);
		startTransactorObj.removeAll();
	}else if(memberFieldType == "endScript"){
		var endTransactorObj = getExtjsObjStartEndScript(resourceType);
		endTransactorObj.removeAll();
	}else if(memberFieldType == "Notification"){
		var notificationObj = notificationStore;
		notificationObj.removeAll();
	}else if(memberFieldType == "ChooseCondition"){
		var conditionExpressionStoreObj = conditionExpressionStore;
		conditionExpressionStoreObj.removeAll();
	}else if(memberFieldType == "startPermission"){
		var fieldResource = getExtjsJsonObjWithName(resourceType);
		fieldResource.removeAll();
		var dynamicTransactorObj = getExtjsJsonObjByNameOFdynamicTransactor(resourceType);
		dynamicTransactorObj.removeAll();
	}else{
		var fieldResource = getExtjsJsonObjWithName(resourceType);
		fieldResource.removeAll();
	}
}

/**
 * 
 * @param scriptName
 * @param scriptFunction
 * @param resourceType
 */
function reInitializeStartEndEvent(scriptName,scriptFunction,resourceType){
	var operationStartEndScript = getExtjsObjStartEndScript(resourceType);
	var resourceRecord = operationStartEndScript.recordType;

	operationStartEndScript.removeAll();

	var aRecordVal = new resourceRecord({
		resource_type: scriptName,
		resource: scriptFunction,
		resourceassignmentexpr: resourceType+' Script Operation',
		language: '',
		evaluatestotype: '',
    });
    operationStartEndScript.add(aRecordVal);
}

/**
 * 
 * @param operationPermissions
 * @param resourceType
 */
function reInitializeOperationPermissionObj(operationPermissions,resourceType){
	var operationFieldResource = getExtjsJsonObjByNameOFPermission(resourceType);
	operationFieldResource.removeAll();
	var resourceRecord = operationFieldResource.recordType;
	for(i=0;i<operationPermissions.length;i++){
		var operationPermission = operationPermissions[i];
		var recordIndex = findARecordInStore(operationPermission.resource_type,operationPermission.resource,operationPermission.resourceassignmentexpr,operationFieldResource);
		if (recordIndex == -1) {
            var aRecordVal = new resourceRecord({
                resource_type: operationPermission.resource_type,
                resource: operationPermission.resource,
                resourceassignmentexpr: operationPermission.resourceassignmentexpr,
                language: '',
                evaluatestotype: '',
            });
            operationFieldResource.add(aRecordVal);
        }
	}
}

/**
 * 
 * @param members
 * @param resourceType
 */
function reInitializeMemberObj(members,resourceType){
	var resourceStoreMember = getExtjsJsonObjByNameMembers(resourceType);
	resourceStoreMember.removeAll();
	var resourceRecord = resourceStoreMember.recordType;
	
	var userList = "";
	
	if(member!=null || member!= ""){
		for(i=0;i<members.length;i++){
			var member = members[i];
			if(i==0){
				userList += member.resourceassignmentexpr;
			}else{
				userList += ","+member.resourceassignmentexpr;
			}
		}
		var params = "{strUserList:"+userList+"}";
		Ext.Ajax.request({
			waitMsg: 'Saving changes...',
			method: 'GET',
			headers: {'Content-Type': 'application/json'},
			url:'/process/userNameCheckFullName',
			params: {strUserList:userList},
			failure:function(response){
				//alert("failure");
				Ext.Msg.show({
					title:'Message',
					msg: ORYX.I18N.Feedback.failure,
					buttons: Ext.Msg.OK,
					animEl: 'elId',
					icon: Ext.MessageBox.ERROR
				});
			},
			success:function(response){
				var resUserMap = response.responseText;
				var jsonFieldData = {};
				jsonFieldData = JSON.parse(resUserMap.toString());
				var fieldParse = jsonFieldData.userFullName;
				for(i=0;i<members.length;i++){
					var member = members[i];
					var userExistFlag = userExist(member.resourceassignmentexpr,resUserMap);
					if(userExistFlag == true){
					for(var index=0;index<fieldParse.length;index++){
						if(fieldParse[index].id == member.resourceassignmentexpr){
						var recordIndex = findARecordInStore(member.resource_type,member.resource,member.resourceassignmentexpr,resourceStoreMember);
						if (recordIndex == -1) {
							var aRecordVal = new resourceRecord({
								resource_type: member.resource_type,
								resource: member.resource,
								resourceassignmentexpr: member.resourceassignmentexpr,
								language: '',
								evaluatestotype: '',
								roletype: member.roletype,
								superior: member.superior,
								subordinate: member.subordinate,
								fullName: fieldParse[index].fullName,
							});
							resourceStoreMember.add(aRecordVal);
						}
						}
					}
						
					} else if(member.resourceassignmentexpr == member.resource){
					var recordIndex = findARecordInStore(member.resource_type,member.resource,member.resourceassignmentexpr,resourceStoreMember);
						if (recordIndex == -1) {
							var aRecordVal = new resourceRecord({
								resource_type: member.resource_type,
								resource: member.resource,
								resourceassignmentexpr: member.resourceassignmentexpr,
								language: '',
								evaluatestotype: '',
								roletype: member.roletype,
								superior: member.superior,
								subordinate: member.subordinate,
								
							});
							resourceStoreMember.add(aRecordVal);
						}
					}
				}
				if(resourceType == ORYX.I18N.PropertyWindow.organizer){
					renderResourceOrderView(resourceType, '');
				}
				if(resourceType == ORYX.I18N.PropertyWindow.reader || resourceType == ORYX.I18N.PropertyWindow.admin || resourceType == ORYX.I18N.PropertyWindow.process){
					
					setTimeout(function(){showSelectedResources(resourceType)},500);
				}
			}
		});
	}
}

/**
 * 
 * @param dynamicTransactor
 * @param resourceType
 */
function reInitializeDynamicTransactor(dynamicTransactor,resourceType,paramFormId){
	var dynamicTransactorObj = getExtjsJsonObjByNameOFdynamicTransactor(resourceType);
	var dynamicTransactorValueList = new Array(); 
	var resourceRecord = dynamicTransactorObj.recordType;	
		Ext.Ajax.request({
			url: "/bpm/form/getAllFormFields",
			params: {formId :paramFormId},
			asynchronous: false,
			method: 'GET',
			failure:function(response,options){
				//alert("failure");
				Ext.Msg.show({
					title:'Message',
					msg: ORYX.I18N.Feedback.failure,
					buttons: Ext.Msg.OK,
					animEl: 'elId',
					icon: Ext.MessageBox.ERROR
				});
			},
			success:function(response,options){	
				var fieldData = response.responseText;
				var jsonFieldData = {};
				jsonFieldData = JSON.parse(fieldData.toString());
				var fieldParse = jsonFieldData.forms;
				dynamicTransactorObj.removeAll();
				for(var i= 0;i< fieldParse.length;i++){
					if(!fieldParse[i].id.endsWith("_FullName")) {
						var evaluatestotypeRecord = 0;
						if(dynamicTransactor == fieldParse[i].id.replace(/\s/g, "").toLowerCase()){
							evaluatestotypeRecord = 1;
						}
						var aRecordVal = new resourceRecord({
							resource_type: fieldParse[i].id.replace(/\s/g, "").toLowerCase(),
							resource: fieldParse[i].name,
							resourceassignmentexpr: "dynamicTransactor",
							language: fieldParse[i].type,
							evaluatestotype: evaluatestotypeRecord,
						});
						dynamicTransactorObj.add(aRecordVal);
					}
				}
				reinitializedFlag = 1;
			}
		});
}

/**
 * 
 * @param userName
 * @param resUserList
 * @returns {Boolean}
 */
function userExist(userName,resUserList){
	var findUserExist  = resUserList.indexOf(userName);
	if(findUserExist > -1){
		return true;
	}else{
		return false;
	}
}

/**
 * 
 * @param fieldPermissions
 * @param resourceType
 */
function reInitializePermissionObj(fieldPermissions,resourceType){
	var fieldResource = getExtjsJsonObjWithName(resourceType);
	fieldResource.removeAll();
	for (var key in fieldPermissions) {
		var resource_type = key;
		var keyValue = key.split("_");
		var resource= keyValue[keyValue.length-1];
		var resourceassignmentexpr= "fieldPermissions";
		var evaluatestotype = fieldPermissions[key];
		addFieldPermission(resource_type,resource,resourceassignmentexpr,evaluatestotype,fieldResource,'');
	}
	
}

/**
 * Toggle fullscreen function for process modeler
 * @param clickOn, get click event from image button or when save the process 
 */
function toggleFullscreenProcessEditor(clickOn){
	if(window.top.document.getElementById('header_menu').style.display == 'none'){
		window.top.document.getElementById("header_menu").style.display = "";
		window.top.document.getElementById("top_menu").style.display = "";
		window.top.document.getElementById("side_menu").style.display = ""
		window.top.document.getElementById("ajax_loader").style.width = "100%";
		window.top.document.getElementById("dashboard-wrapper").style.marginLeft = "190px";
		var iframeHeight = window.top.document.getElementById("target_iframe").style.height;
		var iframeHeightWithoutPx = iframeHeight.substring(0, iframeHeight.length-2);
		iframeHeightWithoutPx = parseInt(iframeHeightWithoutPx) - 100;
		window.top.document.getElementById("target_iframe").style.height = iframeHeightWithoutPx+"px";
		ORYX.Editor.resizeProcessEditor(iframeHeightWithoutPx - 45);
		
	}else if(window.top.document.getElementById('header_menu').style.display == "" || window.top.document.getElementById('header_menu').style.display == 'block'){
		if(clickOn == "top_btn"){
			window.top.document.getElementById("header_menu").style.display = 'none';
			window.top.document.getElementById("top_menu").style.display = 'none';
			window.top.document.getElementById("side_menu").style.display = 'none';
			window.top.document.getElementById("ajax_loader").style.width = "100%";
			window.top.document.getElementById("dashboard-wrapper").style.marginLeft = "0px";
			var iframeHeight = window.top.document.getElementById("target_iframe").style.height;
			var iframeHeightWithoutPx = iframeHeight.substring(0, iframeHeight.length-2);
			iframeHeightWithoutPx = parseInt(iframeHeightWithoutPx) + 100;
			window.top.document.getElementById("target_iframe").style.height = iframeHeightWithoutPx+"px";
			ORYX.Editor.resizeProcessEditor(iframeHeightWithoutPx - 45);
		}
	}	
}

var conditionResonseDataGlob = "";
var conditionFormIdGlob = "";

/**
 * 
 */
var conditionExpressionStore = new Ext.data.JsonStore({
	fields: [{
        name: 'resource_type',
        mapping: 'resource_type'
    }, {
        name: 'resource',
        mapping: 'resource'
    }, {
        name: 'resourceassignmentexpr',
        mapping: 'resourceassignmentexpr'
    }, {
        name: 'evaluatestotype',
        mapping: 'evaluatestotype'
    }
],
    root: 'items',
});

var conditionScriptStore = new Ext.data.JsonStore({
	fields: [{
        name: 'resource_type',
        mapping: 'resource_type'
    }, {
        name: 'resource',
        mapping: 'resource'
    }, {
        name: 'resourceassignmentexpr',
        mapping: 'resourceassignmentexpr'
    }, {
        name: 'evaluatestotype',
        mapping: 'evaluatestotype'
    }
],
    root: 'items',
});

/**
 * 
 * @param currentOption
 * @returns {String}
 */
function generateOptionHtml(currentOption){
	var operationArray = [">","<","==","!=",">=","<=","contains"];
	var htmlContent = "<select id='condition_Relational'>";// onChange='setRelationalValue()'
	for(var i=0; i<operationArray.length; i++){
		var operationValue = operationArray[i];
		if(operationValue === "contains") {
			htmlContent += "<option value='`' id=condition_Operation_ext_get_"+i+">"+operationArray[i]+"</option>";      
		} else {
			htmlContent += "<option value='"+operationArray[i]+"' id=condition_Operation_ext_get_"+i+">"+operationArray[i]+"</option>";      
		}
	}
	htmlContent += "</select>";
	return htmlContent;
}

/**
 * 
 * @param currentOption
 * @returns {String}
 */
function generateOperationHtml(currentOption){
	var operationArray = ["&&","||","Null"];
	var htmlContent = "<select id='condition_Operation'>";//onChange='setOperationValue()'
	for(var i=0; i<operationArray.length; i++){
		var changeDisplay = "";
		if(operationArray[i] == '&&'){
			changeDisplay = "AND"
		}
		if(operationArray[i] == '||'){
			changeDisplay = "OR"
		}
		if(operationArray[i] == 'Null'){
			changeDisplay = "NULL"
		}
		if(currentOption == operationArray[i]){
			htmlContent += "<option value='"+operationArray[i]+"' selected='selected'>"+changeDisplay+"</option>";
		}else{
			htmlContent += "<option value='"+operationArray[i]+"' id=condition_Operation_"+changeDisplay+">"+changeDisplay+"</option>";
		}
		
	}
	htmlContent += "</select>";

	return htmlContent;
}

/**
 * this function to add the field form name
 * @param fieldparse
 * @retrun{String}
 */
function generateFormFieldsParameters(fieldParse){
	var htmlContent = "<select id='condition_Formfieldname'>";// onChange='setFormfieldParameterValue()'
	for(var i=0; i<fieldParse.length;i++){
		var fieldParseId = fieldParse[i].id.toString();
		if(fieldParseId.indexOf('_FullName') == -1) {
		htmlContent += "<option value='"+fieldParse[i].chineseName+"' id=condition_Formfieldname_"+fieldParse[i].chineseName+">"+fieldParse[i].chineseName+"</option>";
		}
		
	}
	htmlContent += "</select>";
	return htmlContent;
	
}
/**
 * this function add condition value to table
 * @param formObject,true or false
 * @return{String}
 */
function conditionExpressionAddRow(formObject,reInitalizeCondition){

	if(reInitalizeCondition === 'True'){
		var table = document.getElementById('condition_Expression');
		var rowCount = table.rows.length;
		//if(rowCount-1 < conditionResonseDataGlob.length){
		if(formObject.formValue != "" ){
			var row = table.insertRow(rowCount);
			var parameterValue = row.insertCell(0);
			var relationValue = row.insertCell(1);
			var formValue = row.insertCell(2);
			var joinValue = row.insertCell(3);
			var operation = row.insertCell(4);
			parameterValue.innerHTML = formObject.parameterName;
			if(formObject.relationValue.toString() == "`"){
				relationValue.innerHTML = "contains";
			} else {
				relationValue.innerHTML = formObject.relationValue;
			}
			
			joinValue.innerHTML = formObject.joinValue;
			formValue.innerHTML = formObject.formValue;
			operation.innerHTML = '<input type="button" id="delete_Condition" value="Delete" onClick="deleteSelectedCondition(this)"/>';
		}
		//}else{
		//	alert("Can't add new condition more than form field");
		//}
				
	
		
	}else{
		var table = document.getElementById('condition_Expression');
		var rowCount = table.rows.length;
		//if(rowCount-1 < conditionResonseDataGlob.length){
			if(document.getElementById("condition_Value").value != "" ){
				var row = table.insertRow(rowCount);
				var parameterValue = row.insertCell(0);
				var relationValue = row.insertCell(1);
				var formValue = row.insertCell(2);
				var joinValue = row.insertCell(3);
				var operation = row.insertCell(4);
				parameterValue.innerHTML = document.getElementById("condition_Formfieldname").value;
				if(document.getElementById("condition_Relational").value == '`'){
					relationValue.innerHTML = "contains";
				} else {
					relationValue.innerHTML = document.getElementById("condition_Relational").value;
				}
				joinValue.innerHTML = document.getElementById("condition_Operation").value;
				formValue.innerHTML = document.getElementById("condition_Value").value;
				operation.innerHTML = '<input type="button" id="delete_Condition" value="Delete" onClick="deleteSelectedCondition(this)"/>';
				document.getElementById("condition_Value").value = "";
			}else{
				//alert("Add Condition Value first.Otherwise can't add condition");
				Ext.Msg.show({
					title:'Message',
					msg: ORYX.I18N.PropertyWindow.conditionError,
					buttons: Ext.Msg.OK,
					animEl: 'elId',
					icon: Ext.MessageBox.ERROR
				});
			}
		//}else{
		//	alert("Can't add new condition more than form field");
		//}
	}
}
/**
 * this function to delete the row in the table
 *
 * @param rowindex
 */
function deleteSelectedCondition(row)
{
    var rowIndex=row.parentNode.parentNode.rowIndex;
    document.getElementById('condition_Expression').deleteRow(rowIndex);
}

/**
 *
 * @param resourceValue
 * @param formValue
 * @return {boolean}
 */
function checkConditionPreviousFormId(resourceValue,formValue){
	if(resourceValue != ""){
		var fieldData = eval('('+resourceValue+')');
		var resourceType = "";
		var previousFormId= fieldData.previousFormId;
		if(previousFormId == formValue){
			return true;
		} else {
			return false;
		}
	}else{
		return false;
	}
}

/**
 * function string prototype contains not supported by chrome
 * @return {boolean}
 */
String.prototype.contains = function(s) {
	return this.indexOf(s) > -1
}

/**
 * 
 * @param formValue
 * @param resourceValue
 */
function conditionExpression(formValue , resourceValue){
	Ext.Ajax.request({
		url: "/bpm/form/getAllFormFields",
		params: {formId :formValue},
		asynchronous: false,
		method: 'GET',
		failure:function(response,options){
			
		},
		success:function(response,options){
			var fieldData = response.responseText;
			var jsonFieldData = {};
			jsonFieldData = JSON.parse(fieldData.toString());
			var fieldParse = jsonFieldData.forms;
			conditionResonseDataGlob = fieldParse;
			var htmlContent = '<table width="100%">';
			var operantContent = [];
			var conditionExpressoin = "[{";
			var conditionExpressionJSON = {};
			var checkFormId = checkConditionPreviousFormId(resourceValue,formValue)
			
			if(conditionExpressionStore.getCount()>0 && checkFormId == true){
				
				var aRecord = conditionExpressionStore.getAt(0);
				var splitExpressions = aRecord.get("resource").split("&&");
				{
					var length = splitExpressions.length;
					var i;
					for(i=0;i<length-1;i++){
						splitExpressions[i]=splitExpressions[i]+"&&";
					}
				}
				var expressionArray = [];
				for(var j=0;j<splitExpressions.length;j++){
					var splitExpression = splitExpressions[j];
					if(splitExpression.contains("||")){
					splitExpressionOR = splitExpression.split("||");
						{
							var length = splitExpressionOR.length;
							var i;
							for(i=0;i<length-1;i++){
								splitExpressionOR[i]=splitExpressionOR[i]+"||";
							}
							
						}
						for(var i=0;i<splitExpressionOR.length;i++){
							expressionArray.push(splitExpressionOR[i]);
						}
					}else if(splitExpression.contains("Null")){
					splitExpressionOR = splitExpression.split("Null");
						{
							var length = splitExpressionOR.length;
							var i;
							for(i=0;i<length-1;i++){
								splitExpressionOR[i]=splitExpressionOR[i]+"NULL";
							}
							
						}
						for(var i=0;i<splitExpressionOR.length;i++){
							expressionArray.push(splitExpressionOR[i]);
						}
					}else{
						expressionArray.push(""+splitExpression+"");
					}
				}
				
				var fieldLength = fieldParse.length-1;
				for(var i= 0;i< fieldParse.length;i++){
				var regMatch = ">";
				var operantExp = '&&';
				var operantContent;
				var expressName;
				if(fieldLength >= i){
					for(var j=0;j<expressionArray.length;j++){
						var regMatchExpression = new RegExp("/==|==|!=|>=|<=|`|>|<|</");
						regMatch = expressionArray[j].match(regMatchExpression);
						var expressionCondentsData = expressionArray[j].split(/==|!=|>=|<=|>|<|`/);   // here contains symbol is represented as ^.
						if(fieldParse[i].name == expressionCondentsData[0]){
							if(expressionCondentsData[1].match('&&')){
								operantExp = '&&';
								operantContent = expressionCondentsData[1].split('&&');
						
							}else if(expressionCondentsData[1].match('||')){
								operantExp = '||';
								operantContent = expressionCondentsData[1].split('||');
							}else{
								operantExp = "NULL";
								operantContent = expressionCondentsData[1].split('NULL');
							}
						
						
							expressionCondents = operantContent[0];//.split(/==|!=|>=|<=|>|</);
							var Re = new RegExp("\\'","g");
							expressName = expressionCondents.replace(Re,"");
							//if(fieldLength != i){
			conditionExpressoin = conditionExpressoin + '"parameterName":"'+fieldParse[i].chineseName+'","relationValue":"'+regMatch+'","joinValue":"'+
operantExp+'","formValue":'+expressName+'},{';			
			
			//}else{
			//conditionExpressoin = conditionExpressoin +'"parameterName":"'+fieldParse[i].chineseName+'","relationValue":"'+regMatch+'","joinValue":"'+
//operantExp+'","formValue":"'+expressName+'"}';
			//}
						
						/*}else{
							regMatch = ">";
							operantExp = "&&";
							expressName = "";*/
						}
					
						
					}
					
				}										
				
			
			
			}
			var lastTwoString = conditionExpressoin;
			if(lastTwoString.slice(-2) == ',{'){
			conditionExpressoin = conditionExpressoin.substring(0, conditionExpressoin.length - 2);
			}
			conditionExpressoin += "]";
			conditionExpressionJSON = JSON.parse(conditionExpressoin.toString());	
			htmlContent = htmlContent + '<tr><td>'+ORYX.I18N.PropertyWindow.join1+'</td><td>'+generateOperationHtml('none')+'</td><td>'+ORYX.I18N.PropertyWindow.parameter+'</td><td>'+generateFormFieldsParameters(fieldParse,'')+'</td><td>'+generateOptionHtml('none')+'</td><td><input type="text" style="width:100px" id="condition_Value" name="condition_Value"/></td><td><button id="conditionAddValue" name="conditionAddValue" onClick="conditionExpressionAddRow(\'\',\'\')">'+ORYX.I18N.PropertyWindow.add+'</button></td></tr>';
			conditionExpressionJSON = removeDuplicateCondition(conditionExpressionJSON);
			for(var i=0;i<conditionExpressionJSON.length;i++){
				conditionExpressionAddRow(conditionExpressionJSON[i],'True');
			}
			}else{
				conditionExpressionStore.removeAll();
				conditionFormIdGlob = formValue;
				//var fieldLength = fieldParse.length-1;
				//for(var i= 0;i< fieldParse.length;i++){
				var table = document.getElementById("condition_Expression");
				for(var i = table.rows.length - 1; i > 0; i--)
				{
    				table.deleteRow(i);
				}
								
						htmlContent = htmlContent + '<tr><td>'+ORYX.I18N.PropertyWindow.join1+'</td><td>'+generateOperationHtml('none')+'</td><td>'+ORYX.I18N.PropertyWindow.parameter+'</td><td>'+generateFormFieldsParameters(fieldParse,'')+'</td><td>'+generateOptionHtml('none')+'</td><td><input type="text" style="width:100px" id="condition_Value" name="condition_Value"/></td><td><button id="conditionAddValue" name="conditionAddValue" onClick="conditionExpressionAddRow(\'\',\'\')">'+ORYX.I18N.PropertyWindow.add+'</button></td></tr>';
			}
			
			htmlContent  = htmlContent + '</table>';

			document.getElementById('chooseContentDiv').innerHTML = htmlContent;
		}
	});
}

/**
 * remove duplicate entry for condition
 *
 * @param conditionExpressionJSON
 * @return object
 */
function removeDuplicateCondition(conditionExpressionJSON){
	if(conditionExpressionJSON.length > 1){
		for(var i=1;i<conditionExpressionJSON.length;i++){
			if(conditionExpressionJSON[i].parameterName == conditionExpressionJSON[i-1].parameterName && conditionExpressionJSON[i].relationValue  == conditionExpressionJSON[i-1].relationValue && conditionExpressionJSON[i].formValue == conditionExpressionJSON[i-1].formValue){
			delete conditionExpressionJSON[i];
			}
		}
	}
	return conditionExpressionJSON;
}

/**
 * generate the json in given condition
 *
 * @param tableId
 * @param conditionResonseDataGlob
 * @return {String}
 */
function conitionExpressionGetData(conditionResonseDataGlob,tableId){
	try {
		 var table = document.getElementById(tableId);
         var rowCount = table.rows.length;
    	 var conitionExpressionData = new Array();
	 var conitionExpressionJsonString = "";
	 var parameterValue = "";
	 for(var index=1; index < rowCount; index++) {
         var mapObj = {};
         var row = table.rows[index];
         var reference_parameterValue = row.cells[0].innerHTML;
         var reference_relationValue = row.cells[1].innerHTML;
         var reference_formValue = row.cells[2].innerHTML;
         var reference_joinValue = row.cells[3].innerHTML;
         var parameterValue = "";
         for(var i=0;i<conditionResonseDataGlob.length;i++){
         	if(conditionResonseDataGlob[i].chineseName == reference_parameterValue){
            		parameterValue = conditionResonseDataGlob[i].name;
            	}
            }
            mapObj['parameterValue'] = parameterValue;
            if(reference_relationValue == "contains"){
            	mapObj['relationValue'] = "`";
            } else {
            	mapObj['relationValue'] = reference_relationValue;
            }
            
	    mapObj['formValue'] = reference_formValue;
	    mapObj['joinValue'] = reference_joinValue;
            conitionExpressionData.push(mapObj);
	    conitionExpressionJsonString += conitionExpressionGenerateJson(mapObj,index);
        }
    }catch(e) {
       // alert(e);
    }
	return conitionExpressionJsonString;
}

/**
 *
 * @param mapObj
 * @param index
 * @retrun {String}
 */
function conitionExpressionGenerateJson(mapObj,index){
	var conitionExpressionJson = "";
	if(index == 0 ){
		if(mapObj['joinValue'] == 'Null'){
			conitionExpressionJson = mapObj['parameterValue']+mapObj['relationValue']+"\""+mapObj['formValue']+"\"";
		}else{
			conitionExpressionJson = mapObj['parameterValue']+mapObj['relationValue']+"\""+mapObj['formValue']+"\""+mapObj['joinValue'];
		}
	}else{
	if(mapObj['joinValue'] == 'Null'){
			conitionExpressionJson = mapObj['parameterValue']+mapObj['relationValue']+"\""+mapObj['formValue']+"\"";
		}else{
			conitionExpressionJson = mapObj['parameterValue']+mapObj['relationValue']+"\""+mapObj['formValue']+"\""+mapObj['joinValue'];
		}
		//conitionExpressionJson = mapObj['parameterValue']+mapObj['relationValue']+"'"+mapObj['formValue']+"'"+mapObj['joinValue'];
	}

	return conitionExpressionJson;
}

/**
 * 
 * @param resourceValue
 * @param recordType
 */
function reInitializeChooseConditionObject(resourceValue,recordType){
	var resourceRecord = conditionExpressionStore.recordType;
	var fieldData = eval('('+resourceValue+')');
	var resourceType = "";
	var expressionType= fieldData.type;
	var expressionContent = unescape(fieldData.value[0].val);
	
	if(expressionType == 'expression'){
		resourceType == 'Expression';
		conditionExpressionStore.removeAll();
		var aRecordVal = new resourceRecord({
			resource_type: resourceType,
			resource: expressionContent,
			resourceassignmentexpr: 'Condition Expression',
			language: '',
			evaluatestotype: '',
		});
		conditionExpressionStore.add(aRecordVal);
	}else{
		resourceType == 'Script';
		conditionScriptStore.removeAll();
		var aRecordVal = new resourceRecord({
			resource_type: resourceType,
			resource: expressionContent,
			resourceassignmentexpr: 'Script',
			language: '',
			evaluatestotype: '',
		});
		conditionScriptStore.add(aRecordVal);
	}
	
	
	
}
function htmlEntities(str) {
    return String(str).replace(/&amp;&amp;/g,'&&').replace(/&lt;/g,'<').replace(/&gt;/g,'>').replace(/==/g,'==').replace(/!=/g,'!=').replace(/&gt;=/g,'>=').replace(/&lt;=/g,'<=')}

/**
 * 
 * @returns {String}
 */
function generateConitionExpressionJson(paramFormId){
	var conditionHtmlExpressionContent = conitionExpressionGetData(conditionResonseDataGlob,'condition_Expression');
	var chooseType = document.getElementById("expScriptExpression").checked;
	var consJsonContent = "";
	if(chooseType){
	var resourceRecord = conditionExpressionStore.recordType;
		consJsonContent = "{'type':'expression','value':[{'val':'";
		conditionExpressionStore.removeAll();
		var conditionExpressionContent =htmlEntities(conditionHtmlExpressionContent);
		var lastTwoString = conditionExpressionContent;
		var aRecordVal = new resourceRecord({
			resource_type: "Expression",
			resource: conditionHtmlExpressionContent,
			resourceassignmentexpr: 'Condition Expression',
			language: '',
			evaluatestotype: '',
		});
		conditionExpressionStore.add(aRecordVal);
		if(lastTwoString.slice(-2) == '&&' || lastTwoString.slice(-2) == '||'){
			conditionExpressionContent = conditionExpressionContent.substring(0, conditionExpressionContent.length - 2);
		}
	consJsonContent = consJsonContent +conditionExpressionContent+ "'}],'previousFormId' : '"+paramFormId+"'}";
	}else{
		var resourceRecord = conditionScriptStore.recordType;
		conditionScriptStore.removeAll();
		var aRecordVal = new resourceRecord({
			resource_type: "Script",
			resource: document.getElementById("sequencescriptfunction").value,
			resourceassignmentexpr: 'Condition Script',
			language: '',
			evaluatestotype: '',
		});
		conditionScriptStore.add(aRecordVal);

		consJsonContent = "{'type':'javascript','value':[{'val':'";
		consJsonContent += escape(document.getElementById("sequencescriptfunction").value);
		consJsonContent = consJsonContent + "'}]}";
	}
	return consJsonContent;
		
/*	var chooseType = document.getElementById("expScriptExpression").checked;
	var consJsonContent = "";
	if(chooseType){
		var fieldLength = conditionResonseDataGlob.length-1;
		consJsonContent = "{'type':'expression','value':[{'val':'";

		var resourceRecord = conditionExpressionStore.recordType;

		var expressionContent = "";
		for(var i= 0;i< conditionResonseDataGlob.length;i++){
			if(document.getElementById('text_'+conditionResonseDataGlob[i].id)){
				if(document.getElementById('text_'+conditionResonseDataGlob[i].id).value !== ""){
					if(fieldLength > i){
						expressionContent = expressionContent + conditionResonseDataGlob[i].name + document.getElementById('select_'+conditionResonseDataGlob[i].id).value+"'"+document.getElementById('text_'+conditionResonseDataGlob[i].id).value + "'" + document.getElementById('operation_'+conditionResonseDataGlob[i].id).value;
					}else{
						expressionContent = expressionContent + conditionResonseDataGlob[i].name + document.getElementById('select_'+conditionResonseDataGlob[i].id).value+"'"+document.getElementById('text_'+conditionResonseDataGlob[i].id).value + "'";
					}
				}
			}
		}
		var lastTwoString = expressionContent;
		if(lastTwoString.slice(-2) == "&&" || lastTwoString.slice(-2) == '||'){
			expressionContent = expressionContent.substring(0, expressionContent.length - 2);
		}

		conditionExpressionStore.removeAll();
		var aRecordVal = new resourceRecord({
			resource_type: "Expression",
			resource: expressionContent,
			resourceassignmentexpr: 'Condition Expression',
			language: '',
			evaluatestotype: '',
		});
		conditionExpressionStore.add(aRecordVal);

		consJsonContent = consJsonContent + escape(expressionContent) + "'}]}";
	}else{
		var resourceRecord = conditionScriptStore.recordType;
		conditionScriptStore.removeAll();
		var aRecordVal = new resourceRecord({
			resource_type: "Script",
			resource: document.getElementById("sequencescriptfunction").value,
			resourceassignmentexpr: 'Condition Script',
			language: '',
			evaluatestotype: '',
		});
		conditionScriptStore.add(aRecordVal);

		consJsonContent = "{'type':'javascript','value':[{'val':'";
		consJsonContent += escape(document.getElementById("sequencescriptfunction").value);
		consJsonContent = consJsonContent + "'}]}";
	}
	return consJsonContent;*/
}

/**
 * conditionExpressionStore
 * @param conditionType
 */
function changeSequenceFlow(conditionType){
	if(conditionType == "expression"){
		document.getElementById("condition_expression_panel").style.display="block";
		document.getElementById("condition_script_panel").style.display="none";
	}else{
		document.getElementById("condition_script_panel").style.display="block";
		document.getElementById("condition_expression_panel").style.display="none";

		if(conditionScriptStore.getCount()>0){
			var aRecord = conditionScriptStore.getAt(0);
			document.getElementById("sequencescriptfunction").value = aRecord.get("resource");
		}
	}
}

/**
 * 
 */
var notificationStore = new Ext.data.JsonStore({
    fields: [{
            name: 'notification_message',
            mapping: 'notification_message'
        }, {
            name: 'notification_type',
            mapping: 'notification_type'
        }, {
            name: 'notification_hour',
            mapping: 'notification_hour'
        }, {
            name: 'notification_minute',
            mapping: 'notification_minute'
        }, {
            name: 'evaluatestotype',
            mapping: 'evaluatestotype'
        }
    ],
    root: 'items',
});

/**
 * 
 * @param resourceType
 * @param resourceValue
 */
function setNotificationContent(resourceType,resourceValue){
	var notificationHtmlContainer = "<div align='center'><table>";
	if(notificationStore.getCount() > 0){
		var aRecord = notificationStore.getAt(0);
		notificationHtmlContainer += '<tr><td>Message </td><td><input type="text" id="notification_message" name="notification_message" value="'+aRecord.get("notification_message")+'"/></td></tr>';
		notificationHtmlContainer += '<tr><td>Type </td><td><select id="notification_type" name="notification_type"><option value="smsNotification" '+checkNotificationSelection(aRecord.get("notification_type"),"smsNotification")+'>SMS Notification</option><option value="emailNotification" '+checkNotificationSelection(aRecord.get("notification_type"),"emailNotification")+'>Email Notification</option></select></td></tr>';
		notificationHtmlContainer += '<tr><td>Hour </td><td><input type="text" id="notification_hour" name="notification_hour" value="'+aRecord.get("notification_hour")+'"/></td></tr>';
		notificationHtmlContainer += '<tr><td>Minute </td><td><input type="text" id="notification_minute" name="notification_minute" value="'+aRecord.get("notification_minute")+'"/></td></tr>';	
	}else{
		notificationHtmlContainer += '<tr><td>Message </td><td><input type="text" id="notification_message" name="notification_message" /></td></tr>';
		notificationHtmlContainer += '<tr><td>Type </td><td><select id="notification_type" name="notification_type"><option value="smsNotification">SMS Notification</option><option value="emailNotification" selected="selected">Email Notification</option></select></td></tr>';
		notificationHtmlContainer += '<tr><td>Hour </td><td><input type="text" id="notification_hour" name="notification_hour" /></td></tr>';
		notificationHtmlContainer += '<tr><td>Minute </td><td><input type="text" id="notification_minute" name="notification_minute" /></td></tr>';	
	}
	notificationHtmlContainer  +=  '</tr></table></div>';
	document.getElementById('notificationDiv').innerHTML = notificationHtmlContainer;
}

/**
 * 
 * @param notificationTypeValue
 * @param currentType
 * @returns {String}
 */
function checkNotificationSelection(notificationTypeValue,currentType){
	if(notificationTypeValue == "smsNotification" && currentType == "smsNotification"){
		return 'selected="selected"';
	}else if (notificationTypeValue == "emailNotification" && currentType == "emailNotification"){
		return 'selected="selected"';
	}else{
		return '';
	}
}

/**
 * 
 * @param resourceType
 * @returns {String}
 */
function generateNotificationJSON(resourceType){
	var notificationMessage = document.getElementById("notification_message").value;	
	var notificationType = document.getElementById("notification_type").value;	
	var notificationHour = document.getElementById("notification_hour").value;	
	var notificationMinute = document.getElementById("notification_minute").value;
	var resourceRecord = notificationStore.recordType;
	notificationStore.removeAll();
	var aRecordVal = new resourceRecord({
        notification_message: notificationMessage,
        notification_type: notificationType,
        notification_hour: notificationHour,
        notification_minute: notificationMinute,
        evaluatestotype: '',
    });
    notificationStore.add(aRecordVal);
	var jsonString = "[{";
	jsonString += "'automaticReminderMessage'" + ":" +"'"+notificationMessage+"',";
	jsonString += "'automaticReminderType'" + ":" +"'"+notificationType+"',";
	jsonString += "'automaticReminderHour'" + ":" +"'"+notificationHour+"',";
	jsonString += "'automaticReminderMinute'" + ":" +"'"+notificationMinute+"'";
	jsonString += "}]";
	return jsonString;
}

/**
 * 
 * @param notificationJson
 * @param notificationStore
 */
function reInitializeNotificationObj(notificationJson,notificationStore){
	var resourceRecord = notificationStore.recordType;
	notificationStore.removeAll();
	var notificationMessage = notificationJson[0].automaticReminderMessage; 
	var notificationType = notificationJson[0].automaticReminderType;
	var notificationHour = notificationJson[0].automaticReminderHour;
	var notificationMinue = notificationJson[0].automaticReminderMinute;

	var aRecordVal = new resourceRecord({
        notification_message: notificationMessage,
        notification_type: notificationType,
        notification_hour: notificationHour,
        notification_minute: notificationMinue,
        evaluatestotype: '',
    });
    notificationStore.add(aRecordVal);
}

/**
 * generate JSON for Transactor relationship
 */
function generateTransactorRelationshipJson(){
	return transactorRelationshipGetData('transactorRelationshipDataTable');
}


var transactorRelationshipFlag = 0;

/**
 * 
 * @param resourceValue
 * @param resourceType
 */
function setTransactorRelationship(resourceValue,resourceType){
	var table = document.getElementById('transactorRelationshipTable');
	var rowCount = table.rows.length;
	if(resourceValue == ""){
			transactorRelationshipAddRow('transactorRelationshipTable',"");
	}else{
		transactorRelationshipAddRow('transactorRelationshipTable',"");
		reInitializeObjectObj(resourceValue,resourceType,"TransactorRelationship","");
	}
}

/**
 * 
 * @param tableID
 * @param transactorRelationshipMap
 */
function transactorRelationshipAddRow(tableID,transactorRelationshipMap) {
	var table = document.getElementById(tableID);
	var rowHeader = table.insertRow(0);
	rowHeader.innerHTML = '<td style="padding: 3px; width: 120px;" id="ext-gen578"><span style="font-weight:bold;font-size:14px;" id="ext-gen583">'+ORYX.I18N.PropertyWindow.operator+'</span></td><td style="padding: 3px; width: 195px;" id="ext-gen579"><span style="font-weight:bold;font-size:14px;" id="ext-gen580">'+ORYX.I18N.PropertyWindow.relation+'</span></td><td style="padding: 3px; width: 80px;" id="ext-gen614"><span style="font-weight:bold;font-size:14px" id="ext-gen613">'+ORYX.I18N.PropertyWindow.join1+'</span></td><td style="padding: 3px; width: 80px;" id="ext-gen616"><span style="font-weight:bold;font-size:14px;" id="ext-gen615">&nbsp;'+ORYX.I18N.PropertyWindow.add+'</span></td>'; 
	
	var rowCount = table.rows.length;	
	var row = table.insertRow(rowCount);

	var cell1 = row.insertCell(0);
	var element1 = document.createElement("select");
	cell1.style.padding="3px";
	cell1.style.width="100px";
	//element1.style.width = "200px";
	element1.style.marginBottom = "0px";
	element1.id="reference_operator";
	element1.setAttribute("onChange", "setReferenceRelation("+transactorRelationshipFlag+")");
	var element1_relation =["Creator","Organizer","Null"];

	for (var i=0;i<element1_relation.length;i++){
		var option;
		option = document.createElement("option");
		option.setAttribute("value", element1_relation[i]);
		if(transactorRelationshipMap != ""){
			if(element1_relation[i] == transactorRelationshipMap["user"]){
				option.setAttribute("selected","selected");
			}
		}
		option.innerHTML = element1_relation[i];
		element1.appendChild(option);
	}
	cell1.appendChild(element1);

	var cell2 = row.insertCell(1);
	var element2 = document.createElement("select");
	cell2.style.padding="3px";
	cell2.style.width="170px";
	element2.style.width = "150px";
	element2.style.marginBottom = "0px";
	element2.id="reference_relation";
	var element2_relation =["History organizers of target node","Creator","People of the same department","Direct superior","All superior","Direct subordinate","All subordinate","Leaders in charge","Leader to secretary","Secretary to leader","Parent department person including sub-deparment","Parent department person excluding sub-department","Department interface people"];
	
	for (var j=0;j<element2_relation.length;j++){
		var option;
		option = document.createElement("option");
		if(j < 2){
			option.setAttribute("disabled", "");
		}
		option.setAttribute("value", j+1);
		if(transactorRelationshipMap != ""){
			var relationshipValue = j+1;
			if(relationshipValue == transactorRelationshipMap["relationship"]){
				option.setAttribute("selected","selected");
			}
		}
		option.innerHTML = element2_relation[j];
		element2.appendChild(option);
	}
	cell2.appendChild(element2);

	var cell3 = row.insertCell(2);
	cell3.style.width="100px";
	var element3 = document.createElement("select");
	cell3.style.padding="3px";
	//element3.style.width = "200px";
	element3.style.marginBottom = "0px";
	element3.id="reference_join";
	var fieldTypes=["OR","AND"];

	for (var k=0;k<fieldTypes.length;k++){
		var option;
		option = document.createElement("option");
		option.setAttribute("value", fieldTypes[k]);
		if(transactorRelationshipMap != ""){
			if(fieldTypes[k] == transactorRelationshipMap["operation"]){
				option.setAttribute("selected","selected");
			}
		}
		option.innerHTML = fieldTypes[k];
		element3.appendChild(option);
	}
	cell3.appendChild(element3);

	var cell4 = row.insertCell(3);
	cell4.style.width="100px";
	var element4 = document.createElement("BUTTON");
	cell4.style.padding="3px";
	element4.appendChild(document.createTextNode("Add"));
	element4.setAttribute("onClick", "transactorRelationshipTableAddRow('transactorRelationshipDataTable','')");
	cell4.appendChild(element4);
		
}

/**
 * 
 * @param tableId
 * @param transactorRelationshipMap
 */
function transactorRelationshipTableAddRow(tableID,transactorRelationshipMap) {
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);

	var cell1 = row.insertCell(0);
	var element1 = document.createElement("span");
	cell1.style.padding="3px";
	cell1.style.width="95px";
	//element1.style.width = "200px";
	element1.style.marginBottom = "0px";
	element1.id="reference_operator_"+transactorRelationshipFlag;
	var element1_value = "";
	if( transactorRelationshipMap != ""){
		element1_value = transactorRelationshipMap["user"];
	}else{
		element1_value = document.getElementById("reference_operator").value;
	}
	element1.appendChild(document.createTextNode(element1_value));
	cell1.appendChild(element1);

	var cell2 = row.insertCell(1);
	var element2 = document.createElement("span");
	cell2.style.padding="3px";
	cell2.style.width="195px";
	element2.style.width = "150px";
	element2.style.display = "none";
	element2.style.marginBottom = "0px";
	element2.id="reference_relation_"+transactorRelationshipFlag;
	var element2_value = "";
	if( transactorRelationshipMap != ""){
		element2_value = transactorRelationshipMap["relationship"];
	}else{
		element2_value = document.getElementById("reference_relation").value;
	} 
	element2.appendChild(document.createTextNode(element2_value));
	cell2.appendChild(element2);
	var element2ReferenceValue = document.createElement("span");
	element2ReferenceValue.style.width = "150px";
	element2ReferenceValue.id="reference_relation_element2ReferenceValue";
	var element2Reference_value = "";
	var element2_relation =["History organizers of target node","Creator","People of the same department","Direct superior","All superior","Direct subordinate","All subordinate","Leaders in charge","Leader to secretary","Secretary to leader","Parent department person including sub-deparment","Parent department person excluding sub-department","Department interface people"];
	if( transactorRelationshipMap != ""){
		element2Reference_value =element2_relation[transactorRelationshipMap["relationship"]-1];
	}else{
		var element2_ReferenceIndex = document.getElementById("reference_relation").value;
		
		element2Reference_value  = element2_relation[element2_ReferenceIndex-1];
	} 
	 element2ReferenceValue.appendChild(document.createTextNode(element2Reference_value));
	cell2.appendChild(element2ReferenceValue);
		

	var cell3 = row.insertCell(2);
	var element3 = document.createElement("span");
	cell3.style.padding="3px";
	cell3.style.width="50px";
	//element3.style.width = "200px";
	element3.style.marginBottom = "0px";
	element3.id="reference_join_"+transactorRelationshipFlag;
	var element3_value = "";
	if( transactorRelationshipMap != ""){
		element3_value = transactorRelationshipMap["operation"];
	}else{
		element3_value = document.getElementById("reference_join").value;
	} 
	element3.appendChild(document.createTextNode(element3_value));				
	cell3.appendChild(element3);


	var cell4 = row.insertCell(3);
	var element4 = document.createElement("BUTTON");
	cell4.style.padding="3px";
	cell4.style.textAlign = "center";
	element4.appendChild(document.createTextNode("Delete"));
	element4.setAttribute("onClick", "transactorRelationshipDeleteRow(this)");
	cell4.appendChild(element4);
	
	transactorRelationshipFlag += transactorRelationshipFlag + 1;
}

/**
 * 
 * @param relationIndexId
 */
function setReferenceRelation(relationIndexId){
	if(document.getElementById("reference_operator").value == "Null"){
		for(i=0;i< 13;i++){
			if(i > 1){
				document.getElementById("reference_relation").options[i].disabled=true;
			}else{
				document.getElementById("reference_relation").options[i].disabled=false;
			}
			
		}
		document.getElementById("reference_relation").selectedIndex=0;
	}else{
		for(i=0;i< 13;i++){
			if(i < 2){
				document.getElementById("reference_relation").options[i].disabled=true;
			}else{
				document.getElementById("reference_relation").options[i].disabled=false;
			}
			
		}
		document.getElementById("reference_relation").selectedIndex=2;
	}
	
	
}

/**
 * 
 * @param tabObj
 */
function transactorRelationshipDeleteRow(tabObj) {
	var rowsIndex=tabObj.parentElement.parentElement.rowIndex;
	var table = document.getElementById('transactorRelationshipDataTable');
   try {
    	
    		table.deleteRow(rowsIndex);	
    	
    }catch(e) {
        alert(e);
    }
}

/**
 * 
 * @param tableID
 * @returns {String}
 */
function transactorRelationshipGetData(tableID){
	try {
		 var table = document.getElementById(tableID);
         var rowCount = table.rows.length;
    	 var transactorData = new Array();
		 var transactorRelationshipJsonString = "[";
		 for(var index=0; index < rowCount; index++) {
        	var mapObj = {};
        	var row = table.rows[index];
            var reference_operator = row.cells[0].childNodes[0];
            var reference_relation = row.cells[1].childNodes[0];
            var reference_join = row.cells[2].childNodes[0];
                      
            mapObj['referenceOperator'] = reference_operator.innerHTML;
            mapObj['referenceRelation'] = reference_relation.innerHTML;
	    mapObj['referenceJoin'] = reference_join.innerHTML;
	    transactorData.push(mapObj);
	    transactorRelationshipJsonString += transactorRelationshipGenerateJson(mapObj,index);
        }
    }catch(e) {
        alert(e);
    }
	transactorRelationshipJsonString += "]";
	return transactorRelationshipJsonString;
}

/**
 * 
 * @param mapObj
 * @param index
 * @returns {String}
 */
function transactorRelationshipGenerateJson(mapObj,index){
	var transactorRelationshipJson = "";
	if(index == 0 ){
		transactorRelationshipJson = "{'user':'"+mapObj['referenceOperator']+"','relationship':'"+mapObj['referenceRelation']+"','operation':'"+mapObj['referenceJoin']+"'}";
	}else{
		transactorRelationshipJson = ",{'user':'"+mapObj['referenceOperator']+"','relationship':'"+mapObj['referenceRelation']+"','operation':'"+mapObj['referenceJoin']+"'}";
	}
	return transactorRelationshipJson;
}

/**
 * 
 * @param transactorRelationshipJson
 */
function reInitializeTransactorRelationshipObj(transactorRelationshipJson){
	var transactorRelationshipMap = transactorRelationshipJson["relationship"];
	for(var i= 0;i< transactorRelationshipJson.length;i++){
		transactorRelationshipTableAddRow('transactorRelationshipDataTable',transactorRelationshipJson[i]);
	}	
}

/**
 * 
 * @param paramFormId
 * @returns {String}
 */
function getFormFieldAutomaticJson(paramFormId){
	return formFieldAutomaticGetData("opinionFormFillTable",paramFormId);
}

/**
 * GenerateFill Value select 
 *
 * @param value
 * @returns {String}
 */
function genarateFillValue(value,previousFormIds){
	var optionValue = ["Organizer","Organizer Sign Off Time","Opinion","Value Selected From Form"];
	var htmlContent = "<select id='opinionFillValue' name='opinionFillValue' onChange='getOpinionValueFromForm(this,\""+previousFormIds+"\")' style='width:150px'>";
	for(var i=0 ;i<optionValue.length;i++){
		if(value == optionValue[i]){
			htmlContent += "<option value='"+optionValue[i]+"' selected='selected'>"+optionValue[i]+"</option>";
		}else{
			htmlContent += "<option value='"+optionValue[i]+"'>"+optionValue[i]+"</option>";
		}
			
	}
	htmlContent += "</selected>";
	return htmlContent;
}

/**
 * to get all form
 *
 * @param element
 * @returns {String}
 */
function getOpinionValueFromForm(element,previousFormIds){
	if(element.value == "Value Selected From Form"){
		var opinionForm = "<table width='100%'><tr><td><b>"+ORYX.I18N.PropertyWindow.fromForm+"</b></td><td>";
		 Ext.Ajax.request({
				url: "/form/getFormsByIds",
				params: {formIds :previousFormIds},
				asynchronous: false,
				method: 'GET',
				scope:this,
				failure:function(response,options){},
				success : function(response,options){ 
					var fieldData = response.responseText;
					var jsonFieldData = {};
					jsonFieldData = JSON.parse(fieldData.toString());
					var fieldParse = jsonFieldData.forms;
					var htmlContent = "<select id='opinonPreviousForm' name='opinonPreviousForm' onChange='setPreviousFormField(this)' style='width:230px'><option value='' selected='selected'></option>"; 
					for(var i=0;i<fieldParse.length;i++){
						htmlContent += "<option value='"+fieldParse[i].id+"' label='"+fieldParse[i].formName+"'>"+fieldParse[i].formName+"</option>";
					}
					htmlContent += "</selected>";
					opinionForm += htmlContent;
					opinionForm += "</td></tr><tr><td><b>"+ORYX.I18N.PropertyWindow.fromFormField+"</b></td><td><input type='text' id='previousFormField'/><select id='opinionFromFormField' name='opinionFromFormField' style='width:230px;display:none' ></select></table>";
					document.getElementById('opinionPreviewForm').innerHTML = opinionForm;
					document.getElementById('opinionPreviewForm').style.display = 'block';
				}
		});
	}else{
		document.getElementById('opinionPreviewForm').style.display = 'none';
	}
}

/**
 * get all fields of form
 *
 * @param element
 */
function setPreviousFormField(element){
	var paramFormId = element.value;
	 Ext.Ajax.request({
				url: "/bpm/form/getAllFormFields",
				params: {formId :paramFormId},
				asynchronous: false,
				method: 'GET',
				scope:this,
				failure:function(response,options){},
				success : function(response,options){ 
					var fieldData = response.responseText;
					var jsonFieldData = {};
					jsonFieldData = JSON.parse(fieldData.toString());
					var fieldParse = jsonFieldData.forms;
					var htmlContent = "";
					for(var i= 0;i< fieldParse.length;i++){
						/*if(formName == fieldParse[i].name){
							permissionContainer += "<option value='"+fieldParse[i].name+"'selected=selected>"+fieldParse[i].name+"</option>";
						}else{*/
							if(!fieldParse[i].id.endsWith("_FullName")) {
								htmlContent += "<option value='"+fieldParse[i].name+"'>"+fieldParse[i].name+"</option>";
							}
						//}
					}
					document.getElementById("opinionFromFormField").innerHTML = htmlContent;
					document.getElementById("previousFormField").style.display = 'none';
					document.getElementById("opinionFromFormField").style.display = 'block';
				}
		});

}

/**
 *
 * @param fillType
 * @returns {String}
 */
function generateFillType(fillType){

	var optionFillType = ["Append","Replace"];
	var htmlContent = "<select id='opinionFillType' name='opinionFillType' style='width:150px'>";
	for(var i=0 ;i<optionFillType.length;i++){
		if(fillType == optionFillType[i]){
			htmlContent += "<option value='"+optionFillType[i]+"' selected='selected'>"+optionFillType[i]+"</option>";
		}else{
			htmlContent += "<option value='"+optionFillType[i]+"'>"+optionFillType[i]+"</option>";
		}
			
	}
	htmlContent += "</selected>";
	return htmlContent;	
		
}


/**
 * 
 * @param resourceType
 * @param paramformId
 * @param resourceValue
 */
function setFormFieldAutomaticField(resourceValue,resourceType,paramFormId,previousFormIds){
	var formFieldAutomatic = "<div><table width='100%'><tr><td><b>"+ORYX.I18N.PropertyWindow.fillForm+"</b></td><td>";
	
	 Ext.Ajax.request({
				url: "/bpm/form/getAllFormFields",
				params: {formId :paramFormId},
				asynchronous: false,
				method: 'GET',
				scope:this,
				failure:function(response,options){},
				success : function(response,options){ 
					var fieldData = response.responseText;
					var jsonFieldData = {};
					jsonFieldData = JSON.parse(fieldData.toString());
					var fieldParse = jsonFieldData.forms;
					var formName = fieldParse[0].id.split("_")[0];
					var htmlContent = "<input type='text' style='width:145px' readonly='true'  id='opinionFormFill' name='"+paramFormId+"' value='"+formName+"'/></td><td><b>"+ORYX.I18N.PropertyWindow.formFields+"</b></td><td><select id='opinionFormFillField' name='opinionFormFillField' style='width:150px'>";
					for(var i= 0;i< fieldParse.length;i++){
						if(!fieldParse[i].id.endsWith("_FullName")) {
							htmlContent += "<option value='"+fieldParse[i].name+"'>"+fieldParse[i].name+"</option>";
						}
					}
					
					htmlContent += "</select>";
					formFieldAutomatic += htmlContent;
					formFieldAutomatic += "</td></tr><tr><td><b>"+ORYX.I18N.PropertyWindow.fillValue+"</b></td><td>"+genarateFillValue('none',previousFormIds)+"</td><td><b>"+ORYX.I18N.PropertyWindow.fillType+"</b></td><td>"+generateFillType('none')+"</td></tr></table>";
					
					formFieldAutomatic += "<div id='opinionPreviewForm' style='display:none'></div><table width='90px' style='margin-top:5px'><tbody><tr><td><b>"+ORYX.I18N.PropertyWindow.isSkip+"</b></td><td><input type='checkbox' id='isSkipFormFieldAutomatic'></td></tr></tbody></table><center><input type='button' class='btn-primary btn' style='font-size:11px;padding:2px 7px;' onClick='addOpinionFillFormValue(\"\",\"\",\""+paramFormId+"\")' value='Add'/>&nbsp&nbsp&nbsp<input type='button' onClick='editFormFieldAutomatic()' value='Edit'  class='btn btn-primary' style='font-size: 11px;padding: 2px 7px;'/></div>";
					document.getElementById('opinionDiv').innerHTML = formFieldAutomatic;
					
					var previousFormFlag = checkPreviousFormId(resourceValue,paramFormId);
					
					if(resourceValue != "" && previousFormFlag == true){
						reInitializeObjectObj(resourceValue,resourceType,"formFieldAutomatic",paramFormId);
					}
				}		
	});
}

/**
 * to check the previous FormId
 *
 * @param resourceValue
 * @param paramFormId
 * @return {boolean}
 */
function checkPreviousFormId(resourceValue ,paramFromId){
	if(resourceValue != ""){
		var strReplace = '"['+resourceValue.replace('$','').replace(/^\s+|\s+$/g,"")+']"';
		var resourceJSON = eval(JSON.parse(strReplace));
		var previousFormId = resourceJSON[0].previousFormId;
		//previousFormFieldAutomatic
		if(previousFormId == paramFromId){
			return true;
		} else {
			return false;
		}
	}else{
		return false;
	}
		
}

/**
 * add opinion to table based on reInitialize flag
 *
 * @param formObject
 * @param paramFormId
 */
function addOpinionFillFormValue(formObject,reInitialize,paramFormId){
	if(reInitialize == 'true'){
		var table = document.getElementById('opinionFormFillTable');
		var rowCount = table.rows.length;
		if(rowCount <= document.getElementById("opinionFormFillField").options.length){
		var row = table.insertRow(rowCount);
		var deleteRow = row.insertCell(0);
		var fillForm = row.insertCell(1);
		var fillField = row.insertCell(2);
		var fillFieldValue = row.insertCell(3);
		var fillType = row.insertCell(4);
		var fromForm = row.insertCell(5);
		var fromFormField = row.insertCell(6);
		var isSkip = row.insertCell(7);
		
		fillForm.innerHTML = document.getElementById("opinionFormFill").value;
		fillField.innerHTML = formObject.fillField;
		fillFieldValue.innerHTML = formObject.fillFieldValue;
		fillType.innerHTML = formObject.fillType;
		if(formObject.fillFieldValue == "ValueSelectedFromForm"){
			fromForm.innerHTML = formObject.fromForm;
			fromFormField.innerHTML = formObject.fromFormField;
		}else{
			fromForm.innerHTML = "";
			fromFormField.innerHTML = "";
		}
		isSkip.innerHTML = formObject.isSkip;
		deleteRow.innerHTML = '<input type="checkbox" id="deleteSelectedOpinion" name="deleteSelectedOpinion"/>';
		hideTableColumn(5,'no');
		hideTableColumn(6,'no');
		//deleteRow.innerHTML = '<input type="button" id="delete_formFieldOpinion" value="Delete" onClick="deleteSelectedFormField(this)"/>';
		}else{
			//alert("Adding form value must less than field length");
			Ext.Msg.show({
	   			title:'Error',
	   			msg: ORYX.I18N.PropertyWindow.formFieldError,
	   			Buttons: Ext.Msg.OK,
	   			animEl: 'elId',
	   			icon: Ext.MessageBox.ERROR
        });
		}
		
	}else{	
		previousFormFieldAutomatic = paramFormId;
		var table = document.getElementById('opinionFormFillTable');
		var rowCount = table.rows.length;
		//if(rowCount <= document.getElementById("opinionFormFillField").options.length){
		var isPreviousFormFill = checkPreviousFormFillValue();
		var row;
               	if(isPreviousFormFill == undefined){
               	 	//alert("Cannt set one field value in more than one time");
            	 	row = table.insertRow(rowCount);
         
               	}else{
               		table.deleteRow(isPreviousFormFill);
               		row = table.insertRow(isPreviousFormFill);
               	}
			var deleteRow = row.insertCell(0);
			var fillForm = row.insertCell(1);
			var fillField = row.insertCell(2);
			var fillFieldValue = row.insertCell(3);
			var fillType = row.insertCell(4);
			var fromForm = row.insertCell(5);
			var fromFormField = row.insertCell(6);
			var isSkip = row.insertCell(7);
               	 	fillForm.innerHTML = document.getElementById("opinionFormFill").value;
               	 	fillField.innerHTML = document.getElementById("opinionFormFillField").value;
               	 	fillFieldValue.innerHTML = document.getElementById("opinionFillValue").value;
			fillType.innerHTML = document.getElementById("opinionFillType").value;
			if(document.getElementById("opinionFillValue").value == "Value Selected From Form"){
				fromForm.innerHTML = document.getElementById("opinonPreviousForm").value;
				fromFormField.innerHTML = document.getElementById("opinionFromFormField").value;
			}else{
				fromForm.innerHTML = "";
				fromFormField.innerHTML = "";
			}
			isSkip.innerHTML = document.getElementById("isSkipFormFieldAutomatic").checked;
			deleteRow.innerHTML = '<input type="checkbox" id="deleteSelectedOpinion" name="deleteSelectedOpinion"/>';
			hideTableColumn(5,'no');
			hideTableColumn(6,'no');
               	
               
		//deleteRow.innerHTML = '<input type="button" id="delete_formFieldOpinion" value="Delete" onClick="deleteSelectedFormField(this)"/>';
		//}else{
			//alert("Adding form value must less than field length");
			/*Ext.Msg.show({
	   							title:'Error',
	   							msg: "Adding form value must less than field length. ",
	   							buttons: Ext.Msg.OK,
	   							animEl: 'elId',
	   							icon: Ext.MessageBox.ERROR
               	});
		}*/
	}
}

/**
 * edit opinion value in table
 *
 */
function editFormFieldAutomatic(){
	try{
	    var table = document.getElementById('opinionFormFillTable');
            var rowCount = table.rows.length;
 	    var noRowSelected = 1;
 	    var isMultiSelect = checkSelectedRowCountToEdit();
 	    if(isMultiSelect == true){
            for(var i=1; i<rowCount; i++) {
                var row = table.rows[i];
                var chkbox = row.cells[0].childNodes[0];
                if(null != chkbox && true == chkbox.checked) {
                    if(rowCount <= 1) {
                        alert(ORYX.I18N.PropertyWindow.deleteError);
												Ext.Msg.show({
	   											title:'Error',
	   											msg: ORYX.I18N.PropertyWindow.deleteError,
	   											buttons: Ext.Msg.OK,
	   											animEl: 'elId',
	   											icon: Ext.MessageBox.ERROR
               	});
                        break;
                    }
                    document.getElementById("opinionFormFill").value = row.cells[1].innerHTML;
                    document.getElementById("opinionFormFillField").value = row.cells[2].innerHTML;
                    document.getElementById("opinionFillValue").value = row.cells[3].innerHTML;
                    document.getElementById("opinionFillType").value = row.cells[4].innerHTML;
                    if(row.cells[3].innerHTML == "Value Selected From Form"){

                    		document.getElementById("opinionPreviewForm").style.display = 'block';
				document.getElementById("opinonPreviousForm").value = row.cells[5].innerHTML;
				document.getElementById("opinionFromFormField").value = row.cells[6].innerHTML;
		    }else{
		    		if(document.getElementById("opinonPreviousForm") != null){
			    		document.getElementById("opinonPreviousForm").value = "";
					document.getElementById("opinionFromFormField").value = "";
				}
				document.getElementById("opinionPreviewForm").style.display = 'none';
				
		    }
		    if(row.cells[7].innerHTML == "false"){
		   	 document.getElementById("isSkipFormFieldAutomatic").checked = "";
		    }else{
		   	 document.getElementById("isSkipFormFieldAutomatic").checked = "true";
		    }
                    //table.deleteRow(i);
                   // rowCount--;
                   // i--;
                }else{
                	
                	noRowSelected += 1;
                }
          }
          } else {
          	//alert("Please Select only one row to edit");
        	  	Ext.Msg.show({
				   			title:'Error',
				  			msg: ORYX.I18N.PropertyWindow.rowError,
				   			buttons: Ext.Msg.OK,
						 	animEl: 'elId',
						 	icon: Ext.MessageBox.ERROR
				});
          }
         
        }catch(e) {
                alert(e);
     }
    
}

/**
 * to check only one field to select for edit
 *
 * @return {boolean}
 */
 function checkSelectedRowCountToEdit(){
     var checkboxes = new Array();
     checkboxes = document.getElementsByName('deleteSelectedOpinion');
     var count = 0;
     for (var i = 0; i < checkboxes.length; i++) {
         if (checkboxes[i].type == 'checkbox' && checkboxes[i].checked == true) {
             count += 1;
         }
     }
     if(count == 1){
     	return true;
     } else {
     	return false;
     }
 }

/**
 * to check previous opinion form value
 *
 * @return {String}
 */ 
function checkPreviousFormFillValue(){
	var table = document.getElementById('opinionFormFillTable');
	var rowCount = table.rows.length;
	var isPreviousFormFill;
	for(var i=1;i< rowCount;i++){
		var insertedFillField = document.getElementById('opinionFormFillTable').rows[i].cells[2].innerHTML;
               	if(insertedFillField == document.getElementById("opinionFormFillField").value){
               	 	isPreviousFormFill =  i;
               	}
      	}
      	return isPreviousFormFill;
}

/** 
 * delete specific row in opinion table
 */
function deleteSelectedFormField(){
	//var rowsIndex=tabObj.parentElement.parentElement.rowIndex;
	//var table = document.getElementById('opinionFormFillTable');
   	 try {
            var table = document.getElementById('opinionFormFillTable');
            var rowCount = table.rows.length;
 	    var noRowSelected = 1;
            for(var i=1; i<rowCount; i++) {
                var row = table.rows[i];
                var chkbox = row.cells[0].childNodes[0];
                if(null != chkbox && true == chkbox.checked) {
                    if(rowCount <= 1) {
                        //alert("Cannot delete all the rows.");
												Ext.Msg.show({
				   								title:'Error',
				  								msg: ORYX.I18N.PropertyWindow.deleteError,
				   								buttons: Ext.Msg.OK,
						 							animEl: 'elId',
						 							icon: Ext.MessageBox.ERROR
											});
                        break;
                    }
                    table.deleteRow(i);
                    rowCount--;
                    i--;
                }else{
                	noRowSelected += 1;
                }
          }
         
        }catch(e) {
                alert(e);
     }
}

/**
 * delete all records in opinion table
 */
function deleteAllFormField(){
	 try {
            var table = document.getElementById('opinionFormFillTable');
            var rowCount = table.rows.length;
 	    var noRowSelected = 1;
            for(var i=1; i<rowCount; i++) {
                var row = table.rows[i];
                var chkbox = row.cells[0].childNodes[0];
                
                    table.deleteRow(i);
                    rowCount--;
                    i--;
                
          }
         
        }catch(e) {
                alert(e);
     }
}

/**  
 * to hide the specific column in table
 *
 * @param columno
 * @param isShow
 */
function hideTableColumn( columnNo, isShow ){
     var rows = document.getElementById('opinionFormFillTable').rows;
     for (var row = 0; row < rows.length; row++) {
        var cols = rows[row].cells;
        if (columnNo >= 0 && columnNo < cols.length) {
            cols[columnNo].style.display = isShow ? 'none' : 'none';
        }
     }

}

/**
 * get form field Automatic value from table
 *
 * @param tableId
 * @param paramFormId
 * @retrun {String}
 */
function formFieldAutomaticGetData(tableID,paramFormId){

	try {
		var table = document.getElementById(tableID);
		var rowCount = table.rows.length;
	    	var formFieldData = new Array();
		var formFieldAutomaticJsonString = "[";
		for(var index=1; index < rowCount; index++) {
			var mapObj = {};
			var row = table.rows[index];
	     	    	var fillForm = row.cells[1].innerHTML;
			var fillField = row.cells[2].innerHTML;
			var fillFieldValue = row.cells[3].innerHTML;
			var fillType = row.cells[4].innerHTML;
			var fromForm = row.cells[5].innerHTML;
			var fromFormField = row.cells[6].innerHTML;
			var isSkip = row.cells[7].innerHTML;
		   	mapObj['fillForm'] = paramFormId;
		    	mapObj['fillField'] = fillField;
		   	 mapObj['fillFieldValue'] = fillFieldValue.replace(/ /gi,"");
		    	mapObj['fillType'] = fillType;
		    	mapObj['fromForm'] = fromForm;
		    	mapObj['fromFormField'] = fromFormField;
		    	mapObj['isSkip'] = isSkip;
		    	formFieldData.push(mapObj);
		        formFieldAutomaticJsonString += formFieldAutomaticGenerateJson(mapObj,index);
            }
     }catch(e) {
        alert(e);
    }	formFieldAutomaticJsonString += "]";
    return formFieldAutomaticJsonString;
}

/**
 * to generate the json
 *
 * @param mapObj
 * @param index
 * @return {String}
 */
function formFieldAutomaticGenerateJson(mapObj,index){
	var formFieldAutomaticJson = "";
	if(index == 1){
		formFieldAutomaticJson = "{'fillForm':'"+mapObj['fillForm']+"','fillField':'"+mapObj['fillField']+"','fillFieldValue':'"+mapObj['fillFieldValue']+"','fillType':'"+mapObj['fillType']+"','fromForm':'"+mapObj['fromForm']+"','fromFormField':'"+mapObj['fromFormField']+"','isSkip':'"+mapObj['isSkip']+"'}";
	}else{
		formFieldAutomaticJson = ",{'fillForm':'"+mapObj['fillForm']+"','fillField':'"+mapObj['fillField']+"','fillFieldValue':'"+mapObj['fillFieldValue']+"','fillType':'"+mapObj['fillType']+"','fromForm':'"+mapObj['fromForm']+"','fromFormField':'"+mapObj['fromFormField']+"','isSkip':'"+mapObj['isSkip']+"'}";	
	
	}
	return formFieldAutomaticJson
}

/**
 *
 * @param formFieldAutomaticJson
 * @param paramFormId
 */
function reInitializeFromFieldAutomaticObj(formFieldAutomaticJSON,paramFormId){
	for(var i= 0;i< formFieldAutomaticJSON.length;i++){
		addOpinionFillFormValue(formFieldAutomaticJSON[i],'true',paramFormId);
	}	
}

/**
 * setting process time
 *
 * @param taskName
 * @param resourceType
 * @param resourceValue
 */
function setProcessTimeSetting(resourceValue,resourceType,taskName){
	if(resourceValue == ""){
		var processTimeSetting = "<table width='100%'><tr><td><b>"+ORYX.I18N.PropertyWindow.maxDays+"</b></td><td><input type='text' style='width:145px' id='timeSettingMaxDays' value=0 ></td><td><b>"+ORYX.I18N.PropertyWindow.warningDays+"</b></td><td><input type='text' style='width:145px' id='timeSettingWarinigDays' value=0 ></td></tr><br/><tr></tr><tr></tr><tr><td><b>"+ORYX.I18N.PropertyWindow.dateType+"</b></td><td>"+generateDateType('none')+"</td><td><b>"+ORYX.I18N.PropertyWindow.urgeTimes+"</b></td><td><input type='text' style='width:145px' id='timeSettingUrgeTimes' value=0 ></td></tr><tr></tr><tr></tr><tr><td><b>"+ORYX.I18N.PropertyWindow.frequence+"</b></td><td><input type='text' style='width:145px' id='timeSettingFrequence' value=0 >&nbsp&nbsp<small>"+ORYX.I18N.PropertyWindow.hours+"</small></td><td><b>"+ORYX.I18N.PropertyWindow.dealTimeout+"</b></td><td>"+generateDealIfTimeout('none')+"</td></tr><tr></tr><tr></tr></table><br/><b>"+ORYX.I18N.PropertyWindow.notificationTYpe+"</b>&nbsp&nbsp&nbsp"+generateNotificationType('none')+"";
	
		document.getElementById('processTimeSetting').innerHTML = processTimeSetting;
	}else{
		var htmlProcessTimeSetting = "";
		var strReplace = '"['+resourceValue.replace('$','').replace(/^\s+|\s+$/g,"")+']"';
		var resourceJSON = eval(JSON.parse(strReplace));
		var processTimeSetting = resourceJSON[0].processTimeSetting;
		for(var i=0 ;i<processTimeSetting.length; i++){
			htmlProcessTimeSetting += "<table width='100%'><tr><td><b>"+ORYX.I18N.PropertyWindow.maxDays+"</b></td><td><input type='text' style='width:145px' id='timeSettingMaxDays' value="+processTimeSetting[i].maxDays+" ></td><td><b>"+ORYX.I18N.PropertyWindow.warningDays+"</b></td><td><input type='text' style='width:145px' id='timeSettingWarinigDays' value="+processTimeSetting[i].warningDays+" ></td></tr><br/><tr></tr><tr></tr><tr><td><b>"+ORYX.I18N.PropertyWindow.dateType+"</b></td><td>"+generateDateType(processTimeSetting[i].dateType)+"</td><td><b>"+ORYX.I18N.PropertyWindow.urgeTimes+"</b></td><td><input type='text' style='width:145px' id='timeSettingUrgeTimes' value="+processTimeSetting[i].urgeTimes+" ></td></tr><tr></tr><tr></tr><tr><td><b>"+ORYX.I18N.PropertyWindow.frequence+"</b></td><td><input type='text' style='width:145px' id='timeSettingFrequence' value="+processTimeSetting[i].frequence+" >&nbsp&nbsp<small>"+ORYX.I18N.PropertyWindow.hours+"</small></td><td><b>"+ORYX.I18N.PropertyWindow.dealTimeout+"</b></td><td>"+generateDealIfTimeout(processTimeSetting[i].dealIfTimeout)+"</td></tr><tr></tr><tr></tr></table><br/><b>"+ORYX.I18N.PropertyWindow.notificationTYpe+"</b>&nbsp&nbsp&nbsp"+generateNotificationType(processTimeSetting[i].notificationType)+"";
		}
		document.getElementById('processTimeSetting').innerHTML = htmlProcessTimeSetting;
	}
}

function validateWarinigDays(){
	if(document.getElementById('timeSettingMaxDays').value < document.getElementById('timeSettingWarinigDays').value){
		return false;
	}else{
		return true;
	}
}

/**
 * generate process time setting JSON 
 *
 * @return {String}
 */
function generateTimeSettingJSON(){
	var maxDays = document.getElementById('timeSettingMaxDays').value;
	var warningDays = document.getElementById('timeSettingWarinigDays').value;
	var dateType = document.getElementById('timeSettingDateType').value;
	var urgeTimes = document.getElementById('timeSettingUrgeTimes').value;
	var frequence = document.getElementById('timeSettingFrequence').value;
	var dealIfTimeout = document.getElementById('timeSettingDealIfTimeout').value;
	var notificationType = "";
	if (document.getElementById('processNotificationsms').checked == true) {
		notificationType += document.getElementById('processNotificationsms').value;
	}
	if (document.getElementById('processNotificationemail').checked == true) {
		if(notificationType == ""){
			notificationType += document.getElementById('processNotificationemail').value;
		}else{
			notificationType += "," + document.getElementById('processNotificationemail').value;
		}
		
	} 
	if(document.getElementById('processNotificationinternal').checked == true) {
		if(notificationType == ""){
			notificationType += document.getElementById('processNotificationinternal').value;
		}else{
			notificationType += "," + document.getElementById('processNotificationinternal').value;
		}
		
	}
	var jsonString = "[{";
	jsonString += "'maxDays'"+":'"+maxDays+"','warningDays'"+":'"+warningDays+"','dateType'"+":'"+dateType+"','urgeTimes'"+":'"+urgeTimes+"','frequence'"+":'"+frequence+"','dealIfTimeout'"+":'"+dealIfTimeout+"','notificationType'"+":'"+notificationType+"'";
	jsonString += "}]";
	return jsonString;
}

/**
 * generate process time setting Date Type
 *
 * @param dateType
 * @retrun {String}
 */
function generateDateType(dateType){
	var dateTypeArray = ["work","normal"];
	var htmlContent = "<select id='timeSettingDateType' style='width:150px'>";
	for(var i=0; i<dateTypeArray.length; i++){
		var changeDisplay = "";
		if(dateTypeArray[i] == 'work'){
			changeDisplay = "Work Days"
		}
		if(dateTypeArray[i] == 'normal'){
			changeDisplay = "Normal Days"
		}
		
		if(dateType == dateTypeArray[i]){
			htmlContent += "<option value='"+dateTypeArray[i]+"' selected='selected'>"+changeDisplay+"</option>";
		}else{
			htmlContent += "<option value='"+dateTypeArray[i]+"'>"+changeDisplay+"</option>";
		}
		
	}
	htmlContent += "</select>";

	return htmlContent;
}

/**
 * generate process time setting Notification Type
 *
 * @param notificationType
 * @retrun {String}
 */
function generateNotificationType(notificationType){
	var notificationTypeArray = ["sms","email","internal"];
	var htmlContent = "";
	for(var i=0; i<notificationTypeArray.length; i++){
		var changeDisplay = "";
		if(notificationTypeArray[i] == 'sms'){
			changeDisplay = "SMS Notification"
		}
		if(notificationTypeArray[i] == 'email'){
			changeDisplay = "Email Notification"
		}
		if(notificationTypeArray[i] == 'internal'){
			changeDisplay = "Internal Message"
		}
		
		if(notificationType != 'none' && notificationType.indexOf(notificationTypeArray[i]) != -1){
			htmlContent += "&nbsp&nbsp<input type='checkbox' value='"+notificationTypeArray[i]+"' id='processNotification"+notificationTypeArray[i]+"'  checked='true'>&nbsp<b>"+changeDisplay+"</b></input>";
		}else{
			htmlContent += "&nbsp&nbsp<input type='checkbox' id='processNotification"+notificationTypeArray[i]+"' value='"+notificationTypeArray[i]+"'>&nbsp<b>"+changeDisplay+"</b></input>";
		}
		
	}
	htmlContent += "";

	return htmlContent;
}

/**
 * generate process time setting DealIfTimeout
 *
 * @param dealIfTimeout
 * @retrun {String}
 */
function generateDealIfTimeout(dealIfTimeout){
	var dealIfTimeoutArray = ["undeal","notice","suspend","return","jump"];
	var htmlContent = "<select id='timeSettingDealIfTimeout' style='width:150px'>";
	for(var i=0; i<dealIfTimeoutArray.length; i++){
		var changeDisplay = "";
		if(dealIfTimeoutArray[i] == 'undeal'){
			changeDisplay = "Don’t Deal";
		}
		if(dealIfTimeoutArray[i] == 'notice'){
			changeDisplay = "Notice to Admin";
		}
		if(dealIfTimeoutArray[i] == 'suspend'){
			changeDisplay = "Suspend Instance";
		}
		if(dealIfTimeoutArray[i] == 'return'){
			changeDisplay = "Return to Previous Node";
		}
		if(dealIfTimeoutArray[i] == 'jump'){
			changeDisplay = "Jump to Next Node";
		}
		
		if(dealIfTimeout == dealIfTimeoutArray[i]){
			htmlContent += "<option value='"+dealIfTimeoutArray[i]+"' selected='selected'>"+changeDisplay+"</option>";
		}else{
			htmlContent += "<option value='"+dealIfTimeoutArray[i]+"'>"+changeDisplay+"</option>";
		}
		
	}
	htmlContent += "</select>";

	return htmlContent;
}

/**
 * to check the property names
 *
 * @param pairType
 * @return boolean
 */
 function checkFirstNodeTaskProperty(pairType,pairName){
  	var isEqualProperty = true;
	var  pairTypeArrayProperty =  [ "formmemberpermission", "addformfieldautomatic", "addonread", "addoncreate", "addonupdate", "formfieldpermission", "operationpermission", "addopinion", "addprocesstimesetting" ,"choice"]; 	
			
	for(var i=0;i<pairTypeArrayProperty.length;i++){
		if(pairType == pairTypeArrayProperty[i]){
			isEqualProperty = true;
		}else{
			isEqualProperty = false;
		}
	}
	if(pairName == "OF Creator"){
		isEqualProperty = true;
	}
	return isEqualProperty;
 }
