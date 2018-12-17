function openSelectDialog(params,width,height,title){
 _execute('popupDialog',params);
 $myDialog = $("#popupDialog");
 $myDialog.dialog({
                            width: 'auto',
                            height: 'auto',
                            modal: true,
                            title: title
             });
 $myDialog.dialog("open");
}
function openSelectDialogForFixedPosition(id,params,width,height,title){
	 _execute(id,params);
	 $myDialog = $("#"+id);
	 $myDialog.dialog({
	                            width: width,
	                            height: height,
	                            modal: true,
	                            title: title,
	                            overflow: 'hidden',
	                            zIndex: 900
	             });
	 $myDialog.dialog("open");
	}
function closeSelectDialog(container){
	//alert(document.getElementById("from").value);
	//alert(document.getElementById("parentId").value);
	
	$("div#"+container).empty();
	$("#"+container).dialog("destroy");
	$("#"+container).dialog('close');
}

//$ date picker
$(document).ready(function() {
		$(".date-picker").datepicker({
	        showOn: 'button',
	        buttonImageOnly: true,
	        buttonImage: '/images/easybpm/form/rbl/_cal.gif',
	        dateFormat: 'mm/dd/yy'
	    });	
});

//show datePicker for given div 
function onDateClick(){	
	document.getElementById("datePicDiv").style.display="block";
}
//show and hide the required  div 
function nowDateClick(){
	document.getElementById("onDatePic").value="";
	document.getElementById("datePicDiv").style.display="none";
}

//Bulk suspend process
function bulkSuspendProcess(){
	var rowid =  gridObj.getGridParam('selarrrow'); 
	var isSuspend = true;
	rowid.forEach(function(item) {
		var state = gridObj.jqGrid('getCell', item, 'suspensionState');
		if(state == 2 && isSuspend){
			var processName = gridObj.jqGrid('getCell', item, 'name');
		validateMessageBox(form.title.message, form.msg.deselectSuspendStateProcess, false);
			
			isSuspend = false;
		}
	});
	if(isSuspend && rowid.length !=0 ){
		suspensionDialogSuspend(rowid,'Bulk');
	}
	if(rowid.length ==0){
		validateMessageBox(form.title.message, form.msg.selectAtleastSingleProcessToSuspend, false);
	
	}
}

//bulk Activate process
function bulkActivateProcess(){
	var rowid =  gridObj.getGridParam('selarrrow'); 
	var isActivate = true;
	rowid.forEach(function(item) {
		var state = gridObj.jqGrid('getCell', item, 'suspensionState');
		if(state == 1 && isActivate){
			var processName = gridObj.jqGrid('getCell', item, 'name');
		validateMessageBox(form.title.message, form.msg.deselectActiveStateProcess, false);
			
			isActivate = false;
		}
	});
	if(isActivate && rowid.length !=0){
		suspensionDialogActivate(rowid,'Bulk');
	}
	if(rowid.length ==0){
		validateMessageBox(form.title.message, form.msg.selectAtleastSingleProcessToActivate, false);
		
	}
}

//bulk Export process
function bulkExportProcess(screenName){
	var content;
	// show the content in dialogue according to screen
	if(screenName.indexOf('ProcessDefiniton')!=-1){
		content = form.msg.selectAtleastSingleProcessToExport;
	}else{
		content = form.msg.selectAtleastSingleProcessVersionToExport;
	}
	var selected_node = $.jstree._focused().get_selected();
	var id = selected_node.attr("name");
	if(id == 'all' || id == undefined){
		id = 'All_Classification';
	}
	var rowid =  gridObj.getGridParam('selarrrow'); 
	var depId ="";
    var i=1;
	rowid.forEach(function(item) {
		var deploymentId = gridObj.jqGrid('getCell', item, 'deploymentId');
		if(i==1){
			depId = deploymentId;
		}else{
			depId = depId +","+deploymentId;
		}
		i++;
			
	});
	if( rowid.length !=0){
		document.location.href = "bpm/manageProcess/bulkProcessXmlDownload?deploymentId="+depId+"&classificationId="+id;
	}else{
		validateMessageBox(form.title.message, content, false);
		
	}
}

//bulk Export process
function bulkExportProcessList(screenName){
	var content;
	// show the content in dialogue according to screen
	if(screenName.indexOf('ProcessDefiniton')!=-1){
		content = form.msg.selectAtleastSingleProcessToExport;
	}else{
		content = form.msg.selectAtleastSingleProcessVersionToExport;
	}
	var selected_node = $.jstree._focused().get_selected();
	var id = selected_node.attr("name");
	if(id == 'all' || id == undefined){
		id = 'All_Classification';
	}
	var rowid =  gridObj.getGridParam('selarrrow'); 
	var depId ="";
   	var i=1;
   	if ( !Array.prototype.forEach ) {
   		for(var index=0;index<rowid.length;index++){
   			var deploymentId = gridObj.jqGrid('getCell', rowid[index], 'Deployment_Id');
			if(index==0){
				depId = deploymentId;
			}else{
				depId = depId +","+deploymentId;
			}
   		}
   	} else {
   		rowid.forEach(function(item) {
		var deploymentId = gridObj.jqGrid('getCell', item, 'Deployment_Id');
		if(i==1){
			depId = deploymentId;
		}else{
			depId = depId +","+deploymentId;
		}
		i++;
			
		});
   	}
	
	
	if( rowid.length !=0){
		document.location.href = "bpm/manageProcess/bulkProcessXmlDownload?deploymentId="+depId+"&classificationId="+id;
	}else{
		validateMessageBox(form.title.message, content, false);
		
	}
}

