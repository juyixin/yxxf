<%@ include file="/common/taglibs.jsp"%>

<%-- <page:applyDecorator name="default"> --%>

<head>
    <title><fmt:message key="newgroup.title"/></title>
    <meta name="heading" content="<fmt:message key='newgroup.title'/>"/>
</head>

<spring:bind path="group.*">
    <%@ include file="/common/messages.jsp" %>
</spring:bind>

<script type="text/javascript">
	$(function(){	
		$("#saveButton").click(function(event) { 		
			var groupName = $('#name').val();
			if(groupName == 'group' || groupName == 'Group' || groupName == 'GROUP'){
				openMessageBox(form.title.error,form.msg.groupNameNotAsGroup,"error",true);
			 	event.preventDefault();
				$('#name').val("");
					$('#name').focus();
			  	return false;			
		} else {
				return true;
		}
			
	});
	
});


	$('#isParent').change(function(){
		hideSuperGroup();
	});
		
	function hideSuperGroup(){
		if($('#isParent').is(':checked')){
			document.getElementById("superGroup").value = "";
			$("#superGroup").attr("disabled", "disabled");	
		}else{
			$("#superGroup").removeAttr("disabled");	
		}
	}
	
	$(document).ready(function() {
		hideSuperGroup();
		$('#administrators').val('${administrators}');
		$('#interfacePeople').val('${interfacePeople}');
		var JSONUsers = JSON.parse('${usersJSON}');
		setUserFullNames("administratorsFullName",$('#administrators').val(),JSONUsers);
		setUserFullNames("interfacePeopleFullName",$('#interfacePeople').val(),JSONUsers);	
		setUserFullNames("personInchargeFullName",$('#personIncharge').val(),JSONUsers);
		setUserFullNames("leaderFullName",$('#leader').val(),JSONUsers);
		
		var JSONGroups = JSON.parse('${groupsJSON}');
		setUserFullNames("superGroupFullName",$('#superGroup').val(),JSONGroups);
		
		$("#groupParentType").attr("disabled", "disabled");
	});
	
	var userRole=$("#currentUserRoles").val().split(",");
	var isAdmin=checkRoles(userRole,"ROLE_ADMIN");
	if(!isAdmin){
		if(${isGroupEdit}){
			if(${isGroupAdmin}){
				var isParentValue=$("#isParent").is(':checked');
				if(isParentValue){
					$("#administratorsFullName").attr("disabled", "disabled");
					$("#isParent").attr("disabled", "disabled");
					$("#groupParentType").removeAttr("disabled");
					$("#groupParentType").val('true');
				}else{
					$("#isParent").attr("disabled", "disabled");
				}
			}else{
				$("#saveButton").hide();
			}
		}else{
			$("#saveButton").hide();
		}
	}

</script>

<div class="page-header">
	<div class="pull-left">
		<h2><fmt:message key="group.heading"/></h2>
	</div>
	<div align="right"><a class="padding10" style="text-decoration: underline;" id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="backToPreviousPage()" data-icon=""><button class="btn"><fmt:message key="button.back"/></button></a> </div>
