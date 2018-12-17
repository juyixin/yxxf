<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%-- <page:applyDecorator name="default"> --%>
<head>
<title><fmt:message key="documentForm.title" /></title>
<meta name="heading" content="<fmt:message key='documentForm.title'/>" />
</head>
<script type="text/javascript">
var browser = navigator.appName;
var version = navigator.appVersion;
var num = version.substr(0,3);

if(browser == "Microsoft Internet Explorer" && num == 4.0) {
	document.getElementById("file").style.display = 'block';
	document.getElementById('profileImage1').title = '';
	
} else {
	document.getElementById("file").style.display = 'none';
	document.getElementById('profileImage1').title = '点击修改头像';
}

function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();            
        reader.onload = function (e) {
            $('#profileImage1').attr('src', e.target.result);
        }
        
        reader.readAsDataURL(input.files[0]);
    }
}

$("#file").change(function(){
    readURL(this);
});

$("input[type='image']").click(function() {
	
    $("input[id='file']").click();
		return false;
});

function removeImgFromUserProfile(){
	var content = form.msg.confirmToRemoveImage;
	var message = "";
	var isSuccess = false;
	var isNeededMsg=true;
	$.msgBox({
	    title: form.title.confirm																																																																										,
	    content: content,
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	$("#profileImage1").attr('src', '/images/profile/default.png');
	        	$("#profileImage").attr('src', '/images/profile/default.png');
	        	$("#profileImg").val("/images/profile/default.png");
	        	$("#removeImage").hide();
	        	
	        	
	        }
	    }
	    });
}

	 
	
	function passwordCheck() {
		//fromPasswordChange();
		var newPassword = document.getElementById("newPassword").value;
		var reEnterNewPassword = document.getElementById("reEnterNewPassword").value;
		var currentPassword = 	document.getElementById("oldPassword").value;	
		
		if (currentPassword != "" && newPassword != "" && newPassword.length >= 6
				&& reEnterNewPassword != "" && reEnterNewPassword.length >= 6
				&& newPassword == reEnterNewPassword
				|| (currentPassword == "" && newPassword == "" && reEnterNewPassword == "")) {

			$("#user").submit();
			return true;
		} else {
			if(currentPassword != "") {
			if (newPassword != "" && newPassword.length < 6) {
				
				$.msgBox({
					title :form.title.message,
					content : form.msg.passwordLength,
					type : "message"

				});
			} else if (newPassword != "" && newPassword.length >= 6
					&& reEnterNewPassword == "") {
				
				$.msgBox({
					title : form.title.message,
					content : form.msg.confirmPasswordNotEmpty,
					type : "message"
				});

			} else {
				
				$.msgBox({
							title : form.title.message,
							content : form.msg.confirmPasswordSameAsNewPassword,
							type : "message"
						});

			}
		} else {
							$.msgBox({
							title : form.title.message,
							content : form.msg.enterCurrentPassword,
							type : "message"
						});

		}
		}

	}
	
	
</script>



	<div class="page-header">
		<div class="pull-left">
			<h2><fmt:message key="passwordChange.title" /></h2>
		</div>
		<div class="clearfix"></div>
		<spring:bind path="userObject.*">
			<%@ include file="/common/messages.jsp"%>
		</spring:bind>
	</div>
	
	<div class="row-fluid">
		<!--<div class="span12">
			<div class="widget">-->
				<div class="widget-body">
					<form:form id="user" commandName="userObject" class="form-horizontal no-margin" method="post" action="bpm/admin/saveChangedPassword" autocomplete="off" modelAttribute="userObject" enctype="multipart/form-data" onSubmit="_execute('target','')">
														 <form:hidden path="pictureByteArrayId" id="profileImg"/>	
							<div class="span3" style="width:20%">
			<!-- 					<div class="control-group"> -->
			<!-- 						<div class="controls"> -->
										<div class="thumbnail" style="width:200px;height:200px;">											
											<c:if test="${not empty userObject.pictureByteArrayId}">
												<c:if test="${userObject.pictureByteArrayId == '/images/profile/default.png'}">
<%-- 														<img class="ie8" id="profileImage" style = "width: 200px; height: 200px; background-position: center center; background-repeat: no-repeat;" src="/images/profile/default.png" alt="${userObject.username}" />
 --%>														<input type="image" class="other"  style = "width: 200px; height: 200px; background-position: center center; background-repeat: no-repeat;" id="profileImage1" width="200px" height="200px" src="/images/profile/default.png" alt="${userObject.username}" />
															<!--<input type="file" name="file" id="file" size="20" accept="image/*" style="display: none;" />-->
													</c:if>
													<c:if test="${userObject.pictureByteArrayId != '/images/profile/default.png'}">
