<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<script type="text/javascript">

function _showGeneralProcessServed(cellValue, options, rawObject) {
	return '<a id="vacateType" href="#bpm/generalProcess/viewDetails" onclick="_execute(\'target\',\'id=' +encodeURI(rawObject.id)+ '\');"><b><u>' + rawObject.itemName + '</u></b></a>';
}


function query(){
	var name=$("#name").val();
	var state=$("#state").val();

	if(state=='200'){
		state="";
	}
	var params = "name="+name+"&state="+state;
	document.location.href="#bpm/generalProcess/generalProcessList";
	_execute('target', params);
}

function getUnitUnionGrid(){
	var name=$("#name").val();
	var state=$("#state").val();

	if(state=='200'){
		state="";
	}
	$.ajaxSetup({ cache:false});
	$.ajax({
		url: 'bpm/generalProcess/generalProcessGrridList',
        type: 'GET',
        dataType: 'html',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		async: false,
		data: "name="+name+"&state="+state,
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
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2>流程管理</h2>
	</div>

	<div align="right">
	 <a style="padding: 3px; "> 
		   <input id="name"
			type="text" placeholder="输入流程事项" style="width:250px"/>
		</a>
		<a style="padding: 3px; "> 
		     <select name="state"
			id="state" style="width:200px">
				<c:forEach var="value" items="${list2}">  
                <option value="${value.code}">  
                ${value.name}  
                </option>  
                </c:forEach>
		</select>
		</a>
		<input type="button" onclick="getUnitUnionGrid()" value="查询"/>  
		
	</div>
</div>

<div id="user-details" class="floatLeft department-list-users">
						<div id="user-grid">
                               	<%= request.getAttribute("script") %>
                           	</div>
                       	</div>


