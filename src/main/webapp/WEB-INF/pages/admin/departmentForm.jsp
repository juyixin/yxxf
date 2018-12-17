<%@ include file="/common/taglibs.jsp"%>

<%-- <page:applyDecorator name="default"> --%>

<head>
<title><fmt:message key="deparment.title" /></title>
<meta name="heading" content="<fmt:message key='deparment.title'/>" />
</head>

<spring:bind path="department.*">
	<%@ include file="/common/messages.jsp"%>
</spring:bind>

<script type="text/javascript">
	$('#isParent').change(function() {
		hideSuperDepartment();
	});

	function hideSuperDepartment() {
		if ($('#isParent').is(':checked')) {
			document.getElementById("superDepartment").value = "";
			$("#superDepartment").attr("disabled", "disabled");
		} else {
			$("#superDepartment").removeAttr("disabled");
		}
	}

	$(document).ready(
			function() {
				hideSuperDepartment();
				if ('${isDepartmentAdmin}' == 'true'
						&& !$("#isParent").is(':checked')) {
					$("#isParent").attr("disabled", "disabled");
				}
				if ('${superDepartment}' != '') {
					$("#superDepartment").val('${superDepartment}');
					$("#superDepartment").attr("readonly", "readonly");
				}
				if ('${permitToEdit}' == 'false') {
					$("#saveButton").hide();
				}
				$('#administrators').val('${administrators}');
				$('#interfacePeople').val('${interfacePeople}');
				var JSONUsers = JSON.parse('${usersJSON}');
				setUserFullNames("administratorsFullName", $('#administrators').val(), JSONUsers);
				setUserFullNames("interfacePeopleFullName", $('#interfacePeople').val(), JSONUsers);
				setUserFullNames("departmentOwnerFullName", $('#departmentOwner').val(), JSONUsers);
				
				var JSONDepartments = JSON.parse('${departmentsJSON}');
				setUserFullNames("superDepartmentFullName",$('#superDepartment').val(),JSONDepartments);
			});
</script>

<div class="page-header">	
	<h2><fmt:message key="deparment.title"/></h2>
	
	<div align="right">
		<a class="padding10" style="text-decoration: underline;"
			id="backToPreviousPage" href="javascript:void(0);" data-role="button"
			data-theme="b" onClick="backToPreviousPage()" data-icon=""><button class="btn"><fmt:message
					key="button.back" /></button></a>
	</div>
</div>
<div class="row-fluid">
	<div class="span12">
		<div class="widget-body">
			<div class="box_main">
				<form:form id="department" commandName="department" method="post"
					class="form-horizontal no-margin" action="bpm/admin/saveDepartment"
					autocomplete="off" modelAttribute="department"
					onSubmit="_execute('target','')">
					<form:hidden path="id" />
					<form:hidden path="version" />
					<form:hidden path="orderNo" />
					<form:hidden path="departmentRole" />
					<form:hidden path="departmentType" />

					<div class="control-group">
						<eazytec:label styleClass="control-label" key="department.name" />
						<div class="controls">
							<c:if test="${department.version == 0}">
								<form:input path="name" id="name" class="span6" />
							</c:if>
							<c:if test="${department.version != 0}">
								<form:input path="name" id="name" readonly="true" class="span6" />
							</c:if>
						</div>
					</div>

					<div class="control-group">
						<eazytec:label styleClass="control-label"
							key="department.viewName" />
						<div class="controls">
							<form:input path="viewName" id="viewName" class="span6" />
						</div>
					</div>

					<div class="control-group">
						<eazytec:label styleClass="control-label"
							key="department.isParent" />
						<div class="controls checkbox-label">
							<form:checkbox path="isParent" id="isParent" />
						</div>
					</div>

					<div class="control-group">
						<eazytec:label styleClass="control-label"
							key="department.superDepartment" />
						<div class="controls">
							<form:input type="hidden" path="superDepartment" id="superDepartment" />
						
							<input type="text" id="superDepartmentFullName"
								class="span6 white-background"
								onclick="showDepartmentSelection('Department', 'single' , 'superDepartment', this, '',true);"
								readonly="true" />
						</div>
					</div>

					<div class="control-group">
						<eazytec:label styleClass="control-label" key="department.owner" />
						<div class="controls">
							<form:input path="departmentOwner" type="hidden"
								name="departmentOwner" id="departmentOwner"
								class="span6 white-background"
								onclick="showUserSelection('User', 'single' , 'departmentOwner', this, '');"
								readonly="true" />
							<input name="departmentOwnerFullName" type="text"
								id="departmentOwnerFullName" class="span6 white-background"
								onclick="showUserSelection('User', 'single' , 'departmentOwner', this, '');"
								readonly="true" />
						</div>
					</div>

					<div class="control-group">
						<eazytec:label styleClass="control-label"
							key="department.administrators" />
						<div class="controls">
							<input type="hidden" id="administrators" name="administrators"
								class="span6 white-background"
								onclick="showUserSelection('User', 'single' , 'administrators', this, '');"
								readonly="readonly" /> <input type="text"
								id="administratorsFullName" name="administrators"
								class="span6 white-background"
								onclick="showUserSelection('User', 'single' , 'administrators', document.getElementById('administrators'), '');"
								readonly="readonly" />
						</div>
					</div>

					<div class="control-group">
						<eazytec:label styleClass="control-label"
							key="department.interfacePeople" />
						<div class="controls">
							<input type="hidden" id="interfacePeople" name="interfacePeople"
								class="span6 white-background"
								onclick="showUserSelection('User', 'single' , 'interfacePeople', this, '');"
								readonly="readonly" /> <input type="text"
								id="interfacePeopleFullName" name="interfacePeople"
								class="span6 white-background"
								onclick="showUserSelection('User', 'single' , 'interfacePeople', document.getElementById('interfacePeople'), '');"
								readonly="readonly" />
						</div>
					</div>

					<div class="control-group">
						<eazytec:label styleClass="control-label"
							key="department.whileDelete" />
						<div class="controls">
							<select name="userAction" id="userAction" class="span6">
								<option value="delete"
									${fn:contains(department.userAction, 'delete') ? 'selected' : ''}>
									<fmt:message key="department.action.delete" />
								</option>
								<option value="move"
									${fn:contains(department.userAction, 'move') ? 'selected' : ''}>
									<fmt:message key="department.action.move" />
								</option>
							</select>
						</div>
					</div>

					<div class="control-group">
						<eazytec:label styleClass="control-label"
							key="department.description" />
						<div class="controls">
							<form:textarea path="description" id="name" rows="4"
								class="span6" />
						</div>
					</div>

					<div class="control-group">
						<div class="form-actions no-margin">
							<button type="submit" class="btn btn-primary" name="save"
								onclick="bCancel=false;" id="saveButton"><i class="icon-ok icon-white"></i>
								<c:choose>
									<c:when test="${department.version != 0}">
										<fmt:message key="button.update" />
									</c:when>
									<c:otherwise>
										<fmt:message key="button.save" />
									</c:otherwise>
								</c:choose>
							</button>
							&nbsp;&nbsp;
							<button type="button" class="btn btn-primary cursor_pointer"
								name="next" onclick="backToPreviousPage()" id="cancelButton"><i class="icon-remove icon-white"></i>
								<fmt:message key="button.cancel" />
							</button>
							<div class="clearfix"></div>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>

<v:javascript formName="department" staticJavascript="false" />
<script type="text/javascript"
	src="<c:url value="/scripts/validator.jsp"/>"></script>
