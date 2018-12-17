<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	$(function() {
	$.ajaxSetup({ cache:false });
		var startDate = $('#startDate').val();
		var endDate = $('#endDate').val();
		var eventType = $('#eventType').val();
		var date = new Date();
		var month = date.getMonth()+1;
		var day = date.getDate();
		var currentDate = date.getFullYear() + '-' +
	    (month<10 ? '0' : '') + month + '-' +
	    (day<10 ? '0' : '') + day;
		
		showRepeats($('#eventType').val());
		
		if(eventType == 'once' || startDate >= currentDate){
			loadDateField('startDate');			
		}
		loadDateField('endDate');
		loadTimeField('startTime');
		loadTimeField('endTime');
		$('#users').val('${users}');
		var a = $('#users').val();
		var JSONUsers = JSON.parse('${usersJSON}');
		setUserFullNames("usersFullName",$('#users').val(),JSONUsers);
		setUserFullNames("creator",$('#createdBy').val(),JSONUsers);
		/* if('${isAdmin}' != true && '${isAdmin}' != 'true'){
			$('#users').val('${pageContext.request.remoteUser}');
			$('#usersDiv').hide();
		} */
		
		 if($('#createdBy').val() != '${pageContext.request.remoteUser}' ){
			$("#updateButton").attr("disabled","disabled");
			$("#deleteButton").attr("disabled","disabled");
		} 
		
		
		
		// Disabling update button in gridview
		
		if(eventType == 'once'){
			if(startDate < currentDate){
				$("#saveButton").attr("disabled","disabled"); 
	  		}
		} else {
			if(startDate < currentDate){
				$("#startDate").attr("readonly", true); 
	  		}
			if(currentDate > endDate){ 
				$("#saveButton").attr("disabled","disabled"); 
	  		}	
		}
		
		
	});
	
	$(window).resize(function() {
	    $("#popupDialog").dialog("option", "position", "center");
	});
	
	$("#remindBefore").keypress(function (event){
		if (event.which != 8 && event.which != 0 && event.which != 46 && (event.which < 47 || event.which > 59))
	    {
	        event.preventDefault();
	        if ((event.which == 46) && ($(this).indexOf('.') != -1)) {
	            event.preventDefault();
	        }
	    }
	});
	
	function showEventSuccessMsg () {
		var msg = "";
	    <c:forEach var="error" items="${successMessages}">
	    	msg = msg + "<c:out value='${error}' escapeXml='false'/> <br />";
		</c:forEach>
		validateMessageBox(form.title.success, msg, "success");
		$("#popupDialog").css('height','0px');
		closeSelectDialog("popupDialog");
		var data = eval('${jsonString}');
		$('#calendar').fullCalendar('removeEvents');
		$('#calendar').fullCalendar( 'refetchEvents' );
		//updateCalendarEvents(data);
	}

	$('div#popupDialog').bind('dialogclose', function(event) {
	   // $( "#remindTime" ).datepicker( "hide" );
	    $( "#startTime" ).datepicker( "hide" );
	    $( "#endTime" ).datepicker( "hide" );
	});
	
	function deleteSchedule(){
		var eventId = $('#id').val();
		var eventOwner = $('#eventOwner').val();
		deleteScheduledEvent(eventId,eventOwner);
	}
	
	//Check the Event form
	
	$( "#data_dictionary" ).each(function( index ) {
	
	var element_id = this.getAttribute("id");
	var parent_dictId = "ff808181456e21bc01456e26a31f0001";
	if('${schedule.id}'!= ''){
		addOptionValToDictionary(element_id,parent_dictId);
		document.getElementById("data_dictionary").value = '${schedule.scheduleEventId}';
	}else{
		addOptionValToDictionary(element_id,parent_dictId);
	}
 
});

	if('${schedule.scheduleEventId}' != '' && '${schedule.scheduleEventId}' != null ) {

		document.getElementById("data_dictionary").value = '${schedule.scheduleEventId}';
	}

	function checkValue(){
	   if($('#data_dictionary option:selected').val()){
		    	document.getElementById("scheduleEventName").value = $('#data_dictionary option:selected').text() ;	
	    }
	}						
	
