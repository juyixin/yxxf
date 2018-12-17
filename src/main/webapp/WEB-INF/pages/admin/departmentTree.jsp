<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%@ include file="/common/messages.jsp" %>
<script type="text/javascript">

//the javascript written here because of using jstl tag for get department list
//construct json data for jsTree

function getActiveChildData(currentNode, parentNode, level, rootNode){
	var hasChildNode = 0;
    if(rootNode == "Role"){
        if(level == 1){
            <c:forEach items="${roleList}" var="role" varStatus="status">
                var roleId =  "${role.id}";
                var roleId2    = roleId.replace(/ /g,'');
                $("#department_tree").jstree("create", $("#"+currentNode), false, {attr : {id: roleId2, name: encodeURI("${role.id}")}, data: "${role.viewName}"}, false, true);
                hasChildNode++;
             </c:forEach>;
        }
    }else if(rootNode == "Organization"){
        if(level == 1){
             <c:forEach items="${departmentList}" var="rootDepartment" varStatus="status">
                var superDepartment = "${rootDepartment.superDepartment}";
                var superDept = superDepartment.replace(/ /g,'');
                if(currentNode == superDept){
                	hasChildNode = hasChildNode + 1;
                    var departmentId =  "${rootDepartment.id}";
                    var departmentId2    = departmentId.replace(/ /g,'');
                    $("#department_tree").jstree("create", $("#"+currentNode), false, {attr : {id: departmentId2, name: encodeURI("${rootDepartment.id}")}, data: "${rootDepartment.viewName}"}, false, true);
                }
            </c:forEach>;
        }else{
            <c:forEach items="${departmentList}" var="childDepartment" varStatus="status">
                var departmentId =  "${childDepartment.id}";
                var departmentId2    = departmentId.replace(/ /g,'');
                var superDepartment = "${childDepartment.superDepartment}";
                var superDept = superDepartment.replace(/ /g,'');
                   if(currentNode == superDept){
                	   hasChildNode = hasChildNode + 1;
                       $("#department_tree").jstree("create", $("#"+currentNode), false, {attr : {id: departmentId2, name: encodeURI("${childDepartment.id}")}, data: "${childDepartment.viewName}"}, false, true);
                   }
               </c:forEach>;
        }
    }else if(rootNode == "Group"){
        if(level == 1){
            <c:forEach items="${groupList}" var="parentGroup" varStatus="status">
                var groupId =  "${parentGroup.id}";
                var groupId2    = groupId.replace(/ /g,'');
                if("${parentGroup.isParent}" == "true"){
                	hasChildNode = hasChildNode + 1;
                    $("#department_tree").jstree("create", $("#"+currentNode), false, {attr : {id: groupId2, name: encodeURI("${parentGroup.id}")}, data: "${parentGroup.viewName}"}, false, true);
                }
            </c:forEach>;
        }else{
            <c:forEach items="${groupList}" var="parentGroup" varStatus="status">
                var groupId =  "${parentGroup.id}";
                var groupId2    = groupId.replace(/ /g,'');
                var superGroup = "${parentGroup.superGroup}";
                superGroup = superGroup.replace(/ /g,'');
                if(superGroup == currentNode){
                	hasChildNode = hasChildNode + 1;
                    $("#department_tree").jstree("create", $("#"+currentNode), false, {attr : {id: groupId, name: encodeURI("${parentGroup.id}")}, data: "${parentGroup.viewName}"}, false, true);
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
     var jsonData = defaultJsonData();
     constructJsTree("department_tree",jsonData,"getDepartmentUserGrid",true);
});

function getDepartmentUserGrid(e,data,queryName){
	
	var parentName="";
	var id = "";

	if(queryName != undefined){
		var id = "all";
		var parentName = "all";
	}else{
		
		$("#queryName").val("");
		
		id = data.rslt.obj.attr("id");
		var userRole=$("#currentUserRoles").val().split(",");
		var isAdmin=checkRoles(userRole,"ROLE_ADMIN");
		
		if(id=="All" || !isAdmin){
	        $("#addUsers").hide();
		}else{
	        $("#addUsers").show();
		}
		if(id=="Organization" || id=="Group" || id=="Role" || id=="All"){
			parentName=data.rslt.obj.attr("id");
		}else{
			parentName = data.inst.get_path(data.node,true)[0];
		}
		parentName = decodeURI(parentName);
	
		id = decodeURI(id);
	}

	$.ajaxSetup({ cache:false});
	$.ajax({
		url: 'bpm/admin/getDepartmentUserGrid',
        type: 'GET',
        dataType: 'html',
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		async: false,
		data: 'id=' + encodeURI(id) + "&parentNode=" + encodeURI(parentName) + "&name=" + encodeURI($("#queryName").val()),
		success : function(response) {
			if(response.length > 0){
				$("#user-grid").html(response);
				$('#grid_header_links').html($('#header_links').html());
			}
		}
	});
}

function query(){
	var queryName = $("#queryName").val();
	getDepartmentUserGrid('','',queryName);
}

</script>

	<div class="row-fluid">
		<div class="page-header">
			<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2><fmt:message key="user.manageUsers"/></h2>
		
			<div id="header_links" align="right">
				<input type="text" id="queryName" placeholder="用户名 / 姓名" style="width:120px;"/>
				<a class="padding3" id="createUser" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="query()"  data-icon=""><button class="btn">查询</button></a>
          		<a class="padding3" id="createUser" href="#bpm/admin/userform" data-role="button" data-theme="b"  onClick="return createDepartmentUser()"  data-icon=""><button class="btn"><fmt:message key="button.createNew"/></button></a>
               	<a class="padding3" id="enableUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="updateUserStatus('disable');"  data-icon=""><button class="btn"><fmt:message key="button.disable"/></button></a>
               	<a class="padding3" id="deleteUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteUsers()"  data-icon=""><button class="btn"><fmt:message key="button.delete"/></button></a>
 	            <a class="padding3" id="addUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="showUsers('User','multi', 'selectUsers');"  data-icon=""><button class="btn">添加</button></a>
           	</div>
        </div>

		<div class="span12">
			<div class="span3">
				<div class="widget">
					<div class="widget-header">
						<div class="title">
							<span class="fs1" aria-hidden="true"></span> <fmt:message key='userList.user'/>
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
							<span class="fs1" aria-hidden="true"></span><fmt:message key='userList.title'/>
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
