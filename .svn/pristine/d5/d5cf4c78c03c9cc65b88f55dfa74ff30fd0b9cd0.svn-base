<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">

function _showStartUser(cellValue, options, rawObject) {
	
	$("#startUserFullName").val("");
	
	var JSONUsers = JSON.parse('${usersJSON}');
	setUserFullNames("startUserFullName",cellValue,JSONUsers);
	return $("#startUserFullName").val();
}

function goBack() {
	window.history.back(-1);
}

</script>

<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2>${gridTitle}</h2>
	</div>

	<div align="right"><a class="padding3" style="text-decoration: underline;" id="backToPreviousPage" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="goBack()" data-icon=""><button class="btn"><fmt:message key="button.back"/></button></a> </div>
</div>

<%= request.getAttribute("script") %>

<input type="hidden" id="startUserFullName"/>