<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script>
function downloadDocument2(id){
    document.location.href = "bpm/alxxb/downloadDocument?id="+id;
}
</script>
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
						<a class="pad-T2 text-underline" id="editDoc" href="#bpm/alxxb/editAlxxbForm" data-role="button" data-theme="b"  onClick="_execute('user-grid','id=${alxxb.id}');"  data-icon=""><button class="btn"><fmt:message key="documentForm.edit"/></button></a>	
					</c:when>
					<c:otherwise>
						<a class="pad-T2 text-underline" id="editDoc" href="#bpm/alxxb/editAlxxbForm" data-role="button" data-theme="b"  onClick="_execute('target','id=${alxxb.id}');"  data-icon=""><button class="btn"><fmt:message key="documentForm.edit"/></button></a>	
					</c:otherwise>
				</c:choose>
				 	<a id="backToPreviousPage" class="text-underline" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="backToPreviousPage()" data-icon=""><button class="btn"><fmt:message key="退回"/></button></a> 	
			</div>
			
			<spring:bind path="alxxb.*">
       			<%@ include file="/common/messages.jsp" %>
   			</spring:bind>
   
 	<div class="row-fluid">
		<div class="span12">
			<form:form  id="alxxbFormOverview" commandName="alxxb" method="post" class="form-horizontal no-margin" action="" autocomplete="off" modelAttribute="alxxb"  onSubmit="_execute('user-grid','')">
				<form:hidden path="id"/>
				<form:hidden path="createdTime"/>
				<form:hidden path="content" id="content"/>
				<form:label path="name"></form:label>
				<div class="box_main">
				 <div class="widget">
					<div class="widget-body">
						<div class="control-group">	
							<eazytec:label styleClass="control-label" key="alxxbForm.name"/>
							<div class="controls pad-T4">
								 <label for="documentName">:&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp ${alxxb.name}</label> 
							</div>
					    </div>
					    
					   <%--  <div class="control-group">	
							<eazytec:label styleClass="control-label" key="alxxbForm.dsr"/>
							<div class="controls pad-T4">
								 <label for="dsr">:&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp ${alxxb.dsr}</label> 
							</div>
					    </div> --%>
						
						<div class="control-group">
							<eazytec:label styleClass="control-label" key="alxxbForm.content"/>
							<div class="controls pad-T4">
								<label id="documentContent" for="documentContent"></label> 
								<%-- <strong><c:out value="${documentForm.content.text()}"/></strong> --%>
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

	$(obj).printPreview('alxxbFormOverview');

$("#print-modal").click(function(){
   if($("a").hasClass("text-underline")) {		

					$("a").show();
					
				}
		
  });
}


</script>
<v:javascript formName="alxxb" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>