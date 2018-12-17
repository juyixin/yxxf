<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/messages.jsp"%>

<form:form id="classification" commandName="classification"
	method="post" action="bpm/process/saveClassification"
	autocomplete="off" modelAttribute="classification"
	cssClass="form-horizontal"
	onSubmit="setTimeout(function() {closeSelectDialog('popupDialog');},2);setTimeout(function() {closeSelectDialog('importPopupDialog');},2);isNeedImport('${isNeedImport}');">



	<div class="control-group">
		<label class="control-label">名称</label>
		<div class="controls">
			<input type="text" name="name" id="classificationName" class="medium" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">描述</label>
		<div class="controls">
			<input type="text" name="description" id="description" class="medium" />
		</div>
	</div>
	<div class="control-group">
		<div class="form-actions no-margin">
			<button type="submit" class="btn btn-primary" name="save"
				onclick="return validateAddClassificatin(this.form)" id="saveButton">
				提交
			</button>
			<button type="button" class="btn btn-primary " name="next"
				onclick="closeSelectDialog('popupDialog');" id="cancelButton"
				style="cursor: pointer;">
				<fmt:message key="button.cancel" />
			</button>
			<div class="clearfix"></div>
		</div>
	</div>
</form:form>

<!-- <table>			
		<tr>
			<td class="fontSize14"><label class="control-label" style="float: right;text-align: right;padding-top:20px;">Name&nbsp;&nbsp;&nbsp;&nbsp;</label></td>	
								
			<td ><div class="pad-T20">	<input name="name" type="text" id="classificationName" class="medium" /></div></td>
			
		</tr>
		<tr>
			<td class="fontSize14"><label class="control-label" style="float: right;text-align: right;padding-top:20px;">Description&nbsp;&nbsp;&nbsp;&nbsp;</label></td>	
											
			<td ><div class="pad-T20">	<input name="description" type="text" id="description"  /></div></td>
			
		</tr>
		<tr>							
			<td></td><td>
				<div class="control-group">
				<div class="pad-T20 form-actions no-margin">
					<input type="submit" value="Submit" class="btn btn-primary" onclick="return validateAddClassificatin(this.form)"/>
					<input type="button" value="Cancel" class="btn btn-primary" onclick="closeSelectDialog('popupDialog');" />
				</div>
				</div>
			</td>
		</tr>						
	</table>	 -->

