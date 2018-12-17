<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%-- <page:applyDecorator name="default"> --%>

<head>
<title><fmt:message key="通讯管理" /></title>
<meta name="heading" content="<fmt:message key='通讯管理'/>" />
</head>
<%@ include file="/common/messages.jsp"%>
<script type="text/javascript">
	//the javascript written here because of using jstl tag for get department list
	//construct json data for jsTree

	function getActiveChildData(currentNode, parentNode, level, rootNode) {

		var hasChildNode = 0;
		if (rootNode == "Organization") {

			if (level == 1) {

				<c:forEach items="${unitUnionList}" var="rootDepartment" varStatus="status">
				var superDepartment = "${rootDepartment.sjbmdm}";
				var superDept = superDepartment.replace(/ /g, '');

				if (currentNode == superDept) {
	
					hasChildNode = hasChildNode + 1;
					var departmentId = "${rootDepartment.id}";
					var departmentId2 = departmentId.replace(/ /g, '');
					var departmentId3 = "${rootDepartment.bm}";
					var departmentId4 = "${rootDepartment.bmdm}";
					/* console.log("departmentId="+departmentId);
					console.log("departmentId2="+departmentId2); */
					$("#department_tree").jstree("create",
							$("#" + currentNode), false, {
								attr : {
									id : departmentId4,
									name : encodeURI("${rootDepartment.bmdm}")
								},
								data : "${rootDepartment.bm}"
							}, false, true);
				}
				</c:forEach>;
			} else {

				<c:forEach items="${unitUnionList}" var="childDepartment" varStatus="status">
				var departmentId = "${childDepartment.id}";
				var departmentId2 = departmentId.replace(/ /g, '');
				var superDepartment = "${childDepartment.sjbmdm}";
				var superDept = superDepartment.replace(/ /g, '');
				var departmentId4 = "${childDepartment.bmdm}";

				/*  console.log("superDept="+superDept);
				 console.log("currentNode="+currentNode); */
				if (currentNode == superDept) {
					hasChildNode = hasChildNode + 1;
					$("#department_tree").jstree("create",
							$("#" + currentNode), false, {
								attr : {
									id : departmentId4,
									name : encodeURI("${childDepartment.bmdm}")
								},
								data : "${childDepartment.bm}"
							}, false, true);

				}
				</c:forEach>;
			}
		}
		if (hasChildNode == 0) {
			$("#" + currentNode).removeClass('jstree-open');
			$("#" + currentNode).removeClass('jstree-closed');
			$("#" + currentNode).addClass('jstree-leaf');
		}
	}

	function defaultUnitJsonData() {
		var jsonData = '[ {"data":"部门","attr":{"id" : "Organization","name" : "Organization","parent" : "Organization"},"metadata": {"id" : "Organization", "name" : "Organization"}}]'
		return jsonData;
	}

	$(function() {
		var targetWidth = $('#target').width();
		var targetHeight = $('#target').height();
		var _width = parseInt(targetWidth) / 5;
		$('#department_tree').height(parseInt(targetHeight) - 128);
		$('#department_tree').width("100%");
		$('#user-details').width('100%');
		$('#user-grid').width('100%');
		$('#user-grid').height(parseInt(targetHeight) - 128);
		$("#addUsers").hide();
		var jsonData = defaultUnitJsonData();
		//var jsonData = defaultDeptJsonData1();
		constructJsTree("department_tree", jsonData, "getUnitUnionGrid", true);
	});

	function getUnitUnionGrid(e, data) {
		var parentName = "";
		var id = data.rslt.obj.attr("id");
		//var domId = data.inst.get_node(data.selected[0]).id;
		if (id == "Organization") {
			parentName = data.rslt.obj.attr("id");
		} else {
			parentName = data.inst.get_path()[0];
		}
		parentName = decodeURI(parentName);
		id = decodeURI(id);

		document.getElementById("createCode").value = id;
		$.ajaxSetup({
			cache : false
		});
		$.ajax({
			url : 'bpm/admin/getTxlGrid',
			type : 'GET',
			dataType : 'html',
			contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
			async : false,
			data : 'id=' + encodeURI(id) + "&parentNode="
					+ encodeURI(parentName),
			success : function(response) {
				if (response.length > 0) {
					$("#user-grid").html(response);
					$('#grid_header_links').html($('#header_links').html());
				}
			}
		});
	}

	function getUnitUnionGrid1(data) {
		var id = data;
		var params = "code=" + id;
		document.location.href = "#bpm/admin/unitUnion";
		_execute('target', params);

	}

	//新增节点
	function getNewForm() {
		var params = "id=" + $("#createCode").val();
		document.location.href = "#bpm/admin/addTxl";
		_execute('target', params);
	}

	/* function getFormdetail(id1,mark,defpIds1){

	 var params = "id="+id1+"&create2="+mark+"&defpIds="+defpIds1;
	 document.location.href = "#bpm/admin/createUnitUnion";
	 _execute('target', params);
	
	 } */

	function deleteAllChancesConfirmNotcie(hrjInfoIds) {
		var params = 'hrjInfoIds=' + encodeURI(hrjInfoIds);
		$.msgBox({
			title : form.title.confirm,
			content : form.msg.confirmToDeleteGroup,
			type : "confirm",
			buttons : [ {
				value : "是"
			}, {
				value : "否"
			} ],
			success : function(result) {
				if (result == "是") {
					document.location.href = "#bpm/admin/cxzczxcz";
					_execute('target', params);
				} else {
					return false;
				}
			}
		});
	}

	function deleteTxl() {
		var rowid = gridObj.getGridParam('selarrrow');
		var chanceId;
		rowid.forEach(function(item) {
			var id = gridObj.jqGrid('getCell', item, 'id');
			chanceId = chanceId + "," + id;

		});

		if (rowid.length != 0) {
			deleteAllChancesConfirmNotcie(chanceId);
		}

		if (rowid.length == 0) {
			validateMessageBox("提示", "请选择要删除的记录", false);
		}
	}

	function _showEditUnitUnionMessage(cellValue, options, rawObject) {
		return '<a id="one" href="#bpm/admin/txlForm" data-role="button" data-theme="b" onclick="_execute(\'target\',\'id='
				+ encodeURI(rawObject.id)
				+ '\');"><b>'
				+ rawObject.bm
				+ '</b></a>';

	}
