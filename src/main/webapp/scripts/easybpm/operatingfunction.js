function submitAddMembers(type, url){
	 $.ajax({
         type: "POST",
         url: url,
         data: $("#addMember").serialize(), // serializes the form's elements.
         success: function(response)
         {
        	if(response.success){
				$.msgBox({
					title: form.title.success,
					content: response.success,
				    type: "success"					    
				});
				closeSelectDialog('involveUsersPopupDialog');
			}else{					
		    	validateMessageBox(form.title.error, response.failure, "error");
			}
         }
       });

	 return false;
}
function terminate(executionId,taskId,resourceName,processDefinitionId){
	var params = 'executionId='+executionId+"&taskId="+taskId+"&resourceName="+resourceName+"&processDefinitionId="+processDefinitionId;
	$.msgBox({
	    title: form.title.confirm,
	    content: "确定要终止流程？",
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	document.location.href = "#bpm/of/terminate";
	    		_execute('task_target',params);
	        }else{
	        	return false;
	        }
	    }
	});
}

function suspend(processInstanceId,taskId,resourceName,suspendState,processDefinitionId){
	if(suspendState && suspendState == "1"){
		var params = 'processInstanceId='+processInstanceId+"&taskId="+taskId+"&resourceName="+resourceName+"&processDefinitionId="+processDefinitionId;
		$.msgBox({
			title: form.title.confirm,
		    content: "确定要暂停流程？",
		    type: "confirm",
		    buttons: [{ value: "Yes" }, { value: "No" }],
		    success: function (result) {
		        if (result == "Yes") {
			    	//  for showing success messages
			    	setTimeout(function(){
		        	document.location.href = "#bpm/of/suspend";	        	
		        	_execute('task_target',params);
			    	},200);
		        }else{
		        	return false;
		        }
		    }
		});	
	}else{
		$.msgBox({
		    title:form.title.message,
		    content:"Process instance is already suspended "
		});
	}
}

function withdraw(processInstanceId,taskId,resourceName,processDefinitionId){
	var params = 'processInstanceId='+processInstanceId+"&taskId="+taskId+"&resourceName="+resourceName+"&processDefinitionId="+processDefinitionId;
	$.msgBox({
	    title: form.title.confirm,
	    content: "确定要撤办吗？",
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
		    	//  for showing success messages
		    	setTimeout(function(){
		    		        	document.location.href = "#bpm/of/withdraw";	        	
		    		        	_execute('task_target',params);
		    	},200);
	        }else{
	        	return false;
	        }
	    }
	});	
}

function showRecall(processInstanceId,taskId,resourceName,processDefinitionId){
	var params = 'processInstanceId='+processInstanceId+"&taskId="+taskId+"&resourceName="+resourceName+"&processDefinitionId="+processDefinitionId;
	setTimeout(function(){
		document.location.href = "#bpm/of/recallTask";	        	
		_execute('task_target',params);
	},200);
}

function ofSubmitForm(formId){

	var form = document.getElementById(formId);
	if(form){
		for(var instanceName in CKEDITOR.instances) {
		    CKEDITOR.instances[instanceName].updateElement();
		}
		if(document.getElementById("isUpdate").value=="true"){
		var signOff=document.getElementById("isSignOff");
		if(signOff){
			if(document.getElementById("isUpdateOnly").value=="true"){
				signOff.value="false";
			}else{
				signOff.value="true";
			}
			
		}			
		form.action="bpm/form/updateTaskForm";
		_execute('task_target','');
	}else{
			form.action="bpm/of/submit";
			_execute('task_target','');
		}
		
		$("#"+formId).submit();
	}else{
		alert("No form element found");
	}
}

function isFormValuesValid() {
	var isFormValid = true;
	
	var jumpType = $('#jumpType').val();
	if(jumpType == 'jump' || jumpType == 'return' ) {
		isFormValid = true;
	} 
	
	$(".mandatory").each(function() {
		if ($.trim($(this).val()).length == 0) {
			$(this).addClass("highlight_err");
			isFormValid = false;
		} else {
			$(this).removeClass("highlight_err");
		}
	});
	$( ".data_dictionary_radio" ).each(function() {	
		var columnName = this.getAttribute("name");
		var isRequired = this.getAttribute("isRequired");
		if(isRequired == "mandatory") {
			if($('input[name='+columnName+']:checked').length<=0)
			{
			$(this).addClass("highlight_err");
			 isFormValid = false;
			}
		}
	});
	$( ".data_dictionary_checkbox" ).each(function() {	
		columnName = this.getAttribute("name");
		isRequired = this.getAttribute("isRequired");
		if(isRequired == "isRequired") {
			if($('input[name='+columnName+']:checked').length<=0)
			{
			$(this).addClass("highlight_err");
			 isFormValid = false;
			}
		}
	});
	return isFormValid;
}


function ofJointConductSubmitForm(formId, taskId){
	var suspendState = $("#suspendState").val();
	/*if(endScriptName != undefined && endScriptName != "") {
		endScriptName;
	}*/
	var endScriptName = '';
	if ( !isFormValuesValid()) {
		validateMessageBox(form.title.message, form.msg.mandatoryMsg , false);
	} else {
			//appendCheckboxValues(formId);
			endScriptName = document.forms[formId].elements['endScriptName'].value;
			if(suspendState && suspendState == "2"){
				$.msgBox({
				    title:form.title.message,
				    content:form.msg.activateMsg
				});
			}else{
				if(!taskId){
					taskId = document.forms[formId].elements['taskId'].value;
				}
				$.ajax({
			    url: "bpm/of/checkAndGetNextOrganizer",
			    data: $("#"+formId).serialize(),
			    type: "POST",
			    dataType: 'json',
			    success: function(response){
			    	var nodeType = "";
			    	if(response != null && response != ""){
			    		var organizerObj = [];
			    		var isOrganizerExist = true;
			    		for(var i in response){
			    	    	if(response[i].error){
						    	validateMessageBox(form.title.error, response[i].error, "error");
						    	isOrganizerExist = false;
						    	//$( "div" ).remove( "#hidden" );
						    	break;
			    	    	}else if(response[i].isNextTaskExist == "true"){
			    	    		nodeType = response[i].nodeType;
			    	    		//break;
			    	    	}else {
			    	    		organizerObj.push({organizer: response[i].organizer,organizerProperty:response[i].organizerProperty});
			    	    		if(response[i].nodeType != undefined) {
			    	    			nodeType = response[i].nodeType;
			    	    		}
			        		}
			    		}
			    		if(isOrganizerExist){
			    			showNextTaskAddMembers(taskId, 'jointConduction', "选择下一节点办理人" , formId,JSON.stringify(organizerObj),"","", nodeType,endScriptName,false);
			    		}
			    	}else{
			    		//$( "div" ).remove( "#hidden" );
			    		submitJointConductionForm(formId,false);
			    	}
			    }
			});
		}
	}
}

