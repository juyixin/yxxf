<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
$(document).ready(function() {
$.ajaxSetup({ cache:false });
	$("#eventOwner").val('${pageContext.request.remoteUser}');
	$("#eventOwnerDiv").hide();
	//var data = getEventOwnerCalendarDatas();
	var data = eval('${jsonString}');
	showCalender("calendar",data); //show the calendar
	var height = getFCHeight();
	$('#calendar').fullCalendar('option', 'height', height);
	
	if("${isAdmin}" == true || "${isAdmin}" == 'true'){
		$('#fc-header').find('tr:first td:first').append('<span class="fc-header-title margin-left55"><label for="Assigned" class="control-label" style="padding-top:2px; ">指派给</label></span><span class="fc-header-title margin-left55"><input class="" type="hidden" style="padding-top: 0px; " name="fc-eventOwner" id="fc-eventOwner" onclick="showUserSelection(\'User\', \'single\' , \'fc-eventOwner\', this, \'refetchEvents\');">	<input type="text" style="width:100px;padding-top: 0px;" name="fc-eventOwnerFullName" id="fc-eventOwnerFullName" onclick="showUserSelection(\'User\', \'single\' , \'fc-eventOwner\', this, \'refetchEvents\');">	</span>');
		$('#fc-eventOwner').val('${pageContext.request.remoteUser}');
		var JSONUsers = JSON.parse('${usersJSON}');
		setUserFullNames('fc-eventOwnerFullName', '${pageContext.request.remoteUser}', JSONUsers);
	}
});

function createNewEvent(){
	var date = dateFormat(new Date, 'YYYY-MM-DD');
    createEvent(date, date, 'calendar');
}

</script>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
			<div class="page-header">
				<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2><fmt:message key="schedule.title"/>	</h2>
			</div>
			<a class="floatRight createEvent" style="text-decoration: underline;" onclick="createNewEvent();" href="javascript:void(0);" id="createSchedule" ><button class="btn"><fmt:message key="schedule.newEvent"/></button></a>
			<spring:bind path="schedule.*">
    			<%@ include file="/common/messages.jsp" %>
    		</spring:bind>
		</div>
	</div>
	<div class="row-fluid" id="eventOwnerDiv">
		<div class="span12">
		<center>
			<table id="schedule-filter">
				<tr>
					<td><label for="Assigned"  class="control-label style3 style18">指派给</label></td>
					<td><input type="text" style="padding-top:0px;" name="eventOwner" id="eventOwner" ></td>
				</tr>
			</table>
		</center>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span12">
			<div id='calendar'> </div>
		</div>
	</div>
</div>