///Bulk Delete process
function bulkDeleteProcess(screenName){
	var rowid =  gridObj.getGridParam('selarrrow'); 
	var i=1;
	var key="";
	if ( !Array.prototype.forEach ) {
		for(var index=0;index<rowid.length;index++){
		var keyId = gridObj.jqGrid('getCell', rowid[index], 'Key');
		if(index==0){
			key = keyId;
		}else{
			key = key +","+keyId;
		}
		}
	} else {
		rowid.forEach(function(item) {
		var keyId = gridObj.jqGrid('getCell', item, 'Key');
		if(i==1){
			key = keyId;
		}else{
			key = key +","+keyId;
		}
		i++;
			
		});
	}
	
	var content;
	// show the content in dialogue according to screen
	if(screenName.indexOf('ProcessDefiniton')!=-1){
		content = form.msg.selectAtleastSingleProcessToDelete;
	}else{
		content = form.msg.selectAtleastSingleProcessVersionToDelete;
	}
	if(rowid.length !=0){
		deleteAllProcessConfirm(key,screenName);
	}	
	if(rowid.length ==0){
		validateMessageBox(form.title.message, content, false);
		
	}
}

function deleteAllProcessConfirm(processKeys,screenName){
	var content;
	// show the content in dialogue according to screen
	if(screenName.indexOf('ProcessDefiniton')!=-1){
		content = form.msg.confirmToDeleteAllProcess;
	}else{
		content = form.msg.confirmToDeleteSelectedProcessVersion;
	}
	params = 'processKeys='+processKeys;
	var message = "";
	var isSuccess = false;
	var isNeededMsg=true;
	$.msgBox({
	    title: form.title.confirm,
	    content: content,
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	$.ajax({
	    			type : 'GET',
	    			async : false,
	    			url : 'bpm/manageProcess/deleteSelectedProcessDefinition?processKeys='+processKeys,
	    			success : function(response) {
	    				if(response.successMsg){
	    					message = response.successMsg;
	    					isSuccess = true;
	    					_loadGridWithSearchParams(_current_listview_id, "", "[{}]", "PROCESS_LIST", "asc", "", "and");
	    				}else{
	    					message = response.errorMsg;
	    				}
	    			}
	    		});
	        	/*document.location.href = "#bpm/manageProcess/bulkDeleteAllProcess";
	    		_execute('target',params);
	    		*/
	        }else{
	        	isNeededMsg=false;
	        	return false;
	        }
	    },
	    afterClose : function(){
	    	if(isNeededMsg){
	    		if(isSuccess){
		    		validateMessageBox(form.title.success, message , "success");
				}else{
					validateMessageBox(form.title.error, message , "error");
				}
	    	}
	    }
	});
}
//bulk Export process
function bulkDownloadProcess(){
	var rowid =  gridObj.getGridParam('selarrrow'); 
	var fileObj ="";
    var i=1;
	rowid.forEach(function(item) {
		var deploymentId = gridObj.jqGrid('getCell', item, 'fileObj');
		if(i==1){
			fileObj = deploymentId;
		}else{
			fileObj = fileObj +","+deploymentId;
		}
		i++;
			
	});
	if( rowid.length !=0){
		document.location.href = "bpm/manageProcess/bulkXmlDownload?fileObj="+fileObj;
	}else{
		validateMessageBox(form.title.message, form.msg.selectProcessToDownload, false);
		
	}
}

//Users Delete
function deleteUsers(){
	var rowid =  gridObj.getGridParam('selarrrow');
	if(rowid.length !=0 ){
		deleteAllUserConfirm(rowid);
	}
	if(rowid.length ==0){
		validateMessageBox(form.title.message, form.msg.selectUserToDelete, false);
		
	}
}

