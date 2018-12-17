<%@ include file="/common/taglibs.jsp"%>

<head>
<title><fmt:message key="userProfile.title" /></title>
</head>
<script type="text/javascript">
	$(function() {
		loadDateField('startDate');			
		loadDateField('endDate');
		/* $("#notificationNeeded").click(function() {
			var $this = $(this);
		    if ($this.is(':checked')) {
		    	$("#notificationType").prop('disabled', false);
	            $("#notificationMessage").prop('disabled', false);
		    } else {
		    	$("#notificationType").prop('disabled', true);
	            $("#notificationMessage").prop('disabled', true);
		    }
		}); */
		
		 
		 var isEdit = "${isEdit}";
		 if(isEdit=="true"){
			 var processId=new Array();
			 processId[0]="${processId}";
			 $("#processIds").val(JSON.stringify(processId));
		 }else{
			 var rowid =  gridObj.getGridParam('selarrrow');
			 var processIds=new Array();
				var i = 0;
				rowid.forEach(function(item) {
					var processId = gridObj.jqGrid('getCell', item, 'Process_Name');
					processIds[i] = processId;
					i++; 
				});
				 $("#processIds").val(JSON.stringify(processIds));
		 }
	});
	
	function checkIsLoginUser(){
		var agentIds=$("#agent").val();
		if(agentIds==""){
			validateMessageBox(form.title.validation,form.error.agentValidation, "info");
			$("#agentFullName").val("");
			return false;
		}else{
			var ids=agentIds.split(",");
			for(var index=0;index<ids.length;index++){
				var currentUserId=$("#currentUserId").val();
				if(currentUserId==ids[index]){
					var usersFullName=$("#userFullName").val();
					validateMessageBox(form.title.validation, form.msg.loginUser+" "+usersFullName+" "+form.error.cantBeAgent , "info");
					$("#agentFullName").val("");
					$("#agent").val("");
					return false;
				}
			}		
		}
		
		var startDate=$("#startDate").val();
		var endDate=$("#endDate").val();
		if(startDate==""){
			validateMessageBox(form.title.validation, form.error.startDateEmptyValidation , "info");
			return false;
		}
		
		if(endDate==""){
			validateMessageBox(form.title.validation, form.error.endDateEmptyValidation , "info");
			return false;
		}
		
		if(startDate>endDate){
			$("#startDate").val("");
			$("#endDate").val("");
			validateMessageBox(form.title.validation, form.error.startDateLesserValidation , "info");
			return false;
		}
		
		 var count=0;
		 if($("#isMail").is(':checked')){
			 count++;
		 }
		 if($("#isInternalMessage").is(':checked')){
			 count++;
		 }
		 if($("#isSms").is(':checked')){
			 count++;
		 }
		 
		if(count==0){
			validateMessageBox(form.title.validation, form.error.selectNotificationType , "info");
			$("#isMail").focus();
			return false;
		}
		return true;
	}
</script>
<spring:bind path="agencySetting.*">
	<%@ include file="/common/messages.jsp"%>
</spring:bind>