function showNextTaskAddMembers(taskId, type, title, formId,organizersList,activityId,resourceName, nodeType,endScriptName,canSave) {
	clearPreviousPopup();
	var params = 'taskId=' + taskId + '&type=' + type + '&formId=' + formId + '&organizersList=' +organizersList+ '&activityId=' + activityId + '&resourceName=' +resourceName+ 
	'&nodeType=' +nodeType+ '&endScriptName=' +endScriptName+"&canSave="+canSave;
	document.location.href = "#bpm/of/showNextTaskAddMembers";
	// openSelectDialog(params,"250","125",type);
	_execute('involveUsersPopupDialog', params);
	$myDialog = $("#involveUsersPopupDialog");
	$myDialog.dialog({
		width : 'auto',
		height : 'auto',
		modal : true,
		zIndex:900,
		title : title
	});
	$myDialog.dialog("open");
}

function ofSaveForm(formId){
	
	if ( !isFormValuesValid()) {
		validateMessageBox(form.title.message, "必填项不能为空" , false);
	} else {
		var count = $("#subFormTable tr").length;
		$('#subFormCount').val(count-1);
		var form = document.getElementById(formId);
		document.getElementsByName('isSave')[0].value = "true";
		if(form){
			for(var instanceName in CKEDITOR.instances) {
			    CKEDITOR.instances[instanceName].updateElement();
			}
			var subFormCount =  $('#subFormCount').val();
			if(subFormCount > 1) {
				$('#htmlSourceForSubForm').val(document.getElementById("form_div").innerHTML);
			}
			if(document.getElementById("isUpdate").value=="true"){
				var signOff=document.getElementById("isSignOff");
				if(signOff){
					if(document.getElementById("isUpdateOnly").value=="true"){
						signOff.value="false";
					}else{
						signOff.value="false";
					}
				}	
				form.action="bpm/form/updateTaskForm";
				_execute('task_target','');
			}else{
				form.action="bpm/of/save";
				_execute('task_target','');
			}
			appendCheckboxValues(formId);
			$("#"+formId).submit();
		}else{
			alert("No form element found");
		}
	}
}


function appendCheckboxValues(formId) {
	var form = document.getElementById(formId);
	$( ".data_dictionary_checkbox" ).each(function() {	
		columnName = this.getAttribute("name");
		if(!$(this).is(':checked')) {
			var checkboxDiv=document.createElement("div");
			checkboxDiv.id = "hidden";
			//checkboxDiv.class = "checkbox";
			checkboxDiv.innerHTML='<input type="hidden"/>';
			form.appendChild(checkboxDiv);
		}
	});
	/*$( ".data_dictionary_radio" ).each(function() {	
		columnName = this.getAttribute("name");	
		if(!$(this).is(':checked')) {
			var div=document.createElement("div");
			div.id = "hidden";
			div.innerHTML='<input type="hidden" name='+columnName+' />';
			form.appendChild(div);
		}
	});*/

}

function executeJavaEvent(formId,type){
	var javaEvent = $('#'+formId).attr(type);
	if(javaEvent != undefined) {
		$.ajax({	
			type: 'GET',
			async: false,
			url : 'bpm/of/executeJavaCode',
			data: { 'javaEvent' : javaEvent} ,
			success : function(response) {
				if(response != null) {
					if(response.result == "success") {
						$.msgBox({
							title : form.title.success,
							type : "Info",
							content : form.msg.javaEventSuccess
						});
					} else {
						validateMessageBox(form.title.error, response.result, "error");
					}
				}
			}
		});
	}
}
function submitJointConductionForm(formId,isNextTaskExist){
	var organizers = document.getElementById("nextTaskOrganizersSelect");
	var coordinators = document.getElementById("nextTaskCoordinatorsSelect");
	var organizerOrders = document.getElementById("taskOrganizerOrder");
	var superior = document.getElementById("taskSuperior");
	var subordinate = document.getElementById("taskSubordinate");
	var organizerValues;
	var coordinatorsValues;
	var form = document.getElementById(formId);
	executeJavaEvent(formId,'javaEventOnSubmit');
	var count = $("#subFormTable tr").length;
	$('#subFormCount').val(count-1);
	if(isNextTaskExist){
		if((organizers && coordinators)){
			organizerValues = organizers.value;
			coordinatorsValues = coordinators.value;
			if((organizerValues!=null && organizerValues!="")){
				var isJointConduction = document.getElementById("isJointConduction");
				document.getElementById("nextTaskOrganizers").value=organizerValues;
				var coOrdinators = document.getElementById("nextTaskCoordinators").value;
				if(coOrdinators){
					document.getElementById("nextTaskCoordinators").value=coOrdinators+","+coordinatorsValues;
				}else{
					document.getElementById("nextTaskCoordinators").value=coordinatorsValues;
				}
				document.getElementById("organizerOrders").value=organizerOrders.value;
				document.getElementById("organizerSuperior").value=superior.value;
                document.getElementById("organizerSubordinate").value=subordinate.value;

				if(isJointConduction){
					isJointConduction.value="true";
					form.appendChild(isJointConduction);
				}
			}else{
				messageBoxShow(form.title.info, "主办人不能为空" , "info");
				return;
			}
		}else{
			messageBoxShow("Warning", "No organizer,co-ordinator element found!" , "info");
			return;
		}	
	} else {
		document.getElementsByName('isLastTask')[0].value = "1";

	}
	if(form){	
		
		if ($("#isMail").is(':checked')) {
			$("#sendMail").val("isMail");
		}
		if ($("#isInternalMessage").is(':checked')) {
			$("#sendInternalMail").val("isInternalMessage");
		}
		var subFormCount =  $('#subFormCount').val();
		if(subFormCount > 1) {
			$('#htmlSourceForSubForm').val(document.getElementById("form_div").innerHTML);
		}
		closeSelectDialog('involveUsersPopupDialog');
		if(document.getElementById("isUpdate").value=="true"){
			var signOff=document.getElementById("isSignOff");
			if(signOff){
				if(document.getElementById("isUpdateOnly").value=="true"){
					signOff.value="false";
				}else{
					signOff.value="true";
				}
				
			}			
			form.action="bpm/form/updateTaskForm";
			_execute('task_target','');
		}else{
			form.action="bpm/of/submit";
			_execute('task_target','');
		}
		$("#"+formId).submit();
	}else{
		messageBoxShow(form.msg.noForm);
		return;
	}
}

