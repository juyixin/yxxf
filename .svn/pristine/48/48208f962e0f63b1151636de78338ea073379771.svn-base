<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ page import="com.eazytec.common.util.CommonUtil,org.springframework.security.core.context.SecurityContextHolder,com.eazytec.model.User"%>
<%
User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String currentRoles = CommonUtil.getRolesAsString(user);
String assMenus = "assignedMenus_"+user.getId();
String currentUserId = user.getId();
String userFullName = user.getFirstName()+" "+user.getLastName();
%>
<%@ page import ="com.eazytec.Constants" %>
<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<html lang="zh-cn">
<head>
	<title><decorator:title/> | 宜兴信访</title>
	<meta http-equiv="Cache-Control" content="IE=edge;no-store"/>
    <meta http-equiv="Pragma" content="IE=edge;no-cache"/>
    <meta http-equiv="Content-Type" content="IE=edge;text/html; charset=utf-8"/>
    <meta name="viewport" content="IE=edge;width=device-width, initial-scale=1.0"/>    
    
	<c:set var="prefLang" value="<%=request.getSession().getAttribute(Constants.PREFERRED_LOCALE_KEY)%>"/>
	<c:set var="prefSkin" value="<%=request.getSession().getAttribute(Constants.USER_PREFERRED_SKIN)%>"/>
	<c:set var="loggedInUser" value="<%= SecurityContextHolder.getContext().getAuthentication().getPrincipal()%>"/>
	
	<script type="text/javascript" src="<c:url value='/scripts/boostrap/html5-trunk.js'/>"></script>
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/bootstrap/icomoon/style.css'/>" />

	<!--[if lte IE 7]>
    <script type="text/javascript" src="<c:url value='/style/bootstrap/icomoon/lte-ie7.js'/>"></script>
    <![endif]-->
    
    <c:choose>
	<c:when test="${fn:contains(header['User-Agent'],'MSIE')}">
		<%@ include file = "/common/includeStyleForIE.jsp" %>
    	<%@ include file = "/common/includeScriptForIE.jsp" %>
	</c:when>
	<c:otherwise>
		<%@ include file = "/common/includeStyle.jsp" %>
    	<%@ include file = "/common/includeScript.jsp" %>
	</c:otherwise>
</c:choose>
    
<decorator:head/>

<script type="text/javascript">
var _mainContainer = "target";
var _params = "";
var topMenuArray = new Array();

$.fn.slideFadeToggle = function(easing, callback) {
    return this.animate({ opacity: 'toggle', height: 'toggle' }, "fast", easing, callback);
};

$(document).click(function(e) {
	if(filterURL.indexOf("processEditor") > -1){
		e.preventDefault();
	}
});

window.onbeforeunload = function (e) {
	if(filterURL.indexOf("processEditor")> -1){
		refresh = "true";
	}
};


$(document).ready(function () {
	var userName = '${pageContext.request.remoteUser}';
   	var fullName = '${loggedInUser.fullName}';
   	var email = '${loggedInUser.email}';
   	var position = '${loggedInUser.position}';
   	var profileImage = '${loggedInUser.pictureByteArrayId}';
   	
	setSidebarHeight2();
	var index = 0;
	<c:forEach items='<%= this.getServletContext().getAttribute(assMenus) %>' var="menu">
		if("${menu.isParent}" == "true" && ("${menu.menuType}"=='top' || "${menu.menuType}"=='header') && "${menu.active}" == "true")	{
			var menu = "${menu.name}";
			var menuName = menu.toLowerCase();
			var menuName = menuName.replace(/\s+/g, '');
			topMenuArray[index] = menuName;
			index ++;
			$("#accordion_"+menuName).accordion({
				collapsible: true,
				animated: 'slide',
				autoHeight: false,
				navigation: true
			});
		}
	</c:forEach> 			
	for(var index = 0; index < topMenuArray.length; index++){
		var menuName = topMenuArray[index];
		if(menuName == 'home'){
			 $("#"+menuName+"_menu").show();
			 $("#"+menuName+"_top_menu").removeClass('top-menu').addClass('top-menu-active');
			 $("#"+menuName+"_menu_onselect_arrow").show();
			 $("#"+menuName+"_menu_onselect_menu2").show();
			 $("#"+menuName+"_menu_onselect_menu1").hide();
		}else{
			$("#"+menuName+"_menu").hide();
			$("#"+menuName+"_top_menu").removeClass('top-menu-active').addClass('top-menu');
			$("#"+menuName+"_menu_onselect_arrow").hide();
			$("#"+menuName+"_menu_onselect_menu1").show();
			$("#"+menuName+"_menu_onselect_menu2").hide();
		}
	}	
});
  