</script>

<div class="container-fluid padding5">
	 
			<spring:bind path="schedule.*">
				<c:if test="${not empty successMessages}">
					<script type="text/javascript">
				    	showEventSuccessMsg();
					</script>
					<c:remove var="successMessages" scope="session" />
				</c:if>
				<c:if test="${not empty errors}">
					<script type="text/javascript">
				    var error = "";
				    <c:forEach var="error" items="${errors}">
				    	error = error + "<c:out value='${error}' escapeXml='false'/> <br />";
					</c:forEach>
					validateMessageBox(form.title.error, error, "error");
				    </script>
					<c:remove var="errors" />
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
		<div class="widget">
			<div class="widget-body">
				<form:form id="schedule" commandName="schedule" method="post" autocomplete="off" class="form-horizontal no-margin" modelAttribute="schedule" action="bpm/oa/saveEvent" onSubmit="_execute('popupDialog','')" >
					<form:hidden path="id" id="id" />
					<form:hidden path="version" id="version" />
					<form:hidden path="createdBy" id="createdBy" />
					<form:hidden path="updatedBy" id="updatedBy" />
					<form:hidden path="createdTime" id="createdTime" />
					<form:hidden path="updatedTime" id="updatedTime" />
					<form:hidden path="from" id="from" />
					
					<div class="row-fluid">
						<div class="span12">
							
							<div class="control-group">
								<eazytec:label styleClass="control-label" key="schedule.creator" />
								<div class="controls">
									<input type="text" readonly="true" name="creator" id="creator" class="span12" />
								</div>
							</div>
							
							<div class="control-group" id="usersDiv">
								<label class="control-label">指派给</label>
								<div class="controls">
									<input type="hidden" name="users" id="users" class="span12" onclick="showUserSelection('User', 'multi', 'users', this, '');" />
									<input type="text" name="usersFullName" id="usersFullName" class="span12" onclick="showUserSelection('User', 'multi', 'users',document.getElementById('users'), '');" />	
								</div>
							</div>
			
							<div class=" control-group" id="eventNameDiv">
								<eazytec:label styleClass="control-label" key="schedule.eventName" />
								<div class="controls">
									<form:input path="eventName" name="eventName" id="eventName" class="span12" />
								</div>
							</div>
				
							<div class="row-fluid">
								<div class="span12">
									<div class="span6">
										<div class="control-group" >
											<eazytec:label styleClass="control-label" key="schedule.location" />
											<div class="controls">
												<form:input path="location" name="location" id="location" class="span12" />
											</div>
										</div>
									</div>
									<div class="span6">
										<div class="control-group">
											<eazytec:label styleClass="control-label" key="schedule.scheduleEventType" />
											<div class="controls">
												<form:select path="scheduleEventId" name="data_dictionary" id="data_dictionary" class="span12" onChange="checkValue()" />
												<form:hidden path="scheduleEventName" name="scheduleEventName" id="scheduleEventName" value=""/>							
											</div>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row-fluid">
							<div class="span12">
								<div class="span6">
									<div class="control-group" id="dateDiv">
										<eazytec:label styleClass="control-label" key="schedule.startDate" />
										<div class="controls">
											<form:input path="startDate" name="startDate" id="startDate" class="span12" readonly="true" />
										</div>
									</div>
								</div>
								<div class="span6">
									<div class=" control-group" id="timeDiv">
										<eazytec:label styleClass="control-label" key="schedule.startTime" />
										<div class="controls">
											<form:input path="startTime" name="startTime" id="startTime" class="span12" readonly="true" />
										</div>
									</div>
								</div>
							</div>
							</div>
							
							<div class="row-fluid">
							<div class="span12">
								<div class="span6">
									<div class="control-group">
										<eazytec:label styleClass="control-label" key="schedule.endDate" />
										<div class="controls">
											<form:input path="endDate" name="endDate" id="endDate" class="span12" readonly="true" />
										</div>
									</div> 
								</div>
								<div class="span6">
									<div class="control-group">
										<eazytec:label styleClass="control-label" key="schedule.endTime" />
										<div class="controls">
											<form:input path="endTime" name="endTime" id="endTime" class="span12" readonly="true" />
										</div>
									</div>
								</div>
							</div>
							</div>
							
							<div class="row-fluid">
								<div class="span12">
									<div class="span6">
										<div id="typeDiv" class="control-group" >
											<eazytec:label styleClass="control-label" key="schedule.repeats" />
											<div class="controls">
												<form:select path="eventType" name="eventType" id="eventType"
													class="span12" onchange="showRepeats(this.value)">
													<form:option value="once"><fmt:message key="show.once"/></form:option>
													<form:option value="daily"><fmt:message key="show.daily"/></form:option>
													<form:option value="weekly"><fmt:message key="show.weekly"/></form:option>
													<form:option value="monthly"><fmt:message key="show.monthly"/></form:option>
												</form:select>
											</div>
										</div>	
									</div>
									<div class="span6">
										<div class="control-group" >
											<eazytec:label styleClass="control-label" key="schedule.remindBefore" />
											<div class="controls">
												<form:input path="remindBefore" name="remindBefore" id="remindBefore" class="span3" />
												<form:select path="remindTimeType" name="remindTimeType" id="remindTimeType" class="span9">
													<form:option value="minutes"><fmt:message key="reminderBefore.minutes"/></form:option>
													<form:option value="hours"><fmt:message key="reminderBefore.hours"/></form:option>
													<form:option value="days"><fmt:message key="reminderBefore.days"/></form:option>
													<form:option value="weeks"><fmt:message key="reminderBefore.weeks"/></form:option>
													<form:option value="months"><fmt:message key="reminderBefore.months"/></form:option>
												</form:select>
											</div>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row-fluid" id="dailyRepeatDiv">
								<div class="span12">
									<div class="span6">
										<div class="control-group" >
											<eazytec:label styleClass="control-label" key="schedule.repeatEvery" />
											<div class="controls">
												<form:select path="repeatEvery" name="repeatEvery"
													id="repeatEveryDays" class=" span12">
													<c:forEach begin="1" end="30" varStatus="loop">
														<option value="${loop.index}" ${schedule.repeatEvery == loop.index ? 'selected' : ''}>${loop.index}</option>
													</c:forEach>
												</form:select>