//激活所有disabled的元素，以保存到流程变量
function activeDisabledElementBeforeSubmit(form){
	var elements = form.elements;
	
	for ( var i = 0; i < elements.length; i++) {
		if(elements[i].disabled == true){
			elements[i].removeAttribute("disabled");
		}
	}
}


function ofJointConductStartProcess(processKey,formId){
	$.ajax({
	    url: "bpm/manageProcess/getOrganizersForStartProcess?processKey=" + processKey,
	    type: "GET",
	    dataType: 'json',
	    success: function(response){
	    	if(response != null && response != ""){
	    		var nodeType = "";
	    		var organizerObj = [];
	    		var isOrganizerExist = false;
	    		var isNodeWithForm = false;
	    		var isError = false;
	    		for(var i in response){
	    	    	if(response[i].error){
				    	validateMessageBox(form.title.error, response[i].error, "error");
				    	isError = true;
				    	break;
	    	    	}else if(response[i].isNextTaskExist){
	    	    		nodeType = response[i].nodeType;
	    	    		isOrganizerExist = true;
	    	    	//	break;
	    	    	}else if(response[i].nodeWithForm){
	    	    		isNodeWithForm = (isNodeWithForm == response[i].nodeWithForm);
	    	    	}else {
	    	    		isOrganizerExist = true;
	    	    		organizerObj.push({organizer: response[i].organizer,organizerProperty:response[i].organizerProperty});
	    	    		if(response[i].nodeType != undefined) {
	    	    			nodeType = response[i].nodeType;
	    	    		}
	        		}
	    		}
	    		if(isOrganizerExist){
	    			showNextTaskAddMembers(processKey, 'startProcess', form.msg.selectTransactorsForStartProcess, formId,JSON.stringify(organizerObj),"","", nodeType,'',false);
	    		}else if(isNodeWithForm){
					var params= "nextTaskOrganizers=&nextTaskCoordinators=&nextTaskOrganizerOrders=&processKey="+processKey+"&isSystemDefined="+true+"&processInstanceId=null";
					_execute("target",params);
					document.location.href = "#bpm/form/submitStartForm";
	    		}else if(!isNodeWithForm && !isError){
	    			validateMessageBox(form.title.message,form.msg.formMandatoryForProcessStart,false);
	    		}else if(isError){
	    			validateMessageBox(form.title.message,form.msg.processStartError,false);
	    		}
	    	}
	    }
	});
}

function getUsersToInvolveForStartProcess(processKey,type,title,formId){
	clearPreviousPopup();
	var params = 'processKey='+processKey+'&type='+type+'&formId='+formId;
	document.location.href = "#bpm/of/showAddMembersForProcess";
	//openSelectDialog(params,"250","125",type);
	 _execute('involveUsersPopupDialog',params);
	 $myDialog = $("#involveUsersPopupDialog");
	 $myDialog.dialog({
                        width: 'auto',
                        height: 'auto',
                        modal: true,
                        title: title,
                        zIndex:900
	             });
	 $myDialog.dialog("open");
}

function submitJointConductionProcess(formId,processKey,formAction,endEventScriptName,isDefaultTask){
	var organizers = document.getElementById("nextTaskOrganizersSelect");
	var coordinators = document.getElementById("nextTaskCoordinatorsSelect");
	var organizerOrders = document.getElementById("taskOrganizerOrder");
	var organizerValues;
	var coordinatorsValues;
	//Execute process with start form
	if(organizers && coordinators){
		organizerValues = organizers.value;
		coordinatorsValues = coordinators.value;
		organizerOrdersValues = organizerOrders.value;
		if(organizerValues!=null && organizerValues!=""){
			if(formId){			
				var form = document.getElementById(formId);				
				document.getElementById("nextTaskOrganizers").value=organizerValues;
				var coOrdinators = document.getElementById("nextTaskCoordinators").value;
				if(coOrdinators){
					document.getElementById("nextTaskCoordinators").value=coOrdinators+","+coordinatorsValues;
				}else{
					document.getElementById("nextTaskCoordinators").value=coordinatorsValues;
				}
				document.getElementById("organizerOrders").value=organizerOrders.value;
				closeSelectDialog('involveUsersPopupDialog');
				form.action=formAction;
				$("#"+formId).submit();
				_execute("target","");
			}else{	
var params;
				if(isDefaultTask == 'true'){
var taskId = document.getElementById("taskId").value;
 params= "nextTaskOrganizers="+organizerValues+"&nextTaskCoordinators="+coordinatorsValues+"&nextTaskOrganizerOrders="+organizerOrdersValues+"&taskId="+taskId;
				}else{	
								 params= "nextTaskOrganizers="+organizerValues+"&nextTaskCoordinators="+coordinatorsValues+"&nextTaskOrganizerOrders="+organizerOrdersValues+"&processKey="+processKey+"&isSystemDefined="+true+"&processInstanceId=null&isFromDoneList=false&creator=";
				}
				_execute("target",params);
				document.location.href = "#"+formAction;
				closeSelectDialog('involveUsersPopupDialog');
			}
		}else{
			messageBoxShow(form.title.message,form.msg.organizerManadatory);
		}
	}else{
		alert(form.msg.noOrganizer);
		return;
	}
}



