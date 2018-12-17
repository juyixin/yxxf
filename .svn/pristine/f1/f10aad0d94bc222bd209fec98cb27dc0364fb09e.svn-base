<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/messages.jsp" %>

<c:if test="${success == true}">
	<%= request.getAttribute("script") %>
	<script type="text/javascript">
	 $(document).ready(function(){
		 var currentNodeId = '${listView.id}'; 
		 if(currentNodeId != null) {
			 $('#header_links').show();
		 }
		  var json_data = eval('${listViewGroup}');
		  var childNodes = json_data;
			if( childNodes != null && childNodes.length > 0 ){
				var selected_nodes = $("#module_relation_tree").jstree("get_selected", null, true); 
				var tree = $.jstree._reference($("#module_relation_tree")); 
		    	var children = tree._get_children(selected_nodes); 
		    	if(children.length == 0 ){
				for(var i=0; i<childNodes.length; i++) {
					$("#module_relation_tree").jstree("create", $("#"+currentNodeId+"_${moduleId}") , false, childNodes[i] , false, true);
				}
		    }
			}
	});
	</script>
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

