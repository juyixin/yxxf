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

</script>

<div id="header_links" align="right" style="display:none">
	<%-- <a id="enableUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="updateUserStatus('enable');"  data-icon=""><fmt:message key="button.enable"/></a>
	<a id="disableUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="updateUserStatus('disable');"  data-icon=""><fmt:message key="button.disable"/></a> --%>
	<a class="padding10" id="createUser" href="#bpm/admin/addNewDictionary" data-role="button" data-theme="b"  onClick="_execute('target','method=add');"  data-icon=""><fmt:message key="button.createNew"/></a>
	<a class="padding10" id="enableUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="updateUserStatus('disable');"  data-icon=""><fmt:message key="button.disable"/></a>
	<a class="padding10" id="deleteUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteUsers()"  data-icon=""><fmt:message key="button.delete"/></a>
</div>

