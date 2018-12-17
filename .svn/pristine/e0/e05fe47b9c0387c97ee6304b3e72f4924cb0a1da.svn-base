<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%-- <head>
    <title><fmt:message key="documentForm.title"/></title>
    <meta name="heading" content="<fmt:message key='documentForm.title'/>"/>
    <a id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="backToPreviousPage()" data-icon=""><fmt:message key="Back"/></a> 	
</head> --%>
<script type="text/javascript"> 
var myarray= new Array();
//Document Name validation 
$("#saveButton").click(function(event) { 
	var documentFormName = $('#title').val();
	var uploadedFiles = document.getElementById('files');
	var regex= /^.*\.(txt|html|csv|doc|rtf|docx|xls|ppt|pptx|odp|ods|odt|jpg|png|gif|bmp|pdf|xml|xlsx)$/i;
	for (var i = 0; i < uploadedFiles.files.length; ++i) {
	  var uploadedFileName = uploadedFiles.files.item(i).name;
		if(regex.test(uploadedFileName) != true) {
			openMessageBox(form.title.msgBoxTitleError,form.error.invalidFileFormat,"error", true);
			event.preventDefault();
		}
	}
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

function deleteDocument(divid){
	myarray.push(divid);
	$('#' + divid).remove();
	$("#deleteArray").val(myarray);
}


var count=0;

function xiazai(){
	document.getElementById("mubanid").submit();
}

function downloadzcfg(id){
    document.location.href = "bpm/admin/downloadzcfgdy?id="+id;
}


function renderCKEditor(){
	CKEDITOR.replace('content', {
		/* fullPage : true, 
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
    	$("#dataType").val('${noticeForm.dataType}')
    	$("#comments").val('${noticeForm.comments}')
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


      
 	<div class="row-fluid">
 	<div class="box_main" style="padding-top: 10px">
		<div class="widget">
			<div class="widget-body">
				<form:form  id="zcfgForm" commandName="zcfgForm" method="post" action="bpm/admin/saveZcfgForm" autocomplete="off" modelAttribute="zcfgForm" enctype="multipart/form-data">
				  	<form:hidden path="id"/>
				  	<input type="hidden" id="deleteArray" name="deleteArray"/>
						<div>
							<div class="form-horizontal no-margin" id="general">					
								<div class="control-group">
									<label for="title" class="control-label">标题</label>
									<div class="controls">
										<form:input  path="title"  id="title" name="title" rows="6" required="true" style="width:600px"></form:input>
									</div>
								</div>
								<div class="control-group">
										<label for="content" class="control-label">内容</label>
										<div  class="controls">	
											<div id="ckEditor" style="width:700px">
											<form:textarea  class="span8" path="content" id="content" name="content" rows="10"></form:textarea>
											
										</div>
									</div>
								</div>
								
						< 		<div class="control-group">
									<c:choose>
		                            	<c:when test ="${not empty zcfgForm.documents}">
						    				<label  class="control-label">已上传文件：</label> 
		                                </c:when>
		                         	</c:choose>
									<div  class="controls">
										<c:forEach items="${zcfgForm.documents}" varStatus="loop" var="document">
											<div id="${document.id}" name="${document.id}">
												<div>
													<label style="display:inline-block;width:220px">${document.fileName}</label>	
								   		 			<a style="padding-left:90px" href="javascript:;" onclick="deleteDocument('${document.id}')" >&nbsp&nbsp删除</a> 
								   		 			<a style="padding-left:90px" href="javascript:;" onclick="downloadzcfg('${document.id}')">下载</a>
											</div>
								   		 	</div>		
										</c:forEach>	
									</div>
								</div> 
								
								<div class="control-group">
									<label class="control-label">上传文件：</label> 
									<div class="controls">
										<div id="uploadDocuments">
										    <div id="clonablerow">
										   		<input type="file" name="files" id="files"/>
										   		<a href="javascript:;" id="add" onclick="cloneDiv('clonablerow')">新增</a>&nbsp;&nbsp;
												<a href="javascript:;" id="delete" class="deleteDiv">删除</a>
										   	</div>
							   			</div>
									</div>
								</div> 
								
								
								 <div class="control-group">

								<div class="control-group" align="center">
									<div class="form-actions no-margin pad-L0">	
										<span id="saveButtonDiv">
											<button type="submit" name="save" onclick="bCancel=false;"
												class="btn btn-primary" id="saveButton" class="cursor_pointer">
												保存
											</button>
										</span> 
										<span id="cancelButtonDiv" class="">
											<button type="button" class="btn btn-primary" name="cancel"
												onclick="backToPreviousPage()" id="cancelButton"
												class="cursor_pointer">
														取消
											</button>
										</span>
										<div class="clearfix"></div>
									</div>
								</div>
							</div> 
						</div>
				</form:form>
			</div>
		</div>
	</div>
</div>