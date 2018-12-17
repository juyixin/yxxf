/**
 * @author Mahesh
 * 
 */
var formUserLogin="";
var formImage="";
var userNameHiddenIds=[];
var formInputIds=[];
var removeFormImage=false;
var formImageHeight="";
function getTabelsByParentTable(element_id){
	$.ajax({
		url: 'bpm/table/allTable',
		type: 'GET',
	    dataType: 'json',
		async: false,
		success: function(data) {
			$( element_id ).empty();
			if(data){
        		 $.each(data, function(index, item) {
                 	$(element_id).get(0).options[$(element_id).get(0).options.length] = new Option(item.tableName, item.id);
                 });
                 $(element_id).prepend("<option value='' selected='selected'></option>");
        	}
        }
    });	
}

function getTableWithParentTable(element_id, tableId, tableName){
	$.ajax({
		url: 'bpm/table/getTableWithParentTable?tableId='+tableId,
	    type: 'GET',
	    dataType: 'json',
		async: false,
		success: function(data) {
			$( element_id ).empty();
			if(data){
	            $.each(data, function(index, item) {
	                $(element_id).get(0).options[$(element_id).get(0).options.length] = new Option(item.tableName, item.id);
	            });
	            $(element_id).append("<option value='"+tableId+"'>"+tableName+"</option>");
	            $(element_id).prepend("<option value='' selected='true'></option>");
	           // $(element_id).val(tableId);
			}
        }
    });	
}

function getAllColumnByTableId(element_id, tableId, fieldType){
	 $.ajax({
         type: 'GET',
         url: '/bpm/table/getAllColumnByTableId?tableId='+tableId+'&fieldType='+fieldType,
         dataType: 'json',
         async: false,
         success: function(data) {
         	$( element_id ).empty();
         	if(data){
	             $.each(data, function(index, item) {
	             	$(element_id).get(0).options[$(element_id).get(0).options.length] = new Option(item.columnName, item.id);
	             });
	             $(element_id).prepend("<option value='' selected='selected'></option>");
         	}
         }
     });
}



/**
 * Script for button management
 */
var _editor;

function showButtonIcons(){
	$.ajax({
		url: 'bpm/form/getButtonIcons',
	    type: 'GET',
	    dataType: 'json',
		async: false,
		success : function(response) {
			var htmlContent = "<table  width='100%'><tr>";
			for(var index = 0; index < response.length; index++){
				if(index % 5 == 0){
					htmlContent = htmlContent + "</tr><tr>";
				}
				htmlContent = htmlContent + "<td align='center'><img class='cursor-pointer' onClick='selectButtonIcon(\""+response[index]+"\");' style='padding:5px;border:1px solid gray;' src='"+response[index]+"'/></td>";
			}
			htmlContent = htmlContent + "</tr></table>";
			$("#button_icon_dialog").html(htmlContent);
			openButtonIconDialog('button_icon_dialog', 300, '选择图标');
		} 
	});
}

function openButtonIconDialog(divId, width, title){
	 clearPreviousPopup();
	 $myDialog = $("#"+divId);
	 $myDialog.dialog({
       width: width,
       height: 'auto',
       modal: true,
       title: title
	 });
	 $myDialog.dialog("open");
}

function selectButtonIcon(iconPath){
	return "<input type='button' value='Print' />";
	/*$("#_iconPath").val(iconPath);
	$("#choosed_iconPath").attr("src", iconPath);
	$("#button_icon_dialog").dialog("close");*/
}

function showFormButtons(editor){
	_editor = editor;
	var htmlContent = "";
	$.ajax({
		url: 'bpm/form/getFormButtons',
	    type: 'GET',
	    dataType: 'json',
		async: false,
		success : function(response) {
			htmlContent = "<table  width='100%'><tr>";
			for(var index = 0; index < response.length; index++){
				if(index % 5 == 0){
					htmlContent = htmlContent + "</tr><tr>";
				}
				
				htmlContent = htmlContent + "<td style='padding:10px;' align='center'> "
				+ "<input type='button' value='"+response[index].name+"' "
				+ "style='"+response[index].style+"' class='btn btn-primary normalButton' "
				+ "onClick='selectButton(\""+response[index].id+"\", \""+response[index].name+"\", \""+response[index].style+"\", \""+response[index].func+"\", \""+response[index].type+"\", \""+response[index].textValue+"\");' /></td>";
			}
			htmlContent = htmlContent + "</tr></table>";
			$("#form_button_dialog").html(htmlContent);
			openButtonIconDialog('form_button_dialog', 300, 'Select Buttons');
		} 
	});
}

function selectButton(id, name, style, func, type, textValue){
	_editor.insertHtml( "<input type='"+type+"' id='"+id+"' name='"+name+"' value='"+textValue+"' style='"+style+"' onclick='"+func+"' class='btn btn-primary normalButton'/>");
	//$("#_iconPath").val(iconPath);
	$("#form_button_dialog").dialog("close");
}


function showFormButtonsInDialog(editor){
	_editor = editor;
	var htmlContent = "<span class='color-red'>* </span><span class='form-title-msg'>"+form.msg.defaultButton+"</span>";
	$.ajax({
		url: 'bpm/form/getFormButtons',
	    type: 'GET',
	    dataType: 'json',
		async: false,
		success : function(response) {
			htmlContent  = htmlContent + "<table width='100%' align='left' style='vertical-align:middle;'><tr><td width='50%' style='vertical-align:middle;'>"
			htmlContent  = htmlContent + "<ul id='form_Button_List' style='height: 180px;list-style-type: none;overflow: auto;'>";
			for(var index = 0; index < response.length; index++){
				var buttonName=response[index].textValue;
				if(response[index].isDefault){
					buttonName=buttonName+" *";
				}
				htmlContent  = htmlContent + "<li id='"+response[index].id+"' style='font-size:15px;cursor:pointer; padding-left: 5px;' "
						+ " onmouseover='mouseOverList(this);' onmouseout='mouseOutList(this);'"
						+ " onclick='showBtnPreview(\""+response[index].id+"\",\""+response[index].name+"\",\""+response[index].style+"\",\""+response[index].func+"\",\""+response[index].type+"\",\""+response[index].textValue+"\",\""+response[index].iconPath+"\");'>"
						+ buttonName +" <img src='/images/remove.png' class='delete-btn displayNone padding-left:38%;' onclick='deleteSelectedButton(\""+response[index].id+"\");' id='deleteButton'></li>"	
			}
			htmlContent  = htmlContent + "</ul></td><td width='50%'><div style='padding-top: 50%;padding-left: 15%;' id='button_preview'></div></td></table>";
		} 
	});
	return htmlContent;
}

function showBtnPreview(id, name, style, func, type, textValue,iconPath){
	var htmlHiddenContent = "<input type='hidden' id='_previewId' value='"+id+"' />";
	htmlHiddenContent = htmlHiddenContent + "<input type='hidden' id='_previewName' value='"+name+"' />";
	htmlHiddenContent = htmlHiddenContent + "<input type='hidden' id='_previewStyle' value='"+style+"' />";
	htmlHiddenContent = htmlHiddenContent + "<input type='hidden' id='_previewFunc' value='"+func+"' />";
	htmlHiddenContent = htmlHiddenContent + "<input type='hidden' id='_previewType' value='"+type+"' />";
	htmlHiddenContent = htmlHiddenContent + "<input type='hidden' id='_previewTextValue' value='"+textValue+"' />";
	htmlHiddenContent = htmlHiddenContent + "<input type='hidden' id='_previewIconPath' value='"+iconPath+"' />";
	
	if(name == "print" || name == "save" || name == "close"){
		$('#button_preview').html("<input onClick='insertBtnInEditor(\""+id+"\", \""+name+"\", \""+style+"\", \""+func+"\", \""+type+"\", \""+textValue+"\");' class='btn btn-primary' id='"+id+"' name='"+name+"' style='"+style+"' type='button' value='"+textValue+"'>"+htmlHiddenContent);
	}else{
	style = '';
		$('#button_preview').html("<input onClick='insertBtnInEditor(\""+id+"\", \""+name+"\", \""+style+"\", \""+func+"\", \""+type+"\", \""+textValue+"\");' class='preview-btn' id='"+id+"' name='"+name+"' style='"+style+"' type='button' value='"+textValue+"' >"+htmlHiddenContent);
	}
}

function mouseOverList(obj){
	//$(obj).css('background-color','#428BCA');
	/*$(obj).css('border-radius','4px');*/
	$($(obj).find(".delete-btn")).show();
	//$($(obj).find(".btn-delete")).show();
	
}
function mouseOutList(obj){
	//$(obj).css('background-color','');
	$($(obj).find(".delete-btn")).hide();
}


function deleteSelectedButton(formButtonId){
	var status="";
	var message="";
	$.msgBox({
		title : form.title.confirm,
		content : form.msg.buttonDelete ,
		type : form.title.msgBoxTitleconfirm,
		buttons : [ {
			value : "Yes"
		}, {
			value : "No"
		} ],
		success : function(result) {
			if (result == "Yes") {
				$.ajax({
					url: 'bpm/form/deleteFormButton?formButtonId='+formButtonId,
				    type: 'GET',
				    dataType: 'json',
					async: false,
					success : function(response) {
						if(response.status=="success"){
							status="success";
						}else{
							status="error";
						}
						message=response.message;
					} 
				});
			}
		},
		afterClose : function() {
			if(status=="success"){
				$.msgBox({
					title : form.title.success,
					content : message,
					type : "success",
					afterClose : function() {
						$($("#form_Button_List").find("#"+formButtonId)).remove();
						var frame = $("#ckEditor").find("iframe")[0];
						$(frame).contents().find("img.cke_button").each(function(){
							var button = decodeURIComponent($(this).attr("data-cke-realelement"));
							var buttonDiv = document.createElement("div");
							buttonDiv.setAttribute("id","buttonDiv");
							buttonDiv.setAttribute("style","display:none");
							document.body.appendChild(buttonDiv);
							$("#buttonDiv").append(button);
							var children = buttonDiv.childNodes;
							if(children[0].id != undefined && children[0].id == formButtonId){
								$(this).remove();
							}
							$("#buttonDiv").remove();
						});
						$(frame).contents().find("img.cke_submit_btn").each(function(){
							var button = decodeURIComponent($(this).attr("data-cke-realelement"));
							var buttonDiv = document.createElement("div");
							buttonDiv.setAttribute("id","buttonDiv");
							buttonDiv.setAttribute("style","display:none");
							document.body.appendChild(buttonDiv);
							$("#buttonDiv").append(button);
							var children = buttonDiv.childNodes;
							if(children[0].id != undefined && children[0].id == formButtonId){
								$(this).remove();
							}
							$("#buttonDiv").remove();
						});
						$("#button_preview").html("");
					}
				});	
			}else if(status=="error"){
				$.msgBox({
					title : form.title.error,
					content : message,
					type : "error"
				});
			}
		}
	});
}



function insertBtnInEditor(id, name, style, func, type, textValue){
	if(name == "print" || name == "save" || name == "close"){
		_editor.insertHtml("<input id='"+id+"' style='"+style+"' type='"+type+"' value='"+textValue+"' name='"+name+"' onclick='"+func+"' class='btn btn-primary normalButton'/>");
	}else{
		_editor.insertHtml("<input id='"+id+"' style='"+style+"' type='"+type+"' iconpath='/images/sidemenu/todolist.png' value='"+textValue+"' name='"+name+"' onclick='"+func+"' />");
	}
	CKEDITOR.dialog.getCurrent().hide();
}

/*---------Form Auto id generation scripts starts ----------*/
function generateFormAutoId(){
	var prefix = document.getElementById("prefix").value;
	var title = document.getElementById("title").value;
	var year = document.getElementById("year").value;
	var month = document.getElementById("month").value;
	var day = document.getElementById("day").value;
	var seperator = document.getElementById("seperator").value;
	var autoId = "";
	if(prefix != ""){
		autoId += "{" + prefix + "}";
	}
	if(title != ""){
		autoId += seperator + "{" + title + "}";
	}
	if(year != ""){
		autoId += seperator + "{" + year + "}";
	}
	if(month != ""){
		autoId += seperator + "{" + month + "}";
	}
	if(day != ""){
		autoId += seperator + "{" + day + "}";
	}
	document.getElementById("autoIdFormat").value = autoId;
}

/*---------Form Auto id generation scripts end ----------*/