function getDefaultValForForm(type){
	var dayMap = {0:'Sunday', 1:'Monday', 2:'Tuesday', 3:'Wednesday', 4:'Thursday', 5:'Friday', 6:'Saturday'};
	var monthMap = {0:'January', 1:'February', 2:'March', 3:'April', 4:'May', 5:'June', 6:'July', 7:'August', 8:'September', 9:'October', 10:'November', 11:'December'};
	if(type.toLowerCase() == "time"){
		return '<fmt:formatDate value="<%= new java.util.Date() %>" pattern="HH:mm" />';
	}else if(type.toLowerCase() == "day"){
		var day = "<%= new java.util.Date().getDay() %>";
		return dayMap[day];
	}else if(type.toLowerCase() == "month"){
		var month = "<%= new java.util.Date().getMonth() %>";
		return monthMap[month];
	}else if(type.toLowerCase() == "year"){
		return '<fmt:formatDate value="<%= new java.util.Date() %>" pattern="yyyy" />';
	}else if(type.toLowerCase() == "date"){
		return '<fmt:formatDate value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd" />';
	}else if(type.toLowerCase() == "datetime"){
		return '<fmt:formatDate value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd HH:mm" />';
	}else if(type.toLowerCase() == "username"){
		return "${username}";
	}else if(type.toLowerCase() == "userlogin"){
		return "${userlogin}";
	}else if(type.toLowerCase() == "userid"){
		return "${userid}";
	}else if(type.toLowerCase() == "deptname"){
		return "${deptname}";
	}else if(type.toLowerCase() == "deptid"){
		return "${deptid}";
	}else if(type.toLowerCase() == "telephone"){
		return "${telephone}";
	}else if(type.toLowerCase() == "roleid"){
		return "${roleid}";
	}else if(type.toLowerCase() == "groupid"){
		return "${groupid}";
	}else if(type.toLowerCase() == "apppath"){
		return '<%=request.getRealPath("")%>';
	}
}
    
function setIndexPage(tempindexPage,menuType){
	$(".submenu li").removeClass('side-menu-active');
  	$(".submenu li a").removeClass('side-menu-active');
  	$("#"+tempindexPage).addClass('side-menu-active');
}
    
function _execute(container, requestedparams) {
	_mainContainer = container;
   	if(requestedparams != ""){
   		_params = requestedparams;
   	}
   	var currentURL = window.location.href;
	if(currentURL.indexOf("#bpm/admin/getAllDictionariesAsLabelValue") == -1){
    	destroyCKEditor();
	}
}
    
