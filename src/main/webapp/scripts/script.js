//Global declaration for CKeditor
var sourceTableColumns={};
var sub_Form_Field_List = [];
var sub_Form_Relation_List = [];
var columnIdList = [];
var form_Field_column_map = {};
var form_Field_name_List = [];
var form_Field_name_Map = {};
var radioOrCheckboxCount = 0;
var localHistoryStorage = [];
var topMenuName = "";
var prevMenuName = "";
var defaultTreeNodes = [];
var removeAttachmentIds = [];
var addAttachmentIds = [];
var refresh = "";
var filterURL = "";
var richTextBoxMap={};
var selectedFromModuleId="";
var rowNumbers = new Array();

// This function is used by the login screen to validate user/pass
// are entered.
function validateRequired(form) {
	var bValid = true;
	var focusField = null;
	var i = 0;
	var fields = new Array();
	oRequired = new required();

	for (x in oRequired) {
		if ((form[oRequired[x][0]].type == 'text'
				|| form[oRequired[x][0]].type == 'textarea'
				|| form[oRequired[x][0]].type == 'select-one'
				|| form[oRequired[x][0]].type == 'radio' || form[oRequired[x][0]].type == 'password')
				&& form[oRequired[x][0]].value == '') {
			if (i == 0)
				focusField = form[oRequired[x][0]];

			fields[i++] = oRequired[x][1];

			bValid = false;
		}
	}

	if (fields.length > 0) {
		focusField.focus();
		alert(fields.join('\n'));
	}

	return bValid;
}

// This function is a generic function to create form elements
function createFormElement(element, type, name, id, value, parent) {
	var e = document.createElement(element);
	e.setAttribute("name", name);
	e.setAttribute("type", type);
	e.setAttribute("id", id);
	e.setAttribute("value", value);
	parent.appendChild(e);
}

function confirmDelete(obj) {
	var msg = form.msg.confirmToDeleteThis + obj + "?";
	ans = confirm(msg);
	return ans;
}

// 18n version of confirmDelete. Message must be already built.
function confirmMessage(obj) {
	var msg = "" + obj;
	ans = confirm(msg);
	return ans;
}

/**
 * Browser window functions
 */
function getWindowWidth() {
	var browserWidth = window.innerWidth
			|| document.documentElement.clientWidth
			|| document.body.clientWidth;
	if (browserWidth) {
		return browserWidth;
	}
	return 0;
}

function getWindowHeight() {
	var browserHeight = window.innerHeight
			|| document.documentElement.clientHeight
			|| document.body.clientHeight;
	if (browserHeight)
		return browserHeight;
	return 0;
}

function organizationUserGridWidth() {
	var width = $("#user-grid").width();
	var winWidth = parseInt(width) - 5;
	return winWidth;
}

function organizationGridWidth() {
	var width = $("#grid_div").width();
	var winWidth = parseInt(width) - 5;
	return winWidth;
}

function listViewColumnsPropertyGridWidth() {
	var width = parseInt($("#col_prop_grid").width());
	var winWidth = parseInt(width) - 1;
	return winWidth;
}

function listViewColumnsPropertyGridHeight() {
	var newHeight = $("#target").height();
	return parseInt(newHeight) - 215;
}

function listViewGroupSettingPropertyGridWidth() {
	var width = parseInt($("#wizardTab-content").width());
	var winWidth = parseInt(width) - 4;
	return winWidth;
}

function listViewGroupSettingPropertyGridHeight() {
	var newHeight = $("#target").height();
	return parseInt(newHeight) - 450;
}

function listViewButtonsPropertyGridWidth() {
	var width = parseInt($("#button_prop_grid").width());
	var winWidth = parseInt(width) - 1;
	return winWidth;
}

function listViewButtonsPropertyGridHeight() {
	var newHeight = $("#target").height();
	return parseInt(newHeight) - 215;
}

function organizationGridHeight() {
	var newHeight = $("#target").height();
	return parseInt(newHeight) - 188;
}
function processGridHeight() {
	var newHeight = $("#target").height();
	return parseInt(newHeight * 55 / 100) - 87;
}
function getGridWidth(width) {
	return parseInt(getWindowWidth()) - 299;
}

function getGridHeight() {
	var newHeight = $("#target").height();
	return parseInt(newHeight) - 130;
}

function getGridHeightWithSearchBox() {
	if(gridId == "TIMING_TASK"){
		var h = $("#newsList").height();
		return h;
	}else{
	var newHeight = $("#target").height();
	return parseInt(newHeight) - 130;
}
}
function getGridHeightWithTreeAndSearchBox() {
	var newHeight = $("#target").height();
	return parseInt(newHeight) - 205;
}

function getGridHeightWithTextArea() {
	
	var newHeight = $("#rightPanel").height();
	return parseInt(newHeight) - 90;
	
}

function getGridWidthWithSearchBox() {
	return parseInt(getWindowWidth()) - 299;
}

function getGridWidthForHomePage(){
	return parseInt($("#newsList").width()) - 1;
}

function getGridHeightForHomePageWithTree(){
	return parseInt($("#"+widgetContainer).height()) - 142;
}

function getGridHeightForHomePage(){
	return parseInt($("#"+widgetContainer).height()) - 70;
}

function _showFormDesigner(cellValue, options, rawObject) {
	if(rawObject.isEdit=='true'){
		if(rawObject.templateForm=="true") {
			return '<a href="#bpm/form/showFormDesignerForEdit"  onclick="javascript:_showFormDesignerForEdit(\''
			+ rawObject.id + '\',\''+rawObject.module+'\');">' + cellValue + '*' + '</a>';	
			
		} else {
			return '<a href="#bpm/form/showFormDesignerForEdit"  onclick="javascript:_showFormDesignerForEdit(\''
			+ rawObject.id + '\',\''+rawObject.module+'\');">' + cellValue + '</a>';
		}
	}else{
		return cellValue;
	}
	
}
function _showFormDesignerForEdit(formId,moduleId) {
	var params = "formId="+formId+"&moduleId="+moduleId;
	_execute('target', params);
}

function _showJspPage(cellValue, options, rawObject) {
	return '<a href="#bpm/form/showJspUpoladForm"  onclick="javascript:showJspPage(\''
	+ rawObject.id + '\',\''+rawObject.module+'\',\''+rawObject.formName+'\');">' + cellValue + '</a>';	
}

function showJspPage(formId,moduleId,formName) {
	var params = "formId="+formId+"&moduleId="+moduleId+"&formName="+formName;
	var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
	var nodePath = $("#module_relation_tree").jstree("get_path", $("#"+selected_nodes.attr("id")), true); 
	var nodeLevel = nodePath.length;
	var moduleId = nodePath[0];
	$("#module_title").html("");
	$("#module_title").html("JSP Form");
	_execute('rightPanel',params);
}

function _deleteForm(cellValue, options, rawObject) {
	var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
	var isEdit=selected_nodes.attr("isedit");
	
	 //return '<a id="deleteFormById" href="#bpm/form/deleteFormById"	 data-role="button" data-theme="b" onclick="_execute(\'target\',\'formId='+rawObject.id+'\&formName='+rawObject.formName+'\');"><b>Delete</b></a>';
	  if(rawObject.active == "false" && rawObject.isSystemModule!="true" && isEdit=='true'){ 
		  return '<a id="deleteFormById"  href="javascript:void(0);" data-role="button" data-theme="b" onclick="confirmDeleteForm(\'bpm/form/deleteFormById\',\''+rawObject.id+'\',\''+rawObject.formName+'\',\''+rawObject.module+'\',\''+rawObject.isEdit+'\',\''+rawObject.isSystemModule+'\',\''+false+'\',\''+rawObject.version+'\');"><b>Delete</b></a>';
	  }else 
		  return "<span style='cursor:url(\"/images/Cancel_icon.png\"),url(\"/images/Cancel_icon.png\"),auto'>Delete</span>";
	 
	/*if (rawObject.active == "false") {
		return '<a id="deleteFormById" href="javascript:void(0);" data-role="button" data-theme="b" onclick="_deleteFormDesignById(\''
				+ rawObject.id
				+ '\',\''
				+ rawObject.formName
				+ '\',\''
				+ rawObject.module + '\', \'true\');"><b>Delete</b></a>';
	} else
		return 'Delete';*/

}

function operateBulkTasks(operation){
	var rowid =  gridObj.getGridParam('selarrrow');
	if(rowid.length == 0){ // here to terminate instance list in null or not
		validateMessageBox(form.title.message,form.msg.select,false);
	}
	if(rowid.length != 0 ){
		if(operation == 'replace'){
			var canReplace = true ;
			var taskJsonObj = [];
			var nodeType = "";
			var title = "修改办理人";
			var nodeTypes = new Array();
			
			if(rowid.length == 1) {
				canReplace = true;
			} else {
				for(var i=1;i<rowid.length;i++){ 
					if( gridObj.jqGrid('getCell', rowid[0], 'nodeType') != gridObj.jqGrid('getCell', rowid[i], 'nodeType') ) {
						canReplace = false;
						break;
					}
				}
			}
			
			if(canReplace) {
				for(var i=0;i<rowid.length;i++){
					var lastOperationPerformed = gridObj.jqGrid('getCell', rowid[i], 'lastOperationPerformed');
					nodeType = gridObj.jqGrid('getCell', rowid[i], 'nodeType');
					if(lastOperationPerformed != 'terminate' && lastOperationPerformed != 'suspend' && lastOperationPerformed != 'withdraw'
						&& gridObj.jqGrid('getCell', rowid[i], 'id') != gridObj.jqGrid('getCell', rowid[i], 'executionId')){
						taskJsonObj.push({taskId: gridObj.jqGrid('getCell', rowid[i], 'id')});
					}else {
						taskJsonObj = [];
						break;
					}
				}
			} 
			
			if(taskJsonObj.length > 0){
				getUsersToInvolve(null, 'bulkReplace',title,null,null,nodeType,null);
			}else {
				if(!canReplace) {
					validateMessageBox(form.title.message,form.msg.replaceError);
				} else {
					validateMessageBox(form.title.message,form.msg.operationError);
				}
			}
			
		}else if(operation == 'restore'){
			var taskJsonObj = [];
			for(var i=0;i<rowid.length;i++){
				var lastOperationPerformed = gridObj.jqGrid('getCell', rowid[i], 'lastOperationPerformed');
				if(lastOperationPerformed == 'suspend' || lastOperationPerformed == 'withdraw'){
					taskJsonObj.push({taskId: gridObj.jqGrid('getCell', rowid[i], 'id'),executionId: gridObj.jqGrid('getCell', rowid[i], 'executionId'),resourceName: gridObj.jqGrid('getCell', rowid[i], 'resourceName')});
				}else {
					taskJsonObj = [];
					break;
				}
			}
			
			if(taskJsonObj.length > 0){
				var jsonValues = JSON.stringify(taskJsonObj);
				params = 'taskDetails='+jsonValues;
				document.location.href = "#bpm/manageProcess/activateProcessInstances";
				 _execute('target',params);
			}else {
				validateMessageBox(form.title.message,form.msg.operationError);
			}
			
		}else if(operation == 'back'){
			var taskJsonObj = [];
			
			for(var i=0;i<rowid.length;i++){
				var lastOperationPerformed = gridObj.jqGrid('getCell', rowid[i], 'lastOperationPerformed');
				var isForStartNodeTask = gridObj.jqGrid('getCell', rowid[i], 'isForStartNodeTask');
				if(lastOperationPerformed != 'terminate' && lastOperationPerformed != 'withdraw' && lastOperationPerformed != 'suspend' 
					&& gridObj.jqGrid('getCell', rowid[i], 'id') != gridObj.jqGrid('getCell', rowid[i], 'executionId') && isForStartNodeTask != 'true'){
					taskJsonObj.push({taskId: gridObj.jqGrid('getCell', rowid[i], 'id'),resourceName: gridObj.jqGrid('getCell', rowid[i], 'resourceName'),
						executionId : gridObj.jqGrid('getCell', rowid[i],'executionId')});
				}else {
					taskJsonObj = [];
					break;
				}
			}
			
			if(taskJsonObj.length > 0){
				var jsonValues = JSON.stringify(taskJsonObj);
				params = 'taskDetails='+jsonValues;
				document.location.href = "#bpm/of/returnTasks";
				 _execute('target',params);
			}else {
				validateMessageBox(form.title.message,form.msg.operationError);
			}
			
		}else if(operation == 'export'){
			myOwnedBucketGridExport('MY_OWNEDTASKS','myOwnedTasks');
		}else {
			var isValid = true;
			var taskJsonObj = [];
			if(operation == 'delete' || operation == 'terminate'){
				if ( !Array.prototype.forEach ) {
					for(var index=0;index<rowid.length;index++){
						var item = rowid[index];
						var lastOperationPerformed = gridObj.jqGrid('getCell', item, 'lastOperationPerformed');
						var isForStartNodeTask = gridObj.jqGrid('getCell', item, 'isForStartNodeTask');
						var processDefinitionId = gridObj.jqGrid('getCell',item, 'processDefinitionId'); 
						if(lastOperationPerformed != 'terminate' && lastOperationPerformed != 'withdraw' && lastOperationPerformed != operation){
						taskJsonObj.push({taskId: gridObj.jqGrid('getCell', item, 'id'),executionId: gridObj.jqGrid('getCell', item, 'executionId'),processDefinitionId: gridObj.jqGrid('getCell', item, 'processDefinitionId'),
						resourceName: gridObj.jqGrid('getCell', item, 'resourceName'),status:gridObj.jqGrid('getCell', item, 'lastOperationPerformed')});
						}
					}
				} else {
					rowid.forEach(function(item) {
						var lastOperationPerformed = gridObj.jqGrid('getCell', item, 'lastOperationPerformed');
						var isForStartNodeTask = gridObj.jqGrid('getCell', item, 'isForStartNodeTask');
						var processDefinitionId = gridObj.jqGrid('getCell', item, 'processDefinitionId');
						if(lastOperationPerformed != 'terminate' && lastOperationPerformed != 'withdraw' && lastOperationPerformed != operation){
						taskJsonObj.push({taskId: gridObj.jqGrid('getCell', item, 'id'),executionId: gridObj.jqGrid('getCell', item, 'executionId'),processDefinitionId: gridObj.jqGrid('getCell', item, 'processDefinitionId'),
							resourceName: gridObj.jqGrid('getCell', item, 'resourceName'),status:gridObj.jqGrid('getCell', item, 'lastOperationPerformed')});
						}
					});
				}
			}else {
				for(var i=0;i<rowid.length;i++){
					var lastOperationPerformed = gridObj.jqGrid('getCell', rowid[i], 'lastOperationPerformed');
					var isForStartNodeTask = gridObj.jqGrid('getCell', rowid[i], 'isForStartNodeTask');
					var resourceName = gridObj.jqGrid('getCell', rowid[i], 'resourceName');
					var taskId = gridObj.jqGrid('getCell', rowid[i], 'id');
					var processDefinitionId = gridObj.jqGrid('getCell', rowid[i], 'processDefinitionId');
					if(lastOperationPerformed != 'terminate' && lastOperationPerformed != 'withdraw' && lastOperationPerformed != operation && isForStartNodeTask != 'true'){
						taskJsonObj.push({
							taskId: gridObj.jqGrid('getCell', rowid[i], 'id'),
							executionId: gridObj.jqGrid('getCell', rowid[i], 'executionId'),
							resourceName: resourceName,
							workflowAdmin: gridObj.jqGrid('getCell',rowid[i],'workflowAdmin'),
							taskId:taskId,
							processDefinitionId: processDefinitionId 
						});
					}else {
						taskJsonObj = [];
						break;
					}
				}
			}
			if(taskJsonObj.length == 0){
				isValid = false;
				var message = form.msg.operationError;
//				if(operation == 'suspend'){
//					message = 'All or some of the instances may be '+ operation+ 'ed or terminated or Start Node Task';
//				}
				validateMessageBox(form.title.message,message);
			}
			if(isValid){
				$.msgBox({
				    title: form.title.confirm,
				    content: "确定要进行该操作吗？",
				    type: "confirm",
				    buttons: [{ value: "Yes" }, { value: "No" }],
				    success: function (result) {
						if (result == "Yes") {
							var jsonValues = JSON.stringify(taskJsonObj);
							params = 'taskDetails='+jsonValues;
							if(operation == 'delete'){
								document.location.href = "#bpm/of/deleteTasks";
							}else if(operation == 'terminate'){
								document.location.href = "#bpm/of/terminateTasks";
							}else if(operation == 'suspend'){
								document.location.href = "#bpm/of/suspendTasks";
							}
							_execute('target',params);							
						}else {
					    	return false;
					    }

				    }
				});	
			}
		}
	}
}


function confirmDeleteForm(url, id, formName,moduleId,isEdit,isSystemModule,isJspForm,version) {
	var params = "";
	var content = "";
	var isDeleted=true;
	var status=false;
	var message="";
	var redirectUrl="";

	if (id == "null") {
		params = "formName=" + formName;
		
		if(isJspForm=='true'){
			content = form.msg.confirmToDeleteJsp + formName + " ?";
		}else{
		content = form.msg.confirmToDeleteAllVersionsOfForm + formName + " ?";
		}
		
	} else {
		params = "formId=" + id + "&formName=" + formName+"&moduleId="+moduleId+"&isEdit="+isEdit+"&isSystemModule="+isSystemModule+"&version="+version;
		content = "Are you sure you want to delete this version ?";
	}

	$.msgBox({
		title : form.title.confirm,
		content : content,
		type : "confirm",
		buttons : [ {
			value : "Yes"
		}, {
			value : "No"
		} ],
		success : function(result) {
			if (result == "Yes") {
				status=true;
					$.ajax({
						type : 'GET',
						async : false,
						url : url+"?"+params,
						success : function(response) {
							if (response.status=="success") {
								isDeleted = true;
							} else {
								isDeleted = false;
							}
							message=response.message;
							redirectUrl=response.redirectUrl;
						}
					});
			} else {
				isDeleted=false;			
			}
		},
		afterClose : function() {
			if(status){
				deleteForm(message, isDeleted, moduleId,isEdit,isSystemModule,redirectUrl,formName,id,isJspForm,version);
			}
		}
	});
}

function deleteForm(message, isDeleted, moduleId,isEdit,isSystemModule,redirectUrl,formName,id,isJspForm,version){

	if (id == "null") {
		var params = 'moduleId='+moduleId+'&isEdit='+isEdit+'&isSystemModule='+isSystemModule+'&isJspForm='+isJspForm;
	}else{
		var params = 'moduleId='+moduleId+'&formName='+formName+'&isEdit='+isEdit+'&isSystemModule='+isSystemModule+'&version='+version;
	}
	if (isDeleted) {
		$.msgBox({
			title : form.title.success,
			content : message,
			type : "success",
			afterClose : function() {
				document.location.href = redirectUrl;
		     	_execute('rightPanel',params); 
			}
		});
	} else {
		$.msgBox({
			title : form.title.error,
			content : message,
			type : "error"
		});
	}
}

function _deleteFormDesignById(formId, formName, moduleId, isDelete) {
	var formIds = [ formId ];
	var content = "";
	if (isDelete == "true") {
		content = form.msg.confirmDelete + formName + form.msg.form;
	} else {
		content = form.msg.confirmToRestore + formName + form.msg.form;
	}
	var contentResponse = "";
	var isDeleted = false;
	var isToDelete = true;
	$.msgBox({
				title : form.title.confirm,
				content : content,
				type : "confirm",
				buttons : [ {
					value : "Yes"
				}, {
					value : "No"
				} ],
				success : function(result) {
					if (result == "Yes") {
						$.ajax({
									type : 'POST',
									async : false,
									url : "/bpm/listView/softDeleteTableDataAndRestore?deleteTblIds="
											+ JSON.stringify(formIds)
											+ "&tableName=ETEC_RE_FORM&columnName=id&isDelete="
											+ isDelete,
									success : function(response) {
										if (response.success) {
											if (isDelete == "true") {
												contentResponse = formName
														+ form.msg.deletedSuccess;
											} else {
												contentResponse = formName
														+ form.msg.restoredSuccess;
											}
											isDeleted = true;
										} else {
											if (isDelete == "true") {
												contentResponse = form.msg.problemWhileDelete	+ formName + form.msg.formm;
											} else {
												contentResponse = form.msg.problemWhileRestore + formName + form.msg.formm;
											}
										}
									}
								});
					} else {
						isToDelete = false;
					}
				},
				afterClose : function() {
					if (isToDelete) {
						deleteFormDetails(contentResponse, isDeleted, moduleId);
					}
				}

			});
}

function deleteFormDetails(contentResponse, isDeleted, moduleId) {
	if (isDeleted) {
		$.msgBox({
			title : form.title.success,
			content : contentResponse,
			type : "success",
			afterClose : function() {
				var params = 'moduleId=' + moduleId;
				document.location.href = "#bpm/form/formListForModule";
				_execute('rightPanel', params);
			}
		});
	} else {
		$.msgBox({
			title : form.title.error,
			content : contentResponse,
			type : "error"
		});
	}
}

function _restoreForm(cellValue, options, rawObject) {
	if (rawObject.active == "true") {
		return "<b>Active</b>";
	} else {
		return '<a id="restore_form" href="javascript:void(0)" data-role="button" data-theme="b" onclick="restoreFormDialog(\'Restore Form Version\',\''
				+ rawObject.id
				+ '\',\''
				+ rawObject.formName
				+ '\',\''
				+ rawObject.module
				+ '\',\''
				+ rawObject.isEdit
				+'\',\''
				+ rawObject.isSystemModule
				+'\')"><b>Restore</b></a>';
	}
}

function restoreFormDialog(title, formId, formName,moduleId,isEdit,isSystemModule) {
	params = 'formId=' + formId + '&formName=' + formName+'&moduleId='+moduleId+'&isEdit='+isEdit+'&isSystemModule='+isSystemModule;
	$.msgBox({
				title : title,
				content : form.msg.activeFormWillBeDeactivatedConfirmToActivateThisFormVersion,
				type : "confirm",
				buttons : [ {
					value : "Yes"
				}, {
					value : "No"
				} ],
				success : function(result) {
					if (result == "Yes") {
						document.location.href = "#bpm/form/restoreForm";
						_execute('rightPanel', params);
					} else {
						return false;
					}
				}
			});
}

function _startProcessInstances(cellValue, options, rawObject) {
	return '<a id="startProcessInstances" href="javascript:void(0)" data-role="button" data-theme="b" onclick="startProcessWithOrganizer(\''
			+ rawObject.id
			+ '\',\''
			+ rawObject.hasStartFormKey
			+ '\',\''
			+ rawObject.isSystemDefined + '\',null,false);"><b>Start</b></a>';
}

