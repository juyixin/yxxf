<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
		<spring:bind path="menu.*">
      		<%-- Error Messages --%>
				<c:if test="${not empty errors}">
				    <%-- <div class="alert alert-error fade in">
				        <a href="#" data-dismiss="alert" class="close">&times;</a>
				        <c:forEach var="error" items="${errors}">
				            <c:out value="${error}"/><br />
				        </c:forEach>
				    </div>
				    <c:remove var="errors"/> --%>
				    <script type="text/javascript">
				    var error = "";
				    <c:forEach var="error" items="${errors}">
				    	error = error + "<c:out value='${error}' escapeXml='false'/> <br />";
					</c:forEach>
					validateMessageBox(form.title.error, error, "error");
				    </script>
				     <c:remove var="errors"/>
				</c:if>
				
				<%-- Success Messages --%>
				<c:if test="${not empty successMessages}">
				    <%-- <div class="alert alert-success fade in">
				        <a href="#" data-dismiss="alert" class="close">&times;</a>
				        <c:forEach var="msg" items="${successMessages}">
				            <c:out value="${msg}"/><br />
				        </c:forEach>
				    </div>
				    <c:remove var="successMessages" scope="session"/> --%>
				    <script type="text/javascript">
				    var msg = "";
				    <c:forEach var="error" items="${successMessages}">
				    	msg = msg + "<c:out value='${error}' escapeXml='false'/> <br />";
					</c:forEach>
						$.msgBox({
							title : form.title.success,
							content : msg,
							type : "success",
							buttons : [ {
								value : "Yes"
							}, {
								value : "No"
							} ],
							success : function(result) {
								if (result == "Yes") {
									//alert("i have to restart ");
									document.location.href = "/logout";
								} else {
									setTimeout(function() {
										openMessageBox(form.title.message,
												form.msg.changeEffectInNextLogin, "Message",
												true);
									}, 500);
									return false;
								}
							}
						});
					</script>
				     <c:remove var="successMessages" scope="session"/> 
				</c:if>
				<c:if test="${!newTab}">
				<c:if test="${not empty status.errorMessages}">
				    <%-- <div class="alert alert-error fade in">
				        <a href="#" data-dismiss="alert" class="close">&times;</a>
				        <c:forEach var="error" items="${status.errorMessages}">
				            <c:out value="${error}" escapeXml="false"/><br/>
				        </c:forEach>
				    </div> --%>
				   <script type="text/javascript">
				    var error = "";
				    <c:forEach var="error" items="${status.errorMessages}">
				    	error = error + "<c:out value='${error}' escapeXml='false'/> <br />";
					</c:forEach>
					validateMessageBox(form.title.error, error , "error");
				    </script>
				</c:if>
				</c:if>
				<c:set var="pathInfo" value='<%=request.getAttribute("javax.servlet.forward.servlet_path")%>'/>
				<script type="text/javascript">
				var path = "<c:out value='${pathInfo}' escapeXml='false'/>";
				setIndexPageByRedirect(path);
				</script>
  		</spring:bind>