/*-------------------- create/edit form script - starts ---------------------*/
function getCkEditorElem(mode){
	/*var formName = $("#formName").val();
	var tableName = $("#tableName").val();
	var metaTableId = $("#tableId").val();
	var updatedSourceValue = $("#htmlsource").text();
	
	if(formName != ""){
		var htmlSourceText, editor_data;
		// editor_data = CKEDITOR.instances.editor1.getData();

		if($("#tab-2").hasClass("btn-primary")) {
						editor_data = updatedSourceValue;
		} else {
		        editor_data = CKEDITOR.instances.editor1.getData();			
		}
		if(mode == 'create'){
			
			htmlSourceText = editor_data;
		}else if(mode == 'edit'){
			htmlSourceText = editor_data;
		}*/

		var formName = $("#formName").val();
		var tableName = $("#tableName").val();
		var metaTableId = $("#tableId").val();
		var updatedSourceValue = $("#htmlsource").text();
		if(formName != ""){
			var htmlSourceText, editor_data;
			if($("#tabs-2").hasClass("btn-primary")){
			
				editor_data = updatedSourceValue;
			}else{
				editor_data = CKEDITOR.instances.editor1.getData();
			}
		var tempDiv = document.createElement('div');
		tempDiv.innerHTML = editor_data;
		var child = tempDiv.children[0].children[0];
		child.removeAttribute("contenteditable");
		editor_data = tempDiv.innerHTML;
		if(mode == 'create'){
			
			htmlSourceText = editor_data;
		}else if(mode == 'edit'){
			htmlSourceText = editor_data;
			
		}
		
		var htmlSourceViewData = "";
		document.getElementById('htmlSourceView').value = htmlSourceViewData;
		if(htmlSourceText.indexOf("<form") == -1 && htmlSourceText.indexOf(formName) == -1){
			validateMessageBox(form.title.validation, "Form is not present." , "info");
			return false;
		}
		document.getElementById('htmlSource').value = htmlSourceText;
		
		var str = "";
		str += startXML(formName,tableName,metaTableId);
		var editorObj = document.createElement('div');
		editorObj.innerHTML = editor_data;
		var input_tags=return_MemOfIdByTag(editorObj,'input');
		if(input_tags.length > 0){
			for(var index = 0; index < input_tags.length; index++){
				var type = input_tags[index].getAttribute('type');
				var name = input_tags[index].getAttribute('name');
				var value = input_tags[index].getAttribute('value');
				var tableId = input_tags[index].getAttribute('table');
				var columnId = input_tags[index].getAttribute('column');
				var optionType = input_tags[index].getAttribute('optionType');
				var dataDictionaryId = input_tags[index].getAttribute('dataDictionary');
				var richTextEditor = input_tags[index].getAttribute('img');
				var isSubForm = false;
				if(input_tags[index].getAttribute('isSubForm')!=null && input_tags[index].getAttribute('isSubForm')!="null" ){
					isSubForm=input_tags[index].getAttribute('isSubForm');
				}
				var autoComplete = input_tags[index].getAttribute('autocomplete');
				var fieldClass = input_tags[index].getAttribute('class');
				var onClick = "";
				var onFocus = "";
				var onChange ="";
				var onblur = "";
				
				if(type!="hidden"){
					formInputIds[formInputIds.length]=input_tags[index].getAttribute('id');
				}else if(name==null || name==""){
					userNameHiddenIds[userNameHiddenIds.length]=input_tags[index].getAttribute('id');
				}
				
				if(type=="radio" || type=="checkbox"){
					onChange = input_tags[index].getAttribute('onchange');
				}else{
					onClick = input_tags[index].getAttribute('onclick');
					onFocus = input_tags[index].getAttribute('onfocus');
					onblur = input_tags[index].getAttribute('onblur');
				}
				var eventSrt="";
		 		if(onClick && onClick.length>0){
		 			eventSrt += ' onclick= "'+onClick+'"';
		 		}
		 		if(onChange && onChange.length>0){
		 			eventSrt += ' onchange= "'+onChange+'"';
		 		}
		 		if(onFocus && onFocus.length>0){
		 			eventSrt+=' onfocus= "'+onFocus+'"';
		 		}
		 		if(onblur && onblur.length>0){
		 			eventSrt+=' onblur= "'+onblur+'"';
		 		}
		 		
		 		if(type!="submit" && type!="button" ){
		 			
		 			if(!checkFieldTableMapping(tableId,columnId,name)){
		 				return false;
		 			}
		 			
		 			if(type!="hidden" && name!=null){
		 				if(!checkFieldName(name,type)){
			 				return false;
			 			}	
		 				
		 				if(!checkFieldColumn(name,columnId,type)){
					     	  return false;
						}
		 			}
		 			
		 			str += drawInputXml(type, name, value, eventSrt, tableId, columnId,formName,fieldClass,isSubForm,optionType,dataDictionaryId, autoComplete);
		 			
		 		}
			}
		}
		
		
		
		var select_tags=return_MemOfIdByTag(editorObj,'select');
		if(select_tags.length > 0){
			for(var index = 0; index < select_tags.length; index++){
				//alert("select  : "+select_tags[index].getAttribute('name'));
				var type = 'select';
				var eventSrt="";
				var name = select_tags[index].getAttribute('name');
				var value = select_tags[index].getAttribute('value');
				var onChange = select_tags[index].getAttribute('onchange');
				var tableId = select_tags[index].getAttribute('table');
				var columnId = select_tags[index].getAttribute('column');
				var optionType = select_tags[index].getAttribute('optionType');
				var dataDictionaryId = select_tags[index].getAttribute('dataDictionary');
				var fieldClass = select_tags[index].getAttribute('class');
				var isSubForm = false;
				if(select_tags[index].getAttribute('isSubForm')!=null && select_tags[index].getAttribute('isSubForm')!="null" ){
					isSubForm=select_tags[index].getAttribute('isSubForm');
				}
				
				if(onChange && onChange.length>0){
		 			eventSrt += " onchange= '"+onChange+"'";
		 		}
				
				if(!checkFieldTableMapping(tableId,columnId,name)){
	 				return false;
	 			}
				
				if(!checkFieldName(name,type)){
	 				return false;
	 			}
				
				if(!checkFieldColumn(name,columnId,type)){
			     	  return false;
				}
				var option_tags=select_tags[index].options;
				
				str += drawSelectXml(type, name, value, eventSrt, tableId, columnId,formName,option_tags,optionType,dataDictionaryId,fieldClass,isSubForm);
				
			}
		}
		
		var ta_tags=return_MemOfIdByTag(editorObj,'textarea');
		if(ta_tags.length > 0){
			for(var index = 0; index < ta_tags.length; index++){
				var type = 'textarea';
				var name = '';
				var cols = '';
				var rows = '';
				var value = ta_tags[index].getAttribute('value');
				var onClick = ta_tags[index].getAttribute('onclick');
				var onFocus = ta_tags[index].getAttribute('onfocus');
				var tableId = ta_tags[index].getAttribute('table');
				var columnId = ta_tags[index].getAttribute('column');
				var onblur = ta_tags[index].getAttribute('onblur');
				var isSubForm = false;
				if(ta_tags[index].getAttribute('isSubForm')!=null && ta_tags[index].getAttribute('isSubForm')!="null" ){
					isSubForm=ta_tags[index].getAttribute('isSubForm');
				}
				var eventSrt="";
				
				if(ta_tags[index].getAttribute('name'))
					name = ta_tags[index].getAttribute('name');
				if(ta_tags[index].getAttribute('rows'))
					rows = ta_tags[index].getAttribute('rows');
				if(ta_tags[index].getAttribute('cols'))
					cols = ta_tags[index].getAttribute('cols');
				
		 		if(onClick && onClick.length>0){
		 			eventSrt += " onclick= '"+onClick+"'";
		 		}
		 		
		 		if(onFocus && onFocus.length>0){
		 			eventSrt+=" onfocus= '"+onFocus+"'";
		 		}
		 		if(onblur && onblur.length>0){
		 			eventSrt+=" onblur= '"+onblur+"'";
		 		}		 		
		 		
		 		if(!checkFieldTableMapping(tableId,columnId,name)){
	 				return false;
	 			}
		 		
		 		if(!checkFieldName(name,type)){
	 				return false;
	 			}
		 		
		 		if(!checkFieldColumn(name,columnId,type)){
			     	  return false;
				}
		 		
		 		str += drawTextAreaXml(type, name, rows, cols, eventSrt, tableId, columnId,formName, value,isSubForm); 
		 
			}
		}
		
		
		var img_tags=return_MemOfIdByTag(editorObj,'img');
		
		if(img_tags.length > 0){
			for(var index = 0; index < img_tags.length; index++){
				
				var className = img_tags[index].getAttribute('class');
				if(className == "richtextbox" || className == "gridviewcontrol"){
					var type = 'img';
					var eventSrt="";
					var name='';
					var gridTitle = img_tags[index].getAttribute('gridTitle');
					var src = img_tags[index].getAttribute('src');
					var gridName = img_tags[index].getAttribute('gridName');
					var tableId = img_tags[index].getAttribute('table');
					var columnId = img_tags[index].getAttribute('column');
					var height = img_tags[index].getAttribute('height');
					var width = img_tags[index].getAttribute('width');
					if(img_tags[index].getAttribute('name'))
						name = img_tags[index].getAttribute('name');
					var isSubForm = false;
					if(img_tags[index].getAttribute('isSubForm')!=null && img_tags[index].getAttribute('isSubForm')!="null" ){
						isSubForm=img_tags[index].getAttribute('isSubForm');
					}	
					str += drawImageXml(type,name ,gridName, gridTitle, src, className, isSubForm,tableId,columnId,formName, height, width);
				}
			}
		}
		str += endXML();
		if(str != ""){
			//$( "#temp_ckEditor_html_div" ).html(editor_data);			
			document.getElementById("temp_ckEditor_html_div").innerHTML = editor_data;			
			//to remove the hidden fields for user tree  
			for(var userNameHiddenIndex=0;userNameHiddenIndex<userNameHiddenIds.length;userNameHiddenIndex++){
				var tempHiddenId=userNameHiddenIds[userNameHiddenIndex]+"FullName";
				var hiddenId=userNameHiddenIds[userNameHiddenIndex];
				var status=false;
				for(var formIdIndex=0;formIdIndex<formInputIds.length;formIdIndex++){
					var formInputId=formInputIds[formIdIndex];
					if(tempHiddenId==formInputId){
						status=true;
						break;
					}else{
						status=false;
					}
				}
				if(!status){
	 				$( "#temp_ckEditor_html_div" ).find("#"+hiddenId).remove();
	 			}
			}
			
			document.getElementById('htmlSource').value=document.getElementById("temp_ckEditor_html_div").innerHTML;
			document.getElementById('xmlSource').value = str;
			userNameHiddenIds=[];
			formInputIds=[];
			columnIdList=[];
			form_Field_column_map={};
			form_Field_name_List=[];
			form_Field_name_Map={};
			destroyCKEditor();
			return true;
		}else{
			return false;
		}
	}else{
		getFormName();
		return false;
	}
}  

/* this function for preview data */
/*function getPreViewData(htmlSource){
	var startNode = document.getElementById('sourceView_id');
	document.getElementById('sourceView_id').innerHTML = htmlSource;
	getFormChildren(startNode ,true);
	var test_value = startNode.innerHTML;
	alert("test_value======"test_value);
	return test_value;
	
}*/
function getFormChildren(startNode, output){
	var nodes;
	if(startNode.childNodes)   {
		nodes = startNode.childNodes;
		childrenNode(nodes, output);
	}else{
		alert(form.msg.errorChildNotExist);
	}
}

