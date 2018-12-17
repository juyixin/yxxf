<%@ include file="/common/taglibs.jsp" %>

<%@ include file="/common/messages.jsp" %>
<%= request.getAttribute("script") %>

<div class="container-fluid form-background">
	<div class="row-fluid">
		<div class="span6">
			<label class="style3 style18"><fmt:message key="organizationTree.available"/></label>
		</div>
		<div class="span3">
			<label class="style3 style18"><fmt:message key="organizationTree.selected"/></label>
		</div>
		<%-- <div class="span3">
			<label class="style3 style18 fontBold"><fmt:message key="userSelection.selectionType"/> : ${fn:toUpperCase(fn:substring(selectionType, 0, 1))}${fn:toLowerCase(fn:substring(selectionType, 1,fn:length(selectionType)))} </label>
		</div> --%>
	</div>
	<div class="row-fluid organization-tree">
		<div class="span6 organization-tree-left" id="organizationTreeLeft"></div>
		<div class="span6 organization-tree-right pad-L20" id="organizationTreeRight"></div>
	</div>
	<div class="row-fluid pad-T6">
		<div class="control-group" align="center">
			<div class="form-actions no-margin">	
				<span id="saveButtonDiv" align="center">
					<button type="button" class="btn btn-primary normalButton padding0 line-height0" name="save" id="saveButton" onClick="addDataDictionarySelectedOption('${appendTo}','${callAfter}','${hiddenElementId}')";>
	                 <fmt:message key="button.select"/>
	    		</button>
				</span>
				<span id="cancelButtonDiv" class="">
					<button type="button" id="cancelEvent" class="btn btn-primary normalButton padding0 line-height0" name="cancel" onClick="closeSelectDialog('userPopupDialog')";>
                  	<fmt:message key="button.cancel"/>
	    		</button>
				</span>
				<div class="clearfix"></div>
			</div>
		</div>	
	</div>
</div>    