<%-- 												<eazytec:label styleClass="form-label" key="schedule.days" /> --%>
											</div>
										</div>
									</div>
									<div style="float:left;">
										<eazytec:label styleClass="form-label" key="schedule.days" />
									</div>
								</div>
							</div>
							
							
							<div class="row-fluid" id="weeklyRepeatDiv">
								<div class="span12">
									<div class="row-fluid">
										<div class="span6">
											<div class="control-group">
												<eazytec:label styleClass="control-label" key="schedule.repeatEvery" />
												<div class="controls">
														<form:select path="repeatEvery" name="repeatEvery" id="repeatEveryWeekDays" class="margin-bottom0 span12">
															<c:forEach begin="1" end="52" varStatus="loop">
																<option value="${loop.index}" ${schedule.repeatEvery == loop.index ? 'selected' : ''}>${loop.index}</option>
															</c:forEach>
														</form:select>
<%-- 													<eazytec:label styleClass="form-label" key="schedule.weeks" /> --%>
												</div>
											</div>
										</div>
										<div style="float:left;">
											<eazytec:label styleClass="form-label" key="schedule.weeks" />
										</div>
									</div>
									<div class="row-fluid">
										<div class="control-group">
											<eazytec:label styleClass="control-label" key="schedule.repeatOn" />
											<div class="controls">
												<div class="span3">
													<label class="checkbox inline"> <form:checkbox path="days.sunday" id="sunday" value="true" /> <fmt:message key="schedule.sunday" />
													</label>
												</div>
												<div class="span3">
													<label class="checkbox inline"> <form:checkbox path="days.monday" id="monday" value="true" /> <fmt:message key="schedule.monday" />
													</label>
												</div>
												<div class="span3">
													<label class="checkbox inline style3 style18"> <form:checkbox path="days.tuesday" id="tuesday" value="true" /> <fmt:message key="schedule.tuesday" />
													</label>
												</div>
												<div class="span3">
													<label class="checkbox inline"> <form:checkbox path="days.wednesday" id="wednesday" value="true" /> <fmt:message key="schedule.wednesday" />
													</label>
												</div>
											</div>
										</div>
									</div>
									<div class="row-fluid">
										<div class="control-group">
											<div class="controls">
												<div class="span3">
													<label class="checkbox inline"> <form:checkbox path="days.thursday" id="thursday" value="true" /> <fmt:message key="schedule.thursday" />
													</label>
												</div>
												<div class="span3">
													<label class="checkbox inline"> <form:checkbox path="days.friday" id="friday" value="true" /> <fmt:message key="schedule.friday" />
													</label>
												</div>
												<div class="span3">
													<label class="checkbox inline"> <form:checkbox path="days.saturday" id="saturday" value="true" /> <fmt:message key="schedule.saturday" />
													</label>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							
							<div class="row-fluid" id="monthlyRepeatDiv">
								<div class="span12">
									<div class="span6">
										<div class=" control-group">
											<eazytec:label styleClass="control-label" key="schedule.repeatEvery" />
											<div class="controls">
												<form:select path="repeatEvery" name="repeatEvery" id="repeatEveryMonths" class="margin-bottom0 span12">
													<c:forEach begin="1" end="12" varStatus="loop">
														<option value="${loop.index}" ${schedule.repeatEvery == loop.index ? 'selected' : ''}>${loop.index}</option>
													</c:forEach>
												</form:select>