<!-- <div id="popupDialog" style="height: auto; min-height: 100px; width: auto;">	 -->
<div > 
<%-- <h2><fmt:message key="agencySetting.heading"/> </h2> --%>
<div class="row-fluid">
<!-- <div class="span12 "> -->
<form:form
	id="agencySetting" commandName="agencySetting" method="post"
	action="bpm/agency/saveAgencySetting" autocomplete="off"
	modelAttribute="agencySetting" cssClass="form-horizontal" onsubmit="setTimeout(function() {closeSelectDialog('popupDialog');}, 2);">
	<form:hidden path="id" />
	<form:hidden path="userId" />
	
					 <div class="control-group">
						<br><eazytec:label styleClass="control-label" key="agencySetting.agent"/>
						<div class="controls">
							<form:hidden  path="agent" id="agent" onclick="showUserSelection('User', 'single', 'agent', this, '');" />
							<form:input path="agentFullName" id="agentFullName" readonly="false" class="span10" onclick="showUserSelection('User', 'single', 'agent',document.getElementById('agent'), '');" />
							<input type="hidden" name="processIds" id="processIds"/>
						</div>
					</div>  
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="agencySetting.startDate"/>
						<div class="controls">
							<form:input path="startDate" id="startDate" readonly="true" class="span10"/>
						</div>
					</div>  
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="agencySetting.endDate"/>
						<div class="controls">
							<form:input path="endDate" id="endDate" readonly="true" class="span10"/>
						</div>
					</div> 
					<div class="control-group">
						<eazytec:label styleClass="control-label" key="agencySetting.notificationMessage"/>
						<div class="controls">
							<form:textarea path="notificationMessage" id="notificationMessage" readonly="false" class="span10"/>
						</div>
					</div> 
					<div class="control-group">
						<%-- <eazytec:label styleClass="control-label" key=" "/> --%>
						<div class="controls">
							<label class="checkbox inline"> 
								<form:checkbox path="isMail" id="isMail"/> <fmt:message key="MAIL"/>
							</label> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<label class="checkbox inline">
								<form:checkbox path="isSms" id="isSms"/><fmt:message key="SMS"/>
							</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<label class="checkbox inline">
								<form:checkbox path="isInternalMessage" id="isInternalMessage"/><fmt:message key="Internal Message"/>
							</label>
						</div>
					</div> 
					
					
					
					
					
					
					<div class="control-group">
                        <div class="form-actions no-margin">    
                            <div class="button" id="saveButtonDiv">
                                <button type="submit" class="btn btn-primary" name="save" id="saveButton" onclick="return checkIsLoginUser();">
                                       <fmt:message key="button.save"/>
                                </button>&nbsp;&nbsp;
                            </div>
                            <div class="button" id="cancelButtonDiv">
                                <button type="button" id="cancelEvent" class="btn btn-primary " name="cancel" onClick="closeSelectDialog('popupDialog');">
                                       <fmt:message key="button.cancel"/>
                                </button>
                            </div> 
                        </div>    
                    </div>
                  
	<%-- <table>
	
		 <tr>	
			<td width="271" height="40">
			<span id="tableNameLable" class="control-label style18 style3"><label><fmt:message key="agencySetting.agent"/><span class="required">&nbsp;*</span></label></span>
			</td>
			<td width="436" class="uneditable-input1">
			
			<form:hidden  path="agent" id="agent" onclick="showUserSelection('User', 'single', 'agent', this, '');" />
			<form:input  path="agentFullName" id="agentFullName" class="medium" onclick="showUserSelection('User', 'single', 'agent',document.getElementById('agent'), '');" />
			</td>
		</tr> 
		
		
		<tr>
			<td width="271" height="40">
			<span id="tableNameLable" class="control-label style18 style3"><label><fmt:message key="agencySetting.startDate"/><span class="required">&nbsp;*</span></label></span>
			<td width="436" class="uneditable-input1">
			<form:input path="startDate" id="startDate"
							class="medium" readonly="true" /></td>
		</tr>
		<tr>
			<td width="271" height="40">
			<span id="tableNameLable" class="control-label style18 style3"><label><fmt:message key="agencySetting.endDate"/><span class="required">&nbsp;*</span></label></span>
			</td>
			<td width="436" class="uneditable-input1">
			<form:input path="endDate"  id="endDate"
							class="medium" readonly="true" /></td>
		</tr>
		
		<tr>
			<td width="271" height="40"><eazytec:label
				styleClass="control-label style3 style18" key="agencySetting.Status" /></td>
			<td width="436" class="uneditable-input1">
			<form:checkbox id="enabled" path="enabled" /></td>
		</tr>
		<tr>
			<td width="271" height="40"><eazytec:label
				styleClass="control-label style3 style18" key="agencySetting.notificationNeeded" /></td>
			<td width="436" class="uneditable-input1">
			<form:checkbox id="notificationNeeded" path="notificationNeeded" /></td>
		</tr>
		<tr>

					<td width="271" height="40"><span id="tableNameLable"
						class="control-label style18 style3"><label><fmt:message
									key="agencySetting.notificationMessage" /><span
								class="required">&nbsp;*</span></label></span></td>


					<td width="436" class="uneditable-input1 pad-T8 pad-B8">
				<form:textarea path="notificationMessage" rows="3" id="notificationMessage"
					cols="27" class="medium" />
		</tr>
		<tr>
			<td width="271" height="40"><eazytec:label
				styleClass="control-label style3 style18" key="agencySetting.notificationType" /></td>
			<td width="436" class="uneditable-input1">
			<form:select path="notificationType"  id="notificationType"
							class="medium margin-bottom0">
							<form:option value=""></form:option>
							<form:option value="mail">Mail</form:option>
							<form:option value="message">Message</form:option>
							<form:option value="SMS">SMS</form:option>
			</form:select>
			
		    <td colspan="3" class="pad-T8 pad-B8">
			    <center> <label class="checkbox inline style3 style18">
		               <form:checkbox path="isMail" id="isMail" />
		               <fmt:message key="MAIL"/>
		         </label>
				<label class="checkbox inline style3 style18">
					<form:checkbox path="isSms" id="isSms" />
		              <fmt:message key="SMS"/>
		         </label>
		         <label class="checkbox inline style3 style18">
		              <form:checkbox path="isInternalMessage" id="isInternalMessage" />
		               <fmt:message key="Internal Message"/>
		         </label></center>
			</td>
		</tr>
		<tr>
			     	<td></td>
			        <td width="436" >
			        		<!-- <div align="center" class="form-actions no-margin"> -->
							 <button type="submit" class="btn btn-primary" name="save" id="saveButton" onclick="return checkIsLoginUser();">
                                            <fmt:message key="button.save"/>
				    		</button>&nbsp;&nbsp;
			    			<button type="button" class="btn btn-primary" name="next" onclick="closeSelectDialog('popupDialog');" id="cancelButton" style="cursor: pointer;">
			        			<fmt:message key="button.cancel"/>
			        		</button>
			        		<!-- </div> -->
			        </td>
		</tr>
		 
	</table> --%>
</form:form>
</div>
</div>
 <!-- </div> -->
<!-- </div> -->



<v:javascript formName="module" staticJavascript="false" />
<script type="text/javascript"
	src="<c:url value="/scripts/validator.jsp"/>"></script>
