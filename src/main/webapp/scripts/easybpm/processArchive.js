function changeWizardByTab(){
		$("#wizardTabs a").each(function(){
			$(this).click(function(){
				var tabId = $(this).attr('id');
				tabId = tabId.split('-');
				var tabContent = document.getElementById('wizardTab-content-' + tabId[1]);
				$(tabContent).addClass('displayBlock');
				$(tabContent).removeClass('displayNone');
			    $(this).parent().addClass('active-step');
			    $(this).parent().siblings().removeClass('active-step');
			    $(this).parent().siblings().addClass('completed-step');
			    $(tabContent).siblings().addClass('displayNone');
			    $(tabContent).siblings().removeClass('displayBlock');
			    userFormButtonsControl(tabId[1]);
			});
		});
   }

 function userFormButtonsControl(count){
	   	   if(count > 1){
			   $('#previousButton').removeAttr("disabled", "disabled");
		   }else{
			   $('#previousButton').attr("disabled", "disabled");
		   }
		   if(count > 4){
		
			   $('#nextButton').attr("disabled", "disabled");
			   /*$('#saveButton').removeAttr('disabled');*/
		   }else{
			   $('#nextButton').removeAttr("disabled", "disabled");
			   /*$('#saveButton').attr("disabled", "disabled");*/
		   }
	}
	
/*function setDefaultValue(formId){
	
} */

function setSubmittedValues(formId,map){
	var elements = document.getElementById(formId).elements;
	for (var i=0;i<elements.length;i++){
		var key = elements[i].getAttribute('column');
		if(map[key]){
			if(elements[i].type == 'radio' || elements[i].type == 'checkbox'){
				var checkedValues=(map[key]).split(',');
				if(checkedValues.indexOf(elements[i].value) != -1) {
					elements[i].checked = true;
				}
			}else if(elements[i].type == 'select' || elements[i].type == 'select-one'){
					/*if(elements[i].getAttribute('optiontype') == 'dynamic'){
						var dropDownElement = document.getElementById(elements[i].id);
						var option = document.createElement("OPTION"); 
						option.text=map[key];  
						option.value=map[key];   
						dropDownElement.add(option);
					}else {
						elements[i].value = map[key];
					}*/
				elements[i].value = map[key];
			}else if(elements[i].type == 'file'){
			}else {
				elements[i].value = map[key];
			}
		}
	}
	var imageTags = document.getElementById(formId).getElementsByClassName("richtextbox");
	for (var i=0;i<imageTags.length;i++){
		loadCKEditor(imageTags[i],formId,map[imageTags[i].getAttribute('column')]);
	}
}

function replaceAllValueAsArchiveReadOnly(formId){
	$.each($('#'+formId).serializeArray(), function(i, field) {
		var form = document.getElementById(formId);
		var element = form[field.name];
		if(field.value!=""){
			$( element ).replaceWith("<span style='font-size: 14px;padding: 4px 6px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;height: 30px;align:left;'><b>&nbsp;&nbsp;&nbsp;"+field.value+"</b></span>" );
		}else{
			$( element ).replaceWith("<span style='font-size: 14px;padding: 4px 6px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;height: 30px;align:left;'><b>&nbsp;&nbsp;&nbsp;</b></span>" );
		}
	});
	
	$("#"+formId).find("input").each(function(index){
		if($(this).attr('type') == 'file'){
			$(this).replaceWith( "<span style='font-size: 14px;padding: 4px 6px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;height: 30px;align:left;'><b>&nbsp;&nbsp;&nbsp;</b></span>" );
		}else if($(this).attr('type') == 'radio' || $(this).attr('type') == 'checkbox'){
			if($(this).is(':checked')){
				$(this).replaceWith("<span style='font-size: 14px;padding: 4px 6px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;height: 30px;align:left;'><b>&nbsp;&nbsp;&nbsp;"+$(this).val()+"</b></span>" );
			}else{
				$(this).replaceWith("<span style='font-size: 14px;padding: 4px 6px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;height: 30px;align:left;'><b>&nbsp;&nbsp;&nbsp;</b></span>" );
			}
		}
	});
	
	$(".ui-datepicker-trigger").hide();
	$(".requiredImg").hide();
	$(".ui-datepicker-trigger").hide();
}

function loadCKEditor(element,formId,value) {
	var ckEditorDiv = document.createElement("div");
	ckEditorDiv.setAttribute('id',element.id);
	ckEditorDiv.setAttribute('class','fontSize13');
	ckEditorDiv.setAttribute('style','border:1px solid #CCCCCC;');
	if(value){
		ckEditorDiv.innerHTML = value;
	}
	element.parentNode.removeChild(element);
	document.getElementById(formId).appendChild(ckEditorDiv);	
}

