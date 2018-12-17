 <%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ page import="com.eazytec.common.util.CommonUtil"%>
<%
String loggedInUserName = CommonUtil.getLoggedInUserId();
%>
<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="documentForm.title"/></title>
    <meta name="heading" content="<fmt:message key='documentForm.title'/>"/>
</head>

<div class="page-header">

<h2>流程常用意见</h2>
<spring:bind path="userOpinion.*">
	<%@ include file="/common/messages.jsp" %>
</spring:bind>
	
</div>
<div align="right"><a class="padding10" style="text-decoration: underline;" id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="showListViews('USEROPINION','用户意见');" data-icon=""><fmt:message key="button.back"/></a> 
</div>
<div class="height10"></div>
<div class="widget">
	<div class="widget-body">
		<form:form commandName="userOpinion" method="post" action="bpm/opinion/saveUserOpinion" id="userOpinion" autocomplete="off"  modelAttribute="userOpinion" enctype="multipart/form-data" cssClass="form-horizontal no-margin"  onSubmit="_execute('target','')">
			<c:if test="${not empty userOpinion.id}">
			<form:hidden path="id"/>
			</c:if>
			<form:hidden path="userId" value="<%=loggedInUserName%>"/>
			<datalist id="searchUserOpinionList"> </datalist>
			
			<div class="control-group">
				<lable class="control-label">内容</lable>
				<div class="controls">
					<%-- <form:input path="word" rows="4" class="span6" id="searchUserOpinion" onkeyup="getUserOpinion(event);" list="searchUserOpinionList" autocomplete="off" name="searchUserOpinion"/> --%>
					<form:textarea path="word" rows="4" class="span6" id="word" name="userOpinion"/>
				</div>
			</div>
			
			<div class="control-group">
				<div class="form-actions no-margin">
					<c:choose>
						<c:when test ="${userOpinion.id != null}">
							<button type="submit" class="btn btn-primary " name="save" onclick="bCancel=false;" id="save">              
								<fmt:message key="button.update"/>          	    	  
							</button>
						</c:when>
						<c:otherwise>
							<button type="submit" class="btn btn-primary " name="save" onclick="bCancel=false;" id="save">              
								<fmt:message key="button.save"/>
							</button>
						</c:otherwise>
					</c:choose>
					<button type="button" class="btn btn-primary" name="next" onclick="showListViews('USEROPINION','用户意见');" id="cancelButton" class="cursor_pointer;">
						<fmt:message key="button.cancel"/>
					</button>
					<div class="clearfix"></div>
				</div>
			</div>
		</form:form>
	</div>
</div>
