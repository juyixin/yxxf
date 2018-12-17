<%@ include file="/common/taglibs.jsp"%>
<%-- <page:applyDecorator name="default"> --%>
<%-- <head>
    <title><fmt:message key="documentForm.title"/></title>
    <meta name="heading" content="<fmt:message key='documentForm.title'/>"/>
    <a id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="backToPreviousPage()" data-icon=""><fmt:message key="Back"/></a> 	
</head> --%>
<script type="text/javascript">

	//Document Name validation 
	$("#saveButton").click(function(event) { 
		var documentFormName = $('#name').val();
		var folderId = $('#folderId').val();
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
		documentFormName = documentFormName.trim();
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
			}
	});
	
	$(function() {
		changeWizardByTab();
		var width = $("#user-grid").width();
		var height = $("#user-grid").height();
		$("#role-permission-grid").css('height',parseInt(height)- 250);
		$("#role-permission-grid").css('width',parseInt(width)- 46);
		
		if($('#id').val() == null || $('#id').val() == ''){
			$('#wizardTab-content-2').hide();
			$('#wizardTab-content-3').hide();
			$('#wizardTab-content-4').hide();
		}
		$('#folderId').val('${folderId}');
	});

	
    
    // delete clone div in file upload
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
    
    
    var count=0;
    
    </script>

	<spring:bind path="documentForm.name">
      	<%@ include file="/common/messages.jsp" %>
  	</spring:bind>
      
 	<div class="row-fluid">
		<div class="widget">
			<div class="widget-body">
				<form:form  id="documentForm" commandName="documentForm" method="post" action="bpm/dms/saveDocumentForm" autocomplete="off" modelAttribute="documentForm" enctype="multipart/form-data" onSubmit="_execute('user-grid','')">
				  	<form:hidden path="id"/>
				  	<form:hidden path="createdBy"/>
				  	<form:hidden path="updatedBy"/>
				  	<form:hidden path="createdTime"/>
				  	<form:hidden path="modifiedTime"/>
				  	<input type="hidden" id="folderId" name="folderId"/>
				  	<div id="wizard">
						<ol>
							<li><fmt:message key="folder.general"/></li>
							<li><fmt:message key="folder.permissions"/></li>
						</ol>
						<div class="well">
							<div class="form-horizontal no-margin" id="general">
								
								<div class="control-group">
									<eazytec:label styleClass="control-label" key="documentForm.name"/>
									<div class="controls">
										<c:if test="${empty documentForm.id}">
							                <form:input path="name" id="name" class="span12"/>
							            </c:if>
							            <c:if test="${not empty documentForm.id}">
							                <form:input path="name" id="name" readonly="false" class="span12"/>
							            </c:if>
									</div>
								</div>
								
								<div class="control-group">
									<eazytec:label styleClass="control-label" key="documentForm.content"/>
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
									<c:choose>
		                            	<c:when test ="${not empty documentForm.documents}">
						    				<eazytec:label styleClass="control-label" key="documentForm.uploadedFiles"/>
		                                </c:when>
		                         	</c:choose>
									<div class="controls">
										<c:forEach items="${documentForm.documents}" varStatus="loop" var="document">
											${document.name}	
								   		 	<c:choose>
												<c:when test="${documentForm.permissionDTO.canDelete == true}">
													&nbsp;&nbsp;<a href="#bpm/dms/deleteDocumentWhileUpload" onclick="_execute('user-grid','documentFormId='+'${documentForm.id}'+'&documentId='+'${document.id}')" ><fmt:message key="documentForm.delete"/></a>				
												</c:when>
												<c:otherwise>
													<span class="fontSize11 padding"><fmt:message key="documentForm.delete"/></span>
												</c:otherwise>
											</c:choose>
										</c:forEach>	
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

								<div class="control-group" align="center">
									<div class="form-actions no-margin pad-L0">	
										<span id="saveButtonDiv">
											<button type="submit" name="save" onclick="bCancel=false;"
												class="btn btn-primary" id="saveButton" class="cursor_pointer">
												<c:choose>
													<c:when test="${not empty documentForm.id}">
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
							</div>
						</div>
						
						<div class="well">
							<div class="form-horizontal no-margin" id="permission">
								<c:if test="${documentForm.id != null && documentForm.id != ''}">
									<div id="role_permission_create_links" align="right" >
		          						<a class="padding5 a" href="#bpm/dms/showDmsRolePermission" onClick="showDmsRolePermission('document');" ><fmt:message key="button.createNew"/></a>
		          						<a class="padding6 a" href="#" onClick="deletePermission('role');" ><fmt:message key="button.delete"/></a>
		     						</div>
	     						</c:if>
								<div class="control-group" id="role-permission-grid">
									<% String msg=" ";
	                                    msg=(String) request.getAttribute("rolePermissionScript");
	                                    if(msg==null) {
	                                        msg="";
	                                    }
	                                    out.print(msg);
									%>
								</div>
							 <div class="control-group"> </div>
							</div>
						</div>
					</div>
				</form:form>
			</div>
		</div>
</div>

<script type="text/javascript">
	$("#wizard").bwizard();
	var newHeight = $("#target").height();
	$(".bwizard-buttons").remove();
	var wizardHeight = parseInt(newHeight) - 259;
 	//$('#wizard').css({height: wizardHeight, overflow: 'auto'}); 
 	$('#general').css({height: wizardHeight, overflow: 'auto'}); 
 	$('#permission').css({height: wizardHeight, overflow: 'auto'});
</script>

<v:javascript formName="documentForm" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
