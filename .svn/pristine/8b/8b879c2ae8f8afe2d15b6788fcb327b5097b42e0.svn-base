<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>

<%-- <page:applyDecorator name="default"> --%>

<head>
<title></title>
</head>

	<div class="page-header">
					<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2 style="margin:2px;"><fmt:message key="政策法规"/></h2>
				 	<div class="height10"></div>
						<div id="header_links" align="right" style="padding: 2px 0">
		            		<a class="padding3" id="createUser" href="#bpm/admin/addZcfgForm" data-role="button" data-theme="b"    data-icon="">
		            		<button class="btn"><fmt:message key="button.createNew"/></button></a>
							<a class="padding3" id="deleteUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteZcfgData()"  data-icon="">
							<button class="btn"><fmt:message key="button.delete"/></button></a>
 	           			</div>
 	           		</div>
					<div id="noticeInfo" name="noticeInfo">
						<%= request.getAttribute("script") %>
					</div>

<script>
function deleteAllChancesConfirmZcfg(hrjInfoIds){
	    	params = 'hrjInfoIds='+encodeURI(hrjInfoIds);
	    	$.msgBox({
	    	    title: form.title.confirm,
	    	    content: form.msg.confirmToDeleteGroup,
	    	    type: "confirm",
	    	    buttons: [{ value: "是" }, { value: "否" }],
	    	    success: function (result) {
	        	   
	    	        if (result == "是") {
	    	        	document.location.href = "#bpm/admin/zcfgremove";
	    	    		_execute('target',params);
	    	        }else{
	    	  		  return false;
	    	  	  }
	    	    }
	    	});
	    }

		 
		 
		function deleteZcfgData(){
		var rowid = gridObj.getGridParam('selarrrow');
		var chanceId;
		rowid.forEach(function(item) {
			var id = gridObj.jqGrid('getCell', item, 'id');
			chanceId = chanceId+","+id;
			
		});
		
		if(rowid.length !=0 ){
			deleteAllChancesConfirmZcfg(chanceId);
		}
		
		if(rowid.length ==0){
			validateMessageBox("提示", "请选择要删除的记录", false);
		}
	}	 
		
		
</script>