<div class="box_main">
	<form:form id="menu" commandName="menu" method="post"
		class="form-horizontal no-margin" action="bpm/admin/saveMenu"
		autocomplete="off" modelAttribute="menu"
		onsubmit="_execute('menu_form','');">
		<form:hidden path="id" id="id" />

		<%-- <div class="control-group"> 
						<div class="controls">
							<h4><span><fmt:message key="menu.menuForm.needTologgedOutAfterCreationOrUpdate"/></span></h4>	
						</div>
					</div>  --%>

		<div class="control-group">
			<eazytec:label styleClass="control-label" key="menu.name" />
			<div class="controls uneditable-input1">
				<form:input path="name" id="name" class="span8" depends="required" />
			</div>
		</div>

		<div class="control-group">
			<eazytec:label styleClass="control-label" key="menu.helpText" />
			<div class="controls uneditable-input1">
				<form:input id="helpText" path="helpText" class="span8" />
			</div>
		</div>

		<div class="control-group">
			<eazytec:label styleClass="control-label" key="menu.urlType" />
			<div class="controls uneditable-input1">
				<form:select path="urlType" id="urlType" name="urlType"
					class="span8" onChange="changeLabelNameOfURL(this.value);">
					<form:option value="url">
						<fmt:message key="menu.menuForm.url" />
					</form:option>
					<form:option value="script">
						<fmt:message key="menu.menuForm.script" />
					</form:option>
					<form:option value="newTab">
						<fmt:message key="menu.menuForm.newTab" />
					</form:option>
					<form:option value="listview">
						<fmt:message key="menu.menuForm.listView" />
					</form:option>
					<form:option value="process">
						<fmt:message key="menu.menuForm.process" />
					</form:option>
					<form:option value="report">
						<fmt:message key="menu.menuForm.report" />
					</form:option>
				</form:select>
			</div>
		</div>

		<div class="control-group" id="url_tr">
			<div id="type_url">
				<eazytec:label styleClass="control-label" key="menu.menuUrl" />
			</div>
			<div id="type_script" class="displayNone">
				<eazytec:label styleClass="control-label" key="menu.menuScript" />
			</div>
			<div id="type_newTab" class="displayNone">
				<eazytec:label styleClass="control-label" key="menu.menuNewTab" />
			</div>
			<div class="controls uneditable-input1">
				<form:input path="menuUrl" class="span8" />
			</div>
		</div>
		<div class="control-group displayNone" id="report_tr">
			<div id="type_url">
				<eazytec:label styleClass="control-label" key="menu.report" />
			</div>
			<div class="controls uneditable-input1">
				<form:input path="reportName" class="span8"
					onclick="showReportSelection('Report', 'single' , 'reportName', this, '');" />
			</div>
		</div>
		<%-- <div class="control-group" id="report_param_tr">
						<div id="type_url">
									<eazytec:label styleClass="control-label" key="menu.reportParam"/>
								</div>
						<div class="controls uneditable-input1">
							<form:textarea path="reportParam" class="span8" placeholder='"keyParam1":"valueParam1","keyParam2":"valueParam2"' />
						</div>
					</div> --%>

		<div class="control-group displayNone" id="list_view_tr">
			<div id="type_url">
				<eazytec:label styleClass="control-label" key="menu.listview" />
			</div>
			<div class="controls uneditable-input1">
				<form:input path="listViewName" class="span8"
					onclick="showListViewSelection('ListView', 'single' , 'listViewName', this, '');" />
			</div>
		</div>

		<div class="control-group" id="list_view_param_tr">
			<div id="type_url">
				<eazytec:label styleClass="control-label" key="menu.listviewparam" />
			</div>
			<div class="controls uneditable-input1">
				<form:textarea path="listViewParam" class="span8"
					placeholder='"keyParam1":"valueParam1","keyParam2":"valueParam2"' />
			</div>
		</div>

		<div class="control-group" id="process_tr">
			<div id="type_url">
				<eazytec:label styleClass="control-label" key="menu.process" />
			</div>
			<div class="controls uneditable-input1">
				<form:input path="processName" class="span8"
					onclick="showProcessSelectionTree('Process', 'single' , 'processName', this, '');" />
			</div>
		</div>

		<div class="control-group">
			<eazytec:label styleClass="control-label" key="menu.menuOrder" />
			<div class="controls uneditable-input1">
				<form:input path="menuOrder" class="span8" />
			</div>
		</div>

		<div class="control-group">
			<eazytec:label styleClass="control-label" key="menu.isParent" />
			<div class="controls uneditable-input1 checkbox-label">
				<form:checkbox id="isParent" path="isParent" />
			</div>
		</div>

		<div class="control-group">
			<eazytec:label styleClass="control-label" key="menu.menuType" />
			<div class="controls uneditable-input1">
				<form:select path="menuType" name="menuType" id="menuType"
					class="span8">
					<option value="header"
						${fn:contains(menu.menuType, 'header') ? 'selected' : ''}>
						<fmt:message key="menu.type.header" />
					</option>
					<option value="top"
						${fn:contains(menu.menuType, 'top') ? 'selected' : ''}>
						<fmt:message key="menu.type.top" />
					</option>
					<option value="side"
						${fn:contains(menu.menuType, 'side') ? 'selected' : ''}>
						<fmt:message key="menu.type.side" />
					</option>
				</form:select>
			</div>
		</div>

		<div class="control-group">
			<eazytec:label styleClass="control-label" key="menu.parentMenu" />
			<div class="controls uneditable-input1">
				<form:select path="parentMenu" name="parentMenu" id="parentMenu"
					class="span8">
					<option value="${menu.parentMenu.id}"
						${fn:contains(menu.parentMenu, sideMenu.name) ? 'selected' : ''}>${menu.parentMenu.name}</option>
				</form:select>
			</div>
		</div>

		<div class="control-group">
			<eazytec:label styleClass="control-label" key="menu.isGlobal" />
			<div class="controls uneditable-input1 checkbox-label">
				<form:checkbox id="isGlobal" path="isGlobal" />
			</div>
		</div>

		<div class="control-group" id="role_row">
			<eazytec:label styleClass="control-label" key="menu.roles" />
			<div class="controls uneditable-input1">
				<input type="hidden" id="menuRoles" name="menuRoles" />
				<input type="text" id="menuRolesFullName"
					class="span8 white-background"
					onclick="showRoleSelection('Role', 'multi' , 'menuRoles', this, '');"
					readonly="readonly" />
			</div>
		</div>

		<div class="control-group" id="group_row">
			<eazytec:label styleClass="control-label" key="menu.groups" />
			<div class="controls uneditable-input1">
				<input type="hidden" id="menuGroups" name="menuGroups" />
				<input type="text" id="menuGroupsFullName"
					class="span8 white-background"
					onclick="showGroupSelection('Group', 'multi' , 'menuGroups', this, '');"
					readonly="readonly" />
			</div>
		</div>

		<div class="control-group" id="department_row">
			<eazytec:label styleClass="control-label" key="menu.departments" />
			<div class="controls uneditable-input1">
				<input type="hidden" id="menuDepartments" name="menuDepartments" />
				<input type="text" id="menuDepartmentsFullName"
					class="span8 white-background"
					onclick="showDepartmentSelection('Department', 'multi' , 'menuDepartments', this, '');"
					readonly="readonly" />
			</div>
		</div>

		<div class="control-group" id="user_row">
			<eazytec:label styleClass="control-label" key="menu.users" />
			<div class="controls uneditable-input1">
				<input type="hidden" id="menuUsers" name="menuUsers"
					class="span8 white-background"
					onclick="showUserSelection('User', 'multi' , 'menuUsers', this, '');"
					readonly="readonly" /> <input type="text" id="menuUsersFullName"
					name="menuUsers" class="span8 white-background"
					onclick="showUserSelection('User', 'multi' , 'menuUsers', document.getElementById('menuUsers'), '');"
					readonly="readonly" />
			</div>
		</div>

		<div class="control-group" id="row_icon1">
			<eazytec:label styleClass="control-label" key="menu.iconPath1" />
			<div class="controls ">
				<form:hidden path="iconPath1" />
				<div class=" span1 menu-icon icon_size checkbox-label"
					align="center">
					<img id="choosed_iconPath1" src="${menu.iconPath1}" />
				</div>
				<a href="javascript:void(0);" onClick="showMenuIcons('iconPath1');">
					<eazytec:label styleClass="control-label cursor-pointer"
						key="menu.chooseIcon" />
				</a>
			</div>
		</div>

		<div class="control-group" id="row_icon2">
			<eazytec:label styleClass="control-label" key="menu.iconPath2" />
			<div class="controls uneditable-input1">
				<form:hidden path="iconPath2" />
				<div class=" span1 menu-icon icon_size checkbox-label"
					align="center">
					<img id="choosed_iconPath2" src="${menu.iconPath2}" />
				</div>
				<a href="javascript:void(0);" onClick="showMenuIcons('iconPath2');">
					<eazytec:label styleClass="control-label cursor-pointer"
						key="menu.chooseIcon" />
				</a>
			</div>
		</div>

		<div class="control-group">
			<eazytec:label styleClass="control-label" key="menu.active" />
			<div class="controls uneditable-input1 checkbox-label">
				<form:checkbox id="isActive" path="active"
					disabled="${menu.isSystemDefined}" />
				<c:if test="${menu.isSystemDefined == true}">
					<form:hidden id="isActive" path="active" value="true" />
				</c:if>
			</div>
		</div>

		<div class="control-group">
			<div class="controls">
				<form:hidden id="isSystemDefined" path="isSystemDefined"
					value="${menu.isSystemDefined}" />
			</div>
		</div>

		<div class="control-group">
			<eazytec:label styleClass="control-label" key="menu.description" />
			<div class="controls uneditable-input1 ">
				<form:textarea path="description" rows="4" class="span8" />
			</div>
		</div>

		<div class="control-group">
			<div class="form-actions no-margin">
				<button type="submit" class="btn btn-primary" name="save"
					onclick="bCancel=false;" id="saveButton">
					<i class="icon-ok icon-white"></i>
					<c:choose>
						<c:when test="${!empty menu.name}">
							<fmt:message key="button.update" />
						</c:when>
						<c:otherwise>
							<fmt:message key="button.save" />
						</c:otherwise>
					</c:choose>
				</button>
				&nbsp;&nbsp;
				<c:if test="${!empty menu.name}">
					<button type="button" class="btn btn-primary" name="next"
						onclick="backToPreviousPage()" id="cancelButton"
						class="cursor_pointer"><i class="icon-remove icon-white"></i>
						<fmt:message key="button.cancel" />
					</button>
				</c:if>
				<div class="clearfix"></div>
			</div>
		</div>

		<div class="control-group">
			<div class="controls">
				<h4>
					<span><fmt:message
							key="menu.menuForm.needTologgedOutAfterCreationOrUpdate" /></span>
				</h4>
			</div>
		</div>

	</form:form>

