<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%-- <page:applyDecorator name="default"> --%>

<head>
    <title><fmt:message key="userList.title"/></title>
    <meta name="heading" content="<fmt:message key='案例信息表维护'/>"/>
</head>
<%@ include file="/common/messages.jsp" %>
<script type="text/javascript">

//the javascript written here because of using jstl tag for get department list
//construct json data for jsTree

function getActiveChildData(currentNode, parentNode, level, rootNode){
	var hasChildNode = 0;
    if(rootNode == "Organization"){

        if(level == 1){
             <c:forEach items="${allxList}" var="rootAllx" varStatus="status">

                var superDepartment = "${rootAllx.superDepartment}";
                var superDept = superDepartment.replace(/ /g,'');
             
                if(currentNode == superDept){
                	hasChildNode = hasChildNode + 1;
                    var departmentId =  "${rootAllx.id}";
                    var departmentId2    = departmentId.replace(/ /g,'');
                    var departmentId3 =  "${rootAllx.lxdm}";
                    $("#department_tree").jstree("create", $("#"+currentNode), false, {attr : {id: departmentId3, name: encodeURI("${rootAllx.lxdm}")}, data: "${rootAllx.lxmc}"}, false, true);
                }
            </c:forEach>;
        }else{

            <c:forEach items="${allxList}" var="childDepartment" varStatus="status">
                var departmentId =  "${childDepartment.id}";
                var departmentId2    = departmentId.replace(/ /g,'');
                var superDepartment = "${childDepartment.superDepartment}";
                var superDept = superDepartment.replace(/ /g,'');
                var departmentId3 =  "${childDepartment.lxdm}";
                   if(currentNode == superDept){
                	   hasChildNode = hasChildNode + 1;
                       $("#department_tree").jstree("create", $("#"+currentNode), false, {attr : {id: departmentId3, name: encodeURI("${childDepartment.lxdm}")}, data: "${childDepartment.lxmc}"}, false, true);
                   }
               </c:forEach>;
        }
    }
    if(hasChildNode == 0){
    	$("#"+currentNode).removeClass('jstree-open');
    	$("#"+currentNode).removeClass('jstree-closed');
    	$("#"+currentNode).addClass('jstree-leaf');
    }
}

function defaultAllxJsonData(){
	var jsonData = '[ {"data":"案例类别","attr":{"id" : "Organization","name" : "Organization","parent" : "Organization"},"metadata": {"id" : "Organization", "name" : "Organization"}}]'
	return jsonData;
}

$(function () {
     var targetWidth = $('#target').width();
     var targetHeight = $('#target').height();
     var _width = parseInt(targetWidth) / 5;
     $('#department_tree').height(parseInt(targetHeight)-128);
     $('#department_tree').width("100%");
	 $('#user-details').width('100%');
     $('#user-grid').width('100%');
     $('#user-grid').height(parseInt(targetHeight)-128);
     $("#addUsers").hide();
     var jsonData = defaultAllxJsonData();
     constructJsTree("department_tree",jsonData,"getDepartmentGrid",true);
});

function getDepartmentGrid(e,data){
	
	var parentName="";
	var id = data.rslt.obj.attr("id");
	//var userRole=$("#currentUserRoles").val().split(",");
	//var isAdmin=checkRoles(userRole,"ROLE_ADMIN");
	  document.getElementById("createCode").value=id;
	
	if(id=="Organization") {
		parentName=data.rslt.obj.attr("id");
	}else{
		parentName = data.inst.get_path()[0];
	}
	parentName = decodeURI(parentName);
	id = decodeURI(id);
	$.ajaxSetup({ cache:false});
	$.ajax({
		url: 'bpm/allxgl/getAlxxbGrid',
        type: 'GET',
        dataType: 'html',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		async: false,
		data: 'id=' + encodeURI(id) + "&parentNode=" + encodeURI(parentName),
		success : function(response) {
			if(response.length > 0){
				$("#user-grid").html(response);
				$('#grid_header_links').html($('#header_links').html());
			}
		}
	});
}
//新增案例
function getNewForm(){
 
	var params = "id="+$("#createCode").val();
	document.location.href = "#bpm/alxxb/alxxbForm";
    _execute('target', params);
 }
 //编辑
function _showEditAlxxb(cellValue, options, rawObject) {
	return '<a id="editgroup" href="#bpm/alxxb/editAlxxbForm" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+encodeURI(rawObject.id)+ '\');"><b>' + rawObject.name + '</b></a>';
}
 
//详情
function _showViewAlxxb(cellValue, options, rawObject) {
	return '<a id="viewgroup" href="#bpm/alxxb/viewAlxxbForm" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
			+encodeURI(rawObject.id)+ '\');"><b>' + rawObject.name + '</b></a>';
}
//删除节点
 
