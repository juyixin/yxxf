<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userProfile.title"/></title>
    <meta name="menu" content="UserMenu"/>
</head>
<c:set var="delObject" scope="request"><fmt:message key="userList.user"/></c:set>
<script type="text/javascript">


   var msgDelConfirm = "<fmt:message key="delete.confirm."><fmt:param value="${delObject}"/></fmt:message>";
   function addFormParams(method){
	   method=method+"=true";
	   _execute('target',method);
   }

function removeImgFromUserProfile(){
	var content = form.msg.confirmToRemoveImage;
	var message = "";
	var isSuccess = false;
	var isNeededMsg=true;
	$.msgBox({
	    title: form.title.confirm,
	    content: content,
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	$("#profileImage").attr('src', '/images/profile/default.png');
	        	$("#profileImg").val("/images/profile/default.png");
	        	$("#removeImage").hide();
	        }
	    }
	    });
}
   function selectAll(selection){
	   $(selection).find("option").each(function() {
	       $(this).attr('selected', 'selected');
	});
   }
</script>
<div class="page-header">
	
 	<h2><fmt:message key="userProfile.heading"/></h2>
	
	<div align="right">
		<a id="backToPreviousPage" style="padding:3px;" href="javascript:void(0);" data-role="button" data-theme="b" onClick="backToPreviousPage()" data-icon=""><button class="btn"><fmt:message key="button.back" /></button></a>
	</div>
</div>
<div class="row-fluid">
	<div class="span12">
	<div class="box_main" style="padding-top:0px;">
		<form:form commandName="user" method="post" action="bpm/admin/userform" id="userForm" autocomplete="off" enctype="multipart/form-data" onSubmit="_execute('target','')">

			<div class="widget">
				<div class="widget-body">
					<div id="wizard">
						<ol>
							<li><fmt:message key="user.personalDetails" /></li>
							<li><fmt:message key="user.jobDetails" /></li>
							<li><fmt:message key="user.contactDetails" /></li>
							<li><fmt:message key="user.permissions" /></li>
							<li><fmt:message key="user.others" /></li>
						</ol>
						
						<div class="well">
						<div  id="personal" >
							<div class="span3" style="width:20%">
<!-- 								<div class="control-group"> -->
<!-- 									<div class="controls"> -->
										<div class="thumbnail" style="width:200px;height:200px;">
											<c:if test="${not empty user.pictureByteArrayId}">
												<c:if
													test="${user.pictureByteArrayId == '/images/profile/default.png'}">
													<img id="profileImage" style = "width: 200px; height: 200px; background-position: center center; background-repeat: no-repeat;" src="/images/profile/default.png" alt="${user.username}" />
												</c:if>
												<c:if
													test="${user.pictureByteArrayId != '/images/profile/default.png'}">
													<img id="profileImage" style = "width: 200px; height: 200px; background-position: center center; background-repeat: no-repeat;" src="${user.pictureByteArrayId}" alt="${user.username}" /> </br>
                                                    <div align="center" > <button data-icon="" onclick="removeImgFromUserProfile()" href="javascript:void(0);" id="removeImage" class="btn btn-info " type="button"><fmt:message key="user.removeImage" /></button></div>		
												</c:if>
											</c:if>
											<c:if test="${empty user.pictureByteArrayId}">
												<img id="profileImage" style = "width: 200px; height: 200px; background-position: center center; background-repeat: no-repeat;" src="/images/profile/default.png" alt="${user.username}" />
											</c:if>
										</div>
