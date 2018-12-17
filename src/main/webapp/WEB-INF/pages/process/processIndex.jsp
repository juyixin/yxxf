<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.Locale"%>
<%@ include file="/common/taglibs.jsp"%>

<%
String processName =  request.getParameter("name");
Locale locale = request.getLocale();

%> 

<script type="text/javascript">
/**
 *For windows Chrome and Internet Explorer  back space Event prevent purpose
 */
function preventBackSpaceEvent(e) {
    var KeyID = (window.event) ? event.keyCode : e.keyCode || e.charCode;
    if(KeyID==8 && ((e.target || e.srcElement).tagName != "TEXTAREA") && ((e.target || e.srcElement).tagName != "INPUT")) {
    	if (navigator.userAgent.toLowerCase().indexOf("msie") == -1) {
    		e.stopPropagation();
		e.preventDefault();
		e.cancelBubble = true;
	} else {
		e.returnValue = false;
	}
	e.stopPropagation();
        return false;
    }
} 

window.onkeydown = preventBackSpaceEvent;

/**
 *For windows Firefox Back Space Event prevent purpose
 */
window.onkeypress = function(event) {
	if (!event) { 
		event = window.event;
	}
	var keyCode = event.keyCode;
	if (keyCode == 8 &&((event.target || event.srcElement).tagName != "TEXTAREA") && ((event.target || event.srcElement).tagName != "INPUT")) { 
		if (navigator.userAgent.toLowerCase().indexOf("msie") == -1) {
			event.stopPropagation();
			event.preventDefault();
			e.cancelBubble = true;
		} else {
			alert("prevented");
			event.returnValue = false;
		}
		return false;
	}
};
</script>

<!-- libraries -->
<!-- oryx plugin -->
<!-- libraries -->

<script type="text/javascript" src="<c:url value='/oryx/lib/prototype-1.5.1.js'/>"></script>
<!-- <script type="text/javascript">jQuery.noConflict();</script> -->
<script type="text/javascript" src="<c:url value='/oryx/lib/path_parser.js'/>"></script>
<script type="text/javascript" src="<c:url value='/oryx/lib/ext-2.0.2/adapter/ext/ext-base.js'/>"></script>
<script type="text/javascript" src="<c:url value='/oryx/lib/ext-2.0.2/ext-all-debug.js'/>"></script>
<script type="text/javascript" src="<c:url value='/oryx/lib/ext-2.0.2/color-field.js'/>"></script>

<!--  Js files inclusion for tree structure in process modeler -->
<script type="text/javascript" src="<c:url value='/oryx/customtree/build/yahoo.js'/>"></script>
<script type="text/javascript" src="<c:url value='/oryx/customtree/build/event.js'/>"></script>
<script type="text/javascript" src="<c:url value='/oryx/customtree/build/treeview.js'/>"></script>
<script type="text/javascript" src="<c:url value='/oryx/customtree/build/jktreeview.js'/>"></script>

<!-- Process Modeler js -->
<script type="text/javascript" src="<c:url value='/scripts/easybpm/processmodeler/ProcessModeler.js' />"></script>


<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/oryx/lib/ext-2.0.2/resources/css/ext-all.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/oryx/lib/ext-2.0.2/resources/css/xtheme-gray.css'/>" />

<!--       Temporary CSS Below is Style sheet for demos. Removed if desired -->
<style type="text/css">

