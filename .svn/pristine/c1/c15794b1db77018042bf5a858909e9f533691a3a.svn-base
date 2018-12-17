<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<%@ include file="/common/messages.jsp" %>
<%= request.getAttribute("script") %>
<% String hiddenFrom=request.getParameter("from"); %>
<% String hiddenParentId=request.getParameter("parentId");  %>
<script type="text/javascript">
	
</script>
<div class="container-fluid form-background">
	<input type="hidden" id="searchValue" class="small" name="searchValue"/>		 	
	<div class="row-fluid">
		 <c:if test="${selection=='user'}">
		 	<div align="right"><input type="text" id="searchUserName" class="small" name="searchUserName" autocomplete="off" onkeyup="getUserNameAndPosition(event,'${selectionType}','${appendTo}','${currentUser}');" onclick="getUserNameAndPosition(event,'${selectionType}','${appendTo}','${currentUser}');" onchange="selectUserAndPosition('${selectionType}','${appendTo}');" placeholder="搜索用户" list="searchUserNameWithPosition"/>
		 		<datalist id="searchUserNameWithPosition">
		 	</div>
		 </c:if>
		 <c:if test="${selection=='selectedListView'}">
		 	<div align="right"><input type="text" id="searchListViewName" class="small" name="searchListViewName" autocomplete="off" onkeyup="getListViewName(event,'${selectionType}','${appendTo}','${currentUser}');" onclick="getListViewName(event,'${selectionType}','${appendTo}','${currentUser}');"  onchange="selectListViewName('${selectionType}','${appendTo}');" placeholder="搜索视图" list="searchListViewNameList"/>
		 		<datalist id="searchListViewNameList">
		 	</div>
		 </c:if>
		<c:if test="${selection=='process'}">
		 	<div align="right"><input type="text" id="searchProcessName" class="small" name="searchProcessName" autocomplete="off" onkeyup="getProcessName(event,'${selectionType}','${appendTo}','${currentUser}');" onclick="getProcessName(event,'${selectionType}','${appendTo}','${currentUser}');"  onchange="selectProcessName('${selectionType}','${appendTo}');" placeholder="搜索流程" list="searchProcessNameList"/>
		 		<datalist id="searchProcessNameList">
		 	</div>
		 </c:if>
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

		<input type="hidden" name="from" id="from" value="<%=hiddenFrom%>" />
		<input type="hidden" name="parentId" id="parentId" value="<%=hiddenParentId%>" />

		<div class="control-group">
			<div class="form-actions no-margin" align="center">
				<span id="saveButtonDiv">
					<button type="submit" name="save"
						onClick="addSelectedOption('${appendTo}','${callAfter}','${selection}');"
						class="btn btn-primary" id="saveButton" class="cursor_pointer">
						<c:choose>
							<c:when test="${appendTo == 'noOfWidget'}">
								<fmt:message key="button.update" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.select" />
							</c:otherwise>
						</c:choose>
					</button>
				</span>
				<span id="cancelButtonDiv" class="">
					<button type="button" class="btn btn-primary" name="cancel"
						onClick="closeSelectDialog('userPopupDialog');" id="cancelButton"
						class="cursor_pointer">
						<fmt:message key="button.cancel" />
					</button>
				</span>
				<div class="clearfix"></div>
			</div>
		</div>
	
