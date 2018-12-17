<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	$(function() {
		loadDateTimeField('startDate');
		loadDateTimeField('endDate');
	});
</script>
<spring:bind path="quartzJob.*">
	<%@ include file="/common/messages.jsp"%>
</spring:bind>
<%-- <div class="span12 box">
	<h2>
		<fmt:message key="quartzJob.heading" />
	</h2>
	<spring:bind path="quartzJob.*">
	<%@ include file="/common/messages.jsp"%>
	</spring:bind>
	<div class="span7 scroll">
		<div class="table-create-user">
			<div id="target" class="span10" style="padding-left: 20PX;">
				<form:form id="quartzJobForm" commandName="quartzJob" method="post" autocomplete="off" modelAttribute="quartzJob" action="bpm/admin/saveQuartzJob">
					<form:hidden path="id" />
					<fieldset class="control-group">
						<div class="span2" style="width: 30%">
							<eazytec:label styleClass="control-label style3 style18" key="quartzJob.triggerName" />
						</div>
						<div class="span3" style="width: 50%">
							<form:input path="triggerName" name="triggerName" id="triggerName" class="large" depends="required"/>
						</div>
					</fieldset>
					
					<fieldset class="control-group">
						<div class="span2" style="width: 30%">
							<eazytec:label styleClass="control-label style3 style18" key="quartzJob.startDate" />
						</div>
						<div class="span3" style="width: 50%">
							<form:input path="startDate" name="startDate" id="startDate" class="large" />
						</div>
					</fieldset>

					<fieldset class="control-group">
						<div class="span2" style="width: 30%">
							<eazytec:label styleClass="control-label style3 style18" key="quartzJob.endDate" />
						</div>
						<div class="span3" style="width: 50%">
							<form:input path="endDate" name="endDate" id="endDate" class="large" />
						</div>
					</fieldset>

					<div id="buttonDiv" style="width: 100%">
						<button type="submit" class=" btn btn-primary normalButton padding0 line-height0" name="save" id="saveButton"">
							<c:choose>
								<c:when test="${quartzjob.id != null }">
									<fmt:message key="button.update" />
								</c:when>
								<c:otherwise>
									<fmt:message key="button.save" />
								</c:otherwise>
							</c:choose>
						</button>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div> --%>


	
<v:javascript formName="quartzJob" staticJavascript="false" />
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>