function ofJointConductStartForm(formId,isDraft){
	var isValid = isFormValuesValid();
	var endScriptName = "";
	endScriptName = document.forms[formId].elements['endScriptName'].value;
	if(!isValid){
		return;
	}
	/*if(endScriptName != undefined && endScriptName != "") {
		endScriptName;
	}*/
	//for save form
	appendCheckboxValues(formId);
	if(isDraft){
		var form = document.getElementById(formId);	
		form['isDraft'].value = isDraft;
		form.action='bpm/form/submitStartForm';
		$("#"+formId).submit();
		_execute("target","");	
	}/*end */else {
		$.ajax({
		    url: "bpm/manageProcess/getOrganizersForStartForm",
		    type: "POST",
		    data: $("#"+formId).serialize(),
		    dataType: 'json',
		    success: function(response){
		    	if(response != null && response != ""){
		    		var nodeType = "";
		    		var organizerObj = [];
		    		var isOrganizerExist = false;
		    		var isNodeWithForm = false;
		    		var isError = false;
		    		for(var i in response){
		    	    	if(response[i].error){
					    	validateMessageBox(form.title.error, response[i].error, "error");
					    	$( "div" ).remove( "#hidden" );
					    	isError = true;
					    	break;
		    	    	}else if(response[i].isNextTaskExist){
		    	    		nodeType = response[i].nodeType;
		    	    		isOrganizerExist = true;
		    	    		//break;
		    	    	}else if(response[i].nodeWithForm){
		    	    		isNodeWithForm = Boolean(response[i].nodeWithForm);
		    	    	}else {
		    	    		isOrganizerExist = true;
		    	    		organizerObj.push({organizer: response[i].organizer,organizerProperty:response[i].organizerProperty});
		    	    		if(response[i].nodeType != undefined) {
		    	    			nodeType = response[i].nodeType;
		    	    		}
		        		}
		    		}
		    		$( "div" ).remove( "#hidden" );
		    		if(isOrganizerExist){
		    			var form = document.getElementById(formId);
		    			var processKey = form.elements["processDefinitionId"].value;
		    			showNextTaskAddMembers(processKey, 'startProcess', 'Select Transactors for Start Process', formId,JSON.stringify(organizerObj),"","",nodeType,endScriptName,false);
		    		}else if(isNodeWithForm){
		    			var form = document.getElementById(formId);		
						form.action='bpm/form/submitStartForm';
						$("#"+formId).submit();
						_execute("target","");	    			
		    		}else if(!isNodeWithForm && !isError){
		    			validateMessageBox(form.title.message,form.msg.formMandatoryForProcessStart,false);
		    		}
		    	}
		    }
		});
	}
}

function messageBoxShow(title,content){
	$.msgBox({
	    title:title,
	    content:content
	});
}


function taskReturn(taskId,resourceName,processDefinitionId,canSave,formId,processInsId){
	var subFormCount =  $('#subFormCount').val();
	if(subFormCount > 1) {
		$('#htmlSourceForSubForm').val(document.getElementById("form_div").innerHTML);
	}
	if(canSave == true){
		var form = document.getElementById(formId);
		if(form){
			for(var instanceName in CKEDITOR.instances) {
			    CKEDITOR.instances[instanceName].updateElement();
			}
			form.action="bpm/of/taskAutoSaveAndBack";
			_execute('task_target','');
			appendCheckboxValues(formId);
			$("#"+formId).submit();
		}else{
			alert("No form element found");
		}
	}else {
		var params = 'taskId='+taskId+"&resourceName="+resourceName+"&returnType=back&processInsId="+processInsId+"&processDefinitionId="+processDefinitionId;
		document.location.href = "#bpm/of/taskReturn";	        	
		_execute('task_target',params);
	}
}

function operatingFunctionPermission(msg){
	$.msgBox({
	    title:form.title.message,
	    content:"You don't have permission to "+msg+" the task"
	});
}

function submitRead(taskId){
	var params = 'taskId='+taskId;
	document.location.href = "#bpm/manageTasks/saveRederDetails";	        	
	_execute('task_target',params);
}

function submitUnRead(taskId){
	var params = 'taskId='+taskId;
	document.location.href = "#bpm/manageTasks/removeRederDetails";	        	
	_execute('task_target',params);
}


function backOffJump(deploymentId,resourceName,processName,processDefinitionId,taskId,activityId,jumpType,processInstanceId,canSave,formId){
	var requestedparams = "deploymentId="+deploymentId+"&resourceName="+resourceName+"&isTitleShow=true&processDefinitionId="+processDefinitionId+"&processName="
	+processName+"&taskId="+taskId+"&activityId="+activityId+"&jumpType="+jumpType+"&processInstanceId="+processInstanceId+"&canSave="+canSave+"&formId="+formId;
	var container = "processPreviewPopupDialog";
	clearTreePopupDiv();
	setTimeout(function(){
		document.location.href = "#bpm/manageTasks/backOffJumpPreview";
		_execute(container, requestedparams);
	},200);
	$.myDialog = $("#" + container);
	$.myDialog.dialog({
		width : 'auto',
		height : 'auto',
		modal : true,
		title : "当前节点名称 - " + processName
	});
	$.myDialog.dialog("open");
}

