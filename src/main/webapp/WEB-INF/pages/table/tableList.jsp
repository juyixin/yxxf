<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/messages.jsp" %> 
<%= request.getAttribute("script") %>
<script type="text/javascript">

$(document).ready(function(){
       $('#grid_header_links').html($('#header_links').html());
});

</script>

<div id="header_links" align="right" class="displayNone">
	<a style="padding:10px;" id="createTable" href="#/bpm/table/createTable" data-role="button" data-theme="b"  onClick="_execute('target','enableRelationTab=false&isEdit=true');"  data-icon=""><fmt:message key="button.createNew"/></a>
</div>