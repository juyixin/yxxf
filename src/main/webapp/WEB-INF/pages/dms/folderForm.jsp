<%@ include file="/common/taglibs.jsp"%>
<%-- <page:applyDecorator name="default"> --%>
<%-- <head>
    <title><fmt:message key="folder.title"/></title>
    <meta name="heading" content="<fmt:message key='folder.title'/>"/>
</head> --%>

<script type="text/javascript">
$(function() {
	 var folderId = "<c:out value='${folderId}' escapeXml='false'/>";
	var parentFolder = $('#parentFolder').val();
	 if(folderId != "" && '${mode}' == "CREATE") {			
			var childNodes = JSON.parse('${childNodes}');				
			$("#folderTree").jstree("create", $("#"+parentFolder) , false,childNodes, false, true);		
			$('#folderTree').jstree('deselect_all');       
			$('#folderTree').jstree('select_node', '#'+folderId);				
			$('#folderTree').jstree('deselect_node', '#'+parentFolder);					
				//	console.log($("#folderTree").jstree("get_selected").text());
				//	$('#folderTree').jstree('deselect_node', '#ff8081813e78c109013e78c35b7f0000');
			editFolder(folderId);
		} 
	changeWizardByTab();
	$('#userSelection').val('${userSelection}');
	$('#roles').val('${roles}');
	$('#groups').val('${groups}');
	$('#departments').val('${departments}');
	/* if('${canAccess}' == true || '${canAccess}' == 'true' ){
		$('#canAccess').prop('checked', true);
	} */
	var width = $("#user-grid").width();
	var height = $("#user-grid").height();
	$("#role-permission-grid").css('height',parseInt(height)- 250);
	$("#role-permission-grid").css('width',parseInt(width)- 46);
	if($('#id').val() == null || $('#id').val() == ''){
		$('#wizardTab-content-3').hide();
		
	}
});

$('#ownerFullName').val('${ownerFullName}');   
</script>
<%-- <div class="span12 box">
	<spring:bind path="folder.*">
        <%@ include file="/common/messages.jsp" %>
    </spring:bind>

	<div class="span11 scroll">
		<!--<div id="user_permission_create_links" align="right" style="display:none">
            <a style="padding:10px;" href="#bpm/dms/showDmsUserPermission" onClick="showDmsUserPermission('folder');" ><fmt:message key="button.createNew"/></a>
            <a style="padding:10px;" href="#" onClick="deletePermission('user','folder');" ><fmt:message key="button.delete"/></a>
        </div>-->
        
        <div id="role_permission_create_links" align="right" style="display:none">
            <a style="padding:10px;" href="#bpm/dms/showDmsRolePermission" onClick="showDmsRolePermission('folder');" ><fmt:message key="button.createNew"/></a>
            <a style="padding:10px;" href="#" onClick="deletePermission('role','folder');" ><fmt:message key="button.delete"/></a>
        </div>
        
      <!--  <div id="group_permission_create_links" align="right" style="display:none">
            <a style="padding:10px;" href="#bpm/dms/showDmsGroupPermission" onClick="showDmsGroupPermission('folder');" ><fmt:message key="button.createNew"/></a>
             <a style="padding:10px;" href="#" onClick="deletePermission('group','folder');" ><fmt:message key="button.delete"/></a>
        </div>-->
        
		<div class="wizard-steps pad-L200 ">
			<ul id="wizardTabs">
				<div class="active-step"><a href="#" id="wizardTab-1" class="fontSize13">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="folder.general"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></div>
				<!--<div class="completed-step"><a href="#" id="wizardTab-2" class="fontSize13" onclick="changeHeaderLink('role')">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="folder.user.permissions"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></div>-->
				<div class="completed-step"><a href="#" id="wizardTab-3" class="fontSize13" onclick="changeHeaderLink('role')">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="folder.role.permissions"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></div>
				<!--<div class="completed-step"><a href="#" id="wizardTab-4" class="fontSize13" onclick="changeHeaderLink('group')">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="folder.group.permissions"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></div>-->
			</ul>
		</div>
		<div class="table-create-user">
			<form:form  id="folder" commandName="folder" method="post" action="bpm/dms/savefolder" autocomplete="off" modelAttribute="folder" onSubmit="_execute('user-grid','')">
				<input type="hidden" id="deletingRows" name="deletingRows"></input>
				<input type="hidden" id="folders" value="folder">
			    <form:hidden path="id"/>
			    <form:hidden path="createdBy"/>
			    <form:hidden path="modifiedBy"/>
			    <form:hidden path="createdTime"/>
			    <form:hidden path="modifiedTime"/> 
			    <form:hidden path="hasChildren"/> 
			    <form:hidden path="parentFolder"/>
			    <form:hidden path="path"/> 
			    
				<div class="height60"></div>
				<div id="wizardTab-content">
					<div id="wizardTab-content-1" class="displayBlock">
						    <div class="span12">
							    <div class="row-fluid control-group">
							    	<div class="span2">
							    		<eazytec:label styleClass="control-label style3 style18" key="folder.name"/>
							    	</div>
							    	<div class="span2">
							    	</div>
							    	<div class="span8">
							    		 <c:if test="${empty folder.id}">
							                <form:input path="name" id="name" class="medium"/>
										</c:if>
										<c:if test="${not empty folder.id}">
										    <form:input path="name" id="name" readonly="true" class="medium"/>
										</c:if>
							    	</div>
								</div>
								
								<div class="row-fluid control-group">
							    	<div class="span2">
							    		<eazytec:label styleClass="control-label style3 style18" key="folder.owner"/>
							    	</div>
							    	<div class="span2">
							    	</div>
							    	<div class="span8">
							    		<form:input path="owner" id="owner" class="span6 white-background" onclick="showUserSelection('User', 'single' , 'owner', this, '');" readonly="true"/>
									<!--<form:input path="owner" id="ownerFullName" class="span6 white-background" onclick="showUserSelection('User', 'single' , 'owner', this, '');" readonly="true"/>-->
							    	</div>
								</div>
								
								<div class="row-fluid control-group">
							    	<div class="span5">
							    	</div>
							    	<div class="span7">
					    		  		<button type="submit" class="btn btn-primary normalButton padding0 line-height0" name="save" id="save">
				                             <c:choose>
			                                     <c:when test ="${not empty folder.id}">
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

					<!--<div id="wizardTab-content-2" class="displayNone">
					    <div class="span12">
					    	<div class="row-fluid control-group">
								<div id="user-permission-grid">
									<%= request.getAttribute("userPermissionScrip") %>
								</div>
							</div>
						</div>
					</div>-->
					
				 	<div id="wizardTab-content-3" class="displayNone">
					    <div class="span12">
					    	<div class="row-fluid control-group">
								<div id="role-permission-grid">
									<%= request.getAttribute("rolePermissionScript") %>
								</div>
							</div>
						</div>
					</div>
					
					<!--<div id="wizardTab-content-4" class="displayNone">
					    <div class="span12">
					    	<div class="row-fluid control-group">
								<div id="group-permission-grid">
									<%= request.getAttribute("groupPermissionScript") %>
								</div>
							</div>
						</div>
					</div>-->
					
				</div>
				
				
				<div class="row-fluid control-group">
				</div>
				<div class="row-fluid control-group">
				    <div class="span12">
						
					</div>
		    	</div>
			</form:form>
		</div> 
	</div>
