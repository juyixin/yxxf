<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<script type="text/javascript">

function query(){
	var queryTitle = $("#queryTitle").val();
	_execute('target','title='+queryTitle);
}

</script>
<%@ include file="/common/messages.jsp" %>
<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2>合同信息管理</h2>
	</div>

<div id="header_links" align="right">
	<input type="text" id="queryTitle" name="queryTitle" value="${queryTitle}" placeholder="请输入合同名称"/>
	<a class="padding3" href="#bpm/crm/contracts" onClick="query();"><button class="btn"><fmt:message key="button.search"/></button></a>
</div>
</div>
<div><%= request.getAttribute("script") %></div>

