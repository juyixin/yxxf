<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<script type="text/javascript">

function _showEditChance(cellValue, options, rawObject) {
	return '<a id="editgroup" href="#bpm/crm/editChance" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+encodeURI(rawObject.id)+ '\');"><b>' + rawObject.customerName+ '</b></a>';
}

function deleteChances(){
	var rowid = gridObj.getGridParam('selarrrow');
	
	var chanceId;
	rowid.forEach(function(item) {
		var id = gridObj.jqGrid('getCell', item, 'id');
		chanceId = chanceId+","+id;
		
	});
	
	if(rowid.length !=0 ){
		deleteAllChancesConfirm(chanceId);
	}
	
	if(rowid.length ==0){
		validateMessageBox(form.title.message, "请选择要删除的记录", false);
	}
}

function deleteAllChancesConfirm(chanceIds){
	params = 'chanceIds='+encodeURI(chanceIds);
	$.msgBox({
	    title: form.title.confirm,
	    content: "确定要删除记录吗",
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	document.location.href = "#bpm/crm/deleteSelectedChance";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}
function query(){
	var querycustomerName = $("#querycustomerName").val();
	_execute('target','customerName='+querycustomerName);
}

</script>
<%@ include file="/common/messages.jsp" %>
<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2>销售管理</h2>
	</div>


<div id="header_links" align="right">
    <input type="text" id="querycustomerName" name="querycustomerName" value="${querycustomerName}" placeholder="请输入客户名称"/>
	<a class="padding3" href="#bpm/crm/chances" onClick="query();"><button class="btn"><fmt:message key="button.search"/></button></a>
	<a class="padding3" id="createUser" href="#bpm/crm/chanceForm" data-role="button" data-theme="b"  onClick="_execute('target','');"  data-icon=""><button class="btn"><fmt:message key="button.createNew"/></button></a>
	<a class="padding3" id="deleteUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteChances()"  data-icon=""><button class="btn"><fmt:message key="button.delete"/></button></a>
</div>
</div>
<div><%= request.getAttribute("script") %></div>
