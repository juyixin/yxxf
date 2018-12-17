<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<script type="text/javascript">

function sortListViewResult(index,sortorder){
	
	var queryNum = $("#queryNum").val();

	if(index == "startTimeByString"){
		index = "startTime";
	}else if(index == "endTimeByString"){
		index = "endTime";
	}else if(index == "createdTimeByString"){
		index = "createdTime";
	}
	
	$("#_RECORD_LIST").jqGrid('clearGridData');
	$("#_RECORD_LIST").jqGrid('setGridParam',{postData: {'num':queryNum,'orderName':index,'orderType':sortorder}}).trigger('reloadGrid');
}

</script>
<%@ include file="/common/messages.jsp" %>
<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2>记录管理</h2>
	</div>

<div id="header_links" align="right">
	<input type="text" id="queryNum" name="queryNum" value="${queryNum}" placeholder="请输入编号"/>
	<a class="padding3" onClick="sortListViewResult('','');"><button class="btn"><fmt:message key="button.search"/></button></a>
</div>
</div>
<div><%= request.getAttribute("script") %></div>

