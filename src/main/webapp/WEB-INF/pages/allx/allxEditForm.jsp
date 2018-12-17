<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function() {
	 $("#createdTime").datetimepicker({dateFormat: "yy-mm-dd",timeFormat: "HH:mm:ss"});
	 $("#lastModifyTime").datetimepicker({dateFormat: "yy-mm-dd",timeFormat: "HH:mm:ss"});
})

 
function showAllxSelection(title, selectionType, appendTo, selectedValues, callAfter,isGraduation){
	//clearPreviousPopup();
	var isGraduationTree=false;
	if(isGraduation){
		isGraduationTree=true;
	}
	
	selectedValues = $("#"+appendTo).val();
	
	document.location.href = "#bpm/admin/showOrganizationTree";
	_execute('userPopupDialog','selectionType='+selectionType+'&appendTo='+appendTo+'&isGraduationTree='+isGraduationTree+'&selection=department&rootNodeURL=bpm/admin/getAllxNodes&childNodeURL=bpm/admin/getAllxChildNodes&selectedValues='+selectedValues+'&callAfter='+callAfter+'&from=&parentId=');
	$myDialog = $("#userPopupDialog");
	
	if(selectionType.toLowerCase() == "single"){
		title = "请选择案例类型(单选)";
	}
	
	if(selectionType.toLowerCase() == "multi"){
		title = "请选择案例类型(多选)";
	}
	
	$myDialog.dialog({
		width: 'auto',
		height: 'auto',
		top: '10px',
		modal: true,
		position: [($(window).width() / 3), 70],
		title: title
	});
	$myDialog.dialog("open");
}
</script>


<head>
<title><fmt:message key="newgroup.title" /></title>
<meta name="heading" content="<fmt:message key='newgroup.title'/>" />
</head>

<spring:bind path="allx.*">
	<%@ include file="/common/messages.jsp"%>
</spring:bind>

<div class="page-header">
	<div class="pull-left">
		<c:choose>
			<c:when test="${empty allx.id}">
				<h2>新增记录</h2>
			</c:when>
			<c:otherwise>
				<h2>编辑记录</h2>
			</c:otherwise>
		</c:choose>
	</div>

	<div align="right"><a class="padding10" style="text-decoration: underline;" id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="backToPreviousPage()" data-icon=""><button class="btn"><fmt:message key="button.back"/></button></a> </div>
</div>

<div class="span12">
	<div class="row-fluid">
		<div class="widget-body">
			<div class="box_main">
				<form:form id="allx" commandName="allx" method="post"
					class="form-horizontal no-margin" action="/bpm/admin/saveAllx"
					autocomplete="off" modelAttribute="allx"
					onSubmit="_execute('target','');">
					
					<form:hidden path="id" />
					
					<form:hidden path="superDepartment"  id="superDepartment" name="superDepartment"/>
					
					<div class="control-group">
						<label for="viewName" class="control-label">类型代码<span class="required">*</span></label>
						<div class="controls">
							<form:input path="lxdm" id="lxdm" required="true"/>
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">类型名称<span class="required">*</span></label>
						<div class="controls">
							<form:input path="lxmc" id="lxmc" required="true"/>
						</div>
					</div>
					<!-- 
					<div class="control-group">
						<label class="control-label">上级案例类型<span class="required">*</span></label>
						<div class="controls">
							<form:input type="hidden" path="superDepartment" id="superDepartment" required="true"/>
						
							<input type="text" id="superDepartmentFullName"
								class="span3 white-background"
								onclick="showAllxSelection('Allx', 'single' , 'superDepartment', this, '',true);"
								readonly="true" />
						</div>
					</div>
					 
				 
					
					<div class="control-group">
						<label for="viewName" class="control-label">上级案例类型</label>
						<div class="controls">
							<form:input path="superDepartment" id="superDepartment" readonly="true"/>
						</div>
					</div>
					 -->
					
					<div class="control-group">
						<label for="viewName" class="control-label">备注</label>
						<div class="controls">
							<form:input path="bz" id="bz" />
						</div>
					</div>
					  
					<div class="control-group">
						<label for="viewName" class="control-label">创建时间</label>
						<div class="controls">
							 <form:input path="CreatedTime" id="CreatedTime" value="${CreatedTime}"  readonly="true" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">最后修改时间</label>
						<div class="controls">
							 <form:input path="LastModifyTime" id="LastModifyTime" value="${LastModifyTime}"  readonly="true" />
						</div>
					</div>
				 
					<%-- 
					<div class="control-group">
						<label for="viewName" class="control-label">是否有效</label>
						<div class="controls">
							<form:select path="isActive" items="${dicList}" itemLabel="name" itemValue="name" ></form:select> 
						</div>
					</div> --%>
					
					<%--
					
					<div class="control-group">
						<label for="viewName" class="control-label">动态单选</label>
						<div class="controls">
               				<form:radiobuttons path="type" items="${dicList}" itemLabel="name" itemValue="name"/>  
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">静态下拉</label>
						<div class="controls">
							<form:select path="type">
								<form:option value="" label="请选择"/>
								<form:option value="下拉数据一" label="下拉数据一"/>
								<form:option value="下拉数据二" label="下拉数据二"/>
								<form:option value="下拉数据三" label="下拉数据三"/>
								<form:option value="下拉数据四" label="下拉数据四"/>
							</form:select> 
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">静态单选</label>
						<div class="controls">
							<form:radiobutton path="type" value="单选数据一" label="单选数据一"/>
               				<form:radiobutton path="type" value="单选数据二" label="单选数据二"/>
               				<form:radiobutton path="type" value="单选数据三" label="单选数据三"/>
               				<form:radiobutton path="type" value="单选数据四" label="单选数据四"/>
               				<form:radiobutton path="type" value="单选数据五" label="单选数据五"/>
						</div>
					</div>
					
					 --%>
					
					<div class="control-group">
						<div class="form-actions no-margin">
							<button type="submit" class="btn btn-primary " name="save"
								onclick="bCancel=false;" id="saveButton"><i class="icon-ok icon-white"></i>
								<c:choose>
									<c:when test="${empty allx.id}">
										<fmt:message key="button.save" />
									</c:when>
									<c:otherwise>
										<fmt:message key="button.update" />
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
				</form:form>
			</div>
		</div>
	</div>
</div>