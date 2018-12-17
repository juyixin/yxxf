<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="timingTask.heading"/></title>
    </head>
<%@ include file="/common/messages.jsp" %>
<%= request.getAttribute("script") %>


<script type="text/javascript">

 $(document).ready(function(){
       $('#grid_header_links').html($('#header_links').html());
});

</script>

<div id="header_links" align="right" class="displayNone">
<a class="padding10" id="create" href="#bpm/admin/timingTask" data-role="button" data-theme="b"  onClick="_execute('target','');"  data-icon=""><fmt:message key="button.createNew"/></a>
<a class="padding10" id="delete" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteTriggers()"  data-icon=""><fmt:message key="button.delete"/></a>
</div>