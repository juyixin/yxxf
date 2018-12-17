<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>

<head>
<title></title>

</head>
<%@ include file="/common/taglibs.jsp"%>

<div class="page-header">
	<div class="pull-left">
		<img src="/images/listcode.png "
			style="float: left; margin: 9px 6px 5px 5px" />
		<h2 style="margin: 2px;">
			<fmt:message key="列表信息" />
		</h2>
		





	</div>

	<div class="height10"></div>
	<div id="header_links" align="right">
		<a style="padding: 3px; ">
		 <input id="searchText"
			type="text" placeholder="输入姓名查找" style="width: 185px"></input>
		</a>
	
		
		<input type="button" onclick="onFtxxInfoSearch()" value="查询"/>    
		<a class="padding3" id="createUser"
			href="#bpm/admin/newPerson" data-role="button"
			data-theme="b" onClick="_execute('target','');" data-icon=""><input type="button"
			value="新建">	
			</input></a> 
			
			<input type="button" onclick="deletePersonInfo()" value="删除"/>  			
	</div>
</div>
<div><%=request.getAttribute("script")%></div>

<script>
function onFtxxInfoSearch(){
	var searchText=$("#searchText").val();
	var params = "searchText="+encodeURI(encodeURI(searchText));
	document.location.href="#bpm/admin/petitionLetterPersonList";
	_execute('target', params);
}


//删除
function deletePersonInfo(){
	var rowid =  gridObj.getGridParam('selarrrow');
	var defpIds = new Array();
	rowid.forEach(function(item) {
		defpIds[defpIds.length] = item;
	});
    var id = new Array();
    for(var i=0;i<defpIds.length;i++){
    	var rowData = $("#_PERSON_LIST").jqGrid('getRowData',rowid[i]);
    	var j = rowData["id"];
    	id.push(j);
    }
	if(rowid.length == 0){
		validateMessageBox(form.title.message, "至少选择一行删除", "info");
	}
	if(rowid.length !=0 ){
		deleteLink(id);		
	}
}

function deleteLink(id){

	$.msgBox({
	    title: "确认",
	    content: "是否删除选中数据",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	        	success: function (result) {
	    	        if (result == "是") {
	    	    		jQuery.ajax({
	    	    			url:'bpm/admin/deletePersonList',
	    	    			type:"get",
	    	    			data: {id:JSON.stringify(id)},
	    	    			dataType:"json",
	    	    		    success:function(data){
	    	    				if(data.code == "1"){
	    	    					linList();
	    	    				} else {
	    	    					alert(data.msg);
	    	    				}
	    	    			},
	    	    			error : function(ex) { alert(ex.status); }
	    	    			});	
	    	        }
	    	    }
	});
}


function linList(){
	document.location.href="#bpm/admin/petitionLetterPersonList";
	_execute('target', '');
}
</script>