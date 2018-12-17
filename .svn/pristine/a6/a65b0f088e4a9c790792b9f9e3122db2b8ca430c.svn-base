<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function() {
	renderCKEditor();
	
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
});

function renderCKEditor(){
	CKEDITOR.replace('messageBody', {
		toolbar :
			[
				{ name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
				{ name: 'links', items : [ 'Link','Unlink','Anchor' ] },
				{ name: 'insert', items : [ 'Table','HorizontalRule','Smiley','PageBreak'] },
				{ name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','-','RemoveFormat' ] },
				{ name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote'] },
				{ name: 'colors', items : [ 'TextColor','BGColor' ] },
				{ name: 'styles', items : [ 'Format','FontSize','Font' ] }
													   
			],
		width:"90%"
	});
}

function getMessageBody(){
	return CKEDITOR.instances.messageBody.getData();    
}

function checkMessage(obj){
	var type = obj.id;
	
	if(type=='sendM'){
		if($("#toUser").val() == ''){
			validateMessageBox(form.title.error, "请填写接收人！" , "error");
			return false;
		}
	}

	if(type=='isDraft' || type=='sendM'){
		if($("#messageName").val() == ''){
			validateMessageBox(form.title.error, "请填写主题！" , "error");
			return false;
		}
	}
	
	if(type=='isDraft'){
		$("#isDraft").val("true");
	}else{
		$("#isDraft").val("false");
	}
	
	$("#messageBody").val(getMessageBody());
	
	$("#sendboxForm").submit();
}

</script>

<div class="page-header">
	<div class="pull-left">
		<h2>写消息</h2>
	</div>
</div>

<div class="row-fluid">
	<div class="span12">
		<div class="widget-body">
			<div class="box_main">
				<form:form id="sendboxForm" commandName="message" modelAttribute="message" action="/bpm/message/saveMessages" method="post"
				 enctype="multipart/form-data" class="form-horizontal no-margin" onSubmit="_execute('target','');">
				 	
				 	<form:hidden path="id" />
				 	<form:hidden path="isDraft" id="isDraft" />

					<div class="control-group">
						<label class="control-label">接收人 <span class="required">*</span></label>
						<div class="controls">
							<form:input type="hidden" path="toUser" id="toUser"/>
							<input type="text" id="toUserFullName" name="toUserFullName" readonly onclick="showUserSelection('User', 'multi' , 'toUser', document.getElementById('toUser'), '')" class="span6" value="${fullnames}"/>
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label">主题 <span class="required">*</span></label>
						<div class="controls">
							<form:input path="messageName" id="messageName" class="span6" />
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label">内容 <span class="required">*</span></label>
						<div class="controls">
							<form:textarea path="messageBody" id="messageBody" class="ckeditor"/>
						</div>
					</div>
					
					<c:if test="${fn:length(message.messageFiles) gt 0}">
					<div class="control-group">
						<label class="control-label">已上传的附件</label>
						<div class="controls">
							<c:forEach items="${message.messageFiles}" var="file">
								<p>
									<a href="bpm/messages/downloadMF?mid=${file.id}">${file.fileName}</a>&nbsp;&nbsp;
									<a href="#bpm/message/deleteFileM" onclick="_execute('target','id=${message.id}&mid=${file.id}')">删除</a>
								</p>
							</c:forEach>
						</div>
					</div>
					</c:if>
					
					<div class="control-group">
						<label class="control-label">附件</label>
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
						<div class="form-actions">
							<button type="button" class="btn btn-primary" id="sendM" onclick="checkMessage(this)">发送</button>
							<button type="button" class="btn btn-primary" id="isDraft" onclick="checkMessage(this)">存草稿</button>
						</div>
					</div>

				</form:form>
			</div>
	</div>
</div>