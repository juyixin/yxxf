<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function showUserFullName(username) {
	$("#userFullName").val("");

	var JSONUsers = JSON.parse('${usersJSON}');
	setUserFullNames("userFullName",username,JSONUsers);
	return $("#userFullName").val();
}
</script>
<input type="hidden" id="userFullName"/>

<div class="row-fluid">
<div class="page-header">
	<div class="pull-left">
		<h2>任务详情</h2>
	</div>
	<div id="header_links" align="right">
		<div align="right"><a class="padding3" style="text-decoration: underline;" id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="backToPreviousPage()" data-icon=""><strong>后退</strong></a></div>
	</div>
	<div style="clear:both"></div>
</div>

<div class="widget">
	<div class="widget-body">
		<div class="form-horizontal no-margin">
			<div class="title">
				<span class="fs1" aria-hidden="true"></span>
				<b><fmt:message key="task.process.details"/></b>
			</div>
			<div class="control-group">
				<eazytec:label styleClass="control-label" key="process.name" />
					<div class="controls" style="padding-top: 8px;">${process.name}</div>
			</div>
			<div class="control-group">
				<label class="control-label"><span><fmt:message key="process.description"/></span></label>
				<div class="controls" style="padding-top: 8px;">${process.description}</div>
			</div>
			<div class="title">
				<span class="fs1" aria-hidden="true"></span>
				<b><fmt:message key="task.details"/></b>
			</div>
			<div class="control-group">
				<label class="control-label"><span><fmt:message key="task.title"/></span></label>
				<div class="controls" style="padding-top: 8px;">${task.name}</div>
			</div>
			<div class="control-group">
				<label class="control-label"><span><fmt:message key="task.description"/></span></label>
				<div class="controls" style="padding-top: 8px;">${task.description}</div>
			</div>
			<div class="control-group">
				<label class="control-label"><span><fmt:message key="task.create.at"/></span></label>
				<div class="controls" style="padding-top: 8px;">${taskCreateTime}</div>
			</div>
			<!-- 
			<div class="control-group">
				<label class="control-label"><span><fmt:message key="task.members.involved"/></span></label>
				<div class="controls" style="padding-top: 8px;"><a href="#" onclick = "getUsersToInvolve('${task.id}','${involveMembers}')" class="button_link">${involveMembers}</a></div>
			</div>
			 -->
			<div class="title">
				<span class="fs1" aria-hidden="true"></span>
				<b>任务参与成员</b>
			</div>
			
			<div class="control-group">
				<%= request.getAttribute("peopleScript") %>
			</div>
			
			<div class="control-group">
				<div class="form-actions no-margin">
					<c:if test="${(isTaskFormApplicable == true) && (isTaskClaimable == false) }">
						<input type="button" class="btn btn-primary" value="办理" onclick = "showTaskFormDetail('${task.id}')">
					</c:if>
					<c:if test="${isTaskClaimable == true}">
						<input type="button" name="claim" class="btn btn-primary" onclick="claimTask(${task.id});" value="签收" >
					</c:if>
					<input type="button" name="Print" class="btn btn-primary" onclick="showPrintPreviewFormId(this,'task_detail_view');" value=<fmt:message key="documentForm.print"/> >
				</div>
			</div>
		</div>
	</div>
</div>
</div>

