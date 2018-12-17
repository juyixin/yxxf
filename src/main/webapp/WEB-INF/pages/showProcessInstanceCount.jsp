<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%-- <page:applyDecorator name="default"> --%>

<head>
    <title><fmt:message key="userList.title"/></title>
    <meta name="heading" content="<fmt:message key='user.manageUsers'/>"/>
</head>

<%@ include file="/common/messages.jsp" %>

<script type="text/javascript">
function flowStatisticsGridExport(gridId,gridType){
    //Get All Grid Columns
    var columnNames = $("#_"+gridId).jqGrid('getGridParam','colNames');
    
    //Get all group header column names
    var groupHeaderColumns=new Array();  
    var groupHeadersOptions = $("#_"+gridId).jqGrid("getGridParam", "groupHeader");
	for(var i=0;i<groupHeadersOptions.groupHeaders.length;i++){
		groupHeaderColumns.push(groupHeadersOptions.groupHeaders[i].titleText);
	}
	
    //Get All Grid Datas 
    var data =  $("#_"+gridId).jqGrid('getGridParam','data');
    var gridjsonRowCollection = JSON.stringify(data);
    
    //Get grid data map key 
    var gridDataKey=new Array();  
    var mymodel = $("#_"+gridId).getGridParam('colModel'); // this get the colModel array
    // loop the array and get the hideen columns
	 $.each(mymodel, function(i) {
		 if(this.name != 'cb') {
			 gridDataKey.push(this.name);
		 }
	 });
	//Calling grid data export by POST method (Using form submit)
	var path = "bpm/manageProcess/flowStatisticsGridExport";
	var params = {gridHeader:columnNames,gridDataKey:gridDataKey,groupHeader:groupHeaderColumns,gridData:gridjsonRowCollection,gridType:gridType};
	creatAndSubmitDynamicForm(path,params,"POST");
}
</script>
     <div id="data-details" class="floatLeft department-list-users">
		<div id="header_links" align="right" style="display:none;">
<!-- 			<span class="simple-search pad-R10">Search </span><input type="text" name="searchBox" id="searchBox" onkeyup="_triggerSimpleSearch(this);"/>
 -->			<a style="padding: 3px;" id="search" href="javascript:void(0);" data-role="button" data-theme="b" onClick="showAdvancedSearchPage();" data-icon=""><button class="btn">高级查询</button></a>
				<a style="padding: 3px;" id="export" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="flowStatisticsGridExport('Flow_Statistics','flowStatistics')"  data-icon=""><button class="btn">导出CSV</button></a>
		</div>
		<div id="gridRecordDetails"></div>
	</div>
	<div id="user-grid" >
		<%= request.getAttribute("script") %>
	</div>
<script type="text/javascript">
$('#grid_header_links').html($('#header_links').html());

function showAdvancedSearchPage(){
	_execute('popupDialog', '');
	openSelectDialogForFixedPosition("popupDialog","","800","300","Advance Search");
	document.location.href = "#/bpm/processInstance/showAdvanceSearch";
}

function _triggerSimpleSearch(searchBoxObj){
	var advanceSearchParamsMap = "";
    advanceSearchParamsMap = advanceSearchParamsMap + "{'startDate':'',";
    advanceSearchParamsMap = advanceSearchParamsMap + "'finishDate':'',";
    advanceSearchParamsMap = advanceSearchParamsMap + "'departmentId':'',";
    advanceSearchParamsMap = advanceSearchParamsMap + "'processName':'"+$(searchBoxObj).val()+"',";
    advanceSearchParamsMap = advanceSearchParamsMap + "'isAdvancedSearch':'false'}";
    var searchParams = "[" + advanceSearchParamsMap + "]";  
    reloadProcessFlowStatiticsGrid(searchParams);
}
</script>							