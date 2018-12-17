<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<script type="text/javascript">

function _showVacateServed(cellValue, options, rawObject) {
	return '<a id="vacateType" href="#bpm/vacate/viewDetails" onclick="_execute(\'target\',\'id=' +encodeURI(rawObject.id)+ '\');"><b><u>' + rawObject.vacateTypeName + '</u></b></a>';
}


function query(){
	var type=$("#type").val();
	var state=$("#state").val();
	if(type=='200'){
		type="";
	}
	if(state=='200'){
		state="";
	}
	var params = "type="+encodeURI(encodeURI(type))+"&state="+encodeURI(encodeURI(state));
	document.location.href="#bpm/vacate/vacateList";
	_execute('target', params); 
}


function getUnitUnionGrid(){
	var type=$("#type").val();
	var state=$("#state").val();
	if(type=='200'){
		type="";
	}
	if(state=='200'){
		state="";
	}
	$.ajaxSetup({ cache:false});
	$.ajax({
		url: 'bpm/vacate/vacateGrridList',
        type: 'GET',
        dataType: 'html',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		async: false,
		data: 'type=' + type + "&state=" + state,
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
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2>请假汇总</h2>
	</div>

	<div align="right">
	 <a style="padding: 3px; "> 
		     <select name="type"
			id="type" style="width:200px">
				<c:forEach var="value" items="${list}">  
                <option value="${value.code}">  
                ${value.name}  
                </option>  
                </c:forEach>
		</select>
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

