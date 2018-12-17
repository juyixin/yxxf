<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
 <%@ include file="/common/taglibs.jsp" %>
 <%@ include file="/common/messages.jsp" %>
<script type="text/javascript">
var userRole=$("#currentUserRoles").val().split(",");
var isAdmin=checkRoles(userRole,"ROLE_ADMIN");
/*START */
function allowMovingModuleOrder(){
	$("#module_relation_tree").bind("move_node.jstree", function (e, data) {
		/*
	    data.rslt contains: 
	    .o - the node being moved 
	    .r - the reference node in the move 
	    .ot - the origin tree instance 
	    .rt - the reference tree instance 
	    .p - the position to move to (may be a string - "last", "first", etc) 
	    .cp - the calculated position to move to (always a number) 
	    .np - the new parent 
	    .oc - the original node (if there was a copy) 
	    .cy - boolen indicating if the move was a copy 
	    .cr - same as np, but if a root node is created this is -1 
	    .op - the former parent 
	    .or - the node that was previously in the position of the moved node 
	    */
	  var currentNodeName = data.rslt.o.data("name");
	  var replaceNodeName = data.rslt.or.data("name");
	  
	  var currentNodeType = data.rslt.o.attr("nodetype");
	  var replaceNodeType = data.rslt.or.attr("nodetype");
	  
	  var currentNodeOrder = data.rslt.o.attr("nodeOrder");
	  var replaceNodeOrder = data.rslt.or.attr("nodeOrder");//data.rslt.np.attr("nodeOrder");
	  
	  var replaceNodeModuleId = data.rslt.np.attr("nodeid");
	  var replaceNodeModuleType = data.rslt.np.attr("nodetype");
	 
	    var currentNodeId ;
	    var replaceNodeId ;
	    var level = data.inst.get_path().length;
	    
	    if(level == 1){
	    	currentNodeId = data.rslt.o.attr("nodeid");
	    	replaceNodeId = data.rslt.or.attr("nodeid");
	     }
	    
	    if(!isAdmin){
	    	$(function () {

	    	    $("#module_relation_tree")

	    	        .jstree({ "plugins" : ["themes","html_data","ui"] })

	    	        // 1) the loaded event fires as soon as data is parsed and inserted

	    	        .bind("loaded.jstree", function (event, data) { })

	    	        // 2) but if you are using the cookie plugin or the core `initially_open` option:

	    	        .one("reopen.jstree", function (event, data) { })

	    	        // 3) but if you are using the cookie plugin or the UI `initially_select` option:

	    	        .one("reselect.jstree", function (event, data) { });

	    	});
	    	 $.msgBox({
	   		    title:form.title.message,
	   		    content:form.msg.permission,
	   		});  
	    	 document.location.href = "#bpm/module/manageModuleRelation";
				_execute('target','');
				return false;
	    }
	    /*Copy Module*/
	    if(currentNodeType == replaceNodeModuleType && currentNodeType!='undefined' && level == 2 && currentNodeType=='module'){
	    	 var replaceNodeModuleName = data.rslt.np.attr("name");
	        if(currentNodeType=='module' && replaceNodeModuleType=='module'){
	        	orderingModule(currentNodeName,replaceNodeModuleName,currentNodeOrder,replaceNodeOrder);	
		    }else{
		    	$(function () {

		    	    $("#module_relation_tree")

		    	        .jstree({ "plugins" : ["themes","html_data","ui"] })

		    	        // 1) the loaded event fires as soon as data is parsed and inserted

		    	        .bind("loaded.jstree", function (event, data) { })

		    	        // 2) but if you are using the cookie plugin or the core `initially_open` option:

		    	        .one("reopen.jstree", function (event, data) { })

		    	        // 3) but if you are using the cookie plugin or the UI `initially_select` option:

		    	        .one("reselect.jstree", function (event, data) { });

		    	});
		    	 $.msgBox({
		   		    title:form.title.message,
		   		    content:form.msg.modulePermission,
		   		});  
		    	 document.location.href = "#bpm/module/manageModuleRelation";
					_execute('target','');
		    }
	    }else if(currentNodeType=='table' && replaceNodeModuleType=='module' && replaceNodeModuleId!='undefined' ){
	    	copyTableToModule(data.rslt.o.attr("nodeid"),replaceNodeModuleId,currentNodeName,data.rslt.np.attr("name"),data.rslt.o.attr("modulename"));
	    }else if(currentNodeType=='listview' && replaceNodeModuleType=='module' && replaceNodeModuleId!='undefined' ){
	    	copyListViewToModule(data.rslt.o.attr("nodeid"),replaceNodeModuleId,currentNodeName,data.rslt.np.attr("name"),data.rslt.o.attr("modulename"));
	    }else{
			orderingModule(currentNodeName,replaceNodeModuleName,currentNodeOrder,replaceNodeOrder);
	    }  
	   });
}

/*END*/