//for jump
function jumpTaskNodes(deploymentId,resourceName,processName,processDefinitionId,taskId,activityId,jumpType,processInstanceId,canSave,formId,executionId){
	var requestedparams = "deploymentId="+deploymentId+"&resourceName="+resourceName+"&isTitleShow=true&processDefinitionId="+processDefinitionId+"&processName="
	+processName+"&taskId="+taskId+"&activityId="+activityId+"&jumpType="+jumpType+"&processInstanceId="+processInstanceId+"&canSave="+canSave+"&formId="+formId+"&executionId="+executionId;
	var container = "processPreviewPopupDialog";
	clearTreePopupDiv();
	setTimeout(function(){
		document.location.href = "#bpm/manageTasks/jumpPreview";
		_execute(container, requestedparams);
	},200);
	$.myDialog = $("#" + container);
	$.myDialog.dialog({
		width : 'auto',
		height : 'auto',
		modal : true,
		title : "当前节点名称 - " + processName
	});
	$.myDialog.dialog("open");
}

function jumpToTask(canSave,formId){
	var activityDetails = document.getElementById("activityId").value;
	var activityDetailsArr =activityDetails.split("/");
	var activityId =activityDetailsArr[0];
	var nodeType = activityDetailsArr[1];
	var taskIdFromJump = document.getElementById("taskIdFromJump").value;
	var resourceName = document.getElementById("resourceName").value;
	var processDefinitionId = document.getElementById("processDefinitionId").value;
 	var params = 'taskIdFromJump='+taskIdFromJump+"&processName="+processDefinitionId.split(":")[0];
	var organizerObj = [];
	var backOffOrganizer = true; // user who returned task will be shown as potential organizer
	if(activityDetailsArr[2] != "" || activityDetailsArr[2] != undefined) {
		var organizer = activityDetailsArr[2];
	}
	var jumpType = document.getElementById("jumpType").value;
	if( (jumpType == 'jump' && nodeType == 'forwardNode') || jumpType == 'backOff'){
		$.ajax({
		    url: "bpm/of/getOrganizersForJumpTask",
		    data: "taskIdFromJump="+taskIdFromJump+"&activityId="+activityId,
		    type: "GET",
		    dataType: 'json',
		    success: function(response){
		    	if(response != null && response != ""){
		    		var nodeType = "";
		    		var isOrganizerExist = true;
		    		for(var i in response){
		    	    	if(response[i].error){
					    	validateMessageBox(form.title.error, response[i].error, "error");
					    	isOrganizerExist = false;
					    	break;
		    	    	}else if(response[i].isNextTaskExist == "true"){
		    	    		organizerObj.push({organizer: organizer,organizerProperty: "1-false-false"});
		    	    		nodeType = response[i].nodeType;
		    	    		break;
		    	    	}else {
		    	    		organizerObj.push({organizer: response[i].organizer,organizerProperty:response[i].organizerProperty});
		    	    		nodeType = response[i].nodeType;
		    	    		if( (organizer != "" || organizer != undefined || backOffOrganizer == true) &&  (organizer!=response[i].organizer)) {
			    	    		organizerObj.push({organizer: organizer,organizerProperty:response.length+1+"-false-false"});
			    	    		backOffOrganizer = false;
		    	    		}
		        		}
		    		}
		    		if(isOrganizerExist){
		    			$("div#processPreviewPopupDialog").empty();
		    			$("#processPreviewPopupDialog").dialog("destroy");
		    			showNextTaskAddMembers(taskIdFromJump, 'jump', '选择办理人' ,formId,JSON.stringify(organizerObj),activityId,resourceName,nodeType,'',canSave);
		    		}
		    	}
		    }
		});
	}else {
		$("div#processPreviewPopupDialog").empty();
		$("#processPreviewPopupDialog").dialog("destroy");
		if(canSave == true){
			var form = document.getElementById(formId);
			if(form){
				for(var instanceName in CKEDITOR.instances) {
				    CKEDITOR.instances[instanceName].updateElement();
				}
	            var hiddenField1 = document.createElement("input");
	            hiddenField1.setAttribute("type", "hidden");
	            hiddenField1.setAttribute("name", 'activityId');
	            hiddenField1.setAttribute("value", activityId);
	            form.appendChild(hiddenField1);
	            
	            var hiddenField2 = document.createElement("input");
	            hiddenField2.setAttribute("type", "hidden");
	            hiddenField2.setAttribute("name", 'jumpType');
	            hiddenField2.setAttribute("value", jumpType);
	            form.appendChild(hiddenField2);
	  
	            var hiddenField3 = document.createElement("input");
	            hiddenField3.setAttribute("type", "hidden");
	            hiddenField3.setAttribute("name", 'jumpNodeType');
	            hiddenField3.setAttribute("value", nodeType);
	            form.appendChild(hiddenField3);
				form.action="bpm/of/autoSaveAndReturn";
				_execute('task_target',params);
				appendCheckboxValues(formId);
				$("#"+formId).submit();
			}else{
				alert("No form element found");
			}
		}else {
			//taskJump(taskId,activityId,resourceName,jumpType);
			document.location.href = "#bpm/manageTasks/jumpToTask";
			requestedparams = "activityId="+activityId+"&taskIdFromJump="+taskIdFromJump+"&resourceName="+resourceName+"&jumpType="+jumpType+"&nodeType="+nodeType+"&nextTaskOrganizers=" +
					"&nextTaskCoordinators=&nextTaskOrganizerOrders=&processDefinitionId="+processDefinitionId;
			_execute("task_target", requestedparams);
		}
	}
}
function submitJointConductionJumpTask(taskId,activityId,resourceName,formId,canSave){
	var organizers = document.getElementById("nextTaskOrganizersSelect");
	var coordinators = document.getElementById("nextTaskCoordinatorsSelect");
	var organizerOrders = document.getElementById("taskOrganizerOrder");
	var superior = document.getElementById("taskSuperior");
	var subordinate = document.getElementById("taskSubordinate");
	var processDefinitionId = document.getElementById("processDefinitionId").value;  
 	var params = 'taskIdFromJump='+taskId+"&processName="+processDefinitionId.split(":")[0];
	var organizerValues;
	var coordinatorsValues;
	if(organizers && coordinators){
		organizerValues = organizers.value;
		coordinatorsValues = coordinators.value;
		organizerOrdersValues = organizerOrders.value;
		if(organizerValues!=null && organizerValues!=""){
			if(canSave){
				var form = document.getElementById(formId);
				if(form){
					for(var instanceName in CKEDITOR.instances) {
					    CKEDITOR.instances[instanceName].updateElement();
					}
					document.getElementById("nextTaskOrganizers").value=organizerValues;
					var coOrdinators = document.getElementById("nextTaskCoordinators").value;
					if(coOrdinators){
						document.getElementById("nextTaskCoordinators").value=coOrdinators+","+coordinatorsValues;
					}else{
						document.getElementById("nextTaskCoordinators").value=coordinatorsValues;
					}
					document.getElementById("organizerOrders").value=organizerOrders.value;
					document.getElementById("organizerSuperior").value=superior.value;
	                document.getElementById("organizerSubordinate").value=subordinate.value;
			        var hiddenField1 = document.createElement("input");
			        hiddenField1.setAttribute("type", "hidden");
			        hiddenField1.setAttribute("name", 'activityId');
			        hiddenField1.setAttribute("value", activityId);
			        form.appendChild(hiddenField1);
			        
			        var hiddenField2 = document.createElement("input");
			        hiddenField2.setAttribute("type", "hidden");
			        hiddenField2.setAttribute("name", 'jumpType');
			        hiddenField2.setAttribute("value", 'forward');
			        form.appendChild(hiddenField2);
			
			        var hiddenField3 = document.createElement("input");
			        hiddenField3.setAttribute("type", "hidden");
			        hiddenField3.setAttribute("name", 'jumpNodeType');
			        hiddenField3.setAttribute("value", 'forwardNode');
			        form.appendChild(hiddenField3);
			        
					form.action="bpm/of/autoSaveAndReturn";
					_execute('task_target',params);
					appendCheckboxValues(formId);
					$("#"+formId).submit();
				}else{
					alert("No form element found");
				}
			}else {
				var params= "activityId="+activityId+"&taskIdFromJump="+taskIdFromJump+"&resourceName="+resourceName+"&jumpType=forward&nodeType=forwardNode&nextTaskOrganizers="
							+organizerValues+"&nextTaskCoordinators="+coordinatorsValues+"&nextTaskOrganizerOrders="+organizerOrdersValues;
				_execute("task_target",params);
				document.location.href = "#bpm/manageTasks/jumpToTask";
			}
			closeSelectDialog('involveUsersPopupDialog');
		}else{
			messageBoxShow(form.title.message,form.msg.organizerManadatory);
		}
	}else{
		alert("No organizer,co-ordinator element found!");
		return;
	}
} // end

