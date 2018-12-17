<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<head>
<title><fmt:message key="newgroup.title" /></title>
<meta name="heading" content="<fmt:message key='newgroup.title'/>" />
</head>

<spring:bind path="classification.*">
	<%@ include file="/common/messages.jsp"%>
</spring:bind>

<div class="page-header">
	<div class="pull-left">
		<h2>新增/编辑流程分类</h2>
	</div>

	<div align="right"><a class="padding10" style="text-decoration: underline;" id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="backToPreviousPage()" data-icon=""><button class="btn"><fmt:message key="button.back"/></button></a> </div>
</div>

<div class="span12">
	<div class="row-fluid">
		<div class="widget-body">
			<div class="box_main">
				<form:form id="classification" commandName="classification" method="post"
					class="form-horizontal no-margin" action="/bpm/manageProcess/saveClassification"
					autocomplete="off" modelAttribute="classification"
					onSubmit="_execute('target','');">
					
					<form:hidden path="id" />
					<form:hidden path="orderNo" />
					
					<div class="control-group">
						<label for="viewName" class="control-label">名称<span class="required">*</span></label>
						<div class="controls">
							<form:input path="name" id="name" class="span6" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">描述</label>
						<div class="controls">
							<form:textarea path="description" id="description" class="span6" />
						</div>
					</div>
					
					<div class="control-group">
						<div class="form-actions no-margin">
							<button type="submit" class="btn btn-primary " name="save"
								onclick="bCancel=false;" id="saveButton"><i class="icon-ok icon-white"></i>
								<c:choose>
									<c:when test="${empty classification.id}">
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