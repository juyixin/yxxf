 <%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>


<div class="page-header">
	<h2>
		<fmt:message key="timingTask.heading" />
	</h2>

<div align="right">
	<a id="backToPreviousPage" href="javascript:void(0);" style="text-decoration: underline; padding: 10px;" data-role="button" data-theme="b"  
		onClick="showListViews('TIMING_TASK','定时任务');"><button class="btn"><fmt:message key="button.back"/></button></a>
</div>	
</div>
	<spring:bind path="timingTask.*">
		 	<%-- Error Messages --%>
<c:if test="${not empty errors}">
    <%-- <div class="alert alert-error fade in">
        <a href="#" data-dismiss="alert" class="close">&times;</a>
        <c:forEach var="error" items="${errors}">
            <c:out value="${error}"/><br />
        </c:forEach>
    </div>
    <c:remove var="errors"/> --%>
    <script type="text/javascript">
    var error = "";
    <c:forEach var="error" items="${errors}">
    	error = error + "<c:out value='${error}' escapeXml='false'/> <br />";
	</c:forEach>
	validateMessageBox(form.title.error, error, "error");
    </script>
     <c:remove var="errors"/>
</c:if>

<%-- Success Messages --%>
<c:if test="${not empty successMessages}">
    <script type="text/javascript">
    var msg = "";
    <c:forEach var="error" items="${successMessages}">
    	msg = msg + "<c:out value='${error}' escapeXml='false'/> <br />";
	</c:forEach>
	validateMessageBox(form.title.success, msg, "success");
	showListViews("TIMING_TASK","Timing Task");
    </script>
     <c:remove var="successMessages" scope="session"/> 
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

<c:set var="pathInfo" value='<%=request.getAttribute("javax.servlet.forward.servlet_path")%>'/>
<script type="text/javascript">
var path = "<c:out value='${pathInfo}' escapeXml='false'/>";
setIndexPageByRedirect(path);
</script>
		
		
	</spring:bind>

	<div class="span12">
	<div class="row-fluid">
				<div class="widget-body">
	<div class="box_main">

				<form:form id="timingTask" commandName="timingTask" method="post"
					autocomplete="off" modelAttribute="timingTask" cssClass="form-horizontal" 
					action="bpm/admin/saveTimingTask">
					<form:hidden path="jobName" id="jobName" value="${jobNameHidden}" />

					<div class="control-group">
						<eazytec:label styleClass="control-label" key="timingTask.name" />
						<div class="controls">
							<c:if test="${mode != 'EDIT'}">
								<form:input path="name" name="name" id="name" class="span5" />
							</c:if>
							<c:if test="${mode == 'EDIT'}">
								<form:input path="name" name="name" id="name" readonly="true"
									class="span5" />
							</c:if>

						</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="timingTask.description" />
						<div class="controls">				
								<form:textarea path="description" id="description" class="span5" />
						</div>
					</div>
					<div class="control-group">
					
						<eazytec:label styleClass="control-label" key="timingTask.jobClassName" />
					
						<div class="controls">
							<!-- <b> Example : com.eazytec.quartz.SchedulerNotificationJob </b> -->
							<c:if test="${mode != 'EDIT'}">
				                <form:textarea path="jobClassName" name="jobClassName" id="jobClassName" class="span5" />
				                </c:if>
				                <c:if test="${mode == 'EDIT'}">
				                <form:textarea path="jobClassName" name="jobClassName" id="jobClassName" readonly="true" class="span5" />
				          </c:if>
						</div><br>
						<div class="controls">
						<b><fmt:message key="timingTask.exampleClassName"/></b>
					</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="timingTask.parameter" />
						<div class="controls">				
								<form:input path="parameter" id="parameter" class="span5" />
						</div><br>
						<div class="controls">
								<fmt:message key="timingTask.parameter.format"/> 
						</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="timingTask.jRunAt" />
						<div class="controls">				
								<form:input path="jRunAt" id="jRunAt" class="span2" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<form:select path="every" name="every" id="every" class="width24">
												<option value="Day"
													${fn:contains(timingTask.every, 'Day)') ? 'selected' : ''}><fmt:message key="timingTask.jRunAt.day"/></option>
												<option value="Hour"
													${fn:contains(timingTask.every, 'Hour') ? 'selected' : ''}><fmt:message key="timingTask.jRunAt.hour"/></option>
												<option value="Minute"
													${fn:contains(timingTask.every, 'Minute') ? 'selected' : ''}><fmt:message key="timingTask.jRunAt.minute"/></option>
								</form:select>
						</div>
					</div>
					<%-- <div class="control-group">
						<eazytec:label styleClass="control-label"  key="timingTask.jobRunOn" />
						<div class="controls">				
							<form:radiobutton path="jobRunOn"  name="jobRunOn" class="checkbox" id="immediately" value="Immediately" checked="true" onclick="showRunOn(this.value)" />
							<fmt:message key="runOn.immediately"/>&nbsp;&nbsp;			
							<form:radiobutton path="jobRunOn" name="jobRunOn" class="inline" id="on" value="on" checked="" onclick="showRunOn(this.value)" />
							<fmt:message key="runOn.on"/>&nbsp;&nbsp;
							<form:input path="jobRunAt" name="jobRunAt" id="jobRunAt" class="medium"  />
						</div>
					</div>	 --%>	
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="timingTask.jobRunOn" />
						<div class="controls">
							<label class="radio inline span2"> 
									<form:radiobutton path="jobRunOn" name="jobRunOn" id="immediately" value="Immediately" checked="true"  onclick="showRunOn(this.value)" /> 
									<eazytec:label key="runOn.immediately" />
								</label> 
								<label class="radio inline span1"> 
									<form:radiobutton path="jobRunOn" name="jobRunOn" id="on" value="On" onchange="showRunOn(this.value)" />
									<eazytec:label key="runOn.on" />
								</label>
								<form:input path="jobRunAt" name="jobRunAt" id="jobRunAt" class="width18" />
						</div>
					</div>			
					<div class="control-group">
						<div class="form-actions no-margin">
							<button type="submit" class="btn btn-primary" name="save"
								onclick="bCancel=false;" id="saveButton">
								<c:choose>
									<c:when test="${mode == 'EDIT'}">
										<fmt:message key="button.update" />
									</c:when>
									<c:otherwise>
										<fmt:message key="button.save" />
									</c:otherwise>
								</c:choose>
							</button>
							<button type="button" class="btn btn-primary " name="next"
								onclick="showListViews('TIMING_TASK','定时任务');" id="cancelButton"
								style="cursor: pointer;">
								<fmt:message key="button.cancel" />
							</button>
							<div class="clearfix"></div>
						</div>
					</div>			
				
				</form:form>
			</div>
		</div>
	</div> 
	</div>
	<script type="text/javascript">

	$(function() {
		if(${mode != 'EDIT'}){
			if(${runOn == 'On'})
			{
				showRunOn('On');
			}
			else{
			var runOnValue = $("input:radio[name=jobRunOn]").val();
		  showRunOn(runOnValue);
		}
		}
		else{
			if(${runOn == 'On'}){
					showRunOn('On');
				}
				else{
					var runOnValue = $("input:radio[name=jobRunOn]").val();
					  showRunOn(runOnValue);
				}
			
		}
		loadDateTimeField('jobRunAt');
	});
</script>
<v:javascript formName="timingTask" staticJavascript="false" /> 
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