function readNotice(insId){
	document.location.href = "#bpm/notice/updateNoticeReadStatus";
	_execute('target', 'noticeInstanceId='+insId);
	
}

function bulkTasksReplace(){
	var userId = $("#userId").val();
	if(userId!=null && userId!=""){
		var rowid =  gridObj.getGridParam('selarrrow');
		var taskJsonObj = [];
		rowid.forEach(function(item) {
			var lastOperationPerformed = gridObj.jqGrid('getCell', item, 'lastOperationPerformed');
			if (lastOperationPerformed != 'terminate'
					&& lastOperationPerformed != 'suspend') {
				taskJsonObj.push({
					taskId : gridObj.jqGrid('getCell', item, 'id'),
					resourceName : gridObj.jqGrid('getCell', item,
							'resourceName'),
					processDefinitionId : gridObj.jqGrid('getCell', item,
							'processDefinitionId'),
					executionId : gridObj.jqGrid('getCell', item,
					'executionId')
				});
			}
		});
		var jsonValues = JSON.stringify(taskJsonObj);	
		params = 'taskDetails='+jsonValues+'&userId='+userId;
		document.location.href = "#bpm/of/replaceTasks";
	    _execute('target',params);
		closeSelectDialog('involveUsersPopupDialog');
	}else{
		messageBoxShow(form.title.message,form.msg.addUserMandatory);
	}
}


function showAttachment(taskId){
	 document.location.href = "#bpm/of/showAttachments";
	var preTaskId = document.getElementById("previousTaskId").value;
	var isSameForm = document.getElementById("isSameForm").value;
	openSelectDialogForFixedPosition("popupDialog","","500","300","Show Attachments");
	var param = "taskId="+preTaskId;
	_execute('popupDialog', param);
	 
	  
}
function downloadAttachment(id){
   document.location.href = "bpm/of/downloadAttachment?id="+id;
}

function deleteAttachment(id){
	var row = document.getElementById(id);
    row.parentNode.removeChild(row);
    removeAttachmentIds.push(id);
    $("#removeAttachmentIds").val(removeAttachmentIds);
}

function viewAttachment(id){

		
		clearPreviousPopup();
		_execute('doc_view_dialog','id='+id);
	
		 
		 
		var url = "bpm/of/viewAttachment?id="+id;
		var win=window.open(url, '_blank');
		win.focus();
		    
}

