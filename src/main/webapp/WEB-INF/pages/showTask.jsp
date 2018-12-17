<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/messages.jsp" %>

<%-- <page:applyDecorator name="default"> --%>

<head>
    <title><fmt:message key="userList.title"/></title>
    <meta name="heading" content="<fmt:message key='user.manageUsers'/>"/>
</head>
<script type="text/javascript">

$(function () {

	 var targetWidth = $('#target').width();
	 var targetHeight = $('#target').height();
	 var _width = parseInt(targetWidth) / 5;
	 $('#tree_structure').height(parseInt(targetHeight)-126);
	 $('#tree_structure').width("100%");
	 $('#data-details').width('100%');
	 $('#user-grid').width('100%');
	 $('#user-grid').height(parseInt(targetHeight)-126);
	 var jsonData ='${jsonTree}' ;
	 constructJsTree("tree_structure",jsonData,"getProcessGrid",false);
	 //setTimeout(function(){refreshGridData();},1000);
});
//showSideMenu('process','ff8081813c864821013c865542ab0001');
function refreshParentWindow(interval,type){
	if(interval == 0) {
		var ajaxLoaderFrame = new ajaxLoader(document.getElementById('ajax_loader'));
		var gridType = window.location.hash.split('/')[2];
		if(type == gridType) {
			refreshGridData(interval,type);
		}
		ajaxLoaderFrame.remove();
	} else {
		refreshGridData(interval,type);
	}
}
function refreshGridData(intervalTime,gridType){
 	if(intervalTime >= 0 && gridType  != undefined ){
		$.ajax({
			url: 'bpm/manageTasks/getMyBucket',
	        type: 'GET',
	        cache: false,
	        dataType: 'json',
	        async: false,
	        data: 'type=' + gridType,
			success : function(response) {
				if(response){
					$("#_MY_BUCKET").jqGrid('clearGridData');
					$("#_MY_BUCKET").jqGrid('setGridParam', {page: 1, data: response}).trigger('reloadGrid');
					$("#_MY_BUCKET").setGridParam({sortname:'createTime', sortorder: 'desc'}).trigger('reloadGrid');
			    }
 				if(intervalTime > 0){
					setTimeout(function(){refreshGridData();}, parseInt(intervalTime));
				} 
			}
		});
	} 	

}
function printWorkflowInstanceGrid(obj){
	$(obj).printPreview('data-details');
}

function readAllTask() {
	var taskIds = [];
	var rowid =  gridObj.getGridParam('selarrrow'); 
	if(rowid.length ==0){
		validateMessageBox(form.title.message, "请选择要操作的记录", false);
	} else {
		rowid.forEach(function(item) {
			var taskId = gridObj.jqGrid('getCell', item, 'id');
			taskIds.push(taskId);
		});
	 	var params = 'taskId='+taskIds;
		document.location.href = "#bpm/manageTasks/bulkReaderDetails";
		_execute('target',params); 
	}
}

function _showOwnerFullName(cellValue, options, rawObject) {
	
	$("#ownerFullName").val("");
	
	var JSONUsers = JSON.parse('${usersJSON}');
	setUserFullNames("ownerFullName",cellValue,JSONUsers);
	return $("#ownerFullName").val();
}

