<%@ include file="/common/taglibs.jsp" %>

<script type="text/javascript">

function _showPersonIncharge(cellValue, options, rawObject) {
	
	$("#personInchargeFullName").val("");
	
	var JSONUsers = JSON.parse('${usersJSON}');
	setUserFullNames("personInchargeFullName",cellValue,JSONUsers);
	return $("#personInchargeFullName").val();
}

function _showSuperGroup(cellValue, options, rawObject) {
	
	$("#superGroupFullName").val("");
	
	var JSONGroups = JSON.parse('${groupsJSON}');
	setUserFullNames("superGroupFullName",cellValue,JSONGroups);
	return $("#superGroupFullName").val();
}

</script>

<%@ include file="/common/messages.jsp" %>
<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2><fmt:message key="user.manageGroups"/></h2>
	</div>

	<div id="header_links" align="right">
		<a class="padding3" id="createUser" href="#bpm/admin/groupForm" data-role="button" data-theme="b"  onClick="_execute('target','');"  data-icon=""><button class="btn"><fmt:message key="button.createNew"/></button></a>
		<a class="padding3" id="deleteUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteGroups()"  data-icon=""><button class="btn"><fmt:message key="button.delete"/></button></a>
	</div>
</div>
<div><%= request.getAttribute("script") %></div>

<input type="hidden" id="personInchargeFullName"/>
<input type="hidden" id="superGroupFullName"/>