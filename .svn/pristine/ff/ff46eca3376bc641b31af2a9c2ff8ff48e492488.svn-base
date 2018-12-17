<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function() {
	 $("#createdTime").datetimepicker({dateFormat: "yy-mm-dd",timeFormat: "HH:mm:ss"});
	 $("#lastModifyTime").datetimepicker({dateFormat: "yy-mm-dd",timeFormat: "HH:mm:ss"});
	 
	var width = $("#user-grid").width();
	var height = $("#user-grid").height();
	if($('#id').val() == null || $('#id').val() == ''){
		$('#wizardTab-content-2').hide();
		$('#wizardTab-content-3').hide();
		$('#wizardTab-content-4').hide();
	}
})
var myarray= new Array();

$("#saveButton").click(function(event) { 
	var documentFormName = $('#name').val();
	var folderId = $('#allx').val('${allx.lxmc}');
	var uploadedFiles = document.getElementById('files');
	//Uploaded Document Validation
	var regex= /^.*\.(txt|html|csv|doc|rtf|docx|xls|ppt|pptx|odp|ods|odt|jpg|png|gif|bmp|pdf|xml)$/i;
	for (var i = 0; i < uploadedFiles.files.length; ++i) {
	  var uploadedFileName = uploadedFiles.files.item(i).name;
		if(regex.test(uploadedFileName) != true) {
			openMessageBox(form.title.error,form.error.invalidFileFormat,"error", true);
			event.preventDefault();
		}
	}
});
	/* documentFormName = documentFormName.trim();
	var documentFormId = $('#id').val();
		if(documentFormName != "") {
			if(/^[\-a-zA-Z0-9\_\u4e00-\u9eff\s]{1,100}$/.test(documentFormName)) {
					$.ajax({	
						type: 'GET',
						async: false,
						url : '/dms/checkDocumentFormName',
						data: { 'documentFormName' : documentFormName, 'documentFormId' : documentFormId, 'folderId' : folderId} ,
						success : function(response) {
							var isDuplicate = response;
							if(isDuplicate) {
								$('#name').val('');
								openMessageBox(form.title.error,form.error.documentFormNameExist,"error", true);
						        event.preventDefault();
							}else {
								return true;
							}
						}
					});
			}else {
				$('#name').val('');
				openMessageBox(form.title.error,form.error.documentFormNameSpecialCharacterValidation,"error", true);
				event.preventDefault();
			}
		}else {
			openMessageBox(form.title.error,form.error.documentFormNameEmptyValidation,"error", true);
	        event.preventDefault();
		} */


$('.deleteDiv').on('click', function() {
	var id = $(this).attr("id");
	var divId = $("#" + id).closest("div").attr("id");
	if (id.indexOf('delete') != 0) {
		$('#' + divId).remove();
	} else {
		$('#'+divId).find("*").each(function() { 
			$(this).attr("value",  "");
		});	
	}
});
 
function downloadDocument2(id){
    document.location.href = "bpm/alxxb/downloadDocument?id="+id;
}
</script>
<head>
<title><fmt:message key="newgroup.title" /></title>
<meta name="heading" content="<fmt:message key='newgroup.title'/>" />
</head>

<spring:bind path="alxxb.*">
	<%@ include file="/common/messages.jsp"%>
</spring:bind>

<div class="page-header">
	<div class="pull-left">
		<c:choose>
			<c:when test="${empty alxxb.id}">
				<h2>新增记录</h2>
			</c:when>
			<c:otherwise>
				<h2>编辑记录</h2>
			</c:otherwise>
		</c:choose>
	</div>

	<div align="right"><a class="padding10" style="text-decoration: underline;" id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="backToPreviousPage()" data-icon=""><button class="btn"><fmt:message key="button.back"/></button></a> </div>
</div>

