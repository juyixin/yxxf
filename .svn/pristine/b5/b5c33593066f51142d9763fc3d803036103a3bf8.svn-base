<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<script type="text/javascript">

function _showEditRecord(cellValue, options, rawObject) {
	return '<a href="#bpm/manageProcess/editClassification" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+encodeURI(rawObject.classificationId)+ '\');"><b>' + rawObject.name + '</b></a>';
}

function _moveClassificationGridRowUpImage(cellValue, options, rawObject) {
	return '<img src="/images/go_up_small.png" onclick="moveGridRow(\'up\',\''
			+ options.rowId + '\',\'CLASSIFICATION_LIST\')"/>';
}

function _moveClassificationGridDownImage(cellValue, options, rawObject) {
	return '<img src="/images/move_down_small.png" onclick="moveGridRow(\'down\',\''
			+ options.rowId + '\',\'CLASSIFICATION_LIST\')"/>';
}

function deleteClassifications(){
	var rowid = gridObj.getGridParam('selarrrow');
	
	var ids;
	rowid.forEach(function(item) {
		var id = gridObj.jqGrid('getCell', item, 'classificationId');
		ids = ids+","+id;
	});
	
	if(rowid.length !=0 ){
		deleteAllClassificationsConfirm(ids);
	}
	
	if(rowid.length ==0){
		validateMessageBox(form.title.message, "请选择要删除的记录", false);
	}
}

function deleteAllClassificationsConfirm(ids){
	params = 'cids='+encodeURI(ids);
	$.msgBox({
	    title: form.title.confirm,
	    content: "确定要删除记录吗",
	    type: "confirm",
	    buttons: [{ value: "Yes" }, { value: "No" }],
	    success: function (result) {
	        if (result == "Yes") {
	        	document.location.href = "#bpm/manageProcess/deleteSelectedClassification";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}

</script>

<%@ include file="/common/messages.jsp" %>
<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2>流程分类</h2>
	</div>

	<div id="header_links" align="right">
		<a class="padding3" href="#bpm/manageProcess/classificationForm" data-role="button" data-theme="b"  onClick="_execute('target','');"  data-icon=""><button class="btn"><fmt:message key="button.createNew"/></button></a>
		<a class="padding3" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteClassifications()"  data-icon=""><button class="btn"><fmt:message key="button.delete"/></button></a>
	</div>
</div>
<div><%= request.getAttribute("script") %></div>
