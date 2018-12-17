<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>

<%-- <page:applyDecorator name="default"> --%>

<head>
<title></title>
</head>

<script src="<c:url value='/scripts/common.js'/>" type="text/javascript"></script>

	<div class="page-header">
					<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2 style="margin:2px;"><fmt:message key="党建速递"/></h2>
				 	<div class="height10"></div>
						<div id="header_links" align="right" style="padding: 2px 0">
		            		<a class="padding3" id="createUser" href="#bpm/admin/addNoticeForms" data-role="button" data-theme="b"    data-icon="">
		            		<button class="btn"><fmt:message key="button.createNew"/></button></a>
							<a class="padding3" id="deleteUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteNoticeData()"  data-icon="">
							<button class="btn"><fmt:message key="button.delete"/></button></a>
 	           			</div>
 	           		</div>
					<div id="noticeInfo" name="noticeInfo">
						<%= request.getAttribute("script") %>
					</div>

<script>

$(document).ready(function(){
	var read='${readOnly}';
	if(read == "readOnly"){
		$("#createUser").css("display","none");
		$("#deleteUsers").css("display","none"); 
	}
});
function deleteAllChancesConfirmNotcie(hrjInfoIds){
	    	params = 'hrjInfoIds='+encodeURI(hrjInfoIds);
	    	$.msgBox({
	    	    title: form.title.confirm,
	    	    content: "确定要删除这些记录",
	    	    type: "confirm",
	    	    buttons: [{ value: "是" }, { value: "否" }],
	    	    success: function (result) {
	        	   
	    	        if (result == "是") {
	    	        	document.location.href = "#bpm/admin/noticeremoves";
	    	    		_execute('target',params);
	    	        }else{
	    	  		  return false;
	    	  	  }
	    	    }
	    	});
	    }

		 
		 
		function deleteNoticeData(){
		var rowid = gridObj.getGridParam('selarrrow');
		var chanceId;
		rowid.forEach(function(item) {
			var id = gridObj.jqGrid('getCell', item, 'id');
			chanceId = chanceId+","+id;
			
		});
		
		if(rowid.length !=0 ){
			deleteAllChancesConfirmNotcie(chanceId);
		}
		
		if(rowid.length ==0){
			validateMessageBox("提示", "请选择要删除的记录", false);
		}
	}	 
</script>