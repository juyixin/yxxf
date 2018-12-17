<%@ include file="/common/taglibs.jsp" %>

<%@ include file="/common/messages.jsp" %>
<%= request.getAttribute("script") %>
<% String hiddenFrom=request.getParameter("from"); %>
<% String hiddenParentId=request.getParameter("parentId");  %>
<script type="text/javascript">
	
	</script>
<div class="container-fluid form-background">
	<div class="row-fluid">
	 	<div align="right"><input type="text" id="searchUserName" class="small" name="searchUserName" autocomplete="off"  onkeyup="getUserNameAndPosition(event,'${selectionType}','${appendTo}','');" onchange="selectUserAndPosition('${selectionType}','${appendTo}');" list="searchUserNameWithPosition"/>
	 		<datalist id="searchUserNameWithPosition">
	 	</div>
		<div class="span6">
			<label class="style3 style18"><fmt:message key="organizationTree.available"/></label>
		</div>
		<div class="span3">
			<label class="style3 style18"><fmt:message key="organizationTree.selected"/></label>
		</div>
		<div class="span3" align="right">
			<label class="style3 style18"><fmt:message key="organizationTree.order" /></label>
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
	
	<input type="hidden" name="from"  id="from" value ="<%=hiddenFrom%>"  />
	<input type="hidden" name="parentId" id="parentId" value ="<%=hiddenParentId%>"  />
	<div class="control-group" align="center">
			<div class="form-actions no-margin">
				<span id="saveButtonDiv" align="center">
					<button type="submit" name="save"
						onClick="addOrganizerSelectedOption('${appendTo}','${callAfter}','${selection}','${isPotentialOrganizer}');" class="btn btn-primary" id="saveButton" class="cursor_pointer">
						<fmt:message key="button.select" />
					</button>
				</span> <span id="cancelButtonDiv" class="">
					<button type="button" class="btn btn-primary" name="cancel"
						onClick="closeSelectDialog('userPopupDialog')" ; id="cancelButton"
						class="cursor_pointer">
						<fmt:message key="button.cancel" />
					</button>
				</span>
				<div class="clearfix"></div>
			</div>
		</div>
	
		<%-- <div class="span4">
	 	</div>
    	<div class="span3">
			<div class="button" id="saveButtonDiv">
				<button type="button" class="btn btn-primary normalButton padding0 line-height0" name="save" id="saveButton" onClick="addOrganizerSelectedOption('${appendTo}','${callAfter}','${selection}','${isPotentialOrganizer}')";>
	                 <fmt:message key="button.select"/>
	    		</button>
			</div>
		</div>
		<div class="span3">
			<div class="button" id="cancelButtonDiv">
				<button type="button" id="cancelEvent" class="btn btn-primary normalButton padding0 line-height0" name="cancel" onClick="closeSelectDialog('userPopupDialog')";>
                  	<fmt:message key="button.cancel"/>
	    		</button>
			</div>
	 	</div>
	 	<div class="span2">
	 	</div> --%>
	</div>
</div>    