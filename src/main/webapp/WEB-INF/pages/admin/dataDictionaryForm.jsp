<%@ include file="/common/taglibs.jsp"%>

<head>
<title><fmt:message key="menuList.title" /></title>
<meta name="menu" content="AdminMenu" />
</head>
<script type="text/javascript">

$.cookie('bpmSideScript',$.cookie('bpmSideAppBack'));
$(document).ready(function(){
	displaySqlTextInput();
	if(document.getElementById("id").value == ''){
		$('#isEnabled').prop('checked', true);
	}
	$('#sqlString').attr('placeholder','<fmt:message key="dataDictionary.sqlString.placeholder"/>');
});

function isDictValueEmpty(){
	if(document.getElementById("dict_type").value.toLowerCase() == "static") {
		document.getElementById("sqlString").value = "";
		if(document.getElementById("value").value.trim().length == 0) {
			validateMessageBox(form.title.error, form.msg.dictionaryValueNotEmpty , "error");	
			return false;
		}
	}else if(document.getElementById("dict_type").value.toLowerCase() == "dynamic") {
		document.getElementById("value").value = "";
		if(document.getElementById("sqlString").value.trim().length == 0) {
			validateMessageBox(form.title.error, form.msg.sqlNotEmpty , "error");	
			return false;
		}
	}
	return true;
}

function dataDictonarySubmit(){
	//Get Classification value from autocomplete and set it
	<%--var n=$(".as-values").val().split(",");
		document.getElementById("classification").value = $(".as-values").val(); --%>
	var params = "hierarchyParentId="+document.getElementById("hierarchyParentId").value;
	_execute('target',params);
}
</script>

 <div class="page-header">
	<h2>
		<fmt:message key="dataDictionary.title" />
	</h2>
</div>
<div align="right">
	<strong><a id="backToPreviousPage" class="padding10"style="text-decoration: underline;" href="javascript:void(0);" data-role="button" data-theme="b" onClick="loadDataDictionaryGrid('UserDefined');" data-icon=""><button class="btn"><fmt:message key="button.back" /></button></a> </strong>
</div>

<spring:bind path="dataDictionary.*">
        <%@ include file="/common/messages.jsp" %>
</spring:bind>
    
<div class="height10"></div>

<div class="span12">
	<div class="row-fluid">
		<div class="widget-body">
			<div class="box_main">
				<form:form commandName="dataDictionary" method="post"
					class="form-horizontal no-margin" action="bpm/admin/saveDictionary"
					autocomplete="off" modelAttribute="dataDictionary"
					id="dictionaryForm" name="dictionaryForm"
					onSubmit="dataDictonarySubmit();">
					<form:hidden path="id" />
					<form:hidden path="createdBy" />
					<form:hidden path="parentDictId" />

					<div class="control-group" id="nameDiv">
						<eazytec:label styleClass="control-label "
							key="dataDictionary.name" />
						<div class="controls ">
							<form:input path="name" id="name" class="span6" />
						</div>
					</div>
					<div class="control-group" id="codeDiv">
						<eazytec:label styleClass="control-label"
							key="dataDictionary.code" />
						<div class="controls">
							<c:choose>
								<c:when test="${mode == 'EDIT'}">
									<form:input path="code" id="code" class="span6" readonly="true" />
								</c:when>
								<c:otherwise>
									<form:input path="code" id="code" class="span6" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>

					<div class="control-group" id="descriptionDiv">
						<eazytec:label styleClass="control-label "
							key="dataDictionary.description" />
						<div class="controls">
							<form:input path="description" id="description" class="span6" />
						</div>
					</div>

					<div class="control-group" id="dictTypeDiv">

						<eazytec:label styleClass="control-label"
							key="dataDictionary.type" />
						<div class="controls">
							<form:select path="type" id="dict_type"
								class="medium mar-B0 span6" onchange="displaySqlTextInput()">

								<form:option value="static" selected="selected">
									<fmt:message key="datadictionary.static" />
								</form:option>
								<form:option value="dynamic">
									<fmt:message key="datadictionary.dynamic" />
								</form:option>
							</form:select>
						</div>
					</div>

					<div class="control-group displayNone" id="valueDiv">
						<eazytec:label styleClass="control-label"
							key="dataDictionary.value">
							<span class="color-red"> *</span>
						</eazytec:label>
						<div class="controls">
							<form:input path="value" id="value" class="span6" />
						</div>
					</div>

					<div class="control-group displayNone" id="sqlStringDiv">
						<eazytec:label styleClass="control-label "
							key="dataDictionary.sqlString"> *</eazytec:label>
						<div class="controls">
							<form:textarea path="sqlString" rows="3" id="sqlString"
								class="span6 " />
						</div>
					</div>

					<div class="control-group" id="dictStatusDiv">
						<eazytec:label styleClass="control-label "
							key="dataDictionary.isEnabled" />
						<div class="controls checkbox-label">
							<form:checkbox path="isEnabled" id="isEnabled" />
						</div>
					</div>

					<div class="control-group">
						<div class="form-actions no-margin" id="buttonDiv">
							<button type="submit" class="btn btn-primary" name="save"
								id="saveButton" onclick="return isDictValueEmpty()"><i class="icon-ok icon-white"></i>
								<c:choose>
									<c:when test="${mode == 'EDIT'}">
										<fmt:message key="button.update" />
									</c:when>
									<c:otherwise>
										<fmt:message key="button.save" />
									</c:otherwise>
								</c:choose>
							</button>
							&nbsp;&nbsp;
							<button type="button" class="btn btn-primary  cursor_pointer"
								name="next" onclick="loadDataDictionaryGrid('UserDefined');"
								id="cancelButton"><i class="icon-remove icon-white"></i>
								<fmt:message key="button.cancel" />
							</button>
							<div class="clearfix"></div>
						</div>
					</div>
					<input type=hidden id="hierarchyParentId" name="hierarchyParentId"
						value='<%=request.getAttribute("hierarchyParentId")%>' />
				</form:form>
			</div>
		</div>
	</div>
</div>