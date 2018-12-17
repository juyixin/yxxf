<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/messages.jsp" %>
<script type="text/javascript">

 	window.onunload = refreshParent;
	function refreshParent() {
	   // window.opener.location.reload();
	   window.opener.refreshParentWindow(0,$('#gridType').val());
	}
</script>