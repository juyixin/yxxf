<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>

<script type="text/javascript">
	function sortListViewResult(index,sortorder){
		$("#_SBOX_LIST").jqGrid('clearGridData');
		$("#_SBOX_LIST").jqGrid('setGridParam',{postData: {'ordername':index,'ordertype':sortorder}}).trigger('reloadGrid');
	}

	function deleteInbox(){
 		var datarow=$('#_SBOX_LIST').jqGrid('getGridParam','selarrrow');
 		if(datarow.length==0){
 			var errorMessage = "请至少选择一条记录！";
			validateMessageBox(form.title.message, errorMessage , false);
 		}
 		var ids="";
 		if(datarow.length!=0){
 			for(var i=0;i<datarow.length;i++){
 				ids=ids+","+datarow[i];
 			}
 			var params="ids="+ids
 			$.msgBox({
	    		title: form.title.confirm,
	    		content: "确定要删除这些数据？",
	    		type: "confirm",
	    		buttons: [{ value: "Yes" }, { value: "No" }],
	    		success: function (result) {
	        		if (result == "Yes") {
	        			document.location.href = "#bpm/message/deleteSelectedInBox";
	    				_execute('target',params);
	        		}else{
	  		  			return false;
	  	  			}
	    		}
			});
 		}
 	}
 	
 	function _showEditInBox(rowId, tv, rawObject, cm, rdata) {
		return '<a id="editSendM" href="#bpm/message/showSendMRead?type=Inbox" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+ rawObject[0] + '&from=list\');"><b>' + rowId + '</b></a>';
	}
 	
 	function _showReadStatus(cellValue, options, rawObject) {
 		if(cellValue == "true"){
 			return "<font color='green'>已读</font>"
 		}else{
 			return "<font color='red'>未读</font>"
 		}
 	}

</script>
 <div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2><fmt:message key="收件箱"/></h2>
	</div>
	
	<div id="header_links" align="right">
		<a class="padding3" id="deleteSM" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteInbox()"  data-icon=""><button class="btn"><fmt:message key="button.delete"/></button></a>
	</div>
</div>

<div id="gridlistdiv"><%= request.getAttribute("script") %></div>
</div>