function loadValuesForTab(id){
	if(id == "formValuesDiv"){
		document.getElementById("formPageDetail").className="displayBlock fontSize13";
		document.getElementById("formValuesDiv").className="active-step";
		if(document.getElementById("WorkFlowTrace") != null){
			document.getElementById("WorkFlowTraceDetail").className="displayNone";
			document.getElementById("WorkFlowTrace").className="completed-step";
		}
		if(document.getElementById("inputOpinion") != null){
			document.getElementById("inputOpinionDetail").className="displayNone";
			document.getElementById("inputOpinion").className="completed-step";
		}
	}else if(id == "inputOpinion"){
		document.getElementById("inputOpinionDetail").className="displayBlock fontSize13";
		document.getElementById("formPageDetail").className="displayNone";
		document.getElementById("WorkFlowTraceDetail").className="displayNone";
		document.getElementById("inputOpinion").className="active-step";
		document.getElementById("formValuesDiv").className="completed-step";
		document.getElementById("WorkFlowTrace").className="completed-step";
	}else if(id == "WorkFlowTrace"){
		document.getElementById("WorkFlowTraceDetail").className="displayBlock fontSize13";
		document.getElementById("formPageDetail").className="displayNone";
		document.getElementById("WorkFlowTrace").className="active-step";
		document.getElementById("formValuesDiv").className="completed-step";
		if(document.getElementById("inputOpinion") != null){
			document.getElementById("inputOpinionDetail").className="displayNone";
			document.getElementById("inputOpinion").className="completed-step";
		}
	}
}

function loadWorkFlowTraceGrid(traceValues,fieldAuditValues){
	for(var i=0 ; i< traceValues.length ; i++){
	 	addRow("workflowTraceTable",traceValues[i],i+1);
	}
	for(var i=0 ; i< fieldAuditValues.length ; i++){
		addRowForFormFiledAudit("formFieldTraceTable",fieldAuditValues[i],i+1);
	}
}

function addRow(tableID,values,no) {
    var table = document.getElementById(tableID);

    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);

    var cell1 = row.insertCell(0);
	cell1.innerHTML = no;

    var cell2 = row.insertCell(1);
    cell2.innerHTML = values.name;

    var cell3 = row.insertCell(2);
  	cell3.innerHTML = values.createdby;
  	
  	var cell4 = row.insertCell(3);
  	cell4.innerHTML = values.createdtime;
  	
    var cell5 = row.insertCell(4);
  	cell5.innerHTML =  values.operationType;  	
}

function changeWizardByTabForDynamicForm(){
	var tabCount = 0;
	$("#wizardTabs a").each(function(){
		tabCount = tabCount + 1;
		$(this).click(function(){
			var tabId = $(this).attr('id');
			tabId = tabId.split('-');
			for(var idx = 1; idx <= tabCount; idx++){
				$("#wizardTab-content-" + idx).css('display', 'none');
				$("#wizardTabSpan-" + idx).removeClass('active-step').addClass('completed-step');
			}
			$("#wizardTab-content-" + tabId[1]).css('display', '');
			$("#wizardTabSpan-" + tabId[1]).removeClass('completed-step').addClass('active-step');
		    userFormButtonsControl(tabId[1]);
		});
	});
}

function loadFirstTabAsDefault(){
	$(".active-step").removeClass('active-step').addClass("completed-step");
	$("#wizardTabSpan-1").removeClass('completed-step').addClass("active-step");	
	var tabCount = 0;
	$("#wizardTabs a").each(function(){tabCount = tabCount + 1;});	
	for(var idx = 1; idx <= tabCount; idx++){
		$("#wizardTab-content-" + idx).css('display', 'none');
	}	
	$("#wizardTab-content-1").css('display', '');
	$("#formValuesDiv").addClass("active-step");
}

function addOptionValToDictionary(element_id,data) {
	$( "#"+element_id ).empty();
	if(data){
            $.each(data, function(index, item) {
                $("#"+element_id).get(0).options[$("#"+element_id).get(0).options.length] = new Option(item.dictValue, item.dictId);
            });
            $("#"+element_id).prepend("<option value='' selected='selected'></option>");
	}
}

function autoCompleteForDataDictionary(element_id,data) {
	var dataList = [];
	$.each(data, function(index, item) {
		dataList.push(item.dictId);
	});
	
	//Accessing list of values to auto complete
	$('#'+element_id).autocomplete({
	    minLength: 1,
	    source: dataList,
	});
	
	//Searching by only start letters
	var escapeRegexp = $.ui.autocomplete.escapeRegex;
	$.extend( $.ui.autocomplete, {
	    escapeRegex: function( pressedLetter ) {
	        return '^' + escapeRegexp(pressedLetter);
	    }
	});
}

