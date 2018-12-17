<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="systemlog.heading" /></title>
<meta name="heading" content="<fmt:message key='systemlog.heading'/>" />
<script type="text/javascript">
</script>

</head>

<div class="page-header">	
		<h2><fmt:message key="systemLog.title"/></h2>
</div>
	<div align="right">
		<strong><a id="backToPreviousPage"
			style="text-decoration: underline; padding: 10px;"
			href="javascript:void(0);" data-role="button" data-theme="b"
			onClick="showListViews('LOG_SETTING', 'LogSetting');" data-icon="">
			<fmt:message key="button.back" /></a> 
		</strong>	
	</div>
	<div class="height10"></div>
	<spring:bind path="systemLog.*">
		<%@ include file="/common/messages.jsp"%>
	</spring:bind>

<div class="span12">
		<div class="row-fluid">
		<div class="widget">
			<div class="widget-body">
			<form:form id="systemLogForm" commandName="systemLog" method="post"
				action="bpm/admin/saveSystemLog" autocomplete="off" cssClass="form-horizontal" 
				modelAttribute="systemLog" onSubmit="_execute('target','')">
				<c:if test="${systemLog.id != null}">
		       			 <form:hidden path="id"/>		        
		   		</c:if>
				<datalist id="searchModuleresults"> </datalist>
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="systemLog.logType" />
						<div class="controls">
							<form:select path="logType" id="logType" name="logType" class="span6">
								<form:option value="Admin Logs"><fmt:message key="systemLog.logType.admin"/></form:option>
								<form:option value="Process Logs"><fmt:message key="systemLog.logType.process"/></form:option>
								<form:option value="System Logs"><fmt:message key="systemLog.logType.system"/></form:option>
							</form:select>
							
						</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="systemLog.moduleName" />
						<div class="controls">
							<form:input path="moduleName" class="span6" id="searchModuleName" onkeyup="getModuleName();"
								 onblur="checkModuleName();" list="searchModuleresults" autocomplete="off" name="searchModule"/> 
						</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="systemLog.name" />
						<div class="controls">
							<form:input path="name" id="name" readonly="false" class="span6" />
						</div>
					</div>					
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="systemLog.status" />
						<div class="controls">
							<form:select path="status" id="status" name="status" class="span6">
								<form:option value="true"><fmt:message key="systemLog.enabled"/></form:option>
								<form:option value="false"><fmt:message key="systemLog.disabled"/></form:option>
							</form:select>
							
						</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="systemLog.saveCycle" />
						<div class="controls">
							<form:select path="saveCycle" id="saveCycle" name="saveCycle" class="span6">
								<form:option value="Day"><fmt:message key="systemLog.saveCycle.day"/></form:option>
								<form:option value="Month"><fmt:message key="systemLog.saveCycle.month"/></form:option>
								<form:option value="Quarter"><fmt:message key="systemLog.saveCycle.quarter"/></form:option>
								<form:option value="Year"><fmt:message key="systemLog.saveCycle.year"/></form:option>
							</form:select>
							
						</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="systemLog.cycleValue" />
						<div class="controls">
							<form:input path="cycleValue" id="cycleValue" onblur = "checkCycleValue(this)" class="span6" />
						</div>
					</div>					
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="systemLog.description" />
						<div class="controls">
							<form:textarea path="description" id="description" class="span6" />
						</div>
					</div>
					<div class="control-group">
						<eazytec:label styleClass="control-label"	key="systemLog.Action" />
						<div class="controls">
							<form:select path="deadLine" id="deadLine" name="deadLine" class="span6">
								<form:option value="Automatically deleted"><fmt:message key="systemLog.Action.autoDelete"/></form:option>
								<form:option value="Automatically archived"><fmt:message key="systemLog.Action.autoArchive"/></form:option>
								<form:option value=" Manually Process"><fmt:message key="systemLog.Action.manual"/> </form:option>
							</form:select>
							</div>
					</div>
					<div class="control-group">
						<div class="form-actions no-margin">
							<button type="submit" class="btn btn-primary" name="save"
								onclick="bCancel=false;" id="saveButton">
								<c:choose>
									<c:when test="${systemLog.id != null}">
										<fmt:message key="button.update" />
									</c:when>
									<c:otherwise>
										<fmt:message key="button.save" />
									</c:otherwise>
								</c:choose>
							</button>
							<button type="button" class="btn btn-primary " name="next"
								onclick="showListViews('LOG_SETTING','Log Setting');" id="cancelButton"
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

