<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%-- <page:applyDecorator name="default"> --%>
<head>
<title><fmt:message key="documentForm.title" /></title>
<meta name="heading" content="<fmt:message key='documentForm.title'/>" />
</head>
<%@ include file="/common/messages.jsp"%>
<script type="text/javascript">
							 function return_MemOfIdByTag(a,b){
								//console.log(a);
								return jQueryet(b,jQuery(a));
							}

							function jQuery(id,d){
							    if(typeof(id)=='string'){
							    	return (isDefined(d)?d:document).getElementById(id);
							    }
								return id;
							} 
				
							/* Return elements by tag
							t-> tag
							d-> object or undefined (default is document)
							###############################*/
							function jQueryet(t,d){
								return (isDefined(d)?d:document).getElementsByTagName(t);
							}
				
							/* Tests whether variable is defined
							a->variable  
							###############################*/
							function isDefined(a){
								return typeof(a)=='undefined'?false:true;
							}
				
							
							function jspFormSubmit() { 
								var sourceJsp = $("#sourcejsp").val() ;
								$("#jspSubmit").attr('disabled','disabled');
								var fileJsp = document.getElementById("files").files[0];
								//alert(sourceJsp+"=========="+fileJsp);
								var editorObj = document.createElement('div');
								var editor_data = "";
								if( (sourceJsp == "" && document.getElementById("files").value == "") || (sourceJsp != "" && document.getElementById("files").value != "") ) {
									validateMessageBox(form.title.validation, "Choose one file" , "info");
									document.getElementById("files").value = "";
									document.getElementById("sourcejsp").value = "";
									return false;
								}else { 
									var file = "";
									if(sourceJsp != "") {
										file = $("#sourcejsp").val();
										var url = window.location.href;
										var jspUrl = "http://"+url.split("/")[2]+"/resources/jsp/"+file;
										//alert("==url=="+url.split("/")[2]+"=="+url.split("/")[1]+"=="+file+"===="+jspUrl);
										$.get(jspUrl, function(data) {
											editor_data = data;
											editorObj.innerHTML = editor_data;
									    });
									} else if(document.getElementById("files").files[0] != ""){
										//alert("ff");
										file = document.getElementById("files").files[0];
								        var reader = new FileReader();
								        reader.readAsText(file, "UTF-8");
								        reader.onload = function (evt) {
								        	editor_data = evt.target.result;
								        	console.log(editor_data);
											editorObj.innerHTML = editor_data;
								        }
									} else {
										validateMessageBox(form.title.validation, "File not uploaded" , "info");
								        document.getElementById("files").value = "";
										return false;
									}
									var form_tags = "";
									form_tags = return_MemOfIdByTag(editorObj,'form');
								setTimeout(function(){
										formatXML(form_tags,editor_data,editorObj);
								},1000);
							}
					}
	
	function formatXML(form_tags,editor_data,editorObj,event) {
			//alert("ffffffff");
			var formName = "";
			var tableId = "";
			if(form_tags.length > 0){
				for(var index = 0; index < form_tags.length; index++){
					 formName = form_tags[index].getAttribute('name');
					 tableId = form_tags[index].getAttribute('table');
				}
			}
			var beginString = '<form action="#"'+' id='+formName+ ' method="post" name='+formName+' style="min-height:300px;width:1142.857142857143px"'+' table='+tableId+'>' ;
			var endString = "</form>";
			console.log(editorObj);
			console.log(form_tags);
			//alert(tableId+"===="+formName+"===="+jspName);
			$("#formName").val(formName)
			var jspName = formName;
			var isValidForm = true ;
			isValidForm = formValidation();
			if(isValidForm == false) {
				//alert(isValidForm);
				//event.preventDefault();
				$("#jspSubmit").removeAttr("disabled");
				return false;
			}
			if(jspName == formName && isValidForm == true){
				$("#tableId").val(tableId);
				var englishName = Math.random().toString(36).substring(7);
				$("#englishName").val(englishName+new Date().getTime());
				var htmlSourceText = editor_data;
				if(htmlSourceText.indexOf("<form") == -1 && htmlSourceText.indexOf("fff") == -1){
					validateMessageBox(form.title.validation, "Form is not present." , "info");
		    		document.getElementById("files").value = "";
					return false;
				}
				document.getElementById('htmlSource').value = htmlSourceText;
				var str = "";
				str += startXML(formName,tableId,tableId);
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
					$( "#temp_ckEditor_html_div" ).html(editor_data);			
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
					document.getElementById('htmlSource').value= beginString +document.getElementById("temp_ckEditor_html_div").innerHTML + endString;
					document.getElementById('xmlSource').value = str;
					userNameHiddenIds=[];
					formInputIds=[];
					columnIdList=[];
					form_Field_column_map={};
					form_Field_name_List=[];
					form_Field_name_Map={};
					$("#jspSubmit").removeAttr("disabled");
					//jspFormSubmit();

				}else{
			    	document.getElementById("files").value = "";
					return false;
				}
		} 
	}
	
