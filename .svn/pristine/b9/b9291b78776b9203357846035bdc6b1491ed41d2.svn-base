<%@ include file="/common/taglibs.jsp"%>
<style>
<!--
tr.row-height{
    height:5px;
}
-->
</style>
<div class="row-fluid">
	<table width="100%" id="buttonEvent"> 
		<tr style="height:15px;"></tr> 
                <tr>
  	             	<td class="pad-L10">
                		 <label class="control-label style3 style18 " style="float: right;
    text-align: right;line-height:10px;" ><fmt:message key="buttonEvent.operationType"/> &nbsp;&nbsp;&nbsp; </label>
                	</td>
                	<td>
                		<div class="controls">
                			<select id="operationType" onChange="getSelectedEvent(this);">
                				<option value="" selected="selected"></option>
						<option value="allSystemProcess"><fmt:message key="buttonEvent.allSystemProcess"/></option>
						<option value="allProcess"><fmt:message key="buttonEvent.allProcess"/></option>
					</select>
                		</div>
                	</td>
                	<td class="pad-L10">
                		 <label class="control-label style3 style18 " style="float: right;
    text-align: right;line-height:10px;" ><fmt:message key="buttonEvent.object"/> &nbsp;&nbsp;&nbsp; </label>
                	</td>
                	<td>
                		<div class="controls">
                			<select id="object" onChange="createButtonEvent();">
					</select>
                		</div>
                	</td>
                	<tr>
                	<td class="pad-L10">
                		 <label class="control-label style3 style18 " style="float: right;
    text-align: right;line-height:10px;" ><fmt:message key="buttonEvent.script"/> &nbsp;&nbsp;&nbsp; </label>
                	</td>
                	<td>
                		<div class="controls">
                			<textarea type="text" class="span12" style="width:215%" id="buttonScriptFunction"
									name="buttonScriptFunction" /></textarea>
                		</div>
                	</td>
                	</tr>
		</tr>
		<tr style="height:20px;"></tr>
		<tr>
			<td colspan="4" align="center">
				<div class="form-actions no-margin">
					<button type="button"		onclick="saveScriptFunction();"	class="btn btn-primary">
						<fmt:message key="button.save" />
					</button>
					<button type="button" onClick="closeSelectDialog('popupDialog');"	class="btn btn-primary">
						<fmt:message key="button.cancel" />
					</button>
				</div>
			</td>
		</tr>
	</table>
</div>

<script type="text/javascript">
var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
var moduleId = $("#moduleId").val()?$("#moduleId").val():selected_nodes.attr("moduleid");
var listViewId = $("#id").val()?$("#id").val():selected_nodes.attr("nodeid");
$(function() {
	var objectValue = "";
	var isReinitialize = false;
	var buttonScriptValue = $("#buttonScript").val();
	if(buttonScriptValue != undefined && buttonScriptValue.length > 1){
	isReinitialize = true;
		if(buttonScriptValue.contains("`")){
			var scriptValues = buttonScriptValue.split("`");
			if(scriptValues.length < 3){
				$("#buttonScriptFunction").val(buttonScriptValue);
				$("#operationType").val(scriptValues[1]);
			} else if(scriptValues.length <2) {
				$("#buttonScriptFunction").val(buttonScriptValue);
			} else {
				$("#buttonScriptFunction").val(buttonScriptValue);
				$("#operationType").val(scriptValues[1]);
				objectValue = scriptValues[3];
			}
		} else {
			$("#buttonScriptFunction").val(buttonScriptValue);
		}
	}
	if($("#operationType").val() != '' && $("#operationType").val().length > 1){
		getSelectedEvent($("#operationType").val(),isReinitialize);
	}
	if(objectValue != "" && objectValue.length > 1){
		$("#object option").each(function(){
			if($(this).val().split(":")[0] === objectValue){
				$(this).attr('selected', isReinitialize);
			}
		});
	}
});

/**
 * create button event function at this place
 *
 * @return 
 */
