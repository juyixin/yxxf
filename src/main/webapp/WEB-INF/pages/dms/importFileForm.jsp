<%@ include file="/common/taglibs.jsp"%>
<%-- <page:applyDecorator name="default"> --%>
<head>
    <title><fmt:message key="documentForm.title"/></title>
    <meta name="heading" content="<fmt:message key='documentForm.title'/>"/>
</head>
<script type="text/javascript">

	//Document Name validation 
	$("#save").click(function(event) { 
		var fileName = $('#name').val();
		var uploadedFiles = document.getElementById('file');
		if(uploadedFiles.value == ""){
			openMessageBox(form.title.error,"File Selecttion filed should not be Empty,So Please select the File to Upload","error",true);
			event.preventDefault();
		}
		//Uploaded Document Validation
		var regex= /^.*\.(jsp|html)$/i;
		for (var i = 0; i < uploadedFiles.files.length; ++i) {
			var uploadedFileName = uploadedFiles.files.item(i).name;
			if(regex.test(uploadedFileName) != true) {
				openMessageBox("Error In File Format","Invalid File Format,Select only Jsp and HTML Files","error", true);
				event.preventDefault();
			}
		}
		if(fileName = "" || fileName.length <=0) {
			openMessageBox(form.title.error,"Document Form Name should not be Empty","error", true);
		        event.preventDefault();
		}
	});
	
	
    
</script>
<div class="span12">

<div class="row-fluid">
		<div class="span12">
			<div class="titleBG">
				<span class="floatLeft fontSize14 pad-L10 pad-T10">File Information</span>
			</div>
			
			
		</div>
	</div>
<div class="span12 box">
 	<spring:bind path="importFile.name">
        <%@ include file="/common/messages.jsp" %> 
    	</spring:bind>

	<div class="span12 scroll">
	
	      
	
		<div class="row-fluid">
			  <form:form  id="importFile" commandName="importFile" method="post" action="bpm/dms/saveImportFile" autocomplete="off" modelAttribute="importFile" enctype="multipart/form-data" onSubmit="_execute('target','')">
				
			  <div class="height60"></div>
			  <div id="wizardTab-content">
					<div id="wizardTab-content-1" class="displayBlock">
						    <div class="span12">
							    <div class="row-fluid control-group">
									<div class="span2">
							    	</div>
							    	<div class="span2">
							    		<eazytec:label styleClass="control-label style3 style18" key="importFile.name"/>
							    	</div>
							    	<div class="span1">
							    	</div>
							    	<div class="span7">
							    	   <c:if test="${empty importFile.id}">
							                <form:input path="name" id="name" class="span12"/>
							                <input type="hidden" id="flag" name="flag" value="insert"/>
							            </c:if>
							            <c:if test="${not empty importFile.id}">
							            	<input type="hidden" id="id" name="id" value="${importFile.id}"/>
							            	<form:hidden path="createdBy"/>
									<form:hidden path="createdTime"/>
							            	
							                <input type="text"  id="name" name="name" readonly="false" value="${importFile.name}" class="span12"/>
							                <input type="hidden" id="flag" name="flag" value="update"/>
							            </c:if>
							    	</div>
								</div>
								
								<div class="row-fluid pad-T8">
									<div class="span2">
							    	</div>
							    	<div class="span2">
							    	</div>
							    	<div class="span1">
							    	</div>
							    	<div class="span7 control-label style3 style18">
												<fmt:message key="support.format"/> HTML and JSP Only...
							    	</div>
								</div>
								
								<div class="row-fluid control-group">
							    	<div class="span2"> </div>
							    	<div class="span2"><br>
							<c:choose>
			                            	<c:when test ="${not empty importFile}">
							    				<eazytec:label styleClass="control-label style3 style18" key="documentForm.uploadedFiles"/>
			                                </c:when>
			                                </c:choose>
							    	</div>
							    	<div class="span1"> </div>
							    	<div class="span5">
							    		<table width="100%" border="1" class="line-height46">
							    			<tr>
							    				<td>
							    					 
							    						<tr>
													   		 <td  class="uneditable-input1 pad-T8 pad-B8"><p>&nbsp;&nbsp;${importFile.resourcePath}</p></td>		
													   		 
														</tr>
																	    							
							    				</td>
							    			</tr>
							    		</table>
									</div>	
								</div>		
															
								<div class="row-fluid control-group">
									<div class="span2">
							    	</div>
							    	<div class="span2">
							    		<eazytec:label styleClass="control-label style3 style18" key="documentForm.uploadFile"/>
							    	</div>
							    	<div class="span1">
							    	</div>
							    	<div class="span7">
							    		 <table id="uploadDocuments" border="0" cellspacing="0" cellpadding="0">
											<tbody>
											    <tr id="clonablerow">
											   		 <td class="uneditable-input1"> <input type="file" name="file" id="file"/></td>
											   		
													
											   	</tr>
									   		</tbody>
									     </table>
							    	</div>
								</div>
								
								<div class="row-fluid control-group">
							    	<div class="span5">
							    	</div>
							    	<div class="span7">
					    		  		<button type="submit" class="btn btn-primary normalButton padding0 line-height0" name="save" id="save">
				                             <c:choose>
			                                     <c:when test ="${not empty importFile.id}">
			                                             <fmt:message key="button.update"/>
			                                     </c:when>
			                                     <c:otherwise>
			                                             <fmt:message key="button.save"/>
			                                     </c:otherwise>
				                             </c:choose>
			                         	</button>
							    	</div>
								</div>
							</div>
					</div>

					
					
					
				</div>
			</form:form>
		</div>
	</div>
</div>
</div>