<!-- 									</div> -->
<!-- 								</div> -->
							</div>
							<div class="span9" style="width:78%">
								<div class="form-horizontal no-margin" >
									<div class="control-group">
										<eazytec:label styleClass="control-label" key="user.username" />
										<div class="controls">
											<c:if test="${empty user.id or user.version == 0}">
												<form:input path="username" id="username" class="span8" />
											</c:if>
											<c:if test="${not empty user.id && user.version !=0}">
												<form:input path="username" id="username" readonly="true" class="span8" />
											</c:if>
										</div>
									</div>
			
									<c:if test="${cookieLogin != 'true'}">
										<div class="control-group">
											<eazytec:label styleClass="control-label"
												key="user.password" />
											<div class="controls">
												<form:password path="password" id="password"
													showPassword="true" class="span8" />
											</div>
										</div>
			
										<div class="control-group">
											<eazytec:label styleClass="control-label"
												key="user.confirmPassword" />
											<div class="controls">
												<form:password path="confirmPassword" id="confirmPassword"
													showPassword="true" class="span8" />
											</div>
										</div>
									</c:if>
			
									<div class="control-group">
										<eazytec:label styleClass="control-label" key="user.passwordHint" />
										<div class="controls">
											<form:input path="passwordHint" id="passwordHint" class="span8"/>
										</div>
									</div>

									<div class="control-group">
										<eazytec:label styleClass="control-label" key="user.fullName" />
										<div class="controls">
											<form:input path="fullName" id="fullName" maxlength="50" class="span8" />
										</div>
									</div>									
			
									<%-- <div class="control-group">
										<eazytec:label styleClass="control-label" key="user.firstName" />
										<div class="controls">
											<form:input path="firstName" id="firstName" maxlength="50" class="span8" />
										</div>
									</div>
			
									<div class="control-group">
										<eazytec:label styleClass="control-label" key="user.lastName" />
										<div class="controls">
											<form:input path="lastName" id="lastName" maxlength="50" class="span8" />
										</div>
									</div> --%>
			
									<div class="control-group">
										<eazytec:label styleClass="control-label" key="user.englishName" />
										<div class="controls">
											<form:input path="englishName" id="englishName" maxlength="50" class="span8" />
										</div>
									</div>
			
									<div class="control-group">
										<eazytec:label styleClass="control-label " key="user.sex" />
										<div class="controls">
											<form:select path="sex" name="sex" id="sex" class="span8">
												<option value="male" ${fn:contains(user.sex, 'male') ? 'selected' : ''}><fmt:message key="user.male" /></option>
												<option value="female" ${fn:contains(user.sex, 'female') ? 'selected' : ''}><fmt:message key="user.female" /></option>
											</form:select>
										</div>
									</div>
			
									<div class="control-group">
										<eazytec:label styleClass="control-label"
											key="user.profileImage" />
										<div class="controls checkbox-label">
											<input type="file" name="file" id="file" size="20" accept="image/*" />
										</div>
									</div>
			
									<div class="control-group">
										<eazytec:label styleClass="control-label"
											key="user.dateOfBirth" />
										<div class="controls">
											<form:input path="dateOfBirth" id="dateOfBirth" class="span3 white-background" readonly="true" />
										</div>
									</div>
			
									<div class="control-group">
										<eazytec:label styleClass="control-label" key="user.email" />
										<div class="controls">
											<form:input path="email" id="email" class="span8" />
										</div>
									</div>
			
									<div class="control-group">
										<eazytec:label styleClass="control-label " key="user.mobile" />
										<div class="controls">
											<form:input path="mobile" id="mobile" class="span8" />
										</div>
									</div>
			
									<div class="control-group">
										<label class="control-label"><fmt:message key="userProfile.accountSettings" /></label>
										<div class="controls">
											<c:choose>
												<c:when test="${param.from != 'profile'}">
													<label class="checkbox inline"> <form:checkbox path="enabled" id="enabled" /> <fmt:message key="user.enabled" /> </label>
													<label class="checkbox inline"> <form:checkbox path="SendEmailNotification" id="SendEmailNotification" /> <fmt:message key="user.SendEmailNotification" /> </label>
												</c:when>
												<c:otherwise>
													<form:hidden path="enabled" />
													<label class="checkbox inline"> <form:checkbox path="SendEmailNotification" id="SendEmailNotification" /> <fmt:message key="user.SendEmailNotification" /> </label>
												</c:otherwise>
											</c:choose>
										</div>
									</div>
								</div>
								
							</div>
							</div>
						</div>
					
						<div class="well" >
						<!-- <div id="job"> -->
							<div class="form-horizontal no-margin" id="job">
								<div class="control-group">
									<eazytec:label styleClass="control-label" key="user.employeeNumber" />
									<div class="controls">
										<form:input path="employeeNumber" id="employeeNumber"
											maxlength="50" class="span6" />
									</div>
								</div>
	
								<div class="control-group">
									<eazytec:label styleClass="control-label" key="user.idCardNumber" />
									<div class="controls">
										<form:input path="idCardNumber" id="idCardNumber" maxlength="50" class="span6" />
									</div>
								</div>
	
								<div class="control-group">
									<eazytec:label styleClass="control-label" key="user.position" />
									<div class="controls">
										<form:input path="position" id="position" maxlength="50" class="span6" />
									</div>
								</div>
	
								<div class="control-group">
									<eazytec:label styleClass="control-label " key="user.partTimePosition" />
									<div class="controls">
										<form:input path="partTimePosition" id="partTimePosition" maxlength="50" class="span6" />
									</div>
								</div>
	
								<div class="control-group">
									<eazytec:label styleClass="control-label" key="user.manager" />
									<div class="controls">
										<form:input type="hidden" path="manager" id="manager" />
										<input type="text" id="managerFullName" class="span6 white-background" onclick="showUserSelection('User', 'single' , 'manager', this,'');" readonly="true" />
									</div>
								</div>
	
								<div class="control-group">
									<eazytec:label styleClass="control-label" key="user.secretary" />
									<div class="controls">
										<form:input path="secretary" type="hidden" id="secretary" class="mini-low white-background" onclick="showUserSelection('User', 'single' , 'secretary', this, '');" readonly="true" />
										<input id="secretaryFullName" type="text" class="span6  white-background" onclick="showUserSelection('User', 'single' , 'secretary', this, '');" readonly="true" />
									</div>
								</div>
	
								<div class="control-group">
									<eazytec:label styleClass="control-label " key="user.department" />
									<div class="controls">
										<form:input path="department" type="hidden" id="department"/>
										<input type="text" id="departmentFullName" class="span6  white-background" onclick="showDepartmentSelection('Department', 'single' , 'department', this, '','true');" readonly="true"/>
									</div>
								</div>
	
								<div class="control-group">
									<eazytec:label styleClass="control-label" key="userProfile.assignPartTimeDepartment" />
									<div class="controls">
										<form:input path="partTimeDepartment" type="hidden" id="partTimeDepartment"/>
										<input type="text" id="partTimeDepartmentFullName" class="span6  white-background" onclick="showDepartmentSelection('Department', 'single' , 'partTimeDepartment', this, '','true');" readonly="true" />
									</div>
								</div>
							</div>
						<!-- </div> -->
						</div>
						<div class="well" >
						<!-- <div id="contact"> -->
							<div class="form-horizontal no-margin" id="contact">
							
								<div class="control-group">
									<eazytec:label styleClass="control-label " key="user.workPhone" />
									<div class="controls">
										<form:input path="workPhone" id="workPhone" class="span6" />
									</div>
								</div>
								
								<div class="control-group">
									<eazytec:label styleClass="control-label" key="user.homePhone" />
									<div class="controls">
										<form:input path="homePhone" id="homePhone" class="span6" />
									</div>
								</div>
								
								<div class="control-group">
									<eazytec:label styleClass="control-label" key="user.fax" />
									<div class="controls">
										<form:input path="fax" id="fax" maxlength="50" class="span6" />
									</div>
								</div>
		
								<div class="control-group">
									<eazytec:label styleClass="control-label" key="user.website" />
									<div class="controls">
										<form:input path="website" id="website" class="span6" />
									</div>
								</div>
		
								<div class="control-group">
									<eazytec:label styleClass="control-label" key="user.address.address" />
									<div class="controls">
										<form:input path="address.address" id="address.address" class="span6" />
									</div>
								</div>
		
								<div class="control-group">
									<eazytec:label styleClass="control-label" key="user.address.city" />
									<div class="controls">
										<form:input path="address.city" id="address.city" class="span6" />
									</div>
								</div>
		
								<div class="control-group">
									<eazytec:label styleClass="control-label" key="user.address.province" />
									<div class="controls">
										<form:input path="address.province" id="address.province" class="span6" />
									</div>
								</div>
		
								<div class="control-group">
									<eazytec:label styleClass="control-label" key="user.address.postalCode" />
									<div class="controls">
										<form:input path="address.postalCode" id="address.postalCode" class="span6" />
									</div>
								</div>
							</div>
						<!-- </div> -->
					</div>	
						<div class="well" >
						<!-- <div id="permission"> -->
							<div class="form-horizontal no-margin" id="permission">
							
								<div class="control-group">
									<eazytec:label styleClass="control-label" key="user.roles" />
									<div class="controls">
										<input type="hidden" id="userRoles" name="userRoles" />
										<input type="text" id="userRolesFullName" class="span6 white-background" onclick="showRoleSelection('Role', 'multi' , 'userRoles', this, '','true');" readonly="readonly" />
									</div>
								</div>
								<div class="control-group">
									<eazytec:label styleClass="control-label " key="userProfile.assignGroups" />
									<div class="controls">
										<input type="hidden" id="userGroups" name="userGroups" />
										<input type="text" id="userGroupsFullName" class="span6 white-background" onclick="showGroupSelection('Group', 'multi' , 'userGroups', this, '');" readonly="readonly" />
									</div>
								</div>
							</div>
						<!-- </div> -->
					</div>	
						<div class="well" >
							<!-- <div id="ohterDetails"> -->
							<div class="form-horizontal no-margin" id="ohterDetails">
								<div class="control-group">
									<eazytec:label styleClass="control-label " key="user.language" />
									<div class="controls">
										<form:select path="language" id="language" name="language" class="span6">
											<option value="zh_CN" ${fn:contains(user.language, 'zh_CN') ? 'selected' : '' } ><fmt:message key="user.language.chinese"/></option>
											<option value="en" ${fn:contains(user.language, 'en') ? 'selected' : ''}><fmt:message key="user.language.english"/></option>
										</form:select>
									</div>
								</div>
								<div class="control-group">
									<eazytec:label styleClass="control-label " key="user.preferredSkin" />
									<div class="controls">
										<form:select path="preferredSkin" id="preferredSkin" class="span6" >
											<option value="main" ${fn:contains(userObject.preferredSkin, 'main') ? 'selected' : ''}><fmt:message key="user.theming.default"/></option>
											<option value="facebook" ${fn:contains(userObject.preferredSkin, 'facebook') ? 'selected' : ''}><fmt:message key="user.theming.facebook"/></option>
											<option value="foursquare" ${fn:contains(userObject.preferredSkin, 'foursquare') ? 'selected' : ''}><fmt:message key="user.theming.fourSquare"/></option>
											<option value="googleplus" ${fn:contains(userObject.preferredSkin, 'google') ? 'selected' : ''}><fmt:message key="user.theming.google+"/>+</option>
											<option value="instagram" ${fn:contains(userObject.preferredSkin, 'instagram') ? 'selected' : ''}><fmt:message key="user.theming.instagram"/></option>
											<option value="whitesmoke" ${fn:contains(userObject.preferredSkin, 'whiteSmoke') ? 'selected' : ''}><fmt:message key="user.theming.whiteSmoke"/></option>
											<option value="grey" ${fn:contains(userObject.preferredSkin, 'grey') ? 'selected' : ''}><fmt:message key="user.theming.grey"/></option> 
										</form:select>
									</div>
								</div>
								
								<div class="control-group">
									<eazytec:label styleClass="control-label " key="user.whereTheWork" />
									<div class="controls">
										<form:textarea path="whereTheWork" id="whereTheWork" rows="4" class="span6" />
									</div>
								</div>
	
								<div class="control-group">
									<eazytec:label styleClass="control-label" key="user.comment" />
									<div class="controls">
										<form:textarea path="comment" id="comment" rows="4" class="span6" />
									</div>
								</div>
	
								<div class="control-group">
									<eazytec:label styleClass="control-label" key="user.resposibilities" />
									<div class="controls">
										<form:textarea path="resposibilities" id="resposibilities" rows="4" class="span6" />
									</div>
								</div>
							</div>
						<!-- </div> -->
					</div>
				</div>
					<div class="control-group" align="center">
						<div class="form-actions no-margin">	
							<span id="saveButtonDiv" align="center">
								<button type="submit" name="save" onclick="return validateUserForm(this);" class="btn btn-primary" id="saveButton" class="cursor_pointer"><i class="icon-ok icon-white"></i>
									<c:choose>
										<c:when test ="${user.version !=0 }">
											<fmt:message key="button.update"/>
										</c:when>
										<c:otherwise>
											<fmt:message key="button.save"/>
										</c:otherwise>
									</c:choose>
								</button>
							</span>
							<span id="cancelButtonDiv" class="">
								<button type="button" class="btn btn-primary" name="cancel" onclick="backToPreviousPage()" id="cancelButton" class="cursor_pointer"><i class="icon-remove icon-white"></i>
									<fmt:message key="button.cancel"/>
								</button>
							</span>
							<div class="clearfix"></div>
						</div>
					</div>	
				</div>
			</div>
			<form:hidden path="id" />
			<form:hidden path="pictureByteArrayId" id="profileImg" />
			<form:hidden path="version" />
			<form:hidden path="accountExpired" id="accountExpired" />
			<form:hidden path="emailPassword" id="emailPassword" />
			<form:hidden path="signature" id="signature" />
			<form:hidden path="accountLocked" id="accountLocked" />
			<form:hidden path="credentialsExpired" id="credentialsExpired" />
			<form:hidden path="organizationName" id="organizationName" />
			<form:hidden path="userRole" />
			<form:hidden path="canDelete" id="canDelete" />
			<input type="hidden" name="from" id="from" value="<c:out value="${param.from}"/>" />
			
			<c:if test="${cookieLogin == 'true'}">
			<form:hidden path="password" />
			<form:hidden path="confirmPassword" />
			</c:if>
			
			<c:if test="${user.version == 0}">
			<input type="hidden" name="encryptPass" value="true" />
			</c:if>
		</form:form>
	</div>