</script>

<input type="hidden" id="createCode" name="createCode" />

<div class="row-fluid">
	<div class="page-header">
		<img src="/images/listcode.png "
			style="float: left; margin: 9px 6px 5px 5px" />
		<h2>
			<fmt:message key="通讯录" />
		</h2>

		<div id="header_links" align="right">
			<a class="padding3" id="createUser" href="javascript:void(0);"
				data-role="button" data-theme="b" onClick="getNewForm()"
				data-icon=""><button class="btn">
					<fmt:message key="button.createDepartment" />
				</button></a> <a class="padding3" id="deleteUsers" href="javascript:void(0);"
				data-role="button" data-theme="b" onClick="deleteTxl()" data-icon=""><button
					class="btn">
					<fmt:message key="button.delete" />
				</button></a>

		</div>
	</div>

	<div class="span12">
		<div class="span3">
			<div class="widget">
				<div class="widget-header">
					<div class="title">
						<span class="fs1" aria-hidden="true"></span>
						<fmt:message key="importBpmn.classification" />
					</div>
				</div>
				<div class="widget-body">
					<div id="department_tree"
						class="floatLeft department-list-departments department-jstree overflow"></div>
					<div class="clearfix"></div>
				</div>
			</div>
		</div>
		<div class="span9">
			<div class="widget">
				<div class="widget-header">
					<div class="title">
						<span class="fs1" aria-hidden="true"></span>
						<fmt:message key="通讯管理" />
					</div>
				</div>
				<div class="widget-body">
					<div id="user-details" class="floatLeft department-list-users">
						<div id="user-grid">
							<%=request.getAttribute("script")%>
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