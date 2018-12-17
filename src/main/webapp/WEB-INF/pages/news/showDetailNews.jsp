<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/messages.jsp" %>

<%=request.getAttribute("script")%>
<script>
checkFormSubmit();
loadCKEditor(function() {
	  setTimeout(function(){	
			for(key in richTextBoxMap){
				$("#"+key).attr("style",richTextBoxMap[key]);
			}
		},1000);
	});
loadListViewsOnForm();
</script>
