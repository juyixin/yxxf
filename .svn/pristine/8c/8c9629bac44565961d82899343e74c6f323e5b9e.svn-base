<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<!--[if lt IE 7]>
    <html class="lt-ie9 lt-ie8 lt-ie7" lang="en">
  <![endif]-->

  <!--[if IE 7]>
    <html class="lt-ie9 lt-ie8" lang="en">
  <![endif]-->

  <!--[if IE 8]>
    <html class="lt-ie9" lang="en">
  <![endif]-->

  <!--[if gt IE 8]>
    <!-->
    <html lang="en">
    <!--
  <![endif]-->
<head>
	<link rel="icon" type="image/x-icon" href="/images/favicon.ico?v=2">
	<link rel="shortcut icon" type="image/x-icon" href="/images/favicon.ico?v=2">
	<meta charset="utf-8">
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title><decorator:title/> | 宜兴信访</title>
	
	<script type="text/javascript" src="<c:url value='/scripts/boostrap/html5-trunk.js'/>"></script>
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/bootstrap/icomoon/style.css'/>" />
	
	<!--[if lte IE 7]>
    <script type="text/javascript" src="<c:url value='/style/bootstrap/icomoon/lte-ie7.js'/>"></script>
    <![endif]-->
    
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/bootstrap/main.css'/>" />
    
    <decorator:head/>
    
    <script type="text/javascript" src="<c:url value='/scripts/boostrap/jquery.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/boostrap/bootstrap.js'/>"></script>
    
    <script type="text/javascript" src="<c:url value='/scripts/lib/plugins/jquery.cookie.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/script.js'/>"></script>
    
</head>

<body<decorator:getProperty property="body.id" writeEntireProperty="true"/> <decorator:getProperty property="body.class" writeEntireProperty="true"/>>  <c:set var="currentMenu" scope="request"><decorator:getProperty property="meta.menu"/></c:set>
	<div class="container-fluid">
		<%@ include file="/common/messages.jsp" %>
		<div class="row-fluid">
			<decorator:body/>
			
		</div>
	</div>


    <%-- div class="navbar navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container-fluid" align="center">
            
            </div>
        </div>
    </div>

    <div class="container-fluid" style="min-height:400px;">
        <%@ include file="/common/messages.jsp" %>
        <div class="row-fluid">
            <decorator:body/>
        </div>
    </div> --%>
    
    <%-- <div id="footer">
        <span class="left"><fmt:message key="webapp.version"/>
            <c:if test="${pageContext.request.remoteUser != null}">
            | <fmt:message key="user.status"/> ${pageContext.request.remoteUser}
            </c:if>
        </span>
        <span class="right">
            &copy; <fmt:message key="copyright.year"/> <a href="<fmt:message key="company.url"/>"><fmt:message key="company.name"/></a>
        </span>
    </div> --%>
	<%= (request.getAttribute("scripts") != null) ?  request.getAttribute("scripts") : "" %>
</body> 

</html>