</div>

	<div id="menu_icon_dialog"></div>
	<script type="text/javascript">
var arr = new Array();
var headerMenus = new Array();
var topMenus = new Array();
var parentMenus = new Array();
var globalMenus = new Array();
var menus = new Array();

function getMenus(){
	$("#parentMenu").empty();
	menus=[];
	var menuType = document.getElementById("menuType");
	var urlType = document.getElementById("urlType").value;
	var menuTypeValue = menuType.options[menuType.selectedIndex].value;
	var isParent= document.getElementById('isParent').checked;
	if(isParent){
		if(menuTypeValue == "side"){
			<c:forEach items="${availableMenus}" var="menu">
			if("${menu.isParent}" == "true" && ("${menu.menuType}" == "top" || "${menu.menuType}" == "header")){
				menus["${menu.id}"] = "${menu.name}";
			}
			</c:forEach>;
			document.getElementById('parentMenu').disabled = false;
		} else {
			document.getElementById('parentMenu').disabled = true;
			menus=[];
		}
		$("#row_icon1").show();
		$("#row_icon2").show();
	}else{
		if(menuTypeValue == "side"){
			document.getElementById('parentMenu').disabled = false;
			<c:forEach items="${availableMenus}" var="menu">
			if("${menu.isParent}" == "true" && "${menu.menuType}" == "side"){
				menus["${menu.id}"] = "${menu.name}";
			}
			</c:forEach>;
		}else{
			menus=[];
			$.msgBox({
	   		    title:form.title.message,
	   		    content: form.msg.checkParentMenuFirst,
	   		    type:'info'
	   		});
		}
		$("#row_icon1").hide();
		$("#row_icon2").hide();
	}
	changeLabelNameOfURL(urlType)
}