function showSideMenu(topMenu,tableId){
   	var activeIndex = 0;
   	if(tableId != ""){
   		if(document.getElementById(tableId)){
   			activeIndex=parseInt(document.getElementById(tableId).getAttribute('accordianindex'));	
   		}
   	}
   	for(var index = 0; index < topMenuArray.length; index++){
   		var menuName = topMenuArray[index];
   		if(menuName == topMenu){
   			 $("#"+menuName+"_menu").show();
   			 $("#"+menuName+"_top_menu").removeClass('top-menu').addClass('top-menu-active');
   			 $("#"+menuName+"_header_menu").removeClass('header-menu-background').addClass('header-menu-active');
   			 $("#"+menuName+"_menu_onselect_arrow").show();
   			 //$("#accordion_"+menuName).filter(":not(:has(.ui-state-active))").accordion("activate", 0);
   			 if(tableId != ""){
				 var east_container = document.getElementById(tableId);
				 if(east_container){
					 var parent_node_id = east_container.parentNode;
					 if(parent_node_id.parentNode.style.display){
						 if(parent_node_id.parentNode.style.display == "none"){
							$("#accordion_"+menuName).filter(":not(:has(.ui-state-active))").accordion("activate", activeIndex);	
							$("#accordion_"+menuName).accordion("activate", activeIndex);								 
						 }
					 } 
				 }
			 }/* else{
				$("#accordion_"+menuName).filter(":not(:has(.ui-state-active))").accordion("activate", activeIndex);	
			 	$("#accordion_"+menuName).accordion("activate", activeIndex);
			 } */
   			 $("#"+menuName+"_menu_onselect_menu1").hide();
   			 $("#"+menuName+"_menu_onselect_menu2").show();
   		}else{
   			$("#"+menuName+"_header_menu").removeClass('header-menu-active').addClass('header-menu-background');
   			$("#"+menuName+"_menu").hide();
   			$("#"+menuName+"_top_menu").removeClass('top-menu-active').addClass('top-menu');
   			$("#"+menuName+"_menu_onselect_arrow").hide();
   			$("#"+menuName+"_menu_onselect_menu1").show();
   			$("#"+menuName+"_menu_onselect_menu2").hide();
   		}
   	}	
}

loadLogo();    
function loadLogo(){
   	$.ajax({
   		url: 'bpm/fileupload/getLogo',
   	    type: 'GET',
   	 	dataType: 'json',
   		success : function(response) {
   			var res = eval(response);
   			if(res.length == 1){
				$("#bpm_logo").attr("src", res);
			}
   		}
   	});
}
</script>
</head>
<body<decorator:getProperty property="body.id" writeEntireProperty="true"/> class="eazybpmBody" id="default_body">
	<c:set var="currentMenu" scope="request"><decorator:getProperty property="meta.menu"/></c:set>
	<header id="header_menu">
		<a href="/" class="logo"><img id="bpm_logo" src="/images/logo/logo.png" alt="Logo" style="height:30px;"></a>
		<%-- <a href="#" class="department">部门 : '${loggedInUser.department.viewName}'</a> --%>
		<div id="mini-nav">
			<%@ include file="/common/headerMenu.jsp" %>  
			<div class="clearfix"></div>
		</div>
	</header>

	<div class="container-fluid">
		<div id="side_menu" class="left-sidebar hidden-tablet hidden-phone">
			<div class="user-details">
				<div class="user-img">
					<!-- <img src="/images/bootstrap/user-.png" class="" alt=""> -->
					<img src="${loggedInUser.pictureByteArrayId}" class="" alt="">
				</div>
				<div class="welcome-text">
					<span>欢迎</span>
					<p class="name">${loggedInUser.fullName}</p>
				</div>
			</div>
			<div class="content min-height-0px">
				<div id="accordion1" class="accordion no-margin">
					<!-- 
					<div class="accordion-group">
						<div class="accordion-heading">
							<a href="#collapseOne" data-parent="#accordion1" data-toggle="collapse" class="accordion-toggle"> 在线<span id="userCounter" class="label info-label label-success"></span></a>
						</div>
						<div class="accordion-body in collapse" id="collapseOne" style="height: auto;">
							<div class="accordion-inner">
								<div class="online-users">
									<div id="searchDiv"><span class="style1 fontColor" style="cursor: pointer;"><input type="text" id="searchOnlineUsers" name="searchOnlineUsers" class="style1 fontColor" style="width:91%" onkeyup="loadSelectedOnlineUser();" placeholder="搜索用户" /></span><br /></div>
									<ul id="members" style="margin-bottom:14px;">
									</ul>
								</div>
							</div>
						</div>
					</div>
					 -->
					<%@ include file="/common/mailMenu.jsp" %>
				</div>
			</div>
		</div>
		<div class="dashboard-wrapper" id="dashboard-wrapper">
			<div id="top_menu">
				<div id="main-nav">
		       		<%@ include file="/common/topMenu.jsp" %> 
		         <div class="clearfix"></div>
		       </div>
	       </div>
			<div class="main-container">
				<div class="navbar hidden-desktop">
					<div class="navbar-inner">
						<div class="container">
							<a data-target=".navbar-responsive-collapse" data-toggle="collapse" class="btn btn-navbar"> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span></a>
							<div class="nav-collapse collapse navbar-responsive-collapse">
								<%@ include file="/common/mobileMenu.jsp" %> 
							</div>
						</div>
					</div>
				</div>			
		
				<div class="row-fluid">
					<div class="span12" id="ajax_loader">
						<div id="target_iframe_div" style="display:none;">
							<iframe id="target_iframe"></iframe>
			    		</div>
						<div id="target_div">
							
							<div id="target"></div>
							
							<div id="popupDialog" class="form-background"></div>	
							<div id="involveUsersPopupDialog"></div>
							<div id="userPopupDialog" class="form-background"></div>
							<div id="userPopupDialogManager"></div>
							<div id="userPopupDialogSecretary"></div>
							<div id="departmentPopupDialog"></div>
							<div id="partTimeDepartmentPopupDialog"></div>
							<div id="groupPopupDialog"></div>
							<div id="importPopupDialog"></div>
							<div id="importSQLPopupDialog"></div>
							<div id="importCSVPopupDialog"></div>
							
							<div id="exportMultiTablePopupDialog"></div>
							
							<div id="classificationPopupDialog"></div>
							<div id="processPreviewPopupDialog" style="text-align: center;vertical-align: middle;"></div>
							<div id="formAutoGenerationId"></div>
							<div id="tableRelationForeignKey"></div>
							<div id="doc_view_dialog" class="form-background"></div>
							<div id="print_preview" class="displayNone"></div>
			    		</div>
			    	</div>
					
				</div>
				
			</div>
		</div>
		<!-- dashboard-container -->
	</div>
	<input id="currentUserRoles" type="hidden" value="<%=currentRoles%>"  />
	<input id="currentUserId" type="hidden" value="<%=currentUserId%>"  />	
	<input id="userFullName" type="hidden" value="<%=userFullName%>"  />
	<input type="hidden" name="dbSource" id="dbSource" value="<fmt:message key='db.source'/>"/>
    
    <div id="evidence" class="form-background"></div>
    