function checkCycleValue(cycleVal) {
	var cycleValue = cycleVal.value;

	if ( isNaN(cycleValue) || cycleValue == ''  ) {
			$.msgBox({
                title : form.title.message,
                content : form.msg.cycleValueNotEmpty,
                buttons : [ {
                    value : "Ok"
                } ],
                success : function(result) {
                    if (result == "Ok") {
                        $("#cycleValue").val('0');
                        $("#cycleValue").focus();
                    }
                }
            });			
			return false;
	} else {
		return true;
	}
}

</script>
<v:javascript formName="systemLog" staticJavascript="false" />
<script type="text/javascript"
	src="<c:url value="/scripts/validator.jsp"/>">
</script>

<%-- <%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="systemlog.heading" /></title>
<meta name="heading" content="<fmt:message key='systemlog.heading'/>" />
<script type="text/javascript">




</script>
</head>

<div class="page-header">
	<div class="pull-left">
		<h2><fmt:message key="systemLog.title"/></h2>
	</div>
	<div align="right">
		<strong><a id="backToPreviousPage"
			style="text-decoration: underline; padding: 10px;"
			href="javascript:void(0);" data-role="button" data-theme="b"
			onClick="showListViews('LOG_SETTING', 'LogSetting');" data-icon=""><fmt:message
					key="button.back" /></a> </strong>
	<spring:bind path="systemLog.*">
		<%@ include file="/common/messages.jsp"%>
	</spring:bind>
	</div>
	<div class="height10"></div>
</div>
<div class="span12">
	
			<form:form id="systemLogForm" commandName="systemLog" method="post"
				action="bpm/admin/saveSystemLog" autocomplete="off" 
				modelAttribute="systemLog" onSubmit="_execute('target','')">
				<c:if test="${systemLog.id != null}">
		       			 <form:hidden path="id"/>		        
		   		</c:if>
				<datalist id="searchModuleresults"> </datalist>

				<table width="100%" border="1" class="line-height46">

					<tr>
						<td width="271" height="40"><eazytec:label
								styleClass="control-label style3 style18 width:250px"
								key="systemLog.logType" /></td>
						<td width="436" class="uneditable-input1">
							<form:select path="logType" id="logType" name="logType" style="width:250px">
								<form:option value="Admin Logs"><fmt:message key="systemLog.logType.admin"/></form:option>
								<form:option value="Process Logs"><fmt:message key="systemLog.logType.process"/></form:option>
								<form:option value="System Logs"><fmt:message key="systemLog.logType.system"/></form:option>
							</form:select>
						</td>
					</tr>


					<tr>
						<td width="271" height="40"><eazytec:label
								styleClass="control-label style3 style18 "
								key="systemLog.moduleName" />
						<td width="436" class="uneditable-input1">
						 <form:input path="moduleName" rows="4" cols="27" class="medium" id="searchModuleName" onkeyup="getModuleName();" onblur="checkModuleName();" list="searchModuleresults" autocomplete="off" name="searchModule"/> 
						</td>
					</tr>
	
					<!-- 					<tr> -->
					<!-- 						<td width="271" height="40"> -->
											<eazytec:label styleClass="control-label style3 style18" key="systemLog.name" />
					<!-- 						<td width="436" class="uneditable-input1"> -->
											<form:input path="name" id="name" /></td>
					<!-- 						</td> -->
					<!-- 					</tr> -->

					<tr>
						<td width="271" height="40"><eazytec:label
								styleClass="control-label style3 style18" key="systemLog.name" /></td>
						<td width="436" class="uneditable-input1"><form:input
								path="name" id="name" class="medium" depends="required" /></td>
					</tr>

<!-- 					<tr> -->
						<td width="271" height="40"><eazytec:label
								styleClass="control-label style3 style18" key="systemLog.link" /></td>
						<td width="436" class="uneditable-input1"><form:input
								path="link" id="link" class="medium" depends="required" /></td>
<!-- 						</td> -->
<!-- 					</tr> -->

					<tr>
						<td width="271" height="40">
						<eazytec:label styleClass="control-label style3 style18" key="systemLog.status" /></td>