function showAttachments(isSameTable,isStartForm,instanceId){
	var taskId = document.getElementById("taskId").value;
	var isCurrentFileAttachment = false;
	var isPreviousFileAttachment = false;
	$.ajax({
		url: 'bpm/of/showAttachments',
	    type: 'GET',
	    dataType: 'json',
	    data: 'taskId=' + taskId +'&isSameTable='+isSameTable+'&isStartForm='+isStartForm+'&instanceId='+instanceId,
		async: false,
		success : function(response) {			
			var attachments = "";
			var htmlContent = "<br/><table width='100%'><th colspan='4'>Previous Task Attachments</th>";
			var currentHtmlContent = "<br/><table width='100%' ><th colspan='4'>Current Task Attachments</th>";
			//console.log("response "+response);
			if(response!=null){
				for(var index = 0; index < response.length; index++){
					if(response[index].taskId != taskId){
						isPreviousFileAttachment = true;
						htmlContent = htmlContent + "<tr><td width='30%'>"+response[index].originalName+"</td><td width='20%'><a href='javascript:;' onclick='downloadAttachment("+response[index].id+")'>Download</a></td><td width='20%'><a href='javascript:;' onclick='viewAttachment("+response[index].id+")'>View</a></td><td width='20%'>IsAdd&nbsp;&nbsp;<input type='checkbox' id="+response[index].id+"  onclick='addAttachmentForTask("+response[index].id+")'/></td></tr>";
					}else{	
						isCurrentFileAttachment = true;
						currentHtmlContent = currentHtmlContent + "<tr id="+response[index].id+"><td width='30%'>"+response[index].originalName+"</td><td width='20%'><a href='javascript:;' onclick='downloadAttachment("+response[index].id+")'>Download</a></td><td width='15%'><a href='javascript:;' class='removeButton' onclick='deleteAttachment("+response[index].id+")'>Remove</a></td><td width='15%'><a href='javascript:;' onclick='viewAttachment("+response[index].id+")'>View</a></td><td width='20%'>IsAdd&nbsp;<input type='checkbox' id="+response[index].id+"  onclick='addAttachmentForTask("+response[index].id+")'/></td></tr>";
					}
				}
			}
			
			if(isCurrentFileAttachment){
				attachments = attachments + currentHtmlContent + "</table>";
			}
			if(isPreviousFileAttachment){
				attachments = attachments + htmlContent+"</table><br/>";
			}
			$("#attachmentDetail").html(attachments);
		}
	});
}
//submit add attachments
function addAttachments(){
	if(addAttachmentIds){
		$("#addAttachmentIds").val(addAttachmentIds);
	}
	if(removeAttachmentIds){
		$("#removeAttachmentIds").val(removeAttachmentIds);
	}
}

//Add previous task attachment to present task
function addAttachmentForTask(id){
	if($('#'+id).is(':checked')){
		addAttachmentIds.push(id);
		$("#addAttachmentIds").val(addAttachmentIds);
	}else{
		addAttachmentIds.splice( addAttachmentIds.indexOf(id), 1 );
	}
}

function showAttachmentsForStartNode(isSameTable,isStartForm,instanceId,isForRecall){
	var isCurrentFileAttachment = false;
	$.ajax({
		url: 'bpm/of/showAttachments',
	    type: 'GET',
	    dataType: 'json',
	    data: 'taskId=' + '' +'&isSameTable='+isSameTable+'&isStartForm='+isStartForm+'&instanceId='+instanceId,
		async: false,
		success : function(response) {			
			var attachments = "";
			var currentHtmlContent = "<table width='300px'><th align='left' colspan='4'>附件列表</th>";
				for ( var index = 0; index < response.length; index++) {
					isCurrentFileAttachment = true;
					currentHtmlContent = currentHtmlContent
							+ "<tr id="
							+ response[index].id
							+ "><td width='30%'>"
							+ response[index].originalName
							+ "</td><td width='20%'><a href='javascript:;' onclick='downloadAttachment("
							+ response[index].id
							+ ")'>下载</a></td><td width='15%'><a href='javascript:;' onclick='viewAttachment("
							+ response[index].id + ")'>查看</a></td>;"
							if(isForRecall != "yes") {
								currentHtmlContent = currentHtmlContent + "<td width='15%'><a href='javascript:;' class='removeButton' onclick='deleteAttachment("
								+ response[index].id + ")'>删除</a></td></tr>";
							}
									
				}
				if(isCurrentFileAttachment){
					attachments = attachments + currentHtmlContent + "</table>";
				}
				$("#attachmentDetail").html(attachments);
		}
	});
}

function recallStartEvent(formId){
	var form = document.getElementById(formId);
	var params = 'taskId='+form['activeTaskId'].value+"&resourceName="+form['resourceName'].value+"&returnType=recall";
	document.location.href = "#bpm/of/taskReturn";	        	
	_execute('target',params);
}

function add_more(id,element_name,divId){
	var tableId = element_name+"Table";
	var txt = "<table id='"+tableId+"'+'Table'> <tbody>	<tr id='clonablerow'><td > <input type=\"file\" name='"+element_name+"'> <a href='javascript:void(0);' onClick=clonefileUploadAddRow('"+tableId+"');>Add</a> &nbsp;&nbsp;<a href='javascript:void(0);' onClick=deleteFileUploadRow(this);>Remove</a></td>";
	document.getElementById(divId).innerHtml = txt;
	$("#"+divId).html(txt);
}
function deleteFileUploadRow(row,tableId){
	if(count>0){
		var sectionRowIndex = $($(row).parent('td').parent('tr')).index();		
		 $($(row).parent('td').parent('tr')).remove();
		 $("#"+tableId).find('tr').each(function(index){
			 if(sectionRowIndex <= index){				 
				 $(this).find("a,input").each(function(){
					 console.log($(this));
					 var topPosition = $(this).css("top");
					 $(this).css({"top":topPosition.split("px")[0]-30});
				 });
			 }
		 });		 
		count--;
	
    	 }else{
		$('#'+tableId+' td').each(function() {
			for ( var i = 0; i < $(this).children.length; i++) {
				var file = $(this).children(i).attr('type');
				if (file == "file") {
					$(this).children(i).val('');
				}
				break;
			}
		});
		messageBoxShow(form.title.message,form.msg.noPrevilage);
		return false;
	}
}