<script type="text/javascript">
$.cookie('windowHeight',$(window).height());

function goToInbox(type) {
	 if(type == "Mail") {
		 document.location.href = "#bpm/mail/jwma/showMailInbox";
		 _execute('target','');
	 } else {
		 document.location.href = "#bpm/messages/showInbox";
		 _execute('target','');
	 }
}

function generateMailRemainder(layout,message,type) {
    var n = noty({
        text        : '<a onclick="goToInbox(\''+type+'\');"><img src="/images/logo/mail.png" height="50px" width="50px" alt="EazyTec">&nbsp;&nbsp;&nbsp;&nbsp;'+message+'</a>',
        type        : 'information',
        dismissQueue: false,
        layout      : layout,
        theme       : 'defaultTheme'
    });
}

$(document).ready(function(){
	
	mailCount();
	
	var homeLoded = false;
	var windowLocation = window.location;
	if(window.location == windowLocation.protocol + '//' + windowLocation.hostname + ':' + windowLocation.port + windowLocation.pathname && homeLoded == false){
		/* checkMenu('home','url','/user/homePage','','','','','top','','top_menu_ff8081813c8630b9013c863c52110001','home');
		setBreadCrumb('home', '', '','/user/homePage');
		document.location.href = "#bpm/user/homePage";
		_execute('target', '');
		homeLoded = true; */
	}
	$('#headNewTab,#topNewTab,#sideMenuNewTab a').click(function(event) {
		event.preventDefault();
		var location = $(this).attr('name');
		var newTabUrl = $(this).attr('href');
		if(location == "newTab") {
			if(newTabUrl.charAt(0) == '/') {
				window.open("#bpm"+newTabUrl);
				_execute('target', '');	
			}else if(newTabUrl.toLowerCase().substring(0, 3) == 'www') {
				window.open("http://"+newTabUrl);
			    _execute('target', '');				
			}else if( (newTabUrl.toLowerCase().indexOf("http") > -1 ) || (newTabUrl.toLowerCase().indexOf("https") > -1 ) ) {
				window.open(newTabUrl);
			    _execute('target', '');				
			}/* else if(newTabUrl.toLowerCase().substring(0, 4) == '#bpm') {
				window.open("http://"+newTabUrl.substring(4));
			    _execute('target', '');				
			} */else {
				window.open("http://"+newTabUrl);
			    _execute('target', '');					
			}
		}else if(homeLoded == false){
			checkMenu('home2','url','/user/homePage','','','','','top','','top_menu_ff8081813c8630b9013c863c52110001','home');
			setBreadCrumb('home2', '', '','/user/homePage','','');
			document.location.href = "#bpm/user/homePage";
			_execute('target', '');
			homeLoded = true;
		}
	});
	
	var bpmMenuName = $.cookie('bpmMenuName');
    var bpmUrlType = $.cookie('bpmUrlType');
    var bpmMenuUrl = $.cookie('bpmMenuUrl');
    var bpmTableId = $.cookie('bpmTableId');
    var bpmListViewName = $.cookie('bpmListViewName');
    var bpmListViewParams = $.cookie('bpmListViewParams'); 
    var bpmProcessName=$.cookie('bpmProcessName');
    var bpmMenuType=$.cookie('bpmMenuType',bpmMenuType);
    
    var bpmSideMenuName=$.cookie('bpmSideMenuName');
	var bpmSideUrlType=$.cookie('bpmSideUrlType');
	var bpmSideAppBack=$.cookie('bpmSideAppBack');
	var bpmSideTableId=$.cookie('bpmSideTableId');
	var bpmSideListViewName=$.cookie('bpmSideListViewName');
	var bpmSideListViewParams=$.cookie('bpmSideListViewParams');
	var bpmSideProcessName=$.cookie('bpmSideProcessName');
	var bpmSideScript=$.cookie('bpmSideScript');
	var bpmSideMenuType=$.cookie('bpmSideMenuType');
	
	if(bpmTableId!=null && bpmTableId!="" && bpmMenuUrl!="" && bpmMenuUrl!=null){
		setIndexPage(bpmTableId);
        checkMenu(bpmMenuName,bpmUrlType,bpmMenuUrl,bpmTableId,bpmListViewName,bpmListViewParams,bpmProcessName,bpmMenuType,'');
 	}
    
    if(bpmSideTableId!=null && bpmSideTableId!="" && bpmSideUrlType!="" && bpmSideUrlType!=null){
    	if(bpmSideUrlType=='url'){
    		document.location.href = "#bpm"+bpmSideAppBack;
    	}else if(bpmSideUrlType=='newTab'){
    		document.location.href = bpmSideAppBack;
    	}
    	
 	  	showSideMenu(bpmMenuName,bpmSideTableId);
 	  	setTimeout(function(){setIndexPage(bpmSideTableId)},1000);
   		setTimeout(function(){checkMenu(bpmSideMenuName,bpmSideUrlType,bpmSideAppBack,bpmSideTableId,bpmSideListViewName,bpmSideListViewParams,bpmSideProcessName,bpmSideMenuType,bpmSideScript)},950);
   		
   		if(bpmSideUrlType=='script'){
   			var callback_function = new Function(bpmSideScript);
   			setTimeout(function(){callback_function()},1000);
   		}
   		resetSubSideMenu();
   	}else if(bpmMenuName!="" && bpmTableId!=null && bpmTableId!= ""){
   		showSideMenu(bpmMenuName, bpmTableId);
   		setTimeout(function(){checkMenu(bpmMenuName,bpmUrlType,'',bpmTableId,bpmListViewName,bpmListViewParams,bpmProcessName,bpmMenuType,'')},1050);
	}else if(homeLoded == false){
		checkMenu('首页','url','/user/homePage','','','','','top','','top_menu_ff8081813c8630b9013c863c52110001','home');
		setBreadCrumb('首页', '', '','/user/homePage','','');
	   	document.location.href = "#bpm/user/homePage";
	   	_execute('target','');
	   	homeLoded = true;
	}
    
    homeLoded = false;
    
});
 
