<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(window).resize(function() {
    $("#processPreviewPopupDialog").dialog("option", "position", "center");
});

$(document).ready(function(){
    $("#processPreviewPopupDialog").dialog("option", "position", "center");
    var svgString = document.getElementById("svgString").value;
    var svgWidth = "";
    $("#svgDiv").find("svg").each(function(){
    	svgWidth = $(this).attr('width');
    	$("#svgDiv").css({"width":svgWidth});
    });
    $('#processPreviewPopupDialog').dialog('option', 'width', parseInt(svgWidth)+26); 
    $("#processPreviewPopupDialog").dialog("option", "position", "center");
    $("#processPreviewPopupDialog").dialog("option", "maxWidth", 800);
    $(".ui-dialog").css({"max-width":"800px","max-height":"500px"});
    $("#processPreviewPopupDialog").css({"max-width":"800px","overflow":"auto","max-height":"450px"});
    
});

</script>
<div id="svgDiv">
	<%=request.getAttribute("svgString")%>
</div>
<div id="jump-full">
	<select id="activityId" name="activityId">
		<c:forEach items="${processDefinitionList}" var="processDef">
			<option value="${processDef.taskId}/${processDef.nodeType}/${processDef.organizer}">${processDef.taskName}</option>
		</c:forEach>
	</select>
	&nbsp;&nbsp;
	<% String jumpType = (String)request.getAttribute("jumpType"); %>
	<% String svgString = (String)request.getAttribute("svgString"); %>
	<% if(jumpType.equals("jump")) {%>
	<input type="button" onclick="jumpToTask(${canSave},'${formId}');" value="跳转" style="vertical-align:top;" class ="btn btn-primary">
	<%}else if(jumpType.equals("backOff")){ %>
	<input type="button" onclick="jumpToTask(${canSave},'${formId}');" value="重新提交" style="vertical-align:top;" class ="btn btn-primary">
	<%}else { %>
		<input type="button" onclick="jumpToTask(${canSave},'${formId}');" value="退回" style="vertical-align:top;" class ="btn btn-primary">
	<% } %>
	
	<input type="hidden" id="taskIdFromJump" name="taskIdFromJump" value="${taskId}">
	<input type="hidden" id="resourceName" name="resourceName" value="${resourceName}">
	<input type="hidden" id="jumpType" name="jumpType" value="${jumpType}">
	<input type="hidden" id=svgString name="svgString" value="${svgString}">
	<input type="hidden" id=processDefinitionId name="processDefinitionId" value="${processDefinitionId}">
	
</div>

<script type="text/javascript">
jumpingPorcessDef();
function jumpingPorcessDef(){
	var jumpDefSize = '${processDefinitionListSize}';
	var jumpType = '${jumpType}';
	if(jumpDefSize <= 0){
		if(jumpType == "jump"){
			message = "<span style=' color: red;font-size: 16px;font-weight: bold;'>There is no node to Jump</span>";
		}else{
			message = "<span style=' color: red;font-size: 16px;font-weight: bold;'>There is no node to Return</span>";
		}
		document.getElementById("jump-full").innerHTML = message;
	}
}

</script>