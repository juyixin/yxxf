<%@ page language="java" contentType="text/html; charset=utf-8"pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function() {
	 $("#startTime").datetimepicker({dateFormat: "yy-dd-mm",timeFormat: "HH:mm:ss"});
	 $("#endTime").datetimepicker({dateFormat: "yy-dd-mm",timeFormat: "HH:mm:ss"});
})
</script>

<head>
<title><fmt:message key="newgroup.title" /></title>
<meta name="heading" content="<fmt:message key='newgroup.title'/>" />

</head>
<spring:bind path="telreg.*">
	<%@ include file="/common/messages.jsp"%>
</spring:bind>


<div class="page-header">
	<div class="pull-left">
		<h2>新增/编辑来电记录</h2>
	</div>

	<div align="right"><a class="padding10" style="text-decoration: underline;" id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="backToPreviousPage()" data-icon=""><button class="btn"><fmt:message key="button.back"/></button></a> </div>
</div>

<div class="span12">
	<div class="row-fluid">
		<div class="widget-body">
			<div class="box_main">
				<form:form id="telreg" commandName="telreg" method="post"
					class="form-horizontal no-margin" action="/bpm/crm/saveTelreg"
					autocomplete="off" modelAttribute="telreg"
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
						<label for="viewName" class="control-label">咨询内容<span class="required">*</span></label>
						<div class="controls">
							<form:radiobutton path="conDet" value="新客户咨询" label="新客户咨询"/>
               				<form:radiobutton path="conDet" value="服务支持" label="服务支持"/>
               				<form:radiobutton path="conDet" value="维修维护" label="维修维护"/>
               				<form:radiobutton path="conDet" value="客户投诉" label="客户投诉"/>
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">来电号码<span class="required">*</span></label>
						<div class="controls">
							<form:input path="telnum" id="telnum" class="span6" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">主题</label>
						<div class="controls">
							<form:input path="theme" id="theme" class="span6" />
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
							<form:input path="note" id="note" class="span6" />
						</div>
					</div>
					
				
					
					<div class="control-group">
						<div class="form-actions no-margin">
							<button type="submit" class="btn btn-primary " name="save"
								onclick="bCancel=false;" id="saveButton"><i class="icon-ok icon-white"></i>
										<c:choose>
									<c:when test="${empty telreg.id}">
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