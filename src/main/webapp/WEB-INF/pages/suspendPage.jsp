<%@ include file="/common/taglibs.jsp"%>
<head>
</head>
<body>
<div class="formHead">
	<c:if test="${suspensionState== '1'}">  
				Suspend Process definition
	</c:if> 		
	<c:if test="${suspensionState == '2'}">
				Activate Process definition
	</c:if>	
	
</div>
<form id="suspendForm" name="suspendForm" action="/suspendOrActivateProcess" method="post">
	<table>			
		<tr>
		<td class="formBody" colspan="2">
			<c:if test="${suspensionState== '1'}">  
				When do you want to suspend this process definition?
			</c:if> 		
			 <c:if test="${suspensionState == '2'}">
				When do you want to activate this process definition?
			</c:if>		
			</td>		
		</tr>
		<tr>							
			<td class="formBody" colspan="2"><input type="radio" name="on" id="nowDate" onclick="nowDateClick()" id="now"/>&nbsp;Now</td>							
		</tr>
		<tr>							
			<td class="formBody"><input type="radio" name="on" id="onDate" onclick="onDateClick()"/>&nbsp;On&nbsp;&nbsp;&nbsp;</td>
			<td>
				<div id="datePicDiv" style="display:none"><input name="onDatePic" type="text" id="onDatePic" class="date-picker" /></div>
			</td>							
		</tr>
		<tr>							
			<td class="formBody" colspan="2"><input type="checkbox" name="isAllProcess" id="isAllProcess" />&nbsp;&nbsp;
				<c:if test="${suspensionState== '1'}">  
					Also Suspend all process instances for this process definition
				</c:if> 		
				<c:if test="${suspensionState == '2'}">
					Also Activate all process instances for this process definition
				</c:if>	
		</tr>
		<tr>							
			<td class="formBody" colspan="2"><input type="submit" value="Ok" onclick="suspendFoemValidation()" style="margin:10px;"/></td>
		</tr>						
	</table>	
	<input name="processId" type="hidden" value="${processId}" />	
	<input name="suspensionState" type="hidden" value="${suspensionState}" />			
</form>
</body>