$(window).load(function () {
    $('head').append('<link href="/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />');
});
  
$( window ).resize(function() {
	setSidebarHeight2();
	var windowHeight = $(window).height();
	var windowOldHeight = $.cookie('windowHeight');
	var bpmMenuName = $.cookie('bpmMenuName');
    var bpmUrlType = $.cookie('bpmUrlType');
    var bpmMenuUrl = $.cookie('bpmMenuUrl');
    var bpmTableId = $.cookie('bpmTableId');
    var bpmListViewName = $.cookie('bpmListViewName');
    var bpmListViewParams = $.cookie('bpmListViewParams'); 
    var bpmProcessName=$.cookie('bpmProcessName');
    var bpmMenuType=$.cookie('bpmMenuType');
    var rootMenuUrl = $.cookie('rootMenuUrl');
    
    var bpmsidemenutype = $.cookie('bpmSideMenuType');
	var menutype="";
	var urlType = "";
	var listViewParams = "";
	var listViewName = "";
	var bpmAppBack = "";
	if(bpmsidemenutype == null || bpmsidemenutype == ""){
	menutype = bpmMenuType;
	} else {
	menutype = bpmsidemenutype;
	}
	if(menutype == "side" || menutype == "Side" ){
		urlType = $.cookie('bpmSideUrlType');
		listViewParams = $.cookie('bpmSideListViewParams');
		listViewName = $.cookie('bpmSideListViewName');
		var script = $.cookie('bpmSideScript');
		if(script != null || script !="null" || script != ''){
			
			var callback_function = new Function(script);
   			setTimeout(function(){callback_function();},100);
		}
	} else {
	urlType = $.cookie('bpmUrlType');
	listViewParams = $.cookie('bpmListViewParams');
	listViewName = $.cookie('bpmListViewName');
	}
    if(bpmMenuUrl != null && (windowOldHeight != windowHeight) && rootMenuUrl != "/user/homePage"){
		if((windowOldHeight < windowHeight) && (windowOldHeight < (parseInt(windowHeight)-100))){
			if(urlType == "listview" || urlType == "listView"){
		    	if(listViewParams != ""){
		    		var listViewParams = "[{"+listViewParams+"}]";
		    		var listViewNameAsTitle=listViewName;    	
		    		checkMenu(bpmMenuName,bpmUrlType,bpmMenuUrl,bpmTableId,listViewName,listViewParams,bpmProcessName,bpmMenuType,'');
		    		showListViewsWithContainerAndParam(listViewName, listViewNameAsTitle.replace('_',' '), 'target', eval(listViewParams));
		    	}else{
		    		var listViewNameAsTitle=listViewName;
		    		checkMenu(bpmMenuName,bpmUrlType,bpmMenuUrl,bpmTableId,listViewName,listViewParams,bpmProcessName,bpmMenuType,'');
		    		showListViewsWithContainer(listViewName, listViewNameAsTitle.replace('_',' '), 'target');
		    	}
		    } else {
		    	/*$.cookie('windowHeight',$(window).height());
		        checkMenu(bpmMenuName,bpmUrlType,bpmMenuUrl,bpmTableId,listViewName,listViewParams,bpmProcessName,bpmMenuType,'');
			   	document.location.href = '#'+window.filterURL+'';
			   	_execute('target','');*/
		    }
		} else if((windowOldHeight > windowHeight) && (windowOldHeight > (parseInt(windowHeight)+100))){
			if(urlType == "listview" || urlType == "listView"){
		    	if(listViewParams != ""){
		    		var listViewParams = "[{"+listViewParams+"}]";
		    		var listViewNameAsTitle=listViewName;    	
		    		$.cookie('bpmListViewName',listViewName);
		    		$.cookie('bpmUrlType',urlType);
		    		//checkMenu(bpmMenuName,bpmUrlType,bpmMenuUrl,bpmTableId,listViewName,listViewParams,bpmProcessName,bpmMenuType,'');
		    		showListViewsWithContainerAndParam(listViewName, listViewNameAsTitle.replace('_',' '), 'target', eval(listViewParams));
		    	}else{
		    		var listViewNameAsTitle=listViewName;
		    		//checkMenu(bpmMenuName,bpmUrlType,bpmMenuUrl,bpmTableId,listViewName,listViewParams,bpmProcessName,bpmMenuType,'');
		    		$.cookie('bpmListViewName',listViewName);
		    		$.cookie('bpmUrlType',urlType);
		    		showListViewsWithContainer(listViewName, listViewNameAsTitle.replace('_',' '), 'target');
		    	}
		    } else {
		    	/*$.cookie('windowHeight',$(window).height());
		        checkMenu(bpmMenuName,bpmUrlType,bpmMenuUrl,bpmTableId,listViewName,listViewParams,bpmProcessName,bpmMenuType,'');
			   	document.location.href =  '#'+window.filterURL+'';
			   	_execute('target','');*/
		    }
		}
		$.cookie('windowHeight',windowHeight);
 	} else if(rootMenuUrl == "/user/homePage") {
			
 		if((windowOldHeight < windowHeight) && (windowOldHeight < (parseInt(windowHeight)-100))){
			$.cookie('windowHeight',$(window).height());
	        checkMenu(bpmMenuName,bpmUrlType,bpmMenuUrl,bpmTableId,bpmListViewName,bpmListViewParams,bpmProcessName,bpmMenuType,'');
		   	document.location.href = '#'+window.filterURL+'';
		   	_execute('target','');
		} else if((windowOldHeight > windowHeight) && (windowOldHeight > (parseInt(windowHeight)+100))){
			$.cookie('windowHeight',$(window).height());
	        checkMenu(bpmMenuName,bpmUrlType,bpmMenuUrl,bpmTableId,bpmListViewName,bpmListViewParams,bpmProcessName,bpmMenuType,'');
		   	document.location.href =  '#'+window.filterURL+'';
		   	_execute('target','');
		}
 		$.cookie('windowHeight',windowHeight);
 	}
 	
});



