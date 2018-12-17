<%@ include file="/common/taglibs.jsp"%>
<%-- <page:applyDecorator name="default"> --%>
<%-- <head>
    <title><fmt:message key="documentForm.title"/></title>
    <meta name="heading" content="<fmt:message key='documentForm.title'/>"/>
</head>


<spring:bind path="documentForm.*">
        <%@ include file="/common/messages.jsp" %>
</spring:bind>

			<div class="floatRight">
<%-- 				<a style="padding10:10px;" id="createDoc" href="#bpm/dms/documentForm" data-role="button" data-theme="b"  onClick="_execute('user-grid','');"  data-icon=""><fmt:message key="button.createNew"/></a>
 --%>				<c:choose>
					<c:when test="${documentForm.permissionDTO.canEdit == true}">
						<a class="pad-T2 text-underline" id="editDoc" href="#bpm/dms/documentForm" data-role="button" data-theme="b"  onClick="_execute('user-grid','id=${documentForm.id}');"  data-icon=""><button class="btn"><fmt:message key="documentForm.edit"/></button></a>	
					</c:when>
					<c:otherwise>
						<span  class="fontSize11 padding10"><fmt:message key="documentForm.edit"/></span>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${documentForm.permissionDTO.canDelete == true}">
						<a class="pad-T2 text-underline"  id="deleteDoc" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteDocumentForm('${documentForm.id}');"  data-icon=""><button class="btn"><fmt:message key="documentForm.delete"/></button></a>	
					</c:when>
					<c:otherwise>
						<span   class="fontSize11 padding10"><fmt:message key="documentForm.delete"/></span>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${documentForm.permissionDTO.canPrint == true}">
						<a   class="pad-T2 text-underline" id="printDoc" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="printForm(this);"  data-icon=""><button class="btn"><fmt:message key="documentForm.print"/></button></a>	
					</c:when>
					<c:otherwise>
						<span   class="pad-T2 fontSize11"><fmt:message key="documentForm.print"/></span>
					</c:otherwise>
				</c:choose>
				 	<a id="backToPreviousPage" class="text-underline" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="backToPreviousPage()" data-icon=""><button class="btn"><fmt:message key="Back"/></button></a> 	
			</div>
			
			<spring:bind path="documentForm.*">
       			<%@ include file="/common/messages.jsp" %>
   			</spring:bind>
   
 	<div class="row-fluid">
		<div class="span12">
			<form:form  id="documentFormOverview" commandName="documentForm" method="post" class="form-horizontal no-margin" action="bpm/dms/saveDocumentForm" autocomplete="off" modelAttribute="documentForm"  onSubmit="_execute('user-grid','')">
				<form:hidden path="id"/>
				<form:hidden path="createdBy"/>
				<form:hidden path="updatedBy"/>
				<form:hidden path="createdTime"/>
				<form:hidden path="content" id="content"/>
				<form:hidden path="modifiedTime"/>
				<form:label path="name"></form:label>
				<div class="box_main">
				 <div class="widget">
					<div class="widget-body">
						<div class="control-group">	
							<eazytec:label styleClass="control-label" key="documentForm.name"/>
							<div class="controls pad-T4">
								 <label for="documentName">:&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp ${documentForm.name}</label> 
							</div>
					    </div>
						
						<div class="control-group">
							<eazytec:label styleClass="control-label" key="documentForm.content"/>
							<div class="controls pad-T4">
								<label id="documentContent" for="documentContent"></label> 
								<%-- <strong><c:out value="${documentForm.content.text()}"/></strong> --%>
							</div> 
						</div>  
						
						 <div class="control-group">
							<eazytec:label styleClass="control-label" key="documentForm.uploadedFiles"/>
							<div class="controls pad-T4">
								<table width='300px' >
								<c:forEach items="${documentForm.documents}" varStatus="loop" var="document">
								<tr>
									<td width='30%'><div class="manage">${document.name}</div></td>
									<c:choose>
										<c:when test="${documentForm.permissionDTO.canDownload == true}">
											<td width='30%'><button class="btn" style="width:75px;" href="javascript:;" onclick="downloadDocument('${document.id}'); return false;"><center><fmt:message key="documentForm.download"/></center></button></td>
										</c:when>
										<c:otherwise>
											&nbsp;&nbsp;<center><fmt:message key="documentForm.download"/></center>
										</c:otherwise>
									</c:choose>	
										
									<c:choose>
										<c:when test="${documentForm.permissionDTO.canRead == true || documentForm.permissionDTO.canEdit == true }">
											<td width='30%'><button class="btn" style="width:75px;" onclick="viewDocument('${document.id}','${document.name}',''); return false;"><fmt:message key="documentForm.view"/></button></td>
										</c:when>
										<c:otherwise>
											&nbsp;&nbsp;<fmt:message key="documentForm.view"/>
										</c:otherwise>
									</c:choose>
										
									<c:choose>
										<c:when test="${documentForm.permissionDTO.canDelete == true}">
											<td width='30%'><button class="btn" style="width:75px;" onclick="document.location.href='#bpm/dms/deleteDocument'; _execute('user-grid','documentFormId='+'${documentForm.id}'+'&documentId='+'${document.id}'); return false;"><fmt:message key="documentForm.delete"/></button></td>		
										</c:when>
										<c:otherwise>
											<span  class="padding10 fontSize11"><fmt:message key="documentForm.delete"/></span>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${documentForm.permissionDTO.canPrint == true}">
											<td width='30%'><button class="btn" style="width:75px;" onClick="viewDocument('${document.id}','${document.name}','${documentForm.id}'); return false;"><fmt:message key="documentForm.print"/></button></td>
										</c:when>
										<c:otherwise>
											<span  class="padding10 fontSize11"><fmt:message key="documentForm.print"/></span>
										</c:otherwise>
									</c:choose>
									</td>
									</tr>
								</c:forEach>	
																		
									</tbody>
								</table>
								
							</div>
						</div>
					</div>
				</div>
			</div>
			</form:form>
		</div>
	</div>


<script type="text/javascript">
$(function(){
	var content = $('#content').val();
	var documentContent = content.substring(3,content.length);
	documentContent = "<p>:&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"+documentContent;
	$('#documentContent').html(documentContent);
});
function printForm(obj){

	if($("a").hasClass("text-underline")) {		
	
					$("a").hide();
					
				}

	$(obj).printPreview('documentFormOverview');

$("#print-modal").click(function(){
   if($("a").hasClass("text-underline")) {		

					$("a").show();
					
				}
		
  });
}


</script>
<v:javascript formName="documentForm" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>