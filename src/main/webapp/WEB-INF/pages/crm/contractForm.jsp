<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function() {
	 $("#signingTime").datetimepicker({dateFormat: "yy-dd-mm",timeFormat: "HH:mm:ss"});
	
})
</script>

<head>
<title><fmt:message key="newgroup.title" /></title>
<meta name="heading" content="<fmt:message key='newgroup.title'/>" />
</head>

<spring:bind path="contract.*">
	<%@ include file="/common/messages.jsp"%>
</spring:bind>

<div class="page-header">
	<div class="pull-left">
		<h2>新增合同信息记录</h2>
	</div>

	<div align="right"><a class="padding10" style="text-decoration: underline;" id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="backToPreviousPage()" data-icon=""><button class="btn"><fmt:message key="button.back"/></button></a> </div>
</div>

<div class="span12">
	<div class="row-fluid">
		<div class="widget-body">
			<div class="box_main">
				<form:form id="contract" commandName="contract" method="post"
					class="form-horizontal no-margin" action="/bpm/crm/saveContract"
					autocomplete="off" modelAttribute="contract"
					onSubmit="_execute('target','');">
					
					<form:hidden path="id" />
					<form:hidden path="createdTime" />
					
					<div class="control-group">
						<label for="viewName" class="control-label">收支类型</label>
						<div class="controls">
							<form:radiobutton path="szlx" value="收入类" label="收入类"/>
               				<form:radiobutton path="szlx" value="支出类" label="支出类"/>
               				<form:radiobutton path="szlx" value="描述类" label="描述类"/>
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">合同类型</label>
						<div class="controls">
							<form:select path="type" items="${dicList}" itemLabel="name" itemValue="name"></form:select>
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">合同名称<span class="required">*</span></label>
						<div class="controls">
							<form:input path="title" id="title" class="span6" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">合同编号</label>
						<div class="controls">
							<form:input path="num" id="num" class="span6" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">客户签约人</label>
						<div class="controls">
							<form:input path="contractor" id="contractor" class="span6" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">签约时间</label>
						<div class="controls">
							<form:input path="signingTime" name="signingTime" id="signingTime" readonly="true" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">合同金额</label>
						<div class="controls">
							<form:input path="amount" id="amount" class="span6" />
						</div>
					</div>
					
					<div class="control-group">
						<label for="viewName" class="control-label">合同内容</label>
						<div class="controls">
							<form:input path="content" id="content" class="span6" />
						</div>
					</div>
					
					
	
					
					<div class="control-group">
						<div class="form-actions no-margin">
							<button type="submit" class="btn btn-primary " name="save"
								onclick="bCancel=false;" id="saveButton"><i class="icon-ok icon-white"></i>
										<c:choose>
									<c:when test="${empty contract.id}">
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