function deleteAllxs(){
	var rowid =  gridObj.getGridParam('selarrrow');
	var departmentTypes=new Array();
	var i=0;
	rowid.forEach(function(item) {
		var deploymentType = gridObj.jqGrid('getCell', item, 'departmentType');
		departmentTypes[i] = deploymentType;
		i++;
	});
	
	var deptIds=[];
	rowid.forEach(function(item) {
		var id = gridObj.jqGrid('getCell', item, 'lxdm');
		deptIds[deptIds.length]=id;
	});
	if(rowid.length !=0 ){
		if(departmentTypes.indexOf('root') > -1){
					validateMessageBox(form.title.message, form.msg.cannotDeleteRootDepartment, false);
			
		}else{
			deleteAllDepartmentsConfirms(deptIds);
		}
	}
	if(rowid.length ==0){
		validateMessageBox(form.title.message, form.msg.selectAtleastSingleDepartmentToDelete, false);
		
	}
}
function deleteAllDepartmentsConfirms(departmentIds){
	params = 'departmentIds='+JSON.stringify(departmentIds);
	$.msgBox({
	    title: form.title.confirm,
	    content: form.msg.confirmToDeleteDepartment,
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	    success: function (result) {
	        if (result == "是") {
	        	document.location.href = "#bpm/admin/deleteSelectedAllxs";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}

function deleteRecords(){
	var rowid = gridObj.getGridParam('selarrrow');
	
	var alxxbId;
	rowid.forEach(function(item) {
		var id = gridObj.jqGrid('getCell', item, 'id');
		alxxbId = alxxbId+","+id;
	});
	
	if(rowid.length !=0 ){
		deleteAllRecordsConfirm(alxxbId);
	}
	
	if(rowid.length ==0){
		validateMessageBox(form.title.message, "请选择要删除的记录", false);
	}
}

function deleteAllRecordsConfirm(alxxbIds){
	params = 'alxxbIds='+encodeURI(alxxbIds);
	$.msgBox({
	    title: form.title.confirm,
	    content: "确定要删除记录吗",
	    type: "confirm",
	    buttons: [{ value: "是" }, { value: "否" }],
	    success: function (result) {
	        if (result == "是") {
	        	document.location.href = "#bpm/alxxb/deleteSelectedAlxxb";
	    		_execute('target',params);
	        }else{
	  		  return false;
	  	  }
	    }
	});
}


function onFtxxInfoSearch(){
	var searchText=$("#searchText").val();
	var params = "searchText="+encodeURI(encodeURI(searchText))+"&id="+$("#createCode").val();
	document.location.href="#bpm/alxxgl/alxxwh";
	_execute('target', params);
}
</script>
<input type="hidden" id="createCode"  name="createCode"/> 

	<div class="row-fluid">
			<div class="page-header">
				<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2><fmt:message key="案例信息表管理"/></h2>
			
			<div id="header_links" align="right">
			
				<a style="padding: 3px; ">
		 <input id="searchText"
			type="text" placeholder="输入案例名称" style="width: 185px"></input>
		</a>
	
		
		<input type="button" onclick="onFtxxInfoSearch()" value="查询"/> 
           		<a class="padding3" id="createUser" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="getNewForm()"  data-icon=""><button class="btn"><fmt:message key="button.createDepartment"/></button ></a>
                <a class="padding3" id="deleteUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteRecords()"  data-icon=""><button class="btn"><fmt:message key="button.deleteDepartment"/></button></a>
		
				<!-- <a class="padding10" id="exportCSV" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="gridDataExportToCSV('DEPARTMENT_LIST','script')"  data-icon=""><fmt:message key="button.deleteDepartment"/></a>-->
	       </div>
	           	</div>

		<div class="span12">
			<div class="span3">
				<div class="widget">
					<div class="widget-header">
						<div class="title">
							<span class="fs1" aria-hidden="true"></span><fmt:message key="importBpmn.classification"/>
						</div>
					</div>
					<div class="widget-body">
						<div id="department_tree" class="floatLeft department-list-departments department-jstree overflow"></div>
						<div class="clearfix"></div>
					</div>
				</div>
			</div>
			<div class="span9">
				<div class="widget">
					<div class="widget-header">
						<div class="title">
							<span class="fs1" aria-hidden="true"></span><fmt:message key="案例信息表管理"/>
						</div>
					</div>
					<div class="widget-body">
						<div id="user-details" class="floatLeft department-list-users">
                           	<div id="user-grid">
                               	<%= request.getAttribute("script") %>
                           	</div>
                       	</div>
						<div class="clearfix"></div>
					</div>
				</div>
			</div>
		</div>
	</div>

<script type="text/javascript">
$('#grid_header_links').html($('#header_links').html());
</script>