var isCreate ="true";
function rightPanel(e,data){
	$('div#header_links').hide();
	$("div#moduleDiv_edit").hide();
	$("div#moduleDiv_admin").hide();
	$("div#moduleimportDiv").show();
	
	$("div#tableDiv_admin").hide();
	$("div#tableDiv_edit").hide();
	$("div#tableDiv_all").hide();
	
	 $("div#listViewDiv_edit").hide();
	 $("div#listViewDiv_admin").hide();
	 $("div#formDiv").hide();
	 $("div#jspFormDiv").hide();
	 
	
	// $("span#module_mig_import").hide();
	var currentNode = data.rslt.obj.attr("nodeid");
	var isEdit=data.rslt.obj.attr("isedit");
	var level = data.inst.get_path().length;
	 var parentNode ="";
	 if(level != 1){
		 parentNode = data.inst._get_parent(data.rslt.obj).attr("nodeid");
	}else if(level == 1){
		 if(currentNode.indexOf("AllForm") > -1){
			 if(isAdmin || isEdit=="true"){
				 $("div#formDiv").show();
			 }else{
				 $("div#formDiv").hide();
			 }
			 $("div#moduleimportDiv").hide();
			 currentNode = currentNode.substring(0, currentNode.length-7);
		 	 var params = 'moduleId='+currentNode;
		     document.location.href = "#bpm/form/formListForModule";
		     _execute('rightPanel',params); 
		     $("#module_title").html("");
		   	 $("#module_title").html("表单列表");
		 }else{
			 var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
			 var moduleName=selected_nodes.data("name");
			 if(isAdmin){
				 $("div#moduleDiv_admin").show();
				 $("div#moduleDiv_edit").show(); 
				 $("div#moduleimportDiv").show();
				 $("span#module_mig_import").show();
			}else if(isEdit=="true"){
				 $("div#moduleDiv_admin").hide();
				 $("div#moduleDiv_edit").show(); 
				 $("div#moduleimportDiv").show();
				 $("span#module_mig_import").show();
			 }else{
				 $("div#moduleDiv_admin").hide();
				 $("div#moduleDiv_edit").hide();
				 $("div#moduleimportDiv").show();
				 $("span#exportSql").hide();
			 }
			
			 if(moduleName!="Default Module"){
				 $("span#module_mig_export").show();
			 }else{
				 $("span#module_mig_export").hide();
			 }
			 if(isEdit !="true" ){
				 $("span#module_mig_import").hide();
				 $("span#module_mig_export").hide();
			 }
			 var params = 'id='+currentNode;
			 document.location.href = "#bpm/module/createModule";
			 _execute('rightPanel',params);
			 $("#module_title").html("");
		   	 $("#module_title").html("新建模块");
		 }
	}
	 
	
	 if(level == 3 && parentNode.indexOf("Tables") != -1){
	 	 if(isAdmin || isEdit=="true"){
			  $("div#tableDiv_admin").show();
			 $("div#tableDiv_edit").hide();
			 $("div#tableDiv_all").hide();
		 /* }else if(isEdit=="true"){
			 $("div#tableDiv_admin").hide();
			 $("div#tableDiv_edit").show();
			 $("div#tableDiv_all").hide();
		  */}else{
			 $("div#tableDiv_admin").hide();
			 $("div#tableDiv_edit").hide();
			 $("div#tableDiv_all").show();
		 }
	 	 
		 $("div#moduleimportDiv").hide();
		 
		 var isTableRelation = $("#isTableRelation").val();
		  if(isTableRelation == 'true'){
			  $("#isTableRelation").val("false");
		 }else{
			 var params = 'moduleId='+data.rslt.obj.attr("moduleid")+'&tableId='+currentNode+'&enableRelationTab=false&isEdit='+data.rslt.obj.attr("isedit");
			 	document.location.href = "#bpm/table/showFieldEdit";
		     	 enableRelationTab = 'false';
		     	_execute('rightPanel',params); 
		 } 
 	}else if(level == 3 && (parentNode.indexOf("ListViews") != -1 || parentNode.indexOf("Recycle") != -1)){
 		if(parentNode.indexOf("ListViews") != -1){
 			if(isAdmin || isEdit=="true"){
 				$("div#listViewDiv_admin").show();
 				$("div#listViewDiv_edit").hide();
 			/* }else if(isEdit=="true"){
 				$("div#listViewDiv_admin").hide();
 				$("div#listViewDiv_edit").show();
 			 */}else{
 				$("div#listViewDiv_admin").hide();
				$("div#listViewDiv_edit").hide();
 			}
		}else if(parentNode.indexOf("Recycle") != -1){
			$("div#listViewDiv_admin").hide();
			$("div#listViewDiv_edit").hide();
		} 
 		 		
 		$("div#moduleimportDiv").hide();
 		if(parentNode.indexOf("ListViews") != -1){
 			 var showNewListView = $("#showNewListView").val();
 			 if(showNewListView == 'true'){
 				  $("#showNewListView").val("false");
 			 }else{
 				  var name =data.rslt.obj.data("name");
 				  var params = "listViewName="+name+"&title=&container=rightPanel&listViewId="+currentNode+"&moduleId="+data.rslt.obj.attr("moduleid");
 				 document.location.href = "#bpm/listView/showListViewGroupGrid";
 				 _execute('rightPanel',params);
 				 $("#module_title").html("");
			   	 $("#module_title").html(name);
 			 }
 		}else if(parentNode.indexOf("Recycle") != -1){
 			 var moduleid=data.rslt.obj.attr("moduleid");
 			 var listViewParams=[{"moduleid":moduleid}];
 			 showListViewsWithContainerAndParam("LIST_VIEW_RECYCLE", "List View Recycle", "rightPanel",listViewParams);
 		}
		
	 }else if(level == 2 && (currentNode.indexOf("AllForm")!= -1 || currentNode.indexOf("JspForms") != -1 )){
		 var isEdit = data.rslt.obj.attr("isedit");
		 if(isAdmin || isEdit=='true'){
			 if(currentNode.indexOf("JspForms") != -1){
				 $("div#jspFormDiv").show();
			 }else{
				 $("div#formDiv").show();
			 }
		 	
		 }else{
			 if(currentNode.indexOf("JspForms") != -1){
				 $("div#jspFormDiv").hide();
			 }else{
				 $("div#formDiv").hide();
			 }
			 
		 }
		 $("div#moduleimportDiv").hide();
		 
		 var isSystemModule = data.rslt.obj.attr("issystemmodule");
		 if(isAdmin){
		 	isSystemModule=false;
		 	isEdit = true;
		 }
		 var params = "";
		 if(currentNode.indexOf("JspForms") != -1){
			  params = 'moduleId='+parentNode+'&isEdit='+isEdit+'&isSystemModule='+isSystemModule+'&isJspForm='+true;
		 }else{
			  params = 'moduleId='+parentNode+'&isEdit='+isEdit+'&isSystemModule='+isSystemModule+'&isJspForm='+false;
		 }
		 
		 selectedFromModuleId=parentNode;
	 	 	document.location.href = "#bpm/form/formListForModule";
	     	_execute('rightPanel',params); 
	     	$("#module_title").html("");
	   	 	$("#module_title").html("表单列表");
	 }else if(level == 2 && currentNode.indexOf("Tables")!= -1){
		 if(isAdmin || isEdit=="true"){
			 $("div#tableDiv_admin").show();
			 $("div#tableDiv_edit").hide();
			 $("div#tableDiv_all").hide();
		 /* }else if(isEdit=="true"){
			 $("div#tableDiv_admin").hide();
			 $("div#tableDiv_edit").show();
			 $("div#tableDiv_all").hide();
		  */}else{
			 $("div#tableDiv_admin").hide();
			 $("div#tableDiv_edit").hide();
			 $("div#tableDiv_all").show();
		 }
		 
		 $("div#moduleimportDiv").hide();
		 /* document.location.href = "#/bpm/table/defaultTable";
		 _execute('rightPanel',""); */
		 
		 var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
			var nodePath = $("#module_relation_tree").jstree("get_path", $("#"+selected_nodes.attr("id")), true); 
			var nodeLevel = nodePath.length;
			var moduleId = nodePath[0];
		 
		 document.location.href = "#/bpm/table/createTable";
		 _execute('rightPanel','enableRelationTab=false&isEdit='+isEdit+'&moduleId='+moduleId);
	 }else if(level == 2 && (currentNode.indexOf("ListViews") != -1 || currentNode.indexOf("Recycle") != -1)){
		 if(currentNode.indexOf("ListViews") != -1){
	 			if(isAdmin || isEdit=="true"){
	 				$("div#listViewDiv_admin").show();
	 				$("div#listViewDiv_edit").hide();
	 			/* }else if(isEdit=="true"){
	 				$("div#listViewDiv_admin").hide();
	 				$("div#listViewDiv_edit").show();
	 			 */}else{
	 				$("div#listViewDiv_admin").hide();
					$("div#listViewDiv_edit").hide();
	 			}
			}else if(currentNode.indexOf("Recycle") != -1){
				$("div#listViewDiv_admin").hide();
				$("div#listViewDiv_edit").hide();
			} 
		 
		 $("div#moduleimportDiv").hide();
		 /* document.location.href = "#/bpm/listView/showDefaultListView";
		 _execute('rightPanel',""); */
		if(currentNode.indexOf("ListViews") != -1){
			 var params = 'isFromModule='+true;
			 document.location.href = "#/bpm/listView/showListView";
			 _execute('rightPanel',params);
			 $("#module_title").html("");
			 $("#module_title").html("新建视图");
		}else if(currentNode.indexOf("Recycle") != -1){
			 var moduleid=data.rslt.obj.attr("moduleid");
 			 var listViewParams=[{"moduleid":moduleid}];
 			 showListViewsWithContainerAndParam("LIST_VIEW_RECYCLE", "List View Recycle", "rightPanel",listViewParams, false);
 			 if(isAdmin || isEdit=="true"){
 				$('div#header_links').show();	 
 			 }else{
 				$('div#header_links').hide();
 			 }
 			 
 			 $("#module_title").html("");
		   	 $("#module_title").html("视图回收站");
		}
		
	 }else if(level == 4){
		 renderGroupSettingChildNodesForModule(e, data, parentNode, gridId);
		 if(isAdmin || isEdit=="true"){
				$("div#listViewDiv_admin").show();
				$("div#listViewDiv_edit").hide();
			/* }else if(isEdit=="true"){
				$("div#listViewDiv_admin").hide();
				$("div#listViewDiv_edit").show();
			 */}else{
				$("div#listViewDiv_admin").hide();
				$("div#listViewDiv_edit").hide();
			}
		 $("div#moduleimportDiv").hide();
	 }else if (level>4){
		 parentNode = data.rslt.obj.attr("listViewid");
		 renderGroupSettingChildNodesForModule(e, data, parentNode, gridId);
		 if(isAdmin || isEdit=="true"){
				$("div#listViewDiv_admin").show();
				$("div#listViewDiv_edit").hide();
			/* }else if(isEdit=="true"){
				$("div#listViewDiv_admin").hide();
				$("div#listViewDiv_edit").show();
			 */}else{
				$("div#listViewDiv_admin").hide();
				$("div#listViewDiv_edit").hide();
			}
		 $("div#moduleimportDiv").hide();
	 }
		
}