function startProcessWithOrganizer(processKey, hasStartFormKey, isSystemDefined,processInstanceId,isFromDoneList,isFormReadOnly) {

	if(isFormReadOnly == undefined || isFormReadOnly =='undefined' ){
		isFormReadOnly=	"&formReadOnly=false";
	}
	$.ajax({
		type : 'GET',
		async : false,
		url : 'bpm/of/defaultStartProcessInstance?processKey='+encodeURI(processKey),
		cache : false,
		success : function(response) {
			if (response.needOrganizerPopup == 'true') {
				showNextTaskAddMembers(response.taskId, 'defaultTask',
						'Select Transactors for task', "", "", "", "",
						response.nodeType, "", false);
			} else {
				var url = "showTaskFormDetail?taskId=" + response.taskId+ "&suspendState=1&isStartNodeTask=true"+ isFormReadOnly + "&creator="+ "&lastOperationPerformed=create&gridType=gridType";
				_execute('target', url);
				var win = window.open(url, '_blank');
				win.focus();
			}
		}
	});
}

function startDefaultProcess(processKey,isDefault) {
	//remove start node form from process
	$.ajax({
		type : 'GET',
		async : false,
		url : 'bpm/of/defaultStartProcessInstance?processKey='+encodeURI(processKey),
		cache : false,
		success : function(response) {
			if (response.needOrganizerPopup == 'true') {
				showNextTaskAddMembers(response.taskId, 'defaultTask',
						'Select Transactors for task', "", "", "", "",
						response.nodeType, "", false);
			} else {
				var url = "showTaskFormDetail?taskId=" + response.taskId+ "&suspendState=1&isStartNodeTask=true&creator="+ "&lastOperationPerformed=create&gridType=gridType&isDefault="+isDefault;
				_execute('target', url);
				var win = window.open(url, '_blank');
				win.focus();
			}
		}
	});

}

function _editProcess(cellValue, options, rawObject) {
	return '<a id="startProcessInstances" href="#bpm/process/processEditor" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+ rawObject.id
			+ '\&key='
			+ rawObject.key
			+ '\&name='
			+ rawObject.name
			+ '\&description='
			+ rawObject.description
			+ '\&classificationId='
			+ rawObject.classificationId
			+ '\');"><b>'
			+ rawObject.name + '</b></a>';
}
function _claim(cellValue, options, rawObject) {
	if (rawObject.taskClaimable == true)
		return '<a id="claim" href="#bpm/manageTasks/claimTask" data-role="button" data-theme="b" onclick="_execute(\'target\',\'taskId='
				+ rawObject.id + '\');"><b>签收</b></a>';

	else
		return '';
}

function claimTask(taskId) {
	document.location.href = "#bpm/manageTasks/claimTask";
	var params = 'taskId=' + taskId;
	_execute('target', params);
}

function _processInstances(cellValue, options, rawObject) {
	if (rawObject.noOfInstacnes > 0)
		return '<a id="processInstances" href="javascript:void(0);" data-role="button" data-theme="b" onclick="showProcessInstanceDetail(\''
				+ rawObject.id
				+ '\',\'totalInstances\',\''
				+ rawObject.processGridType
				+ '\');"><b>'
				+ rawObject.noOfInstacnes + '</b></a>';
	else
		return '0';
}
	

function showTaskFormDetailForDoneList(taskId,suspendState,lastOpPerformed,processInstanceId,resourceName,processDefinitionId,canActivate,isFormReadOnly) {
	var gridType = window.location.hash.split('/')[2];

	var formReadOnly="&formReadOnly";
	
	if(isFormReadOnly=="true"){
		formReadOnly=formReadOnly+"=true";
	}else{
		formReadOnly=formReadOnly+"=false";
	}
	
	if(taskId == processInstanceId){
		startProcessWithOrganizer(processDefinitionId, 'true', 'false',processInstanceId,true,formReadOnly);
	}else {
		var url = "showTaskFormDetail?taskId="+taskId+"&suspendState="+suspendState+"&isStartNodeTask=false"+formReadOnly+"&creator=&lastOperationPerformed="+lastOpPerformed+"&gridType="+gridType;

		_execute('task_target', url);
		var win=window.open(url, '_blank');
		win.focus();
	}
}
function showTaskFormDetail(taskId,suspendState,lastOpPerformed,processInstanceId,resourceName,processDefinitionId,creator,gridName) {
	var gridType = window.location.hash.split('/')[2];
	if(gridType == "showListViewGrid" || gridName == "workflow") {
		gridType = gridName;
	}
	if (lastOpPerformed && (lastOpPerformed == 'suspend' || lastOpPerformed == 'withdraw' || suspendState == "3" || suspendState == "2" )) {
		var operationMesage = "暂停";
		if (lastOpPerformed == 'withdraw' || suspendState == "3") {
			operationMesage = "撤办";
		}
		
		var confirmMessage = "该任务处于 " + operationMesage + " 状态，确认要重新激活该任务吗？";
		activateTaskByProcessInstanceId(taskId, processInstanceId,gridType, confirmMessage, resourceName,processDefinitionId);
	} else {
		if (taskId == processInstanceId) {
			var params = "nextTaskOrganizers=&nextTaskCoordinators=&nextTaskOrganizerOrders=&processKey="
					+ processDefinitionId
					+ "&isSystemDefined=false&processInstanceId="
					+ processInstanceId
					+ "&isFromDoneList=false&creator="
					+ creator;
			_execute("target", params);
			document.location.href = "#bpm/manageProcess/startProcessInstance";
		} else {
			// remove start node form from process
			var url = "showTaskFormDetail?taskId=" + taskId + "&suspendState="
					+ suspendState + "&isStartNodeTask=false&creator="
					+ creator + "&lastOperationPerformed=" + lastOpPerformed
					+ "&gridType=" + gridType;
			_execute('task_target', url);
			var win = window.open(url, '_blank');
			win.focus();
		}
	}
}

function showTaskFormDetailForToRead(taskId,isRead,suspensionState) {
	/*document.location.href = "#bpm/manageTasks/showTaskFormDetailForReader";
	params = 'taskId=' + taskId+'&isRead='+isRead;
	_execute('target', params);*/
	var gridType = window.location.hash.split('/')[2];
	if(isRead == "read") {
		var url = "showTaskFormDetailForReader?taskId="+taskId+'&isRead='+isRead+'&suspensionState='+suspensionState+"&gridType="+gridType;
	} else {
		var url = "showTaskFormDetailForReader?taskId="+taskId+'&isRead='+isRead+'&suspensionState='+suspensionState+"&gridType="+gridType;
	}
    _execute('task_target', url);
	var win=window.open(url, '_blank');
	win.focus();
}

function _showTaskDetail(cellValue, options, rawObject) {
	if(rawObject.id == rawObject.processInstanceId){
		return cellValue;
	}else {
		return '<a id="showTaskDetail" href="#bpm/manageTasks/showTaskDetail" data-role="button" data-theme="b" onclick="_execute(\'target\',\'taskId='
		+ rawObject.id + '\');"><b>' + cellValue + '</b></a>';	
	}
}

function _showOlderForm(cellValue, options, rawObject) {
	return '<a id="showOlderForm" href="#bpm/manageTasks/showOlderTaskForm" data-role="button" data-theme="b" onclick="_execute(\'target\',\'taskId='
			+ rawObject.id + '\');"><b>Show</b></a>';
}

function _showFormOnly(cellValue, options, rawObject) {
		return '<a id="taskInfo" href="javascript:void(0);" data-role="button" data-theme="b" onclick="showFormOnly(\''
		+ rawObject.id + '\',\''+ rawObject.suspensionState + '\',\''+ rawObject.lastOperationPerformed + '\',\''+ rawObject.executionId + '\',\''
		+ rawObject.resourceName + '\',\''+ rawObject.processDefinitionId + '\',\''+ rawObject.owner + '\');"><b>'+rawObject.processName+'</b></a> ';
}

function showFormOnly(taskId,suspendState,lastOpPerformed,processInstanceId,resourceName,processDefinitionId,creator,gridName) {
	var url = "showTaskFormDetail?taskId="+taskId+"&suspendState="+suspendState+"&isStartNodeTask=false&creator="+creator+"&lastOperationPerformed="
	+lastOpPerformed+"&gridType="+gridName+"&isDefault=true";
	_execute('task_target', url);
	var win = window.open(url, '_blank');
	win.focus();
}

function _deployProcess(cellValue, options, rawObject) {
	return '<a href="javascript:void(0);" class="hyperlink" onclick="javascript:deployProcess(\''
			+ rawObject.id + '\');"><b>Deploy</b></a>';
}

function _startProcess(cellValue, options, rawObject) {
	return '<a href="javascript:void(0);" class="hyperlink" onclick="javascript:startProcess(\''
			+ rawObject.id + '\');"><b>Start</b></a>';
}

function restoreProcessDialog(title, processId, processKey, activeProcessId) {
	params = 'processId=' + encodeURI(processId) + '&processKey=' + encodeURI(processKey)
			+ '&activeProcessId=' + encodeURI(activeProcessId);
	var isSuccess = false;
	var message = "";
	var isNeeded=true;
	$.msgBox({
				title : title,
				content : form.msg.activeFormWillBeDeactivatedConfirmToActivateThisProcessVersion,
				type : "confirm",
				buttons : [ {
					value : "Yes"
				}, {
					value : "No"
				} ],
				success : function(result) {
					if (result == "Yes") {
						$.ajaxSetup({ cache:false});
						$.ajax({
			    			type : 'GET',
			    			async : false,
						cache:false,
			    			url : 'bpm/manageProcess/restoreProcessVersionAjax?'+params,
			    			success : function(response) {
			    				if(response.successMsg){
			    					message = response.successMsg;
			    					isSuccess = true;
			    					var params = [{processKey:response.processKey}];
			    					showListViewsWithContainerAndParam("PROCESS_LIST_VERSION", "Process List Version", 'target', params);
			    				}else{
			    					message = response.errorMsg;
			    				}
			    			}
			    		});
						
					} else {
						isNeeded=false;
						return false;
					}
				},
			    afterClose : function(){
			    	if(isNeeded){
				    	if(isSuccess){
				    		validateMessageBox(form.title.success, message , "success");
						}else{
							validateMessageBox(form.title.error, message , "error");
						}
			    	}
			    }
			});
}

function deleteSelectedVersion(){
	var rowid =  gridObj.getGridParam('selarrrow');
	if(rowid.length == 0){
		validateMessageBox(form.title.message, form.msg.selectAtleastSingleWordToDelete, false);
		
	}
	if(rowid.length != 0){
		var processId = "";
		var processKey = "";
		var isActive = false;
		rowid.forEach(function(item) {
			var status = gridObj.jqGrid('getCell', item, 'Status');
			if(status != "Active"){
			var id = gridObj.jqGrid('getCell', item, 'id');
			var key = gridObj.jqGrid('getCell', item, 'Key');
			if(processId == ""){
				processId = id;
				processKey = key;
			} else {
				processId += ","+id;
			}		
			}else{
				isActive=true;
			}
		});
			if(isActive==true){
			validateMessageBox(form.title.message, form.msg.unaleToDeleteActiveVersions, false);
			
			} else{
				var params = 'processId=' + processId + '&processKey='
				+ processKey;
				deleteVersionConfirm(params);
			}
	}
}

function deleteVersionConfirm(params) {
	var isSuccess = false;
	var message = "";
	var isNeeded=true;
	$.msgBox({
				title : form.title.confirm,
				content : form.msg.confirmToDeleteThisProcessVersion,
				type : "confirm",
				buttons : [ {
					value : "Yes"
				}, {
					value : "No"
				} ],
				success : function(result) {
					if (result == "Yes") {
						$.ajaxSetup({ cache:false});
						$.ajax({
			    			type : 'GET',
						cache:false,
			    			async : false,
			    			url : 'bpm/manageProcess/deleteProcessVersionAjax?'+params,
			    			success : function(response) {
			    				if(response.successMsg){
			    					message = response.successMsg;
			    					isSuccess = true;
			    					var params = [{processKey:response.processKey}];
			    					showListViewsWithContainerAndParam("PROCESS_LIST_VERSION", "Process List Version", 'target', params);
			    				}else{
			    					message = response.errorMsg;
			    				}
			    			}
			    		});
						
					} else {
						isNeeded=false;
						return false;
					}
				},
			    afterClose : function(){
			    	if(isNeeded){
				    	if(isSuccess){
				    		validateMessageBox(form.title.success, message , "success");
						}else{
							validateMessageBox(form.title.error, message , "error");
						}
			    	}
			    }
			});
}

function _deleteAllProcessVersions(cellValue, options, rawObject) {
	return '<a id="deleteAllProcessVersions" href="javascript:void(0);" data-role="button" data-theme="b" onclick="javascript:deleteAllVersionConfirm(\''
			+ rawObject.key + '\');"><b>Delete</b></a>';

}

function deleteAllVersionConfirm(processKey) {
	params = 'processKey=' + processKey;
	$.msgBox({
				title : form.title.confirm,
				content : form.msg.confirmToDeleteAllPricessDefinitions,
				type : "confirm",
				buttons : [ {
					value : "Yes"
				}, {
					value : "No"
				} ],
				success : function(result) {
					if (result == "Yes") {
						document.location.href = "#bpm/manageProcess/deleteAllProcessVersions";
						_execute('target', params);
					} else {
						return false;
					}
				}
			});
}

function completeTask() {
	var url = "bpm/manageTasks/completeTask";
	_execute(url, 'target', '');
}

function _showVersions(cellValue, options, rawObject) {
	if(rawObject.isEdit=='true'){
		return '<a id="showVersions" href="#bpm/form/showAllFormVersions" data-role="button" data-theme="b" onclick="_execute(\'rightPanel\',\'formName='
		+ rawObject.formName +'\&moduleId='+rawObject.module+'\&isEdit='+rawObject.isEdit+'\&isSystemModule='+rawObject.isSystemModule+'\');"><b>Show Versions</b></a>';
	}else{
		return "<span style='cursor:url(\"/images/Cancel_icon.png\"),url(\"/images/Cancel_icon.png\"),auto'>Show Versions</span>";
	}
	
}

function _generateAutoId(cellValue, options, rawObject) {
	var id = rawObject.id;
	return '<a id="generateAutoId" href="javascript:formAutoGenerationId(\''
			+ id
			+ '\',\''
			+ rawObject.autoGenerationId
			+ '\')" data-role="button" data-theme="b"><b>Count Rule</b></a>';
}

function _showProcessVersions(cellValue, options, rawObject) {
	return '<a href="javascript:void(0);" data-role="button" data-theme="b" onclick="__showProcessVersions(\''+rawObject.key+'\');"><b>查看</b></a>';
}

function __showProcessVersions(key){
	
	var params = [{
        processKey: encodeURI(key)
    }];
	
	showListViewsWithContainerAndParam('PROCESS_LIST_VERSION', 'Process List Version', 'target', params);
}

// show the Suspend or Activate page
function _suspensionProcess(cellValue, options, rawObject) {
	if (rawObject.suspensionState == 2) {
		return '<a id="suspensionProcess" href="javascript:void(0);" data-role="button" data-theme="b" onclick="suspensionDialogActivate(\''
				+ rawObject.id + '\',\'Single\');"><b>Activate</b></a>';

	} else {
		return '<a id="suspensionProcess" href="javascript:void(0);" data-role="button" data-theme="b" onclick="suspensionDialogSuspend(\''
				+ rawObject.id + '\',\'Single\');"><b>Suspend</b></a>';
	}
}

function suspensionDialogActivate(processId, type) {
	var content;
	// show the content in dialogue according to screen
	if (type.indexOf('Bulk') != -1) {
		content = "Do you want to activate these Process Definitions?";
	} else {
		content = "Do you want to activate this Process Definition?";
	}
	params = 'processId=' + processId + '&suspensionState=' + 2;
	$.msgBox({
				title : form.title.confirm,
				content : content,
				type : "confirm",
				buttons : [ {
					value : "Yes"
				}, {
					value : "No"
				} ],
				success : function(result) {
					if (result == "Yes") {
						document.location.href = "#bpm/manageProcess/suspensionProcess";
						_execute('target', params);
					} else {
						return false;
					}
				}
			});
}

function suspensionDialogSuspend(processId, type) {
	var content;
	// show the content in dialogue according to screen
	if (type.indexOf('Bulk') != -1) {
		content = "Do you want to suspend these Process Definition?";
	} else {
		content = "Do you want to suspend this Process Definition?";
	}
	params = 'processId=' + processId + '&suspensionState=' + 1;
	$.msgBox({
				title : form.title.confirm,
				content : content,
				type : "confirm",
				buttons : [ {
					value : "Yes"
				}, {
					value : "No"
				} ],
				success : function(result) {
					if (result == "Yes") {
						document.location.href = "#bpm/manageProcess/suspensionProcess";
						_execute('target', params);
					} else {
						return false;
					}
				}
			});
}

function removeMemberPage(userId, taskId, identityLinkType, title) {
	var params = 'userId=' + userId + '&taskId=' + taskId
			+ '&identityLinkType=' + identityLinkType;
	$.msgBox({
		title : form.title.confirm,
		content : form.msg.confirmToRemoveMember,
		type : "confirm",
		buttons : [ {
			value : "Yes"
		}, {
			value : "No"
		} ],
		success : function(result) {
			if (result == "Yes") {
				document.location.href = "#bpm/TaskDetails/removeMember";
				_execute('target', params);
			} else {
				return false;
			}
		}
	});
}

function assignSubstitute(substitute) {
	var rowid = gridObj.getGridParam('selarrrow');
	var taskIds = "";
	var i = 1;
	rowid.forEach(function(item) {
		var taskId = gridObj.jqGrid('getCell', item, 'id');
		if (i == 1) {
			taskIds = taskId;
		} else {
			taskIds = taskIds + "," + taskId;
		}
		i++;

	});
	if (rowid.length != 0) {
		$.msgBox({
					title : form.title.confirm,
					content : "确定让 " + substitute + " 代办已选的任务？",
					type : "confirm",
					buttons : [ {
						value : "Yes"
					}, {
						value : "No"
					} ],
					success : function(result) {
						if (result == "Yes") {
							var params = 'taskId=' + taskIds + '&assignee='
									+ substitute;
							document.location.href = "#bpm/manageTasks/assignSubstitute";
							_execute('target', params);
						} else {
							return false;
						}
					}
				});
	} else {
		validateMessageBox(form.title.message, "请选择要操作的任务", false);
	}
}

function getUsersToInvolve(taskId, type,resourceName) {
	clearPreviousPopup();
	var params = 'taskId=' + taskId + '&type=' + type +"&resourceName="+resourceName;
	setTimeout(function(){
		document.location.href = "#bpm/of/showAddMembers";
		// openSelectDialog(params,"250","125",type);
		_execute('involveUsersPopupDialog', params);
	},200);
	$myDialog = $("#involveUsersPopupDialog");
	$myDialog.dialog({
		width : 'auto',
		height : 'auto',
		modal : true,
		title : type
	});
	$myDialog.dialog("open");
}

function getUsersToInvolve(taskId, type, title,resourceName,processDefinitionId) {
	clearPreviousPopup();
	var params = 'taskId=' + taskId + '&type=' + type+"&resourceName="+resourceName+'&processDefinitionId='+processDefinitionId;
	setTimeout(function(){
		document.location.href = "#bpm/of/showAddMembers";
		// openSelectDialog(params,"250","125",type);
		_execute('involveUsersPopupDialog', params);
	},200);
	$myDialog = $("#involveUsersPopupDialog");
	$myDialog.dialog({
		width : 'auto',
		height : 'auto',
		modal : true,
		title : title
	});
	$myDialog.dialog("open");
}

function getUsersToInvolve(taskId, type, title, formId,resourceName,nodeType,processDefinitionId,processInsId) {
	clearPreviousPopup();
	var params = 'taskId=' + taskId + '&type=' + type + '&formId=' + formId+"&resourceName="+resourceName+"&nodeType="+nodeType+'&processDefinitionId='+processDefinitionId+'&processInsId='+processInsId;
	setTimeout(function(){
		document.location.href = "#bpm/of/showAddMembers";
		// openSelectDialog(params,"250","125",type);
		_execute('involveUsersPopupDialog', params);
	},200);
	$myDialog = $("#involveUsersPopupDialog");
	$myDialog.dialog({
		width : 'auto',
		height : 'auto',
		modal : true,
		title : title
	});
	$myDialog.dialog("open");
}

function usersTreeList(title, id, selectType) {
	$("div#userPopupDialog").empty();
	$("div#departmentPopupDialog").empty();
	$("div#groupPopupDialog").empty();
	$("#popupDialog").dialog("destroy");
	// $("#involveUsersPopupDialog").dialog("destroy");
	$("#userPopupDialog").dialog("destroy");
	$("#userPopupDialogManager").dialog("destroy");
	$("#userPopupDialogSecretary").dialog("destroy");
	$("#departmentPopupDialog").dialog("destroy");
	$("#partTimeDepartmentPopupDialog").dialog("destroy");
	$("#groupPopupDialog").dialog("destroy");
	var params = 'title=' + title + '&id=' + id + '&selectType=' + selectType;
	document.location.href = "#bpm/admin/usersTreeList";
	_execute('userPopupDialog', params);
	$myDialog = $("#userPopupDialog");
	$myDialog.dialog({
		width : 'auto',
		height : 'auto',
		modal : true,
		title : title
	});
	$myDialog.dialog("open");
}

function usersTreeListManager(title, id, selectType) {
	var params = 'title=' + title + '&id=' + id + '&selectType=' + selectType;
	document.location.href = "#bpm/admin/usersTreeList";
	_execute('userPopupDialogManager', params);
	$myDialog = $("#userPopupDialogManager");
	$myDialog.dialog({
		width : 'auto',
		height : 'auto',
		modal : true,
		title : title
	});
	$myDialog.dialog("open");
}

function usersTreeListSecretary(title, id, selectType) {
	var params = 'title=' + title + '&id=' + id + '&selectType=' + selectType;
	document.location.href = "#bpm/admin/usersTreeList";
	_execute('userPopupDialogSecretary', params);
	$myDialog = $("#userPopupDialogSecretary");
	$myDialog.dialog({
		width : 'auto',
		height : 'auto',
		modal : true,
		title : title
	});
	$myDialog.dialog("open");
}

function formAutoGenerationId(formId, autoGeneratedId) {
	// $("#formAutoIdGeneration").dialog("destroy");
	$("div#formAutoGenerationId").empty();
	var title = "Form Auto Id Generation";
	var params = 'formId=' + formId + '&autoGeneratedId=' + autoGeneratedId;
	document.location.href = "#bpm/form/formAutoGenerationId";
	_execute('formAutoGenerationId', params);
	$myDialog = $("#formAutoGenerationId");
	$myDialog.dialog({
		width : 'auto',
		height : 'auto',
		modal : true,
		position : [ ($(window).width() / 3), 150 ],
		title : title
	});
	$myDialog.dialog("open");
}

function _showInstanceDetail(cellValue, options, rawObject) {
	return '<a id="showInstanceDetail" href="#bpm/manageProcess/showInstanceDetail" data-role="button" data-theme="b" onclick="_execute(\'target\',\'instanceId='
			+ rawObject.id + '\');"><b>' + cellValue + '</b></a>';
}

function _deleteProcessInstance(cellValue, options, rawObject) {
	return '<a id="deleteProcessInstance" href="javascript:void(0);" data-role="button" data-theme="b" onclick="deleteProcessInsDialog(\''
			+ rawObject.id + '\' ,\''+ rawObject.processDefinitionId + '\',\''+ rawObject.operationPerformed + '\');">删除</a>';
}

