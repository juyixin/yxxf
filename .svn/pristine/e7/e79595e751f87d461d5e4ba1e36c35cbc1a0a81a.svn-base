<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<head>
<title><fmt:message key="newgroup.title" /></title>
<meta name="heading" content="<fmt:message key='newgroup.title'/>" />
</head>

<spring:bind path="chance.*">
	<%@ include file="/common/messages.jsp"%>
</spring:bind>

<div class="page-header">
	<div class="pull-left">
		<h2>新增/编辑记录</h2>
	</div>

	<div align="right"><a class="padding10" style="text-decoration: underline;" id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="backToPreviousPage()" data-icon=""><button class="btn"><fmt:message key="button.back"/></button></a> </div>
</div>

<div class="span12">
	<div class="row-fluid">
		<div class="widget-body">
			<div class="box_main">
				<form:form id="chance" commandName="chance" method="post"
					class="form-horizontal no-margin" action="/bpm/crm/saveChance"
					autocomplete="off" modelAttribute="chance"
					onSubmit="_execute('target','');">
					
					<form:hidden path="id" />
					<form:hidden path="createdTime" />
					
					<div class="control-group">
						<label for="viewName" class="control-label">客户姓名<span class="required">*</span></label>
						<div class="controls">
							<form:input path="customerName" id="customerName" class="span6" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">销售阶段</label>
						<div class="controls">
							<form:select path="salesStage" items="${dicList}" itemLabel="name" itemValue="name"></form:select> 
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">重要程度</label>
						<div class="controls">
							<form:select path="importance" items="${deList}" itemLabel="name" itemValue="name"></form:select> 
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">预计销售额</label>
						<div class="controls">
							<form:input path="estimatedSales" id="estimatedSales" class="span6" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">成功可能性</label>
						<div class="controls">
							<form:select path="success">
								<form:option value="" label="请选择"/>
								<form:option value="20%" label="20%"/>
								<form:option value="30%" label="30%"/>
								<form:option value="40%" label="40%"/>
								<form:option value="50%" label="50%"/>
								<form:option value="60%" label="60%"/>
								<form:option value="70%" label="70%"/>
								<form:option value="80%" label="80%"/>
								<form:option value="90%" label="90%"/>
							</form:select> 
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">细节</label>
						<div class="controls">
							<form:textarea path="detail" id="detail" class="span6" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">分析</label>
						<div class="controls">
							<form:textarea path="analysis" id="analysis" class="span6" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">销售策略</label>
						<div class="controls">
							<form:textarea path="salesStrategy" id="salesStrategy" class="span6" />
						</div>
					</div>
					
					
					<div class="control-group">
						<div class="form-actions no-margin">
							<button type="submit" class="btn btn-primary " name="save"
								onclick="bCancel=false;" id="saveButton"><i class="icon-ok icon-white"></i>
								<c:choose>
									<c:when test="${empty chance.id}">
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