<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<script type="text/javascript">
 
function _showEditAllx(cellValue, options, rawObject) {
	return '<a id="editgroup" href="#bpm/alxxgl/editAllx" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+encodeURI(rawObject.id)+ '\');"><b>' + rawObject.lxdm + '</b></a>';
}

function deleteRecords(){
	var rowid = gridObj.getGridParam('selarrrow');
	
	var allxId;
	rowid.forEach(function(item) {
		var id = gridObj.jqGrid('getCell', item, 'id');
		allxId = allxId+","+id;
		
	});
	
	if(rowid.length !=0 ){
		deleteAllRecordsConfirm(allxId);
	}
	
	if(rowid.length ==0){
		validateMessageBox(form.title.message, "请选择要删除的记录", false);
	}
}

function deleteAllRecordsConfirm(allxIds){
	params = 'allxIds='+encodeURI(allxIds);
	$.msgBox({
	    title: form.title.confirm,
	    content: "确定要删除记录吗",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	    success: function (result) {
	        if (result == "是") {
	        	document.location.href = "#bpm/alxxgl/deleteSelectedAllx";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}

function query(){
	var queryNum = $("#queryNum").val();
	_execute('target','lxmc='+queryNum);
}

</script>
<%@ include file="/common/messages.jsp" %>
<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2>案例类别维护</h2>
	</div>

<div id="header_links" align="right">
	<input type="text" id="queryNum" name="queryNum" value="${queryNum}" placeholder="请输入类型名称"/>
	<a class="padding3" href="#bpm/alxxgl/allbwh" onClick="query();"><button class="btn"><fmt:message key="button.search"/></button></a>
	<a class="padding3" id="createUser" href="#bpm/alxxgl/allxForm" data-role="button" data-theme="b"  onClick="_execute('target','');"  data-icon=""><button class="btn"><fmt:message key="button.createNew"/></button></a>
	<a class="padding3" id="deleteUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteRecords()"  data-icon=""><button class="btn"><fmt:message key="button.delete"/></button></a>
</div>
</div>
<div><%= request.getAttribute("script") %></div>

