<%@ include file="/common/taglibs.jsp"%>
<script>
$(document).ready(function(){
	setModuleNamesList("","?isPrivilegeNeeded=true");
});
</script>

<head>
</head>
<body>
	<div class="row-fluid">
		<form:form id="sqlDumpForm" cssClass="form-horizontal" style="padding-top: 20px;" method="post" action="#" enctype="multipart/form-data" onSubmit="setTimeout(function() {closeSelectDialog('importSQLPopupDialog');},2);_execute('target', '');" >
			<div class="control-group">
				<eazytec:label styleClass="control-label" key="table.modulename" />
				<div class="controls">
					<input type="text" autocomplete="off" list="searchModuleresults" name="_moduleName" id="importModuleName" class="medium" onblur="setImportModuleDetails('module_Id','false')"/>
					<datalist id="searchModuleresults"></datalist>
				</div>
			</div>
			<div class="control-group">
				<eazytec:label styleClass="control-label" key="table.location" />
				<div class="controls">
					<input type="file" name="file" id="sqlDumpfile" class="medium bpm_import_file"  multiple="multiple" />
				</div>
			</div>
			<div class="control-group">
					<div class="form-actions no-margin">
						<button type="submit" class="btn btn-primary" name="save"						
							onclick="return checkDumpFilePath('#sqlDumpfile','SQL','bpm/table/importSQL','sqlDumpForm');"
							id="fileSubmit">
							<fmt:message key="table.button.import" />	
						</button>
						<button type="button" class="btn btn-primary " name="next"
							onclick="closeSelectDialog('importSQLPopupDialog');"
							id="cancelButton" style="cursor: pointer;">
							<fmt:message key="button.cancel" />
						</button>
						<div class="clearfix"></div>
					</div>
				</div>
	
	<input type="hidden" id="importModuleId" name="module_Id"/>		
	
	<%-- <table width="100%">	
		<tr>
			<td class="fontSize14" align="right">Choose Location &nbsp;&nbsp;</td>	
			<td><div style="width:320px;" class="pad-T20"><input type="file" name="file" id="sqlDumpfile" multiple="multiple" class="bpm_import_file" size="23"/></div></td>
		</tr>
		<tr>
			<td class="fontSize14" align="right">Module Name &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>	
				<td>
					<div class="pad-T20">
						<fieldset class="control-group">
				            <input type="text" autocomplete="off" list="searchModuleresults" id="importModuleName" class="normal" name="_moduleName" onblur="setImportModuleDetails('module_Id','false')">
			                <datalist id="searchModuleresults"/>
				        </fieldset>
			        </div>
				</td>
		</tr>
		<tr>							
			<td></td>
			<td>
			<div align="left" class="pad-T20">
				<input type="submit" value="<fmt:message key="table.button.import"/>" id="fileSubmit" class="btn btn-primary clearButton mar-L10 pad-B3 height25" onclick="return checkDumpFilePath('#sqlDumpfile','SQL','bpm/table/importSQL','sqlDumpForm');" />
				<input type="button" value="Cancel" class="btn btn-primary clearButton mar-L10 pad-B3 height25" onclick="closeSelectDialog('importSQLPopupDialog');" />
			</div>
			</td>
		</tr>
		<input type="hidden" id="importModuleId" name="module_Id"/>								
	</table> --%>
		</form:form>
	</div>
</body>