</div> --%>

<div class="span12">
	<spring:bind path="folder.*">
        <%@ include file="/common/messages.jsp" %>
    </spring:bind>
  <!--<div id="user_permission_create_links" align="right" style="display:none">
        <a class="padding" href="#bpm/dms/showDmsUserPermission" onClick="showDmsUserPermission('folder');" ><fmt:message key="button.createNew"/></a>
        <a class="padding" href="#" onClick="deletePermission('user','folder');" ><fmt:message key="button.delete"/></a>
    </div>-->
    
   <%--  <div id="role_permission_create_links" align="right" style="display:none">
         <a style="padding:10px;" href="#bpm/dms/showDmsRolePermission" onClick="showDmsRolePermission('folder');" ><fmt:message key="button.createNew"/></a>
         <a style="padding:10px;" href="#" onClick="deletePermission('role','folder');" ><fmt:message key="button.delete"/></a>
    </div> --%>
    
  <!--  <div id="group_permission_create_links" align="right" class="display_none">
        <a class="padding" href="#bpm/dms/showDmsGroupPermission" onClick="showDmsGroupPermission('folder');" ><fmt:message key="button.createNew"/></a>
         <a class="padding" href="#" onClick="deletePermission('group','folder');" ><fmt:message key="button.delete"/></a>
    </div>--> 
    <div class="widget">
		<div class="widget-body">
			<form:form  id="folder" commandName="folder" method="post" action="bpm/dms/savefolder" autocomplete="off" modelAttribute="folder" onSubmit="_execute('user-grid','')">
				<input type="hidden" id="deletingRows" name="deletingRows"></input>
				<input type="hidden" id="folders" value="folder">
			    <form:hidden path="id"/>
			    <form:hidden path="createdBy"/>
			    <form:hidden path="modifiedBy"/>
			    <form:hidden path="createdTime"/>
			    <form:hidden path="modifiedTime"/> 
			    <form:hidden path="hasChildren"/> 
			    <form:hidden path="parentFolder"/>
			    <form:hidden path="path"/> 
			    
			    <div id="wizard">
					<ol>
						<li><fmt:message key="folder.general"/></li>
						<li><fmt:message key="folder.permissions"/></li>
					</ol>
					
					<div class="well">
						<div class="form-horizontal no-margin">
							
							<div class="control-group">
								<eazytec:label styleClass="control-label" key="folder.name"/>
								<div class="controls">
									<c:if test="${empty folder.id}">
						                <form:input path="name" id="name" class="span6"/>
									</c:if>
									<c:if test="${not empty folder.id}">
									    <form:input path="name" id="name" readonly="true" class="span6"/>
									</c:if>
								</div>
							</div>
							
							<div class="control-group">
								<eazytec:label styleClass="control-label" key="folder.owner"/>
								<div class="controls">
									<form:hidden path="owner" id="owner" class="span6 white-background" onclick="showUserSelection('User', 'single' , 'owner', this, '');" readonly="true"/>
 									<input  id="ownerFullName" class="span6 white-background" onclick="showUserSelection('User', 'single' , 'owner', this, '');" readonly="true"/>
 								</div>
							</div>
							
							<div class="control-group">
								<div class="controls">
									 <button type="submit" class="btn btn-primary" name="save" id="save">
			                             <c:choose>
		                                     <c:when test ="${not empty folder.id}">
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
							
					<div class="well">
						<div class="form-horizontal no-margin">
							<c:if test="${folder.id != null && folder.id != ''}">
								 <div id="role_permission_create_links" align="right">
		         					<a class="padding5 a" href="#bpm/dms/showDmsRolePermission" onClick="showDmsRolePermission('folder');" ><fmt:message key="button.createNew"/></a>
		         					<a class="padding5 a" href="#" onClick="deletePermission('role','folder');" ><fmt:message key="button.delete"/></a>
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
	var wizardHeight = parseInt(newHeight) - 190;
	/* $('#wizard').css({height: wizardHeight, overflow: 'auto'}); */
</script>