<%-- 														<img class="ie8" id="profileImage"  style = "width: 200px; height: 200px; background-position: center center; background-repeat: no-repeat;" src="${userObject.pictureByteArrayId}" alt="${userObject.username}" />
 --%>														<input class="other" type="image" id="profileImage1"  style = "width: 200px; height: 200px; background-position: center center; background-repeat: no-repeat;" src="${userObject.pictureByteArrayId}" alt="${userObject.username}" />
														<!--<input type="file" name="file" id="file" size="20" accept="image/*" style="display: none;" />-->
														 </br>
						                            <div align="center"  style="padding-top:20px;"> <button data-icon="" onclick="removeImgFromUserProfile()" href="javascript:void(0);" id="removeImage" class="btn btn-primary " type="button"><fmt:message key="user.removeImage" /></button></div>		
													</c:if>
												</c:if>
												<br><input type="file" name="file" id="file" size="20" accept="image/*" />
													<c:if test="${empty userObject.pictureByteArrayId}">
<%-- 								column						<img class="ie8" id="profileImage" style = "width: 200px; height: 200px; background-position: center center; background-repeat: no-repeat;" src="/images/profile/default.png" alt="${userObject.username}" />
 --%>														<input class="other" type="image" id="profileImage1" width="200px" height="200px" src="/images/profile/default.png" alt="${userObject.username}" />
											<!--			<input type="file" name="file" id="file" size="20" accept="image/*" style="display: none;" />-->
													</c:if>
													
											</div>
			<!-- 							</div> -->
			<!-- 						</div> -->
								</div>
								<div class="span9" style="width:78%">
								<div class="control-group" id="onlyIe8">
								
													<%-- <eazytec:label styleClass="control-label"
														key="user.profileImage" />
													<div class="controls checkbox-label">
													
														<input type="file" name="file" id="file" size="20" accept="image/*"/>
													</div> --%>
													
								</div>
								<div class="control-group">
									<eazytec:label styleClass="control-label " key="user.language" />
									<div class="controls">
										<form:select path="language" id="language" name="language" class="span6">
											<option value="zh_CN" ${fn:contains(userObject.language, 'zh_CN') ? 'selected' : '' } ><fmt:message key="user.language.chinese"/></option>
											<option value="en" ${fn:contains(userObject.language, 'en') ? 'selected' : ''}><fmt:message key="user.language.english"/></option>
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
											<option value="googleplus" ${fn:contains(userObject.preferredSkin, 'google') ? 'selected' : ''}><fmt:message key="user.theming.google+"/></option>
											<option value="instagram" ${fn:contains(userObject.preferredSkin, 'instagram') ? 'selected' : ''}><fmt:message key="user.theming.instagram"/></option>
											<option value="whitesmoke" ${fn:contains(userObject.preferredSkin, 'whitesmoke') ? 'selected' : ''}><fmt:message key="user.theming.whiteSmoke"/></option>
											<option value="grey" ${fn:contains(userObject.preferredSkin, 'grey') ? 'selected' : ''}><fmt:message key="user.theming.grey"/></option> 
										</form:select>
									</div>
								</div>
							
							<div class="control-group">
								<eazytec:label styleClass="control-label" key="user.currentPassword" />
								<div class="controls">
									<input type="password" id="oldPassword" name="oldPassword" class="span6"  /> 
									<label><small><fmt:message key="userProfile.currentPassword"/></small></label>
								</div>
							</div>
							
							<div class="control-group">
								<eazytec:label styleClass="control-label " key="userProfile.newPasssword" />
								<div class="controls">
									<form:password path="password" id="newPassword" class="span6" />
									<label><small><fmt:message key="userProfile.specifyPassword"/></small></label>
								</div>
							</div>
																																																																		
							<div class="control-group">
								<eazytec:label styleClass="control-label" key="userProfile.confirmPassword" />
								<div class="controls">
									<input type="password" id="reEnterNewPassword" class="span6" /> 
									<label class="displayNone color_red" id="passwordMissMatchLabel"></label>
									<label><small><fmt:message key="userProfile.confirmNewPassword"/></small></label>
								</div>
							</div>
							
							
							</div>
							<div class="control-group">
								<div class="form-actions no-margin" style="padding-left:48%;">
									<button type="button" class="btn btn-primary" name="save" id="save" onclick="passwordCheck()">
										<fmt:message key="button.update" />
									</button>
									<div class="clearfix"></div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
			<!--</div>
		</div>-->
