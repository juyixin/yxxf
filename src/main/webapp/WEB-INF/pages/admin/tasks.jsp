<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
		<spring:bind path="menu.*">
      		<%@ include file="/common/messages.jsp" %>
  		</spring:bind>
    	<table>
				<tr>
					<td align="center" style="padding-left:10px;">
		    			<span class="style16">Application will be logged out automatically after menu creation or update!</span>
		    		</td>
				</tr>
		</table>    
		<div class="table-create-user">
			<div class="span10" style="padding-left: 20PX;">
				<form:form id="menu" commandName="menu" method="post" action="bpm/admin/saveMenu" autocomplete="off" modelAttribute="menu" onsubmit="_execute('menu_form','');">
					<form:hidden path="id" id="id"/>
					<table width="100%" border="1" class="line-height46">
						<tr>
							<td width="271" height="40" >
				    			<eazytec:label styleClass="control-label style3 style18" key="menu.name"/>
				    		</td>
							<td width="436" class="uneditable-input1">
					        	<form:input path="name" id="name" class="medium"  depends="required"/>
		    	    		</td>
						</tr>
						<tr>
							<td width="271" height="40" >
				    			<eazytec:label styleClass="control-label style3 style18" key="menu.helpText"/>
				    		</td>
							<td width="436" class="uneditable-input1">
								<form:input id="helpText" path="helpText" class="medium"/>
							</td>
						</tr>
						<tr>
							<td width="271" height="40" >
				    			<eazytec:label styleClass="control-label style3 style18" key="menu.urlType"/>
				    		</td>
							<td width="436" class="uneditable-input1">
								<form:select path="urlType" id="urlType" name="urlType" class="medium" onChange="changeLabelNameOfURL(this.value);">
									<form:option value="url">URL</form:option>
									<form:option value="script">script</form:option>
								</form:select>
							</td>		
						</tr>
						<tr>
							<td width="271" height="40" >
								<div id="type_url">
									<eazytec:label styleClass="control-label style3 style18" key="menu.menuUrl"/>
								</div>
								<div id="type_script" class="displayNone">
									<eazytec:label styleClass="control-label style3 style18"  key="menu.menuScript"/>
								</div>
				    		</td>
							<td width="436" class="uneditable-input1">
								<form:input path="menuUrl" class="medium"/>
							</td>		
						</tr>
						<tr>
							<td width="271" height="40" >
				    			<eazytec:label styleClass="control-label style3 style18" key="menu.menuOrder"/>
				    		</td>
							<td width="436" class="uneditable-input1">
								<form:input path="menuOrder" class="medium"/>
							</td>		
						</tr>
						<tr>
							<td width="271" height="40" >
				    			<eazytec:label styleClass="control-label style3 style18" key="menu.isParent"/>
				    		</td>
							<td width="436" class="uneditable-input1">
								<form:checkbox id="isParent" path="isParent" />
							</td>
						</tr>
						<tr>
							<td width="271" height="40" >
				    			<eazytec:label styleClass="control-label style3 style18" key="menu.menuType"/>
				    		</td>
							<td width="436" class="uneditable-input1">
								<form:select path="menuType" name="menuType" id="menuType" class="medium">
									<option value="header" ${fn:contains(menu.menuType, 'header') ? 'selected' : ''}>Header Menu</option>
									<option value="top" ${fn:contains(menu.menuType, 'top') ? 'selected' : ''}>Top Menu</option>
									<option value="side" ${fn:contains(menu.menuType, 'side') ? 'selected' : ''}>Side Menu</option>                    
								</form:select>
		        			</td>
						</tr>
						<tr>
							<td width="271" height="40" >
				    			<eazytec:label styleClass="control-label style3 style18" key="menu.parentMenu"/>
				    		</td>
							<td width="436" class="uneditable-input1">
								<form:select path="parentMenu" name="parentMenu" id="parentMenu" class="medium">
									<option value="${menu.parentMenu.id}" ${fn:contains(menu.parentMenu, sideMenu.name) ? 'selected' : ''}>${menu.parentMenu.name}</option>
								</form:select>
							</td>
						</tr>
						<tr>
							<td width="271" height="40" >
				    			<eazytec:label styleClass="control-label style3 style18" key="menu.isGlobal"/>
				    		</td>
							<td width="436" class="uneditable-input1">
								<form:checkbox id="isGlobal" path="isGlobal"/>
							</td>
						</tr>
						<tr id="role_row" class="displayNone">
							<td width="271" height="40">
								<eazytec:label styleClass="control-label style3 style18" key="menu.roles"/>
							</td>
							<td>
								<input type="text" id="roles" name="roles" class="medium white-background" onclick="showRoleSelection('Role', 'multi' , 'roles', this, '');" readonly="readonly"/>
							</td>
						</tr> 
						<tr id="row_icon1">
							<td width="271" height="40">
				    			<eazytec:label styleClass="control-label style3 style18" key="menu.iconPath1"/>
				    		</td>
							<td width="436" class="uneditable-input1">
								<form:hidden path="iconPath1" />
								<table>
									<tr>
										<td width="30px"  height="10px" align="center" class="menu-icon">
											<img id="choosed_iconPath1" src="${menu.iconPath1}" />
										</td>
										<td align="center">
											<a href="javascript:void(0);" onClick="showMenuIcons('iconPath1');" ><eazytec:label styleClass="control-label style3 style18 cursor-pointer" key="menu.chooseIcon"/></a>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr id="row_icon2">
							<td width="271" height="40" >
				    			<eazytec:label styleClass="control-label style3 style18" key="menu.iconPath2"/>
				    		</td>
							<td width="436" class="uneditable-input1">
								<form:hidden path="iconPath2" />
								 <table>
									<tr>
										<td width="30px"  height="10px" align="center" class="menu-icon">
											<img id="choosed_iconPath2" src="${menu.iconPath2}" />
										</td>
										<td align="center">
											<a href="javascript:void(0);" onClick="showMenuIcons('iconPath2');" ><eazytec:label styleClass="control-label style3 style18 cursor-pointer"  key="menu.chooseIcon"/></a>
										</td>
									</tr>
								</table> 
							</td>
						</tr>
						<tr>
							<td width="271" height="40" >
				    			<eazytec:label styleClass="control-label style3 style18" key="menu.active"/>
				    		</td>
							<td width="436" class="uneditable-input1">
								<form:checkbox id="isActive" path="active"/>
							</td>
						</tr>
						<tr>
							<td width="271" height="40" >
				    			<eazytec:label styleClass="control-label style3 style18" key="menu.description"/>
				    		</td>
							<td width="436" class="uneditable-input1">
								<form:textarea path="description" rows="4" cols="45" class="medium"/>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center"><button type="submit" class="btn btn-primary normalButton padding0 line-height0" name="save" onclick="bCancel=false;" id="saveButton" >
			        		 <fmt:message key="button.save"/>
			    		</button>
			    		</td>
		    			</tr>
		    			<tr>
							<td colspan="2" align="center">
			    				<span class="style16">Application will be logged out automatically after menu creation or update!</span>
			    			</td>
		    			</tr>
		    			<tr height="8px">
				    	</tr>
					</table>  
				</form:form> 
			</div>
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
	var urlType = document.getElementById("urlType");
	var menuTypeValue = menuType.options[menuType.selectedIndex].value;
	var isParent= document.getElementById('isParent').checked;
	if(isParent){
		if(menuTypeValue == "side"){
			document.getElementById('parentMenu').disabled = false;
			<c:forEach items="${availableMenus}" var="menu">
			if("${menu.isParent}" == "true" && ("${menu.menuType}" == "top" || "${menu.menuType}" == "header")){
				menus["${menu.id}"] = "${menu.name}";
			}
			</c:forEach>;
		}else{
			menus=[];
			document.getElementById('parentMenu').disabled = true;
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
	   		    content:"Please check the parent menu first",
	   		    type:'info'
	   		});
		}
		$("#row_icon1").hide();
		$("#row_icon2").hide();
	}
	changeLabelNameOfURL(urlType)
}