function childrenNode(nodes, output)	{
	var node;
	for(var i=0;i<nodes.length;i++)	{
		node = nodes[i];
		if(output)   {
			childNode(node);
	    }
		if(node.childNodes)   {
			getFormChildren(node, output);
	    }
	}
}
function childNode(node)	{
        var isCheckBoxChecked = 0;
        var isRadioChecked = 0;
	if(node.nodeType == 1)	    {
	   if((node.getAttribute('value') == null || node.getAttribute('value') == "") || (node.id == null || node.id == "")){
	    if((node.tagName == 'INPUT' || node.tagName == 'input')&&(node.type != "radio" || node.type != "RADIO")&& (node.type != "checkbox" || node.type != "CHECKBOX")){	     	
	    		$('input[type=button]').each(function(){
			             $(this).remove();
			});
			$('input[type=submit]').each(function(){
			             $(this).remove();
			});	
	       		var htmlSourceViewObj = document.createElement("span");
	        	htmlSourceViewObj.setAttribute("id",node.id);
	        	htmlSourceViewObj.setAttribute("type",node.type);
	        	htmlSourceViewObj.setAttribute("name",node.getAttribute('name'));
	        	htmlSourceViewObj.setAttribute("table",node.getAttribute('table'));
			htmlSourceViewObj.setAttribute("column",node.getAttribute('column'));
			htmlSourceViewObj.setAttribute("optionType",node.getAttribute('optionType'));
			htmlSourceViewObj.setAttribute("dataDictionary",node.getAttribute('dataDictionary'));
			htmlSourceViewObj.setAttribute("isSubForm",node.getAttribute('isSubForm'));
			htmlSourceViewObj.setAttribute("autocomplete",node.getAttribute('autocomplete'));
			htmlSourceViewObj.setAttribute("onchange",node.getAttribute('onchange'));
			htmlSourceViewObj.setAttribute("onchange",node.getAttribute('onClick'));
			htmlSourceViewObj.setAttribute("onchange",node.getAttribute('onFocus'));
			htmlSourceViewObj.setAttribute("onblur",node.getAttribute('onblur'));
			htmlSourceViewObj.setAttribute("class",node.getAttribute('class'));
			var value=document.createTextNode(node.value);
			htmlSourceViewObj.appendChild(value);
			$("#"+node.id).replaceWith(htmlSourceViewObj);
			

		}else if(node.id == null || node.id == "") {
			
		}else if(node.tagName == "SELECT" || node.tagName == 'select'){
			var htmlSourceViewObj = document.createElement("span");
	        	htmlSourceViewObj.setAttribute("id",node.id);
	        	htmlSourceViewObj.setAttribute("type",node.type);
	        	htmlSourceViewObj.setAttribute("name",node.getAttribute('name'));
	        	htmlSourceViewObj.setAttribute("table",node.getAttribute('table'));
			htmlSourceViewObj.setAttribute("column",node.getAttribute('column'));
			htmlSourceViewObj.setAttribute("optionType",node.getAttribute('optionType'));
			htmlSourceViewObj.setAttribute("dataDictionary",node.getAttribute('dataDictionary'));
			htmlSourceViewObj.setAttribute("isSubForm",node.getAttribute('isSubForm'));
			htmlSourceViewObj.setAttribute("autocomplete",node.getAttribute('autocomplete'));
			htmlSourceViewObj.setAttribute("onchange",node.getAttribute('onchange'));
			htmlSourceViewObj.setAttribute("onchange",node.getAttribute('onClick'));
			htmlSourceViewObj.setAttribute("onchange",node.getAttribute('onFocus'));
			htmlSourceViewObj.setAttribute("onblur",node.getAttribute('onblur'));
			htmlSourceViewObj.setAttribute("class",node.getAttribute('class'));
			var value=document.createTextNode(node.value);
			htmlSourceViewObj.appendChild(value);
			$("#"+node.id).replaceWith(htmlSourceViewObj);
			
		}else if((node.tagName == 'textarea' || node.tagName == 'TEXTAREA')){
			var htmlSourceViewObj = document.createElement("span");
			htmlSourceViewObj.setAttribute("id",node.id);
			htmlSourceViewObj.setAttribute("type",node.type);
			htmlSourceViewObj.setAttribute("name",node.getAttribute('name'));
			htmlSourceViewObj.setAttribute("table",node.getAttribute('table'));
			htmlSourceViewObj.setAttribute("column",node.getAttribute('column'));
			htmlSourceViewObj.setAttribute("isSubForm",node.getAttribute('isSubForm'));
			htmlSourceViewObj.setAttribute("onchange",node.getAttribute('onClick'));
			htmlSourceViewObj.setAttribute("onchange",node.getAttribute('onFocus'));
			htmlSourceViewObj.setAttribute("onblur",node.getAttribute('onblur'));
			htmlSourceViewObj.setAttribute("class",node.getAttribute('class'));
			htmlSourceViewObj.setAttribute("cols",node.getAttribute('cols'));
			htmlSourceViewObj.setAttribute("rows",node.getAttribute('rows'));
			var value=document.createTextNode(node.value);
		    htmlSourceViewObj.appendChild(value);
		    $("#"+node.id).replaceWith(htmlSourceViewObj);
			
		}else if((node.tagName == 'img' || node.tagName == 'IMG')){
			var htmlSourceViewObj = document.createElement("span");
			htmlSourceViewObj.setAttribute("id",node.id);
			htmlSourceViewObj.setAttribute("name",node.name);
			htmlSourceViewObj.setAttribute("src",node.getAttribute('src'));
			htmlSourceViewObj.setAttribute("gridName",node.getAttribute('gridName'));
			htmlSourceViewObj.setAttribute("table",node.getAttribute('table'));
			htmlSourceViewObj.setAttribute("column",node.getAttribute('column'));
			htmlSourceViewObj.setAttribute("height",node.getAttribute('height'));
			htmlSourceViewObj.setAttribute("width",node.getAttribute('width'));
			htmlSourceViewObj.setAttribute("isSubForm",node.getAttribute('isSubForm'));
			var value=document.createTextNode(node.getAttribute('src'));
		    	htmlSourceViewObj.appendChild(value);
		    	$("#"+node.id).replaceWith(htmlSourceViewObj);
		}
		
		else{
			
		
		}
	    }else{
	    	if((node.tagName == 'INPUT' || node.tagName == 'input')&&(node.type == "checkbox" || node.type == "CHECKBOX")){ 
	    		
	    	if(isCheckBoxChecked == 0){
 			$('input[type=checkbox]').each(function(){
 
 			 if($(this).is(':checked')){
                   		 var htmlSourceViewObj = document.createElement("span");
				htmlSourceViewObj.setAttribute("id",this.id);
				htmlSourceViewObj.setAttribute("type",this.type);
				htmlSourceViewObj.setAttribute("name",this.getAttribute('name'));
				htmlSourceViewObj.setAttribute("table",this.getAttribute('table'));
				htmlSourceViewObj.setAttribute("column",this.getAttribute('column'));
				htmlSourceViewObj.setAttribute("optionType",this.getAttribute('optionType'));
				htmlSourceViewObj.setAttribute("dataDictionary",this.getAttribute('dataDictionary'));
				htmlSourceViewObj.setAttribute("isSubForm",this.getAttribute('isSubForm'));
				htmlSourceViewObj.setAttribute("autocomplete",this.getAttribute('autocomplete'));
				htmlSourceViewObj.setAttribute("onchange",this.getAttribute('onchange'));
				htmlSourceViewObj.setAttribute("class",this.getAttribute('class'));
				var value=document.createTextNode("True ");
				htmlSourceViewObj.appendChild(value);
				$(this).replaceWith(htmlSourceViewObj);
               	      	}
			if ($(this).is(':not(:checked)')){
             			var htmlSourceViewObj = document.createElement("span");
				htmlSourceViewObj.setAttribute("id",this.id);
				htmlSourceViewObj.setAttribute("type",this.type);
				htmlSourceViewObj.setAttribute("name",this.getAttribute('name'));
				htmlSourceViewObj.setAttribute("table",this.getAttribute('table'));
				htmlSourceViewObj.setAttribute("column",this.getAttribute('column'));
				htmlSourceViewObj.setAttribute("optionType",this.getAttribute('optionType'));
				htmlSourceViewObj.setAttribute("dataDictionary",this.getAttribute('dataDictionary'));
				htmlSourceViewObj.setAttribute("isSubForm",this.getAttribute('isSubForm'));
				htmlSourceViewObj.setAttribute("autocomplete",this.getAttribute('autocomplete'));
				htmlSourceViewObj.setAttribute("onchange",this.getAttribute('onchange'));
				htmlSourceViewObj.setAttribute("class",this.getAttribute('class'));
				var value=document.createTextNode("False ");
				htmlSourceViewObj.appendChild(value);
				$(this).replaceWith(htmlSourceViewObj);

			}

			});
		isCheckBoxChecked = 1;
	    	}		
	    	} else if((node.tagName == 'INPUT' || node.tagName == 'input')&&(node.type == "radio" || node.type == "RADIO")){
	    	    	if(isRadioChecked == 0){
			$('input[type=radio]').each(function(){
 			   if($(this).is(':checked')){
                    		var htmlSourceViewObj = document.createElement("span");
				htmlSourceViewObj.setAttribute("id",this.id);
				htmlSourceViewObj.setAttribute("type",this.type);
				htmlSourceViewObj.setAttribute("name",this.getAttribute('name'));
				htmlSourceViewObj.setAttribute("table",this.getAttribute('table'));
				htmlSourceViewObj.setAttribute("column",this.getAttribute('column'));
				htmlSourceViewObj.setAttribute("optionType",this.getAttribute('optionType'));
				htmlSourceViewObj.setAttribute("dataDictionary",this.getAttribute('dataDictionary'));
				htmlSourceViewObj.setAttribute("isSubForm",this.getAttribute('isSubForm'));
				htmlSourceViewObj.setAttribute("autocomplete",this.getAttribute('autocomplete'));
				htmlSourceViewObj.setAttribute("onchange",this.getAttribute('onchange'));
				htmlSourceViewObj.setAttribute("class",this.getAttribute('class'));
				var value=document.createTextNode("True ");
				htmlSourceViewObj.appendChild(value);
				$(this).replaceWith(htmlSourceViewObj);
               		  }
			  if ($(this).is(':not(:checked)')){
             			var htmlSourceViewObj = document.createElement("span");
				htmlSourceViewObj.setAttribute("id",this.id);
				htmlSourceViewObj.setAttribute("type",this.type);
				htmlSourceViewObj.setAttribute("name",this.getAttribute('name'));
				htmlSourceViewObj.setAttribute("table",this.getAttribute('table'));
				htmlSourceViewObj.setAttribute("column",this.getAttribute('column'));
				htmlSourceViewObj.setAttribute("optionType",this.getAttribute('optionType'));
				htmlSourceViewObj.setAttribute("dataDictionary",this.getAttribute('dataDictionary'));
				htmlSourceViewObj.setAttribute("isSubForm",this.getAttribute('isSubForm'));
				htmlSourceViewObj.setAttribute("autocomplete",this.getAttribute('autocomplete'));
				htmlSourceViewObj.setAttribute("onchange",this.getAttribute('onchange'));
				htmlSourceViewObj.setAttribute("class",this.getAttribute('class'));
				var value=document.createTextNode("False ");
				htmlSourceViewObj.appendChild(value);
				$(this).replaceWith(htmlSourceViewObj);

			 }

		    });
		   isRadioChecked = 1;
	    	  }		
	     }else if((node.tagName == 'INPUT' || node.tagName == 'input')&&(node.type != "radio" || node.type != "RADIO")&& (node.type != "checkbox" || node.type != "CHECKBOX")){
	     		$('input[type=button]').each(function(){
			             $(this).remove();
			});
			$('input[type=submit]').each(function(){
			             $(this).remove();
			});
	       		var htmlSourceViewObj = document.createElement("span");
	        	htmlSourceViewObj.setAttribute("id",node.id);
	        	htmlSourceViewObj.setAttribute("type",node.type);
	        	htmlSourceViewObj.setAttribute("name",node.getAttribute('name'));
	        	htmlSourceViewObj.setAttribute("table",node.getAttribute('table'));
			htmlSourceViewObj.setAttribute("column",node.getAttribute('column'));
			htmlSourceViewObj.setAttribute("optionType",node.getAttribute('optionType'));
			htmlSourceViewObj.setAttribute("dataDictionary",node.getAttribute('dataDictionary'));
			htmlSourceViewObj.setAttribute("isSubForm",node.getAttribute('isSubForm'));
			htmlSourceViewObj.setAttribute("autocomplete",node.getAttribute('autocomplete'));
			htmlSourceViewObj.setAttribute("onchange",node.getAttribute('onchange'));
			htmlSourceViewObj.setAttribute("onchange",node.getAttribute('onClick'));
			htmlSourceViewObj.setAttribute("onchange",node.getAttribute('onFocus'));
			htmlSourceViewObj.setAttribute("onblur",node.getAttribute('onblur'));
			htmlSourceViewObj.setAttribute("class",node.getAttribute('class'));
			var value=document.createTextNode(node.getAttribute('value'));
			htmlSourceViewObj.appendChild(value);
			$("#"+node.id).replaceWith(htmlSourceViewObj);

		}else if((node.tagName == 'img' || node.tagName == 'IMG')){
			var htmlSourceViewObj = document.createElement("span");
			htmlSourceViewObj.setAttribute("id",node.id);
			htmlSourceViewObj.setAttribute("name",node.name);
			htmlSourceViewObj.setAttribute("src",node.getAttribute('src'));
			htmlSourceViewObj.setAttribute("gridName",node.getAttribute('gridName'));
			htmlSourceViewObj.setAttribute("table",node.getAttribute('table'));
			htmlSourceViewObj.setAttribute("column",node.getAttribute('column'));
			htmlSourceViewObj.setAttribute("height",node.getAttribute('height'));
			htmlSourceViewObj.setAttribute("width",node.getAttribute('width'));
			htmlSourceViewObj.setAttribute("isSubForm",node.getAttribute('isSubForm'));
			var value=document.createTextNode(node.getAttribute('src'));
		    	htmlSourceViewObj.appendChild(value);
		    	$("#"+node.id).replaceWith(htmlSourceViewObj);
		}else{
			var htmlSourceViewObj = document.createElement("span");
			htmlSourceViewObj.setAttribute("id",node.id);
			htmlSourceViewObj.setAttribute("name",node.getAttribute('name'));
			htmlSourceViewObj.setAttribute("table",node.getAttribute('table'));
			htmlSourceViewObj.setAttribute("column",node.getAttribute('column'));
			htmlSourceViewObj.setAttribute("dataDictionary",node.getAttribute('dataDictionary'));
			htmlSourceViewObj.setAttribute("class",node.getAttribute('class'));
			var value=document.createTextNode(node.value);
		    htmlSourceViewObj.appendChild(value);
		    $("#"+node.id).replaceWith(htmlSourceViewObj);
		}
		
	   }
      }                           
}
function getFormName(){

	if(typeof String.prototype.trim !== 'function') {
  String.prototype.trim = function() {
    return this.replace(/^\s+|\s+$/g, ''); 
  }
}
	var tempFormname = "";
	var tempTablename = "";
	$.msgBox({ type: "prompt",
		title: form.title.getformdetails,
	    inputs: [
	    { header: form.title.formname+"&nbsp;&nbsp;&nbsp;", type: "text", name: "_formName",id:"_formName" },
	    { header: form.title.tablename+"&nbsp;&nbsp;&nbsp;", type: "text", name: "_tableName", autocomplete: "true", datalistId : "searchTableresults", onblur: "setFormTableDetails()"},
	    { header: form.title.moduleName, type: "hidden", id: "_moduleName", name: "_moduleName", autocomplete: "true", datalistId : "searchModuleresults", onblur: "setFormModuleDetails('_moduleId','true')"},
	   /* { header: form.title.ispublic +" &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", type: "checkbox", name: "_isPublic", value:'public', onclick:'setDefaultModuleDetails(this)'},*/
	    { header: form.title.description, type: "text", name: "_description", id:"_description"},
	    { header: form.title.isTemplate+"&nbsp;&nbsp;&nbsp;&nbsp;", type: "checkbox", name: "_isTemplate", value:'template' },
	    { header: "", type: "hidden", name: "_tableId" },
	    { header: "", type: "hidden", name: "_moduleId" },
 		{ header: form.title.isDefault+"&nbsp;&nbsp;&nbsp;&nbsp;", type: "checkbox", name: "isDefault", value:'false' , id:'isDefault' }],
	    buttons: [
	    { value: "Ok" }, {value:"Cancel"}],
	    success: function (result, values) {
	    	if(result == "Ok"){
	    		var ajaxLoaderFrame = new ajaxLoader(document.getElementById('ajax_loader'));
	    		tempFormname=values[0].value.trim();
	    		tempTablename = values[1].value.trim();
	    		var tempTableId=values[5].value.trim();
	    		var tempModuleId=selectedFromModuleId;
	    		var tempPublicForm = false;
	    		var tempIsTemplate = false;
				var tempDefault = false;
	    		var description = values[3].value;
				/*if(values[3].checked != null){
					tempPublicForm = true;
				}
				*/
				//alert(tempFormname);
				if(values[4].checked != null){
					tempIsTemplate = true;
				}
				
				if(values[7].checked != null){
					tempDefault = true;
				}
				
				//alert("======is template value====="+tempDefault+"======"+tempIsTemplate);
	    		if(tempFormname!="" ){
	    			if(!(tempFormname.indexOf(' ') > -1)){
			    		$("#formName").val(tempFormname);
			    		setFormName(tempFormname, false, tempTablename);
			    		if(tempTablename!=""){
			    			var table_name=tempTablename.toUpperCase().replace(/[" "]/g,"_");
			    			var englishName = Math.random().toString(36).substring(7);
			    			$("#tableName").val(table_name);
			    			$("#tableId").val(tempTableId);
			    			$("#moduleId").val(tempModuleId);
			    			$("#publicForm").val(tempPublicForm);
			    			$("#templateForm").val(tempIsTemplate);
							$("#isDefaultForm").val(tempDefault);
			    			$("#description").val(description);
			    			$("#englishName").val(englishName+new Date().getTime());
				    	}else{
			    			setTimeout(function() {
				    			openMessage(form.title.error,form.error.emptytablename,"error",false,tempFormname,tempTablename,tempIsTemplate,tempDefault);
				    		}, 500);
			    		}
	    			}else{
	    				setTimeout(function() {
			    			openMessage(form.title.error,form.error.formnamespace,"error",false,tempFormname,tempTablename,tempIsTemplate,tempDefault);
			    		}, 500);
	    			}
		    	}else{
		    		setTimeout(function() {
		    			openMessage(form.title.error,form.error.emptyformname,"error",false,tempFormname,tempTablename,tempIsTemplate,tempDefault);
		    		}, 500);
		    	}
	    		var height = $("#target").height();
	    		var ckEditor = $("#ckEditor").children().children().children('.cke_contents');
	    		
	    		ckEditor.css('height',parseInt(height)-295);
	    		$("#ckEditor").css('height',parseInt(height)-180);
	    		if (ajaxLoaderFrame) ajaxLoaderFrame.remove();
	    	}else{
	    		goBackToFormListView();
	    	}
	    }
	});
	setTableNamesList();
	setModuleNamesList('_moduleId');
}


