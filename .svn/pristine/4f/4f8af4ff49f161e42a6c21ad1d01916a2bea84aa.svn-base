<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<script type="text/javascript">

function _showEditContract(cellValue, options, rawObject) {
	return '<a id="editgroup" href="#bpm/crm/editContract" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+encodeURI(rawObject.id)+ '\');"><b>' + rawObject.num + '</b></a>';
}


function deleteContracts(){
	var rowid = gridObj.getGridParam('selarrrow');
	
	var contractId;
	rowid.forEach(function(item) {
		var id = gridObj.jqGrid('getCell', item, 'id');
		contractId = contractId+","+id;
		
	});
	
	if(rowid.length !=0 ){
		deleteAllContractsConfirm(contractId);
	}
	
	if(rowid.length ==0){
		validateMessageBox(form.title.message, "请选择要删除的记录", false);
	}
}

function deleteAllContractsConfirm(contractIds){
	params = 'contractIds='+encodeURI(contractIds);
	$.msgBox({
	    title: form.title.confirm,
	    content: "确定要删除记录吗",
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	document.location.href = "#bpm/crm/deleteSelectedContract";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}

function query(){
	var queryTitle = $("#queryTitle").val();
	_execute('target','title='+queryTitle);
}


</script>
<%@ include file="/common/messages.jsp" %>
<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2>合同信息管理</h2>
	</div>


<div id="header_links" align="right">
    <input type="text" id="queryTitle" name="queryTitle" value="${queryTitle}" placeholder="请输入合同名称"/>
	<a class="padding3" href="#bpm/crm/contracts" onClick="query();"><button class="btn"><fmt:message key="button.search"/></button></a>
	<a class="padding3" id="createUser" href="#bpm/crm/contractForm" data-role="button" data-theme="b"  onClick="_execute('target','');"  data-icon=""><button class="btn"><fmt:message key="button.createNew"/></button></a>
	<a class="padding3" id="deleteUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteContracts()"  data-icon=""><button class="btn"><fmt:message key="button.delete"/></button></a>
	
</div>
</div>
<div><%= request.getAttribute("script") %></div>