function deleteAllUserConfirm(userIds){
	params = 'userIds='+userIds;
	$.msgBox({
	    title: form.title.confirm,
	    content: form.msg.confirmToDeleteUser,
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	document.location.href = "#bpm/admin/userformDeleteSelectedUsers";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}

//method for update user status enable/disable.
function updateUserStatus(status){
	var rowid =  gridObj.getGridParam('selarrrow');
	var disabled=false;
	if(rowid.length !=0 ){
		rowid.forEach(function(item) {
			var enabled = gridObj.jqGrid('getCell', item, 'enabled');
			if(enabled == "Disabled"){
				disabled=true;
			}
		});
		if(disabled){
		validateMessageBox(form.title.message, form.msg.someSelectedUsersAreAlreadyDisabled, false);
			
			 return false;
		}else{
			params = 'userIds='+rowid;
			params = params+'&userStatus='+status;
			$.msgBox({
			    title: form.title.confirm,
			    content: form.msg.confirmToDisable,
			    type: "confirm",
			    buttons: [{ value: "Yes" }, { value: "No" }],
			    success: function (result) {
			        if (result == "Yes") {
			        	document.location.href = "#bpm/admin/userformUpdateUserStatus";
			    		_execute('target',params);
			        }else{
			  		  return false;
			  	  }
			    }
			});
		}
	}
	if(rowid.length ==0){
		validateMessageBox(form.title.message, form.msg.selectAtleastSingleUserToDisable, false);
		
	}
}

//Roles Delete
function deleteRoles(){
	var rowid =  gridObj.getGridParam('selarrrow');
	var roleTypes=new Array();
	var i = 0;
	rowid.forEach(function(item) {
		var roleType = gridObj.jqGrid('getCell', item, 'roleType');
		roleTypes[i] = roleType;
		i++;
	});
	
	var roleId;
	rowid.forEach(function(item) {
		var id = gridObj.jqGrid('getCell', item, 'roleId');
		roleId = roleId+","+id;
		
	});
	
	if(rowid.length !=0 ){
		if(roleTypes.indexOf('System') > -1){
		validateMessageBox(form.title.message, form.msg.cannotDeleteSystemRole, false);
			
		}else{
			deleteAllRoleConfirm(roleId);
		}
	}
	if(rowid.length ==0){
		validateMessageBox(form.title.message, form.msg.selectAtleastSingleRoleToDelete, false);
		
	}
}

function deleteAllRoleConfirm(roleIds){
	params = 'roleIds='+roleIds;
	$.msgBox({
	    title: form.title.confirm,
	    content: form.msg.confirmToDelete,
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	document.location.href = "#bpm/admin/deleteSelectedRoles";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}

// here to delete the uploaded file in database

function deleteImportedFile(){
	var rowid =  gridObj.getGridParam('selarrrow');
	if(rowid.length ==0){
		validateMessageBox(form.title.message, form.msg.selectAtleastSingleFileToDelete, false);
		
	}
	if(rowid.length != 0){
		var fileId = "";
		rowid.forEach(function(item) {
			var id = gridObj.jqGrid('getCell', item, 'File_Id');
			if(fileId == ""){
				fileId = id;
			} else {
				fileId += ","+id;
			}		
		});
		deleteAllImportedFiles(fileId);
	}
}

function deleteAllImportedFiles(fileIds){
	
	params = 'fileIds='+fileIds;
	$.msgBox({
	    title: form.title.confirm,
	    content:  form.msg.confirmToDeleteFiles,
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	document.location.href = "#bpm/dms/deleteSelectedFiles";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}



function validateAndOperateFunction(operation,parentDictId){
	var rowid =  gridObj.getGridParam('selarrrow');
	if(rowid.length !=0 ){
		if(operation == "disable"){
			handleDataDictionary(operation,parentDictId,rowid);
		}else if(operation == "delete"){
			$.msgBox({
			    title: form.title.confirm,
			    content: form.msg.confirmToDeleteDataDictionary,
			    type: "confirm",
			    buttons: [{ value: "Yes" }, { value: "No" }],
			    success: function (result) {
			    	 if (result == "Yes") {
			 			deleteDataDictionary(parentDictId,rowid);
			    	 }else{
				  		  return false;
				  	 }
		    	}
		    });
		}

	}else{
		validateMessageBox(form.title.message, form.msg.selectAtleastSingleDataDictionaryTo +operation, false);
		
	}
}

function handleDataDictionary(operation,parentDictId,rowid) {
	var disabled=false;
	var deleteColumnsProperty = [];
	rowid.forEach(function(item) {
		var data = $("#_DICTIONARY_LIST").jqGrid('getRowData', item); 
		deleteColumnsProperty[deleteColumnsProperty.length] = data.dictionaryId;
		if(operation === "disable") {
			var status = gridObj.jqGrid('getCell', item, 'isEnabled');
			if(status.toLowerCase() == "disabled"){
				disabled=true;
			}
		}
	});
	if(disabled){
		validateMessageBox(form.title.message, form.msg.someDataDictionaryAreAlready +operation+form.msg.d, false);
		
		 return false;
	}else{
		var param = "operation="+operation+"&dataDictionaryIds="+JSON.stringify(deleteColumnsProperty)+"&parentDictId="+parentDictId;
		$.ajax({	
			type: 'GET',
			async: false,
			url : '/bpm/admin/dataDictionaryGrid',
			data: param,
			success : function(response) {
				validateMessageBox(form.title.success, form.msg.dataDictionaryOperationSuccess +operation+form.msg.d, "success");
				var gridData = eval(response);
				 $("#_DICTIONARY_LIST").jqGrid('clearGridData');
	               $('#_DICTIONARY_LIST').jqGrid('setGridParam', {page: 1, data: gridData}).trigger('reloadGrid');
			}
		});
	}
}


function deleteDataDictionary(parentDictId,rowid) {
	var deleteColumnsProperty = [];
	if ( !Array.prototype.forEach ) {
		for(var i=0;i<rowid.length;i++){
			var data = $("#_DICTIONARY_LIST").jqGrid('getRowData', rowid[i]); 
			deleteColumnsProperty[deleteColumnsProperty.length] = data.dictionaryId; 
		}
	} else {
		rowid.forEach(function(item) {
		var data = $("#_DICTIONARY_LIST").jqGrid('getRowData', item); 
		deleteColumnsProperty[deleteColumnsProperty.length] = data.dictionaryId;
	});
	}
	var hierarchyParentId = document.getElementById("hierarchyParentId").value;
	var param = "dataDictionaryIds="+JSON.stringify(deleteColumnsProperty)+"&parentDictId="+parentDictId+"&hierarchyParentId="+hierarchyParentId;
	document.location.href = "#bpm/admin/deleteDataDictionaryGrid";
	_execute('target',param);
}	


/**
 * Move a dictionary row up by one row.
 */
function moveDictionaryRow(direction,currRow) {
	
    //get the record count of the grid.
    var recordCount = $('#_DICTIONARY_LIST').getGridParam("records");
    //get the selected row id. by default id will be numbered from 1,2,3
   // var currRow = $('#_DICTIONARY_LIST').getGridParam('selrow');
    var rowNoFrom = 0;
    var rowNoTo = 0;
    var dictionaryList = new Array();
    
    // move up only if it is not a first row
    if (currRow > 1 && direction === "up") {
    	rowNoFrom = currRow;
        rowNoTo = currRow - 1;
    }else if(currRow < recordCount && direction === "down"){
    	rowNoFrom = currRow;
        rowNoTo = parseInt(currRow) + 1;
    }
    var rowObjFrom = $("#_DICTIONARY_LIST").jqGrid('getRowData', rowNoFrom);
    var rowObjTo = $("#_DICTIONARY_LIST").jqGrid('getRowData', rowNoTo);
    var jsonRowObjFrom = eval(rowObjFrom);
    var jsonRowObjTo = eval(rowObjTo);
    
    
    var temp = 0;
    temp = jsonRowObjFrom.orderNo;
    jsonRowObjFrom.orderNo = jsonRowObjTo.orderNo;
    jsonRowObjTo.orderNo = temp;
    var tempJSONRowFromObjStatus;
    var tempJSONRowToObjStatus;
    if(jsonRowObjFrom.isEnabled === "Enabled"){
    	tempJSONRowFromObjStatus = "true";
    }else{
    	tempJSONRowFromObjStatus = "false";
    }
    if(jsonRowObjTo.isEnabled === "Enabled"){
    	tempJSONRowToObjStatus = "true";
    }else{
    	tempJSONRowToObjStatus = "false";
    }
    var datarowFrom = {
        id: rowNoFrom,
        dictionaryId: jsonRowObjFrom.dictionaryId,
        isEnabled: tempJSONRowFromObjStatus,
        name: jsonRowObjFrom.name,
        code: jsonRowObjFrom.code,
        orderNo: jsonRowObjFrom.orderNo
    };
    var datarowTo = {
        id: rowNoTo,
        dictionaryId: jsonRowObjTo.dictionaryId,
        isEnabled: tempJSONRowToObjStatus,
        name: jsonRowObjTo.name,
        code: jsonRowObjTo.code,
        orderNo: jsonRowObjTo.orderNo
    };
    $("#_DICTIONARY_LIST").jqGrid('setRowData', rowNoFrom, datarowTo);
    $("#_DICTIONARY_LIST").jqGrid('setRowData', rowNoTo, datarowFrom);
    
    $("#list_view_"+jsonRowObjFrom.dictionaryId).html("");
    $("#list_view_"+jsonRowObjTo.dictionaryId).html("");
    $("#list_view_"+jsonRowObjFrom.dictionaryId).html('<a id="showInteriorDictionary" href="javascript:void(0);" onclick="getDataDictionaryGrid(\''+jsonRowObjTo.dictionaryId+'\')"><b>Interior Dictionary</b></a>');
    $("#list_view_"+jsonRowObjTo.dictionaryId).html('<a id="showInteriorDictionary" href="javascript:void(0);" onclick="getDataDictionaryGrid(\''+jsonRowObjFrom.dictionaryId+'\')"><b>Interior Dictionary</b></a>');
    dictionaryList.push(datarowTo);
    dictionaryList.push(datarowFrom);
    //deselecting the checkbox if we are not checked it 
    if($("#_DICTIONARY_LIST").jqGrid('getGridParam', 'selarrrow') == ''){
    	 $("#_DICTIONARY_LIST").jqGrid('setSelection', currRow , false);
    }
    if(jsonRowObjTo.orderNo != undefined && jsonRowObjFrom.orderNo != undefined){
    	updateDataDictionaryOrderNo(dictionaryList);
    }
    
}

function updateDataDictionaryOrderNo(dictionaryList) {
	$.ajax({
		url: 'bpm/admin/updateOrderNos',
        type: 'POST',
        data: 'dataDictionaryJson='+JSON.stringify(dictionaryList),
        dataType: 'json',
        async : false,
        success : function(response) {
			data=response;
        },
		error : function(response){
				data=response;
				validateMessageBox(form.title.error, form.msg.cannotUpdateDictionaryOrder, false);
		}
	});
}

/**
 * Move a grid row up by one row.
 */
function moveGridRow(direction,currRow, gridName) {
		
    //get the record count of the grid.
    var recordCount = $('#_'+gridName).getGridParam("records");
    //get the selected row id. by default id will be numbered from 1,2,3
   // var currRow = $('#_DICTIONARY_LIST').getGridParam('selrow');
    var rowNoFrom = 0;
    var rowNoTo = 0;
    var gridArrayList = new Array();
    
    // move up only if it is not a first row
    if (currRow > 1 && direction === "up") {
    	rowNoFrom = currRow;
        rowNoTo = currRow - 1;
    }else if(currRow < recordCount && direction === "down"){
    	rowNoFrom = currRow;
        rowNoTo = parseInt(currRow) + 1;
    }
		
		
    var rowObjFrom = $('#_'+gridName).jqGrid('getRowData', rowNoFrom);
    var rowObjTo = $('#_'+gridName).jqGrid('getRowData', rowNoTo);
		
    var jsonRowObjFrom = eval(rowObjFrom);
    var jsonRowObjTo = eval(rowObjTo);
    var temp = 0;
    temp = jsonRowObjFrom.orderNo;
		
    jsonRowObjFrom.orderNo = jsonRowObjTo.orderNo;
    jsonRowObjTo.orderNo = temp;
    
    var datarowFrom = "";
    var datarowTo =  "";
    
    if(gridName == "DEPARTMENT_LIST"){
    	datarowFrom = gridJsonMapForDept(rowNoFrom, jsonRowObjFrom);
        datarowTo =  gridJsonMapForDept(rowNoTo, jsonRowObjTo);
	}else if(gridName == "GROUP_LIST"){
		datarowFrom = gridJsonMapForGroup(rowNoFrom, jsonRowObjFrom);
        datarowTo =  gridJsonMapForGroup(rowNoTo, jsonRowObjTo);
	}else if(gridName == "ROLE_LIST"){
		datarowFrom = gridJsonMapForRole(rowNoFrom, jsonRowObjFrom);
        datarowTo =  gridJsonMapForRole(rowNoTo, jsonRowObjTo);
	}else if(gridName == "CLASSIFICATION_LIST"){
		datarowFrom = gridJsonMapForClassification(rowNoFrom, jsonRowObjFrom);
        datarowTo =  gridJsonMapForClassification(rowNoTo, jsonRowObjTo);
	}else if(gridName == "LIST_VIEW_COLUMNS_PROPERTY"){

		datarowFrom = gridJsonMapForListViewColumn(rowNoFrom, jsonRowObjFrom);
        datarowTo =  gridJsonMapForListViewColumn(rowNoTo, jsonRowObjTo);
			
		 $('#_'+gridName).jqGrid('setRowData', rowNoFrom, datarowTo);
		 $('#_'+gridName).jqGrid('setRowData', rowNoTo, datarowFrom);
		gridArrayList.push(datarowTo);
		gridArrayList.push(datarowFrom);
		
		//updateListViewColumnOrderNo(gridArrayList);
	}
    
    $('#_'+gridName).jqGrid('setRowData', rowNoFrom, datarowTo);
    $('#_'+gridName).jqGrid('setRowData', rowNoTo, datarowFrom);

    gridArrayList.push(datarowTo);
    gridArrayList.push(datarowFrom);
    
    //deselecting the checkbox if we are not checked it   
    if($('#_'+gridName).jqGrid('getGridParam', 'selarrrow') == ''){
    	$('#_'+gridName).jqGrid('setSelection', currRow , false);
    }
    if(jsonRowObjTo.orderNo != undefined && jsonRowObjFrom.orderNo != undefined){
    	if(gridName == "DEPARTMENT_LIST"){
    		updateDepartmentOrderNo(gridArrayList);
    	}else if(gridName == "GROUP_LIST"){
    		updateGroupOrderNo(gridArrayList);
    	}else if(gridName == "ROLE_LIST"){
    		updateRoleOrderNo(gridArrayList);
    	}else if(gridName == "CLASSIFICATION_LIST"){
    		updateClassificationOrderNo(gridArrayList);
    	}else if(gridName == "LIST_VIEW_COLUMNS_PROPERTY"){
    		updateListViewColumnOrderNo(gridArrayList);
    	}
    }
    
}

function updateListViewColumnOrderNo(gridArrayList) {
	
	$.ajax({
		url: 'bpm/listView/updateListViewColumnOrderNos',
        type: 'POST',
        data: 'listViewColumnJson='+JSON.stringify(gridArrayList),
        dataType: 'json',
        async : false,
        success : function(response) {
			data=response;
        },
		error : function(response){
				data=response;
				validateMessageBox(form.title.error, form.msg.cannotUpdateColumnOrder, false);
		}
	});
}


function gridJsonMapForDept(rowNo, jsonRowObj){
	
	return {
		id: rowNo,
		departmentId: jsonRowObj.departmentId,
		description: jsonRowObj.description,
		viewName: jsonRowObj.viewName,
		departmentOwner: jsonRowObj.departmentOwner,
        orderNo: jsonRowObj.orderNo,
        createdTimeByString: jsonRowObj.createdTimeByString,
        departmentType: jsonRowObj.departmentType,
        superDepartment:jsonRowObj.superDepartment
	}
}

function gridJsonMapForGroup(rowNo, jsonRowObj){
	return {
		id: rowNo,
		groupId: jsonRowObj.groupId,
        description: jsonRowObj.description,
        viewName: jsonRowObj.viewName,
        personIncharge: jsonRowObj.personIncharge,
        superGroup: jsonRowObj.superGroup,
        orderNo: jsonRowObj.orderNo,
        createdTimeByString: jsonRowObj.createdTimeByString
	}
}

function gridJsonMapForRole(rowNo, jsonRowObj){
	return {
		id: rowNo,
		roleId: jsonRowObj.roleId,
        description: jsonRowObj.description,
        name: jsonRowObj.name,
        viewName: jsonRowObj.viewName,
        roleType: jsonRowObj.roleType,
        orderNo: jsonRowObj.orderNo,
        createdTimeByString: jsonRowObj.createdTimeByString,
        columnTitle	: jsonRowObj.columnTitle,
        dataFields	: jsonRowObj.dataFields
				
	}
}

function gridJsonMapForClassification(rowNo, jsonRowObj){
	return {
		id: rowNo,
		classificationId: jsonRowObj.classificationId,
		name: jsonRowObj.name,
        description: jsonRowObj.description,
        orderNo: jsonRowObj.orderNo,
        columnTitle	: jsonRowObj.columnTitle,
        dataFields	: jsonRowObj.dataFields
	}
}

function gridJsonMapForListViewColumn(rowNo, jsonRowObj){
	return {
		orderBy: rowNo,
		orderNo: jsonRowObj.orderNo,
		//columnId: jsonRowObj.columnId,
	    columnTitle: jsonRowObj.columnTitle,	            
    	dataFields: jsonRowObj.dataFields,
		orderBy:jsonRowObj.orderBy
	}
}

function updateDepartmentOrderNo(gridArrayList) {
	$.ajax({
		url: 'bpm/admin/updateDepartmentOrderNos',
        type: 'POST',
        data: 'departmentJson='+JSON.stringify(gridArrayList),
        dataType: 'json',
        async : false,
        success : function(response) {
			data=response;
        },
		error : function(response){
				data=response;
				validateMessageBox(form.title.error, form.msg.cannotUpdateDepartmentOrder, false);
		}
	});
}

function updateGroupOrderNo(gridArrayList) {
	$.ajax({
		url: 'bpm/admin/updateGroupOrderNos',
        type: 'POST',
        data: 'groupJson='+JSON.stringify(gridArrayList),
        dataType: 'json',
        async : false,
        success : function(response) {
			data=response;
	    },
		error : function(response){
			data=response;
			validateMessageBox(form.title.error, form.msg.cannotUpdateGroupOrder, false);
		}
	});
	
	
}
//    		updateListViewColumnOrderNo(gridArrayList);

function updateRoleOrderNo(gridArrayList) {
	$.ajax({
		url: 'bpm/admin/updateRoleOrderNos',
        type: 'POST',
        data: 'roleJson='+JSON.stringify(gridArrayList),
        dataType: 'json',
        async : false,
        success : function(response) {
			data=response;
        },
		error : function(response){
				data=response;
				validateMessageBox(form.title.error, form.msg.cannotUpdateRoleOrder, false);
		}
	});
}


function updateClassificationOrderNo(gridArrayList) {
	$.ajax({
		url: 'bpm/manageProcess/updateClassificationOrderNos',
        type: 'POST',
        data: 'classificationJson='+JSON.stringify(gridArrayList),
        dataType: 'json',
        async : false,
        success : function(response) {
			data=response;
        },
		error : function(response){
			data=response;
			validateMessageBox(form.title.error, form.msg.cannotUpdateRoleOrder, false);
		}
	});
}



//Groups Delete
function deleteGroups(){
	var rowid =  gridObj.getGridParam('selarrrow');
	
	var groupId;
	rowid.forEach(function(item) {
		var id = gridObj.jqGrid('getCell', item, 'groupId');
		groupId = groupId+","+id;
		
	});
	
	if(rowid.length !=0 ){
		deleteAllGroupsConfirm(groupId);
	}
	if(rowid.length ==0){
		validateMessageBox(form.title.message, form.msg.selectAtleastSingleGroupToDelete, false);
	}
}

function deleteAllGroupsConfirm(groupIds){
	params = 'groupIds='+encodeURI(groupIds);
	$.msgBox({
	    title: form.title.confirm,
	    content: form.msg.confirmToDeleteGroup,
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	document.location.href = "#bpm/admin/deleteSelectedGroup";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}

//Departments Delete
function deleteDepartments(){
	var rowid =  gridObj.getGridParam('selarrrow');
	var departmentTypes=new Array();
	var i=0;
	rowid.forEach(function(item) {
		var deploymentType = gridObj.jqGrid('getCell', item, 'departmentType');
		departmentTypes[i] = deploymentType;
		i++;
	});
	var deptIds=[];
	rowid.forEach(function(item) {
		var id = gridObj.jqGrid('getCell', item, 'departmentId');
		deptIds[deptIds.length]=id;
	});
	
	if(rowid.length !=0 ){
		if(departmentTypes.indexOf('root') > -1){
					validateMessageBox(form.title.message, form.msg.cannotDeleteRootDepartment, false);
			
		}else{
			deleteAllDepartmentsConfirm(deptIds);
		}
	}
	if(rowid.length ==0){
		validateMessageBox(form.title.message, form.msg.selectAtleastSingleDepartmentToDelete, false);
		
	}
}
function deleteAllDepartmentsConfirm(departmentIds){
	params = 'departmentIds='+JSON.stringify(departmentIds);
	$.msgBox({
	    title: form.title.confirm,
	    content: form.msg.confirmToDeleteDepartment,
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	document.location.href = "#bpm/admin/deleteSelectedDepartments";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}

function gridDataExportToCSV(gridName,gridType){
	var colArray=new Array();
	colArray=$(gridObj).getDataIDs();
	
	var rowId =  gridObj.getGridParam('selarrrow');
	//if(rowId.length > 0 ){
	    //Get grid all column name
	    var colName=$(gridObj).getRowData(colArray[0]); 
	    var colNames=new Array();  
	    var ii=0;
	    for (var i in colName){colNames[ii++]=i;} 
	    //Get All Grid Datas
	    var data =  gridObj.jqGrid('getGridParam','data');
	    var gridjsonRowCollection = JSON.stringify(data);
	   // document.location.href = "bpm/manageProcess/gridCSVExport?gridHeader="+colNames+"&gridDatas="+gridjsonRowCollection+"&gridName="+gridName;
	    document.location.href = "bpm/manageProcess/gridCSVExport?gridHeader="+colNames+"&gridName="+gridName+"&gridType="+gridType+"&instanceId="+rowId;
	//} else {
	//	validateMessageBox(form.title.message, form.msg.selectRowsToExport,false);
	//}

}
function contains(a, obj) {
    var i = a.length;
    while (i--) {
       if (a[i] === obj) {
           return true;
       }
    }
    return false;
}
function showTreeList(title, id, selectType,treeName,popupId,selectedValues){
	var values = [];
	$('#'+id+' option').each(function() { 
	    values.push( $(this).attr('value') );
	});
	
	if(treeName == 'moduleTree'){
		var moduleId = document.getElementById("id").value;
		params = 'title='+title+'&id='+id+'&selectType='+selectType+'&moduleId='+moduleId+'&selectedValue='+values;
	}else{
		var params = 'title='+title+'&id='+id+'&selectType='+selectType+'&selectedValue='+values;
	}
	clearTreePopupDiv();
	
	document.location.href = "#bpm/admin/"+treeName;
	_execute(popupId,params);
	$myDialog = $("#"+popupId);
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       modal: true,
                       title: title
	             });
	 $myDialog.dialog("open");
}


function clearTreePopupDiv(){
	$("div#popupDialog").empty();
	$("#popupDialog").dialog("destroy");
}

function encodeURLStringComponent(encodeURLString){
	return encodeURLString.replace(/\%/g,"%25").replace(/\#/g,"%23").replace(/\&/g,"%26").replace(/\~/g, "%7E").replace(/\*/g, "%2A").replace(/\+/g,"%2B");
}

//create dynamic form and submit
function creatAndSubmitDynamicForm(path,params,method){
	method = method || "POST"; // Set method to post by default if not specified.

    // The rest of this code assumes you are not using a library.
    // It can be made less wordy if you use one.
    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", path);

    for(var key in params) {
        if(params.hasOwnProperty(key)) {
            var hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", params[key]);

            form.appendChild(hiddenField);
         }
    }

    document.body.appendChild(form);
    form.submit();
}

//redirects to previous page
function backToPreviousPage(){
	/*if(CKEDITOR.instances['content'] != null && CKEDITOR.instances['content'] != undefined){
		CKEDITOR.instances['content'].destroy();
	}*/
	var bpmmenutype = $.cookie('bpmMenuType');
	var bpmsidemenutype = $.cookie('bpmSideMenuType');
	var menutype="";
	var urlType = "";
	var listViewParams = "";
	var listViewName = "";
	var bpmAppBack = "";
	if(bpmsidemenutype == null || bpmsidemenutype == ""){
	menutype = bpmmenutype;
	} else {
	menutype = bpmsidemenutype;
	}
	if(menutype == "side" || menutype == "Side" ){
		urlType = $.cookie('bpmSideUrlType');
		listViewParams = $.cookie('bpmSideListViewParams');
		listViewName = $.cookie('bpmSideListViewName');
		var script = $.cookie('bpmSideScript');
		if(script != null || script !="null"){
			var callback_function = new Function(script);
   			setTimeout(function(){callback_function();},100);
		}
	} else {
		urlType = $.cookie('bpmUrlType');
		listViewParams = $.cookie('bpmListViewParams');
		listViewName = $.cookie('bpmListViewName');
	}
	if(urlType == "listview" || urlType == "listView"){
		if(listViewParams != ""){
			var listViewParams = "[{"+listViewParams+"}]";
			var listViewNameAsTitle=listViewName;
			//showListViewsWithContainerAndParam(listViewName, listViewNameAsTitle.replace('_',' '), 'target', eval(listViewParams));
			showListViewsWithContainerAndParam(listViewName, '', 'target', eval(listViewParams));
		}else{
			var listViewNameAsTitle=listViewName;
			//showListViewsWithContainer(listViewName, listViewNameAsTitle.replace('_',' '), 'target');
			showListViewsWithContainer(listViewName, '', 'target');
		}
	} else {
		
		bpmAppBack = $.cookie('bpmAppBack');
		document.location.href='#bpm' + bpmAppBack;
		_execute('target','');
	}
}

function myOwnedBucketGridExport(gridId,gridType){
    //Get All display except hidden & checkbox Grid Columns
	//var columnNames = $("#_"+gridId).jqGrid('getGridParam','colNames');
    var mymodel = $("#_"+gridId).getGridParam('colModel'); // this get the colModel array
    
    var gridDataKey=new Array();  
    // loop the array and get the hideen columns
	 $.each(mymodel, function(i) {
		 if(this.hidden != true && this.name != 'cb' && this.name != 'operate') {
			 gridDataKey.push(this.name);
		 }
	 });
    
    //Get all group header column names
    var groupHeaderColumns=new Array();  
    var groupHeadersOptions = $("#_"+gridId).jqGrid("getGridParam", "groupHeader");
    if(groupHeadersOptions != null){
    	for(var i=0;i<groupHeadersOptions.groupHeaders.length;i++){
    		groupHeaderColumns.push(groupHeadersOptions.groupHeaders[i].titleText);
    	}
    }
	
    //Get All Grid Datas 
    var data = $("#_"+gridId).jqGrid('getGridParam','data');
    //Remove unselected grid data
    var rowid =  gridObj.getGridParam('selarrrow');	
    var filtereddata = [];
    rowid.forEach(function(item) 
    { 
	   data.filter(function(dataItem) {
		   if(dataItem.id == gridObj.jqGrid('getCell', item, 'id')){
			   filtereddata.push(dataItem) ;
		   }
	   });	   
    });

    var gridjsonRowCollection = JSON.stringify(filtereddata);
    //Calling grid data export by POST method (Using form submit)
	var path = "bpm/manageProcess/flowStatisticsGridExport";
	var params = {gridHeader:gridDataKey,gridDataKey:gridDataKey,groupHeader:groupHeaderColumns,gridData:gridjsonRowCollection,gridType:gridType};
	creatAndSubmitDynamicForm(path,params,"POST");
}

//bulk Export process
function bulkProcessArchivalExport(){
	var rowid =  gridObj.getGridParam('selarrrow'); 
	if( rowid.length !=0){
		var processJsonObj = [];
		rowid.forEach(function(item) {
			processJsonObj.push({path: gridObj.jqGrid('getCell', item, 'Path')});
		});
		//Calling grid data export by POST method (Using form submit)
		var path = "bpm/manageProcess/bulkProcessArchivalExport";
		var params = {processDetails:JSON.stringify(eval(processJsonObj))};
		creatAndSubmitDynamicForm(path,params,"POST");
	}else{
		validateMessageBox(form.title.message,form.msg.selectAtleastSingleInstanceToExport,form.title.info);
	}
}

function _openHtmlFile(path){
	var url = "resources"+path+"/operatePage.html";
	var win=window.open(url);
	win.focus();
}
function deleteUserOpinion(){
	var rowid =  gridObj.getGridParam('selarrrow');
	if(rowid.length == 0){
		validateMessageBox(form.title.message, "请选择要删除的记录", false);
		
	}
	if(rowid.length != 0){
		var userOpinionId = "";
		rowid.forEach(function(item) {
			var id = gridObj.jqGrid('getCell', item, 'id');
			if(userOpinionId == ""){
				userOpinionId = id;
			} else {
				userOpinionId += ","+id;
			}		
		});
		deleteAllUserOpinion(userOpinionId);
	}
}

function deleteAllUserOpinion(userOpinionIds){
	
	params = 'userOpinionIds='+userOpinionIds;
	$.msgBox({
	    title: form.title.confirm,
	    content: "确定要删除选中的记录吗？",
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	document.location.href = "#bpm/opinion/deleteUserOpinion";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}

function getUserOpinion(e){
    if(e.keyCode ==8 || !(e.keyCode < 65 || e.keyCode > 90) || !(e.keyCode < 48 || e.keyCode > 57) || !(e.keyCode < 96|| e.keyCode > 105)){
    	var searchName=$("#searchUserOpinion").val();
    	if(searchName!=""){
    		$.ajax({
    			type : 'GET',
    			async : false,
    			url : '/bpm/opinion/getUserOpinion?opinionWord='+searchName,
    			success : function(data) {
    				var dataList = $("#searchUserOpinionList");
    				dataList.empty();
    				$.each(data, function(index, item) {
    					var selectOption = document.createElement("OPTION");
    					selectOption.text = item.opinionWord;
    					selectOption.value = item.opinionWord;
    					selectOption.id = item.id;
    					dataList.append(selectOption);
    				});
    			}
    		});
    	}
      }
}

function selectWorkFlowExportType(){
	var rowid =  gridObj.getGridParam('selarrrow'); 
	var canExport = true;
	if( rowid.length !=0){
		/*for(var i=0;i<rowid.length;i++){ 
			var hasForm = gridObj.jqGrid('getCell', rowid[i] , 'hasForm');
			if(hasForm == false) {
				canExport = false;
				break;
			}
		}*/
			$.msgBox({
				title : form.title.exportOption,
				content : form.msg.justExport,
				type : "confirm",
				buttons : [ {
					value : "Form"
				},{
					value : "Form and Trace"
				}, {
					value : "Cancel"
				} ],
				success : function(result) {
					if (result == "Cancel") {
						return;
					}else {
						var processJsonObj = [];
						var withTrace = true;
						
							rowid.forEach(function(item) {
									processJsonObj.push({taskId: gridObj.jqGrid('getCell', item, 'id'),processInstanceId: gridObj.jqGrid('getCell', item, 'executionId'),
										processDefinitionId: gridObj.jqGrid('getCell', item, 'processDefinitionId'),processName: gridObj.jqGrid('getCell', item, 'processName'),
										classificationId: gridObj.jqGrid('getCell', item, 'classificationId')});

							});
							if (result == "Form") {
								withTrace = false;
							}
							//Calling grid data export by POST method (Using form submit)
							var path = "bpm/manageProcess/exportWorkFlowInstance";
							var params = {processDetails:JSON.stringify(eval(processJsonObj)),withTrace:withTrace};
							creatAndSubmitDynamicForm(path,params,"POST");
						

					}
				}
			});
	}else{
		validateMessageBox(form.title.message, form.msg.selectAtleastSingleInstanceToExport, form.title.info);
	}
}

function deleteSystemLog() {
	var rowid =  gridObj.getGridParam('selarrrow');
	if(rowid.length == 0){
		validateMessageBox(form.title.message, form.msg.selectAtleastSingleRowToDelete, false);
		
	}
	if(rowid.length != 0){
		var systemLogId = "";
		rowid.forEach(function(item) {
			var id = gridObj.jqGrid('getCell', item, 'id');
			if(systemLogId == ""){
				systemLogId = id;
			} else {
				systemLogId += ","+id;
			}		
		});
		deleteAllSystemLog(systemLogId);
	}	
}

function deleteAllSystemLog(systemLogIds){
	
	params = 'systemLogIds='+systemLogIds;
	$.msgBox({
	    title: form.title.confirm,
	    content: form.msg.confirmToDeleteLogs,
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	document.location.href = "#bpm/admin/deleteSystemLog";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}


function deleteLogs(){
	var rowid =  gridObj.getGridParam('selarrrow');
	var logDate = '';
	var logType = '';
	if(rowid.length == 0){
		validateMessageBox(form.title.message, form.msg.selectAtleastSingleRowToDelete, false);
		
	}else{
		rowid.forEach(function(item) {
			var logdate = gridObj.jqGrid ('getCell', item, 'Date');
			logType = gridObj.jqGrid('getCell',item,'Type');
			if(logDate == ""){
				logDate = logdate;
			} else {
				logDate += ","+logdate;
			}	
		});
		params = 'logDate='+logDate+'&logType='+logType;
		$.msgBox({
		    title: form.title.confirm,
		    content: form.msg.confirmToDeleteLogs,
		    type: "confirm",
		    buttons: [{ value: "Yes" }, { value: "No" }],
		    success: function (result) {
		        if (result == "Yes") {
		        	document.location.href = "#bpm/admin/deleteLog";
		    		_execute('target',params);
		        }else{
		  		  return false;
		  	  }
		    }
		});
	}	
}

/* For Report */
function deleteReport(){
	var rowid =  gridObj.getGridParam('selarrrow');
	if(rowid.length == 0){
	validateMessageBox(form.title.message, form.msg.selectAtleastSingleRowToDelete, false);
		
	}
	if(rowid.length != 0){
		var reportId = "";
		rowid.forEach(function(item) {
			var id = gridObj.jqGrid('getCell', item, 'id');
			if(reportId == ""){
				reportId = id;
			} else {
				reportId += ","+id;
			}		
		});
		deleteAllReports(reportId);
	}
}

function deleteAllReports(reportIds){
	
	params = 'reportIds='+reportIds;
	$.msgBox({
	    title: form.title.confirm,
	    content: form.msg.confirmToDeleteReports,
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	document.location.href = "#bpm/report/deleteReport";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}

