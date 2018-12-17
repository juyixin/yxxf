<%-- <%@ include file="/common/taglibs.jsp"%>
<head>
</head>
<body>
 <form:form id="multiTableSqlDump" method="post" action="#" onSubmit="setTimeout(function() {closeSelectDialog('exportMultiTablePopupDialog');},2);_execute('target', '');" >
	<table width="100%">	
		<tr>
			<td class="fontSize14" align="right">Choose Tables &nbsp;&nbsp;</td>	
			<td><div class="pad-T20"><textarea id="multiTableName" name="multiTableName" rows="3" cols="45" style="width:86%" onclick="showRunTimeTables();"></textarea></div></td>
		</tr>
		<tr>							
			<td></td>
			<td>
			<div align="left" class="pad-T20">
				<input type="button" value="Export" class="btn btn-primary" onclick="return checkMultiTableDump('#multiTableName','','multiTableSqlDump');" />
				<input type="button" value="Cancel" class="btn btn-primary" onclick="closeSelectDialog('exportMultiTablePopupDialog');" />
			</div>
			</td>
		</tr>
	</table>
</form:form>
</body> --%>
<%@ include file="/common/taglibs.jsp"%>
<head>
</head>
<body>
	<div class="row-fluid">
		<div class="span12">
			<form:form id="multiTableSqlDump" method="post" action="#" style="padding-top:20px"
				onSubmit="setTimeout(function() {closeSelectDialog('exportMultiTablePopupDialog');},2);_execute('target', '');"
				cssClass="form-horizontal">
				<div class="control-group">
					<eazytec:label styleClass="control-label" key="table.export" />
					<div class="controls">
						<textarea  name="multiTableName"
							id="multiTableName" class="span9"
							onclick="showRunTimeTables();" ></textarea>
					</div>
				</div>
				<div class="control-group">
					<div class="form-actions no-margin">
						<button type="button" class="btn btn-primary" name="save"						
							onclick="return checkMultiTableDump('#multiTableName','','multiTableSqlDump');"
							id="saveButton">
							<fmt:message key="Export" />	
						</button>
						<button type="button" class="btn btn-primary " name="next"
							onclick="closeSelectDialog('exportMultiTablePopupDialog');"
							id="cancelButton" style="cursor: pointer;">
							<fmt:message key="button.cancel" />
						</button>
						<div class="clearfix"></div>
					</div>
				</div>
			</form:form>
		</div>
	</div>
</body>
