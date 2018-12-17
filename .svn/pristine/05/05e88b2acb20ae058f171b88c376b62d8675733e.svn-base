<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="report.title"/></title>
    </head>
    <%@ include file="/common/messages.jsp" %>
<%= request.getAttribute("script") %>
<script type="text/javascript">

 $(document).ready(function(){
       $('#grid_header_links').html($('#header_links').html());
});

</script>
<div id="header_links" align="right" class="displayNone pad-T5">
	<a class="padding10" id="create" href="#bpm/report/createReportForm" data-role="button" data-theme="b"  onClick="_execute('target','');"  data-icon="info"><fmt:message key="button.createNew"/></a>
	<a class="padding10" id="delete" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteReport()"  data-icon="info"><fmt:message key="button.delete"/></a>
</div>
	