//Bulk Delete in My Instance Grid
function deleteMultipleProcessInstances(){
	var rowid =  gridObj.getGridParam('selarrrow');
	var operationPerformed =new Array();
	var i=0;
	if ( !Array.prototype.forEach ) {
		for(var index=0;index<rowid.length;index++){
			var operation = gridObj.jqGrid('getCell', rowid[index] , 'operationPerformed');
			operationPerformed[i] = operation;
			i++;
		}
	} else {
		rowid.forEach(function(item) {
		var operation = gridObj.jqGrid('getCell', item, 'operationPerformed');
		operationPerformed[i] = operation;
		i++;
		});
	}
	
	
	if(rowid.length ==0){
		validateMessageBox(form.title.message, form.msg.selectAtleastSingleProcessInstanceToDelete, false);
		
	} else {
		if(operationPerformed == true){
		validateMessageBox(form.title.message, form.msg.contDeleteAnyOfSelectedInstanceIsOperated, false);
			
		}else{
			deleteAllMultipleProcessInstances(rowid);
		}
	}
}

function deleteAllMultipleProcessInstances(processInsId){
	var params = 'processInsId=' + processInsId + '&reason=Bulk Delete'; 
	var message = "";
	$.msgBox({
	    title: form.title.confirm,
	    content: form.msg.confirmToDeleteSelectedProcessInstances,
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
				$.ajax({
					type : 'GET',
					async : false,
					url : 'bpm/process/deleteProcessInstanceAjax?'+ params,
						success : function(response) {
							if (response.successMsg) {
								message = response.successMsg;
						    	setTimeout(function(){
									validateMessageBox(form.title.success, message, "success");
									document.location.href = "#bpm/process/myInstances";
									_execute('target', '');
						    	},500);
								
							} else {
								message = response.errorMsg;
							}
						}
				});
	        }else{
	  		  return false;
	  	  }
	    }
	});
}


function deleteProcessInsDialog(id, processDefId, operationPerformed) {
	var message = "";
	if (operationPerformed == "false") {
		$.msgBox({
			title: form.title.confirm,
			content : form.msg.confirmToDeleteThisProcessInstance,
			type: "confirm",
		    buttons: [{ value: "Yes" }, { value: "No" }],
			success : function(result, values) {
				if (result == "Yes") {
					params = 'processInsId=' + id ; 
					$.ajax({
						type : 'GET',
						async : false,
						url : 'bpm/process/deleteProcessInstanceAjax?'+ params,
							success : function(response) {
								if (response.successMsg) {
									message = response.successMsg;
							    	setTimeout(function(){
										validateMessageBox(form.title.success, message, "success");
										document.location.href = "#bpm/process/myInstances";
										_execute('target', '');
							    	},500);
								} else {
									message = response.errorMsg;
								}
							}
					});
				}else{
			  		  return false;
			  	  }
			}
		});
	} else {
		var message = form.msg.contDeleteSelectedProcessIsOperated
			validateMessageBox(form.title.info, message, "alert");
	}
}

function showProcessInstanceDetail(processDefinitionId, status, processGridType) {
	document.location.href = "#bpm/manageProcess/showProcessInstanceForStatus";
	params = 'processDefinitionId=' + processDefinitionId + '&status=' + status
			+ '&processGridType=' + processGridType;
	_execute('target', params);
}

function _showEditRole(cellValue, options, rawObject) {
	if (rawObject.roleType != "System") {
		return '<a id="editrole" href="#bpm/admin/editRole" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
				+ encodeURI(rawObject.roleId)+ '\');"><b>' + rawObject.name + '</b></a>';
	} else {
		return rawObject.name;
	}
}

function _showEditGroup(cellValue, options, rawObject) {
	return '<a id="editgroup" href="#bpm/admin/editGroup" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+encodeURI(rawObject.groupId)+ '\');"><b>' + rawObject.viewName + '</b></a>';
}

function _showEditDepartment(cellValue, options, rawObject) {
	//alert(rawObject.viewName+"=="+rawObject.name);
	return '<a id="editdepartment" href="#bpm/admin/departmentForm" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+ encodeURI(rawObject.departmentId)
			+ '\');"><b>'
			+ rawObject.viewName
			+ '</b></a>';
}

function callLink() {
	_execute('target', '');
}

function callLink(params) {
	_execute('target', params);
}

function _downloadProcessXml(cellValue, options, rawObject) {
	return '<a href="javascript:void(0);" onclick="javascript:xmlDownloadLink(\''
			+ rawObject.resourceName
			+ '\',\''
			+ rawObject.deploymentId
			+ '\');"><b>导出</b></a>';

}

function xmlDownloadLink(resourceName, deploymentId) {
	document.location.href = "bpm/manageProcess/processXmlDownload?resourceName="
			+ resourceName + "&deploymentId=" + deploymentId;
}
function viewXmlPopup(title, resourceName, deploymentId, name) {
	params = 'resourceName=' + resourceName + '&deploymentId=' + deploymentId
			+ '&isTitleShow=false&processName=' + name;
	openSelectDialogForFixedPosition(params, "1200", "800", name + " XML View");
	// document.location.href =
	// "bpm/manageProcess/processXmlDownload?resourceName="+resourceName+"&deploymentId="+deploymentId;
}

function _deleteFormDesigns(cellValue, options, rawObject) {
	var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
	var isEdit=selected_nodes.attr("isedit");
	 //return '<a id="deleteFormDesigns" href="#bpm/form/deleteAllFromVersions"
	 //data-role="button" data-theme="b"
	// onclick="_execute(\'target\',\'formName='+rawObject.formName+'\');"><b>Delete</b></a>';
	if(isEdit=='true' && rawObject.isSystemModule!="true"){
		return '<a id="deleteFormDesigns" href="javascript:void(0);" data-role="button" data-theme="b" onclick="confirmDeleteForm(\'bpm/form/deleteAllFromVersions\',\'null\',\''+rawObject.formName+'\',\''+rawObject.module+'\',\''+rawObject.isEdit+'\',\''+rawObject.isSystemModule+'\',\''+false+'\');"><b>Delete</b></a>';
	}else{
		return "<span style='cursor:url(\"/images/Cancel_icon.png\"),url(\"/images/Cancel_icon.png\"),auto'>Delete</span>";
	}
	
	/*<div style="cursor:url('/images/delete.gif'),url('/images/delete.gif'),auto">Delete</div>*/
	/*return '<a id="deleteFormById" href="javascript:void(0);" data-role="button" data-theme="b" onclick="_deleteFormDesignById(\''
			+ rawObject.id
			+ '\',\''
			+ rawObject.formName
			+ '\',\''
			+ rawObject.module + '\', \'true\');"><b>Delete</b></a>';*/
}
function _deleteJspForm(cellValue, options, rawObject) {
	var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
	var isEdit=selected_nodes.attr("isedit");
	if(isEdit=='true' && rawObject.isSystemModule!="true"){
		return '<a id="deleteFormDesigns" href="javascript:void(0);" data-role="button" data-theme="b" onclick="confirmDeleteForm(\'bpm/form/deleteAllFromVersions\',\'null\',\''+rawObject.formName+'\',\''+rawObject.module+'\',\''+rawObject.isEdit+'\',\''+rawObject.isSystemModule+'\',\''+true+'\');"><b>Delete</b></a>';
	}else{
		return "<span style='cursor:url(\"/images/Cancel_icon.png\"),url(\"/images/Cancel_icon.png\"),auto'>Delete</span>";
	}
	
	
}

function _deleteTableDesign(cellValue, options, rawObject) {
	return '<a id="deleteTable" href="javascript:void(0);" data-role="button" data-theme="b" onclick="confirmDeleteTable(\'#bpm/table/deleteTableById\',\''
			+ rawObject.id
			+ '\',\''
			+ rawObject.tableName
			+ '\');"><b>Delete</b></a>';
}

function confirmDeleteTable(url, id, tableName) {
	var params = "tableId=" + id;
	var content = "Are you sure you want to delete the Table: " + tableName
			+ " ?";

	$.msgBox({
		title : form.title.confirm,
		content : content,
		type : "confirm",
		buttons : [ {
			value : "Yes"
		}, {
			value : "No"
		} ],
		success : function(result) {
			if (result == "Yes") {
				document.location.href = url;
				_execute('target', params);
			} else {
				return false;
			}
		}
	});
}

function _xmlViewLink(cellValue, options, rawObject) {
	return '<a id="viewXml" href="#bpm/manageProcess/processXmlView" data-role="button" data-theme="b" onclick="_execute(\'target\',\'resourceName='
			+ encodeURI(rawObject.Resource_Name)
			+ '&processName='
			+ encodeURI(rawObject.Process_Name)
			+ '&isTitleShow=true&deploymentId='
			+ encodeURI(rawObject.Deployment_Id)
			+ '\');"><b><img src="/images/easybpm/common/exportpreview.png" /></b></a>';

}

function _previewProcess(cellValue, options, rawObject) {
	return '<a id="processPreview" href="#" data-role="button" data-theme="b" onclick="_previewProcessPopup(\'processPreviewPopupDialog\',\''
			+ encodeURI(rawObject.Process_Name)
			+ '\',\'resourceName='
			+ encodeURI(rawObject.Resource_Name)
			+ '&processName='
			+ encodeURI(rawObject.Process_Name)
			+ '&isTitleShow=true&deploymentId='
			+ encodeURI(rawObject.Deployment_Id)
			+ '\');"><img src="/images/easybpm/common/preview.png" /></a>';

}

function _showDjsdInfo(cellValue, options, rawObject) {
	return '<a id="notice" href="#bpm/admin/editNoticeForms?id='+rawObject.id+'" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+ encodeURI(rawObject.id)+ '\');"><b style="color:#428bca;text-decoration:underline">' + rawObject.title + '</b></a>';

}


function _showNoticeInfo(cellValue, options, rawObject) {
	return '<a id="notice" href="#bpm/admin/editNoticeForm?id='+rawObject.id+'" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+ encodeURI(rawObject.id)+ '\');"><b style="color:#428bca;text-decoration:underline">' + rawObject.title + '</b></a>';

}

function _showNoticeInfo1(cellValue, options, rawObject) {
	return '<a id="notice" href="#bpm/admin/editNoticeForm1?id='+rawObject.id+'" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+ encodeURI(rawObject.id)+ '\');"><b style="color:#428bca;text-decoration:underline">' + rawObject.title + '</b></a>';

}

function _showzcfgInfo(cellValue, options, rawObject) {
	return '<a id="notice" href="#bpm/admin/editZcfgForm?id='+rawObject.id+'" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+ encodeURI(rawObject.id)+ '\');"><b style="color:#428bca;text-decoration:underline">' + rawObject.title + '</b></a>';

}
function _showzcfgckInfo(cellValue, options, rawObject) {
	return '<a id="notice" href="#bpm/admin/editZcfgckForm?id='+rawObject.id+'" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+ encodeURI(rawObject.id)+ '\');"><b style="color:#428bca;text-decoration:underline">' + rawObject.title + '</b></a>';

}


function _previewProcessPopup(container, processName, requestedparams) {
	$("#"+container).html("");
	clearTreePopupDiv();
	setTimeout(function() {
		document.location.href = "#bpm/manageProcess/processPreview";
		_execute(container, requestedparams);
	}, 200);
	setTimeout(function() {
		$.myDialog = $("#" + container);
		$.myDialog.dialog({
			width : 'auto',
			height : 'auto',
			modal : true,
			title : "流程预览 - " + decodeURI(processName)
		});
		$.myDialog.dialog("open");
	}, 500);
}

function _showEditMenu(rowId, tv, rawObject, cm, rdata) {
	return '<a id="editmenu" href="#bpm/admin/menuForm" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+ rawObject.id + '\');"><b>' + rawObject.name + '</b></a>';
}

function _showEditUser(rowId, tv, rawObject, cm, rdata) {
	return '<a id="edituser" href="#bpm/admin/userform" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+ rawObject.id + '&from=list\');"><b>' + rawObject.id + '</b></a>';
}

function _showEnabled(rowId, tv, rawObject, cm, rdata) {
	var enabled = rawObject.enabled;
	if (enabled == "true") {
		return "有效";
	} else {
		return "无效";
	}
}

// Showing status column in Data Dictioanry Grid as String from boolean value
function _showIsEnabled(rowId, tv, rawObject, cm, rdata) {
	var isEnabled = rawObject.isEnabled;
	if (isEnabled == "true") {
		return "有效";
	} else {
		return "无效";
	}
}


/**
 * Description:切换状态
 * 作者 : 蒋晨 
 * 时间 : 2017-2-6 下午1:45:23
 */
function _showColor(rowId, tv, rawObject, cm, rdata) {
	var isState = rawObject.isActive;
	//console.log(isState);
	if(isState == '1'){
		//var state = "启用";
		return '<a id="changeColor" href="#bpm/admin/changeStatus" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
		+ rawObject.id + '\');"><b><font color=#A6FFA6>'+"启用"+'</font></b></a>';
		// return "<font color=lime>启用</font>";
	}else if(isState == '2'){
		return '<a id="changeColor1" href="#bpm/admin/changeStatus1" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
		+ rawObject.id + '\');"><b><font color=red>'+"停用"+'</font></b></a>';
	}
}

/**
 * Description:修改查看
 * 作者 : 蒋晨 
 * 时间 : 2017-2-6 下午2:39:04
 */
function showDetail(rowId, tv, rawObject, cm, rdata) {
	return '<a id="updateLinkList" href="#bpm/admin/modifyLink" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
	+ rawObject.id + '\');"><b><u>'+ rawObject.title + '</u></b></a>';

}

/**
 * Description:修改查看
 * 作者 : 蒋晨 
 * 时间 : 2017-2-6 下午2:39:04
 */
function showPersonDetail(rowId, tv, rawObject, cm, rdata) {
	return '<a id="updatePersonList" href="#bpm/admin/modifyPerson" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
	+ rawObject.id + '\');"><b><u>'+ rawObject.xm + '</u></b></a>';

}


/**
 * Description:修改查看
 * 作者 : 蒋晨 
 * 时间 : 2017-2-6 下午2:39:04
 */
function showEventDetail(rowId, tv, rawObject, cm, rdata) {
		return '<a id="updateEventList" href="#bpm/admin/modifyEvent" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
		+ rawObject.id + '\');"><b><u>'+ rawObject.eventName + '</u></b></a>';
}




/**
 * Description:修改查看
 * 作者 : 蒋晨 
 * 时间 : 2017-2-15 上午10:27:56
 */
function showEvidenceDetail(rowId, tv, rawObject, cm, rdata) {
	return '<a id="updateEvidenceList" href="#bpm/admin/evidenceDetail" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
	+ rawObject.id + '\');"><b><u>'+ rawObject.eventName + '</u></b></a>';

}


function showNewsImage(cellValue, options, rawObject) {
	return '<img src="' + rawObject.image + '"/>';

}

function _xmlBrowseLink(cellValue, options, rawObject) {
	return '<a id="browseXml" href="#bpm/manageProcess/browseXmlList" data-role="button" data-theme="b" onclick="xmlFileListDialog(\'File List\',\''
			+ rawObject.resourceName
			+ '\',\''
			+ rawObject.deploymentId
			+ '\');"><b>Browse</b></a>';

}

function xmlFileListDialog(title, resourceName, deploymentId) {
	params = 'resourceName=' + resourceName + '&deploymentId=' + deploymentId;
	openSelectDialog(params, "600", "200", title);
}

function _showNewsDetail(cellValue, options, rawObject) {
	return '<a id="showTaskDetail" href="#bpm/news/showDetailNews" data-role="button" data-theme="b" onclick="_execute(\'target\',\'newsInstanceId='
			+ rawObject.processInstanceId
			+ '\');"><b>'
			+ cellValue
			+ '</b></a>';
}

function _showDefaultFormDetail(cellValue, options, rawObject) {
	return '<a id="showTaskDetail" href="#bpm/default/showDefaultFormDetail" data-role="button" data-theme="b" onclick="_execute(\'target\',\'processInstanceId='
			+ rawObject.processInstanceId
			+ '\');"><b>'
			+ cellValue
			+ '</b></a>';
}

function _showNewsTemplate(cellValue, options, rawObject) {
	return '<a id="showTaskDetail" href="#bpm/news/showNewsTemplate" data-role="button" data-theme="b" onclick="_execute(\'target\',\'newsInstanceId='
			+ rawObject.processInstanceId
			+ '\');"><b>'
			+ cellValue
			+ '</b></a>';
}

function _selectedXmlDownload(cellValue, options, rawObject) {
	return '<a id="downloadXml" href="#" data-role="button" data-theme="b" onclick="javascript:selectedXmlDownloadLink(\''
			+ rawObject.fileName
			+ '\',\''
			+ rawObject.fileObj
			+ '\');"><b>Export</b></a>';

}

function selectedXmlDownloadLink(fileName, fileObj) {
	document.location.href = "bpm/manageProcess/selectedXmlDownload?fileName="
			+ fileName + "&fileObj=" + fileObj;
}

function _getProcessInfo(cellValue, options, rawObject) {
	return '<a id="processInfo" href="#bpm/manageProcess/showProcessInformation" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+ rawObject.id + '\');"><b>' + rawObject.name + '</b></a>';
}

function changeStatus() {
	document.location.href = "#bpm/manageProcess/showSuspensionProcess";
	var id = document.getElementById("id").value;
	var suspensionState = document.getElementById("suspensionState").value;
	var title;
	if (suspensionState == 2) {
		title = "Activate Process definition";
	} else {
		title = "Suspend Process definition";
	}
	suspensionDialog(title, id, suspensionState);
}

function deleteProcess() {
	var key = document.getElementById("key").value;
	deleteAllVersionConfirm(key);
}

function downloadProcess() {
	var resourceName = document.getElementById("resourceName").value;
	var deploymentId = document.getElementById("deploymentId").value;
	xmlDownloadLink(resourceName, deploymentId);
}

function viewProcess() {
	var resourceName = document.getElementById("resourceName").value;
	var deploymentId = document.getElementById("deploymentId").value;
	var params = 'resourceName=' + resourceName + '&deploymentId='
			+ deploymentId;
	document.location.href = "#bpm/manageProcess/processXmlView";
	_execute('target', params);
}

function browseProcess() {
	var resourceName = document.getElementById("resourceName").value;
	var deploymentId = document.getElementById("deploymentId").value;
	document.location.href = "#bpm/manageProcess/browseXmlList";
	xmlFileListDialog('File List', resourceName, deploymentId);
}

function _terminateProcessInstance(cellValue, options, rawObject) {
	return '<a id="processInfo" href="javascript:void(0);"  data-role="button" data-theme="b" onclick="javascript:terminateProcessInstanceConfirm(\''
	+ rawObject.id + '\' ,\''+ rawObject.processDefinitionId + '\' ,\''+ rawObject.taskId + '\' ,\''+ rawObject.resourceName + '\');"><b>Terminate</b></a>';
}

function terminateProcessInstanceConfirm(executionId, processDefId,taskId,resourceName) {
	var isSuccess = false;
	var isFailure = false;
	var message = "";
	var operationType = "terminate"
	var params = 'executionId=' + executionId + '&taskId=' +taskId + '&resourceName=' +resourceName + '&operationType=' +operationType;
	$.msgBox({
				title : form.title.confirm,
				content : form.msg.confirmToTerminateProcessInstance,
				type : "confirm",
				buttons : [ {
					value : "Yes"
				}, {
					value : "No"
				} ],
				success : function(result) {
					if (result == "Yes") {
						//document.location.href = "#bpm/process/terminateProcessInstance";
						//_execute('target', params);
						
						$.ajax({
			    			type : 'GET',
			    			async : false,
			    			url : 'bpm/process/terminateProcessInstanceAjax?'+params,
			    			success : function(response) {
			    				if(response.successMsg){
			    					message = response.successMsg;
			    					isSuccess = true;
			    					var params = [{processDefId:processDefId}];
			    					document.location.href = "#bpm/process/myInstances";
			    					_execute('target', '');
			    					//showListViewsWithContainerAndParam("PROCESS_LIST_MY_INSTANCES", "My Instances", 'target', params);
			    				}else{
			    					isFailure = true;
			    					message = response.errorMsg;
			    				}
			    			}
			    		});
						
					} else {
						return false;
					}
				},
			    afterClose : function(){
			    	if(isSuccess){
			    		validateMessageBox(form.title.success, message , "success");
					}else if(isFailure){
						validateMessageBox(form.title.error, message , "error");
					}
			    }
			});
}
function isDublicateFormName() {
	cancelFullScreenMode('target');
	var formName = $("#formName").val();
	var isFormNamePresent = false;
	if (formName != "") {
		var newdata = $.ajax({
			type : "GET",
			async : false,
			url : "bpm/form/isFormNamePresent?formName=" + formName
		}).responseText;
		var da = eval('(' + newdata + ')');
		if (da.isPresent == true) {
			setTimeout(function() {
				openMessageBox("Form Name already exists!",
						"Please give a different Form Name.", "info", true);
			}, 500);
		} else {
			isFormNamePresent = true;
		}
	} else {
		$.msgBox({
			type : "prompt",
			title : form.msg.pleaseSpecifyAFormName,
			inputs : [ {
				header : form.msg.formName,
				type : "text",
				name : "_formName"
			} ],
			buttons : [ {
				value : "Ok"
			}, {
				value : "Cancel"
			} ],
			success : function(result, values) {
				if (result == "Ok") {
					var ajaxLoaderFrame = new ajaxLoader(document
							.getElementById('ajax_loader'));
					var tempFormname = values[0].value.trim();
					if (tempFormname != "") {
						$("#formName").val(tempFormname);
						showFormPanelCheck(tempFormname, true);
					} else {
						setTimeout(function() {
							openMessageBox(form.title.error,	form.msg.formNotEmpty, "error", true);
						}, 500);
					}
					if (ajaxLoaderFrame)
						ajaxLoaderFrame.remove();
				}
			}
		});
	}
	return isFormNamePresent;
}

function showFormPanelCheck(tempFormname, isSave) {
	var e_name = new String(document.getElementById('e_name'));
	if (e_name != "null") {
		saveFormName(tempFormname, isSave);
	} else {
		setTimeout(function() {
			showFormPanel();
			showFormPanelCheck(tempFormname, isSave);
		}, 100);
	}
}

function saveFormName(tempFormname, isSave) {
	$("#e_name").val(tempFormname);
	$("#e_label").val(tempFormname);
	saveFormPanel();
	var newdata = $.ajax({
		type : "GET",
		async : false,
		url : "bpm/form/isFormNamePresent?formName=" + tempFormname
	}).responseText;

	var da = eval('(' + newdata + ')');
	if (da.isPresent == true) {
		$("#formName").val("");
		$("#e_name").val("");
		setTimeout(function() {
			openMessageBox(form.msg.formNameAlreadyExists,	form.msg.giveDifferentFormName, "info", isSave);
		}, 500);
	} else {
		if (isSave) {
			$("#saveForm").submit();
		}
	}
}

