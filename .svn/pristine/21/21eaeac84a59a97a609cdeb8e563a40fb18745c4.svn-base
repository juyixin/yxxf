<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/messages.jsp" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity" %>
<script type="text/javascript">

</script>
<label>
	共有 ${fn:length(plist)} 个流程，请选择流程开始办理。
</label>
<div class="row-fluid target-background">
<c:forEach items="${clist}" var="c">


<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2>${c.name}</h2>
	</div>
	<div style="clear:both"></div>
</div>

<div>
<c:forEach items="${plist}" var="p">
	<c:if test="${p.classificationId eq c.id}">
	<div style="padding:10px 30px;float:left;text-align:center;">
		<img title="${p.description}" src="/images/process.png" border="0"/>
		<p style="text-align:center;">
			${p.name}
		</p>
		<p style="text-align:center;">
			<%
			ProcessDefinitionEntity o = (ProcessDefinitionEntity)pageContext.getAttribute("p");
			String id = o.getId();
			boolean hasStartFormKey = o.getHasStartFormKey();
			String processName = URLEncoder.encode(o.getName());
			String resourceName = URLEncoder.encode(o.getResourceName());
			String deploymentId = o.getDeploymentId();
			%>
		
			<a href="javascript:void(0);" title="点击开始办理" class="btn" style="padding:0px 3px;font-size:12px;" onclick="startProcessWithOrganizer('<%=id %>',<%=hasStartFormKey %>,false,null,false);">
				新建工作
			</a>
			
			<a href="javascript:void(0);" title="点击查看流程图" class="btn" style="padding:0px 3px;font-size:12px;" onclick="_previewProcessPopup('processPreviewPopupDialog','<%=processName%>',
			'resourceName=<%=resourceName%>&processName=<%=processName%>&isTitleShow=true&deploymentId=<%=deploymentId%>');">
				流程图
			</a>
		</p>
	</div>
	</c:if>
</c:forEach>
<div style="clear:both">
</div>

</c:forEach>
</div>