<%-- 												<eazytec:label styleClass="form-label" key="schedule.Months" /> --%>
											</div>
										</div>
									</div>
									<div style="float:left;">
										<eazytec:label styleClass="form-label" key="schedule.Months" />
									</div>
								</div>
							</div>
							
							<div class="row-fluid">
								<div class="span12">
									<div class="control-group">
										<eazytec:label styleClass="control-label" key="schedule.description" />
										<div class="controls">
											<form:textarea path="description" name="description" id="description" class="span12" />
										</div>
									</div>
								</div>
							</div>
							
							<div class="form-actions no-margin">
								
									<div class="control-group">
										
											<c:choose>
												<c:when test="${schedule.version != 0 && schedule.from != 'grid'}">
													<div class="button" id="updateButtonDiv">
														<button type="submit" onclick="return validateEventForm(this);" class="btn btn-primary" name="update" id="updateButton">
															<fmt:message key="button.update" />
														</button>
													</div>
										
													<div class="button" id="cancelButtonDiv">
														<button type="button" id="cancelEvent" class="btn btn-primary" name="cancel" onClick="closeSelectDialog('popupDialog')";>
															<fmt:message key="button.cancel" />
														</button>
													</div>
									
													<div class="button" id="deleteButtonDiv">
														<button type="button" id="deleteButton" class="btn btn-primary" name="delete" onClick="deleteSchedule();">
															<fmt:message key="button.delete" />
														</button>
													</div>
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${schedule.id != null && schedule.id != '' }">
															<div class="button" id="updateButtonDiv">
																<button type="submit" onclick="return validateEventForm(this);" class="btn btn-primary" name="update" id="updateButton">
																	<fmt:message key="button.update" />
																</button>
															</div>
														</c:when>
														<c:otherwise>
															<div class="button" id="saveButtonDiv">
																<button type="submit" onclick="return validateEventForm(this);" class="btn btn-primary" name="save" id="saveButton">
																	<fmt:message key="button.save" />
																</button>
															</div>
														</c:otherwise>
													</c:choose>
													<div class="button" id="cancelButtonDiv">
														<button type="button" id="cancelEvent" class="btn btn-primary" name="cancel" onClick="closeSelectDialog('popupDialog')";>
															<fmt:message key="button.cancel" />
														</button>
													</div>
												</c:otherwise>
											</c:choose>
										</div>
									
							</div>
						</div>
					</div>
				</form:form>
			</div>
		</div>
</div>
	
