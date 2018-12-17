<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<script type="text/javascript">

function _showEditDocumentsServed(cellValue, options, rawObject) {
	return '<a id="documnets" href="#bpm/sendDocuments/viewDetails" onclick="_execute(\'target\',\'id=' +encodeURI(rawObject.id)+ '\');"><b><u>' + rawObject.theme + '</u></b></a>';
}


function query(){
	var searchText=$("#searchText").val();
	var params = "searchText="+encodeURI(encodeURI(searchText));
	document.location.href="#bpm/sendDocuments/sendDocumentsList";
	_execute('target', params);
}


function getUnitUnionGrid(){
	var searchText=$("#searchText").val();
	
	$.ajaxSetup({ cache:false});
	$.ajax({
		url: 'bpm/sendDocuments/sendDocumentsGridList',
        type: 'GET',
        dataType: 'html',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		async: false,
		data: 'searchText=' + encodeURI(encodeURI(searchText)),
		success : function(response) {
			if(response.length > 0){
				$("#user-grid").html(response);
				$('#grid_header_links').html($('#header_links').html());
			}
		}
	});
}


</script>
<%@ include file="/common/messages.jsp" %>
<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2>文件发送管理</h2>
	</div>

	<div align="right">
	 <a style="padding: 3px; "> 
		<input id="searchText"
			type="text"placeholder="输入文件主题" style="width:300px"/>
			</a>
		<a style="padding: 3px; "> 
		<input type="button" onclick="getUnitUnionGrid()" value="查询"/>  
		</a>
	</div>
</div>
<div id="user-details" class="floatLeft department-list-users">
						<div id="user-grid">
                               	<%= request.getAttribute("script") %>
                           	</div>
                       	</div>