//To retain field values
function getFormNameWithValues(enteredFormName, enteredTableValue,isTemplateValue,isDefaultValue){

	var tempFormname = enteredFormName;
	var tempTablename = enteredTableValue;
	var templateCheckbox;
	var defaultCheckbox;		

	if(isTemplateValue){
 		templateCheckbox =  { header: form.title.isTemplate+"&nbsp;&nbsp;&nbsp;&nbsp;",id:"_isTemplate", type: "checkbox", name: "_isTemplate", 		value:'template',checked: isTemplateValue};
} else{
	templateCheckbox = { header: form.title.isTemplate+"&nbsp;&nbsp;&nbsp;&nbsp;",id:"_isTemplate", type: "checkbox", name: "_isTemplate", value:'template'};
}
	if(isDefaultValue) {
		defaultCheckbox =  { header: form.title.isDefault+"&nbsp;&nbsp;&nbsp;&nbsp;",id:"_isDefault", type: "checkbox", name: "_isDefault", 		value:'defaultValue',checked: isDefaultValue};
} else{
	defaultCheckbox = { header: form.title.isDefault+"&nbsp;&nbsp;&nbsp;&nbsp;",id:"_isDefault", type: "checkbox", name: "_isDefault", value:'defaultValue'};	
	}
	
	$.msgBox({ type: "prompt",
		title: form.title.getformdetails,
	    inputs: [
	    { header: form.title.formname+"&nbsp;&nbsp;&nbsp;", type: "text", name: "_formName", value: enteredFormName },
	    { header: form.title.tablename+"&nbsp;&nbsp;&nbsp;", type: "text", name: "_tableName",value: enteredTableValue, autocomplete: "true", datalistId : "searchTableresults", onblur: "setFormTableDetails()"},
	    { header: form.title.moduleName, type: "hidden", id: "_moduleName", name: "_moduleName", autocomplete: "true", datalistId : "searchModuleresults", onblur: "setFormModuleDetails('_moduleId','true')"},
	      { header: form.title.description+"&nbsp;&nbsp;", type: "text", name: "_description", id:"_description"},
	   /* { header: form.title.ispublic +" &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", type: "checkbox", name: "_isPublic", value:'public', onclick:'setDefaultModuleDetails(this)'},*/

	    templateCheckbox,
	    defaultCheckbox,
	    { header: "", type: "hidden", name: "_tableId" },
	    { header: "", type: "hidden", name: "_moduleId" }],
	    buttons: [
	    { value: "Ok" }, {value:"Cancel"}],
	    success: function (result, values) {
	    	if(result == "Ok"){
	    		var ajaxLoaderFrame = new ajaxLoader(document.getElementById('ajax_loader'));
	    		tempFormname=values[0].value.trim();
	    		tempTablename = values[1].value.trim();
	    		var tempTableId=values[5].value.trim();
	    		var tempModuleId=selectedFromModuleId;
	    		var tempPublicForm = false;
	    		var tempIsTemplate = false;
				var tempDefault = false;
	    		var description = values[3].value;
				/*if(values[3].checked != null){
					tempPublicForm = true;
				}
				*/
				//alert(tempFormname);
				if(values[4].checked != null){
					tempIsTemplate = true;
				}
				if(values[5].checked != null){
					tempDefault = true;
				}
	    		if(tempFormname!="" ){
	    			if(!(tempFormname.indexOf(' ') > -1)){
			    		$("#formName").val(tempFormname);
			    		setFormName(tempFormname, false, tempTablename);
			    		if(tempTablename!=""){
			    			var table_name=tempTablename.toUpperCase().replace(/[" "]/g,"_");
			    			var englishName = Math.random().toString(36).substring(7);
			    			$("#tableName").val(table_name);
			    			$("#tableId").val(tempTableId);
			    			$("#moduleId").val(tempModuleId);
			    			$("#publicForm").val(tempPublicForm);
			    			$("#templateForm").val(tempIsTemplate);
							$("#isDefaultForm").val(tempDefault);						
			    			$("#description").val(description);
			    			$("#englishName").val(englishName+new Date().getTime());
				    	}else{
			    			setTimeout(function() {
				    			openMessage(form.title.error,form.error.emptytablename,"error",false,tempFormname,tempTablename,tempIsTemplate,tempDefault);
				    		}, 500);
			    		}
	    			}else{
	    				setTimeout(function() {
			    			openMessage(form.title.error,form.error.formnamespace,"error",false,tempFormname,tempTablename,tempIsTemplate,tempDefault);
			    		}, 500);
	    			}
		    	}else{
		    		setTimeout(function() {
		    			openMessage(form.title.error,form.error.emptyformname,"error",false,tempFormname,tempTablename,tempIsTemplate,tempDefault);
		    		}, 500);
		    	}
	    		var height = $("#target").height();
	    		var ckEditor = $("#ckEditor").children().children().children('.cke_contents');
	    		
	    		ckEditor.css('height',parseInt(height)-295);
	    		$("#ckEditor").css('height',parseInt(height)-180);
	    		if (ajaxLoaderFrame) ajaxLoaderFrame.remove();
	    	}else{
	    		goBackToFormListView();
	    	}
	    }
	});
	setTableNamesList();
	setModuleNamesList('_moduleId');
}



function setDefaultModuleDetails(checkObj){
	if(checkObj.checked == true){
		document.getElementById('_moduleName').disabled = true;
		document.getElementById('_moduleName').value = "Default Module";
	}else{
		document.getElementById('_moduleName').value = "";
		document.getElementById('_moduleName').disabled = false;
	}
}

function setFormName(tempFormname, isSave, tempTablename){
	if(/^[\-a-zA-Z\u4e00-\u9eff\s][\-a-zA-Z\u4e00-\u9eff\s0-9]*$/.test(tempFormname) !=false){
		//saveFormPanel();
		var newdata = $.ajax({
			type: "GET",
			async:false,
			url: "bpm/form/isFormNamePresent?formName="+tempFormname
		}).responseText;
	
		var da = eval('('+newdata+')');
		if(da.isPresent == true){
			$("#formName").val(""); 
			setTimeout(function() {
				openMessage(form.error.formexists,form.msg.differentformname,"info", false);
			}, 500);
		}else{
			document.getElementById("showFormName").innerHTML = "&nbsp;- " + tempFormname;
			if(isSave){
				getElem();
				$("#saveForm").submit();
			}
			if(tempTablename != "" )
				CKEDITOR.instances["editor1"].openDialog('form');
		}
	}else{
		$("#formName").val(""); 
		setTimeout(function() {
			openMessage(form.title.error,form.error.specialcharformname,form.title.msgBoxTitleerror, false);
		}, 500);
	}
}

function destroyCKEditor(){
	var instance = CKEDITOR.instances['editor1'];
	if(instance){
		$("#ckEditor").hide();
		$("#save_cke_form").hide();
		$("#update_cke_form").hide();
		CKEDITOR.instances['editor1'].destroy();
  	}
}

function checkFieldName(name,type){
	var status=false;
	var isIdExists = false;
	if(name.toLowerCase() == "id"){
		status=false;
		isIdExists = true;	
	} else if( form_Field_name_List.length==0  ) {
		status = true;
	} else{
		for(var index=0;index<form_Field_name_List.length;index++){
			if(form_Field_name_List[index]==name){
				if(type=="radio" || type=="checkbox"){
					if(form_Field_name_Map[name]==type){
						status=true;
					}else{
						status=false;
						break;
					}
				}else{
					status=false;
					break;
				}
			}else{
				status=true;
			}
		}			
	}
	
	if(status){
		form_Field_name_List[form_Field_name_List.length]=name;
		form_Field_name_Map[name]=type;
		return status;
	}else{
		if(isIdExists) {
			$.msgBox({
    		    title:form.title.check ,
    		    content:"Field Name "+name+" contains ID",
    		    afterClose:function (){
    		    	var height = $("#target").height();
    		    	var ckEditor = $("#ckEditor").children().children().children('.cke_contents');
    		    	ckEditor.css('height',parseInt(height)-295);
					$("#ckEditor").css('height',parseInt(height)-180);
    		    }
   			});
		} else {
			$.msgBox({
    		    title:form.title.check ,
    		    content:"Field Name "+name+" is duplicated in ("+form_Field_name_Map[name]+","+type+").",
    		    afterClose:function (){
    		    	var height = $("#target").height();
    		    	var ckEditor = $("#ckEditor").children().children().children('.cke_contents');
    		    	ckEditor.css('height',parseInt(height)-295);
					$("#ckEditor").css('height',parseInt(height)-180);
    		    }
   			});
		}
		form_Field_name_List=[];
		form_Field_name_Map={};
		columnIdList=[];
		form_Field_column_map={};
		return status;
	}

}

function checkFieldTableMapping(tableId,columnId,name){
	if(tableId=="" || columnId=="" ){
		$.msgBox({
		    title: form.title.check,
		    content: form.error.fieldname + "'"+name+"'" + form.error.fieldnotmappedwithtable,
		    afterClose:function (){
		    	var height = $("#target").height();
		    	var ckEditor = $("#ckEditor").children().children().children('.cke_contents');
		    	ckEditor.css('height',parseInt(height)-295);
				$("#ckEditor").css('height',parseInt(height)-180);
		    }
			});
		return false;
	}else{
		return true;
	}
}

function checkFieldColumn(name,columnId,type){
	var status=false;
	if(columnIdList.length==0){
		status=true;
	}else{
		for(var index=0;index<columnIdList.length;index++){
			if(columnIdList[index]==columnId){
				if(type=="radio" || type=="checkbox"){
					if(form_Field_column_map[columnId]==name){
						status=true;
					}else{
						status=false;
						break;
					}
				}else{
					status=false;
					break;
				}
			}else{
				status=true;
			}
		}			
	}
	
	if(status){
		columnIdList[columnIdList.length]=columnId;
		form_Field_column_map[columnId]=name;
		return status;
	}else{
		$.msgBox({
    		    title:form.title.check ,
    		    content:"Table column duplicated in ( "+form_Field_column_map[columnIdList[index]]+" and "+name+" Fields )",
   			});
		columnIdList=[];
		radioOrCheckboxCount=0;
		form_Field_column_map={};
		form_Field_name_List=[];
		form_Field_name_Map={};
		return status;
	}

}

function startXML(formName,tableName, metaTableId){
	var str="";
	str += "<?xml version='1.0' encoding='utf-8'?><form initiator = '"+formName+"' id='"+formName+"' name='"+formName+"' action='#' label='"+formName+"' tableName='"+tableName+"' tableId='"+metaTableId+"'  method='post'><extensionElements>";
	return str;
}

function endXML(){
	/*var subFormXml="";
	if(sub_Form_Field_List.length!=0 && sub_Form_Relation_List!=0){
		for(var subFormIndex=0;subFormIndex<sub_Form_Field_List.length;subFormIndex++){
			var listOfSupForm=sub_Form_Field_List[subFormIndex]["subForm_"+subFormIndex];
			for(var subMap=0;subMap<listOfSupForm.length;subMap++){
				subFormXml += listOfSupForm[subMap].xmlContent;
			}
		}
		//createRelationshipTable();
	}
	var str="";
	str += subFormXml+"</extensionElements></form>";
	*/
	var str = "";
	str += "</extensionElements></form>";
	/*sub_Form_Field_List=[];
	sub_Form_Relation_List=[];*/
	return str;
}