$(function () {
	$('#menuRoles').val('${roles}');
	$('#menuGroups').val('${groups}');
	$('#menuDepartments').val('${departments}');
	$('#menuUsers').val('${users}');
	var parentMenu = document.getElementById("parentMenu");
	var parentMenuValue = parentMenu.options[parentMenu.selectedIndex].value;
	if(document.getElementById("id").value == ''){
		if("${initial}" == "false"){
			if("${menu.isParent}" == "true" && ("${menu.menuType}" == "top" || "${menu.menuType}" == "header")){
				menus["${menu.id}"] = "${menu.name}";
			}
		} else {
			$('#isParent').prop('checked', true);
		}
		if("${isActive}" == "false"){
			$('#isActive').prop('checked', false);
		}else{
			$('#isActive').prop('checked', true);
		}
	}
	getMenus();
	 for(var i in menus)
		{
		   $('#parentMenu').append( $('<option> </option>').val(i).html(menus[i]) );   
		} 
	$("#parentMenu").val("${parentMenu}");
	
	$("#isGlobal").click(function() {
		var $this = $(this);
	    if ($this.is(':checked')) {
	    	$("#role_row").hide();
            $("#group_row").hide();
            $("#department_row").hide();
            $("#user_row").hide();
	    } else {
	    	$("#role_row").show();
            $("#group_row").show();
            $("#department_row").show();
            $("#user_row").show();
	    }
	});
	var isGlobalChecked = $("#isGlobal").is(':checked');
	if(isGlobalChecked){
		$("#role_row").hide();
		 $("#group_row").hide();
         $("#department_row").hide();
         $("#user_row").hide();
	} else {
    	$("#role_row").show();
        $("#group_row").show();
        $("#department_row").show();
        $("#user_row").show();
    }
});  

