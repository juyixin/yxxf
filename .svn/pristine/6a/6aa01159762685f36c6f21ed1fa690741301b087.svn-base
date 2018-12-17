<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<script type="text/javascript">
<%--
function _showEditCustom(cellValue, options, rawObject) {
	return '<a id="editgroup" href="#bpm/crm/editCustom" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+encodeURI(rawObject.id)+ '\');"><b>' + rawObject.num + '</b></a>';
}
--%>
<%--
function deleteCustoms(){
	var rowid = gridObj.getGridParam('selarrrow');
	
	var customId;
	rowid.forEach(function(item) {
		var id = gridObj.jqGrid('getCell', item, 'id');
		customId = customId+","+id;
		
	});
	
	if(rowid.length !=0 ){
		deleteAllCustomsConfirm(customId);
	}
	
	if(rowid.length ==0){
		validateMessageBox(form.title.message, "请选择要删除的记录", false);
	}
}

function deleteAllCustomsConfirm(customIds){
	params = 'customIds='+encodeURI(customIds);
	$.msgBox({
	    title: form.title.confirm,
	    content: "确定要删除记录吗",
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	document.location.href = "#bpm/crm/deleteSelectedCustom";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}
--%>
function query(){
	var queryNum = $("#queryNum").val();
	var queryName = $("#queryName").val();
	_execute('target','num='+queryNum+'&'+'name='+queryName);

}
</script>
<%@ include file="/common/messages.jsp" %>
<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2>客户信息管理</h2>
	</div>


<div id="header_links" align="right">
    <input type="text" id="queryNum" name="queryNum" value="${queryNum}" placeholder="请输入客户编号"/>
	<input type="text" id="queryName" name="queryName" value="${queryName}" placeholder="请输入客户名称"/>
	<a class="padding3" href="#bpm/crm/customs" onClick="query();"><button class="btn"><fmt:message key="button.search"/></button></a>
	
	 <%--
	<a class="padding3" id="createUser" href="#bpm/crm/customForm" data-role="button" data-theme="b"  onClick="_execute('target','');"  data-icon=""><button class="btn"><fmt:message key="button.createNew"/></button></a>
	<a class="padding3" id="deleteUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteCustoms()"  data-icon=""><button class="btn"><fmt:message key="button.delete"/></button></a>
	--%>
</div>
</div>
<div><%= request.getAttribute("script") %></div>