/*function createRelationshipTable(){
	var param={ subFormRelationList: JSON.stringify(sub_Form_Relation_List) }
	
	 $.ajax({	
		type: 'POST',
		async: false,
		url : 'bpm/form/createRelationshipTables',
		data: param,
		success : function(response) {
			
		}
	}); 
}*/

function drawInputXml(type, name, value,eventSrt, tableId, columnId,formName,fieldClass,isSubForm,optionType,dataDictionary,autoComplete){
	var str = "";
	if(type=="radio" || type=="checkbox"){
		str += "<formProperty type= '"+type+"' id='"+formName+"_"+name+"_"+radioOrCheckboxCount+"' name='"+name+"' isSubForm='"+isSubForm+"' "+ eventSrt +" table='"+tableId+"' optionType='"+optionType+"' dataDictionary='"+dataDictionary+"' ";
		
		if(value!="" && value!=null && value!="null"){
			str +=" value='"+value+"' "; 
		}
		
		if(fieldClass!="" && fieldClass!=null && fieldClass!="null"){
			str +=" fieldClass='"+fieldClass+"' ";
		}
		
		str +=" column='"+columnId+"'></formProperty>";
		radioOrCheckboxCount++;
	}else{
		str += "<formProperty type= '"+type+"' id='"+formName+"_"+name+"' name='"+name+"' isSubForm='"+isSubForm+"' ";
		
		if(fieldClass!="" && fieldClass!=null && fieldClass!="null"){
			str +=" fieldClass='"+fieldClass+"' ";
		}
		
		if(value!="" && value!=null && value!="null"){
			str +=" value='"+value+"' "; 
		}
		if(autoComplete && autocomplete != ''){
			str += eventSrt +" autocomplete='"+autoComplete+"' table='"+tableId+"' column='"+columnId+"'></formProperty>";
		}else{
			str += eventSrt +" table='"+tableId+"' column='"+columnId+"' optionType='"+optionType+"'></formProperty>";
		}
		
	}
	
	return str;
}


/*function drawSubFormInputXml(type, name, value,eventSrt, tableId, columnId,formName){
	var str = "";
	str += "<formProperty type= '"+type+"' id='"+formName+"_"+name+"' name='"+name+"' isSubForm='true' value='"+value+"' "+ eventSrt +" table='"+tableId+"' column='"+columnId+"'></formProperty>";
	return str;
}*/

function drawSelectXml(type, name, value, eventSrt, tableId, columnId,formName,option_tags,optionType,dataDictionary,fieldClass,isSubForm){
	var str = "";
	var optionStr="";
	if(option_tags.length > 0){
		for(var index = 0; index < option_tags.length; index++){
			var value=option_tags[index].getAttribute('value');
			var label=option_tags[index].label;
			optionStr +="<formProperty type= 'option' value='"+value+"' label='"+label+"'></formProperty>"
		}
		str += "<formProperty type= '"+type+"' id='"+formName+"_"+name+"' name='"+name+"' isSubForm='"+isSubForm+"' "+ eventSrt;
		
		if(value!="" && value!=null && value!="null"){
			str +=" value='"+value+"' "; 
		}
		
		if(fieldClass!="" && fieldClass!=null && fieldClass!="null"){
			str +=" fieldClass='"+fieldClass+"' ";
		}
		str +=" table='"+tableId+"' column='"+columnId+"' optionType='"+optionType+"' dataDictionary='"+dataDictionary+"'>"+optionStr+"</formProperty>";
	}else{
		str += "<formProperty type= '"+type+"' id='"+formName+"_"+name+"' name='"+name+"' isSubForm='"+isSubForm+"' "+ eventSrt +" table='"+tableId+"' column='"+columnId+"' optionType='"+optionType+"' ";
		if(fieldClass!="" && fieldClass!=null && fieldClass!="null"){
			str +="fieldClass='"+fieldClass+"' ";
		}
		
		if(value!="" && value!=null && value!="null"){
			str +=" value='"+value+"' "; 
		}
		
		str +=" dataDictionary='"+dataDictionary+"'></formProperty>";	
	}
	
	return str;
}

function drawTextAreaXml(type, name, rows, cols, eventSrt, tableId, columnId,formName,value,isSubForm){
	var str = "";
	str += "<formProperty type= '"+type+"' id='"+formName+"_"+name+"' name='"+name+"' isSubForm='"+isSubForm+"' rows='"+rows+"' cols='"+cols+"' "+eventSrt+" table='"+tableId+"' column='"+columnId+"' ";
	
	if(value!="" && value!=null && value!="null"){
		str +=" value='"+value+"' "; 
	}
	
	str +=" ></formProperty>";
	
	return str;
}

function drawImageXml(type,name, gridName, gridTitle, imagePath, className, isSubForm,tableId ,columnId ,formName ,height, width){
	var str = "";
	if(className == "richtextbox"){
		str += "<formProperty  type= '"+type+"' id='"+formName+"_"+name+"' name='"+name+"'  class= '"+className+"' isSubForm='"+isSubForm+"' table='"+tableId+"' column='"+columnId+"'></formProperty>";
	}else if(className == "gridviewcontrol"){
		str += "<formProperty  class= '"+className+"' gridName='"+gridName+"' gridTitle='"+gridTitle+"' isSubForm='"+isSubForm+"' height='"+height+"' width='"+width+"'></formProperty>";
	}	
	return str;
}

function setHTMLSource(){
	var formName = $("#formName").val();
	var tableName=$("#tableName").val();
	var tableId = $("#tableId").val();
	var editor_data = CKEDITOR.instances.editor1.getData();
	/*
	var start_form_tag="";
	if(formName != ""){
		start_form_tag='<form name="'+formName+'" id="'+formName+'" tableName="'+tableName+'" tableId= "'+tableId+'" method="post"> ';
	}else{
		start_form_tag='<form name=""> ';
	}
	var end_form_tag='</form>';
	var htmlSourceText = "";
	var formExist = editor_data.indexOf("</form>");
	if(formExist != -1){
		htmlSourceText=editor_data;				
	}else{
		htmlSourceText=start_form_tag+editor_data+end_form_tag;		
	}*/
	htmlSourceText = editor_data;		
	$("#htmlsource").text(htmlSourceText);
}

function setCKEditorSource(mode){
	var htmlsource = $("#htmlsource").text();
	CKEDITOR.instances['editor1'].destroy();
	renderCKEditor();
	CKEDITOR.instances.editor1.setData(htmlsource);
	var tabContent = document.getElementById('tab-content-1');
    tabContent.style.display = 'block';
    document.getElementById("paletteMainContainer").style.width="85%";
	document.getElementById("paletteList").style.display="block";
	document.getElementById("hiddedPalette").style.display="none";
	var height = $("#target").height();
	var ckEditor = $("#ckEditor").children().children().children('.cke_contents');
	ckEditor.css('height',parseInt(height)-295);
	$("#ckEditor").css('height',parseInt(height)-180);
    $(this).addClass('selected');
    $(this).siblings().removeClass('selected');	
    $(tabContent).siblings().css('display','none');
	$(".connectedSortable li").removeClass("selected");
	$("#tabs-2").removeClass("selected");
	$("#tabs-1").addClass("selected");
	var returnType = getCkEditorElem(mode);
	return returnType;
}

function destroyCKEditorForTemplate(formTag, html,formStyle){
	
	var height = $("#target").height();
	CKEDITOR.dialog.getCurrent().hide();
	CKEDITOR.instances['editor1'].destroy();
	renderCKEditor();
	
	var currentFormStyle="";
	if(formStyle!=undefined && formStyle!=null){
		currentFormStyle=formStyle;
	}else{
		currentFormStyle=$(formTag).attr("style");
	}
	
	var htmlContent = "<form action='"+$(formTag).attr("action")+"' id='"+$(formTag).attr("id")+"'  method='"+$(formTag).attr("method")+"' name='"+$(formTag).attr("name")+"' style='"+currentFormStyle+"' table='"+$(formTag).attr("table")+"' ";
	        if($(formTag).attr("enctype")) {
	        	htmlContent = htmlContent + " enctype='"+$(formTag).attr("enctype")+"'";
	        }	
			if($(formTag).attr("onsubmit")) {
				htmlContent = htmlContent + " onsubmit='"+$(formTag).attr("onsubmit")+"'";
		    }
			if($(formTag).attr("onunload")) {
				 htmlContent = htmlContent + " onunload='"+$(formTag).attr("onunload")+"'";
		    }
			if($(formTag).attr("onload")) {
				 htmlContent = htmlContent + " onload='"+$(formTag).attr("onload")+"'";
		    }			
			htmlContent = htmlContent + ">" + html + "</form>";
	CKEDITOR.instances.editor1.setData( htmlContent, function() {
		CKEDITOR.instances.editor1.focus();
		var ckEditor = $("#ckEditor").children().children().children('.cke_contents');
		ckEditor.css('height',parseInt(height)-295);
		$("#ckEditor").css('height',parseInt(height)-180);
	});
}

function setPreviewSource(){
	var formName = $("#formName").val();
	var tableName=$("#tableName").val();
	var tableId = $("#tableId").val();
	/*var start_form_tag="";
	var editor_data = CKEDITOR.instances.editor1.getData();
	if(formName != ""){
		start_form_tag='<form name="'+formName+'" id="'+formName+'" tableName="'+tableName+'" tableId= "'+tableId+'"> ';
	}else{
		start_form_tag='<form name="">';
	}
	var end_form_tag='</form>';
	
	var htmlSourceText=start_form_tag+editor_data+end_form_tag;*/
	var editor_data = CKEDITOR.instances.editor1.getData();
	var htmlSourceText = editor_data;
	
	$("#previewdst").html( htmlSourceText );
	
	$("#preview_form").html("");	
	var print_modal = $("#preview_form");
	var print_frame = $('<iframe id="print-modal-content" scrolling="no" border="0" frameborder="0" name="print-frame" style="margin:0;overflow: auto;"/><script>$(document).ready(function(){	$("input[type=submit]").each(function(){  /* $(this).attr("disabled","disabled");  */});	 });</script>');
	print_modal.append(print_frame).appendTo('#tab-content-3');
	var print_frame_ref = document.getElementById('print-modal-content').contentDocument;
	print_frame_ref.open();
	print_frame_ref.write('<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">' +
	     '<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">' + 
	     '<head><title>' + document.title + '</title></head>' +
	     '<body style="background:#FFFFFF;"></body>' +
	     '</html>');
	print_frame_ref.close(); 
	
	var $iframe_head = $('head link[media*=print], head link[media=all]').clone();
	var $iframe_body = htmlSourceText;//$('#previewdst > *:not(#print-modal):not(script)').clone();
	
	$iframe_head.each(function() {
        $(this).attr('media', 'all');
    });
    if (!$.browser.msie && !($.browser.version < 7) ) {
        $('head', print_frame_ref).append($iframe_head);
        $('body', print_frame_ref).append($iframe_body);
    }
    else {
        $('body > *:not(#print-modal):not(script)').clone().each(function() {
            $('body', print_frame_ref).append(this.outerHTML);
        });
        $('head link[media*=print], head link[media=all]').each(function() {
            $('head', print_frame_ref).append($(this).clone().attr('media', 'all')[0].outerHTML);
        });
    }
    
    loadScriptFileInCKEditorIframe('print-modal-content');
}

function setHtmlInEditor(){
	var htmlSource = document.getElementById("htmlSource").value;
	CKEDITOR.instances.editor1.setData( htmlSource, function() {
		CKEDITOR.instances.editor1.focus();
		var height = $("#target").height();
		var ckEditor = $("#ckEditor").children().children().children('.cke_contents');
		ckEditor.css('height',parseInt(height)-280);
		$("#ckEditor").css('height',parseInt(height)-160);
		if(htmlSource.indexOf("changeWizardByTabForDynamicForm()") > -1){
			var ckEditorIframe = $('#ckEditor').find("iframe")[0];
			$(ckEditorIframe).attr('id','ckEditor_iframe');
			loadScriptFileInCKEditorIframe('ckEditor_iframe');
		}
		
		var is_chrome = navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
		if(is_chrome) {
			CKEDITOR.instances.editor1.document.getById('form_div').removeAttribute("contenteditable");
			CKEDITOR.instances.editor1.document.getById('form_div').setAttribute("contenteditable","true");	
		} else {
			CKEDITOR.instances.editor1.document.getBody().removeAttribute("contenteditable");
			CKEDITOR.instances.editor1.document.getById('form_div').setAttribute("contenteditable","true");	
		}
		
		
	});
}

function setEditModeHTMLSource(){
	var editor_data = CKEDITOR.instances.editor1.getData();
	$("#htmlsource").text(editor_data);
}
 
function setEditModePreviewSource(){
var formName = $("#formName").val();
	var editor_data = CKEDITOR.instances.editor1.getData();

	$("#previewdst").html( editor_data );
}

/*function fullScreenMode(){
	$('#toggle-fullscreen-div').on('click', function(){
		activateFullScreenMode('target');
		setTimeout(function(){
			var height = $("#target").height();
			$("#htmlsource").css('height',parseInt(height)-110);
	    	$("#previewdst").css('height',parseInt(height)-105);
	    	$("#ckEditor").css('height',parseInt(height)-180);
	    	document.getElementById("paletteList").style.height = height-6+"px";
	    	var ckEditor = $("#ckEditor").children().children().children('.cke_contents');
	    	ckEditor.css('height',parseInt(height)-295);
		},400);
	});
}*/

function fullScreenMode(){
	$('#toggle-fullscreen-div').on('click', function(){
		toggleFulscreenMode('top_btn');
	});
}