$("#isParent").click(function() {
	var $this = $(this);
    if ($this.is(':checked')) {
    	$('#menuType').empty();
    	$('#menuType').append(new Option('次级菜单', 'side', true, true));
    	$('#menuType').append(new Option('主菜单', 'top', true, true));  
    	$('#menuType').append(new Option('顶部菜单', 'header', true, true));    	
     	
    	$("#row_icon1").show();
		$("#row_icon2").show();
    } else {
    	$('#menuType').empty();
    	$('#menuType').append(new Option('次级菜单', 'side', true, true));
    	$("#row_icon1").hide();
		$("#row_icon2").hide();
    }
    getMenus();	
	for(var i in menus){
		$('#parentMenu').append( $('<option> </option>').val(i).html(menus[i]) );   
	} 
});

/* var isParentChecked = $("#isParent").is(':checked');
if(isParentChecked){
	$('#menuType').empty();
	$('#menuType').append(new Option('Side Menu', 'side', true, true));
	$('#menuType').append(new Option('Top Menu', 'top', true, true));
	$('#menuType').append(new Option('Header Menu', 'header', true, true)); 
	$("#row_icon1").show();
	$("#row_icon2").show();
} */ 
	
	
$('#menuType').change(function() {
	getMenus();

	 for(var i in menus)
	{
	   $('#parentMenu').append( $('<option> </option>').val(i).html(menus[i]) );   
	} 


});


$('#menu').submit(function() {
    if(!$('#isGlobal').is(':checked')){
            $('#isGlobal').val(false);
    }
    if(!$('#isParent').is(':checked')){
            $('#isParent').val(false); 
    }
    if(!$('#isActive').is(':checked')){
            $('#isActive').val(false);
    }
});

function showMenuIcons(){
	$.ajax({
		url: 'bpm/admin/getMenuIcons',
	    type: 'GET',
	    dataType: 'json',
		async: false,
		success : function(response) {
			
		} 
	});
}