$(function () {
	$('#roles').val('${roles}');
	var parentMenu = document.getElementById("parentMenu");
	var parentMenuValue = parentMenu.options[parentMenu.selectedIndex].value;
	if(document.getElementById("id").value == ''){
		$('#isParent').prop('checked', true);
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
	    } else {
	    	$("#role_row").show();
	    }
	});
	var isGlobalChecked = $("#isGlobal").is(':checked');
	if(!isGlobalChecked){
		$("#role_row").show();
	}
}); 

$("#isParent").click(function() {
	var $this = $(this);
    if ($this.is(':checked')) {
    	$('#menuType').empty();
    	$('#menuType').append(new Option('Header Menu', 'header', true, true));
    	$('#menuType').append(new Option('Top Menu', 'top', true, true));
    	$('#menuType').append(new Option('Side Menu', 'side', true, true)); 
    	$("#row_icon1").show();
		$("#row_icon2").show();
    } else {
    	$('#menuType').empty();
    	$('#menuType').append(new Option('Side Menu', 'side', true, true));
    	$("#row_icon1").hide();
		$("#row_icon2").hide();
    }
    getMenus();	
	for(var i in menus){
		$('#parentMenu').append( $('<option> </option>').val(i).html(menus[i]) );   
	} 
});
	
	
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
			var htmlContent = "<table  width='100%'><tr>";
			for(var index = 0; index < response.length; index++){
				if(index % 5 == 0){
					htmlContent = htmlContent + "</tr><tr>";
				}
				htmlContent = htmlContent + "<td align='center'><img class='cursor-pointer' onClick='selectMenuIcon(\""+response[index]+"\", \""+iconPath+"\");' style='padding:5px;border:1px solid gray;' src='"+response[index]+"'/></td>";
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
	if(urlType == "script"){
		$("#type_script").show();
		$("#type_url").hide();
	}else{
		$("#type_script").hide();
		$("#type_url").show();
	}
}
</script>
<v:javascript formName="menu" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
