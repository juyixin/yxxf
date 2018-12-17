<!doctype html>
<%@ include file="/common/taglibs.jsp"%>

<table class="tree-selector-table treeBoxShadow" style="background-image: url('/styles/easybpm/images/palette_line.jpg');vertical-align:top;">
	<tr>
		<td style="vertical-align:top;">
			<div id="selector_left_form" class="tree-left" style="background: none !important;"></div>
		</td>
		<td style="vertical-align:top;">
			 <div id="selector_right_form" class="tree-right mar-L0">
			</div>
		</td>
	</tr>
</table>

<script>

$(window).resize(function() {
    $("#popupDialog").dialog("option", "position", "center");
});

$(document).ready(function(){
	var selectedValues = '${selectedValue}';
	var values = selectedValues.split(",");
	var menuData = ""; 
	menuData = "<div class='padding5 overflow height-per-76' id='formList'>";
	<c:forEach items="${availableForm}" var="formObj">
	if("${selectType}" == 'single'){
   		menuData += "<input type='radio'  name='formDetail' id='${formObj.id}' value='${formObj.formName}'><span class='pad-L5'>${formObj.formName}</span></input><br>";
	}else{
		if(contains(values, "${formObj.id}" )){
			menuData += "<input type='checkbox'  checked='checked' name='formDetail' id='${formObj.id}' value='${formObj.formName}'><span class='pad-L5'>${formObj.formName}</span></input><br>";
		}else{
			menuData += "<input type='checkbox'  name='formDetail' id='${formObj.id}' value='${formObj.formName}'><span class='pad-L5'>${formObj.formName}</span></input><br>";
		}
	}
	</c:forEach> 
	menuData += '</div>';
	menuData += '<div class="popup-button id="saveButtonDiv">'+
	 '<button type="submit" class="btn btn-primary normalButton padding0 line-height0" name="save" id="saveButton" onclick="saveTreeValue();">'+
	 'Select' +
	 '</button></div>';
	 document.getElementById("selector_right_form").innerHTML = menuData;
    $("#popupDialog").dialog("option", "position", "center");
});
//Form the left tree
constructJsTree("selector_left_form",'${formTree}',"formRightSideValue",false);

//Form the right tree
function formRightSideValue(e,data){
	document.getElementById("selector_right_form").innerHTML = "";
		var formId = data.rslt.obj.data("id");
		var name = data.rslt.obj.data("name");
		var menuData = ""; 
		menuData = "<div class='padding5 overflow height-per-76' id='formList'>";
		if(formId=='all'){
			<c:forEach items="${availableForm}" var="formObj">
				if("${selectType}" == 'single'){
		       		menuData += "<input type='radio'  name='formDetail' id='${formObj.id}' value='${formObj.formName}'><span class='pad-L5'>${formObj.formName}</span></input><br>";
				}else{
					menuData += "<input type='checkbox'  name='formDetail' id='${formObj.id}' value='${formObj.formName}'><span class='pad-L5'>${formObj.formName}</span></input><br>";
				}
		</c:forEach> 
		}else{	
	
			if("${selectType}" == 'single'){
	       		menuData += "<input type='radio'  name='formDetail' id='"+formId+"' value='"+name+"'><span class='pad-L5'>"+name+"</span></input><br>";
			}else{
				menuData += "<input type='checkbox'  name='formDetail' id='"+formId+"' value='"+name+"'><span class='pad-L5'>"+name+"</span></input><br>";
			}
		}
		menuData += '</div>';
			menuData += '<div class="popup-button id="saveButtonDiv">'+
			 '<button type="submit" class="btn btn-primary normalButton padding0 line-height0" name="save" id="saveButton" onclick="saveTreeValue();">'+
        	 'Select' +
    		 '</button></div>';
    		 document.getElementById("selector_right_form").innerHTML = menuData;
}
var selectedVal="";
var selectedArray=new Array();

// store the selected value
function loadForm(){
	if("${selectType}" == 'single'){
		selected = $("#formList input[type='radio'][name='formDetail']:checked");
		if (selected.length > 0){
			//document.getElementById(requestorId).value = selected.val();
			selectedArray.push(selected.val());
		}
	}else{
		$("#formList input[type='checkbox'][name='formDetail']:checked").each(function(){
			var $this = $(this);      
			var id=$this.attr('id');
			var value = $this.attr('value');
			selectedVal += '<option value='+id+' selected>'+value+'</option>';
			selectedArray.push(value);
		});
	}
}

//Insert the selected value to combo
function saveTreeValue(){
	loadForm();
	var requestorId = "${requestorId}";
	if("${selectType}" == 'single'){
		for(var i=0; i<selectedArray.length;i++){
			selectedVal=selectedArray[i];
		}
		document.getElementById(requestorId).value = selectedVal;
		selectedVal="";
		selectedArray=[];
	}else{
		var selectArray = [];
		$.each(selectedArray, function(i, el){
		    if($.inArray(el, selectArray) === -1) selectArray.push(el);
		});
		for(var i=0;i<selectArray.length;i++){
			//selectedVal += '<option value='+selectArray[i]+' selected>'+selectArray[i]+'</option>';
		}
		selectArray=[];
		document.getElementById(requestorId).innerHTML = selectedVal;
	}
	
	selectedVal="";
	selectedArray=[];
	closeSelectDialog("popupDialog");
	
}
</script>