/**
 * Toggle fullscreen function for Form builder
 * @param clickOn, get click event from image button or when save or update the form
 */
function toggleFulscreenMode(clickOn){
	setTimeout(function(){
		if($('#header_menu').css('display') == 'none' || clickOn == 'back_btn'){
				$("#header_menu").show();
				$("#top_menu").show();
				$("#side_menu").show();
				//$("#ajax_loader").width("85.1%");
				$("#target").height($("#target").height()-10);
				$(".dashboard-wrapper").css('margin-left',190);
				$(".dashboard-wrapper").css('margin-bottom',10);
				var height = $("#target").height();
		    	$("#create_form").css('height',parseInt(height)- 130);
		    	$("#palette_block").css('height',parseInt(height)-130);
		}else if($('#header_menu').css('display') == 'block'){
			if(clickOn == "top_btn"){
				$("#header_menu").hide();
				$("#top_menu").hide();
				$("#side_menu").hide();
				//$("#ajax_loader").width("100%");
				$("#target").height($("#target").height()+100);
				var height = $("#target").height();
		    	$("#create_form").css('height',parseInt(height)- 130);
		    	$("#palette_block").css('height',parseInt(height)-130);
		    	$(".dashboard-wrapper").css('margin',0)
			}
		}	
		var height = $("#target").height();
		$("#htmlsource").css('height',parseInt(height)-110);
    	$("#previewdst").css('height',parseInt(height)-105);
    	$("#ckEditor").css('height',parseInt(height)-180);
//    	if(document.getElementById("paletteList") != null){
//    		document.getElementById("paletteList").style.height = height-5+"px";
//    	}
    	var ckEditor = $("#ckEditor").children().children().children('.cke_contents');
    	ckEditor.css('height',parseInt(height)-295);
	},400);
}

function setHeightPalette(){
	var height = $("#target").height();
	document.getElementById("paletteList").style.height = height-5+"px";
}
/*--------------------------create/edit form script end ----------------------------------*/


/*-------------------------- To uplad form image -----------------------------------------*/
function _uploadFormBGImage(){
	var fileExtension = ['jpeg', 'jpg', 'png', 'gif', 'bmp'];
	var uploadImageName=$("#formUploadImage").val();
	var contentResponse="";
	var isSuccess=false;
	if(uploadImageName!=""){
		if ($.inArray(uploadImageName.split('.').pop().toLowerCase(), fileExtension) == -1) {
			$.msgBox({
				title: form.title.message,
				content: "Only '.jpeg','.jpg', '.png', '.gif', '.bmp' formats are allowed.",
			    type: false,
			    afterClose : function(){
			    	return false;
			    }
			});
			return false;
		}else{
			$("#formImageUploadForm").ajaxSubmit(function(response){ 
        		if(response.success){
					contentResponse=response.success;
					loginUser=response.loginUser;
					uploadImageName=response.fileName;
					isSuccess=true;
					formImageUploder(contentResponse,isSuccess,uploadImageName,loginUser);
					return true;
				}else{
					return false;
				}
        	});
			
		}
	}else{
		return false;
	}
	
}

function formImageUploder(contentResponse,isSuccess,uploadImageName){
	if(isSuccess){
		$.msgBox({
			title: form.title.success,
			content: contentResponse,
		        type: form.title.msgBoxTitlesuccess,
		        afterClose:function (){
		        	formUserLogin=loginUser;
		        	formImage=uploadImageName;
		        	var imageUploaded = "/resources/"+formUserLogin+"/"+formImage;
		        	var currentDialog=CKEDITOR.dialog.getCurrent();
			    	var currentStyle=currentDialog.getValueOf('info','imagePath');
			    	currentDialog.setValueOf('info','imagePath', imageUploaded);
		        	/*var currentDialog=CKEDITOR.dialog.getCurrent();
		        	var currentStyle=currentDialog.getValueOf('info','style');
		        	var imageUploaded="/resources/"+loginUser+"/"+uploadImageName;
		        	currentStyle=currentStyle+";background-image:url('"+imageUploaded+"');"
		        	currentDialog.setValueOf('info','style',currentStyle);*/
	        }
		});
	}else{
		$.msgBox({
			title: form.title.error,
			content: contentResponse,
		        type: form.title.msgBoxTitleerror,
		});
	}
}


function getAllDataDictionaries(element_id) {
	
	$.ajax({
		url: 'bpm/admin/getAllDictionariesAsLabelValue',
	    type: 'GET',
	    dataType: 'json',
		async: false,
		success: function(data) {
			$( element_id ).empty();
			if(data){
	            $.each(data, function(index, item) {
	            	
	                $(element_id).get(0).options[$(element_id).get(0).options.length] = new Option(item.dictCode, item.dictId);
	            });
	            $(element_id).prepend("<option value='' selected='selected'></option>");
			}
        }
    });
}

function addOptionValToDictionary(element_id,parent_dictId,columnId,pastValues,isFormReadOnly) {
	$.ajax({
		url: 'bpm/admin/getDictValueByParentId',
	    type: 'GET',
	    data:"parentId="+parent_dictId,
	    dataType: 'json',
		async: false,
		success: function(data) {
			$( "#"+element_id ).empty();
			if(data){
	            $.each(data, function(index, item) {
	                $("#"+element_id).get(0).options[$("#"+element_id).get(0).options.length] = new Option(item.dictValue, item.dictId);
	            });
	            $("#"+element_id).prepend("<option value='' selected='selected'></option>");
			}
	    }
	});
	if(pastValues){
		if(isFormReadOnly == "true"){
			//$('<span style="font-size: 14px;padding: 4px 6px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;height: 30px;align:left;"><b>&nbsp;&nbsp;&nbsp;'+pastValues[columnId]+'</b></span>').insertAfter( "#"+element_id );
			$("#"+element_id).val(pastValues[columnId]);
		}else{
			if ($('option[value="'+pastValues[columnId]+'"]', '#'+element_id).length) {
				$("#"+element_id).val(pastValues[columnId]);
			} else if($('option[value="' +pastValues[columnId+"_"+element_id]+ '"]', '#'+element_id).length) {
				$("#"+element_id).val(pastValues[columnId+"_"+element_id]);
			}
		}
	}
}

function radioButtonForDataDictionary(element_id,parent_dictId,columnId,pastValues,isFormReadOnly) {
	var elementId=$( "#"+element_id ).attr('id');
	var top = $("#"+elementId).css("top");
	var topPx = top.split('px')[0];
	var left = $("#"+elementId).css("left");
	var leftPx = left.split('px')[0];
	var temp = 0;
	var parent = document.getElementById(element_id);
	var defaultSize = 15;
	var fontSize = 0;
	if((parent.parentNode != undefined &&  parent.parentNode.nodeName == "SPAN") || (parent.parentNode.parentNode != undefined && parent.parentNode.parentNode.nodeName == "SPAN")){
		if(parent.parentNode != undefined &&  parent.parentNode.nodeName == "SPAN"){
			fontSize = parent.parentNode.style.fontSize.split('px')[0];
			if(fontSize == "" && parent.parentNode.parentNode != undefined && parent.parentNode.parentNode.nodeName == "SPAN"){
			fontSize = parent.parentNode.parentNode.style.fontSize.split('px')[0];
			}
		} else if(parent.parentNode.parentNode != undefined && parent.parentNode.parentNode.nodeName == "SPAN"){
			fontSize = parent.parentNode.parentNode.style.fontSize.split('px')[0];
			if(fontSize == "" && parent.parentNode != undefined && parent.parentNode.nodeName == "SPAN"){
			fontSize = parent.parentNode.parentNode.style.fontSize.split('px')[0];
			}
		}
	}
	var dynamicLeftPosition = top.split('px')[0];
	$.ajax({
		url: 'bpm/admin/getDictValueByParentId',
	    type: 'GET',
	    data:"parentId="+parent_dictId,
	    dataType: 'json',
		async: false,
		success: function(data) {
			if(data){
				 $.each(data, function(index, item) {
					 //var elementId=$( "#"+element_id ).attr('id');
					// var top = $("#"+elementId).css("top");
					// var topPx = top.split('px');
					// var left = $("#"+elementId).css("left");
					// var leftPx = left.split('px')[0];
					// var spanLeft = +leftPx + +item.dictValue.length;
						 if(isFormReadOnly!="true"){
							if(fontSize > 0){
								dynamicLeftPosition = parseInt(top.split('px')[0])+(fontSize/2);
								$( '<input type="radio" '+
						    		   'table="'+$( "#"+element_id ).attr('table')+'" '+
					  	            	   'optiontype="'+$( "#"+element_id ).attr('optiontype')+'" '+
					  	            	   'name="'+$( "#"+element_id ).attr('name')+'" '+
					  	            	   'id="'+$( "#"+element_id ).attr('id')+'_'+item.dictId+'" '+ 
					  	            	   'datadictionarylabel="'+$( "#"+element_id ).attr('datadictionarylabel')+'" '+
					  	            	   'datadictionary="'+$( "#"+element_id ).attr('datadictionary')+'" '+
					  	            	   'column= "'+$( "#"+element_id ).attr('column')+'"  '+
					  	            	   'isRequired="'+$( "#"+element_id ).attr('isrequired')+'" '+
					  	            	   'class="data_dictionary_radio" '+
					      	            	   'style = "top:' +dynamicLeftPosition+'px;left:'+leftPx+'px;position:absolute'+'" '+
					      	           	   'value="'+item.dictId+'"> <span style=top:'+top+';left:'+(parseInt(leftPx)+15)+'px;position:absolute>'+item.dictValue+'</span></input>').insertAfter("#"+element_id);
								if(fontSize <= 20){
							      	       leftPx = parseInt(leftPx)+fontSize*3;
								} else if(fontSize <=30){
									leftPx = parseInt(leftPx)+fontSize*5;
								} else if(fontSize <=45){
									leftPx = parseInt(leftPx)+fontSize*5;
								} else if(fontSize <=60){
									leftPx = parseInt(leftPx)+fontSize*7;
								} else if(fontSize <=70){
									leftPx = parseInt(leftPx)+fontSize*8;
								} else if(fontSize <=80){
									leftPx = parseInt(leftPx)+fontSize*10;
								}
						      	       if(item.dictValue.length <= 10){
					      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*4)+20;
					      	            	} else if(item.dictValue.length > 10 && item.dictValue.length <= 20) {
					      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*6)+20;
					      	            	} else if(item.dictValue.length > 20 &&item.dictValue.length <= 30) {
					      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*6)+20;
					      	            	} else if(item.dictValue.length > 30 &&item.dictValue.length <= 40) {
					      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*7)+20;
					      	            	} else if(item.dictValue.length > 40 &&item.dictValue.length <= 50) {
					      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*8)+20;
					      	            	} else {
					      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*10)+20;
					      	            	}
							} else {
								$( '<input type="radio" '+
						    		   'table="'+$( "#"+element_id ).attr('table')+'" '+
					  	            	   'optiontype="'+$( "#"+element_id ).attr('optiontype')+'" '+
					  	            	   'name="'+$( "#"+element_id ).attr('name')+'" '+
					  	            	   'id="'+$( "#"+element_id ).attr('id')+'_'+item.dictId+'" '+ 
					  	            	   'datadictionarylabel="'+$( "#"+element_id ).attr('datadictionarylabel')+'" '+
					  	            	   'datadictionary="'+$( "#"+element_id ).attr('datadictionary')+'" '+
					  	            	   'column= "'+$( "#"+element_id ).attr('column')+'"  '+
					  	            	   'isRequired="'+$( "#"+element_id ).attr('isrequired')+'" '+
					  	            	   'class="data_dictionary_radio" '+
					      	            	   'style = "top:' +dynamicLeftPosition+'px;left:'+leftPx+'px;position:absolute'+'" '+
					      	           	   'value="'+item.dictId+'"> <span style=top:'+top+';left:'+(parseInt(leftPx)+15)+'px;position:absolute>'+item.dictValue+'</span></input>').insertAfter("#"+element_id);
							      	       leftPx = parseInt(leftPx)+defaultSize;
						      	       if(item.dictValue.length <= 10){
					      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*4)+20;
					      	            	} else if(item.dictValue.length > 10 && item.dictValue.length <= 20) {
					      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*6)+20;
					      	            	} else if(item.dictValue.length > 20 &&item.dictValue.length <= 30) {
					      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*6)+20;
					      	            	} else if(item.dictValue.length > 30 &&item.dictValue.length <= 40) {
					      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*7)+20;
					      	            	} else if(item.dictValue.length > 40 &&item.dictValue.length <= 50) {
					      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*8)+20;
					      	            	} else {
					      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*10)+20;
					      	            	}
							}
							 
				  	               /* var element = document.createElement("input");
								element.setAttribute('type', 'radio');
								element.setAttribute('optionType',$( "#"+element_id ).attr('optiontype'));
								element.setAttribute('value', item.dictId);
								element.setAttribute('name', $( "#"+element_id ).attr('name')); 
								element.setAttribute('id', $( "#"+element_id ).attr('id')+'_'+item.dictId);
								element.setAttribute('column',$( "#"+element_id ).attr('column'));
								element.setAttribute('class',"data_dictionary_radio");
								element.setAttribute('datadictionary',$( "#"+element_id ).attr('datadictionary'));
								element.setAttribute('datadictionarylabel',$( "#"+element_id ).attr('datadictionarylabel'));
								element.setAttribute('table',$( "#"+element_id ).attr('table'));
								element.setAttribute('isRequired',$( "#"+element_id ).attr('isrequired'));
								var currentElement = document.getElementById( elementId);
								var parent = currentElement.parentNode;
								parent.insertBefore(element, currentElement.nextSibling);
								var spanElement = document.createElement("span"); 
								spanElement.innerHTML = item.dictValue;
								var spanParentElement = element.parentNode;
								spanParentElement.insertBefore(spanElement, element.nextSibling);*/
						 }

				if(isFormReadOnly == "true"){	
	            	 		if(pastValues[columnId] != undefined){
	            	    	 	if(item.dictId == pastValues[columnId]){
	            	    	 	   	 	$('<span style="font-size: 14px;padding: 4px 6px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;height: 30px;align:left;"><b>&nbsp;&nbsp;&nbsp;'+item.dictValue+'</b></span>').insertAfter( "#"+element_id );
				 	}
				 	}else{
				 		$('<span style="font-size: 14px;padding: 4px 6px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;height: 30px;align:left;"><b>&nbsp;&nbsp;&nbsp;'+item.dictValue+'</b></span>').insertAfter( "#"+element_id );
				 	}
	            	 	}
		            	 var tempElementId=elementId+'_'+item.dictId;
		            	if(pastValues){
			            	if(item.dictId == pastValues[columnId]){
			            		if(isFormReadOnly=="true"){
			            			$(document.getElementById(tempElementId)).replaceWith("<span style='font-size: 14px;padding: 4px 6px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;height: 30px;align:left;'><b>&nbsp;&nbsp;&nbsp;"+item.dictValue+"</b></span>" );
				            	}else{
				            		$("#"+$( "#"+element_id ).attr('id')+'_'+item.dictId).prop("checked", true);
				            	}
			            	}
		            	}
		            });
		            $( "#"+element_id ).remove();
			}
	    }
	});
}

