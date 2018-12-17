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
	myarray.push(divid)
	$('#' + divid).remove();
	$("#deleteArray").val(myarray);
}


var count=0;

function xiazai(){
	document.getElementById("mubanid").submit();
}




</script>


      
 	<div class="row-fluid">
 	<div class="box_main" style="padding-top: 10px">
		<div class="widget">
			<div class="widget-body">
				<form:form  id="djsdForm" commandName="djsdForm" method="post" action="bpm/admin/saveNoticeForm" autocomplete="off" modelAttribute="djsdForm" enctype="multipart/form-data">
				  	<form:hidden path="id"/>
				  	<form:hidden path="content" id="content"/>
				  	<input type="hidden" id="deleteArray" name="deleteArray"/>
						<div>
							<div class="form-horizontal no-margin" id="general">					
								<div class="control-group">
									<label for="title" class="control-label">标题</label>
									<div class="controls">
										<label style="display:inline-block;width:220px">${djsdForm.title}</label>
									</div>
								</div>
								<div class="control-group">
										<label for="content" class="control-label">内容</label>
										<div class="controls pad-T4">
											<label  id="documentContent"  for="content"></label> 
										</div> 
								</div>
								
							<div class="control-group">
									<c:choose>
		                            	<c:when test ="${not empty djsdForm.documents}">
						    				<label  class="control-label">已上传文件：</label> 
		                                </c:when>
		                         	</c:choose>
									<div  class="controls">
										<c:forEach items="${djsdForm.documents}" varStatus="loop" var="document">
											<div id="${document.id}" name="${document.id}">
												<div>
													<label style="display:inline-block;width:220px">${document.name}</label>	
								   		 			<%-- <a style="padding-left:90px" href="javascript:;" onclick="deleteDocument('${document.id}')" >&nbsp&nbsp删除</a>	 --%>
				<%-- 				   		 			<a class="btn" style="width:75px;" href="javascript:;" onclick="downloadDocumentnotice('${document.id}'); return false;"><center><fmt:message key="documentForm.download"/></center></button> --%>	
								   		 			<a style="padding-left:90px" href="javascript:;" onclick="downloadDocumentnotice('${document.id}')" >下载</a>
								   		 		</div>
								   		 	</div>		
										</c:forEach>	
									</div>
								</div>
								 <div class="control-group">

								<div class="control-group" align="center">
									<div class="form-actions no-margin pad-L0">	
										<span id="cancelButtonDiv" class="">
											<button type="button" class="btn btn-primary" name="cancel"
												onclick="backToPreviousPage()" id="cancelButton"
												class="cursor_pointer">
														后退
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
<script type="text/javascript">
$(function(){
	var content = $('#content').val();
	//alert(content);
	var documentContent = content;
	/* documentContent = "<p"+documentContent; */
	$('#documentContent').html(documentContent);
});
</script>