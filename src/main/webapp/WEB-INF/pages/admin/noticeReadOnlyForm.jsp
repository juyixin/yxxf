<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%-- <head>
    <title>通知公告</title>
</head> --%>
 	<div class="row-fluid">
 	<div class="box_main" style="padding-top: 10px">
		<div class="widget">
			<div class="widget-body">
				<form:form  id="noticeForm" commandName="noticeForm"  autocomplete="off" modelAttribute="noticeForm" enctype="multipart/form-data">
				  	<form:hidden path="id"/>
				  	<form:hidden path="content" id="content"/>
						<div>
							<div class="form-horizontal no-margin" id="general">

								<div class="control-group">
									<label for="title" class="control-label">标题:</label>
									<div class="controls pad-T4">
										<label for="title">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<b> ${noticeForm.title}</b></label>
									</div>
								</div>
								<div class="control-group">
										<label for="documentContent" class="control-label">内容:</label>
										<div class="controls pad-T4">
											<label id="documentContent" for="documentContent"></label> 
										</div> 
								</div>
								
								<div class="control-group">
									<c:choose>
		                            	<c:when test ="${not empty noticeForm.documents}">
						    				<label for="documentContent" class="control-label">附件:</label> 
		                                </c:when>
		                         	</c:choose>
									<div class="controls pad-T4">
										<c:forEach items="${noticeForm.documents}" varStatus="loop" var="document">
											<div id="${document.id}" name="${document.id}">
												<div>
													<label style="display:inline-block;width:220px">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp${document.name}</label>	
								   		 			<a style="padding-left:90px" href="javascript:;" onclick="downloadNoticeDoc('${document.id}')" >&nbsp&nbsp下载</a>		
								   		 		</div>
								   		 	</div>		
										</c:forEach>	
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
	var documentContent = content.substring(3,content.length);
	documentContent = "<p>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"+documentContent;
	$('#documentContent').html(documentContent);
});
</script>