<!-- 						<td width="436" class="uneditable-input1" ><input type="radio" -->
<!-- 							 name="status" value="true" id="enabled"  checked="true"/> -->
<!-- 							Enabled <input type="radio"  name="status" -->
<!-- 							value="false" id="disabled" /> Disabled</td> -->
<!-- 						</td> -->
						<td>
						<form:select path="status" id="status" name="status" style="width:250px">
								<form:option value="true"><fmt:message key="systemLog.enabled"/></form:option>
								<form:option value="false"><fmt:message key="systemLog.disabled"/></form:option>
							</form:select>
						</td>
					</tr>

					<tr>
						<td width="271" height="40"><eazytec:label
								styleClass="control-label style3 style18"
								key="systemLog.saveCycle" /></td>
												<td width="436" class="uneditable-input1"><form:input
														path="saveCycle" id="saveCycle" class="medium" /></td>
						<!-- 						</td> -->

						<td width="436" class="uneditable-input1">
							<form:select path="saveCycle" id="saveCycle" name="saveCycle" style="width:250px">
								<form:option value="Day"><fmt:message key="systemLog.saveCycle.day"/></form:option>
								<form:option value="Month"><fmt:message key="systemLog.saveCycle.month"/></form:option>
								<form:option value="Quarter"><fmt:message key="systemLog.saveCycle.quarter"/></form:option>
								<form:option value="Year"><fmt:message key="systemLog.saveCycle.year"/></form:option>
							</form:select>
						</td>
					</tr>

					<tr>
						<td width="271" height="40"><eazytec:label
								styleClass="control-label style3 style18"
								key="systemLog.cycleValue" /></td>
						<td width="436" class="uneditable-input1"><form:input
								path = "cycleValue"	name="cycleValue"		id="cycleValue"  class="medium" onblur = "checkCycleValue(this)" depends="required" /></td>
								
						</tr>

					<tr>
						<td width="271" height="40"><eazytec:label
								styleClass="control-label style3 style18"
								key="systemLog.description" /></td>
						<td width="436" class="uneditable-input1"><form:textarea
								path="description" id="description" rows="4" cols="27"
								class="medium" /></td>
					</tr>


					<tr>
						<td width="271" height="40"><eazytec:label
								styleClass="control-label style3 style18"
								key="systemLog.Action" /></td>
						<td width="436" class="uneditable-input1">
								<form:select path="deadLine" id="deadLine" name="deadLine" style="width:250px">
								<form:option value="Automatically deleted"><fmt:message key="systemLog.Action.autoDelete"/></form:option>
								<form:option value="Automatically archived"><fmt:message key="systemLog.Action.autoArchive"/></form:option>
								<form:option value="Manually Process"><fmt:message key="systemLog.Action.manual"/></form:option>
							</form:select>
						</td>
					</tr>

					<tr>
						<td></td>
						<td width="436" >
							<button type="submit"
								class="btn btn-primary normalButton padding0 line-height0 "
								name="save" id="saveButton">
								<c:choose>
									<c:when test="${systemLog.id  != null}">
										<fmt:message key="button.update" />
									</c:when>
									<c:otherwise>
										<fmt:message key="button.save" />
									</c:otherwise>
								</c:choose>
							</button>
							<button type="button" class="btn btn-primary normalButton padding0 line-height0" name="cancel" onclick="showListViews('LOG_SETTING','Log Setting')" id="cancelButton" style="cursor: pointer;">
			        			<fmt:message key="button.cancel"/>
			    			</button>
						</td>
					</tr>

				</table>
			</form:form>
		</div>
	</div>
</div>

<script type="text/javascript">

function checkCycleValue(cycleVal) {
	var cycleValue = cycleVal.value;

	if ( isNaN(cycleValue) || cycleValue == ''  ) {
			$.msgBox({
								title : form.title.message,
                content : form.msg.cycleValueNotEmpty,
								
                buttons : [ {
                    value : "Ok"
                } ],
                success : function(result) {
                    if (result == "Ok") {
                        $("#cycleValue").val('0');
                        $("#cycleValue").focus();
                    }
                }
            });			
			return false;
	} else {
		return true;
	}
}

</script>
<v:javascript formName="systemLog" staticJavascript="false" />
<script type="text/javascript"
	src="<c:url value="/scripts/validator.jsp"/>">
</script>
 --%>
 