function showMenuIcons(iconPath){
	$.ajax({
		url: 'bpm/admin/getMenuIcons',
	    type: 'GET',
	    dataType: 'json',
		async: false,
		success : function(response) {
			var htmlContent = "<table style='background-color:#DEDEDE;border:2px solid #fff;' width='100%'><tr>";
			for(var index = 0; index < response.length; index++){
				if(index % 5 == 0){
					htmlContent = htmlContent + "</tr><tr>";
				}
				htmlContent = htmlContent + "<td align='center' style='padding:5px;border:2px solid #fff;'><img class='cursor-pointer' onClick='selectMenuIcon(\""+response[index]+"\", \""+iconPath+"\");'  src='"+response[index]+"'/></td>";
			}
			htmlContent = htmlContent + "</tr></table>";
			$("#menu_icon_dialog").html(htmlContent);
			openSelectDialog('menu_icon_dialog', 300, '选择图标');
		} 
	});
}

function openSelectDialog(divId, width, title){
	 $myDialog = $("#"+divId);
	 $myDialog.dialog({
        width: width,
        height: 'auto',
        modal: true,
        title: title
	 });
	 $myDialog.dialog("open");
}

function selectMenuIcon(iconPath, iconPathHiddenId){
	$("#"+iconPathHiddenId).val(iconPath);
	$("#choosed_"+iconPathHiddenId).attr("src", iconPath);
	$("#menu_icon_dialog").dialog("close");
}

function changeLabelNameOfURL(urlType){
	if(urlType == "url"){
		$("#type_script").hide();
		$("#type_url").show();
		$("#type_newTab").hide();	
		$("#url_tr").show();
		$("#list_view_tr").hide();
		$("#list_view_param_tr").hide();
		$("#process_tr").hide();
		$("#report_tr").hide();
		$("#report_param_tr").hide();
	}else if(urlType == "script"){
		$("#type_script").show();
		$("#type_url").hide();
		$("#type_newTab").hide();	
		$("#url_tr").show();
		$("#list_view_tr").hide();
		$("#list_view_param_tr").hide();
		$("#process_tr").hide();
		$("#report_tr").hide();
		$("#report_param_tr").hide();
	}else if(urlType == "newTab"){
		$("#type_script").hide();
		$("#type_url").hide();
		$("#type_newTab").show();
		$("#url_tr").show();
		$("#list_view_tr").hide();
		$("#list_view_param_tr").hide();
		$("#process_tr").hide();
		$("#report_tr").hide();
		$("#report_param_tr").hide();
	}else if(urlType == "listview"){
		$("#url_tr").hide();
		$("#process_tr").hide();
		$("#report_tr").hide();
		$("#report_param_tr").hide();
		$("#list_view_tr").show();
		$("#list_view_param_tr").show();
	}else if(urlType == "process"){
		$("#url_tr").hide();
		$("#list_view_tr").hide();
		$("#list_view_param_tr").hide();
		$("#process_tr").show();
		$("#report_tr").hide();
		$("#report_param_tr").hide();
	} else if(urlType == "report") {
		$("#report_tr").show();
		$("#report_param_tr").show();
		$("#url_tr").hide();
		$("#list_view_tr").hide();
		$("#list_view_param_tr").hide();
		$("#process_tr").hide();
	}
	
}
$(document).ready(function() {
	var JSONUsers = JSON.parse('${usersJSON}');
	setUserFullNames("menuUsersFullName",$('#menuUsers').val(),JSONUsers);
	
	var JSONRoles = JSON.parse('${rolesJSON}');
	setUserFullNames("menuRolesFullName",$('#menuRoles').val(),JSONRoles);
	
	var JSONGroups = JSON.parse('${groupsJSON}');
	setUserFullNames("menuGroupsFullName",$('#menuGroups').val(),JSONGroups);
	
	var JSONDepartments = JSON.parse('${departmentsJSON}');
	setUserFullNames("menuDepartmentsFullName",$('#menuDepartments').val(),JSONDepartments);
	
})
</script>
	<v:javascript formName="menu" staticJavascript="false" />
	<script type="text/javascript"
		src="<c:url value="/scripts/validator.jsp"/>"></script>