$("#module_relation_tree").bind('loaded.jstree', function(event, data) {
	if('${isFormShow}' == 'true'){
		$("#module_relation_tree").jstree("select_node", '#'+'${formModuleId}'+'AllForm'); 
    	}
	if('${isTableCancel}' == 'true'){
	    	$("#module_relation_tree").jstree("select_node", '#'+'${formModuleId}'+'Tables'); 
	}
});
//constructJsTree("module_relation_tree",'${moduleTree}',"rightPanel");
constructModuleJsTree("module_relation_tree",'${moduleTree}',"rightPanel");
/* $("#module_relation_tree").bind('before.jstree', function(event, data) {
	if (data.func == 'check_move') {
		console.log(event);
		return false;
	}
}); */


$(document).ready(function(){
	var height = $("#target").height();
	$("#module_form").css('height',parseInt(height)- 128);
	$("#rightPanel").css('height',parseInt(height)- 128);
	
	 $("div#listViewDiv_admin").hide();
	 $("div#listViewDiv_edit").hide();
	 
	 $("div#formDiv").hide(); 
	 $("div#jspFormDiv").hide();
	 
 	 $("div#tableDiv_admin").hide();
	 $("div#tableDiv_edit").hide();
	 $("div#tableDiv_all").hide();
	 $("div#moduleDiv_edit").hide();
	 $("div#moduleDiv_admin").hide();
	 if(!isAdmin){
		 $("span#module_mig_import").hide();
	}
	 document.location.href = "#bpm/module/createModule";
	 _execute('rightPanel','isEdit='+isAdmin);
	 $("#module_title").html("");
	 $("#module_title").html("新建模块");
	 
}); 

