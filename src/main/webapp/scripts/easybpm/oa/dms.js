//method for edit grid data
function _showEditDocumentForm(cellValue, options, rawObject){
	 return '<a id="editdocument" href="#bpm/dms/documentForm" data-role="button" data-icon="" data-theme="b" onclick="_execute(\'target\',\'id='+rawObject.id+'\');"><b>'+rawObject.name+'</b></a>';
}

function _showViewDocumentForm(cellValue, options, rawObject){
	 return '<a id="viewdocument" href="#bpm/dms/viewDocumentForm" data-role="button" data-icon="" data-theme="b" onclick="_execute(\'user-grid\',\'id='+rawObject.id+'\');"><b>'+rawObject.name+'</b></a>';
}

function deleteDocumentForm(id){
	params = 'id='+id;
	$.msgBox({
	    title: form.title.confirm,
	    content: form.msg.deleteConfirm,
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	document.location.href = "#bpm/dms/deleteDocumentForm";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}

function createFolderDocumentForm(){
	var jsTree = $.jstree._focused().get_selected();
	var folderId = jsTree.attr("id");
	var canEdit = jsTree.attr("canEdit");
	var isRoot = jsTree.attr("isRoot");
		if(folderId == undefined || folderId == null || folderId == ''){
			validateMessageBox(form.title.message, form.msg.selectFolder , false);
			return false;
		}
		if(isRoot == "true" || canEdit == "true" || jsTree.attr("cancreate") == "true") {
			_execute('user-grid','folderId='+folderId);
			return true;
		} else if(canEdit == "false") {
			validateMessageBox(form.title.noPermission, form.msg.noWritePermission , false);
			return false;
		}
}

function downloadDocument(id){
    document.location.href = "bpm/dms/downloadDocument?id="+id;
}

function downloadDocumentnotice(id){
    document.location.href = "bpm/admin/downloadNoticeDoc?id="+id;
}

function viewDocument(id,name,documentFormId){
	
	clearPreviousPopup();
	//document.location.href = "#bpm/dms/viewDocument";
	_execute('doc_view_dialog','id='+id);
/*	$myDialog = $("#doc_view_dialog");
	$myDialog.dialog({
                       width: '50%',
                       height: 'auto',
                       modal: true,
                       position: ['center', 25],
                       title: name,
                       open: function (event, ui) {
                    	   $('body').css('overflow','hidden');
                       },                     
                       close: function() {
							var docPath=document.getElementById("docName").value;
							$.ajax({
								url: 'bpm/dms/deleteTmpFile',
						        type: 'GET',
						        data: 'docPath='+docPath,
						        async : false,
								success : function(response) {
								},
								error : function(response){
								}
							});
							
						}

	             });
	 $myDialog.dialog("open");*/
	 
	 
	var url = "bpm/dms/viewDocument?id="+id+"&documentFormId="+documentFormId;
	// alert(url);
	var win=window.open(url, '_blank');
	win.focus();
	    
  //  document.location.href = "bpm/dms/viewDocument?id="+id;
}


//get menus for dms organization tree right click
function getMangeDmsMenus(node){
	var data = $(node);
	var currentNode = data.attr("id");
	var disabledCreate = false;
	var disabledEdit = false;
	if(data.attr("isRoot") == 'true'){
		disabledEdit = true;
		disabledDelete = true;
	}
	if(data.attr("canEdit") != 'true'){
			disabledEdit = true;
	}
	if(data.attr("canCreate") == 'false') {
		disabledCreate = true;
	}

   var items = {
      "Create Folder" : {
		  "label"     : "新建文件夹",
		  "icon"      : "create",
		  "_disabled" : disabledCreate,
		  "action"    : function(){
	    	 				createFolder(currentNode);
		  				},
      },
	  "Edit Folder" : {
		  "label"  	: "编辑文件夹",
		  "icon"   	: "edit",
		  "_disabled": disabledEdit,
		  "action"	: function(){
	    	 				editFolder(currentNode);
	    	 			},
	  },
	  "Delete Folder" : {
		  "label"  	: "删除文件夹",
		  "icon"   	: "delete",
		  "_disabled": disabledEdit,
		  "action" 	: function(){
        	 				deleteFolder(currentNode);
        	 			},
	      }
	   };
	
	   return items;
}

//create dms organization tree folder
function createFolder(currentNode){
	document.location.href = "#bpm/dms/showFolderPage";
	_execute('user-grid','parentNodeId='+currentNode);
}

//edit dms organization tree folder
function editFolder(currentNode){
	document.location.href = "#bpm/dms/showFolderPage";
	_execute('user-grid','id='+currentNode);
}

//delete dms organization tree folder
function deleteFolder(currentNode){
	$.msgBox({
	    title: form.title.confirm,
	    content: form.msg.deleteConfirm,
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	document.location.href = "#bpm/dms/deleteFolder";
	        	_execute('user-grid','id='+currentNode);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}

function renderFolderChildNodes(e,data){
	var currentNode = data.rslt.obj.attr("id");
	var selected_nodes = $("#folderTree").jstree("get_selected", null, true); 
	var tree = $.jstree._reference($("#folderTree")); 
	var children = tree._get_children(selected_nodes); 
	var children_length = data.inst._get_children(data.rslt.obj).length;
	if ( children_length == 0 ) {
		var childNodes = getFolderChildNodes(currentNode);
		if( childNodes != null && childNodes.length > 0 ){
			for(var i=0; i<childNodes.length; i++) {
				if( (childNodes[i].attr.canEdit) || (childNodes[i].attr.canRead)) { 
					$("#folderTree").jstree("create", $("#"+currentNode) , false, childNodes[i] , false, true);					
				} 				
			}
		}else{
			$("#"+currentNode).removeClass('jstree-open');
	    	$("#"+currentNode).removeClass('jstree-closed');
	    	$("#"+currentNode).addClass('jstree-leaf');
		}
	}
	getFolderDocument(currentNode);
}

//method to get user child nodes of parent node
function getFolderChildNodes(currentNode){ 
	var data = [];
	$.ajax({
		url: 'bpm/dms/getFolderChildNodes',
        type: 'GET',
        data: 'currentNode='+currentNode,
        dataType: 'json',
        async : false,
		success : function(response) {
				data=response;
		},
		error : function(response){
				data=response;
				validateMessageBox(form.title.message, form.msg.errorInGettingChildNodes , false);
		}
	});
	return data;
}

//get folder documents
function getFolderDocument(folderId){
	$.ajax({
		url: 'bpm/dms/getFolderDocumentGrid',
        type: 'GET',
        dataType: 'html',
        cache: false,
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		async: false,
		data: 'folderId='+folderId,
		success : function(response) {
			if(response.length > 0){
				$("#user-grid").html(response);
				$('#grid_header_links').html($('#header_links').html());
			}
			}
	});
}

function documentGridWidth(){
	var width = $("#user-grid").width();
    var winWidth = parseInt(width)-5;
    return winWidth;
}

function documentGridHeight(){
	var newHeight = $("#user-grid").height();
	return parseInt(newHeight)-95;
}

function hideDocumentPermissionFields(){
/*	$('#canDelete').hide();
	$('#canPrint').hide();
	$('#canDownload').hide();
	$('#canDeleteLabel').hide();
	$('#canPrintLabel').hide();
	$('#canDownloadLabel').hide();*/
}

function hideFolderPermissionFields(){
	//	$('#canRead').hide();
		$('#canWrite').hide();
	//	$('#canReadLabel').hide();
		$('#canWriteLabel').hide();
	}

function userPermissionGridWidth() {
	var width = $("#role-permission-grid").width();
	var winWidth = parseInt(width)-5;
	return winWidth;
}

function userPermissionGridHeight() {
	var height = $("#role-permission-grid").height();
	var winHeight = parseInt(height)-25;
	return winHeight;
}

function changeHeaderLink(gridType){
	if(gridType == 'user'){
		$('#gview__USER_PERMISSION_LIST').find("#grid_header_links").html($('#user_permission_create_links').html());
	} else if(gridType == 'role'){
		$('#gview__ROLE_PERMISSION_LIST').find('#grid_header_links').html($('#role_permission_create_links').html());
	} else if(gridType == 'group'){
		$('#gview__GROUP_PERMISSION_LIST').find('#grid_header_links').html($('#group_permission_create_links').html());
	}
}

function showDmsUserPermission(from){
	var folderId = $('#id').val();
	_execute('popupDialog','from='+from+'&folderId='+folderId);
	$myDialog = $("#popupDialog");
	$myDialog.dialog({
                       width: '800px',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 150],
                       title: 'User Permission'
	             });
	 $myDialog.dialog("open");
}

function showDmsRolePermission(from){
	var folderId = $('#id').val();
	_execute('popupDialog','from='+from+'&folderId='+folderId);
	$myDialog = $("#popupDialog");
	$myDialog.dialog({
                       width: '800px',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 150],
                       title: 'Permission'
	             });
	 $myDialog.dialog("open");
}

function showDmsGroupPermission(from){
	var folderId = $('#id').val();
	_execute('popupDialog','from='+from+'&folderId='+folderId);
	$myDialog = $("#popupDialog");
	$myDialog.dialog({
                       width: '800px',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 150],
                       title: 'Group Permission'
	             });
	 $myDialog.dialog("open");
}

function _editDmsUserPermission(cellValue, options, rawObject){
	var from = $('#folders').val();
	if(from == "folder"){
		from = "folder";
	}else {
		from = "document";
	}
	return '<a id="editUserPermission" href="#bpm/dms/showDmsUserPermission" data-role="button" data-icon="" data-theme="b" onclick="editDmsUserPermission(\''+from+'\',\''+rawObject.id+'\');"><b>'+rawObject.userName+'</b></a>';
}

function editDmsUserPermission(from,permissionId) {
	_execute('popupDialog','from='+from+'&id='+permissionId);
	$myDialog = $("#popupDialog");
	$myDialog.dialog({
                       width: '800px',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       position: [($(window).width() / 3), 150],
                       title: 'User Permission'
	             });
	 $myDialog.dialog("open");
}


function _editDmsRolePermission(cellValue, options, rawObject){
	var from = $('#folders').val();
	if(from == "folder"){
		from = "folder";
	}else {
		from = "document";
	}
	if(rawObject.userFullName == null) {
		return '<a id="editRolePermission" href="#bpm/dms/showDmsRolePermission" data-role="button" data-icon="" data-theme="b" onclick="editDmsRolePermission(\''+from+'\',\''+rawObject.id+'\');"><b>'+rawObject.name+'</b></a>';
	} else {
		return '<a id="editRolePermission" href="#bpm/dms/showDmsRolePermission" data-role="button" data-icon="" data-theme="b" onclick="editDmsRolePermission(\''+from+'\',\''+rawObject.id+'\');"><b>'+rawObject.userFullName+'</b></a>';
	}
}

function editDmsRolePermission(from,permissionId) {
	_execute('popupDialog','from='+from+'&id='+permissionId);
	$myDialog = $("#popupDialog");
	$myDialog.dialog({
                      width: '800px',
                      height: 'auto',
                      top: '10px',
                      modal: true,
                      position: [($(window).width() / 3), 150],
                      title: 'Permission'
	             });
	 $myDialog.dialog("open");
}

function _editDmsGroupPermission(cellValue, options, rawObject){
	var from = $('#folders').val();
	if(from == "folder"){
		from = "folder";
	}else {
		from = "document";
	}
	return '<a id="editGroupPermission" href="#bpm/dms/showDmsGroupPermission" data-role="button" data-icon="" data-theme="b" onclick="editDmsGroupPermission(\''+from+'\',\''+rawObject.id+'\');"><b>'+rawObject.groupName+'</b></a>';
}

function editDmsGroupPermission(from,permissionId) {
	_execute('popupDialog','from='+from+'&id='+permissionId);
	$myDialog = $("#popupDialog");
	$myDialog.dialog({
                      width: '800px',
                      height: 'auto',
                      top: '10px',
                      modal: true,
                      position: [($(window).width() / 3), 150],
                      title: 'Group Permission'
	             });
	 $myDialog.dialog("open");
}

//get user permission grid
function getUserPermisisonGrid(from){
	var id = $('#id').val(); 
	$.ajax({
		url: 'bpm/dms/getUserPermisisonGrid',
        type: 'GET',
        dataType: 'html',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		async: false,
		data: 'from='+from+'&id='+id,
		success : function(response) {
			if(response.length > 0){
				$("#user-permission-grid").html(response);
				$('#gview__USER_PERMISSION_LIST').find("#grid_header_links").html($('#user_permission_create_links').html());
			}
			}
	});
}

//get role permission grid
function getRolePermisisonGrid(from){
	var id = $('#id').val();
	$.ajax({
		url: 'bpm/dms/getRolePermisisonGrid',
        type: 'GET',
        dataType: 'html',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		async: false,
		data: 'from='+from+'&id='+id,
		success : function(response) {
			if(response.length > 0){
				$("#role-permission-grid").html(response);
				$('#gview__ROLE_PERMISSION_LIST').find('#grid_header_links').html($('#role_permission_create_links').html());
			}
			}
	});
}

//get group permission grid
function getGroupPermisisonGrid(from){
	var id = $('#id').val();
	$.ajax({
		url: 'bpm/dms/getGroupPermisisonGrid',
        type: 'GET',
        dataType: 'html',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		async: false,
		data: 'from='+from+'&id='+id,
		success : function(response) {
			if(response.length > 0){
				$("#group-permission-grid").html(response);
				$('#gview__GROUP_PERMISSION_LIST').find('#grid_header_links').html($('#group_permission_create_links').html());
			}
			}
	});
}

function validateDmsUserPermission(form){
	var users = $('#users').val();
	
	if(users != null && users != ''){
		//checkBoxValidation();
		return true;
	} else {
		validateMessageBox(form.title.message, form.msg.userMandatory , false);
		return false;
	}
}

function validateDmsRolePermission(form){
	var roles = $('#roles').val();
	if(roles != null && roles != ''){
		//checkBoxValidation();
		return true;
	} else {
		validateMessageBox(form.title.message, form.msg.roleMandatory , false);
		return false;
	}
}

function checkPermissionFieldEmpty(folderForm){


	if($('#checkedValue').val() === 'USER'){
	 	var users = $('#users').val();
		var userFullName = $('#usersFullName').val();
		var userId = $('#users').val();
		$('#users_FullName').val(userId+"_"+userFullName);
	 	if(users != null && users != ''){
	 		checkBoxValidation();
		return true;
		} else {
			validateMessageBox(form.title.message, form.msg.userMandatory , false);
			return false;
		}
	 }else	 if($('#checkedValue').val() === 'ROLE'){
	 	var roles = $('#getRoles').val();
	 	//alert(users);
	 	if(roles != null && roles != ''){
	 		checkBoxValidation();
		return true;
		} else {
			validateMessageBox(form.title.message, form.msg.roleMandatory , false);
			return false;
		}
	 }else  if($('#checkedValue').val() === 'DEPARTMENT'){
	 	var departments = $('#getDepartments').val();
	 	if(departments != null && departments != ''){
	 		checkBoxValidation();
		return true;
		} else {
			validateMessageBox(form.title.message, form.msg.departmentMandatory , false);
			return false;
		}
		
	 }else if($('#checkedValue').val() === 'GROUP'){
	 	var groups = $('#getGroups').val();
	 	if(groups != null && groups != ''){
	 		checkBoxValidation();
		return true;
		} else {
			validateMessageBox(form.title.message, form.msg.groupMandatory , false);
			return false;
		}
	 }else{
		validateMessageBox(form.title.message, form.msg.permissionMandatory , false);
		return false;
	}
	 
	
}
function validateDmsGroupPermission(form){
	var groups = $('#groups').val();
	if(groups != null && groups != ''){
		//checkBoxValidation();
		return true;
	} else {
		validateMessageBox(form.title.message, form.msg.groupMandatory , false);
		return false;
	}
}


function deletePermission(from,at){

	if(from == 'user'){
		var rowid = $("#_USER_PERMISSION_LIST").jqGrid('getGridParam','selarrrow');
	}else if(from == 'role'){
		var rowid = $("#_ROLE_PERMISSION_LIST").jqGrid('getGridParam','selarrrow');
	}else if(from == 'group'){
		var rowid = $("#_GROUP_PERMISSION_LIST").jqGrid('getGridParam','selarrrow');
	}
	if(rowid.length !=0 ){
			params = 'ids='+rowid;
			params = params+'&from='+from;
			$.msgBox({
			    title: form.title.confirm,
			    content: "\n Are you sure want to delete the permission?",
			    type: "confirm",
			    buttons: [{ value: "Yes" }, { value: "No" }],
			    success: function (result) {
			        if (result == "Yes") {
			        	$.ajax({
			        		url: 'bpm/dms/deleteDocumentPermission',
			                type: 'GET',
			                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			        		async: false,
			        		data: params,
			        		success : function(response) {
			        				if(response == 'success'){
				        				if(at=='folder'){
				        					if(from == 'user'){
				        						getUserPermisisonGrid('folder');
				        					}else if(from == 'role'){
				        						getRolePermisisonGrid('folder');
				        					}else if(from == 'group'){
				        						getGroupPermisisonGrid('folder');
				        					}
				        				}else{
				        					if(from == 'user'){
				        						getUserPermisisonGrid('document');
				        					}else if(from == 'role'){
				        						getRolePermisisonGrid('document');
				        					}else if(from == 'group'){
				        						getGroupPermisisonGrid('document');
				        					}
				        				}
			        				}
			        			}
			        	});
			        }else{
			  		  return false;
			  	  }
			    }
			});
		
	}
	if(rowid.length ==0){
		$.msgBox({
   		    title:form.title.message,
   		    content:"Please select atleast single row to delete",
   		});
	}

}
function checkBoxValidation(){
	$(document).ready(function() {
	    $('#dmsRolePermission').submit(function() {
	        var checkedValue = $(this).find('input[type="checkbox"]:checked');
	        if (!checkedValue.length) {
	            validateMessageBox(form.title.message,"Select Atleast One Permission ","message");
	            return false; // The form will *not* submit
	        }
	    });
	});
}
