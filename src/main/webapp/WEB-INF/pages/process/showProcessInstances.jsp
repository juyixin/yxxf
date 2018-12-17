<%@ include file="/common/taglibs.jsp"%>
<body>
<div>&nbsp;</div>
<div><span class="panel-title-style style3 style18">&nbsp;Process Instance</span></div>
<div>
<table class="gridview">
	<tr class="header">
		<th>Status</th>
		<th>Count</th>
	</tr>
	<tr>
			<td>Success</td>
			<td align="center">
			     <c:choose>
  					<c:when test="${Success>0}">
					   <a href="javascript:void(0);" onclick="showProcessInstanceDetail('${processDefinitionId}','success','${processGridType}')"><b>${Success}</b></a>
				    </c:when>
				    <c:otherwise>0</c:otherwise>
				</c:choose>
			</td>
	</tr>
	<tr>
			<td>InProgress</td>
			<td align="center">
				 <c:choose>
	  				<c:when test="${InProgress>0}">
					    <a href="javascript:void(0);" onclick="showProcessInstanceDetail('${processDefinitionId}','activated','${processGridType}')"><b>${InProgress}</b></a>
					</c:when>
				    <c:otherwise>0</c:otherwise>
				</c:choose>
			 </td>
	</tr>
	<tr>
			<td>Suspended</td>
			<td align="center">
				 <c:choose>
	  				<c:when test="${Suspended>0}">
					    <a href="javascript:void(0);" onclick="showProcessInstanceDetail('${processDefinitionId}','suspended','${processGridType}')"><b>${Suspended}</b></a>
					</c:when>
				    <c:otherwise>0</c:otherwise>
				</c:choose>
			 </td>
	</tr>
	<tr>
			<td>Failed</td>
			<td align="center">
				 <c:choose>
	  				 <c:when test="${Deleted>0}">
					 	<a href="javascript:void(0);" onclick="showProcessInstanceDetail('${processDefinitionId}','deleted','${processGridType}')"><b>${Deleted}</b></a>
					 </c:when>
				    <c:otherwise>0</c:otherwise>
				</c:choose>
			</td>
	</tr>	
	<tr>
			<td>Terminated</td>
			<td align="center">
				 <c:choose>
	  				 <c:when test="${Terminated>0}">
					 	<b>${Terminated}</b>
					 </c:when>
				    <c:otherwise>0</c:otherwise>
				</c:choose>
			</td>
	</tr>	
</table>
</div>
</body>