//====================================================table related function===========================================================
function editTableDetails(){
	var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
	var tableId=selected_nodes.attr("nodeid");
	var moduleId=selected_nodes.attr("moduleid");
		if(tableId && tableId!=undefined && tableId.indexOf("Tables") == -1){
				var params = "moduleId="+moduleId+"&tableId="+tableId+"&enableRelationTab=false";
				document.location.href = "#/bpm/table/showEditTable";
				_execute('rightPanel', params); 	
		}else{
			$.msgBox({
			    title: form.title.message,
			    content: form.msg.selectTableToEdit,
			});	
		}	
	
}

function exportTableDetails(){
	var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
	var tableId=selected_nodes.attr("nodeid");
		if(tableId && tableId!=undefined && tableId.indexOf("Tables") == -1){
			  document.location.href = "bpm/table/exportTable?tableId="+tableId;
		}else{
			$.msgBox({
			    title: form.title.message,
			    content: form.msg.selectTableToExport,
			});	
		}
}
function exportSQLTableDetails(){
	var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
	var tableId=selected_nodes.attr("nodeid");
		if(tableId && tableId!=undefined && tableId.indexOf("Tables") == -1){
			  document.location.href = "bpm/table/exportTableSql?tableId="+tableId;
		}else{
			$.msgBox({
			    title: form.title.message,
			    content: form.msg.selectTableToExport,
			});	
		}	
}

function importSQLTableDetails(){
	  openSelectDialogForFixedPosition("importSQLPopupDialog","","550","260","Import SQL");
	  document.location.href = "#bpm/table/importSQLDump";
}

function importCSVTableDetails(){
	 openSelectDialogForFixedPosition("importCSVPopupDialog","","515","260","Import CSV");
	  document.location.href = "#bpm/table/importCSVDump"; 
}


function deleteTableDetails(){
	var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
	var tableId=selected_nodes.attr("nodeid");
	var tableName=selected_nodes.attr("name");
	var isSystemModule=selected_nodes.attr("issystemmodule");
	if(isAdmin){
		isSystemModule = false;
	}
	var moduleId =  $("#moduleId").val()?$("#moduleId").val():selected_nodes.attr("moduleid");
		if(tableId && tableId!=undefined && tableId.indexOf("Tables") == -1 && moduleId){
			if(isSystemModule=="true"){
				$.msgBox({
				    title: form.title.message,
				    content: form.msg.moduleDeletePermission,
				});
			}else{
				var params="tableId="+tableId+"&tableName="+tableName+"&moduleId="+moduleId;
				var content="Are you sure you want to delete the Table: "+tableName+" ?";
				
				$.msgBox({
				    title: form.title.confirm,
				    content: content,
				    type: "confirm",
				    buttons: [{ value: "Yes" }, { value: "No" }],
				    success: function (result) {
				        if (result == "Yes") {
				        	document.location.href = "#bpm/table/deleteTableById";
				    		_execute('target',params);
				        }else{
				        	return false;
				        }
				    }
				});	
			}
		}else{
			$.msgBox({
			    title: form.title.message,
			    content: form.msg.selectTableToDelete,
			});	
		}	
}



