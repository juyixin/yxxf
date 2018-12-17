<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
$(function() {
	$('#from').val('${from}');
	$('#getRoles').val('${roles}');
	if('${roles}' != '' && '${roles}' != null){
		$('input:radio[id=permissionManage][value=ROLE]').attr('checked', true);
		 document.getElementById("rolesDiv").style.display = 'block';
		 
	} else if('${users}' != '' && '${users}'!= null){
		$('input:radio[id=permissionManage][value=USER]').attr('checked', true);
		document.getElementById("usersDiv").style.display = 'block';
	} else if('${groups}' != '' && '${groups}' != null){
		$('input:radio[id=permissionManage][value=GROUP]').attr('checked', true);
		document.getElementById("groupsDiv").style.display = 'block';
	}else if('${departments}' != '' && '${departments}' != null){
		$('input:radio[id=permissionManage][value=DEPARTMENT]').attr('checked', true);
		document.getElementById("departmentsDiv").style.display = 'block';
	}
	$('#users').val('${users}');
	$('#getGroups').val('${groups}');
	$('#getDepartments').val('${departments}');
	$('#usersFullName').val('${usersFullName}');
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
$( document ).ready(function() {
	$('input[type=radio]').click(function() {
	    if ($(this).val() === 'USER' && $(this).is(':checked')) {
	      
	      document.getElementById("checkedValue").value = "USER";
	      document.getElementById("roleType").value = "USER";
	      document.getElementById("usersDiv").style.display = 'block';
	      document.getElementById("rolesDiv").style.display = 'none';
	      document.getElementById("groupsDiv").style.display = 'none';
	      document.getElementById("departmentsDiv").style.display = 'none';
	    } else if ($(this).val() === 'ROLE' && $(this).is(':checked')) {
	     document.getElementById("checkedValue").value = "ROLE";
	     document.getElementById("roleType").value = "ROLE";
	     document.getElementById("rolesDiv").style.display = 'block';
	     document.getElementById("usersDiv").style.display = 'none';
	     document.getElementById("groupsDiv").style.display = 'none';
	     document.getElementById("departmentsDiv").style.display = 'none';
	     
	    } else if ($(this).val() === 'GROUP' && $(this).is(':checked')) {
	     document.getElementById("checkedValue").value = "GROUP";
	     document.getElementById("roleType").value = "GROUP";
	     document.getElementById("groupsDiv").style.display = 'block';
	     document.getElementById("usersDiv").style.display = 'none';
	     document.getElementById("rolesDiv").style.display = 'none';
	     document.getElementById("departmentsDiv").style.display = 'none';
	     
	    } else if ($(this).val() === 'DEPARTMENT' && $(this).is(':checked')) {
	     document.getElementById("checkedValue").value = "DEPARTMENT";
	     document.getElementById("roleType").value = "DEPARTMENT";
	     document.getElementById("departmentsDiv").style.display = 'block';
	     document.getElementById("groupsDiv").style.display = 'none';
	     document.getElementById("usersDiv").style.display = 'none';
	     document.getElementById("rolesDiv").style.display = 'none';
	     
	    } 
	  });
	  $('input[type=radio]').each(function() {
	    if ($(this).val() === 'USER' && $(this).is(':checked')) {
	      document.getElementById("checkedValue").value = "USER";
	      document.getElementById("roleType").value = "USER";
	      document.getElementById("usersDiv").style.display = 'block';
	      document.getElementById("rolesDiv").style.display = 'none';
	      document.getElementById("groupsDiv").style.display = 'none';
	      document.getElementById("departmentsDiv").style.display = 'none';
	    } else if ($(this).val() === 'ROLE' && $(this).is(':checked')) {
	    document.getElementById("checkedValue").value = "ROLE";
	     document.getElementById("roleType").value = "ROLE";
	     document.getElementById("rolesDiv").style.display = 'block';
	     document.getElementById("usersDiv").style.display = 'none';
	     document.getElementById("groupsDiv").style.display = 'none';
	     document.getElementById("departmentsDiv").style.display = 'none';
	     
	    } else if ($(this).val() === 'GROUP' && $(this).is(':checked')) {
	     document.getElementById("checkedValue").value = "GROUP";
	     document.getElementById("roleType").value = "GROUP";
	     document.getElementById("groupsDiv").style.display = 'block';
	     document.getElementById("usersDiv").style.display = 'none';
	     document.getElementById("rolesDiv").style.display = 'none';
	     document.getElementById("departmentsDiv").style.display = 'none';
	     
	    } else if ($(this).val() === 'DEPARTMENT' && $(this).is(':checked')) {
	     document.getElementById("checkedValue").value = "DEPARTMENT";
	     document.getElementById("roleType").value = "DEPARTMENT";
	     document.getElementById("departmentsDiv").style.display = 'block';
	     document.getElementById("groupsDiv").style.display = 'none';
	     document.getElementById("usersDiv").style.display = 'none';
	     document.getElementById("rolesDiv").style.display = 'none';
	     
	    } 
	  });

	});

function showEventSuccessMsg () {
	var msg = "";
    <c:forEach var="error" items="${successMessages}">
    	msg = msg + "<c:out value='${error}' escapeXml='false'/> <br />";
	</c:forEach>
	validateMessageBox(form.title.success, msg, "success");
	$("#popupDialog").css('min-height','0px');
	closeSelectDialog("popupDialog");
	getRolePermisisonGrid('${from}');
}

function _editDmsRolePermission(cellValue, options, rawObject){
//	var JSONUsers = JSON.parse('${usersJSON}');
	var from = $('#folders').val();
	if(from == "folder"){
		from = "folder";
	}else {
		from = "document";
	}
	if(rawObject.userFullName == null) {
		return '<a id="editRolePermission" href="#bpm/dms/showDmsRolePermission" data-role="button" data-icon="" data-theme="b" onclick="editDmsRolePermission(\''+from+'\',\''+rawObject.id+'\');"><b>'+rawObject.name+'</b></a>';
	} else {
		return '<a id="editRolePermission" href="#bpm/dms/showDmsRolePermission" data-role="button" data-icon="" data-theme="b" onclick="editDmsRolePermission(\''+from+'\',\''+rawObject.id+'\');"><b>'+rawObject.userFullName+'</b></a>';
	}
}

/*var JSONUsers = JSON.parse('${usersJSON}');
		setUserFullNames('usersFullName', '', JSONUsers);*/

</script>

<div class="container-fluid padding5">
	<div class="row-fluid">
		<div class="span12">
			<spring:bind path="dmsRolePermission.*">
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
	    	<form:form id="dmsRolePermission" commandName="dmsRolePermission"  method="post" autocomplete="off" modelAttribute="dmsRolePermission" action="bpm/dms/saveDmsRolePermission" onSubmit="_execute('popupDialog','')" class="form-horizontal no-margin">
	    		<form:hidden path="id" id="id"/>
	    		<input type="hidden" class="radio inline" id="folderId" name="folderId"/>
	    		<input type="hidden" class="radio inline" id="documentId" name="documentId"/>
	    		<input type="hidden" class="radio inline" id="from" name="from"/>
	    		<input type="hidden" class="radio inline" id="roleType" name="roleType"/>
	    		
	    		<div class="row-fluid" >
	    			<div class="span12">
		    			<div class="span3">
				    		<label class="radio inline">			    		
				    			<input type="radio"  id="permissionManage" name="permissionManage" value="USER"/>User Permission
				    		</label>
			    		</div>
			    		<div class="span3">
			    		<label class="radio inline">
			    			<input type="radio"  id="permissionManage" name="permissionManage" value="ROLE"/>Role Permission
			    		</label>
			    		</div>
			    		<div class="span3">
			    		<label class="radio inline">
				    		<input type="radio"  id="permissionManage" name="permissionManage" value="GROUP"/>Group Permission
				    	</label>
				    	</div>
				    	<div class="span3">
			    		<label class="radio inline">
				    		<input type="radio"  id="permissionManage" name="permissionManage" value="DEPARTMENT"/>Department Permission
			    		</label>
			    		</div>
			    	</div>		    		
	    		</div>
	    		
	    		<label>
	    			<input type="hidden" id="checkedValue" name="checkedValue"/>
	    		</label>
	    		
	    		<div class="row-fluid"  id="rolesDiv" style="display:none">
				  <div class="control-group">
		    			<eazytec:label styleClass="control-label" key="permission.roles"/>
		    			<div class=controls>
			    			<input type="text" name="getRoles" id="getRoles" readonly="readonly" class="span8 white-background" onclick="showRoleSelection('Role', 'multi', 'getRoles', this, '');"/>
				  		</div>
				  </div>
			  </div>
			  
			  <div class="row-fluid"  id="usersDiv" style="display:none">
				  <div class="control-group">
			    		<eazytec:label styleClass="control-label" key="permission.users"/>
		    			<div class="controls">
							<input type="hidden" name="users_FullName" id="users_FullName" class="span8 white-background" readonly="readonly" value=""/>
							<input type="hidden" name="users" id="users" class="span8 white-background" readonly="readonly" onclick="showUserSelection('User', 'multi', 'users', this, '');"/>
							<input type="text" name="usersFullName" id="usersFullName" class="span8 white-background" readonly="readonly" onclick="showUserSelection('User', 'multi',  'users', this, '');"/>				    			
						</div>
				  </div>
			  </div>
			 
			 <div class="row-fluid"  id="groupsDiv" style="display:none">
				<div class="control-group">
					<eazytec:label styleClass="control-label" key="permission.groups"/>
		    		<div class="controls">
		    			<input type="text" name="getGroups" id="getGroups" readonly="readonly" class="span8 white-background" onclick="showGroupSelection('Group', 'multi', 'getGroups', this, '');"/>
					 </div>
				</div>
			 </div>
			 <div class="row-fluid" id="departmentsDiv" style="display:none">
				  <div class="control-group" >
		    			<eazytec:label styleClass="control-label" key="permission.departments"/>
		    			<div class="controls">
			    			<input type="text" name="getDepartments" id="getDepartments" readonly="readonly" class="span8 white-background" onclick="showDepartmentSelection('Department', 'multi' , 'getDepartments', this, '');"/>
				 		</div>
				 </div>
			 </div>
			 
				<div class="row-fluid">
					<div class="span12">
				    	<div class="span2" style="margin-left:100px;">
				    		<div class="control-group">
								<c:set var="from" value='<%=request.getAttribute("from")%>'/>
				            	<c:choose>
				            		<c:when test ="${from == 'folder'}">
				                		<form:checkbox path="canEdit" id="canEdit" name="canEdit" value="true"/>
				                		<label class="checkbox inline" id="canEditLabel" for="canEditLabel"> <fmt:message key="permission.write"/></label>					            		
				            		</c:when>
				            		<c:otherwise>
				                		<form:checkbox path="canEdit" id="canEdit" name="canEdit" value="true"/>
				                		<label class="checkbox inline" id="canEditLabel" for="canEditLabel"> <fmt:message key="permission.edit"/></label>					            		
				            		</c:otherwise>
				            	</c:choose>
							</div>
				    	</div>
				    	
				    	<div class="span2">
				    		<div class="control-group">
				                <form:checkbox path="canDelete" id="canDelete" name="canDelete" value="true"/>
				                <label class="checkbox inline" id="canDeleteLabel" for="canDeleteLabel"> <fmt:message key="permission.delete"/></label>
							</div>
				    	</div>
				    	
				    	<div class="span2">
				    		<div class="control-group">
				                <form:checkbox path="canPrint" id="canPrint" name="canPrint" value="true"/>
				                <label  class="checkbox inline"  id="canPrintLabel" for="canPrintLabel"> <fmt:message key="permission.print"/></label>
							</div>
				    	</div>
				    	
				    	<div class="span2">
				    		<div class="control-group">
				                <form:checkbox path="canDownload" id="canDownload" name="canDownload" value="true"/>
				                <label  class="checkbox inline" id="canDownloadLabel" for="canDownloadLabel"> <fmt:message key="permission.download"/></label>
							</div>
				    	</div>
				    	
				    	<div class="span2">
				    		<div class="control-group">
				                <form:checkbox path="canRead" id="canRead" name="canRead" value="true"/>
				                <label  class="checkbox inline"  id="canReadLabel" for="canReadLabel"> <fmt:message key="permission.read"/></label>
							</div>
						</div>	
					</div>			    	
				   </div>
				
				 	<div class="control-group">
						<div class="form-actions no-margin pad-L0" align="center">	
							<div class="button" id="saveButtonDiv">
								<button type="submit" class="btn btn-primary" name="save" id="saveButton" onclick="return checkPermissionFieldEmpty(this);">
		                           	<fmt:message key="button.save"/>
				    			</button>
							</div>
							<div class="button" id="cancelButtonDiv">
								<button type="button" id="cancelEvent" class="btn btn-primary " name="cancel" onClick="closeSelectDialog('popupDialog')";>
		                           	<fmt:message key="button.cancel"/>
					    		</button>
							</div> 
						</div>	
					</div>
				 
			</form:form>
	    </div>
	</div>
</div>    