function checkboxForDataDictionary(element_id,parent_dictId,columnId,pastValues,isFormReadOnly) {
	 var elementId=$( "#"+element_id ).attr('id');
	 var top = $("#"+elementId).css("top");
	 var topPx = top.split('px');
	 var left = $("#"+elementId).css("left");
	 var leftPx = left.split('px')[0];
	 var temp = 0;
	var parent = document.getElementById(element_id);
	var defaultSize = 15;
	var fontSize = 0;
	if((parent.parentNode != undefined &&  parent.parentNode.nodeName == "SPAN") || (parent.parentNode.parentNode != undefined && parent.parentNode.parentNode.nodeName == "SPAN")){
		if(parent.parentNode != undefined &&  parent.parentNode.nodeName == "SPAN"){
			fontSize = parent.parentNode.style.fontSize.split('px')[0];
			if(fontSize == "" && parent.parentNode.parentNode != undefined && parent.parentNode.parentNode.nodeName == "SPAN"){
			fontSize = parent.parentNode.parentNode.style.fontSize.split('px')[0];
			}
		} else if(parent.parentNode.parentNode != undefined && parent.parentNode.parentNode.nodeName == "SPAN"){
			fontSize = parent.parentNode.parentNode.style.fontSize.split('px')[0];
			if(fontSize == "" && parent.parentNode != undefined && parent.parentNode.nodeName == "SPAN"){
			fontSize = parent.parentNode.parentNode.style.fontSize.split('px')[0];
			}
		}
	}
	var dynamicLeftPosition = top.split('px')[0];
	$.ajax({
		url: 'bpm/admin/getDictValueByParentId',
	    type: 'GET',
	    data:"parentId="+parent_dictId,
	    dataType: 'json',
		async: false,
		success: function(data) {
			if(data){
	            $.each(data, function(index, item) {
	            	// var elementId=$( "#"+element_id ).attr('id');
					
					 //var spanLeft = +leftPx + +item.dictValue.length;
	            	 if(isFormReadOnly!="true"){
	            	 if(fontSize > 0){
				dynamicLeftPosition = parseInt(top.split('px')[0])+(fontSize/2);
	            		 $( "#"+element_id).first().before( '<input type="checkbox" '+
	      	            	   'table="'+$( "#"+element_id ).attr('table')+'" '+
	      	            	   'optiontype="'+$( "#"+element_id ).attr('optiontype')+'" '+
	      	            	   'name="'+$( "#"+element_id ).attr('name')+'" '+
	      	            	   'id="'+$( "#"+element_id ).attr('id')+'_'+item.dictId+'" '+ 
	      	            	   'datadictionarylabel="'+$( "#"+element_id ).attr('datadictionarylabel')+'" '+
	      	            	   'datadictionary="'+$( "#"+element_id ).attr('datadictionary')+'" '+
	      	            	   'column="'+$( "#"+element_id ).attr('column')+'" '+
	      	            	 'isRequired="'+$( "#"+element_id ).attr('isrequired')+'" '+
	      	            	   'class="data_dictionary_checkbox" '+
	      	            	 'style = "top:' +dynamicLeftPosition+'px;left:'+leftPx+'px;position:absolute'+'" '+
			      	            'value="'+item.dictId+'"> <span style=top:'+top+';left:'+(parseInt(leftPx)+15)+'px;position:absolute>'+item.dictValue+'</span></input>' );
						if(fontSize <= 20){
							      	       leftPx = parseInt(leftPx)+fontSize*3;
								} else if(fontSize <=30){
									leftPx = parseInt(leftPx)+fontSize*5;
								} else if(fontSize <=45){
									leftPx = parseInt(leftPx)+fontSize*5;
								} else if(fontSize <=60){
									leftPx = parseInt(leftPx)+fontSize*7;
								} else if(fontSize <=70){
									leftPx = parseInt(leftPx)+fontSize*8;
								} else if(fontSize <=80){
									leftPx = parseInt(leftPx)+fontSize*10;
								}
			      	            	if(item.dictValue.length <= 10){
			      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*4)+20;
			      	            	} else if(item.dictValue.length <= 20) {
			      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*6)+20;
			      	            	} else if(item.dictValue.length <= 30) {
			      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*6)+20;
			      	            	} else if(item.dictValue.length <= 40) {
			      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*7)+20;
			      	            	} else if(item.dictValue.length <= 50) {
			      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*8)+20;
			      	            	} else {
			      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*10)+20;
			      	            	}
			
			     	             // $( "#"+element_id ).parent().attr("style",'top:' +top+';left:'+left+';position:absolute');
	      	            	/*var element = document.createElement("input");
				element.setAttribute('type', 'checkbox');
				element.setAttribute('optionType',$( "#"+element_id ).attr('optiontype'));
				element.setAttribute('value', item.dictId);
				element.setAttribute('name', $( "#"+element_id ).attr('name')); 
				element.setAttribute('id', $( "#"+element_id ).attr('id')+'_'+item.dictId);
				element.setAttribute('column',$( "#"+element_id ).attr('column'));
				element.setAttribute('class',"data_dictionary_checkbox");
				element.setAttribute('datadictionary',$( "#"+element_id ).attr('datadictionary'));
				element.setAttribute('datadictionarylabel',$( "#"+element_id ).attr('datadictionarylabel'));
				element.setAttribute('table',$( "#"+element_id ).attr('table'));
				element.setAttribute('isRequired',$( "#"+element_id ).attr('isrequired'));
				var currentElement = document.getElementById( elementId);
				var parent = currentElement.parentNode;
				parent.insertBefore(element, currentElement.nextSibling);
				var spanElement = document.createElement("span"); 
				spanElement.innerHTML = item.dictValue;
				var spanParentElement = element.parentNode;
				spanParentElement.insertBefore(spanElement, element.nextSibling);
				$(element).insertAfter( "#"+element_id );*/
	            	 } else {
				$( "#"+element_id).first().before( '<input type="checkbox" '+
	      	            	   'table="'+$( "#"+element_id ).attr('table')+'" '+
	      	            	   'optiontype="'+$( "#"+element_id ).attr('optiontype')+'" '+
	      	            	   'name="'+$( "#"+element_id ).attr('name')+'" '+
	      	            	   'id="'+$( "#"+element_id ).attr('id')+'_'+item.dictId+'" '+ 
	      	            	   'datadictionarylabel="'+$( "#"+element_id ).attr('datadictionarylabel')+'" '+
	      	            	   'datadictionary="'+$( "#"+element_id ).attr('datadictionary')+'" '+
	      	            	   'column="'+$( "#"+element_id ).attr('column')+'" '+
	      	            	 'isRequired="'+$( "#"+element_id ).attr('isrequired')+'" '+
	      	            	   'class="data_dictionary_checkbox" '+
	      	            	 'style = "top:' +dynamicLeftPosition+'px;left:'+leftPx+'px;position:absolute'+'" '+
			      	            'value="'+item.dictId+'"> <span style=top:'+top+';left:'+(parseInt(leftPx)+15)+'px;position:absolute>'+item.dictValue+'</span></input>' );
						leftPx = parseInt(leftPx)+defaultSize;
			      	            	if(item.dictValue.length <= 10){
			      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*4)+20;
			      	            	} else if(item.dictValue.length <= 20) {
			      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*6)+20;
			      	            	} else if(item.dictValue.length <= 30) {
			      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*6)+20;
			      	            	} else if(item.dictValue.length <= 40) {
			      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*7)+20;
			      	            	} else if(item.dictValue.length <= 50) {
			      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*8)+20;
			      	            	} else {
			      	            		leftPx = parseInt(leftPx)+(item.dictValue.length*10)+20;
			      	            	}
			}
		}
            	if(isFormReadOnly == "true"){	
       			if(pastValues[columnId] != undefined){
	            	 	var pastCheckedValues = pastValues[columnId].split(",");
			    	for(var i=0;i<pastCheckedValues.length;i++){
				    	 if(item.dictId == pastCheckedValues[i]){
				    	 	$('<span style="font-size: 14px;padding: 4px 6px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;height: 30px;align:left;"><b>&nbsp;&nbsp;&nbsp;'+item.dictValue+'</b></span>').insertAfter( "#"+element_id );
				    	 	
				    	 }
			    	 }
			    	}else{
						$('<span style="font-size: 14px;padding: 4px 6px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;height: 30px;align:left;"><b>&nbsp;&nbsp;&nbsp;'+item.dictValue+'</b></span>').insertAfter( "#"+element_id );
				}
	            	}
	            	 var tempElementId=elementId+'_'+item.dictId;
	            	 
	            	if(pastValues){
		            	if(item.dictId == pastValues[columnId]){
		            		if(isFormReadOnly=="true"){
		            			$(document.getElementById(tempElementId)).replaceWith("<span style='font-size: 14px;padding: 4px 6px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;height: 30px;align:left;'><b>&nbsp;&nbsp;&nbsp;"+item.dictValue+"</b></span>" );
			            	}else{
			            		$("#"+$( "#"+element_id ).attr('id')+'_'+item.dictId).prop("checked", true);
			            	}
		            	}
	            	}
	            	
	            });
	            $( "#"+element_id ).remove();
			}
	    }
	});
}

function getAllListView(element_id){
	 $.ajax({
        type: 'GET',
        url: '/bpm/listView/getAllListView',
        dataType: 'json',
        async: false,
        success: function(data) {
        	$( element_id ).empty();
        	if(data){
	             $.each(data, function(index, item) {
	             	$(element_id).get(0).options[$(element_id).get(0).options.length] = new Option(item.listViewName, item.listViewName);
	             });
	             $(element_id).prepend("<option value='' selected='selected'></option>");
        	}
        }
    });
}

function selectIconForBtn(iconPath){
	var currentDialog=CKEDITOR.dialog.getCurrent();
	var currentStyle=currentDialog.getValueOf('info','iconPath');
	currentDialog.setValueOf('info','iconPath', iconPath);
	$("#popupDialog").dialog("close");
}

//method to show organization tree selection in popup for select user
function showDataDictionaryListTree(title, selectionType, appendTo, selectedValues, callAfter, hiddenElementId){
	//clearPreviousPopup(); 
	$myDialog = $("#userPopupDialog");
	$myDialog.dialog({
                       width: 'auto',
                       height: 'auto',
                       top: '10px',
                       modal: true,
                       zIndex: 1000200,
                       stack: false,
                       position: [($(window).width() / 3), 150],
                       title: "Select " +selectionType.charAt(0).toUpperCase() + selectionType.slice(1) + " " + title
	             });
	 $myDialog.dialog("open");
	 document.location.href = "#bpm/admin/getAllDictionariesAsLabelValue";
	 _execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&selectedValues='+selectedValues+'&rootNodeURL=bpm/admin/getDataDictionaryNodes&childNodeURL=bpm/admin/getDictionaryChildNodes&callAfter='+callAfter+'&hiddenElementId='+hiddenElementId);
}

function addDataDictionarySelectedOption(appendTo,callAfter,hiddenElementId){
	var selectedValues = [];
	var selectedValueId = [];
	$("#organizationTreeRight span").each(function(index, elem){
		selectedValues.push($(this).text());
		selectedValueId.push(elem.id);
	});
	$('#'+appendTo).val(selectedValues);
	$('#'+hiddenElementId).val(selectedValueId);
	closeSelectDialog('userPopupDialog');
	if(callAfter != null && callAfter != ''){
		window[callAfter](selectedValues);
	}
	_execute('target','');
}