//===============================================List view functions==================================================================
	function editListView(){
		var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
		/* var listViewId=selected_nodes.attr("nodeid");
		var moduleId=selected_nodes.attr("moduleid"); */
		var listViewId = $("#id").val()?$("#id").val():selected_nodes.attr("nodeid");
		var moduleId = $("#moduleId").val()?$("#moduleId").val():selected_nodes.attr("moduleid");
		if(listViewId && listViewId!=undefined && listViewId.indexOf("ListViews") == -1){
			var params = 'listViewId='+listViewId+'&isListViewTemplate=false&moduleId='+moduleId+'&isEdit=true&isFromModule=true';
	     	document.location.href = "#bpm/listView/showListViewDesignEdit";
	     	_execute('rightPanel',params);  	
		}else{
			$.msgBox({
			    title: form.title.message,
			    content: form.msg.seleteListViewEdit,
			});	
		}	
	}
	
	function softDeleteAndRestoreListView(isDeleted){
		var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
		var listViewId=selected_nodes.attr("nodeid");
		var moduleId=selected_nodes.attr("moduleid"); 
		var deleteTblIds = new Array();
		var isDeletedSuccessfully = false;
		var wantToDelete = false;
		
		deleteTblIds.push(listViewId);
		var content = isDeleted? "Are you sure to delete the List View?" : "Are you sure to restore the List View?";
		if(listViewId && listViewId!=undefined && listViewId.indexOf("ListViews") == -1){
			var isSystemModule=selected_nodes.attr("issystemmodule");
			if(isSystemModule=="true" && !isAdmin){
				$.msgBox({
				    title: form.title.message,
				    content: form.msg.deleteError,
				});
			}else{
				$.msgBox({
				    title: form.title.confirm,
				    content: content,
				    type: "confirm",
				    buttons: [{ value: "Yes" }, { value: "No" }],
				    success: function (result) {
				     if (result == "Yes") {
				    	 wantToDelete=true;
				        	 $.ajax({	
								type: 'POST',
								async: false,
								url : "/bpm/listView/softDeleteTableDataAndRestore?deleteTblIds="+JSON.stringify(deleteTblIds)+"&tableName=ETEC_RE_LIST_VIEW&columnName=id&isDelete="+isDeleted,
								success : function(response) {
									if(response.success){
										isDeletedSuccessfully = true;
										deletedMsg = response.success;
										getAllModuleAsJSON('', '', '', false);
									}else{
										isDeletedSuccessfully = false;
										deletedMsg = response.error;
									}
								}
							}); 
				        }
				    },
				    afterClose: function() {
				    	if(isDeletedSuccessfully){
				    		var msgCon = isDeleted? form.msg.deletedListView : form.msg.restoredListView;
				    		validateMessageBox(form.title.success, msgCon , "success");
				    		$("#module_relation_tree").jstree("select_node", '#'+moduleId+'ListViews');
				    	}else{
				    		if(wantToDelete){
								var msgCon = isDeleted? form.msg.erroeDeleteListView : form.msg.errorRestoreListView;
					    		validateMessageBox(form.title.error, msgCon , "error");	
				    		}
				    	}
				    }
				});	
			}
		}else{
			$.msgBox({
			    title: form.title.message,
			    content: form.msg.listViewDelete,
			});	
		}
		
	}
	
	
	function deleteListView(){
		var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
		var listViewId=selected_nodes.attr("nodeid");
		var listViewName=selected_nodes.attr("name");
		if( listViewId && listViewId!=undefined && listViewId.indexOf("ListViews") == -1){
		var param="listViewId="+listViewId;
		var content="Are you sure you want to delete this List View : "+listViewName+" ?";
		var contentResponse="";
		var isDeleted=false;
		var isToDelete=true;
	$.msgBox({
	    title: form.title.confirm,
	    content: content,
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	     if (result == "Yes") {
	        	 $.ajax({	
				type: 'POST',
				async: false,
				url : '/bpm/listView/deleteListViewDetails',
				data: param,
				success : function(response) {
					if(response.success){
						contentResponse=response.success;
						isDeleted=true;
					}else{
						contentResponse=response.error;
					}
				}
			}); 
	        }else{
	        	isToDelete=false;
	        }
	    },afterClose:function (){
		    	if(isToDelete){
		    		deleteListViewDetails(contentResponse,isDeleted);
		    	}
		}
	    
	});
		}else{
			$.msgBox({
			    title: form.title.message,
			    content: form.msg.listViewDelete,
			});	
		}
}

function deleteListViewDetails(contentResponse,isDeleted){
	if(isDeleted){
		$.msgBox({
			title: form.title.success,
			content: contentResponse,
		        type: "success",
		        afterClose:function (){
		    		document.location.href = "#bpm/module/manageModuleRelation";
				_execute('target','');
			}
		});
	}else{
		$.msgBox({
			title: form.title.error,
			content: contentResponse,
		        type: "error",
			afterClose:function (){
		    		document.location.href = "#bpm/module/manageModuleRelation";
				_execute('target','');
			}
		});
	}
}



