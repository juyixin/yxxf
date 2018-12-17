<%@ include file="/common/taglibs.jsp"%>
<body>
<div class="formHead" style="height:35px"> Process Definition </div>
<div style="height:50px">
<table>
	
	<tr>
		<c:if test="${suspensionState == 2}">
			<td class="formBody" align="left" ><input type="button" value="Activate" class="btn btn-primary" onclick="changeStatus()" /></td>
		</c:if>
		<c:if test="${suspensionState == 1}">
			<td class="formBody" align="left" ><input type="button" value="Suspend" class="btn btn-primary" onclick="changeStatus()" /></td>
		</c:if>
		<td class="formBody" align="left" ><input type="button" value="Delete" class="btn btn-primary" onclick="deleteProcess()" /></td>
		<td class="formBody" align="left" ><input type="button" value="Export" class="btn btn-primary" onclick="downloadProcess()" /></td>
		<td class="formBody" align="left" ><input type="button" value="View" class="btn btn-primary" onclick="viewProcess()" /></td>
<!-- 		<td class="formBody" align="left" ><input type="button" value="Browse" class="btn btn-primary" onclick="browseProcess()"/></td> -->
	
	</tr>
</table>
</div>
<div class="line-height20 width-per-100"><div class="outer-pad floatLeft style3 style18 width-per-10">Id</div><div class="outer-pad style19" >: ${id}</div></div>
<div class="line-height20 width-per-100"><div class="outer-pad floatLeft style3 style18 width-per-10">Name</div><div class="outer-pad style19 " >: ${name}</div></div>
<div class="line-height20 width-per-100"><div class="outer-pad floatLeft style3 style18 width-per-10">Display Name</div><div class="outer-pad style19 ">: ${displayName}</div></div>
<div class="line-height20 width-per-100"><div class="outer-pad floatLeft style3 style18 width-per-10">Description</div><div class="outer-pad style19 ">: ${description}</div></div>
<div class="line-height20 width-per-100"><div class="outer-pad floatLeft style3 style18 width-per-10">Version</div><div class="outer-pad style19 ">: ${version}</div></div>
<div class="line-height20 width-per-100"><div class="outer-pad floatLeft style3 style18 width-per-10">No Of Instances</div><div class="outer-pad style19 ">: ${noOfInstacnes}</div></div>
<div class="line-height20 width-per-100"><div class="outer-pad floatLeft style3 style18 width-per-10">Process State </div><div class="outer-pad style19 ">: ${processState}</div></div>
<div class="line-height20 width-per-100"><div class="outer-pad floatLeft style3 style18 width-per-10">ResourceName</div><div class="outer-pad style19 " >: ${resourceName}</div></div>
<div class="line-height20 width-per-100"><div class="outer-pad floatLeft style3 style18 width-per-10">Key</div><div class="outer-pad style19 ">: ${key}</div></div>
<div class="line-height20 width-per-100"><div class="outer-pad floatLeft style3 style18 width-per-10">SuspensionState</div><div class="outer-pad style19 ">:
<c:if test="${suspensionState == 1}">
				Activated
			</c:if>
			<c:if test="${suspensionState == 2}">
				Suspended
			</c:if></div></div>
			<input type="hidden" id="suspensionState" value="${suspensionState}" />
			<input type="hidden" id="deploymentId" value="${deploymentId}" />
			<input type="hidden" id="id" value="${id}" />
			<input type="hidden" id="processState" value="${processState}" />
			<input type="hidden" id="resourceName" value="${resourceName}" />
			<input type="hidden" id="key" value="${key}" />

