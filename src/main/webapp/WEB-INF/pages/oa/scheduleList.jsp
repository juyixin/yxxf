<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>


<%-- <%@ include file="/common/messages.jsp" %> --%>
<script type="text/javascript">
$(document).ready(function(){
	 closeSelectDialog('popupDialog');
     $('#grid_header_links').html($('#header_links').html());
     $('#eventOwner').val('${pageContext.request.remoteUser}');
     $('#eventOwnerDiv').hide();
});
 
 function createNewEvent(){
	 var date = dateFormat(new Date, 'YYYY-MM-DD');
     createEvent(date, date, 'grid');
 }
 
 function showEventSuccessMsg () {
		var msg = "";
	    <c:forEach var="error" items="${successMessages}">
	    	msg = msg + "<c:out value='${error}' escapeXml='false'/> <br />";
		</c:forEach>
		validateMessageBox(form.title.success, msg, "success");
		document.location.href = "#bpm/oa/schedules";
		_execute('target','');
		//updateCalendarEvents(data);
	}
</script>
<spring:bind path="schedule.*">
				<c:if test="${not empty successMessages}">
				    <script type="text/javascript">
				    	showEventSuccessMsg();
					</script>
				 	<c:remove var="successMessages" scope="session"/> 
				</c:if>
				<c:if test="${not empty errors}">
				    <script type="text/javascript">
				    var error = "";
				    <c:forEach var="error" items="${errors}">
				    	error = error + "<c:out value='${error}' escapeXml='false'/> <br />";
					</c:forEach>
					validateMessageBox(form.title.error, error, "error");
				    </script>
				     <c:remove var="errors"/>
				</c:if>
				<c:if test="${not empty status.errorMessages}">
				   <script type="text/javascript">
				    var error = "";
				    <c:forEach var="error" items="${status.errorMessages}">
				    	error = error + "<c:out value='${error}' escapeXml='false'/> <br />";
					</c:forEach>
					validateMessageBox(form.title.error, error , "error");
				    </script>
				</c:if>
			</spring:bind>
<div class="row-fluid" id="eventOwnerDiv">
	<div class="span12">
	<center>
		<table id="schedule-filter">
			<tr>
				<td><input type="hidden" name="eventOwner" id="eventOwner" onclick="showEventOwnerTree('User','eventOwner', 'single');"></td>
			</tr>
		</table>
	</center>
	</div>
</div>

<div class="row-fluid">
	<div class="page-header">
		<div class="pull-left">
			<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2>日程管理</h2>
		</div>
		<div id="header_links" align="right" >
			<a class="padding3" id="createSchedule" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="createNewEvent();"  data-icon=""><button class="btn"><fmt:message key="button.createNew"/></button></a>
			<a class="padding3" id="deleteSchedules" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteSchedules()"  data-icon=""><button class="btn"><fmt:message key="button.delete"/></button></a>
		</div>
	</div>
</div>
<div class="row-fluid">
	<%= request.getAttribute("script") %>
</div>
