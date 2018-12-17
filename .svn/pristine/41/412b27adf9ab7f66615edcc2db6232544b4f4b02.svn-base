<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="widget.title"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>

<%@ include file="/common/messages.jsp" %>
<%= request.getAttribute("script") %>

<script type="text/javascript">

 $(document).ready(function(){
       $('#grid_header_links').html($('#header_links').html());
});

 function showCreateWidget(){
		document.location.href = "#bpm/admin/showCreateWidget";
		_execute('target');
 }

 function showEditWidget(id){
	var params = 'id='+id
	document.location.href = "#bpm/admin/showCreateWidget";
    _execute('target',params);	
 }
 
 

</script>

<div id="header_links" align="right" class="overflow">
	<a class="padding10" id="createWidget" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="javascript:showCreateWidget(0)"  data-icon=""><fmt:message key="button.createNew"/></a>
	<a class="padding10" id="deleteWidget" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteWidgets()"  data-icon=""><fmt:message key="button.delete"/></a> 
</div>