function clonefileUploadAddRow(id,elementId){
	var table   = document.getElementById( id );
    if ( table ) {
      var tbody   = table.getElementsByTagName('tbody')[ 0 ];
      var numRows = tbody.rows.length;
      var lastRow = tbody.rows[ numRows - 1 ];
      var newRow  = lastRow.cloneNode( true );
      var leftPosition;
      newRow.setAttribute( 'id', 'row' + numRows );
      $(newRow).find('input,file').each(
				function() {
					var top, left;
					var currentNameAttr = $(this).attr('name').replace(/\[]/g, ''); // get the
					var newNameAttr = currentNameAttr.replace(/([^\d]*)(\d*)([^\w]*)/,function(match, p1, p2, p3) {
								return p1 + (count) + p3;
							});
					 var topPosition =  $(this).css("top");
					 leftPosition =  $(this).css("left");
					 top = topPosition.split("px");
					 left = leftPosition.split("px");
					 $(this).css({"top":parseInt(top[0])+30});
					 $(this).attr('id', newNameAttr);
					if (newNameAttr.indexOf("id") !== -1) {
					  	$(this).attr('value', '');
					} else {
					}	  					
					$(newRow).find("a").each(function(){
						if($(this).text() == "Add"){
							$(this).css({"top":parseInt(top[0])+35, "left":parseInt(left[0])+300});
						}else{
							$(this).css({"top":parseInt(top[0])+35, "left":parseInt(left[0])+340});
						}
					});
				});
      if(leftPosition != "auto"){
    	  $(table).css({"margin-left":leftPosition});
      }     
      tbody.appendChild( newRow );
      var fileInputField = document.getElementById(elementId+count);
      fileInputField.value = ""; 
	}
    count++;
 }

function setAutomaticValuesForFormElements(formId,fieldName ,fieldValue,fieldType) {
	var type = new String(fieldType);
	if(type.indexOf("Append") == 0 && $("input[name='" + fieldName + "']").val()){
		if($("input[name='" + fieldName + "_FullName']").val()){
			$("input[name='" + fieldName + "_FullName']").val($("input[name='" + fieldName + "_FullName']").val()+" "+fieldValue);
		}else{
			$("input[name='" + fieldName + "']").val($("input[name='" + fieldName + "']").val()+" "+fieldValue);
		}
		
	}else if(type.indexOf("Append") == 0 && $("textarea[name='"+fieldName+"']").val()){
		$("textarea[name='"+fieldName+"']").val($("textarea[name='" + fieldName + "']").val()+" "+fieldValue) 
	}else{
			if($("select[name='" + fieldName + "']").val()){
				var element_id = $("select[name='" + fieldName + "']").attr("id");
				var select = document.getElementById(element_id);
				for (var i  = 0; i < select.options.length; i++) {
					if (select.options[i].value === fieldValue ) {
						select.selectedIndex = i;
					}else{
						 $('#'+element_id).prepend("<option value='"+fieldValue+"' selected='selected'>"+fieldValue+"</option>");
						 break;
					}
				}
				
				$("select[name='" + fieldName + "']").val(fieldValue);
			}else if($("input[name='" + fieldName + "']").attr("type") == "checkbox"){
				  var index = 0;
				  var element_id = $("input[name='" + fieldName + "']").attr("id");
				  var addCheckBox = "";
				  $('input[type="checkbox"]').each(function() {	
				  if(fieldValue.contains(",")){
					  var fieldValueArray = fieldValue.split(",");
					  for(var i=0;i<fieldValueArray.length;i++){
					  	if($(this).val() === fieldValueArray[i]){
					  		$(this).attr('checked', true);
					  	}else{
					  		if(index < fieldValueArray.length){
					  			//addCheckBox += "<input type='checkbox' value='"+fieldValueArray[i]+"' checked='true'/>"+fieldValueArray[i]+"";
					  		}
					  	}
					  	index += 1;
					  }
				  	
				  	}else{
				  		if(fieldValue != null && fieldValue != "" && fieldValue.length>0 ){
				  		addCheckBox += "<input type='checkbox' value='"+fieldValue+"' checked='true'/>"+fieldValue+"";
				  		}
				  	}
				  	
				  });	
				  $('#'+element_id).parent().append(addCheckBox);	
			} else if($("input[name='" + fieldName + "']").attr("type") == "radio"){
				var isMatchedValue = false;
				 var element_id = $("input[name='" + fieldName + "']").attr("id");
				$('input[type="radio"]').each(function() {	
					if($(this).val() === fieldValue){
						$(this).attr('checked', true);
						isMatchedValue = true;
					}
				});
				if(isMatchedValue == false){
					$('#'+element_id).parent().append("<input type='radio' value='"+fieldValue+"' checked='true'/>"+fieldValue+"");
				}
			} else{
				$("input[name='" + fieldName + "']").val(fieldValue);
			}
		}
		
	
}

//for urge
function urgePopUp(processName,processDefinitionId,taskId,processInsId){
	var url = window.location.href.split('?')[0]
	var requestedparams = "processDefinitionId="+processDefinitionId+"&taskId="+taskId+"&processInsId="+processInsId+"&url="+url;
	var container = "processPreviewPopupDialog";
	clearTreePopupDiv();
	setTimeout(function(){
		document.location.href = "#bpm/of/showUrge";
		_execute(container, requestedparams);
	},200);
	$.myDialog = $("#" + container);
	$.myDialog.dialog({
		width : 'auto',
		height : 'auto',
		modal : true,
		title : "催办 - " + processName
	});
	$.myDialog.dialog("open");
}

function sendUrgeNotification(){
	 $.ajax({
        type: "POST",
        url: "/bpm/of/sendUrgeNotification",
        data: $("#urgeForm").serialize(), // serializes the form's elements.
        success: function(response)
        {
       	 if(response.success){
       	     	document.getElementById('task_target').style.visibility = 'hidden'; 
					$.msgBox({
						title: form.title.success,
						content: response.success,
					    type: "success"					    
					});
					closeSelectDialog('processPreviewPopupDialog');
/*					if(url){
						var params = "";
						document.location.href = "#bpm/manageTasks/mybucket";
						_execute('task_target',params);
					}*/
				}else{					
			    	validateMessageBox(form.title.error, response.failure, "error");
				}
        }
      });

	 return false;
}