//==============================================Module functions=======================================================================
	
	
	function deleteModule(){
		var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
	
	var moduleName=selected_nodes.data("name");
	var isSystemModule=selected_nodes.attr("issystemmodule");
	if(moduleName && moduleName!=undefined){
		if(isSystemModule=="true"){
			$.msgBox({
			    title: form.title.message,
			    content: form.msg.errorModuleDelete,
			});
		}else{
			var moduleId=document.getElementById("id").value;
			var params="moduleId="+moduleId+"&moduleName="+moduleName+"&screenName=managModules";
			var content="Are you sure you want to delete the Module: "+moduleName+" ?";
			
			$.msgBox({
			    title: form.title.confirm,
			    content: content,
			    type: "confirm",
			    buttons: [{ value: "Yes" }, { value: "No" }],
			    success: function (result) {
			        if (result == "Yes") {
			        	document.location.href = "#bpm/module/deleteModule";
			    		_execute('target',params);
			    		//refreshTableJSTree('table_tree');
			        }else{
			        	return false;
			        }
			    }
			});	
		}
	}else{
		$.msgBox({
		    title: form.title.message,
		    content: form.msg.selectModuleDelete,
		});	
	}
}

	function moduleExportToCSV(){
		var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
		var moduleId=selected_nodes.attr("nodeid");
		if(moduleId){
		 document.location.href = "bpm/module/moduelCSVExport?moduleId="+moduleId;
		}else{
			$.msgBox({
			    title: form.title.message,
			    content: form.msg.exportModule,
			});
		}
	}
	
	function moduleMigrationExport(){
		var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
		var moduleId=selected_nodes.attr("nodeid");
		if(moduleId){
		 document.location.href = "bpm/module/moduleMigrationExport?moduleId="+moduleId;
		}else{
			$.msgBox({
			    title: form.title.message,
			    content: form.msg.exportModule,
			});
		}
	}
	
	function moduleMigrationImport(){
		openSelectDialogForFixedPosition("popupDialog","","550","200","Import Module");
		  document.location.href = "#bpm/module/showModuleImport";
	}

	function orderingModule(currentNodeName,replaceNodeName,currentNodeOrder,replaceNodeOrder){
		var params="currentNodeOrder="+currentNodeOrder+"&replaceNodeOrder="+replaceNodeOrder;
		var content="Are you sure you want to apply the order changes for "+currentNodeName+" ?";
		$.msgBox({
		    title: form.title.confirm,
		    content: content,
		    type: "confirm",
		    buttons: [{ value: "Yes" }, { value: "No" }],
		    success: function (result) {
		        if (result == "Yes") {
		        	document.location.href = "#bpm/module/changeModuleOrder";
		    		_execute('target',params);
		        }else{
		        	 document.location.href = "#bpm/module/manageModuleRelation";
						_execute('target','');
		        }
		    }
		});	
	}
	
	function copyTableToModule(tableId,moduleId,tableName,moduleName,currentModuleName){
		var params="tableId="+tableId+"&moduleId="+moduleId+"&tableName="+tableName+"&moduleName="+moduleName+"&currentModuleName="+currentModuleName;
		var content="Are you sure you want to copy the table : "+tableName+" to Module :"+moduleName+" ?";
		$.msgBox({
		    title: form.title.confirm,
		    content: content,
		    type: "confirm",
		    buttons: [{ value: "Yes" }, { value: "No" }],
		    success: function (result) {
		        if (result == "Yes") {
		        	document.location.href = "#bpm/table/copyTableToModule";
		    		_execute('target',params);
		        }else{
		        	 document.location.href = "#bpm/module/manageModuleRelation";
						_execute('target','');
		        }
		    }
		});	
	}
	
	function copyListViewToModule(listViewId,moduleId,listViewName,moduleName,currentModuleName){
		var params="listViewId="+listViewId+"&moduleId="+moduleId+"&listViewName="+listViewName+"&moduleName="+moduleName+"&currentModuleName="+currentModuleName;
		var content="Are you sure you want to copy the List View  : "+listViewName+" to Module :"+moduleName+" ?";
		$.msgBox({
		    title: form.title.confirm,
		    content: content,
		    type: "confirm",
		    buttons: [{ value: "Yes" }, { value: "No" }],
		    success: function (result) {
		        if (result == "Yes") {
		        	document.location.href = "#bpm/listView/copyListViewToModule";
		    		_execute('target',params);
		    	}else{
		        	 document.location.href = "#bpm/module/manageModuleRelation";
						_execute('target','');
		        }
		    }
		});	
	}
	
	function createTable(){
		var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
		var nodePath = $("#module_relation_tree").jstree("get_path", $("#"+selected_nodes.attr("id")), true); 
		var nodeLevel = nodePath.length;
		var moduleId = nodePath[0];
		
		document.location.href = "#/bpm/table/createTable";
		_execute('rightPanel','enableRelationTab=false&isEdit=true&moduleId='+moduleId);
		 
	}
	
	function createJspForm(){
		var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
		var nodePath = $("#module_relation_tree").jstree("get_path", $("#"+selected_nodes.attr("id")), true); 
		var nodeLevel = nodePath.length;
		var moduleId = nodePath[0];
		
		document.location.href = "#/bpm/form/showJspUpoladForm";
		_execute('rightPanel','moduleId='+moduleId);
		$("#module_title").html("");
		$("#module_title").html("JSP表单");
	}
	
	
</script>