function formValidation() {
	var tempFormname = $("#formName").val();
	var formId = $("#formId").val();
	if(/^[\-a-zA-Z\u4e00-\u9eff\s][\-a-zA-Z\u4e00-\u9eff\s0-9]*$/
			.test(tempFormname) != false ) {
		if(!formId) {
			var newdata = $.ajax({
				type : "GET",
				async : false,
				url : "bpm/form/isFormNamePresent?formName=" + tempFormname
			}).responseText;
			var da = eval('(' + newdata + ')');
			if (da.isPresent == true) {
				$("#formName").val("");
				setTimeout(
						function() {
							validateMessageBox(form.title.message,
									form.error.formexists, "info");
						}, 500);
				document.getElementById("files").value = "";
				document.getElementById("sourcejsp").value = "";
				return false;
			} else { 
				return true;
			}
			
		} else {
			return true;
		}
			 
	} else {
		$("#formName").val("");
		setTimeout(function() {
			validateMessageBox(form.title.message,
					form.error.specialcharformname, "info");
		}, 500);
		document.getElementById("files").value = "";
		document.getElementById("sourcejsp").value = "";
		return false;
	}
}


	function deleteRow(row){
        document.getElementById("files").value = "";
     }
	
	function showJspFileSelection(title, selectionType, appendTo, selectedValues, callAfter){
		document.location.href = "#bpm/form/showJspSelection";
		_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&rootNodeURL=bpm/form/getAllJspFromSource&selectedValues='+selectedValues.value+'&callAfter='+callAfter+'&selection=selectedJsp');
		$myDialog = $("#userPopupDialog");
		
		if(selectionType.toLowerCase() == "single"){
			title = "请选择Jsp文件(单选)";
		}
		
		if(selectionType.toLowerCase() == "multi"){
			title = "请选择Jsp文件(多选)";
		}
		
		$myDialog.dialog({
			width: 'auto',
			height: 'auto',
			top: '10px',
			modal: true,
			position: [($(window).width() / 3), 70],
			title: title
         });
		 $myDialog.dialog("open");
	}
	
	/*  function jspFormSubmit(){
		alert("subbbbbb");
		var form = document.getElementById("metaForm");
		form.action= "bpm/form/saveDynamicJspForm";
		_execute('rightPanel','');
		$("#metaForm").submit();
	}  */
	
/*$(document).ready(function() {
    $('#metaForm').submit(function() {
var ttt = formatxml();
       alert(isSuccessJsp);

 var url = "bpm/form/saveDynamicJspForm"; // the script where you handle the form input.

    $.ajax({
           type: "POST",
           url: url,
           data: $("#metaForm").serialize(), // serializes the form's elements.
           success: function(data)
           {
               alert("success"); // show response from the php script.
           }
         });

return false;
    });
});*/ 
</script>
<div class="span12">
	<div class="row-fluid">
		<div class="control-group">
			 <div class="titleBG"> 
				<strong><span class="floatLeft fontSize14 pad-L10 pad-T10"><fmt:message key="module.title.fileInformation"/></span></strong><br><br>		
			</div>
		</div>
	</div>
<div class="span12 box">
	<spring:bind path="metaForm.*">
		<%@ include file="/common/messages.jsp"%>
	</spring:bind>
	<div class="span12 scroll">
		<div class="row-fluid">
			<form:form commandName="metaForm" method="post"	class="form-horizontal no-margin" action="bpm/form/saveDynamicJspForm" autocomplete="off" modelAttribute="metaForm" id="metaForm" onSubmit="_execute('rightPanel','')">				<div id="wizardTab-content">
					<div id="wizardTab-content-1" class="displayBlock">
						
							
						<div class="control-group">
							<eazytec:label styleClass="control-label"
								key="documentForm.uploadFile" />
							<div class="controls">
								<div id="jspFile">
									<div id="clonablerow">
										<input type="file" name="files" id="files""/> 
										<a href="javascript:;" onclick="deleteRow(this)"><fmt:message key="task.involved.remove"/></a>
									</div>
								</div>
							</div>
						</div>
						
						<div class="control-group">
							<label for="uploadFileFromSource" class="control-label">从源代码选择</label>
							<div class="controls">
								<div id="jspFileFromSource">
									<div id="clonablerow">
 										<input type="text" id="sourcejsp" name="sourcejsp" class="span5"  onchage="checkFormSubmit();" onclick="showJspFileSelection('Jsp', 'single' , 'jsp', this , '')"/>
 										<input type="hidden" id="jspName" name="jspName" class="span5"/>
									</div>
								</div>
							</div>
						</div>
							<c:if test="${not empty metaForm.id}">
								<div class="control-group">
									<eazytec:label styleClass="control-label"
										key="documentForm.uploadedFileName" />
									<div class="controls">
										<b font-size="15px"> ${formName} </b>
									</div>
								</div>
							</c:if>

						<div class="control-group">
							[备注 : 弹出选择框中将列出 <b>{Application Path}/resources/jsp</b> 路径下的所有Jsp文件]
						</div>
							<div class="form-horizontal no-margin">
							<div class="control-group">
								<div class="form-actions no-margin">
									<span id="saveButtonDiv">
										<button type="submit" id="jspSubmit" class="btn btn-primary normalButton padding0 line-height0" name="save"  >
                                             <fmt:message key="button.save"/>
			                         	</button>
									</span>
									<div class="clearfix"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div id="temp_ckEditor_html_div" class="displayNone"></div>
				<input type="hidden" id="formName" name="formName" />
				<input type="hidden" id="formId" name="formId" value="${formId}" />				
				<input type="hidden" id="xmlSource" name="xmlSource" />
				<input type="hidden" id="id" name="id" />
				<input type="hidden" id="htmlSource" name="htmlSource" />
				<input type="hidden" id="htmlSourceView" name="htmlSourceView" />
				<input type="hidden" id="tableName" name="tableName" />
				<input type="hidden" id="tableId" name="tableId" />
				<input type="hidden" id="moduleId" name="moduleId"
					value="${moduleId}" />
				<input type="hidden" id="publicForm" name="publicForm" value="false">
				<input type="hidden" id="templateForm" name="templateForm"
					value="false">
				<input type="hidden" id="englishName" name="englishName" />
			</form:form>
		</div>
	</div>
</div>
</div>


