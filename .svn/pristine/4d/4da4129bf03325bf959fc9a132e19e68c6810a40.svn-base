<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>

<head>
<title></title>

</head>
<%@ include file="/common/taglibs.jsp"%>

<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png "
			style="float: left; margin: 9px 6px 5px 5px" />
		<h2 style="margin: 2px;">
			<fmt:message key="列表信息" />
		</h2>

	</div>

	<div class="height10"></div>
	<div id="header_links" align="right">
		<a style="padding: 3px; ">
		 <input id="searchText"
			type="text" placeholder="输入事件名称、当事人、处理人姓名" style="width: 225px"></input>
		</a>		
		<input type="button" onclick="onFtxxInfoSearch()" value="查询"/>    		
	</div>
</div>
<div><%=request.getAttribute("script")%></div>

<script>
function onFtxxInfoSearch(){
	var searchText=$("#searchText").val();
	var params = "searchText="+encodeURI(encodeURI(searchText));
	document.location.href="#bpm/admin/evidenceList";
	_execute('target', params);
}


/* function getDetail(cellValue, options, rawObject) {
	var params = 'id=' + rawObject.id;
		$("#evidenceDetailInfo").dialog("destroy");
		document.location.href = "#bpm/admin/evidenceDetail";
 		_execute('evidenceDetailInfo', params);
 		$myDialog = $("#evidenceDetailInfo");
 		$myDialog.dialog({
 			 width: 'auto',
             height: 'auto',
             top: '10px',
             modal: true,
             resizable:false,
             position: [($(window).width()-1000)/2, ($(window).height()-300)/2],
 			title : '详情'
 		});
 		$myDialog.dialog("open");
} */

</script>