//setInterval("mailCount()",3000);

var oldMailCount = 0;
var oldMessageCount = 0;

function mailCount(){
	
	$.ajax({
		url: 'bpm/mail/getCount',
        type: 'GET',
        dataType: 'html',
        contentType: 'application/json; charset=UTF-8',
		async: false,
		data: '',
		success : function(response) {
			if(response.length > 0){
				var obj = $.parseJSON(response);
				
				var mailCount = obj['unreadEmailCount'];
	         	var messageCount = obj['unreadMessageCount'];
				
				if ($('#mailCount').length > 0) {
	        		$('#mailCount').html('' +mailCount+'');
		       	}else{
		       		$('#parent-ff8081813cb4dd84013cb4f24a5f0000').append(' <span id="mailCount" class="label info-label label-warning">'+mailCount+'</span>');
		       	}
	         	
	         	if ($('#messageCount').length > 0) {
		       		$('#messageCount').html('' +messageCount+'');
		    	}else{
		    		$('#parent-a5699bf2-51a9-11e3-bfeb-e069959ac2ac').append(' <span id="messageCount" class="label info-label label-important">'+messageCount+'</span>');
		    	}

				if($('#toDoListCount').length > 0){
		    		$('#toDoListCount').html('(' +obj['toDoListCount']+')');
		    	} else {
		    		$('#72c8234a-87f5-11e2-eeee-e069959ac296').append(' <span id="toDoListCount">('+obj['toDoListCount']+')</span>');
		    	}
				
		    	if($('#toReadCount').length > 0){
		    		$('#toReadCount').html('(' +obj['toReadCount']+')');
		    	} else {
		    		$('#ff8081813eeb4537013eeb4b964cffff').append(' <span id="toReadCount">('+obj['toReadCount']+')</span>');
		    	}
		    	
		    	
		    	if(oldMailCount != mailCount){
					if(oldMailCount < mailCount && mailCount != 0){
						generateMailRemainder('bottomRight','您有新的邮件，请注意查收!','Mail');   
					} 
					oldMailCount = mailCount;
				} 
				
				if(oldMessageCount != messageCount){
					if(oldMessageCount < messageCount && messageCount != 0 ){
						generateMailRemainder('bottomLeft','您有新的消息，请注意查收!','Message');   
					}
					oldMessageCount = messageCount;
				} 
		    	
			}
		}
	});
}

</script>
</body>
</html>
