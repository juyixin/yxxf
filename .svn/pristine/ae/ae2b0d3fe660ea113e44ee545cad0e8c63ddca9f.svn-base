<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="sysconfig.title"/></title>
</head>
<%@ include file="/common/messages.jsp" %>
<%= request.getAttribute("script") %>

<script type="text/javascript">

 $(document).ready(function(){
       $('#grid_header_links').html($('#header_links').html());
});

</script>
<div id="header_links" align="right" class="displayNone">
	<a class="padding3" id="create" href="#bpm/admin/sysConfigForm" data-role="button" data-theme="b"  onClick="_execute('target','');"  data-icon=""><button class="btn"><fmt:message key="button.createNew"/></button></a>
	<a class="padding3" id="delete" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteSysConfig()"  data-icon=""><button class="btn"><fmt:message key="button.delete"/></button></a>
</div>
