<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@page import="com.eazytec.util.PropertyReader"%>
<%@ page import="org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices" %>
<%@ page import="javax.servlet.http.Cookie" %>
<%@ page session="true" %>
<%@page import="java.util.UUID"%><head>

<head>
    <title><fmt:message key="login.title"/></title>
    <meta name="menu" content="Login"/>
<link href="styles/login.css" rel="stylesheet" type="text/css" media="all" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
    <%
	
		Boolean isCaptchaNeeded=Boolean.valueOf(PropertyReader.getInstance().getPropertyFromFile("Boolean", "system.captcha.needed"));
		String uuid = UUID.randomUUID().toString();
	%>
	<script type="text/javascript">

function randomUUID() {
	  var s = [], itoh = '0123456789ABCDEF';
	 
	  // Make array of random hex digits. The UUID only has 32 digits in it, but we
	  // allocate an extra items to make room for the '-'s we'll be inserting.
	  for (var i = 0; i < 36; i++) s[i] = Math.floor(Math.random()*0x10);
	 
	  // Conform to RFC-4122, section 4.4
	  s[14] = 4;  // Set 4 high bits of time_high field to version
	  s[19] = (s[19] & 0x3) | 0x8;  // Specify 2 high bits of clock sequence
	 
	  // Convert to hex chars
	  for (var i = 0; i < 36; i++) s[i] = itoh[s[i]];
	 
	  // Insert '-'s
	  s[8] = s[13] = s[18] = s[23] = '-';
	 
	  return s.join('');
	}

function showLicenseAlert() {
	var licenseKey = prompt("Enter License Key");
	if (licenseKey != null && licenseKey.length > 0) {
		licenseKey = licenseKey.replace(/&/g, '`');
		$.ajax({
			type: 'GET',
			async: false,
			url: '/bpm/license/saveLicense',
			data: 'licenseKey=' + licenseKey,
			success: function () {
				alert("License Key Added success Fully");
			}
		});
		//alert(a);
	} else {
		alert("Please, Enter the license key !!");
	}

}
	</script>
<style>
body{
	background-color: #E6EAED;
}
.container-fluid{
	padding: 0;
}

.main-body{
	width: 100%;
	margin:0 auto;
}
.main-top{
	width: 900px;
	margin: 0 auto;
}
.loing-top{
	height: 135px;
	background: url("../images/dwghxx_09.png") no-repeat 0 45px;
	position: relative;
}

.login-box{
	width: 353px;
	height: 320px;
	position: absolute;
	z-index:100;
	background: url("../images/dlk.png") no-repeat;
	left: 460px;
	top: 114px;
}
.login-box-body{
	width: 320px;
	margin: 0 auto;
}
.login-box-body img{
	display: block;
	margin: 0 auto;
}
.login-error{
	display: block;
	/*margin: 5px auto;*/
	color: red;
}
.login-box-form{
	width: 264px;
	margin: 0 auto;
}
#login-name,#login-pwd {
	height: 28px;
	border: 2px solid #C3CEE0;
	width: 220px;
	padding: 0;
	margin: 0;
	padding-left: 40px !important;
}
#login-name{
	background:url("../images/login-name.png") no-repeat 5px center #D1DEF1 !important;
}
#login-pwd{
	background:url("../images/login-pwd.png") no-repeat 5px center #D1DEF1 !important;
	margin-top:20px;
}
#login-btn{
	width: 146px;
	background:url("../images/login-btn-all.png");
	height: 38px;
	float: none;
	border-radius: 0;
	border: 0;
	font-size: 16px;
	text-align: center;
	padding: 0;
}

#login-btn-body{
	width: 146px;
	margin: 20px auto;
}
#loginForm{
	padding: 0;
	background: none;
}
</style>
</head>
<body id="body">
<div class="main-top loing-top">
	<div class="login-box">
		<form method="post" id="loginForm" action="<c:url value='/j_spring_security_check'/>" onsubmit="saveUsername(this);return validateForm(this)" class="signin-wrapper"  autocomplete="off">
			<div class="login-box-body">
				<img src="../images/login/yhdl_03.png" style="margin-top: 30px;margin-bottom: 22px;">
				<img src="../images/login/shangxian_03.png">
				<div class="login-box-form">
					<div style="height: 24px;line-height: 24px;">
					<c:if test="${param.error != null}">
						<c:choose>
							<c:when test = "${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message == 'Bad credentials'}">
								<fmt:message key="errors.password.mismatch"/>

							</c:when>
							<c:otherwise>
								<span class="login-error">请输入正确的用户名密码</span>
								<%--<c:out value="${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}"/>--%>
								<c:set var="license" value="${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}"/>
								<c:if test="${fn:contains(license, 'Host')}">
									<a href="" onclick="showLicenseAlert()"/>License Save</a>
								</c:if>

							</c:otherwise>
						</c:choose>
					</c:if>
					</div>
					<input id="login-name" name="j_username" class="login-name" type="text" autocomplete="off"/>
					<input id="login-pwd" name="j_password" type="password"/>
					<div>
						<input type="checkbox">
						<span style="color: #A19B9B;margin:5px 0 5px 5px;display: inline-block;">记住密码</span>
					</div>

				</div>

				<%--<div style="height:9px;width:320px;background: url('../images/login/xiantiao_03.png') "></div>--%>
				<img src="../images/login/xiahua_03.png">
			</div>
			<div style="width: 264px;margin: 0 auto;">
				<div id="login-btn-body" >
					<input id="login-btn" type="submit" value="登 录"/>
				</div>
			</div>
		</form>
	</div>
</div>
<div style="height: 356px;width: 100%;background: #019cee;">

	<div style="width: 70%;float: left;height:356px;text-align: right;">
		<img src="../images/login/swdl_06.png;" style="margin-right: 250px;">
	</div>
	<div style="width: 30%;float: left;height:356px;"><img src="../images/login/swdl_08.png"></div>
</div>
<div style="width: 100%;">
	<div style="width: 500px;text-align: center;font-size: 14px;margin:0 auto;padding-top: 60px;">
		技术支持：江苏卓易信息科技股份有限公司
	</div>

</div>
<div class="clear"> </div>
<c:set var="scripts" scope="request">
<%@ include file="/scripts/login.js"%>
</c:set>
</body>
</html>