</div>
</div>

<script type="text/javascript">
/*	$("#whereTheWork").val('${whereTheWork}');
	$("#resposibilities").val('${resposibilities}');
	$("#comment").val('${comment}');*/
	$("#wizard").bwizard();	
	var newHeight = $("#target").height();
	
	var wizardHeight = parseInt(newHeight) - 190;
	$('#wizard').css({height: wizardHeight});
	$(".bwizard-buttons").css({display: 'none'});
	var personalHeight = parseInt(newHeight) - 257;
	$('#personal').css({height: personalHeight,overflow: 'auto'});
	
	var jobHeight = parseInt(newHeight) - 257;
	$('#job').css({height: jobHeight,overflow:'auto'});
	
	var contactHeight = parseInt(newHeight) - 257;
	$('#contact').css({height: contactHeight,overflow: 'auto'});
	
	var permissionHeight = parseInt(newHeight) - 393;
	$('#permission').css({height: permissionHeight,overflow: 'auto'});
	
	var othersHeight = parseInt(newHeight) - 257;
	$('#ohterDetails').css({height: othersHeight,overflow: 'auto'});
	
	function validateUserForm(aForm){
		var roles = $('#userRoles').val();
		if(roles == null || roles == ''){
			var errorMessage = "<fmt:message key="user.error.selectRole"/>";
			validateMessageBox(form.title.error, errorMessage , "error");
			return false;
		}
		   
	}
	$(function(){
		   changeWizardByTab();
		   $( '#dateOfBirth' ).datepicker({
				dateFormat: 'yy-mm-dd',
				changeYear: true,
				changeMonth: true,
				yearRange: "-120:+0",
				showOn: "button",
				buttonImage:'/images/easybpm/form/rbl/_cal.gif',
				maxDate: new Date(),
			});
		   
			if(document.getElementById("id").value == '' && document.getElementById("version").value == 0){
				$('#enabled').prop('checked', true);
			}
			var JSONUsers = JSON.parse('${usersJSON}');
			setUserFullNames("managerFullName",$('#manager').val(),JSONUsers);
			//$('#managerFullName').html($('#managerFullName').val());
			setUserFullNames("secretaryFullName",$('#secretary').val(),JSONUsers);
			//$('#secretaryFullName').html($('#secretaryFullName').val());
			
			var JSONDepartments = JSON.parse('${departmentsJSON}');
			setUserFullNames("departmentFullName",$('#department').val(),JSONDepartments);
			setUserFullNames("partTimeDepartmentFullName",$('#partTimeDepartment').val(),JSONDepartments);
			
			$('#userRoles').val('${userRoles}');
			
			var JSONRoles = JSON.parse('${rolesJSON}');
			setUserFullNames("userRolesFullName",$('#userRoles').val(),JSONRoles);
			
			$('#userGroups').val('${userGroups}');
			
			var JSONGroups = JSON.parse('${groupsJSON}');
			setUserFullNames("userGroupsFullName",$('#userGroups').val(),JSONGroups);
			
			$("#whereTheWork").val('${whereTheWork}');
			$("#comment").val('${comment}');
			$("#password").val('${password}');
			$("#confirmPassword").val('${password}');
			//$('#secretaryFullName').html(userFullNames);
			//$('#userRoles').val('${userRoles}');
			//$('#userGroups').val('${userGroups}');
			//$("#whereTheWork").val('${whereTheWork}');
			//$("#comment").val('${comment}');
			//$("#resposibilities").val('${resposibilities}');
			showUserErrorTab();
			if('${superDepartment}' != ''){
				$("#department").val('${superDepartment}');	
				//$("#department").attr("disabled", "disabled");	
			}
			if('${permitToEdit}' == 'false'){
				$("#saveButton").hide();	
				$("#department").attr("disabled","disabled");
				$("#partTimeDepartment").attr("disabled","disabled");
				$("#managerFullName").attr("disabled","disabled");
				$("#userRoles").attr("disabled","disabled");
				$("#userGroups").attr("disabled","disabled");
			}
		});
</script>

<c:set var="scripts" scope="request">
<script type="text/javascript">
// This is here so we can exclude the selectAll call when roles is hidden 
function onFormSubmit(theForm) {
    return validateUser(theForm);
}
</script>
</c:set>
<spring:bind path="user.*">
        <%@ include file="/common/messages.jsp" %>
</spring:bind>
<v:javascript formName="user" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

