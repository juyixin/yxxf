<!doctype html>
<%@ include file="/common/taglibs.jsp"%>

<table class="tree-selector-table treeBoxShadow" style="background-image: url('/styles/easybpm/images/palette_line.jpg');vertical-align:top;">
	<tr>
		<td style="vertical-align:top;">
			<div id="selector_left_process" class="tree-left" style="background: none !important;"></div>
		</td>
		<td style="vertical-align:top;">
			 <div id="selector_right_process" class="tree-right mar-L0">
			</div>
		</td>
	</tr>
</table>

<script>

$(window).resize(function() {
    $("#popupDialog").dialog("option", "position", "center");
});

$(document).ready(function(){
	$("#popupDialog").dialog("option", "position", "center");
	var selectedValues = '${selectedValue}';
	var values = selectedValues.split(",");
	var menuData = ""; 
	menuData = "<div class='padding5 overflow height-per-76' id='processList'>";
		<c:forEach items="${availableProcess}" var="process">
			if("${selectType}" == 'single'){
	       		menuData += "<input type='radio'  name='processDetail' id='${process.id}' value='${process.name}'><span class='pad-L5'>${process.name}</span></input><br>";
			}else{
				if(contains(values, "${process.id}" )){
					menuData += "<input type='checkbox'  checked='checked' name='processDetail' id='${process.id}' value='${process.name}'><span class='pad-L5'>${process.name}</span></input><br>";
				}else{
					menuData += "<input type='checkbox'  name='processDetail' id='${process.id}' value='${process.name}'><span class='pad-L5'>${process.name}</span></input><br>";
				}
			}
	</c:forEach> 
	menuData += '</div>';
	menuData += '<div class="popup-button id="saveButtonDiv">'+
	 '<button type="submit" class="btn btn-primary normalButton padding0 line-height0" name="save" id="saveButton" onclick="saveTreeValue();">'+
	 'Select' +
	 '</button></div>';
	 document.getElementById("selector_right_process").innerHTML = menuData;
    $("#popupDialog").dialog("option", "position", "center");
});
//Form the left tree
constructJsTree("selector_left_process",'${processTree}',"processRightSideValue",false);

//Form the right tree
function processRightSideValue(e,data){
	
}
var selectedVal="";
var selectedArray=new Array();

// store the selected value
function loadProcess(){
	if("${selectType}" == 'single'){
		selected = $("#processList input[type='radio'][name='processDetail']:checked");
		if (selected.length > 0){
			//document.getElementById(requestorId).value = selected.val();
			selectedArray.push(selected.val());
		}
	}else{
		$("#processList input[type='checkbox'][name='processDetail']:checked").each(function(){
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
	loadProcess();
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