function openMessageBox(title, content, type, isSave) {
	$.msgBox({
		title : title,
		content : content,
		type : type,
		buttons : [ {
			value : "Ok"
		} ],
		success : function(result) {
			if (result == "Ok") {
				setTimeout(function() {
					if (isSave) {
						isDublicateFormName();
					} else {
						getFormNameInMsgBox();
					}
				}, 500);
			}
		}
	});
}

function validateFileUpload() {
	var fup = document.getElementById('file');
	var fileName = fup.value;
	var index = fileName.indexOf(".bpmn20.xml");
	if (index == -1) {
				validateMessageBox(form.title.message, form.msg.uploadValidFileFormate, false);
		
	} else {
		return true;
	}

}

function closeImportFileDialog(){
	if($.browser.msie != undefined && $.browser.msie && $.browser.version < 10){
	} else {
		setTimeout(function() {closeSelectDialog('importPopupDialog');},2);
	}

	_execute('target','');
    }

function validateBpmnFileUpload() {
	var fup = document.getElementById('file');
	var fileName = fup.value;
	var index = fileName.indexOf(".bpmn20.xml");
        var zipIndex = fileName.indexOf(".zip");
    	
    	
 	if (index == -1 && zipIndex == -1) {
				validateMessageBox(form.title.message, form.msg.uploadValidFileFormate, false);
	} else {
		return true;
	}
	return false;
}

function validateXlsFileUpload() {
	var screenName = document.getElementById("screenName").value;
	var params = "screenName=" + document.getElementById("screenName").value;
	_execute('target', params);
	var fup = document.getElementById('file');
	var fileName = fup.value;
	var index = fileName.indexOf(".xls");
	if (fileName == '') {
		validateMessageBox(form.title.message, form.msg.pleaseChooseFileLocation, false);
		
	}
	if (index == -1) {
						validateMessageBox(form.title.message, form.msg.uploadValidFileFormatexls, false);
	} else {
		return true;
	}
}

function validateSQLFileUpload() {
	var fup = document.getElementById('file');
	var fileName = fup.value;
	var index = fileName.indexOf(".sql");
	if (fileName == '') {
		validateMessageBox(form.title.message, form.msg.pleaseChooseFileLocation, false);
		
	} else {
		if (index == -1) {
					validateMessageBox(form.title.message, form.msg.uploadValidFileFormatesql, false);
		
		} else {
			return true;
		}
	}
	return false;
}

function validateAddMembers() {
	var userId = document.getElementById("userId").value;
	var involvePeopleCount = document.getElementById("involvePeopleCount").value;
	if (userId == '') {
		validateMessageBox(form.title.message, form.msg.availableUserShouldNotBeEmpty, false);
		
	} else {
		if (involvePeopleCount > 0) {
			var identityLinkType = document.getElementById("identityLinkType").value;
			if (identityLinkType != '') {
				for ( var i = 1; i <= involvePeopleCount; i++) {
					var peopleName = document.getElementById("peopleName_" + i).value;
					var roleMessage = document.getElementById("roleMessage_"
							+ i).value;
					if (peopleName == userId && roleMessage == identityLinkType) {
								validateMessageBox(form.title.message, form.msg.userAlreadyAddedAs + identityLinkType, false);
						
					}
				}
			}

		}
	}
}

function validateAddClassificatin() {
	
	var fname = document.getElementById('classificationName').value;
	//[\-a-zA-Z\s\u4e00-\u9eff][A-Za-z\u4e00-\u9eff\.][A-Za-z \w\u4e00-\u9eff\.]
	var regex = /^[\-a-zA-Z\u4e00-\u9eff]{1,40}$/;

	if (fname.trim() == '') {
		validateMessageBox(form.title.message, form.msg.PleaseEnterClassyName, false);
		
		return false;
		
	} else if (!regex.test(fname)) {
		validateMessageBox(form.title.message, form.msg.numbersAndSplCharsNotAllowed, false);
		
		return false;
		
	} else {
		// closeSelectDialog('popupDialog');
		validateMessageBox(form.title.success, form.msg.classificationAddedSuccessfully, "success");
		
		return true;
		
	}
}
function getFormNameInMsgBox() {
	$.msgBox({
		type : "prompt",
		title : form.msg.pleaseSpecifyAFormName,
		inputs : [ {
			header : form.msg.formName,
			type : "text",
			name : "_formName"
		} ],
		buttons : [ {
			value : "Ok"
		}, {
			value : "Cancel"
		} ],
		success : function(result, values) {
			if (result == "Ok") {
				var ajaxLoaderFrame = new ajaxLoader(document
						.getElementById('ajax_loader'));
				var tempFormname = values[0].value.trim();
				if (tempFormname != "") {
					$("#formName").val(tempFormname);
					showFormPanelCheck(tempFormname, false);
					/*
					 * showFormPanel(); setTimeout(function() {
					 * $("#e_name").val(tempFormname); saveFormPanel(); var
					 * newdata = $.ajax({ type: "GET", async:false, url:
					 * "bpm/form/isFormNamePresent?formName="+tempFormname
					 * }).responseText; var da = eval('('+newdata+')');
					 * if(da.isPresent == true){ $("#formName").val("");
					 * $("#e_name").val(""); setTimeout(function() {
					 * openMessageBox(); }, 500); }else{
					 * //$("#saveForm").submit(); } }, 300);
					 */
				} else {
					setTimeout(function() {
						openMessageBox(form.title.error, form.msg.formNotEmpty, "error",	false);
					}, 500);
				}

				if (ajaxLoaderFrame)
					ajaxLoaderFrame.remove();
			}
		}
	});
}

function cancelFullScreenMode(container) {
	var isFullScreenMode = $('#' + container).fullScreen('state');
	if (isFullScreenMode == "fullscreen") {
		if (!$('#' + container).fullScreen('disable'))
			;
		$("#" + container).removeClass('overlay');
	}
}
function activateFullScreenMode(container) {
	var isFullScreenMode = $('#' + container).fullScreen('state');
	if (isFullScreenMode == "fullscreen") {
		if (!$('#' + container).fullScreen('disable'))
			;
		$("#" + container).removeClass('overlay');
	} else {
		if (!$('#' + container).fullScreen('enable'))
			;
		$("#" + container).addClass('overlay');
	}
}

function checkIsNumber(id, fieldName) {
	var enteredNumber = document.getElementById(id).value;
	if (isNaN(enteredNumber)) {
		$.msgBox({
			title : form.title.error,
			content : "Field " + fieldName + " shoud be a number.",
			type : "error",
			buttons : [ {
				value : "Ok"
			} ],
			success : function(result) {
				document.getElementById(id).value = "";
				document.getElementById(id).focus();
			}
		});
	}
}

function resetSubSideMenu() {
	$("#ff8081813c8630b9013c864089a20005").removeClass("side-menu-active");
	$("#ff8081813c8a2a77013c8a39aa9f0006").removeClass("side-menu-active");
}

function _suspendProcessInstance(cellValue, options, rawObject) {
	return '<a id="processInfo" href="javascript:void(0);" data-role="button" data-theme="b" onclick="supendProcessInstanceById(\''
			+ rawObject.id
			+ '\',\''
			+ rawObject.processGridType
			+ '\',\''
			+ rawObject.taskId
			+ '\',\''
			+ rawObject.resourceName
			+ '\');"><b>Suspend</b></a>';

}

function supendProcessInstanceById(id, processGridType,taskId,resourceName) {
	document.location.href = "#bpm/manageProcess/suspendProcessInstance";
	params = 'id=' + id + '&processGridType=' + processGridType + '&taskId=' + taskId + '&resourceName=' + resourceName;
	_execute('target', params);

}

function supendProcessInstanceById(id, processGridType,taskId,resourceName) {
	document.location.href = "#bpm/manageProcess/suspendProcessInstance";
	params = 'id=' + id + '&processGridType=' + processGridType + '&taskId=' + taskId + '&resourceName=' + resourceName;
	_execute('target', params);

}

function _activateProcessInstance(cellValue, options, rawObject,taskId,resourceName) {
	return '<a id="processInfo" href="javascript:void(0);" data-role="button" data-theme="b" onclick="activateProcessInstanceById(\''
			+ rawObject.id
			+ '\',\''
			+ rawObject.processGridType
			+ '\',\''
			+ rawObject.id
			+ '\',\''
			+ rawObject.resourceName
			+ '\',\''
			+ rawObject.processDefinitionId
			+ '\');"><b>Activate</b></a>';

}

function activateProcessInstanceById(id, processGridType,taskId,resourceName,processDefId) {
	var params = 'id=' + id + '&processGridType=bpm/manageTasks/'+processGridType + '&taskId=' + taskId + '&resourceName=' + resourceName+ '&processDefinitionId=' + processDefId;
	$.ajax({
		type : 'GET',
		url : "bpm/manageProcess/activateProcessInstance?"+params,
		success : function(response) {
			if(processGridType == "workflow") {
				showListView("WORKFLOW_INSTANCE_MANAGER","WORKFLOW INSTANCE MANAGER");
			} else {
		    	setTimeout(function(){
					validateMessageBox(form.title.message, response.success, "success");
					document.location.href = '#bpm/manageTasks/'+processGridType;
					_execute('target','');
			    	},200);
			}

		},
		failure : function(response) {
			validateMessageBox(form.title.message, response.success, "error");
		}
	});
	
//	document.location.href = "#bpm/manageProcess/activateProcessInstance";
//	_execute('target', params);
}

function _processInstanceGirdforSuspend(cellValue, options, rawObject) {
	if (rawObject.suspendCount > 0)
		return '<a id="processInfo" href="javascript:void(0);" data-role="button" data-theme="b" onclick="showProcessInstanceDetail(\''
				+ rawObject.id
				+ '\',\'suspended\',\''
				+ rawObject.processGridType
				+ '\');"><b>'
				+ rawObject.suspendCount + '</b></a>';
	else
		return '0';

}

function _processInstanceGirdforActive(cellValue, options, rawObject) {
	if (rawObject.activeCount > 0)
		return '<a id="processInfo" href="javascript:void(0);" data-role="button" data-theme="b" onclick="showProcessInstanceDetail(\''
				+ rawObject.id
				+ '\',\'activated\',\''
				+ rawObject.processGridType
				+ '\');"><b>'
				+ rawObject.activeCount + '</b></a>';
	else
		return '0';
}
function cancelTaskView(taskId) {
	/*params = 'taskId=' + taskId;
	document.location.href = "#bpm/manageTasks/mybucket";
	_execute('target', params);*/
	close();
}

function cancelTaskReadedView(taskId) {
	//params = "";
	//document.location.href = "##bpm/manageTasks/readedList";
	//_execute('target', params);
	close();
}

function cancelTaskToReadView(taskId) {
	close();
}

function _showStatusForMyBuckets(cellValue, options, rawObject){
	//cellValue 和 rawObject.lastOperationPerformed 的值相同
	if(cellValue == "save") {
		return "<font color='brown'>已保存</font>";
	} else if(rawObject.lastOperationPerformed == "return" || rawObject.lastOperationPerformed == "return submit" || rawObject.lastOperationPerformed == "Back") {  
		return "<font color='red'>退回</font>";
	}
	return "";
}

function _showPriorityForMyBuckets(cellValue, options, rawObject){
	if(cellValue <= 50) {
		return '<a href="javascript:void(0);" onclick="updateTaskPriority(\''+ rawObject.id + '\',100);"><img src="/images/star_boxed_empty.png"/></a>';
	} else if(cellValue > 50) {  
		return '<a href="javascript:void(0);" onclick="updateTaskPriority(\''+ rawObject.id + '\',50);"><img src="/images/star_boxed_full.png"/></a>';
	}
}

function updateTaskPriority(taskId, priority){
	var params = 'taskId='+taskId+'&priority='+priority;
	document.location.href = "#bpm/manageTasks/priority";
	_execute('target',params);
}

function _operateOnTask(cellValue, options, rawObject) {
	return '<a id="taskInfo" href="javascript:void(0);" data-role="button" data-theme="b" onclick="showTaskFormDetail(\''
	+ rawObject.id + '\',\''+ rawObject.suspensionState + '\',\''+ rawObject.lastOperationPerformed + '\',\''+ rawObject.executionId + '\',\''
	+ rawObject.resourceName + '\',\''+ rawObject.processDefinitionId + '\',\''+ rawObject.owner + '\');"><b>办理</b></a> ';
}

function _showStatusForDoneList(cellValue, options, rawObject){
	if(cellValue == "complete") {
		return "<font color='green'>已结束</font>";
	} else {
		return "<font color='red'>执行中</font>";
	}
}

function _operateOnTaskForDoneList(cellValue, options, rawObject) {
	return '<a id="taskInfo" href="javascript:void(0);" data-role="button" data-theme="b" onclick="showTaskFormDetailForDoneList(\''
	+ rawObject.id + '\',\''+ rawObject.suspensionState + '\',\''+ rawObject.lastOperationPerformed + '\',\''+ rawObject.executionId + '\',\''
	+ rawObject.resourceName + '\',\''+ rawObject.processDefinitionId + '\',\''+ rawObject.canActivate + '\',\''+ rawObject.isFormReadOnly + '\');"><b>办理</b></a>';
}

function _operateOnTaskForReader(cellValue, options, rawObject) {
	return '<a id="taskInfo" href="javascript:void(0);" data-role="button"  data-theme="b" onclick="showTaskFormDetailForToRead(\''
			+ rawObject.id + '\',\''+ false + '\',\''+ rawObject.suspensionState + '\');"><b>查看</b></a>';
}

function _operateOnTaskForReadedUser(cellValue, options, rawObject) {
	return '<a id="taskInfo" href="javascript:void(0);" data-role="button" data-theme="b" onclick="showTaskFormDetailForToRead(\''
			+ rawObject.id + '\',\''+ true + '\',\''+ rawObject.suspensionState + '\');"><b>查看</b></a>';
}

function validateMessageBox(title, content, type,focusDivId) {
	$.msgBox({
		title : title,
		content : content,
		type : type,
		afterClose:function (){
			if(focusDivId!=undefined){
				$("#"+focusDivId).focus();
			}
	    }
	});
}

function setLocale(languageOption) {
	document.location.href = "bpm/admin/setLocale?locale=" + languageOption;
}

function setUserPreferredSkin(preferredSkin) {
	if(preferredSkin != "ChooseSkin"){
		document.location.href = "bpm/admin/setUserPreferredSkin?preferredSkin=" + preferredSkin;
		$.cookie('setPreferedsKin',"True");
	}
}

function importXml() {
	openSelectDialogForFixedPosition("importPopupDialog", "", "750", "260","导入流程");
	document.location.href = "#bpm/showProcess/import";
}

function showPrintPreview(obj) {
	$(obj).printPreview($(obj).closest('form').attr('id'));
}


function showPrintPreviewFormId(obj,formId) {
	$(obj).printPreview(formId);
}


function clickPrintAll() {
	
	if ($("input[name='isPrintAll']").is(':checked')) {
		$("input[name='isPrintForm']").attr('checked', true);
		$("input[name='isPrintOpinion']").attr('checked', true);
		$("input[name='isPrintTrace']").attr('checked', true);

	} else {
			$("input[name='isPrintForm']").attr('checked', true);
		$("input[name='isPrintOpinion']").attr('checked', false);
		$("input[name='isPrintTrace']").attr('checked', false);

	}
	

}

function clickPrintForm() {
	if ($("input[name='isPrintForm']").is(':checked')) {
	} else {
		$("input[name='isPrintAll']").attr('checked', false);

	}
}

function clickPrintOpinion() {
	if ($("input[name='isPrintOpinion']").is(':checked')) {
	} else {
		$("input[name='isPrintAll']").attr('checked', false);

	}
	}
	function clickPrintTrace() {
	if ($("input[name='isPrintTrace']").is(':checked')) {
	} else {
		$("input[name='isPrintAll']").attr('checked', false);

	}
}
function showPrintTemplate(taskId, formId, formName){
	var self = this;
	var isPrintAll = false, isPrintForm = false, isPrintTrace = false, isPrintOpinion = false, response = "", isCancel = true;	
	 
	$.msgBox({
		type:'prompt',
		title : "打印选项",
		inputs: [
		{ header: "打印表单&nbsp;&nbsp;&nbsp;&nbsp;", type: "checkbox", name: "isPrintForm", value:'isPrintForm', checked:true, onclick:'clickPrintForm()'},  
		{ header: "打印意见&nbsp;&nbsp;&nbsp;&nbsp;", type: "checkbox", name: "isPrintOpinion", value:'isPrintOpinion',onclick:'clickPrintOpinion()'}, 
		{ header: "打印流程图&nbsp;&nbsp;&nbsp;&nbsp;", type: "checkbox", name: "isPrintTrace", id: "isPrintTrace", value:'isPrintTrace', onclick:'clickPrintTrace()'},   
		{ header: "打印所有&nbsp;&nbsp;&nbsp;&nbsp;", type: "checkbox", name: "isPrintAll", id: "isPrintAll", value:'isPrintAll', onclick:'clickPrintAll()'}   
		],
		buttons : [{
			value : "确定"			
		}, {
			value : "取消"
		}],
		success : function(result, values) {
			if (result == "确定") {
				isPrintAll = $("input[name='isPrintAll']").is(":checked");
				isPrintForm = $("input[name='isPrintForm']").is(":checked");
				isPrintOpinion = $("input[name='isPrintOpinion']").is(":checked");
				isPrintTrace = $("input[name='isPrintTrace']").is(":checked");
				isCancel = false; 
			}else{
				isCancel = true;
			}
		},
		afterClose: function(){
			if(isPrintForm || isPrintAll){
				var formAttributes = $('#'+formName).serializeArray();
				var templateValue = '[{';
				var subFormMap = new Object();
				var subFormAttrValues = "";
		
				
				_.each(formAttributes, function(obj){
					if(obj.name != "htmlSourceForSubForm") {
						if(obj.name.startsWith("subForm_")) {
							//alert(obj.name+"==="+obj.value);
							//subFormMap[obj.name] = obj.value;
							subFormAttrValues = subFormAttrValues + obj.name+":"+obj.value+",";
						} else {
							templateValue = templateValue +"'"+obj.name+"':'"+obj.value+"',";
						}
						
					}
					
					for(var instanceName in CKEDITOR.instances) {
						if(CKEDITOR.instances[instanceName].name.split("_")[1] == obj.name){
			   				var CKEditorData = CKEDITOR.instances[instanceName].getData();
			   				templateValue = templateValue +"'"+obj.name+"':'"+CKEditorData.replace(/\r\n/g, '').replace(/[\r\n]/g, '')+"',";
			   			}
					}
					
				});
				

				 var table = document.getElementById( 'subFormTable' );
				 	var count = $("#subFormTable tr th").length;
				 	templateValue = templateValue  +"'tdCount':'"+parseInt(count-2)+"',";

				var pastValues = $('#pastValuesJson').val();

				templateValue = templateValue.substring(0, templateValue.length-1);
				templateValue = templateValue + "}]";
				response = $.ajax({	
					url: '/form/getContentForPrintTemplate',
					type: 'POST',
					async: false,
					data: { formId: formId, templateValue: templateValue , pastValues : pastValues , subFormAttrValues : subFormAttrValues}
			  	}).responseText;
			}
			if(!isCancel){
				printTemplate(self, isPrintAll, isPrintForm, isPrintOpinion, isPrintTrace, response);
			}
		}
	});
	
}

if (typeof String.prototype.startsWith != 'function') {
	  String.prototype.startsWith = function (str){
	    return this.indexOf(str) == 0;
	  };
}

function printTemplate(self, isPrintAll, isPrintForm, isPrintOpinion, isPrintTrace, response){
	var printFieldData = response;
	var jsonPrintData = {};
	jsonPrintData = JSON.parse(printFieldData.toString());
	response = jsonPrintData.printHtmlContent;
	var opinionTable = '<table style="background: none repeat scroll 0 0 #FBFBFB;" class="table table-condensed no-margin">';
	$("#opinionTable").find("tr").each(function(){
		if($($(this).find('label')).html()){
			opinionTable = opinionTable + '<tr style="border-bottom: 2px solid #E4E4E4;"><td width="80%" class="border-none"><label class="style3 style18 ">'
			opinionTable = opinionTable + $($(this).find('label')).html();
			opinionTable = opinionTable + '</label></td></tr>';
		}
	});
	opinionTable = opinionTable + '</table>'
	
	var htmlContent = '<div class="container-fluid" style="overflow:auto">';
	var htmlContentForm = '<div class="row-fluid">'
					   		+'<div class="span12"> '
					   		+'<h5></h5>' // Removed Form Label in Print (bug id 4013)
					   		+'</div>'
					   		+'<div class="span12">'
					   		+response		
							+'</div>';
						  +'</div>';
	var htmlContentOpinion = '<div class="row-fluid">'
							+'<div class="span12">'
							+'<h5>Input Opinion</h5>'
							+'</div>'
							+'<div class="span12">'
							+ opinionTable
							+'</div>'
					      +'</div>';
	var htmlContentTrace = '<div class="row-fluid">'
							+'<div class="span12" style="overflow:auto">'
							+'<h5>Instance Trace</h5>'
							+'</div>'
							+'<div class="span12">'
							+ $("#opinion_div").html();
							+'</div>'
						+'</div>';
							
	var workflowTraceTable = '<div class="row-fluid">'
				+'<div class="span12">'
				+$("#workflowTraceTableDiv").html();
				+'</div>'
			+'</div>';
	
	if(isPrintAll || (isPrintForm && isPrintOpinion && isPrintTrace)){
		htmlContent = htmlContent + htmlContentForm + htmlContentOpinion + htmlContentTrace+workflowTraceTable;
	}else {
		htmlContent = isPrintForm ? htmlContent + htmlContentForm : htmlContent;
		htmlContent = isPrintOpinion ? htmlContent + htmlContentOpinion : htmlContent;
		htmlContent = isPrintTrace ? htmlContent + htmlContentTrace + workflowTraceTable : htmlContent;
	}	
	htmlContent = htmlContent + '</div>';	
	
	$("#print_preview").html(htmlContent);
	$(self).printPreview("print_preview");
}

/**
 * function string prototype contains not supported by chrome
 * @return {boolean}
 */
String.prototype.contains = function(s) {
	return this.indexOf(s) > -1
}

function getPreViewData(htmlSource){
	var startNode = document.getElementById('taskFormDiv');
	
	//alert("startNode=="+startNode+"=htmlSource=="+htmlSource);
	//document.getElementById('taskFormDiv').innerHTML = htmlSource;
	getFormChildren(startNode ,true);
	var test_value = startNode.innerHTML;
	//alert("test_value==="+test_value);
	//document.getElementById("importPopupDialog").innerHTML = test_value;
}

function saveCurrentForm(btnObj) {
	var formId = $(btnObj).closest('form').attr('id');
	ofSaveForm(formId);;
}
function closeForm() {
	document.location.href = "#bpm/user/homePage";
	_execute('target', '');
}

