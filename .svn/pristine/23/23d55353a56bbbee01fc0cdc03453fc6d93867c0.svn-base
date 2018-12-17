<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/messages.jsp" %>

 <c:if test="${success == true}">
	<%= request.getAttribute("script") %> 
</c:if>
<c:if test="${success == false}">
<input type="hidden" id="error_msg" value="<%=request.getAttribute("script")%>" /> 

<script type="text/javascript">
$(function(){
	$.msgBox({
		title : form.title.error,
		content : $("#error_msg").val(),
		type : "error"
	});
});
</script>
</c:if>
<div id="print_preview" class="displayNone"></div>
