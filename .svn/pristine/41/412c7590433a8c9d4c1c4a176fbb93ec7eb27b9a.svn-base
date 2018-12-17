<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript" src="<c:url value='/scripts/easybpm/form/translation_zh_cn.js'/>"></script>
	
<script>
    $(document).ready(function(){ 
    	getFormName();
    	_prev_tab_id = 1;
       	$('#tabs-content').addClass('js');	
       	$("#button_panel button").each(function() {
       		$(this).click(function() {
       			var tabId = $(this).attr('id');
       		    tabId = tabId.split('-');
	       		 if(tabId[1] == 1){
	       			 var htmlSourceBeforePreview= $("#htmlSourceBeforePreview").val();
	       			 if(_prev_tab_id != 1){
	       				document.getElementById("paletteMainContainer").style.width="85%";
						document.getElementById("paletteList").style.display="block";
						//document.getElementById("hiddedPalette").style.display="none";
						 var htmlsource = "";
			 			 if(_prev_tab_id == 3){
			 				if(htmlSourceBeforePreview!=''){
			 					window.setTimeout(function(){CKEDITOR.instances.editor1.setData(htmlSourceBeforePreview);},0);
	 		       			 }
			 				htmlsource = htmlSourceBeforePreview;
			 			 }else if(_prev_tab_id == 2){
			 				htmlsource = $("#htmlsource").text();	
			 				window.setTimeout(function(){CKEDITOR.instances.editor1.setData(htmlsource);},0);
			 			 }
						CKEDITOR.instances['editor1'].destroy();
						renderCKEditor();
						window.setTimeout(function(){CKEDITOR.instances.editor1.setData( htmlsource, function() {
							_prev_tab_id = 1;
							CKEDITOR.instances.editor1.focus();
							if(htmlsource.indexOf("changeWizardByTabForDynamicForm()") > -1){
								var ckEditorIframe = $('#ckEditor').find("iframe")[0];
								$(ckEditorIframe).attr('id','ckEditor_iframe');
							}
							var height = $("#target").height();
							$("#ckEditor").css('height',parseInt(height)-160);
							var ckEditor = $("#ckEditor").children().children().children('.cke_contents');
							ckEditor.css('height',parseInt(height)-280);
							CKEDITOR.on( 'instanceReady', function( ev ){
								loadScriptFileInCKEditorIframe('ckEditor_iframe');  
							});
							CKEDITOR.instances.editor1.document.getBody().removeAttribute("contenteditable");
							 
						});},0);
	       			 }
				  }
		 		  if(tabId[1] == 2){
		 			 var htmlSourceBeforePreview=  $("#htmlSourceBeforePreview").val();
		 			 if(_prev_tab_id==3 && htmlSourceBeforePreview!=''){
		 				window.setTimeout(function(){CKEDITOR.instances.editor1.setData(htmlSourceBeforePreview);},0);
		       			$("#htmlsource").html(htmlSourceBeforePreview);
		       		 }
		 			window.setTimeout(function(){
			 			 _prev_tab_id = 2;
			 			 
						document.getElementById("paletteMainContainer").style.width="96%";
						document.getElementById("paletteList").style.display="none";
						//document.getElementById("hiddedPalette").style.display="block";
						var height = $("#target").height();
						$("#htmlsource").css('height',parseInt(height)-205);
			 			setHTMLSource();
		 			},0);
	 		      }
		 		  if(tabId[1] == 3){
		 			 if(_prev_tab_id != 3){
		 				 var height = $("#target").height();
			 			 var htmlsource = "";
			 			 if(_prev_tab_id == 1)
			 				htmlsource = CKEDITOR.instances.editor1.getData();
			 			 else if(_prev_tab_id == 2)
			 			 	htmlsource = $("#htmlsource").text();		
			 			 _prev_tab_id = 3;		
						 document.getElementById("paletteMainContainer").style.width="96%";
						 document.getElementById("paletteList").style.display="none";
						 //document.getElementById("hiddedPalette").style.display="block";
						 $("#preview_form").css('height',parseInt(height)-170);
						 document.getElementById("htmlSourceBeforePreview").value=htmlsource;
						 $("#previewHtmlSourceDiv").html(htmlsource);
						 $("#previewHtmlSourceDiv").find(".datetimepicker").each(function() {
							  var id=$(this).attr("id")+"_temp_datePiceker_elem";
							  var style=$(this).attr("style");
							  $(this).attr("id",id);
							  $("#"+id).replaceWith('<img style="'+style+'" src="/scripts/ckeditor/plugins/datetimepicker/images/datetimefield.png"/>');
							});
						$("#previewHtmlSourceDiv").find(".number-validation").each(function() {
							$(this).attr("onkeyup","this.value =this.value.replace(/[^0-9]/g,'');");
						});

						$("#previewHtmlSourceDiv").find(".ck-form-button").each(function() {
							var style=$(this).attr("style");
							//$(this).prop("style").val(style+"readonly:'readonly';");
							$(this).attr("disabled", "disabled");
						});
						 $("#previewHtmlSourceDiv").find(".datepicker").each(function() {
							  var id=$(this).attr("id")+"_temp_datePiceker_elem";
							  var style=$(this).attr("style");
							  $(this).attr("id",id);
							  $("#"+id).replaceWith('<img style="'+style+'" src="/scripts/ckeditor/plugins/datetimepicker/images/datetimefield.png"/>');
						});
	
						$("#previewHtmlSourceDiv").find("#form_div").each(function() {
							this.setAttribute("contenteditable","false");
						});
						var timeOut=0;
						$("#previewHtmlSourceDiv").find(".data_dictionary").each(function() {
							timeOut=500;
							var element_id = this.getAttribute("id");
							var parent_dictId = this.getAttribute("datadictionary");
							var style=$(this).attr("style");
							var temp="<select style='"+style+"'>";
							$.ajax({
							    url: 'bpm/admin/getDictValueByParentId',
							    type: 'GET',
							    data:"parentId="+parent_dictId,
							    dataType: 'json',
							    async: true,
							    success: function(data) {
							    $( "#"+element_id ).empty();
								if(data){
								   var option ="";
								    $.each(data, function(index, item) {
									option=option+"<option value='"+item.dictValue+"'>"+item.dictId+"</option>";
								    });
								temp = temp+option+"</select>";
								$("#"+element_id).replaceWith(temp);
								}
							    }
							});
						});
						 window.setTimeout(function(){CKEDITOR.instances.editor1.setData($("#previewHtmlSourceDiv").html());setPreviewSource();},timeOut);
						}
		 		   }
       		    var tabContent = document.getElementById('tab-content-' + tabId[1]);
       		    tabContent.style.display = 'block';
       		 	$(this).removeClass('btn hidden-tablet');
       		    $(this).addClass('btn btn-primary');
       		    $(this).siblings().removeClass('btn btn-primary');	
       		 	$(this).siblings().addClass('btn hidden-tablet');
       		    $(tabContent).siblings().css('display','none');
       		});
           });
       	fullScreenMode();
       	var height = $("#target").height();
    	$("#create_form").css('height',parseInt(height)- 130);
    	$("#palette_block").css('height',parseInt(height)- 130);
    });
     