</script>
<c:if test="${gridType != 'documentToBeDone' && gridType != 'toReadDocument' && gridType != 'readDocumentList' && gridType != 'doneDocumentList' && gridType != 'cancelDocumentList' }">
	<div class="row-fluid">
		<div class="span12">
			<div class="page-header">
				<div class="pull-left">
					<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2>${gridTitle}</h2>
				</div>
				<div id="header_links" align="right">
					<c:if test="${gridType == 'mybucket'}">
      						<c:if test="${not empty isSecretaryExist}">
							<c:if test="${isSecretaryExist == 'true'}">
								<a class="padding3" id="substitute" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="assignSubstitute('${substitute}')"  data-icon=""><button class="btn">秘书代办</button></a>
							</c:if>
						</c:if>	 								
					</c:if>
					<c:if test="${gridType == 'toRead'}">
						<a class="padding3" href="javascript:void(0);" onclick="readAllTask();"><button class="btn">批量查看</button></a>
					</c:if>					
					<c:if test="${gridType == 'myOwnedTasks'}">
						<!-- <input type="text" name="searchBox" id="searchBox" onkeyup="_triggerSimpleSearch(this);"/> -->
						<a class="padding3" href="javascript:void(0);" id="suspendTasks" onclick="operateBulkTasks('suspend')"><button class="btn"><fmt:message key="task.suspend"/></button></a>
						<a class="padding3" href="javascript:void(0);" id="restoreTasks" onclick="operateBulkTasks('restore')"><button class="btn"><fmt:message key="task.restore"/></button></a>
						<a class="padding3" href="javascript:void(0);" id="terminateTasks" onclick="operateBulkTasks('terminate')"><button class="btn"><fmt:message key="task.terminate"/></button></a>
						<a class="padding3" href="javascript:void(0);" id="backTasks" onclick="operateBulkTasks('back')"><button class="btn"><fmt:message key="task.back"/></button></a>
						<a class="padding3" href="javascript:void(0);" id="replaceTasks" onclick="operateBulkTasks('replace')"><button class="btn"><fmt:message key="task.replace"/></button></a>
						<a class="padding3" href="javascript:void(0);" id="deleteTasks" onclick="operateBulkTasks('delete')"><button class="btn"><fmt:message key="task.delete"/></button></a>
						<a class="padding3" href="javascript:void(0);" id="exportTasks" onclick="selectWorkFlowExportType()"><button class="btn"><fmt:message key="task.exportTasks"/></button></a>
						<a class="padding3" href="javascript:void(0);" id="print" onclick="printWorkflowInstanceGrid(this)"><button class="btn"><fmt:message key="task.print"/></button></a>						
					</c:if>
				</div>
			</div>
		</div>
	</div>
</c:if>
	 
<c:if test="${not empty isStartScriptExist}">
	<c:if test="${isStartScriptExist == 'true'}"> 
 		<%= request.getAttribute("startScript") %>
 	</c:if>
</c:if>
						
<div class="row-fluid">
   	<div class="span12">
       	<c:choose>
 				<c:when test="${gridType != 'documentToBeDone' && gridType != 'readDocumentList' && gridType != 'toReadDocument' && gridType != 'doneDocumentList' }">
              <div class="span3">
              		<div class="widget">
					<div class="widget-header">
						<div class="title">
							<span class="fs1" aria-hidden="true"></span> <fmt:message key="listView.classifications"/> 
						</div>
					</div>
					<div class="widget-body">
						<div id="tree_structure" class="floatLeft department-list-departments department-jstree"></div>
						<div class="clearfix"></div>
					</div>
				</div>
              </div>
            <div class="span9">
				<div class="widget">
					<div class="widget-header">
						<div class="title">
							<span class="fs1" aria-hidden="true"></span> ${gridTitle}
						</div>
					</div>
					<div class="widget-body">
						<div id="data-details" class="floatLeft department-list-users">
                           	<div id="user-grid" >
                               	<%= request.getAttribute("script") %>
								<div id="gridRecordDetails"></div>
                           	</div>
                       	</div>
						<div class="clearfix"></div>
					</div>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<%= request.getAttribute("script") %>
			<div id="gridRecordDetails"></div>
		</c:otherwise>
		</c:choose>
	</div>
</div>
<input type="hidden" id="ownerFullName"/>
<script type="text/javascript">
$(document).ready(function() {
	setTimeout(function() {
		$("#all a").removeClass("").addClass('jstree-clicked');	
		$("#_MY_BUCKET_PAGER_center table.ui-pg-table tr").append ('<td><div><select class="grid-refresh" onChange="refreshGridData(this.value);"><option value=""></option><option value="100000">1</option><option value="300000">3</option><option value="500000">5</option></select> Mins</div></td>');
	},300);
	$("#_MY_BUCKET").setGridParam({sortname:'createTime', sortorder: 'desc'}).trigger('reloadGrid');
});
</script>
