<!-- 
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
-->
<div id="operatingFunctionDiv" class="btn-group">
  <a class="dropdown-toggle padding-1" data-toggle="dropdown" href="#">
    <button class="btn btn-info">其他操作</button>
  </a>
 ${operatingFunctionForTask.print}
  <ul class="dropdown-menu" style="min-width:150px;">
	<c:if test="${operatingFunctionForTask.returnOperation==true}">
		<li><a href="#" onClick="jumpTaskNodes('${deploymentId}','${resourceName}','${currentTaskName}','${processDefinitionId}','${taskId}','${activityId}',
		'return','${processInstanceId}',${operatingFunctionForTask.save},'${formId}','${executionId}');">退回</a></li>
	</c:if>
	<c:if test="${operatingFunction.returnOperation==true && operatingFunctionForTask.returnOperation==false}">
		<li><a href="#" onClick="operatingFunctionPermission('Retrun')">退回</a></li>
	</c:if>
	
	<c:if test="${ operatingFunctionForTask.recall==true || recallOperatingFunctionForTask.recall == true}">
		<li><a href="#" onClick="showRecall('${processInstanceId}','${taskId}','${resourceName}','${processDefinitionId}');">拿回</a></li>
	</c:if>
	<c:if test="${operatingFunction.recall==true && operatingFunctionForTask.recall==false}">
		<li><a href="#" onClick="operatingFunctionPermission('Recall')">拿回</a></li>
	</c:if>
	
	<c:if test="${ operatingFunctionForTask.withdraw==true}">
		<li><a href="#" onClick="withdraw('${processInstanceId}','${taskId}','${resourceName}' , '${processDefinitionId}');">撤办</a></li>
	</c:if>
	<c:if test="${operatingFunction.withdraw==true && operatingFunctionForTask.withdraw==false}">
		<li><a href="#" onClick="operatingFunctionPermission('Withdraw')">撤办</a></li>
	</c:if>
	
	<c:if test="${ operatingFunctionForTask.transfer==true}">
				<li><a href="#" onClick="getUsersToInvolve('${taskId}','${transfer}', 'Transfer','','${resourceName}','${nodeType}','${processDefinitionId}','${processInsId}')">转办</a></li>
	</c:if>
	<c:if test="${operatingFunction.transfer==true && operatingFunctionForTask.transfer==false}">
		<li><a href="#" onClick="operatingFunctionPermission('Transfer')">转办</a></li>
	</c:if>
	
	<c:if test="${operatingFunctionForTask.urge==true }">
		<li><a href="#" onClick="urgePopUp('${currentTaskName}','${processDefinitionId}','${taskId}','${processInsId}');">催办</a></li>
	</c:if>
	<c:if test="${operatingFunction.urge==true && operatingFunctionForTask.urge==false }">
		<li><a href="#" onClick="operatingFunctionPermission('Urge')">催办</a></li>
	</c:if>
	
	<c:if test="${(operatingFunctionForTask.add==true)}">
		<li><a href="#" onClick="getUsersToInvolve('${taskId}','${add}', 'Add Members','','${resourceName}','${nodeType}','${processDefinitionId}','${processInsId}')">加签</a></li>
	</c:if>
	
	<c:if test="${(operatingFunction.add==true) && (operatingFunctionForTask.add==false)}">
		<li><a href="#" onClick="operatingFunctionPermission('Add')">加签</a></li>
	</c:if>
	
	<c:if test="${operatingFunctionForTask.confluentSignature==true}">
		<li><a href="#" onClick="getUsersToInvolve('${taskId}','${confluentSignature}', 'Select User for Collaborative Operation','','${resourceName}','${nodeType}','${processDefinitionId}','${processInsId}')">会签</a></li>
	</c:if>
	<c:if test="${operatingFunction.confluentSignature==true && operatingFunctionForTask.confluentSignature==false}">
		<li><a href="#" onClick="operatingFunctionPermission('Collaborative Operation')">会签</a></li>
	</c:if>
	
	<c:if test="${operatingFunctionForTask.circulatePerusal==true}">
		<li><a href="#" onClick="getUsersToInvolve('${taskId}','${circulatePerusal}', '请选择要传阅的用户','','${resourceName}','${nodeType}','${processDefinitionId}','${processInsId}')">传阅</a></li>
	</c:if>
	<c:if test="${operatingFunction.circulatePerusal==true && operatingFunctionForTask.circulatePerusal==false}">
		<li><a href="#" onClick="operatingFunctionPermission('Circulate Perusal')">传阅</a></li>
	</c:if>
	
	<c:if test="${operatingFunctionForTask.jump==true}">
		<li><a href="#" onClick="jumpTaskNodes('${deploymentId}','${resourceName}','${currentTaskName}','${processDefinitionId}','${taskId}','${activityId}',
		'jump','${processInstanceId}',${operatingFunctionForTask.save},'${formId}','${executionId}');">跳转</a></li>
	</c:if>
	<c:if test="${operatingFunction.jump==true && operatingFunctionForTask.jump==false}">
		<li><a href="#" onClick="operatingFunctionPermission('Jump')">跳转</a></li>
	</c:if>
	
	<c:if test="${operatingFunctionForTask.transactorReplacement==true}">
		<li><a href="#" onClick="getUsersToInvolve('${taskId}','${transactorReplacement}', '修改办理人','','${resourceName}','${nodeType}','${processDefinitionId}','${processInsId}')">修改办理人</a></li>
	</c:if>
	<c:if test="${operatingFunction.transactorReplacement==true && operatingFunctionForTask.transactorReplacement==false}">
		<li><a href="#" onClick="operatingFunctionPermission('Replace Transactor')">修改办理人</a></li>
	</c:if>
	
	<c:if test="${operatingFunctionForTask.terminate==true}">
		<li><a href="#" onClick="terminate('${executionId}','${taskId}','${resourceName}' , '${processDefinitionId}');">终止流程</a></li>
	</c:if>
	<c:if test="${operatingFunction.terminate==true && operatingFunctionForTask.terminate==false}">
		<li><a href="#" onClick="operatingFunctionPermission('Terminate')">终止流程</a></li>
	</c:if>
	
	<c:if test="${operatingFunctionForTask.suspend==true}">
		<li><a href="#" onClick="suspend('${processInstanceId}','${taskId}','${resourceName}','${suspendState}','${processDefinitionId}');">暂停流程</a></li>
	</c:if>
	<c:if test="${operatingFunction.suspend==true && operatingFunctionForTask.suspend==false}">
		<li><a href="#" onClick="operatingFunctionPermission('Suspend')">暂停流程</a></li>
	</c:if>
	
	<c:if test="${operatingFunctionForTask.submit==true }">
		<li><a href="#" onClick="taskReturn('${taskId}','${resourceName}','${processDefinitionId}',${operatingFunctionForTask.save},'${formId}','${processInsId}');">退回上一节点</a></li>
	</c:if>
	</ul>
</div>
