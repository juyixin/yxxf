<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	$(function() {
		$('#from').val('${from}');
		$('#groups').val('${groups}');
		if('${from}' == 'folder'){
			hideDocumentPermissionFields();
			$('#folderId').val('${folderId}');
		} else{
			$('#documentId').val('${documentId}');
			hideFolderPermissionFields();
		}
	});
	
	$(window).resize(function() {
	    $("#popupDialog").dialog("option", "position", "center");
	});
	
	
	function showEventSuccessMsg () {
		var msg = "";
	    <c:forEach var="error" items="${successMessages}">
	    	msg = msg + "<c:out value='${error}' escapeXml='false'/> <br />";
		</c:forEach>
		validateMessageBox(form.title.success, msg, "success");
		$("#popupDialog").css('min-height','0px');
		closeSelectDialog("popupDialog");
		getGroupPermisisonGrid('${from}');
	}

</script>

<div class="container-fluid padding5">
	<div class="row-fluid">
		<div class="span12">
			<spring:bind path="dmsGroupPermission.*">
				<c:if test="${not empty successMessages}">
				    <script type="text/javascript">
				    	showEventSuccessMsg();
					</script>
				 	<c:remove var="successMessages" scope="session"/> 
				</c:if>
				<c:if test="${not empty errors}">
				    <script type="text/javascript">
				    var error = "";
				    <c:forEach var="error" items="${errors}">
				    	error = error + "<c:out value='${error}' escapeXml='false'/> <br />";
					</c:forEach>
					validateMessageBox(form.title.error, error, "error");
				    </script>
				     <c:remove var="errors"/>
				</c:if>
				<c:if test="${not empty status.errorMessages}">
				   <script type="text/javascript">
				    var error = "";
				    <c:forEach var="error" items="${status.errorMessages}">
				    	error = error + "<c:out value='${error}' escapeXml='false'/> <br />";
					</c:forEach>
					validateMessageBox(form.title.error, error , "error");
				    </script>
				</c:if>
			</spring:bind>
		</div>
	</div>
	<div class="row-fluid">
	    <div class="span12">
	    	<form:form id="dmsGroupPermission" commandName="dmsGroupPermission" method="post" autocomplete="off" modelAttribute="dmsGroupPermission" action="bpm/dms/saveDmsGroupPermission" onSubmit="_execute('popupDialog','')">
	    		<form:hidden path="id" id="id"/>
	    		<input type="hidden" id="folderId" name="folderId"/>
	    		<input type="hidden" id="documentId" name="documentId"/>
	    		<input type="hidden" id="from" name="from"/>
			    <div class="row-fluid control-group" id="groupsDiv">
			    	<div class="span2">
			    		<eazytec:label styleClass="control-label style3 style18" key="permission.groups"/>
			    	</div>
			    	<div class="span10">
			    		<input type="text" name="groups" id="groups" class="large" onclick="showGroupSelection('Group', 'multi', 'groups', this, '');"/>
			    	</div>
				</div>
				<div class="row-fluid control-group">
				    	<div class="span2">
				    		<eazytec:label styleClass="style3 style18" key="permission.permission"/>
				    	</div>
				    	<div class="span2">
				    		<div class="controls">
								<c:set var="from" value='<%=request.getAttribute("from")%>'/>
				    		       	<c:choose>
					            		<c:when test ="${from == 'folder'}">
					            			<label class="checkbox inline style3 style18">
					                		<form:checkbox path="canEdit" id="canEdit" name="canEdit" value="true"/>
					                		<label id="canEditLabel" for="canEditLabel" class="style3 style18"> <fmt:message key="permission.write"/></label>
					            			</label>					            		
					            		</c:when>
					            		<c:otherwise>
					            			<label class="checkbox inline style3 style18">
					                		<form:checkbox path="canEdit" id="canEdit" name="canEdit" value="true"/>
					                		<label id="canEditLabel" for="canEditLabel" class="style3 style18"> <fmt:message key="permission.edit"/></label>
					            			</label>					            		
					            		</c:otherwise>
					            	</c:choose>
							</div>
				    	</div>
				    	<div class="span2">
				    		<div class="controls">
					            <label class="checkbox inline style3 style18">
					                <form:checkbox path="canDelete" id="canDelete" name="canDelete" value="true"/>
					                <label id="canDeleteLabel" for="canDeleteLabel" class="style3 style18"> <fmt:message key="permission.delete"/></label>
					            </label>
							</div>
				    	</div>
				    	<div class="span2">
				    		<div class="controls">
					            <label class="checkbox inline style3 style18">
					                <form:checkbox path="canPrint" id="canPrint" name="canPrint" value="true"/>
					                <label id="canPrintLabel" for="canPrintLabel" class="style3 style18"> <fmt:message key="permission.print"/></label>
					                
					            </label>
							</div>
				    	</div>
				    	<div class="span2">
				    		<div class="controls">
					            <label class="checkbox inline style3 style18">
					                <form:checkbox path="canDownload" id="canDownload" name="canDownload" value="true"/>
					                <label id="canDownloadLabel" for="canDownloadLabel" class="style3 style18"> <fmt:message key="permission.download"/></label>
					            </label>
							</div>
				    	</div>
				    	<div class="span2">
				    		<div class="controls">
					            <label class="checkbox inline style3 style18">
					                <form:checkbox path="canRead" id="canRead" name="canRead" value="true"/>
					                <label id="canReadLabel" for="canReadLabel" class="style3 style18"> <fmt:message key="permission.read"/></label>
					            </label>
							</div>
						</div>					    	
				</div>
				 <div class="row-fluid control-group">
				 		<div class="span4">
					 	</div>
				    	<div class="span3">
							<div class="button" id="saveButtonDiv">
								<button type="submit" class="btn btn-primary normalButton padding0 line-height0" name="save" id="saveButton" onclick="return validateDmsGroupPermission(this);">
	                                         <fmt:message key="button.save"/>
				    			</button>
							</div>
						</div>
						<div class="span3">
							<div class="button" id="cancelButtonDiv">
								<button type="button" id="cancelEvent" class="btn btn-primary normalButton padding0 line-height0" name="cancel" onClick="closeSelectDialog('popupDialog')";>
	                            	<fmt:message key="button.cancel"/>
					    		</button>
							</div>
					 	</div>
					 	<div class="span2">
					 	</div>
				 </div>
			</form:form>
	    </div>
	</div>
</div>    