body {font: normal 11px verdana, sans-serif; color: #333; line-height: 19px;}
a { text-decoration: underline; color: #46546C; }
a:hover { text-decoration: underline; color: #4d77c3; }
/* #tree2 {width:auto;padding: 10px;float:left;} */


button.btn, input.btn[type="submit"] {
}
.btn {
    border-color: #D1D2D3;
}
.btn {
    border-color: rgba(0, 0, 0, 0.15) rgba(0, 0, 0, 0.15) rgba(0, 0, 0, 0.25);
}
.btn {
    -moz-border-bottom-colors: none;
    -moz-border-left-colors: none;
    -moz-border-right-colors: none;
    -moz-border-top-colors: none;
    background-color: #E6E6E6;
    background-image: -moz-linear-gradient(center top , white, #E6E6E6);
    background-repeat: repeat-x;
    border-color: #F0F0F0 #F0F0F0 #E6E6E6;
    border-image: none;
    border-radius: 2px 2px 2px 2px;
    border-style: solid;
    border-width: 1px;
    color: #333333;
    cursor: pointer;
    display: inline-block;
    font-size: 14px;
    margin-bottom: 0;
    padding: 4px 12px;
    text-align: center;
    text-shadow: 0 1px 1px rgba(255, 255, 255, 0.75);
    vertical-align: middle;
}

.btn-primary {
    background-color: #3071A9;
    background-image: -moz-linear-gradient(center top , #428BCA, #3071A9);
    background-repeat: repeat-x;
    border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25);
    color: white;
    text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.25);
}

</style>

<!-- oryx editor -->
<!-- language files -->

<%
	if(locale.toString().equalsIgnoreCase("zh_CN")) {%>
		<script type="text/javascript" src="<c:url value='/oryx/i18n/translation_zh_cn.js'/>"></script>
	<%} else {%>
		<script type="text/javascript" src="<c:url value='/oryx/i18n/translation_en_us.js'/>"></script>
<% } %>
		
<script type="text/javascript" src="<c:url value='/oryx/profiles/oryx.core.uncompressed.js'/>"></script>
<script type="text/javascript" src="<c:url value='/oryx/profiles/bpmn2.0Uncompressed.js'/>"></script>

<link rel="Stylesheet" media="screen" href="/oryx/css/theme_norm.css" type="text/css" />

<link rel="stylesheet" type="text/css" href="<c:url value='/oryx/customtree/css/multi/tree.css'/>" />
<!-- erdf schemas -->

<!-- Common -->
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/easybpm/common/common.css'/>"/>

<!-- Form -->
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/easybpm/form/local.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/easybpm/form/form.css'/>" />


<script type='text/javascript'>
	if(!ORYX) var ORYX = {};
	if(!ORYX.CONFIG) ORYX.CONFIG = {};
	ORYX.CONFIG.PLUGINS_CONFIG =ORYX.CONFIG.PROFILE_PATH + 'bpmn2.0.xml';
	ORYX.CONFIG.SSET='stencilsets/bpmn2.0/bpmn2.0.json';
	ORYX.CONFIG.PANEL_HEIGHT = 650;
	ORYX.CONFIG.SSEXTS=[];
	
	/*if ('undefined' == typeof(window.onOryxResourcesLoaded)) { 
		ORYX.Log.warn('No adapter to repository specified, default used. You need a function window.onOryxResourcesLoaded that obtains model-JSON from your repository');
		window.onOryxResourcesLoaded = function() {
			if (location.hash.slice(1).length == 0 || location.hash.slice(1).indexOf('new')!=-1){
				var stencilset=ORYX.Utils.getParamFromUrl('stencilset')?ORYX.Utils.getParamFromUrl('stencilset'):'stencilsets/bpmn2.0/bpmn2.0.json';
				new ORYX.Editor({id: 'oryx-canvas123',stencilset: {url: '/oryx/'+	stencilset		}
				})
			} else {
				ORYX.Editor.createByUrl('/processCreate'
						+ location.hash.slice(1) + '/json', {
					id : 'oryx-canvas123'
				});
			}
			;
		}
	}*/
	
	function designView(){	
		document.getElementById("tabs-1").setAttribute("class", "btn btn-primary");
		document.getElementById("tabs-2").setAttribute("class", "btn hidden-tablet");
		document.getElementById('oryx-canvas123').style.display="block";
		document.getElementById('view-source').style.display="none";		
	}
	
	function sourceView(){
		document.getElementById("tabs-2").setAttribute("class", "btn btn-primary");
		document.getElementById("tabs-1").setAttribute("class", "btn hidden-tablet");
		document.getElementById('ext-gen202').click();
	}
	
	function toggleMenu(menuId,menuLength){
		for(var i=1; i<=menuLength; i++){
			if(menuId == i){
				document.getElementById(i+'_popup_menu').className = 'current';
				document.getElementById(i+'_menu_container').style.display = 'block';
			}else{
				document.getElementById(i+'_popup_menu').className = '';			
				document.getElementById(i+'_menu_container').style.display = 'none';	
			}
		}
	}
	
	function testFullScreenFun(){
		toggleFullscreenProcessEditor('top_btn');
	}
</script>

<div class="row-fluid">
	<div class="span12" id="target_iframe_div_hd">
		<div class="titleBG">
			<%
			if(processName != null && processName.length() >0) {%>
				<span>编辑流程 - <%=processName %> </span>
			<%} else {%>
				<span>创建流程</span>
			<% }
			%>
	
     		<span class="floatRight fontSize14 pad-L10 pad-T5">
	   			<div class="cursor_pointer">
	   				<img src="/images/arrow-left.png" onclick="window.top.backProcessList();">
	   			</div>
	   		</span>
			<span id="toggle-fullscreen-iframe" class="floatRight pad-T5 cursor_pointer" onClick="testFullScreenFun();">
			   	<img  src="/images/easybpm/common/min.png" />
			</span> 
			<div class="floatRight pad-T2 pad-R5" style="margin-top:-3px;">
				<button class="btn btn-primary" id="tabs-1" onclick="designView()" type="button">设计</button>
   				<button class="btn hidden-tablet" id="tabs-2" onclick="sourceView()" type="button">源码</button>
			</div>
		</div>
	</div>
</div>
<body style="overflow:hidden;">
	<div class="processdata" style="width:400px;height:500px;"></div>
	<div id="oryx-canvas123" name="oryx-canvas123"></div>
	<div id="view-source" name="view-source" style="display:none;border: 1px solid #E0E0E0;padding:10px;"></div>
	<!-- <div class="floatRight pad-T2 pad-R5">
	<button type="button" class="btn btn-primary normalButton padding0 line-height0 floatRight pad-T5" name="cancel" onclick="window.top.backProcessList();" id="cancelButton" style="cursor: pointer; background-color: #FF9900;">Cancel</button>
	</div> -->
</body>

