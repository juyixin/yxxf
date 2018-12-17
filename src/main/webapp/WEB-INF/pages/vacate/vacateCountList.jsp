<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<script type="text/javascript">

function _showVacateDetailServed(cellValue, options, rawObject) {
	return '<a id="vacateType" href="#bpm/vacate/vacateCountDetail" onclick="_execute(\'target\',\'id=' +encodeURI(rawObject.name)+","+ rawObject.searchStartTime +","+ rawObject.searchEndTime +'\');"><b><u>' + rawObject.recipientName + '</u></b></a>';
}


function query(){
	var startTime=$("#startTime").val();
	var endTime=$("#endTime").val();
	if(endTime!=null&&endTime!="" && startTime!=null&&startTime!=""){
		if(endTime >= startTime){
			 var params = "startTime="+startTime+"&endTime="+endTime;
			   document.location.href="#bpm/vacate/vacateCount";
			   _execute('target', params);
		}else{
			openMessageBox(form.title.error,"时间范围结束应大于时间范围起始","error", true);
			event.preventDefault();
		}
	}else{
		var params = "startTime="+startTime+"&endTime="+endTime;
		   document.location.href="#bpm/vacate/vacateCount";
		   _execute('target', params);
	}

}

function getUnitUnionGrid(){
	var startTime=$("#startTime").val();
	var endTime=$("#endTime").val();
	if(endTime!=null&&endTime!="" && startTime!=null&&startTime!=""){
		if(endTime < startTime){
		
			openMessageBox(form.title.error,"时间范围结束应大于时间范围起始","error", true);
			event.preventDefault();
		}
	}
	$.ajaxSetup({ cache:false});
	$.ajax({
		url: 'bpm/vacate/getUnitUnionGrid',
        type: 'GET',
        dataType: 'html',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		async: false,
		data: 'startTime=' + startTime + "&endTime=" + endTime,
		success : function(response) {
			if(response.length > 0){
				$("#user-grid").html(response);
				$('#grid_header_links').html($('#header_links').html());
			}
		}
	});
}


$(document).ready(function(){
/* 	   changeWizardByTab();
	   $( '#startTime' ).datepicker({
			dateFormat: 'yy-mm-dd',
			changeYear: true,
			changeMonth: true,
			yearRange: "-120:+0",
			showOn: "button",
			buttonImage:'/images/easybpm/form/rbl/_cal.gif',
			maxDate: new Date(),
		}); */
	   
	   $("#startTime").datetimepicker({dateFormat: "yy-mm-dd",timeFormat: "HH:mm:ss"});
	
	   $("#endTime").datetimepicker({dateFormat: "yy-mm-dd",timeFormat: "HH:mm:ss"});

});

</script>
<%@ include file="/common/messages.jsp" %>
<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2>请假统计</h2>
	</div>

	<div align="right">
	 <a style="padding: 3px; "> 
		     <input name="startTime"
			id="startTime" type="text" placeholder="请选择时间范围起始"/>
			</a> 
         --
		<a style="padding: 3px; "> 
		     <input name="endTime"
			id="endTime" type="text" placeholder="请选择时间范围结束"/>
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

