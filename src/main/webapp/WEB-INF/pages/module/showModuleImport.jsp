<%@ include file="/common/taglibs.jsp"%>
<%--  <form:form method="post" action="bpm/module/moduelCSVImport" cssClass="form-horizontal" enctype="multipart/form-data" onSubmit="closeSelectDialog('popupDialog');" > --%>
<form:form method="post" action="bpm/module/moduleMigrationImport" cssClass="form-horizontal" enctype="multipart/form-data" onSubmit="setTimeout(function() {closeSelectDialog('popupDialog');}, 1);" >
	
			<div class="control-group">
				<eazytec:label styleClass="control-label" key="table.location" />
				<div class="controls">
					<input type="file" name="file" id="file" class="medium bpm_import_file" />
				</div>
			</div>
			<div class="control-group">
					<div class="form-actions no-margin">
						<button type="submit" class="btn btn-primary" name="save"						
							onclick="return validateSQLFileUpload(this.form);"
							id="fileSubmit">
							<fmt:message key="table.button.import" />	
						</button>
						<button type="button" class="btn btn-primary " name="next"
							onclick="closeSelectDialog('popupDialog');"
							id="cancelButton" style="cursor: pointer;">
							<fmt:message key="button.cancel" />
						</button>
						<div class="clearfix"></div>
					</div>
				</div>
	
	<!-- <table>			
		<tr>
			<td class="fontSize14">Choose Location&nbsp;&nbsp;</td>	
								
			<td ><div class="pad-T20">	 <input type="file" name="file" id="file"  size="23"/></div></td>
			
		</tr>
	
		<tr>							
			<td ></td><td>
			<div class="pad-T20 pad-R25">
			
				<input type="submit" value="Submit" id="fileSubmit" class="btn btn-small" onclick="return validateXlsFileUpload(this.form);" />
				<input type="submit" value="Submit" id="fileSubmit" class="btn btn-small" onclick="return validateSQLFileUpload(this.form);" />
				<input type="button" value="Cancel" class="btn btn-small clearButton mar-L10" onclick="closeSelectDialog('popupDialog');" />
			</div>
			</td>
		</tr>						
	</table> -->
</form:form>