</script>
<script type="text/javascript">
/**
 *For windows Chrome and Internet Explorer  back space Event prevent purpose
 */
/* function preventBackSpaceEvent(e) {
    var KeyID = (window.event) ? event.keyCode : e.keyCode || e.charCode;
    if(KeyID==8 && ((e.target || e.srcElement).tagName != "TEXTAREA") && ((e.target || e.srcElement).tagName != "INPUT")) {
    	if (navigator.userAgent.toLowerCase().indexOf("msie") == -1) {
    		e.stopPropagation();
		e.preventDefault();
		e.cancelBubble = true;
	} else {
		e.returnValue = false;
	}
	e.stopPropagation();
        return false;
    }
}  */

//window.onkeydown = preventBackSpaceEvent;

/**
 *For windows Firefox Back Space Event prevent purpose
 */
/* window.onkeypress = function(event) {
	if (!event) { 
		event = window.event;
	}
	var keyCode = event.keyCode;
	if (keyCode == 8 &&((event.target || event.srcElement).tagName != "TEXTAREA") && ((event.target || event.srcElement).tagName != "INPUT")) { 
		if (navigator.userAgent.toLowerCase().indexOf("msie") == -1) {
			event.stopPropagation();
			event.preventDefault();
			e.cancelBubble = true;
		} else {
			alert("prevented");
			event.returnValue = false;
		}
		return false;
	}
}; */
</script>
<div class="row-fluid">
	<div class="page-header">
		<div class="pull-left">
			<h2><fmt:message key="formdesigner.title"/></h2>
		</div>
		<span class="floatRight fontSize14 pad-L10 pad-T2">
   		<div class="cursor_pointer">
   				<img onclick="goBackToFormListView();" src="/images/arrow-left.png" />
   			</div>
   		</span>
   		<span class="floatRight fontSize14 pad-L10 pad-T2">
   			<div id="toggle-fullscreen-div" class="cursor_pointer">
   				<img src="/images/easybpm/common/min.png" />
   			</div>
   		</span>
   		<div id="button_panel" class="pull-right">
   			<button class="btn btn-primary" id="tabs-1" type="button"><fmt:message key="form.design"/></button>
   			<button class="btn hidden-tablet" id="tabs-2" type="button"><fmt:message key="form.hTMLSource"/></button>
   			<button class="btn hidden-tablet" id="tabs-3" type="button"><fmt:message key="form.preview"/></button>
   		</div>
	</div>
	<div class="span12">
			<div id="paletteList" class="span2">
				<div class="widget">
					<div class="widget-header">
						<div class="title">
							<span class="fs1" aria-hidden="true"></span><fmt:message key="form.palette"/>
						</div>
					</div>
					<div class="widget-body">
						<div id="palette_block" class="manageMenu_boder" >
			        	<a class="form_elements btn btn-small btn-block"  id="textfield" href="#" > 
							<div align="left">
							<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/textbox.png" /></div>
							<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.textBox"/></span> 
							</div>
			        	</a>
						<a class="form_elements btn btn-small btn-block"  id="label" href="#" > 
							<div align="left">
							<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/label.png" /></div>
							<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.label"/></span> 
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block"  id="numberfield" href="#" > 
							<div align="left">
							<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/number.png" /></div> 
						    <span class="fs1" aria-hidden="true"><fmt:message key="form.palette.numberBox"/></span>
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block"  id="textarea" href="#" > 
							<div align="left">
			        		<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/textarea.png" /></div> 
			        		<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.textArea"/></span> 
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block"  id="select" href="#" > 
							<div align="left">
							<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/dropdown.png" /></div> 
						    <span class="fs1" aria-hidden="true"><fmt:message key="form.palette.dropDown"/></span> 
							</div>
			        	</a>
			        	 <a class="form_elements btn btn-small btn-block"  id="autocomplete" href="#" > 
							<div align="left">
							<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/autocompletefield.png" /></div>
							<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.autocomplete"/></span> 
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block"  id="radio" href="#" > 
							<div align="left">
							<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/radiobutton.png" /></div> 
						    <span class="fs1" aria-hidden="true"><fmt:message key="form.palette.radioButton"/></span> 
							</div>
						        	
			        	</a>
			        	<a class="form_elements btn btn-small btn-block"  id="checkbox" href="#" > 
							<div align="left">
							<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/checkbox.png" /></div> 
			        		<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.checkBox"/></span> 
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block"  id="hiddenfield" href="#" > 
							<div align="left">
							<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/hiddenfield.gif" /></div> 
			        		<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.hiddenField"/></span>
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block" id="datetimepicker" href="#"> 
							<div align="left">
			        		<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/datetime.png" /></div> 
			        		<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.dateTime"/></span>
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block" id="button" href="#"> 
							<div align="left">
			        		<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/submitbutton.png" /></div> 
			        		<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.addButton"/></span>
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block" id="showbuttons" href="#"> 
							<div align="left">
			        		<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/button_list.png" /></div> 
			        		<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.showButtons"/></span>
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block" id="fileupload" href="#"> 
							<div align="left">
			        		<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/fileupload.png" /></div> 
			        		<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.fileUpload"/></span>
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block" id="image" href="#"> 
							<div align="left">
			        		<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/imageIcon.png" /></div> 
			        		<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.Image"/></span>
							</div>
			        	</a>
			            <a class="form_elements btn btn-small btn-block" id="usercontrol" href="#"> 
							<div align="left">
			                   <div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/usercontrol.png" /></div> 
			                   <span class="fs1" aria-hidden="true"><fmt:message key="form.palette.userControl"/></span>
							</div>
			            </a>
			        	<a class="form_elements btn btn-small btn-block" id="userlist" href="#"> 
							<div align="left">
			        		<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/user_icon.png" width="14" height="14"/></div> 
			        		<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.user"/></span>
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block" id="departmentlist" href="#"> 
							<div align="left">
			        		<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/departments.png" /></div> 
			        		<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.department"/></span>
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block" id="rolelist" href="#"> 
							<div align="left">
			        		<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/role_icon.png" /></div> 
			        		<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.role"/></span>
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block" id="grouplist" href="#"> 
							<div align="left">
			        		<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/group_icon.png" /></div> 
			        		<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.group"/></span>
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block" id="subforms"href="#"> 
							<div align="left">
			        		<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/subformm.png" /></div> 
			        		<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.subForm"/></span>
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block" id="templates" href="#"> 
							<div align="left">
			        		<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/template.png" /></div> 
			        		<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.templates"/></span>
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block" id="richtextbox" href="#"> 
							<div align="left">
			        		<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/richtextbox.jpg" /></div> 
			        		<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.richtextbox"/></span>
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block" id="gridviewcontrol" href="#"> 
							<div align="left">
			        		<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/images/easybpm/form/gridview.jpg" /></div> 
			        		<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.gridview"/></span>
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block" id="tabcontrol" href="#"> 
							<div align="left">
			        		<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/scripts/ckeditor/plugins/tabcontrol/images/tab-add-icon.png" /></div> 
			        		<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.tabDesign"/></span>
							</div>
			        	</a>
			        	<a class="form_elements btn btn-small btn-block" id="onloadevent" href="#"> 
							<div align="left">
			        		<div class="floatLeft" style="padding-left:10px;padding-right:10px;margin-top:-2px;"><img src="/scripts/ckeditor/plugins/onloadevent/images/onload.gif" /></div> 
			        		<span class="fs1" aria-hidden="true"><fmt:message key="form.palette.formOnLoadScript"/></span>
							</div>
			        	</a>
						</div>
		        	</div>
		        	<div class="clearfix"></div>
		        </div>
			</div>
			
			<div id="paletteMainContainer" class="span10">

				<div class="widget">
					<div class="widget-header">
						<div class="title">
							<span class="fs1" aria-hidden="true"></span> <fmt:message key="formdesigner.title"/> <span id="showFormName"></span>
						</div>
					</div>
					<div class="widget-body">
						<div id="create_form" class="manageMenu_boder" >
						<form:form  commandName="form" method="post" action="bpm/form/saveDynamicForm" autocomplete="off" modelAttribute="metaForm" id="saveForm"> 
						<div id="tabs-content">
							<div id="tab-content-1" class="height-per-100">
							<div id="ckEditor">
								<textarea cols="80" id="editor1" name="editor1" rows="10"></textarea>
								<script>
								 
								function renderCKEditor(){
									CKEDITOR.replace('editor1', {
										/* fullPage : true, */
										/* { name: 'forms', items : [ 'Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ShowButtons', 'AddButton', 'ImageButton', 'HiddenField' ] },*/
										//extraPlugins : 'button_manager',
										toolbar :
											[
											    { name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },
											    { name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote','CreateDiv','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl' ] },
											    { name: 'links', items : [ 'Link','Unlink','Anchor' ] },
											    { name: 'insert', items : [ 'Table','HorizontalRule','Smiley','PageBreak'] },
											    { name: 'colors', items : [ 'TextColor','BGColor' ] },
											    '/',
											    { name: 'styles', items : [ 'Styles','Format','Font','FontSize' ] },
											    { name: 'tools', items : [ 'ShowBlocks','-'] },
											    { name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
											    { name: 'editing', items : [ 'Find','Replace','-','SelectAll'] }
											]
									});
								}
									
								$(document).ready(function(){ 
							        	renderCKEditor();
							    });
		
								/* Return element id object
								id-> str|obj:
								    if string, return element id object
								    if object, return object
								d-> object or undefined (default is document)
								###############################*/
								function jQuery(id,d){
									 // alert("jquer" + typeof(id));
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
									  //alert("et");
									return (isDefined(d)?d:document).getElementsByTagName(t);
								}
		
								/* Tests whether variable is defined
								a->variable  
								###############################*/
								function isDefined(a){
									// alert("isdefi" + a);
									return typeof(a)=='undefined'?false:true;
								}
		
								/* Returns DOM elements that are members of id (a) having tag (b)
								a->str|obj: element id or object
								b->str: tag name
								###############################*/
								function return_MemOfIdByTag(a,b){
									 //alert("mem tag");
									return jQueryet(b,jQuery(a));
								}
		
								/* Returns Editor Object
								a->str: id of element converted into an editor
								###############################*/
								function Editor_Object(a){
									//alert("editor obj");
									a=return_MemOfIdByTag('cke_editor1','iframe')[0];
									//alert("a.contentDocument----" + a.contentDocument);
									return a.contentDocument?a.contentDocument:a.contentWindow.document;
								} 	 
								</script>
							</div>
							<div id="submitForm" align="right" class="pad-R10">
								<input type="hidden" id="xmlSource" name="xmlSource"/>
								<input type="hidden" id="htmlSource" name="htmlSource"/>
								<input type="hidden" id="htmlSourceView" name="htmlSourceView"/>
								<input type="hidden" id="formName" name="formName"/>
								<input type="hidden" id="wizardScript" name="wizardScript"/>
								<input type="hidden" id="tableName" name="tableName"/>
								<input type="hidden" id="tableId" name="table.id"/>
								<input type="hidden" id="moduleId" name="module.Id"/>
								<input type="hidden" id="publicForm" name="publicForm">
								<input type="hidden" id="templateForm" name="templateForm">
								<input type="hidden" id="englishName" name="englishName"/>
								<input type="hidden" id="description" name="description"/>
								<input type="hidden" id="backgroungImgPath" name="backgroungImgPath"/>
								<input type="hidden" id="isDefaultForm" name="isDefaultForm"/>
								
								<input id="update_cke_form" type="submit" value="保存" class="btn btn-primary" onclick="clearFormData();toggleFulscreenMode('form_submit');return getCkEditorElem('create');"/>
								<input type="button" class="btn btn-primary" name="next" onclick="goBackToFormListView()" id="cancelButton" style="cursor: pointer;" value="取消" />
							</div>
							</div>
							<div id="tab-content-2" class="textalign-left">
						   		<!-- <div class="row-fluid background-white height-per-95"> -->
						   		 <pre class="mxml" name="htmlsource" id="htmlsource" contenteditable="true"></pre>
						   		 <div id="updateForm" class="mar-B15 mar-R15  mar-T10" align="right">
					             	<input id="save_cke_form" type="submit" value="保存" onclick= "clearFormData();toggleFulscreenMode('form_submit');return setCKEditorSource('create');clearFormData();" class="btn btn-primary"/>
					             </div>
						   		<!-- </div> -->
						   	</div>
						   	<div id="tab-content-3" class="textalign-left">
								 <div id="previewdst" class="mxml" style="display:none;"></div>
								 <div id="preview_form" class="mxml"></div>
							</div>
						</div>
						</form:form>
						</div>
						<div class="clearfix"></div>
					</div>
				</div>
			</div>
	</div>
</div>
<div id="button_icon_dialog"></div>		
<div id="form_button_dialog"></div>	
<div id="temp_ckEditor_html_div" class="displayNone"></div>	
<form  commandName="formButton" method="post" action="bpm/form/saveFormButton" autocomplete="off" modelAttribute="formButton" id="saveFormButton" style="display:none;"> 
	<input type="hidden" id="_btnName" name="name"/>
	<input type="hidden" id="_btnFunc" name="func"/>
	<input type="hidden" id="_btnStyle" name="style"/>
	<input type="hidden" id="_btnType" name="type"/>
	<input type="hidden" id="_iconPath" name="iconPath"/>
	<input type="hidden" id="_btnTextValue" name="textValue"/>
	<input type="submit" value="Save Form" />
</form>

<div id="previewHtmlSourceDiv" class="displayNone"></div>
<!-- <div id="htmlSourceBeforePreview" class="displayNone"></div> -->
<div id="template_data" class="displayNone"></div>
<textarea id="htmlSourceBeforePreview" name="htmlSourceBeforePreview" style="display:none;"></textarea>
<div id="sourceView_id" name="sourceView_id" class="displayNone"></div>