function showAddClassification() {
	openSelectDialogForFixedPosition("popupDialog", "", "520", "250","添加分类");
	document.location.href = "#bpm/manageProcess/showAddClassification";
}
function showAddClassificationForModule() {
	openSelectDialogForFixedPosition("popupDialog", "", "300", "200","Add Classification");
	document.location.href = "#bpm/manageProcess/showAddClassificationForModule";
}
function _previewTableQuery(cellValue, options, rawObject) {
	if (rawObject.tableQuery != null) {
		var tableQuery = rawObject.tableQuery.replace(/'/g, "`");
		return '<a id="previewTableQuery" href="javascript:void(0);" data-role="button" data-theme="b" onclick="previewTableQuery(\''
				+ tableQuery + '\');"><b>Preview</b></a>';
	} else {
		return '<b></b>';
	}
}

function previewTableQuery(tableQuery) {
	clearPreviousPopup();
	$("#popupDialog").html(tableQuery);
	$myDialog = $("#popupDialog");
	$myDialog.dialog({
		width : '50%',
		height : 'auto',
		modal : true,
		title : 'Query Preview'
	});
	$myDialog.dialog("open");
}

function getProcessGrid(e, data) {
	var filters = "";
	var id = data.rslt.obj.data("id");
	if (id == "all") {
		filters = "";
	} else {
		filters = {
			"groupOp" : "AND",
			"rules" : [ {
				"field" : "classificationId",
				"op" : "eq",
				"data" : id
			} ]
		};
	}

	jqGridFilter(filters, gridObj);
	document.getElementById("gridRecordDetails").innerHTML = "";

	function jqGridFilter(filtersparam, grid) {
		grid.setGridParam({
			postData : {
				filters : filtersparam
			},
			search : true
		});
		grid.setGridParam({
			page : 1
		});
		grid.trigger("reloadGrid");
	}
}

function reloadProcessGridByClassification(e, data) {
	var filters = "";
	var id = data.rslt.obj.data("id");
	if (id == "all") {
		filters = "";
	} else {
		filters = {
			"groupOp" : "AND",
			"rules" : [ {
				"field" : "Classification_Id",
				"op" : "eq",
				"data" : id
			} ]
		};
	}

	jqGridFilter(filters, gridObj);
	document.getElementById("gridRecordDetails").innerHTML = "";

	function jqGridFilter(filtersparam, grid) {
		grid.setGridParam({
			postData : {
				filters : filtersparam
			},
			search : true
		});
		grid.setGridParam({
			page : 1
		});
		grid.trigger("reloadGrid");
	}
}

function _showEditTable(cellValue, options, rawObject) {
	return '<a href="#/bpm/table/showEditTable"  onclick="javascript:showEditTable(\''
			+ rawObject.id + '\');">' + cellValue + '</a>';
}

function showEditTable(tableId) {
	var params = "tableId=" + tableId + "&enableRelationTab=false";
	;
	_execute('target', params);
}

function setBreadCrumb(rootMenuName, parentMenuName, childMenuName, rootMenuUrl, rootMenuType, rootMenuListview){
	$.cookie('rootMenuName',rootMenuName);
	$.cookie('parentMenuName',parentMenuName);
	$.cookie('childMenuName',childMenuName);
	$.cookie('rootMenuUrl',rootMenuUrl);
	$.cookie('rootMenuType',rootMenuType);
	$.cookie('rootMenuListview',rootMenuListview);
	if(parentMenuName == "compose"){
		var editor = CKEDITOR.instances['content'];
		if (editor) { 
		CKEDITOR.instances['content'].destroy();
		}
	}
	
}

function createBreadCrumb(){
	var breadCrumb = '<div id="breadCrumb" class="row-fluid" style="margin-bottom:5px;"><ul class="breadcrumb">';
	 var menus = new Array("rootMenuName","parentMenuName","childMenuName");
	 for(var i=0; i<menus.length; i++){
		 if( $.cookie(menus[i]) != undefined && $.cookie(menus[i]) != null && $.cookie(menus[i]) != ''){
			breadCrumb = breadCrumb + '<li class="text-capitalize">';
			if(0==i){
				breadCrumb=breadCrumb+'<span style=" margin-right:10px; margin-left:6px;"><a href="#bpm/user/homePage"><img src="/images/house.png"/></a></span>';
			}
			if((($.cookie('rootMenuUrl') != null && $.cookie('rootMenuUrl') != '' && $.cookie('rootMenuUrl') != '#') || $.cookie('rootMenuType') == 'listview') && i == 0){
				if($.cookie('rootMenuType') == 'listview'){
					var onClickMethod = "setBreadCrumb('"+$.cookie(menus[i])+"','','','"+$.cookie('rootMenuUrl')+"','"+$.cookie('rootMenuType')+"','"+$.cookie('rootMenuListview')+"');showListViewsWithContainer('"+$.cookie('rootMenuListview')+"', '"+$.cookie(menus[i])+"', 'target');";
					breadCrumb = breadCrumb + '<a href="javascript:void(0);" onClick="'+onClickMethod+'"><font color="white">'+ $.cookie(menus[i])+'</font></a>';
				} else {
					var backUrl = "#bpm"+$.cookie('rootMenuUrl');
					var onClickMethod = "setBreadCrumb('"+$.cookie(menus[i])+"','','','"+$.cookie('rootMenuUrl')+"','"+$.cookie('rootMenuType')+"','"+$.cookie('rootMenuListview')+"');";
					breadCrumb = breadCrumb + '<a href="'+backUrl+'" onClick="'+onClickMethod+'"><font color="white">'+ $.cookie(menus[i])+'</font></a>';
				}
			} else {
				breadCrumb = breadCrumb + $.cookie(menus[i]);
			}
			if(menus[i+1] != undefined && $.cookie(menus[i+1]) != undefined && $.cookie(menus[i+1]) != null && $.cookie(menus[i+1]) != ''){
				breadCrumb = breadCrumb + '<span class="divider">></span></li>';
			}
			breadCrumb = breadCrumb + '</li>';
	     }
	 }
	breadCrumb = breadCrumb + '</ul></div>';
	return breadCrumb;
}

function checkMenu(menuName, urlType, menuUrl, tableId, listViewName, listViewParams, processName,menuType,menuScript, selectedRootMenuId, selectedRootMenuName) {
	
	if(filterURL.indexOf("processEditor") > -1 && refresh =="false"){
		$.msgBox({
		    title: form.title.confirm,
		    content: form.msg.unsavedDataWillBeLostConfirmToContinue,
		    type: "confirm",
		    buttons: [{ value: "Yes" }, { value: "No" }],
		    success: function (result) {
		        if (result == "Yes") {
		        	filterURL = menuUrl;
		        	if(menuUrl != ''){
		        	document.location.href = '#bpm'+menuUrl;
		        	}else{
		        		var callback_function = new Function(menuScript);
		       			setTimeout(function(){callback_function()},100);
		        	}
		        	checkMenu(menuName, urlType, menuUrl, tableId, listViewName, listViewParams, processName,menuType,menuScript)
		        }else{
		  	 	  return false;
		        }
		    }
		});
	}else{
	//stores menuUrl into cookies .
	$.cookie('bpmAppBack',menuUrl);
	if($.cookie('bpmSelectedMenu') != null && $.cookie('bpmSelectedMenu')!= undefined){
		if(selectedRootMenuId != undefined){
			$("#"+$.cookie('bpmSelectedMenu')).removeClass('selected');
			$("#"+selectedRootMenuId).addClass('selected');
			$.cookie('bpmSelectedMenu',selectedRootMenuId);
		} else {
			$("#"+$.cookie('bpmSelectedMenu')).addClass('selected');
		}
	} else if(selectedRootMenuId != undefined){
			$("#"+selectedRootMenuId).addClass('selected');
			$.cookie('bpmSelectedMenu',selectedRootMenuId);
	}
	
	if($.cookie('selectedRootMenuName') != null && $.cookie('selectedRootMenuName')!= undefined){
		if(selectedRootMenuName != undefined && $.cookie('selectedRootMenuName') != selectedRootMenuName){
			$("#"+selectedRootMenuName+"_menu_onselect_menu2").show();
			$("#"+selectedRootMenuName+"_menu_onselect_menu1").hide();
			$("#"+$.cookie('selectedRootMenuName')+"_menu_onselect_menu1").show();
			$("#"+$.cookie('selectedRootMenuName')+"_menu_onselect_menu2").hide();
			$.cookie('selectedRootMenuName',selectedRootMenuName);
		} else {
			$("#"+$.cookie('selectedRootMenuName')+"_menu_onselect_menu2").show();
			$("#"+$.cookie('selectedRootMenuName')+"_menu_onselect_menu1").hide();
		}
	} else if(selectedRootMenuName != undefined){
		$("#"+selectedRootMenuName+"_menu_onselect_menu2").show();
		$("#"+selectedRootMenuName+"_menu_onselect_menu1").hide();
		$.cookie('selectedRootMenuName',selectedRootMenuName);
	}
	
	var bpmMenuName = $.cookie('bpmMenuName');
	if (menuName != null && menuName != '' && menuName != undefined && bpmMenuName!=menuName) {
		showSideMenu(menuName, tableId);
	}
	if (urlType != null && urlType != '' && urlType != undefined && urlType == 'url' ) {
		if(menuUrl.toLowerCase().substring(0, 3) == 'www') {
			window.open("http://"+menuUrl);
		}else if( (menuUrl.toLowerCase().indexOf("http") > -1 ) || (menuUrl.toLowerCase().indexOf("https") > -1 ) ) {
			window.open(menuUrl);
		}
		_execute('target', '');
	} else if (urlType != null && urlType != '' && urlType != undefined && (urlType == 'newTab' || urlType == 'script')) {
		var callback_function = new Function(menuUrl);
		callback_function();
	} else if (urlType != null && urlType != '' && urlType != undefined && (urlType == 'listview' || urlType == 'process' || urlType == 'report')) {
		if(urlType == 'listview'){
			if(listViewParams != ""){
				var listViewParams = "[{"+listViewParams+"}]";
				showListViewsWithContainerAndParam(listViewName, menuName, 'target', eval(listViewParams));
			}else{
				showListViewsWithContainer(listViewName, menuName, 'target');
			}
		}else if(urlType == 'report'){
			
			var reportName = listViewName;
			document.location.href = "#bpm/report/iframeReportForMenu?reportName="+reportName;
		}else if(urlType == 'process'){
			createNewBtnRedirect(processName);
		}
		
	}else {
		_execute('target', '');
	
	// Add data in browser history
	// var historyData = {menuUrl:{container:_mainContainer, params:_params,
	// menuUrl:menuUrl, menuName:menuName, tableId:tableId, urlType:urlType}};
	if (menuName != null && menuName != '' && menuName != undefined) {
		prevMenuName = topMenuName;
		topMenuName = menuName;
	}
	var mURL = menuUrl.replace(/\//g, "_")
	var historyData = "{'" + mURL + "':{'container':'" + _mainContainer
			+ "','params':'" + _params + "', 'menuUrl':'" + menuUrl
			+ "', 'menuName':'" + menuName + "', 'tableId':'" + tableId
			+ "', 'urlType':'" + urlType + "'}}";
	localHistoryStorage[localHistoryStorage.length] = historyData;
	}
	
	if(menuType=="side"){
		$.cookie('bpmSideMenuName',menuName);
		$.cookie('bpmSideUrlType',urlType);
		$.cookie('bpmSideAppBack',menuUrl);
		$.cookie('bpmSideTableId',tableId);
		$.cookie('bpmSideListViewName',listViewName);
		$.cookie('bpmSideListViewParams',listViewParams);
		$.cookie('bpmSideProcessName',processName);
		$.cookie('bpmSideScript',menuScript);
		$.cookie('bpmSideMenuType',menuType);
		$.cookie('isQuickMenu',"true");
	}else{
		$.cookie('bpmMenuName',menuName);
		$.cookie('bpmUrlType',urlType);
		$.cookie('bpmMenuUrl',menuUrl); 
		$.cookie('bpmTableId',tableId);
		$.cookie('bpmListViewName',listViewName);
		$.cookie('bpmListViewParams',listViewParams);
		$.cookie('bpmProcessName',processName);
		$.cookie('bpmMenuType',menuType);
		
		$.cookie('bpmSideMenuName',"");
		$.cookie('bpmSideUrlType',"");
		$.cookie('bpmSideAppBack',"");
		$.cookie('bpmSideTableId',"");
		$.cookie('bpmSideListViewName',"");
		$.cookie('bpmSideListViewParams',"");
		$.cookie('bpmSideProcessName',"");
		$.cookie('bpmSideScript',"");
		$.cookie('bpmSideMenuType',"");
		$.cookie('isQuickMenu',"false");
	}
	refresh = "false";
	}
	return true;
}

function clearMenuDataFromCookie(){
	$.cookie('bpmMenuName',"");
	$.cookie('bpmUrlType',"");
	$.cookie('bpmMenuUrl',""); 
	$.cookie('bpmTableId',"");
	$.cookie('bpmListViewName',"");
	$.cookie('bpmListViewParams',"");
	$.cookie('bpmProcessName',"");
	$.cookie('bpmMenuType',"");
	
	$.cookie('bpmSideMenuName',"");
	$.cookie('bpmSideUrlType',"");
	$.cookie('bpmSideAppBack',"");
	$.cookie('bpmSideTableId',"");
	$.cookie('bpmSideListViewName',"");
	$.cookie('bpmSideListViewParams',"");
	$.cookie('bpmSideProcessName',"");
	$.cookie('bpmSideScript',"");
	$.cookie('bpmSideMenuType',"");
	
}

function logOut(){
	var _hostURL = location.protocol+"//"+window.location.host+"/logout";
	location.href = _hostURL;
	
}

function setTableNamesList() {
	if (document.getElementsByName("_tableName")) {
		$.ajax({
			type : 'GET',
			async : false,
			url : '/bpm/table/allTable',
			success : function(data) {
				var dataList = $("#searchTableresults");
				dataList.empty();
				$.each(data, function(index, item) {
					var selectOption = document.createElement("OPTION");
					selectOption.text = item.tableName;
					selectOption.value = item.tableName;
					selectOption.id = item.id;
					dataList.append(selectOption);
				});
			}
		});
	} else {
		setTableNamesList();
	}
}

function setModuleNamesList(id,privilegeParam) {
	var param="";
	if(privilegeParam!="" && privilegeParam!=undefined){
		param=privilegeParam;
	}
		
	if (document.getElementsByName("_moduleName")) {
		$.ajax({
			type : 'GET',
			async : false,
			url : '/bpm/table/allModules'+param,
			success : function(data) {
				if($.browser.msie != undefined && $.browser.msie && $.browser.version < 10){
					var dataList = $("#importModuleName");
    					dataList.remove();
					var select = document.createElement("select");
 					select.setAttribute("name", "_moduleName");
 					select.setAttribute("id", "importModuleName");
  
 					/* setting an onchange event */
   					select.onblur = function() {setImportModuleDetails('module_Id','false')};								
    				var dataListAppend = $("#searchModuleresults");
					var str="";
					for(var index=0;index<data.length;index++){
						var item = data[index];
						var selectOption = document.createElement("OPTION");
						selectOption.text = item.moduleName;
						if (!($("input[name='" + id + "']").val())) {
							if (item.moduleName == "Default Module") {
								$("input[name='" + id + "']").val(item.id);
							}
						}
						str += "<option id='"+item.id+"' value='"+item.moduleName+"'>"+item.moduleName+"</option>"
						selectOption.value = item.moduleName;
						selectOption.id = item.id;
						select.add(selectOption);
						dataListAppend.append(str);
					}
					if(document.getElementById("searchModuleresults") != null ){
						var parent = document.getElementById("searchModuleresults").parentNode;
						document.getElementById("searchModuleresults").style.display="none";
						parent.appendChild(select);
					}
					
				} else {
					var dataList = $("#searchModuleresults");
					dataList.empty();
					var str = "<select>";
					$.each(data, function(index, item) {
						if (!($("input[name='" + id + "']").val())) {
							if (item.moduleName == "Default Module") {
								$("input[name='" + id + "']").val(item.id);
							}
						}
						str += "<option id='"+item.id+"' value='"+item.moduleName+"'>"+item.moduleName+"</option>"
					});
					str+="</select>";
					dataList.append(str);
				
				}
				
			}
		});
	} else {
		setModuleNamesList();
	}
}
function setFormTableDetails() {
	if(typeof String.prototype.trim !== 'function') {
	  String.prototype.trim = function() {
	    return this.replace(/^\s+|\s+$/g, ''); 
	  }
	}
	var tableName = document.getElementsByName("_tableName")[0].value.trim();
	
	var formName = 	document.getElementById("_formName").value;
	
	if (tableName != "") {
		var tableDataList = $('#searchTableresults');
		var tableVal = $(tableDataList).find(
				'option[value="' + tableName + '"]');
		if (tableVal.length == 0) {
			openMessage(form.title.message, form.error.existsTableName, "info", false,formName,"", false);

		} else {
			$("input[name='_tableId']").val(tableVal.attr('id'));
		}
	}
}

function setFormModuleDetails(id, isShowForm) {
	var moduleName = document.getElementsByName("_moduleName")[0].value.trim();
	if (moduleName == "") {
		moduleName = "Default Module";
	}
	var moduleDataList = $('#searchModuleresults');
	var moduleVal = $(moduleDataList)
			.find('option[value="' + moduleName + '"]');
	if (moduleVal.length != 0) {
		$("input[name='" + id + "']").val(moduleVal.attr('id'));
	} else {
		if (isShowForm == "false") {
			$.msgBox({
				title : form.title.message,
				content : form.msg.chooseExistingModule,
				buttons : [ {
					value : "Ok"
				} ],
				success : function(result) {
					if (result == "Ok") {
						$("#modules").val("");
						$("#modules").focus();
					}
				}
			});
		} else {
			openMessage(form.title.message, form.msg.chooseExistingModule, "info", false);
		}
	}
}

function setFormModuleDetailsByFieldName(id, isShowForm,fieldName,isListView) {
	var moduleName = document.getElementsByName(fieldName)[0].value.trim();
	if (moduleName == "") {
		moduleName = "Default Module";
	}
	var moduleDataList = $('#searchModuleresults');
	var moduleVal = $(moduleDataList)
			.find('option[value="' + moduleName + '"]');
	if (moduleVal.length != 0) {
		$("input[name='" + id + "']").val(moduleVal.attr('id'));
		if(isListView =='true'){
			$("#_moduleName").val(moduleName);
		}
	} else {
		if (isShowForm == "false") {
			$.msgBox({
				title : form.title.message,
				content : form.msg.chooseExistingModule,
				buttons : [ {
					value : "Ok"
				} ],
				success : function(result) {
					if (result == "Ok") {
						$("#modules").val("");
						$("#modules").focus();
					}
				}
			});
		} else {
			openMessage(form.title.message, form.msg.chooseExistingModule, "info", false);
		}
	}
}

function openMessage(title, content, type, isSave,formName,tableName,isTemplate) {
	//alert("================inside openMessage====formName==="+formName);
	//alert("================inside openMessage====tableName==="+tableName);
	//alert("===============openMessage isTemplate========="+isTemplate);
	$.msgBox({
		title : title,
		content : content,
		type : type,
		buttons : [ {
			value : "Ok"
		} ],
		success : function(result) {
			if (result == "Ok") {
				setTimeout(function() {
					if (isSave) {
						// isDublicateFormName();
					} else {
						getFormNameWithValues(formName,tableName,isTemplate);
					}
				}, 500);
			}
		}
	});
}

function openMessageCopyForm(title, content, type, moduleId) {
	$.msgBox({
		title : title,
		content : content,
		type : type,
		buttons : [ {
			value : "Ok"
		} ],
		success : function(result) {
			if (result == "Ok") {
				setTimeout(function() {
					copyForms(moduleId);
				}, 500);
			}
		}
	});
}

function prepareSubmitTaskForm(formId, formAction,showReadButton) {
	var form = document.getElementById(formId);
	// $("#form_div").css("height",getFormHeight(form));
	if (form) {
		var attachmentDetail = document.getElementById("attachmentDetail");
		form.appendChild(attachmentDetail);
		var curSubmit = $("input[type=submit]");
		if (curSubmit && curSubmit.length == 0) {
			var subSection = document.getElementById("formSubmitSection");
			form.appendChild(subSection);
		}
		
		if(showReadButton=='false' || showReadButton=='true'){
			$("input[type=submit]").hide();
			$("input[value=submit]").hide();
			$("input[type=Submit]").hide();
			$("input[value=Submit]").hide();
			var subSection = document.getElementById("formSubmitSection");
			form.appendChild(subSection);
		}
		
		var hiddenDetails = document.getElementById("hiddenDetails");
		if (hiddenDetails) {
			form.appendChild(hiddenDetails);
		}
		// var taskInput = document.createElement("input");
		// taskInput.type = "hidden";
		// taskInput.name = "taskId";
		// taskInput.value = taskId;
		// form.appendChild(taskInput);
		// form.action=formAction;
		// form.submit();
	} else {
		alert("Script Error: No form found on page with id: " + formId);
	}

}



function prepareSubmitRecallTaskForm(formId, formAction) {
	var form = document.getElementById(formId);
	if (form) {
		var curSubmit = $("input[type=submit]");
		if (curSubmit && curSubmit.length == 0) {
			var subSection = document.getElementById("formSubmitSection");
			form.appendChild(subSection); 	
		}
		var hiddenDetails = document.getElementById("hiddenDetails");
		if (hiddenDetails) {
			form.appendChild(hiddenDetails);
		}
		// var taskInput = document.createElement("input");
		// taskInput.type = "hidden";
		// taskInput.name = "taskId";
		// taskInput.value = taskId;
		// form.appendChild(taskInput);
		 form.action=formAction;
		_execute('task_target','');
		// form.submit();
	} else {
		alert("Script Error: No form found on page with id: " + formId);
	}

}

function systemDefinedFormStart(formId,formAction){
	var form = document.getElementById(formId);
	if ( !isFormValuesValid()) {
		validateMessageBox(form.title.message, "必填项不能为空" , false);
	} else {
		for(var instanceName in CKEDITOR.instances) {
		    CKEDITOR.instances[instanceName].updateElement();
		}
		$('#'+formId).find(':input').each(function(index){
			if($(this).attr('type') == 'checkbox'){
				var columnName = this.getAttribute("name");
				if(!$(this).is(':checked')) {
				    var element = document.createElement("input");
				    //Assign different attributes to the element.
				    element.setAttribute("type", "hidden");
				    element.setAttribute("value", false);
				    element.setAttribute("name", columnName);
					form.appendChild(element);
					
				}
			}
			if($(this).attr('type') == 'radio'){
				var columnName = this.getAttribute("name");
				if(!$(this).is(':checked')) {
				    var element = document.createElement("input");
				    //Assign different attributes to the element.
				    element.setAttribute("type", "hidden");
				    element.setAttribute("value", false);
				    element.setAttribute("name", columnName);
					form.appendChild(element);
					
				}
			}
		});
		
		if(form == "News" || form == "Notice") {
			var title = form['title'].value;
			var classification = form['classification'].value;
			var isSuccess = true;
			if(classification == "" || title == "") {
				isSuccess = false;
				validateMessageBox(form.title.message, "Title and classification should not be empty", false);
			}
			if(form['startDate'] && form['endDate']){
				isSuccess = validateStartAndEndDate(formId);
			}
			if(isSuccess == true){
				if(form['isNotice'] ){
					if($('input[name="status"]:radio:checked').val() == 'formal'){
						if(!$('[name="user"]').val() && !$('[name="role"]').val() && !$('[name="department"]').val() && !$('[name="group"]').val()){
								messageBoxShow(form.title.message,"Any one of the field (User,Role,Department,Group) should be fill");
								return false;
							}
					}
					//Create an input type dynamically.
				/*if(!form['isNotice'].checked){
						    var element = document.createElement("input");
						    //Assign different attributes to the element.
						    element.setAttribute("type", "hidden");
						    element.setAttribute("value", false);
						    element.setAttribute("name", "isNotice");
							form.appendChild(element);
				}*/
				}
				form.action=formAction;
				$("#"+formId).submit();
				if(formId == 'Notice') {
					checkMenu('','url','/notice/noticeList','8a10d45940f3dd3c014117b46a6a01ce','','','','side');
				} else if(formId == 'News') {
					checkMenu('','url','/news/newsList','ff8081813cc8a0d1013cc8cbc4d80000','','','','side');
				}
				
			}else {
				return false;
			}
		} else {
			// submit default process form
			form.action=formAction;
			$("#"+formId).submit();
			return true;
		}
		//return false;
	}
}

function validateStartAndEndDate(formId){
	var form = document.getElementById(formId);
	var startDate = form['startDate'].value;
	var title = form['title'].value;
	var classification = form['classification'].value;	
	var endDate = form['endDate'].value;
	if( startDate > endDate ||  startDate == "" || endDate == "") {
		validateMessageBox(form.title.message, "End Date should be greater than start Date", false);
		return false;
	}
	if(classification == "" || title == "") {
		validateMessageBox(form.title.message, "Title and classification should not be empty", false);
		return false;
	}	
	return true;
}

function prepareSubmitStartFormForSubmit(formId, formAction,isForRecall) {
	var form = document.getElementById(formId);
	 $("#form_div").css("overflow","auto");
	 $("#form_div").css("position","relative");
	if (form) {
		var attachmentDetail = document.getElementById("attachmentDetail");
		form.appendChild(attachmentDetail);
		var curSubmit = $("input[type=submit]");
		var curSave = $("input[type=button]");
		if (curSubmit && curSubmit.length == 0 && curSave.length == 0) {
			var subSection = document.getElementById("formSubmitSection");
			form.appendChild(subSection);
		} else if (curSubmit && curSubmit.length > 0) {
				var newButton = curSubmit.clone();
				newButton.attr("type", "button");
				newButton.attr("class", "btn btn-info");
				newButton.insertBefore(curSubmit);
				if(isForRecall == "yes") {
					newButton.attr("value","Recall");
					newButton.attr("onclick", "recallStartEvent('" + formId + "')");
				} else if(isForRecall == "notApplicable") {
					newButton.attr("onclick", "ofJointConductStartForm('" + formId + "')",false);
				} else {
					newButton.attr("value","Reset");
				}
				
				curSubmit.remove();
		}
		var hiddenDetails = document.getElementById("hiddenDetails");
		if (hiddenDetails) {
			form.appendChild(hiddenDetails);
		}
		// var processInput = document.createElement("input");
		// processInput.type = "hidden";
		// processInput.name = "processDefinitionId";
		// processInput.value = processDefinitionId;
		// form.appendChild(processInput);
		// form.action=formAction;
		// form.submit();
	} else {
		validateMessageBox(form.title.error, form.msg.scriptError + formId, "error");
		//alert("Script Error: No form found on page with id: " + formId);
	}

}

/*
 * function prepareSubmitStartForm(formId, formAction){ var form =
 * document.getElementById(formId); if(form){ var curSubmit =
 * $("input[type=submit]"); if(curSubmit&&curSubmit.length==0){ var subSection =
 * document.getElementById("formSubmitSection"); form.appendChild(subSection); }
 * var hiddenDetails = document.getElementById("hiddenDetails");
 * if(hiddenDetails){ form.appendChild(hiddenDetails); } //var processInput =
 * document.createElement("input"); //processInput.type = "hidden";
 * //processInput.name = "processDefinitionId"; //processInput.value =
 * processDefinitionId; //form.appendChild(processInput);
 * form.action=formAction; //form.submit(); }else{ alert("Script Error: No form
 * found on page with id: "+formId); } }
 */

function checkDumpFilePath(fileElement, fileType, actionPath, formId) {
	var status = false;
	var isTableName = false;
	var fileName = $(fileElement).val().split('.');
	if (fileType == "XLS") {
		var tableName = $("#importTableName").val();
		if (tableName.length <= 0) {
			$.msgBox({
				title : form.title.check,
				content : form.msg.selectTableName,
				afterClose : function() {
					isTableName = false;
					$("#importTableName").focus();
				}
			});
		} else {
			isTableName = true;
		}
	}else if(fileType=="SQL"){
		var moduleId = $("#importModuleId").val();
		if(moduleId==null || moduleId==""){
			validateMessageBox(form.title.message, form.msg.moduleShouldNotBeEmpty , "info");
			isTableName = false;
		}else{
			isTableName = true;
		}
	} else {
		isTableName = true;
	}

	if (isTableName) {
		if ($(fileElement).val() != "") {
			if (!fileName[1] || fileName[1].toUpperCase() != fileType) {
				$.msgBox({
					title : "Check",
					content : "Only " + fileType
							+ " formate files can be imported",
					afterClose : function() {
						status = false;
					}
				});
			} else {
				$("#" + formId).attr('action', actionPath);
				//$("#" + formId).submit();
				status = true;
			}
		} else {
			$.msgBox({
				title : form.title.check,
				content : form.msg.selectFileLocation,
				afterClose : function() {
					status = false;
				}
			});
		}
	} else {
		status = false;
	}

	return status;
}

function isNeedImport(isNeedImport) {
	if (isNeedImport == 'true') {
		importXml();
	}
}

var count = 0;

function deleteRow(row, table) {
	var delRow = row.parentNode.parentNode.rowIndex;
	var tbody = table.getElementsByTagName('tbody')[0];
	var numRows = tbody.rows.length ;
	if (numRows == 2) {
    	var rowId = $(row).closest('td').parent().attr("id");
		$('#'+rowId).find("*").each(function() { 
			$(this).attr("value",  "");
		});
    	/*var rowsIndex=$(row).closest('td').parent()[0].sectionRowIndex;
		var tbody = table.getElementsByTagName('tbody')[0];
		var numRows = tbody.rows.length ;
		removeFrom(numRows-1);
        if(numRows>2) {
        	table.deleteRow(numRows-1);	
        } else {*/

      //  }
	} else {
		table.deleteRow(delRow);
		var rowId = row.parentNode.parentNode.id;
		var rowNum = rowId.substring(rowId.length - 1);
		removeFrom(rowNum);
		// update subcount value and html source 
		var tbody = table.getElementsByTagName('tbody')[0];
		$('#subFormCount').val(tbody.rows.length -1);
		$('#htmlSourceForSubForm').val(document.getElementById("form_div").innerHTML);
		//count--;
	}
}

function removeFrom(value) {
	var rowPos = rowNumbers.indexOf(parseInt(value));
	rowNumbers.splice(rowPos,1);
	console.log(rowNumbers);
	$('#rowNumbers').val(rowNumbers);
}

function cloneMappingRow(table) {
	// var table = document.getElementById( id );
	if (table) {
		var tbody = table.getElementsByTagName('tbody')[0];
		var numRows = tbody.rows.length ;
		$('#subFormCount').val(numRows);
		var lastRow = tbody.rows[numRows - 1];
		var lastRowId = lastRow.id;
       	var rowId = parseInt(lastRowId.substring(lastRowId.length-1))+1;
		var newRow = lastRow.cloneNode(true);
		var rowid = 'row' + rowId;
		newRow.setAttribute('id', rowid);
		var length;
		$(newRow).find('input, hidden, select, textarea, checkbox').each(
				function() {
					var currentNameAttr = $(this).attr('name'); 
					length = parseInt(currentNameAttr.substring(currentNameAttr.lastIndexOf("_")+1,currentNameAttr.length))+1;
					var className = "" ;
					className = $(this).attr('class');
					var onFocusEvent = null;
					var onBlurEvent = null;
					var emptySpace = " ";
					onFocusEvent = $(this).attr('onfocus');
					onBlurEvent = $(this).attr('onblur');
					//if(numRows > 2) {
						currentNameAttr = currentNameAttr.substring(0, currentNameAttr.lastIndexOf("_"));
					//}
					$(this).attr('id', currentNameAttr+"_"+length);
					$(this).attr('name', currentNameAttr+"_"+length);
					if(className != undefined ) {
						if(className.indexOf("validation") > 0 ) {
							$(this).attr('onkeyup', "validateFields();");
						}
					}
					if(onFocusEvent != null) {
							var regex = new RegExp('_Form_'+(numRows-1), 'g');
							$(this).attr('onfocus',onFocusEvent.replace(regex, "_Form_"+numRows));
					}
					if( onBlurEvent != null ) {
							var regex = new RegExp('_Form_'+(numRows-1), 'g');
							$(this).attr('onblur',onBlurEvent.replace(regex, "_Form_"+numRows));
					}

					// current name
					// attribute
					// construct a new name attribute using regular expressions
					// the match is divided into three groups (indicated by
					// parentheses)
					// p1 will be 'v', p2 will be the number, p3 will be the
					// remainder of the string
				//	alert("count=i11111111111=="+count);
					var newNameAttr = currentNameAttr.replace(
							/([^\d]*)(\d*)([^\w]*)/,
							function(match, p1, p2, p3) {
								return p1 + (count) + p3;
							});
//					if (newNameAttr.indexOf("id") !== -1) {
						$(this).attr('value', '');
//					} else {
//					}
				});
		rowNumbers.push(length);
		console.log(rowNumbers);
		$('#rowNumbers').val(rowNumbers);
		tbody.appendChild(newRow);
		$('#htmlSourceForSubForm').val(document.getElementById("form_div").innerHTML);

	}
	count++;
}


function validateFields() {
	$('.number-validation').keyup(function () { 
	    this.value = this.value.replace(/[^0-9]/g,'');
	});	
	$('.decimal-validation').keyup(function () { 
	    this.value = this.value.replace(/[^0-9\.]/g,'');
	            if(this.value.split(".").length>2){
	                  this.value=this.value.slice(0,-1);
	            }
	});
	$('.email-validation').blur(function () { 
		if(!regex.test(this.value)){
			this.value="";
		}
		});
}

function setIndexPageByRedirect(url) {
	var els = document.getElementsByTagName("a");
	for ( var i = 0, l = els.length; i < l; i++) {
		var el = els[i];
		var path = $(el).attr('href');
		pathurl = url.substring(1);
		if (path === "#" + pathurl) {
			var id = $(el).attr('id');
			if(id === $.cookie('bpmSideTableId') || $.cookie('bpmSideTableId') == ''){
				setIndexPage(id);
				if($.cookie('isQuickMenu') != 'true'){
					showSideMenu( $.cookie('bpmMenuName'),id);
				}
				if($.cookie('setPreferedsKin') == 'True'){
					alert('call ---skin');
					checkMenu('home','url','/user/homePage','','','','','top');
	   				document.location.href = "#bpm/user/homePage";
	   				_execute('target','');
	   				$.cookie('setPreferedsKin',"False");
				}
				
			}
		}
	}
}

function fileDownloadLink(filePath) {
	document.location.href = "bpm/manageFile/downloadFile?filePath=" + filePath;
}

function startProcess() {
	var processKey = 'news:2:4003';
	document.location.href = "#bpm/manageProcess/startProcessInstance";
	_execute('target', 'processKey=' + processKey);
}

/*var formVariable = "";

function getTestChildNodes(startNode, output,formId){
	//alert("getTestChildNodes===="+startNode +"==formId=="+formId);
var formValue;
	var nodes;
	if(output == true){
		if(startNode.childNodes)   {
			nodes = startNode.childNodes;
			childrenTestNode(nodes, output,formId);
		}if(output == false){
			//alert("nodes==="+nodes.innerHTML);
			formValue = startNode.innerHTML;
		}
	}else{
		//alert("====startNode===="+startNode.innerHTML);
		//return startNode.innerHTML;
	}
	return startNode;
}

function childrenTestNode(nodes, output,formId)	{
	var node;
	var formValue;
	for(var i=0;i<nodes.length;i++)	{
		node = nodes[i];
		//alert("childrenTestNode======"+node.id+"==formId=="+formId);
		if(node.id == formId){
			formVariable = node.innerHTML;
			formValue = node.innerHTML;
			return 
		}
		if(output==true){
			if(node.childNodes)   {
				getTestChildNodes(node, output, formId);
		    }
		}
		if(output)   {
		
			childNode(node);
	    }
		
	}
}*/

function loadValuesPreview(formId){
	var form = document.getElementById(formId).innerHTML;
	document.getElementById(formId).style="";
	getPreViewData(form);
	
}

function loadValues(formId, name, value,isFormReadOnly) {
	var form = document.getElementById(formId);
	var element = form[name];
	//console.log(element);
	if (element) {
		if(element.constructor == NodeList){
			var tempValue="";
			var tempElement=[];
			for(var i = 0; i < element.length; i++){
				var individualElement = element[i];
				if (individualElement.type == "checkbox" || individualElement.type == "radio") {
					var checkedValues=value.split(","); 
					if(checkedValues.indexOf(individualElement.value) != -1) {
						individualElement.checked = true;
						tempValue=tempValue+individualElement.value;
					}
				}
				tempElement[tempElement.length]=individualElement;
			}
			
			for(var i = 0; i < tempElement.length; i++){
				var individualElement = tempElement[i];
				if(isFormReadOnly=="true" && individualElement!=undefined && individualElement!=formId){
					var elemStyleTop=individualElement.style.top+";";
					var elemStyleLeft=individualElement.style.left+";";
					if(i==0){
	      				$(individualElement).replaceWith("<span style='left:"+elemStyleLeft+"; top:"+elemStyleTop+" position: absolute; font-size: 14px;padding: 4px 6px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;height: 30px;align:left;'><b>&nbsp;&nbsp;&nbsp;"+tempValue+"</b></span>" );
	      				tempValue="";
					}else{
						$(individualElement).replaceWith("" );
					}
				}
			}

		}else {
			if (element.nodeName && (element.nodeName.toLowerCase() == "select")) {
			   if ($('option[value='+value+']', '#'+element.id).length) {
				   $("#"+element.id).val(value);
			   } 
			} else if (element.type == "checkbox" || element.type == "radio") {
				if(element.value == value) {
					element.checked = true;
				}
				/*if(isFormReadOnly=="true" && element!=undefined && element!=formId){
					if(element.checked){
						$( element ).replaceWith("<span style='font-size: 14px;padding: 4px 6px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;height: 30px;align:left;'><b>&nbsp;&nbsp;&nbsp;"+element.value+"</b></span>" );
					}else{
						$( element ).replaceWith("<span style='font-size: 14px;padding: 4px 6px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;height: 30px;align:left;'><b>&nbsp;&nbsp;&nbsp;</b></span>" );
					}
				}*/
				
			} else if (element.type == "file") {
				var p = element;
				/*if(isFormReadOnly=="true" && element!=undefined && element!=formId){
					$( element ).replaceWith( "<span style='font-size: 14px;padding: 4px 6px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;height: 30px;align:left;'><b>&nbsp;&nbsp;&nbsp;"+value+"</b></span>" );
				}*/
				
//				while (p.firstChild) {
//					p.parentNode.insertBefore(p.firstChild, p);
//				}
//				var el = document.createElement('a');
//				// el.data-icon="";
//				// el.data-role="button";
//				// el.href="javascript:fileDownloadLink('"+value+"');";
//				el.href = "javascript:void(0);";
//				el.onclick = function() {
//					fileDownloadLink(value);
//				};
//				//el.innerHTML = "Download";
//
//				p.parentNode.insertBefore(el, p);
//				p.parentNode.removeChild(p);
			} else {
				try {
					//console.log("===="+value+"==="+element.type);
					element.value = value;
				} catch (err) {

				}
			}
		}
	}
	var labels = document.getElementsByTagName('LABEL');
	for (var i = 0; i < labels.length; i++) {
		if(labels[i].getAttribute('name') == name) {
			labels[i].innerHTML = value;
		}else {
			labels[i].innerHTML = "";
		}
	}
	
}

function replaceAllValueAsReadOnly(formId){
		$.each($('#'+formId).serializeArray(), function(i, field) {
			// remove ckeditor
			for(name in CKEDITOR.instances) {
			    CKEDITOR.instances[name].destroy()
			}
			var form = document.getElementById(formId);
			var element = form[field.name];
			
			if(element.type != 'hidden'){
			if(element.nodeName != undefined){
			if(field.value!="" && element.nodeName.toLowerCase() != "select"){
				if(element.type == "checkbox") {
					$( element ).attr('disabled', true); // show checkbox in disabled format
				} else {
					for(name in CKEDITOR.instances) {
					    CKEDITOR.instances[name].destroy()
					}
					$( element ).replaceWith("<span>"+field.value+"</span>" );
				}
			}else if(element.nodeName.toLowerCase()== "select"){
				if(element.text != undefined){
					$( element ).replaceWith("<span>"+element.text+"</span>" );
				}else{
					$( element ).replaceWith("<span>"+element.options[element.selectedIndex].text+"</span>" );
				}
			}else{
				if(element.nodeName.toLowerCase() == "textarea"){
				$( element ).replaceWith("<span>"+field.value+"</span>" );
				} else {
					$( element ).replaceWith("<span></span>" );		
				}
			}
			}
			}
		});
		
		$('#'+formId).find(':input').each(function(index){
			if($(this).attr('type') != 'hidden'){
				if($(this).attr('type') == 'radio'){
					  if($(this).is(':checked')){
						$(this).replaceWith("<span>"+$(this).val()+"</span>");
						}else{
							$(this).replaceWith("<span></span>");
						}
				}else if($(this).attr('type') == 'file'){
						$(this).replaceWith( "<span></span>" );
				}if($(this).attr('type') == 'checkbox'){
			  			$( this ).attr('disabled', true); // show checkbox in disabled format
				}else {
					if($(this).attr('type') != 'button'){
						$(this).replaceWith( "<span>"+$(this).val()+"</span>" );
					}else {
						if(($(this).attr('name') == "Submit")  || ($(this).attr('name') == "Save") || ($(this).attr('name') == "Start") || ($(this).attr('name') == "Reset")){
							$(this).replaceWith( "<span></span>" );
						}
					}
				}
			}
		});
				
		
		$("#"+formId).find("select").each(function(index){
			$(this).replaceWith("<span></span>" );
		});
		
		$(".ui-datepicker-trigger").hide();
		$(".requiredImg").hide();
		$(".ui-datepicker-trigger").hide();
}

function hideElement(element) {
	element.parentNode.style.display = "none";
	//element.style.display = "none";
}

function removeElement(element) {
	element.parentNode.remove();
	/*var childNodes = element.parentNode.childNodes;
	for ( var i = childNodes.length - 1; i >= 0; i--) {
		var childNode = childNodes[i];
		if (childNode.name == element.name
				&& element.tagName == childNode.tagName) {
			childNode.parentNode.removeChild(childNode.nextSibling);
			childNode.parentNode.removeChild(childNode);
		}
	}*/
	// element.parentNode.removeChild(document.getElementById(element.id));
}

function loadPermissionsOnCreate(formId, name, isEditable, isRequired,
		isReadOnly, isHidden, isNoOutput) {
	
	var form = document.getElementById(formId);
	
	var element;
	
	if(name.indexOf("subForm_")>=0){
    	var name = name.substring(0,name.lastIndexOf("_")+1);
    	
    	element = $("input[name^='"+name+"']");
    	
    }else{
    	element = form[name];
    }
	
	if (element) {
		if (element.length && element.length > 1) {
			for ( var i = 0; i < element.length; i++) {
				var el = element[i];
				setElementPermissionForCreate(el, isRequired, isReadOnly, isHidden, isNoOutput);
			}
		} else {
			setElementPermissionForCreate(element, isRequired, isReadOnly, isHidden, isNoOutput);
		}

	}

}

function setElementPermissionForCreate(el, isRequired, isReadOnly, isHidden, isNoOutput) {
	if (el.tagName == "option" || el.tagName == "OPTION") {
		el = el.parentNode;
	}
	if (isRequired == "true") {
		if (isReadOnly == "true") {
			// alert("Check with your admin! Permissions have been set as both
			// required and readonly for field "+name)
		}
		el.className += " mandatory";
	}
	
	if((el.className.indexOf("datepicker") != -1 || el.className.indexOf("datetimepicker") != -1) &&  isReadOnly == "true") {
		
		//$('.datepicker').datepicker().datepicker('disable');
		el.className="";
		//$('.datepicker').datepicker().disable = true;
		//$('.datetimepicker').datepicker().disable = true;
	}
	if (isReadOnly == "true") {
		if( el.onclick){
			el.onclick="";
		}
		
		el.readOnly = true;
		CKEDITOR.config.readOnly = true;
		// readonly for user field
		
		//if(el.type=="checkbox" || el.type=="radio" || el.type=="userlist" || el.type=="departmentlist" || el.type=="rolelist" || el.type=="grouplist"){
		if(el.type=="checkbox" || el.type=="radio" || el.type == "file" || el.type == "select-one"){
			el.disabled = true;
		}
		var elementId = el.id;
		elementId = elementId + 'FullName';
		$('#'+elementId).prop('disabled', true);
	}else{
		el.readOnly = false;
		CKEDITOR.config.readOnly = false;
	}
	if (isHidden == "true") {
		var elementId = el.id;
		//$('#'+elementId).remove(); 
		hideElement(el);
	}
	if (isNoOutput == "true") {
		var elementId = el.id;
		//$('#'+elementId).remove(); 
		removeElement(el);
	}
}

function setElementPermissionForEdit(element, isEditable) {
	if (element.tagName == "option" || element.tagName == "OPTION") {
		element = element.parentNode;
	}
	if (isEditable == "false") {
		element.readOnly = true;
	}
}

function loadPermissionsOnEdit(formId, name, isEditable, isRequired, isReadOnly, isHidden, isNoOutput) {
	var form = document.getElementById(formId);
	var element = form[name];
	if (element) {
		if (element.length && element.length > 1) {
			for (i = 0; i < element.length; i++) {
				setElementPermissionForEdit(element[i], isEditable);
				setElementPermissionForCreate(element[i], isRequired, isReadOnly, isHidden, isNoOutput);
			}
		} else {
			setElementPermissionForEdit(element, isEditable);
			setElementPermissionForCreate(element, isRequired, isReadOnly, isHidden, isNoOutput);
		}

	}

}

function setCkEditorLanguage() {
	var language = $("#languageOption").val();
	return language;
}

//Home page functions start

function _editLayout(cellValue, options, rawObject){
	 return '<a id="editLayout" href="javascript:void(0);" data-role="button" data-theme="b" onclick="showEditLayout(\''+rawObject.id+'\');"><b>'+rawObject.name+'</b></a>';
}

function _editQuickNav(cellValue, options, rawObject){
	 return '<a id="editLayout" href="javascript:void(0);" data-role="button" data-theme="b" onclick="showEditQuickNav(\''+rawObject.id+'\');"><b>'+rawObject.name+'</b></a>';
}

function _isAddToQuickNav(cellValue, options, rawObject){
	if(rawObject.status == 'true'){
		return '<input type="checkbox" onclick="addToQuickNavigation(\''+rawObject.id+'\',\''+rawObject.name+'\')"  id="isQuickAdd" checked="checked">';
	}else{
		return '<input type="checkbox" onclick="addToQuickNavigation(\''+rawObject.id+'\',\''+rawObject.name+'\')"  id="isQuickAdd" >';
	}
	
}

function addToQuickNavigation(id,name){
			addQuickNavToHome(id,name,$("#isQuickAdd").is(':checked'));
}

function addQuickNavToHome(id,name,status){
	$.ajax({
		url: 'bpm/admin/updateQuickNavStatus?id='+id+'&status='+status,
	    type: 'GET',
	    dataType: 'json',
		async: false,
		success: function(data) {
			if(status){
				validateMessageBox(form.title.message, name + form.msg.addedToQuickNav, "success");
			}else{
				validateMessageBox(form.title.message, name + form.msg.removededToQuickNav, "success");
			}
       }
   });	
}

function showIcons(iconPath){
	$.ajax({
		url: 'bpm/admin/getMenuIcons',
	    type: 'GET',
	    dataType: 'json',
		async: false,
		success : function(response) {
			var htmlContent = "<table  width='100%'><tr>";
			for(var index = 0; index < response.length; index++){
				if(index % 5 == 0){
					htmlContent = htmlContent + "</tr><tr>";
				}
				htmlContent = htmlContent + "<td align='center'><img class='cursor-pointer' onClick='selectIcon(\""+response[index]+"\", \""+iconPath+"\");' style='padding:5px;border:1px solid gray;' src='"+response[index]+"'/></td>";
			}
			htmlContent = htmlContent + "</tr></table>";
			$("#icon_dialog").html(htmlContent);
			openSelectDialogForFixedPosition("icon_dialog","",300,300,"选择图标");
		} 
	});
}

function selectIcon(iconPath,id){
	$("#"+id).val(iconPath);
	 setTimeout(
                function () {
                   $("#icon_dialog").dialog("close");
                }, 10	);
	//$("#icon_dialog").dialog("close");
}

function quickNavigationPage(url){
	document.location.href="#bpm/"+url;
	_execute('target','');
}

//Home page functions end
function _showInteriorDictionaries(cellValue, options, rawObject) {
	return '<span id="list_view_'
			+ rawObject.dictionaryId
			+ '"><a href="javascript:void(0);" onclick="getDataDictionaryGrid(\''
			+ rawObject.dictionaryId
			+ '\')"><b>查看</b></a></span>';
}

function loadDataDictionaryGrid(id) {
	
	$.cookie('bpmSideScript','');
	var params = 'parentDictId=' + id + '&hierarchyParentId=' + id;
	document.location.href = "#bpm/admin/dataDictionary";
	_execute('target', params);
}

function _editDataDictionary(cellValue, options, rawObject) {
	return '<a id="editDictionary" href="javascript:void(0);" data-role="button" data-theme="b" onclick="editDataDictionary(\''
			+ rawObject.dictionaryId + '\')"><b>' + rawObject.name + '</b></a>';
}

function CKupdate() {
	for (instance in CKEDITOR.instances) {
		var editor_data = CKEDITOR.instances[instance].getData();
		if (editor_data) {
			editor_data = editor_data.replace(/\n|\r/g, '<br />');
		}
		$('#' + instance).html(editor_data);
	}
}

function checkFormSubmit() {
	$("form").submit(function() {
		CKupdate();
		for(var instanceName in CKEDITOR.instances) {
		    CKEDITOR.instances[instanceName].updateElement();
		}
	});
}

function loadCKEditor(callBackLoadCkEditor) {
	
	$(".richtextbox").each(
			function() {
				var fullStyle = "";
				var cke_height = "";
				var height=$(this).attr('height');
				var width=$(this).attr('width');
				var style=$(this).attr('style');
				var elementId=$(this).attr('id');
				var t = $("<textarea cols='30' id=" + elementId
						+ " name=" + $(this).attr('name')
						+ " column=" + $(this).attr('column')
						+ " table=" + $(this).attr('table')
						+ " rows='10' style='"+style+"height:"+height+"px;width:"+width+"px;'></textarea>");
				t.insertBefore($(this));
				style=$(this).attr('style');
				$(this).remove();
				cke_height = parseInt(height)-105;
				fullStyle = style+"height:"+height+"px;width:"+width+"px;";
				richTextBoxMap["cke_"+elementId]=fullStyle;
				CKEDITOR.replace($(this).attr('name'), {
					toolbar : [
							[ 'Bold', 'Italic', 'Underline', 'StrikeThrough',
									'-', 'Undo', 'Redo', '-', 'Cut', 'Copy',
									'Paste', 'Find', 'Replace' ],
							[ 'NumberedList', 'BulletedList', '-',
									'JustifyLeft', 'JustifyCenter',
									'JustifyRight', 'JustifyBlock' ],
							[ 'Table', '-', 'TextColor', 'BGColor','Image' ],
							[ 'Styles', 'Format', 'Font', 'FontSize' ] 
							],
							 width: width,
							 height: cke_height
				});
				callBackLoadCkEditor();
			});
}

function loadListViewsOnForm() {
	var formId = $('#formId').val();
	$(".gridviewcontrol").each(
			function() {
				var containerId = $(this).attr('gridtitle') + "_formgrid";
				var height = $(this).attr('height');
				var width = $(this).attr('width');
				var style=$(this).attr('style');
				var gridViewParams=$(this).attr('gridviewparams');
				if(gridViewParams.indexOf('@') > 0) {
					getData(formId);
					$(this).hide();
				} else {
					var listView = $("<div id='" + containerId + "' name='" + containerId	+ "' style='overflow: auto;'></div>");
					listView.insertBefore($(this));
					$(this).hide();
					listViewDisplay($(this).attr('gridName'), $(this).attr('gridtitle'), containerId,gridViewParams,width,height);
				}
				/*showListViewsWithContainer($(this).attr('gridName'), $(this)
						.attr('gridtitle'), containerId);*/
				//showListViewsWithContainerAndParamAndSize($(this).attr('gridName'), $(this).attr('gridtitle'), containerId,$.parseJSON(gridViewParams),height,width);
			});
}

function getData (formId) {
	$('.gridviewcontrol').each(function() {
				var containerId = $(this).attr('gridtitle') + "_formgrid";
				var height = $(this).attr('height');
				var width = $(this).attr('width');
				var style=$(this).attr('style');
				var gridViewParams=$(this).attr('gridviewparams');
				gridViewParams = gridViewParams.replace('[','');
				gridViewParams = gridViewParams.replace(']','');
				var result = $.parseJSON(gridViewParams);
				$.each(result, function(key, value) {
				    if (value.substring(0, 1) == "@") {
				    	var formValue = value.replace('@','');
						//alert("===con=="+gridViewParams+"==="+formValue);
						var form = document.getElementById(formId);
						var paramValue = form[formValue].value;
						result[key] = paramValue;
				    }
				});
			//	alert("params====="+result);
			//	console.log(JSON.stringify(result)+"===="+$.parseJSON(result));
				var params = "["+JSON.stringify(result)+"]" ;
				if ($('#'+containerId).length > 0) {
				   $('#'+containerId).remove();
				}
				var listView = $("<div id='" + containerId + "' name='" + containerId	+ "' style='overflow: auto;'></div>");
				listView.insertBefore($(this));
				$(this).hide();
				listViewDisplay($(this).attr('gridName'), $(this).attr('gridtitle'), containerId,params,width,height);
	});
}

function _showMoveUpImage(cellValue, options, rawObject) {
	return '<img src="/images/go_up_small.png" onclick="moveDictionaryRow(\'up\',\''
			+ options.rowId + '\')"/>';
}

function _showMoveDownImage(cellValue, options, rawObject) {
	return '<img src="/images/move_down_small.png" onclick="moveDictionaryRow(\'down\',\''
			+ options.rowId + '\')"/>';
}

function _moveGroupGridRowUpImage(cellValue, options, rawObject) {
	return '<img src="/images/go_up_small.png" onclick="moveGridRow(\'up\',\''
			+ options.rowId + '\',\'GROUP_LIST\')"/>';
}

function _moveGroupGridDownImage(cellValue, options, rawObject) {
	return '<img src="/images/move_down_small.png" onclick="moveGridRow(\'down\',\''
			+ options.rowId + '\',\'GROUP_LIST\')"/>';
}

function _moveDepartmentGridRowUpImage(cellValue, options, rawObject) {
	return '<img src="/images/go_up_small.png" onclick="moveGridRow(\'up\',\''
			+ options.rowId + '\',\'DEPARTMENT_LIST\')"/>';
}

function _moveDepartmentGridDownImage(cellValue, options, rawObject) {
	return '<img src="/images/move_down_small.png" onclick="moveGridRow(\'down\',\''
			+ options.rowId + '\',\'DEPARTMENT_LIST\')"/>';
}

function _moveRoleGridRowUpImage(cellValue, options, rawObject) {
	return '<img src="/images/go_up_small.png" onclick="moveGridRow(\'up\',\''
			+ options.rowId + '\',\'ROLE_LIST\')"/>';
}

function _moveRoleGridRowDownImage(cellValue, options, rawObject) {
	
	return '<img src="/images/move_down_small.png" onclick="moveGridRow(\'down\',\''
			+ options.rowId + '\',\'ROLE_LIST\')"/>';
}




/*
 * function checkCustomOperationFunction(isUpdate){ var
 * customFunctionName=$("#customFunctionName").val(); if(customFunctionName!="" &&
 * customFunctionName!=null){
 * 
 * }else{ $.msgBox({ title: "Check", content: "Function Name shoud not be
 * empty", afterClose:function (){ $("#customFunctionName").focus() ; } }); } }
 */

/**
 * To set the parent and child relation between tables
 */
function setTableRelation(tableId, treeName, subTableId, sourceTree,
		movingNode, sourceTableName) {
	var targetTableName=movingNode.o.attr("name");
	if(treeName == "parent_table_names"){
		targetTableName=targetTableName+" *";
	}else{
		sourceTableName=sourceTableName+" *";
	}
	
	var status = false;
	var renderTreeName = "";
	var removeTreeNoad = "";
	var movingTableId = movingNode.o.attr("id");
	// table.title.sourceColumnName
	// table.title.targetColumnName
	$.msgBox({
		title : form.title.tableRelation,
		type : "prompt",
		inputs : [ {
			header : sourceTableName + "&nbsp;&nbsp;&nbsp;",
			type : "text",
			name : "_sourceTableColumns",
			autocomplete : "true",
			datalistId : "sourceTableColumns",
			onblur : "setSourceColumnsDetails()"
		}, {
			header : targetTableName + "&nbsp;&nbsp;&nbsp;",
			type : "text",
			name : "_targetTableColumns",
			autocomplete : "true",
			datalistId : "targetTableColumns",
			onblur : "setTargetColumnsDetails()"
		}, {
			header : "",
			type : "hidden",
			name : "_sourceColumnId"
		}, {
			header : "",
			type : "hidden",
			name : "_targetCoduleId"
		} ],
		buttons : [ {
			value : "Yes"
		}, {
			value : "Cancel"
		} ],
		success : function(result, values) {
			if (result == "Yes") {

				var sourceColumnName = values[0].value.trim();
				var sourceColumnId = values[2].value.trim();
				var targetColumnName = values[1].value.trim();
				var targetColumnId = values[3].value.trim();

				if (sourceColumnName != "" && targetColumnName != "") {
					var movedJsonData = "";

					if (treeName == "parent_table_names") {
						renderTreeName = "parentTable";
						removeTreeNoad = tableId;
						movedJsonData = {
							"data" : movingNode.o.attr("name"),
							"attr" : {
								"id" : movingTableId,
								"name" : movingNode.o.attr("name"),
								"foreignKeyColumnId" : targetColumnId,
								"foreignKeyColumnName" : targetColumnName,
								"childKeyColumnid" : sourceColumnId,
								"childKeyColumnName" : sourceColumnName
							},
							"metadata" : {
								"id" : movingTableId,
								"name" : movingNode.o.attr("name")
							}
						};

					} else if (treeName == "sub_table_names") {
						renderTreeName = "childTable";
						removeTreeNoad = subTableId;
						movedJsonData = {
							"data" : movingNode.o.attr("name"),
							"attr" : {
								"id" : movingTableId,
								"name" : movingNode.o.attr("name"),
								"foreignKeyColumnId" : sourceColumnId,
								"foreignKeyColumnName" : sourceColumnName,
								"childKeyColumnid" : targetColumnId,
								"childKeyColumnName" : targetColumnName
							},
							"metadata" : {
								"id" : movingTableId,
								"name" : movingNode.o.attr("name")
							}
						};
					}

					// $("#"+treeName).jstree("create", $("#"+renderTreeName) ,
					// false, iiii , false, true);
					$("#" + treeName).jstree("create", $("#" + renderTreeName),
							false, movedJsonData, false, true);
					// removing
					if (sourceTree == "parentTable") {
						$("#parent_table_names").jstree("remove",
								"#" + movingTableId);
					} else if (sourceTree == "childTable") {
						$("#sub_table_names").jstree("remove",
								"#" + movingTableId);
					} else {
						$("#" + sourceTree).jstree("remove",
								"#" + movingTableId);
					}

					// $("#"+sourceTree).find("li").("#"+movingTableId).remove();

					// $("#"+sourceTree).jstree("remove","#"+movingTableId);
				} else {
					validateMessageBox(form.title.message, form.msg.selectgSourceAndTargetColumn, "message");
					//alert("select a source and target column");
				}

			}
		}
	});
	if (document.getElementsByName("_sourceTableColumns")) {
		setSourceTableColumns(treeName);
	}
	if (document.getElementsByName("_targetTableColumns")) {
		setTargetTableColumns(movingTableId,treeName);
	}

}

// To get the current table columns
function setSourceTableColumns(treeName) {
	if (document.getElementsByName("_sourceTableColumns")) {
		var dataList = $("#sourceTableColumns");
		dataList.empty();
		for (key in sourceTableColumns) {
			var selectOption = document.createElement("OPTION");
			selectOption.text = sourceTableColumns[key];
			selectOption.value = sourceTableColumns[key];
			selectOption.id = key;
			if(treeName=="sub_table_names"){
				dataList.append(selectOption);
			}else if(treeName=="parent_table_names"){
				//if(selectOption.text!="ID"){
					dataList.append(selectOption);
				//}
			}
		}
	} else {
		setSourceTableColumns();
	}
}

// To get the moving table columns
function setTargetTableColumns(tableId,treeName) {
	if (document.getElementsByName("_targetTableColumns")) {
		$.ajax({
			type : 'GET',
			async : false,
			url : '/bpm/table/allTableColumns?tableId=' + tableId,
			success : function(data) {
				var dataList = $("#targetTableColumns");
				dataList.empty();
				$.each(data, function(index, item) {
					var selectOption = document.createElement("OPTION");
					selectOption.text = item.columnName;
					selectOption.value = item.columnName;
					selectOption.id = item.id;
					if(treeName=="sub_table_names"){
						if(selectOption.text!="ID"){
							dataList.append(selectOption);
						}
					}else if(treeName=="parent_table_names"){
						//if(selectOption.text!="ID"){
							dataList.append(selectOption);
						//}						
					}
				});
			}
		});
	} else {
		setTargetTableColumns();
	}
}

// To set the source details
function setSourceColumnsDetails() {
	var columnName = document.getElementsByName("_sourceTableColumns")[0].value
			.trim();
	if (columnName != "") {
		var columnDataList = $('#sourceTableColumns');
		var columnVal = $(columnDataList).find(
				'option[value="' + columnName + '"]');
		if (columnVal.length == 0) {
			validateMessageBox(firm.title.message, form.msg.chooseExistingColumn,	"info", false);
		} else {
			$("input[name='_sourceColumnId']").val(columnVal.attr('id'));
		}

	}
}

// To set the target details
function setTargetColumnsDetails() {
	var columnName = document.getElementsByName("_targetTableColumns")[0].value
			.trim();
	if (columnName != "") {
		var columnDataList = $('#targetTableColumns');
		var columnVal = $(columnDataList).find(
				'option[value="' + columnName + '"]');
		if (columnVal.length == 0) {
						validateMessageBox(firm.title.message, form.msg.chooseExistingColumn,	"info", false);
		} else {
			$("input[name='_targetCoduleId']").val(columnVal.attr('id'));
		}

	}
}

function _showTaskDetailForMyInstance(cellValue, options, rawObject) {
	return '<a id="taskDetail" href="javascript:void(0);" data-role="button" data-theme="b" onclick="showTaskDetailsDiv(\''
			+ rawObject.id
			+ '\',\''
			+ rawObject.processInstanceId
			+ '\')"><b>'
			+ rawObject.name + '</b></a>';
}

function showTaskDetailsDiv(taskId, processInstanceId) {
	$('.taskDetails').remove();
	var params = 'taskId=' + taskId + '&processInstanceId=' + processInstanceId;
	document.location.href = "#bpm/manageProcess/showTaskDetail";
	_execute('taskDetails', params);

}

function unPrevilegeMessage() {
				validateMessageBox(firm.title.message, form.msg.userDoesntHavePrevilegeToSubmit, "info");
	
}

function prepareSubmitTaskFormForSubmit(formId, formAction, isOperationfnSubmit, isFinalTransactor,showReadButton) {
	var form = document.getElementById(formId);
	 $("#form_div").css("height",getFormHeight(form));
	if (form) {
		var attachmentDetail = document.getElementById("attachmentDetail");
		form.appendChild(attachmentDetail);
		var submitType = $("input[type=submit]");
		var buttonType = $("input[type=button]");
		//console.log(submitType.length+"==="+buttonType.length);
		if (submitType && submitType.length == 0 && buttonType.length == 0) { // if no buttons added in Form
			var subSection = document.getElementById("formSubmitSection");
			form.appendChild(subSection);
		} else if ( (submitType && submitType.length > 0) ) { // Button type - 'submit'
			//jQuery.each(submitType, function() {
				$(this).attr("class", "btn btn-info");
				addButtons($(this),isOperationfnSubmit,isFinalTransactor,formId);
		//	});
		} else if(  buttonType && buttonType.length > 0 ) { // Button type - 'button'
			//jQuery.each(buttonType, function() {
				$(this).attr("class", "btn btn-info");
				buttonType.attr("class", "btn btn-info");
				addButtons(buttonType,isOperationfnSubmit,isFinalTransactor,formId);
			//});
		}

		if(showReadButton=='false' || showReadButton=='true'){
			$("input[type=submit]").hide();
			$("input[value=submit]").hide();
			$("input[type=Submit]").hide();
			$("input[value=Submit]").hide();
			var subSection = document.getElementById("formSubmitSection");
			form.appendChild(subSection);
		}
		
		var hiddenDetails = document.getElementById("hiddenDetails");
		if (hiddenDetails) {
			form.appendChild(hiddenDetails);
		}
		form.action = formAction;
	} else {
		validateMessageBox(form.title.error, form.msg.scriptError + formId, "error");
	}
}

function addButtons(element,isOperationfnSubmit,isFinalTransactor,formId) {
	var submitButton = element.clone(true);
	var leftPosition = element.css("left");
	var left = leftPosition.split("px");
	var taskId = $('#taskId').val();

	if(submitButton.length == 1 ) {
		submitButton.attr("type", "button");
		submitButton.attr("class", "btn btn-info");
		submitButton.attr("value", "Submit");
		submitButton.css({"left":parseInt(left[0])+100});
		submitButton.show().insertAfter(element);
		console.log(submitButton);
	}
	var saveButton =  element.clone(true);

	if(saveButton.length == 1 ) {

		saveButton.attr("type", "button");
		saveButton.attr("class", "btn btn-info");
		saveButton.attr("value", "Save")
		saveButton.attr("onclick", "ofSaveForm( '"+ formId +"' , '"+taskId+"')");
		saveButton.css({"left":parseInt(left[0])+200});
		saveButton.show().insertAfter(element);
		console.log(saveButton);
	}
	var $cloneCancelButton =  element.clone(true);
		if($cloneCancelButton.length == 1 ) {
			$cloneCancelButton.attr("type", "button");
			$cloneCancelButton.attr("class", "btn btn-info");
			$cloneCancelButton.attr("value", "Cancel")
			$cloneCancelButton.attr("onclick", "cancelTaskView( '"+ taskId +"')");
			$cloneCancelButton.css({"left":parseInt(left[0])+400});
			$cloneCancelButton.show().insertAfter(element);
		}
	
	if (isOperationfnSubmit && isOperationfnSubmit == "true") {
		if (isFinalTransactor && isFinalTransactor == "true") {
			submitButton.attr("onclick",
					"ofJointConductSubmitForm('"+ formId + "','')");
		} else {
			submitButton.attr("onclick", "ofSubmitForm('" + formId
					+ "')");
		}
	} else {
		submitButton.attr("onclick", "unPrevilegeMessage()");
	}
}

// Clear File upload field Function
function clearField(id) {
	$("#" + id).val('');
}

function clearFileUpload(id) {
	clearField(id);

}

function _mailSubject(cellValue, options, rawObject) {
	if (rawObject.isDraft == true || rawObject.isDraft == 'true') {
		return '<a href="#bpm/mail/jwma/getMessage" onclick="_execute(\'target\',\'acton=message&folderName='
				+ rawObject.folderName
				+ '&todo=composedraft&number='
				+ rawObject.messageNumber
				+ '\');">'
				+ rawObject.subject
				+ '</a>';
	} else {
		return '<a href="#bpm/mail/jwma/getMessage?acton=message&folderName='+rawObject.folderName+'&todo=display&number='+rawObject.messageNumber+'");">'
				+ rawObject.subject
				+ '</a>';
	}
}

function _mailFrom(cellValue, options, rawObject) {
	if (rawObject.isDraft == true || rawObject.isDraft == 'true') {
		return '<a href="#bpm/mail/jwma/getMessage" onclick="_execute(\'target\',\'acton=message&folderName='
				+ rawObject.folderName
				+ '&todo=composedraft&number='
				+ rawObject.messageNumber
				+ '\');">'
				+ rawObject.from
				+ '</a>';
	} else {
		return "<a href='#bpm/mail/jwma/getMessage?acton=message&folderName="+rawObject.folderName+"&todo=display&number="+rawObject.messageNumber+"'>"+rawObject.from+"</a>";
	}
}

function mailGridSearch(gridObj) {
	var search=$("#mailSearchBox").val();
	var filters = "";
	if (search == "") {
		filters = "";
	} else {
		filters = {
			"groupOp" : "OR",
			"rules" : [ {
				"field" : "subject",
				"op" : "cn",
				"data" : search
			},{ 
				"field": "from",
				"op": "cn", 
				"data": search
			}]
		};
	}

	mailGridFilter(filters, "#"+gridObj);
}

function mailGridFilter(filtersparam, grid) {
	$(grid).setGridParam({
		postData : {
			filters : filtersparam
		},
		search : true
	});
	$(grid).setGridParam({
		page : 1
	});
	$(grid).trigger("reloadGrid");
}

function linkToNavigate(url, target){	
	var tagId = "";	
	if(target == 'frame' && target != 'popup' && target != '_blank'){	
	$('a').click(function(event) {
	   tagId = $(this).attr("id");	 
	   var contentId = tagId.slice(-1);	
		if($(this).attr("targetoption") == "frame") {
	 		$('#wizardTab-content-'+contentId).html("<iframe id='TabFrame' src='"+url+"' width=100% height=300 marginwidth=0 				marginheight=0 hspace=0 vspace=0 frameBorder=0 scrolling=auto></iframe>"); 
		} 
	});
		
	}else if(target == 'popup' && target != 'frame'){		
		window.open(url,'_blank',"width=700,height=500");
		$("#TabFrame").hide();
	}else{
		window.open(url, '_blank');
		$("#TabFrame").hide();
	}

}

//To find the obj text is in the array
function checkRoles(arr, obj) {
	var result = false;
    for(var i=0; i<arr.length; i++) {
	    if (arr[i]== obj){
	    	result = true;
	    	break;
	    } else {
	    	result = false;
	    }
    }
    return result;
}

//setting values for same form 
/*function setValuesForSameForm(formId,pastValues){
   var isValueSet =false;
   //var pastValuesJson = JSON.parse(pastValues);
   var elements = document.getElementById(formId).elements;
   for (var i=0;i<elements.length;i++){
	   var columnId = elements[i].getAttribute('column');
       if(pastValues[columnId]){
           if(elements[i].type == "radio" || elements[i].type == "checkbox"){
               var checkedValues=pastValues[columnId].split(","); 
               if(checkedValues.indexOf(elements[i].value) != -1) {
                       elements[i].checked = true;
                       isValueSet =true;
               }
           }else if(elements[i].type == 'select'){
              if ($('option[value='+pastValues[columnId]+']', '#'+element_id).length) {
                      $("#"+element_id).val(pastValues[columnId]);
                      isValueSet =true;
              } 
           }else if(elements[i].type == 'file'){
        	   isValueSet =true;
           }else {
               elements[i].value = pastValues[columnId];
               isValueSet =true;
           }
       }
   }
   return isValueSet;
}*/

//setting values for same form
function setValuesForFormElements(formId, columnId, value,isStartForm) {
	var isValueSet =false;
	var elements = document.getElementById(formId).elements;

	if(columnId == 'startNode' && !isStartForm){
		isStartForm = value;
	}
	for ( var i = 0; i < elements.length; i++) {
		if(elements[i].type != "hidden") {
			var elementColumnId = elements[i].getAttribute('column');
			if(elements[i].getAttribute('issubform') != "true") {
				elementColumnId = elementColumnId
			} else if(elements[i].getAttribute('issubform') == "true") {
				elementColumnId = elementColumnId+"_"+elements[i].getAttribute('name');
			} else {
				elementColumnId = elementColumnId +"_hiddenUserId";
			}
			if(elements[i].getAttribute('column') ){
				if (elementColumnId == columnId) {
					if (elements[i].type == "radio" || elements[i].type == "checkbox") {
						var checkedValues = value.split(",");
						if (checkedValues.indexOf(elements[i].value) != -1) {
							elements[i].checked = true;
							isValueSet =true;
						}
					} else if (elements[i].type == 'select') {
						if ($('option[value=' + value + ']', '#'
								+ elements[i].id).length) {
							$("#" + elements[i].id).val(value);
							isValueSet = true;
						}
					} else if (elements[i].type == 'file') {
						isValueSet = true;
					} else {
						elements[i].value = value;
						isValueSet =true;
					}
				}
			}
		} 
	}
	var labels = document.getElementsByTagName('LABEL');
	for (var i = 0; i < labels.length; i++) {
		//alert(labels[i].getAttribute('column')+"==="+columnId+"==="+value);
		if(	columnId == 'startNode'){
			labels[i].innerHTML = "";
		} else {
			if(labels[i].getAttribute('column') == columnId) {
				labels[i].innerHTML = value;
			}
		}
	}
	
	return isValueSet;
}


function activateTaskByProcessInstanceId(taskId,processInstanceId, processGridType,confirmMessage,resourceName,processDefinitionId) {
	$.msgBox({
		title : form.title.confirm,
		content : confirmMessage,
		type : "confirm",
		buttons : [ {
			value : "Yes"
		}, {
			value : "No"
		} ],
		success : function(result) {
			if (result == "Yes") {
				activateProcessInstanceById(processInstanceId,processGridType,taskId,resourceName,processDefinitionId);
			} else {
				return false;
			}
		}
	});
}

function setImportModuleDetails(id, isShowForm) {
	var moduleName =$("#importModuleName").val();
	if (moduleName == "") {
		moduleName = "Default Module";
	}
	var moduleDataList = $('#searchModuleresults');
	var moduleVal = $(moduleDataList)
			.find('option[value="' + moduleName + '"]');
	if (moduleVal.length != 0) {
		$("input[name='" + id + "']").val(moduleVal.attr('id'));
	} else {
		if (isShowForm == "false") {
			$.msgBox({
				title : form.title.message,
				content : form.msg.chooseExistingModule,
				buttons : [ {
					value : "Ok"
				} ],
				success : function(result) {
					if (result == "Ok") {
						$("#module").val("");
						$("#module").focus();
					}
				}
			});
		} else {
			openMessage(form.title.message, form.msg.chooseExistingModule, "info", false);
		}
	}

}

//method for send chat history to email
function sendChatHistory(userName, userFullName, userEmailId, peerUserName, peerUserFullName, peerUserEmailId, chatMessage){
	var params = 'userName=' + userName + '&userFullName=' + userFullName + '&userEmailId=' + userEmailId + '&peerUserName=' + peerUserName + '&peerUserFullName=' + peerUserFullName + '&peerUserEmailId=' + peerUserEmailId + '&chatMessage=' + chatMessage;
	$.ajax({
		type : 'POST',
		async : false,
		url : 'bpm/notification/notifyChatHistory?'+params,
		success : function(response) {
		}
	});
}


function showProcessList(classification_type){
//	showListViews("PROCESS_LIST","PROCESS_LIST");
		if(classification_type != undefined && classification_type == "document_management"){
			var listViewParam="listViewName=PROCESS_DOCUMENT_LIST&title=PROCESS_DOCUMENT_LIST&container=target&listViewParams=[{}]";
			_execute('target',listViewParam);
			var _hostURL = location.protocol+"//"+window.location.host+"/";
			location.href = _hostURL+"mainMenu##bpm/listView/showListViewGrid";
			
		}else{
			var listViewParam="listViewName=PROCESS_LIST&title=Process%20List&container=target&listViewParams=[{}]";
			_execute('target',listViewParam);
			var _hostURL = location.protocol+"//"+window.location.host+"/";
			location.href = _hostURL+"mainMenu##bpm/listView/showListViewGrid";
		}
}

function backProcessList(){
	$.msgBox({
	    title: form.title.confirm,
	    content: form.msg.unsavedDataWillBeLostConfirmToContinue,
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	    	if(result == "Yes"){
	    		var listViewParam="listViewName=PROCESS_LIST&title=Process%20List&container=target&listViewParams=[{}]";
	    		_execute('target',listViewParam);
	    		var _hostURL = location.protocol+"//"+window.location.host+"/";
	    		location.href = _hostURL+"mainMenu##bpm/listView/showListViewGrid";
	    	}else{
	    		return false;
	    	}
	    }
	});
	window.top.document.getElementById("header_menu").style.display = "";
	window.top.document.getElementById("top_menu").style.display = "";
	window.top.document.getElementById("side_menu").style.display = ""
	window.top.document.getElementById("ajax_loader").style.width = "100%";
	window.top.document.getElementById("dashboard-wrapper").style.marginLeft = "190px";
	var iframeHeight = window.top.document.getElementById("target_iframe").style.height;
	var iframeHeightWithoutPx = iframeHeight.substring(0, iframeHeight.length-2);
	iframeHeightWithoutPx = parseInt(iframeHeightWithoutPx) - 100;
	window.top.document.getElementById("target_iframe").style.height = iframeHeightWithoutPx+"px";

}

function checkMultiTableDump(fileElement, actionPath, formId){
	var tableNames = $(fileElement).val();
	var status = false;
	if(tableNames!=""){
		closeSelectDialog('exportMultiTablePopupDialog');
		document.location.href = "bpm/table/multiTableSqlDump?tableNames="+tableNames;
	}else{
		validateMessageBox(form.title.message, form.msg.tableShouldNotBeEmpty , "info");
		status = false;
	}
	return status;
}

function exportMultiTableDetails(){
	openSelectDialogForFixedPosition("exportMultiTablePopupDialog","","550","255","Export Multi Tables");
	document.location.href = "#bpm/table/exportMultiTables";
}

function deleteData(tableName,viewName){
	var rowid =  gridObj.getGridParam('selarrrow');
	var ids = new Array();
	rowid.forEach(function(item) {
		var id = gridObj.jqGrid('getCell', item, 'id');
		ids[ids.length] = id;
	});
	
	if(rowid.length == 0){
		validateMessageBox(form.title.message, "请选择要删除的记录", "info");
	}
	if(rowid.length !=0 ){
		deleteAll(ids,tableName,viewName);
	}
}

function deleteAll(ids,tableName,viewName){
	var status=false;
	var message="";
	var deleteResult="";
	var deleteType="";
	
	$.msgBox({
	    title: form.title.confirm,
	    content: "确定要删除选中的记录吗？",
	    type: "confirm",
	    buttons: [{ value: "确定" }, { value: "取消" }],
	    
	        	success: function (result) {
	    	        if (result == "确定") {
	    	        	$.ajax({
	    	        		url: 'bpm/table/deleteDataByIds',
	    	                type: 'GET',
	    	                dataType: 'json',
	    	                async: false,
	    	                data: 'ids='+JSON.stringify(ids)+"&tableName="+tableName,
	    	        		success : function(response) {
	    	        			deleteCB(response,viewName,function (res){
	    	        				status=true;
	    	        				if(res){
	    	        					deleteResult="操作成功";
	    	        					deleteType="success";
	    	        					message=response.successMessage;
	    	        				}else{
	    	        					deleteResult="操作失败";
	    	        					deleteType="error";
	    	        					message=response.errorMessage;
	    	        				}
	    	        			});
	    	        		
    	        			}
	    	        	});
	    	        }else{
	    	        	status= false;
	    	  	  }
	    	    },afterClose:function (){
	    	    	if(status){
    	    			$.msgBox({
    						title : deleteResult,
    						content : message,
							type : deleteType
    					});		
	     		    }
	     		}
	});
}

function deleteCB(response,viewName,cb) {
	var status=true;
	if(response.successMessage){ 
		status=true;
	}else{
		status=false;
	}
	showListView(viewName);
	cb(status);
}


function deleteForms(){
	var url = document.URL;
	var versionGridUrlStatus;
	var newUrl = url.split("#").pop();
	
	if(newUrl.contains("bpm/form/showAllFormVersions") || newUrl.contains("bpm/form/restoreForm")) {
		versionGridUrlStatus = true;
	
	} else {
		versionGridUrlStatus = false;		
	}

 	var data =  gridObj.jqGrid('getGridParam','data');
 	var formNames=[];
 	var status=false;
	var message="";
	var deleteResult="";
	var deleteType="";
	var isActive = [];
 	 if(data!=""){
 		var selectedRowId =  $(gridObj).jqGrid ('getGridParam', 'selarrrow');
 		
 		if(selectedRowId!=""){
 			if ( !Array.prototype.forEach ) {
 				for(var i=0;i<selectedRowId.length;i++){
 					for(var index=0;index<data.length;index++){
 						if(data[index].id==selectedRowId[i]){
 						formNames[formNames.length]=data[index].formName;
 						isActive = data[index].active;
 						}
 					}
 				}
 			} else {
 				selectedRowId.forEach(function(item) {
 				data.forEach(function(valuRow) {
 					if(valuRow.id==item){
 						formNames[formNames.length]=valuRow.formName;
						isActive[formNames.length] = valuRow.active;
 					}
 				});
 	 			});
 			}
 			if ( isActive.indexOf('true') !== -1 && versionGridUrlStatus){
         			validateMessageBox(form.title.message, form.msg.canotDeleteActiveVersionOfForm, "error");
				return false;     
    }
		if(versionGridUrlStatus) {
			msgContent = form.msg.confirmToDeleteTheseFormVersions;
		} else {
			msgContent = form.msg.confirmToDeleteTheseForms;	
		}
 			$.msgBox({
 			    title: form.title.confirm,
 			    content: msgContent,//form.msg.confirmToDeleteTheseForms,
 			    type: "confirm",
 			    buttons: [{ value: "Yes" }, { value: "No" }],
 			    
 			        	success: function (result) {
 			    	        if (result == "Yes") {
 			    	        	$.ajax({
 			    	        		url: 'bpm/form/deleteFormsByNames',
 			    	                type: 'GET',
 			    	                dataType: 'json',
 			    	                async: false,
 			    	                data: 'formNames='+JSON.stringify(formNames)+"&formName="+formNames+"&selectedRowId="+selectedRowId+"&versionGridUrlStatus="+versionGridUrlStatus,
 			    	        		success : function(response) {
 			    	        			deleteFormCB(response,function (res){
 			    	        				status=true;
 			    	        				if(res){
 			    	        					deleteResult=form.title.success;
 			    	        					deleteType="success";
 			    	        					message=response.successMessage;
 			    	        				}else{
 			    	        					deleteResult=form.title.error;
 			    	        					deleteType="error";
 			    	        					message=response.errorMessage;
 			    	        				}
 			    	        			});
 			    	        		
 		    	        			}
 			    	        	});
 			    	        }else{
 			    	        	status= false;
 			    	  	  }
 			    	    },afterClose:function (){
 			    	    	if(status){
 			    	    			$.msgBox({
 		        						title : deleteResult,
 		        						content : message,
 		    							type : deleteType
 		        					});		
 			     		    }
 			     		}
 			});
 		}else{
 			validateMessageBox(form.title.message, form.msg.selectAtleastSingleFormToDelete, "info");
 		}
 	 }else{
 		validateMessageBox(form.title.message, form.msg.noEmptyListView, "info");
     }
}

function deleteFormCB(response,cb) {
	var status=true;
	if(response.successMessage){ 
		status=true;
	}else{
		status=false;
	}
	
	var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
	var isEdit=selected_nodes.attr("isEdit");
	var isSystemModule=selected_nodes.attr("issystemmodule");
	var nodePath = $("#module_relation_tree").jstree("get_path", $("#"+selected_nodes.attr("id")), true); 
	var nodeLevel = nodePath.length;
	var moduleId = nodePath[0];
	 
	var params = 'moduleId='+moduleId+'&isEdit='+isEdit+'&isSystemModule='+isSystemModule+'&isJspForm='+false;
	selectedFromModuleId=moduleId;
 	document.location.href = "#bpm/form/formListForModule";
    _execute('rightPanel',params); 
     	
	cb(status);
}

function getFormHeight(form){
	var formHeight=$(form).css("height");
	var tempFormHeight=formHeight.replace("px","");
	var formStyle=form.getAttribute("style");
	
	var bgImgPath=$(form).css("background-image");
	if(bgImgPath != null && bgImgPath != "" && bgImgPath != "null" && bgImgPath != "none"){
		var minHeight=$(form).css("min-height");
		var tempMinHeight=minHeight.replace("px","");
		if(parseInt(tempMinHeight)<parseInt(tempFormHeight)){
			return formHeight;
		}else{
			return null;
		}
	}else{
		return null;
	}
}

function setAgentSetting(){
	 var rowid =  gridObj.getGridParam('selarrrow');
		var processIds=new Array();
		var processNames=new Array();
		var i = 0;
		rowid.forEach(function(item) {
			var processId = gridObj.jqGrid('getCell', item, 'id');
			var agentId = gridObj.jqGrid('getCell', item, 'agentId');
			if(agentId!="" && agentId!=null){
				var processName = gridObj.jqGrid('getCell', item, 'Process_Name');
				processNames[processNames.length]=processName;
			}
			processIds[i] = processId;
			i++; 
		});
		
		if(rowid.length == 0){
			validateMessageBox(form.title.selectProcess, form.msg.pleaseSelectAProcess, "info");
		}else if(rowid.length==1){
			if(processNames.length==0){
				 openSelectDialogForFixedPosition("popupDialog","","620","440","Agency Setting");
				 document.location.href = "#bpm/agency/agencySetting";
			}else{
				validateMessageBox(form.title.unselectProcess, form.msg.processName + processNames + form.msg.alreadyHasAgencySetting, "info");
				/*$.msgBox({
		   		    title:"Unselect Process",
		   		    content:"Process name '"+processNames+"' already has Agency Setting"
		   		});*/
			}
		}else{
						validateMessageBox(form.title.selectProcess, form.msg.pleaseSelectAProcess, "info");
		}
}

function removeAgentSetting(){
	var rowid =  gridObj.getGridParam('selarrrow');
	var agentIds=new Array();
	var status=false;
	var deleteResult="";
	var deleteType="";
	var message="";
	
	rowid.forEach(function(item) {
		var agentId = gridObj.jqGrid('getCell', item, 'agentId');
		if(agentId!="" && agentId!=null){
			agentIds[agentIds.length]=agentId;
		}
	});
	
	if(rowid.length == 0){
								validateMessageBox(form.title.selectProcess, form.msg.pleaseSelectAProcess, "info");
		/*$.msgBox({
   		    title:"Select Process",
   		    content:"Please select a process "
   		});*/
	}else if(agentIds.length != 0){
		$.msgBox({
			    title: form.title.confirm,
			    content: form.msg.confirmToRemoveAgencySetting,
			    type: "confirm",
			    buttons: [{ value: "Yes" }, { value: "No" }],
			        	success: function (result) {
			    	        if (result == "Yes") {
			    	        	$.ajax({
			    	        		url: 'bpm/agency/removeAgentSettings',
			    	                type: 'GET',
			    	                dataType: 'json',
			    	                async: false,
			    	                data: 'agentIds='+JSON.stringify(agentIds),
			    	        		success : function(response) {
		    	        				status=true;
		    	        				if(response){
		    	        					deleteResult=form.title.success;
		    	        					deleteType="success";
		    	        					message=response.message;
		    	        				}else{
		    	        					deleteResult=form.title.error;
		    	        					deleteType="error";
		    	        					message=response.message;
		    	        				}
			    	        		}
			    	        	});
			    	        }else{
			    	        	status= false;
			    	  	  }
			    	    },afterClose:function (){
			    	    	if(status){
			    	    			$.msgBox({
		        						title : deleteResult,
		        						content : message,
		    							type : deleteType
		        					});
		        						showListViews("AGENCY_LIST","AGENCY LIST");
		        			}
			     		}
			});
		
	}else{
		validateMessageBox(form.title.selectProcess, form.msg.pleaseSelectAProcessWithAgencySetting, "info");
		/*$.msgBox({
   		    title:"Select Process",
   		    content:"Please select a process with Agency Setting "
   		});*/
	}
		
}

function agencyEdit(cellValue, options, rawObject){
	if(rawObject.agentId!="" && rawObject.agentId!=null){
		return '<a id="editAgency"  href="javascript:void(0);" data-role="button" data-theme="b" onclick="editAgency(\''+rawObject.agentId+'\');"><b>Edit</b></a>';
	}else{
		return '';
	}
}

function editAgency(agentId){
	openSelectDialogForFixedPosition("popupDialog","","620","440","Edit Agency Setting");
	document.location.href = "#bpm/agency/editAgencySetting?agentId="+agentId;
}

function getModuleName(){
	
	var searchName=$("#moduleName").val();
	var id;
	if(searchName!=""){
		$.ajax({
			type : 'GET',
			async : false,
			url : '/bpm/table/allModules',
			success : function(data) {
				var dataList = $("#searchModuleresults");
				dataList.empty();
				$.each(data, function(index, item) {
					var selectOption = document.createElement("OPTION");
					if (!($("input[name='" + id + "']").val())) {
						if (item.moduleName == "Default Module") {
							$("input[name='" + id + "']").val(item.id);
						}
					}
					selectOption.text = item.moduleName;
					selectOption.value = item.moduleName;
					selectOption.id = item.id;
					dataList.append(selectOption);
				});
			}
		});
	}
}

function checkModuleName() {
	
	var moduleName =$("#searchModuleName").val();
	if (moduleName == "") {
		moduleName = "Default Module";
	}
	var moduleDataList = $('#searchModuleresults');
	var moduleVal = $(moduleDataList)
			.find('option[value="' + moduleName + '"]');
	if (moduleVal.length != 0) {
		$("input[name='" + id + "']").val(moduleVal.attr('moduleName'));
	} else {
			$.msgBox({
				title : form.title.message,
				content : form.msg.chooseExistingModule,
				buttons : [ {
					value : "Ok"
				} ],
				success : function(result) {
					if (result == "Ok") {
						$("#searchModuleName").val("");
						//$("#searchModuleName").focus();
					}
				}
			});
	}

}

function createJasperReport(){
	var host = window.location.protocol+'//'+window.location.hostname+':'+window.location.port;
	var reportUrl = host+"/jasperserver/flow.html?_flowId=searchFlow&j_username=jasperadmin&j_password=jasperadmin";
	openURLInNewTab(reportUrl);
}

function openURLInNewTab(url)
{
  var win=window.open(url, '_blank');
  win.focus();
}

function callLocationByUrl(headUrl){
	document.location.href = headUrl;
	_execute('target', '');
}

function cloneDiv(divId){
	var count = $('div[id^='+divId+']').length;
	var last = $('div[id^='+divId+']').last();
	var $clone = last.clone(true);
	var next_id = count + 1;
	$clone.attr('id', divId+"_"+next_id);
	$clone.attr('class', 'clone');
	$clone.show().insertAfter(last);
	$('div[id^='+divId+']').last().find("*[id]").each(function() { 
		//if( $(this).attr("type").indexOf("file") < 0 ) {
			$(this).attr("id",  +next_id);
		//}
	});
	$('div[id^='+divId+']').last().find("*").each(function() { 
		$(this).attr("value",  "");
		$(this).attr("class",  'clone');
		 var topPosition =  $(this).css("top");
		 var leftPosition =  $(this).css("left");
		 var top = topPosition.split("px");
		 var left = leftPosition.split("px");
			if($(this).text() == "Add"){
				$(this).css({"top":parseInt(top[0])+35});
			}else if($(this).text() == "Remove"){
				$(this).css({"top":parseInt(top[0])+35});
			} else {
				$(this).css({"top":parseInt(top[0])+35});
			}
		});		
}  

function _showToUser(rowId, tv, rawObject, cm, rdata) {
	var username;
	if(rowId==null){
		return '无';
	}else{
		$.ajax({
			url:'bpm/messages/getUserByUserid?userid='+rowId,
			type:'GET',
			async: false,
			success : function(resultData) {
				username=resultData.usernames;
			}
		});
		return username;
	}
}
