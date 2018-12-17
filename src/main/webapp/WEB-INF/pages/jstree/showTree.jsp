<%@ include file="/common/taglibs.jsp" %>

<%@ include file="/common/messages.jsp" %>
<%= request.getAttribute("script") %>

<div class="container-fluid form-background">
	<div class="row-fluid">
		<div align="right"><input type="text" id="searchTableName" class="small" name="searchTableName" autocomplete="off" onkeyup="getTableName(event,'${selectionType}','${appendTo}','${currentUser}');" onclick="getTableName(event,'${selectionType}','${appendTo}','${currentUser}');"  onchange="selectTableName('${selectionType}','${appendTo}');" placeholder="Search Table" list="searchTableNameList"/>
		 		<datalist id="searchTableNameList">
		 	</div>
		<div class="span6">
			<label class="style3 style18"><fmt:message key="tree.available"/></label>
		</div>
		<div class="span3">
			<label class="style3 style18"><fmt:message key="tree.selected"/></label>
		</div>
		<%-- <div class="span3">
			<label class="style3 style18 fontBold"><fmt:message key="userSelection.selectionType"/> : ${fn:toUpperCase(fn:substring(selectionType, 0, 1))}${fn:toLowerCase(fn:substring(selectionType, 1,fn:length(selectionType)))} </label>
		</div> --%>
	</div>
	<div class="row-fluid organization-tree">
		<div class="span6 organization-tree-left" id="treeLeft"></div>
		<div class="span6 organization-tree-right pad-L20" id="treeRight" style="width:49%;"></div>
	</div>
	<div class="row-fluid pad-T6">
		<div class="control-group" align="center">
			<div class="form-actions no-margin">
				<span id="saveButtonDiv" align="center">
					<button type="submit" name="save" class="btn btn-primary"
						id="saveButton" class="cursor_pointer"
						onClick="addSelectedOptionInField('${appendTo}','${callAfter}');">
						<fmt:message key="button.save" />
					</button>
				</span> <span id="cancelButtonDiv" class="">
					<button type="button" class="btn btn-primary" name="cancel"
						onClick="closeSelectDialog('popupDialog');" id="cancelButton"
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
				<button type="button" class="btn btn-primary normalButton padding0 line-height0" name="save" id="saveButton" onClick="addSelectedOptionInField('${appendTo}','${callAfter}')";>
	                 <fmt:message key="button.select"/>
	    		</button>
			</div>
		</div>
		<div class="span3">
			<div class="button" id="cancelButtonDiv">
				<button type="button" id="cancelEvent" class="btn btn-primary normalButton padding0 line-height0" name="cancel" onClick="closeSelectDialog('popupDialog')";>
                  	<fmt:message key="button.cancel"/>
	    		</button>
			</div>
	 	</div>
	 	<div class="span2">
	 	</div> --%>
	</div>
</div>    