<div class="span12">
	<div class="row-fluid">
		<div class="widget-body">
			<div class="box_main">
				<form:form id="alxxb" commandName="alxxb" method="post"
					class="form-horizontal no-margin" action="/bpm/alxxb/saveAlxxb?type=${alxxb.allx}"
					autocomplete="off" modelAttribute="alxxb"
					onSubmit="_execute('target','');">
					<form:hidden path="id" />
					<div class="control-group">
						<label   for="viewName" class="control-label" >案例名称<span class="required">*</span></label>
						<div class="controls">
							<c:if test="${empty alxxb.id}">
				                <form:input path="name" id="name" class="span6"  required="true"/>
				            </c:if>
				            <c:if test="${not empty alxxb.id}">
				                <form:input path="name" id="name" readonly="false" class="span6"/>
				            </c:if>
						</div>
					</div>
				 		
					 
					<%-- <div class="control-group">
						<label for="viewName" class="control-label">当事人<span class="required">*</span></label>
						<div class="controls">
							<form:input path="dsr" id="dsr" required="true"/>
						</div>
					</div> --%>
					
					<div class="control-group">
						<label for="viewName" class="control-label">案例类型 </label>
						<div class="controls">
							<form:input path="allx" id="allx" readonly="true"  value="${lxmc}" />
						</div>
					</div>
					
		 	<div class="control-group">
						<label for="viewName" class="control-label">案例内容</label>
							<div class="controls">
								<div id="ckEditor">
									<form:textarea  path="content" class="span8" id="content" name="content" rows="10"></form:textarea>
									<script>
									function renderCKEditor(){
										CKEDITOR.replace('content', {
											/* fullPage : true, */
											/* { name: 'forms', items : [ 'Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ShowButtons', 'AddButton', 'ImageButton', 'HiddenField' ] },*/
											//extraPlugins : 'button_manager',
											toolbar :
												[
													{ name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
													{ name: 'links', items : [ 'Link','Unlink','Anchor' ] },
													{ name: 'insert', items : [ 'Table','HorizontalRule','Smiley','PageBreak'] },
												    { name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','-','RemoveFormat' ] },
												    { name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote'] },
												    { name: 'colors', items : [ 'TextColor','BGColor' ] },
												    { name: 'styles', items : [ 'Format','FontSize','Font' ] }
												   
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
							</div>
						</div>
		 				<div class="control-group">
							<div class="controls">
								<fmt:message key="support.format"/> (${fn:replace(fileFormat,'|',', ')})
							</div>
						</div>
					 
					  <div class="control-group">
							<eazytec:label styleClass="control-label" key="documentForm.uploadedFiles"/>
							<div class="controls pad-T4">
								<table width='300px' >
								<c:forEach items="${documents}" varStatus="loop" var="document">
								<tr>
									<td width='30%'><div class="manage">${document.name}</div></td>
									<c:choose>
										<c:when test="${documentForm.permissionDTO.canDownload == true}">
											<td width='30%'><button class="btn" style="width:75px;" href="javascript:;" onclick="downloadDocument2('${document.id}'); return false;"><center><fmt:message key="documentForm.download"/></center></button></td>
										</c:when>
										<c:otherwise>
											<td width='30%'><button class="btn" style="width:75px;" href="javascript:;" onclick="downloadDocument2('${document.id}'); return false;"><center><fmt:message key="documentForm.download"/></center></button></td>
										</c:otherwise>
									</c:choose>	
										
						 
									<c:choose>
										<c:when test="${documentForm.permissionDTO.canDelete == true}">
											<td width='30%'><button class="btn" style="width:75px;" onclick="document.location.href='#bpm/alxxb/deleteDocument'; _execute('target','documentFormId='+'${alxxb.id}'+'&documentId='+'${document.id}'); return false;"><fmt:message key="documentForm.delete"/></button></td>		
										</c:when>
										<c:otherwise>
											<td width='30%'><button class="btn" style="width:75px;" onclick="document.location.href='#bpm/alxxb/deleteDocument'; _execute('target','documentFormId='+'${alxxb.id}'+'&documentId='+'${document.id}'); return false;"><fmt:message key="documentForm.delete"/></button></td>		
										</c:otherwise>
									</c:choose>
									</td>
									</tr>
								</c:forEach>	
																		
									</tbody>
								</table>
								
							</div>
						</div> 
					 
			 
					 	  <div class="control-group">
							<eazytec:label styleClass="control-label" key="documentForm.uploadFile"/>
							<div class="controls">
								<div id="uploadDocuments">
								    <div id="clonablerow">
								   		<input type="file" name="files" id="files"/>
								   		<a href="javascript:;" id="add" onclick="cloneDiv('clonablerow')"><fmt:message key="form.add"/></a>&nbsp;&nbsp;
										<a href="javascript:;" id="delete" class="deleteDiv"><fmt:message key="form.remove"/></a>
								   	</div>
					   			</div>
							</div>
						</div>  
						

					<div class="control-group">
						<label for="viewName" class="control-label">备注</label>
						<div class="controls">
							<form:input path="bz" id="bz" />
						</div>
					</div>

					<div class="control-group">
						<label for="viewName" class="control-label">创建时间</label>
						<div class="controls">
							 <form:input path="createdTime" name="createdTime" id="createdTime" readonly="true"/>
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">最后修改时间</label>
						<div class="controls">
							<form:input path="lastModifyTime" name="lastModifyTime" id="lastModifyTime" readonly="true" />
						</div>
					</div>
					<!-- 
					<div class="control-group">
						<label for="viewName" class="control-label">是否有效</label>
						<div class="controls">
							<form:select path="isActive" items="${dicList}" itemLabel="name" itemValue="name"></form:select> 
						</div>
					</div>
					 -->
					
					
					<div class="control-group" align="center">
							<div class="form-actions no-margin pad-L0">	
								<span id="saveButtonDiv">
									<button type="submit" name="save" onclick="bCancel=false;"
										class="btn btn-primary" id="saveButton" class="cursor_pointer">
										<c:choose>
											<c:when test="${not empty alxxb.id}">
												<fmt:message key="button.update" />
											</c:when>
											<c:otherwise>
												<fmt:message key="button.save" />
											</c:otherwise>
										</c:choose>
									</button>
								</span> 
								<span id="cancelButtonDiv" class="">
									<button type="button" class="btn btn-primary" name="cancel"
										onclick="backToPreviousPage()" id="cancelButton"
										class="cursor_pointer">
										<fmt:message key="button.cancel" />
									</button>
								</span>
								<div class="clearfix"></div>
							</div>
						</div>
				 
				</form:form>
			</div>
		</div>
	</div>
</div>

<v:javascript formName="alxxb" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>