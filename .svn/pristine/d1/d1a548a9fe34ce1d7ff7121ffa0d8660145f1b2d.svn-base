<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function() {
	 $("#startTime").datetimepicker({dateFormat: "yy-mm-dd",timeFormat: "HH:mm:ss"});
	 $("#endTime").datetimepicker({dateFormat: "yy-mm-dd",timeFormat: "HH:mm:ss"});
	 
	 $("#startTime").datetimepicker('setDate', (new Date()));
	 
})
</script>


<head>
<title><fmt:message key="newgroup.title" /></title>
<meta name="heading" content="<fmt:message key='newgroup.title'/>" />
</head>

<spring:bind path="record.*">
	<%@ include file="/common/messages.jsp"%>
</spring:bind>

<div class="page-header">
	<div class="pull-left">
		<h2>新增/编辑记录</h2>
	</div>

	<div align="right"><a class="padding10" style="text-decoration: underline;" id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="backToPreviousPage()" data-icon=""><button class="btn"><fmt:message key="button.back"/></button></a> </div>
</div>

<div class="row-fluid">
	<div class="span12">
		<div class="widget-body">
			<div class="box_main">
				<form:form id="record" commandName="record" method="post"
					class="form-horizontal no-margin" action="/bpm/crm/saveRecord"
					autocomplete="off" modelAttribute="record"
					onSubmit="_execute('target','');">
					
					<form:hidden path="id" />
					<form:hidden path="createdTime" />
					
					<div class="control-group">
						<label for="viewName" class="control-label">编号<span class="required">*</span></label>
						<div class="controls">
							<form:input path="num" id="num" class="span6" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">记录类型</label>
						<div class="controls">
							<form:select path="type" items="${dicList}" itemLabel="name" itemValue="name"></form:select> 
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">开始时间</label>
						<div class="controls">
							 <form:input path="startTime" name="startTime" id="startTime" readonly="true" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">结束时间</label>
						<div class="controls">
							<form:input path="endTime" name="endTime" id="endTime" readonly="true" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">备注</label>
						<div class="controls">
							<form:textarea path="note" id="note" class="span6" />
						</div>
					</div>
					
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
									<c:when test="${empty record.id}">
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