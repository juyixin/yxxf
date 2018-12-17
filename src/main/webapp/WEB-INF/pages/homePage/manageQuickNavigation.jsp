<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="roleList.title"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>

<%@ include file="/common/messages.jsp" %>
<%= request.getAttribute("script") %>

<script type="text/javascript">

 $(document).ready(function(){
       $('#grid_header_links').html($('#header_links').html());
});

 function showQuickNav(){
	 openSelectDialogForFixedPosition("popupDialog","","600","300","Create QuickNavigation");
		document.location.href = "#bpm/admin/showCreateQuickNav";
 }
 

 function showEditQuickNav(id){
	 openSelectDialogForFixedPosition("popupDialog","","600","300","Edit QuickNavigation");
	 var params = 'id='+id
		document.location.href = "#bpm/admin/showCreateQuickNav";
    	_execute('popupDialog',params);
    	
    	
 }
</script>

<div id="header_links" align="right" class="displayNone">
	<a class="padding10" id="createLayout" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="javascript:showQuickNav(0)"  data-icon=""><fmt:message key="button.createNew"/></a>
<%-- 	<a class="padding10" id="deleteLayout" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteRoles()"  data-icon=""><fmt:message key="button.delete"/></a> --%>
</div>
	