function radioButtonForDataDictionary(element_id,data) {
	if(data){
		$.each(data, function(index, item) {
            		$( '<input type="radio" '+
            		   'table="'+$( "#"+element_id ).attr('table')+'" '+
  	            	   'optiontype="'+$( "#"+element_id ).attr('optiontype')+'" '+
  	            	   'name="'+$( "#"+element_id ).attr('name')+'" '+
  	            	   'id="'+$( "#"+element_id ).attr('id')+'_'+item.dictId+'" '+ 
  	            	   'datadictionarylabel="'+$( "#"+element_id ).attr('datadictionarylabel')+'" '+
  	            	   'datadictionary="'+$( "#"+element_id ).attr('datadictionary')+'" '+
  	            	   'column="'+$( "#"+element_id ).attr('column')+'" '+
  	            	   'class="data_dictionary_radio" '+
  	            	   'value="'+item.dictId+'"> '+item.dictValue+' </input>' ).insertAfter( "#"+element_id );
		});
		$( "#"+element_id ).remove();
	}
}

function checkboxForDataDictionary(element_id,data) {
	if(data){
		$.each(data, function(index, item) {
			$( '<input type="checkbox" '+
			   'table="'+$( "#"+element_id ).attr('table')+'" '+
			   'optiontype="'+$( "#"+element_id ).attr('optiontype')+'" '+
			   'name="'+$( "#"+element_id ).attr('name')+'" '+
			   'id="'+$( "#"+element_id ).attr('id')+'_'+item.dictId+'" '+ 
			   'datadictionarylabel="'+$( "#"+element_id ).attr('datadictionarylabel')+'" '+
			   'datadictionary="'+$( "#"+element_id ).attr('datadictionary')+'" '+
			   'column="'+$( "#"+element_id ).attr('column')+'" '+
			   'class="data_dictionary_checkbox" '+
			   'value="'+item.dictId+'"> '+item.dictValue+' </input>' ).insertAfter( "#"+element_id );
		});
		$( "#"+element_id ).remove();
	}
}

function loadDataDictionaryValues(listBoxData, autoCompleteData, radioButtonData, checkBoxData){
	$( ".data_dictionary" ).each(function( index ) {
		var element_id = this.getAttribute("id");
		addOptionValToDictionary(element_id,listBoxData[element_id]);
	  
	});
	
	$( ".autocomplete" ).each(function( index ) {
		var element_id = this.getAttribute("id");
		autoCompleteForDataDictionary(element_id,autoCompleteData[element_id]);
	  
	});
	
	$( ".data_dictionary_radio" ).each(function( index ) {
		var element_id = this.getAttribute("id");
		radioButtonForDataDictionary(element_id,radioButtonData[element_id]);
	  
	});
	
	$( ".data_dictionary_checkbox" ).each(function( index ) {	
		var element_id = this.getAttribute("id");
		checkboxForDataDictionary(element_id,checkBoxData[element_id]);
	  
	});
}

function addRowForFormFiledAudit(tableID,values,no) {
    var table = document.getElementById(tableID);
	if(no == 1){
		document.getElementById("formFieldTraceTableDiv").innerHTML = "<div style='line-height: 18px; font-weight: bold;'>Form Field Audit :</div>";
		var header=table.createTHead();
		var row=header.insertRow(0);
		var cell=row.insertCell(0);
		cell.innerHTML="<b>&nbsp;No&nbsp;</b>";
		
		var cell=row.insertCell(1);
		cell.innerHTML="<b>&nbsp;Modified Time&nbsp;</b>";
		
		var cell=row.insertCell(2);
		cell.innerHTML="<b>&nbsp;Modified By&nbsp;</b>";
		
		var cell=row.insertCell(3);
		cell.innerHTML="<b>&nbsp;Field Name&nbsp;</b>";
		
		var cell=row.insertCell(4);
		cell.innerHTML="<b>&nbsp;Chinese Name of the Field&nbsp;</b>";
		
		var cell=row.insertCell(5);
		cell.innerHTML="<b>&nbsp;Original Value&nbsp;</b>";
		
		var cell=row.insertCell(6);
		cell.innerHTML="<b>&nbsp; New Value&nbsp;</b>";
	}
    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);

    var cell1 = row.insertCell(0);
	cell1.innerHTML = "&nbsp;"+no+"&nbsp;";

    var cell2 = row.insertCell(1);
    cell2.innerHTML = "&nbsp;"+values.modifiedTime+"&nbsp;";
	
    var cell3 = row.insertCell(2);
  	cell3.innerHTML = "&nbsp;"+values.modifiedBy+"&nbsp;";
	
  	var cell4 = row.insertCell(3);
  	cell4.innerHTML = "&nbsp;"+values.fieldName+"&nbsp;";
	
    var cell5 = row.insertCell(4);
  	cell5.innerHTML =  "&nbsp;"+values.chineseName+"&nbsp;";  	
	
  	var cell6 = row.insertCell(5);
  	cell6.innerHTML = "&nbsp;"+values.oldValue+"&nbsp;";
  	
    var cell7 = row.insertCell(6);
  	cell7.innerHTML =  "&nbsp;"+values.newValue+"&nbsp;";
}