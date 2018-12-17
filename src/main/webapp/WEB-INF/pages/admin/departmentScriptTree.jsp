<%@ include file="/common/taglibs.jsp"%>

<%-- <page:applyDecorator name="default"> --%>

<head>
    <title><fmt:message key="userList.title"/></title>
    <meta name="heading" content="<fmt:message key='user.manageUsers'/>"/>
</head>
<%@ include file="/common/messages.jsp" %>
<script type="text/javascript">

//the javascript written here because of using jstl tag for get department list
//construct json data for jsTree

function getActiveChildData(currentNode, parentNode, level, rootNode){

	var hasChildNode = 0;
    if(rootNode == "Organization"){

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
     var jsonData = defaultDeptJsonData();

     constructJsTree("department_tree",jsonData,"getDepartmentGrid",true);
});

function getDepartmentGrid(e,data){
	
	var parentName="";
	var id = data.rslt.obj.attr("id");
	//var userRole=$("#currentUserRoles").val().split(",");
	//var isAdmin=checkRoles(userRole,"ROLE_ADMIN");
	
	
	if(id=="Organization") {
		parentName=data.rslt.obj.attr("id");
	}else{
		parentName = data.inst.get_path()[0];
	}
	parentName = decodeURI(parentName);

	id = decodeURI(id);
	$.ajaxSetup({ cache:false});
	$.ajax({
		url: 'bpm/admin/getDepartmentGrid',
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
</script>



	<div class="row-fluid">
			<div class="page-header">
				<img src="/images/listcode.png " style="float:left; margin:9px 6px 5px 5px" /><h2><fmt:message key="dept.manageDepartment"/></h2>
			
			<div id="header_links" align="right">
           		<a class="padding3" id="createUser" href="#bpm/admin/departmentForm" data-role="button" data-theme="b"  onClick="return createDepartmentUser()"  data-icon=""><button class="btn"><fmt:message key="button.createDepartment"/></button ></a>
                <a class="padding3" id="deleteUsers" href="javascript:void(0);" data-role="button" data-theme="b"  onClick="deleteDepartments()"  data-icon=""><button class="btn"><fmt:message key="button.deleteDepartment"/></button></a>
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
							<span class="fs1" aria-hidden="true"></span><fmt:message key="dept.manageDepartment"/>
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