function createButtonEvent(){
	var buttonScriptValue = $("#buttonScriptFunction").val();
	var operationTypeValue = $("#operationType").val();
	var objectValue = $("#object").val();
	var objectText = $("#object option[value='"+objectValue+"']").text();
	var buttonScript = "";
	if(operationTypeValue != undefined && objectValue != undefined && operationTypeValue.length > 0 && operationTypeValue != "" && objectValue != "" && objectValue.length > 0){
		buttonScript += 'var operationType ="`'+operationTypeValue+'`";\n var objectValue = "`'+objectText+'`";\n';
		if(operationTypeValue == "allProcess"){
			buttonScript += "startProcessWithOrganizer('"+objectValue+"','true','false',null,false);";
		} else {
			//buttonScript += "startProcessWithOrganizer('"+objectValue+"','true','false',null,false);";
			//buttonScript += "var params ='formId="+objectValue+"&moduleId='; document.location.href = '#bpm/form/showFormDesignerForEdit'; _execute('target',params);";
			buttonScript += "document.location.href = '#bpm/defaultProcess/"+objectText+"'; _execute('target','');";
		}
	}
	var randomNumber = Math.floor((Math.random()*1000000)+1);
	var buttonEventFuntion = "function buttonEvent"+randomNumber+"(){\n\n"+buttonScript+"}";
	$("#buttonScriptFunction").val(buttonEventFuntion)
}

/**
 * get selected operation and get all related object values
 *
 * @param operationObject
 * @return
 */
function getSelectedEvent(operationObj,isReinitialize){
	var operationValue = "";
	if(operationObj.value != undefined){
		operationValue = operationObj.value;
	} else {
		operationValue = operationObj;
	}
	if(operationValue != '' && operationValue.length > 1){
		/*if(operationValue == "formFromModule"){
			getAllFormsByModuleId(moduleId);
		} else*/
		if(operationValue == "allSystemProcess"){
			getAllActiveSystemProcess(isReinitialize);
		} else if(operationValue == "allProcess"){
			getAllActiveProcess(isReinitialize);
		}
	}  else {
		$("#object" ).empty();
	}
}

/**
 * load all form with in the current module
 *
 * @param moduleId
 * @return 
 */
function getAllFormsByModuleId(moduleId){
	$.ajax({
		url:'/form/getAllFormsByModuleId?moduleId='+moduleId+'',
		type:'GET',
		dataType: 'json',
		async: false,
		success: function(data) {
			var createOption = "";
			$("#object" ).empty();
			$.each(data, function(index, item) {
				//generate the option tag
				createOption = createOption + "<option value='"+item.label+"'>"+item.value+"</option>";
			});
           		$("#object").prepend(createOption);
           		createButtonEvent();
        	}
        });
}

/**
 * load all forms
 *
 * @return 
 */
function getAllForms(){
	$.ajax({
		url:'/form/getAllForms',
		type:'GET',
		async: false,
		success: function(response) {
		console.log(response);
			var fieldData = response.forms;
			var id='object';
			$("#"+id ).empty();
			for(var i=0 ;i<fieldData.length;i++){
				//generate option tags
				$("#"+id).get(0).options[$("#"+id).get(0).options.length] = new Option(fieldData[i].formName,fieldData[i].id);
			}
			createButtonEvent();
	       	}
        });
	
}

function getAllActiveSystemProcess(isReinitialize){
	$.ajax({
		url:'bpm/admin/aetAllProcessBySystemDefiend?isSystemDefined=1',
		type:'GET',
		async: false,
		success: function(data) {
		var createOption = "";
			$("#object" ).empty();
			$.each(data, function(index, item) {
				//Generate option tags
				if(item.label != "Default Process") {
					createOption = createOption + "<option value='"+item.value+"'>"+item.label+"</option>";
				}
           	});
           		
           		$("#object").prepend(createOption);
           		if(isReinitialize == undefined || isReinitialize == false){
           			createButtonEvent();
           		}
	       	}
        });
}

/**
 * function to save the script to button event
 *
 * @return 
 */
function saveScriptFunction(){
	$("#buttonScript").val($("#buttonScriptFunction").val());
	closeSelectDialog('popupDialog');
}

/**
 * to load all process 
 *
 * @return 
 */
function getAllActiveProcess(isReinitialize){
	$.ajax({
		url: 'bpm/admin/aetAllProcessBySystemDefiend?isSystemDefined=0',
	    type: 'GET',
	    dataType: 'json',
		async: false,
		success : function(data) {
			var createOption = "";
			$("#object" ).empty();
			$.each(data, function(index, item) {
				//Generate option tags
				createOption = createOption + "<option value='"+item.value+"'>"+item.label+"</option>";
           		 });
           		
           		$("#object").prepend(createOption);
           		if(isReinitialize == undefined  || isReinitialize==false){
           			createButtonEvent();
           		}
		}
	});
}
</script>