//For rendering values for auto complete fields in from
function autoCompleteForDataDictionary(element_id,parent_dictId) {
	var dataList = [];
	$.ajax({
		url: 'bpm/admin/getDictValueByParentId',
	    type: 'GET',
	    data:"parentId="+parent_dictId,
	    dataType: 'json',
		async: false,
		success : function(data) {
			$.each(data, function(index, item) {
				dataList.push(item.dictValue);
            });
		}
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

function getAutocompleteData(element_id,parent_dictId) {
	var dataList = $("#searchData");
	dataList.empty();

	$.ajax({
		url: 'bpm/admin/getDictValueByParentId',
	    type: 'GET',
	    data:"parentId="+parent_dictId,
	    dataType: 'json',
		async: false,
		success : function(data) {
			var selectOption = document.createElement("OPTION");
			$.each(data, function(index, item) {
				//dataList.empty();
				dataList.push(item.dictValue);
					selectOption.text = item.dictValue;
					selectOption.value = item.dictValue;
					selectOption.id = item.dictId;
					dataList.append(selectOption);
			});
				dataList.append('<option value=""></option>');	
		}
	});
}

function clearFormData(){
	formImage="";
}

function loadScriptFileInCKEditorIframe(iFrameId){
	
	var previewIframe = document.getElementById(iFrameId);
	if(previewIframe != undefined && previewIframe != null){
	var element = previewIframe.contentDocument.createElement('script');  
	element.setAttribute('src', '/scripts/lib/jquery-1.7.2.min.js');
	previewIframe.contentWindow.document.getElementsByTagName('head')[0].appendChild(element);
	  
	var element = previewIframe.contentDocument.createElement('script');  
	element.setAttribute('src', '/scripts/easybpm/jqueryUI/jquery-ui-1.8.22.js');  
	previewIframe.contentWindow.document.getElementsByTagName('head')[0].appendChild(element); 
	    
	var element = previewIframe.contentDocument.createElement('script');  
	element.setAttribute('src', '/scripts/easybpm/ckeditor-iframe/ckeditor-iframe.js');  
	previewIframe.contentWindow.document.getElementsByTagName('head')[0].appendChild(element); 
	}
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
}

function copyForms(){
	var selectedRowId =  gridObj.jqGrid ('getGridParam', 'selarrrow');
	var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
	var moduleId=selected_nodes.attr("moduleid");
	if(selectedRowId!=""){
		if(selectedRowId.length>1){
			validateMessageBox(form.title.validation, form.title.copyFormError  , false);
		}else{
			$.msgBox({
				title : form.title.copyForm,
				type : form.title.copyFormType,
				inputs : [ {
					header : "Module Name ",
					id: "_copyModuleName",
					type : "text",
					name : "_copyTableToModule",
					autocomplete : "true",
					datalistId : "searchModuleresults",
					onblur : "setCopyModuleId('"+moduleId+"')"
				},{
					header : "",
					type : "hidden",
					name : "_copyModuleId"
				},
					{ header: form.title.formname+"&nbsp;&nbsp;&nbsp;", type: "text", name: "_copyFormName" }
				],
				buttons : [ {
					value : "Yes"
						
				}, {
					value : "Cancel"
				} ],
				success : function(result, values) {
					if (result == "Yes") {
						var selectedModuleId=values[1].value.trim();
						var newFormName=values[2].value.trim();
						if(selectedModuleId!=""){
							if(newFormName==""){
								setTimeout(function() {
									openModuleMessage(form.title.validation, form.msg.mandatoryName , false,false,moduleId);
								}, 500);
							}else if(checkCopyFormName(newFormName,moduleId)){
									var param="moduleId="+selectedModuleId+"&newFormName="+newFormName+"&formIds="+JSON.stringify(selectedRowId);
									//console.log("param  "+param);
									document.location.href = "#bpm/form/copyFormsToModule";
									_execute('target',param);
							}
						}else{
							setTimeout(function() {
								openModuleMessage(form.title.validation, form.msg.moduleCopy , false,false,moduleId);
							}, 500);
						}
					}
				}
			});
		}
	}else{
		validateMessageBox(form.title.validation, form.msg.copyErrorSelect , false);
	}
	
	if (document.getElementsByName("_copyTableToModule")) {
		setModuleNamesList("_copyModuleName","?isPrivilegeNeeded=true");
	}
}

function checkCopyFormName(newFormName,moduleId){
	var status=false;
	if(!(newFormName.indexOf(' ') > -1)){
		if(/^[\-a-zA-Z\u4e00-\u9eff\s][\-a-zA-Z\u4e00-\u9eff\s0-9]*$/.test(newFormName) !=false){
			
			var newdata = $.ajax({
				type: "GET",
				async:false,
				url: "bpm/form/isFormNamePresent?formName="+newFormName
			}).responseText;
		
			var da = eval('('+newdata+')');
			if(da.isPresent == true){
				setTimeout(function() {
					openMessageCopyForm(form.error.formexists,form.msg.differentformname,false, moduleId);
				}, 500);
			}else{
				status=true;
			}
		}else{
			setTimeout(function() {
				openMessageCopyForm(form.title.error,form.error.specialcharformname,form.title.msgBoxTitleerror, moduleId);
			}, 500);
		}
	}else{
		setTimeout(function() {
			openMessageCopyForm(form.title.error,form.error.formnamespace,form.title.msgBoxTitleerror,moduleId);
		}, 500);
	}
	return status;
}

function openModuleMessage(title, content, type,isSave,moduleId) {
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
						copyForms(moduleId);
					}
				}, 500);
			}
		}
	});
}


//To set the target details
function setCopyModuleId(moduleId) {
	var moduleName = document.getElementsByName("_copyTableToModule")[0].value
			.trim();
	if (moduleName != "") {
		var moduleDataList = $('#searchModuleresults');
		var moduleVal = $(moduleDataList).find(
				'option[value="' + moduleName + '"]');
		if (moduleVal.length == 0) {
			$.msgBox({
				title : form.title.message,
				content : form.title.moduleChoose,
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
		} else {
			var copyModuleId=moduleVal.attr('id');
			$("input[name='_copyModuleId']").val(moduleVal.attr('id'));
		}
	}
}

function setDefaultValue(formName){
	$.each($('input, select ,textarea', '#'+formName),function(index){
		var currentValue = $(this).val();
		if(currentValue != null) {
			var startChar = currentValue.substring(0, 1);
			var endChar = currentValue.substring(currentValue.length-1, currentValue.length);
			if(startChar == "|" && endChar == "|"){
				var type = currentValue.substring(1, currentValue.length-1);
				$(this).val(getDefaultValForForm(type));
			}
		}
	});
}

function resetDefaultValue(formName) {
	// forcing reset event
	if ($('#wizardTab-content').length > 0) {
		var childDivLength = $('#wizardTab-content').children().length ;
		for(i=1 ; i<=childDivLength ;i++) {
			var style = $('#wizardTab-content' + "-" + i).attr('style');
			if(style == "") {
				$('#wizardTab-content' + "-" + i).children().find('input,select,textarea').each(function() {
					$(this).val('');
					resetCkeditor();
					setDefaultValue(formName);
				});
				break;
			} 
		}
	} else {
		document.getElementById(formName).reset();
		resetCkeditor();
		setDefaultValue(formName);
	}
}

function resetCkeditor() {
	for (instance in CKEDITOR.instances) {
		var newContent = "";
		var editor_data = CKEDITOR.instances[instance].setData(newContent);
	}
}

//To redirect from form version to form list
function goBackToFormListFromFormVersion() {	
	var params = 'moduleId='+selectedFromModuleId+'&isEdit=true&isSystemModule=false&isJspForm=false';	
	_execute('rightPanel', params);
	Backbone.history.navigate("#bpm/form/formListForModule", {trigger: true, replace: true});
}

function goBackToFormListView(){
	if (CKEDITOR.instances != undefined){
		for (instance in CKEDITOR.instances) {
			window.setTimeout(function(){CKEDITOR.instances[instance].setData("");},0);
			CKEDITOR.instances[instance].destroy();
		}
		toggleFulscreenMode("back_btn");
	}
	document.getElementById("editor1").style.display="none";
	var params = 'isFormCancel=true&moduleId='+selectedFromModuleId;
	 _execute('target', params);
	 Backbone.history.navigate("#bpm/module/manageModuleRelation", {trigger: true, replace: true});
}

function cloneStyleSheet(){
	$("#form_template_preview").html("");	
	var print_modal = $("#form_template_preview");
	var print_frame = $('<iframe class="span12" style="background-color:#FFFFFF;border-radius:3px;border:1px solid #CCCCCC;overflow:auto;" id="print_preview_template_iframe" scrolling="no" border="0" frameborder="0" name="print-frame" /><script>$(document).ready(function(){	$("input[type=submit]").each(function(){  /* $(this).attr("disabled","disabled");  */});	 });</script>');
	print_modal.append(print_frame).appendTo('#form_template');
	var print_frame_ref = document.getElementById('print_preview_template_iframe').contentDocument;
	print_frame_ref.open();
	print_frame_ref.write('<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">' +
	     '<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">' + 
	     '<head><title>' + document.title + '</title></head>' +
	     '<body style="padding:5px"></body>' +
	     '</html>');
	print_frame_ref.close(); 
	
	var $iframe_head = $('head link[media*=print], head link[media=all]').clone();
	
	$iframe_head.each(function() {
        $(this).attr('media', 'all');
    });
    if (!$.browser.msie && !($.browser.version < 7) ) {
        $('head', print_frame_ref).append($iframe_head);
    }
    else {
        $('body > *:not(#print-modal):not(script)').clone().each(function() {
            $('body', print_frame_ref).append(this.outerHTML);
        });
        $('head link[media*=print], head link[media=all]').each(function() {
            $('head', print_frame_ref).append($(this).clone().attr('media', 'all')[0].outerHTML);
        });
    }
}

function addStyleToCKEDITORFakeElement(fakeElement, element){
	if(fakeElement){
		var newDiv = document.createElement("div");
		newDiv.setAttribute("style", element.attributes.style);
		fakeElement.attributes.style = "top:"+newDiv.style.top+";left:"+newDiv.style.left+";position:"+newDiv.style.position+";";
		return fakeElement;
	}else{
		return element;
	}
}

function addStyleToCKEDITORFakeElementOnOK(fakeElement, style){
	var newDiv = document.createElement("div");
	var newStyle = "";
	if(style != ""){
		newDiv.setAttribute("style", style);
		newStyle = "top:"+newDiv.style.top+";left:"+newDiv.style.left+";position:"+newDiv.style.position+";";
	}else{
		var cursor = ckeCartPosition();
		newDiv.setAttribute("style", style);
		newStyle = "top:"+cursor.y+"px;left:"+cursor.x+"px;position:absolute;";
	}
	fakeElement.setAttribute("style", newStyle);
	fakeElement.appendText("&nbsp;");
	return fakeElement;
}

function addStyleToCKEDITORElementOnOK(element){
	var style = element.getAttribute("style");
	if(style != null && style != "" && style != "null"){
		/*$(element.$).css("position","absolute");
		$(element.$).css("left",100);
		$(element.$).css("top",150);
		element.style.position = "absolute"; 
		element.style.left = cursor.x;
		element.style.top = cursor.y;*/
	}else{
		var cursor = ckeCartPosition();
		var newStyle = "top:"+cursor.y+"px;left:"+cursor.x+"px;position:absolute;";
		element.setAttribute("style", newStyle);
	}	
	
	return element;
}

function ckeCartPosition(){
	var editor = CKEDITOR.instances['editor1']; //get your CKEDITOR instance here
	var dummyElement = editor.document.createElement( 'img', {
	   attributes : {
	      src : 'null',
	      width : 0,
	      height : 0
	   }
	});
	editor.insertElement( dummyElement );
	var obj = dummyElement.$;
	var cursor = {
	   x : 0,
	   y : 0
	}
	
	cursor.keydown = false;
	
	while (obj.offsetParent) {
	   cursor.x += obj.offsetLeft;
	   cursor.y += obj.offsetTop;
	   obj = obj.offsetParent;
	}
	cursor.x += obj.offsetLeft;
	cursor.y += obj.offsetTop;
	cursor.keydown = true;
	
	dummyElement.remove();
	return cursor;
}

var url = window.URL || window.webkitURL;
function getImageHeight(){
	$("#formUploadImage").change(function(e) {
        var chosen = this.files[0];
        var image = new Image();
        image.onload = function() {
        	formImageHeight=this.height;
        };
        image.src = url.createObjectURL(chosen);                    
	}); 
}

function manageTemplateForms(){
	var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true);
	var isEdit=selected_nodes.attr("isedit");
	var moduleId=selected_nodes.attr("moduleid");
	var isSystemModule = selected_nodes.attr("issystemmodule");
	var params = 'moduleId='+moduleId+'&isEdit='+isEdit+'&isSystemModule='+isSystemModule+'&isJspForm='+false+'&isTemplateList=true';
	document.location.href = "#bpm/form/formListForModule";
 	_execute('rightPanel',params); 
}

function _showFormTypeIcon(cellValue, options, rawObject){
	if(rawObject.templateForm=="true"){
		return '<img src="/images/templates.png" >';
	}else{
		return '<img src="/images/addresses_small.png">';
	}
	
}
//For Report Classification
function addOptionValToReport(element_id,parent_dictId,columnId,pastValues,isFormReadOnly) {
	$.ajax({
		url: 'bpm/admin/getDictValueByParentId',
	    type: 'GET',
	    data:"parentId="+parent_dictId,
	    dataType: 'json',
		async: false,
		success: function(data) {
			$( "#"+element_id ).empty();
			if(data){
	            $.each(data, function(index, item) {
	                $("#"+element_id).get(0).options[$("#"+element_id).get(0).options.length] = new Option(item.dictValue, item.dictValue);
	            });
	            $("#"+element_id).prepend("<option value='' selected='selected'></option>");
			}
	    }
	});
	if(pastValues){
		if(isFormReadOnly == "true"){
			$('<span style="font-size: 14px;padding: 4px 6px;font-family:Helvetica Neue,Helvetica,Arial,sans-serif;height: 30px;align:left;"><b>&nbsp;&nbsp;&nbsp;'+pastValues[columnId]+'</b></span>').insertAfter( "#"+element_id );
		}else{
			if ($('option[value='+pastValues[columnId]+']', '#'+element_id).length) {
				$("#"+element_id).val(pastValues[columnId]);
			} 
		}
	}
}
