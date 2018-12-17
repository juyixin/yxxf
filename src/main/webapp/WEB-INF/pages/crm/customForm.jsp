<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<head>
<title><fmt:message key="newgroup.title" /></title>
<meta name="heading" content="<fmt:message key='newgroup.title'/>" />
</head>
<spring:bind path="custom.*">
	<%@ include file="/common/messages.jsp"%>
</spring:bind>

<div class="page-header">
	<div class="pull-left">
		<h2>新增/编辑客户信息</h2>
	</div>

	<div align="right"><a class="padding10" style="text-decoration: underline;" id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="backToPreviousPage()" data-icon=""><button class="btn"><fmt:message key="button.back"/></button></a> </div>
</div>

<div class="span12">
	<div class="row-fluid">
		<div class="widget-body">
			<div class="box_main">
				<form:form id="custom" commandName="custom" method="post"
					class="form-horizontal no-margin" action="/bpm/crm/saveCustom"
					autocomplete="off" modelAttribute="custom"
					onSubmit="_execute('target','');">
					
					<form:hidden path="id" />
					<form:hidden path="createdTime" />
					
					<div class="control-group">
						<label for="viewName" class="control-label">客户编号<span class="required">*</span></label>
						<div class="controls">
							<form:input path="num" id="num" class="span6" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">来源</label>
						<div class="controls">
							<form:select path="source" items="${dicList}" itemLabel="name" itemValue="name"></form:select>
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">所属客户</label>
						<div class="controls">
							<form:radiobutton path="belong" value="私有" label="私有"/>
							<form:radiobutton path="belong" value="公共" label="公共"/>
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">客户名称<span class="required">*</span></label>
						<div class="controls">
							<form:input path="name" id="name" class="span6" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">手机</label>
						<div class="controls">
							<form:input path="phone" id="phone" class="span6" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">邮箱</label>
						<div class="controls">
							<form:input path="email" id="email" class="span6" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">地址</label>
						<div class="controls">
							<form:input path="address" id="address" class="span6" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">重要程度</label>
						<div class="controls">
							<form:select path="degree" items="${deList}" itemLabel="name" itemValue="name"></form:select>
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">简介</label>
						<div class="controls">
							<form:textarea path="profile" id="profile" class="span6" />
						</div>
					</div>
					
					
					
					<div class="control-group">
						<div class="form-actions no-margin">
							<button type="submit" class="btn btn-primary " name="save"
								onclick="bCancel=false;" id="saveButton"><i class="icon-ok icon-white"></i>
										<c:choose>
									<c:when test="${empty custom.id}">
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