<div id="parent_container">
	<div class="page-header">
		<div class="pull-left">
			<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2><fmt:message key="admin.module.list"/></h2>
		</div>
		</div>
		
		<div id="tableDiv_admin">
			<span id="module_table_delete"  class="floatRight" style="margin-top:-5px"><a class="padding3" id="deleteTable" href="javascript:void(0);" data-role="button" data-theme="b" style="text-decoration: underline;"  onClick="deleteTableDetails();"  data-icon=""><button class="btn"><fmt:message key="table.button.delete"/></button></a></span>
			<span class="floatRight" style="margin-top:-5px"><a class="padding3" id="importSql" href="javascript:void(0);" data-role="button" data-theme="b" style="text-decoration: underline;"  onClick="importSQLTableDetails();"  data-icon=""><button class="btn"><fmt:message key="table.button.import.sql"/></button></a></span>
			<span class="floatRight" style="margin-top:-5px"><a class="padding3" id="importCsv" href="javascript:void(0);" data-role="button" data-theme="b" style="text-decoration: underline;"  onClick="importCSVTableDetails();"  data-icon=""><button class="btn"><fmt:message key="table.button.import.csv"/></button></a></span>
			<span class="floatRight" style="margin-top:-5px"><a class="padding3" id="exportSql" href="javascript:void(0);" data-role="button" data-theme="b" style="text-decoration: underline;"  onClick="exportSQLTableDetails();"  data-icon=""><button class="btn"><fmt:message key="table.button.export.sql"/></button></a></span>
			<span class="floatRight" style="margin-top:-5px"><a class="padding3" id="export" href="javascript:void(0);" data-role="button" data-theme="b" style="text-decoration: underline;"  onClick="exportTableDetails();"  data-icon=""><button class="btn"><fmt:message key="table.button.export"/></button></a></span>
			
			<span class="floatRight" style="margin-top:-5px"><a class="padding3" id="exportMultiTableSql" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="exportMultiTableDetails();" style="text-decoration: underline;" data-icon=""><button class="btn"><fmt:message key="table.multi.export.sql"/></button></a></span>
			
			<span id="module_table_edit" class="floatRight" style="margin-top:-5px"><a class="padding3" id="editTable" href="javascript:void(0);" data-role="button" data-theme="b" style="text-decoration: underline;"  onClick="editTableDetails();"  data-icon=""><button class="btn"><fmt:message key="table.button.edit"/></button></a></span>
			<span id="module_table_create" class="floatRight" style="margin-top:-5px"><a class="padding3" id="createTable" href="javascript:void(0);" data-role="button" data-theme="b" style="text-decoration: underline;"  onClick="createTable();"  data-icon=""><button class="btn"><fmt:message key="button.createNew"/></button></a></span>
		</div>
			
		<div id="tableDiv_edit">
			<span class="floatRight" style="margin-top:-5px"><a class="padding3" id="importSql" href="javascript:void(0);" data-role="button" data-theme="b" style="text-decoration: underline;"   onClick="importSQLTableDetails();"  data-icon=""><button class="btn"><fmt:message key="table.button.import.sql"/></button></a></span>
			<span class="floatRight" style="margin-top:-5px"><a class="padding3" id="importCsv" href="javascript:void(0);" data-role="button" data-theme="b" style="text-decoration: underline;"  onClick="importCSVTableDetails();"  data-icon=""><button class="btn"><fmt:message key="table.button.import.csv"/></button></a></span>
			<span class="floatRight" style="margin-top:-5px"><a class="padding3" id="exportSql" href="javascript:void(0);" data-role="button" data-theme="b" style="text-decoration: underline;"  onClick="exportSQLTableDetails();"  data-icon=""><button class="btn"><fmt:message key="table.button.export.sql"/></button></a></span>
			<span class="floatRight" style="margin-top:-5px"><a class="padding3" id="export" href="javascript:void(0);" data-role="button" data-theme="b" style="text-decoration: underline;"  onClick="exportTableDetails();"  data-icon=""><button class="btn"><fmt:message key="table.button.export"/></button></a></span>
			<span id="module_table_edit" class="floatRight" style="margin-top:-5px"><a class="padding3" id="editTable" href="javascript:void(0);" style="text-decoration: underline;"  data-role="button" data-theme="b"  onClick="editTableDetails();"  data-icon=""><fmt:message key="table.button.edit"/></a></span>
		</div>
			
		<div id="tableDiv_all">
			<%-- <span class="floatRight" style="margin-top:-5px"><a class="padding3" id="importSql" href="javascript:void(0);" data-role="button" data-theme="b" style="text-decoration: underline;"  onClick="importSQLTableDetails();"  data-icon=><fmt:message key="table.button.import.sql"/></a></span>
			<span class="floatRight" style="margin-top:-5px"><a class="padding3" id="importCsv" href="javascript:void(0);" data-role="button" data-theme="b" style="text-decoration: underline;"  onClick="importCSVTableDetails();"  data-icon=""><fmt:message key="table.button.import.csv"/></a></span>
			<span class="floatRight" style="margin-top:-5px"><a class="padding3" id="exportSql" href="javascript:void(0);" data-role="button" data-theme="b" style="text-decoration: underline;"  onClick="exportSQLTableDetails();"  data-icon=""><fmt:message key="table.button.export.sql"/></a></span>
			<span class="floatRight" style="margin-top:-5px"><a class="padding3" id="export" href="javascript:void(0);" data-role="button" data-theme="b" style="text-decoration: underline;"  onClick="exportTableDetails();"  data-icon=""><fmt:message key="table.button.export"/></a></span> --%>
		</div>
			
			
		<div id="listViewDiv_admin">
			<span id="module_listView_delete" class="floatRight" style="margin-top:-5px"><a class="padding3" style="text-decoration: underline;"  id="deleteListView" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="softDeleteAndRestoreListView(true);"  data-icon=""><button class="btn"><fmt:message key="table.button.delete"/></button></a></span>
			<!--<span class="floatRight" style="margin-top:-5px"><a class="padding3" style="text-decoration: underline;"  id="templateGrid" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="showListViewsWithContainer('TEMPLATE_LIST_VIEW','','rightPanel');"  data-icon=""><button class="btn"><fmt:message key="listView.button.template.list"/></button></a></span>
			--><span id="module_listView_edit" class="floatRight" style="margin-top:-5px"><a class="padding3" style="text-decoration: underline;"  id="editListView" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="editListView()"  data-icon=""><button class="btn"><fmt:message key="table.button.edit"/></button></a></span>
			<span id="module_listView_create" class="floatRight" style="margin-top:-5px"><a class="padding3" style="text-decoration: underline;"  id="createListView" href="#/bpm/listView/showListView?isFromModule=true" data-role="button" data-theme="b"  onClick="_execute('rightPanel','');"  data-icon=""><button class="btn"><fmt:message key="button.createNew"/></button></a></span>
		</div>
			
			<div id="listViewDiv_edit">
			<!--<span class="floatRight" style="margin-top:-5px"><a class="padding3" style="text-decoration: underline;"  id="templateGrid" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="showListViewsWithContainer('TEMPLATE_LIST_VIEW','','rightPanel');"  data-icon=""><button class="btn"><fmt:message key="listView.button.template.list"/></button></a></span>
			--><span id="module_listView_edit" class="floatRight" style="margin-top:-5px"><a class="padding3" style="text-decoration: underline;"  id="editListView" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="editListView()"  data-icon=""><button class="btn"><fmt:message key="table.button.edit"/></button></a></span>
			</div>
			
		<div id="formDiv">
			<span class="floatRight" style="margin-top:-5px"><a class="padding3" style="text-decoration: underline;" id="goBackToFormListFromFormVersion" onClick="goBackToFormListFromFormVersion();" href="javascript:void(0);" data-role="button" data-theme="b" data-icon=""><button class="btn"><fmt:message key="button.back"/></button></a></span>
			<span class="floatRight" style="margin-top:-5px"> <a class="padding3" style="text-decoration: underline;" id="deleteForms" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteForms();"  data-icon=""><button class="btn"><fmt:message key="form.deleteForms"/></button></a></span>
		    <span class="floatRight" style="margin-top:-5px"><a class="padding3" style="text-decoration: underline;" id="copyForms" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="copyForms()"  data-icon=""><button class="btn"><fmt:message key="form.copyForm"/></button></a></span>
		    <span class="floatRight" style="margin-top:-5px"><a class="padding3" style="text-decoration: underline;" id="manageTemplateForms" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="manageTemplateForms()"  data-icon=""><button class="btn"><fmt:message key="form.palette.templates"/></button></a></span>
		    <span class="floatRight" style="margin-top:-5px"><a class="padding3" style="text-decoration: underline;"  id="createForm" href="#bpm/form/createForm" data-role="button" data-theme="b"  onClick="_execute('target','');"  data-icon=""><button class="btn"><fmt:message key="button.createNew"/></button></a></span>
		</div>  
		
		<div id="jspFormDiv">
			<span class="floatRight" style="margin-top:-5px"><a class="padding3" style="text-decoration: underline;"  id="createJspForm" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="createJspForm()"  data-icon=""><button class="btn"><fmt:message key="button.createNew"/></button></a></span>
		</div>
			
		<div id="moduleimportDiv">
			<span class="floatRight" style="margin-top:-5px"><a onclick="_execute('target','');" class="padding3" href="#bpm/module/manageModuleRelation" ><img src="/images/refresh.png"/></a></span>
			<span id="module_create_span" class="floatRight" style="margin-top:-5px"><select id="searchType" style="width:100px;"><option value="Table">Table</option><option  value="From">Form</option></select>
				<input type="text" id="searchModuleDatas" class="small" name="searchModuleDatas" autocomplete="off" onkeyup="getModuleData(event);" placeholder="Search" list="searchModuleDataList"/>
				<button type="button" class="btn btn-primary" name="Search" onclick="selectTreeInModule();">Search</button>
		 		<datalist id="searchModuleDataList"/>
		 	</span>
			<span id="module_mig_import" class="floatRight" style="margin-top:-5px"><a title="Import" style="text-decoration: underline;" class="padding3" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="moduleMigrationImport()"  data-icon=""><button class="btn"><fmt:message key="module.migration.import"/></button></a></span>
			<span id="module_mig_export" class="floatRight" style="margin-top:-5px"><a title="Export" style="text-decoration: underline;" class="padding3" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="moduleMigrationExport()"  data-icon=""><button class="btn"><fmt:message key="module.migration.export"/></button></a></span>
		</div>
		
		<div id="moduleDiv_edit">
			<span id="module_delete_span" style="text-decoration: underline;margin-top:-5px" class="floatRight"><a class="padding3" id="deleteModule" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteModule();"  data-icon=""><button class="btn"><fmt:message key="table.button.delete"/></button></a></span>
			<!-- <span class="floatRight fontSize14 pad-R10 pad-T10"><a onclick="orderingModule('down');"><img src="/images/move_down_small.png"/></a></span>
			<span class="floatRight fontSize14 pad-R10 pad-T10"><a onclick="orderingModule('up');"><img src="/images/go_up_small.png"/></a></span> -->
		</div>
			
		<div id="moduleDiv_admin">
			<%-- <span class="floatRight" style="margin-top:-5px"><a title="Import" href="javascript:void(0);" class="padding3" data-role="button" data-theme="b"  onClick="importModule()"  data-icon=""><fmt:message key="button.import"/></a></span>
			<span class="floatRight" style="margin-top:-5px"><a title="Export" href="javascript:void(0);" class="padding3" data-role="button" data-theme="b"  onClick="moduleExportToCSV()"  data-icon=""><fmt:message key="button.export"/></a></span>
			<span id="module_mig_import" class="floatRight" style="margin-top:-5px"><a title="Import" class="padding3" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="moduleMigrationImport()"  data-icon=""><fmt:message key="module.migration.import"/></a></span> --%>
			<span id="module_create_span" class="floatRight" style="margin-top:-5px"><a title="Create New" style="text-decoration: underline;" class="padding3" data-icon="" onclick="_execute('rightPanel','isEdit=true');" data-theme="b" data-role="button" href="#bpm/module/createModule"><button class="btn"><fmt:message key="button.createNew"/></button></a></span>
		</div>

	<div class="row-fluid">
		<div class="span12">
			<div class="span3">
				<div class="widget">
					<div class="widget-header">
						<div class="title">
							<span class="fs1" aria-hidden="true"></span><fmt:message key="form.modules"/>
						</div>
					</div>
					<div class="widget-body">
						<div id="module_form" class="overflow-all">
							<div id="module_relation_tree" ></div>
						</div>
						<div class="clearfix"></div>
					</div>
				</div>
			</div>
			<div class="span9">
				<div class="widget">
					<div class="widget-header">
						<div class="title">
							<span class="fs1" aria-hidden="true"></span><div id="module_title"><fmt:message key="form.createModule"/></div>
						</div>
						<div id="header_links" align="right"></div>
					</div>
					<div class="widget-body">
						<div id="rightPanel" class="overflow-all">
							<%@ include file="/WEB-INF/pages/module/createModule.jsp" %>
						</div>
						<div class="clearfix"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="screenName" value="managModules"/>
