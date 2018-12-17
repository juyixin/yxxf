<%@ include file="/common/taglibs.jsp"%>
<%-- <page:applyDecorator name="default"> --%>
<head>
    <title><fmt:message key="newrole.title"/></title>
    <meta name="heading" content="<fmt:message key='newrole.title'/>"/>
     <style type="text/css">
     .jstree-default a .jstree-icon { background-position:display:none; }
     </style>
</head>
<c:set var="delObject" scope="request"><fmt:message key="roleList.role"/></c:set>
<script type="text/javascript">var msgDelConfirm =
          "<fmt:message key="delete.confirm"><fmt:param value="${delObject}"/></fmt:message>";
          function addFormParams(method){
                  method=method+"=true";
                  _execute('target',method);
          }
          
</script>

<div class="page-header">
	<div class="pull-left">
		<h2><fmt:message key="role.heading"/></h2>
	</div>
	<spring:bind path="role.*">
        <%@ include file="/common/messages.jsp" %>
    </spring:bind>
<div align="right"><a class="padding10" style="text-decoration: underline;" id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="backToPreviousPage()" data-icon=""><button class="btn"><fmt:message key="button.back"/></button></a> </div>
</div>
	<div class="row-fluid">
	<div class="span12">
				<div class="widget-body">
					<div class="box_main">
					<form:form  id="role" commandName="role" method="post" class="form-horizontal no-margin" action="bpm/admin/saveRole" autocomplete="off" modelAttribute="role" onSubmit="_execute('target','')">
						<form:hidden path="id"/>
				    	<form:hidden path="version"/>
				    	<form:hidden path="orderNo"/>
				    	
			    		<div class="control-group">
			    			<eazytec:label styleClass="control-label" key="role.name"/>
							<div class="controls">
								<c:if test="${role.version == 0}">
				            		<form:input path="name" id="name" class="span6" />
								</c:if>
				            	<c:if test="${role.version != 0}">
				               	 	<form:input path="name" id="name" class="span6" readonly="true"/>
				            	</c:if>
							</div>
						</div>
						
						<div class="control-group">
							<eazytec:label styleClass="control-label" key="group.viewName" />
							<div class="controls">
								<form:input path="viewName" id="viewName" class="span6" />
							</div>
						</div>
						
						<div class="control-group">
			    			<eazytec:label styleClass="control-label" key="role.type"/>
							<div class="controls">
								<form:select path="roleType" name="roleType" id="roleType" class="span6">
									<option value="Public" ${fn:contains(role.roleType, 'Public') ? 'selected' : ''}><fmt:message key="role.type.public"/></option>
									<option value="Department" ${fn:contains(role.roleType, 'Department') ? 'selected' : ''}><fmt:message key="role.type.department"/></option>
								</form:select>
							</div>
						</div>
						
						<div class="control-group" id="deptdiv">
			    			<eazytec:label styleClass="control-label" key="role.roleDepartment"/>
							<div class="controls">
								<form:input type="hidden" path="roleDepartment" id="roleDepartment"/>
								<input type="text" id="roleDepartmentFullName" class="span6 white-background" onclick="showDepartmentSelection('Department', 'single' , 'roleDepartment', this, '',true);" readonly="true"/>
							</div>
						</div>
						
						<div class="control-group">
			    			<eazytec:label styleClass="control-label" key="role.decsription"></eazytec:label>
							<div class="controls">
								<form:textarea path="description" class="span6" rows="5"/>
							</div>
						</div>
						
						<div class="control-group">
							<div class="form-actions no-margin">
								<button type="submit" class="btn btn-primary" name="save" onclick="bCancel=false;" id="saveButton"><i class="icon-ok icon-white"></i>
                                 	<c:choose>
                                        <c:when test ="${role.version != 0}">
                                        	<fmt:message key="button.update"/>
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:message key="button.save"/>
                                        </c:otherwise>
                                 	</c:choose>     	
                            	</button>
                            	<button type="button" class="btn btn-primary " name="next" onclick="backToPreviousPage()" id="cancelButton" style="cursor: pointer;"><i class="icon-remove icon-white"></i>
			        				<fmt:message key="button.cancel"/>
			        			</button>
								<div class="clearfix"></div>
							</div>
						</div>
						<input type="hidden" id="departmentAdminRoleType" name="roleType"/> 
			    	</form:form>
			    	</div>
			</div>
		</div>
		</div>
<script type="text/javascript">
$("#departmentAdminRoleType").attr("disabled", "disabled");

var userRole=$("#currentUserRoles").val().split(",");
var isAdmin=checkRoles(userRole,"ROLE_ADMIN");

if(!isAdmin){
	if(${isRoleEdit}){
		if(${isDepartmentAdmin}){
			$("#roleType").val('Department');
			$("#roleType").attr("disabled", "disabled");
			$("#departmentAdminRoleType").removeAttr("disabled");
			$("#departmentAdminRoleType").val('Department');
		}else{
			$("#roleType").val('');
			$("#roleType").attr("disabled", "disabled");
			$("#saveButton").attr("disabled", "disabled");
		}		
	}else{
		$("#saveButton").attr("disabled", "disabled");
	}
	
}

function disableDepartment(){
	var roleType = document.getElementById("roleType");
	var roleTypeValue = roleType.options[roleType.selectedIndex].value;
	if(roleTypeValue == "Department"){
		$("#deptdiv").show();
		$("#roleDepartment").removeAttr("disabled");
	}else{
		$("#roleDepartment").val('');
		$("#roleDepartment").attr("disabled", "disabled");	
		$("#deptdiv").hide();		
	}
}

$('#roleType').change(function() {
	disableDepartment();
});

$('#roleDepartment').keypress(function(e) {
    var a = [];
    var k = e.which;
    
    for (i = 0; i < 1; i++)
        a.push(i);
    
    if (!(a.indexOf(k)>=0))
        e.preventDefault();
   
});

$(function () {
	disableDepartment();
	
	var JSONDepartments = JSON.parse('${departmentsJSON}');
	setUserFullNames("roleDepartmentFullName",$('#roleDepartment').val(),JSONDepartments);
});

</script>
<v:javascript formName="role" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
