<%@ include file="/common/taglibs.jsp"%>
<head>
</head>
<body>
<form id="deleteProcessInstant" name="deleteProcessInstant" action="bpm/process/deleteProcessInstance" method="post" onSubmit="_execute('target','');">
	<table>			
		<tr><td colspan="2" class="style3 style18 " >Are you sure you want to delete process instance ${name}?</td></tr><tr><td colspan="2" class="style3 style18 style26"> This will also delete all associated tasks and data.</td></tr>
		<tr></tr>
		<tr>
			<td class="style3 style18 style26" width="10px">Reason&nbsp;&nbsp;</td>							
			<td class="style3 style18 style26" ><input name="reason" type="text" id="reason"  /></td>
		</tr>
		<tr>							
			<td colspan="2">
			<div align="center">
				<input type="submit" value="Yes"  onclick="closeSelectDialog('popupDialog')"/>
				<input type="button" value="No" class="normalButton" onclick="closeSelectDialog('popupDialog');" />
			</div>
			</td>
		</tr>						
	</table>	
	<input name="processInsId" type="hidden" value="${processInsId}" />
</form>
</body>