</div>
<div class="row-fluid">
	<div class="span12">
		<div class="widget-body">
			<div class="box_main">
				<form:form id="group" commandName="group" method="post"
					class="form-horizontal no-margin" action="/bpm/admin/saveGroup"
					autocomplete="off" modelAttribute="group"
					onSubmit="_execute('target','');">
					<form:hidden path="id" />
					<form:hidden path="version" />
					<form:hidden path="orderNo" />
					<form:hidden path="groupRole" />

					<div class="control-group">
						<eazytec:label styleClass="control-label" key="group.name" />
						<div class="controls">
							<c:if test="${group.version == 0}">
								<form:input path="name" id="name" class="span6" />
							</c:if>
							<c:if test="${group.version != 0}">
								<form:input path="name" id="name" readonly="true" class="span6" />
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
						<eazytec:label styleClass="control-label" key="group.isParent" />
						<div class="controls checkbox-label">
							<form:checkbox path="isParent" id="isParent" />
						</div>
					</div>

					<div class="control-group">
						<eazytec:label styleClass="control-label" key="group.superGroup" />
						<div class="controls">
							<form:input type='hidden' path="superGroup" id="superGroup" name="superGroup" />
							<input type='text' id="superGroupFullName"
								class="span6 white-background"
								onclick="showGroupSelection('Group', 'single' , 'superGroup', this, '',true);"
								readonly="readonly" />
						</div>
					</div>

					<div class="control-group">
						<eazytec:label styleClass="control-label"
							key="group.personIncharge" />
						<div class="controls">
							<form:input type='hidden' path="personIncharge"
								id="personIncharge" class="medium white-background"
								onclick="showUserSelection('User', 'single' , 'personIncharge', this, '')"
								readonly="true" />
							<input type="text" id="personInchargeFullName"
								class="span6 white-background"
								onclick="showUserSelection('User', 'single' , 'personIncharge', this, '')"
								readonly="true" />
						</div>
					</div>

					<div class="control-group">
						<eazytec:label styleClass="control-label" key="group.leader" />
						<div class="controls">
							<form:input path="leader" id="leader" type='hidden'
								class="medium white-background"
								onclick="showUserSelection('User', 'single' , 'leader', this, '')"
								readonly="true" />
							<input type="text" id="leaderFullName"
								class="span6 white-background"
								onclick="showUserSelection('User', 'single' , 'leader', this, '')"
								readonly="true" />
						</div>
					</div>

					<div class="control-group">
						<eazytec:label styleClass="control-label"
							key="group.administrators" />
						<div class="controls">
							<input type='hidden' id="administrators" name="administrators"
								class="medium white-background"
								onclick="showUserSelection('User', 'multi' , 'administrators', this, '');"
								readonly="readonly" /> <input type="text"
								id="administratorsFullName" name="administratorsFullName"
								class="span6 white-background"
								onclick="showUserSelection('User', 'multi' , 'administrators', document.getElementById('administrators'), '');"
								readonly="readonly" />
						</div>
					</div>

					<div class="control-group">
						<eazytec:label styleClass="control-label"
							key="group.interfacePeople" />
						<div class="controls">
							<input type="hidden" id="interfacePeople" name="interfacePeople"
								class="medium white-background"
								onclick="showUserSelection('User', 'multi' , 'interfacePeople', this, '');"
								readonly="readonly" /> <input type="text"
								id="interfacePeopleFullName" name="interfacePeople"
								class="span6 white-background"
								onclick="showUserSelection('User', 'multi' , 'interfacePeople', document.getElementById('interfacePeople'), '');"
								readonly="readonly" />
						</div>
					</div>

					<div class="control-group">
						<eazytec:label styleClass="control-label" key="group.decsription" />
						<div class="controls">
							<form:textarea path="description" id="description" rows="4"
								class="span6" />
						</div>
					</div>

					<div class="control-group">
						<eazytec:label styleClass="control-label" key="group.remark" />
						<div class="controls">
							<form:textarea path="remark" id="remark" rows="4" class="span6" />
						</div>
					</div>

					<div class="control-group">
						<div class="form-actions no-margin">
							<button type="submit" class="btn btn-primary " name="save"
								onclick="bCancel=false;" id="saveButton"><i class="icon-ok icon-white"></i>
								<c:choose>
									<c:when test="${group.version != 0}">
										<fmt:message key="button.update" />
									</c:when>
									<c:otherwise>
										<fmt:message key="button.save" />
									</c:otherwise>
								</c:choose>
							</button>
							&nbsp;&nbsp;
							<button type="button" class="btn btn-primary" name="next"
								onclick="backToPreviousPage()" id="cancelButton"
								class="cursor_pointer"><i class="icon-remove icon-white"></i>
								<fmt:message key="button.cancel" />
							</button>
							<div class="clearfix"></div>
						</div>
					</div>
					<input type="hidden" id="groupParentType" name="isParent" />
				</form:form>
			</div>
		</div>
	</div>
</div>


<%-- </page:applyDecorator> --%>
<v:javascript formName="group" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
